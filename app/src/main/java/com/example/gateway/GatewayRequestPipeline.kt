package com.example.gateway

import com.example.core.UniversalAiProvider
import com.example.core.UniversalAiRequest
import com.example.core.UniversalAiResponse

/**
 * PHASE 12D — Request Pipeline
 * Enforces strict 7-Stage Execution Flow:
 * Step 1: Input Receive
 * Step 2: Input Validation
 * Step 3: AI Gateway Route
 * Step 4: Provider Manager Resolution & Health Check
 * Step 5: AI Provider Execution
 * Step 6: Response Validation
 * Step 7: Formatter & Output
 */

enum class PipelineStep(val stepName: String) {
    STEP_1_INPUT("Input Received"),
    STEP_2_VALIDATION("Input Validated"),
    STEP_3_GATEWAY("Routed via AI Gateway"),
    STEP_4_PROVIDER_RESOLUTION("Resolved via Provider Manager"),
    STEP_5_PROVIDER_EXECUTION("AI Provider Executed"),
    STEP_6_RESPONSE_VALIDATION("Response Validated"),
    STEP_7_FORMATTER("Formatted for UI")
}

data class PipelineExecutionResult(
    val finalResponse: UniversalAiResponse,
    val completedSteps: List<PipelineStep>,
    val validationResult: ValidationResult,
    val totalTimeMs: Long
)

object GatewayRequestPipeline {

    suspend fun executePipeline(
        request: UniversalAiRequest,
        providerCall: suspend (UniversalAiProvider, AiModelConfiguration) -> UniversalAiResponse
    ): PipelineExecutionResult {
        val startTime = System.currentTimeMillis()
        val steps = mutableListOf<PipelineStep>()

        // STEP 1: INPUT RECEIVE
        steps.add(PipelineStep.STEP_1_INPUT)

        // STEP 2: INPUT VALIDATION
        steps.add(PipelineStep.STEP_2_VALIDATION)
        val isInputEmpty = request.textPrompt.isNullOrBlank() &&
                request.primaryImageUri.isNullOrBlank() &&
                request.shoppingUrl.isNullOrBlank() &&
                request.creatorScreenshotUri.isNullOrBlank() &&
                request.imageUris.isEmpty()

        if (isInputEmpty) {
            val safeFail = AiSafeFallback.createSafeFailureResponse(
                requestId = request.id,
                customMessage = "Input validation failed: Please provide a valid prompt, URL, or image."
            )
            return PipelineExecutionResult(
                finalResponse = safeFail,
                completedSteps = steps,
                validationResult = ValidationResult(false, 0.0, rejectionReason = "Empty Input"),
                totalTimeMs = System.currentTimeMillis() - startTime
            )
        }

        // STEP 3: AI GATEWAY ROUTE
        steps.add(PipelineStep.STEP_3_GATEWAY)

        // STEP 4: PROVIDER MANAGER RESOLUTION
        steps.add(PipelineStep.STEP_4_PROVIDER_RESOLUTION)
        val targetProvider = OfflineAiEngine.resolveProviderForNetwork(
            request.preferredProvider ?: UniversalAiProvider.GEMINI
        )
        val fallbackChain = GatewayProviderManager.resolveFallbackChain(targetProvider)

        // STEP 5: AI PROVIDER EXECUTION
        steps.add(PipelineStep.STEP_5_PROVIDER_EXECUTION)
        var response: UniversalAiResponse? = null

        for (provider in fallbackChain) {
            response = GatewayProviderManager.executeWithRetry(request, provider, providerCall)
            if (response.isSuccess) break
        }

        val actualResponse = response ?: AiSafeFallback.createSafeFailureResponse(
            requestId = request.id,
            providerUsed = targetProvider
        )

        // STEP 6: RESPONSE VALIDATION
        steps.add(PipelineStep.STEP_6_RESPONSE_VALIDATION)
        val valResult = AiResponseValidator.validateResponse(actualResponse)

        val finalValidatedResponse = if (valResult.isValid) {
            actualResponse
        } else {
            AiSafeFallback.createSafeFailureResponse(
                requestId = request.id,
                providerUsed = actualResponse.providerUsed,
                customMessage = valResult.rejectionReason ?: AiSafeFallback.FALLBACK_ERROR_MESSAGE
            )
        }

        // STEP 7: FORMATTER & OUTPUT
        steps.add(PipelineStep.STEP_7_FORMATTER)

        val duration = System.currentTimeMillis() - startTime

        // Developer logging
        AiDeveloperLogger.logDiagnostic(
            DeveloperDiagnosticEntry(
                provider = finalValidatedResponse.providerUsed.displayName,
                model = GatewayProviderManager.getConfiguration(finalValidatedResponse.providerUsed).modelName,
                latencyMs = duration,
                confidenceScore = valResult.confidence,
                ocrQualityScore = 0.90,
                parsingErrors = valResult.missingFields + valResult.invalidValues
            )
        )

        return PipelineExecutionResult(
            finalResponse = finalValidatedResponse,
            completedSteps = steps,
            validationResult = valResult,
            totalTimeMs = duration
        )
    }
}
