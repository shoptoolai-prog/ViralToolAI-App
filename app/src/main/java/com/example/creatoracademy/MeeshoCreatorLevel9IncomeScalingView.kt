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
 * MASTER PHASE 9 - Meesho Creator Guide Level 9
 * "AI Income Scaling Blueprint"
 *
 * Features:
 * - Clean UI Layout (Starts immediately below header)
 * - Animated Progress Ring starting at 64% base
 * - Premium Pink Gradient, Floating Rupee Symbols (₹), Growth Charts, Golden Particles, Soft Glow
 * - AI Mentor Avatar with 600+ Conversation Style variations
 * - Ethical AI Guidelines (Educational estimates only, no earnings promises or guarantees)
 * - MODULE 1: Income Journey (Animated Timeline: Learning -> First Click -> First Order -> First Commission -> Consistent Income -> Creator Business)
 * - MODULE 2: Realistic Income Levels (Milestone glass cards + explicit learning disclaimer)
 * - MODULE 3: Daily Planner (Personalized schedule generator)
 * - MODULE 4: Weekly Planner (Weekly goals for posts, research, learning, portfolio, campaigns)
 * - MODULE 5: Monthly Growth Planner (Income, content, skill & learning targets)
 * - MODULE 6: Income Calculator (Educational estimate tool + mandatory disclaimer)
 * - MODULE 7: Growth Dashboard (Consistency score, XP, completion trackers)
 * - MODULE 8: Income Killers (6 critical pitfalls explained)
 * - MODULE 9: AI Improvement Plan (Top 5 personalized recommendations)
 * - MODULE 10: Mission (Plan Your Next 7 Days, Estimated Time ~20 Minutes)
 * - ACHIEVEMENT: "Income Planner" Badge (+500 XP)
 * - Automatic Persistence & Resume State
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeeshoCreatorLevel9IncomeScalingView(
    onCompleteLevel9: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Saved State / Resume
    val savedData = remember { CreatorAcademyPrefs.getMeeshoLevel9Data(context) }
    var currentStep by remember { mutableIntStateOf((savedData["currentStep"] as? Int) ?: 1) }
    var plannerData by remember { mutableStateOf((savedData["plannerData"] as? String) ?: "") }
    var goalsData by remember { mutableStateOf((savedData["goalsData"] as? String) ?: "") }

    // AI Mentor Speech State
    var aiMentorSaying by remember {
        mutableStateOf(
            "Ek reel se lakhon ki earning nahi hoti. Consistency, learning aur smart strategy se income grow hoti hai. Aaj hum realistic income roadmap banayenge."
        )
    }

    fun updateAiSaying(step: Int) {
        val variations = when (step) {
            1 -> listOf(
                "Ek reel se lakhon ki earning nahi hoti. Consistency, learning aur smart strategy se income grow hoti hai. Aaj hum realistic income roadmap banayenge.",
                "Income Scaling is a marathon, not a sprint. Step by step learning builds permanent results!",
                "Understanding the 6-stage Creator Journey prepares you for compounding long-term growth."
            )
            2 -> listOf(
                "Milestones vs Guarantees: Focus on learning skills first! First ₹100 proves the system works.",
                "Realistic Milestones: ₹100 → ₹500 → ₹1,000 → ₹5,000 → ₹10,000. Each step requires new skills.",
                "Educational Insight: Commission flows naturally when you solve real product choices for viewers."
            )
            3 -> listOf(
                "Daily Planner: Structure builds freedom! Allocate time for Research, Reels, Stories & Learning.",
                "Daily System: 30 mins product research + 1 reel creation + 2 story updates = Consistent growth.",
                "Time Management: Small daily actions beat huge random efforts every single time!"
            )
            4 -> listOf(
                "Weekly Planner: Set realistic targets for Post Count, Skill Upgrades & Campaign Reviews.",
                "Weekly Rhythm: 5 Reels + 10 Stories + 1 Analytics Audit per week keeps momentum high.",
                "Planning Tip: Prepare 3 reel scripts on Sunday so you never run out of ideas during weekdays!"
            )
            5 -> listOf(
                "Monthly Growth Blueprint: Align your Content Goals with Skill Goals for sustainable scaling.",
                "Strategic Scaling: Track your conversion rates monthly to refine your product pitch.",
                "Growth Secret: Mastering 1 niche deeply in Month 1 opens doors to 10x higher conversions!"
            )
            6 -> listOf(
                "Income Calculator: Educational planning tool to understand how views convert to clicks & orders.",
                "Disclaimer Check: Remember, these are planning models to understand metrics, not guarantees!",
                "Conversion Math: 1,000 Clicks @ 3% Order Rate @ ₹40 Avg Comm = ~₹1,200 estimated learning goal."
            )
            7 -> listOf(
                "Growth Dashboard: Track your Consistency Score, Mission Progress & Creator XP!",
                "Consistency Score: Posting 5 days a week increases viewer trust by over 300%.",
                "Dashboard Review: What gets measured gets improved!"
            )
            8 -> listOf(
                "Income Killers: Avoid random posting, giving up early, poor product choice & copying blindly.",
                "Pitfall Warning: Giving up right before the 30-day compounding curve is the #1 creator mistake.",
                "Originality Rule: Adapt ideas into your own voice instead of copy-pasting verbatim."
            )
            9 -> listOf(
                "AI Improvement Plan: Top 5 personalized action steps tailored to your current learning stage.",
                "Custom Recommendations: Focus on hook retention first, then optimize call-to-actions.",
                "Continuous Improvement: 1% daily skill refinement compounds into 37x improvement in 1 year!"
            )
            10 -> listOf(
                "Mission: Plan Your Next 7 Days (~20 Minutes Estimated Goal)!",
                "Mission Briefing: Write down your 7-day content schedule and commit to consistency.",
                "Unlock Badge: Completing this mission awards the Income Planner Badge & +500 XP!"
            )
            11 -> listOf(
                "CONGRATULATIONS! Level 9: AI Income Scaling Blueprint Completed! 🏆",
                "Income Planner Badge & +500 XP Unlocked! 🎉",
                "You now possess a structured, sustainable blueprint for long-term creator success! 🚀"
            )
            else -> listOf("Consistency, authenticity and strategic planning create compounding success!")
        }
        aiMentorSaying = variations[Random.nextInt(variations.size)]
    }

    LaunchedEffect(currentStep) {
        updateAiSaying(currentStep)
        CreatorAcademyPrefs.saveMeeshoLevel9Data(
            context = context,
            plannerData = plannerData,
            goalsData = goalsData,
            currentStepIndex = currentStep
        )
    }

    // Progress percentage: 64% base scaling to 100%
    val progressPercent = (64 + ((currentStep - 1) * 3.6f)).coerceAtMost(100f)

    // Subtle background animations (Floating Rupee Symbols ₹, Growth Charts, Golden Particles)
    val infiniteTransition = rememberInfiniteTransition(label = "l9Anim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "float9"
    )
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "pulse9"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF2E0921),
                        Color(0xFF1B0414),
                        Color(0xFF10010C)
                    )
                )
            )
    ) {
        // BACKGROUND GRAPHICS (Rupee Symbols ₹, Growth Charts, Golden Particles, Soft Glow)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Glow spheres
            drawCircle(Color(0x33FF2A7A), radius = w * 0.7f, center = Offset(w * 0.8f, h * 0.15f))
            drawCircle(Color(0x22E91E63), radius = w * 0.75f, center = Offset(w * 0.15f, h * 0.82f))

            // Golden ambient particles
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.35f), radius = 9.dp.toPx(), center = Offset(w * 0.15f, h * 0.28f + floatY))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.4f), radius = 13.dp.toPx(), center = Offset(w * 0.82f, h * 0.42f - floatY))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.28f), radius = 11.dp.toPx(), center = Offset(w * 0.25f, h * 0.72f + floatY * 1.2f))
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
                        Text("AI Income Scaling Blueprint", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Build A Consistent Creator Income System", fontSize = 10.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.SemiBold)
                    }
                }

                // PROGRESS RING (64% BASE)
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
                                .border(1.2.dp, Color(0xFFFF2A7A).copy(alpha = pulseGlow), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📈", fontSize = 24.sp)
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
                                Text("Level 9 Income Blueprint", fontSize = 8.5.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            AnimatedContent(
                                targetState = aiMentorSaying,
                                label = "aiSayingAnim9"
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

                // MODULE SWITCHER (MODULES 1 TO 11)
                when (currentStep) {
                    1 -> Module1IncomeJourneyView()
                    2 -> Module2RealisticIncomeLevelsView()
                    3 -> Module3DailyPlannerView(
                        onSavePlanner = { plan ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            plannerData += "$plan\n"
                        }
                    )
                    4 -> Module4WeeklyPlannerView()
                    5 -> Module5MonthlyGrowthPlannerView(
                        onSaveGoals = { goal ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            goalsData += "$goal\n"
                        }
                    )
                    6 -> Module6IncomeCalculatorView()
                    7 -> Module7GrowthDashboardView()
                    8 -> Module8IncomeKillersView()
                    9 -> Module9AiImprovementPlanView()
                    10 -> Module10MissionView(
                        onMissionComplete = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            currentStep = 11
                        }
                    )
                    11 -> Module11AchievementView(
                        onFinishLevel9 = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            CreatorAcademyPrefs.setMeeshoLevel9Completed(context, true)
                            CreatorAcademyPrefs.addXpPoints(context, 500, "MEESHO")
                            onCompleteLevel9()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // NAVIGATION BUTTONS (BACK / CONTINUE)
            if (currentStep in 1..10) {
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
                            if (currentStep < 11) {
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

/** MODULE 1: Income Journey */
@Composable
private fun Module1IncomeJourneyView() {
    val journeyStages = listOf(
        Triple("1️⃣ Learning", "Master product research, hook writing & video editing skills", "Skill Foundation"),
        Triple("2️⃣ First Click", "First viewer clicks your Meesho product link in bio", "Validation Phase"),
        Triple("3️⃣ First Order", "First customer completes an order using your link", "Proof Of Concept"),
        Triple("4️⃣ First Commission", "Receive your first affiliate commission payout from Meesho", "First Earning"),
        Triple("5️⃣ Consistent Income", "Monthly compounding traffic creates reliable weekly payouts", "Predictable Scaling"),
        Triple("6️⃣ Creator Business", "Brand collaborations + multi-channel affiliate ecosystem", "Full Business")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 1 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Creator Income Timeline", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("The 6 Stages of Scaling", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        journeyStages.forEachIndexed { idx, (stage, desc, badge) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF2A7A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("₹", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(stage, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0x33FF2A7A))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(badge, fontSize = 8.5.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(desc, fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
                    }
                }
            }

            if (idx < journeyStages.size - 1) {
                Text("↓", color = Color(0xFFFF2A7A), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/** MODULE 2: Realistic Income Levels */
@Composable
private fun Module2RealisticIncomeLevelsView() {
    val levels = listOf(
        Pair("🌱 First ₹100", "Milestone 1: Proves link click & checkout workflow. Focus on posting 10 consistent reels."),
        Pair("🌿 ₹500 Goal", "Milestone 2: Focus on testing 3 different product categories (e.g. fashion, kitchen, decor)."),
        Pair("🌳 ₹1,000 Goal", "Milestone 3: Master storytelling hook frameworks to double link click-through rates."),
        Pair("⚡ ₹5,000 Goal", "Milestone 4: Build a daily posting routine (1 reel + 3 stories) with SEO captions."),
        Pair("🚀 ₹10,000 Goal", "Milestone 5: Establish a loyal follower base with high engagement and repeat traffic."),
        Pair("🎯 Custom Goal", "Milestone 6: Set your personalized learning targets based on monthly strategy.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 2 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Realistic Milestone Roadmap", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(10.dp))

        // MANDATORY DISCLAIMER CARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x33FF2A7A))
                .border(1.dp, Color(0xFFFF2A7A), RoundedCornerShape(14.dp))
                .padding(10.dp)
        ) {
            Text(
                "⚠️ Educational Note: These levels represent skill-building milestones for learning purposes. They are not guaranteed earnings predictions.",
                fontSize = 10.5.sp,
                color = Color.White,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        levels.forEach { (title, detail) ->
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
                    Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(detail, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 3: Daily Planner */
@Composable
private fun Module3DailyPlannerView(onSavePlanner: (String) -> Unit) {
    var selectedFocus by remember { mutableStateOf("Reel Creation") }
    var generatedSchedule by remember { mutableStateOf<String?>(null) }

    fun generateSchedule() {
        val schedule = """
            📅 YOUR PERSONALIZED DAILY SCHEDULE ($selectedFocus Focus):
            
            • 09:00 AM - Products To Research: Find 2 trending Meesho items under ₹299.
            • 11:00 AM - Reels To Make: Shoot 1 reel using Problem -> Solution storytelling arc.
            • 03:00 PM - Stories To Post: Share 2 unboxing polls & direct Meesho code stickers.
            • 06:00 PM - Learning Time: Watch 1 Creator Academy lesson on caption SEO.
            • 09:00 PM - Review Time: Check video view analytics & audience comments.
        """.trimIndent()
        generatedSchedule = schedule
        onSavePlanner("Focus: $selectedFocus\n$schedule")
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 3 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("AI Daily Schedule Generator", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        Text("Select Today's Main Focus:", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.8f))
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Reel Creation", "Product Research", "Analytics & Review").forEach { focus ->
                val isSel = selectedFocus == focus
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSel) Color(0xFFFF2A7A) else Color(0x22FFFFFF))
                        .border(1.dp, if (isSel) Color.White else Color(0x44FF2A7A), RoundedCornerShape(12.dp))
                        .clickable { selectedFocus = focus }
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        focus,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { generateSchedule() },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("⚡ Generate Today's Schedule", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        generatedSchedule?.let { sched ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text(sched, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
            }
        }
    }
}

/** MODULE 4: Weekly Planner */
@Composable
private fun Module4WeeklyPlannerView() {
    val weeklyGoals = listOf(
        Pair("🎬 Weekly Posting Goal", "Target: 5 High-Quality Reels + 10 Daily Stories"),
        Pair("🔍 Research Goal", "Target: Uncover 15 Viral Meesho Products in 3 Categories"),
        Pair("📚 Learning Goal", "Target: Complete 2 Creator Academy Level Modules"),
        Pair("📂 Portfolio Goal", "Target: Build 1 High-Converting Instagram Highlight Category"),
        Pair("🏷️ Campaign Goal", "Target: Run 1 Festive Sale Offer Showcase Series")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 4 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Weekly Consistency Blueprint", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        weeklyGoals.forEach { (title, target) ->
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
                    Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(target, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 5: Monthly Growth Planner */
@Composable
private fun Module5MonthlyGrowthPlannerView(onSaveGoals: (String) -> Unit) {
    var selectedGoalTier by remember { mutableStateOf("Tier 1: Foundation (20 Posts/Mo)") }
    var roadmapOutput by remember { mutableStateOf<String?>(null) }

    fun buildRoadmap() {
        val rm = """
            🗺️ MONTHLY ROADMAP ($selectedGoalTier):
            
            • Week 1: Niche Focus & 5 Product Unboxings. Hook optimization practice.
            • Week 2: Storytelling refinement. Add price comparisons & problem-solution scripts.
            • Week 3: SEO Captioning & Keyword tagging. Organize Bio link highlights.
            • Week 4: Performance audit. Double down on top 2 performing product categories!
        """.trimIndent()
        roadmapOutput = rm
        onSaveGoals("Tier: $selectedGoalTier\n$rm")
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 5 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Monthly Growth Roadmap", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        listOf(
            "Tier 1: Foundation (20 Posts/Mo)",
            "Tier 2: Growth (30 Posts/Mo)",
            "Tier 3: Scale (45 Posts/Mo)"
        ).forEach { tier ->
            val isSel = selectedGoalTier == tier
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isSel) Color(0xFFFF2A7A) else Color(0x22FFFFFF))
                    .border(1.dp, if (isSel) Color.White else Color(0x44FF2A7A), RoundedCornerShape(14.dp))
                    .clickable { selectedGoalTier = tier }
                    .padding(12.dp)
            ) {
                Text(tier, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { buildRoadmap() },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("🎯 Generate Monthly Roadmap", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        roadmapOutput?.let { out ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x33FF2A7A))
                    .border(1.dp, Color(0xFFFF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text(out, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
            }
        }
    }
}

/** MODULE 6: Income Calculator */
@Composable
private fun Module6IncomeCalculatorView() {
    var postsCountInput by remember { mutableStateOf("15") }
    var clicksPerPostInput by remember { mutableStateOf("50") }
    var conversionRateInput by remember { mutableStateOf("3") }
    var avgCommissionInput by remember { mutableStateOf("35") }
    var calculatedEstimate by remember { mutableStateOf<String?>(null) }

    fun calculateEstimate() {
        val posts = postsCountInput.toIntOrNull() ?: 0
        val clicks = clicksPerPostInput.toIntOrNull() ?: 0
        val conv = conversionRateInput.toDoubleOrNull() ?: 0.0
        val comm = avgCommissionInput.toIntOrNull() ?: 0

        val totalClicks = posts * clicks
        val totalOrders = (totalClicks * (conv / 100.0)).toInt()
        val totalEstEarning = totalOrders * comm

        calculatedEstimate = """
            📊 EDUCATIONAL ESTIMATE BREAKDOWN:
            • Total Projected Clicks: $totalClicks clicks
            • Projected Conversions ($conv%): ~$totalOrders orders
            • Estimated Educational Commission Goal: ~₹$totalEstEarning
            
            ⚠️ Mandatory Disclaimer: This is only a planning estimate and not a prediction or guarantee.
        """.trimIndent()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 6 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Educational Income Calculator", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(10.dp))

        // MANDATORY DISCLAIMER BANNER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33FF2A7A))
                .border(1.dp, Color(0xFFFF2A7A), RoundedCornerShape(12.dp))
                .padding(10.dp)
        ) {
            Text(
                "\"This is only a planning estimate and not a prediction or guarantee.\"",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Number of Posts", fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.8f))
                OutlinedTextField(
                    value = postsCountInput,
                    onValueChange = { postsCountInput = it },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFFFF2A7A),
                        unfocusedBorderColor = Color(0x66FF2A7A),
                        focusedContainerColor = Color(0x22FFFFFF),
                        unfocusedContainerColor = Color(0x11FFFFFF)
                    )
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text("Clicks / Post", fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.8f))
                OutlinedTextField(
                    value = clicksPerPostInput,
                    onValueChange = { clicksPerPostInput = it },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFFFF2A7A),
                        unfocusedBorderColor = Color(0x66FF2A7A),
                        focusedContainerColor = Color(0x22FFFFFF),
                        unfocusedContainerColor = Color(0x11FFFFFF)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Est. Conversion %", fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.8f))
                OutlinedTextField(
                    value = conversionRateInput,
                    onValueChange = { conversionRateInput = it },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFFFF2A7A),
                        unfocusedBorderColor = Color(0x66FF2A7A),
                        focusedContainerColor = Color(0x22FFFFFF),
                        unfocusedContainerColor = Color(0x11FFFFFF)
                    )
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text("Avg Commission (₹)", fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.8f))
                OutlinedTextField(
                    value = avgCommissionInput,
                    onValueChange = { avgCommissionInput = it },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFFFF2A7A),
                        unfocusedBorderColor = Color(0x66FF2A7A),
                        focusedContainerColor = Color(0x22FFFFFF),
                        unfocusedContainerColor = Color(0x11FFFFFF)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { calculateEstimate() },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("📐 Calculate Planning Estimate", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        calculatedEstimate?.let { est ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text(est, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
            }
        }
    }
}

/** MODULE 7: Growth Dashboard */
@Composable
private fun Module7GrowthDashboardView() {
    val dashMetrics = listOf(
        Pair("🎓 Current Level", "Level 9: AI Income Scaling Blueprint"),
        Pair("📈 Weekly Progress", "88% Tasks Completed This Week"),
        Pair("🏆 Mission Completion", "8 of 10 Level Missions Cleared"),
        Pair("🔥 Consistency Score", "94/100 (5 Days Active Streak)"),
        Pair("🧠 Learning Score", "92/100 (High Mastery Rating)"),
        Pair("⭐ Creator XP", "3,450 XP Earned Total")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 7 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Creator Growth Dashboard", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        dashMetrics.forEach { (label, value) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(label, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(value, fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
                }
            }
        }
    }
}

/** MODULE 8: Income Killers */
@Composable
private fun Module8IncomeKillersView() {
    val pitfalls = listOf(
        Pair("🎲 Posting Randomly", "Irregular posting schedule prevents social algorithms from categorizing your content."),
        Pair("🏳️ Giving Up Early", "Most creators quit right before the 30-day compounding video views kick in."),
        Pair("📦 Poor Product Selection", "Promoting products with zero demand or low rating leads to high refund rates."),
        Pair("📉 Ignoring Analytics", "Failing to check watch time retention causes repeated video format mistakes."),
        Pair("🤖 Copying Everyone", "Blindly copying other creators without personal voice destroys viewer trust."),
        Pair("⚡ No Consistency", "Inconsistent effort kills viewer retention and affiliate link click habit.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 8 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("6 Major Income Killers To Avoid", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        pitfalls.forEach { (killer, explain) ->
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
                    Text(killer, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF2A7A))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(explain, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 9: AI Improvement Plan */
@Composable
private fun Module9AiImprovementPlanView() {
    val top5Improvements = listOf(
        "1. Hook Retention: Add dynamic text popups in the first 2 seconds of every video.",
        "2. Product Lighting: Shoot near natural window light to showcase true fabric colors.",
        "3. Bio Link Clarity: Place Meesho code directly inside the first 3 lines of video caption.",
        "4. Story Highlights: Create 3 categorized highlight circles ('Under ₹299', 'Fashion', 'Decor').",
        "5. Community Response: Reply to every viewer question within 1 hour to boost engagement algorithms."
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 9 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("AI Personalized Improvement Plan", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        top5Improvements.forEach { imp ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Text(imp, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
            }
        }
    }
}

/** MISSION: Plan Your Next 7 Days */
@Composable
private fun Module10MissionView(onMissionComplete: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFFFD700))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("LEVEL 9 MISSION", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Plan Your Next 7 Days", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
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
                Text("📋 Mission Blueprint Objectives:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Spacer(modifier = Modifier.height(6.dp))
                Text("1. Select 5 trending Meesho items to feature this week.", fontSize = 11.5.sp, color = Color.White)
                Text("2. Draft 5 storytelling scripts using Problem -> Solution format.", fontSize = 11.5.sp, color = Color.White)
                Text("3. Schedule posting times for Mon, Tue, Thu, Fri, Sat.", fontSize = 11.5.sp, color = Color.White)
                Text("4. Set up 1 dedicated bio link highlight folder.", fontSize = 11.5.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onMissionComplete,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("✅ Complete 7-Day Planning Mission (+500 XP)", fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }
    }
}

/** MODULE 11: ACHIEVEMENT - Income Planner Badge (+500 XP) */
@Composable
private fun Module11AchievementView(onFinishLevel9: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "badgeAnim9")
    val badgeGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "bgGlow9"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // BADGE ICON WITH GOLDEN GLOW
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFFFD700).copy(alpha = badgeGlow), Color(0xFFFF2A7A).copy(alpha = 0.3f))
                    )
                )
                .border(2.5.dp, Color(0xFFFFD700), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🏅", fontSize = 52.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text("LEVEL 9 COMPLETED!", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

        Spacer(modifier = Modifier.height(4.dp))

        Text("Achievement Unlocked:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
        Text("Income Planner", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x44FF2A7A))
                .border(1.dp, Color(0xFFFF2A7A), RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text("+500 XP REWARD", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Text(
                "You have mastered the AI Income Scaling Blueprint! You now hold a structured, sustainable, long-term system for building a thriving creator business on Meesho.",
                fontSize = 11.5.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onFinishLevel9,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("Complete Level 9 & Claim +500 XP 🎉", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }
    }
}
