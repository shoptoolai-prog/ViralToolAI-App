package com.example.modules

/**
 * SHOPTOOLAI Phase 5A — Modular AI Feature Architecture
 * Independent modules that can be enabled, configured, or updated without affecting core engine stability.
 */
enum class AiModuleType {
    SHOPPING_INTELLIGENCE,
    CREATOR_PROFILE_AI,
    INSTAGRAM_SHOPPING_AI,
    FUTURE_CHAT_SHOPPING_AI
}

data class AiModuleConfig(
    val type: AiModuleType,
    val isEnabled: Boolean = true,
    val label: String,
    val description: String,
    val isBeta: Boolean = false
)

object FeatureModuleManager {
    private val modulesMap = mutableMapOf<AiModuleType, AiModuleConfig>(
        AiModuleType.SHOPPING_INTELLIGENCE to AiModuleConfig(
            type = AiModuleType.SHOPPING_INTELLIGENCE,
            isEnabled = true,
            label = "Shopping Intelligence Core",
            description = "Universal link analysis, product extraction, and price comparison."
        ),
        AiModuleType.CREATOR_PROFILE_AI to AiModuleConfig(
            type = AiModuleType.CREATOR_PROFILE_AI,
            isEnabled = true,
            label = "Creator Profile AI",
            description = "AI audit for Instagram profiles, bio optimization, and growth roadmap.",
            isBeta = true
        ),
        AiModuleType.INSTAGRAM_SHOPPING_AI to AiModuleConfig(
            type = AiModuleType.INSTAGRAM_SHOPPING_AI,
            isEnabled = true,
            label = "Instagram Shopping AI",
            description = "Reel scan & wearable fashion product detection."
        ),
        AiModuleType.FUTURE_CHAT_SHOPPING_AI to AiModuleConfig(
            type = AiModuleType.FUTURE_CHAT_SHOPPING_AI,
            isEnabled = false,
            label = "Chat Shopping AI",
            description = "Conversational shopping assistant for natural language product search.",
            isBeta = true
        )
    )

    fun isModuleEnabled(type: AiModuleType): Boolean {
        return modulesMap[type]?.isEnabled ?: false
    }

    fun setModuleEnabled(type: AiModuleType, enabled: Boolean) {
        modulesMap[type]?.let { current ->
            modulesMap[type] = current.copy(isEnabled = enabled)
        }
    }

    fun getModuleConfig(type: AiModuleType): AiModuleConfig? {
        return modulesMap[type]
    }

    fun getAllModules(): List<AiModuleConfig> {
        return modulesMap.values.toList()
    }
}
