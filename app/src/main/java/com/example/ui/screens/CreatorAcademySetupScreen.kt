package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.creatoracademy.CreatorSetupData
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite

/**
 * MASTER PHASE 15B.1 — Conversational Creator Academy Onboarding
 * AI Mentor asks one question at a time across 8 steps:
 * 1. Platform (Instagram, YouTube, Both)
 * 2. Experience (Beginner, Intermediate, Advanced)
 * 3. Current Followers (Custom input)
 * 4. Niche (Fashion, Beauty, Gaming, Education, Business, Shopping Reviews, Tech, Travel, Food, Fitness, Other)
 * 5. Primary Goal (Followers, Views, Brand Deals, Affiliate Income, Business, Personal Brand)
 * 6. Posting Frequency (Daily, Weekly, Weekend, Custom)
 * 7. Available Learning Time (15 min, 30 min, 1 hour, Custom)
 * 8. Preferred Language (English, HinEnglish, Hindi)
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CreatorAcademySetupScreen(
    onSetupCompleted: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var currentStep by remember { mutableStateOf(1) } // 1 to 8

    var selectedPlatform by remember { mutableStateOf("Instagram") }
    var selectedSkillLevel by remember { mutableStateOf("Beginner") }
    var currentFollowersInput by remember { mutableStateOf("1k") }
    var selectedNiche by remember { mutableStateOf("Tech") }
    var customNicheText by remember { mutableStateOf("") }
    var selectedGoal by remember { mutableStateOf("Followers") }
    var selectedPostingFreq by remember { mutableStateOf("Daily") }
    var customFreqText by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("15 min") }
    var customTimeText by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("English") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header & Step Progress
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (currentStep > 1) {
                                currentStep--
                            } else {
                                onBack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextWhite
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "AI Mentor",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI Mentor Onboarding",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    Text(
                        text = "Question $currentStep/8",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { currentStep / 8f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = EmeraldPrimary,
                    trackColor = Color(0x22FFFFFF)
                )
            }

            // Step Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(280)) + scaleIn(initialScale = 0.96f, animationSpec = tween(280))) with
                                (fadeOut(animationSpec = tween(280)) + scaleOut(targetScale = 1.04f, animationSpec = tween(280)))
                    },
                    label = "SetupStepTransition"
                ) { step ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (step) {
                            1 -> PlatformStep(
                                selected = selectedPlatform,
                                onSelect = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedPlatform = it
                                }
                            )
                            2 -> ExperienceStep(
                                selected = selectedSkillLevel,
                                onSelect = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedSkillLevel = it
                                }
                            )
                            3 -> FollowersStep(
                                followersValue = currentFollowersInput,
                                onValueChange = { currentFollowersInput = it },
                                onChipSelect = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    currentFollowersInput = it
                                }
                            )
                            4 -> NicheStep(
                                selectedNiche = selectedNiche,
                                customNiche = customNicheText,
                                onSelectNiche = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedNiche = it
                                },
                                onCustomNicheChange = { customNicheText = it }
                            )
                            5 -> GoalStep(
                                selectedGoal = selectedGoal,
                                onSelectGoal = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedGoal = it
                                }
                            )
                            6 -> PostingFreqStep(
                                selectedFreq = selectedPostingFreq,
                                customFreq = customFreqText,
                                onSelectFreq = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedPostingFreq = it
                                },
                                onCustomFreqChange = { customFreqText = it }
                            )
                            7 -> LearningTimeStep(
                                selectedTime = selectedTime,
                                customTime = customTimeText,
                                onSelectTime = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedTime = it
                                },
                                onCustomTimeChange = { customTimeText = it }
                            )
                            8 -> LanguageStep(
                                selectedLang = selectedLanguage,
                                onSelectLang = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedLanguage = it
                                }
                            )
                        }
                    }
                }
            }

            // Bottom Navigation Action
            val buttonInteractionSource = remember { MutableInteractionSource() }
            val isPressed by buttonInteractionSource.collectIsPressedAsState()
            val scale by animateFloatAsState(
                targetValue = if (isPressed) 0.95f else 1f,
                animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
                label = "setupBtnScale"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(26.dp), spotColor = EmeraldPrimary)
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF10B981), Color(0xFF059669))
                        )
                    )
                    .clickable(
                        interactionSource = buttonInteractionSource,
                        indication = androidx.compose.foundation.LocalIndication.current,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (currentStep < 8) {
                                currentStep++
                            } else {
                                val effectiveNiche = if (selectedNiche == "Other") customNicheText.ifBlank { "General" } else selectedNiche
                                val effectiveFreq = if (selectedPostingFreq == "Custom") customFreqText.ifBlank { "Flexible" } else selectedPostingFreq
                                val effectiveTime = if (selectedTime == "Custom") customTimeText.ifBlank { "Flexible" } else selectedTime

                                val setupData = CreatorSetupData(
                                    targetPlatform = selectedPlatform,
                                    skillLevel = selectedSkillLevel,
                                    currentFollowers = currentFollowersInput.ifBlank { "0" },
                                    niche = effectiveNiche,
                                    primaryGoal = selectedGoal,
                                    postingFrequency = effectiveFreq,
                                    availableTime = effectiveTime,
                                    preferredLanguage = selectedLanguage
                                )
                                CreatorAcademyPrefs.saveSetupData(context, setupData)
                                onSetupCompleted()
                            }
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (currentStep == 8) "Generate My AI Roadmap 🚀" else "Continue",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        letterSpacing = 0.5.sp
                    )
                    if (currentStep < 8) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next",
                            tint = TextWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MentorBubble(promptText: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0x1510B981))
            .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f)), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(EmeraldPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Mentor AI",
                    tint = AmoledBlack,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "AI Mentor",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = EmeraldPrimary,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = promptText,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextWhite,
                    lineHeight = 18.sp
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
private fun PlatformStep(selected: String, onSelect: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        MentorBubble("Welcome Creator! First, which platform are you focusing on building right now?")
        val options = listOf(
            Triple("Instagram", "Reels, Posts, Stories & Bio Growth", "📸"),
            Triple("YouTube", "Shorts, Long-form Videos & Channel SEO", "▶️"),
            Triple("Both", "Dual-Platform Strategy for Maximum Reach", "⚡")
        )
        options.forEach { (item, desc, emoji) ->
            SetupOptionCard(title = "$emoji $item", subtitle = desc, isSelected = selected == item, onClick = { onSelect(item) })
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ExperienceStep(selected: String, onSelect: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        MentorBubble("What is your current content creation experience level?")
        val options = listOf(
            Triple("Beginner", "Just starting out with camera, tools & ideas", "🌱"),
            Triple("Intermediate", "Consistently posting, looking for growth formulas", "⚡"),
            Triple("Advanced", "Established creator seeking monetization & scale", "🚀")
        )
        options.forEach { (item, desc, emoji) ->
            SetupOptionCard(title = "$emoji $item", subtitle = desc, isSelected = selected == item, onClick = { onSelect(item) })
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun FollowersStep(
    followersValue: String,
    onValueChange: (String) -> Unit,
    onChipSelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        MentorBubble("How many followers or subscribers do you currently have?")

        OutlinedTextField(
            value = followersValue,
            onValueChange = onValueChange,
            label = { Text("Current Followers / Subs", color = TextWhite.copy(alpha = 0.6f)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = Color(0x33FFFFFF),
                focusedLabelColor = EmeraldPrimary,
                cursorColor = EmeraldPrimary,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            ),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Or tap a quick range:",
            fontSize = 11.sp,
            color = TextWhite.copy(alpha = 0.5f),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val chips = listOf("0 - 1k", "1k - 10k", "10k - 50k", "50k - 100k", "100k+")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            chips.take(3).forEach { chip ->
                QuickChip(text = chip, isSelected = followersValue == chip, onClick = { onChipSelect(chip) }, modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            chips.drop(3).forEach { chip ->
                QuickChip(text = chip, isSelected = followersValue == chip, onClick = { onChipSelect(chip) }, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun NicheStep(
    selectedNiche: String,
    customNiche: String,
    onSelectNiche: (String) -> Unit,
    onCustomNicheChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        MentorBubble("What is your primary creator niche?")

        val niches = listOf(
            "Fashion", "Beauty", "Gaming", "Education",
            "Business", "Shopping Reviews", "Tech", "Travel",
            "Food", "Fitness", "Other"
        )

        niches.forEach { niche ->
            SetupOptionCard(
                title = niche,
                subtitle = "Tailor content & growth formulas for $niche",
                isSelected = selectedNiche == niche,
                onClick = { onSelectNiche(niche) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (selectedNiche == "Other") {
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = customNiche,
                onValueChange = onCustomNicheChange,
                label = { Text("Specify Custom Niche", color = TextWhite.copy(alpha = 0.6f)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    unfocusedBorderColor = Color(0x33FFFFFF),
                    focusedLabelColor = EmeraldPrimary,
                    cursorColor = EmeraldPrimary,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                shape = RoundedCornerShape(14.dp)
            )
        }
    }
}

@Composable
private fun GoalStep(selectedGoal: String, onSelectGoal: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        MentorBubble("What is your primary goal right now?")
        val goals = listOf(
            "Followers" to "Build a loyal, engaged audience base",
            "Views" to "Maximize viral reach & video retention",
            "Brand Deals" to "Secure paid sponsorships & collaborations",
            "Affiliate Income" to "Earn commissions promoting products",
            "Business" to "Generate leads & sales for your products/services",
            "Personal Brand" to "Establish industry authority & trust"
        )
        goals.forEach { (goal, desc) ->
            SetupOptionCard(title = goal, subtitle = desc, isSelected = selectedGoal == goal, onClick = { onSelectGoal(goal) })
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun PostingFreqStep(
    selectedFreq: String,
    customFreq: String,
    onSelectFreq: (String) -> Unit,
    onCustomFreqChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        MentorBubble("How often do you plan to post content?")
        val options = listOf(
            Triple("Daily", "Post every day for maximum algorithm momentum", "📅"),
            Triple("Weekly", "3-4 strategic posts per week", "📆"),
            Triple("Weekend", "Batch create & post on Saturdays & Sundays", "☕"),
            Triple("Custom", "Set your own flexible posting pace", "⏱️")
        )
        options.forEach { (freq, desc, emoji) ->
            SetupOptionCard(title = "$emoji $freq", subtitle = desc, isSelected = selectedFreq == freq, onClick = { onSelectFreq(freq) })
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (selectedFreq == "Custom") {
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = customFreq,
                onValueChange = onCustomFreqChange,
                label = { Text("Specify Custom Frequency (e.g., 2x a week)", color = TextWhite.copy(alpha = 0.6f)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    unfocusedBorderColor = Color(0x33FFFFFF),
                    focusedLabelColor = EmeraldPrimary,
                    cursorColor = EmeraldPrimary,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                shape = RoundedCornerShape(14.dp)
            )
        }
    }
}

@Composable
private fun LearningTimeStep(
    selectedTime: String,
    customTime: String,
    onSelectTime: (String) -> Unit,
    onCustomTimeChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        MentorBubble("How much time can you dedicate to learning per session?")
        val timeOptions = listOf(
            Triple("15 min", "Quick daily micro-lessons & actionable hooks", "⚡"),
            Triple("30 min", "Standard lesson + exercise & script draft", "🎯"),
            Triple("1 hour", "Deep-dive strategy, competitor research & editing", "🧠"),
            Triple("Custom", "Flexible learning pace based on availability", "⏱️")
        )
        timeOptions.forEach { (time, desc, emoji) ->
            SetupOptionCard(title = "$emoji $time", subtitle = desc, isSelected = selectedTime == time, onClick = { onSelectTime(time) })
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (selectedTime == "Custom") {
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = customTime,
                onValueChange = onCustomTimeChange,
                label = { Text("Specify Custom Time (e.g., 45 mins)", color = TextWhite.copy(alpha = 0.6f)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    unfocusedBorderColor = Color(0x33FFFFFF),
                    focusedLabelColor = EmeraldPrimary,
                    cursorColor = EmeraldPrimary,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                shape = RoundedCornerShape(14.dp)
            )
        }
    }
}

@Composable
private fun LanguageStep(selectedLang: String, onSelectLang: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        MentorBubble("Select your preferred language for AI mentorship lessons:")
        val languages = listOf(
            Triple("English", "International English (Default)", "🌐"),
            Triple("HinEnglish", "Mix of Hindi & English for natural clarity", "🇮🇳"),
            Triple("Hindi", "Shuddha Hindi (हिंदी)", "📜")
        )
        languages.forEach { (lang, desc, emoji) ->
            SetupOptionCard(title = "$emoji $lang", subtitle = desc, isSelected = selectedLang == lang, onClick = { onSelectLang(lang) })
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun QuickChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(38.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0x2210B981) else Color(0x0EFFFFFF))
            .border(BorderStroke(1.dp, if (isSelected) EmeraldPrimary else Color(0x1AFFFFFF)), RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) EmeraldPrimary else TextWhite.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun SetupOptionCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "setupOptScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0x2210B981) else Color(0x0CFFFFFF))
            .border(
                BorderStroke(if (isSelected) 1.5.dp else 1.dp, if (isSelected) EmeraldPrimary else Color(0x1AFFFFFF)),
                RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClick = onClick
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) EmeraldPrimary else TextWhite
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextWhite.copy(alpha = 0.6f)
                )
            }

            if (isSelected) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(EmeraldPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = AmoledBlack,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
