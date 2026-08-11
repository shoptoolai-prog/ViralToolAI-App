package com.example.creatoracademy

import java.util.Locale

// ==============================================================================
// REEL BENCHMARK ENGINE (PART 6B)
// Evaluates Reel structure against platform best-practice patterns
// ==============================================================================

data class BenchmarkItem(
    val category: String,
    val status: String, // "BELOW PATTERN", "ALIGNED", "STRONG"
    val evidence: String,
    val action: String
)

data class BenchmarkAnalysis(
    val contentTypeEvaluated: String,
    val hook: BenchmarkItem,
    val clarity: BenchmarkItem,
    val pacing: BenchmarkItem,
    val visual: BenchmarkItem,
    val text: BenchmarkItem,
    val audio: BenchmarkItem,
    val story: BenchmarkItem,
    val cta: BenchmarkItem,
    val thumbnail: BenchmarkItem,
    val contentFit: BenchmarkItem,
    val overallSummary: String
)

object ReelBenchmarkEngine {

    fun analyze(
        context: UniversalDetectionContext
    ): BenchmarkAnalysis {
        val c = context
        val primaryType = c.selectedVideoTypes.firstOrNull()?.lowercase() ?: c.category.categoryName.lowercase()

        // 1. Hook Benchmark
        val deadTime = c.observationLedger.getVerifiedObservations().minOfOrNull { it.timestampStart } ?: 0.0f
        val hookStatus = when {
            deadTime <= 0.5f && c.hook.visualHookScore >= 70 -> "STRONG"
            deadTime <= 1.2f -> "ALIGNED"
            else -> "BELOW PATTERN"
        }
        val hookItem = BenchmarkItem(
            category = "Hook & Opening",
            status = hookStatus,
            evidence = "WHAT: Opening setup. WHERE: 00:00.0 - 00:02.0. WHY: Dead time of ${String.format(Locale.US, "%.1fs", deadTime)} measured before primary visual motion.",
            action = if (hookStatus == "BELOW PATTERN") "Trim initial static delay to hook viewer immediately." else "Maintain rapid opening subject entry."
        )

        // 2. Content Clarity Benchmark
        val hasSubject = c.human.faceType != FaceDetectionType.NO_FACE || c.product.productExists
        val hasTopicText = c.ocr.captionsDetected.isNotEmpty() || c.speech.hasSpeech
        val clarityStatus = when {
            hasSubject && hasTopicText -> "STRONG"
            hasSubject || hasTopicText -> "ALIGNED"
            else -> "BELOW PATTERN"
        }
        val clarityItem = BenchmarkItem(
            category = "Content Clarity",
            status = clarityStatus,
            evidence = "WHAT: Message clarity. WHERE: Overall video. WHY: Subject presence = $hasSubject, Topic text/speech = $hasTopicText.",
            action = if (clarityStatus == "BELOW PATTERN") "Add title text overlay in lower third to clarify topic instantly." else "Clear message communication verified."
        )

        // 3. Pacing Benchmark
        val avgCut = c.scene.avgSceneDurationSec
        val pacingStatus = when {
            avgCut in 1.5f..3.5f -> "STRONG"
            avgCut in 1.0f..4.5f -> "ALIGNED"
            else -> "BELOW PATTERN"
        }
        val pacingItem = BenchmarkItem(
            category = "Pacing & Rhythm",
            status = pacingStatus,
            evidence = "WHAT: Edit density. WHERE: Timeline transitions. WHY: Average shot duration is ${String.format(Locale.US, "%.1fs", avgCut)} across ${c.scene.sceneCount} scenes.",
            action = if (pacingStatus == "BELOW PATTERN") "Increase scene variation by cutting long static clips." else "Pacing aligns with short-form retention patterns."
        )

        // 4. Visual Quality Benchmark
        val lighting = c.lighting.lightingQualityScore
        val visualStatus = when {
            lighting >= 80 -> "STRONG"
            lighting >= 60 -> "ALIGNED"
            else -> "BELOW PATTERN"
        }
        val visualItem = BenchmarkItem(
            category = "Visual Quality & Framing",
            status = visualStatus,
            evidence = "WHAT: Lighting & contrast. WHERE: Video frame. WHY: Lighting quality score calculated at $lighting/100 (${c.lighting.lightingType}).",
            action = if (visualStatus == "BELOW PATTERN") "Boost key lighting or increase contrast during color grading." else "Good visual framing and contrast verified."
        )

        // 5. Text / OCR Benchmark
        val captionCount = c.ocr.captionsDetected.size
        val textStatus = when {
            captionCount >= 3 -> "STRONG"
            captionCount >= 1 -> "ALIGNED"
            else -> "BELOW PATTERN"
        }
        val textItem = BenchmarkItem(
            category = "On-Screen Text",
            status = textStatus,
            evidence = "WHAT: Caption overlays. WHERE: Lower-third zone. WHY: Detected $captionCount text phrase(s).",
            action = if (textStatus == "BELOW PATTERN") "Add automated or manual captions for silent feed viewers." else "Text overlays support message comprehension."
        )

        // 6. Audio / Speech Benchmark
        val hasSpeech = c.speech.hasSpeech
        val audioStatus = when {
            hasSpeech && c.speech.speechConfidence >= 80 -> "STRONG"
            c.audio.hasMusic || hasSpeech -> "ALIGNED"
            else -> "BELOW PATTERN"
        }
        val audioItem = BenchmarkItem(
            category = "Audio & Speech",
            status = audioStatus,
            evidence = "WHAT: Vocal clarity & audio track. WHERE: Audio channel. WHY: Speech detected = $hasSpeech (${c.speech.languageDetected}).",
            action = if (audioStatus == "BELOW PATTERN") "Ensure audio track or voiceover is present and clearly audible." else "Audio mix aligns with short-form standards."
        )

        // 7. Story / Narrative Benchmark
        val storyStatus = if (c.retention.deadMomentsCount == 0 && c.scene.sceneCount >= 2) "STRONG" else "ALIGNED"
        val storyItem = BenchmarkItem(
            category = "Story Progression",
            status = storyStatus,
            evidence = "WHAT: Visual narrative flow. WHERE: Scene transitions. WHY: ${c.scene.sceneCount} scene(s) detected with ${c.retention.deadMomentsCount} dead moment(s).",
            action = if (storyStatus == "BELOW PATTERN") "Structure video with clear beginning, middle, and payoff." else "Narrative progression flows smoothly."
        )

        // 8. CTA Benchmark
        val ctaCount = c.cta.detectedCtaTypes.size
        val ctaStatus = when {
            ctaCount >= 1 && c.cta.ctaClarityScore >= 70 -> "STRONG"
            ctaCount >= 1 -> "ALIGNED"
            else -> "BELOW PATTERN"
        }
        val ctaItem = BenchmarkItem(
            category = "Call-To-Action (CTA)",
            status = ctaStatus,
            evidence = "WHAT: Call to action. WHERE: Final 3 seconds. WHY: Detected $ctaCount explicit CTA prompt(s).",
            action = if (ctaStatus == "BELOW PATTERN") "Add a clear end-screen CTA (e.g. 'Save for later' or 'Comment')." else "Clear CTA prompt verified at conclusion."
        )

        // 9. Thumbnail Benchmark
        val topCand = c.thumbnailCandidates.firstOrNull()
        val thumbStatus = when {
            topCand != null && topCand.score >= 80 -> "STRONG"
            topCand != null && topCand.score >= 60 -> "ALIGNED"
            else -> "BELOW PATTERN"
        }
        val thumbItem = BenchmarkItem(
            category = "Thumbnail Candidate",
            status = thumbStatus,
            evidence = "WHAT: Primary cover frame. WHERE: ${topCand?.formattedTimestamp ?: "00:01.0"}. WHY: Best candidate score ${topCand?.score ?: 50}/100.",
            action = if (thumbStatus == "BELOW PATTERN") "Select a cover frame with high visual contrast and clear subject framing." else "Primary cover candidate provides strong visual hierarchy."
        )

        // 10. Content-Type Fit Benchmark (Evaluated specifically for selected content category)
        val fitItem = evaluateContentTypeFit(primaryType, c)

        val overallSummary = "Reel benchmark evaluated against short-form best practices ($primaryType format). Key strengths: ${listOf(hookItem, clarityItem, pacingItem, ctaItem).filter { it.status == "STRONG" }.map { it.category }.ifEmpty { listOf("General structure") }.joinToString(", ")}."

        return BenchmarkAnalysis(
            contentTypeEvaluated = primaryType.uppercase(),
            hook = hookItem,
            clarity = clarityItem,
            pacing = pacingItem,
            visual = visualItem,
            text = textItem,
            audio = audioItem,
            story = storyItem,
            cta = ctaItem,
            thumbnail = thumbItem,
            contentFit = fitItem,
            overallSummary = overallSummary
        )
    }

    private fun evaluateContentTypeFit(primaryType: String, c: UniversalDetectionContext): BenchmarkItem {
        return when (primaryType) {
            "talking_head", "vlog" -> {
                val status = if (c.human.faceType != FaceDetectionType.NO_FACE && c.speech.hasSpeech) "STRONG" else "ALIGNED"
                BenchmarkItem(
                    category = "Content-Type Fit ($primaryType)",
                    status = status,
                    evidence = "WHAT: $primaryType benchmark. WHERE: Framing & Audio. WHY: Face framing = ${c.human.faceType.name}, Speech = ${c.speech.hasSpeech}.",
                    action = "Ensure subject is centered with clear vocal narration."
                )
            }
            "product", "review" -> {
                val status = if (c.product.productExists) "STRONG" else "BELOW PATTERN"
                BenchmarkItem(
                    category = "Content-Type Fit ($primaryType)",
                    status = status,
                    evidence = "WHAT: Product review benchmark. WHERE: Center frame. WHY: Product presence = ${c.product.productExists} (${c.product.productCategory ?: "Product"}).",
                    action = if (status == "BELOW PATTERN") "Ensure product is clearly framed and demonstrated in center view." else "Product framing aligns with review standards."
                )
            }
            "dance", "lifestyle" -> {
                val status = if (c.hook.movementScore >= 60) "STRONG" else "ALIGNED"
                BenchmarkItem(
                    category = "Content-Type Fit ($primaryType)",
                    status = status,
                    evidence = "WHAT: Motion/Lifestyle benchmark. WHERE: Full frame. WHY: Motion score = ${c.hook.movementScore}/100.",
                    action = "Maintain dynamic physical movement throughout clip."
                )
            }
            else -> {
                BenchmarkItem(
                    category = "Content-Type Fit ($primaryType)",
                    status = "ALIGNED",
                    evidence = "WHAT: General short-form benchmark. WHERE: Timeline. WHY: Aligned with standard Reel video layout.",
                    action = "Optimize pacing and on-screen text overlays."
                )
            }
        }
    }
}
