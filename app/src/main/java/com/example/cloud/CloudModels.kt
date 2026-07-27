package com.example.cloud

enum class ToolStatus {
    ENABLED,
    DISABLED,
    COMING_SOON,
    MAINTENANCE;

    companion object {
        fun fromString(value: String?): ToolStatus {
            return when (value?.lowercase()?.trim()) {
                "disabled" -> DISABLED
                "coming_soon", "comingsoon", "soon" -> COMING_SOON
                "maintenance", "maint" -> MAINTENANCE
                else -> ENABLED
            }
        }
    }
}

data class AnnouncementConfig(
    val enabled: Boolean = false,
    val title: String = "",
    val message: String = "",
    val image: String = "",
    val buttonText: String = "OK",
    val buttonAction: String = ""
)

data class BrandAmbassadorConfig(
    val enabled: Boolean = true,
    val image: String = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=800&q=80",
    val durationMs: Long = 5000L
)

data class FestivalBannerConfig(
    val enabled: Boolean = true,
    val image: String = "https://images.unsplash.com/photo-1513151233558-d860c5398176?auto=format&fit=crop&w=800&q=80",
    val title: String = "Festival Special Offers!",
    val subtitle: String = "Exclusive Creator Tools & Shopping AI Discounts"
)

data class AnnouncementItem(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val imageUrl: String = "",
    val actionUrl: String = "",
    val category: String = "General",
    val timestamp: Long = System.currentTimeMillis()
)

data class CourseItem(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "General",
    val duration: String = "10 mins",
    val level: String = "Beginner",
    val iconUrl: String = "",
    val videoUrl: String = "",
    val keyTakeaways: List<String> = emptyList(),
    val isFeatured: Boolean = false
)

data class MotivationalQuote(
    val id: String = "",
    val quote: String = "",
    val author: String = "ShopTool AI",
    val category: String = "Growth"
)

data class ReleaseNote(
    val id: String = "",
    val version: String = "1.0.0",
    val title: String = "New Release",
    val features: List<String> = emptyList(),
    val releaseDate: Long = System.currentTimeMillis()
)

data class ToolUpdate(
    val id: String = "",
    val toolId: String = "",
    val toolName: String = "",
    val updateMessage: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class BrandCollab(
    val id: String = "",
    val brandName: String = "",
    val logoUrl: String = "",
    val payoutRange: String = "₹5,000 - ₹50,000",
    val category: String = "Tech & Fashion",
    val requirements: String = "Min 5K Followers",
    val applyUrl: String = ""
)

data class ShoppingTip(
    val id: String = "",
    val title: String = "",
    val tip: String = "",
    val merchant: String = "Flipkart",
    val savingsPotential: String = "Up to 40%"
)

data class FutureTool(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val targetReleaseDate: String = "Q3 2026",
    val votes: Int = 0
)
