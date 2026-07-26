package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ExperienceSelectorScreen(
    onExperienceSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val isPreviewMode = LocalInspectionMode.current

    var selectedChoice by remember { mutableStateOf("SHOPPING") }
    var rememberChoice by remember { mutableStateOf(CreatorAcademyPrefs.isRememberExperience(context)) }

    val infiniteTransition = rememberInfiniteTransition(label = "expBg")
    val auroraOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "auroraOffset"
    )

    val iconFloatY by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconFloatY"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF040806),
                        Color(0xFF09140E),
                        Color(0xFF0A1119),
                        Color(0xFF030608)
                    ),
                    start = Offset(auroraOffset % 800f, 0f),
                    end = Offset((auroraOffset % 800f) + 600f, 1200f)
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
    ) {
        // Soft Ambient Aurora Glows & Particles
        if (!isPreviewMode) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            EmeraldPrimary.copy(alpha = 0.28f),
                            Color(0xFF00E5FF).copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    ),
                    radius = size.width * 0.65f,
                    center = Offset(size.width * 0.5f, size.height * 0.22f)
                )

                // Particles
                for (i in 0..16) {
                    val px = (sin((i * 1.6f + auroraOffset * 0.005f)) * 0.45f + 0.5f) * size.width
                    val py = (cos((i * 2.2f + auroraOffset * 0.004f)) * 0.45f + 0.5f) * size.height
                    drawCircle(
                        color = EmeraldGlow.copy(alpha = 0.25f),
                        radius = (2f + (i % 3) * 1.5f).dp.toPx(),
                        center = Offset(px, py)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                // Animated Floating Glass Icon
                Box(
                    modifier = Modifier
                        .graphicsLayer { translationY = if (!isPreviewMode) iconFloatY else 0f }
                        .size(68.dp)
                        .shadow(20.dp, CircleShape, spotColor = EmeraldGlow)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(EmeraldPrimary, EmeraldGlow, Color(0xFF00E5FF))
                            )
                        )
                        .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.85f)), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "ViralToolAI",
                        tint = AmoledBlack,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Choose Your Experience",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    letterSpacing = (-0.3).sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Select your primary workspace in ViralToolAI v1.0",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextWhite.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center
                )
            }

            // Interactive Glass Options
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Option 1: Shopping Intelligence
                ExperienceOptionCard(
                    title = "Shopping Intelligence",
                    subtitle = "AI Price Compare, Store Recommendations & Best Store Discovery",
                    badge = "ACTIVE ENGINE",
                    icon = Icons.Default.ShoppingBag,
                    isSelected = selectedChoice == "SHOPPING",
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        selectedChoice = "SHOPPING"
                    }
                )

                // Option 2: Creator Academy AI
                ExperienceOptionCard(
                    title = "Creator Academy AI",
                    subtitle = "Learn. Create. Grow. AI Mentorship & Viral Reel Scripts",
                    badge = "CREATOR STUDIO",
                    icon = Icons.Default.School,
                    isSelected = selectedChoice == "CREATOR_ACADEMY",
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        selectedChoice = "CREATOR_ACADEMY"
                    }
                )
            }

            // Bottom Confirm & Continue Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Remember Choice Checkbox Capsule
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x18FFFFFF))
                        .border(BorderStroke(1.dp, Color(0x28FFFFFF)), RoundedCornerShape(16.dp))
                        .clickable { rememberChoice = !rememberChoice }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Checkbox(
                        checked = rememberChoice,
                        onCheckedChange = { rememberChoice = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = EmeraldGlow,
                            uncheckedColor = TextWhite.copy(alpha = 0.45f),
                            checkmarkColor = AmoledBlack
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Remember my choice",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite.copy(alpha = 0.90f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                val buttonInteractionSource = remember { MutableInteractionSource() }
                val isPressed by buttonInteractionSource.collectIsPressedAsState()
                val scale by animateFloatAsState(
                    targetValue = if (isPressed) 0.94f else 1f,
                    animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
                    label = "confirmBtnScale"
                )

                val btnShimmerPos by infiniteTransition.animateFloat(
                    initialValue = -300f,
                    targetValue = 800f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(3200, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "btnShimmerPos"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .shadow(elevation = 20.dp, shape = RoundedCornerShape(29.dp), spotColor = EmeraldGlow)
                        .clip(RoundedCornerShape(29.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(EmeraldPrimary, EmeraldGlow, Color(0xFF00E5FF))
                            )
                        )
                        .border(
                            BorderStroke(
                                1.5.dp,
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.9f),
                                        Color.White.copy(alpha = 0.2f),
                                        Color.White.copy(alpha = 0.95f)
                                    ),
                                    start = Offset(btnShimmerPos, 0f),
                                    end = Offset(btnShimmerPos + 250f, 100f)
                                )
                            ),
                            RoundedCornerShape(29.dp)
                        )
                        .clickable(
                            interactionSource = buttonInteractionSource,
                            indication = androidx.compose.foundation.LocalIndication.current,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                CreatorAcademyPrefs.setExperienceChoice(context, selectedChoice, rememberChoice)
                                onExperienceSelected(selectedChoice)
                            }
                        )
                        .testTag("continue_experience_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Continue to Experience",
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.Black,
                            color = AmoledBlack,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = AmoledBlack,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExperienceOptionCard(
    title: String,
    subtitle: String,
    badge: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "optionScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (isSelected) 20.dp else 6.dp,
                shape = RoundedCornerShape(26.dp),
                spotColor = if (isSelected) EmeraldGlow else Color.Transparent,
                ambientColor = Color.Black
            )
            .clip(RoundedCornerShape(26.dp))
            .background(
                if (isSelected) {
                    Brush.verticalGradient(
                        listOf(Color(0xEE0B1A12), Color(0xF806110B))
                    )
                } else {
                    Brush.verticalGradient(
                        listOf(Color(0x18FFFFFF), Color(0x0CFFFFFF))
                    )
                }
            )
            .border(
                BorderStroke(
                    if (isSelected) 1.8.dp else 1.dp,
                    if (isSelected) {
                        Brush.horizontalGradient(
                            listOf(EmeraldGlow, Color(0xFF00E5FF), EmeraldGlow)
                        )
                    } else {
                        androidx.compose.ui.graphics.SolidColor(Color(0x28FFFFFF))
                    }
                ),
                RoundedCornerShape(26.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClick = onClick
            )
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .shadow(if (isSelected) 10.dp else 0.dp, CircleShape, spotColor = EmeraldGlow)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) {
                            Brush.linearGradient(listOf(EmeraldPrimary, EmeraldGlow))
                        } else {
                            androidx.compose.ui.graphics.SolidColor(Color(0x22FFFFFF))
                        }
                    )
                    .border(
                        BorderStroke(
                            1.dp,
                            if (isSelected) Color.White.copy(alpha = 0.85f) else Color(0x28FFFFFF)
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) AmoledBlack else TextWhite,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) EmeraldGlow.copy(alpha = 0.22f) else Color(0x1AFFFFFF))
                            .border(
                                BorderStroke(
                                    1.dp,
                                    if (isSelected) EmeraldGlow.copy(alpha = 0.7f) else Color.Transparent
                                ),
                                RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isSelected) EmeraldGlow else TextWhite.copy(alpha = 0.65f),
                            letterSpacing = 0.8.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextWhite.copy(alpha = 0.80f),
                    lineHeight = 17.sp
                )
            }

            if (isSelected) {
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .shadow(10.dp, CircleShape, spotColor = EmeraldGlow)
                        .clip(CircleShape)
                        .background(EmeraldGlow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = AmoledBlack,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
