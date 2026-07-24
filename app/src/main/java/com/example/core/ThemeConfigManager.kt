package com.example.core

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.CrimsonRed

enum class AppThemePreset(val id: String, val displayName: String, val primaryColor: Color) {
    EMERALD_AMOLED("emerald", "Emerald Flagship", EmeraldPrimary),
    PURPLE_ROYAL("purple", "Royal Electric", ElectricPurple),
    CRIMSON_LUXURY("crimson", "Luxury Crimson", CrimsonRed)
}

/**
 * Architecture state manager for Phase 11.3 Dynamic Themes & Accent Colors.
 */
object ThemeConfigManager {
    private const val PREF_NAME = "viraltoolai_theme_prefs"
    private const val KEY_THEME = "selected_theme_preset"
    private const val KEY_ADAPTIVE_COLORS = "adaptive_colors_enabled"
    private const val KEY_AI_WALLPAPER = "ai_wallpaper_enabled"

    fun getSelectedPreset(context: Context): AppThemePreset {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val id = prefs.getString(KEY_THEME, AppThemePreset.EMERALD_AMOLED.id)
        return AppThemePreset.values().find { it.id == id } ?: AppThemePreset.EMERALD_AMOLED
    }

    fun setSelectedPreset(context: Context, preset: AppThemePreset) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_THEME, preset.id).apply()
    }

    fun isAdaptiveColorsEnabled(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_ADAPTIVE_COLORS, true)
    }

    fun isAiWallpaperEnabled(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_AI_WALLPAPER, true)
    }
}
