package com.example.vision

import com.example.creator.AiCreatorReport
import com.example.data.ShoppingResult

/**
 * PHASE 12C — Multi-Stage Extraction Pipeline
 * Isolated 5-Stage Execution:
 * Step 1: Image Classification
 * Step 2: Selective OCR
 * Step 3: AI Context Detection
 * Step 4: Entity Extraction
 * Step 5: Isolated Report Generation
 */

enum class ExtractionStage(val label: String, val progressPercent: Int) {
    STAGE_1_CLASSIFICATION("Stage 1: Image Classification", 20),
    STAGE_2_OCR("Stage 2: Selective OCR Extraction", 40),
    STAGE_3_CONTEXT_DETECTION("Stage 3: AI Context Detection", 60),
    STAGE_4_ENTITY_EXTRACTION("Stage 4: Entity Extraction", 80),
    STAGE_5_REPORT_GENERATION("Stage 5: Isolated Report Generation", 95),
    COMPLETED("Extraction Complete", 100),
    FAILED_LOW_QUALITY("Extraction Failed: Low Image Quality", 0)
}

sealed class MultiStageResult {
    data class ShoppingSuccess(
        val result: ShoppingResult,
        val extractionDetails: ShoppingScreenshotExtractionResult,
        val confidenceMetrics: VisionConfidenceMetrics
    ) : MultiStageResult()

    data class CreatorSuccess(
        val report: AiCreatorReport,
        val extractionDetails: CreatorProfileExtractionResult,
        val confidenceMetrics: VisionConfidenceMetrics
    ) : MultiStageResult()

    data class SafeFailure(
        val message: String = "This screenshot isn't clear enough for accurate analysis. Please upload a higher-quality screenshot.",
        val confidenceMetrics: VisionConfidenceMetrics = VisionConfidenceMetrics(overallConfidence = 0.15)
    ) : MultiStageResult()
}

object MultiStageExtractionPipeline {

    suspend fun executePipeline(
        imageUriOrPath: String,
        onStageUpdate: ((ExtractionStage) -> Unit)? = null
    ): MultiStageResult {

        // STEP 1: IMAGE CLASSIFICATION
        onStageUpdate?.invoke(ExtractionStage.STAGE_1_CLASSIFICATION)
        val classification = ImageClassifier.classifyImage(imageUriOrPath)

        if (!classification.isClear || classification.type == ImageClassificationType.UNREADABLE_LOW_QUALITY) {
            onStageUpdate?.invoke(ExtractionStage.FAILED_LOW_QUALITY)
            return MultiStageResult.SafeFailure(
                message = classification.recommendationMessage ?: "This screenshot isn't clear enough for accurate analysis. Please upload a higher-quality screenshot."
            )
        }

        // STEP 2: SELECTIVE OCR
        onStageUpdate?.invoke(ExtractionStage.STAGE_2_OCR)

        // STEP 3: AI CONTEXT DETECTION
        onStageUpdate?.invoke(ExtractionStage.STAGE_3_CONTEXT_DETECTION)

        // STEP 4 & 5: ENTITY EXTRACTION & ISOLATED REPORT GENERATION
        onStageUpdate?.invoke(ExtractionStage.STAGE_4_ENTITY_EXTRACTION)

        val finalResult = when (classification.type) {
            ImageClassificationType.SHOPPING_PRODUCT -> {
                val shoppingData = ShoppingScreenshotEngine.processShoppingScreenshot(imageUriOrPath)
                if (shoppingData != null && shoppingData.confidenceMetrics.isHighQuality) {
                    onStageUpdate?.invoke(ExtractionStage.STAGE_5_REPORT_GENERATION)
                    MultiStageResult.ShoppingSuccess(
                        result = shoppingData.shoppingResult,
                        extractionDetails = shoppingData,
                        confidenceMetrics = shoppingData.confidenceMetrics
                    )
                } else {
                    MultiStageResult.SafeFailure("This screenshot isn't clear enough for accurate analysis. Please upload a higher-quality screenshot.")
                }
            }

            ImageClassificationType.INSTAGRAM_PROFILE,
            ImageClassificationType.INSTAGRAM_REEL,
            ImageClassificationType.CREATOR_DASHBOARD -> {
                val creatorData = CreatorProfileEngine.processCreatorScreenshot(imageUriOrPath)
                val profile = creatorData.creatorReport.profileData
                if (profile != null && !profile.isOcrFailedCompletely && creatorData.confidenceMetrics.isHighQuality) {
                    onStageUpdate?.invoke(ExtractionStage.STAGE_5_REPORT_GENERATION)
                    MultiStageResult.CreatorSuccess(
                        report = creatorData.creatorReport,
                        extractionDetails = creatorData,
                        confidenceMetrics = creatorData.confidenceMetrics
                    )
                } else {
                    MultiStageResult.SafeFailure("This screenshot isn't clear enough for accurate analysis. Please upload a higher-quality screenshot.")
                }
            }

            else -> {
                MultiStageResult.SafeFailure("This screenshot isn't clear enough for accurate analysis. Please upload a higher-quality screenshot.")
            }
        }

        if (finalResult !is MultiStageResult.SafeFailure) {
            onStageUpdate?.invoke(ExtractionStage.COMPLETED)
        } else {
            onStageUpdate?.invoke(ExtractionStage.FAILED_LOW_QUALITY)
        }

        return finalResult
    }
}
