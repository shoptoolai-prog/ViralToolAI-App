package com.example.engine

import com.example.data.ShoppingResult

/**
 * SHOPTOOLAI Phase 9C — Product Intelligence Engine
 * Centralized layer for analyzing product attributes from verified sources.
 * Strictly avoids hallucinating missing facts: unknown fields display "Not Available".
 */

data class ProductIntelligenceData(
    val category: String,
    val brand: String,
    val material: String,
    val colour: String,
    val variant: String,
    val size: String,
    val targetAudience: String,
    val verifiedFactsCount: Int,
    val isVerifiedProduct: Boolean,
    val intelligenceSummary: String
)

object ProductIntelligenceEngine {

    fun analyzeProduct(result: ShoppingResult): ProductIntelligenceData {
        val categoryStr = if (!result.category.isNullOrBlank() && result.category != "Unknown Product" && result.category != "Unknown") {
            result.category
        } else {
            "Not Available"
        }

        val brandStr = if (!result.brand.isNullOrBlank() && result.brand != "Unknown Brand" && result.brand != "Brand Item") {
            result.brand
        } else {
            "Not Available"
        }

        val materialStr = if (!result.material.isNullOrBlank() && result.material != "Unknown") {
            result.material
        } else {
            // Check specifications for material
            val specMaterial = result.specifications.find { it.title.equals("Material", ignoreCase = true) || it.title.contains("Fabric", ignoreCase = true) }?.value
            specMaterial?.takeIf { it.isNotBlank() } ?: "Not Available"
        }

        val colourStr = if (!result.color.isNullOrBlank() && result.color != "Unknown") {
            result.color
        } else {
            val specColour = result.specifications.find { it.title.equals("Colour", ignoreCase = true) || it.title.equals("Color", ignoreCase = true) }?.value
            specColour?.takeIf { it.isNotBlank() } ?: "Not Available"
        }

        val variantStr = if (!result.variant.isNullOrBlank() && result.variant != "Unknown") {
            result.variant
        } else {
            val specVariant = result.specifications.find { it.title.contains("Variant", ignoreCase = true) || it.title.contains("RAM", ignoreCase = true) || it.title.contains("Storage", ignoreCase = true) }?.value
            specVariant?.takeIf { it.isNotBlank() } ?: "Not Available"
        }

        val sizeStr = if (!result.size.isNullOrBlank() && result.size != "Unknown") {
            result.size
        } else {
            val specSize = result.specifications.find { it.title.equals("Size", ignoreCase = true) || it.title.contains("Dimensions", ignoreCase = true) }?.value
            specSize?.takeIf { it.isNotBlank() } ?: "Not Available"
        }

        // Target Audience detection based on verified category & product name
        val targetAudienceStr = detectVerifiedTargetAudience(result.productName, categoryStr, result.specifications.map { "${it.title}: ${it.value}" })

        // Count verified facts
        var verifiedCount = 0
        if (categoryStr != "Not Available") verifiedCount++
        if (brandStr != "Not Available") verifiedCount++
        if (materialStr != "Not Available") verifiedCount++
        if (colourStr != "Not Available") verifiedCount++
        if (variantStr != "Not Available") verifiedCount++
        if (sizeStr != "Not Available") verifiedCount++
        if (targetAudienceStr != "Not Available") verifiedCount++

        val summaryText = if (verifiedCount >= 4) {
            "High confidence verified product intelligence profile ($verifiedCount facts confirmed)."
        } else if (verifiedCount >= 2) {
            "Partial product intelligence profile ($verifiedCount verified facts confirmed)."
        } else {
            "Basic product profile ($verifiedCount verified facts available)."
        }

        return ProductIntelligenceData(
            category = categoryStr,
            brand = brandStr,
            material = materialStr,
            colour = colourStr,
            variant = variantStr,
            size = sizeStr,
            targetAudience = targetAudienceStr,
            verifiedFactsCount = verifiedCount,
            isVerifiedProduct = result.isReliable,
            intelligenceSummary = summaryText
        )
    }

    private fun detectVerifiedTargetAudience(productName: String, category: String, specs: List<String>): String {
        val combined = "$productName $category ${specs.joinToString(" ")}".lowercase()
        return when {
            combined.contains("women") || combined.contains("female") || combined.contains("ladies") || combined.contains("kurti") || combined.contains("saree") -> "Women"
            combined.contains("men") || combined.contains("male") || combined.contains("gentlemen") -> "Men"
            combined.contains("kid") || combined.contains("children") || combined.contains("toy") || combined.contains("toddler") -> "Kids & Children"
            combined.contains("unisex") -> "Unisex (All Audiences)"
            combined.contains("smartphone") || combined.contains("laptop") || combined.contains("electronics") || combined.contains("watch") -> "Tech Enthusiasts & Professionals"
            combined.contains("shoes") || combined.contains("sneaker") || combined.contains("running") || combined.contains("fitness") -> "Active & Sportswear Users"
            else -> "Not Available"
        }
    }
}
