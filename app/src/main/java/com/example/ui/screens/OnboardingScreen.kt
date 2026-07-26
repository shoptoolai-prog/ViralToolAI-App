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
import androidx.compose.ui.platform.LocalInspectionMode
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
import kotlin.math.cos
import kotlin.math.sin

data class OnboardingPageData(
    val pageNumber: Int,
    val title: String,
    val titleHighlight: String,
    val subtitle: String,
    val description: String,
    val icon: ImageVector,
    val badgeText: String,
    val primaryAccent: Color,
    val secondaryAccent: Color
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
    val isPreviewMode = LocalInspectionMode.current

    val pages = remember {
        listOf(
            OnboardingPageData(
                pageNumber = 1,
                title = "Discover Deals with",
                titleHighlight = "AI Intelligence",
                subtitle = "SHOPPING & PRICE COMPARE",
                description = "Instantly analyze links from Amazon, Flipkart, Myntra & AJIO. AI detects verified price drops, fake discounts, and best merchant stores.",
                icon = Icons.Default.ShoppingBag,
                badgeText = "SHOPPING ENGINE",
                primaryAccent = EmeraldPrimary,
                secondaryAccent = Color(0xFF00E5FF)
            ),
            OnboardingPageData(
                pageNumber = 2,
                title = "Build & Monetize your",
                titleHighlight = "Creator Brand",
                subtitle = "VIRAL CREATOR STUDIO",
                description = "Turn products into viral Instagram Reels & Shorts. Generate AI Hinglish hooks, trending captions, and high-converting affiliate links.",
                icon = Icons.Default.Movie,
                badgeText = "CREATOR ACADEMY",
                primaryAccent = Color(0xFF8B5CF6),
                secondaryAccent = Color(0xFFEC4899)
            ),
            OnboardingPageData(
                pageNumber = 3,
                title = "Supercharged by",
                titleHighlight = "Gemini Core AI",
                subtitle = "FLAGSHIP AI WORKSPACE",
                description = "Get real-time deal scoring, creator profile audits, smart campaign planners, and instant affiliate link generation in one place.",
                icon = Icons.Default.AutoAwesome,
                badgeText = "FLAGSHIP AI V1.0",
                primaryAccent = EmeraldGlow,
                secondaryAccent = Color(0xFF3B82F6)
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
            animation = tween(14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "auroraOffset"
    )

    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    val currentPageData = pages[pagerState.currentPage]

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
    ) {
        // Soft Ambient Floating Aurora Orbs & Particles
        if (!isPreviewMode) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            currentPageData.primaryAccent.copy(alpha = 0.22f * pulseGlow),
                            currentPageData.secondaryAccent.copy(alpha = 0.10f),
                            Color.Transparent
                        )
                    ),
                    radius = size.width * 0.70f,
                    center = Offset(size.width * 0.5f, size.height * 0.28f)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF00E5FF).copy(alpha = 0.12f * pulseGlow),
                            Color.Transparent
                        )
                    ),
                    radius = size.width * 0.55f,
                    center = Offset(size.width * 0.82f, size.height * 0.72f)
                )

                // Tiny ambient twinkling particles
                val numParticles = 20
                for (i in 0 until numParticles) {
                    val px = (sin((i * 1.5f + auroraOffset * 0.005f)) * 0.45f + 0.5f) * size.width
                    val py = (cos((i * 2.1f + auroraOffset * 0.004f)) * 0.45f + 0.5f) * size.height
                    val radius = (1.8f + (i % 3) * 1.5f).dp.toPx()
                    drawCircle(
                        color = currentPageData.primaryAccent.copy(
                            alpha = (0.20f + 0.30f * sin(i + pulseGlow))
                        ),
                        radius = radius,
                        center = Offset(px, py)
                    )
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Premium Header Bar with Glass Brand Capsule & Glass Skip Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Glass Mini Brand Capsule with Pulsing Icon
                Box(
                    modifier = Modifier
                        .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = EmeraldGlow)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xBB0A1811))
                        .border(
                            BorderStroke(
                                1.2.dp,
                                Brush.horizontalGradient(
                                    listOf(
                                        EmeraldGlow.copy(alpha = 0.8f),
                                        Color(0xFF00E5FF).copy(alpha = 0.4f),
                                        EmeraldGlow.copy(alpha = 0.8f)
                                    )
                                )
                            ),
                            RoundedCornerShape(22.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
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
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        Text(
                            text = "ViralToolAI",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 0.6.sp
                        )
                    }
                }

                // Premium Glass Skip Capsule
                if (pagerState.currentPage < pages.size - 1) {
                    val skipInteractionSource = remember { MutableInteractionSource() }
                    val isSkipPressed by skipInteractionSource.collectIsPressedAsState()
                    val skipScale by animateFloatAsState(
                        targetValue = if (isSkipPressed) 0.90f else 1f,
                        animationSpec = spring(stiffness = Spring.StiffnessHigh),
                        label = "skipScale"
                    )

                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = skipScale
                                scaleY = skipScale
                            }
                            .shadow(10.dp, RoundedCornerShape(20.dp), spotColor = Color.White)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x22FFFFFF))
                            .border(
                                BorderStroke(1.2.dp, Color(0x44FFFFFF)),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable(
                                interactionSource = skipInteractionSource,
                                indication = androidx.compose.foundation.LocalIndication.current
                            ) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                finishOnboarding()
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("skip_onboarding_button")
                    ) {
                        Text(
                            text = "Skip",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite.copy(alpha = 0.9f),
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

                val pageAlpha = (1f - kotlin.math.abs(pageOffset) * 0.55f).coerceIn(0f, 1f)
                val pageScale = (1f - kotlin.math.abs(pageOffset) * 0.12f).coerceIn(0.88f, 1f)

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

            // Bottom Navigation Controls: Animated Progress Capsule & Continue Button
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
                            targetValue = if (isSelected) 38.dp else 10.dp,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium)
                        )
                        val color by animateColorAsState(
                            targetValue = if (isSelected) currentPageData.primaryAccent else Color(0x33FFFFFF),
                            animationSpec = tween(300)
                        )

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(10.dp)
                                .width(width)
                                .shadow(
                                    elevation = if (isSelected) 10.dp else 0.dp,
                                    shape = CircleShape,
                                    spotColor = currentPageData.primaryAccent
                                )
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) {
                                        Brush.horizontalGradient(
                                            listOf(currentPageData.primaryAccent, currentPageData.secondaryAccent)
                                        )
                                    } else {
                                        androidx.compose.ui.graphics.SolidColor(color)
                                    }
                                )
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        if (isSelected) Color.White.copy(alpha = 0.7f) else Color.Transparent
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
                        animation = tween(2800, easing = LinearEasing),
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
                            elevation = 20.dp,
                            shape = RoundedCornerShape(29.dp),
                            spotColor = currentPageData.primaryAccent,
                            ambientColor = Color.Black
                        )
                        .clip(RoundedCornerShape(29.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(currentPageData.primaryAccent, EmeraldGlow, currentPageData.secondaryAccent)
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
    val isPreviewMode = LocalInspectionMode.current
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
            animation = tween(3800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cardShimmerPos"
    )

    val iconPulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconPulse"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Floating 3D Hero Glass Card with Aurora Border & Depth Shadow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(32.dp),
                    spotColor = pageData.primaryAccent.copy(alpha = 0.5f),
                    ambientColor = Color.Black
                )
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xEE0B1812),
                            Color(0xF808110D),
                            Color(0xFF040A07)
                        )
                    )
                )
                .border(
                    BorderStroke(
                        1.5.dp,
                        Brush.linearGradient(
                            colors = listOf(
                                pageData.primaryAccent.copy(alpha = 0.85f),
                                pageData.secondaryAccent.copy(alpha = 0.6f),
                                Color.White.copy(alpha = 0.8f)
                            ),
                            start = Offset(cardShimmerPos, 0f),
                            end = Offset(cardShimmerPos + 320f, 220f)
                        )
                    ),
                    RoundedCornerShape(32.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Radial Glow Behind Hero Icon
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(pageData.primaryAccent.copy(alpha = 0.32f), Color.Transparent)
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
                        .background(pageData.primaryAccent.copy(alpha = 0.18f))
                        .border(
                            BorderStroke(1.2.dp, pageData.primaryAccent.copy(alpha = 0.65f)),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = pageData.primaryAccent,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = pageData.badgeText,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black,
                            color = pageData.primaryAccent,
                            letterSpacing = 1.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Floating Glass Icon Container
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = if (!isPreviewMode) floatingOffsetY else 0f
                            scaleX = if (!isPreviewMode) iconPulse else 1f
                            scaleY = if (!isPreviewMode) iconPulse else 1f
                        }
                        .size(92.dp)
                        .shadow(20.dp, CircleShape, spotColor = pageData.primaryAccent)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(pageData.primaryAccent, pageData.secondaryAccent)
                            )
                        )
                        .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.85f)), CircleShape),
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

        Spacer(modifier = Modifier.height(30.dp))

        // Large Premium Title
        Text(
            text = pageData.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center,
            letterSpacing = 0.2.sp
        )

        // Gradient Highlighted Title Keyword
        Text(
            text = pageData.titleHighlight,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = pageData.primaryAccent,
            textAlign = TextAlign.Center,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Subtitle / Category Pill
        Text(
            text = pageData.subtitle,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = EmeraldGlow,
            textAlign = TextAlign.Center,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // High Impact Description
        Text(
            text = pageData.description,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = TextWhite.copy(alpha = 0.80f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
