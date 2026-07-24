package com.example.core

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CrimsonLight
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import kotlin.math.cos
import kotlin.math.sin

/**
 * SHOPTOOLAI Phase 6B — Premium AI Core Scanning Visual Effect Component
 */
@Composable
fun AiCoreScanningEffect(
    stageLabel: String,
    progressPercent: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "AiCoreGlow")

    // Pulsing Core Glow
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    // Particle / Ring Rotation
    val rotationDegrees by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RotationDegrees"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        CrimsonRed.copy(alpha = 0.22f),
                        Color(0x18000000),
                        Color(0x05000000)
                    )
                )
            )
            .border(
                width = 1.2.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(CrimsonRed, CrimsonLight, Color(0x33FFFFFF))
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Animated AI Core Particle Orb Canvas
            Box(
                modifier = Modifier
                    .size(90.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val radius = (size.width / 2.6f) * pulseScale

                    // Outer glowing aura ring
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(CrimsonRed.copy(alpha = 0.5f), Color.Transparent),
                            center = center,
                            radius = radius * 1.5f
                        )
                    )

                    // Rotating particle points
                    val radians = Math.toRadians(rotationDegrees.toDouble())
                    for (i in 0..5) {
                        val angle = radians + (i * Math.PI / 3)
                        val px = center.x + (radius * cos(angle)).toFloat()
                        val py = center.y + (radius * sin(angle)).toFloat()
                        drawCircle(
                            color = CrimsonLight,
                            radius = 3.5f,
                            center = Offset(px, py)
                        )
                    }

                    // Inner Neural Core Ring
                    drawCircle(
                        color = CrimsonRed,
                        center = center,
                        radius = radius * 0.7f,
                        style = Stroke(width = 2.5f)
                    )

                    drawCircle(
                        color = TextWhite,
                        center = center,
                        radius = radius * 0.3f
                    )
                }
            }

            // Stage Label & Progress Text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stageLabel,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "AI Neural Engine • $progressPercent%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextGray
                )
            }
        }
    }
}
