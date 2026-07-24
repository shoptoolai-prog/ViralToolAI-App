package com.example.core

import com.example.core.engines.*
import com.example.core.log.AiInternalLogger
import com.example.core.log.LogCategory
import com.example.core.model.StandardAiResponse
import com.example.creator.AiCreatorReport
import com.example.data.ShoppingResult

/**
 * SHOPTOOLAI Phase 12A — Centralized AI Core Hub
 * Coordinates all 7 real intelligence engines:
 * 1. Vision Engine (AiVisionEngineModule)
 * 2. OCR Engine (AiOcrEngine)
 * 3. Product Engine (AiProductEngine)
 * 4. Creator Engine (AiCreatorEngineModule)
 * 5. Review Engine (AiReviewEngine)
 * 6. Price Engine (AiPriceEngine)
 * 7. Gemini Engine (AiGeminiEngine)
 */
object CentralizedAiCore {

    val visionEngine: AiVisionEngineModule get() = AiVisionEngineModule
    val ocrEngine: AiOcrEngine get() = AiOcrEngine
    val productEngine: AiProductEngine get() = AiProductEngine
    val creatorEngine: AiCreatorEngineModule get() = AiCreatorEngineModule
    val reviewEngine: AiReviewEngine get() = AiReviewEngine
    val priceEngine: AiPriceEngine get() = AiPriceEngine
    val geminiEngine: AiGeminiEngine get() = AiGeminiEngine

    suspend fun analyzeShoppingProduct(inputUrlOrQuery: String): StandardAiResponse<ShoppingResult> {
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "Centralized AI Core -> Routing to Product Engine", inputUrlOrQuery)
        return productEngine.extractProduct(inputUrlOrQuery)
    }

    suspend fun analyzeCreatorProfile(usernameOrScreenshot: String): StandardAiResponse<AiCreatorReport> {
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "Centralized AI Core -> Routing to Creator Engine", usernameOrScreenshot)
        return creatorEngine.extractCreatorProfile(usernameOrScreenshot)
    }

    suspend fun extractOcrText(imagePathOrUri: String): StandardAiResponse<String> {
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "Centralized AI Core -> Routing to OCR Engine", imagePathOrUri)
        return ocrEngine.extractTextFromImage(imagePathOrUri)
    }

    suspend fun analyzeVisionImage(imagePathOrUri: String): StandardAiResponse<Map<String, Any>> {
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "Centralized AI Core -> Routing to Vision Engine", imagePathOrUri)
        return visionEngine.detectObjectsAndBrand(imagePathOrUri)
    }

    suspend fun comparePrices(productTitle: String, currentPrice: Double, merchant: String): StandardAiResponse<List<Map<String, Any>>> {
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "Centralized AI Core -> Routing to Price Engine", productTitle)
        return priceEngine.extractAndComparePrices(productTitle, currentPrice, merchant)
    }

    suspend fun summarizeReviews(productTitle: String, rawReviews: List<String>): StandardAiResponse<Map<String, Any>> {
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "Centralized AI Core -> Routing to Review Engine", productTitle)
        return reviewEngine.extractAndSummarizeReviews(productTitle, rawReviews)
    }
}
