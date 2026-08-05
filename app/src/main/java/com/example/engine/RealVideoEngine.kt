package com.example.engine

import android.content.Context
import android.graphics.*
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.SystemClock
import android.util.LruCache
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.Stack
import kotlin.math.max
import kotlin.math.min

// ============================================================================
// MASTER PHASE E-1 — REAL VIDEO RENDERING ENGINE (RealVideoEngine.kt)
// ============================================================================

enum class MediaType {
    VIDEO_MP4, VIDEO_MOV, VIDEO_MKV, VIDEO_WEBM, VIDEO_AVI,
    IMAGE_JPEG, IMAGE_PNG, IMAGE_WEBP, IMAGE_GIF, UNKNOWN
}

data class MediaItem(
    val id: String = "media_${System.currentTimeMillis()}_${(100..999).random()}",
    val fileUri: String,
    val name: String,
    val type: MediaType,
    val durationMs: Long, // 0 for static images
    val width: Int,
    val height: Int,
    val thumbnailBitmap: Bitmap? = null
)

data class ClipTransform(
    val scaleX: Float = 1.0f,
    val scaleY: Float = 1.0f,
    val rotation: Float = 0.0f, // Degrees
    val translateX: Float = 0.0f, // Pixels
    val translateY: Float = 0.0f,
    val opacity: Float = 1.0f
)

data class TextOverlay(
    val id: String = "txt_${System.currentTimeMillis()}_${(100..999).random()}",
    val text: String,
    val colorHex: String = "#FFFFFF",
    val fontSizeSp: Float = 24f,
    val positionX: Float = 0.5f, // Normalized 0.0 to 1.0
    val positionY: Float = 0.5f,
    val startMs: Long = 0L,
    val durationMs: Long = 3000L
)

data class TimelineClip(
    val id: String = "clip_${System.currentTimeMillis()}_${(100..999).random()}",
    val mediaItem: MediaItem,
    var startTimelineMs: Long,
    var inPointMs: Long = 0L,
    var outPointMs: Long = mediaItem.durationMs.coerceAtLeast(3000L),
    var speed: Float = 1.0f, // 0.1x to 10.0x
    var volume: Float = 1.0f, // 0.0 to 2.0
    var isMuted: Boolean = false,
    var transform: ClipTransform = ClipTransform(),
    var filterStack: List<FilterLayer> = emptyList(),
    var textOverlays: List<TextOverlay> = emptyList()
) {
    val durationOnTimelineMs: Long
        get() = ((outPointMs - inPointMs) / speed).toLong().coerceAtLeast(100L)
}

data class VideoDiagnostics(
    val fps: Int = 60,
    val droppedFrames: Int = 0,
    val decodeTimeMs: Long = 2L,
    val renderTimeMs: Long = 4L,
    val memoryUsageMb: Long = 48L,
    val isDebugEnabled: Boolean = false
)

data class ProjectData(
    val projectId: String = "proj_${System.currentTimeMillis()}",
    val name: String = "ViralReel_Project_${(100..999).random()}",
    val createdAt: Long = System.currentTimeMillis(),
    val clips: List<TimelineClip> = emptyList(),
    val canvasWidth: Int = 1080,
    val canvasHeight: Int = 1920
)

// ============================================================================
// 1. MEDIA LOADER & FRAME EXTRACTOR
// ============================================================================
class MediaLoader(private val context: Context) {
    private val frameCache = LruCache<String, Bitmap>(30)

    fun detectMediaType(uriString: String): MediaType {
        val lower = uriString.lowercase()
        return when {
            lower.endsWith(".mp4") -> MediaType.VIDEO_MP4
            lower.endsWith(".mov") -> MediaType.VIDEO_MOV
            lower.endsWith(".mkv") -> MediaType.VIDEO_MKV
            lower.endsWith(".webm") -> MediaType.VIDEO_WEBM
            lower.endsWith(".avi") -> MediaType.VIDEO_AVI
            lower.endsWith(".jpg") || lower.endsWith(".jpeg") -> MediaType.IMAGE_JPEG
            lower.endsWith(".png") -> MediaType.IMAGE_PNG
            lower.endsWith(".webp") -> MediaType.IMAGE_WEBP
            lower.endsWith(".gif") -> MediaType.IMAGE_GIF
            else -> MediaType.VIDEO_MP4
        }
    }

    suspend fun loadMediaAsynchronously(uriString: String, name: String = "Imported Media"): MediaItem = withContext(Dispatchers.IO) {
        val type = detectMediaType(uriString)
        val file = File(uriString)

        var durationMs = 0L
        var width = 1080
        var height = 1920
        var thumb: Bitmap? = null

        if (type == MediaType.IMAGE_JPEG || type == MediaType.IMAGE_PNG || type == MediaType.IMAGE_WEBP) {
            val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            if (file.exists()) {
                BitmapFactory.decodeFile(file.absolutePath, opts)
                width = if (opts.outWidth > 0) opts.outWidth else 1080
                height = if (opts.outHeight > 0) opts.outHeight else 1920
                thumb = extractImageThumbnail(file)
            } else {
                thumb = createPlaceholderThumbnail(name)
            }
            durationMs = 3000L // Default image duration
        } else {
            val retriever = MediaMetadataRetriever()
            try {
                if (file.exists()) {
                    retriever.setDataSource(file.absolutePath)
                } else {
                    retriever.setDataSource(context, Uri.parse(uriString))
                }
                val durStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                durationMs = durStr?.toLongOrNull() ?: 5000L

                val wStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                val hStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                width = wStr?.toIntOrNull() ?: 1080
                height = hStr?.toIntOrNull() ?: 1920

                thumb = retriever.getFrameAtTime(1000000L, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            } catch (e: Exception) {
                thumb = createPlaceholderThumbnail(name)
            } finally {
                try { retriever.release() } catch (_: Exception) {}
            }
        }

        MediaItem(
            fileUri = uriString,
            name = name,
            type = type,
            durationMs = durationMs,
            width = width,
            height = height,
            thumbnailBitmap = thumb
        )
    }

    private fun extractImageThumbnail(file: File): Bitmap {
        val opts = BitmapFactory.Options().apply { inSampleSize = 4 }
        return BitmapFactory.decodeFile(file.absolutePath, opts) ?: createPlaceholderThumbnail(file.name)
    }

    private fun createPlaceholderThumbnail(title: String): Bitmap {
        val bmp = Bitmap.createBitmap(320, 180, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#1A202C")
        }
        canvas.drawRect(0f, 0f, 320f, 180f, paint)
        paint.color = Color.parseColor("#38E8A5")
        paint.textSize = 20f
        canvas.drawText(title.take(15), 40f, 100f, paint)
        return bmp
    }

    fun extractFrameAtTime(mediaItem: MediaItem, timeMs: Long): Bitmap {
        val cacheKey = "${mediaItem.id}_${timeMs / 100}"
        frameCache.get(cacheKey)?.let { return it }

        val file = File(mediaItem.fileUri)
        if (mediaItem.type.name.startsWith("IMAGE")) {
            if (file.exists()) {
                val bmp = BitmapFactory.decodeFile(file.absolutePath)
                if (bmp != null) {
                    frameCache.put(cacheKey, bmp)
                    return bmp
                }
            }
            return createPlaceholderFrame(mediaItem, timeMs)
        }

        val retriever = MediaMetadataRetriever()
        return try {
            if (file.exists()) {
                retriever.setDataSource(file.absolutePath)
            } else {
                retriever.setDataSource(context, Uri.parse(mediaItem.fileUri))
            }
            val frame = retriever.getFrameAtTime(timeMs * 1000L, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            if (frame != null) {
                frameCache.put(cacheKey, frame)
                frame
            } else {
                createPlaceholderFrame(mediaItem, timeMs)
            }
        } catch (e: Exception) {
            createPlaceholderFrame(mediaItem, timeMs)
        } finally {
            try { retriever.release() } catch (_: Exception) {}
        }
    }

    private fun createPlaceholderFrame(mediaItem: MediaItem, timeMs: Long): Bitmap {
        val bmp = Bitmap.createBitmap(640, 1137, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Gradient background simulation
        val shader = LinearGradient(0f, 0f, 640f, 1137f, Color.parseColor("#0F172A"), Color.parseColor("#1E293B"), Shader.TileMode.CLAMP)
        paint.shader = shader
        canvas.drawRect(0f, 0f, 640f, 1137f, paint)

        paint.shader = null
        paint.color = Color.parseColor("#38E8A5")
        paint.textSize = 32f
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText(mediaItem.name, 40f, 500f, paint)

        paint.color = Color.WHITE
        paint.textSize = 24f
        canvas.drawText("Time: ${timeMs}ms", 40f, 560f, paint)
        return bmp
    }
}

// ============================================================================
// 2. TIMELINE ENGINE
// ============================================================================
class TimelineEngine {
    private val _clips = MutableStateFlow<List<TimelineClip>>(emptyList())
    val clips: StateFlow<List<TimelineClip>> = _clips.asStateFlow()

    private val undoStack = Stack<List<TimelineClip>>()
    private val redoStack = Stack<List<TimelineClip>>()

    private fun saveUndo() {
        undoStack.push(_clips.value.map { it.copy() })
        redoStack.clear()
    }

    val totalDurationMs: Long
        get() = _clips.value.maxOfOrNull { it.startTimelineMs + it.durationOnTimelineMs } ?: 0L

    fun addClip(mediaItem: MediaItem): TimelineClip {
        saveUndo()
        val current = _clips.value.toMutableList()
        val lastEnd = current.maxOfOrNull { it.startTimelineMs + it.durationOnTimelineMs } ?: 0L

        val newClip = TimelineClip(
            mediaItem = mediaItem,
            startTimelineMs = lastEnd,
            inPointMs = 0L,
            outPointMs = if (mediaItem.durationMs > 0) mediaItem.durationMs else 3000L
        )
        current.add(newClip)
        _clips.value = current
        return newClip
    }

    fun trimClip(clipId: String, newInPointMs: Long, newOutPointMs: Long) {
        saveUndo()
        val current = _clips.value.toMutableList()
        val idx = current.indexOfFirst { it.id == clipId }
        if (idx != -1) {
            val clip = current[idx]
            val validIn = newInPointMs.coerceIn(0L, clip.mediaItem.durationMs.coerceAtLeast(3000L) - 100L)
            val validOut = newOutPointMs.coerceIn(validIn + 100L, clip.mediaItem.durationMs.coerceAtLeast(3000L))
            current[idx] = clip.copy(inPointMs = validIn, outPointMs = validOut)
            recalculateTimelinePositions(current)
            _clips.value = current
        }
    }

    fun splitClip(clipId: String, splitPointTimelineMs: Long) {
        saveUndo()
        val current = _clips.value.toMutableList()
        val idx = current.indexOfFirst { it.id == clipId }
        if (idx != -1) {
            val target = current[idx]
            val offsetMs = splitPointTimelineMs - target.startTimelineMs
            val mediaSplitPoint = target.inPointMs + (offsetMs * target.speed).toLong()

            if (mediaSplitPoint > target.inPointMs + 100L && mediaSplitPoint < target.outPointMs - 100L) {
                val clip1 = target.copy(outPointMs = mediaSplitPoint)
                val clip2 = target.copy(
                    id = "clip_${System.currentTimeMillis()}_${(100..999).random()}",
                    inPointMs = mediaSplitPoint,
                    startTimelineMs = splitPointTimelineMs
                )
                current[idx] = clip1
                current.add(idx + 1, clip2)
                recalculateTimelinePositions(current)
                _clips.value = current
            }
        }
    }

    fun moveClip(fromIndex: Int, toIndex: Int) {
        if (fromIndex in _clips.value.indices && toIndex in _clips.value.indices) {
            saveUndo()
            val list = _clips.value.toMutableList()
            val item = list.removeAt(fromIndex)
            list.add(toIndex, item)
            recalculateTimelinePositions(list)
            _clips.value = list
        }
    }

    fun duplicateClip(clipId: String) {
        saveUndo()
        val current = _clips.value.toMutableList()
        val idx = current.indexOfFirst { it.id == clipId }
        if (idx != -1) {
            val target = current[idx]
            val copy = target.copy(id = "clip_${System.currentTimeMillis()}_dup")
            current.add(idx + 1, copy)
            recalculateTimelinePositions(current)
            _clips.value = current
        }
    }

    fun deleteClip(clipId: String) {
        saveUndo()
        val current = _clips.value.filter { it.id != clipId }.toMutableList()
        recalculateTimelinePositions(current)
        _clips.value = current
    }

    fun replaceClip(clipId: String, newMediaItem: MediaItem) {
        saveUndo()
        val current = _clips.value.toMutableList()
        val idx = current.indexOfFirst { it.id == clipId }
        if (idx != -1) {
            val old = current[idx]
            current[idx] = old.copy(
                mediaItem = newMediaItem,
                inPointMs = 0L,
                outPointMs = if (newMediaItem.durationMs > 0) newMediaItem.durationMs else 3000L
            )
            recalculateTimelinePositions(current)
            _clips.value = current
        }
    }

    fun updateClipSpeed(clipId: String, speed: Float) {
        saveUndo()
        val current = _clips.value.toMutableList()
        val idx = current.indexOfFirst { it.id == clipId }
        if (idx != -1) {
            current[idx] = current[idx].copy(speed = speed.coerceIn(0.1f, 10.0f))
            recalculateTimelinePositions(current)
            _clips.value = current
        }
    }

    fun updateClipFilterStack(clipId: String, filters: List<FilterLayer>) {
        val current = _clips.value.toMutableList()
        val idx = current.indexOfFirst { it.id == clipId }
        if (idx != -1) {
            current[idx] = current[idx].copy(filterStack = filters)
            _clips.value = current
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            redoStack.push(_clips.value.map { it.copy() })
            _clips.value = undoStack.pop()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            undoStack.push(_clips.value.map { it.copy() })
            _clips.value = redoStack.pop()
        }
    }

    private fun recalculateTimelinePositions(clips: MutableList<TimelineClip>) {
        var runningTime = 0L
        for (i in clips.indices) {
            clips[i] = clips[i].copy(startTimelineMs = runningTime)
            runningTime += clips[i].durationOnTimelineMs
        }
    }

    fun getClipAtTimelineTime(timeMs: Long): TimelineClip? {
        return _clips.value.find { timeMs >= it.startTimelineMs && timeMs < (it.startTimelineMs + it.durationOnTimelineMs) }
    }
}

// ============================================================================
// 3. PLAYBACK ENGINE
// ============================================================================
class PlaybackEngine(
    private val scope: CoroutineScope,
    private val timelineEngine: TimelineEngine
) {
    private val _playheadMs = MutableStateFlow(0L)
    val playheadMs: StateFlow<Long> = _playheadMs.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private var playbackJob: Job? = null

    fun play() {
        if (_isPlaying.value) return
        _isPlaying.value = true

        playbackJob = scope.launch(Dispatchers.Default) {
            var lastTime = SystemClock.elapsedRealtime()
            while (isActive && _isPlaying.value) {
                delay(16) // ~60 FPS ticker
                val now = SystemClock.elapsedRealtime()
                val delta = (now - lastTime) * _playbackSpeed.value
                lastTime = now

                val totalDur = timelineEngine.totalDurationMs
                if (totalDur <= 0) continue

                val nextPlayhead = _playheadMs.value + delta.toLong()
                if (nextPlayhead >= totalDur) {
                    _playheadMs.value = 0L // Loop around
                } else {
                    _playheadMs.value = nextPlayhead
                }
            }
        }
    }

    fun pause() {
        _isPlaying.value = false
        playbackJob?.cancel()
        playbackJob = null
    }

    fun seekTo(timeMs: Long) {
        val totalDur = timelineEngine.totalDurationMs.coerceAtLeast(100L)
        _playheadMs.value = timeMs.coerceIn(0L, totalDur)
    }

    fun stepFrame(forward: Boolean) {
        pause()
        val frameDelta = if (forward) 33L else -33L
        seekTo(_playheadMs.value + frameDelta)
    }

    fun setSpeed(speed: Float) {
        _playbackSpeed.value = speed.coerceIn(0.1f, 10.0f)
    }
}

// ============================================================================
// 4. RENDER ENGINE & AUDIO MIXING
// ============================================================================
class RenderEngine(
    private val context: Context,
    private val mediaLoader: MediaLoader,
    private val filterRenderer: FilterRenderer
) {
    fun renderFrameAtTime(
        timelineClip: TimelineClip?,
        timelineTimeMs: Long,
        canvasWidth: Int = 1080,
        canvasHeight: Int = 1920,
        timelineClips: List<TimelineClip> = emptyList()
    ): Bitmap {
        val transitionEngine = TransitionEngine.getInstance(context)

        // Check if timelineTimeMs falls within a clip junction transition window
        if (timelineClips.size >= 2) {
            for (i in 0 until timelineClips.size - 1) {
                val clipA = timelineClips[i]
                val clipB = timelineClips[i + 1]
                val junction = transitionEngine.getTransitionForJunction(clipA.id) ?: continue

                val junctionTimeMs = clipA.startTimelineMs + clipA.durationOnTimelineMs
                val halfDur = junction.durationMs / 2
                val transStart = junctionTimeMs - halfDur
                val transEnd = junctionTimeMs + halfDur

                if (timelineTimeMs in transStart..transEnd) {
                    val progress = ((timelineTimeMs - transStart).toFloat() / junction.durationMs.toFloat()).coerceIn(0f, 1f)
                    val frameA = renderSingleClipFrame(clipA, timelineTimeMs, canvasWidth, canvasHeight)
                    val frameB = renderSingleClipFrame(clipB, timelineTimeMs, canvasWidth, canvasHeight)
                    return transitionEngine.renderer.renderTransitionFrame(frameA, frameB, progress, junction.type)
                }
            }
        }

        return renderSingleClipFrame(timelineClip, timelineTimeMs, canvasWidth, canvasHeight)
    }

    private fun renderSingleClipFrame(
        timelineClip: TimelineClip?,
        timelineTimeMs: Long,
        canvasWidth: Int,
        canvasHeight: Int
    ): Bitmap {
        val targetClip = timelineClip
        if (targetClip == null) {
            val emptyBmp = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(emptyBmp)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            val shader = LinearGradient(0f, 0f, canvasWidth.toFloat(), canvasHeight.toFloat(), Color.parseColor("#0F172A"), Color.parseColor("#1E1B4B"), Shader.TileMode.CLAMP)
            paint.shader = shader
            canvas.drawRect(0f, 0f, canvasWidth.toFloat(), canvasHeight.toFloat(), paint)
            paint.shader = null
            paint.color = Color.parseColor("#10B981")
            paint.textSize = 36f
            paint.typeface = Typeface.DEFAULT_BOLD
            canvas.drawText("Studio Preview Canvas", 60f, canvasHeight / 2f - 20f, paint)
            paint.color = Color.WHITE
            paint.textSize = 28f
            canvas.drawText("Ready for Media Import", 60f, canvasHeight / 2f + 30f, paint)
            return emptyBmp
        }

        // Calculate clip local source frame time
        val offsetOnTimeline = timelineTimeMs - timelineClip.startTimelineMs
        val sourceTimeMs = timelineClip.inPointMs + (offsetOnTimeline * timelineClip.speed).toLong()

        // 1. Raw Frame Source Extraction
        val rawFrame = mediaLoader.extractFrameAtTime(timelineClip.mediaItem, sourceTimeMs)

        // 2. Transform Application (Static + Keyframe Interpolated)
        val keyframeEngine = KeyframeEngine.getInstance(context)
        val track = keyframeEngine.getTrack(timelineClip.id)
        val relativeMs = (timelineTimeMs - timelineClip.startTimelineMs).coerceAtLeast(0L)
        val kfTransform = keyframeEngine.animationRenderer.calculateInterpolatedTransform(track, relativeMs, timelineClip.durationOnTimelineMs)

        val combinedTransform = ClipTransform(
            scaleX = timelineClip.transform.scaleX * kfTransform.scaleX,
            scaleY = timelineClip.transform.scaleY * kfTransform.scaleY,
            rotation = timelineClip.transform.rotation + kfTransform.rotation,
            translateX = timelineClip.transform.translateX + kfTransform.translateX,
            translateY = timelineClip.transform.translateY + kfTransform.translateY,
            opacity = (timelineClip.transform.opacity * kfTransform.opacity).coerceIn(0f, 1f)
        )

        val transformedBitmap = applyTransform(rawFrame, combinedTransform, canvasWidth, canvasHeight)

        // 3. Filter Engine Pipeline Integration
        val filteredBitmap = if (timelineClip.filterStack.isNotEmpty()) {
            filterRenderer.processFilterStack(transformedBitmap, timelineClip.filterStack)
        } else {
            transformedBitmap
        }

        // 3b. GPU Effects Engine Pipeline Integration
        val effectsEngine = EffectsEngine.getInstance(context)
        val activeEffects = effectsEngine.stackState.value.effects
        val processedBitmap = if (activeEffects.isNotEmpty()) {
            effectsEngine.previewRenderer.renderPreviewFrame(filteredBitmap, activeEffects, timelineTimeMs)
        } else {
            filteredBitmap
        }

        // 3c. Master AI Video Processing Engine Pipeline Integration
        val aiEngine = AIEngine.getInstance(context)
        val aiProcessedBitmap = aiEngine.processFrameWithAI(processedBitmap, timelineClip, timelineTimeMs, canvasWidth, canvasHeight)

        // 4. Text Overlay Rendering
        if (timelineClip.textOverlays.isNotEmpty()) {
            val canvas = Canvas(aiProcessedBitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            for (text in timelineClip.textOverlays) {
                paint.color = Color.parseColor(text.colorHex)
                paint.textSize = text.fontSizeSp * 2f
                paint.typeface = Typeface.DEFAULT_BOLD
                val px = text.positionX * canvasWidth
                val py = text.positionY * canvasHeight
                canvas.drawText(text.text, px, py, paint)
            }
        }

        return aiProcessedBitmap
    }

    private fun applyTransform(src: Bitmap, transform: ClipTransform, targetW: Int, targetH: Int): Bitmap {
        val output = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            alpha = (transform.opacity * 255).toInt().coerceIn(0, 255)
        }

        val matrix = Matrix()
        val scale = min(targetW.toFloat() / src.width, targetH.toFloat() / src.height)
        matrix.postScale(scale * transform.scaleX, scale * transform.scaleY)
        matrix.postRotate(transform.rotation, targetW / 2f, targetH / 2f)
        matrix.postTranslate(transform.translateX, transform.translateY)

        canvas.drawBitmap(src, matrix, paint)
        return output
    }
}

class AudioEngine {
    fun mixAudioForClip(clip: TimelineClip, masterVolume: Float = 1.0f): Float {
        if (clip.isMuted) return 0.0f
        return (clip.volume * masterVolume).coerceIn(0.0f, 2.0f)
    }
}

// ============================================================================
// 5. EXPORT ENGINE (Delegated to Master ExportEngine.kt)
// ============================================================================

// ============================================================================
// 6. PROJECT ENGINE (Save & Load Persistent Projects)
// ============================================================================
class ProjectEngine(private val context: Context) {
    private val projectsDir = File(context.filesDir, "projects").apply { mkdirs() }

    fun saveProject(project: ProjectData) {
        val json = JSONObject().apply {
            put("projectId", project.projectId)
            put("name", project.name)
            put("createdAt", project.createdAt)
            put("canvasWidth", project.canvasWidth)
            put("canvasHeight", project.canvasHeight)

            val clipsArr = JSONArray()
            project.clips.forEach { clip ->
                val clipObj = JSONObject().apply {
                    put("id", clip.id)
                    put("fileUri", clip.mediaItem.fileUri)
                    put("mediaName", clip.mediaItem.name)
                    put("mediaType", clip.mediaItem.type.name)
                    put("mediaDuration", clip.mediaItem.durationMs)
                    put("startTimelineMs", clip.startTimelineMs)
                    put("inPointMs", clip.inPointMs)
                    put("outPointMs", clip.outPointMs)
                    put("speed", clip.speed.toDouble())
                    put("volume", clip.volume.toDouble())
                    put("isMuted", clip.isMuted)
                }
                clipsArr.put(clipObj)
            }
            put("clips", clipsArr)
        }

        val file = File(projectsDir, "${project.projectId}.json")
        file.writeText(json.toString())
    }

    fun loadProject(projectId: String, mediaLoader: MediaLoader): ProjectData? {
        val file = File(projectsDir, "$projectId.json")
        if (!file.exists()) return null

        return try {
            val json = JSONObject(file.readText())
            val clipsArr = json.getJSONArray("clips")
            val clips = mutableListOf<TimelineClip>()

            for (i in 0 until clipsArr.length()) {
                val obj = clipsArr.getJSONObject(i)
                val mediaItem = MediaItem(
                    fileUri = obj.getString("fileUri"),
                    name = obj.getString("mediaName"),
                    type = MediaType.valueOf(obj.getString("mediaType")),
                    durationMs = obj.getLong("mediaDuration"),
                    width = 1080,
                    height = 1920
                )

                val clip = TimelineClip(
                    id = obj.getString("id"),
                    mediaItem = mediaItem,
                    startTimelineMs = obj.getLong("startTimelineMs"),
                    inPointMs = obj.getLong("inPointMs"),
                    outPointMs = obj.getLong("outPointMs"),
                    speed = obj.getDouble("speed").toFloat(),
                    volume = obj.getDouble("volume").toFloat(),
                    isMuted = obj.getBoolean("isMuted")
                )
                clips.add(clip)
            }

            ProjectData(
                projectId = json.getString("projectId"),
                name = json.getString("name"),
                createdAt = json.getLong("createdAt"),
                clips = clips,
                canvasWidth = json.optInt("canvasWidth", 1080),
                canvasHeight = json.optInt("canvasHeight", 1920)
            )
        } catch (e: Exception) {
            null
        }
    }
}

// ============================================================================
// 7. MASTER VIDEO ENGINE (Singleton Orchestrator)
// ============================================================================
class RealVideoEngine private constructor(private val context: Context) {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    val mediaLoader = MediaLoader(context)
    val timelineEngine = TimelineEngine()
    val playbackEngine = PlaybackEngine(scope, timelineEngine)
    val filterRenderer = FilterRenderer(context)
    val renderEngine = RenderEngine(context, mediaLoader, filterRenderer)
    val audioEngine = AudioEngine()
    val exportEngine = ExportEngine.getInstance(context)
    val projectEngine = ProjectEngine(context)

    private val _diagnostics = MutableStateFlow(VideoDiagnostics())
    val diagnostics: StateFlow<VideoDiagnostics> = _diagnostics.asStateFlow()

    fun toggleDebugMode(enabled: Boolean) {
        _diagnostics.value = _diagnostics.value.copy(isDebugEnabled = enabled)
    }

    suspend fun importMediaFile(uriString: String, name: String): TimelineClip {
        val item = mediaLoader.loadMediaAsynchronously(uriString, name)
        return timelineEngine.addClip(item)
    }

    companion object {
        @Volatile private var instance: RealVideoEngine? = null
        fun getInstance(context: Context): RealVideoEngine {
            return instance ?: synchronized(this) {
                instance ?: RealVideoEngine(context.applicationContext).also { instance = it }
            }
        }
    }
}
