package com.example.gateway

import com.example.core.UniversalAiProvider

/**
 * PHASE 12D — AI Model Configuration
 * Configurable per-provider & per-module AI parameters.
 */
data class AiModelConfiguration(
    val provider: UniversalAiProvider = UniversalAiProvider.GEMINI,
    val modelName: String = "gemini-1.5-flash",
    val temperature: Double = 0.7,
    val timeoutMs: Long = 15000L,
    val maxTokens: Int = 2048,
    val apiVersion: String = "v1beta",
    val topP: Double = 0.95,
    val topK: Int = 40,
    val isLocalFallbackEnabled: Boolean = true
) {
    companion object {
        fun defaultForProvider(provider: UniversalAiProvider): AiModelConfiguration {
            return when (provider) {
                UniversalAiProvider.GEMINI -> AiModelConfiguration(
                    provider = UniversalAiProvider.GEMINI,
                    modelName = "gemini-1.5-flash",
                    apiVersion = "v1beta"
                )
                UniversalAiProvider.OPENAI -> AiModelConfiguration(
                    provider = UniversalAiProvider.OPENAI,
                    modelName = "gpt-4o",
                    apiVersion = "v1"
                )
                UniversalAiProvider.CLAUDE -> AiModelConfiguration(
                    provider = UniversalAiProvider.CLAUDE,
                    modelName = "claude-3-5-sonnet",
                    apiVersion = "v1"
                )
                UniversalAiProvider.GROQ -> AiModelConfiguration(
                    provider = UniversalAiProvider.GROQ,
                    modelName = "llama-3.1-70b-versatile",
                    apiVersion = "v1"
                )
                UniversalAiProvider.LOCAL_AI -> AiModelConfiguration(
                    provider = UniversalAiProvider.LOCAL_AI,
                    modelName = "shoptool-on-device-v2",
                    timeoutMs = 5000L,
                    isLocalFallbackEnabled = true
                )
            }
        }
    }
}
