package com.example.creatoracademy

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class MissionCategory {
    DAILY, WEEKLY, MONTHLY
}

enum class MissionRewardType {
    XP, BADGE, THEME, ROBOT_ACCESSORY, FRAME, ICON
}

data class CreatorMission(
    val id: String,
    val title: String,
    val description: String,
    val category: MissionCategory,
    val xpReward: Int,
    val progress: Int,
    val total: Int,
    val isCompleted: Boolean = false,
    val isSkipped: Boolean = false,
    val rewardType: MissionRewardType = MissionRewardType.XP,
    val rewardTitle: String = "",
    val iconEmoji: String = "🎯"
)

data class StreakData(
    val currentStreak: Int = 3,
    val bestStreak: Int = 7,
    val lastActiveDate: String = "",
    val milestones: List<Int> = listOf(1, 3, 7, 15, 30, 100)
)

data class CreatorLevelData(
    val level: Int = 2,
    val levelTitle: String = "Rising Star Creator",
    val currentXp: Int = 420,
    val xpForNextLevel: Int = 1000,
    val progressFraction: Float = 0.42f,
    val unlockedRewardsCount: Int = 4
)

data class CreatorMissionAchievement(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean = false,
    val unlockedDate: String = "",
    val rewardXp: Int = 100
)

data class MissionRewardItem(
    val id: String,
    val title: String,
    val type: MissionRewardType,
    val description: String,
    val iconEmoji: String,
    val requiredLevel: Int,
    val isUnlocked: Boolean,
    val previewGradient: List<Long> = listOf(0xFF00E5FF, 0xFF10B981)
)

data class ViriReaction(
    val speechMessage: String,
    val animationAction: String, // "JUMP", "CELEBRATE", "SHRUG", "DANCE"
    val isCelebration: Boolean = true
)

data class MissionCompletionResult(
    val mission: CreatorMission,
    val viriReaction: ViriReaction,
    val xpGained: Int,
    val newTotalXp: Int,
    val didLevelUp: Boolean,
    val newLevelTitle: String? = null
)

object CreatorMissionsEngine {
    private const val PREF_NAME = "creator_missions_prefs"
    private const val KEY_COMPLETED_MISSIONS = "completed_missions_ids"
    private const val KEY_SKIPPED_MISSIONS = "skipped_missions_ids"
    private const val KEY_MISSION_PROGRESS = "mission_progress_map"
    private const val KEY_TOTAL_XP = "total_xp_points"
    private const val KEY_STREAK_DAYS = "streak_days_count"
    private const val KEY_BEST_STREAK = "best_streak_count"
    private const val KEY_LAST_ACTIVE_DATE = "last_active_date_str"
    private const val KEY_UNLOCKED_ACHIEVEMENTS = "unlocked_achievements_ids"
    private const val KEY_ACTIVE_THEME = "active_theme_id"
    private const val KEY_ACTIVE_FRAME = "active_frame_id"
    private const val KEY_ACTIVE_ACCESSORY = "active_accessory_id"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getTodayDateStr(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    // Default Daily Missions List
    fun getDailyMissions(context: Context): List<CreatorMission> {
        val prefs = getPrefs(context)
        val completedSet = prefs.getStringSet(KEY_COMPLETED_MISSIONS, emptySet()) ?: emptySet()
        val skippedSet = prefs.getStringSet(KEY_SKIPPED_MISSIONS, emptySet()) ?: emptySet()

        val rawList = listOf(
            CreatorMission(
                id = "daily_1",
                title = "Upload 1 Reel",
                description = "Export or post 1 video to Instagram or YouTube Reels",
                category = MissionCategory.DAILY,
                xpReward = 50,
                progress = if (completedSet.contains("daily_1")) 1 else 0,
                total = 1,
                rewardType = MissionRewardType.XP,
                rewardTitle = "50 XP + Streak Ring",
                iconEmoji = "📹"
            ),
            CreatorMission(
                id = "daily_2",
                title = "Hook Score Above 85",
                description = "Analyse or generate a hook with retention prediction > 85%",
                category = MissionCategory.DAILY,
                xpReward = 75,
                progress = if (completedSet.contains("daily_2")) 1 else 0,
                total = 1,
                rewardType = MissionRewardType.BADGE,
                rewardTitle = "75 XP + Hook Badge",
                iconEmoji = "🪝"
            ),
            CreatorMission(
                id = "daily_3",
                title = "Face Visible for 80%",
                description = "Ensure high human engagement framing in AI Viral X-Ray",
                category = MissionCategory.DAILY,
                xpReward = 60,
                progress = if (completedSet.contains("daily_3")) 1 else 0,
                total = 1,
                rewardType = MissionRewardType.XP,
                rewardTitle = "60 XP",
                iconEmoji = "👤"
            ),
            CreatorMission(
                id = "daily_4",
                title = "Product Shown within 2 sec",
                description = "Instant hook payoff: showcase key product in first 2 seconds",
                category = MissionCategory.DAILY,
                xpReward = 80,
                progress = if (completedSet.contains("daily_4")) 1 else 0,
                total = 1,
                rewardType = MissionRewardType.ROBOT_ACCESSORY,
                rewardTitle = "80 XP + Cyber Visor",
                iconEmoji = "⚡"
            ),
            CreatorMission(
                id = "daily_5",
                title = "CTA Added",
                description = "Include clear Call-To-Action sticker, text, or spoken script",
                category = MissionCategory.DAILY,
                xpReward = 50,
                progress = if (completedSet.contains("daily_5")) 1 else 0,
                total = 1,
                rewardType = MissionRewardType.XP,
                rewardTitle = "50 XP",
                iconEmoji = "📣"
            )
        )

        return rawList.map { m ->
            m.copy(
                isCompleted = completedSet.contains(m.id),
                isSkipped = skippedSet.contains(m.id)
            )
        }
    }

    // Default Weekly Missions List
    fun getWeeklyMissions(context: Context): List<CreatorMission> {
        val prefs = getPrefs(context)
        val completedSet = prefs.getStringSet(KEY_COMPLETED_MISSIONS, emptySet()) ?: emptySet()
        val skippedSet = prefs.getStringSet(KEY_SKIPPED_MISSIONS, emptySet()) ?: emptySet()

        val rawList = listOf(
            CreatorMission(
                id = "weekly_1",
                title = "Analyse 15 Reels",
                description = "Run AI Reel Analysis on 15 total videos this week",
                category = MissionCategory.WEEKLY,
                xpReward = 250,
                progress = if (completedSet.contains("weekly_1")) 15 else 8,
                total = 15,
                rewardType = MissionRewardType.ROBOT_ACCESSORY,
                rewardTitle = "250 XP + Cyber Headphones",
                iconEmoji = "📊"
            ),
            CreatorMission(
                id = "weekly_2",
                title = "Improve Hook +10 Points",
                description = "Boost hook score from 70s to 80+ across 3 consecutive reels",
                category = MissionCategory.WEEKLY,
                xpReward = 300,
                progress = if (completedSet.contains("weekly_2")) 1 else 0,
                total = 1,
                rewardType = MissionRewardType.THEME,
                rewardTitle = "300 XP + Emerald Glow Theme",
                iconEmoji = "📈"
            ),
            CreatorMission(
                id = "weekly_3",
                title = "Get Upload Confidence > 90%",
                description = "Achieve top tier green light in AI Upload Simulator",
                category = MissionCategory.WEEKLY,
                xpReward = 400,
                progress = if (completedSet.contains("weekly_3")) 1 else 0,
                total = 1,
                rewardType = MissionRewardType.FRAME,
                rewardTitle = "400 XP + Neon Glow Frame",
                iconEmoji = "🚀"
            ),
            CreatorMission(
                id = "weekly_4",
                title = "Maintain 5 Day Streak",
                description = "Complete at least 1 mission daily for 5 days in a row",
                category = MissionCategory.WEEKLY,
                xpReward = 500,
                progress = if (completedSet.contains("weekly_4")) 5 else getStreakData(context).currentStreak.coerceAtMost(5),
                total = 5,
                rewardType = MissionRewardType.ROBOT_ACCESSORY,
                rewardTitle = "500 XP + Golden Antenna",
                iconEmoji = "🔥"
            )
        )

        return rawList.map { m ->
            m.copy(
                isCompleted = completedSet.contains(m.id) || m.progress >= m.total,
                isSkipped = skippedSet.contains(m.id)
            )
        }
    }

    // Default Monthly Missions List
    fun getMonthlyMissions(context: Context): List<CreatorMission> {
        val prefs = getPrefs(context)
        val completedSet = prefs.getStringSet(KEY_COMPLETED_MISSIONS, emptySet()) ?: emptySet()
        val skippedSet = prefs.getStringSet(KEY_SKIPPED_MISSIONS, emptySet()) ?: emptySet()

        val rawList = listOf(
            CreatorMission(
                id = "monthly_1",
                title = "30 Analysed Reels",
                description = "Build a robust analytics portfolio with 30 AI scanned reels",
                category = MissionCategory.MONTHLY,
                xpReward = 1000,
                progress = if (completedSet.contains("monthly_1")) 30 else 18,
                total = 30,
                rewardType = MissionRewardType.FRAME,
                rewardTitle = "1000 XP + Elite Creator Frame",
                iconEmoji = "🏆"
            ),
            CreatorMission(
                id = "monthly_2",
                title = "Unlock Elite Creator Badge",
                description = "Reach Level 3 Creator status and claim elite status",
                category = MissionCategory.MONTHLY,
                xpReward = 1200,
                progress = if (completedSet.contains("monthly_2")) 1 else 0,
                total = 1,
                rewardType = MissionRewardType.BADGE,
                rewardTitle = "1200 XP + Elite Creator Badge",
                iconEmoji = "👑"
            ),
            CreatorMission(
                id = "monthly_3",
                title = "Earn 1000 XP in 30 Days",
                description = "Gain 1000 total experience points through daily consistency",
                category = MissionCategory.MONTHLY,
                xpReward = 1000,
                progress = if (completedSet.contains("monthly_3")) 1000 else getCreatorLevelData(context).currentXp.coerceAtMost(1000),
                total = 1000,
                rewardType = MissionRewardType.THEME,
                rewardTitle = "1000 XP + Aura Gold Theme",
                iconEmoji = "✨"
            ),
            CreatorMission(
                id = "monthly_4",
                title = "Perfect Thumbnail 10 Times",
                description = "Generate 10 ultra-high CTR AI visual thumbnails",
                category = MissionCategory.MONTHLY,
                xpReward = 800,
                progress = if (completedSet.contains("monthly_4")) 10 else 6,
                total = 10,
                rewardType = MissionRewardType.ICON,
                rewardTitle = "800 XP + Thumbnail Genius Icon",
                iconEmoji = "🖼️"
            )
        )

        return rawList.map { m ->
            m.copy(
                isCompleted = completedSet.contains(m.id) || m.progress >= m.total,
                isSkipped = skippedSet.contains(m.id)
            )
        }
    }

    // Complete a mission
    fun completeMission(context: Context, missionId: String): MissionCompletionResult {
        val prefs = getPrefs(context)
        val completedSet = prefs.getStringSet(KEY_COMPLETED_MISSIONS, emptySet())?.toMutableSet() ?: mutableSetOf()
        val skippedSet = prefs.getStringSet(KEY_SKIPPED_MISSIONS, emptySet())?.toMutableSet() ?: mutableSetOf()

        completedSet.add(missionId)
        skippedSet.remove(missionId)
        prefs.edit().putStringSet(KEY_COMPLETED_MISSIONS, completedSet)
            .putStringSet(KEY_SKIPPED_MISSIONS, skippedSet).apply()

        // Find mission details
        val allMissions = getDailyMissions(context) + getWeeklyMissions(context) + getMonthlyMissions(context)
        val targetMission = allMissions.find { it.id == missionId }
            ?: CreatorMission(missionId, "Mission Completed", "Done", MissionCategory.DAILY, 50, 1, 1, true)

        val oldXp = prefs.getInt(KEY_TOTAL_XP, 420)
        val xpGain = targetMission.xpReward
        val newXp = oldXp + xpGain
        prefs.edit().putInt(KEY_TOTAL_XP, newXp).apply()

        val oldLevel = calculateLevel(oldXp)
        val newLevel = calculateLevel(newXp)
        val didLevelUp = newLevel.level > oldLevel.level

        // Update streak if completing a daily mission
        updateStreakActivity(context)

        val viriSpeech = if (didLevelUp) {
            "Level Up! You are now a ${newLevel.levelTitle}! 🎉🚀"
        } else {
            val celebratePhrases = listOf(
                "Mission Complete 🎉 You earned +${xpGain} XP!",
                "Awesome job, Creator! +${xpGain} XP locked in! 🔥",
                "Superb execution! Viri is super proud of you! ✨",
                "Consistency is key! Keep crushing these missions! 🚀"
            )
            celebratePhrases.random()
        }

        val reaction = ViriReaction(
            speechMessage = viriSpeech,
            animationAction = if (didLevelUp) "DANCE" else "JUMP",
            isCelebration = true
        )

        return MissionCompletionResult(
            mission = targetMission.copy(isCompleted = true, progress = targetMission.total),
            viriReaction = reaction,
            xpGained = xpGain,
            newTotalXp = newXp,
            didLevelUp = didLevelUp,
            newLevelTitle = if (didLevelUp) newLevel.levelTitle else null
        )
    }

    // Skip a mission
    fun skipMission(context: Context, missionId: String): ViriReaction {
        val prefs = getPrefs(context)
        val skippedSet = prefs.getStringSet(KEY_SKIPPED_MISSIONS, emptySet())?.toMutableSet() ?: mutableSetOf()
        skippedSet.add(missionId)
        prefs.edit().putStringSet(KEY_SKIPPED_MISSIONS, skippedSet).apply()

        return ViriReaction(
            speechMessage = "Aaj ka mission reh gaya 😅 Koi baat nahi, kal phodenge!",
            animationAction = "SHRUG",
            isCelebration = false
        )
    }

    // Streak handling
    fun getStreakData(context: Context): StreakData {
        val prefs = getPrefs(context)
        val currentStreak = prefs.getInt(KEY_STREAK_DAYS, 3)
        val bestStreak = prefs.getInt(KEY_BEST_STREAK, 7).coerceAtLeast(currentStreak)
        val lastDate = prefs.getString(KEY_LAST_ACTIVE_DATE, getTodayDateStr()) ?: getTodayDateStr()

        return StreakData(
            currentStreak = currentStreak,
            bestStreak = bestStreak,
            lastActiveDate = lastDate,
            milestones = listOf(1, 3, 7, 15, 30, 100)
        )
    }

    private fun updateStreakActivity(context: Context) {
        val prefs = getPrefs(context)
        val todayStr = getTodayDateStr()
        val lastDate = prefs.getString(KEY_LAST_ACTIVE_DATE, "")

        if (lastDate != todayStr) {
            val currentStreak = prefs.getInt(KEY_STREAK_DAYS, 3) + 1
            val bestStreak = prefs.getInt(KEY_BEST_STREAK, 7).coerceAtLeast(currentStreak)
            prefs.edit()
                .putInt(KEY_STREAK_DAYS, currentStreak)
                .putInt(KEY_BEST_STREAK, bestStreak)
                .putString(KEY_LAST_ACTIVE_DATE, todayStr)
                .apply()
        }
    }

    // Creator Level Data
    fun getCreatorLevelData(context: Context): CreatorLevelData {
        val prefs = getPrefs(context)
        val totalXp = prefs.getInt(KEY_TOTAL_XP, 420)
        return calculateLevel(totalXp)
    }

    private fun calculateLevel(totalXp: Int): CreatorLevelData {
        return when {
            totalXp < 300 -> CreatorLevelData(
                level = 1,
                levelTitle = "Novice Creator",
                currentXp = totalXp,
                xpForNextLevel = 300,
                progressFraction = (totalXp / 300f).coerceIn(0f, 1f),
                unlockedRewardsCount = 1
            )
            totalXp < 800 -> CreatorLevelData(
                level = 2,
                levelTitle = "Rising Star Creator",
                currentXp = totalXp,
                xpForNextLevel = 800,
                progressFraction = ((totalXp - 300) / 500f).coerceIn(0f, 1f),
                unlockedRewardsCount = 3
            )
            totalXp < 1800 -> CreatorLevelData(
                level = 3,
                levelTitle = "Viral Specialist",
                currentXp = totalXp,
                xpForNextLevel = 1800,
                progressFraction = ((totalXp - 800) / 1000f).coerceIn(0f, 1f),
                unlockedRewardsCount = 6
            )
            totalXp < 3500 -> CreatorLevelData(
                level = 4,
                levelTitle = "Content Architect",
                currentXp = totalXp,
                xpForNextLevel = 3500,
                progressFraction = ((totalXp - 1800) / 1700f).coerceIn(0f, 1f),
                unlockedRewardsCount = 9
            )
            else -> CreatorLevelData(
                level = 5,
                levelTitle = "Elite Creator Master",
                currentXp = totalXp,
                xpForNextLevel = 5000,
                progressFraction = ((totalXp - 3500) / 1500f).coerceIn(0f, 1f),
                unlockedRewardsCount = 12
            )
        }
    }

    // Achievements Catalog
    fun getAchievements(context: Context): List<CreatorMissionAchievement> {
        val prefs = getPrefs(context)
        val unlockedSet = prefs.getStringSet(KEY_UNLOCKED_ACHIEVEMENTS, setOf("ach_1", "ach_2")) ?: setOf("ach_1", "ach_2")

        val all = listOf(
            CreatorMissionAchievement("ach_1", "First Analysis", "Scanned your first reel with AI Viral X-Ray", "🔥", unlockedSet.contains("ach_1"), "Today", 100),
            CreatorMissionAchievement("ach_2", "10 Reels Milestone", "Analysed 10 reels in creator portfolio", "🔥", unlockedSet.contains("ach_2"), "3 days ago", 200),
            CreatorMissionAchievement("ach_3", "100 Reels Master", "Analysed 100 reels with AI perfection", "⚡", unlockedSet.contains("ach_3"), "", 500),
            CreatorMissionAchievement("ach_4", "First Elite Score", "Achieved a Viral Score of 90+ on a reel", "👑", unlockedSet.contains("ach_4"), "", 300),
            CreatorMissionAchievement("ach_5", "30 Day Streak", "Maintained a 30-day posting or analysis streak", "🚀", unlockedSet.contains("ach_5"), "", 1000),
            CreatorMissionAchievement("ach_6", "Thumbnail Master", "Generated 10 high-CTR AI thumbnails", "🖼️", unlockedSet.contains("ach_6"), "", 250),
            CreatorMissionAchievement("ach_7", "Hook Genius", "Crafted 10 hooks with an 85+ retention score", "🪝", unlockedSet.contains("ach_7"), "", 350)
        )
        return all
    }

    // Rewards Catalog
    fun getRewardsCatalog(context: Context): List<MissionRewardItem> {
        val levelData = getCreatorLevelData(context)
        val currentLevel = levelData.level

        return listOf(
            MissionRewardItem("r_1", "Cyber Visor", MissionRewardType.ROBOT_ACCESSORY, "Neon blue HUD display for Viri mascot", "🥽", 1, true, listOf(0xFF00E5FF, 0xFF00B0FF)),
            MissionRewardItem("r_2", "Emerald Glow Theme", MissionRewardType.THEME, "High contrast dark emerald green UI aesthetic", "🟢", 2, currentLevel >= 2, listOf(0xFF10B981, 0xFF059669)),
            MissionRewardItem("r_3", "Golden Antenna", MissionRewardType.ROBOT_ACCESSORY, "Glowing gold antenna for boost receptivity", "📡", 2, currentLevel >= 2, listOf(0xFFF59E0B, 0xFFD97706)),
            MissionRewardItem("r_4", "Neon Glow Frame", MissionRewardType.FRAME, "Animated cyan-magenta halo border around profile", "⭕", 3, currentLevel >= 3, listOf(0xFFEC4899, 0xFF8B5CF6)),
            MissionRewardItem("r_5", "Royal Crown", MissionRewardType.ROBOT_ACCESSORY, "Golden royal crown for Viri Mascot", "👑", 3, currentLevel >= 3, listOf(0xFFFBBF24, 0xFFB45309)),
            MissionRewardItem("r_6", "Aura Gold Theme", MissionRewardType.THEME, "Luxury amoled gold UI theme styling", "✨", 4, currentLevel >= 4, listOf(0xFFF59E0B, 0xFF78350F)),
            MissionRewardItem("r_7", "Cyberpunk Viri", MissionRewardType.ICON, "Futuristic cyber-enhanced Viri avatar icon", "🤖", 4, currentLevel >= 4, listOf(0xFF3B82F6, 0xFF1D4ED8)),
            MissionRewardItem("r_8", "Elite Creator Frame", MissionRewardType.FRAME, "Diamond holographic border frame", "💎", 5, currentLevel >= 5, listOf(0xFF06B6D4, 0xFF3B82F6))
        )
    }

    fun claimAchievement(context: Context, achievementId: String) {
        val prefs = getPrefs(context)
        val unlockedSet = prefs.getStringSet(KEY_UNLOCKED_ACHIEVEMENTS, emptySet())?.toMutableSet() ?: mutableSetOf()
        unlockedSet.add(achievementId)
        prefs.edit().putStringSet(KEY_UNLOCKED_ACHIEVEMENTS, unlockedSet).apply()

        val ach = getAchievements(context).find { it.id == achievementId }
        ach?.let {
            val oldXp = prefs.getInt(KEY_TOTAL_XP, 420)
            prefs.edit().putInt(KEY_TOTAL_XP, oldXp + it.rewardXp).apply()
        }
    }
}
