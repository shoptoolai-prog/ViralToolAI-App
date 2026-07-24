package com.example.data.integration

import com.example.cache.LocalShoppingCache
import com.example.data.ShoppingResult
import com.example.data.extraction.RawExtractedMetadata
import com.example.data.extraction.SafeReportBuilder
import com.example.data.extraction.ShoppingMetadataExtractor
import com.example.data.reliability.DataReliabilityEngine
import com.example.data.resolver.LinkResolutionEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * MASTER PHASE 13A — Real Data Integration Layer
 * Single central integration point for ALL shopping product queries in ViralToolAI.
 * Sources: HTML Metadata, JSON-LD, Open Graph, Merchant Metadata, Future APIs.
 *
 * Guarantees:
 * 1. Prioritizes VERIFIED data only.
 * 2. Unresolved values are marked Unavailable / null.
 * 3. Never invents product titles, prices, merchants, or reviews.
 * 4. Resolves redirects, affiliate, and mobile links automatically.
 * 5. Handles extraction failures gracefully without crashing.
 */
object RealDataIntegrationLayer {

    /**
     * Central async product data retrieval pipeline.
     */
    suspend fun getProductData(rawUrl: String): ShoppingResult = withContext(Dispatchers.IO) {
        val trimmedInput = rawUrl.trim()
        if (trimmedInput.isBlank()) {
            return@withContext SafeReportBuilder.buildResultFromVerifiedReport(
                DataReliabilityEngine.processAndVerify(RawExtractedMetadata(), rawUrl),
                rawUrl
            )
        }

        try {
            // 1. Link Resolution & Cleaning (Short links, redirects, tracking parameters)
            val resolvedCleanUrl = LinkResolutionEngine.resolveCleanProductUrl(trimmedInput)

            // 2. Local Cache Check
            val cachedResult = LocalShoppingCache.getCachedProductResult(resolvedCleanUrl)
            if (cachedResult != null && cachedResult.isReliable) {
                return@withContext cachedResult
            }

            // 3. Extract Metadata from Live Sources (HTML, JSON-LD, OpenGraph, Meta Tags)
            val rawExtracted = ShoppingMetadataExtractor.fetchAndExtractMetadata(resolvedCleanUrl)

            // 4. Validate Fields & Process via Reliability Engine
            val verifiedReport = DataReliabilityEngine.processAndVerify(rawExtracted, resolvedCleanUrl)

            // 5. Build Safe Report (Hides unverified fields, shows Unavailable)
            val shoppingResult = SafeReportBuilder.buildResultFromVerifiedReport(verifiedReport, resolvedCleanUrl)

            // 6. Cache Verified Results
            if (shoppingResult.isReliable) {
                LocalShoppingCache.cacheProductResult(shoppingResult)
                LocalShoppingCache.addRecentSearch(resolvedCleanUrl)
            }

            shoppingResult
        } catch (e: Exception) {
            // Error Recovery: Return safe unverified state with friendly error message instead of crashing
            val fallbackReport = DataReliabilityEngine.processAndVerify(RawExtractedMetadata(), trimmedInput)
            SafeReportBuilder.buildResultFromVerifiedReport(fallbackReport, trimmedInput)
        }
    }

    /**
     * Synchronous caller wrapper for legacy callers.
     */
    fun getProductDataSync(rawUrl: String): ShoppingResult {
        return try {
            runBlocking(Dispatchers.IO) {
                getProductData(rawUrl)
            }
        } catch (e: Exception) {
            val fallbackReport = DataReliabilityEngine.processAndVerify(RawExtractedMetadata(), rawUrl)
            SafeReportBuilder.buildResultFromVerifiedReport(fallbackReport, rawUrl)
        }
    }
}
