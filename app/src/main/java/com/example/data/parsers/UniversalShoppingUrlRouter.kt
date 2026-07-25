package com.example.data.parsers

import android.util.Log
import java.net.URI
import java.util.regex.Pattern

/**
 * STEP 1 — UNIVERSAL URL ROUTER ENGINE
 * Responsible ONLY for URL validation, normalization, domain detection, and parser routing.
 * Does NOT perform product extraction or report generation.
 */
object UniversalShoppingUrlRouter {

    private const val TAG = "UniversalUrlRouter"

    sealed class RouteResult {
        data class Routed(
            val normalizedUrl: String,
            val platform: ShoppingPlatform,
            val parser: IShoppingPlatformParser
        ) : RouteResult()

        data class Unsupported(
            val rawUrl: String,
            val reason: String = "This shopping website is not supported yet."
        ) : RouteResult()

        data class Invalid(
            val reason: String = "Invalid or broken shopping link."
        ) : RouteResult()
    }

    private val NON_SHOPPING_DOMAINS = listOf(
        "google.com", "google.co.in", "youtube.com", "youtu.be",
        "facebook.com", "fb.com", "instagram.com", "x.com", "twitter.com",
        "linkedin.com", "reddit.com", "wikipedia.org", "github.com",
        "stackoverflow.com", "bing.com", "yahoo.com", "duckduckgo.com"
    )

    private val KNOWN_BRAND_DOMAINS = listOf(
        "nike.com", "adidas.com", "adidas.co.in", "puma.com", "zara.com", "hm.com",
        "lifestylestores.com", "croma.com", "reliancedigital.in", "tatacliq.com",
        "boat-lifestyle.com", "sugarcosmetics.com", "mamaearth.in", "snitch.co.in",
        "allensolly.com", "bewakoof.com", "urbanic.com", "lenskart.com", "decathlon.in"
    )

    /**
     * Normalizes a raw input string (which may contain WhatsApp shared text, tracking parameters, etc.)
     * and extracts the clean URL.
     */
    fun normalizeUrl(rawInput: String?): String? {
        if (rawInput.isNullOrBlank()) return null

        val trimmed = rawInput.trim()

        // 1. Extract URL if embedded in shared text (e.g. WhatsApp / Social shares)
        val urlPattern = Pattern.compile("https?://[^\\s]+", Pattern.CASE_INSENSITIVE)
        val matcher = urlPattern.matcher(trimmed)
        val extractedUrl = if (matcher.find()) matcher.group(0) else trimmed

        // Ensure valid scheme
        val urlWithScheme = if (!extractedUrl.startsWith("http://", ignoreCase = true) && !extractedUrl.startsWith("https://", ignoreCase = true)) {
            if (extractedUrl.contains(".")) "https://$extractedUrl" else return null
        } else {
            extractedUrl
        }

        // 2. Clean tracking parameters (utm_*, fbclid, gclid, ref_, share_id, etc.)
        return stripTrackingParams(urlWithScheme)
    }

    private fun stripTrackingParams(urlStr: String): String {
        return try {
            val uri = URI(urlStr)
            val host = uri.host ?: return urlStr
            val path = uri.path ?: ""
            val query = uri.query

            if (query.isNullOrBlank()) {
                return urlStr
            }

            val trackingKeys = setOf(
                "utm_source", "utm_medium", "utm_campaign", "utm_term", "utm_content",
                "fbclid", "gclid", "ref", "ref_", "tag", "share_id", "igshid",
                "pf_rd_r", "pf_rd_p", "pf_rd_m", "pf_rd_s", "pf_rd_t", "pf_rd_i"
            )

            val cleanParams = query.split("&").filter { param ->
                val key = param.substringBefore("=").lowercase()
                !trackingKeys.contains(key) && !key.startsWith("utm_")
            }

            val newQuery = if (cleanParams.isNotEmpty()) "?" + cleanParams.joinToString("&") else ""
            "${uri.scheme}://$host$path$newQuery"
        } catch (e: Exception) {
            urlStr
        }
    }

    /**
     * Validates and routes an incoming URL to the correct platform parser.
     */
    fun detectAndRoute(rawInput: String?): RouteResult {
        val normalizedUrl = normalizeUrl(rawInput)

        if (normalizedUrl == null) {
            Log.d(TAG, "Routing Failure: Invalid or empty URL input")
            return RouteResult.Invalid("Invalid or empty link provided.")
        }

        val lowerUrl = normalizedUrl.lowercase()

        // 1. Check for non-shopping domains
        if (NON_SHOPPING_DOMAINS.any { domain -> lowerUrl.contains(domain) }) {
            Log.d(TAG, "Routing Failure: Non-shopping URL ($normalizedUrl)")
            return RouteResult.Unsupported(normalizedUrl, "Non-shopping URL provided.")
        }

        // 2. Smart Platform Routing
        val platform = when {
            // Flipkart
            lowerUrl.contains("flipkart.com") || lowerUrl.contains("fkrt.it") || lowerUrl.contains("shopsy.in") -> {
                ShoppingPlatform.FLIPKART
            }
            // Amazon
            lowerUrl.contains("amazon.in") || lowerUrl.contains("amazon.com") || lowerUrl.contains("amzn.to") || lowerUrl.contains("amzn.in") || lowerUrl.contains("a.co") -> {
                ShoppingPlatform.AMAZON
            }
            // Meesho
            lowerUrl.contains("meesho.com") || lowerUrl.contains("meesho.app.link") -> {
                ShoppingPlatform.MEESHO
            }
            // Myntra
            lowerUrl.contains("myntra.com") || lowerUrl.contains("myntra.app.link") -> {
                ShoppingPlatform.MYNTRA
            }
            // AJIO
            lowerUrl.contains("ajio.com") || lowerUrl.contains("ajio.page.link") -> {
                ShoppingPlatform.AJIO
            }
            // Nykaa
            lowerUrl.contains("nykaa.com") || lowerUrl.contains("nykaa.app.link") -> {
                ShoppingPlatform.NYKAA
            }
            // Snapdeal
            lowerUrl.contains("snapdeal.com") -> {
                ShoppingPlatform.SNAPDEAL
            }
            // Shopify
            lowerUrl.contains("myshopify.com") -> {
                ShoppingPlatform.SHOPIFY
            }
            // Known Official Brand Stores or General E-Commerce Store pattern
            KNOWN_BRAND_DOMAINS.any { domain -> lowerUrl.contains(domain) } || lowerUrl.contains("/product/") || lowerUrl.contains("/p/") || lowerUrl.contains("/dp/") -> {
                ShoppingPlatform.OFFICIAL_BRAND
            }
            else -> {
                Log.d(TAG, "Routing Failure: Unsupported Shopping Domain ($normalizedUrl)")
                return RouteResult.Unsupported(normalizedUrl, "This shopping website is not supported yet.")
            }
        }

        val parser: IShoppingPlatformParser = when (platform) {
            ShoppingPlatform.FLIPKART -> FlipkartParser
            ShoppingPlatform.AMAZON -> AmazonParser
            ShoppingPlatform.MEESHO -> MeeshoParser
            ShoppingPlatform.MYNTRA -> MyntraParser
            ShoppingPlatform.AJIO -> AjioParser
            ShoppingPlatform.NYKAA -> NykaaParser
            ShoppingPlatform.SNAPDEAL -> SnapdealParser
            ShoppingPlatform.SHOPIFY -> ShopifyParser
            ShoppingPlatform.OFFICIAL_BRAND -> OfficialBrandStoreParser
        }

        // Internal Logging
        Log.d(TAG, "Detected Platform: ${platform.displayName}")
        Log.d(TAG, "Parser Selected: ${parser::class.java.simpleName}")
        Log.d(TAG, "Routing Success: $normalizedUrl -> ${platform.displayName}")

        return RouteResult.Routed(
            normalizedUrl = normalizedUrl,
            platform = platform,
            parser = parser
        )
    }
}
