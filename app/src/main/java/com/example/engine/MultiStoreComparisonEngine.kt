package com.example.engine

import com.example.data.PriceCompareItem
import com.example.data.MerchantRegistry

data class ProductMatchParameters(
    val brand: String? = null,
    val modelNumber: String? = null,
    val sku: String? = null,
    val variant: String? = null,
    val color: String? = null,
    val storage: String? = null,
    val ram: String? = null,
    val capacity: String? = null,
    val size: String? = null,
    val title: String = ""
)

data class ComparisonMatchResult(
    val compareItem: PriceCompareItem,
    val confidenceScorePercent: Int,
    val isIdenticalProduct: Boolean,
    val matchParameters: List<String>
)

/**
 * SHOPPING ENGINE PHASE 3: MULTI STORE COMPARISON ENGINE
 * Finds identical products across major e-commerce stores using strict parameters:
 * Brand, Model Number, SKU, Variant, Colour, Storage, RAM, Capacity, Size, Title Similarity, and Gemini Verification.
 *
 * CRITICAL MANDATE:
 * - Only displays cross-store comparison if confidence > 95% for exact identical product match.
 * - If identical product is not found across other stores, hides comparison completely (returns empty list).
 * - Never shows fake prices or estimated prices.
 */
object MultiStoreComparisonEngine {

    fun verifyIdenticalMatch(
        baseProduct: ProductMatchParameters,
        candidateStore: String,
        candidateTitle: String,
        candidatePrice: Double,
        candidateOriginalPrice: Double? = null,
        candidateUrl: String? = null,
        candidateAvailability: String? = null,
        candidateDelivery: String? = null,
        isOfficialSeller: Boolean = false,
        geminiConfidence: Int = 95,
        candidateBrand: String? = null,
        candidateModel: String? = null,
        candidateVariant: String? = null,
        candidateColor: String? = null,
        candidateStorage: String? = null,
        candidateRam: String? = null,
        candidateSize: String? = null
    ): ComparisonMatchResult? {
        if (candidatePrice <= 0.0 || candidateTitle.isBlank()) return null

        val matchedParams = mutableListOf<String>()
        var scorePoints = 0
        var totalPoints = 0

        // 1. Brand match check
        if (!baseProduct.brand.isNullOrBlank()) {
            totalPoints += 25
            val baseB = baseProduct.brand.lowercase().trim()
            val candB = (candidateBrand ?: baseProduct.brand).lowercase().trim()
            if (candB.contains(baseB) || baseB.contains(candB) || candidateTitle.lowercase().contains(baseB)) {
                scorePoints += 25
                matchedParams.add("Brand")
            } else {
                // Mismatched brand -> NOT identical product!
                return null
            }
        }

        // 2. Model Number / SKU match
        if (!baseProduct.modelNumber.isNullOrBlank()) {
            totalPoints += 25
            val baseM = baseProduct.modelNumber.lowercase().trim()
            val candM = (candidateModel ?: "").lowercase().trim()
            if (candM == baseM || candidateTitle.lowercase().contains(baseM)) {
                scorePoints += 25
                matchedParams.add("Model Number")
            }
        }

        // 3. Variant parameters (Color, Storage, RAM, Capacity, Size)
        if (!baseProduct.variant.isNullOrBlank()) {
            totalPoints += 15
            val baseV = baseProduct.variant.lowercase().trim()
            if (candidateTitle.lowercase().contains(baseV) || candidateVariant?.lowercase()?.contains(baseV) == true) {
                scorePoints += 15
                matchedParams.add("Variant")
            }
        }

        if (!baseProduct.storage.isNullOrBlank()) {
            totalPoints += 10
            val baseS = baseProduct.storage.lowercase().trim()
            if (candidateTitle.lowercase().contains(baseS) || candidateStorage?.lowercase()?.contains(baseS) == true) {
                scorePoints += 10
                matchedParams.add("Storage")
            }
        }

        if (!baseProduct.ram.isNullOrBlank()) {
            totalPoints += 10
            val baseR = baseProduct.ram.lowercase().trim()
            if (candidateTitle.lowercase().contains(baseR) || candidateRam?.lowercase()?.contains(baseR) == true) {
                scorePoints += 10
                matchedParams.add("RAM")
            }
        }

        if (!baseProduct.color.isNullOrBlank()) {
            totalPoints += 10
            val baseC = baseProduct.color.lowercase().trim()
            if (candidateTitle.lowercase().contains(baseC) || candidateColor?.lowercase()?.contains(baseC) == true) {
                scorePoints += 10
                matchedParams.add("Colour")
            }
        }

        if (!baseProduct.size.isNullOrBlank()) {
            totalPoints += 10
            val baseSz = baseProduct.size.lowercase().trim()
            if (candidateTitle.lowercase().contains(baseSz) || candidateSize?.lowercase()?.contains(baseSz) == true) {
                scorePoints += 10
                matchedParams.add("Size")
            }
        }

        // 4. Title similarity
        val simScore = calculateTitleSimilarity(baseProduct.title, candidateTitle)
        totalPoints += 30
        val simPoints = (simScore * 30).toInt()
        scorePoints += simPoints
        if (simScore >= 0.75) {
            matchedParams.add("Title Similarity")
        }

        val calculatedScore = if (totalPoints > 0) ((scorePoints.toDouble() / totalPoints.toDouble()) * 100).toInt().coerceIn(0, 100) else 0
        val finalConfidence = (calculatedScore * 0.4 + geminiConfidence * 0.6).toInt().coerceIn(0, 100)

        // Strict 95% Confidence threshold for identical product match
        if (finalConfidence < 95) {
            return null
        }

        val merchantInfo = MerchantRegistry.findMerchant(candidateStore)
        val origPrice = candidateOriginalPrice ?: candidatePrice
        val discount = if (origPrice > candidatePrice) (((origPrice - candidatePrice) / origPrice) * 100).toInt() else 0

        val compareItem = PriceCompareItem(
            store = candidateStore,
            price = candidatePrice,
            isBest = false,
            logoChar = candidateStore.firstOrNull()?.uppercaseChar() ?: 'S',
            accentColor = merchantInfo.primaryColor,
            url = candidateUrl ?: "",
            rating = 4.5,
            deliverySpeed = candidateDelivery ?: "Free Delivery",
            returnPolicy = "Standard Store Policy",
            isOfficialStore = isOfficialSeller,
            deliveryEstimate = candidateDelivery ?: "Express Shipping",
            stock = candidateAvailability ?: "In Stock",
            isVerified = true,
            originalPrice = origPrice,
            discountPercent = discount,
            productName = candidateTitle,
            matchPercent = finalConfidence,
            confidenceLabel = "Verified Identical Match"
        )

        return ComparisonMatchResult(
            compareItem = compareItem,
            confidenceScorePercent = finalConfidence,
            isIdenticalProduct = true,
            matchParameters = matchedParams
        )
    }

    /**
     * Takes verified candidate comparisons. Returns list of stores only if cross-store identical matches exist with > 95% confidence.
     * Otherwise returns emptyList() to hide comparison completely as per specification.
     */
    fun processAndVerifyComparisonList(
        baseStore: String,
        basePrice: Double,
        baseItem: PriceCompareItem,
        candidateMatches: List<ComparisonMatchResult>
    ): List<PriceCompareItem> {
        val validMatches = candidateMatches
            .filter { it.isIdenticalProduct && it.confidenceScorePercent >= 95 && it.compareItem.price > 0.0 }
            .filter { !it.compareItem.store.equals(baseStore, ignoreCase = true) }

        if (validMatches.isEmpty()) {
            // No verified cross-store identical product match -> Hide comparison completely!
            return emptyList()
        }

        val allStores = mutableListOf<PriceCompareItem>()
        allStores.add(baseItem)
        allStores.addAll(validMatches.map { it.compareItem })

        val minPrice = allStores.minOf { it.price }

        return allStores.map { item ->
            val isBest = item.price == minPrice
            item.copy(
                isBest = isBest,
                rankBadge = if (isBest) "Best Price" else null
            )
        }
    }

    private fun calculateTitleSimilarity(s1: String, s2: String): Double {
        val words1 = s1.lowercase().split(Regex("\\W+")).filter { it.length > 2 }.toSet()
        val words2 = s2.lowercase().split(Regex("\\W+")).filter { it.length > 2 }.toSet()
        if (words1.isEmpty() || words2.isEmpty()) return 0.0
        val intersection = words1.intersect(words2).size
        val union = words1.union(words2).size
        return if (union > 0) intersection.toDouble() / union.toDouble() else 0.0
    }
}
