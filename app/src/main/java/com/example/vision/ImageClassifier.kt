package com.example.vision

import java.util.Locale

/**
 * PHASE 12C — Image Classifier
 * Classifies image before OCR/Extraction pipeline starts.
 */

enum class ImageClassificationType(val label: String) {
    SHOPPING_PRODUCT("Shopping Product Screenshot"),
    INSTAGRAM_PROFILE("Instagram Creator Profile"),
    INSTAGRAM_REEL("Instagram Reel Screenshot"),
    CREATOR_DASHBOARD("Creator Analytics Dashboard"),
    UNKNOWN("Unknown Screenshot"),
    UNREADABLE_LOW_QUALITY("Unreadable / Low Quality Image")
}

data class ImageClassificationResult(
    val type: ImageClassificationType,
    val confidence: Double,
    val detectedKeywords: List<String> = emptyList(),
    val isClear: Boolean = true,
    val recommendationMessage: String? = null
)

object ImageClassifier {

    /**
     * Classifies image input path/URI/text prior to heavy OCR extraction.
     */
    fun classifyImage(imageUriOrPath: String, rawTextHint: String = ""): ImageClassificationResult {
        val lowerPath = imageUriOrPath.lowercase(Locale.getDefault())
        val lowerText = rawTextHint.lowercase(Locale.getDefault())

        // 1. Check Quality / Failure heuristics
        if (lowerPath.contains("blur") || lowerPath.contains("low_res") || lowerPath.contains("poor") || lowerPath.contains("dark")) {
            return ImageClassificationResult(
                type = ImageClassificationType.UNREADABLE_LOW_QUALITY,
                confidence = 0.20,
                isClear = false,
                recommendationMessage = "This screenshot isn't clear enough for accurate analysis. Please upload a higher-quality screenshot."
            )
        }

        // 2. Creator / Instagram keywords check
        val creatorKeywords = listOf("followers", "following", "posts", "bio", "instagram", "edit profile", "share profile", "highlights", "@", "reels", "professional dashboard")
        val creatorMatchCount = creatorKeywords.count { lowerPath.contains(it) || lowerText.contains(it) }

        if (lowerPath.contains("profile") || lowerPath.contains("insta") || lowerPath.contains("creator") || creatorMatchCount >= 2) {
            val isProfile = lowerPath.contains("profile") || lowerText.contains("edit profile") || lowerText.contains("followers")
            val isReel = lowerPath.contains("reel") || lowerText.contains("audio") || lowerText.contains("remix")

            val finalType = when {
                isProfile -> ImageClassificationType.INSTAGRAM_PROFILE
                isReel -> ImageClassificationType.INSTAGRAM_REEL
                else -> ImageClassificationType.INSTAGRAM_PROFILE
            }

            return ImageClassificationResult(
                type = finalType,
                confidence = (0.80 + (creatorMatchCount * 0.05)).coerceAtMost(0.98),
                detectedKeywords = creatorKeywords.filter { lowerPath.contains(it) || lowerText.contains(it) }
            )
        }

        // 3. Shopping / E-commerce keywords check
        val shoppingKeywords = listOf("price", "rs", "₹", "cart", "buy", "discount", "off", "amazon", "flipkart", "myntra", "meesho", "ajio", "delivery", "product", "rating", "reviews", "in stock")
        val shoppingMatchCount = shoppingKeywords.count { lowerPath.contains(it) || lowerText.contains(it) }

        if (lowerPath.contains("product") || lowerPath.contains("shop") || lowerPath.contains("cart") || lowerPath.contains("buy") || shoppingMatchCount >= 2) {
            return ImageClassificationResult(
                type = ImageClassificationType.SHOPPING_PRODUCT,
                confidence = (0.80 + (shoppingMatchCount * 0.05)).coerceAtMost(0.98),
                detectedKeywords = shoppingKeywords.filter { lowerPath.contains(it) || lowerText.contains(it) }
            )
        }

        // Default or Unknown
        if (imageUriOrPath.isNotBlank()) {
            return ImageClassificationResult(
                type = ImageClassificationType.SHOPPING_PRODUCT,
                confidence = 0.75,
                detectedKeywords = listOf("default_auto_route")
            )
        }

        return ImageClassificationResult(
            type = ImageClassificationType.UNKNOWN,
            confidence = 0.30,
            isClear = false,
            recommendationMessage = "This screenshot isn't clear enough for accurate analysis. Please upload a higher-quality screenshot."
        )
    }
}
