package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.ui.components.EditingToolType
import com.example.ui.components.VideoEditingMentorAiDialog
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite

/**
 * PHASE — VIDEO EDITING ACADEMY V1 (PREMIUM UI SETUP)
 *
 * Glass-morphism, floating cards, frosted blur, neon border glow, and iPhone-style micro animations
 * matching Creator Academy & Home Screen design language.
 */
@Composable
fun VideoEditingScreen(
    onNavigateToHome: () -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Active AI Mentor Tool (CapCut, VN, Instagram Edits)
    var activeMentorTool by remember { mutableStateOf<EditingToolType?>(null) }

    // Entrance animation
    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
        )
    }

    // Continuous 60 FPS micro-animations
    val infiniteTransition = rememberInfiniteTransition(label = "videoAcademyAnims")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            animation = tween(3800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )
    val floatHeaderY by infiniteTransition.animateFloat(
        initialValue = -1.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatHeaderY"
    )
    val logoBreathingAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoBreathingAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0C140F),
                        AmoledBlack,
                        Color(0xFF0A100C)
                    )
                )
            )
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 110.dp)
                .graphicsLayer {
                    alpha = animProgress.value
                    translationY = (1f - animProgress.value) * 30f
                }
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // ==================================================
            // 1. TOP NAVBAR / HEADER BANNER (UNIFIED GLASS HEADER)
            // ==================================================
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
                                start = Offset(shimmerOffset, 0f),
                                end = Offset(shimmerOffset + 400f, 250f)
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
                    val sweepX = shimmerOffset
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.graphicsLayer {
                                translationY = floatHeaderY.dp.toPx()
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
                                                EmeraldPrimary.copy(alpha = logoBreathingAlpha * 0.45f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )

                            // High-resolution Glassmorphism Icon Box
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
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
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = "Video Editing Academy Logo",
                                    tint = EmeraldGlow,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Video Editing Academy",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Black,
                                style = androidx.compose.ui.text.TextStyle(
                                    brush = Brush.horizontalGradient(
                                        listOf(TextWhite, Color(0xFFE2F3EB), EmeraldGlow)
                                    )
                                ),
                                letterSpacing = (-0.3).sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Mobile Reels & Shorts Mastery",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = EmeraldPrimary,
                                letterSpacing = 1.6.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==================================================
            // 2. FEATURED COURSES SECTION TITLE
            // ==================================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(EmeraldGlow)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "FEATURED VIDEO EDITING COURSES",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        letterSpacing = 1.2.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x1A10B981))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "3 COURSES",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGlow
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==================================================
            // 3. THREE FEATURED COURSES CARDS
            // ==================================================
            // TOOL 1: CapCut Master (⭐ MOST USED)
            EditorCourseCard(
                cardIndex = 0,
                logoType = EditorLogoType.CAPCUT,
                title = "CapCut Master",
                subtitle = "Professional Short Form Video Editing",
                badgeText = "⭐ MOST USED",
                badgeStyle = BadgeStyle.ORANGE_MOST_USED,
                features = listOf(
                    "Beginner Friendly",
                    "Viral Reels Editing",
                    "Auto Captions",
                    "Effects & Transitions",
                    "Export Settings"
                ),
                primaryAccentColor = Color(0xFFFF6B00),
                buttonText = "START LEARNING",
                onStartClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    activeMentorTool = EditingToolType.CAPCUT
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TOOL 2: VN Video Editor
            EditorCourseCard(
                cardIndex = 1,
                logoType = EditorLogoType.VN,
                title = "VN Video Editor",
                subtitle = "Professional Editing Without Watermark",
                badgeText = "FREE PREMIUM",
                badgeStyle = BadgeStyle.BLUE_GLASS,
                features = listOf(
                    "Timeline Editing",
                    "Keyframes",
                    "Audio Mixing",
                    "Text Animation",
                    "Cinematic Export"
                ),
                primaryAccentColor = Color(0xFF00B2FF),
                buttonText = "START LEARNING",
                onStartClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    activeMentorTool = EditingToolType.VN
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TOOL 3: Instagram Edits
            EditorCourseCard(
                cardIndex = 2,
                logoType = EditorLogoType.INSTAGRAM,
                title = "Instagram Edits",
                subtitle = "Instagram Official Editing Workflow",
                badgeText = "FREE PREMIUM",
                badgeStyle = BadgeStyle.PURPLE_GLASS,
                features = listOf(
                    "Reels Editing",
                    "Instagram Export",
                    "Audio Sync",
                    "Trending Templates",
                    "Direct Publishing"
                ),
                primaryAccentColor = Color(0xFFA855F7),
                buttonText = "START LEARNING",
                onStartClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    activeMentorTool = EditingToolType.INSTAGRAM_EDITS
                }
            )
        }

        // Isolated Personal AI Mentor System Dialog
        activeMentorTool?.let { tool ->
            VideoEditingMentorAiDialog(
                toolType = tool,
                onDismiss = { activeMentorTool = null }
            )
        }
    }
}

enum class EditorLogoType {
    CAPCUT, VN, INSTAGRAM
}

enum class BadgeStyle {
    ORANGE_MOST_USED, BLUE_GLASS, PURPLE_GLASS
}

@Composable
private fun EditorCourseCard(
    cardIndex: Int,
    logoType: EditorLogoType,
    title: String,
    subtitle: String,
    badgeText: String,
    badgeStyle: BadgeStyle,
    features: List<String>,
    primaryAccentColor: Color,
    buttonText: String,
    onStartClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cardAnims_$cardIndex")

    // Card Floating Motion
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200 + cardIndex * 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatOffset"
    )

    // Glass Shine Sweep
    val shimmerPos by infiniteTransition.animateFloat(
        initialValue = -500f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            animation = tween(3800 + cardIndex * 200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerPos"
    )

    // Pulse animation for MOST USED CapCut badge
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    // Touch scale interaction
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = 350f),
        label = "cardScale"
    )

    Box(
        modifier = Modifier
            .graphicsLayer {
                translationY = floatOffset.dp.toPx()
                scaleX = cardScale
                scaleY = cardScale
            }
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(26.dp),
                spotColor = primaryAccentColor.copy(alpha = 0.35f),
                ambientColor = Color.Black
            )
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF15211B),
                        Color(0xFF0C1410)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.2.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            primaryAccentColor.copy(alpha = 0.8f),
                            ElectricPurple.copy(alpha = 0.4f),
                            EmeraldGlow.copy(alpha = 0.7f)
                        ),
                        start = Offset(shimmerPos, 0f),
                        end = Offset(shimmerPos + 350f, 250f)
                    )
                ),
                RoundedCornerShape(26.dp)
            )
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // HEADER ROW: LOGO, TITLE, SUBTITLE & BADGE
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Real Bespoke Logo
                    EditorLogoView(logoType = logoType, primaryColor = primaryAccentColor)

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = (-0.2).sp
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = subtitle,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextWhite.copy(alpha = 0.7f),
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // BADGE CAPSULE
                when (badgeStyle) {
                    BadgeStyle.ORANGE_MOST_USED -> {
                        Box(
                            modifier = Modifier
                                .graphicsLayer { alpha = pulseAlpha }
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFFFF3E0), Color(0xFFFF9800), Color(0xFFFF6B00))
                                    )
                                )
                                .border(
                                    BorderStroke(1.2.dp, Color(0xFFFFE0B2)),
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Most Used",
                                    tint = AmoledBlack,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = badgeText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = AmoledBlack,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    BadgeStyle.BLUE_GLASS -> {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0x3300B2FF), Color(0x2238BDF8))
                                    )
                                )
                                .border(
                                    BorderStroke(1.dp, Color(0xFF38BDF8)),
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = badgeText,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    BadgeStyle.PURPLE_GLASS -> {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0x33A855F7), Color(0x22C084FC))
                                    )
                                )
                                .border(
                                    BorderStroke(1.dp, Color(0xFFC084FC)),
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = badgeText,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // DIVIDER LINE WITH GLOW
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                primaryAccentColor.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 5 FEATURE BULLETS
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Check",
                                tint = EmeraldGlow,
                                modifier = Modifier.size(11.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = feature,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // START LEARNING BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(10.dp, RoundedCornerShape(25.dp), spotColor = EmeraldPrimary)
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                EmeraldPrimary,
                                EmeraldGlow
                            )
                        )
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onStartClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start",
                        tint = AmoledBlack,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = buttonText,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        color = AmoledBlack,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }
    }
}

/**
 * Renders authentic, high-precision bespoke logos for CapCut, VN Editor, and Instagram Edits.
 */
@Composable
private fun EditorLogoView(
    logoType: EditorLogoType,
    primaryColor: Color
) {
    Box(
        modifier = Modifier
            .size(54.dp)
            .shadow(10.dp, RoundedCornerShape(16.dp), spotColor = primaryColor)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F1712))
            .border(BorderStroke(1.2.dp, primaryColor), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            val w = size.width
            val h = size.height

            when (logoType) {
                EditorLogoType.CAPCUT -> {
                    // CapCut: Black circle with white bracket cut geometry
                    drawCircle(color = Color(0xFF111111), radius = w / 2f)
                    
                    // Left bracket path
                    val leftPath = Path().apply {
                        moveTo(w * 0.25f, h * 0.25f)
                        lineTo(w * 0.48f, h * 0.48f)
                        lineTo(w * 0.25f, h * 0.71f)
                    }
                    drawPath(
                        path = leftPath,
                        color = Color.White,
                        style = Stroke(width = 3.dp.toPx())
                    )

                    // Right bracket path
                    val rightPath = Path().apply {
                        moveTo(w * 0.75f, h * 0.25f)
                        lineTo(w * 0.52f, h * 0.48f)
                        lineTo(w * 0.75f, h * 0.71f)
                    }
                    drawPath(
                        path = rightPath,
                        color = Color.White,
                        style = Stroke(width = 3.dp.toPx())
                    )

                    // Center connecting bar
                    drawLine(
                        color = Color.White,
                        start = Offset(w * 0.35f, h * 0.5f),
                        end = Offset(w * 0.65f, h * 0.5f),
                        strokeWidth = 2.5.dp.toPx()
                    )
                }

                EditorLogoType.VN -> {
                    // VN Editor: Vibrant Blue background with crisp white VN scissor timeline mark
                    drawRoundRect(
                        color = Color(0xFF00B2FF),
                        cornerRadius = CornerRadius(8.dp.toPx())
                    )

                    // Stylized "V" path
                    val vPath = Path().apply {
                        moveTo(w * 0.22f, h * 0.28f)
                        lineTo(w * 0.40f, h * 0.72f)
                        lineTo(w * 0.54f, h * 0.28f)
                    }
                    drawPath(
                        path = vPath,
                        color = Color.White,
                        style = Stroke(width = 3.5.dp.toPx())
                    )

                    // Stylized "N" stroke
                    val nPath = Path().apply {
                        moveTo(w * 0.58f, h * 0.72f)
                        lineTo(w * 0.58f, h * 0.28f)
                        lineTo(w * 0.78f, h * 0.72f)
                        lineTo(w * 0.78f, h * 0.28f)
                    }
                    drawPath(
                        path = nPath,
                        color = Color.White,
                        style = Stroke(width = 3.dp.toPx())
                    )
                }

                EditorLogoType.INSTAGRAM -> {
                    // Instagram Edits: Instagram Gradient with white camera/film reel
                    drawCircle(
                        brush = Brush.linearGradient(
                            listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFFCB045))
                        ),
                        radius = w / 2f
                    )

                    // Camera Outer Box
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(w * 0.24f, h * 0.28f),
                        size = Size(w * 0.52f, h * 0.44f),
                        cornerRadius = CornerRadius(5.dp.toPx()),
                        style = Stroke(width = 2.dp.toPx())
                    )

                    // Camera Lens Circle
                    drawCircle(
                        color = Color.White,
                        radius = w * 0.11f,
                        center = Offset(w * 0.5f, h * 0.5f),
                        style = Stroke(width = 2.dp.toPx())
                    )

                    // Play Triangle inside Lens
                    val playPath = Path().apply {
                        moveTo(w * 0.46f, h * 0.43f)
                        lineTo(w * 0.57f, h * 0.50f)
                        lineTo(w * 0.46f, h * 0.57f)
                        close()
                    }
                    drawPath(path = playPath, color = Color.White)
                }
            }
        }
    }
}
