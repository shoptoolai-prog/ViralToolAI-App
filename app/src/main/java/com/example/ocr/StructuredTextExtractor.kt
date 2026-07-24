package com.example.ocr

import java.util.regex.Pattern

/**
 * SHOPTOOLAI Phase 8C — Structured Text Extraction Engine
 * Parses raw extracted OCR text lines into strongly-typed VerifiedField objects.
 * 
 * Rules:
 * - Uses strict pattern matching for username @handles, follower/following K/M multipliers, prices, discounts, ratings.
 * - Unreadable or ambiguous fields marked as VerifiedField.unverified().
 * - Never invents fake values.
 */

object StructuredTextExtractor {

    private val USERNAME_REGEX = Pattern.compile("(?i)@?([a-zA-Z0-9_\\.]{3,30})")
    private val FOLLOWERS_REGEX = Pattern.compile("(?i)([0-9,]+(?:\\.[0-9]+)?)\\s*([KMkB])?\\s*(?:followers|follower|subscribers)")
    private val FOLLOWING_REGEX = Pattern.compile("(?i)([0-9,]+(?:\\.[0-9]+)?)\\s*([KMkB])?\\s*(?:following)")
    private val POSTS_REGEX = Pattern.compile("(?i)([0-9,]+(?:\\.[0-9]+)?)\\s*([KMkB])?\\s*(?:posts|post|uploads)")
    private val CATEGORY_REGEX = Pattern.compile("(?i)(digital creator|shopping & retail|public figure|video creator|entrepreneur|blogger|artist|clothing brand)")
    private val PRICE_REGEX = Pattern.compile("(?i)(?:₹|\\$|EUR|INR|Rs\\.?)\\s*([0-9,]+(?:\\.[0-9]{2})?)")
    private val DISCOUNT_REGEX = Pattern.compile("(?i)(\\d{1,2})%\\s*(?:off|discount)")
    private val RATING_REGEX = Pattern.compile("(?i)(\\d\\.\\d)\\s*(?:\\/\\s*5|★|stars)")

    /**
     * Extracts Structured Creator Profile Data from raw OCR lines
     */
    fun extractCreatorProfile(lines: List<String>, providerName: String = "Universal OCR"): StructuredCreatorOcrData {
        var usernameField = VerifiedField.unverified<String>(providerName)
        var displayNameField = VerifiedField.unverified<String>(providerName)
        var bioField = VerifiedField.unverified<String>(providerName)
        var followersField = VerifiedField.unverified<Long>(providerName)
        var followingField = VerifiedField.unverified<Long>(providerName)
        var postsField = VerifiedField.unverified<Int>(providerName)
        var categoryField = VerifiedField.unverified<String>(providerName)
        val actionButtons = mutableListOf<VerifiedField<String>>()

        val bioLines = mutableListOf<String>()

        lines.forEachIndexed { index, line ->
            val trimmed = line.trim()
            if (trimmed.isBlank()) return@forEachIndexed

            // Username Check
            if (!usernameField.isVerified) {
                if (trimmed.startsWith("@") && trimmed.length in 3..32) {
                    val handle = trimmed.removePrefix("@")
                    usernameField = VerifiedField.verified("@$handle", confidence = 96, source = providerName)
                } else if (trimmed.contains("username", ignoreCase = true) || trimmed.contains("handle", ignoreCase = true)) {
                    val userMatcher = Pattern.compile("(?i)(?:username|handle):?\\s*@?([a-zA-Z0-9_\\.]{3,30})").matcher(trimmed)
                    if (userMatcher.find()) {
                        val handle = userMatcher.group(1) ?: trimmed
                        usernameField = VerifiedField.verified("@$handle", confidence = 95, source = providerName)
                    }
                } else if (index == 0 && trimmed.matches(Regex("[a-zA-Z0-9_\\.]{3,30}"))) {
                    usernameField = VerifiedField.verified("@$trimmed", confidence = 90, source = providerName)
                }
            }

            // Category Check
            if (!categoryField.isVerified) {
                val catMatcher = CATEGORY_REGEX.matcher(trimmed)
                if (catMatcher.find()) {
                    categoryField = VerifiedField.verified(catMatcher.group(1) ?: trimmed, confidence = 92, source = providerName)
                }
            }

            // Followers Check
            if (!followersField.isVerified) {
                val folMatcher = FOLLOWERS_REGEX.matcher(trimmed)
                if (folMatcher.find()) {
                    val numStr = folMatcher.group(1)?.replace(",", "")
                    val unit = folMatcher.group(2)
                    val parsed = parseMultiplier(numStr, unit)
                    if (parsed != null) {
                        followersField = VerifiedField.verified(parsed, confidence = 94, source = providerName)
                    }
                }
            }

            // Following Check
            if (!followingField.isVerified) {
                val follingMatcher = FOLLOWING_REGEX.matcher(trimmed)
                if (follingMatcher.find()) {
                    val numStr = follingMatcher.group(1)?.replace(",", "")
                    val unit = follingMatcher.group(2)
                    val parsed = parseMultiplier(numStr, unit)
                    if (parsed != null) {
                        followingField = VerifiedField.verified(parsed, confidence = 94, source = providerName)
                    }
                }
            }

            // Posts Check
            if (!postsField.isVerified) {
                val postsMatcher = POSTS_REGEX.matcher(trimmed)
                if (postsMatcher.find()) {
                    val numStr = postsMatcher.group(1)?.replace(",", "")
                    val unit = postsMatcher.group(2)
                    val parsed = parseMultiplier(numStr, unit)?.toInt()
                    if (parsed != null) {
                        postsField = VerifiedField.verified(parsed, confidence = 90, source = providerName)
                    }
                }
            }

            // Buttons Check (Follow, Message, Contact, Shop)
            if (trimmed.equals("Follow", ignoreCase = true) || 
                trimmed.equals("Message", ignoreCase = true) || 
                trimmed.equals("Contact", ignoreCase = true) ||
                trimmed.equals("Email", ignoreCase = true) ||
                trimmed.equals("Edit profile", ignoreCase = true)) {
                actionButtons.add(VerifiedField.verified(trimmed, confidence = 95, source = providerName))
            }

            // Bio collection heuristics
            if (!trimmed.startsWith("@") && !trimmed.contains("followers", ignoreCase = true) && !trimmed.contains("following", ignoreCase = true) && !trimmed.contains("posts", ignoreCase = true) && trimmed.length > 8) {
                if (bioLines.size < 4 && !trimmed.equals("Follow", ignoreCase = true) && !trimmed.equals("Edit profile", ignoreCase = true)) {
                    bioLines.add(trimmed)
                }
            }
        }

        if (bioLines.isNotEmpty()) {
            bioField = VerifiedField.verified(bioLines.joinToString("\n"), confidence = 88, source = providerName)
        }

        return StructuredCreatorOcrData(
            username = usernameField,
            displayName = displayNameField,
            bio = bioField,
            followers = followersField,
            following = followingField,
            posts = postsField,
            category = categoryField,
            actionButtons = actionButtons,
            rawLines = lines
        )
    }

    /**
     * Extracts Structured Shopping Screenshot Data from raw OCR lines
     */
    fun extractShoppingProduct(lines: List<String>, providerName: String = "Universal OCR"): StructuredShoppingOcrData {
        var titleField = VerifiedField.unverified<String>(providerName)
        var brandField = VerifiedField.unverified<String>(providerName)
        var priceField = VerifiedField.unverified<Double>(providerName)
        var discountField = VerifiedField.unverified<Int>(providerName)
        var ratingField = VerifiedField.unverified<Double>(providerName)
        var reviewCountField = VerifiedField.unverified<Int>(providerName)
        var merchantField = VerifiedField.unverified<String>(providerName)

        lines.forEach { line ->
            val trimmed = line.trim()
            if (trimmed.isBlank()) return@forEach

            // Price Check
            if (!priceField.isVerified) {
                val priceMatcher = PRICE_REGEX.matcher(trimmed)
                if (priceMatcher.find()) {
                    val rawVal = priceMatcher.group(1)?.replace(",", "")?.toDoubleOrNull()
                    if (rawVal != null) {
                        priceField = VerifiedField.verified(rawVal, confidence = 95, source = providerName)
                    }
                }
            }

            // Discount Check
            if (!discountField.isVerified) {
                val discMatcher = DISCOUNT_REGEX.matcher(trimmed)
                if (discMatcher.find()) {
                    val rawDisc = discMatcher.group(1)?.toIntOrNull()
                    if (rawDisc != null) {
                        discountField = VerifiedField.verified(rawDisc, confidence = 92, source = providerName)
                    }
                }
            }

            // Rating Check
            if (!ratingField.isVerified) {
                val ratMatcher = RATING_REGEX.matcher(trimmed)
                if (ratMatcher.find()) {
                    val ratingVal = ratMatcher.group(1)?.toDoubleOrNull()
                    if (ratingVal != null && ratingVal <= 5.0) {
                        ratingField = VerifiedField.verified(ratingVal, confidence = 90, source = providerName)
                    }
                }
            }

            // Merchant / Store heuristic
            if (!merchantField.isVerified) {
                if (trimmed.contains("Amazon", ignoreCase = true) || 
                    trimmed.contains("Flipkart", ignoreCase = true) || 
                    trimmed.contains("Myntra", ignoreCase = true) ||
                    trimmed.contains("Ajio", ignoreCase = true) ||
                    trimmed.contains("Nike", ignoreCase = true) ||
                    trimmed.contains("Apple", ignoreCase = true)) {
                    merchantField = VerifiedField.verified(trimmed, confidence = 95, source = providerName)
                }
            }

            // Title Heuristic (Longest line usually)
            if (!titleField.isVerified && trimmed.length in 15..120 && !trimmed.contains("₹") && !trimmed.contains("$")) {
                titleField = VerifiedField.verified(trimmed, confidence = 80, source = providerName)
            }
        }

        return StructuredShoppingOcrData(
            productTitle = titleField,
            brand = brandField,
            price = priceField,
            discountPercent = discountField,
            rating = ratingField,
            reviewCount = reviewCountField,
            merchant = merchantField,
            visibleTextBlocks = lines
        )
    }

    private fun parseMultiplier(numStr: String?, unitStr: String?): Long? {
        if (numStr == null) return null
        val baseVal = numStr.toDoubleOrNull() ?: return null
        return when (unitStr?.uppercase()) {
            "K" -> (baseVal * 1000).toLong()
            "M" -> (baseVal * 1000000).toLong()
            "B" -> (baseVal * 1000000000).toLong()
            else -> baseVal.toLong()
        }
    }
}
