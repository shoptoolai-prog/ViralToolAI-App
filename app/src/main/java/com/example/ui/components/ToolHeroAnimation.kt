package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

enum class ToolHeroType {
    BRAND_COLLAB,
    MEESHO_CREATOR,
    INSTAGRAM_CREATOR,
    YOUTUBE_CREATOR,
    AI_PROMPT_EXTRACTOR,
    CAPCUT_MASTER,
    VN_EDITOR,
    INSTAGRAM_EDITS
}

@Composable
fun ToolHeroBanner(
    toolType: ToolHeroType,
    modifier: Modifier = Modifier,
    height: Dp = 140.dp,
    badgeText: String? = null,
    subtitleText: String? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF0F1018))
    ) {
        when (toolType) {
            ToolHeroType.BRAND_COLLAB -> BrandCollabHeroCanvas()
            ToolHeroType.MEESHO_CREATOR -> MeeshoHeroCanvas()
            ToolHeroType.INSTAGRAM_CREATOR -> InstagramHeroCanvas()
            ToolHeroType.YOUTUBE_CREATOR -> YouTubeHeroCanvas()
            ToolHeroType.AI_PROMPT_EXTRACTOR -> AiPromptExtractorHeroCanvas()
            ToolHeroType.CAPCUT_MASTER -> CapCutMasterHeroCanvas()
            ToolHeroType.VN_EDITOR -> VnEditorHeroCanvas()
            ToolHeroType.INSTAGRAM_EDITS -> InstagramEditsHeroCanvas()
        }

        if (badgeText != null || subtitleText != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0xDD0A0B10))
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (subtitleText != null) {
                    Text(
                        text = subtitleText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
                if (badgeText != null) {
                    val badgeColor = when (toolType) {
                        ToolHeroType.BRAND_COLLAB -> Color(0xFFFFD700)
                        ToolHeroType.MEESHO_CREATOR -> Color(0xFFFF4081)
                        ToolHeroType.INSTAGRAM_CREATOR -> Color(0xFFE1306C)
                        ToolHeroType.YOUTUBE_CREATOR -> Color(0xFFFF0000)
                        ToolHeroType.AI_PROMPT_EXTRACTOR -> Color(0xFF8B5CF6)
                        ToolHeroType.CAPCUT_MASTER -> Color(0xFF00E5FF)
                        ToolHeroType.VN_EDITOR -> Color(0xFF0288D1)
                        ToolHeroType.INSTAGRAM_EDITS -> Color(0xFFCFD8DC)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(badgeColor.copy(alpha = 0.2f))
                            .border(BorderStroke(1.dp, badgeColor.copy(alpha = 0.6f)), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor
                        )
                    }
                }
            }
        }
    }
}

/** 1. Brand Collaboration Hero: Golden particles, handshake/collaborating deal figures, floating sponsorship icons, soft shine sweep */
@Composable
private fun BrandCollabHeroCanvas() {
    val transition = rememberInfiniteTransition(label = "brandCollab")
    val sweepProgress by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Restart),
        label = "sweep"
    )
    val floatY by transition.animateFloat(
        initialValue = -8f, targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(2500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "float"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Background Gradient
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x33FFD700), Color(0x11FFA000), Color(0xFF0C0D14)),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.7f
            )
        )

        // Golden Particles
        val particleCount = 12
        for (i in 0 until particleCount) {
            val px = (w * (0.08f + 0.84f * ((i * 37) % 100) / 100f))
            val py = (h * (0.15f + 0.7f * ((i * 53) % 100) / 100f) + sin((sweepProgress * 6.28f + i).toDouble()).toFloat() * 6f)
            val pRadius = 2f + (i % 3) * 1.5f
            drawCircle(
                color = Color(0xFFFFD700).copy(alpha = 0.4f + 0.4f * sin((sweepProgress * 3.14f + i).toDouble()).toFloat()),
                radius = pRadius,
                center = Offset(px, py)
            )
        }

        // Deal Badge / Handshake Icon graphic in center
        val centerX = w * 0.5f
        val centerY = h * 0.48f + floatY

        // Outer Golden Ring
        drawCircle(
            brush = Brush.sweepGradient(listOf(Color(0xFFFFD700), Color(0xFFFFA000), Color(0xFFFFD700))),
            radius = 34f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 3.5f)
        )
        drawCircle(
            color = Color(0x22FFD700),
            radius = 32f,
            center = Offset(centerX, centerY)
        )

        // Handshake lines
        val path = Path().apply {
            moveTo(centerX - 16f, centerY - 2f)
            lineTo(centerX - 4f, centerY + 10f)
            lineTo(centerX + 4f, centerY + 2f)
            lineTo(centerX + 16f, centerY - 4f)
        }
        drawPath(path = path, color = Color(0xFFFFD700), style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Floating deal icons (Sponsor $ & Deal Badges)
        drawRoundRect(
            color = Color(0x33FFD700),
            topLeft = Offset(w * 0.2f, h * 0.3f + floatY * 0.5f),
            size = Size(46f, 26f),
            cornerRadius = CornerRadius(8f),
            style = Stroke(width = 1.5f)
        )
        drawRoundRect(
            color = Color(0x33FFD700),
            topLeft = Offset(w * 0.75f, h * 0.4f - floatY * 0.5f),
            size = Size(40f, 24f),
            cornerRadius = CornerRadius(8f),
            style = Stroke(width = 1.5f)
        )

        // Gold Shine Sweep
        val shineX = -w * 0.5f + (w * 2f) * sweepProgress
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(Color.Transparent, Color(0x22FFD700), Color.Transparent),
                start = Offset(shineX, 0f),
                end = Offset(shineX + 100f, h)
            )
        )
    }
}

/** 2. Meesho Creator Hero: Floating shopping bags, product cards, affiliate commission coins, pink particles */
@Composable
private fun MeeshoHeroCanvas() {
    val transition = rememberInfiniteTransition(label = "meesho")
    val pulse by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart),
        label = "pulse"
    )
    val floatY by transition.animateFloat(
        initialValue = -6f, targetValue = 6f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "float"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x33FF4081), Color(0x11E91E63), Color(0xFF0D0B12)),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.7f
            )
        )

        // Floating Shopping Bag outlines & Product Cards
        val bagLeft = w * 0.22f
        val bagTop = h * 0.35f + floatY
        drawRoundRect(
            color = Color(0xFFFF4081),
            topLeft = Offset(bagLeft, bagTop),
            size = Size(36f, 44f),
            cornerRadius = CornerRadius(6f),
            style = Stroke(width = 2f)
        )
        // Bag handle
        drawArc(
            color = Color(0xFFFF4081),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(bagLeft + 9f, bagTop - 10f),
            size = Size(18f, 16f),
            style = Stroke(width = 2f)
        )

        // Center Product Card
        val cardX = w * 0.5f - 24f
        val cardY = h * 0.3f - floatY * 0.8f
        drawRoundRect(
            color = Color(0x44FF4081),
            topLeft = Offset(cardX, cardY),
            size = Size(48f, 56f),
            cornerRadius = CornerRadius(8f)
        )
        drawRoundRect(
            color = Color(0xFFFF4081),
            topLeft = Offset(cardX, cardY),
            size = Size(48f, 56f),
            cornerRadius = CornerRadius(8f),
            style = Stroke(width = 1.5f)
        )
        // Product tag inside
        drawCircle(color = Color(0xFFFF4081), radius = 6f, center = Offset(cardX + 24f, cardY + 20f))

        // Affiliate Coins ($ / ₹) floating on right
        val coinX = w * 0.76f
        val coinY = h * 0.42f + floatY
        drawCircle(color = Color(0xFFE91E63), radius = 16f, center = Offset(coinX, coinY), style = Stroke(width = 2f))
        drawCircle(color = Color(0x33FF4081), radius = 14f, center = Offset(coinX, coinY))

        // Pink light particles
        for (i in 0..8) {
            val px = (w * (0.1f + 0.8f * ((i * 41) % 100) / 100f))
            val py = (h * (0.2f + 0.6f * ((i * 67) % 100) / 100f) + cos((pulse * 6.28f + i).toDouble()).toFloat() * 5f)
            drawCircle(
                color = Color(0xFFFF4081).copy(alpha = 0.3f + 0.5f * sin((pulse * 3.14f + i).toDouble()).toFloat()),
                radius = 3f,
                center = Offset(px, py)
            )
        }
    }
}

/** 3. Instagram Creator Hero: Floating Reels frame, hearts, likes, follower count badge + viral spark particles */
@Composable
private fun InstagramHeroCanvas() {
    val transition = rememberInfiniteTransition(label = "instagram")
    val rotateVal by transition.animateFloat(
        initialValue = -5f, targetValue = 5f,
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "rotate"
    )
    val sparkPulse by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), RepeatMode.Restart),
        label = "spark"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Instagram Tri-color Gradient background
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x33E1306C), Color(0x22833AB4), Color(0x11FD1D1D), Color(0xFF0C0A10)),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.75f
            )
        )

        // Floating Reels Camera / Phone Frame in Center
        val centerX = w * 0.5f
        val centerY = h * 0.45f
        rotate(rotateVal, pivot = Offset(centerX, centerY)) {
            // Reels Phone Frame
            drawRoundRect(
                brush = Brush.linearGradient(listOf(Color(0xFF833AB4), Color(0xFFE1306C), Color(0xFFFD1D1D))),
                topLeft = Offset(centerX - 24f, centerY - 36f),
                size = Size(48f, 72f),
                cornerRadius = CornerRadius(10f),
                style = Stroke(width = 2.5f)
            )

            // Inner Play / Reel Icon
            val reelPath = Path().apply {
                moveTo(centerX - 6f, centerY - 10f)
                lineTo(centerX + 10f, centerY)
                lineTo(centerX - 6f, centerY + 10f)
                close()
            }
            drawPath(path = reelPath, color = Color(0xFFE1306C))
        }

        // Floating Heart / Like Bubbles
        val heartX1 = w * 0.22f
        val heartY1 = h * 0.32f + sin(sparkPulse * 6.28f).toFloat() * 6f
        drawCircle(color = Color(0x33E1306C), radius = 14f, center = Offset(heartX1, heartY1))
        drawCircle(color = Color(0xFFE1306C), radius = 14f, center = Offset(heartX1, heartY1), style = Stroke(width = 1.5f))

        val heartX2 = w * 0.78f
        val heartY2 = h * 0.38f - sin(sparkPulse * 6.28f).toFloat() * 6f
        drawCircle(color = Color(0x33833AB4), radius = 12f, center = Offset(heartX2, heartY2))
        drawCircle(color = Color(0xFF833AB4), radius = 12f, center = Offset(heartX2, heartY2), style = Stroke(width = 1.5f))

        // Viral Spark Particles
        for (i in 0..10) {
            val angle = (i * 36) * (Math.PI / 180.0)
            val dist = 50f + 25f * sin((sparkPulse * 3.14f + i).toDouble()).toFloat()
            val px = (centerX + cos(angle) * dist).toFloat()
            val py = (centerY + sin(angle) * dist).toFloat()
            drawCircle(
                color = if (i % 2 == 0) Color(0xFFE1306C) else Color(0xFFF77737),
                radius = 2.5f,
                center = Offset(px, py)
            )
        }
    }
}

/** 4. YouTube Creator Hero: Play button, Shorts badge, subscribe button chip, trending graph wave, red glow */
@Composable
private fun YouTubeHeroCanvas() {
    val transition = rememberInfiniteTransition(label = "youtube")
    val waveOffset by transition.animateFloat(
        initialValue = 0f, targetValue = 6.28f,
        animationSpec = infiniteRepeatable(tween(3500, easing = LinearEasing), RepeatMode.Restart),
        label = "wave"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x33FF0000), Color(0x11E50914), Color(0xFF0E0B0C)),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.7f
            )
        )

        // Trending Growth Curve (Sine wave graph)
        val path = Path()
        var first = true
        for (x in 0..w.toInt() step 10) {
            val y = h * 0.65f - sin((x * 0.02f + waveOffset).toDouble()).toFloat() * 12f - (x / w) * 15f
            if (first) {
                path.moveTo(x.toFloat(), y)
                first = false
            } else {
                path.lineTo(x.toFloat(), y)
            }
        }
        drawPath(path = path, color = Color(0x66FF0000), style = Stroke(width = 2.5f, cap = StrokeCap.Round))

        // Center YouTube Play Button
        val centerX = w * 0.5f
        val centerY = h * 0.42f

        drawRoundRect(
            color = Color(0xFFFF0000),
            topLeft = Offset(centerX - 32f, centerY - 20f),
            size = Size(64f, 40f),
            cornerRadius = CornerRadius(12f)
        )
        // White Play Triangle
        val playPath = Path().apply {
            moveTo(centerX - 8f, centerY - 10f)
            lineTo(centerX + 10f, centerY)
            lineTo(centerX - 8f, centerY + 10f)
            close()
        }
        drawPath(path = playPath, color = Color.White)

        // Shorts Badge outline on Left
        drawRoundRect(
            color = Color(0x44FF0000),
            topLeft = Offset(w * 0.18f, h * 0.35f),
            size = Size(26f, 38f),
            cornerRadius = CornerRadius(6f),
            style = Stroke(width = 1.5f)
        )

        // Subscribe Badge outline on Right
        drawRoundRect(
            color = Color(0x44FF0000),
            topLeft = Offset(w * 0.72f, h * 0.38f),
            size = Size(42f, 20f),
            cornerRadius = CornerRadius(6f),
            style = Stroke(width = 1.5f)
        )
    }
}

/** 5. AI Prompt Extractor Hero: AI image scan laser beam line moving top-to-bottom, neural network nodes & connecting lines, vision analysis grid */
@Composable
private fun AiPromptExtractorHeroCanvas() {
    val transition = rememberInfiniteTransition(label = "aiPrompt")
    val scanY by transition.animateFloat(
        initialValue = 0.15f, targetValue = 0.85f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing), RepeatMode.Reverse),
        label = "scan"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x338B5CF6), Color(0x119C27B0), Color(0xFF0B0A12)),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.7f
            )
        )

        // Neural network nodes
        val nodes = listOf(
            Offset(w * 0.2f, h * 0.3f),
            Offset(w * 0.35f, h * 0.6f),
            Offset(w * 0.5f, h * 0.35f),
            Offset(w * 0.65f, h * 0.65f),
            Offset(w * 0.8f, h * 0.3f)
        )

        // Draw connecting neural lines
        for (i in 0 until nodes.size - 1) {
            drawLine(
                color = Color(0x448B5CF6),
                start = nodes[i],
                end = nodes[i + 1],
                strokeWidth = 1.5f
            )
        }

        // Draw nodes
        for (node in nodes) {
            drawCircle(color = Color(0xFF8B5CF6), radius = 5f, center = node)
            drawCircle(color = Color(0x338B5CF6), radius = 10f, center = node)
        }

        // Laser Scanner Beam Line sweeping vertically
        val laserY = h * scanY
        drawLine(
            brush = Brush.horizontalGradient(
                listOf(Color.Transparent, Color(0xFF8B5CF6), Color(0xFFA855F7), Color(0xFF8B5CF6), Color.Transparent)
            ),
            start = Offset(w * 0.1f, laserY),
            end = Offset(w * 0.9f, laserY),
            strokeWidth = 3f
        )
        // Laser glow effect
        drawRect(
            brush = Brush.verticalGradient(
                listOf(Color.Transparent, Color(0x228B5CF6), Color.Transparent)
            ),
            topLeft = Offset(w * 0.1f, laserY - 12f),
            size = Size(w * 0.8f, 24f)
        )
    }
}

/** 6. CapCut Master Hero: Editing timeline tracks, keyframe diamonds, transition pulse wave, floating layers */
@Composable
private fun CapCutMasterHeroCanvas() {
    val transition = rememberInfiniteTransition(label = "capcut")
    val playheadX by transition.animateFloat(
        initialValue = 0.15f, targetValue = 0.85f,
        animationSpec = infiniteRepeatable(tween(3200, easing = LinearEasing), RepeatMode.Restart),
        label = "playhead"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x3300E5FF), Color(0x112979FF), Color(0xFF0A0D12)),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.7f
            )
        )

        // Timeline tracks
        val track1Y = h * 0.35f
        val track2Y = h * 0.58f

        // Video track 1 clips
        drawRoundRect(
            color = Color(0x4400E5FF),
            topLeft = Offset(w * 0.15f, track1Y - 10f),
            size = Size(w * 0.32f, 20f),
            cornerRadius = CornerRadius(4f)
        )
        drawRoundRect(
            color = Color(0x4400E5FF),
            topLeft = Offset(w * 0.5f, track1Y - 10f),
            size = Size(w * 0.35f, 20f),
            cornerRadius = CornerRadius(4f)
        )

        // Audio track 2
        drawRoundRect(
            color = Color(0x332979FF),
            topLeft = Offset(w * 0.2f, track2Y - 8f),
            size = Size(w * 0.6f, 16f),
            cornerRadius = CornerRadius(4f)
        )

        // Keyframe Diamonds
        val keyframe1 = Offset(w * 0.3f, track1Y)
        val keyframe2 = Offset(w * 0.65f, track1Y)
        val diamondPath1 = Path().apply {
            moveTo(keyframe1.x, keyframe1.y - 6f)
            lineTo(keyframe1.x + 6f, keyframe1.y)
            lineTo(keyframe1.x, keyframe1.y + 6f)
            lineTo(keyframe1.x - 6f, keyframe1.y)
            close()
        }
        drawPath(diamondPath1, color = Color(0xFF00E5FF))

        val diamondPath2 = Path().apply {
            moveTo(keyframe2.x, keyframe2.y - 6f)
            lineTo(keyframe2.x + 6f, keyframe2.y)
            lineTo(keyframe2.x, keyframe2.y + 6f)
            lineTo(keyframe2.x - 6f, keyframe2.y)
            close()
        }
        drawPath(diamondPath2, color = Color(0xFF00E5FF))

        // Moving Playhead Line
        val currentPlayX = w * playheadX
        drawLine(
            color = Color(0xFF00E5FF),
            start = Offset(currentPlayX, h * 0.2f),
            end = Offset(currentPlayX, h * 0.8f),
            strokeWidth = 2.5f
        )
        // Playhead head handle
        drawCircle(color = Color(0xFF00E5FF), radius = 5f, center = Offset(currentPlayX, h * 0.2f))
    }
}

/** 7. VN Video Editor Hero: Audio waveform bars bouncing, video layer clips, cinematic pan motion */
@Composable
private fun VnEditorHeroCanvas() {
    val transition = rememberInfiniteTransition(label = "vnEditor")
    val wavePhase by transition.animateFloat(
        initialValue = 0f, targetValue = 6.28f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Restart),
        label = "phase"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x330288D1), Color(0x1100ACC1), Color(0xFF090C12)),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.7f
            )
        )

        // Bouncing Audio Waveform Bars
        val barCount = 18
        val barWidth = 6f
        val gap = (w * 0.7f) / barCount
        val startX = w * 0.15f
        val centerY = h * 0.5f

        for (i in 0 until barCount) {
            val bx = startX + i * gap
            val bh = 12f + 28f * (0.5f + 0.5f * sin((wavePhase + i * 0.4f).toDouble()).toFloat())
            drawLine(
                color = Color(0xFF0288D1),
                start = Offset(bx, centerY - bh / 2f),
                end = Offset(bx, centerY + bh / 2f),
                strokeWidth = barWidth,
                cap = StrokeCap.Round
            )
        }

        // Cinematic Crop Frame Overlay
        drawLine(color = Color(0x440288D1), start = Offset(w * 0.1f, h * 0.22f), end = Offset(w * 0.9f, h * 0.22f), strokeWidth = 1.5f)
        drawLine(color = Color(0x440288D1), start = Offset(w * 0.1f, h * 0.78f), end = Offset(w * 0.9f, h * 0.78f), strokeWidth = 1.5f)
    }
}

/** 8. Instagram Edits Hero: Before/After slider line sweeping horizontally, image enhancement sparkle nodes, clean editor interface */
@Composable
private fun InstagramEditsHeroCanvas() {
    val transition = rememberInfiniteTransition(label = "instaEdits")
    val sliderPos by transition.animateFloat(
        initialValue = 0.2f, targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(3000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "slider"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x33CFD8DC), Color(0x1190A4AE), Color(0xFF0B0D10)),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.7f
            )
        )

        // Split Before / After Image Box
        val boxX = w * 0.2f
        val boxY = h * 0.25f
        val boxW = w * 0.6f
        val boxH = h * 0.5f

        drawRoundRect(
            color = Color(0x22CFD8DC),
            topLeft = Offset(boxX, boxY),
            size = Size(boxW, boxH),
            cornerRadius = CornerRadius(12f)
        )
        drawRoundRect(
            color = Color(0xFFCFD8DC),
            topLeft = Offset(boxX, boxY),
            size = Size(boxW, boxH),
            cornerRadius = CornerRadius(12f),
            style = Stroke(width = 1.5f)
        )

        // Slider dividing line
        val lineX = boxX + boxW * (sliderPos - 0.2f) / 0.6f
        drawLine(
            color = Color.White,
            start = Offset(lineX, boxY),
            end = Offset(lineX, boxY + boxH),
            strokeWidth = 2.5f
        )
        drawCircle(color = Color.White, radius = 7f, center = Offset(lineX, boxY + boxH / 2f))
        drawCircle(color = Color(0xFFCFD8DC), radius = 5f, center = Offset(lineX, boxY + boxH / 2f))

        // Sparkles on Enhanced side (Right side of slider)
        val sparkleX = lineX + 20f
        if (sparkleX < boxX + boxW) {
            drawCircle(color = Color.White, radius = 3f, center = Offset(sparkleX + 10f, boxY + 15f))
            drawCircle(color = Color.White, radius = 2f, center = Offset(sparkleX + 30f, boxY + 30f))
        }
    }
}
