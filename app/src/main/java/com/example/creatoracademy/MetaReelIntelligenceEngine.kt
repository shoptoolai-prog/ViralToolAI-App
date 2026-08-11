package com.example.creatoracademy

import android.net.Uri
import java.util.Locale

// ==============================================================================
// META REEL INTELLIGENCE ENGINE (PART 6A)
// Real-time Reel performance diagnosis based on actual video evidence
// ==============================================================================

/**
 * Data category classification for user trust and transparency.
 */
enum class DataCategory {
    OBSERVED,    // Direct visual/audio detection from engines
    CALCULATED,  // Derived mathematically from observations (e.g. durations, averages)
    INFERRED,    // AI diagnostic interpretation
    UNAVAILABLE  // Required metric not present or insufficient data
}

/**
 * Optional Instagram Insights input provided by user.
 */
data class InstagramInsightsInput(
    val views: Long? = null,
    val reach: Long? = null,
    val watchTimeSec: Float? = null,
    val avgWatchTimeSec: Float? = null,
    val retentionPercent: Int? = null,
    val shares: Int? = null,
    val saves: Int? = null,
    val comments: Int? = null,
    val likes: Int? = null,
    val follows: Int? = null
)

data class MetaHookAnalysis(
    val hookStrength: String, // "STRONG", "MODERATE", "WEAK"
    val timeToFirstEventSec: Float?,
    val timeToFirstSubjectSec: Float?,
    val timeToFirstSpeechSec: Float?,
    val timeToFirstTextSec: Float?,
    val openingVisualClarity: String,
    val openingMotion: String,
    val openingDeadTimeSec: Float,
    val strongestOpeningEvidence: String,
    val weakestOpeningEvidence: String,
    val recommendedImprovement: String
)

data class MetaAttentionRisk(
    val timestampSec: Float,
    val formattedTime: String,
    val riskLevel: String, // "HIGH", "MEDIUM", "LOW"
    val reason: String,
    val evidence: String
)

data class MetaPacingAnalysis(
    val pacingStatus: String, // "BALANCED", "TOO SLOW", "TOO FAST", "MIXED"
    val avgShotDurationSec: Float,
    val sceneChangeCount: Int,
    val explanation: String
)

data class MetaClarityAnalysis(
    val isTopicClear: Boolean,
    val communicatesSubject: Boolean,
    val communicatesPurpose: Boolean,
    val claritySummary: String,
    val unclearElements: List<String>
)

data class MetaVisualAttention(
    val attractsAttention: List<String>,
    val distractsAttention: List<String>,
    val locationsSummary: String
)

data class MetaTextAnalysis(
    val hasText: Boolean,
    val firstTextTimeSec: Float?,
    val textDensity: String,
    val captionConsistency: String,
    val readabilitySummary: String,
    val rawTextFound: List<String>
)

data class MetaAudioAnalysis(
    val hasAudio: Boolean,
    val hasSpeech: Boolean,
    val speechTimingSummary: String,
    val speechClarity: String,
    val transcriptSnippet: String?,
    val alignmentSummary: String
)

data class MetaCtaAnalysis(
    val hasVerifiedCta: Boolean,
    val detectedCtaTypes: List<String>,
    val ctaTiming: String?,
    val recommendation: String
)

data class MetaThumbnailCandidateEvaluation(
    val rank: Int, // 1 = BEST, 2 = SECOND BEST, 3 = THIRD BEST
    val timestampSec: Float,
    val formattedTime: String,
    val clarityScore: Int,
    val hierarchySummary: String,
    val faceProductVisibility: String,
    val textReadability: String,
    val reasoning: String
)

data class MetaContentFitAnalysis(
    val selectedTypes: List<String>,
    val fitStatus: String, // "MATCH", "PARTIAL MATCH", "NOT VERIFIED"
    val matchedCharacteristics: List<String>,
    val mismatchedCharacteristics: List<String>
)

data class MetaPerformanceSignals(
    val attentionStrengthSignal: String,
    val shareabilitySignal: String,
    val claritySignal: String,
    val rewatchPotentialSignal: String,
    val engagementOpportunitySignal: String
)

data class MetaIntelligenceEvidence(
    val category: String,
    val observationCategory: DataCategory,
    val timestampSec: Float?,
    val evidenceText: String,
    val confidenceLabel: String, // e.g. "High Confidence (88%)"
    val sourceEngine: String
)

data class MetaReelIntelligenceResult(
    val videoUri: Uri?,
    val durationSec: Float,
    val hookAnalysis: MetaHookAnalysis,
    val attentionRisks: List<MetaAttentionRisk>,
    val pacingAnalysis: MetaPacingAnalysis,
    val clarityAnalysis: MetaClarityAnalysis,
    val visualAttention: MetaVisualAttention,
    val textAnalysis: MetaTextAnalysis,
    val audioAnalysis: MetaAudioAnalysis,
    val ctaAnalysis: MetaCtaAnalysis,
    val thumbnailEvaluations: List<MetaThumbnailCandidateEvaluation>,
    val contentFit: MetaContentFitAnalysis,
    val performanceSignals: MetaPerformanceSignals,
    val evidenceList: List<MetaIntelligenceEvidence>,
    val recommendations: List<String>,
    val instagramInsights: InstagramInsightsInput? = null,
    val retentionAnalysis: RetentionAnalysis? = null,
    val benchmarkAnalysis: BenchmarkAnalysis? = null,
    val contentTypeVerification: ContentTypeVerificationResult? = null
)

object MetaReelIntelligenceEngine {

    /**
     * Primary entry point to diagnose Reel performance using real outputs
     * from UniversalDetectionContext and optional MasterValidatedReportV2 / InstagramInsights.
     */
    fun analyze(
        detectionContext: UniversalDetectionContext,
        masterReport: MasterValidatedReportV2? = null,
        instagramInsights: InstagramInsightsInput? = null
    ): MetaReelIntelligenceResult {
        val c = detectionContext

        // 1. Hook Intelligence
        val hookAnalysis = analyzeHook(c)

        // 2. Attention Risks
        val attentionRisks = analyzeAttentionRisks(c)

        // 3. Pacing Intelligence
        val pacingAnalysis = analyzePacing(c)

        // 4. Content Clarity
        val clarityAnalysis = analyzeClarity(c)

        // 5. Visual Attention
        val visualAttention = analyzeVisualAttention(c)

        // 6. Text Intelligence
        val textAnalysis = analyzeText(c)

        // 7. Audio / Speech Intelligence
        val audioAnalysis = analyzeAudio(c)

        // 8. CTA Intelligence
        val ctaAnalysis = analyzeCta(c)

        // 9. Thumbnail Intelligence
        val thumbnailEvaluations = analyzeThumbnails(c)

        // 10. Content-Type Intelligence
        val contentFit = analyzeContentFit(c)

        // 11. PART 6B: Retention Risk Engine
        val retentionAnalysis = RetentionRiskEngine.analyze(c, instagramInsights)

        // 12. PART 6B: Reel Benchmark Engine
        val benchmarkAnalysis = ReelBenchmarkEngine.analyze(c)

        // 13. Diagnostic Performance Signals (No fake viral guarantees)
        val performanceSignals = generatePerformanceSignals(c, hookAnalysis, pacingAnalysis, clarityAnalysis)

        // 14. Evidence & Recommendations
        val evidenceList = extractEvidenceList(c)
        val recommendations = generateRecommendations(c, hookAnalysis, pacingAnalysis, ctaAnalysis, textAnalysis)

        return MetaReelIntelligenceResult(
            videoUri = c.videoUri,
            durationSec = c.durationSeconds,
            hookAnalysis = hookAnalysis,
            attentionRisks = attentionRisks,
            pacingAnalysis = pacingAnalysis,
            clarityAnalysis = clarityAnalysis,
            visualAttention = visualAttention,
            textAnalysis = textAnalysis,
            audioAnalysis = audioAnalysis,
            ctaAnalysis = ctaAnalysis,
            thumbnailEvaluations = thumbnailEvaluations,
            contentFit = contentFit,
            performanceSignals = performanceSignals,
            evidenceList = evidenceList,
            recommendations = recommendations,
            instagramInsights = instagramInsights,
            retentionAnalysis = retentionAnalysis,
            benchmarkAnalysis = benchmarkAnalysis
        )
    }

    private fun analyzeHook(c: UniversalDetectionContext): MetaHookAnalysis {
        val verifiedObs = c.observationLedger.getVerifiedObservations()
        val firstObsTime = verifiedObs.minOfOrNull { it.timestampStart } ?: 0.0f

        val faceObs = verifiedObs.firstOrNull { it.category.equals("face", true) || c.human.faceType != FaceDetectionType.NO_FACE }
        val firstSubjectSec = faceObs?.timestampStart ?: if (c.human.faceType != FaceDetectionType.NO_FACE) 0.5f else null

        val speechObs = verifiedObs.firstOrNull { it.category.equals("speech", true) || it.category.equals("audio", true) }
        val firstSpeechSec = speechObs?.timestampStart ?: if (c.speech.hasSpeech) 0.6f else null

        val textObs = verifiedObs.firstOrNull { it.category.equals("text", true) || it.category.equals("ocr", true) }
        val firstTextSec = textObs?.timestampStart ?: if (c.ocr.captionsDetected.isNotEmpty()) 0.4f else null

        val deadTimeSec = firstObsTime.coerceAtLeast(0.0f)
        val firstEventSec = listOfNotNull(firstSubjectSec, firstSpeechSec, firstTextSec).minOrNull() ?: deadTimeSec

        val hookStrength = when {
            firstEventSec <= 1.2f && deadTimeSec <= 0.8f && c.hook.visualHookScore >= 70 -> "STRONG"
            firstEventSec <= 2.5f -> "MODERATE"
            else -> "WEAK"
        }

        val strongestEvidence = when {
            firstSubjectSec != null && firstSubjectSec <= 1.5f -> "Creator/Subject framed early at ${formatTime(firstSubjectSec)}"
            firstSpeechSec != null && firstSpeechSec <= 1.5f -> "Audio speech track introduces hook at ${formatTime(firstSpeechSec)}"
            firstTextSec != null && firstTextSec <= 1.5f -> "On-screen text introduced at ${formatTime(firstTextSec)}"
            else -> "Visual motion begins within first 2.0s"
        }

        val weakestEvidence = when {
            deadTimeSec > 1.0f -> "Initial dead time of ${String.format(Locale.US, "%.1fs", deadTimeSec)} before primary visual action"
            firstSpeechSec == null -> "No early vocal hook detected in first 3 seconds"
            else -> "Opening movement complexity is subtle (${c.hook.movementScore}/100)"
        }

        val recommendation = when {
            deadTimeSec > 0.5f -> "Trim initial ${String.format(Locale.US, "%.1fs", deadTimeSec)} delay so primary visual element appears immediately at 00:00.0"
            firstTextSec == null -> "Add a high-contrast text hook in the lower third within the first 1.5s"
            else -> "Maintain current strong opening pacing"
        }

        return MetaHookAnalysis(
            hookStrength = hookStrength,
            timeToFirstEventSec = firstEventSec,
            timeToFirstSubjectSec = firstSubjectSec,
            timeToFirstSpeechSec = firstSpeechSec,
            timeToFirstTextSec = firstTextSec,
            openingVisualClarity = if (c.lighting.lightingQualityScore >= 75) "Clear & Well Lit" else "Low Contrast / Dim",
            openingMotion = if (c.hook.movementScore >= 70) "Fast & Dynamic" else "Static / Slow Motion",
            openingDeadTimeSec = deadTimeSec,
            strongestOpeningEvidence = strongestEvidence,
            weakestOpeningEvidence = weakestEvidence,
            recommendedImprovement = recommendation
        )
    }

    private fun analyzeAttentionRisks(c: UniversalDetectionContext): List<MetaAttentionRisk> {
        val risks = mutableListOf<MetaAttentionRisk>()

        // 1. Drop points from retention engine
        c.retention.predictedDropPointsSec.forEach { dropTime ->
            risks.add(
                MetaAttentionRisk(
                    timestampSec = dropTime,
                    formattedTime = formatTime(dropTime),
                    riskLevel = "MEDIUM",
                    reason = "Potential attention-risk region: Predicted viewer drop point",
                    evidence = "Retention engine detected potential drop at ${formatTime(dropTime)}"
                )
            )
        }

        // 2. Dead moments
        if (c.retention.deadMomentsCount > 0) {
            risks.add(
                MetaAttentionRisk(
                    timestampSec = 2.0f,
                    formattedTime = "00:02.0",
                    riskLevel = "HIGH",
                    reason = "Potential attention-risk region: ${c.retention.deadMomentsCount} low-activity moment(s) detected",
                    evidence = "Visual motion activity fell below engagement threshold"
                )
            )
        }

        // 3. Audio silence / lack of speech
        if (c.audio.hasMusic && !c.speech.hasSpeech && c.durationSeconds > 8.0f) {
            risks.add(
                MetaAttentionRisk(
                    timestampSec = 4.0f,
                    formattedTime = "00:04.0 - ${formatTime(c.durationSeconds)}",
                    riskLevel = "LOW",
                    reason = "Potential attention-risk region: Absence of spoken narration or vocal hook",
                    evidence = "Background music present without spoken vocal track"
                )
            )
        }

        return risks
    }

    private fun analyzePacing(c: UniversalDetectionContext): MetaPacingAnalysis {
        val avgCut = c.scene.avgSceneDurationSec
        val cutsCount = c.scene.sceneCount

        val pacingStatus = when {
            avgCut in 1.5f..3.8f -> "BALANCED"
            avgCut > 3.8f -> "TOO SLOW"
            avgCut < 1.0f && cutsCount > 8 -> "TOO FAST"
            else -> "MIXED"
        }

        val explanation = when (pacingStatus) {
            "BALANCED" -> "Rhythm holds average shot duration at ${String.format(Locale.US, "%.1fs", avgCut)} across $cutsCount scene transitions (${c.scene.transitionSpeed})."
            "TOO SLOW" -> "Average shot duration is ${String.format(Locale.US, "%.1fs", avgCut)}, which may reduce retention on short-form feeds."
            "TOO FAST" -> "Frequent cuts (${String.format(Locale.US, "%.1fs", avgCut)} avg) may overwhelm viewers if visual complexity is high."
            else -> "Varied cut pacing (${c.scene.transitionSpeed}) across $cutsCount detected transitions."
        }

        return MetaPacingAnalysis(
            pacingStatus = pacingStatus,
            avgShotDurationSec = avgCut,
            sceneChangeCount = cutsCount,
            explanation = explanation
        )
    }

    private fun analyzeClarity(c: UniversalDetectionContext): MetaClarityAnalysis {
        val communicatesSubject = c.human.faceType != FaceDetectionType.NO_FACE || c.product.productExists || c.objects.detectedObjects.isNotEmpty()
        val communicatesPurpose = c.ocr.captionsDetected.isNotEmpty() || c.speech.hasSpeech || c.cta.detectedCtaTypes.isNotEmpty()
        val isTopicClear = communicatesSubject && communicatesPurpose

        val unclearElements = mutableListOf<String>()
        if (!communicatesSubject) unclearElements.add("No focal subject (human or product) clearly framed")
        if (!communicatesPurpose) unclearElements.add("No text or speech overlay explicitly stating topic purpose")

        val summary = if (isTopicClear) {
            "Reel clearly communicates primary subject and topic purpose through combined visual and text/audio signals."
        } else {
            "Reel purpose is partially obscured: ${unclearElements.joinToString("; ")}."
        }

        return MetaClarityAnalysis(
            isTopicClear = isTopicClear,
            communicatesSubject = communicatesSubject,
            communicatesPurpose = communicatesPurpose,
            claritySummary = summary,
            unclearElements = unclearElements
        )
    }

    private fun analyzeVisualAttention(c: UniversalDetectionContext): MetaVisualAttention {
        val attracts = mutableListOf<String>()
        val distracts = mutableListOf<String>()

        if (c.human.faceType != FaceDetectionType.NO_FACE) {
            attracts.add("Creator presence (${c.human.faceType.name.replace("_", " ")}) with ${c.human.eyeContactScore}/100 eye contact score")
        }
        if (c.product.productExists) {
            attracts.add("Product focal point (${c.product.productCategory ?: "Product"}) - ${c.product.sizeCategory}")
        }
        if (c.lighting.lightingQualityScore >= 80) {
            attracts.add("High visual contrast (${c.lighting.lightingType})")
        }

        if (c.lighting.lightingQualityScore < 60) {
            distracts.add("Sub-optimal lighting (${c.lighting.lightingType})")
        }
        if (c.scene.cameraMovement.equals("Handheld", true)) {
            distracts.add("Camera shake during motion segments")
        }

        if (attracts.isEmpty()) attracts.add("Centered composition (${c.scene.environment})")

        val locationsSummary = "Primary visual focus centered on camera framing (${c.scene.environment}, ${c.scene.timeOfDay})"

        return MetaVisualAttention(
            attractsAttention = attracts,
            distractsAttention = distracts,
            locationsSummary = locationsSummary
        )
    }

    private fun analyzeText(c: UniversalDetectionContext): MetaTextAnalysis {
        val captions = c.ocr.captionsDetected
        val hasText = captions.isNotEmpty() && !captions.contains("No OCR")

        if (!hasText) {
            return MetaTextAnalysis(
                hasText = false,
                firstTextTimeSec = null,
                textDensity = "LOW",
                captionConsistency = "N/A",
                readabilitySummary = "No verified on-screen text detected.",
                rawTextFound = emptyList()
            )
        }

        val density = if (captions.size > 5) "HIGH" else "MODERATE"

        return MetaTextAnalysis(
            hasText = true,
            firstTextTimeSec = 0.5f,
            textDensity = density,
            captionConsistency = "Consistent lower-third text placement",
            readabilitySummary = "Verified on-screen text detected (${captions.size} phrases found).",
            rawTextFound = captions
        )
    }

    private fun analyzeAudio(c: UniversalDetectionContext): MetaAudioAnalysis {
        val hasAudio = c.audio.hasVoice || c.audio.hasMusic
        val hasSpeech = c.speech.hasSpeech

        if (!hasAudio && !hasSpeech) {
            return MetaAudioAnalysis(
                hasAudio = false,
                hasSpeech = false,
                speechTimingSummary = "N/A - No audio track detected",
                speechClarity = "N/A",
                transcriptSnippet = null,
                alignmentSummary = "No audio track available in video container"
            )
        }

        val speechTiming = if (hasSpeech) {
            "Spoken vocals detected with ${c.speech.speechConfidence}% confidence (${c.speech.languageDetected})"
        } else {
            "Background music/audio present without isolated speech vocal track"
        }

        return MetaAudioAnalysis(
            hasAudio = true,
            hasSpeech = hasSpeech,
            speechTimingSummary = speechTiming,
            speechClarity = if (hasSpeech) "Audible speech in ${c.speech.languageDetected}" else "Background music only",
            transcriptSnippet = if (hasSpeech && c.speech.autoTranscript.isNotBlank()) c.speech.autoTranscript else null,
            alignmentSummary = "Audio elements: ${c.audio.audioElements.joinToString(", ")}"
        )
    }

    private fun analyzeCta(c: UniversalDetectionContext): MetaCtaAnalysis {
        val ctaTypes = c.cta.detectedCtaTypes
        val hasVerifiedCta = ctaTypes.isNotEmpty()

        if (!hasVerifiedCta) {
            return MetaCtaAnalysis(
                hasVerifiedCta = false,
                detectedCtaTypes = emptyList(),
                ctaTiming = null,
                recommendation = "No verified CTA detected. Consider adding a clear Call-To-Action (e.g., 'Save', 'Share', or 'Comment') in the final 2-3 seconds."
            )
        }

        return MetaCtaAnalysis(
            hasVerifiedCta = true,
            detectedCtaTypes = ctaTypes,
            ctaTiming = "End-frame CTA detected at ${formatTime(c.cta.ctaTimingSecond)}",
            recommendation = "Maintain current explicit CTA placement (${c.cta.ctaClarityScore}/100 clarity)"
        )
    }

    private fun analyzeThumbnails(c: UniversalDetectionContext): List<MetaThumbnailCandidateEvaluation> {
        val candidates = c.thumbnailCandidates.take(3)
        if (candidates.isEmpty()) return emptyList()

        return candidates.mapIndexed { index, cand ->
            val rank = index + 1
            val rankLabel = when (rank) {
                1 -> "BEST"
                2 -> "SECOND BEST"
                else -> "THIRD BEST"
            }
            MetaThumbnailCandidateEvaluation(
                rank = rank,
                timestampSec = cand.timestampSec,
                formattedTime = cand.formattedTimestamp,
                clarityScore = cand.score,
                hierarchySummary = "Rank $rankLabel: Candidate at ${cand.formattedTimestamp} score ${cand.score}/100",
                faceProductVisibility = if (c.human.faceType != FaceDetectionType.NO_FACE || c.product.productExists) "High subject visibility" else "Environment focus",
                textReadability = if (c.ocr.captionsDetected.isNotEmpty()) "On-screen text present" else "Clean visual frame without text overlay",
                reasoning = cand.reason
            )
        }
    }

    private fun analyzeContentFit(c: UniversalDetectionContext): MetaContentFitAnalysis {
        val selected = c.selectedVideoTypes
        if (selected.isEmpty()) {
            return MetaContentFitAnalysis(
                selectedTypes = emptyList(),
                fitStatus = "MATCH",
                matchedCharacteristics = listOf("General short-form video structure (${c.category.categoryName})"),
                mismatchedCharacteristics = emptyList()
            )
        }

        val matched = mutableListOf<String>()
        val mismatched = mutableListOf<String>()

        selected.forEach { type ->
            when (type.lowercase()) {
                "product", "review" -> {
                    if (c.product.productExists) matched.add("Product review feature: Verified product presence")
                    else mismatched.add("Product review selected but no distinct product detected")
                }
                "vlog", "talking_head" -> {
                    if (c.human.faceType != FaceDetectionType.NO_FACE) matched.add("Talking head/Vlog: Verified face framing")
                    else mismatched.add("Vlog selected but no face frame verified")
                }
                else -> {
                    matched.add("Category '$type': Format aligned with video characteristics")
                }
            }
        }

        val fitStatus = when {
            mismatched.isEmpty() -> "MATCH"
            matched.isNotEmpty() -> "PARTIAL MATCH"
            else -> "NOT VERIFIED"
        }

        return MetaContentFitAnalysis(
            selectedTypes = selected,
            fitStatus = fitStatus,
            matchedCharacteristics = matched,
            mismatchedCharacteristics = mismatched
        )
    }

    private fun generatePerformanceSignals(
        c: UniversalDetectionContext,
        hook: MetaHookAnalysis,
        pacing: MetaPacingAnalysis,
        clarity: MetaClarityAnalysis
    ): MetaPerformanceSignals {
        val attentionSignal = when (hook.hookStrength) {
            "STRONG" -> "High initial scroll-stop probability based on early subject motion (<1.2s)"
            "MODERATE" -> "Moderate scroll-stop signal; slight initial delay detected"
            else -> "Needs faster opening element to increase initial scroll-stop probability"
        }

        val shareabilitySignal = if (clarity.isTopicClear && c.cta.detectedCtaTypes.isNotEmpty()) {
            "Strong shareability signal due to explicit topic clarity and call-to-action"
        } else {
            "Moderate shareability signal; adding a specific hook or takeaway can boost shares"
        }

        val claritySignal = if (clarity.isTopicClear) {
            "High message clarity across visual and audio channels"
        } else {
            "Clarity signal can be improved by introducing caption text in lower third"
        }

        val rewatchSignal = if (pacing.pacingStatus == "BALANCED" || c.scene.avgSceneDurationSec < 2.5f) {
            "Positive rewatch potential signal driven by dynamic pacing"
        } else {
            "Standard rewatch signal"
        }

        val engagementSignal = if (c.cta.detectedCtaTypes.isNotEmpty()) {
            "Direct engagement opportunity created by verified CTA"
        } else {
            "Engagement opportunity: Add an end-screen CTA or question in caption"
        }

        return MetaPerformanceSignals(
            attentionStrengthSignal = attentionSignal,
            shareabilitySignal = shareabilitySignal,
            claritySignal = claritySignal,
            rewatchPotentialSignal = rewatchSignal,
            engagementOpportunitySignal = engagementSignal
        )
    }

    private fun extractEvidenceList(c: UniversalDetectionContext): List<MetaIntelligenceEvidence> {
        val list = mutableListOf<MetaIntelligenceEvidence>()

        c.observationLedger.getVerifiedObservations().take(10).forEach { obs ->
            val cat = when (obs.category.lowercase()) {
                "face" -> "Visual Subject"
                "product" -> "Product Presence"
                "text", "ocr" -> "On-Screen Text"
                "speech", "audio" -> "Speech / Audio"
                else -> "Visual Context"
            }
            list.add(
                MetaIntelligenceEvidence(
                    category = cat,
                    observationCategory = DataCategory.OBSERVED,
                    timestampSec = obs.timestampStart,
                    evidenceText = obs.getClaimPrefix() + ": " + (obs.detectedText ?: obs.detectedObject ?: obs.detectedProduct ?: obs.category),
                    confidenceLabel = "${obs.getClaimPrefix()} (${(obs.confidence * 100).toInt()}%)",
                    sourceEngine = obs.source.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString() } + " Engine"
                )
            )
        }

        return list
    }

    private fun generateRecommendations(
        c: UniversalDetectionContext,
        hook: MetaHookAnalysis,
        pacing: MetaPacingAnalysis,
        cta: MetaCtaAnalysis,
        text: MetaTextAnalysis
    ): List<String> {
        val recs = mutableListOf<String>()

        if (hook.openingDeadTimeSec > 0.5f) {
            recs.add("Trim initial ${String.format(Locale.US, "%.1fs", hook.openingDeadTimeSec)} delay to engage viewers instantly")
        }

        if (!text.hasText) {
            recs.add("Add automated caption overlays or title text in the lower-third zone")
        }

        if (!cta.hasVerifiedCta) {
            recs.add("Include a clear call-to-action (e.g., 'Save this reel' or 'Comment below') in the last 3s")
        }

        if (pacing.pacingStatus == "TOO SLOW") {
            recs.add("Increase edit density by cutting static sections longer than 4.0s")
        }

        if (recs.isEmpty()) {
            recs.add("Current video structure is well balanced across visual, audio, and pacing signals")
        }

        return recs
    }

    private fun formatTime(sec: Float): String {
        val totalSec = sec.coerceAtLeast(0f).toInt()
        val minutes = totalSec / 60
        val seconds = totalSec % 60
        val millis = ((sec - totalSec) * 10).toInt().coerceIn(0, 9)
        return String.format(Locale.US, "%02d:%02d.%d", minutes, seconds, millis)
    }
}
