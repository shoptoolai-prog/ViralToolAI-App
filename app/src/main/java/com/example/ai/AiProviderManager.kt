package com.example.ai

import com.example.core.AiExecutionLog
import com.example.core.AiExecutionLogger
import com.example.core.AiSecurityManager
import com.example.core.PromptCategory
import com.example.core.UniversalAiProvider
import com.example.core.UniversalAiProviderManager
import com.example.core.UniversalAiRequest
import com.example.core.UniversalAiResponse
import com.example.core.UniversalAiStreamChunk
import com.example.core.UniversalPromptLibrary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.util.concurrent.atomic.AtomicReference

/**
 * SHOPTOOLAI Phase 8A — Real AI Provider Layer Manager
 * Centralized AI Provider Manager (Gemini, OpenAI, Claude, Groq, Local AI Ready)
 * 
 * Rules:
 * 1. UI components NEVER directly call an AI provider.
 * 2. Every request passes through the 5-stage pipeline: Input Validation -> Prompt Prep -> Send -> Receive & Validate -> Cache & Render.
 * 3. Secure configuration without hardcoded keys.
 * 4. Local AI Response Caching for duplicate suppression.
 * 5. Streaming & Retry pipeline on Dispatchers.IO.
 */

data class AiProviderSettings(
    val activeProvider: UniversalAiProvider = UniversalAiProvider.GEMINI,
    val apiKey: String = "",
    val timeoutMs: Long = 15000L,
    val maxRetries: Int = 3,
    val maxTokens: Int = 2048,
    val temperature: Float = 0.7f,
    val cacheEnabled: Boolean = true
)

object AiProviderManager {

    private val settingsRef = AtomicReference(
        AiProviderSettings(
            apiKey = AiSecurityManager.getActiveApiKey(UniversalAiProvider.GEMINI) ?: ""
        )
    )

    fun getSettings(): AiProviderSettings = settingsRef.get()

    fun updateSettings(newSettings: AiProviderSettings) {
        settingsRef.set(newSettings)
    }

    fun updateActiveProvider(provider: UniversalAiProvider) {
        val current = settingsRef.get()
        val key = current.apiKey.ifBlank { AiSecurityManager.getActiveApiKey(provider) ?: "" }
        settingsRef.set(current.copy(activeProvider = provider, apiKey = key))
    }

    /**
     * CENTRALIZED ENTRY POINT: Executes AI Request through full Pipeline
     */
    suspend fun processRequest(request: UniversalAiRequest): UniversalAiResponse = withContext(Dispatchers.IO) {
        val settings = getSettings()
        val startTime = System.currentTimeMillis()

        // STAGE 1: VALIDATE INPUT
        val isPromptEmpty = request.textPrompt.isNullOfBlank() && 
                            request.primaryImageUri.isNullOfBlank() && 
                            request.shoppingUrl.isNullOfBlank() && 
                            request.creatorScreenshotUri.isNullOfBlank() && 
                            request.imageUris.isEmpty()

        if (isPromptEmpty) {
            val errLog = AiExecutionLog(
                requestId = request.id,
                moduleUsed = request.promptCategory.name,
                providerUsed = request.preferredProvider ?: settings.activeProvider,
                processingTimeMs = System.currentTimeMillis() - startTime,
                isSuccess = false,
                errorReason = "Input prompt or context cannot be empty."
            )
            AiExecutionLogger.logExecution(errLog)

            return@withContext UniversalAiResponse(
                requestId = request.id,
                providerUsed = request.preferredProvider ?: settings.activeProvider,
                summary = "Input validation failed. Please provide a valid prompt, URL, or image.",
                isSuccess = false,
                errorMessage = "Input prompt or context cannot be empty."
            )
        }

        // STAGE 2: CHECK CACHE FOR DUPLICATE / REPEAT REQUESTS
        if (settings.cacheEnabled) {
            val cachedResponse = AiResponseCache.get(request)
            if (cachedResponse != null) {
                return@withContext cachedResponse.copy(
                    summary = cachedResponse.summary + " [Cached]",
                    processingTimeMs = System.currentTimeMillis() - startTime
                )
            }
        }

        // STAGE 3: PREPARE PROMPT
        val preparedPrompt = if (!request.textPrompt.isNullOfBlank()) {
            request.textPrompt!!
        } else {
            val inputStr = request.shoppingUrl ?: request.primaryImageUri ?: "Default Payload"
            UniversalPromptLibrary.getPrompt(request.promptCategory, inputStr, request.language)
        }

        val enrichedRequest = request.copy(
            textPrompt = preparedPrompt,
            preferredProvider = request.preferredProvider ?: settings.activeProvider
        )

        // STAGE 4: SEND TO PROVIDER WITH TIMEOUT & RETRY PIPELINE
        var attempts = 0
        var lastExceptionMessage: String? = null
        var finalResponse: UniversalAiResponse? = null

        while (attempts < settings.maxRetries && finalResponse == null) {
            attempts++
            try {
                withTimeout(settings.timeoutMs) {
                    val resp = UniversalAiProviderManager.processRequest(enrichedRequest)
                    if (resp.isSuccess && !resp.summary.isBlank()) {
                        finalResponse = resp
                    } else {
                        lastExceptionMessage = resp.errorMessage ?: "Provider returned blank or malformed output."
                    }
                }
            } catch (e: Exception) {
                lastExceptionMessage = e.message ?: "AI request execution timeout or network error."
            }
        }

        // STAGE 5: VALIDATE RESPONSE & CACHE / ERROR UI HOOK
        if (finalResponse != null && finalResponse!!.isSuccess) {
            val successResponse = finalResponse!!.copy(
                processingTimeMs = System.currentTimeMillis() - startTime
            )

            // Cache successful result
            if (settings.cacheEnabled) {
                AiResponseCache.put(request, successResponse)
            }

            return@withContext successResponse
        } else {
            // Error handling - return user-friendly error response
            val failureReason = lastExceptionMessage ?: "AI service is temporarily unavailable."
            
            val failedLog = AiExecutionLog(
                requestId = request.id,
                moduleUsed = request.promptCategory.name,
                providerUsed = enrichedRequest.preferredProvider ?: settings.activeProvider,
                processingTimeMs = System.currentTimeMillis() - startTime,
                isSuccess = false,
                errorReason = failureReason
            )
            AiExecutionLogger.logExecution(failedLog)

            return@withContext UniversalAiResponse(
                requestId = request.id,
                providerUsed = enrichedRequest.preferredProvider ?: settings.activeProvider,
                summary = "AI service is temporarily unavailable.",
                isSuccess = false,
                errorMessage = failureReason
            )
        }
    }

    /**
     * STREAMING PIPELINE
     */
    fun streamRequest(request: UniversalAiRequest): Flow<UniversalAiStreamChunk> = flow {
        val settings = getSettings()
        val targetProvider = request.preferredProvider ?: settings.activeProvider
        val enriched = request.copy(preferredProvider = targetProvider)

        UniversalAiProviderManager.streamResponse(enriched).collect { chunk ->
            emit(chunk)
        }
    }.flowOn(Dispatchers.IO)

    private fun String?.isNullOfBlank(): Boolean = this.isNullOrEmpty() || this.isBlank()
}
