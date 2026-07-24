package com.example.engine

import com.example.analytics.ShoppingAnalytics
import com.example.analytics.ShoppingEvent
import com.example.cache.LocalShoppingCache
import com.example.data.MerchantDetector
import com.example.data.MerchantInfo
import com.example.data.ProductIdentity
import com.example.data.ShoppingResult
import com.example.data.generateResultData
import com.example.providers.ProviderManager

/**
 * SHOPTOOLAI Phase 5A — Smart Result States
 */
sealed class SmartResultState {
    object Loading : SmartResultState()
    data class Searching(val step: String, val progressInt: Int) : SmartResultState()
    data class Comparing(val merchantCount: Int) : SmartResultState()
    data class Verified(val result: ShoppingResult) : SmartResultState()
    data class Unavailable(val reason: String) : SmartResultState()
    object WaitingForLiveData : SmartResultState()
}

/**
 * SHOPTOOLAI Phase 5A — Universal Shopping Engine Pipeline
 *
 * Flow:
 * 1. Receive Link
 * 2. Detect Merchant
 * 3. Normalize URL
 * 4. Identify Product
 * 5. Identify Category
 * 6. Create Product Identity
 * 7. Request Product Provider
 * 8. Generate AI Shopping Report
 */
object UniversalShoppingEngine {

    /**
     * Executes full end-to-end shopping intelligence analysis pipeline.
     */
    fun processUrlPipeline(rawUrl: String): SmartResultState {
        // 1. Receive Link
        val trimmedInput = rawUrl.trim()
        if (trimmedInput.isBlank()) {
            return SmartResultState.Unavailable("Empty link provided.")
        }
        ShoppingAnalytics.logEvent(ShoppingEvent.LinkPasted(trimmedInput))

        // Check local cache for immediate response
        val cached = LocalShoppingCache.getCachedProductResult(trimmedInput)
        if (cached != null) {
            ShoppingAnalytics.logEvent(ShoppingEvent.MerchantDetected(cached.detectedStore, true))
            ShoppingAnalytics.logEvent(ShoppingEvent.ProductIdentified(cached.productName, cached.category, cached.brand))
            return SmartResultState.Verified(cached)
        }

        // 2. Detect Merchant & 3. Normalize URL
        val urlAnalysis = MerchantDetector.analyzeUrl(trimmedInput)
        val normalizedUrl = urlAnalysis.normalizedUrl
        val merchantInfo = urlAnalysis.merchantInfo

        LocalShoppingCache.cacheMerchant(merchantInfo)
        ShoppingAnalytics.logEvent(ShoppingEvent.MerchantDetected(merchantInfo.merchantName, urlAnalysis.isProductPage))

        // 4. Identify Product & 5. Identify Category & 6. Create Product Identity
        val generatedResult = generateResultData(normalizedUrl)

        if (!generatedResult.isReliable || generatedResult.status == "Product details unavailable") {
            return SmartResultState.Unavailable(generatedResult.aiRecommendation)
        }

        // Extract Product Identity
        val productIdentity = ProductIdentity(
            productName = generatedResult.productName,
            brand = generatedResult.brand,
            category = generatedResult.category,
            merchant = generatedResult.detectedStore,
            image = generatedResult.productImageWebUrl ?: generatedResult.imageUrl,
            url = normalizedUrl,
            sku = generatedResult.modelNumber,
            modelNumber = generatedResult.modelNumber,
            variant = generatedResult.variant,
            color = generatedResult.color,
            size = generatedResult.size,
            storage = generatedResult.capacity
        )

        LocalShoppingCache.addRecentlyViewedIdentity(productIdentity)
        ShoppingAnalytics.logEvent(
            ShoppingEvent.ProductIdentified(
                title = productIdentity.productName,
                category = productIdentity.category,
                brand = productIdentity.brand
            )
        )

        // 7. Request Product Provider & Apply Phase 5B Strict 95% Similarity Validation
        val crossProviderItems = ProviderManager.fetchComparisonList(productIdentity)

        val verifiedComparisonList = crossProviderItems.mapNotNull { compareItem ->
            val targetIdentity = compareItem.productIdentity ?: productIdentity
            val matchResult = AiMatchEngine.calculateMatchScore(productIdentity, targetIdentity)
            // Phase 5B Rule 5: Discard if similarity < 95%
            if (matchResult.scorePercent >= 95) {
                // Phase 5B Rule 1 & 4: Lock Identity & Same Product Rule (title, image, brand, category)
                compareItem.copy(
                    productName = productIdentity.productName,
                    productImage = productIdentity.image,
                    matchPercent = matchResult.scorePercent,
                    confidenceLabel = matchResult.level.label,
                    productIdentity = productIdentity
                )
            } else {
                null
            }
        }

        // 8. Generate AI Shopping Report (No fake comparison fallback - Phase 5B Rule 3 & 6)
        val finalShoppingReport = generatedResult.copy(
            priceComparison = verifiedComparisonList,
            url = normalizedUrl
        )

        // Cache result
        LocalShoppingCache.cacheProductResult(finalShoppingReport)
        LocalShoppingCache.addRecentSearch(normalizedUrl)
        ShoppingAnalytics.logEvent(
            ShoppingEvent.ComparisonViewed(
                productTitle = finalShoppingReport.productName,
                storeCount = verifiedComparisonList.size
            )
        )

        return SmartResultState.Verified(finalShoppingReport)
    }
}
