package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*

/**
 * Universal Tool Popup Frame
 * Standardized across the entire application for 100% UI consistency.
 */
@Composable
fun UniversalToolPopupDialog(
    onDismiss: () -> Unit,
    icon: ImageVector = Icons.Default.AutoAwesome,
    title: String,
    subtitle: String? = null,
    langTag: String? = null,
    onLangClick: (() -> Unit)? = null,
    currentStepText: String? = null,
    ctaText: String? = null,
    onCtaClick: (() -> Unit)? = null,
    ctaEnabled: Boolean = true,
    secondaryCtaText: String? = null,
    onSecondaryCtaClick: (() -> Unit)? = null,
    scrollableContent: @Composable ColumnScope.() -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val responsiveMetrics = LocalResponsiveMetrics.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AmoledBlack)
                .statusBarsPadding()
                .responsiveImeAndNavPadding(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = Color(0xFF0F1A14), // Glass Theme Dark Palette
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .responsiveDialogBounds(responsiveMetrics)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 1. GLASS HEADER
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0D1611))
                            .border(
                                BorderStroke(0.8.dp, Color(0x22FFFFFF)),
                                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                            )
                            .padding(
                                horizontal = responsiveMetrics.horizontalPadding,
                                vertical = 12.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(responsiveMetrics.scaledDp(42f))
                                    .clip(CircleShape)
                                    .background(EmeraldPrimary.copy(alpha = 0.2f))
                                    .border(BorderStroke(1.2.dp, EmeraldGlow), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = EmeraldGlow,
                                    modifier = Modifier.size(responsiveMetrics.scaledDp(22f))
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                AutoResizedText(
                                    text = title,
                                    fontSize = responsiveMetrics.scaledSp(15f),
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                subtitle?.let { sub ->
                                    AutoResizedText(
                                        text = sub,
                                        fontSize = responsiveMetrics.scaledSp(11f),
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldGlow,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            langTag?.let { tag ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0x1A10B981))
                                        .border(
                                            BorderStroke(0.8.dp, EmeraldPrimary.copy(alpha = 0.5f)),
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable { onLangClick?.invoke() }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = tag,
                                        fontSize = responsiveMetrics.scaledSp(10f),
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldGlow
                                    )
                                }
                            }

                            currentStepText?.let { step ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0x1A8B5CF6))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = step,
                                        fontSize = responsiveMetrics.scaledSp(10f),
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFA78BFA)
                                    )
                                }
                            }

                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = TextWhite.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    // 2. SCROLLABLE CONTENT BODY
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(
                                horizontal = responsiveMetrics.horizontalPadding,
                                vertical = 14.dp
                            ),
                        content = scrollableContent
                    )

                    // 3. FIXED BOTTOM CTA AREA
                    if (ctaText != null || secondaryCtaText != null) {
                        Surface(
                            color = Color(0xFF0B120E),
                            border = BorderStroke(0.8.dp, Color(0x22FFFFFF)),
                            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = responsiveMetrics.horizontalPadding,
                                        vertical = 14.dp
                                    ),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                ctaText?.let { text ->
                                    UniversalCtaButton(
                                        text = text,
                                        enabled = ctaEnabled,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            onCtaClick?.invoke()
                                        }
                                    )
                                }

                                secondaryCtaText?.let { text ->
                                    OutlinedButton(
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            onSecondaryCtaClick?.invoke()
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .responsiveButtonBounds(responsiveMetrics),
                                        shape = RoundedCornerShape(16.dp),
                                        border = BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.5f)),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = EmeraldGlow
                                        )
                                    ) {
                                        AutoResizedText(
                                            text = text,
                                            fontSize = responsiveMetrics.scaledSp(13.5f),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UniversalCtaButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    val responsiveMetrics = LocalResponsiveMetrics.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "btnScale"
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .responsiveButtonBounds(responsiveMetrics)
            .scale(scale)
            .shadow(12.dp, RoundedCornerShape(16.dp), spotColor = EmeraldGlow),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Unspecified,
            disabledContainerColor = Color(0xFF223329)
        ),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (enabled) {
                        Brush.horizontalGradient(
                            listOf(
                                EmeraldPrimary,
                                EmeraldGlow,
                                Color(0xFF059669)
                            )
                        )
                    } else {
                        Brush.horizontalGradient(
                            listOf(Color(0xFF1E2E25), Color(0xFF19261E))
                        )
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = if (enabled) AmoledBlack else TextWhite.copy(alpha = 0.4f),
                        modifier = Modifier.size(responsiveMetrics.scaledDp(20f))
                    )
                }
                AutoResizedText(
                    text = text,
                    fontSize = responsiveMetrics.scaledSp(15f),
                    fontWeight = FontWeight.Black,
                    color = if (enabled) AmoledBlack else TextWhite.copy(alpha = 0.4f),
                    maxLines = 1
                )
            }
        }
    }
}
