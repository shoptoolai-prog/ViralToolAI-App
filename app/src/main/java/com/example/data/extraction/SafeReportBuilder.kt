package com.example.data.extraction

import com.example.data.MerchantRegistry
import com.example.data.ShoppingResult

/**
 * PHASE 12E — Safe Report Builder
 * Strictly converts a VerifiedProductReport into a ShoppingResult.
 * Hides unverified sections. Never fills gaps with AI guesses or fake values.
 */
object SafeReportBuilder {

    fun buildResultFromVerifiedReport(
        report: VerifiedProductReport,
        inputUrl: String
    ): ShoppingResult {
        val merchantInfo = MerchantRegistry.findMerchant(report.merchantName.value ?: inputUrl)

        val titleText = if (report.title.isVerified) {
            report.title.value!!
        } else {
            "Product title unavailable"
        }

        val priceVal = if (report.currentPrice.isVerified) {
            report.currentPrice.value!!
        } else {
            0.0
        }

        val origPriceVal = if (report.originalPrice.isVerified) {
            report.originalPrice.value!!
        } else {
            priceVal
        }

        val ratingVal = if (report.rating.isVerified) {
            report.rating.value!!
        } else {
            0.0
        }

        val reviewsCountVal = if (report.reviewsCount.isVerified) {
            report.reviewsCount.value!!
        } else {
            0
        }

        val brandText = if (report.brand.isVerified) {
            report.brand.value!!
        } else {
            merchantInfo.merchantName
        }

        val imageWebUrl = if (report.imageUrl.isVerified) {
            report.imageUrl.value
        } else {
            null
        }

        val isReliableResult = report.isVerifiedEnoughToDisplay && report.title.isVerified

        val recommendation = if (isReliableResult) {
            val priceStr = if (priceVal > 0.0) "at ${report.currency.value ?: "INR"} $priceVal" else ""
            "Verified product metadata extracted from ${merchantInfo.merchantName} $priceStr."
        } else {
            report.userErrorMessage
                ?: "We couldn't verify enough product information from this page. Try opening the product page directly or use another supported product link."
        }

        return ShoppingResult(
            url = report.canonicalUrl,
            productName = titleText,
            brand = brandText,
            imageUrl = merchantInfo.merchantName.lowercase(),
            productImageWebUrl = imageWebUrl,
            detectedStore = merchantInfo.merchantName,
            logoChar = merchantInfo.merchantName.firstOrNull()?.uppercaseChar() ?: 'S',
            accentColor = merchantInfo.primaryColor,
            currentPrice = priceVal,
            bestPrice = priceVal,
            availability = if (report.currentPrice.isVerified) "In Stock" else "Price unavailable",
            rating = ratingVal,
            reviewsCount = reviewsCountVal,
            priceTrend = emptyList(), // No fake price trends
            priceComparison = emptyList(), // No fake cross-store price comparisons
            similarProducts = emptyList(), // No fake similar products
            aiRecommendation = recommendation,
            detectionConfidence = report.overallConfidenceScore,
            isCloudVerificationRequired = false,
            isReliable = isReliableResult,
            isPreviewResult = false,
            category = "E-Commerce Product",
            estimatedMatch = if (isReliableResult) "Verified Product" else "Unverified Link",
            status = if (isReliableResult) "Verified Result" else "Product details unavailable"
        )
    }
}
