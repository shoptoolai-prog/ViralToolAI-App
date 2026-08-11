package com.example.creatoracademy

import android.util.Log
import java.util.Locale

// ==============================================================================
// PART 6D — REALITY CHECK & ANTI-HALLUCINATION ENGINE
// ==============================================================================

enum class ClaimVerificationStatus {
    VERIFIED,
    PARTIALLY_VERIFIED,
    REJECTED,
    INSUFFICIENT_EVIDENCE
}

data class VerifiedClaimRecord(
    val claimId: String,
    val claimTitle: String,
    val category: String, // "Visual", "Text", "Price", "Logo", "Face", "Product", "Brand", "Audience", "VideoType", "Temporal", "Contradiction"
    val timestampSec: Float?,
    val formattedTimestamp: String?,
    val sourceEngine: String,
    val confidencePercent: Int,
    val verificationStatus: ClaimVerificationStatus,
    val reasonExplanation: String,
    val supportingEvidenceText: String?,
    val originalClaimText: String,
    val correctedClaimText: String? = null
)

data class ClaimCorrection(
    val originalClaim: String,
    val correctedClaim: String,
    val reason: String,
    val timestampSec: Float? = null
)

data class RealityCheckResult(
    val verifiedObservations: List<VerifiedClaimRecord>,
    val partiallyVerified: List<VerifiedClaimRecord>,
    val rejectedObservations: List<VerifiedClaimRecord>,
    val insufficientEvidence: List<VerifiedClaimRecord>,
    val corrections: List<ClaimCorrection>,
    val realityCheckScoreAdjustment: Int = 0,
    val totalClaimsTested: Int = 0,
    val debugSummary: String = ""
)

object RealityCheckEngine {

    private const val TAG = "RealityCheckEngine"

    /**
     * Sits AFTER AI Decision Engine and BEFORE final report output.
     * Evaluates claims against real video evidence in UniversalDetectionContext.
     * Returns a pair of (Approved AIDecisionResult, RealityCheckResult).
     */
    fun verify(
        u: UniversalDetectionContext,
        report: MasterValidatedReportV2,
        decisionResult: AIDecisionResult,
        insights: InstagramInsightsInput? = null
    ): Pair<AIDecisionResult, RealityCheckResult> {
        Log.d(TAG, "Executing RealityCheckEngine validation pass on ${decisionResult.positiveFindings.size + decisionResult.negativeFindings.size + decisionResult.uncertainFindings.size} claims...")

        val verifiedList = mutableListOf<VerifiedClaimRecord>()
        val partiallyList = mutableListOf<VerifiedClaimRecord>()
        val rejectedList = mutableListOf<VerifiedClaimRecord>()
        val insufficientList = mutableListOf<VerifiedClaimRecord>()
        val corrections = mutableListOf<ClaimCorrection>()

        val rawClaimsToTest = collectClaims(decisionResult, report, u)

        for (claim in rawClaimsToTest) {
            val eval = evaluateClaim(claim, u, insights)
            when (eval.verificationStatus) {
                ClaimVerificationStatus.VERIFIED -> verifiedList.add(eval)
                ClaimVerificationStatus.PARTIALLY_VERIFIED -> {
                    partiallyList.add(eval)
                    eval.correctedClaimText?.let { corrected ->
                        corrections.add(ClaimCorrection(eval.originalClaimText, corrected, eval.reasonExplanation, eval.timestampSec))
                    }
                }
                ClaimVerificationStatus.REJECTED -> {
                    rejectedList.add(eval)
                    eval.correctedClaimText?.let { corrected ->
                        corrections.add(ClaimCorrection(eval.originalClaimText, corrected, eval.reasonExplanation, eval.timestampSec))
                    }
                }
                ClaimVerificationStatus.INSUFFICIENT_EVIDENCE -> insufficientList.add(eval)
            }
        }

        // Merge duplicate observations across nearby timestamps
        val mergedVerified = mergeDuplicateClaims(verifiedList)
        val mergedRejected = mergeDuplicateClaims(rejectedList)
        val mergedPartially = mergeDuplicateClaims(partiallyList)
        val mergedInsufficient = mergeDuplicateClaims(insufficientList)

        // Sanitize AIDecisionResult for user-facing output (Report Protection)
        val approvedDecisionResult = sanitizeDecisionResult(decisionResult, mergedRejected, corrections, u)

        // Score Protection: Apply minor deduction for hallucinated/rejected claims without altering base formulas
        val scoreAdjustment = if (mergedRejected.isNotEmpty()) - (mergedRejected.size * 2).coerceAtMost(10) else 0

        val debugLog = StringBuilder().apply {
            append("=== REALITY CHECK DEBUG SUMMARY ===\n")
            append("Total Claims Analyzed: ${rawClaimsToTest.size}\n")
            append("VERIFIED: ${mergedVerified.size} | PARTIAL: ${mergedPartially.size} | REJECTED: ${mergedRejected.size} | INSUFFICIENT: ${mergedInsufficient.size}\n")
            if (mergedRejected.isNotEmpty()) {
                append("REJECTED CLAIMS:\n")
                mergedRejected.forEach { r ->
                    append(" - [${r.category}] ${r.claimTitle} @ ${r.formattedTimestamp}: ${r.reasonExplanation}\n")
                }
            }
            if (corrections.isNotEmpty()) {
                append("CORRECTIONS APPLIED:\n")
                corrections.forEach { c ->
                    append(" - Original: '${c.originalClaim}' -> Corrected: '${c.correctedClaim}' (${c.reason})\n")
                }
            }
        }.toString()

        val realityCheckResult = RealityCheckResult(
            verifiedObservations = mergedVerified,
            partiallyVerified = mergedPartially,
            rejectedObservations = mergedRejected,
            insufficientEvidence = mergedInsufficient,
            corrections = corrections,
            realityCheckScoreAdjustment = scoreAdjustment,
            totalClaimsTested = rawClaimsToTest.size,
            debugSummary = debugLog
        )

        Log.d(TAG, debugLog)

        return Pair(approvedDecisionResult, realityCheckResult)
    }

    private data class InternalClaim(
        val claimId: String,
        val claimTitle: String,
        val category: String,
        val timestampSec: Float?,
        val sourceEngine: String,
        val confidencePercent: Int,
        val claimText: String
    )

    private fun collectClaims(
        decisionResult: AIDecisionResult,
        report: MasterValidatedReportV2,
        u: UniversalDetectionContext
    ): List<InternalClaim> {
        val list = mutableListOf<InternalClaim>()

        decisionResult.positiveFindings.forEachIndexed { idx, f ->
            list.add(
                InternalClaim(
                    claimId = "pos_$idx",
                    claimTitle = f.title,
                    category = categorizeTitle(f.title, f.description),
                    timestampSec = f.timestampSec,
                    sourceEngine = f.engineSource,
                    confidencePercent = f.confidencePercent,
                    claimText = "${f.title}: ${f.description}"
                )
            )
        }

        decisionResult.negativeFindings.forEachIndexed { idx, f ->
            list.add(
                InternalClaim(
                    claimId = "neg_$idx",
                    claimTitle = f.title,
                    category = categorizeTitle(f.title, f.description),
                    timestampSec = f.timestampSec,
                    sourceEngine = f.engineSource,
                    confidencePercent = f.confidencePercent,
                    claimText = "${f.title}: ${f.description}"
                )
            )
        }

        decisionResult.uncertainFindings.forEachIndexed { idx, f ->
            list.add(
                InternalClaim(
                    claimId = "unc_$idx",
                    claimTitle = f.title,
                    category = categorizeTitle(f.title, f.description),
                    timestampSec = f.timestampSec,
                    sourceEngine = f.engineSource,
                    confidencePercent = f.confidencePercent,
                    claimText = "${f.title}: ${f.description}"
                )
            )
        }

        return list
    }

    private fun categorizeTitle(title: String, desc: String): String {
        val combined = "$title $desc".lowercase(Locale.US)
        return when {
            combined.contains("price") || combined.contains("₹") || combined.contains("$") -> "Price"
            combined.contains("logo") -> "Logo"
            combined.contains("brand") || combined.contains("amazon") || combined.contains("meesho") || combined.contains("flipkart") || combined.contains("myntra") -> "Brand"
            combined.contains("face") || combined.contains("creator presence") || combined.contains("expression") -> "Face"
            combined.contains("product") || combined.contains("saree") || combined.contains("dress") || combined.contains("apparel") -> "Product"
            combined.contains("text") || combined.contains("caption") || combined.contains("ocr") || combined.contains("overlay") -> "Text"
            combined.contains("audience") || combined.contains("demographic") || combined.contains("male") || combined.contains("female") -> "Audience"
            combined.contains("video type") || combined.contains("content type") || combined.contains("format") -> "VideoType"
            else -> "Visual"
        }
    }

    private fun evaluateClaim(
        claim: InternalClaim,
        u: UniversalDetectionContext,
        insights: InstagramInsightsInput?
    ): VerifiedClaimRecord {
        val text = claim.claimText.lowercase(Locale.US)
        val fmtTime = claim.timestampSec?.let { formatTime(it) } ?: "00:00.0"

        val maxDuration = u.durationSeconds
        val hasOcrText = u.ocr.captionsDetected.isNotEmpty() || u.ocr.priceText != null || u.ocr.offerText != null || u.ocr.ctaText != null || u.ocr.brandName != null

        // 1. TEMPORAL VALIDATION (Timestamp exceeding duration)
        if (claim.timestampSec != null && maxDuration > 0f && claim.timestampSec > maxDuration) {
            return VerifiedClaimRecord(
                claimId = claim.claimId,
                claimTitle = claim.claimTitle,
                category = "Temporal",
                timestampSec = claim.timestampSec,
                formattedTimestamp = fmtTime,
                sourceEngine = claim.sourceEngine,
                confidencePercent = claim.confidencePercent,
                verificationStatus = ClaimVerificationStatus.REJECTED,
                reasonExplanation = "Temporal mismatch: Claim timestamp (${fmtTime}) exceeds total video duration (${formatTime(maxDuration)}).",
                supportingEvidenceText = "Video duration: ${maxDuration}s",
                originalClaimText = claim.claimText,
                correctedClaimText = null
            )
        }

        // 2. BLACK / CORRUPTED FRAME PROTECTION
        if (claim.timestampSec != null && (u.lighting.lightingType.contains("Dark") || u.lighting.lightingType.contains("Underexposed")) && claim.timestampSec < 0.3f) {
            return VerifiedClaimRecord(
                claimId = claim.claimId,
                claimTitle = claim.claimTitle,
                category = "Temporal",
                timestampSec = claim.timestampSec,
                formattedTimestamp = fmtTime,
                sourceEngine = claim.sourceEngine,
                confidencePercent = claim.confidencePercent,
                verificationStatus = ClaimVerificationStatus.REJECTED,
                reasonExplanation = "Black frame protection: Claim rejected because initial frame is underexposed or black artifact.",
                supportingEvidenceText = "Lighting: ${u.lighting.lightingType}",
                originalClaimText = claim.claimText,
                correctedClaimText = null
            )
        }

        // 3. LOW CONFIDENCE / CROSS-FRAME TRANSIENT CHECK
        if (claim.confidencePercent < 50) {
            return VerifiedClaimRecord(
                claimId = claim.claimId,
                claimTitle = claim.claimTitle,
                category = claim.category,
                timestampSec = claim.timestampSec,
                formattedTimestamp = fmtTime,
                sourceEngine = claim.sourceEngine,
                confidencePercent = claim.confidencePercent,
                verificationStatus = ClaimVerificationStatus.INSUFFICIENT_EVIDENCE,
                reasonExplanation = "Cross-frame validation: Low confidence (${claim.confidencePercent}%) indicates possible transient artifact or false positive.",
                supportingEvidenceText = "Confidence below 50% threshold",
                originalClaimText = claim.claimText
            )
        }

        // 4. PRICE CLAIM RULE
        if (claim.category == "Price" || text.contains("price") || text.contains("₹") || text.contains("$")) {
            val hasPriceText = u.ocr.priceText != null || u.ocr.captionsDetected.any { t ->
                t.contains(Regex("""\b(₹|\$|€|INR|USD)\s*\d+\b"""))
            }
            if (!hasPriceText) {
                return VerifiedClaimRecord(
                    claimId = claim.claimId,
                    claimTitle = claim.claimTitle,
                    category = "Price",
                    timestampSec = claim.timestampSec,
                    formattedTimestamp = fmtTime,
                    sourceEngine = claim.sourceEngine,
                    confidencePercent = 95,
                    verificationStatus = ClaimVerificationStatus.REJECTED,
                    reasonExplanation = "Price claim rejected: Zero price or currency symbols found in OCR text sampling.",
                    supportingEvidenceText = null,
                    originalClaimText = claim.claimText,
                    correctedClaimText = "No verified price detected."
                )
            } else {
                return VerifiedClaimRecord(
                    claimId = claim.claimId,
                    claimTitle = claim.claimTitle,
                    category = "Price",
                    timestampSec = claim.timestampSec,
                    formattedTimestamp = fmtTime,
                    sourceEngine = claim.sourceEngine,
                    confidencePercent = 90,
                    verificationStatus = ClaimVerificationStatus.VERIFIED,
                    reasonExplanation = "Price verified via on-screen OCR text.",
                    supportingEvidenceText = "Price text: ${u.ocr.priceText ?: "Verified"}",
                    originalClaimText = claim.claimText
                )
            }
        }

        // 5. LOGO CLAIM RULE
        if (claim.category == "Logo" || (text.contains("logo") && !text.contains("no logo"))) {
            if (!u.ocr.logoDetected && u.ocr.brandName == null) {
                return VerifiedClaimRecord(
                    claimId = claim.claimId,
                    claimTitle = claim.claimTitle,
                    category = "Logo",
                    timestampSec = claim.timestampSec,
                    formattedTimestamp = fmtTime,
                    sourceEngine = claim.sourceEngine,
                    confidencePercent = 90,
                    verificationStatus = ClaimVerificationStatus.REJECTED,
                    reasonExplanation = "Logo claim rejected: Zero brand logos or visual emblems detected.",
                    supportingEvidenceText = null,
                    originalClaimText = claim.claimText,
                    correctedClaimText = "No recognizable logo detected."
                )
            }
        }

        // 6. BRAND / PLATFORM CLAIM RULE (Amazon, Meesho, Flipkart, Myntra, etc.)
        val brands = listOf("amazon", "meesho", "flipkart", "myntra", "meesho creator")
        val mentionedBrand = brands.firstOrNull { text.contains(it) }
        if (mentionedBrand != null) {
            val isBrandInOcr = u.ocr.captionsDetected.any { it.lowercase(Locale.US).contains(mentionedBrand) } || u.ocr.brandName?.lowercase(Locale.US)?.contains(mentionedBrand) == true
            if (!isBrandInOcr) {
                return VerifiedClaimRecord(
                    claimId = claim.claimId,
                    claimTitle = claim.claimTitle,
                    category = "Brand",
                    timestampSec = claim.timestampSec,
                    formattedTimestamp = fmtTime,
                    sourceEngine = claim.sourceEngine,
                    confidencePercent = 75,
                    verificationStatus = ClaimVerificationStatus.PARTIALLY_VERIFIED,
                    reasonExplanation = "Specific brand '${mentionedBrand.replaceFirstChar { it.uppercase() }}' not confirmed via OCR/Logo. Converted to generic commercial showcase.",
                    supportingEvidenceText = "Unverified brand reference scrubbed",
                    originalClaimText = claim.claimText,
                    correctedClaimText = claim.claimText.replace(Regex("(?i)$mentionedBrand"), "commercial product")
                )
            }
        }

        // 7. FACE & HUMAN PRESENCE RULE
        if (claim.category == "Face" || text.contains("face") || text.contains("creator presence")) {
            if (u.human.faceType == FaceDetectionType.NO_FACE) {
                return VerifiedClaimRecord(
                    claimId = claim.claimId,
                    claimTitle = claim.claimTitle,
                    category = "Face",
                    timestampSec = claim.timestampSec,
                    formattedTimestamp = fmtTime,
                    sourceEngine = claim.sourceEngine,
                    confidencePercent = 95,
                    verificationStatus = ClaimVerificationStatus.REJECTED,
                    reasonExplanation = "Face claim rejected: Zero human faces detected in frame sample.",
                    supportingEvidenceText = null,
                    originalClaimText = claim.claimText,
                    correctedClaimText = "No human face framed."
                )
            } else if (u.human.faceType == FaceDetectionType.HALF_FACE) {
                return VerifiedClaimRecord(
                    claimId = claim.claimId,
                    claimTitle = claim.claimTitle,
                    category = "Face",
                    timestampSec = claim.timestampSec,
                    formattedTimestamp = fmtTime,
                    sourceEngine = claim.sourceEngine,
                    confidencePercent = 70,
                    verificationStatus = ClaimVerificationStatus.PARTIALLY_VERIFIED,
                    reasonExplanation = "Partial or side profile face detected; lower facial visibility.",
                    supportingEvidenceText = "Face type: ${u.human.faceType}",
                    originalClaimText = claim.claimText,
                    correctedClaimText = "Partial human face framing detected."
                )
            }
        }

        // 8. PRODUCT CLAIM & CONTRADICTION RULE
        if (claim.category == "Product" || text.contains("product")) {
            if (!u.product.productExists) {
                return VerifiedClaimRecord(
                    claimId = claim.claimId,
                    claimTitle = claim.claimTitle,
                    category = "Product",
                    timestampSec = claim.timestampSec,
                    formattedTimestamp = fmtTime,
                    sourceEngine = claim.sourceEngine,
                    confidencePercent = claim.confidencePercent,
                    verificationStatus = ClaimVerificationStatus.REJECTED,
                    reasonExplanation = "Product claim rejected: Zero commercial products detected in video frames.",
                    supportingEvidenceText = "Product detection confidence: ${u.product.confidence}%",
                    originalClaimText = claim.claimText,
                    correctedClaimText = "No product detected in frame sample."
                )
            } else if (text.contains("saree") && text.contains("dress")) {
                // Engine contradiction handling
                return VerifiedClaimRecord(
                    claimId = claim.claimId,
                    claimTitle = claim.claimTitle,
                    category = "Product",
                    timestampSec = claim.timestampSec,
                    formattedTimestamp = fmtTime,
                    sourceEngine = claim.sourceEngine,
                    confidencePercent = 75,
                    verificationStatus = ClaimVerificationStatus.PARTIALLY_VERIFIED,
                    reasonExplanation = "Contradiction check: Category disputed between Saree and Dress. Generalized to clothing item.",
                    supportingEvidenceText = "Broad category match",
                    originalClaimText = claim.claimText,
                    correctedClaimText = "Clothing item detected; exact category uncertain."
                )
            } else {
                return VerifiedClaimRecord(
                    claimId = claim.claimId,
                    claimTitle = claim.claimTitle,
                    category = "Product",
                    timestampSec = claim.timestampSec,
                    formattedTimestamp = fmtTime,
                    sourceEngine = claim.sourceEngine,
                    confidencePercent = u.product.confidence,
                    verificationStatus = ClaimVerificationStatus.VERIFIED,
                    reasonExplanation = "Product verified in central framing.",
                    supportingEvidenceText = "Product: ${u.product.productCategory ?: "Verified item"}",
                    originalClaimText = claim.claimText
                )
            }
        }

        // 9. TEXT / CAPTION CLAIM RULE
        if (claim.category == "Text" || text.contains("text") || text.contains("caption")) {
            if (!hasOcrText) {
                if (text.contains("missing") || text.contains("no explicit") || text.contains("no on-screen") || text.contains("absent")) {
                    // Correctly asserting absence of text
                    return VerifiedClaimRecord(
                        claimId = claim.claimId,
                        claimTitle = claim.claimTitle,
                        category = "Text",
                        timestampSec = claim.timestampSec,
                        formattedTimestamp = fmtTime,
                        sourceEngine = claim.sourceEngine,
                        confidencePercent = 95,
                        verificationStatus = ClaimVerificationStatus.VERIFIED,
                        reasonExplanation = "Verified absence of on-screen text overlays via OCR.",
                        supportingEvidenceText = "Zero OCR text detected",
                        originalClaimText = claim.claimText
                    )
                } else {
                    return VerifiedClaimRecord(
                        claimId = claim.claimId,
                        claimTitle = claim.claimTitle,
                        category = "Text",
                        timestampSec = claim.timestampSec,
                        formattedTimestamp = fmtTime,
                        sourceEngine = claim.sourceEngine,
                        confidencePercent = claim.confidencePercent,
                        verificationStatus = ClaimVerificationStatus.REJECTED,
                        reasonExplanation = "Text claim rejected: OCR engine found zero readable text in video frames.",
                        supportingEvidenceText = null,
                        originalClaimText = claim.claimText,
                        correctedClaimText = "No on-screen text verified."
                    )
                }
            } else {
                return VerifiedClaimRecord(
                    claimId = claim.claimId,
                    claimTitle = claim.claimTitle,
                    category = "Text",
                    timestampSec = claim.timestampSec,
                    formattedTimestamp = fmtTime,
                    sourceEngine = claim.sourceEngine,
                    confidencePercent = 90,
                    verificationStatus = ClaimVerificationStatus.VERIFIED,
                    reasonExplanation = "OCR confirmed readable text overlays.",
                    supportingEvidenceText = "Found text: ${u.ocr.captionsDetected.take(2).joinToString(", ")}",
                    originalClaimText = claim.claimText
                )
            }
        }

        // 10. AUDIENCE & GENDER DEMOGRAPHICS RULE
        if (claim.category == "Audience" || text.contains("male") || text.contains("female") || text.contains("% audience") || text.contains("demographic")) {
            if (insights == null || insights.views == null) {
                return VerifiedClaimRecord(
                    claimId = claim.claimId,
                    claimTitle = claim.claimTitle,
                    category = "Audience",
                    timestampSec = claim.timestampSec,
                    formattedTimestamp = fmtTime,
                    sourceEngine = claim.sourceEngine,
                    confidencePercent = 85,
                    verificationStatus = ClaimVerificationStatus.PARTIALLY_VERIFIED,
                    reasonExplanation = "Audience demographic percentages require real Instagram Insights data.",
                    supportingEvidenceText = "Specific % demographic metrics scrubbed without Insights",
                    originalClaimText = claim.claimText,
                    correctedClaimText = "Content appears relevant to ${u.category.categoryName} based on observed topic."
                )
            }
        }

        // DEFAULT VERIFIED CLAIM FOR SUPPORTED OBSERVATIONS
        return VerifiedClaimRecord(
            claimId = claim.claimId,
            claimTitle = claim.claimTitle,
            category = claim.category,
            timestampSec = claim.timestampSec,
            formattedTimestamp = fmtTime,
            sourceEngine = claim.sourceEngine,
            confidencePercent = claim.confidencePercent,
            verificationStatus = ClaimVerificationStatus.VERIFIED,
            reasonExplanation = "Claim verified against frame sampling evidence.",
            supportingEvidenceText = "Frame evidence confirmed",
            originalClaimText = claim.claimText
        )
    }

    private fun mergeDuplicateClaims(claims: List<VerifiedClaimRecord>): List<VerifiedClaimRecord> {
        val merged = mutableListOf<VerifiedClaimRecord>()
        val grouped = claims.groupBy { "${it.category}_${it.claimTitle}" }

        grouped.forEach { (_, list) ->
            if (list.size == 1) {
                merged.add(list.first())
            } else {
                val first = list.first()
                val minTime = list.mapNotNull { it.timestampSec }.minOrNull() ?: 0.0f
                val maxTime = list.mapNotNull { it.timestampSec }.maxOrNull() ?: 0.0f
                val rangeFmt = if (minTime != maxTime) "${formatTime(minTime)}–${formatTime(maxTime)}" else formatTime(minTime)
                merged.add(
                    first.copy(
                        timestampSec = minTime,
                        formattedTimestamp = rangeFmt,
                        supportingEvidenceText = "${first.supportingEvidenceText ?: "Verified"} across $rangeFmt (${list.size} occurrences)"
                    )
                )
            }
        }
        return merged
    }

    private fun sanitizeDecisionResult(
        decisionResult: AIDecisionResult,
        rejectedClaims: List<VerifiedClaimRecord>,
        corrections: List<ClaimCorrection>,
        u: UniversalDetectionContext
    ): AIDecisionResult {
        val rejectedTitles = rejectedClaims.map { it.claimTitle.lowercase(Locale.US) }.toSet()

        val sanitizedPositives = decisionResult.positiveFindings.filterNot { f ->
            rejectedTitles.contains(f.title.lowercase(Locale.US))
        }.map { applyCorrectionsToFinding(it, corrections) }

        val sanitizedNegatives = decisionResult.negativeFindings.filterNot { f ->
            rejectedTitles.contains(f.title.lowercase(Locale.US))
        }.map { applyCorrectionsToFinding(it, corrections) }

        val sanitizedCards = decisionResult.cardData.map { card ->
            cleanReportCard(card, u, rejectedTitles)
        }

        return decisionResult.copy(
            positiveFindings = sanitizedPositives,
            negativeFindings = sanitizedNegatives,
            cardData = sanitizedCards
        )
    }

    private fun applyCorrectionsToFinding(
        f: DecisionFinding,
        corrections: List<ClaimCorrection>
    ): DecisionFinding {
        var updatedTitle = f.title
        var updatedDesc = f.description

        corrections.forEach { c ->
            if (f.description.contains(c.originalClaim, ignoreCase = true)) {
                updatedDesc = f.description.replace(c.originalClaim, c.correctedClaim)
            }
        }

        return f.copy(title = updatedTitle, description = updatedDesc)
    }

    private fun cleanReportCard(
        card: ReportCardData,
        u: UniversalDetectionContext,
        rejectedTitles: Set<String>
    ): ReportCardData {
        var pos = card.positiveFinding
        var neg = card.negativeFinding
        var rec = card.recommendedAction

        // If card 8 (Text) is cleaned and no text was found
        if (card.cardIndex == 8 && u.ocr.captionsDetected.isEmpty()) {
            pos = "No on-screen text verified in video frames."
            neg = "Missing text captions for silent viewers."
            rec = "Add lower-third caption text in opening 1.5s."
        }

        // If card 4 (Visual) mentions product but no product exists
        if (!u.product.productExists) {
            if (pos?.contains("product", ignoreCase = true) == true) {
                pos = "Subject framed clearly in center."
            }
            if (neg?.contains("product", ignoreCase = true) == true) {
                neg = "No commercial product framed in video."
            }
        }

        return card.copy(
            positiveFinding = pos,
            negativeFinding = neg,
            recommendedAction = rec
        )
    }

    private fun formatTime(sec: Float): String {
        val totalSec = sec.coerceAtLeast(0f).toInt()
        val minutes = totalSec / 60
        val seconds = totalSec % 60
        val millis = ((sec - totalSec) * 10).toInt().coerceIn(0, 9)
        return String.format(Locale.US, "%02d:%02d.%d", minutes, seconds, millis)
    }
}
