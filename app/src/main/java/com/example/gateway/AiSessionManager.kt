package com.example.gateway

import com.example.core.UniversalAiProvider
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * PHASE 12D — AI Session Manager
 * Tracks Request ID, Timestamps, Processing Duration, Provider Used, and Session Diagnostics.
 */

data class AiSessionRecord(
    val requestId: String,
    val module: AiPromptModule,
    val providerUsed: UniversalAiProvider,
    val modelName: String,
    val startTimeMs: Long,
    var endTimeMs: Long = 0L,
    var durationMs: Long = 0L,
    var isSuccess: Boolean = false,
    var failureReason: String? = null,
    var confidenceScore: Double = 0.0,
    var ocrQualityScore: Double = 0.0,
    var isOfflineFallback: Boolean = false
)

object AiSessionManager {

    private val sessionHistory = ConcurrentLinkedQueue<AiSessionRecord>()
    private val maxHistorySize = 100

    fun startSession(
        requestId: String,
        module: AiPromptModule,
        providerUsed: UniversalAiProvider,
        modelName: String
    ): AiSessionRecord {
        val record = AiSessionRecord(
            requestId = requestId,
            module = module,
            providerUsed = providerUsed,
            modelName = modelName,
            startTimeMs = System.currentTimeMillis()
        )
        sessionHistory.add(record)
        if (sessionHistory.size > maxHistorySize) {
            sessionHistory.poll()
        }
        return record
    }

    fun completeSession(
        sessionRecord: AiSessionRecord,
        isSuccess: Boolean,
        confidenceScore: Double = 0.90,
        ocrQualityScore: Double = 0.90,
        failureReason: String? = null,
        isOfflineFallback: Boolean = false
    ) {
        val endTime = System.currentTimeMillis()
        sessionRecord.endTimeMs = endTime
        sessionRecord.durationMs = endTime - sessionRecord.startTimeMs
        sessionRecord.isSuccess = isSuccess
        sessionRecord.confidenceScore = confidenceScore
        sessionRecord.ocrQualityScore = ocrQualityScore
        sessionRecord.failureReason = failureReason
        sessionRecord.isOfflineFallback = isOfflineFallback
    }

    fun getSessionHistory(): List<AiSessionRecord> = sessionHistory.toList()

    fun clearHistory() {
        sessionHistory.clear()
    }
}
