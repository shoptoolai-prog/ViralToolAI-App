package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalResponsiveMetrics
import com.example.ui.theme.responsiveCardBounds

import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = Color(0x33FF2E44), // Subtle crimson border default
    backgroundColor: Color = Color(0x1AFFFFFF), // Minimal white glass background
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val responsiveMetrics = LocalResponsiveMetrics.current
    val shape = RoundedCornerShape(22.dp)
    
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 16.dp else 8.dp,
        label = "Elevation"
    )
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.02f else 1.0f,
        label = "Scale"
    )
    
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = LocalIndication.current,
            onClick = {
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                onClick()
            }
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .responsiveCardBounds(responsiveMetrics)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = elevation,
                shape = shape,
                clip = false,
                ambientColor = Color(0x33FF2E44),
                spotColor = Color(0x55E50914)
            )
            .border(
                BorderStroke(
                    width = 1.2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            borderColor,
                            Color(0x11FFFFFF),
                            borderColor.copy(alpha = 0.1f)
                        )
                    )
                ),
                shape = shape
            )
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = 0.05f)
                    )
                )
            )
            .then(clickableModifier)
            .padding(if (responsiveMetrics.isSmallPhone) 12.dp else 16.dp)
    ) {
        content()
    }
}

/**
 * MASTER PHASE 1 — DYNAMIC TOOL COLOUR THEMES
 */
data class MentorToolTheme(
    val primaryColor: Color,
    val secondaryColor: Color,
    val gradientBrush: Brush,
    val glassBorderColor: Color,
    val glowColor: Color,
    val badgeLabel: String
) {
    companion object {
        val BrandCollab = MentorToolTheme(
            primaryColor = Color(0xFFFACC15),
            secondaryColor = Color(0xFFEAB308),
            gradientBrush = Brush.horizontalGradient(listOf(Color(0xFFFACC15), Color(0xFFEAB308))),
            glassBorderColor = Color(0x66FACC15),
            glowColor = Color(0x33FACC15),
            badgeLabel = "Brand Collab AI"
        )
        val MeeshoCreator = MentorToolTheme(
            primaryColor = Color(0xFFF43F5E),
            secondaryColor = Color(0xFFE91E63),
            gradientBrush = Brush.horizontalGradient(listOf(Color(0xFFF43F5E), Color(0xFFE91E63))),
            glassBorderColor = Color(0x66F43F5E),
            glowColor = Color(0x33F43F5E),
            badgeLabel = "Meesho Creator AI"
        )
        val InstagramCreator = MentorToolTheme(
            primaryColor = Color(0xFFE1306C),
            secondaryColor = Color(0xFF833AB4),
            gradientBrush = Brush.linearGradient(listOf(Color(0xFF833AB4), Color(0xFFE1306C), Color(0xFFF77737))),
            glassBorderColor = Color(0x66E1306C),
            glowColor = Color(0x33E1306C),
            badgeLabel = "Instagram Creator AI"
        )
        val YouTubeCreator = MentorToolTheme(
            primaryColor = Color(0xFFEF4444),
            secondaryColor = Color(0xFFDC2626),
            gradientBrush = Brush.horizontalGradient(listOf(Color(0xFFEF4444), Color(0xFFB91C1C))),
            glassBorderColor = Color(0x66EF4444),
            glowColor = Color(0x33EF4444),
            badgeLabel = "YouTube Creator AI"
        )
        val AiVideoImage = MentorToolTheme(
            primaryColor = Color(0xFF06B6D4),
            secondaryColor = Color(0xFFA855F7),
            gradientBrush = Brush.horizontalGradient(listOf(Color(0xFF06B6D4), Color(0xFFA855F7))),
            glassBorderColor = Color(0x6606B6D4),
            glowColor = Color(0x3306B6D4),
            badgeLabel = "AI Video & Image AI"
        )
        val CapCutMaster = MentorToolTheme(
            primaryColor = Color(0xFF38BDF8),
            secondaryColor = Color(0xFF0284C7),
            gradientBrush = Brush.horizontalGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7))),
            glassBorderColor = Color(0x6638BDF8),
            glowColor = Color(0x3338BDF8),
            badgeLabel = "CapCut Video Mentor"
        )
        val VnEditor = MentorToolTheme(
            primaryColor = Color(0xFF3B82F6),
            secondaryColor = Color(0xFF1D4ED8),
            gradientBrush = Brush.horizontalGradient(listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8))),
            glassBorderColor = Color(0x663B82F6),
            glowColor = Color(0x333B82F6),
            badgeLabel = "VN Video Editor AI"
        )
        val InstaAutoDm = MentorToolTheme(
            primaryColor = Color(0xFF10B981),
            secondaryColor = Color(0xFF059669),
            gradientBrush = Brush.horizontalGradient(listOf(Color(0xFF10B981), Color(0xFF059669))),
            glassBorderColor = Color(0x6610B981),
            glowColor = Color(0x3310B981),
            badgeLabel = "Insta Auto DM AI"
        )
    }
}

/**
 * MASTER PHASE 1 — PREMIUM IPHONE STYLE BUTTON
 * Features frosted glass background, dynamic tool glow, animated press scale,
 * smooth border gradient, and haptic feedback.
 */
@Composable
fun PremiumIPhoneButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    theme: MentorToolTheme = MentorToolTheme.InstagramCreator,
    enabled: Boolean = true,
    isSecondary: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1.0f,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessLow
        ),
        label = "PressScale"
    )

    val buttonShape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (isPressed) 4.dp else 10.dp,
                shape = buttonShape,
                ambientColor = theme.glowColor,
                spotColor = theme.primaryColor
            )
            .clip(buttonShape)
            .background(
                if (!enabled) {
                    Brush.verticalGradient(listOf(Color(0xFF333333), Color(0xFF222222)))
                } else if (isSecondary) {
                    Brush.verticalGradient(listOf(Color(0x22FFFFFF), Color(0x0AFFFFFF)))
                } else {
                    theme.gradientBrush
                }
            )
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    brush = if (enabled && isSecondary) {
                        theme.gradientBrush
                    } else if (enabled) {
                        Brush.linearGradient(listOf(Color.White.copy(alpha = 0.5f), theme.primaryColor.copy(alpha = 0.3f)))
                    } else {
                        SolidColor(Color(0x33FFFFFF))
                    }
                ),
                shape = buttonShape
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                }
            )
            .padding(horizontal = 18.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSecondary || !enabled) Color.White else Color.Black,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(18.dp)
                )
            }
            Text(
                text = text,
                color = if (!enabled) Color(0xFF888888) else if (isSecondary) Color.White else Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

/**
 * MASTER PHASE 1 — AUTO-HIDE COMPACT CHIP
 * Automatically collapses previous completed cards into a sleek compact chip.
 * Users can tap to expand or collapse cleanly.
 */
@Composable
fun CompactHelperChip(
    title: String,
    subtitle: String? = null,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier,
    theme: MentorToolTheme = MentorToolTheme.InstagramCreator,
    content: @Composable (() -> Unit)? = null
) {
    val chipShape = RoundedCornerShape(14.dp)
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(chipShape)
            .background(Color(0x1AFFFFFF))
            .border(BorderStroke(1.dp, if (isExpanded) theme.glassBorderColor else Color(0x1CFFFFFF)), chipShape)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onToggleExpand()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(theme.glowColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand",
                        tint = theme.primaryColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Column {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subtitle != null && !isExpanded) {
                        Text(
                            text = subtitle,
                            color = Color(0xFFAAAAAA),
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Text(
                text = if (isExpanded) "Tap to minimize" else "Tap to view",
                color = theme.primaryColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }

        if (isExpanded && content != null) {
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

/**
 * MASTER PHASE 2 — STEP CELEBRATION CARD
 * Shows a glowing success card whenever a learning step is completed.
 */
@Composable
fun StepCelebrationCard(
    stepTitle: String,
    xpEarned: Int = 50,
    theme: MentorToolTheme = MentorToolTheme.InstagramCreator,
    modifier: Modifier = Modifier,
    onNext: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(20.dp)
    val infiniteTransition = rememberInfiniteTransition(label = "GlowTransition")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(
                        theme.primaryColor.copy(alpha = 0.18f),
                        Color(0x1A111111)
                    )
                )
            )
            .border(
                BorderStroke(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            theme.primaryColor.copy(alpha = glowAlpha),
                            theme.secondaryColor.copy(alpha = 0.3f)
                        )
                    )
                ),
                shape = shape
            )
            .shadow(
                elevation = 12.dp,
                shape = shape,
                ambientColor = theme.glowColor,
                spotColor = theme.primaryColor
            )
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(theme.primaryColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Column {
                    Text(
                        text = "🎉 Step Completed!",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Creator Level Increased • +$xpEarned XP",
                        color = theme.primaryColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Great Job! You have mastered: \"$stepTitle\"",
                color = Color(0xFFDDDDDD),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            if (onNext != null) {
                Spacer(modifier = Modifier.height(12.dp))
                PremiumIPhoneButton(
                    text = "Continue to Next Step 🚀",
                    onClick = onNext,
                    theme = theme,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * MASTER PHASE 2 — SMART HINT CHIP
 * Compact expandable hint chip that offers optional hints ("Need a hint?").
 */
@Composable
fun SmartHintChip(
    hintText: String,
    theme: MentorToolTheme = MentorToolTheme.InstagramCreator,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    CompactHelperChip(
        title = "💡 Need a Hint?",
        subtitle = "Tap to view a quick shortcut / tip",
        isExpanded = isExpanded,
        onToggleExpand = { isExpanded = !isExpanded },
        theme = theme,
        modifier = modifier
    ) {
        Text(
            text = hintText,
            color = Color(0xFFFFE082),
            fontSize = 13.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

/**
 * MASTER PHASE 2 — PREMIUM COURSE COMPLETION CARD
 * Displayed when the full mentor course finishes.
 */
@Composable
fun CourseCompletionCard(
    courseTitle: String,
    skillsLearned: List<String>,
    onContinue: () -> Unit,
    onResetCourse: () -> Unit,
    theme: MentorToolTheme = MentorToolTheme.InstagramCreator,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF1E1E2C),
                        Color(0xFF0F0F1A)
                    )
                )
            )
            .border(
                BorderStroke(2.dp, theme.gradientBrush),
                shape = shape
            )
            .shadow(16.dp, shape, ambientColor = theme.glowColor, spotColor = theme.primaryColor)
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(theme.gradientBrush),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Trophy",
                    tint = Color.Black,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "🏆 COURSE COMPLETED!",
                color = theme.primaryColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )

            Text(
                text = courseTitle,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x33FFFFFF))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "100% Mastered • Creator Certified",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x1AFFFFFF))
                    .border(BorderStroke(1.dp, Color(0x1CFFFFFF)), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "📈 Skills Mastered:",
                    color = theme.primaryColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                skillsLearned.forEach { skill ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = theme.primaryColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = skill,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            PremiumIPhoneButton(
                text = "🎯 Ready for Next Course",
                onClick = onContinue,
                theme = theme,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            PremiumIPhoneButton(
                text = "🔄 Revise / Practice Again",
                onClick = onResetCourse,
                theme = theme,
                isSecondary = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * MASTER PHASE 2 — PREMIUM CHAT BUBBLE
 * AI Mentor glass card with soft glow & user contrasting pill bubble.
 */
@Composable
fun MentorChatBubble(
    message: String,
    isUser: Boolean,
    theme: MentorToolTheme = MentorToolTheme.InstagramCreator,
    followUpText: String? = null,
    modifier: Modifier = Modifier
) {
    if (isUser) {
        Box(
            modifier = modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = 18.dp,
                            bottomEnd = 4.dp
                        )
                    )
                    .background(theme.gradientBrush)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message,
                    color = Color.Black,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    } else {
        val shape = RoundedCornerShape(
            topStart = 4.dp,
            topEnd = 18.dp,
            bottomStart = 18.dp,
            bottomEnd = 18.dp
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape)
                .background(Color(0x1F222233))
                .border(BorderStroke(1.dp, theme.glassBorderColor), shape)
                .shadow(8.dp, shape, ambientColor = theme.glowColor, spotColor = theme.primaryColor)
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(theme.gradientBrush),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AI",
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = theme.badgeLabel,
                        color = theme.primaryColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message,
                    color = Color.White,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal
                )

                if (!followUpText.isNull_or_blank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(theme.glowColor)
                            .border(BorderStroke(1.dp, theme.glassBorderColor), RoundedCornerShape(10.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = followUpText ?: "",
                            color = theme.primaryColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

private fun String?.isNull_or_blank(): Boolean = this == null || this.trim().isEmpty()


