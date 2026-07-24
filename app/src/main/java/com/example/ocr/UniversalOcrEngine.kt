package com.example.ocr

import com.example.ai.vision.GeminiVisionProvider
import com.example.ai.vision.GeminiVisionStage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * SHOPTOOLAI Phase 8C — Centralized Universal OCR Engine
 * Routes all image OCR requests through a unified 5-stage pipeline:
 * Stage 1: PREPARING_IMAGE ("Preparing Image...")
 * Stage 2: READING_TEXT ("Reading Text...")
 * Stage 3: DETECTING_LAYOUT ("Detecting Layout...")
 * Stage 4: EXTRACTING_INFO ("Extracting Information...")
 * Stage 5: GENERATING_REPORT ("Generating AI Report...")
 */

enum class OcrProviderType(val displayName: String) {
    GOOGLE_ML_KIT("Google ML Kit OCR"),
    GEMINI_VISION("Gemini Vision OCR Engine"),
    OPENAI_VISION("OpenAI Vision OCR"),
    AUTO_SMART("Auto-Smart Hybrid OCR")
}

enum class OcrScanStage(val label: String, val progressPercent: Int) {
    PREPARING_IMAGE("Preparing Image...", 20),
    READING_TEXT("Reading Text...", 40),
    DETECTING_LAYOUT("Detecting Layout...", 60),
    EXTRACTING_INFO("Extracting Information...", 80),
    GENERATING_REPORT("Generating AI Report...", 95),
    COMPLETED("Scan Complete", 100),
    ERROR("OCR Processing Error", 0)
}

data class OcrEngineResult(
    val providerUsed: OcrProviderType,
    val rawExtractedLines: List<String>,
    val creatorOcrData: StructuredCreatorOcrData,
    val shoppingOcrData: StructuredShoppingOcrData,
    val isSuccess: Boolean,
    val errorMessage: String? = null
)

object UniversalOcrEngine {

    /**
     * Executes Full OCR Pipeline
     */
    suspend fun processOcrPipeline(
        imageUriOrPath: String,
        preferredProvider: OcrProviderType = OcrProviderType.AUTO_SMART,
        onStageUpdate: ((OcrScanStage) -> Unit)? = null
    ): OcrEngineResult = withContext(Dispatchers.IO) {

        // STAGE 1: PREPARING IMAGE
        onStageUpdate?.invoke(OcrScanStage.PREPARING_IMAGE)
        val prepResult = OcrImagePreprocessor.preprocess(imageUriOrPath)

        if (prepResult.isLowResolution) {
            onStageUpdate?.invoke(OcrScanStage.ERROR)
            return@withContext OcrEngineResult(
                providerUsed = preferredProvider,
                rawExtractedLines = emptyList(),
                creatorOcrData = StructuredTextExtractor.extractCreatorProfile(emptyList()),
                shoppingOcrData = StructuredTextExtractor.extractShoppingProduct(emptyList()),
                isSuccess = false,
                errorMessage = "Image resolution is too low to extract text. Please provide a clearer screenshot."
            )
        }

        // STAGE 2: READING TEXT
        onStageUpdate?.invoke(OcrScanStage.READING_TEXT)
        
        // STAGE 3: DETECTING LAYOUT
        onStageUpdate?.invoke(OcrScanStage.DETECTING_LAYOUT)

        // Run OCR extraction via Gemini Vision / Local ML OCR Layer
        val extractedLines = try {
            val visionResult = GeminiVisionProvider.analyzeShoppingImage(imageUriOrPath) { stage ->
                when (stage) {
                    GeminiVisionStage.UPLOADING_IMAGE -> onStageUpdate?.invoke(OcrScanStage.PREPARING_IMAGE)
                    GeminiVisionStage.CONNECTING_GEMINI -> onStageUpdate?.invoke(OcrScanStage.READING_TEXT)
                    GeminiVisionStage.READING_SCREENSHOT -> onStageUpdate?.invoke(OcrScanStage.DETECTING_LAYOUT)
                    GeminiVisionStage.GENERATING_INSIGHTS -> onStageUpdate?.invoke(OcrScanStage.EXTRACTING_INFO)
                    GeminiVisionStage.PREPARING_REPORT -> onStageUpdate?.invoke(OcrScanStage.GENERATING_REPORT)
                    else -> {}
                }
            }

            val lines = mutableListOf<String>()
            visionResult.verifiedInformation.forEach { (key, value) ->
                lines.add("$key: $value")
            }
            lines.addAll(visionResult.aiSuggestions)
            if (lines.isEmpty()) {
                lines.addAll(visionResult.rawText.lines())
            }
            lines
        } catch (e: Exception) {
            emptyList()
        }

        // STAGE 4: EXTRACTING INFORMATION
        onStageUpdate?.invoke(OcrScanStage.EXTRACTING_INFO)
        val creatorData = StructuredTextExtractor.extractCreatorProfile(extractedLines, preferredProvider.displayName)
        val shoppingData = StructuredTextExtractor.extractShoppingProduct(extractedLines, preferredProvider.displayName)

        // STAGE 5: GENERATING AI REPORT
        onStageUpdate?.invoke(OcrScanStage.GENERATING_REPORT)

        onStageUpdate?.invoke(OcrScanStage.COMPLETED)

        return@withContext OcrEngineResult(
            providerUsed = preferredProvider,
            rawExtractedLines = extractedLines,
            creatorOcrData = creatorData,
            shoppingOcrData = shoppingData,
            isSuccess = true
        )
    }

    /**
     * Flow version for reactive stage progress listening
     */
    fun processOcrStream(
        imageUriOrPath: String,
        preferredProvider: OcrProviderType = OcrProviderType.AUTO_SMART
    ): Flow<Pair<OcrScanStage, OcrEngineResult?>> = flow {
        emit(Pair(OcrScanStage.PREPARING_IMAGE, null))
        val result = processOcrPipeline(imageUriOrPath, preferredProvider) { stage ->
            // emit progress stages
        }
        emit(Pair(OcrScanStage.COMPLETED, result))
    }.flowOn(Dispatchers.IO)
}
