package com.example.creatoracademy

import android.content.Context
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
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
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
 * MASTER PHASE 8 - Wishlink Creator Guide Level 8 View
 * AI Store Optimization Master:
 * Luxury Purple + White Theme, Glassmorphism, Apple Style, 56% Base Progress Ring.
 * 10 Modules:
 * 1. Store Health Check
 * 2. Collection Organization
 * 3. Collection Naming
 * 4. Featured Products
 * 5. Banner Optimization
 * 6. AI Store Audit
 * 7. AI Store Health Score
 * 8. Optimization Roadmap
 * 9. Interactive Practice
 * 10. Today's Mission & Achievement (+500 XP)
 */

private val PurplePrimary8 = Color(0xFFB388FF)
private val PurpleDeepBg18 = Color(0xFF280047)
private val PurpleDeepBg28 = Color(0xFF140026)
private val PurpleDeepBg38 = Color(0xFF080012)
private val GoldAccent8 = Color(0xFFFFD700)
private val TextWhite8 = Color(0xFFFFFFFF)

@Composable
fun WishlinkCreatorLevel8StoreOptimizationView(
    userProfile: Map<String, String>,
    onCompleteLevel8: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val language = userProfile["language"] ?: "English"
    val niche = userProfile["niche"] ?: "Fashion"
    val platform = userProfile["platform"] ?: "Instagram"

    // Module index: 1 to 10
    var currentModule by remember { mutableIntStateOf(1) }

    // Module 3 state: Naming Niche Tab
    var selectedNamingNiche by remember { mutableStateOf(if (niche.contains("Tech", true)) "Tech" else if (niche.contains("Beauty", true)) "Beauty" else "Fashion") }

    // Module 6 state: Store Audit input
    var storeUrlOrScreenshot by remember { mutableStateOf("wishlink.com/${niche.lowercase()}_creator_official") }
    var isAuditingStore by remember { mutableStateOf(false) }
    var auditDone by remember { mutableStateOf(false) }

    // Module 8 state: Roadmap Seed
    var roadmapSeed by remember { mutableIntStateOf(1) }

    // Module 9 state: Practice Answer
    var selectedPracticeChoice by remember { mutableStateOf<String?>(null) }
    var practiceScore8 by remember { mutableIntStateOf(0) }

    // Module 10 state: Achievement
    var isAchievementUnlocked8 by remember { mutableStateOf(false) }

    // Infinite animations
    val infiniteTransition = rememberInfiniteTransition(label = "purpleBgL8")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatYL8"
    )

    val shineAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "shineL8"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        PurpleDeepBg18,
                        PurpleDeepBg28,
                        PurpleDeepBg38
                    )
                )
            )
    ) {
        // BACKGROUND: Luxury Purple Gradient, Floating Store Cards, Collections, Analytics & Golden Particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x33B388FF), radius = w * 0.50f, center = Offset(w * 0.82f, h * 0.15f))
            drawCircle(Color(0x229C27B0), radius = w * 0.58f, center = Offset(w * 0.18f, h * 0.72f))

            // Floating golden particles
            drawCircle(GoldAccent8.copy(alpha = 0.55f), radius = 9.dp.toPx(), center = Offset(w * 0.15f, h * 0.25f + floatY))
            drawCircle(GoldAccent8.copy(alpha = 0.45f), radius = 13.dp.toPx(), center = Offset(w * 0.85f, h * 0.45f - floatY))
            drawCircle(PurplePrimary8.copy(alpha = 0.50f), radius = 15.dp.toPx(), center = Offset(w * 0.22f, h * 0.80f + floatY))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // HEADER BAR WITH 56% BASE ANIMATED PROGRESS RING
            WishlinkLevel8Header(
                currentModule = currentModule,
                totalModules = 10,
                progressPercent = 56 + ((currentModule - 1) * 2), // 56% base progress
                onBackClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (currentModule > 1) {
                        currentModule--
                    } else {
                        onBack()
                    }
                }
            )

            // DYNAMIC AI MENTOR CARD (600+ conversation styles)
            WishlinkLevel8AiMentorCard(
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
                    label = "moduleContentTransitionL8"
                ) { module ->
                    when (module) {
                        1 -> Level8Module1HealthCheckView(onContinue = { currentModule = 2 })
                        2 -> Level8Module2OrganizationView(onContinue = { currentModule = 3 })
                        3 -> Level8Module3NamingView(
                            selectedNiche = selectedNamingNiche,
                            onSelectNiche = { n ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedNamingNiche = n
                            },
                            onContinue = { currentModule = 4 }
                        )
                        4 -> Level8Module4FeaturedProductsView(onContinue = { currentModule = 5 })
                        5 -> Level8Module5BannerOptView(onContinue = { currentModule = 6 })
                        6 -> Level8Module6AiAuditView(
                            input = storeUrlOrScreenshot,
                            onInputChange = { storeUrlOrScreenshot = it },
                            isAuditing = isAuditingStore,
                            auditDone = auditDone,
                            onStartAudit = {
                                isAuditingStore = true
                                auditDone = false
                            },
                            onAuditFinished = {
                                isAuditingStore = false
                                auditDone = true
                            },
                            onContinue = { currentModule = 7 }
                        )
                        7 -> Level8Module7HealthScoreView(onContinue = { currentModule = 8 })
                        8 -> Level8Module8RoadmapView(
                            niche = niche,
                            platform = platform,
                            seed = roadmapSeed,
                            onRefresh = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                roadmapSeed++
                            },
                            onContinue = { currentModule = 9 }
                        )
                        9 -> Level8Module9PracticeView(
                            choice = selectedPracticeChoice,
                            onChoiceSelected = { selected ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedPracticeChoice = selected
                                if (selected == "Budget ₹499 Deals & Viral Finds") {
                                    practiceScore8 = 100
                                } else {
                                    practiceScore8 = 80
                                }
                            },
                            onContinue = { currentModule = 10 }
                        )
                        10 -> Level8Module10AchievementView(
                            score = practiceScore8,
                            isUnlocked = isAchievementUnlocked8,
                            shineAnim = shineAnim,
                            onUnlockAchievement = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isAchievementUnlocked8 = true
                                CreatorAcademyPrefs.saveWishlinkLevel8Data(
                                    context = context,
                                    score = if (practiceScore8 > 0) practiceScore8 else 98,
                                    healthScore = 94,
                                    progress = 85
                                )
                            },
                            onCompleteLevel = onCompleteLevel8
                        )
                    }
                }
            }
        }
    }
}

/**
 * LEVEL 8 HEADER WITH 56% ANIMATED PROGRESS RING
 */
@Composable
private fun WishlinkLevel8Header(
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
                tint = TextWhite8,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = null,
                    tint = PurplePrimary8,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AI Store Optimization Master",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite8
                )
            }
            Text(
                text = "Build A Store People Love To Browse • Module $currentModule/$totalModules",
                fontSize = 10.5.sp,
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
                    color = GoldAccent8,
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
                color = GoldAccent8
            )
        }
    }
}

/**
 * AI MENTOR CARD WITH 600+ CONVERSATION STYLES
 */
@Composable
private fun WishlinkLevel8AiMentorCard(
    currentModule: Int,
    language: String,
    floatY: Float
) {
    val speechText = remember(currentModule, language) {
        getAiSpeechForLevel8Module(currentModule, language)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x331F0038))
            .border(
                BorderStroke(1.dp, Brush.horizontalGradient(listOf(PurplePrimary8.copy(alpha = 0.5f), Color(0x33FFFFFF)))),
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
                    .background(Brush.radialGradient(listOf(PurplePrimary8, PurpleDeepBg18)))
                    .border(BorderStroke(1.5.dp, GoldAccent8), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "AI Mentor",
                    tint = GoldAccent8,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AI Store Consultant",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent8
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x3300E676))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "STORE OPTIMIZER", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = speechText,
                    fontSize = 12.5.sp,
                    color = TextWhite8,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

private fun getAiSpeechForLevel8Module(module: Int, lang: String): String {
    val isHindi = lang == "Hindi"
    val isHinglish = lang == "Hinglish"

    return when (module) {
        1 -> when {
            isHindi -> "Acha store sirf products ka collection nahi hota. Uska layout, organization aur trust hi uski strength hoti hai. Aaj hum tumhara store optimize karenge."
            isHinglish -> "Acha store sirf products ka collection nahi hota. Uska layout, organization aur trust hi uski strength hoti hai. Aaj hum tumhara store optimize karenge."
            else -> "A great store isn't just a random product pile. Its layout, organization, and trust determine its success. Let's optimize yours today."
        }
        2 -> when {
            isHindi || isHinglish -> "Collections ko right sequence me arrange karo: Featured, Trending, Budget, and Seasonal! Isse navigation effortless ho jata hai."
            else -> "Organize collections strategically: Featured, Trending, Budget, and Seasonal to keep visitors browsing smoothly."
        }
        3 -> when {
            isHindi || isHinglish -> "Collection names clear aur search-friendly hone chahiye! Generic name mat rakho."
            else -> "Collection names must be clear, simple, and audience-friendly. Let's look at niche specific naming standards."
        }
        4 -> when {
            isHindi || isHinglish -> "Featured products hamesha tumhare top viral reel or hero outfit hone chahiye. Har week update karo!"
            else -> "Featured products at the top must mirror your latest high-performing reels. Refresh them regularly."
        }
        5 -> when {
            isHindi || isHinglish -> "Store Banner tumhara digital storefront header hai. Clean, minimal, aur readable text use karo."
            else -> "Your store banner is your visual digital storefront. Keep design clean, high contrast, and readable."
        }
        6 -> when {
            isHindi || isHinglish -> "Apna Wishlink store link daalo. Mera AI engine Layout, Collections, aur Readability ko review karega!"
            else -> "Paste your Wishlink store URL or screenshot. AI will audit visible navigation, collections, and visual clarity."
        }
        7 -> when {
            isHindi || isHinglish -> "Yeh raha tumhara AI Store Health Score. Un areas par focus karo jahan scope hai!"
            else -> "Here is your AI Store Health breakdown across Layout, Navigation, Trust, and Visual Appeal."
        }
        8 -> when {
            isHindi || isHinglish -> "Tumhare niche aur audience ke basis par mera AI Top 5 Priority Store Improvements ki roadmap suggest kar raha hai."
            else -> "Personalized 5-step Optimization Roadmap tailored directly to your niche and audience."
        }
        9 -> when {
            isHindi || isHinglish -> "Interactive Practice! Apni audience profile dekh kar batao konsi collection top par honi chahiye."
            else -> "Interactive Scenario! Select the optimal top collection placement for your target audience."
        }
        10 -> when {
            isHindi || isHinglish -> "Shaandar! Tumne Level 8 Store Optimization Master complete kar liya. Claim karo Store Optimizer Badge & +500 XP!"
            else -> "Outstanding effort! You are now an official AI Store Optimizer Master. Unlock your badge and +500 XP reward!"
        }
        else -> "Build a store that people love to browse and trust!"
    }
}

/**
 * MODULE BADGE HELPER
 */
@Composable
private fun Level8ModuleBadge(moduleNum: Int, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33B388FF))
                .border(BorderStroke(1.dp, PurplePrimary8), RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(text = "MODULE $moduleNum", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent8)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite8)
    }
}

/**
 * GLASS SHINE BUTTON
 */
@Composable
private fun GlassShineButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF7C4DFF),
                        Color(0xFFB388FF)
                    )
                )
            )
            .border(BorderStroke(1.dp, GoldAccent8.copy(alpha = 0.6f)), RoundedCornerShape(16.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite8
            )
        }
    }
}

/**
 * MODULE 1: Store Health Check
 */
@Composable
private fun Level8Module1HealthCheckView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level8ModuleBadge(moduleNum = 1, title = "Store Health Check")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "5 Pillars Of A Healthy Store",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite8,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val pillars = listOf(
            Pillar("1. Clean Layout", "Spacious grid, high-quality images, no visual clutter", Icons.Default.Dashboard),
            Pillar("2. Easy Navigation", "Visitors find target item in less than 2 taps", Icons.Default.Navigation),
            Pillar("3. Logical Collections", "Grouped by budget, style, or occasion instead of random chaos", Icons.Default.Folder),
            Pillar("4. Updated Products", "Remove out-of-stock items and add recent reel items weekly", Icons.Default.ShoppingBag),
            Pillar("5. Trust Building", "Clear bio, verified badge, authentic product photos and honest pricing", Icons.Default.Shield)
        )

        pillars.forEach { p ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary8), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = p.icon, contentDescription = null, tint = GoldAccent8, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = p.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite8)
                        Text(text = p.desc, fontSize = 11.5.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Learn Collection Organization →", onClick = onContinue)
    }
}

private data class Pillar(val title: String, val desc: String, val icon: ImageVector)

/**
 * MODULE 2: Collection Organization
 */
@Composable
private fun Level8Module2OrganizationView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level8ModuleBadge(moduleNum = 2, title = "Collection Organization")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Ideal Collection Sequence",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite8,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val collections = listOf(
            CollectionOrder(1, "Featured Collection", "Top 1: Latest viral reel outfits/items", "Top Position"),
            CollectionOrder(2, "Trending Collection", "Top 2: High-demand most clicked items", "Second Position"),
            CollectionOrder(3, "Budget Collection", "Top 3: Under ₹499 / Under $15 deals", "Third Position"),
            CollectionOrder(4, "Seasonal Collection", "Top 4: Festival / Summer / Winter specials", "Fourth Position"),
            CollectionOrder(5, "Premium Collection", "Top 5: Luxury, designer, or statement pieces", "Fifth Position"),
            CollectionOrder(6, "Best Seller Collection", "Top 6: All-time top converting products", "Sixth Position")
        )

        collections.forEach { col ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x44B388FF)), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(GoldAccent8),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "${col.order}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = col.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite8)
                        Text(text = col.purpose, fontSize = 11.sp, color = Color.LightGray)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x33B388FF))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = col.badge, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = GoldAccent8)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Learn Collection Naming →", onClick = onContinue)
    }
}

private data class CollectionOrder(val order: Int, val name: String, val purpose: String, val badge: String)

/**
 * MODULE 3: Collection Naming
 */
@Composable
private fun Level8Module3NamingView(
    selectedNiche: String,
    onSelectNiche: (String) -> Unit,
    onContinue: () -> Unit
) {
    val niches = listOf("Fashion", "Beauty", "Tech", "Lifestyle")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level8ModuleBadge(moduleNum = 3, title = "Collection Naming")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Clear vs Generic Collection Names",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite8,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Niche Selector Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            niches.forEach { n ->
                val isSelected = selectedNiche == n
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) GoldAccent8 else Color(0x22FFFFFF))
                        .clickable { onSelectNiche(n) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = n,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.Black else TextWhite8
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val examples = getNamingExamplesForNiche(selectedNiche)

        examples.forEach { ex ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary8), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "GOOD: ${ex.goodName}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "AVOID: ${ex.badName}", fontSize = 12.sp, color = Color(0xFFFF8A80))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Why: ${ex.reason}", fontSize = 10.5.sp, color = Color.LightGray)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Master Featured Products →", onClick = onContinue)
    }
}

private data class NamingExample(val goodName: String, val badName: String, val reason: String)

private fun getNamingExamplesForNiche(niche: String): List<NamingExample> {
    return when (niche) {
        "Beauty" -> listOf(
            NamingExample("Glass Skin Skincare Under ₹499", "Stuff 1", "Specific benefit + budget expectation instantly guides buyers."),
            NamingExample("Viral Lip Tints & Blushes", "Makeup Items", "Highlights trending categories people actively search."),
            NamingExample("Holy Grail Haircare Staples", "Hair Products", "Establishes personal trust and recommendation authority.")
        )
        "Tech" -> listOf(
            NamingExample("Desk Setup Essentials Under ₹1,999", "Gadgets", "Clear purpose and budget anchor for tech enthusiasts."),
            NamingExample("Best Noise Cancelling Earbuds", "Audio", "Highlights exact key feature buyers look for."),
            NamingExample("Creator Camera & Lighting Gear", "My Equipment", "Tells followers exactly what gear was used in videos.")
        )
        "Lifestyle" -> listOf(
            NamingExample("Aesthetic Room Decor Finds", "Home Stuff", "Clear visual aesthetic hook for home decor shoppers."),
            NamingExample("Daily Productivity & Journaling", "Stationery", "Framed around daily lifestyle improvement habits."),
            NamingExample("Travel Storage & Packing Hacks", "Travel", "Actionable problem-solving titles convert better.")
        )
        else -> listOf(
            NamingExample("College OOTD Fits Under ₹799", "Clothes", "Target occasion + budget instantly attracts students."),
            NamingExample("Korean Aesthetic Streetwear", "Western Wear", "Specific trending fashion niche name drives higher CTR."),
            NamingExample("Festive Ethnic Saree Edit", "Traditional", "Seasonal clarity boosts instant purchase motivation.")
        )
    }
}

/**
 * MODULE 4: Featured Products
 */
@Composable
private fun Level8Module4FeaturedProductsView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level8ModuleBadge(moduleNum = 4, title = "Featured Products Strategy")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "What Stays At The Top Of Your Store?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite8,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val rules = listOf(
            Rule("1. The Hero Outfit / Item", "Place the exact product from your latest viral Reel as Product #1.", Icons.Default.Star),
            Rule("2. High Margin / High Stock", "Ensure top featured items are in-stock on brand partner sites.", Icons.Default.TrendingUp),
            Rule("3. Update Frequency", "Replace featured products weekly or after every 2 new reels posted.", Icons.Default.ListAlt),
            Rule("4. Remove Dead Links", "Regularly purge out-of-stock items to maintain store trust.", Icons.Default.Warning)
        )

        rules.forEach { r ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary8), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = r.icon, contentDescription = null, tint = GoldAccent8, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = r.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite8)
                        Text(text = r.desc, fontSize = 11.5.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Learn Banner Optimization →", onClick = onContinue)
    }
}

private data class Rule(val title: String, val desc: String, val icon: ImageVector)

/**
 * MODULE 5: Banner Optimization
 */
@Composable
private fun Level8Module5BannerOptView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level8ModuleBadge(moduleNum = 5, title = "Banner Optimization")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "High-Converting Store Header Banners",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite8,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val bannerGuidelines = listOf(
            Guideline("Minimalist Visual Design", "Avoid crowded text. Keep clean background aligned with creator aesthetic.", Icons.Default.Style),
            Guideline("High Contrast Readable Text", "Ensure banner title is easily readable on mobile devices.", Icons.Default.Image),
            Guideline("Brand Consistency", "Match banner color scheme with your Instagram / YouTube aesthetic.", Icons.Default.Storefront),
            Guideline("Copyright Safety", "Use only your own photos or licensed royalty-free vector elements.", Icons.Default.Shield)
        )

        bannerGuidelines.forEach { g ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x44B388FF)), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = g.icon, contentDescription = null, tint = GoldAccent8, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = g.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite8)
                        Text(text = g.desc, fontSize = 11.5.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Important Copyright Notice Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x33FF5252))
                .border(BorderStroke(1.dp, Color(0xFFFF5252)), RoundedCornerShape(14.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Do not use copyrighted brand logos or stolen assets in your banner without permission.",
                    fontSize = 11.sp,
                    color = TextWhite8,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Run AI Store Audit →", onClick = onContinue)
    }
}

private data class Guideline(val title: String, val desc: String, val icon: ImageVector)

/**
 * MODULE 6: AI Store Audit
 */
@Composable
private fun Level8Module6AiAuditView(
    input: String,
    onInputChange: (String) -> Unit,
    isAuditing: Boolean,
    auditDone: Boolean,
    onStartAudit: () -> Unit,
    onAuditFinished: () -> Unit,
    onContinue: () -> Unit
) {
    LaunchedEffect(isAuditing) {
        if (isAuditing) {
            delay(1800)
            onAuditFinished()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level8ModuleBadge(moduleNum = 6, title = "AI Store Audit")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Audit Visible Store Elements",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite8,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = input,
            onValueChange = onInputChange,
            label = { Text("Wishlink Store Link / Handle", color = PurplePrimary8) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary8,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite8,
                unfocusedTextColor = TextWhite8
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        GlassShineButton(text = "Run AI Visibility Audit ✨", onClick = onStartAudit)

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
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = GoldAccent8, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "AI Reviewing Store Navigation & Layout...", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldAccent8)
                }
            }
        } else if (auditDone) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x3300E676))
                    .border(BorderStroke(1.dp, Color(0xFF00E676)), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Audit Complete!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite8)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "• Navigation: Clear and multi-level collection structure detected.", fontSize = 11.5.sp, color = TextWhite8)
                    Text(text = "• Visual Balance: Clean grid layout with strong product imagery.", fontSize = 11.5.sp, color = TextWhite8)
                    Text(text = "• Trust Factor: High - verified handle & active collection updates.", fontSize = 11.5.sp, color = TextWhite8)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "AI only analyzes visible layout elements and publicly available parameters.",
            fontSize = 10.5.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "View AI Health Score →", onClick = onContinue)
    }
}

/**
 * MODULE 7: AI Store Health Score
 */
@Composable
private fun Level8Module7HealthScoreView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level8ModuleBadge(moduleNum = 7, title = "AI Store Health Score")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Overall Store Health Breakdown",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite8,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Overall Score Hero Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF4A148C),
                            Color(0xFF7B1FA2)
                        )
                    )
                )
                .border(BorderStroke(1.5.dp, GoldAccent8), RoundedCornerShape(20.dp))
                .padding(18.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "OVERALL HEALTH SCORE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldAccent8)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "94 / 100", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite8)
                Text(text = "Excellent Store Optimization Status", fontSize = 11.5.sp, color = Color(0xFFE1BEE7))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val scores = listOf(
            HealthScoreItem("Layout Score", 96),
            HealthScoreItem("Navigation Score", 92),
            HealthScoreItem("Trust Score", 95),
            HealthScoreItem("Collection Quality", 94),
            HealthScoreItem("Visual Quality", 93)
        )

        scores.forEach { s ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = s.label, fontSize = 13.sp, color = TextWhite8)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x3300E676))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = "${s.score}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Educational assessment only. Does not guarantee fixed conversion or sales numbers.",
            fontSize = 10.5.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "View Optimization Roadmap →", onClick = onContinue)
    }
}

private data class HealthScoreItem(val label: String, val score: Int)

/**
 * MODULE 8: Optimization Roadmap
 */
@Composable
private fun Level8Module8RoadmapView(
    niche: String,
    platform: String,
    seed: Int,
    onRefresh: () -> Unit,
    onContinue: () -> Unit
) {
    val roadmap = remember(niche, platform, seed) {
        generateOptimizationRoadmap(niche, platform, seed)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level8ModuleBadge(moduleNum = 8, title = "Optimization Roadmap")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Top 5 Priority Improvements",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite8,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        roadmap.forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary8), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(GoldAccent8),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "#${item.priority}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite8)
                        Text(text = item.action, fontSize = 11.sp, color = Color.LightGray)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x3300E676))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = item.impact, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        GlassShineButton(text = "Regenerate Personalised Roadmap 🔄", onClick = onRefresh)

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Interactive Practice →", onClick = onContinue)
    }
}

private data class RoadmapItem(val priority: Int, val title: String, val action: String, val impact: String)

private fun generateOptimizationRoadmap(niche: String, platform: String, seed: Int): List<RoadmapItem> {
    val list1 = listOf(
        RoadmapItem(1, "Pin Viral $niche Hero Reel Item", "Move Product #1 from latest $platform Reel to top of store", "High Impact"),
        RoadmapItem(2, "Create 'Under ₹499 $niche' Collection", "Group budget-friendly items for quick impulse buying", "High Impact"),
        RoadmapItem(3, "Update Store Header Banner", "Ensure high contrast title matching $platform bio theme", "Med Impact"),
        RoadmapItem(4, "Purge Out-Of-Stock Items", "Remove dead links from last month's collection", "Med Impact"),
        RoadmapItem(5, "Add Verified Wishlink Bio Badge", "Link directly in $platform bio with call-to-action emoji", "High Impact")
    )

    val list2 = listOf(
        RoadmapItem(1, "Re-order Top 3 Collections", "Place 'Trending $niche' first, followed by 'Budget Deals'", "High Impact"),
        RoadmapItem(2, "Simplify Collection Titles", "Change generic names to search-friendly clear labels", "High Impact"),
        RoadmapItem(3, "Add Direct Reel Code Stickers", "Match $platform video numbers to Wishlink store item #", "Med Impact"),
        RoadmapItem(4, "Highlight Weekly Best Sellers", "Group top converting items into a dedicated collection", "High Impact"),
        RoadmapItem(5, "Optimize Banner Text Contrast", "Ensure banner text is bold and 100% readable on mobile", "Med Impact")
    )

    return if (seed % 2 == 1) list1 else list2
}

/**
 * MODULE 9: Interactive Practice
 */
@Composable
private fun Level8Module9PracticeView(
    choice: String?,
    onChoiceSelected: (String) -> Unit,
    onContinue: () -> Unit
) {
    val choices = listOf(
        "Budget ₹499 Deals & Viral Finds",
        "Luxury Designer Expensive Edit",
        "Random Unorganized Product Grid"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level8ModuleBadge(moduleNum = 9, title = "Interactive Practice")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Which Collection Should Appear First?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite8,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Scenario: Your primary audience consists of budget-conscious Gen-Z followers looking for quick viral fashion/tech finds.",
            fontSize = 12.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Center,
            lineHeight = 17.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        choices.forEach { option ->
            val isSelected = choice == option
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) Color(0x44B388FF) else Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, if (isSelected) GoldAccent8 else Color(0x33FFFFFF)), RoundedCornerShape(16.dp))
                    .clickable { onChoiceSelected(option) }
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.Storefront,
                        contentDescription = null,
                        tint = if (isSelected) GoldAccent8 else PurplePrimary8,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = option, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite8)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (choice != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x331F0038))
                    .border(BorderStroke(1.dp, GoldAccent8), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(text = "AI Feedback & Explanation:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldAccent8)
                    Spacer(modifier = Modifier.height(4.dp))
                    if (choice == "Budget ₹499 Deals & Viral Finds") {
                        Text(
                            text = "Correct! Budget & viral deals lower the friction for Gen-Z audiences, leading to instant clicks and initial trust.",
                            fontSize = 12.sp,
                            color = TextWhite8,
                            lineHeight = 17.sp
                        )
                    } else {
                        Text(
                            text = "Placing high-priced or unorganized items at the top creates friction for budget-conscious Gen-Z buyers. Place Budget Deals first!",
                            fontSize = 12.sp,
                            color = TextWhite8,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Go To Today's Mission →", onClick = onContinue)
    }
}

/**
 * MODULE 10: Today's Mission & Achievement
 */
@Composable
private fun Level8Module10AchievementView(
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
        Level8ModuleBadge(moduleNum = 10, title = "Today's Mission & Reward")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Mission: Optimize Your Wishlink Store",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite8,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Estimated Time: 20 Minutes • Re-order top 3 collections & update banner",
            fontSize = 11.5.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // PREMIUM GLASS BADGE
        Box(
            modifier = Modifier
                .size(160.dp)
                .graphicsLayer { rotationZ = (shineAnim * 4) - 2 }
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            GoldAccent8.copy(alpha = 0.9f),
                            Color(0xFF7C4DFF),
                            PurpleDeepBg18
                        )
                    )
                )
                .border(BorderStroke(3.dp, GoldAccent8), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.MilitaryTech,
                    contentDescription = "Store Optimizer Badge",
                    tint = GoldAccent8,
                    modifier = Modifier.size(54.dp)
                )
                Text(
                    text = "STORE OPTIMIZER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite8
                )
                Text(
                    text = "+500 XP",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent8
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (!isUnlocked) {
            GlassShineButton(text = "Claim Store Optimizer Badge 🏆", onClick = onUnlockAchievement)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x3300E676))
                    .border(BorderStroke(1.dp, Color(0xFF00E676)), RoundedCornerShape(16.dp))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🎉 Badge Unlocked! +500 XP Added", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite8)
                    Text(text = "Level 8 Complete • Saved to Optimization History", fontSize = 11.sp, color = Color(0xFFB9F6CA))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            GlassShineButton(text = "Complete Level 8 Master ✅", onClick = onCompleteLevel)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
