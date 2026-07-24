package com.example.engine

import com.example.data.ShoppingResult

/**
 * SHOPTOOLAI Phase 9C — Product Quality Engine
 * Calculates AI Quality Estimate based strictly on verified rating, trust score, and merchant reliability.
 * Levels: Excellent, Good, Average, Unknown.
 */

enum class QualityLevel(val displayName: String) {
    EXCELLENT("Excellent"),
    GOOD("Good"),
    AVERAGE("Average"),
    UNKNOWN("Unknown")
}

data class ProductQualityEstimate(
    val scorePercent: Int, // 0 - 100
    val level: QualityLevel,
    val summary: String,
    val verifiedFactorCount: Int,
    val ratingValue: Double,
    val trustScore: Int
)

object ProductQualityEngine {

    fun estimateQuality(result: ShoppingResult): ProductQualityEstimate {
        val rating = result.rating
        val trustScore = result.trustScorePercent
        
        // Compute combined quality score based on verified data
        val qualityScore = if (rating > 0 && trustScore > 0) {
            val ratingPercent = ((rating / 5.0) * 100).toInt()
            ((ratingPercent * 0.5) + (trustScore * 0.5)).toInt().coerceIn(0, 100)
        } else if (trustScore > 0) {
            trustScore.coerceIn(0, 100)
        } else if (rating > 0) {
            ((rating / 5.0) * 100).toInt().coerceIn(0, 100)
        } else {
            0
        }

        val level = when {
            qualityScore >= 85 -> QualityLevel.EXCELLENT
            qualityScore >= 70 -> QualityLevel.GOOD
            qualityScore >= 50 -> QualityLevel.AVERAGE
            else -> QualityLevel.UNKNOWN
        }

        val summaryText = when (level) {
            QualityLevel.EXCELLENT -> "Top Grade — Verified high seller ratings and brand authenticity."
            QualityLevel.GOOD -> "Good Grade — Consistent positive buyer reviews and verified seller reputation."
            QualityLevel.AVERAGE -> "Average Grade — Standard commercial rating, check return policies."
            QualityLevel.UNKNOWN -> "Unknown Quality — Insufficient verified seller ratings available."
        }

        var factorCount = 0
        if (result.rating > 0) factorCount++
        if (result.reviewsCount > 0) factorCount++
        if (result.trustScorePercent > 0) factorCount++
        if (result.priceComparison.any { it.isVerified }) factorCount++

        return ProductQualityEstimate(
            scorePercent = qualityScore,
            level = level,
            summary = summaryText,
            verifiedFactorCount = factorCount,
            ratingValue = result.rating,
            trustScore = trustScore
        )
    }
}
