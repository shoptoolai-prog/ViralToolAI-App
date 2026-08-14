package com.example.creatorassistant.engine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import com.example.creatorassistant.domain.ContentType
import com.example.creatorassistant.domain.FinalQcJob
import com.example.creatorassistant.domain.QcStatus
import com.example.creatorassistant.domain.ReframeCropWindow
import com.example.creatorassistant.domain.TargetRatio
import com.example.creatorassistant.domain.VideoMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.abs

class FinalQualityControlEngine(private val context: Context) {

    suspend fun validateOutput(
        jobId: String,
        sourceVideoUri: Uri,
        outputFile: File,
        targetRatio: TargetRatio,
        originalMetadata: VideoMetadata,
        cropPath: List<ReframeCropWindow> = emptyList(),
        contentType: ContentType = ContentType.GENERAL,
        retryCount: Int = 1
    ): FinalQcJob = withContext(Dispatchers.IO) {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        val outputVideoUri = Uri.fromFile(outputFile)
        val fingerprint = "${outputFile.name}_${outputFile.length()}_${outputFile.lastModified()}"

        // 1. FILE EXISTENCE & READABILITY CHECK
        if (!outputFile.exists() || outputFile.length() <= 0L || !outputFile.canRead()) {
            errors.add("Exported file does not exist, is 0 bytes, or cannot be read.")
            return@withContext FinalQcJob(
                jobId = jobId,
                sourceVideoUri = sourceVideoUri,
                outputVideoUri = outputVideoUri,
                sourceDurationMs = originalMetadata.durationMs,
                outputDurationMs = 0L,
                sourceWidth = originalMetadata.width,
                sourceHeight = originalMetadata.height,
                outputWidth = 0,
                outputHeight = 0,
                sourceFps = originalMetadata.fps,
                outputFps = 0f,
                sourceHasAudio = originalMetadata.audioCodec != "None",
                outputHasAudio = false,
                ratioValid = false,
                durationValid = false,
                fpsValid = false,
                audioValid = false,
                syncValid = false,
                frameValid = false,
                subjectValid = false,
                blackFrameValid = false,
                playbackValid = false,
                qualityScore = 0,
                errors = errors,
                status = QcStatus.FAILED,
                outputFingerprint = fingerprint,
                retryCount = retryCount
            )
        }

        // 2. VIDEO DECODER & METADATA CHECK
        val retriever = MediaMetadataRetriever()
        var outWidth = 0
        var outHeight = 0
        var outDurationMs = 0L
        var outFps = originalMetadata.fps
        var canOpenContainer = false

        try {
            retriever.setDataSource(outputFile.absolutePath)
            canOpenContainer = true

            val wStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            val hStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            val durStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val fpsStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE)

            outWidth = wStr?.toIntOrNull() ?: 0
            outHeight = hStr?.toIntOrNull() ?: 0
            outDurationMs = durStr?.toLongOrNull() ?: 0L
            if (!fpsStr.isNullOrBlank()) {
                outFps = fpsStr.toFloatOrNull() ?: originalMetadata.fps
            }
        } catch (e: Exception) {
            Log.e("FinalQcEngine", "Failed to decode exported file metadata: ${e.message}")
            errors.add("Corrupted media container: ${e.localizedMessage}")
        }

        val fileIntegrityValid = canOpenContainer && outWidth > 0 && outHeight > 0

        if (!fileIntegrityValid) {
            errors.add("Media container could not be parsed by independent decoder.")
            try { retriever.release() } catch (_: Exception) {}
            return@withContext FinalQcJob(
                jobId = jobId,
                sourceVideoUri = sourceVideoUri,
                outputVideoUri = outputVideoUri,
                sourceDurationMs = originalMetadata.durationMs,
                outputDurationMs = outDurationMs,
                sourceWidth = originalMetadata.width,
                sourceHeight = originalMetadata.height,
                outputWidth = outWidth,
                outputHeight = outHeight,
                sourceFps = originalMetadata.fps,
                outputFps = outFps,
                sourceHasAudio = originalMetadata.audioCodec != "None",
                outputHasAudio = false,
                ratioValid = false,
                durationValid = false,
                fpsValid = false,
                audioValid = false,
                syncValid = false,
                frameValid = false,
                subjectValid = false,
                blackFrameValid = false,
                playbackValid = false,
                qualityScore = 10,
                errors = errors,
                status = QcStatus.FAILED,
                outputFingerprint = fingerprint,
                retryCount = retryCount
            )
        }

        // 3. FRAME SAMPLING & BLACK/CORRUPT FRAME DETECTION
        val sampleTimestamps = if (outDurationMs > 3000L) {
            listOf(
                0L,                                      // First
                (outDurationMs * 0.15).toLong() * 1000L, // Early
                (outDurationMs * 0.50).toLong() * 1000L, // Middle
                (outDurationMs * 0.85).toLong() * 1000L, // Late
                (outDurationMs * 0.98).toLong() * 1000L  // Final
            )
        } else {
            listOf(1000000L)
        }

        val sampledBitmaps = mutableListOf<Bitmap>()
        var frameDecodeSuccessCount = 0

        for (timeUs in sampleTimestamps) {
            try {
                val frame = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                if (frame != null) {
                    frameDecodeSuccessCount++
                    val scaled = Bitmap.createScaledBitmap(frame, 120, 120, true)
                    sampledBitmaps.add(scaled)
                }
            } catch (e: Exception) {
                Log.w("FinalQcEngine", "Frame decoding failed at timestamp $timeUs: ${e.message}")
            }
        }

        try { retriever.release() } catch (_: Exception) {}

        val frameValid = frameDecodeSuccessCount >= (sampleTimestamps.size * 0.6)
        if (!frameValid) {
            errors.add("Frame decoding check failed: Multiple frames were corrupted or unreadable.")
        }

        // Check for unexpected black frames
        var blackFrameCount = 0
        var totalPixelsSampled = 0
        var totalLuminanceSum = 0.0

        sampledBitmaps.forEach { bmp ->
            var frameLumSum = 0.0
            val bw = bmp.width
            val bh = bmp.height
            var frameNearBlackPixels = 0

            for (y in 0 until bh) {
                for (x in 0 until bw) {
                    val px = bmp.getPixel(x, y)
                    val r = Color.red(px)
                    val g = Color.green(px)
                    val b = Color.blue(px)
                    val lum = (0.299f * r + 0.587f * g + 0.114f * b) / 255f

                    frameLumSum += lum
                    if (lum < 0.03f) frameNearBlackPixels++
                    totalPixelsSampled++
                }
            }

            val avgFrameLum = frameLumSum / (bw * bh)
            totalLuminanceSum += avgFrameLum

            if (avgFrameLum < 0.025f && (frameNearBlackPixels.toFloat() / (bw * bh)) > 0.95f) {
                blackFrameCount++
            }
        }

        val blackFrameValid = blackFrameCount == 0
        if (!blackFrameValid) {
            errors.add("Black frame anomaly detected: $blackFrameCount sampled frames returned near-total black output.")
        }

        // 4. ASPECT RATIO VALIDATION
        val actualRatio = outWidth.toFloat() / outHeight.coerceAtLeast(1).toFloat()
        val expectedRatio = if (targetRatio == TargetRatio.ORIGINAL) {
            originalMetadata.aspectRatio
        } else {
            targetRatio.aspectRatio
        }

        val ratioDelta = abs(actualRatio - expectedRatio)
        val ratioValid = ratioDelta < 0.08f

        if (!ratioValid) {
            errors.add("Aspect ratio mismatch: Requested ${targetRatio.label} (${"%.2f".format(expectedRatio)}) but exported video actual ratio is ${"%.2f".format(actualRatio)} (${outWidth}x${outHeight}).")
        }

        // 5. DURATION & FPS VALIDATION
        val durationDeltaMs = abs(originalMetadata.durationMs - outDurationMs)
        val durationValid = outDurationMs > 0L && (durationDeltaMs < maxOf(2000L, (originalMetadata.durationMs * 0.10).toLong()))
        if (!durationValid) {
            errors.add("Duration anomaly: Original ${originalMetadata.durationMs}ms vs Exported ${outDurationMs}ms.")
        }

        val fpsValid = outFps > 10f
        if (!fpsValid) {
            warnings.add("Low FPS detected: ${outFps} fps.")
        }

        // 6. AUDIO & SYNC VALIDATION
        val sourceHasAudio = originalMetadata.audioCodec != "None"
        var outputHasAudioTrack = false
        var audioSampleRate = 44100
        var audioTrackDurationUs = 0L

        val extractor = MediaExtractor()
        try {
            extractor.setDataSource(outputFile.absolutePath)
            for (i in 0 until extractor.trackCount) {
                val format = extractor.getTrackFormat(i)
                val mime = format.getString(MediaFormat.KEY_MIME) ?: ""
                if (mime.startsWith("audio/")) {
                    outputHasAudioTrack = true
                    if (format.containsKey(MediaFormat.KEY_SAMPLE_RATE)) {
                        audioSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE)
                    }
                    if (format.containsKey(MediaFormat.KEY_DURATION)) {
                        audioTrackDurationUs = format.getLong(MediaFormat.KEY_DURATION)
                    }
                    break
                }
            }
        } catch (e: Exception) {
            Log.e("FinalQcEngine", "Audio extractor inspect error: ${e.message}")
        } finally {
            try { extractor.release() } catch (_: Exception) {}
        }

        val audioValid = if (sourceHasAudio) {
            outputHasAudioTrack
        } else {
            true // Source had no audio -> PASS with NOT_PRESENT_IN_SOURCE
        }

        if (sourceHasAudio && !outputHasAudioTrack) {
            errors.add("Audio track lost: Source video contained audio but exported video lacks an audio track.")
        }

        // Sync check: compare video duration vs audio track duration
        val audioDurationMs = audioTrackDurationUs / 1000L
        val syncDeltaMs = if (sourceHasAudio && outputHasAudioTrack && audioDurationMs > 0L) {
            abs(outDurationMs - audioDurationMs)
        } else 0L

        val syncValid = syncDeltaMs < 600L
        if (!syncValid) {
            warnings.add("Potential audio/video sync drift detected: ${syncDeltaMs}ms delta.")
        }

        val audioStatusText = when {
            !sourceHasAudio -> "NOT_PRESENT_IN_SOURCE"
            outputHasAudioTrack -> "AAC ${audioSampleRate}Hz • Valid track"
            else -> "Audio track missing"
        }

        val syncStatusText = if (!sourceHasAudio) {
            "N/A (Silent source)"
        } else if (syncValid) {
            "Synchronized (${syncDeltaMs}ms drift)"
        } else {
            "Desynchronized (${syncDeltaMs}ms drift)"
        }

        // 7. SUBJECT & FACE PRESERVATION VALIDATION
        var skinPixelsDetected = 0
        var totalSkinPixelsPossible = 0

        sampledBitmaps.forEach { bmp ->
            val bw = bmp.width
            val bh = bmp.height
            for (y in 0 until bh) {
                for (x in 0 until bw) {
                    val px = bmp.getPixel(x, y)
                    val r = Color.red(px)
                    val g = Color.green(px)
                    val b = Color.blue(px)
                    if (r > 95 && g > 40 && b > 20 && (r - g) > 15 && r > g && r > b) {
                        skinPixelsDetected++
                    }
                    totalSkinPixelsPossible++
                }
            }
        }

        val faceRatio = if (totalSkinPixelsPossible > 0) skinPixelsDetected.toFloat() / totalSkinPixelsPossible else 0f
        val subjectValid = if (originalMetadata.detectedFacesCount > 0) {
            faceRatio > 0.015f // Face preserved in cropped canvas
        } else {
            true // General content
        }

        if (!subjectValid) {
            warnings.add("Subject cut hazard: Face was detected in source but appears partially clipped after reframing.")
        }

        val subjectStatusText = when {
            originalMetadata.detectedFacesCount > 0 && subjectValid -> "Face & Subject Preserved"
            originalMetadata.detectedFacesCount > 0 -> "Face partially clipped"
            else -> "${contentType.name} Subject Centered"
        }

        // 8. VISUAL QUALITY EVALUATION
        val avgOverallLum = if (sampledBitmaps.isNotEmpty()) totalLuminanceSum / sampledBitmaps.size else 0.5
        val visualQualityScore = when {
            avgOverallLum < 0.10 -> 70
            avgOverallLum > 0.90 -> 72
            else -> 92
        }

        val playbackValid = fileIntegrityValid && frameValid && blackFrameValid && ratioValid

        // 9. DYNAMIC QC SCORE CALCULATION
        // Weights:
        // File Integrity: 20%
        // Video Integrity: 15%
        // Ratio: 15%
        // Subject Preservation: 15%
        // Audio Integrity: 10%
        // Audio/Video Sync: 10%
        // Visual Quality: 10%
        // Duration/FPS: 5%

        var scoreSum = 0

        if (fileIntegrityValid) scoreSum += 20
        if (frameValid && blackFrameValid) scoreSum += 15 else if (frameValid) scoreSum += 8
        if (ratioValid) scoreSum += 15
        if (subjectValid) scoreSum += 15 else scoreSum += 5
        if (audioValid) scoreSum += 10
        if (syncValid) scoreSum += 10 else scoreSum += 4
        scoreSum += (visualQualityScore * 0.10).toInt()
        if (durationValid && fpsValid) scoreSum += 5 else if (durationValid) scoreSum += 3

        val calculatedQcScore = scoreSum.coerceIn(0, 100)

        // 10. STATUS DETERMINATION
        val criticalPass = fileIntegrityValid && frameValid && blackFrameValid && ratioValid && audioValid && playbackValid
        val status = when {
            criticalPass && calculatedQcScore >= 80 -> QcStatus.PASSED
            !criticalPass && retryCount < 3 -> QcStatus.RETRY_REQUIRED
            !criticalPass -> QcStatus.FAILED
            else -> QcStatus.PASSED
        }

        FinalQcJob(
            jobId = jobId,
            sourceVideoUri = sourceVideoUri,
            outputVideoUri = outputVideoUri,
            sourceDurationMs = originalMetadata.durationMs,
            outputDurationMs = outDurationMs,
            sourceWidth = originalMetadata.width,
            sourceHeight = originalMetadata.height,
            outputWidth = outWidth,
            outputHeight = outHeight,
            sourceFps = originalMetadata.fps,
            outputFps = outFps,
            sourceHasAudio = sourceHasAudio,
            outputHasAudio = outputHasAudioTrack,
            ratioValid = ratioValid,
            durationValid = durationValid,
            fpsValid = fpsValid,
            audioValid = audioValid,
            syncValid = syncValid,
            frameValid = frameValid,
            subjectValid = subjectValid,
            blackFrameValid = blackFrameValid,
            playbackValid = playbackValid,
            qualityScore = calculatedQcScore,
            errors = errors,
            warnings = warnings,
            status = status,
            outputFingerprint = fingerprint,
            retryCount = retryCount,
            audioStatusText = audioStatusText,
            syncStatusText = syncStatusText,
            subjectStatusText = subjectStatusText,
            visualStatusText = if (frameValid && blackFrameValid) "Frame decoding & luminance passed" else "Frame decoding issue"
        )
    }
}
