package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.core.LanguageEngine
import com.example.creatoracademy.AiMentorEngine
import com.example.creatoracademy.AiMentorTask
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.creatoracademy.CreatorLevel
import com.example.creatoracademy.TaskState
import com.example.creatoracademy.ViralMemoryEngine
import com.example.ui.components.GlassCard
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * MASTER PHASE 15B — AI Mentor Engine v1
 * Transforms Creator Academy into an interactive AI Mentor:
 * - Dynamic Mentor Dashboard (Today's Mission, Level, XP, Streak, % Progress, Est. Time)
 * - Personalized Task Engine (Platform, Skill Level, Goal, Available Time)
 * - 4-State Task Engine (Locked, Current, Completed, Skipped)
 * - Interactive Task Verification (YES / NOT YET -> Explain, Example, Skip)
 * - Conversational AI Coach Mode
 * - Example Library (Good, Bad, Pro Tip, Common Mistake)
 * - Progress System with Creator Levels (Bronze, Silver, Gold, Diamond, Legend)
 * - Smart Welcome Back Reminders & Session Memory
 * - Premium Micro-Animations under 1 second
 */
@Composable
fun CreatorAcademyScreen(
    onSwitchExperience: () -> Unit,
    onResetSetup: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    var setupData by remember { mutableStateOf(CreatorAcademyPrefs.getSetupData(context)) }
    var selectedPlatform by remember { mutableStateOf("INSTAGRAM") } // INSTAGRAM, YOUTUBE, VIDEO_EDITING
    var showVideoEditingLockedDialog by remember { mutableStateOf(false) }
    var showBrandCollabDialog by remember { mutableStateOf(false) }
    var selectedPremiumTool by remember { mutableStateOf<com.example.ui.components.PremiumToolData?>(null) }

    var activeToolDialog by remember { mutableStateOf<String?>(null) }
    var activeLinkDialog by remember { mutableStateOf<String?>(null) }
    var coursePlaceholderTitle by remember { mutableStateOf<String?>(null) }
    var showInstagramCreatorV2Dialog by remember { mutableStateOf(false) }
    var showYouTubeCreatorV2Dialog by remember { mutableStateOf(false) }
    var showAiVideoImageGeneratorDialog by remember { mutableStateOf(false) }

    val entranceAnimProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        entranceAnimProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
        )
    }
    val cardAnimVal = entranceAnimProgress.value

    val instaAlpha = ((cardAnimVal - 0.25f) / 0.35f).coerceIn(0f, 1f)
    val instaTranslationY = (30f * (1f - instaAlpha))

    val ytAlpha = ((cardAnimVal - 0.45f) / 0.35f).coerceIn(0f, 1f)
    val ytTranslationY = (30f * (1f - ytAlpha))

    // Micro animation trigger for task verification
    var showCelebration by remember { mutableStateOf(false) }
    val celebrationScale = remember { Animatable(0.8f) }

    fun triggerVerificationCelebration() {
        coroutineScope.launch {
            showCelebration = true
            celebrationScale.snapTo(0.7f)
            celebrationScale.animateTo(
                targetValue = 1.15f,
                animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessHigh)
            )
            celebrationScale.animateTo(
                targetValue = 1.0f,
                animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMedium)
            )
            delay(800) // Under 1 second micro celebration
            showCelebration = false
        }
    }

    // One-time entrance animation sequence
    val headerAnimProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        headerAnimProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp)
                .graphicsLayer {
                    alpha = headerAnimProgress.value
                    translationY = (1f - headerAnimProgress.value) * 30f
                }
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // ==================================================
            // 1. TOP HEADER & BRANDING BAR (PREMIUM UNIFIED GLASS HEADER)
            // ==================================================
            val animVal = headerAnimProgress.value

            val logoScale = 0.75f + (0.25f * (animVal / 0.35f).coerceIn(0f, 1f))
            val logoPulseGlow = if (animVal in 0.25f..0.65f) ((1f - Math.abs(animVal - 0.45f) / 0.2f) * 0.5f) else 0f
            val titleAlpha = ((animVal - 0.2f) / 0.35f).coerceIn(0f, 1f)
            val taglineAlpha = ((animVal - 0.35f) / 0.35f).coerceIn(0f, 1f)

            // Continuous 60 FPS micro-animations
            val headerInfiniteTransition = rememberInfiniteTransition(label = "academyHeaderAnims")
            val logoBreathingAlpha by headerInfiniteTransition.animateFloat(
                initialValue = 0.35f,
                targetValue = 0.85f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2200, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "logoBreathingAlpha"
            )
            val headerShimmerOffset by headerInfiniteTransition.animateFloat(
                initialValue = -300f,
                targetValue = 900f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3800, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "headerShimmerOffset"
            )
            val logoFloatY by headerInfiniteTransition.animateFloat(
                initialValue = -1.5f,
                targetValue = 1.5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2600, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "logoFloatY"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp)
                    .shadow(
                        elevation = 14.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = EmeraldPrimary.copy(alpha = 0.35f),
                        ambientColor = Color.Black
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF131A16), Color(0xFF0A0F0D))
                        )
                    )
                    .border(
                        BorderStroke(
                            1.2.dp,
                            Brush.linearGradient(
                                colors = listOf(
                                    EmeraldPrimary.copy(alpha = logoBreathingAlpha),
                                    ElectricPurple.copy(alpha = 0.45f),
                                    EmeraldGlow.copy(alpha = logoBreathingAlpha)
                                ),
                                start = Offset(headerShimmerOffset, 0f),
                                end = Offset(headerShimmerOffset + 400f, 250f)
                            )
                        ),
                        RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                // Glass reflection shimmer sweep line across container
                Canvas(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(24.dp))
                ) {
                    val sweepX = headerShimmerOffset
                    drawLine(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.03f),
                                Color.White.copy(alpha = 0.12f),
                                Color.White.copy(alpha = 0.03f),
                                Color.Transparent
                            )
                        ),
                        start = Offset(sweepX, 0f),
                        end = Offset(sweepX + 180f, size.height),
                        strokeWidth = 30.dp.toPx()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left branding: Logo + Title + Tagline
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // CREATOR ACADEMY AI LOGO WITH GLASS MORPHISM & GREEN GLOW
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.graphicsLayer {
                                translationY = logoFloatY.dp.toPx()
                            }
                        ) {
                            // Soft outer green neon aura ring
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                EmeraldPrimary.copy(alpha = (logoBreathingAlpha * 0.45f) + logoPulseGlow),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )

                            // High-resolution Glassmorphism Icon Box
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .graphicsLayer {
                                        scaleX = logoScale
                                        scaleY = logoScale
                                    }
                                    .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = EmeraldPrimary)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF1B2820), Color(0xFF0D1610))
                                        )
                                    )
                                    .border(
                                        BorderStroke(
                                            1.2.dp,
                                            Brush.linearGradient(
                                                listOf(
                                                    EmeraldPrimary.copy(alpha = logoBreathingAlpha),
                                                    EmeraldGlow
                                                )
                                            )
                                        ),
                                        RoundedCornerShape(14.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = "Creator Academy AI Logo",
                                    tint = EmeraldGlow,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // CREATOR ACADEMY AI TITLE & TAGLINE
                        Column {
                            Text(
                                text = "Creator Academy AI",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Black,
                                style = androidx.compose.ui.text.TextStyle(
                                    brush = Brush.horizontalGradient(
                                        listOf(
                                            TextWhite,
                                            Color(0xFFE2F3EB),
                                            EmeraldGlow
                                        )
                                    )
                                ),
                                letterSpacing = (-0.3).sp,
                                modifier = Modifier.graphicsLayer {
                                    alpha = titleAlpha
                                }
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Learn. Create. Grow.",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = EmeraldPrimary,
                                letterSpacing = 1.6.sp,
                                modifier = Modifier.graphicsLayer {
                                    alpha = taglineAlpha
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==================================================
            // 2. FEATURED CREATOR COURSES (INSTAGRAM & YOUTUBE)
            // ==================================================
            Text(
                text = "FEATURED CREATOR COURSES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite.copy(alpha = 0.5f),
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // CARD 1: INSTAGRAM CREATOR
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = instaAlpha
                        translationY = instaTranslationY.dp.toPx()
                    }
            ) {
                AcademyCourseCard(
                    title = "Instagram Creator",
                    logoName = "instagram",
                    accentColor = Color(0xFFE1306C),
                    features = listOf(
                        "Learn Instagram Growth",
                        "Reels & Viral Hooks",
                        "Brand Deals & Sponsorships",
                        "Creator Journey Blueprint",
                        "Monetization Strategies"
                    ),
                    onStartLearning = {
                        showInstagramCreatorV2Dialog = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // CARD 2: YOUTUBE CREATOR
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = ytAlpha
                        translationY = ytTranslationY.dp.toPx()
                    }
            ) {
                AcademyCourseCard(
                    title = "YouTube Creator",
                    logoName = "youtube",
                    accentColor = Color(0xFFFF0000),
                    features = listOf(
                        "YouTube Growth & Algorithm",
                        "SEO, Titles & Tagging",
                        "Long Form & Thumbnail Secrets",
                        "YouTube Shorts Strategy",
                        "Monetization & AdSense"
                    ),
                    onStartLearning = {
                        showYouTubeCreatorV2Dialog = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // CARD 3: AI VIDEO & IMAGES GENERATOR
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = ytAlpha
                        translationY = ytTranslationY.dp.toPx()
                    }
            ) {
                AcademyCourseCard(
                    title = "AI Prompt Extractor",
                    subtitleText = "AI Vision Analysis & Style Recreation Prompts",
                    tagText = "✨ AI VISION",
                    logoName = "chatgpt",
                    accentColor = Color(0xFF8B5CF6),
                    features = listOf(
                        "AI Video Generation from Zero using Free AI Tools",
                        "Professional AI Image Creation & Prompt Writing",
                        "Talking AI Avatars, Animate Photos & Motion Controls",
                        "Viral YouTube Thumbnail Psychology & High CTR Rules",
                        "Step-by-Step Tool Setup & Beginner Guide"
                    ),
                    onStartLearning = {
                        showAiVideoImageGeneratorDialog = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==================================================
            // 5. AI CREATOR TOOLKIT (CONTEXT-AWARE)
            // ==================================================
            AiCreatorToolsSection(
                setupData = setupData,
                onOpenTool = { toolName ->
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    activeToolDialog = toolName
                }
            )
        }

        // Active Tool Dialogs
        when (activeToolDialog) {
            "Caption Generator" -> CaptionGeneratorDialog(setupData = setupData, onDismiss = { activeToolDialog = null })
            "Hashtag Generator" -> HashtagGeneratorDialog(setupData = setupData, onDismiss = { activeToolDialog = null })
            "Hook Generator" -> HookGeneratorDialog(setupData = setupData, onDismiss = { activeToolDialog = null })
            "Content Planner" -> ContentPlannerDialog(setupData = setupData, onDismiss = { activeToolDialog = null })
            "Posting Checklist" -> PostingChecklistDialog(onDismiss = { activeToolDialog = null })
            "Brand Pitch Guide" -> BrandPitchGuideDialog(setupData = setupData, onDismiss = { activeToolDialog = null })
        }

        // Active Link Analysis Dialogs
        if (activeLinkDialog != null) {
            LinkAnalysisDialog(type = activeLinkDialog!!, onDismiss = { activeLinkDialog = null })
        }

        // Instagram Creator AI V2 Personal Mentor Dialog
        if (showInstagramCreatorV2Dialog) {
            com.example.ui.components.InstagramCreatorAiV2Dialog(
                onDismiss = { showInstagramCreatorV2Dialog = false }
            )
        }

        // YouTube Creator AI V2 Personal Mentor Dialog
        if (showYouTubeCreatorV2Dialog) {
            com.example.ui.components.YouTubeCreatorAiV2Dialog(
                onDismiss = { showYouTubeCreatorV2Dialog = false }
            )
        }

        // AI Video & Images Generator Dialog
        if (showAiVideoImageGeneratorDialog) {
            com.example.ui.components.AiVideoImageGeneratorDialog(
                onDismiss = { showAiVideoImageGeneratorDialog = false }
            )
        }

        // Placeholder Course Dialog
        coursePlaceholderTitle?.let { title ->
            CoursePlaceholderDialog(
                courseName = title,
                onDismiss = { coursePlaceholderTitle = null }
            )
        }

        // ==================================================
        // MICRO-CELEBRATION ANIMATION OVERLAY (<1s Apple-inspired)
        // ==================================================
        if (showCelebration) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .scale(celebrationScale.value)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.radialGradient(
                                listOf(EmeraldPrimary, Color(0xFF0F382A), Color(0xFF13131E))
                            )
                        )
                        .border(BorderStroke(2.dp, EmeraldPrimary), RoundedCornerShape(24.dp))
                        .padding(horizontal = 32.dp, vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success",
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "+100 XP EARNED!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "🎯 Lesson Unlocked • Streak Active!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // ==================================================
        // LOCKED PREMIUM POPUP DIALOG
        // ==================================================
        if (showVideoEditingLockedDialog) {
            VideoEditingLockedDialog(
                onDismiss = { showVideoEditingLockedDialog = false }
            )
        }

        if (showBrandCollabDialog) {
            com.example.creatoracademy.BrandCollaborationAiDialog(
                onDismiss = { showBrandCollabDialog = false }
            )
        }

        selectedPremiumTool?.let { tool ->
            if (tool.id == "brand_collab_ai") {
                com.example.creatoracademy.BrandCollaborationAiDialog(
                    onDismiss = { selectedPremiumTool = null }
                )
            } else {
                com.example.ui.components.CommonPremiumToolPopupDialog(
                    tool = tool,
                    onDismiss = { selectedPremiumTool = null }
                )
            }
        }
    }
}

// ====================================================================
// SMART REMINDER CARD
// ====================================================================
@Composable
private fun SmartReminderCard(
    taskNumber: Int,
    taskTitle: String,
    platform: String,
    onContinue: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(18.dp), spotColor = EmeraldPrimary)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF0A2E23), Color(0xFF131322))
                )
            )
            .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Welcome Back",
                            tint = AmoledBlack,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "WELCOME BACK, CREATOR!",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = EmeraldPrimary,
                        letterSpacing = 1.sp
                    )
                }

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = TextWhite.copy(alpha = 0.5f),
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onDismiss() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ready to continue Lesson #$taskNumber ($platform)?",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Text(
                text = taskTitle,
                fontSize = 12.sp,
                color = TextWhite.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(EmeraldPrimary)
                        .clickable { onContinue() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "CONTINUE LESSON",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = AmoledBlack
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = AmoledBlack,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0x15FFFFFF))
                        .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(18.dp))
                        .clickable { onDismiss() }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "LATER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

// ====================================================================
// DYNAMIC AI MENTOR DASHBOARD CARD
// ====================================================================
@Composable
private fun MentorDashboardCard(
    xpPoints: Int,
    streakDays: Int,
    setupData: com.example.creatoracademy.CreatorSetupData,
    selectedPlatform: String,
    currentTaskIndex: Int,
    totalTasks: Int,
    currentTask: AiMentorTask?
) {
    val creatorLevel = CreatorLevel.getLevelForXp(xpPoints)
    val levelProgress = ((xpPoints - creatorLevel.minXp).toFloat() / (creatorLevel.maxXp - creatorLevel.minXp).toFloat())
        .coerceIn(0f, 1f)

    val overallProgressPercent = if (totalTasks > 0) {
        ((currentTaskIndex.toFloat() / totalTasks.toFloat()) * 100).toInt().coerceAtMost(100)
    } else 0

    val remainingTasks = (totalTasks - currentTaskIndex).coerceAtLeast(0)
    val estCompletionText = if (remainingTasks > 0) "$remainingTasks lessons left (~${remainingTasks * 5} mins)" else "All lessons completed!"

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = creatorLevel.color.copy(alpha = 0.5f),
        backgroundColor = Color(0x1210B981)
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
            // Header Row: Level Badge & XP
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(creatorLevel.color),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = creatorLevel.icon,
                            contentDescription = creatorLevel.name,
                            tint = AmoledBlack,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = creatorLevel.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${creatorLevel.badgeName} • ${setupData.skillLevel}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = creatorLevel.color,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Streak Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x22FF9800))
                            .border(BorderStroke(1.dp, Color(0x66FF9800)), RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = Color(0xFFFF9800),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "$streakDays Days",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFF9800)
                            )
                        }
                    }

                    // XP Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x2210B981))
                            .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)), RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$xpPoints XP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = EmeraldPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // Mentor Dashboard Metrics Grid
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Today's Mission & Current Skill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "TODAY'S MISSION",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite.copy(alpha = 0.5f),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = currentTask?.title ?: "Mastery Achieved",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "CURRENT SKILL",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite.copy(alpha = 0.5f),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = currentTask?.skillCategory ?: "Advanced Strategy",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary,
                            maxLines = 1
                        )
                    }
                }

                // Goal & Est Completion
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Goal: ${setupData.primaryGoal}",
                        fontSize = 11.sp,
                        color = TextWhite.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = estCompletionText,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextWhite.copy(alpha = 0.5f)
                    )
                }

                // Overall Roadmap Progress Bar
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Roadmap Progress ($selectedPlatform)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "$overallProgressPercent%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = EmeraldPrimary
                    )
                }

                LinearProgressIndicator(
                    progress = { overallProgressPercent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = EmeraldPrimary,
                    trackColor = Color(0x22FFFFFF)
                )
            }
        }
    }
}

// ====================================================================
// PLATFORM OPTION CARD
// ====================================================================
@Composable
private fun PlatformOptionCard(
    title: String,
    subtitle: String,
    badge: String,
    isSelected: Boolean,
    isLocked: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "platformOptScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) Color(0x2210B981) else Color(0x0AFFFFFF))
            .border(
                BorderStroke(if (isSelected) 1.5.dp else 1.dp, if (isSelected) EmeraldPrimary else Color(0x1AFFFFFF)),
                RoundedCornerShape(18.dp)
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (isLocked) Color(0x11FFFFFF) else if (isSelected) EmeraldPrimary else Color(0x1AFFFFFF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isLocked) Icons.Default.Lock else if (isSelected) Icons.Default.Check else Icons.Default.Videocam,
                    contentDescription = title,
                    tint = if (isLocked) TextWhite.copy(alpha = 0.5f) else if (isSelected) AmoledBlack else TextWhite,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isLocked) TextWhite.copy(alpha = 0.7f) else TextWhite
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isLocked) Color(0x22FF5252) else if (isSelected) Color(0x3310B981) else Color(0x1AFFFFFF))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isLocked) Color(0xFFFF5252) else if (isSelected) EmeraldPrimary else TextWhite.copy(alpha = 0.6f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextWhite.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// ====================================================================
// AI COACH TASK & VERIFICATION ENGINE CARD
// ====================================================================
@Composable
private fun AiCoachTaskCard(
    task: AiMentorTask,
    totalTasks: Int,
    onVerifyYes: () -> Unit,
    onSkipTask: () -> Unit
) {
    var showNotYetOptions by remember(task.id) { mutableStateOf(false) }
    var showDetailedExplanation by remember(task.id) { mutableStateOf(false) }
    var showExampleLibrary by remember(task.id) { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(22.dp), spotColor = EmeraldPrimary)
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF13131E))
            .border(
                BorderStroke(1.2.dp, Brush.linearGradient(listOf(EmeraldPrimary.copy(alpha = 0.6f), Color(0x1AFFFFFF)))),
                RoundedCornerShape(22.dp)
            )
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Bar: AI Coach Mode
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x2210B981)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "AI Coach",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "AI Mentor Coach",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "Step ${task.stepNumber} of $totalTasks • ${task.skillCategory}",
                            fontSize = 10.5.sp,
                            color = TextWhite.copy(alpha = 0.6f)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x2210B981))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "CURRENT LESSON",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = EmeraldPrimary,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Conversational Mentor Speech Bubble
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x0DFFFFFF))
                    .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f)), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = task.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = task.coachMessage,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextWhite.copy(alpha = 0.9f),
                        lineHeight = 18.sp
                    )
                }
            }

            // Detailed Explanation Accordion (if expanded by Explain Again)
            AnimatedVisibility(
                visible = showDetailedExplanation,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x1800E5FF))
                            .border(BorderStroke(1.dp, Color(0x4400E5FF)), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = Color(0xFF00E5FF),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Deep-Dive Mentor Explanation",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00E5FF)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = task.detailExplanation,
                                fontSize = 12.sp,
                                color = TextWhite.copy(alpha = 0.85f),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ==================================================
            // EXAMPLE LIBRARY (GOOD vs BAD, PRO TIP, COMMON MISTAKE)
            // ==================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showExampleLibrary = !showExampleLibrary },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Example Library",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "EXAMPLE LIBRARY & PRO TIPS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        letterSpacing = 0.8.sp
                    )
                }

                Icon(
                    imageVector = if (showExampleLibrary) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            AnimatedVisibility(
                visible = showExampleLibrary,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Good Example Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x1510B981))
                            .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f)), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ThumbUp,
                                    contentDescription = "Good Example",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "GOOD EXAMPLE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = EmeraldPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = task.goodExample,
                                fontSize = 11.5.sp,
                                color = TextWhite,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    // Bad Example Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x15FF5252))
                            .border(BorderStroke(1.dp, Color(0x44FF5252)), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ThumbDown,
                                    contentDescription = "Bad Example",
                                    tint = Color(0xFFFF5252),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "BAD EXAMPLE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFF5252)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = task.badExample,
                                fontSize = 11.5.sp,
                                color = TextWhite.copy(alpha = 0.85f),
                                lineHeight = 15.sp
                            )
                        }
                    }

                    // Pro Tip & Common Mistake Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Pro Tip
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x10FFD700))
                                .border(BorderStroke(1.dp, Color(0x33FFD700)), RoundedCornerShape(12.dp))
                                .padding(8.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Pro Tip",
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "PRO TIP",
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFFFD700)
                                    )
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = task.proTip,
                                    fontSize = 10.5.sp,
                                    color = TextWhite.copy(alpha = 0.9f),
                                    lineHeight = 14.sp
                                )
                            }
                        }

                        // Common Mistake
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x10FF9800))
                                .border(BorderStroke(1.dp, Color(0x33FF9800)), RoundedCornerShape(12.dp))
                                .padding(8.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Common Mistake",
                                        tint = Color(0xFFFF9800),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "PITFALL TO AVOID",
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFFF9800)
                                    )
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = task.commonMistake,
                                    fontSize = 10.5.sp,
                                    color = TextWhite.copy(alpha = 0.9f),
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // ==================================================
            // TASK VERIFICATION PROMPT
            // ==================================================
            Text(
                text = "HAVE YOU COMPLETED THIS TASK?",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite.copy(alpha = 0.8f),
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // YES DONE BUTTON (+100 XP)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .shadow(elevation = 6.dp, shape = RoundedCornerShape(23.dp), spotColor = EmeraldPrimary)
                        .clip(RoundedCornerShape(23.dp))
                        .background(EmeraldPrimary)
                        .clickable { onVerifyYes() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Done",
                            tint = AmoledBlack,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "YES, DONE! (+100 XP)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = AmoledBlack
                        )
                    }
                }

                // NOT YET BUTTON
                Box(
                    modifier = Modifier
                        .height(46.dp)
                        .clip(RoundedCornerShape(23.dp))
                        .background(Color(0x15FFFFFF))
                        .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(23.dp))
                        .clickable { showNotYetOptions = !showNotYetOptions }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (showNotYetOptions) "HIDE OPTIONS" else "NOT YET",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite.copy(alpha = 0.8f)
                    )
                }
            }

            // Expanded NOT YET options: Explain Again, Show Example, Skip
            AnimatedVisibility(
                visible = showNotYetOptions,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Need help or want to skip?",
                        fontSize = 11.sp,
                        color = TextWhite.copy(alpha = 0.6f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Option 1: Explain Again
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x1A00E5FF))
                                .border(BorderStroke(1.dp, Color(0x4400E5FF)), RoundedCornerShape(12.dp))
                                .clickable {
                                    showDetailedExplanation = true
                                    showNotYetOptions = false
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.HelpOutline,
                                    contentDescription = null,
                                    tint = Color(0xFF00E5FF),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Explain Again",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00E5FF)
                                )
                            }
                        }

                        // Option 2: Show Example
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x1A10B981))
                                .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f)), RoundedCornerShape(12.dp))
                                .clickable {
                                    showExampleLibrary = true
                                    showNotYetOptions = false
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Show Example",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                            }
                        }

                        // Option 3: Skip Task
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x1AFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(12.dp))
                                .clickable {
                                    showNotYetOptions = false
                                    onSkipTask()
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FastForward,
                                    contentDescription = null,
                                    tint = TextWhite.copy(alpha = 0.8f),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Skip Task",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ====================================================================
// MASTERY COMPLETED CARD
// ====================================================================
@Composable
private fun MasteryCompletedCard(selectedPlatform: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0x1510B981))
            .border(BorderStroke(1.5.dp, EmeraldPrimary), RoundedCornerShape(22.dp))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(EmeraldPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Trophy",
                    tint = AmoledBlack,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "🏆 MASTERY CLASS COMPLETED!",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = EmeraldPrimary,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "You have completed all foundational $selectedPlatform mentor lessons! Check back for advanced growth modules.",
                fontSize = 12.sp,
                color = TextWhite.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}

// ====================================================================
// PERSONALIZED CREATOR ROADMAP SECTION (WITH TASK STATES)
// ====================================================================
@Composable
private fun PersonalizedRoadmapSection(
    mentorTasks: List<AiMentorTask>,
    selectedPlatform: String,
    currentTaskIndex: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "PERSONALIZED MENTOR ROADMAP ($selectedPlatform)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite.copy(alpha = 0.5f),
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Map tasks into 4 weeks
        val weeks = listOf(
            Triple("Week 1: Profile & Bio Positioning", "Foundation, High-converting Bio & Niche Focus", mentorTasks.take(2)),
            Triple("Week 2: Content Strategy & Viral Hooks", "Pillars, 3-sec Hook Formulas & Scripts", mentorTasks.drop(2).take(2)),
            Triple("Week 3: Algorithm & Research", "Competitor Audits, Audio Trends & Schedules", mentorTasks.drop(4).take(2)),
            Triple("Week 4: SEO, Captions & Growth Pushes", "Metadata Stacks, Hashtags & Algorithm Mastery", mentorTasks.drop(6))
        )

        weeks.forEachIndexed { weekIndex, (weekTitle, weekDesc, weekTasks) ->
            var isExpanded by remember { mutableStateOf(weekIndex == 0 || weekTasks.any { it.state == TaskState.CURRENT }) }
            val isWeekCompleted = weekTasks.isNotEmpty() && weekTasks.all { it.state == TaskState.COMPLETED || it.state == TaskState.SKIPPED }

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                borderColor = if (isWeekCompleted) Color(0x4410B981) else Color(0x1F2C2C2C),
                backgroundColor = if (isWeekCompleted) Color(0x1210B981) else Color(0x08FFFFFF),
                onClick = { isExpanded = !isExpanded }
            ) {
                Column(modifier = Modifier.padding(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(if (isWeekCompleted) EmeraldPrimary else Color(0x15FFFFFF)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isWeekCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Done",
                                        tint = AmoledBlack,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else {
                                    Text(
                                        text = "${weekIndex + 1}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = weekTitle,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = weekDesc,
                                    fontSize = 10.5.sp,
                                    color = TextWhite.copy(alpha = 0.5f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Expand",
                            tint = TextWhite.copy(alpha = 0.6f),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    AnimatedVisibility(visible = isExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp)

                            weekTasks.forEach { task ->
                                TaskItemStateRow(task = task)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ====================================================================
// TASK ITEM STATE ROW (SHOWS LOCKED, CURRENT, COMPLETED, SKIPPED)
// ====================================================================
@Composable
private fun TaskItemStateRow(task: AiMentorTask) {
    val (bgColor, borderColor, textColor, icon, stateLabel) = when (task.state) {
        TaskState.COMPLETED -> Tuple5(
            Color(0x1510B981), Color(0x3310B981), EmeraldPrimary, Icons.Default.Check, "COMPLETED"
        )
        TaskState.SKIPPED -> Tuple5(
            Color(0x0AFFFFFF), Color(0x1AFFFFFF), TextWhite.copy(alpha = 0.5f), Icons.Default.FastForward, "SKIPPED"
        )
        TaskState.CURRENT -> Tuple5(
            Color(0x2210B981), EmeraldPrimary, TextWhite, Icons.Default.AutoAwesome, "ACTIVE LESSON"
        )
        TaskState.LOCKED -> Tuple5(
            Color(0x05FFFFFF), Color(0x10FFFFFF), TextWhite.copy(alpha = 0.4f), Icons.Default.Lock, "LOCKED"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(borderColor.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = stateLabel,
                    tint = if (task.state == TaskState.CURRENT) EmeraldPrimary else textColor,
                    modifier = Modifier.size(14.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Lesson #${task.stepNumber}: ${task.title}",
                    fontSize = 12.sp,
                    fontWeight = if (task.state == TaskState.CURRENT) FontWeight.Bold else FontWeight.Medium,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${task.skillCategory} • +${task.xpReward} XP",
                    fontSize = 10.sp,
                    color = textColor.copy(alpha = 0.6f)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(borderColor.copy(alpha = 0.2f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = stateLabel,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
        }
    }
}

private data class Tuple5<A, B, C, D, E>(
    val a: A, val b: B, val c: C, val d: D, val e: E
)

@Composable
fun VideoEditingLockedDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("FEATURES") } // FEATURES, TERMS, PRIVACY, REFUND

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF141420),
            border = BorderStroke(1.2.dp, Brush.linearGradient(listOf(Color(0xFFFF5252), EmeraldPrimary))),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FF5252)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color(0xFFFF5252),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Mobile Video Editing AI",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x2210B981))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "🔒 Premium • Coming Soon",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x10FFFFFF))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Early Access Price Placeholder: ₹99 / Lifetime Pass",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tabs for Terms, Privacy, Refund
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val tabs = listOf("FEATURES", "TERMS", "PRIVACY", "REFUND")
                    tabs.forEach { tab ->
                        Text(
                            text = tab,
                            fontSize = 10.sp,
                            fontWeight = if (selectedTab == tab) FontWeight.Black else FontWeight.Medium,
                            color = if (selectedTab == tab) EmeraldPrimary else TextWhite.copy(alpha = 0.5f),
                            modifier = Modifier
                                .clickable { selectedTab = tab }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x0AFFFFFF))
                        .padding(10.dp)
                ) {
                    val infoText = when (selectedTab) {
                        "TERMS" -> "Terms of Service: Premium features will activate upon official release. Lifetime access covers all future AI mobile video updates."
                        "PRIVACY" -> "Privacy Guarantee: No video recordings or personal media files are ever transmitted to third parties without permission."
                        "REFUND" -> "100% 7-Day Money Back Refund Guarantee applies automatically upon release if unsatisfied with the tool."
                        else -> "Features Preview: Pro CapCut Templates, AI Auto-Captioning Generator, Premiere LUTs, VN Speed Curves & One-tap Auto Cut."
                    }
                    Text(
                        text = infoText,
                        fontSize = 11.sp,
                        color = TextWhite.copy(alpha = 0.8f),
                        lineHeight = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(EmeraldPrimary)
                        .clickable {
                            Toast.makeText(context, "🎉 Registered for Early Access Notification!", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Notify Me On Release",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmoledBlack
                    )
                }
            }
        }
    }
}

// ====================================================================
// ACADEMY COURSE CARD (PREMIUM GLASS & SHINE SWEEP)
// ====================================================================
@Composable
private fun AcademyCourseCard(
    title: String,
    logoName: String,
    accentColor: Color,
    features: List<String>,
    subtitleText: String = "PREMIUM COURSE",
    tagText: String = "✨ FEATURED",
    onStartLearning: () -> Unit
) {
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "courseCardScale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "courseCardShimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "courseCardShimmerOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = accentColor.copy(alpha = 0.4f),
                ambientColor = Color.Black
            )
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF181824),
                        Color(0xFF10101A)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.2.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.85f),
                            accentColor.copy(alpha = 0.35f),
                            accentColor.copy(alpha = 0.75f)
                        ),
                        start = Offset(shimmerOffset, 0f),
                        end = Offset(shimmerOffset + 400f, 300f)
                    )
                ),
                RoundedCornerShape(24.dp)
            )
            .padding(18.dp)
    ) {
        // Shine Sweep Overlay
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(24.dp))
        ) {
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.04f),
                        Color.White.copy(alpha = 0.12f),
                        Color.White.copy(alpha = 0.04f),
                        Color.Transparent
                    )
                ),
                start = Offset(shimmerOffset, 0f),
                end = Offset(shimmerOffset + 180f, size.height),
                strokeWidth = 32.dp.toPx()
            )
        }

        val heroType = when (logoName) {
            "instagram" -> com.example.ui.components.ToolHeroType.INSTAGRAM_CREATOR
            "youtube" -> com.example.ui.components.ToolHeroType.YOUTUBE_CREATOR
            "chatgpt" -> com.example.ui.components.ToolHeroType.AI_PROMPT_EXTRACTOR
            "capcut" -> com.example.ui.components.ToolHeroType.CAPCUT_MASTER
            "vn" -> com.example.ui.components.ToolHeroType.VN_EDITOR
            "meesho" -> com.example.ui.components.ToolHeroType.MEESHO_CREATOR
            else -> com.example.ui.components.ToolHeroType.BRAND_COLLAB
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            com.example.ui.components.ToolHeroBanner(
                toolType = heroType,
                height = 110.dp,
                badgeText = tagText,
                subtitleText = subtitleText
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Header Row: Logo + Title + Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.18f))
                            .border(BorderStroke(1.dp, accentColor.copy(alpha = 0.6f)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        OfficialLogo(name = logoName, modifier = Modifier.size(20.dp))
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = title,
                            fontSize = 17.5.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = (-0.3).sp
                        )
                        Text(
                            text = subtitleText,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldGlow,
                            letterSpacing = 1.2.sp,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x2210B981))
                        .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = tagText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGlow
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bullet points description
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                features.forEach { feature ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = EmeraldGlow,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = feature,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextWhite.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Start Learning Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .shadow(8.dp, RoundedCornerShape(23.dp), spotColor = EmeraldPrimary)
                    .clip(RoundedCornerShape(23.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(EmeraldPrimary, EmeraldGlow)
                        )
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onStartLearning()
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = AmoledBlack,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "START LEARNING",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Black,
                        color = AmoledBlack,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }
    }
}

// ====================================================================
// COURSE PLACEHOLDER DIALOG
// ====================================================================
@Composable
private fun CoursePlaceholderDialog(
    courseName: String,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF141A16),
            border = BorderStroke(1.2.dp, Brush.linearGradient(listOf(EmeraldPrimary, EmeraldGlow))),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(EmeraldPrimary.copy(alpha = 0.2f))
                        .border(BorderStroke(1.dp, EmeraldGlow), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = EmeraldGlow,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "$courseName Course",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(EmeraldPrimary.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "🚀 NEXT PHASE LAUNCH",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGlow
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Interactive video lessons, growth blueprints, reels templates & monetization tools for $courseName will launch in the next phase! Your learning progress will be saved automatically.",
                    fontSize = 12.5.sp,
                    color = TextWhite.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Brush.horizontalGradient(listOf(EmeraldPrimary, EmeraldGlow)))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "GOT IT",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = AmoledBlack,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }
    }
}
