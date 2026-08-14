package com.example.creatorassistant.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creatorassistant.domain.*
import com.example.creatorassistant.engine.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AiCreatorAssistantViewModel(
    private val context: Context
) : ViewModel() {

    private val metadataEngine = VideoMetadataEngine(context)
    private val validationEngine = OutputValidationEngine(context)
    private val videoAnalysisEngine = VideoAnalysisEngine(context)
    private val understandingEngine = VideoUnderstandingEngine(context)
    private val aiDecisionEngine = AIDecisionEngine(context)
    private val thumbnailEngine = ThumbnailEngine(context)
    private val exportManager = ExportManager(context)

    private val pipeline = VideoProcessingPipeline(
        context = context,
        metadataEngine = metadataEngine,
        validationEngine = validationEngine,
        thumbnailEngine = thumbnailEngine
    )

    private val autoCorrectionCoordinator = AutoCorrectionCoordinator(
        context = context,
        pipeline = pipeline
    )

    var pipelineStatus by mutableStateOf(PipelineStatus.IDLE)
        private set

    var currentStage by mutableStateOf(ProcessingStage.IDLE)
        private set

    var progressPercent by mutableStateOf(0)
        private set

    var retryCount by mutableStateOf(0)
        private set

    var subjectTrackingActive by mutableStateOf(true)
        private set

    var faceTrackingActive by mutableStateOf(true)
        private set

    var reframePathReady by mutableStateOf(false)
        private set

    var outputStatusText by mutableStateOf("Initializing pipeline...")
        private set

    var validationStatusText by mutableStateOf("Pending validation...")
        private set

    var analysisResult by mutableStateOf<VideoAnalysisResult?>(null)
        private set

    var understandingResult by mutableStateOf<VideoUnderstandingResult?>(null)
        private set

    var processingPlan by mutableStateOf<ProcessingPlan?>(null)
        private set

    var selectedTargetRatio by mutableStateOf(TargetRatio.REELS_9_16)
        private set

    var selectedActions by mutableStateOf<Set<AiActionType>>(emptySet())
        private set

    var isCustomizing by mutableStateOf(false)
        private set

    var processingResult by mutableStateOf<AiProcessingResult?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var selectedRetryApproach by mutableStateOf(RetryApproach.DEFAULT)
        private set

    var isSavingVideo by mutableStateOf(false)
        private set

    var saveSuccessMessage by mutableStateOf<String?>(null)
        private set

    var engineTestResult by mutableStateOf<EngineTestResult?>(null)
        private set

    var isTestingEngine by mutableStateOf(false)
        private set

    fun runEngineTest(testUri: Uri? = null) {
        val uri = testUri ?: analysisResult?.videoUri
        if (uri == null) {
            errorMessage = "Please select or import a video first to test the engine."
            return
        }
        if (isTestingEngine) return
        isTestingEngine = true
        engineTestResult = null
        viewModelScope.launch {
            val result = pipeline.testVideoEngine(uri)
            engineTestResult = result
            isTestingEngine = false
        }
    }

    fun clearEngineTestResult() {
        engineTestResult = null
    }

    private var activeProcessingJob: Job? = null

    fun onVideoSelected(uri: Uri) {
        pipelineStatus = PipelineStatus.ANALYZING
        currentStage = ProcessingStage.VIDEO_SELECTED
        errorMessage = null
        processingResult = null

        viewModelScope.launch {
            try {
                currentStage = ProcessingStage.ANALYZING_VIDEO
                val analysis = videoAnalysisEngine.analyzeVideo(uri)
                val understanding = understandingEngine.analyzeAndUnderstand(uri)
                
                analysisResult = analysis
                understandingResult = understanding

                val plan = aiDecisionEngine.createProcessingPlan(understanding, understanding.recommendedRatio)
                processingPlan = plan

                selectedTargetRatio = understanding.recommendedRatio
                selectedActions = plan.allDecisions
                    .filter { it.recommended }
                    .map { it.operation }
                    .toSet()

                pipelineStatus = PipelineStatus.IDLE
            } catch (e: Exception) {
                Log.e("AiCreatorAssistantVM", "Analysis failed: ${e.message}", e)
                errorMessage = "Unable to analyze video: ${e.localizedMessage}"
                pipelineStatus = PipelineStatus.FAILED
                currentStage = ProcessingStage.FAILED
            }
        }
    }

    fun selectTargetRatio(ratio: TargetRatio) {
        selectedTargetRatio = ratio
        understandingResult?.let { und ->
            val plan = aiDecisionEngine.createProcessingPlan(und, ratio)
            processingPlan = plan
            selectedActions = plan.allDecisions
                .filter { it.recommended }
                .map { it.operation }
                .toSet()
        }
    }

    fun toggleAction(action: AiActionType) {
        selectedActions = if (selectedActions.contains(action)) {
            selectedActions - action
        } else {
            selectedActions + action
        }
    }

    fun applyAutoRecommended() {
        understandingResult?.let { und ->
            val plan = aiDecisionEngine.createProcessingPlan(und, und.recommendedRatio)
            processingPlan = plan
            selectedTargetRatio = und.recommendedRatio
            selectedActions = plan.allDecisions
                .filter { it.recommended }
                .map { it.operation }
                .toSet()
            isCustomizing = false
        }
    }

    fun setCustomizingMode(enabled: Boolean) {
        isCustomizing = enabled
    }

    private var progressAnimationJob: Job? = null

    fun cancelProcessing() {
        pipeline.cancelProcessing()
        activeProcessingJob?.cancel()
        progressAnimationJob?.cancel()
        pipelineStatus = PipelineStatus.CANCELLED
        currentStage = ProcessingStage.IDLE
        progressPercent = 0
        errorMessage = null
    }

    fun reprocessWithOption(option: String) {
        when (option) {
            "Improve face framing" -> {
                selectedActions = selectedActions + setOf(AiActionType.AUTO_REFRAME, AiActionType.SMART_CROP)
            }
            "Keep full body visible" -> {
                selectedActions = selectedActions + setOf(AiActionType.AUTO_REFRAME)
            }
            "Keep main subject centered" -> {
                selectedActions = selectedActions + setOf(AiActionType.AUTO_REFRAME, AiActionType.SMART_CROP)
            }
            "Preserve more background" -> {
                selectedActions = selectedActions - setOf(AiActionType.SMART_CROP)
            }
            "Re-analyze entire video" -> {
                analysisResult?.let { onVideoSelected(it.videoUri) }
                return
            }
        }
        startAiProcessing(isAutoFix = true)
    }

    private suspend fun stepProgressTo(targetPercent: Int, stepDelayMs: Long = 40L) {
        val target = targetPercent.coerceIn(0, 100)
        while (progressPercent < target) {
            progressPercent += 1
            delay(stepDelayMs)
        }
    }

    fun startAiProcessing(isAutoFix: Boolean = false) {
        if (pipelineStatus == PipelineStatus.PROCESSING && activeProcessingJob?.isActive == true) {
            Log.i("AiCreatorAssistantVM", "Processing already active, ignoring duplicate start request.")
            return
        }

        val analysis = analysisResult ?: return

        errorMessage = null
        pipelineStatus = PipelineStatus.PROCESSING
        currentStage = ProcessingStage.UPLOAD_VALIDATE_VIDEO
        progressPercent = 0

        activeProcessingJob?.cancel()
        progressAnimationJob?.cancel()

        activeProcessingJob = viewModelScope.launch {
            stepProgressTo(5, stepDelayMs = 20L)

            val result = autoCorrectionCoordinator.runPipelineWithQualityControl(
                videoUri = analysis.videoUri,
                analysis = analysis,
                targetRatio = selectedTargetRatio,
                selectedActions = selectedActions,
                contentType = analysis.contentType,
                onStageUpdate = { stage ->
                    currentStage = stage
                    outputStatusText = stage.activeDescription
                    if (stage.stageNumber >= 3) subjectTrackingActive = true
                    if (stage.stageNumber >= 7) reframePathReady = true

                    val targetPercent = when (stage) {
                        ProcessingStage.READY_FOR_PREVIEW, ProcessingStage.COMPLETED -> 100
                        else -> stage.progressPercent
                    }
                    progressAnimationJob?.cancel()
                    progressAnimationJob = viewModelScope.launch {
                        stepProgressTo(targetPercent, stepDelayMs = 30L)
                    }
                }
            )

            progressAnimationJob?.join()

            if (result.isSuccess) {
                val res = result.getOrNull()
                val candidateUri = res?.processedVideoUri

                currentStage = ProcessingStage.FINALIZE_OUTPUT
                outputStatusText = "Finalizing video..."
                stepProgressTo(95, stepDelayMs = 25L)

                // 1. Initial Validation
                var validation = validationEngine.validateGeneratedVideo(candidateUri)

                // 2. Automatic One-Time Finalization/Render Retry if validation failed
                if (!validation.isValid && candidateUri != null) {
                    Log.w("AiCreatorAssistantVM", "First validation failed: ${validation.failureReason}. Attempting one automatic render-finalization retry...")
                    outputStatusText = "Finalizing video output..."
                    delay(500L) // Wait for I/O flush
                    validation = validationEngine.validateGeneratedVideo(candidateUri)
                }

                if (res != null && validation.isValid && candidateUri != null) {
                    currentStage = ProcessingStage.READY_FOR_PREVIEW
                    outputStatusText = "Video ready!"
                    stepProgressTo(100, stepDelayMs = 15L)

                    processingResult = res
                    pipelineStatus = PipelineStatus.SUCCESS
                    currentStage = ProcessingStage.COMPLETED
                    validationStatusText = "✓ Video generated & validated successfully"
                } else {
                    val err = "Video couldn't be generated correctly. Please try again."
                    errorMessage = err
                    pipelineStatus = PipelineStatus.FAILED
                    currentStage = ProcessingStage.FAILED
                }
            } else {
                val err = result.exceptionOrNull()?.message ?: "Video couldn't be generated correctly. Please try again."
                errorMessage = err
                pipelineStatus = PipelineStatus.FAILED
                currentStage = ProcessingStage.FAILED
            }
        }
    }

    fun retryWithApproach(approach: RetryApproach) {
        selectedRetryApproach = approach
        pipeline.cancelProcessing()
        currentStage = ProcessingStage.RETRYING
        viewModelScope.launch {
            delay(400)
            startAiProcessing(isAutoFix = false)
        }
    }

    fun downloadOriginal() {
        val analysis = analysisResult ?: return
        saveVideoToStorage(analysis.videoUri, TargetRatio.ORIGINAL, "Original")
    }

    fun downloadAiVersion() {
        val res = processingResult ?: return
        val qcJob = res.finalQcJob
        val isPlaybackValid = qcJob?.playbackValid == true || res.validationResult?.isValid == true

        if (pipelineStatus != PipelineStatus.SUCCESS || !isPlaybackValid) {
            errorMessage = "Output video could not be safely validated. Download unavailable."
            return
        }
        val uri = res.processedVideoUri ?: return
        saveVideoToStorage(uri, res.targetRatio, "AI Version")
    }

    private fun saveVideoToStorage(uri: Uri, ratio: TargetRatio, label: String) {
        isSavingVideo = true
        saveSuccessMessage = null
        viewModelScope.launch {
            val result = exportManager.saveVideoToDevice(uri, ratio)
            isSavingVideo = false
            if (result.isSuccess) {
                saveSuccessMessage = "Saved $label to Movies/ViralToolAI!"
            } else {
                errorMessage = "Failed to save $label to storage."
            }
        }
    }

    fun clearSaveMessage() {
        saveSuccessMessage = null
    }

    fun resetToUpload() {
        pipeline.cancelProcessing()
        pipelineStatus = PipelineStatus.IDLE
        currentStage = ProcessingStage.IDLE
        analysisResult = null
        processingResult = null
        errorMessage = null
    }
}
