package com.example.creatorassistant.domain

import android.graphics.Bitmap
import android.net.Uri

enum class SubjectType {
    FACE,
    PERSON,
    PRODUCT,
    OBJECT,
    TEXT_REGION,
    MAIN_VISUAL_REGION
}

enum class ContentType {
    TALKING_HEAD,
    PRODUCT_REVIEW,
    DANCE,
    VLOG,
    GENERAL
}

enum class TrackingVisibility {
    HIGH,
    MEDIUM,
    LOW,
    LOST
}

data class SubjectScore(
    val subjectType: SubjectType,
    val screenAreaRatio: Float,
    val persistenceScore: Float,
    val movementScore: Float,
    val positionScore: Float,
    val appearanceFrequency: Float,
    val faceVisibilityScore: Float,
    val objectConfidence: Float,
    val totalScore: Float
)

data class TrackedSubject(
    val trackId: Long,
    val type: SubjectType,
    val label: String,
    val centerX: Float, // 0.0 .. 1.0
    val centerY: Float, // 0.0 .. 1.0
    val widthRatio: Float,
    val heightRatio: Float,
    val timestampMs: Long,
    val confidence: Float,
    val visibility: TrackingVisibility,
    val velocityX: Float = 0.0f,
    val velocityY: Float = 0.0f,
    val lastSeenMs: Long = timestampMs,
    val score: SubjectScore? = null
)

data class SafeMargin(
    val leftMarginRatio: Float,
    val topMarginRatio: Float,
    val rightMarginRatio: Float,
    val bottomMarginRatio: Float
)

data class ReframeCropWindow(
    val timestampMs: Long,
    val cropX: Float, // 0.0 .. 1.0
    val cropY: Float, // 0.0 .. 1.0
    val cropWidth: Float, // 0.0 .. 1.0
    val cropHeight: Float, // 0.0 .. 1.0
    val targetRatio: TargetRatio,
    val primaryTrackId: Long,
    val secondaryTrackId: Long? = null,
    val trackingState: TrackingVisibility,
    val contentType: ContentType
)

data class TrackingQualityReport(
    val subjectVisibilityPercent: Float,
    val subjectCropLossPercent: Float,
    val cropMovementSmoothness: Float,
    val jitterScore: Float,
    val sceneContinuityScore: Float,
    val overallQualityScore: Int,
    val isQualityAcceptable: Boolean,
    val recoveryEventsCount: Int
)

data class TrackingDebugEntry(
    val timestampMs: Long,
    val trackId: Long,
    val type: SubjectType,
    val centerX: Float,
    val centerY: Float,
    val confidence: Float,
    val cropRectStr: String,
    val state: String,
    val eventNote: String? = null
)

enum class AudioStatus {
    HAS_AUDIO,
    NO_AUDIO,
    CORRUPTED_AUDIO,
    UNSUPPORTED_AUDIO
}

enum class SpeechClassification {
    NO_SPEECH,
    LOW_SPEECH,
    NORMAL_SPEECH,
    HIGH_SPEECH
}

enum class AudioClassificationType {
    SPEECH,
    MUSIC,
    MIXED,
    SILENCE,
    UNKNOWN
}

enum class NoiseLevelCategory {
    LOW,
    MODERATE,
    HIGH,
    UNKNOWN
}

enum class AudioAction {
    NO_CHANGE,
    DENOISE,
    VOICE_ENHANCE,
    WIND_REDUCE,
    DE_HUM,
    SPEECH_CLARITY,
    NORMALIZE,
    DE_CLIP,
    REMOVE_SILENCE,
    PRESERVE_ORIGINAL
}

data class AudioMetrics(
    val status: AudioStatus,
    val durationMs: Long,
    val rmsDb: Float,
    val peakLevelDb: Float,
    val silenceDurationMs: Long,
    val speechDurationMs: Long,
    val nonSpeechDurationMs: Long,
    val clippingDetected: Boolean,
    val clippingRanges: List<Pair<Long, Long>> = emptyList(),
    val noiseEstimate: Float,
    val noiseCategory: NoiseLevelCategory,
    val speechCategory: SpeechClassification,
    val audioType: AudioClassificationType,
    val dynamicRangeDb: Float,
    val audioConsistencyScore: Float, // 0.0 .. 1.0
    val sampleRateHz: Int,
    val channelsCount: Int,
    val detectedLanguage: String = "UNKNOWN",
    val silenceSegments: List<SilenceSegment> = emptyList()
)

data class AudioQualityScore(
    val isAudioAvailable: Boolean,
    val speechClarityScore: Int?, // null if no audio
    val noiseLevelScore: Int?,    // null if no audio
    val consistencyScore: Int?,   // null if no audio
    val overallAudioScore: Int?   // null if no audio
)

data class AudioProcessingPlan(
    val primaryAction: AudioAction,
    val actionsToApply: List<AudioAction>,
    val applyNoiseReduction: Boolean,
    val applyVoiceEnhancement: Boolean,
    val applyWindReduction: Boolean = false,
    val applyDeHum: Boolean = false,
    val applySpeechClarity: Boolean = false,
    val applyLoudnessNormalization: Boolean,
    val applySilenceTrim: Boolean,
    val targetLoudnessLufs: Float = -14.0f,
    val recommendationText: String = "",
    val reason: String
)

data class AudioOperationAudit(
    val detectedIssue: String,
    val recommendedAction: String,
    val appliedAction: String,
    val validationResult: String // "PASS", "FAIL", "SKIPPED", "FALLBACK"
)

data class AudioIntelligenceResult(
    val metrics: AudioMetrics,
    val qualityScore: AudioQualityScore,
    val plan: AudioProcessingPlan,
    val creatorInsights: List<String>,
    val auditLogs: List<AudioOperationAudit>
)

data class AudioValidationResult(
    val isValid: Boolean,
    val hasAudioStream: Boolean,
    val isDurationValid: Boolean,
    val isSampleRateValid: Boolean,
    val isSyncValid: Boolean,
    val failureReason: String? = null
)

enum class TargetRatio(
    val label: String,
    val description: String,
    val aspectRatio: Float, // width / height
    val tag: String
) {
    ORIGINAL("Original", "Keep input aspect ratio", 0f, "orig"),
    REELS_9_16("9:16", "Reels / Shorts / TikTok", 9f / 16f, "9x16"),
    YOUTUBE_16_9("16:9", "YouTube / Landscape", 16f / 9f, "16x9"),
    SQUARE_1_1("1:1", "Square Post", 1f / 1f, "1x1"),
    FEED_4_5("4:5", "Instagram Feed", 4f / 5f, "4x5"),
    PORTRAIT_3_4("3:4", "Portrait 3:4", 3f / 4f, "3x4"),
    LANDSCAPE_4_3("4:3", "Landscape 4:3", 4f / 3f, "4x3"),
    CINEMATIC_21_9("21:9", "Cinematic Ultrawide", 21f / 9f, "21x9"),
    CUSTOM("Custom", "Adaptive Smart Crop", 9f / 16f, "custom")
}

enum class AiActionType(
    val title: String,
    val description: String,
    val category: String
) {
    CHANGE_RATIO("Change Ratio", "Convert aspect ratio to target platform specs", "Ratio"),
    AUTO_REFRAME("Auto Reframe", "Automatically center key subjects across frames", "Visual"),
    SMART_CROP("Smart Crop", "Intelligent crop avoiding face/text clipping", "Visual"),
    SUBJECT_TRACKING("Subject Tracking", "Keep person or product centered in movement", "Visual"),
    BACKGROUND_CLEANUP("Background Cleanup", "Reduce background distractions and clutter", "Visual"),
    VOICE_CLEANUP("Voice Cleanup", "Remove chatter and background vocal noise", "Audio"),
    NOISE_REMOVAL("Noise Removal", "Suppress hum, hiss, wind, and ambient noise", "Audio"),
    VOICE_ENHANCEMENT("Voice Enhancement", "Improve speech clarity, tone, and presence", "Audio"),
    SPEECH_CLARITY("Speech Clarity", "Enhance vocal intelligibility and diction", "Audio"),
    VOLUME_BALANCE("Volume Balance", "Balance dialogue, music, and background sound", "Audio"),
    COLOR_ENHANCEMENT("Color Enhancement", "Enhance vibrancy, contrast, and color balance", "Visual"),
    LOW_LIGHT_ENHANCEMENT("Low-Light Enhancement", "Brighten shadow detail and reduce grain", "Visual"),
    VIDEO_SHARPENING("Video Sharpening", "Recover edge detail and perceived sharpness", "Visual"),
    FACE_SUBJECT_CLARITY("Face/Subject Clarity", "Enhance facial definition and detail", "Visual"),
    STABILIZATION("Stabilization", "Smooth camera motion and reduce shake", "Visual"),
    SILENCE_CLEANUP("Silence Cleanup", "Trim dead air and awkward pauses", "Audio"),
    AUTO_CAPTIONS("Auto Captions", "Generate synchronized captions", "Content"),
    SAFE_AREA("Safe Area / Social Layout", "Keep faces and text out of platform UI overlays", "Content"),
    AI_QUALITY_ENHANCE("AI Quality Enhance", "Apply complete multi-pass AI enhancement", "General")
}

data class SilenceSegment(
    val startMs: Long,
    val endMs: Long,
    val durationMs: Long
)

data class DetectedSubject(
    val label: String,
    val confidence: Float,
    val boundingBox: Pair<Float, Float>, // center X, center Y (0.0 .. 1.0)
    val widthRatio: Float,
    val heightRatio: Float
)

data class QualityScores(
    val framingScore: Int,      // 0..100
    val videoQualityScore: Int, // 0..100
    val audioQualityScore: Int, // 0..100
    val socialReadyScore: Int   // 0..100
)

data class ThumbnailCandidate(
    val id: String,
    val timestampMs: Long,
    val bitmap: Bitmap?,
    val score: Int,
    val title: String,
    val isBest: Boolean = false
)

data class VideoAnalysisResult(
    val videoUri: Uri,
    val originalWidth: Int,
    val originalHeight: Int,
    val originalAspectRatio: Float, // width / height
    val durationMs: Long,
    val fps: Float,
    val resolutionLabel: String,
    val orientationLabel: String,
    val codecLabel: String,
    val hasAudio: Boolean,
    val audioPresenceLabel: String,
    val audioSpecsLabel: String,
    val fileSizeLabel: String,
    val hasSpeech: Boolean,
    val hasMusic: Boolean,
    val hasNoise: Boolean,
    val silenceSegments: List<SilenceSegment>,
    val detectedFacesCount: Int,
    val detectedSubjects: List<DetectedSubject>,
    val motionScore: Float, // 0.0 .. 1.0
    val averageBrightness: Float, // 0.0 .. 1.0
    val sharpnessScore: Float, // 0.0 .. 1.0
    val stabilityScore: Float, // 0.0 .. 1.0
    val audioLoudnessDb: Float,
    val recommendedRatio: TargetRatio,
    val recommendedOperations: List<AiActionType>,
    val initialQuality: QualityScores,
    val contentType: ContentType = ContentType.GENERAL
)

enum class ProcessingStage(
    val stageNumber: Int,
    val label: String,
    val activeDescription: String,
    val progressPercent: Int
) {
    IDLE(0, "Idle", "Select a video to begin", 0),
    VIDEO_SELECTED(0, "Video Selected", "Inspecting video format", 0),

    // 19 Pipeline Stages in Exact Order with Weighted Breakdown
    UPLOAD_VALIDATE_VIDEO(1, "Upload & Validate Video", "Validating video format...", 5),
    ANALYZE_METADATA_AUDIO(2, "Analyze Video Metadata & Audio", "Analyzing video & audio tracks...", 15),
    DETECT_PRIMARY_SUBJECT(3, "Detect Primary Subject / Face", "Analyzing faces & subjects...", 25),
    TRACK_SUBJECT_MOVEMENT(4, "Track Subject Movement", "Tracking subject movement...", 35),
    ANALYZE_COMPOSITION_MARGINS(5, "Analyze Composition & Safe Margins", "Planning safe margins & framing...", 42),
    ANALYZE_REQUESTED_RATIO(6, "Analyze Requested Aspect Ratio", "Adapting to requested ratio...", 48),
    BUILD_SMART_REFRAME_PATH(7, "Build Smart Reframe Path", "Building smart reframe path...", 55),
    APPLY_AUTO_REFRAME_CROP(8, "Apply Auto Reframe / Smart Crop", "Preparing crop parameters...", 60),
    APPLY_VIDEO_STABILIZATION(9, "Apply Video Stabilization", "Calculating motion stabilization...", 65),
    CLEAN_BACKGROUND_NOISE(10, "Clean Background Noise", "Cleaning background noise...", 70),
    ENHANCE_VOICE_DIALOGUE(11, "Enhance Voice / Dialogue", "Enhancing voice...", 75),
    BALANCE_AUDIO_LEVELS(12, "Balance Audio Levels", "Balancing audio...", 80),
    RENDER_TARGET_RATIO(13, "Render Target Ratio", "Rendering video frames...", 85),
    VERIFY_FACE_SUBJECT_VISIBILITY(14, "Verify Face / Subject Visibility", "Checking subject visibility...", 92),
    VERIFY_AUDIO_VIDEO_SYNC(15, "Verify Audio & Video Sync", "Verifying audio & video sync...", 94),
    VERIFY_OUTPUT_PLAYBACK(16, "Verify Output Playback", "Verifying output...", 96),
    GENERATE_THUMBNAILS(17, "Generate Before / After Thumbnails", "Generating preview...", 98),
    FINALIZE_OUTPUT(18, "Finalize Output", "Finalizing video...", 99),
    READY_FOR_PREVIEW(19, "Ready for Preview", "Video almost ready...", 100),

    // Legacy & Status Aliases
    ANALYZING_VIDEO(2, "Analyze Video Metadata & Audio", "Analyzing video & audio tracks...", 15),
    DETECTING_SUBJECTS(3, "Detect Primary Subject / Face", "Analyzing faces & subjects...", 25),
    UNDERSTANDING_COMPOSITION(5, "Analyze Composition & Safe Margins", "Planning safe margins & framing...", 42),
    PROCESSING_VIDEO(8, "Apply Auto Reframe / Smart Crop", "Smart reframing in progress...", 60),
    OPTIMIZING_AUDIO(10, "Clean Background Noise", "Cleaning background noise...", 70),
    APPLYING_REQUESTED_RATIO(13, "Render Target Ratio", "Rendering video frames...", 85),
    QUALITY_CHECK(16, "Verify Output Playback", "Verifying output...", 96),
    FINALIZING_OUTPUT(18, "Finalize Output", "Finalizing video...", 99),

    COMPLETED(19, "Completed", "Your video is ready to review.", 100),
    FAILED(0, "Processing Failed", "Unable to complete processing", 0),
    RETRYING(0, "Retrying", "Re-running AI transformation", 0);

    val description: String get() = activeDescription
}

data class RetryApproach(
    val id: String,
    val name: String,
    val description: String
) {
    companion object {
        val DEFAULT = RetryApproach("default", "Retry with Same Settings", "Re-run current AI pipeline")
        val FOCUS_FACE = RetryApproach("focus_face", "Focus on Face & Center", "Prioritize facial tracking over background")
        val PRESERVE_BACKGROUND = RetryApproach("preserve_bg", "Preserve More Background", "Widen crop window to capture context")
        val AGGRESSIVE_AUDIO = RetryApproach("strong_audio", "Stronger Audio Cleanup", "Maximal noise suppression & voice isolation")
        val NATURAL_ENHANCE = RetryApproach("natural_enhance", "Subtle Natural Look", "Milder contrast & color adjustments")
    }
}

enum class PipelineStatus {
    IDLE,
    ANALYZING,
    QUEUED,
    PROCESSING,
    VALIDATING,
    SUCCESS,
    FAILED,
    CANCELLED
}

data class OperationReport(
    val operationName: String,
    val actionType: AiActionType? = null,
    val requested: Boolean = true,
    var started: Boolean = false,
    var completed: Boolean = false,
    var success: Boolean = false,
    var outputChanged: Boolean = false,
    var errorMessage: String? = null
)

data class VideoMetadata(
    val width: Int,
    val height: Int,
    val aspectRatio: Float,
    val calculatedRatioLabel: String,
    val durationMs: Long,
    val fps: Float,
    val bitrate: Long,
    val videoCodec: String,
    val audioCodec: String,
    val audioSampleRate: Int,
    val audioChannels: Int,
    val fileSizeBytes: Long,
    val orientation: String,
    val rotationDegrees: Int,
    val detectedFacesCount: Int = 0
)

data class ValidationResult(
    val isValid: Boolean,
    val fileExists: Boolean,
    val fileSizeGtZero: Boolean,
    val canOpen: Boolean,
    val hasVideoStream: Boolean,
    val validDuration: Boolean,
    val validWidthHeight: Boolean,
    val aspectRatioCorrect: Boolean,
    val audioStreamValid: Boolean,
    val isPlayable: Boolean,
    val outputActuallyChanged: Boolean,
    val failureReason: String? = null
)

data class ProjectHistoryItem(
    val id: String,
    val originalFileName: String,
    val outputFileName: String,
    val originalRatio: String,
    val outputRatio: String,
    val operationsApplied: List<String>,
    val timestampMs: Long,
    val status: String
)

data class VideoUnderstandingResult(
    val videoUri: Uri,
    val width: Int,
    val height: Int,
    val aspectRatio: Float,
    val resolutionLabel: String,
    val durationMs: Long,
    val fps: Float,
    val orientationLabel: String,
    val codecLabel: String,
    val fileSizeLabel: String,
    val fileSizeBytes: Long,
    val hasAudio: Boolean,
    val audioCodec: String,
    val audioChannels: Int,
    val audioSampleRate: Int,
    val hasSpeech: Boolean,
    val speechConfidence: Float,
    val personDetected: Boolean,
    val faceDetected: Boolean,
    val subjectType: String,
    val subjectConfidence: Float,
    val boundingRegion: Pair<Float, Float>,
    val motionLevel: Float,
    val cameraShakeLevel: String,
    val sceneCount: Int,
    val sceneType: String,
    val brightnessScore: Float,
    val contrastScore: Float,
    val sharpnessScore: Float,
    val audioLevelDb: Float,
    val noiseEstimate: Float,
    val clippingDetected: Boolean,
    val silenceSegments: List<SilenceSegment>,
    val recommendedRatio: TargetRatio,
    val initialQuality: QualityScores
)

data class ProcessingOperationDecision(
    val operation: AiActionType,
    val recommended: Boolean,
    val confidence: Float,
    val reason: String,
    val estimatedRisk: String = "LOW"
)

data class ProcessingPlan(
    val convertRatio: TargetRatio,
    val autoReframe: ProcessingOperationDecision,
    val smartCrop: ProcessingOperationDecision,
    val stabilize: ProcessingOperationDecision,
    val noiseReduction: ProcessingOperationDecision,
    val voiceEnhancement: ProcessingOperationDecision,
    val loudnessNormalization: ProcessingOperationDecision,
    val brightnessCorrection: ProcessingOperationDecision,
    val contrastCorrection: ProcessingOperationDecision,
    val sharpening: ProcessingOperationDecision,
    val allDecisions: List<ProcessingOperationDecision>,
    val estimatedProcessingTimeSec: Int
)

enum class ExposureStatus {
    UNDEREXPOSED,
    OVEREXPOSED,
    BALANCED,
    UNEVEN
}

enum class CameraShakeLevel {
    STABLE,
    MILD_SHAKE,
    HIGH_SHAKE
}

data class VisualAnalysisResult(
    val width: Int,
    val height: Int,
    val fps: Float,
    val aspectRatio: Float,
    val brightnessScore: Float, // 0.0 .. 1.0
    val exposureStatus: ExposureStatus,
    val contrastScore: Float, // 0.0 .. 1.0
    val colorBalanceScore: Float, // 0.0 .. 1.0
    val colorCast: String, // "NEUTRAL", "WARM", "COOL"
    val saturationLevel: Float, // 0.0 .. 1.0
    val sharpnessScore: Float, // 0.0 .. 1.0
    val noiseLevel: Float, // 0.0 .. 1.0
    val cameraShakeLevel: CameraShakeLevel,
    val hasFace: Boolean,
    val hasTextOrSubtitles: Boolean,
    val overallVisualScore: Int, // 0 .. 100
    val stabilityScore: Int, // %
    val exposureScore: Int, // %
    val detailScore: Int, // %
    val colorScore: Int, // %
    val recommendationText: String
)

data class VisualEnhancementPlan(
    val applyStabilization: Boolean,
    val applyExposureCorrection: Boolean,
    val applyColorCorrection: Boolean,
    val applyContrastCorrection: Boolean,
    val applySharpening: Boolean,
    val applyNoiseReduction: Boolean,
    val applyUpscale: Boolean,
    val targetResolutionLabel: String = "1080p",
    val brightnessOffset: Float = 0.0f,
    val contrastMultiplier: Float = 1.0f,
    val saturationMultiplier: Float = 1.0f,
    val sharpeningAmount: Float = 0.0f,
    val stabilizationFactor: Float = 1.0f,
    val appliedChanges: List<String> = emptyList(),
    val recommendationText: String = "",
    val reason: String = ""
)

data class AiProcessingResult(
    val originalUri: Uri,
    val processedVideoUri: Uri?,
    val originalRatio: TargetRatio,
    val targetRatio: TargetRatio,
    val appliedActions: List<AiActionType>,
    val skippedActionsWithReason: Map<AiActionType, String> = emptyMap(),
    val operationReports: List<OperationReport> = emptyList(),
    val validationResult: ValidationResult? = null,
    val finalQuality: QualityScores,
    val thumbnails: List<ThumbnailCandidate>,
    val outputFileName: String,
    val processedDurationMs: Long,
    val isRealProcessed: Boolean = true,
    val outputWidth: Int = 1080,
    val outputHeight: Int = 1920,
    val reframeQualityScore: Int = 92,
    val reframeQualityReason: String = "92/100 • Excellent subject tracking",
    val reframeAttempts: Int = 1,
    val contentType: ContentType = ContentType.GENERAL,
    val audioIntelligenceResult: AudioIntelligenceResult? = null,
    val originalAudioUri: Uri? = null,
    val enhancedAudioUri: Uri? = null,
    val audioAppliedChanges: List<String> = emptyList(),
    val audioRecommendationText: String = "",
    val visualAnalysisResult: VisualAnalysisResult? = null,
    val visualAppliedChanges: List<String> = emptyList(),
    val visualRecommendationText: String = "",
    val targetResolutionLabel: String = "1080p",
    val finalQcJob: FinalQcJob? = null
)

enum class QcStatus {
    CHECKING,
    PASSED,
    RETRY_REQUIRED,
    FAILED
}

data class FinalQcJob(
    val jobId: String,
    val sourceVideoUri: Uri,
    val outputVideoUri: Uri,

    val sourceDurationMs: Long,
    val outputDurationMs: Long,

    val sourceWidth: Int,
    val sourceHeight: Int,
    val outputWidth: Int,
    val outputHeight: Int,

    val sourceFps: Float,
    val outputFps: Float,

    val sourceHasAudio: Boolean,
    val outputHasAudio: Boolean,

    val ratioValid: Boolean,
    val durationValid: Boolean,
    val fpsValid: Boolean,
    val audioValid: Boolean,
    val syncValid: Boolean,
    val frameValid: Boolean,
    val subjectValid: Boolean,
    val blackFrameValid: Boolean,
    val playbackValid: Boolean,

    val qualityScore: Int, // Calculated dynamically

    val errors: List<String> = emptyList(),
    val warnings: List<String> = emptyList(),

    val status: QcStatus = QcStatus.CHECKING,
    val outputFingerprint: String = "",
    val retryCount: Int = 1,

    val audioStatusText: String = "AAC 44.1kHz • Synchronized",
    val syncStatusText: String = "0ms drift",
    val subjectStatusText: String = "Subject & Face Preserved",
    val visualStatusText: String = "Frame decoding passed"
)

