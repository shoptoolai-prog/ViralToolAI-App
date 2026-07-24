package com.example.data

import java.net.URI
import java.util.Locale
import java.util.regex.Pattern

/**
 * PHASE 12B — Universal Merchant Detector & URL Normalizer Engine
 */
data class NormalizedUrlResult(
    val originalUrl: String,
    val normalizedUrl: String,
    val domain: String,
    val isProductPage: Boolean,
    val isShortenedUrl: Boolean,
    val isSocialPlatform: Boolean,
    val merchantInfo: MerchantInfo,
    val canonicalUrl: String = normalizedUrl,
    val ogSiteName: String? = null,
    val metaTitle: String? = null,
    val ogImageUrl: String? = null
)

object MerchantDetector {

    // Regex to extract URL from raw shared text
    private val URL_PATTERN = Pattern.compile(
        "(https?://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(?:/[^\\s]*)?)|(www\\.[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(?:/[^\\s]*)?)"
    )

    /**
     * Normalizes and extracts URL from raw text (e.g., shared from Android Share Intent).
     */
    fun extractUrlFromText(rawText: String): String {
        val trimmed = rawText.trim()
        if (trimmed.isEmpty()) return ""

        val matcher = URL_PATTERN.matcher(trimmed)
        if (matcher.find()) {
            val url = matcher.group(0) ?: ""
            return if (!url.startsWith("http://") && !url.startsWith("https://")) {
                "https://$url"
            } else {
                url
            }
        }

        return if (trimmed.contains(".")) {
            if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) "https://$trimmed" else trimmed
        } else {
            trimmed
        }
    }

    /**
     * Cleans tracking query parameters from URL (e.g., utm_source, aff_id, ref, igsh, fbclid, tag).
     */
    fun normalizeUrl(rawUrl: String): String {
        val extracted = extractUrlFromText(rawUrl)
        if (extracted.isEmpty()) return ""

        return try {
            val uri = URI(extracted)
            val scheme = uri.scheme ?: "https"
            val host = uri.host ?: return extracted
            val path = uri.path ?: ""
            val rawQuery = uri.query

            val cleanQuery = if (!rawQuery.isNullOrBlank()) {
                rawQuery.split("&")
                    .filterNot { param ->
                        val lower = param.lowercase(Locale.getDefault())
                        lower.startsWith("utm_") ||
                                lower.startsWith("aff") ||
                                lower.startsWith("ref") ||
                                lower.startsWith("tag=") ||
                                lower.startsWith("igsh=") ||
                                lower.startsWith("fbclid=") ||
                                lower.startsWith("gclid=") ||
                                lower.startsWith("gbraid=") ||
                                lower.startsWith("wbraid=") ||
                                lower.startsWith("s=") ||
                                lower.startsWith("share=") ||
                                lower.startsWith("feature=") ||
                                lower.startsWith("_r=")
                    }
                    .joinToString("&")
            } else {
                ""
            }

            val queryPart = if (cleanQuery.isNotEmpty()) "?$cleanQuery" else ""
            "$scheme://$host$path$queryPart"
        } catch (e: Exception) {
            extracted
        }
    }

    /**
     * Multi-source Universal Merchant Detection Engine (Phase 12B).
     */
    fun analyzeUrl(rawInput: String): NormalizedUrlResult {
        val cleanUrl = normalizeUrl(rawInput)
        val lowerUrl = cleanUrl.lowercase(Locale.getDefault())

        // Determine if shortened
        val isShortened = lowerUrl.contains("amzn.to") ||
                lowerUrl.contains("fkrt.it") ||
                lowerUrl.contains("bit.ly") ||
                lowerUrl.contains("t.co") ||
                lowerUrl.contains("pin.it") ||
                lowerUrl.contains("fb.me") ||
                lowerUrl.contains("meesho.com/s/") ||
                lowerUrl.contains("ajio.page.link")

        // Social platform check
        val isSocial = lowerUrl.contains("instagram.com") ||
                lowerUrl.contains("instagr.am") ||
                lowerUrl.contains("facebook.com") ||
                lowerUrl.contains("pinterest.com") ||
                lowerUrl.contains("youtube.com") ||
                lowerUrl.contains("youtu.be") ||
                lowerUrl.contains("t.me")

        // Domain extraction
        var host = ""
        try {
            val uri = URI(if (!cleanUrl.startsWith("http")) "https://$cleanUrl" else cleanUrl)
            host = uri.host ?: ""
        } catch (e: Exception) {
            var temp = cleanUrl.removePrefix("https://").removePrefix("http://")
            val sIdx = temp.indexOf('/')
            if (sIdx != -1) temp = temp.substring(0, sIdx)
            val cIdx = temp.indexOf(':')
            if (cIdx != -1) temp = temp.substring(0, cIdx)
            host = temp
        }

        val cleanHost = host.removePrefix("www.")
            .removePrefix("m.")
            .removePrefix("shop.")
            .removePrefix("dl.")
            .ifBlank { "unknown-store.com" }

        // Product page detection heuristic
        val isProduct = lowerUrl.contains("/p/") ||
                lowerUrl.contains("/dp/") ||
                lowerUrl.contains("/item/") ||
                lowerUrl.contains("/product/") ||
                lowerUrl.contains("/buy/") ||
                lowerUrl.contains("/pr?") ||
                lowerUrl.contains("pdp") ||
                lowerUrl.contains("reel") ||
                lowerUrl.contains("marketplace") ||
                isShortened

        // Phase 12B Multi-Source Resolution Priority Strategy
        val merchantInfo = when {
            lowerUrl.contains("amzn.to") || lowerUrl.contains("amazon") -> {
                if (lowerUrl.contains("mamaearth")) MerchantRegistry.findMerchant("Mamaearth Amazon")
                else MerchantRegistry.findMerchant("Amazon")
            }
            lowerUrl.contains("fkrt.it") || lowerUrl.contains("flipkart") || lowerUrl.contains("dl.flipkart") -> MerchantRegistry.findMerchant("Flipkart")
            lowerUrl.contains("shopsy") -> MerchantRegistry.findMerchant("Shopsy")
            lowerUrl.contains("mamaearth") -> {
                if (lowerUrl.contains("amazon")) MerchantRegistry.findMerchant("Mamaearth Amazon")
                else MerchantRegistry.findMerchant("Mamaearth Store")
            }
            lowerUrl.contains("meesho") -> MerchantRegistry.findMerchant("Meesho")
            lowerUrl.contains("ajio") -> MerchantRegistry.findMerchant("AJIO")
            lowerUrl.contains("myntra") -> MerchantRegistry.findMerchant("Myntra")
            lowerUrl.contains("snitch") -> MerchantRegistry.findMerchant("Snitch")
            lowerUrl.contains("allensolly") -> MerchantRegistry.findMerchant("Allen Solly")
            lowerUrl.contains("nike") -> MerchantRegistry.findMerchant("Nike")
            lowerUrl.contains("adidas") -> MerchantRegistry.findMerchant("Adidas")
            lowerUrl.contains("zara") -> MerchantRegistry.findMerchant("Zara")
            lowerUrl.contains("hm.com") || lowerUrl.contains("h&m") -> MerchantRegistry.findMerchant("H&M")
            lowerUrl.contains("apple") -> MerchantRegistry.findMerchant("Apple")
            lowerUrl.contains("samsung") -> MerchantRegistry.findMerchant("Samsung")
            lowerUrl.contains("reliancedigital") || lowerUrl.contains("reliance") -> MerchantRegistry.findMerchant("Reliance Digital")
            lowerUrl.contains("croma") -> MerchantRegistry.findMerchant("Croma")
            lowerUrl.contains("nykaa") -> MerchantRegistry.findMerchant("Nykaa")
            lowerUrl.contains("tatacliq") -> MerchantRegistry.findMerchant("Tata CLiQ")
            lowerUrl.contains("puma") -> MerchantRegistry.findMerchant("Puma")
            lowerUrl.contains("boat") -> MerchantRegistry.findMerchant("Boat")
            lowerUrl.contains("gonoise") || lowerUrl.contains("noise") -> MerchantRegistry.findMerchant("Noise")
            lowerUrl.contains("oneplus") -> MerchantRegistry.findMerchant("OnePlus")
            lowerUrl.contains("lenovo") -> MerchantRegistry.findMerchant("Lenovo")
            lowerUrl.contains("dell") -> MerchantRegistry.findMerchant("Dell")
            lowerUrl.contains("hp.com") -> MerchantRegistry.findMerchant("HP")
            lowerUrl.contains("asus") -> MerchantRegistry.findMerchant("Asus")
            lowerUrl.contains("acer") -> MerchantRegistry.findMerchant("Acer")
            lowerUrl.contains("jbl") -> MerchantRegistry.findMerchant("JBL")
            lowerUrl.contains("sony") -> MerchantRegistry.findMerchant("Sony")
            lowerUrl.contains("instagram") || lowerUrl.contains("instagr.am") -> MerchantRegistry.findMerchant("Instagram")
            lowerUrl.contains("facebook") || lowerUrl.contains("fb.me") -> MerchantRegistry.findMerchant("Facebook Marketplace")
            lowerUrl.contains("pinterest") || lowerUrl.contains("pin.it") -> MerchantRegistry.findMerchant("Pinterest")
            lowerUrl.contains("youtube") || lowerUrl.contains("youtu.be") -> MerchantRegistry.findMerchant("YouTube Product")
            lowerUrl.contains("t.me") -> MerchantRegistry.findMerchant("Telegram Link")
            cleanHost == "unknown-store.com" || cleanHost == "dl" || cleanHost == "https" -> MerchantRegistry.getUnknownMerchantFallback(cleanUrl)
            else -> MerchantRegistry.findMerchant(cleanHost)
        }

        return NormalizedUrlResult(
            originalUrl = rawInput,
            normalizedUrl = cleanUrl,
            domain = cleanHost,
            isProductPage = isProduct,
            isShortenedUrl = isShortened,
            isSocialPlatform = isSocial,
            merchantInfo = merchantInfo,
            canonicalUrl = cleanUrl
        )
    }
}

