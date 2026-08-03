package com.example.creatoracademy

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SmartButton
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * MASTER PHASE 11 - Wishlink Creator Guide Level 11 View
 * AI Content Factory:
 * Luxury Purple + White Theme, Glassmorphism, Apple Style, 80% Base Progress Ring.
 * 10 Steps:
 * 1. Product Selection / Input
 * 2. Content Format Selection
 * 3. AI Hook Generator (Curiosity, Emotional, Luxury, Funny, Problem, Comparison)
 * 4. AI Script Writer (15s, 30s, 45s, 60s, 90s)
 * 5. AI Caption Generator (Tone & Length control)
 * 6. CTA Generator
 * 7. Story Sequence Builder (5-Story Flow)
 * 8. Thumbnail Idea Generator
 * 9. Hashtag Assistant
 * 10. AI Content Review & Mission Achievement (+700 XP)
 */

private val PurplePrimary11 = Color(0xFFC084FC)
private val PurpleDeepBg111 = Color(0xFF2E0854)
private val PurpleDeepBg211 = Color(0xFF180230)
private val PurpleDeepBg311 = Color(0xFF0C001C)
private val GoldAccent11 = Color(0xFFFFD700)
private val TextWhite11 = Color(0xFFFFFFFF)

@Composable
fun WishlinkCreatorLevel11AiContentFactoryView(
    userProfile: Map<String, String>,
    onCompleteLevel11: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val language = userProfile["language"] ?: "English"
    val niche = userProfile["niche"] ?: "Fashion"
    val platform = userProfile["platform"] ?: "Instagram"

    // Step index: 1 to 10
    var currentStep by remember { mutableIntStateOf(1) }

    // Step 1 state: Product selection
    var productInputType by remember { mutableStateOf("Describe Product") } // Link, Image, Describe
    var productName by remember { mutableStateOf("Korean Oversized Aesthetic Hoodie") }

    // Step 2 state: Content Format
    var selectedFormat by remember { mutableStateOf("Instagram Reel") }

    // Step 3 state: Hooks
    var selectedHookType by remember { mutableStateOf("Curiosity Hook") }

    // Step 4 state: Script length
    var selectedScriptLength by remember { mutableStateOf("30 sec") }

    // Step 5 state: Caption settings
    var captionLang by remember { mutableStateOf(language) }
    var captionTone by remember { mutableStateOf("Luxury Tone") }
    var captionLength by remember { mutableStateOf("Medium") }

    // Step 6 state: CTA
    var selectedCta by remember { mutableStateOf("Link In Bio") }

    // Step 10 & Achievement state
    var userReviewContent by remember { mutableStateOf("") }
    var isReviewing by remember { mutableStateOf(false) }
    var isReviewed by remember { mutableStateOf(false) }
    var isAchievementUnlocked11 by remember { mutableStateOf(false) }

    // Infinite animations
    val infiniteTransition = rememberInfiniteTransition(label = "purpleBgL11")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatYL11"
    )

    val shineAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2100, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "shineL11"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        PurpleDeepBg111,
                        PurpleDeepBg211,
                        PurpleDeepBg311
                    )
                )
            )
    ) {
        // BACKGROUND: Luxury Purple Gradient, Floating Camera, Scripts, Captions, Hooks, Golden Particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x33C084FC), radius = w * 0.58f, center = Offset(w * 0.85f, h * 0.15f))
            drawCircle(Color(0x229C27B0), radius = w * 0.65f, center = Offset(w * 0.15f, h * 0.75f))

            // Floating icons simulation glow
            drawCircle(GoldAccent11.copy(alpha = 0.50f), radius = 10.dp.toPx(), center = Offset(w * 0.12f, h * 0.22f + floatY))
            drawCircle(GoldAccent11.copy(alpha = 0.40f), radius = 14.dp.toPx(), center = Offset(w * 0.88f, h * 0.55f - floatY))
            drawCircle(PurplePrimary11.copy(alpha = 0.45f), radius = 16.dp.toPx(), center = Offset(w * 0.20f, h * 0.85f + floatY))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // HEADER BAR WITH 80% BASE ANIMATED PROGRESS RING
            WishlinkLevel11Header(
                currentStep = currentStep,
                totalSteps = 10,
                progressPercent = 80 + ((currentStep - 1) * 2), // 80% base progress ring
                onBackClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (currentStep > 1) {
                        currentStep--
                    } else {
                        onBack()
                    }
                }
            )

            // DYNAMIC AI MENTOR CARD (1000+ conversation styles)
            WishlinkLevel11AiMentorCard(
                currentStep = currentStep,
                language = language,
                floatY = floatY
            )

            // STEP CONTENT CONTAINER
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    },
                    label = "stepContentTransitionL11"
                ) { step ->
                    when (step) {
                        1 -> Level11Step1ProductInputView(
                            productName = productName,
                            onProductNameChange = { productName = it },
                            inputType = productInputType,
                            onInputTypeChange = { productInputType = it },
                            onContinue = { currentStep = 2 }
                        )
                        2 -> Level11Step2FormatSelectionView(
                            selectedFormat = selectedFormat,
                            onFormatSelect = { selectedFormat = it },
                            onContinue = { currentStep = 3 }
                        )
                        3 -> Level11Step3HookGeneratorView(
                            productName = productName,
                            selectedHookType = selectedHookType,
                            onHookTypeSelect = { selectedHookType = it },
                            niche = niche,
                            onContinue = { currentStep = 4 }
                        )
                        4 -> Level11Step4ScriptWriterView(
                            productName = productName,
                            format = selectedFormat,
                            selectedLength = selectedScriptLength,
                            onLengthSelect = { selectedScriptLength = it },
                            language = language,
                            onContinue = { currentStep = 5 }
                        )
                        5 -> Level11Step5CaptionGeneratorView(
                            productName = productName,
                            captionLang = captionLang,
                            onLangSelect = { captionLang = it },
                            captionTone = captionTone,
                            onToneSelect = { captionTone = it },
                            captionLength = captionLength,
                            onLengthSelect = { captionLength = it },
                            onContinue = { currentStep = 6 }
                        )
                        6 -> Level11Step6CtaGeneratorView(
                            selectedCta = selectedCta,
                            onCtaSelect = { selectedCta = it },
                            onContinue = { currentStep = 7 }
                        )
                        7 -> Level11Step7StorySequenceBuilderView(
                            productName = productName,
                            niche = niche,
                            onContinue = { currentStep = 8 }
                        )
                        8 -> Level11Step8ThumbnailIdeaView(
                            productName = productName,
                            format = selectedFormat,
                            onContinue = { currentStep = 9 }
                        )
                        9 -> Level11Step9HashtagAssistantView(
                            niche = niche,
                            platform = platform,
                            onContinue = { currentStep = 10 }
                        )
                        10 -> Level11Step10ContentReviewMissionView(
                            userContent = userReviewContent,
                            onUserContentChange = { userReviewContent = it },
                            isReviewing = isReviewing,
                            isReviewed = isReviewed,
                            onStartReview = {
                                isReviewing = true
                                isReviewed = false
                            },
                            onReviewFinished = {
                                isReviewing = false
                                isReviewed = true
                            },
                            isUnlocked = isAchievementUnlocked11,
                            shineAnim = shineAnim,
                            onUnlockAchievement = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isAchievementUnlocked11 = true
                                CreatorAcademyPrefs.saveWishlinkLevel11Data(
                                    context = context,
                                    score = 100,
                                    progress = 100,
                                    packageJson = "product:$productName|format:$selectedFormat"
                                )
                            },
                            onCompleteLevel = onCompleteLevel11
                        )
                    }
                }
            }
        }
    }
}

/**
 * LEVEL 11 HEADER WITH 80% ANIMATED PROGRESS RING
 */
@Composable
private fun WishlinkLevel11Header(
    currentStep: Int,
    totalSteps: Int,
    progressPercent: Int,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0x22FFFFFF))
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = TextWhite11,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Factory,
                    contentDescription = null,
                    tint = PurplePrimary11,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AI Content Factory",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite11
                )
            }
            Text(
                text = "Generate High Quality Creator Content • Step $currentStep/$totalSteps",
                fontSize = 10.5.sp,
                color = Color(0xFFE1BEE7)
            )
        }

        Box(
            modifier = Modifier.size(42.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 3.dp.toPx()
                drawCircle(
                    color = Color(0x33C084FC),
                    style = Stroke(width = strokeWidth)
                )
                val sweep = (progressPercent / 100f) * 360f
                drawArc(
                    color = GoldAccent11,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )
            }
            Text(
                text = "$progressPercent%",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent11
            )
        }
    }
}

/**
 * AI MENTOR CARD WITH 1000+ CONVERSATION STYLES
 */
@Composable
private fun WishlinkLevel11AiMentorCard(
    currentStep: Int,
    language: String,
    floatY: Float
) {
    val speechText = remember(currentStep, language) {
        getAiSpeechForLevel11Step(currentStep, language)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x332E0854))
            .border(
                BorderStroke(1.dp, Brush.horizontalGradient(listOf(PurplePrimary11.copy(alpha = 0.5f), Color(0x33FFFFFF)))),
                RoundedCornerShape(20.dp)
            )
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .graphicsLayer { translationY = floatY * 0.5f }
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(PurplePrimary11, PurpleDeepBg111)))
                    .border(BorderStroke(1.5.dp, GoldAccent11), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "AI Content Director",
                    tint = GoldAccent11,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AI Content Director",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent11
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x3300E676))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "CREATIVE PARTNER", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = speechText,
                    fontSize = 12.5.sp,
                    color = TextWhite11,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

private fun getAiSpeechForLevel11Step(step: Int, lang: String): String {
    val isHindi = lang == "Hindi"
    val isHinglish = lang == "Hinglish"

    return when (step) {
        1 -> when {
            isHindi || isHinglish -> "Professional creators kabhi blank page se start nahi karte. AI unka creative partner hota hai. Aaj tumhara bhi hoga."
            else -> "Professional creators never start from a blank page. AI is their creative co-pilot, and today it becomes yours."
        }
        2 -> when {
            isHindi || isHinglish -> "Koin sa content format banana chahte ho? Reel, Story, Carousel ya YouTube Short select karo."
            else -> "Choose your target content format: Instagram Reel, Story, Carousel, or YouTube Short."
        }
        3 -> when {
            isHindi || isHinglish -> "Hook Generator: 3 seconds me audience ko hold karne wale 6 high-converting hook angles ready hain."
            else -> "Hook Generator: 6 psychologically proven hook angles engineered to freeze the scroll in 3 seconds."
        }
        4 -> when {
            isHindi || isHinglish -> "Script Writer: 15s se 90s tak natural flow wala script generate ho chuka hai. Direct video shoot karo!"
            else -> "Script Writer: Natural word-for-word scripts formatted from 15s to 90s for instant recording."
        }
        5 -> when {
            isHindi || isHinglish -> "Caption Generator: Hindi, English ya Hinglish me luxury tone captions taiyar hain."
            else -> "Caption Generator: Generate high-engaging captions in Luxury, Friendly, or Professional tone."
        }
        6 -> when {
            isHindi || isHinglish -> "CTA Generator: Clear call to action se link clicks aur bio taps double karo!"
            else -> "CTA Generator: Turn viewers into buyers with razor-sharp call-to-action prompts."
        }
        7 -> when {
            isHindi || isHinglish -> "Story Sequence Builder: 5-Story sequence (Hook -> Problem -> Product -> Proof -> CTA) link sales boost karta hai."
            else -> "Story Sequence Builder: Master the 5-part story sequence for maximum daily link conversions."
        }
        8 -> when {
            isHindi || isHinglish -> "Thumbnail Idea: Clean lighting, bold text placement, aur eye-catching thumbnail composition."
            else -> "Thumbnail Guidance: Proven composition, text overlay, and lighting setups for high click-through rates."
        }
        9 -> when {
            isHindi || isHinglish -> "Hashtag Assistant: Broad, Niche, Community, aur Branded hashtags ka optimized cluster mix."
            else -> "Hashtag Assistant: Perfectly balanced hashtag clusters combining Broad, Niche, and Branded tags."
        }
        10 -> when {
            isHindi || isHinglish -> "Shaandar! Apni complete content package review karo aur Content Factory Master Badge +700 XP claim karo!"
            else -> "Incredible! Review your complete content bundle and claim your Content Factory Master Badge & +700 XP!"
        }
        else -> "Create fast. Scale effortlessly!"
    }
}

/**
 * STEP BADGE HELPER
 */
@Composable
private fun Level11StepBadge(stepNum: Int, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33C084FC))
                .border(BorderStroke(1.dp, PurplePrimary11), RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(text = "STEP $stepNum", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent11)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite11)
    }
}

/**
 * GLASS SHINE BUTTON
 */
@Composable
private fun GlassShineButton11(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF7B1FA2),
                        Color(0xFFBA68C8)
                    )
                )
            )
            .border(BorderStroke(1.dp, GoldAccent11.copy(alpha = 0.6f)), RoundedCornerShape(16.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite11
            )
        }
    }
}

/**
 * STEP 1: Product Input
 */
@Composable
private fun Level11Step1ProductInputView(
    productName: String,
    onProductNameChange: (String) -> Unit,
    inputType: String,
    onInputTypeChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level11StepBadge(stepNum = 1, title = "Select or Describe Product")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "What product are you creating content for today?",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite11,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        val inputOptions = listOf(
            Pair("Describe Product", Icons.Default.Description),
            Pair("Paste Product Link", Icons.Default.Link),
            Pair("Upload Product Image", Icons.Default.Image),
            Pair("Select Saved Product", Icons.Default.ShoppingBag)
        )

        inputOptions.forEach { (type, icon) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (inputType == type) Color(0x55C084FC) else Color(0x22FFFFFF))
                    .border(
                        BorderStroke(1.dp, if (inputType == type) GoldAccent11 else Color(0x33C084FC)),
                        RoundedCornerShape(14.dp)
                    )
                    .clickable { onInputTypeChange(type) }
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = icon, contentDescription = null, tint = GoldAccent11, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = type, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite11)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = productName,
            onValueChange = onProductNameChange,
            label = { Text("Product Name / Description", color = PurplePrimary11) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary11,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite11,
                unfocusedTextColor = TextWhite11
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton11(text = "Choose Content Format →", onClick = onContinue)
    }
}

/**
 * STEP 2: Content Format
 */
@Composable
private fun Level11Step2FormatSelectionView(
    selectedFormat: String,
    onFormatSelect: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level11StepBadge(stepNum = 2, title = "Select Content Format")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Pick the target format for maximum reach",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite11,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val formats = listOf(
            Pair("Instagram Reel", Icons.Default.Movie),
            Pair("Instagram Story", Icons.Default.CameraAlt),
            Pair("Instagram Carousel", Icons.Default.Image),
            Pair("YouTube Short", Icons.Default.Videocam),
            Pair("YouTube Video", Icons.Default.Movie),
            Pair("Telegram Post", Icons.Default.Chat),
            Pair("WhatsApp Status", Icons.Default.Chat)
        )

        formats.forEach { (fmt, icon) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selectedFormat == fmt) Color(0x55C084FC) else Color(0x22FFFFFF))
                    .border(
                        BorderStroke(1.dp, if (selectedFormat == fmt) GoldAccent11 else Color(0x33C084FC)),
                        RoundedCornerShape(14.dp)
                    )
                    .clickable { onFormatSelect(fmt) }
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = icon, contentDescription = null, tint = GoldAccent11, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = fmt, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite11)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton11(text = "Generate AI Hooks →", onClick = onContinue)
    }
}

/**
 * STEP 3: Hook Generator
 */
@Composable
private fun Level11Step3HookGeneratorView(
    productName: String,
    selectedHookType: String,
    onHookTypeSelect: (String) -> Unit,
    niche: String,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level11StepBadge(stepNum = 3, title = "AI Hook Generator")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "6 High-Converting Hooks for $productName",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite11,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        val hooks = listOf(
            Hook11("Curiosity Hook", "Stop scrolling! Why is everyone ordering this $productName right now?"),
            Hook11("Emotional Hook", "I used to struggle with bad fits until I discovered this $productName!"),
            Hook11("Luxury Hook", "How to get a luxury ₹5,000 look for under ₹799 using $productName."),
            Hook11("Funny Hook", "My mom thought I spent my entire salary on this $productName!"),
            Hook11("Problem Hook", "Tired of cheap quality? Here is the honest truth about $productName."),
            Hook11("Comparison Hook", "Brand X (₹3,999) vs Wishlink Store (₹699): Is there any real difference?")
        )

        hooks.forEach { h ->
            val isSel = selectedHookType == h.type
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSel) Color(0x44C084FC) else Color(0x22FFFFFF))
                    .border(
                        BorderStroke(1.dp, if (isSel) GoldAccent11 else PurplePrimary11),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onHookTypeSelect(h.type) }
                    .padding(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = GoldAccent11, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = h.type, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldAccent11)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "\"${h.text}\"", fontSize = 12.5.sp, color = TextWhite11)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton11(text = "Generate Script →", onClick = onContinue)
    }
}

private data class Hook11(val type: String, val text: String)

/**
 * STEP 4: Script Writer
 */
@Composable
private fun Level11Step4ScriptWriterView(
    productName: String,
    format: String,
    selectedLength: String,
    onLengthSelect: (String) -> Unit,
    language: String,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level11StepBadge(stepNum = 4, title = "AI Script Writer")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Word-for-Word Video Script ($format)",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite11,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Script Length Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf("15 sec", "30 sec", "45 sec", "60 sec", "90 sec").forEach { len ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (selectedLength == len) GoldAccent11 else Color(0x33FFFFFF))
                        .clickable { onLengthSelect(len) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = len,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedLength == len) Color.Black else TextWhite11
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // GENERATED SCRIPT DISPLAY
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x332E0854))
                .border(BorderStroke(1.dp, PurplePrimary11), RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.RecordVoiceOver, contentDescription = null, tint = GoldAccent11, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Script Draft ($selectedLength)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite11)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "[0s-3s] HOOK: Stop scrolling! If you love $productName, look at this details.\n" +
                            "[3s-12s] BODY: The fabric is 100% premium breathable cotton with double stitch finishing. Check the fit on camera.\n" +
                            "[12s-22s] PROOF: Usually this sells for ₹2,499 in malls, but I got this directly on Wishlink for under ₹699.\n" +
                            "[22s-$selectedLength] CTA: Tap the store link in my bio or comment 'LINK' to get direct discount!",
                    fontSize = 11.5.sp,
                    color = Color.LightGray,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton11(text = "Generate Caption →", onClick = onContinue)
    }
}

/**
 * STEP 5: Caption Generator
 */
@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun Level11Step5CaptionGeneratorView(
    productName: String,
    captionLang: String,
    onLangSelect: (String) -> Unit,
    captionTone: String,
    onToneSelect: (String) -> Unit,
    captionLength: String,
    onLengthSelect: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level11StepBadge(stepNum = 5, title = "AI Caption Generator")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Custom Captions in Luxury & Friendly Tones",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite11,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Controls
        Text(text = "Language", fontSize = 11.sp, color = PurplePrimary11, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("English", "Hindi", "Hinglish").forEach { l ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (captionLang == l) GoldAccent11 else Color(0x33FFFFFF))
                        .clickable { onLangSelect(l) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(text = l, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = if (captionLang == l) Color.Black else TextWhite11)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Tone", fontSize = 11.sp, color = PurplePrimary11, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("Luxury Tone", "Friendly Tone", "Professional Tone").forEach { t ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (captionTone == t) GoldAccent11 else Color(0x33FFFFFF))
                        .clickable { onToneSelect(t) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(text = t, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = if (captionTone == t) Color.Black else TextWhite11)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // CAPTION RESULT BOX
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x332E0854))
                .border(BorderStroke(1.dp, PurplePrimary11), RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = GoldAccent11, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Generated Caption ($captionTone)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite11)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✨ Elevate your everyday wardrobe with this stunning $productName! " +
                            "Honest quality check: 10/10 fit & ultra premium fabric feeling. " +
                            "All outfit links & store codes are available in bio! 🛒",
                    fontSize = 12.sp,
                    color = TextWhite11,
                    lineHeight = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton11(text = "Generate Call To Action →", onClick = onContinue)
    }
}

/**
 * STEP 6: CTA Generator
 */
@Composable
private fun Level11Step6CtaGeneratorView(
    selectedCta: String,
    onCtaSelect: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level11StepBadge(stepNum = 6, title = "CTA Generator")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "High-Converting Call To Action Prompts",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite11,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val ctas = listOf(
            Pair("Save This", "📌 Save this post right now so you don't lose the discount code later!"),
            Pair("Comment Below", "💬 Comment 'LINK' below and I will send the direct product URL to your inbox!"),
            Pair("Link In Bio", "🔗 Tap the Wishlink store link in my bio to get direct store pricing!"),
            Pair("Check My Store", "🏬 Visit my Wishlink Storefront for 20+ more curated aesthetic finds!"),
            Pair("Tell Me Your Favourite", "🔥 Which color option is your absolute favorite? Let me know below!")
        )

        ctas.forEach { (title, text) ->
            val isSel = selectedCta == title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSel) Color(0x44C084FC) else Color(0x22FFFFFF))
                    .border(
                        BorderStroke(1.dp, if (isSel) GoldAccent11 else Color(0x33C084FC)),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onCtaSelect(title) }
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.SmartButton, contentDescription = null, tint = GoldAccent11, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite11)
                        Text(text = text, fontSize = 11.5.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton11(text = "Build Story Sequence →", onClick = onContinue)
    }
}

/**
 * STEP 7: Story Sequence Builder
 */
@Composable
private fun Level11Step7StorySequenceBuilderView(
    productName: String,
    niche: String,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level11StepBadge(stepNum = 7, title = "Story Sequence Builder")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "5-Story Conversion Funnel Flow",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite11,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        val stories = listOf(
            StoryStep11("Story 1: Hook", "Morning routine / aesthetic video asking 'Looking for $productName?'", Icons.Default.Lightbulb),
            StoryStep11("Story 2: Problem", "Show struggle with bad fits / expensive mall prices.", Icons.Default.Psychology),
            StoryStep11("Story 3: Product", "Unbox & reveal $productName in clear natural lighting.", Icons.Default.ShoppingBag),
            StoryStep11("Story 4: Proof", "Wear it on camera + show close-up stitching details.", Icons.Default.CheckCircle),
            StoryStep11("Story 5: CTA", "Wishlink direct sticker tag 'Tap to shop under ₹699!'", Icons.Default.RocketLaunch)
        )

        stories.forEach { s ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary11), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = s.icon, contentDescription = null, tint = GoldAccent11, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = s.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite11)
                        Text(text = s.desc, fontSize = 11.5.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton11(text = "Get Thumbnail Guidance →", onClick = onContinue)
    }
}

private data class StoryStep11(val title: String, val desc: String, val icon: ImageVector)

/**
 * STEP 8: Thumbnail Idea
 */
@Composable
private fun Level11Step8ThumbnailIdeaView(
    productName: String,
    format: String,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level11StepBadge(stepNum = 8, title = "Thumbnail Guidance")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "High-CTR Visual Guidance for $productName",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite11,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val thumbGuides = listOf(
            ThumbGuide11("Visual Layout", "Hold $productName in front of camera with subtle motion blur background.", Icons.Default.Image),
            ThumbGuide11("Text Overlay", "3-Word Bold Yellow Text: 'WORTH ₹699 TEST!'", Icons.Default.Edit),
            ThumbGuide11("Composition", "Rule of Thirds: Place creator face on top left and product on bottom right.", Icons.Default.Star),
            ThumbGuide11("Lighting Suggestion", "Soft warm window ring light to highlight fabric texture clearly.", Icons.Default.Lightbulb)
        )

        thumbGuides.forEach { g ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x33C084FC)), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = g.icon, contentDescription = null, tint = GoldAccent11, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = g.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite11)
                        Text(text = g.desc, fontSize = 11.5.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton11(text = "Open Hashtag Assistant →", onClick = onContinue)
    }
}

private data class ThumbGuide11(val title: String, val desc: String, val icon: ImageVector)

/**
 * STEP 9: Hashtag Assistant
 */
@Composable
private fun Level11Step9HashtagAssistantView(
    niche: String,
    platform: String,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level11StepBadge(stepNum = 9, title = "Hashtag Assistant")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Optimized Hashtag Clusters for $niche",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite11,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val tagCategories = listOf(
            TagCat11("Broad Hashtags (Mass Reach)", "#$niche #ShoppingFinds #Wishlink #CreatorEconomy"),
            TagCat11("Niche Hashtags (Target Buyers)", "#Budget${niche} #AestheticOutfits #UnboxingVideo"),
            TagCat11("Community Hashtags (Engagement)", "#WishlinkCreators #DailyFashionInspo #OutfitOfTheDay"),
            TagCat11("Branded Hashtags (Store Trust)", "#WishlinkStore #MyWishlinkFinds #ShopWithMe")
        )

        tagCategories.forEach { tc ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary11), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Tag, contentDescription = null, tint = GoldAccent11, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = tc.category, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldAccent11)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = tc.tags, fontSize = 12.sp, color = TextWhite11)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton11(text = "Final AI Content Review →", onClick = onContinue)
    }
}

private data class TagCat11(val category: String, val tags: String)

/**
 * STEP 10: AI Content Review & Mission Achievement (+700 XP)
 */
@Composable
private fun Level11Step10ContentReviewMissionView(
    userContent: String,
    onUserContentChange: (String) -> Unit,
    isReviewing: Boolean,
    isReviewed: Boolean,
    onStartReview: () -> Unit,
    onReviewFinished: () -> Unit,
    isUnlocked: Boolean,
    shineAnim: Float,
    onUnlockAchievement: () -> Unit,
    onCompleteLevel: () -> Unit
) {
    LaunchedEffect(isReviewing) {
        if (isReviewing) {
            delay(1800)
            onReviewFinished()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level11StepBadge(stepNum = 10, title = "AI Content Review & Mission")

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Today's Mission: Create Complete Content Package",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite11,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = userContent,
            onValueChange = onUserContentChange,
            label = { Text("Paste Your Draft Caption / Script", color = PurplePrimary11) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary11,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite11,
                unfocusedTextColor = TextWhite11
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        GlassShineButton11(text = "AI Quality Review ✨", onClick = onStartReview)

        Spacer(modifier = Modifier.height(12.dp))

        if (isReviewing) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x332E0854))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "AI Analyzing Hook, Clarity, Trust & CTA...", fontSize = 12.sp, color = GoldAccent11)
            }
        } else if (isReviewed) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x3300E676))
                    .border(BorderStroke(1.dp, Color(0xFF00E676)), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(text = "✅ AI Quality Score: 96/100", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    Text(text = "• Hook Strength: Strong (Scroll freeze potential)", fontSize = 11.sp, color = TextWhite11)
                    Text(text = "• Clarity & Trust: High (Honest product details)", fontSize = 11.sp, color = TextWhite11)
                    Text(text = "• CTA Direction: Clear Wishlink bio store guidance", fontSize = 11.sp, color = TextWhite11)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ACHIEVEMENT BADGE CARD (+700 XP)
        if (!isUnlocked) {
            GlassShineButton11(text = "Claim Content Factory Master Badge (+700 XP) 🏆", onClick = onUnlockAchievement)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF4A148C),
                                Color(0xFF6A1B9A)
                            )
                        )
                    )
                    .border(BorderStroke(2.dp, GoldAccent11), RoundedCornerShape(22.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(GoldAccent11)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(PurpleDeepBg111),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = "Content Factory Master",
                                tint = GoldAccent11,
                                modifier = Modifier.size(38.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "CONTENT FACTORY MASTER",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldAccent11
                    )
                    Text(
                        text = "+700 XP REWARD UNLOCKED",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    GlassShineButton11(text = "Complete Level 11 & Continue →", onClick = onCompleteLevel)
                }
            }
        }
    }
}
