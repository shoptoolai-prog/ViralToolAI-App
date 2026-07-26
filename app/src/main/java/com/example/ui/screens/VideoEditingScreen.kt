package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite

/**
 * MASTER PHASE — Mobile Video Editing AI Screen
 * Rendered when user taps the "Video Editing" bottom navigation tab.
 */
@Composable
fun VideoEditingScreen(
    onNavigateToHome: () -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    var isSubscribedWaitlist by remember { mutableStateOf(false) }

    // One-time entrance animation sequence
    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }
    val animVal = animProgress.value

    // Continuous 60 FPS micro-animations
    val infiniteTransition = rememberInfiniteTransition(label = "videoEditingMicroAnims")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            animation = tween(3800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )
    val floatY by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatY"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 110.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // ==================================================
            // HEADER BANNER
            // ==================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp)
                    .shadow(
                        elevation = 14.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = EmeraldPrimary.copy(alpha = 0.35f),
                        ambientColor = Color.Black
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF131A16), Color(0xFF0A0F0D))
                        )
                    )
                    .border(
                        BorderStroke(
                            1.2.dp,
                            Brush.linearGradient(
                                colors = listOf(
                                    EmeraldPrimary.copy(alpha = 0.7f),
                                    ElectricPurple.copy(alpha = 0.45f),
                                    EmeraldGlow.copy(alpha = 0.7f)
                                ),
                                start = Offset(shimmerOffset, 0f),
                                end = Offset(shimmerOffset + 400f, 250f)
                            )
                        ),
                        RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.graphicsLayer {
                                translationY = floatY.dp.toPx()
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = EmeraldPrimary)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF1B2820), Color(0xFF0D1610))
                                        )
                                    )
                                    .border(
                                        BorderStroke(1.2.dp, EmeraldGlow),
                                        RoundedCornerShape(14.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = "Video Editing AI",
                                    tint = EmeraldGlow,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Video Editing AI",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Black,
                                style = androidx.compose.ui.text.TextStyle(
                                    brush = Brush.horizontalGradient(
                                        listOf(TextWhite, Color(0xFFE2F3EB), EmeraldGlow)
                                    )
                                ),
                                letterSpacing = (-0.3).sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Mobile Reels & Shorts Engine",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = EmeraldPrimary,
                                letterSpacing = 1.2.sp
                            )
                        }
                    }

                    // Status pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(EmeraldPrimary.copy(alpha = 0.18f))
                            .border(BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.6f)), RoundedCornerShape(16.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "🔒 NEXT PHASE",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            // ==================================================
            // HERO PROMO CARD
            // ==================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF1A2620), Color(0xFF0D1411))
                        )
                    )
                    .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.35f)), RoundedCornerShape(22.dp))
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimary.copy(alpha = 0.2f))
                            .border(BorderStroke(1.dp, EmeraldGlow), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Movie,
                            contentDescription = null,
                            tint = EmeraldGlow,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "AI-Powered Mobile Video Suite",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Turn raw clips into viral Instagram Reels and YouTube Shorts automatically with zero editing skills required.",
                        fontSize = 12.5.sp,
                        color = TextWhite.copy(alpha = 0.75f),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Join Waitlist / Notify Button
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val buttonScale by animateFloatAsState(
                        targetValue = if (isPressed) 0.94f else 1f,
                        animationSpec = spring(stiffness = 300f),
                        label = "buttonScale"
                    )

                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = buttonScale
                                scaleY = buttonScale
                            }
                            .fillMaxWidth()
                            .height(48.dp)
                            .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = EmeraldPrimary)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                if (isSubscribedWaitlist) {
                                    Brush.horizontalGradient(listOf(Color(0xFF10B981), Color(0xFF059669)))
                                } else {
                                    Brush.horizontalGradient(listOf(EmeraldPrimary, EmeraldGlow))
                                }
                            )
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isSubscribedWaitlist = !isSubscribedWaitlist
                                val msg = if (isSubscribedWaitlist) "🎉 You're registered for early Video Editing AI access!" else "Notification preference updated"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isSubscribedWaitlist) Icons.Default.Check else Icons.Default.Notifications,
                                contentDescription = null,
                                tint = AmoledBlack,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isSubscribedWaitlist) "VIP EARLY ACCESS ACTIVE" else "NOTIFY ME ON LAUNCH",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Black,
                                color = AmoledBlack,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==================================================
            // UPCOMING FEATURES LIST
            // ==================================================
            Text(
                text = "PLANNED AI EDITING MODULES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite.copy(alpha = 0.5f),
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                VideoFeatureCard(
                    icon = Icons.Default.ElectricBolt,
                    title = "Smart Auto-Cut & Silence Trimmer",
                    subtitle = "Instantly removes dead pauses, stutters, and background noise in one tap."
                )

                VideoFeatureCard(
                    icon = Icons.Default.Subtitles,
                    title = "Dynamic Auto Subtitles & Captions",
                    subtitle = "Generates high-retention animated subtitles with word-by-word highlights."
                )

                VideoFeatureCard(
                    icon = Icons.Default.MusicNote,
                    title = "Viral Sound & Beat Sync Analyzer",
                    subtitle = "Matches video transitions with trending audio beats for maximum retention."
                )

                VideoFeatureCard(
                    icon = Icons.Default.AutoAwesome,
                    title = "AI Color Grading & Cinematic LUTs",
                    subtitle = "Applies 4K HDR iphone-style cinema color presets automatically."
                )
            }
        }
    }
}

@Composable
private fun VideoFeatureCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF121815))
            .border(BorderStroke(1.dp, Color(0x2210B981)), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x2210B981)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = EmeraldGlow,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.5.sp,
                    color = TextWhite.copy(alpha = 0.65f),
                    lineHeight = 15.sp
                )
            }
        }
    }
}
