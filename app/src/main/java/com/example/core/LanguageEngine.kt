package com.example.core

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.reports.ReportLanguage

/**
 * MASTER HOTFIX PHASE 15A.1 — Language Engine & Localization Manager
 * Handles full real-time localization across English, HinEnglish, and Hindi.
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
    }

    fun get(key: String): String {
        val lang = _currentLanguageState.value
        return translations[key]?.get(lang) ?: translations[key]?.get(ReportLanguage.ENGLISH) ?: key
    }

    private val translations: Map<String, Map<ReportLanguage, String>> = mapOf(
        // Buttons
        "btn_continue" to mapOf(
            ReportLanguage.ENGLISH to "Continue",
            ReportLanguage.HINGLISH to "Continue karein",
            ReportLanguage.HINDI to "आगे बढ़ें"
        ),
        "btn_back" to mapOf(
            ReportLanguage.ENGLISH to "Back",
            ReportLanguage.HINGLISH to "Peeche jayein",
            ReportLanguage.HINDI to "पीछे जाएँ"
        ),
        "btn_save" to mapOf(
            ReportLanguage.ENGLISH to "Save",
            ReportLanguage.HINGLISH to "Save karein",
            ReportLanguage.HINDI to "सहेजें"
        ),
        "btn_cancel" to mapOf(
            ReportLanguage.ENGLISH to "Cancel",
            ReportLanguage.HINGLISH to "Cancel",
            ReportLanguage.HINDI to "रद्द करें"
        ),
        "btn_confirm" to mapOf(
            ReportLanguage.ENGLISH to "Confirm",
            ReportLanguage.HINGLISH to "Confirm karein",
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
            ReportLanguage.HINGLISH to "Workspace badlein",
            ReportLanguage.HINDI to "कार्यस्थान बदलें"
        ),
        "btn_clear_history" to mapOf(
            ReportLanguage.ENGLISH to "Clear History",
            ReportLanguage.HINGLISH to "History clear karein",
            ReportLanguage.HINDI to "इतिहास साफ़ करें"
        ),
        "btn_notify_release" to mapOf(
            ReportLanguage.ENGLISH to "Notify Me On Release",
            ReportLanguage.HINGLISH to "Release par notify karein",
            ReportLanguage.HINDI to "रिलीज़ होने पर सूचित करें"
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

        // Mentor Mode
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
            ReportLanguage.ENGLISH to "TODAY'S TASK",
            ReportLanguage.HINGLISH to "AAJ KA TASK",
            ReportLanguage.HINDI to "आज का कार्य"
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

        // Creator Academy & Setup
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
            ReportLanguage.HINGLISH to "Primary Goal",
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
