package com.example.core

import android.content.Context
import android.util.Log

/**
 * Architecture for optional startup sound chime in future.
 * Configured not to autoplay by default, but ready for Phase 11 updates.
 */
object StartupSoundPlayer {
    private const val PREF_NAME = "viraltoolai_settings"
    private const val KEY_SOUND_ENABLED = "startup_sound_enabled"

    fun isSoundEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_SOUND_ENABLED, false) // Default false (do not play automatically)
    }

    fun setSoundEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply()
    }

    fun playStartupChimeIfEnabled(context: Context) {
        if (isSoundEnabled(context)) {
            try {
                // Future audio chime playback hook
                Log.d("StartupSoundPlayer", "Startup chime triggered (Sound enabled)")
            } catch (e: Exception) {
                Log.e("StartupSoundPlayer", "Failed to play startup chime", e)
            }
        }
    }
}
