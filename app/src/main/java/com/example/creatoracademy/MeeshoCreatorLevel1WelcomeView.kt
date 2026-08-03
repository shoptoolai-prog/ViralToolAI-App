package com.example.creatoracademy

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
 * MASTER PHASE 1 - Meesho Creator Guide Level 1 View
 * Complete Redesign: Welcome, Onboarding Cards, Language Selection, Single-Question Personalization, AI Analysis & Roadmap
 */

@Composable
fun MeeshoCreatorLevel1WelcomeView(
    onCompleteLevel1: (profile: Map<String, String>) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Internal State Machine: ONBOARDING -> LANGUAGE -> Q1 -> Q2 -> Q3 -> Q4 -> Q5 -> AI_ANALYSIS -> ROADMAP
    var stepState by remember { mutableStateOf("ONBOARDING") }

    // Onboarding Cards index (0 to 3)
    var onboardingIndex by remember { mutableIntStateOf(0) }

    // User Selection States
    var selectedLanguage by remember { mutableStateOf("") }
    var usedMeeshoBefore by remember { mutableStateOf("") }
    var hasMeeshoAccount by remember { mutableStateOf("") }
    var hasCreatorAccount by remember { mutableStateOf("") }
    var selectedPlatforms by remember { mutableStateOf(setOf<String>()) }
    var selectedGoal by remember { mutableStateOf("") }
    var customGoalInput by remember { mutableStateOf("") }

    // Calculated Learning Level
    var learningLevel by remember { mutableStateOf("Beginner") }
    var firstMissionTitle by remember { mutableStateOf("Complete Account Setup") }
    var estimatedTotalMinutes by remember { mutableStateOf("15 Minutes") }
    var roadmapDifficulty by remember { mutableStateOf("Beginner Friendly") }

    // Floating animation
    val infiniteTransition = rememberInfiniteTransition(label = "pinkBgAnim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatY"
    )

    val shineAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "shine"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF330922),
                        Color(0xFF1F0515),
                        Color(0xFF12020D)
                    )
                )
            )
    ) {
        // BACKGROUND: Floating Shopping Bags, Gift Boxes, Rupee Symbols, Golden Particles, Soft Glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Pink Soft Glow Radial Centers
            drawCircle(
                color = Color(0x33FF2A7A),
                radius = w * 0.45f,
                center = Offset(w * 0.2f, h * 0.25f)
            )
            drawCircle(
                color = Color(0x22E91E63),
                radius = w * 0.5f,
                center = Offset(w * 0.8f, h * 0.75f)
            )

            // Golden Particles
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.4f), radius = 10.dp.toPx(), center = Offset(w * 0.15f, h * 0.18f + floatY * 2f))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.3f), radius = 14.dp.toPx(), center = Offset(w * 0.85f, h * 0.32f - floatY * 2.2f))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.35f), radius = 18.dp.toPx(), center = Offset(w * 0.22f, h * 0.65f + floatY * 2.5f))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.25f), radius = 12.dp.toPx(), center = Offset(w * 0.78f, h * 0.82f - floatY * 2.8f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // TOP BAR (Skip on Top Right during Onboarding)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button (if beyond Onboarding or for Onboarding)
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        when (stepState) {
                            "ONBOARDING" -> if (onboardingIndex > 0) onboardingIndex-- else onBack()
                            "LANGUAGE" -> stepState = "ONBOARDING"
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
                    Text("←", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                // Level 1 Badge Title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x33FF2A7A))
                            .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("MEESHO CREATOR • LEVEL 1", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
                    }
                }

                // Skip Button (Only visible during onboarding cards)
                if (stepState == "ONBOARDING") {
                    TextButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            stepState = "LANGUAGE"
                        }
                    ) {
                        Text("Skip", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF2A7A))
                    }
                } else {
                    Spacer(modifier = Modifier.width(36.dp))
                }
            }

            // DYNAMIC BODY CONTENT ACCORDING TO STEP
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (stepState) {
                    "ONBOARDING" -> {
                        MeeshoOnboardingCardsView(
                            cardIndex = onboardingIndex,
                            onSwipeLeft = {
                                if (onboardingIndex < 3) {
                                    onboardingIndex++
                                } else {
                                    stepState = "LANGUAGE"
                                }
                            },
                            onSwipeRight = {
                                if (onboardingIndex > 0) onboardingIndex--
                            }
                        )
                    }

                    "LANGUAGE" -> {
                        MeeshoLanguageSelectionView(
                            onSelect = { lang ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedLanguage = lang
                                stepState = "Q1"
                            }
                        )
                    }

                    "Q1" -> {
                        MeeshoSingleQuestionCard(
                            questionNumber = "1 / 5",
                            questionText = "Have you ever used Meesho?",
                            options = listOf("Yes", "No"),
                            selectedOption = usedMeeshoBefore,
                            onOptionSelected = { option ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                usedMeeshoBefore = option
                                stepState = "Q2"
                            }
                        )
                    }

                    "Q2" -> {
                        MeeshoSingleQuestionCard(
                            questionNumber = "2 / 5",
                            questionText = "Do you already have a Meesho account?",
                            options = listOf("Yes", "No"),
                            selectedOption = hasMeeshoAccount,
                            onOptionSelected = { option ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                hasMeeshoAccount = option
                                stepState = "Q3"
                            }
                        )
                    }

                    "Q3" -> {
                        MeeshoSingleQuestionCard(
                            questionNumber = "3 / 5",
                            questionText = "Do you already have a Meesho Creator / Affiliate account?",
                            options = listOf("Yes", "No"),
                            selectedOption = hasCreatorAccount,
                            onOptionSelected = { option ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                hasCreatorAccount = option
                                stepState = "Q4"
                            }
                        )
                    }

                    "Q4" -> {
                        MeeshoMultiPlatformQuestionCard(
                            questionNumber = "4 / 5",
                            questionText = "Which platform do you want to promote on?",
                            platforms = listOf("Instagram", "YouTube", "Facebook", "WhatsApp", "Telegram", "Multiple"),
                            selectedSet = selectedPlatforms,
                            onPlatformToggled = { p ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedPlatforms = if (selectedPlatforms.contains(p)) {
                                    selectedPlatforms - p
                                } else {
                                    selectedPlatforms + p
                                }
                            },
                            onContinue = {
                                if (selectedPlatforms.isNotEmpty()) {
                                    stepState = "Q5"
                                } else {
                                    Toast.makeText(context, "Please select at least 1 platform", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }

                    "Q5" -> {
                        MeeshoGoalQuestionCard(
                            questionNumber = "5 / 5",
                            questionText = "What is your current goal?",
                            goals = listOf("Earn First ₹100", "Earn ₹1,000", "Earn ₹10,000", "Earn ₹50,000", "Become Full-Time Creator", "Custom"),
                            selectedGoal = selectedGoal,
                            customGoalInput = customGoalInput,
                            onCustomGoalChange = { customGoalInput = it },
                            onGoalSelected = { goal ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedGoal = goal
                                val finalGoal = if (goal == "Custom" && customGoalInput.isNotBlank()) customGoalInput else goal

                                // Calculate Learning Level
                                learningLevel = when {
                                    hasCreatorAccount == "Yes" -> "Advanced"
                                    hasMeeshoAccount == "Yes" && usedMeeshoBefore == "Yes" -> "Intermediate"
                                    else -> "Beginner"
                                }

                                firstMissionTitle = if (hasCreatorAccount == "Yes") {
                                    "Explore High-Commission Categories"
                                } else {
                                    "Complete Account Setup"
                                }

                                estimatedTotalMinutes = if (learningLevel == "Advanced") "10 Minutes" else "15 Minutes"
                                roadmapDifficulty = if (learningLevel == "Advanced") "Fast-Track Professional" else "Beginner Friendly"

                                // Save Profile
                                CreatorAcademyPrefs.saveMeeshoLevel1Profile(
                                    context = context,
                                    language = selectedLanguage,
                                    usedBefore = usedMeeshoBefore,
                                    accountStatus = hasMeeshoAccount,
                                    creatorStatus = hasCreatorAccount,
                                    platform = if (selectedPlatforms.contains("Multiple")) "Multiple" else selectedPlatforms.joinToString(", "),
                                    goal = finalGoal,
                                    learningLevel = learningLevel
                                )

                                stepState = "AI_ANALYSIS"
                            }
                        )
                    }

                    "AI_ANALYSIS" -> {
                        MeeshoAiAnalysisLoadingView(
                            onFinished = {
                                stepState = "ROADMAP"
                            }
                        )
                    }

                    "ROADMAP" -> {
                        MeeshoRoadmapMissionView(
                            learningLevel = learningLevel,
                            selectedLanguage = selectedLanguage,
                            firstMissionTitle = firstMissionTitle,
                            estimatedTime = estimatedTotalMinutes,
                            difficulty = roadmapDifficulty,
                            targetGoal = if (selectedGoal == "Custom") customGoalInput else selectedGoal,
                            onStartMission = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                val profile = CreatorAcademyPrefs.getMeeshoLevel1Profile(context)
                                onCompleteLevel1(profile)
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 1. ONBOARDING CARDS VIEW (SWIPE ONLY, NO NEXT BUTTON)
 */
@Composable
private fun MeeshoOnboardingCardsView(
    cardIndex: Int,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit
) {
    val cards = listOf(
        OnboardingCardData(
            icon = "📦",
            heading = "Welcome To Meesho Creator Guide",
            subtitle = "Learn everything from Zero to Professional Meesho Creator."
        ),
        OnboardingCardData(
            icon = "📱",
            heading = "Learn • Promote • Earn",
            subtitle = "Understand how Meesho Creator works step by step."
        ),
        OnboardingCardData(
            icon = "🎬",
            heading = "No Experience Needed",
            subtitle = "Even beginners can start. We'll guide you step by step."
        ),
        OnboardingCardData(
            icon = "🤖",
            heading = "Meet Your AI Mentor",
            subtitle = "I'll personally guide you until you become a Professional Meesho Creator."
        )
    )

    val card = cards.getOrElse(cardIndex) { cards[0] }

    var dragOffset by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(cardIndex) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (dragOffset < -60f) {
                            onSwipeLeft()
                        } else if (dragOffset > 60f) {
                            onSwipeRight()
                        }
                        dragOffset = 0f
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        dragOffset += dragAmount
                    }
                )
            }
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0x331F0814))
                .border(1.5.dp, Color(0x66FF2A7A), RoundedCornerShape(24.dp))
                .padding(28.dp)
        ) {
            // Animated Icon Display
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFFFF2A7A).copy(alpha = 0.5f), Color.Transparent)
                        )
                    )
                    .border(2.dp, Color(0xFFFF2A7A), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(card.icon, fontSize = 54.sp)
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = card.heading,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = card.subtitle,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Page Indicator Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0..3) {
                    val isActive = i == cardIndex
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(if (isActive) 24.dp else 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isActive) Color(0xFFFF2A7A) else Color.White.copy(alpha = 0.3f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Swipe prompt hint
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Swipe ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF2A7A))
                Text("→", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
            }
        }
    }
}

private data class OnboardingCardData(
    val icon: String,
    val heading: String,
    val subtitle: String
)

/**
 * 2. LANGUAGE SELECTION VIEW
 */
@Composable
private fun MeeshoLanguageSelectionView(
    onSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Choose Your Language",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        val languages = listOf(
            Triple("Hindi", "🇮🇳 हिन्दी", "हिन्दी में सीखें meesho creator roadmap"),
            Triple("English", "🇺🇸 English", "Learn in clear English step by step"),
            Triple("Hinglish", "🌐 Hinglish", "Simple Hindi + English easy guidance")
        )

        languages.forEach { (key, title, subtitle) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x33FF2A7A))
                    .border(1.5.dp, Color(0x66FF2A7A), RoundedCornerShape(18.dp))
                    .clickable { onSelect(key) }
                    .padding(20.dp)
            ) {
                Column {
                    Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(subtitle, fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                }
            }
        }
    }
}

/**
 * 3. SINGLE QUESTION PERSONALIZATION CARD
 */
@Composable
private fun MeeshoSingleQuestionCard(
    questionNumber: String,
    questionText: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text("QUESTION $questionNumber", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = questionText,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        options.forEach { option ->
            val isSelected = selectedOption == option
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) Color(0xFFFF2A7A) else Color(0x22FFFFFF))
                    .border(1.5.dp, if (isSelected) Color.White else Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .clickable { onOptionSelected(option) }
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.9f)
                    )
                    if (isSelected) {
                        Text("✓", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                }
            }
        }
    }
}

/**
 * 4. MULTI-PLATFORM QUESTION CARD
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MeeshoMultiPlatformQuestionCard(
    questionNumber: String,
    questionText: String,
    platforms: List<String>,
    selectedSet: Set<String>,
    onPlatformToggled: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text("QUESTION $questionNumber", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = questionText,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            platforms.forEach { platform ->
                val isSelected = selectedSet.contains(platform)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) Color(0xFFFF2A7A) else Color(0x22FFFFFF))
                        .border(1.5.dp, if (isSelected) Color.White else Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                        .clickable { onPlatformToggled(platform) }
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = platform,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Continue →", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

/**
 * 5. GOAL QUESTION CARD
 */
@Composable
private fun MeeshoGoalQuestionCard(
    questionNumber: String,
    questionText: String,
    goals: List<String>,
    selectedGoal: String,
    customGoalInput: String,
    onCustomGoalChange: (String) -> Unit,
    onGoalSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text("QUESTION $questionNumber", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = questionText,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        goals.forEach { goal ->
            val isSelected = selectedGoal == goal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) Color(0xFFFF2A7A) else Color(0x22FFFFFF))
                    .border(1.5.dp, if (isSelected) Color.White else Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .clickable { onGoalSelected(goal) }
                    .padding(16.dp)
            ) {
                Text(goal, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        if (selectedGoal == "Custom") {
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = customGoalInput,
                onValueChange = onCustomGoalChange,
                placeholder = { Text("Enter your custom target...", color = Color.White.copy(alpha = 0.5f)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF2A7A),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { onGoalSelected("Custom") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
            ) {
                Text("Confirm Custom Goal", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 6. AI ANALYSIS LOADING ANIMATION
 */
@Composable
private fun MeeshoAiAnalysisLoadingView(
    onFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1800)
        onFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color(0x33FF2A7A))
                .border(2.dp, Color(0xFFFF2A7A), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🧠", fontSize = 42.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "AI Analyzing Your Profile...",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Generating personalized learning roadmap & custom mission...",
            fontSize = 12.5.sp,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 7. ROADMAP & TODAY'S MISSION GLASS CARD VIEW
 */
@Composable
private fun MeeshoRoadmapMissionView(
    learningLevel: String,
    selectedLanguage: String,
    firstMissionTitle: String,
    estimatedTime: String,
    difficulty: String,
    targetGoal: String,
    onStartMission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text("✨ Your Personalized Roadmap", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Spacer(modifier = Modifier.height(4.dp))
        Text("AI Has Custom-Tailored Level 1 for You", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(20.dp))

        // Profile Analysis Summary Glass Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("LEARNING LEVEL", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x44FF2A7A))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(learningLevel.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Target Goal", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                        Text(targetGoal.ifBlank { "Earn ₹10,000" }, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Estimated Time", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                        Text(estimatedTime, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // TODAY'S MISSION GLASS CARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Brush.linearGradient(listOf(Color(0x44FF2A7A), Color(0x22E91E63))))
                .border(1.5.dp, Color(0xFFFF2A7A), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🎯 TODAY'S MISSION", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0x33FFFFFF))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("5 Minutes", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = firstMissionTitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Start your step-by-step Meesho Creator journey with AI Mentor guidance.",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onStartMission,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Continue to Mission →", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
