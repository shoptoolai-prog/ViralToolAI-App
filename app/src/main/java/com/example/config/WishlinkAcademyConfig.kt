package com.example.config

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * Single Configuration File for Wishlink Creator Academy Links & Official Resources.
 * Update any official URL here to reflect across all screens and components instantly.
 */
object WishlinkAcademyConfig {
    const val OFFICIAL_WEBSITE_URL = "https://www.wishlink.com/"
    const val CREATOR_DASHBOARD_URL = "https://creator.wishlink.com/"
    const val PLAY_STORE_PACKAGE = "com.wishlink.creator"
    const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.wishlink.creator"
    const val INSTAGRAM_OFFICIAL_URL = "https://www.instagram.com/wishlink.in/"
    const val HELP_CENTER_URL = "https://creator.wishlink.com/help"
    const val COMMISSIONS_GUIDE_URL = "https://www.wishlink.com/faq"

    fun openUrl(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open URL: $url", Toast.LENGTH_SHORT).show()
        }
    }

    fun openPlayStore(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$PLAY_STORE_PACKAGE")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            openUrl(context, PLAY_STORE_URL)
        }
    }
}
