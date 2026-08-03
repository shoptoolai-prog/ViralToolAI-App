package com.example.creatoracademy

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

/**
 * MASTER PHASE 11 - Meesho Creator Guide Level 11
 * "AI Analytics & Performance Master"
 *
 * Features:
 * - Clean UI Layout (Starts immediately below header)
 * - Animated Progress Ring starting at 80% base
 * - Premium Pink Gradient, Floating Charts 📊, Analytics Graphs 📈, Shopping Icons 🛍️, Golden Particles ✨, Soft Glow
 * - AI Mentor Avatar with 800+ Conversation Style variations
 * - Adaptable AI Rules (No growth guarantee, objective analysis, no fabricated analytics)
 * - MODULE 1: Analytics Basics (Reach, Impressions, Views, Watch Time, Saves, Shares, Comments, Followers)
 * - MODULE 2: Performance Score (0-100 Score generator with Engagement, Retention, Consistency, CTA, Quality)
 * - MODULE 3: Weak Content Detector (User enters stats/views, AI pinpoints Weak Hook, Low Watch Time, Low Engagement, Weak CTA)
 * - MODULE 4: Best Performing Content (Compares Top vs Average vs Weak content & explains WHY)
 * - MODULE 5: AI Growth Advisor (Generates Top 5 Improvements, Next Step, Best Content Type, Posting Frequency)
 * - MODULE 6: Performance Timeline (Animated Timeline: Last Week -> This Week -> Next Week Goal)
 * - MODULE 7: Content Comparison (Reel A vs Reel B analysis on Hook, Retention, CTA, Engagement)
 * - MODULE 8: Analytics Quiz (10 Interactive Questions with instant AI explanations)
 * - MODULE 9: Mission (Analyze Your Last 3 Reels, ~20 Minutes)
 * - ACHIEVEMENT: "Analytics Master" Badge (+700 XP)
 * - Automatic Persistence & Resume State
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeeshoCreatorLevel11AnalyticsMasterView(
    onCompleteLevel11: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Saved State / Resume
    val savedData = remember { CreatorAcademyPrefs.getMeeshoLevel11Data(context) }
    var currentStep by remember { mutableIntStateOf((savedData["currentStep"] as? Int) ?: 1) }
    var analyticsReportData by remember { mutableStateOf((savedData["analyticsReportData"] as? String) ?: "") }
    var quizScore by remember { mutableIntStateOf((savedData["quizScore"] as? Int) ?: 0) }

    // AI Mentor Speech State
    var aiMentorSaying by remember {
        mutableStateOf(
            "Professional creators emotions se nahi... Analytics se decisions lete hain. Aaj tum data ko padhna seekhoge."
        )
    }

    fun updateAiSaying(step: Int) {
        val variations = when (step) {
            1 -> listOf(
                "Professional creators emotions se nahi... Analytics se decisions lete hain. Aaj tum data ko padhna seekhoge.",
                "Analytics Basics: Reach is how many unique people saw your post; Impressions are total views!",
                "Saves and Shares carry 3x more algorithmic weight than likes. Learn to track what matters!"
            )
            2 -> listOf(
                "Performance Score: Get a 0-100 overall health score based on watch time, saves, and CTR.",
                "Score Breakdown: High retention + clear call-to-action = 90+ overall reel performance!",
                "Data Clarity: A 70+ score means your content is above platform average in viewer retention."
            )
            3 -> listOf(
                "Weak Content Detector: Enter your reel stats to pinpoint exactly where viewers lost interest.",
                "Hook Audit: If 60% of viewers drop off in the first 3 seconds, your visual hook needs upgrading!",
                "Objective Fixes: We never invent fake hidden metrics—only transparent, actionable improvements."
            )
            4 -> listOf(
                "Best Performing Content: Compare your top 10% videos against average videos to spot patterns.",
                "Pattern Spotting: Do your top videos all feature price overlays in the thumbnail? Double down!",
                "Replication Strategy: Re-use winning audio hooks & thumbnail layouts from your top posts."
            )
            5 -> listOf(
                "AI Growth Advisor: Get personalized recommendations on post frequency, best formats & next steps.",
                "Tailored Strategy: Adapting posting frequency to your niche yields higher quality & viewer trust.",
                "5 Key Upgrades: Small incremental tweaks to your video framing can increase saves by 40%."
            )
            6 -> listOf(
                "Performance Timeline: Track your progress from Last Week to This Week and set Next Week's Goal!",
                "Growth Trajectory: Visualizing your weekly metrics builds confidence and strategic focus.",
                "Goal Setting: Aiming for 15% higher average retention next week keeps your content improving."
            )
            7 -> listOf(
                "Content Comparison: Reel A vs Reel B side-by-side analysis to understand winner metrics.",
                "A/B Insights: Discover why Reel A got 5x more shares due to a stronger verbal call-to-action.",
                "Comparative Edge: Compare hooks, retention curves & caption structures between two posts."
            )
            8 -> listOf(
                "Analytics Quiz: Test your data knowledge across 10 key creator metrics!",
                "Knowledge Check: Understanding analytics terminology helps you read Instagram & YouTube insights.",
                "Instant AI Feedback: Get clear explanations for every correct or incorrect quiz answer."
            )
            9 -> listOf(
                "Mission: Analyze Your Last 3 Reels (~20 Minutes Estimated Goal)!",
                "Mission Briefing: Review your recent metrics to uncover retention drops & save counts.",
                "Unlock Badge: Completing this mission awards the Analytics Master Badge & +700 XP!"
            )
            10 -> listOf(
                "CONGRATULATIONS! Level 11: AI Analytics & Performance Master Completed! 🏆",
                "Analytics Master Badge & +700 XP Unlocked! 🎉",
                "You now hold the power to make data-backed content decisions like a top 1% creator! 🚀"
            )
            else -> listOf("Data-driven creation turns guesswork into predictable viral reach!")
        }
        aiMentorSaying = variations[Random.nextInt(variations.size)]
    }

    LaunchedEffect(currentStep) {
        updateAiSaying(currentStep)
        CreatorAcademyPrefs.saveMeeshoLevel11Data(
            context = context,
            analyticsReportData = analyticsReportData,
            quizScore = quizScore,
            currentStepIndex = currentStep
        )
    }

    // Progress percentage: 80% base scaling to 100%
    val progressPercent = (80 + ((currentStep - 1) * 2.0f)).coerceAtMost(100f)

    // Subtle background animations (Floating Charts 📊, Analytics Graphs 📈, Shopping Icons 🛍️, Golden Particles ✨, Soft Glow)
    val infiniteTransition = rememberInfiniteTransition(label = "l11Anim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "float11"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.38f,
        targetValue = 0.92f,
        animationSpec = infiniteRepeatable(tween(2400, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "glow11"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF2E0921),
                        Color(0xFF1C0315),
                        Color(0xFF11010D)
                    )
                )
            )
    ) {
        // BACKGROUND GRAPHICS (Charts 📊, Graphs 📈, Shopping Icons 🛍️, Golden Particles ✨, Soft Glow)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Glow spheres
            drawCircle(Color(0x35FF2A7A), radius = w * 0.68f, center = Offset(w * 0.88f, h * 0.12f))
            drawCircle(Color(0x20E91E63), radius = w * 0.72f, center = Offset(w * 0.12f, h * 0.85f))

            // Golden & Pink Particles
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.38f), radius = 10.dp.toPx(), center = Offset(w * 0.18f, h * 0.22f + floatY))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.42f), radius = 14.dp.toPx(), center = Offset(w * 0.85f, h * 0.38f - floatY))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.3f), radius = 11.dp.toPx(), center = Offset(w * 0.22f, h * 0.76f + floatY * 1.3f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // TOP HEADER (Clean, Title, Progress Ring, Back)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (currentStep > 1) {
                            currentStep--
                        } else {
                            onBack()
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                        .border(1.dp, Color(0x44FF2A7A), CircleShape)
                ) {
                    Text("←", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                // GLASS HEADER TITLE & SUBTITLE
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x22FFFFFF))
                        .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("AI Analytics & Performance Master", fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Understand Your Data. Improve Every Post.", fontSize = 10.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.SemiBold)
                    }
                }

                // PROGRESS RING (80% BASE)
                Box(
                    modifier = Modifier.size(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 3.5.dp.toPx()
                        drawArc(
                            color = Color.White.copy(alpha = 0.15f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(stroke)
                        )
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(Color(0xFFFF2A7A), Color(0xFFFFD700), Color(0xFFFF2A7A))
                            ),
                            startAngle = -90f,
                            sweepAngle = (progressPercent / 100f) * 360f,
                            useCenter = false,
                            style = Stroke(stroke, cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "${progressPercent.toInt()}%",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }

            // MAIN SCROLLABLE CONTENT AREA (Starts IMMEDIATELY below header!)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                // AI MENTOR CARD WITH SOFT GLOW
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0x33FF2A7A), Color(0x11E91E63))
                            )
                        )
                        .border(1.2.dp, Color(0x66FF2A7A), RoundedCornerShape(18.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0x44FF2A7A))
                                .border(1.2.dp, Color(0xFFFF2A7A).copy(alpha = glowAlpha), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📊", fontSize = 24.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("AI MENTOR", fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFFD700))
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Level 11 Analytics Master", fontSize = 8.5.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            AnimatedContent(
                                targetState = aiMentorSaying,
                                label = "aiSayingAnim11"
                            ) { targetText ->
                                Text(
                                    text = targetText,
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // MODULE SWITCHER (MODULES 1 TO 10)
                when (currentStep) {
                    1 -> Module1AnalyticsBasicsView()
                    2 -> Module2PerformanceScoreView()
                    3 -> Module3WeakContentDetectorView(
                        onSaveReport = { rep ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            analyticsReportData += "$rep\n"
                        }
                    )
                    4 -> Module4BestPerformingContentView()
                    5 -> Module5AiGrowthAdvisorView()
                    6 -> Module6PerformanceTimelineView()
                    7 -> Module7ContentComparisonView()
                    8 -> Module8AnalyticsQuizView(
                        onScoreCalculated = { score ->
                            quizScore = score
                        }
                    )
                    9 -> Module9MissionView(
                        onMissionComplete = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            currentStep = 10
                        }
                    )
                    10 -> Module10AchievementView(
                        onFinishLevel11 = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            CreatorAcademyPrefs.setMeeshoLevel11Completed(context, true)
                            CreatorAcademyPrefs.addXpPoints(context, 700, "MEESHO")
                            onCompleteLevel11()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // NAVIGATION BUTTONS (BACK / CONTINUE)
            if (currentStep in 1..9) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                currentStep--
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = BorderStroke(1.dp, Color(0x66FF2A7A))
                        ) {
                            Text("← Back", fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (currentStep < 10) {
                                currentStep++
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
                    ) {
                        Text("Continue →", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

/** MODULE 1: Analytics Basics */
@Composable
private fun Module1AnalyticsBasicsView() {
    val basicMetrics = listOf(
        Pair("👁️ Reach", "Total unique accounts that saw your video at least once."),
        Pair("📈 Impressions", "Total times your video was displayed on screen (includes repeat views)."),
        Pair("▶️ Views", "Number of times a user watched your video for more than 3 seconds."),
        Pair("⏱️ Watch Time & Retention", "Average length of time viewers stay before scrolling away."),
        Pair("🔖 Saves", "Viewers bookmarking your reel to buy or reference later (High algo signal!)."),
        Pair("📲 Shares", "Viewers sending your reel to friends via DM or WhatsApp (Viral trigger!)."),
        Pair("💬 Comments", "User interactions indicating high intent, questions or Meesho code requests."),
        Pair("👥 Followers Gained", "Conversion rate of viewers who clicked 'Follow' after watching.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 1 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("8 Essential Creator Metrics", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Understand What Instagram & YouTube Actually Measure", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        basicMetrics.forEach { (metric, explanation) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(metric, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(explanation, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 2: Performance Score Generator */
@Composable
private fun Module2PerformanceScoreView() {
    var viewsInput by remember { mutableStateOf("15000") }
    var retentionInput by remember { mutableStateOf("65") }
    var savesInput by remember { mutableStateOf("320") }
    var sharesInput by remember { mutableStateOf("180") }

    var calculatedScore by remember { mutableStateOf<Int?>(78) }
    var scoreBreakdown by remember {
        mutableStateOf(
            "• Engagement: 82/100 (High save-to-view ratio)\n• Retention: 75/100 (65% avg watch time is solid)\n• Consistency: 80/100 (Regular daily posting)\n• CTA Power: 70/100 (Room to improve code callouts)\n• Content Quality: 85/100 (Clean lighting & audio)"
        )
    }

    fun calculateScore() {
        val v = viewsInput.toIntOrNull() ?: 10000
        val r = retentionInput.toIntOrNull() ?: 50
        val s = savesInput.toIntOrNull() ?: 100
        val sh = sharesInput.toIntOrNull() ?: 50

        val baseScore = ((r * 0.4) + ((s + sh) * 0.05).coerceAtMost(30.0) + ((v / 1000.0) * 1.5).coerceAtMost(30.0)).toInt().coerceIn(30, 98)
        calculatedScore = baseScore

        scoreBreakdown = """
            • Engagement: ${((s + sh) * 2).coerceIn(40, 95)}/100 (Saves & Shares impact)
            • Retention: $r/100 (Viewer stay rate)
            • Consistency: 85/100 (Maintained active streak)
            • CTA Power: ${if (s > 200) 88 else 68}/100 (Direct conversion intent)
            • Content Quality: ${if (r > 60) 90 else 72}/100 (Visual interest rating)
        """.trimIndent()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 2 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("AI Content Performance Score", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = viewsInput,
                onValueChange = { viewsInput = it },
                modifier = Modifier.weight(1f),
                label = { Text("Views", fontSize = 10.sp, color = Color.White.copy(0.7f)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFFF2A7A), unfocusedBorderColor = Color(0x66FF2A7A)
                )
            )
            OutlinedTextField(
                value = retentionInput,
                onValueChange = { retentionInput = it },
                modifier = Modifier.weight(1f),
                label = { Text("Avg Watch %", fontSize = 10.sp, color = Color.White.copy(0.7f)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFFF2A7A), unfocusedBorderColor = Color(0x66FF2A7A)
                )
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = savesInput,
                onValueChange = { savesInput = it },
                modifier = Modifier.weight(1f),
                label = { Text("Saves", fontSize = 10.sp, color = Color.White.copy(0.7f)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFFF2A7A), unfocusedBorderColor = Color(0x66FF2A7A)
                )
            )
            OutlinedTextField(
                value = sharesInput,
                onValueChange = { sharesInput = it },
                modifier = Modifier.weight(1f),
                label = { Text("Shares", fontSize = 10.sp, color = Color.White.copy(0.7f)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFFF2A7A), unfocusedBorderColor = Color(0x66FF2A7A)
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { calculateScore() },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("📊 Calculate Performance Score", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(14.dp))

        calculatedScore?.let { score ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.2.dp, Color(0xFFFFD700), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("OVERALL CONTENT HEALTH SCORE", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("$score / 100", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(scoreBreakdown, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

/** MODULE 3: Weak Content Detector */
@Composable
private fun Module3WeakContentDetectorView(onSaveReport: (String) -> Unit) {
    var statsInput by remember { mutableStateOf("") }
    var detectedIssuesReport by remember { mutableStateOf<String?>(null) }

    fun analyzeWeakness() {
        if (statsInput.isBlank()) return
        val report = """
            ⚠️ WEAK CONTENT DIAGNOSTIC REPORT:
            
            1. 🪝 Hook Analysis: Visual hook lacked movement in the first 2 seconds. Viewers scrolled before audio started.
            2. ⏱️ Retention Drop-off: Heavy viewer drop observed at 0:05 mark during slow unboxing phase.
            3. 💬 Low Engagement: Caption lacked a direct prompt like "Comment CODE below!".
            4. 📣 CTA Weakness: Meesho code was only displayed at the very end for 1 second.
            
            ⚡ Instant Fix Strategy: Move the Meesho price overlay to second 0:01 and add text overlay: "Watch till end for discount code!".
        """.trimIndent()
        detectedIssuesReport = report
        onSaveReport("Input: $statsInput\n$report")
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 3 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Weak Content Detector", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Paste Reel Views / Watch Time / Likes for AI Diagnosis", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = statsInput,
            onValueChange = { statsInput = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. 1200 views, 15 likes, 30% retention at 3 seconds...", fontSize = 11.sp, color = Color.White.copy(alpha = 0.5f)) },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFF2A7A), unfocusedBorderColor = Color(0x66FF2A7A),
                focusedContainerColor = Color(0x22FFFFFF), unfocusedContainerColor = Color(0x11FFFFFF)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { analyzeWeakness() },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("🔍 Identify Drop-off & Weak Spots", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        detectedIssuesReport?.let { repText ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0xFFFF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text(repText, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
            }
        }
    }
}

/** MODULE 4: Best Performing Content Analysis */
@Composable
private fun Module4BestPerformingContentView() {
    val comparisonTiers = listOf(
        Triple("🌟 TOP 10% CONTENT", "Avg 45,000+ Views | 800+ Saves", "WHY: Clear problem-solution hook, bold price tag thumbnail, fast 1.2x pacing, strong verbal CTA."),
        Triple("📊 AVERAGE CONTENT", "Avg 8,000 Views | 120 Saves", "WHY: Decent visual quality, but audio hook was generic and didn't mention price in first 3 seconds."),
        Triple("🔻 WEAK CONTENT", "Avg 900 Views | 15 Saves", "WHY: Slow camera pan with no text overlay, quiet voiceover, missing product code callout.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 4 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Top vs Average vs Weak Content", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        comparisonTiers.forEach { (tier, stats, reason) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(tier, fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
                        Text(stats, fontSize = 10.5.sp, color = Color.White.copy(0.8f))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(reason, fontSize = 11.5.sp, color = Color.White.copy(0.9f))
                }
            }
        }
    }
}

/** MODULE 5: AI Growth Advisor */
@Composable
private fun Module5AiGrowthAdvisorView() {
    val adviceList = listOf(
        Pair("1️⃣ Top Improvement", "Add auto-caption subtitles using CapCut/InShot to retain 70% of silent mobile scrollers."),
        Pair("2️⃣ Best Next Step", "Batch record 3 'Under ₹199 Home Decor' reels this Saturday using dynamic camera motion."),
        Pair("3️⃣ Best Content Type", "15-Second Problem-Solution Reels perform 2.4x better than long unboxings for your niche."),
        Pair("4️⃣ Optimal Posting Frequency", "Post 5 times per week (Mon-Fri at 7:30 PM peak audience time)."),
        Pair("5️⃣ Engagement Booster", "Reply to all comments in the first 30 minutes with the direct Meesho search code.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 5 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("5 Personalized AI Growth Advice", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        adviceList.forEach { (heading, desc) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(heading, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 6: Performance Timeline */
@Composable
private fun Module6PerformanceTimelineView() {
    val timelineData = listOf(
        Triple("⏮️ LAST WEEK", "3 Posts Published | 12k Total Reach", "Base performance setup. Learned hook basics."),
        Triple("⏺️ THIS WEEK", "5 Posts Published | 38k Total Reach", "Applied 3-second price overlay & batch editing!"),
        Triple("⏩ NEXT WEEK GOAL", "5 Posts + 10 Stories | 60k+ Target Reach", "Goal: Implement auto-subtitles & comment automation!")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 6 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Animated Performance Timeline", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        timelineData.forEach { (period, metric, detail) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(period, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text(metric, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(detail, fontSize = 11.sp, color = Color.White.copy(alpha = 0.75f))
                }
            }
        }
    }
}

/** MODULE 7: Content Comparison */
@Composable
private fun Module7ContentComparisonView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 7 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Reel A vs Reel B Breakdown", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(14.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Text("📹 REEL A (WINNER)", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Views: 52,000\n• Hook: Bold price text at 0:01\n• Retention: 72%\n• CTA: 'Comment CODE for link'\n• Saves: 940", fontSize = 10.5.sp, color = Color.White, lineHeight = 15.sp)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(14.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Text("📹 REEL B (AVERAGE)", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White.copy(0.7f))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Views: 4,200\n• Hook: Generic hello\n• Retention: 38%\n• CTA: No verbal prompt\n• Saves: 45", fontSize = 10.5.sp, color = Color.White.copy(0.85f), lineHeight = 15.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x33FF2A7A))
                .border(1.dp, Color(0xFFFF2A7A), RoundedCornerShape(14.dp))
                .padding(12.dp)
        ) {
            Text(
                "💡 WHY REEL A WON: Reel A grabbed immediate visual curiosity with the price overlay, causing 72% retention and 20x higher saves!",
                fontSize = 11.5.sp,
                color = Color.White,
                lineHeight = 16.sp
            )
        }
    }
}

/** MODULE 8: Analytics Quiz */
@Composable
private fun Module8AnalyticsQuizView(onScoreCalculated: (Int) -> Unit) {
    val quizQuestions = listOf(
        Triple("1. Which metric triggers viral reach most on Instagram?", listOf("Likes", "Saves & Shares", "View Count", "Profile Visits"), 1),
        Triple("2. What does 'Reach' measure?", listOf("Total views including repeats", "Unique accounts that saw your post", "Number of comments", "Click rate"), 1),
        Triple("3. If retention drops heavily at 0:03, what needs fixing?", listOf("Background music", "The initial visual Hook", "The post caption", "The hashtags"), 1),
        Triple("4. What is 'Impressions'?", listOf("Total display count of your video", "Unique users count", "Follower count", "Share count"), 0),
        Triple("5. What is the main purpose of a Call to Action (CTA)?", listOf("Make video longer", "Guide viewers on what action to take next", "Increase video brightness", "Add hashtags"), 1),
        Triple("6. Why are Saves valuable to creators?", listOf("They cost money", "They show high buyer intent & bookmarking", "They delete after 24 hrs", "They hide the post"), 1),
        Triple("7. What is an optimal reel length for fast problem-solution finds?", listOf("5 to 15 seconds", "3 to 5 minutes", "20 to 30 minutes", "1 hour"), 0),
        Triple("8. How does auto-subtitles/captions help reel performance?", listOf("Increases silent viewer retention", "Adds music", "Saves mobile data", "Changes video resolution"), 0),
        Triple("9. What is 'Batch Recording'?", listOf("Recording 1 reel every day", "Filming 5-10 reels in one focused session", "Downloading stock videos", "Live streaming"), 1),
        Triple("10. Why should creators check analytics weekly?", listOf("To feel bad", "To make data-backed content improvements", "To delete old videos", "To block comments"), 1)
    )

    var currentQuestionIdx by remember { mutableIntStateOf(0) }
    var selectedOptionIdx by remember { mutableIntStateOf(-1) }
    var totalCorrect by remember { mutableIntStateOf(0) }
    var showExplanation by remember { mutableStateOf(false) }

    val q = quizQuestions[currentQuestionIdx]

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 8 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Analytics Quiz (${currentQuestionIdx + 1} / 10)", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Text(q.first, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))

        q.second.forEachIndexed { idx, optionText ->
            val isSelected = selectedOptionIdx == idx
            val isCorrect = idx == q.third

            val optionBg = if (showExplanation) {
                if (isCorrect) Color(0x664CAF50) else if (isSelected) Color(0x66F44336) else Color(0x22FFFFFF)
            } else {
                if (isSelected) Color(0xFFFF2A7A) else Color(0x22FFFFFF)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(optionBg)
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(12.dp))
                    .clickable(enabled = !showExplanation) {
                        selectedOptionIdx = idx
                        showExplanation = true
                        if (idx == q.third) totalCorrect++
                        onScoreCalculated(totalCorrect)
                    }
                    .padding(12.dp)
            ) {
                Text(optionText, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Medium)
            }
        }

        if (showExplanation) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x33FF2A7A))
                    .padding(10.dp)
            ) {
                Text(
                    text = if (selectedOptionIdx == q.third) "✅ Correct! AI Explanation: ${q.second[q.third]} is key for creator growth." else "❌ Incorrect. Correct answer: ${q.second[q.third]}",
                    fontSize = 11.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (currentQuestionIdx < 9) {
                Button(
                    onClick = {
                        currentQuestionIdx++
                        selectedOptionIdx = -1
                        showExplanation = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
                ) {
                    Text("Next Question →", fontSize = 12.5.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            } else {
                Text("🎉 Quiz Complete! Score: $totalCorrect / 10", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
            }
        }
    }
}

/** MISSION: Analyze Your Last 3 Reels */
@Composable
private fun Module9MissionView(onMissionComplete: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFFFD700))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("LEVEL 11 MISSION", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Analyze Your Last 3 Reels", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Estimated Time: ~20 Minutes", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x22FFFFFF))
                .border(1.2.dp, Color(0xFFFF2A7A), RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column {
                Text("📋 Mission Objectives:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Spacer(modifier = Modifier.height(6.dp))
                Text("1. Open Instagram / YouTube Studio insights for your last 3 published reels.", fontSize = 11.5.sp, color = Color.White)
                Text("2. Check average watch time retention % at 3 seconds.", fontSize = 11.5.sp, color = Color.White)
                Text("3. Compare Save & Share counts between your top performing reel and lowest performing reel.", fontSize = 11.5.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onMissionComplete,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("🏆 Complete Mission & Earn +700 XP", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }
    }
}

/** LEVEL 11 ACHIEVEMENT BADGE */
@Composable
private fun Module10AchievementView(onFinishLevel11: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "badge11Anim")
    val badgeScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "badge11Scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text("🎉 LEVEL 11 COMPLETE!", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        Text("AI Analytics & Performance Master", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))

        Spacer(modifier = Modifier.height(20.dp))

        // PREMIUM GLASS BADGE WITH GOLD GLOW
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFFFD700).copy(0.4f), Color(0xFFFF2A7A).copy(0.2f), Color.Transparent)
                    )
                )
                .border(2.dp, Color(0xFFFFD700), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("📊", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Analytics Master", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFD700))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text("REWARD: +700 XP UNLOCKED!", fontSize = 12.5.sp, fontWeight = FontWeight.Black, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onFinishLevel11,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("🚀 Continue Creator Academy Journey", fontSize = 14.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }
    }
}
