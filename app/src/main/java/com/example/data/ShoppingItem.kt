package com.example.data

import java.util.UUID

data class ShoppingItem(
    val id: String = UUID.randomUUID().toString(),
    val url: String,
    val platform: String,
    val timestamp: String,
    val status: String = "Ready to Compare",
    
    // Internally prepared data models for Phase 2B & 7B
    val productName: String? = null,
    val brand: String? = null,
    val category: String? = null,
    val price: Double? = null,
    val offerPrice: Double? = null,
    val seller: String? = null,
    val rating: Double? = null,
    val reviewsCount: Int? = null,
    val similarProducts: List<String>? = null,
    val shoppingScore: Int? = null,
    val lowestPrice: Double? = null,
    val thumbnailUrl: String? = null,
    val date: String? = null,
    val time: String? = null,
    val dealScore: Int? = null,
    val merchant: String? = null,
    val isWishlisted: Boolean = false,
    val aiRecommendation: String? = null
)
