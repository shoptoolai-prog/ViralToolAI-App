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
    private const val KEY_BRAND_COLLAB_CREATOR_TYPE = "brand_collab_creator_type"
    private const val KEY_BRAND_COLLAB_ONBOARDING_DONE = "brand_collab_onboarding_done"
    
    // Brand Collab Phase 2 Profile Analysis Preferences
    private const val KEY_BRAND_COLLAB_PROFILE_COMPLETED = "brand_collab_profile_completed"
    private const val KEY_BRAND_COLLAB_PROFILE_PLATFORM = "brand_collab_profile_platform"
    private const val KEY_BRAND_COLLAB_PROFILE_FOLLOWERS = "brand_collab_profile_followers"
    private const val KEY_BRAND_COLLAB_PROFILE_NICHE = "brand_collab_profile_niche"
    private const val KEY_BRAND_COLLAB_PROFILE_LEVEL = "brand_collab_profile_level"
    private const val KEY_BRAND_COLLAB_PROFILE_GOAL = "brand_collab_profile_goal"
    private const val KEY_BRAND_COLLAB_PROFILE_PROBLEM = "brand_collab_profile_problem"
    private const val KEY_BRAND_COLLAB_PROFILE_LESSON = "brand_collab_profile_lesson"
    private const val KEY_BRAND_COLLAB_PROFILE_PROGRESS = "brand_collab_profile_progress"

    // Brand Collab Phase 3 Level 2 Become Brand Ready Preferences
    private const val KEY_BRAND_COLLAB_LEVEL2_COMPLETED = "brand_collab_level2_completed"
    private const val KEY_BRAND_COLLAB_LEVEL2_STEP = "brand_collab_level2_step"
    private const val KEY_BRAND_COLLAB_LEVEL2_PROFILE_LINK = "brand_collab_level2_profile_link"
    private const val KEY_BRAND_COLLAB_LEVEL2_SELECTED_USERNAME = "brand_collab_level2_selected_username"
    private const val KEY_BRAND_COLLAB_LEVEL2_SELECTED_BIO = "brand_collab_level2_selected_bio"
    private const val KEY_BRAND_COLLAB_LEVEL2_CHECKLIST = "brand_collab_level2_checklist"
    private const val KEY_MEESHO_CREATOR_LANG = "meesho_creator_language"
    private const val KEY_MEESHO_CREATOR_STEP_INDEX = "meesho_creator_step_index"

    private const val KEY_WISHLINK_LANG = "wishlink_creator_language"
    private const val KEY_WISHLINK_STEP_INDEX = "wishlink_creator_step_index"
    private const val KEY_WISHLINK_COMPLETED_STEPS = "wishlink_creator_completed_steps"

    private const val KEY_INSTAGRAM_INTRO_COMPLETED = "instagram_creator_intro_completed"
    private const val KEY_INSTAGRAM_LANG = "instagram_creator_language"
    private const val KEY_INSTAGRAM_CATEGORY = "instagram_creator_category"
    private const val KEY_INSTAGRAM_EXP = "instagram_creator_exp"
    private const val KEY_INSTAGRAM_FOLLOWERS = "instagram_creator_followers"
    private const val KEY_INSTAGRAM_GOAL = "instagram_creator_goal"
    private const val KEY_INSTAGRAM_DAILY_TIME = "instagram_creator_daily_time"
    private const val KEY_INSTAGRAM_ROADMAP_JSON = "instagram_creator_roadmap_json"
    private const val KEY_INSTAGRAM_CURRENT_STEP = "instagram_creator_current_step"
    private const val KEY_INSTAGRAM_COMPLETED_STEPS = "instagram_creator_completed_steps"

    private const val KEY_YOUTUBE_INTRO_COMPLETED = "youtube_creator_v2_intro_completed"
    private const val KEY_YOUTUBE_LEVEL0_COMPLETED = "youtube_creator_v2_level0_completed"
    private const val KEY_YOUTUBE_LEVEL1_COMPLETED = "youtube_creator_v2_level1_completed"
    private const val KEY_YOUTUBE_LEVEL2_COMPLETED = "youtube_creator_v2_level2_completed"
    private const val KEY_YOUTUBE_LEVEL3_COMPLETED = "youtube_creator_v2_level3_completed"
    private const val KEY_YOUTUBE_LEVEL4_COMPLETED = "youtube_creator_v2_level4_completed"
    private const val KEY_YOUTUBE_LEVEL5_COMPLETED = "youtube_creator_v2_level5_completed"
    private const val KEY_YOUTUBE_LEVEL6_COMPLETED = "youtube_creator_v2_level6_completed"
    private const val KEY_YOUTUBE_FINAL_COMPLETED = "youtube_creator_v2_final_completed"
    private const val KEY_YOUTUBE_GROWTH_GOAL = "youtube_creator_v2_growth_goal"
    private const val KEY_YOUTUBE_SUBS_RANGE = "youtube_creator_v2_subs_range"
    private const val KEY_YOUTUBE_WATCH_HOURS_RANGE = "youtube_creator_v2_watch_hours_range"
    private const val KEY_YOUTUBE_SHORTS_VIEWS_RANGE = "youtube_creator_v2_shorts_views_range"
    private const val KEY_YOUTUBE_NICHE = "youtube_creator_v2_niche"
    private const val KEY_YOUTUBE_NICHE_EXP = "youtube_creator_v2_niche_exp"
    private const val KEY_YOUTUBE_CONTENT_STYLE = "youtube_creator_v2_content_style"
    private const val KEY_YOUTUBE_CONTENT_PILLARS = "youtube_creator_v2_content_pillars"
    private const val KEY_YOUTUBE_HAS_CHANNEL = "youtube_creator_v2_has_channel"
    private const val KEY_YOUTUBE_CHANNEL_NAME = "youtube_creator_v2_channel_name"
    private const val KEY_YOUTUBE_CURRENT_LEVEL = "youtube_creator_v2_current_level"
    private const val KEY_YOUTUBE_MAIN_GOAL = "youtube_creator_v2_main_goal"
    private const val KEY_YOUTUBE_VIDEO_TYPE = "youtube_creator_v2_video_type"
    private const val KEY_YOUTUBE_WEEKLY_TIME = "youtube_creator_v2_weekly_time"
    private const val KEY_YOUTUBE_EDITING_EXP = "youtube_creator_v2_editing_exp"
    private const val KEY_YOUTUBE_RECORDING_SETUP = "youtube_creator_v2_recording_setup"
    private const val KEY_YOUTUBE_VIDEO_LANG = "youtube_creator_v2_video_lang"
    private const val KEY_YOUTUBE_BIGGEST_PROBLEM = "youtube_creator_v2_biggest_problem"
    private const val KEY_YOUTUBE_LANG = "youtube_creator_v2_language"
    private const val KEY_YOUTUBE_CREATOR_TYPE = "youtube_creator_v2_type"
    private const val KEY_YOUTUBE_CURRENT_STEP = "youtube_creator_v2_current_step"
    private const val KEY_YOUTUBE_COMPLETED_STEPS = "youtube_creator_v2_completed_steps"
    private const val KEY_YOUTUBE_CURRENT_LESSON = "youtube_creator_v2_current_lesson"
    private const val KEY_YOUTUBE_CURRENT_MISSION = "youtube_creator_v2_current_mission"
    private const val KEY_YOUTUBE_LAST_CONVERSATION = "youtube_creator_v2_last_conversation"

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

    fun isYouTubeIntroCompleted(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_YOUTUBE_INTRO_COMPLETED, false)
    }

    fun setYouTubeIntroCompleted(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_YOUTUBE_INTRO_COMPLETED, completed).apply()
    }

    fun isYouTubeLevel0Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_YOUTUBE_LEVEL0_COMPLETED, false)
    }

    fun setYouTubeLevel0Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_YOUTUBE_LEVEL0_COMPLETED, completed).apply()
    }

    fun isYouTubeLevel1Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_YOUTUBE_LEVEL1_COMPLETED, false)
    }

    fun setYouTubeLevel1Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_YOUTUBE_LEVEL1_COMPLETED, completed).apply()
    }

    fun isYouTubeLevel2Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_YOUTUBE_LEVEL2_COMPLETED, false)
    }

    fun setYouTubeLevel2Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_YOUTUBE_LEVEL2_COMPLETED, completed).apply()
    }

    fun isYouTubeLevel3Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_YOUTUBE_LEVEL3_COMPLETED, false)
    }

    fun setYouTubeLevel3Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_YOUTUBE_LEVEL3_COMPLETED, completed).apply()
    }

    fun isYouTubeLevel4Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_YOUTUBE_LEVEL4_COMPLETED, false)
    }

    fun setYouTubeLevel4Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_YOUTUBE_LEVEL4_COMPLETED, completed).apply()
    }

    fun isYouTubeLevel5Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_YOUTUBE_LEVEL5_COMPLETED, false)
    }

    fun setYouTubeLevel5Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_YOUTUBE_LEVEL5_COMPLETED, completed).apply()
    }

    fun isYouTubeLevel6Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_YOUTUBE_LEVEL6_COMPLETED, false)
    }

    fun setYouTubeLevel6Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_YOUTUBE_LEVEL6_COMPLETED, completed).apply()
    }

    fun isYouTubeFinalCompleted(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_YOUTUBE_FINAL_COMPLETED, false)
    }

    fun setYouTubeFinalCompleted(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_YOUTUBE_FINAL_COMPLETED, completed).apply()
    }

    fun resetAllYouTubeData(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_YOUTUBE_LEVEL0_COMPLETED, false)
            .putBoolean(KEY_YOUTUBE_LEVEL1_COMPLETED, false)
            .putBoolean(KEY_YOUTUBE_LEVEL2_COMPLETED, false)
            .putBoolean(KEY_YOUTUBE_LEVEL3_COMPLETED, false)
            .putBoolean(KEY_YOUTUBE_LEVEL4_COMPLETED, false)
            .putBoolean(KEY_YOUTUBE_LEVEL5_COMPLETED, false)
            .putBoolean(KEY_YOUTUBE_LEVEL6_COMPLETED, false)
            .putBoolean(KEY_YOUTUBE_FINAL_COMPLETED, false)
            .apply()
    }

    fun getYouTubeGrowthGoal(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_GROWTH_GOAL, "1000 Subscribers") ?: "1000 Subscribers"
    }

    fun saveYouTubeGrowthGoal(context: Context, goal: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_GROWTH_GOAL, goal).apply()
    }

    fun getYouTubeSubsRange(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_SUBS_RANGE, "0–100") ?: "0–100"
    }

    fun saveYouTubeSubsRange(context: Context, range: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_SUBS_RANGE, range).apply()
    }

    fun getYouTubeWatchHoursRange(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_WATCH_HOURS_RANGE, "0–500") ?: "0–500"
    }

    fun saveYouTubeWatchHoursRange(context: Context, range: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_WATCH_HOURS_RANGE, range).apply()
    }

    fun getYouTubeNiche(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_NICHE, null)
    }

    fun saveYouTubeNiche(context: Context, niche: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_NICHE, niche).apply()
    }

    fun getYouTubeNicheExp(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_NICHE_EXP, null)
    }

    fun saveYouTubeNicheExp(context: Context, exp: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_NICHE_EXP, exp).apply()
    }

    fun getYouTubeContentStyle(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_CONTENT_STYLE, null)
    }

    fun saveYouTubeContentStyle(context: Context, style: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_CONTENT_STYLE, style).apply()
    }

    fun getYouTubeHasChannel(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_HAS_CHANNEL, null)
    }

    fun saveYouTubeHasChannel(context: Context, hasChannel: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_HAS_CHANNEL, hasChannel).apply()
    }

    fun getYouTubeChannelName(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_CHANNEL_NAME, null)
    }

    fun saveYouTubeChannelName(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_CHANNEL_NAME, name).apply()
    }

    fun getYouTubeCurrentLevel(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_CURRENT_LEVEL, null)
    }

    fun saveYouTubeCurrentLevel(context: Context, level: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_CURRENT_LEVEL, level).apply()
    }

    fun getYouTubeMainGoal(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_MAIN_GOAL, null)
    }

    fun saveYouTubeMainGoal(context: Context, goal: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_MAIN_GOAL, goal).apply()
    }

    fun getYouTubeVideoType(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_VIDEO_TYPE, null)
    }

    fun saveYouTubeVideoType(context: Context, type: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_VIDEO_TYPE, type).apply()
    }

    fun getYouTubeWeeklyTime(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_WEEKLY_TIME, null)
    }

    fun saveYouTubeWeeklyTime(context: Context, time: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_WEEKLY_TIME, time).apply()
    }

    fun getYouTubeEditingExp(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_EDITING_EXP, null)
    }

    fun saveYouTubeEditingExp(context: Context, exp: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_EDITING_EXP, exp).apply()
    }

    fun getYouTubeRecordingSetup(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_RECORDING_SETUP, null)
    }

    fun saveYouTubeRecordingSetup(context: Context, setup: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_RECORDING_SETUP, setup).apply()
    }

    fun getYouTubeVideoLanguage(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_VIDEO_LANG, null)
    }

    fun saveYouTubeVideoLanguage(context: Context, lang: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_VIDEO_LANG, lang).apply()
    }

    fun getYouTubeBiggestProblem(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_BIGGEST_PROBLEM, null)
    }

    fun saveYouTubeBiggestProblem(context: Context, problem: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_BIGGEST_PROBLEM, problem).apply()
    }

    fun getYouTubeCurrentLesson(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_YOUTUBE_CURRENT_LESSON, 1)
    }

    fun saveYouTubeCurrentLesson(context: Context, lesson: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_YOUTUBE_CURRENT_LESSON, lesson).apply()
    }

    fun getYouTubeCurrentMission(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_CURRENT_MISSION, "Set up your Channel") ?: "Set up your Channel"
    }

    fun saveYouTubeCurrentMission(context: Context, mission: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_CURRENT_MISSION, mission).apply()
    }

    fun getYouTubeLastConversation(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_YOUTUBE_LAST_CONVERSATION, "") ?: ""
    }

    fun saveYouTubeLastConversation(context: Context, chatJson: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_YOUTUBE_LAST_CONVERSATION, chatJson).apply()
    }

    // Instagram Creator Guide Preferences
    fun isInstagramIntroCompleted(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_INSTAGRAM_INTRO_COMPLETED, false)
    }

    fun setInstagramIntroCompleted(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_INSTAGRAM_INTRO_COMPLETED, completed).apply()
    }

    fun getInstagramLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_INSTAGRAM_LANG, "") ?: ""
    }

    fun saveInstagramLanguage(context: Context, lang: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_INSTAGRAM_LANG, lang).apply()
    }

    fun getInstagramCategory(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_INSTAGRAM_CATEGORY, "") ?: ""
    }

    fun saveInstagramCategory(context: Context, category: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_INSTAGRAM_CATEGORY, category).apply()
    }

    fun getInstagramCurrentStep(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_INSTAGRAM_CURRENT_STEP, 0)
    }

    fun saveInstagramCurrentStep(context: Context, step: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_INSTAGRAM_CURRENT_STEP, step).apply()
    }

    fun getInstagramCompletedSteps(context: Context): List<Int> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val strSet = prefs.getStringSet(KEY_INSTAGRAM_COMPLETED_STEPS, emptySet()) ?: emptySet()
        return strSet.mapNotNull { it.toIntOrNull() }
    }

    fun saveInstagramCompletedSteps(context: Context, steps: Set<Int>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val strSet = steps.map { it.toString() }.toSet()
        prefs.edit().putStringSet(KEY_INSTAGRAM_COMPLETED_STEPS, strSet).apply()
    }

    fun getInstagramExperience(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_INSTAGRAM_EXP, "") ?: ""
    }

    fun saveInstagramExperience(context: Context, exp: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_INSTAGRAM_EXP, exp).apply()
    }

    fun getInstagramFollowers(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_INSTAGRAM_FOLLOWERS, "") ?: ""
    }

    fun saveInstagramFollowers(context: Context, followers: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_INSTAGRAM_FOLLOWERS, followers).apply()
    }

    fun getInstagramGoal(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_INSTAGRAM_GOAL, "") ?: ""
    }

    fun saveInstagramGoal(context: Context, goal: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_INSTAGRAM_GOAL, goal).apply()
    }

    fun getInstagramDailyTime(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_INSTAGRAM_DAILY_TIME, "") ?: ""
    }

    fun saveInstagramDailyTime(context: Context, time: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_INSTAGRAM_DAILY_TIME, time).apply()
    }

    fun getInstagramRoadmapJson(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_INSTAGRAM_ROADMAP_JSON, "") ?: ""
    }

    fun saveInstagramRoadmapJson(context: Context, json: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_INSTAGRAM_ROADMAP_JSON, json).apply()
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

    fun getBrandCollabCreatorType(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_BRAND_COLLAB_CREATOR_TYPE, "Content Creator") ?: "Content Creator"
    }

    fun setBrandCollabCreatorType(context: Context, type: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_BRAND_COLLAB_CREATOR_TYPE, type).apply()
    }

    fun isBrandCollabOnboardingDone(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BRAND_COLLAB_ONBOARDING_DONE, false)
    }

    fun setBrandCollabOnboardingDone(context: Context, done: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_BRAND_COLLAB_ONBOARDING_DONE, done).apply()
    }

    // Phase 2 Profile Data
    fun isBrandCollabProfileCompleted(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BRAND_COLLAB_PROFILE_COMPLETED, false)
    }

    fun saveBrandCollabProfile(
        context: Context,
        platform: String,
        followers: String,
        niche: String,
        level: String,
        goal: String,
        problem: String,
        lesson: String = "Creator Profile Analysis",
        progress: Int = 5
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_BRAND_COLLAB_PROFILE_COMPLETED, true)
            .putString(KEY_BRAND_COLLAB_PROFILE_PLATFORM, platform)
            .putString(KEY_BRAND_COLLAB_PROFILE_FOLLOWERS, followers)
            .putString(KEY_BRAND_COLLAB_PROFILE_NICHE, niche)
            .putString(KEY_BRAND_COLLAB_PROFILE_LEVEL, level)
            .putString(KEY_BRAND_COLLAB_PROFILE_GOAL, goal)
            .putString(KEY_BRAND_COLLAB_PROFILE_PROBLEM, problem)
            .putString(KEY_BRAND_COLLAB_PROFILE_LESSON, lesson)
            .putInt(KEY_BRAND_COLLAB_PROFILE_PROGRESS, progress)
            .apply()
    }

    fun getBrandCollabProfileData(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "completed" to prefs.getBoolean(KEY_BRAND_COLLAB_PROFILE_COMPLETED, false),
            "platform" to (prefs.getString(KEY_BRAND_COLLAB_PROFILE_PLATFORM, "Instagram") ?: "Instagram"),
            "followers" to (prefs.getString(KEY_BRAND_COLLAB_PROFILE_FOLLOWERS, "2K–10K") ?: "2K–10K"),
            "niche" to (prefs.getString(KEY_BRAND_COLLAB_PROFILE_NICHE, "Fashion") ?: "Fashion"),
            "level" to (prefs.getString(KEY_BRAND_COLLAB_PROFILE_LEVEL, "Beginner") ?: "Beginner"),
            "goal" to (prefs.getString(KEY_BRAND_COLLAB_PROFILE_GOAL, "Get My First Brand Deal") ?: "Get My First Brand Deal"),
            "problem" to (prefs.getString(KEY_BRAND_COLLAB_PROFILE_PROBLEM, "No Brand Replies") ?: "No Brand Replies"),
            "lesson" to (prefs.getString(KEY_BRAND_COLLAB_PROFILE_LESSON, "Creator Profile Analysis") ?: "Creator Profile Analysis"),
            "progress" to prefs.getInt(KEY_BRAND_COLLAB_PROFILE_PROGRESS, 5)
        )
    }

    // Phase 3 Level 2 Preference Helpers
    fun isBrandCollabLevel2Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL2_COMPLETED, false)
    }

    fun saveBrandCollabLevel2State(
        context: Context,
        step: Int,
        profileLink: String,
        selectedUsername: String,
        selectedBio: String,
        checklistCsv: String,
        isCompleted: Boolean = false
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_BRAND_COLLAB_LEVEL2_STEP, step)
            .putString(KEY_BRAND_COLLAB_LEVEL2_PROFILE_LINK, profileLink)
            .putString(KEY_BRAND_COLLAB_LEVEL2_SELECTED_USERNAME, selectedUsername)
            .putString(KEY_BRAND_COLLAB_LEVEL2_SELECTED_BIO, selectedBio)
            .putString(KEY_BRAND_COLLAB_LEVEL2_CHECKLIST, checklistCsv)
            .putBoolean(KEY_BRAND_COLLAB_LEVEL2_COMPLETED, isCompleted)
            .apply()
    }

    fun getBrandCollabLevel2Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "completed" to prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL2_COMPLETED, false),
            "step" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL2_STEP, 1),
            "profile_link" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL2_PROFILE_LINK, "") ?: ""),
            "username" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL2_SELECTED_USERNAME, "") ?: ""),
            "bio" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL2_SELECTED_BIO, "") ?: ""),
            "checklist" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL2_CHECKLIST, "") ?: "")
        )
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

    fun getPreferredLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_PREFERRED_LANGUAGE, "English") ?: "English"
    }

    fun setPreferredLanguage(context: Context, lang: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_PREFERRED_LANGUAGE, lang).apply()
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
                editor.remove(KEY_YOUTUBE_INTRO_COMPLETED)
                editor.remove(KEY_YOUTUBE_LEVEL0_COMPLETED)
                editor.remove(KEY_YOUTUBE_HAS_CHANNEL)
                editor.remove(KEY_YOUTUBE_CHANNEL_NAME)
                editor.remove(KEY_YOUTUBE_CURRENT_LEVEL)
                editor.remove(KEY_YOUTUBE_MAIN_GOAL)
                editor.remove(KEY_YOUTUBE_VIDEO_TYPE)
                editor.remove(KEY_YOUTUBE_WEEKLY_TIME)
                editor.remove(KEY_YOUTUBE_EDITING_EXP)
                editor.remove(KEY_YOUTUBE_RECORDING_SETUP)
                editor.remove(KEY_YOUTUBE_VIDEO_LANG)
                editor.remove(KEY_YOUTUBE_BIGGEST_PROBLEM)
                editor.remove(KEY_YOUTUBE_LANG)
                editor.remove(KEY_YOUTUBE_CREATOR_TYPE)
                editor.remove(KEY_YOUTUBE_CURRENT_STEP)
                editor.remove(KEY_YOUTUBE_COMPLETED_STEPS)
                editor.remove(KEY_YOUTUBE_CURRENT_LESSON)
                editor.remove(KEY_YOUTUBE_CURRENT_MISSION)
                editor.remove(KEY_YOUTUBE_LAST_CONVERSATION)
            }
            "instagram" -> {
                editor.remove(KEY_INSTAGRAM_INTRO_COMPLETED)
                editor.remove(KEY_INSTAGRAM_LANG)
                editor.remove(KEY_INSTAGRAM_CATEGORY)
                editor.remove(KEY_INSTAGRAM_EXP)
                editor.remove(KEY_INSTAGRAM_FOLLOWERS)
                editor.remove(KEY_INSTAGRAM_GOAL)
                editor.remove(KEY_INSTAGRAM_DAILY_TIME)
                editor.remove(KEY_INSTAGRAM_ROADMAP_JSON)
                editor.remove("instagram_creator_language")
                editor.remove("instagram_creator_type")
                editor.remove(KEY_INSTAGRAM_CURRENT_STEP)
                editor.remove("instagram_creator_current_step")
                editor.remove(KEY_INSTAGRAM_COMPLETED_STEPS)
                editor.remove("instagram_creator_completed_steps")
                editor.remove(KEY_BRAND_COLLAB_STEP_INDEX)
            }
            "brand_collab" -> {
                editor.remove(KEY_BRAND_COLLAB_LANG)
                editor.remove(KEY_BRAND_COLLAB_STEP_INDEX)
                editor.remove(KEY_BRAND_COLLAB_CREATOR_TYPE)
                editor.remove(KEY_BRAND_COLLAB_ONBOARDING_DONE)
                editor.remove(KEY_BRAND_COLLAB_PROFILE_COMPLETED)
                editor.remove(KEY_BRAND_COLLAB_PROFILE_PLATFORM)
                editor.remove(KEY_BRAND_COLLAB_PROFILE_FOLLOWERS)
                editor.remove(KEY_BRAND_COLLAB_PROFILE_NICHE)
                editor.remove(KEY_BRAND_COLLAB_PROFILE_LEVEL)
                editor.remove(KEY_BRAND_COLLAB_PROFILE_GOAL)
                editor.remove(KEY_BRAND_COLLAB_PROFILE_PROBLEM)
                editor.remove(KEY_BRAND_COLLAB_PROFILE_LESSON)
                editor.remove(KEY_BRAND_COLLAB_PROFILE_PROGRESS)
                editor.remove(KEY_BRAND_COLLAB_LEVEL2_COMPLETED)
                editor.remove(KEY_BRAND_COLLAB_LEVEL2_STEP)
                editor.remove(KEY_BRAND_COLLAB_LEVEL2_PROFILE_LINK)
                editor.remove(KEY_BRAND_COLLAB_LEVEL2_SELECTED_USERNAME)
                editor.remove(KEY_BRAND_COLLAB_LEVEL2_SELECTED_BIO)
                editor.remove(KEY_BRAND_COLLAB_LEVEL2_CHECKLIST)
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

    // ==========================================
    // REFER & REWARDS PHASE 4 PREFERENCES
    // ==========================================
    private const val KEY_REWARD_SUBMISSION_EXISTS = "reward_sub_exists"
    private const val KEY_REWARD_EMAIL = "reward_sub_email"
    private const val KEY_REWARD_NAME = "reward_sub_name"
    private const val KEY_REWARD_IG_USERNAME = "reward_sub_ig_username"
    private const val KEY_REWARD_IG_LINK = "reward_sub_ig_link"
    private const val KEY_REWARD_CONTENT_TYPES = "reward_sub_content_types"
    private const val KEY_REWARD_SCREENSHOT = "reward_sub_screenshot"
    private const val KEY_REWARD_STATUS = "reward_sub_status"
    private const val KEY_REWARD_DATE = "reward_sub_date"

    // ==========================================
    // PROFILE & SETTINGS PHASE 5 PREFERENCES
    // ==========================================
    private const val KEY_APP_THEME_MODE = "app_theme_mode" // "System", "Dark", "Light"
    private const val KEY_APP_LANGUAGE = "app_setting_language" // "English", "Hindi", "Hinglish"
    private const val KEY_APP_NOTIFICATIONS = "app_setting_notifications" // Boolean
    private const val KEY_LAST_COURSE_NAME = "last_opened_course_name"
    private const val KEY_LAST_COURSE_PROGRESS = "last_opened_course_progress"
    private const val KEY_LAST_COURSE_TOTAL_LESSONS = "last_opened_course_total_lessons"
    private const val KEY_LAST_COURSE_COMPLETED_LESSONS = "last_opened_course_completed_lessons"

    // User Profile Display Name
    private const val KEY_USER_DISPLAY_NAME = "user_display_name"

    fun getUserDisplayName(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_DISPLAY_NAME, "Creator Pro") ?: "Creator Pro"
    }

    fun saveUserDisplayName(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USER_DISPLAY_NAME, name).apply()
    }

    fun getAppThemeMode(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_APP_THEME_MODE, "Dark") ?: "Dark"
    }

    fun setAppThemeMode(context: Context, mode: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_APP_THEME_MODE, mode).apply()
    }

    fun getAppLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_APP_LANGUAGE, "English") ?: "English"
    }

    fun setAppLanguage(context: Context, lang: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_APP_LANGUAGE, lang).apply()
    }

    fun getNotificationsEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_APP_NOTIFICATIONS, true)
    }

    fun setNotificationsEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_APP_NOTIFICATIONS, enabled).apply()
    }

    fun saveLastOpenedCourse(
        context: Context,
        courseName: String,
        completedLessons: Int,
        totalLessons: Int
    ) {
        val progressPercent = if (totalLessons > 0) ((completedLessons.toFloat() / totalLessons.toFloat()) * 100).toInt() else 0
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_LAST_COURSE_NAME, courseName)
            .putInt(KEY_LAST_COURSE_PROGRESS, progressPercent)
            .putInt(KEY_LAST_COURSE_TOTAL_LESSONS, totalLessons)
            .putInt(KEY_LAST_COURSE_COMPLETED_LESSONS, completedLessons)
            .apply()
    }

    fun getLastOpenedCourse(context: Context): Map<String, Any>? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val name = prefs.getString(KEY_LAST_COURSE_NAME, null) ?: return null
        val progress = prefs.getInt(KEY_LAST_COURSE_PROGRESS, 0)
        val completed = prefs.getInt(KEY_LAST_COURSE_COMPLETED_LESSONS, 0)
        val total = prefs.getInt(KEY_LAST_COURSE_TOTAL_LESSONS, 1)
        return mapOf(
            "name" to name,
            "progress" to progress,
            "completed" to completed,
            "total" to total
        )
    }

    fun saveRewardSubmission(
        context: Context,
        email: String,
        name: String,
        igUsername: String,
        igLink: String,
        contentTypes: String,
        screenshotUri: String,
        status: String = "Pending Review"
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val dateFormat = java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault())
        val dateStr = dateFormat.format(java.util.Date())

        prefs.edit()
            .putBoolean(KEY_REWARD_SUBMISSION_EXISTS, true)
            .putString(KEY_REWARD_EMAIL, email)
            .putString(KEY_REWARD_NAME, name)
            .putString(KEY_REWARD_IG_USERNAME, igUsername)
            .putString(KEY_REWARD_IG_LINK, igLink)
            .putString(KEY_REWARD_CONTENT_TYPES, contentTypes)
            .putString(KEY_REWARD_SCREENSHOT, screenshotUri)
            .putString(KEY_REWARD_STATUS, status)
            .putString(KEY_REWARD_DATE, dateStr)
            .apply()
    }

    fun hasRewardSubmission(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_REWARD_SUBMISSION_EXISTS, false)
    }

    fun getRewardSubmissionStatus(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_REWARD_STATUS, "Pending Review") ?: "Pending Review"
    }

    fun getRewardSubmissionDetails(context: Context): Map<String, String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "email" to (prefs.getString(KEY_REWARD_EMAIL, "") ?: ""),
            "name" to (prefs.getString(KEY_REWARD_NAME, "") ?: ""),
            "igUsername" to (prefs.getString(KEY_REWARD_IG_USERNAME, "") ?: ""),
            "igLink" to (prefs.getString(KEY_REWARD_IG_LINK, "") ?: ""),
            "contentTypes" to (prefs.getString(KEY_REWARD_CONTENT_TYPES, "") ?: ""),
            "screenshot" to (prefs.getString(KEY_REWARD_SCREENSHOT, "") ?: ""),
            "status" to (prefs.getString(KEY_REWARD_STATUS, "Pending Review") ?: "Pending Review"),
            "date" to (prefs.getString(KEY_REWARD_DATE, "") ?: "")
        )
    }

    fun clearRewardSubmission(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .remove(KEY_REWARD_SUBMISSION_EXISTS)
            .remove(KEY_REWARD_EMAIL)
            .remove(KEY_REWARD_NAME)
            .remove(KEY_REWARD_IG_USERNAME)
            .remove(KEY_REWARD_IG_LINK)
            .remove(KEY_REWARD_CONTENT_TYPES)
            .remove(KEY_REWARD_SCREENSHOT)
            .remove(KEY_REWARD_STATUS)
            .remove(KEY_REWARD_DATE)
            .apply()
    }
}
