package com.example.creatoracademy

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * MASTER PHASE 1 - Wishlink Creator Guide Level 1 View
 * Redesign: Luxury Purple + White Glassmorphism Theme, Floating Particles, Swipe Onboarding, Language Selection,
 * Single-Question Personalization, AI Analysis & Personalized Learning Roadmap.
 */

private val PurplePrimary = Color(0xFFB388FF)
private val PurpleGlow = Color(0x33B388FF)
private val PurpleDeepBg1 = Color(0xFF280047)
private val PurpleDeepBg2 = Color(0xFF140026)
private val PurpleDeepBg3 = Color(0xFF080012)
private val GoldAccent = Color(0xFFFFD700)
private val TextWhite = Color(0xFFFFFFFF)

@Composable
fun WishlinkCreatorLevel1WelcomeView(
    onCompleteLevel1: (profile: Map<String, String>) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Check if onboarding was already completed
    val onboardingAlreadyDone = remember { CreatorAcademyPrefs.isWishlinkOnboardingDone(context) }
    val savedLanguage = remember { CreatorAcademyPrefs.getWishlinkLanguage(context) }

    // Internal State Machine: ONBOARDING -> LANGUAGE -> Q1 -> Q2 -> Q3 -> Q4 -> Q5 -> AI_ANALYSIS -> ROADMAP
    var stepState by remember {
        mutableStateOf(
            when {
                !onboardingAlreadyDone -> "ONBOARDING"
                savedLanguage.isBlank() -> "LANGUAGE"
                else -> "Q1"
            }
        )
    }

    // Onboarding Cards index (0 to 3)
    var onboardingIndex by remember { mutableIntStateOf(0) }

    // User Selection States
    var selectedLanguage by remember { mutableStateOf(savedLanguage) }
    var heardBefore by remember { mutableStateOf("") }
    var hasAccount by remember { mutableStateOf("") }
    var selectedPlatform by remember { mutableStateOf("") }
    var selectedNiche by remember { mutableStateOf("") }
    var selectedGoal by remember { mutableStateOf("") }
    var customGoalInput by remember { mutableStateOf("") }

    // Calculated AI Learning Level & Mission
    var learningLevel by remember { mutableStateOf("Beginner") }
    var firstMissionTitle by remember { mutableStateOf("Complete Wishlink Account Setup") }
    var estimatedTotalMinutes by remember { mutableStateOf("15 Mins / Day • 5 Days Masterclass") }

    // Floating animation
    val infiniteTransition = rememberInfiniteTransition(label = "purpleBgAnim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatY"
    )

    val shineAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "shine"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        PurpleDeepBg1,
                        PurpleDeepBg2,
                        PurpleDeepBg3
                    )
                )
            )
    ) {
        // BACKGROUND: Luxury Purple Gradient, Floating Link Icons 🔗, Shopping Bags 🛍️, Gift Boxes 🎁, Golden Particles ✨
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Soft Purple Radial Glow
            drawCircle(
                color = Color(0x33B388FF),
                radius = w * 0.5f,
                center = Offset(w * 0.25f, h * 0.2f)
            )
            drawCircle(
                color = Color(0x228E24AA),
                radius = w * 0.55f,
                center = Offset(w * 0.75f, h * 0.75f)
            )

            // Golden Particles
            drawCircle(GoldAccent.copy(alpha = 0.45f), radius = 10.dp.toPx(), center = Offset(w * 0.15f, h * 0.16f + floatY * 2f))
            drawCircle(GoldAccent.copy(alpha = 0.35f), radius = 14.dp.toPx(), center = Offset(w * 0.85f, h * 0.3f - floatY * 2.2f))
            drawCircle(PurplePrimary.copy(alpha = 0.4f), radius = 16.dp.toPx(), center = Offset(w * 0.22f, h * 0.68f + floatY * 2.5f))
            drawCircle(GoldAccent.copy(alpha = 0.3f), radius = 12.dp.toPx(), center = Offset(w * 0.8f, h * 0.84f - floatY * 2.8f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // TOP BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        when (stepState) {
                            "ONBOARDING" -> if (onboardingIndex > 0) onboardingIndex-- else onBack()
                            "LANGUAGE" -> stepState = if (!onboardingAlreadyDone) "ONBOARDING" else "Q1"
                            "Q1" -> stepState = "LANGUAGE"
                            "Q2" -> stepState = "Q1"
                            "Q3" -> stepState = "Q2"
                            "Q4" -> stepState = "Q3"
                            "Q5" -> stepState = "Q4"
                            "ROADMAP" -> stepState = "Q5"
                            else -> onBack()
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Link,
                        contentDescription = "Wishlink",
                        tint = PurplePrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Wishlink Creator Guide",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }

                // Right Top Action (Skip during Onboarding)
                if (stepState == "ONBOARDING") {
                    TextButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            CreatorAcademyPrefs.setWishlinkOnboardingDone(context, true)
                            stepState = "LANGUAGE"
                        }
                    ) {
                        Text(
                            text = "Skip",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PurplePrimary
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }

            // CONTENT AREA
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = stepState,
                    transitionSpec = {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    },
                    label = "stepTransition"
                ) { targetStep ->
                    when (targetStep) {
                        "ONBOARDING" -> {
                            WishlinkOnboardingSwipeView(
                                currentIndex = onboardingIndex,
                                onIndexChange = { newIdx ->
                                    onboardingIndex = newIdx
                                },
                                onFinishOnboarding = {
                                    CreatorAcademyPrefs.setWishlinkOnboardingDone(context, true)
                                    stepState = "LANGUAGE"
                                }
                            )
                        }

                        "LANGUAGE" -> {
                            WishlinkLanguageSelectionStep(
                                selectedLang = selectedLanguage,
                                onSelectLang = { lang ->
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedLanguage = lang
                                    CreatorAcademyPrefs.setWishlinkLanguage(context, lang)
                                    stepState = "Q1"
                                }
                            )
                        }

                        "Q1" -> {
                            SingleQuestionCard(
                                questionNumber = 1,
                                totalQuestions = 5,
                                title = "Have you heard about Wishlink before?",
                                subtitle = "Select your familiarity level with Wishlink Creator program.",
                                options = listOf(
                                    QuestionOption("Yes", "I already know what Wishlink is.", "✨"),
                                    QuestionOption("No", "I am completely new to Wishlink.", "🌱")
                                ),
                                selectedValue = heardBefore,
                                onOptionSelect = { opt ->
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    heardBefore = opt
                                    stepState = "Q2"
                                }
                            )
                        }

                        "Q2" -> {
                            SingleQuestionCard(
                                questionNumber = 2,
                                totalQuestions = 5,
                                title = "Do you already have a Wishlink account?",
                                subtitle = "This helps us customize your account setup steps.",
                                options = listOf(
                                    QuestionOption("Yes", "I have an active Wishlink account.", "📱"),
                                    QuestionOption("No", "I need to register as a new creator.", "🚀")
                                ),
                                selectedValue = hasAccount,
                                onOptionSelect = { opt ->
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    hasAccount = opt
                                    stepState = "Q3"
                                }
                            )
                        }

                        "Q3" -> {
                            SingleQuestionCard(
                                questionNumber = 3,
                                totalQuestions = 5,
                                title = "Which platform do you create content on?",
                                subtitle = "Choose your primary creator platform for affiliate links.",
                                options = listOf(
                                    QuestionOption("Instagram", "Reels, Stories, Bio Links & Auto-DMs", "📸"),
                                    QuestionOption("YouTube", "Shorts, Description Links & Community", "▶️"),
                                    QuestionOption("Facebook", "Pages, Groups & Video Reels", "📘"),
                                    QuestionOption("Telegram", "Deals Channel & Broadcast Group", "✈️"),
                                    QuestionOption("Multiple", "Cross-platform creator presence", "🌟")
                                ),
                                selectedValue = selectedPlatform,
                                onOptionSelect = { opt ->
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedPlatform = opt
                                    stepState = "Q4"
                                }
                            )
                        }

                        "Q4" -> {
                            SingleQuestionCard(
                                questionNumber = 4,
                                totalQuestions = 5,
                                title = "What type of content do you make?",
                                subtitle = "Select your primary content niche for product recommendations.",
                                options = listOf(
                                    QuestionOption("Fashion", "Outfits, try-on hauls & styling tips", "👗"),
                                    QuestionOption("Beauty", "Skincare, makeup & product reviews", "💄"),
                                    QuestionOption("Tech", "Gadgets, unboxing & phone reviews", "🎧"),
                                    QuestionOption("Lifestyle", "Daily vlogs, home finds & aesthetic gear", "🌿"),
                                    QuestionOption("Home", "Decor, kitchen tools & organization", "🏠"),
                                    QuestionOption("Fitness", "Gym wear, supplements & workout gear", "🏋️"),
                                    QuestionOption("Gaming", "Setup gear, gaming wear & accessories", "🎮"),
                                    QuestionOption("Education", "Study setups, books & productivity", "📚"),
                                    QuestionOption("Other", "Unique recommendation content", "💡")
                                ),
                                selectedValue = selectedNiche,
                                onOptionSelect = { opt ->
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedNiche = opt
                                    stepState = "Q5"
                                }
                            )
                        }

                        "Q5" -> {
                            SingleQuestionCardWithCustom(
                                questionNumber = 5,
                                totalQuestions = 5,
                                title = "What is your goal?",
                                subtitle = "What do you want to achieve with Wishlink Creator Guide?",
                                options = listOf(
                                    QuestionOption("Earn First ₹100", "Get your first affiliate commission fast", "🪙"),
                                    QuestionOption("Earn ₹1,000", "Build a steady weekly side income", "💵"),
                                    QuestionOption("Earn ₹10,000", "Scale to high-volume affiliate sales", "💰"),
                                    QuestionOption("Get Brand Deals", "Attract premium brand collaborations", "🤝"),
                                    QuestionOption("Build Long-Term Creator Business", "Establish a full-time digital income stream", "🏢"),
                                    QuestionOption("Custom Goal", "Specify your personalized target", "🎯")
                                ),
                                selectedValue = selectedGoal,
                                customInput = customGoalInput,
                                onCustomInputChange = { customGoalInput = it },
                                onOptionSelect = { opt ->
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedGoal = opt
                                    if (opt != "Custom Goal") {
                                        // Calculate AI analysis
                                        learningLevel = calculateLearningLevel(heardBefore, hasAccount, selectedPlatform)
                                        stepState = "AI_ANALYSIS"
                                    }
                                },
                                onContinueCustom = {
                                    if (customGoalInput.isNotBlank()) {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        selectedGoal = customGoalInput
                                        learningLevel = calculateLearningLevel(heardBefore, hasAccount, selectedPlatform)
                                        stepState = "AI_ANALYSIS"
                                    }
                                }
                            )
                        }

                        "AI_ANALYSIS" -> {
                            WishlinkAiAnalysisLoadingView(
                                platform = selectedPlatform,
                                niche = selectedNiche,
                                goal = selectedGoal,
                                onAnalysisComplete = {
                                    stepState = "ROADMAP"
                                }
                            )
                        }

                        "ROADMAP" -> {
                            WishlinkPersonalizedRoadmapView(
                                language = selectedLanguage,
                                platform = selectedPlatform,
                                niche = selectedNiche,
                                goal = selectedGoal,
                                learningLevel = learningLevel,
                                missionTitle = firstMissionTitle,
                                totalTime = estimatedTotalMinutes,
                                shineAnim = shineAnim,
                                onStartMission = {
                                    // Save preferences
                                    val finalGoal = if (selectedGoal == "Custom Goal") customGoalInput else selectedGoal
                                    CreatorAcademyPrefs.saveWishlinkLevel1Profile(
                                        context = context,
                                        heardBefore = heardBefore,
                                        hasAccount = hasAccount,
                                        platform = selectedPlatform,
                                        niche = selectedNiche,
                                        goal = finalGoal,
                                        learningLevel = learningLevel
                                    )

                                    val profileMap = mapOf(
                                        "heardBefore" to heardBefore,
                                        "hasAccount" to hasAccount,
                                        "platform" to selectedPlatform,
                                        "niche" to selectedNiche,
                                        "goal" to finalGoal,
                                        "learningLevel" to learningLevel,
                                        "language" to selectedLanguage
                                    )
                                    onCompleteLevel1(profileMap)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun calculateLearningLevel(heardBefore: String, hasAccount: String, platform: String): String {
    return when {
        hasAccount == "Yes" && (platform == "Instagram" || platform == "Multiple") -> "Advanced"
        hasAccount == "Yes" || heardBefore == "Yes" -> "Intermediate"
        else -> "Beginner"
    }
}

data class QuestionOption(
    val title: String,
    val description: String,
    val iconEmoji: String
)

/**
 * Onboarding Cards Swipe View (Cards 1 to 4)
 * NO NEXT BUTTON. Only horizontal swipe or Skip button.
 */
@Composable
private fun WishlinkOnboardingSwipeView(
    currentIndex: Int,
    onIndexChange: (Int) -> Unit,
    onFinishOnboarding: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val cards = listOf(
        WishlinkOnboardingCardData(
            id = 1,
            heading = "Welcome To Wishlink Creator Guide",
            subtitle = "Learn Affiliate Marketing From Zero To Professional.",
            badge = "LEVEL 1 • WELCOME",
            icon = Icons.Default.Link,
            previewType = "SHARE_LINKS"
        ),
        WishlinkOnboardingCardData(
            id = 2,
            heading = "Create • Share • Earn",
            subtitle = "Turn your content into affiliate income with smart strategies.",
            badge = "COMMISSION ENGINE",
            icon = Icons.Default.MonetizationOn,
            previewType = "EARNINGS_PHONE"
        ),
        WishlinkOnboardingCardData(
            id = 3,
            heading = "One Link. Multiple Platforms.",
            subtitle = "Learn how creators use Wishlink across Instagram, YouTube and more.",
            badge = "CROSS-PLATFORM",
            icon = Icons.Default.TrendingUp,
            previewType = "PLATFORMS_HUB"
        ),
        WishlinkOnboardingCardData(
            id = 4,
            heading = "Meet Your AI Mentor",
            subtitle = "I'll personally guide you until you become a professional Wishlink Creator.",
            badge = "PERSONAL AI MENTOR",
            icon = Icons.Default.AutoAwesome,
            previewType = "AI_MENTOR"
        )
    )

    val currentCard = cards[currentIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(currentIndex) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount < -35) { // Swipe Left -> Next
                        if (currentIndex < cards.size - 1) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onIndexChange(currentIndex + 1)
                        } else {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onFinishOnboarding()
                        }
                    } else if (dragAmount > 35) { // Swipe Right -> Prev
                        if (currentIndex > 0) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onIndexChange(currentIndex - 1)
                        }
                    }
                }
            }
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Glass Card Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0x331F0038))
                    .border(
                        BorderStroke(1.5.dp, Brush.linearGradient(listOf(PurplePrimary.copy(alpha = 0.6f), Color(0x22FFFFFF)))),
                        RoundedCornerShape(28.dp)
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(PurpleGlow)
                            .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = currentCard.badge,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PurplePrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Graphic Preview Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x22000000))
                            .border(BorderStroke(1.dp, Color(0x33B388FF)), RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        OnboardingCardGraphic(previewType = currentCard.previewType)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Heading
                    Text(
                        text = currentCard.heading,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextWhite,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Subtitle
                    Text(
                        text = currentCard.subtitle,
                        fontSize = 13.5.sp,
                        color = Color(0xFFD1C4E9),
                        textAlign = TextAlign.Center,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Page Indicator Dots
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        cards.forEachIndexed { idx, _ ->
                            val isActive = idx == currentIndex
                            val width = if (isActive) 24.dp else 8.dp
                            val color = if (isActive) PurplePrimary else Color(0x44FFFFFF)
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .height(8.dp)
                                    .width(width)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Swipe prompt indicator
                    if (currentIndex < cards.size - 1) {
                        Text(
                            text = "Swipe →",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PurplePrimary
                        )
                    } else {
                        // Card 4 CTA
                        GlassShineButton(
                            text = "Get Started →",
                            onClick = onFinishOnboarding
                        )
                    }
                }
            }
        }
    }
}

private data class WishlinkOnboardingCardData(
    val id: Int,
    val heading: String,
    val subtitle: String,
    val badge: String,
    val icon: ImageVector,
    val previewType: String
)

@Composable
private fun OnboardingCardGraphic(previewType: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "graphicAnim")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(1600, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "pulse"
    )

    when (previewType) {
        "SHARE_LINKS" -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(PurpleGlow)
                        .border(BorderStroke(2.dp, PurplePrimary), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Link,
                        contentDescription = "Links",
                        tint = GoldAccent,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = PurplePrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "wishlink.com/creator/share",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }
        }

        "EARNINGS_PHONE" -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(130.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1B0C2E))
                        .border(BorderStroke(1.5.dp, PurplePrimary), RoundedCornerShape(16.dp))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Total Commission", fontSize = 9.sp, color = Color.LightGray)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "₹12,450", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = GoldAccent)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x3300E676))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = "+142 Clicks Today", fontSize = 8.5.sp, color = Color(0xFF00E676), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        "PLATFORMS_HUB" -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                PlatformPill(name = "Instagram", icon = "📸", color = Color(0xFFE1306C))
                Icon(Icons.Default.Link, contentDescription = null, tint = PurplePrimary, modifier = Modifier.size(24.dp))
                PlatformPill(name = "YouTube", icon = "▶️", color = Color(0xFFFF0000))
            }
        }

        "AI_MENTOR" -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .graphicsLayer {
                            scaleX = pulseScale
                            scaleY = pulseScale
                        }
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(PurplePrimary.copy(alpha = 0.8f), PurpleDeepBg1)
                            )
                        )
                        .border(BorderStroke(2.dp, GoldAccent), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Mentor",
                        tint = GoldAccent,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Wishlink AI Mentor Active",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }
        }
    }
}

@Composable
private fun PlatformPill(name: String, icon: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.2f))
            .border(BorderStroke(1.dp, color), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        }
    }
}

/**
 * Language Selection Step
 */
@Composable
private fun WishlinkLanguageSelectionStep(
    selectedLang: String,
    onSelectLang: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(PurpleGlow)
                .border(BorderStroke(1.5.dp, PurplePrimary), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = "Language",
                tint = PurplePrimary,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Choose Your Language",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Select your preferred language for the Wishlink Creator Guide. This choice will be saved for all lessons.",
            fontSize = 13.sp,
            color = Color(0xFFD1C4E9),
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Language Cards
        LangOptionCard(
            title = "Hindi",
            nativeTitle = "हिन्दी",
            desc = "पूरा विशलिंक क्रिएटर कोर्स आसान हिंदी में सीखें।",
            flagEmoji = "🇮🇳",
            isSelected = selectedLang == "Hindi",
            onClick = { onSelectLang("Hindi") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        LangOptionCard(
            title = "English",
            nativeTitle = "English",
            desc = "Master Wishlink affiliate guide in simple English.",
            flagEmoji = "🌐",
            isSelected = selectedLang == "English",
            onClick = { onSelectLang("English") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        LangOptionCard(
            title = "Hinglish",
            nativeTitle = "Hinglish",
            desc = "Conversational Hindi + English guide for everyday creators.",
            flagEmoji = "⚡",
            isSelected = selectedLang == "Hinglish",
            onClick = { onSelectLang("Hinglish") }
        )
    }
}

@Composable
private fun LangOptionCard(
    title: String,
    nativeTitle: String,
    desc: String,
    flagEmoji: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.97f else 1f, label = "cardScale")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Color(0x44B388FF) else Color(0x22FFFFFF))
            .border(
                BorderStroke(
                    if (isSelected) 2.dp else 1.dp,
                    if (isSelected) PurplePrimary else Color(0x33FFFFFF)
                ),
                RoundedCornerShape(20.dp)
            )
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = flagEmoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "($nativeTitle)",
                        fontSize = 13.sp,
                        color = PurplePrimary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = desc,
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    lineHeight = 16.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = PurplePrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Single Question Card (For Q1, Q2, Q3, Q4)
 */
@Composable
private fun SingleQuestionCard(
    questionNumber: Int,
    totalQuestions: Int,
    title: String,
    subtitle: String,
    options: List<QuestionOption>,
    selectedValue: String,
    onOptionSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Progress Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "QUESTION $questionNumber OF $totalQuestions",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PurplePrimary,
                letterSpacing = 1.sp
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PurpleGlow)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${(questionNumber * 100) / totalQuestions}%",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Title & Subtitle
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextWhite,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = subtitle,
            fontSize = 13.sp,
            color = Color(0xFFD1C4E9),
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Options List
        options.forEach { option ->
            val isSelected = selectedValue == option.title
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val scale by animateFloatAsState(targetValue = if (isPressed) 0.98f else 1f, label = "optScale")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isSelected) Color(0x44B388FF) else Color(0x22FFFFFF))
                    .border(
                        BorderStroke(
                            if (isSelected) 2.dp else 1.dp,
                            if (isSelected) PurplePrimary else Color(0x33FFFFFF)
                        ),
                        RoundedCornerShape(18.dp)
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onOptionSelect(option.title) }
                    )
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = option.iconEmoji, fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = option.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = option.description,
                            fontSize = 12.sp,
                            color = Color.LightGray,
                            lineHeight = 16.sp
                        )
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = PurplePrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Single Question Card with Custom Option (For Question 5 - Goal)
 */
@Composable
private fun SingleQuestionCardWithCustom(
    questionNumber: Int,
    totalQuestions: Int,
    title: String,
    subtitle: String,
    options: List<QuestionOption>,
    selectedValue: String,
    customInput: String,
    onCustomInputChange: (String) -> Unit,
    onOptionSelect: (String) -> Unit,
    onContinueCustom: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "QUESTION $questionNumber OF $totalQuestions",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PurplePrimary,
                letterSpacing = 1.sp
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PurpleGlow)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "100%",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextWhite,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = subtitle,
            fontSize = 13.sp,
            color = Color(0xFFD1C4E9),
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        options.forEach { option ->
            val isSelected = selectedValue == option.title
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val scale by animateFloatAsState(targetValue = if (isPressed) 0.98f else 1f, label = "optCustomScale")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isSelected) Color(0x44B388FF) else Color(0x22FFFFFF))
                    .border(
                        BorderStroke(
                            if (isSelected) 2.dp else 1.dp,
                            if (isSelected) PurplePrimary else Color(0x33FFFFFF)
                        ),
                        RoundedCornerShape(18.dp)
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onOptionSelect(option.title) }
                    )
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = option.iconEmoji, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = option.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = option.description,
                            fontSize = 11.5.sp,
                            color = Color.LightGray
                        )
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = PurplePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Custom Goal Text Field if selected
        if (selectedValue == "Custom Goal") {
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedTextField(
                value = customInput,
                onValueChange = onCustomInputChange,
                placeholder = { Text("Type your personalized creator goal...", color = Color.Gray, fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PurplePrimary,
                    unfocusedBorderColor = Color(0x44FFFFFF),
                    focusedContainerColor = Color(0x221F0038),
                    unfocusedContainerColor = Color(0x221F0038),
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            GlassShineButton(
                text = "Continue to AI Analysis →",
                onClick = onContinueCustom
            )
        }
    }
}

/**
 * AI Analysis Processing Screen
 */
@Composable
private fun WishlinkAiAnalysisLoadingView(
    platform: String,
    niche: String,
    goal: String,
    onAnalysisComplete: () -> Unit
) {
    var progress by remember { mutableStateOf(0f) }
    var currentAnalysisText by remember { mutableStateOf("Analyzing creator profile & platform...") }

    LaunchedEffect(Unit) {
        currentAnalysisText = "Evaluating $platform creator ecosystem..."
        progress = 0.25f
        delay(700)

        currentAnalysisText = "Optimizing $niche product affiliate conversion model..."
        progress = 0.60f
        delay(700)

        currentAnalysisText = "Generating strategy for goal: $goal..."
        progress = 0.90f
        delay(600)

        progress = 1.0f
        delay(300)
        onAnalysisComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(PurpleGlow)
                    .border(BorderStroke(2.dp, PurplePrimary), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "AI Processing",
                    tint = GoldAccent,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Generating Your Wishlink AI Roadmap",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = currentAnalysisText,
                fontSize = 13.sp,
                color = PurplePrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(fraction = progress)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(PurplePrimary, GoldAccent)
                            )
                        )
                )
            }
        }
    }
}

/**
 * AI Personalized Learning Roadmap Screen
 */
@Composable
private fun WishlinkPersonalizedRoadmapView(
    language: String,
    platform: String,
    niche: String,
    goal: String,
    learningLevel: String,
    missionTitle: String,
    totalTime: String,
    shineAnim: Float,
    onStartMission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Success Header Badge
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(PurpleGlow)
                    .border(BorderStroke(1.5.dp, GoldAccent), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Verified, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Personal Learning Roadmap Ready!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite
                )
                Text(
                    text = "Customized for $platform • $niche",
                    fontSize = 12.sp,
                    color = PurplePrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // AI Summary Glass Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0x331F0038))
                .border(BorderStroke(1.5.dp, PurpleGlow), RoundedCornerShape(22.dp))
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RoadmapSummaryPill(title = "LEARNING LEVEL", value = learningLevel, color = GoldAccent)
                    RoadmapSummaryPill(title = "ESTIMATED TIME", value = "5 Mins / Day", color = PurplePrimary)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RoadmapSummaryPill(title = "TARGET GOAL", value = goal, color = TextWhite)
                    RoadmapSummaryPill(title = "LANGUAGE", value = language.ifBlank { "English" }, color = Color(0xFF00E676))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // TODAY'S FIRST MISSION CARD
        Text(
            text = "TODAY'S FIRST MISSION",
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PurplePrimary,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0x44B388FF), Color(0x221F0038))
                    )
                )
                .border(BorderStroke(1.5.dp, PurplePrimary), RoundedCornerShape(22.dp))
                .padding(18.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GoldAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🚀", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = missionTitle,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "Estimated Time: 5 Minutes",
                            fontSize = 12.sp,
                            color = GoldAccent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "• Set up your official Wishlink Creator account & verify mobile OTP.\n• Connect your primary $platform creator profile.\n• Unlock 100+ top affiliate fashion & lifestyle brand links.",
                    fontSize = 12.5.sp,
                    color = Color.LightGray,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Glass Action Button
        GlassShineButton(
            text = "Start Today's First Mission 🚀",
            onClick = onStartMission
        )
    }
}

@Composable
private fun RoadmapSummaryPill(title: String, value: String, color: Color) {
    Column {
        Text(text = title, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 0.5.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

/**
 * Premium Glass Button with Animated Shine Overlay & Ripple Effect
 */
@Composable
fun GlassShineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.96f else 1f, label = "btnScale")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(26.dp))
            .background(
                if (enabled) {
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF8E24AA),
                            Color(0xFFB388FF),
                            Color(0xFF7B1FA2)
                        )
                    )
                } else {
                    Brush.horizontalGradient(
                        listOf(
                            Color(0x448E24AA),
                            Color(0x44B388FF),
                            Color(0x447B1FA2)
                        )
                    )
                }
            )
            .border(
                BorderStroke(1.5.dp, if (enabled) Color(0x66FFFFFF) else Color(0x22FFFFFF)),
                RoundedCornerShape(26.dp)
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                fontSize = 15.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite,
                letterSpacing = 0.5.sp
            )
        }
    }
}
