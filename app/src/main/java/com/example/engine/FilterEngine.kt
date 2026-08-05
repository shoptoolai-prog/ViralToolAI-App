package com.example.engine

import android.content.Context
import android.graphics.*
import android.util.LruCache
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Stack
import kotlin.math.*

// ============================================================================
// MASTER PHASE 10.4 — PROFESSIONAL GPU FILTER ENGINE (FilterEngine.kt)
// ============================================================================

enum class FilterLayerType {
    PRESET_3D_LUT,
    PNG_LUT,
    COLOR_MATRIX,
    EXPOSURE_CONTRAST,
    TEMPERATURE_TINT,
    HIGHLIGHTS_SHADOWS,
    HSL_ADJUSTMENT,
    SPLIT_TONING,
    COLOR_WHEELS,
    VIGNETTE_GRAIN,
    GLOW_BLUR,
    SHARPEN
}

data class HSLChannel(
    val hueShift: Float = 0f,      // -180f to +180f
    val saturation: Float = 0f,    // -100f to +100f
    val luminance: Float = 0f      // -100f to +100f
)

data class HSLAdjustment(
    val red: HSLChannel = HSLChannel(),
    val orange: HSLChannel = HSLChannel(),
    val yellow: HSLChannel = HSLChannel(),
    val green: HSLChannel = HSLChannel(),
    val aqua: HSLChannel = HSLChannel(),
    val blue: HSLChannel = HSLChannel(),
    val purple: HSLChannel = HSLChannel(),
    val magenta: HSLChannel = HSLChannel()
)

data class ColorWheelValue(
    val red: Float = 0f,    // -1f to 1f
    val green: Float = 0f,
    val blue: Float = 0f,
    val master: Float = 0f
)

data class ColorWheels(
    val lift: ColorWheelValue = ColorWheelValue(),     // Shadows
    val gamma: ColorWheelValue = ColorWheelValue(),    // Midtones
    val gain: ColorWheelValue = ColorWheelValue()      // Highlights
)

data class FilterLayer(
    val id: String = "layer_${System.currentTimeMillis()}_${(100..999).random()}",
    val name: String,
    val type: FilterLayerType,
    val enabled: Boolean = true,
    var intensity: Float = 1.0f, // 0.0f to 1.0f
    
    // Shader & LUT attributes
    val lutFilePath: String? = null,
    val lut3DSize: Int = 33,
    val lutTable3D: Array<FloatArray>? = null, // N x N x N 3D LUT buffer
    
    // Adjustment Parameters
    var brightness: Float = 0.0f,   // -1.0f to 1.0f
    var contrast: Float = 1.0f,     // 0.0f to 2.0f
    var exposure: Float = 0.0f,     // -2.0f to 2.0f
    var saturation: Float = 1.0f,   // 0.0f to 2.0f
    var temperature: Float = 0.0f,  // -100f to 100f (Warm / Cool)
    var tint: Float = 0.0f,         // -100f to 100f (Green / Magenta)
    var gamma: Float = 1.0f,        // 0.2f to 3.0f
    
    var highlights: Float = 0.0f,   // -1.0f to 1.0f
    var shadows: Float = 0.0f,      // -1.0f to 1.0f
    var fade: Float = 0.0f,         // 0.0f to 1.0f
    var sharpen: Float = 0.0f,      // 0.0f to 1.0f
    var blurRadius: Float = 0.0f,   // 0.0f to 25.0f
    var filmGrain: Float = 0.0f,    // 0.0f to 1.0f
    var vignette: Float = 0.0f,     // 0.0f to 1.0f
    var glow: Float = 0.0f,         // 0.0f to 1.0f
    
    // Complex Color Adjustments
    var hsl: HSLAdjustment = HSLAdjustment(),
    var highlightTint: Int = Color.parseColor("#FFFFD700"), // Gold Highlight
    var shadowTint: Int = Color.parseColor("#FF000080"),    // Navy Shadow
    var splitToneBalance: Float = 0.0f,                     // -1.0f to 1.0f
    var colorWheels: ColorWheels = ColorWheels()
)

data class FilterStackState(
    val layers: List<FilterLayer> = emptyList(),
    val globalOpacity: Float = 1.0f,
    val compareOriginal: Boolean = false
)

// ============================================================================
// 1. LUT MANAGER (3D .cube and 2D .png LUT parser)
// ============================================================================
class LUTManager private constructor(private val context: Context) {
    private val lutCache = LruCache<String, Array<FloatArray>>(20)

    fun parseCubeLUT(inputStream: InputStream): Array<FloatArray>? {
        return try {
            val reader = BufferedReader(InputStreamReader(inputStream))
            var size = 33
            val table = mutableListOf<FloatArray>()
            
            reader.forEachLine { line ->
                val trimmed = line.trim()
                if (trimmed.startsWith("LUT_3D_SIZE")) {
                    val parts = trimmed.split("\\s+".toRegex())
                    if (parts.size >= 2) {
                        size = parts[1].toIntOrNull() ?: 33
                    }
                } else if (trimmed.isNotEmpty() && !trimmed.startsWith("#") && !trimmed.startsWith("TITLE")) {
                    val rgb = trimmed.split("\\s+".toRegex()).mapNotNull { it.toFloatOrNull() }
                    if (rgb.size == 3) {
                        table.add(floatArrayOf(rgb[0], rgb[1], rgb[2]))
                    }
                }
            }
            table.toTypedArray()
        } catch (e: Exception) {
            null
        }
    }

    fun apply3DLUTToBitmap(src: Bitmap, lutTable: Array<FloatArray>, lutSize: Int, intensity: Float): Bitmap {
        if (intensity <= 0.001f) return src
        val width = src.width
        val height = src.height
        val pixels = IntArray(width * height)
        src.getPixels(pixels, 0, width, 0, 0, width, height)

        val size = lutSize.coerceAtLeast(2)
        val maxIndex = size - 1

        for (i in pixels.indices) {
            val c = pixels[i]
            val r = (c shr 16 and 0xFF) / 255.0f
            val g = (c shr 8 and 0xFF) / 255.0f
            val b = (c and 0xFF) / 255.0f

            val rIdx = (r * maxIndex).coerceIn(0f, maxIndex.toFloat())
            val gIdx = (g * maxIndex).coerceIn(0f, maxIndex.toFloat())
            val bIdx = (b * maxIndex).coerceIn(0f, maxIndex.toFloat())

            val rFloor = rIdx.toInt().coerceIn(0, size - 1)
            val gFloor = gIdx.toInt().coerceIn(0, size - 1)
            val bFloor = bIdx.toInt().coerceIn(0, size - 1)

            val lutIndex = (bFloor * size * size) + (gFloor * size) + rFloor
            if (lutIndex in lutTable.indices) {
                val lutR = lutTable[lutIndex][0]
                val lutG = lutTable[lutIndex][1]
                val lutB = lutTable[lutIndex][2]

                // Blend with original intensity
                val finalR = ((r * (1f - intensity) + lutR * intensity) * 255f).toInt().coerceIn(0, 255)
                val finalG = ((g * (1f - intensity) + lutG * intensity) * 255f).toInt().coerceIn(0, 255)
                val finalB = ((b * (1f - intensity) + lutB * intensity) * 255f).toInt().coerceIn(0, 255)

                pixels[i] = (c and 0xFF000000.toInt()) or (finalR shl 16) or (finalG shl 8) or finalB
            }
        }

        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        result.setPixels(pixels, 0, width, 0, 0, width, height)
        return result
    }

    companion object {
        @Volatile private var instance: LUTManager? = null
        fun getInstance(context: Context): LUTManager {
            return instance ?: synchronized(this) {
                instance ?: LUTManager(context.applicationContext).also { instance = it }
            }
        }
    }
}

// ============================================================================
// 2. GPU SHADER MANAGER (Matrix & Bitmap Shader Computations)
// ============================================================================
class GPUShaderManager {

    fun createColorMatrixForLayer(layer: FilterLayer): ColorMatrix {
        val cm = ColorMatrix()

        // Exposure & Brightness
        val exposureScale = 2.0.pow(layer.exposure.toDouble()).toFloat()
        val brightnessOffset = layer.brightness * 255f
        
        // Contrast
        val contrastScale = layer.contrast
        val contrastTranslate = (-0.5f * contrastScale + 0.5f) * 255f

        // Saturation
        val satMatrix = ColorMatrix()
        satMatrix.setSaturation(layer.saturation)

        // Temperature & Tint
        val tempR = if (layer.temperature > 0) 1.0f + (layer.temperature / 200f) else 1.0f
        val tempB = if (layer.temperature < 0) 1.0f - (layer.temperature / 200f) else 1.0f
        val tintG = if (layer.tint < 0) 1.0f + (abs(layer.tint) / 200f) else 1.0f

        val basicMatrix = ColorMatrix(floatArrayOf(
            exposureScale * contrastScale * tempR, 0f, 0f, 0f, brightnessOffset + contrastTranslate,
            0f, exposureScale * contrastScale * tintG, 0f, 0f, brightnessOffset + contrastTranslate,
            0f, 0f, exposureScale * contrastScale * tempB, 0f, brightnessOffset + contrastTranslate,
            0f, 0f, 0f, 1f, 0f
        ))

        cm.postConcat(basicMatrix)
        cm.postConcat(satMatrix)
        return cm
    }

    fun applyVignetteAndGrain(canvas: Canvas, bitmapWidth: Int, bitmapHeight: Int, vignette: Float, grain: Float) {
        if (vignette > 0.01f) {
            val cx = bitmapWidth / 2f
            val cy = bitmapHeight / 2f
            val radius = max(cx, cy) * 1.2f
            val gradient = RadialGradient(
                cx, cy, radius,
                intArrayOf(Color.TRANSPARENT, Color.argb((vignette * 200).toInt().coerceIn(0, 240), 0, 0, 0)),
                floatArrayOf(0.4f, 1.0f),
                Shader.TileMode.CLAMP
            )
            val p = Paint(Paint.ANTI_ALIAS_FLAG).apply { shader = gradient }
            canvas.drawRect(0f, 0f, bitmapWidth.toFloat(), bitmapHeight.toFloat(), p)
        }

        if (grain > 0.01f) {
            val grainPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                alpha = (grain * 45).toInt().coerceIn(0, 90)
            }
            val numDots = ((bitmapWidth * bitmapHeight) / 800 * grain).toInt().coerceAtMost(3000)
            for (i in 0 until numDots) {
                val rx = (0..bitmapWidth).random().toFloat()
                val ry = (0..bitmapHeight).random().toFloat()
                canvas.drawCircle(rx, ry, 1.2f, grainPaint)
            }
        }
    }
}

// ============================================================================
// 3. FILTER RENDERER (Sequential Stack Processor)
// ============================================================================
class FilterRenderer(private val context: Context) {
    private val gpuShaderManager = GPUShaderManager()
    private val lutManager = LUTManager.getInstance(context)

    fun processFilterStack(sourceBitmap: Bitmap, stack: List<FilterLayer>): Bitmap {
        if (stack.isEmpty() || stack.none { it.enabled }) return sourceBitmap

        var currentBitmap = sourceBitmap.copy(Bitmap.Config.ARGB_8888, true)

        for (layer in stack) {
            if (!layer.enabled || layer.intensity <= 0.001f) continue

            // 1. Color Matrix Transformations
            val cm = gpuShaderManager.createColorMatrixForLayer(layer)
            val canvas = Canvas(currentBitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                colorFilter = ColorMatrixColorFilter(cm)
            }
            val tempBitmap = Bitmap.createBitmap(currentBitmap.width, currentBitmap.height, Bitmap.Config.ARGB_8888)
            val tempCanvas = Canvas(tempBitmap)
            tempCanvas.drawBitmap(currentBitmap, 0f, 0f, paint)

            // 2. 3D LUT blending if available
            if (layer.lutTable3D != null) {
                currentBitmap = lutManager.apply3DLUTToBitmap(tempBitmap, layer.lutTable3D, layer.lut3DSize, layer.intensity)
            } else {
                currentBitmap = tempBitmap
            }

            // 3. Vignette & Grain Effects
            if (layer.vignette > 0f || layer.filmGrain > 0f) {
                val fxCanvas = Canvas(currentBitmap)
                gpuShaderManager.applyVignetteAndGrain(fxCanvas, currentBitmap.width, currentBitmap.height, layer.vignette, layer.filmGrain)
            }
        }

        return currentBitmap
    }
}

// ============================================================================
// 4. PREVIEW RENDERER & EXPORT RENDERER
// ============================================================================
class PreviewRenderer(private val filterRenderer: FilterRenderer) {
    private val proxyCache = LruCache<String, Bitmap>(10)

    fun renderProxyPreview(sourceBitmap: Bitmap, stack: List<FilterLayer>, targetWidth: Int = 640): Bitmap {
        val aspect = sourceBitmap.height.toFloat() / sourceBitmap.width.toFloat()
        val targetHeight = (targetWidth * aspect).toInt().coerceAtLeast(1)

        val proxySource = Bitmap.createScaledBitmap(sourceBitmap, targetWidth, targetHeight, true)
        return filterRenderer.processFilterStack(proxySource, stack)
    }
}

class ExportRenderer(private val filterRenderer: FilterRenderer) {

    fun exportFullResolution(
        sourceBitmap: Bitmap,
        stack: List<FilterLayer>,
        targetResolution: ExportResolution
    ): Bitmap {
        val finalSource = if (targetResolution == ExportResolution.ORIGINAL) {
            sourceBitmap
        } else {
            Bitmap.createScaledBitmap(sourceBitmap, targetResolution.width, targetResolution.height, true)
        }
        return filterRenderer.processFilterStack(finalSource, stack)
    }
}

// ============================================================================
// 5. MASTER FILTER ENGINE (Orchestrator, Undo/Redo, Realtime Flow)
// ============================================================================
class FilterEngine private constructor(private val context: Context) {

    private val filterRenderer = FilterRenderer(context)
    val previewRenderer = PreviewRenderer(filterRenderer)
    val exportRenderer = ExportRenderer(filterRenderer)

    private val _stackState = MutableStateFlow(FilterStackState())
    val stackState: StateFlow<FilterStackState> = _stackState.asStateFlow()

    private val undoStack = Stack<List<FilterLayer>>()
    private val redoStack = Stack<List<FilterLayer>>()

    // STARTER PRESETS CATALOG
    val PRESET_FILTERS = listOf(
        FilterLayer(name = "Original", type = FilterLayerType.COLOR_MATRIX, intensity = 1.0f),
        FilterLayer(name = "Cinematic Teal & Orange", type = FilterLayerType.EXPOSURE_CONTRAST, temperature = 15f, contrast = 1.25f, exposure = 0.1f, saturation = 1.15f, vignette = 0.35f, filmGrain = 0.15f),
        FilterLayer(name = "HDR Vibrant High Contrast", type = FilterLayerType.EXPOSURE_CONTRAST, contrast = 1.4f, exposure = 0.2f, saturation = 1.35f, highlights = -0.2f, shadows = 0.2f),
        FilterLayer(name = "Vintage 1980s Film", type = FilterLayerType.EXPOSURE_CONTRAST, temperature = 25f, tint = -10f, contrast = 0.9f, fade = 0.2f, filmGrain = 0.4f, vignette = 0.25f),
        FilterLayer(name = "Moody Dark Emerald", type = FilterLayerType.EXPOSURE_CONTRAST, temperature = -20f, tint = -15f, contrast = 1.3f, exposure = -0.15f, saturation = 0.85f, vignette = 0.5f),
        FilterLayer(name = "Monochrome Noir", type = FilterLayerType.EXPOSURE_CONTRAST, saturation = 0.0f, contrast = 1.45f, exposure = 0.05f, filmGrain = 0.3f, vignette = 0.4f),
        FilterLayer(name = "India Sunset Gold", type = FilterLayerType.EXPOSURE_CONTRAST, temperature = 35f, tint = 10f, contrast = 1.15f, saturation = 1.25f, vignette = 0.2f),
        FilterLayer(name = "Cyberpunk Neon Glow", type = FilterLayerType.EXPOSURE_CONTRAST, temperature = -30f, tint = 25f, contrast = 1.35f, saturation = 1.4f, glow = 0.25f, vignette = 0.3f)
    )

    init {
        // Default with Cinematic Teal & Orange
        pushStack(listOf(PRESET_FILTERS[1]))
    }

    private fun saveUndoState() {
        undoStack.push(_stackState.value.layers.map { it.copy() })
        redoStack.clear()
    }

    fun addLayer(layer: FilterLayer) {
        saveUndoState()
        val current = _stackState.value.layers.toMutableList()
        current.add(layer)
        _stackState.value = _stackState.value.copy(layers = current)
    }

    fun updateLayer(updatedLayer: FilterLayer) {
        val current = _stackState.value.layers.toMutableList()
        val index = current.indexOfFirst { it.id == updatedLayer.id }
        if (index != -1) {
            current[index] = updatedLayer
            _stackState.value = _stackState.value.copy(layers = current)
        }
    }

    fun removeLayer(layerId: String) {
        saveUndoState()
        val current = _stackState.value.layers.filter { it.id != layerId }
        _stackState.value = _stackState.value.copy(layers = current)
    }

    fun reorderLayers(fromIndex: Int, toIndex: Int) {
        if (fromIndex in _stackState.value.layers.indices && toIndex in _stackState.value.layers.indices) {
            saveUndoState()
            val list = _stackState.value.layers.toMutableList()
            val item = list.removeAt(fromIndex)
            list.add(toIndex, item)
            _stackState.value = _stackState.value.copy(layers = list)
        }
    }

    fun duplicateLayer(layerId: String) {
        saveUndoState()
        val current = _stackState.value.layers.toMutableList()
        val layer = current.find { it.id == layerId } ?: return
        val copy = layer.copy(id = "layer_${System.currentTimeMillis()}_dup", name = "${layer.name} (Copy)")
        current.add(copy)
        _stackState.value = _stackState.value.copy(layers = current)
    }

    fun toggleLayerEnabled(layerId: String) {
        val current = _stackState.value.layers.toMutableList()
        val index = current.indexOfFirst { it.id == layerId }
        if (index != -1) {
            val layer = current[index]
            current[index] = layer.copy(enabled = !layer.enabled)
            _stackState.value = _stackState.value.copy(layers = current)
        }
    }

    fun updateLayerIntensity(layerId: String, intensity: Float) {
        val current = _stackState.value.layers.toMutableList()
        val index = current.indexOfFirst { it.id == layerId }
        if (index != -1) {
            current[index] = current[index].copy(intensity = intensity)
            _stackState.value = _stackState.value.copy(layers = current)
        }
    }

    fun setCompareOriginal(compare: Boolean) {
        _stackState.value = _stackState.value.copy(compareOriginal = compare)
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            redoStack.push(_stackState.value.layers.map { it.copy() })
            val previous = undoStack.pop()
            _stackState.value = _stackState.value.copy(layers = previous)
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            undoStack.push(_stackState.value.layers.map { it.copy() })
            val next = redoStack.pop()
            _stackState.value = _stackState.value.copy(layers = next)
        }
    }

    fun resetStack() {
        saveUndoState()
        _stackState.value = _stackState.value.copy(layers = emptyList())
    }

    fun pushStack(layers: List<FilterLayer>) {
        saveUndoState()
        _stackState.value = _stackState.value.copy(layers = layers)
    }

    companion object {
        @Volatile private var instance: FilterEngine? = null
        fun getInstance(context: Context): FilterEngine {
            return instance ?: synchronized(this) {
                instance ?: FilterEngine(context.applicationContext).also { instance = it }
            }
        }
    }
}
