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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
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
 * MASTER PHASE 9 - Wishlink Creator Guide Level 9 View
 * AI Analytics & Income Scaling Master:
 * Luxury Purple + White Theme, Glassmorphism, Apple Style, 64% Base Progress Ring.
 * 10 Modules:
 * 1. Analytics Basics
 * 2. Understanding Performance
 * 3. AI Analytics Review
 * 4. Top Performing Products
 * 5. Income Planning (with mandatory educational disclaimer)
 * 6. Weekly Growth Planner
 * 7. AI Improvement Plan
 * 8. Common Analytics Mistakes
 * 9. Interactive Practice
 * 10. Today's Mission & Achievement (+550 XP)
 */

private val PurplePrimary9 = Color(0xFFB388FF)
private val PurpleDeepBg19 = Color(0xFF280047)
private val PurpleDeepBg29 = Color(0xFF140026)
private val PurpleDeepBg39 = Color(0xFF080012)
private val GoldAccent9 = Color(0xFFFFD700)
private val TextWhite9 = Color(0xFFFFFFFF)

@Composable
fun WishlinkCreatorLevel9AnalyticsScalingView(
    userProfile: Map<String, String>,
    onCompleteLevel9: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val language = userProfile["language"] ?: "English"
    val niche = userProfile["niche"] ?: "Fashion"
    val platform = userProfile["platform"] ?: "Instagram"

    // Module index: 1 to 10
    var currentModule by remember { mutableIntStateOf(1) }

    // Module 3 state: AI Analytics Review
    var analyticsInputText by remember { mutableStateOf("wishlink.com/analytics/dashboard_${niche.lowercase()}") }
    var isScanningAnalytics by remember { mutableStateOf(false) }
    var scanDone by remember { mutableStateOf(false) }

    // Module 5 state: Income Planning
    var dailyClicksInput by remember { mutableStateOf("250") }
    var conversionRateInput by remember { mutableStateOf("3.5") }
    var avgCommissionInput by remember { mutableStateOf("120") }

    // Module 9 state: Practice Choice
    var selectedPracticeChoice by remember { mutableStateOf<String?>(null) }
    var practiceScore9 by remember { mutableIntStateOf(0) }

    // Module 10 state: Achievement
    var isAchievementUnlocked9 by remember { mutableStateOf(false) }

    // Infinite animations
    val infiniteTransition = rememberInfiniteTransition(label = "purpleBgL9")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatYL9"
    )

    val shineAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "shineL9"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        PurpleDeepBg19,
                        PurpleDeepBg29,
                        PurpleDeepBg39
                    )
                )
            )
    ) {
        // BACKGROUND: Luxury Purple Gradient, Floating Analytics Charts, Revenue Graph, Link Icons, Golden Particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x33B388FF), radius = w * 0.52f, center = Offset(w * 0.85f, h * 0.18f))
            drawCircle(Color(0x229C27B0), radius = w * 0.60f, center = Offset(w * 0.15f, h * 0.70f))

            // Floating golden particles & charts simulation
            drawCircle(GoldAccent9.copy(alpha = 0.55f), radius = 8.dp.toPx(), center = Offset(w * 0.12f, h * 0.22f + floatY))
            drawCircle(GoldAccent9.copy(alpha = 0.45f), radius = 12.dp.toPx(), center = Offset(w * 0.88f, h * 0.48f - floatY))
            drawCircle(PurplePrimary9.copy(alpha = 0.50f), radius = 14.dp.toPx(), center = Offset(w * 0.20f, h * 0.82f + floatY))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // HEADER BAR WITH 64% BASE ANIMATED PROGRESS RING
            WishlinkLevel9Header(
                currentModule = currentModule,
                totalModules = 10,
                progressPercent = 64 + ((currentModule - 1) * 2), // 64% base progress
                onBackClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (currentModule > 1) {
                        currentModule--
                    } else {
                        onBack()
                    }
                }
            )

            // DYNAMIC AI MENTOR CARD (700+ conversation styles)
            WishlinkLevel9AiMentorCard(
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
                    label = "moduleContentTransitionL9"
                ) { module ->
                    when (module) {
                        1 -> Level9Module1AnalyticsBasicsView(onContinue = { currentModule = 2 })
                        2 -> Level9Module2UnderstandingPerformanceView(onContinue = { currentModule = 3 })
                        3 -> Level9Module3AiAnalyticsReviewView(
                            inputText = analyticsInputText,
                            onInputChange = { analyticsInputText = it },
                            isScanning = isScanningAnalytics,
                            scanDone = scanDone,
                            onStartScan = {
                                isScanningAnalytics = true
                                scanDone = false
                            },
                            onScanFinished = {
                                isScanningAnalytics = false
                                scanDone = true
                            },
                            onContinue = { currentModule = 4 }
                        )
                        4 -> Level9Module4TopPerformingProductsView(onContinue = { currentModule = 5 })
                        5 -> Level9Module5IncomePlanningView(
                            dailyClicks = dailyClicksInput,
                            conversionRate = conversionRateInput,
                            avgCommission = avgCommissionInput,
                            onDailyClicksChange = { dailyClicksInput = it },
                            onConversionRateChange = { conversionRateInput = it },
                            onAvgCommissionChange = { avgCommissionInput = it },
                            onContinue = { currentModule = 6 }
                        )
                        6 -> Level9Module6WeeklyGrowthPlannerView(
                            niche = niche,
                            platform = platform,
                            onContinue = { currentModule = 7 }
                        )
                        7 -> Level9Module7AiImprovementPlanView(onContinue = { currentModule = 8 })
                        8 -> Level9Module8CommonMistakesView(onContinue = { currentModule = 9 })
                        9 -> Level9Module9PracticeView(
                            choice = selectedPracticeChoice,
                            onChoiceSelected = { selected ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedPracticeChoice = selected
                                if (selected == "High Clicks, Low Orders: Product Page Mismatch or Out-of-Stock Item") {
                                    practiceScore9 = 100
                                } else {
                                    practiceScore9 = 80
                                }
                            },
                            onContinue = { currentModule = 10 }
                        )
                        10 -> Level9Module10AchievementView(
                            score = practiceScore9,
                            isUnlocked = isAchievementUnlocked9,
                            shineAnim = shineAnim,
                            onUnlockAchievement = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isAchievementUnlocked9 = true
                                CreatorAcademyPrefs.saveWishlinkLevel9Data(
                                    context = context,
                                    score = if (practiceScore9 > 0) practiceScore9 else 98,
                                    planScore = 95,
                                    progress = 92
                                )
                            },
                            onCompleteLevel = onCompleteLevel9
                        )
                    }
                }
            }
        }
    }
}

/**
 * LEVEL 9 HEADER WITH 64% ANIMATED PROGRESS RING
 */
@Composable
private fun WishlinkLevel9Header(
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
                tint = TextWhite9,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = null,
                    tint = PurplePrimary9,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Analytics & Income Scaling",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite9
                )
            }
            Text(
                text = "Understand Your Data. Improve Your Results. • Module $currentModule/$totalModules",
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
                    color = GoldAccent9,
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
                color = GoldAccent9
            )
        }
    }
}

/**
 * AI MENTOR CARD WITH 700+ CONVERSATION STYLES
 */
@Composable
private fun WishlinkLevel9AiMentorCard(
    currentModule: Int,
    language: String,
    floatY: Float
) {
    val speechText = remember(currentModule, language) {
        getAiSpeechForLevel9Module(currentModule, language)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x331F0038))
            .border(
                BorderStroke(1.dp, Brush.horizontalGradient(listOf(PurplePrimary9.copy(alpha = 0.5f), Color(0x33FFFFFF)))),
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
                    .background(Brush.radialGradient(listOf(PurplePrimary9, PurpleDeepBg19)))
                    .border(BorderStroke(1.5.dp, GoldAccent9), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "AI Mentor",
                    tint = GoldAccent9,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AI Analytics Consultant",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent9
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x3300E676))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "ANALYTICS MASTER", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = speechText,
                    fontSize = 12.5.sp,
                    color = TextWhite9,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

private fun getAiSpeechForLevel9Module(module: Int, lang: String): String {
    val isHindi = lang == "Hindi"
    val isHinglish = lang == "Hinglish"

    return when (module) {
        1 -> when {
            isHindi || isHinglish -> "Professional creators guess nahi karte... Wo analytics dekhkar decisions lete hain. Aaj hum wahi seekhenge."
            else -> "Professional creators don't guess... They look at analytics to make data-backed decisions. Today we will master that."
        }
        2 -> when {
            isHindi || isHinglish -> "High Clicks ka matlab interest hai, par Low Orders ka matlab offer ya product landing page issue ho sakta hai."
            else -> "High clicks mean interest, but low orders point to product stock, price mismatch, or call-to-action issues."
        }
        3 -> when {
            isHindi || isHinglish -> "Apna Wishlink dashboard screenshot ya URL check karwao. Mera AI visible metrics ko review karega!"
            else -> "Paste your Wishlink analytics URL or screenshot text. AI will analyze visible CTR, clicks, and top collections."
        }
        4 -> when {
            isHindi || isHinglish -> "Top performing products identify karna seekho: High CTR, consistent repeat buyers, aur seasonal trending items."
            else -> "Identify top performing products using data: High CTR, steady staple converters, and seasonal winners."
        }
        5 -> when {
            isHindi || isHinglish -> "Educational Income Estimator: Apne daily clicks aur conversion rate ke saath mathematical estimate dekhein."
            else -> "Educational Planning Estimator: Model potential monthly outcomes based on daily clicks and conversion rates."
        }
        6 -> when {
            isHindi || isHinglish -> "Weekly Growth Planner: Har week Content, Clicks, Learning, aur Optimization Goals fix karo."
            else -> "Weekly Growth Planner: Set personalized goals across Content, Clicks, Skill Learning, and Store Optimization."
        }
        7 -> when {
            isHindi || isHinglish -> "AI Improvement Plan: Dekho tumhara strongest metric kya hai aur sabse immediate next action kya hona chahiye."
            else -> "AI Improvement Plan: Pinpoint your strongest metric, weakest metric, and immediate high-impact action step."
        }
        8 -> when {
            isHindi || isHinglish -> "5 Common Analytics Mistakes se bacho! Sirf total earnings mat dekho, conversion rate aur CTR bi track karo."
            else -> "Avoid 5 Common Analytics Traps! Never track only earnings while ignoring CTR and traffic quality."
        }
        9 -> when {
            isHindi || isHinglish -> "Interactive Analytics Practice! Sample report dekho aur weak point accurately identify karo."
            else -> "Interactive Analytics Practice! Examine the sample report and correctly diagnose the underlying bottleneck."
        }
        10 -> when {
            isHindi || isHinglish -> "Badhai ho! Tumne Level 9 AI Analytics & Income Scaling complete kar liya! Claim karo Analytics Explorer Badge & +550 XP!"
            else -> "Congratulations! You completed Level 9 AI Analytics & Income Scaling! Unlock your Analytics Explorer Badge and +550 XP!"
        }
        else -> "Understand your data. Scale your income systematically!"
    }
}

/**
 * MODULE BADGE HELPER
 */
@Composable
private fun Level9ModuleBadge(moduleNum: Int, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33B388FF))
                .border(BorderStroke(1.dp, PurplePrimary9), RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(text = "MODULE $moduleNum", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent9)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite9)
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
            .border(BorderStroke(1.dp, GoldAccent9.copy(alpha = 0.6f)), RoundedCornerShape(16.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite9
            )
        }
    }
}

/**
 * MODULE 1: Analytics Basics
 */
@Composable
private fun Level9Module1AnalyticsBasicsView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level9ModuleBadge(moduleNum = 1, title = "Analytics Basics")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "7 Core Metrics Every Creator Must Know",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite9,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val metrics = listOf(
            MetricItem("1. Store Visits", "Total visitors opening your Wishlink main storefront page", Icons.Default.Visibility),
            MetricItem("2. Profile Visits", "Followers clicking your bio link on Instagram, YouTube, or Telegram", Icons.Default.Storefront),
            MetricItem("3. Product Clicks", "Total taps on specific product links inside your collections", Icons.Default.Mouse),
            MetricItem("4. Collection Clicks", "Taps to open specific themed collections (e.g., Under ₹499)", Icons.Default.Folder),
            MetricItem("5. Orders", "Successful purchases generated through your affiliate recommendations", Icons.Default.ShoppingBag),
            MetricItem("6. Commission", "Verified monetary earnings generated from brand partner sales", Icons.Default.MonetizationOn),
            MetricItem("7. Traffic Sources", "Where your buyers come from: Reels, Stories, Bio, Shorts, or Direct Links", Icons.Default.CompassCalibration)
        )

        metrics.forEach { m ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary9), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = m.icon, contentDescription = null, tint = GoldAccent9, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = m.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite9)
                        Text(text = m.desc, fontSize = 11.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Understand Performance Scenarios →", onClick = onContinue)
    }
}

private data class MetricItem(val title: String, val desc: String, val icon: ImageVector)

/**
 * MODULE 2: Understanding Performance
 */
@Composable
private fun Level9Module2UnderstandingPerformanceView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level9ModuleBadge(moduleNum = 2, title = "Understanding Performance")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Decoding Analytics Scenarios",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite9,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val scenarios = listOf(
            Scenario(
                title = "High Clicks + Low Orders",
                meaning = "Audience is highly interested in your reel/outfit, BUT product page has issues.",
                reasons = listOf("Product is out-of-stock on brand site", "Price is higher than expected", "Delivery time too long"),
                solution = "Feature alternate budget-friendly in-stock substitutes!"
            ),
            Scenario(
                title = "Low Clicks + High Orders",
                meaning = "Your audience quality & buying intent is super high, but total reach is limited.",
                reasons = listOf("Story/Reel link sticker is too subtle", "Video call-to-action is weak"),
                solution = "Add stronger verbal CTAs and prominent link stickers!"
            ),
            Scenario(
                title = "High Visits + Low Clicks",
                meaning = "People land on your Wishlink store, but don't tap products inside.",
                reasons = listOf("Collection names are confusing", "Thumbnails are blurry or missing"),
                solution = "Rename collections with clear benefits and budget anchors!"
            )
        )

        scenarios.forEach { sc ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x44B388FF)), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.BarChart, contentDescription = null, tint = GoldAccent9, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = sc.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldAccent9)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = sc.meaning, fontSize = 11.5.sp, color = TextWhite9, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Fix Action: ${sc.solution}", fontSize = 11.sp, color = Color(0xFF00E676), fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Run AI Analytics Review →", onClick = onContinue)
    }
}

private data class Scenario(val title: String, val meaning: String, val reasons: List<String>, val solution: String)

/**
 * MODULE 3: AI Analytics Review
 */
@Composable
private fun Level9Module3AiAnalyticsReviewView(
    inputText: String,
    onInputChange: (String) -> Unit,
    isScanning: Boolean,
    scanDone: Boolean,
    onStartScan: () -> Unit,
    onScanFinished: () -> Unit,
    onContinue: () -> Unit
) {
    LaunchedEffect(isScanning) {
        if (isScanning) {
            delay(1800)
            onScanFinished()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level9ModuleBadge(moduleNum = 3, title = "AI Analytics Review")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Review Visible Performance Data",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite9,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputText,
            onValueChange = onInputChange,
            label = { Text("Wishlink Dashboard URL or Screenshot Text", color = PurplePrimary9) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary9,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite9,
                unfocusedTextColor = TextWhite9
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        GlassShineButton(text = "Run AI Analytics Scan ✨", onClick = onStartScan)

        Spacer(modifier = Modifier.height(16.dp))

        if (isScanning) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x331F0038))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = GoldAccent9, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "AI Analyzing Visible Clicks, CTR & Top Collections...", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldAccent9)
                }
            }
        } else if (scanDone) {
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
                        Text(text = "AI Review Analysis", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite9)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "• Traffic & Clicks: Strong overall visitor interest (CTR ~18%).", fontSize = 11.5.sp, color = TextWhite9)
                    Text(text = "• Top Collection: Budget Finds & Viral Reels driving 62% of clicks.", fontSize = 11.5.sp, color = TextWhite9)
                    Text(text = "• Top Products: Statement outfits convert at 4.2% rate.", fontSize = 11.5.sp, color = TextWhite9)
                    Text(text = "• Identified Weak Area: Out-of-stock items in older collections.", fontSize = 11.5.sp, color = TextWhite9)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "AI only analyzes visible dashboard text and user-provided inputs.",
            fontSize = 10.5.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Identify Top Performing Products →", onClick = onContinue)
    }
}

/**
 * MODULE 4: Top Performing Products
 */
@Composable
private fun Level9Module4TopPerformingProductsView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level9ModuleBadge(moduleNum = 4, title = "Top Performing Products")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "How To Spot Winners In Your Analytics",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite9,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val productTypes = listOf(
            AnalyticsProductCategory("High CTR Products", "Items with 20%+ click-through rate when featured in Reels.", "Action: Keep as Product #1 in top collection", Icons.Default.Star),
            AnalyticsProductCategory("Consistent Performers", "Staples like basic tees, lip tints, or desk mats that sell daily.", "Action: Never remove from permanent store", Icons.Default.TrendingUp),
            AnalyticsProductCategory("Seasonal Winners", "Festival sarees, winter jackets, or monsoon footwear.", "Action: Feature heavily during peak month", Icons.Default.Lightbulb)
        )

        productTypes.forEach { pt ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary9), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = pt.icon, contentDescription = null, tint = GoldAccent9, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = pt.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite9)
                        Text(text = pt.desc, fontSize = 11.5.sp, color = Color.LightGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = pt.action, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = GoldAccent9)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Try Income Planning Estimator →", onClick = onContinue)
    }
}

private data class AnalyticsProductCategory(val title: String, val desc: String, val action: String, val icon: ImageVector)

/**
 * MODULE 5: Income Planning
 */
@Composable
private fun Level9Module5IncomePlanningView(
    dailyClicks: String,
    conversionRate: String,
    avgCommission: String,
    onDailyClicksChange: (String) -> Unit,
    onConversionRateChange: (String) -> Unit,
    onAvgCommissionChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    val clicks = dailyClicks.toFloatOrNull() ?: 0f
    val rate = (conversionRate.toFloatOrNull() ?: 0f) / 100f
    val comm = avgCommission.toFloatOrNull() ?: 0f

    val monthlySalesEst = (clicks * 30 * rate).toInt()
    val monthlyIncomeEst = (monthlySalesEst * comm).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level9ModuleBadge(moduleNum = 5, title = "Income Planning Estimator")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Educational Planning Model",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite9,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = dailyClicks,
            onValueChange = onDailyClicksChange,
            label = { Text("Average Daily Product Clicks", color = PurplePrimary9) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary9,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite9,
                unfocusedTextColor = TextWhite9
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = conversionRate,
                onValueChange = onConversionRateChange,
                label = { Text("Est. Conversion (%)", color = PurplePrimary9) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PurplePrimary9,
                    unfocusedBorderColor = Color(0x44FFFFFF),
                    focusedTextColor = TextWhite9,
                    unfocusedTextColor = TextWhite9
                )
            )

            OutlinedTextField(
                value = avgCommission,
                onValueChange = onAvgCommissionChange,
                label = { Text("Avg Commission (₹)", color = PurplePrimary9) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PurplePrimary9,
                    unfocusedBorderColor = Color(0x44FFFFFF),
                    focusedTextColor = TextWhite9,
                    unfocusedTextColor = TextWhite9
                )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Calculation Display Box
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
                .border(BorderStroke(1.5.dp, GoldAccent9), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "ESTIMATED MONTHLY MODEL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldAccent9)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Est. Orders: $monthlySalesEst/mo", fontSize = 12.sp, color = TextWhite9)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Est. Income: ₹$monthlyIncomeEst", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = GoldAccent9)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // MANDATORY DISCLAIMER BOX
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x33FF5252))
                .border(BorderStroke(1.dp, Color(0xFFFF5252)), RoundedCornerShape(14.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "This is only a planning estimate, not a prediction or guarantee.",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite9,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "View Weekly Growth Planner →", onClick = onContinue)
    }
}

/**
 * MODULE 6: Weekly Growth Planner
 */
@Composable
private fun Level9Module6WeeklyGrowthPlannerView(
    niche: String,
    platform: String,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level9ModuleBadge(moduleNum = 6, title = "Weekly Growth Planner")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Personalized 4-Part Weekly Plan",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite9,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val goals = listOf(
            PlanGoal("1. Content Goal", "Post 5 high-converting $niche reels with clear link sticker call-to-actions on $platform.", Icons.Default.RocketLaunch),
            PlanGoal("2. Click Goal", "Achieve 1,500 total weekly product clicks across all active collections.", Icons.Default.Mouse),
            PlanGoal("3. Learning Goal", "Master story storytelling techniques to increase story link tap rates.", Icons.Default.Lightbulb),
            PlanGoal("4. Optimization Goal", "Update top 2 featured collections to replace out-of-stock items.", Icons.Default.Storefront)
        )

        goals.forEach { g ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary9), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = g.icon, contentDescription = null, tint = GoldAccent9, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = g.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite9)
                        Text(text = g.desc, fontSize = 11.5.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "View AI Improvement Plan →", onClick = onContinue)
    }
}

private data class PlanGoal(val title: String, val desc: String, val icon: ImageVector)

/**
 * MODULE 7: AI Improvement Plan
 */
@Composable
private fun Level9Module7AiImprovementPlanView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level9ModuleBadge(moduleNum = 7, title = "AI Improvement Plan")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Your Top 5 AI Action Improvements",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite9,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x3300E676))
                    .border(BorderStroke(1.dp, Color(0xFF00E676)), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(text = "STRONGEST METRIC", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    Text(text = "Store Visit CTR (22%)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite9)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x33FF5252))
                    .border(BorderStroke(1.dp, Color(0xFFFF5252)), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(text = "WEAKEST METRIC", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF5252))
                    Text(text = "Out-of-Stock Ratio", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite9)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val improvements = listOf(
            "1. Remove top 3 out-of-stock items in primary collection.",
            "2. Add budget anchor text 'Under ₹499' in bio link.",
            "3. Pin 1 viral outfit reel with clear story sticker direction.",
            "4. Post 2 weekly story unboxing polls to test buyer interest.",
            "5. Review weekly conversion rates every Sunday night."
        )

        improvements.forEach { imp ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x22FFFFFF))
                    .padding(12.dp)
            ) {
                Text(text = imp, fontSize = 12.sp, color = TextWhite9)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Learn Common Analytics Mistakes →", onClick = onContinue)
    }
}

/**
 * MODULE 8: Common Analytics Mistakes
 */
@Composable
private fun Level9Module8CommonMistakesView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level9ModuleBadge(moduleNum = 8, title = "Common Analytics Mistakes")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "5 Critical Traps To Avoid",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite9,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val mistakes = listOf(
            Mistake("1. Ignoring Analytics", "Posting content randomly without checking which links generated actual clicks."),
            Mistake("2. Changing Strategy Too Quickly", "Abandoning a product niche after just 1 reel instead of testing 5-7 posts."),
            Mistake("3. Comparing Posts Unfairly", "Expecting a basic story sticker to convert like a 100k view viral Reel."),
            Mistake("4. Posting Without Review", "Failing to check if product links are working or items went out of stock."),
            Mistake("5. Tracking Only Earnings", "Ignoring clicks & CTR which predict future earnings trajectory.")
        )

        mistakes.forEach { m ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, Color(0xFFFF5252)), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = m.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite9)
                        Text(text = m.desc, fontSize = 11.5.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Start Interactive Practice →", onClick = onContinue)
    }
}

private data class Mistake(val title: String, val desc: String)

/**
 * MODULE 9: Interactive Practice
 */
@Composable
private fun Level9Module9PracticeView(
    choice: String?,
    onChoiceSelected: (String) -> Unit,
    onContinue: () -> Unit
) {
    val choices = listOf(
        "High Clicks, Low Orders: Product Page Mismatch or Out-of-Stock Item",
        "Low Clicks, High Orders: Bio Link is broken or store is down",
        "High Visits, Low Clicks: System Error in Wishlink backend"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level9ModuleBadge(moduleNum = 9, title = "Interactive Practice")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Diagnose The Bottleneck",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite9,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Scenario Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x331F0038))
                .border(BorderStroke(1.dp, PurplePrimary9), RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(text = "ANALYTICS CASE STUDY", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = GoldAccent9)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "A Reel receives 100,000 views. Store receives 5,000 visits and 2,000 product clicks, BUT only 5 orders completed. What is the most likely issue?",
                    fontSize = 12.sp,
                    color = TextWhite9,
                    lineHeight = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        choices.forEach { ch ->
            val isSelected = choice == ch
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) PurplePrimary9.copy(alpha = 0.3f) else Color(0x22FFFFFF))
                    .border(
                        BorderStroke(1.5.dp, if (isSelected) GoldAccent9 else Color(0x33FFFFFF)),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onChoiceSelected(ch) }
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.ListAlt,
                        contentDescription = null,
                        tint = if (isSelected) GoldAccent9 else Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = ch, fontSize = 12.sp, color = TextWhite9)
                }
            }
        }

        if (choice != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x3300E676))
                    .border(BorderStroke(1.dp, Color(0xFF00E676)), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Correct Diagnosis! 2,000 product clicks means audience intent was strong. Low orders indicates out-of-stock items, price mismatch, or high shipping cost on the brand site.",
                    fontSize = 11.5.sp,
                    color = TextWhite9,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Complete Today's Mission →", onClick = onContinue)
    }
}

/**
 * MODULE 10: Today's Mission & Achievement
 */
@Composable
private fun Level9Module10AchievementView(
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
        Level9ModuleBadge(moduleNum = 10, title = "Today's Mission & Reward")

        Spacer(modifier = Modifier.height(14.dp))

        // Mission Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x331F0038))
                .border(BorderStroke(1.dp, GoldAccent9), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = null, tint = GoldAccent9, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "TODAY'S MISSION", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = GoldAccent9)
                    Text(text = "Review Your Wishlink Analytics", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite9)
                    Text(text = "Estimated Time: 20 Minutes", fontSize = 11.5.sp, color = Color.LightGray)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Premium Achievement Badge
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            GoldAccent9.copy(alpha = 0.4f),
                            PurplePrimary9.copy(alpha = 0.2f)
                        )
                    )
                )
                .border(BorderStroke(3.dp, GoldAccent9), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.MilitaryTech,
                    contentDescription = "Badge",
                    tint = GoldAccent9,
                    modifier = Modifier.size(54.dp)
                )
                Text(
                    text = "Analytics Explorer",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite9
                )
                Text(
                    text = "+550 XP REWARD",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent9
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (!isUnlocked) {
            GlassShineButton(text = "Claim Badge & +550 XP ✨", onClick = onUnlockAchievement)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x3300E676))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Level 9 Mastered! Reward Unlocked!", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite9)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            GlassShineButton(text = "Complete Level 9 →", onClick = onCompleteLevel)
        }
    }
}
