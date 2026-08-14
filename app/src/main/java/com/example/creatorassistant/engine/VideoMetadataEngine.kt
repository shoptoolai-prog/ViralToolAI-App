package com.example.creatorassistant.engine

import android.content.Context
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.example.creatorassistant.domain.VideoMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoMetadataEngine(private val context: Context) {

    suspend fun extractMetadata(videoUri: Uri): VideoMetadata = withContext(Dispatchers.IO) {
        val retriever = MediaMetadataRetriever()
        var width = 1080
        var height = 1920
        var durationMs = 0L
        var bitrate = 0L
        var videoCodec = "Unknown"
        var audioCodec = "None"
        var audioSampleRate = 44100
        var audioChannels = 2
        var rotationDegrees = 0
        var fileSizeBytes = 0L

        try {
            retriever.setDataSource(context, videoUri)
            val wStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            val hStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            val durStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val bitStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)
            val rotStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION) ?: "0"
            val mimeStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)

            width = wStr?.toIntOrNull() ?: 1080
            height = hStr?.toIntOrNull() ?: 1920
            durationMs = durStr?.toLongOrNull() ?: 0L
            bitrate = bitStr?.toLongOrNull() ?: 0L
            rotationDegrees = rotStr.toIntOrNull() ?: 0
            if (!mimeStr.isNullOrBlank()) videoCodec = mimeStr

            // Swap dimensions if video is rotated 90 or 270 degrees
            if (rotationDegrees == 90 || rotationDegrees == 270) {
                val tmp = width
                width = height
                height = tmp
            }
        } catch (e: Exception) {
            Log.e("VideoMetadataEngine", "Retriever error: ${e.message}")
        } finally {
            try {
                retriever.release()
            } catch (e: Exception) {
                // Ignore
            }
        }

        // Use MediaExtractor for precise track inspection (FPS, Audio channels, Sample rate)
        var fps = 30f
        var hasAudioTrack = false
        val extractor = MediaExtractor()
        try {
            extractor.setDataSource(context, videoUri, null)
            val numTracks = extractor.trackCount
            for (i in 0 until numTracks) {
                val format = extractor.getTrackFormat(i)
                val mime = format.getString(MediaFormat.KEY_MIME) ?: ""
                if (mime.startsWith("video/")) {
                    if (format.containsKey(MediaFormat.KEY_FRAME_RATE)) {
                        fps = format.getInteger(MediaFormat.KEY_FRAME_RATE).toFloat()
                    }
                    videoCodec = mime.removePrefix("video/")
                } else if (mime.startsWith("audio/")) {
                    hasAudioTrack = true
                    audioCodec = mime.removePrefix("audio/")
                    if (format.containsKey(MediaFormat.KEY_SAMPLE_RATE)) {
                        audioSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE)
                    }
                    if (format.containsKey(MediaFormat.KEY_CHANNEL_COUNT)) {
                        audioChannels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("VideoMetadataEngine", "MediaExtractor error: ${e.message}")
        } finally {
            try {
                extractor.release()
            } catch (e: Exception) {
                // Ignore
            }
        }

        // Query file size
        try {
            context.contentResolver.query(videoUri, null, null, null, null)?.use { cursor ->
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (sizeIndex != -1 && cursor.moveToFirst()) {
                    fileSizeBytes = cursor.getLong(sizeIndex)
                }
            }
        } catch (e: Exception) {
            Log.e("VideoMetadataEngine", "Size query error: ${e.message}")
        }

        val aspectRatio = width.toFloat() / height.coerceAtLeast(1).toFloat()
        val calculatedRatioLabel = when {
            kotlin.math.abs(aspectRatio - (9f / 16f)) < 0.12f -> "9:16 (Portrait)"
            kotlin.math.abs(aspectRatio - (16f / 9f)) < 0.12f -> "16:9 (Landscape)"
            kotlin.math.abs(aspectRatio - 1f) < 0.12f -> "1:1 (Square)"
            kotlin.math.abs(aspectRatio - (4f / 5f)) < 0.12f -> "4:5 (Vertical Feed)"
            else -> "${width}:${height} (${"%.2f".format(aspectRatio)})"
        }

        val orientation = if (width >= height) "Landscape" else "Portrait"

        VideoMetadata(
            width = width,
            height = height,
            aspectRatio = aspectRatio,
            calculatedRatioLabel = calculatedRatioLabel,
            durationMs = durationMs,
            fps = fps,
            bitrate = bitrate,
            videoCodec = videoCodec,
            audioCodec = if (hasAudioTrack) audioCodec else "None",
            audioSampleRate = audioSampleRate,
            audioChannels = audioChannels,
            fileSizeBytes = fileSizeBytes,
            orientation = orientation,
            rotationDegrees = rotationDegrees
        )
    }
}
