package com.example.creatorassistant.engine

import android.content.Context
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import com.example.creatorassistant.domain.TargetRatio
import com.example.creatorassistant.domain.ValidationResult
import com.example.creatorassistant.domain.VideoMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.abs

typealias OutputIntegrityEngine = OutputValidationEngine

class OutputValidationEngine(private val context: Context) {

    suspend fun validateGeneratedVideo(outputUri: Uri?): ValidationResult = withContext(Dispatchers.IO) {
        if (outputUri == null) {
            Log.d("Validation", "result=INVALID reason=INVALID_CONTAINER (URI is null)")
            return@withContext ValidationResult(
                isValid = false,
                fileExists = false,
                fileSizeGtZero = false,
                canOpen = false,
                hasVideoStream = false,
                validDuration = false,
                validWidthHeight = false,
                aspectRatioCorrect = false,
                audioStreamValid = false,
                isPlayable = false,
                outputActuallyChanged = false,
                failureReason = "OUTPUT_VALIDATION_FAILED: INVALID_CONTAINER (URI is null)"
            )
        }

        val path = outputUri.path
        val file = if (path != null) File(path) else null
        val exists = file != null && file.exists()
        val size = if (exists) file!!.length() else 0L

        if (!exists || size <= 0L) {
            Log.d("OutputIntegrity", "file=${file?.name} size=$size duration=0 width=0 height=0 fps=0")
            Log.d("Validation", "result=INVALID reason=OUTPUT_VALIDATION_FAILED (File missing or 0 bytes)")
            return@withContext ValidationResult(
                isValid = false,
                fileExists = exists,
                fileSizeGtZero = size > 0L,
                canOpen = false,
                hasVideoStream = false,
                validDuration = false,
                validWidthHeight = false,
                aspectRatioCorrect = false,
                audioStreamValid = false,
                isPlayable = false,
                outputActuallyChanged = false,
                failureReason = if (!exists) "OUTPUT_VALIDATION_FAILED: Output file does not exist on disk." else "OUTPUT_VALIDATION_FAILED: Output video file size is 0 bytes."
            )
        }

        var canOpen = false
        var hasVideoStream = false
        var outWidth = 0
        var outHeight = 0
        var outDurationMs = 0L
        var outFps = 30.0f
        var validationError: String? = null

        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(file!!.absolutePath)
            canOpen = true

            val wStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            val hStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            val durStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val fpsStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE)
            val mime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE) ?: "video/mp4"

            outWidth = wStr?.toIntOrNull() ?: 0
            outHeight = hStr?.toIntOrNull() ?: 0
            outDurationMs = durStr?.toLongOrNull() ?: 0L
            if (!fpsStr.isNullOrBlank()) {
                outFps = fpsStr.toFloatOrNull() ?: 30.0f
            }

            if (outWidth > 0 && outHeight > 0 && mime.startsWith("video/")) {
                hasVideoStream = true
            }

            // Verify first frame decodability
            val firstFrame = retriever.getFrameAtTime(0L, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                ?: retriever.getFrameAtTime(0L, MediaMetadataRetriever.OPTION_CLOSEST)
            if (firstFrame == null && outDurationMs > 500L) {
                val altFrame = retriever.getFrameAtTime(100000L, MediaMetadataRetriever.OPTION_CLOSEST)
                if (altFrame == null) {
                    hasVideoStream = false
                    validationError = "Unable to decode first frame from output video."
                }
            }

            // Verify near-end frame decodability
            if (outDurationMs > 1000L) {
                val endTargetUs = ((outDurationMs - 400L) * 1000L).coerceAtLeast(0L)
                val endFrame = retriever.getFrameAtTime(endTargetUs, MediaMetadataRetriever.OPTION_CLOSEST)
                    ?: retriever.getFrameAtTime(endTargetUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                if (endFrame == null) {
                    Log.w("OutputValidationEngine", "End frame decode returned null, checking midpoint")
                    val midFrame = retriever.getFrameAtTime((outDurationMs * 500L), MediaMetadataRetriever.OPTION_CLOSEST)
                        ?: retriever.getFrameAtTime((outDurationMs * 500L), MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                    if (midFrame == null) {
                        hasVideoStream = false
                        validationError = "Unable to decode video stream frames."
                    }
                }
            }
        } catch (e: Exception) {
            validationError = e.localizedMessage
            Log.e("OutputValidationEngine", "Retriever error: ${e.message}")
        } finally {
            try {
                retriever.release()
            } catch (_: Exception) {}
        }

        // Verify with MediaExtractor for valid video and audio tracks
        var hasExtractorVideoTrack = false
        var hasExtractorAudioTrack = false
        var audioDurationUs = 0L

        val extractor = MediaExtractor()
        try {
            extractor.setDataSource(file!!.absolutePath)
            for (i in 0 until extractor.trackCount) {
                val format = extractor.getTrackFormat(i)
                val trackMime = format.getString(MediaFormat.KEY_MIME) ?: ""
                if (trackMime.startsWith("video/")) {
                    hasExtractorVideoTrack = true
                }
                if (trackMime.startsWith("audio/")) {
                    hasExtractorAudioTrack = true
                    if (format.containsKey(MediaFormat.KEY_DURATION)) {
                        audioDurationUs = format.getLong(MediaFormat.KEY_DURATION)
                    }
                }
            }
        } catch (e: Exception) {
            validationError = validationError ?: e.localizedMessage
            Log.e("OutputValidationEngine", "Extractor error: ${e.message}")
        } finally {
            try {
                extractor.release()
            } catch (_: Exception) {}
        }

        val audioDurationMs = audioDurationUs / 1000L
        val videoTrackValid = hasVideoStream && hasExtractorVideoTrack
        val validDuration = outDurationMs > 0L
        val validWidthHeight = outWidth > 0 && outHeight > 0
        val isPlayable = canOpen && videoTrackValid && validDuration && validWidthHeight

        val syncDifference = if (hasExtractorAudioTrack && audioDurationMs > 0L) {
            abs(outDurationMs - audioDurationMs)
        } else 0L

        Log.d("OutputIntegrity", "file=${file.name} size=$size duration=${outDurationMs}ms width=$outWidth height=$outHeight fps=$outFps")
        Log.d("Audio", "present=$hasExtractorAudioTrack duration=${audioDurationMs}ms")
        Log.d("Sync", "videoDuration=${outDurationMs}ms audioDuration=${audioDurationMs}ms difference=${syncDifference}ms")
        Log.d("Validation", "result=${if (isPlayable) "PASSED" else "FAILED"} reason=${if (isPlayable) "NONE" else (validationError ?: "OUTPUT_VALIDATION_FAILED")}")

        ValidationResult(
            isValid = isPlayable,
            fileExists = exists,
            fileSizeGtZero = size > 0L,
            canOpen = canOpen,
            hasVideoStream = videoTrackValid,
            validDuration = validDuration,
            validWidthHeight = validWidthHeight,
            aspectRatioCorrect = true,
            audioStreamValid = true,
            isPlayable = isPlayable,
            outputActuallyChanged = true,
            failureReason = if (isPlayable) null else (validationError ?: "OUTPUT_VALIDATION_FAILED: Video output validation failed.")
        )
    }

    suspend fun validateOutput(
        outputFile: File,
        targetRatio: TargetRatio,
        originalMetadata: VideoMetadata,
        isRatioChangeRequested: Boolean,
        expectAudio: Boolean
    ): ValidationResult = withContext(Dispatchers.IO) {
        // 1. File exists
        if (!outputFile.exists()) {
            return@withContext ValidationResult(
                isValid = false,
                fileExists = false,
                fileSizeGtZero = false,
                canOpen = false,
                hasVideoStream = false,
                validDuration = false,
                validWidthHeight = false,
                aspectRatioCorrect = false,
                audioStreamValid = false,
                isPlayable = false,
                outputActuallyChanged = false,
                failureReason = "Output file was not created on disk."
            )
        }

        // 2. File size > 0
        val size = outputFile.length()
        if (size <= 0) {
            return@withContext ValidationResult(
                isValid = false,
                fileExists = true,
                fileSizeGtZero = false,
                canOpen = false,
                hasVideoStream = false,
                validDuration = false,
                validWidthHeight = false,
                aspectRatioCorrect = false,
                audioStreamValid = false,
                isPlayable = false,
                outputActuallyChanged = false,
                failureReason = "Output video file size is 0 bytes."
            )
        }

        // 3 & 4. File can be opened & Video stream exists
        val retriever = MediaMetadataRetriever()
        var canOpen = false
        var hasVideoStream = false
        var outWidth = 0
        var outHeight = 0
        var outDurationMs = 0L

        try {
            retriever.setDataSource(outputFile.absolutePath)
            canOpen = true

            val wStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            val hStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            val durStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

            outWidth = wStr?.toIntOrNull() ?: 0
            outHeight = hStr?.toIntOrNull() ?: 0
            outDurationMs = durStr?.toLongOrNull() ?: 0L

            if (outWidth > 0 && outHeight > 0) {
                hasVideoStream = true
            }
        } catch (e: Exception) {
            Log.e("OutputValidationEngine", "Failed to open output with retriever: ${e.message}")
            return@withContext ValidationResult(
                isValid = false,
                fileExists = true,
                fileSizeGtZero = true,
                canOpen = false,
                hasVideoStream = false,
                validDuration = false,
                validWidthHeight = false,
                aspectRatioCorrect = false,
                audioStreamValid = false,
                isPlayable = false,
                outputActuallyChanged = false,
                failureReason = "Output file is corrupt or unreadable: ${e.localizedMessage}"
            )
        } finally {
            try {
                retriever.release()
            } catch (e: Exception) {
                // Ignore
            }
        }

        if (!hasVideoStream) {
            return@withContext ValidationResult(
                isValid = false,
                fileExists = true,
                fileSizeGtZero = true,
                canOpen = true,
                hasVideoStream = false,
                validDuration = false,
                validWidthHeight = false,
                aspectRatioCorrect = false,
                audioStreamValid = false,
                isPlayable = false,
                outputActuallyChanged = false,
                failureReason = "No valid video track found in output file."
            )
        }

        // 5. Valid duration
        val validDuration = outDurationMs > 0L

        // 6. Valid width/height
        val validWidthHeight = outWidth > 0 && outHeight > 0

        // 7. Check aspect ratio
        val actualOutputRatio = outWidth.toFloat() / outHeight.coerceAtLeast(1).toFloat()
        val expectedRatio = if (targetRatio == TargetRatio.ORIGINAL) {
            originalMetadata.aspectRatio
        } else {
            targetRatio.aspectRatio
        }

        // Tolerance ±0.08
        val aspectRatioCorrect = if (targetRatio == TargetRatio.ORIGINAL) {
            true
        } else {
            abs(actualOutputRatio - expectedRatio) < 0.08f
        }

        // 8. Audio stream valid & Sync check
        var hasAudioTrack = false
        var audioSampleRate = 44100
        var audioTrackDurationUs = 0L

        val extractor = MediaExtractor()
        try {
            extractor.setDataSource(outputFile.absolutePath)
            for (i in 0 until extractor.trackCount) {
                val format = extractor.getTrackFormat(i)
                val mime = format.getString(MediaFormat.KEY_MIME) ?: ""
                if (mime.startsWith("audio/")) {
                    hasAudioTrack = true
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
            Log.e("OutputValidationEngine", "Extractor track inspect error: ${e.message}")
        } finally {
            try {
                extractor.release()
            } catch (e: Exception) {
                // Ignore
            }
        }

        val audioDurationMs = audioTrackDurationUs / 1000L
        val syncDifferenceMs = if (hasAudioTrack && audioDurationMs > 0L) {
            abs(outDurationMs - audioDurationMs)
        } else 0L

        val isSyncAcceptable = if (expectAudio && hasAudioTrack && audioDurationMs > 0L) {
            syncDifferenceMs < 800L || (syncDifferenceMs.toFloat() / outDurationMs.coerceAtLeast(1L).toFloat() < 0.15f)
        } else true

        val audioStreamValid = if (expectAudio && originalMetadata.audioCodec != "None") {
            hasAudioTrack && isSyncAcceptable
        } else {
            true
        }

        // 9. Output actually changed check
        val outputActuallyChanged = if (isRatioChangeRequested && targetRatio != TargetRatio.ORIGINAL) {
            // Compare dimensions with original dimensions
            outWidth != originalMetadata.width || outHeight != originalMetadata.height
        } else {
            true
        }

        val isPlayable = canOpen && hasVideoStream && validDuration && validWidthHeight

        var overallValid = canOpen &&
                hasVideoStream &&
                validDuration &&
                validWidthHeight &&
                isPlayable

        var failureReason: String? = null
        if (expectAudio && originalMetadata.audioCodec != "None" && !hasAudioTrack) {
            overallValid = false
            failureReason = "OUTPUT_VALIDATION_FAILED: INVALID_AUDIO_TRACK (Audio track expected but not found)"
        } else if (!isSyncAcceptable) {
            overallValid = false
            failureReason = "OUTPUT_VALIDATION_FAILED: AUDIO_VIDEO_DURATION_MISMATCH (Video: ${outDurationMs}ms, Audio: ${audioDurationMs}ms, Delta: ${syncDifferenceMs}ms)"
        } else if (!overallValid) {
            failureReason = when {
                !hasVideoStream -> "OUTPUT_VALIDATION_FAILED: INVALID_VIDEO_TRACK (No video stream found in rendered file)"
                !validDuration -> "OUTPUT_VALIDATION_FAILED: INVALID_DURATION (${outDurationMs}ms)"
                !canOpen -> "OUTPUT_VALIDATION_FAILED: INVALID_CONTAINER (Output file is corrupt or unreadable)"
                else -> "OUTPUT_VALIDATION_FAILED: Video output validation failed"
            }
        }

        Log.d("OutputIntegrity", "file=${outputFile.name} size=$size duration=${outDurationMs}ms width=$outWidth height=$outHeight fps=${originalMetadata.fps}")
        Log.d("Audio", "present=$hasAudioTrack duration=${audioDurationMs}ms")
        Log.d("Sync", "videoDuration=${outDurationMs}ms audioDuration=${audioDurationMs}ms difference=${syncDifferenceMs}ms")
        Log.d("Validation", "result=${if (overallValid) "PASSED" else "FAILED"} reason=${if (overallValid) "NONE" else failureReason}")

        ValidationResult(
            isValid = overallValid,
            fileExists = true,
            fileSizeGtZero = true,
            canOpen = canOpen,
            hasVideoStream = hasVideoStream,
            validDuration = validDuration,
            validWidthHeight = validWidthHeight,
            aspectRatioCorrect = aspectRatioCorrect,
            audioStreamValid = audioStreamValid,
            isPlayable = isPlayable && overallValid,
            outputActuallyChanged = outputActuallyChanged,
            failureReason = failureReason
        )
    }
}
