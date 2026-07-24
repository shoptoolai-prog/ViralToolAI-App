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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Soft Ambient Emerald Glow in Background
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp)
                .size(280.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            EmeraldPrimary.copy(alpha = 0.25f),
                            ElectricPurple.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
                .blur(80.dp)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar with Brand & Skip Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mini Brand Logo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(EmeraldPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = AmoledBlack,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "ViralToolAI",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                }

                // Skip Button
                if (pagerState.currentPage < pages.size - 1) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x1AFFFFFF))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                finishOnboarding()
                            }
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Skip",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextGray
                        )
                    }
                }
            }

            // Pager Body
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { pageIdx ->
                val pageData = pages[pageIdx]
                OnboardingPageContent(pageData = pageData)
            }

            // Bottom Navigation Controls
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animated Pager Indicators
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    repeat(pages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        val width by animateDpAsState(
                            targetValue = if (isSelected) 32.dp else 8.dp,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium)
                        )
                        val color by animateColorAsState(
                            targetValue = if (isSelected) EmeraldGlow else Color(0x33FFFFFF),
                            animationSpec = tween(300)
                        )

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(8.dp)
                                .width(width)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }

                // Liquid Primary Action Pill Button
                val isLastPage = pagerState.currentPage == pages.size - 1
                var isPressed by remember { mutableStateOf(false) }
                val buttonScale by animateFloatAsState(
                    targetValue = if (isPressed) 0.95f else 1f,
                    animationSpec = spring(stiffness = Spring.StiffnessHigh)
                )

                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (isLastPage) {
                            finishOnboarding()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .scale(buttonScale)
                        .shadow(16.dp, RoundedCornerShape(28.dp), spotColor = EmeraldPrimary)
                        .testTag(if (isLastPage) "get_started_button" else "continue_onboarding_button"),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(EmeraldPrimary, EmeraldGlow)
                                ),
                                RoundedCornerShape(28.dp)
                            )
                            .border(
                                1.dp,
                                Brush.horizontalGradient(
                                    listOf(Color.White.copy(alpha = 0.8f), EmeraldLight)
                                ),
                                RoundedCornerShape(28.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (isLastPage) "Get Started" else "Continue",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
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
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Created by Asit • ViralToolAI",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextGray.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun OnboardingPageContent(pageData: OnboardingPageData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Large Illustration Hero Glass Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFF0F0F1A))
                .border(
                    BorderStroke(
                        1.5.dp,
                        Brush.linearGradient(
                            listOf(EmeraldPrimary.copy(alpha = 0.6f), ElectricPurple.copy(alpha = 0.4f), Color(0x22FFFFFF))
                        )
                    ),
                    RoundedCornerShape(32.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Background Radial Glow
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(EmeraldPrimary.copy(alpha = 0.35f), Color.Transparent)
                        ),
                        CircleShape
                    )
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Badge Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(EmeraldPrimary.copy(alpha = 0.2f))
                        .border(1.dp, EmeraldPrimary.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = pageData.badgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = EmeraldGlow,
                        letterSpacing = 1.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Icon Container
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .shadow(16.dp, CircleShape, spotColor = EmeraldPrimary)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(EmeraldPrimary, Color(0xFF059669))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = pageData.icon,
                        contentDescription = pageData.title,
                        tint = AmoledBlack,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Title
        Text(
            text = pageData.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite,
            textAlign = TextAlign.Center,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle
        Text(
            text = pageData.subtitle.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary,
            textAlign = TextAlign.Center,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            text = pageData.description,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = TextGray,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}
