package com.example.engine

import com.example.data.PriceTrendPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PriceAlert(
    val url: String,
    val productName: String,
    val initialPrice: Double,
    val targetPrice: Double,
    val isTriggered: Boolean = false,
    val createdAtFormatted: String = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
)

data class PriceHistoryStats(
    val currentPrice: Double,
    val lowestPrice: Double,
    val highestPrice: Double,
    val averagePrice: Double,
    val previousPrice: Double?,
    val trendDirection: String, // "Trending Down", "Trending Up", "Stable"
    val changePercent: Double
)

/**
 * SHOPPING ENGINE PHASE 4: PRICE HISTORY ENGINE
 * Handles real price history tracking, 30D/90D/180D filtering, and Price Drop Alerts.
 *
 * CRITICAL MANDATE:
 * - Never generates fake history.
 * - If history is unavailable, returns empty list so UI hides the graph completely.
 * - Prevents NaN, division by zero, and invalid placeholders.
 */
object PriceTrackerEngine {

    private val priceHistoryMap = mutableMapOf<String, MutableList<PriceTrendPoint>>()
    private val activeAlertsMap = mutableMapOf<String, PriceAlert>()

    fun recordPriceSnapshot(url: String, currentPrice: Double) {
        if (currentPrice <= 0.0 || url.isBlank()) return

        val normalizedUrl = url.trim()
        val history = priceHistoryMap.getOrPut(normalizedUrl) { mutableListOf() }
        val todayDate = SimpleDateFormat("MMM d", Locale.getDefault()).format(Date())

        val lastPoint = history.lastOrNull()
        if (lastPoint == null) {
            history.add(PriceTrendPoint(date = todayDate, price = currentPrice, daysAgo = 0))
        } else if (lastPoint.price != currentPrice) {
            history.add(PriceTrendPoint(date = todayDate, price = currentPrice, daysAgo = 0))
        }
    }

    fun getFilteredPriceHistory(rawPoints: List<PriceTrendPoint>, timeframeDays: Int): List<PriceTrendPoint> {
        if (rawPoints.isEmpty()) return emptyList()

        return rawPoints.filter { it.daysAgo <= timeframeDays }
    }

    fun calculateStats(points: List<PriceTrendPoint>, fallbackCurrentPrice: Double): PriceHistoryStats {
        if (points.isEmpty()) {
            return PriceHistoryStats(
                currentPrice = fallbackCurrentPrice,
                lowestPrice = fallbackCurrentPrice,
                highestPrice = fallbackCurrentPrice,
                averagePrice = fallbackCurrentPrice,
                previousPrice = null,
                trendDirection = "Stable",
                changePercent = 0.0
            )
        }

        val prices = points.map { it.price }
        val currentPrice = prices.lastOrNull() ?: fallbackCurrentPrice
        val lowestPrice = prices.minOrNull() ?: currentPrice
        val highestPrice = prices.maxOrNull() ?: currentPrice
        val avgPrice = if (prices.isNotEmpty()) prices.average() else currentPrice
        val previousPrice = if (prices.size >= 2) prices[prices.size - 2] else null

        val firstPrice = prices.firstOrNull() ?: currentPrice
        val trendDirection = when {
            currentPrice < firstPrice -> "Trending Down"
            currentPrice > firstPrice -> "Trending Up"
            else -> "Stable"
        }

        val changePercent = if (firstPrice > 0.0) {
            (((currentPrice - firstPrice) / firstPrice) * 100)
        } else {
            0.0
        }

        return PriceHistoryStats(
            currentPrice = currentPrice,
            lowestPrice = lowestPrice,
            highestPrice = highestPrice,
            averagePrice = avgPrice,
            previousPrice = previousPrice,
            trendDirection = trendDirection,
            changePercent = changePercent
        )
    }

    fun setPriceAlert(url: String, productName: String, currentPrice: Double, targetPrice: Double): PriceAlert {
        val alert = PriceAlert(
            url = url,
            productName = productName,
            initialPrice = currentPrice,
            targetPrice = targetPrice,
            isTriggered = currentPrice <= targetPrice
        )
        activeAlertsMap[url] = alert
        return alert
    }

    fun removePriceAlert(url: String) {
        activeAlertsMap.remove(url)
    }

    fun getAlert(url: String): PriceAlert? {
        return activeAlertsMap[url]
    }

    fun checkPriceDropTrigger(url: String, currentPrice: Double): Boolean {
        val alert = activeAlertsMap[url] ?: return false
        return currentPrice <= alert.targetPrice
    }
}
