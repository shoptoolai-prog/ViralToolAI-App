package com.example.gateway

import android.util.Log

/**
 * PHASE 12D — Developer Diagnostic Logger
 * Hidden from standard end users; records detailed provider latency, confidence, OCR quality,
 * and parsing errors for developer inspection.
 */

data class DeveloperDiagnosticEntry(
    val timestampMs: Long = System.currentTimeMillis(),
    val provider: String,
    val model: String,
    val latencyMs: Long,
    val confidenceScore: Double,
    val ocrQualityScore: Double,
    val parsingErrors: List<String> = emptyList(),
    val rawResponseSnippet: String? = null
)

object AiDeveloperLogger {

    private const val TAG = "ViralToolAI_Gateway"
    private val diagnosticLogs = mutableListOf<DeveloperDiagnosticEntry>()
    private const val MAX_LOGS = 200

    fun logDiagnostic(entry: DeveloperDiagnosticEntry) {
        synchronized(diagnosticLogs) {
            diagnosticLogs.add(entry)
            if (diagnosticLogs.size > MAX_LOGS) {
                diagnosticLogs.removeAt(0)
            }
        }
        Log.d(TAG, "[DEVELOPER DIAGNOSTIC] Provider: ${entry.provider} (${entry.model}) | Latency: ${entry.latencyMs}ms | Confidence: ${(entry.confidenceScore * 100).toInt()}% | OCR Quality: ${(entry.ocrQualityScore * 100).toInt()}% | Errors: ${entry.parsingErrors.size}")
    }

    fun getLogs(): List<DeveloperDiagnosticEntry> {
        synchronized(diagnosticLogs) {
            return diagnosticLogs.toList()
        }
    }

    fun clearLogs() {
        synchronized(diagnosticLogs) {
            diagnosticLogs.clear()
        }
    }
}
