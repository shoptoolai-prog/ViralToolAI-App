package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
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
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

data class OnboardingCardData(
    val pageIndex: Int,
    val title: String,
    val subtitle: String,
    val highlights: List<String>,
    val icon: ImageVector,
    val badge: String,
    val primaryColor: Color,
    val secondaryColor: Color
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

    // 3 Fresh Onboarding Cards
    val cards = remember {
        listOf(
            OnboardingCardData(
                pageIndex = 0,
                title = "What You Can Create",
                subtitle = "Viral Reels, Shorts, Posts, Captions & Prompts",
                highlights = listOf(
                    "Viral Reels & Shorts Scripts",
                    "AI Prompt Extractor",
                    "Image & Video Analysis",
                    "High-Converting Posts"
                ),
                icon = Icons.Default.Movie,
                badge = "CREATOR SUITE",
                primaryColor = EmeraldPrimary,
                secondaryColor = Color(0xFF00E5FF)
            ),
            OnboardingCardData(
                pageIndex = 1,
                title = "Creator AI Tools",
                subtitle = "Everything a creator needs to speed up workflow",
                highlights = listOf(
                    "Smart AI Generator",
                    "Shopping Intelligence",
                    "Creator Academy Guides",
                    "Daily Creator Support"
                ),
                icon = Icons.Default.AutoAwesome,
                badge = "AI TOOLKIT",
                primaryColor = Color(0xFF8B5CF6),
                secondaryColor = Color(0xFFEC4899)
            ),
            OnboardingCardData(
                pageIndex = 2,
                title = "How ViralToolAI Helps You Grow",
                subtitle = "Scale reach & monetize across platforms",
                highlights = listOf(
                    "Brand Collaboration AI",
                    "Instagram Creator AI",
                    "YouTube Creator AI",
                    "Meesho Creator AI"
                ),
                icon = Icons.Default.RocketLaunch,
                badge = "VIRAL GROWTH",
                primaryColor = Color(0xFFA3E635),
                secondaryColor = EmeraldGlow
            )
        )
    }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { cards.size })

    val finishOnboarding = {
        OnboardingPrefs.setOnboardingCompleted(context, true)
        onOnboardingFinished()
    }

    // Startup Entrance Fade-in
    var isMounted by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isMounted = true
    }

    val startupScale by animateFloatAsState(
        targetValue = if (isMounted) 1f else 0.94f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "startupScale"
    )

    val startupAlpha by animateFloatAsState(
        targetValue = if (isMounted) 1f else 0f,
        animationSpec = tween(600, easing = LinearOutSlowInEasing),
        label = "startupAlpha"
    )

    // Infinite Ambient Background Aurora
    val infiniteTransition = rememberInfiniteTransition(label = "bgAurora")
    val auroraOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "auroraOffset"
    )

    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    val currentCard = cards[pagerState.currentPage]
    val isLastPage = pagerState.currentPage == cards.size - 1

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF030706),
                        Color(0xFF08120E),
                        Color(0xFF0A1017),
                        Color(0xFF020507)
                    ),
                    start = Offset(auroraOffset % 800f, 0f),
                    end = Offset((auroraOffset % 800f) + 600f, 1200f)
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .graphicsLayer {
                scaleX = startupScale
                scaleY = startupScale
                alpha = startupAlpha
            }
    ) {
        // Soft Ambient Floating Radial Orbs
        if (!isPreviewMode) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            currentCard.primaryColor.copy(alpha = 0.22f * pulseGlow),
                            currentCard.secondaryColor.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    ),
                    radius = size.width * 0.72f,
                    center = Offset(size.width * 0.5f, size.height * 0.35f)
                )

                // Twinkling background particles
                val numParticles = 14
                for (i in 0 until numParticles) {
                    val px = (sin((i * 1.5f + auroraOffset * 0.005f)) * 0.45f + 0.5f) * size.width
                    val py = (cos((i * 2.1f + auroraOffset * 0.004f)) * 0.45f + 0.5f) * size.height
                    val radius = (1.5f + (i % 3) * 1.2f).dp.toPx()
                    drawCircle(
                        color = currentCard.primaryColor.copy(
                            alpha = (0.15f + 0.25f * sin(i + pulseGlow))
                        ),
                        radius = radius,
                        center = Offset(px, py)
                    )
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar: Glass App Logo Pill + Skip Option
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Glass App Title Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xBB0A1812))
                        .border(
                            BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.5f)),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
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
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Text(
                            text = "ViralToolAI",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Skip Button
                val skipInteraction = remember { MutableInteractionSource() }
                val isSkipPressed by skipInteraction.collectIsPressedAsState()
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
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0x22FFFFFF))
                        .border(BorderStroke(1.dp, Color(0x33FFFFFF)), RoundedCornerShape(18.dp))
                        .clickable(
                            interactionSource = skipInteraction,
                            indication = androidx.compose.foundation.LocalIndication.current
                        ) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            finishOnboarding()
                        }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("skip_onboarding_button")
                ) {
                    Text(
                        text = "Skip",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite.copy(alpha = 0.85f),
                        letterSpacing = 0.3.sp
                    )
                }
            }

            // Swipeable Onboarding Horizontal Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { pageIdx ->
                val cardData = cards[pageIdx]

                // Page Parallax Offset Calculation
                val pageOffset = (
                    (pagerState.currentPage - pageIdx) + pagerState.currentPageOffsetFraction
                ).coerceIn(-1f, 1f)

                val pageAlpha = (1f - abs(pageOffset) * 0.55f).coerceIn(0f, 1f)
                val pageScale = (1f - abs(pageOffset) * 0.08f).coerceIn(0.92f, 1f)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp)
                        .graphicsLayer {
                            alpha = pageAlpha
                            scaleX = pageScale
                            scaleY = pageScale
                        }
                ) {
                    OnboardingCardItem(
                        cardData = cardData,
                        pageOffset = pageOffset,
                        isLastPage = isLastPage,
                        onActionClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            finishOnboarding()
                        }
                    )
                }
            }

            // Bottom Navigation Controls Bar: Arrows, Page Dots, & Main CTA
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp, top = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left Chevron Arrow Indicator (Previous Card)
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(if (pagerState.currentPage > 0) Color(0x28FFFFFF) else Color.Transparent)
                            .border(
                                BorderStroke(1.dp, if (pagerState.currentPage > 0) Color(0x44FFFFFF) else Color.Transparent),
                                CircleShape
                            )
                            .clickable(enabled = pagerState.currentPage > 0) {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (pagerState.currentPage > 0) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Previous Card",
                                tint = TextWhite,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Animated Page Dots Indicator
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(cards.size) { index ->
                            val isSelected = pagerState.currentPage == index
                            val dotWidth by animateDpAsState(
                                targetValue = if (isSelected) 28.dp else 8.dp,
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                                label = "dotWidth"
                            )
                            val dotColor by animateColorAsState(
                                targetValue = if (isSelected) currentCard.primaryColor else Color(0x33FFFFFF),
                                animationSpec = tween(300),
                                label = "dotColor"
                            )

                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp)
                                    .height(8.dp)
                                    .width(dotWidth)
                                    .shadow(
                                        elevation = if (isSelected) 8.dp else 0.dp,
                                        shape = CircleShape,
                                        spotColor = currentCard.primaryColor
                                    )
                                    .clip(CircleShape)
                                    .background(dotColor)
                                    .clickable {
                                        scope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    }
                            )
                        }
                    }

                    // Right Chevron Arrow Indicator (Next Card)
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(if (!isLastPage) currentCard.primaryColor.copy(alpha = 0.25f) else Color.Transparent)
                            .border(
                                BorderStroke(1.dp, if (!isLastPage) currentCard.primaryColor else Color.Transparent),
                                CircleShape
                            )
                            .clickable(enabled = !isLastPage) {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (!isLastPage) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Next Card",
                                tint = TextWhite,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // If Last Page: Prominent Single-Line Compact CTA Button
                if (isLastPage) {
                    Spacer(modifier = Modifier.height(12.dp))

                    val btnInteraction = remember { MutableInteractionSource() }
                    val isBtnPressed by btnInteraction.collectIsPressedAsState()
                    val btnScale by animateFloatAsState(
                        targetValue = if (isBtnPressed) 0.95f else 1f,
                        animationSpec = spring(stiffness = Spring.StiffnessHigh),
                        label = "btnScale"
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = btnScale
                                scaleY = btnScale
                            }
                            .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = EmeraldGlow)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(EmeraldPrimary, EmeraldGlow)
                                )
                            )
                            .border(
                                BorderStroke(1.2.dp, Color.White.copy(alpha = 0.85f)),
                                RoundedCornerShape(24.dp)
                            )
                            .clickable(
                                interactionSource = btnInteraction,
                                indication = androidx.compose.foundation.LocalIndication.current
                            ) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                finishOnboarding()
                            }
                            .padding(vertical = 13.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Start Creating with ViralToolAI",
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Black,
                                color = AmoledBlack,
                                maxLines = 1,
                                letterSpacing = 0.2.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = AmoledBlack,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingCardItem(
    cardData: OnboardingCardData,
    pageOffset: Float,
    isLastPage: Boolean,
    onActionClick: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val maxHeight = maxHeight

        // iPhone Glassmorphic Main Card
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(32.dp),
                    spotColor = cardData.primaryColor.copy(alpha = 0.5f)
                )
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xF00D1812),
                            Color(0xF808110D),
                            Color(0xFF040A07)
                        )
                    )
                )
                .border(
                    BorderStroke(
                        1.2.dp,
                        Brush.linearGradient(
                            colors = listOf(
                                cardData.primaryColor.copy(alpha = 0.85f),
                                cardData.secondaryColor.copy(alpha = 0.45f),
                                Color.White.copy(alpha = 0.7f)
                            )
                        )
                    ),
                    RoundedCornerShape(32.dp)
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Badge Header
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            translationX = pageOffset * -40f
                        }
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardData.primaryColor.copy(alpha = 0.16f))
                        .border(
                            BorderStroke(1.dp, cardData.primaryColor.copy(alpha = 0.6f)),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = cardData.icon,
                            contentDescription = null,
                            tint = cardData.primaryColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = cardData.badge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = cardData.primaryColor,
                            letterSpacing = 1.2.sp
                        )
                    }
                }

                // Middle Floating Illustration Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    OnboardingFloatingIllustration(
                        cardData = cardData,
                        pageOffset = pageOffset
                    )
                }

                // Title, Subtitle & Highlights Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            translationX = pageOffset * -30f
                        }
                ) {
                    Text(
                        text = cardData.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = cardData.subtitle,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextWhite.copy(alpha = 0.72f),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )

                    // Highlights List (Cards 2, 3, 4)
                    if (cardData.highlights.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            cardData.highlights.chunked(2).forEach { rowItems ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowItems.forEach { item ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(Color(0x33000000))
                                                .border(
                                                    BorderStroke(1.dp, cardData.primaryColor.copy(alpha = 0.4f)),
                                                    RoundedCornerShape(14.dp)
                                                )
                                                .padding(horizontal = 10.dp, vertical = 7.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = cardData.primaryColor,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                                Text(
                                                    text = item,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextWhite,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                    if (rowItems.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
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
private fun OnboardingFloatingIllustration(
    cardData: OnboardingCardData,
    pageOffset: Float
) {
    val infiniteTransition = rememberInfiniteTransition(label = "heroMotion")

    val floatY by infiniteTransition.animateFloat(
        initialValue = -7f,
        targetValue = 7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatY"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                translationY = floatY
            },
        contentAlignment = Alignment.Center
    ) {
        // Glowing Background Orb
        Box(
            modifier = Modifier
                .size(160.dp)
                .graphicsLayer {
                    translationX = pageOffset * 50f
                }
                .background(
                    Brush.radialGradient(
                        listOf(
                            cardData.primaryColor.copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )

        // Center Hero Graphic Circle
        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationX = pageOffset * -80f
                    rotationZ = pageOffset * 8f
                }
                .size(90.dp)
                .shadow(20.dp, CircleShape, spotColor = cardData.primaryColor)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(cardData.primaryColor, cardData.secondaryColor)
                    )
                )
                .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.85f)), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = cardData.icon,
                contentDescription = cardData.title,
                tint = AmoledBlack,
                modifier = Modifier.size(44.dp)
            )
        }

        // Floating Glass Accent Badges according to card index
        when (cardData.pageIndex) {
            0 -> {
                // Workspace Accents
                FloatingGlassChip(
                    text = "AI Workspace",
                    icon = Icons.Default.AutoAwesome,
                    color = cardData.primaryColor,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .graphicsLayer { translationX = pageOffset * -60f - 10f; translationY = -10f }
                )
                FloatingGlassChip(
                    text = "Deal Radar",
                    icon = Icons.Default.ShoppingBag,
                    color = cardData.secondaryColor,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .graphicsLayer { translationX = pageOffset * 60f + 10f; translationY = 10f }
                )
            }
            1 -> {
                // Content Creation Accents
                FloatingGlassChip(
                    text = "Prompt Extractor",
                    icon = Icons.Default.AutoAwesome,
                    color = cardData.primaryColor,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .graphicsLayer { translationX = pageOffset * 50f; translationY = -12f }
                )
                FloatingGlassChip(
                    text = "4K Video AI",
                    icon = Icons.Default.Movie,
                    color = cardData.secondaryColor,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .graphicsLayer { translationX = pageOffset * -50f; translationY = 12f }
                )
            }
            2 -> {
                // Platform Growth Accents
                FloatingGlassChip(
                    text = "10x Reach",
                    icon = Icons.Default.TrendingUp,
                    color = cardData.primaryColor,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .graphicsLayer { translationX = pageOffset * -50f; translationY = -10f }
                )
                FloatingGlassChip(
                    text = "Brand Deals AI",
                    icon = Icons.Default.AutoAwesome,
                    color = cardData.secondaryColor,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .graphicsLayer { translationX = pageOffset * 50f; translationY = 10f }
                )
            }
        }
    }
}

@Composable
private fun FloatingGlassChip(
    text: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xDD000000))
            .border(BorderStroke(1.dp, color.copy(alpha = 0.7f)), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = text,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }
    }
}
