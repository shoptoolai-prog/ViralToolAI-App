package com.example.creatorassistant.engine

import android.util.Log

enum class PipelineStage(
    val stageNumber: Int,
    val label: String,
    val description: String,
    val defaultProgress: Int
) {
    UPLOAD(1, "Upload", "Uploading video...", 10),
    VALIDATE(2, "Validate", "Validating video format & integrity...", 25),
    ANALYZE(3, "Analyze", "Analyzing video metadata & audio tracks...", 40),
    PROCESS(4, "Process", "Preparing transformation plan...", 55),
    RENDER(5, "Render", "Rendering video frames...", 75),
    VERIFY_OUTPUT(6, "Verify Output", "Verifying output playback & quality...", 90),
    FINAL_PREVIEW(7, "Final Preview", "Preparing preview...", 100)
}

enum class EngineJobState {
    QUEUED,
    RUNNING,
    COMPLETED,
    FAILED,
    CANCELLED
}

data class StageStatus(
    val stage: PipelineStage,
    val state: EngineJobState,
    val progressPercent: Int,
    val statusMessage: String,
    val errorMessage: String? = null,
    val timestampMs: Long = System.currentTimeMillis()
)

data class ProcessingJobReport(
    val jobId: String,
    val overallState: EngineJobState,
    val currentStage: PipelineStage,
    val stageStatuses: Map<PipelineStage, StageStatus>,
    val overallProgressPercent: Int,
    val failureReason: String? = null
)

class CentralProcessingManager {

    private var currentJobId: String? = null
    private var overallState: EngineJobState = EngineJobState.QUEUED
    private var currentStage: PipelineStage = PipelineStage.UPLOAD
    private val stageMap = mutableMapOf<PipelineStage, StageStatus>()
    private var listener: ((ProcessingJobReport) -> Unit)? = null

    init {
        resetJob("initial_job")
    }

    fun resetJob(jobId: String) {
        currentJobId = jobId
        overallState = EngineJobState.QUEUED
        currentStage = PipelineStage.UPLOAD
        stageMap.clear()

        PipelineStage.values().forEach { stage ->
            stageMap[stage] = StageStatus(
                stage = stage,
                state = EngineJobState.QUEUED,
                progressPercent = 0,
                statusMessage = "Queued for ${stage.label}"
            )
        }
        notifyUpdate()
    }

    fun setListener(onUpdate: (ProcessingJobReport) -> Unit) {
        listener = onUpdate
        notifyUpdate()
    }

    fun updateStage(
        stage: PipelineStage,
        state: EngineJobState,
        progressPercent: Int,
        message: String,
        error: String? = null
    ) {
        currentStage = stage
        if (state == EngineJobState.RUNNING && overallState != EngineJobState.CANCELLED) {
            overallState = EngineJobState.RUNNING
        } else if (state == EngineJobState.FAILED) {
            overallState = EngineJobState.FAILED
        } else if (state == EngineJobState.CANCELLED) {
            overallState = EngineJobState.CANCELLED
        }

        stageMap[stage] = StageStatus(
            stage = stage,
            state = state,
            progressPercent = progressPercent.coerceIn(0, 100),
            statusMessage = message,
            errorMessage = error
        )

        Log.d("CentralProcessingManager", "Job $currentJobId | Stage: ${stage.name} | State: ${state.name} | Progress: $progressPercent% | Msg: $message")
        notifyUpdate()
    }

    fun markCompleted(message: String = "Processing completed successfully") {
        overallState = EngineJobState.COMPLETED
        currentStage = PipelineStage.FINAL_PREVIEW
        stageMap[PipelineStage.FINAL_PREVIEW] = StageStatus(
            stage = PipelineStage.FINAL_PREVIEW,
            state = EngineJobState.COMPLETED,
            progressPercent = 100,
            statusMessage = message
        )
        notifyUpdate()
    }

    fun markFailed(stage: PipelineStage, reason: String) {
        overallState = EngineJobState.FAILED
        currentStage = stage
        stageMap[stage] = StageStatus(
            stage = stage,
            state = EngineJobState.FAILED,
            progressPercent = stageMap[stage]?.progressPercent ?: 0,
            statusMessage = "Failed: $reason",
            errorMessage = reason
        )
        notifyUpdate()
    }

    fun markCancelled(reason: String = "Cancelled by user") {
        overallState = EngineJobState.CANCELLED
        stageMap[currentStage] = StageStatus(
            stage = currentStage,
            state = EngineJobState.CANCELLED,
            progressPercent = stageMap[currentStage]?.progressPercent ?: 0,
            statusMessage = reason,
            errorMessage = reason
        )
        notifyUpdate()
    }

    fun getReport(): ProcessingJobReport {
        val currentStageStatus = stageMap[currentStage]
        val progress = when (overallState) {
            EngineJobState.COMPLETED -> 100
            EngineJobState.FAILED, EngineJobState.CANCELLED -> currentStageStatus?.progressPercent ?: 0
            else -> currentStageStatus?.progressPercent ?: currentStage.defaultProgress
        }

        val firstError = stageMap.values.firstOrNull { it.state == EngineJobState.FAILED }?.errorMessage

        return ProcessingJobReport(
            jobId = currentJobId ?: "unknown_job",
            overallState = overallState,
            currentStage = currentStage,
            stageStatuses = stageMap.toMap(),
            overallProgressPercent = progress,
            failureReason = firstError
        )
    }

    private fun notifyUpdate() {
        listener?.invoke(getReport())
    }
}
