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
    private const val KEY_LANG_SELECTED = "academy_lang_selected"
    private const val KEY_BRAND_COLLAB_LANG = "brand_collab_language"
    private const val KEY_BRAND_COLLAB_STEP_INDEX = "brand_collab_step_index"
    private const val KEY_MEESHO_CREATOR_LANG = "meesho_creator_language"
    private const val KEY_MEESHO_CREATOR_STEP_INDEX = "meesho_creator_step_index"

    private const val KEY_WISHLINK_LANG = "wishlink_creator_language"
    private const val KEY_WISHLINK_STEP_INDEX = "wishlink_creator_step_index"
    private const val KEY_WISHLINK_COMPLETED_STEPS = "wishlink_creator_completed_steps"

    private const val KEY_YOUTUBE_LANG = "youtube_creator_v2_language"
    private const val KEY_YOUTUBE_CREATOR_TYPE = "youtube_creator_v2_type"
    private const val KEY_YOUTUBE_CURRENT_STEP = "youtube_creator_v2_current_step"
    private const val KEY_YOUTUBE_COMPLETED_STEPS = "youtube_creator_v2_completed_steps"

    // Video Editing Mentor Tools (CapCut, VN, Instagram Edits) Data Isolation
    fun getEditingToolLanguage(context: Context, toolKey: String): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString("video_editing_${toolKey}_lang", null)
    }

    fun saveEditingToolLanguage(context: Context, toolKey: String, lang: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString("video_editing_${toolKey}_lang", lang).apply()
    }

    fun getEditingToolVideoType(context: Context, toolKey: String): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString("video_editing_${toolKey}_video_type", null)
    }

    fun saveEditingToolVideoType(context: Context, toolKey: String, type: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString("video_editing_${toolKey}_video_type", type).apply()
    }

    fun getEditingToolCurrentStep(context: Context, toolKey: String): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt("video_editing_${toolKey}_current_step", 1)
    }

    fun saveEditingToolCurrentStep(context: Context, toolKey: String, step: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt("video_editing_${toolKey}_current_step", step).apply()
    }

    fun getEditingToolCompletedSteps(context: Context, toolKey: String): List<Int> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val strSet = prefs.getStringSet("video_editing_${toolKey}_completed_steps", emptySet()) ?: emptySet()
        return strSet.mapNotNull { it.toIntOrNull() }
    }

    fun saveEditingToolCompletedSteps(context: Context, toolKey: String, steps: Set<Int>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val strSet = steps.map { it.toString() }.toSet()
        prefs.edit().putStringSet("video_editing_${toolKey}_completed_steps", strSet).apply()
    }

    fun getYouTubeLanguage(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_LANG, null)
    }

    fun saveYouTubeLanguage(context: Context, lang: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_LANG, lang).apply()
    }

    fun getYouTubeCreatorType(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_CREATOR_TYPE, null)
    }

    fun saveYouTubeCreatorType(context: Context, type: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_CREATOR_TYPE, type).apply()
    }

    fun getYouTubeCurrentStep(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_YOUTUBE_CURRENT_STEP, 1)
    }

    fun saveYouTubeCurrentStep(context: Context, step: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_YOUTUBE_CURRENT_STEP, step).apply()
    }

    fun getYouTubeCompletedSteps(context: Context): List<Int> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val strSet = prefs.getStringSet(KEY_YOUTUBE_COMPLETED_STEPS, emptySet()) ?: emptySet()
        return strSet.mapNotNull { it.toIntOrNull() }
    }

    fun saveYouTubeCompletedSteps(context: Context, steps: Set<Int>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val strSet = steps.map { it.toString() }.toSet()
        prefs.edit().putStringSet(KEY_YOUTUBE_COMPLETED_STEPS, strSet).apply()
    }

    fun getBrandCollabLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_BRAND_COLLAB_LANG, "") ?: ""
    }

    fun setBrandCollabLanguage(context: Context, lang: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_BRAND_COLLAB_LANG, lang).apply()
    }

    fun getBrandCollabStepIndex(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_BRAND_COLLAB_STEP_INDEX, 0)
    }

    fun setBrandCollabStepIndex(context: Context, stepIndex: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_BRAND_COLLAB_STEP_INDEX, stepIndex).apply()
    }

    fun getMeeshoLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_MEESHO_CREATOR_LANG, "") ?: ""
    }

    fun setMeeshoLanguage(context: Context, lang: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_MEESHO_CREATOR_LANG, lang).apply()
    }

    fun getMeeshoStepIndex(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_MEESHO_CREATOR_STEP_INDEX, 0)
    }

    fun setMeeshoStepIndex(context: Context, stepIndex: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_MEESHO_CREATOR_STEP_INDEX, stepIndex).apply()
    }

    fun getWishlinkLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_WISHLINK_LANG, "") ?: ""
    }

    fun setWishlinkLanguage(context: Context, lang: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_WISHLINK_LANG, lang).apply()
    }

    fun getWishlinkStepIndex(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_WISHLINK_STEP_INDEX, 0)
    }

    fun setWishlinkStepIndex(context: Context, stepIndex: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_WISHLINK_STEP_INDEX, stepIndex).apply()
    }

    fun getWishlinkCompletedSteps(context: Context): List<Int> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val strSet = prefs.getStringSet(KEY_WISHLINK_COMPLETED_STEPS, emptySet()) ?: emptySet()
        return strSet.mapNotNull { it.toIntOrNull() }
    }

    fun saveWishlinkCompletedSteps(context: Context, steps: Set<Int>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val strSet = steps.map { it.toString() }.toSet()
        prefs.edit().putStringSet(KEY_WISHLINK_COMPLETED_STEPS, strSet).apply()
    }

    fun isLanguageSelected(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_LANG_SELECTED, false)
    }

    fun setLanguageSelected(context: Context, selected: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_LANG_SELECTED, selected).apply()
    }

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
        return prefs.getInt("${KEY_XP_POINTS}_$path", 0)
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
        return prefs.getInt("${KEY_STREAK_DAYS}_$path", 0)
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

    /**
     * Unified Reset Course Progress for any learning tool
     */
    fun resetCourseProgress(context: Context, courseKey: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        when (courseKey.lowercase()) {
            "youtube" -> {
                editor.remove(KEY_YOUTUBE_LANG)
                editor.remove(KEY_YOUTUBE_CREATOR_TYPE)
                editor.remove(KEY_YOUTUBE_CURRENT_STEP)
                editor.remove(KEY_YOUTUBE_COMPLETED_STEPS)
            }
            "instagram" -> {
                editor.remove("instagram_creator_language")
                editor.remove("instagram_creator_type")
                editor.remove("instagram_creator_current_step")
                editor.remove("instagram_creator_completed_steps")
                editor.remove(KEY_BRAND_COLLAB_STEP_INDEX)
            }
            "brand_collab" -> {
                editor.remove(KEY_BRAND_COLLAB_LANG)
                editor.remove(KEY_BRAND_COLLAB_STEP_INDEX)
            }
            "meesho" -> {
                editor.remove(KEY_MEESHO_CREATOR_LANG)
                editor.remove(KEY_MEESHO_CREATOR_STEP_INDEX)
            }
            else -> {
                editor.remove("video_editing_${courseKey}_lang")
                editor.remove("video_editing_${courseKey}_video_type")
                editor.remove("video_editing_${courseKey}_current_step")
                editor.remove("video_editing_${courseKey}_completed_steps")
                editor.remove("course_${courseKey}_lang")
                editor.remove("course_${courseKey}_type")
                editor.remove("course_${courseKey}_step")
                editor.remove("course_${courseKey}_completed")
            }
        }
        editor.apply()
    }
}
