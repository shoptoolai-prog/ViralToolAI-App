package com.example.vision

/**
 * PHASE 12C — Vision Extraction Confidence Engine
 * Tracks field detection, missing state, confidence scores, and visibility.
 */

data class ExtractedField<T>(
    val fieldName: String,
    val value: T?,
    val isDetected: Boolean = value != null,
    val isMissing: Boolean = value == null,
    val confidence: Double = if (value != null) 0.92 else 0.0,
    val isVisible: Boolean = isDetected,
    val isHidden: Boolean = !isVisible
) {
    fun displayOrFallback(fallbackMessage: String = "Unavailable"): String {
        return if (isDetected && value != null) value.toString() else fallbackMessage
    }
}

data class VisionConfidenceMetrics(
    val classificationConfidence: Double = 0.95,
    val ocrQualityScore: Double = 0.90,
    val fieldConfidenceMap: Map<String, Double> = emptyMap(),
    val overallConfidence: Double = 0.92
) {
    val isHighQuality: Boolean get() = overallConfidence >= 0.70
    val isLowQuality: Boolean get() = overallConfidence < 0.40
}
