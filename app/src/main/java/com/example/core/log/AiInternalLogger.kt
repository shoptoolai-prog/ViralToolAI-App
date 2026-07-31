package com.example.core.log

import android.util.Log
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * SHOPTOOLAI Phase 12A — Internal Developer Logging System
 * Tracks OCR failures, detection failures, merchant failures, and crash points internally.
 * Non-visible to users.
 */
enum class LogCategory {
    OCR_FAILURE,
    DETECTION_FAILURE,
    MERCHANT_FAILURE,
    CRASH_POINT,
    CONFIDENCE_WARN,
    PERFORMANCE,
    PIPELINE_EVENT
}

data class AiLogEvent(
    val category: LogCategory,
    val message: String,
    val details: String? = null,
    val confidence: Double? = null,
    val timestamp: Long = System.currentTimeMillis()
)

object AiInternalLogger {
    private val logs = ConcurrentLinkedQueue<AiLogEvent>()
    private const val MAX_LOGS = 500

    fun log(category: LogCategory, message: String, details: String? = null, confidence: Double? = null) {
        val event = AiLogEvent(category, message, details, confidence)
        logs.add(event)
        if (logs.size > MAX_LOGS) {
            logs.poll()
        }
        Log.d("ViralToolAi_InternalLog", "[$category] $message ${details?.let { "($it)" } ?: ""} ${confidence?.let { "(conf: $it)" } ?: ""}")
    }

    fun getLogs(): List<AiLogEvent> = logs.toList()

    fun getLogsByCategory(category: LogCategory): List<AiLogEvent> = logs.filter { it.category == category }

    fun clear() {
        logs.clear()
    }
}
