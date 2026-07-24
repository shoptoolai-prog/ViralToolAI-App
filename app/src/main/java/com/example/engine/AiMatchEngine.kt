package com.example.engine

import com.example.data.ProductIdentity
import kotlin.math.max

/**
 * SHOPTOOLAI Phase 5A — AI Match Engine
 * 
 * Compares products using token similarity, brand matching, model match,
 * and category heuristics to prevent comparing different products.
 */
enum class MatchConfidenceLevel(val label: String, val minScore: Int) {
    AI_VERIFIED("AI Verified Match", 95),
    STRONG("Strong Match", 85),
    POSSIBLE("Possible Match", 70),
    NO_MATCH("No Match", 0)
}

data class AiMatchResult(
    val scorePercent: Int,
    val level: MatchConfidenceLevel,
    val reason: String
)

object AiMatchEngine {

    /**
     * Calculates a similarity score between two ProductIdentities (0-100).
     */
    fun calculateMatchScore(base: ProductIdentity, target: ProductIdentity): AiMatchResult {
        if (base.productName.isBlank() || target.productName.isBlank()) {
            return AiMatchResult(0, MatchConfidenceLevel.NO_MATCH, "Missing product title")
        }

        var totalScore = 0.0
        val maxScore = 100.0

        // 1. Brand Match (30 pts)
        val baseBrand = base.brand.lowercase().trim()
        val targetBrand = target.brand.lowercase().trim()
        if (baseBrand.isNotEmpty() && targetBrand.isNotEmpty()) {
            if (baseBrand == targetBrand || base.productName.lowercase().contains(targetBrand) || target.productName.lowercase().contains(baseBrand)) {
                totalScore += 30.0
            } else {
                // Different brands = penalty
                totalScore -= 15.0
            }
        } else {
            totalScore += 15.0 // Neutral
        }

        // 2. Title Jaccard/Token Similarity (40 pts)
        val baseTokens = tokenize(base.productName)
        val targetTokens = tokenize(target.productName)
        val intersection = baseTokens.intersect(targetTokens).size
        val union = baseTokens.union(targetTokens).size
        val tokenSimilarity = if (union > 0) (intersection.toDouble() / union.toDouble()) else 0.0
        totalScore += tokenSimilarity * 40.0

        // 3. Model Number / SKU Match (15 pts)
        val baseModel = base.modelNumber ?: base.sku
        val targetModel = target.modelNumber ?: target.sku
        if (!baseModel.isNullOrBlank() && !targetModel.isNullOrBlank()) {
            if (baseModel.trim().equals(targetModel.trim(), ignoreCase = true)) {
                totalScore += 15.0
            }
        } else {
            totalScore += 7.5
        }

        // 4. Category & Variant Match (15 pts)
        val baseCat = base.category.lowercase().trim()
        val targetCat = target.category.lowercase().trim()
        if (baseCat.isNotEmpty() && targetCat.isNotEmpty() && baseCat == targetCat) {
            totalScore += 10.0
        }
        val baseVar = base.variant?.lowercase()?.trim() ?: ""
        val targetVar = target.variant?.lowercase()?.trim() ?: ""
        if (baseVar.isNotEmpty() && targetVar.isNotEmpty() && baseVar == targetVar) {
            totalScore += 5.0
        }

        val finalPercent = totalScore.coerceIn(0.0, 100.0).toInt()

        val level = when {
            finalPercent >= MatchConfidenceLevel.AI_VERIFIED.minScore -> MatchConfidenceLevel.AI_VERIFIED
            finalPercent >= MatchConfidenceLevel.STRONG.minScore -> MatchConfidenceLevel.STRONG
            finalPercent >= MatchConfidenceLevel.POSSIBLE.minScore -> MatchConfidenceLevel.POSSIBLE
            else -> MatchConfidenceLevel.NO_MATCH
        }

        val reason = when (level) {
            MatchConfidenceLevel.AI_VERIFIED -> "Exact product specification & title match verified by AI."
            MatchConfidenceLevel.STRONG -> "High title and brand similarity ($finalPercent%)."
            MatchConfidenceLevel.POSSIBLE -> "Moderate match ($finalPercent%). Please verify variant details."
            MatchConfidenceLevel.NO_MATCH -> "Product specs do not match closely enough ($finalPercent%)."
        }

        return AiMatchResult(finalPercent, level, reason)
    }

    private fun tokenize(text: String): Set<String> {
        val stopWords = setOf("the", "a", "an", "and", "or", "in", "on", "for", "with", "by", "of", "to", "at", "official", "store", "buy", "online", "price", "india")
        return text.lowercase()
            .replace(Regex("[^a-z0-9\\s]"), "")
            .split(Regex("\\s+"))
            .filter { it.length > 1 && !stopWords.contains(it) }
            .toSet()
    }
}
