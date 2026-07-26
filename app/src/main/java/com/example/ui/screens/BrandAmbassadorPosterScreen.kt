package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

object BrandAmbassadorPrefs {
    private const val PREF_NAME = "brand_ambassador_launch_prefs"

    fun shouldShowPoster(context: Context): Boolean {
        // Show every time on app open
        return true
    }

    fun recordPosterShown(context: Context, dontShowAgain: Boolean = false) {
        // No-op to respect 'show every time' mandate
    }
}

@Composable
fun BrandAmbassadorPosterScreen(
    onDismiss: () -> Unit,
    onExploreClicked: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var isExiting by remember { mutableStateOf(false) }

    // Screen entrance animations
    val screenAlpha = remember { Animatable(0f) }
    val screenScale = remember { Animatable(0.98f) }
    val cardOffsetY = remember { Animatable(100f) }
    val cardAlpha = remember { Animatable(0f) }
    val buttonsScale = remember { Animatable(0.85f) }
    val buttonsAlpha = remember { Animatable(0f) }

    // Typing headline state ("Learn. Create. Earn.")
    val fullHeadline = "Learn. Create. Earn."
    var typedHeadlineText by remember { mutableStateOf("") }
    var subtitleAlpha by remember { mutableFloatStateOf(0f) }

    // Feature chips pop animation state
    var chipsVisibleCount by remember { mutableIntStateOf(0) }
    val featureList = listOf(
        "AI Learning",
        "Creator Growth",
        "Video Editing",
        "Brand Collaboration",
        "Affiliate Earnings"
    )

    // Countdown state (3s auto countdown)
    var countdownSeconds by remember { mutableIntStateOf(3) }

    // Handle dismiss with exit animation
    val handleDismiss = { isExplore: Boolean ->
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
        // Step 1: Smooth Fade & Scale In (Poster & Background)
        launch { screenAlpha.animateTo(1f, tween(300)) }
        launch { screenScale.animateTo(1f, spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessLow)) }

        // Step 2: Glass Card Slides Up
        delay(120)
        launch { cardOffsetY.animateTo(0f, spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessMediumLow)) }
        launch { cardAlpha.animateTo(1f, tween(250)) }

        // Step 3: Headline Typing Animation ("Learn. Create. Earn.")
        delay(150)
        for (i in 1..fullHeadline.length) {
            typedHeadlineText = fullHeadline.substring(0, i)
            delay(30)
        }

        // Step 4: Subtitle Fade Up ("Turn Your Creativity Into Income")
        delay(80)
        subtitleAlpha = 1f

        // Step 5: Feature Chips Pop One by One
        delay(100)
        for (c in 1..featureList.size) {
            chipsVisibleCount = c
            delay(70)
        }

        // Step 6: Buttons Spring Scale
        delay(100)
        launch { buttonsAlpha.animateTo(1f, tween(200)) }
        launch { buttonsScale.animateTo(1f, spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium)) }

        // Step 7: 3 Seconds Total Auto Countdown
        for (sec in 3 downTo 1) {
            countdownSeconds = sec
            delay(1000)
        }

        if (!isExiting) {
            handleDismiss(false)
        }
    }

    // Exit Animation Effects: Fade Out -> Small Scale -> Blur
    val animatedExitAlpha by animateFloatAsState(
        targetValue = if (isExiting) 0f else screenAlpha.value,
        animationSpec = tween(320, easing = FastOutSlowInEasing),
        label = "exitAlpha"
    )
    val animatedExitScale by animateFloatAsState(
        targetValue = if (isExiting) 0.93f else screenScale.value,
        animationSpec = tween(320, easing = FastOutSlowInEasing),
        label = "exitScale"
    )
    val animatedExitBlur by animateFloatAsState(
        targetValue = if (isExiting) 16f else 0f,
        animationSpec = tween(320, easing = FastOutSlowInEasing),
        label = "exitBlur"
    )

    // Infinite Background Glow & Particle Movements
    val infiniteTransition = rememberInfiniteTransition(label = "posterBg")
    val ambientPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambientPulse"
    )

    val lightSweepPos by infiniteTransition.animateFloat(
        initialValue = -400f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "lightSweepPos"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                alpha = animatedExitAlpha
                scaleX = animatedExitScale
                scaleY = animatedExitScale
            }
            .blur(animatedExitBlur.dp)
            .background(Color(0xFF030705))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // High Quality Brand Ambassador Poster Graphic Canvas (Original Image Faithful Vector Rendering)
        BrandAmbassadorPortraitCanvas(modifier = Modifier.fillMaxSize())

        // Ambient Light Particles Layer
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        EmeraldPrimary.copy(alpha = 0.30f * ambientPulse),
                        EmeraldGlow.copy(alpha = 0.12f),
                        Color.Transparent
                    )
                ),
                radius = size.width * 0.8f,
                center = Offset(size.width * 0.5f, size.height * 0.30f)
            )

            val numParticles = 28
            for (i in 0 until numParticles) {
                val px = (sin((i * 1.8f + ambientPulse * 3.2f)) * 0.46f + 0.5f) * size.width
                val py = (cos((i * 2.4f + ambientPulse * 2.2f)) * 0.46f + 0.5f) * size.height
                val radius = (2.5f + (i % 4) * 2f).dp.toPx()
                drawCircle(
                    color = EmeraldGlow.copy(alpha = (0.20f + 0.30f * sin(i + ambientPulse))),
                    radius = radius,
                    center = Offset(px, py)
                )
            }
        }

        // Top Header Row: Skip Button & Auto Timer Badge
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Countdown Timer Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x990A1510))
                    .border(
                        BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.4f)),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(EmeraldGlow)
                )
                Text(
                    text = "Auto in ${countdownSeconds}s",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGlow,
                    letterSpacing = 0.5.sp
                )
            }

            // Top Right Glass "Skip" Button
            val skipInteractionSource = remember { MutableInteractionSource() }
            val isSkipPressed by skipInteractionSource.collectIsPressedAsState()
            val skipScale by animateFloatAsState(
                targetValue = if (isSkipPressed) 0.90f else 1f,
                animationSpec = spring(stiffness = Spring.StiffnessHigh),
                label = "skipScale"
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = skipScale
                        scaleY = skipScale
                    }
                    .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = EmeraldGlow)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xCC08120D))
                    .border(
                        BorderStroke(1.2.dp, Color(0x66FFFFFF)),
                        RoundedCornerShape(20.dp)
                    )
                    .clickable(
                        interactionSource = skipInteractionSource,
                        indication = androidx.compose.foundation.LocalIndication.current
                    ) { handleDismiss(false) }
                    .padding(horizontal = 14.dp, vertical = 7.dp)
                    .testTag("skip_poster_button")
            ) {
                Text(
                    text = "Skip",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Skip Poster",
                    tint = TextWhite,
                    modifier = Modifier.size(15.dp)
                )
            }
        }

        // Floating Glass Card at Bottom (Apple iOS Inspired)
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
                        elevation = 28.dp,
                        shape = RoundedCornerShape(28.dp),
                        spotColor = EmeraldGlow.copy(alpha = 0.55f),
                        ambientColor = Color.Black
                    )
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xEE0B1A12),
                                Color(0xF806110B),
                                Color(0xFF030A06)
                            )
                        )
                    )
                    .border(
                        BorderStroke(
                            1.2.dp,
                            Brush.horizontalGradient(
                                listOf(
                                    EmeraldGlow.copy(alpha = 0.85f),
                                    Color(0xFF00E5FF).copy(alpha = 0.6f),
                                    EmeraldGlow.copy(alpha = 0.95f)
                                )
                            )
                        ),
                        RoundedCornerShape(28.dp)
                    )
                    .padding(20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 1. "ViralToolAI Exclusive" Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x2810B981))
                            .border(
                                BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.7f)),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = EmeraldGlow,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "ViralToolAI Exclusive",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = EmeraldGlow,
                            letterSpacing = 1.2.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. Headline: "Learn. Create. Earn." (Typing Animation)
                    Text(
                        text = typedHeadlineText,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        textAlign = TextAlign.Center,
                        letterSpacing = 1.1.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 3. Subtitle: "Turn Your Creativity Into Income" (Fade Up)
                    Text(
                        text = "Turn Your Creativity Into Income",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGlow,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.alpha(subtitleAlpha)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4. Feature Chips Row (Pop one by one)
                    OptInChipsFlowRow(
                        features = featureList,
                        visibleCount = chipsVisibleCount
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // 5. Action Buttons Row (Spring Animation + Glass Shine Sweep)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = buttonsScale.value
                                scaleY = buttonsScale.value
                                alpha = buttonsAlpha.value
                            },
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Primary Button: "Explore ViralToolAI"
                        val btn1InteractionSource = remember { MutableInteractionSource() }
                        val isBtn1Pressed by btn1InteractionSource.collectIsPressedAsState()
                        val btn1Scale by animateFloatAsState(
                            targetValue = if (isBtn1Pressed) 0.94f else 1f,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium),
                            label = "btn1Scale"
                        )

                        Box(
                            modifier = Modifier
                                .weight(1.35f)
                                .height(48.dp)
                                .graphicsLayer {
                                    scaleX = btn1Scale
                                    scaleY = btn1Scale
                                }
                                .shadow(
                                    elevation = 14.dp,
                                    shape = RoundedCornerShape(24.dp),
                                    spotColor = EmeraldGlow
                                )
                                .clip(RoundedCornerShape(24.dp))
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
                                                Color.White.copy(alpha = 0.85f),
                                                Color.White.copy(alpha = 0.2f),
                                                Color.White.copy(alpha = 0.9f)
                                            ),
                                            start = Offset(lightSweepPos, 0f),
                                            end = Offset(lightSweepPos + 220f, 80f)
                                        )
                                    ),
                                    RoundedCornerShape(24.dp)
                                )
                                .clickable(
                                    interactionSource = btn1InteractionSource,
                                    indication = androidx.compose.foundation.LocalIndication.current
                                ) { handleDismiss(true) }
                                .testTag("explore_viraltool_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Explore ViralToolAI",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = AmoledBlack,
                                    letterSpacing = 0.2.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = AmoledBlack,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }

                        // Secondary Button: "Continue →"
                        val btn2InteractionSource = remember { MutableInteractionSource() }
                        val isBtn2Pressed by btn2InteractionSource.collectIsPressedAsState()
                        val btn2Scale by animateFloatAsState(
                            targetValue = if (isBtn2Pressed) 0.94f else 1f,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium),
                            label = "btn2Scale"
                        )

                        Box(
                            modifier = Modifier
                                .weight(0.95f)
                                .height(48.dp)
                                .graphicsLayer {
                                    scaleX = btn2Scale
                                    scaleY = btn2Scale
                                }
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0x22FFFFFF))
                                .border(
                                    BorderStroke(1.2.dp, Color(0x44FFFFFF)),
                                    RoundedCornerShape(24.dp)
                                )
                                .clickable(
                                    interactionSource = btn2InteractionSource,
                                    indication = androidx.compose.foundation.LocalIndication.current
                                ) { handleDismiss(false) }
                                .testTag("continue_poster_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Continue",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    letterSpacing = 0.2.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = TextWhite,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Feature Chips Flow Layout with staggered pop animations.
 */
@Composable
fun OptInChipsFlowRow(
    features: List<String>,
    visibleCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
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
                        targetValue = if (isVisible) 1f else 0.5f,
                        animationSpec = spring(
                            dampingRatio = 0.65f,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "chipScale$index"
                    )
                    val chipAlpha by animateFloatAsState(
                        targetValue = if (isVisible) 1f else 0f,
                        animationSpec = tween(150),
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
                            .background(Color(0x2210B981))
                            .border(
                                BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.4f)),
                                RoundedCornerShape(14.dp)
                            )
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "• $feature",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite.copy(alpha = 0.9f)
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
                        targetValue = if (isVisible) 1f else 0.5f,
                        animationSpec = spring(
                            dampingRatio = 0.65f,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "chipScale$globalIdx"
                    )
                    val chipAlpha by animateFloatAsState(
                        targetValue = if (isVisible) 1f else 0f,
                        animationSpec = tween(150),
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
                            .background(Color(0x2210B981))
                            .border(
                                BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.4f)),
                                RoundedCornerShape(14.dp)
                            )
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "• $feature",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Custom Vector Canvas rendering the Brand Ambassador Portrait Poster.
 * Faithfully matches Asit (young Indian brand ambassador) with dark wavy hair,
 * short beard and mustache, dusty rose / mauve buttoned shirt with open collar,
 * dark beaded rudraksha necklace with stone pendant, and atmospheric emerald background.
 */
@Composable
fun BrandAmbassadorPortraitCanvas(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "portraitRays")
    val rayShift by infiniteTransition.animateFloat(
        initialValue = -18f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(4200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rayShift"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // 1. Deep Atmospheric Dark Emerald Background Base
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF020905),
                    Color(0xFF071B11),
                    Color(0xFF0E2F1E),
                    Color(0xFF06180F),
                    Color(0xFF020704)
                )
            )
        )

        // 2. Volumetric Emerald Light Beams / Rays Behind Portrait
        val beamCenter = Offset(w * 0.5f, h * 0.28f)
        val beamPath = Path().apply {
            moveTo(beamCenter.x - w * 0.45f + rayShift, 0f)
            lineTo(beamCenter.x + w * 0.45f - rayShift, 0f)
            lineTo(w * 0.95f, h * 0.62f)
            lineTo(w * 0.05f, h * 0.62f)
            close()
        }
        drawPath(
            path = beamPath,
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF10B981).copy(alpha = 0.32f),
                    Color(0xFF059669).copy(alpha = 0.14f),
                    Color.Transparent
                ),
                center = beamCenter,
                radius = w * 0.80f
            )
        )

        // 3. Ambassador Body Silhouette - Dusty Rose / Mauve Buttoned Shirt
        val shirtColor = Color(0xFFC092A2) // Dusty Rose Mauve
        val shirtShadow = Color(0xFF8C5F70)
        val shirtDark = Color(0xFF5E3947)

        val chestY = h * 0.32f
        val waistY = h * 0.75f

        // Shoulders & Torso Path
        val torsoPath = Path().apply {
            moveTo(w * 0.10f, waistY)
            lineTo(w * 0.20f, chestY + 50f)
            quadraticTo(w * 0.32f, chestY - 10f, w * 0.40f, chestY + 20f) // Left Collar
            lineTo(w * 0.50f, chestY + 80f) // Center V-Neck open collar
            lineTo(w * 0.60f, chestY + 20f) // Right Collar
            quadraticTo(w * 0.68f, chestY - 10f, w * 0.80f, chestY + 50f)
            lineTo(w * 0.90f, waistY)
            close()
        }
        drawPath(
            path = torsoPath,
            brush = Brush.verticalGradient(
                colors = listOf(shirtColor, shirtShadow, shirtDark),
                startY = chestY,
                endY = waistY
            )
        )

        // Sleeves
        val leftSleeve = Path().apply {
            moveTo(w * 0.02f, waistY)
            lineTo(w * 0.20f, chestY + 50f)
            lineTo(w * 0.10f, waistY)
            close()
        }
        drawPath(leftSleeve, color = shirtDark)

        val rightSleeve = Path().apply {
            moveTo(w * 0.98f, waistY)
            lineTo(w * 0.80f, chestY + 50f)
            lineTo(w * 0.90f, waistY)
            close()
        }
        drawPath(rightSleeve, color = shirtDark)

        // Shirt Collar Wings
        val leftCollar = Path().apply {
            moveTo(w * 0.38f, chestY - 5f)
            lineTo(w * 0.46f, chestY + 45f)
            lineTo(w * 0.36f, chestY + 35f)
            close()
        }
        drawPath(leftCollar, color = Color(0xFFD6A7B7))

        val rightCollar = Path().apply {
            moveTo(w * 0.62f, chestY - 5f)
            lineTo(w * 0.54f, chestY + 45f)
            lineTo(w * 0.64f, chestY + 35f)
            close()
        }
        drawPath(rightCollar, color = Color(0xFFD6A7B7))

        // 4. Neck & Skin Tone
        val skinTone = Color(0xFFD9A083) // Warm Tan Skin Tone
        val skinShadow = Color(0xFFB37356)

        val neckPath = Path().apply {
            moveTo(w * 0.42f, chestY - 30f)
            lineTo(w * 0.58f, chestY - 30f)
            lineTo(w * 0.55f, chestY + 40f)
            lineTo(w * 0.45f, chestY + 40f)
            close()
        }
        drawPath(neckPath, brush = Brush.verticalGradient(listOf(skinTone, skinShadow)))

        // 5. Beaded Necklace with Pendant
        val necklacePath = Path().apply {
            moveTo(w * 0.42f, chestY - 10f)
            quadraticTo(w * 0.50f, chestY + 110f, w * 0.58f, chestY - 10f)
        }
        drawPath(
            path = necklacePath,
            color = Color(0xFF22181C),
            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
        )

        // Individual Beads along necklace
        for (b in 0..16) {
            val t = b / 16f
            val bx = (1 - t) * (1 - t) * (w * 0.42f) + 2 * (1 - t) * t * (w * 0.50f) + t * t * (w * 0.58f)
            val by = (1 - t) * (1 - t) * (chestY - 10f) + 2 * (1 - t) * t * (chestY + 110f) + t * t * (chestY - 10f)
            drawCircle(color = Color(0xFF38262E), radius = 3.5.dp.toPx(), center = Offset(bx, by))
        }

        // Stone Pendant
        drawCircle(
            color = Color(0xFFE2C08D),
            radius = 8.dp.toPx(),
            center = Offset(w * 0.50f, chestY + 110f)
        )
        drawCircle(
            color = EmeraldGlow,
            radius = 5.dp.toPx(),
            center = Offset(w * 0.50f, chestY + 110f)
        )

        // 6. Ambassador Head & Facial Features
        val headCenterY = chestY - 110f
        val headRadiusX = w * 0.16f
        val headRadiusY = 80.dp.toPx()

        // Face Base Oval
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(skinTone, skinShadow),
                center = Offset(w * 0.50f, headCenterY),
                radius = headRadiusX * 1.2f
            ),
            topLeft = Offset(w * 0.50f - headRadiusX, headCenterY - headRadiusY),
            size = Size(headRadiusX * 2, headRadiusY * 2)
        )

        // Dark Wavy Hair Style
        val hairPath = Path().apply {
            moveTo(w * 0.32f, headCenterY - 10f)
            quadraticTo(w * 0.30f, headCenterY - headRadiusY - 40f, w * 0.50f, headCenterY - headRadiusY - 50f)
            quadraticTo(w * 0.70f, headCenterY - headRadiusY - 40f, w * 0.68f, headCenterY - 10f)
            quadraticTo(w * 0.60f, headCenterY - headRadiusY + 10f, w * 0.50f, headCenterY - headRadiusY + 15f)
            quadraticTo(w * 0.40f, headCenterY - headRadiusY + 10f, w * 0.32f, headCenterY - 10f)
            close()
        }
        drawPath(hairPath, color = Color(0xFF141215))

        // Beard & Mustache
        val beardPath = Path().apply {
            moveTo(w * 0.38f, headCenterY + 30f)
            quadraticTo(w * 0.50f, headCenterY + headRadiusY + 10f, w * 0.62f, headCenterY + 30f)
            quadraticTo(w * 0.50f, headCenterY + headRadiusY + 2f, w * 0.38f, headCenterY + 30f)
            close()
        }
        drawPath(beardPath, color = Color(0xFF1F1C21).copy(alpha = 0.85f))

        // 7. Dark Vignette Bottom Gradient overlay
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    Color(0x88030805),
                    Color(0xEE030805),
                    Color(0xFF030805)
                ),
                startY = h * 0.40f,
                endY = h
            )
        )
    }
}
