package com.example.data.parsers

import com.example.data.extraction.RawExtractedMetadata

/**
 * MASTER PHASE V4.5 — Supported Shopping Platforms
 */
enum class ShoppingPlatform(
    val displayName: String,
    val domains: List<String>,
    val primaryColor: Long,
    val brandBadge: String,
    val officialLogoUrl: String
) {
    FLIPKART("Flipkart", listOf("flipkart.com", "fkrt.it", "shopsy.in"), 0xFF2874F0, "FK", "https://www.google.com/s2/favicons?domain=flipkart.com&sz=128"),
    AMAZON("Amazon", listOf("amazon.in", "amazon.com", "amzn.to", "amzn.in"), 0xFFFF9900, "AZ", "https://www.google.com/s2/favicons?domain=amazon.in&sz=128"),
    MEESHO("Meesho", listOf("meesho.com"), 0xFFF43397, "MS", "https://www.google.com/s2/favicons?domain=meesho.com&sz=128"),
    MYNTRA("Myntra", listOf("myntra.com"), 0xFFFC2779, "MY", "https://www.google.com/s2/favicons?domain=myntra.com&sz=128"),
    AJIO("AJIO", listOf("ajio.com", "ajio.page.link"), 0xFF2C3E50, "AJIO", "https://www.google.com/s2/favicons?domain=ajio.com&sz=128"),
    NYKAA("Nykaa", listOf("nykaa.com"), 0xFFFC2779, "NYKAA", "https://www.google.com/s2/favicons?domain=nykaa.com&sz=128"),
    SNAPDEAL("Snapdeal", listOf("snapdeal.com"), 0xFFE40046, "SNAP", "https://www.google.com/s2/favicons?domain=snapdeal.com&sz=128"),
    SHOPIFY("Shopify Store", listOf("myshopify.com"), 0xFF95BF47, "SHOPIFY", "https://www.google.com/s2/favicons?domain=shopify.com&sz=128"),
    OFFICIAL_BRAND("Official Brand Store", emptyList(), 0xFF6C5CE7, "BRAND", "https://www.google.com/s2/favicons?domain=google.com&sz=128");

    companion object {
        fun detectPlatform(url: String): ShoppingPlatform {
            val lower = url.lowercase().trim()
            for (platform in values()) {
                if (platform == OFFICIAL_BRAND) continue
                if (platform.domains.any { domain -> lower.contains(domain) }) {
                    return platform
                }
            }
            return OFFICIAL_BRAND
        }
    }
}

/**
 * Verified Product Data Model extracted from dedicated platform parser.
 */
data class ExtractedProductData(
    val platform: ShoppingPlatform,
    val title: String?,
    val currentPrice: Double?,
    val originalPrice: Double? = null,
    val currency: String = "INR",
    val imageUrl: String?,
    val brand: String? = null,
    val rating: Double? = null,
    val reviewsCount: Int? = null,
    val availability: String? = null,
    val deliveryInfo: String? = null,
    val category: String? = null,
    val merchantName: String = platform.displayName,
    val modelNumber: String? = null,
    val variant: String? = null,
    val color: String? = null,
    val size: String? = null,
    val storage: String? = null,
    val isVerified: Boolean = false,
    val confidenceScore: Int = 0,
    val extractionSource: String = "PLATFORM_DEDICATED_PARSER"
)

interface IShoppingPlatformParser {
    val platform: ShoppingPlatform
    fun extract(url: String, htmlContent: String?, rawMeta: RawExtractedMetadata?): ExtractedProductData
}
