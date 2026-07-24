package com.example.engine.phase10f

/**
 * SHOPTOOLAI Phase 10E -> Phase 10F Forward Compatibility Architecture
 * Prepares interfaces and engine stubs for Festival Planner, Campaign Planner,
 * Brand Campaign Calendar, and AI Reminder Engine without requiring UI redesigns.
 */

data class FestivalCampaignPayload(
    val festivalName: String = "Upcoming Sale Event",
    val daysRemaining: Int = 14,
    val recommendedProductFocus: String = "Trending Gadgets & Shopping Deals",
    val statusMessage: String = "AI Campaign Calendar Ready"
)

data class BrandCalendarEntry(
    val date: String,
    val campaignTitle: String,
    val brandPartner: String,
    val contentFormat: String
)

interface FestivalPlannerEngine {
    suspend fun getUpcomingFestivalCampaigns(): List<FestivalCampaignPayload>
}

interface CampaignPlannerEngine {
    suspend fun generateBrandCampaignCalendar(brandName: String): List<BrandCalendarEntry>
}

interface AiReminderEngine {
    suspend fun scheduleContentNotification(title: String, timeInMillis: Long): Boolean
}

object Phase10FRegistry {
    val defaultFestivalPayload = FestivalCampaignPayload()
    val sampleCalendarEntries = listOf(
        BrandCalendarEntry("Oct 15", "Diwali Unboxing Blast", "Verified Store Partner", "Reel + Story"),
        BrandCalendarEntry("Nov 25", "Black Friday Shopping Guide", "Global Retail Partner", "Carousel + Reel")
    )
}
