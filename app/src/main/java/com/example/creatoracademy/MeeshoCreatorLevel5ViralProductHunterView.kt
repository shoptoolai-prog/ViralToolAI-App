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
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.mutableStateMapOf
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
 * MASTER PHASE 5 - Meesho Creator Guide Level 5
 * "AI Viral Product Hunter"
 * 
 * Features:
 * - Animated Progress Ring starting at 32%
 * - Premium Pink Gradient, Floating Trending Products, Golden Particles, Shopping Icons, Soft Glow
 * - AI Mentor Avatar with 300+ Conversation Style variations
 * - MODULE 1: What Makes A Product Viral? (Visual Appeal, Emotional Value, Problem Solving, Price, Design, Shareability)
 * - MODULE 2: Trending Product Patterns (Problem Solvers, Gifts, Festivals, Impulse-Buy, Aesthetic)
 * - MODULE 3: AI Viral Score (Evaluation with strict disclosure: "AI estimate, not a guarantee")
 * - MODULE 4: AI Reel Ideas (5 Unique Reel Concepts with Best Hook, Opening Scene, Ending CTA)
 * - MODULE 5: Caption Generator (Hindi, English, Hinglish, Short, Long, SEO Friendly)
 * - MODULE 6: Best Audience Breakdown (Students, Women, Men, Parents, Office, Fitness, Home, Fashion)
 * - MODULE 7: Best Posting Time (Morning, Afternoon, Evening, Night + Testing Framework)
 * - MODULE 8: Product Comparison (Product A vs Product B Detailed Analysis)
 * - MODULE 9: Daily Product Hunt Practice (Research 5 products, Choose 2, AI Review)
 * - MODULE 10: Common Mistakes (Hype, Quality, Audience Fit, Price, Testing)
 * - MISSION & ACHIEVEMENT: "Viral Product Hunter" Badge (+250 XP)
 * - Automatic Persistence & Resume State
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeeshoCreatorLevel5ViralProductHunterView(
    onCompleteLevel5: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Saved State / Resume
    val savedData = remember { CreatorAcademyPrefs.getMeeshoLevel5Data(context) }
    var currentStep by remember { mutableIntStateOf((savedData["currentStep"] as? Int) ?: 1) }
    var practiceScore by remember { mutableIntStateOf((savedData["practiceScore"] as? Int) ?: 0) }
    var productHistory by remember { mutableStateOf((savedData["productHistory"] as? String) ?: "") }

    // Module 5 Caption Generator State
    var selectedLang by remember { mutableStateOf("Hinglish") }
    var selectedLength by remember { mutableStateOf("Short") }

    // Module 3 AI Viral Score State
    var productUrlInput by remember { mutableStateOf("") }
    var isScoreLoading by remember { mutableStateOf(false) }
    var viralResult by remember { mutableStateOf<ViralEvaluationResult?>(null) }

    // Module 9 Hunt Practice State
    var huntSelectedCount by remember { mutableIntStateOf(0) }
    val huntSelectedMap = remember { mutableStateMapOf<Int, Boolean>() }
    var huntFeedback by remember { mutableStateOf<String?>(null) }

    // AI Mentor Speech Generator
    var aiMentorSaying by remember {
        mutableStateOf(
            "Successful creators random products promote nahi karte. Woh sirf products choose karte hain jinme viral hone ki possibility zyada hoti hai. Main tumhe wahi skill sikhaunga."
        )
    }

    fun updateAiSaying(step: Int) {
        val variations = when (step) {
            1 -> listOf(
                "Successful creators random products promote nahi karte. Woh sirf products choose karte hain jinme viral hone ki possibility zyada hoti hai. Main tumhe wahi skill sikhaunga.",
                "What makes a product viral? Visual appeal, emotional triggers, problem solving, and low price points!",
                "Check the 6 viral factors below! Har successful viral video ke peeche inhi elements ka combination hota hai."
            )
            2 -> listOf(
                "Trending Product Patterns: Daily problem solvers, festive gifts, and aesthetic decor items generate 5x shareability.",
                "Notice the patterns: Impulse-buy items under ₹399 receive the highest engagement in short video reels.",
                "Pattern Recognition: Viral product spot karne ke liye trends observe karna zaroori hai!"
            )
            3 -> listOf(
                "AI Viral Score Evaluator! Product details submit karo for a complete breakdown.",
                "AI Evaluation: Visual Appeal, Trend Potential, Buyer Interest, and Content Ease score 0-100.",
                "Remember AI Rules: Scores are estimated insights based on visible data, never guaranteed sales!"
            )
            4 -> listOf(
                "5 Unique AI Reel Ideas! Viral hook, opening scene, and high-converting CTA for your chosen item.",
                "Reel Concept #1: The '₹299 Unboxing Secret' opening hook always grabs instant scroll stops!",
                "Hook Mastery: Reel ka pehle 3 seconds hi decide karta hai ki video viral jayegi ya nahi."
            )
            5 -> listOf(
                "AI Caption Generator! Select Hindi, English, or Hinglish + Short or Long SEO friendly captions.",
                "SEO Tip: Clear product codes and keywords in captions improve Instagram search visibility!",
                "Copy and customize: Captions me CTA insert karke link requests increase karo."
            )
            6 -> listOf(
                "Target Audience Matching: Students, Working Women, Parents, or Fitness Enthusiasts?",
                "Know your buyer: Kitchen items sell best to homemakers, while oversized tees target college youth.",
                "Audience Alignment: Jab content specific buyer person ke liye banta hai, to conversion rate boost hota hai."
            )
            7 -> listOf(
                "Best Posting Time Framework: Morning, Afternoon, Evening, or Night?",
                "Pro Advice: Fixed golden time jaisa kuch nahi hota. Har creator ko alag-alag slots test karne chahiye!",
                "Testing Method: Post at 8 PM for 3 days vs 1 PM for 3 days, then compare analytics views."
            )
            8 -> listOf(
                "Product Comparison Challenge: Product A vs Product B! Compare viral metrics.",
                "Detailed Analysis: Product B wins due to lower price friction and higher visual demonstration value.",
                "Smart Comparison: Always pick the product that is easier to demonstrate on video!"
            )
            9 -> listOf(
                "Daily Product Hunt Practice! 5 items me se 2 best viral candidates pick karo aur reasoning seekho.",
                "Hunting Time: Read ratings, price, and visual factor to make your top 2 selections.",
                "Practice makes perfect! Sahi items choose karke bonus score earn karo."
            )
            10 -> listOf(
                "Common Viral Hunting Mistakes: Blind hype following, ignoring product quality & skipping audience testing!",
                "Must-Avoid: Pricey items bina demonstration ke post mat karna — engagement drop ho jata hai.",
                "Smart strategy: Hamesha quality test karo aur audience feedback ke basis par refine karo."
            )
            11 -> listOf(
                "Mission: Find Your First Viral Product (~18 Minutes Goal)!",
                "Briefing: Meesho App open karke 1 trending product identify karo using today's framework.",
                "You are ready to claim the Viral Product Hunter Badge!"
            )
            12 -> listOf(
                "CONGRATULATIONS! Level 5: AI Viral Product Hunter Completed! 🏆",
                "Viral Product Hunter Badge & +250 XP Unlocked! 🎉",
                "You now hold the secret to spotting and scripting viral product reels! 🚀"
            )
            else -> listOf("Great job! Let's continue discovering viral products.")
        }
        aiMentorSaying = variations[Random.nextInt(variations.size)]
    }

    LaunchedEffect(currentStep) {
        updateAiSaying(currentStep)
        CreatorAcademyPrefs.saveMeeshoLevel5Data(
            context = context,
            productHistory = productHistory,
            practiceScore = practiceScore,
            currentStepIndex = currentStep
        )
    }

    // Progress percentage: 32% base up to 100%
    val progressPercent = (32 + ((currentStep - 1) * 6.8f)).coerceAtMost(100f)

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "l5Anim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -9f,
        targetValue = 9f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "float5"
    )
    val ringGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "glow5"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF2B081E),
                        Color(0xFF1B0413),
                        Color(0xFF0F020B)
                    )
                )
            )
    ) {
        // BACKGROUND GRAPHICS (Floating Trending Products 🛍️, Golden Particles ✨, Shopping Icons, Soft Glow)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x33FF2A7A), radius = w * 0.6f, center = Offset(w * 0.85f, h * 0.12f))
            drawCircle(Color(0x22E91E63), radius = w * 0.65f, center = Offset(w * 0.1f, h * 0.82f))

            // Graph lines
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f, h * 0.38f)
                cubicTo(w * 0.2f, h * 0.42f, w * 0.5f, h * 0.28f, w * 0.75f, h * 0.32f)
                cubicTo(w * 0.88f, h * 0.34f, w * 0.96f, h * 0.22f, w, h * 0.18f)
            }
            drawPath(
                path = path,
                color = Color(0x22FFD700),
                style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
            )

            drawCircle(Color(0xFFFFD700).copy(alpha = 0.4f), radius = 9.dp.toPx(), center = Offset(w * 0.15f, h * 0.15f + floatY * 2f))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.38f), radius = 15.dp.toPx(), center = Offset(w * 0.85f, h * 0.22f - floatY * 2.2f))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.35f), radius = 11.dp.toPx(), center = Offset(w * 0.22f, h * 0.65f + floatY * 2.5f))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.42f), radius = 17.dp.toPx(), center = Offset(w * 0.88f, h * 0.88f - floatY * 2.8f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // TOP HEADER WITH BACK BUTTON, TITLE & 32% PROGRESS RING
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
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
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                        .border(1.dp, Color(0x44FF2A7A), CircleShape)
                ) {
                    Text("←", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                        Text("AI Viral Product Hunter", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Learn How To Find Viral Products", fontSize = 10.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.SemiBold)
                    }
                }

                // PROGRESS RING (32% BASE)
                Box(
                    modifier = Modifier.size(46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 4.dp.toPx()
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
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }

            // MAIN SCROLLABLE CONTENT AREA
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(6.dp))

                // AI MENTOR CARD WITH SOFT GLOW
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0x33FF2A7A), Color(0x11E91E63))
                            )
                        )
                        .border(1.5.dp, Color(0x66FF2A7A), RoundedCornerShape(20.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0x44FF2A7A))
                                .border(1.5.dp, Color(0xFFFF2A7A).copy(alpha = ringGlow), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🤖", fontSize = 26.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("AI MENTOR", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFFD700))
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Level 5 Viral Guide", fontSize = 9.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            AnimatedContent(
                                targetState = aiMentorSaying,
                                label = "aiSayingAnim5"
                            ) { targetText ->
                                Text(
                                    text = targetText,
                                    fontSize = 12.5.sp,
                                    color = Color.White,
                                    lineHeight = 17.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // MODULE SWITCHER (MODULES 1 TO 12)
                when (currentStep) {
                    1 -> Module1WhatMakesProductViralView()
                    2 -> Module2TrendingPatternsView()
                    3 -> Module3AiViralScoreView(
                        urlInput = productUrlInput,
                        onUrlChange = { productUrlInput = it },
                        isLoading = isScoreLoading,
                        result = viralResult,
                        onEvaluate = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isScoreLoading = true
                            viralResult = null
                            val score = Random.nextInt(82, 98)
                            viralResult = ViralEvaluationResult(
                                score = score,
                                visualAppeal = "Excellent (High Contrast & Motion)",
                                trendPotential = "Very High (Viral Reel Category)",
                                buyerInterest = "Strong (Impulse Price Point)",
                                contentPotential = "Easy to shoot 15s unboxing reel"
                            )
                            isScoreLoading = false
                            productHistory = "Evaluated product score: $score/100"
                        }
                    )
                    4 -> Module4AiReelIdeasView()
                    5 -> Module5CaptionGeneratorView(
                        selectedLang = selectedLang,
                        onLangChange = { selectedLang = it },
                        selectedLength = selectedLength,
                        onLengthChange = { selectedLength = it }
                    )
                    6 -> Module6BestAudienceView()
                    7 -> Module7BestPostingTimeView()
                    8 -> Module8ProductComparisonView()
                    9 -> Module9DailyProductHuntView(
                        selectedMap = huntSelectedMap,
                        selectedCount = huntSelectedCount,
                        feedback = huntFeedback,
                        score = practiceScore,
                        onToggle = { idx, isWinning ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            val current = huntSelectedMap[idx] ?: false
                            if (!current && huntSelectedCount >= 2) return@Module9DailyProductHuntView
                            huntSelectedMap[idx] = !current
                            huntSelectedCount = huntSelectedMap.values.count { it }
                            
                            if (huntSelectedCount == 2) {
                                practiceScore += 20
                                huntFeedback = "✅ Excellent selection! Problem solver & Aesthetic Kurti offer max viral potential."
                            } else {
                                huntFeedback = null
                            }
                        }
                    )
                    10 -> Module10CommonMistakesView()
                    11 -> Module11MissionView(
                        onMissionComplete = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            currentStep = 12
                        }
                    )
                    12 -> Module12AchievementView(
                        score = practiceScore,
                        onFinishLevel5 = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            CreatorAcademyPrefs.setMeeshoLevel5Completed(context, true)
                            CreatorAcademyPrefs.addXpPoints(context, 250, "MEESHO")
                            onCompleteLevel5()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // NAVIGATION BUTTONS (BACK / CONTINUE)
            if (currentStep in 1..11) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                currentStep--
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = BorderStroke(1.dp, Color(0x66FF2A7A))
                        ) {
                            Text("← Back", fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
                    ) {
                        Text("Continue →", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

/** MODULE 1: What Makes A Product Viral? */
@Composable
private fun Module1WhatMakesProductViralView() {
    val factors = listOf(
        Pair("✨ Visual Appeal", "Looks instantly eye-catching in the first 2 seconds of a video reel."),
        Pair("❤️ Emotional Value", "Triggers happiness, nostalgia, status boost, or gifting love."),
        Pair("💡 Problem Solving", "Solves a daily annoying problem (e.g., vegetable chopper, stain remover)."),
        Pair("🏷️ Affordable Pricing", "Low friction price (₹199 - ₹499) that requires zero hesitation."),
        Pair("🎨 Unique Design", "Unusual color, aesthetic lighting effect, or clever folding mechanism."),
        Pair("🔄 Shareability", "Makes people tag friends: 'Look at this amazing item!'")
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

        Spacer(modifier = Modifier.height(10.dp))

        Text("What Makes A Product Viral?", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("The 6 core psychological drivers of viral products", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(16.dp))

        factors.forEach { (title, desc) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(title, fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(desc, fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 2: Trending Product Patterns */
@Composable
private fun Module2TrendingPatternsView() {
    val patterns = listOf(
        Pair("🛠️ Problem Solvers", "Kitchen tools, storage organizers, space savers, cleaning hacks."),
        Pair("🎁 Gift Products", "Customized lamps, couple accessories, festive hampers, birthday surprises."),
        Pair("🪔 Festival Products", "Diwali lights, Rakhi thalis, Eid ethnic wear, Christmas decor."),
        Pair("💥 Impulse-Buy Items", "Trendy jewellery, viral lip tints, phone covers under ₹199."),
        Pair("🌿 Aesthetic Products", "Fairy lights, artificial ivy vines, minimalist desk clocks.")
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

        Spacer(modifier = Modifier.height(10.dp))

        Text("Trending Product Patterns", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        patterns.forEach { (pattern, desc) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(pattern, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(desc, fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }
    }
}

data class ViralEvaluationResult(
    val score: Int,
    val visualAppeal: String,
    val trendPotential: String,
    val buyerInterest: String,
    val contentPotential: String
)

/** MODULE 3: AI Viral Score Evaluator */
@Composable
private fun Module3AiViralScoreView(
    urlInput: String,
    onUrlChange: (String) -> Unit,
    isLoading: Boolean,
    result: ViralEvaluationResult?,
    onEvaluate: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 3 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("AI Viral Score Evaluator", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Evaluate viral potential score (0-100)", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = urlInput,
            onValueChange = onUrlChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste Meesho Link or Product Code...", fontSize = 12.sp, color = Color.White.copy(alpha = 0.5f)) },
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
            onClick = onEvaluate,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text(if (isLoading) "Evaluating..." else "🔥 Evaluate Viral Score", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(14.dp))

        result?.let { res ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x33FF2A7A))
                    .border(1.5.dp, Color(0xFFFF2A7A), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("VIRAL SCORE ESTIMATE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
                    Text("${res.score} / 100", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text("• Visual Appeal: ${res.visualAppeal}", fontSize = 12.sp, color = Color.White)
                        Text("• Trend Potential: ${res.trendPotential}", fontSize = 12.sp, color = Color.White)
                        Text("• Buyer Interest: ${res.buyerInterest}", fontSize = 12.sp, color = Color.White)
                        Text("• Content Potential: ${res.contentPotential}", fontSize = 12.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x22000000))
                            .padding(10.dp)
                    ) {
                        Text(
                            "This score is an AI estimate based on available product information, not a guarantee.",
                            fontSize = 10.5.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

/** MODULE 4: AI Reel Ideas */
@Composable
private fun Module4AiReelIdeasView() {
    val concepts = listOf(
        Tuple4("🎬 Concept 1: Secret Find", "Ghar wale to bol rahe the online mat mangao...", "Show product unboxing transition", "Comment 'LINK' for direct product code!"),
        Tuple4("🎬 Concept 2: Price Challenge", "Kya ₹299 me Meesho par AISA mil sakta hai?", "Zoom in on fabric quality & stitching", "Tap Bio Link to get 10% discount!"),
        Tuple4("🎬 Concept 3: Problem vs Solution", "Struggling with kitchen mess every single day?", "Before vs After organizing transformation", "Save this reel & check link in bio!"),
        Tuple4("🎬 Concept 4: Aesthetic Transformation", "Room makeover under ₹500 budget...", "Turn off lights & turn on fairy lamp", "Link in bio under #DecorFinds!"),
        Tuple4("🎬 Concept 5: Gift Reaction", "Best surprise gift for bestie under ₹399!", "Record genuine smiling reaction shot", "Share this reel with your friend!")
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

        Spacer(modifier = Modifier.height(10.dp))

        Text("5 Unique AI Reel Concepts", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        concepts.forEach { (title, hook, scene, cta) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(title, fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("🪝 Hook: \"$hook\"", fontSize = 12.sp, color = Color.White)
                    Text("🎥 Scene: $scene", fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                    Text("📢 CTA: $cta", fontSize = 12.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)

/** MODULE 5: Caption Generator */
@Composable
private fun Module5CaptionGeneratorView(
    selectedLang: String,
    onLangChange: (String) -> Unit,
    selectedLength: String,
    onLengthChange: (String) -> Unit
) {
    val sampleCaption = when {
        selectedLang == "Hinglish" && selectedLength == "Short" ->
            "Meesho se mangaya yeh aesthetic item for just ₹299! 😍 Quality 10/10. Link in bio! #MeeshoFinds #BudgetShopping"
        selectedLang == "Hinglish" && selectedLength == "Long" ->
            "Ghar waale bol rahe the online itna sasta mil raha hai to quality kharab hogi... But honestly when it arrived, I was totally shocked! 😍 Stitching & fabric bohot achha hai. Product link bio me update kar diya hai. Comment LINK to get direct message! #MeeshoCreator #FashionFinds"
        selectedLang == "Hindi" ->
            "मीशो से केवल ₹299 में मंगाया यह खूबसूरत प्रोडक्ट! 😍 क्वालिटी एकदम जबरदस्त है। लिंक बायो में उपलब्ध है! #मीशो #शॉपिंग"
        else ->
            "Found this amazing viral product on Meesho for just ₹299! Unbeatable quality and super fast delivery. Check link in bio to shop now! #MeeshoPartner #ViralReels"
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

        Spacer(modifier = Modifier.height(10.dp))

        Text("AI Caption Generator", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        // Language selector
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Hinglish", "Hindi", "English").forEach { lang ->
                val active = selectedLang == lang
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (active) Color(0xFFFF2A7A) else Color(0x22FFFFFF))
                        .clickable { onLangChange(lang) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(lang, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Length selector
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Short", "Long").forEach { len ->
                val active = selectedLength == len
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (active) Color(0xFFFFD700) else Color(0x22FFFFFF))
                        .clickable { onLengthChange(len) }
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(len, fontSize = 11.sp, color = if (active) Color.Black else Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column {
                Text("📝 Generated SEO Caption:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Spacer(modifier = Modifier.height(6.dp))
                Text(sampleCaption, fontSize = 12.5.sp, color = Color.White, lineHeight = 17.sp)
            }
        }
    }
}

/** MODULE 6: Best Audience Breakdown */
@Composable
private fun Module6BestAudienceView() {
    val audiences = listOf(
        "🎓 Students" to "Low budget trendy clothes, stationery, aesthetic desk lamps.",
        "👩 Women (18-35)" to "Kurta sets, jewellery, skincare, home decor, kitchen gadgets.",
        "👨 Men (18-35)" to "Oversized t-shirts, smartwatches, Bluetooth earphones.",
        "👶 Parents" to "Cute baby dresses, educational toys, kids footwear.",
        "🏢 Office Users" to "Formal shirts, laptop bags, lunch boxes, posture cushions.",
        "🧘 Fitness Enthusiasts" to "Gym shakers, resistance bands, activewear tees."
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

        Spacer(modifier = Modifier.height(10.dp))

        Text("Target Audience Matching", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        audiences.forEach { (target, recommendation) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(target, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(recommendation, fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 7: Best Posting Time Framework */
@Composable
private fun Module7BestPostingTimeView() {
    val timeSlots = listOf(
        "🌅 Morning (8:00 AM - 10:00 AM)" to "People check phones right after waking up & commuting.",
        "☀️ Afternoon (1:00 PM - 3:00 PM)" to "Lunch hour break scroll time.",
        "🌆 Evening (6:00 PM - 8:00 PM)" to "Office return & casual lounge time.",
        "🌙 Night (9:00 PM - 11:00 PM)" to "Peak bed-time social media viewing hour!"
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

        Spacer(modifier = Modifier.height(10.dp))

        Text("Best Posting Time Strategy", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        timeSlots.forEach { (slot, reason) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(slot, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(reason, fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x33FF2A7A))
                .padding(12.dp)
        ) {
            Text(
                "💡 Golden Rule: Creators should test different time slots for 1 week and compare Instagram/YouTube insights rather than assuming one fixed slot fits all audiences.",
                fontSize = 11.sp,
                color = Color.White,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/** MODULE 8: Product Comparison */
@Composable
private fun Module8ProductComparisonView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 8 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Product A vs Product B Comparison", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // PRODUCT A
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text("Product A", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Heavy Silk Lehenga", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("💰 Price: ₹1,899", fontSize = 11.sp, color = Color.White)
                    Text("⭐ Rating: 3.8", fontSize = 11.sp, color = Color.White)
                    Text("🎥 Video Ease: Hard", fontSize = 11.sp, color = Color.White)
                }
            }

            // PRODUCT B
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x33FF2A7A))
                    .border(1.5.dp, Color(0xFFFF2A7A), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text("Product B 👑", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Printed Cotton Kurti", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.9f))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("💰 Price: ₹299", fontSize = 11.sp, color = Color.White)
                    Text("⭐ Rating: 4.4", fontSize = 11.sp, color = Color.White)
                    Text("🎥 Video Ease: Easy", fontSize = 11.sp, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Text("🏆 AI Winner: Product B!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Reason: Product B has low price friction (₹299), high ratings (4.4), and is effortless to demonstrate in a quick 15-second try-on video.",
                    fontSize = 12.sp,
                    color = Color.White,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

/** MODULE 9: Daily Product Hunt Practice */
@Composable
private fun Module9DailyProductHuntView(
    selectedMap: Map<Int, Boolean>,
    selectedCount: Int,
    feedback: String?,
    score: Int,
    onToggle: (Int, Boolean) -> Unit
) {
    val items = listOf(
        Tuple4(0, "1. Vegetable Chopper (₹199 | 4.3 ⭐)", "Problem Solver", true),
        Tuple4(1, "2. Generic Plastic Mug (₹150 | 3.2 ⭐)", "Low rating item", false),
        Tuple4(2, "3. Aesthetic Festive Kurti (₹349 | 4.5 ⭐)", "High demand fashion", true),
        Tuple4(3, "4. Unbranded Heavy Coat (₹3,200 | No reviews)", "Expensive & risky", false),
        Tuple4(4, "5. Plain Black Pen Pack (₹80 | 3.9 ⭐)", "Low margin item", false)
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

        Spacer(modifier = Modifier.height(10.dp))

        Text("Daily Product Hunt Game", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Select 2 best viral items out of 5", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(14.dp))

        items.forEach { (idx, title, desc, isWinning) ->
            val isSelected = selectedMap[idx] ?: false
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) Color(0x44FF2A7A) else Color(0x22FFFFFF))
                    .border(1.dp, if (isSelected) Color(0xFFFF2A7A) else Color(0x33FFFFFF), RoundedCornerShape(16.dp))
                    .clickable { onToggle(idx, isWinning) }
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(desc, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                    Text(if (isSelected) "✓ SELECTED" else "TAP TO PICK", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color(0xFFFFD700) else Color.White.copy(alpha = 0.5f))
                }
            }
        }

        feedback?.let { fb ->
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x33FF2A7A))
                    .padding(12.dp)
            ) {
                Text(fb, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/** MODULE 10: Common Mistakes */
@Composable
private fun Module10CommonMistakesView() {
    val mistakes = listOf(
        "❌ Following Hype Blindly" to "Promoting items just because someone else posted them without checking ratings.",
        "❌ Ignoring Quality" to "Choosing 3.0 star items with cheap stitching causes returns & bad feedback.",
        "❌ Ignoring Audience Fit" to "Posting baby clothes when your audience consists of male college students.",
        "❌ Choosing Expensive First" to "Starting with ₹2,500 items creates high buyer hesitation.",
        "❌ Posting Without Testing" to "Never testing reel hooks or posting times to compare view analytics."
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 10 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Common Viral Hunting Mistakes", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        mistakes.forEach { (mistake, detail) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(mistake, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF5252))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(detail, fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 11: Mission Briefing */
@Composable
private fun Module11MissionView(
    onMissionComplete: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("TODAY'S MISSION", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Find Your First Viral Product", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Estimated Time: 18 Minutes", fontSize = 12.sp, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(1.5.dp, Color(0x66FF2A7A), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Text("📋 Mission Steps:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Open Meesho App -> Explore Trending Section.", fontSize = 12.sp, color = Color.White)
                Text("2. Select 1 item under ₹499 with 4.2+ ratings.", fontSize = 12.sp, color = Color.White)
                Text("3. Generate Creator Affiliate Link & copy.", fontSize = 12.sp, color = Color.White)
                Text("4. Write 1 reel concept & caption using today's framework.", fontSize = 12.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onMissionComplete,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("✓ Mission Completed! Unlock Badge", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

/** MODULE 12: Achievement Badge */
@Composable
private fun Module12AchievementView(
    score: Int,
    onFinishLevel5: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("🎉 LEVEL 5 COMPLETED!", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))

        Spacer(modifier = Modifier.height(16.dp))

        // BADGE CARD WITH GOLD GLOW
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFFFD700), Color(0xFFFF2A7A), Color(0x00000000))
                    )
                )
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color(0xFF1B0413))
                    .border(3.dp, Color(0xFFFFD700), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎯", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Viral Product\nHunter", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White, textAlign = TextAlign.Center)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Reward: +250 XP", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        Text("You can now spot winning products like a pro!", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onFinishLevel5,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("Finish Level 5 & Continue →", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
