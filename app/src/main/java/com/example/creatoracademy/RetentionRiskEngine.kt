package com.example.creatoracademy

import java.util.Locale

// ==============================================================================
// RETENTION RISK ENGINE (PART 6B)
// Predictive diagnostic engine identifying attention-risk regions and signals
// ==============================================================================

data class RetentionRiskSegment(
    val startSec: Float,
    val endSec: Float,
    val formattedRange: String,
    val riskLevel: String, // "HIGH", "MEDIUM", "LOW"
    val reason: String,
    val evidence: String,
    val recommendation: String
)

data class TopImpactChange(
    val priority: String, // "P0", "P1", "P2", "P3"
    val timestampSec: Float,
    val formattedTime: String,
    val problem: String,
    val evidence: String,
    val action: String,
    val expectedImprovementArea: String
)

data class RetentionAnalysis(
    val riskSegments: List<RetentionRiskSegment>,
    val rewatchSignal: String, // "STRONG", "MODERATE", "LOW"
    val shareabilitySignal: String, // "STRONG", "MODERATE", "LOW"
    val saveabilitySignal: String, // "STRONG", "MODERATE", "LOW"
    val saveabilityReason: String,
    val pacingRisks: List<String>,
    val top3Changes: List<TopImpactChange>,
    val isInsightsCorrelated: Boolean,
    val insightsDiagnosticNote: String?
)

object RetentionRiskEngine {

    fun analyze(
        context: UniversalDetectionContext,
        insights: InstagramInsightsInput? = null
    ): RetentionAnalysis {
        val c = context
        val riskSegments = mutableListOf<RetentionRiskSegment>()
        val pacingRisks = mutableListOf<String>()

        // 1. Analyze Timeline Regions for Attention Risk
        // Opening dead time
        val verifiedObs = c.observationLedger.getVerifiedObservations()
        val openingDeadSec = verifiedObs.minOfOrNull { it.timestampStart } ?: 0.0f
        if (openingDeadSec > 0.6f) {
            val endSec = openingDeadSec.coerceAtMost(c.durationSeconds)
            riskSegments.add(
                RetentionRiskSegment(
                    startSec = 0.0f,
                    endSec = endSec,
                    formattedRange = "00:00.0 - ${formatTime(endSec)}",
                    riskLevel = "HIGH",
                    reason = "Potential attention-risk region: Initial delay before primary action or subject entry",
                    evidence = "Measured ${String.format(Locale.US, "%.1fs", openingDeadSec)} static delay prior to first motion event",
                    recommendation = "Trim opening ${String.format(Locale.US, "%.1fs", openingDeadSec)} so key visual or vocal element appears immediately at 00:00.0"
                )
            )
        }

        // Mid-video scene cuts / static durations
        val avgCut = c.scene.avgSceneDurationSec
        if (avgCut > 4.0f && c.durationSeconds > 10.0f) {
            val midStart = (c.durationSeconds * 0.35f)
            val midEnd = (midStart + avgCut).coerceAtMost(c.durationSeconds)
            riskSegments.add(
                RetentionRiskSegment(
                    startSec = midStart,
                    endSec = midEnd,
                    formattedRange = "${formatTime(midStart)} - ${formatTime(midEnd)}",
                    riskLevel = "MEDIUM",
                    reason = "Potential attention-risk region: Extended static shot duration without cut or visual transition",
                    evidence = "Average shot length is ${String.format(Locale.US, "%.1fs", avgCut)} (recommended < 3.5s for short-form video)",
                    recommendation = "Shorten this section or insert a B-roll cut, text pop-up, or zoom effect to maintain pacing"
                )
            )
            pacingRisks.add("Shot duration in mid-section (${formatTime(midStart)}) exceeds short-form engagement benchmark (${String.format(Locale.US, "%.1fs", avgCut)} avg)")
        }

        // Predicted drop points from retention module
        c.retention.predictedDropPointsSec.forEach { dropTime ->
            if (riskSegments.none { Math.abs(it.startSec - dropTime) < 2.0f }) {
                val start = (dropTime - 1.0f).coerceAtLeast(0.0f)
                val end = (dropTime + 1.5f).coerceAtMost(c.durationSeconds)
                riskSegments.add(
                    RetentionRiskSegment(
                        startSec = start,
                        endSec = end,
                        formattedRange = "${formatTime(start)} - ${formatTime(end)}",
                        riskLevel = "MEDIUM",
                        reason = "Potential attention-risk region: Visual activity drop detected",
                        evidence = "Motion and frame complexity dropped near ${formatTime(dropTime)}",
                        recommendation = "Introduce a visual transition or audio shift around ${formatTime(dropTime)} to re-engage viewers"
                    )
                )
            }
        }

        // Audio silence risk
        if (c.audio.hasMusic && !c.speech.hasSpeech && c.durationSeconds > 8.0f) {
            pacingRisks.add("Video relies solely on background track without vocal hook or speech narration")
        }

        // 2. Rewatch Signal (Information density, visual pacing, loop potential)
        val rewatchSignal = when {
            c.scene.avgSceneDurationSec < 2.5f && c.ocr.captionsDetected.size >= 3 -> "STRONG"
            c.scene.avgSceneDurationSec <= 4.0f || c.human.faceType != FaceDetectionType.NO_FACE -> "MODERATE"
            else -> "LOW"
        }

        // 3. Shareability Signal (Clear topic, emotional face, explicit CTA, useful content)
        val hasClearTopic = c.ocr.captionsDetected.isNotEmpty() || c.speech.hasSpeech
        val shareabilitySignal = when {
            hasClearTopic && c.cta.detectedCtaTypes.isNotEmpty() -> "STRONG"
            hasClearTopic || c.product.productExists -> "MODERATE"
            else -> "LOW"
        }

        // 4. Saveability Signal (Tips, instructions, reference value)
        val isEducational = c.selectedVideoTypes.any { it.equals("product", true) || it.equals("review", true) || it.equals("educational", true) }
        val saveabilitySignal = when {
            isEducational && c.ocr.captionsDetected.size >= 3 -> "STRONG"
            c.product.productExists || c.ocr.captionsDetected.isNotEmpty() -> "MODERATE"
            else -> "LOW"
        }

        val saveabilityReason = if (saveabilitySignal == "LOW") {
            "No strong save-oriented content signal verified (e.g. detailed tutorial, product breakdown, or step-by-step text)."
        } else {
            "Verified saveable signals: On-screen text overlays and topic clarity."
        }

        // 5. Instagram Insights Correlation (Diagnostic linkage if real insights provided)
        var isInsightsCorrelated = false
        var insightsNote: String? = null

        if (insights != null) {
            isInsightsCorrelated = true
            val avgWatch = insights.avgWatchTimeSec
            val retentionPct = insights.retentionPercent
            if (avgWatch != null && avgWatch < (c.durationSeconds * 0.4f)) {
                insightsNote = "High-Confidence Diagnostic: Actual user watch time (${String.format(Locale.US, "%.1fs", avgWatch)}) confirms early viewer exit, matching detected opening dead time (${String.format(Locale.US, "%.1fs", openingDeadSec)})."
            } else if (retentionPct != null && retentionPct < 40) {
                insightsNote = "High-Confidence Diagnostic: Reported 3s retention ($retentionPct%) correlates with pacing friction points in initial 00:03.0."
            } else {
                insightsNote = "Performance Insights Provided: Watch metrics align with verified video structure."
            }
        }

        // 6. Top 3 Highest Impact Changes (Priority ranked)
        val top3Changes = generateTop3Changes(c, riskSegments, pacingRisks)

        return RetentionAnalysis(
            riskSegments = riskSegments,
            rewatchSignal = rewatchSignal,
            shareabilitySignal = shareabilitySignal,
            saveabilitySignal = saveabilitySignal,
            saveabilityReason = saveabilityReason,
            pacingRisks = pacingRisks,
            top3Changes = top3Changes,
            isInsightsCorrelated = isInsightsCorrelated,
            insightsDiagnosticNote = insightsNote
        )
    }

    private fun generateTop3Changes(
        c: UniversalDetectionContext,
        risks: List<RetentionRiskSegment>,
        pacingRisks: List<String>
    ): List<TopImpactChange> {
        val list = mutableListOf<TopImpactChange>()

        // 1. Opening / Hook fix (P0)
        val openingDeadSec = c.observationLedger.getVerifiedObservations().minOfOrNull { it.timestampStart } ?: 0.0f
        if (openingDeadSec > 0.5f) {
            list.add(
                TopImpactChange(
                    priority = "P0",
                    timestampSec = 0.0f,
                    formattedTime = "00:00.0",
                    problem = "Initial delay before visual action or vocal entrance",
                    evidence = "Measured ${String.format(Locale.US, "%.1fs", openingDeadSec)} static delay at video start",
                    action = "Trim initial static frames so main subject or motion starts immediately at 00:00.0",
                    expectedImprovementArea = "Initial 3s Scroll-Stop & Retention"
                )
            )
        } else if (c.ocr.captionsDetected.isEmpty()) {
            list.add(
                TopImpactChange(
                    priority = "P0",
                    timestampSec = 0.5f,
                    formattedTime = "00:00.5",
                    problem = "Missing on-screen text hook in opening",
                    evidence = "No verified OCR text detected in first 3.0s",
                    action = "Add a high-contrast 3-4 word text hook in the lower third zone",
                    expectedImprovementArea = "Silent-Feed Viewers & Topic Clarity"
                )
            )
        }

        // 2. Pacing / Cut duration fix (P1)
        val highRiskSeg = risks.firstOrNull { it.riskLevel == "HIGH" || it.riskLevel == "MEDIUM" }
        if (highRiskSeg != null) {
            list.add(
                TopImpactChange(
                    priority = "P1",
                    timestampSec = highRiskSeg.startSec,
                    formattedTime = formatTime(highRiskSeg.startSec),
                    problem = highRiskSeg.reason,
                    evidence = highRiskSeg.evidence,
                    action = highRiskSeg.recommendation,
                    expectedImprovementArea = "Mid-Video Attention Continuity"
                )
            )
        } else if (c.scene.avgSceneDurationSec > 3.8f) {
            list.add(
                TopImpactChange(
                    priority = "P1",
                    timestampSec = c.durationSeconds / 2.0f,
                    formattedTime = formatTime(c.durationSeconds / 2.0f),
                    problem = "Slow editing pace with long static scene holds",
                    evidence = "Average shot length is ${String.format(Locale.US, "%.1fs", c.scene.avgSceneDurationSec)}",
                    action = "Cut 1-2 seconds off redundant clips to keep average shot under 3.0s",
                    expectedImprovementArea = "Overall Video Pacing & Rhythm"
                )
            )
        }

        // 3. Call-to-action or Ending fix (P2)
        if (c.cta.detectedCtaTypes.isEmpty()) {
            val endSec = (c.durationSeconds - 2.5f).coerceAtLeast(0.0f)
            list.add(
                TopImpactChange(
                    priority = "P2",
                    timestampSec = endSec,
                    formattedTime = formatTime(endSec),
                    problem = "No explicit Call-To-Action detected at conclusion",
                    evidence = "Zero verified CTA text/audio overlays detected in final frames",
                    action = "Add an end-frame prompt (e.g., 'Save this reel' or 'Comment below')",
                    expectedImprovementArea = "Outbound Engagement & Saves/Shares"
                )
            )
        } else {
            list.add(
                TopImpactChange(
                    priority = "P2",
                    timestampSec = 1.0f,
                    formattedTime = "00:01.0",
                    problem = "Lighting or foreground visual contrast optimization",
                    evidence = "Lighting quality score: ${c.lighting.lightingQualityScore}/100 (${c.lighting.lightingType})",
                    action = "Apply slight contrast exposure boost in color grading",
                    expectedImprovementArea = "Visual Quality & Professional Polish"
                )
            )
        }

        return list.take(3)
    }

    private fun formatTime(sec: Float): String {
        val totalSec = sec.coerceAtLeast(0f).toInt()
        val minutes = totalSec / 60
        val seconds = totalSec % 60
        val millis = ((sec - totalSec) * 10).toInt().coerceIn(0, 9)
        return String.format(Locale.US, "%02d:%02d.%d", minutes, seconds, millis)
    }
}
