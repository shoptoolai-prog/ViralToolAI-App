package com.example.data.extraction

import com.example.data.ShoppingResult
import com.example.data.integration.RealDataIntegrationLayer

/**
 * PHASE 12E & 13A — Real Extraction Pipeline Engine
 * Delegates to RealDataIntegrationLayer for unified link resolution,
 * HTML parsing, validation, and safe report generation.
 */
object RealExtractionPipeline {

    /**
     * Coroutine-friendly async extraction pipeline.
     */
    suspend fun extractProductFromUrl(url: String): ShoppingResult {
        return RealDataIntegrationLayer.getProductData(url)
    }

    /**
     * Synchronous extraction method for legacy UI callers.
     */
    fun extractProductFromUrlSync(url: String): ShoppingResult {
        return RealDataIntegrationLayer.getProductDataSync(url)
    }
}

