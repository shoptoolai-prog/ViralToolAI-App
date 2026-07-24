package com.example.analytics

import android.util.Log

/**
 * SHOPTOOLAI Phase 5A — Internal Analytics Event Layer
 * Lightweight event pipeline for shopping intelligence telemetry.
 */
sealed class ShoppingEvent(val name: String) {
    data class LinkPasted(val url: String) : ShoppingEvent("Link Pasted")
    data class MerchantDetected(val merchantName: String, val isProductPage: Boolean) : ShoppingEvent("Merchant Detected")
    data class ProductIdentified(val title: String, val category: String, val brand: String) : ShoppingEvent("Product Identified")
    data class BuyButtonClicked(val merchantName: String, val targetUrl: String, val price: Double) : ShoppingEvent("Buy Button Clicked")
    data class ComparisonViewed(val productTitle: String, val storeCount: Int) : ShoppingEvent("Comparison Viewed")
    object CreatorAiOpened : ShoppingEvent("Creator AI Opened")
    object InstagramShoppingAiOpened : ShoppingEvent("Instagram Shopping AI Opened")
}

object ShoppingAnalytics {
    private val eventHistory = mutableListOf<ShoppingEvent>()
    private const val TAG = "ShoppingAnalytics"

    @Synchronized
    fun logEvent(event: ShoppingEvent) {
        eventHistory.add(event)
        if (eventHistory.size > 200) {
            eventHistory.removeAt(0)
        }
        Log.d(TAG, "Event Tracked: ${event.name} -> $event")
    }

    @Synchronized
    fun getEventHistory(): List<ShoppingEvent> = eventHistory.toList()
}
