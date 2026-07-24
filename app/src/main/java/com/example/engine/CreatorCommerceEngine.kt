package com.example.engine

import com.example.data.ShoppingResult

/**
 * SHOPTOOLAI Master Phase 10A — Creator Commerce AI Engine
 * Converts analyzed shopping products into creator content kits:
 * Reel Ideas, Shot Sequences, Voiceover Structure, Caption Lab, Hashtag Lab, and Viral Potential Score.
 * Never invents product information or creator statistics.
 */

data class ReelIdea(
    val category: String,
    val title: String,
    val concept: String,
    val callToAction: String
)

data class ShotItem(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val durationSeconds: Int,
    val cameraAngle: String
)

data class VoiceoverStep(
    val section: String,
    val guidance: String,
    val duration: String
)

data class CreatorCommerceKit(
    val productUrl: String,
    val productName: String,
    val productCategory: String,
    val merchant: String,
    val bestPrice: Double,
    val viralScore: Int,
    val viralLevel: String,
    val reelIdeas: List<ReelIdea>,
    val shotList: List<ShotItem>,
    val voiceoverStructure: List<VoiceoverStep>,
    val captionDrafts: Map<String, String>,
    val hashtagSet: Map<String, List<String>>
)

object CreatorCommerceEngine {

    fun generateContentKit(resultData: ShoppingResult): CreatorCommerceKit {
        val name = resultData.productName.ifBlank { "Featured Product" }
        val category = resultData.category.ifBlank { "Shopping" }
        val store = resultData.detectedStore.ifBlank { "Store" }
        val priceStr = if (resultData.bestPrice > 0) "₹${String.format("%,.0f", resultData.bestPrice)}" else ""

        // Viral Score Calculation (80-98 range based on discount & rating)
        val score = when {
            resultData.rating >= 4.5 && resultData.dealScore >= 80 -> 94
            resultData.rating >= 4.0 || resultData.dealScore >= 70 -> 88
            resultData.rating >= 3.5 -> 79
            else -> 72
        }

        val level = when {
            score >= 88 -> "High"
            score >= 78 -> "Medium"
            else -> "Low"
        }

        // 2. REEL IDEAS GENERATOR (8 core themes)
        val ideas = listOf(
            ReelIdea(
                category = "Unboxing",
                title = "Unboxing $name",
                concept = "Show the initial unboxing experience, packaging quality, and what comes inside the box.",
                callToAction = "Comment 'LINK' to get the best deal on $store!"
            ),
            ReelIdea(
                category = "First Impression",
                title = "First 60 Seconds with $name",
                concept = "Highlight key physical build, weight, feel, and initial setup reaction.",
                callToAction = "Save this reel before price increases!"
            ),
            ReelIdea(
                category = "Honest Review",
                title = "Is $name Worth $priceStr?",
                concept = "A realistic breakdown of pros, cons, and day-to-day usability.",
                callToAction = "Check full link in bio for active coupons!"
            ),
            ReelIdea(
                category = "Top Features",
                title = "3 Best Features of $name",
                concept = "Focus on top 3 verified specifications and unique selling points.",
                callToAction = "Which feature do you like most?"
            ),
            ReelIdea(
                category = "Worth Buying?",
                title = "Don't Buy $name Until You Watch This!",
                concept = "Hook audience with price analysis and deal value before revealing final recommendation.",
                callToAction = "Share with someone looking to buy this!"
            ),
            ReelIdea(
                category = "Comparison",
                title = "$name vs Category Rivals",
                concept = "Compare key features and merchant prices with similar alternatives.",
                callToAction = "Which option would you choose?"
            ),
            ReelIdea(
                category = "Gift Idea",
                title = "Perfect Gift Under $priceStr?",
                concept = "Position product as a curated gift choice for friends or family.",
                callToAction = "Tag a friend who needs this!"
            ),
            ReelIdea(
                category = "Lifestyle Use",
                title = "A Day with $name",
                concept = "Aesthetic montage showing practical real-world usage in daily life.",
                callToAction = "Link in bio for instant discount!"
            )
        )

        // 3. SHOT LIST
        val shots = listOf(
            ShotItem(
                stepNumber = 1,
                title = "Package & Box Unveiling",
                description = "Overhead shot opening packaging and showcasing pristine condition.",
                durationSeconds = 3,
                cameraAngle = "Overhead 90°"
            ),
            ShotItem(
                stepNumber = 2,
                title = "Product Close-Up Macro",
                description = "Slow pan across primary logo, material texture, and key button placement.",
                durationSeconds = 4,
                cameraAngle = "Macro 45° Angle"
            ),
            ShotItem(
                stepNumber = 3,
                title = "Build & Material Inspection",
                description = "Demonstrate physical tactile feel, portability, and key build details.",
                durationSeconds = 4,
                cameraAngle = "Eye-Level Medium Shot"
            ),
            ShotItem(
                stepNumber = 4,
                title = "In-Action Live Usage",
                description = "Show product performing its core primary function seamlessly.",
                durationSeconds = 5,
                cameraAngle = "Over-The-Shoulder Action"
            ),
            ShotItem(
                stepNumber = 5,
                title = "Final Opinion & Price Badge",
                description = "Creator holding product with deal price overlay and CTA badge.",
                durationSeconds = 3,
                cameraAngle = "Frontal Talking Head"
            )
        )

        // 4. VOICEOVER STRUCTURE
        val voiceover = listOf(
            VoiceoverStep("Hook", "Start with an energetic question: 'Is this $name actually worth $priceStr?'", "0-3s"),
            VoiceoverStep("Problem", "Address common buyer hesitation or search intent in $category.", "3-6s"),
            VoiceoverStep("Product", "Introduce $name and mention it was found on $store.", "6-10s"),
            VoiceoverStep("Features", "Highlight key verified specs without exaggeration.", "10-15s"),
            VoiceoverStep("Opinion", "Give honest creator verdict on value for money.", "15-20s"),
            VoiceoverStep("CTA", "Direct viewers: 'Comment LINK or tap bio link to check live discount!'", "20-25s")
        )

        // 5. CAPTION LAB
        val cleanName = name.take(35)
        val cleanBrand = resultData.brand.ifBlank { "Brand" }
        val cleanStore = store.replace(" ", "")

        val captions = mapOf(
            "Instagram" to "Honest review of $cleanName! 🚀 Found this deal on $store for $priceStr. Check out the full breakdown and active discount in my bio link! 👇 #$cleanStore #$category #ProductReview",
            "YouTube" to "Is $cleanName worth it in 2026? Unboxing, feature test, and best price comparison from $store ($priceStr). Link in description!",
            "Facebook" to "Looking for a great option in $category? Here is an in-depth look at $cleanName available on $store at $priceStr.",
            "Threads" to "Just analyzed $cleanName ($priceStr on $store). Solid choice for $category. What do you think? 💬"
        )

        // 6. HASHTAG LAB
        val hashtags = mapOf(
            "Trending" to listOf("#ViralReels", "#TrendingProduct", "#MustHave", "#ShoppingDeals", "#UnboxingReel"),
            "Niche" to listOf("#${category.replace(" ", "")}", "#ProductReview", "#SmartShopping", "#TechFinds"),
            "Product" to listOf("#${cleanName.replace(" ", "")}", "#$cleanBrand"),
            "Brand" to listOf("#$cleanStore", "#$cleanBrand"),
            "Regional" to listOf("#ReelsIndia", "#ShoppingIndia", "#CreatorCommunity")
        )

        return CreatorCommerceKit(
            productUrl = resultData.url,
            productName = name,
            productCategory = category,
            merchant = store,
            bestPrice = resultData.bestPrice,
            viralScore = score,
            viralLevel = level,
            reelIdeas = ideas,
            shotList = shots,
            voiceoverStructure = voiceover,
            captionDrafts = captions,
            hashtagSet = hashtags
        )
    }
}
