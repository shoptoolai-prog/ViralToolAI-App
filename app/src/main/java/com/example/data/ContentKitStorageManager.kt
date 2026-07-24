package com.example.data

import androidx.compose.runtime.mutableStateListOf
import com.example.engine.CreatorCommerceKit

/**
 * SHOPTOOLAI Phase 10A — Local Content Kit Storage Manager
 * Enables creators to save Shot Lists, Caption Drafts, Voiceover Structures, and Reel Ideas locally.
 */

data class SavedContentKit(
    val id: String,
    val kit: CreatorCommerceKit,
    val dateSaved: String
)

object ContentKitStorageManager {
    private val _savedKits = mutableStateListOf<SavedContentKit>()

    fun getSavedKits(): List<SavedContentKit> = _savedKits.toList()

    fun isKitSaved(url: String): Boolean {
        return _savedKits.any { it.kit.productUrl.equals(url, ignoreCase = true) }
    }

    fun toggleSaveKit(kit: CreatorCommerceKit): Boolean {
        val existingIndex = _savedKits.indexOfFirst { it.kit.productUrl.equals(kit.productUrl, ignoreCase = true) }
        return if (existingIndex != -1) {
            _savedKits.removeAt(existingIndex)
            false // removed
        } else {
            val dateStr = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date())
            val newSaved = SavedContentKit(
                id = java.util.UUID.randomUUID().toString(),
                kit = kit,
                dateSaved = dateStr
            )
            _savedKits.add(0, newSaved)
            true // saved
        }
    }

    fun removeKit(id: String) {
        _savedKits.removeAll { it.id == id }
    }

    fun clearKits() {
        _savedKits.clear()
    }
}
