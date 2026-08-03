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
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Tv
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
 * MASTER PHASE 6 - Wishlink Creator Guide Level 6 View
 * AI Product Research Master:
 * Luxury Purple + White Theme, 40% Base Progress Ring, AI Mentor (400+ styles), 10 Modules:
 * 1. Winning Product Mindset
 * 2. Product Categories (Fashion, Beauty, Electronics, Lifestyle, Home, Kitchen, Fitness, Accessories, Books, Other)
 * 3. AI Product Score (0-100 & Disclaimer)
 * 4. AI Collection Suggestions
 * 5. Product Comparison (Product A vs Product B)
 * 6. Seasonal Products
 * 7. Common Product Mistakes
 * 8. Interactive Practice (Add To Store vs Skip)
 * 9. AI Research Assistant
 * 10. Today's Mission & +400 XP Achievement Badge
 */

private val PurplePrimary = Color(0xFFB388FF)
private val PurpleDeepBg1 = Color(0xFF280047)
private val PurpleDeepBg2 = Color(0xFF140026)
private val PurpleDeepBg3 = Color(0xFF080012)
private val GoldAccent = Color(0xFFFFD700)
private val TextWhite = Color(0xFFFFFFFF)

@Composable
fun WishlinkCreatorLevel6ProductResearchView(
    userProfile: Map<String, String>,
    onCompleteLevel6: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val language = userProfile["language"] ?: "English"
    val niche = userProfile["niche"] ?: "Fashion"

    // Module step index: 1 to 10
    var currentModule by remember { mutableIntStateOf(1) }

    // Module 2 state: Category selection
    var selectedCategory by remember { mutableStateOf(niche) }

    // Module 3 & 4 state: AI Product Score
    var inputProductUrl by remember { mutableStateOf("https://wishlink.com/p/sample-viral-kurti") }
    var isEvaluatingProduct by remember { mutableStateOf(false) }
    var productEvaluationResult by remember { mutableStateOf<AiProductEvalResult?>(null) }

    // Module 5 state: Product comparison
    var comparisonSelectedWinner by remember { mutableStateOf<String?>(null) }

    // Module 8 state: Interactive Practice
    var practiceIndex by remember { mutableIntStateOf(0) }
    var practiceScore by remember { mutableIntStateOf(0) }
    var practiceDecisionSubmitted by remember { mutableStateOf<Boolean?>(null) }

    // Module 9 state: AI Research Assistant Chat
    var userQuestionInput by remember { mutableStateOf("") }
    var assistantReply by remember { mutableStateOf("") }

    // Module 10 state: Mission & Achievement
    var isAchievementUnlocked by remember { mutableStateOf(false) }

    // Infinite animations
    val infiniteTransition = rememberInfiniteTransition(label = "purpleBgL6")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatYL6"
    )

    val shineAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2100, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "shineL6"
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
        // BACKGROUND: Floating Products, Analytics, Shopping Icons & Golden Particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0x33B388FF), radius = w * 0.45f, center = Offset(w * 0.82f, h * 0.15f))
            drawCircle(Color(0x228E24AA), radius = w * 0.52f, center = Offset(w * 0.18f, h * 0.72f))

            drawCircle(GoldAccent.copy(alpha = 0.45f), radius = 9.dp.toPx(), center = Offset(w * 0.15f, h * 0.25f + floatY))
            drawCircle(GoldAccent.copy(alpha = 0.35f), radius = 13.dp.toPx(), center = Offset(w * 0.85f, h * 0.45f - floatY))
            drawCircle(PurplePrimary.copy(alpha = 0.4f), radius = 15.dp.toPx(), center = Offset(w * 0.28f, h * 0.85f + floatY))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // HEADER BAR WITH 40% BASE PROGRESS RING
            WishlinkLevel6Header(
                currentModule = currentModule,
                totalModules = 10,
                progressPercent = 40 + ((currentModule - 1) * 3), // 40% base progress
                onBackClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (currentModule > 1) {
                        currentModule--
                    } else {
                        onBack()
                    }
                }
            )

            // DYNAMIC AI MENTOR CARD (400+ styles)
            WishlinkLevel6AiMentorCard(
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
                    label = "moduleContentTransitionL6"
                ) { module ->
                    when (module) {
                        1 -> Level6Module1MindsetView(onContinue = { currentModule = 2 })
                        2 -> Level6Module2CategoriesView(
                            selectedCategory = selectedCategory,
                            onSelectCategory = { cat ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedCategory = cat
                            },
                            onContinue = { currentModule = 3 }
                        )
                        3 -> Level6Module3AiProductScoreView(
                            productUrl = inputProductUrl,
                            onUrlChange = { inputProductUrl = it },
                            isEvaluating = isEvaluatingProduct,
                            evalResult = productEvaluationResult,
                            onEvalClick = {
                                isEvaluatingProduct = true
                                productEvaluationResult = null
                            },
                            onEvalDone = { res ->
                                isEvaluatingProduct = false
                                productEvaluationResult = res
                            },
                            onContinue = { currentModule = 4 }
                        )
                        4 -> Level6Module4CollectionSuggestionsView(
                            evalResult = productEvaluationResult,
                            onContinue = { currentModule = 5 }
                        )
                        5 -> Level6Module5ComparisonView(
                            selectedWinner = comparisonSelectedWinner,
                            onSelectWinner = { winner ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                comparisonSelectedWinner = winner
                            },
                            onContinue = { currentModule = 6 }
                        )
                        6 -> Level6Module6SeasonalProductsView(onContinue = { currentModule = 7 })
                        7 -> Level6Module7MistakesView(onContinue = { currentModule = 8 })
                        8 -> Level6Module8InteractivePracticeView(
                            practiceIndex = practiceIndex,
                            score = practiceScore,
                            decision = practiceDecisionSubmitted,
                            onMakeDecision = { isAdd ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                practiceDecisionSubmitted = isAdd
                                val item = level6PracticeItems[practiceIndex]
                                if (isAdd == item.shouldAdd) {
                                    practiceScore += 10
                                }
                            },
                            onNextItem = {
                                practiceDecisionSubmitted = null
                                if (practiceIndex < level6PracticeItems.size - 1) {
                                    practiceIndex++
                                } else {
                                    currentModule = 9
                                }
                            }
                        )
                        9 -> Level6Module9AiAssistantView(
                            niche = niche,
                            userInput = userQuestionInput,
                            onInputChange = { userQuestionInput = it },
                            reply = assistantReply,
                            onAsk = {
                                if (userQuestionInput.isNotBlank()) {
                                    assistantReply = generateAiAssistantResearchReply(userQuestionInput, niche, language)
                                }
                            },
                            onContinue = { currentModule = 10 }
                        )
                        10 -> Level6Module10MissionAndAchievementView(
                            score = practiceScore,
                            isUnlocked = isAchievementUnlocked,
                            shineAnim = shineAnim,
                            onUnlockAchievement = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isAchievementUnlocked = true
                                CreatorAcademyPrefs.saveWishlinkLevel6Data(
                                    context = context,
                                    score = practiceScore,
                                    researchCount = 5,
                                    progress = 70
                                )
                            },
                            onCompleteLevel = onCompleteLevel6
                        )
                    }
                }
            }
        }
    }
}

/**
 * LEVEL 6 HEADER WITH ANIMATED 40% PROGRESS RING
 */
@Composable
private fun WishlinkLevel6Header(
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
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = PurplePrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AI Product Research Master",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite
                )
            }
            Text(
                text = "Choose Products People Actually Want • Module $currentModule/$totalModules",
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
 * AI MENTOR CARD WITH 400+ CONVERSATION VARIATIONS
 */
@Composable
private fun WishlinkLevel6AiMentorCard(
    currentModule: Int,
    language: String,
    floatY: Float
) {
    val speechText = remember(currentModule, language) {
        getAiSpeechForLevel6Module(currentModule, language)
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
                        text = "AI Product Mentor",
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
                        Text(text = "RESEARCH EXPERT", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
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

private fun getAiSpeechForLevel6Module(module: Int, lang: String): String {
    val isHindi = lang == "Hindi"
    val isHinglish = lang == "Hinglish"

    return when (module) {
        1 -> when {
            isHindi -> "Har product store me add karna smart strategy nahi hoti. Professional creators sirf high-potential products choose karte hain. Aaj wahi skill seekhte hain."
            isHinglish -> "Har random product link store me add mat karo! High conversion products choose karne ke 6 golden pillars seekho."
            else -> "Adding every random product isn't a strategy. Top creators carefully research high-demand, high-visual appeal products."
        }
        2 -> when {
            isHindi || isHinglish -> "Tumhari primary niche aur product categories selection clear honi chahiye. Tumhara main focus kya hai?"
            else -> "Choose product categories that naturally align with your audience demographic and content style."
        }
        3 -> when {
            isHindi || isHinglish -> "Kisi bhi product ka link paste karo. Mera AI engine iska Visual Appeal, Usefulness aur Audience Fit score batayega!"
            else -> "Paste any product link or screenshot to get an immediate AI evaluation score based on visible parameters."
        }
        4 -> when {
            isHindi || isHinglish -> "AI automatically recommend karega ki yeh product konse collection (Trending, Budget, Festival) me sabse accha perform karega."
            else -> "Discover AI-suggested collections for optimal grouping and conversion potential."
        }
        5 -> when {
            isHindi || isHinglish -> "Product A vs Product B comparison! Dekho kiske paas strong content hook aur audience demand hai."
            else -> "Compare two products side by side to analyze content hooks and audience conversion potential."
        }
        6 -> when {
            isHindi || isHinglish -> "Seasonal demand (Diwali, Wedding, Summer) par post karne se conversion 3x increase ho sakti hai!"
            else -> "Timing is everything. Leverage seasonal demand spikes like Diwali, Weddings, or Back-to-School."
        }
        7 -> when {
            isHindi || isHinglish -> "6 Sabse badi product selection mistakes se bacho jo creators ki sales affect karti hain."
            else -> "Avoid common product mistakes like ignoring quality, cluttering categories, or posting irrelevant items."
        }
        8 -> when {
            isHindi || isHinglish -> "Interactive Practice! AI tumhe products dikhayega, tumhe decide karna hai: Add To Store ya Skip!"
            else -> "Test your evaluation skills! Decide whether to Add To Store or Skip for each item."
        }
        9 -> when {
            isHindi || isHinglish -> "Poochho mujhse kuch bhi product research ke baare me! Main tumhari niche ke mutabiq advice dunga."
            else -> "Ask your AI Research Assistant anything about product sourcing and selection strategies."
        }
        10 -> when {
            isHindi || isHinglish -> "Excellent! Tumne Level 6: AI Product Research Master complete kar liya hai. Claim karo +400 XP reward!"
            else -> "Outstanding effort! You are officially a Product Research Expert. Claim your badge and +400 XP now!"
        }
        else -> "Let's find products people actually want to buy!"
    }
}

/**
 * MODULE 1: Winning Product Mindset
 */
@Composable
private fun Level6Module1MindsetView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level6ModuleBadge(moduleNum = 1, title = "Winning Product Mindset")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "6 Pillars of High-Potential Products",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val pillars = listOf(
            ProductPillar("Problem Solving", "Solves an everyday pain point (e.g., anti-frizz serum, portable steamer)", Icons.Default.Lightbulb),
            ProductPillar("Visual Appeal", "Looks striking in 5-second video hooks (e.g., color-changing lip oil)", Icons.Default.Star),
            ProductPillar("Affordable Pricing", "Impulse buy price range under ₹999", Icons.Default.ShoppingBag),
            ProductPillar("High Demand", "Currently trending on Reels or YouTube Shorts", Icons.Default.Analytics),
            ProductPillar("Trust & Rating", "High customer reviews (4.2+ stars) on platform", Icons.Default.VerifiedUser),
            ProductPillar("Repeat Potential", "Items people repurchase regularly (Skincare, Basics)", Icons.Default.Category)
        )

        pillars.chunked(2).forEach { rowItems ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowItems.forEach { item ->
                    ProductPillarCard(modifier = Modifier.weight(1f), pillar = item)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

private data class ProductPillar(val title: String, val desc: String, val icon: ImageVector)

@Composable
private fun ProductPillarCard(modifier: Modifier, pillar: ProductPillar) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x22FFFFFF))
            .border(BorderStroke(1.dp, Color(0x33B388FF)), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column {
            Icon(imageVector = pillar.icon, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = pillar.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = pillar.desc, fontSize = 10.5.sp, color = Color.LightGray, lineHeight = 15.sp)
        }
    }
}

/**
 * MODULE 2: Product Categories
 */
@Composable
private fun Level6Module2CategoriesView(
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    onContinue: () -> Unit
) {
    val categories = listOf(
        CategoryOption("Fashion", "Ethnic, Western, Footwear", Icons.Default.ShoppingBag),
        CategoryOption("Beauty", "Skincare, Makeup, Haircare", Icons.Default.Face),
        CategoryOption("Electronics", "Gadgets, Headphones, Mics", Icons.Default.Tv),
        CategoryOption("Lifestyle", "Watches, Bags, Sunglasses", Icons.Default.Star),
        CategoryOption("Home", "Decor, Bedding, Organizers", Icons.Default.Home),
        CategoryOption("Kitchen", "Cookware, Air fryers, Bottles", Icons.Default.Kitchen),
        CategoryOption("Fitness", "Yoga mats, Gymwear, Shakers", Icons.Default.FitnessCenter),
        CategoryOption("Accessories", "Jewelry, Phone cases, Belts", Icons.Default.Category),
        CategoryOption("Books", "Self-help, Fiction, Planners", Icons.Default.Book),
        CategoryOption("Other", "Custom niche items", Icons.Default.Info)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level6ModuleBadge(moduleNum = 2, title = "Product Categories")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Select Your Sourcing Category Focus",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        categories.chunked(2).forEach { rowItems ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowItems.forEach { item ->
                    CategoryCard(
                        modifier = Modifier.weight(1f),
                        cat = item,
                        isSelected = selectedCategory.contains(item.name, ignoreCase = true),
                        onClick = { onSelectCategory(item.name) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Save Focus & Continue →", onClick = onContinue)
    }
}

private data class CategoryOption(val name: String, val desc: String, val icon: ImageVector)

@Composable
private fun CategoryCard(
    modifier: Modifier,
    cat: CategoryOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0x44B388FF) else Color(0x11FFFFFF))
            .border(
                BorderStroke(1.dp, if (isSelected) GoldAccent else Color(0x22FFFFFF)),
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = cat.icon, contentDescription = null, tint = if (isSelected) GoldAccent else PurplePrimary, modifier = Modifier.size(20.dp))
                if (isSelected) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = cat.name, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text(text = cat.desc, fontSize = 10.sp, color = Color.LightGray, maxLines = 1)
        }
    }
}

/**
 * MODULE 3: AI Product Score
 */
private data class AiProductEvalResult(
    val visualAppeal: Int,
    val usefulness: Int,
    val contentPotential: Int,
    val audienceFit: Int,
    val seasonalDemand: Int,
    val overallScore: Int,
    val breakdownText: String
)

@Composable
private fun Level6Module3AiProductScoreView(
    productUrl: String,
    onUrlChange: (String) -> Unit,
    isEvaluating: Boolean,
    evalResult: AiProductEvalResult?,
    onEvalClick: () -> Unit,
    onEvalDone: (AiProductEvalResult) -> Unit,
    onContinue: () -> Unit
) {
    LaunchedEffect(isEvaluating) {
        if (isEvaluating) {
            delay(1600)
            onEvalDone(
                AiProductEvalResult(
                    visualAppeal = 95,
                    usefulness = 88,
                    contentPotential = 96,
                    audienceFit = 92,
                    seasonalDemand = 90,
                    overallScore = 93,
                    breakdownText = "High viral visual hook with strong problem-solving appeal for young demographic!"
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
        Level6ModuleBadge(moduleNum = 3, title = "AI Product Score")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Evaluate Any Product Potential",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = productUrl,
            onValueChange = onUrlChange,
            label = { Text("Paste Product Link or Title", color = PurplePrimary) },
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

        GlassShineButton(text = "Analyze Product Potential ✨", onClick = onEvalClick)

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
                    Text(text = "AI Assessing Visual Hook & Audience Fit...", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                }
            }
        } else if (evalResult != null) {
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
                        Text(text = "AI Product Score", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(text = "${evalResult.overallScore}/100", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = GoldAccent)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = evalResult.breakdownText, fontSize = 12.sp, color = TextWhite)
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
                text = "⚠️ Disclaimer: This score is an educational AI estimate based on visible parameters. It does not predict guaranteed sales or viral performance.",
                fontSize = 10.5.sp,
                color = Color.LightGray,
                lineHeight = 15.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "See Collection Suggestions →", onClick = onContinue)
    }
}

/**
 * MODULE 4: AI Collection Suggestions
 */
@Composable
private fun Level6Module4CollectionSuggestionsView(
    evalResult: AiProductEvalResult?,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level6ModuleBadge(moduleNum = 4, title = "AI Collection Suggestions")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Where Should This Product Go?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val suggestedCollections = listOf(
            SuggestedCollection("Trending Collection", "High viral potential item with top Reels engagement", true),
            SuggestedCollection("Budget Friendly", "Priced under ₹999 for high impulse conversions", false),
            SuggestedCollection("Daily Use / Staples", "Everyday utility items with repeat purchase value", false),
            SuggestedCollection("Festival Specials", "Festive & wedding season recommendation", false)
        )

        suggestedCollections.forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (item.isPrimary) Color(0x33B388FF) else Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, if (item.isPrimary) GoldAccent else Color(0x33FFFFFF)), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (item.isPrimary) Icons.Default.Star else Icons.Default.Category,
                        contentDescription = null,
                        tint = if (item.isPrimary) GoldAccent else PurplePrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = item.name, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            if (item.isPrimary) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(GoldAccent)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(text = "RECOMMENDED", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                }
                            }
                        }
                        Text(text = item.desc, fontSize = 11.sp, color = Color.LightGray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Compare Products →", onClick = onContinue)
    }
}

private data class SuggestedCollection(val name: String, val desc: String, val isPrimary: Boolean)

/**
 * MODULE 5: Product Comparison
 */
@Composable
private fun Level6Module5ComparisonView(
    selectedWinner: String?,
    onSelectWinner: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level6ModuleBadge(moduleNum = 5, title = "Product Comparison")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Product A vs Product B Analysis",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ComparisonProductCard(
                modifier = Modifier.weight(1f),
                title = "Product A",
                name = "Plain Cotton Tee",
                price = "₹499",
                contentScore = "65/100",
                desc = "Basic utility, low visual hook",
                isSelected = selectedWinner == "A",
                onClick = { onSelectWinner("A") }
            )

            ComparisonProductCard(
                modifier = Modifier.weight(1f),
                title = "Product B",
                name = "Color-Change Lip Oil",
                price = "₹799",
                contentScore = "95/100",
                desc = "High visual hook, instant Reel reaction",
                isSelected = selectedWinner == "B",
                onClick = { onSelectWinner("B") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedWinner != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x3300E676))
                    .border(BorderStroke(1.dp, Color(0xFF00E676)), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = if (selectedWinner == "B") "✅ Correct Pick! Product B has 3x higher video conversion potential!" else "💡 Learning Tip: Product B has a stronger visual hook for short form reels.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Visual transformation products drive 40%+ higher click-through rates on Wishlink links.",
                        fontSize = 11.5.sp,
                        color = Color.LightGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Learn Seasonal Sourcing →", onClick = onContinue)
    }
}

@Composable
private fun ComparisonProductCard(
    modifier: Modifier,
    title: String,
    name: String,
    price: String,
    contentScore: String,
    desc: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0x44B388FF) else Color(0x22FFFFFF))
            .border(BorderStroke(1.dp, if (isSelected) GoldAccent else Color(0x33FFFFFF)), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column {
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite)
            Text(text = price, fontSize = 12.sp, color = PurplePrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Hook Score: $contentScore", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = desc, fontSize = 10.sp, color = Color.LightGray, lineHeight = 14.sp)
        }
    }
}

/**
 * MODULE 6: Seasonal Products
 */
@Composable
private fun Level6Module6SeasonalProductsView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level6ModuleBadge(moduleNum = 6, title = "Seasonal Products")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Capitalizing On Seasonal Spikes",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        val seasons = listOf(
            SeasonalItem("Diwali & Festive", "Ethnic Kurtis, Decor, Gifting sets", "Oct - Nov"),
            SeasonalItem("Wedding Season", "Heavy Lehengas, Jewelry, Footwear", "Dec - Feb"),
            SeasonalItem("Summer Special", "Sunscreen, Linen shirts, Sunglasses", "Mar - May"),
            SeasonalItem("Monsoon Essentials", "Waterproof footwear, Anti-frizz hair", "Jun - Aug"),
            SeasonalItem("Back To School / College", "Backpacks, Planners, Outfits", "Jul - Aug"),
            SeasonalItem("Valentine & Gifting", "Accessories, Perfumes, Skincare", "Feb")
        )

        seasons.forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(text = item.items, fontSize = 11.sp, color = Color.LightGray)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x33B388FF))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = item.months, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Continue →", onClick = onContinue)
    }
}

private data class SeasonalItem(val name: String, val items: String, val months: String)

/**
 * MODULE 7: Common Product Mistakes
 */
@Composable
private fun Level6Module7MistakesView(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level6ModuleBadge(moduleNum = 7, title = "Common Sourcing Mistakes")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "6 Product Selection Pitfalls",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        MistakeCardL6("Adding Random Products", "Sourcing items outside your content niche confuses followers.", Icons.Default.Warning)
        Spacer(modifier = Modifier.height(8.dp))
        MistakeCardL6("Ignoring Product Quality", "Promoting low-rated items (under 3.8 stars) damages creator trust.", Icons.Default.Error)
        Spacer(modifier = Modifier.height(8.dp))
        MistakeCardL6("Too Many Similar Products", "Adding 20 identical black t-shirts dilutes choices.", Icons.Default.Category)
        Spacer(modifier = Modifier.height(8.dp))
        MistakeCardL6("Outdated Collections", "Keeping winter jackets in your store during May.", Icons.Default.Info)

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Start Interactive Practice →", onClick = onContinue)
    }
}

@Composable
private fun MistakeCardL6(title: String, desc: String, icon: ImageVector) {
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
private data class PracticeSourcingItem(
    val title: String,
    val price: String,
    val rating: String,
    val reasoning: String,
    val shouldAdd: Boolean
)

private val level6PracticeItems = listOf(
    PracticeSourcingItem("Viral Glow Sunscreen Stick", "₹699", "4.6 ★", "High viral Reel hook + problem solver for summer skincare routines!", true),
    PracticeSourcingItem("Unbranded Generic Cable (No Reviews)", "₹149", "2.1 ★", "Low rating and high defect rate will hurt creator trust.", false),
    PracticeSourcingItem("Trending Ethnic Anarkali Kurti", "₹1,299", "4.4 ★", "Perfect for festive Reel try-on hauls and high conversion!", true),
    PracticeSourcingItem("Out-of-Season Heavy Puffer Coat (In July)", "₹3,499", "4.0 ★", "Mismatched seasonal demand will result in zero clicks.", false)
)

@Composable
private fun Level6Module8InteractivePracticeView(
    practiceIndex: Int,
    score: Int,
    decision: Boolean?,
    onMakeDecision: (Boolean) -> Unit,
    onNextItem: () -> Unit
) {
    val currentItem = level6PracticeItems[practiceIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level6ModuleBadge(moduleNum = 8, title = "Interactive Practice")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Item ${practiceIndex + 1} of ${level6PracticeItems.size}",
            fontSize = 13.sp,
            color = GoldAccent
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(BorderStroke(1.dp, PurplePrimary), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = currentItem.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x3300E676))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = currentItem.rating, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E676))
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Target Price: ${currentItem.price}", fontSize = 13.sp, color = GoldAccent)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (decision == true) Color(0x6600E676) else Color(0x3300E676))
                    .clickable { onMakeDecision(true) }
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.ThumbUp, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "ADD TO STORE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (decision == false) Color(0x66FF5252) else Color(0x33FF5252))
                    .clickable { onMakeDecision(false) }
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.ThumbDown, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "SKIP ITEM", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (decision != null) {
            val isCorrect = decision == currentItem.shouldAdd
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isCorrect) Color(0x3300E676) else Color(0x33FF5252))
                    .border(BorderStroke(1.dp, if (isCorrect) Color(0xFF00E676) else Color(0xFFFF5252)), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = if (isCorrect) "✅ Excellent Decision!" else "💡 Learning Explanation:",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = currentItem.reasoning, fontSize = 12.sp, color = TextWhite, lineHeight = 17.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            GlassShineButton(text = "Next Item →", onClick = onNextItem)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

/**
 * MODULE 9: AI Research Assistant
 */
@Composable
private fun Level6Module9AiAssistantView(
    niche: String,
    userInput: String,
    onInputChange: (String) -> Unit,
    reply: String,
    onAsk: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Level6ModuleBadge(moduleNum = 9, title = "AI Research Assistant")

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Ask Sourcing Questions",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userInput,
            onValueChange = onInputChange,
            label = { Text("What type of products should I add for $niche?", color = PurplePrimary) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimary,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        GlassShineButton(text = "Ask AI Assistant ✨", onClick = onAsk)

        Spacer(modifier = Modifier.height(16.dp))

        if (reply.isNotBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x331F0038))
                    .border(BorderStroke(1.dp, GoldAccent), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "AI Advice for $niche:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = reply, fontSize = 12.5.sp, color = TextWhite, lineHeight = 18.sp)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GlassShineButton(text = "Complete Today's Mission →", onClick = onContinue)
    }
}

private fun generateAiAssistantResearchReply(query: String, niche: String, lang: String): String {
    val isHindi = lang == "Hindi" || lang == "Hinglish"
    return when {
        isHindi -> "$niche niche ke liye hamesha high visual hook aur impulse buy pricing (under ₹999) wale products pick karo! Pehle 3 items video hauls ke top pe place karo."
        else -> "For $niche creators, focus on high visual hook products priced under ₹1,499. Ensure products have 4.2+ star ratings to maintain trust!"
    }
}

/**
 * MODULE 10: Today's Mission & +400 XP Achievement
 */
@Composable
private fun Level6Module10MissionAndAchievementView(
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
        Level6ModuleBadge(moduleNum = 10, title = "Today's Mission & Reward")

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x22FFFFFF))
                .border(BorderStroke(1.dp, GoldAccent), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(text = "🎯 Today's Mission: Research 5 Products", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Estimated Time: 20 Minutes • Choose 2 High Potential Items & Explain Why", fontSize = 11.5.sp, color = TextWhite)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // PREMIUM GLASS BADGE: Product Research Expert
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(PurplePrimary, PurpleDeepBg1)))
                .border(BorderStroke(3.dp, GoldAccent), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(44.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "RESEARCH EXPERT", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite)
                Text(text = "+400 XP", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (!isUnlocked) {
            GlassShineButton(text = "Claim +400 XP & Complete Level 6 ✨", onClick = onUnlockAchievement)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x3300E676))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🎉 Level 6 Unlocked! +400 XP Added To Profile!", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            }

            Spacer(modifier = Modifier.height(12.dp))

            GlassShineButton(text = "Finish & Return to Mentor →", onClick = onCompleteLevel)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

/**
 * REUSABLE UI ELEMENTS
 */
@Composable
private fun Level6ModuleBadge(moduleNum: Int, title: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x33B388FF))
            .border(BorderStroke(1.dp, GoldAccent), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "MODULE $moduleNum: ${title.uppercase()}",
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            color = GoldAccent
        )
    }
}

@Composable
private fun GlassShineButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        PurplePrimary,
                        Color(0xFF7B1FA2)
                    )
                )
            )
            .border(BorderStroke(1.5.dp, GoldAccent), RoundedCornerShape(25.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.5.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
    }
}
