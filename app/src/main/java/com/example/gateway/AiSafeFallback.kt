package com.example.gateway

import com.example.core.UniversalAiProvider
import com.example.core.UniversalAiResponse

/**
 * PHASE 12D — Safe Fallback Handler
 * Guarantees zero application crashes or thread freezes on AI execution failure.
 */

object AiSafeFallback {

    const val FALLBACK_ERROR_MESSAGE = "AI couldn't confidently complete this analysis. Please try again."

    /**
     * Creates a standardized, non-crashing safe failure response.
     */
    fun createSafeFailureResponse(
        requestId: String,
        providerUsed: UniversalAiProvider = UniversalAiProvider.GEMINI,
        customMessage: String? = null
    ): UniversalAiResponse {
        val displayMsg = customMessage ?: FALLBACK_ERROR_MESSAGE
        return UniversalAiResponse(
            requestId = requestId,
            providerUsed = providerUsed,
            summary = displayMsg,
            suggestions = listOf("Ensure product image or link is clear.", "Check internet connectivity and try again."),
            confidencePercent = 15,
            warnings = listOf(displayMsg),
            isFallbackTriggered = true,
            isSuccess = false,
            errorMessage = displayMsg
        )
    }
}
