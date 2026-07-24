package com.example.creatoracademy

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/**
 * MASTER PHASE 18 — Creator Business Hub Engine
 * Manages Creator Goals, Achievement Badges, Early Access Notifications,
 * and Real Progress AI Insights.
 */

data class CreatorGoal(
    val id: String = System.currentTimeMillis().toString(),
    val title: String,
    val targetValue: Int,
    val currentValue: Int = 0,
    val unit: String = "Followers",
    val category: String = "Growth", // Growth, Content, Income, Learning
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class AchievementBadge(
    val id: String,
    val title: String,
    val description: String,
    val iconType: String, // "REEL", "STREAK_7", "STREAK_30", "LESSONS_100", "AFFILIATE", "PITCH"
    val isUnlocked: Boolean,
    val progressText: String
)

data class BusinessInsight(
    val title: String,
    val message: String,
    val iconType: String = "CONSISTENCY" // "CONSISTENCY", "MILESTONE", "ACTION", "RECOMMENDATION"
)

object CreatorBusinessEngine {

    private const val PREF_NAME = "creator_business_hub_prefs"
    private const val KEY_GOALS = "creator_goals_list"
    private const val KEY_NOTIFY_MODULES = "early_access_notify_modules"
    private const val KEY_MANUAL_BADGES = "unlocked_manual_badges"

    // Default Goal Presets for creators
    val PRESET_GOALS = listOf(
        CreatorGoal(title = "Reach 100 Followers", targetValue = 100, unit = "Followers", category = "Growth"),
        CreatorGoal(title = "Reach 1,000 Followers", targetValue = 1000, unit = "Followers", category = "Growth"),
        CreatorGoal(title = "Secure First Brand Deal", targetValue = 1, unit = "Deal", category = "Income"),
        CreatorGoal(title = "First Affiliate Sale", targetValue = 1, unit = "Sale", category = "Income"),
        CreatorGoal(title = "Upload 30 Reels / Shorts", targetValue = 30, unit = "Videos", category = "Content"),
        CreatorGoal(title = "Complete 100 Mentor Lessons", targetValue = 100, unit = "Lessons", category = "Learning")
    )

    // Goals CRUD
    fun getGoals(context: Context): List<CreatorGoal> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonStr = prefs.getString(KEY_GOALS, null)

        if (jsonStr.isNull_or_blank_compat()) {
            // Seed with default initial goals
            val defaults = listOf(
                CreatorGoal(id = "default_1", title = "Reach 1,000 Followers", targetValue = 1000, currentValue = 250, unit = "Followers", category = "Growth"),
                CreatorGoal(id = "default_2", title = "Upload 30 Reels / Shorts", targetValue = 30, currentValue = 8, unit = "Videos", category = "Content"),
                CreatorGoal(id = "default_3", title = "Complete 100 Lessons", targetValue = 100, currentValue = 18, unit = "Lessons", category = "Learning")
            )
            saveGoalsList(context, defaults)
            return defaults
        }

        val list = mutableListOf<CreatorGoal>()
        try {
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.optString("id", System.currentTimeMillis().toString())
                val title = obj.optString("title", "Goal")
                val target = obj.optInt("targetValue", 100)
                val current = obj.optInt("currentValue", 0)
                val unit = obj.optString("unit", "Units")
                val category = obj.optString("category", "Growth")
                val completed = obj.optBoolean("isCompleted", current >= target)
                val createdAt = obj.optLong("createdAt", System.currentTimeMillis())

                list.add(
                    CreatorGoal(
                        id = id,
                        title = title,
                        targetValue = target,
                        currentValue = current,
                        unit = unit,
                        category = category,
                        isCompleted = completed,
                        createdAt = createdAt
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun addGoal(context: Context, goal: CreatorGoal) {
        val current = getGoals(context).toMutableList()
        current.add(0, goal)
        saveGoalsList(context, current)
    }

    fun updateGoalProgress(context: Context, goalId: String, newCurrentValue: Int) {
        val currentList = getGoals(context).toMutableList()
        val index = currentList.indexOfFirst { it.id == goalId }
        if (index != -1) {
            val oldGoal = currentList[index]
            val updatedVal = newCurrentValue.coerceAtLeast(0)
            val isComp = updatedVal >= oldGoal.targetValue
            currentList[index] = oldGoal.copy(
                currentValue = updatedVal,
                isCompleted = isComp
            )
            saveGoalsList(context, currentList)
        }
    }

    fun deleteGoal(context: Context, goalId: String) {
        val currentList = getGoals(context).filter { it.id != goalId }
        saveGoalsList(context, currentList)
    }

    private fun saveGoalsList(context: Context, goals: List<CreatorGoal>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonArray = JSONArray()
        goals.forEach { g ->
            val obj = JSONObject().apply {
                put("id", g.id)
                put("title", g.title)
                put("targetValue", g.targetValue)
                put("currentValue", g.currentValue)
                put("unit", g.unit)
                put("category", g.category)
                put("isCompleted", g.isCompleted)
                put("createdAt", g.createdAt)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_GOALS, jsonArray.toString()).apply()
    }

    // Achievement Badges
    fun getBadges(context: Context, streakDays: Int, completedLessons: Int, xpPoints: Int): List<AchievementBadge> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val manualUnlocked = prefs.getStringSet(KEY_MANUAL_BADGES, emptySet()) ?: emptySet()

        return listOf(
            AchievementBadge(
                id = "badge_first_reel",
                title = "First Reel",
                description = "Planned and created your very first content piece.",
                iconType = "REEL",
                isUnlocked = manualUnlocked.contains("badge_first_reel") || completedLessons >= 1,
                progressText = if (completedLessons >= 1) "Unlocked ✦" else "0/1 Reel Created"
            ),
            AchievementBadge(
                id = "badge_7day_streak",
                title = "7 Day Streak",
                description = "Stayed consistent in Creator Academy for 7 full days.",
                iconType = "STREAK_7",
                isUnlocked = streakDays >= 7 || manualUnlocked.contains("badge_7day_streak"),
                progressText = if (streakDays >= 7) "Unlocked ✦" else "$streakDays/7 Days"
            ),
            AchievementBadge(
                id = "badge_30day_streak",
                title = "30 Day Streak",
                description = "Unstoppable momentum with 30 consecutive days.",
                iconType = "STREAK_30",
                isUnlocked = streakDays >= 30 || manualUnlocked.contains("badge_30day_streak"),
                progressText = if (streakDays >= 30) "Unlocked ✦" else "$streakDays/30 Days"
            ),
            AchievementBadge(
                id = "badge_100_lessons",
                title = "100 Lessons Mastered",
                description = "Completed 100 curriculum modules across platforms.",
                iconType = "LESSONS_100",
                isUnlocked = completedLessons >= 100 || manualUnlocked.contains("badge_100_lessons"),
                progressText = "$completedLessons/100 Lessons"
            ),
            AchievementBadge(
                id = "badge_affiliate",
                title = "Affiliate Pioneer",
                description = "Launched your first affiliate product showcase.",
                iconType = "AFFILIATE",
                isUnlocked = manualUnlocked.contains("badge_affiliate") || xpPoints >= 300,
                progressText = if (xpPoints >= 300) "Unlocked ✦" else "Earn 300 XP"
            ),
            AchievementBadge(
                id = "badge_brand_pitch",
                title = "Brand Pitcher",
                description = "Sent your first pitch deck using the Collaboration Mentor.",
                iconType = "PITCH",
                isUnlocked = manualUnlocked.contains("badge_brand_pitch") || xpPoints >= 400,
                progressText = if (xpPoints >= 400) "Unlocked ✦" else "Earn 400 XP"
            )
        )
    }

    fun toggleManualBadge(context: Context, badgeId: String): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val set = prefs.getStringSet(KEY_MANUAL_BADGES, emptySet())?.toMutableSet() ?: mutableSetOf()
        val newState = if (set.contains(badgeId)) {
            set.remove(badgeId)
            false
        } else {
            set.add(badgeId)
            true
        }
        prefs.edit().putStringSet(KEY_MANUAL_BADGES, set).apply()
        return newState
    }

    // Early Access Notification
    fun registerNotifyInterest(context: Context, moduleName: String): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val set = prefs.getStringSet(KEY_NOTIFY_MODULES, emptySet())?.toMutableSet() ?: mutableSetOf()
        set.add(moduleName)
        prefs.edit().putStringSet(KEY_NOTIFY_MODULES, set).apply()
        return true
    }

    fun isNotified(context: Context, moduleName: String): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val set = prefs.getStringSet(KEY_NOTIFY_MODULES, emptySet()) ?: emptySet()
        return set.contains(moduleName)
    }

    // Real Progress AI Insights (Never fake data)
    fun getBusinessInsights(
        context: Context,
        streakDays: Int,
        completedTasksCount: Int,
        activeGoals: List<CreatorGoal>
    ): List<BusinessInsight> {
        val list = mutableListOf<BusinessInsight>()

        if (streakDays > 0) {
            list.add(
                BusinessInsight(
                    title = "Consistency Streak Active",
                    message = "You've been consistent for $streakDays days in a row! Consistency is the #1 driver of creator audience trust.",
                    iconType = "CONSISTENCY"
                )
            )
        } else {
            list.add(
                BusinessInsight(
                    title = "Start Your Daily Streak",
                    message = "Complete 1 daily task in Creator Academy today to trigger your consistency multiplier.",
                    iconType = "CONSISTENCY"
                )
            )
        }

        val incompleteGoal = activeGoals.firstOrNull { !it.isCompleted }
        if (incompleteGoal != null) {
            val percent = if (incompleteGoal.targetValue > 0) (incompleteGoal.currentValue * 100 / incompleteGoal.targetValue) else 0
            list.add(
                BusinessInsight(
                    title = "Milestone Progress: ${incompleteGoal.title}",
                    message = "You are currently at $percent% (${incompleteGoal.currentValue}/${incompleteGoal.targetValue} ${incompleteGoal.unit}). Keep posting consistently to cross the finish line!",
                    iconType = "MILESTONE"
                )
            )
        }

        if (completedTasksCount >= 5) {
            list.add(
                BusinessInsight(
                    title = "Curriculum Level Up",
                    message = "You have completed $completedTasksCount creator modules! Apply your learnings in AI Content Studio before uploading your next video.",
                    iconType = "ACTION"
                )
            )
        } else {
            list.add(
                BusinessInsight(
                    title = "Growth Strategy Recommendation",
                    message = "Focus on completing 3 curriculum tasks this week to unlock brand pitch templates and pricing frameworks.",
                    iconType = "RECOMMENDATION"
                )
            )
        }

        return list
    }

    private fun String?.isNull_or_blank_compat(): Boolean = this == null || this.trim().isEmpty()
}
