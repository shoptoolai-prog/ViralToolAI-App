package com.example.data.resolver

import com.example.data.MerchantDetector
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URI
import java.util.concurrent.TimeUnit

/**
 * PHASE 13A — Universal Link Resolution Engine
 * Automatically resolves:
 * - Shortened / Redirect URLs (amzn.to, fkrt.it, bit.ly, onelink.me)
 * - Tracking / Affiliate parameter stripping (utm_*, gclid, fbclid, tag, affid)
 * - Mobile / App deep link URL normalization
 */
object LinkResolutionEngine {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .build()

    private val TRACKING_PARAMS = setOf(
        "utm_source", "utm_medium", "utm_campaign", "utm_term", "utm_content",
        "gclid", "fbclid", "affid", "tag", "ref", "clickid", "srsltid", "pdp_npi",
        "cmpid", "pf_rd_r", "pf_rd_p", "pd_rd_w", "pd_rd_r", "pd_rd_i"
    )

    /**
     * Resolves redirects and strips tracking parameters to yield a clean product URL.
     */
    fun resolveCleanProductUrl(rawUrl: String): String {
        val extractedUrl = MerchantDetector.extractUrlFromText(rawUrl.trim())
        if (extractedUrl.isBlank()) return rawUrl

        var targetUrl = if (!extractedUrl.startsWith("http://") && !extractedUrl.startsWith("https://")) {
            "https://$extractedUrl"
        } else {
            extractedUrl
        }

        // 1. Follow HTTP Redirects for shortened/affiliate URLs
        if (isShortOrRedirectDomain(targetUrl)) {
            try {
                val request = Request.Builder()
                    .url(targetUrl)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .head()
                    .build()

                httpClient.newCall(request).execute().use { response ->
                    val finalUrl = response.request.url.toString()
                    if (finalUrl.isNotBlank() && finalUrl.startsWith("http")) {
                        targetUrl = finalUrl
                    }
                }
            } catch (e: Exception) {
                // Ignore network errors during head resolve, fallback to original
            }
        }

        // 2. Clean tracking parameters
        return stripTrackingParameters(targetUrl)
    }

    private fun isShortOrRedirectDomain(url: String): Boolean {
        val lower = url.lowercase()
        return lower.contains("amzn.to") ||
                lower.contains("fkrt.it") ||
                lower.contains("bit.ly") ||
                lower.contains("tinyurl.com") ||
                lower.contains("onelink.me") ||
                lower.contains("dl.flipkart.com") ||
                lower.contains("t.co") ||
                lower.contains("goo.gl")
    }

    fun stripTrackingParameters(urlStr: String): String {
        return try {
            val uri = URI(urlStr)
            val query = uri.rawQuery ?: return urlStr
            if (query.isBlank()) return urlStr

            val cleanQuery = query.split("&")
                .mapNotNull { pair ->
                    val parts = pair.split("=", limit = 2)
                    val key = parts[0].lowercase()
                    if (TRACKING_PARAMS.contains(key)) null else pair
                }
                .joinToString("&")

            val newUri = URI(
                uri.scheme,
                uri.authority,
                uri.path,
                if (cleanQuery.isBlank()) null else cleanQuery,
                null
            )
            newUri.toString()
        } catch (e: Exception) {
            urlStr
        }
    }
}
