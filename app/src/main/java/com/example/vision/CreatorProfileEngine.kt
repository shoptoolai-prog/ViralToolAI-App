package com.example.vision

import com.example.creator.AiCreatorEngine
import com.example.creator.AiCreatorReport
import com.example.creator.CreatorInputType

/**
 * PHASE 12C — Dedicated Creator Profile Vision Engine
 * Extracts strictly visible creator profile data from screenshots.
 * NEVER outputs shopping reports or product price comparisons.
 */

data class CreatorProfileExtractionResult(
    val username: ExtractedField<String>,
    val displayName: ExtractedField<String>,
    val bio: ExtractedField<String>,
    val followers: ExtractedField<Long>,
    val following: ExtractedField<Long>,
    val posts: ExtractedField<Int>,
    val category: ExtractedField<String>,
    val actionButtons: ExtractedField<List<String>>,
    val creatorReport: AiCreatorReport,
    val confidenceMetrics: VisionConfidenceMetrics
)

object CreatorProfileEngine {

    suspend fun processCreatorScreenshot(imageUriOrPath: String): CreatorProfileExtractionResult {
        val report = AiCreatorEngine.analyzeCreatorScreenshot(
            inputType = CreatorInputType.INSTAGRAM_PROFILE_SCREENSHOT,
            imageUriOrPath = imageUriOrPath
        )

        val profile = report.profileData ?: com.example.creator.ProfileDetectionData()

        val usernameField = ExtractedField("Username", profile.username, isDetected = profile.isUsernameVerified, confidence = if (profile.isUsernameVerified) 0.95 else 0.0)
        val displayNameField = ExtractedField("Display Name", profile.displayName, isDetected = profile.isDisplayNameVerified, confidence = if (profile.isDisplayNameVerified) 0.92 else 0.0)
        val bioField = ExtractedField("Bio", profile.bio, isDetected = profile.isBioVerified, confidence = if (profile.isBioVerified) 0.90 else 0.0)
        val followersField = ExtractedField("Followers", profile.followersCount, isDetected = profile.isFollowersVerified, confidence = if (profile.isFollowersVerified) 0.94 else 0.0)
        val followingField = ExtractedField("Following", profile.followingCount, isDetected = profile.isFollowingVerified, confidence = if (profile.isFollowingVerified) 0.94 else 0.0)
        val postsField = ExtractedField("Posts", profile.postsCount, isDetected = profile.isPostsVerified, confidence = if (profile.isPostsVerified) 0.94 else 0.0)
        val categoryField = ExtractedField("Category", profile.category, isDetected = profile.category != null, confidence = 0.88)
        val buttonsField = ExtractedField("Action Buttons", profile.actionButtons, isDetected = profile.actionButtons.isNotEmpty(), confidence = 0.85)

        val confidence = if (profile.isOcrFailedCompletely) 0.20 else 0.90

        return CreatorProfileExtractionResult(
            username = usernameField,
            displayName = displayNameField,
            bio = bioField,
            followers = followersField,
            following = followingField,
            posts = postsField,
            category = categoryField,
            actionButtons = buttonsField,
            creatorReport = report,
            confidenceMetrics = VisionConfidenceMetrics(
                classificationConfidence = 0.96,
                ocrQualityScore = confidence,
                overallConfidence = confidence
            )
        )
    }
}
