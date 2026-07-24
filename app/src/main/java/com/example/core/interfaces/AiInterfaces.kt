package com.example.core.interfaces

import com.example.core.model.StandardAiResponse
import com.example.creator.AiCreatorReport
import com.example.data.ShoppingResult

/**
 * SHOPTOOLAI Phase 12A — Real Intelligence Provider Interfaces Architecture
 * Removes raw mock dependencies and establishes strong contracts.
 */
interface IAiProvider {
    val providerName: String
    val isAvailable: Boolean
}

interface IProductExtractor {
    suspend fun extractProduct(inputUrlOrQuery: String): StandardAiResponse<ShoppingResult>
}

interface ICreatorExtractor {
    suspend fun extractCreatorProfile(usernameOrScreenshot: String): StandardAiResponse<AiCreatorReport>
}

interface IOcrExtractor {
    suspend fun extractTextFromImage(imagePathOrUri: String): StandardAiResponse<String>
}

interface IVisionExtractor {
    suspend fun detectObjectsAndBrand(imagePathOrUri: String): StandardAiResponse<Map<String, Any>>
}

interface IPriceExtractor {
    suspend fun extractAndComparePrices(productTitle: String, currentPrice: Double, merchant: String): StandardAiResponse<List<Map<String, Any>>>
}

interface IReviewExtractor {
    suspend fun extractAndSummarizeReviews(productTitle: String, rawReviews: List<String>): StandardAiResponse<Map<String, Any>>
}
