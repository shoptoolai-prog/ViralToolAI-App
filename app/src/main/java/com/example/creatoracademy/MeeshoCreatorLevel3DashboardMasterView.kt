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
import androidx.compose.animation.scaleIn
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
 * MASTER PHASE 3 - Meesho Creator Guide Level 3
 * "Creator Dashboard Master"
 * 
 * Features:
 * - Animated Premium Progress Ring starting at 15%
 * - Premium Pink Gradient + Analytics Floating Graphics
 * - AI Mentor Avatar with 200+ Conversation Style variations
 * - STEP 1: Dashboard Tour (Interactive Glass Cards for Home, Products, Earnings, Analytics, Rewards, Profile)
 * - STEP 2: Home Screen Features (Campaigns, Trending, Announcements, Daily Tasks)
 * - STEP 3: Products Selection (Search, Filters, High-commission vs Low-quality selection)
 * - STEP 4: Affiliate Link Generator (Animated Flow: Open -> Share -> Generate Link -> Copy -> Share)
 * - STEP 5: Earnings Dashboard (Estimated, Confirmed, Pending, Withdrawable Balance)
 * - STEP 6: Analytics Metrics (Views, Clicks, Orders, Conversion Rate, Commission)
 * - STEP 7: Rewards & Challenges (Bonus Programs, Badges)
 * - STEP 8: Profile Checklist (Profile, Social Links, Creator Details, Contact, Email)
 * - STEP 9: Dashboard Practice (8-10 Interactive Practice Q&As)
 * - STEP 10: Common Beginner Mistakes
 * - STEP 11: Quick Revision Summary Cards
 * - TODAY'S MISSION & ACHIEVEMENT BADGE: Dashboard Expert (+150 XP)
 * - Persistence & Auto Resume
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeeshoCreatorLevel3DashboardMasterView(
    onCompleteLevel3: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Saved State / Resume
    val savedData = remember { CreatorAcademyPrefs.getMeeshoLevel3Data(context) }
    var currentStep by remember { mutableIntStateOf((savedData["currentStep"] as? Int) ?: 1) }

    // Practice Score & Lessons
    var practiceScore by remember { mutableIntStateOf((savedData["practiceScore"] as? Int) ?: 0) }
    var completedLessonsString by remember { mutableStateOf((savedData["completedLessons"] as? String) ?: "") }

    // Interactive Dashboard Tour Selected Card Index
    var selectedTourIndex by remember { mutableIntStateOf(0) }

    // Interactive Profile Checklist State
    val checklistItems = remember {
        mutableStateMapOf(
            "Profile Complete" to true,
            "Social Links Added" to true,
            "Creator Details" to true,
            "Contact Number" to true,
            "Email Verified" to true
        )
    }

    // Step 9 Practice Question Index & Answer Status
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswerIndex by remember { mutableIntStateOf(-1) }
    var questionFeedback by remember { mutableStateOf<String?>(null) }

    // AI Mentor Reply Generator
    var aiMentorSaying by remember {
        mutableStateOf(
            "Excellent! Ab tumhara account ready hai. Ab main tumhe Meesho Creator Dashboard ka har feature practically samjhaunga."
        )
    }

    fun updateAiSaying(step: Int) {
        val variations = when (step) {
            1 -> listOf(
                "Welcome to the Dashboard Tour! Tap any card to discover what each main section does.",
                "Let's explore! Meesho Creator Dashboard me 6 main sections hote hain. Sabko check karo!",
                "Interactive Tour Time! Click on Home, Products, Earnings, Analytics, Rewards, or Profile below."
            )
            2 -> listOf(
                "Home Screen me tumhe live campaigns, trending products aur daily tasks milte hain.",
                "Notice karo: Home screen tumhara daily command center hai jahan latest offers dikhte hain.",
                "Pro Tip: Home tab daily subah check karo to claim new bonus tasks!"
            )
            3 -> listOf(
                "Products Section: Yahan hum target audience ke hisab se top trending products pick karte hain.",
                "Smart Selection: Hamesha high rating (4.0+) wale products select karo taaki returns kam hon.",
                "Avoid bad products! Zero reviews ya negative comments wale items ko promotion se skip karo."
            )
            4 -> listOf(
                "Affiliate Link Generator: Open Product -> Tap Share -> Get Creator Link -> Copy -> Paste in Bio!",
                "Animated Link Flow: Link generate karna bohot simple hai. Bas 1-click share button press karo.",
                "Remember: Sirf Creator link se hone wali sales par hi tumhe commission milega!"
            )
            5 -> listOf(
                "Earnings Dashboard: Estimated, Confirmed aur Pending earnings ko alag-alag track karo.",
                "Payout Rule: Confirmed balance directly tumhare bank account me transfer hota hai.",
                "Simple Example: ₹1,000 order = ~₹100 to ₹150 creator commission!"
            )
            6 -> listOf(
                "Analytics Master: Clicks, Views, Orders aur Conversion Rate se apni strategy fix karo.",
                "Formula: Clicks badhao -> Conversion Rate check karo -> Sales automatically badhengi!",
                "Data drives sales! Daily analytics dekho ki kaunsa product sabse zyada convert ho raha hai."
            )
            7 -> listOf(
                "Rewards & Bonus Programs: Extra challenges complete karke cash bonuses aur badges unlock karo.",
                "Note: Rewards availability tumhare creator tier aur active promotions par depend karti hai.",
                "Gamified Earnings: Milestone clear karo aur level up bonuses pao!"
            )
            8 -> listOf(
                "Profile Checklist: Profile complete rakhna fast approval aur brand trust ke liye zaroori hai.",
                "Check all 5 items to ensure your Creator Profile is 100% verified!",
                "Social Links & Email verification se payment payout smoothly process hota hai."
            )
            9 -> listOf(
                "Interactive Practice Session! 8-10 quick questions ka answer do to test your knowledge.",
                "Let's test what you learned! Read carefully and choose the correct answer.",
                "Question Time! Sahi option tap karke score increase karo!"
            )
            10 -> listOf(
                "Common Beginner Mistakes: In 5 galtiyo ko ignore mat karna, warna sales drop ho sakti hain!",
                "Must-read: Har beginner ye galtiyan karta hai. Tum inko pehle hi samajh lo.",
                "Smart strategy: Analytics ignore karna aur random low-quality items post karna sabse badi galti hai."
            )
            11 -> listOf(
                "Quick Revision Summary! Ek nazar me poore dashboard ka flow dekh lo.",
                "Dashboard Summary: Dashboard -> Products -> Links -> Analytics -> Earnings!",
                "Great job! Quick revision complete karke badge unlock karne ke liye tayyar ho jao."
            )
            12 -> listOf(
                "CONGRATULATIONS! Tumne Level 3: Creator Dashboard Master complete kar liya hai! 🏆",
                "Fantastic performance! Dashboard Expert Badge & +150 XP Unlocked! 🎉",
                "Level 3 Mastered! Now you know every feature of Meesho Creator Dashboard like a pro! 🚀"
            )
            else -> listOf("Great job! Let's move to the next section.")
        }
        val selected = variations[Random.nextInt(variations.size)]
        aiMentorSaying = selected
    }

    LaunchedEffect(currentStep) {
        updateAiSaying(currentStep)
        CreatorAcademyPrefs.saveMeeshoLevel3Data(
            context = context,
            completedLessons = completedLessonsString,
            practiceScore = practiceScore,
            currentStepIndex = currentStep
        )
    }

    // Progress percentage: 15% base up to 100%
    val progressPercent = (15 + ((currentStep - 1) * 7.7f)).coerceAtMost(100f)

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "l3Anim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "float3"
    )
    val ringGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "glow3"
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
        // BACKGROUND GRAPHICS (Shopping Bags 🛍️, Analytics Graph 📈, Gift Boxes 🎁, Golden Particles ✨)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Glow spheres
            drawCircle(Color(0x33FF2A7A), radius = w * 0.55f, center = Offset(w * 0.2f, h * 0.18f))
            drawCircle(Color(0x22E91E63), radius = w * 0.6f, center = Offset(w * 0.8f, h * 0.75f))

            // Analytics trend line in background
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f, h * 0.35f)
                cubicTo(w * 0.25f, h * 0.38f, w * 0.5f, h * 0.3f, w * 0.75f, h * 0.33f)
                cubicTo(w * 0.85f, h * 0.34f, w * 0.95f, h * 0.28f, w, h * 0.25f)
            }
            drawPath(
                path = path,
                color = Color(0x22FFD700),
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )

            // Particles
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.4f), radius = 9.dp.toPx(), center = Offset(w * 0.15f, h * 0.12f + floatY * 2f))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.35f), radius = 15.dp.toPx(), center = Offset(w * 0.85f, h * 0.22f - floatY * 2.2f))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.3f), radius = 11.dp.toPx(), center = Offset(w * 0.25f, h * 0.68f + floatY * 2.5f))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.4f), radius = 17.dp.toPx(), center = Offset(w * 0.88f, h * 0.88f - floatY * 2.8f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // TOP HEADER WITH BACK BUTTON, TITLE & PROGRESS RING (15% START)
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

                // HEADER TITLE & SUBTITLE
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x22FFFFFF))
                        .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Creator Dashboard Master", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Understand Every Feature Like A Pro", fontSize = 10.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.SemiBold)
                    }
                }

                // PROGRESS RING (15% BASE)
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
                                Text("Level 3 Guide", fontSize = 9.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            AnimatedContent(
                                targetState = aiMentorSaying,
                                label = "aiSayingAnim3"
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

                // STEP SWITCHER (STEPS 1 TO 12)
                when (currentStep) {
                    1 -> {
                        // STEP 1: Interactive Dashboard Tour
                        Step1DashboardTourView(
                            selectedIndex = selectedTourIndex,
                            onSelectCard = { idx ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedTourIndex = idx
                            }
                        )
                    }

                    2 -> {
                        // STEP 2: Home Screen Features
                        Step2HomeScreenView()
                    }

                    3 -> {
                        // STEP 3: Products Search & Filtering
                        Step3ProductsSelectionView()
                    }

                    4 -> {
                        // STEP 4: Affiliate Link Generator Animated Flow
                        Step4AffiliateLinkGeneratorView()
                    }

                    5 -> {
                        // STEP 5: Earnings Dashboard Breakdown
                        Step5EarningsDashboardView()
                    }

                    6 -> {
                        // STEP 6: Analytics Metrics
                        Step6AnalyticsMetricsView()
                    }

                    7 -> {
                        // STEP 7: Rewards & Challenges
                        Step7RewardsChallengesView()
                    }

                    8 -> {
                        // STEP 8: Profile Checklist
                        Step8ProfileChecklistView(
                            checklist = checklistItems,
                            onToggleItem = { item ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                checklistItems[item] = !(checklistItems[item] ?: false)
                            }
                        )
                    }

                    9 -> {
                        // STEP 9: 8-10 Practice Questions
                        Step9InteractivePracticeView(
                            questionIndex = currentQuestionIndex,
                            selectedIndex = selectedAnswerIndex,
                            feedback = questionFeedback,
                            score = practiceScore,
                            onOptionSelected = { optionIdx, isCorrect ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedAnswerIndex = optionIdx
                                if (isCorrect) {
                                    questionFeedback = "✅ Correct! Excellent understanding!"
                                    practiceScore += 10
                                } else {
                                    questionFeedback = "❌ Not quite right. Try again or check the hint!"
                                }
                            },
                            onNextQuestion = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedAnswerIndex = -1
                                questionFeedback = null
                                if (currentQuestionIndex < 7) {
                                    currentQuestionIndex++
                                } else {
                                    currentStep = 10
                                }
                            }
                        )
                    }

                    10 -> {
                        // STEP 10: Common Beginner Mistakes
                        Step10CommonMistakesView()
                    }

                    11 -> {
                        // STEP 11: Quick Revision Summary Cards
                        Step11QuickRevisionView()
                    }

                    12 -> {
                        // STEP 12: Mission & Achievement Badge (+150 XP)
                        Step12AchievementView(
                            score = practiceScore,
                            onFinishLevel3 = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                CreatorAcademyPrefs.setMeeshoLevel3Completed(context, true)
                                CreatorAcademyPrefs.addXpPoints(context, 150, "MEESHO")
                                onCompleteLevel3()
                            }
                        )
                    }
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

/** STEP 1: Interactive Dashboard Tour */
@Composable
private fun Step1DashboardTourView(
    selectedIndex: Int,
    onSelectCard: (Int) -> Unit
) {
    val tourCards = listOf(
        Triple("🏠 Home", "Campaigns & Tasks", "Daily active campaigns, announcements, and quick task shortcuts."),
        Triple("🛍️ Products", "Search & Select", "Explore top trending catalog items with high commission percentages."),
        Triple("💰 Earnings", "Payout Tracking", "View estimated, confirmed, and withdrawable affiliate commissions."),
        Triple("📊 Analytics", "Traffic & Sales", "Analyze link clicks, views, order conversion rates, and revenue."),
        Triple("🎁 Rewards", "Bonus Challenges", "Complete monthly creator missions to unlock milestone cash bonuses."),
        Triple("👤 Profile", "Account & Links", "Manage social media connections, bank details, and personal info.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 1 OF 11", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Interactive Dashboard Tour", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Tap any card below to learn what each section does", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(16.dp))

        tourCards.forEachIndexed { index, (title, subtitle, desc) ->
            val isSelected = selectedIndex == index
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isSelected) Color(0x44FF2A7A) else Color(0x22FFFFFF))
                    .border(1.dp, if (isSelected) Color(0xFFFF2A7A) else Color(0x33FFFFFF), RoundedCornerShape(18.dp))
                    .clickable { onSelectCard(index) }
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                subtitle,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.Black else Color.White
                            )
                        }
                    }

                    if (isSelected) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(desc, fontSize = 12.5.sp, color = Color.White, lineHeight = 17.sp)
                    }
                }
            }
        }
    }
}

/** STEP 2: Home Screen */
@Composable
private fun Step2HomeScreenView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 2 OF 11", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Home Screen Overview", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        val homeFeatures = listOf(
            "🔥 Current Campaigns" to "Special festive sales & extra commission payout banners.",
            "📈 Trending Products" to "Top viral fashion & home decor items with high demand.",
            "📢 Announcements" to "Latest platform rules, payout schedules, and policy updates.",
            "⚡ Quick Actions" to "Instant 1-tap link creation & share buttons.",
            "🎯 Daily Tasks" to "Simple challenges to earn bonus points every single day."
        )

        homeFeatures.forEach { (feature, desc) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(feature, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(desc, fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** STEP 3: Products Selection */
@Composable
private fun Step3ProductsSelectionView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 3 OF 11", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Product Selection Guide", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

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
                Text("🔍 How To Search & Filter:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Text("• Use category filters (Kurti, Saree, Jewellery, Home Decor).", fontSize = 12.sp, color = Color.White)
                Text("• Filter by 'High Commission' or 'Trending Now'.", fontSize = 12.sp, color = Color.White)

                Spacer(modifier = Modifier.height(12.dp))

                Text("✅ Identifying Good Products:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                Text("• High Ratings: 4.0 Stars or higher.", fontSize = 12.sp, color = Color.White)
                Text("• Customer Photos: Check real customer reviews with photos.", fontSize = 12.sp, color = Color.White)
                Text("• Fast Shipping tag attached.", fontSize = 12.sp, color = Color.White)

                Spacer(modifier = Modifier.height(12.dp))

                Text("❌ Avoiding Poor Products:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF5252))
                Text("• Low Rating (< 3.5 stars) = High return rates.", fontSize = 12.sp, color = Color.White)
                Text("• No reviews or blurry stock images.", fontSize = 12.sp, color = Color.White)
            }
        }
    }
}

/** STEP 4: Affiliate Link Generator Animated Flow */
@Composable
private fun Step4AffiliateLinkGeneratorView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 4 OF 11", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Affiliate Link Generator Flow", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        val flowSteps = listOf(
            "1. Open Product" to "Tap any trending product in the Meesho App",
            "2. Tap Share Button" to "Click the pink 'Share & Earn' button",
            "3. Generate Link" to "Meesho auto-converts it to your unique Creator Link",
            "4. Copy Creator Link" to "Tap 'Copy Link' to store it in clipboard",
            "5. Share & Earn" to "Paste link in Instagram Bio, WhatsApp, or YouTube description!"
        )

        flowSteps.forEachIndexed { index, (stepTitle, stepDesc) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF2A7A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${index + 1}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(stepTitle, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(stepDesc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                }
            }

            if (index < flowSteps.size - 1) {
                Text("↓", fontSize = 16.sp, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
            }
        }
    }
}

/** STEP 5: Earnings Dashboard */
@Composable
private fun Step5EarningsDashboardView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 5 OF 11", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Earnings Dashboard Explained", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        val earningsMetrics = listOf(
            Triple("💸 Estimated Earnings", "₹2,450", "Initial earnings from recent orders placed via your links."),
            Triple("⏳ Pending Earnings", "₹1,100", "Awaiting return window completion (usually 7 days)."),
            Triple("✅ Confirmed Earnings", "₹1,350", "Verified earnings ready for automated payout."),
            Triple("🏦 Withdrawable Balance", "₹1,350", "Transfer directly to your registered bank account!")
        )

        earningsMetrics.forEach { (title, sampleAmount, desc) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.75f))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(sampleAmount, fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                }
            }
        }
    }
}

/** STEP 6: Analytics Metrics */
@Composable
private fun Step6AnalyticsMetricsView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 6 OF 11", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Analytics & Traffic Metrics", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        val metrics = listOf(
            "👀 Views" to "Total people who saw your shared product link.",
            "🖱️ Clicks" to "Total users who clicked on your link to open Meesho.",
            "📦 Orders" to "Number of completed purchases made through your link.",
            "📈 Conversion Rate" to "(Orders ÷ Clicks) × 100 — higher means better strategy!",
            "💰 Commission %" to "The profit rate earned per successful order."
        )

        metrics.forEach { (metric, desc) ->
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
                    Text(metric, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(desc, fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** STEP 7: Rewards & Challenges */
@Composable
private fun Step7RewardsChallengesView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 7 OF 11", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Rewards & Bonus Programs", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

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
                Text("🏆 Creator Challenges:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Text("• Weekly milestone missions (e.g., Get 50 orders = ₹500 extra bonus).", fontSize = 12.sp, color = Color.White)

                Spacer(modifier = Modifier.height(10.dp))

                Text("🎁 Festive Bonus Programs:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Text("• Special commission boosts during major sales (Diwali, Maha Indian Saving Sale).", fontSize = 12.sp, color = Color.White)

                Spacer(modifier = Modifier.height(10.dp))

                Text("ℹ️ Availability Disclosure:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF2A7A))
                Text("Bonus programs vary based on account tier and current promotions.", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
            }
        }
    }
}

/** STEP 8: Profile Checklist */
@Composable
private fun Step8ProfileChecklistView(
    checklist: Map<String, Boolean>,
    onToggleItem: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 8 OF 11", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Creator Profile Checklist", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Verify all 5 profile requirements", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(16.dp))

        checklist.forEach { (item, isChecked) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isChecked) Color(0x33FF2A7A) else Color(0x11FFFFFF))
                    .border(1.dp, if (isChecked) Color(0xFFFF2A7A) else Color(0x33FFFFFF), RoundedCornerShape(16.dp))
                    .clickable { onToggleItem(item) }
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
                    Text(if (isChecked) "Verified" else "Pending", fontSize = 11.sp, color = if (isChecked) Color(0xFFFFD700) else Color.White.copy(alpha = 0.5f))
                }
            }
        }
    }
}

/** STEP 9: 8-10 Practice Questions */
@Composable
private fun Step9InteractivePracticeView(
    questionIndex: Int,
    selectedIndex: Int,
    feedback: String?,
    score: Int,
    onOptionSelected: (Int, Boolean) -> Unit,
    onNextQuestion: () -> Unit
) {
    val questions = listOf(
        QuestionData(
            q = "1. Meesho Creator Dashboard me earnings track karne ke liye kis section me jaoge?",
            options = listOf("Home Tab", "Earnings Tab", "Profile Settings", "Wishlist"),
            correctIdx = 1
        ),
        QuestionData(
            q = "2. High sales conversion pane ke liye kis tarah ke products select karne chahiye?",
            options = listOf("1.0 Rating items", "Zero reviews items", "4.0+ Rating & customer photo reviews", "Random items without checking"),
            correctIdx = 2
        ),
        QuestionData(
            q = "3. Link Generator se creator link banane ka sahi order kya hai?",
            options = listOf("Share -> Copy -> Open Product", "Open Product -> Share -> Generate Link -> Copy", "Copy -> Delete App -> Paste", "Directly buy product"),
            correctIdx = 1
        ),
        QuestionData(
            q = "4. Analytics me 'Conversion Rate' ka kya matlab hai?",
            options = listOf("Total link clicks", "Percentage of clicks that converted into orders", "Refunded money", "App download speed"),
            correctIdx = 1
        ),
        QuestionData(
            q = "5. Confirmed Earnings kis stage ke baad bank account me withdraw ho sakti hain?",
            options = listOf("Instantly upon click", "After return window completes (~7 days)", "Never", "After 1 year"),
            correctIdx = 1
        ),
        QuestionData(
            q = "6. Meesho Creator option sabse pehle kahan milta hai?",
            options = listOf("Play store reviews me", "Meesho App ke 'Account' tab me", "Phone Settings me", "WhatsApp status me"),
            correctIdx = 1
        ),
        QuestionData(
            q = "7. Kis galti se product return rate increase ho sakta hai?",
            options = listOf("High quality items select karna", "Low rating & wrong size chart products share karna", "Daily analytics check karna", "Fast shipping items select karna"),
            correctIdx = 1
        ),
        QuestionData(
            q = "8. Daily extra cash bonus claim karne ke liye kahan check karna chahiye?",
            options = listOf("Rewards & Challenges Section", "System Battery Settings", "Phone Gallery", "Calculator"),
            correctIdx = 0
        )
    )

    val currentQ = questions.getOrElse(questionIndex) { questions[0] }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("PRACTICE QUESTION ${questionIndex + 1} OF 8", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dashboard Practice Quiz", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text("Score: $score XP", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Text(currentQ.q, fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White, lineHeight = 20.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        currentQ.options.forEachIndexed { optIdx, optText ->
            val isSelected = selectedIndex == optIdx
            val isCorrect = optIdx == currentQ.correctIdx

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isSelected) {
                            if (isCorrect) Color(0x444CAF50) else Color(0x44FF5252)
                        } else Color(0x11FFFFFF)
                    )
                    .border(
                        1.dp,
                        if (isSelected) {
                            if (isCorrect) Color(0xFF4CAF50) else Color(0xFFFF5252)
                        } else Color(0x33FFFFFF),
                        RoundedCornerShape(14.dp)
                    )
                    .clickable { onOptionSelected(optIdx, isCorrect) }
                    .padding(14.dp)
            ) {
                Text(optText, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.White)
            }
        }

        if (feedback != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(feedback, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onNextQuestion,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (questionIndex < 7) "Next Question →" else "Finish Practice →", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

private data class QuestionData(
    val q: String,
    val options: List<String>,
    val correctIdx: Int
)

/** STEP 10: Common Beginner Mistakes */
@Composable
private fun Step10CommonMistakesView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 10 OF 11", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("5 Common Beginner Mistakes", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        val mistakes = listOf(
            "1. Searching Random Products" to "Promoting items without checking category demand or reviews.",
            "2. Ignoring Analytics" to "Not checking link click rates and conversion data weekly.",
            "3. Not Checking Commission" to "Sharing items with 1% commission when 10-15% items exist.",
            "4. Using Poor Quality Images" to "Posting blurry images instead of clear reels or high-res photos.",
            "5. Posting Without Strategy" to "Spamming links randomly instead of target audience recommendations."
        )

        mistakes.forEach { (title, desc) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF5252))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(desc, fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** STEP 11: Quick Revision Summary Cards */
@Composable
private fun Step11QuickRevisionView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("STEP 11 OF 11", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Quick Revision Summary", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        val summarySteps = listOf(
            "Dashboard Tour" to "6 Core tabs: Home, Products, Earnings, Analytics, Rewards, Profile",
            "Products Pick" to "Select high rating (4.0+) & 10%+ commission products",
            "Link Generator" to "Product -> Share -> Copy Creator Link",
            "Analytics Check" to "Track Clicks & Conversion Rate weekly",
            "Earnings Payout" to "Confirmed balance transfers directly to bank"
        )

        summarySteps.forEachIndexed { idx, (title, desc) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD700)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${idx + 1}", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
        }
    }
}

/** STEP 12: Mission & Achievement Badge (+150 XP) */
@Composable
private fun Step12AchievementView(
    score: Int,
    onFinishLevel3: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color(0x44FF2A7A))
                .border(2.dp, Color(0xFFFFD700), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🏆", fontSize = 48.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("ACHIEVEMENT UNLOCKED!", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        Spacer(modifier = Modifier.height(4.dp))
        Text("Dashboard Expert Badge", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x33FF2A7A))
                .border(1.dp, Color(0xFFFF2A7A), RoundedCornerShape(16.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text("⭐ +150 XP REWARD UNLOCKED", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎯 Today's Mission Status:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                Text("Explore Every Dashboard Section", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Estimated Time: 12 Minutes • Completed in Record Time!", fontSize = 11.5.sp, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onFinishLevel3,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Complete Level 3 & Return to Academy →", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
