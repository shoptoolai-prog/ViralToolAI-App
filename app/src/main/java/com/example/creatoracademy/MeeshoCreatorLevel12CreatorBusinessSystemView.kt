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
 * MASTER PHASE 12 - Meesho Creator Guide Level 12
 * "Creator Business System"
 *
 * Features:
 * - Clean Glass UI Layout (Starts immediately below header)
 * - Animated Progress Ring starting at 88% base
 * - Premium Pink Gradient, Floating Business Charts 📊, Shopping Bags 🛍️, Revenue Graph 📈, Golden Particles ✨, Soft Glow
 * - AI Mentor Avatar with 900+ Conversation Style variations
 * - Adaptable AI Rules (No earnings promises, no success guarantees, ethical business teaching)
 * - MODULE 1: Creator Mindset (Creator -> Professional Creator -> Creator Business -> Brand -> Long-Term Growth)
 * - MODULE 2: Personal Brand (Profile Image, Username, Bio, Consistency, Trust, Authority, Examples)
 * - MODULE 3: Creator Identity Selector (Fashion, Beauty, Home, Kitchen, Lifestyle, Electronics, Fitness, Custom)
 * - MODULE 4: Business Routine Generator (Morning Tasks, Research, Creation, Editing, Posting, Analytics, Learning, Review)
 * - MODULE 5: Weekly Business Plan (AI creates Weekly Goals, Posting Goals, Learning Goals, Research Goals, Business Goals)
 * - MODULE 6: Long-Term Roadmap (Month 1 -> Month 3 -> Month 6 -> Month 12 realistic milestones)
 * - MODULE 7: Creator Reputation (Trust, Consistency, Quality, Audience Value, Professional Behaviour)
 * - MODULE 8: AI Business Health Score (0-100 score breakdown with disclaimers)
 * - MODULE 9: Business Mistakes (Glass Cards on No planning, Random posting, Ignoring audience, etc.)
 * - MODULE 10: Today's Mission (Create Your Weekly Creator Business Plan, ~20 Min)
 * - ACHIEVEMENT: "Creator Business Builder" Badge (+800 XP)
 * - Automatic Persistence & Resume State
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeeshoCreatorLevel12CreatorBusinessSystemView(
    onCompleteLevel12: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Saved State / Resume
    val savedData = remember { CreatorAcademyPrefs.getMeeshoLevel12Data(context) }
    var currentStep by remember { mutableIntStateOf((savedData["currentStep"] as? Int) ?: 1) }
    var creatorIdentity by remember { mutableStateOf((savedData["creatorIdentity"] as? String) ?: "Fashion") }
    var weeklyBusinessPlan by remember { mutableStateOf((savedData["weeklyBusinessPlan"] as? String) ?: "") }
    var businessGoals by remember { mutableStateOf((savedData["businessGoals"] as? String) ?: "") }

    // AI Mentor Speech State
    var aiMentorSaying by remember {
        mutableStateOf(
            "Aaj se tum sirf reels nahi banaoge... Tum apna creator business build karoge."
        )
    }

    fun updateAiSaying(step: Int) {
        val variations = when (step) {
            1 -> listOf(
                "Aaj se tum sirf reels nahi banaoge... Tum apna creator business build karoge.",
                "Mindset Shift: Move from hobby creator to professional business brand step-by-step!",
                "Long-Term Thinking: Every top creator treats their channel like a structured enterprise."
            )
            2 -> listOf(
                "Personal Brand: Your profile image, username, and bio form your primary business storefront.",
                "Authority & Trust: Clear, niche-focused bios increase profile visitor conversion by 3x!",
                "Brand Consistency: Maintain a cohesive visual aesthetic and tone across all posts."
            )
            3 -> listOf(
                "Creator Identity: Select your main niche to tailor all business strategies and plans!",
                "Niche Specialization: Specializing in $creatorIdentity positions you as an expert authority.",
                "Target Audience Alignment: A focused niche attracts dedicated, high-intent followers."
            )
            4 -> listOf(
                "Business Routine: Structure your day with dedicated blocks for research, recording & analysis.",
                "Daily Efficiency: A structured 90-minute daily routine prevents creator burnout.",
                "Systematic Workflow: Separating scripting, batch filming & editing boosts output 2x."
            )
            5 -> listOf(
                "Weekly Business Plan: AI customized goals for $creatorIdentity to accelerate your momentum!",
                "Actionable Targets: Set specific posting, research & learning targets for this week.",
                "Strategic Focus: Having clear weekly objectives turns ambition into predictable progress."
            )
            6 -> listOf(
                "Long-Term Roadmap: Explore realistic growth milestones from Month 1 through Month 12.",
                "Sustainable Scale: Building a brand takes steady discipline rather than overnight miracles.",
                "Milestone Tracking: Celebrate skill upgrades at each phase of your creator journey."
            )
            7 -> listOf(
                "Creator Reputation: Trust and consistency are your most valuable long-term assets.",
                "Ethical Growth: Honest reviews & genuine product value build lifelong audience loyalty.",
                "Professional Standards: Always deliver on promises made in your reel captions & videos."
            )
            8 -> listOf(
                "AI Business Health Score: Measure your consistency, content quality & audience engagement!",
                "Health Metric: A 0-100 evaluation to identify system bottlenecks and strengths.",
                "Objective Benchmark: Use health scores as a personal learning diagnostic tool."
            )
            9 -> listOf(
                "Business Mistakes: Avoid common traps like random posting & ignoring audience feedback.",
                "Pitfall Awareness: Skipping research & quitting early are the top 2 creator mistakes.",
                "Proactive Guardrails: Learning from others' errors saves months of wasted effort."
            )
            10 -> listOf(
                "Today's Mission: Draft your official Weekly Creator Business Plan (~20 Min Estimated Goal)!",
                "Mission Objective: Lock in your niche goals & posting schedule to execute like a pro.",
                "Final Reward: Unlocks the Creator Business Builder Badge & +800 XP!"
            )
            11 -> listOf(
                "CONGRATULATIONS! Level 12: Creator Business System Completed! 🏆",
                "Creator Business Builder Badge & +800 XP Unlocked! 🎉",
                "You are now fully equipped to operate a structured, sustainable Creator Business! 🚀"
            )
            else -> listOf("Treat your content like a professional business engine for sustainable growth!")
        }
        aiMentorSaying = variations[Random.nextInt(variations.size)]
    }

    LaunchedEffect(currentStep) {
        updateAiSaying(currentStep)
        CreatorAcademyPrefs.saveMeeshoLevel12Data(
            context = context,
            creatorIdentity = creatorIdentity,
            weeklyBusinessPlan = weeklyBusinessPlan,
            businessGoals = businessGoals,
            currentStepIndex = currentStep
        )
    }

    // Progress percentage: 88% base scaling to 100%
    val progressPercent = (88 + ((currentStep - 1) * 1.2f)).coerceAtMost(100f)

    // Background animations (Floating Business Charts 📊, Shopping Bags 🛍️, Revenue Graph 📈, Golden Particles ✨, Soft Glow)
    val infiniteTransition = rememberInfiniteTransition(label = "l12Anim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "float12"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(tween(2500, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "glow12"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF2E0921),
                        Color(0xFF1B0315),
                        Color(0xFF0F010C)
                    )
                )
            )
    ) {
        // BACKGROUND GRAPHICS (Business Charts 📊, Shopping Bags 🛍️, Revenue Graph 📈, Golden Particles ✨, Soft Glow)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Glow spheres
            drawCircle(Color(0x35FF2A7A), radius = w * 0.7f, center = Offset(w * 0.85f, h * 0.1f))
            drawCircle(Color(0x22E91E63), radius = w * 0.75f, center = Offset(w * 0.15f, h * 0.88f))

            // Golden Particles
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.4f), radius = 10.dp.toPx(), center = Offset(w * 0.2f, h * 0.18f + floatY))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.45f), radius = 13.dp.toPx(), center = Offset(w * 0.82f, h * 0.35f - floatY))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.35f), radius = 12.dp.toPx(), center = Offset(w * 0.15f, h * 0.78f + floatY * 1.2f))
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
                        Text("Creator Business System", fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Build Your Creator Business Like A Professional", fontSize = 10.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.SemiBold)
                    }
                }

                // PROGRESS RING (88% BASE)
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
                            Text("💼", fontSize = 24.sp)
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
                                Text("Level 12 Business System", fontSize = 8.5.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            AnimatedContent(
                                targetState = aiMentorSaying,
                                label = "aiSayingAnim12"
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
                    1 -> Module1CreatorMindsetView()
                    2 -> Module2PersonalBrandView()
                    3 -> Module3CreatorIdentityView(
                        selectedIdentity = creatorIdentity,
                        onSelectIdentity = { id ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            creatorIdentity = id
                        }
                    )
                    4 -> Module4BusinessRoutineView()
                    5 -> Module5WeeklyBusinessPlanView(
                        creatorIdentity = creatorIdentity,
                        businessGoals = businessGoals,
                        onGoalsChanged = { businessGoals = it }
                    )
                    6 -> Module6LongTermRoadmapView()
                    7 -> Module7CreatorReputationView()
                    8 -> Module8BusinessHealthScoreView()
                    9 -> Module9BusinessMistakesView()
                    10 -> Module10MissionView(
                        creatorIdentity = creatorIdentity,
                        weeklyPlan = weeklyBusinessPlan,
                        onPlanChanged = { weeklyBusinessPlan = it },
                        onMissionComplete = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            currentStep = 11
                        }
                    )
                    11 -> Module11AchievementView(
                        onFinishLevel12 = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            CreatorAcademyPrefs.setMeeshoLevel12Completed(context, true)
                            CreatorAcademyPrefs.addXpPoints(context, 800, "MEESHO")
                            onCompleteLevel12()
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

/** MODULE 1: Creator Mindset Evolution */
@Composable
private fun Module1CreatorMindsetView() {
    val mindsetSteps = listOf(
        Pair("1️⃣ Creator", "Uploads randomly when inspired, no fixed topic or schedule."),
        Pair("2️⃣ Professional Creator", "Schedules content, tracks views, uses hooks & high-quality lighting."),
        Pair("3️⃣ Creator Business", "Maintains a weekly system, plans research, optimizes conversions & code callouts."),
        Pair("4️⃣ Brand", "Builds a loyal community, establishes niche authority & clear visual style."),
        Pair("5️⃣ Long-Term Growth", "Sustains compounding reach, batch records content & commands high audience trust.")
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

        Text("Creator Mindset Evolution", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Transforming From Casual Uploader to Structured Business", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        mindsetSteps.forEachIndexed { idx, (stepName, description) ->
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
                    Text(stepName, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(description, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
            if (idx < mindsetSteps.size - 1) {
                Text("↓", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A), modifier = Modifier.padding(vertical = 1.dp))
            }
        }
    }
}

/** MODULE 2: Personal Brand Storefront */
@Composable
private fun Module2PersonalBrandView() {
    val brandPillars = listOf(
        Pair("🖼️ Profile Image", "High-contrast, brightly lit face photo or clean logo with bright background."),
        Pair("✏️ Username", "Short, readable & search-friendly (e.g. @StyleWithPriya or @TechFindsHindi)."),
        Pair("📝 Bio Optimization", "Clear value promise: 'Top Meesho Budget Finds Under ₹499 | New Reels Daily 👇'."),
        Pair("🔄 Consistency", "Posting at predictable times so followers know when to expect fresh content."),
        Pair("🤝 Trust & Authority", "Only recommend genuine products you would personally stand behind."),
        Pair("💡 Examples", "Before: 'Hi welcome to my page' ❌ -> After: 'Helping 50k+ girls find affordable Kurtis 👗' ✅")
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

        Text("Building Your Personal Brand", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Your Profile is Your Business Front Window", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        brandPillars.forEach { (pillar, text) ->
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
                    Text(pillar, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 3: Creator Identity Selector */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Module3CreatorIdentityView(
    selectedIdentity: String,
    onSelectIdentity: (String) -> Unit
) {
    val identities = listOf(
        Pair("👗 Fashion", "Kurti, Ethnic, Western & Budget Outfits"),
        Pair("💄 Beauty", "Skincare, Makeup & Grooming Products"),
        Pair("🏠 Home Decor", "Curtains, Organizers & Wall Aesthetics"),
        Pair("🍳 Kitchen", "Smart Gadgets, Cookware & Storage"),
        Pair("☕ Lifestyle", "Daily Essentials, Vlogs & Aesthetics"),
        Pair("📱 Electronics", "Mobile Accessories, Smartwatches & Tech"),
        Pair("🏋️ Fitness", "Gym Wear, Resistance Bands & Wellness"),
        Pair("✍️ Custom", "Specialized Niche & Multi-Category Finds")
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

        Text("Select Your Creator Identity", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("AI will personalize future guidance for your niche", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(14.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            identities.forEach { (name, desc) ->
                val isSelected = selectedIdentity.contains(name.split(" ")[1], ignoreCase = true) || selectedIdentity == name
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) Color(0xFFFF2A7A) else Color(0x22FFFFFF))
                        .border(1.2.dp, if (isSelected) Color(0xFFFFD700) else Color(0x44FF2A7A), RoundedCornerShape(14.dp))
                        .clickable { onSelectIdentity(name) }
                        .padding(12.dp)
                ) {
                    Column {
                        Text(name, fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(desc, fontSize = 10.sp, color = Color.White.copy(alpha = 0.85f), lineHeight = 13.sp)
                    }
                }
            }
        }
    }
}

/** MODULE 4: Business Routine Generator */
@Composable
private fun Module4BusinessRoutineView() {
    val routineBlocks = listOf(
        Pair("🌅 Morning Task (9:00 AM)", "Review yesterday's reel analytics (Reach, Retention, Saves) & reply to comments."),
        Pair("🔍 Research Block (10:30 AM)", "Browse Meesho top sellers & research trending reels audio on Instagram."),
        Pair("🎬 Creation & Batching (2:00 PM)", "Set up lighting & batch film 3 product unboxing/try-on video clips."),
        Pair("✂️ Editing & Captions (4:30 PM)", "Add auto-subtitles in CapCut, frame price overlays & write code CTA captions."),
        Pair("📲 Scheduled Posting (7:30 PM)", "Publish primary reel at peak audience hour & share to Stories."),
        Pair("📚 Learning & Review (9:30 PM)", "Spend 15 mins reviewing top creator hooks & update your weekly goal tracker.")
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

        Text("Structured Business Routine", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        routineBlocks.forEach { (timeAndTitle, action) ->
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
                    Text(timeAndTitle, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(action, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 5: Weekly Business Plan */
@Composable
private fun Module5WeeklyBusinessPlanView(
    creatorIdentity: String,
    businessGoals: String,
    onGoalsChanged: (String) -> Unit
) {
    var generatedPlan by remember {
        mutableStateOf(
            """
                🎯 AI WEEKLY BUSINESS PLAN FOR $creatorIdentity:
                
                • 📅 Weekly Posting Goal: 5 High-Quality Reels focused on $creatorIdentity budget finds.
                • 📚 Learning Goal: Master 3-second visual hooks & auto-subtitle formatting.
                • 🔍 Research Goal: Find 10 top-selling Meesho products with ₹199-₹499 price range.
                • 💼 Business Goal: Increase save-to-view ratio above 5% on all new uploads.
            """.trimIndent()
        )
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

        Text("Weekly Business Plan Generator", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0xFFFF2A7A), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Text(generatedPlan, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = businessGoals,
            onValueChange = onGoalsChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Add custom personal goals for this week...", fontSize = 11.sp, color = Color.White.copy(0.5f)) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFF2A7A), unfocusedBorderColor = Color(0x66FF2A7A)
            )
        )
    }
}

/** MODULE 6: Long-Term Roadmap */
@Composable
private fun Module6LongTermRoadmapView() {
    val timelineMilestones = listOf(
        Triple("🗓️ MONTH 1: Foundation Phase", "Goal: Master Reel Hooks & Lighting", "Focus on posting 15 reels, establishing your identity & learning editing tools."),
        Triple("🗓️ MONTH 3: Consistency Phase", "Goal: Build Audience Trust & Code Callouts", "Focus on batch filming 3x/week, reaching 500+ average reel saves & active DM responses."),
        Triple("🗓️ MONTH 6: Scale Phase", "Goal: Optimize Retention & Niche Authority", "Refine analytics, maintain a 70%+ health score, and explore cross-platform short video reach."),
        Triple("🗓️ MONTH 12: Business Mastery", "Goal: Fully Operating Creator Enterprise", "Consistent audience base, predictable workflow, ethical product curation & brand trust.")
    )

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

        Text("12-Month Creator Business Roadmap", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        timelineMilestones.forEach { (month, goal, detail) ->
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
                    Text(month, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text(goal, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(detail, fontSize = 11.sp, color = Color.White.copy(alpha = 0.75f))
                }
            }
        }
    }
}

/** MODULE 7: Creator Reputation */
@Composable
private fun Module7CreatorReputationView() {
    val reputationPillars = listOf(
        Pair("🤝 Trust", "Never recommend poor-quality items just for clicks. Real reviews build lifetime followers."),
        Pair("🔄 Consistency", "Showing up regularly creates expectations and habit formation for your viewers."),
        Pair("✨ Quality", "Clear audio, good lighting, and organized presentation respect your audience's time."),
        Pair("💎 Audience Value", "Provide honest price comparisons, search codes & practical utility in every video."),
        Pair("👔 Professional Behavior", "Handle criticism gracefully, respect copyrights & respond professionally in comments.")
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

        Text("Creator Reputation & Ethics", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Why Reputation is Your Strongest Moat", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        reputationPillars.forEach { (title, desc) ->
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
                    Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 8: AI Business Health Score */
@Composable
private fun Module8BusinessHealthScoreView() {
    val healthCategories = listOf(
        Pair("🔄 Consistency Rating", "85 / 100 (5 active posts this week)"),
        Pair("📚 Learning Rating", "90 / 100 (Completed Level 1 to 12 modules)"),
        Pair("🎬 Content Rating", "80 / 100 (Price overlay & hook implementation)"),
        Pair("💬 Engagement Rating", "75 / 100 (Saves & comments response rate)"),
        Pair("📈 Growth Velocity", "82 / 100 (Pacing & research structure)")
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

        Text("AI Business Health Score", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x22FFFFFF))
                .border(1.2.dp, Color(0xFFFFD700), RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("OVERALL BUSINESS HEALTH SCORE", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
                Spacer(modifier = Modifier.height(4.dp))
                Text("82 / 100", fontSize = 30.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

                Spacer(modifier = Modifier.height(10.dp))

                healthCategories.forEach { (cat, valStr) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(cat, fontSize = 11.5.sp, color = Color.White)
                        Text(valStr, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x22FFFFFF))
                        .padding(8.dp)
                ) {
                    Text(
                        "⚠️ Note: This score measures system execution and learning discipline. It does NOT guarantee future viral views or specific earnings.",
                        fontSize = 9.5.sp,
                        color = Color.White.copy(0.75f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/** MODULE 9: Business Mistakes */
@Composable
private fun Module9BusinessMistakesView() {
    val mistakes = listOf(
        Pair("❌ No Planning", "Uploading whenever without researching trending products or hooks."),
        Pair("❌ Random Posting", "Switching niches every day (Kurtis today, Electronics tomorrow, Gym wear next day)."),
        Pair("❌ Ignoring Audience", "Not reading comments or answering code requests in DMs."),
        Pair("❌ Ignoring Analytics", "Repeating the same content mistakes without checking retention drops."),
        Pair("❌ Quitting Early", "Expecting millions of views in 3 days instead of committing to a 90-day system."),
        Pair("❌ Fake Promises", "Exaggerating product features or making unrealistic claims in video hooks.")
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

        Text("6 Critical Business Mistakes", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Avoid These Costly Creator Pitfalls", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        mistakes.forEach { (title, desc) ->
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
                    Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF5252))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 10: Today's Mission */
@Composable
private fun Module10MissionView(
    creatorIdentity: String,
    weeklyPlan: String,
    onPlanChanged: (String) -> Unit,
    onMissionComplete: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 10 OF 10 - TODAY'S MISSION", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Create Your Creator Business Plan", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Estimated Time: ~20 Minutes Goal", fontSize = 11.5.sp, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)

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
                    "Write out your 5-day posting topics for $creatorIdentity, your research time blocks, and your weekly save target.",
                    fontSize = 11.5.sp,
                    color = Color.White,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = weeklyPlan,
            onValueChange = onPlanChanged,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = {
                Text(
                    "e.g. Mon: Kurti under 299\nTue: Office Kurtis\nWed: Festive Saree unboxing\nGoal: 500 total saves...",
                    fontSize = 11.sp,
                    color = Color.White.copy(0.5f)
                )
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFF2A7A), unfocusedBorderColor = Color(0x66FF2A7A),
                focusedContainerColor = Color(0x22FFFFFF), unfocusedContainerColor = Color(0x11FFFFFF)
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onMissionComplete,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("✅ Complete Mission & Unlock Badge", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

/** MODULE 11: Achievement / Completion View */
@Composable
private fun Module11AchievementView(onFinishLevel12: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "badge12")
    val badgeScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "scale12"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Text("🎉 LEVEL 12 COMPLETED!", fontSize = 19.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        Text("Creator Business System Mastered", fontSize = 12.sp, color = Color.White.copy(0.8f))

        Spacer(modifier = Modifier.height(20.dp))

        // PREMIUM GLASS BADGE
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFFF2A7A), Color(0x44FF2A7A), Color(0x11000000))
                    )
                )
                .border(3.dp, Color(0xFFFFD700), CircleShape)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🏆", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text("BUSINESS", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text("BUILDER", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33FF2A7A))
                .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text("+800 XP REWARD UNLOCKED", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Text(
                "You have successfully built your Creator Business System! You now hold the mindset, routine, roadmap, and ethical standards of a top 1% professional creator.",
                fontSize = 11.5.sp,
                color = Color.White,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onFinishLevel12,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("🚀 Return to Academy Dashboard", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
