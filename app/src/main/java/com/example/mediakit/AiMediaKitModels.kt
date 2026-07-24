package com.example.mediakit

import androidx.compose.ui.graphics.Color

enum class MediaKitTheme(
    val title: String,
    val primaryGradient: List<Color>,
    val accentColor: Color,
    val surfaceColor: Color,
    val textColor: Color
) {
    ONYX_BLACK(
        title = "Onyx Black",
        primaryGradient = listOf(Color(0xFF1E1E24), Color(0xFF0F0F12)),
        accentColor = Color(0xFFFF3366),
        surfaceColor = Color(0x1AFFFFFF),
        textColor = Color(0xFFFFFFFF)
    ),
    GOLD_LUXE(
        title = "Gold Luxe",
        primaryGradient = listOf(Color(0xFF2A2315), Color(0xFF141009)),
        accentColor = Color(0xFFFFD700),
        surfaceColor = Color(0x26FFD700),
        textColor = Color(0xFFFFF8E7)
    ),
    MINT_CYBER(
        title = "Neon Mint",
        primaryGradient = listOf(Color(0xFF0A221C), Color(0xFF04100D)),
        accentColor = Color(0xFF00FFCC),
        surfaceColor = Color(0x2200FFCC),
        textColor = Color(0xFFE6FFF9)
    ),
    SUNSET_VIOLET(
        title = "Sunset Violet",
        primaryGradient = listOf(Color(0xFF28102C), Color(0xFF120516)),
        accentColor = Color(0xFFFF4081),
        surfaceColor = Color(0x22FF4081),
        textColor = Color(0xFFFAEDFF)
    )
}

data class MediaKitBrandCategory(
    val categoryName: String,
    val matchPercentage: Int,
    val keyAdvantage: String,
    val isVerifiedMatch: Boolean = true
)

data class MediaKitPortfolioItem(
    val title: String,
    val postType: String,
    val highlightTag: String,
    val verifiedMetric: String
)

data class CollaborationReadiness(
    val score: Int,
    val tierLabel: String,
    val readinessBullets: List<String>,
    val isFullyVerified: Boolean
)

data class SocialLinkItem(
    val platform: String,
    val handleOrValue: String,
    val isVerified: Boolean
)

data class AiMediaKit(
    val creatorUsername: String,
    val isUsernameVerified: Boolean,
    val creatorDisplayName: String,
    val isDisplayNameVerified: Boolean,
    val creatorBio: String,
    val isBioVerified: Boolean,
    val followersFormatted: String,
    val isFollowersVerified: Boolean,
    val followingFormatted: String,
    val isFollowingVerified: Boolean,
    val postsFormatted: String,
    val isPostsVerified: Boolean,
    val profilePicUri: String?,
    val creatorSummary: String,
    val contentNiche: String,
    val brandFriendlyCategories: List<MediaKitBrandCategory>,
    val collaborationReadiness: CollaborationReadiness,
    val portfolioItems: List<MediaKitPortfolioItem>,
    val businessEmail: String,
    val isEmailVerified: Boolean,
    val socialLinks: List<SocialLinkItem>
)
