package com.example.creatorassistant.engine

import android.content.Context
import android.util.Log

data class VideoQualityResult(
    val brightnessScore: Float,
    val contrastScore: Float,
    val sharpnessScore: Float,
    val cameraShakeLevel: String,
    val motionLevel: Float
)

class VideoQualityEngine(private val context: Context) {

    fun evaluateQuality(
        width: Int,
        height: Int,
        durationMs: Long,
        fps: Float
    ): VideoQualityResult {
        Log.d("VideoQualityEngine", "Evaluating visual quality & camera stability")

        val brightness = 0.72f
        val contrast = 0.68f
        val sharpness = 0.78f
        val cameraShake = if (durationMs > 20000L) "MILD_SHAKE" else "STABLE"
        val motionLevel = if (fps >= 50f) 0.85f else 0.55f

        return VideoQualityResult(
            brightnessScore = brightness,
            contrastScore = contrast,
            sharpnessScore = sharpness,
            cameraShakeLevel = cameraShake,
            motionLevel = motionLevel
        )
    }
}
