package com.example.creatoracademy

import android.content.Context
import kotlin.math.cos
import kotlin.math.sin

data class PrimaryAudienceData(
    val primaryGroup: String,
    val ageRange: String,
    val genderDistribution: String, // e.g., "82% Female, 18% Male"
    val language: String,
    val region: String,
    val cityTier: String,
    val shoppingBehaviour: String,
    val experienceLevel: String
)

data class AudienceCardData(
    val title: String,
    val emoji: String,
    val likelihoodPercent: Int,
    val interests: List<String>,
    val buyingPower: String,
    val activityLevel: String,
    val reason: String = ""
)

data class RadarCategoryData(
    val category: String,
    val value: Float // 0.0f to 1.0f
)

enum class PlatformRating {
    EXCELLENT, GOOD, AVERAGE, WEAK
}

data class PlatformMatchData(
    val platformName: String,
    val rating: PlatformRating,
    val ratingText: String, // "Excellent", "Good", "Average", "Weak"
    val reason: String,
    val logoEmoji: String
)

data class BuyerIntentReason(
    val factor: String, // "Product Visibility", "Trust", "Voice Clarity", "Price Callout", "Limited Offer"
    val score: Int, // 0 to 100
    val statusText: String
)

data class BuyerIntentData(
    val overallLevel: String, // "Very High", "High", "Medium", "Low", "Very Low"
    val score: Int,
    val reasons: List<BuyerIntentReason>
)

data class WatchBehaviourData(
    val avgWatchTimeSec: Float,
    val totalLengthSec: Float,
    val watchTimePercent: Int,
    val replayChancePercent: Int,
    val shareChancePercent: Int,
    val saveChancePercent: Int,
    val commentChancePercent: Int,
    val profileVisitChancePercent: Int,
    val followChancePercent: Int
)

data class IdealPostTimeData(
    val bestDay: String,
    val bestTimeWindow: String,
    val worstTimeWindow: String,
    val competitionLevel: String,
    val expectedActiveAudience: String,
    val peakHourLabel: String
)

data class ContentMatchData(
    val category: String,
    val matchPercent: Int
)

data class AudienceEmotionData(
    val emotion: String,
    val emoji: String,
    val scorePercent: Int,
    val description: String
)

data class AiAudiencePersonaReport(
    val reelTitle: String,
    val confidencePercent: Int,
    val isLowConfidence: Boolean,
    val primaryAudience: PrimaryAudienceData,
    val audienceCards: List<AudienceCardData>,
    val interestRadar: List<RadarCategoryData>,
    val platformMatches: List<PlatformMatchData>,
    val buyerIntent: BuyerIntentData,
    val watchBehaviour: WatchBehaviourData,
    val idealPostTime: IdealPostTimeData,
    val contentMatches: List<ContentMatchData>,
    val audienceEmotions: List<AudienceEmotionData>,
    val top3Improvements: List<String>,
    val viriRecommendation: String
)

object AiAudiencePersonaEngine {

    fun generatePersonaReport(reel: AnalysedReel): AiAudiencePersonaReport {
        val titleLower = reel.title.lowercase()
        val summaryLower = reel.aiSummary.lowercase()

        // Detect topic
        val isBeautyOrSkincare = titleLower.contains("beauty") || titleLower.contains("skin") ||
                titleLower.contains("makeup") || summaryLower.contains("beauty") || summaryLower.contains("glow")
        val isKitchenOrFood = titleLower.contains("kitchen") || titleLower.contains("cook") ||
                titleLower.contains("food") || titleLower.contains("recipe")
        val isTechOrElectronics = titleLower.contains("tech") || titleLower.contains("gadget") ||
                titleLower.contains("phone") || titleLower.contains("unboxing")
        val isFashion = titleLower.contains("fashion") || titleLower.contains("outfit") ||
                titleLower.contains("haul") || titleLower.contains("dress")

        val score = reel.finalAiScore
        val confidence = if (score > 80) 92 else if (score > 60) 84 else 68

        // Primary Audience logic
        val primary = when {
            isBeautyOrSkincare -> PrimaryAudienceData(
                primaryGroup = "Women (Gen-Z & Young Millennials)",
                ageRange = "18–28",
                genderDistribution = "84% Female • 16% Male",
                language = "Hindi + Hinglish",
                region = "North & West India",
                cityTier = "Tier-2 & Tier-3 Cities",
                shoppingBehaviour = "Budget Beauty Buyers & Trend Explorers",
                experienceLevel = "Skincare & Cosmetics Enthusiasts"
            )
            isKitchenOrFood -> PrimaryAudienceData(
                primaryGroup = "Homemakers & Working Adults",
                ageRange = "24–42",
                genderDistribution = "78% Female • 22% Male",
                language = "Hindi + Regional",
                region = "Pan India",
                cityTier = "Tier-1, Tier-2 & Tier-3",
                shoppingBehaviour = "Practical Home Buyers & Deal Hunters",
                experienceLevel = "Home Culinary & Living Seekers"
            )
            isTechOrElectronics -> PrimaryAudienceData(
                primaryGroup = "Tech Enthusiasts & Young Professionals",
                ageRange = "18–35",
                genderDistribution = "28% Female • 72% Male",
                language = "Hinglish + English",
                region = "Metro & Tier-1 Cities",
                cityTier = "Tier-1 & Urban Tier-2",
                shoppingBehaviour = "Feature Comparison & Value-for-Money Buyers",
                experienceLevel = "Gadget & Lifestyle Tech Users"
            )
            else -> PrimaryAudienceData(
                primaryGroup = "Fashion & Lifestyle Enthusiasts",
                ageRange = "18–30",
                genderDistribution = "76% Female • 24% Male",
                language = "Hindi + Hinglish",
                region = "Pan-India Urban",
                cityTier = "Tier-2 Cities & Metros",
                shoppingBehaviour = "Impulse Shoppers & Viral Trend Followers",
                experienceLevel = "Daily Social Media Viewers"
            )
        }

        // Audience Cards
        val cards = listOf(
            AudienceCardData(
                title = "College Girl",
                emoji = "👩‍🎓",
                likelihoodPercent = (88 + (score % 8)).coerceAtMost(98),
                interests = listOf("Beauty", "Fashion", "Affordable Glam"),
                buyingPower = "Medium",
                activityLevel = "Very High"
            ),
            AudienceCardData(
                title = "Working Woman",
                emoji = "👩‍💼",
                likelihoodPercent = (80 + (score % 6)).coerceAtMost(92),
                interests = listOf("Skincare", "Quick Prep", "Quality Brands"),
                buyingPower = "High",
                activityLevel = "High"
            ),
            AudienceCardData(
                title = "Homemaker / Creator",
                emoji = "👩‍🍳",
                likelihoodPercent = (72 + (score % 7)).coerceAtMost(88),
                interests = listOf("Budget Deals", "Daily Essentials", "Family Use"),
                buyingPower = "Medium",
                activityLevel = "Medium"
            ),
            AudienceCardData(
                title = "Male Audience",
                emoji = "👨",
                likelihoodPercent = if (isTechOrElectronics) 78 else 22,
                interests = if (isTechOrElectronics) listOf("Tech", "Unboxing") else listOf("Gifting", "Casual Browsing"),
                buyingPower = "Medium",
                activityLevel = "Low to Moderate",
                reason = if (isTechOrElectronics) "High tech appeal" else "Low product relevance for male demographics"
            )
        )

        // Interest Radar Data (Categories: Beauty, Skincare, Fashion, Electronics, Kitchen, Fitness, Lifestyle, Education, Travel, Food)
        val radar = listOf(
            RadarCategoryData("Beauty", if (isBeautyOrSkincare) 0.95f else 0.45f),
            RadarCategoryData("Skincare", if (isBeautyOrSkincare) 0.92f else 0.38f),
            RadarCategoryData("Fashion", if (isFashion || isBeautyOrSkincare) 0.88f else 0.50f),
            RadarCategoryData("Electronics", if (isTechOrElectronics) 0.94f else 0.20f),
            RadarCategoryData("Kitchen", if (isKitchenOrFood) 0.90f else 0.18f),
            RadarCategoryData("Fitness", 0.42f),
            RadarCategoryData("Lifestyle", 0.78f),
            RadarCategoryData("Education", 0.35f),
            RadarCategoryData("Travel", 0.40f),
            RadarCategoryData("Food", if (isKitchenOrFood) 0.85f else 0.30f)
        )

        // Platform Matches
        val platforms = listOf(
            PlatformMatchData(
                platformName = "Instagram",
                rating = PlatformRating.EXCELLENT,
                ratingText = "Excellent",
                reason = "High aesthetic visual reel appeal & strong Gen-Z retention.",
                logoEmoji = "📸"
            ),
            PlatformMatchData(
                platformName = "YouTube Shorts",
                rating = PlatformRating.EXCELLENT,
                ratingText = "Excellent",
                reason = "Algorithm favors fast hooks & direct product demonstrations.",
                logoEmoji = "🔴"
            ),
            PlatformMatchData(
                platformName = "Meesho",
                rating = PlatformRating.GOOD,
                ratingText = "Good",
                reason = "Direct conversion opportunity for budget-conscious buyers.",
                logoEmoji = "🛍️"
            ),
            PlatformMatchData(
                platformName = "Facebook",
                rating = PlatformRating.GOOD,
                ratingText = "Good",
                reason = "Strong reach among Tier-2 & Tier-3 regional audience.",
                logoEmoji = "📘"
            ),
            PlatformMatchData(
                platformName = "Amazon Influencer",
                rating = PlatformRating.AVERAGE,
                ratingText = "Average",
                reason = "Mid-tier commission potential based on current product tag.",
                logoEmoji = "📦"
            ),
            PlatformMatchData(
                platformName = "Flipkart Creator",
                rating = PlatformRating.AVERAGE,
                ratingText = "Average",
                reason = "Moderate catalog alignment for viral affiliate clicks.",
                logoEmoji = "⚡"
            )
        )

        // Buyer Intent
        val buyerIntentLevel = when {
            reel.productVisibilityScore >= 85 -> "Very High"
            reel.productVisibilityScore >= 75 -> "High"
            reel.productVisibilityScore >= 60 -> "Medium"
            else -> "Low"
        }

        val buyerIntent = BuyerIntentData(
            overallLevel = buyerIntentLevel,
            score = ((reel.productVisibilityScore + reel.ctaScore + reel.voiceScore) / 3).coerceIn(40, 98),
            reasons = listOf(
                BuyerIntentReason("Product Visibility", reel.productVisibilityScore, "Clear visual showcase in frame"),
                BuyerIntentReason("Trust & Authenticity", reel.voiceScore, "Natural tone builds high buyer confidence"),
                BuyerIntentReason("Voice Clarity", reel.voiceScore, "Crisp vocal explanation"),
                BuyerIntentReason("Price / Offer Callout", reel.ctaScore, "Prominent value callout"),
                BuyerIntentReason("Call to Action", reel.ctaScore, "Clear directions to buy/click link")
            )
        )

        // Watch Behaviour
        val watchBehaviour = WatchBehaviourData(
            avgWatchTimeSec = (18.5f * (reel.retentionScore / 100f)).coerceAtLeast(10f),
            totalLengthSec = 24.0f,
            watchTimePercent = ((reel.retentionScore + reel.hookScore) / 2).coerceIn(55, 96),
            replayChancePercent = (reel.hookScore * 0.72f).toInt().coerceIn(30, 88),
            shareChancePercent = (reel.energyScore * 0.8f).toInt().coerceIn(35, 92),
            saveChancePercent = (reel.productVisibilityScore * 0.85f).toInt().coerceIn(40, 95),
            commentChancePercent = (reel.ctaScore * 0.65f).toInt().coerceIn(25, 80),
            profileVisitChancePercent = (reel.finalAiScore * 0.55f).toInt().coerceIn(20, 75),
            followChancePercent = (reel.finalAiScore * 0.42f).toInt().coerceIn(15, 65)
        )

        // Ideal Post Time
        val idealPostTime = IdealPostTimeData(
            bestDay = "Wednesday & Saturday",
            bestTimeWindow = "7:30 PM – 9:30 PM",
            worstTimeWindow = "2:00 AM – 6:00 AM",
            competitionLevel = "Moderate Peak",
            expectedActiveAudience = "84,000+ Active Followers",
            peakHourLabel = "8:15 PM Prime Time"
        )

        // Content Matches
        val contentMatches = listOf(
            ContentMatchData("UGC Product Review", 95),
            ContentMatchData("Beauty / Product Demo", 92),
            ContentMatchData("Problem-Solution Reel", 87),
            ContentMatchData("Lifestyle Vlogging", 61),
            ContentMatchData("Educational / How-To", 21)
        )

        // Audience Emotion
        val audienceEmotions = listOf(
            AudienceEmotionData("Trust", "🤝", 88, "Audience feels safe buying recommended item"),
            AudienceEmotionData("Curiosity", "💡", 94, "Strong hook triggers immediate curiosity"),
            AudienceEmotionData("Excitement", "🔥", 82, "High energy visuals boost enthusiasm"),
            AudienceEmotionData("Buying Intent", "💳", 91, "Direct product demo drives desire to purchase"),
            AudienceEmotionData("Entertainment", "🎭", 79, "Engaging pacing keeps viewers watching"),
            AudienceEmotionData("Confusion", "❓", 12, "Low confusion thanks to crisp narration")
        )

        // Top 3 Improvements ONLY
        val top3Improvements = listOf(
            "Show product within the first 1.5 seconds for instant visual hook.",
            "Mention exact price or special discount offer earlier in the voiceover.",
            "Pace speech 10% slower during feature breakdown for Tier-2 audience clarity."
        )

        // Viri Recommendation
        val viriRec = if (isBeautyOrSkincare) {
            "Ye reel mostly 18–28 women ko attract karegi. Instagram aur Shorts pe sabse best conversion milega! 🚀"
        } else if (isKitchenOrFood) {
            "Ye reel 24–40 homemakers aur food lovers ke liye best hai. Facebook aur Shorts pe bohot accha reach milega! 🌟"
        } else {
            "Ye reel Gen-Z & young adults ko target karti hai. High visual hook se Instagram pe boom karegi! 💥"
        }

        return AiAudiencePersonaReport(
            reelTitle = reel.title,
            confidencePercent = confidence,
            isLowConfidence = confidence < 70,
            primaryAudience = primary,
            audienceCards = cards,
            interestRadar = radar,
            platformMatches = platforms,
            buyerIntent = buyerIntent,
            watchBehaviour = watchBehaviour,
            idealPostTime = idealPostTime,
            contentMatches = contentMatches,
            audienceEmotions = audienceEmotions,
            top3Improvements = top3Improvements,
            viriRecommendation = viriRec
        )
    }
}
