package com.example.creatorassistant.engine

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.creatorassistant.domain.TargetRatio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ExportManager(private val context: Context) {

    suspend fun saveVideoToDevice(
        sourceUri: Uri,
        targetRatio: TargetRatio
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            val fileName = "viraltoolai_ai_${targetRatio.tag}_${System.currentTimeMillis()}.mp4"
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Video.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                    put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/ViralToolAI")
                }

                val resolver = context.contentResolver
                val destinationUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
                    ?: return@withContext Result.failure(Exception("Could not create MediaStore entry"))

                resolver.openOutputStream(destinationUri)?.use { outStream ->
                    resolver.openInputStream(sourceUri)?.use { inStream ->
                        inStream.copyTo(outStream)
                    }
                }

                Result.success(destinationUri)
            } else {
                val dir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
                    "ViralToolAI"
                )
                if (!dir.exists()) dir.mkdirs()

                val outFile = File(dir, fileName)
                context.contentResolver.openInputStream(sourceUri)?.use { inStream ->
                    FileOutputStream(outFile).use { outStream ->
                        inStream.copyTo(outStream)
                    }
                }
                Result.success(Uri.fromFile(outFile))
            }
        } catch (e: Exception) {
            Log.e("ExportManager", "Failed to save video: ${e.message}", e)
            Result.failure(e)
        }
    }
}
