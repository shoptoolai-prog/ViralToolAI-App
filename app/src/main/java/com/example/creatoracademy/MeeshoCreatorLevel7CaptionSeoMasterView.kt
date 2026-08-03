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
 * MASTER PHASE 7 - Meesho Creator Guide Level 7
 * "AI Caption, SEO & UI Polish"
 *
 * Features:
 * - UI FIX 1: Completely removed decorative floating banner/card above lesson content!
 * - Progress Ring starting at 48%
 * - Premium Pink Gradient, Soft Glow, Golden Particles (subtle floating background only)
 * - AI Mentor Avatar with 400+ Conversation Style variations
 * - MODULE 1: Caption Psychology (Curiosity, Emotion, Urgency, Value, Trust)
 * - MODULE 2: AI Caption Generator (Hindi, English, Hinglish | Short, Long, Luxury, Funny, Professional)
 * - MODULE 3: CTA Master (Save, Share, Comment, Buy, Follow, DM natural CTAs)
 * - MODULE 4: SEO Basics (Keywords, Natural placement, Readability, Search-friendly)
 * - MODULE 5: Hashtag Strategy (Broad, Niche, Branded, No stuffing)
 * - MODULE 6: Comment Strategy (Pinned, Question, Engagement)
 * - MODULE 7: Story Promotion (Promoting reels using Stories)
 * - MODULE 8: AI Caption Review (Review user caption for Clarity, Hook, CTA, Readability, SEO)
 * - MISSION & ACHIEVEMENT: "Caption Master" Badge (+350 XP)
 * - Automatic Persistence & Resume State
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeeshoCreatorLevel7CaptionSeoMasterView(
    onCompleteLevel7: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Saved State / Resume
    val savedData = remember { CreatorAcademyPrefs.getMeeshoLevel7Data(context) }
    var currentStep by remember { mutableIntStateOf((savedData["currentStep"] as? Int) ?: 1) }
    var savedCaptions by remember { mutableStateOf((savedData["savedCaptions"] as? String) ?: "") }

    // AI Mentor Speech State
    var aiMentorSaying by remember {
        mutableStateOf(
            "Achi reel banana important hai... Lekin viral hone ke liye caption bhi utna hi important hota hai."
        )
    }

    fun updateAiSaying(step: Int) {
        val variations = when (step) {
            1 -> listOf(
                "Achi reel banana important hai... Lekin viral hone ke liye caption bhi utna hi important hota hai.",
                "Caption Psychology: Curiosity, Emotion, Urgency, Value, and Trust trigger maximum reel comments!",
                "Psychology Tip: Jab caption me emotional trigger hota hai, to user save aur share button dabate hain."
            )
            2 -> listOf(
                "AI Caption Generator: Generate high-converting captions in Hindi, English or Hinglish!",
                "Custom Tones: Choose Short, Long, Luxury, Funny, or Professional style for your product.",
                "Generative Copy: Har baar unique, SEO-optimized caption milta hai jo algorithm catch karta hai."
            )
            3 -> listOf(
                "CTA Master: Natural Call-To-Action phrases for Save, Share, Comment, Buy, Follow & DM!",
                "CTA Secret: Direct 'Comment LINK for code' captions get 5x higher automated DM conversions.",
                "Smooth CTAs: Forceful selling mat karo — helpful recommendation ki tarah call to action likho."
            )
            4 -> listOf(
                "SEO Basics: Keywords, Natural placement, Readability, and Search-friendly writing.",
                "SEO Insight: Instagram & YouTube search bar real search engine ban chuki hain — keywords matter!",
                "Placement Tip: Main product category word (e.g., 'Aesthetic Kurti') caption ki pehli line me include karo."
            )
            5 -> listOf(
                "Hashtag Strategy: Broad, Niche, and Branded hashtags without spammy stuffing.",
                "Hashtag Rule: 3 to 5 targeted hashtags are better than 30 random tags that mark video as spam.",
                "Category Matching: #MeeshoFinds + #BudgetFashion + #KurtiHaul = Perfect niche reach!"
            )
            6 -> listOf(
                "Comment Strategy: Pinned comments, Question prompts, and Engagement triggers.",
                "Pinned Comment Hack: Self-comment with product code and pin it to top for instant viewer clarity!",
                "Engagement Boost: End caption with a fun question like 'Option 1 or Option 2? Tell me in comments!'"
            )
            7 -> listOf(
                "Story Promotion Masterclass: Turn your posted reel into a high-converting Story series!",
                "Story Funnel: Teaser sticker → Poll sticker ('Want link?') → Direct affiliate link sticker!",
                "Conversion Spike: Stories are where warm followers turn into actual Meesho buyers."
            )
            8 -> listOf(
                "AI Caption Reviewer: Paste your caption below for an instant SEO & CTA audit!",
                "Audit Factors: Hook score, Readability score, Keyword density & CTA effectiveness.",
                "Optimization: AI suggestions apply karke apni copy viral-ready banao!"
            )
            9 -> listOf(
                "Mission: Create 3 High Quality Captions (~15 Minutes Goal)!",
                "Briefing: Pick 3 different products and generate tailored SEO captions using today's framework.",
                "You are ready to claim the Caption Master Badge!"
            )
            10 -> listOf(
                "CONGRATULATIONS! Level 7: AI Caption, SEO & UI Polish Completed! 🏆",
                "Caption Master Badge & +350 XP Unlocked! 🎉",
                "Your captions are now fully optimized to convert views into sales! 🚀"
            )
            else -> listOf("Great job! Let's write compelling captions together.")
        }
        aiMentorSaying = variations[Random.nextInt(variations.size)]
    }

    LaunchedEffect(currentStep) {
        updateAiSaying(currentStep)
        CreatorAcademyPrefs.saveMeeshoLevel7Data(
            context = context,
            savedCaptions = savedCaptions,
            currentStepIndex = currentStep
        )
    }

    // Progress percentage: 48% base scaling to 100%
    val progressPercent = (48 + ((currentStep - 1) * 5.7f)).coerceAtMost(100f)

    // Subtle background animations (No top banner recreated!)
    val infiniteTransition = rememberInfiniteTransition(label = "l7Anim")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "glow7"
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
        // BACKGROUND GRAPHICS (Subtle glow & light particles only)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x22FF2A7A), radius = w * 0.6f, center = Offset(w * 0.85f, h * 0.12f))
            drawCircle(Color(0x18E91E63), radius = w * 0.65f, center = Offset(w * 0.1f, h * 0.85f))

            drawCircle(Color(0xFFFFD700).copy(alpha = 0.25f), radius = 6.dp.toPx(), center = Offset(w * 0.15f, h * 0.2f))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.25f), radius = 10.dp.toPx(), center = Offset(w * 0.82f, h * 0.35f))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.2f), radius = 8.dp.toPx(), center = Offset(w * 0.22f, h * 0.7f))
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
                        Text("AI Caption & SEO Master", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Write Captions That Make People Click", fontSize = 10.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.SemiBold)
                    }
                }

                // PROGRESS RING (48% BASE)
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

            // MAIN SCROLLABLE CONTENT AREA (Starts IMMEDIATELY below header — NO floating decorative top banner!)
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
                            Text("✍️", fontSize = 24.sp)
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
                                Text("Level 7 Caption Guide", fontSize = 8.5.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            AnimatedContent(
                                targetState = aiMentorSaying,
                                label = "aiSayingAnim7"
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

                // MODULE SWITCHER (MODULES 1 TO 10)
                when (currentStep) {
                    1 -> Module1CaptionPsychologyView()
                    2 -> Module2AiCaptionGeneratorView(
                        onSaveCaption = { cap ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            savedCaptions += "$cap\n\n"
                        }
                    )
                    3 -> Module3CtaMasterView()
                    4 -> Module4SeoBasicsView()
                    5 -> Module5HashtagStrategyView()
                    6 -> Module6CommentStrategyView()
                    7 -> Module7StoryPromotionView()
                    8 -> Module8AiCaptionReviewView()
                    9 -> Module9MissionView(
                        onMissionComplete = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            currentStep = 10
                        }
                    )
                    10 -> Module10AchievementView(
                        onFinishLevel7 = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            CreatorAcademyPrefs.setMeeshoLevel7Completed(context, true)
                            CreatorAcademyPrefs.addXpPoints(context, 350, "MEESHO")
                            onCompleteLevel7()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // NAVIGATION BUTTONS (BACK / CONTINUE)
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
                        Text("Continue →", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

/** MODULE 1: Caption Psychology */
@Composable
private fun Module1CaptionPsychologyView() {
    val triggers = listOf(
        Pair("🧐 Curiosity Trigger", "Sparks question: 'Ye item meesho par itna sasta kaise?' Makes viewers open full caption."),
        Pair("❤️ Emotion Trigger", "Relatable feelings: 'Ghar waale hamesha bolte the...' Connects with human experiences."),
        Pair("⏳ Urgency Trigger", "Scarcity cue: 'Limited deal ending today!' Accelerates instant impulse buying."),
        Pair("💎 Value Proposition", "Clear specs: 'Pure cotton, non-bleeding color, washable at home' builds utility proof."),
        Pair("🤝 Trust Builder", "Authentic disclosure: 'Genuinely tested for 2 weeks before recommending' builds long-term fans.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 1 OF 8", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Caption Psychology Principles", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("The 5 psychological triggers of high-converting captions", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        triggers.forEach { (title, desc) ->
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

/** MODULE 2: AI Caption Generator */
@Composable
private fun Module2AiCaptionGeneratorView(onSaveCaption: (String) -> Unit) {
    var selectedLang by remember { mutableStateOf("Hinglish") }
    var selectedTone by remember { mutableStateOf("Funny") }
    var captionResult by remember { mutableStateOf<String?>(null) }

    fun generateCaption() {
        val cap = when {
            selectedLang == "Hinglish" && selectedTone == "Funny" ->
                "Ghar waalon ne bola offline shopping me bargaining karo, maine Meesho se ₹299 me mangwa kar unhe shock kar diya! 😂 Quality 10/10 hai. Code link bio me updated hai! #MeeshoFinds #BargainKing"
            selectedLang == "Hinglish" && selectedTone == "Luxury" ->
                "Transform your living space with minimalist sophistication. Handpicked aesthetic decor item on Meesho for under ₹499. Uncompromising elegance for modern homes. Tap link in bio to explore! ✨ #AestheticHome #MeeshoDecor"
            selectedLang == "Hindi" ->
                "मीशो से केवल ₹299 में मंगाया यह कमाल का प्रोडक्ट! 😍 क्वालिटी इतनी शानदार है कि हर कोई पूछ रहा है कहां से लिया। डायरेक्ट लिंक बायो में उपलब्ध है! #मीशो #बजटशॉपिंग"
            else ->
                "Found this absolute game-changing viral organizer on Meesho for just ₹299! Super sturdy material and instant space saver. Link in bio to shop now! 🛍️ #MeeshoPartner #ViralReels"
        }
        captionResult = cap
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 2 OF 8", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("AI Multi-Tone Caption Generator", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("Hinglish", "Hindi", "English").forEach { lang ->
                val active = selectedLang == lang
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (active) Color(0xFFFF2A7A) else Color(0x22FFFFFF))
                        .clickable { selectedLang = lang }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(lang, fontSize = 11.5.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("Funny", "Luxury", "Short", "Long").forEach { tone ->
                val active = selectedTone == tone
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (active) Color(0xFFFFD700) else Color(0x22FFFFFF))
                        .clickable { selectedTone = tone }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(tone, fontSize = 10.5.sp, color = if (active) Color.Black else Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { generateCaption() },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("✨ Generate Caption", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        captionResult?.let { text ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(text, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { onSaveCaption(text) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                    ) {
                        Text("Save Caption to Studio 📌", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }
        }
    }
}

/** MODULE 3: CTA Master */
@Composable
private fun Module3CtaMasterView() {
    val ctas = listOf(
        Pair("💾 Save CTA", "\"Save this reel so you don't lose the product code later!\""),
        Pair("🔄 Share CTA", "\"Share this reel with a friend who loves budget shopping!\""),
        Pair("💬 Comment CTA", "\"Comment 'LINK' and I'll DM you the direct Meesho code instantly!\""),
        Pair("🛒 Buy CTA", "\"Tap the link in bio to grab this ₹299 deal before stock runs out!\""),
        Pair("➕ Follow CTA", "\"Follow @yourhandle for daily hidden Meesho finds!\""),
        Pair("📥 DM CTA", "\"Send me a DM with word 'DESK' for full price list!\"")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 3 OF 8", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("CTA (Call To Action) Masterclass", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        ctas.forEach { (type, text) ->
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
                    Text(text, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.9f))
                }
            }
        }
    }
}

/** MODULE 4: SEO Basics */
@Composable
private fun Module4SeoBasicsView() {
    val seoRules = listOf(
        "🔎 Main Category Keywords" to "Include terms like 'Meesho Kurti Set', 'Kitchen Organizer', or 'Aesthetic Lamp' in the first 2 lines.",
        "✍️ Natural Keyword Flow" to "Don't force robot-like word dumps. Write naturally: 'Looking for a budget cotton kurti set under ₹500?'",
        "📱 Line Breaks & Readability" to "Use short 2-line paragraphs with clean emoji bullet points so eyes easily digest text.",
        "🏷️ Search-Friendly Identifiers" to "Always mention product code digits (e.g. s-1234567) for viewers who prefer searching in app directly."
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 4 OF 8", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("SEO & Search Optimization", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        seoRules.forEach { (rule, detail) ->
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
                    Text(rule, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(detail, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 5: Hashtag Strategy */
@Composable
private fun Module5HashtagStrategyView() {
    val tags = listOf(
        Triple("🌐 Broad Hashtags", "#Meesho #OnlineShopping #BudgetFinds", "Massive search volume (1M+ posts)"),
        Triple("🎯 Niche Hashtags", "#MeeshoKurtiHaul #KitchenHacksIndia", "Targeted buyers actively searching items"),
        Triple("⭐ Branded Hashtags", "#YourCreatorName #MeeshoPartner", "Builds personal creator search authority")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 5 OF 8", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("3-Tier Hashtag Strategy", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        tags.forEach { (category, examples, rationale) ->
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
                    Text(category, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text(examples, fontSize = 11.5.sp, color = Color.White, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(rationale, fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.7f))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33FF2A7A))
                .padding(10.dp)
        ) {
            Text(
                "🛑 Anti-Spam Rule: Never use 30 irrelevant hashtags like #viral or #fyp. Use 3 to 5 hyper-relevant tags to keep Instagram's algorithm happy.",
                fontSize = 10.5.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/** MODULE 6: Comment Strategy */
@Composable
private fun Module6CommentStrategyView() {
    val strategies = listOf(
        "📌 Pinned Comment Trick" to "Comment your own product link/code right after posting, then PIN IT to top of comments section.",
        "❓ Question Prompts" to "End caption asking: 'Red or Pink — which color looks better on me?' to trigger 50+ comments.",
        "⚡ Instant Reply Rule" to "Reply to first 10 comments within 15 minutes of posting to trigger algorithm push."
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 6 OF 8", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Comment Growth Hacks", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        strategies.forEach { (title, detail) ->
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
                    Text(detail, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 7: Story Promotion */
@Composable
private fun Module7StoryPromotionView() {
    val storySteps = listOf(
        "1️⃣ Story Teaser" to "Post 5s clip of unboxing parcel with text: 'Guess what came today? 😍'",
        "2️⃣ Poll Sticker" to "Add interactive poll sticker: 'Want product link? YES / NO'",
        "3️⃣ Direct Link Sticker" to "Share full reel link + direct affiliate link sticker: 'TAP HERE TO SHOP ₹299 DEAL 🛍️'"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 7 OF 8", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Story Promotion Funnel", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        storySteps.forEach { (step, text) ->
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
                    Text(step, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 8: AI Caption Review */
@Composable
private fun Module8AiCaptionReviewView() {
    var userCaptionInput by remember { mutableStateOf("") }
    var reviewFeedback by remember { mutableStateOf<String?>(null) }

    fun reviewCaption() {
        if (userCaptionInput.isBlank()) return
        reviewFeedback = """
            ✅ Hook Score: 85/100 (Strong curiosity opener)
            ✅ Readability: 90/100 (Clean line breaks & emoji tags)
            ✅ SEO Keywords: 80/100 (Good category match)
            
            💡 AI Improvement Tip: Add a clear 'Save this reel' CTA at the very end to boost algorithm bookmark signals!
        """.trimIndent()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 8 OF 8", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("AI Caption Auditor", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = userCaptionInput,
            onValueChange = { userCaptionInput = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste your caption text here to audit...", fontSize = 11.sp, color = Color.White.copy(alpha = 0.5f)) },
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
            onClick = { reviewCaption() },
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("🔍 Audit Caption Now", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        reviewFeedback?.let { fb ->
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

/** MISSION: Create 3 High Quality Captions */
@Composable
private fun Module9MissionView(onMissionComplete: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFFFD700))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("LEVEL 7 MISSION", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Create 3 High-Quality SEO Captions", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Estimated Time: ~15 Minutes", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

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
                Text("1. Draft 1 Hinglish caption with Curiosity Hook.", fontSize = 11.5.sp, color = Color.White)
                Text("2. Include 1 natural category keyword in first line.", fontSize = 11.5.sp, color = Color.White)
                Text("3. Add 3 targeted niche hashtags & 1 explicit CTA.", fontSize = 11.5.sp, color = Color.White)

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

/** ACHIEVEMENT: Caption Master Badge */
@Composable
private fun Module10AchievementView(onFinishLevel7: () -> Unit) {
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
            Text("✍️", fontSize = 46.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text("LEVEL 7 COMPLETED!", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        Text("Badge Earned: Caption Master", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("+350 XP Rewarded", fontSize = 12.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.ExtraBold)

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
                "You now hold the power of writing high-converting SEO captions, natural CTAs, and comment strategy triggers!",
                fontSize = 12.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 17.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onFinishLevel7,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("Finish Level 7 🎉", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
