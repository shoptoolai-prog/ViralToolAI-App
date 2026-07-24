package com.example.ai.vision

/**
 * SHOPTOOLAI Phase 8B — Gemini Vision Analysis Stages
 * Mandatory 5-stage pipeline for image analysis feedback
 */
enum class GeminiVisionStage(val label: String, val progressPercent: Int) {
    UPLOADING_IMAGE("Uploading Image...", 20),
    CONNECTING_GEMINI("Connecting Gemini...", 40),
    READING_SCREENSHOT("Reading Screenshot...", 60),
    GENERATING_INSIGHTS("Generating Insights...", 80),
    PREPARING_REPORT("Preparing Premium Report...", 95),
    COMPLETED("Vision Analysis Complete", 100),
    ERROR("Vision Analysis Error", 0)
}
