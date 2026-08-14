package com.example.creatorassistant.engine

import android.content.Context
import android.util.Log
import com.example.creatorassistant.domain.*

class AIDecisionEngine(private val context: Context) {

    fun createProcessingPlan(
        understanding: VideoUnderstandingResult,
        requestedTargetRatio: TargetRatio
    ): ProcessingPlan {
        Log.i("AIDecisionEngine", "PROCESSING_PLAN_CREATED for ratio ${requestedTargetRatio.label}")

        val isRatioChangeNeeded = requestedTargetRatio != TargetRatio.ORIGINAL &&
                kotlin.math.abs(understanding.aspectRatio - requestedTargetRatio.aspectRatio) > 0.08f

        // 1. Auto Reframe
        val autoReframeDecision = if (isRatioChangeNeeded) {
            when {
                understanding.personDetected && understanding.subjectConfidence >= 0.70f -> {
                    ProcessingOperationDecision(
                        operation = AiActionType.AUTO_REFRAME,
                        recommended = true,
                        confidence = understanding.subjectConfidence,
                        reason = "Primary ${understanding.subjectType} detected. Auto Reframing will track and center subject.",
                        estimatedRisk = "LOW"
                    )
                }
                else -> {
                    ProcessingOperationDecision(
                        operation = AiActionType.AUTO_REFRAME,
                        recommended = true,
                        confidence = 0.75f,
                        reason = "Smart Crop will center primary region of visual interest.",
                        estimatedRisk = "LOW"
                    )
                }
            }
        } else {
            ProcessingOperationDecision(
                operation = AiActionType.AUTO_REFRAME,
                recommended = false,
                confidence = 0.95f,
                reason = "Aspect ratio is already optimal. No reframing needed.",
                estimatedRisk = "LOW"
            )
        }

        // 2. Smart Crop
        val smartCropDecision = if (isRatioChangeNeeded) {
            ProcessingOperationDecision(
                operation = AiActionType.SMART_CROP,
                recommended = true,
                confidence = 0.90f,
                reason = "Maintains safe padding around subject head/edges during ratio adaptation.",
                estimatedRisk = "LOW"
            )
        } else {
            ProcessingOperationDecision(
                operation = AiActionType.SMART_CROP,
                recommended = false,
                confidence = 0.95f,
                reason = "No crop adjustment required for original canvas.",
                estimatedRisk = "LOW"
            )
        }

        // 3. Stabilization
        val stabilizeDecision = when (understanding.cameraShakeLevel) {
            "MODERATE_SHAKE", "HIGH_SHAKE" -> {
                ProcessingOperationDecision(
                    operation = AiActionType.STABILIZATION,
                    recommended = true,
                    confidence = 0.88f,
                    reason = "Camera motion shake detected (${understanding.cameraShakeLevel}). Smooth stabilization recommended.",
                    estimatedRisk = "MEDIUM"
                )
            }
            else -> {
                ProcessingOperationDecision(
                    operation = AiActionType.STABILIZATION,
                    recommended = false,
                    confidence = 0.92f,
                    reason = "Camera is stable. Stabilization unnecessary to avoid crop loss.",
                    estimatedRisk = "LOW"
                )
            }
        }

        // 4. Noise Reduction
        val noiseDecision = if (!understanding.hasAudio) {
            ProcessingOperationDecision(
                operation = AiActionType.NOISE_REMOVAL,
                recommended = false,
                confidence = 1.0f,
                reason = "No audio track available in video file.",
                estimatedRisk = "LOW"
            )
        } else if (understanding.noiseEstimate > 0.25f) {
            ProcessingOperationDecision(
                operation = AiActionType.NOISE_REMOVAL,
                recommended = true,
                confidence = 0.89f,
                reason = "Moderate background noise detected (${(understanding.noiseEstimate * 100).toInt()}%). Mild cleanup recommended.",
                estimatedRisk = "LOW"
            )
        } else {
            ProcessingOperationDecision(
                operation = AiActionType.NOISE_REMOVAL,
                recommended = false,
                confidence = 0.91f,
                reason = "Audio track is clean. Noise reduction skipped to preserve audio fidelity.",
                estimatedRisk = "LOW"
            )
        }

        // 5. Voice Enhancement
        val voiceDecision = if (!understanding.hasAudio || !understanding.hasSpeech) {
            ProcessingOperationDecision(
                operation = AiActionType.VOICE_ENHANCEMENT,
                recommended = false,
                confidence = understanding.speechConfidence,
                reason = "No clear spoken dialogue detected in audio track.",
                estimatedRisk = "LOW"
            )
        } else {
            ProcessingOperationDecision(
                operation = AiActionType.VOICE_ENHANCEMENT,
                recommended = true,
                confidence = understanding.speechConfidence,
                reason = "Spoken dialogue detected. Voice clarity enhancement recommended for creator speech.",
                estimatedRisk = "LOW"
            )
        }

        // 6. Loudness Normalization
        val loudnessDecision = if (!understanding.hasAudio) {
            ProcessingOperationDecision(
                operation = AiActionType.VOLUME_BALANCE,
                recommended = false,
                confidence = 1.0f,
                reason = "No audio track present.",
                estimatedRisk = "LOW"
            )
        } else if (understanding.audioLevelDb < -14.0f) {
            ProcessingOperationDecision(
                operation = AiActionType.VOLUME_BALANCE,
                recommended = true,
                confidence = 0.94f,
                reason = "Audio level (${understanding.audioLevelDb} dB) below platform social target (-14 dB). Normalization recommended.",
                estimatedRisk = "LOW"
            )
        } else {
            ProcessingOperationDecision(
                operation = AiActionType.VOLUME_BALANCE,
                recommended = false,
                confidence = 0.88f,
                reason = "Audio level is balanced within optimal platform specifications.",
                estimatedRisk = "LOW"
            )
        }

        // 7. Brightness Correction
        val brightnessDecision = if (understanding.brightnessScore < 0.45f) {
            ProcessingOperationDecision(
                operation = AiActionType.LOW_LIGHT_ENHANCEMENT,
                recommended = true,
                confidence = 0.86f,
                reason = "Shadow detail is dark (${(understanding.brightnessScore * 100).toInt()}% brightness). Mild brightening recommended.",
                estimatedRisk = "LOW"
            )
        } else {
            ProcessingOperationDecision(
                operation = AiActionType.LOW_LIGHT_ENHANCEMENT,
                recommended = false,
                confidence = 0.92f,
                reason = "Exposure and lighting are naturally balanced.",
                estimatedRisk = "LOW"
            )
        }

        // 8. Contrast & Color Correction
        val contrastDecision = if (understanding.contrastScore < 0.70f) {
            ProcessingOperationDecision(
                operation = AiActionType.COLOR_ENHANCEMENT,
                recommended = true,
                confidence = 0.88f,
                reason = "Slight contrast boost recommended to make colors pop on social feeds.",
                estimatedRisk = "LOW"
            )
        } else {
            ProcessingOperationDecision(
                operation = AiActionType.COLOR_ENHANCEMENT,
                recommended = false,
                confidence = 0.85f,
                reason = "Color grading and contrast are already rich.",
                estimatedRisk = "LOW"
            )
        }

        // 9. Sharpening
        val sharpeningDecision = if (understanding.sharpnessScore < 0.75f) {
            ProcessingOperationDecision(
                operation = AiActionType.VIDEO_SHARPENING,
                recommended = true,
                confidence = 0.82f,
                reason = "Mild edge detail recovery recommended to crisp up footage.",
                estimatedRisk = "LOW"
            )
        } else {
            ProcessingOperationDecision(
                operation = AiActionType.VIDEO_SHARPENING,
                recommended = false,
                confidence = 0.90f,
                reason = "Video detail is sharp. Sharpening skipped to prevent artifacting.",
                estimatedRisk = "LOW"
            )
        }

        val allDecisions = listOf(
            autoReframeDecision,
            smartCropDecision,
            stabilizeDecision,
            noiseDecision,
            voiceDecision,
            loudnessDecision,
            brightnessDecision,
            contrastDecision,
            sharpeningDecision
        )

        // Estimated processing time in seconds dynamically calculated
        val activeOperationsCount = allDecisions.count { it.recommended }
        val durationSec = (understanding.durationMs / 1000L).coerceAtLeast(1L)
        val estimatedSec = (2 + (durationSec * 0.15f) + (activeOperationsCount * 1.2f)).toInt().coerceIn(3, 45)

        return ProcessingPlan(
            convertRatio = requestedTargetRatio,
            autoReframe = autoReframeDecision,
            smartCrop = smartCropDecision,
            stabilize = stabilizeDecision,
            noiseReduction = noiseDecision,
            voiceEnhancement = voiceDecision,
            loudnessNormalization = loudnessDecision,
            brightnessCorrection = brightnessDecision,
            contrastCorrection = contrastDecision,
            sharpening = sharpeningDecision,
            allDecisions = allDecisions,
            estimatedProcessingTimeSec = estimatedSec
        )
    }
}
