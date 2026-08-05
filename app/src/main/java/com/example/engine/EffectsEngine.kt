package com.example.engine

import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.LruCache
import kotlinx.coroutines.flow.*
import java.util.Stack
import kotlin.math.*
import kotlin.random.Random

// ============================================================================
// MASTER PHASE E-2 — PROFESSIONAL GPU EFFECTS ENGINE (EffectsEngine.kt)
// ============================================================================

enum class EffectCategory(val displayName: String) {
    WEATHER("Weather & Nature"),
    LIGHT("Light & Glow"),
    PARTICLES("Particle System"),
    GLITCH("Glitch & Distortion"),
    FILM("Film & Vintage"),
    CINEMATIC("Cinematic & Lens"),
    NEON_CYBER("Neon & Cyberpunk"),
    ALL("All Effects")
}

enum class EffectBlendMode {
    NORMAL,
    ADD,
    MULTIPLY,
    SCREEN,
    OVERLAY
}

enum class EffectType {
    // Weather
    WEATHER_RAIN,
    WEATHER_HEAVY_RAIN,
    WEATHER_SNOW,
    WEATHER_FOG,
    WEATHER_LIGHTNING,
    WEATHER_SUN_RAYS,
    WEATHER_WATER_DROPS,

    // Light
    LIGHT_LENS_FLARE,
    LIGHT_LIGHT_LEAK,
    LIGHT_GOLDEN_GLOW,
    LIGHT_SUN_GLOW,
    LIGHT_RGB_GLOW,
    LIGHT_BLOOM,
    LIGHT_BOKEH,

    // Particles
    PARTICLE_SPARKS,
    PARTICLE_FIRE,
    PARTICLE_SMOKE,
    PARTICLE_CONFETTI,
    PARTICLE_STARS,
    PARTICLE_BUBBLES,
    PARTICLE_LEAVES,

    // Glitch
    GLITCH_RGB_SPLIT,
    GLITCH_DIGITAL_NOISE,
    GLITCH_SCANLINE,
    GLITCH_TV_CRT,
    GLITCH_WAVE_DISTORTION,

    // Film
    FILM_GRAIN_PRO,
    FILM_BURN,
    FILM_DUST_SCRATCH,
    FILM_VINTAGE_CAM
}

data class EffectLayer(
    val id: String = "fx_${System.currentTimeMillis()}_${(100..999).random()}",
    val name: String,
    val type: EffectType,
    val category: EffectCategory,
    val enabled: Boolean = true,
    var intensity: Float = 1.0f,  // 0.0 to 1.0
    var opacity: Float = 1.0f,    // 0.0 to 1.0
    var scale: Float = 1.0f,      // 0.5 to 3.0
    var rotation: Float = 0.0f,   // 0 to 360 deg
    var blendMode: EffectBlendMode = EffectBlendMode.NORMAL,
    var speed: Float = 1.0f,
    var primaryColorHex: String = "#38E8A5"
)

data class EffectStackState(
    val effects: List<EffectLayer> = emptyList(),
    val isPreviewActive: Boolean = true
)

// ============================================================================
// 1. BLEND ENGINE
// ============================================================================
class BlendEngine {
    fun getPorterDuffMode(blendMode: EffectBlendMode): PorterDuff.Mode {
        return when (blendMode) {
            EffectBlendMode.NORMAL -> PorterDuff.Mode.SRC_OVER
            EffectBlendMode.ADD -> PorterDuff.Mode.ADD
            EffectBlendMode.MULTIPLY -> PorterDuff.Mode.MULTIPLY
            EffectBlendMode.SCREEN -> PorterDuff.Mode.SCREEN
            EffectBlendMode.OVERLAY -> PorterDuff.Mode.OVERLAY
        }
    }

    fun applyBlend(baseCanvas: Canvas, effectBitmap: Bitmap, blendMode: EffectBlendMode, opacity: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            xfermode = PorterDuffXfermode(getPorterDuffMode(blendMode))
            alpha = (opacity * 255).toInt().coerceIn(0, 255)
        }
        baseCanvas.drawBitmap(effectBitmap, 0f, 0f, paint)
    }
}

// ============================================================================
// 2. PARTICLE ENGINE (Real-Time Physics Procedural Particles)
// ============================================================================
class ParticleEngine {

    fun renderParticles(
        effectType: EffectType,
        targetWidth: Int,
        targetHeight: Int,
        timeMs: Long,
        intensity: Float,
        speed: Float,
        colorHex: String
    ): Bitmap {
        val bmp = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val timeSec = (timeMs / 1000.0f) * speed
        val baseColor = Color.parseColor(colorHex)

        when (effectType) {
            EffectType.PARTICLE_SPARKS -> {
                val sparkCount = (100 * intensity).toInt().coerceAtLeast(10)
                paint.color = Color.YELLOW
                for (i in 0 until sparkCount) {
                    val rand = Random(i.toLong())
                    val x = (rand.nextFloat() * targetWidth + timeSec * 300f * (rand.nextFloat() - 0.5f)) % targetWidth
                    val y = (targetHeight - (timeSec * 400f * rand.nextFloat() % targetHeight))
                    val r = rand.nextFloat() * 4f + 2f
                    paint.alpha = ((1.0f - (y / targetHeight)) * 255 * rand.nextFloat()).toInt().coerceIn(0, 255)
                    canvas.drawCircle(abs(x), abs(y), r, paint)
                }
            }
            EffectType.PARTICLE_FIRE -> {
                val flameCount = (80 * intensity).toInt().coerceAtLeast(10)
                for (i in 0 until flameCount) {
                    val rand = Random(i * 17L)
                    val cx = (targetWidth / 2f) + (rand.nextFloat() - 0.5f) * targetWidth * 0.6f
                    val y = targetHeight - ((timeSec * 250f + i * 10f) % targetHeight)
                    val r = (rand.nextFloat() * 25f + 10f) * (1f - y / targetHeight)
                    val colors = intArrayOf(Color.YELLOW, Color.RED, Color.TRANSPARENT)
                    val shader = RadialGradient(cx, y, r.coerceAtLeast(1f), colors, floatArrayOf(0f, 0.7f, 1f), Shader.TileMode.CLAMP)
                    paint.shader = shader
                    paint.alpha = (intensity * 200).toInt().coerceIn(0, 255)
                    canvas.drawCircle(cx, y, r, paint)
                }
            }
            EffectType.PARTICLE_SMOKE -> {
                paint.color = Color.argb((intensity * 60).toInt(), 200, 200, 200)
                val puffCount = 15
                for (i in 0 until puffCount) {
                    val px = (targetWidth * 0.5f) + sin(timeSec + i) * 120f
                    val py = targetHeight - ((timeSec * 80f + i * 60f) % targetHeight)
                    val radius = 60f + i * 15f
                    canvas.drawCircle(px, py, radius, paint)
                }
            }
            EffectType.PARTICLE_CONFETTI -> {
                val colors = intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN)
                val count = (120 * intensity).toInt()
                for (i in 0 until count) {
                    val rand = Random(i * 31L)
                    paint.color = colors[i % colors.size]
                    val x = (rand.nextFloat() * targetWidth + sin(timeSec + i) * 50f) % targetWidth
                    val y = (timeSec * 200f * rand.nextFloat() + i * 20f) % targetHeight
                    val rot = timeSec * 180f + i * 45f
                    canvas.save()
                    canvas.rotate(rot, x, y)
                    canvas.drawRect(x - 8f, y - 4f, x + 8f, y + 4f, paint)
                    canvas.restore()
                }
            }
            EffectType.PARTICLE_STARS -> {
                paint.color = Color.WHITE
                val starCount = (70 * intensity).toInt()
                for (i in 0 until starCount) {
                    val rand = Random(i * 43L)
                    val x = rand.nextFloat() * targetWidth
                    val y = rand.nextFloat() * targetHeight
                    val twinkle = sin(timeSec * 5f + i).absoluteValue
                    paint.alpha = (twinkle * 255 * intensity).toInt().coerceIn(0, 255)
                    canvas.drawCircle(x, y, rand.nextFloat() * 3.5f + 1f, paint)
                }
            }
            EffectType.PARTICLE_BUBBLES -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 3f
                paint.color = Color.argb(180, 200, 240, 255)
                val bubbleCount = (30 * intensity).toInt()
                for (i in 0 until bubbleCount) {
                    val rand = Random(i * 53L)
                    val x = (rand.nextFloat() * targetWidth + cos(timeSec + i) * 40f) % targetWidth
                    val y = targetHeight - ((timeSec * 120f * rand.nextFloat() + i * 40f) % targetHeight)
                    val r = rand.nextFloat() * 18f + 6f
                    canvas.drawCircle(x, y, r, paint)
                }
            }
            EffectType.PARTICLE_LEAVES -> {
                paint.color = Color.parseColor("#E67E22")
                val count = (40 * intensity).toInt()
                for (i in 0 until count) {
                    val rand = Random(i * 61L)
                    val x = (rand.nextFloat() * targetWidth + sin(timeSec * 2f + i) * 100f) % targetWidth
                    val y = (timeSec * 150f + i * 30f) % targetHeight
                    canvas.save()
                    canvas.rotate(timeSec * 90f + i * 30f, x, y)
                    val path = Path().apply {
                        moveTo(x, y - 10f)
                        quadTo(x + 10f, y, x, y + 10f)
                        quadTo(x - 10f, y, x, y - 10f)
                    }
                    canvas.drawPath(path, paint)
                    canvas.restore()
                }
            }
            else -> {}
        }

        return bmp
    }
}

// ============================================================================
// 3. SHADER & GPU PROCEDURAL EFFECTS ENGINE
// ============================================================================
class ShaderEngine {

    fun renderShaderEffect(
        effectType: EffectType,
        baseFrame: Bitmap,
        timeMs: Long,
        intensity: Float,
        speed: Float
    ): Bitmap {
        val w = baseFrame.width
        val h = baseFrame.height
        val output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        canvas.drawBitmap(baseFrame, 0f, 0f, null)

        val timeSec = (timeMs / 1000.0f) * speed

        when (effectType) {
            // WEATHER
            EffectType.WEATHER_RAIN, EffectType.WEATHER_HEAVY_RAIN -> {
                val dropCount = if (effectType == EffectType.WEATHER_HEAVY_RAIN) 300 else 120
                paint.color = Color.argb((intensity * 160).toInt(), 200, 220, 255)
                paint.strokeWidth = if (effectType == EffectType.WEATHER_HEAVY_RAIN) 3f else 1.8f
                for (i in 0 until dropCount) {
                    val rand = Random(i * 19L)
                    val x = (rand.nextFloat() * w + timeSec * 400f) % w
                    val y = (timeSec * 1400f * rand.nextFloat() + i * 20f) % h
                    val len = 25f + rand.nextFloat() * 30f
                    canvas.drawLine(x, y, x - 8f, y + len, paint)
                }
            }
            EffectType.WEATHER_SNOW -> {
                val flakeCount = (150 * intensity).toInt()
                paint.color = Color.WHITE
                for (i in 0 until flakeCount) {
                    val rand = Random(i * 23L)
                    val x = (rand.nextFloat() * w + sin(timeSec + i) * 60f) % w
                    val y = (timeSec * 180f * rand.nextFloat() + i * 15f) % h
                    val r = rand.nextFloat() * 3.5f + 1f
                    paint.alpha = (rand.nextFloat() * 200 + 55).toInt()
                    canvas.drawCircle(x, y, r, paint)
                }
            }
            EffectType.WEATHER_FOG -> {
                val fogPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.argb((intensity * 90).toInt(), 220, 225, 235)
                }
                val fogY = h * 0.4f + sin(timeSec) * 40f
                val gradient = LinearGradient(0f, fogY, 0f, h.toFloat(), Color.TRANSPARENT, Color.argb((intensity * 120).toInt(), 240, 240, 250), Shader.TileMode.CLAMP)
                fogPaint.shader = gradient
                canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), fogPaint)
            }
            EffectType.WEATHER_LIGHTNING -> {
                if (Random(timeMs / 400).nextFloat() < (0.25f * intensity)) {
                    val flashPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = Color.argb(140, 255, 255, 255)
                    }
                    canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), flashPaint)

                    paint.color = Color.WHITE
                    paint.strokeWidth = 6f
                    val path = Path().apply {
                        moveTo(w * 0.5f, 0f)
                        lineTo(w * 0.45f, h * 0.3f)
                        lineTo(w * 0.55f, h * 0.35f)
                        lineTo(w * 0.4f, h * 0.7f)
                        lineTo(w * 0.52f, h * 0.75f)
                        lineTo(w * 0.48f, h.toFloat())
                    }
                    canvas.drawPath(path, paint)
                }
            }

            // LIGHT & GLOW
            EffectType.LIGHT_LENS_FLARE -> {
                val cx = w * 0.3f + sin(timeSec * 0.5f) * w * 0.2f
                val cy = h * 0.3f + cos(timeSec * 0.5f) * h * 0.15f
                val flarePaint = Paint(Paint.ANTI_ALIAS_FLAG)
                val gradient = RadialGradient(cx, cy, 220f * intensity, intArrayOf(Color.WHITE, Color.argb(160, 255, 200, 100), Color.TRANSPARENT), floatArrayOf(0f, 0.4f, 1f), Shader.TileMode.CLAMP)
                flarePaint.shader = gradient
                canvas.drawCircle(cx, cy, 220f * intensity, flarePaint)
            }
            EffectType.LIGHT_LIGHT_LEAK -> {
                val leakPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                val gradient = LinearGradient(0f, 0f, w * 0.8f, h * 0.8f, Color.argb((intensity * 180).toInt(), 255, 120, 50), Color.TRANSPARENT, Shader.TileMode.CLAMP)
                leakPaint.shader = gradient
                canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), leakPaint)
            }
            EffectType.LIGHT_GOLDEN_GLOW -> {
                val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.argb((intensity * 70).toInt(), 255, 215, 0)
                }
                canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), glowPaint)
            }
            EffectType.LIGHT_BOKEH -> {
                val bokehCount = (25 * intensity).toInt()
                for (i in 0 until bokehCount) {
                    val rand = Random(i * 71L)
                    val bx = (rand.nextFloat() * w + sin(timeSec + i) * 30f) % w
                    val by = (rand.nextFloat() * h + cos(timeSec + i) * 30f) % h
                    val br = rand.nextFloat() * 40f + 20f
                    paint.color = Color.argb((rand.nextFloat() * 100 * intensity).toInt(), 255, 230, 180)
                    canvas.drawCircle(bx, by, br, paint)
                }
            }

            // GLITCH & DISTORTION
            EffectType.GLITCH_RGB_SPLIT -> {
                val offset = (18f * intensity * sin(timeSec * 15f)).toInt()
                if (abs(offset) > 1) {
                    val splitBmp = baseFrame.copy(Bitmap.Config.ARGB_8888, true)
                    val pixels = IntArray(w * h)
                    splitBmp.getPixels(pixels, 0, w, 0, 0, w, h)
                    val resultPixels = pixels.clone()

                    for (y in 0 until h) {
                        for (x in 0 until w) {
                            val rX = (x + offset).coerceIn(0, w - 1)
                            val bX = (x - offset).coerceIn(0, w - 1)
                            val origR = (pixels[y * w + rX] shr 16) and 0xFF
                            val origG = (pixels[y * w + x] shr 8) and 0xFF
                            val origB = pixels[y * w + bX] and 0xFF
                            resultPixels[y * w + x] = (0xFF shl 24) or (origR shl 16) or (origG shl 8) or origB
                        }
                    }
                    splitBmp.setPixels(resultPixels, 0, w, 0, 0, w, h)
                    canvas.drawBitmap(splitBmp, 0f, 0f, null)
                }
            }
            EffectType.GLITCH_SCANLINE -> {
                paint.color = Color.argb((intensity * 80).toInt(), 0, 0, 0)
                paint.strokeWidth = 2f
                var y = 0f
                while (y < h) {
                    canvas.drawLine(0f, y, w.toFloat(), y, paint)
                    y += 6f
                }
            }
            EffectType.GLITCH_TV_CRT -> {
                paint.color = Color.argb((intensity * 40).toInt(), 255, 255, 255)
                val noiseCount = (w * h / 1000 * intensity).toInt()
                for (i in 0 until noiseCount) {
                    val rx = Random.nextFloat() * w
                    val ry = Random.nextFloat() * h
                    canvas.drawPoint(rx, ry, paint)
                }
            }

            // FILM & VINTAGE
            EffectType.FILM_GRAIN_PRO -> {
                paint.color = Color.argb((intensity * 50).toInt(), 255, 255, 255)
                val dots = (w * h / 600 * intensity).toInt()
                for (i in 0 until dots) {
                    val rx = Random.nextFloat() * w
                    val ry = Random.nextFloat() * h
                    canvas.drawCircle(rx, ry, 1.2f, paint)
                }
            }
            EffectType.FILM_DUST_SCRATCH -> {
                paint.color = Color.argb((intensity * 120).toInt(), 240, 240, 240)
                paint.strokeWidth = 1.5f
                val scratchCount = (4 * intensity).toInt().coerceAtLeast(1)
                for (i in 0 until scratchCount) {
                    val rand = Random(i * 89L + (timeMs / 200))
                    val sx = rand.nextFloat() * w
                    val sy = rand.nextFloat() * h * 0.3f
                    canvas.drawLine(sx, sy, sx + (rand.nextFloat() - 0.5f) * 20f, sy + 80f + rand.nextFloat() * 100f, paint)
                }
            }
            EffectType.FILM_BURN -> {
                val burnPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                val gradient = RadialGradient(w.toFloat(), 0f, w * 0.7f * intensity, intArrayOf(Color.argb((intensity * 220).toInt(), 255, 100, 20), Color.TRANSPARENT), floatArrayOf(0f, 1f), Shader.TileMode.CLAMP)
                burnPaint.shader = gradient
                canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), burnPaint)
            }
            else -> {}
        }

        return output
    }
}

// ============================================================================
// 4. EFFECT PIPELINE & RENDERERS
// ============================================================================
class EffectPipeline {
    private val blendEngine = BlendEngine()
    private val particleEngine = ParticleEngine()
    private val shaderEngine = ShaderEngine()

    fun processEffectStack(
        sourceBitmap: Bitmap,
        effectStack: List<EffectLayer>,
        timeMs: Long
    ): Bitmap {
        if (effectStack.isEmpty() || effectStack.none { it.enabled }) return sourceBitmap

        var current = sourceBitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(current)

        for (layer in effectStack) {
            if (!layer.enabled || layer.intensity <= 0.001f || layer.opacity <= 0.001f) continue

            val isParticle = layer.category == EffectCategory.PARTICLES || layer.type.name.startsWith("PARTICLE")

            val layerBitmap = if (isParticle) {
                particleEngine.renderParticles(
                    effectType = layer.type,
                    targetWidth = current.width,
                    targetHeight = current.height,
                    timeMs = timeMs,
                    intensity = layer.intensity,
                    speed = layer.speed,
                    colorHex = layer.primaryColorHex
                )
            } else {
                shaderEngine.renderShaderEffect(
                    effectType = layer.type,
                    baseFrame = current,
                    timeMs = timeMs,
                    intensity = layer.intensity,
                    speed = layer.speed
                )
            }

            blendEngine.applyBlend(
                baseCanvas = canvas,
                effectBitmap = layerBitmap,
                blendMode = layer.blendMode,
                opacity = layer.opacity
            )
        }

        return current
    }
}

class PreviewEffectRenderer(private val pipeline: EffectPipeline) {
    fun renderPreviewFrame(sourceBitmap: Bitmap, effectStack: List<EffectLayer>, timeMs: Long): Bitmap {
        return pipeline.processEffectStack(sourceBitmap, effectStack, timeMs)
    }
}

class ExportEffectRenderer(private val pipeline: EffectPipeline) {
    fun renderExportFrame(sourceBitmap: Bitmap, effectStack: List<EffectLayer>, timeMs: Long): Bitmap {
        return pipeline.processEffectStack(sourceBitmap, effectStack, timeMs)
    }
}

// ============================================================================
// 5. MASTER EFFECTS ENGINE (Singleton Orchestrator)
// ============================================================================
class EffectsEngine private constructor(private val context: Context) {

    private val pipeline = EffectPipeline()
    val previewRenderer = PreviewEffectRenderer(pipeline)
    val exportRenderer = ExportEffectRenderer(pipeline)

    private val _stackState = MutableStateFlow(EffectStackState())
    val stackState: StateFlow<EffectStackState> = _stackState.asStateFlow()

    private val undoStack = Stack<List<EffectLayer>>()
    private val redoStack = Stack<List<EffectLayer>>()

    // MASTER CATALOG OF ALL REAL GPU EFFECTS
    val PRESET_EFFECTS = listOf(
        EffectLayer(name = "Monsoon Rain", type = EffectType.WEATHER_RAIN, category = EffectCategory.WEATHER, blendMode = EffectBlendMode.SCREEN),
        EffectLayer(name = "Storm Lightning", type = EffectType.WEATHER_LIGHTNING, category = EffectCategory.WEATHER, blendMode = EffectBlendMode.ADD),
        EffectLayer(name = "Himalayan Snow", type = EffectType.WEATHER_SNOW, category = EffectCategory.WEATHER, blendMode = EffectBlendMode.SCREEN),
        EffectLayer(name = "Morning Fog", type = EffectType.WEATHER_FOG, category = EffectCategory.WEATHER, blendMode = EffectBlendMode.NORMAL),

        EffectLayer(name = "Anamorphic Lens Flare", type = EffectType.LIGHT_LENS_FLARE, category = EffectCategory.LIGHT, blendMode = EffectBlendMode.ADD),
        EffectLayer(name = "Sunset Light Leak", type = EffectType.LIGHT_LIGHT_LEAK, category = EffectCategory.LIGHT, blendMode = EffectBlendMode.SCREEN),
        EffectLayer(name = "Golden Hour Glow", type = EffectType.LIGHT_GOLDEN_GLOW, category = EffectCategory.LIGHT, blendMode = EffectBlendMode.OVERLAY),
        EffectLayer(name = "Bokeh Stars", type = EffectType.LIGHT_BOKEH, category = EffectCategory.LIGHT, blendMode = EffectBlendMode.ADD),

        EffectLayer(name = "Golden Fire Sparks", type = EffectType.PARTICLE_SPARKS, category = EffectCategory.PARTICLES, blendMode = EffectBlendMode.ADD),
        EffectLayer(name = "Cinematic Fire Flame", type = EffectType.PARTICLE_FIRE, category = EffectCategory.PARTICLES, blendMode = EffectBlendMode.ADD),
        EffectLayer(name = "Celebration Confetti", type = EffectType.PARTICLE_CONFETTI, category = EffectCategory.PARTICLES, blendMode = EffectBlendMode.NORMAL),
        EffectLayer(name = "Autumn Leaves", type = EffectType.PARTICLE_LEAVES, category = EffectCategory.PARTICLES, blendMode = EffectBlendMode.NORMAL),

        EffectLayer(name = "Cyberpunk RGB Split", type = EffectType.GLITCH_RGB_SPLIT, category = EffectCategory.GLITCH, blendMode = EffectBlendMode.NORMAL),
        EffectLayer(name = "Vintage Scanlines", type = EffectType.GLITCH_SCANLINE, category = EffectCategory.GLITCH, blendMode = EffectBlendMode.MULTIPLY),
        EffectLayer(name = "Retro CRT Noise", type = EffectType.GLITCH_TV_CRT, category = EffectCategory.GLITCH, blendMode = EffectBlendMode.SCREEN),

        EffectLayer(name = "35mm Film Grain", type = EffectType.FILM_GRAIN_PRO, category = EffectCategory.FILM, blendMode = EffectBlendMode.OVERLAY),
        EffectLayer(name = "Analog Film Burn", type = EffectType.FILM_BURN, category = EffectCategory.FILM, blendMode = EffectBlendMode.ADD),
        EffectLayer(name = "Dust & Scratches", type = EffectType.FILM_DUST_SCRATCH, category = EffectCategory.FILM, blendMode = EffectBlendMode.SCREEN)
    )

    private fun saveUndoState() {
        undoStack.push(_stackState.value.effects.map { it.copy() })
        redoStack.clear()
    }

    fun addEffect(effect: EffectLayer) {
        saveUndoState()
        val current = _stackState.value.effects.toMutableList()
        current.add(effect)
        _stackState.value = _stackState.value.copy(effects = current)
    }

    fun updateEffect(updated: EffectLayer) {
        val current = _stackState.value.effects.toMutableList()
        val index = current.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            current[index] = updated
            _stackState.value = _stackState.value.copy(effects = current)
        }
    }

    fun removeEffect(effectId: String) {
        saveUndoState()
        val current = _stackState.value.effects.filter { it.id != effectId }
        _stackState.value = _stackState.value.copy(effects = current)
    }

    fun toggleEffectEnabled(effectId: String) {
        val current = _stackState.value.effects.toMutableList()
        val index = current.indexOfFirst { it.id == effectId }
        if (index != -1) {
            val fx = current[index]
            current[index] = fx.copy(enabled = !fx.enabled)
            _stackState.value = _stackState.value.copy(effects = current)
        }
    }

    fun duplicateEffect(effectId: String) {
        saveUndoState()
        val current = _stackState.value.effects.toMutableList()
        val fx = current.find { it.id == effectId } ?: return
        val copy = fx.copy(id = "fx_${System.currentTimeMillis()}_dup", name = "${fx.name} (Copy)")
        current.add(copy)
        _stackState.value = _stackState.value.copy(effects = current)
    }

    fun reorderEffects(fromIndex: Int, toIndex: Int) {
        if (fromIndex in _stackState.value.effects.indices && toIndex in _stackState.value.effects.indices) {
            saveUndoState()
            val list = _stackState.value.effects.toMutableList()
            val item = list.removeAt(fromIndex)
            list.add(toIndex, item)
            _stackState.value = _stackState.value.copy(effects = list)
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            redoStack.push(_stackState.value.effects.map { it.copy() })
            _stackState.value = _stackState.value.copy(effects = undoStack.pop())
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            undoStack.push(_stackState.value.effects.map { it.copy() })
            _stackState.value = _stackState.value.copy(effects = redoStack.pop())
        }
    }

    fun resetStack() {
        saveUndoState()
        _stackState.value = _stackState.value.copy(effects = emptyList())
    }

    companion object {
        @Volatile private var instance: EffectsEngine? = null
        fun getInstance(context: Context): EffectsEngine {
            return instance ?: synchronized(this) {
                instance ?: EffectsEngine(context.applicationContext).also { instance = it }
            }
        }
    }
}
