package com.example.data.merchant

import com.example.data.MerchantDetector
import com.example.data.MerchantRegistry
import java.net.URI
import java.util.Locale

/**
 * PHASE 13A — Merchant Validation Engine
 * Ensures merchant display names are strictly clean, human-readable brand/store names.
 * Never displays: "https", "www", "dl", raw URLs, or tracking IDs.
 */
object MerchantValidationEngine {

    private val REJECTED_MERCHANT_TOKENS = setOf(
        "http", "https", "www", "dl", "m", "mobile", "pdp", "search", "shop", "store",
        "unknown", "undefined", "null", "item", "product", "details"
    )

    /**
     * Cleans and validates merchant display name.
     */
    fun getCleanMerchantName(rawMerchant: String?, url: String): String {
        // 1. Check known registry via URL analysis first
        val urlAnalysis = MerchantDetector.analyzeUrl(url)
        val registryMerchant = urlAnalysis.merchantInfo.merchantName
        if (registryMerchant != "Unknown Store" && registryMerchant != "Online Store") {
            return registryMerchant
        }

        // 2. Validate provided raw merchant name
        if (!rawMerchant.isNullOrBlank()) {
            val clean = rawMerchant.trim()
            val lower = clean.lowercase(Locale.getDefault())

            if (!clean.contains("://") && !clean.contains(".") && !REJECTED_MERCHANT_TOKENS.contains(lower)) {
                return formatMerchantTitleCase(clean)
            }
        }

        // 3. Infer clean merchant name from URL domain host
        return extractCleanMerchantFromDomain(url)
    }

    private fun extractCleanMerchantFromDomain(urlStr: String): String {
        return try {
            val uri = URI(if (!urlStr.startsWith("http")) "https://$urlStr" else urlStr)
            val host = uri.host ?: return "Online Store"
            val parts = host.split(".").filter { part ->
                !part.equals("www", ignoreCase = true) &&
                        !part.equals("m", ignoreCase = true) &&
                        !part.equals("dl", ignoreCase = true) &&
                        !part.equals("com", ignoreCase = true) &&
                        !part.equals("in", ignoreCase = true) &&
                        !part.equals("co", ignoreCase = true) &&
                        !part.equals("org", ignoreCase = true) &&
                        !part.equals("net", ignoreCase = true) &&
                        !part.equals("store", ignoreCase = true)
            }

            val primaryPart = parts.firstOrNull { it.length >= 3 } ?: parts.firstOrNull() ?: "Online Store"
            formatMerchantTitleCase(primaryPart)
        } catch (e: Exception) {
            "Online Store"
        }
    }

    private fun formatMerchantTitleCase(token: String): String {
        return token.split(Regex("[\\s_\\-]"))
            .filter { it.isNotBlank() }
            .joinToString(" ") { word ->
                word.lowercase(Locale.getDefault()).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            }
            .takeIf { it.isNotBlank() } ?: "Online Store"
    }
}
