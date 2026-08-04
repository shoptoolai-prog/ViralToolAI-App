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

    // Brand Collab Phase 4 Level 3 AI Media Kit Preferences
    private const val KEY_BRAND_COLLAB_LEVEL3_COMPLETED = "brand_collab_level3_completed"
    private const val KEY_BRAND_COLLAB_LEVEL3_STEP = "brand_collab_level3_step"
    private const val KEY_BRAND_COLLAB_LEVEL3_FULL_NAME = "brand_collab_level3_full_name"
    private const val KEY_BRAND_COLLAB_LEVEL3_CREATOR_NAME = "brand_collab_level3_creator_name"
    private const val KEY_BRAND_COLLAB_LEVEL3_EMAIL = "brand_collab_level3_email"
    private const val KEY_BRAND_COLLAB_LEVEL3_CITY = "brand_collab_level3_city"
    private const val KEY_BRAND_COLLAB_LEVEL3_COUNTRY = "brand_collab_level3_country"
    private const val KEY_BRAND_COLLAB_LEVEL3_SOCIAL_LINKS = "brand_collab_level3_social_links"
    private const val KEY_BRAND_COLLAB_LEVEL3_BIO = "brand_collab_level3_bio"
    private const val KEY_BRAND_COLLAB_LEVEL3_AUDIENCE = "brand_collab_level3_audience"
    private const val KEY_BRAND_COLLAB_LEVEL3_DEMO_COUNTRY = "brand_collab_level3_demo_country"
    private const val KEY_BRAND_COLLAB_LEVEL3_DEMO_LANG = "brand_collab_level3_demo_lang"
    private const val KEY_BRAND_COLLAB_LEVEL3_DEMO_AGE = "brand_collab_level3_demo_age"
    private const val KEY_BRAND_COLLAB_LEVEL3_CATEGORIES = "brand_collab_level3_categories"
    private const val KEY_BRAND_COLLAB_LEVEL3_ACHIEVEMENTS = "brand_collab_level3_achievements"
    private const val KEY_BRAND_COLLAB_LEVEL3_FOLLOWERS = "brand_collab_level3_followers"
    private const val KEY_BRAND_COLLAB_LEVEL3_REACH = "brand_collab_level3_reach"
    private const val KEY_BRAND_COLLAB_LEVEL3_VIEWS = "brand_collab_level3_views"
    private const val KEY_BRAND_COLLAB_LEVEL3_ENGAGEMENT = "brand_collab_level3_engagement"
    private const val KEY_BRAND_COLLAB_LEVEL3_MONTHLY_VIEWS = "brand_collab_level3_monthly_views"
    private const val KEY_BRAND_COLLAB_LEVEL3_CHECKLIST = "brand_collab_level3_checklist"

    // Brand Collab Phase 5 Level 4 AI Rate Card Builder Preferences
    private const val KEY_BRAND_COLLAB_LEVEL4_COMPLETED = "brand_collab_level4_completed"
    private const val KEY_BRAND_COLLAB_LEVEL4_STEP = "brand_collab_level4_step"
    private const val KEY_BRAND_COLLAB_LEVEL4_FOLLOWERS = "brand_collab_level4_followers"
    private const val KEY_BRAND_COLLAB_LEVEL4_REACH = "brand_collab_level4_reach"
    private const val KEY_BRAND_COLLAB_LEVEL4_REEL_VIEWS = "brand_collab_level4_reel_views"
    private const val KEY_BRAND_COLLAB_LEVEL4_STORY_VIEWS = "brand_collab_level4_story_views"
    private const val KEY_BRAND_COLLAB_LEVEL4_LIKES = "brand_collab_level4_likes"
    private const val KEY_BRAND_COLLAB_LEVEL4_ENGAGEMENT = "brand_collab_level4_engagement"
    private const val KEY_BRAND_COLLAB_LEVEL4_CONTENT_TYPES = "brand_collab_level4_content_types"
    private const val KEY_BRAND_COLLAB_LEVEL4_COLLAB_TYPES = "brand_collab_level4_collab_types"
    private const val KEY_BRAND_COLLAB_LEVEL4_BRAND_TYPES = "brand_collab_level4_brand_types"
    private const val KEY_BRAND_COLLAB_LEVEL4_COUNTRY = "brand_collab_level4_country"
    private const val KEY_BRAND_COLLAB_LEVEL4_STORY_PRICE = "brand_collab_level4_story_price"
    private const val KEY_BRAND_COLLAB_LEVEL4_REEL_PRICE = "brand_collab_level4_reel_price"
    private const val KEY_BRAND_COLLAB_LEVEL4_FEED_PRICE = "brand_collab_level4_feed_price"
    private const val KEY_BRAND_COLLAB_LEVEL4_YOUTUBE_PRICE = "brand_collab_level4_youtube_price"
    private const val KEY_BRAND_COLLAB_LEVEL4_UGC_PRICE = "brand_collab_level4_ugc_price"
    private const val KEY_BRAND_COLLAB_LEVEL4_MONTHLY_PACKAGE_PRICE = "brand_collab_level4_monthly_package_price"
    private const val KEY_BRAND_COLLAB_LEVEL4_NEGOTIATION_CONFIDENCE = "brand_collab_level4_negotiation_confidence"
    private const val KEY_BRAND_COLLAB_LEVEL4_CHECKLIST = "brand_collab_level4_checklist"

    // Brand Collab Phase 6 Level 5 AI Brand Finder Preferences
    private const val KEY_BRAND_COLLAB_LEVEL5_COMPLETED = "brand_collab_level5_completed"
    private const val KEY_BRAND_COLLAB_LEVEL5_STEP = "brand_collab_level5_step"
    private const val KEY_BRAND_COLLAB_LEVEL5_PLATFORM = "brand_collab_level5_platform"
    private const val KEY_BRAND_COLLAB_LEVEL5_NICHE = "brand_collab_level5_niche"
    private const val KEY_BRAND_COLLAB_LEVEL5_BRAND_SIZE = "brand_collab_level5_brand_size"
    private const val KEY_BRAND_COLLAB_LEVEL5_COLLAB_TYPE = "brand_collab_level5_collab_type"
    private const val KEY_BRAND_COLLAB_LEVEL5_SAVED_BRANDS = "brand_collab_level5_saved_brands"
    private const val KEY_BRAND_COLLAB_LEVEL5_CHECKLIST = "brand_collab_level5_checklist"

    // Brand Collab Phase 7 Level 6 AI Outreach Master Preferences
    private const val KEY_BRAND_COLLAB_LEVEL6_COMPLETED = "brand_collab_level6_completed"
    private const val KEY_BRAND_COLLAB_LEVEL6_STEP = "brand_collab_level6_step"
    private const val KEY_BRAND_COLLAB_LEVEL6_METHOD = "brand_collab_level6_method"
    private const val KEY_BRAND_COLLAB_LEVEL6_BRAND_TYPE = "brand_collab_level6_brand_type"
    private const val KEY_BRAND_COLLAB_LEVEL6_BRAND_NAME = "brand_collab_level6_brand_name"
    private const val KEY_BRAND_COLLAB_LEVEL6_WEBSITE = "brand_collab_level6_website"
    private const val KEY_BRAND_COLLAB_LEVEL6_IG_HANDLE = "brand_collab_level6_ig_handle"
    private const val KEY_BRAND_COLLAB_LEVEL6_PURPOSE = "brand_collab_level6_purpose"
    private const val KEY_BRAND_COLLAB_LEVEL6_EMAIL_TEXT = "brand_collab_level6_email_text"
    private const val KEY_BRAND_COLLAB_LEVEL6_DM_TEXT = "brand_collab_level6_dm_text"
    private const val KEY_BRAND_COLLAB_LEVEL6_FOLLOWUP_TEXT = "brand_collab_level6_followup_text"
    private const val KEY_BRAND_COLLAB_LEVEL6_CONFIDENCE = "brand_collab_level6_confidence"

    // Brand Collab Phase 8 Level 7 AI Negotiation Master Preferences
    private const val KEY_BRAND_COLLAB_LEVEL7_COMPLETED = "brand_collab_level7_completed"
    private const val KEY_BRAND_COLLAB_LEVEL7_STEP = "brand_collab_level7_step"
    private const val KEY_BRAND_COLLAB_LEVEL7_MODULE = "brand_collab_level7_module"
    private const val KEY_BRAND_COLLAB_LEVEL7_SCORE_CONFIDENCE = "brand_collab_level7_score_confidence"
    private const val KEY_BRAND_COLLAB_LEVEL7_SCORE_COMMUNICATION = "brand_collab_level7_score_comm"
    private const val KEY_BRAND_COLLAB_LEVEL7_SCORE_CLOSING = "brand_collab_level7_score_closing"
    private const val KEY_BRAND_COLLAB_LEVEL7_SCORE_PRO = "brand_collab_level7_score_pro"

    // Brand Collab Phase 9 Level 8 AI Contract & Legal Guide Preferences
    private const val KEY_BRAND_COLLAB_LEVEL8_COMPLETED = "brand_collab_level8_completed"
    private const val KEY_BRAND_COLLAB_LEVEL8_STEP = "brand_collab_level8_step"
    private const val KEY_BRAND_COLLAB_LEVEL8_MODULE = "brand_collab_level8_module"
    private const val KEY_BRAND_COLLAB_LEVEL8_EXPLAIN_LANG = "brand_collab_level8_explain_lang"

    // Brand Collab Phase 10 Level 9 AI Payment & Finance Hub Preferences
    private const val KEY_BRAND_COLLAB_LEVEL9_COMPLETED = "brand_collab_level9_completed"
    private const val KEY_BRAND_COLLAB_LEVEL9_STEP = "brand_collab_level9_step"
    private const val KEY_BRAND_COLLAB_LEVEL9_MODULE = "brand_collab_level9_module"
    private const val KEY_BRAND_COLLAB_LEVEL9_INVOICE_NUM = "brand_collab_level9_inv_num"
    private const val KEY_BRAND_COLLAB_LEVEL9_BRAND_NAME = "brand_collab_level9_brand_name"
    private const val KEY_BRAND_COLLAB_LEVEL9_AMOUNT = "brand_collab_level9_amount"

    // Brand Collab Phase 11 Level 10 Creator CRM Preferences
    private const val KEY_BRAND_COLLAB_LEVEL10_COMPLETED = "brand_collab_level10_completed"
    private const val KEY_BRAND_COLLAB_LEVEL10_STEP = "brand_collab_level10_step"
    private const val KEY_BRAND_COLLAB_LEVEL10_MODULE = "brand_collab_level10_module"
    private const val KEY_BRAND_COLLAB_LEVEL10_BRANDS_DATA = "brand_collab_level10_brands_data"
    private const val KEY_BRAND_COLLAB_LEVEL10_SELECTED_BRAND = "brand_collab_level10_selected_brand"

    // Brand Collab Phase 12 Level 11 AI Campaign Planner Preferences
    private const val KEY_BRAND_COLLAB_LEVEL11_COMPLETED = "brand_collab_level11_completed"
    private const val KEY_BRAND_COLLAB_LEVEL11_STEP = "brand_collab_level11_step"
    private const val KEY_BRAND_COLLAB_LEVEL11_MODULE = "brand_collab_level11_module"
    private const val KEY_BRAND_COLLAB_LEVEL11_CAMPAIGN_NAME = "brand_collab_level11_campaign_name"
    private const val KEY_BRAND_COLLAB_LEVEL11_BRAND_NAME = "brand_collab_level11_brand_name"

    // Brand Collab Phase 13 Level 12 AI Portfolio Builder Preferences
    private const val KEY_BRAND_COLLAB_LEVEL12_COMPLETED = "brand_collab_level12_completed"
    private const val KEY_BRAND_COLLAB_LEVEL12_STEP = "brand_collab_level12_step"
    private const val KEY_BRAND_COLLAB_LEVEL12_MODULE = "brand_collab_level12_module"
    private const val KEY_BRAND_COLLAB_LEVEL12_PORTFOLIO_NAME = "brand_collab_level12_portfolio_name"
    private const val KEY_BRAND_COLLAB_LEVEL12_PORTFOLIO_BIO = "brand_collab_level12_portfolio_bio"

    // Brand Collab Phase 14 Level 13 Creator Business Dashboard Preferences
    private const val KEY_BRAND_COLLAB_LEVEL13_COMPLETED = "brand_collab_level13_completed"
    private const val KEY_BRAND_COLLAB_LEVEL13_STEP = "brand_collab_level13_step"
    private const val KEY_BRAND_COLLAB_LEVEL13_MODULE = "brand_collab_level13_module"
    private const val KEY_BRAND_COLLAB_LEVEL13_MONTHLY_GOAL = "brand_collab_level13_monthly_goal"
    private const val KEY_BRAND_COLLAB_LEVEL13_CURRENT_XP = "brand_collab_level13_current_xp"

    // Brand Collab Phase 15 Final Level Creator Success Hub Preferences
    private const val KEY_BRAND_COLLAB_PHASE15_COMPLETED = "brand_collab_phase15_completed"
    private const val KEY_BRAND_COLLAB_CERTIFICATE_DATE = "brand_collab_certificate_date"
    private const val KEY_BRAND_COLLAB_CERTIFICATE_ID = "brand_collab_certificate_id"
    private const val KEY_BRAND_COLLAB_SUCCESS_GOAL = "brand_collab_success_goal"
    private const val KEY_BRAND_COLLAB_SUCCESS_XP = "brand_collab_success_xp"
    private const val KEY_MEESHO_CREATOR_LANG = "meesho_creator_language"
    private const val KEY_MEESHO_CREATOR_STEP_INDEX = "meesho_creator_step_index"

    private const val KEY_WISHLINK_LANG = "wishlink_creator_language"
    private const val KEY_WISHLINK_STEP_INDEX = "wishlink_creator_step_index"
    private const val KEY_WISHLINK_COMPLETED_STEPS = "wishlink_creator_completed_steps"
    private const val KEY_WISHLINK_ONBOARDING_DONE = "wishlink_creator_onboarding_done"
    private const val KEY_WISHLINK_LEVEL1_COMPLETED = "wishlink_creator_level1_completed"
    private const val KEY_WISHLINK_HEARD_BEFORE = "wishlink_creator_heard_before"
    private const val KEY_WISHLINK_HAS_ACCOUNT = "wishlink_creator_has_account"
    private const val KEY_WISHLINK_PLATFORMS = "wishlink_creator_platforms"
    private const val KEY_WISHLINK_NICHE = "wishlink_creator_niche"
    private const val KEY_WISHLINK_GOAL = "wishlink_creator_goal"
    private const val KEY_WISHLINK_LEARNING_LEVEL = "wishlink_creator_learning_level"
    private const val KEY_WISHLINK_LEVEL2_COMPLETED = "wishlink_creator_level2_completed"
    private const val KEY_WISHLINK_LEVEL3_COMPLETED = "wishlink_creator_level3_completed"
    private const val KEY_WISHLINK_LEVEL3_SCORE = "wishlink_creator_level3_score"
    private const val KEY_WISHLINK_LEVEL4_COMPLETED = "wishlink_creator_level4_completed"
    private const val KEY_WISHLINK_LEVEL4_SCORE = "wishlink_creator_level4_score"
    private const val KEY_WISHLINK_LEVEL4_GENERATED_LINKS = "wishlink_creator_level4_generated_links"
    private const val KEY_WISHLINK_LEVEL5_COMPLETED = "wishlink_creator_level5_completed"
    private const val KEY_WISHLINK_LEVEL5_SCORE = "wishlink_creator_level5_score"
    private const val KEY_WISHLINK_LEVEL5_STORE_STYLE = "wishlink_creator_level5_store_style"
    private const val KEY_WISHLINK_LEVEL5_AUDIT_SCORE = "wishlink_creator_level5_audit_score"
    private const val KEY_WISHLINK_LEVEL6_COMPLETED = "wishlink_creator_level6_completed"
    private const val KEY_WISHLINK_LEVEL6_SCORE = "wishlink_creator_level6_score"
    private const val KEY_WISHLINK_LEVEL6_RESEARCH_COUNT = "wishlink_creator_level6_research_count"
    private const val KEY_WISHLINK_LEVEL7_COMPLETED = "wishlink_creator_level7_completed"
    private const val KEY_WISHLINK_LEVEL7_SCORE = "wishlink_creator_level7_score"
    private const val KEY_WISHLINK_LEVEL7_FUNNEL_COUNT = "wishlink_creator_level7_funnel_count"
    private const val KEY_WISHLINK_LEVEL8_COMPLETED = "wishlink_creator_level8_completed"
    private const val KEY_WISHLINK_LEVEL8_SCORE = "wishlink_creator_level8_score"
    private const val KEY_WISHLINK_LEVEL8_HEALTH_SCORE = "wishlink_creator_level8_health_score"
    private const val KEY_WISHLINK_LEVEL9_COMPLETED = "wishlink_creator_level9_completed"
    private const val KEY_WISHLINK_LEVEL9_SCORE = "wishlink_creator_level9_score"
    private const val KEY_WISHLINK_LEVEL9_PLAN_SCORE = "wishlink_creator_level9_plan_score"
    private const val KEY_WISHLINK_LEVEL10_COMPLETED = "wishlink_creator_level10_completed"
    private const val KEY_WISHLINK_LEVEL10_SCORE = "wishlink_creator_level10_score"
    private const val KEY_WISHLINK_LEVEL10_STREAK = "wishlink_creator_level10_streak"
    private const val KEY_WISHLINK_LEVEL10_CALENDAR_JSON = "wishlink_creator_level10_calendar_json"
    private const val KEY_WISHLINK_LEVEL10_WEEKLY_PLAN_JSON = "wishlink_creator_level10_weekly_plan_json"
    private const val KEY_WISHLINK_LEVEL11_COMPLETED = "wishlink_creator_level11_completed"
    private const val KEY_WISHLINK_LEVEL11_SCORE = "wishlink_creator_level11_score"
    private const val KEY_WISHLINK_LEVEL11_PACKAGE_JSON = "wishlink_creator_level11_package_json"
    private const val KEY_WISHLINK_LEVEL12_COMPLETED = "wishlink_creator_level12_completed"
    private const val KEY_WISHLINK_LEVEL12_SCORE = "wishlink_creator_level12_score"
    private const val KEY_WISHLINK_LEVEL12_PLAN_JSON = "wishlink_creator_level12_plan_json"
    private const val KEY_WISHLINK_LEVEL12_HEALTH_SCORE = "wishlink_creator_level12_health_score"
    private const val KEY_WISHLINK_LEVEL13_COMPLETED = "wishlink_creator_level13_completed"
    private const val KEY_WISHLINK_LEVEL13_SCORE = "wishlink_creator_level13_score"
    private const val KEY_WISHLINK_LEVEL13_PORTFOLIO_JSON = "wishlink_creator_level13_portfolio_json"
    private const val KEY_WISHLINK_LEVEL13_MEDIA_KIT_JSON = "wishlink_creator_level13_media_kit_json"
    private const val KEY_WISHLINK_LEVEL14_COMPLETED = "wishlink_creator_level14_completed"
    private const val KEY_WISHLINK_LEVEL14_SCORE = "wishlink_creator_level14_score"
    private const val KEY_WISHLINK_LEVEL14_GOALS_JSON = "wishlink_creator_level14_goals_json"
    private const val KEY_WISHLINK_LEVEL14_VAULT_JSON = "wishlink_creator_level14_vault_json"
    private const val KEY_WISHLINK_LEVEL15_COMPLETED = "wishlink_creator_level15_completed"
    private const val KEY_WISHLINK_LEVEL15_SCORE = "wishlink_creator_level15_score"
    private const val KEY_WISHLINK_LEVEL15_CERTIFICATE_JSON = "wishlink_creator_level15_certificate_json"
    private const val KEY_WISHLINK_INSTALLED = "wishlink_creator_installed"
    private const val KEY_WISHLINK_ACCOUNT_STATUS = "wishlink_creator_account_status"
    private const val KEY_WISHLINK_PLATFORM_CONNECTED = "wishlink_creator_platform_connected"
    private const val KEY_WISHLINK_STORE_STATUS = "wishlink_creator_store_status"
    private const val KEY_WISHLINK_PROGRESS = "wishlink_creator_progress"

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

    // Phase 4 Level 3 Preference Helpers
    fun isBrandCollabLevel3Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL3_COMPLETED, false)
    }

    fun saveBrandCollabLevel3State(
        context: Context,
        step: Int,
        fullName: String = "",
        creatorName: String = "",
        email: String = "",
        city: String = "",
        country: String = "",
        socialLinks: String = "",
        bio: String = "",
        audience: String = "",
        demoCountry: String = "",
        demoLang: String = "",
        demoAge: String = "",
        categories: String = "",
        achievements: String = "",
        followers: String = "",
        reach: String = "",
        views: String = "",
        engagement: String = "",
        monthlyViews: String = "",
        checklistCsv: String = "",
        isCompleted: Boolean = false
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_BRAND_COLLAB_LEVEL3_STEP, step)
            .putString(KEY_BRAND_COLLAB_LEVEL3_FULL_NAME, fullName)
            .putString(KEY_BRAND_COLLAB_LEVEL3_CREATOR_NAME, creatorName)
            .putString(KEY_BRAND_COLLAB_LEVEL3_EMAIL, email)
            .putString(KEY_BRAND_COLLAB_LEVEL3_CITY, city)
            .putString(KEY_BRAND_COLLAB_LEVEL3_COUNTRY, country)
            .putString(KEY_BRAND_COLLAB_LEVEL3_SOCIAL_LINKS, socialLinks)
            .putString(KEY_BRAND_COLLAB_LEVEL3_BIO, bio)
            .putString(KEY_BRAND_COLLAB_LEVEL3_AUDIENCE, audience)
            .putString(KEY_BRAND_COLLAB_LEVEL3_DEMO_COUNTRY, demoCountry)
            .putString(KEY_BRAND_COLLAB_LEVEL3_DEMO_LANG, demoLang)
            .putString(KEY_BRAND_COLLAB_LEVEL3_DEMO_AGE, demoAge)
            .putString(KEY_BRAND_COLLAB_LEVEL3_CATEGORIES, categories)
            .putString(KEY_BRAND_COLLAB_LEVEL3_ACHIEVEMENTS, achievements)
            .putString(KEY_BRAND_COLLAB_LEVEL3_FOLLOWERS, followers)
            .putString(KEY_BRAND_COLLAB_LEVEL3_REACH, reach)
            .putString(KEY_BRAND_COLLAB_LEVEL3_VIEWS, views)
            .putString(KEY_BRAND_COLLAB_LEVEL3_ENGAGEMENT, engagement)
            .putString(KEY_BRAND_COLLAB_LEVEL3_MONTHLY_VIEWS, monthlyViews)
            .putString(KEY_BRAND_COLLAB_LEVEL3_CHECKLIST, checklistCsv)
            .putBoolean(KEY_BRAND_COLLAB_LEVEL3_COMPLETED, isCompleted)
            .apply()
    }

    fun getBrandCollabLevel3Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "completed" to prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL3_COMPLETED, false),
            "step" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL3_STEP, 1),
            "fullName" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_FULL_NAME, "") ?: ""),
            "creatorName" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_CREATOR_NAME, "") ?: ""),
            "email" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_EMAIL, "") ?: ""),
            "city" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_CITY, "") ?: ""),
            "country" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_COUNTRY, "") ?: ""),
            "socialLinks" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_SOCIAL_LINKS, "") ?: ""),
            "bio" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_BIO, "") ?: ""),
            "audience" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_AUDIENCE, "") ?: ""),
            "demoCountry" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_DEMO_COUNTRY, "India") ?: "India"),
            "demoLang" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_DEMO_LANG, "Hindi / English") ?: "Hindi / English"),
            "demoAge" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_DEMO_AGE, "18–24 years") ?: "18–24 years"),
            "categories" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_CATEGORIES, "") ?: ""),
            "achievements" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_ACHIEVEMENTS, "") ?: ""),
            "followers" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_FOLLOWERS, "10,500") ?: "10,500"),
            "reach" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_REACH, "45,000 / month") ?: "45,000 / month"),
            "views" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_VIEWS, "25,000 / reel") ?: "25,000 / reel"),
            "engagement" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_ENGAGEMENT, "6.8%") ?: "6.8%"),
            "monthlyViews" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_MONTHLY_VIEWS, "180,000") ?: "180,000"),
            "checklist" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL3_CHECKLIST, "") ?: "")
        )
    }

    // Phase 5 Level 4 Preference Helpers
    fun isBrandCollabLevel4Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL4_COMPLETED, false)
    }

    fun saveBrandCollabLevel4State(
        context: Context,
        step: Int,
        followers: String = "",
        reach: String = "",
        reelViews: String = "",
        storyViews: String = "",
        likes: String = "",
        engagement: String = "",
        contentTypes: String = "",
        collabTypes: String = "",
        brandTypes: String = "",
        country: String = "",
        storyPrice: String = "",
        reelPrice: String = "",
        feedPrice: String = "",
        youtubePrice: String = "",
        ugcPrice: String = "",
        monthlyPackagePrice: String = "",
        negotiationConfidence: String = "",
        checklistCsv: String = "",
        isCompleted: Boolean = false
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_BRAND_COLLAB_LEVEL4_STEP, step)
            .putString(KEY_BRAND_COLLAB_LEVEL4_FOLLOWERS, followers)
            .putString(KEY_BRAND_COLLAB_LEVEL4_REACH, reach)
            .putString(KEY_BRAND_COLLAB_LEVEL4_REEL_VIEWS, reelViews)
            .putString(KEY_BRAND_COLLAB_LEVEL4_STORY_VIEWS, storyViews)
            .putString(KEY_BRAND_COLLAB_LEVEL4_LIKES, likes)
            .putString(KEY_BRAND_COLLAB_LEVEL4_ENGAGEMENT, engagement)
            .putString(KEY_BRAND_COLLAB_LEVEL4_CONTENT_TYPES, contentTypes)
            .putString(KEY_BRAND_COLLAB_LEVEL4_COLLAB_TYPES, collabTypes)
            .putString(KEY_BRAND_COLLAB_LEVEL4_BRAND_TYPES, brandTypes)
            .putString(KEY_BRAND_COLLAB_LEVEL4_COUNTRY, country)
            .putString(KEY_BRAND_COLLAB_LEVEL4_STORY_PRICE, storyPrice)
            .putString(KEY_BRAND_COLLAB_LEVEL4_REEL_PRICE, reelPrice)
            .putString(KEY_BRAND_COLLAB_LEVEL4_FEED_PRICE, feedPrice)
            .putString(KEY_BRAND_COLLAB_LEVEL4_YOUTUBE_PRICE, youtubePrice)
            .putString(KEY_BRAND_COLLAB_LEVEL4_UGC_PRICE, ugcPrice)
            .putString(KEY_BRAND_COLLAB_LEVEL4_MONTHLY_PACKAGE_PRICE, monthlyPackagePrice)
            .putString(KEY_BRAND_COLLAB_LEVEL4_NEGOTIATION_CONFIDENCE, negotiationConfidence)
            .putString(KEY_BRAND_COLLAB_LEVEL4_CHECKLIST, checklistCsv)
            .putBoolean(KEY_BRAND_COLLAB_LEVEL4_COMPLETED, isCompleted)
            .apply()
    }

    fun getBrandCollabLevel4Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "completed" to prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL4_COMPLETED, false),
            "step" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL4_STEP, 1),
            "followers" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_FOLLOWERS, "") ?: ""),
            "reach" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_REACH, "") ?: ""),
            "reelViews" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_REEL_VIEWS, "") ?: ""),
            "storyViews" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_STORY_VIEWS, "") ?: ""),
            "likes" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_LIKES, "") ?: ""),
            "engagement" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_ENGAGEMENT, "") ?: ""),
            "contentTypes" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_CONTENT_TYPES, "") ?: ""),
            "collabTypes" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_COLLAB_TYPES, "") ?: ""),
            "brandTypes" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_BRAND_TYPES, "") ?: ""),
            "country" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_COUNTRY, "India") ?: "India"),
            "storyPrice" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_STORY_PRICE, "₹1,500 – ₹3,500") ?: "₹1,500 – ₹3,500"),
            "reelPrice" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_REEL_PRICE, "₹5,000 – ₹12,000") ?: "₹5,000 – ₹12,000"),
            "feedPrice" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_FEED_PRICE, "₹3,000 – ₹7,000") ?: "₹3,000 – ₹7,000"),
            "youtubePrice" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_YOUTUBE_PRICE, "₹10,000 – ₹25,000") ?: "₹10,000 – ₹25,000"),
            "ugcPrice" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_UGC_PRICE, "₹4,000 – ₹9,000") ?: "₹4,000 – ₹9,000"),
            "monthlyPackagePrice" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_MONTHLY_PACKAGE_PRICE, "₹25,000 – ₹60,000") ?: "₹25,000 – ₹60,000"),
            "negotiationConfidence" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_NEGOTIATION_CONFIDENCE, "High") ?: "High"),
            "checklist" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL4_CHECKLIST, "") ?: "")
        )
    }

    // Phase 6 Level 5 Preference Helpers
    fun isBrandCollabLevel5Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL5_COMPLETED, false)
    }

    fun saveBrandCollabLevel5State(
        context: Context,
        step: Int,
        platform: String = "",
        niche: String = "",
        brandSize: String = "",
        collabType: String = "",
        savedBrandsJson: String = "",
        checklistCsv: String = "",
        isCompleted: Boolean = false
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_BRAND_COLLAB_LEVEL5_STEP, step)
            .putString(KEY_BRAND_COLLAB_LEVEL5_PLATFORM, platform)
            .putString(KEY_BRAND_COLLAB_LEVEL5_NICHE, niche)
            .putString(KEY_BRAND_COLLAB_LEVEL5_BRAND_SIZE, brandSize)
            .putString(KEY_BRAND_COLLAB_LEVEL5_COLLAB_TYPE, collabType)
            .putString(KEY_BRAND_COLLAB_LEVEL5_SAVED_BRANDS, savedBrandsJson)
            .putString(KEY_BRAND_COLLAB_LEVEL5_CHECKLIST, checklistCsv)
            .putBoolean(KEY_BRAND_COLLAB_LEVEL5_COMPLETED, isCompleted)
            .apply()
    }

    fun getBrandCollabLevel5Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "completed" to prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL5_COMPLETED, false),
            "step" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL5_STEP, 1),
            "platform" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL5_PLATFORM, "Instagram") ?: "Instagram"),
            "niche" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL5_NICHE, "Tech") ?: "Tech"),
            "brandSize" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL5_BRAND_SIZE, "Growing Brands") ?: "Growing Brands"),
            "collabType" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL5_COLLAB_TYPE, "Paid") ?: "Paid"),
            "savedBrands" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL5_SAVED_BRANDS, "") ?: ""),
            "checklist" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL5_CHECKLIST, "") ?: "")
        )
    }

    // Phase 7 Level 6 Preference Helpers
    fun isBrandCollabLevel6Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL6_COMPLETED, false)
    }

    fun saveBrandCollabLevel6State(
        context: Context,
        step: Int,
        method: String = "",
        brandType: String = "",
        brandName: String = "",
        website: String = "",
        igHandle: String = "",
        purpose: String = "",
        emailText: String = "",
        dmText: String = "",
        followupText: String = "",
        confidenceScore: Int = 88,
        isCompleted: Boolean = false
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_BRAND_COLLAB_LEVEL6_STEP, step)
            .putString(KEY_BRAND_COLLAB_LEVEL6_METHOD, method)
            .putString(KEY_BRAND_COLLAB_LEVEL6_BRAND_TYPE, brandType)
            .putString(KEY_BRAND_COLLAB_LEVEL6_BRAND_NAME, brandName)
            .putString(KEY_BRAND_COLLAB_LEVEL6_WEBSITE, website)
            .putString(KEY_BRAND_COLLAB_LEVEL6_IG_HANDLE, igHandle)
            .putString(KEY_BRAND_COLLAB_LEVEL6_PURPOSE, purpose)
            .putString(KEY_BRAND_COLLAB_LEVEL6_EMAIL_TEXT, emailText)
            .putString(KEY_BRAND_COLLAB_LEVEL6_DM_TEXT, dmText)
            .putString(KEY_BRAND_COLLAB_LEVEL6_FOLLOWUP_TEXT, followupText)
            .putInt(KEY_BRAND_COLLAB_LEVEL6_CONFIDENCE, confidenceScore)
            .putBoolean(KEY_BRAND_COLLAB_LEVEL6_COMPLETED, isCompleted)
            .apply()
    }

    fun getBrandCollabLevel6Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "completed" to prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL6_COMPLETED, false),
            "step" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL6_STEP, 1),
            "method" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL6_METHOD, "Instagram DM") ?: "Instagram DM"),
            "brandType" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL6_BRAND_TYPE, "Growing Brand") ?: "Growing Brand"),
            "brandName" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL6_BRAND_NAME, "") ?: ""),
            "website" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL6_WEBSITE, "") ?: ""),
            "igHandle" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL6_IG_HANDLE, "") ?: ""),
            "purpose" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL6_PURPOSE, "Paid Collaboration") ?: "Paid Collaboration"),
            "emailText" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL6_EMAIL_TEXT, "") ?: ""),
            "dmText" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL6_DM_TEXT, "") ?: ""),
            "followupText" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL6_FOLLOWUP_TEXT, "") ?: ""),
            "confidence" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL6_CONFIDENCE, 88)
        )
    }

    // Phase 8 Level 7 Preference Helpers
    fun isBrandCollabLevel7Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL7_COMPLETED, false)
    }

    fun saveBrandCollabLevel7State(
        context: Context,
        step: Int,
        module: Int = 1,
        scoreConfidence: Int = 90,
        scoreComm: Int = 92,
        scoreClosing: Int = 88,
        scorePro: Int = 95,
        isCompleted: Boolean = false
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_BRAND_COLLAB_LEVEL7_STEP, step)
            .putInt(KEY_BRAND_COLLAB_LEVEL7_MODULE, module)
            .putInt(KEY_BRAND_COLLAB_LEVEL7_SCORE_CONFIDENCE, scoreConfidence)
            .putInt(KEY_BRAND_COLLAB_LEVEL7_SCORE_COMMUNICATION, scoreComm)
            .putInt(KEY_BRAND_COLLAB_LEVEL7_SCORE_CLOSING, scoreClosing)
            .putInt(KEY_BRAND_COLLAB_LEVEL7_SCORE_PRO, scorePro)
            .putBoolean(KEY_BRAND_COLLAB_LEVEL7_COMPLETED, isCompleted)
            .apply()
    }

    fun getBrandCollabLevel7Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "completed" to prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL7_COMPLETED, false),
            "step" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL7_STEP, 1),
            "module" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL7_MODULE, 1),
            "scoreConfidence" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL7_SCORE_CONFIDENCE, 90),
            "scoreComm" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL7_SCORE_COMMUNICATION, 92),
            "scoreClosing" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL7_SCORE_CLOSING, 88),
            "scorePro" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL7_SCORE_PRO, 95)
        )
    }

    // Phase 9 Level 8 Preference Helpers
    fun isBrandCollabLevel8Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL8_COMPLETED, false)
    }

    fun saveBrandCollabLevel8State(
        context: Context,
        step: Int,
        module: Int = 1,
        explainLang: String = "Hinglish",
        isCompleted: Boolean = false
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_BRAND_COLLAB_LEVEL8_STEP, step)
            .putInt(KEY_BRAND_COLLAB_LEVEL8_MODULE, module)
            .putString(KEY_BRAND_COLLAB_LEVEL8_EXPLAIN_LANG, explainLang)
            .putBoolean(KEY_BRAND_COLLAB_LEVEL8_COMPLETED, isCompleted)
            .apply()
    }

    fun getBrandCollabLevel8Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "completed" to prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL8_COMPLETED, false),
            "step" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL8_STEP, 1),
            "module" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL8_MODULE, 1),
            "explainLang" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL8_EXPLAIN_LANG, "Hinglish") ?: "Hinglish")
        )
    }

    // Phase 10 Level 9 Preference Helpers
    fun isBrandCollabLevel9Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL9_COMPLETED, false)
    }

    fun saveBrandCollabLevel9State(
        context: Context,
        step: Int,
        module: Int = 1,
        invNum: String = "INV-2026-001",
        brandName: String = "Boat Audio",
        amount: String = "₹15,000",
        isCompleted: Boolean = false
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_BRAND_COLLAB_LEVEL9_STEP, step)
            .putInt(KEY_BRAND_COLLAB_LEVEL9_MODULE, module)
            .putString(KEY_BRAND_COLLAB_LEVEL9_INVOICE_NUM, invNum)
            .putString(KEY_BRAND_COLLAB_LEVEL9_BRAND_NAME, brandName)
            .putString(KEY_BRAND_COLLAB_LEVEL9_AMOUNT, amount)
            .putBoolean(KEY_BRAND_COLLAB_LEVEL9_COMPLETED, isCompleted)
            .apply()
    }

    fun getBrandCollabLevel9Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "completed" to prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL9_COMPLETED, false),
            "step" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL9_STEP, 1),
            "module" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL9_MODULE, 1),
            "invNum" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL9_INVOICE_NUM, "INV-2026-001") ?: "INV-2026-001"),
            "brandName" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL9_BRAND_NAME, "Boat Audio") ?: "Boat Audio"),
            "amount" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL9_AMOUNT, "₹15,000") ?: "₹15,000")
        )
    }

    // Phase 11 Level 10 Preference Helpers
    fun isBrandCollabLevel10Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL10_COMPLETED, false)
    }

    fun saveBrandCollabLevel10State(
        context: Context,
        step: Int,
        module: Int = 1,
        brandsData: String = "",
        selectedBrand: String = "",
        isCompleted: Boolean = false
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_BRAND_COLLAB_LEVEL10_STEP, step)
            .putInt(KEY_BRAND_COLLAB_LEVEL10_MODULE, module)
            .putString(KEY_BRAND_COLLAB_LEVEL10_BRANDS_DATA, brandsData)
            .putString(KEY_BRAND_COLLAB_LEVEL10_SELECTED_BRAND, selectedBrand)
            .putBoolean(KEY_BRAND_COLLAB_LEVEL10_COMPLETED, isCompleted)
            .apply()
    }

    fun getBrandCollabLevel10Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "completed" to prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL10_COMPLETED, false),
            "step" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL10_STEP, 1),
            "module" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL10_MODULE, 1),
            "brandsData" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL10_BRANDS_DATA, "") ?: ""),
            "selectedBrand" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL10_SELECTED_BRAND, "") ?: "")
        )
    }

    // Phase 12 Level 11 Preference Helpers
    fun isBrandCollabLevel11Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL11_COMPLETED, false)
    }

    fun saveBrandCollabLevel11State(
        context: Context,
        step: Int,
        module: Int = 1,
        campaignName: String = "",
        brandName: String = "",
        isCompleted: Boolean = false
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_BRAND_COLLAB_LEVEL11_STEP, step)
            .putInt(KEY_BRAND_COLLAB_LEVEL11_MODULE, module)
            .putString(KEY_BRAND_COLLAB_LEVEL11_CAMPAIGN_NAME, campaignName)
            .putString(KEY_BRAND_COLLAB_LEVEL11_BRAND_NAME, brandName)
            .putBoolean(KEY_BRAND_COLLAB_LEVEL11_COMPLETED, isCompleted)
            .apply()
    }

    fun getBrandCollabLevel11Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "completed" to prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL11_COMPLETED, false),
            "step" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL11_STEP, 1),
            "module" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL11_MODULE, 1),
            "campaignName" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL11_CAMPAIGN_NAME, "Nirvana Ion Launch") ?: "Nirvana Ion Launch"),
            "brandName" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL11_BRAND_NAME, "Boat Audio") ?: "Boat Audio")
        )
    }

    // Phase 13 Level 12 Preference Helpers
    fun isBrandCollabLevel12Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL12_COMPLETED, false)
    }

    fun saveBrandCollabLevel12State(
        context: Context,
        step: Int,
        module: Int = 1,
        portfolioName: String = "",
        portfolioBio: String = "",
        isCompleted: Boolean = false
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_BRAND_COLLAB_LEVEL12_STEP, step)
            .putInt(KEY_BRAND_COLLAB_LEVEL12_MODULE, module)
            .putString(KEY_BRAND_COLLAB_LEVEL12_PORTFOLIO_NAME, portfolioName)
            .putString(KEY_BRAND_COLLAB_LEVEL12_PORTFOLIO_BIO, portfolioBio)
            .putBoolean(KEY_BRAND_COLLAB_LEVEL12_COMPLETED, isCompleted)
            .apply()
    }

    fun getBrandCollabLevel12Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "completed" to prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL12_COMPLETED, false),
            "step" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL12_STEP, 1),
            "module" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL12_MODULE, 1),
            "portfolioName" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL12_PORTFOLIO_NAME, "Creator") ?: "Creator"),
            "portfolioBio" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL12_PORTFOLIO_BIO, "Tech & Lifestyle Creator building high impact brand content.") ?: "Tech & Lifestyle Creator building high impact brand content.")
        )
    }

    // Phase 14 Level 13 Preference Helpers
    fun isBrandCollabLevel13Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL13_COMPLETED, false)
    }

    fun saveBrandCollabLevel13State(
        context: Context,
        step: Int,
        module: Int = 1,
        monthlyGoal: String = "₹50,000 Goal",
        currentXp: Int = 1000,
        isCompleted: Boolean = false
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_BRAND_COLLAB_LEVEL13_STEP, step)
            .putInt(KEY_BRAND_COLLAB_LEVEL13_MODULE, module)
            .putString(KEY_BRAND_COLLAB_LEVEL13_MONTHLY_GOAL, monthlyGoal)
            .putInt(KEY_BRAND_COLLAB_LEVEL13_CURRENT_XP, currentXp)
            .putBoolean(KEY_BRAND_COLLAB_LEVEL13_COMPLETED, isCompleted)
            .apply()
    }

    fun getBrandCollabLevel13Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "completed" to prefs.getBoolean(KEY_BRAND_COLLAB_LEVEL13_COMPLETED, false),
            "step" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL13_STEP, 1),
            "module" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL13_MODULE, 1),
            "monthlyGoal" to (prefs.getString(KEY_BRAND_COLLAB_LEVEL13_MONTHLY_GOAL, "₹50,000 Goal") ?: "₹50,000 Goal"),
            "currentXp" to prefs.getInt(KEY_BRAND_COLLAB_LEVEL13_CURRENT_XP, 1000)
        )
    }

    // Phase 15 Final Level Creator Success Hub Preference Helpers
    fun isBrandCollabPhase15Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BRAND_COLLAB_PHASE15_COMPLETED, false)
    }

    fun saveBrandCollabPhase15State(
        context: Context,
        isCompleted: Boolean = true,
        goal: String = "₹1,00,000 Goal",
        xp: Int = 5000,
        certDate: String = "August 1, 2026",
        certId: String = "BCH-2026-LEGEND-8892"
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_BRAND_COLLAB_PHASE15_COMPLETED, isCompleted)
            .putString(KEY_BRAND_COLLAB_SUCCESS_GOAL, goal)
            .putInt(KEY_BRAND_COLLAB_SUCCESS_XP, xp)
            .putString(KEY_BRAND_COLLAB_CERTIFICATE_DATE, certDate)
            .putString(KEY_BRAND_COLLAB_CERTIFICATE_ID, certId)
            .apply()
    }

    fun getBrandCollabPhase15Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "completed" to prefs.getBoolean(KEY_BRAND_COLLAB_PHASE15_COMPLETED, false),
            "goal" to (prefs.getString(KEY_BRAND_COLLAB_SUCCESS_GOAL, "₹1,00,000 Goal") ?: "₹1,00,000 Goal"),
            "xp" to prefs.getInt(KEY_BRAND_COLLAB_SUCCESS_XP, 5000),
            "certDate" to (prefs.getString(KEY_BRAND_COLLAB_CERTIFICATE_DATE, "August 1, 2026") ?: "August 1, 2026"),
            "certId" to (prefs.getString(KEY_BRAND_COLLAB_CERTIFICATE_ID, "BCH-2026-LEGEND-8892") ?: "BCH-2026-LEGEND-8892")
        )
    }

    fun resetBrandCollabFullCourse(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val keysToRemove = prefs.all.keys.filter { it.startsWith("brand_collab_") }
        for (k in keysToRemove) {
            editor.remove(k)
        }
        editor.apply()
    }

    fun getMeeshoLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_MEESHO_CREATOR_LANG, "") ?: ""
    }

    fun setMeeshoLanguage(context: Context, lang: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_MEESHO_CREATOR_LANG, lang).apply()
    }

    fun isMeeshoOnboardingCompleted(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("meesho_onboarding_done", false)
    }

    fun isMeeshoLevel2Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("meesho_level2_completed", false)
    }

    fun setMeeshoLevel2Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("meesho_level2_completed", completed).apply()
    }

    fun isMeeshoLevel3Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("meesho_level3_completed", false)
    }

    fun setMeeshoLevel3Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("meesho_level3_completed", completed).apply()
    }

    fun isMeeshoLevel4Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("meesho_level4_completed", false)
    }

    fun setMeeshoLevel4Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("meesho_level4_completed", completed).apply()
    }

    fun isMeeshoLevel5Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("meesho_level5_completed", false)
    }

    fun setMeeshoLevel5Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("meesho_level5_completed", completed).apply()
    }

    fun isMeeshoLevel6Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("meesho_level6_completed", false)
    }

    fun setMeeshoLevel6Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("meesho_level6_completed", completed).apply()
    }

    fun isMeeshoLevel7Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("meesho_level7_completed", false)
    }

    fun setMeeshoLevel7Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("meesho_level7_completed", completed).apply()
    }

    fun isMeeshoLevel8Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("meesho_level8_completed", false)
    }

    fun setMeeshoLevel8Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("meesho_level8_completed", completed).apply()
    }

    fun isMeeshoLevel9Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("meesho_level9_completed", false)
    }

    fun setMeeshoLevel9Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("meesho_level9_completed", completed).apply()
    }

    fun isMeeshoLevel10Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("meesho_level10_completed", false)
    }

    fun setMeeshoLevel10Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("meesho_level10_completed", completed).apply()
    }

    fun isMeeshoLevel11Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("meesho_level11_completed", false)
    }

    fun setMeeshoLevel11Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("meesho_level11_completed", completed).apply()
    }

    fun saveMeeshoLevel11Data(
        context: Context,
        analyticsReportData: String,
        quizScore: Int,
        currentStepIndex: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("meesho_level11_report", analyticsReportData)
            .putInt("meesho_level11_quiz_score", quizScore)
            .putInt("meesho_level11_step", currentStepIndex)
            .apply()
    }

    fun getMeeshoLevel11Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "analyticsReportData" to (prefs.getString("meesho_level11_report", "") ?: ""),
            "quizScore" to prefs.getInt("meesho_level11_quiz_score", 0),
            "currentStep" to prefs.getInt("meesho_level11_step", 1)
        )
    }

    fun isMeeshoLevel13Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("meesho_level13_completed", false)
    }

    fun setMeeshoLevel13Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("meesho_level13_completed", completed).apply()
    }

    fun saveMeeshoLevel13Data(
        context: Context,
        creatorName: String,
        username: String,
        niche: String,
        bio: String,
        aboutMe: String,
        skillsMask: Int,
        currentStepIndex: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("meesho_level13_name", creatorName)
            .putString("meesho_level13_username", username)
            .putString("meesho_level13_niche", niche)
            .putString("meesho_level13_bio", bio)
            .putString("meesho_level13_aboutme", aboutMe)
            .putInt("meesho_level13_skills", skillsMask)
            .putInt("meesho_level13_step", currentStepIndex)
            .apply()
    }

    fun getMeeshoLevel13Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "creatorName" to (prefs.getString("meesho_level13_name", "") ?: ""),
            "username" to (prefs.getString("meesho_level13_username", "") ?: ""),
            "niche" to (prefs.getString("meesho_level13_niche", "Fashion & Lifestyle") ?: "Fashion & Lifestyle"),
            "bio" to (prefs.getString("meesho_level13_bio", "") ?: ""),
            "aboutMe" to (prefs.getString("meesho_level13_aboutme", "") ?: ""),
            "skillsMask" to prefs.getInt("meesho_level13_skills", 0),
            "currentStep" to prefs.getInt("meesho_level13_step", 1)
        )
    }

    fun isMeeshoLevel14Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("meesho_level14_completed", false)
    }

    fun setMeeshoLevel14Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("meesho_level14_completed", completed).apply()
    }

    fun saveMeeshoLevel14Data(
        context: Context,
        weeklyGoal: String,
        monthlyGoal: String,
        customGoal: String,
        customGoalProgress: Int,
        vaultNotes: String,
        currentStepIndex: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("meesho_level14_weekly_goal", weeklyGoal)
            .putString("meesho_level14_monthly_goal", monthlyGoal)
            .putString("meesho_level14_custom_goal", customGoal)
            .putInt("meesho_level14_custom_progress", customGoalProgress)
            .putString("meesho_level14_vault_notes", vaultNotes)
            .putInt("meesho_level14_step", currentStepIndex)
            .apply()
    }

    fun getMeeshoLevel14Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "weeklyGoal" to (prefs.getString("meesho_level14_weekly_goal", "Post 3 Reels & 2 Shorts") ?: "Post 3 Reels & 2 Shorts"),
            "monthlyGoal" to (prefs.getString("meesho_level14_monthly_goal", "Reach 5,000 Total Video Views") ?: "Reach 5,000 Total Video Views"),
            "customGoal" to (prefs.getString("meesho_level14_custom_goal", "Earn First ₹1,000 Affiliate Commission") ?: "Earn First ₹1,000 Affiliate Commission"),
            "customProgress" to prefs.getInt("meesho_level14_custom_progress", 60),
            "vaultNotes" to (prefs.getString("meesho_level14_vault_notes", "") ?: ""),
            "currentStep" to prefs.getInt("meesho_level14_step", 1)
        )
    }

    fun isMeeshoLevel15Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("meesho_level15_completed", false)
    }

    fun setMeeshoLevel15Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("meesho_level15_completed", completed).apply()
    }

    fun saveMeeshoLevel15Data(
        context: Context,
        customGoal: String,
        customGoalProgress: Int,
        vaultNotes: String,
        currentStepIndex: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("meesho_level15_custom_goal", customGoal)
            .putInt("meesho_level15_custom_progress", customGoalProgress)
            .putString("meesho_level15_vault_notes", vaultNotes)
            .putInt("meesho_level15_step", currentStepIndex)
            .apply()
    }

    fun getMeeshoLevel15Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "customGoal" to (prefs.getString("meesho_level15_custom_goal", "Reach ₹10,000 Creator Earnings") ?: "Reach ₹10,000 Creator Earnings"),
            "customProgress" to prefs.getInt("meesho_level15_custom_progress", 75),
            "vaultNotes" to (prefs.getString("meesho_level15_vault_notes", "") ?: ""),
            "currentStep" to prefs.getInt("meesho_level15_step", 1)
        )
    }

    fun resetMeeshoCourse(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        for (i in 1..15) {
            editor.remove("meesho_level${i}_completed")
            editor.remove("meesho_level${i}_step")
        }
        editor.apply()
    }

    fun isMeeshoLevel12Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("meesho_level12_completed", false)
    }

    fun setMeeshoLevel12Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("meesho_level12_completed", completed).apply()
    }

    fun saveMeeshoLevel12Data(
        context: Context,
        creatorIdentity: String,
        weeklyBusinessPlan: String,
        businessGoals: String,
        currentStepIndex: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("meesho_level12_identity", creatorIdentity)
            .putString("meesho_level12_plan", weeklyBusinessPlan)
            .putString("meesho_level12_goals", businessGoals)
            .putInt("meesho_level12_step", currentStepIndex)
            .apply()
    }

    fun getMeeshoLevel12Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "creatorIdentity" to (prefs.getString("meesho_level12_identity", "Fashion") ?: "Fashion"),
            "weeklyBusinessPlan" to (prefs.getString("meesho_level12_plan", "") ?: ""),
            "businessGoals" to (prefs.getString("meesho_level12_goals", "") ?: ""),
            "currentStep" to prefs.getInt("meesho_level12_step", 1)
        )
    }

    fun saveMeeshoLevel10Data(
        context: Context,
        calendarData: String,
        weeklyPlanData: String,
        currentStreak: Int,
        currentStepIndex: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("meesho_level10_calendar", calendarData)
            .putString("meesho_level10_weekly", weeklyPlanData)
            .putInt("meesho_level10_streak", currentStreak)
            .putInt("meesho_level10_step", currentStepIndex)
            .apply()
    }

    fun getMeeshoLevel10Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "calendarData" to (prefs.getString("meesho_level10_calendar", "") ?: ""),
            "weeklyPlanData" to (prefs.getString("meesho_level10_weekly", "") ?: ""),
            "currentStreak" to prefs.getInt("meesho_level10_streak", 5),
            "currentStep" to prefs.getInt("meesho_level10_step", 1)
        )
    }

    fun saveMeeshoLevel9Data(
        context: Context,
        plannerData: String,
        goalsData: String,
        currentStepIndex: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("meesho_level9_planner", plannerData)
            .putString("meesho_level9_goals", goalsData)
            .putInt("meesho_level9_step", currentStepIndex)
            .apply()
    }

    fun getMeeshoLevel9Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "plannerData" to (prefs.getString("meesho_level9_planner", "") ?: ""),
            "goalsData" to (prefs.getString("meesho_level9_goals", "") ?: ""),
            "currentStep" to prefs.getInt("meesho_level9_step", 1)
        )
    }

    fun saveMeeshoLevel8Data(
        context: Context,
        savedStories: String,
        conversationHistory: String,
        currentStepIndex: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("meesho_level8_stories", savedStories)
            .putString("meesho_level8_chat", conversationHistory)
            .putInt("meesho_level8_step", currentStepIndex)
            .apply()
    }

    fun getMeeshoLevel8Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "savedStories" to (prefs.getString("meesho_level8_stories", "") ?: ""),
            "conversationHistory" to (prefs.getString("meesho_level8_chat", "") ?: ""),
            "currentStep" to prefs.getInt("meesho_level8_step", 1)
        )
    }

    fun saveMeeshoLevel7Data(
        context: Context,
        savedCaptions: String,
        currentStepIndex: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("meesho_level7_captions", savedCaptions)
            .putInt("meesho_level7_step", currentStepIndex)
            .apply()
    }

    fun getMeeshoLevel7Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "savedCaptions" to (prefs.getString("meesho_level7_captions", "") ?: ""),
            "currentStep" to prefs.getInt("meesho_level7_step", 1)
        )
    }

    fun saveMeeshoLevel6Data(
        context: Context,
        savedScripts: String,
        savedHooks: String,
        currentStepIndex: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("meesho_level6_scripts", savedScripts)
            .putString("meesho_level6_hooks", savedHooks)
            .putInt("meesho_level6_step", currentStepIndex)
            .apply()
    }

    fun getMeeshoLevel6Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "savedScripts" to (prefs.getString("meesho_level6_scripts", "") ?: ""),
            "savedHooks" to (prefs.getString("meesho_level6_hooks", "") ?: ""),
            "currentStep" to prefs.getInt("meesho_level6_step", 1)
        )
    }

    fun saveMeeshoLevel5Data(
        context: Context,
        productHistory: String,
        practiceScore: Int,
        currentStepIndex: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("meesho_level5_history", productHistory)
            .putInt("meesho_level5_score", practiceScore)
            .putInt("meesho_level5_step", currentStepIndex)
            .apply()
    }

    fun getMeeshoLevel5Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "productHistory" to (prefs.getString("meesho_level5_history", "") ?: ""),
            "practiceScore" to prefs.getInt("meesho_level5_score", 0),
            "currentStep" to prefs.getInt("meesho_level5_step", 1)
        )
    }

    fun saveMeeshoLevel4Data(
        context: Context,
        researchHistory: String,
        practiceScore: Int,
        currentStepIndex: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("meesho_level4_history", researchHistory)
            .putInt("meesho_level4_score", practiceScore)
            .putInt("meesho_level4_step", currentStepIndex)
            .apply()
    }

    fun getMeeshoLevel4Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "researchHistory" to (prefs.getString("meesho_level4_history", "") ?: ""),
            "practiceScore" to prefs.getInt("meesho_level4_score", 0),
            "currentStep" to prefs.getInt("meesho_level4_step", 1)
        )
    }

    fun saveMeeshoLevel3Data(
        context: Context,
        completedLessons: String,
        practiceScore: Int,
        currentStepIndex: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("meesho_level3_lessons", completedLessons)
            .putInt("meesho_level3_score", practiceScore)
            .putInt("meesho_level3_step", currentStepIndex)
            .apply()
    }

    fun getMeeshoLevel3Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "completedLessons" to (prefs.getString("meesho_level3_lessons", "") ?: ""),
            "practiceScore" to prefs.getInt("meesho_level3_score", 0),
            "currentStep" to prefs.getInt("meesho_level3_step", 1)
        )
    }

    fun saveMeeshoLevel2Data(
        context: Context,
        installationStatus: String,
        loginStatus: String,
        creatorStatus: String,
        profileStatus: String,
        currentStepIndex: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("meesho_level2_install", installationStatus)
            .putString("meesho_level2_login", loginStatus)
            .putString("meesho_level2_creator", creatorStatus)
            .putString("meesho_level2_profile", profileStatus)
            .putInt("meesho_level2_step", currentStepIndex)
            .apply()
    }

    fun getMeeshoLevel2Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "installationStatus" to (prefs.getString("meesho_level2_install", "") ?: ""),
            "loginStatus" to (prefs.getString("meesho_level2_login", "") ?: ""),
            "creatorStatus" to (prefs.getString("meesho_level2_creator", "") ?: ""),
            "profileStatus" to (prefs.getString("meesho_level2_profile", "") ?: ""),
            "currentStep" to prefs.getInt("meesho_level2_step", 1)
        )
    }

    fun setMeeshoOnboardingCompleted(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("meesho_onboarding_done", completed).apply()
    }

    fun saveMeeshoLevel1Profile(
        context: Context,
        language: String,
        usedBefore: String,
        accountStatus: String,
        creatorStatus: String,
        platform: String,
        goal: String,
        learningLevel: String
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("meesho_onboarding_done", true)
            .putString(KEY_MEESHO_CREATOR_LANG, language)
            .putString("meesho_used_before", usedBefore)
            .putString("meesho_account_status", accountStatus)
            .putString("meesho_creator_status", creatorStatus)
            .putString("meesho_platform", platform)
            .putString("meesho_goal", goal)
            .putString("meesho_learning_level", learningLevel)
            .apply()
    }

    fun getMeeshoLevel1Profile(context: Context): Map<String, String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "language" to (prefs.getString(KEY_MEESHO_CREATOR_LANG, "Hinglish") ?: "Hinglish"),
            "usedBefore" to (prefs.getString("meesho_used_before", "Yes") ?: "Yes"),
            "accountStatus" to (prefs.getString("meesho_account_status", "Yes") ?: "Yes"),
            "creatorStatus" to (prefs.getString("meesho_creator_status", "No") ?: "No"),
            "platform" to (prefs.getString("meesho_platform", "Instagram") ?: "Instagram"),
            "goal" to (prefs.getString("meesho_goal", "Earn ₹10,000") ?: "Earn ₹10,000"),
            "learningLevel" to (prefs.getString("meesho_learning_level", "Beginner") ?: "Beginner")
        )
    }

    fun resetMeeshoCreatorCourse(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val keysToRemove = prefs.all.keys.filter { it.startsWith("meesho_") }
        for (k in keysToRemove) {
            editor.remove(k)
        }
        editor.apply()
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

    fun isWishlinkOnboardingDone(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_ONBOARDING_DONE, false)
    }

    fun setWishlinkOnboardingDone(context: Context, done: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WISHLINK_ONBOARDING_DONE, done).apply()
    }

    fun isWishlinkLevel1Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_LEVEL1_COMPLETED, false)
    }

    fun setWishlinkLevel1Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WISHLINK_LEVEL1_COMPLETED, completed).apply()
    }

    fun saveWishlinkLevel1Profile(
        context: Context,
        heardBefore: String,
        hasAccount: String,
        platform: String,
        niche: String,
        goal: String,
        learningLevel: String
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_LEVEL1_COMPLETED, true)
            .putString(KEY_WISHLINK_HEARD_BEFORE, heardBefore)
            .putString(KEY_WISHLINK_HAS_ACCOUNT, hasAccount)
            .putString(KEY_WISHLINK_PLATFORMS, platform)
            .putString(KEY_WISHLINK_NICHE, niche)
            .putString(KEY_WISHLINK_GOAL, goal)
            .putString(KEY_WISHLINK_LEARNING_LEVEL, learningLevel)
            .apply()
    }

    fun getWishlinkLevel1Profile(context: Context): Map<String, String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "heardBefore" to (prefs.getString(KEY_WISHLINK_HEARD_BEFORE, "") ?: ""),
            "hasAccount" to (prefs.getString(KEY_WISHLINK_HAS_ACCOUNT, "") ?: ""),
            "platform" to (prefs.getString(KEY_WISHLINK_PLATFORMS, "Instagram") ?: "Instagram"),
            "niche" to (prefs.getString(KEY_WISHLINK_NICHE, "Fashion") ?: "Fashion"),
            "goal" to (prefs.getString(KEY_WISHLINK_GOAL, "Earn First ₹100") ?: "Earn First ₹100"),
            "learningLevel" to (prefs.getString(KEY_WISHLINK_LEARNING_LEVEL, "Beginner") ?: "Beginner")
        )
    }

    fun isWishlinkLevel2Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_LEVEL2_COMPLETED, false)
    }

    fun setWishlinkLevel2Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WISHLINK_LEVEL2_COMPLETED, completed).apply()
    }

    fun saveWishlinkLevel2Data(
        context: Context,
        isInstalled: Boolean,
        accountStatus: String,
        platformConnected: String,
        storeStatus: String,
        progress: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_LEVEL2_COMPLETED, true)
            .putBoolean(KEY_WISHLINK_INSTALLED, isInstalled)
            .putString(KEY_WISHLINK_ACCOUNT_STATUS, accountStatus)
            .putString(KEY_WISHLINK_PLATFORM_CONNECTED, platformConnected)
            .putString(KEY_WISHLINK_STORE_STATUS, storeStatus)
            .putInt(KEY_WISHLINK_PROGRESS, progress)
            .apply()
    }

    fun getWishlinkLevel2Data(context: Context): Map<String, Any> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return mapOf(
            "isInstalled" to prefs.getBoolean(KEY_WISHLINK_INSTALLED, true),
            "accountStatus" to (prefs.getString(KEY_WISHLINK_ACCOUNT_STATUS, "Created") ?: "Created"),
            "platformConnected" to (prefs.getString(KEY_WISHLINK_PLATFORM_CONNECTED, "Instagram") ?: "Instagram"),
            "storeStatus" to (prefs.getString(KEY_WISHLINK_STORE_STATUS, "Configured") ?: "Configured"),
            "progress" to prefs.getInt(KEY_WISHLINK_PROGRESS, 8)
        )
    }

    fun isWishlinkLevel3Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_LEVEL3_COMPLETED, false)
    }

    fun setWishlinkLevel3Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WISHLINK_LEVEL3_COMPLETED, completed).apply()
    }

    fun saveWishlinkLevel3Data(
        context: Context,
        score: Int,
        progress: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_LEVEL3_COMPLETED, true)
            .putInt(KEY_WISHLINK_LEVEL3_SCORE, score)
            .putInt(KEY_WISHLINK_PROGRESS, progress)
            .apply()
    }

    fun isWishlinkLevel4Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_LEVEL4_COMPLETED, false)
    }

    fun setWishlinkLevel4Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WISHLINK_LEVEL4_COMPLETED, completed).apply()
    }

    fun saveWishlinkLevel4Data(
        context: Context,
        score: Int,
        linksCount: Int,
        progress: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_LEVEL4_COMPLETED, true)
            .putInt(KEY_WISHLINK_LEVEL4_SCORE, score)
            .putInt(KEY_WISHLINK_LEVEL4_GENERATED_LINKS, linksCount)
            .putInt(KEY_WISHLINK_PROGRESS, progress)
            .apply()
    }

    fun isWishlinkLevel5Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_LEVEL5_COMPLETED, false)
    }

    fun setWishlinkLevel5Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WISHLINK_LEVEL5_COMPLETED, completed).apply()
    }

    fun saveWishlinkLevel5Data(
        context: Context,
        score: Int,
        storeStyle: String,
        auditScore: Int,
        progress: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_LEVEL5_COMPLETED, true)
            .putInt(KEY_WISHLINK_LEVEL5_SCORE, score)
            .putString(KEY_WISHLINK_LEVEL5_STORE_STYLE, storeStyle)
            .putInt(KEY_WISHLINK_LEVEL5_AUDIT_SCORE, auditScore)
            .putInt(KEY_WISHLINK_PROGRESS, progress)
            .apply()
    }

    fun isWishlinkLevel6Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_LEVEL6_COMPLETED, false)
    }

    fun setWishlinkLevel6Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WISHLINK_LEVEL6_COMPLETED, completed).apply()
    }

    fun saveWishlinkLevel6Data(
        context: Context,
        score: Int,
        researchCount: Int,
        progress: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_LEVEL6_COMPLETED, true)
            .putInt(KEY_WISHLINK_LEVEL6_SCORE, score)
            .putInt(KEY_WISHLINK_LEVEL6_RESEARCH_COUNT, researchCount)
            .putInt(KEY_WISHLINK_PROGRESS, progress)
            .apply()
    }

    fun isWishlinkLevel7Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_LEVEL7_COMPLETED, false)
    }

    fun setWishlinkLevel7Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WISHLINK_LEVEL7_COMPLETED, completed).apply()
    }

    fun saveWishlinkLevel7Data(
        context: Context,
        score: Int,
        funnelCount: Int,
        progress: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_LEVEL7_COMPLETED, true)
            .putInt(KEY_WISHLINK_LEVEL7_SCORE, score)
            .putInt(KEY_WISHLINK_LEVEL7_FUNNEL_COUNT, funnelCount)
            .putInt(KEY_WISHLINK_PROGRESS, progress)
            .apply()
    }

    fun isWishlinkLevel8Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_LEVEL8_COMPLETED, false)
    }

    fun setWishlinkLevel8Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WISHLINK_LEVEL8_COMPLETED, completed).apply()
    }

    fun saveWishlinkLevel8Data(
        context: Context,
        score: Int,
        healthScore: Int,
        progress: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_LEVEL8_COMPLETED, true)
            .putInt(KEY_WISHLINK_LEVEL8_SCORE, score)
            .putInt(KEY_WISHLINK_LEVEL8_HEALTH_SCORE, healthScore)
            .putInt(KEY_WISHLINK_PROGRESS, progress)
            .apply()
    }

    fun isWishlinkLevel9Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_LEVEL9_COMPLETED, false)
    }

    fun setWishlinkLevel9Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WISHLINK_LEVEL9_COMPLETED, completed).apply()
    }

    fun saveWishlinkLevel9Data(
        context: Context,
        score: Int,
        planScore: Int,
        progress: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_LEVEL9_COMPLETED, true)
            .putInt(KEY_WISHLINK_LEVEL9_SCORE, score)
            .putInt(KEY_WISHLINK_LEVEL9_PLAN_SCORE, planScore)
            .putInt(KEY_WISHLINK_PROGRESS, progress)
            .apply()
    }

    fun isWishlinkLevel10Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_LEVEL10_COMPLETED, false)
    }

    fun setWishlinkLevel10Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WISHLINK_LEVEL10_COMPLETED, completed).apply()
    }

    fun saveWishlinkLevel10Data(
        context: Context,
        score: Int,
        streak: Int,
        progress: Int,
        calendarJson: String,
        weeklyPlanJson: String
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_LEVEL10_COMPLETED, true)
            .putInt(KEY_WISHLINK_LEVEL10_SCORE, score)
            .putInt(KEY_WISHLINK_LEVEL10_STREAK, streak)
            .putInt(KEY_WISHLINK_PROGRESS, progress)
            .putString(KEY_WISHLINK_LEVEL10_CALENDAR_JSON, calendarJson)
            .putString(KEY_WISHLINK_LEVEL10_WEEKLY_PLAN_JSON, weeklyPlanJson)
            .apply()
    }

    fun getWishlinkLevel10Streak(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_WISHLINK_LEVEL10_STREAK, 7)
    }

    fun saveWishlinkLevel10State(
        context: Context,
        streak: Int,
        calendarJson: String,
        weeklyPlanJson: String
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_WISHLINK_LEVEL10_STREAK, streak)
            .putString(KEY_WISHLINK_LEVEL10_CALENDAR_JSON, calendarJson)
            .putString(KEY_WISHLINK_LEVEL10_WEEKLY_PLAN_JSON, weeklyPlanJson)
            .apply()
    }

    fun getWishlinkLevel10CalendarJson(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_WISHLINK_LEVEL10_CALENDAR_JSON, "") ?: ""
    }

    fun getWishlinkLevel10WeeklyPlanJson(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_WISHLINK_LEVEL10_WEEKLY_PLAN_JSON, "") ?: ""
    }

    fun isWishlinkLevel11Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_LEVEL11_COMPLETED, false)
    }

    fun setWishlinkLevel11Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WISHLINK_LEVEL11_COMPLETED, completed).apply()
    }

    fun saveWishlinkLevel11Data(
        context: Context,
        score: Int,
        progress: Int,
        packageJson: String
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_LEVEL11_COMPLETED, true)
            .putInt(KEY_WISHLINK_LEVEL11_SCORE, score)
            .putInt(KEY_WISHLINK_PROGRESS, progress)
            .putString(KEY_WISHLINK_LEVEL11_PACKAGE_JSON, packageJson)
            .apply()
    }

    fun getWishlinkLevel11PackageJson(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_WISHLINK_LEVEL11_PACKAGE_JSON, "") ?: ""
    }

    fun isWishlinkLevel12Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_LEVEL12_COMPLETED, false)
    }

    fun setWishlinkLevel12Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WISHLINK_LEVEL12_COMPLETED, completed).apply()
    }

    fun saveWishlinkLevel12Data(
        context: Context,
        score: Int,
        progress: Int,
        planJson: String,
        healthScore: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_LEVEL12_COMPLETED, true)
            .putInt(KEY_WISHLINK_LEVEL12_SCORE, score)
            .putInt(KEY_WISHLINK_PROGRESS, progress)
            .putString(KEY_WISHLINK_LEVEL12_PLAN_JSON, planJson)
            .putInt(KEY_WISHLINK_LEVEL12_HEALTH_SCORE, healthScore)
            .apply()
    }

    fun getWishlinkLevel12PlanJson(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_WISHLINK_LEVEL12_PLAN_JSON, "") ?: ""
    }

    fun isWishlinkLevel13Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_LEVEL13_COMPLETED, false)
    }

    fun setWishlinkLevel13Completed(context: Context, completed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WISHLINK_LEVEL13_COMPLETED, completed).apply()
    }

    fun saveWishlinkLevel13Data(
        context: Context,
        score: Int,
        progress: Int,
        portfolioJson: String,
        mediaKitJson: String
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_LEVEL13_COMPLETED, true)
            .putInt(KEY_WISHLINK_LEVEL13_SCORE, score)
            .putInt(KEY_WISHLINK_PROGRESS, progress)
            .putString(KEY_WISHLINK_LEVEL13_PORTFOLIO_JSON, portfolioJson)
            .putString(KEY_WISHLINK_LEVEL13_MEDIA_KIT_JSON, mediaKitJson)
            .apply()
    }

    fun getWishlinkLevel13PortfolioJson(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_WISHLINK_LEVEL13_PORTFOLIO_JSON, "") ?: ""
    }

    fun getWishlinkLevel13MediaKitJson(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_WISHLINK_LEVEL13_MEDIA_KIT_JSON, "") ?: ""
    }

    fun isWishlinkLevel14Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_LEVEL14_COMPLETED, false)
    }

    fun getWishlinkLevel14Score(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_WISHLINK_LEVEL14_SCORE, 0)
    }

    fun completeWishlinkLevel14(context: Context, score: Int, goalsJson: String, vaultJson: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val progress = Math.max(prefs.getInt(KEY_WISHLINK_PROGRESS, 0), 98)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_LEVEL14_COMPLETED, true)
            .putInt(KEY_WISHLINK_LEVEL14_SCORE, score)
            .putInt(KEY_WISHLINK_PROGRESS, progress)
            .putString(KEY_WISHLINK_LEVEL14_GOALS_JSON, goalsJson)
            .putString(KEY_WISHLINK_LEVEL14_VAULT_JSON, vaultJson)
            .apply()
    }

    fun saveWishlinkLevel14Data(context: Context, goalsJson: String, vaultJson: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_WISHLINK_LEVEL14_GOALS_JSON, goalsJson)
            .putString(KEY_WISHLINK_LEVEL14_VAULT_JSON, vaultJson)
            .apply()
    }

    fun getWishlinkLevel14GoalsJson(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_WISHLINK_LEVEL14_GOALS_JSON, "") ?: ""
    }

    fun getWishlinkLevel14VaultJson(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_WISHLINK_LEVEL14_VAULT_JSON, "") ?: ""
    }

    fun isWishlinkLevel15Completed(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_WISHLINK_LEVEL15_COMPLETED, false)
    }

    fun getWishlinkLevel15Score(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_WISHLINK_LEVEL15_SCORE, 0)
    }

    fun completeWishlinkLevel15(context: Context, score: Int, certJson: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_LEVEL15_COMPLETED, true)
            .putInt(KEY_WISHLINK_LEVEL15_SCORE, score)
            .putInt(KEY_WISHLINK_PROGRESS, 100)
            .putString(KEY_WISHLINK_LEVEL15_CERTIFICATE_JSON, certJson)
            .apply()
    }

    fun getWishlinkLevel15CertificateJson(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_WISHLINK_LEVEL15_CERTIFICATE_JSON, "") ?: ""
    }

    fun resetWishlinkLevel1Data(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_WISHLINK_ONBOARDING_DONE, false)
            .putBoolean(KEY_WISHLINK_LEVEL1_COMPLETED, false)
            .putBoolean(KEY_WISHLINK_LEVEL2_COMPLETED, false)
            .putBoolean(KEY_WISHLINK_LEVEL3_COMPLETED, false)
            .putBoolean(KEY_WISHLINK_LEVEL4_COMPLETED, false)
            .putBoolean(KEY_WISHLINK_LEVEL5_COMPLETED, false)
            .putBoolean(KEY_WISHLINK_LEVEL6_COMPLETED, false)
            .putBoolean(KEY_WISHLINK_LEVEL7_COMPLETED, false)
            .putBoolean(KEY_WISHLINK_LEVEL8_COMPLETED, false)
            .putBoolean(KEY_WISHLINK_LEVEL9_COMPLETED, false)
            .putBoolean(KEY_WISHLINK_LEVEL10_COMPLETED, false)
            .putBoolean(KEY_WISHLINK_LEVEL11_COMPLETED, false)
            .putBoolean(KEY_WISHLINK_LEVEL12_COMPLETED, false)
            .putBoolean(KEY_WISHLINK_LEVEL13_COMPLETED, false)
            .putBoolean(KEY_WISHLINK_LEVEL14_COMPLETED, false)
            .putBoolean(KEY_WISHLINK_LEVEL15_COMPLETED, false)
            .putInt(KEY_WISHLINK_LEVEL3_SCORE, 0)
            .putInt(KEY_WISHLINK_LEVEL4_SCORE, 0)
            .putInt(KEY_WISHLINK_LEVEL4_GENERATED_LINKS, 0)
            .putInt(KEY_WISHLINK_LEVEL5_SCORE, 0)
            .putString(KEY_WISHLINK_LEVEL5_STORE_STYLE, "")
            .putInt(KEY_WISHLINK_LEVEL5_AUDIT_SCORE, 0)
            .putInt(KEY_WISHLINK_LEVEL6_SCORE, 0)
            .putInt(KEY_WISHLINK_LEVEL6_RESEARCH_COUNT, 0)
            .putInt(KEY_WISHLINK_LEVEL7_SCORE, 0)
            .putInt(KEY_WISHLINK_LEVEL7_FUNNEL_COUNT, 0)
            .putInt(KEY_WISHLINK_LEVEL8_SCORE, 0)
            .putInt(KEY_WISHLINK_LEVEL8_HEALTH_SCORE, 0)
            .putInt(KEY_WISHLINK_LEVEL9_SCORE, 0)
            .putInt(KEY_WISHLINK_LEVEL9_PLAN_SCORE, 0)
            .putInt(KEY_WISHLINK_LEVEL10_SCORE, 0)
            .putInt(KEY_WISHLINK_LEVEL10_STREAK, 7)
            .putString(KEY_WISHLINK_LEVEL10_CALENDAR_JSON, "")
            .putString(KEY_WISHLINK_LEVEL10_WEEKLY_PLAN_JSON, "")
            .putInt(KEY_WISHLINK_LEVEL11_SCORE, 0)
            .putString(KEY_WISHLINK_LEVEL11_PACKAGE_JSON, "")
            .putString(KEY_WISHLINK_LANG, "")
            .putString(KEY_WISHLINK_HEARD_BEFORE, "")
            .putString(KEY_WISHLINK_HAS_ACCOUNT, "")
            .putString(KEY_WISHLINK_PLATFORMS, "")
            .putString(KEY_WISHLINK_NICHE, "")
            .putString(KEY_WISHLINK_GOAL, "")
            .putString(KEY_WISHLINK_LEARNING_LEVEL, "")
            .putBoolean(KEY_WISHLINK_INSTALLED, false)
            .putString(KEY_WISHLINK_ACCOUNT_STATUS, "")
            .putString(KEY_WISHLINK_PLATFORM_CONNECTED, "")
            .putString(KEY_WISHLINK_STORE_STATUS, "")
            .putInt(KEY_WISHLINK_PROGRESS, 0)
            .putInt(KEY_WISHLINK_STEP_INDEX, 0)
            .putStringSet(KEY_WISHLINK_COMPLETED_STEPS, emptySet())
            .apply()
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
