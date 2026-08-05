package com.example.engine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaCodecList
import android.media.MediaFormat
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import kotlin.math.roundToInt

// ============================================================================
// MASTER PHASE E-5 — PROFESSIONAL EXPORT ENGINE (ExportEngine.kt)
// ============================================================================

enum class ExportResolution(val displayName: String, val width: Int, val height: Int) {
    RES_480P("480p SD", 854, 480),
    RES_720P("720p HD", 1280, 720),
    RES_1080P("1080p Full HD", 1920, 1080),
    RES_2K_1440P("1440p 2K QHD", 2560, 1440),
    RES_4K_2160P("2160p 4K UHD", 3840, 2160),
    ORIGINAL("Original Resolution", 1920, 1080),
    CUSTOM("Custom Resolution", 1920, 1080);

    val label: String get() = displayName
}

enum class ExportFrameRate(val fpsValue: Int, val displayName: String) {
    FPS_24(24, "24 FPS (Cinematic)"),
    FPS_25(25, "25 FPS (PAL)"),
    FPS_30(30, "30 FPS (Standard)"),
    FPS_50(50, "50 FPS (PAL Smooth)"),
    FPS_60(60, "60 FPS (Ultra Smooth)"),
    FPS_120(120, "120 FPS (High Speed)")
}

enum class VideoCodec(val displayName: String, val mimeType: String) {
    H264_AVC("H.264 / AVC", MediaFormat.MIMETYPE_VIDEO_AVC),
    H265_HEVC("H.265 / HEVC", MediaFormat.MIMETYPE_VIDEO_HEVC),
    AV1("AV1 Next-Gen", "video/av01")
}

enum class AudioCodec(val displayName: String, val sampleRate: Int, val bitrateKbps: Int) {
    AAC_STEREO_48K("AAC Stereo 48kHz", 48000, 320),
    AAC_STEREO_44K("AAC Stereo 44.1kHz", 44100, 256),
    PCM_WAV("PCM Uncompressed WAV", 48000, 1411),
    MONO_AAC("AAC Mono 48kHz", 48000, 128)
}

enum class ExportBitratePreset(val displayName: String, val multiplier: Float) {
    LOW("Low (Space Saver)", 0.6f),
    MEDIUM("Standard Quality", 1.0f),
    HIGH("High Quality (Recommended)", 1.6f),
    ULTRA("Master / Lossless", 2.5f),
    CUSTOM("Custom Bitrate", 1.0f)
}

enum class ExportFormat(val extension: String, val displayName: String) {
    MP4_VIDEO("mp4", "MP4 Video File"),
    GIF_ANIMATED("gif", "Animated GIF"),
    IMAGE_SEQUENCE("png", "PNG Image Sequence"),
    AUDIO_ONLY("m4a", "Audio Only (M4A / AAC)"),
    FRAME_SNAPSHOT("jpg", "Single Frame Snapshot")
}

enum class ExportDestination(val displayName: String, val folderName: String) {
    GALLERY_MOVIES("Gallery / Movies", Environment.DIRECTORY_MOVIES),
    DOWNLOADS("Downloads Folder", Environment.DIRECTORY_DOWNLOADS),
    CUSTOM_FOLDER("App Internal Storage", "RenderedStudioVideos")
}

data class ExportMetadata(
    var title: String = "Untitled Video Edit",
    var author: String = "Pro Video Studio User",
    var copyright: String = "All Rights Reserved",
    var keepMetadata: Boolean = true
)

enum class ExportStatus {
    QUEUED,
    RENDERING,
    PAUSED,
    COMPLETED,
    FAILED,
    CANCELLED
}

data class ExportJob(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val totalDurationMs: Long,
    val resolution: ExportResolution,
    val frameRate: ExportFrameRate,
    val format: ExportFormat,
    val videoCodec: VideoCodec,
    val audioCodec: AudioCodec,
    val bitratePreset: ExportBitratePreset,
    val customBitrateMbps: Float = 16.0f,
    val metadata: ExportMetadata = ExportMetadata(),
    val destination: ExportDestination = ExportDestination.GALLERY_MOVIES,
    
    var status: ExportStatus = ExportStatus.QUEUED,
    var progress: Float = 0.0f, // 0.0 to 1.0
    var currentFrame: Int = 0,
    var totalFrames: Int = 0,
    var currentFps: Float = 0.0f,
    var encodingSpeedX: Float = 1.0f,
    var estimatedRemainingSec: Long = 0L,
    var outputFilePath: String = "",
    var errorMessage: String? = null
)

// ============================================================================
// 1. HARDWARE CODEC MANAGER
// ============================================================================
class HardwareCodecManager {
    fun getSupportedVideoCodecs(): List<VideoCodec> {
        val supported = mutableListOf<VideoCodec>()
        supported.add(VideoCodec.H264_AVC) // Always supported

        try {
            val list = MediaCodecList(MediaCodecList.REGULAR_CODECS)
            for (info in list.codecInfos) {
                if (!info.isEncoder) continue
                for (type in info.supportedTypes) {
                    if (type.equals(MediaFormat.MIMETYPE_VIDEO_HEVC, ignoreCase = true)) {
                        if (!supported.contains(VideoCodec.H265_HEVC)) supported.add(VideoCodec.H265_HEVC)
                    }
                    if (type.equals("video/av01", ignoreCase = true)) {
                        if (!supported.contains(VideoCodec.AV1)) supported.add(VideoCodec.AV1)
                    }
                }
            }
        } catch (e: Exception) {
            // Fallback gracefully
        }
        return supported
    }

    fun calculateTargetBitrateBps(resolution: ExportResolution, fps: ExportFrameRate, preset: ExportBitratePreset, customMbps: Float): Int {
        if (preset == ExportBitratePreset.CUSTOM) {
            return (customMbps * 1_000_000).toInt()
        }
        val baseMbps = when (resolution) {
            ExportResolution.RES_480P -> 4.0f
            ExportResolution.RES_720P -> 8.0f
            ExportResolution.RES_1080P -> 16.0f
            ExportResolution.RES_2K_1440P -> 28.0f
            ExportResolution.RES_4K_2160P -> 50.0f
            else -> 16.0f
        }
        val fpsFactor = if (fps.fpsValue > 30) 1.4f else 1.0f
        return (baseMbps * preset.multiplier * fpsFactor * 1_000_000).toInt()
    }

    fun calculateEstimatedFileSizeMB(durationMs: Long, resolution: ExportResolution, fps: ExportFrameRate, preset: ExportBitratePreset, customMbps: Float): Float {
        val bps = calculateTargetBitrateBps(resolution, fps, preset, customMbps)
        val audioBps = 320_000 // 320kbps audio
        val totalBps = bps + audioBps
        val durationSec = durationMs / 1000.0f
        val bits = totalBps * durationSec
        return (bits / (8.0f * 1024.0f * 1024.0f))
    }
}

// ============================================================================
// 2. MASTER EXPORT ENGINE & PIPELINE ORCHESTRATOR
// ============================================================================
class ExportEngine private constructor(private val context: Context) {

    val hardwareCodecManager = HardwareCodecManager()
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _exportQueueState = MutableStateFlow<List<ExportJob>>(emptyList())
    val exportQueueState: StateFlow<List<ExportJob>> = _exportQueueState.asStateFlow()

    private val _activeJobState = MutableStateFlow<ExportJob?>(null)
    val activeJobState: StateFlow<ExportJob?> = _activeJobState.asStateFlow()

    private var activeJobJob: Job? = null

    fun submitExportJob(
        title: String,
        clips: List<TimelineClip>,
        texts: List<TextOverlay>,
        resolution: ExportResolution,
        frameRate: ExportFrameRate,
        format: ExportFormat,
        videoCodec: VideoCodec,
        audioCodec: AudioCodec,
        bitratePreset: ExportBitratePreset,
        customBitrateMbps: Float,
        metadata: ExportMetadata,
        destination: ExportDestination
    ): String {
        val totalDurationMs = clips.maxOfOrNull { it.startTimelineMs + it.durationOnTimelineMs } ?: 3000L

        val job = ExportJob(
            title = title,
            totalDurationMs = totalDurationMs,
            resolution = resolution,
            frameRate = frameRate,
            format = format,
            videoCodec = videoCodec,
            audioCodec = audioCodec,
            bitratePreset = bitratePreset,
            customBitrateMbps = customBitrateMbps,
            metadata = metadata,
            destination = destination,
            totalFrames = ((totalDurationMs / 1000.0f) * frameRate.fpsValue).roundToInt().coerceAtLeast(1)
        )

        val currentList = _exportQueueState.value.toMutableList()
        currentList.add(job)
        _exportQueueState.value = currentList

        processQueue(clips, texts)
        return job.id
    }

    private fun processQueue(clips: List<TimelineClip>, texts: List<TextOverlay>) {
        if (_activeJobState.value != null) return // Already running a job

        val nextJob = _exportQueueState.value.firstOrNull { it.status == ExportStatus.QUEUED } ?: return
        _activeJobState.value = nextJob

        activeJobJob = scope.launch {
            executeJobRendering(nextJob, clips, texts)
        }
    }

    private suspend fun executeJobRendering(job: ExportJob, clips: List<TimelineClip>, texts: List<TextOverlay>) {
        val realVideoEngine = RealVideoEngine.getInstance(context)

        // Target output file
        val outputDir = File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES), "StudioExports").apply { mkdirs() }
        val fileName = "Export_${System.currentTimeMillis()}.${job.format.extension}"
        val outputFile = File(outputDir, fileName)

        job.outputFilePath = outputFile.absolutePath
        job.status = ExportStatus.RENDERING

        updateJobInList(job)

        val width = job.resolution.width
        val height = job.resolution.height
        val totalFrames = job.totalFrames
        val frameIntervalMs = (1000.0f / job.frameRate.fpsValue).toLong()

        val startTime = System.currentTimeMillis()
        var processedFrames = 0

        try {
            val fileOutputStream = FileOutputStream(outputFile)

            // High precision render loop
            for (frameIdx in 0 until totalFrames) {
                if (job.status == ExportStatus.CANCELLED) break
                while (job.status == ExportStatus.PAUSED) {
                    delay(200)
                }

                val frameTimeMs = frameIdx * frameIntervalMs

                // Render pipeline call (Timeline -> Keyframes -> Filters -> Effects -> Transitions -> Text)
                val activeClip = clips.find { frameTimeMs in it.startTimelineMs..(it.startTimelineMs + it.durationOnTimelineMs) }
                val currentRenderedBitmap = realVideoEngine.renderEngine.renderFrameAtTime(
                    timelineClip = activeClip,
                    timelineTimeMs = frameTimeMs,
                    canvasWidth = width,
                    canvasHeight = height,
                    timelineClips = clips
                )

                // Overlay Text items if present
                val canvas = Canvas(currentRenderedBitmap)
                texts.forEach { txt ->
                    if (frameTimeMs in txt.startMs..(txt.startMs + txt.durationMs)) {
                        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                            color = try { Color.parseColor(txt.colorHex) } catch (e: Exception) { Color.WHITE }
                            textSize = txt.fontSizeSp * 2.5f
                        }
                        canvas.drawText(txt.text, txt.positionX, txt.positionY, paint)
                    }
                }

                // Compress & write frame data
                if (job.format == ExportFormat.FRAME_SNAPSHOT && frameIdx == totalFrames / 2) {
                    currentRenderedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, fileOutputStream)
                } else if (job.format == ExportFormat.MP4_VIDEO || job.format == ExportFormat.GIF_ANIMATED) {
                    // For MP4/GIF simulation in demo environment, stream compressed PNG/JPEG frames into file container
                    currentRenderedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fileOutputStream)
                }

                processedFrames++
                val elapsedMs = (System.currentTimeMillis() - startTime).coerceAtLeast(1L)
                val fpsNow = (processedFrames * 1000.0f) / elapsedMs
                val remainingFrames = totalFrames - processedFrames
                val remainingSec = if (fpsNow > 0) (remainingFrames / fpsNow).toLong() else 0L

                job.currentFrame = processedFrames
                job.progress = (processedFrames.toFloat() / totalFrames.toFloat()).coerceIn(0f, 1f)
                job.currentFps = fpsNow
                job.encodingSpeedX = fpsNow / job.frameRate.fpsValue
                job.estimatedRemainingSec = remainingSec

                updateJobInList(job)
                delay(10) // Allow UI yield & smooth 60fps status stream
            }

            fileOutputStream.flush()
            fileOutputStream.close()

            if (job.status != ExportStatus.CANCELLED) {
                job.status = ExportStatus.COMPLETED
                job.progress = 1.0f
                updateJobInList(job)
            }
        } catch (e: Exception) {
            job.status = ExportStatus.FAILED
            job.errorMessage = e.localizedMessage ?: "Encoding Exception"
            updateJobInList(job)
        } finally {
            _activeJobState.value = null
            processQueue(clips, texts)
        }
    }

    fun pauseJob(jobId: String) {
        val job = _exportQueueState.value.find { it.id == jobId } ?: return
        if (job.status == ExportStatus.RENDERING) {
            job.status = ExportStatus.PAUSED
            updateJobInList(job)
        }
    }

    fun resumeJob(jobId: String, clips: List<TimelineClip>, texts: List<TextOverlay>) {
        val job = _exportQueueState.value.find { it.id == jobId } ?: return
        if (job.status == ExportStatus.PAUSED) {
            job.status = ExportStatus.RENDERING
            updateJobInList(job)
        }
    }

    fun cancelJob(jobId: String) {
        val job = _exportQueueState.value.find { it.id == jobId } ?: return
        job.status = ExportStatus.CANCELLED
        updateJobInList(job)
        if (_activeJobState.value?.id == jobId) {
            activeJobJob?.cancel()
            _activeJobState.value = null
        }
    }

    private fun updateJobInList(job: ExportJob) {
        val currentList = _exportQueueState.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == job.id }
        if (index != -1) {
            currentList[index] = job
            _exportQueueState.value = currentList
        }
    }

    companion object {
        @Volatile private var instance: ExportEngine? = null
        fun getInstance(context: Context): ExportEngine {
            return instance ?: synchronized(this) {
                instance ?: ExportEngine(context.applicationContext).also { instance = it }
            }
        }
    }
}
