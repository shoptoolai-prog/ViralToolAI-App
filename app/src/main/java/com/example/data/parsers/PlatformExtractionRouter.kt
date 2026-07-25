package com.example.data.parsers

import com.example.data.extraction.RawExtractedMetadata

/**
 * MASTER PHASE V4.5 — Platform Extraction Router
 * Directs incoming URL to platform-specific dedicated parser after platform detection.
 */
object PlatformExtractionRouter {

    fun routeAndExtract(
        url: String,
        htmlContent: String? = null,
        rawMeta: RawExtractedMetadata? = null
    ): ExtractedProductData {
        val routeResult = UniversalShoppingUrlRouter.detectAndRoute(url)

        return when (routeResult) {
            is UniversalShoppingUrlRouter.RouteResult.Routed -> {
                val cleanUrl = routeResult.normalizedUrl
                val parser = routeResult.parser
                val extracted = parser.extract(cleanUrl, htmlContent, rawMeta)

                // Strict HTTPS Bug Fix: Title must never be a raw URL or "https..."
                val validatedTitle = if (extracted.title != null && (extracted.title.startsWith("http://") || extracted.title.startsWith("https://") || extracted.title.contains("://") || extracted.title.startsWith("www."))) {
                    null
                } else {
                    extracted.title
                }

                val isVerified = !validatedTitle.isNullOrBlank() && extracted.currentPrice != null && extracted.currentPrice > 0.0

                extracted.copy(
                    title = validatedTitle,
                    isVerified = isVerified,
                    confidenceScore = if (isVerified) extracted.confidenceScore.coerceAtLeast(80) else 0
                )
            }

            is UniversalShoppingUrlRouter.RouteResult.Unsupported -> {
                ExtractedProductData(
                    platform = ShoppingPlatform.OFFICIAL_BRAND,
                    title = null,
                    currentPrice = null,
                    imageUrl = null,
                    merchantName = "Unsupported Store",
                    isVerified = false,
                    confidenceScore = 0
                )
            }

            is UniversalShoppingUrlRouter.RouteResult.Invalid -> {
                ExtractedProductData(
                    platform = ShoppingPlatform.OFFICIAL_BRAND,
                    title = null,
                    currentPrice = null,
                    imageUrl = null,
                    merchantName = "Invalid Store",
                    isVerified = false,
                    confidenceScore = 0
                )
            }
        }
    }
}
