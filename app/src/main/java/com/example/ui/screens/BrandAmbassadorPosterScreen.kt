package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

object BrandAmbassadorPrefs {
    fun shouldShowPoster(context: Context): Boolean {
        // Show EVERY TIME the app opens
        return true
    }

    fun recordPosterShown(context: Context, dontShowAgain: Boolean = false) {
        // No-op - Always show every launch
    }
}

@Composable
fun BrandAmbassadorPosterScreen(
    onDismiss: () -> Unit,
    onExploreClicked: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val isPreviewMode = LocalInspectionMode.current

    var isExiting by remember { mutableStateOf(false) }

    // Screen entrance & ambient animations
    val screenAlpha = remember { Animatable(0f) }
    val cardOffsetY = remember { Animatable(120f) }
    val cardAlpha = remember { Animatable(0f) }
    val badgeAlpha = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val buttonScale = remember { Animatable(0.85f) }
    val buttonAlpha = remember { Animatable(0f) }

    // Soft zoom for poster image (100% to 102% max)
    val posterZoomScale = remember { Animatable(1.0f) }

    // Feature chips pop animation state
    var chipsVisibleCount by remember { mutableIntStateOf(0) }
    val featureList = listOf(
        "AI Learning",
        "Creator Growth",
        "Video Editing",
        "Brand Collaboration",
        "Affiliate Earnings"
    )

    // Countdown state (3s duration)
    var countdownSeconds by remember { mutableIntStateOf(3) }

    // Exit handler with Haptic & Smooth Exit Transition
    val handleExit = { isExplore: Boolean ->
        if (!isExiting) {
            isExiting = true
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            if (isExplore && onExploreClicked != null) {
                onExploreClicked()
            } else {
                onDismiss()
            }
        }
    }

    // Timeline Animations & 3 Second Auto Timer
    LaunchedEffect(Unit) {
        if (isPreviewMode) {
            // Preview Safe Mode: Immediately reveal all elements without delay or countdown loop
            screenAlpha.snapTo(1f)
            cardOffsetY.snapTo(0f)
            cardAlpha.snapTo(1f)
            badgeAlpha.snapTo(1f)
            titleAlpha.snapTo(1f)
            subtitleAlpha.snapTo(1f)
            buttonAlpha.snapTo(1f)
            buttonScale.snapTo(1f)
            chipsVisibleCount = featureList.size
            return@LaunchedEffect
        }

        // Soft Zoom over 3 seconds (1.0 -> 1.02 max)
        launch {
            posterZoomScale.animateTo(
                targetValue = 1.02f,
                animationSpec = tween(durationMillis = 3200, easing = LinearEasing)
            )
        }

        // 1. Poster & Background Fade In
        launch { screenAlpha.animateTo(1f, tween(300)) }

        // 2. Badge Fade In
        delay(100)
        launch { badgeAlpha.animateTo(1f, tween(200)) }

        // 3. Glass Card Slides Up
        delay(80)
        launch { cardOffsetY.animateTo(0f, spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessMediumLow)) }
        launch { cardAlpha.animateTo(1f, tween(250)) }

        // 4. Main Title Fade Up
        delay(100)
        launch { titleAlpha.animateTo(1f, tween(200)) }

        // 5. Subtitle Fade Up
        delay(80)
        launch { subtitleAlpha.animateTo(1f, tween(200)) }

        // 6. Feature Chips Pop One by One
        delay(100)
        for (c in 1..featureList.size) {
            chipsVisibleCount = c
            delay(60)
        }

        // 7. Get Started Button Spring Scale
        delay(80)
        launch { buttonAlpha.animateTo(1f, tween(180)) }
        launch { buttonScale.animateTo(1f, spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium)) }

        // 8. 3 Seconds Total Auto Countdown
        for (sec in 3 downTo 1) {
            countdownSeconds = sec
            delay(1000)
        }

        if (!isExiting) {
            handleExit(false)
        }
    }

    // Exit Animation Effects: Fade Out -> Small Scale -> Blur
    val animatedExitAlpha by animateFloatAsState(
        targetValue = if (isExiting) 0f else screenAlpha.value,
        animationSpec = tween(320, easing = FastOutSlowInEasing),
        label = "exitAlpha"
    )
    val animatedExitScale by animateFloatAsState(
        targetValue = if (isExiting) 0.93f else 1f,
        animationSpec = tween(320, easing = FastOutSlowInEasing),
        label = "exitScale"
    )
    val animatedExitBlur by animateFloatAsState(
        targetValue = if (isExiting) 16f else 0f,
        animationSpec = tween(320, easing = FastOutSlowInEasing),
        label = "exitBlur"
    )

    // Infinite Background Glow, Pulse & Glass Shine Sweep
    val infiniteTransition = rememberInfiniteTransition(label = "posterBg")
    val ambientPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.20f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambientPulse"
    )

    val lightSweepPos by infiniteTransition.animateFloat(
        initialValue = -400f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "lightSweepPos"
    )

    val baseModifier = Modifier
        .fillMaxSize()
        .graphicsLayer {
            alpha = animatedExitAlpha
            scaleX = animatedExitScale
            scaleY = animatedExitScale
        }

    val blurModifier = if (animatedExitBlur > 0.5f && !isPreviewMode) {
        baseModifier.blur(animatedExitBlur.dp)
    } else {
        baseModifier
    }

    Box(
        modifier = blurModifier
            .background(Color(0xFF020704))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // ==========================================
        // REAL BRAND AMBASSADOR POSTER BACKGROUND
        // Uses the exact downloaded user poster image
        // ==========================================
        Image(
            painter = painterResource(id = R.drawable.brand_ambassador_poster),
            contentDescription = "Brand Ambassador Poster",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = posterZoomScale.value
                    scaleY = posterZoomScale.value
                }
        )

        // Overlay Ambient Floating Particles & Light Reflections
        if (!isPreviewMode) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val numParticles = 18
                for (i in 0 until numParticles) {
                    val px = (sin((i * 1.7f + ambientPulse * 3.5f)) * 0.48f + 0.5f) * size.width
                    val py = (cos((i * 2.3f + ambientPulse * 2.5f)) * 0.48f + 0.5f) * size.height
                    val radius = (2.5f + (i % 4) * 2f).dp.toPx()
                    drawCircle(
                        color = EmeraldGlow.copy(alpha = (0.22f + 0.35f * sin(i + ambientPulse))),
                        radius = radius,
                        center = Offset(px, py)
                    )
                }
            }
        }

        // Top Row: ✨ BRAND AMBASSADOR Badge & Countdown Indicator
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ✨ BRAND AMBASSADOR Top Badge (Glass Background)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .graphicsLayer { alpha = badgeAlpha.value }
                    .shadow(16.dp, RoundedCornerShape(20.dp), spotColor = EmeraldGlow)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xDD091610))
                    .border(
                        BorderStroke(
                            1.2.dp,
                            Brush.horizontalGradient(
                                listOf(EmeraldGlow, Color(0xFF00E5FF), EmeraldGlow)
                            )
                        ),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = EmeraldGlow,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "BRAND AMBASSADOR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = EmeraldGlow,
                    letterSpacing = 1.3.sp
                )
            }

            // Countdown Timer Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xBB000000))
                    .border(
                        BorderStroke(1.dp, Color(0x44FFFFFF)),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(EmeraldGlow)
                )
                Text(
                    text = "${countdownSeconds}s",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Floating Glassmorphism UI Overlay at Bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp)
                .graphicsLayer {
                    translationY = cardOffsetY.value
                    alpha = cardAlpha.value
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 32.dp,
                        shape = RoundedCornerShape(28.dp),
                        spotColor = EmeraldGlow.copy(alpha = 0.60f),
                        ambientColor = Color.Black
                    )
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xEE091811),
                                Color(0xF805100B),
                                Color(0xFF020704)
                            )
                        )
                    )
                    .border(
                        BorderStroke(
                            1.2.dp,
                            Brush.horizontalGradient(
                                listOf(
                                    EmeraldGlow.copy(alpha = 0.90f),
                                    Color(0xFF00E5FF).copy(alpha = 0.65f),
                                    EmeraldGlow.copy(alpha = 0.95f)
                                )
                            )
                        ),
                        RoundedCornerShape(28.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // "ViralToolAI Exclusive" Tag
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x2810B981))
                            .border(
                                BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.6f)),
                                RoundedCornerShape(14.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = EmeraldGlow,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "ViralToolAI Exclusive",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black,
                            color = EmeraldGlow,
                            letterSpacing = 1.2.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // MAIN TITLE: WELCOME TO VIRALTOOLAI
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.graphicsLayer { alpha = titleAlpha.value }
                    ) {
                        Text(
                            text = "WELCOME TO",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite.copy(alpha = 0.7f),
                            letterSpacing = 2.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "VIRALTOOLAI",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 1.5.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // SUBTITLE: Build Your Creator Journey With AI
                    Text(
                        text = "Build Your Creator Journey With AI",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EmeraldGlow,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.graphicsLayer { alpha = subtitleAlpha.value }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // FEATURE CHIPS (AI Learning, Creator Growth, Video Editing, Brand Collaboration, Affiliate Earnings)
                    FeatureChipsFlowLayout(
                        features = featureList,
                        visibleCount = chipsVisibleCount
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // PRIMARY BUTTON: Get Started →
                    val btnInteractionSource = remember { MutableInteractionSource() }
                    val isBtnPressed by btnInteractionSource.collectIsPressedAsState()
                    val btnPressedScale by animateFloatAsState(
                        targetValue = if (isBtnPressed) 0.94f else 1f,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium),
                        label = "btnPressedScale"
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .graphicsLayer {
                                scaleX = buttonScale.value * btnPressedScale
                                scaleY = buttonScale.value * btnPressedScale
                                alpha = buttonAlpha.value
                            }
                            .shadow(
                                elevation = 16.dp,
                                shape = RoundedCornerShape(25.dp),
                                spotColor = EmeraldGlow
                            )
                            .clip(RoundedCornerShape(25.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(EmeraldPrimary, EmeraldGlow, Color(0xFF00E5FF))
                                )
                            )
                            .border(
                                BorderStroke(
                                    1.dp,
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.9f),
                                            Color.White.copy(alpha = 0.2f),
                                            Color.White.copy(alpha = 0.95f)
                                        ),
                                        start = Offset(lightSweepPos, 0f),
                                        end = Offset(lightSweepPos + 220f, 80f)
                                    )
                                ),
                                RoundedCornerShape(25.dp)
                            )
                            .clickable(
                                interactionSource = btnInteractionSource,
                                indication = androidx.compose.foundation.LocalIndication.current
                            ) { handleExit(true) }
                            .testTag("get_started_poster_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Get Started",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = AmoledBlack,
                                letterSpacing = 0.3.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = AmoledBlack,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // BOTTOM TEXT: Trusted by Future Creators
                    Text(
                        text = "Trusted by Future Creators",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextWhite.copy(alpha = 0.65f),
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Feature Chips Layout displaying the 5 core capabilities in pop-in chips.
 */
@Composable
fun FeatureChipsFlowLayout(
    features: List<String>,
    visibleCount: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Row 1: AI Learning • Creator Growth • Video Editing
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            features.take(3).forEachIndexed { index, feature ->
                val isVisible = index < visibleCount
                val chipScale by animateFloatAsState(
                    targetValue = if (isVisible) 1f else 0.4f,
                    animationSpec = spring(
                        dampingRatio = 0.65f,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "chipScale$index"
                )
                val chipAlpha by animateFloatAsState(
                    targetValue = if (isVisible) 1f else 0f,
                    animationSpec = tween(140),
                    label = "chipAlpha$index"
                )

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = chipScale
                            scaleY = chipScale
                            alpha = chipAlpha
                        }
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x2810B981))
                        .border(
                            BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.45f)),
                            RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "• $feature",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite.copy(alpha = 0.92f)
                    )
                }
            }
        }

        // Row 2: Brand Collaboration • Affiliate Earnings
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            features.drop(3).forEachIndexed { idx, feature ->
                val globalIdx = idx + 3
                val isVisible = globalIdx < visibleCount
                val chipScale by animateFloatAsState(
                    targetValue = if (isVisible) 1f else 0.4f,
                    animationSpec = spring(
                        dampingRatio = 0.65f,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "chipScale$globalIdx"
                )
                val chipAlpha by animateFloatAsState(
                    targetValue = if (isVisible) 1f else 0f,
                    animationSpec = tween(140),
                    label = "chipAlpha$globalIdx"
                )

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = chipScale
                            scaleY = chipScale
                            alpha = chipAlpha
                        }
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x2810B981))
                        .border(
                            BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.45f)),
                            RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "• $feature",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite.copy(alpha = 0.92f)
                    )
                }
            }
        }
    }
}
