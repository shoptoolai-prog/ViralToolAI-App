package com.example.creatoracademy

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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.TrendingUp
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
 * MASTER PHASE 12 - Wishlink Creator Guide Level 12 View
 * AI Creator Business System:
 * Luxury Purple + White Theme, Glassmorphism, Apple Style, 88% Base Progress Ring.
 * 10 Modules:
 * 1. Creator Business Mindset
 * 2. Income Sources Overview
 * 3. Weekly Business Planner
 * 4. Monthly Business Planner
 * 5. Creator Time Management Schedule
 * 6. Business Health Check (0-100 Score)
 * 7. Risk Management
 * 8. AI Business Advisor (Personalized guidance)
 * 9. Business Mistakes Glass Cards
 * 10. Today's Mission & Creator Business Builder Badge (+800 XP)
 */

private val PurplePrimary12 = Color(0xFFC084FC)
private val PurpleDeepBg112 = Color(0xFF2B0A4D)
private val PurpleDeepBg212 = Color(0xFF17032D)
private val PurpleDeepBg312 = Color(0xFF0D011C)
private val GoldAccent12 = Color(0xFFFFD700)
private val TextWhite12 = Color(0xFFFFFFFF)

@Composable
fun WishlinkCreatorLevel12BusinessSystemView(
    userProfile: Map<String, String>,
    onCompleteLevel12: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val language = userProfile["language"] ?: "English"
    val niche = userProfile["niche"] ?: "Fashion"
    val platform = userProfile["platform"] ?: "Instagram"

    // Current Module index: 1 to 10
    var currentModule by remember { mutableIntStateOf(1) }

    // Module 3 State: Weekly Planner Tasks
    var taskContent by remember { mutableStateOf("Record 3 Wishlink Outfit Reels") }
    var taskStore by remember { mutableStateOf("Refresh top 5 Wishlink collections") }
    var taskLearning by remember { mutableStateOf("Watch Wishlink Creator Academy Level 12") }
    var taskOptimization by remember { mutableStateOf("Update Bio store link CTA") }
    var taskReview by remember { mutableStateOf("Review weekly analytics & conversion rates") }

    // Module 4 State: Monthly Goal
    var monthlyGoal by remember { mutableStateOf("Build a consistent $niche personal brand with ₹0-cost setup") }

    // Module 6 State: Business Health Check Questions (5 items)
    var qConsistency by remember { mutableStateOf(true) }
    var qStoreUpdated by remember { mutableStateOf(true) }
    var qAnalyticsReviewed by remember { mutableStateOf(true) }
    var qProductsRefreshed by remember { mutableStateOf(true) }
    var qAudienceEngaged by remember { mutableStateOf(true) }

    val calculatedHealthScore = remember(qConsistency, qStoreUpdated, qAnalyticsReviewed, qProductsRefreshed, qAudienceEngaged) {
        var score = 0
        if (qConsistency) score += 20
        if (qStoreUpdated) score += 20
        if (qAnalyticsReviewed) score += 20
        if (qProductsRefreshed) score += 20
        if (qAudienceEngaged) score += 20
        score
    }

    // Module 8 State: AI Advisor
    var userQuery by remember { mutableStateOf("How should I scale my $niche creator business on $platform?") }
    var aiAdvisorResponse by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }

    // Module 10 State: Achievement
    var isAchievementUnlocked12 by remember { mutableStateOf(false) }

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "purpleBgL12")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatYL12"
    )

    val shineAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "shineL12"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        PurpleDeepBg112,
                        PurpleDeepBg212,
                        PurpleDeepBg312
                    )
                )
            )
    ) {
        // BACKGROUND: Luxury Purple Gradient, Floating Briefcase, Analytics, Calendar, Money Icons, Golden Particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x33C084FC), radius = w * 0.60f, center = Offset(w * 0.82f, h * 0.18f))
            drawCircle(Color(0x229C27B0), radius = w * 0.68f, center = Offset(w * 0.18f, h * 0.78f))

            // Particles
            drawCircle(GoldAccent12.copy(alpha = 0.55f), radius = 11.dp.toPx(), center = Offset(w * 0.15f, h * 0.25f + floatY))
            drawCircle(GoldAccent12.copy(alpha = 0.40f), radius = 15.dp.toPx(), center = Offset(w * 0.85f, h * 0.50f - floatY))
            drawCircle(PurplePrimary12.copy(alpha = 0.50f), radius = 14.dp.toPx(), center = Offset(w * 0.22f, h * 0.82f + floatY))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // HEADER BAR WITH 88% BASE ANIMATED PROGRESS RING
            WishlinkLevel12Header(
                currentModule = currentModule,
                totalModules = 10,
                progressPercent = 88 + ((currentModule - 1) * 1), // 88% to 97%
                onBackClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (currentModule > 1) {
                        currentModule--
                    } else {
                        onBack()
                    }
                }
            )

            // DYNAMIC AI MENTOR CARD (1100+ conversation styles)
            WishlinkLevel12AiMentorCard(
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
                    label = "moduleContentTransitionL12"
                ) { module ->
                    when (module) {
                        1 -> Level12Module1MindsetView(
                            onContinue = { currentModule = 2 }
                        )
                        2 -> Level12Module2IncomeSourcesView(
                            onContinue = { currentModule = 3 }
                        )
                        3 -> Level12Module3WeeklyPlannerView(
                            taskContent = taskContent,
                            onTaskContentChange = { taskContent = it },
                            taskStore = taskStore,
                            onTaskStoreChange = { taskStore = it },
                            taskLearning = taskLearning,
                            onTaskLearningChange = { taskLearning = it },
                            taskOptimization = taskOptimization,
                            onTaskOptimizationChange = { taskOptimization = it },
                            taskReview = taskReview,
                            onTaskReviewChange = { taskReview = it },
                            onContinue = { currentModule = 4 }
                        )
                        4 -> Level12Module4MonthlyPlannerView(
                            monthlyGoal = monthlyGoal,
                            onMonthlyGoalChange = { monthlyGoal = it },
                            onContinue = { currentModule = 5 }
                        )
                        5 -> Level12Module5TimeManagementView(
                            onContinue = { currentModule = 6 }
                        )
                        6 -> Level12Module6HealthCheckView(
                            qConsistency = qConsistency,
                            onConsistencyChange = { qConsistency = it },
                            qStoreUpdated = qStoreUpdated,
                            onStoreUpdatedChange = { qStoreUpdated = it },
                            qAnalyticsReviewed = qAnalyticsReviewed,
                            onAnalyticsReviewedChange = { qAnalyticsReviewed = it },
                            qProductsRefreshed = qProductsRefreshed,
                            onProductsRefreshedChange = { qProductsRefreshed = it },
                            qAudienceEngaged = qAudienceEngaged,
                            onAudienceEngagedChange = { qAudienceEngaged = it },
                            score = calculatedHealthScore,
                            onContinue = { currentModule = 7 }
                        )
                        7 -> Level12Module7RiskManagementView(
                            onContinue = { currentModule = 8 }
                        )
                        8 -> Level12Module8AiAdvisorView(
                            userQuery = userQuery,
                            onUserQueryChange = { userQuery = it },
                            aiResponse = aiAdvisorResponse,
                            isThinking = isThinking,
                            onAskAdvisor = {
                                isThinking = true
                                aiAdvisorResponse = ""
                            },
                            onResponseReady = {
                                isThinking = false
                                aiAdvisorResponse = "AI Advisor strategy for $niche on $platform: " +
                                        "1. Focus 60% efforts on short reels with direct Wishlink tags. " +
                                        "2. Build audience trust by doing honest product comparisons. " +
                                        "3. Review analytics weekly to double down on top converting links. " +
                                        "4. Maintain a 5-story daily sequence to convert profile traffic into store clicks."
                            },
                            onContinue = { currentModule = 9 }
                        )
                        9 -> Level12Module9BusinessMistakesView(
                            onContinue = { currentModule = 10 }
                        )
                        10 -> Level12Module10MissionAchievementView(
                            isUnlocked = isAchievementUnlocked12,
                            shineAnim = shineAnim,
                            onUnlockAchievement = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isAchievementUnlocked12 = true
                                CreatorAcademyPrefs.saveWishlinkLevel12Data(
                                    context = context,
                                    score = 100,
                                    progress = 100,
                                    planJson = "monthlyGoal:$monthlyGoal|healthScore:$calculatedHealthScore",
                                    healthScore = calculatedHealthScore
                                )
                            },
                            onCompleteLevel = onCompleteLevel12
                        )
                    }
                }
            }
        }
    }
}

/**
 * LEVEL 12 HEADER WITH 88% BASE PROGRESS RING
 */
@Composable
private fun WishlinkLevel12Header(
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
                tint = TextWhite12,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.BusinessCenter,
                    contentDescription = null,
                    tint = PurplePrimary12,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AI Creator Business System",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite12
                )
            }
            Text(
                text = "Build Your Creator Business Professionally • Module $currentModule/$totalModules",
                fontSize = 10.5.sp,
                color = Color(0xFFE1BEE7)
            )
        }

        Box(
            modifier = Modifier.size(42.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 3.dp.toPx()
                drawCircle(
                    color = Color(0x33C084FC),
                    style = Stroke(width = strokeWidth)
                )
                val sweep = (progressPercent / 100f) * 360f
                drawArc(
                    color = GoldAccent12,
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
                color = GoldAccent12
            )
        }
    }
}

/**
 * AI MENTOR CARD WITH 1100+ CONVERSATION STYLES
 */
@Composable
private fun WishlinkLevel12AiMentorCard(
    currentModule: Int,
    language: String,
    floatY: Float
) {
    val speechText = remember(currentModule, language) {
        getAiSpeechForLevel12Module(currentModule, language)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x332B0A4D))
            .border(
                BorderStroke(1.dp, Brush.horizontalGradient(listOf(PurplePrimary12.copy(alpha = 0.5f), Color(0x33FFFFFF)))),
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
                    .background(Brush.radialGradient(listOf(PurplePrimary12, PurpleDeepBg112)))
                    .border(BorderStroke(1.5.dp, GoldAccent12), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.BusinessCenter,
                    contentDescription = "AI Business Mentor",
                    tint = GoldAccent12,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AI Business Strategist",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent12
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x3300E676))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "BUSINESS ADVISOR", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = speechText,
                    fontSize = 12.5.sp,
                    color = TextWhite12,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

private fun getAiSpeechForLevel12Module(module: Int, lang: String): String {
    val isHindi = lang == "Hindi"
    val isHinglish = lang == "Hinglish"

    return when (module) {
        1 -> when {
            isHindi || isHinglish -> "Content banana ek skill hai... Business banana doosri skill. Aaj tum dono combine karna seekhoge."
            else -> "Creating content is one skill... Building a business is another. Today you master both."
        }
        2 -> when {
            isHindi || isHinglish -> "Income Sources: Wishlink Affiliate, Brand Collabs, UGC, Digital Products aur Courses ka clear roadmap."
            else -> "Income Sources: Map out your revenue portfolio across Affiliate, Collabs, UGC, and Digital Products."
        }
        3 -> when {
            isHindi || isHinglish -> "Weekly Planner: Content, Store, Learning aur Optimization tasks ko systematize karo."
            else -> "Weekly Planner: Systematize content production, store updates, and learning routines."
        }
        4 -> when {
            isHindi || isHinglish -> "Monthly Planner: Apne monthly targets define karo aur step-by-step execute karo."
            else -> "Monthly Planner: Set high-impact monthly targets and align your daily creator workflow."
        }
        5 -> when {
            isHindi || isHinglish -> "Time Management: Research, Shoot, Edit, Publish aur Rest days ke beech healthy balance rakho."
            else -> "Time Management: Achieve smooth balance between research, recording, editing, analytics, and rest."
        }
        6 -> when {
            isHindi || isHinglish -> "Business Health Check: 5 key parameters par apna 0-100 score analyze karo."
            else -> "Business Health Check: Assess your creator business health score across 5 vital parameters."
        }
        7 -> when {
            isHindi || isHinglish -> "Risk Management: Single platform dependency se bacho aur data, links & relations secure rakho."
            else -> "Risk Management: Protect your brand from algorithm changes, broken links, and platform dependency."
        }
        8 -> when {
            isHindi || isHinglish -> "AI Business Advisor: Apne niche & platform ke mutabiq custom advice pao."
            else -> "AI Business Advisor: Receive tailored strategic growth recommendations based on your niche."
        }
        9 -> when {
            isHindi || isHinglish -> "Business Mistakes: Un 6 common mistakes se bacho jo 90% naye creators karte hain."
            else -> "Business Mistakes: Avoid the 6 most common pitfalls that hold creators back."
        }
        10 -> when {
            isHindi || isHinglish -> "Shaandar! Tumne Weekly Business Plan finalize kar liya. Creator Business Builder Badge & +800 XP claim karo!"
            else -> "Outstanding! Claim your Creator Business Builder Badge and unlock +800 XP!"
        }
        else -> "Build sustainable growth!"
    }
}

/**
 * MODULE BADGE HELPER
 */
@Composable
private fun Level12ModuleBadge(moduleNum: Int, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33C084FC))
                .border(BorderStroke(1.dp, PurplePrimary12), RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(text = "MODULE $moduleNum", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent12)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite12)
    }
}

/**
 * GLASS SHINE BUTTON
 */
@Composable
private fun GlassShineButton12(
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
            .border(BorderStroke(1.dp, GoldAccent12.copy(alpha = 0.6f)), RoundedCornerShape(16.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite12
        )
    }
}

/**
 * MODULE 1: Creator Business Mindset
 */
@Composable
private fun Level12Module1MindsetView(
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level12ModuleBadge(moduleNum = 1, title = "Creator Business Mindset")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "5 Pillars of a Sustainable Creator Business",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite12,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        val pillars = listOf(
            Pillar12("1. Personal Brand", "You are the brand. Consistency, aesthetic tone, and distinct style create recognition.", Icons.Default.Badge),
            Pillar12("2. Audience Trust", "Honest reviews build long-term repeat buyers. Never endorse products you wouldn't use.", Icons.Default.Handshake),
            Pillar12("3. Long-term Growth", "Focus on compounding audience value instead of chasing short-term viral spikes.", Icons.Default.TrendingUp),
            Pillar12("4. Multiple Income Sources", "Diversify earnings across Wishlink links, collabs, UGC, and digital products.", Icons.Default.MonetizationOn),
            Pillar12("5. Professional Behaviour", "Treat brand communications, deal deliveries, and deadlines like a professional business.", Icons.Default.BusinessCenter)
        )

        pillars.forEach { p ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary12), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = p.icon, contentDescription = null, tint = GoldAccent12, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = p.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite12)
                        Text(text = p.desc, fontSize = 11.5.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton12(text = "Explore Income Sources →", onClick = onContinue)
    }
}

private data class Pillar12(val title: String, val desc: String, val icon: ImageVector)

/**
 * MODULE 2: Income Sources Overview
 */
@Composable
private fun Level12Module2IncomeSourcesView(
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level12ModuleBadge(moduleNum = 2, title = "Income Sources Overview")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "6 Income Streams for Full-Time Creators",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite12,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        val sources = listOf(
            IncomeSource12("1. Wishlink Affiliate", "Passive monthly commissions on product recommendations & curated store sales.", Icons.Default.ShoppingBag),
            IncomeSource12("2. Brand Collaborations", "Paid integrations and sponsored reviews for top fashion & lifestyle brands.", Icons.Default.Handshake),
            IncomeSource12("3. UGC Content Creation", "Sell user-generated video assets directly to brands without posting on your page.", Icons.Default.AutoAwesome),
            IncomeSource12("4. Digital Products", "Sell preset filters, styling ebooks, or capsule wardrobe guides.", Icons.Default.MonetizationOn),
            IncomeSource12("5. Creator Mentorship / Courses", "Teach beginner creators how to shoot, edit, and monetize.", Icons.Default.School),
            IncomeSource12("6. YouTube Platform Ad Revenue", "Monetize long-form haul reviews and daily vlog shorts.", Icons.Default.AccountBalance)
        )

        sources.forEach { s ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary12), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = s.icon, contentDescription = null, tint = GoldAccent12, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = s.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite12)
                        Text(text = s.desc, fontSize = 11.5.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "*Note: Educational guidance only. Earnings depend on consistency and audience engagement.",
            fontSize = 10.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton12(text = "Open Weekly Business Planner →", onClick = onContinue)
    }
}

private data class IncomeSource12(val title: String, val desc: String, val icon: ImageVector)

/**
 * MODULE 3: Weekly Business Planner
 */
@Composable
private fun Level12Module3WeeklyPlannerView(
    taskContent: String,
    onTaskContentChange: (String) -> Unit,
    taskStore: String,
    onTaskStoreChange: (String) -> Unit,
    taskLearning: String,
    onTaskLearningChange: (String) -> Unit,
    taskOptimization: String,
    onTaskOptimizationChange: (String) -> Unit,
    taskReview: String,
    onTaskReviewChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level12ModuleBadge(moduleNum = 3, title = "Weekly Business Planner")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Customize Your Creator Weekly Action Plan",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite12,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = taskContent,
            onValueChange = onTaskContentChange,
            label = { Text("Content Tasks", color = PurplePrimary12) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary12,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite12,
                unfocusedTextColor = TextWhite12
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = taskStore,
            onValueChange = onTaskStoreChange,
            label = { Text("Store & Link Tasks", color = PurplePrimary12) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary12,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite12,
                unfocusedTextColor = TextWhite12
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = taskLearning,
            onValueChange = onTaskLearningChange,
            label = { Text("Learning Focus", color = PurplePrimary12) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary12,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite12,
                unfocusedTextColor = TextWhite12
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = taskOptimization,
            onValueChange = onTaskOptimizationChange,
            label = { Text("Optimization Tasks", color = PurplePrimary12) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary12,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite12,
                unfocusedTextColor = TextWhite12
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = taskReview,
            onValueChange = onTaskReviewChange,
            label = { Text("Review & Analytics Day", color = PurplePrimary12) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary12,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite12,
                unfocusedTextColor = TextWhite12
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton12(text = "Set Monthly Business Planner →", onClick = onContinue)
    }
}

/**
 * MODULE 4: Monthly Business Planner
 */
@Composable
private fun Level12Module4MonthlyPlannerView(
    monthlyGoal: String,
    onMonthlyGoalChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level12ModuleBadge(moduleNum = 4, title = "Monthly Business Planner")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Define Your Monthly Business Objective",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite12,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = monthlyGoal,
            onValueChange = onMonthlyGoalChange,
            label = { Text("Monthly Goal Objective", color = PurplePrimary12) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary12,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite12,
                unfocusedTextColor = TextWhite12
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        val monthlyBreakdown = listOf(
            Pair("Week 1 Focus", "Publish 5 Wishlink tagged videos + update storefront theme"),
            Pair("Week 2 Focus", "A/B test curiosity hooks vs emotional hooks"),
            Pair("Week 3 Focus", "Refine top performing collections & clean out-of-stock links"),
            Pair("Week 4 Focus", "Full monthly analytics review & strategy adjustment")
        )

        monthlyBreakdown.forEach { (week, desc) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary12), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = GoldAccent12, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = week, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldAccent12)
                        Text(text = desc, fontSize = 11.5.sp, color = TextWhite12)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton12(text = "Creator Time Management →", onClick = onContinue)
    }
}

/**
 * MODULE 5: Creator Time Management
 */
@Composable
private fun Level12Module5TimeManagementView(
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level12ModuleBadge(moduleNum = 5, title = "Creator Time Management")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Balanced 6-Day Creator Weekly Schedule",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite12,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        val schedule = listOf(
            ScheduleDay12("Monday", "Research & Scripting", "Find trending audio, Wishlink products, write 3 hooks.", Icons.Default.Lightbulb),
            ScheduleDay12("Tuesday", "Batch Recording", "Shoot 3-4 videos in natural daylight lighting.", Icons.Default.Edit),
            ScheduleDay12("Wednesday", "Editing & Captions", "Trim cuts, add subtitles, generate Wishlink hashtags.", Icons.Default.AutoAwesome),
            ScheduleDay12("Thursday", "Publishing & Engagement", "Post Reel #1, run 5-story sequence, reply to comments.", Icons.Default.Send),
            ScheduleDay12("Friday", "Analytics & Store Refresh", "Check click-through rates, swap out-of-stock links.", Icons.Default.Analytics),
            ScheduleDay12("Saturday", "Rest & Creative Recharge", "Take a complete mental rest day to prevent burnout.", Icons.Default.Schedule)
        )

        schedule.forEach { s ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary12), RoundedCornerShape(14.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = s.icon, contentDescription = null, tint = GoldAccent12, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "${s.day}: ${s.task}", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite12)
                        Text(text = s.detail, fontSize = 11.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton12(text = "Run Business Health Check →", onClick = onContinue)
    }
}

private data class ScheduleDay12(val day: String, val task: String, val detail: String, val icon: ImageVector)

/**
 * MODULE 6: Business Health Check
 */
@Composable
private fun Level12Module6HealthCheckView(
    qConsistency: Boolean,
    onConsistencyChange: (Boolean) -> Unit,
    qStoreUpdated: Boolean,
    onStoreUpdatedChange: (Boolean) -> Unit,
    qAnalyticsReviewed: Boolean,
    onAnalyticsReviewedChange: (Boolean) -> Unit,
    qProductsRefreshed: Boolean,
    onProductsRefreshedChange: (Boolean) -> Unit,
    qAudienceEngaged: Boolean,
    onAudienceEngagedChange: (Boolean) -> Unit,
    score: Int,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level12ModuleBadge(moduleNum = 6, title = "Business Health Check")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Assess Your Creator Business Score (0-100)",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite12,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // SCORE DISPLAY
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color(0x33C084FC))
                .border(BorderStroke(3.dp, GoldAccent12), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "$score", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = GoldAccent12)
                Text(text = "HEALTH SCORE", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextWhite12)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val questions = listOf(
            Pair("Posting Consistency maintained (3-4 posts/week)?", qConsistency to onConsistencyChange),
            Pair("Wishlink Store updated with recent links?", qStoreUpdated to onStoreUpdatedChange),
            Pair("Weekly Analytics & Click rates reviewed?", qAnalyticsReviewed to onAnalyticsReviewedChange),
            Pair("Out-of-stock products swapped or updated?", qProductsRefreshed to onProductsRefreshedChange),
            Pair("Audience comments & DMs replied consistently?", qAudienceEngaged to onAudienceEngagedChange)
        )

        questions.forEach { (text, statePair) ->
            val (value, onChange) = statePair
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (value) Color(0x44C084FC) else Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, if (value) GoldAccent12 else Color(0x33C084FC)), RoundedCornerShape(12.dp))
                    .clickable { onChange(!value) }
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (value) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (value) GoldAccent12 else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = text, fontSize = 12.sp, color = TextWhite12)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton12(text = "Learn Risk Management →", onClick = onContinue)
    }
}

/**
 * MODULE 7: Risk Management
 */
@Composable
private fun Level12Module7RiskManagementView(
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level12ModuleBadge(moduleNum = 7, title = "Creator Risk Management")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Protect Your Business Against Algorithm & Link Risks",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite12,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        val risks = listOf(
            RiskItem12("1. Platform Dependency", "Never rely on 1 social platform alone. Build a Telegram community or email list.", Icons.Default.Security),
            RiskItem12("2. Policy & Guidelines", "Follow community guidelines strictly to prevent shadowbans or account restrictions.", Icons.Default.Warning),
            RiskItem12("3. Link Maintenance", "Audit broken or expired Wishlink links weekly so buyers never land on dead pages.", Icons.Default.Speed),
            RiskItem12("4. Content Data Backup", "Store original raw video clips and thumbnail project files in cloud storage.", Icons.Default.TaskAlt),
            RiskItem12("5. Professional Communication", "Keep written email records for all brand deals and partnership agreements.", Icons.Default.Handshake)
        )

        risks.forEach { r ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary12), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = r.icon, contentDescription = null, tint = GoldAccent12, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = r.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite12)
                        Text(text = r.desc, fontSize = 11.5.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton12(text = "Ask AI Business Advisor →", onClick = onContinue)
    }
}

private data class RiskItem12(val title: String, val desc: String, val icon: ImageVector)

/**
 * MODULE 8: AI Business Advisor
 */
@Composable
private fun Level12Module8AiAdvisorView(
    userQuery: String,
    onUserQueryChange: (String) -> Unit,
    aiResponse: String,
    isThinking: Boolean,
    onAskAdvisor: () -> Unit,
    onResponseReady: () -> Unit,
    onContinue: () -> Unit
) {
    LaunchedEffect(isThinking) {
        if (isThinking) {
            delay(1200)
            onResponseReady()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level12ModuleBadge(moduleNum = 8, title = "AI Business Advisor")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Ask Strategic Growth Questions",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite12,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = userQuery,
            onValueChange = onUserQueryChange,
            label = { Text("Your Business Question", color = PurplePrimary12) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary12,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite12,
                unfocusedTextColor = TextWhite12
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(GoldAccent12)
                .clickable { onAskAdvisor() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = if (isThinking) "Analyzing Your Business..." else "Get AI Advice ✨", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (aiResponse.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x332B0A4D))
                    .border(BorderStroke(1.dp, PurplePrimary12), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Psychology, contentDescription = null, tint = GoldAccent12, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Advisor Recommendation", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite12)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = aiResponse, fontSize = 12.sp, color = TextWhite12, lineHeight = 17.sp)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton12(text = "Review Business Mistakes →", onClick = onContinue)
    }
}

/**
 * MODULE 9: Business Mistakes
 */
@Composable
private fun Level12Module9BusinessMistakesView(
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level12ModuleBadge(moduleNum = 9, title = "6 Fatal Creator Business Mistakes")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Pitfalls That Stop Creators From Scaling",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite12,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        val mistakes = listOf(
            MistakeItem12("1. Only Chasing Trends", "Ignoring core niche value to post random viral audio trends."),
            MistakeItem12("2. Ignoring Audience", "Never replying to comments or DMs destroys buyer trust."),
            MistakeItem12("3. No Weekly Planning", "Random posting without content calendars leads to fast burnout."),
            MistakeItem12("4. Poor Consistency", "Posting 5 times in 1 week then ghosting for 3 weeks."),
            MistakeItem12("5. No Analytics Review", "Not checking click rates means repeating low-converting videos."),
            MistakeItem12("6. No Portfolio / Store", "Failing to maintain an updated Wishlink store front.")
        )

        mistakes.forEach { m ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, Color(0xFFFF5252)), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = m.title, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite12)
                        Text(text = m.desc, fontSize = 11.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton12(text = "Complete Today's Mission →", onClick = onContinue)
    }
}

private data class MistakeItem12(val title: String, val desc: String)

/**
 * MODULE 10: Today's Mission & Achievement
 */
@Composable
private fun Level12Module10MissionAchievementView(
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
        Level12ModuleBadge(moduleNum = 10, title = "Today's Mission & Achievement")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Build Your Complete Creator Business Plan",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite12,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // MISSION BOX
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x332B0A4D))
                .border(BorderStroke(1.dp, PurplePrimary12), RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.TaskAlt, contentDescription = null, tint = GoldAccent12, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "TODAY'S MISSION", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldAccent12)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Finalize your Weekly Business Planner, set your Monthly Goal, and complete your Business Health Check.",
                    fontSize = 12.sp,
                    color = TextWhite12
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Estimated Time: 20 Minutes", fontSize = 10.5.sp, color = Color.LightGray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // BADGE BOX
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0x55C084FC),
                            Color(0x222B0A4D)
                        )
                    )
                )
                .border(
                    BorderStroke(1.5.dp, if (isUnlocked) GoldAccent12 else PurplePrimary12),
                    RoundedCornerShape(20.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(if (isUnlocked) GoldAccent12 else Color(0x33FFFFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (isUnlocked) Color.Black else GoldAccent12,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Creator Business Builder",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite12
                )

                Text(
                    text = "+800 XP REWARD",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent12
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (!isUnlocked) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(GoldAccent12)
                            .clickable { onUnlockAchievement() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Claim Badge & +800 XP ✨",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Achievement Unlocked!", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (isUnlocked) {
            GlassShineButton12(
                text = "Complete Level 12 →",
                onClick = onCompleteLevel
            )
        }
    }
}
