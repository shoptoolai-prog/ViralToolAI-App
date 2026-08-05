package com.example.engine

import android.content.Context
import android.graphics.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.*
import java.util.Stack
import kotlin.math.*

// ============================================================================
// MASTER PHASE E-4 — PROFESSIONAL KEYFRAME & ANIMATION ENGINE (KeyframeEngine.kt)
// ============================================================================

enum class KeyframeInterpolation {
    LINEAR,
    EASE_IN,
    EASE_OUT,
    EASE_IN_OUT,
    BEZIER_CUBIC,
    BOUNCE,
    ELASTIC,
    OVERSHOOT,
    CUSTOM_CURVE
}

enum class MotionPathType {
    STRAIGHT,
    CURVE_SMOOTH,
    BEZIER_QUADRATIC,
    CIRCLE_ORBIT,
    CUSTOM_PATH
}

enum class PresetAnimationType {
    NONE,
    // IN
    FADE_IN,
    ZOOM_IN,
    SLIDE_IN_LEFT,
    SLIDE_IN_UP,
    POP_IN,
    BOUNCE_IN,
    TYPEWRITER,
    ROTATE_IN,
    ELASTIC_IN,
    GLITCH_IN,

    // OUT
    FADE_OUT,
    ZOOM_OUT,
    SLIDE_OUT_RIGHT,
    SLIDE_OUT_DOWN,
    POP_OUT,
    BOUNCE_OUT,

    // LOOP
    PULSE_ZOOM,
    FLOAT_BOUNCE,
    SPIN_CONTINUOUS,
    SHAKE_JITTER,
    NEON_GLOW_WAVE,
    SWING_PENDULUM
}

data class KeyframeData(
    val id: String = "kf_${System.currentTimeMillis()}_${(100..999).random()}",
    val timeMs: Long, // Offset relative to clip start (0 to durationMs)
    var positionX: Float = 0.0f, // Pixels or relative
    var positionY: Float = 0.0f,
    var scaleX: Float = 1.0f,
    var scaleY: Float = 1.0f,
    var rotation: Float = 0.0f, // Degrees
    var opacity: Float = 1.0f,   // 0.0 to 1.0
    var anchorX: Float = 0.5f,
    var anchorY: Float = 0.5f,
    var cropLeft: Float = 0.0f,
    var cropTop: Float = 0.0f,
    var blurRadius: Float = 0.0f,
    var filterIntensity: Float = 1.0f,
    var effectIntensity: Float = 1.0f,
    var audioVolume: Float = 1.0f,
    var colorHex: String = "#FFFFFF",
    var interpolation: KeyframeInterpolation = KeyframeInterpolation.LINEAR,
    var cubicControlP1x: Float = 0.25f,
    var cubicControlP1y: Float = 0.1f,
    var cubicControlP2x: Float = 0.25f,
    var cubicControlP2y: Float = 1.0f
)

data class KeyframeTrack(
    val targetId: String, // clipId or textId
    val keyframes: List<KeyframeData> = emptyList(), // sorted by timeMs
    val motionPathType: MotionPathType = MotionPathType.STRAIGHT,
    val inAnimation: PresetAnimationType = PresetAnimationType.NONE,
    val inDurationMs: Long = 500L,
    val outAnimation: PresetAnimationType = PresetAnimationType.NONE,
    val outDurationMs: Long = 500L,
    val loopAnimation: PresetAnimationType = PresetAnimationType.NONE,
    val loopSpeed: Float = 1.0f
)

data class InterpolatedTransform(
    val translateX: Float = 0.0f,
    val translateY: Float = 0.0f,
    val scaleX: Float = 1.0f,
    val scaleY: Float = 1.0f,
    val rotation: Float = 0.0f,
    val opacity: Float = 1.0f,
    val blurRadius: Float = 0.0f,
    val filterIntensity: Float = 1.0f,
    val effectIntensity: Float = 1.0f,
    val audioVolume: Float = 1.0f,
    val textVisibleCharCountFactor: Float = 1.0f
)

// ============================================================================
// 1. INTERPOLATION ENGINE
// ============================================================================
class InterpolationEngine {

    fun evaluateProgress(t: Float, type: KeyframeInterpolation, p1x: Float = 0.25f, p1y: Float = 0.1f, p2x: Float = 0.25f, p2y: Float = 1.0f): Float {
        val clampedT = t.coerceIn(0f, 1f)
        return when (type) {
            KeyframeInterpolation.LINEAR -> clampedT
            KeyframeInterpolation.EASE_IN -> clampedT * clampedT
            KeyframeInterpolation.EASE_OUT -> clampedT * (2f - clampedT)
            KeyframeInterpolation.EASE_IN_OUT -> if (clampedT < 0.5f) 2f * clampedT * clampedT else -1f + (4f - 2f * clampedT) * clampedT
            KeyframeInterpolation.BOUNCE -> evaluateBounce(clampedT)
            KeyframeInterpolation.ELASTIC -> evaluateElastic(clampedT)
            KeyframeInterpolation.OVERSHOOT -> {
                val s = 1.70158f
                val t1 = clampedT - 1f
                t1 * t1 * ((s + 1f) * t1 + s) + 1f
            }
            KeyframeInterpolation.BEZIER_CUBIC, KeyframeInterpolation.CUSTOM_CURVE -> evaluateCubicBezier(clampedT, p1x, p1y, p2x, p2y)
        }
    }

    private fun evaluateBounce(t: Float): Float {
        var x = t
        val n1 = 7.5625f
        val d1 = 2.75f
        return when {
            x < 1f / d1 -> n1 * x * x
            x < 2f / d1 -> {
                x -= 1.5f / d1
                n1 * x * x + 0.75f
            }
            x < 2.5f / d1 -> {
                x -= 2.25f / d1
                n1 * x * x + 0.9375f
            }
            else -> {
                x -= 2.625f / d1
                n1 * x * x + 0.984375f
            }
        }
    }

    private fun evaluateElastic(t: Float): Float {
        val c4 = (2f * Math.PI) / 3f
        return when (t) {
            0f -> 0f
            1f -> 1f
            else -> -2f.pow(10f * t - 10f) * sin((t * 10f - 10.75f) * c4).toFloat()
        }
    }

    private fun evaluateCubicBezier(t: Float, p1x: Float, p1y: Float, p2x: Float, p2y: Float): Float {
        // Simplified cubic bezier Y calculation for given parametric T
        val u = 1f - t
        val tt = t * t
        val uu = u * u
        val uuu = uu * u
        val ttt = tt * t
        return uuu * 0f + 3f * uu * t * p1y + 3f * u * tt * p2y + ttt * 1f
    }

    fun lerp(start: Float, end: Float, fraction: Float): Float {
        return start + (end - start) * fraction
    }
}

// ============================================================================
// 2. MOTION PATH ENGINE
// ============================================================================
class MotionPathEngine {

    fun calculatePositionAlongPath(
        startKf: KeyframeData,
        endKf: KeyframeData,
        progress: Float,
        pathType: MotionPathType
    ): Pair<Float, Float> {
        return when (pathType) {
            MotionPathType.STRAIGHT -> {
                val x = startKf.positionX + (endKf.positionX - startKf.positionX) * progress
                val y = startKf.positionY + (endKf.positionY - startKf.positionY) * progress
                Pair(x, y)
            }
            MotionPathType.CURVE_SMOOTH, MotionPathType.BEZIER_QUADRATIC -> {
                // Arc outward control point
                val midX = (startKf.positionX + endKf.positionX) / 2f
                val midY = (startKf.positionY + endKf.positionY) / 2f - 120f
                val u = 1f - progress
                val x = u * u * startKf.positionX + 2f * u * progress * midX + progress * progress * endKf.positionX
                val y = u * u * startKf.positionY + 2f * u * progress * midY + progress * progress * endKf.positionY
                Pair(x, y)
            }
            MotionPathType.CIRCLE_ORBIT -> {
                val cx = (startKf.positionX + endKf.positionX) / 2f
                val cy = (startKf.positionY + endKf.positionY) / 2f
                val radius = hypot(endKf.positionX - startKf.positionX, endKf.positionY - startKf.positionY) / 2f
                val angle = progress * Math.PI.toFloat()
                val x = cx + radius * cos(angle)
                val y = cy + radius * sin(angle)
                Pair(x, y)
            }
            MotionPathType.CUSTOM_PATH -> {
                val x = startKf.positionX + (endKf.positionX - startKf.positionX) * progress
                val y = startKf.positionY + (endKf.positionY - startKf.positionY) * progress
                Pair(x, y)
            }
        }
    }
}

// ============================================================================
// 3. ANIMATION RENDERER & PREVIEW ENGINE
// ============================================================================
class AnimationRenderer {

    private val interpolator = InterpolationEngine()
    private val motionPathEngine = MotionPathEngine()

    fun calculateInterpolatedTransform(
        track: KeyframeTrack?,
        relativeTimeMs: Long,
        totalClipDurationMs: Long
    ): InterpolatedTransform {
        if (track == null) return InterpolatedTransform()

        var transform = InterpolatedTransform()

        // 1. Evaluate Keyframe Track if present
        val kfs = track.keyframes
        if (kfs.isNotEmpty()) {
            if (relativeTimeMs <= kfs.first().timeMs) {
                val first = kfs.first()
                transform = transform.copy(
                    translateX = first.positionX,
                    translateY = first.positionY,
                    scaleX = first.scaleX,
                    scaleY = first.scaleY,
                    rotation = first.rotation,
                    opacity = first.opacity,
                    blurRadius = first.blurRadius,
                    filterIntensity = first.filterIntensity,
                    effectIntensity = first.effectIntensity,
                    audioVolume = first.audioVolume
                )
            } else if (relativeTimeMs >= kfs.last().timeMs) {
                val last = kfs.last()
                transform = transform.copy(
                    translateX = last.positionX,
                    translateY = last.positionY,
                    scaleX = last.scaleX,
                    scaleY = last.scaleY,
                    rotation = last.rotation,
                    opacity = last.opacity,
                    blurRadius = last.blurRadius,
                    filterIntensity = last.filterIntensity,
                    effectIntensity = last.effectIntensity,
                    audioVolume = last.audioVolume
                )
            } else {
                // Find bounding keyframe interval
                for (i in 0 until kfs.size - 1) {
                    val kfA = kfs[i]
                    val kfB = kfs[i + 1]
                    if (relativeTimeMs in kfA.timeMs..kfB.timeMs) {
                        val segDuration = (kfB.timeMs - kfA.timeMs).coerceAtLeast(1L)
                        val rawT = (relativeTimeMs - kfA.timeMs).toFloat() / segDuration.toFloat()
                        val easedT = interpolator.evaluateProgress(
                            rawT, kfA.interpolation,
                            kfA.cubicControlP1x, kfA.cubicControlP1y,
                            kfA.cubicControlP2x, kfA.cubicControlP2y
                        )

                        val (posPx, posPy) = motionPathEngine.calculatePositionAlongPath(kfA, kfB, easedT, track.motionPathType)

                        transform = transform.copy(
                            translateX = posPx,
                            translateY = posPy,
                            scaleX = interpolator.lerp(kfA.scaleX, kfB.scaleX, easedT),
                            scaleY = interpolator.lerp(kfA.scaleY, kfB.scaleY, easedT),
                            rotation = interpolator.lerp(kfA.rotation, kfB.rotation, easedT),
                            opacity = interpolator.lerp(kfA.opacity, kfB.opacity, easedT),
                            blurRadius = interpolator.lerp(kfA.blurRadius, kfB.blurRadius, easedT),
                            filterIntensity = interpolator.lerp(kfA.filterIntensity, kfB.filterIntensity, easedT),
                            effectIntensity = interpolator.lerp(kfA.effectIntensity, kfB.effectIntensity, easedT),
                            audioVolume = interpolator.lerp(kfA.audioVolume, kfB.audioVolume, easedT)
                        )
                        break
                    }
                }
            }
        }

        // 2. Evaluate Preset IN Animation
        if (track.inAnimation != PresetAnimationType.NONE && relativeTimeMs < track.inDurationMs) {
            val inProgress = (relativeTimeMs.toFloat() / track.inDurationMs.toFloat()).coerceIn(0f, 1f)
            transform = applyPresetInAnimation(transform, track.inAnimation, inProgress)
        }

        // 3. Evaluate Preset OUT Animation
        val outStart = totalClipDurationMs - track.outDurationMs
        if (track.outAnimation != PresetAnimationType.NONE && relativeTimeMs > outStart && totalClipDurationMs > 0) {
            val outProgress = ((relativeTimeMs - outStart).toFloat() / track.outDurationMs.toFloat()).coerceIn(0f, 1f)
            transform = applyPresetOutAnimation(transform, track.outAnimation, outProgress)
        }

        // 4. Evaluate Preset LOOP Animation
        if (track.loopAnimation != PresetAnimationType.NONE) {
            val loopTimeSec = (relativeTimeMs / 1000.0f) * track.loopSpeed
            transform = applyPresetLoopAnimation(transform, track.loopAnimation, loopTimeSec)
        }

        return transform
    }

    private fun applyPresetInAnimation(base: InterpolatedTransform, type: PresetAnimationType, progress: Float): InterpolatedTransform {
        val p = progress
        return when (type) {
            PresetAnimationType.FADE_IN -> base.copy(opacity = base.opacity * p)
            PresetAnimationType.ZOOM_IN -> base.copy(scaleX = base.scaleX * p, scaleY = base.scaleY * p, opacity = base.opacity * p)
            PresetAnimationType.SLIDE_IN_LEFT -> base.copy(translateX = base.translateX - (1f - p) * 400f)
            PresetAnimationType.SLIDE_IN_UP -> base.copy(translateY = base.translateY + (1f - p) * 400f)
            PresetAnimationType.POP_IN -> {
                val popScale = if (p < 0.7f) (p / 0.7f) * 1.2f else 1.2f - ((p - 0.7f) / 0.3f) * 0.2f
                base.copy(scaleX = base.scaleX * popScale, scaleY = base.scaleY * popScale)
            }
            PresetAnimationType.BOUNCE_IN -> {
                val bounce = interpolator.evaluateProgress(p, KeyframeInterpolation.BOUNCE)
                base.copy(translateY = base.translateY - (1f - bounce) * 300f)
            }
            PresetAnimationType.TYPEWRITER -> base.copy(textVisibleCharCountFactor = p)
            PresetAnimationType.ROTATE_IN -> base.copy(rotation = base.rotation + (1f - p) * 360f, opacity = base.opacity * p)
            PresetAnimationType.ELASTIC_IN -> {
                val el = interpolator.evaluateProgress(p, KeyframeInterpolation.ELASTIC)
                base.copy(scaleX = base.scaleX * el, scaleY = base.scaleY * el)
            }
            else -> base
        }
    }

    private fun applyPresetOutAnimation(base: InterpolatedTransform, type: PresetAnimationType, progress: Float): InterpolatedTransform {
        val p = progress
        val inv = 1f - p
        return when (type) {
            PresetAnimationType.FADE_OUT -> base.copy(opacity = base.opacity * inv)
            PresetAnimationType.ZOOM_OUT -> base.copy(scaleX = base.scaleX * inv, scaleY = base.scaleY * inv, opacity = base.opacity * inv)
            PresetAnimationType.SLIDE_OUT_RIGHT -> base.copy(translateX = base.translateX + p * 400f)
            PresetAnimationType.SLIDE_OUT_DOWN -> base.copy(translateY = base.translateY + p * 400f)
            PresetAnimationType.POP_OUT -> base.copy(scaleX = base.scaleX * inv, scaleY = base.scaleY * inv)
            PresetAnimationType.BOUNCE_OUT -> base.copy(translateY = base.translateY + p * p * 300f)
            else -> base
        }
    }

    private fun applyPresetLoopAnimation(base: InterpolatedTransform, type: PresetAnimationType, loopTimeSec: Float): InterpolatedTransform {
        return when (type) {
            PresetAnimationType.PULSE_ZOOM -> {
                val pulse = 1.0f + 0.1f * sin(loopTimeSec * 4f)
                base.copy(scaleX = base.scaleX * pulse, scaleY = base.scaleY * pulse)
            }
            PresetAnimationType.FLOAT_BOUNCE -> {
                val dy = 20f * sin(loopTimeSec * 3f)
                base.copy(translateY = base.translateY + dy)
            }
            PresetAnimationType.SPIN_CONTINUOUS -> {
                val rot = (loopTimeSec * 90f) % 360f
                base.copy(rotation = base.rotation + rot)
            }
            PresetAnimationType.SHAKE_JITTER -> {
                val jx = (sin(loopTimeSec * 25f) * 8f)
                val jy = (cos(loopTimeSec * 20f) * 8f)
                base.copy(translateX = base.translateX + jx, translateY = base.translateY + jy)
            }
            PresetAnimationType.SWING_PENDULUM -> {
                val swing = 15f * sin(loopTimeSec * 2f)
                base.copy(rotation = base.rotation + swing)
            }
            else -> base
        }
    }

    fun applyTransformToCanvas(canvas: Canvas, sourceBitmap: Bitmap, transform: InterpolatedTransform) {
        if (transform.opacity <= 0.01f) return

        val matrix = Matrix().apply {
            val cx = sourceBitmap.width / 2f
            val cy = sourceBitmap.height / 2f
            postTranslate(transform.translateX, transform.translateY)
            postScale(transform.scaleX, transform.scaleY, cx + transform.translateX, cy + transform.translateY)
            postRotate(transform.rotation, cx + transform.translateX, cy + transform.translateY)
        }

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            alpha = (transform.opacity * 255).toInt().coerceIn(0, 255)
        }

        canvas.drawBitmap(sourceBitmap, matrix, paint)
    }
}

// ============================================================================
// 4. MASTER KEYFRAME ENGINE (Singleton Orchestrator)
// ============================================================================
class KeyframeEngine private constructor(private val context: Context) {

    val animationRenderer = AnimationRenderer()

    private val _tracksState = MutableStateFlow<Map<String, KeyframeTrack>>(emptyMap()) // targetId -> KeyframeTrack
    val tracksState: StateFlow<Map<String, KeyframeTrack>> = _tracksState.asStateFlow()

    var autoKeyframeEnabled by checkAutoKeyframe()

    private fun checkAutoKeyframe() = mutableStateOf(false)

    private val undoStack = Stack<Map<String, KeyframeTrack>>()
    private val redoStack = Stack<Map<String, KeyframeTrack>>()

    private fun saveUndo() {
        undoStack.push(_tracksState.value.toMap())
        redoStack.clear()
    }

    fun getTrack(targetId: String): KeyframeTrack {
        return _tracksState.value[targetId] ?: KeyframeTrack(targetId = targetId)
    }

    fun addOrUpdateKeyframe(targetId: String, keyframe: KeyframeData) {
        saveUndo()
        val track = getTrack(targetId)
        val kfs = track.keyframes.toMutableList()
        val index = kfs.indexOfFirst { abs(it.timeMs - keyframe.timeMs) < 15L }
        if (index != -1) {
            kfs[index] = keyframe
        } else {
            kfs.add(keyframe)
            kfs.sortBy { it.timeMs }
        }

        val updatedMap = _tracksState.value.toMutableMap()
        updatedMap[targetId] = track.copy(keyframes = kfs)
        _tracksState.value = updatedMap
    }

    fun removeKeyframe(targetId: String, keyframeId: String) {
        saveUndo()
        val track = getTrack(targetId)
        val kfs = track.keyframes.filter { it.id != keyframeId }
        val updatedMap = _tracksState.value.toMutableMap()
        updatedMap[targetId] = track.copy(keyframes = kfs)
        _tracksState.value = updatedMap
    }

    fun setPresetAnimations(
        targetId: String,
        inAnim: PresetAnimationType = PresetAnimationType.NONE,
        inDur: Long = 500L,
        outAnim: PresetAnimationType = PresetAnimationType.NONE,
        outDur: Long = 500L,
        loopAnim: PresetAnimationType = PresetAnimationType.NONE,
        loopSpeed: Float = 1.0f
    ) {
        saveUndo()
        val track = getTrack(targetId)
        val updatedTrack = track.copy(
            inAnimation = inAnim,
            inDurationMs = inDur,
            outAnimation = outAnim,
            outDurationMs = outDur,
            loopAnimation = loopAnim,
            loopSpeed = loopSpeed
        )
        val updatedMap = _tracksState.value.toMutableMap()
        updatedMap[targetId] = updatedTrack
        _tracksState.value = updatedMap
    }

    fun copyTrack(sourceTargetId: String, destinationTargetId: String) {
        saveUndo()
        val sourceTrack = getTrack(sourceTargetId)
        val copiedTrack = sourceTrack.copy(
            targetId = destinationTargetId,
            keyframes = sourceTrack.keyframes.map { it.copy(id = "kf_${System.currentTimeMillis()}_${(100..999).random()}") }
        )
        val updatedMap = _tracksState.value.toMutableMap()
        updatedMap[destinationTargetId] = copiedTrack
        _tracksState.value = updatedMap
    }

    fun mirrorKeyframes(targetId: String) {
        saveUndo()
        val track = getTrack(targetId)
        if (track.keyframes.isEmpty()) return
        val maxT = track.keyframes.last().timeMs
        val mirrored = track.keyframes.map {
            it.copy(
                id = "kf_${System.currentTimeMillis()}_${(100..999).random()}",
                timeMs = maxT - it.timeMs
            )
        }.sortedBy { it.timeMs }

        val updatedMap = _tracksState.value.toMutableMap()
        updatedMap[targetId] = track.copy(keyframes = mirrored)
        _tracksState.value = updatedMap
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            redoStack.push(_tracksState.value.toMap())
            _tracksState.value = undoStack.pop()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            undoStack.push(_tracksState.value.toMap())
            _tracksState.value = redoStack.pop()
        }
    }

    companion object {
        @Volatile private var instance: KeyframeEngine? = null
        fun getInstance(context: Context): KeyframeEngine {
            return instance ?: synchronized(this) {
                instance ?: KeyframeEngine(context.applicationContext).also { instance = it }
            }
        }
    }
}
