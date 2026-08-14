package com.example.creatorassistant.engine

import android.content.Context
import com.example.creatorassistant.domain.AiActionType
import com.example.creatorassistant.domain.CameraShakeLevel
import com.example.creatorassistant.domain.ExposureStatus
import com.example.creatorassistant.domain.VisualAnalysisResult
import com.example.creatorassistant.domain.VisualEnhancementPlan

class VideoEnhancementEngine(private val context: Context) {

    fun buildEnhancementPlan(
        analysis: VisualAnalysisResult,
        selectedActions: Set<AiActionType>,
        retryAttempt: Int = 1
    ): VisualEnhancementPlan {
        val userWantsStabilize = selectedActions.contains(AiActionType.STABILIZATION) || selectedActions.contains(AiActionType.AI_QUALITY_ENHANCE)
        val userWantsLighting = selectedActions.contains(AiActionType.LOW_LIGHT_ENHANCEMENT) || selectedActions.contains(AiActionType.AI_QUALITY_ENHANCE)
        val userWantsColor = selectedActions.contains(AiActionType.COLOR_ENHANCEMENT) || selectedActions.contains(AiActionType.AI_QUALITY_ENHANCE)
        val userWantsSharpen = selectedActions.contains(AiActionType.VIDEO_SHARPENING) || selectedActions.contains(AiActionType.FACE_SUBJECT_CLARITY)
        val userWantsNoise = selectedActions.contains(AiActionType.LOW_LIGHT_ENHANCEMENT)

        // Adaptive AI Decision: only apply if needed or requested
        val applyStabilize = (analysis.cameraShakeLevel != CameraShakeLevel.STABLE) || userWantsStabilize
        val applyExposure = (analysis.exposureStatus != ExposureStatus.BALANCED) || userWantsLighting
        val applyColor = (analysis.colorCast != "NEUTRAL" || analysis.saturationLevel < 0.40f) || userWantsColor
        val applyContrast = (analysis.contrastScore < 0.60f) || userWantsColor
        val applySharpen = (analysis.sharpnessScore < 0.72f) || userWantsSharpen
        val applyNoise = (analysis.noiseLevel > 0.16f) || userWantsNoise

        // Tuning factors adapted for retries
        val baseBrightnessOffset = when (analysis.exposureStatus) {
            ExposureStatus.UNDEREXPOSED -> 0.12f + (retryAttempt * 0.02f)
            ExposureStatus.OVEREXPOSED -> -0.08f - (retryAttempt * 0.02f)
            else -> if (userWantsLighting) 0.06f else 0.02f
        }

        val baseContrastMultiplier = if (applyContrast) 1.10f + (retryAttempt * 0.02f) else 1.02f
        val baseSaturationMultiplier = if (applyColor) 1.12f + (retryAttempt * 0.02f) else 1.02f
        val baseSharpeningAmount = if (applySharpen) 1.15f - ((retryAttempt - 1) * 0.05f) else 1.0f

        val appliedChanges = mutableListOf<String>()
        if (applyStabilize) appliedChanges.add("Stabilization")
        if (applyExposure) appliedChanges.add("Exposure corrected")
        if (applyColor) appliedChanges.add("Color balance corrected")
        if (applyContrast) appliedChanges.add("Contrast enhanced")
        if (applyNoise) appliedChanges.add("Visual noise reduced")
        if (applySharpen) appliedChanges.add("Detail enhancement")

        val recommendation = when {
            appliedChanges.isNotEmpty() -> "AI applied targeted visual corrections: ${appliedChanges.joinToString(", ")}."
            else -> "Original video visual quality is optimal. Minimal enhancement required."
        }

        return VisualEnhancementPlan(
            applyStabilization = applyStabilize,
            applyExposureCorrection = applyExposure,
            applyColorCorrection = applyColor,
            applyContrastCorrection = applyContrast,
            applySharpening = applySharpen,
            applyNoiseReduction = applyNoise,
            applyUpscale = false,
            targetResolutionLabel = "1080p",
            brightnessOffset = baseBrightnessOffset,
            contrastMultiplier = baseContrastMultiplier,
            saturationMultiplier = baseSaturationMultiplier,
            sharpeningAmount = baseSharpeningAmount,
            stabilizationFactor = if (applyStabilize) 0.85f else 1.0f,
            appliedChanges = appliedChanges,
            recommendationText = recommendation,
            reason = recommendation
        )
    }
}
