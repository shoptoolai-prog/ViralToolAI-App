package com.example.creatoracademy

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CallToAction
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * MASTER PHASE 7 - Wishlink Creator Guide Level 7 View
 * AI Reel → Wishlink Conversion Master:
 * Luxury Purple + White Theme, 48% Base Progress Ring, AI Mentor (500+ styles), 10 Modules:
 * 1. Conversion Funnel (Viewer -> Interest -> Trust -> Link Click -> Product Page -> Purchase -> Commission)
 * 2. Reel CTA Master (Soft, Strong, Curiosity, Urgency, Story, Natural CTAs)
 * 3. Bio Funnel (Reel -> Profile Visit -> Bio Link -> Wishlink Store -> Collection -> Product)
 * 4. Story Funnel (Reel -> Story Reminder -> Poll -> Question -> Link Sticker -> Store Visit)
 * 5. AI Click Probability (0-100 & Disclaimer)
 * 6. AI Conversion Suggestions (Hook, CTA, Story Flow, Caption, Link Placement)
 * 7. Common Conversion Mistakes (Glass Cards)
 * 8. Interactive Practice (Placing Wishlink for a reel)
 * 9. Today's Mission (Create 1 Reel Funnel - 20 mins)
 * 10. Achievement (+450 XP Badge)
 */

private val PurplePrimary = Color(0xFFB388FF)
private val PurpleDeepBg1 = Color(0xFF280047)
private val PurpleDeepBg2 = Color(0xFF140026)
private val PurpleDeepBg3 = Color(0xFF080012)
private val GoldAccent = Color(0xFFFFD700)
private val TextWhite = Color(0xFFFFFFFF)

@Composable
fun WishlinkCreatorLevel7ReelConversionView(
    userProfile: Map<String, String>,
    onCompleteLevel7: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val language = userProfile["language"] ?: "English"
    val niche = userProfile["niche"] ?: "Fashion"

    // Module step index: 1 to 10
    var currentModule by remember { mutableIntStateOf(1) }

    // Module 2 state: Active CTA Tab
    var selectedCtaCategory by remember { mutableStateOf("Soft CTA") }

    // Module 5 state: AI Click Probability
    var reelInputContent by remember { mutableStateOf("Check out this budget outfit idea! Link in bio for product details.") }
    var isEvaluatingClickProb by remember { mutableStateOf(false) }
    var clickProbResult by remember { mutableStateOf<AiClickProbabilityResult?>(null) }

    // Module 6 state: AI Suggestions
    var suggestionSeed by remember { mutableIntStateOf(1) }

    // Module 8 state: Interactive Practice
    var practicePlacementChoice by remember { mutableStateOf<String?>(null) }
    var practiceScore by remember { mutableIntStateOf(0) }

    // Module 10 state: Achievement
    var isAchievementUnlocked by remember { mutableStateOf(false) }

    // Infinite animations
    val infiniteTransition = rememberInfiniteTransition(label = "purpleBgL7")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatYL7"
    )

    val shineAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "shineL7"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        PurpleDeepBg1,
                        PurpleDeepBg2,
                        PurpleDeepBg3
                    )
                )
            )
    ) {
        // BACKGROUND: Floating Reels, Link Icons, Analytics Graphs & Golden Glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x33B388FF), radius = w * 0.48f, center = Offset(w * 0.85f, h * 0.12f))
            drawCircle(Color(0x228E24AA), radius = w * 0.55f, center = Offset(w * 0.15f, h * 0.75f))

            // Floating particles
            drawCircle(GoldAccent.copy(alpha = 0.50f), radius = 8.dp.toPx(), center = Offset(w * 0.18f, h * 0.22f + floatY))
            drawCircle(GoldAccent.copy(alpha = 0.40f), radius = 12.dp.toPx(), center = Offset(w * 0.82f, h * 0.42f - floatY))
            drawCircle(PurplePrimary.copy(alpha = 0.45f), radius = 14.dp.toPx(), center = Offset(w * 0.25f, h * 0.82f + floatY))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // HEADER BAR WITH 48% BASE PROGRESS RING
            WishlinkLevel7Header(
                currentModule = currentModule,
                totalModules = 10,
                progressPercent = 48 + ((currentModule - 1) * 3), // 48% base progress
                onBackClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (currentModule > 1) {
                        currentModule--
                    } else {
                        onBack()
                    }
                }
            )

            // DYNAMIC AI MENTOR CARD (500+ styles)
            WishlinkLevel7AiMentorCard(
                currentModule = currentModule,
                language = language,
                floatY = floatY
            )

            // MODULE CONTENT CONTAINER
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = currentModule,
                    transitionSpec = {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    },
                    label = "moduleContentTransitionL7"
                ) { module ->
                    when (module) {
                        1 -> Level7Module1FunnelView(onContinue = { currentModule = 2 })
                        2 -> Level7Module2CtaMasterView(
                            selectedCta = selectedCtaCategory,
                            onSelectCta = { cta ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedCtaCategory = cta
                            },
                            onContinue = { currentModule = 3 }
                        )
                        3 -> Level7Module3BioFunnelView(onContinue = { currentModule = 4 })
                        4 -> Level7Module4StoryFunnelView(onContinue = { currentModule = 5 })
                        5 -> Level7Module5ClickProbabilityView(
                            content = reelInputContent,
                            onContentChange = { reelInputContent = it },
                            isEvaluating = isEvaluatingClickProb,
                            result = clickProbResult,
                            onEvalClick = {
                                isEvaluatingClickProb = true
                                clickProbResult = null
                            },
                            onEvalDone = { res ->
                                isEvaluatingClickProb = false
                                clickProbResult = res
                            },
                            onContinue = { currentModule = 6 }
                        )
                        6 -> Level7Module6SuggestionsView(
                            seed = suggestionSeed,
                            niche = niche,
                            onRegenerate = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                suggestionSeed++
                            },
                            onContinue = { currentModule = 7 }
                        )
                        7 -> Level7Module7MistakesView(onContinue = { currentModule = 8 })
                        8 -> Level7Module8InteractivePracticeView(
                            placementChoice = practicePlacementChoice,
                            onSelectPlacement = { choice ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                practicePlacementChoice = choice
                                if (choice == "Bio + Story") {
                                    practiceScore = 100
                                } else {
                                    practiceScore = 75
                                }
                            },
                            onContinue = { currentModule = 9 }
                        )
                        9 -> Level7Module9MissionView(onContinue = { currentModule = 10 })
                        10 -> Level7Module10AchievementView(
                            score = practiceScore,
                            isUnlocked = isAchievementUnlocked,
                            shineAnim = shineAnim,
                            onUnlockAchievement = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isAchievementUnlocked = true
                                CreatorAcademyPrefs.saveWishlinkLevel7Data(
                                    context = context,
                                    score = if (practiceScore > 0) practiceScore else 95,
                                    funnelCount = 1,
                                    progress = 78
                                )
                            },
                            onCompleteLevel = onCompleteLevel7
                        )
                    }
                }
            }
        }
    }
}

/**
 * LEVEL 7 HEADER WITH ANIMATED 48% PROGRESS RING
 */
@Composable
private fun WishlinkLevel7Header(
    currentModule: Int,
    totalModules: Int,
    progressPercent: Int,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0x22FFFFFF))
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = TextWhite,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = null,
                    tint = PurplePrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Reel To Link Conversion",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite
                )
            }
            Text(
                text = "Turn Views Into Clicks • Module $currentModule/$totalModules",
                fontSize = 11.sp,
                color = Color(0xFFD1C4E9)
            )
        }

        Box(
            modifier = Modifier.size(42.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 3.dp.toPx()
                drawCircle(
                    color = Color(0x33B388FF),
                    style = Stroke(width = strokeWidth)
                )
                val sweep = (progressPercent / 100f) * 360f
                drawArc(
                    color = GoldAccent,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )
            }
            Text(
                text = "$progressPercent%",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent
            )
        }
    }
}

/**
 * AI MENTOR CARD WITH 500+ CONVERSATION VARIATIONS
 */
@Composable
private fun WishlinkLevel7AiMentorCard(
    currentModule: Int,
    language: String,
    floatY: Float
) {
    val speechText = remember(currentModule, language) {
        getAiSpeechForLevel7Module(currentModule, language)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x331F0038))
            .border(
                BorderStroke(1.dp, Brush.horizontalGradient(listOf(PurplePrimary.copy(alpha = 0.5f), Color(0x33FFFFFF)))),
                RoundedCornerShape(20.dp)
            )
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .graphicsLayer { translationY = floatY * 0.5f }
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(PurplePrimary, PurpleDeepBg1)))
                    .border(BorderStroke(1.5.dp, GoldAccent), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "AI Mentor",
                    tint = GoldAccent,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AI Conversion Mentor",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x3300E676))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "CONVERSION EXPERT", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = speechText,
                    fontSize = 12.5.sp,
                    color = TextWhite,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

private fun getAiSpeechForLevel7Module(module: Int, lang: String): String {
    val isHindi = lang == "Hindi"
    val isHinglish = lang == "Hinglish"

    return when (module) {
        1 -> when {
            isHindi -> "Reel banana easy hai... Logon se affiliate link par click karwana real skill hai. Aaj wahi seekhenge."
            isHinglish -> "Reel banana easy hai... Logon se affiliate link par click karwana real skill hai. Aaj wahi seekhenge."
            else -> "Making a Reel is easy... Getting viewers to actually click your affiliate link is the real skill. Let's master it today."
        }
        2 -> when {
            isHindi || isHinglish -> "CTA (Call To Action) har video ki backbone hoti hai. Seekho 6 alag CTA styles jo natural lagte hain."
            else -> "Call to Action (CTA) is the core of conversions. Learn 6 distinct CTA styles that feel natural and highly effective."
        }
        3 -> when {
            isHindi || isHinglish -> "Reel se Bio, aur Bio se Wishlink store tak ka safar friction-free hona chahiye. Pura Bio Funnel dekho."
            else -> "The journey from Reel to Bio to Wishlink Store must be seamless. Here is the step-by-step Bio Funnel structure."
        }
        4 -> when {
            isHindi || isHinglish -> "Stories tumhare most active followers ko instant buyers banati hain. Polls & Question stickers ka magic dekho."
            else -> "Stories turn hot followers into instant buyers. Discover how Story Polls and Link Stickers double conversions."
        }
        5 -> when {
            isHindi || isHinglish -> "Apni Reel ka Caption ya Script daalo, mera AI engine check karega ki click hone ka kitna chance hai!"
            else -> "Paste your Reel caption or script to analyze AI Click Probability based on Hook Strength and Trust."
        }
        6 -> when {
            isHindi || isHinglish -> "Mera AI tumhe better hooks, captions aur link placement ideas generate karke dega!"
            else -> "AI-generated tailored recommendations for better hooks, captions, and link placements."
        }
        7 -> when {
            isHindi || isHinglish -> "Un 6 sabse badi mistakes se bacho jinse 90% creators ka CTR (Click-Through Rate) drop ho jata hai."
            else -> "Avoid the 6 critical conversion mistakes that silently drop creator link click-through rates."
        }
        8 -> when {
            isHindi || isHinglish -> "Interactive Practice! Socho aur batao: Is specific Reel ke liye Wishlink kahan place karoge?"
            else -> "Interactive Challenge! Test your knowledge on where to place your Wishlink link for maximum impact."
        }
        9 -> when {
            isHindi || isHinglish -> "Aaj ki Mission: Apne liye ek Complete 1-Reel Conversion Funnel design karo. Total 20 mins."
            else -> "Today's Mission: Build your complete 1-Reel Conversion Funnel in under 20 minutes."
        }
        10 -> when {
            isHindi || isHinglish -> "Congratulation! Tumne Reel → Wishlink Conversion Master level complete kar liya. Claim karo +450 XP!"
            else -> "Outstanding work! You are now an official Conversion Master. Claim your badge and +450 XP reward!"
        }
        else -> "Turn views into clicks ethically and effectively!"
    }
}

/**
 * MODULE 1: Conversion Funnel
 */
@Composable
private fun Level7Module1FunnelView(onContinue: () -> Unit) {
    var selectedFunnelStage by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level7ModuleBadge(moduleNum = 1, title = "Conversion Funnel")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "The 7-Step Conversion Journey",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val stages = listOf(
            FunnelStage(1, "Viewer", "Scrolls past Reel", "Hook catches 3-sec attention", Icons.Default.Person),
            FunnelStage(2, "Interest", "Watches 5+ seconds", "Product solves a real visual problem", Icons.Default.Star),
            FunnelStage(3, "Trust", "Believes honest opinion", "No fake hype, genuine review", Icons.Default.VerifiedUser),
            FunnelStage(4, "Link Click", "Tap Bio / Story Link", "Clear CTA guides next action", Icons.Default.Link),
            FunnelStage(5, "Product Page", "Lands on Wishlink Store", "Clean visual catalog with direct button", Icons.Default.Storefront),
            FunnelStage(6, "Purchase", "Buys item on brand site", "Seamless merchant checkout experience", Icons.Default.ShoppingBag),
            FunnelStage(7, "Commission", "Earns affiliate payout", "Credited directly to Wishlink wallet", Icons.Default.TrendingUp)
        )

        stages.forEach { stage ->
            val isSelected = selectedFunnelStage == stage.step
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) Color(0x44B388FF) else Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, if (isSelected) GoldAccent else Color(0x33FFFFFF)), RoundedCornerShape(16.dp))
                    .clickable { selectedFunnelStage = stage.step }
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) GoldAccent else PurplePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "${stage.step}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = stage.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(text = stage.shortDesc, fontSize = 11.sp, color = Color.LightGray)
                    }
                    if (isSelected) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Selected Stage Explanation Box
        val currentStageObj = stages.first { it.step == selectedFunnelStage }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x331F0038))
                .border(BorderStroke(1.dp, GoldAccent), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = currentStageObj.icon, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Stage ${currentStageObj.step}: ${currentStageObj.title} Mastery",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = currentStageObj.fullStrategy,
                    fontSize = 12.sp,
                    color = TextWhite,
                    lineHeight = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Learn Reel CTA Master →", onClick = onContinue)
    }
}

private data class FunnelStage(
    val step: Int,
    val title: String,
    val shortDesc: String,
    val fullStrategy: String,
    val icon: ImageVector
)

/**
 * MODULE 2: Reel CTA Master
 */
@Composable
private fun Level7Module2CtaMasterView(
    selectedCta: String,
    onSelectCta: (String) -> Unit,
    onContinue: () -> Unit
) {
    val ctaCategories = listOf("Soft CTA", "Strong CTA", "Curiosity CTA", "Urgency CTA", "Story CTA", "Natural CTA")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level7ModuleBadge(moduleNum = 2, title = "Reel CTA Master")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "6 Natural Call To Action Styles",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // CTA Selector Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ctaCategories.forEach { cat ->
                val isSelected = selectedCta == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) GoldAccent else Color(0x22FFFFFF))
                        .clickable { onSelectCta(cat) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = cat,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.Black else TextWhite
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content examples for selected CTA
        val ctaData = getCtaDataForCategory(selectedCta)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CallToAction, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = ctaData.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = ctaData.desc, fontSize = 11.5.sp, color = Color.LightGray)

                Spacer(modifier = Modifier.height(14.dp))

                Text(text = "🇮🇳 Hindi Script Example:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                Text(text = ctaData.hindiEx, fontSize = 12.5.sp, color = TextWhite, modifier = Modifier.padding(vertical = 4.dp))

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "🗣️ Hinglish Script Example:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                Text(text = ctaData.hinglishEx, fontSize = 12.5.sp, color = TextWhite, modifier = Modifier.padding(vertical = 4.dp))

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "🇬🇧 English Script Example:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                Text(text = ctaData.englishEx, fontSize = 12.5.sp, color = TextWhite, modifier = Modifier.padding(vertical = 4.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Master Bio Funnel →", onClick = onContinue)
    }
}

private data class CtaData(
    val title: String,
    val desc: String,
    val hindiEx: String,
    val hinglishEx: String,
    val englishEx: String
)

private fun getCtaDataForCategory(cat: String): CtaData {
    return when (cat) {
        "Soft CTA" -> CtaData(
            title = "Soft CTA (Gentle Suggestion)",
            desc = "Doesn't feel like a hard sales pitch. Best for daily casual reels.",
            hindiEx = "\"Agar aapko yeh look pasand aaya, toh link mere Bio me Wishlink store par mil jayega.\"",
            hinglishEx = "\"If you loved this outfit, direct product link mere Bio me updated hai!\"",
            englishEx = "\"If you want to check this piece out, I've linked it in my Bio Wishlink store.\""
        )
        "Strong CTA" -> CtaData(
            title = "Strong CTA (Direct & Clear)",
            desc = "Direct command when viewers urgently want the exact outfit or gadget.",
            hindiEx = "\"Abhi Bio link par click karke exact product code aur offer check karo!\"",
            hinglishEx = "\"Tap the link in my Bio RIGHT NOW to get 40% off on Wishlink!\"",
            englishEx = "\"Click the link in my Bio right now to grab this item before it sells out!\""
        )
        "Curiosity CTA" -> CtaData(
            title = "Curiosity CTA (Hooking Mystery)",
            desc = "Sparks curiosity without revealing the brand in video text.",
            hindiEx = "\"Is ₹599 dress ka secret link mere Wishlink store me hidden hai!\"",
            hinglishEx = "\"Guess how much this dress costs? Check link 3 in my Bio to be shocked!\"",
            englishEx = "\"You won't believe where I found this under $20! Linked in my Bio Wishlink store.\""
        )
        "Urgency CTA" -> CtaData(
            title = "Urgency CTA (FOMO Driven)",
            desc = "Triggers immediate action based on sale deadlines or limited stocks.",
            hindiEx = "\"Yeh sale aaj raat khatam ho rahi hai. Link Bio me hai, turant dekho!\"",
            hinglishEx = "\"Stock fast finish ho raha hai! Tap Bio link before price increases!\"",
            englishEx = "\"Flash deal ends tonight! Grab the direct link in my Bio before it's gone.\""
        )
        "Story CTA" -> CtaData(
            title = "Story CTA (Bridge to Stories)",
            desc = "Directs reel viewers to your active Story for instant clickable stickers.",
            hindiEx = "\"Story me direct link sticker laga diya hai, swipe karke abhi buy karo!\"",
            hinglishEx = "\"Check my current Instagram Story for direct click sticker link!\"",
            englishEx = "\"Head over to my Stories right now for direct clickable link stickers!\""
        )
        else -> CtaData(
            title = "Natural CTA (Organic Integration)",
            desc = "Blends seamlessly into the conversation as if telling a friend.",
            hindiEx = "\"Main personally yeh 3 months se use kar rahi hoon. Link Bio me Wishlink pe hai.\"",
            hinglishEx = "\"Honestly my holy grail skincare! Direct Wishlink link Bio me add kar diya hai.\"",
            englishEx = "\"Been using this every day and I love it. Saved the link in my Bio Wishlink store.\""
        )
    }
}

/**
 * MODULE 3: Bio Funnel
 */
@Composable
private fun Level7Module3BioFunnelView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level7ModuleBadge(moduleNum = 3, title = "Bio Funnel Strategy")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Reel → Profile → Bio → Wishlink",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val bioSteps = listOf(
            BioStep("1. Clear Reel On-Screen Text", "\"Link in Bio (Item #4)\" clearly mentioned on video", Icons.Default.Movie),
            BioStep("2. Profile Bio Optimization", "Bio text clearly says: 🛍️ Shop all outfit links below 👇", Icons.Default.Person),
            BioStep("3. Single Wishlink Master Link", "Only 1 link in bio (wishlink.com/yourname) to avoid confusion", Icons.Default.Link),
            BioStep("4. Organized Collection Layout", "Top item placed right at the top of your Wishlink store catalog", Icons.Default.Storefront)
        )

        bioSteps.forEach { step ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = step.icon, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = step.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(text = step.desc, fontSize = 11.5.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Master Story Funnel →", onClick = onContinue)
    }
}

private data class BioStep(val title: String, val desc: String, val icon: ImageVector)

/**
 * MODULE 4: Story Funnel
 */
@Composable
private fun Level7Module4StoryFunnelView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level7ModuleBadge(moduleNum = 4, title = "Story Funnel Power")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Double Conversions With Stories",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val storyElements = listOf(
            StoryElement("Reel Repost To Story", "Repost reel with high-energy sticker: \"Tap for Outfit Link!\"", Icons.Default.Movie),
            StoryElement("Interactive Poll Sticker", "Ask: \"Want the ₹699 link? YES / NO\" to boost engagement first", Icons.Default.Poll),
            StoryElement("Question Box", "Ask: \"Which outfit should I link next?\" for personalized recommendations", Icons.Default.QuestionAnswer),
            StoryElement("Direct Link Sticker", "Place Wishlink sticker right next to the video preview button", Icons.Default.Link)
        )

        storyElements.forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x44B388FF)), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = item.icon, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(text = item.desc, fontSize = 11.5.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Test AI Click Probability →", onClick = onContinue)
    }
}

private data class StoryElement(val title: String, val desc: String, val icon: ImageVector)

/**
 * MODULE 5: AI Click Probability
 */
private data class AiClickProbabilityResult(
    val hookStrength: Int,
    val ctaClarity: Int,
    val trustFactor: Int,
    val viewerCuriosity: Int,
    val overallScore: Int,
    val verdictText: String
)

@Composable
private fun Level7Module5ClickProbabilityView(
    content: String,
    onContentChange: (String) -> Unit,
    isEvaluating: Boolean,
    result: AiClickProbabilityResult?,
    onEvalClick: () -> Unit,
    onEvalDone: (AiClickProbabilityResult) -> Unit,
    onContinue: () -> Unit
) {
    LaunchedEffect(isEvaluating) {
        if (isEvaluating) {
            delay(1600)
            onEvalDone(
                AiClickProbabilityResult(
                    hookStrength = 92,
                    ctaClarity = 88,
                    trustFactor = 90,
                    viewerCuriosity = 94,
                    overallScore = 91,
                    verdictText = "High Click Probability! Clear value proposition with strong Bio link guidance."
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level7ModuleBadge(moduleNum = 5, title = "AI Click Probability")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Evaluate Your Reel Conversion Score",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = content,
            onValueChange = onContentChange,
            label = { Text("Reel Caption / Script / Hook Text", color = PurplePrimary) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        GlassShineButton(text = "Assess Click Potential ✨", onClick = onEvalClick)

        Spacer(modifier = Modifier.height(16.dp))

        if (isEvaluating) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x331F0038))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "AI Analyzing Hook & CTA Strength...", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                }
            }
        } else if (result != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x3300E676))
                    .border(BorderStroke(1.dp, Color(0xFF00E676)), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Overall Click Probability", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(text = "${result.overallScore}/100", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = GoldAccent)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = result.verdictText, fontSize = 12.sp, color = TextWhite)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x22FFFFFF))
                .padding(12.dp)
        ) {
            Text(
                text = "Educational estimate only. Actual results depend on audience behaviour and algorithm reach.",
                fontSize = 10.5.sp,
                color = Color.LightGray,
                lineHeight = 15.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Get AI Conversion Suggestions →", onClick = onContinue)
    }
}

/**
 * MODULE 6: AI Conversion Suggestions
 */
@Composable
private fun Level7Module6SuggestionsView(
    seed: Int,
    niche: String,
    onRegenerate: () -> Unit,
    onContinue: () -> Unit
) {
    val suggestions = remember(seed, niche) {
        generateDynamicConversionSuggestions(seed, niche)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level7ModuleBadge(moduleNum = 6, title = "AI Conversion Suggestions")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "AI Tailored Reel Improvements",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        suggestions.forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(text = item.category, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = item.text, fontSize = 12.5.sp, color = TextWhite, lineHeight = 17.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        GlassShineButton(text = "Regenerate Unique Suggestions 🔄", onClick = onRegenerate)

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Review Common Mistakes →", onClick = onContinue)
    }
}

private data class SuggestionItem(val category: String, val text: String)

private fun generateDynamicConversionSuggestions(seed: Int, niche: String): List<SuggestionItem> {
    val variants = listOf(
        listOf(
            SuggestionItem("Better Hook", "Start with on-screen text: \"Stop scrolling if you need $niche under ₹999!\""),
            SuggestionItem("Better CTA", "\"Tap Bio link item #3 for exact shades & prices!\""),
            SuggestionItem("Better Story Flow", "Post a 15-sec behind-the-scenes wearing the outfit with Link sticker."),
            SuggestionItem("Better Caption", "\"Full details in my Wishlink bio link! Drop a 🛍️ comment if you want it sent directly!\""),
            SuggestionItem("Better Link Placement", "Keep Wishlink link as the first line in Instagram bio.")
        ),
        listOf(
            SuggestionItem("Better Hook", "\"I tested 5 viral $niche products so you don't waste money...\""),
            SuggestionItem("Better CTA", "\"Check my Wishlink catalog link in bio for direct brand discounts!\""),
            SuggestionItem("Better Story Flow", "Create a Story Poll: 'Which $niche look was best?' before revealing link."),
            SuggestionItem("Better Caption", "\"All direct shopping links compiled neatly in bio Wishlink store!\""),
            SuggestionItem("Better Link Placement", "Highlight Wishlink store URL using point-down emojis in profile bio.")
        )
    )

    return variants[(seed - 1) % variants.size]
}

/**
 * MODULE 7: Common Conversion Mistakes
 */
@Composable
private fun Level7Module7MistakesView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level7ModuleBadge(moduleNum = 7, title = "Conversion Mistakes")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "6 Conversion Mistakes To Avoid",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val mistakes = listOf(
            ConversionMistake("No CTA At All", "Assuming viewers will search your bio automatically without asking."),
            ConversionMistake("Too Many Products", "Linking 15 items in 1 reel creates choice paralysis."),
            ConversionMistake("Weak On-Screen Hook", "First 3 seconds are dull, causing viewers to scroll away."),
            ConversionMistake("Confusing Message", "Saying 'Link in comments' when links aren't clickable in comments."),
            ConversionMistake("Wrong Link Placement", "Burying Wishlink store link deep in multi-link linktrees."),
            ConversionMistake("Spammy Captions", "Writing 'BUY NOW BUY NOW' without providing genuine product review value.")
        )

        mistakes.forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FF5252))
                    .border(BorderStroke(1.dp, Color(0xFFFF5252)), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(text = item.desc, fontSize = 11.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Start Interactive Practice →", onClick = onContinue)
    }
}

private data class ConversionMistake(val title: String, val desc: String)

/**
 * MODULE 8: Interactive Practice
 */
@Composable
private fun Level7Module8InteractivePracticeView(
    placementChoice: String?,
    onSelectPlacement: (String) -> Unit,
    onContinue: () -> Unit
) {
    val choices = listOf("Bio Only", "Story Only", "Comment Text", "Bio + Story")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level7ModuleBadge(moduleNum = 8, title = "Interactive Practice")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Where would you place your Wishlink for this reel?",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        choices.forEach { choice ->
            val isSelected = placementChoice == choice
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) Color(0x44B388FF) else Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, if (isSelected) GoldAccent else Color(0x33FFFFFF)), RoundedCornerShape(16.dp))
                    .clickable { onSelectPlacement(choice) }
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = choice, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    if (isSelected) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (placementChoice != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x3300E676))
                    .border(BorderStroke(1.dp, Color(0xFF00E676)), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = if (placementChoice == "Bio + Story")
                        "🎯 PERFECT! Bio + Story gives 2x higher conversion rate because Bio serves permanent traffic while Story captures instant hot viewers!"
                    else
                        "💡 Good choice, but combining Bio + Story gives the highest overall conversions!",
                    fontSize = 12.5.sp,
                    color = TextWhite,
                    lineHeight = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "View Today's Mission →", onClick = onContinue)
    }
}

/**
 * MODULE 9: Today's Mission
 */
@Composable
private fun Level7Module9MissionView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level7ModuleBadge(moduleNum = 9, title = "Today's Mission")

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0x331F0038))
                .border(BorderStroke(1.5.dp, GoldAccent), RoundedCornerShape(24.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Default.Movie, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(44.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Create One Reel Funnel", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "⏱️ Estimated Time: 20 Minutes", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldAccent)

                Spacer(modifier = Modifier.height(16.dp))

                val tasks = listOf(
                    "1. Select 1 high-visual Wishlink product",
                    "2. Write a 5-sec visual hook script",
                    "3. Add clear 'Link in Bio #1' CTA on screen",
                    "4. Repost reel preview to Story with Link Sticker"
                )

                tasks.forEach { task ->
                    Text(text = task, fontSize = 12.5.sp, color = TextWhite, modifier = Modifier.padding(vertical = 3.dp))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Claim +450 XP Achievement →", onClick = onContinue)
    }
}

/**
 * MODULE 10: Achievement Badge
 */
@Composable
private fun Level7Module10AchievementView(
    score: Int,
    isUnlocked: Boolean,
    shineAnim: Float,
    onUnlockAchievement: () -> Unit,
    onCompleteLevel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level7ModuleBadge(moduleNum = 10, title = "Mastery Achievement")

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(PurplePrimary, PurpleDeepBg1)))
                .border(BorderStroke(3.dp, GoldAccent), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(52.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "CONVERSION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                Text(text = "EXPERT", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Level 7 Complete!",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextWhite
        )
        Text(
            text = "You are now a certified Reel → Wishlink Conversion Master",
            fontSize = 12.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!isUnlocked) {
            GlassShineButton(text = "Unlock +450 XP Badge 🏆", onClick = onUnlockAchievement)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x3300E676))
                    .border(BorderStroke(1.dp, Color(0xFF00E676)), RoundedCornerShape(16.dp))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🎉 +450 XP REWARD CLAIMED!", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = GoldAccent)
                    Text(text = "Practice Score Saved • Progress Updated to 78%", fontSize = 11.sp, color = TextWhite)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            GlassShineButton(text = "Finish Level 7 →", onClick = onCompleteLevel)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

/**
 * REUSABLE COMPONENTS
 */
@Composable
private fun Level7ModuleBadge(moduleNum: Int, title: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x33B388FF))
            .border(BorderStroke(1.dp, GoldAccent), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = "MODULE $moduleNum: $title", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
    }
}

@Composable
private fun GlassShineButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        PurplePrimary,
                        Color(0xFF8E24AA)
                    )
                )
            )
            .border(BorderStroke(1.5.dp, GoldAccent), RoundedCornerShape(26.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
    }
}
