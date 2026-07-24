package com.example.core.engines

import com.example.core.interfaces.IVisionExtractor
import com.example.core.log.AiInternalLogger
import com.example.core.log.LogCategory
import com.example.core.model.AiResponseStatus
import com.example.core.model.StandardAiResponse
import com.example.vision.AiVisionEngine
import com.example.vision.VisionSource

/**
 * SHOPTOOLAI Phase 12A — Real Vision Intelligence Engine Module
 */
object AiVisionEngineModule : IVisionExtractor {

    override suspend fun detectObjectsAndBrand(imagePathOrUri: String): StandardAiResponse<Map<String, Any>> {
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "Starting Vision Analysis Pipeline", imagePathOrUri)

        if (imagePathOrUri.isBlank()) {
            AiInternalLogger.log(LogCategory.DETECTION_FAILURE, "Vision Input Path Blank")
            return StandardAiResponse(
                status = AiResponseStatus.FAILURE,
                confidence = 0.0,
                errorMessage = "Couldn't confidently identify visual objects: empty path",
                warnings = listOf("No image supplied for vision analysis"),
                sourceType = "VISION_ENGINE"
            )
        }

        return try {
            val routingResult = com.example.vision.AiVisionRouter.routeInput(imagePathOrUri)

            when (routingResult) {
                is com.example.vision.VisionRoutingResult.ShoppingScreenshotResult -> {
                    val shoppingResult = routingResult.result
                    val extraction = routingResult.extraction
                    val detectedMap = mutableMapOf<String, Any>()
                    detectedMap["detectedProduct"] = shoppingResult.productName
                    detectedMap["merchant"] = shoppingResult.detectedStore
                    detectedMap["price"] = shoppingResult.currentPrice
                    detectedMap["discount"] = shoppingResult.discountPercent ?: 0
                    detectedMap["rating"] = shoppingResult.rating
                    detectedMap["reviewsCount"] = shoppingResult.reviewsCount
                    detectedMap["imageUrl"] = shoppingResult.imageUrl

                    val confidence = extraction.confidenceMetrics.overallConfidence

                    StandardAiResponse(
                        status = com.example.core.model.AiResponseStatus.SUCCESS,
                        confidence = confidence,
                        detectedData = detectedMap,
                        detectedFields = listOf("productName", "merchant", "price", "discount", "rating", "reviewsCount"),
                        missingFields = emptyList(),
                        sourceType = "SHOPPING_VISION_ENGINE"
                    )
                }

                is com.example.vision.VisionRoutingResult.CreatorScreenshotResult -> {
                    val report = routingResult.report
                    val extraction = routingResult.extraction
                    val profileData = report.profileData
                    val detectedMap = mutableMapOf<String, Any>()
                    detectedMap["username"] = profileData?.displayUsername() ?: ""
                    detectedMap["displayName"] = profileData?.displayDisplayName() ?: ""
                    detectedMap["followers"] = profileData?.displayFollowers() ?: ""
                    detectedMap["following"] = profileData?.displayFollowing() ?: ""
                    detectedMap["posts"] = profileData?.displayPosts() ?: ""
                    detectedMap["bio"] = profileData?.displayBio() ?: ""

                    val confidence = extraction.confidenceMetrics.overallConfidence

                    StandardAiResponse(
                        status = com.example.core.model.AiResponseStatus.SUCCESS,
                        confidence = confidence,
                        detectedData = detectedMap,
                        detectedFields = listOf("username", "displayName", "followers", "following", "posts", "bio"),
                        missingFields = emptyList(),
                        sourceType = "CREATOR_VISION_ENGINE"
                    )
                }

                is com.example.vision.VisionRoutingResult.ShoppingLinkResult -> {
                    val shoppingResult = routingResult.result
                    val detectedMap = mutableMapOf<String, Any>()
                    detectedMap["detectedProduct"] = shoppingResult.productName
                    detectedMap["merchant"] = shoppingResult.detectedStore
                    detectedMap["price"] = shoppingResult.currentPrice

                    StandardAiResponse(
                        status = com.example.core.model.AiResponseStatus.SUCCESS,
                        confidence = 0.95,
                        detectedData = detectedMap,
                        detectedFields = listOf("productName", "merchant", "price"),
                        missingFields = emptyList(),
                        sourceType = "SHOPPING_LINK_ENGINE"
                    )
                }

                is com.example.vision.VisionRoutingResult.UnreadableOrFailed -> {
                    StandardAiResponse(
                        status = com.example.core.model.AiResponseStatus.FAILURE,
                        confidence = 0.15,
                        errorMessage = routingResult.errorMessage,
                        warnings = listOf(routingResult.errorMessage),
                        sourceType = "VISION_ENGINE"
                    )
                }
            }
        } catch (e: Exception) {
            AiInternalLogger.log(LogCategory.CRASH_POINT, "Vision Pipeline exception: ${e.message}")
            StandardAiResponse(
                status = AiResponseStatus.FAILURE,
                confidence = 0.0,
                errorMessage = e.message ?: "Vision processing error",
                warnings = listOf("Couldn't confidently identify visual objects."),
                sourceType = "VISION_ENGINE"
            )
        }
    }
}
