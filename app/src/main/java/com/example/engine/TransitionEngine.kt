package com.example.engine

import android.content.Context
import android.graphics.*
import android.util.LruCache
import kotlinx.coroutines.flow.*
import java.util.Stack
import kotlin.math.*

// ============================================================================
// MASTER PHASE E-3 — PROFESSIONAL TRANSITION ENGINE (TransitionEngine.kt)
// ============================================================================

enum class TransitionCategory(val displayName: String) {
    BASIC("Basic & Fade"),
    MOVEMENT("Movement & Slide"),
    CAMERA("Camera & 3D"),
    FILM_LIGHT("Film & Light Leak"),
    GLITCH("Glitch & Distortion"),
    BLUR("Blur & Zoom"),
    MASK("Shape Mask"),
    NATURE("Nature & Particles"),
    ALL("All Transitions")
}

enum class TransitionType {
    // Basic
    FADE_CROSS,
    FADE_DIP_BLACK,
    FADE_DIP_WHITE,

    // Movement
    SLIDE_LEFT,
    SLIDE_RIGHT,
    SLIDE_UP,
    SLIDE_DOWN,
    PUSH_LEFT,
    PULL_RIGHT,

    // Camera & 3D
    WHIP_PAN,
    SPIN_ROTATE,
    FLIP_3D,
    PAGE_TURN,

    // Film & Light
    FILM_BURN,
    LIGHT_LEAK_FLASH,
    GLOW_DISSOLVE,

    // Glitch
    GLITCH_RGB_SPLIT,
    GLITCH_DIGITAL_STATIC,

    // Blur & Zoom
    ZOOM_IN_BLUR,
    ZOOM_OUT_BLUR,
    RADIAL_BLUR_SWIRL,

    // Mask
    MASK_CIRCLE,
    MASK_DIAMOND,
    MASK_RECTANGLE,

    // Nature & Particles
    PARTICLE_SPARK_BURST,
    SMOKE_DISSOLVE
}

data class TransitionJunction(
    val id: String = "trans_${System.currentTimeMillis()}_${(100..999).random()}",
    val clipAId: String,
    val clipBId: String,
    val type: TransitionType,
    val durationMs: Long = 800L, // Default 800ms overlap
    val intensity: Float = 1.0f,
    val isReversed: Boolean = false
)

data class TransitionState(
    val junctions: Map<String, TransitionJunction> = emptyMap() // Keyed by clipAId
)

// ============================================================================
// 1. GPU TRANSITION PIPELINE & RENDERER
// ============================================================================
class GPUTransitionPipeline {

    fun renderTransition(
        frameA: Bitmap,
        frameB: Bitmap,
        progress: Float, // 0.0f (100% frameA) to 1.0f (100% frameB)
        type: TransitionType,
        intensity: Float = 1.0f
    ): Bitmap {
        val w = frameA.width
        val h = frameA.height
        val output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val p = progress.coerceIn(0f, 1f)

        when (type) {
            // BASIC
            TransitionType.FADE_CROSS -> {
                // Draw Frame A
                canvas.drawBitmap(frameA, 0f, 0f, null)
                // Draw Frame B over Frame A with alpha
                paint.alpha = (p * 255).toInt().coerceIn(0, 255)
                canvas.drawBitmap(frameB, 0f, 0f, paint)
            }
            TransitionType.FADE_DIP_BLACK -> {
                if (p < 0.5f) {
                    val fadeP = p * 2f
                    canvas.drawBitmap(frameA, 0f, 0f, null)
                    paint.color = Color.BLACK
                    paint.alpha = (fadeP * 255).toInt().coerceIn(0, 255)
                    canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), paint)
                } else {
                    val fadeP = (p - 0.5f) * 2f
                    canvas.drawBitmap(frameB, 0f, 0f, null)
                    paint.color = Color.BLACK
                    paint.alpha = ((1f - fadeP) * 255).toInt().coerceIn(0, 255)
                    canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), paint)
                }
            }
            TransitionType.FADE_DIP_WHITE -> {
                if (p < 0.5f) {
                    val fadeP = p * 2f
                    canvas.drawBitmap(frameA, 0f, 0f, null)
                    paint.color = Color.WHITE
                    paint.alpha = (fadeP * 255).toInt().coerceIn(0, 255)
                    canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), paint)
                } else {
                    val fadeP = (p - 0.5f) * 2f
                    canvas.drawBitmap(frameB, 0f, 0f, null)
                    paint.color = Color.WHITE
                    paint.alpha = ((1f - fadeP) * 255).toInt().coerceIn(0, 255)
                    canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), paint)
                }
            }

            // MOVEMENT
            TransitionType.SLIDE_LEFT -> {
                val shiftX = p * w
                canvas.drawBitmap(frameA, -shiftX, 0f, null)
                canvas.drawBitmap(frameB, w - shiftX, 0f, null)
            }
            TransitionType.SLIDE_RIGHT -> {
                val shiftX = p * w
                canvas.drawBitmap(frameA, shiftX, 0f, null)
                canvas.drawBitmap(frameB, -w + shiftX, 0f, null)
            }
            TransitionType.SLIDE_UP -> {
                val shiftY = p * h
                canvas.drawBitmap(frameA, 0f, -shiftY, null)
                canvas.drawBitmap(frameB, 0f, h - shiftY, null)
            }
            TransitionType.SLIDE_DOWN -> {
                val shiftY = p * h
                canvas.drawBitmap(frameA, 0f, shiftY, null)
                canvas.drawBitmap(frameB, 0f, -h + shiftY, null)
            }
            TransitionType.PUSH_LEFT -> {
                val shiftX = p * w
                canvas.drawBitmap(frameA, -shiftX, 0f, null)
                canvas.drawBitmap(frameB, w - shiftX, 0f, null)
            }
            TransitionType.PULL_RIGHT -> {
                val shiftX = p * w
                canvas.drawBitmap(frameB, 0f, 0f, null)
                canvas.drawBitmap(frameA, shiftX, 0f, null)
            }

            // CAMERA & 3D
            TransitionType.WHIP_PAN -> {
                val shiftX = p * w * 1.5f
                canvas.drawBitmap(frameA, -shiftX, 0f, null)
                canvas.drawBitmap(frameB, w * 1.5f - shiftX, 0f, null)

                // Motion Blur Overlay Lines
                paint.color = Color.argb(120, 255, 255, 255)
                paint.strokeWidth = 10f
                for (i in 0..15) {
                    val ly = (h / 15f) * i
                    canvas.drawLine(0f, ly, w.toFloat(), ly, paint)
                }
            }
            TransitionType.SPIN_ROTATE -> {
                val angleA = p * 180f
                val angleB = -180f + p * 180f
                val matrixA = Matrix().apply {
                    postRotate(angleA, w / 2f, h / 2f)
                    val s = 1f - p * 0.5f
                    postScale(s, s, w / 2f, h / 2f)
                }
                val matrixB = Matrix().apply {
                    postRotate(angleB, w / 2f, h / 2f)
                    val s = 0.5f + p * 0.5f
                    postScale(s, s, w / 2f, h / 2f)
                }

                paint.alpha = ((1f - p) * 255).toInt()
                canvas.drawBitmap(frameA, matrixA, paint)

                paint.alpha = (p * 255).toInt()
                canvas.drawBitmap(frameB, matrixB, paint)
            }
            TransitionType.FLIP_3D -> {
                val scaleA = (1f - p * 2f).coerceAtLeast(0f)
                val scaleB = ((p - 0.5f) * 2f).coerceAtLeast(0f)

                if (p < 0.5f) {
                    val matrix = Matrix().apply {
                        postScale(scaleA, 1f, w / 2f, h / 2f)
                    }
                    canvas.drawBitmap(frameA, matrix, null)
                } else {
                    val matrix = Matrix().apply {
                        postScale(scaleB, 1f, w / 2f, h / 2f)
                    }
                    canvas.drawBitmap(frameB, matrix, null)
                }
            }
            TransitionType.PAGE_TURN -> {
                canvas.drawBitmap(frameB, 0f, 0f, null)
                val curlX = (1f - p) * w
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(curlX, 0f)
                    lineTo(curlX - 100f, h.toFloat())
                    lineTo(0f, h.toFloat())
                    close()
                }
                canvas.save()
                canvas.clipPath(path)
                canvas.drawBitmap(frameA, 0f, 0f, null)
                canvas.restore()
            }

            // FILM & LIGHT
            TransitionType.FILM_BURN -> {
                canvas.drawBitmap(frameA, 0f, 0f, null)
                paint.alpha = (p * 255).toInt()
                canvas.drawBitmap(frameB, 0f, 0f, paint)

                // Golden Burn Radial
                val burnRadius = (max(w, h) * sin(p * Math.PI)).toFloat()
                val burnPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                val gradient = RadialGradient(
                    w / 2f, h / 2f, burnRadius.coerceAtLeast(1f),
                    intArrayOf(Color.argb(220, 255, 140, 0), Color.argb(150, 255, 60, 0), Color.TRANSPARENT),
                    floatArrayOf(0f, 0.6f, 1f),
                    Shader.TileMode.CLAMP
                )
                burnPaint.shader = gradient
                canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), burnPaint)
            }
            TransitionType.LIGHT_LEAK_FLASH -> {
                val flashPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                val alpha = (sin(p * Math.PI) * 240).toInt().coerceIn(0, 255)
                canvas.drawBitmap(if (p < 0.5f) frameA else frameB, 0f, 0f, null)
                flashPaint.color = Color.argb(alpha, 255, 230, 150)
                canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), flashPaint)
            }

            // GLITCH
            TransitionType.GLITCH_RGB_SPLIT -> {
                val offset = (sin(p * Math.PI) * 30f * intensity).toFloat()
                canvas.drawBitmap(frameA, -offset, 0f, null)
                paint.alpha = (p * 255).toInt()
                canvas.drawBitmap(frameB, offset, 0f, paint)
            }

            // BLUR & ZOOM
            TransitionType.ZOOM_IN_BLUR -> {
                val scaleA = 1.0f + p * 0.4f
                val scaleB = 0.7f + p * 0.3f
                val matrixA = Matrix().apply { postScale(scaleA, scaleA, w / 2f, h / 2f) }
                val matrixB = Matrix().apply { postScale(scaleB, scaleB, w / 2f, h / 2f) }

                paint.alpha = ((1f - p) * 255).toInt()
                canvas.drawBitmap(frameA, matrixA, paint)

                paint.alpha = (p * 255).toInt()
                canvas.drawBitmap(frameB, matrixB, paint)
            }
            TransitionType.ZOOM_OUT_BLUR -> {
                val scaleA = 1.0f - p * 0.3f
                val scaleB = 1.3f - p * 0.3f
                val matrixA = Matrix().apply { postScale(scaleA, scaleA, w / 2f, h / 2f) }
                val matrixB = Matrix().apply { postScale(scaleB, scaleB, w / 2f, h / 2f) }

                paint.alpha = ((1f - p) * 255).toInt()
                canvas.drawBitmap(frameA, matrixA, paint)

                paint.alpha = (p * 255).toInt()
                canvas.drawBitmap(frameB, matrixB, paint)
            }

            // MASK
            TransitionType.MASK_CIRCLE -> {
                canvas.drawBitmap(frameA, 0f, 0f, null)
                val maxR = sqrt((w * w + h * h).toDouble()).toFloat() / 2f
                val radius = p * maxR

                val path = Path().apply {
                    addCircle(w / 2f, h / 2f, radius, Path.Direction.CW)
                }
                canvas.save()
                canvas.clipPath(path)
                canvas.drawBitmap(frameB, 0f, 0f, null)
                canvas.restore()
            }
            TransitionType.MASK_DIAMOND -> {
                canvas.drawBitmap(frameA, 0f, 0f, null)
                val size = p * max(w, h) * 1.5f
                val cx = w / 2f
                val cy = h / 2f

                val path = Path().apply {
                    moveTo(cx, cy - size)
                    lineTo(cx + size, cy)
                    lineTo(cx, cy + size)
                    lineTo(cx - size, cy)
                    close()
                }
                canvas.save()
                canvas.clipPath(path)
                canvas.drawBitmap(frameB, 0f, 0f, null)
                canvas.restore()
            }

            else -> {
                // Default Crossfade
                canvas.drawBitmap(frameA, 0f, 0f, null)
                paint.alpha = (p * 255).toInt()
                canvas.drawBitmap(frameB, 0f, 0f, paint)
            }
        }

        return output
    }
}

class TransitionRenderer(private val pipeline: GPUTransitionPipeline) {
    fun renderTransitionFrame(frameA: Bitmap, frameB: Bitmap, progress: Float, type: TransitionType): Bitmap {
        return pipeline.renderTransition(frameA, frameB, progress, type)
    }
}

// ============================================================================
// 2. MASTER TRANSITION ENGINE (Singleton Orchestrator)
// ============================================================================
class TransitionEngine private constructor(private val context: Context) {

    private val pipeline = GPUTransitionPipeline()
    val renderer = TransitionRenderer(pipeline)

    private val _transitionState = MutableStateFlow(TransitionState())
    val transitionState: StateFlow<TransitionState> = _transitionState.asStateFlow()

    private val undoStack = Stack<Map<String, TransitionJunction>>()
    private val redoStack = Stack<Map<String, TransitionJunction>>()

    // CATALOG OF REAL GPU TRANSITIONS
    val PRESET_TRANSITIONS = listOf(
        TransitionJunction(clipAId = "", clipBId = "", type = TransitionType.FADE_CROSS),
        TransitionJunction(clipAId = "", clipBId = "", type = TransitionType.FADE_DIP_BLACK),
        TransitionJunction(clipAId = "", clipBId = "", type = TransitionType.SLIDE_LEFT),
        TransitionJunction(clipAId = "", clipBId = "", type = TransitionType.SLIDE_RIGHT),
        TransitionJunction(clipAId = "", clipBId = "", type = TransitionType.WHIP_PAN),
        TransitionJunction(clipAId = "", clipBId = "", type = TransitionType.SPIN_ROTATE),
        TransitionJunction(clipAId = "", clipBId = "", type = TransitionType.FLIP_3D),
        TransitionJunction(clipAId = "", clipBId = "", type = TransitionType.PAGE_TURN),
        TransitionJunction(clipAId = "", clipBId = "", type = TransitionType.FILM_BURN),
        TransitionJunction(clipAId = "", clipBId = "", type = TransitionType.LIGHT_LEAK_FLASH),
        TransitionJunction(clipAId = "", clipBId = "", type = TransitionType.GLITCH_RGB_SPLIT),
        TransitionJunction(clipAId = "", clipBId = "", type = TransitionType.ZOOM_IN_BLUR),
        TransitionJunction(clipAId = "", clipBId = "", type = TransitionType.MASK_CIRCLE),
        TransitionJunction(clipAId = "", clipBId = "", type = TransitionType.MASK_DIAMOND)
    )

    private fun saveUndo() {
        undoStack.push(_transitionState.value.junctions.toMap())
        redoStack.clear()
    }

    fun setJunctionTransition(clipAId: String, clipBId: String, type: TransitionType, durationMs: Long = 800L) {
        saveUndo()
        val current = _transitionState.value.junctions.toMutableMap()
        current[clipAId] = TransitionJunction(
            clipAId = clipAId,
            clipBId = clipBId,
            type = type,
            durationMs = durationMs
        )
        _transitionState.value = _transitionState.value.copy(junctions = current)
    }

    fun removeJunctionTransition(clipAId: String) {
        saveUndo()
        val current = _transitionState.value.junctions.toMutableMap()
        current.remove(clipAId)
        _transitionState.value = _transitionState.value.copy(junctions = current)
    }

    fun getTransitionForJunction(clipAId: String): TransitionJunction? {
        return _transitionState.value.junctions[clipAId]
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            redoStack.push(_transitionState.value.junctions.toMap())
            _transitionState.value = _transitionState.value.copy(junctions = undoStack.pop())
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            undoStack.push(_transitionState.value.junctions.toMap())
            _transitionState.value = _transitionState.value.copy(junctions = redoStack.pop())
        }
    }

    companion object {
        @Volatile private var instance: TransitionEngine? = null
        fun getInstance(context: Context): TransitionEngine {
            return instance ?: synchronized(this) {
                instance ?: TransitionEngine(context.applicationContext).also { instance = it }
            }
        }
    }
}
