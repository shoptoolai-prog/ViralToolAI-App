package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.AutoResizedText
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.LocalResponsiveMetrics
import com.example.ui.theme.TextWhite
import com.example.ui.theme.responsiveButtonBounds

/**
 * Universal Premium Glass Header for Tools & Dialogs.
 * Guarantees proper status bar padding, high contrast title, tool icon, subtitle and close/back action.
 */
@Composable
fun UniversalPremiumHeader(
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    iconTint: Color = EmeraldPrimary,
    onCloseClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xF90F1A14),
        border = BorderStroke(1.dp, Color(0x3300FF87)),
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp), spotColor = EmeraldPrimary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                if (onBackClick != null) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0x22FFFFFF))
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else if (icon != null) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.radialGradient(listOf(iconTint.copy(alpha = 0.3f), Color(0x11FFFFFF))))
                            .border(BorderStroke(1.dp, iconTint.copy(alpha = 0.5f)), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Column {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Text(
                            text = subtitle,
                            fontSize = 11.5.sp,
                            color = TextWhite.copy(alpha = 0.6f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                trailingContent?.invoke(this)

                if (onCloseClick != null) {
                    IconButton(
                        onClick = onCloseClick,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x22FFFFFF))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextWhite.copy(alpha = 0.8f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Universal Glass Input Bar.
 * Universal pill-shaped chat input bar that respects IME and navigation bar padding,
 * stays attached above the keyboard, and supports expanding text up to 5 lines.
 */
@Composable
fun UniversalBottomInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    placeholder: String = "Type a message...",
    onMicClick: (() -> Unit)? = null,
    accentColor: Color = EmeraldPrimary,
    glowColor: Color = EmeraldGlow,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    Surface(
        color = Color(0xF9121E17),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)),
        shape = RoundedCornerShape(28.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(28.dp), spotColor = glowColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onMicClick != null) {
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onMicClick()
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0x1AFFFFFF))
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Input",
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
            }

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = placeholder,
                        fontSize = 13.sp,
                        color = TextWhite.copy(alpha = 0.45f)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 44.dp),
                shape = RoundedCornerShape(22.dp),
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = Color(0x22FFFFFF),
                    focusedContainerColor = Color(0xFF18261E),
                    unfocusedContainerColor = Color(0xFF142019),
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    cursorColor = accentColor
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            val isSendEnabled = value.isNotBlank()
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSendEnabled) {
                            Brush.horizontalGradient(listOf(accentColor, glowColor))
                        } else {
                            Brush.horizontalGradient(listOf(Color(0x33FFFFFF), Color(0x22FFFFFF)))
                        }
                    )
                    .clickable(enabled = isSendEnabled) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSendClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = if (isSendEnabled) AmoledBlack else TextWhite.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Universal Primary Action Button with touch feedback, glow elevation,
 * guaranteed minimum 48dp height, and zero clipping.
 */
@Composable
fun UniversalPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    gradientColors: List<Color> = listOf(EmeraldPrimary, EmeraldGlow),
    textColor: Color = AmoledBlack
) {
    val haptic = LocalHapticFeedback.current
    val responsiveMetrics = LocalResponsiveMetrics.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1.0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "btnScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .responsiveButtonBounds(responsiveMetrics)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (enabled) 12.dp else 0.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = gradientColors.firstOrNull() ?: EmeraldPrimary
            )
            .clip(RoundedCornerShape(24.dp))
            .background(
                if (enabled) {
                    Brush.horizontalGradient(gradientColors)
                } else {
                    Brush.horizontalGradient(listOf(Color(0x33FFFFFF), Color(0x22FFFFFF)))
                }
            )
            .border(
                BorderStroke(
                    1.dp,
                    if (enabled) Color.White.copy(alpha = 0.5f) else Color.Transparent
                ),
                RoundedCornerShape(24.dp)
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = LocalIndication.current
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentSize()
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(responsiveMetrics.scaledDp(20f))
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            AutoResizedText(
                text = text,
                fontSize = responsiveMetrics.scaledSp(15f),
                fontWeight = FontWeight.Bold,
                color = if (enabled) textColor else TextWhite.copy(alpha = 0.4f),
                maxLines = 1
            )
        }
    }
}

/**
 * Universal Secondary Outlined/Glass Button.
 */
@Composable
fun UniversalSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    borderColor: Color = Color(0x33FFFFFF),
    textColor: Color = TextWhite
) {
    val haptic = LocalHapticFeedback.current
    val responsiveMetrics = LocalResponsiveMetrics.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1.0f,
        label = "secondaryBtnScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .responsiveButtonBounds(responsiveMetrics)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0x1AFFFFFF))
            .border(BorderStroke(1.2.dp, borderColor), RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 10.dp),
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
                    tint = textColor,
                    modifier = Modifier.size(responsiveMetrics.scaledDp(18f))
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            AutoResizedText(
                text = text,
                fontSize = responsiveMetrics.scaledSp(14.5f),
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                maxLines = 1
            )
        }
    }
}

/**
 * Universal Animated AI Thinking / Typing Dot Indicator.
 */
@Composable
fun UniversalLoadingAnimation(
    message: String = "AI is thinking...",
    accentColor: Color = EmeraldPrimary,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")
    val dot1Scale by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "dot1"
    )
    val dot2Scale by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "dot2"
    )
    val dot3Scale by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "dot3"
    )

    Surface(
        color = Color(0x2200FF87),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
        modifier = modifier.padding(vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.size(8.dp).graphicsLayer { scaleX = dot1Scale; scaleY = dot1Scale }.clip(CircleShape).background(accentColor))
                Box(modifier = Modifier.size(8.dp).graphicsLayer { scaleX = dot2Scale; scaleY = dot2Scale }.clip(CircleShape).background(accentColor))
                Box(modifier = Modifier.size(8.dp).graphicsLayer { scaleX = dot3Scale; scaleY = dot3Scale }.clip(CircleShape).background(accentColor))
            }
            Text(
                text = message,
                fontSize = 12.sp,
                color = TextWhite.copy(alpha = 0.9f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
