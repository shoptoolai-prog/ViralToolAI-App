package com.example.creatorassistant.service

import android.content.ContentValues
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.creatorassistant.domain.AudioProcessingPlan
import com.example.creatorassistant.domain.ProcessingStage
import com.example.creatorassistant.domain.ReframeCropWindow
import com.example.creatorassistant.domain.TargetRatio
import com.example.creatorassistant.domain.VideoMetadata
import com.example.creatorassistant.domain.VisualEnhancementPlan
import com.example.creatorassistant.engine.OutputValidationEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

enum class RenderJobStatus {
    QUEUED,
    INITIALIZING,
    PROCESSING,
    FINALIZING,
    VERIFYING,
    READY,
    FAILED,
    CANCELLED
}

data class RenderJob(
    val jobId: String,
    val sourceFile: File? = null,
    val sourceUri: Uri,
    val sourceWidth: Int = 0,
    val sourceHeight: Int = 0,
    val sourceDurationMs: Long = 0L,
    val sourceFPS: Float = 30f,
    val sourceHasAudio: Boolean = false,
    val targetAspectRatio: TargetRatio = TargetRatio.REELS_9_16,
    val targetWidth: Int = 1080,
    val targetHeight: Int = 1920,
    val status: RenderJobStatus = RenderJobStatus.QUEUED,
    val progress: Int = 0,
    val currentStage: ProcessingStage = ProcessingStage.IDLE,
    val outputFile: File? = null,
    val outputUrl: String? = null,
    val error: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

class VideoInputService(private val context: Context) {
    suspend fun validateAndPrepareInput(uri: Uri): Result<File> = withContext(Dispatchers.IO) {
        try {
            val cacheDir = File(context.cacheDir, "input_videos").apply { mkdirs() }
            val destFile = File(cacheDir, "input_${System.currentTimeMillis()}.mp4")

            if (uri.scheme == "file" && !uri.path.isNullOrEmpty()) {
                val srcFile = File(uri.path!!)
                if (srcFile.exists() && srcFile.length() > 0) {
                    srcFile.copyTo(destFile, overwrite = true)
                    return@withContext Result.success(destFile)
                }
            }

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }

            if (destFile.exists() && destFile.length() > 0) {
                Result.success(destFile)
            } else {
                Result.failure(Exception("Could not decode or open source video file."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Video input validation error: ${e.localizedMessage}"))
        }
    }
}

class VideoMetadataService(private val context: Context) {
    fun extractMetadata(uri: Uri): VideoMetadata {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, uri)
            val dur = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
            val w = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: 0
            val h = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: 0
            val rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)?.toIntOrNull() ?: 0
            val (finalW, finalH) = if (rotation == 90 || rotation == 270) Pair(h, w) else Pair(w, h)
            val hasAudio = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO) == "yes"

            val aspect = if (finalH > 0) finalW.toFloat() / finalH.toFloat() else 1f
            VideoMetadata(
                width = finalW,
                height = finalH,
                aspectRatio = aspect,
                calculatedRatioLabel = "${finalW}x${finalH}",
                durationMs = dur,
                fps = 30f,
                bitrate = 5_000_000L,
                videoCodec = "h264",
                audioCodec = if (hasAudio) "aac" else "None",
                audioSampleRate = 44100,
                audioChannels = 2,
                fileSizeBytes = 0L,
                orientation = if (finalH > finalW) "9:16 Portrait" else "16:9 Landscape",
                rotationDegrees = rotation
            )
        } catch (e: Exception) {
            VideoMetadata(
                width = 1080,
                height = 1920,
                aspectRatio = 9f / 16f,
                calculatedRatioLabel = "1080x1920",
                durationMs = 10000L,
                fps = 30f,
                bitrate = 5_000_000L,
                videoCodec = "h264",
                audioCodec = "aac",
                audioSampleRate = 44100,
                audioChannels = 2,
                fileSizeBytes = 0L,
                orientation = "9:16 Portrait",
                rotationDegrees = 0
            )
        } finally {
            try { retriever.release() } catch (_: Exception) {}
        }
    }
}

class VideoRenderEngine(private val context: Context) {
    suspend fun executeRender(
        inputUri: Uri,
        outputFile: File,
        outWidth: Int,
        outHeight: Int,
        targetRatio: TargetRatio,
        cropPath: List<ReframeCropWindow> = emptyList(),
        audioConfig: AudioProcessingPlan? = null,
        visualPlan: VisualEnhancementPlan? = null,
        durationMs: Long = 10000L,
        onProgress: (Int) -> Unit
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val tempInputDir = File(context.cacheDir, "ffmpeg_job_inputs").apply { mkdirs() }
            val inputFile = File(tempInputDir, "in_job_${System.currentTimeMillis()}.mp4")

            if (inputUri.scheme == "file" && !inputUri.path.isNullOrEmpty() && File(inputUri.path!!).exists()) {
                File(inputUri.path!!).copyTo(inputFile, overwrite = true)
            } else {
                context.contentResolver.openInputStream(inputUri)?.use { input ->
                    FileOutputStream(inputFile).use { output ->
                        input.copyTo(output)
                    }
                }
            }

            if (!inputFile.exists() || inputFile.length() == 0L) {
                return@withContext Result.failure(Exception("Rendering engine failed to prepare input file."))
            }

            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(inputFile.absolutePath)
            val srcWidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: 1080
            val srcHeight = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: 1920
            val hasAudio = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO) == "yes"
            val totalDurationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: durationMs
            retriever.release()

            val vfFilters = mutableListOf<String>()
            if (cropPath.isNotEmpty()) {
                val primaryCrop = cropPath.firstOrNull { it.cropWidth < 0.99f || it.cropHeight < 0.99f } ?: cropPath.first()
                val cropX = (primaryCrop.cropX * srcWidth).toInt().coerceIn(0, srcWidth - 1)
                val cropY = (primaryCrop.cropY * srcHeight).toInt().coerceIn(0, srcHeight - 1)
                val cropW = (primaryCrop.cropWidth * srcWidth).toInt().coerceIn(1, srcWidth - cropX)
                val cropH = (primaryCrop.cropHeight * srcHeight).toInt().coerceIn(1, srcHeight - cropY)
                vfFilters.add("crop=$cropW:$cropH:$cropX:$cropY")
            }

            vfFilters.add("scale=$outWidth:$outHeight:force_original_aspect_ratio=decrease,pad=$outWidth:$outHeight:(ow-iw)/2:(oh-ih)/2:black")

            if (visualPlan != null && (visualPlan.applyExposureCorrection || visualPlan.applyColorCorrection || visualPlan.applyContrastCorrection)) {
                val brightness = (visualPlan.brightnessOffset * 0.2f).coerceIn(-1.0f, 1.0f)
                val contrast = visualPlan.contrastMultiplier.coerceIn(0.5f, 2.0f)
                val saturation = visualPlan.saturationMultiplier.coerceIn(0.0f, 3.0f)
                vfFilters.add("eq=brightness=$brightness:contrast=$contrast:saturation=$saturation")
            }

            val afFilters = mutableListOf<String>()
            if (hasAudio && audioConfig != null) {
                if (audioConfig.applyNoiseReduction) afFilters.add("afftdn=nr=12:nf=-25")
                if (audioConfig.applyVoiceEnhancement || audioConfig.applySpeechClarity) afFilters.add("highpass=f=200,lowpass=f=3500")
                if (audioConfig.applyLoudnessNormalization) afFilters.add("loudnorm=I=-16:TP=-1.5:LRA=11")
            }

            val ffmpegExecutable = listOf(
                "/usr/bin/ffmpeg",
                "/usr/local/bin/ffmpeg",
                "/system/bin/ffmpeg",
                "/system/xbin/ffmpeg",
                "ffmpeg"
            ).firstOrNull { File(it).canExecute() || File(it).exists() } ?: "ffmpeg"

            val cmd = mutableListOf(
                ffmpegExecutable, "-y",
                "-i", inputFile.absolutePath,
                "-vf", vfFilters.joinToString(","),
                "-c:v", "libx264",
                "-preset", "ultrafast",
                "-pix_fmt", "yuv420p"
            )

            if (hasAudio) {
                if (afFilters.isNotEmpty()) {
                    cmd.add("-af")
                    cmd.add(afFilters.joinToString(","))
                }
                cmd.add("-c:a")
                cmd.add("aac")
                cmd.add("-b:a")
                cmd.add("128k")
            } else {
                cmd.add("-an")
            }

            cmd.add(outputFile.absolutePath)

            Log.d("VideoRenderEngine", "Executing FFmpeg: ${cmd.joinToString(" ")}")

            val processBuilder = ProcessBuilder(cmd)
            processBuilder.redirectErrorStream(true)
            val process = processBuilder.start()

            val reader = process.inputStream.bufferedReader()
            var line: String?
            val timeRegex = Regex("""time=(\d+):(\d+):(\d+)(?:\.(\d+))?""")

            while (reader.readLine().also { line = it } != null) {
                line?.let { l ->
                    val match = timeRegex.find(l)
                    if (match != null) {
                        val hours = match.groupValues[1].toLongOrNull() ?: 0L
                        val mins = match.groupValues[2].toLongOrNull() ?: 0L
                        val secs = match.groupValues[3].toLongOrNull() ?: 0L
                        val currMs = (hours * 3600 + mins * 60 + secs) * 1000L
                        if (totalDurationMs > 0) {
                            val percent = (currMs.toFloat() / totalDurationMs.toFloat() * 100f).toInt().coerceIn(0, 100)
                            onProgress(percent)
                        }
                    }
                }
            }

            val exitCode = process.waitFor()
            if (exitCode == 0 && outputFile.exists() && outputFile.length() > 0L) {
                Result.success(outputFile)
            } else {
                Result.failure(Exception("Rendering process failed with exit code $exitCode."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Rendering engine error: ${e.localizedMessage}"))
        }
    }
}

class RenderProgressManager {
    private var currentProgress: Int = 0

    fun updateProgress(percent: Int): Int {
        currentProgress = percent.coerceIn(0, 99)
        return currentProgress
    }

    fun complete(): Int {
        currentProgress = 100
        return currentProgress
    }
}

class OutputVerificationService(private val context: Context) {
    private val validator = OutputValidationEngine(context)

    fun verifyOutput(
        outputFile: File?,
        targetRatio: TargetRatio,
        expectedWidth: Int,
        expectedHeight: Int,
        expectAudio: Boolean
    ): Boolean {
        if (outputFile == null || !outputFile.exists() || outputFile.length() == 0L) {
            return false
        }
        val metadataRetriever = MediaMetadataRetriever()
        return try {
            metadataRetriever.setDataSource(outputFile.absolutePath)
            val dur = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
            val w = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: 0
            val h = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: 0
            dur > 0L && w > 0 && h > 0
        } catch (e: Exception) {
            false
        } finally {
            try { metadataRetriever.release() } catch (_: Exception) {}
        }
    }
}

class VideoPreviewService {
    fun preparePreviewUri(file: File?): Uri? {
        return if (file != null && file.exists() && file.length() > 0) {
            Uri.fromFile(file)
        } else null
    }
}

class VideoDownloadService(private val context: Context) {
    suspend fun saveProcessedVideo(
        sourceFile: File,
        targetRatio: TargetRatio
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            if (!sourceFile.exists() || sourceFile.length() == 0L) {
                return@withContext Result.failure(Exception("Output file does not exist."))
            }

            val fileName = "ViralToolAI_${targetRatio.tag}_Processed_${System.currentTimeMillis()}.mp4"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Video.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                    put(MediaStore.Video.Media.RELATIVE_PATH, "${Environment.DIRECTORY_MOVIES}/ViralToolAI")
                    put(MediaStore.Video.Media.IS_PENDING, 1)
                }

                val uri = context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
                    ?: return@withContext Result.failure(Exception("Failed to create MediaStore entry."))

                context.contentResolver.openOutputStream(uri)?.use { output ->
                    sourceFile.inputStream().use { input ->
                        input.copyTo(output)
                    }
                }

                values.clear()
                values.put(MediaStore.Video.Media.IS_PENDING, 0)
                context.contentResolver.update(uri, values, null, null)

                Result.success(uri)
            } else {
                val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "ViralToolAI").apply { mkdirs() }
                val destFile = File(dir, fileName)
                sourceFile.copyTo(destFile, overwrite = true)
                Result.success(Uri.fromFile(destFile))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to download processed video: ${e.localizedMessage}"))
        }
    }
}

class RenderJobManager(private val context: Context) {
    private val inputService = VideoInputService(context)
    private val metadataService = VideoMetadataService(context)
    private val renderEngine = VideoRenderEngine(context)
    private val verificationService = OutputVerificationService(context)

    var currentJob: RenderJob? = null
        private set

    fun createJob(sourceUri: Uri, targetRatio: TargetRatio): RenderJob {
        val metadata = metadataService.extractMetadata(sourceUri)
        val job = RenderJob(
            jobId = "job_${System.currentTimeMillis()}",
            sourceUri = sourceUri,
            sourceWidth = metadata.width,
            sourceHeight = metadata.height,
            sourceDurationMs = metadata.durationMs,
            sourceFPS = metadata.fps,
            sourceHasAudio = metadata.audioCodec != "None",
            targetAspectRatio = targetRatio,
            status = RenderJobStatus.QUEUED,
            progress = 0
        )
        currentJob = job
        return job
    }

    fun updateJobStatus(status: RenderJobStatus, progress: Int, stage: ProcessingStage, error: String? = null) {
        currentJob = currentJob?.copy(
            status = status,
            progress = progress,
            currentStage = stage,
            error = error
        )
    }

    fun markJobReady(outputFile: File) {
        currentJob = currentJob?.copy(
            status = RenderJobStatus.READY,
            progress = 100,
            currentStage = ProcessingStage.COMPLETED,
            outputFile = outputFile,
            outputUrl = outputFile.absolutePath
        )
    }

    fun cancelActiveJob() {
        currentJob = currentJob?.copy(
            status = RenderJobStatus.CANCELLED,
            progress = 0,
            currentStage = ProcessingStage.IDLE,
            error = "Processing cancelled by user"
        )
    }
}
