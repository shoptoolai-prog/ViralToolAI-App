package com.example.core.engines

import com.example.core.interfaces.IPriceExtractor
import com.example.core.log.AiInternalLogger
import com.example.core.log.LogCategory
import com.example.core.model.AiResponseStatus
import com.example.core.model.StandardAiResponse
import com.example.providers.ProviderManager

/**
 * SHOPTOOLAI Phase 12A — Real Price Intelligence Engine Module
 */
object AiPriceEngine : IPriceExtractor {

    override suspend fun extractAndComparePrices(
        productTitle: String,
        currentPrice: Double,
        merchant: String
    ): StandardAiResponse<List<Map<String, Any>>> {
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "Starting Price Engine Pipeline", "Product: $productTitle, Merchant: $merchant")

        if (productTitle.isBlank() || currentPrice <= 0.0) {
            AiInternalLogger.log(LogCategory.DETECTION_FAILURE, "Invalid price or title", "Title: $productTitle, Price: $currentPrice")
            return StandardAiResponse(
                status = AiResponseStatus.PARTIAL,
                confidence = 0.2,
                detectedData = emptyList(),
                missingFields = listOf("validPrice", "validTitle"),
                warnings = listOf("Couldn't confidently identify competitive pricing for invalid input."),
                sourceType = "PRICE_ENGINE"
            )
        }

        return try {
            val identity = com.example.data.ProductIdentity(
                productName = productTitle,
                brand = "Verified Brand",
                category = "Shopping",
                merchant = merchant,
                image = "",
                url = ""
            )
            val comparisonItems = ProviderManager.fetchComparisonList(identity)

            val priceDataList = comparisonItems.map { item ->
                mapOf<String, Any>(
                    "store" to item.store,
                    "price" to item.price,
                    "url" to item.url,
                    "isBest" to item.isBest,
                    "deliveryInfo" to item.deliveryEstimate
                )
            }

            val confidence = if (comparisonItems.isNotEmpty()) 0.92 else 0.45

            AiInternalLogger.log(
                LogCategory.PIPELINE_EVENT,
                "Price comparison found ${comparisonItems.size} store prices",
                confidence = confidence
            )

            StandardAiResponse(
                status = AiResponseStatus.SUCCESS,
                confidence = confidence,
                detectedData = priceDataList,
                detectedFields = listOf("store", "price", "url", "isBest", "deliveryInfo"),
                sourceType = "CROSS_STORE_PRICE_ENGINE"
            )
        } catch (e: Exception) {
            AiInternalLogger.log(LogCategory.CRASH_POINT, "Price comparison error: ${e.message}")
            StandardAiResponse(
                status = AiResponseStatus.FAILURE,
                confidence = 0.0,
                errorMessage = e.message ?: "Price comparison failed",
                warnings = listOf("Couldn't confidently identify price comparison data."),
                sourceType = "PRICE_ENGINE"
            )
        }
    }
}
