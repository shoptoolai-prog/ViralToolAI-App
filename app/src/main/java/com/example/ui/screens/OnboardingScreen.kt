package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.launch

data class OnboardingPageData(
    val pageNumber: Int,
    val title: String,
    val subtitle: String,
    val description: String,
    val icon: ImageVector,
    val badgeText: String
)

object OnboardingPrefs {
    private const val PREF_NAME = "viraltoolai_prefs"
    private const val KEY_COMPLETED = "onboarding_completed"

    fun isOnboardingCompleted(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_COMPLETED, false)
    }

    fun setOnboardingCompleted(context: Context, completed: Boolean = true) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_COMPLETED, completed).apply()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onOnboardingFinished: () -> Unit) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val pages = remember {
        listOf(
            OnboardingPageData(
                pageNumber = 1,
                title = "Discover Products",
                subtitle = "Shopping & Deal Intelligence",
                description = "Paste links from Amazon, Flipkart, Myntra, and AJIO. AI analyzes price history, detects verified discounts, and finds the best store.",
                icon = Icons.Default.ShoppingBag,
                badgeText = "SMART SHOPPING"
            ),
            OnboardingPageData(
                pageNumber = 2,
                title = "Grow as Creator",
                subtitle = "Viral Creator Studio",
                description = "Turn any product into viral Instagram Reels & YouTube Shorts. Generate AI hooks, Hinglish captions, and high-conversion hashtag sets.",
                icon = Icons.Default.Movie,
                badgeText = "CREATOR STUDIO"
            ),
            OnboardingPageData(
                pageNumber = 3,
                title = "AI Powered Everything",
                subtitle = "Deep Gemini Intelligence",
                description = "Get instant Deal Scores, profile growth audits, smart campaign planners, and real-time shopping guidance in one flagship experience.",
                icon = Icons.Default.AutoAwesome,
                badgeText = "FLAGSHIP CORE"
            )
        )
    }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pages.size })

    val finishOnboarding = {
        OnboardingPrefs.setOnboardingCompleted(context, true)
        onOnboardingFinished()
    }

    // Infinite Background Aurora Movement
    val infiniteTransition = rememberInfiniteTransition(label = "bgAurora")
    val auroraOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "auroraOffset"
    )

    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF060B08),
                        Color(0xFF0B1711),
                        Color(0xFF09121B),
                        Color(0xFF05080A)
                    ),
                    start = Offset(auroraOffset % 800f, 0f),
                    end = Offset((auroraOffset % 800f) + 600f, 1200f)
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Soft Ambient Floating Aurora Orbs
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        EmeraldPrimary.copy(alpha = 0.28f * pulseGlow),
                        ElectricPurple.copy(alpha = 0.12f),
                        Color.Transparent
                    )
                ),
                radius = size.width * 0.65f,
                center = Offset(size.width * 0.5f, size.height * 0.25f)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF00E5FF).copy(alpha = 0.18f * pulseGlow),
                        Color.Transparent
                    )
                ),
                radius = size.width * 0.5f,
                center = Offset(size.width * 0.8f, size.height * 0.65f)
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar with Brand Capsule & Glass Skip Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Glass Mini Brand Capsule
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x1810B981))
                        .border(
                            BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.5f)),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(EmeraldPrimary, EmeraldGlow)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = AmoledBlack,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = "ViralToolAI",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Glass Skip Button Capsule
                if (pagerState.currentPage < pages.size - 1) {
                    val skipInteractionSource = remember { MutableInteractionSource() }
                    val isSkipPressed by skipInteractionSource.collectIsPressedAsState()
                    val skipScale by animateFloatAsState(
                        targetValue = if (isSkipPressed) 0.92f else 1f,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium),
                        label = "skipScale"
                    )

                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = skipScale
                                scaleY = skipScale
                            }
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x1EFFFFFF))
                            .border(
                                BorderStroke(1.dp, Color(0x33FFFFFF)),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable(
                                interactionSource = skipInteractionSource,
                                indication = androidx.compose.foundation.LocalIndication.current
                            ) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                finishOnboarding()
                            }
                            .padding(horizontal = 16.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = "Skip",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite.copy(alpha = 0.85f),
                            letterSpacing = 0.3.sp
                        )
                    }
                }
            }

            // Pager Body with Smooth Page Transformations
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { pageIdx ->
                val pageData = pages[pageIdx]

                // Page offset graphics transformation
                val pageOffset = (
                    (pagerState.currentPage - pageIdx) + pagerState.currentPageOffsetFraction
                ).coerceIn(-1f, 1f)

                val pageAlpha = (1f - kotlin.math.abs(pageOffset) * 0.5f).coerceIn(0f, 1f)
                val pageScale = (1f - kotlin.math.abs(pageOffset) * 0.15f).coerceIn(0.85f, 1f)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = pageAlpha
                            scaleX = pageScale
                            scaleY = pageScale
                        }
                ) {
                    OnboardingPageContent(pageData = pageData)
                }
            }

            // Bottom Navigation Controls
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animated Progress Capsule Indicators
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 22.dp)
                ) {
                    repeat(pages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        val width by animateDpAsState(
                            targetValue = if (isSelected) 36.dp else 9.dp,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium)
                        )
                        val color by animateColorAsState(
                            targetValue = if (isSelected) EmeraldGlow else Color(0x33FFFFFF),
                            animationSpec = tween(300)
                        )

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(9.dp)
                                .width(width)
                                .shadow(
                                    elevation = if (isSelected) 8.dp else 0.dp,
                                    shape = CircleShape,
                                    spotColor = EmeraldGlow
                                )
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) {
                                        Brush.horizontalGradient(
                                            listOf(EmeraldPrimary, EmeraldGlow)
                                        )
                                    } else {
                                        androidx.compose.ui.graphics.SolidColor(color)
                                    }
                                )
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        if (isSelected) Color.White.copy(alpha = 0.6f) else Color.Transparent
                                    ),
                                    CircleShape
                                )
                        )
                    }
                }

                // Liquid Primary Action Pill Button
                val isLastPage = pagerState.currentPage == pages.size - 1
                val btnInteractionSource = remember { MutableInteractionSource() }
                val isBtnPressed by btnInteractionSource.collectIsPressedAsState()
                val buttonScale by animateFloatAsState(
                    targetValue = if (isBtnPressed) 0.94f else 1f,
                    animationSpec = spring(stiffness = Spring.StiffnessMedium),
                    label = "btnScale"
                )

                val btnShimmerPos by infiniteTransition.animateFloat(
                    initialValue = -300f,
                    targetValue = 800f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(3000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "btnShimmerPos"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .graphicsLayer {
                            scaleX = buttonScale
                            scaleY = buttonScale
                        }
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(29.dp),
                            spotColor = EmeraldGlow,
                            ambientColor = Color.Black
                        )
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
                                        Color.White.copy(alpha = 0.8f),
                                        Color.White.copy(alpha = 0.2f),
                                        Color.White.copy(alpha = 0.9f)
                                    ),
                                    start = Offset(btnShimmerPos, 0f),
                                    end = Offset(btnShimmerPos + 250f, 100f)
                                )
                            ),
                            RoundedCornerShape(29.dp)
                        )
                        .clickable(
                            interactionSource = btnInteractionSource,
                            indication = androidx.compose.foundation.LocalIndication.current
                        ) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (isLastPage) {
                                finishOnboarding()
                            } else {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        }
                        .testTag(if (isLastPage) "get_started_button" else "continue_onboarding_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isLastPage) "Get Started" else "Continue",
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.Black,
                            color = AmoledBlack,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = if (isLastPage) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = AmoledBlack,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Created by Asit • ViralToolAI v1.0",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite.copy(alpha = 0.45f),
                    letterSpacing = 1.2.sp
                )
            }
        }
    }
}

@Composable
fun OnboardingPageContent(pageData: OnboardingPageData) {
    val infiniteTransition = rememberInfiniteTransition(label = "heroAnimation")

    // Sine-wave floating vertical bounce for hero icon
    val floatingOffsetY by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatingOffsetY"
    )

    val cardShimmerPos by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cardShimmerPos"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Floating Hero Glass Card with Aurora Border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(32.dp),
                    spotColor = EmeraldPrimary.copy(alpha = 0.4f),
                    ambientColor = Color.Black
                )
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(0xFF111A15),
                            Color(0xFF0D1412),
                            Color(0xFF0F1A1B)
                        )
                    )
                )
                .border(
                    BorderStroke(
                        1.5.dp,
                        Brush.linearGradient(
                            colors = listOf(
                                EmeraldPrimary.copy(alpha = 0.8f),
                                ElectricPurple.copy(alpha = 0.5f),
                                Color(0xFF00E5FF).copy(alpha = 0.7f)
                            ),
                            start = Offset(cardShimmerPos, 0f),
                            end = Offset(cardShimmerPos + 300f, 200f)
                        )
                    ),
                    RoundedCornerShape(32.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Radial Glow Behind Hero Icon
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(EmeraldPrimary.copy(alpha = 0.3f), Color.Transparent)
                        ),
                        CircleShape
                    )
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Glass Badge Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(EmeraldGlow.copy(alpha = 0.18f))
                        .border(
                            BorderStroke(1.2.dp, EmeraldGlow.copy(alpha = 0.6f)),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = pageData.badgeText,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        color = EmeraldGlow,
                        letterSpacing = 1.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Floating Icon Container with dual-layer glass circle
                Box(
                    modifier = Modifier
                        .graphicsLayer { translationY = floatingOffsetY }
                        .size(92.dp)
                        .shadow(16.dp, CircleShape, spotColor = EmeraldGlow)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(EmeraldPrimary, Color(0xFF059669), Color(0xFF00E5FF))
                            )
                        )
                        .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.8f)), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = pageData.icon,
                        contentDescription = pageData.title,
                        tint = AmoledBlack,
                        modifier = Modifier.size(46.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Title with High Contrast & Letter Tracking
        Text(
            text = pageData.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite,
            textAlign = TextAlign.Center,
            letterSpacing = 0.3.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Subtitle / Category
        Text(
            text = pageData.subtitle.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = EmeraldGlow,
            textAlign = TextAlign.Center,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Description
        Text(
            text = pageData.description,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = TextWhite.copy(alpha = 0.75f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
