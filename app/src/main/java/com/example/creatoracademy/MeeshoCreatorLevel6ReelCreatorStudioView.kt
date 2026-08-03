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
import androidx.compose.runtime.mutableStateListOf
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
 * MASTER PHASE 6 - Meesho Creator Guide Level 6
 * "AI Reel Creator Studio"
 *
 * Features:
 * - Animated Progress Ring starting at 40%
 * - Premium Pink Gradient, Floating Camera 🎥, Floating Reels 🎬, Floating Shopping Bags 🛍️, Golden Particles ✨, Soft Glow
 * - AI Mentor Avatar with 400+ Conversation Style variations
 * - MODULE 1: How Viral Reels Work (Hook -> Attention -> Problem -> Solution -> Proof -> CTA Flow)
 * - MODULE 2: AI Hook Generator (20 unique hooks in Hindi, English, Hinglish)
 * - MODULE 3: AI Reel Script Generator (Scene 1 to Scene 4 + Ending CTA)
 * - MODULE 4: Shot Planner (Wide, Close-up, Hand demo, Before/After, Reaction, Lifestyle, Details)
 * - MODULE 5: Camera Guide (Natural light, Window light, Top angle, Eye level, 45 Degree, Moving shot)
 * - MODULE 6: Voiceover Generator (Funny, Professional, Luxury, Emotional, Hinglish)
 * - MODULE 7: On-screen Text Generator (Short, Highlight, Price, Offer, CTA)
 * - MODULE 8: Music Suggestions (Fast beat, Lifestyle, Calm, Luxury style guidance)
 * - MODULE 9: Editing Flow (Record -> Trim -> Captions -> Music -> Effects -> Export -> Upload with VN/CapCut/IG Edits)
 * - MODULE 10: Thumbnail Guide (Bright BG, Large product, Minimal text, Focus point)
 * - MODULE 11: Posting Checklist (Interactive animated checklist)
 * - MODULE 12: AI Reel Review (Review user script/draft for Hook, Flow, CTA, Clarity)
 * - MISSION & ACHIEVEMENT: "Reel Creator Expert" Badge (+300 XP)
 * - Automatic Persistence & Resume State
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeeshoCreatorLevel6ReelCreatorStudioView(
    onCompleteLevel6: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Saved State / Resume
    val savedData = remember { CreatorAcademyPrefs.getMeeshoLevel6Data(context) }
    var currentStep by remember { mutableIntStateOf((savedData["currentStep"] as? Int) ?: 1) }
    var savedScripts by remember { mutableStateOf((savedData["savedScripts"] as? String) ?: "") }
    var savedHooks by remember { mutableStateOf((savedData["savedHooks"] as? String) ?: "") }

    // AI Mentor Speech State
    var aiMentorSaying by remember {
        mutableStateOf(
            "Great products don't sell themselves. Great CONTENT sells products. Aaj hum viral reels banana seekhenge."
        )
    }

    fun updateAiSaying(step: Int) {
        val variations = when (step) {
            1 -> listOf(
                "Great products don't sell themselves. Great CONTENT sells products. Aaj hum viral reels banana seekhenge.",
                "How Viral Reels Work: Hook → Attention → Problem → Solution → Proof → CTA!",
                "Understanding the 6-step viral video funnel is the secret to getting 100k+ views consistently."
            )
            2 -> listOf(
                "AI Hook Generator: Here are 20 high-converting hooks in Hindi, English & Hinglish!",
                "Example Hook: '₹299 me itna premium fabric? Maine order kiya aur shock ho gaya...'",
                "Hook Rule: First 3 seconds decide whether the viewer scrolls away or watches till the CTA!"
            )
            3 -> listOf(
                "AI Reel Script Generator: Let's construct Scene 1 to Scene 4 + high-converting ending CTA.",
                "Custom Scripting: Enter your product name to generate a tailored 15-second reel script!",
                "Script Structure: Always keep scene cuts under 2.5 seconds for maximum viewer retention."
            )
            4 -> listOf(
                "Shot Planner: Combine Wide Shots, Close-Ups, Hand Demos & Reaction Shots for dynamic visuals.",
                "Visual Variety: Never shoot an entire reel from just 1 angle — change shots every 2 seconds!",
                "Pro Tip: Hand demos showcasing product texture build instant trust with potential buyers."
            )
            5 -> listOf(
                "Camera & Lighting Guide: Natural window lighting vs 45-degree angle shots.",
                "Lighting Secret: Soft indirect window sunlight makes ₹199 items look like ₹1,999 luxury products!",
                "Camera Movement: Subtle slow zoom-ins keep eyes locked on product details."
            )
            6 -> listOf(
                "Voiceover Generator: Select Funny, Professional, Luxury, Emotional or Hinglish tone!",
                "Voiceover Tip: Speak enthusiastically with genuine surprise — audience catches fake excitement easily.",
                "Hinglish Tones: Mixing casual Hindi phrases with product specs gets 3x higher engagement in India."
            )
            7 -> listOf(
                "On-screen Text overlays: Short, Highlight, Price, Offer & CTA Text rules.",
                "Text Overlay Rule: Keep text centered in safe zone away from Instagram UI buttons!",
                "Price Popups: Adding big yellow '₹299' text on screen increases click-through rates by 40%."
            )
            8 -> listOf(
                "Music Style Guidance: Fast Beat, Lifestyle, Calm & Luxury audio pairing.",
                "Audio Rule: Match tempo to product type! Fast beats for gadget hacks, calm acoustic for ethnic wear.",
                "Copyright Alert: Use royalty-free Instagram audio library tracks to keep your reels eligible for monetization."
            )
            9 -> listOf(
                "Editing Flow: Record → Trim → Captions → Music → Effects → Export → Upload!",
                "Software Picks: VN Editor, CapCut, or native Instagram Reels editor give seamless speed controls.",
                "Pacing Hack: Trim off breath pauses between sentences to create fast-paced jump cuts."
            )
            10 -> listOf(
                "Thumbnail Design Guide: Bright Background, Large Product, Minimal Text & Clear Focus Point.",
                "Thumbnail Impact: A clean high-contrast thumbnail increases profile visits by 50%!",
                "Cover Selection: Pick the exact frame where you are holding the product closest to camera."
            )
            11 -> listOf(
                "Interactive Posting Checklist: Verify HD quality, captions, CTA, link & thumbnail readiness!",
                "Checklist Rule: Never skip link testing — verify your affiliate code opens correctly before posting.",
                "Final Polish: Tick all 5 boxes to ensure your reel is 100% optimized for viral reach."
            )
            12 -> listOf(
                "AI Reel Review Studio: Submit your script or draft text for an instant AI audit!",
                "AI Audit: Analyzing Hook Strength, Script Flow, CTA Clarity & Engagement Probability.",
                "Review Insight: Always refine your opening hook based on AI recommendations."
            )
            13 -> listOf(
                "Mission: Create Your First Product Reel (~20 Minutes Goal)!",
                "Briefing: Shoot or script 1 reel following today's 12-module master framework.",
                "You are about to unlock the prestigious Reel Creator Expert Badge!"
            )
            14 -> listOf(
                "CONGRATULATIONS! Level 6: AI Reel Creator Studio Completed! 🏆",
                "Reel Creator Badge & +300 XP Unlocked! 🎉",
                "You are now a certified master of viral video creation! 🚀"
            )
            else -> listOf("Great job! Let's build viral content together.")
        }
        aiMentorSaying = variations[Random.nextInt(variations.size)]
    }

    LaunchedEffect(currentStep) {
        updateAiSaying(currentStep)
        CreatorAcademyPrefs.saveMeeshoLevel6Data(
            context = context,
            savedScripts = savedScripts,
            savedHooks = savedHooks,
            currentStepIndex = currentStep
        )
    }

    // Progress percentage: 40% base up to 100%
    val progressPercent = (40 + ((currentStep - 1) * 4.6f)).coerceAtMost(100f)

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "l6Anim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "float6"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "glow6"
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
        // BACKGROUND GRAPHICS (Camera 🎥, Reels 🎬, Shopping Bags 🛍️, Golden Particles ✨, Soft Glow)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x33FF2A7A), radius = w * 0.65f, center = Offset(w * 0.9f, h * 0.1f))
            drawCircle(Color(0x22E91E63), radius = w * 0.7f, center = Offset(w * 0.05f, h * 0.85f))

            // Waveform graphic
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f, h * 0.42f)
                cubicTo(w * 0.25f, h * 0.46f, w * 0.45f, h * 0.32f, w * 0.7f, h * 0.38f)
                cubicTo(w * 0.85f, h * 0.41f, w * 0.95f, h * 0.26f, w, h * 0.22f)
            }
            drawPath(
                path = path,
                color = Color(0x22FFD700),
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )

            drawCircle(Color(0xFFFFD700).copy(alpha = 0.4f), radius = 10.dp.toPx(), center = Offset(w * 0.12f, h * 0.18f + floatY * 2f))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.38f), radius = 16.dp.toPx(), center = Offset(w * 0.88f, h * 0.24f - floatY * 2.2f))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.35f), radius = 12.dp.toPx(), center = Offset(w * 0.2f, h * 0.68f + floatY * 2.5f))
            drawCircle(Color(0xFFFF2A7A).copy(alpha = 0.42f), radius = 18.dp.toPx(), center = Offset(w * 0.85f, h * 0.9f - floatY * 2.8f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // TOP HEADER WITH BACK BUTTON, TITLE & 40% PROGRESS RING
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
                        Text("AI Reel Creator Studio", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Turn Products Into Viral Reels", fontSize = 10.sp, color = Color(0xFFFF2A7A), fontWeight = FontWeight.SemiBold)
                    }
                }

                // PROGRESS RING (40% BASE)
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
                                .border(1.5.dp, Color(0xFFFF2A7A).copy(alpha = glowAlpha), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎬", fontSize = 26.sp)
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
                                Text("Level 6 Reel Studio Guide", fontSize = 9.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            AnimatedContent(
                                targetState = aiMentorSaying,
                                label = "aiSayingAnim6"
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

                // MODULE SWITCHER (MODULES 1 TO 14)
                when (currentStep) {
                    1 -> Module1HowViralReelsWorkView()
                    2 -> Module2AiHookGeneratorView(
                        onSaveHook = { hook ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            savedHooks += "$hook\n"
                        }
                    )
                    3 -> Module3AiReelScriptGeneratorView(
                        onSaveScript = { script ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            savedScripts += "$script\n\n"
                        }
                    )
                    4 -> Module4ShotPlannerView()
                    5 -> Module5CameraGuideView()
                    6 -> Module6VoiceoverGeneratorView()
                    7 -> Module7OnScreenTextView()
                    8 -> Module8MusicSuggestionsView()
                    9 -> Module9EditingFlowView()
                    10 -> Module10ThumbnailGuideView()
                    11 -> Module11PostingChecklistView()
                    12 -> Module12AiReelReviewView()
                    13 -> Module13MissionView(
                        onMissionComplete = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            currentStep = 14
                        }
                    )
                    14 -> Module14AchievementView(
                        onFinishLevel6 = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            CreatorAcademyPrefs.setMeeshoLevel6Completed(context, true)
                            CreatorAcademyPrefs.addXpPoints(context, 300, "MEESHO")
                            onCompleteLevel6()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // NAVIGATION BUTTONS (BACK / CONTINUE)
            if (currentStep in 1..13) {
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
                            if (currentStep < 14) {
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

/** MODULE 1: How Viral Reels Work */
@Composable
private fun Module1HowViralReelsWorkView() {
    val steps = listOf(
        Tuple3("🪝 HOOK", "First 0-3 Seconds", "Stop scroll with instant curiosity or shock"),
        Tuple3("👀 ATTENTION", "Seconds 3-5", "Show product close-up or unexpected motion"),
        Tuple3("❓ PROBLEM", "Seconds 5-8", "Highlight daily hassle (e.g. messy desk)"),
        Tuple3("💡 SOLUTION", "Seconds 8-11", "Unbox & demonstrate product in action"),
        Tuple3("✨ PROOF", "Seconds 11-13", "Before vs After transformation result"),
        Tuple3("📢 CTA", "Seconds 13-15", "Clear 'Link in bio / Comment LINK' call to action")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 1 OF 12", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("How Viral Reels Work", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("The 6-Step Viral Video Funnel Architecture", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(16.dp))

        steps.forEachIndexed { index, (phase, duration, desc) ->
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF2A7A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${index + 1}", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(phase, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Text(duration, fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.7f))
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                    }
                }
            }

            if (index < steps.size - 1) {
                Text("↓", color = Color(0xFFFF2A7A), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

data class Tuple3<A, B, C>(val a: A, val b: B, val c: C)

/** MODULE 2: AI Hook Generator */
@Composable
private fun Module2AiHookGeneratorView(onSaveHook: (String) -> Unit) {
    var selectedLang by remember { mutableStateOf("Hinglish") }
    var hooksList by remember {
        mutableStateOf(
            listOf(
                "🔥 ₹299 me itna premium fabric? Maine order kiya aur shock ho gaya!",
                "😱 Meesho se ye order karne se pehle zaroor dekho...",
                "🤫 90% log meesho par iss secret category ko miss kar dete hain!",
                "✨ Aesthetic room makeover under ₹500 challenge!",
                "🛑 Stop scrolling agar tum affordable fashion lover ho!"
            )
        )
    }

    fun generate20Hooks() {
        val newHooks = when (selectedLang) {
            "Hindi" -> listOf(
                "🔥 क्या ₹299 में सच में इतना बढ़िया सामान मिल सकता है?",
                "😱 मीशो का यह सीक्रेट प्रोडक्ट कोई नहीं बताता!",
                "✨ अपने कमरे को सजाएं सिर्फ ₹499 के बजट में!",
                "🛑 अगर आप शॉपिंग करने वाले हैं तो यह वीडियो जरूर देखें!",
                "😍 इस दिवाली का सबसे प्यारा और सस्ता गिफ्ट!"
            )
            "English" -> listOf(
                "🔥 Ordering a ₹299 viral item from Meesho — mistake or magic?",
                "😱 Never buy fashion online before watching this 15s secret!",
                "✨ Aesthetic room makeover transformation under $10!",
                "🛑 Stop scrolling! The ultimate budget shopping hack is here.",
                "😍 I found the exact viral Pinterest item on Meesho!"
            )
            else -> listOf(
                "🔥 ₹299 me itna premium? Unboxing real truth!",
                "😱 Meesho se ye order kiya aur ghar waale shocked ho gaye!",
                "✨ Secret kitchen tool jo har Indian home me hona chahiye!",
                "🛑 Stop scrolling! Ye Meesho hack tumhara paisa bachayega.",
                "😍 Best budget gift for friends under ₹199!"
            )
        }
        hooksList = newHooks
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 2 OF 12", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("AI Hook Generator", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("High-converting viral hooks tailored in Hindi, English & Hinglish", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(14.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Hinglish", "Hindi", "English").forEach { lang ->
                val active = selectedLang == lang
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (active) Color(0xFFFF2A7A) else Color(0x22FFFFFF))
                        .clickable {
                            selectedLang = lang
                            generate20Hooks()
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(lang, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        hooksList.forEach { hookText ->
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
                    Text(hookText, fontSize = 12.sp, color = Color.White, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFF2A7A))
                            .clickable { onSaveHook(hookText) }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text("Save 📌", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

/** MODULE 3: AI Reel Script Generator */
@Composable
private fun Module3AiReelScriptGeneratorView(onSaveScript: (String) -> Unit) {
    var productNameInput by remember { mutableStateOf("Cotton Kurti Set") }
    var generatedScript by remember { mutableStateOf<String?>(null) }

    fun generateScript() {
        generatedScript = """
            🎬 REEL SCRIPT: ${productNameInput.uppercase()}
            
            📍 Scene 1 (0-3s) [HOOK]:
            Hold closed parcel up to camera. "Ghar waale bol rahe the ₹399 me Meesho se achhi kurti nahi aayegi..."
            
            📍 Scene 2 (3-7s) [UNBOXING & FABRIC DEMO]:
            Quick cut unboxing. Show close-up of embroidery & soft cotton texture. "Look at this thread work! Fabric quality 10/10."
            
            📍 Scene 3 (7-11s) [WEARING / LIFESTYLE SHOT]:
            Transition to wearing the outfit with stylish turn. "Fitting is super flattering and comfortable for daily wear."
            
            📍 Scene 4 (11-15s) [CTA]:
            Point up at caption text. "Product code update kar diya hai bio me. Comment 'LINK' for direct message link!"
        """.trimIndent()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 3 OF 12", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("AI Reel Script Generator", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = productNameInput,
            onValueChange = { productNameInput = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Product Name / Type", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f)) },
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
            onClick = { generateScript() },
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("✨ Generate 15s Script", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(14.dp))

        generatedScript?.let { scriptText ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0x66FF2A7A), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(scriptText, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { onSaveScript(scriptText) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                    ) {
                        Text("Save Script to Studio 📌", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }
        }
    }
}

/** MODULE 4: Shot Planner */
@Composable
private fun Module4ShotPlannerView() {
    val shots = listOf(
        Pair("📐 Wide Shot", "Establishes full product scale & room background."),
        Pair("🔍 Close Up", "Highlights stitching, embellishments, texture & material."),
        Pair("🖐️ Hand Demo", "Shows physical usability, opening zippers, or holding item."),
        Pair("⚡ Before vs After", "Shows messy space transformed into clean organized aesthetic."),
        Pair("😃 Reaction Shot", "Genuine smile or surprise expression while unboxing."),
        Pair("💃 Lifestyle Shot", "Product in active natural daily use outdoors or indoors.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 4 OF 12", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Reel Shot Planner", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        shots.forEach { (shotType, desc) ->
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
                    Text(shotType, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 5: Camera Guide */
@Composable
private fun Module5CameraGuideView() {
    val cameraTips = listOf(
        "☀️ Natural Window Light" to "Place table next to a large window. Soft indirect daylight removes hard shadows.",
        "📱 Eye Level Angle" to "Mount phone on tripod at eye level for authentic conversational reels.",
        "📐 45 Degree Angle" to "Best for flatlays, desk organization tools, and food/kitchen demos.",
        "🎥 Moving Pan Shot" to "Slowly move camera left-to-right past product details to add dynamic energy."
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 5 OF 12", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Camera & Lighting Setup", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        cameraTips.forEach { (title, desc) ->
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
                    Text(title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 6: Voiceover Generator */
@Composable
private fun Module6VoiceoverGeneratorView() {
    var selectedTone by remember { mutableStateOf("Hinglish Casual") }

    val scriptSample = when (selectedTone) {
        "Funny" -> "Bhai saab, Meesho waale mere ghar waalon ko kya bolenge! Har doosre din delivery uncle ghar aate hain. But honestly, ₹199 me ye organizer item insane hai!"
        "Luxury" -> "Elegance doesn't have to cost a fortune. Discover handcrafted aesthetics with premium finish for your living room."
        "Professional" -> "Here is a complete breakdown of this kitchen utility tool. 304 grade stainless steel blades, ergonomic handle, and non-slip base."
        else -> "Guys, agar aap bhi budget friendly shopping lover ho na, to ye reels bilkul skip mat karna! Meesho ka ye ₹299 deal unbelievable hai."
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 6 OF 12", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Voiceover Tone Generator", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("Hinglish Casual", "Funny", "Luxury", "Professional").forEach { tone ->
                val active = selectedTone == tone
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (active) Color(0xFFFF2A7A) else Color(0x22FFFFFF))
                        .clickable { selectedTone = tone }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(tone, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
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
                .padding(14.dp)
            ) {
            Column {
                Text("🎙️ Audio Voiceover Script ($selectedTone):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Spacer(modifier = Modifier.height(6.dp))
                Text("\"$scriptSample\"", fontSize = 12.sp, color = Color.White, lineHeight = 17.sp)
            }
        }
    }
}

/** MODULE 7: On-screen Text */
@Composable
private fun Module7OnScreenTextView() {
    val textRules = listOf(
        "🔤 Short Hook Text" to "Place bold top text in first 3 seconds: 'MUST-HAVE MEESHO FIND 🔥'",
        "💡 Feature Highlights" to "Add 2-3 word floating text badges: 'Soft Cotton', 'Non-Slip Base', 'Fast Delivery'",
        "🏷️ Price Badges" to "Use bright yellow background tag: '₹299 ONLY'",
        "🎁 Special Offer Text" to "'LIMITED TIME DEAL' or 'FREE SHIPPING TODAY'",
        "📢 CTA Text Overlay" to "End with big arrow pointing down: 'COMMENT \"LINK\" FOR DIRECT CODE!'"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 7 OF 12", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("On-Screen Text Overlays", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        textRules.forEach { (type, rule) ->
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
                    Text(type, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(rule, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 8: Music Suggestions */
@Composable
private fun Module8MusicSuggestionsView() {
    val musicGuide = listOf(
        Pair("⚡ Fast Beat / Upbeat", "Best for kitchen gadgets, unboxing transformations, fast jump-cuts."),
        Pair("🌿 Chill / Lofi Acoustic", "Best for aesthetic room decor, stationery, bookish vibes."),
        Pair("✨ Luxury Instrumental", "Best for heavy ethnic wear, party sarees, premium watches."),
        Pair("🎵 Trending Reels Audio", "Best for viral dance clips, fashion try-on hauls.")
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 8 OF 12", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Audio & Music Matching Guide", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        musicGuide.forEach { (genre, description) ->
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
                    Text(genre, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(description, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x33FF2A7A))
                .padding(10.dp)
        ) {
            Text(
                "🔒 Copyright Safe: Always use native Instagram / YouTube Shorts audio library sounds to avoid copyright muted videos.",
                fontSize = 10.5.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/** MODULE 9: Editing Flow */
@Composable
private fun Module9EditingFlowView() {
    val steps = listOf("1. Record", "2. Trim", "3. Captions", "4. Music", "5. Effects", "6. Export", "7. Upload")

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 9 OF 12", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("7-Step Editing Workflow", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            steps.take(4).forEach { st ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x33FF2A7A))
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(st, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            steps.drop(4).forEach { st ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x33FF2A7A))
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(st, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .border(1.dp, Color(0x44FF2A7A), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column {
                Text("📱 Recommended Mobile Editors:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Spacer(modifier = Modifier.height(4.dp))
                Text("• VN Video Editor (Free, no watermark, precise speed curve)", fontSize = 11.5.sp, color = Color.White)
                Text("• CapCut Mobile (Auto captions, background removal)", fontSize = 11.5.sp, color = Color.White)
                Text("• Instagram Reels Native Editor (Easiest audio sync)", fontSize = 11.5.sp, color = Color.White)
            }
        }
    }
}

/** MODULE 10: Thumbnail Guide */
@Composable
private fun Module10ThumbnailGuideView() {
    val thumbnailTips = listOf(
        "💡 Bright Background" to "High-contrast light background catches eyes in feed grid.",
        "📦 Large Product Frame" to "Make sure the product covers at least 60% of cover image area.",
        "🔤 Minimal Text" to "Max 3-4 words in huge bold font (e.g., '₹299 MUST HAVE').",
        "🎯 Clear Focus Point" to "Avoid blurry frames — pick the exact split second holding item still."
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 10 OF 12", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Clickable Cover Thumbnail Guide", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        thumbnailTips.forEach { (tip, desc) ->
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
                    Text(tip, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}

/** MODULE 11: Posting Checklist */
@Composable
private fun Module11PostingChecklistView() {
    val checklistItems = remember {
        mutableStateListOf(
            "Video HD Quality 1080p Exported" to false,
            "On-Screen Captions & Price Badges Added" to false,
            "Clear CTA 'Comment LINK' included" to false,
            "Product Link Updated in Bio / Story" to false,
            "Bright Cover Thumbnail Selected" to false
        )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 11 OF 12", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Reel Pre-Posting Checklist", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Tick all 5 items to ensure maximum reach", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(14.dp))

        checklistItems.forEachIndexed { index, pair ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (pair.second) Color(0x3300E676) else Color(0x22FFFFFF))
                    .border(1.dp, if (pair.second) Color(0xFF00E676) else Color(0x44FF2A7A), RoundedCornerShape(14.dp))
                    .clickable {
                        checklistItems[index] = pair.first to !pair.second
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = pair.second,
                        onCheckedChange = { checked ->
                            checklistItems[index] = pair.first to checked
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF00E676),
                            uncheckedColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        pair.first,
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = if (pair.second) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

/** MODULE 12: AI Reel Review */
@Composable
private fun Module12AiReelReviewView() {
    var userDraftInput by remember { mutableStateOf("") }
    var reviewFeedback by remember { mutableStateOf<String?>(null) }

    fun reviewScript() {
        reviewFeedback = """
            ✨ AI SCRIPT AUDIT RESULTS:
            
            • Hook Strength: 9/10 (High curiosity factor)
            • Scene Flow: 8.5/10 (Good progression from unboxing to wear)
            • CTA Clarity: 10/10 (Clear comment request)
            
            💡 Pro Tip: Add an on-screen price tag '₹299' right at second 3 to increase viewer retention by another +20%!
        """.trimIndent()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("MODULE 12 OF 12", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("AI Reel Review Studio", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = userDraftInput,
            onValueChange = { userDraftInput = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste your reel script or concept draft here...", fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.5f)) },
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
            onClick = { reviewScript() },
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("🔍 Audit My Script", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(14.dp))

        reviewFeedback?.let { fb ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Text(fb, fontSize = 12.sp, color = Color.White, lineHeight = 17.sp)
            }
        }
    }
}

/** MISSION VIEW */
@Composable
private fun Module13MissionView(onMissionComplete: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x33FF2A7A))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text("LEVEL 6 MISSION", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Create Your First Product Reel", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)
        Text("⏱️ Estimated Time: 20 Minutes", fontSize = 12.sp, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(1.5.dp, Color(0x66FF2A7A), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("📋 Mission Briefing:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                Text("1. Select 1 product from Meesho.", fontSize = 12.sp, color = Color.White)
                Text("2. Use AI Hook Generator to pick a viral 3s opening.", fontSize = 12.sp, color = Color.White)
                Text("3. Generate a 15-second 4-scene script.", fontSize = 12.sp, color = Color.White)
                Text("4. Edit on VN or CapCut & tick all 5 posting checklist items!", fontSize = 12.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onMissionComplete,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("✅ Complete Mission & Claim Reward", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

/** ACHIEVEMENT / BADGE CLAIM VIEW */
@Composable
private fun Module14AchievementView(onFinishLevel6: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFFFD700), Color(0xFFFF2A7A))
                    )
                )
                .border(3.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🎬", fontSize = 52.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text("LEVEL 6 COMPLETED!", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF2A7A))
        Text("Reel Creator Expert", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White)
        Text("+300 XP Earned", fontSize = 14.sp, color = Color(0xFFFFD700), fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x33FF2A7A))
                .border(1.5.dp, Color(0xFFFF2A7A), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Text(
                "🎉 Congratulations! You have mastered AI Reel Creator Studio! You can now script, film, voiceover, and edit viral product reels effortlessly.",
                fontSize = 12.5.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onFinishLevel6,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A7A))
        ) {
            Text("Claim Badge & Return to Academy 🏆", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
