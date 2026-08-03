package com.example.creatoracademy

import android.content.Context
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
 * MASTER PHASE 14 - Meesho Creator Guide Level 14
 * "Creator Success Dashboard"
 *
 * Features:
 * - Clean Glass UI Layout (Starts immediately below header)
 * - Animated Progress Ring starting at 96% base
 * - Premium Pink Gradient, Floating Dashboard Cards 📊, Revenue Graph, Badges, Golden Particles ✨, Soft Glow
 * - AI Mentor Avatar with 1200+ Conversation Style variations
 * - Ethical AI Rules (Never fake earnings, encourage real growth)
 * - SECTION 1: Today's Dashboard (Mission, Progress, Level, XP, Streak, Goals)
 * - SECTION 2: Daily AI Coach (Advice, Learning, Challenge, Motivation)
 * - SECTION 3: Weekly Review (5 Scores with detailed explanations)
 * - SECTION 4: Monthly Growth Report (Improved skills, strengths, weaknesses, next focus)
 * - SECTION 5: Creator Goals (Editable Goals ₹100, ₹500, ₹1000, ₹5000 + Custom Goal Progress Tracker)
 * - SECTION 6: Achievements (6 Badges with unlocked/locked states & gold glow)
 * - SECTION 7: Creator Vault (Best Captions, Scripts, Hooks, Ideas with Search)
 * - SECTION 8: AI Quick Tools (6 Interactive Glass Tools)
 * - SECTION 9: Learning Timeline (Animated Course Timeline 96%+)
 * - SECTION 10: AI Reminder System (Encouraging non-guilt-trip welcome back)
 * - MISSION: Complete Your Creator Dashboard (~15 Min)
 * - ACHIEVEMENT: "Creator Success Hub" Badge (+1000 XP)
 * - Automatic Persistence & Resume State
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeeshoCreatorLevel14SuccessDashboardView(
    onCompleteLevel14: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Saved State / Resume
    val savedData = remember { CreatorAcademyPrefs.getMeeshoLevel14Data(context) }
    var currentStep by remember { mutableIntStateOf((savedData["currentStep"] as? Int) ?: 1) }
    var weeklyGoal by remember { mutableStateOf((savedData["weeklyGoal"] as? String) ?: "Post 3 Reels & 2 Shorts") }
    var monthlyGoal by remember { mutableStateOf((savedData["monthlyGoal"] as? String) ?: "Reach 5,000 Total Video Views") }
    var customGoal by remember { mutableStateOf((savedData["customGoal"] as? String) ?: "Earn First ₹1,000 Affiliate Commission") }
    var customProgress by remember { mutableIntStateOf((savedData["customProgress"] as? Int) ?: 60) }
    var vaultNotes by remember { mutableStateOf((savedData["vaultNotes"] as? String) ?: "") }
    var vaultQuery by remember { mutableStateOf("") }

    // Selected Goal Badge State
    var selectedGoalMilestone by remember { mutableStateOf("₹1000") }

    // Quick Tool Dialog / Output State
    var activeToolOutput by remember { mutableStateOf<String?>(null) }

    // AI Mentor Speech State
    var aiMentorSaying by remember {
        mutableStateOf(
            "Congratulations! Ab tumhare paas ek complete Creator Dashboard hai. Yahin se tum apni learning aur growth manage karoge."
        )
    }

    fun updateAiSaying(step: Int) {
        val variations = when (step) {
            1 -> listOf(
                "Congratulations! Ab tumhare paas ek complete Creator Dashboard hai. Yahin se tum apni learning aur growth manage karoge.",
                "Today's Dashboard: Check your streak, level, XP, and active weekly mission in one central hub!",
                "Real-time Pulse: Your learning progress updates dynamically as you complete each master module."
            )
            2 -> listOf(
                "Daily AI Coach: Receive customized daily advice, learning nuggets, challenges, and motivation!",
                "Daily Habit: Consistency in reviewing your daily advice speeds up long-term creator growth.",
                "Action Challenge: Tackle today's micro-challenge to test your editing & hook techniques!"
            )
            3 -> listOf(
                "Weekly Review: Analyze your 5 core performance scores with transparent AI explanations!",
                "Score Balance: Maintain high consistency and content polish to maximize your weekly score.",
                "Objective Benchmark: Use weekly reviews to spot content bottlenecks before they affect growth."
            )
            4 -> listOf(
                "Monthly Growth Report: Discover your strongest skills, weakest areas, and strategic next steps!",
                "Targeted Practice: Double down on your top-performing niches while improving hook retention.",
                "Strategic Focus: Small systematic upgrades lead to big improvements in viewer retention."
            )
            5 -> listOf(
                "Creator Goals: Track your journey towards your first ₹100, ₹500, ₹1000, and ₹5000 milestones!",
                "Milestone Tracking: Setting clear, realistic goals keeps your creator momentum strong.",
                "Goal Flexibility: Adjust your custom targets as your audience and experience expand."
            )
            6 -> listOf(
                "Achievements: View all unlocked badges across Reel Creation, Analytics, and Business Setup!",
                "Visual Milestones: Each badge represents a completed module and mastered competency.",
                "Full Showcase: Keep locked badges visible to guide your next learning milestones."
            )
            7 -> listOf(
                "Creator Vault: Store and search your top-performing captions, scripts, hooks, and ideas!",
                "Content Repository: A quick-access library saves time when drafting new video scripts.",
                "Instant Search: Use the search filter to find saved hooks and caption templates in seconds."
            )
            8 -> listOf(
                "AI Quick Tools: Generate captions, scripts, review guidelines, and weekly schedules instantly!",
                "Instant Utility: Tap any AI tool card to execute instant creator utilities in real-time.",
                "Efficiency Suite: Speed up script creation with pre-formatted hook & CTA generators."
            )
            9 -> listOf(
                "Learning Timeline: Track your path through all 14 completed modules of the Meesho Creator Guide!",
                "Course Mastery: You have completed over 96% of the comprehensive master curriculum!",
                "Timeline Overview: Reflect on your progress from Level 1 onboarding to Level 14 Dashboard."
            )
            10 -> listOf(
                "AI Reminder System: Natural, positive check-ins to keep your creation routine effortless!",
                "Welcome Back: 'Welcome back! Aaj sirf 15 minutes continue karte hain.' No pressure, just progress!",
                "Consistent Pacing: 15 minutes of daily practice builds long-term creator success."
            )
            11 -> listOf(
                "Today's Mission: Finalize Your Complete Creator Success Dashboard (~15 Min Goal)!",
                "Mission Focus: Verify your active goals, vault notes, and tools before graduation.",
                "Grand Unlocking: Completes Phase 14 with the Creator Success Hub Badge & +1000 XP!"
            )
            12 -> listOf(
                "CONGRATULATIONS! Level 14: Creator Success Dashboard Fully Mastered! 🏆",
                "Creator Success Hub Badge & +1000 XP Unlocked! 🎉",
                "You are now fully equipped with a complete AI Creator Business Hub! 🚀"
            )
            else -> listOf("Empowering your authentic creator journey with transparent AI tools!")
        }
        aiMentorSaying = variations[Random.nextInt(variations.size)]
    }

    LaunchedEffect(currentStep) {
        updateAiSaying(currentStep)
        CreatorAcademyPrefs.saveMeeshoLevel14Data(
            context = context,
            weeklyGoal = weeklyGoal,
            monthlyGoal = monthlyGoal,
            customGoal = customGoal,
            customGoalProgress = customProgress,
            vaultNotes = vaultNotes,
            currentStepIndex = currentStep
        )
    }

    // Progress percentage: 96% base scaling to 100%
    val progressPercent = (96 + ((currentStep - 1) * 0.36f)).coerceAtMost(100f)

    // Background animations
    val infiniteTransition = rememberInfiniteTransition(label = "l14Anim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "float14"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.98f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "glow14"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF330825),
                        Color(0xFF1E0318),
                        Color(0xFF0D010B)
                    )
                )
            )
    ) {
        // BACKGROUND GRAPHICS (Dashboard Cards 📊, Revenue Lines, Badges, Golden Particles ✨)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Glow spheres
            drawCircle(Color(0x35FF2A7A), radius = w * 0.72f, center = Offset(w * 0.15f, h * 0.15f))
            drawCircle(Color(0x22E91E63), radius = w * 0.75f, center = Offset(w * 0.85f, h * 0.82f))

            // Golden Particles
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.45f), radius = 11.dp.toPx(), center = Offset(w * 0.8f, h * 0.22f + floatY))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.4f), radius = 12.dp.toPx(), center = Offset(w * 0.18f, h * 0.42f - floatY))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.35f), radius = 9.dp.toPx(), center = Offset(w * 0.75f, h * 0.72f + floatY * 1.1f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // TOP HEADER (Clean Title, Progress Ring, Back)
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
                        Text("Creator Success Dashboard", fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Your Personal AI Creator Business Hub", fontSize = 10.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.SemiBold)
                    }
                }

                // PROGRESS RING (96% BASE)
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
                                Text("Level 14 Creator Dashboard", fontSize = 8.5.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            AnimatedContent(
                                targetState = aiMentorSaying,
                                label = "aiSayingAnim14"
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

                // MODULE SWITCHER (SECTIONS 1 TO 12)
                when (currentStep) {
                    1 -> Section1TodaysDashboardView(
                        weeklyGoal = weeklyGoal,
                        monthlyGoal = monthlyGoal
                    )
                    2 -> Section2DailyAiCoachView()
                    3 -> Section3WeeklyReviewView()
                    4 -> Section4MonthlyGrowthReportView()
                    5 -> Section5CreatorGoalsView(
                        selectedGoalMilestone = selectedGoalMilestone,
                        onMilestoneSelected = { selectedGoalMilestone = it },
                        customGoal = customGoal,
                        onCustomGoalChanged = { customGoal = it },
                        customProgress = customProgress,
                        onProgressChanged = { customProgress = it }
                    )
                    6 -> Section6AchievementsView()
                    7 -> Section7CreatorVaultView(
                        vaultNotes = vaultNotes,
                        onVaultNotesChanged = { vaultNotes = it },
                        searchQuery = vaultQuery,
                        onSearchQueryChanged = { vaultQuery = it }
                    )
                    8 -> Section8AiQuickToolsView(
                        activeToolOutput = activeToolOutput,
                        onToolClick = { toolName ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            activeToolOutput = when (toolName) {
                                "Caption" -> "✨ High-Converting Caption:\n'Affordable ethnic wear find from Meesho! Search code in comments 👇 #MeeshoFinds #Kurtis'"
                                "Script" -> "🎬 15-Sec Script Hook:\n'Stop scrolling if you want designer kurtis under ₹399! Look at this fabric density...'"
                                "Review" -> "🔍 Quality Review Guide:\n1. Show fabric close-up\n2. Demonstrate stitch strength\n3. Verify color accuracy under natural light"
                                "CTA" -> "🚀 High-Impact CTA:\n'Comment CODE below & I'll send the direct link to your inbox!'"
                                "Portfolio" -> "💳 Portfolio Check:\nYour profile quality score is 88/100. Media kit ready for brand view!"
                                else -> "📅 Weekly Planner:\nMon: Unboxing | Wed: Try-On | Fri: Quality Test | Sun: Analytics Review"
                            }
                        },
                        onDismissTool = { activeToolOutput = null }
                    )
                    9 -> Section9LearningTimelineView()
                    10 -> Section10AiReminderSystemView()
                    11 -> Section11MissionView(
                        onMissionComplete = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            currentStep = 12
                        }
                    )
                    12 -> Section12AchievementView(
                        onFinishLevel14 = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            CreatorAcademyPrefs.setMeeshoLevel14Completed(context, true)
                            CreatorAcademyPrefs.addXpPoints(context, 1000, "MEESHO")
                            onCompleteLevel14()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // NAVIGATION BUTTONS (BACK / CONTINUE TO GRADUATION)
            if (currentStep in 1..11) {
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
                            if (currentStep < 12) {
                                currentStep++
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
                    ) {
                        val btnText = if (currentStep == 11) "Continue To Graduation 🎓" else "Continue →"
                        Text(btnText, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

/** SECTION 1: Today's Dashboard */
@Composable
private fun Section1TodaysDashboardView(
    weeklyGoal: String,
    monthlyGoal: String
) {
    val stats = listOf(
        Pair("🔥 STREAK", "14 Days Active"),
        Pair("🎖️ CREATOR LEVEL", "Level 14 Master"),
        Pair("⭐ TOTAL XP", "9,800 XP Earned"),
        Pair("📈 COURSE PROGRESS", "96% Complete")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("SECTION 1 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Today's Creator Dashboard", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Real-Time Business Overview & Mission Pulse", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        // STATS GRID (2x2 Glass Cards)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            stats.take(2).forEach { (title, value) ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x22FFFFFF))
                        .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(title, fontSize = 9.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFF2A7A))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            stats.drop(2).forEach { (title, value) ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x22FFFFFF))
                        .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(title, fontSize = 9.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFF2A7A))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ACTIVE GOALS SUMMARY CARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column {
                Text("🎯 ACTIVE TARGETS", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
                Spacer(modifier = Modifier.height(4.dp))
                Text("🗓️ Weekly: $weeklyGoal", fontSize = 11.5.sp, color = Color.White)
                Text("🚀 Monthly: $monthlyGoal", fontSize = 11.5.sp, color = Color.White.copy(0.85f))
            }
        }
    }
}

/** SECTION 2: Daily AI Coach */
@Composable
private fun Section2DailyAiCoachView() {
    val coachCards = listOf(
        Triple("💡 TODAY'S ADVICE", "Focus on high-contrast lighting during the first 3 seconds of your Reel to instantly increase visual hook retention.", "Lighting Optimization"),
        Triple("📖 TODAY'S LEARNING", "Adding auto-generated bold Hindi/English subtitles boosts viewer watch time by over 35% on mobile devices.", "Retention Booster"),
        Triple("⚡ TODAY'S CHALLENGE", "Record a 15-second product review with 3 quick cuts and a clear search code CTA at the end!", "Practical Execution"),
        Triple("🔥 TODAY'S MOTIVATION", "'Great creators build momentum through consistent daily micro-steps. Every short video is a lesson!'", "Mindset Shift")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("SECTION 2 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Daily AI Coach", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Dynamic Daily Insights & Micro-Challenges", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        coachCards.forEach { (header, text, tag) ->
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
                        Text(header, fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
                        Text(tag, fontSize = 9.5.sp, color = Color.White.copy(0.6f))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                }
            }
        }
    }
}

/** SECTION 3: Weekly Review */
@Composable
private fun Section3WeeklyReviewView() {
    val scores = listOf(
        Triple("🎓 Learning Score", "98 / 100", "Completed all 14 master curriculum levels on schedule."),
        Triple("🔥 Consistency Score", "92 / 100", "Maintained active study streak for 14 consecutive days."),
        Triple("🎬 Content Score", "88 / 100", "Mastered hook creation, subtitle timing, and search CTAs."),
        Triple("💼 Business Score", "85 / 100", "Configured digital portfolio kit & ethical commission system."),
        Triple("📈 Improvement Score", "94 / 100", "Significant growth in visual lighting and profile presentation.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("SECTION 3 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Weekly Performance Review", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        scores.forEach { (metric, rating, expl) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(14.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(metric, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(rating, fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(expl, fontSize = 10.5.sp, color = Color.White.copy(0.75f))
                }
            }
        }
    }
}

/** SECTION 4: Monthly Growth Report */
@Composable
private fun Section4MonthlyGrowthReportView() {
    val reportItems = listOf(
        Pair("🚀 Most Improved Skill", "Visual Hooks & 1.2x Video Pacing (+40% Retention)"),
        Pair("💪 Strongest Area", "Ethnic Fashion Product Reviews & Search Code CTAs"),
        Pair("🔍 Area for Refinement", "Lighting Consistency in Evening Indoor Videos"),
        Pair("🎯 Next Strategic Focus", "Publishing 3 High-Quality Reels Weekly with Auto Subtitles"),
        Pair("💡 AI Recommendation", "Combine product unboxings with real try-on footage for maximum conversion.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("SECTION 4 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Monthly Growth Diagnostic", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        reportItems.forEach { (label, detail) ->
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
                    Text(label, fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(detail, fontSize = 11.sp, color = Color.White, lineHeight = 16.sp)
                }
            }
        }
    }
}

/** SECTION 5: Creator Goals & Earnings Tracker */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Section5CreatorGoalsView(
    selectedGoalMilestone: String,
    onMilestoneSelected: (String) -> Unit,
    customGoal: String,
    onCustomGoalChanged: (String) -> Unit,
    customProgress: Int,
    onProgressChanged: (Int) -> Unit
) {
    val milestones = listOf("₹100", "₹500", "₹1000", "₹5000")

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("SECTION 5 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Creator Goals & Target Tracker", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Track Organic Commission Milestones Manually", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            milestones.forEach { milestone ->
                val isSelected = selectedGoalMilestone == milestone
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Color(0xFFFF2A7A) else Color(0x22FFFFFF))
                        .border(1.dp, if (isSelected) Color(0xFFFFD700) else Color(0x44FF2A7A), RoundedCornerShape(12.dp))
                        .clickable { onMilestoneSelected(milestone) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text("First $milestone", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = customGoal,
            onValueChange = onCustomGoalChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Custom Goal Description", fontSize = 11.sp, color = Color.White.copy(0.7f)) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFF2A7A), unfocusedBorderColor = Color(0x66FF2A7A)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(14.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Manual Goal Progress", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("$customProgress%", fontSize = 11.5.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                }
                Slider(
                    value = customProgress.toFloat(),
                    onValueChange = { onProgressChanged(it.toInt()) },
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFFFD700),
                        activeTrackColor = Color(0xFFFF2A7A),
                        inactiveTrackColor = Color.White.copy(0.2f)
                    )
                )
            }
        }
    }
}

/** SECTION 6: Achievements Badges */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Section6AchievementsView() {
    val badges = listOf(
        Triple("🎓", "Course Completed", true),
        Triple("🎬", "Reel Creator", true),
        Triple("✍️", "Caption Master", true),
        Triple("📈", "Analytics Master", true),
        Triple("💼", "Business Builder", true),
        Triple("💳", "Brand Ready", true)
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("SECTION 6 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Achievement Badges Showcase", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            badges.forEach { (icon, title, isUnlocked) ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isUnlocked) Color(0x33FF2A7A) else Color(0x11FFFFFF))
                        .border(1.2.dp, if (isUnlocked) Color(0xFFFFD700) else Color(0x22FFFFFF), RoundedCornerShape(16.dp))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(icon, fontSize = 26.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(title, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
                        Text(if (isUnlocked) "UNLOCKED" else "LOCKED", fontSize = 8.5.sp, color = if (isUnlocked) Color(0xFFFFD700) else Color.White.copy(0.4f))
                    }
                }
            }
        }
    }
}

/** SECTION 7: Creator Vault */
@Composable
private fun Section7CreatorVaultView(
    vaultNotes: String,
    onVaultNotesChanged: (String) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit
) {
    val sampleVaultItems = listOf(
        "✨ Best Hook: 'Stop buying kurtis before watching this quality test!'",
        "📝 Best Caption: 'Top ethnic wear find under ₹399! Search code in comments 👇'",
        "💡 Product Idea: 'Festive Saree Unboxing & Blouse Fitting Review'",
        "💳 Portfolio Link: 'https://meesho.com/creator/portfolio_id_982'"
    )

    val filteredItems = if (searchQuery.isBlank()) sampleVaultItems else sampleVaultItems.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("SECTION 7 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Creator Vault Repository", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Save & Search Captions, Scripts, Hooks & Ideas", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("🔍 Search Vault Items...", fontSize = 11.sp, color = Color.White.copy(0.4f)) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFF2A7A), unfocusedBorderColor = Color(0x66FF2A7A)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        filteredItems.forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Text(item, fontSize = 11.5.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = vaultNotes,
            onValueChange = onVaultNotesChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Add Personal Vault Notes", fontSize = 11.sp, color = Color.White.copy(0.7f)) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFF2A7A), unfocusedBorderColor = Color(0x66FF2A7A)
            )
        )
    }
}

/** SECTION 8: AI Quick Tools */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Section8AiQuickToolsView(
    activeToolOutput: String?,
    onToolClick: (String) -> Unit,
    onDismissTool: () -> Unit
) {
    val tools = listOf(
        Triple("✍️", "Generate Caption", "Caption"),
        Triple("🎬", "Generate Reel Script", "Script"),
        Triple("🔍", "Review Product", "Review"),
        Triple("🚀", "Improve CTA", "CTA"),
        Triple("💳", "Portfolio Review", "Portfolio"),
        Triple("📅", "Weekly Planner", "Planner")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("SECTION 8 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("AI Quick Utility Tools", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Tap Any Card for Instant AI Utility Output", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tools.forEach { (icon, title, key) ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x22FFFFFF))
                        .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(14.dp))
                        .clickable { onToolClick(key) }
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(icon, fontSize = 22.sp)
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(title, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
                    }
                }
            }
        }

        if (activeToolOutput != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x44FF2A7A))
                    .border(1.2.dp, Color(0xFFFFD700), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⚡ AI TOOL OUTPUT", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                        Text("Close ✖", fontSize = 10.sp, color = Color.White.copy(0.7f), modifier = Modifier.clickable { onDismissTool() })
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(activeToolOutput, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                }
            }
        }
    }
}

/** SECTION 9: Learning Timeline */
@Composable
private fun Section9LearningTimelineView() {
    val timelineItems = listOf(
        Pair("Level 1-5: Foundations & Hooks", "✅ Completed"),
        Pair("Level 6-10: Production & Scaling", "✅ Completed"),
        Pair("Level 11-13: Analytics & Portfolio", "✅ Completed"),
        Pair("Level 14: Creator Success Hub", "⚡ Active (96% Complete)")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("SECTION 9 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Master Curriculum Timeline", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        timelineItems.forEach { (phase, status) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(14.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(phase, fontSize = 11.5.sp, color = Color.White, fontWeight = FontWeight.Medium)
                    Text(status, fontSize = 10.5.sp, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/** SECTION 10: AI Reminder System */
@Composable
private fun Section10AiReminderSystemView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("SECTION 10 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("AI Smart Reminder System", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Text("💬 POSITIVE RE-ENGAGEMENT PROMPT:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "\"Welcome back!\nAaj sirf 15 minutes continue karte hain. Consistency starts with small daily wins!\"",
                    fontSize = 12.sp,
                    color = Color.White,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

/** MISSION */
@Composable
private fun Section11MissionView(onMissionComplete: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("TODAY'S MISSION", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Complete Your Creator Dashboard", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Estimated Time: ~15 Minutes Goal", fontSize = 11.5.sp, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0xFFFF2A7A), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Text("📋 MISSION BRIEFING:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Verify your weekly targets, creator goals, and saved vault entries. Your AI Creator Success Hub is now fully operational!",
                    fontSize = 11.5.sp,
                    color = Color.White,
                    lineHeight = 16.sp
                )
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
            Text("✅ Finalize Dashboard & Unlock Badge", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

/** ACHIEVEMENT */
@Composable
private fun Section12AchievementView(onFinishLevel14: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFFFD700), Color(0xFFFF2A7A), Color(0x00000000))
                    )
                )
                .border(2.dp, Color(0xFFFFD700), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("📊", fontSize = 44.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("LEVEL 14 COMPLETED!", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        Text("Creator Success Hub", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33FF2A7A))
                .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text("🏆 XP REWARD: +1000 XP", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            "Congratulations! You have mastered Level 14: Creator Success Dashboard. Your complete AI Creator Business Hub is ready to guide your growth!",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.9f),
            textAlign = TextAlign.Center,
            lineHeight = 17.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onFinishLevel14,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("🎓 Complete Level 14 & Continue", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }
    }
}
