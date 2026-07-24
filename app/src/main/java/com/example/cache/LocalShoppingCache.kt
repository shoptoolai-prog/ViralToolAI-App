package com.example.cache

import com.example.data.MerchantInfo
import com.example.data.ProductIdentity
import com.example.data.ShoppingResult

/**
 * SHOPTOOLAI Phase 5A — Local Cache Architecture
 * Thread-safe memory cache for fast startup, zero lag, and instant offline/offline fallback lookups.
 */
data class UserPreferences(
    val defaultCountry: String = "India",
    val preferredCurrency: String = "INR (₹)",
    val enableAutoPriceComparison: Boolean = true,
    val notifyOnPriceDrop: Boolean = true,
    val preferredMerchants: List<String> = listOf("Amazon", "Flipkart", "Myntra")
)

object LocalShoppingCache {
    private val recentSearches = mutableListOf<String>()
    private val recentProducts = mutableListOf<ShoppingResult>()
    private val recentlyViewedIdentities = mutableListOf<ProductIdentity>()
    private val merchantLogoCache = mutableMapOf<String, MerchantInfo>()
    private var cachedUserPreferences = UserPreferences()

    // Maximum cache capacities
    private const val MAX_RECENT_SEARCHES = 20
    private const val MAX_RECENT_PRODUCTS = 30
    private const val MAX_RECENT_IDENTITIES = 50
    private const val MAX_MERCHANT_CACHE = 100

    @Synchronized
    fun addRecentSearch(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return
        recentSearches.remove(trimmed)
        recentSearches.add(0, trimmed)
        if (recentSearches.size > MAX_RECENT_SEARCHES) {
            recentSearches.removeAt(recentSearches.size - 1)
        }
    }

    @Synchronized
    fun getRecentSearches(): List<String> = recentSearches.toList()

    @Synchronized
    fun cacheProductResult(result: ShoppingResult) {
        recentProducts.removeAll { it.url.equals(result.url, ignoreCase = true) }
        recentProducts.add(0, result)
        if (recentProducts.size > MAX_RECENT_PRODUCTS) {
            recentProducts.removeAt(recentProducts.size - 1)
        }
    }

    @Synchronized
    fun getCachedProductResult(url: String): ShoppingResult? {
        return recentProducts.firstOrNull { it.url.equals(url.trim(), ignoreCase = true) }
    }

    @Synchronized
    fun getRecentProducts(): List<ShoppingResult> = recentProducts.toList()

    @Synchronized
    fun addRecentlyViewedIdentity(identity: ProductIdentity) {
        recentlyViewedIdentities.removeAll { it.url.equals(identity.url, ignoreCase = true) }
        recentlyViewedIdentities.add(0, identity)
        if (recentlyViewedIdentities.size > MAX_RECENT_IDENTITIES) {
            recentlyViewedIdentities.removeAt(recentlyViewedIdentities.size - 1)
        }
    }

    @Synchronized
    fun getRecentlyViewedIdentities(): List<ProductIdentity> = recentlyViewedIdentities.toList()

    @Synchronized
    fun cacheMerchant(merchantInfo: MerchantInfo) {
        if (merchantLogoCache.size >= MAX_MERCHANT_CACHE && !merchantLogoCache.containsKey(merchantInfo.merchantName.lowercase())) {
            merchantLogoCache.keys.firstOrNull()?.let { merchantLogoCache.remove(it) }
        }
        merchantLogoCache[merchantInfo.merchantName.lowercase()] = merchantInfo
    }

    @Synchronized
    fun getCachedMerchant(merchantName: String): MerchantInfo? {
        return merchantLogoCache[merchantName.lowercase()]
    }

    @Synchronized
    fun updateUserPreferences(prefs: UserPreferences) {
        cachedUserPreferences = prefs
    }

    @Synchronized
    fun getUserPreferences(): UserPreferences = cachedUserPreferences

    @Synchronized
    fun clearCache() {
        recentSearches.clear()
        recentProducts.clear()
        recentlyViewedIdentities.clear()
        merchantLogoCache.clear()
    }
}
