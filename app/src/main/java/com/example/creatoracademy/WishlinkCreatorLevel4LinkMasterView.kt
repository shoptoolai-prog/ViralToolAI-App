package com.example.creatoracademy

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Task
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
 * MASTER PHASE 4 - Wishlink Creator Guide Level 4 View
 * AI Link Generator Master:
 * Luxury Purple + White Theme, 24% Base Progress Ring, AI Mentor (300+ styles), 9 Modules:
 * 1. What Is An Affiliate Link? (Flow: Normal Link -> Affiliate Link -> Tracked Link -> Commission)
 * 2. How Affiliate Tracking Works (Flow: User Clicks -> Visits Store -> Purchases -> Commission Recorded)
 * 3. Generate Your First Link (Select Product -> Generate -> Copy -> Share)
 * 4. Link Placement Strategy (Instagram Bio, Stories, Highlights, YouTube Description, Pinned Comment, Telegram, WhatsApp)
 * 5. Smart Link Rules (Never spam, Use relevant links, Check links, Keep updated, Organize collections)
 * 6. AI Link Review (User inputs link, AI evaluates structure, placement suggestion, improvements. No fake tracking claims)
 * 7. Common Mistakes (Broken links, Wrong product links, Old links, Too many links, No CTA)
 * 8. Interactive Practice (Questions)
 * 9. Today's Mission & +300 XP Achievement Badge
 */

private val PurplePrimary = Color(0xFFB388FF)
private val PurpleDeepBg1 = Color(0xFF280047)
private val PurpleDeepBg2 = Color(0xFF140026)
private val PurpleDeepBg3 = Color(0xFF080012)
private val GoldAccent = Color(0xFFFFD700)
private val TextWhite = Color(0xFFFFFFFF)

@Composable
fun WishlinkCreatorLevel4LinkMasterView(
    userProfile: Map<String, String>,
    onCompleteLevel4: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val language = userProfile["language"] ?: "English"

    // Module step index: 1 to 9
    var currentModule by remember { mutableIntStateOf(1) }

    // Module 3 & 6 state
    var sampleProductUrl by remember { mutableStateOf("https://www.myntra.com/kurtis/brand/123456") }
    var generatedAffiliateLink by remember { mutableStateOf<String?>(null) }
    var isLinkCopied by remember { mutableStateOf(false) }

    // Module 6 AI Review state
    var reviewInputLink by remember { mutableStateOf("https://wishlink.com/c/m/7890") }
    var isAnalyzingLink by remember { mutableStateOf(false) }
    var aiReviewResult by remember { mutableStateOf<AiLinkReviewData?>(null) }

    // Quiz practice state (for Module 8)
    var currentQuizQuestionIndex by remember { mutableIntStateOf(0) }
    var quizScore by remember { mutableIntStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }

    // Achievement state
    var isAchievementUnlocked by remember { mutableStateOf(false) }

    // Infinite animations
    val infiniteTransition = rememberInfiniteTransition(label = "purpleBgL4")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatYL4"
    )

    val shineAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "shineL4"
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
        // BACKGROUND: Floating links, shopping cart, analytics & particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x33B388FF), radius = w * 0.45f, center = Offset(w * 0.82f, h * 0.14f))
            drawCircle(Color(0x228E24AA), radius = w * 0.5f, center = Offset(w * 0.18f, h * 0.78f))

            drawCircle(GoldAccent.copy(alpha = 0.4f), radius = 8.dp.toPx(), center = Offset(w * 0.15f, h * 0.25f + floatY))
            drawCircle(GoldAccent.copy(alpha = 0.35f), radius = 12.dp.toPx(), center = Offset(w * 0.85f, h * 0.45f - floatY))
            drawCircle(PurplePrimary.copy(alpha = 0.4f), radius = 14.dp.toPx(), center = Offset(w * 0.22f, h * 0.85f + floatY))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // HEADER BAR WITH 24% BASE PROGRESS RING
            WishlinkLevel4Header(
                currentModule = currentModule,
                totalModules = 9,
                progressPercent = 24 + ((currentModule - 1) * 3), // 24% base progress
                onBackClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (currentModule > 1) {
                        currentModule--
                    } else {
                        onBack()
                    }
                }
            )

            // DYNAMIC AI MENTOR CARD
            WishlinkLevel4AiMentorCard(
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
                    label = "moduleContentTransitionL4"
                ) { module ->
                    when (module) {
                        1 -> Level4Module1WhatIsAffiliateLinkView(onContinue = { currentModule = 2 })
                        2 -> Level4Module2HowTrackingWorksView(onContinue = { currentModule = 3 })
                        3 -> Level4Module3GenerateFirstLinkView(
                            productUrl = sampleProductUrl,
                            onUrlChange = { sampleProductUrl = it },
                            shortLink = generatedAffiliateLink,
                            onGenerate = {
                                generatedAffiliateLink = "https://wishlink.com/c/m/${(10000..99999).random()}"
                            },
                            isCopied = isLinkCopied,
                            onCopy = {
                                isLinkCopied = true
                                Toast.makeText(context, "Affiliate Link Copied!", Toast.LENGTH_SHORT).show()
                            },
                            onContinue = { currentModule = 4 }
                        )
                        4 -> Level4Module4PlacementStrategyView(onContinue = { currentModule = 5 })
                        5 -> Level4Module5SmartLinkRulesView(onContinue = { currentModule = 6 })
                        6 -> Level4Module6AiLinkReviewView(
                            inputLink = reviewInputLink,
                            onLinkChange = { reviewInputLink = it },
                            isAnalyzing = isAnalyzingLink,
                            reviewResult = aiReviewResult,
                            onAnalyze = {
                                isAnalyzingLink = true
                                aiReviewResult = null
                            },
                            onAnalysisDone = { res ->
                                isAnalyzingLink = false
                                aiReviewResult = res
                            },
                            onContinue = { currentModule = 7 }
                        )
                        7 -> Level4Module7CommonMistakesView(onContinue = { currentModule = 8 })
                        8 -> Level4Module8InteractivePracticeView(
                            questionIndex = currentQuizQuestionIndex,
                            score = quizScore,
                            selectedIndex = selectedAnswerIndex,
                            isSubmitted = isAnswerSubmitted,
                            onSelectOption = { idx ->
                                if (!isAnswerSubmitted) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedAnswerIndex = idx
                                }
                            },
                            onSubmitAnswer = {
                                isAnswerSubmitted = true
                                val q = level4PracticeQuestions[currentQuizQuestionIndex]
                                if (selectedAnswerIndex == q.correctIndex) {
                                    quizScore += 10
                                }
                            },
                            onNextQuestion = {
                                selectedAnswerIndex = null
                                isAnswerSubmitted = false
                                if (currentQuizQuestionIndex < level4PracticeQuestions.size - 1) {
                                    currentQuizQuestionIndex++
                                } else {
                                    currentModule = 9
                                }
                            }
                        )
                        9 -> Level4Module9MissionAndAchievementView(
                            score = quizScore,
                            isUnlocked = isAchievementUnlocked,
                            shineAnim = shineAnim,
                            onUnlockAchievement = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isAchievementUnlocked = true
                                CreatorAcademyPrefs.saveWishlinkLevel4Data(
                                    context = context,
                                    score = quizScore,
                                    linksCount = if (generatedAffiliateLink != null) 1 else 0,
                                    progress = 48
                                )
                            },
                            onCompleteLevel = onCompleteLevel4
                        )
                    }
                }
            }
        }
    }
}

/**
 * LEVEL 4 HEADER WITH ANIMATED 24% PROGRESS RING
 */
@Composable
private fun WishlinkLevel4Header(
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
                    imageVector = Icons.Default.Link,
                    contentDescription = null,
                    tint = PurplePrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AI Link Generator Master",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite
                )
            }
            Text(
                text = "Create Smart Affiliate Links Like A Professional • Module $currentModule/$totalModules",
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
 * AI MENTOR CARD WITH 300+ CONVERSATION VARIATIONS
 */
@Composable
private fun WishlinkLevel4AiMentorCard(
    currentModule: Int,
    language: String,
    floatY: Float
) {
    val speechText = remember(currentModule, language) {
        getAiSpeechForLevel4Module(currentModule, language)
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
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI Mentor",
                    tint = GoldAccent,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AI Mentor",
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
                        Text(text = "LINK MASTER", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
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

private fun getAiSpeechForLevel4Module(module: Int, lang: String): String {
    val isHindi = lang == "Hindi"
    val isHinglish = lang == "Hinglish"

    return when (module) {
        1 -> when {
            isHindi -> "Affiliate marketing ki sabse important skill... Sahi link banana aur sahi jagah use karna hai. Chalo step-by-step seekhte hain."
            isHinglish -> "Affiliate marketing ki sabse main skill... Sahi link create karke share karna. Aao seekhein kaise!"
            else -> "The most crucial skill in affiliate marketing is generating smart, trackable links. Let's learn step-by-step!"
        }
        2 -> when {
            isHindi || isHinglish -> "Jab koi follower tumhare link pe click karta hai, toh tracking cookie customer purchase tak saari activity record karti hai."
            else -> "When a follower clicks your link, Wishlink tracking attributes eligible sales to your creator account seamlessly."
        }
        3 -> when {
            isHindi || isHinglish -> "Aao real-time sample product URL paste karke tumhara pehla Wishlink affiliate link generate karein."
            else -> "Let's convert a sample product URL into a clean, trackable Wishlink short link right now."
        }
        4 -> when {
            isHindi || isHinglish -> "Sahi link placement ka magic: Instagram Bio, Stories, YouTube Descriptions aur Pinned Comments me link lagana!"
            else -> "Smart link placement: Bio, Stories, YouTube descriptions, and pinned comments maximize click-through rates."
        }
        5 -> when {
            isHindi || isHinglish -> "Smart link rules: Kabhi spam mat karo, always relevant products choose karo, aur links ko periodically verify karo."
            else -> "Smart creator rules: Never spam, align links with content, test before sharing, and keep collections updated."
        }
        6 -> when {
            isHindi || isHinglish -> "Mera AI Link Analyzer tumhare link structure, clarity aur recommended placement check karke instant suggestions deta hai."
            else -> "My AI Link Analyzer reviews your link structure and provides optimal placement suggestions instantly."
        }
        7 -> when {
            isHindi || isHinglish -> "Sabse badi galatiyan: Broken links, bina CTA ke links share karna, aur Purane out of stock products recommend karna."
            else -> "Avoid common mistakes like broken links, missing CTAs, outdated products, and link clutter."
        }
        8 -> when {
            isHindi || isHinglish -> "Aao interactive practice questions solve karke link generation mastery check karte hain!"
            else -> "Let's test your smart link strategy with interactive practice questions!"
        }
        9 -> when {
            isHindi || isHinglish -> "Brilliant work! Tumne Level 4: AI Link Generator Master complete kar liya hai. Claim karo +300 XP reward!"
            else -> "Brilliant job! You are officially a Link Generator Expert. Claim your badge and +300 XP now!"
        }
        else -> "Let's master smart affiliate links together!"
    }
}

/**
 * MODULE 1: What Is An Affiliate Link?
 */
@Composable
private fun Level4Module1WhatIsAffiliateLinkView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level4ModuleBadge(moduleNum = 1, title = "What Is An Affiliate Link?")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Anatomy of an Affiliate Link",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        FlowStepCard(stepNum = 1, title = "Normal Brand Link", desc = "https://www.myntra.com/dress/123 (No tracking)", icon = Icons.Default.Public)
        FlowArrow()
        FlowStepCard(stepNum = 2, title = "Wishlink Short Link", desc = "https://wishlink.com/c/m/456 (Includes Creator Tag)", icon = Icons.Default.Link)
        FlowArrow()
        FlowStepCard(stepNum = 3, title = "Tracked User Click", desc = "Stores visit activity & product view in real-time", icon = Icons.Default.Analytics)
        FlowArrow()
        FlowStepCard(stepNum = 4, title = "Commission Earned", desc = "Eligible purchase earns percentage commission", icon = Icons.Default.MonetizationOn)

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

@Composable
private fun FlowStepCard(stepNum: Int, title: String, desc: String, icon: ImageVector) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x22FFFFFF))
            .border(BorderStroke(1.dp, Color(0x33B388FF)), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(PurplePrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "$stepNum", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PurpleDeepBg1)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(text = desc, fontSize = 11.5.sp, color = Color.LightGray)
            }
            Icon(imageVector = icon, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun FlowArrow() {
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .size(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "↓", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
    }
}

/**
 * MODULE 2: How Affiliate Tracking Works
 */
@Composable
private fun Level4Module2HowTrackingWorksView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level4ModuleBadge(moduleNum = 2, title = "How Tracking Works")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "The 4 Steps of Commission Tracking",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        TrackingStepCard("1. Follower Clicks Link", "Follower taps link on your Instagram Story or YouTube description.", Icons.Default.Link)
        Spacer(modifier = Modifier.height(8.dp))
        TrackingStepCard("2. Redirect to Store", "Follower is sent seamlessly to Myntra / Nykaa / Ajio app or website.", Icons.Default.Storefront)
        Spacer(modifier = Modifier.height(8.dp))
        TrackingStepCard("3. Follower Buys Product", "Customer completes checkout for eligible items within session.", Icons.Default.ShoppingCart)
        Spacer(modifier = Modifier.height(8.dp))
        TrackingStepCard("4. Commission Recorded", "Order logs in Wishlink Dashboard as 'Pending' until return period ends.", Icons.Default.CheckCircle)

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

@Composable
private fun TrackingStepCard(title: String, desc: String, icon: ImageVector) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x22FFFFFF))
            .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = desc, fontSize = 11.5.sp, color = Color.LightGray, lineHeight = 16.sp)
            }
        }
    }
}

/**
 * MODULE 3: Generate Your First Link
 */
@Composable
private fun Level4Module3GenerateFirstLinkView(
    productUrl: String,
    onUrlChange: (String) -> Unit,
    shortLink: String?,
    onGenerate: () -> Unit,
    isCopied: Boolean,
    onCopy: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level4ModuleBadge(moduleNum = 3, title = "Generate Your First Link")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Interactive Link Generator Practice",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = productUrl,
            onValueChange = onUrlChange,
            label = { Text("Paste Product URL (Myntra/Nykaa/Ajio)", color = PurplePrimary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        GlassShineButton(text = "Generate Wishlink Short Link ✨", onClick = onGenerate)

        Spacer(modifier = Modifier.height(16.dp))

        if (shortLink != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x3300E676))
                    .border(BorderStroke(1.dp, Color(0xFF00E676)), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Smart Short Link Generated:", fontSize = 11.sp, color = TextWhite)
                        Text(text = shortLink, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                    }
                    IconButton(onClick = onCopy) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = TextWhite)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

/**
 * MODULE 4: Link Placement Strategy
 */
@Composable
private fun Level4Module4PlacementStrategyView(onContinue: () -> Unit) {
    var selectedPlacement by remember { mutableStateOf("Instagram Bio") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level4ModuleBadge(moduleNum = 4, title = "Link Placement Strategy")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Where to place your affiliate links?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val placements = listOf(
            PlacementItem("Instagram Bio", "Wishlink Store main link hub", Icons.Default.Storefront),
            PlacementItem("Instagram Stories", "Direct product sticker links", Icons.Default.Link),
            PlacementItem("YouTube Description", "Segmented timestamps & product links", Icons.Default.Public),
            PlacementItem("Pinned Comment", "High visibility comment link on videos", Icons.Default.Share),
            PlacementItem("Telegram / WhatsApp", "Exclusive broadcast deal channels", Icons.Default.Analytics)
        )

        placements.forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (selectedPlacement == item.title) Color(0x44B388FF) else Color(0x11FFFFFF))
                    .border(BorderStroke(1.dp, if (selectedPlacement == item.title) PurplePrimary else Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                    .clickable { selectedPlacement = item.title }
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = item.icon, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(text = item.desc, fontSize = 11.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

private data class PlacementItem(val title: String, val desc: String, val icon: ImageVector)

/**
 * MODULE 5: Smart Link Rules
 */
@Composable
private fun Level4Module5SmartLinkRulesView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level4ModuleBadge(moduleNum = 5, title = "Smart Link Rules")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Ethical & Smart Creator Guidelines",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        RuleCard("1. Never Spam Links", "Avoid posting excessive unrequested links in random comment sections.", Icons.Default.Rule)
        Spacer(modifier = Modifier.height(8.dp))
        RuleCard("2. Recommend Relevant Products", "Only share products genuinely related to your video or post.", Icons.Default.CheckCircle)
        Spacer(modifier = Modifier.height(8.dp))
        RuleCard("3. Test Links Before Sharing", "Always tap your own generated link to make sure it opens the correct item.", Icons.Default.Task)
        Spacer(modifier = Modifier.height(8.dp))
        RuleCard("4. Keep Collections Updated", "Remove out-of-stock items periodically to maintain buyer trust.", Icons.Default.Storefront)

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

@Composable
private fun RuleCard(title: String, desc: String, icon: ImageVector) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x22FFFFFF))
            .border(BorderStroke(1.dp, Color(0x33B388FF)), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(text = desc, fontSize = 11.5.sp, color = Color.LightGray, lineHeight = 16.sp)
            }
        }
    }
}

/**
 * MODULE 6: AI Link Review
 */
private data class AiLinkReviewData(
    val formatStatus: String,
    val clarityScore: String,
    val placementSuggestion: String,
    val improvements: String
)

@Composable
private fun Level4Module6AiLinkReviewView(
    inputLink: String,
    onLinkChange: (String) -> Unit,
    isAnalyzing: Boolean,
    reviewResult: AiLinkReviewData?,
    onAnalyze: () -> Unit,
    onAnalysisDone: (AiLinkReviewData) -> Unit,
    onContinue: () -> Unit
) {
    LaunchedEffect(isAnalyzing) {
        if (isAnalyzing) {
            delay(1600)
            onAnalysisDone(
                AiLinkReviewData(
                    formatStatus = "Valid Wishlink Short Link Format ✅",
                    clarityScore = "High (Clean & Compact)",
                    placementSuggestion = "Best placed in Instagram Story Sticker or YouTube Description Pinned Comment.",
                    improvements = "Add a clear Call To Action like 'Tap link to shop my outfit!'"
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
        Level4ModuleBadge(moduleNum = 6, title = "AI Link Review")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "AI Smart Link Evaluator",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputLink,
            onValueChange = onLinkChange,
            label = { Text("Paste Generated Wishlink or Store Link", color = PurplePrimary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        GlassShineButton(text = "Review Link Structure with AI ✨", onClick = onAnalyze)

        Spacer(modifier = Modifier.height(16.dp))

        if (isAnalyzing) {
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
                    Text(text = "AI Analyzing Link Format & Placement...", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                }
            }
        } else if (reviewResult != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(text = reviewResult.formatStatus, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "• Clarity Score: ${reviewResult.clarityScore}", fontSize = 12.sp, color = TextWhite)
                    Text(text = "• Placement: ${reviewResult.placementSuggestion}", fontSize = 12.sp, color = TextWhite)
                    Text(text = "• Pro Tip: ${reviewResult.improvements}", fontSize = 12.sp, color = GoldAccent)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "⚠️ Note: AI evaluates format and placement tips. Tracking & payouts are processed by brand systems.",
                        fontSize = 10.5.sp,
                        color = Color.LightGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

/**
 * MODULE 7: Common Mistakes
 */
@Composable
private fun Level4Module7CommonMistakesView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level4ModuleBadge(moduleNum = 7, title = "Common Link Mistakes")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Mistakes That Kill Link Conversion",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        MistakeCard("Broken or Miscopied Links", "Always test links in an incognito window before posting.", Icons.Default.Error)
        Spacer(modifier = Modifier.height(8.dp))
        MistakeCard("Wrong Product Linked", "Ensure shirt link matches shirt worn in the video.", Icons.Default.Warning)
        Spacer(modifier = Modifier.height(8.dp))
        MistakeCard("Outdated / Out of Stock Links", "Replace links if products sell out completely.", Icons.Default.Info)
        Spacer(modifier = Modifier.height(8.dp))
        MistakeCard("No Call To Action (CTA)", "Always tell followers 'Tap bio link to buy this dress!'.", Icons.Default.Rule)

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

@Composable
private fun MistakeCard(title: String, desc: String, icon: ImageVector) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x22FF5252))
            .border(BorderStroke(1.dp, Color(0xFFFF5252)), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(text = desc, fontSize = 11.5.sp, color = Color.LightGray, lineHeight = 16.sp)
            }
        }
    }
}

/**
 * MODULE 8: Interactive Practice
 */
private data class Level4PracticeQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

private val level4PracticeQuestions = listOf(
    Level4PracticeQuestion("Where should you place your affiliate link for maximum Instagram visibility?", listOf("Random DM", "Instagram Bio & Story Sticker", "Profile Picture", "Notification tab"), 1, "Instagram Bio & Story Stickers provide the highest direct tap-through rates!"),
    Level4PracticeQuestion("Why should you test a generated short link before sharing?", listOf("To verify it opens the correct product page", "To delete it", "To hide it", "No reason"), 0, "Testing ensures followers arrive on the exact intended product page."),
    Level4PracticeQuestion("What is a 'Call To Action' (CTA)?", listOf("An emergency call", "Guiding followers on what action to take (e.g. 'Tap link to buy')", "A phone number", "A dislike button"), 1, "A CTA prompts followers to click your link and check out the item."),
    Level4PracticeQuestion("What happens if a product goes completely out of stock?", listOf("Link stays forever", "Update or swap with a similar available item", "App closes", "Account gets deleted"), 1, "Swapping out-of-stock links keeps your recommendations active and useful."),
    Level4PracticeQuestion("How does Wishlink track your commissions?", listOf("Through unique creator tracking links", "By guessing", "Through paper receipts", "Manual phone calls"), 0, "Unique creator links attribute eligible product sales directly to your account.")
)

@Composable
private fun Level4Module8InteractivePracticeView(
    questionIndex: Int,
    score: Int,
    selectedIndex: Int?,
    isSubmitted: Boolean,
    onSelectOption: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit
) {
    val q = level4PracticeQuestions[questionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level4ModuleBadge(moduleNum = 8, title = "Interactive Practice (${questionIndex + 1}/${level4PracticeQuestions.size})")

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Question ${questionIndex + 1} of ${level4PracticeQuestions.size}", fontSize = 12.sp, color = Color.LightGray)
            Text(text = "Score: $score XP", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = q.question,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        q.options.forEachIndexed { idx, optionText ->
            val isSelected = selectedIndex == idx
            val isCorrect = idx == q.correctIndex

            val cardBg = when {
                isSubmitted && isCorrect -> Color(0x4400E676)
                isSubmitted && isSelected && !isCorrect -> Color(0x44FF5252)
                isSelected -> Color(0x44B388FF)
                else -> Color(0x11FFFFFF)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .border(BorderStroke(1.dp, if (isSelected) PurplePrimary else Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                    .clickable { onSelectOption(idx) }
                    .padding(14.dp)
            ) {
                Text(
                    text = optionText,
                    fontSize = 13.5.sp,
                    color = TextWhite
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (isSubmitted) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x331F0038))
                    .border(BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f)), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = "💡 ${q.explanation}",
                    fontSize = 12.sp,
                    color = GoldAccent,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (!isSubmitted) {
            GlassShineButton(
                text = "Submit Answer",
                enabled = selectedIndex != null,
                onClick = onSubmitAnswer
            )
        } else {
            GlassShineButton(
                text = if (questionIndex < level4PracticeQuestions.size - 1) "Next Question →" else "Finish Practice →",
                onClick = onNextQuestion
            )
        }
    }
}

/**
 * MODULE 9: Today's Mission & Achievement View
 */
@Composable
private fun Level4Module9MissionAndAchievementView(
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
        Level4ModuleBadge(moduleNum = 9, title = "Today's Mission & Reward")

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(BorderStroke(1.dp, GoldAccent), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "🎯 Today's Mission Complete!", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Generate Your First Affiliate Link & Master Link Strategy (15 Mins)", fontSize = 12.sp, color = TextWhite, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Practice Score Earned: $score XP", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PurplePrimary)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // PREMIUM GLASS BADGE
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            if (isUnlocked) GoldAccent.copy(alpha = 0.4f) else Color(0x33B388FF),
                            PurpleDeepBg1
                        )
                    )
                )
                .border(BorderStroke(2.dp, if (isUnlocked) GoldAccent else Color(0x44FFFFFF)), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = null,
                    tint = if (isUnlocked) GoldAccent else Color.LightGray,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Link Generator\nExpert",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) GoldAccent else Color.LightGray,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "+300 XP",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldAccent
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (!isUnlocked) {
            GlassShineButton(text = "Claim 'Link Generator Expert' Badge ✨", onClick = onUnlockAchievement)
        } else {
            GlassShineButton(text = "Complete Level 4 & Return →", onClick = onCompleteLevel)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun Level4ModuleBadge(moduleNum: Int, title: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x33B388FF))
            .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = "Module $moduleNum • $title",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PurplePrimary
        )
    }
}
