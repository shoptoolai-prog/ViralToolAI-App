package com.example.data

import java.net.URI
import java.util.Locale

/**
 * SHOPTOOLAI Phase 7A.5 — Universal Shopping Link Resolver
 *
 * Accepts ALL valid shopping links including deep links, shortened links,
 * redirect links, affiliate links, dl.*, onelink.*, fkrt.it, amzn.to, etc.
 * No hardcoded whitelist.
 */
data class ShoppingValidationResult(
    val isValid: Boolean,
    val merchantName: String? = null,
    val errorTitle: String = "We couldn't detect a shopping product on the final page.",
    val errorSubtitle: String = "Please paste a direct product link or valid shopping share link from any online store."
)

object ShoppingUrlValidator {

    fun validate(rawInput: String): ShoppingValidationResult {
        val trimmed = rawInput.trim()
        if (trimmed.isEmpty()) {
            return ShoppingValidationResult(
                isValid = false,
                errorTitle = "We couldn't detect a shopping product on the final page.",
                errorSubtitle = "Please paste a direct product link or valid shopping share link from any online store."
            )
        }

        val extracted = MerchantDetector.extractUrlFromText(trimmed)
        val lower = extracted.lowercase(Locale.getDefault())

        // REJECT ONLY explicit social media profile/chat pages, Google search, YouTube
        val isForbiddenSocialOrSearch = lower.contains("instagram.com/profile") ||
                (lower.contains("instagram.com/") && !lower.contains("/p/") && !lower.contains("/reel/")) ||
                lower.contains("instagr.am/profile") ||
                lower.contains("google.com/search") ||
                lower.contains("google.co.in/search") ||
                lower.contains("youtube.com/watch") ||
                lower.contains("youtube.com/channel") ||
                lower.contains("youtu.be") ||
                lower.contains("facebook.com/profile") ||
                lower.contains("twitter.com") ||
                lower.contains("x.com") ||
                lower.contains("wa.me") ||
                lower.contains("whatsapp.com") ||
                lower.contains("reddit.com")

        if (isForbiddenSocialOrSearch) {
            return ShoppingValidationResult(
                isValid = false,
                errorTitle = "We couldn't detect a shopping product on the final page.",
                errorSubtitle = "Social media profile and search engine links do not contain shopping products. Please paste a product link from an online store."
            )
        }

        // STEP 1: Accept if starts with http/https or contains valid domain dot structure
        val hasUrlStructure = extracted.startsWith("http://") || 
                extracted.startsWith("https://") || 
                (extracted.contains(".") && !extracted.contains(" "))

        if (!hasUrlStructure) {
            return ShoppingValidationResult(
                isValid = false,
                errorTitle = "We couldn't detect a shopping product on the final page.",
                errorSubtitle = "Please paste a valid web URL or product link from any online shopping store."
            )
        }

        // Detect Merchant for display
        val analysis = MerchantDetector.analyzeUrl(extracted)
        val detectedMerchantName = analysis.merchantInfo.merchantName
        val finalMerchant = if (detectedMerchantName != "Unknown Store") detectedMerchantName else "Online Store"

        // ALWAYS ACCEPT valid HTTP/HTTPS URLs for background resolution and analysis
        return ShoppingValidationResult(
            isValid = true,
            merchantName = finalMerchant
        )
    }
}



