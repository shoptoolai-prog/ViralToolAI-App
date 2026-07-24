package com.example.core.engines

import com.example.core.interfaces.IOcrExtractor
import com.example.core.log.AiInternalLogger
import com.example.core.log.LogCategory
import com.example.core.model.AiResponseStatus
import com.example.core.model.StandardAiResponse
import com.example.ocr.OcrProviderType
import com.example.ocr.UniversalOcrEngine

/**
 * SHOPTOOLAI Phase 12A — Real OCR Intelligence Engine Module
 */
object AiOcrEngine : IOcrExtractor {

    override suspend fun extractTextFromImage(imagePathOrUri: String): StandardAiResponse<String> {
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "Starting OCR Pipeline", imagePathOrUri)

        if (imagePathOrUri.isBlank()) {
            AiInternalLogger.log(LogCategory.OCR_FAILURE, "Empty image URI or path provided")
            return StandardAiResponse(
                status = AiResponseStatus.FAILURE,
                confidence = 0.0,
                errorMessage = "Couldn't confidently identify text: empty image path",
                warnings = listOf("No valid image file supplied for OCR"),
                sourceType = "OCR_ENGINE"
            )
        }

        return try {
            val ocrResult = UniversalOcrEngine.processOcrPipeline(
                imageUriOrPath = imagePathOrUri,
                preferredProvider = OcrProviderType.AUTO_SMART
            )

            if (!ocrResult.isSuccess || ocrResult.rawExtractedLines.isEmpty()) {
                AiInternalLogger.log(
                    LogCategory.OCR_FAILURE,
                    "OCR Engine produced no lines",
                    "Path: $imagePathOrUri",
                    confidence = 0.1
                )
                return StandardAiResponse(
                    status = AiResponseStatus.PARTIAL,
                    confidence = 0.15,
                    detectedData = "",
                    missingFields = listOf("rawText", "extractedLines"),
                    warnings = listOf("Couldn't confidently identify text in image."),
                    sourceType = "MLKIT_OCR"
                )
            }

            val extractedText = ocrResult.rawExtractedLines.joinToString("\n")
            val confidence = if (ocrResult.isSuccess && ocrResult.rawExtractedLines.isNotEmpty()) {
                (0.5 + (ocrResult.rawExtractedLines.size * 0.05)).coerceAtMost(0.95)
            } else {
                0.15
            }

            AiInternalLogger.log(
                LogCategory.PIPELINE_EVENT,
                "OCR Extracted ${ocrResult.rawExtractedLines.size} lines",
                confidence = confidence
            )

            StandardAiResponse(
                status = AiResponseStatus.SUCCESS,
                confidence = confidence,
                detectedData = extractedText,
                detectedFields = listOf("rawText", "lines", "blockCount"),
                sourceType = "MLKIT_OCR"
            )
        } catch (e: Exception) {
            AiInternalLogger.log(
                LogCategory.CRASH_POINT,
                "OCR Pipeline exception: ${e.message}",
                details = e.stackTraceToString()
            )
            StandardAiResponse(
                status = AiResponseStatus.FAILURE,
                confidence = 0.0,
                errorMessage = e.message ?: "OCR pipeline failure",
                warnings = listOf("Couldn't confidently identify text due to processing error."),
                sourceType = "OCR_ENGINE"
            )
        }
    }
}
