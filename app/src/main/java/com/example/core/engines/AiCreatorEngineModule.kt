package com.example.core.engines

import com.example.core.interfaces.ICreatorExtractor
import com.example.core.log.AiInternalLogger
import com.example.core.log.LogCategory
import com.example.core.model.AiResponseStatus
import com.example.core.model.StandardAiResponse
import com.example.creator.AiCreatorEngine
import com.example.creator.AiCreatorReport
import com.example.creator.CreatorInputType

/**
 * SHOPTOOLAI Phase 12A — Real Creator Pipeline Intelligence Engine
 *
 * PIPELINE SEQUENCE:
 * Screenshot
 *  ↓
 * OCR
 *  ↓
 * Profile Detection
 *  ↓
 * Creator Analysis
 *  ↓
 * Growth Suggestions
 *  ↓
 * Final Report
 *
 * Completely separated from Shopping Pipeline.
 */
object AiCreatorEngineModule : ICreatorExtractor {

    override suspend fun extractCreatorProfile(usernameOrScreenshot: String): StandardAiResponse<AiCreatorReport> {
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "--- Creator Pipeline Stage 1: Input Received ---", usernameOrScreenshot)

        val trimmed = usernameOrScreenshot.trim()
        if (trimmed.isBlank()) {
            AiInternalLogger.log(LogCategory.DETECTION_FAILURE, "Empty input provided to Creator Engine")
            return StandardAiResponse(
                status = AiResponseStatus.FAILURE,
                confidence = 0.0,
                errorMessage = "Couldn't confidently identify creator: empty input",
                warnings = listOf("Please provide a valid creator username or upload a profile screenshot."),
                sourceType = "CREATOR_PIPELINE"
            )
        }

        val detectedFields = mutableListOf<String>()
        val missingFields = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // STAGE 2: OCR Engine Analysis
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "--- Creator Pipeline Stage 2: OCR Analysis ---")
        val report = AiCreatorEngine.analyzeCreatorScreenshot(
            inputType = CreatorInputType.INSTAGRAM_PROFILE_SCREENSHOT,
            imageUriOrPath = trimmed
        )

        // STAGE 3: Profile Detection Verification
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "--- Creator Pipeline Stage 3: Profile Field Detection ---")
        val profile = report.profileData

        if (profile != null) {
            if (profile.isUsernameVerified && !profile.username.isNullOrBlank()) detectedFields.add("username") else missingFields.add("username")
            if (profile.isDisplayNameVerified && !profile.displayName.isNullOrBlank()) detectedFields.add("displayName") else missingFields.add("displayName")
            if (profile.isBioVerified && !profile.bio.isNullOrBlank()) detectedFields.add("bio") else missingFields.add("bio")
            if (profile.isFollowersVerified && profile.followersCount != null) detectedFields.add("followersCount") else missingFields.add("followersCount")
            if (profile.isFollowingVerified && profile.followingCount != null) detectedFields.add("followingCount") else missingFields.add("followingCount")
            if (profile.isPostsVerified && profile.postsCount != null) detectedFields.add("postsCount") else missingFields.add("postsCount")
        } else {
            missingFields.addAll(listOf("username", "displayName", "bio", "followersCount", "followingCount", "postsCount"))
            warnings.add("Couldn't confidently identify profile details from screenshot.")
        }

        // STAGE 4: Creator Analysis
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "--- Creator Pipeline Stage 4: Creator Analysis ---")
        if (report.contentAnalysis != null) {
            detectedFields.add("contentAnalysis")
        }

        // STAGE 5: Growth Suggestions
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "--- Creator Pipeline Stage 5: Growth Suggestions ---")
        if (report.growthStrategyHinglish.isNotEmpty()) {
            detectedFields.add("growthStrategy")
        }

        // STAGE 6: Final Report Assembly
        AiInternalLogger.log(LogCategory.PIPELINE_EVENT, "--- Creator Pipeline Stage 6: Final Report Assembly ---")

        // Calculate REAL Confidence Score:
        val totalFields = 6.0
        val detectedCount = detectedFields.count { it in listOf("username", "displayName", "bio", "followersCount", "followingCount", "postsCount") }.toDouble()
        val confidence = (detectedCount / totalFields).coerceIn(0.1, 0.98)

        AiInternalLogger.log(
            LogCategory.PIPELINE_EVENT,
            "Creator Pipeline Completed",
            "Detected $detectedCount / 6 profile fields",
            confidence = confidence
        )

        if (confidence < 0.5) {
            warnings.add("Couldn't confidently identify all creator profile fields.")
        }

        val status = when {
            confidence >= 0.70 -> AiResponseStatus.SUCCESS
            confidence >= 0.25 -> AiResponseStatus.PARTIAL
            else -> AiResponseStatus.FAILURE
        }

        return StandardAiResponse(
            status = status,
            confidence = confidence,
            detectedData = report,
            detectedFields = detectedFields,
            missingFields = missingFields,
            warnings = warnings,
            sourceType = "REAL_CREATOR_PIPELINE"
        )
    }
}
