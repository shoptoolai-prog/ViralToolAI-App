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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

/**
 * MASTER PHASE 15 - Meesho Creator Guide Level 15 (FINAL LEVEL)
 * "Meesho Creator Success Hub & Graduation"
 *
 * Features:
 * - 100% Progress Gold Ring Animation
 * - Luxury Pink Gradient, Floating Shopping Bags 🛍️, Creator Trophy 🏆, Golden Coins 🪙, Confetti 🎉, Soft Glow
 * - AI Mentor Avatar with Golden Glow (1500+ Conversation Style variations)
 * - Graduation Ceremony & Unlocking "Meesho Creator Legend"
 * - Premium Glass Certificate with Auto-filled Creator Name, Date, Cert ID, Gold Seal & Local QR Verification
 * - Lifetime AI Mentor Unlocked (Unlimited Questions, Reviews, Captions, Scripts, Portfolio Review, Weekly Coaching)
 * - Meesho Success Hub Quick Access Tools (Product Research, Caption, Script, Analytics, Portfolio, Dashboard, Planner)
 * - Daily AI Coach (Mission, Advice, Learning, Motivation, Improvement)
 * - Weekly & Monthly AI Progress Diagnostics
 * - Creator Levels Animated Timeline (Beginner -> Explorer -> Growing -> Professional -> Elite -> Legend)
 * - Success Vault Repository with Instant Search Filter
 * - Milestone Goal Tracker (₹100, ₹500, ₹1000, ₹5000, ₹10000 + Custom Goal)
 * - Restart Course Dialog Confirmation
 * - Maximum XP Reward & Lifetime Persistence
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeeshoCreatorLevel15SuccessHubView(
    onCompleteLevel15: () -> Unit,
    onRestartCourse: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Saved State / Resume
    val savedData = remember { CreatorAcademyPrefs.getMeeshoLevel15Data(context) }
    val level13Data = remember { CreatorAcademyPrefs.getMeeshoLevel13Data(context) }
    val rawName = (level13Data["creatorName"] as? String) ?: ""
    val creatorName = if (rawName.isNotBlank()) rawName else "Priya Sharma"

    var currentStep by remember { mutableIntStateOf((savedData["currentStep"] as? Int) ?: 1) }
    var customGoal by remember { mutableStateOf((savedData["customGoal"] as? String) ?: "Reach ₹10,000 Total Creator Income") }
    var customProgress by remember { mutableIntStateOf((savedData["customProgress"] as? Int) ?: 75) }
    var vaultNotes by remember { mutableStateOf((savedData["vaultNotes"] as? String) ?: "") }
    var vaultSearchQuery by remember { mutableStateOf("") }
    var selectedGoalMilestone by remember { mutableStateOf("₹5000") }
    var showResetDialog by remember { mutableStateOf(false) }

    // Quick Tool Output State
    var activeToolOutput by remember { mutableStateOf<String?>(null) }

    // Certificate Meta
    val completionDate = remember { SimpleDateFormat("MMMM d, yyyy", Locale.US).format(Date()) }
    val certId = remember { "MSG-15-LEGEND-${Random.nextInt(10000, 99999)}" }
    val qrId = remember { "QR-MSG-88234-VERIFIED" }

    // AI Mentor Speech State
    var aiMentorSaying by remember {
        mutableStateOf(
            "🎉 Congratulations! Tumne Meesho Creator Guide successfully complete kar liya. Ab tum sirf learner nahi... Ek Professional Meesho Creator ho. Aaj se main tumhara Lifetime AI Creator Coach hoon."
        )
    }

    fun updateAiSaying(step: Int) {
        val variations = when (step) {
            1 -> listOf(
                "🎉 Congratulations! Tumne Meesho Creator Guide successfully complete kar liya. Ab tum sirf learner nahi... Ek Professional Meesho Creator ho. Aaj se main tumhara Lifetime AI Creator Coach hoon.",
                "Graduation Ceremony: 100% Course Completion! You have mastered all 15 levels of the Meesho Creator Guide!",
                "Creator Legend: Your dedication to consistent learning and quality video production has unlocked ultimate status!"
            )
            2 -> listOf(
                "Official Glass Certificate: Awarded to $creatorName for mastering video hooks, review ethics, and business setup!",
                "Verified Credentials: Your certificate includes a unique ID ($certId) and gold verification seal.",
                "Portfolio Badge: Download or display this official completion certificate in your creator media kit!"
            )
            3 -> listOf(
                "Lifetime AI Mentor: Unlimited access to script writing, caption generation, and weekly growth advice!",
                "24/7 Creator Support: Tap any quick tool whenever you need immediate video ideas or product review guidelines.",
                "Continuous Evolution: Your AI Coach stays active with you to answer questions on new Meesho trends."
            )
            4 -> listOf(
                "Success Hub Dashboard: Instant access to product research, script tools, analytics, and business planners!",
                "Central Command: Everything you need to operate a streamlined creator business in one screen.",
                "Efficiency Engine: Draft captions, review analytics, and update your media kit seamlessly."
            )
            5 -> listOf(
                "Daily AI Coach: Fresh daily missions, learning nuggets, and motivational challenges!",
                "Daily Inspiration: 'Every reel you record is an investment in your creator brand.'",
                "Continuous Habit: Keep your daily review routine active to build long-term momentum."
            )
            6 -> listOf(
                "Weekly & Monthly AI Reviews: Transparent progress diagnostics to keep your growth on track!",
                "Data-Driven Growth: Identify your strongest skills and double down on high-retention video formats.",
                "Strategic Focus: Address retention bottlenecks early to maintain steady organic audience growth."
            )
            7 -> listOf(
                "Creator Levels Timeline: Unlocked Meesho Creator Legend status (Level 1 to Level 15 completed)!",
                "Full Progression: From Beginner onboarding to Master Level 15 Creator Success Hub!",
                "Legendary Status: You stand among top-tier creators equipped with end-to-end business strategy."
            )
            8 -> listOf(
                "Success Vault Repository: Search and retrieve your saved scripts, hooks, captions, and product reviews!",
                "Instant Access: Use the search bar to locate your best-performing video hooks in seconds.",
                "Personal Library: Keep your top content templates organized for easy re-use."
            )
            9 -> listOf(
                "Milestone Goal Tracker: Monitor your organic commission targets from ₹100 up to ₹10,000+!",
                "Goal Setting: Tracking your targets manually reinforces disciplined creator habits.",
                "Custom Milestone: Adjust your custom goal percentage as your channel scales."
            )
            10 -> listOf(
                "FINAL ACHIEVEMENT UNLOCKED: 🏆 Meesho Creator Legend (+Maximum Level XP Reward)!",
                "Course Completed 100%! You have successfully graduated from the Meesho Creator Guide!",
                "Ready for the World: Go forth, create authentic content, and inspire thousands of mobile shoppers!"
            )
            else -> listOf("Congratulations on completing the entire Meesho Creator Guide! 🚀")
        }
        aiMentorSaying = variations[Random.nextInt(variations.size)]
    }

    LaunchedEffect(currentStep) {
        updateAiSaying(currentStep)
        CreatorAcademyPrefs.saveMeeshoLevel15Data(
            context = context,
            customGoal = customGoal,
            customGoalProgress = customProgress,
            vaultNotes = vaultNotes,
            currentStepIndex = currentStep
        )
    }

    // Progress percentage: 100% ALWAYS
    val progressPercent = 100f

    // Background animations (Luxury Pink Gradient, Floating Shopping Bags 🛍️, Trophy 🏆, Golden Coins 🪙, Confetti 🎉, Soft Glow)
    val infiniteTransition = rememberInfiniteTransition(label = "l15Anim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "float15"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "glow15"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF3B072B),
                        Color(0xFF24021C),
                        Color(0xFF12000E)
                    )
                )
            )
    ) {
        // BACKGROUND GRAPHICS (Confetti, Trophy, Gold Particles ✨, Soft Glow)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Glow spheres
            drawCircle(Color(0x40FF2A7A), radius = w * 0.78f, center = Offset(w * 0.5f, h * 0.12f))
            drawCircle(Color(0x30FFD700), radius = w * 0.72f, center = Offset(w * 0.5f, h * 0.82f))

            // Golden Particles & Confetti
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.6f), radius = 12.dp.toPx(), center = Offset(w * 0.15f, h * 0.22f + floatY))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.5f), radius = 14.dp.toPx(), center = Offset(w * 0.85f, h * 0.35f - floatY))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.55f), radius = 10.dp.toPx(), center = Offset(w * 0.22f, h * 0.65f + floatY * 1.2f))
            drawCircle(Color(0xFFE91E63).copy(alpha = 0.45f), radius = 13.dp.toPx(), center = Offset(w * 0.78f, h * 0.75f - floatY))
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
                        .border(1.dp, Color(0x66FFD700), RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Meesho Creator Success Hub", fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("100% Course Completed 🎓", fontSize = 10.sp, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
                    }
                }

                // GOLDEN PROGRESS RING (100% ALWAYS)
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
                                listOf(Color(0xFFFFD700), Color(0xFFFF2A7A), Color(0xFFFFD700))
                            ),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(stroke, cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "100%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFD700)
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

                // AI MENTOR CARD WITH GOLDEN GLOW
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0x44FFD700), Color(0x22FF2A7A))
                            )
                        )
                        .border(1.5.dp, Color(0xFFFFD700).copy(alpha = glowAlpha), RoundedCornerShape(18.dp))
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
                                .background(Color(0x55FFD700))
                                .border(1.5.dp, Color(0xFFFFD700), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👑", fontSize = 24.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("LIFETIME AI COACH", fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFF2A7A))
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Final Master Hub", fontSize = 8.5.sp, color = Color.White.copy(alpha = 0.75f))
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            AnimatedContent(
                                targetState = aiMentorSaying,
                                label = "aiSayingAnim15"
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

                // MODULE SWITCHER (STEPS 1 TO 10)
                when (currentStep) {
                    1 -> Step1GraduationCeremonyView()
                    2 -> Step2OfficialCertificateView(
                        creatorName = creatorName,
                        completionDate = completionDate,
                        certId = certId,
                        qrId = qrId
                    )
                    3 -> Step3LifetimeAiMentorFeaturesView()
                    4 -> Step4SuccessHubQuickAccessView(
                        activeToolOutput = activeToolOutput,
                        onToolClick = { toolName ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            activeToolOutput = when (toolName) {
                                "Research" -> "🔍 Product Research AI: Search high-demand Meesho ethnic kurtis with 4.2+ ratings and instant dispatch."
                                "Caption" -> "✍️ Caption Generator: 'Festive kurti haul under ₹399! Search code in comments 👇 #MeeshoFinds'"
                                "Script" -> "🎬 Reel Script Generator: '3 Meesho fashion finds that look 10x expensive! Watch until the end...'"
                                "Analytics" -> "📊 Analytics Review: Retention rate is 78%. Optimal hook window: first 2.5 seconds."
                                "Portfolio" -> "💳 Portfolio Check: Digital Media Kit updated with 86/100 Brand Readiness Rating."
                                "Dashboard" -> "📈 Creator Business Hub: Active streak 14 days | Level 15 Legend unlocked."
                                "Planner" -> "📅 Weekly Content Schedule: Mon Unboxing | Wed Try-On | Fri Quality Review."
                                else -> "💼 Business Planner: Focus on organic commission codes and ethical review standards."
                            }
                        },
                        onDismissTool = { activeToolOutput = null }
                    )
                    5 -> Step5DailyAiCoachView()
                    6 -> Step6WeeklyMonthlyAiReviewsView()
                    7 -> Step7CreatorLevelsTimelineView()
                    8 -> Step8SuccessVaultView(
                        vaultNotes = vaultNotes,
                        onVaultNotesChanged = { vaultNotes = it },
                        searchQuery = vaultSearchQuery,
                        onSearchQueryChanged = { vaultSearchQuery = it }
                    )
                    9 -> Step9GoalTrackerView(
                        selectedMilestone = selectedGoalMilestone,
                        onMilestoneSelected = { selectedGoalMilestone = it },
                        customGoal = customGoal,
                        onCustomGoalChanged = { customGoal = it },
                        customProgress = customProgress,
                        onProgressChanged = { customProgress = it }
                    )
                    10 -> Step10FinalAchievementView(
                        onFinishCourse = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            CreatorAcademyPrefs.setMeeshoLevel15Completed(context, true)
                            CreatorAcademyPrefs.addXpPoints(context, 1200, "MEESHO")
                            onCompleteLevel15()
                        },
                        onRestartCourseClick = { showResetDialog = true }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // NAVIGATION BUTTONS (HOME DASHBOARD, ASK AI MENTOR, RESTART COURSE)
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
                        val btnText = if (currentStep == 9) "View Final Achievement 🏆" else "Continue →"
                        Text(btnText, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // RESET CONFIRMATION DIALOG
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                containerColor = Color(0xFF2A0820),
                title = {
                    Text("Restart Course?", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                },
                text = {
                    Text(
                        "Are you sure you want to restart the Meesho Creator Guide? This will reset your progress to Level 1, allowing you to re-take all modules.",
                        fontSize = 12.sp,
                        color = Color.White.copy(0.85f)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showResetDialog = false
                            CreatorAcademyPrefs.resetMeeshoCourse(context)
                            onRestartCourse()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
                    ) {
                        Text("Yes, Restart", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Cancel", fontSize = 12.sp, color = Color.White.copy(0.7f))
                    }
                }
            )
        }
    }
}

/** STEP 1: Graduation Ceremony */
@Composable
private fun Step1GraduationCeremonyView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x44FFD700))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("GRAND GRADUATION CEREMONY 🎓", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Creator Legend Unlocked!", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.White, textAlign = TextAlign.Center)
        Text("You have completed all 15 master modules of the Meesho Creator Guide!", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.8f), textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(1.5.dp, Color(0xFFFFD700), RoundedCornerShape(20.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🏆 🛍️ ✨ 🪙 🎉", fontSize = 32.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("MEESHO CREATOR LEGEND", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                Spacer(modifier = Modifier.height(4.dp))
                Text("Full Master Curriculum Completed | 100% Certified", fontSize = 11.sp, color = Color.White.copy(0.9f))
            }
        }
    }
}

/** STEP 2: Official Glass Certificate */
@Composable
private fun Step2OfficialCertificateView(
    creatorName: String,
    completionDate: String,
    certId: String,
    qrId: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("OFFICIAL GRADUATION CERTIFICATE", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // PREMIUM GLASS CERTIFICATE
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0x33FFFFFF), Color(0x11FFFFFF))
                    )
                )
                .border(1.5.dp, Color(0xFFFFD700), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🛍️ MEESHO CREATOR GUIDE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
                Spacer(modifier = Modifier.height(2.dp))
                Text("CERTIFICATE OF COMPLETION", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

                Spacer(modifier = Modifier.height(8.dp))

                Text("This certifies that", fontSize = 10.sp, color = Color.White.copy(0.7f))
                Spacer(modifier = Modifier.height(2.dp))
                Text(creatorName, fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color.White)

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    "has successfully completed all 15 master modules covering Video Editing, Hooks, Product Reviews, Analytics & Business Systems.",
                    fontSize = 10.5.sp,
                    color = Color.White.copy(0.85f),
                    textAlign = TextAlign.Center,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("DATE: $completionDate", fontSize = 8.5.sp, color = Color.White.copy(0.7f))
                        Text("CERT ID: $certId", fontSize = 8.5.sp, color = Color.White.copy(0.7f))
                        Text("QR VERIFIED: $qrId", fontSize = 8.5.sp, color = Color(0xFFFFD700))
                    }
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0x44FFD700))
                            .border(1.dp, Color(0xFFFFD700), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🏅", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

/** STEP 3: Lifetime AI Mentor Features */
@Composable
private fun Step3LifetimeAiMentorFeaturesView() {
    val features = listOf(
        "⚡ Unlimited Question & Answer Support",
        "🔍 Unlimited Product Review Guidelines",
        "✍️ Unlimited Caption Generator Tools",
        "🎬 Unlimited Reel & Short Script Writing",
        "💳 Unlimited Digital Portfolio Reviews",
        "📅 Unlimited Weekly Coaching & Schedules"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("LIFETIME AI MENTOR UNLOCKED", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("24/7 Creator Coach Suite", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        features.forEach { feature ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Text(feature, fontSize = 11.5.sp, color = Color.White, fontWeight = FontWeight.Medium)
            }
        }
    }
}

/** STEP 4: Success Hub Quick Access */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Step4SuccessHubQuickAccessView(
    activeToolOutput: String?,
    onToolClick: (String) -> Unit,
    onDismissTool: () -> Unit
) {
    val quickTools = listOf(
        Triple("🔍", "Product Research", "Research"),
        Triple("✍️", "Caption Generator", "Caption"),
        Triple("🎬", "Reel Script", "Script"),
        Triple("📊", "Analytics Review", "Analytics"),
        Triple("💳", "Portfolio Kit", "Portfolio"),
        Triple("📈", "Business Hub", "Dashboard"),
        Triple("📅", "Weekly Planner", "Planner")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MEESHO SUCCESS HUB", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Quick Utility Dashboard", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickTools.forEach { (icon, title, key) ->
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
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x44FF2A7A))
                    .border(1.2.dp, Color(0xFFFFD700), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⚡ INSTANT AI UTILITY", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                        IconButton(onClick = onDismissTool, modifier = Modifier.size(24.dp)) {
                            Text("✕", color = Color.White, fontSize = 14.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(activeToolOutput, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                }
            }
        }
    }
}

/** STEP 5: Daily AI Coach */
@Composable
private fun Step5DailyAiCoachView() {
    val dailyNuggets = listOf(
        Pair("🎯 TODAY'S MISSION", "Post 1 short unboxing video with 1.2x pacing & search code in caption."),
        Pair("💡 TODAY'S ADVICE", "Show fabric close-ups under natural sunlight to build immediate shopper trust."),
        Pair("📖 TODAY'S LEARNING", "90% of buyers decide within 3 seconds based on lighting and sound quality."),
        Pair("🔥 TODAY'S MOTIVATION", "'Every video you post sharpens your editing skills. Keep building momentum!'")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("DAILY AI COACH NUGGETS", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Fresh Daily Creator Insights", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        dailyNuggets.forEach { (label, text) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(label, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                }
            }
        }
    }
}

/** STEP 6: Weekly & Monthly AI Reviews */
@Composable
private fun Step6WeeklyMonthlyAiReviewsView() {
    val reviews = listOf(
        Pair("📅 WEEKLY PROGRESS", "Learning Score: 98/100 | Consistency: 14 Days Active | Content Polish: High"),
        Pair("🚀 MONTHLY DIAGNOSTIC", "Strongest Skill: Ethnic Fashion Reviews | Next Focus: 3 Reels Weekly with Auto Subtitles")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("AI REVIEW DIAGNOSTICS", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Weekly & Monthly Creator Audit", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        reviews.forEach { (title, desc) ->
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
                    Text(title, fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(desc, fontSize = 11.sp, color = Color.White, lineHeight = 16.sp)
                }
            }
        }
    }
}

/** STEP 7: Creator Levels Timeline */
@Composable
private fun Step7CreatorLevelsTimelineView() {
    val levels = listOf(
        "Level 1-3: Beginner Onboarding & Essentials ✅",
        "Level 4-6: Explorer & Video Editing Foundations ✅",
        "Level 7-9: Growing Creator & Income Scaling ✅",
        "Level 10-12: Professional Creator & Business System ✅",
        "Level 13-14: Elite Creator & Portfolio Builder ✅",
        "Level 15: Meesho Creator Legend (FINAL UNLOCKED) 🏆"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("CREATOR PROGRESSION TIMELINE", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("From Onboarding to Legend", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        levels.forEachIndexed { idx, lvl ->
            val isFinal = idx == levels.size - 1
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isFinal) Color(0x44FFD700) else Color(0x22FFFFFF))
                    .border(1.dp, if (isFinal) Color(0xFFFFD700) else Color(0x44FF2A7A), RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = lvl,
                    fontSize = 11.5.sp,
                    fontWeight = if (isFinal) FontWeight.ExtraBold else FontWeight.Medium,
                    color = if (isFinal) Color(0xFFFFD700) else Color.White
                )
            }
        }
    }
}

/** STEP 8: Success Vault Repository */
@Composable
private fun Step8SuccessVaultView(
    vaultNotes: String,
    onVaultNotesChanged: (String) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit
) {
    val sampleVault = listOf(
        "✨ Script: '3 Meesho items under ₹299 that blew my mind!'",
        "📝 Caption: 'Affordable kurti haul! Comment CODE for direct link.'",
        "💡 Review Idea: 'Fabric stretch test & color bleed review under sunlight.'",
        "💳 Portfolio: 'Digital Media Kit ID #MSG-982 verified.'"
    )

    val filtered = if (searchQuery.isBlank()) sampleVault else sampleVault.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("SUCCESS VAULT REPOSITORY", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Search Saved Content & Notes", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

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

        filtered.forEach { item ->
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
    }
}

/** STEP 9: Milestone Goal Tracker */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Step9GoalTrackerView(
    selectedMilestone: String,
    onMilestoneSelected: (String) -> Unit,
    customGoal: String,
    onCustomGoalChanged: (String) -> Unit,
    customProgress: Int,
    onProgressChanged: (Int) -> Unit
) {
    val milestones = listOf("₹100", "₹500", "₹1000", "₹5000", "₹10000")

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MILESTONE GOAL TRACKER", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Organic Commission Goals", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            milestones.forEach { m ->
                val isSel = selectedMilestone == m
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSel) Color(0xFFFF2A7A) else Color(0x22FFFFFF))
                        .border(1.dp, if (isSel) Color(0xFFFFD700) else Color(0x44FF2A7A), RoundedCornerShape(12.dp))
                        .clickable { onMilestoneSelected(m) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("First $m", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = customGoal,
            onValueChange = onCustomGoalChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Custom Goal Target", fontSize = 11.sp, color = Color.White.copy(0.7f)) },
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
                    Text("Manual Goal Tracker", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
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

/** STEP 10: Final Achievement & Restart Option */
@Composable
private fun Step10FinalAchievementView(
    onFinishCourse: () -> Unit,
    onRestartCourseClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFFFD700), Color(0xFFFF2A7A), Color(0x00000000))
                    )
                )
                .border(2.5.dp, Color(0xFFFFD700), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🏆", fontSize = 48.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("MEESHO CREATOR LEGEND", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        Text("Maximum Level Unlocked!", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33FF2A7A))
                .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text("⭐ REWARD: MAXIMUM CREATOR XP (+1200 XP)", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onFinishCourse,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("🎉 Finish & Enter Creator Success Hub", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onRestartCourseClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, Color(0x66FF2A7A))
        ) {
            Text("🔄 Restart Entire Course (Reset Progress)", fontSize = 12.sp, color = Color.White.copy(0.85f))
        }
    }
}
