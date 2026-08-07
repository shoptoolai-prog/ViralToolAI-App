package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.example.ui.components.ViralToolAiLogo

private val PureBlack = Color(0xFF000000)
private val SolidWhite = Color(0xFFFFFFFF)
private val CyanAccent = Color(0xFF20D9E8)
private val TextGrey = Color(0xFFB7B7B7)

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    // Intercept back gesture during startup
    BackHandler(enabled = true) {
        // Suppress back gesture during startup sequence
    }

    // Animation States
    val bgAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.85f) }
    val logoAlpha = remember { Animatable(0f) }
    val highlightProgress = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }

    val appleEasing = CubicBezierEasing(0.25f, 0.1f, 0.25f, 1.0f)

    LaunchedEffect(Unit) {
        // 1. Background fades in (0ms -> 200ms)
        launch {
            bgAlpha.animateTo(1f, tween(200, easing = LinearEasing))
        }

        delay(150)

        // 2. Logo scales from 0.85x to 1.0x & fades in (150ms -> 600ms)
        launch {
            logoAlpha.animateTo(1f, tween(450, easing = appleEasing))
        }
        launch {
            logoScale.animateTo(1.0f, tween(450, easing = appleEasing))
        }

        delay(400)

        // 3. Small cyan highlight travels once across the logo (550ms -> 1150ms)
        launch {
            highlightProgress.animateTo(1f, tween(600, easing = appleEasing))
        }

        delay(350)

        // 4. App name fades in (900ms -> 1250ms)
        launch {
            titleAlpha.animateTo(1f, tween(350, easing = appleEasing))
        }

        delay(200)

        // 5. Subtitle fades in (1100ms -> 1450ms)
        launch {
            subtitleAlpha.animateTo(1f, tween(350, easing = appleEasing))
        }

        // 6. Everything stays visible for 700ms
        delay(1050)

        // 7. Smoothly crossfade into onboarding / next screen
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlack)
            .alpha(bgAlpha.value)
            .testTag("splash_screen")
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Center Monogram Logo (V + T merged geometric symbol)
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value),
                contentAlignment = Alignment.Center
            ) {
                ViralToolAiLogo(
                    size = 110.dp,
                    highlightProgress = highlightProgress.value
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // App Name: ViralToolAI (Bold, Large, White)
            Text(
                text = "ViralToolAI",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = SolidWhite,
                letterSpacing = 1.2.sp,
                modifier = Modifier
                    .alpha(titleAlpha.value)
                    .testTag("splash_title")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle: AI Creator Intelligence (Small, Grey)
            Text(
                text = "AI Creator Intelligence",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextGrey,
                letterSpacing = 2.sp,
                modifier = Modifier
                    .alpha(subtitleAlpha.value)
                    .testTag("splash_subtitle")
            )
        }
    }
}

/**
 * Geometric Monogram combining 'V' and 'T':
 * - Solid White minimal luxury geometry.
 * - Single small #20D9E8 Cyan highlight traveling once across the symbol path.
 */
@Composable
private fun VtMonogramCanvas(
    modifier: Modifier = Modifier,
    highlightProgress: Float
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val strokeWidth = 8.dp.toPx()

        // Key Monogram Coordinates:
        // Top T Bar
        val tLeft = Offset(w * 0.16f, h * 0.22f)
        val tRight = Offset(w * 0.84f, h * 0.22f)
        
        // V Diagonals
        val vBottom = Offset(w * 0.50f, h * 0.80f)
        
        // T Stem
        val tStemTop = Offset(w * 0.50f, h * 0.22f)
        val tStemBottom = Offset(w * 0.50f, h * 0.58f)

        // 1. Draw T Horizontal Top Bar
        drawLine(
            color = SolidWhite,
            start = tLeft,
            end = tRight,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // 2. Draw V Left Diagonal (from Top-Left T end down to Apex V)
        drawLine(
            color = SolidWhite,
            start = tLeft,
            end = vBottom,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // 3. Draw V Right Diagonal (from Apex V up to Top-Right T end)
        drawLine(
            color = SolidWhite,
            start = vBottom,
            end = tRight,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // 4. Draw T Vertical Center Stem (descending down the center)
        drawLine(
            color = SolidWhite,
            start = tStemTop,
            end = tStemBottom,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // 5. Single Small #20D9E8 Cyan Highlight traveling once across the logo path
        if (highlightProgress in 0.01f..0.99f) {
            val highlightPos = when {
                highlightProgress < 0.35f -> {
                    // Travel across Top T Bar (0.0 -> 0.35)
                    val p = highlightProgress / 0.35f
                    Offset(
                        x = tLeft.x + (tRight.x - tLeft.x) * p,
                        y = tLeft.y
                    )
                }
                highlightProgress < 0.70f -> {
                    // Travel down V Left diagonal (0.35 -> 0.70)
                    val p = (highlightProgress - 0.35f) / 0.35f
                    Offset(
                        x = tLeft.x + (vBottom.x - tLeft.x) * p,
                        y = tLeft.y + (vBottom.y - tLeft.y) * p
                    )
                }
                else -> {
                    // Travel up V Right diagonal (0.70 -> 1.0)
                    val p = (highlightProgress - 0.70f) / 0.30f
                    Offset(
                        x = vBottom.x + (tRight.x - vBottom.x) * p,
                        y = vBottom.y + (tRight.y - vBottom.y) * p
                    )
                }
            }

            // Draw small Cyan highlight point
            drawCircle(
                color = CyanAccent,
                radius = 5.dp.toPx(),
                center = highlightPos
            )
            // Subtle glow around the highlight point
            drawCircle(
                color = CyanAccent.copy(alpha = 0.35f),
                radius = 10.dp.toPx(),
                center = highlightPos
            )
        }
    }
}
