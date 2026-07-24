package com.example.creatoracademy

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.EmeraldPrimary
import java.util.Calendar

/**
 * MASTER PHASE 16 — Viral Memory Engine™
 * Transforms ViralToolAI into a Personal AI Mentor that remembers every user's journey
 * and restores state seamlessly across sessions.
 */

data class ViralLevel(
    val levelNumber: Int,
    val name: String,
    val badgeName: String,
    val minXp: Int,
    val maxXp: Int,
    val color: Color,
    val icon: ImageVector
) {
    companion object {
        fun getLevelForXp(xp: Int): ViralLevel {
            return when {
                xp < 100 -> ViralLevel(1, "Beginner", "LEVEL 1 • BEGINNER", 0, 100, Color(0xFF9E9E9E), Icons.Default.Star)
                xp < 250 -> ViralLevel(2, "Explorer", "LEVEL 2 • EXPLORER", 100, 250, Color(0xFF81C784), Icons.Default.MilitaryTech)
                xp < 500 -> ViralLevel(3, "Creator", "LEVEL 3 • CREATOR", 250, 500, Color(0xFF64B5F6), Icons.Default.WorkspacePremium)
                xp < 900 -> ViralLevel(4, "Advanced", "LEVEL 4 • ADVANCED", 500, 900, Color(0xFFBA68C8), Icons.Default.Psychology)
                xp < 1400 -> ViralLevel(5, "Professional", "LEVEL 5 • PROFESSIONAL", 900, 1400, Color(0xFFFFD54F), Icons.Default.EmojiEvents)
                xp < 2000 -> ViralLevel(6, "Expert", "LEVEL 6 • EXPERT", 1400, 2000, Color(0xFFFF8A65), Icons.Default.WorkspacePremium)
                else -> ViralLevel(7, "Elite Creator", "LEVEL 7 • ELITE CREATOR", 2000, 5000, EmeraldPrimary, Icons.Default.EmojiEvents)
            }
        }
    }
}

data class DynamicTask(
    val id: String,
    val title: String,
    val description: String,
    val xpReward: Int = 50,
    val isCompleted: Boolean = false
)

object ViralMemoryEngine {
    private const val PREF_NAME = "viral_memory_engine_prefs"

    private const val KEY_LAST_WORKSPACE = "last_workspace" // SHOPPING or CREATOR_ACADEMY
    private const val KEY_LAST_COMPLETED_LESSON = "last_completed_lesson"
    private const val KEY_NEXT_LESSON = "next_lesson"
    private const val KEY_LAST_SESSION_TIMESTAMP = "last_session_timestamp"
    private const val KEY_REMEMBER_WELCOME_CHOICE = "remember_welcome_choice"
    private const val KEY_COMPLETED_DYNAMIC_TASKS = "completed_dynamic_tasks"

    private val DAILY_MOTIVATIONS = listOf(
        "Consistency beats talent.",
        "One Reel can change your career.",
        "Today's upload becomes tomorrow's opportunity.",
        "Quality content creates timeless connections.",
        "Every top creator started at zero views.",
        "Action creates momentum; perfection is the enemy.",
        "Small daily improvements lead to massive yearly results.",
        "Focus on providing value, and views will follow.",
        "Build authority one high-converting video at a time."
    )

    val DYNAMIC_TASK_LIST = listOf(
        DynamicTask("dt_1", "Upload one reel", "Share 1 high-retention short video today", 50),
        DynamicTask("dt_2", "Rewrite Bio", "Optimize bio with 3 clear value bullet points", 50),
        DynamicTask("dt_3", "Improve Hook", "Test 3 scroll-stopping 3-second visual hooks", 50),
        DynamicTask("dt_4", "Reply to 10 comments", "Boost post engagement & algorithm signals", 50),
        DynamicTask("dt_5", "Edit one Short", "Add auto-captions & high-contrast graphics", 50),
        DynamicTask("dt_6", "Record B-roll", "Film 3 dynamic 5-second background clips", 50),
        DynamicTask("dt_7", "Send one Brand Pitch", "Reach out to 1 potential affiliate sponsor", 50),
        DynamicTask("dt_8", "Improve SEO", "Add 5 niche search keywords to caption", 50),
        DynamicTask("dt_9", "Design Thumbnail", "Create a high-CTR cover frame for feed", 50),
        DynamicTask("dt_10", "Test Trending Audio", "Use audio with rising arrow ↗️ indicator", 50)
    )

    // Daily Motivation Selector
    fun getDailyMotivation(): String {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val index = dayOfYear % DAILY_MOTIVATIONS.size
        return DAILY_MOTIVATIONS[index]
    }

    // Workspace Memory
    fun getLastWorkspace(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LAST_WORKSPACE, "SHOPPING") ?: "SHOPPING"
    }

    fun saveLastWorkspace(context: Context, workspace: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LAST_WORKSPACE, workspace).apply()
    }

    // Session & Lesson Progress Memory
    fun saveLessonProgress(context: Context, lastCompleted: String, nextLesson: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_LAST_COMPLETED_LESSON, lastCompleted)
            .putString(KEY_NEXT_LESSON, nextLesson)
            .putLong(KEY_LAST_SESSION_TIMESTAMP, System.currentTimeMillis())
            .apply()
    }

    fun getLastCompletedLesson(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LAST_COMPLETED_LESSON, null)
    }

    fun getNextLesson(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_NEXT_LESSON, "Lesson #1: High-Converting Bio Setup") ?: "Lesson #1: High-Converting Bio Setup"
    }

    fun hasPreviousSession(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val hasCompletedLessonKey = prefs.contains(KEY_LAST_COMPLETED_LESSON)
        val completedIg = CreatorAcademyPrefs.getCompletedTasks(context, "INSTAGRAM").isNotEmpty()
        val completedYt = CreatorAcademyPrefs.getCompletedTasks(context, "YOUTUBE").isNotEmpty()
        return hasCompletedLessonKey || completedIg || completedYt
    }

    fun isRememberWelcomeChoice(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_REMEMBER_WELCOME_CHOICE, false)
    }

    fun setRememberWelcomeChoice(context: Context, remember: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_REMEMBER_WELCOME_CHOICE, remember).apply()
    }

    // Dynamic Tasks Memory
    fun getCompletedDynamicTasks(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_COMPLETED_DYNAMIC_TASKS, emptySet()) ?: emptySet()
    }

    fun markDynamicTaskCompleted(context: Context, taskId: String): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val completed = getCompletedDynamicTasks(context).toMutableSet()
        if (!completed.contains(taskId)) {
            completed.add(taskId)
            prefs.edit().putStringSet(KEY_COMPLETED_DYNAMIC_TASKS, completed).apply()
            // Add +50 XP
            CreatorAcademyPrefs.addXpPoints(context, 50, "INSTAGRAM")
            return true
        }
        return false
    }

    fun getTodayDynamicTask(context: Context): DynamicTask {
        val completed = getCompletedDynamicTasks(context)
        val available = DYNAMIC_TASK_LIST.filter { !completed.contains(it.id) }
        return if (available.isNotEmpty()) {
            val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            available[dayOfYear % available.size]
        } else {
            DYNAMIC_TASK_LIST.first().copy(isCompleted = true)
        }
    }
}
