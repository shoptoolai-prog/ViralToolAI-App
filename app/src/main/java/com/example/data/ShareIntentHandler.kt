package com.example.data

import android.content.Intent

/**
 * SHOPTOOLAI Phase 4A — Android Share Target Intent Handler
 * Automatically handles shared product URLs from Flipkart, Amazon, Instagram, Chrome, Meesho, AJIO, Myntra, etc.
 */
object ShareIntentHandler {

    /**
     * Extracts a shared shopping URL from an incoming Android Intent.
     * Handles ACTION_SEND (text/plain) and ACTION_VIEW intents.
     */
    fun extractSharedUrl(intent: Intent?): String? {
        if (intent == null) return null

        val action = intent.action ?: return null
        val type = intent.type

        when {
            Intent.ACTION_SEND == action && type?.startsWith("text/") == true -> {
                val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                    ?: intent.getCharSequenceExtra(Intent.EXTRA_TEXT)?.toString()
                
                if (!sharedText.isNullOrBlank()) {
                    val extractedUrl = MerchantDetector.extractUrlFromText(sharedText)
                    if (extractedUrl.isNotBlank()) {
                        return MerchantDetector.normalizeUrl(extractedUrl)
                    }
                }
            }
            Intent.ACTION_VIEW == action -> {
                val dataUri = intent.dataString
                if (!dataUri.isNullOrBlank()) {
                    val extractedUrl = MerchantDetector.extractUrlFromText(dataUri)
                    if (extractedUrl.isNotBlank()) {
                        return MerchantDetector.normalizeUrl(extractedUrl)
                    }
                }
            }
        }

        return null
    }
}
