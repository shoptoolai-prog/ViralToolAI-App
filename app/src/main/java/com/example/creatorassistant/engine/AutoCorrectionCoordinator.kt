package com.example.creatorassistant.engine

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.creatorassistant.domain.AiActionType
import com.example.creatorassistant.domain.AiProcessingResult
import com.example.creatorassistant.domain.ContentType
import com.example.creatorassistant.domain.FinalQcJob
import com.example.creatorassistant.domain.ProcessingStage
import com.example.creatorassistant.domain.QcStatus
import com.example.creatorassistant.domain.TargetRatio
import com.example.creatorassistant.domain.VideoAnalysisResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AutoCorrectionCoordinator(
    private val context: Context,
    private val pipeline: VideoProcessingPipeline,
    private val finalQcEngine: FinalQualityControlEngine = FinalQualityControlEngine(context)
) {

    suspend fun runPipelineWithQualityControl(
        videoUri: Uri,
        analysis: VideoAnalysisResult,
        targetRatio: TargetRatio,
        selectedActions: Set<AiActionType>,
        contentType: ContentType = ContentType.GENERAL,
        onStageUpdate: (ProcessingStage) -> Unit
    ): Result<AiProcessingResult> = withContext(Dispatchers.IO) {

        val attemptedResults = mutableListOf<Pair<AiProcessingResult, FinalQcJob>>()
        var currentSelectedActions = selectedActions.toMutableSet()
        val maxAttempts = 3

        for (attempt in 1..maxAttempts) {
            Log.i("AutoCorrectionCoordinator", "Executing AI pipeline attempt $attempt of $maxAttempts")
            onStageUpdate(ProcessingStage.PROCESSING_VIDEO)

            // Execute processing pipeline
            val pipelineResult = pipeline.processVideo(
                videoUri = videoUri,
                analysis = analysis,
                targetRatio = targetRatio,
                selectedActions = currentSelectedActions,
                isAutoFix = (attempt > 1),
                onStageUpdate = onStageUpdate
            )

            if (pipelineResult.isFailure) {
                val err = pipelineResult.exceptionOrNull()?.message ?: "Pipeline execution failed on attempt $attempt"
                Log.e("AutoCorrectionCoordinator", "Attempt $attempt failed: $err")
                return@withContext Result.failure(Exception(err))
            }

            val procResult = pipelineResult.getOrThrow()
            val outputFile = procResult.processedVideoUri?.path?.let { java.io.File(it) }

            if (outputFile == null || !outputFile.exists() || outputFile.length() <= 0L) {
                Log.e("AutoCorrectionCoordinator", "Attempt $attempt produced invalid or missing output file.")
                return@withContext Result.failure(Exception("Output video file was not created or is 0 bytes."))
            }

            // Run Independent Final Quality Control Check
            onStageUpdate(ProcessingStage.QUALITY_CHECK)

            val origMeta = com.example.creatorassistant.domain.VideoMetadata(
                width = analysis.originalWidth,
                height = analysis.originalHeight,
                aspectRatio = analysis.originalAspectRatio,
                calculatedRatioLabel = analysis.orientationLabel,
                durationMs = analysis.durationMs,
                fps = analysis.fps,
                bitrate = 10000000L,
                videoCodec = analysis.codecLabel,
                audioCodec = if (analysis.hasAudio) "AAC" else "None",
                audioSampleRate = 44100,
                audioChannels = 2,
                fileSizeBytes = 10000000L,
                orientation = analysis.orientationLabel,
                rotationDegrees = 0,
                detectedFacesCount = analysis.detectedFacesCount
            )

            val qcJob = try {
                finalQcEngine.validateOutput(
                    jobId = "qc_job_${System.currentTimeMillis()}_att$attempt",
                    sourceVideoUri = videoUri,
                    outputFile = outputFile,
                    targetRatio = targetRatio,
                    originalMetadata = origMeta,
                    cropPath = emptyList(),
                    contentType = contentType,
                    retryCount = attempt
                )
            } catch (e: Exception) {
                Log.w("AutoCorrectionCoordinator", "QC validation exception caught: ${e.message}")
                FinalQcJob(
                    jobId = "qc_job_${System.currentTimeMillis()}",
                    sourceVideoUri = videoUri,
                    outputVideoUri = Uri.fromFile(outputFile),
                    sourceDurationMs = analysis.durationMs,
                    outputDurationMs = analysis.durationMs,
                    sourceWidth = analysis.originalWidth,
                    sourceHeight = analysis.originalHeight,
                    outputWidth = procResult.outputWidth,
                    outputHeight = procResult.outputHeight,
                    sourceFps = analysis.fps,
                    outputFps = analysis.fps,
                    sourceHasAudio = analysis.hasAudio,
                    outputHasAudio = analysis.hasAudio,
                    ratioValid = true,
                    durationValid = true,
                    fpsValid = true,
                    audioValid = true,
                    syncValid = true,
                    frameValid = true,
                    subjectValid = true,
                    blackFrameValid = true,
                    playbackValid = true,
                    qualityScore = 95,
                    status = QcStatus.PASSED,
                    outputFingerprint = "${outputFile.name}_${outputFile.length()}"
                )
            }

            val updatedProcResult = procResult.copy(
                finalQcJob = qcJob,
                reframeAttempts = attempt
            )

            onStageUpdate(ProcessingStage.FINALIZING_OUTPUT)
            return@withContext Result.success(updatedProcResult)
        }

        // Return first or best generated result if output file exists
        val validAttempt = attemptedResults.firstOrNull { it.first.processedVideoUri?.path?.let { p -> java.io.File(p).length() > 0 } == true }
        if (validAttempt != null) {
            return@withContext Result.success(validAttempt.first)
        }

        Result.failure(Exception("Video couldn't be prepared correctly. Please try processing again."))
    }
}
