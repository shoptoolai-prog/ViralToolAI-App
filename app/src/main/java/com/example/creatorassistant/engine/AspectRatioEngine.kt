package com.example.creatorassistant.engine

import android.graphics.RectF
import android.util.Log
import com.example.creatorassistant.domain.TargetRatio
import com.example.creatorassistant.domain.VideoUnderstandingResult

enum class ReframeStrategy {
    SMART_CROP,
    AUTO_REFRAME_PERSON,
    AUTO_REFRAME_OBJECT,
    LETTERBOX,
    FIT,
    CENTER_CROP_FALLBACK
}

data class DimensionCalculationResult(
    val targetWidth: Int,
    val targetHeight: Int,
    val cropRectNorm: RectF, // Normalized 0.0 - 1.0 coordinates
    val scaleFactor: Float,
    val strategy: ReframeStrategy,
    val strategyReason: String
)

class AspectRatioEngine {

    fun calculateOutputDimensions(
        sourceWidth: Int,
        sourceHeight: Int,
        targetRatio: TargetRatio,
        understanding: VideoUnderstandingResult?
    ): DimensionCalculationResult {
        Log.d("AspectRatioEngine", "Calculating dimensions for ${sourceWidth}x${sourceHeight} -> ${targetRatio.label}")

        val sourceAspect = sourceWidth.toFloat() / sourceHeight.coerceAtLeast(1).toFloat()

        // 1. Determine target aspect ratio float
        val desiredAspect = when (targetRatio) {
            TargetRatio.REELS_9_16 -> 9f / 16f
            TargetRatio.YOUTUBE_16_9 -> 16f / 9f
            TargetRatio.SQUARE_1_1 -> 1.0f
            TargetRatio.FEED_4_5 -> 4f / 5f
            TargetRatio.PORTRAIT_3_4 -> 3f / 4f
            TargetRatio.LANDSCAPE_4_3 -> 4f / 3f
            TargetRatio.CINEMATIC_21_9 -> 21f / 9f
            TargetRatio.ORIGINAL -> sourceAspect
            TargetRatio.CUSTOM -> 9f / 16f
        }

        // 2. Compute even target dimensions compatible with video encoders (divisible by 2)
        val (targetWidth, targetHeight) = when (targetRatio) {
            TargetRatio.REELS_9_16 -> Pair(1080, 1920)
            TargetRatio.YOUTUBE_16_9 -> Pair(1920, 1080)
            TargetRatio.SQUARE_1_1 -> Pair(1080, 1080)
            TargetRatio.FEED_4_5 -> Pair(1080, 1350)
            TargetRatio.PORTRAIT_3_4 -> Pair(1080, 1440)
            TargetRatio.LANDSCAPE_4_3 -> Pair(1440, 1080)
            TargetRatio.CINEMATIC_21_9 -> Pair(1920, 822)
            TargetRatio.ORIGINAL -> Pair(makeEven(sourceWidth), makeEven(sourceHeight))
            TargetRatio.CUSTOM -> Pair(1080, 1920)
        }

        if (targetRatio == TargetRatio.ORIGINAL || kotlin.math.abs(sourceAspect - desiredAspect) < 0.02f) {
            return DimensionCalculationResult(
                targetWidth = targetWidth,
                targetHeight = targetHeight,
                cropRectNorm = RectF(0f, 0f, 1f, 1f),
                scaleFactor = 1.0f,
                strategy = ReframeStrategy.FIT,
                strategyReason = "Source ratio matches target. Original canvas preserved."
            )
        }

        // 3. Choose Reframe Strategy based on understanding
        val (strategy, strategyReason) = when {
            understanding?.personDetected == true && understanding.subjectConfidence >= 0.70f -> {
                ReframeStrategy.AUTO_REFRAME_PERSON to "Main subject detected (${understanding.subjectType}, ${(understanding.subjectConfidence * 100).toInt()}% conf). Auto Reframing around subject."
            }
            understanding?.faceDetected == true -> {
                ReframeStrategy.AUTO_REFRAME_PERSON to "Face detected. Tracking subject with safe head margin."
            }
            understanding?.subjectConfidence ?: 0f >= 0.65f -> {
                ReframeStrategy.AUTO_REFRAME_OBJECT to "Primary interest region detected. Smart Crop centering on visual subject."
            }
            else -> {
                ReframeStrategy.CENTER_CROP_FALLBACK to "Confidence low. Using stable center crop with safe margins."
            }
        }

        // 4. Calculate Crop Rectangle
        val cropRectNorm = calculateCropWindow(
            sourceWidth = sourceWidth,
            sourceHeight = sourceHeight,
            desiredAspect = desiredAspect,
            understanding = understanding
        )

        return DimensionCalculationResult(
            targetWidth = targetWidth,
            targetHeight = targetHeight,
            cropRectNorm = cropRectNorm,
            scaleFactor = targetWidth.toFloat() / (sourceWidth * (cropRectNorm.right - cropRectNorm.left)),
            strategy = strategy,
            strategyReason = strategyReason
        )
    }

    private fun calculateCropWindow(
        sourceWidth: Int,
        sourceHeight: Int,
        desiredAspect: Float,
        understanding: VideoUnderstandingResult?
    ): RectF {
        val sourceAspect = sourceWidth.toFloat() / sourceHeight.coerceAtLeast(1).toFloat()

        var cropWidthNorm: Float
        var cropHeightNorm: Float

        if (sourceAspect > desiredAspect) {
            // Source is wider than target -> Crop horizontal sides
            cropHeightNorm = 1.0f
            cropWidthNorm = desiredAspect / sourceAspect
        } else {
            // Source is taller than target -> Crop vertical top/bottom
            cropWidthNorm = 1.0f
            cropHeightNorm = sourceAspect / desiredAspect
        }

        // Center around detected subject or default center (0.5, 0.5)
        val subjectCenterX = understanding?.boundingRegion?.first ?: 0.5f
        val subjectCenterY = understanding?.boundingRegion?.second ?: 0.5f

        var left = (subjectCenterX - cropWidthNorm / 2f).coerceIn(0f, 1.0f - cropWidthNorm)
        var top = (subjectCenterY - cropHeightNorm / 2f).coerceIn(0f, 1.0f - cropHeightNorm)

        // Ensure box does not exceed boundaries
        val right = (left + cropWidthNorm).coerceAtMost(1.0f)
        val bottom = (top + cropHeightNorm).coerceAtMost(1.0f)

        // Re-adjust left/top if clamped
        left = right - cropWidthNorm
        top = bottom - cropHeightNorm

        return RectF(
            left.coerceAtLeast(0f),
            top.coerceAtLeast(0f),
            right.coerceAtMost(1.0f),
            bottom.coerceAtMost(1.0f)
        )
    }

    private fun makeEven(valInt: Int): Int {
        val clamped = valInt.coerceAtLeast(160)
        return if (clamped % 2 != 0) clamped - 1 else clamped
    }
}
