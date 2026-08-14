package com.example.creatorassistant.engine

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.util.LruCache
import com.example.creatorassistant.domain.AiActionType
import com.example.creatorassistant.domain.AudioIntelligenceResult
import com.example.creatorassistant.domain.TargetRatio
import com.example.creatorassistant.domain.ValidationResult
import com.example.creatorassistant.domain.VideoAnalysisResult
import com.example.creatorassistant.domain.VideoUnderstandingResult
import com.example.creatorassistant.domain.VisualAnalysisResult
import com.example.creatorassistant.domain.VisualEnhancementPlan
import com.example.creatorassistant.engine.SmartReframeEngine.ReframeResult
import java.io.File
import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

enum class CacheStage {
    ANALYSIS,
    REFRAME,
    AUDIO_ENHANCE,
    RENDER_OUTPUT
}

data class CachedOutputEntry(
    val outputFile: File,
    val targetRatio: TargetRatio,
    val configSignature: String,
    val validationResult: ValidationResult,
    val timestamp: Long = System.currentTimeMillis()
)

data class CacheMetrics(
    val totalRequests: Int,
    val cacheHits: Int,
    val cacheMisses: Int,
    val cacheHitRate: Float,
    val framesReused: Int,
    val analysisTimeSavedMs: Long,
    val renderTimeSavedMs: Long
)

/**
 * Thread-safe centralized caching engine for analysis, decoded frames, reframing paths,
 * audio enhancements, and validated output renderings.
 */
object ProcessingCache {

    private const val TAG = "CACHE"

    // 1. Frame cache with bounded memory (max 24 small thumbnail/analysis Bitmaps ~ 5MB)
    private val frameCache = object : LruCache<String, Bitmap>(24) {
        override fun sizeOf(key: String, value: Bitmap): Int = 1
        override fun entryRemoved(evicted: Boolean, key: String, oldValue: Bitmap?, newValue: Bitmap?) {
            // Let GC manage recycled bitmaps safely
        }
    }

    // 2. High-level analysis results
    private val understandingCache = ConcurrentHashMap<String, VideoUnderstandingResult>()
    private val analysisCache = ConcurrentHashMap<String, VideoAnalysisResult>()
    private val visualAnalysisCache = ConcurrentHashMap<String, VisualAnalysisResult>()
    private val audioIntelligenceCache = ConcurrentHashMap<String, AudioIntelligenceResult>()

    // 3. Reframe paths by videoHash + TargetRatio
    private val reframeCache = ConcurrentHashMap<String, ReframeResult>()

    // 4. Enhanced audio files by videoHash + AudioConfigSignature
    private val audioEnhanceCache = ConcurrentHashMap<String, Pair<File, File>>()

    // 5. Final validated render outputs by videoHash + compositeSignature
    private val renderOutputCache = ConcurrentHashMap<String, CachedOutputEntry>()

    // Active video lock to isolate video sessions
    @Volatile
    private var activeVideoFingerprint: String? = null

    // Metrics counters
    private val totalRequestsCount = AtomicInteger(0)
    private val cacheHitsCount = AtomicInteger(0)
    private val cacheMissesCount = AtomicInteger(0)
    private val framesReusedCount = AtomicInteger(0)
    private val analysisTimeSavedMs = AtomicLong(0L)
    private val renderTimeSavedMs = AtomicLong(0L)

    /**
     * Computes a resilient fingerprint for a given video Uri.
     */
    fun computeFingerprint(context: Context, videoUri: Uri): String {
        try {
            val uriStr = videoUri.toString()
            var length = 0L
            var lastModified = 0L

            if (videoUri.scheme == "file") {
                val file = File(videoUri.path ?: "")
                if (file.exists()) {
                    length = file.length()
                    lastModified = file.lastModified()
                }
            } else {
                try {
                    context.contentResolver.openFileDescriptor(videoUri, "r")?.use { pfd ->
                        length = pfd.statSize
                    }
                } catch (_: Exception) {}
            }

            val rawKey = "${uriStr}_${length}_${lastModified}"
            val md = MessageDigest.getInstance("MD5")
            val digest = md.digest(rawKey.toByteArray())
            return digest.joinToString("") { "%02x".format(it) }.take(16)
        } catch (e: Exception) {
            return "vid_${videoUri.hashCode()}"
        }
    }

    /**
     * Initializes or switches the current working video. Clears previous session cache if video changed.
     */
    @Synchronized
    fun switchActiveVideo(newFingerprint: String) {
        if (activeVideoFingerprint != null && activeVideoFingerprint != newFingerprint) {
            Log.d(TAG, "INVALIDATED: Different video imported ($activeVideoFingerprint -> $newFingerprint). Clearing previous state.")
            clearAll()
        }
        activeVideoFingerprint = newFingerprint
    }

    // ==========================================
    // FRAME-LEVEL CACHE
    // ==========================================

    fun getFrame(videoHash: String, timestampUs: Long, w: Int, h: Int): Bitmap? {
        val key = "${videoHash}_${timestampUs}_${w}x${h}"
        synchronized(frameCache) {
            val bmp = frameCache.get(key)
            if (bmp != null && !bmp.isRecycled) {
                framesReusedCount.incrementAndGet()
                cacheHitsCount.incrementAndGet()
                totalRequestsCount.incrementAndGet()
                Log.d(TAG, "HIT: Frame at $timestampUs us for $videoHash")
                Log.d(TAG, "REUSED: Decoded Bitmap (${w}x${h})")
                return bmp
            }
        }
        totalRequestsCount.incrementAndGet()
        cacheMissesCount.incrementAndGet()
        Log.d(TAG, "MISS: Frame at $timestampUs us for $videoHash")
        return null
    }

    fun putFrame(videoHash: String, timestampUs: Long, w: Int, h: Int, bitmap: Bitmap) {
        if (bitmap.isRecycled) return
        val key = "${videoHash}_${timestampUs}_${w}x${h}"
        synchronized(frameCache) {
            frameCache.put(key, bitmap)
        }
    }

    // ==========================================
    // ANALYSIS CACHE
    // ==========================================

    fun getUnderstanding(videoHash: String): VideoUnderstandingResult? {
        totalRequestsCount.incrementAndGet()
        val result = understandingCache[videoHash]
        if (result != null) {
            cacheHitsCount.incrementAndGet()
            analysisTimeSavedMs.addAndGet(1200L)
            Log.d(TAG, "HIT: VideoUnderstanding for $videoHash")
            Log.d(TAG, "REUSED: VideoUnderstandingResult")
            return result
        }
        cacheMissesCount.incrementAndGet()
        Log.d(TAG, "MISS: VideoUnderstanding for $videoHash")
        return null
    }

    fun putUnderstanding(videoHash: String, result: VideoUnderstandingResult) {
        understandingCache[videoHash] = result
    }

    fun getAnalysis(videoHash: String): VideoAnalysisResult? {
        totalRequestsCount.incrementAndGet()
        val result = analysisCache[videoHash]
        if (result != null) {
            cacheHitsCount.incrementAndGet()
            analysisTimeSavedMs.addAndGet(1000L)
            Log.d(TAG, "HIT: VideoAnalysis for $videoHash")
            Log.d(TAG, "REUSED: VideoAnalysisResult")
            return result
        }
        cacheMissesCount.incrementAndGet()
        Log.d(TAG, "MISS: VideoAnalysis for $videoHash")
        return null
    }

    fun putAnalysis(videoHash: String, result: VideoAnalysisResult) {
        analysisCache[videoHash] = result
    }

    fun getVisualAnalysis(videoHash: String): VisualAnalysisResult? {
        val result = visualAnalysisCache[videoHash]
        if (result != null) {
            Log.d(TAG, "HIT: VisualAnalysis for $videoHash")
            Log.d(TAG, "REUSED: VisualAnalysisResult")
            return result
        }
        return null
    }

    fun putVisualAnalysis(videoHash: String, result: VisualAnalysisResult) {
        visualAnalysisCache[videoHash] = result
    }

    fun getAudioIntelligence(videoHash: String): AudioIntelligenceResult? {
        val result = audioIntelligenceCache[videoHash]
        if (result != null) {
            Log.d(TAG, "HIT: AudioIntelligence for $videoHash")
            Log.d(TAG, "REUSED: AudioIntelligenceResult")
            return result
        }
        return null
    }

    fun putAudioIntelligence(videoHash: String, result: AudioIntelligenceResult) {
        audioIntelligenceCache[videoHash] = result
    }

    // ==========================================
    // REFRAME CACHE
    // ==========================================

    fun getReframe(videoHash: String, targetRatio: TargetRatio): ReframeResult? {
        totalRequestsCount.incrementAndGet()
        val key = "${videoHash}_REFRAME_${targetRatio.name}"
        val result = reframeCache[key]
        if (result != null) {
            cacheHitsCount.incrementAndGet()
            analysisTimeSavedMs.addAndGet(800L)
            Log.d(TAG, "HIT: Reframe path for $videoHash + $targetRatio")
            Log.d(TAG, "REUSED: ReframeResult (${result.cropPath.size} keyframes)")
            return result
        }
        cacheMissesCount.incrementAndGet()
        Log.d(TAG, "MISS: Reframe path for $videoHash + $targetRatio")
        return null
    }

    fun putReframe(videoHash: String, targetRatio: TargetRatio, result: ReframeResult) {
        val key = "${videoHash}_REFRAME_${targetRatio.name}"
        reframeCache[key] = result
    }

    // ==========================================
    // AUDIO ENHANCE CACHE
    // ==========================================

    fun getAudioEnhance(videoHash: String, configSignature: String): Pair<File, File>? {
        totalRequestsCount.incrementAndGet()
        val key = "${videoHash}_AUDIO_${configSignature}"
        val pair = audioEnhanceCache[key]
        if (pair != null && pair.first.exists() && pair.second.exists() && pair.second.length() > 0L) {
            cacheHitsCount.incrementAndGet()
            renderTimeSavedMs.addAndGet(1500L)
            Log.d(TAG, "HIT: Audio enhancement for $videoHash ($configSignature)")
            Log.d(TAG, "REUSED: Enhanced audio files")
            return pair
        }
        cacheMissesCount.incrementAndGet()
        Log.d(TAG, "MISS: Audio enhancement for $videoHash ($configSignature)")
        return null
    }

    fun putAudioEnhance(videoHash: String, configSignature: String, origFile: File, enhFile: File) {
        val key = "${videoHash}_AUDIO_${configSignature}"
        if (origFile.exists() && enhFile.exists() && enhFile.length() > 0L) {
            audioEnhanceCache[key] = Pair(origFile, enhFile)
        }
    }

    // ==========================================
    // RENDER OUTPUT CACHE
    // ==========================================

    fun getRenderOutput(
        videoHash: String,
        targetRatio: TargetRatio,
        audioSig: String,
        visualSig: String
    ): CachedOutputEntry? {
        totalRequestsCount.incrementAndGet()
        val key = "${videoHash}_OUT_${targetRatio.name}_${audioSig}_${visualSig}"
        val entry = renderOutputCache[key]
        if (entry != null && entry.outputFile.exists() && entry.outputFile.length() > 0L && entry.validationResult.isValid) {
            cacheHitsCount.incrementAndGet()
            renderTimeSavedMs.addAndGet(4500L)
            Log.d(TAG, "HIT: Full Render Output for $videoHash ($key)")
            Log.d(TAG, "REUSED: Output file ${entry.outputFile.name} (${entry.outputFile.length()} bytes)")
            return entry
        }
        cacheMissesCount.incrementAndGet()
        Log.d(TAG, "MISS: Render Output for $videoHash ($key)")
        return null
    }

    fun putRenderOutput(
        videoHash: String,
        targetRatio: TargetRatio,
        audioSig: String,
        visualSig: String,
        outputFile: File,
        validation: ValidationResult
    ) {
        if (!validation.isValid || !outputFile.exists() || outputFile.length() <= 0L) {
            Log.w(TAG, "INVALIDATED: Attempted to cache invalid output file. Ignored.")
            return
        }
        val key = "${videoHash}_OUT_${targetRatio.name}_${audioSig}_${visualSig}"
        renderOutputCache[key] = CachedOutputEntry(
            outputFile = outputFile,
            targetRatio = targetRatio,
            configSignature = "${audioSig}_${visualSig}",
            validationResult = validation
        )
        Log.d(TAG, "Cached validated output file successfully: $key")
    }

    // ==========================================
    // STAGE-SPECIFIC INVALIDATION & CLEANUP
    // ==========================================

    fun invalidateStage(videoHash: String, stage: CacheStage) {
        when (stage) {
            CacheStage.ANALYSIS -> {
                understandingCache.remove(videoHash)
                analysisCache.remove(videoHash)
                visualAnalysisCache.remove(videoHash)
                audioIntelligenceCache.remove(videoHash)
                Log.d(TAG, "INVALIDATED: Analysis stage for $videoHash")
            }
            CacheStage.REFRAME -> {
                reframeCache.keys.filter { it.startsWith("${videoHash}_REFRAME_") }.forEach { reframeCache.remove(it) }
                Log.d(TAG, "INVALIDATED: Reframe stage for $videoHash")
            }
            CacheStage.AUDIO_ENHANCE -> {
                audioEnhanceCache.keys.filter { it.startsWith("${videoHash}_AUDIO_") }.forEach { audioEnhanceCache.remove(it) }
                Log.d(TAG, "INVALIDATED: Audio enhance stage for $videoHash")
            }
            CacheStage.RENDER_OUTPUT -> {
                renderOutputCache.keys.filter { it.startsWith("${videoHash}_OUT_") }.forEach { renderOutputCache.remove(it) }
                Log.d(TAG, "INVALIDATED: Render output stage for $videoHash")
            }
        }
    }

    fun invalidateIncompleteOutput(videoHash: String, targetRatio: TargetRatio) {
        renderOutputCache.keys.filter { it.startsWith("${videoHash}_OUT_${targetRatio.name}") }.forEach {
            renderOutputCache.remove(it)
        }
        Log.d(TAG, "INVALIDATED: Incomplete or cancelled output entries for $videoHash ($targetRatio)")
    }

    fun clearAll() {
        synchronized(frameCache) {
            frameCache.evictAll()
        }
        understandingCache.clear()
        analysisCache.clear()
        visualAnalysisCache.clear()
        audioIntelligenceCache.clear()
        reframeCache.clear()
        audioEnhanceCache.clear()
        renderOutputCache.clear()
        activeVideoFingerprint = null
        Log.d(TAG, "INVALIDATED: All cache entries cleared.")
    }

    fun getMetrics(): CacheMetrics {
        val total = totalRequestsCount.get().coerceAtLeast(1)
        val hits = cacheHitsCount.get()
        val misses = cacheMissesCount.get()
        val rate = (hits.toFloat() / total.toFloat()) * 100f

        return CacheMetrics(
            totalRequests = total,
            cacheHits = hits,
            cacheMisses = misses,
            cacheHitRate = rate,
            framesReused = framesReusedCount.get(),
            analysisTimeSavedMs = analysisTimeSavedMs.get(),
            renderTimeSavedMs = renderTimeSavedMs.get()
        )
    }
}
