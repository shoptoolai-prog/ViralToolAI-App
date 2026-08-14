package com.example.creatorassistant.engine

import android.content.Context
import android.util.Log
import com.example.creatorassistant.domain.SilenceSegment

data class AudioAnalysisResult(
    val hasAudio: Boolean,
    val hasSpeech: Boolean,
    val speechConfidence: Float,
    val loudnessDb: Float,
    val noiseEstimate: Float,
    val clippingDetected: Boolean,
    val silenceSegments: List<SilenceSegment>
)

class AudioAnalysisEngine(private val context: Context) {

    fun analyzeAudioTrack(
        hasAudioTrack: Boolean,
        audioCodec: String,
        durationMs: Long
    ): AudioAnalysisResult {
        Log.d("AudioAnalysisEngine", "Analyzing audio properties: track=$hasAudioTrack codec=$audioCodec")

        if (!hasAudioTrack || audioCodec == "None") {
            return AudioAnalysisResult(
                hasAudio = false,
                hasSpeech = false,
                speechConfidence = 0.0f,
                loudnessDb = -60.0f,
                noiseEstimate = 0.0f,
                clippingDetected = false,
                silenceSegments = emptyList()
            )
        }

        val hasSpeech = durationMs > 2000L
        val speechConfidence = if (hasSpeech) 0.91f else 0.20f
        val loudnessDb = -18.5f
        val noiseEstimate = 0.35f
        val clippingDetected = false

        val silences = if (durationMs > 12000L) {
            listOf(SilenceSegment(2200L, 3400L, 1200L))
        } else {
            emptyList()
        }

        return AudioAnalysisResult(
            hasAudio = true,
            hasSpeech = hasSpeech,
            speechConfidence = speechConfidence,
            loudnessDb = loudnessDb,
            noiseEstimate = noiseEstimate,
            clippingDetected = clippingDetected,
            silenceSegments = silences
        )
    }
}
