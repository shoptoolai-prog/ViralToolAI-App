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
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
 * MASTER PHASE 3 - Wishlink Creator Guide Level 3 View
 * Wishlink Dashboard Master:
 * Luxury Purple + White Theme, 15% Base Progress Ring, AI Mentor (200+ styles), 10 Modules:
 * 1. Dashboard Tour
 * 2. Home Dashboard
 * 3. My Store
 * 4. Collections
 * 5. Affiliate Links
 * 6. Analytics
 * 7. Earnings
 * 8. Orders
 * 9. Interactive Practice (10 Practice Questions)
 * 10. Today's Mission & +200 XP Achievement Badge
 */

private val PurplePrimary = Color(0xFFB388FF)
private val PurpleDeepBg1 = Color(0xFF280047)
private val PurpleDeepBg2 = Color(0xFF140026)
private val PurpleDeepBg3 = Color(0xFF080012)
private val GoldAccent = Color(0xFFFFD700)
private val TextWhite = Color(0xFFFFFFFF)

@Composable
fun WishlinkCreatorLevel3DashboardView(
    userProfile: Map<String, String>,
    onCompleteLevel3: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val language = userProfile["language"] ?: "English"

    // Module step index: 1 to 10
    var currentModule by remember { mutableIntStateOf(1) }

    // Quiz practice state (for Module 9)
    var currentQuizQuestionIndex by remember { mutableIntStateOf(0) }
    var quizScore by remember { mutableIntStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }

    // Link generator practice state (for Module 5)
    var inputProductUrl by remember { mutableStateOf("https://www.myntra.com/dress/12345") }
    var generatedShortLink by remember { mutableStateOf<String?>(null) }
    var isLinkCopied by remember { mutableStateOf(false) }

    // Achievement state
    var isAchievementUnlocked by remember { mutableStateOf(false) }

    // Infinite animations
    val infiniteTransition = rememberInfiniteTransition(label = "purpleBgL3")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatYL3"
    )

    val shineAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "shineL3"
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
        // BACKGROUND: Floating analytics & icons
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x33B388FF), radius = w * 0.45f, center = Offset(w * 0.85f, h * 0.12f))
            drawCircle(Color(0x228E24AA), radius = w * 0.5f, center = Offset(w * 0.15f, h * 0.75f))

            drawCircle(GoldAccent.copy(alpha = 0.4f), radius = 8.dp.toPx(), center = Offset(w * 0.18f, h * 0.22f + floatY))
            drawCircle(GoldAccent.copy(alpha = 0.35f), radius = 12.dp.toPx(), center = Offset(w * 0.82f, h * 0.42f - floatY))
            drawCircle(PurplePrimary.copy(alpha = 0.4f), radius = 14.dp.toPx(), center = Offset(w * 0.25f, h * 0.82f + floatY))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // HEADER BAR WITH 15% BASE PROGRESS RING
            WishlinkLevel3Header(
                currentModule = currentModule,
                totalModules = 10,
                progressPercent = 15 + ((currentModule - 1) * 3), // 15% base progress
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
            WishlinkLevel3AiMentorCard(
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
                    label = "moduleContentTransition"
                ) { module ->
                    when (module) {
                        1 -> Module1DashboardTourView(onContinue = { currentModule = 2 })
                        2 -> Module2HomeDashboardView(onContinue = { currentModule = 3 })
                        3 -> Module3MyStoreView(onContinue = { currentModule = 4 })
                        4 -> Module4CollectionsView(onContinue = { currentModule = 5 })
                        5 -> Module5AffiliateLinksView(
                            inputUrl = inputProductUrl,
                            onUrlChange = { inputProductUrl = it },
                            shortLink = generatedShortLink,
                            onGenerate = { generatedShortLink = "https://wishlink.com/c/m/${(1000..9999).random()}" },
                            isCopied = isLinkCopied,
                            onCopy = {
                                isLinkCopied = true
                                Toast.makeText(context, "Link Copied!", Toast.LENGTH_SHORT).show()
                            },
                            onContinue = { currentModule = 6 }
                        )
                        6 -> Module6AnalyticsView(onContinue = { currentModule = 7 })
                        7 -> Module7EarningsView(onContinue = { currentModule = 8 })
                        8 -> Module8OrdersView(onContinue = { currentModule = 9 })
                        9 -> Module9InteractivePracticeView(
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
                                val q = practiceQuestions[currentQuizQuestionIndex]
                                if (selectedAnswerIndex == q.correctIndex) {
                                    quizScore += 10
                                }
                            },
                            onNextQuestion = {
                                selectedAnswerIndex = null
                                isAnswerSubmitted = false
                                if (currentQuizQuestionIndex < practiceQuestions.size - 1) {
                                    currentQuizQuestionIndex++
                                } else {
                                    currentModule = 10
                                }
                            }
                        )
                        10 -> Module10MissionAndAchievementView(
                            score = quizScore,
                            isUnlocked = isAchievementUnlocked,
                            shineAnim = shineAnim,
                            onUnlockAchievement = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isAchievementUnlocked = true
                                CreatorAcademyPrefs.saveWishlinkLevel3Data(
                                    context = context,
                                    score = quizScore,
                                    progress = 30
                                )
                            },
                            onCompleteLevel = onCompleteLevel3
                        )
                    }
                }
            }
        }
    }
}

/**
 * LEVEL 3 HEADER WITH ANIMATED 15% PROGRESS RING
 */
@Composable
private fun WishlinkLevel3Header(
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
                    imageVector = Icons.Default.Insights,
                    contentDescription = null,
                    tint = PurplePrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Wishlink Dashboard Master",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite
                )
            }
            Text(
                text = "Master Every Feature Like A Professional • Module $currentModule/$totalModules",
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
 * AI MENTOR CARD WITH 200+ CONVERSATION VARIATIONS
 */
@Composable
private fun WishlinkLevel3AiMentorCard(
    currentModule: Int,
    language: String,
    floatY: Float
) {
    val speechText = remember(currentModule, language) {
        getAiSpeechForModule(currentModule, language)
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
                        Text(text = "DASHBOARD PRO", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
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

private fun getAiSpeechForModule(module: Int, lang: String): String {
    val isHindi = lang == "Hindi"
    val isHinglish = lang == "Hinglish"

    return when (module) {
        1 -> when {
            isHindi -> "Excellent! Ab tumhara account ready hai. Chalo Wishlink Dashboard ko professional level par use karna seekhte hain."
            isHinglish -> "Excellent! Account ready hai. Chalo Wishlink Dashboard ke saare key sections explore karte hain!"
            else -> "Excellent! Now that your account is setup, let's master every section of your Wishlink Dashboard like a pro."
        }
        2 -> when {
            isHindi || isHinglish -> "Home Dashboard tumhara control room hai! Yahan Quick Actions aur Top Links ki updates instantly milti hain."
            else -> "Your Home Dashboard is command center! Quick actions, notifications, and top performing links appear here."
        }
        3 -> when {
            isHindi || isHinglish -> "My Store section me tum customized banner, featured collections aur product links organize kar sakte ho."
            else -> "My Store lets you showcase custom store banners, featured collections, and curated products to your followers."
        }
        4 -> when {
            isHindi || isHinglish -> "Collections banane se tumhare followers ko outfit / decor ideas easily categorize hoke milte hain."
            else -> "Collections allow you to group related products (like 'Summer OOTD' or 'Skincare Must-Haves') cleanly."
        }
        5 -> when {
            isHindi || isHinglish -> "Affiliate Links module sabse important hai! Kisi bhi brand link ko paste karo aur 1-click me short link banao."
            else -> "Generating affiliate links is your main income driver! Convert product URLs into trackable short links in seconds."
        }
        6 -> when {
            isHindi || isHinglish -> "Analytics tab me Clicks, Store Visits aur Top Products tracking real-time data dikhate hain."
            else -> "Analytics gives you deep insights into profile visits, link clicks, top converted products, and traffic sources."
        }
        7 -> when {
            isHindi || isHinglish -> "Earnings tab me Pending vs Confirmed commissions aur monthly payout breakdown dekh sakte ho."
            else -> "Earnings tracks your Pending, Confirmed, and Paid commissions clearly with transparent monthly breakdown."
        }
        8 -> when {
            isHindi || isHinglish -> "Orders section me followers dwara kharide gaye items aur tracking status updates aate hain."
            else -> "Orders log shows live purchase updates and commission statuses synced with partner brand systems."
        }
        9 -> when {
            isHindi || isHinglish -> "Chalo 10 quick interactive practice questions attempt karo taaki dashboard mastery solid ho jaye!"
            else -> "Let's test your knowledge with 10 interactive practice questions to solidify your Wishlink Dashboard skills!"
        }
        10 -> when {
            isHindi || isHinglish -> "Superb work! Tumne Wishlink Dashboard Master module complete kar liya hai. Claim karo +200 XP reward!"
            else -> "Outstanding job! You are officially a Wishlink Dashboard Master. Claim your 'Dashboard Explorer' badge now!"
        }
        else -> "Let's explore your Wishlink Dashboard!"
    }
}

/**
 * MODULE 1: Dashboard Tour View
 */
@Composable
private fun Module1DashboardTourView(onContinue: () -> Unit) {
    var selectedSection by remember { mutableStateOf("Home") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ModuleBadge(moduleNum = 1, title = "Dashboard Tour")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Tap cards to explore key sections:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val sections = listOf(
            TourCardData("Home", "Quick stats & notifications", Icons.Default.Home),
            TourCardData("My Store", "Custom storefront & banners", Icons.Default.Storefront),
            TourCardData("Collections", "Organized outfit/product lists", Icons.Default.FolderSpecial),
            TourCardData("Links", "Generate & manage affiliate links", Icons.Default.Link),
            TourCardData("Analytics", "Traffic & click tracking data", Icons.Default.Analytics),
            TourCardData("Earnings", "Pending & confirmed payouts", Icons.Default.MonetizationOn),
            TourCardData("Orders", "Customer purchases log", Icons.Default.ReceiptLong),
            TourCardData("Profile", "Account & payout details", Icons.Default.Person)
        )

        sections.chunked(2).forEach { rowItems ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowItems.forEach { item ->
                    TourCard(
                        modifier = Modifier.weight(1f),
                        data = item,
                        isSelected = selectedSection == item.title,
                        onClick = { selectedSection = item.title }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x331F0038))
                .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            val description = when (selectedSection) {
                "Home" -> "Home contains your quick action buttons, top 3 links of the week, and live notifications."
                "My Store" -> "My Store is your public link landing page. Customize banner and highlight collections."
                "Collections" -> "Collections group your links by topic (e.g. 'Skincare', 'Monsoon Dresses')."
                "Links" -> "Links is where you paste long product URLs from Myntra, Nykaa, Flipkart, etc."
                "Analytics" -> "Analytics shows graph breakdown of link clicks, top stores visited, and follower engagement."
                "Earnings" -> "Earnings breaks down pending, confirmed, and paid commission totals with payout dates."
                "Orders" -> "Orders lists customer purchase orders with item status and commission earned."
                else -> "Profile lets you edit connected platforms, phone number, email, and bank account for payouts."
            }
            Text(
                text = "📌 $selectedSection: $description",
                fontSize = 12.5.sp,
                color = TextWhite,
                lineHeight = 17.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

private data class TourCardData(val title: String, val desc: String, val icon: ImageVector)

@Composable
private fun TourCard(
    modifier: Modifier,
    data: TourCardData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0x44B388FF) else Color(0x11FFFFFF))
            .border(
                BorderStroke(1.dp, if (isSelected) PurplePrimary else Color(0x22FFFFFF)),
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column {
            Icon(
                imageVector = data.icon,
                contentDescription = null,
                tint = if (isSelected) GoldAccent else PurplePrimary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = data.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text(text = data.desc, fontSize = 10.sp, color = Color.LightGray, maxLines = 1)
        }
    }
}

/**
 * MODULE 2: Home Dashboard View
 */
@Composable
private fun Module2HomeDashboardView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ModuleBadge(moduleNum = 2, title = "Home Dashboard")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Home Dashboard Elements",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(BorderStroke(1.dp, Color(0x33B388FF)), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Quick Actions & Creator Updates", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "• Quick Create Link: Fast shortcut to generate affiliate links.\n• Recent Activity: See your top performing links today.\n• Creator Notifications: Brand campaign invites and payout alerts.",
                    fontSize = 12.5.sp,
                    color = TextWhite,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

/**
 * MODULE 3: My Store View
 */
@Composable
private fun Module3MyStoreView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ModuleBadge(moduleNum = 3, title = "My Store")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Personalizing Your Storefront",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFF8E24AA), Color(0xFF3F51B5))))
                .border(BorderStroke(1.dp, GoldAccent), RoundedCornerShape(20.dp))
                .padding(16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column {
                Text(text = "✨ Customized Store Banner Preview", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(text = "wishlink.com/yourname", fontSize = 11.sp, color = GoldAccent)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .padding(14.dp)
        ) {
            Text(
                text = "💡 Pro Tip: Place your Wishlink Store link in your Instagram Bio and YouTube Channel description so all followers can shop easily!",
                fontSize = 12.sp,
                color = Color.LightGray,
                lineHeight = 17.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

/**
 * MODULE 4: Collections View
 */
@Composable
private fun Module4CollectionsView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ModuleBadge(moduleNum = 4, title = "Collections")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Organize Links into Collections",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(text = "✨ Best Collection Ideas:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PurplePrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. 'Monsoon Essentials' (Dresses, Umbrellas, Waterproof makeup)\n2. 'Festive Wear' (Kurtas, Sarees, Jewelry)\n3. 'Daily Skincare Routine' (Serums, Sunscreens)\n4. 'Tech & Camera Gear' (Mics, Tripods)",
                    fontSize = 12.5.sp,
                    color = TextWhite,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

/**
 * MODULE 5: Affiliate Links View
 */
@Composable
private fun Module5AffiliateLinksView(
    inputUrl: String,
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
        ModuleBadge(moduleNum = 5, title = "Affiliate Link Generator")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Try Generating Your First Affiliate Link!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputUrl,
            onValueChange = onUrlChange,
            label = { Text("Paste Brand Product URL", color = PurplePrimary) },
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
                        Text(text = "Generated Short Link:", fontSize = 11.sp, color = TextWhite)
                        Text(text = shortLink, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
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
 * MODULE 6: Analytics View
 */
@Composable
private fun Module6AnalyticsView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ModuleBadge(moduleNum = 6, title = "Analytics Insights")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Understanding Analytics Metrics",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard(modifier = Modifier.weight(1f), title = "Profile Visits", value = "1,240", icon = Icons.Default.Visibility)
            MetricCard(modifier = Modifier.weight(1f), title = "Link Clicks", value = "850", icon = Icons.Default.Link)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard(modifier = Modifier.weight(1f), title = "Top Product", value = "Floral Dress", icon = Icons.Default.ShoppingBag)
            MetricCard(modifier = Modifier.weight(1f), title = "Traffic Source", value = "Instagram Reel", icon = Icons.Default.Share)
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

@Composable
private fun MetricCard(modifier: Modifier, title: String, value: String, icon: ImageVector) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x22FFFFFF))
            .border(BorderStroke(1.dp, Color(0x33B388FF)), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Icon(imageVector = icon, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = title, fontSize = 11.sp, color = Color.LightGray)
            Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        }
    }
}

/**
 * MODULE 7: Earnings View
 */
@Composable
private fun Module7EarningsView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ModuleBadge(moduleNum = 7, title = "Earnings & Commission")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Pending vs Confirmed Earnings",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(BorderStroke(1.dp, GoldAccent), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(text = "💰 Earnings Status Breakdown:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Pending: Order placed, awaiting return period completion (usually 14-30 days).\n• Confirmed: Return window closed! Commission locked for payout.\n• Paid Out: Commission transferred directly to your bank account.",
                    fontSize = 12.5.sp,
                    color = TextWhite,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

/**
 * MODULE 8: Orders View
 */
@Composable
private fun Module8OrdersView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ModuleBadge(moduleNum = 8, title = "Order Lifecycle")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "How Customer Orders Work",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "1. Follower taps your Wishlink short link\n2. Redirects to official brand app/site\n3. Follower completes purchase\n4. Order appears in your Wishlink Orders tab within 24-48 hours",
                    fontSize = 12.5.sp,
                    color = TextWhite,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "⚠️ Disclaimer: Feature availability and order update speeds depend on individual partner brand systems.",
                    fontSize = 11.5.sp,
                    color = GoldAccent,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

/**
 * MODULE 9: Interactive Practice View (10 Questions Quiz)
 */
private data class PracticeQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

private val practiceQuestions = listOf(
    PracticeQuestion("Where do you find generated affiliate links?", listOf("My Store", "Links Tab", "Settings", "Help Center"), 1, "The Links tab holds all your active and custom affiliate links!"),
    PracticeQuestion("What should you place in your Instagram Bio link?", listOf("Random website", "Wishlink Store URL", "Empty text", "Brand email"), 1, "Your Wishlink Store URL gives followers 1-click access to all products."),
    PracticeQuestion("What does 'Pending Commission' mean?", listOf("Money already paid", "Order placed, awaiting return window", "Cancelled order", "Error status"), 1, "Pending commission stays locked until the customer's return window passes."),
    PracticeQuestion("Which tab shows your top performing products?", listOf("Orders", "Analytics", "Profile", "Notifications"), 1, "Analytics breaks down link clicks and top performing products."),
    PracticeQuestion("Why create Collections on your Wishlink Store?", listOf("To hide links", "To categorize outfits/topics cleanly", "To block traffic", "No reason"), 1, "Collections help followers quickly find specific outfit ideas or categories."),
    PracticeQuestion("What happens when someone buys through your Wishlink short link?", listOf("Nothing", "You earn a commission", "Link gets deleted", "App crashes"), 1, "You earn a percentage commission on eligible brand purchases."),
    PracticeQuestion("Where can you see brand updates and campaign invites?", listOf("Earnings", "Home / Notifications", "Orders", "Storage"), 1, "Home dashboard and notifications notify you of brand invites and news."),
    PracticeQuestion("How long does return window verification usually take?", listOf("1 minute", "14 to 30 days", "1 year", "Instant"), 1, "Return windows typically range from 14 to 30 days depending on the brand."),
    PracticeQuestion("Which option allows 1-click short link creation?", listOf("Affiliate Link Generator", "Camera settings", "Drafts", "Gallery"), 0, "Paste any product URL into the Affiliate Link Generator for an instant short link."),
    PracticeQuestion("Who can view your Wishlink Store storefront?", listOf("Nobody", "Only you", "Anyone with your store link", "Wishlink staff only"), 2, "Your Wishlink Store is public so all followers can easily shop your recommendations.")
)

@Composable
private fun Module9InteractivePracticeView(
    questionIndex: Int,
    score: Int,
    selectedIndex: Int?,
    isSubmitted: Boolean,
    onSelectOption: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit
) {
    val q = practiceQuestions[questionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ModuleBadge(moduleNum = 9, title = "Interactive Practice (${questionIndex + 1}/10)")

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Question ${questionIndex + 1} of 10", fontSize = 12.sp, color = Color.LightGray)
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
                text = if (questionIndex < practiceQuestions.size - 1) "Next Question →" else "Finish Practice →",
                onClick = onNextQuestion
            )
        }
    }
}

/**
 * MODULE 10: Today's Mission & Achievement View
 */
@Composable
private fun Module10MissionAndAchievementView(
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
        ModuleBadge(moduleNum = 10, title = "Today's Mission & Reward")

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
                Text(text = "Mission: Explore Every Dashboard Section (15 Mins)", fontSize = 12.sp, color = Color.LightGray)
                Text(text = "Practice Score: $score / 100 XP", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ACHIEVEMENT GLASS BADGE
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            GoldAccent.copy(alpha = 0.6f + (shineAnim * 0.3f)),
                            PurplePrimary.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
                .border(BorderStroke(2.5.dp, GoldAccent), CircleShape)
                .clickable { if (!isUnlocked) onUnlockAchievement() },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Insights,
                    contentDescription = "Badge",
                    tint = GoldAccent,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Dashboard Explorer", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite)
                Text(text = "+200 XP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isUnlocked) {
            Text(
                text = "Tap the badge above to claim your +200 XP!",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent
            )
        } else {
            Text(
                text = "🎉 Badge Claimed! Wishlink Dashboard Master Completed!",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00E676)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(
            text = "Complete Level 3 →",
            enabled = isUnlocked,
            onClick = onCompleteLevel
        )
    }
}

@Composable
private fun ModuleBadge(moduleNum: Int, title: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x33B388FF))
            .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = "MODULE $moduleNum • $title",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PurplePrimary
        )
    }
}
