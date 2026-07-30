package com.example.ui.screens

import android.content.Context
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.R
import com.example.core.StartupSoundPlayer
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Cinematic Animation Sequence States
    val ambientGlow = remember { Animatable(0f) }
    val particleProgress = remember { Animatable(0f) }
    
    // Logo Reveal States
    val logoScale = remember { Animatable(0.6f) }
    val logoAlpha = remember { Animatable(0f) }
    val logoPulse = remember { Animatable(1f) }
    val glassSweepProgress = remember { Animatable(-1f) }
    
    // Text States
    val titleAlpha = remember { Animatable(0f) }
    val taglineAlpha = remember { Animatable(0f) }
    val developerAlpha = remember { Animatable(0f) }
    val mobileNoticeAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        StartupSoundPlayer.playStartupChimeIfEnabled(context)

        // Preload poster image & home carousel banners in background for instant display
        try {
            val bannerUrls = listOf(
                "https://raw.githubusercontent.com/shoptoolai-prog/ViralToolAI-App/main/assets/brand-ambassadors/1785321241752.png",
                "https://raw.githubusercontent.com/shoptoolai-prog/ViralToolAI-App/main/assets/brand-ambassadors/Picsart_26-07-29_23-45-35-887.jpg",
                "https://raw.githubusercontent.com/shoptoolai-prog/ViralToolAI-App/main/assets/brand-ambassadors/Picsart_26-07-29_23-46-04-094.jpg",
                "https://raw.githubusercontent.com/shoptoolai-prog/ViralToolAI-App/main/assets/brand-ambassadors/Picsart_26-07-29_23-46-40-738.jpg"
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

        // 0.1s: Custom ViralToolAI icon & scale fade in
        delay(100)
        launch {
            logoAlpha.animateTo(1f, tween(350, easing = FastOutSlowInEasing))
        }
        launch {
            logoScale.animateTo(1f, spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMediumLow))
        }

        // 0.2s: Spotify Green ambient glow & particles appear
        delay(100)
        launch {
            ambientGlow.animateTo(0.9f, tween(400, easing = LinearOutSlowInEasing))
        }
        launch {
            particleProgress.animateTo(1f, tween(500, easing = FastOutSlowInEasing))
        }

        // 0.35s: Soft glass reflection sweeps across icon + haptic tick
        delay(150)
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        launch {
            glassSweepProgress.animateTo(1f, tween(400, easing = FastOutSlowInEasing))
        }

        // 0.5s: Title, Tagline, & Developer credits reveal in parallel
        delay(150)
        launch {
            logoPulse.animateTo(1.05f, tween(150, easing = FastOutSlowInEasing))
            logoPulse.animateTo(1.00f, tween(150, easing = LinearOutSlowInEasing))
        }
        launch {
            titleAlpha.animateTo(1f, tween(250))
        }
        launch {
            taglineAlpha.animateTo(1f, tween(300))
        }
        launch {
            developerAlpha.animateTo(1f, tween(300))
        }

        // 0.75s: Mobile notice appears (~40% opacity)
        delay(250)
        launch {
            mobileNoticeAlpha.animateTo(0.40f, tween(250))
        }

        // 1.8s: Complete splash smoothly -> Open Home Screen
        delay(1050)
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack),
        contentAlignment = Alignment.Center
    ) {
        // Spotify Green Ambient Glow (#000000 -> Ambient Glow)
        Box(
            modifier = Modifier
                .size(320.dp)
                .alpha(ambientGlow.value)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            EmeraldPrimary.copy(alpha = 0.50f),
                            EmeraldPrimary.copy(alpha = 0.20f),
                            Color(0xFF0D3320).copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )

        // Minimal Premium Particles Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f - 40.dp.toPx()
            val particleCount = 14
            val pProgress = particleProgress.value

            if (pProgress > 0f) {
                for (i in 0 until particleCount) {
                    val angle = (i * (360f / particleCount)) * (Math.PI / 180f)
                    val radius = 170.dp.toPx() * pProgress
                    
                    val x = (cx + radius * cos(angle)).toFloat()
                    val y = (cy + radius * sin(angle)).toFloat()
                    val alpha = (pProgress * 0.75f).coerceIn(0f, 0.75f)

                    drawCircle(
                        color = EmeraldPrimary.copy(alpha = alpha),
                        radius = 2.dp.toPx() * pProgress,
                        center = Offset(x, y)
                    )
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Main Custom Icon Container with Scale, Pulse, and Glass Reflection Sweep
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .scale(logoScale.value * logoPulse.value)
                    .alpha(logoAlpha.value)
            ) {
                // Outer Halo
                Box(
                    modifier = Modifier
                        .size(118.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(EmeraldPrimary.copy(alpha = 0.25f), Color.Transparent)
                            )
                        )
                        .border(
                            1.dp,
                            Brush.linearGradient(
                                listOf(EmeraldGlow.copy(alpha = 0.6f), Color.White.copy(alpha = 0.15f))
                            ),
                            CircleShape
                        )
                )

                // Custom Uploaded / Generated Icon Card
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF141A16), Color(0xFF0A0F0C))
                            )
                        )
                        .border(1.2.dp, EmeraldPrimary.copy(alpha = 0.5f), RoundedCornerShape(26.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_viraltool_icon),
                        contentDescription = "ViralToolAI Icon",
                        modifier = Modifier
                            .size(68.dp)
                            .padding(4.dp)
                    )
                }

                // Glass Reflection Sweep Line
                val sweepVal = glassSweepProgress.value
                if (sweepVal in -0.9f..0.9f) {
                    Canvas(
                        modifier = Modifier
                            .size(118.dp)
                            .clip(RoundedCornerShape(28.dp))
                    ) {
                        val yPos = (size.height / 2f) + (sweepVal * size.height / 2f)
                        drawLine(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.15f),
                                    Color.White.copy(alpha = 0.60f),
                                    Color.White.copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            ),
                            start = Offset(0f, yPos),
                            end = Offset(size.width, yPos),
                            strokeWidth = 4.dp.toPx()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Application Title
            Text(
                text = "ViralToolAI",
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(titleAlpha.value)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Tagline: From Products to Popularity
            Text(
                text = "From Products to Popularity",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(taglineAlpha.value)
            )
        }

        // Bottom Developer Credit & Mobile Notice
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Developer Credit: "Created by Asit"
                Text(
                    text = "Created by Asit",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    letterSpacing = 1.5.sp,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        shadow = Shadow(
                            color = EmeraldPrimary.copy(alpha = 0.5f),
                            blurRadius = 10f
                        )
                    ),
                    modifier = Modifier.alpha(developerAlpha.value)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Small premium typography notice: "Designed & Built Entirely on Mobile" (~40% opacity)
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

