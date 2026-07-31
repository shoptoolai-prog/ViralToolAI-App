package com.example.ui.screens

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.R
import com.example.core.StartupSoundPlayer
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.VioletGlow
import com.example.ui.theme.VioletPrimary
import com.example.ui.theme.VioletLight
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // CRITICAL REQUIREMENT: Disable Back Gesture during startup
    BackHandler(enabled = true) {
        // Suppress back button during cinematic startup sequence
    }

    // ==================================================
    // 10-STEP CINEMATIC STARTUP ANIMATION SEQUENCE
    // ==================================================
    // Step 1: Black background fade-in
    val bgAlpha = remember { Animatable(0f) }
    
    // Step 2: Soft purple particles slowly appear
    val particleAlpha = remember { Animatable(0f) }
    
    // Step 3: White glowing waves animate from bottom
    val waveProgress = remember { Animatable(0f) }
    
    // Step 4: New ViralToolAi logo fades in
    val logoScale = remember { Animatable(0.5f) }
    val logoAlpha = remember { Animatable(0f) }
    
    // Step 5: Gentle breathing logo pulse (continuous)
    val infiniteTransition = rememberInfiniteTransition(label = "splashInfinite")
    val logoBreathing by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathingPulse"
    )
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveOffset"
    )

    // Step 6: Purple glow expands softly
    val purpleGlowScale = remember { Animatable(0.4f) }
    val purpleGlowAlpha = remember { Animatable(0f) }

    // Step 7: Text "ViralToolAi" appears with animated letter reveal
    val letterRevealProgress = remember { Animatable(0f) }

    // Step 8: Tagline appears below with fade-up animation
    val taglineAlpha = remember { Animatable(0f) }
    val taglineOffsetY = remember { Animatable(18f) }

    // Step 9: Bottom progress indicator fills smoothly
    val progressAnim = remember { Animatable(0f) }
    val developerAlpha = remember { Animatable(0f) }
    val mobileNoticeAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        StartupSoundPlayer.playStartupChimeIfEnabled(context)

        // Preload assets for instant presentation on next screens
        try {
            val bannerUrls = listOf(
                "https://raw.githubusercontent.com/shoptoolai-prog/ViralToolAi-App/main/assets/brand-ambassadors/1785321241752.png",
                "https://raw.githubusercontent.com/shoptoolai-prog/ViralToolAi-App/main/assets/brand-ambassadors/Picsart_26-07-29_23-45-35-887.jpg",
                "https://raw.githubusercontent.com/shoptoolai-prog/ViralToolAi-App/main/assets/brand-ambassadors/Picsart_26-07-29_23-46-04-094.jpg"
            )
            bannerUrls.forEach { url ->
                val preloadRequest = ImageRequest.Builder(context)
                    .data(url)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build()
                context.imageLoader.enqueue(preloadRequest)
            }
        } catch (_: Exception) {}

        // --- STEP 1: Black background fades in (0ms - 150ms) ---
        launch { bgAlpha.animateTo(1f, tween(200, easing = LinearEasing)) }

        // --- STEP 2: Soft purple particles slowly appear (100ms - 400ms) ---
        delay(100)
        launch { particleAlpha.animateTo(1f, tween(400, easing = LinearOutSlowInEasing)) }

        // --- STEP 3: White glowing waves animate from bottom (200ms - 600ms) ---
        delay(100)
        launch { waveProgress.animateTo(1f, tween(600, easing = FastOutSlowInEasing)) }

        // --- STEP 4 & 6: Logo fades in & Purple glow expands softly (300ms - 800ms) ---
        delay(100)
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        launch { logoAlpha.animateTo(1f, tween(350, easing = FastOutSlowInEasing)) }
        launch { logoScale.animateTo(1f, spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow)) }
        launch { purpleGlowAlpha.animateTo(0.75f, tween(450)) }
        launch { purpleGlowScale.animateTo(1.0f, tween(550, easing = FastOutSlowInEasing)) }

        // --- STEP 7: Text "ViralToolAi" animated letter reveal (500ms - 900ms) ---
        delay(200)
        launch { letterRevealProgress.animateTo(1f, tween(450, easing = FastOutSlowInEasing)) }

        // --- STEP 8: Tagline appears below with fade-up animation (700ms - 1100ms) ---
        delay(200)
        launch { taglineAlpha.animateTo(1f, tween(350)) }
        launch { taglineOffsetY.animateTo(0f, tween(350, easing = FastOutSlowInEasing)) }

        // --- STEP 9: Progress indicator fills smoothly + Credits (900ms - 2700ms) ---
        delay(200)
        launch { developerAlpha.animateTo(1f, tween(300)) }
        launch { mobileNoticeAlpha.animateTo(0.5f, tween(400)) }
        
        // Progress bar smooth fill over 1.8 seconds with easing
        progressAnim.animateTo(1.0f, tween(1800, easing = LinearOutSlowInEasing))

        // --- STEP 10: Smooth transition into Launch Screen ---
        delay(150)
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .alpha(bgAlpha.value)
            // CRITICAL REQUIREMENT: Intercept & consume all touch events during startup
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        event.changes.forEach { it.consume() }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // ==================================================
        // BOTTOM EFFECTS: WHITE GLOWING WAVES & PURPLE GLOW
        // ==================================================
        val wProgress = waveProgress.value
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (wProgress > 0f) {
                val w = size.width
                val h = size.height

                // 1. Soft Purple Glow Fill at Bottom
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x188B5CF6),
                            Color(0x358B5CF6)
                        ),
                        startY = h * 0.55f,
                        endY = h
                    )
                )

                // 2. Animated Layer 1: Glowing White Wave Curve
                val wavePath1 = Path().apply {
                    moveTo(0f, h)
                    val baseWaveY = h - (120.dp.toPx() * wProgress)
                    var x = 0f
                    while (x <= w) {
                        val y = baseWaveY + sin((x / w * 3.14f * 2.5f) + waveOffset) * (14.dp.toPx() * wProgress)
                        lineTo(x, y)
                        x += 15f
                    }
                    lineTo(w, h)
                    close()
                }
                drawPath(
                    path = wavePath1,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.08f * wProgress),
                            VioletGlow.copy(alpha = 0.12f * wProgress),
                            Color.Transparent
                        )
                    )
                )

                // 3. Animated Layer 2: Secondary Violet Wave Curve
                val wavePath2 = Path().apply {
                    moveTo(0f, h)
                    val baseWaveY = h - (90.dp.toPx() * wProgress)
                    var x = 0f
                    while (x <= w) {
                        val y = baseWaveY + cos((x / w * 3.14f * 2.0f) - waveOffset * 0.8f) * (10.dp.toPx() * wProgress)
                        lineTo(x, y)
                        x += 15f
                    }
                    lineTo(w, h)
                    close()
                }
                drawPath(
                    path = wavePath2,
                    brush = Brush.verticalGradient(
                        listOf(
                            VioletPrimary.copy(alpha = 0.25f * wProgress),
                            ElectricPurple.copy(alpha = 0.10f * wProgress),
                            Color.Transparent
                        )
                    )
                )
            }
        }

        // ==================================================
        // FLOATING PARTICLES & LIGHT SPARKS
        // ==================================================
        val pAlpha = particleAlpha.value
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (pAlpha > 0f) {
                val cx = size.width / 2f
                val cy = size.height / 2f - 40.dp.toPx()
                val particleCount = 20

                for (i in 0 until particleCount) {
                    val angle = (i * (360f / particleCount)) * (Math.PI / 180f)
                    val orbitRadius = (120.dp.toPx() + (i % 4) * 25.dp.toPx()) * pAlpha
                    val driftY = sin(waveOffset + i).toFloat() * 12.dp.toPx()

                    val x = (cx + orbitRadius * cos(angle)).toFloat()
                    val y = (cy + orbitRadius * sin(angle)).toFloat() + driftY
                    val itemAlpha = (pAlpha * (0.35f + 0.45f * sin(i + waveOffset))).coerceIn(0f, 0.8f)

                    // Sparkle dot
                    drawCircle(
                        color = if (i % 3 == 0) Color.White.copy(alpha = itemAlpha) else VioletGlow.copy(alpha = itemAlpha),
                        radius = (1.5.dp.toPx() + (i % 3) * 1.2.dp.toPx()),
                        center = Offset(x, y)
                    )
                }
            }
        }

        // ==================================================
        // EXPANDING SOFT PURPLE NEBULA GLOW
        // ==================================================
        Box(
            modifier = Modifier
                .size(340.dp)
                .scale(purpleGlowScale.value)
                .alpha(purpleGlowAlpha.value)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            VioletPrimary.copy(alpha = 0.55f),
                            ElectricPurple.copy(alpha = 0.25f),
                            Color(0xFF230D42).copy(alpha = 0.10f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )

        // ==================================================
        // CENTER LOGO, TITLE, TAGLINE & PROGRESS
        // ==================================================
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // STEP 4 & 5: Standalone Logo with Scale, Breathing, & Glow
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .scale(logoScale.value * logoBreathing)
                    .alpha(logoAlpha.value)
            ) {
                // Soft Outer Aura Halo
                Box(
                    modifier = Modifier
                        .size(135.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    VioletPrimary.copy(alpha = 0.60f),
                                    ElectricPurple.copy(alpha = 0.30f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Large Official New "V" Logo
                Image(
                    painter = painterResource(id = R.drawable.ic_viraltool_icon),
                    contentDescription = "ViralToolAi Logo",
                    modifier = Modifier
                        .size(105.dp)
                        .shadow(20.dp, CircleShape, spotColor = VioletPrimary)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // STEP 7: Application Title "ViralToolAi" with Animated Letter Reveal
            val fullText = "ViralToolAi"
            val revealVal = letterRevealProgress.value

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                fullText.forEachIndexed { index, char ->
                    val charProgress = ((revealVal * fullText.length) - index).coerceIn(0f, 1f)
                    val isAiPart = index >= 9 // "Ai" at end

                    Text(
                        text = char.toString(),
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isAiPart) VioletGlow else TextWhite,
                        letterSpacing = 1.2.sp,
                        style = TextStyle(
                            shadow = Shadow(
                                color = if (isAiPart) VioletPrimary else VioletPrimary.copy(alpha = 0.4f),
                                blurRadius = if (isAiPart) 16f else 8f
                            )
                        ),
                        modifier = Modifier.graphicsLayer {
                            alpha = charProgress
                            scaleX = 0.7f + (0.3f * charProgress)
                            scaleY = 0.7f + (0.3f * charProgress)
                            translationY = (10.dp.toPx() * (1f - charProgress))
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // STEP 8: Tagline "Powering Creators. Amplifying Growth."
            Text(
                text = "Powering Creators. Amplifying Growth.",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = VioletGlow,
                letterSpacing = 1.4.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = taglineAlpha.value
                        translationY = taglineOffsetY.value.dp.toPx()
                    }
            )

            Spacer(modifier = Modifier.height(34.dp))

            // STEP 9: Bottom Progress Indicator filling smoothly
            Box(
                modifier = Modifier
                    .width(170.dp)
                    .height(4.dp)
                    .alpha(taglineAlpha.value)
                    .clip(CircleShape)
                    .background(Color(0x25FFFFFF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progressAnim.value)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(VioletPrimary, VioletGlow, ElectricPurple, Color.White)
                            )
                        )
                )
            }
        }

        // ==================================================
        // BOTTOM DEVELOPER CREDIT & MOBILE NOTICE
        // ==================================================
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Created by Asit",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    letterSpacing = 1.5.sp,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        shadow = Shadow(
                            color = VioletPrimary.copy(alpha = 0.6f),
                            blurRadius = 10f
                        )
                    ),
                    modifier = Modifier.alpha(developerAlpha.value)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Designed & Built Entirely on Mobile",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextWhite,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.alpha(mobileNoticeAlpha.value)
                )
            }
        }
    }
}


