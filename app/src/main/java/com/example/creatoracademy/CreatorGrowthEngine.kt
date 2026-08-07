package com.example.creatoracademy

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AnalysedReel(
    val id: String,
    val title: String,
    val date: String,
    val timestamp: Long = System.currentTimeMillis(),
    val platform: String = "Instagram Reels",
    val category: String = "Tech Review",
    val finalAiScore: Int = 88,
    val uploadConfidence: Int = 85,
    val hookScore: Int = 90,
    val retentionScore: Int = 84,
    val lightingScore: Int = 88,
    val voiceScore: Int = 82,
    val thumbnailScore: Int = 89,
    val ctaScore: Int = 80,
    val energyScore: Int = 85,
    val productVisibilityScore: Int = 87,
    val aiSummary: String = "Strong visual hook with excellent product clarity.",
    val weaknesses: List<String> = listOf("CTA appears 2s late", "Voice energy dips at 0:15"),
    val strengths: List<String> = listOf("Top 5% scroll-stopping hook", "Bright 3-point lighting setup")
)

data class CreatorGrowthLevel(
    val levelName: String, // Beginner, Growing, Advanced, Professional, Elite, Master Creator
    val currentXp: Int,
    val nextLevelXp: Int,
    val progressPercent: Float,
    val badgeEmoji: String
)

data class CreatorGrowthStats(
    val currentStreakDays: Int,
    val totalReelsAnalysed: Int,
    val averageAiScore: Int,
    val currentUploadStreak: Int,
    val monthlyGrowthPercent: Int
)

data class RecurringMistake(
    val title: String,
    val occurrences: Int,
    val advice: String
)

data class BiggestImprovement(
    val title: String,
    val gainPercentage: Int,
    val note: String
)

data class MonthlyReportData(
    val monthName: String,
    val reelsAnalysedCount: Int,
    val hookGain: Int,
    val retentionGain: Int,
    val ctaGain: Int,
    val voiceGain: Int,
    val thumbnailGain: Int
)

data class CreatorBadgeItem(
    val id: String,
    val title: String,
    val emoji: String,
    val description: String,
    val isUnlocked: Boolean,
    val unlockedDate: String,
    val progressText: String
)

data class AiCoachMemoryConversation(
    val id: String,
    val date: String,
    val viriQuote: String,
    val contextTag: String,
    val isActionItemCompleted: Boolean
)

data class SmartChallenge(
    val id: String,
    val type: String, // Daily, Weekly, Monthly
    val title: String,
    val rewardXp: Int,
    val isCompleted: Boolean,
    val progressText: String
)

data class YearlyMilestone(
    val id: String,
    val date: String,
    val title: String,
    val description: String,
    val emoji: String
)

object CreatorGrowthEngine {
    private const val PREF_NAME = "creator_growth_memory_engine_prefs"
    private const val KEY_REELS_JSON = "key_reels_history_json"
    private const val KEY_CREATOR_XP = "key_creator_xp"
    private const val KEY_STREAK_DAYS = "key_streak_days"
    private const val KEY_UPLOAD_STREAK = "key_upload_streak"
    private const val KEY_COMPLETED_CHALLENGES = "key_completed_challenges"
    private const val KEY_USER_ACCOUNT_SYNCED = "key_user_account_synced"

    // 1. Get or Initialize 30 Reel History
    fun getAnalysedReels(context: Context): List<AnalysedReel> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonStr = prefs.getString(KEY_REELS_JSON, null)
        if (jsonStr.isNullOrEmpty()) {
            val initialList = generateInitial30ReelHistory()
            saveReelsHistory(context, initialList)
            return initialList
        }
        return parseReelsFromJson(jsonStr)
    }

    fun addAnalysedReel(context: Context, newReel: AnalysedReel) {
        val currentList = getAnalysedReels(context).toMutableList()
        currentList.add(0, newReel)
        if (currentList.size > 30) {
            currentList.removeAt(currentList.lastIndex)
        }
        saveReelsHistory(context, currentList)

        // Add XP (+75 XP per Reel Analysis)
        addXp(context, 75)
        
        // Update Upload Streak
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val curUploadStreak = prefs.getInt(KEY_UPLOAD_STREAK, 5)
        prefs.edit().putInt(KEY_UPLOAD_STREAK, curUploadStreak + 1).apply()
    }

    private fun saveReelsHistory(context: Context, list: List<AnalysedReel>) {
        val jsonArray = JSONArray()
        list.forEach { reel ->
            val obj = JSONObject().apply {
                put("id", reel.id)
                put("title", reel.title)
                put("date", reel.date)
                put("timestamp", reel.timestamp)
                put("platform", reel.platform)
                put("category", reel.category)
                put("finalAiScore", reel.finalAiScore)
                put("uploadConfidence", reel.uploadConfidence)
                put("hookScore", reel.hookScore)
                put("retentionScore", reel.retentionScore)
                put("lightingScore", reel.lightingScore)
                put("voiceScore", reel.voiceScore)
                put("thumbnailScore", reel.thumbnailScore)
                put("ctaScore", reel.ctaScore)
                put("energyScore", reel.energyScore)
                put("productVisibilityScore", reel.productVisibilityScore)
                put("aiSummary", reel.aiSummary)
                
                val wArr = JSONArray()
                reel.weaknesses.forEach { wArr.put(it) }
                put("weaknesses", wArr)

                val sArr = JSONArray()
                reel.strengths.forEach { sArr.put(it) }
                put("strengths", sArr)
            }
            jsonArray.put(obj)
        }
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_REELS_JSON, jsonArray.toString())
            .apply()
    }

    private fun parseReelsFromJson(jsonStr: String): List<AnalysedReel> {
        val list = mutableListOf<AnalysedReel>()
        try {
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                
                val wList = mutableListOf<String>()
                if (obj.has("weaknesses")) {
                    val wArr = obj.getJSONArray("weaknesses")
                    for (j in 0 until wArr.length()) {
                        wList.add(wArr.getString(j))
                    }
                }

                val sList = mutableListOf<String>()
                if (obj.has("strengths")) {
                    val sArr = obj.getJSONArray("strengths")
                    for (j in 0 until sArr.length()) {
                        sList.add(sArr.getString(j))
                    }
                }

                list.add(
                    AnalysedReel(
                        id = obj.optString("id", "reel_$i"),
                        title = obj.optString("title", "Reel #$i"),
                        date = obj.optString("date", "Aug 06, 2026"),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                        platform = obj.optString("platform", "Instagram Reels"),
                        category = obj.optString("category", "Tech Review"),
                        finalAiScore = obj.optInt("finalAiScore", 85),
                        uploadConfidence = obj.optInt("uploadConfidence", 82),
                        hookScore = obj.optInt("hookScore", 88),
                        retentionScore = obj.optInt("retentionScore", 80),
                        lightingScore = obj.optInt("lightingScore", 85),
                        voiceScore = obj.optInt("voiceScore", 80),
                        thumbnailScore = obj.optInt("thumbnailScore", 86),
                        ctaScore = obj.optInt("ctaScore", 78),
                        energyScore = obj.optInt("energyScore", 84),
                        productVisibilityScore = obj.optInt("productVisibilityScore", 85),
                        aiSummary = obj.optString("aiSummary", "Viral potential is high with good framing."),
                        weaknesses = wList.ifEmpty { listOf("Include clearer CTA") },
                        strengths = sList.ifEmpty { listOf("Great lighting and hook") }
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (list.isEmpty()) generateInitial30ReelHistory() else list
    }

    // 2. Creator Level Calculation
    fun getCreatorLevel(context: Context): CreatorGrowthLevel {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val xp = prefs.getInt(KEY_CREATOR_XP, 1250)
        return calculateLevelFromXp(xp)
    }

    fun calculateLevelFromXp(xp: Int): CreatorGrowthLevel {
        return when {
            xp < 300 -> CreatorGrowthLevel("Beginner", xp, 300, (xp / 300f).coerceIn(0f, 1f), "🌱")
            xp < 800 -> CreatorGrowthLevel("Growing", xp, 800, ((xp - 300) / 500f).coerceIn(0f, 1f), "🚀")
            xp < 1800 -> CreatorGrowthLevel("Advanced", xp, 1800, ((xp - 800) / 1000f).coerceIn(0f, 1f), "🔥")
            xp < 3500 -> CreatorGrowthLevel("Professional", xp, 3500, ((xp - 1800) / 1700f).coerceIn(0f, 1f), "⚡")
            xp < 6000 -> CreatorGrowthLevel("Elite", xp, 6000, ((xp - 3500) / 2500f).coerceIn(0f, 1f), "👑")
            else -> CreatorGrowthLevel("Master Creator", xp, 10000, 1f, "🏆")
        }
    }

    fun addXp(context: Context, xpAmount: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val current = prefs.getInt(KEY_CREATOR_XP, 1250)
        prefs.edit().putInt(KEY_CREATOR_XP, current + xpAmount).apply()
    }

    // 3. Stats Summary
    fun getGrowthStats(context: Context): CreatorGrowthStats {
        val reels = getAnalysedReels(context)
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val streakDays = prefs.getInt(KEY_STREAK_DAYS, 14)
        val uploadStreak = prefs.getInt(KEY_UPLOAD_STREAK, 6)

        val avgScore = if (reels.isNotEmpty()) reels.map { it.finalAiScore }.average().toInt() else 88
        return CreatorGrowthStats(
            currentStreakDays = streakDays,
            totalReelsAnalysed = reels.size,
            averageAiScore = avgScore,
            currentUploadStreak = uploadStreak,
            monthlyGrowthPercent = 24
        )
    }

    // 4. Recurring Top 3 Mistakes
    fun getTopRecurringMistakes(context: Context): List<RecurringMistake> {
        return listOf(
            RecurringMistake("❌ Show product late", 12, "Position product within first 2.5s to increase retention by +22%."),
            RecurringMistake("❌ Forget CTA at end", 9, "End every reel with clear 'Comment LINK' or 'Save for later' trigger."),
            RecurringMistake("❌ Low voice energy", 7, "Boost speaking cadence by +15% and cut micro pauses in audio editor.")
        )
    }

    // 5. Biggest Improvements
    fun getBiggestImprovements(context: Context): List<BiggestImprovement> {
        return listOf(
            BiggestImprovement("✔ Better Thumbnails", 21, "CTR jumped from 4.2% to 8.9% with text overlays."),
            BiggestImprovement("✔ Better Lighting", 18, "Subject clarity score increased from 72 to 92."),
            BiggestImprovement("✔ Faster Hooks", 15, "0-3s retention rate increased by +34%."),
            BiggestImprovement("✔ Dynamic Expressions", 12, "Audience connection index at all-time high.")
        )
    }

    // 6. Monthly Report Data
    fun getMonthlyReport(context: Context): MonthlyReportData {
        val reels = getAnalysedReels(context)
        val count = reels.size.coerceAtLeast(18)
        return MonthlyReportData(
            monthName = "This Month",
            reelsAnalysedCount = count,
            hookGain = 12,
            retentionGain = 9,
            ctaGain = 15,
            voiceGain = 6,
            thumbnailGain = 21
        )
    }

    // 7. Creator Badges
    fun getCreatorBadges(context: Context): List<CreatorBadgeItem> {
        val reels = getAnalysedReels(context)
        val totalCount = reels.size
        val maxScore = reels.maxOfOrNull { it.finalAiScore } ?: 91

        return listOf(
            CreatorBadgeItem("badge_hook", "Hook Master", "🔥", "Achieved Hook Score > 90 in 5+ Reels", true, "Aug 02, 2026", "Completed"),
            CreatorBadgeItem("badge_voice", "Voice Pro", "🎤", "Maintained Voice Energy > 85 for 3 consecutive reels", true, "Aug 04, 2026", "Completed"),
            CreatorBadgeItem("badge_product", "Product Expert", "📦", "Product Visibility score above 90% in 10 reels", true, "Aug 05, 2026", "Completed"),
            CreatorBadgeItem("badge_thumb", "Thumbnail King", "📸", "Created 5 high-CTR custom thumbnail covers", totalCount >= 15, "Aug 01, 2026", if (totalCount >= 15) "Unlocked" else "$totalCount/15 Reels"),
            CreatorBadgeItem("badge_streak", "Consistency Hero", "⚡", "Maintained 7-day upload streak", true, "Aug 03, 2026", "Completed"),
            CreatorBadgeItem("badge_viral", "Viral Creator", "🏆", "Predicted viral AI score > 92", maxScore >= 92, "Aug 06, 2026", if (maxScore >= 92) "Unlocked" else "Highest: $maxScore/92")
        )
    }

    // 8. AI Coach Viri Memory Conversations
    fun getCoachMemoryConversations(context: Context): List<AiCoachMemoryConversation> {
        return listOf(
            AiCoachMemoryConversation("conv_1", "Last Week", "Last week maine bola tha CTA improve karo. Great! Is reel me +15% CTA improvement dikh raha hai.", "CTA Growth", true),
            AiCoachMemoryConversation("conv_2", "3 Days Ago", "Voice energy pehle se kafi clear aur high audio curve me hai. Keep this energy!", "Voice Calibration", true),
            AiCoachMemoryConversation("conv_3", "Yesterday", "Ab thumbnail text contrast pe thoda kaam karte hain. Next reel me yellow + black text overlay try karo.", "Thumbnail Optimization", false)
        )
    }

    // 9. Smart Challenges
    fun getSmartChallenges(context: Context): List<SmartChallenge> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val completedSet = prefs.getStringSet(KEY_COMPLETED_CHALLENGES, emptySet()) ?: emptySet()

        return listOf(
            SmartChallenge("c_daily", "Daily Mission", "Show product within first 2 seconds in your next reel", 50, completedSet.contains("c_daily"), "1/1 Reel"),
            SmartChallenge("c_weekly", "Weekly Challenge", "Increase Hook Score above 90 across 3 consecutive reels", 150, completedSet.contains("c_weekly"), "2/3 Reels"),
            SmartChallenge("c_monthly", "Monthly Challenge", "Upload & analyse 20 Reels this month", 500, completedSet.contains("c_monthly"), "18/20 Reels")
        )
    }

    fun completeChallenge(context: Context, challengeId: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val set = (prefs.getStringSet(KEY_COMPLETED_CHALLENGES, emptySet()) ?: emptySet()).toMutableSet()
        set.add(challengeId)
        prefs.edit().putStringSet(KEY_COMPLETED_CHALLENGES, set).apply()

        // Reward XP based on challenge
        val reward = when (challengeId) {
            "c_daily" -> 50
            "c_weekly" -> 150
            "c_monthly" -> 500
            else -> 100
        }
        addXp(context, reward)
    }

    // 10. Yearly Timeline
    fun getYearlyTimeline(context: Context): List<YearlyMilestone> {
        return listOf(
            YearlyMilestone("m_1", "Jan 12, 2026", "Joined ViralToolAI", "Started creator journey on ViralToolAI OS.", "🎉"),
            YearlyMilestone("m_2", "Feb 01, 2026", "First Reel Analysis", "Analysed first product review reel (Score: 71).", "📈"),
            YearlyMilestone("m_3", "Mar 18, 2026", "First Viral Prediction", "AI predicted 250K+ views on Outfit Haul reel.", "🚀"),
            YearlyMilestone("m_4", "May 04, 2026", "First Elite Badge", "Unlocked Hook Master & Voice Pro badges.", "👑"),
            YearlyMilestone("m_5", "Jul 22, 2026", "Highest AI Score", "Scored 96/100 on Tech Accessory Breakdown reel.", "🏆"),
            YearlyMilestone("m_6", "Aug 06, 2026", "Creator Memory Synced", "30 Reel AI Memory active & auto-syncing.", "⚡")
        )
    }

    // 11. Sync Status
    fun isAccountSynced(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_USER_ACCOUNT_SYNCED, true)
    }

    // Seed realistic 30 Reel History if empty
    private fun generateInitial30ReelHistory(): List<AnalysedReel> {
        val categories = listOf("Tech Review", "Fashion Haul", "Meesho Deal", "Wishlink Store", "Lifestyle Vlog", "Fitness Shorts")
        val platforms = listOf("Instagram Reels", "YouTube Shorts")
        val list = mutableListOf<AnalysedReel>()

        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        val baseTime = System.currentTimeMillis() - (30 * 24 * 3600 * 1000L)

        for (i in 30 downTo 1) {
            val dateStr = sdf.format(Date(baseTime + (31 - i) * 24 * 3600 * 1000L))
            val baseScore = 70 + (i * 23 / 30) + (i % 5)
            val score = baseScore.coerceIn(65, 96)
            
            list.add(
                AnalysedReel(
                    id = "reel_history_$i",
                    title = "Reel #$i • ${categories[i % categories.size]}",
                    date = dateStr,
                    timestamp = baseTime + (31 - i) * 24 * 3600 * 1000L,
                    platform = platforms[i % platforms.size],
                    category = categories[i % categories.size],
                    finalAiScore = score,
                    uploadConfidence = (score - 3).coerceIn(60, 98),
                    hookScore = (score + 2).coerceIn(65, 99),
                    retentionScore = (score - 2).coerceIn(60, 95),
                    lightingScore = (score + 1).coerceIn(65, 98),
                    voiceScore = (score - 4).coerceIn(60, 94),
                    thumbnailScore = (score + 3).coerceIn(65, 99),
                    ctaScore = (score - 5).coerceIn(55, 92),
                    energyScore = (score - 1).coerceIn(60, 96),
                    productVisibilityScore = (score + 2).coerceIn(65, 98),
                    aiSummary = "Reel #$i analysis completed. Solid pacing with high retention potential.",
                    weaknesses = listOf("CTA could be placed earlier", "Micro audio gaps detected"),
                    strengths = listOf("Exceptional 3s hook clarity", "Vibrant lighting & color grade")
                )
            )
        }
        return list
    }
}
