package com.example.engine

import com.example.data.ShoppingResult

/**
 * SHOPTOOLAI Phase 9C — Smart Buying Guide Engine
 * Generates verified shopping advice cards labeled "AI Recommendation".
 */

data class SmartBuyingGuideData(
    val bestFor: String,
    val checkBeforeBuying: List<String>,
    val whoShouldBuy: String,
    val whoShouldAvoid: String,
    val recommendationLabel: String = "AI Recommendation"
)

object SmartBuyingGuideEngine {

    fun generateGuide(result: ShoppingResult): SmartBuyingGuideData {
        val bestForText = if (result.bestUseCases.isNotEmpty()) {
            result.bestUseCases.joinToString(" • ")
        } else {
            "Daily usage, premium brand reliability & verified store savings"
        }

        val checkList = mutableListOf<String>()
        checkList.add("Verify seller return policy (${result.priceComparison.firstOrNull()?.returnPolicy ?: "Standard Return Policy"})")
        if (result.coupons.isNotEmpty()) {
            checkList.add("Check active bank instant discount offers at checkout")
        } else {
            checkList.add("Confirm active bank discount coupons before final payment")
        }
        checkList.add("Confirm official store warranty inclusion")

        val whoBuyText = result.whoShouldBuy.ifBlank {
            "Shoppers looking for verified genuine quality with warranty protection and express delivery."
        }

        val whoAvoidText = result.whoShouldAvoid.ifBlank {
            "Buyers looking for unauthorized marketplace alternatives or unverified budget knock-offs."
        }

        return SmartBuyingGuideData(
            bestFor = bestForText,
            checkBeforeBuying = checkList,
            whoShouldBuy = whoBuyText,
            whoShouldAvoid = whoAvoidText
        )
    }
}
