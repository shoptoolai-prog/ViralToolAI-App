package com.example.creatoracademy

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun BusinessGlassCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0x221E293B))
            .border(1.dp, Color(0x33FFD700), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandCollabLevel13BusinessDashboardView(
    userNiche: String,
    userPlatform: String,
    userName: String = "Creator",
    onLevel13Completed: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current

    val savedData = remember { CreatorAcademyPrefs.getBrandCollabLevel13Data(context) }
    var currentModule by remember { mutableIntStateOf((savedData["module"] as? Int) ?: 1) }
    var currentStep by remember { mutableIntStateOf((savedData["step"] as? Int) ?: 1) }
    var isAlreadyCompleted by remember { mutableStateOf((savedData["completed"] as? Boolean) ?: false) }

    // Business Goal State
    var selectedMonthlyGoal by remember { mutableStateOf((savedData["monthlyGoal"] as? String) ?: "₹50,000 Goal") }
    var customGoalInput by remember { mutableStateOf("") }
    var userTotalXp by remember { mutableIntStateOf((savedData["currentXp"] as? Int) ?: 1250) }

    // Daily Missions State
    val dailyMissions = remember {
        mutableStateListOf(
            "Improve Portfolio" to true,
            "Contact One Brand" to true,
            "Study One Campaign" to true,
            "Reply To Brands" to false,
            "Update Media Kit" to true,
            "Complete One Lesson" to false
        )
    }

    // Success Habits Checklist
    val successHabits = remember {
        mutableStateListOf(
            "Learn daily content strategy" to true,
            "Improve video lighting & audio" to true,
            "Contact 1 new brand manager" to false,
            "Update portfolio metrics & reach" to true,
            "Practice brand rate negotiation" to true,
            "Complete Creator Academy mission" to false
        )
    }

    // Final Mission Tasks
    val missionChecklist = remember {
        mutableStateListOf(
            true, // Creator Business Profile Setup
            true, // Financial Target Configured
            true, // Daily Missions & Habits Active
            false, // Monthly Analytics Reviewed
            false // Business Manager Badge Verified
        )
    }

    var showFullReportModal by remember { mutableStateOf(false) }

    // Persist helper
    fun persistState(completed: Boolean = isAlreadyCompleted) {
        CreatorAcademyPrefs.saveBrandCollabLevel13State(
            context = context,
            step = currentStep,
            module = currentModule,
            monthlyGoal = selectedMonthlyGoal,
            currentXp = userTotalXp,
            isCompleted = completed
        )
    }

    val progressPercent = 100
    val progressRingAngle by animateFloatAsState(
        targetValue = 360f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "level13ProgressRing"
    )

    // Background animation
    val infiniteTransition = rememberInfiniteTransition(label = "businessBg")
    val floatingOffsetY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatingOffsetBusiness"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF0F172A))))
    ) {
        // Floating Gold Particles & Financial Growth Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0xFFFFD700).copy(alpha = 0.22f), radius = 14.dp.toPx(), center = Offset(w * 0.12f, h * 0.12f + floatingOffsetY * 2f))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.18f), radius = 18.dp.toPx(), center = Offset(w * 0.88f, h * 0.25f - floatingOffsetY * 2.2f))
            drawCircle(Color(0x3338BDF8), radius = 16.dp.toPx(), center = Offset(w * 0.20f, h * 0.72f + floatingOffsetY * 2.5f))
            drawCircle(Color(0x224ADE80), radius = 20.dp.toPx(), center = Offset(w * 0.80f, h * 0.85f - floatingOffsetY * 2.8f))

            // Revenue Graph Canvas Overlay
            val graphPath = Path().apply {
                moveTo(w * 0.08f, h * 0.38f)
                cubicTo(w * 0.3f, h * 0.36f, w * 0.5f, h * 0.32f, w * 0.7f, h * 0.28f)
                lineTo(w * 0.92f, h * 0.22f)
            }
            drawPath(
                path = graphPath,
                color = Color(0x33FFD700),
                style = Stroke(width = 2.dp.toPx())
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // HEADER: Creator Business Dashboard
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xCC1E293B))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (showFullReportModal) {
                            showFullReportModal = false
                        } else if (currentModule > 1) {
                            currentModule--
                            persistState()
                        } else {
                            onBack()
                        }
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                ) {
                    Text("←", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Creator Business Dashboard",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0x33FFD700))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("LEVEL 13", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                        }
                    }
                    Text(
                        text = "Run Your Creator Business Like A CEO",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }

                // 100% Milestone Animated Progress Ring
                Box(
                    modifier = Modifier.size(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = Color.White.copy(alpha = 0.15f),
                            style = Stroke(width = 3.dp.toPx())
                        )
                        drawArc(
                            color = Color(0xFFFFD700),
                            startAngle = -90f,
                            sweepAngle = progressRingAngle,
                            useCenter = false,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "$progressPercent%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFD700)
                    )
                }
            }

            // Scrollable Content Body
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                if (!showFullReportModal) {
                    // Marquee Brand Logos Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("💼 Boat Audio", "👔 Snitch", "✨ Minimalist", "🛍 Myntra", "📦 Amazon", "💄 Nykaa", "📱 Samsung", "🎧 OnePlus").forEach { logo ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0x22FFD700))
                                    .border(1.dp, Color(0x44FFD700), RoundedCornerShape(20.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(logo, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            }
                        }
                    }

                    // AI MENTOR CARD
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.linearGradient(listOf(Color(0x331E293B), Color(0x33334155))))
                            .border(1.dp, Color(0x44FFD700), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFD700))
                                    .border(2.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("👑", fontSize = 22.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("AI Business Mentor", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("• CEO Advisor", fontSize = 9.sp, color = Color(0xFF4ADE80))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                val mentorSpeech = when (currentModule) {
                                    1 -> "Congratulations! Ab tum sirf content creator nahi... Ek Creator Business Owner ho. Yeh dashboard tumhare poore creator business ko CEO benchmark standard par manage karega."
                                    2 -> "Monthly Analytics track karta hai tumhari deal closure velocity, brand satisfaction rates & monthly revenue growth momentum."
                                    3 -> "Yearly Growth Roadmap: 0 → ₹1 Lakh/month → ₹3 Lakhs/month → Custom Creator Enterprise Empire!"
                                    4 -> "AI Business Insights: Real-time analysis of your biggest strength, pitch gaps, and highest priority revenue actions."
                                    5 -> "Daily Missions: Complete high-value tasks daily to earn XP and build unstoppable brand deal momentum!"
                                    6 -> "Weekly Challenge: Push your boundary to pitch 5 brands, draft 3 plans, and win your highest paying deal."
                                    7 -> "Select your Monthly Revenue Target: ₹10,000, ₹25,000, ₹50,000, or ₹1,00,000+ per month."
                                    8 -> "Creator Levels: Unlock prestigious creator badges from 'Beginner' to 'Creator Legend' with XP!"
                                    9 -> "AI Performance Review: 360-degree review of your consistency, communication, professionalism & overall score."
                                    10 -> "Creator Achievements & Daily Habits: Track your badges earned and stick to daily winning habits."
                                    11 -> "Final Mission: Complete Creator Business Dashboard setup to claim your Creator Business Manager Badge & +1000 XP!"
                                    else -> "Creator Business Dashboard active! Lead your creator enterprise with confidence."
                                }
                                Text(
                                    text = mentorSpeech,
                                    fontSize = 11.5.sp,
                                    color = Color.White,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Module Navigation Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(bottom = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val tabs = listOf(
                            1 to "1. Home",
                            2 to "2. Analytics",
                            3 to "3. Yearly",
                            4 to "4. Insights",
                            5 to "5. Missions",
                            6 to "6. Weekly",
                            7 to "7. Targets",
                            8 to "8. Levels",
                            9 to "9. Review",
                            10 to "10. Badges",
                            11 to "11. Final"
                        )
                        tabs.forEach { (modNum, modLabel) ->
                            val isSelected = currentModule == modNum
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                    .clickable {
                                        currentModule = modNum
                                        persistState()
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = modLabel,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.Black else Color.White
                                )
                            }
                        }
                    }

                    // MODULE DISPLAY CASES
                    when (currentModule) {
                        // 1. HOME DASHBOARD
                        1 -> {
                            BusinessGlassCard(title = "1. Creator CEO Overview") {
                                Text("Key business metrics at a glance:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0x33FFD700))
                                            .padding(10.dp)
                                    ) {
                                        Column {
                                            Text("BRAND READINESS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            Text("98 / 100", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                            Text("GRADE A+", fontSize = 9.sp, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0x22FFFFFF))
                                            .padding(10.dp)
                                    ) {
                                        Column {
                                            Text("CREATOR LEVEL", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            Text("Professional", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            Text("XP: $userTotalXp / 3500", fontSize = 9.sp, color = Color(0xFFFFD700))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                val keyStats = listOf(
                                    "🎯 Monthly Revenue Target" to selectedMonthlyGoal,
                                    "📅 Pending Brand Campaigns" to "2 Active (Boat, Snitch)",
                                    "✅ Completed Brand Deals" to "12 Closed Collaborations",
                                    "📨 Brands Contacted (Outreach)" to "34 Brand Managers",
                                    "⚡ Pitch Response Rate" to "42.8% Industry Leading",
                                    "⭐ Creator Reputation Score" to "9.6 / 10.0 Excellent"
                                )

                                keyStats.forEach { (label, value) ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x11FFFFFF))
                                            .padding(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(label, fontSize = 11.sp, color = Color.White)
                                            Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                        }
                                    }
                                }
                            }
                        }

                        // 2. MONTHLY ANALYTICS
                        2 -> {
                            BusinessGlassCard(title = "2. Monthly Business Analytics") {
                                Text("August 2026 Monthly Performance Summary:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                val metrics = listOf(
                                    "💼 Deals Completed This Month" to "4 Deals (₹42,000 Earned)",
                                    "📈 Monthly Income Progress" to "84% of $selectedMonthlyGoal",
                                    "🎬 Campaign Completion Rate" to "100% On Time Delivery",
                                    "⭐ Brand Satisfaction Score" to "4.9 / 5.0 Rating",
                                    "🚀 Growth Score (MoM)" to "+28.5% Revenue Growth",
                                    "🎯 Mission Completion Rate" to "95% Daily Habits Met"
                                )

                                metrics.forEach { (title, stat) ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0x22FFFFFF))
                                            .border(1.dp, Color(0x22FFD700), RoundedCornerShape(12.dp))
                                            .padding(10.dp)
                                    ) {
                                        Column {
                                            Text(title, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(stat, fontSize = 11.sp, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }

                        // 3. YEARLY GROWTH
                        3 -> {
                            BusinessGlassCard(title = "3. Yearly Creator Growth Roadmap") {
                                Text("Long-term career projection timeline:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                val timelineSteps = listOf(
                                    "📍 Current Position" to "₹42,000/mo • 125K Engaged Followers • 4 Paid Retainers",
                                    "🎯 Next Goal (3 Months)" to "₹1,00,000/mo • 250K Followers • Brand Ambassador Deals",
                                    "🚀 Long-Term Target (1 Year)" to "₹3,00,000/mo • 500K Community • Dedicated Content Team",
                                    "👑 Dream Milestone" to "₹10,00,000/mo Enterprise • Launching Own Direct-to-Consumer Brand"
                                )

                                timelineSteps.forEachIndexed { idx, (stepTitle, stepDetail) ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(if (idx == 0) Color(0xFF4ADE80) else Color(0xFFFFD700)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("${idx + 1}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(stepTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            Text(stepDetail, fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.75f))
                                        }
                                    }
                                }
                            }
                        }

                        // 4. AI BUSINESS INSIGHTS
                        4 -> {
                            BusinessGlassCard(title = "4. AI Business Insights Engine") {
                                Text("Personalized strategic diagnostics based on course data:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                val insights = listOf(
                                    "💪 Biggest Strength" to "High Reel Engagement (8.4%) & 24hr delivery turnaround.",
                                    "⚠️ Weakest Area" to "Rate Card Negotiation - Underpricing long-term usage rights.",
                                    "🌟 Best Opportunity" to "3-Month Retainer Deals with Consumer Tech & Fashion brands.",
                                    "⚡ Highest Priority" to "Syndicate reels across YouTube Shorts & Instagram simultaneously.",
                                    "🎯 Next Focus Area" to "Build automated follow-up email sequence for brand leads."
                                )

                                insights.forEach { (head, desc) ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x22FFFFFF))
                                            .padding(10.dp)
                                    ) {
                                        Column {
                                            Text(head, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(desc, fontSize = 11.sp, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }

                        // 5. DAILY MISSIONS
                        5 -> {
                            BusinessGlassCard(title = "5. Interactive Daily Missions & XP") {
                                Text("Complete daily tasks to unlock bonus XP points:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                dailyMissions.forEachIndexed { idx, (taskName, isDone) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isDone) Color(0x224ADE80) else Color(0x11FFFFFF))
                                            .clickable {
                                                dailyMissions[idx] = taskName to !isDone
                                                if (!isDone) userTotalXp += 150 else userTotalXp -= 150
                                                persistState()
                                            }
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = isDone,
                                            onCheckedChange = { checked ->
                                                dailyMissions[idx] = taskName to checked
                                                if (checked) userTotalXp += 150 else userTotalXp -= 150
                                                persistState()
                                            },
                                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4ADE80), uncheckedColor = Color.White)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(taskName, fontSize = 11.5.sp, color = if (isDone) Color(0xFF4ADE80) else Color.White, modifier = Modifier.weight(1f))
                                        Text("+150 XP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    }
                                }
                            }
                        }

                        // 6. WEEKLY CHALLENGE
                        6 -> {
                            BusinessGlassCard(title = "6. Weekly Creator CEO Challenge") {
                                Text("Complete 5 high-impact business targets this week:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                val weeklyTasks = listOf(
                                    "Contact 5 Brand Managers via Email" to "4 / 5 Done",
                                    "Complete 3 AI Campaign Strategy Plans" to "3 / 3 Done ✅",
                                    "Improve Portfolio Score above 95%" to "Completed (98%) ✅",
                                    "Create & Publish New Media Kit PDF" to "Completed ✅",
                                    "Win First Paid Reply of the Week" to "In Progress"
                                )

                                weeklyTasks.forEach { (task, status) ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x22FFFFFF))
                                            .padding(10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(task, fontSize = 11.sp, color = Color.White, modifier = Modifier.weight(1f))
                                            Text(status, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                        }
                                    }
                                }
                            }
                        }

                        // 7. MONTHLY TARGET SELECTOR
                        7 -> {
                            BusinessGlassCard(title = "7. Monthly Revenue Goal Selector") {
                                Text("Choose or customize your monthly earning target:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                val goalOptions = listOf("First Paid Deal", "₹10,000 Goal", "₹25,000 Goal", "₹50,000 Goal", "₹100,000 Goal")
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    goalOptions.forEach { option ->
                                        val isSel = selectedMonthlyGoal == option
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(20.dp))
                                                .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                                .border(1.dp, if (isSel) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                                                .clickable {
                                                    selectedMonthlyGoal = option
                                                    persistState()
                                                }
                                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = option,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSel) Color.Black else Color.White
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text("Or Enter Custom Earning Goal:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = customGoalInput,
                                        onValueChange = { customGoalInput = it },
                                        placeholder = { Text("e.g. ₹1,50,000 Goal", fontSize = 11.sp, color = Color.White.copy(alpha = 0.4f)) },
                                        modifier = Modifier.weight(1f),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFFFFD700),
                                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White
                                        ),
                                        singleLine = true
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            if (customGoalInput.isNotBlank()) {
                                                selectedMonthlyGoal = customGoalInput
                                                persistState()
                                                Toast.makeText(context, "Goal updated to $customGoalInput", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                                    ) {
                                        Text("Set", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }
                        }

                        // 8. CREATOR LEVELS
                        8 -> {
                            BusinessGlassCard(title = "8. Creator Progression Levels & XP") {
                                Text("Unlock higher level perks by accumulating XP:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                val levels = listOf(
                                    "🌱 Beginner Creator" to "0 - 500 XP",
                                    "🌿 Growing Creator" to "501 - 1,500 XP",
                                    "💼 Professional Creator" to "1,501 - 3,500 XP (CURRENT)",
                                    "✨ Verified Creator" to "3,501 - 7,000 XP",
                                    "👑 Elite Creator" to "7,001 - 15,000 XP",
                                    "🌌 Creator Legend" to "15,000+ XP"
                                )

                                levels.forEach { (lvlName, xpRange) ->
                                    val isCurrent = lvlName.contains("CURRENT")
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isCurrent) Color(0x33FFD700) else Color(0x11FFFFFF))
                                            .border(1.dp, if (isCurrent) Color(0xFFFFD700) else Color.Transparent, RoundedCornerShape(10.dp))
                                            .padding(10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(lvlName, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = if (isCurrent) Color(0xFFFFD700) else Color.White)
                                            Text(xpRange, fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
                                        }
                                    }
                                }
                            }
                        }

                        // 9. AI PERFORMANCE REVIEW
                        9 -> {
                            BusinessGlassCard(title = "9. AI 360° Performance Review") {
                                Text("Automated evaluation across 5 business dimensions:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                val reviewAspects = listOf(
                                    "⚡ Posting & Outreach Consistency" to "96% (Grade A+)",
                                    "💬 Brand Communication & Manners" to "92% (Grade A)",
                                    "💼 Professionalism & Deadlines" to "98% (Grade A+)",
                                    "📚 Learning & Strategy Progress" to "100% (Grade S)",
                                    "📈 Overall Business & Income Growth" to "94% (Grade A+)"
                                )

                                reviewAspects.forEach { (aspect, score) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(aspect, fontSize = 11.sp, color = Color.White)
                                        Text(score, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    }
                                }
                            }
                        }

                        // 10. ACHIEVEMENTS & HABITS
                        10 -> {
                            BusinessGlassCard(title = "10. Creator Badges & Success Habits") {
                                Text("Badges Unlocked & Daily Success Habits Checklist:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                val badges = listOf("First Deal 🏆", "5 Deals 🌟", "10 Deals 💼", "25 Deals 🚀", "50 Deals 👑", "100 Deals 💎")
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    badges.forEachIndexed { idx, badge ->
                                        val unlocked = idx < 3
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (unlocked) Color(0x33FFD700) else Color(0x11FFFFFF))
                                                .border(1.dp, if (unlocked) Color(0xFFFFD700) else Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = if (unlocked) badge else "🔒 $badge",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (unlocked) Color(0xFFFFD700) else Color.White.copy(alpha = 0.4f)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Daily Success Habits Checklist:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                Spacer(modifier = Modifier.height(6.dp))

                                successHabits.forEachIndexed { idx, (habitName, isHabitDone) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp)
                                            .clickable { successHabits[idx] = habitName to !isHabitDone }
                                    ) {
                                        Text(if (isHabitDone) "✔ " else "○ ", color = if (isHabitDone) Color(0xFF4ADE80) else Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(habitName, color = if (isHabitDone) Color(0xFF4ADE80) else Color.White, fontSize = 11.sp)
                                    }
                                }
                            }
                        }

                        // 11. FINAL MISSION & ACHIEVEMENT
                        11 -> {
                            BusinessGlassCard(title = "11. Master Level 13 Final Mission") {
                                Text("Complete Creator Business Dashboard (Estimated Time: 10 Minutes)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                Spacer(modifier = Modifier.height(8.dp))

                                val finalTasks = listOf(
                                    "Creator Business Profile Setup verified",
                                    "Financial Target & Monthly Goal configured",
                                    "Daily Missions & Success Habits active",
                                    "Monthly Analytics & Growth Roadmap reviewed",
                                    "Creator Business Manager Badge unlocked"
                                )

                                finalTasks.forEachIndexed { idx, task ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (missionChecklist[idx]) Color(0x224ADE80) else Color(0x11FFFFFF))
                                            .clickable { missionChecklist[idx] = !missionChecklist[idx] }
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = missionChecklist[idx],
                                            onCheckedChange = { checked -> missionChecklist[idx] = checked },
                                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4ADE80), uncheckedColor = Color.White)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(task, fontSize = 11.sp, color = if (missionChecklist[idx]) Color(0xFF4ADE80) else Color.White)
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // MASTER GLASS BADGE & REWARD
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Brush.linearGradient(listOf(Color(0x44FFD700), Color(0x22FFD700))))
                                        .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(16.dp))
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("🏆 MASTER ACHIEVEMENT UNLOCKED", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Creator Business Manager", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("+1000 XP Reward • Level 13 Creator Business Dashboard Completed", fontSize = 11.sp, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            showFullReportModal = true
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFD700)),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp)
                                    ) {
                                        Text("📊 Full CEO Report", color = Color(0xFFFFD700), fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Button(
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            isAlreadyCompleted = true
                                            persistState(true)
                                            onLevel13Completed()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp)
                                    ) {
                                        Text("🎉 Finish Phase 14", color = Color.Black, fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // NEXT / PREVIOUS NAVIGATION BUTTONS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (currentModule > 1) {
                                    currentModule--
                                    persistState()
                                }
                            },
                            enabled = currentModule > 1,
                            border = BorderStroke(1.dp, if (currentModule > 1) Color(0xFFFFD700) else Color.Gray),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                        ) {
                            Text("← Prev Module", fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                if (currentModule < 11) {
                                    currentModule++
                                    persistState()
                                } else {
                                    isAlreadyCompleted = true
                                    persistState(true)
                                    onLevel13Completed()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                        ) {
                            Text(
                                text = if (currentModule < 11) "Next Module →" else "Finish Level 13 🏆",
                                color = Color.Black,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    // FULL CONSOLIDATED CEO REPORT PREVIEW MODAL VIEW
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFF0F172A))
                            .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("💼 CREATOR BUSINESS CEO REPORT", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                IconButton(
                                    onClick = { showFullReportModal = false },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Text("✕", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            val fullTextReport = """
                                👑 CREATOR BUSINESS EXECUTIVE REPORT
                                Owner: $userName
                                Niche: ${userNiche.ifBlank { "Tech & Lifestyle" }}
                                Platform: ${userPlatform.ifBlank { "Instagram & YouTube" }}
                                
                                📊 KEY METRICS:
                                • Brand Readiness Score: 98/100 (Grade A+)
                                • Monthly Goal: $selectedMonthlyGoal
                                • Completed Deals: 12 Collaborations Closed
                                • Pending Campaigns: 2 Active (Boat, Snitch)
                                • Brand Satisfaction Rate: 4.9/5 ⭐
                                • Pitch Response Rate: 42.8%
                                
                                🎯 1-YEAR ROADMAP:
                                • Current: ₹42,000/mo (125K Followers)
                                • Target: ₹1,00,000/mo (250K Followers)
                                • Long-Term: ₹3,00,000/mo Enterprise
                                
                                🏆 BADGE: Creator Business Manager
                                Verified by Creator Academy AI Mentor
                            """.trimIndent()

                            Text(
                                text = fullTextReport,
                                fontSize = 11.sp,
                                color = Color.White,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(fullTextReport))
                                    Toast.makeText(context, "Full Business CEO Report copied to clipboard!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("📋 Copy Full Business Report", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
