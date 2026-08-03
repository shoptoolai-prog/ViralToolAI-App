package com.example.creatoracademy

import android.content.Context
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
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
 * MASTER PHASE 10 - Wishlink Creator Guide Level 10 View
 * AI Content Growth System:
 * Luxury Purple + White Theme, Glassmorphism, Apple Style, 72% Base Progress Ring.
 * 10 Modules:
 * 1. Content Pillars
 * 2. 30-Day AI Content Calendar
 * 3. Weekly Planner
 * 4. Content Batching
 * 5. Story Growth Strategy
 * 6. AI Content Planner
 * 7. Consistency Tracker
 * 8. Burnout Prevention
 * 9. Weekly Review
 * 10. Today's Mission & Achievement (+600 XP)
 */

private val PurplePrimary10 = Color(0xFFB388FF)
private val PurpleDeepBg110 = Color(0xFF24003E)
private val PurpleDeepBg210 = Color(0xFF120022)
private val PurpleDeepBg310 = Color(0xFF070010)
private val GoldAccent10 = Color(0xFFFFD700)
private val TextWhite10 = Color(0xFFFFFFFF)

@Composable
fun WishlinkCreatorLevel10ContentGrowthView(
    userProfile: Map<String, String>,
    onCompleteLevel10: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val language = userProfile["language"] ?: "English"
    val niche = userProfile["niche"] ?: "Fashion"
    val platform = userProfile["platform"] ?: "Instagram"

    // Module index: 1 to 10
    var currentModule by remember { mutableIntStateOf(1) }

    // Persistent State values
    var currentStreak by remember { mutableIntStateOf(CreatorAcademyPrefs.getWishlinkLevel10Streak(context)) }

    // Module 3 state: Weekly Planner
    var monPlan by remember { mutableStateOf("Reel: Top 3 $niche Finds") }
    var tuePlan by remember { mutableStateOf("Story: Budget Under ₹499 Poll") }
    var wedPlan by remember { mutableStateOf("Reel: How To Style / Use Item") }
    var thuPlan by remember { mutableStateOf("Story: Q&A Sticker & Wishlink Store Link") }
    var friPlan by remember { mutableStateOf("Reel: Unboxing & First Impression") }
    var satPlan by remember { mutableStateOf("Story: Weekend Steal Deals & Countdown") }
    var sunPlan by remember { mutableStateOf("Carousel / Short: Weekly Recap & Rest Day") }

    // Module 6 state: AI Content Planner
    var selectedProductInput by remember { mutableStateOf("Viral Oversized Cargo Pants") }
    var isGeneratingContentPack by remember { mutableStateOf(false) }
    var contentPackGenerated by remember { mutableStateOf(false) }

    // Module 10 state: Achievement
    var isAchievementUnlocked10 by remember { mutableStateOf(false) }

    // Infinite animations
    val infiniteTransition = rememberInfiniteTransition(label = "purpleBgL10")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -14f,
        targetValue = 14f,
        animationSpec = infiniteRepeatable(tween(2700, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatYL10"
    )

    val shineAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "shineL10"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        PurpleDeepBg110,
                        PurpleDeepBg210,
                        PurpleDeepBg310
                    )
                )
            )
    ) {
        // BACKGROUND: Luxury Purple Gradient, Floating Calendar, Content Cards, Camera Icons, Golden Particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x33B388FF), radius = w * 0.54f, center = Offset(w * 0.82f, h * 0.16f))
            drawCircle(Color(0x228E24AA), radius = w * 0.62f, center = Offset(w * 0.18f, h * 0.72f))

            // Floating camera/calendar icons simulation glow
            drawCircle(GoldAccent10.copy(alpha = 0.50f), radius = 9.dp.toPx(), center = Offset(w * 0.14f, h * 0.24f + floatY))
            drawCircle(GoldAccent10.copy(alpha = 0.40f), radius = 13.dp.toPx(), center = Offset(w * 0.86f, h * 0.52f - floatY))
            drawCircle(PurplePrimary10.copy(alpha = 0.45f), radius = 15.dp.toPx(), center = Offset(w * 0.22f, h * 0.84f + floatY))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // HEADER BAR WITH 72% BASE ANIMATED PROGRESS RING
            WishlinkLevel10Header(
                currentModule = currentModule,
                totalModules = 10,
                progressPercent = 72 + ((currentModule - 1) * 2), // 72% base progress ring
                onBackClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (currentModule > 1) {
                        currentModule--
                    } else {
                        onBack()
                    }
                }
            )

            // DYNAMIC AI MENTOR CARD (800+ conversation styles)
            WishlinkLevel10AiMentorCard(
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
                    label = "moduleContentTransitionL10"
                ) { module ->
                    when (module) {
                        1 -> Level10Module1ContentPillarsView(onContinue = { currentModule = 2 })
                        2 -> Level10Module2ContentCalendarView(
                            niche = niche,
                            platform = platform,
                            onContinue = { currentModule = 3 }
                        )
                        3 -> Level10Module3WeeklyPlannerView(
                            mon = monPlan, tue = tuePlan, wed = wedPlan, thu = thuPlan, fri = friPlan, sat = satPlan, sun = sunPlan,
                            onMonChange = { monPlan = it }, onTueChange = { tuePlan = it }, onWedChange = { wedPlan = it },
                            onThuChange = { thuPlan = it }, onFriChange = { friPlan = it }, onSatChange = { satPlan = it },
                            onSunChange = { sunPlan = it },
                            onContinue = { currentModule = 4 }
                        )
                        4 -> Level10Module4ContentBatchingView(onContinue = { currentModule = 5 })
                        5 -> Level10Module5StoryGrowthStrategyView(onContinue = { currentModule = 6 })
                        6 -> Level10Module6AiContentPlannerView(
                            productInput = selectedProductInput,
                            onProductInputChange = { selectedProductInput = it },
                            isGenerating = isGeneratingContentPack,
                            isGenerated = contentPackGenerated,
                            onStartGenerate = {
                                isGeneratingContentPack = true
                                contentPackGenerated = false
                            },
                            onGenerateFinished = {
                                isGeneratingContentPack = false
                                contentPackGenerated = true
                            },
                            niche = niche,
                            onContinue = { currentModule = 7 }
                        )
                        7 -> Level10Module7ConsistencyTrackerView(
                            streak = currentStreak,
                            onIncrementStreak = {
                                currentStreak++
                                CreatorAcademyPrefs.saveWishlinkLevel10State(context, currentStreak, "", "")
                            },
                            onContinue = { currentModule = 8 }
                        )
                        8 -> Level10Module8BurnoutPreventionView(onContinue = { currentModule = 9 })
                        9 -> Level10Module9WeeklyReviewView(
                            niche = niche,
                            platform = platform,
                            onContinue = { currentModule = 10 }
                        )
                        10 -> Level10Module10AchievementView(
                            streak = currentStreak,
                            isUnlocked = isAchievementUnlocked10,
                            shineAnim = shineAnim,
                            onUnlockAchievement = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isAchievementUnlocked10 = true
                                CreatorAcademyPrefs.saveWishlinkLevel10Data(
                                    context = context,
                                    score = 100,
                                    streak = currentStreak,
                                    progress = 100,
                                    calendarJson = "30-day-calendar",
                                    weeklyPlanJson = "mon:$monPlan|tue:$tuePlan|wed:$wedPlan"
                                )
                            },
                            onCompleteLevel = onCompleteLevel10
                        )
                    }
                }
            }
        }
    }
}

/**
 * LEVEL 10 HEADER WITH 72% ANIMATED PROGRESS RING
 */
@Composable
private fun WishlinkLevel10Header(
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
                tint = TextWhite10,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = PurplePrimary10,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AI Content Growth System",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite10
                )
            }
            Text(
                text = "Create Consistent Content That Builds Trust • Module $currentModule/$totalModules",
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
                    color = GoldAccent10,
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
                color = GoldAccent10
            )
        }
    }
}

/**
 * AI MENTOR CARD WITH 800+ CONVERSATION STYLES
 */
@Composable
private fun WishlinkLevel10AiMentorCard(
    currentModule: Int,
    language: String,
    floatY: Float
) {
    val speechText = remember(currentModule, language) {
        getAiSpeechForLevel10Module(currentModule, language)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x331D0032))
            .border(
                BorderStroke(1.dp, Brush.horizontalGradient(listOf(PurplePrimary10.copy(alpha = 0.5f), Color(0x33FFFFFF)))),
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
                    .background(Brush.radialGradient(listOf(PurplePrimary10, PurpleDeepBg110)))
                    .border(BorderStroke(1.5.dp, GoldAccent10), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "AI Content Strategist",
                    tint = GoldAccent10,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AI Content Strategist",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent10
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x3300E676))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "GROWTH EXPERT", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = speechText,
                    fontSize = 12.5.sp,
                    color = TextWhite10,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

private fun getAiSpeechForLevel10Module(module: Int, lang: String): String {
    val isHindi = lang == "Hindi"
    val isHinglish = lang == "Hinglish"

    return when (module) {
        1 -> when {
            isHindi || isHinglish -> "Consistency hi creator ki sabse badi superpower hai. Aaj hum tumhara complete content system banayenge."
            else -> "Consistency is every creator's single greatest superpower. Today we will build your complete content growth system."
        }
        2 -> when {
            isHindi || isHinglish -> "30-Day Content Calendar: Har din ka clear topic aur format ready hone par burnout kabhi nahi hota."
            else -> "30-Day Content Calendar: With clear daily topics and formats mapped out, creator block becomes impossible."
        }
        3 -> when {
            isHindi || isHinglish -> "Weekly Planner: Monday se Sunday tak apna complete weekly posting framework edit aur custom lock karo."
            else -> "Weekly Planner: Edit and lock your complete Monday to Sunday weekly posting rhythm."
        }
        4 -> when {
            isHindi || isHinglish -> "Content Batching: Daily shoot karne ke bajaye ek hi din me 4-5 videos record aur schedule karo."
            else -> "Content Batching: Save 6+ hours weekly by filming and editing multiple videos in dedicated batch blocks."
        }
        5 -> when {
            isHindi || isHinglish -> "Story Growth Strategy: 7 high-converting story types jo link clicks aur follower trust double karti hain."
            else -> "Story Growth Strategy: Master 7 proven story templates designed to double bio link taps and audience trust."
        }
        6 -> when {
            isHindi || isHinglish -> "AI Content Planner: Apne product ka naam dalo, mera AI full Reel, Story, Carousel & Caption plan generate karega!"
            else -> "AI Content Planner: Enter any product name to generate a complete custom Reel, Story, Caption, and CTA bundle!"
        }
        7 -> when {
            isHindi || isHinglish -> "Consistency Tracker: Apne daily posting streak, weekly goals, aur mission completion ko live track karo."
            else -> "Consistency Tracker: Monitor your live posting streak, weekly target completions, and learning milestones."
        }
        8 -> when {
            isHindi || isHinglish -> "Burnout Prevention: Long-term success ke liye rest days, time blocking, aur realistic routines bohot zaroori hain."
            else -> "Burnout Prevention: Safeguard your long-term creative longevity with rest days, time blocks, and simple workflows."
        }
        9 -> when {
            isHindi || isHinglish -> "Weekly Review: AI tumhara posting consistency review karega aur agle hafte ke focus points suggest karega."
            else -> "Weekly Review: AI evaluates your overall consistency and automatically formulates your high-impact plan for next week."
        }
        10 -> when {
            isHindi || isHinglish -> "Shaandar! Tumne Level 10 complete kar liya! Claim karo Content Growth Expert Badge & +600 XP!"
            else -> "Phenomenal work! You completed Level 10! Claim your Content Growth Expert Badge and +600 XP!"
        }
        else -> "Build trust. Post consistently. Grow exponentially!"
    }
}

/**
 * MODULE BADGE HELPER
 */
@Composable
private fun Level10ModuleBadge(moduleNum: Int, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33B388FF))
                .border(BorderStroke(1.dp, PurplePrimary10), RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(text = "MODULE $moduleNum", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent10)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite10)
    }
}

/**
 * GLASS SHINE BUTTON
 */
@Composable
private fun GlassShineButton10(
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
                        Color(0xFF6A1B9A),
                        Color(0xFFAB47BC)
                    )
                )
            )
            .border(BorderStroke(1.dp, GoldAccent10.copy(alpha = 0.6f)), RoundedCornerShape(16.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite10
            )
        }
    }
}

/**
 * MODULE 1: Content Pillars
 */
@Composable
private fun Level10Module1ContentPillarsView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level10ModuleBadge(moduleNum = 1, title = "Content Pillars")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "The 8 Core Pillars of High-Converting Creators",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite10,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val pillars = listOf(
            ContentPillar10("1. Product Reviews", "In-depth honest testing of quality, fit, and durability", "Best for: Building deep buyer trust", Icons.Default.Star),
            ContentPillar10("2. Shopping Finds", "Viral hauls, budget roundups, and 'What I Ordered Vs Got'", "Best for: High engagement & shares", Icons.Default.ShoppingBag),
            ContentPillar10("3. Lifestyle", "Daily routines, workspace setup, GRWM, and vlog snippets", "Best for: Follower connection", Icons.Default.SelfImprovement),
            ContentPillar10("4. Fashion Tips", "Styling 1 outfit 3 ways, capsule wardrobes, color pairing", "Best for: High bookmarking/saves", Icons.Default.Style),
            ContentPillar10("5. Beauty Tips", "Skincare routines, shade swatches, 5-min makeup hacks", "Best for: Steady repeat link clicks", Icons.Default.AutoAwesome),
            ContentPillar10("6. Tech Finds", "Desk accessories, gadgets, phone covers, aesthetic setups", "Best for: Impulse purchases", Icons.Default.Videocam),
            ContentPillar10("7. Budget Deals", "Under ₹299 / ₹499 curation lists with direct Wishlink tags", "Best for: Maximum volume sales", Icons.Default.TrendingUp),
            ContentPillar10("8. Seasonal Picks", "Festive wear, monsoon essentials, winter layerings", "Best for: Peak seasonal revenue", Icons.Default.DateRange)
        )

        pillars.forEach { p ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary10), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = p.icon, contentDescription = null, tint = GoldAccent10, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = p.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite10)
                        Text(text = p.desc, fontSize = 11.sp, color = Color.LightGray)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = p.whenToUse, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = GoldAccent10)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton10(text = "View 30-Day AI Content Calendar →", onClick = onContinue)
    }
}

private data class ContentPillar10(val title: String, val desc: String, val whenToUse: String, val icon: ImageVector)

/**
 * MODULE 2: 30-Day AI Content Calendar
 */
@Composable
private fun Level10Module2ContentCalendarView(
    niche: String,
    platform: String,
    onContinue: () -> Unit
) {
    var selectedWeek by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level10ModuleBadge(moduleNum = 2, title = "30-Day AI Content Calendar")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "30-Day Schedule tailored for $niche on $platform",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite10,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Week Selector Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            (1..4).forEach { w ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedWeek == w) GoldAccent10 else Color(0x33FFFFFF))
                        .clickable { selectedWeek = w }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Week $w",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedWeek == w) Color.Black else TextWhite10
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        val daysList = remember(selectedWeek, niche) {
            val startDay = (selectedWeek - 1) * 7 + 1
            listOf(
                CalDay(startDay, "Reel: Top 3 $niche Items Under ₹499", "Reel / Short", "High Sales"),
                CalDay(startDay + 1, "Story: GRWM + Link Sticker to Wishlink Store", "Story", "Trust & Clicks"),
                CalDay(startDay + 2, "Reel: 1 Outfit / Item Styled 3 Different Ways", "Reel", "High Saves"),
                CalDay(startDay + 3, "Story: Q&A Sticker 'Which one should I try?'", "Interactive Story", "Engagement"),
                CalDay(startDay + 4, "Reel: Honest Unboxing & Quality Inspection", "Reel", "Conversion"),
                CalDay(startDay + 5, "Story: Weekend Flash Steals & Countdown Sticker", "Story", "Urgency Sales"),
                CalDay(startDay + 6, "Carousel: Weekly Top 5 Favorite Picks Collection", "Carousel", "Store Visits")
            )
        }

        daysList.forEach { d ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x33B388FF)), RoundedCornerShape(14.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x33B388FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "D${d.dayNum}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldAccent10)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = d.topic, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite10)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Format: ${d.type}", fontSize = 10.5.sp, color = Color.LightGray)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "• Goal: ${d.goal}", fontSize = 10.5.sp, color = Color(0xFF00E676))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton10(text = "Open Weekly Planner →", onClick = onContinue)
    }
}

private data class CalDay(val dayNum: Int, val topic: String, val type: String, val goal: String)

/**
 * MODULE 3: Weekly Planner
 */
@Composable
private fun Level10Module3WeeklyPlannerView(
    mon: String, tue: String, wed: String, thu: String, fri: String, sat: String, sun: String,
    onMonChange: (String) -> Unit, onTueChange: (String) -> Unit, onWedChange: (String) -> Unit,
    onThuChange: (String) -> Unit, onFriChange: (String) -> Unit, onSatChange: (String) -> Unit,
    onSunChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level10ModuleBadge(moduleNum = 3, title = "Weekly Planner")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Editable Monday to Sunday Schedule",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite10,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        val days = listOf(
            Triple("Monday", mon, onMonChange),
            Triple("Tuesday", tue, onTueChange),
            Triple("Wednesday", wed, onWedChange),
            Triple("Thursday", thu, onThuChange),
            Triple("Friday", fri, onFriChange),
            Triple("Saturday", sat, onSatChange),
            Triple("Sunday", sun, onSunChange)
        )

        days.forEach { (dayName, value, onChange) ->
            OutlinedTextField(
                value = value,
                onValueChange = onChange,
                label = { Text("$dayName Plan", color = PurplePrimary10) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                maxLines = 1,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PurplePrimary10,
                    unfocusedBorderColor = Color(0x44FFFFFF),
                    focusedTextColor = TextWhite10,
                    unfocusedTextColor = TextWhite10
                )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        GlassShineButton10(text = "Master Content Batching →", onClick = onContinue)
    }
}

/**
 * MODULE 4: Content Batching
 */
@Composable
private fun Level10Module4ContentBatchingView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level10ModuleBadge(moduleNum = 4, title = "Content Batching")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "5-Step Workflow: Save 6+ Hours Weekly",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite10,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val steps = listOf(
            BatchStep("Step 1: Research (30 Mins)", "Find trending hooks, audio & Wishlink high-commission items.", Icons.Default.Lightbulb),
            BatchStep("Step 2: Record Batch (90 Mins)", "Film 4 to 5 reels in one lighting setup without changing gear.", Icons.Default.Videocam),
            BatchStep("Step 3: Edit Together (60 Mins)", "Apply consistent captions, transitions & cut out silence.", Icons.Default.Edit),
            BatchStep("Step 4: Schedule Together (20 Mins)", "Save drafts & set captions with direct Wishlink bio tags.", Icons.Default.Schedule),
            BatchStep("Step 5: Publish Consistently", "Post automatically without daily shooting stress!", Icons.Default.RocketLaunch)
        )

        steps.forEach { st ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary10), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = st.icon, contentDescription = null, tint = GoldAccent10, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = st.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite10)
                        Text(text = st.desc, fontSize = 11.5.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton10(text = "Learn Story Growth Strategy →", onClick = onContinue)
    }
}

private data class BatchStep(val title: String, val desc: String, val icon: ImageVector)

/**
 * MODULE 5: Story Growth Strategy
 */
@Composable
private fun Level10Module5StoryGrowthStrategyView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level10ModuleBadge(moduleNum = 5, title = "Story Growth Strategy")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "7 Story Formats That Drive 2X Link Taps",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite10,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val stories = listOf(
            StoryType("1. Morning Story", "Good morning coffee / outfit preview before starting day.", Icons.Default.SelfImprovement),
            StoryType("2. Behind The Scenes", "Unboxing parcel boxes or filming chaos.", Icons.Default.CameraAlt),
            StoryType("3. Poll Sticker", "'Option A vs Option B: Which one should I style?'", Icons.Default.TaskAlt),
            StoryType("4. Question Sticker", "'Ask me for links to any product from today's reel!'", Icons.Default.Psychology),
            StoryType("5. Product Reminder", "Close-up fabric / quality video with Wishlink direct link.", Icons.Default.ShoppingBag),
            StoryType("6. Countdown Sticker", "'Sale ends in 3 hours! Grab under ₹499 picks.'", Icons.Default.Schedule),
            StoryType("7. Store Reminder", "'Full catalog linked in bio! Tap store link below.'", Icons.Default.Storefront)
        )

        stories.forEach { s ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x33B388FF)), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = s.icon, contentDescription = null, tint = GoldAccent10, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = s.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite10)
                        Text(text = s.desc, fontSize = 11.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton10(text = "Try AI Content Planner →", onClick = onContinue)
    }
}

private data class StoryType(val name: String, val desc: String, val icon: ImageVector)

/**
 * MODULE 6: AI Content Planner
 */
@Composable
private fun Level10Module6AiContentPlannerView(
    productInput: String,
    onProductInputChange: (String) -> Unit,
    isGenerating: Boolean,
    isGenerated: Boolean,
    onStartGenerate: () -> Unit,
    onGenerateFinished: () -> Unit,
    niche: String,
    onContinue: () -> Unit
) {
    LaunchedEffect(isGenerating) {
        if (isGenerating) {
            delay(1800)
            onGenerateFinished()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level10ModuleBadge(moduleNum = 6, title = "AI Content Planner")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Generate Unique Content Pack",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite10,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = productInput,
            onValueChange = onProductInputChange,
            label = { Text("Enter Target Product / Topic", color = PurplePrimary10) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary10,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite10,
                unfocusedTextColor = TextWhite10
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        GlassShineButton10(text = "Generate Content Pack ✨", onClick = onStartGenerate)

        Spacer(modifier = Modifier.height(16.dp))

        if (isGenerating) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x331D0032))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = GoldAccent10, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "AI Generating Reel, Story, Carousel & Caption Pack...", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldAccent10)
                }
            }
        } else if (isGenerated) {
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
                        Text(text = "AI Content Pack for: $productInput", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite10)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "• Reel Hook Idea: 'Stop scrolling if you want $productInput under ₹499!'", fontSize = 11.5.sp, color = TextWhite10)
                    Text(text = "• Story Idea: Close-up material zoom + Poll 'Cop or Drop?'", fontSize = 11.5.sp, color = TextWhite10)
                    Text(text = "• Carousel Idea: 3 ways to style / use $productInput.", fontSize = 11.5.sp, color = TextWhite10)
                    Text(text = "• Caption Direction: 'Found the best $niche deal on Wishlink! Link in bio.'", fontSize = 11.5.sp, color = TextWhite10)
                    Text(text = "• Call To Action: 'Comment LINK & check bio for direct code!'", fontSize = 11.5.sp, color = GoldAccent10, fontWeight = FontWeight.Bold)
                    Text(text = "• Thumbnail Text: 'Worth the Hype? Honest Test!'", fontSize = 11.5.sp, color = TextWhite10)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton10(text = "Check Consistency Tracker →", onClick = onContinue)
    }
}

/**
 * MODULE 7: Consistency Tracker
 */
@Composable
private fun Level10Module7ConsistencyTrackerView(
    streak: Int,
    onIncrementStreak: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level10ModuleBadge(moduleNum = 7, title = "Consistency Tracker")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Your Live Growth Dashboard",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite10,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // STREAK HERO CARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFFE65100),
                            Color(0xFFFF8F00)
                        )
                    )
                )
                .border(BorderStroke(1.5.dp, GoldAccent10), RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.LocalFireDepartment, contentDescription = null, tint = GoldAccent10, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "CURRENT POSTING STREAK", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite10)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "$streak DAYS ACTIVE 🔥", fontSize = 24.sp, fontWeight = FontWeight.Black, color = TextWhite10)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x33000000))
                        .clickable { onIncrementStreak() }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(text = "+ Log Today", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite10)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        val metrics = listOf(
            TrackerMetric("Weekly Posts Goal", "6 / 7 Posts Completed", 0.85f),
            TrackerMetric("Monthly Posts Target", "24 / 30 Posts Completed", 0.80f),
            TrackerMetric("Learning & Skills", "10 / 10 Modules Mastered", 1.0f),
            TrackerMetric("Mission Completion", "100% On Track", 1.0f)
        )

        metrics.forEach { m ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x33B388FF)), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = m.label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite10)
                        Text(text = m.value, fontSize = 11.sp, color = GoldAccent10, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(Color(0x33FFFFFF))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(m.progress)
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(GoldAccent10)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton10(text = "Learn Burnout Prevention →", onClick = onContinue)
    }
}

private data class TrackerMetric(val label: String, val value: String, val progress: Float)

/**
 * MODULE 8: Burnout Prevention
 */
@Composable
private fun Level10Module8BurnoutPreventionView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level10ModuleBadge(moduleNum = 8, title = "Burnout Prevention")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Sustainable Creator Longevity Rules",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite10,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val rules = listOf(
            BurnoutRule("1. Mandatory Rest Days", "Schedule at least 1 day per week with zero filming or editing.", Icons.Default.SelfImprovement),
            BurnoutRule("2. Batch Recording", "Never shoot every single day. Film in dedicated 2-hour windows.", Icons.Default.Videocam),
            BurnoutRule("3. Time Blocking", "Separate research time, editing time, and story posting time.", Icons.Default.Schedule),
            BurnoutRule("4. Simple Workflow", "Use clean templates for stories & reels to avoid over-editing.", Icons.Default.AutoAwesome),
            BurnoutRule("5. Healthy Routine", "Prioritize sleep, hydration, and offline hobbies above views.", Icons.Default.FitnessCenter)
        )

        rules.forEach { r ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary10), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = r.icon, contentDescription = null, tint = GoldAccent10, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = r.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite10)
                        Text(text = r.desc, fontSize = 11.5.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton10(text = "Run AI Weekly Review →", onClick = onContinue)
    }
}

private data class BurnoutRule(val title: String, val desc: String, val icon: ImageVector)

/**
 * MODULE 9: Weekly Review
 */
@Composable
private fun Level10Module9WeeklyReviewView(
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
        Level10ModuleBadge(moduleNum = 9, title = "Weekly Review")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "AI Consistency & Growth Summary",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite10,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x331D0032))
                .border(BorderStroke(1.5.dp, GoldAccent10), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Psychology, contentDescription = null, tint = GoldAccent10, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "AI Evaluation Report", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldAccent10)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "• Posting Consistency: 92% adherence to batching schedule.", fontSize = 12.sp, color = TextWhite10)
                Text(text = "• Content Learning: All 10 Master Modules completed successfully.", fontSize = 12.sp, color = TextWhite10)
                Text(text = "• Store Optimization: Wishlink store active with high CTR collections.", fontSize = 12.sp, color = TextWhite10)

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Generated Next Week Focus:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                Text(text = "1. Double down on $niche budget deals under ₹499.", fontSize = 11.5.sp, color = TextWhite10)
                Text(text = "2. Maintain 5 reels/shorts weekly schedule on $platform.", fontSize = 11.5.sp, color = TextWhite10)
                Text(text = "3. Post daily story poll stickers to increase bio link traffic.", fontSize = 11.5.sp, color = TextWhite10)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton10(text = "Proceed to Today's Mission & Achievement →", onClick = onContinue)
    }
}

/**
 * MODULE 10: Today's Mission & Achievement (+600 XP)
 */
@Composable
private fun Level10Module10AchievementView(
    streak: Int,
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
        Level10ModuleBadge(moduleNum = 10, title = "Today's Mission & Achievement")

        Spacer(modifier = Modifier.height(14.dp))

        // MISSION CARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x331D0032))
                .border(BorderStroke(1.dp, PurplePrimary10), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.TaskAlt, contentDescription = null, tint = GoldAccent10, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "TODAY'S MISSION: Plan Your Next Week", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite10)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Estimated Time: 20 Minutes • Setup your 5 reels for next week", fontSize = 11.sp, color = Color.LightGray)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // BADGE UNLOCK CARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF38006B),
                            Color(0xFF6A1B9A),
                            Color(0xFF4A148C)
                        )
                    )
                )
                .border(BorderStroke(2.dp, GoldAccent10), RoundedCornerShape(24.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .graphicsLayer { scaleX = 1f + (shineAnim * 0.05f); scaleY = 1f + (shineAnim * 0.05f) }
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(GoldAccent10, Color(0xFFFF6F00))))
                        .border(BorderStroke(3.dp, TextWhite10), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MilitaryTech,
                        contentDescription = "Content Growth Expert Badge",
                        tint = Color.Black,
                        modifier = Modifier.size(46.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Content Growth Expert",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldAccent10
                )

                Text(
                    text = "Mastered 30-Day Content System & Consistency",
                    fontSize = 11.5.sp,
                    color = TextWhite10,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(GoldAccent10)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "+600 XP REWARD UNLOCKED",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (!isUnlocked) {
            GlassShineButton10(
                text = "Claim Badge & Complete Level 10 🏆",
                onClick = onUnlockAchievement
            )
        } else {
            GlassShineButton10(
                text = "Finish & Return to Creator Guide 🎉",
                onClick = onCompleteLevel
            )
        }
    }
}
