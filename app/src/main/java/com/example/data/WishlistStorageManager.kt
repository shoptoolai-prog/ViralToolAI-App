package com.example.data

import androidx.compose.runtime.mutableStateListOf

data class WishlistItem(
    val id: String,
    val productName: String,
    val merchant: String,
    val price: Double,
    val url: String,
    val thumbnailUrl: String?,
    val dateSaved: String,
    val dealScore: Int,
    val category: String? = null
)

object WishlistStorageManager {
    private val _wishlistItems = mutableStateListOf<WishlistItem>()

    fun getWishlistItems(): List<WishlistItem> = _wishlistItems.toList()

    fun isWishlisted(url: String): Boolean {
        return _wishlistItems.any { it.url.equals(url, ignoreCase = true) }
    }

    fun toggleWishlist(
        productName: String,
        merchant: String,
        price: Double,
        url: String,
        thumbnailUrl: String?,
        dealScore: Int,
        category: String? = null
    ): Boolean {
        val existingIndex = _wishlistItems.indexOfFirst { it.url.equals(url, ignoreCase = true) }
        return if (existingIndex != -1) {
            _wishlistItems.removeAt(existingIndex)
            false // removed
        } else {
            val dateStr = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date())
            val newItem = WishlistItem(
                id = java.util.UUID.randomUUID().toString(),
                productName = productName,
                merchant = merchant,
                price = price,
                url = url,
                thumbnailUrl = thumbnailUrl,
                dateSaved = dateStr,
                dealScore = dealScore,
                category = category
            )
            _wishlistItems.add(0, newItem)
            true // added
        }
    }

    fun removeItem(id: String) {
        _wishlistItems.removeAll { it.id == id }
    }

    fun clearWishlist() {
        _wishlistItems.clear()
    }
}
