package com.example.core

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.reports.ReportLanguage

/**
 * MASTER BUG FIX PHASE — Language Engine & Localization Manager
 * Handles full real-time localization across English, HinEnglish (Hinglish), and Hindi.
 * Fully reactive state for Jetpack Compose, persisting across app restarts and syncing across all tools.
 */
object LanguageEngine {
    private const val PREF_NAME = "language_engine_prefs"
    private const val KEY_LANG = "selected_language"

    private val _currentLanguageState = mutableStateOf(ReportLanguage.ENGLISH)
    val currentLanguageState: State<ReportLanguage> get() = _currentLanguageState

    var currentLanguage: ReportLanguage
        get() = _currentLanguageState.value
        set(value) {
            _currentLanguageState.value = value
            AiSessionMemory.currentLanguage = value
        }

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedLangName = prefs.getString(KEY_LANG, ReportLanguage.ENGLISH.name) ?: ReportLanguage.ENGLISH.name
        val lang = try {
            ReportLanguage.valueOf(savedLangName)
        } catch (e: Exception) {
            ReportLanguage.ENGLISH
        }
        _currentLanguageState.value = lang
        AiSessionMemory.currentLanguage = lang
    }

    fun setLanguage(context: Context, language: ReportLanguage) {
        _currentLanguageState.value = language
        AiSessionMemory.currentLanguage = language
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANG, language.name).apply()

        // Sync with CreatorAcademyPrefs as well
        val currentSetup = CreatorAcademyPrefs.getSetupData(context)
        val prefLangStr = when (language) {
            ReportLanguage.HINDI -> "Hindi"
            ReportLanguage.HINGLISH -> "HinEnglish"
            ReportLanguage.ENGLISH -> "English"
        }
        if (currentSetup.preferredLanguage != prefLangStr) {
            CreatorAcademyPrefs.saveSetupData(context, currentSetup.copy(preferredLanguage = prefLangStr))
        }
    }

    fun get(key: String): String {
        val lang = _currentLanguageState.value
        return translations[key]?.get(lang) 
            ?: translations[key]?.get(ReportLanguage.ENGLISH) 
            ?: key
    }

    private val translations: Map<String, Map<ReportLanguage, String>> = mapOf(
        // Buttons
        "btn_continue" to mapOf(
            ReportLanguage.ENGLISH to "Continue",
            ReportLanguage.HINGLISH to "Aage Badhein",
            ReportLanguage.HINDI to "आगे बढ़ें"
        ),
        "btn_back" to mapOf(
            ReportLanguage.ENGLISH to "Back",
            ReportLanguage.HINGLISH to "Peeche Jayein",
            ReportLanguage.HINDI to "पीछे जाएँ"
        ),
        "btn_save" to mapOf(
            ReportLanguage.ENGLISH to "Save",
            ReportLanguage.HINGLISH to "Save Karein",
            ReportLanguage.HINDI to "सहेजें"
        ),
        "btn_cancel" to mapOf(
            ReportLanguage.ENGLISH to "Cancel",
            ReportLanguage.HINGLISH to "Cancel",
            ReportLanguage.HINDI to "रद्द करें"
        ),
        "btn_confirm" to mapOf(
            ReportLanguage.ENGLISH to "Confirm",
            ReportLanguage.HINGLISH to "Confirm Karein",
            ReportLanguage.HINDI to "पुष्टि करें"
        ),
        "btn_done" to mapOf(
            ReportLanguage.ENGLISH to "Yes, Done!",
            ReportLanguage.HINGLISH to "Haan, Ho gaya!",
            ReportLanguage.HINDI to "हाँ, हो गया!"
        ),
        "btn_not_yet" to mapOf(
            ReportLanguage.ENGLISH to "Not Yet",
            ReportLanguage.HINGLISH to "Abhi nahi",
            ReportLanguage.HINDI to "अभी नहीं"
        ),
        "btn_switch_workspace" to mapOf(
            ReportLanguage.ENGLISH to "Switch Workspace",
            ReportLanguage.HINGLISH to "Workspace Badlein",
            ReportLanguage.HINDI to "कार्यस्थान बदलें"
        ),
        "btn_clear_history" to mapOf(
            ReportLanguage.ENGLISH to "Clear History",
            ReportLanguage.HINGLISH to "History Clear Karein",
            ReportLanguage.HINDI to "इतिहास साफ़ करें"
        ),
        "btn_notify_release" to mapOf(
            ReportLanguage.ENGLISH to "Notify Me On Release",
            ReportLanguage.HINGLISH to "Release par notify karein",
            ReportLanguage.HINDI to "रिलीज़ होने पर सूचित करें"
        ),
        "btn_analyze_reel" to mapOf(
            ReportLanguage.ENGLISH to "Analyze Reel",
            ReportLanguage.HINGLISH to "Reel Analyze Karein",
            ReportLanguage.HINDI to "रील का विश्लेषण करें"
        ),
        "btn_paste_link" to mapOf(
            ReportLanguage.ENGLISH to "Paste Link",
            ReportLanguage.HINGLISH to "Link Paste Karein",
            ReportLanguage.HINDI to "लिंक पेस्ट करें"
        ),
        "btn_clear" to mapOf(
            ReportLanguage.ENGLISH to "Clear",
            ReportLanguage.HINGLISH to "Clear",
            ReportLanguage.HINDI to "साफ़ करें"
        ),

        // Menus / Nav Tabs
        "tab_home" to mapOf(
            ReportLanguage.ENGLISH to "Home",
            ReportLanguage.HINGLISH to "Home",
            ReportLanguage.HINDI to "होम"
        ),
        "tab_academy" to mapOf(
            ReportLanguage.ENGLISH to "Academy",
            ReportLanguage.HINGLISH to "Academy",
            ReportLanguage.HINDI to "अकादमी"
        ),
        "tab_history" to mapOf(
            ReportLanguage.ENGLISH to "History",
            ReportLanguage.HINGLISH to "History",
            ReportLanguage.HINDI to "इतिहास"
        ),
        "tab_profile" to mapOf(
            ReportLanguage.ENGLISH to "Profile",
            ReportLanguage.HINGLISH to "Profile",
            ReportLanguage.HINDI to "प्रोफ़ाइल"
        ),

        // Home Screen
        "home_title" to mapOf(
            ReportLanguage.ENGLISH to "ViralToolAI",
            ReportLanguage.HINGLISH to "ViralToolAI",
            ReportLanguage.HINDI to "वायरलटूल AI"
        ),
        "home_subtitle" to mapOf(
            ReportLanguage.ENGLISH to "AI Social & Commerce Intelligence",
            ReportLanguage.HINGLISH to "AI Social & Commerce Intelligence",
            ReportLanguage.HINDI to "AI सोशल और कॉमर्स इंटेलिजेंस"
        ),
        "home_input_placeholder" to mapOf(
            ReportLanguage.ENGLISH to "Paste Instagram Reel, YouTube Short, or Product Link...",
            ReportLanguage.HINGLISH to "Instagram Reel, YouTube Short, ya Product Link paste karein...",
            ReportLanguage.HINDI to "इंस्टाग्राम रील, यूट्यूब शॉर्ट, या प्रोडक्ट लिंक पेस्ट करें..."
        ),
        "home_recent_analyses" to mapOf(
            ReportLanguage.ENGLISH to "Recent Analyses",
            ReportLanguage.HINGLISH to "Recent Analyses",
            ReportLanguage.HINDI to "हाल के विश्लेषण"
        ),
        "home_trending_products" to mapOf(
            ReportLanguage.ENGLISH to "Trending Products",
            ReportLanguage.HINGLISH to "Trending Products",
            ReportLanguage.HINDI to "ट्रेंडिंग प्रोडक्ट्स"
        ),
        "home_creator_studio" to mapOf(
            ReportLanguage.ENGLISH to "Creator Studio",
            ReportLanguage.HINGLISH to "Creator Studio",
            ReportLanguage.HINDI to "क्रिएटर स्टूडियो"
        ),
        "home_commerce_hub" to mapOf(
            ReportLanguage.ENGLISH to "Commerce Hub",
            ReportLanguage.HINGLISH to "Commerce Hub",
            ReportLanguage.HINDI to "कॉमर्स हब"
        ),
        "home_trend_intelligence" to mapOf(
            ReportLanguage.ENGLISH to "Trend Intelligence",
            ReportLanguage.HINGLISH to "Trend Intelligence",
            ReportLanguage.HINDI to "ट्रेंड इंटेलिजेंस"
        ),
        "home_content_planner" to mapOf(
            ReportLanguage.ENGLISH to "Content Planner",
            ReportLanguage.HINGLISH to "Content Planner",
            ReportLanguage.HINDI to "कंटेंट प्लानर"
        ),
        "home_campaign_hub" to mapOf(
            ReportLanguage.ENGLISH to "Campaign Hub",
            ReportLanguage.HINGLISH to "Campaign Hub",
            ReportLanguage.HINDI to "अभियान हब"
        ),
        "home_quick_tools" to mapOf(
            ReportLanguage.ENGLISH to "Quick Tools",
            ReportLanguage.HINGLISH to "Quick Tools",
            ReportLanguage.HINDI to "त्वरित टूल"
        ),
        "home_hinglish_badge" to mapOf(
            ReportLanguage.ENGLISH to "✨ Explained in simple language • No Login Required",
            ReportLanguage.HINGLISH to "✨ Simple Hinglish me samjhaya gaya • No Login Required",
            ReportLanguage.HINDI to "✨ सरल हिंदी में समझाया गया • कोई लॉगिन आवश्यक नहीं"
        ),

        // Dialogs & Headers
        "title_choose_experience" to mapOf(
            ReportLanguage.ENGLISH to "Choose Your Experience",
            ReportLanguage.HINGLISH to "Apna Experience chunein",
            ReportLanguage.HINDI to "अपना अनुभव चुनें"
        ),
        "remember_choice" to mapOf(
            ReportLanguage.ENGLISH to "Remember my choice",
            ReportLanguage.HINGLISH to "Mera choice yaad rakhein",
            ReportLanguage.HINDI to "मेरा विकल्प याद रखें"
        ),
        "title_clear_history" to mapOf(
            ReportLanguage.ENGLISH to "Clear All History?",
            ReportLanguage.HINGLISH to "Pura History clear karein?",
            ReportLanguage.HINDI to "सभी इतिहास साफ़ करें?"
        ),

        // Mentor Mode & Creator Academy
        "title_mentor_mode" to mapOf(
            ReportLanguage.ENGLISH to "AI Mentor Mode",
            ReportLanguage.HINGLISH to "AI Mentor Mode",
            ReportLanguage.HINDI to "AI मेंटर मोड"
        ),
        "subtitle_mentor_mode" to mapOf(
            ReportLanguage.ENGLISH to "One task at a time • Zero overwhelm",
            ReportLanguage.HINGLISH to "Ek time par ek task • No stress",
            ReportLanguage.HINDI to "एक समय में एक कार्य • कोई तनाव नहीं"
        ),
        "todays_task" to mapOf(
            ReportLanguage.ENGLISH to "TODAY'S MISSION",
            ReportLanguage.HINGLISH to "AAJ KA MISSION",
            ReportLanguage.HINDI to "आज का मिशन"
        ),
        "mentor_tip" to mapOf(
            ReportLanguage.ENGLISH to "Take your time! Consistency beats speed.",
            ReportLanguage.HINGLISH to "Aaram se karein! Consistency sabse zaroori hai.",
            ReportLanguage.HINDI to "अपना समय लें! निरंतरता गति से बेहतर है।"
        ),
        "task_completed_toast" to mapOf(
            ReportLanguage.ENGLISH to "Task Completed! +100 XP Earned!",
            ReportLanguage.HINGLISH to "Task poora hua! +100 XP mil gaye!",
            ReportLanguage.HINDI to "कार्य पूर्ण! +100 XP अर्जित किए!"
        ),
        "title_creator_academy" to mapOf(
            ReportLanguage.ENGLISH to "Creator Academy",
            ReportLanguage.HINGLISH to "Creator Academy",
            ReportLanguage.HINDI to "क्रिएटर अकादमी"
        ),
        "choose_learning_path" to mapOf(
            ReportLanguage.ENGLISH to "CHOOSE LEARNING PATH",
            ReportLanguage.HINGLISH to "LEARNING PATH CHUNEIN",
            ReportLanguage.HINDI to "सीखने का मार्ग चुनें"
        ),
        "path_instagram" to mapOf(
            ReportLanguage.ENGLISH to "Instagram Creator",
            ReportLanguage.HINGLISH to "Instagram Creator",
            ReportLanguage.HINDI to "इंस्टाग्राम क्रिएटर"
        ),
        "path_youtube" to mapOf(
            ReportLanguage.ENGLISH to "YouTube Creator",
            ReportLanguage.HINGLISH to "YouTube Creator",
            ReportLanguage.HINDI to "यूट्यूब क्रिएटर"
        ),
        "path_video_editing" to mapOf(
            ReportLanguage.ENGLISH to "Mobile Video Editing",
            ReportLanguage.HINGLISH to "Mobile Video Editing",
            ReportLanguage.HINDI to "मोबाइल वीडियो एडिटिंग"
        ),
        "personalized_roadmap" to mapOf(
            ReportLanguage.ENGLISH to "PERSONALIZED CREATOR ROADMAP",
            ReportLanguage.HINGLISH to "PERSONALIZED CREATOR ROADMAP",
            ReportLanguage.HINDI to "व्यक्तिगत क्रिएटर रोडमैप"
        ),
        "label_skill_level" to mapOf(
            ReportLanguage.ENGLISH to "Skill Level",
            ReportLanguage.HINGLISH to "Skill Level",
            ReportLanguage.HINDI to "कौशल स्तर"
        ),
        "label_primary_goal" to mapOf(
            ReportLanguage.ENGLISH to "Primary Goal",
            ReportLanguage.HINGLISH to "Main Goal",
            ReportLanguage.HINDI to "मुख्य लक्ष्य"
        ),
        "label_available_time" to mapOf(
            ReportLanguage.ENGLISH to "Available Time",
            ReportLanguage.HINGLISH to "Available Time",
            ReportLanguage.HINDI to "उपलब्ध समय"
        ),
        "label_preferred_language" to mapOf(
            ReportLanguage.ENGLISH to "Preferred Language",
            ReportLanguage.HINGLISH to "Preferred Language",
            ReportLanguage.HINDI to "पसंदीदा भाषा"
        ),
        "btn_done_task" to mapOf(
            ReportLanguage.ENGLISH to "YES, DONE!",
            ReportLanguage.HINGLISH to "HAAN, HO GAYA!",
            ReportLanguage.HINDI to "हाँ, हो गया!"
        ),
        "btn_explain_again" to mapOf(
            ReportLanguage.ENGLISH to "NOT YET - EXPLAIN AGAIN",
            ReportLanguage.HINGLISH to "ABHI NAHI - DIL SE SAMJHAO",
            ReportLanguage.HINDI to "अभी नहीं - पुनः समझाएँ"
        ),
        "btn_skip_now" to mapOf(
            ReportLanguage.ENGLISH to "SKIP FOR NOW",
            ReportLanguage.HINGLISH to "ABHI SKIP KAREIN",
            ReportLanguage.HINDI to "अभी छोड़ें"
        ),
        "label_coach_message" to mapOf(
            ReportLanguage.ENGLISH to "Coach Message",
            ReportLanguage.HINGLISH to "Coach Ka Message",
            ReportLanguage.HINDI to "कोच संदेश"
        ),
        "label_why_it_matters" to mapOf(
            ReportLanguage.ENGLISH to "Why It Matters",
            ReportLanguage.HINGLISH to "Yeh Zaroori Kyun Hai",
            ReportLanguage.HINDI to "यह क्यों महत्वपूर्ण है"
        ),
        "label_good_example" to mapOf(
            ReportLanguage.ENGLISH to "Good Example",
            ReportLanguage.HINGLISH to "Sahi Example",
            ReportLanguage.HINDI to "अच्छा उदाहरण"
        ),
        "label_bad_example" to mapOf(
            ReportLanguage.ENGLISH to "Bad Example",
            ReportLanguage.HINGLISH to "Galat Example",
            ReportLanguage.HINDI to "गलत उदाहरण"
        ),
        "label_pro_tip" to mapOf(
            ReportLanguage.ENGLISH to "Pro Tip",
            ReportLanguage.HINGLISH to "Pro Tip",
            ReportLanguage.HINDI to "प्रो टिप"
        ),
        "label_common_mistake" to mapOf(
            ReportLanguage.ENGLISH to "Common Mistake",
            ReportLanguage.HINGLISH to "Aam Galti",
            ReportLanguage.HINDI to "सामान्य गलती"
        ),
        "label_action_task" to mapOf(
            ReportLanguage.ENGLISH to "Action Task",
            ReportLanguage.HINGLISH to "Action Task",
            ReportLanguage.HINDI to "कार्य पद"
        ),
        "label_simpler_exp" to mapOf(
            ReportLanguage.ENGLISH to "Simpler Explanation",
            ReportLanguage.HINGLISH to "Aasan Bhasha Me",
            ReportLanguage.HINDI to "सरल व्याख्या"
        ),
        "label_extra_example" to mapOf(
            ReportLanguage.ENGLISH to "Extra Real Example",
            ReportLanguage.HINGLISH to "Real World Example",
            ReportLanguage.HINDI to "वास्तविक उदाहरण"
        ),
        "label_recommended_tool" to mapOf(
            ReportLanguage.ENGLISH to "Recommended Tool",
            ReportLanguage.HINGLISH to "Recommended Tool",
            ReportLanguage.HINDI to "अनुशंसित टूल"
        ),
        "btn_reset_progress" to mapOf(
            ReportLanguage.ENGLISH to "Reset Progress",
            ReportLanguage.HINGLISH to "Progress Reset Karein",
            ReportLanguage.HINDI to "प्रगति रीसेट करें"
        ),

        // Results Screen
        "result_title" to mapOf(
            ReportLanguage.ENGLISH to "AI Analysis Result",
            ReportLanguage.HINGLISH to "AI Analysis Result",
            ReportLanguage.HINDI to "AI विश्लेषण परिणाम"
        ),
        "result_viral_score" to mapOf(
            ReportLanguage.ENGLISH to "Viral Potential Score",
            ReportLanguage.HINGLISH to "Viral Potential Score",
            ReportLanguage.HINDI to "वायरल संभावना स्कोर"
        ),
        "result_content_breakdown" to mapOf(
            ReportLanguage.ENGLISH to "Content Breakdown",
            ReportLanguage.HINGLISH to "Content Breakdown",
            ReportLanguage.HINDI to "सामग्री विवरण"
        ),
        "result_ai_hooks" to mapOf(
            ReportLanguage.ENGLISH to "AI Hooks",
            ReportLanguage.HINGLISH to "AI Hooks",
            ReportLanguage.HINDI to "AI हुक्स"
        ),
        "result_captions" to mapOf(
            ReportLanguage.ENGLISH to "Captions",
            ReportLanguage.HINGLISH to "Captions",
            ReportLanguage.HINDI to "कैप्शन"
        ),
        "result_hashtags" to mapOf(
            ReportLanguage.ENGLISH to "Hashtag Stack",
            ReportLanguage.HINGLISH to "Hashtag Stack",
            ReportLanguage.HINDI to "हैशटैग समूह"
        ),
        "result_shopping_insights" to mapOf(
            ReportLanguage.ENGLISH to "Shopping Insights",
            ReportLanguage.HINGLISH to "Shopping Insights",
            ReportLanguage.HINDI to "शॉपिंग इनसाइट्स"
        ),
        "result_price_analysis" to mapOf(
            ReportLanguage.ENGLISH to "Price Analysis",
            ReportLanguage.HINGLISH to "Price Analysis",
            ReportLanguage.HINDI to "मूल्य विश्लेषण"
        ),
        "result_pros_cons" to mapOf(
            ReportLanguage.ENGLISH to "Pros & Cons",
            ReportLanguage.HINGLISH to "Pros & Cons",
            ReportLanguage.HINDI to "फायदे और नुकसान"
        ),
        "result_recommendations" to mapOf(
            ReportLanguage.ENGLISH to "Actionable Recommendations",
            ReportLanguage.HINGLISH to "Actionable Advice",
            ReportLanguage.HINDI to "कार्रवाई योग्य सिफारिशें"
        ),
        "btn_copy_captions" to mapOf(
            ReportLanguage.ENGLISH to "Copy Captions",
            ReportLanguage.HINGLISH to "Captions Copy Karein",
            ReportLanguage.HINDI to "कैप्शन कॉपी करें"
        ),
        "btn_save_history" to mapOf(
            ReportLanguage.ENGLISH to "Save to History",
            ReportLanguage.HINGLISH to "History me Save Karein",
            ReportLanguage.HINDI to "इतिहास में सहेजें"
        ),
        "btn_share_report" to mapOf(
            ReportLanguage.ENGLISH to "Share Report",
            ReportLanguage.HINGLISH to "Report Share Karein",
            ReportLanguage.HINDI to "रिपोर्ट साझा करें"
        ),
        "btn_reanalyze" to mapOf(
            ReportLanguage.ENGLISH to "Re-Analyze",
            ReportLanguage.HINGLISH to "Dubara Analyze Karein",
            ReportLanguage.HINDI to "पुनः विश्लेषण करें"
        ),

        // History Screen
        "history_title" to mapOf(
            ReportLanguage.ENGLISH to "Analysis History",
            ReportLanguage.HINGLISH to "Analysis History",
            ReportLanguage.HINDI to "विश्लेषण इतिहास"
        ),
        "history_search_hint" to mapOf(
            ReportLanguage.ENGLISH to "Search history...",
            ReportLanguage.HINGLISH to "History me khojein...",
            ReportLanguage.HINDI to "इतिहास में खोजें..."
        ),
        "history_filter_all" to mapOf(
            ReportLanguage.ENGLISH to "All Items",
            ReportLanguage.HINGLISH to "All Items",
            ReportLanguage.HINDI to "सभी आइटम"
        ),
        "history_filter_shopping" to mapOf(
            ReportLanguage.ENGLISH to "Shopping",
            ReportLanguage.HINGLISH to "Shopping",
            ReportLanguage.HINDI to "शॉपिंग"
        ),
        "history_filter_reels" to mapOf(
            ReportLanguage.ENGLISH to "Reels",
            ReportLanguage.HINGLISH to "Reels",
            ReportLanguage.HINDI to "रील्स"
        ),
        "history_empty" to mapOf(
            ReportLanguage.ENGLISH to "No history found",
            ReportLanguage.HINGLISH to "Koi history nahi mili",
            ReportLanguage.HINDI to "कोई इतिहास नहीं मिला"
        ),
        "history_clear_all" to mapOf(
            ReportLanguage.ENGLISH to "Clear All History",
            ReportLanguage.HINGLISH to "Puri History Clear Karein",
            ReportLanguage.HINDI to "सभी इतिहास साफ़ करें"
        ),

        // Settings / Profile Screen
        "profile_title" to mapOf(
            ReportLanguage.ENGLISH to "Profile & Settings",
            ReportLanguage.HINGLISH to "Profile & Settings",
            ReportLanguage.HINDI to "प्रोफ़ाइल और सेटिंग्स"
        ),
        "profile_creator_identity" to mapOf(
            ReportLanguage.ENGLISH to "CREATOR IDENTITY",
            ReportLanguage.HINGLISH to "CREATOR IDENTITY",
            ReportLanguage.HINDI to "क्रिएटर पहचान"
        ),
        "profile_language" to mapOf(
            ReportLanguage.ENGLISH to "LANGUAGE",
            ReportLanguage.HINGLISH to "LANGUAGE",
            ReportLanguage.HINDI to "भाषा"
        ),
        "profile_select_language" to mapOf(
            ReportLanguage.ENGLISH to "Select App Language",
            ReportLanguage.HINGLISH to "App Language Chunein",
            ReportLanguage.HINDI to "ऐप भाषा चुनें"
        ),
        "profile_clear_cache" to mapOf(
            ReportLanguage.ENGLISH to "Clear App Cache & Data",
            ReportLanguage.HINGLISH to "App Cache Clear Karein",
            ReportLanguage.HINDI to "ऐप कैश और डेटा साफ़ करें"
        ),
        "profile_version" to mapOf(
            ReportLanguage.ENGLISH to "App Version",
            ReportLanguage.HINGLISH to "App Version",
            ReportLanguage.HINDI to "ऐप संस्करण"
        ),
        "profile_developer" to mapOf(
            ReportLanguage.ENGLISH to "Developer Info",
            ReportLanguage.HINGLISH to "Developer Info",
            ReportLanguage.HINDI to "डेवलपर जानकारी"
        ),
        "profile_created_by" to mapOf(
            ReportLanguage.ENGLISH to "Created by Asit",
            ReportLanguage.HINGLISH to "Created by Asit",
            ReportLanguage.HINDI to "असित द्वारा निर्मित"
        ),
        "profile_built_mobile" to mapOf(
            ReportLanguage.ENGLISH to "Designed & Built Entirely on Mobile",
            ReportLanguage.HINGLISH to "Pura Mobile par banaya gaya hai",
            ReportLanguage.HINDI to "पूर्ण रूप से मोबाइल पर डिज़ाइन और निर्मित"
        ),

        // Shopping & Settings
        "title_shopping" to mapOf(
            ReportLanguage.ENGLISH to "Shopping Intelligence",
            ReportLanguage.HINGLISH to "Shopping Intelligence",
            ReportLanguage.HINDI to "शॉपिंग इंटेलिजेंस"
        ),
        "title_settings" to mapOf(
            ReportLanguage.ENGLISH to "Settings & Preferences",
            ReportLanguage.HINGLISH to "Settings & Preferences",
            ReportLanguage.HINDI to "सेटिंग्स और प्राथमिकताएँ"
        )
    )
}

