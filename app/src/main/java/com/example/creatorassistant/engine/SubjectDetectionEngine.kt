package com.example.creatorassistant.engine

import android.content.Context
import android.util.Log
import com.example.creatorassistant.domain.DetectedSubject

data class SubjectDetectionResult(
    val subjectType: String,
    val boundingRegion: Pair<Float, Float>,
    val confidence: Float,
    val personDetected: Boolean,
    val faceDetected: Boolean,
    val trackingAvailable: Boolean,
    val detectedSubjectsList: List<DetectedSubject>,
    val isEngineAvailable: Boolean = false,
    val statusMessage: String = "Subject and Face detection vision models are NOT READY / not initialized."
)

class SubjectDetectionEngine(private val context: Context) {

    fun isEngineReady(): Boolean = false

    fun detectPrimarySubject(
        width: Int,
        height: Int,
        durationMs: Long,
        orientation: String
    ): SubjectDetectionResult {
        Log.w("SubjectDetectionEngine", "Subject / Face detection vision engine requested, but model is NOT READY.")

        return SubjectDetectionResult(
            subjectType = "Uninitialized Vision Model",
            boundingRegion = Pair(0.5f, 0.5f),
            confidence = 0.0f,
            personDetected = false,
            faceDetected = false,
            trackingAvailable = false,
            detectedSubjectsList = emptyList(),
            isEngineAvailable = false,
            statusMessage = "Subject and Face Detection ML models are NOT READY."
        )
    }
}

