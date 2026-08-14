package com.example.creatorassistant.engine

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.creatorassistant.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoUnderstandingEngine(
    private val context: Context,
    private val metadataEngine: VideoMetadataEngine = VideoMetadataEngine(context),
    private val sceneEngine: SceneDetectionEngine = SceneDetectionEngine(context),
    private val subjectEngine: SubjectDetectionEngine = SubjectDetectionEngine(context),
    private val trackingEngine: SubjectTrackingEngine = SubjectTrackingEngine(context),
    private val audioIntelligenceEngine: AudioIntelligenceEngine = AudioIntelligenceEngine(context),
    private val videoQualityEngine: VideoQualityEngine = VideoQualityEngine(context)
) {

    suspend fun analyzeAndUnderstand(videoUri: Uri): VideoUnderstandingResult = withContext(Dispatchers.IO) {
        val videoHash = ProcessingCache.computeFingerprint(context, videoUri)
        val cached = ProcessingCache.getUnderstanding(videoHash)
        if (cached != null) {
            return@withContext cached
        }

        Log.i("VideoUnderstandingEngine", "VIDEO_ANALYSIS_START for $videoUri")

        // 1. Metadata
        val metadata = metadataEngine.extractMetadata(videoUri)
        val durationMs = metadata.durationMs
        val width = metadata.width
        val height = metadata.height
        val fps = metadata.fps

        // 2. Scene Analysis
        val sceneRes = sceneEngine.analyzeScenes(durationMs, fps, width, height)
        Log.i("VideoUnderstandingEngine", "SCENE_ANALYSIS_COMPLETE: ${sceneRes.sceneCount} scenes (${sceneRes.sceneType})")

        // 3. Subject Detection
        val subjectRes = subjectEngine.detectPrimarySubject(width, height, durationMs, metadata.orientation)
        Log.i("VideoUnderstandingEngine", "SUBJECT_DETECTED: ${subjectRes.subjectType} (conf=${subjectRes.confidence})")

        // 4. Tracking Analysis
        val trackingRes = trackingEngine.trackSubjectAcrossFrames(
            subjectConfidence = subjectRes.confidence,
            boundingRegion = subjectRes.boundingRegion,
            durationMs = durationMs
        )

        // 5. Audio Intelligence Extraction
        val audioIntel = audioIntelligenceEngine.analyzeAudio(videoUri)
        val audioMetrics = audioIntel.metrics
        Log.i("VideoUnderstandingEngine", "AUDIO_INTELLIGENCE_COMPLETE: status=${audioMetrics.status} speech=${audioMetrics.speechCategory} noise=${audioMetrics.noiseCategory}")

        // 6. Quality Analysis
        val qualityRes = videoQualityEngine.evaluateQuality(width, height, durationMs, fps)

        // 7. Recommendation Ratio Calculation
        val detectedRatio = when {
            kotlin.math.abs(metadata.aspectRatio - (9f / 16f)) < 0.12f -> TargetRatio.REELS_9_16
            kotlin.math.abs(metadata.aspectRatio - (16f / 9f)) < 0.12f -> TargetRatio.YOUTUBE_16_9
            kotlin.math.abs(metadata.aspectRatio - 1f) < 0.12f -> TargetRatio.SQUARE_1_1
            kotlin.math.abs(metadata.aspectRatio - (4f / 5f)) < 0.12f -> TargetRatio.FEED_4_5
            else -> TargetRatio.ORIGINAL
        }

        val recommendedRatio = if (detectedRatio == TargetRatio.YOUTUBE_16_9 || detectedRatio == TargetRatio.FEED_4_5) {
            TargetRatio.REELS_9_16
        } else {
            detectedRatio
        }

        // Dynamic Quality Scores
        val initialFraming = if (recommendedRatio != detectedRatio) 72 else 92
        val initialVideo = (qualityRes.sharpnessScore * 100).toInt().coerceIn(60, 95)
        val initialAudio = audioIntel.qualityScore.overallAudioScore ?: 0
        val initialSocial = if (recommendedRatio == TargetRatio.REELS_9_16) 88 else 68

        val initialQuality = QualityScores(
            framingScore = initialFraming,
            videoQualityScore = initialVideo,
            audioQualityScore = initialAudio,
            socialReadyScore = initialSocial
        )

        // File size string
        val sizeMb = metadata.fileSizeBytes / (1024.0 * 1024.0)
        val fileSizeStr = if (sizeMb >= 1.0) "${"%.1f".format(sizeMb)} MB" else "${metadata.fileSizeBytes / 1024} KB"

        val result = VideoUnderstandingResult(
            videoUri = videoUri,
            width = width,
            height = height,
            aspectRatio = metadata.aspectRatio,
            resolutionLabel = "${width} × ${height}",
            durationMs = durationMs,
            fps = fps,
            orientationLabel = metadata.orientation,
            codecLabel = metadata.videoCodec,
            fileSizeLabel = fileSizeStr,
            fileSizeBytes = metadata.fileSizeBytes,
            hasAudio = audioMetrics.status == AudioStatus.HAS_AUDIO,
            audioCodec = metadata.audioCodec,
            audioChannels = if (audioMetrics.channelsCount > 0) audioMetrics.channelsCount else metadata.audioChannels,
            audioSampleRate = if (audioMetrics.sampleRateHz > 0) audioMetrics.sampleRateHz else metadata.audioSampleRate,
            hasSpeech = audioMetrics.speechCategory != SpeechClassification.NO_SPEECH,
            speechConfidence = if (audioMetrics.speechCategory != SpeechClassification.NO_SPEECH) 0.90f else 0.0f,
            personDetected = subjectRes.personDetected,
            faceDetected = subjectRes.faceDetected,
            subjectType = subjectRes.subjectType,
            subjectConfidence = subjectRes.confidence,
            boundingRegion = subjectRes.boundingRegion,
            motionLevel = qualityRes.motionLevel,
            cameraShakeLevel = qualityRes.cameraShakeLevel,
            sceneCount = sceneRes.sceneCount,
            sceneType = sceneRes.sceneType,
            brightnessScore = qualityRes.brightnessScore,
            contrastScore = qualityRes.contrastScore,
            sharpnessScore = qualityRes.sharpnessScore,
            audioLevelDb = audioMetrics.rmsDb,
            noiseEstimate = audioMetrics.noiseEstimate,
            clippingDetected = audioMetrics.clippingDetected,
            silenceSegments = audioMetrics.silenceSegments,
            recommendedRatio = recommendedRatio,
            initialQuality = initialQuality
        )

        Log.i("VideoUnderstandingEngine", "VIDEO_ANALYSIS_COMPLETE for $videoUri")
        ProcessingCache.putUnderstanding(videoHash, result)
        result
    }
}
