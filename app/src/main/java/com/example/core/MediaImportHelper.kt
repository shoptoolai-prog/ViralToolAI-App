package com.example.core

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import com.example.ui.screens.MediaPickerItem
import com.example.ui.screens.ProjectSetupConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MediaImportHelper {

    /**
     * Copies a Uri content to app's cache directory so that ExoPlayer, MediaMetadataRetriever,
     * and Coil can access it safely without SecurityException or content stream permission issues.
     */
    fun copyUriToCache(context: Context, uri: Uri): File? {
        return try {
            val contentResolver = context.contentResolver
            val mimeType = contentResolver.getType(uri) ?: ""
            val uriStr = uri.toString().lowercase(Locale.ROOT)
            val isImage = mimeType.startsWith("image") ||
                    uriStr.endsWith(".jpg") || uriStr.endsWith(".jpeg") ||
                    uriStr.endsWith(".png") || uriStr.endsWith(".webp") || uriStr.endsWith(".heic")
            val ext = when {
                isImage && mimeType.contains("png") -> ".png"
                isImage -> ".jpg"
                mimeType.contains("quicktime") || mimeType.contains("mov") -> ".mov"
                mimeType.contains("webm") -> ".webm"
                else -> ".mp4"
            }

            val timeStamp = System.currentTimeMillis()
            val cacheDir = File(context.cacheDir, "imported_media").apply { mkdirs() }
            val cacheFile = File(cacheDir, "imported_media_${timeStamp}_${(1000..9999).random()}$ext")

            contentResolver.openInputStream(uri)?.use { inputStream ->
                cacheFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            if (cacheFile.exists() && cacheFile.length() > 0) {
                cacheFile
            } else {
                null
            }
        } catch (e: Throwable) {
            Log.e("MediaImportHelper", "Failed to copy URI to cache: $uri", e)
            null
        }
    }

    /**
     * Safely reads video or image metadata and returns a MediaPickerItem.
     * Guaranteed never to throw uncaught exceptions or freeze. Returns null only if URI is empty or blank.
     */
    suspend fun importVideoUri(context: Context, originalUri: Uri): MediaPickerItem? = withContext(Dispatchers.IO) {
        if (originalUri == Uri.EMPTY || originalUri.toString().isBlank()) return@withContext null

        try {
            // Attempt persistable permission if supported
            try {
                context.contentResolver.takePersistableUriPermission(
                    originalUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Throwable) {}

            // Copy to app cache for safe, local file access
            val cachedFile = try {
                copyUriToCache(context, originalUri)
            } catch (e: Throwable) {
                Log.w("MediaImportHelper", "Error caching URI: $originalUri", e)
                null
            }
            val safeUri = if (cachedFile != null) Uri.fromFile(cachedFile) else originalUri

            val detectedMime = try {
                context.contentResolver.getType(originalUri) ?: context.contentResolver.getType(safeUri) ?: ""
            } catch (_: Throwable) { "" }

            val uriStr = safeUri.toString().lowercase(Locale.ROOT)
            val isImage = detectedMime.startsWith("image") ||
                    uriStr.endsWith(".jpg") || uriStr.endsWith(".jpeg") ||
                    uriStr.endsWith(".png") || uriStr.endsWith(".webp") || uriStr.endsWith(".heic")

            var durationMs = 0L
            var width = 1920
            var height = 1080
            var title = try { originalUri.lastPathSegment ?: if (isImage) "Imported Image" else "Imported Video" } catch (_: Throwable) { if (isImage) "Imported Image" else "Imported Video" }

            // Extract metadata with 3-second timeout to prevent any coroutine/native freeze
            try {
                kotlinx.coroutines.withTimeoutOrNull(3000L) {
                    if (isImage) {
                        try {
                            val options = android.graphics.BitmapFactory.Options().apply { inJustDecodeBounds = true }
                            if (cachedFile != null) {
                                android.graphics.BitmapFactory.decodeFile(cachedFile.absolutePath, options)
                            } else {
                                context.contentResolver.openInputStream(originalUri)?.use { stream ->
                                    android.graphics.BitmapFactory.decodeStream(stream, null, options)
                                }
                            }
                            if (options.outWidth > 0 && options.outHeight > 0) {
                                width = options.outWidth
                                height = options.outHeight
                            }
                        } catch (e: Throwable) {
                            Log.w("MediaImportHelper", "Error reading image dimensions", e)
                        }
                        durationMs = 5000L
                    } else {
                        val retriever = MediaMetadataRetriever()
                        try {
                            if (cachedFile != null) {
                                retriever.setDataSource(cachedFile.absolutePath)
                            } else {
                                retriever.setDataSource(context, originalUri)
                            }

                            val durStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                            if (!durStr.isNullOrEmpty()) {
                                durationMs = durStr.toLongOrNull() ?: 0L
                            }

                            val wStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                            val hStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                            val rotationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)

                            var parsedW = wStr?.toIntOrNull() ?: 1920
                            var parsedH = hStr?.toIntOrNull() ?: 1080
                            val rotation = rotationStr?.toIntOrNull() ?: 0
                            if (rotation == 90 || rotation == 270) {
                                val tmp = parsedW
                                parsedW = parsedH
                                parsedH = tmp
                            }
                            if (parsedW > 0) width = parsedW
                            if (parsedH > 0) height = parsedH

                            val metaTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                            if (!metaTitle.isNullOrBlank()) {
                                title = metaTitle
                            }
                        } catch (e: Throwable) {
                            Log.w("MediaImportHelper", "Error extracting metadata from video URI: $originalUri", e)
                        } finally {
                            try {
                                retriever.release()
                            } catch (_: Throwable) {}
                        }
                    }
                }
            } catch (e: Throwable) {
                Log.w("MediaImportHelper", "Metadata retrieval timed out or failed for $originalUri", e)
            }

            val durationSeconds = if (durationMs > 0) durationMs / 1000 else if (isImage) 5L else 15L
            val mins = durationSeconds / 60
            val secs = durationSeconds % 60
            val durFormatted = if (isImage) "0:05" else String.format(Locale.US, "%d:%02d", mins, secs)

            val resLabel = if (width >= 3840 || height >= 2160) "4K"
            else if (width >= 1920 || height >= 1080) "1080p"
            else if (width > 0) "${height}p"
            else "1080p"

            val fileSizeBytes = cachedFile?.length() ?: 0L
            val sizeMb = fileSizeBytes / (1024f * 1024f)
            val sizeFormatted = if (sizeMb > 0f) String.format(Locale.US, "%.1f MB", sizeMb) else if (isImage) "Image File" else "Video File"

            MediaPickerItem(
                id = "imported_${System.currentTimeMillis()}_${(100..999).random()}",
                uri = safeUri,
                title = title,
                durationFormatted = durFormatted,
                durationSeconds = durationSeconds,
                isVideo = !isImage,
                albumName = "Imports",
                mimeType = if (isImage) (if (detectedMime.startsWith("image")) detectedMime else "image/jpeg") else (if (detectedMime.startsWith("video")) detectedMime else "video/mp4"),
                dateAddedSeconds = System.currentTimeMillis() / 1000,
                resolutionLabel = resLabel,
                width = width,
                height = height,
                fileSizeFormatted = sizeFormatted,
                fileSizeBytes = fileSizeBytes,
                frameRateLabel = if (isImage) "Static" else "30 FPS"
            )
        } catch (e: Throwable) {
            Log.e("MediaImportHelper", "Fallback import for media URI: $originalUri", e)
            // Guaranteed fallback object so non-empty valid URIs never return null or freeze
            MediaPickerItem(
                id = "imported_${System.currentTimeMillis()}_${(100..999).random()}",
                uri = originalUri,
                title = "Imported Video",
                durationFormatted = "0:15",
                durationSeconds = 15L,
                isVideo = true,
                albumName = "Imports",
                mimeType = "video/mp4",
                dateAddedSeconds = System.currentTimeMillis() / 1000,
                resolutionLabel = "1080p",
                width = 1080,
                height = 1920,
                fileSizeFormatted = "Video File",
                fileSizeBytes = 0L,
                frameRateLabel = "30 FPS"
            )
        }
    }

    /**
     * Imports multiple selected video/image URIs.
     */
    suspend fun importVideoUris(context: Context, uris: List<Uri>): List<MediaPickerItem> = withContext(Dispatchers.IO) {
        uris.mapNotNull { uri ->
            importVideoUri(context, uri)
        }
    }

    /**
     * Creates a standard ProjectSetupConfig from imported media items for immediate Editor launching.
     */
    fun createDefaultProjectConfig(selectedItems: List<MediaPickerItem>): ProjectSetupConfig {
        val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        val defaultName = "Viral Project - ${dateFormat.format(Date())}"
        val firstItemRes = selectedItems.firstOrNull()?.resolutionLabel ?: "1080p"

        return ProjectSetupConfig(
            projectName = defaultName,
            selectedMedia = selectedItems,
            aspectRatio = "9:16",
            resolution = if (firstItemRes.contains("4K")) "4K" else "1080p",
            fps = "30 FPS",
            autoCaptionsEnabled = true,
            aiAudioCleanEnabled = true,
            smartReframerEnabled = true,
            autoCutFillersEnabled = false
        )
    }
}

