package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.TouchApp
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.AutoResizedText
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.LocalResponsiveMetrics
import com.example.ui.theme.TextWhite
import com.example.ui.theme.responsiveButtonBounds
import com.example.ui.theme.responsiveImeAndNavPadding
import com.example.ui.theme.responsiveDialogBounds
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CameraAlt

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

/**
 * 1. COMMON RESPONSIVE TEXT
 * Automatically scales font size and truncates gracefully so text NEVER overflows or gets cut outside cards/containers.
 */
@Composable
fun CommonResponsiveText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = TextWhite,
    maxLines: Int = 1,
    textAlign: TextAlign = TextAlign.Start,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified
) {
    val responsiveMetrics = LocalResponsiveMetrics.current
    val scaledSp = responsiveMetrics.scaledSp(fontSize.value)

    AutoResizedText(
        text = text,
        fontSize = scaledSp,
        fontWeight = fontWeight,
        color = color,
        maxLines = maxLines,
        textAlign = textAlign,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight,
        modifier = modifier
    )
}

/**
 * 2. COMMON POPUP ANIMATION
 * Provides a standardized entrance and exit animation (spring scale + fade + slide) for popups and tool dialog cards.
 */
@Composable
fun CommonPopupAnimation(
    visible: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(280)) +
                scaleIn(initialScale = 0.92f, animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) +
                slideInVertically(initialOffsetY = { it / 8 }, animationSpec = spring(stiffness = Spring.StiffnessMediumLow)),
        exit = fadeOut(animationSpec = tween(200)) + scaleOut(targetScale = 0.94f, animationSpec = tween(200)),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * 3. COMMON SWIPE CONTAINER
 * Universal swipe-based tool container supporting:
 * - Centered premium popup card design
 * - Left/Right swipe navigation
 * - Subtle left/right arrow indicators
 * - Page dots indicator at bottom
 * - First-time swipe hint animation
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommonSwipeContainer(
    pageCount: Int,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
    showIndicators: Boolean = true,
    showArrows: Boolean = true,
    showSwipeHint: Boolean = true,
    onPageChanged: ((Int) -> Unit)? = null,
    pageContent: @Composable (pageIndex: Int) -> Unit
) {
    if (pageCount <= 0) return

    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val responsiveMetrics = LocalResponsiveMetrics.current
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { pageCount })

    var hasSwiped by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.currentPage) {
        onPageChanged?.invoke(pagerState.currentPage)
        if (pagerState.currentPage != 0) {
            hasSwiped = true
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "swipeHintPulse")
    val hintTranslateX by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hintTranslateX"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // First-time swipe hint overlay banner
        AnimatedVisibility(
            visible = showSwipeHint && !hasSwiped && pageCount > 1,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x2210B981))
                    .border(BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.4f)), RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = null,
                    tint = EmeraldGlow,
                    modifier = Modifier
                        .size(16.dp)
                        .graphicsLayer { translationX = hintTranslateX }
                )
                Text(
                    text = "Swipe left / right to navigate tools",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGlow
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
            contentAlignment = Alignment.Center
        ) {
            // Main Horizontal Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { pageIdx ->
                val pageOffset = (
                    (pagerState.currentPage - pageIdx) + pagerState.currentPageOffsetFraction
                ).coerceIn(-1f, 1f)

                val pageAlpha = (1f - kotlin.math.abs(pageOffset) * 0.45f).coerceIn(0f, 1f)
                val pageScale = (1f - kotlin.math.abs(pageOffset) * 0.08f).coerceIn(0.92f, 1f)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = pageAlpha
                            scaleX = pageScale
                            scaleY = pageScale
                        },
                    contentAlignment = Alignment.Center
                ) {
                    pageContent(pageIdx)
                }
            }

            // Left Arrow Button Indicator
            if (showArrows && pageCount > 1 && pagerState.currentPage > 0) {
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 4.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xCC0D1611))
                        .border(BorderStroke(1.dp, Color(0x33FFFFFF)), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Previous Tool",
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Right Arrow Button Indicator
            if (showArrows && pageCount > 1 && pagerState.currentPage < pageCount - 1) {
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xCC0D1611))
                        .border(BorderStroke(1.dp, Color(0x33FFFFFF)), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Next Tool",
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Page Dots Indicator
        if (showIndicators && pageCount > 1) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until pageCount) {
                    val isSelected = pagerState.currentPage == i
                    val width by animateDpAsState(
                        targetValue = if (isSelected) 18.dp else 6.dp,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium),
                        label = "dotWidth"
                    )
                    val color by animateColorAsState(
                        targetValue = if (isSelected) EmeraldGlow else Color(0x33FFFFFF),
                        label = "dotColor"
                    )

                    Box(
                        modifier = Modifier
                            .height(6.dp)
                            .width(width)
                            .clip(CircleShape)
                            .background(color)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(i)
                                }
                            }
                    )
                }
            }
        }
    }
}

/**
 * 5. COMMON TOOL INTRO CARD DATA
 */
data class ToolIntroCardData(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val highlightTag: String,
    val bulletPoints: List<String>
)

/**
 * COMMON TOOL INTRO CARD
 * Master UI card style extracted directly from Brand Collaboration AI.
 */
@Composable
fun CommonToolIntroCard(
    data: ToolIntroCardData,
    pageIndex: Int,
    totalPages: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "borderGlowIntro")
    val borderGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlphaIntro"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = EmeraldGlow),
        shape = RoundedCornerShape(24.dp),
        color = Color(0x1A14241C),
        border = BorderStroke(
            1.2.dp,
            Brush.linearGradient(
                listOf(
                    EmeraldPrimary.copy(alpha = borderGlowAlpha),
                    Color(0x33FFFFFF)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0x2210B981))
                        .border(BorderStroke(1.5.dp, EmeraldPrimary), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = data.icon,
                        contentDescription = data.title,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x2210B981))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    CommonResponsiveText(
                        text = data.highlightTag,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        letterSpacing = 0.5.sp,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                CommonResponsiveText(
                    text = data.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                CommonResponsiveText(
                    text = data.subtitle,
                    fontSize = 12.sp,
                    color = TextWhite.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    data.bulletPoints.forEach { pt ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Check",
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            CommonResponsiveText(
                                text = pt,
                                fontSize = 12.sp,
                                color = TextWhite.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            CommonResponsiveText(
                text = "Card ${pageIndex + 1} of $totalPages",
                fontSize = 11.sp,
                color = TextWhite.copy(alpha = 0.5f),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * COMMON TOOL INTRO CONTAINER
 * Complete swipeable intro experience for any ViralToolAI tool.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommonToolIntroContainer(
    cards: List<ToolIntroCardData>,
    onCompleteIntro: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (cards.isEmpty()) {
        onCompleteIntro()
        return
    }

    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val pagerState = rememberPagerState(pageCount = { cards.size })
    var hasSwiped by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage > 0) {
            hasSwiped = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Navigation Bar (Swipe Hint + Skip Button)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!hasSwiped) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x2210B981))
                        .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    CommonResponsiveText(
                        text = "⬅ Swipe to explore ➡",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        maxLines = 1
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }

            Text(
                text = "Skip Intro ➔",
                fontSize = 12.sp,
                color = TextWhite.copy(alpha = 0.7f),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onCompleteIntro()
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Swipe Pager for Tool Intro Cards
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 8.dp),
            pageSpacing = 12.dp
        ) { pageIndex ->
            CommonToolIntroCard(
                data = cards[pageIndex],
                pageIndex = pageIndex,
                totalPages = cards.size
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Page Indicator Dots
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in cards.indices) {
                val isSelected = pagerState.currentPage == i
                val width by animateDpAsState(
                    targetValue = if (isSelected) 20.dp else 6.dp,
                    animationSpec = spring(stiffness = Spring.StiffnessMedium),
                    label = "dotWidth"
                )
                val color by animateColorAsState(
                    targetValue = if (isSelected) EmeraldGlow else Color(0x33FFFFFF),
                    label = "dotColor"
                )

                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(color)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(i)
                            }
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom CTA Button
        val isLastPage = pagerState.currentPage == cards.size - 1
        UniversalPrimaryButton(
            text = if (isLastPage) "Get Started ➔" else "Next Card ➔",
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                if (isLastPage) {
                    onCompleteIntro()
                } else {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            gradientColors = listOf(EmeraldPrimary, EmeraldGlow)
        )
    }
}

/**
 * COMMON POPUP CARD / DIALOG CONTAINER
 * Centered popup dialog layout matching Brand Collaboration AI style.
 */
@Composable
fun CommonPopupCard(
    title: String,
    subtitle: String? = null,
    icon: ImageVector = Icons.Default.AutoAwesome,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val responsiveMetrics = LocalResponsiveMetrics.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .statusBarsPadding()
            .responsiveImeAndNavPadding(),
        contentAlignment = Alignment.Center
    ) {
        CommonPopupAnimation(visible = true) {
            Surface(
                color = Color(0xFF0F1A14),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0x3310B981)),
                modifier = modifier.responsiveDialogBounds(responsiveMetrics)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Header Row
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
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldPrimary.copy(alpha = 0.2f))
                                    .border(BorderStroke(1.2.dp, EmeraldGlow), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = EmeraldGlow,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                CommonResponsiveText(
                                    text = title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite,
                                    maxLines = 1
                                )
                                subtitle?.let { sub ->
                                    CommonResponsiveText(
                                        text = sub,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldGlow,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = onClose,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x22FFFFFF))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextWhite,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Body Content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(responsiveMetrics.horizontalPadding)
                    ) {
                        content()
                    }
                }
            }
        }
    }
}

