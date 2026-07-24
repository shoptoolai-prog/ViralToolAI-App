package com.example.ocr

/**
 * SHOPTOOLAI Phase 8C — Verified Data Model
 * Every extracted field contains:
 * 1. Value (or null if unreadable)
 * 2. Confidence (0 - 100%)
 * 3. Source (e.g. "Google ML Kit OCR", "Gemini Vision OCR")
 * 4. Verification Status
 * 
 * Rules:
 * - Only extracted text with confidence >= 60% is considered verified.
 * - If unreadable or low confidence, value displays "Unable to verify".
 * - Never guess or invent fake data.
 */
data class VerifiedField<T>(
    val rawValue: T?,
    val confidence: Int, // 0 - 100
    val source: String,
    val isVerified: Boolean = rawValue != null && confidence >= 60
) {
    /**
     * Returns formatted string or "Unable to verify" if unverified.
     */
    fun displayValue(fallback: String = "Unable to verify"): String {
        return if (isVerified && rawValue != null) {
            rawValue.toString()
        } else {
            fallback
        }
    }

    companion object {
        fun <T> unverified(source: String = "Universal OCR Engine"): VerifiedField<T> {
            return VerifiedField(
                rawValue = null,
                confidence = 0,
                source = source,
                isVerified = false
            )
        }

        fun <T> verified(value: T, confidence: Int = 95, source: String = "Universal OCR Engine"): VerifiedField<T> {
            return VerifiedField(
                rawValue = value,
                confidence = confidence.coerceIn(0, 100),
                source = source,
                isVerified = confidence >= 60
            )
        }
    }
}

/**
 * Structured Creator Screenshot OCR Data Model
 */
data class StructuredCreatorOcrData(
    val username: VerifiedField<String>,
    val displayName: VerifiedField<String>,
    val bio: VerifiedField<String>,
    val followers: VerifiedField<Long>,
    val following: VerifiedField<Long>,
    val posts: VerifiedField<Int>,
    val category: VerifiedField<String>,
    val actionButtons: List<VerifiedField<String>>,
    val rawLines: List<String>
)

/**
 * Structured Shopping Screenshot OCR Data Model
 */
data class StructuredShoppingOcrData(
    val productTitle: VerifiedField<String>,
    val brand: VerifiedField<String>,
    val price: VerifiedField<Double>,
    val discountPercent: VerifiedField<Int>,
    val rating: VerifiedField<Double>,
    val reviewCount: VerifiedField<Int>,
    val merchant: VerifiedField<String>,
    val visibleTextBlocks: List<String>
)
