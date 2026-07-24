package com.example.gateway

import com.example.reports.ReportLanguage
import java.util.Locale

/**
 * PHASE 12D — Prompt Engine
 * Centralized prompt manager separating prompts strictly by module.
 * No hardcoded prompt strings in UI components.
 */

enum class AiPromptModule {
    SHOPPING_AI,
    CREATOR_AI,
    OCR_AI,
    VISION_AI,
    REPORTS_AI,
    RECOMMENDATIONS_AI
}

object PromptEngine {

    /**
     * Builds modular prompt string for given module and inputs.
     */
    fun buildPrompt(
        module: AiPromptModule,
        inputPayload: String,
        language: ReportLanguage = ReportLanguage.HINGLISH,
        extraParams: Map<String, String> = emptyMap()
    ): String {
        val langName = language.displayName
        return when (module) {
            AiPromptModule.SHOPPING_AI -> """
                [SYSTEM: ViralToolAI Shopping Intelligence Engine v12.4]
                Target Module: SHOPPING_AI
                Language Mode: $langName
                
                Input Payload: $inputPayload
                Merchant Hint: ${extraParams["merchant"] ?: "Auto-Detect"}
                Category Hint: ${extraParams["category"] ?: "General Shopping"}
                
                Task:
                1. Extract Product Title, Current Price, Original Price, Discount %, Merchant Store, and Rating.
                2. Verify merchant trust score and delivery reliability.
                3. Generate 3 actionable shopping savings advice bullet points.
                4. Output strictly structured json or verified key-value format.
            """.trimIndent()

            AiPromptModule.CREATOR_AI -> """
                [SYSTEM: ViralToolAI Creator Intelligence Engine v12.4]
                Target Module: CREATOR_AI
                Language Mode: $langName
                
                Input Profile / Payload: $inputPayload
                Platform Hint: ${extraParams["platform"] ?: "Instagram"}
                
                Task:
                1. Extract Username, Display Name, Bio, Followers, Following, and Posts count.
                2. Evaluate Content Niche, Visual Quality, Branding Consistency, and Profile Optimization.
                3. Provide 3 high-impact profile growth and monetization suggestions.
                4. Only use visible screenshot/URL data. Never invent figures.
            """.trimIndent()

            AiPromptModule.OCR_AI -> """
                [SYSTEM: ViralToolAI OCR Extraction Engine v12.4]
                Target Module: OCR_AI
                
                Input Image / Text Lines: $inputPayload
                
                Task:
                1. Prioritize large, bold, header, and button text labels.
                2. Extract prices (₹/Rs/$), titles, ratings, badges, and user statistics.
                3. Return extracted key-value structure with confidence scores per field.
            """.trimIndent()

            AiPromptModule.VISION_AI -> """
                [SYSTEM: ViralToolAI Vision Intelligence Router v12.4]
                Target Module: VISION_AI
                
                Image URI / Path: $inputPayload
                
                Task:
                1. Classify image into Shopping Screenshot, Creator Profile, Reel, or Unreadable.
                2. Extract visible entities without mixing shopping and creator metadata.
                3. Flag low-quality or blurry screenshots immediately.
            """.trimIndent()

            AiPromptModule.REPORTS_AI -> """
                [SYSTEM: ViralToolAI Report Generation Engine v12.4]
                Target Module: REPORTS_AI
                Language Mode: $langName
                
                Product/Creator Data: $inputPayload
                
                Task:
                1. Generate concise, scannable intelligence report.
                2. Highlight top deal strengths, potential risks, and cross-store savings options.
            """.trimIndent()

            AiPromptModule.RECOMMENDATIONS_AI -> """
                [SYSTEM: ViralToolAI Recommendation Engine v12.4]
                
                Context Payload: $inputPayload
                
                Task:
                Provide top 3 personalized smart recommendations for maximum value.
            """.trimIndent()
        }
    }
}
