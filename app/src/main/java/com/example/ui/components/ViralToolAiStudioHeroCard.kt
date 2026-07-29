package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ViralToolAiStudioHeroCard(
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val infiniteTransition = rememberInfiniteTransition(label = "studioHeroAnims")

    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "studioShimmer"
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "studioPulse"
    )

    val cardShape = RoundedCornerShape(24.dp)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .materialSharedBounds("viraltool_ai_studio_tool_card")
            .shadow(
                elevation = 18.dp,
                shape = cardShape,
                ambientColor = EmeraldPrimary,
                spotColor = ElectricPurple
            )
            .clip(cardShape)
            .border(
                BorderStroke(
                    1.5.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            EmeraldGlow.copy(alpha = pulseAlpha),
                            ElectricPurple.copy(alpha = 0.8f),
                            EmeraldPrimary.copy(alpha = pulseAlpha)
                        ),
                        start = Offset(shimmerOffset, 0f),
                        end = Offset(shimmerOffset + 400f, 300f)
                    )
                ),
                cardShape
            )
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
        color = Color(0xFF0D1411),
        shape = cardShape
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF111D18),
                            Color(0xFF0C1310),
                            Color(0xFF151022)
                        )
                    )
                )
                .padding(18.dp)
        ) {
            // Background Canvas Shimmer
            Canvas(modifier = Modifier.matchParentSize()) {
                val sweepX = shimmerOffset
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.03f),
                            Color.White.copy(alpha = 0.15f),
                            Color.White.copy(alpha = 0.03f),
                            Color.Transparent
                        )
                    ),
                    start = Offset(sweepX, 0f),
                    end = Offset(sweepX + 220f, size.height),
                    strokeWidth = 40.dp.toPx()
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                // TOP TAG + BADGE ROW
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(EmeraldPrimary, ElectricPurple)
                                )
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "FLAGSHIP TOOL #1",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Gemini 3.1 & Veo Powered",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGlow
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // TOOL TITLE & TAGLINE
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .shadow(10.dp, RoundedCornerShape(16.dp), spotColor = EmeraldGlow)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(EmeraldPrimary, ElectricPurple)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Studio Icon",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "ViralToolAI Studio",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                        Text(
                            text = "All-In-One AI Studio: Image, Veo Video & Grounded Intelligence",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextWhite.copy(alpha = 0.75f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // QUICK FEATURE CHIPS
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val chips = listOf(
                        "🖼️ Create & Edit Images",
                        "🎬 Animate Image to Video",
                        "🎥 Text to Veo Video",
                        "🎨 1K/2K/4K Images",
                        "🧠 High Thinking Mode",
                        "🌐 Search Grounding",
                        "⚡ Low Latency"
                    )
                    items(chips) { chip ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF16221D))
                                .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.35f)), RoundedCornerShape(10.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = chip,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite.copy(alpha = 0.9f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // LAUNCH BUTTON
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(EmeraldPrimary, ElectricPurple)
                            )
                        )
                        .border(BorderStroke(1.dp, EmeraldGlow), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Open ViralToolAI Studio",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
