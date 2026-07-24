package com.example.core.model

enum class AiResponseStatus {
    SUCCESS,
    PARTIAL,
    FAILURE,
    REQUIRES_KEY
}

/**
 * SHOPTOOLAI Phase 12A — Standardized AI Response & Confidence Architecture
 */
data class StandardAiResponse<T>(
    val status: AiResponseStatus,
    val confidence: Double, // 0.0 to 1.0 real confidence score
    val detectedData: T? = null,
    val detectedFields: List<String> = emptyList(),
    val missingFields: List<String> = emptyList(),
    val sourceType: String = "LIVE_ENGINE",
    val warnings: List<String> = emptyList(),
    val errorMessage: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) {
    val isSuccessful: Boolean get() = status == AiResponseStatus.SUCCESS || status == AiResponseStatus.PARTIAL
}
