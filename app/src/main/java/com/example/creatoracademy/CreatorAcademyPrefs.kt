package com.example.creatoracademy

import android.content.Context

data class CreatorSetupData(
    val targetPlatform: String = "Instagram", // Instagram, YouTube, Both
    val skillLevel: String = "Beginner", // Beginner, Intermediate, Advanced
    val currentFollowers: String = "1k", // Custom input string e.g. "1.5k"
    val niche: String = "Tech", // Fashion, Beauty, Gaming, Education, Business, Shopping Reviews, Tech, Travel, Food, Fitness, Other
    val primaryGoal: String = "Followers", // Followers, Views, Brand Deals, Affiliate Income, Business, Personal Brand
    val postingFrequency: String = "Daily", // Daily, Weekly, Weekend, Custom
    val availableTime: String = "15 min", // 15 min, 30 min, 1 hour, Custom
    val preferredLanguage: String = "English" // English, HinEnglish, Hindi
)

object CreatorAcademyPrefs {
    private const val PREF_NAME = "creator_academy_prefs"
    
    private const val KEY_EXPERIENCE_CHOICE = "experience_choice" // "SHOPPING" or "CREATOR_ACADEMY"
    private const val KEY_REMEMBER_EXPERIENCE = "remember_experience"
    
    private const val KEY_SETUP_COMPLETED = "setup_completed"
    private const val KEY_TARGET_PLATFORM = "target_platform"
    private const val KEY_SKILL_LEVEL = "skill_level"
    private const val KEY_CURRENT_FOLLOWERS = "current_followers"
    private const val KEY_NICHE = "niche"
    private const val KEY_PRIMARY_GOAL = "primary_goal"
    private const val KEY_POSTING_FREQUENCY = "posting_frequency"
    private const val KEY_AVAILABLE_TIME = "available_time"
    private const val KEY_PREFERRED_LANGUAGE = "preferred_language"
    
    private const val KEY_XP_POINTS = "xp_points"
    private const val KEY_COMPLETED_TASKS = "completed_tasks"
    private const val KEY_SKIPPED_TASKS = "skipped_tasks"
    private const val KEY_CURRENT_TASK_INDEX = "current_task_index"
    private const val KEY_STREAK_DAYS = "streak_days"
    private const val KEY_REMINDER_DISMISSED = "reminder_dismissed"

    // Experience Selection
    fun getExperienceChoice(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_EXPERIENCE_CHOICE, "SHOPPING") ?: "SHOPPING"
    }

    fun setExperienceChoice(context: Context, choice: String, remember: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_EXPERIENCE_CHOICE, choice)
            .putBoolean(KEY_REMEMBER_EXPERIENCE, remember)
            .apply()
    }

    fun isRememberExperience(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_REMEMBER_EXPERIENCE, false)
    }

    // Setup Flow
    fun isSetupCompleted(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_SETUP_COMPLETED, false)
    }

    fun saveSetupData(context: Context, data: CreatorSetupData) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_SETUP_COMPLETED, true)
            .putString(KEY_TARGET_PLATFORM, data.targetPlatform)
            .putString(KEY_SKILL_LEVEL, data.skillLevel)
            .putString(KEY_CURRENT_FOLLOWERS, data.currentFollowers)
            .putString(KEY_NICHE, data.niche)
            .putString(KEY_PRIMARY_GOAL, data.primaryGoal)
            .putString(KEY_POSTING_FREQUENCY, data.postingFrequency)
            .putString(KEY_AVAILABLE_TIME, data.availableTime)
            .putString(KEY_PREFERRED_LANGUAGE, data.preferredLanguage)
            .apply()
    }

    fun getSetupData(context: Context): CreatorSetupData {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return CreatorSetupData(
            targetPlatform = prefs.getString(KEY_TARGET_PLATFORM, "Instagram") ?: "Instagram",
            skillLevel = prefs.getString(KEY_SKILL_LEVEL, "Beginner") ?: "Beginner",
            currentFollowers = prefs.getString(KEY_CURRENT_FOLLOWERS, "1k") ?: "1k",
            niche = prefs.getString(KEY_NICHE, "Tech") ?: "Tech",
            primaryGoal = prefs.getString(KEY_PRIMARY_GOAL, "Followers") ?: "Followers",
            postingFrequency = prefs.getString(KEY_POSTING_FREQUENCY, "Daily") ?: "Daily",
            availableTime = prefs.getString(KEY_AVAILABLE_TIME, "15 min") ?: "15 min",
            preferredLanguage = prefs.getString(KEY_PREFERRED_LANGUAGE, "English") ?: "English"
        )
    }

    // Gamification (XP & Tasks) — Path-Specific Data Isolation
    fun getXpPoints(context: Context, path: String = "INSTAGRAM"): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt("${KEY_XP_POINTS}_$path", if (path == "YOUTUBE") 180 else 250)
    }

    fun addXpPoints(context: Context, pts: Int, path: String = "INSTAGRAM"): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val newXp = getXpPoints(context, path) + pts
        prefs.edit().putInt("${KEY_XP_POINTS}_$path", newXp).apply()
        return newXp
    }

    fun getCurrentTaskIndex(context: Context, path: String = "INSTAGRAM"): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt("${KEY_CURRENT_TASK_INDEX}_$path", 0)
    }

    fun advanceTaskIndex(context: Context, path: String = "INSTAGRAM") {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val current = getCurrentTaskIndex(context, path)
        prefs.edit().putInt("${KEY_CURRENT_TASK_INDEX}_$path", current + 1).apply()
    }

    fun getCompletedTasks(context: Context, path: String = "INSTAGRAM"): Set<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet("${KEY_COMPLETED_TASKS}_$path", emptySet()) ?: emptySet()
    }

    fun markTaskCompleted(context: Context, taskId: String, path: String = "INSTAGRAM") {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val set = getCompletedTasks(context, path).toMutableSet()
        set.add(taskId)
        prefs.edit().putStringSet("${KEY_COMPLETED_TASKS}_$path", set).apply()
    }

    fun getSkippedTasks(context: Context, path: String = "INSTAGRAM"): Set<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet("${KEY_SKIPPED_TASKS}_$path", emptySet()) ?: emptySet()
    }

    fun markTaskSkipped(context: Context, taskId: String, path: String = "INSTAGRAM") {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val set = getSkippedTasks(context, path).toMutableSet()
        set.add(taskId)
        prefs.edit().putStringSet("${KEY_SKIPPED_TASKS}_$path", set).apply()
    }

    fun getStreakDays(context: Context, path: String = "INSTAGRAM"): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt("${KEY_STREAK_DAYS}_$path", if (path == "YOUTUBE") 3 else 5)
    }

    fun incrementStreak(context: Context, path: String = "INSTAGRAM"): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val current = getStreakDays(context, path)
        val next = current + 1
        prefs.edit().putInt("${KEY_STREAK_DAYS}_$path", next).apply()
        return next
    }

    fun isReminderDismissed(context: Context, path: String = "INSTAGRAM"): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("${KEY_REMINDER_DISMISSED}_$path", false)
    }

    fun setReminderDismissed(context: Context, path: String = "INSTAGRAM", dismissed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("${KEY_REMINDER_DISMISSED}_$path", dismissed).apply()
    }
    
    fun resetSetup(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_SETUP_COMPLETED, false).apply()
    }
}
