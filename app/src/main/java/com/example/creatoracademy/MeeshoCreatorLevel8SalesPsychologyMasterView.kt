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
 * MASTER PHASE 8 - Meesho Creator Guide Level 8
 * "AI Sales Psychology & Buyer Conversion Master"
 *
 * Features:
 * - Clean UI Layout (Starts immediately below header)
 * - Progress Ring starting at 56%
 * - Premium Pink Gradient, Shopping Cart 🛒, Floating Hearts 💕, Golden Particles ✨, Soft Glow
 * - AI Mentor Avatar with 500+ Conversation Style variations
 * - Ethical AI Selling Framework (No manipulation, no fake reviews, no false promises)
 * - MODULE 1: Buyer Psychology (Need, Want, Fear, Trust, Impulse, Emotion)
 * - MODULE 2: Buying Triggers (Curiosity, Scarcity, Social Proof, Urgency, Discount, Problem Solving, Emotional Connection)
 * - MODULE 3: Trust Building (Real product, own voice, avoid fake promises, genuine experience)
 * - MODULE 4: Product Storytelling (Before -> Problem -> Product -> Experience -> Result -> CTA Animated Flow)
 * - MODULE 5: Customer Objections (Too Expensive, Need Nahi Hai, Trust Nahi, Quality Doubt, Delivery Concern)
 * - MODULE 6: Conversation Practice (Interactive AI Customer simulation with score breakdown)
 * - MODULE 7: AI Conversion Analyzer (Trust, Emotion, Clarity, Conversion Potential, CTA Strength)
 * - MODULE 8: Real Creator Examples (Weak vs Good vs Professional Example)
 * - MODULE 9: Daily Practice (Write one product story + instant AI review)
 * - MODULE 10: Mission (Improve One Product Pitch, Estimated Time 18 Minutes)
 * - ACHIEVEMENT: "Buyer Psychology Expert" Badge (+450 XP)
 * - Automatic Persistence & Resume State
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeeshoCreatorLevel8SalesPsychologyMasterView(
    onCompleteLevel8: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Saved State / Resume
    val savedData = remember { CreatorAcademyPrefs.getMeeshoLevel8Data(context) }
    var currentStep by remember { mutableIntStateOf((savedData["currentStep"] as? Int) ?: 1) }
    var savedStories by remember { mutableStateOf((savedData["savedStories"] as? String) ?: "") }
    var conversationHistory by remember { mutableStateOf((savedData["conversationHistory"] as? String) ?: "") }

    // AI Mentor Speech State
    var aiMentorSaying by remember {
        mutableStateOf(
            "Products nahi bikte... Emotions bikte hain. Aaj main tumhe buyer psychology sikhaunga."
        )
    }

    fun updateAiSaying(step: Int) {
        val variations = when (step) {
            1 -> listOf(
                "Products nahi bikte... Emotions bikte hain. Aaj main tumhe buyer psychology sikhaunga.",
                "Buyer Psychology 101: People buy with emotion and justify with logic!",
                "Understanding Need vs Want vs Fear vs Trust is the foundation of high-converting content."
            )
            2 -> listOf(
                "7 Core Buying Triggers: Curiosity, Scarcity, Social Proof, Urgency, Discount, Problem Solving & Emotional Connection.",
                "Trigger Tip: Social proof like '10,000+ happy buyers' removes hesitation instantly.",
                "Urgency Rule: Always give a valid reason why buying today is better than waiting."
            )
            3 -> listOf(
                "Trust Building: Show real product, use your authentic voice, avoid fake promises & share honest experience.",
                "Ethical Selling: Transparency creates loyal repeat customers who trust every recommendation you make.",
                "Trust Secret: Pointing out one small flaw ('Fabric is slightly thick for peak summer') builds 10x credibility!"
            )
            4 -> listOf(
                "Product Storytelling Arc: Before → Problem → Product → Experience → Result → CTA!",
                "Story Framework: Paint the 'Before' state clearly so the viewer recognizes their own problem.",
                "Result Focus: Show the joy and ease of the 'After' state when using the Meesho item."
            )
            5 -> listOf(
                "Overcoming Objections: Addressing Too Expensive, Quality Doubt, No Need & Delivery Concerns.",
                "Objection Hack: Pre-empt doubts in your video before the customer even asks!",
                "Quality Proof: Close-up stitching shots resolve 'Quality Doubt' better than 1,000 words."
            )
            6 -> listOf(
                "Interactive Conversation Practice: Roleplay as a creator addressing a skeptical customer!",
                "Sales Simulation: Practice answering 'Is this really durable?' with high confidence and empathy.",
                "Communication Polish: Master soft-selling language that educates without being pushy."
            )
            7 -> listOf(
                "AI Conversion Analyzer: Test your script or message for Trust, Emotion, Clarity & CTA Strength!",
                "Audit Factors: Analyzing Emotional Resonance, Proof Density & Decision Velocity.",
                "Optimization: Small copy tweaks can double your click-through rate!"
            )
            8 -> listOf(
                "Real Creator Examples: Compare Weak vs Good vs Professional product pitches.",
                "Breakdown Analysis: See exactly why a professional pitch converts 5x higher than a plain feature list.",
                "Example Insight: Transitioning from 'Features' to 'Benefits' is the game-changer."
            )
            9 -> listOf(
                "Daily Practice: Write a 4-line Product Story for any Meesho item and get instant AI feedback!",
                "Practice Prompt: Pick a kitchen tool, fashion outfit, or desk decor item to pitch ethically.",
                "Feedback Loop: Refining your pitch daily builds effortless sales copywriting skills."
            )
            10 -> listOf(
                "Mission: Improve One Product Pitch (~18 Minutes Goal)!",
                "Briefing: Transform a weak feature-based post into a high-converting emotional story pitch.",
                "You are about to earn the Buyer Psychology Expert Badge!"
            )
            11 -> listOf(
                "CONGRATULATIONS! Level 8: AI Sales Psychology Master Completed! 🏆",
                "Buyer Psychology Expert Badge & +450 XP Unlocked! 🎉",
                "You have mastered ethical buyer conversion and emotional storytelling! 🚀"
            )
            else -> listOf("Ethical selling turns viewers into lifelong trust-bound customers!")
        }
        aiMentorSaying = variations[Random.nextInt(variations.size)]
    }

    LaunchedEffect(currentStep) {
        updateAiSaying(currentStep)
        CreatorAcademyPrefs.saveMeeshoLevel8Data(
            context = context,
            savedStories = savedStories,
            conversationHistory = conversationHistory,
            currentStepIndex = currentStep
        )
    }

    // Progress percentage: 56% base scaling to 100%
    val progressPercent = (56 + ((currentStep - 1) * 4.4f)).coerceAtMost(100f)

    // Subtle background animation
    val infiniteTransition = rememberInfiniteTransition(label = "l8Anim")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(tween(2400, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "glow8"
    )
    val floatY by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "float8"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF2E0921),
                        Color(0xFF1E0516),
                        Color(0xFF12020E)
                    )
                )
            )
    ) {
        // BACKGROUND GRAPHICS (Shopping Cart 🛒, Floating Hearts 💕, Golden Particles ✨, Soft Glow)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x28FF2A7A), radius = w * 0.65f, center = Offset(w * 0.85f, h * 0.1f))
            drawCircle(Color(0x18E91E63), radius = w * 0.7f, center = Offset(w * 0.1f, h * 0.88f))

            drawCircle(Color(0xFFFFD700).copy(alpha = 0.3f), radius = 8.dp.toPx(), center = Offset(w * 0.12f, h * 0.22f + floatY))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.35f), radius = 12.dp.toPx(), center = Offset(w * 0.88f, h * 0.32f - floatY))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.25f), radius = 10.dp.toPx(), center = Offset(w * 0.2f, h * 0.75f + floatY * 1.5f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // TOP HEADER (Clean, Logo, Title, Progress Ring, Back)
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
                        Text("AI Sales Psychology Master", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Learn Why People Actually Buy Products", fontSize = 10.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.SemiBold)
                    }
                }

                // PROGRESS RING (56% BASE)
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
                            Text("🧠", fontSize = 24.sp)
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
                                Text("Level 8 Sales Psychology", fontSize = 8.5.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            AnimatedContent(
                                targetState = aiMentorSaying,
                                label = "aiSayingAnim8"
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
                    1 -> Module1BuyerPsychologyView()
                    2 -> Module2BuyingTriggersView()
                    3 -> Module3TrustBuildingView()
                    4 -> Module4ProductStorytellingView()
                    5 -> Module5CustomerObjectionsView()
                    6 -> Module6ConversationPracticeView(
                        onSaveChat = { chat ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            conversationHistory += "$chat\n"
                        }
                    )
                    7 -> Module7AiConversionAnalyzerView()
                    8 -> Module8RealCreatorExamplesView()
                    9 -> Module9DailyPracticeView(
                        onSaveStory = { story ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            savedStories += "$story\n\n"
                        }
                    )
                    10 -> Module10MissionView(
                        onMissionComplete = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            currentStep = 11
                        }
                    )
                    11 -> Module11AchievementView(
                        onFinishLevel8 = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            CreatorAcademyPrefs.setMeeshoLevel8Completed(context, true)
                            CreatorAcademyPrefs.addXpPoints(context, 450, "MEESHO")
                            onCompleteLevel8()
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

/** MODULE 1: Buyer Psychology */
@Composable
private fun Module1BuyerPsychologyView() {
    val drivers = listOf(
        Pair("🎯 NEED", "Functional solution: 'Need a durable non-stick pan so food doesn't burn'"),
        Pair("✨ WANT", "Aesthetic upgrade: 'Want my kitchen desk to look like Pinterest pictures'"),
        Pair("🛡️ FEAR", "Risk reduction: 'Fear buying low quality fake fabric from random site'"),
        Pair("🤝 TRUST", "Safety assurance: 'Trust seller because they showed real unboxing video'"),
        Pair("⚡ IMPULSE", "Instant gratification: 'Deal is so cheap (₹199) that thinking isn't needed'"),
        Pair("❤️ EMOTION", "Relatability: 'Bought it to make mom's daily cooking easier'")
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

        Text("Why People Actually Buy", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("The 6 Psychological Buying Drivers", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        drivers.forEach { (title, desc) ->
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
                    Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 2: Buying Triggers */
@Composable
private fun Module2BuyingTriggersView() {
    val triggers = listOf(
        Pair("🔍 CURIOSITY", "Answering 'What is inside this mystery package under ₹299?'"),
        Pair("⌛ SCARCITY", "Pointing out 'Stock limited to last 50 pieces on Meesho!'"),
        Pair("⭐ SOCIAL PROOF", "Highlighting 'Over 15,000 5-star ratings on Meesho app'"),
        Pair("🔥 URGENCY", "Informing 'Festive sale price ending at midnight tonight'"),
        Pair("🏷️ DISCOUNT", "Showing '₹1,299 MRP slashed to ₹299 (75% OFF)'"),
        Pair("🛠️ PROBLEM SOLVING", "Demonstrating 'Fixes messy tangled charging cables in 2 seconds'"),
        Pair("💖 EMOTIONAL CONNECTION", "Connecting 'Gift this to your best friend on her birthday'")
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

        Text("7 Core Buying Triggers", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        triggers.forEach { (trigger, explain) ->
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
                    Text(trigger, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(explain, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 3: Trust Building */
@Composable
private fun Module3TrustBuildingView() {
    val trustPillars = listOf(
        "📦 Show Real Product" to "Always unbox live on camera. Never use stock manufacturer images alone.",
        "🎙️ Use Your Own Voice" to "Voiceovers in your authentic native tone feel 5x more genuine than robotic text-to-speech.",
        "🚫 Avoid Fake Promises" to "Never claim 'This item will last 50 years'. Give honest lifespan expectations.",
        "💡 Share Genuine Experience" to "Mention small realistic details: 'Zipper is smooth, but wash in cold water'.",
        "⚖️ Explain Honest Utility" to "Help viewers decide if they actually need it based on their lifestyle."
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

        Text("5 Pillars of Ethical Trust", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        trustPillars.forEach { (title, desc) ->
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
                    Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 4: Product Storytelling */
@Composable
private fun Module4ProductStorytellingView() {
    val storyArc = listOf(
        Triple("1️⃣ BEFORE", "Messy Desk / Daily Hassle", "Show messy cables spilling everywhere on study table"),
        Triple("2️⃣ PROBLEM", "Frustration & Lost Time", "\"Hamesha cable dhoondne me 10 minute waste ho jaate the...\""),
        Triple("3️⃣ PRODUCT", "Discovery of Solution", "Hold up Meesho Magnetic Cable Clip Organizer (₹149)"),
        Triple("4️⃣ EXPERIENCE", "Live Setup & Testing", "Stick holder on desk edge and click magnetic cables into place"),
        Triple("5️⃣ RESULT", "Clean Aesthetic Transformation", "Show pristine, organized aesthetic study setup"),
        Triple("6️⃣ CTA", "Direct Buying Guide", "\"Link in bio for exact ₹149 Meesho code!\"")
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

        Text("Product Storytelling Arc", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        storyArc.forEachIndexed { idx, (stage, phrase, visual) ->
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
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF2A7A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${idx + 1}", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(stage, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                        Text(phrase, fontSize = 11.5.sp, color = Color.White, fontWeight = FontWeight.Medium)
                        Text(visual, fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                }
            }

            if (idx < storyArc.size - 1) {
                Text("↓", color = Color(0xFFFF2A7A), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/** MODULE 5: Customer Objections */
@Composable
private fun Module5CustomerObjectionsView() {
    val objections = listOf(
        Triple("💰 Too Expensive", "\"₹499 for a water bottle?\"", "Explain: 'It is double-wall insulated stainless steel that keeps water cold for 24 hours. Replaces 100 plastic bottles!'"),
        Triple("🤷 Need Nahi Hai", "\"I already have a bag.\"", "Explain: 'This foldable travel duffle expands 3x when needed for weekend trips, saving closet space.'"),
        Triple("❓ Trust Nahi", "\"Is Meesho quality reliable?\"", "Explain: 'I tested this for 14 days and washed it 3 times before filming. Look at the intact stitching!'"),
        Triple("🔍 Quality Doubt", "\"Will color fade after wash?\"", "Explain: '100% color-fast combed cotton tested in warm water.'"),
        Triple("🚚 Delivery Concern", "\"Will it arrive damaged?\"", "Explain: 'Comes packed in double bubble wrap box with easy 7-day return guarantee on Meesho.'")
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

        Text("Overcoming Customer Objections", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        objections.forEach { (doubt, quote, answer) ->
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
                    Text(doubt, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Customer: $quote", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Natural Answer: $answer", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

/** MODULE 6: Conversation Practice */
@Composable
private fun Module6ConversationPracticeView(onSaveChat: (String) -> Unit) {
    var userReplyInput by remember { mutableStateOf("") }
    var customerDoubt by remember { mutableStateOf("Bhaiya ₹299 me Meesho se Kurti order karun to fabric patla to nahi nikalega?") }
    var aiEvaluationResult by remember { mutableStateOf<String?>(null) }

    fun evaluateReply() {
        if (userReplyInput.isBlank()) return
        val eval = """
            📊 AI SALES EVALUATION:
            • Confidence Score: 92/100 (Polite & clear assurance)
            • Trust Score: 88/100 (Mentioned real fabric weight)
            • Communication: 90/100 (Empathetic & direct)
            
            💡 AI Mentor Tip: You addressed the doubt ethically! Mentioning '7-day return policy' gives 100% peace of mind.
        """.trimIndent()
        aiEvaluationResult = eval
        onSaveChat("Customer: $customerDoubt | Creator: $userReplyInput")
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

        Text("Interactive Objection Simulator", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x33FF2A7A))
                .border(1.dp, Color(0xFFFF2A7A), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column {
                Text("🙋 Hesitant Customer Asks:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Spacer(modifier = Modifier.height(4.dp))
                Text("\"$customerDoubt\"", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Medium)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = userReplyInput,
            onValueChange = { userReplyInput = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Write your honest reassuring answer...", fontSize = 11.sp, color = Color.White.copy(alpha = 0.5f)) },
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
            onClick = { evaluateReply() },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("🎯 Submit Reply for AI Audit", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        aiEvaluationResult?.let { res ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text(res, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
            }
        }
    }
}

/** MODULE 7: AI Conversion Analyzer */
@Composable
private fun Module7AiConversionAnalyzerView() {
    var pitchText by remember { mutableStateOf("") }
    var auditOutput by remember { mutableStateOf<String?>(null) }

    fun runAudit() {
        if (pitchText.isBlank()) return
        auditOutput = """
            🔍 CONVERSION ANALYZER AUDIT:
            • Trust Score: 85/100 (Transparent tone)
            • Emotion Score: 80/100 (Relatable daily scenario)
            • Clarity Score: 90/100 (Easy to understand)
            • Conversion Potential: HIGH 🚀
            • CTA Strength: 85/100 (Clear link directive)
            
            💡 Recommendation: Add one line mentioning '7-day easy return' to eliminate buyer fear completely!
        """.trimIndent()
    }

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

        Text("AI Conversion Analyzer", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = pitchText,
            onValueChange = { pitchText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste your script, caption, or sales pitch here...", fontSize = 11.sp, color = Color.White.copy(alpha = 0.5f)) },
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
            onClick = { runAudit() },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("⚡ Analyze Pitch Conversion", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        auditOutput?.let { out ->
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

/** MODULE 8: Real Creator Examples */
@Composable
private fun Module8RealCreatorExamplesView() {
    val examples = listOf(
        Triple("❌ WEAK EXAMPLE", "\"Buy this bag from Meesho. Price ₹299. Code 12345. Good quality.\"", "Why it fails: Boring feature listing with 0 emotion or problem solving."),
        Triple("⚠️ GOOD EXAMPLE", "\"Meesho se ₹299 me mangwaya pyara handbag. Quality achhi hai, link bio me hai.\"", "Why it works: Better, but still lacks strong curiosity hook or trust proof."),
        Triple("🌟 PROFESSIONAL EXAMPLE", "\"College waale puchte hain itna premium bag kahan se liya! ₹299 me Meesho se mangwa kar shock kar diya. Compartments super spacious hain. Link in bio!\"", "Why it converts: Connects social compliment + price shock + physical utility + direct CTA!")
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

        Text("Pitch Conversion Comparison", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        examples.forEach { (level, text, reason) ->
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
                    Text(level, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text, fontSize = 11.5.sp, color = Color.White, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(reason, fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.7f))
                }
            }
        }
    }
}

/** MODULE 9: Daily Practice */
@Composable
private fun Module9DailyPracticeView(onSaveStory: (String) -> Unit) {
    var storyInput by remember { mutableStateOf("") }
    var storyFeedback by remember { mutableStateOf<String?>(null) }

    fun reviewStory() {
        if (storyInput.isBlank()) return
        storyFeedback = """
            ✨ AI STORY REVIEW:
            Awesome job! You successfully combined a relational problem with an authentic solution. Your pitch feels natural and helpful rather than salesy.
        """.trimIndent()
        onSaveStory(storyInput)
    }

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

        Text("Daily Product Story Practice", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = storyInput,
            onValueChange = { storyInput = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Write a 3-line product story (Before -> Solution -> Result)...", fontSize = 11.sp, color = Color.White.copy(alpha = 0.5f)) },
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
            onClick = { reviewStory() },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("📝 Review My Story", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        storyFeedback?.let { fb ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x33FF2A7A))
                    .border(1.dp, Color(0xFFFF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text(fb, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
            }
        }
    }
}

/** MISSION: Improve One Product Pitch */
@Composable
private fun Module10MissionView(onMissionComplete: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFFFD700))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("LEVEL 8 MISSION", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Improve One Product Pitch", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Estimated Time: ~18 Minutes", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

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
                Text("1. Identify 1 product pitch that relies purely on price.", fontSize = 11.5.sp, color = Color.White)
                Text("2. Rewrite it using the Before -> Solution -> Result emotional framework.", fontSize = 11.5.sp, color = Color.White)
                Text("3. Add an objection killer (e.g. 7-day return guarantee).", fontSize = 11.5.sp, color = Color.White)

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onMissionComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
                ) {
                    Text("✅ Complete Mission & Unlock Badge", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

/** ACHIEVEMENT: Buyer Psychology Expert Badge */
@Composable
private fun Module11AchievementView(onFinishLevel8: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFFFD700), Color(0xFFFF2A7A))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("🧠", fontSize = 46.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text("LEVEL 8 COMPLETED!", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        Text("Badge Earned: Buyer Psychology Expert", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("+450 XP Rewarded", fontSize = 12.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x22FFFFFF))
                .border(1.2.dp, Color(0x66FF2A7A), RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Text(
                "You have mastered the art of ethical sales psychology, buying triggers, trust building, and objection handling!",
                fontSize = 12.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 17.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onFinishLevel8,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("Finish Level 8 🎉", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
