package com.example.gateway

import com.example.core.UniversalAiProvider
import com.example.core.UniversalAiRequest
import com.example.core.UniversalAiResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import java.util.concurrent.ConcurrentHashMap

/**
 * PHASE 12D — AI Provider Manager
 * Manages provider health, fallback chains, retries, timeouts, and version compatibility.
 */

object GatewayProviderManager {

    private val providerConfigs = ConcurrentHashMap<UniversalAiProvider, AiModelConfiguration>()
    private val providerHealthStatus = ConcurrentHashMap<UniversalAiProvider, Boolean>()

    init {
        // Register default configurations
        UniversalAiProvider.values().forEach { provider ->
            providerConfigs[provider] = AiModelConfiguration.defaultForProvider(provider)
            providerHealthStatus[provider] = true
        }
    }

    fun getConfiguration(provider: UniversalAiProvider): AiModelConfiguration {
        return providerConfigs[provider] ?: AiModelConfiguration.defaultForProvider(provider)
    }

    fun updateConfiguration(config: AiModelConfiguration) {
        providerConfigs[config.provider] = config
    }

    fun isProviderHealthy(provider: UniversalAiProvider): Boolean {
        return providerHealthStatus[provider] ?: true
    }

    fun setProviderHealth(provider: UniversalAiProvider, isHealthy: Boolean) {
        providerHealthStatus[provider] = isHealthy
    }

    /**
     * Resolves ordered fallback chain for a requested provider.
     */
    fun resolveFallbackChain(preferred: UniversalAiProvider): List<UniversalAiProvider> {
        val chain = mutableListOf<UniversalAiProvider>()
        chain.add(preferred)

        if (preferred != UniversalAiProvider.GEMINI) chain.add(UniversalAiProvider.GEMINI)
        if (preferred != UniversalAiProvider.OPENAI) chain.add(UniversalAiProvider.OPENAI)
        if (preferred != UniversalAiProvider.LOCAL_AI) chain.add(UniversalAiProvider.LOCAL_AI)

        return chain.filter { isProviderHealthy(it) || it == UniversalAiProvider.LOCAL_AI }
    }

    /**
     * Executes AI provider call with timeout & retry mechanics.
     */
    suspend fun executeWithRetry(
        request: UniversalAiRequest,
        provider: UniversalAiProvider,
        block: suspend (UniversalAiProvider, AiModelConfiguration) -> UniversalAiResponse
    ): UniversalAiResponse {
        val config = getConfiguration(provider)
        var attempts = 0
        val maxAttempts = 2
        var lastError: String? = null

        while (attempts < maxAttempts) {
            attempts++
            try {
                if (AiRateLimitManager.isRateLimited(provider.name)) {
                    AiRateLimitManager.applyBackoffDelay(attempts)
                }

                val result = withTimeout(config.timeoutMs) {
                    block(provider, config)
                }

                if (result.isSuccess) {
                    AiRateLimitManager.registerRequest(provider.name)
                    return result
                } else {
                    lastError = result.errorMessage
                }
            } catch (e: Exception) {
                lastError = e.message ?: "Execution timeout on ${provider.displayName}"
                setProviderHealth(provider, false)
            }
            AiRateLimitManager.applyBackoffDelay(attempts)
        }

        return AiSafeFallback.createSafeFailureResponse(
            requestId = request.id,
            providerUsed = provider,
            customMessage = lastError ?: "Execution failed on provider ${provider.displayName}"
        )
    }
}
