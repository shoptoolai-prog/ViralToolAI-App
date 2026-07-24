package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CropOriginal
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocr.OcrScanStage
import com.example.ui.theme.*
import kotlin.random.Random

/**
 * SHOPTOOLAI Phase 8C — Premium Scanning Experience Component
 * Features:
 * - Moving laser scan line
 * - Animated AI Matrix Grid overlay
 * - Floating glowing light particles
 * - Glassmorphic high-contrast frame
 * - 5-Stage Luxury progress indicator
 */

data class FloatingParticle(
    val xRatio: Float,
    val initialYRatio: Float,
    val radius: Float,
    val speed: Float,
    val alpha: Float
)

@Composable
fun OcrScannerOverlay(
    currentStage: OcrScanStage,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    // Laser line animation
    val infiniteTransition = rememberInfiniteTransition(label = "OcrScannerTransition")
    val scanProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LaserScanProgress"
    )

    val particleAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ParticleProgress"
    )

    // Pre-calculated floating particles
    val particles = remember {
        List(18) {
            FloatingParticle(
                xRatio = Random.nextFloat(),
                initialYRatio = Random.nextFloat(),
                radius = Random.nextFloat() * 3f + 1.5f,
                speed = Random.nextFloat() * 0.4f + 0.2f,
                alpha = Random.nextFloat() * 0.6f + 0.3f
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0x11FF0033),
                        AmoledBlack,
                        Color(0x22000000)
                    )
                )
            )
            .border(
                BorderStroke(1.2.dp, Brush.linearGradient(listOf(CrimsonRed, CrimsonLight, Color(0x33FFFFFF)))),
                RoundedCornerShape(22.dp)
            )
            .padding(18.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CrimsonRed.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DocumentScanner,
                            contentDescription = "OCR Engine",
                            tint = CrimsonLight,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "UNIVERSAL OCR & VISION INTELLIGENCE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = CrimsonLight,
                            letterSpacing = 1.1.sp
                        )
                        Text(
                            text = "Structured Vision Analysis",
                            fontSize = 9.sp,
                            color = TextGray
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI Active",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(18.dp)
                )
            }

            // Scanner Canvas area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22000000))
                    .border(BorderStroke(1.dp, Color(0x33FFFFFF)), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // 1. Draw AI Matrix Grid
                    val gridStep = 24f
                    var x = 0f
                    while (x < w) {
                        drawLine(
                            color = Color(0x11FFFFFF),
                            start = Offset(x, 0f),
                            end = Offset(x, h),
                            strokeWidth = 1f
                        )
                        x += gridStep
                    }
                    var y = 0f
                    while (y < h) {
                        drawLine(
                            color = Color(0x11FFFFFF),
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1f
                        )
                        y += gridStep
                    }

                    // 2. Draw Floating Particles
                    particles.forEach { p ->
                        val currentY = (p.initialYRatio + (particleAnim * p.speed)) % 1f
                        val pX = p.xRatio * w
                        val pY = currentY * h
                        drawCircle(
                            color = CrimsonLight.copy(alpha = p.alpha),
                            radius = p.radius,
                            center = Offset(pX, pY)
                        )
                    }

                    // 3. Draw Moving Laser Scan Line
                    val scanY = h * scanProgress
                    drawLine(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                CrimsonRed.copy(alpha = 0.3f),
                                CrimsonRed,
                                CrimsonLight,
                                CrimsonRed,
                                CrimsonRed.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        ),
                        start = Offset(0f, scanY),
                        end = Offset(w, scanY),
                        strokeWidth = 5f
                    )

                    // Laser Glow Beam
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                CrimsonRed.copy(alpha = 0.25f),
                                Color.Transparent
                            ),
                            startY = scanY,
                            endY = (scanY + 30f).coerceAtMost(h)
                        ),
                        topLeft = Offset(0f, scanY),
                        size = Size(w, 30f)
                    )

                    // 4. Target Focus Reticle Corners
                    val margin = 20f
                    val cornerLen = 28f
                    val strokeW = 3f

                    // Top Left
                    drawLine(CrimsonLight, Offset(margin, margin), Offset(margin + cornerLen, margin), strokeW)
                    drawLine(CrimsonLight, Offset(margin, margin), Offset(margin, margin + cornerLen), strokeW)

                    // Top Right
                    drawLine(CrimsonLight, Offset(w - margin, margin), Offset(w - margin - cornerLen, margin), strokeW)
                    drawLine(CrimsonLight, Offset(w - margin, margin), Offset(w - margin, margin + cornerLen), strokeW)

                    // Bottom Left
                    drawLine(CrimsonLight, Offset(margin, h - margin), Offset(margin + cornerLen, h - margin), strokeW)
                    drawLine(CrimsonLight, Offset(margin, h - margin), Offset(margin, h - margin - cornerLen), strokeW)

                    // Bottom Right
                    drawLine(CrimsonLight, Offset(w - margin, h - margin), Offset(w - margin - cornerLen, h - margin), strokeW)
                    drawLine(CrimsonLight, Offset(w - margin, h - margin), Offset(w - margin, h - margin - cornerLen), strokeW)
                }

                // Center Icon / Overlay Text
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CropOriginal,
                        contentDescription = "Scanning",
                        tint = CrimsonLight,
                        modifier = Modifier.size(38.dp)
                    )
                    Text(
                        text = "LIVE VISION MATRIX SCAN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Error or Progress Display
            if (currentStage == OcrScanStage.ERROR || errorMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x22FF2E44))
                        .border(BorderStroke(1.dp, CrimsonRed), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = CrimsonRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = errorMessage ?: "Unable to verify text from image. Please try a clearer screenshot.",
                            fontSize = 11.sp,
                            color = TextWhite,
                            lineHeight = 15.sp
                        )
                    }
                }
            } else {
                // Progress Bar & Stage Description
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentStage.label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CrimsonLight
                        )
                        Text(
                            text = "${currentStage.progressPercent}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = TextGray
                        )
                    }

                    LinearProgressIndicator(
                        progress = { currentStage.progressPercent / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = CrimsonRed,
                        trackColor = Color(0x22FFFFFF)
                    )
                }
            }
        }
    }
}
