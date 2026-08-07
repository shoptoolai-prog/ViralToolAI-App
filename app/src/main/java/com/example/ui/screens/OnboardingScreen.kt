package com.example.ui.screens

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

import com.example.R
import com.example.ui.components.ViralToolAiLogo

private val OnboardingBg = Color(0xFF0B0B0B)
private val CardBg = Color(0xFF141414)
private val SecondaryCardBg = Color(0xFF1B1B1B)
private val TextWhite = Color(0xFFFFFFFF)
private val TextGray = Color(0xFFB7B7B7)
private val CyanAccent = Color(0xFF20D9E8)

data class OnboardingPageData(
    val badge: String,
    val title: String,
    val line1: String,
    val line2: String,
    val icon: ImageVector,
    val pageType: Int
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

@Composable
fun OnboardingScreen(onOnboardingFinished: () -> Unit) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val pages = remember {
        listOf(
            OnboardingPageData(
                badge = "AI Analysis",
                title = "Analyze Before You Post",
                line1 = "Upload your reel.",
                line2 = "AI finds mistakes instantly.",
                icon = Icons.Default.Analytics,
                pageType = 1
            ),
            OnboardingPageData(
                badge = "Daily Plan",
                title = "Grow Every Day",
                line1 = "Complete daily tasks.",
                line2 = "Stay consistent.",
                icon = Icons.Default.CalendarToday,
                pageType = 2
            ),
            OnboardingPageData(
                badge = "Hook Library",
                title = "Hooks That Stop Scrolls",
                line1 = "Find the perfect hook in seconds.",
                line2 = "",
                icon = Icons.Default.FormatQuote,
                pageType = 3
            ),
            OnboardingPageData(
                badge = "Ready",
                title = "Start Creating",
                line1 = "Let's build your next viral review.",
                line2 = "",
                icon = Icons.Default.RocketLaunch,
                pageType = 4
            )
        )
    }

    val pagerState = rememberPagerState(initialPage = 0) { pages.size }

    BackHandler(enabled = true) {
        if (pagerState.currentPage > 0) {
            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OnboardingBg)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar: Logo + Skip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ViralToolAiLogo(size = 28.dp)
                    Text(
                        text = "ViralToolAI",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }

                if (pagerState.currentPage < pages.size - 1) {
                    Text(
                        text = "Skip",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextGray,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                OnboardingPrefs.setOnboardingCompleted(context, true)
                                onOnboardingFinished()
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.width(40.dp))
                }
            }

            // Pager Content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) { pageIdx ->
                val item = pages[pageIdx]
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Badge Pill
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SecondaryCardBg)
                            .border(1.dp, Color(0xFF262626), CircleShape)
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(CyanAccent)
                            )
                            Text(
                                text = item.badge.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                letterSpacing = 1.2.sp
                            )
                        }
                    }

                    // Large Illustration Canvas Card
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .clip(RoundedCornerShape(28.dp)),
                        color = CardBg,
                        shape = RoundedCornerShape(28.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF222222))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            when (item.pageType) {
                                1 -> IllustrationAiAnalysis()
                                2 -> IllustrationDailyPlan()
                                3 -> IllustrationHookLibrary()
                                4 -> IllustrationStartCreating()
                            }
                        }
                    }

                    // Text Details
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = item.title,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            textAlign = TextAlign.Center
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = item.line1,
                                fontSize = 15.sp,
                                color = TextGray,
                                textAlign = TextAlign.Center
                            )
                            if (item.line2.isNotBlank()) {
                                Text(
                                    text = item.line2,
                                    fontSize = 15.sp,
                                    color = TextGray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Navigation: Dots + Large Cyan Rounded Button
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 4 Animated Dots Indicator
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        val width by animateDpAsState(
                            targetValue = if (isSelected) 28.dp else 8.dp,
                            animationSpec = tween(durationMillis = 300)
                        )
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(width)
                                .clip(CircleShape)
                                .background(if (isSelected) CyanAccent else SecondaryCardBg)
                        )
                    }
                }

                // Large Cyan Rounded Button
                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (pagerState.currentPage == pages.size - 1) {
                            OnboardingPrefs.setOnboardingCompleted(context, true)
                            onOnboardingFinished()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag(if (pagerState.currentPage == pages.size - 1) "btn_get_started" else "btn_next"),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyanAccent,
                        contentColor = Color.Black
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (pagerState.currentPage == pages.size - 1) "Get Started" else "Continue",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// Custom Premium AI Artwork Canvas Illustrations
// -------------------------------------------------------------------------

@Composable
private fun IllustrationAiAnalysis() {
    val infiniteTransition = rememberInfiniteTransition(label = "scan")
    val scanY by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scanLine"
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Background subtle grid/glow
            drawCircle(
                color = CyanAccent.copy(alpha = 0.08f),
                radius = w * 0.35f,
                center = Offset(w * 0.5f, h * 0.5f)
            )

            // Phone Frame
            val phoneWidth = w * 0.42f
            val phoneHeight = h * 0.72f
            val phoneLeft = (w - phoneWidth) / 2
            val phoneTop = (h - phoneHeight) / 2

            drawRoundRect(
                color = Color(0xFF1E1E1E),
                topLeft = Offset(phoneLeft, phoneTop),
                size = Size(phoneWidth, phoneHeight),
                cornerRadius = CornerRadius(20.dp.toPx())
            )
            drawRoundRect(
                color = CyanAccent.copy(alpha = 0.3f),
                topLeft = Offset(phoneLeft, phoneTop),
                size = Size(phoneWidth, phoneHeight),
                cornerRadius = CornerRadius(20.dp.toPx()),
                style = Stroke(width = 2.dp.toPx())
            )

            // Inner Screen (Skincare/Beauty Creator visual representation)
            drawRoundRect(
                color = Color(0xFF121212),
                topLeft = Offset(phoneLeft + 12f, phoneTop + 12f),
                size = Size(phoneWidth - 24f, phoneHeight - 24f),
                cornerRadius = CornerRadius(16.dp.toPx())
            )

            // Stylized Creator Avatar inside phone
            val avatarCenter = Offset(w * 0.5f, h * 0.45f)
            drawCircle(
                color = Color(0xFF333333),
                radius = 28.dp.toPx(),
                center = avatarCenter
            )
            drawCircle(
                color = CyanAccent.copy(alpha = 0.4f),
                radius = 16.dp.toPx(),
                center = avatarCenter
            )

            // Scanning Line across screen
            val currentScanY = phoneTop + (phoneHeight * scanY)
            drawLine(
                color = CyanAccent,
                start = Offset(phoneLeft + 12f, currentScanY),
                end = Offset(phoneLeft + phoneWidth - 12f, currentScanY),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Floating AI Audit Badges
            drawRoundRect(
                color = Color(0xFF222222),
                topLeft = Offset(w * 0.08f, h * 0.28f),
                size = Size(100.dp.toPx(), 32.dp.toPx()),
                cornerRadius = CornerRadius(12.dp.toPx())
            )
            drawRoundRect(
                color = CyanAccent.copy(alpha = 0.4f),
                topLeft = Offset(w * 0.08f, h * 0.28f),
                size = Size(100.dp.toPx(), 32.dp.toPx()),
                cornerRadius = CornerRadius(12.dp.toPx()),
                style = Stroke(width = 1.dp.toPx())
            )

            drawRoundRect(
                color = Color(0xFF222222),
                topLeft = Offset(w * 0.62f, h * 0.62f),
                size = Size(110.dp.toPx(), 32.dp.toPx()),
                cornerRadius = CornerRadius(12.dp.toPx())
            )
            drawRoundRect(
                color = CyanAccent.copy(alpha = 0.4f),
                topLeft = Offset(w * 0.62f, h * 0.62f),
                size = Size(110.dp.toPx(), 32.dp.toPx()),
                cornerRadius = CornerRadius(12.dp.toPx()),
                style = Stroke(width = 1.dp.toPx())
            )
        }

        // Overlay Text Badges
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "⚡ Hook Score 96%",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = CyanAccent,
                modifier = Modifier
                    .padding(start = 28.dp, top = 82.dp)
            )
            Text(
                text = "✓ Lighting Optimal",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 96.dp)
            )
        }
    }
}

@Composable
private fun IllustrationDailyPlan() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Workspace background board
        val boardWidth = w * 0.78f
        val boardHeight = h * 0.72f
        val boardLeft = (w - boardWidth) / 2
        val boardTop = (h - boardHeight) / 2

        drawRoundRect(
            color = Color(0xFF1A1A1A),
            topLeft = Offset(boardLeft, boardTop),
            size = Size(boardWidth, boardHeight),
            cornerRadius = CornerRadius(20.dp.toPx())
        )
        drawRoundRect(
            color = Color(0xFF282828),
            topLeft = Offset(boardLeft, boardTop),
            size = Size(boardWidth, boardHeight),
            cornerRadius = CornerRadius(20.dp.toPx()),
            style = Stroke(width = 1.5.dp.toPx())
        )

        // Growth Chart Line (Rising Cyan Trend)
        val chartPath = Path().apply {
            moveTo(boardLeft + 30f, boardTop + boardHeight - 40f)
            cubicTo(
                boardLeft + 100f, boardTop + boardHeight - 60f,
                boardLeft + 160f, boardTop + boardHeight - 110f,
                boardLeft + boardWidth - 30f, boardTop + 40f
            )
        }

        drawPath(
            path = chartPath,
            color = CyanAccent,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Glowing points on graph
        drawCircle(
            color = CyanAccent,
            radius = 6.dp.toPx(),
            center = Offset(boardLeft + boardWidth - 30f, boardTop + 40f)
        )
        drawCircle(
            color = CyanAccent.copy(alpha = 0.35f),
            radius = 12.dp.toPx(),
            center = Offset(boardLeft + boardWidth - 30f, boardTop + 40f)
        )

        // Calendar task cards
        val cardY1 = boardTop + 30f
        val cardY2 = cardY1 + 50f
        val cardY3 = cardY2 + 50f

        drawRoundRect(
            color = Color(0xFF252525),
            topLeft = Offset(boardLeft + 24f, cardY1),
            size = Size(boardWidth - 48f, 38f),
            cornerRadius = CornerRadius(8.dp.toPx())
        )
        drawRoundRect(
            color = Color(0xFF252525),
            topLeft = Offset(boardLeft + 24f, cardY2),
            size = Size(boardWidth - 48f, 38f),
            cornerRadius = CornerRadius(8.dp.toPx())
        )
        drawRoundRect(
            color = Color(0xFF252525),
            topLeft = Offset(boardLeft + 24f, cardY3),
            size = Size(boardWidth - 48f, 38f),
            cornerRadius = CornerRadius(8.dp.toPx())
        )

        // Checkmarks
        drawCircle(color = CyanAccent, radius = 8f, center = Offset(boardLeft + 44f, cardY1 + 19f))
        drawCircle(color = CyanAccent, radius = 8f, center = Offset(boardLeft + 44f, cardY2 + 19f))
        drawCircle(color = Color(0xFF555555), radius = 8f, center = Offset(boardLeft + 44f, cardY3 + 19f))
    }
}

@Composable
private fun IllustrationHookLibrary() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Background floating cards (3 stacked viral hook cards)
        val cardW = w * 0.68f
        val cardH = 54.dp.toPx()

        // Card 1 (Back)
        drawRoundRect(
            color = Color(0xFF1A1A1A),
            topLeft = Offset((w - cardW) / 2 + 16f, h * 0.18f),
            size = Size(cardW, cardH),
            cornerRadius = CornerRadius(16.dp.toPx())
        )

        // Card 2 (Middle)
        drawRoundRect(
            color = Color(0xFF222222),
            topLeft = Offset((w - cardW) / 2 - 12f, h * 0.38f),
            size = Size(cardW, cardH),
            cornerRadius = CornerRadius(16.dp.toPx())
        )

        // Card 3 (Front - Active Cyan Highlighted)
        drawRoundRect(
            color = Color(0xFF2A2A2A),
            topLeft = Offset((w - cardW) / 2, h * 0.58f),
            size = Size(cardW, cardH),
            cornerRadius = CornerRadius(16.dp.toPx())
        )
        drawRoundRect(
            color = CyanAccent,
            topLeft = Offset((w - cardW) / 2, h * 0.58f),
            size = Size(cardW, cardH),
            cornerRadius = CornerRadius(16.dp.toPx()),
            style = Stroke(width = 1.5.dp.toPx())
        )

        // Quote symbols
        drawCircle(color = CyanAccent, radius = 10f, center = Offset((w - cardW) / 2 + 28f, h * 0.58f + cardH / 2))
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(top = 28.dp),
            verticalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            Text(
                text = "🔥 \"Stop scrolling if you...\"",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray
            )
            Text(
                text = "💡 \"3 secrets nobody tells you...\"",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                text = "⚡ \"I tested this so you don't...\"",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CyanAccent
            )
        }
    }
}

@Composable
private fun IllustrationStartCreating() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Central glowing starburst / rocket propulsion ring
        drawCircle(
            color = CyanAccent.copy(alpha = 0.12f),
            radius = w * 0.38f,
            center = Offset(w * 0.5f, h * 0.5f)
        )
        drawCircle(
            color = CyanAccent.copy(alpha = 0.25f),
            radius = w * 0.24f,
            center = Offset(w * 0.5f, h * 0.5f)
        )

        // Concentric tech rings
        drawCircle(
            color = CyanAccent,
            radius = w * 0.28f,
            center = Offset(w * 0.5f, h * 0.5f),
            style = Stroke(width = 1.5.dp.toPx())
        )
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(CyanAccent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.RocketLaunch,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(38.dp)
            )
        }
    }
}
