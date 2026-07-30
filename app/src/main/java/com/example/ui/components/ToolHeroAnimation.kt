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
    INSTAGRAM_EDITS,
    AI_VIDEO_IMAGE
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
            ToolHeroType.AI_VIDEO_IMAGE -> AiVideoImageHeroCanvas()
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
                        ToolHeroType.BRAND_COLLAB -> Color(0xFFE2E8F0)
                        ToolHeroType.MEESHO_CREATOR -> Color(0xFFFF4081)
                        ToolHeroType.INSTAGRAM_CREATOR -> Color(0xFFE1306C)
                        ToolHeroType.YOUTUBE_CREATOR -> Color(0xFFFF0000)
                        ToolHeroType.AI_PROMPT_EXTRACTOR -> Color(0xFF8B5CF6)
                        ToolHeroType.CAPCUT_MASTER -> Color(0xFF00E5FF)
                        ToolHeroType.VN_EDITOR -> Color(0xFF0288D1)
                        ToolHeroType.INSTAGRAM_EDITS -> Color(0xFFCFD8DC)
                        ToolHeroType.AI_VIDEO_IMAGE -> Color(0xFF8B5CF6)
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

/** 1. Brand Collaboration Hero: Premium Titanium & Silver Luxury Collab Canvas (Handshake, Contract, Luxury Brand Badges, Soft White Glow) */
@Composable
private fun BrandCollabHeroCanvas() {
    val transition = rememberInfiniteTransition(label = "brandCollab")
    val sweepProgress by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4500, easing = LinearEasing), RepeatMode.Restart),
        label = "sweep"
    )
    val floatY by transition.animateFloat(
        initialValue = -7f, targetValue = 7f,
        animationSpec = infiniteRepeatable(tween(2800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "float"
    )
    val rotationAngle by transition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(14000, easing = LinearEasing), RepeatMode.Restart),
        label = "rotation"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Deep Graphite & Titanium Radial Background
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF333846), Color(0xFF1B1D26), Color(0xFF0F1017)),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.75f
            )
        )

        // Silver & Platinum Particles
        val particleCount = 14
        for (i in 0 until particleCount) {
            val px = (w * (0.05f + 0.9f * ((i * 37) % 100) / 100f))
            val py = (h * (0.12f + 0.76f * ((i * 53) % 100) / 100f) + sin((sweepProgress * 6.28f + i).toDouble()).toFloat() * 5f)
            val pRadius = 2f + (i % 3) * 1.5f
            drawCircle(
                color = Color(0xFFE2E8F0).copy(alpha = 0.35f + 0.45f * sin((sweepProgress * 3.14f + i).toDouble()).toFloat()),
                radius = pRadius,
                center = Offset(px, py)
            )
        }

        val centerX = w * 0.5f
        val centerY = h * 0.48f + floatY

        // Rotating Silver & Titanium Outer Seal Ring
        rotate(degrees = rotationAngle, pivot = Offset(centerX, centerY)) {
            drawCircle(
                brush = Brush.sweepGradient(listOf(Color(0xFFF1F5F9), Color(0xFF64748B), Color(0xFFE2E8F0))),
                radius = 36f,
                center = Offset(centerX, centerY),
                style = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f))
            )
        }

        // Inner Titanium Glass Disc
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF475569), Color(0xFF1E293B)),
                center = Offset(centerX, centerY),
                radius = 32f
            ),
            radius = 32f,
            center = Offset(centerX, centerY)
        )
        drawCircle(
            color = Color(0x66E2E8F0),
            radius = 32f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 1.5f)
        )

        // Creator & Brand Handshake Icon Graphic
        val handshakePath = Path().apply {
            moveTo(centerX - 16f, centerY - 2f)
            lineTo(centerX - 5f, centerY + 9f)
            lineTo(centerX + 3f, centerY + 1f)
            lineTo(centerX + 16f, centerY - 5f)
        }
        drawPath(
            path = handshakePath,
            color = Color(0xFFF8FAFC),
            style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Floating Luxury Brand Star / Diamond Badge (Left)
        val leftX = w * 0.16f
        val leftY = h * 0.35f + floatY * 0.6f
        drawRoundRect(
            brush = Brush.linearGradient(listOf(Color(0xFF334155), Color(0xFF1E293B))),
            topLeft = Offset(leftX, leftY),
            size = Size(52f, 28f),
            cornerRadius = CornerRadius(10f)
        )
        drawRoundRect(
            color = Color(0x88CBD5E1),
            topLeft = Offset(leftX, leftY),
            size = Size(52f, 28f),
            cornerRadius = CornerRadius(10f),
            style = Stroke(width = 1.2f)
        )
        // Diamond / Star symbol inside left card
        drawCircle(
            color = Color(0xFFE2E8F0),
            radius = 5f,
            center = Offset(leftX + 16f, leftY + 14f)
        )

        // Floating Camera / Creator Studio Box (Right)
        val rightX = w * 0.74f
        val rightY = h * 0.38f - floatY * 0.6f
        drawRoundRect(
            brush = Brush.linearGradient(listOf(Color(0xFF334155), Color(0xFF1E293B))),
            topLeft = Offset(rightX, rightY),
            size = Size(48f, 28f),
            cornerRadius = CornerRadius(10f)
        )
        drawRoundRect(
            color = Color(0x88CBD5E1),
            topLeft = Offset(rightX, rightY),
            size = Size(48f, 28f),
            cornerRadius = CornerRadius(10f),
            style = Stroke(width = 1.2f)
        )
        // Camera lens graphic inside right card
        drawCircle(
            color = Color(0xFFE2E8F0),
            radius = 6f,
            center = Offset(rightX + 24f, rightY + 14f),
            style = Stroke(width = 2f)
        )

        // Premium Metallic Silver Sweep Animation
        val shineX = -w * 0.5f + (w * 2f) * sweepProgress
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(Color.Transparent, Color(0x35E2E8F0), Color.Transparent),
                start = Offset(shineX, 0f),
                end = Offset(shineX + 110f, h)
            )
        )
    }
}

/** 2. Meesho Creator Hero: Floating shopping bags, product cards, affiliate commission coins, pink particles */
@Composable
private fun MeeshoHeroCanvas() {
    val transition = rememberInfiniteTransition(label = "meeshoHero")
    val pulse by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3500, easing = LinearEasing), RepeatMode.Restart),
        label = "pulse"
    )
    val floatY by transition.animateFloat(
        initialValue = -7f, targetValue = 7f,
        animationSpec = infiniteRepeatable(tween(2400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "float"
    )
    val sweepProgress by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Restart),
        label = "sweep"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Deep Luxury Dark Pink/Magenta Radial Backdrop
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x44FF2A7A), Color(0x22E91E63), Color(0xFF140B13)),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.75f
            )
        )

        // Floating Shopping Bag (Left)
        val bagLeft = w * 0.18f
        val bagTop = h * 0.32f + floatY
        drawRoundRect(
            brush = Brush.linearGradient(listOf(Color(0xFFFF2A7A), Color(0xFFE91E63))),
            topLeft = Offset(bagLeft, bagTop),
            size = Size(42f, 50f),
            cornerRadius = CornerRadius(8f)
        )
        drawRoundRect(
            color = Color(0xFFFFFFFF),
            topLeft = Offset(bagLeft, bagTop),
            size = Size(42f, 50f),
            cornerRadius = CornerRadius(8f),
            style = Stroke(width = 1.5f)
        )
        // Bag Handles
        drawArc(
            color = Color(0xFFFFFFFF),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(bagLeft + 11f, bagTop - 12f),
            size = Size(20f, 18f),
            style = Stroke(width = 2f, cap = StrokeCap.Round)
        )

        // Center Product Box / Gift Box (With Cross Ribbon)
        val cardX = w * 0.5f - 28f
        val cardY = h * 0.28f - floatY * 0.8f
        drawRoundRect(
            brush = Brush.linearGradient(listOf(Color(0x66FF2A7A), Color(0x33E91E63))),
            topLeft = Offset(cardX, cardY),
            size = Size(56f, 62f),
            cornerRadius = CornerRadius(10f)
        )
        drawRoundRect(
            color = Color(0xFFFF2A7A),
            topLeft = Offset(cardX, cardY),
            size = Size(56f, 62f),
            cornerRadius = CornerRadius(10f),
            style = Stroke(width = 1.8f)
        )
        // Ribbon Vertical
        drawLine(
            color = Color(0xFFFFFFFF),
            start = Offset(cardX + 28f, cardY),
            end = Offset(cardX + 28f, cardY + 62f),
            strokeWidth = 3f
        )
        // Ribbon Horizontal
        drawLine(
            color = Color(0xFFFFFFFF),
            start = Offset(cardX, cardY + 31f),
            end = Offset(cardX + 56f, cardY + 31f),
            strokeWidth = 3f
        )
        // Bow tie at top
        drawCircle(color = Color(0xFFFF2A7A), radius = 6f, center = Offset(cardX + 28f, cardY + 31f))

        // Right Floating Commission Coins & Tag (Right)
        val coinX = w * 0.80f
        val coinY = h * 0.38f + floatY * 0.9f
        drawCircle(
            brush = Brush.radialGradient(listOf(Color(0xFFFF2A7A), Color(0xFFE91E63))),
            radius = 18f,
            center = Offset(coinX, coinY)
        )
        drawCircle(
            color = Color(0xFFFFFFFF),
            radius = 18f,
            center = Offset(coinX, coinY),
            style = Stroke(width = 1.5f)
        )
        drawCircle(
            color = Color(0x55FF2A7A),
            radius = 12f,
            center = Offset(coinX, coinY)
        )

        // Smaller secondary Coin
        drawCircle(
            brush = Brush.radialGradient(listOf(Color(0xFFFF5252), Color(0xFFE91E63))),
            radius = 12f,
            center = Offset(coinX - 18f, coinY + 22f)
        )
        drawCircle(
            color = Color(0xFFFFFFFF),
            radius = 12f,
            center = Offset(coinX - 18f, coinY + 22f),
            style = Stroke(width = 1.2f)
        )

        // Pink Glowing Particle Stars
        for (i in 0..12) {
            val px = (w * (0.08f + 0.84f * ((i * 37) % 100) / 100f))
            val py = (h * (0.15f + 0.7f * ((i * 59) % 100) / 100f) + cos((pulse * 6.28f + i).toDouble()).toFloat() * 6f)
            val alpha = 0.25f + 0.65f * sin((pulse * 3.14f + i).toDouble()).toFloat().coerceIn(0f, 1f)
            drawCircle(
                color = Color(0xFFFF2A7A).copy(alpha = alpha),
                radius = if (i % 2 == 0) 3.5f else 2f,
                center = Offset(px, py)
            )
        }

        // Luxury Pink Sweep Shine Effect
        val shineX = -w * 0.5f + (w * 2f) * sweepProgress
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(Color.Transparent, Color(0x40FF2A7A), Color(0x60FFFFFF), Color(0x40FF2A7A), Color.Transparent),
                start = Offset(shineX, 0f),
                end = Offset(shineX + 120f, h)
            )
        )
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

/** 9. AI Video & Image Generator Hero: Animated soft AI particles, glowing futuristic light waves, purple/violet motion rings */
@Composable
private fun AiVideoImageHeroCanvas() {
    val transition = rememberInfiniteTransition(label = "aiVideoImage")
    val pulse by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3200, easing = LinearEasing), RepeatMode.Restart),
        label = "pulse"
    )
    val floatY by transition.animateFloat(
        initialValue = -8f, targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "float"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Deep Violet Futuristic Backdrop
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x448B5CF6), Color(0x227C3AED), Color(0xFF100B1E)),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.75f
            )
        )

        // Pulsing Futuristic Glowing Aperture Rings in Center
        val centerX = w * 0.5f
        val centerY = h * 0.45f + floatY

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0x668B5CF6), Color(0x22A78BFA), Color.Transparent),
                center = Offset(centerX, centerY),
                radius = 60f
            ),
            radius = 60f,
            center = Offset(centerX, centerY)
        )

        drawCircle(
            color = Color(0xFFA78BFA),
            radius = 32f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 2f)
        )

        drawCircle(
            color = Color(0xFF8B5CF6),
            radius = 22f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 1.5f)
        )

        drawCircle(
            color = Color(0xFFC084FC),
            radius = 12f,
            center = Offset(centerX, centerY)
        )

        // Floating Lens/Frame Rectangles on Left & Right
        val frameLeftX = w * 0.2f
        val frameLeftY = h * 0.35f - floatY * 0.7f
        drawRoundRect(
            brush = Brush.linearGradient(listOf(Color(0xFF8B5CF6), Color(0xFFA855F7))),
            topLeft = Offset(frameLeftX, frameLeftY),
            size = Size(40f, 48f),
            cornerRadius = CornerRadius(8f),
            style = Stroke(width = 1.8f)
        )

        val frameRightX = w * 0.78f
        val frameRightY = h * 0.38f + floatY * 0.8f
        drawRoundRect(
            brush = Brush.linearGradient(listOf(Color(0xFFA78BFA), Color(0xFFC084FC))),
            topLeft = Offset(frameRightX, frameRightY),
            size = Size(42f, 28f),
            cornerRadius = CornerRadius(6f),
            style = Stroke(width = 1.8f)
        )

        // Soft AI Particles & Glowing Light Orbs
        for (i in 0..15) {
            val px = (w * (0.05f + 0.9f * ((i * 43) % 100) / 100f))
            val py = (h * (0.15f + 0.7f * ((i * 61) % 100) / 100f) + sin((pulse * 6.28f + i).toDouble()).toFloat() * 6f)
            val alpha = (0.3f + 0.6f * sin((pulse * 3.14f + i * 0.5f).toDouble()).toFloat()).coerceIn(0.1f, 1f)
            
            drawCircle(
                color = if (i % 2 == 0) Color(0xFF8B5CF6).copy(alpha = alpha) else Color(0xFFC084FC).copy(alpha = alpha),
                radius = if (i % 3 == 0) 3.5f else 2f,
                center = Offset(px, py)
            )
        }

        // Futuristic Sweep Shine Effect
        val shineX = -w * 0.5f + (w * 2f) * pulse
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(Color.Transparent, Color(0x338B5CF6), Color(0x60A78BFA), Color(0x338B5CF6), Color.Transparent),
                start = Offset(shineX, 0f),
                end = Offset(shineX + 130f, h)
            )
        )
    }
}
