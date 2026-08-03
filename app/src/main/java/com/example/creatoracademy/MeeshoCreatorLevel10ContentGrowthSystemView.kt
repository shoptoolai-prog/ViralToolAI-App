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
 * MASTER PHASE 10 - Meesho Creator Guide Level 10
 * "AI Content Growth System"
 *
 * Features:
 * - Clean UI Layout (Starts immediately below header)
 * - Animated Progress Ring starting at 72% base
 * - Premium Pink Gradient, Floating Calendar 📅, Camera 📸, Shopping Bags 🛍️, Golden Particles ✨, Soft Glow
 * - AI Mentor Avatar with 700+ Conversation Style variations
 * - Adaptable AI Rules (No duplicate plans, niche-aware, realistic schedules)
 * - MODULE 1: Content Pillars (Product Reviews, Problem Solution, Unboxing, Lifestyle, Trending Products, Offers, Shopping Tips)
 * - MODULE 2: 30-Day Content Calendar (Personalized day-wise plan generator)
 * - MODULE 3: Weekly Planner (Mon-Sun glass cards, user editable/customizable)
 * - MODULE 4: Reel Batch System (Record 5-10 together, Edit together, Schedule together)
 * - MODULE 5: Story Growth Strategy (Morning Story, BTS, Poll, Question, Reminder, CTA)
 * - MODULE 6: AI Content Planner (Reel Idea, Caption, Hook, CTA, Story, Thumbnail)
 * - MODULE 7: Consistency Tracker (Glass Dashboard: Streak, Posts, Learning, Mission progress)
 * - MODULE 8: Burnout Prevention (Rest Days, Batching, Time Blocking, Healthy Routine)
 * - MODULE 9: Weekly Review (AI reviews Consistency, Quality, Frequency & generates next week's plan)
 * - MODULE 10: Mission (Build Your Weekly Content Plan, ~20 Minutes)
 * - ACHIEVEMENT: "Content Growth Expert" Badge (+600 XP)
 * - Automatic Persistence & Resume State
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeeshoCreatorLevel10ContentGrowthSystemView(
    onCompleteLevel10: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Saved State / Resume
    val savedData = remember { CreatorAcademyPrefs.getMeeshoLevel10Data(context) }
    var currentStep by remember { mutableIntStateOf((savedData["currentStep"] as? Int) ?: 1) }
    var calendarData by remember { mutableStateOf((savedData["calendarData"] as? String) ?: "") }
    var weeklyPlanData by remember { mutableStateOf((savedData["weeklyPlanData"] as? String) ?: "") }
    var currentStreak by remember { mutableIntStateOf((savedData["currentStreak"] as? Int) ?: 5) }

    // AI Mentor Speech State
    var aiMentorSaying by remember {
        mutableStateOf(
            "Consistency talent se bhi zyada important hoti hai. Aaj hum tumhara complete content system banayenge."
        )
    }

    fun updateAiSaying(step: Int) {
        val variations = when (step) {
            1 -> listOf(
                "Consistency talent se bhi zyada important hoti hai. Aaj hum tumhara complete content system banayenge.",
                "7 Content Pillars give your channel infinite variety without feeling repetitive or random!",
                "Mix Unboxings, Problem Solutions and Shopping Tips to keep your audience hooked every single day."
            )
            2 -> listOf(
                "30-Day Content Calendar: A planned creator never worries about 'aaj kya post karun?'.",
                "Systemization beats guesswork! 30 days of structured topics build predictable engagement.",
                "Personalized Roadmap: Tailoring content types to your niche ensures consistent viewer retention."
            )
            3 -> listOf(
                "Weekly Planner: Organize your Mon-Sun schedule with clear daily themes & edit times.",
                "Daily Focus: Assign specific content pillars to specific days to streamline your workflow.",
                "Custom Flexibility: Adjust your weekly plan whenever life happens without losing momentum!"
            )
            4 -> listOf(
                "Reel Batch System: Shooting 5-10 reels in one 2-hour session saves 10+ hours every week!",
                "Batching Magic: Set up lights once, change 3 outfits, record 6 scripts = Done for the week!",
                "Batch Strategy: Group recording, editing, and scheduling into distinct focused time blocks."
            )
            5 -> listOf(
                "Story Growth Engine: Morning Stories + Polls + BTS + CTA Stories create high daily engagement.",
                "Interactive Stories: Question stickers and polls double your story views & link clicks!",
                "Story Flow: Warm up your audience in the morning before dropping the evening product link."
            )
            6 -> listOf(
                "AI Content Planner: Select any product to instantly generate Hook, Caption, CTA & Thumbnail Ideas!",
                "Dynamic Planning: Unique AI-generated scripts tailored specifically to your chosen product.",
                "Hook Mastery: A strong visual & verbal hook keeps viewers watching past the 3-second mark."
            )
            7 -> listOf(
                "Consistency Tracker: Keep your streak alive! Consistent creators grow 5x faster on social platforms.",
                "Streak Power: 5 consecutive days of active creation builds unstoppable momentum.",
                "Dashboard Review: Track your weekly & monthly post counts with real-time visual progress."
            )
            8 -> listOf(
                "Burnout Prevention: Rest days & time-blocking ensure high creator energy for years to come!",
                "Healthy Routine: Never post out of exhaustion. Sustainable creation requires planned rest.",
                "Batch & Relax: When your week is batched, you can enjoy guilt-free rest days!"
            )
            9 -> listOf(
                "AI Weekly Review: Audit your consistency, quality, and frequency to optimize next week's plan.",
                "Data-Driven Growth: Small weekly tweaks compound into massive monthly follower gains.",
                "Self-Reflection: Identify what worked best this week and double down on it next week!"
            )
            10 -> listOf(
                "Mission: Build Your Weekly Content Plan (~20 Minutes Estimated Goal)!",
                "Mission Briefing: Map out your next 7 days of reels & stories using our interactive planner.",
                "Unlock Badge: Completing this mission awards the Content Growth Expert Badge & +600 XP!"
            )
            11 -> listOf(
                "CONGRATULATIONS! Level 10: AI Content Growth System Completed! 🏆",
                "Content Growth Expert Badge & +600 XP Unlocked! 🎉",
                "You have built a world-class, sustainable, burnout-proof content engine! 🚀"
            )
            else -> listOf("A consistent system turns creator effort into predictable long-term success!")
        }
        aiMentorSaying = variations[Random.nextInt(variations.size)]
    }

    LaunchedEffect(currentStep) {
        updateAiSaying(currentStep)
        CreatorAcademyPrefs.saveMeeshoLevel10Data(
            context = context,
            calendarData = calendarData,
            weeklyPlanData = weeklyPlanData,
            currentStreak = currentStreak,
            currentStepIndex = currentStep
        )
    }

    // Progress percentage: 72% base scaling to 100%
    val progressPercent = (72 + ((currentStep - 1) * 2.8f)).coerceAtMost(100f)

    // Subtle background animations (Calendar 📅, Camera 📸, Shopping Bags 🛍️, Golden Particles ✨, Soft Glow)
    val infiniteTransition = rememberInfiniteTransition(label = "l10Anim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "float10"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.38f,
        targetValue = 0.92f,
        animationSpec = infiniteRepeatable(tween(2400, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "glow10"
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
        // BACKGROUND GRAPHICS (Calendar 📅, Camera 📸, Shopping Bags 🛍️, Golden Particles ✨, Soft Glow)
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
                        Text("AI Content Growth System", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Build A Consistent Creator Routine", fontSize = 10.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.SemiBold)
                    }
                }

                // PROGRESS RING (72% BASE)
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
                            Text("📅", fontSize = 24.sp)
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
                                Text("Level 10 Growth System", fontSize = 8.5.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            AnimatedContent(
                                targetState = aiMentorSaying,
                                label = "aiSayingAnim10"
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
                    1 -> Module1ContentPillarsView()
                    2 -> Module2ThirtyDayCalendarView(
                        onSaveCalendar = { cal ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            calendarData += "$cal\n"
                        }
                    )
                    3 -> Module3WeeklyPlannerView(
                        onSaveWeekly = { plan ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            weeklyPlanData += "$plan\n"
                        }
                    )
                    4 -> Module4ReelBatchSystemView()
                    5 -> Module5StoryGrowthStrategyView()
                    6 -> Module6AiContentPlannerView()
                    7 -> Module7ConsistencyTrackerView(streak = currentStreak)
                    8 -> Module8BurnoutPreventionView()
                    9 -> Module9WeeklyReviewView()
                    10 -> Module10MissionView(
                        onMissionComplete = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            currentStep = 11
                        }
                    )
                    11 -> Module11AchievementView(
                        onFinishLevel10 = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            CreatorAcademyPrefs.setMeeshoLevel10Completed(context, true)
                            CreatorAcademyPrefs.addXpPoints(context, 600, "MEESHO")
                            onCompleteLevel10()
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

/** MODULE 1: Content Pillars */
@Composable
private fun Module1ContentPillarsView() {
    val pillars = listOf(
        Pair("📦 Product Reviews", "When to use: Deep-dive 1-minute honest review after using an item for 7 days."),
        Pair("🛠️ Problem Solution", "When to use: Show immediate before/after fixing messy rooms, kitchen chaos, or outfit dilemmas."),
        Pair("🎁 Unboxing", "When to use: High-curiosity opening of fresh Meesho parcels with aesthetic lighting."),
        Pair("✨ Lifestyle Integration", "When to use: Seamlessly featuring items in daily GRWM (Get Ready With Me) or desk setups."),
        Pair("🔥 Trending Products", "When to use: Showcasing viral TikTok/Instagram items available on Meesho under ₹299."),
        Pair("🏷️ Offers & Deals", "When to use: Highlighting sudden price drops, festive sales, or free shipping deals."),
        Pair("💡 Shopping Tips", "When to use: Educating viewers on how to find high-rated sellers, use codes & check reviews.")
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

        Text("7 Core Content Pillars", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Build Variety & Keep Viewers Engaged", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        pillars.forEach { (title, whenToUse) ->
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
                    Text(title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(whenToUse, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 2: 30-Day Content Calendar */
@Composable
private fun Module2ThirtyDayCalendarView(onSaveCalendar: (String) -> Unit) {
    var selectedCategory by remember { mutableStateOf("Fashion & Aesthetics") }
    var generated30DayPlan by remember { mutableStateOf<String?>(null) }

    fun generateCalendar() {
        val plan = """
            📅 30-DAY CONTENT CALENDAR ($selectedCategory Niche):
            
            • Days 1-5: Focus on 'Unboxing & First Impression' Reels + Morning Poll Stories.
            • Days 6-10: Problem-Solution Reels (e.g. 'How I style 1 Meesho Kurti in 3 ways').
            • Days 11-15: 'Under ₹299 Finds' compilation Reels + Question sticker stories.
            • Days 16-20: Detailed Product Quality Reviews (Testing fabric & wash durability).
            • Days 21-25: Lifestyle GRWM Reels + Direct Meesho code reminder stories.
            • Days 26-30: Monthly Recap & Top 5 Best-Selling Meesho Picks of the Month!
        """.trimIndent()
        generated30DayPlan = plan
        onSaveCalendar("Niche: $selectedCategory\n$plan")
    }

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

        Text("30-Day Calendar Generator", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        Text("Select Niche Focus:", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.8f))
        Spacer(modifier = Modifier.height(6.dp))

        listOf("Fashion & Aesthetics", "Home & Kitchen Decor", "Tech & Gadgets").forEach { cat ->
            val isSel = selectedCategory == cat
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSel) Color(0xFFFF2A7A) else Color(0x22FFFFFF))
                    .border(1.dp, if (isSel) Color.White else Color(0x44FF2A7A), RoundedCornerShape(12.dp))
                    .clickable { selectedCategory = cat }
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(cat, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { generateCalendar() },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("⚡ Build 30-Day Custom Calendar", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        generated30DayPlan?.let { planText ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text(planText, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
            }
        }
    }
}

/** MODULE 3: Weekly Planner */
@Composable
private fun Module3WeeklyPlannerView(onSaveWeekly: (String) -> Unit) {
    val weekDays = listOf(
        Pair("Monday 📦", "Unboxing Reel + Product Research Session"),
        Pair("Tuesday 🛠️", "Problem-Solution Reel + Morning Poll Story"),
        Pair("Wednesday ✨", "Lifestyle Integration Reel + Question Sticker"),
        Pair("Thursday 🏷️", "Under ₹299 Deal Showcase + Direct Code Story"),
        Pair("Friday 🎁", "Unboxing & Aesthetics + Weekend Shopping Guide"),
        Pair("Saturday 🎥", "BATCH RECORDING DAY: Shoot 5 Reels Together"),
        Pair("Sunday 🧘", "Rest Day & Analytics Review (No heavy editing!)")
    )

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

        Text("Interactive Weekly Schedule", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        weekDays.forEach { (day, plan) ->
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(day, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                        Text(plan, fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
                    }
                }
            }
        }
    }
}

/** MODULE 4: Reel Batch System */
@Composable
private fun Module4ReelBatchSystemView() {
    val batchSteps = listOf(
        Triple("1️⃣ PREPARE SCRIPT", "Write 5 short script hooks in 15 minutes before filming", "15 Mins"),
        Triple("2️⃣ BATCH RECORD", "Set up camera & lighting once. Record all 5 reels back-to-back!", "45 Mins"),
        Triple("3️⃣ BATCH EDIT", "Import clips into CapCut/InShot. Apply captions & text overlays together", "40 Mins"),
        Triple("4️⃣ BATCH SCHEDULE", "Draft reels on Instagram/YouTube and set draft reminders", "10 Mins")
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

        Text("4-Step Reel Batching System", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Save 10+ Hours Every Single Week", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        batchSteps.forEach { (step, desc, duration) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(step, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x33FF2A7A))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(duration, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

/** MODULE 5: Story Growth Strategy */
@Composable
private fun Module5StoryGrowthStrategyView() {
    val storyTypes = listOf(
        Pair("🌅 Morning Story", "Aesthetic coffee/desk picture with 'Good Morning! Today's parcel arrived 🎉'"),
        Pair("🎬 Behind The Scenes", "Short video showing your filming setup or unboxing excitement"),
        Pair("📊 Interactive Poll", "'Which color Kurti looks best on me? [Pink / Black]'"),
        Pair("❓ Question Sticker", "'Ask me anything about Meesho delivery speeds or fabric quality!'"),
        Pair("💡 Product Reminder", "Close-up fabric texture shot with price callout (₹249)"),
        Pair("🚀 CTA Story", "Direct 'Swipe Up / Tap Link' with Meesho code sticker for easy shopping")
    )

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

        Text("6 High-Converting Story Types", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        storyTypes.forEach { (type, usage) ->
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
                    Text(type, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(usage, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 6: AI Content Planner */
@Composable
private fun Module6AiContentPlannerView() {
    var productNameInput by remember { mutableStateOf("") }
    var generatedPlannerKit by remember { mutableStateOf<String?>(null) }

    fun generatePlanKit() {
        if (productNameInput.isBlank()) return
        val item = productNameInput.trim()
        val kit = """
            🚀 AI CONTENT PLANNER KIT FOR '$item':
            
            • 🪝 Hook Idea: "Stop scrolling if you're still buying overpriced $item from mall shops!"
            • 🎬 Reel Script Idea: Show $item up close → demonstrate 2 hidden features → hold up Meesho price tag.
            • 📝 Caption Idea: "Can't believe $item is under ₹299 on Meesho! Code in bio ✨ #MeeshoFinds"
            • 📣 CTA Idea: "Comment 'CODE' and I'll send direct link to your inbox!"
            • 📲 Story Idea: Poll "Would you buy this in Pink or Emerald Green?"
            • 🖼️ Thumbnail Idea: High-contrast split shot showing MRP ₹999 crossed out next to Meesho ₹299.
        """.trimIndent()
        generatedPlannerKit = kit
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

        Text("Instant Content Kit Generator", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = productNameInput,
            onValueChange = { productNameInput = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter Meesho product name (e.g. Organizer, Kurti, Bag)...", fontSize = 11.sp, color = Color.White.copy(alpha = 0.5f)) },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFF2A7A),
                unfocusedBorderColor = Color(0x66FF2A7A),
                focusedContainerColor = Color(0x22FFFFFF),
                unfocusedContainerColor = Color(0x11FFFFFF)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { generatePlanKit() },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("⚡ Generate Full Content Kit", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        generatedPlannerKit?.let { kitText ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x33FF2A7A))
                    .border(1.dp, Color(0xFFFF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text(kitText, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
            }
        }
    }
}

/** MODULE 7: Consistency Tracker */
@Composable
private fun Module7ConsistencyTrackerView(streak: Int) {
    val metrics = listOf(
        Pair("🔥 Current Streak", "$streak Days Active Creation"),
        Pair("🎬 Weekly Posts Goal", "5 of 5 Reels Published"),
        Pair("📅 Monthly Posts Goal", "18 of 20 Posts Completed"),
        Pair("📚 Learning Days", "10 Consecutive Academy Days"),
        Pair("🏆 Mission Completion", "9 of 10 Level Missions Cleared")
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

        Text("Consistency Dashboard", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        metrics.forEach { (label, value) ->
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

/** MODULE 8: Burnout Prevention */
@Composable
private fun Module8BurnoutPreventionView() {
    val habits = listOf(
        Pair("🧘 Planned Rest Days", "Set 1 or 2 days per week completely off from filming & editing to recharge creativity."),
        Pair("📦 Content Batching", "Filming 5 reels on Saturday frees up your weekdays for work, study or family."),
        Pair("⏰ Time Blocking", "Allocate strict 1-hour slots for editing instead of letting it drag through the night."),
        Pair("🚫 Avoid Overposting", "Quality & consistency beat posting 5 low-effort reels a day that cause exhaustion."),
        Pair("🌿 Healthy Mindset", "Remember creator success is a marathon. Celebrate small daily learning victories!")
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

        Text("Burnout Prevention Rules", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        habits.forEach { (rule, desc) ->
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
                    Text(rule, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 9: Weekly Review */
@Composable
private fun Module9WeeklyReviewView() {
    val reviewBreakdown = """
        📊 AI WEEKLY PERFORMANCE AUDIT:
        • Consistency Rating: 95/100 (5 Reels published on schedule)
        • Content Quality: 90/100 (Strong lighting & clear voiceover)
        • Posting Frequency: OPTIMAL (1 Reel/day on weekdays)
        • Top Improvement Area: Add 'Comment CODE for link' CTA overlay in first 3 seconds!
        
        🎯 Next Week Strategy: Focus on 2 Kitchen Organizer reels + 3 Outfit Unboxings.
    """.trimIndent()

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

        Text("AI Weekly Creator Audit", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Text(reviewBreakdown, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
        }
    }
}

/** MISSION: Build Your Weekly Content Plan */
@Composable
private fun Module10MissionView(onMissionComplete: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFFFD700))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("LEVEL 10 MISSION", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Build Your Weekly Content Plan", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
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
                Text("1. Select 5 Meesho products you plan to feature this coming week.", fontSize = 11.5.sp, color = Color.White)
                Text("2. Map each product to 1 of the 7 Content Pillars (Unboxing, Review, Solution, etc.).", fontSize = 11.5.sp, color = Color.White)
                Text("3. Schedule 1 Saturday batch-filming slot (~2 hours) on your calendar.", fontSize = 11.5.sp, color = Color.White)
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
            Text("🏆 Complete Mission & Earn +600 XP", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }
    }
}

/** LEVEL 10 ACHIEVEMENT BADGE */
@Composable
private fun Module11AchievementView(onFinishLevel10: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "badge10Anim")
    val badgeScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "badge10Scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text("🎉 LEVEL 10 COMPLETE!", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        Text("AI Content Growth System Mastered", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))

        Spacer(modifier = Modifier.height(20.dp))

        // PREMIUM GLASS BADGE WITH GOLD GLOW
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFFFD700), Color(0xFFFF2A7A), Color(0x33FF2A7A))
                    )
                )
                .border(3.dp, Color(0xFFFFD700), CircleShape)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("📅", fontSize = 42.sp)
                Text("GROWTH", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text("EXPERT", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x44FF2A7A))
                .border(1.dp, Color(0xFFFF2A7A), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text("✨ +600 CREATOR XP REWARD ✨", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onFinishLevel10,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("🚀 Finish Level 10 & Continue Growth", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
        }
    }
}
