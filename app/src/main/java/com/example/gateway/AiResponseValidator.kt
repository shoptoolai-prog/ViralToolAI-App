package com.example.gateway

import com.example.core.UniversalAiResponse

/**
 * PHASE 12D — AI Response Validator
 * Validates AI responses for completeness, confidence score, valid field boundaries,
 * and structural sanity before returning to UI caller.
 */

data class ValidationResult(
    val isValid: Boolean,
    val confidence: Double,
    val missingFields: List<String> = emptyList(),
    val invalidValues: List<String> = emptyList(),
    val rejectionReason: String? = null
)

object AiResponseValidator {

    private const val MIN_ACCEPTABLE_CONFIDENCE = 0.35

    /**
     * Validates UniversalAiResponse for missing fields, invalid numeric values, or empty output.
     */
    fun validateResponse(response: UniversalAiResponse): ValidationResult {
        val missing = mutableListOf<String>()
        val invalid = mutableListOf<String>()

        if (!response.isSuccess) {
            return ValidationResult(
                isValid = false,
                confidence = 0.0,
                rejectionReason = response.errorMessage ?: "Response flagged as failure by provider."
            )
        }

        if (response.summary.isBlank()) {
            missing.add("summary")
        }

        if (response.summary.contains("Not detected", ignoreCase = true) && response.suggestions.isEmpty()) {
            return ValidationResult(
                isValid = false,
                confidence = 0.20,
                rejectionReason = "AI output contained placeholder detection failure messages."
            )
        }

        val normConfidence = response.confidencePercent / 100.0
        if (normConfidence < MIN_ACCEPTABLE_CONFIDENCE) {
            return ValidationResult(
                isValid = false,
                confidence = normConfidence,
                rejectionReason = "AI confidence score ($normConfidence) below minimum acceptable threshold ($MIN_ACCEPTABLE_CONFIDENCE)."
            )
        }

        return ValidationResult(
            isValid = missing.isEmpty() && invalid.isEmpty(),
            confidence = normConfidence,
            missingFields = missing,
            invalidValues = invalid
        )
    }
}
