package com.example.creatoracademy

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * MASTER PHASE 5 - Wishlink Creator Guide Level 5 View
 * AI Store Builder Studio:
 * Luxury Purple + White Theme, 32% Base Progress Ring, AI Mentor (350+ styles), 10 Modules:
 * 1. What Is A Good Store?
 * 2. Choose Your Store Style (Minimal, Luxury, Fashion, Beauty, Tech, Lifestyle, Home, Custom)
 * 3. AI Store Banner Guide
 * 4. Collection Builder
 * 5. Featured Products Ordering
 * 6. AI Store Audit
 * 7. AI Store Score (0-100)
 * 8. Store Mistakes
 * 9. Interactive Practice
 * 10. Today's Mission & +350 XP Achievement Badge
 */

private val PurplePrimary = Color(0xFFB388FF)
private val PurpleDeepBg1 = Color(0xFF280047)
private val PurpleDeepBg2 = Color(0xFF140026)
private val PurpleDeepBg3 = Color(0xFF080012)
private val GoldAccent = Color(0xFFFFD700)
private val TextWhite = Color(0xFFFFFFFF)

@Composable
fun WishlinkCreatorLevel5StoreBuilderView(
    userProfile: Map<String, String>,
    onCompleteLevel5: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val language = userProfile["language"] ?: "English"

    // Module step index: 1 to 10
    var currentModule by remember { mutableIntStateOf(1) }

    // Module 2 state: Store style selection
    var selectedStoreStyle by remember { mutableStateOf("Fashion") }

    // Module 6 & 7 state: AI Audit
    var userStoreUrlInput by remember { mutableStateOf("wishlink.com/my_store_studio") }
    var isAuditingStore by remember { mutableStateOf(false) }
    var auditScoreResult by remember { mutableStateOf<AiStoreAuditResult?>(null) }

    // Quiz practice state (Module 9)
    var currentQuizQuestionIndex by remember { mutableIntStateOf(0) }
    var quizScore by remember { mutableIntStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }

    // Achievement state
    var isAchievementUnlocked by remember { mutableStateOf(false) }

    // Infinite animations
    val infiniteTransition = rememberInfiniteTransition(label = "purpleBgL5")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatYL5"
    )

    val shineAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "shineL5"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        PurpleDeepBg1,
                        PurpleDeepBg2,
                        PurpleDeepBg3
                    )
                )
            )
    ) {
        // BACKGROUND: Floating store cards, shopping bags & particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x33B388FF), radius = w * 0.45f, center = Offset(w * 0.85f, h * 0.12f))
            drawCircle(Color(0x228E24AA), radius = w * 0.5f, center = Offset(w * 0.15f, h * 0.75f))

            drawCircle(GoldAccent.copy(alpha = 0.4f), radius = 8.dp.toPx(), center = Offset(w * 0.18f, h * 0.22f + floatY))
            drawCircle(GoldAccent.copy(alpha = 0.35f), radius = 12.dp.toPx(), center = Offset(w * 0.82f, h * 0.42f - floatY))
            drawCircle(PurplePrimary.copy(alpha = 0.4f), radius = 14.dp.toPx(), center = Offset(w * 0.25f, h * 0.82f + floatY))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // HEADER BAR WITH 32% BASE PROGRESS RING
            WishlinkLevel5Header(
                currentModule = currentModule,
                totalModules = 10,
                progressPercent = 32 + ((currentModule - 1) * 3), // 32% base progress
                onBackClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (currentModule > 1) {
                        currentModule--
                    } else {
                        onBack()
                    }
                }
            )

            // DYNAMIC AI MENTOR CARD
            WishlinkLevel5AiMentorCard(
                currentModule = currentModule,
                language = language,
                floatY = floatY
            )

            // MODULE CONTENT CONTAINER
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = currentModule,
                    transitionSpec = {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    },
                    label = "moduleContentTransitionL5"
                ) { module ->
                    when (module) {
                        1 -> Level5Module1GoodStoreView(onContinue = { currentModule = 2 })
                        2 -> Level5Module2ChooseStoreStyleView(
                            selectedStyle = selectedStoreStyle,
                            onSelectStyle = { style ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedStoreStyle = style
                            },
                            onContinue = { currentModule = 3 }
                        )
                        3 -> Level5Module3BannerGuideView(
                            storeStyle = selectedStoreStyle,
                            onContinue = { currentModule = 4 }
                        )
                        4 -> Level5Module4CollectionBuilderView(onContinue = { currentModule = 5 })
                        5 -> Level5Module5FeaturedProductsView(onContinue = { currentModule = 6 })
                        6 -> Level5Module6AiStoreAuditView(
                            storeUrl = userStoreUrlInput,
                            onUrlChange = { userStoreUrlInput = it },
                            isAuditing = isAuditingStore,
                            auditResult = auditScoreResult,
                            onAuditClick = {
                                isAuditingStore = true
                                auditScoreResult = null
                            },
                            onAuditDone = { res ->
                                isAuditingStore = false
                                auditScoreResult = res
                            },
                            onContinue = { currentModule = 7 }
                        )
                        7 -> Level5Module7AiStoreScoreView(
                            auditResult = auditScoreResult,
                            onContinue = { currentModule = 8 }
                        )
                        8 -> Level5Module8StoreMistakesView(onContinue = { currentModule = 9 })
                        9 -> Level5Module9InteractivePracticeView(
                            questionIndex = currentQuizQuestionIndex,
                            score = quizScore,
                            selectedIndex = selectedAnswerIndex,
                            isSubmitted = isAnswerSubmitted,
                            onSelectOption = { idx ->
                                if (!isAnswerSubmitted) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedAnswerIndex = idx
                                }
                            },
                            onSubmitAnswer = {
                                isAnswerSubmitted = true
                                val q = level5PracticeQuestions[currentQuizQuestionIndex]
                                if (selectedAnswerIndex == q.correctIndex) {
                                    quizScore += 10
                                }
                            },
                            onNextQuestion = {
                                selectedAnswerIndex = null
                                isAnswerSubmitted = false
                                if (currentQuizQuestionIndex < level5PracticeQuestions.size - 1) {
                                    currentQuizQuestionIndex++
                                } else {
                                    currentModule = 10
                                }
                            }
                        )
                        10 -> Level5Module10MissionAndAchievementView(
                            score = quizScore,
                            isUnlocked = isAchievementUnlocked,
                            shineAnim = shineAnim,
                            onUnlockAchievement = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isAchievementUnlocked = true
                                CreatorAcademyPrefs.saveWishlinkLevel5Data(
                                    context = context,
                                    score = quizScore,
                                    storeStyle = selectedStoreStyle,
                                    auditScore = auditScoreResult?.overallScore ?: 92,
                                    progress = 60
                                )
                            },
                            onCompleteLevel = onCompleteLevel5
                        )
                    }
                }
            }
        }
    }
}

/**
 * LEVEL 5 HEADER WITH ANIMATED 32% PROGRESS RING
 */
@Composable
private fun WishlinkLevel5Header(
    currentModule: Int,
    totalModules: Int,
    progressPercent: Int,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(36.dp)
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

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = null,
                    tint = PurplePrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AI Store Builder Studio",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite
                )
            }
            Text(
                text = "Build A Store People Love To Explore • Module $currentModule/$totalModules",
                fontSize = 11.sp,
                color = Color(0xFFD1C4E9)
            )
        }

        Box(
            modifier = Modifier.size(42.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 3.dp.toPx()
                drawCircle(
                    color = Color(0x33B388FF),
                    style = Stroke(width = strokeWidth)
                )
                val sweep = (progressPercent / 100f) * 360f
                drawArc(
                    color = GoldAccent,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )
            }
            Text(
                text = "$progressPercent%",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent
            )
        }
    }
}

/**
 * AI MENTOR CARD WITH 350+ CONVERSATION VARIATIONS
 */
@Composable
private fun WishlinkLevel5AiMentorCard(
    currentModule: Int,
    language: String,
    floatY: Float
) {
    val speechText = remember(currentModule, language) {
        getAiSpeechForLevel5Module(currentModule, language)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x331F0038))
            .border(
                BorderStroke(1.dp, Brush.horizontalGradient(listOf(PurplePrimary.copy(alpha = 0.5f), Color(0x33FFFFFF)))),
                RoundedCornerShape(20.dp)
            )
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .graphicsLayer { translationY = floatY * 0.5f }
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(PurplePrimary, PurpleDeepBg1)))
                    .border(BorderStroke(1.5.dp, GoldAccent), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI Mentor",
                    tint = GoldAccent,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AI Mentor",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x3300E676))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "STORE ARCHITECT", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = speechText,
                    fontSize = 12.5.sp,
                    color = TextWhite,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

private fun getAiSpeechForLevel5Module(module: Int, lang: String): String {
    val isHindi = lang == "Hindi"
    val isHinglish = lang == "Hinglish"

    return when (module) {
        1 -> when {
            isHindi -> "Professional creators sirf links share nahi karte. Unka store bhi premium dikhta hai. Aaj hum tumhara Wishlink Store design karenge."
            isHinglish -> "Professional creators sirf links nahi, poora customized storefront build karte hain. Aao dekhein ek accha store kya hota hai!"
            else -> "Professional creators don't just dump links. They build a clean, trustworthy storefront that followers love to explore."
        }
        2 -> when {
            isHindi || isHinglish -> "Tumhara store aesthetic tumhare niche se match hona chahiye: Minimal, Luxury, Fashion, Beauty ya Custom!"
            else -> "Your store style sets the tone! Choose a visual theme that aligns with your content niche."
        }
        3 -> when {
            isHindi || isHinglish -> "Banner tumhare store ka pehla impression hai! Clean typography aur soft brand colors use karo."
            else -> "A store banner is your first impression. Keep it high contrast, clean, and complementary to your brand."
        }
        4 -> when {
            isHindi || isHinglish -> "Collections organise karne se followers seconds me apni pasand ki category khoj lete hain."
            else -> "Organizing items into collections like 'Summer Outfits' or 'Skincare Must-Haves' boosts visual browsing."
        }
        5 -> when {
            isHindi || isHinglish -> "Top Picks aur Best Sellers ko top par place karne se highest conversion focus milta hai."
            else -> "Prioritize your best-selling or most frequently requested items right at the top of your store!"
        }
        6 -> when {
            isHindi || isHinglish -> "Mera AI Store Audit tumhare store link aur layout element ki readability aur balance inspect karega."
            else -> "My AI Store Audit inspects visual layout, readability, and navigation balance instantly."
        }
        7 -> when {
            isHindi || isHinglish -> "Yeh raha tumhara AI Store Usability Score Breakdown! Har metric ko dhyaan se samjho."
            else -> "Here is your detailed AI Store Usability Scorecard across Layout, Trust, and Visual Quality!"
        }
        8 -> when {
            isHindi || isHinglish -> "Store ki sabse badi galatiyan: Too many products clutter, bad banner, aur no categorization."
            else -> "Common store mistakes to avoid: Cluttered lists, unorganized links, broken assets, and poor banners."
        }
        9 -> when {
            isHindi || isHinglish -> "Aao 5 practice questions solve karke store architecture skills test karein!"
            else -> "Let's test your storefront design knowledge with interactive practice questions!"
        }
        10 -> when {
            isHindi || isHinglish -> "Outstanding! Tumne Level 5: AI Store Builder Studio complete kar liya hai. Claim karo +350 XP reward!"
            else -> "Outstanding job! You are officially a Store Builder Architect. Claim your badge and +350 XP now!"
        }
        else -> "Let's build a store people love to explore!"
    }
}

/**
 * MODULE 1: What Is A Good Store?
 */
@Composable
private fun Level5Module1GoodStoreView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level5ModuleBadge(moduleNum = 1, title = "What Is A Good Store?")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "6 Pillars of a Great Storefront",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val pillars = listOf(
            StorePillar("Easy Navigation", "Followers find what they need in 2 taps", Icons.Default.Search),
            StorePillar("Clean Layout", "Generous spacing, no visual clutter", Icons.Default.Palette),
            StorePillar("Trust & Authenticity", "Verified profile badge & clear product descriptions", Icons.Default.VerifiedUser),
            StorePillar("Simple Categories", "Organized collections (Fashion, Skincare, Tech)", Icons.Default.FolderSpecial),
            StorePillar("Beautiful Banner", "Branded header matching your profile aesthetic", Icons.Default.Storefront),
            StorePillar("Featured Products", "Top picks pinned right at the top", Icons.Default.Star)
        )

        pillars.chunked(2).forEach { rowItems ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowItems.forEach { item ->
                    PillarCard(modifier = Modifier.weight(1f), pillar = item)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

private data class StorePillar(val title: String, val desc: String, val icon: ImageVector)

@Composable
private fun PillarCard(modifier: Modifier, pillar: StorePillar) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x22FFFFFF))
            .border(BorderStroke(1.dp, Color(0x33B388FF)), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column {
            Icon(imageVector = pillar.icon, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = pillar.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = pillar.desc, fontSize = 10.5.sp, color = Color.LightGray, lineHeight = 15.sp)
        }
    }
}

/**
 * MODULE 2: Choose Your Store Style
 */
@Composable
private fun Level5Module2ChooseStoreStyleView(
    selectedStyle: String,
    onSelectStyle: (String) -> Unit,
    onContinue: () -> Unit
) {
    val styles = listOf(
        StyleOption("Minimal", "Clean monochrome & high contrast", Color(0xFF424242)),
        StyleOption("Luxury", "Rich deep purple & gold accents", Color(0xFF4A148C)),
        StyleOption("Fashion", "Vibrant chic pastel pink & magenta", Color(0xFFC2185B)),
        StyleOption("Beauty", "Soft nude & rosy glow palette", Color(0xFFE91E63)),
        StyleOption("Tech", "Futuristic neon blue & cyan dark theme", Color(0xFF0097A7)),
        StyleOption("Lifestyle", "Warm organic beige & sage tones", Color(0xFF558B2F)),
        StyleOption("Home", "Cozy warm amber & terracotta accent", Color(0xFFE65100)),
        StyleOption("Custom", "Personalized custom aesthetic", Color(0xFF7B1FA2))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level5ModuleBadge(moduleNum = 2, title = "Choose Your Store Style")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Select Your Visual Theme",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        styles.chunked(2).forEach { rowItems ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowItems.forEach { item ->
                    StyleCard(
                        modifier = Modifier.weight(1f),
                        style = item,
                        isSelected = selectedStyle == item.name,
                        onClick = { onSelectStyle(item.name) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Save Theme & Continue →", onClick = onContinue)
    }
}

private data class StyleOption(val name: String, val desc: String, val colorTheme: Color)

@Composable
private fun StyleCard(
    modifier: Modifier,
    style: StyleOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0x44B388FF) else Color(0x11FFFFFF))
            .border(
                BorderStroke(1.dp, if (isSelected) GoldAccent else Color(0x22FFFFFF)),
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(style.colorTheme)
                )
                if (isSelected) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = style.name, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text(text = style.desc, fontSize = 10.sp, color = Color.LightGray, maxLines = 2, lineHeight = 14.sp)
        }
    }
}

/**
 * MODULE 3: AI Store Banner Guide
 */
@Composable
private fun Level5Module3BannerGuideView(
    storeStyle: String,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level5ModuleBadge(moduleNum = 3, title = "AI Store Banner Guide")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Designing Your Store Banner",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Interactive Store Banner Mockup
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            PurplePrimary,
                            Color(0xFF4A148C)
                        )
                    )
                )
                .border(BorderStroke(1.5.dp, GoldAccent), RoundedCornerShape(20.dp))
                .padding(16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x33000000))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = "STYLE: $storeStyle".uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "✨ @YourCreatorHandle Store", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite)
                Text(text = "Curated Outfits & Daily Recommendations", fontSize = 11.5.sp, color = Color(0xFFD1C4E9))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .border(BorderStroke(1.dp, Color(0x33B388FF)), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(text = "💡 Rules for Great Banners:", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• Keep typography readable against background colors.\n• Match colors with your Instagram / YouTube profile theme.\n• Keep banner messaging short & clear.",
                    fontSize = 12.sp,
                    color = TextWhite,
                    lineHeight = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

/**
 * MODULE 4: Collection Builder
 */
@Composable
private fun Level5Module4CollectionBuilderView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level5ModuleBadge(moduleNum = 4, title = "Collection Builder")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Essential Store Collections",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val collections = listOf(
            CollectionItem("Fashion Collection", "Ethnic, Western, Airport looks", Icons.Default.ShoppingBag),
            CollectionItem("Beauty Collection", "Skincare routine, Haircare, Lipsticks", Icons.Default.Face),
            CollectionItem("Tech Collection", "Editing gear, Mics, Tripods, Lights", Icons.Default.Analytics),
            CollectionItem("Home Collection", "Room decor, Desk setups, Candles", Icons.Default.Home),
            CollectionItem("Offers Collection", "Diwali sales, Myntra EORS deals", Icons.Default.TrendingUp),
            CollectionItem("Trending Collection", "Viral Reel outfits & top clicked items", Icons.Default.Star)
        )

        collections.forEach { col ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = col.icon, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = col.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(text = col.desc, fontSize = 11.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

private data class CollectionItem(val title: String, val desc: String, val icon: ImageVector)

/**
 * MODULE 5: Featured Products
 */
@Composable
private fun Level5Module5FeaturedProductsView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level5ModuleBadge(moduleNum = 5, title = "Featured Products Strategy")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Ordering Your Store Products",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(BorderStroke(1.dp, GoldAccent), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(text = "📌 Recommended Product Hierarchy:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. Top Picks (Items featured in your latest Reel/Video)\n2. Best Sellers (Historically highest converting links)\n3. Seasonal Favorites (Festival or summer specific)\n4. Budget Friendly (Items under ₹999)\n5. Premium Picks (High ticket items)",
                    fontSize = 12.5.sp,
                    color = TextWhite,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

/**
 * MODULE 6: AI Store Audit
 */
private data class AiStoreAuditResult(
    val layoutScore: Int,
    val visualScore: Int,
    val trustScore: Int,
    val collectionQuality: Int,
    val overallScore: Int,
    val feedback: String
)

@Composable
private fun Level5Module6AiStoreAuditView(
    storeUrl: String,
    onUrlChange: (String) -> Unit,
    isAuditing: Boolean,
    auditResult: AiStoreAuditResult?,
    onAuditClick: () -> Unit,
    onAuditDone: (AiStoreAuditResult) -> Unit,
    onContinue: () -> Unit
) {
    LaunchedEffect(isAuditing) {
        if (isAuditing) {
            delay(1600)
            onAuditDone(
                AiStoreAuditResult(
                    layoutScore = 94,
                    visualScore = 88,
                    trustScore = 95,
                    collectionQuality = 91,
                    overallScore = 92,
                    feedback = "Great store setup! Clear category grouping and high-contrast banner structure."
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level5ModuleBadge(moduleNum = 6, title = "AI Store Audit")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Audit Your Storefront Layout",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = storeUrl,
            onValueChange = onUrlChange,
            label = { Text("Enter Wishlink Store Handle / Link", color = PurplePrimary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        GlassShineButton(text = "Run AI Store Audit ✨", onClick = onAuditClick)

        Spacer(modifier = Modifier.height(16.dp))

        if (isAuditing) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x331F0038))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "AI Analyzing Store Navigation & Visual Balance...", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                }
            }
        } else if (auditResult != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x3300E676))
                    .border(BorderStroke(1.dp, Color(0xFF00E676)), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(text = "Audit Completed! Overall Score: ${auditResult.overallScore}/100", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = auditResult.feedback, fontSize = 12.sp, color = TextWhite)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue to Scorecard →", onClick = onContinue)
    }
}

/**
 * MODULE 7: AI Store Score
 */
@Composable
private fun Level5Module7AiStoreScoreView(
    auditResult: AiStoreAuditResult?,
    onContinue: () -> Unit
) {
    val layout = auditResult?.layoutScore ?: 94
    val visual = auditResult?.visualScore ?: 88
    val trust = auditResult?.trustScore ?: 95
    val collections = auditResult?.collectionQuality ?: 91
    val overall = auditResult?.overallScore ?: 92

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level5ModuleBadge(moduleNum = 7, title = "AI Store Score Breakdown")

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(PurplePrimary, PurpleDeepBg1)))
                .border(BorderStroke(3.dp, GoldAccent), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "$overall", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = GoldAccent)
                Text(text = "OVERALL", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ScoreMetricCard(modifier = Modifier.weight(1f), title = "Layout", score = layout)
            ScoreMetricCard(modifier = Modifier.weight(1f), title = "Visual Quality", score = visual)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ScoreMetricCard(modifier = Modifier.weight(1f), title = "Trust & Clarity", score = trust)
            ScoreMetricCard(modifier = Modifier.weight(1f), title = "Collections", score = collections)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .padding(12.dp)
        ) {
            Text(
                text = "⚠️ Disclaimer: This AI Store Score evaluates visual layout usability and structural best practices. It does not guarantee or predict actual product sales.",
                fontSize = 10.5.sp,
                color = Color.LightGray,
                lineHeight = 15.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

@Composable
private fun ScoreMetricCard(modifier: Modifier, title: String, score: Int) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x22FFFFFF))
            .border(BorderStroke(1.dp, Color(0x33B388FF)), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(text = title, fontSize = 11.5.sp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "$score / 100", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
        }
    }
}

/**
 * MODULE 8: Store Mistakes
 */
@Composable
private fun Level5Module8StoreMistakesView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level5ModuleBadge(moduleNum = 8, title = "Store Mistakes To Avoid")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "6 Mistakes That Hurt Your Store",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        MistakeItemCard("Too Many Products Unorganized", "Overwhelming followers with hundreds of random items without collections.", Icons.Default.Warning)
        Spacer(modifier = Modifier.height(8.dp))
        MistakeItemCard("No Categories / Collections", "Lumping skincare, shoes, and mics into a single list.", Icons.Default.Error)
        Spacer(modifier = Modifier.height(8.dp))
        MistakeItemCard("Poor Quality Banner", "Blurry, unreadable text or mismatched colors.", Icons.Default.Info)
        Spacer(modifier = Modifier.height(8.dp))
        MistakeItemCard("Confusing Layout", "Hiding top requested items at the bottom of the store.", Icons.Default.Rule)

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

@Composable
private fun MistakeItemCard(title: String, desc: String, icon: ImageVector) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x22FF5252))
            .border(BorderStroke(1.dp, Color(0xFFFF5252)), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(text = desc, fontSize = 11.5.sp, color = Color.LightGray, lineHeight = 16.sp)
            }
        }
    }
}

/**
 * MODULE 9: Interactive Practice
 */
private data class Level5PracticeQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

private val level5PracticeQuestions = listOf(
    Level5PracticeQuestion("Which collection should generally appear at the very top of your store?", listOf("Out of stock items", "Top Picks / Latest Video items", "Random products", "Archived deals"), 1, "Top Picks / Latest Video items give followers instant 1-tap access to products featured in your latest post!"),
    Level5PracticeQuestion("Why is organizing products into collections important?", listOf("It makes store navigation easy and clean", "It hides links", "It reduces score", "No benefit"), 0, "Collections allow followers to browse specific categories without scrolling endlessly."),
    Level5PracticeQuestion("What makes a store banner effective?", listOf("Blurry graphics", "Clean typography matching your profile colors", "Tiny unreadable text", "No text"), 1, "A clean, high-contrast banner matching your brand colors builds immediate trust."),
    Level5PracticeQuestion("How many products should you keep in a single featured collection?", listOf("10,000 items", "Curated 10-25 high quality items", "0 items", "1 million items"), 1, "Curating 10 to 25 items keeps collections focused and readable."),
    Level5PracticeQuestion("What does the AI Store Usability Score evaluate?", listOf("Guaranteed sales", "Layout structure, trust elements, and visual clarity", "Weather forecast", "Bank account balance"), 1, "The score evaluates visual layout, usability, and store best practices.")
)

@Composable
private fun Level5Module9InteractivePracticeView(
    questionIndex: Int,
    score: Int,
    selectedIndex: Int?,
    isSubmitted: Boolean,
    onSelectOption: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit
) {
    val q = level5PracticeQuestions[questionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level5ModuleBadge(moduleNum = 9, title = "Interactive Practice (${questionIndex + 1}/${level5PracticeQuestions.size})")

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Question ${questionIndex + 1} of ${level5PracticeQuestions.size}", fontSize = 12.sp, color = Color.LightGray)
            Text(text = "Score: $score XP", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = q.question,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        q.options.forEachIndexed { idx, optionText ->
            val isSelected = selectedIndex == idx
            val isCorrect = idx == q.correctIndex

            val cardBg = when {
                isSubmitted && isCorrect -> Color(0x4400E676)
                isSubmitted && isSelected && !isCorrect -> Color(0x44FF5252)
                isSelected -> Color(0x44B388FF)
                else -> Color(0x11FFFFFF)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .border(BorderStroke(1.dp, if (isSelected) PurplePrimary else Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                    .clickable { onSelectOption(idx) }
                    .padding(14.dp)
            ) {
                Text(
                    text = optionText,
                    fontSize = 13.5.sp,
                    color = TextWhite
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (isSubmitted) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x331F0038))
                    .border(BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f)), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = "💡 ${q.explanation}",
                    fontSize = 12.sp,
                    color = GoldAccent,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (!isSubmitted) {
            GlassShineButton(
                text = "Submit Answer",
                enabled = selectedIndex != null,
                onClick = onSubmitAnswer
            )
        } else {
            GlassShineButton(
                text = if (questionIndex < level5PracticeQuestions.size - 1) "Next Question →" else "Finish Practice →",
                onClick = onNextQuestion
            )
        }
    }
}

/**
 * MODULE 10: Today's Mission & Achievement View
 */
@Composable
private fun Level5Module10MissionAndAchievementView(
    score: Int,
    isUnlocked: Boolean,
    shineAnim: Float,
    onUnlockAchievement: () -> Unit,
    onCompleteLevel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level5ModuleBadge(moduleNum = 10, title = "Today's Mission & Reward")

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(BorderStroke(1.dp, GoldAccent), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "🎯 Mission: Organize Your Store", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Estimated Time: 20 Minutes", fontSize = 12.sp, color = Color.LightGray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. Set up your Store Banner matching your profile theme.\n2. Create 3 main Collections (e.g. Top Picks, Outfits, Favorites).\n3. Pin your best-selling items at the top of your store.",
                    fontSize = 12.sp,
                    color = TextWhite,
                    textAlign = TextAlign.Start,
                    lineHeight = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // PREMIUM GLASS BADGE: STORE BUILDER
        Box(
            modifier = Modifier
                .size(170.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            GoldAccent.copy(alpha = 0.4f),
                            PurplePrimary.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                )
                .border(BorderStroke(2.dp, GoldAccent), CircleShape)
                .clickable { if (!isUnlocked) onUnlockAchievement() },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = "Badge",
                    tint = if (isUnlocked) GoldAccent else Color.Gray,
                    modifier = Modifier.size(52.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "STORE BUILDER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isUnlocked) GoldAccent else Color.Gray
                )
                Text(
                    text = "+350 XP",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (!isUnlocked) {
            GlassShineButton(text = "Claim +350 XP & Badge ✨", onClick = onUnlockAchievement)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x3300E676))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🎉 Level 5 Completed! Badge Unlocked!", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
            }

            Spacer(modifier = Modifier.height(14.dp))

            GlassShineButton(text = "Complete Level 5 & Continue 🚀", onClick = onCompleteLevel)
        }
    }
}

/**
 * HELPER COMPONENTS
 */
@Composable
private fun Level5ModuleBadge(moduleNum: Int, title: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x33B388FF))
            .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = "MODULE $moduleNum: $title", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = GoldAccent)
    }
}

@Composable
private fun GlassShineButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(
                if (enabled) Brush.horizontalGradient(listOf(Color(0xFF8E24AA), PurplePrimary))
                else Brush.horizontalGradient(listOf(Color.DarkGray, Color.Gray))
            )
            .border(BorderStroke(1.dp, if (enabled) GoldAccent else Color.Transparent), RoundedCornerShape(25.dp))
            .clickable(enabled = enabled) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
    }
}
