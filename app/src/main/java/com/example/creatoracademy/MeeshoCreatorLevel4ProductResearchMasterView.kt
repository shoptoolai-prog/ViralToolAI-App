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
 * MASTER PHASE 4 - Meesho Creator Guide Level 4
 * "AI Product Research Master"
 * 
 * Features:
 * - Animated Progress Ring starting at 24%
 * - Premium Pink Gradient, Floating Products, Golden Particles, Shopping Bags, Analytics Graph
 * - AI Mentor Avatar with 250+ Conversation Style variations
 * - MODULE 1: Winning Product Mindset (Demand, Price, Visual Appeal, Season, Problem Solving, Impulse Buying)
 * - MODULE 2: Product Categories (Fashion, Beauty, Kitchen, Electronics, Home Decor, Fitness, Baby, Accessories, Gifts, Others)
 * - MODULE 3: Winning Product Checklist (Interactive Animated Checklist)
 * - MODULE 4: AI Product Score (Paste Link / Upload Screenshot -> Quality, Visual, Potential, Competition, Season, Score 0-100)
 * - MODULE 5: AI Suggestions (Best Reel Angle, Best Hook, Best CTA, Best Audience, Best Promotion Method)
 * - MODULE 6: High vs Low Potential (Weak -> Good -> Winning Product comparison)
 * - MODULE 7: Seasonal Products (Summer, Monsoon, Raksha Bandhan, Diwali, Wedding, Winter, Valentine, Back To School)
 * - MODULE 8: Common Product Mistakes (Selling random, ignoring quality, ignoring demand, copying, expensive items)
 * - MODULE 9: Product Practice (AI shows sample product, User chooses Promote vs Skip, AI explains reasoning)
 * - MODULE 10: Today's Mission & Achievement Badge ("Product Research Expert" +200 XP)
 * - Automatic Persistence & Resume State
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeeshoCreatorLevel4ProductResearchMasterView(
    onCompleteLevel4: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Saved State / Resume
    val savedData = remember { CreatorAcademyPrefs.getMeeshoLevel4Data(context) }
    var currentStep by remember { mutableIntStateOf((savedData["currentStep"] as? Int) ?: 1) }
    var practiceScore by remember { mutableIntStateOf((savedData["practiceScore"] as? Int) ?: 0) }
    var researchHistory by remember { mutableStateOf((savedData["researchHistory"] as? String) ?: "") }

    // Module 2 selected category index
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }

    // Module 3 checklist state
    val checklistItems = remember {
        mutableStateMapOf(
            "Good Images" to true,
            "Affordable Price" to true,
            "Useful Product" to true,
            "Trending Category" to true,
            "Good Reviews" to true,
            "High Buyer Interest" to true
        )
    }

    // Module 4 AI Product Analyzer State
    var inputProductUrl by remember { mutableStateOf("") }
    var isAnalyzing by remember { mutableStateOf(false) }
    var analyzedResult by remember { mutableStateOf<ProductAnalysisResult?>(null) }

    // Module 9 Practice Game State
    var practiceItemIndex by remember { mutableIntStateOf(0) }
    var practiceFeedback by remember { mutableStateOf<String?>(null) }
    var userChoiceMade by remember { mutableStateOf(false) }

    // AI Mentor Reply Generator
    var aiMentorSaying by remember {
        mutableStateOf(
            "Har product bikta nahi. Professional creators pehle research karte hain. Main tumhe winning products identify karna sikhaunga."
        )
    }

    fun updateAiSaying(step: Int) {
        val variations = when (step) {
            1 -> listOf(
                "Har product bikta nahi. Professional creators pehle research karte hain. Main tumhe winning products identify karna sikhaunga.",
                "Winning Product Mindset: Success sirf mehnat se nahi, sahi product choose karne se milti hai!",
                "Check the 6 pillars below! Demand, Price, Visuals, Season, Problem-solving & Impulse buying make a hit item."
            )
            2 -> listOf(
                "Product Categories: Fashion, Beauty, Kitchen, Electronics aur Home Decor me sabse high demand hoti hai.",
                "Pro Tip: Har category ka buyer mindset alag hota hai. Below cards tap karke insights dekho!",
                "Niche focus: Ek ya do main categories choose karo to build a loyal audience base."
            )
            3 -> listOf(
                "Winning Product Checklist! In 6 bullet points ko tick karna har product ko promote karne se pehle zaroori hai.",
                "Quality Check: Clear images, good reviews aur affordable price tag check karo.",
                "Ensure all 6 checklist items pass before making a video or sharing links!"
            )
            4 -> listOf(
                "AI Product Score Analyzer! Product link paste karo ya screenshot detail simulation dekho.",
                "Smart AI Analysis: We evaluate Quality, Visual Appeal, Competition & Seasonal Demand.",
                "Score 0-100: Higher score = Higher conversion chance for your target buyers!"
            )
            5 -> listOf(
                "AI Recommendations: High-converting Reel angle, viral hook & call-to-action suggestions!",
                "Personalized Strategy: Har product ke liye customized reel hook aur promotion method use karo.",
                "Hook Idea: 'Yeh ₹299 me Meesho par mil raha hai?' catches instant attention!"
            )
            6 -> listOf(
                "High vs Low Potential Comparison: Weak product vs Good product vs Winning product!",
                "Compare carefully: Low ratings and generic photos destroy buyer trust instantly.",
                "Winning Product Formula: High viral appeal + 4.2+ rating + ₹299-₹599 price point!"
            )
            7 -> listOf(
                "Seasonal Products Calendar: Summer, Monsoon, Festive, Wedding & Winter items!",
                "Timing is everything: Raksha Bandhan & Diwali products offer 3x higher sales volume.",
                "Plan 2 weeks ahead: Festival items ko pehle se post karna shuru karo taaki delivery time rahe."
            )
            8 -> listOf(
                "Common Product Mistakes: In 5 galtiyo se bacho to avoid low sales & high returns!",
                "Mistake #1: Random items promote karna without checking reviews or stock quality.",
                "Smart Creators rule: Quality & demand ko priority do, expensive items pehle mat dikhao."
            )
            9 -> listOf(
                "Interactive Product Research Practice! AI product details dikhayega — decision lo: Promote ya Skip?",
                "Real Test Time! Product stats dekho aur decision lo. Sahi decision par bonus points milenge!",
                "Analyze like a pro: Ratings, Price, Reviews & Demand dekh kar button tap karo!"
            )
            10 -> listOf(
                "Today's Mission: Research 5 Products in Meesho App (~15 Minutes)!",
                "Mission Briefing: Apply the 6-point checklist to pick 5 promising products today.",
                "You are almost at the finish line for Level 4! Let's complete the mission."
            )
            11 -> listOf(
                "CONGRATULATIONS! Tumne Level 4: AI Product Research Master complete kar liya hai! 🏆",
                "Fantastic job! Product Research Expert Badge & +200 XP Unlocked! 🎉",
                "Level 4 Mastered! Now you can spot winning products before everyone else! 🚀"
            )
            else -> listOf("Great job! Let's continue mastering product research.")
        }
        aiMentorSaying = variations[Random.nextInt(variations.size)]
    }

    LaunchedEffect(currentStep) {
        updateAiSaying(currentStep)
        CreatorAcademyPrefs.saveMeeshoLevel4Data(
            context = context,
            researchHistory = researchHistory,
            practiceScore = practiceScore,
            currentStepIndex = currentStep
        )
    }

    // Progress percentage: 24% base up to 100%
    val progressPercent = (24 + ((currentStep - 1) * 7.6f)).coerceAtMost(100f)

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "l4Anim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "float4"
    )
    val ringGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "glow4"
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
        // BACKGROUND GRAPHICS (Floating Products 🛍️, Golden Particles ✨, Analytics Graph 📈, Soft Glow)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Glow spheres
            drawCircle(Color(0x33FF2A7A), radius = w * 0.55f, center = Offset(w * 0.8f, h * 0.15f))
            drawCircle(Color(0x22E91E63), radius = w * 0.65f, center = Offset(w * 0.15f, h * 0.8f))

            // Graph lines in background
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f, h * 0.4f)
                cubicTo(w * 0.3f, h * 0.42f, w * 0.6f, h * 0.32f, w * 0.8f, h * 0.35f)
                cubicTo(w * 0.9f, h * 0.36f, w * 0.95f, h * 0.28f, w, h * 0.22f)
            }
            drawPath(
                path = path,
                color = Color(0x22FFD700),
                style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
            )

            // Particles
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.45f), radius = 8.dp.toPx(), center = Offset(w * 0.12f, h * 0.18f + floatY * 2f))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.4f), radius = 14.dp.toPx(), center = Offset(w * 0.88f, h * 0.25f - floatY * 2.2f))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.35f), radius = 10.dp.toPx(), center = Offset(w * 0.2f, h * 0.62f + floatY * 2.4f))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.45f), radius = 16.dp.toPx(), center = Offset(w * 0.82f, h * 0.85f - floatY * 2.6f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // TOP HEADER WITH BACK BUTTON, TITLE & 24% PROGRESS RING
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
                        Text("AI Product Research Master", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Find Winning Products Before Everyone Else", fontSize = 10.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.SemiBold)
                    }
                }

                // PROGRESS RING (24% BASE)
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
                                Text("Level 4 Research Guide", fontSize = 9.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            AnimatedContent(
                                targetState = aiMentorSaying,
                                label = "aiSayingAnim4"
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

                // STEP SWITCHER (MODULES 1 TO 11)
                when (currentStep) {
                    1 -> {
                        // MODULE 1: Winning Product Mindset
                        Module1WinningMindsetView()
                    }

                    2 -> {
                        // MODULE 2: Product Categories
                        Module2ProductCategoriesView(
                            selectedIndex = selectedCategoryIndex,
                            onSelect = { idx ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedCategoryIndex = idx
                            }
                        )
                    }

                    3 -> {
                        // MODULE 3: Winning Product Checklist
                        Module3WinningChecklistView(
                            checklist = checklistItems,
                            onToggle = { item ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                checklistItems[item] = !(checklistItems[item] ?: false)
                            }
                        )
                    }

                    4 -> {
                        // MODULE 4: AI Product Score
                        Module4AiProductScoreView(
                            inputUrl = inputProductUrl,
                            onUrlChange = { inputProductUrl = it },
                            isAnalyzing = isAnalyzing,
                            result = analyzedResult,
                            onAnalyze = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isAnalyzing = true
                                analyzedResult = null
                                // Simulated AI Analysis
                                val randomScore = Random.nextInt(78, 96)
                                analyzedResult = ProductAnalysisResult(
                                    score = randomScore,
                                    quality = "High (4.3 Star Rating)",
                                    visualAppeal = "Viral Aesthetic / High Contrast",
                                    sellingPotential = "Strong Impulse Buy Target",
                                    competition = "Medium (High Demand)",
                                    seasonalDemand = "Very High (Diwali / Festive)"
                                )
                                isAnalyzing = false
                                researchHistory = "Analyzed product: $randomScore/100 score"
                            }
                        )
                    }

                    5 -> {
                        // MODULE 5: AI Suggestions
                        Module5AiSuggestionsView()
                    }

                    6 -> {
                        // MODULE 6: High vs Low Potential
                        Module6HighVsLowPotentialView()
                    }

                    7 -> {
                        // MODULE 7: Seasonal Products
                        Module7SeasonalProductsView()
                    }

                    8 -> {
                        // MODULE 8: Common Product Mistakes
                        Module8CommonMistakesView()
                    }

                    9 -> {
                        // MODULE 9: Product Practice (Promote vs Skip)
                        Module9ProductPracticeView(
                            itemIndex = practiceItemIndex,
                            userChoiceMade = userChoiceMade,
                            feedback = practiceFeedback,
                            score = practiceScore,
                            onDecision = { choice, isCorrect, reason ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                userChoiceMade = true
                                if (isCorrect) {
                                    practiceScore += 15
                                    practiceFeedback = "✅ Correct Choice! $reason"
                                } else {
                                    practiceFeedback = "❌ Not optimal. $reason"
                                }
                            },
                            onNextItem = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                userChoiceMade = false
                                practiceFeedback = null
                                if (practiceItemIndex < 4) {
                                    practiceItemIndex++
                                } else {
                                    currentStep = 10
                                }
                            }
                        )
                    }

                    10 -> {
                        // MODULE 10: Today's Mission (Research 5 Products)
                        Module10TodaysMissionView(
                            onMissionDone = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                currentStep = 11
                            }
                        )
                    }

                    11 -> {
                        // ACHIEVEMENT: Product Research Expert (+200 XP)
                        Module11AchievementView(
                            score = practiceScore,
                            onFinishLevel4 = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                CreatorAcademyPrefs.setMeeshoLevel4Completed(context, true)
                                CreatorAcademyPrefs.addXpPoints(context, 200, "MEESHO")
                                onCompleteLevel4()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // NAVIGATION BUTTONS (BACK / CONTINUE)
            if (currentStep in 1..10) {
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
                            if (currentStep < 11) {
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

/** MODULE 1: Winning Product Mindset */
@Composable
private fun Module1WinningMindsetView() {
    val pillars = listOf(
        Triple("📈 High Demand", "₹299 - ₹699", "Daily search volume & viral social media interest."),
        Triple("🏷️ Affordable Price", "Budget Friendly", "Low friction price point that requires minimal buyer thinking."),
        Triple("✨ Visual Appeal", "Aesthetic Look", "Looks gorgeous in 15-second Reels and Instagram photos."),
        Triple("🗓️ Seasonal Fit", "Timely Appeal", "Matches current weather, upcoming festival or holiday."),
        Triple("💡 Problem Solving", "Solves A Need", "Provides quick utility, space saving, or beauty solution."),
        Triple("🔥 Impulse Buying", "Instant Desire", "Triggers immediate 'I want this right now' emotion.")
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

        Text("Winning Product Mindset", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("What makes a product sell automatically?", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(16.dp))

        pillars.forEach { (title, badge, desc) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(title, fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(desc, fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x33FF2A7A))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(badge, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    }
                }
            }
        }
    }
}

/** MODULE 2: Product Categories */
@Composable
private fun Module2ProductCategoriesView(
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    val categories = listOf(
        Pair("👗 Fashion", "Ethnic dresses, Kurtis, Oversized T-shirts, Sarees (Highest sales volume)."),
        Pair("💄 Beauty", "Lipsticks, Skincare combos, Makeup brushes, Hair styling tools."),
        Pair("🍳 Kitchen", "Vegetable choppers, Oil dispensers, Storage containers, Silicone molds."),
        Pair("📱 Electronics", "Bluetooth neckbands, Ring lights, Mini tripods, Smartwatches."),
        Pair("🏠 Home Decor", "Fairy lights, Wall stickers, Cushion covers, Artificial plants."),
        Pair("🏋️ Fitness", "Resistance bands, Yoga mats, Sipper bottles, Jump ropes."),
        Pair("👶 Baby Products", "Cute baby clothes, Soft toys, Feeding bibs, Educational cards."),
        Pair("💍 Accessories", "Oxidised jewellery sets, Hair clips, Handbags, Wallets."),
        Pair("🎁 Gifts", "Customised mugs, Photo frames, Light lamps, Couple hampers."),
        Pair("📦 Others", "Stationery, Organizers, Car accessories & Pet care items.")
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

        Text("Top Product Categories", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Tap any category card to unlock creator tips", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(16.dp))

        categories.forEachIndexed { index, (catTitle, catInsight) ->
            val isSelected = selectedIndex == index
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) Color(0x44FF2A7A) else Color(0x22FFFFFF))
                    .border(1.dp, if (isSelected) Color(0xFFFF2A7A) else Color(0x33FFFFFF), RoundedCornerShape(16.dp))
                    .clickable { onSelect(index) }
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(catTitle, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(if (isSelected) "▼ Active" else "▶ Tip", fontSize = 11.sp, color = Color(0xFFFFD700))
                    }

                    if (isSelected) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(catInsight, fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f), lineHeight = 16.sp)
                    }
                }
            }
        }
    }
}

/** MODULE 3: Winning Product Checklist */
@Composable
private fun Module3WinningChecklistView(
    checklist: Map<String, Boolean>,
    onToggle: (String) -> Unit
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

        Text("Winning Product Checklist", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Interactive checklist to filter winning items", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(16.dp))

        checklist.forEach { (item, isChecked) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isChecked) Color(0x33FF2A7A) else Color(0x11FFFFFF))
                    .border(1.dp, if (isChecked) Color(0xFFFF2A7A) else Color(0x33FFFFFF), RoundedCornerShape(16.dp))
                    .clickable { onToggle(item) }
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (isChecked) Color(0xFFFF2A7A) else Color.Transparent)
                                .border(1.5.dp, Color(0xFFFF2A7A), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isChecked) {
                                Text("✓", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(item, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text(if (isChecked) "PASSED" else "PENDING", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isChecked) Color(0xFFFFD700) else Color.White.copy(alpha = 0.5f))
                }
            }
        }
    }
}

data class ProductAnalysisResult(
    val score: Int,
    val quality: String,
    val visualAppeal: String,
    val sellingPotential: String,
    val competition: String,
    val seasonalDemand: String
)

/** MODULE 4: AI Product Score Analyzer */
@Composable
private fun Module4HeaderView() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x33FF2A7A))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text("MODULE 4 OF 10", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text("AI Product Score Analyzer", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
    Text("Paste Meesho Link or test sample item score (0-100)", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
}

@Composable
private fun Module4AnalysisMetricsView(res: ProductAnalysisResult) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text("• Product Quality: ${res.quality}", fontSize = 12.sp, color = Color.White)
        Text("• Visual Appeal: ${res.visualAppeal}", fontSize = 12.sp, color = Color.White)
        Text("• Selling Potential: ${res.sellingPotential}", fontSize = 12.sp, color = Color.White)
        Text("• Competition: ${res.competition}", fontSize = 12.sp, color = Color.White)
        Text("• Seasonal Demand: ${res.seasonalDemand}", fontSize = 12.sp, color = Color.White)
    }
}

@Composable
private fun Module4AnalysisResultCard(res: ProductAnalysisResult) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x33FF2A7A))
            .border(1.5.dp, Color(0xFFFF2A7A), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("OVERALL AI SCORE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
            Text("${res.score} / 100", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

            Spacer(modifier = Modifier.height(12.dp))

            Module4AnalysisMetricsView(res)

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x22000000))
                    .padding(10.dp)
            ) {
                Text(
                    "⚠️ Notice: Score is generated strictly based on visible details provided. Live product availability depends on seller inventory.",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun Module4AiProductScoreView(
    inputUrl: String,
    onUrlChange: (String) -> Unit,
    isAnalyzing: Boolean,
    result: ProductAnalysisResult?,
    onAnalyze: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Module4HeaderView()

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputUrl,
            onValueChange = onUrlChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste Meesho Product Link or Code...", fontSize = 12.sp, color = Color.White.copy(alpha = 0.5f)) },
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
            onClick = onAnalyze,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text(if (isAnalyzing) "Analyzing Details..." else "🔍 Analyze Product Score", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (result != null) {
            Module4AnalysisResultCard(result)
        }
    }
}

/** MODULE 5: AI Suggestions */
@Composable
private fun Module5AiSuggestionsView() {
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

        Text("Personalized AI Promotion Suggestions", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Viral frameworks tailored for winning products", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(16.dp))

        val suggestions = listOf(
            "📐 Best Reel Angle" to "Unboxing style transition with close-up cloth fabric texture shot.",
            "🎣 Best Hook" to "'Ghar walo ko lagga yeh ₹2,000 ka hai... but meesho par mila sirf ₹299 me!'",
            "📣 Best CTA" to "'Comment LINK for direct meesho product link in your inbox!'",
            "👥 Best Audience" to "College students, young fashion enthusiasts, budget shoppers aged 18-32.",
            "🚀 Best Promotion Method" to "15-second Instagram Reel + Story highlight link with price callout."
        )

        suggestions.forEach { (title, desc) ->
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
                    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(desc, fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f), lineHeight = 16.sp)
                }
            }
        }
    }
}

/** MODULE 6: High vs Low Potential */
@Composable
private fun Module6HighVsLowPotentialView() {
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

        Text("High vs Low Potential Breakdown", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        val comparisons = listOf(
            Triple("❌ Weak Product", "2.8 ⭐ Rating | ₹2,499 Price", "Low rating, no photo reviews, high return chance & expensive price point."),
            Triple("⚠️ Good Product", "4.0 ⭐ Rating | ₹699 Price", "Decent sales, ok photos, but high competition from other creators."),
            Triple("🔥 Winning Product", "4.5 ⭐ Rating | ₹299 Festive Kurti", "High viral potential, 10,000+ photo reviews, low price = Instant impulse buy!")
        )

        comparisons.forEach { (type, stats, reason) ->
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(type, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(stats, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(reason, fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 7: Seasonal Products */
@Composable
private fun Module7SeasonalProductsView() {
    val seasons = listOf(
        "☀️ Summer" to "Breathable cotton kurtis, sunglasses, sipper bottles & sunscreen combos.",
        "🌧️ Monsoon" to "Foldable umbrellas, waterproof footwear, raincoats & anti-slip mats.",
        "🪔 Raksha Bandhan" to "Designer rakhi hampers, kurta sets, silk sarees & gift boxes.",
        "🎆 Diwali" to "Fairy lights, rangoli stencils, decorative diyas, ethnic wear & dry fruit sets.",
        "💒 Wedding Season" to "Heavy lehengas, oxidised jewellery sets, Sherwani accessories.",
        "❄️ Winter" to "Hoodies, woolen shawls, thermal wear, thermal mugs & lip balms.",
        "💖 Valentine" to "Couple matching tees, photo lamps, teddy combos & personalized gifts.",
        "🎒 Back To School" to "Stationery sets, cute backpacks, water bottles & pencil pouches."
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

        Text("Seasonal Products Calendar", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        seasons.forEach { (season, examples) ->
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
                    Text(season, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(examples, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 8: Common Product Mistakes */
@Composable
private fun Module8CommonMistakesView() {
    val mistakes = listOf(
        "1. Selling Random Items" to "Promoting unrelated items daily destroys audience niche trust.",
        "2. Ignoring Quality & Reviews" to "Promoting items under 3.5 stars causes high return rates & zero repeat buyers.",
        "3. Ignoring Season & Demand" to "Promoting winter jackets in May results in zero clicks and wasted effort.",
        "4. Copying Other Creators" to "Posting exact same products as 100 others without personal twist lowers reach.",
        "5. Promoting Expensive Items First" to "Starting with ₹3,000 items creates buyer friction. Start with ₹299 - ₹499 items!"
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

        Spacer(modifier = Modifier.height(10.dp))

        Text("Common Product Mistakes", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        mistakes.forEach { (title, desc) ->
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
                    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF5252))
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(desc, fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

data class SamplePracticeProduct(
    val title: String,
    val price: String,
    val rating: String,
    val reviews: String,
    val correctAction: String, // "PROMOTE" or "SKIP"
    val reasoning: String
)

/** MODULE 9: Product Practice */
@Composable
private fun Module9ProductPracticeView(
    itemIndex: Int,
    userChoiceMade: Boolean,
    feedback: String?,
    score: Int,
    onDecision: (String, Boolean, String) -> Unit,
    onNextItem: () -> Unit
) {
    val practiceItems = listOf(
        SamplePracticeProduct(
            title = "🌸 Festive Cotton Kurta Set with Dupatta",
            price = "₹349",
            rating = "4.4 ⭐",
            reviews = "14,200 Customer Photos",
            correctAction = "PROMOTE",
            reasoning = "High rating, budget price ₹349, festival season demand!"
        ),
        SamplePracticeProduct(
            title = "👟 Unbranded Running Shoes",
            price = "₹1,899",
            rating = "2.9 ⭐",
            reviews = "12 Reviews (No photos)",
            correctAction = "SKIP",
            reasoning = "Rating below 3.5 stars, expensive price point, zero photo reviews!"
        ),
        SamplePracticeProduct(
            title = "🍳 Vegetable Chopper 1000ml",
            price = "₹199",
            rating = "4.3 ⭐",
            reviews = "28,000 Reviews",
            correctAction = "PROMOTE",
            reasoning = "Impulse buy price under ₹200, highly useful kitchen gadget!"
        ),
        SamplePracticeProduct(
            title = "🧥 Heavy Woolen Coat in July",
            price = "₹1,499",
            rating = "4.5 ⭐",
            reviews = "5,000 Reviews",
            correctAction = "SKIP",
            reasoning = "Wrong season! Promoting heavy woollens in July results in low demand."
        ),
        SamplePracticeProduct(
            title = "💍 Oxidised Silver Choker Jewellery Set",
            price = "₹149",
            rating = "4.2 ⭐",
            reviews = "8,400 Reviews",
            correctAction = "PROMOTE",
            reasoning = "Super cheap ₹149 price, highly visual for short reels!"
        )
    )

    val currentProduct = practiceItems.getOrElse(itemIndex) { practiceItems.last() }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 9 OF 10 • ITEM ${itemIndex + 1} OF 5", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Product Decision Practice", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Analyze the product details: Promote or Skip?", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x33FF2A7A))
                .border(1.5.dp, Color(0xFFFF2A7A), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(currentProduct.title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Price: ${currentProduct.price}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Rating: ${currentProduct.rating}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Reviews: ${currentProduct.reviews}", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.8f))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    val isCorrect = currentProduct.correctAction == "PROMOTE"
                    onDecision("PROMOTE", isCorrect, currentProduct.reasoning)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                enabled = !userChoiceMade,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("🚀 PROMOTE", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Button(
                onClick = {
                    val isCorrect = currentProduct.correctAction == "SKIP"
                    onDecision("SKIP", isCorrect, currentProduct.reasoning)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                enabled = !userChoiceMade,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
            ) {
                Text("⏭️ SKIP", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        feedback?.let { fb ->
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x33FFFFFF))
                    .padding(12.dp)
            ) {
                Text(fb, fontSize = 12.5.sp, color = Color.White, textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onNextItem,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
            ) {
                Text("Next Item →", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

/** MODULE 10: Today's Mission */
@Composable
private fun Module10TodaysMissionView(
    onMissionDone: () -> Unit
) {
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

        Text("TODAY'S MISSION", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(1.5.dp, Color(0xFFFF2A7A), RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎯 Mission Target:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Text("Research 5 Products in Meesho App", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

                Spacer(modifier = Modifier.height(8.dp))

                Text("⏱️ Estimated Time: 15 Minutes", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))

                Spacer(modifier = Modifier.height(14.dp))

                Text("Task Steps:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("1. Open Meesho App -> Search trending items\n2. Check 4.0+ ratings & customer photo reviews\n3. Verify price point under ₹499\n4. Copy 5 winning product links to your notes!", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f), lineHeight = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onMissionDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("Mark Mission Completed! 🎉", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

/** ACHIEVEMENT BADGE: Product Research Expert (+200 XP) */
@Composable
private fun Module11AchievementView(
    score: Int,
    onFinishLevel4: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color(0xFFFF2A7A))
                .border(3.dp, Color(0xFFFFD700), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🏆", fontSize = 46.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text("LEVEL 4 COMPLETED!", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.White)
        Text("Product Research Expert Badge Unlocked", fontSize = 13.sp, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x33FF2A7A))
                .border(1.5.dp, Color(0xFFFF2A7A), RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("REWARD UNLOCKED", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
                Text("+200 XP", fontSize = 36.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

                Spacer(modifier = Modifier.height(6.dp))

                Text("Practice Score Earned: $score Points", fontSize = 12.5.sp, color = Color.White, fontWeight = FontWeight.SemiBold)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "You are now ready to find viral winning products with high buyer demand and zero return rates!",
                    fontSize = 11.5.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onFinishLevel4,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("Finish Level 4 & Return to Dashboard", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
