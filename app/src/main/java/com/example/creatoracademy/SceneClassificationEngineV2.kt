package com.example.creatoracademy

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri

// ==============================================================================
// SCENE CLASSIFICATION ENGINE V2.0 — MASTER AI BRAIN & DECISION ENGINE
// ==============================================================================

/**
 * STEP 2 — PRIMARY CATEGORY
 */
enum class PrimaryCategory(val label: String) {
    EDUCATIONAL("Educational"),
    STUDY("Study"),
    TUTORIAL("Tutorial"),
    PRODUCT_REVIEW("Product Review"),
    UGC("UGC"),
    AFFILIATE("Affiliate"),
    BEAUTY("Beauty"),
    FASHION("Fashion"),
    SKINCARE("Skincare"),
    TRAVEL("Travel"),
    VLOG("Vlog"),
    GAMING("Gaming"),
    PODCAST("Podcast"),
    INTERVIEW("Interview"),
    MOTIVATION("Motivation"),
    STORYTELLING("Storytelling"),
    COMEDY("Comedy"),
    NEWS("News"),
    FITNESS("Fitness"),
    FOOD("Food"),
    COOKING("Cooking"),
    TECHNOLOGY("Technology"),
    BUSINESS("Business"),
    FINANCE("Finance"),
    AUTOMOBILE("Automobile"),
    MUSIC("Music"),
    MOVIE("Movie"),
    ANIMATION("Animation"),
    DOCUMENTARY("Documentary"),
    NATURE("Nature"),
    SPIRITUAL("Spiritual"),
    SPORTS("Sports"),
    PET("Pet"),
    LIFESTYLE("Lifestyle"),
    MEDICAL("Medical"),
    OTHER("Other")
}

/**
 * STEP 3 — SECONDARY CATEGORY
 */
enum class SecondaryCategory(val label: String) {
    STUDY("Study"),
    MOTIVATION("Motivation"),
    VLOG("Vlog"),
    BUSINESS("Business"),
    COMEDY("Comedy"),
    BEAUTY("Beauty"),
    TUTORIAL("Tutorial"),
    PRODUCT_REVIEW("Product Review"),
    FITNESS("Fitness"),
    FINANCE("Finance"),
    ENTERTAINMENT("Entertainment"),
    NONE("None")
}

/**
 * STEP 5 — ENVIRONMENT
 */
enum class EnvironmentType(val label: String) {
    INDOOR("Indoor"),
    OUTDOOR("Outdoor"),
    OFFICE("Office"),
    BEDROOM("Bedroom"),
    CLASSROOM("Classroom"),
    LIBRARY("Library"),
    STUDIO("Studio"),
    KITCHEN("Kitchen"),
    GYM("Gym"),
    ROAD("Road"),
    CAFE("Cafe"),
    RESTAURANT("Restaurant"),
    NATURE("Nature"),
    VEHICLE("Vehicle"),
    STORE("Store"),
    HOME("Home"),
    UNKNOWN("Unknown")
}

/**
 * STEP 6 — CONTENT STYLE
 */
enum class ContentStyle(val label: String) {
    TALKING_HEAD("Talking Head"),
    VOICEOVER("Voiceover"),
    B_ROLL("B-roll"),
    POV("POV"),
    CINEMATIC("Cinematic"),
    SCREEN_RECORDING("Screen Recording"),
    SLIDE_SHOW("Slide Show"),
    TUTORIAL("Tutorial"),
    REACTION("Reaction"),
    INTERVIEW("Interview"),
    PODCAST("Podcast"),
    FACE_CAMERA("Face Camera"),
    ANIMATION("Animation"),
    TEXT_BASED("Text Based")
}

/**
 * STEP 8 — CONTENT INTENT
 */
enum class ContentIntent(val label: String) {
    EDUCATION("Education"),
    ENTERTAINMENT("Entertainment"),
    SELLING("Selling"),
    AWARENESS("Awareness"),
    TEACHING("Teaching"),
    STORYTELLING("Storytelling"),
    BRANDING("Branding"),
    PERSONAL("Personal"),
    REVIEW("Review"),
    COMPARISON("Comparison"),
    TUTORIAL_INTENT("Tutorial"),
    MOTIVATION("Motivation")
}

/**
 * STEP 7 — ENGINE TOGGLES (MASTER BRAIN CONTROLS ALL ENGINES)
 */
data class EngineToggles(
    val enableOcr: Boolean,
    val enableStudyEngine: Boolean,
    val enableSpeech: Boolean,
    val enableFace: Boolean,
    val enableMotion: Boolean,
    val enableLandscape: Boolean,
    val enableColor: Boolean,
    val enableCamera: Boolean,
    val enableScreenAnalysis: Boolean,
    val enableAudio: Boolean,
    val enableProduct: Boolean,
    val enableBrand: Boolean,
    val enablePrice: Boolean,
    val enableBuyerIntent: Boolean,
    val enableShoppingPersona: Boolean,
    val enableAffiliate: Boolean,
    val enableEmotion: Boolean,
    val enableShoppingModules: Boolean // Master toggle for shopping/price/affiliate
)

/**
 * STEP 1 — SMART ACTIVATION RESULT
 */
data class SmartSceneActivation(
    val isClassified: Boolean,
    val confidencePercent: Int, // Must be >= 70% to classify
    val isCategorySpecificEnabled: Boolean, // Requires >= 95% confidence for category-specific engines
    val activationReason: String,
    val displayText: String
)

/**
 * STEP 10 — AI SUMMARY
 */
data class SceneSummaryReport(
    val primaryCategoryLabel: String?,
    val secondaryCategoryLabel: String?,
    val environmentLabel: String,
    val contentStyleLabel: String,
    val intentLabel: String,
    val overallConfidencePercent: Int,
    val summaryDisplayText: String
)

/**
 * MASTER SCENE CLASSIFICATION V2.0 REPORT
 */
data class SceneClassificationV2Report(
    val activation: SmartSceneActivation,
    val primaryCategory: PrimaryCategory?,
    val secondaryCategory: SecondaryCategory?,
    val environment: EnvironmentType,
    val contentStyle: ContentStyle,
    val contentIntent: ContentIntent,
    val engineToggles: EngineToggles,
    val platformSuitability: List<String>, // Recommended platforms
    val summary: SceneSummaryReport,
    val failSafeActive: Boolean,
    val failSafeNotice: String?
)

object SceneClassificationEngineV2 {

    /**
     * MAIN ENTRY POINT: Analyzes reel to establish Master AI Brain Scene Classification
     */
    fun analyzeReelSceneV2(
        context: Context,
        mediaUri: Uri?,
        durationSec: Float,
        reel: AnalysedReel
    ): SceneClassificationV2Report {

        // Collect signals from existing V2 engines
        val faceReport = FaceEngineV2.analyzeReelFaceEngineV2(context, mediaUri, durationSec, reel)
        val productReport = ProductEngineV2.analyzeReelProductEngineV2(context, mediaUri, durationSec, reel)
        val humanActReport = HumanActivityEngineV2.analyzeReelHumanActivityV2(context, mediaUri, durationSec, reel)
        val ocrReport = OcrEngineV2.analyzeReelOcrEngineV2(context, mediaUri, durationSec, reel)

        val hasFace = faceReport.personDetection.isHumanPresent
        val hasProduct = productReport.activation.isProductPresent
        val isStudyActivity = humanActReport.studyMode.isStudyModeActive
        val isProductActivity = humanActReport.productMode.isProductReviewActive
        val hasOcrText = ocrReport.activation.isTextVisible

        val textCorpus = "${reel.title} ${reel.category} ${reel.aiSummary} ${ocrReport.textBlocks.joinToString(" ") { it.rawText }}".lowercase()

        // Evaluate Category and Confidence
        val (primaryCat, secondaryCat, confidence, intent, env, style) = classifyReelMaster(
            textCorpus = textCorpus,
            hasFace = hasFace,
            hasProduct = hasProduct,
            isStudyActivity = isStudyActivity,
            isProductActivity = isProductActivity,
            hasOcrText = hasOcrText
        )

        // STEP 4 — FAIL SAFE CONFIDENCE CHECK (< 70%)
        if (confidence < 70 || primaryCat == null) {
            return buildDisabledSceneReport(
                confidence = confidence,
                reason = "Confidence ($confidence%) below 70% threshold. Unable to confidently classify this reel.",
                displayText = "Unable to confidently classify this reel."
            )
        }

        // STEP 7 — DECISION ENGINE CONTROLS ALL OTHER ENGINES
        val engineToggles = buildEngineToggles(
            primaryCategory = primaryCat,
            hasFace = hasFace,
            confidence = confidence
        )

        // STEP 9 — PLATFORM SUITABILITY
        val platforms = recommendPlatforms(primaryCat)

        // STEP 10 — AI SUMMARY
        val summary = SceneSummaryReport(
            primaryCategoryLabel = primaryCat.label,
            secondaryCategoryLabel = if (secondaryCat != SecondaryCategory.NONE) secondaryCat.label else null,
            environmentLabel = env.label,
            contentStyleLabel = style.label,
            intentLabel = intent.label,
            overallConfidencePercent = confidence,
            summaryDisplayText = "${primaryCat.label}${if (secondaryCat != SecondaryCategory.NONE) " + ${secondaryCat.label}" else ""} • ${env.label} • ${style.label} (${confidence}% Conf)"
        )

        return SceneClassificationV2Report(
            activation = SmartSceneActivation(
                isClassified = true,
                confidencePercent = confidence,
                isCategorySpecificEnabled = confidence >= 95,
                activationReason = "Reel classified as ${primaryCat.label} with $confidence% confidence based on multimodal visual & textual evidence.",
                displayText = "${primaryCat.label} (${confidence}% Conf)"
            ),
            primaryCategory = primaryCat,
            secondaryCategory = secondaryCat,
            environment = env,
            contentStyle = style,
            contentIntent = intent,
            engineToggles = engineToggles,
            platformSuitability = platforms,
            summary = summary,
            failSafeActive = false,
            failSafeNotice = null
        )
    }

    private fun classifyReelMaster(
        textCorpus: String,
        hasFace: Boolean,
        hasProduct: Boolean,
        isStudyActivity: Boolean,
        isProductActivity: Boolean,
        hasOcrText: Boolean
    ): Tuple6<PrimaryCategory?, SecondaryCategory, Int, ContentIntent, EnvironmentType, ContentStyle> {

        var primary: PrimaryCategory? = null
        var secondary: SecondaryCategory = SecondaryCategory.NONE
        var confidence = 96
        var intent = ContentIntent.EDUCATION
        var env = EnvironmentType.STUDIO
        var style = ContentStyle.TALKING_HEAD

        when {
            isStudyActivity || textCorpus.contains("study") || textCorpus.contains("learn") || textCorpus.contains("book") || textCorpus.contains("exam") || textCorpus.contains("class") -> {
                primary = PrimaryCategory.STUDY
                secondary = if (textCorpus.contains("motive") || textCorpus.contains("mindset")) SecondaryCategory.MOTIVATION else SecondaryCategory.TUTORIAL
                confidence = 96
                intent = ContentIntent.TEACHING
                env = if (textCorpus.contains("library")) EnvironmentType.LIBRARY else EnvironmentType.CLASSROOM
                style = ContentStyle.TALKING_HEAD
            }
            isProductActivity || (hasProduct && (textCorpus.contains("unboxing") || textCorpus.contains("review") || textCorpus.contains("price") || textCorpus.contains("buy"))) -> {
                primary = PrimaryCategory.PRODUCT_REVIEW
                secondary = if (textCorpus.contains("beauty") || textCorpus.contains("skin")) SecondaryCategory.BEAUTY else SecondaryCategory.PRODUCT_REVIEW
                confidence = 95
                intent = ContentIntent.REVIEW
                env = EnvironmentType.STUDIO
                style = ContentStyle.POV
            }
            textCorpus.contains("cook") || textCorpus.contains("recipe") || textCorpus.contains("kitchen") -> {
                primary = PrimaryCategory.COOKING
                secondary = SecondaryCategory.VLOG
                confidence = 94
                intent = ContentIntent.TUTORIAL_INTENT
                env = EnvironmentType.KITCHEN
                style = ContentStyle.POV
            }
            textCorpus.contains("travel") || textCorpus.contains("trip") || textCorpus.contains("tour") -> {
                primary = PrimaryCategory.TRAVEL
                secondary = SecondaryCategory.VLOG
                confidence = 93
                intent = ContentIntent.STORYTELLING
                env = EnvironmentType.OUTDOOR
                style = ContentStyle.CINEMATIC
            }
            textCorpus.contains("podcast") || textCorpus.contains("interview") -> {
                primary = PrimaryCategory.PODCAST
                secondary = SecondaryCategory.BUSINESS
                confidence = 96
                intent = ContentIntent.AWARENESS
                env = EnvironmentType.STUDIO
                style = ContentStyle.PODCAST
            }
            textCorpus.contains("game") || textCorpus.contains("gaming") -> {
                primary = PrimaryCategory.GAMING
                secondary = SecondaryCategory.COMEDY
                confidence = 95
                intent = ContentIntent.ENTERTAINMENT
                env = EnvironmentType.BEDROOM
                style = ContentStyle.SCREEN_RECORDING
            }
            textCorpus.contains("workout") || textCorpus.contains("gym") || textCorpus.contains("fitness") -> {
                primary = PrimaryCategory.FITNESS
                secondary = SecondaryCategory.MOTIVATION
                confidence = 94
                intent = ContentIntent.EDUCATION
                env = EnvironmentType.GYM
                style = ContentStyle.FACE_CAMERA
            }
            hasFace -> {
                primary = PrimaryCategory.EDUCATIONAL
                secondary = SecondaryCategory.TUTORIAL
                confidence = 92
                intent = ContentIntent.EDUCATION
                env = EnvironmentType.STUDIO
                style = ContentStyle.TALKING_HEAD
            }
            hasOcrText -> {
                primary = PrimaryCategory.EDUCATIONAL
                secondary = SecondaryCategory.NONE
                confidence = 88
                intent = ContentIntent.EDUCATION
                env = EnvironmentType.INDOOR
                style = ContentStyle.TEXT_BASED
            }
            else -> {
                // Low confidence signal fallback
                primary = null
                secondary = SecondaryCategory.NONE
                confidence = 55
                intent = ContentIntent.ENTERTAINMENT
                env = EnvironmentType.UNKNOWN
                style = ContentStyle.B_ROLL
            }
        }

        return Tuple6(primary, secondary, confidence, intent, env, style)
    }

    private fun buildEngineToggles(
        primaryCategory: PrimaryCategory,
        hasFace: Boolean,
        confidence: Int
    ): EngineToggles {

        val isHighConf = confidence >= 70

        return when (primaryCategory) {
            PrimaryCategory.EDUCATIONAL, PrimaryCategory.STUDY, PrimaryCategory.TUTORIAL -> EngineToggles(
                enableOcr = isHighConf,
                enableStudyEngine = isHighConf,
                enableSpeech = isHighConf,
                enableFace = hasFace,
                enableMotion = false,
                enableLandscape = false,
                enableColor = true,
                enableCamera = true,
                enableScreenAnalysis = false,
                enableAudio = true,
                enableProduct = false,
                enableBrand = false,
                enablePrice = false,
                enableBuyerIntent = false,
                enableShoppingPersona = false,
                enableAffiliate = false,
                enableEmotion = hasFace,
                enableShoppingModules = false // STRICT RULE: Disable shopping for Study/Educational
            )
            PrimaryCategory.TRAVEL, PrimaryCategory.VLOG, PrimaryCategory.NATURE -> EngineToggles(
                enableOcr = false,
                enableStudyEngine = false,
                enableSpeech = true,
                enableFace = hasFace,
                enableMotion = true,
                enableLandscape = true,
                enableColor = true,
                enableCamera = true,
                enableScreenAnalysis = false,
                enableAudio = true,
                enableProduct = false,
                enableBrand = false,
                enablePrice = false,
                enableBuyerIntent = false,
                enableShoppingPersona = false,
                enableAffiliate = false,
                enableEmotion = true,
                enableShoppingModules = false
            )
            PrimaryCategory.GAMING -> EngineToggles(
                enableOcr = true,
                enableStudyEngine = false,
                enableSpeech = true,
                enableFace = hasFace,
                enableMotion = true,
                enableLandscape = false,
                enableColor = true,
                enableCamera = false,
                enableScreenAnalysis = true,
                enableAudio = true,
                enableProduct = false,
                enableBrand = false,
                enablePrice = false,
                enableBuyerIntent = false,
                enableShoppingPersona = false,
                enableAffiliate = false,
                enableEmotion = false,
                enableShoppingModules = false
            )
            PrimaryCategory.PRODUCT_REVIEW, PrimaryCategory.AFFILIATE, PrimaryCategory.UGC -> EngineToggles(
                enableOcr = true,
                enableStudyEngine = false,
                enableSpeech = true,
                enableFace = hasFace,
                enableMotion = true,
                enableLandscape = false,
                enableColor = true,
                enableCamera = true,
                enableScreenAnalysis = false,
                enableAudio = true,
                enableProduct = true,
                enableBrand = true,
                enablePrice = true,
                enableBuyerIntent = true,
                enableShoppingPersona = true,
                enableAffiliate = true,
                enableEmotion = true,
                enableShoppingModules = true // Enable ALL shopping modules
            )
            PrimaryCategory.PODCAST, PrimaryCategory.INTERVIEW -> EngineToggles(
                enableOcr = false,
                enableStudyEngine = false,
                enableSpeech = true,
                enableFace = hasFace,
                enableMotion = false,
                enableLandscape = false,
                enableColor = true,
                enableCamera = true,
                enableScreenAnalysis = false,
                enableAudio = true,
                enableProduct = false,
                enableBrand = false,
                enablePrice = false,
                enableBuyerIntent = false,
                enableShoppingPersona = false,
                enableAffiliate = false,
                enableEmotion = true,
                enableShoppingModules = false
            )
            else -> EngineToggles(
                enableOcr = true,
                enableStudyEngine = false,
                enableSpeech = true,
                enableFace = hasFace,
                enableMotion = true,
                enableLandscape = false,
                enableColor = true,
                enableCamera = true,
                enableScreenAnalysis = false,
                enableAudio = true,
                enableProduct = false,
                enableBrand = false,
                enablePrice = false,
                enableBuyerIntent = false,
                enableShoppingPersona = false,
                enableAffiliate = false,
                enableEmotion = true,
                enableShoppingModules = false
            )
        }
    }

    private fun recommendPlatforms(category: PrimaryCategory): List<String> {
        return when (category) {
            PrimaryCategory.EDUCATIONAL, PrimaryCategory.STUDY, PrimaryCategory.TUTORIAL -> listOf("YouTube", "Instagram")
            PrimaryCategory.GAMING -> listOf("YouTube", "Twitch")
            PrimaryCategory.PODCAST, PrimaryCategory.INTERVIEW -> listOf("Spotify", "YouTube", "Apple Podcasts")
            PrimaryCategory.PRODUCT_REVIEW, PrimaryCategory.AFFILIATE, PrimaryCategory.UGC -> listOf("Instagram", "Amazon", "Meesho", "YouTube Shorts")
            PrimaryCategory.TRAVEL, PrimaryCategory.VLOG -> listOf("Instagram", "YouTube", "TikTok")
            else -> listOf("Instagram", "YouTube")
        }
    }

    private fun buildDisabledSceneReport(
        confidence: Int,
        reason: String,
        displayText: String
    ): SceneClassificationV2Report {
        return SceneClassificationV2Report(
            activation = SmartSceneActivation(
                isClassified = false,
                confidencePercent = confidence,
                isCategorySpecificEnabled = false,
                activationReason = reason,
                displayText = displayText
            ),
            primaryCategory = null,
            secondaryCategory = SecondaryCategory.NONE,
            environment = EnvironmentType.UNKNOWN,
            contentStyle = ContentStyle.B_ROLL,
            contentIntent = ContentIntent.ENTERTAINMENT,
            engineToggles = EngineToggles(
                enableOcr = false,
                enableStudyEngine = false,
                enableSpeech = false,
                enableFace = false,
                enableMotion = false,
                enableLandscape = false,
                enableColor = false,
                enableCamera = false,
                enableScreenAnalysis = false,
                enableAudio = false,
                enableProduct = false,
                enableBrand = false,
                enablePrice = false,
                enableBuyerIntent = false,
                enableShoppingPersona = false,
                enableAffiliate = false,
                enableEmotion = false,
                enableShoppingModules = false
            ),
            platformSuitability = emptyList(),
            summary = SceneSummaryReport(
                primaryCategoryLabel = null,
                secondaryCategoryLabel = null,
                environmentLabel = "Unknown",
                contentStyleLabel = "Unknown",
                intentLabel = "Unknown",
                overallConfidencePercent = confidence,
                summaryDisplayText = displayText
            ),
            failSafeActive = true,
            failSafeNotice = reason
        )
    }
}

private data class Tuple6<A, B, C, D, E, F>(
    val a: A,
    val b: B,
    val c: C,
    val d: D,
    val e: E,
    val f: F
)
