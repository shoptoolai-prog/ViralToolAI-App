package com.example.data

import androidx.compose.runtime.mutableStateListOf

/**
 * SHOPTOOLAI Phase 10D — Local Trend History Storage Manager
 * Stores local history of trend reports including Trend Score, Creator Score, Content Category, Date, and Product details for Quick Reopen.
 */

data class TrendHistoryEntry(
    val id: String,
    val productUrl: String,
    val productName: String,
    val store: String,
    val contentCategory: String,
    val trendScore: Int,
    val creatorScore: Int,
    val dateSaved: String
)

object TrendHistoryStorageManager {
    private val _history = mutableStateListOf<TrendHistoryEntry>()

    fun getHistory(): List<TrendHistoryEntry> = _history.toList()

    fun recordTrendReport(
        url: String,
        name: String,
        store: String,
        category: String,
        trendScore: Int,
        creatorScore: Int
    ) {
        val existingIndex = _history.indexOfFirst { it.productUrl.equals(url, ignoreCase = true) }
        val dateStr = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date())
        val entry = TrendHistoryEntry(
            id = if (existingIndex != -1) _history[existingIndex].id else java.util.UUID.randomUUID().toString(),
            productUrl = url,
            productName = name,
            store = store,
            contentCategory = category,
            trendScore = trendScore,
            creatorScore = creatorScore,
            dateSaved = dateStr
        )

        if (existingIndex != -1) {
            _history.removeAt(existingIndex)
        }
        _history.add(0, entry)
    }

    fun removeEntry(id: String) {
        _history.removeAll { it.id == id }
    }

    fun clearHistory() {
        _history.clear()
    }
}
