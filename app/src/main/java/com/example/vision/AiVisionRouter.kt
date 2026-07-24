package com.example.vision

import com.example.creator.AiCreatorReport
import com.example.data.ShoppingResult
import com.example.engine.UniversalShoppingEngine

/**
 * PHASE 12C — Central AI Vision Router
 * Classifies input automatically and routes to correct engine without mixing outputs.
 */

enum class VisionInputClassification {
    SHOPPING_PRODUCT_LINK,
    SHOPPING_PRODUCT_SCREENSHOT,
    INSTAGRAM_CREATOR_SCREENSHOT,
    UNKNOWN_SCREENSHOT,
    UNSUPPORTED_IMAGE
}

sealed class VisionRoutingResult {
    data class ShoppingLinkResult(val result: ShoppingResult) : VisionRoutingResult()
    data class ShoppingScreenshotResult(val result: ShoppingResult, val extraction: ShoppingScreenshotExtractionResult) : VisionRoutingResult()
    data class CreatorScreenshotResult(val report: AiCreatorReport, val extraction: CreatorProfileExtractionResult) : VisionRoutingResult()
    data class UnreadableOrFailed(val errorMessage: String = "This screenshot isn't clear enough for accurate analysis. Please upload a higher-quality screenshot.") : VisionRoutingResult()
}

object AiVisionRouter {

    /**
     * Central routing method for links or image inputs.
     */
    suspend fun routeInput(inputPathOrUrl: String): VisionRoutingResult {
        val trimmed = inputPathOrUrl.trim()

        if (trimmed.isBlank()) {
            return VisionRoutingResult.UnreadableOrFailed("This screenshot isn't clear enough for accurate analysis. Please upload a higher-quality screenshot.")
        }

        // 1. Check if input is a Web URL
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            val state = UniversalShoppingEngine.processUrlPipeline(trimmed)
            return if (state is com.example.engine.SmartResultState.Verified) {
                VisionRoutingResult.ShoppingLinkResult(state.result)
            } else {
                VisionRoutingResult.UnreadableOrFailed("Could not process shopping link: $trimmed")
            }
        }

        // 2. Route Image input through Multi-Stage Extraction Pipeline
        val multiStageResult = MultiStageExtractionPipeline.executePipeline(trimmed)

        return when (multiStageResult) {
            is MultiStageResult.ShoppingSuccess -> {
                VisionRoutingResult.ShoppingScreenshotResult(
                    result = multiStageResult.result,
                    extraction = multiStageResult.extractionDetails
                )
            }

            is MultiStageResult.CreatorSuccess -> {
                VisionRoutingResult.CreatorScreenshotResult(
                    report = multiStageResult.report,
                    extraction = multiStageResult.extractionDetails
                )
            }

            is MultiStageResult.SafeFailure -> {
                VisionRoutingResult.UnreadableOrFailed(multiStageResult.message)
            }
        }
    }
}
