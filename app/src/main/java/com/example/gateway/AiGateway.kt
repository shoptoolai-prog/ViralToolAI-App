package com.example.gateway

import com.example.ai.AiProviderManager
import com.example.core.UniversalAiProvider
import com.example.core.UniversalAiRequest
import com.example.core.UniversalAiResponse
import com.example.core.UniversalAiStreamChunk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * PHASE 12D — Central AI Gateway
 * The single, unified entry point for all AI operations in ViralToolAI.
 *
 * Rules:
 * 1. UI components NEVER directly call an AI provider.
 * 2. All requests pass through the 7-Step Request Pipeline.
 * 3. Enforces response validation, safe fallbacks, developer logging, and session tracking.
 * 4. Supports future expansion (Gemini, OpenAI, Claude, Local AI, ML Kit, Custom APIs) without UI changes.
 */

object AiGateway {

    /**
     * Executes AI request synchronously through full 7-step pipeline on Dispatchers.IO.
     */
    suspend fun processRequest(request: UniversalAiRequest): UniversalAiResponse = withContext(Dispatchers.IO) {
        val targetProvider = request.preferredProvider ?: UniversalAiProvider.GEMINI
        val modelConfig = GatewayProviderManager.getConfiguration(targetProvider)

        val sessionRecord = AiSessionManager.startSession(
            requestId = request.id,
            module = resolveModuleFromCategory(request.promptCategory),
            providerUsed = targetProvider,
            modelName = modelConfig.modelName
        )

        try {
            val pipelineResult = GatewayRequestPipeline.executePipeline(request) { provider, config ->
                // Delegate to existing underlying UniversalAiProviderManager engine
                AiProviderManager.processRequest(
                    request.copy(preferredProvider = provider)
                )
            }

            val finalResp = pipelineResult.finalResponse

            AiSessionManager.completeSession(
                sessionRecord = sessionRecord,
                isSuccess = finalResp.isSuccess,
                confidenceScore = pipelineResult.validationResult.confidence,
                failureReason = finalResp.errorMessage,
                isOfflineFallback = finalResp.providerUsed == UniversalAiProvider.LOCAL_AI
            )

            finalResp
        } catch (e: Exception) {
            val safeFail = AiSafeFallback.createSafeFailureResponse(
                requestId = request.id,
                providerUsed = targetProvider,
                customMessage = AiSafeFallback.FALLBACK_ERROR_MESSAGE
            )

            AiSessionManager.completeSession(
                sessionRecord = sessionRecord,
                isSuccess = false,
                confidenceScore = 0.0,
                failureReason = e.message ?: AiSafeFallback.FALLBACK_ERROR_MESSAGE
            )

            safeFail
        }
    }

    /**
     * Streams AI request responses on Dispatchers.IO.
     */
    fun streamRequest(request: UniversalAiRequest): Flow<UniversalAiStreamChunk> {
        return AiProviderManager.streamRequest(request)
    }

    private fun resolveModuleFromCategory(category: com.example.core.PromptCategory): AiPromptModule {
        return when (category) {
            com.example.core.PromptCategory.SHOPPING,
            com.example.core.PromptCategory.PRODUCT_DETECTION,
            com.example.core.PromptCategory.MERCHANT_ANALYSIS -> AiPromptModule.SHOPPING_AI

            com.example.core.PromptCategory.CREATOR,
            com.example.core.PromptCategory.CAPTION,
            com.example.core.PromptCategory.HASHTAGS,
            com.example.core.PromptCategory.PROFILE_REVIEW -> AiPromptModule.CREATOR_AI
        }
    }
}
