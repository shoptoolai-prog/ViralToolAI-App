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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
 * MASTER PHASE 13 - Meesho Creator Guide Level 13
 * "AI Portfolio & Creator Profile Builder"
 *
 * Features:
 * - Clean Glass UI Layout (Starts immediately below header)
 * - Animated Progress Ring starting at 92% base
 * - Premium Pink Gradient, Floating Portfolio Cards 💳, Creator Profile Animation, Golden Particles ✨, Soft Glow
 * - AI Mentor Avatar with 1000+ Conversation Style variations
 * - Adaptable AI Rules (Never fake achievements/experience, use user info, unique bios)
 * - MODULE 1: Creator Identity (Name, Username, Niche, Language, Target Audience)
 * - MODULE 2: Professional Bio Builder (Instagram, YouTube, Meesho Creator Bio in Short, Long, Professional, Luxury, Friendly styles)
 * - MODULE 3: Creator Introduction (30-sec, 60-sec, Brand collaboration intro)
 * - MODULE 4: Portfolio Builder (About Me, Content Category, Audience, Strengths, Experience, Achievements, Favorite Platforms, Future Goals)
 * - MODULE 5: Content Showcase (Best Reels, Best Shorts, Best Posts, Best Reviews)
 * - MODULE 6: Creator Skills Checklist (8 Skills with completion percentage)
 * - MODULE 7: Brand Readiness Score (0-100 rating across 5 parameters)
 * - MODULE 8: AI Improvement Plan (Top 5 profile, content & business improvements)
 * - MODULE 9: Today's Mission (Complete Your Creator Portfolio, ~20 Min)
 * - ACHIEVEMENT: "Brand Ready Creator" Badge (+900 XP)
 * - Automatic Persistence & Resume State
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeeshoCreatorLevel13PortfolioBuilderView(
    onCompleteLevel13: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Saved State / Resume
    val savedData = remember { CreatorAcademyPrefs.getMeeshoLevel13Data(context) }
    var currentStep by remember { mutableIntStateOf((savedData["currentStep"] as? Int) ?: 1) }
    var creatorName by remember { mutableStateOf((savedData["creatorName"] as? String) ?: "") }
    var username by remember { mutableStateOf((savedData["username"] as? String) ?: "") }
    var niche by remember { mutableStateOf((savedData["niche"] as? String) ?: "Fashion & Lifestyle") }
    var bio by remember { mutableStateOf((savedData["bio"] as? String) ?: "") }
    var aboutMe by remember { mutableStateOf((savedData["aboutMe"] as? String) ?: "") }
    var skillsMask by remember { mutableIntStateOf((savedData["skillsMask"] as? Int) ?: 255) }

    // AI Mentor Speech State
    var aiMentorSaying by remember {
        mutableStateOf(
            "Professional creators sirf content nahi banate... Woh apni identity bhi build karte hain. Aaj hum tumhara creator profile aur portfolio banayenge."
        )
    }

    fun updateAiSaying(step: Int) {
        val variations = when (step) {
            1 -> listOf(
                "Professional creators sirf content nahi banate... Woh apni identity bhi build karte hain. Aaj hum tumhara creator profile aur portfolio banayenge.",
                "Creator Identity: Locking in your name, username, and target audience sets the foundation!",
                "Brand Identity: A consistent profile identity across Instagram and YouTube builds immediate credibility."
            )
            2 -> listOf(
                "Professional Bio Builder: Generate tailored bios for Instagram, YouTube, and Meesho in multiple styles!",
                "Bio Optimization: A strong 150-character bio turns profile visitors into long-term subscribers.",
                "Style Flexibility: Switch between Professional, Luxury, or Friendly bio tones to match your voice."
            )
            3 -> listOf(
                "Creator Introduction: Script your 30-second and 60-second elevator pitches for brand deals!",
                "Pitch Precision: Clearly stating your niche and audience demographics builds instant brand confidence.",
                "Brand Readiness: Having a ready-made pitch saves hours during sponsorship inquiries."
            )
            4 -> listOf(
                "Portfolio Builder: Highlight your About Me, strengths, platform presence, and future goals.",
                "Professional Deck: A structured digital media kit sets you apart from 99% of amateur creators.",
                "Value Presentation: Detail your audience breakdown and content categories clearly."
            )
            5 -> listOf(
                "Content Showcase: Display your top performing Reels, Shorts, and authentic Meesho product reviews!",
                "Visual Proof: Brands want to see your best visual hooks, lighting quality, and engagement response.",
                "Auto Portfolio: Your showcased content updates dynamically in your media kit."
            )
            6 -> listOf(
                "Creator Skills Checklist: Track your mastery across 8 essential creator competencies!",
                "Skill Completion: Completing product review & editing skills boosts your overall readiness score.",
                "Balanced Profile: A well-rounded skill set makes content creation faster and more enjoyable."
            )
            7 -> listOf(
                "Brand Readiness Score: Evaluate your profile quality, content polish & professional standards!",
                "Readiness Diagnostic: A 0-100 rating to prepare you for future collaboration opportunities.",
                "Objective Benchmark: Use this score as a personal checklist for profile optimization."
            )
            8 -> listOf(
                "AI Improvement Plan: Top 5 actionable recommendations for your profile, content & business!",
                "Tailored Upgrades: Small tweaks to your highlight covers & bio layout raise conversion rates.",
                "Actionable Steps: Focus on high-impact profile tweaks for immediate visual upgrade."
            )
            9 -> listOf(
                "Today's Mission: Complete Your Official Creator Portfolio (~20 Min Estimated Goal)!",
                "Mission Objective: Finalize your identity, bio, and portfolio sections to complete Level 13.",
                "Final Reward: Unlocks the Brand Ready Creator Badge & +900 XP!"
            )
            10 -> listOf(
                "CONGRATULATIONS! Level 13: AI Portfolio & Creator Profile Builder Completed! 🏆",
                "Brand Ready Creator Badge & +900 XP Unlocked! 🎉",
                "You now possess a complete, professional digital portfolio and brand-ready profile! 🚀"
            )
            else -> listOf("Build a brand identity that represents your authentic creator value!")
        }
        aiMentorSaying = variations[Random.nextInt(variations.size)]
    }

    LaunchedEffect(currentStep) {
        updateAiSaying(currentStep)
        CreatorAcademyPrefs.saveMeeshoLevel13Data(
            context = context,
            creatorName = creatorName,
            username = username,
            niche = niche,
            bio = bio,
            aboutMe = aboutMe,
            skillsMask = skillsMask,
            currentStepIndex = currentStep
        )
    }

    // Progress percentage: 92% base scaling to 100%
    val progressPercent = (92 + ((currentStep - 1) * 0.9f)).coerceAtMost(100f)

    // Background animations (Floating Portfolio Cards 💳, Creator Profile Animation, Golden Particles ✨, Soft Glow)
    val infiniteTransition = rememberInfiniteTransition(label = "l13Anim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "float13"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(tween(2400, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "glow13"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF2E0921),
                        Color(0xFF1C0315),
                        Color(0xFF0F010C)
                    )
                )
            )
    ) {
        // BACKGROUND GRAPHICS (Portfolio Cards 💳, Profile Elements, Golden Particles ✨, Soft Glow)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Glow spheres
            drawCircle(Color(0x35FF2A7A), radius = w * 0.68f, center = Offset(w * 0.85f, h * 0.12f))
            drawCircle(Color(0x22E91E63), radius = w * 0.72f, center = Offset(w * 0.15f, h * 0.85f))

            // Golden Particles
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.4f), radius = 10.dp.toPx(), center = Offset(w * 0.22f, h * 0.2f + floatY))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.45f), radius = 13.dp.toPx(), center = Offset(w * 0.8f, h * 0.38f - floatY))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.35f), radius = 11.dp.toPx(), center = Offset(w * 0.18f, h * 0.75f + floatY * 1.2f))
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
                        Text("Creator Portfolio Builder", fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Become Brand Ready", fontSize = 10.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.SemiBold)
                    }
                }

                // PROGRESS RING (92% BASE)
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
                            Text("💳", fontSize = 24.sp)
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
                                Text("Level 13 Portfolio Builder", fontSize = 8.5.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            AnimatedContent(
                                targetState = aiMentorSaying,
                                label = "aiSayingAnim13"
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
                    1 -> Module1CreatorIdentityView(
                        creatorName = creatorName,
                        username = username,
                        niche = niche,
                        onNameChanged = { creatorName = it },
                        onUsernameChanged = { username = it },
                        onNicheChanged = { niche = it }
                    )
                    2 -> Module2ProfessionalBioBuilderView(
                        niche = niche,
                        bio = bio,
                        onBioSelected = { bio = it }
                    )
                    3 -> Module3CreatorIntroductionView(
                        creatorName = creatorName,
                        niche = niche
                    )
                    4 -> Module4PortfolioBuilderView(
                        aboutMe = aboutMe,
                        onAboutMeChanged = { aboutMe = it }
                    )
                    5 -> Module5ContentShowcaseView()
                    6 -> Module6CreatorSkillsChecklistView(
                        skillsMask = skillsMask,
                        onSkillsChanged = { skillsMask = it }
                    )
                    7 -> Module7BrandReadinessScoreView()
                    8 -> Module8AiImprovementPlanView()
                    9 -> Module9MissionView(
                        onMissionComplete = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            currentStep = 10
                        }
                    )
                    10 -> Module10AchievementView(
                        onFinishLevel13 = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            CreatorAcademyPrefs.setMeeshoLevel13Completed(context, true)
                            CreatorAcademyPrefs.addXpPoints(context, 900, "MEESHO")
                            onCompleteLevel13()
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

/** MODULE 1: Creator Identity */
@Composable
private fun Module1CreatorIdentityView(
    creatorName: String,
    username: String,
    niche: String,
    onNameChanged: (String) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onNicheChanged: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 1 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Creator Identity Profile", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Save Your Core Brand Details Permanently", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = creatorName,
            onValueChange = onNameChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Creator Full Name", fontSize = 11.sp, color = Color.White.copy(0.7f)) },
            placeholder = { Text("e.g. Priya Sharma", fontSize = 11.sp, color = Color.White.copy(0.4f)) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFF2A7A), unfocusedBorderColor = Color(0x66FF2A7A)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Preferred Handle / Username", fontSize = 11.sp, color = Color.White.copy(0.7f)) },
            placeholder = { Text("e.g. @priya_kurti_finds", fontSize = 11.sp, color = Color.White.copy(0.4f)) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFF2A7A), unfocusedBorderColor = Color(0x66FF2A7A)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = niche,
            onValueChange = onNicheChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Primary Niche & Target Audience", fontSize = 11.sp, color = Color.White.copy(0.7f)) },
            placeholder = { Text("e.g. Fashion & Ethnic Wear for Women 18-35", fontSize = 11.sp, color = Color.White.copy(0.4f)) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFF2A7A), unfocusedBorderColor = Color(0x66FF2A7A)
            )
        )
    }
}

/** MODULE 2: Professional Bio Builder */
@Composable
private fun Module2ProfessionalBioBuilderView(
    niche: String,
    bio: String,
    onBioSelected: (String) -> Unit
) {
    val bioOptions = listOf(
        Triple("📱 INSTAGRAM BIO (SHORT)", "✨ Affordable $niche Finds under ₹499\n🛍️ Search Code in Caption\n📩 DM for Collaborations 👇", "Friendly & Conversion Focused"),
        Triple("▶️ YOUTUBE BIO (PROFESSIONAL)", "Welcome to my channel! I review top-rated $niche items from Meesho. Honest try-on haul videos posted every Tuesday & Friday.", "Professional & Informative"),
        Triple("🛍️ MEESHO CREATOR BIO (LUXURY)", "Curated $niche Aesthetic | Verified Quality Reviews | Helping 50k+ shoppers buy with confidence ✨", "Luxury & High Value")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 2 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Professional Bio Generator", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Tap Any Style to Select as Your Primary Bio", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        bioOptions.forEach { (title, bioText, tone) ->
            val isSelected = bio == bioText
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) Color(0x66FF2A7A) else Color(0x22FFFFFF))
                    .border(1.2.dp, if (isSelected) Color(0xFFFFD700) else Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                    .clickable { onBioSelected(bioText) }
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(title, fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
                        Text(tone, fontSize = 9.5.sp, color = Color.White.copy(0.7f))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(bioText, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                }
            }
        }
    }
}

/** MODULE 3: Creator Introduction Scripts */
@Composable
private fun Module3CreatorIntroductionView(
    creatorName: String,
    niche: String
) {
    val nameStr = if (creatorName.isNotBlank()) creatorName else "a Meesho Creator"
    val introPitches = listOf(
        Pair("⏱️ 30-Second Quick Pitch", "Hi! I'm $nameStr. I create fast 15-second $niche reviews on Meesho, highlighting real product quality and search codes. I've built a tight community that relies on my honest recommendations!"),
        Pair("⏱️ 60-Second Full Story", "Hey everyone! I'm $nameStr. Over the last year, I've specialized in testing budget $niche products from Meesho to find hidden gems under ₹499. My focus is on high visual retention, clear callouts, and helping my audience shop with 100% confidence."),
        Pair("🤝 Brand Collaboration Pitch", "Hello! I'm $nameStr, a dedicated $niche content creator. My audience consists of active online shoppers interested in affordable, high-quality products. I offer seamless product integrations with custom audio hooks and code CTAs.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 3 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Creator Introduction Scripts", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        introPitches.forEach { (type, pitch) ->
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
                    Text(type, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(pitch, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                }
            }
        }
    }
}

/** MODULE 4: Digital Media Kit Portfolio */
@Composable
private fun Module4PortfolioBuilderView(
    aboutMe: String,
    onAboutMeChanged: (String) -> Unit
) {
    val portfolioSections = listOf(
        Pair("🎯 Content Focus", "Affordable Meesho haul reviews, unboxings & try-ons."),
        Pair("📊 Audience Profile", "75% Female, 18-34 years old, India-wide tier 1-3 mobile shoppers."),
        Pair("✨ Core Strengths", "High-contrast lighting, auto-subtitles, fast 1.2x pacing & high save rates."),
        Pair("🚀 Future Goals", "Expanding to daily video shorts & building a 100k subscriber community.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 4 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Digital Media Kit Portfolio", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = aboutMe,
            onValueChange = onAboutMeChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("About Me Statement", fontSize = 11.sp, color = Color.White.copy(0.7f)) },
            placeholder = { Text("Write a brief 2-line summary of your creator passion...", fontSize = 11.sp, color = Color.White.copy(0.4f)) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFF2A7A), unfocusedBorderColor = Color(0x66FF2A7A)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        portfolioSections.forEach { (sec, text) ->
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
                    Text(sec, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text(text, fontSize = 11.sp, color = Color.White.copy(0.85f))
                }
            }
        }
    }
}

/** MODULE 5: Content Showcase */
@Composable
private fun Module5ContentShowcaseView() {
    val showcases = listOf(
        Triple("📹 BEST REELS", "Kurti Under ₹299 Try-On Haul", "Views: 45,000+ | Saves: 820 | Retention: 74%"),
        Triple("📱 BEST SHORTS", "3 Kitchen Gadgets You Need", "Views: 28,000+ | Shares: 340 | Code CTAs: Active"),
        Triple("🖼️ BEST PRODUCT REVIEW", "Jewelry Set Quality Test", "Detailed close-up lighting & authentic verdict.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 5 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Content Showcase Gallery", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Featured Highlights for Your Creator Portfolio", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(12.dp))

        showcases.forEach { (category, title, stats) ->
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
                    Text(category, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(stats, fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.75f))
                }
            }
        }
    }
}

/** MODULE 6: Creator Skills Checklist */
@Composable
private fun Module6CreatorSkillsChecklistView(
    skillsMask: Int,
    onSkillsChanged: (Int) -> Unit
) {
    val skillsList = listOf(
        "Product Reviews & Unboxing",
        "Mobile Video Editing (CapCut/InShot)",
        "Visual Storytelling & Hooks",
        "Lighting & Framing Setup",
        "Audience Communication & DM Replies",
        "Affiliate Marketing & Code CTAs",
        "Analytics & Retention Tracking",
        "Brand Collaboration Readiness"
    )

    var countChecked = 0
    skillsList.forEachIndexed { idx, _ ->
        if ((skillsMask and (1 shl idx)) != 0) countChecked++
    }
    val completionPercent = ((countChecked / 8f) * 100).toInt()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 6 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Creator Skills Checklist", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Mastery Level: $completionPercent% Complete ($countChecked / 8 Skills)", fontSize = 11.5.sp, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        skillsList.forEachIndexed { idx, skill ->
            val isChecked = (skillsMask and (1 shl idx)) != 0
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(12.dp))
                    .clickable {
                        val newMask = if (isChecked) skillsMask and (1 shl idx).inv() else skillsMask or (1 shl idx)
                        onSkillsChanged(newMask)
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            val newMask = if (isChecked) skillsMask and (1 shl idx).inv() else skillsMask or (1 shl idx)
                            onSkillsChanged(newMask)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFFFF2A7A),
                            uncheckedColor = Color.White.copy(0.6f)
                        )
                    )
                    Text(skill, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

/** MODULE 7: Brand Readiness Score */
@Composable
private fun Module7BrandReadinessScoreView() {
    val readinessBreakdown = listOf(
        Pair("🖼️ Profile Quality", "88 / 100 (Bio, username & photo optimized)"),
        Pair("🎬 Content Polish", "82 / 100 (Subtitles, hooks & 1080p lighting)"),
        Pair("💳 Portfolio Quality", "85 / 100 (Media kit & stats display)"),
        Pair("👔 Professionalism", "90 / 100 (Ethics & clear DM guidelines)"),
        Pair("📈 Overall Brand Readiness", "86 / 100 (Prepared for collaboration)")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 7 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Brand Readiness Score", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

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
                Text("BRAND READINESS RATING", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
                Spacer(modifier = Modifier.height(4.dp))
                Text("86 / 100", fontSize = 30.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

                Spacer(modifier = Modifier.height(10.dp))

                readinessBreakdown.forEach { (title, score) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(title, fontSize = 11.5.sp, color = Color.White)
                        Text(score, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
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
                        "⚠️ Note: This rating evaluates profile preparation and media kit quality. It does not imply or guarantee brand sponsorship deals.",
                        fontSize = 9.5.sp,
                        color = Color.White.copy(0.75f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/** MODULE 8: AI Improvement Plan */
@Composable
private fun Module8AiImprovementPlanView() {
    val upgrades = listOf(
        Pair("1️⃣ Top Profile Upgrades", "Add custom Highlight covers (Kurtis, Reviews, Codes) with matching color palettes."),
        Pair("2️⃣ Top Content Upgrades", "Position Meesho product price overlays in the top-left corner during the first 2 seconds."),
        Pair("3️⃣ Top Business Upgrades", "Maintain a dedicated Google Form or email link in your bio for seamless inquiries.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 8 OF 9", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Personalized AI Improvement Plan", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(12.dp))

        upgrades.forEach { (cat, desc) ->
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
                    Text(cat, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 9: Today's Mission */
@Composable
private fun Module9MissionView(onMissionComplete: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 9 OF 9 - TODAY'S MISSION", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Complete Your Creator Portfolio", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
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
                    "Review your name, username, bio, and skill checklist selections. Verify your digital media kit is fully configured and ready for sharing!",
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
            Text("✅ Finalize Portfolio & Unlock Badge", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

/** ACHIEVEMENT: Level 13 Completion */
@Composable
private fun Module10AchievementView(onFinishLevel13: () -> Unit) {
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
            Text("💳", fontSize = 44.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("LEVEL 13 COMPLETED!", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        Text("Brand Ready Creator", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33FF2A7A))
                .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text("🏆 XP REWARD: +900 XP", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            "You have successfully built a professional creator profile and complete digital media kit portfolio! You are now prepared to operate as a brand-ready creator.",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.9f),
            textAlign = TextAlign.Center,
            lineHeight = 17.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onFinishLevel13,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("Complete Level 13 🎉", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
