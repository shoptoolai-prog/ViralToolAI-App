package com.example.engine

import com.example.data.ShoppingResult

/**
 * SHOPTOOLAI Master Phase 10E — AI Content Planner Engine
 * Generates 30-Day Content Roadmaps, Daily Content Ideas, Reel Schedule,
 * Content Balance Ratios (AI Estimate), Creator Goal Checklists, and Smart Reminder Stubs.
 * Never invents fake trending events or stats.
 */

data class RoadmapWeek(
    val weekNumber: Int,
    val weekTitle: String,
    val focusArea: String, // Shopping Reviews, Lifestyle, Educational, Brand Building
    val keyDeliverable: String,
    val primaryFormat: String
)

data class DailyIdeaCard(
    val ideaType: String, // Product Review, Comparison, Top 5 Products, Hidden Features, Budget Picks, Gift Ideas, Short Tips
    val title: String,
    val hookSuggestion: String,
    val estimatedDuration: String
)

data class ReelScheduleDay(
    val dayName: String,
    val contentType: String,
    val bestPostingSlot: String,
    val actionNote: String
)

data class ContentBalanceEstimate(
    val reviewContentPercent: Int = 40,
    val educationalPercent: Int = 25,
    val lifestylePercent: Int = 20,
    val entertainmentPercent: Int = 15,
    val note: String = "AI Suggested Balance for High Retention"
)

data class CreatorGoalItem(
    val id: String,
    val title: String,
    val category: String,
    val isCompleted: Boolean = false
)

data class SmartReminderStub(
    val reminderType: String,
    val label: String,
    val status: String = "Coming Soon"
)

data class ContentPlannerPackage(
    val productName: String,
    val storeName: String,
    val category: String,
    val roadmapWeeks: List<RoadmapWeek>,
    val dailyIdeas: List<DailyIdeaCard>,
    val reelSchedule: List<ReelScheduleDay>,
    val contentBalance: ContentBalanceEstimate,
    val goals: List<CreatorGoalItem>,
    val reminders: List<SmartReminderStub>
)

object AiContentPlannerEngine {

    fun generatePlannerPackage(resultData: ShoppingResult): ContentPlannerPackage {
        val name = resultData.productName.ifBlank { "Featured Product" }
        val category = resultData.category.ifBlank { "Shopping" }
        val store = resultData.detectedStore.ifBlank { "Verified Store" }

        // 30-Day Roadmap (4 Weeks)
        val roadmap = listOf(
            RoadmapWeek(
                weekNumber = 1,
                weekTitle = "Unboxing & First Impressions",
                focusArea = "Shopping Reviews",
                keyDeliverable = "Detailed Macro Video + Pricing Breakdown for $name",
                primaryFormat = "Reels & Shorts"
            ),
            RoadmapWeek(
                weekNumber = 2,
                weekTitle = "Deep Dive & Comparison",
                focusArea = "Educational",
                keyDeliverable = "Versus Comparison in $category category",
                primaryFormat = "Carousel & Reels"
            ),
            RoadmapWeek(
                weekNumber = 3,
                weekTitle = "Practical Integration",
                focusArea = "Lifestyle",
                keyDeliverable = "Aesthetic Day-in-the-Life setup using $name",
                primaryFormat = "Aesthetic Short & Story Highlights"
            ),
            RoadmapWeek(
                weekNumber = 4,
                weekTitle = "Community & Buying Tips",
                focusArea = "Brand Building",
                keyDeliverable = "Top Deals Q&A + Verified $store Coupon Alerts",
                primaryFormat = "Interactive Q&A & Live Session"
            )
        )

        // Daily Content Ideas (7 types)
        val ideas = listOf(
            DailyIdeaCard("Product Review", "Is $name worth buying in 2026?", "Honest review after testing $name from $store.", "30s"),
            DailyIdeaCard("Comparison", "$name vs Top $category Alternatives", "Comparing features and value for money side-by-side.", "45s"),
            DailyIdeaCard("Top 5 Products", "Top 5 Must-Have $category Items Right Now", "Featuring $name at position #1 for value.", "60s"),
            DailyIdeaCard("Hidden Features", "Hidden Feature on $name Nobody Talks About", "Zoom-in close-up on special build quality.", "20s"),
            DailyIdeaCard("Budget Picks", "Best Value $category Deal Under Budget", "Highlighting live deal pricing on $store.", "25s"),
            DailyIdeaCard("Gift Ideas", "Ultimate Gift Idea for $category Fans", "Unboxing visual aesthetic for gifts.", "30s"),
            DailyIdeaCard("Short Tips", "3 Quick Tips Before Buying $category Online", "Verified buyer checklist before checking out.", "15s")
        )

        // Reel Planner (Mon - Sun)
        val schedule = listOf(
            ReelScheduleDay("Monday", "Shopping Review", "6:30 PM", "Post $name Unboxing Reel"),
            ReelScheduleDay("Tuesday", "Hidden Feature", "8:00 PM", "Post Quick Tips & Macro Shots"),
            ReelScheduleDay("Wednesday", "Educational", "7:00 PM", "Post Comparison Chart in $category"),
            ReelScheduleDay("Thursday", "Budget Pick", "8:30 PM", "Post Verified $store Price Alert"),
            ReelScheduleDay("Friday", "Lifestyle Setup", "6:00 PM", "Post Aesthetic B-roll Montage"),
            ReelScheduleDay("Saturday", "Top 5 List", "5:00 PM", "Post Weekend Shopping Guide"),
            ReelScheduleDay("Sunday", "Community Q&A", "7:30 PM", "Reply to comments & share story highlights")
        )

        // Creator Goals
        val goals = listOf(
            CreatorGoalItem("goal_1", "Upload 3 High-Retention Reels this week", "Posting"),
            CreatorGoalItem("goal_2", "Reply to first 10 buyer comments within 15 mins", "Engagement"),
            CreatorGoalItem("goal_3", "Improve Hook strength with 0-3s visual zoom", "Quality"),
            CreatorGoalItem("goal_4", "Create clear CTA to save reel for discount link", "Conversion"),
            CreatorGoalItem("goal_5", "Use soft key lighting for crystal-clear macro shots", "Production")
        )

        // Smart Reminders (Coming Soon)
        val reminders = listOf(
            SmartReminderStub("daily", "Today's Reel Reminder"),
            SmartReminderStub("weekly", "Weekly Review Audit"),
            SmartReminderStub("monthly", "Monthly Content Performance Analysis")
        )

        return ContentPlannerPackage(
            productName = name,
            storeName = store,
            category = category,
            roadmapWeeks = roadmap,
            dailyIdeas = ideas,
            reelSchedule = schedule,
            contentBalance = ContentBalanceEstimate(),
            goals = goals,
            reminders = reminders
        )
    }
}
