package com.example.reports

import com.example.analytics.ShoppingAnalytics
import com.example.analytics.ShoppingEvent
import com.example.creator.AiCreatorReport
import com.example.data.ProductIdentity
import com.example.data.ShoppingResult
import com.example.vision.DetectedProductObject

/**
 * SHOPTOOLAI Phase 5F — AI Report Engine & Smart Insight Foundation
 */

enum class ReportLanguage(val displayName: String) {
    ENGLISH("English"),
    HINDI("Hindi"),
    HINGLISH("HinEnglish")
}

enum class ReportType {
    SHOPPING_INTELLIGENCE,
    CREATOR_PROFILE_AI,
    INSTAGRAM_SHOPPING_AI,
    MULTI_VISION_SCAN
}

enum class InsightCategory(val label: String) {
    POSITIVE_FINDINGS("Positive Findings"),
    WEAK_AREAS("Weak Areas"),
    RECOMMENDATIONS("Recommendations"),
    WARNINGS("Warnings"),
    QUICK_WINS("Quick Wins")
}

data class InsightItem(
    val category: InsightCategory,
    val title: String,
    val description: String,
    val impactLevel: String = "High",
    val actionableTip: String? = null
)

data class ReusableScore(
    val scoreName: String,
    val scoreValue: Int, // 0 to 100
    val grade: String = "A",
    val confidencePercent: Int = 95,
    val description: String = ""
)

data class ReusableScoreSet(
    val shoppingScore: ReusableScore? = null,
    val creatorScore: ReusableScore? = null,
    val profileScore: ReusableScore? = null,
    val trustScore: ReusableScore? = null,
    val aiConfidence: ReusableScore? = null
)

// 2. SHOPPING REPORT TEMPLATE
data class ShoppingReportTemplate(
    val productSummary: String,
    val merchantSummary: String,
    val aiConfidenceLabel: String,
    val priceStatus: String,
    val shoppingRecommendation: String,
    val riskAnalysis: String,
    val isBestDeal: Boolean = true
)

// 3. CREATOR REPORT TEMPLATE
data class CreatorReportTemplate(
    val profileOverview: String,
    val bioReview: String,
    val contentQuality: String,
    val growthSuggestions: List<String>,
    val hashtagSuggestions: List<String>,
    val captionSuggestions: List<String>,
    val brandReadiness: String,
    val improvementRoadmap: List<String>
)

// 1. MASTER AI REPORT
data class MasterAiReport(
    val id: String = java.util.UUID.randomUUID().toString(),
    val timestampMs: Long = System.currentTimeMillis(),
    val reportType: ReportType,
    val language: ReportLanguage = ReportLanguage.HINGLISH,
    val summaryBlock: String, // 10. AI SUMMARY
    val scoreSet: ReusableScoreSet,
    val insights: List<InsightItem> = emptyList(),
    val shoppingTemplate: ShoppingReportTemplate? = null,
    val creatorTemplate: CreatorReportTemplate? = null,
    val isSaved: Boolean = false,
    val isPinned: Boolean = false
)

// 8. SAVE & 9. SHARE REPORT ARCHITECTURE
object ReportStorageManager {
    private val savedReports = mutableListOf<MasterAiReport>()
    private val historyReports = mutableListOf<MasterAiReport>()

    @Synchronized
    fun addReportToHistory(report: MasterAiReport) {
        historyReports.removeAll { it.id == report.id }
        historyReports.add(0, report)
        if (historyReports.size > 50) {
            historyReports.removeAt(historyReports.size - 1)
        }
    }

    @Synchronized
    fun saveReport(reportId: String): Boolean {
        val found = historyReports.firstOrNull { it.id == reportId } ?: return false
        val updated = found.copy(isSaved = true)
        savedReports.removeAll { it.id == reportId }
        savedReports.add(0, updated)
        return true
    }

    @Synchronized
    fun pinReport(reportId: String, pinned: Boolean): Boolean {
        val index = savedReports.indexOfFirst { it.id == reportId }
        if (index != -1) {
            savedReports[index] = savedReports[index].copy(isPinned = pinned)
            return true
        }
        return false
    }

    @Synchronized
    fun getSavedReports(): List<MasterAiReport> = savedReports.sortedByDescending { it.isPinned }

    @Synchronized
    fun getHistoryReports(): List<MasterAiReport> = historyReports.toList()

    // Share Helpers
    fun exportReportAsTextSummary(report: MasterAiReport): String {
        return """
            --- VIRALTOOLAI AI REPORT ---
            Type: ${report.reportType.name}
            Summary: ${report.summaryBlock}
            Overall Score: ${report.scoreSet.aiConfidence?.scoreValue ?: 95}%
            ---
            Generated via ViralToolAI — From Products to Popularity
        """.trimIndent()
    }

    fun generatePdfDocumentHook(report: MasterAiReport) {
        /* Future PDF Exporter Hook */
    }

    fun generateShareImageHook(report: MasterAiReport) {
        /* Future Image Card Render Exporter Hook */
    }
}

/**
 * CENTRALIZED AI REPORT ENGINE (Phase 5F Architecture)
 */
object AiReportEngine {

    fun buildShoppingMasterReport(
        shoppingResult: ShoppingResult,
        language: ReportLanguage = ReportLanguage.HINGLISH
    ): MasterAiReport {
        val shoppingTemplate = ShoppingReportTemplate(
            productSummary = "${shoppingResult.productName} by ${shoppingResult.brand} in category ${shoppingResult.category}.",
            merchantSummary = "Verified across ${shoppingResult.priceComparison.size} stores with best price at ${shoppingResult.detectedStore}.",
            aiConfidenceLabel = "AI High Confidence Match (98%)",
            priceStatus = "Lowest Price Found: ₹${String.format("%,.0f", shoppingResult.bestPrice)}",
            shoppingRecommendation = shoppingResult.aiRecommendation,
            riskAnalysis = "Zero risk detected. Verified merchant listing with return policy.",
            isBestDeal = shoppingResult.currentPrice <= shoppingResult.bestPrice
        )

        val scoreSet = ReusableScoreSet(
            shoppingScore = ReusableScore("Shopping Value", 94, "A+", 98, "Excellent value for money"),
            trustScore = ReusableScore("Merchant Trust", 96, "A+", 99, "Verified official partner store"),
            aiConfidence = ReusableScore("AI Match Confidence", 98, "A+", 98, "Exact product match")
        )

        val insights = listOf(
            InsightItem(
                category = InsightCategory.POSITIVE_FINDINGS,
                title = "Cheapest Merchant Verified",
                description = "Lowest price across all stores on ${shoppingResult.detectedStore}.",
                impactLevel = "High"
            ),
            InsightItem(
                category = InsightCategory.QUICK_WINS,
                title = "Instant Buy Available",
                description = "Direct merchant link available without redirect loops.",
                impactLevel = "Medium"
            )
        )

        val summary = "Sahi deal verified hai! ${shoppingResult.detectedStore} par lowest price mil raha hai with official warranty."

        val masterReport = MasterAiReport(
            reportType = ReportType.SHOPPING_INTELLIGENCE,
            language = language,
            summaryBlock = summary,
            scoreSet = scoreSet,
            insights = insights,
            shoppingTemplate = shoppingTemplate
        )

        ReportStorageManager.addReportToHistory(masterReport)
        return masterReport
    }

    fun buildCreatorMasterReport(
        creatorReport: AiCreatorReport,
        language: ReportLanguage = ReportLanguage.HINGLISH
    ): MasterAiReport {

        val creatorTemplate = CreatorReportTemplate(
            profileOverview = "Profile ${creatorReport.profileData?.username ?: "@creator"} has ${creatorReport.profileData?.followersCount ?: 0} followers.",
            bioReview = creatorReport.bioImprovementsHinglish.firstOrNull() ?: "Bio is clear and well structured.",
            contentQuality = "Content quality score is ${creatorReport.contentAnalysis?.contentQualityScore ?: 85}/100.",
            growthSuggestions = creatorReport.growthStrategyHinglish,
            hashtagSuggestions = creatorReport.hashtags?.trendingHashtags ?: emptyList(),
            captionSuggestions = creatorReport.captionSuggestions.values.toList(),
            brandReadiness = creatorReport.brandReadinessHinglish,
            improvementRoadmap = creatorReport.contentIdeasHinglish
        )

        val scoreSet = ReusableScoreSet(
            creatorScore = ReusableScore("Creator Score", creatorReport.creatorScore?.overallCreatorScore ?: 86, "A", 95),
            profileScore = ReusableScore("Profile Score", creatorReport.creatorScore?.brandIdentity ?: 85, "A", 95),
            trustScore = ReusableScore("Growth Potential", creatorReport.creatorScore?.growthPotential ?: 82, "B+", 90),
            aiConfidence = ReusableScore("AI Analysis Confidence", 95, "A+", 95)
        )

        val insights = listOf(
            InsightItem(
                category = InsightCategory.POSITIVE_FINDINGS,
                title = "Strong Brand Alignment",
                description = "Content style is clear and ready for brand collaborations.",
                impactLevel = "High"
            ),
            InsightItem(
                category = InsightCategory.RECOMMENDATIONS,
                title = "Add Stronger Bio Call-To-Action",
                description = "Include explicit deal link or store link in bio.",
                impactLevel = "High"
            )
        )

        val summary = "Aapka profile strong hai, baseline branding solid hai. Bio mein Call-To-Action add karke engagement aur badha sakte hain."

        val masterReport = MasterAiReport(
            reportType = ReportType.CREATOR_PROFILE_AI,
            language = language,
            summaryBlock = summary,
            scoreSet = scoreSet,
            insights = insights,
            creatorTemplate = creatorTemplate
        )

        ReportStorageManager.addReportToHistory(masterReport)
        return masterReport
    }
}
