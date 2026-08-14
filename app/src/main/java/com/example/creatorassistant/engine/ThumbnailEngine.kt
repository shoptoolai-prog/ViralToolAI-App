package com.example.creatorassistant.engine

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import com.example.creatorassistant.domain.ThumbnailCandidate
import com.example.creatorassistant.domain.VideoAnalysisResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ThumbnailTimeSpec(
    val timeUs: Long,
    val title: String,
    val score: Int,
    val isBest: Boolean
)

class ThumbnailEngine(private val context: Context) {

    fun isBitmapBlackOrEmpty(bitmap: Bitmap?): Boolean {
        if (bitmap == null || bitmap.width <= 0 || bitmap.height <= 0) return true
        val width = bitmap.width
        val height = bitmap.height

        val stepX = (width / 10).coerceAtLeast(1)
        val stepY = (height / 10).coerceAtLeast(1)
        var totalLuminance = 0L
        var sampleCount = 0
        var brightPixels = 0

        for (x in 0 until width step stepX) {
            for (y in 0 until height step stepY) {
                val pixel = bitmap.getPixel(x, y)
                val r = (pixel shr 16) and 0xFF
                val g = (pixel shr 8) and 0xFF
                val b = pixel and 0xFF
                val luminance = (299 * r + 587 * g + 114 * b) / 1000
                totalLuminance += luminance
                if (luminance > 20) brightPixels++
                sampleCount++
            }
        }

        if (sampleCount == 0) return true
        val avgLuminance = totalLuminance / sampleCount
        return avgLuminance < 12 || (brightPixels.toFloat() / sampleCount) < 0.04f
    }

    private fun extractNonBlackFrame(
        retriever: MediaMetadataRetriever,
        durationUs: Long,
        preferredTimeUs: Long
    ): Pair<Bitmap?, Long> {
        val candidates = mutableListOf(
            preferredTimeUs,
            (durationUs * 0.10).toLong(),
            (durationUs * 0.25).toLong(),
            (durationUs * 0.33).toLong(),
            (durationUs * 0.50).toLong(),
            (durationUs * 0.66).toLong(),
            (durationUs * 0.75).toLong(),
            (durationUs * 0.90).toLong()
        ).distinct()

        var bestBitmap: Bitmap? = null
        var bestTimeUs: Long = preferredTimeUs

        for (timeUs in candidates) {
            try {
                val bmp = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                if (bmp != null) {
                    if (!isBitmapBlackOrEmpty(bmp)) {
                        Log.d("ThumbnailEngine", "Found non-black thumbnail frame at ${timeUs / 1000}ms")
                        return Pair(bmp, timeUs)
                    }
                    if (bestBitmap == null) {
                        bestBitmap = bmp
                        bestTimeUs = timeUs
                    }
                }
            } catch (e: Exception) {
                Log.w("ThumbnailEngine", "Frame decode error at $timeUs: ${e.message}")
            }
        }

        return Pair(bestBitmap, bestTimeUs)
    }

    suspend fun generateThumbnails(
        analysis: VideoAnalysisResult
    ): List<ThumbnailCandidate> = withContext(Dispatchers.IO) {
        val list = mutableListOf<ThumbnailCandidate>()
        val retriever = MediaMetadataRetriever()

        try {
            retriever.setDataSource(context, analysis.videoUri)
            val durationUs = (analysis.durationMs * 1000L).coerceAtLeast(1000000L)

            val specs = listOf(
                ThumbnailTimeSpec(durationUs / 4L, "Hook Scene", 96, true),
                ThumbnailTimeSpec(durationUs / 2L, "Peak Expression", 91, false),
                ThumbnailTimeSpec((durationUs * 3L) / 4L, "Action Frame", 88, false)
            )

            for ((idx, spec) in specs.withIndex()) {
                val (bmp, extractedTimeUs) = extractNonBlackFrame(retriever, durationUs, spec.timeUs)

                list.add(
                    ThumbnailCandidate(
                        id = "thumb_$idx",
                        timestampMs = extractedTimeUs / 1000L,
                        bitmap = bmp,
                        score = spec.score,
                        title = spec.title,
                        isBest = spec.isBest
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("ThumbnailEngine", "Error initializing retriever: ${e.message}")
            list.add(
                ThumbnailCandidate(
                    id = "thumb_default",
                    timestampMs = 1000L,
                    bitmap = null,
                    score = 90,
                    title = "AI Frame",
                    isBest = true
                )
            )
        } finally {
            try {
                retriever.release()
            } catch (e: Exception) {
                // Ignore
            }
        }

        list
    }
}
