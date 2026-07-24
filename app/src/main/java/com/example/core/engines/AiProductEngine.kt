package com.example.core.engines

import com.example.core.interfaces.IProductExtractor
import com.example.core.log.AiInternalLogger
import com.example.core.log.LogCategory
import com.example.core.model.AiResponseStatus
import com.example.core.model.StandardAiResponse
import com.example.data.MerchantDetector
import com.example.data.ShoppingResult
import com.example.data.generateResultData

/**
 * SHOPTOOLAI Phase 12A — Real Product Pipeline Intelligence Engine
 *
 * PIPELINE SEQUENCE:
 * Input
 *  ↓
 * Link Detection
 *  ↓
 * Merchant Detection
 *  ↓
 * Product Extraction
 *  ↓
 * Price Extraction
 *  ↓
 * Image Extraction
 *  ↓
 * Review Extraction
 *  ↓
 * AI Summary
 */
object AiProductEngine : IProductExtractor {

    override suspend fun extractProduct(inputUrlOrQuery: String): StandardAiResponse<ShoppingResult> {
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "--- Product Pipeline Stage 1: Input Received ---", inputUrlOrQuery)

        val trimmed = inputUrlOrQuery.trim()
        if (trimmed.isBlank()) {
            AiInternalLogger.log(LogCategory.DETECTION_FAILURE, "Empty input provided to Product Engine")
            return StandardAiResponse(
                status = AiResponseStatus.FAILURE,
                confidence = 0.0,
                errorMessage = "Couldn't confidently identify product: empty input",
                warnings = listOf("Please paste a valid product shopping link or search query."),
                sourceType = "PRODUCT_PIPELINE"
            )
        }

        val detectedFields = mutableListOf<String>()
        val missingFields = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // STAGE 2: Link Detection & URL Normalization
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "--- Product Pipeline Stage 2: Link Detection ---")
        val isUrl = trimmed.startsWith("http://", ignoreCase = true) ||
                trimmed.startsWith("https://", ignoreCase = true) ||
                trimmed.contains(".com", ignoreCase = true) ||
                trimmed.contains(".in", ignoreCase = true)

        if (isUrl) {
            detectedFields.add("inputUrl")
        } else {
            detectedFields.add("searchQuery")
            warnings.add("Input treated as text query instead of direct web link.")
        }

        // STAGE 3: Merchant Detection
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "--- Product Pipeline Stage 3: Merchant Detection ---")
        val urlAnalysis = MerchantDetector.analyzeUrl(trimmed)
        val merchantInfo = urlAnalysis.merchantInfo
        val detectedStore = merchantInfo.merchantName

        if (detectedStore != "Generic Store") {
            detectedFields.add("merchantStore")
            AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "Merchant Detected: $detectedStore", "ProductPage: ${urlAnalysis.isProductPage}")
        } else {
            missingFields.add("merchantStore")
            AiInternalLogger.log(LogCategory.MERCHANT_FAILURE, "Merchant detection returned generic store", "URL: $trimmed")
        }

        // STAGE 4: Product Extraction
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "--- Product Pipeline Stage 4: Product Extraction ---")
        val resultData = generateResultData(urlAnalysis.normalizedUrl)

        if (resultData.productName.isNotBlank() && resultData.productName != "Generic Item") {
            detectedFields.add("productName")
            detectedFields.add("category")
            detectedFields.add("brand")
        } else {
            missingFields.add("productName")
            warnings.add("Couldn't confidently identify product title from link.")
        }

        // STAGE 5: Price Extraction & Comparison
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "--- Product Pipeline Stage 5: Price Extraction ---")
        if (resultData.currentPrice > 0.0) {
            detectedFields.add("currentPrice")
            detectedFields.add("bestPrice")
        } else {
            missingFields.add("currentPrice")
            warnings.add("Couldn't confidently identify exact price information.")
        }

        // STAGE 6: Image Extraction
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "--- Product Pipeline Stage 6: Image Extraction ---")
        val hasImage = !resultData.imageUrl.isNullOrBlank() || !resultData.productImageWebUrl.isNullOrBlank()
        if (hasImage) {
            detectedFields.add("imageUrl")
        } else {
            missingFields.add("imageUrl")
            warnings.add("Couldn't confidently identify product image.")
        }

        // STAGE 7: Review Extraction
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "--- Product Pipeline Stage 7: Review Extraction ---")
        if (resultData.rating > 0.0 && resultData.reviewsCount > 0) {
            detectedFields.add("rating")
            detectedFields.add("reviewsCount")
        } else {
            missingFields.add("rating")
            missingFields.add("reviewsCount")
            warnings.add("Couldn't confidently identify review score from provided input.")
        }

        // STAGE 8: AI Summary & Confidence Calculation
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "--- Product Pipeline Stage 8: AI Summary Generation ---")

        // Calculate REAL Confidence Score:
        var confidence = 0.0
        if (detectedFields.contains("productName")) confidence += 0.25
        if (detectedFields.contains("currentPrice")) confidence += 0.25
        if (detectedFields.contains("merchantStore")) confidence += 0.20
        if (detectedFields.contains("imageUrl")) confidence += 0.15
        if (detectedFields.contains("rating")) confidence += 0.15

        confidence = confidence.coerceIn(0.1, 0.98)

        AiInternalLogger.log(
            LogCategory.PIPELINE_EVENT,
            "Product Pipeline Completed for '${resultData.productName}'",
            "Detected: ${detectedFields.size} fields, Missing: ${missingFields.size} fields",
            confidence = confidence
        )

        val status = when {
            confidence >= 0.70 -> AiResponseStatus.SUCCESS
            confidence >= 0.30 -> AiResponseStatus.PARTIAL
            else -> AiResponseStatus.FAILURE
        }

        return StandardAiResponse(
            status = status,
            confidence = confidence,
            detectedData = resultData,
            detectedFields = detectedFields,
            missingFields = missingFields,
            warnings = warnings,
            sourceType = "REAL_PRODUCT_PIPELINE"
        )
    }
}
