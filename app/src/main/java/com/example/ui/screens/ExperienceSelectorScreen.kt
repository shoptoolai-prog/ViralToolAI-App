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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite

@Composable
fun ExperienceSelectorScreen(
    onExperienceSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

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
                        Color(0xFF060B08),
                        Color(0xFF0C1712),
                        Color(0xFF09121B),
                        Color(0xFF05080A)
                    ),
                    start = Offset(auroraOffset % 800f, 0f),
                    end = Offset((auroraOffset % 800f) + 600f, 1200f)
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
    ) {
        // Soft Ambient Aurora Glows
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        EmeraldPrimary.copy(alpha = 0.25f),
                        Color.Transparent
                    )
                ),
                radius = size.width * 0.6f,
                center = Offset(size.width * 0.5f, size.height * 0.2f)
            )
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
                Box(
                    modifier = Modifier
                        .graphicsLayer { translationY = iconFloatY }
                        .size(64.dp)
                        .shadow(16.dp, CircleShape, spotColor = EmeraldGlow)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF132019), Color(0xFF0B1712))
                            )
                        )
                        .border(BorderStroke(1.5.dp, EmeraldGlow), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "ViralToolAI",
                        tint = EmeraldGlow,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Choose Your Experience",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    letterSpacing = (-0.3).sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Select your primary workspace in ViralToolAI v1.0",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextWhite.copy(alpha = 0.7f),
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
                    subtitle = "AI Price Compare, Store Recommendations & Creator Monetization",
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
                    subtitle = "Learn. Create. Grow. AI Mentorship & Personalized Creator Roadmaps",
                    badge = "NEW MODULE",
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x10FFFFFF))
                        .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(14.dp))
                        .clickable { rememberChoice = !rememberChoice }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Checkbox(
                        checked = rememberChoice,
                        onCheckedChange = { rememberChoice = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = EmeraldGlow,
                            uncheckedColor = TextWhite.copy(alpha = 0.4f),
                            checkmarkColor = AmoledBlack
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Remember my choice",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite.copy(alpha = 0.85f)
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
                        .height(56.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .shadow(elevation = 16.dp, shape = RoundedCornerShape(28.dp), spotColor = EmeraldGlow)
                        .clip(RoundedCornerShape(28.dp))
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
                                        Color.White.copy(alpha = 0.8f),
                                        Color.White.copy(alpha = 0.2f),
                                        Color.White.copy(alpha = 0.9f)
                                    ),
                                    start = Offset(btnShimmerPos, 0f),
                                    end = Offset(btnShimmerPos + 250f, 100f)
                                )
                            ),
                            RoundedCornerShape(28.dp)
                        )
                        .clickable(
                            interactionSource = buttonInteractionSource,
                            indication = androidx.compose.foundation.LocalIndication.current,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                CreatorAcademyPrefs.setExperienceChoice(context, selectedChoice, rememberChoice)
                                onExperienceSelected(selectedChoice)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Continue to Experience",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = AmoledBlack,
                        letterSpacing = 0.5.sp
                    )
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
                elevation = if (isSelected) 16.dp else 4.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = if (isSelected) EmeraldGlow else Color.Transparent,
                ambientColor = Color.Black
            )
            .clip(RoundedCornerShape(22.dp))
            .background(
                if (isSelected) {
                    Brush.linearGradient(
                        listOf(Color(0xFF132219), Color(0xFF0F1B14))
                    )
                } else {
                    Brush.linearGradient(
                        listOf(Color(0x12FFFFFF), Color(0x08FFFFFF))
                    )
                }
            )
            .border(
                BorderStroke(
                    if (isSelected) 1.8.dp else 1.dp,
                    if (isSelected) {
                        Brush.linearGradient(
                            listOf(EmeraldGlow, Color(0xFF00E5FF))
                        )
                    } else {
                        androidx.compose.ui.graphics.SolidColor(Color(0x22FFFFFF))
                    }
                ),
                RoundedCornerShape(22.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClick = onClick
            )
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) {
                            Brush.linearGradient(listOf(EmeraldPrimary, EmeraldGlow))
                        } else {
                            androidx.compose.ui.graphics.SolidColor(Color(0x18FFFFFF))
                        }
                    )
                    .border(
                        BorderStroke(
                            1.dp,
                            if (isSelected) Color.White.copy(alpha = 0.8f) else Color(0x22FFFFFF)
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) AmoledBlack else TextWhite,
                    modifier = Modifier.size(25.dp)
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) EmeraldGlow.copy(alpha = 0.22f) else Color(0x1AFFFFFF))
                            .border(
                                BorderStroke(
                                    1.dp,
                                    if (isSelected) EmeraldGlow.copy(alpha = 0.6f) else Color.Transparent
                                ),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isSelected) EmeraldGlow else TextWhite.copy(alpha = 0.65f),
                            letterSpacing = 0.8.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    fontSize = 11.5.sp,
                    color = TextWhite.copy(alpha = 0.75f),
                    lineHeight = 16.sp
                )
            }

            if (isSelected) {
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .shadow(8.dp, CircleShape, spotColor = EmeraldGlow)
                        .clip(CircleShape)
                        .background(EmeraldGlow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = AmoledBlack,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}
