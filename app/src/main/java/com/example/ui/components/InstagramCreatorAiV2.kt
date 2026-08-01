package com.example.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ai.GeminiStudioNativeEngine
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.ui.screens.OfficialLogo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

// =========================================================
// COLOR PALETTE & THEME CONSTANTS (DARK VIOLET / GLASS)
// =========================================================
private val CanvasBg = Color(0xFF0A0512)
private val CardBg = Color(0xFF140B22)
private val GlassBg = Color(0x22A855F7)
private val GlassBorder = Color(0x33A855F7)
private val VioletPrimary = Color(0xFF8B5CF6)
private val VioletGlow = Color(0xFFA855F7)
private val VioletDeep = Color(0xFF581C87)
private val MagentaAccent = Color(0xFFD946EF)
private val TextWhite = Color(0xFFFFFFFF)
private val TextMuted = Color(0xFFB3A1C9)
private val SuccessGreen = Color(0xFF10B981)
private val WarningAmber = Color(0xFFF59E0B)
private val GoldAccent = Color(0xFFFFD700)
private val ErrorRed = Color(0xFFEF4444)

// =========================================================
// CREATOR CATEGORY MODEL
// =========================================================
data class CreatorCategoryItem(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val titleHinglish: String,
    val emoji: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val descriptionHinglish: String
)

val INSTAGRAM_CATEGORIES = listOf(
    CreatorCategoryItem("Vlogging", "Vlogging & Daily Life", "व्लॉगिंग और दैनिक जीवन", "Vlogging aur Daily Life", "📹", "Lifestyle, travel, behind-the-scenes", "लाइफस्टाइल, ट्रैवल, बीहाइंड द सीन्स", "Lifestyle, travel, daily routine reels"),
    CreatorCategoryItem("Tech", "Tech & AI Hacks", "टेक और एआई हैक्स", "Tech aur AI Hacks", "🚀", "Gadget reviews, AI tools, coding", "गैजेट रिव्यूज, एआई टूल्स, कोडिंग", "Gadget reviews, AI tools, tech tips"),
    CreatorCategoryItem("Gaming", "Gaming & Streaming", "गेमिंग और स्ट्रीमिंग", "Gaming aur Streaming", "🎮", "Gameplay clips, esports, gaming setups", "गेमप्ले क्लिप्स, ई-स्पोर्ट्स, गेमिंग सेटअप", "Gameplay clips, setup tips, highlights"),
    CreatorCategoryItem("Education", "Education & Knowledge", "शिक्षा और ज्ञान", "Education aur Knowledge", "🎓", "Study tips, general knowledge, skills", "स्टडी टिप्स, सामान्य ज्ञान, स्किल्स", "Study tips, GK, skill learning"),
    CreatorCategoryItem("Fashion", "Fashion & Styling", "फैशन और स्टाइलिंग", "Fashion aur Styling", "👗", "Outfit ideas, lookbooks, trends", "आउटफिट आइडियाज, लुकबुक्स, ट्रेंड्स", "Outfit styling, lookbooks, trends"),
    CreatorCategoryItem("Fitness", "Fitness & Health", "फिटनेस और स्वास्थ्य", "Fitness aur Health", "🏋️‍♂️", "Workouts, diet tips, transformations", "वर्कआउट, डाइट टिप्स, ट्रांसफॉर्मेशन", "Workouts, nutrition, gym motivation"),
    CreatorCategoryItem("Comedy", "Comedy & Memes", "कॉमेडी और मीम्स", "Comedy aur Memes", "🎭", "Skits, relatable humor, parodies", "स्किट, हास्य, मीम्स और पैरोडी", "Skits, relatable jokes, fun content"),
    CreatorCategoryItem("Finance", "Finance & Money", "वित्त और शेयर बाज़ार", "Finance aur Money", "💰", "Investing, saving tips, passive income", "इन्वेस्टिंग, बचत टिप्स, पैसिव इनकम", "Investing, money saving, crypto tips"),
    CreatorCategoryItem("Food", "Food & Cooking", "फूड और कुकिंग", "Food aur Cooking", "🍲", "Recipes, food reviews, street food", "रेसिपी, फूड रिव्यूज, स्ट्रीट फूड", "Quick recipes, food review reels"),
    CreatorCategoryItem("Beauty", "Beauty & Skincare", "ब्यूटी और स्किनकेयर", "Beauty aur Skincare", "💄", "Makeup tutorials, skin routines", "मेकअप ट्यूटोरियल, स्किन केयर रुटीन", "Makeup hacks, skin care routines"),
    CreatorCategoryItem("Other", "Custom Niche", "कस्टम नीच", "Custom Niche", "✨", "Art, music, motivation, general", "आर्ट, म्यूजिक, मोटिवेशन, जनरल", "Art, music, motivation, creator content")
)

// =========================================================
// ONBOARDING QUESTION MODEL
// =========================================================
data class OnboardingQuestion(
    val id: Int,
    val questionEn: String,
    val questionHi: String,
    val questionHinglish: String,
    val optionsEn: List<String>,
    val optionsHi: List<String>,
    val optionsHinglish: List<String>
) {
    fun getQuestion(lang: String): String = when (lang) {
        "HI" -> questionHi
        "HINGLISH" -> questionHinglish
        else -> questionEn
    }

    fun getOptions(lang: String): List<String> = when (lang) {
        "HI" -> optionsHi
        "HINGLISH" -> optionsHinglish
        else -> optionsEn
    }
}

val INSTAGRAM_ONBOARDING_QUESTIONS = listOf(
    OnboardingQuestion(
        id = 1,
        questionEn = "How long have you been using Instagram?",
        questionHi = "आप कितने समय से इंस्टाग्राम का उपयोग कर रहे हैं?",
        questionHinglish = "Kitne time se Instagram use kar rahe ho?",
        optionsEn = listOf("New", "Less than 6 months", "1 year", "More than 1 year"),
        optionsHi = listOf("नया (New)", "6 महीने से कम", "1 साल", "1 साल से ज़्यादा"),
        optionsHinglish = listOf("New User", "Less than 6 months", "1 year", "More than 1 year")
    ),
    OnboardingQuestion(
        id = 2,
        questionEn = "Current Followers?",
        questionHi = "आपके मौजूदा फॉलोअर्स कितने हैं?",
        questionHinglish = "Aapke abhi kitne Followers hain?",
        optionsEn = listOf("0 to 500", "500 to 2,000", "2,000 to 10,000", "10,000+"),
        optionsHi = listOf("0 से 500", "500 से 2,000", "2,000 से 10,000", "10,000 से ज़्यादा"),
        optionsHinglish = listOf("0 to 500", "500 to 2,000", "2,000 to 10,000", "10,000+")
    ),
    OnboardingQuestion(
        id = 3,
        questionEn = "What is your biggest goal on Instagram?",
        questionHi = "इंस्टाग्राम पर आपका सबसे बड़ा लक्ष्य क्या है?",
        questionHinglish = "Instagram par aapka sabse bada goal kya hai?",
        optionsEn = listOf("Followers", "Brand Deals", "Income", "Personal Brand", "Business", "Other"),
        optionsHi = listOf("फॉलोअर्स बढ़ाना", "ब्रांड डील्स पाना", "इनकम कमाना", "पर्सनल ब्रांड बनाना", "बिजनेस बढ़ाना", "अन्य"),
        optionsHinglish = listOf("Followers", "Brand Deals", "Income", "Personal Brand", "Business", "Other")
    ),
    OnboardingQuestion(
        id = 4,
        questionEn = "How much time can you dedicate daily?",
        questionHi = "आप रोजाना कितना समय दे सकते हैं?",
        questionHinglish = "Aap daily kitna time de sakte ho?",
        optionsEn = listOf("30 min", "1 hour", "2 hour", "4+ hour"),
        optionsHi = listOf("30 मिनट", "1 घंटा", "2 घंटे", "4+ घंटे"),
        optionsHinglish = listOf("30 min", "1 hour", "2 hour", "4+ hour")
    )
)

// =========================================================
// PERSONALIZED ROADMAP STEP MODEL
// =========================================================
data class PersonalRoadmapStep(
    val id: Int,
    val title: String,
    val badge: String,
    val shortExplanation: String,
    val missionTitle: String,
    val missionTask: String,
    val practicalChecklist: List<String>,
    val categoryExample: String,
    val smartHelpTip: String
)

// =========================================================
// MENTOR CHAT MESSAGE MODEL
// =========================================================
data class MentorChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "MENTOR" or "USER"
    val text: String,
    val isQuestion: Boolean = false,
    val options: List<String> = emptyList(),
    val questionId: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

// =========================================================
// MAIN CONTAINER DIALOG
// =========================================================
@Composable
fun InstagramCreatorAiV2Dialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // Preferences State
    var isIntroCompleted by remember {
        mutableStateOf(CreatorAcademyPrefs.isInstagramIntroCompleted(context))
    }
    var currentLang by remember {
        mutableStateOf(CreatorAcademyPrefs.getInstagramLanguage(context))
    }
    var selectedCategory by remember {
        mutableStateOf(CreatorAcademyPrefs.getInstagramCategory(context))
    }
    var userExp by remember {
        mutableStateOf(CreatorAcademyPrefs.getInstagramExperience(context))
    }
    var userFollowers by remember {
        mutableStateOf(CreatorAcademyPrefs.getInstagramFollowers(context))
    }
    var userGoal by remember {
        mutableStateOf(CreatorAcademyPrefs.getInstagramGoal(context))
    }
    var userDailyTime by remember {
        mutableStateOf(CreatorAcademyPrefs.getInstagramDailyTime(context))
    }

    var completedLessons by remember {
        mutableStateOf(CreatorAcademyPrefs.getInstagramCompletedSteps(context).toSet())
    }
    var activeLessonId by remember { mutableStateOf<Int?>(null) }
    var showResetConfirm by remember { mutableStateOf(false) }

    // Save helper functions
    fun saveLang(lang: String) {
        currentLang = lang
        CreatorAcademyPrefs.saveInstagramLanguage(context, lang)
    }

    fun saveCategory(category: String) {
        selectedCategory = category
        CreatorAcademyPrefs.saveInstagramCategory(context, category)
    }

    fun markLessonCompleted(lessonId: Int) {
        val updated = completedLessons + lessonId
        completedLessons = updated
        CreatorAcademyPrefs.saveInstagramCompletedSteps(context, updated)
        CreatorAcademyPrefs.saveInstagramCurrentStep(context, lessonId)
    }

    fun resetCourse() {
        CreatorAcademyPrefs.resetCourseProgress(context, "instagram")
        completedLessons = emptySet()
        isIntroCompleted = false
        currentLang = ""
        selectedCategory = ""
        userExp = ""
        userFollowers = ""
        userGoal = ""
        userDailyTime = ""
        activeLessonId = null
        showResetConfirm = false
        Toast.makeText(context, "Course reset successfully", Toast.LENGTH_SHORT).show()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CanvasBg)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            when {
                // PHASE 1: FIRST TIME ONBOARDING (4 SWIPE CARDS - NO BUTTONS)
                !isIntroCompleted -> {
                    InstagramFirstTimeOnboardingScreen(
                        onOnboardingCompleted = {
                            isIntroCompleted = true
                            CreatorAcademyPrefs.setInstagramIntroCompleted(context, true)
                        },
                        onClose = onDismiss
                    )
                }

                // PHASE 2: LANGUAGE SELECTION (NEVER AUTO-SELECT)
                currentLang.isBlank() -> {
                    InstagramLanguageSelectionScreen(
                        onLanguageSelected = { lang ->
                            saveLang(lang)
                        },
                        onClose = onDismiss
                    )
                }

                // PHASE 3: CATEGORY / CREATOR TYPE SELECTION
                selectedCategory.isBlank() -> {
                    InstagramCategorySelectionScreen(
                        currentLang = currentLang,
                        onCategorySelected = { cat ->
                            saveCategory(cat)
                        },
                        onClose = onDismiss
                    )
                }

                // PHASE 4: AI MENTOR QUESTIONNAIRE (ASK 1 QUESTION AT A TIME)
                userExp.isBlank() || userFollowers.isBlank() || userGoal.isBlank() || userDailyTime.isBlank() -> {
                    InstagramMentorQuestionnaireChatScreen(
                        currentLang = currentLang,
                        selectedCategory = selectedCategory,
                        initialExp = userExp,
                        initialFollowers = userFollowers,
                        initialGoal = userGoal,
                        initialDailyTime = userDailyTime,
                        onQuestionnaireCompleted = { exp, followers, goal, dailyTime ->
                            userExp = exp
                            userFollowers = followers
                            userGoal = goal
                            userDailyTime = dailyTime
                            CreatorAcademyPrefs.saveInstagramExperience(context, exp)
                            CreatorAcademyPrefs.saveInstagramFollowers(context, followers)
                            CreatorAcademyPrefs.saveInstagramGoal(context, goal)
                            CreatorAcademyPrefs.saveInstagramDailyTime(context, dailyTime)
                        },
                        onClose = onDismiss
                    )
                }

                // PHASE 5: MAIN PERSONALISED ROADMAP & DASHBOARD WITH MISSIONS & AI MENTOR
                else -> {
                    InstagramPersonalizedDashboardScreen(
                        currentLang = currentLang,
                        selectedCategory = selectedCategory,
                        userExp = userExp,
                        userFollowers = userFollowers,
                        userGoal = userGoal,
                        userDailyTime = userDailyTime,
                        completedLessons = completedLessons,
                        onLessonCompleted = { id -> markLessonCompleted(id) },
                        onChangeLang = { saveLang("") },
                        onChangeCategory = { saveCategory("") },
                        onResetCourse = { showResetConfirm = true },
                        onClose = onDismiss
                    )
                }
            }

            // RESET CONFIRM DIALOG
            if (showResetConfirm) {
                ResetConfirmDialog(
                    currentLang = currentLang,
                    onConfirm = { resetCourse() },
                    onDismiss = { showResetConfirm = false }
                )
            }
        }
    }
}

// =========================================================
// PHASE 1: FIRST TIME ONBOARDING (4 SWIPE CARDS)
// =========================================================
@Composable
private fun InstagramFirstTimeOnboardingScreen(
    onOnboardingCompleted: () -> Unit,
    onClose: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == 3) {
            delay(1200)
            onOnboardingCompleted()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(CanvasBg, CardBg, Color(0xFF1E0C36))
                )
            )
            .padding(20.dp)
    ) {
        // TOP CLOSE BUTTON
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(GlassBg)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // BRAND LOGO
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InstagramLogo(size = 32.dp)
                Text(
                    text = "Instagram Growth AI",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            // HORIZONTAL PAGER CARDS
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
            ) { page ->
                OnboardingSwipeCardItem(page = page)
            }

            // BOTTOM SWIPE INDICATOR & GESTURE GUIDANCE
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // PAGE DOTS
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { idx ->
                        val isSelected = pagerState.currentPage == idx
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(if (isSelected) 24.dp else 8.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MagentaAccent else GlassBorder)
                        )
                    }
                }

                // SWIPE GESTURE INSTRUCTION
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(GlassBg)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (pagerState.currentPage < 3) "Swipe left to continue 👈" else "Starting AI Setup... 🚀",
                        fontSize = 12.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingSwipeCardItem(page: Int) {
    val (title, description, icon, badge) = when (page) {
        0 -> InstagramQuadruple(
            "Personal AI Mentor 👋",
            "Get instant 1-on-1 Instagram coaching tailored specifically for your creator category and growth goals.",
            Icons.Default.SmartToy,
            "1-ON-1 AI COACH"
        )
        1 -> InstagramQuadruple(
            "100% Unique Roadmap 🗺️",
            "No generic courses! Your roadmap dynamically generates based on your follower count, goals, and daily time.",
            Icons.Default.AutoAwesome,
            "CUSTOM ROADMAP"
        )
        2 -> InstagramQuadruple(
            "Daily Action Missions 🎯",
            "Master Instagram step-by-step with small practical missions. Complete tasks and unlock viral growth secrets.",
            Icons.Default.EmojiEvents,
            "ACTION MISSIONS"
        )
        else -> InstagramQuadruple(
            "Smart AI Help & Multi-Style 💡",
            "Stuck anywhere? Ask AI for simple explanations, real category examples, flowcharts, or analogies.",
            Icons.Default.Lightbulb,
            "SMART HELP ENGINE"
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .shadow(16.dp, RoundedCornerShape(28.dp), ambientColor = VioletGlow, spotColor = VioletGlow),
        shape = RoundedCornerShape(28.dp),
        color = CardBg,
        border = BorderStroke(1.5.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(VioletGlow.copy(alpha = 0.4f), Color.Transparent)
                        )
                    )
                    .border(1.5.dp, MagentaAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = TextWhite, modifier = Modifier.size(40.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = GlassBg,
                border = BorderStroke(1.dp, GlassBorder)
            ) {
                Text(
                    text = badge,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MagentaAccent,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}

// =========================================================
// PHASE 2: LANGUAGE SELECTION SCREEN
// =========================================================
@Composable
private fun InstagramLanguageSelectionScreen(
    onLanguageSelected: (String) -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CanvasBg)
            .padding(20.dp)
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(GlassBg)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            InstagramLogo(size = 48.dp)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Select Preferred Language",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "अपनी भाषा चुनें • Choose language for your AI Mentor",
                    fontSize = 13.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3 LANGUAGE CARDS
            LanguageOptionCard(
                title = "Hinglish (हिंदी + English)",
                subtitle = "Most Popular • Easy & conversational Indian creator style",
                badge = "RECOMMENDED ⭐",
                onClick = { onLanguageSelected("HINGLISH") }
            )

            LanguageOptionCard(
                title = "English",
                subtitle = "Global standard language for international audience",
                badge = "GLOBAL 🌐",
                onClick = { onLanguageSelected("EN") }
            )

            LanguageOptionCard(
                title = "हिंदी (Hindi)",
                subtitle = "सरल और स्पष्ट हिंदी मार्गदर्शन",
                badge = "REGIONAL 🇮🇳",
                onClick = { onLanguageSelected("HI") }
            )
        }
    }
}

@Composable
private fun LanguageOptionCard(
    title: String,
    subtitle: String,
    badge: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        color = CardBg,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = GlassBg
                ) {
                    Text(
                        text = badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = VioletGlow,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            Icon(
                Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MagentaAccent,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// =========================================================
// PHASE 3: CATEGORY SELECTION SCREEN
// =========================================================
@Composable
private fun InstagramCategorySelectionScreen(
    currentLang: String,
    onCategorySelected: (String) -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CanvasBg)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = when (currentLang) {
                            "HI" -> "अपनी नीच / कैटेगरी चुनें 🎯"
                            "HINGLISH" -> "Apni Niche / Category Select Karein 🎯"
                            else -> "Select Your Creator Category 🎯"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = when (currentLang) {
                            "HI" -> "AI मेंटर आपकी कैटेगरी के अनुसार टिप्स देगा"
                            "HINGLISH" -> "AI Mentor aapki category ke according tips dega"
                            else -> "AI Mentor will customize strategies for your niche"
                        },
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(GlassBg)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // CATEGORIES GRID
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(INSTAGRAM_CATEGORIES) { item ->
                    CategoryGridCardItem(
                        item = item,
                        currentLang = currentLang,
                        onClick = { onCategorySelected(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryGridCardItem(
    item: CreatorCategoryItem,
    currentLang: String,
    onClick: () -> Unit
) {
    val title = when (currentLang) {
        "HI" -> item.titleHi
        "HINGLISH" -> item.titleHinglish
        else -> item.titleEn
    }
    val desc = when (currentLang) {
        "HI" -> item.descriptionHi
        "HINGLISH" -> item.descriptionHinglish
        else -> item.descriptionEn
    }

    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp),
        shape = RoundedCornerShape(18.dp),
        color = CardBg,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = item.emoji, fontSize = 24.sp)
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
            }

            Column {
                Text(
                    text = title,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = desc,
                    fontSize = 10.5.sp,
                    color = TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// =========================================================
// PHASE 4: AI MENTOR QUESTIONNAIRE CHAT SCREEN (1 QUESTION AT A TIME)
// =========================================================
@Composable
private fun InstagramMentorQuestionnaireChatScreen(
    currentLang: String,
    selectedCategory: String,
    initialExp: String,
    initialFollowers: String,
    initialGoal: String,
    initialDailyTime: String,
    onQuestionnaireCompleted: (String, String, String, String) -> Unit,
    onClose: () -> Unit
) {
    var currentExp by remember { mutableStateOf(initialExp) }
    var currentFollowers by remember { mutableStateOf(initialFollowers) }
    var currentGoal by remember { mutableStateOf(initialGoal) }
    var currentDailyTime by remember { mutableStateOf(initialDailyTime) }

    var activeQuestionIndex by remember {
        mutableStateOf(
            when {
                currentExp.isBlank() -> 0
                currentFollowers.isBlank() -> 1
                currentGoal.isBlank() -> 2
                currentDailyTime.isBlank() -> 3
                else -> 0
            }
        )
    }

    var isAnalyzing by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val chatMessages = remember {
        mutableStateListOf<MentorChatMessage>()
    }

    // INITIAL GREETING & QUESTION 1
    LaunchedEffect(Unit) {
        if (chatMessages.isEmpty()) {
            val greetingText = when (currentLang) {
                "HI" -> "शानदार! 👋 अब मुझे आपके इंस्टाग्राम अनुभव के बारे में थोड़ा और जानना है ताकि मैं आपके लिए सबसे बेहतरीन रोडमैप बना सकूं।"
                "HINGLISH" -> "Awesome! 👋 Ab mujhe tumhare baare mein thoda aur jaana hai taaki main tumhare liye best roadmap bana sakun."
                else -> "Awesome! 👋 Now I need to know a little more about you so I can create the best roadmap for your growth."
            }
            chatMessages.add(MentorChatMessage(sender = "MENTOR", text = greetingText))

            delay(600)

            val q1 = INSTAGRAM_ONBOARDING_QUESTIONS[0]
            chatMessages.add(
                MentorChatMessage(
                    sender = "MENTOR",
                    text = q1.getQuestion(currentLang),
                    isQuestion = true,
                    options = q1.getOptions(currentLang),
                    questionId = 1
                )
            )
        }
    }

    // AUTO SCROLL
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    fun handleOptionSelected(qId: Int, selectedOption: String) {
        coroutineScope.launch {
            // Add user message
            chatMessages.add(MentorChatMessage(sender = "USER", text = selectedOption))

            when (qId) {
                1 -> {
                    currentExp = selectedOption
                    activeQuestionIndex = 1
                    delay(500)
                    val nextQ = INSTAGRAM_ONBOARDING_QUESTIONS[1]
                    val ack = when (currentLang) {
                        "HI" -> "बहुत बढ़िया! 📈 अगला सवाल:"
                        "HINGLISH" -> "Got it! 📈 Next question:"
                        else -> "Great! 📈 Next question:"
                    }
                    chatMessages.add(MentorChatMessage(sender = "MENTOR", text = ack))
                    delay(400)
                    chatMessages.add(
                        MentorChatMessage(
                            sender = "MENTOR",
                            text = nextQ.getQuestion(currentLang),
                            isQuestion = true,
                            options = nextQ.getOptions(currentLang),
                            questionId = 2
                        )
                    )
                }
                2 -> {
                    currentFollowers = selectedOption
                    activeQuestionIndex = 2
                    delay(500)
                    val nextQ = INSTAGRAM_ONBOARDING_QUESTIONS[2]
                    val ack = when (currentLang) {
                        "HI" -> "समझ गया! 🎯 अब यह बताएं:"
                        "HINGLISH" -> "Understood! 🎯 Now tell me:"
                        else -> "Understood! 🎯 Now tell me:"
                    }
                    chatMessages.add(MentorChatMessage(sender = "MENTOR", text = ack))
                    delay(400)
                    chatMessages.add(
                        MentorChatMessage(
                            sender = "MENTOR",
                            text = nextQ.getQuestion(currentLang),
                            isQuestion = true,
                            options = nextQ.getOptions(currentLang),
                            questionId = 3
                        )
                    )
                }
                3 -> {
                    currentGoal = selectedOption
                    activeQuestionIndex = 3
                    delay(500)
                    val nextQ = INSTAGRAM_ONBOARDING_QUESTIONS[3]
                    val ack = when (currentLang) {
                        "HI" -> "लक्ष्य निर्धारित! 🚀 आखिरी सवाल:"
                        "HINGLISH" -> "Target locked! 🚀 Final question:"
                        else -> "Target locked! 🚀 Final question:"
                    }
                    chatMessages.add(MentorChatMessage(sender = "MENTOR", text = ack))
                    delay(400)
                    chatMessages.add(
                        MentorChatMessage(
                            sender = "MENTOR",
                            text = nextQ.getQuestion(currentLang),
                            isQuestion = true,
                            options = nextQ.getOptions(currentLang),
                            questionId = 4
                        )
                    )
                }
                4 -> {
                    currentDailyTime = selectedOption
                    isAnalyzing = true
                    delay(500)
                    val analyzingText = when (currentLang) {
                        "HI" -> "विश्लेषण जारी है... ⚡ आपकी नीच ($selectedCategory), लक्ष्य ($currentGoal), और समय ($currentDailyTime) के आधार पर आपका 100% अनूठा रोडमैप तैयार किया जा रहा है!"
                        "HINGLISH" -> "Analyzing your answers... ⚡ Creating a 100% custom roadmap for your $selectedCategory niche, $currentGoal goal, and $currentDailyTime daily time!"
                        else -> "Analyzing your answers... ⚡ Creating a 100% custom roadmap for your $selectedCategory niche, $currentGoal goal, and $currentDailyTime daily time!"
                    }
                    chatMessages.add(MentorChatMessage(sender = "MENTOR", text = analyzingText))

                    delay(1200)
                    onQuestionnaireCompleted(currentExp, currentFollowers, currentGoal, currentDailyTime)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CanvasBg)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TOOLBAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(GlassBg)
                            .border(1.dp, VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.SmartToy, contentDescription = null, tint = MagentaAccent, modifier = Modifier.size(20.dp))
                    }

                    Column {
                        Text(
                            text = "AI Creator Growth Mentor",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(SuccessGreen)
                            )
                            Text(
                                text = "Personalizing $selectedCategory Roadmap",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(GlassBg)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite)
                }
            }

            // CHAT MESSAGES LIST
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(chatMessages, key = { it.id }) { msg ->
                    if (msg.sender == "MENTOR") {
                        MentorChatBubble(
                            message = msg,
                            onOptionClick = { qId, opt -> handleOptionSelected(qId, opt) }
                        )
                    } else {
                        UserChatBubble(text = msg.text)
                    }
                }

                if (isAnalyzing) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MagentaAccent,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Generating Custom Roadmap...",
                                fontSize = 12.sp,
                                color = VioletGlow,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MentorChatBubble(
    message: MentorChatMessage,
    onOptionClick: (Int, String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(GlassBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.SmartToy, contentDescription = null, tint = VioletGlow, modifier = Modifier.size(18.dp))
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.widthIn(max = 290.dp)) {
            Surface(
                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 18.dp, bottomEnd = 18.dp, bottomStart = 18.dp),
                color = CardBg,
                border = BorderStroke(1.dp, GlassBorder)
            ) {
                Text(
                    text = message.text,
                    fontSize = 13.5.sp,
                    color = TextWhite,
                    modifier = Modifier.padding(14.dp),
                    lineHeight = 19.sp
                )
            }

            if (message.isQuestion && message.options.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    message.options.forEach { opt ->
                        Surface(
                            onClick = { onOptionClick(message.questionId, opt) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = GlassBg,
                            border = BorderStroke(1.dp, VioletGlow.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = opt,
                                    fontSize = 13.sp,
                                    color = TextWhite,
                                    fontWeight = FontWeight.Medium
                                )
                                Icon(
                                    Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = MagentaAccent,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserChatBubble(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 18.dp, topEnd = 4.dp, bottomEnd = 18.dp, bottomStart = 18.dp),
            color = VioletPrimary,
            modifier = Modifier.widthIn(max = 260.dp)
        ) {
            Text(
                text = text,
                fontSize = 13.5.sp,
                color = TextWhite,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            )
        }
    }
}

// =========================================================
// PHASE 5: DYNAMIC PERSONALIZED ROADMAP & DASHBOARD SCREEN
// =========================================================
@Composable
private fun InstagramPersonalizedDashboardScreen(
    currentLang: String,
    selectedCategory: String,
    userExp: String,
    userFollowers: String,
    userGoal: String,
    userDailyTime: String,
    completedLessons: Set<Int>,
    onLessonCompleted: (Int) -> Unit,
    onChangeLang: () -> Unit,
    onChangeCategory: () -> Unit,
    onResetCourse: () -> Unit,
    onClose: () -> Unit
) {
    var showAiChatSheet by remember { mutableStateOf(false) }
    var smartHelpMessage by remember { mutableStateOf<String?>(null) }
    var activeLessonForChat by remember { mutableStateOf<PersonalRoadmapStep?>(null) }
    var showLevel1CelebrationDialog by remember { mutableStateOf(false) }
    var showLevel3CelebrationDialog by remember { mutableStateOf(false) }
    var showLevel4CelebrationDialog by remember { mutableStateOf(false) }
    var showLevel5CelebrationDialog by remember { mutableStateOf(false) }
    var showLevel6CelebrationDialog by remember { mutableStateOf(false) }
    var showLevel7CelebrationDialog by remember { mutableStateOf(false) }

    // Level 1 lesson step IDs: 101, 102, 103, 104, 105
    val level1StepIds = listOf(101, 102, 103, 104, 105)
    val level1CompletedCount = level1StepIds.count { completedLessons.contains(it) }
    val isLevel1AllCompleted = level1CompletedCount == 5

    // Level 3 Reel Mastery step IDs: 301..310
    val level3StepIds = listOf(301, 302, 303, 304, 305, 306, 307, 308, 309, 310)
    val level3CompletedCount = level3StepIds.count { completedLessons.contains(it) }
    val isLevel3AllCompleted = level3CompletedCount == 10

    // Level 4 Video Editing Mastery step IDs: 401..414
    val level4StepIds = listOf(401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414)
    val level4CompletedCount = level4StepIds.count { completedLessons.contains(it) }
    val isLevel4AllCompleted = level4CompletedCount == 14

    // Level 5 Upload & Growth System step IDs: 501..514
    val level5StepIds = listOf(501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511, 512, 513, 514)
    val level5CompletedCount = level5StepIds.count { completedLessons.contains(it) }
    val isLevel5AllCompleted = level5CompletedCount == 14

    // Level 6 Monetization & Brand Deals step IDs: 601..615
    val level6StepIds = listOf(601, 602, 603, 604, 605, 606, 607, 608, 609, 610, 611, 612, 613, 614, 615)
    val level6CompletedCount = level6StepIds.count { completedLessons.contains(it) }
    val isLevel6AllCompleted = level6CompletedCount == 15

    // Level 7 Lifetime AI Creator Coach step IDs: 701..710
    val level7StepIds = listOf(701, 702, 703, 704, 705, 706, 707, 708, 709, 710)
    val level7CompletedCount = level7StepIds.count { completedLessons.contains(it) }
    val isLevel7AllCompleted = level7CompletedCount == 10

    // DYNAMIC ROADMAP GENERATOR
    val personalSteps = remember(selectedCategory, currentLang, userExp, userFollowers, userGoal, userDailyTime) {
        generatePersonalizedRoadmap(selectedCategory, currentLang, userExp, userFollowers, userGoal, userDailyTime)
    }

    val totalAllMissions = personalSteps.size + 5 + 10 + 14 + 14 + 15 + 10
    val totalCompletedAll = completedLessons.size
    val progressPercent = if (totalAllMissions > 0) {
        (totalCompletedAll.toFloat() / totalAllMissions.toFloat() * 100).toInt().coerceIn(0, 100)
    } else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CanvasBg)
    ) {
        // TOP TOOLBAR
        InstagramDashboardTopBar(
            currentLang = currentLang,
            selectedCategory = selectedCategory,
            onChangeLang = onChangeLang,
            onChangeCategory = onChangeCategory,
            onResetCourse = onResetCourse,
            onClose = onClose
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HERO USER PROFILE & GOAL CARD
            item {
                UserProfileGoalHeaderCard(
                    selectedCategory = selectedCategory,
                    currentLang = currentLang,
                    userExp = userExp,
                    userFollowers = userFollowers,
                    userGoal = userGoal,
                    userDailyTime = userDailyTime,
                    completedCount = totalCompletedAll,
                    totalCount = totalAllMissions,
                    progressPercent = progressPercent
                )
            }

            // LEVEL 1 JOURNEY CARD (PHASE 3)
            item {
                InstagramLevel1JourneyCard(
                    completedCount = level1CompletedCount,
                    totalCount = 5,
                    isAllCompleted = isLevel1AllCompleted,
                    currentLang = currentLang
                )
            }

            // LEVEL 1 INTERACTIVE LESSONS (5 LESSONS)
            item {
                InstagramLevel1LessonsSection(
                    completedLessons = completedLessons,
                    selectedCategory = selectedCategory,
                    userGoal = userGoal,
                    currentLang = currentLang,
                    onLessonCompleted = { lessonId ->
                        onLessonCompleted(lessonId)
                        if (lessonId == 105 || (level1CompletedCount + 1) == 5) {
                            showLevel1CelebrationDialog = true
                        }
                    },
                    onOpenSmartHelp = { stepTitle, helpText ->
                        smartHelpMessage = helpText
                        activeLessonForChat = PersonalRoadmapStep(
                            id = 999,
                            title = stepTitle,
                            badge = "LEVEL 1",
                            shortExplanation = helpText,
                            missionTitle = stepTitle,
                            missionTask = "",
                            practicalChecklist = emptyList(),
                            categoryExample = "",
                            smartHelpTip = ""
                        )
                        showAiChatSheet = true
                    }
                )
            }

            // SECTION HEADER FOR MASTER ROADMAP (LEVEL 2+)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = when (currentLang) {
                                "HI" -> "🚀 लेवल 2+ वायरल कंटेंट मास्टर रोडमैप"
                                "HINGLISH" -> "🚀 Level 2+ Content Master Roadmap"
                                else -> "🚀 Level 2+ Content Master Roadmap"
                            },
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = when (currentLang) {
                                "HI" -> "आपके लक्ष्य ($userGoal) और समय ($userDailyTime) के अनुसार"
                                "HINGLISH" -> "Tailored for your $userGoal goal & $userDailyTime daily time"
                                else -> "Tailored for your $userGoal goal & $userDailyTime daily time"
                            },
                            fontSize = 11.5.sp,
                            color = TextMuted
                        )
                    }

                    // ASK AI MENTOR FLOATING BUTTON
                    Button(
                        onClick = { showAiChatSheet = true },
                        colors = ButtonDefaults.buttonColors(containerColor = GlassBg),
                        border = BorderStroke(1.dp, VioletGlow),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.SmartToy, contentDescription = null, tint = MagentaAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("AI Mentor 💬", fontSize = 11.sp, color = TextWhite)
                    }
                }
            }

            // ROADMAP MISSION STEPS (LEVEL 2+)
            itemsIndexed(personalSteps) { idx, step ->
                val isCompleted = completedLessons.contains(step.id)
                PersonalizedMissionCardItem(
                    step = step,
                    index = idx,
                    isCompleted = isCompleted,
                    currentLang = currentLang,
                    selectedCategory = selectedCategory,
                    onMissionComplete = {
                        onLessonCompleted(step.id)
                    },
                    onOpenSmartHelp = { helpType ->
                        activeLessonForChat = step
                        smartHelpMessage = generateSmartHelpResponse(step, helpType, selectedCategory, currentLang)
                        showAiChatSheet = true
                    }
                )
            }

            // LEVEL 3 JOURNEY CARD (PHASE 5 - REEL MASTERY)
            item {
                InstagramLevel3JourneyCard(
                    completedCount = level3CompletedCount,
                    totalCount = 10,
                    isAllCompleted = isLevel3AllCompleted,
                    currentLang = currentLang
                )
            }

            // LEVEL 3 INTERACTIVE REEL COACH SECTION (10 STEPS)
            item {
                InstagramLevel3ReelCoachSection(
                    completedLessons = completedLessons,
                    selectedCategory = selectedCategory,
                    userGoal = userGoal,
                    currentLang = currentLang,
                    onLessonCompleted = { lessonId ->
                        onLessonCompleted(lessonId)
                        if (lessonId == 310 || (level3CompletedCount + 1) == 10) {
                            showLevel3CelebrationDialog = true
                        }
                    },
                    onOpenSmartHelp = { stepTitle, helpText ->
                        smartHelpMessage = helpText
                        activeLessonForChat = PersonalRoadmapStep(
                            id = 998,
                            title = stepTitle,
                            badge = "LEVEL 3",
                            shortExplanation = helpText,
                            missionTitle = stepTitle,
                            missionTask = "",
                            practicalChecklist = emptyList(),
                            categoryExample = "",
                            smartHelpTip = ""
                        )
                        showAiChatSheet = true
                    }
                )
            }

            // LEVEL 4 JOURNEY CARD (PHASE 6 - VIDEO EDITING MASTERY)
            item {
                InstagramLevel4JourneyCard(
                    completedCount = level4CompletedCount,
                    totalCount = 14,
                    isAllCompleted = isLevel4AllCompleted,
                    currentLang = currentLang
                )
            }

            // LEVEL 4 INTERACTIVE VIDEO EDITING COACH SECTION (14 STEPS)
            item {
                InstagramLevel4EditingCoachSection(
                    completedLessons = completedLessons,
                    selectedCategory = selectedCategory,
                    userGoal = userGoal,
                    currentLang = currentLang,
                    onLessonCompleted = { lessonId ->
                        onLessonCompleted(lessonId)
                        if (lessonId == 414 || (level4CompletedCount + 1) == 14) {
                            showLevel4CelebrationDialog = true
                        }
                    },
                    onOpenSmartHelp = { stepTitle, helpText ->
                        smartHelpMessage = helpText
                        activeLessonForChat = PersonalRoadmapStep(
                            id = 999,
                            title = stepTitle,
                            badge = "LEVEL 4",
                            shortExplanation = helpText,
                            missionTitle = stepTitle,
                            missionTask = "",
                            practicalChecklist = emptyList(),
                            categoryExample = "",
                            smartHelpTip = ""
                        )
                        showAiChatSheet = true
                    }
                )
            }

            // LEVEL 5 JOURNEY CARD (PHASE 7 - UPLOAD • ALGORITHM • GROWTH)
            item {
                InstagramLevel5JourneyCard(
                    completedCount = level5CompletedCount,
                    totalCount = 14,
                    isAllCompleted = isLevel5AllCompleted,
                    currentLang = currentLang
                )
            }

            // LEVEL 5 INTERACTIVE GROWTH COACH SECTION (14 STEPS)
            item {
                InstagramLevel5GrowthCoachSection(
                    completedLessons = completedLessons,
                    selectedCategory = selectedCategory,
                    userGoal = userGoal,
                    currentLang = currentLang,
                    onLessonCompleted = { lessonId ->
                        onLessonCompleted(lessonId)
                        if (lessonId == 514 || (level5CompletedCount + 1) == 14) {
                            showLevel5CelebrationDialog = true
                        }
                    },
                    onOpenSmartHelp = { stepTitle, helpText ->
                        smartHelpMessage = helpText
                        activeLessonForChat = PersonalRoadmapStep(
                            id = 999,
                            title = stepTitle,
                            badge = "LEVEL 5",
                            shortExplanation = helpText,
                            missionTitle = stepTitle,
                            missionTask = "",
                            practicalChecklist = emptyList(),
                            categoryExample = "",
                            smartHelpTip = ""
                        )
                        showAiChatSheet = true
                    }
                )
            }

            // LEVEL 6 JOURNEY CARD (PHASE 8 - MONETIZATION & BRAND DEALS)
            item {
                InstagramLevel6JourneyCard(
                    completedCount = level6CompletedCount,
                    totalCount = 15,
                    isAllCompleted = isLevel6AllCompleted,
                    currentLang = currentLang
                )
            }

            // LEVEL 6 INTERACTIVE MONETIZATION COACH SECTION (15 STEPS + BONUS PLANNER)
            item {
                InstagramLevel6MonetizationCoachSection(
                    completedLessons = completedLessons,
                    selectedCategory = selectedCategory,
                    userGoal = userGoal,
                    userFollowers = userFollowers,
                    currentLang = currentLang,
                    onLessonCompleted = { lessonId ->
                        onLessonCompleted(lessonId)
                        if (lessonId == 615 || (level6CompletedCount + 1) == 15) {
                            showLevel6CelebrationDialog = true
                        }
                    },
                    onOpenSmartHelp = { stepTitle, helpText ->
                        smartHelpMessage = helpText
                        activeLessonForChat = PersonalRoadmapStep(
                            id = 999,
                            title = stepTitle,
                            badge = "LEVEL 6",
                            shortExplanation = helpText,
                            missionTitle = stepTitle,
                            missionTask = "",
                            practicalChecklist = emptyList(),
                            categoryExample = "",
                            smartHelpTip = ""
                        )
                        showAiChatSheet = true
                    }
                )
            }

            // LEVEL 7 JOURNEY CARD (PHASE 9 - AI CREATOR COACH & LIFETIME MENTOR)
            item {
                InstagramLevel7JourneyCard(
                    completedCount = level7CompletedCount,
                    totalCount = 10,
                    isAllCompleted = isLevel7AllCompleted,
                    currentLang = currentLang
                )
            }

            // LEVEL 7 LIFETIME AI CREATOR COACH DASHBOARD (10 PERMANENT COACHING TOOLS)
            item {
                InstagramLevel7AiCreatorCoachDashboardSection(
                    completedLessons = completedLessons,
                    selectedCategory = selectedCategory,
                    userGoal = userGoal,
                    userFollowers = userFollowers,
                    currentLang = currentLang,
                    onLessonCompleted = { lessonId ->
                        onLessonCompleted(lessonId)
                        if (lessonId == 710 || (level7CompletedCount + 1) == 10) {
                            showLevel7CelebrationDialog = true
                        }
                    },
                    onOpenSmartHelp = { stepTitle, helpText ->
                        smartHelpMessage = helpText
                        activeLessonForChat = PersonalRoadmapStep(
                            id = 999,
                            title = stepTitle,
                            badge = "LEVEL 7",
                            shortExplanation = helpText,
                            missionTitle = stepTitle,
                            missionTask = "",
                            practicalChecklist = emptyList(),
                            categoryExample = "",
                            smartHelpTip = ""
                        )
                        showAiChatSheet = true
                    }
                )
            }

            // COMPLETION CERTIFICATE
            if (completedLessons.size >= totalAllMissions && totalAllMissions > 0) {
                item {
                    CourseCompletionCertificate(
                        selectedCategory = selectedCategory,
                        currentLang = currentLang,
                        onResetCourse = onResetCourse
                    )
                }
            }
        }
    }

    // LEVEL 1 REWARD CELEBRATION MODAL
    if (showLevel1CelebrationDialog) {
        Level1RewardCelebrationDialog(
            selectedCategory = selectedCategory,
            currentLang = currentLang,
            onDismiss = { showLevel1CelebrationDialog = false }
        )
    }

    // LEVEL 3 REWARD CELEBRATION MODAL
    if (showLevel3CelebrationDialog) {
        Level3RewardCelebrationDialog(
            selectedCategory = selectedCategory,
            currentLang = currentLang,
            onDismiss = { showLevel3CelebrationDialog = false }
        )
    }

    // LEVEL 4 REWARD CELEBRATION MODAL
    if (showLevel4CelebrationDialog) {
        Level4RewardCelebrationDialog(
            selectedCategory = selectedCategory,
            currentLang = currentLang,
            onDismiss = { showLevel4CelebrationDialog = false }
        )
    }

    // LEVEL 5 REWARD CELEBRATION MODAL
    if (showLevel5CelebrationDialog) {
        Level5RewardCelebrationDialog(
            selectedCategory = selectedCategory,
            currentLang = currentLang,
            onDismiss = { showLevel5CelebrationDialog = false }
        )
    }

    // LEVEL 6 REWARD CELEBRATION MODAL
    if (showLevel6CelebrationDialog) {
        Level6RewardCelebrationDialog(
            selectedCategory = selectedCategory,
            currentLang = currentLang,
            onDismiss = { showLevel6CelebrationDialog = false }
        )
    }

    // LEVEL 7 REWARD CELEBRATION MODAL (FINAL LEVEL COMPLETE & LIFETIME COACH ACTIVATION)
    if (showLevel7CelebrationDialog) {
        Level7RewardCelebrationDialog(
            selectedCategory = selectedCategory,
            currentLang = currentLang,
            onDismiss = { showLevel7CelebrationDialog = false }
        )
    }

    // AI MENTOR DIRECT CHAT BOTTOM SHEET / DIALOG
    if (showAiChatSheet) {
        InstagramAiMentorLiveChatSheet(
            currentLang = currentLang,
            selectedCategory = selectedCategory,
            userGoal = userGoal,
            initialHelpText = smartHelpMessage,
            activeLesson = activeLessonForChat,
            onDismiss = {
                showAiChatSheet = false
                smartHelpMessage = null
            }
        )
    }
}

// =========================================================
// USER PROFILE & GOAL HEADER CARD
// =========================================================
@Composable
private fun UserProfileGoalHeaderCard(
    selectedCategory: String,
    currentLang: String,
    userExp: String,
    userFollowers: String,
    userGoal: String,
    userDailyTime: String,
    completedCount: Int,
    totalCount: Int,
    progressPercent: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(24.dp), ambientColor = VioletGlow, spotColor = VioletGlow),
        shape = RoundedCornerShape(24.dp),
        color = CardBg,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🔥", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "$selectedCategory Creator",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "Exp: $userExp • Followers: $userFollowers",
                            fontSize = 11.5.sp,
                            color = TextMuted
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = VioletDeep,
                    border = BorderStroke(1.dp, MagentaAccent)
                ) {
                    Text(
                        text = "🎯 $userGoal",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // PROGRESS BAR & PERCENT
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Roadmap Progress ($completedCount / $totalCount Missions)",
                    fontSize = 12.sp,
                    color = TextMuted
                )
                Text(
                    text = "$progressPercent%",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = SuccessGreen,
                trackColor = GlassBg
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Schedule, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(14.dp))
                Text(
                    text = "Daily Dedicated Time: $userDailyTime",
                    fontSize = 11.5.sp,
                    color = TextMuted
                )
            }
        }
    }
}

// =========================================================
// PERSONALIZED MISSION CARD ITEM WITH "MISSION COMPLETE"
// =========================================================
@Composable
private fun PersonalizedMissionCardItem(
    step: PersonalRoadmapStep,
    index: Int,
    isCompleted: Boolean,
    currentLang: String,
    selectedCategory: String,
    onMissionComplete: () -> Unit,
    onOpenSmartHelp: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(index == 0 || !isCompleted) }
    var isJustCompleted by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0F1A15) else CardBg,
        border = BorderStroke(
            1.dp,
            if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // STEP HEADER ROW
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text(
                                text = "${index + 1}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MagentaAccent
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GlassBg
                        ) {
                            Text(
                                text = step.badge,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = VioletGlow,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = step.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            // EXPANDED MISSION CONTENT
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier.padding(top = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // ONE EXPLANATION (CONCISE)
                    Text(
                        text = step.shortExplanation,
                        fontSize = 13.sp,
                        color = TextWhite,
                        lineHeight = 18.sp
                    )

                    // REAL CATEGORY EXAMPLE
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = GlassBg,
                        border = BorderStroke(1.dp, GlassBorder)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "💡 $selectedCategory Example:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MagentaAccent
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = step.categoryExample,
                                fontSize = 12.sp,
                                color = TextMuted,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // TODAY'S MISSION CARD
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF1E1133),
                        border = BorderStroke(1.dp, MagentaAccent.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(18.dp))
                                Text(
                                    text = "TODAY'S MISSION: ${step.missionTitle}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarningAmber
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = step.missionTask,
                                fontSize = 12.5.sp,
                                color = TextWhite,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            step.practicalChecklist.forEach { item ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.padding(vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                                    Text(text = item, fontSize = 11.5.sp, color = TextMuted)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // MISSION COMPLETE BUTTON
                            Button(
                                onClick = {
                                    onMissionComplete()
                                    isJustCompleted = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isCompleted) SuccessGreen else VioletPrimary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = if (isCompleted) Icons.Default.Check else Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = TextWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isCompleted) "Mission Complete 🔥" else "Complete Mission 🔥",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                        }
                    }

                    // NATURAL CONVERSATIONAL AI FEEDBACK IF JUST COMPLETED
                    if (isCompleted) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = SuccessGreen.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, SuccessGreen)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("🔥", fontSize = 20.sp)
                                Text(
                                    text = when (currentLang) {
                                        "HI" -> "शानदार काम! 🔥 यह कदम बहुत महत्वपूर्ण था। अब आप अगले लेवल के लिए तैयार हैं।"
                                        "HINGLISH" -> "Great job! 🔥 Ye step bahut important tha. Ab tum next level ke liye ready ho."
                                        else -> "Great job! 🔥 This step was crucial. Now you are ready for the next level."
                                    },
                                    fontSize = 12.sp,
                                    color = TextWhite,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // SMART HELP BUTTONS BAR
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onOpenSmartHelp("SIMPLE") },
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, VioletGlow.copy(alpha = 0.4f)),
                            contentPadding = PaddingValues(4.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("💡 Simple", fontSize = 10.5.sp, color = TextMuted)
                        }

                        OutlinedButton(
                            onClick = { onOpenSmartHelp("EXAMPLE") },
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, VioletGlow.copy(alpha = 0.4f)),
                            contentPadding = PaddingValues(4.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("🎬 Example", fontSize = 10.5.sp, color = TextMuted)
                        }

                        OutlinedButton(
                            onClick = { onOpenSmartHelp("FLOWCHART") },
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, VioletGlow.copy(alpha = 0.4f)),
                            contentPadding = PaddingValues(4.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("📊 Flowchart", fontSize = 10.5.sp, color = TextMuted)
                        }

                        OutlinedButton(
                            onClick = { onOpenSmartHelp("AGAIN") },
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, MagentaAccent.copy(alpha = 0.6f)),
                            contentPadding = PaddingValues(4.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("🔄 Rewrite", fontSize = 10.5.sp, color = MagentaAccent)
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// AI MENTOR LIVE CHAT SHEET / DIALOG (SMART HELP & RE-WRITING)
// =========================================================
@Composable
private fun InstagramAiMentorLiveChatSheet(
    currentLang: String,
    selectedCategory: String,
    userGoal: String,
    initialHelpText: String?,
    activeLesson: PersonalRoadmapStep?,
    onDismiss: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val chatMessages = remember {
        mutableStateListOf<MentorChatMessage>()
    }

    LaunchedEffect(Unit) {
        if (chatMessages.isEmpty()) {
            val introMsg = when (currentLang) {
                "HI" -> "नमस्ते! 👋 मैं आपका AI इंस्टाग्राम मेंटर हूँ। अपनी नीच ($selectedCategory) या लक्ष्य ($userGoal) के बारे में कुछ भी पूछें!"
                "HINGLISH" -> "Hey! 👋 Main aapka AI Instagram Mentor hoon. Apni $selectedCategory niche ya $userGoal goal ke baare mein kuch bhi poocho!"
                else -> "Hey there! 👋 I am your AI Instagram Mentor. Ask me anything about growing your $selectedCategory channel!"
            }
            chatMessages.add(MentorChatMessage(sender = "MENTOR", text = introMsg))

            if (!initialHelpText.isNullOrBlank()) {
                delay(300)
                chatMessages.add(MentorChatMessage(sender = "MENTOR", text = initialHelpText))
            }
        }
    }

    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    fun sendMessage(userQuery: String) {
        if (userQuery.isBlank()) return
        val text = userQuery
        inputText = ""
        chatMessages.add(MentorChatMessage(sender = "USER", text = text))

        coroutineScope.launch {
            isThinking = true

            val reply = try {
                val systemPrompt = "You are a friendly, natural, professional Instagram Growth Coach for a creator in the '$selectedCategory' niche aiming for '$userGoal'. Speak in '$currentLang' (Hinglish/Hindi/English). NO MARKDOWN (no stars, no hash headers), NO CHATGPT STYLE, NO ROBOTIC NUMBERING. Speak naturally like a personal mentor on chat."
                val raw = GeminiStudioNativeEngine.generateWithHighThinking(
                    prompt = "Creator Question: $text\nCurrent Active Lesson: ${activeLesson?.title ?: "General Growth"}",
                    systemInstruction = systemPrompt
                )
                cleanAiResponse(raw)
            } catch (e: Exception) {
                generateFallbackMentorReply(text, selectedCategory, currentLang)
            }

            isThinking = false
            chatMessages.add(MentorChatMessage(sender = "MENTOR", text = reply))
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CanvasBg)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // CHAT SHEET TOP BAR
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(GlassBg)
                                .border(1.dp, VioletGlow, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.SmartToy, contentDescription = null, tint = MagentaAccent, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text(
                                text = "AI Instagram Mentor 🟢",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "$selectedCategory • Goal: $userGoal",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(GlassBg)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite)
                    }
                }

                // SMART HELP SUGGESTION PILLS
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SuggestionPill("💡 Explain Simply") {
                        sendMessage("Can you explain ${activeLesson?.title ?: "this step"} in very simple terms?")
                    }
                    SuggestionPill("🎬 $selectedCategory Example") {
                        sendMessage("Give me a real example for $selectedCategory niche.")
                    }
                    SuggestionPill("📊 Step-by-Step Flowchart") {
                        sendMessage("Give me a step-by-step flowchart for this.")
                    }
                    SuggestionPill("🧠 Give Analogy") {
                        sendMessage("Explain this using a real-life analogy.")
                    }
                    SuggestionPill("🔄 Explain Again") {
                        sendMessage("Rewrite your previous answer from scratch in a fresh style.")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // CHAT MESSAGES
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(chatMessages, key = { it.id }) { msg ->
                        if (msg.sender == "MENTOR") {
                            MentorChatBubble(message = msg, onOptionClick = { _, _ -> })
                        } else {
                            UserChatBubble(text = msg.text)
                        }
                    }

                    if (isThinking) {
                        item {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = MagentaAccent,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("AI Mentor is typing...", fontSize = 11.5.sp, color = TextMuted)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // INPUT ROW
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Ask your mentor anything...", fontSize = 12.5.sp, color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MagentaAccent,
                            unfocusedBorderColor = GlassBorder,
                            focusedContainerColor = CardBg,
                            unfocusedContainerColor = CardBg,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        shape = RoundedCornerShape(20.dp),
                        maxLines = 3
                    )

                    IconButton(
                        onClick = { sendMessage(inputText) },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (inputText.isNotBlank()) VioletPrimary else GlassBg)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = TextWhite)
                    }
                }
            }
        }
    }
}

@Composable
private fun SuggestionPill(label: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = GlassBg,
        border = BorderStroke(1.dp, VioletGlow.copy(alpha = 0.5f))
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextWhite,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

// =========================================================
// DYNAMIC PERSONALIZED ROADMAP GENERATOR
// =========================================================
private fun generatePersonalizedRoadmap(
    category: String,
    lang: String,
    exp: String,
    followers: String,
    goal: String,
    dailyTime: String
): List<PersonalRoadmapStep> {
    val isBeginner = exp.contains("New", true) || followers.contains("0 to 500", true)
    val isMonetization = goal.contains("Brand", true) || goal.contains("Income", true) || goal.contains("Business", true)

    return listOf(
        PersonalRoadmapStep(
            id = 1,
            title = if (isBeginner) "1. Profile & Bio Storefront" else "1. High-Converting Bio Audit",
            badge = "FOUNDATION",
            shortExplanation = when (lang) {
                "HI" -> "आपकी प्रोफाइल आपकी डिजिटल दुकान है। पहले 2 सेकंड में विजिटर तय करता है कि फॉलो करना है या नहीं।"
                "HINGLISH" -> "Aapki profile aapki digital storefront hai. First 2 seconds me visitor decide karta hai follow karein ya nahi."
                else -> "Your profile is your digital storefront. Within 2 seconds, visitors decide whether to follow you."
            },
            missionTitle = "Bio & Handle Optimisation",
            missionTask = "Set a 3-line clear value bio for your $category channel.",
            practicalChecklist = listOf(
                "Line 1: State who you help in $category",
                "Line 2: Add social proof or key highlight",
                "Line 3: Add clear call-to-action link"
            ),
            categoryExample = when (lang) {
                "HI" -> "🚀 [$category क्रिएटर] | 💡 डेली $category टिप्स | ⬇️ फ्री गाइड नीचे"
                "HINGLISH" -> "🚀 [$category Creator] | 💡 Daily $category Hacks | ⬇️ Free Creator Toolkit link below"
                else -> "🚀 [$category Creator] | 💡 Daily $category Insights | ⬇️ Free Guide Link Below"
            },
            smartHelpTip = "Use a bright HD face photo with high contrast background!"
        ),
        PersonalRoadmapStep(
            id = 2,
            title = "2. 1080p Crystal Clear Settings",
            badge = "QUALITY",
            shortExplanation = when (lang) {
                "HI" -> "इंस्टाग्राम डिफ़ॉल्ट रूप से वीडियो क्वालिटी घटाता है। सेटिंग्स में 'Upload at Highest Quality' ऑन करें।"
                "HINGLISH" -> "Instagram default me video compress karta hai. Settings me 'Upload at Highest Quality' ON karein."
                else -> "Toggle 'Upload at Highest Quality' in settings to force full HD 1080p crisp video uploads."
            },
            missionTitle = "Enable HD Uploads",
            missionTask = "Turn on Highest Quality Upload setting in Instagram settings.",
            practicalChecklist = listOf(
                "Open Profile -> Settings -> Data Usage",
                "Enable 'Upload at highest quality'",
                "Export videos at 1080p 60FPS"
            ),
            categoryExample = "Bright facial lighting makes $category videos look 3x more professional!",
            smartHelpTip = "Avoid using heavy filters that add blur to your footage."
        ),
        PersonalRoadmapStep(
            id = 3,
            title = "3. 3-Second Viral Hook System",
            badge = "VIRAL HOOK",
            shortExplanation = when (lang) {
                "HI" -> "अगर शुरुआती 3 सेकंड में हुक नहीं है तो लोग स्वाइप कर देंगे। स्क्रीन पर बड़ा बोल्ड टेक्स्ट रखें।"
                "HINGLISH" -> "First 3 seconds me bold text aur energetic hook nahi hoga toh log swipe kar denge."
                else -> "Attention spans are under 3 seconds. Place a bold contrast title text overlay in frame 1."
            },
            missionTitle = "Create 1 Hooked Reel",
            missionTask = "Record a short 7-second $category reel with a bold curiosity gap hook.",
            practicalChecklist = listOf(
                "Add high contrast text in first frame",
                "Keep hook line under 6 words",
                "Start with action or gesture"
            ),
            categoryExample = when (lang) {
                "HI" -> "हुक: 'अगर $category में तेजी से ग्रो करना है, तो यह 1 गलती तुरंत बंद करो! 🚨'"
                "HINGLISH" -> "Hook: 'Agar $category me grow karna hai, toh ye 1 mistake abhi roko! 🚨'"
                else -> "Hook: 'Stop making this 1 major $category mistake if you want 100k views! 🚨'"
            },
            smartHelpTip = "Ask a curiosity gap question that forces viewers to watch till the end!"
        ),
        PersonalRoadmapStep(
            id = 4,
            title = "4. Trending Audio & Voice Balance",
            badge = "AUDIO ENGINE",
            shortExplanation = when (lang) {
                "HI" -> "↗️ तीर वाले ट्रेंडिंग ऑडियो का इस्तेमाल करें। वॉइसओवर 100% और बैकग्राउंड म्यूजिक 12% रखें।"
                "HINGLISH" -> "Arrow ↗️ wale trending audio use karein. Voiceover 100% aur BGM 12% rakhein."
                else -> "Use ↗️ trending audio clips. Set spoken voiceover to 100% and background music to 12%."
            },
            missionTitle = "Audio Mix Mastery",
            missionTask = "Select 1 trending audio with arrow ↗️ icon and balance audio mix.",
            practicalChecklist = listOf(
                "Find trending audio with ↗️ arrow icon",
                "Record voiceover in quiet room",
                "Mix voice 100% and background music 12%"
            ),
            categoryExample = "12% background music triggers the trending audio tag without hiding your spoken voice!",
            smartHelpTip = "Use quiet noise-canceling environment while recording."
        ),
        PersonalRoadmapStep(
            id = 5,
            title = "5. SEO Caption & 3-Tier Hashtags",
            badge = "ALGORITHM SEO",
            shortExplanation = when (lang) {
                "HI" -> "इंस्टाग्राम सर्च इंजन है। कैप्शन की पहली लाइन में कीवर्ड डालें और 3 નીચ + 3 मीडियम + 2 बड़े हैशटैग्स मिलाएं।"
                "HINGLISH" -> "Caption line 1 me main keyword likhein. 3 Niche + 3 Medium + 2 Broad tags mix use karein."
                else -> "Include primary keywords in caption line 1 and combine 3 Niche + 3 Medium + 2 Broad hashtags."
            },
            missionTitle = "SEO Caption Formula",
            missionTask = "Write an SEO caption with 8 target hashtags for your $category reel.",
            practicalChecklist = listOf(
                "Put main keyword in sentence 1",
                "Add 3 Niche + 3 Medium + 2 Broad tags",
                "Include location tag"
            ),
            categoryExample = "Caption: 'Ultimate 2026 $category guide' | Tags: #${category}Tips #${category}Hacks #ReelGrowth",
            smartHelpTip = "Never spam 100 identical hashtags; quality over quantity!"
        ),
        PersonalRoadmapStep(
            id = 6,
            title = if (isMonetization) "6. Brand Pitch & Media Kit" else "6. 1-Hour Comment Velocity",
            badge = if (isMonetization) "MONETIZATION" else "GROWTH VELOCITY",
            shortExplanation = when (lang) {
                "HI" -> if (isMonetization) "ब्रांड्स के साथ काम करने के लिए 1-पेज मीडिया किट और पिच मैसेज तैयार करें।" else "रील्स पोस्ट करने के पहले 60 मिनट में सभी कमेंट्स का रिप्लाई करें ताकि रीच 3x हो जाए।"
                "HINGLISH" -> if (isMonetization) "Brands ke saath collab ke liye 1-page media kit aur pitch message ready karein." else "Reel post hone ke pehle 60 mins me saare comments ka reply karein."
                else -> if (isMonetization) "Prepare a 1-page media kit & pitch script to close brand sponsorships." else "Reply to all early comments within 60 mins of posting to trigger algorithm push."
            },
            missionTitle = if (isMonetization) "Draft Brand Pitch" else "Reply to Comments",
            missionTask = if (isMonetization) "Draft a 3-line DM pitch to 3 micro brands in $category." else "Reply to first 5 comments within 60 mins.",
            practicalChecklist = listOf(
                if (isMonetization) "Highlight your $category audience stats" else "Reply to comments within 60 mins",
                if (isMonetization) "Attach rate card link" else "Ask questions in reply to spark thread",
                "Pin top positive comment"
            ),
            categoryExample = if (isMonetization) "Hey! Loved your $category product. I can showcase it to my engaged audience." else "Reply fast in hour 1 to double engagement rate!",
            smartHelpTip = "Early engagement signals algorithm to push content to Explore page!"
        )
    )
}

// =========================================================
// SMART HELP RESPONSE GENERATOR
// =========================================================
private fun generateSmartHelpResponse(
    step: PersonalRoadmapStep,
    helpType: String,
    category: String,
    lang: String
): String {
    return when (helpType) {
        "SIMPLE" -> when (lang) {
            "HI" -> "सरल भाषा में: ${step.title}\n${step.shortExplanation}\n\nमुख्य काम: ${step.missionTask}"
            "HINGLISH" -> "Simple terms me: ${step.title}\n${step.shortExplanation}\n\nMain Task: ${step.missionTask}"
            else -> "In simple terms: ${step.title}\n${step.shortExplanation}\n\nMain Task: ${step.missionTask}"
        }
        "EXAMPLE" -> "🎬 Real $category Example:\n${step.categoryExample}\n\nTry this exact format for your next $category Reel!"
        "FLOWCHART" -> "📊 Step-by-Step Flowchart:\nStep 1: ${step.practicalChecklist.getOrNull(0) ?: "Setup"}\n⬇️\nStep 2: ${step.practicalChecklist.getOrNull(1) ?: "Execute"}\n⬇️\nStep 3: ${step.practicalChecklist.getOrNull(2) ?: "Publish & Engage"}"
        "AGAIN" -> when (lang) {
            "HI" -> "आइए इसे नए नजरिए से समझें! 🔥\n${step.title} को पूरा करने के लिए केवल इस 1 काम पर ध्यान दें: ${step.missionTask}. उदाहरण: ${step.categoryExample}"
            "HINGLISH" -> "Naye tareeqe se samajhte hain! 🔥\n${step.title} complete karne ke liye bas ye 1 kaam karo: ${step.missionTask}. Example: ${step.categoryExample}"
            else -> "Let's re-frame this from scratch! 🔥\nTo master ${step.title}, focus purely on this 1 mission: ${step.missionTask}. Example: ${step.categoryExample}"
        }
        else -> step.shortExplanation
    }
}

private fun cleanAiResponse(raw: String): String {
    return raw
        .replace("**", "")
        .replace("###", "")
        .replace("##", "")
        .replace("#", "")
        .replace("```", "")
        .trim()
}

private fun generateFallbackMentorReply(query: String, category: String, lang: String): String {
    val options = listOf(
        when (lang) {
            "HI" -> "यह एक बेहतरीन सवाल है! $category नीच में आगे बढ़ने के लिए अपनी पहली 3 सेकंड की रील में बड़ा हुक टेक्स्ट लगाएं और पोस्ट करने के पहले 1 घंटे में एक्टिव रहें! 🚀"
            "HINGLISH" -> "Bohot achha question hai! $category niche me viral hone ke liye pehle 3 seconds me bold text hook lagayein aur upload ke first 1 hour me comments reply karein! 🚀"
            else -> "Great question! For $category growth, ensure a bold text hook in the first 3 seconds and engage with all early comments in hour 1! 🚀"
        },
        when (lang) {
            "HI" -> "याद रखें: क्वालिटी + कंसिस्टेंसी = ग्रोथ! अपनी वीडियो को हमेशा 1080p में एक्सपोर्ट करें और 3-टियर हैशटैग्स का सही मिक्स इस्तेमाल करें! 🔥"
            "HINGLISH" -> "Yaad rakho: High quality + consistency = viral growth! Video ko 1080p 60FPS me upload karein aur 3-tier hashtags use karein! 🔥"
            else -> "Remember: Quality + Consistency = Growth! Always export in 1080p 60FPS and use the 3-tier hashtag strategy! 🔥"
        }
    )
    return options[Random.nextInt(options.size)]
}

// =========================================================
// TOP TOOLBAR
// =========================================================
@Composable
private fun InstagramDashboardTopBar(
    currentLang: String,
    selectedCategory: String,
    onChangeLang: () -> Unit,
    onChangeCategory: () -> Unit,
    onResetCourse: () -> Unit,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CanvasBg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InstagramLogo(size = 28.dp)

            if (selectedCategory.isNotBlank()) {
                Surface(
                    onClick = onChangeCategory,
                    shape = RoundedCornerShape(12.dp),
                    color = GlassBg,
                    border = BorderStroke(1.dp, GlassBorder)
                ) {
                    Text(
                        text = "$selectedCategory ✏️",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = VioletGlow,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            if (currentLang.isNotBlank()) {
                Surface(
                    onClick = onChangeLang,
                    shape = RoundedCornerShape(12.dp),
                    color = GlassBg,
                    border = BorderStroke(1.dp, GlassBorder)
                ) {
                    Text(
                        text = "$currentLang 🌐",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(onClick = onResetCourse) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = TextMuted)
            }

            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(GlassBg)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite)
            }
        }
    }
}

// =========================================================
// COURSE COMPLETION CERTIFICATE
// =========================================================
@Composable
private fun CourseCompletionCertificate(
    selectedCategory: String,
    currentLang: String,
    onResetCourse: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = CardBg,
        border = BorderStroke(2.dp, SuccessGreen)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(SuccessGreen.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(36.dp))
            }

            Text(
                text = "🎓 Certificate of Completion",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Congratulations! You have completed your personalized Instagram $selectedCategory Creator Roadmap.",
                fontSize = 12.5.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )

            OutlinedButton(
                onClick = onResetCourse,
                border = BorderStroke(1.dp, VioletGlow),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Replay Course / Reset Progress 🔄", fontSize = 12.sp, color = VioletGlow)
            }
        }
    }
}

// =========================================================
// RESET CONFIRM DIALOG
// =========================================================
@Composable
private fun ResetConfirmDialog(
    currentLang: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = when (currentLang) {
                    "HI" -> "कोर्स प्रगति रीसेट करें?"
                    "HINGLISH" -> "Course Progress Reset Karein?"
                    else -> "Reset Course Progress?"
                },
                color = TextWhite,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = when (currentLang) {
                    "HI" -> "आपकी भाषा, नीच और पूर्ण किए गए पाठ रीसेट कर दिए जाएंगे।"
                    "HINGLISH" -> "Aapki language, niche aur completed lessons reset ho jayenge."
                    else -> "All saved progress, selected language, category, and lessons will be reset."
                },
                color = TextMuted
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
            ) {
                Text("Reset Everything", color = TextWhite)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        },
        containerColor = CardBg
    )
}

private @Composable
fun InstagramLogo(size: androidx.compose.ui.unit.Dp = 32.dp) {
    OfficialLogo(name = "ViralToolAi", modifier = Modifier.size(size))
}

private data class InstagramQuadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

// =========================================================
// PHASE 3 — LEVEL 1 JOURNEY CARD & INTERACTIVE LESSONS
// =========================================================

@Composable
private fun InstagramLevel1JourneyCard(
    completedCount: Int,
    totalCount: Int = 5,
    isAllCompleted: Boolean,
    currentLang: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(24.dp), ambientColor = VioletGlow, spotColor = VioletGlow),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF1B0E33),
        border = BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(VioletGlow, MagentaAccent)))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // LEVEL BADGE ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = VioletDeep,
                    border = BorderStroke(1.dp, MagentaAccent)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "🔥", fontSize = 14.sp)
                        Text(
                            text = "LEVEL 1 • INSTAGRAM FOUNDATION",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isAllCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg,
                    border = BorderStroke(1.dp, if (isAllCompleted) SuccessGreen else GlassBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isAllCompleted) Icons.Default.EmojiEvents else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isAllCompleted) WarningAmber else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (isAllCompleted) "Badge: Profile Builder 🏆" else "Badge Locked 🔒",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAllCompleted) WarningAmber else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Account Setup & Brand Ready Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite
            )

            Text(
                text = "Master account foundation, switch to Creator, claim a brandable handle, HD DP, and high-converting bio.",
                fontSize = 12.5.sp,
                color = TextMuted,
                lineHeight = 17.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            // METRICS ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = "📚 5 Lessons", fontSize = 11.5.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    Text(text = "⏱️ 25–35 Mins", fontSize = 11.5.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    Text(text = "⚡ +500 XP", fontSize = 11.5.sp, color = WarningAmber, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "$completedCount / $totalCount Completed",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isAllCompleted) SuccessGreen else VioletGlow
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { completedCount.toFloat() / totalCount.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = if (isAllCompleted) SuccessGreen else MagentaAccent,
                trackColor = GlassBg
            )
        }
    }
}

// =========================================================
// LEVEL 1 INTERACTIVE LESSONS SECTION
// =========================================================
@Composable
private fun InstagramLevel1LessonsSection(
    completedLessons: Set<Int>,
    selectedCategory: String,
    userGoal: String,
    currentLang: String,
    onLessonCompleted: (Int) -> Unit,
    onOpenSmartHelp: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // LESSON 1
        Level1Lesson1Card(
            isCompleted = completedLessons.contains(101),
            currentLang = currentLang,
            onMissionComplete = { onLessonCompleted(101) },
            onOpenHelp = { onOpenSmartHelp("Lesson 1: Account Setup", generateHelpModeContent(1, selectedCategory, currentLang)) }
        )

        // LESSON 2
        Level1Lesson2Card(
            isCompleted = completedLessons.contains(102),
            selectedCategory = selectedCategory,
            currentLang = currentLang,
            onMissionComplete = { onLessonCompleted(102) },
            onOpenHelp = { onOpenSmartHelp("Lesson 2: Professional Account", generateHelpModeContent(2, selectedCategory, currentLang)) }
        )

        // LESSON 3
        Level1Lesson3Card(
            isCompleted = completedLessons.contains(103),
            selectedCategory = selectedCategory,
            currentLang = currentLang,
            onMissionComplete = { onLessonCompleted(103) },
            onOpenHelp = { onOpenSmartHelp("Lesson 3: Username Mastery", generateHelpModeContent(3, selectedCategory, currentLang)) }
        )

        // LESSON 4
        Level1Lesson4Card(
            isCompleted = completedLessons.contains(104),
            selectedCategory = selectedCategory,
            currentLang = currentLang,
            onMissionComplete = { onLessonCompleted(104) },
            onOpenHelp = { onOpenSmartHelp("Lesson 4: Profile Photo DP", generateHelpModeContent(4, selectedCategory, currentLang)) }
        )

        // LESSON 5
        Level1Lesson5Card(
            isCompleted = completedLessons.contains(105),
            selectedCategory = selectedCategory,
            userGoal = userGoal,
            currentLang = currentLang,
            onMissionComplete = { onLessonCompleted(105) },
            onOpenHelp = { onOpenSmartHelp("Lesson 5: Bio Generator", generateHelpModeContent(5, selectedCategory, currentLang)) }
        )
    }
}

// =========================================================
// LESSON 1: ACCOUNT SETUP & DOWNLOAD
// =========================================================
@Composable
private fun Level1Lesson1Card(
    isCompleted: Boolean,
    currentLang: String,
    onMissionComplete: () -> Unit,
    onOpenHelp: () -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var userChoice by remember { mutableStateOf<String?>(null) } // "YES" or "NO"
    var isAccountCreatedConfirmed by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // LESSON HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("1", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("LESSON 1", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = VioletGlow)
                        Text("Account Setup & Installation", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // AI MENTOR STATEMENT
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = GlassBg,
                        border = BorderStroke(1.dp, GlassBorder)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                            Text("🤖", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("AI Mentor Guidance", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Perfect! 🔥 Ab hum sabse pehla step karenge.\nAaj ka target hai ek Brand Ready Instagram Account banana.",
                                    fontSize = 13.sp,
                                    color = TextWhite,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    // QUESTION & BUTTONS
                    Text("Instagram account pehle se bana hua hai?", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { userChoice = "YES" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (userChoice == "YES") SuccessGreen else GlassBg
                            ),
                            border = BorderStroke(1.dp, if (userChoice == "YES") SuccessGreen else VioletGlow),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("✅ Yes, Existing", fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { userChoice = "NO" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (userChoice == "NO") MagentaAccent else GlassBg
                            ),
                            border = BorderStroke(1.dp, if (userChoice == "NO") MagentaAccent else VioletGlow),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("➕ No, Need to Create", fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                        }
                    }

                    // IF USER SELECTS NO: SHOW DOWNLOAD & STEP-BY-STEP CREATION
                    if (userChoice == "NO") {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF1B112D),
                            border = BorderStroke(1.dp, VioletGlow.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("📥 Download Official Instagram App:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Button(
                                        onClick = {
                                            try {
                                                val intent = android.content.Intent(
                                                    android.content.Intent.ACTION_VIEW,
                                                    android.net.Uri.parse("https://play.google.com/store/apps/details?id=com.instagram.android")
                                                )
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Opening Play Store...", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = GlassBg),
                                        border = BorderStroke(1.dp, VioletGlow),
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("🤖 Play Store", fontSize = 11.5.sp, color = TextWhite)
                                    }

                                    Button(
                                        onClick = {
                                            try {
                                                val intent = android.content.Intent(
                                                    android.content.Intent.ACTION_VIEW,
                                                    android.net.Uri.parse("https://apps.apple.com/app/instagram/id389801252")
                                                )
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Opening App Store...", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = GlassBg),
                                        border = BorderStroke(1.dp, VioletGlow),
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("🍎 App Store", fontSize = 11.5.sp, color = TextWhite)
                                    }
                                }

                                Text("📝 Steps to Create Account:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("1. Open Instagram & tap 'Create new account'.", fontSize = 11.5.sp, color = TextMuted)
                                    Text("2. Enter phone number or email & verify OTP.", fontSize = 11.5.sp, color = TextMuted)
                                    Text("3. Choose a temporary username & strong password.", fontSize = 11.5.sp, color = TextMuted)
                                    Text("4. Fill basic profile info & sign in.", fontSize = 11.5.sp, color = TextMuted)
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Kya account successfully ban gaya?", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                                Button(
                                    onClick = { isAccountCreatedConfirmed = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isAccountCreatedConfirmed) SuccessGreen else MagentaAccent
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = if (isAccountCreatedConfirmed) "✅ Account Successfully Created!" else "✅ Ban Gaya! (Confirm Account Created)",
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }
                            }
                        }
                    }

                    // MISSION CARD & COMPLETE BUTTON
                    if (userChoice == "YES" || isAccountCreatedConfirmed) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF17291F),
                            border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(18.dp))
                                    Text("TODAY'S MISSION: Ensure Instagram App & Account Ready", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Log in or create your active Instagram account.", fontSize = 12.sp, color = TextWhite)
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = onMissionComplete,
                                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        enabled = !isCompleted
                                    ) {
                                        Text(if (isCompleted) "Mission Complete ✅ (+100 XP)" else "🎯 Mission Complete (+100 XP)", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    }

                                    IconButton(
                                        onClick = onOpenHelp,
                                        modifier = Modifier
                                            .background(GlassBg, RoundedCornerShape(12.dp))
                                            .border(1.dp, VioletGlow, RoundedCornerShape(12.dp))
                                    ) {
                                        Icon(Icons.Default.Lightbulb, contentDescription = "Help", tint = WarningAmber, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// LESSON 2: PROFESSIONAL CREATOR ACCOUNT
// =========================================================
@Composable
private fun Level1Lesson2Card(
    isCompleted: Boolean,
    selectedCategory: String,
    currentLang: String,
    onMissionComplete: () -> Unit,
    onOpenHelp: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var isActivatedConfirmed by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // LESSON HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("2", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("LESSON 2", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = VioletGlow)
                        Text("Switch to Professional Creator Account", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Aapka Personal account viral reach restrict karta hai. Creator Account me switch karne se 3x reach, full trending audio, aur deep analytics milte hain!",
                        fontSize = 13.sp,
                        color = TextWhite,
                        lineHeight = 18.sp
                    )

                    // COMPARISON CARDS
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = GlassBg,
                            border = BorderStroke(1.dp, GlassBorder)
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("👤", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Personal Account", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text("❌ No analytics • ❌ Limited music • ❌ No algorithm push", fontSize = 11.sp, color = TextMuted)
                                }
                            }
                        }

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF26123D),
                            border = BorderStroke(1.5.dp, MagentaAccent)
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("⭐", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Creator Account (RECOMMENDED ⭐)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)
                                    Text("✅ Viral reach boost • ✅ 100% Trending Audio • ✅ Detailed Insights", fontSize = 11.sp, color = TextWhite)
                                }
                            }
                        }

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = GlassBg,
                            border = BorderStroke(1.dp, GlassBorder)
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("💼", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Business Account", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text("⚠️ Restricted copyright music library (For local shops/businesses)", fontSize = 11.sp, color = TextMuted)
                                }
                            }
                        }
                    }

                    // SWITCH STEPS GUIDE
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF1A122B),
                        border = BorderStroke(1.dp, VioletGlow.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("📱 How to Switch to Creator Account:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            Text("1. Open Instagram Profile -> Tap ☰ Menu (Top right)", fontSize = 11.5.sp, color = TextMuted)
                            Text("2. Tap 'Settings and privacy' -> 'Account type and tools'", fontSize = 11.5.sp, color = TextMuted)
                            Text("3. Tap 'Switch to professional account'", fontSize = 11.5.sp, color = TextMuted)
                            Text("4. Select 'Creator' & choose category ($selectedCategory)", fontSize = 11.5.sp, color = TextMuted)

                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Kya Creator Account activate ho gaya?", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                            Button(
                                onClick = { isActivatedConfirmed = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isActivatedConfirmed) SuccessGreen else VioletGlow
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (isActivatedConfirmed) "✅ Creator Account Active!" else "✅ Activate Ho Gaya! (Confirm)",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                        }
                    }

                    // MISSION CARD
                    if (isActivatedConfirmed || isCompleted) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF17291F),
                            border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(18.dp))
                                    Text("TODAY'S MISSION: Activate Creator Account", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Switch your account type to Creator in Instagram settings.", fontSize = 12.sp, color = TextWhite)
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = onMissionComplete,
                                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        enabled = !isCompleted
                                    ) {
                                        Text(if (isCompleted) "Mission Complete ✅ (+100 XP)" else "🎯 Mission Complete (+100 XP)", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    }

                                    IconButton(
                                        onClick = onOpenHelp,
                                        modifier = Modifier
                                            .background(GlassBg, RoundedCornerShape(12.dp))
                                            .border(1.dp, VioletGlow, RoundedCornerShape(12.dp))
                                    ) {
                                        Icon(Icons.Default.Lightbulb, contentDescription = "Help", tint = WarningAmber, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// LESSON 3: USERNAME MASTERY & AI HANDLE SUGGESTER
// =========================================================
@Composable
private fun Level1Lesson3Card(
    isCompleted: Boolean,
    selectedCategory: String,
    currentLang: String,
    onMissionComplete: () -> Unit,
    onOpenHelp: () -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var currentHandleInput by remember { mutableStateOf("") }
    var refreshCount by remember { mutableIntStateOf(0) }

    val handleSuggestions = remember(selectedCategory, refreshCount) {
        generateCategoryUsernames(selectedCategory, refreshCount)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // LESSON HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("3", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("LESSON 3", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = VioletGlow)
                        Text("Username Mastery & AI Handle Suggester", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Aapka username aapka digital brand handle hai. Clean aur searchable username aapko search algorithm me 5x priority deta hai!",
                        fontSize = 13.sp,
                        color = TextWhite,
                        lineHeight = 18.sp
                    )

                    // GOOD VS BAD COMPARISON
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF2A141A),
                            border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("❌ Bad Username", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = ErrorRed)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("• rahul_1234_official_xx_99\n• Too long, symbols, numbers\n• Hard to search & type", fontSize = 10.5.sp, color = TextMuted, lineHeight = 15.sp)
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF132A1F),
                            border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("✅ Good Username", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("• @TechWithRahul\n• Niche keyword included\n• Easy to speak & remember", fontSize = 10.5.sp, color = TextWhite, lineHeight = 15.sp)
                            }
                        }
                    }

                    // INPUT CURRENT HANDLE
                    OutlinedTextField(
                        value = currentHandleInput,
                        onValueChange = { currentHandleInput = it },
                        label = { Text("Enter Your Current Handle (Optional)") },
                        placeholder = { Text("e.g. @yourname") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VioletGlow,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = VioletGlow,
                            unfocusedLabelColor = TextMuted
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    // AI SUGGESTER BOX
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF1D1130),
                        border = BorderStroke(1.dp, MagentaAccent.copy(alpha = 0.6f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("🤖 AI Generated Brandable Usernames:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)

                                Button(
                                    onClick = { refreshCount++ },
                                    colors = ButtonDefaults.buttonColors(containerColor = GlassBg),
                                    border = BorderStroke(1.dp, VioletGlow),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("🔄 Generate 5 New", fontSize = 10.5.sp, color = TextWhite)
                                }
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                handleSuggestions.forEach { handle ->
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp),
                                        color = GlassBg,
                                        border = BorderStroke(1.dp, GlassBorder)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = handle, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                                            IconButton(
                                                onClick = {
                                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                                    val clip = android.content.ClipData.newPlainText("Username", handle.removePrefix("@"))
                                                    clipboard.setPrimaryClip(clip)
                                                    Toast.makeText(context, "Copied $handle to Clipboard! 📋", Toast.LENGTH_SHORT).show()
                                                },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = VioletGlow, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // MISSION CARD
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF17291F),
                        border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(18.dp))
                                Text("TODAY'S MISSION: Update Username", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Set a clean, brandable username on your Instagram profile.", fontSize = 12.sp, color = TextWhite)
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = onMissionComplete,
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    enabled = !isCompleted
                                ) {
                                    Text(if (isCompleted) "Mission Complete ✅ (+100 XP)" else "🎯 Mission Complete (+100 XP)", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                }

                                IconButton(
                                    onClick = onOpenHelp,
                                    modifier = Modifier
                                        .background(GlassBg, RoundedCornerShape(12.dp))
                                        .border(1.dp, VioletGlow, RoundedCornerShape(12.dp))
                                ) {
                                    Icon(Icons.Default.Lightbulb, contentDescription = "Help", tint = WarningAmber, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// LESSON 4: PROFILE PHOTO (DP) MASTERY
// =========================================================
@Composable
private fun Level1Lesson4Card(
    isCompleted: Boolean,
    selectedCategory: String,
    currentLang: String,
    onMissionComplete: () -> Unit,
    onOpenHelp: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var isDpConfirmed by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // LESSON HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("4", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("LESSON 4", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = VioletGlow)
                        Text("Profile Photo (DP) Mastery", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Jab bhi aapka comment ya reel dikhta hai, aapki DP pehle 1 second me impression banati hai. High-contrast DP se profile clicks 300% badhte hain!",
                        fontSize = 13.sp,
                        color = TextWhite,
                        lineHeight = 18.sp
                    )

                    // RULES CHECKLIST
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF1B112D),
                        border = BorderStroke(1.dp, VioletGlow.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("📸 High-Impact DP Checklist:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("💡 Bright Lighting: Face bright & clearly visible in small thumbnail.", fontSize = 11.5.sp, color = TextMuted)
                                Text("🎨 High Contrast Background: Bright solid or vibrant background color.", fontSize = 11.5.sp, color = TextMuted)
                                Text("😊 Confident Expression: Direct eye contact & welcoming smile.", fontSize = 11.5.sp, color = TextMuted)
                                Text("⭕ Circle Crop Check: Head & shoulders centered inside circle crop.", fontSize = 11.5.sp, color = TextMuted)
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Kya profile photo update ho gayi?", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                            Button(
                                onClick = { isDpConfirmed = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isDpConfirmed) SuccessGreen else VioletGlow
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (isDpConfirmed) "✅ DP Updated Successfully!" else "✅ Photo Update Ho Gayi! (Confirm)",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                        }
                    }

                    // MISSION CARD
                    if (isDpConfirmed || isCompleted) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF17291F),
                            border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(18.dp))
                                    Text("TODAY'S MISSION: Set High Quality DP", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Upload a bright, well-lit profile picture on Instagram.", fontSize = 12.sp, color = TextWhite)
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = onMissionComplete,
                                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        enabled = !isCompleted
                                    ) {
                                        Text(if (isCompleted) "Mission Complete ✅ (+100 XP)" else "🎯 Mission Complete (+100 XP)", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    }

                                    IconButton(
                                        onClick = onOpenHelp,
                                        modifier = Modifier
                                            .background(GlassBg, RoundedCornerShape(12.dp))
                                            .border(1.dp, VioletGlow, RoundedCornerShape(12.dp))
                                    ) {
                                        Icon(Icons.Default.Lightbulb, contentDescription = "Help", tint = WarningAmber, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// LESSON 5: HIGH-CONVERTING BIO GENERATOR
// =========================================================
@Composable
private fun Level1Lesson5Card(
    isCompleted: Boolean,
    selectedCategory: String,
    userGoal: String,
    currentLang: String,
    onMissionComplete: () -> Unit,
    onOpenHelp: () -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var refreshCount by remember { mutableIntStateOf(0) }
    var selectedBioIndex by remember { mutableIntStateOf(0) }

    val generatedBios = remember(selectedCategory, userGoal, currentLang, refreshCount) {
        generateCategoryBios(selectedCategory, userGoal, currentLang, refreshCount)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // LESSON HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("5", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("LESSON 5", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = VioletGlow)
                        Text("High-Converting Bio Generator", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Aapki Bio aapki digital storefront copy hai! Ek high-converting Bio 3 seconds me batati hai aap kaun ho, kya value dete ho, aur kya action lena hai.",
                        fontSize = 13.sp,
                        color = TextWhite,
                        lineHeight = 18.sp
                    )

                    // AI BIO GENERATOR BOX
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF1D1130),
                        border = BorderStroke(1.dp, MagentaAccent.copy(alpha = 0.6f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("🤖 3 Custom AI Generated Bios:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)

                                Button(
                                    onClick = { refreshCount++ },
                                    colors = ButtonDefaults.buttonColors(containerColor = GlassBg),
                                    border = BorderStroke(1.dp, VioletGlow),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("🔄 Fresh Bios", fontSize = 10.5.sp, color = TextWhite)
                                }
                            }

                            // 3 BIOS
                            generatedBios.forEachIndexed { idx, bioText ->
                                val isSelected = selectedBioIndex == idx
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedBioIndex = idx },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) Color(0xFF2B1445) else GlassBg,
                                    border = BorderStroke(1.dp, if (isSelected) MagentaAccent else GlassBorder)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                            Text("Option ${idx + 1}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSelected) MagentaAccent else TextMuted)

                                            Button(
                                                onClick = {
                                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                                    val clip = android.content.ClipData.newPlainText("Bio", bioText)
                                                    clipboard.setPrimaryClip(clip)
                                                    Toast.makeText(context, "Copied Bio to Clipboard! 📋", Toast.LENGTH_SHORT).show()
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = GlassBg),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = TextWhite, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Copy Bio", fontSize = 10.sp, color = TextWhite)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = bioText, fontSize = 12.sp, color = TextWhite, lineHeight = 17.sp)
                                    }
                                }
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                TextButton(onClick = { refreshCount++ }) {
                                    Text("👎 Not Good? Generate New Set", fontSize = 11.sp, color = ErrorRed)
                                }
                            }
                        }
                    }

                    // MISSION CARD
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF17291F),
                        border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(18.dp))
                                Text("TODAY'S MISSION: Set High-Converting Bio", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Copy your favorite Bio above and update your Instagram profile.", fontSize = 12.sp, color = TextWhite)
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = onMissionComplete,
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    enabled = !isCompleted
                                ) {
                                    Text(if (isCompleted) "Mission Complete ✅ (+100 XP)" else "🎯 Mission Complete (+100 XP)", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                }

                                IconButton(
                                    onClick = onOpenHelp,
                                    modifier = Modifier
                                        .background(GlassBg, RoundedCornerShape(12.dp))
                                        .border(1.dp, VioletGlow, RoundedCornerShape(12.dp))
                                ) {
                                    Icon(Icons.Default.Lightbulb, contentDescription = "Help", tint = WarningAmber, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// LEVEL 1 REWARD CELEBRATION DIALOG
// =========================================================
@Composable
private fun Level1RewardCelebrationDialog(
    selectedCategory: String,
    currentLang: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF160A29),
            border = BorderStroke(2.dp, Brush.horizontalGradient(listOf(WarningAmber, MagentaAccent))),
            shadowElevation = 24.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(text = "🎉 LEVEL 1 COMPLETE! 🎉", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = WarningAmber, textAlign = TextAlign.Center)

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(GlassBg)
                        .border(2.dp, WarningAmber, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🏆", fontSize = 42.sp)
                }

                Text(
                    text = "BADGE UNLOCKED:\nProfile Builder",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Aapka Instagram Account ab 100% Brand Ready aur viral content ke liye optimized hai!",
                    fontSize = 13.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = GlassBg,
                    border = BorderStroke(1.dp, GlassBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "⚡ Earned Reward:", fontSize = 12.sp, color = TextWhite)
                        Text(text = "+500 XP", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                    }
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = MagentaAccent),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(text = "🚀 Continue Level 2 Journey", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }
        }
    }
}

// =========================================================
// HELPER FUNCTIONS FOR USERNAME & BIO GENERATION
// =========================================================
private fun generateCategoryUsernames(category: String, refreshCount: Int): List<String> {
    val cleanCategory = category.replace(" ", "")
    val setIndex = refreshCount % 3

    return when (setIndex) {
        0 -> listOf(
            "@${cleanCategory}WithMe",
            "@The${cleanCategory}Byte",
            "@${cleanCategory}Mastery",
            "@Daily${cleanCategory}Hacks",
            "@${cleanCategory}VerseIndia"
        )
        1 -> listOf(
            "@Future${cleanCategory}Guru",
            "@${cleanCategory}Insider",
            "@The${cleanCategory}Blueprint",
            "@Smart${cleanCategory}Lab",
            "@Real${cleanCategory}Talks"
        )
        else -> listOf(
            "@${cleanCategory}HubPro",
            "@NextGen${cleanCategory}",
            "@${cleanCategory}Uncut",
            "@${cleanCategory}Vista",
            "@Digital${cleanCategory}Zone"
        )
    }
}

private fun generateCategoryBios(category: String, goal: String, lang: String, refreshCount: Int): List<String> {
    val setIndex = refreshCount % 3

    return when (setIndex) {
        0 -> listOf(
            "🚀 $category Creator | Daily Hacks\n💡 Helping you grow faster\n👇 Free Creator Guide below",
            "🔥 Unboxing $category Secrets Everyday\n⚡ Best tips & reviews for 2026\n👇 Watch my latest reel here",
            "✨ $category Made Simple & Fun\n📩 Collabs & Inquiries: DM us\n👇 Tap link to join community"
        )
        1 -> listOf(
            "🤖 $category Hacks & Insights Everyday\n📈 Learn proven growth secrets\n👇 Download free PDF toolkit",
            "⚡ $category Insider | Featured in Top Reels\n🌟 Building a strong $category brand\n👇 Join our private Telegram group",
            "📱 Ultimate $category Blueprint for 2026\n💻 Tested strategies & tools\n👇 Click link for exclusive access"
        )
        else -> listOf(
            "💡 1-Minute $category Solutions\n🎓 Master your content journey\n👇 Tap link to start today",
            "💥 Stop making $category mistakes! Read this.\n🚀 Honest reviews & tricks\n👇 Check my bio link below",
            "🌐 Future $category Guides & Reviews\n🤝 Business inquiries: DM or Email\n👇 Tap link for full video"
        )
    }
}

private fun generateHelpModeContent(lessonNum: Int, category: String, lang: String): String {
    return when (lessonNum) {
        1 -> "📊 FLOWCHART STEP-BY-STEP:\nPlay Store -> Search 'Instagram' -> Download -> Register Mobile/Email -> Verify OTP -> Done!\n\n💡 SIMPLE ANALOGY:\nInstagram account banana kisi school me admission lene jaisa hai. Identity card (account) milne ke baad hi aap class (learning) start kar sakte hain."
        2 -> "📊 FLOWCHART STEP-BY-STEP:\nProfile -> ☰ Menu -> Settings & Privacy -> Account type & tools -> Switch to Professional -> Creator -> Select $category -> Done!\n\n💡 SIMPLE ANALOGY:\nPersonal Account ek normal bicycle ki tarah hai. Creator Account ek turbocharged supercar hai jisme speedometer (analytics) aur boost button (viral reach) milta hai!"
        3 -> "📊 FLOWCHART STEP-BY-STEP:\nProfile -> Edit Profile -> Username -> Type clean handle -> Check blue tick -> Save!\n\n💡 SIMPLE ANALOGY:\nUsername aapka dukan ke naam jaisa hai. Agar naam bohot lamba aur number se bhara ho toh log dukan bhool jayenge."
        4 -> "📊 FLOWCHART STEP-BY-STEP:\nClick DP -> New Profile Picture -> Choose bright face shot -> Center inside circle -> Save!\n\n💡 SIMPLE ANALOGY:\nDP aapki digital smile hai. Jab aap kisi party me jate hain toh ache kapde aur light me photo kheenchate hain, waise hi DP bhi clear honi chahiye."
        else -> "📊 FLOWCHART STEP-BY-STEP:\nCopy AI Bio -> Edit Profile -> Bio -> Paste -> Save!\n\n💡 SIMPLE ANALOGY:\nBio aapke dukan ka signboard hai. Customer dukan me ghasne se pehle signboard padhta hai ki yahan kya milta hai."
    }
}

// =========================================================
// PHASE 5 — LEVEL 3 REEL MASTERY JOURNEY CARD & COACH
// =========================================================

@Composable
private fun InstagramLevel3JourneyCard(
    completedCount: Int,
    totalCount: Int = 10,
    isAllCompleted: Boolean,
    currentLang: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(24.dp), ambientColor = MagentaAccent, spotColor = MagentaAccent),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF1F0E33),
        border = BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(MagentaAccent, WarningAmber)))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // LEVEL BADGE ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = VioletDeep,
                    border = BorderStroke(1.dp, MagentaAccent)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "🎬", fontSize = 14.sp)
                        Text(
                            text = "LEVEL 3 • REEL MASTERY COACH",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isAllCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg,
                    border = BorderStroke(1.dp, if (isAllCompleted) SuccessGreen else GlassBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isAllCompleted) Icons.Default.EmojiEvents else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isAllCompleted) WarningAmber else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (isAllCompleted) "Badge: Reel Creator 🎬" else "Badge Locked 🔒",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAllCompleted) WarningAmber else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Reel Mastery Coach 🎬",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite
            )

            // AI MENTOR STATEMENT
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(14.dp),
                color = GlassBg,
                border = BorderStroke(1.dp, GlassBorder)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Text("🤖", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "🔥 Ab asli game shuru hota hai. Followers bio se nahi... REELS se aate hain! Aaj hum tumhari pehli high-quality reel banayenge.",
                        fontSize = 12.5.sp,
                        color = TextWhite,
                        lineHeight = 17.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // METRICS ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(text = "📚 10 Steps", fontSize = 11.5.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    Text(text = "⏱️ 1–2 Hours", fontSize = 11.5.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    Text(text = "⚡ +1000 XP", fontSize = 11.5.sp, color = WarningAmber, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "$completedCount / $totalCount Completed",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isAllCompleted) SuccessGreen else MagentaAccent
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { completedCount.toFloat() / totalCount.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = if (isAllCompleted) SuccessGreen else WarningAmber,
                trackColor = GlassBg
            )
        }
    }
}

// =========================================================
// LEVEL 3 INTERACTIVE REEL COACH SECTION
// =========================================================
@Composable
private fun InstagramLevel3ReelCoachSection(
    completedLessons: Set<Int>,
    selectedCategory: String,
    userGoal: String,
    currentLang: String,
    onLessonCompleted: (Int) -> Unit,
    onOpenSmartHelp: (String, String) -> Unit
) {
    var selectedReelType by remember { mutableStateOf("Talking Head") }
    var selectedReelGoal by remember { mutableStateOf(userGoal.ifBlank { "Followers" }) }
    var selectedDuration by remember { mutableStateOf("30s") }
    var hookRefreshCount by remember { mutableIntStateOf(0) }
    var isVideoRecordedConfirmed by remember { mutableStateOf(false) }
    var selectedMistakeProblem by remember { mutableStateOf<String?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // STEP 1: CHOOSE REEL TYPE (301)
        Level3Lesson1TypeCard(
            isCompleted = completedLessons.contains(301),
            selectedType = selectedReelType,
            onSelectType = { selectedReelType = it },
            onMissionComplete = { onLessonCompleted(301) }
        )

        // STEP 2: GOAL (302)
        Level3Lesson2GoalCard(
            isCompleted = completedLessons.contains(302),
            selectedGoal = selectedReelGoal,
            onSelectGoal = { selectedReelGoal = it },
            onMissionComplete = { onLessonCompleted(302) }
        )

        // STEP 3: HOOK BUILDER (303)
        Level3Lesson3HookCard(
            isCompleted = completedLessons.contains(303),
            reelType = selectedReelType,
            selectedCategory = selectedCategory,
            selectedGoal = selectedReelGoal,
            refreshCount = hookRefreshCount,
            onRefreshHooks = { hookRefreshCount++ },
            onMissionComplete = { onLessonCompleted(303) }
        )

        // STEP 4: SCRIPT BUILDER (304)
        Level3Lesson4ScriptCard(
            isCompleted = completedLessons.contains(304),
            reelType = selectedReelType,
            selectedCategory = selectedCategory,
            selectedGoal = selectedReelGoal,
            selectedDuration = selectedDuration,
            onSelectDuration = { selectedDuration = it },
            onMissionComplete = { onLessonCompleted(304) }
        )

        // STEP 5: SHOT PLANNER (305)
        Level3Lesson5ShotPlannerCard(
            isCompleted = completedLessons.contains(305),
            reelType = selectedReelType,
            selectedCategory = selectedCategory,
            onMissionComplete = { onLessonCompleted(305) }
        )

        // STEP 6: CAMERA GUIDE (306)
        Level3Lesson6CameraGuideCard(
            isCompleted = completedLessons.contains(306),
            onMissionComplete = { onLessonCompleted(306) }
        )

        // STEP 7: RECORDING CHECKLIST (307)
        Level3Lesson7ChecklistCard(
            isCompleted = completedLessons.contains(307),
            onMissionComplete = { onLessonCompleted(307) }
        )

        // STEP 8: MISTAKE DETECTION & COACHING (308)
        Level3Lesson8MistakeDetectionCard(
            isCompleted = completedLessons.contains(308),
            isVideoRecorded = isVideoRecordedConfirmed,
            onConfirmRecorded = { isVideoRecordedConfirmed = it },
            selectedProblem = selectedMistakeProblem,
            onSelectProblem = { selectedMistakeProblem = it },
            onMissionComplete = { onLessonCompleted(308) }
        )

        // STEP 9: THUMBNAIL PLANNING (309)
        Level3Lesson9ThumbnailCard(
            isCompleted = completedLessons.contains(309),
            reelType = selectedReelType,
            selectedCategory = selectedCategory,
            onMissionComplete = { onLessonCompleted(309) }
        )

        // STEP 10: PRACTICE CHALLENGE (310)
        Level3Lesson10PracticeChallengeCard(
            isCompleted = completedLessons.contains(310),
            selectedType = selectedReelType,
            onMissionComplete = { onLessonCompleted(310) }
        )
    }
}

// =========================================================
// STEP 1: CHOOSE REEL TYPE (301)
// =========================================================
@Composable
private fun Level3Lesson1TypeCard(
    isCompleted: Boolean,
    selectedType: String,
    onSelectType: (String) -> Unit,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val reelTypes = listOf(
        InstagramQuadruple("Talking Head 🗣️", "Direct camera speak with subtitles", Icons.Default.SmartToy, "High Trust"),
        InstagramQuadruple("Voice Over 🎙️", "Screen/B-roll recording with audio commentary", Icons.Default.Mic, "Popular"),
        InstagramQuadruple("Cinematic 🎬", "Aesthetic shots with trending music", Icons.Default.Movie, "Viral"),
        InstagramQuadruple("Tutorial 💡", "Step-by-step problem solving", Icons.Default.AutoAwesome, "High Saves"),
        InstagramQuadruple("Motivation 🔥", "High energy quotes & mindset shift", Icons.Default.Star, "High Shares"),
        InstagramQuadruple("Storytelling 📖", "Personal story or transformation journey", Icons.Default.PlayArrow, "Retention"),
        InstagramQuadruple("Product Review 📦", "Unboxing & honest review", Icons.Default.CenterFocusStrong, "Sales"),
        InstagramQuadruple("Trending Reel 📈", "Adapting viral Audio/Format", Icons.Default.Refresh, "Fast Growth"),
        InstagramQuadruple("Meme Style 🎭", "Relatable humor with text overlay", Icons.Default.EmojiEvents, "Viral"),
        InstagramQuadruple("Other 🎯", "Custom niche specific style", Icons.Default.Lightbulb, "Flexible")
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("1", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 1", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Choose Reel Type 🎭", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Aaj kis type ki reel banana chahte ho? (Tap any card to select)", fontSize = 13.sp, color = TextWhite)

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(reelTypes) { item ->
                            val title = item.first
                            val desc = item.second
                            val badgeTag = item.fourth
                            val isSelected = selectedType.startsWith(title.take(10))

                            Surface(
                                modifier = Modifier
                                    .width(170.dp)
                                    .clickable { onSelectType(title) },
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) Color(0xFF2E1343) else GlassBg,
                                border = BorderStroke(1.5.dp, if (isSelected) MagentaAccent else GlassBorder)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        if (isSelected) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = desc, fontSize = 11.sp, color = TextMuted, lineHeight = 15.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = VioletDeep
                                    ) {
                                        Text(
                                            text = badgeTag,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = WarningAmber,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF17291F),
                        border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Selected: $selectedType", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                            Button(
                                onClick = onMissionComplete,
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                shape = RoundedCornerShape(10.dp),
                                enabled = !isCompleted
                            ) {
                                Text(if (isCompleted) "Saved ✅" else "Save & Next ➔", fontSize = 11.5.sp, color = TextWhite)
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 2: GOAL (302)
// =========================================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Level3Lesson2GoalCard(
    isCompleted: Boolean,
    selectedGoal: String,
    onSelectGoal: (String) -> Unit,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val goals = listOf("Followers 🚀", "Views 🔥", "Sales 💰", "Brand Deal 🤝", "Education 🎓", "Entertainment 😂")

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("2", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 2", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Reel Goal & Purpose 🎯", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Is reel ka main purpose kya hai?", fontSize = 13.sp, color = TextWhite)

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        goals.forEach { item ->
                            val isSelected = selectedGoal.contains(item.take(5))
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectGoal(item) },
                                label = { Text(item, fontSize = 12.sp, color = TextWhite) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MagentaAccent,
                                    containerColor = GlassBg
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = GlassBorder,
                                    selectedBorderColor = MagentaAccent,
                                    enabled = true,
                                    selected = isSelected
                                )
                            )
                        }
                    }

                    // AI ANALYSIS OF ANSWER
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1D1130),
                        border = BorderStroke(1.dp, VioletGlow)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("🤖 AI Strategy Analysis:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = when {
                                    selectedGoal.contains("Follower") -> "For Followers: High curios hook + Strong Call To Action ('Follow for Part 2')."
                                    selectedGoal.contains("Sales") -> "For Sales: Focus on customer pain point -> Show instant transformation -> 'DM word SALE'."
                                    selectedGoal.contains("Brand Deal") -> "For Brand Deals: High aesthetic quality + HD lighting + Tag brand handles."
                                    else -> "For Viral Views: Fast pacing + Relatable humor or shocking visual in 0-3s."
                                },
                                fontSize = 12.sp,
                                color = TextWhite,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Goal Saved ✅" else "Confirm Purpose 🎯", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 3: HOOK BUILDER (303)
// =========================================================
@Composable
private fun Level3Lesson3HookCard(
    isCompleted: Boolean,
    reelType: String,
    selectedCategory: String,
    selectedGoal: String,
    refreshCount: Int,
    onRefreshHooks: () -> Unit,
    onMissionComplete: () -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val hooks = remember(reelType, selectedCategory, selectedGoal, refreshCount) {
        generate10ReelHooks(reelType, selectedCategory, selectedGoal, refreshCount)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("3", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 3", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Hook Builder (0-3s Decision) ⚡", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "First 3 seconds decide everything! Below are 10 viral hooks generated specifically for your $selectedCategory ($reelType):",
                        fontSize = 12.5.sp,
                        color = TextWhite,
                        lineHeight = 17.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🔥 10 Unique Hooks", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)

                        Button(
                            onClick = onRefreshHooks,
                            colors = ButtonDefaults.buttonColors(containerColor = GlassBg),
                            border = BorderStroke(1.dp, VioletGlow),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("🔄 Generate Fresh Hooks", fontSize = 10.5.sp, color = TextWhite)
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        hooks.forEachIndexed { idx, hookText ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${idx + 1}. $hookText",
                                        fontSize = 12.sp,
                                        color = TextWhite,
                                        modifier = Modifier.weight(1f)
                                    )

                                    IconButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                            val clip = android.content.ClipData.newPlainText("Hook", hookText)
                                            clipboard.setPrimaryClip(clip)
                                            Toast.makeText(context, "Copied Hook #${idx + 1}! 📋", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = VioletGlow, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Hook Finalized ✅" else "Select Hook & Proceed ⚡", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 4: SCRIPT BUILDER (304)
// =========================================================
@Composable
private fun Level3Lesson4ScriptCard(
    isCompleted: Boolean,
    reelType: String,
    selectedCategory: String,
    selectedGoal: String,
    selectedDuration: String,
    onSelectDuration: (String) -> Unit,
    onMissionComplete: () -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val scriptData = remember(reelType, selectedCategory, selectedGoal, selectedDuration) {
        generateReelHumanScript(reelType, selectedCategory, selectedDuration, selectedGoal)
    }

    val durations = listOf("15s", "30s", "45s", "60s", "90s")

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("4", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 4", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Human-Style Script Builder 📝", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Select Reel Duration:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        durations.forEach { dur ->
                            val isSelected = selectedDuration == dur
                            Button(
                                onClick = { onSelectDuration(dur) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) MagentaAccent else GlassBg
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(dur, fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // SCRIPT CONTAINER
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF190F2E),
                        border = BorderStroke(1.dp, VioletGlow)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("📄 Generated Script ($selectedDuration)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = WarningAmber)

                                IconButton(
                                    onClick = {
                                        val fullScript = "HOOK: ${scriptData.first}\nBODY: ${scriptData.second}\nCTA: ${scriptData.third}\nTIP: ${scriptData.fourth}"
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                        val clip = android.content.ClipData.newPlainText("Script", fullScript)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Full Script Copied! 📋", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = VioletGlow, modifier = Modifier.size(16.dp))
                                }
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("⚡ HOOK (0-3s):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)
                                Text(scriptData.first, fontSize = 12.sp, color = TextWhite, lineHeight = 16.sp)

                                Spacer(modifier = Modifier.height(4.dp))
                                Text("💡 BODY & VALUE ($selectedDuration):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VioletGlow)
                                Text(scriptData.second, fontSize = 12.sp, color = TextWhite, lineHeight = 16.sp)

                                Spacer(modifier = Modifier.height(4.dp))
                                Text("🎯 CTA (CALL TO ACTION):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                Text(scriptData.third, fontSize = 12.sp, color = TextWhite, lineHeight = 16.sp)

                                Spacer(modifier = Modifier.height(4.dp))
                                Text("🎥 PRODUCTION TIP:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                                Text(scriptData.fourth, fontSize = 11.5.sp, color = TextMuted)
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Script Saved ✅" else "Approve Script & Continue 📝", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 5: SHOT PLANNER (305)
// =========================================================
@Composable
private fun Level3Lesson5ShotPlannerCard(
    isCompleted: Boolean,
    reelType: String,
    selectedCategory: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val shotList = remember(reelType, selectedCategory) {
        generateReelShotList(reelType, selectedCategory)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("5", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 5", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Shot Planner & B-Roll List 🎥", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Complete shot list for smooth recording:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        shotList.forEach { shot ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(text = shot.first, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)
                                    Text(text = "📷 Angle & Movement: ${shot.second}", fontSize = 11.5.sp, color = TextWhite)
                                    Text(text = "😃 Expression: ${shot.third}", fontSize = 11.5.sp, color = TextWhite)
                                    Text(text = "⏱️ Pause & B-Roll: ${shot.fourth}", fontSize = 11.sp, color = TextMuted)
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Shots Planned ✅" else "Save Shot List & Next 🎥", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 6: CAMERA GUIDE (306)
// =========================================================
@Composable
private fun Level3Lesson6CameraGuideCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("6", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 6", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Camera & Studio Setup Guide 📱", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Master phone position & studio lighting in 1 minute:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("📱 Phone Position: Eye level camera height (Use stack of books or tripod). Never shoot from below chin!", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("💡 Lighting: Face the window (Natural light) or place Ring light behind phone. Avoid light source behind your head!", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("🖼️ Background & Framing: Clean, clutter-free room or neat desk. Keep head in top 30% grid.", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("👀 Eye Contact & Audio: Look directly into lens (NOT your screen image!). Speak clearly with mic 6 inches away.", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Camera Ready ✅" else "Setup Complete 📱", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 7: RECORDING CHECKLIST (307)
// =========================================================
@Composable
private fun Level3Lesson7ChecklistCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    var check1 by remember { mutableStateOf(false) }
    var check2 by remember { mutableStateOf(false) }
    var check3 by remember { mutableStateOf(false) }
    var check4 by remember { mutableStateOf(false) }
    var check5 by remember { mutableStateOf(false) }

    val allChecked = check1 && check2 && check3 && check4 && check5

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("7", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 7", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Pre-Recording Checklist ✔️", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Confirm all 5 checks before hitting Record:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { check1 = !check1 }) {
                            Checkbox(checked = check1, onCheckedChange = { check1 = it }, colors = CheckboxDefaults.colors(checkedColor = SuccessGreen))
                            Text("📷 Clean Lens (Wipe camera with microfiber fabric)", fontSize = 12.sp, color = TextWhite)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { check2 = !check2 }) {
                            Checkbox(checked = check2, onCheckedChange = { check2 = it }, colors = CheckboxDefaults.colors(checkedColor = SuccessGreen))
                            Text("🎥 Camera Quality Set to 1080p @ 60fps (or 4K 30fps)", fontSize = 12.sp, color = TextWhite)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { check3 = !check3 }) {
                            Checkbox(checked = check3, onCheckedChange = { check3 = it }, colors = CheckboxDefaults.colors(checkedColor = SuccessGreen))
                            Text("💡 Good Lighting (No shadow or overexposed face)", fontSize = 12.sp, color = TextWhite)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { check4 = !check4 }) {
                            Checkbox(checked = check4, onCheckedChange = { check4 = it }, colors = CheckboxDefaults.colors(checkedColor = SuccessGreen))
                            Text("📐 Stable Shot (Phone mounted on tripod or firm support)", fontSize = 12.sp, color = TextWhite)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { check5 = !check5 }) {
                            Checkbox(checked = check5, onCheckedChange = { check5 = it }, colors = CheckboxDefaults.colors(checkedColor = SuccessGreen))
                            Text("🎙️ Mic & Noise Check (Quiet room, minimal fan sound)", fontSize = 12.sp, color = TextWhite)
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = if (allChecked || isCompleted) SuccessGreen else GlassBg),
                        border = BorderStroke(1.dp, if (allChecked) SuccessGreen else VioletGlow),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Checklist Confirmed ✅" else "Confirm All Checks ✔️", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 8: MISTAKE DETECTION & AI COACHING (308)
// =========================================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Level3Lesson8MistakeDetectionCard(
    isCompleted: Boolean,
    isVideoRecorded: Boolean,
    onConfirmRecorded: (Boolean) -> Unit,
    selectedProblem: String?,
    onSelectProblem: (String) -> Unit,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val problems = listOf("Lighting 💡", "Noise 🎙️", "Confidence 🗣️", "Background 🖼️", "Blur 🔍", "Camera Shake 📳")

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("8", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 8", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("AI Mistake Detection & Coach 🛠️", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Video record ho gayi?", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { onConfirmRecorded(true) },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isVideoRecorded) SuccessGreen else GlassBg),
                            border = BorderStroke(1.dp, if (isVideoRecorded) SuccessGreen else VioletGlow),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("✅ YES, Recorded!", fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onConfirmRecorded(false) },
                            colors = ButtonDefaults.buttonColors(containerColor = GlassBg),
                            border = BorderStroke(1.dp, VioletGlow),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("⏳ Not Yet", fontSize = 12.sp, color = TextWhite)
                        }
                    }

                    if (isVideoRecorded) {
                        Text("Koi problem aayi recording me?", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            problems.forEach { prob ->
                                val isSelected = selectedProblem == prob
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onSelectProblem(prob) },
                                    label = { Text(prob, fontSize = 11.5.sp, color = TextWhite) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MagentaAccent,
                                        containerColor = GlassBg
                                    )
                                )
                            }
                        }

                        if (selectedProblem != null) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF1D1130),
                                border = BorderStroke(1.dp, VioletGlow)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("🤖 AI Personal Coach Solution:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = generateAiMistakeSolution(selectedProblem),
                                        fontSize = 12.sp,
                                        color = TextWhite,
                                        lineHeight = 16.5.sp
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = onMissionComplete,
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isCompleted
                        ) {
                            Text(if (isCompleted) "Mistakes Fixed ✅" else "Confirm Fix & Next 🛠️", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 9: THUMBNAIL PLANNING (309)
// =========================================================
@Composable
private fun Level3Lesson9ThumbnailCard(
    isCompleted: Boolean,
    reelType: String,
    selectedCategory: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val thumbnailIdeas = remember(reelType, selectedCategory) {
        generateReelThumbnailIdeas(reelType, selectedCategory)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("9", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 9", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Thumbnail & Cover Design 🎨", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("5 High-Converting Thumbnail Cover Ideas:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        thumbnailIdeas.forEachIndexed { idx, idea ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("Idea #${idx + 1}: ${idea.first}", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)
                                    Text("👤 Face Position: ${idea.second}", fontSize = 11.5.sp, color = TextWhite)
                                    Text("🎨 Colors & BG: ${idea.third}", fontSize = 11.5.sp, color = TextWhite)
                                    Text("🔥 Style: ${idea.fourth}", fontSize = 11.sp, color = TextMuted)
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Thumbnail Planned ✅" else "Save Cover Ideas 🎨", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 10: PRACTICE CHALLENGE (310)
// =========================================================
@Composable
private fun Level3Lesson10PracticeChallengeCard(
    isCompleted: Boolean,
    selectedType: String,
    onMissionComplete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF1B2E24),
        border = BorderStroke(1.5.dp, SuccessGreen)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(24.dp))
                Column {
                    Text("PRACTICE MISSION • LEVEL 3 FINAL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                    Text("Record Your First Reel 🎬", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite)
                }
            }

            Text(
                text = "Record your $selectedType reel on your camera or Instagram app. (Upload optional, just complete recording today!)",
                fontSize = 12.5.sp,
                color = TextWhite,
                lineHeight = 17.sp
            )

            Button(
                onClick = onMissionComplete,
                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                enabled = !isCompleted
            ) {
                Text(
                    text = if (isCompleted) "Level 3 Complete! 🎉 (+1000 XP)" else "🎯 Mission Complete (+1000 XP)",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }
        }
    }
}

// =========================================================
// LEVEL 3 REWARD CELEBRATION DIALOG
// =========================================================
@Composable
private fun Level3RewardCelebrationDialog(
    selectedCategory: String,
    currentLang: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF1E0E33),
            border = BorderStroke(2.dp, Brush.horizontalGradient(listOf(WarningAmber, MagentaAccent)))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("🎬✨🏆", fontSize = 48.sp)

                Text(
                    text = "LEVEL 3 COMPLETE!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = WarningAmber
                )

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = VioletDeep,
                    border = BorderStroke(1.dp, MagentaAccent)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("BADGE UNLOCKED", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)
                        Text("Reel Creator 🎬", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("⚡ +1000 XP EARNED", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                    }
                }

                Text(
                    text = "Congratulations! You have mastered reel types, hooks, scripts, shot planning, lighting, checklist & thumbnail design for $selectedCategory!",
                    fontSize = 12.5.sp,
                    color = TextWhite,
                    lineHeight = 17.5.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = MagentaAccent),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Continue Journey 🚀", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }
        }
    }
}

// =========================================================
// GENERATOR FUNCTIONS FOR REEL HOOKS, SCRIPTS, SHOTS & THUMBNAILS
// =========================================================
private fun generate10ReelHooks(reelType: String, category: String, goal: String, refreshCount: Int): List<String> {
    val set = refreshCount % 2
    return if (set == 0) {
        listOf(
            "Stop doing this $category mistake right now!",
            "3 $category secrets that experts never tell you...",
            "If you want $goal in 2026, watch this till the end!",
            "I tried this $category hack for 7 days, here's what happened...",
            "Nobody talks about this $category shortcut!",
            "Do this 1 thing to boost your $category reach instantly.",
            "Why 90% of $category creators fail at $goal...",
            "Save this reel before Instagram deletes it!",
            "Steal my exact $category framework for $goal.",
            "The simplest way to master $category in 2026."
        )
    } else {
        listOf(
            "How I got $goal using just 1 $category reel!",
            "This 30-second $category trick changes everything.",
            "Don't post another $category video without checking this!",
            "The dark truth about growing in $category...",
            "3 tools every $category creator MUST use in 2026.",
            "How to go viral in $category with 0 followers.",
            "Watch what happens when you try this $category method!",
            "1 simple tweak to double your $category results.",
            "Is this the ultimate $category strategy for $goal?",
            "3 hidden $category features you are not using!"
        )
    }
}

private fun generateReelHumanScript(reelType: String, category: String, duration: String, goal: String): InstagramQuadruple<String, String, String, String> {
    return InstagramQuadruple(
        first = "Stop wasting time on generic $category advice! If you want $goal, here is the exact 3-step method you need right now.",
        second = "Step 1: Focus on clear high-contrast visual framing. Step 2: Deliver 1 actionable tip without fluff or silence. Step 3: Keep pacing fast with automatic text subtitles.",
        third = "Comment 'GUIDE' below and I'll DM you the free $category blueprint! Follow for daily growth hacks.",
        fourth = "Pro Tip: Speak with energetic tone, keep camera at eye-level, and maintain eye contact with the camera lens!"
    )
}

private fun generateReelShotList(reelType: String, category: String): List<InstagramQuadruple<String, String, String, String>> {
    return listOf(
        InstagramQuadruple("Scene 1: Hook (0-3s)", "Close-up eye level, fast zoom in", "Shocked / Pointing to text", "0.5s pause, sound effect"),
        InstagramQuadruple("Scene 2: Core Value (3-15s)", "Medium shot talking head / screen recording", "Confident smile & clear hands gesture", "Overlay 3 text bullet points"),
        InstagramQuadruple("Scene 3: Proof / B-Roll (15-25s)", "Over-the-shoulder / product angle", "Focused action / demonstration", "Fast cut every 1.5 seconds"),
        InstagramQuadruple("Scene 4: Call to Action (25-30s)", "Medium close-up looking at camera", "Warm inviting smile, pointing down", "Pop up animated 'Follow' button")
    )
}

private fun generateReelThumbnailIdeas(reelType: String, category: String): List<InstagramQuadruple<String, String, String, String>> {
    return listOf(
        InstagramQuadruple("Shocked Face + Big Bold Text", "Right side close-up face", "High contrast Neon Yellow & Black", "High CTR curiosity trigger"),
        InstagramQuadruple("Before vs After Split Screen", "Centered side-by-side", "Red & Green status accents", "Instant transformation proof"),
        InstagramQuadruple("Product / Screen + Pointer Arrow", "Center object framing", "Dark aesthetic background with bright glow", "Clean & modern professional style"),
        InstagramQuadruple("3 Bullet Points Preview", "Left side face, right side text", "Purple gradient & white bold typography", "High save rate educational look"),
        InstagramQuadruple("Minimalist One-Word Teaser", "Center headshot with ambient backlight", "Off-white clean text framing", "Aesthetic viral creator vibe")
    )
}

private fun generateAiMistakeSolution(problem: String): String {
    return when {
        problem.contains("Lighting") -> "💡 Lighting Solution: Stand facing the brightest window or place a ring light directly behind your phone at eye level. Never have a bright bulb behind you!"
        problem.contains("Noise") -> "🎙️ Noise Solution: Record in a carpeted room or closet with clothes. Turn off fans and AC while recording."
        problem.contains("Confidence") -> "🗣️ Confidence Solution: Script your text in 3-word bullet points. Record 1 sentence at a time and edit cuts together."
        problem.contains("Background") -> "🖼️ Background Solution: Clear all visible trash/clothes. Place a small desk lamp or warm artificial light behind you."
        problem.contains("Blur") -> "🔍 Blur Solution: Clean your camera lens with a microfiber cloth and tap your face on screen to lock focus before recording."
        else -> "📳 Camera Shake Solution: Rest phone against a mug or book stack if you don't have a tripod. Keep elbows on table while holding phone."
    }
}

// =========================================================
// PHASE 6 — LEVEL 4 VIDEO EDITING MASTERY JOURNEY CARD
// =========================================================

@Composable
private fun InstagramLevel4JourneyCard(
    completedCount: Int,
    totalCount: Int = 14,
    isAllCompleted: Boolean,
    currentLang: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(24.dp), ambientColor = VioletGlow, spotColor = VioletGlow),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF140D2B),
        border = BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(VioletGlow, MagentaAccent)))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = VioletDeep,
                    border = BorderStroke(1.dp, VioletGlow)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "✂️", fontSize = 14.sp)
                        Text(
                            text = "LEVEL 4 • VIDEO EDITING COACH",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isAllCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg,
                    border = BorderStroke(1.dp, if (isAllCompleted) SuccessGreen else GlassBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isAllCompleted) Icons.Default.EmojiEvents else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isAllCompleted) WarningAmber else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (isAllCompleted) "Badge: Video Editing Master ✂️" else "Badge Locked 🔒",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAllCompleted) WarningAmber else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Video Editing Mastery ✂️",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite
            )

            // AI MENTOR STATEMENT
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(14.dp),
                color = GlassBg,
                border = BorderStroke(1.dp, GlassBorder)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Text("🤖", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "🔥 Great! Tumhari reel record ho chuki hai. Ab usko professional banana hai. Editing hi normal reel aur viral reel ke beech ka difference hota hai.",
                        fontSize = 12.5.sp,
                        color = TextWhite,
                        lineHeight = 17.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // METRICS ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(text = "📚 14 Steps", fontSize = 11.5.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    Text(text = "⏱️ 1–2 Hours", fontSize = 11.5.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    Text(text = "⚡ +1500 XP", fontSize = 11.5.sp, color = WarningAmber, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "$completedCount / $totalCount Completed",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isAllCompleted) SuccessGreen else VioletGlow
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { completedCount.toFloat() / totalCount.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = if (isAllCompleted) SuccessGreen else VioletGlow,
                trackColor = GlassBg
            )
        }
    }
}

// =========================================================
// LEVEL 4 INTERACTIVE EDITING COACH SECTION
// =========================================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InstagramLevel4EditingCoachSection(
    completedLessons: Set<Int>,
    selectedCategory: String,
    userGoal: String,
    currentLang: String,
    onLessonCompleted: (Int) -> Unit,
    onOpenSmartHelp: (String, String) -> Unit
) {
    var selectedEditingApp by remember { mutableStateOf("CapCut") }
    var userEditingLevel by remember { mutableStateOf("Never") }
    var hasImportedClips by remember { mutableStateOf(false) }
    var selectedTroubleIssue by remember { mutableStateOf<String?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // STEP 1: CHOOSE EDITING APP (401)
        Level4Lesson1AppCard(
            isCompleted = completedLessons.contains(401),
            selectedApp = selectedEditingApp,
            onSelectApp = { selectedEditingApp = it },
            onMissionComplete = { onLessonCompleted(401) }
        )

        // STEP 2: BEGINNER CHECK (402)
        Level4Lesson2LevelCard(
            isCompleted = completedLessons.contains(402),
            userLevel = userEditingLevel,
            onSelectLevel = { userEditingLevel = it },
            onMissionComplete = { onLessonCompleted(402) }
        )

        // STEP 3: WORKSPACE TOUR (403)
        Level4Lesson3WorkspaceTourCard(
            isCompleted = completedLessons.contains(403),
            selectedApp = selectedEditingApp,
            onMissionComplete = { onLessonCompleted(403) }
        )

        // STEP 4: IMPORT VIDEO (404)
        Level4Lesson4ImportCard(
            isCompleted = completedLessons.contains(404),
            hasImported = hasImportedClips,
            onConfirmImport = { hasImportedClips = it },
            onMissionComplete = { onLessonCompleted(404) }
        )

        // STEP 5: CUT & TRIM (405)
        Level4Lesson5CutTrimCard(
            isCompleted = completedLessons.contains(405),
            onMissionComplete = { onLessonCompleted(405) }
        )

        // STEP 6: TRANSITIONS (406)
        Level4Lesson6TransitionsCard(
            isCompleted = completedLessons.contains(406),
            onMissionComplete = { onLessonCompleted(406) }
        )

        // STEP 7: TEXT ANIMATION (407)
        Level4Lesson7TextAnimationCard(
            isCompleted = completedLessons.contains(407),
            onMissionComplete = { onLessonCompleted(407) }
        )

        // STEP 8: AUTO CAPTIONS (408)
        Level4Lesson8AutoCaptionsCard(
            isCompleted = completedLessons.contains(408),
            selectedApp = selectedEditingApp,
            onMissionComplete = { onLessonCompleted(408) }
        )

        // STEP 9: MUSIC & AUDIO (409)
        Level4Lesson9MusicAudioCard(
            isCompleted = completedLessons.contains(409),
            onMissionComplete = { onLessonCompleted(409) }
        )

        // STEP 10: EFFECTS & FILTERS (410)
        Level4Lesson10EffectsCard(
            isCompleted = completedLessons.contains(410),
            onMissionComplete = { onLessonCompleted(410) }
        )

        // STEP 11: COLOUR CORRECTION (411)
        Level4Lesson11ColourCorrectionCard(
            isCompleted = completedLessons.contains(411),
            onMissionComplete = { onLessonCompleted(411) }
        )

        // STEP 12: EXPORT SETTINGS (412)
        Level4Lesson12ExportSettingsCard(
            isCompleted = completedLessons.contains(412),
            selectedApp = selectedEditingApp,
            onMissionComplete = { onLessonCompleted(412) }
        )

        // STEP 13: AI EDITING REVIEW & TROUBLESHOOTER (413)
        Level4Lesson13EditingReviewCard(
            isCompleted = completedLessons.contains(413),
            selectedIssue = selectedTroubleIssue,
            onSelectIssue = { selectedTroubleIssue = it },
            onMissionComplete = { onLessonCompleted(413) }
        )

        // STEP 14: PRACTICE CHALLENGE & EXPORT REEL (414)
        Level4Lesson14PracticeExportCard(
            isCompleted = completedLessons.contains(414),
            selectedApp = selectedEditingApp,
            onMissionComplete = { onLessonCompleted(414) }
        )
    }
}

// =========================================================
// STEP 1: CHOOSE EDITING APP (401)
// =========================================================
@Composable
private fun Level4Lesson1AppCard(
    isCompleted: Boolean,
    selectedApp: String,
    onSelectApp: (String) -> Unit,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val appList = listOf(
        InstagramQuadruple("CapCut 🎬", "Most popular, powerful auto captions & templates", Icons.Default.Movie, "Recommended"),
        InstagramQuadruple("VN Editor 📱", "Clean UI, no watermark, professional timeline", Icons.Default.Smartphone, "No Watermark"),
        InstagramQuadruple("Instagram Edits 📸", "Built-in IG editor, direct music sync", Icons.Default.CameraAlt, "Built-in"),
        InstagramQuadruple("Other App 🛠️", "KineMaster, InShot, Premiere Rush, etc.", Icons.Default.Build, "Custom")
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("1", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 1", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Choose Editing App 📲", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Tum kaunsa app use karke edit karoge? (Selected choice saved permanently):", fontSize = 12.5.sp, color = TextWhite)

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(appList) { item ->
                            val name = item.first
                            val desc = item.second
                            val tag = item.fourth
                            val isSelected = selectedApp.startsWith(name.take(5))

                            Surface(
                                modifier = Modifier
                                    .width(160.dp)
                                    .clickable { onSelectApp(name) },
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) Color(0xFF281140) else GlassBg,
                                border = BorderStroke(1.5.dp, if (isSelected) VioletGlow else GlassBorder)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        if (isSelected) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = desc, fontSize = 11.sp, color = TextMuted, lineHeight = 15.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = VioletDeep
                                    ) {
                                        Text(
                                            text = tag,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = WarningAmber,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF17291F),
                        border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Saved App: $selectedApp", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                            Button(
                                onClick = onMissionComplete,
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                shape = RoundedCornerShape(10.dp),
                                enabled = !isCompleted
                            ) {
                                Text(if (isCompleted) "Saved ✅" else "Save Choice ➔", fontSize = 11.5.sp, color = TextWhite)
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 2: BEGINNER CHECK (402)
// =========================================================
@Composable
private fun Level4Lesson2LevelCard(
    isCompleted: Boolean,
    userLevel: String,
    onSelectLevel: (String) -> Unit,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val levels = listOf(
        "Never 🐣" to "First time opening video editor",
        "Little Experience 🌱" to "Know basic cut and background music",
        "Intermediate 🚀" to "Understand timeline, text & keyframes",
        "Advanced 🔥" to "Master colour grading & audio mixing"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("2", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 2", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Beginner Experience Level 🎓", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Kya tumne pehle kabhi video editing ki hai?", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        levels.forEach { (lvl, desc) ->
                            val isSelected = userLevel.startsWith(lvl.take(5))
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectLevel(lvl) },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Color(0xFF281140) else GlassBg,
                                border = BorderStroke(1.5.dp, if (isSelected) VioletGlow else GlassBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = lvl, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text(text = desc, fontSize = 11.5.sp, color = TextMuted)
                                    }
                                    if (isSelected) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                                    }
                                }
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1D1130),
                        border = BorderStroke(1.dp, VioletGlow)
                    ) {
                        Text(
                            text = "🤖 AI Coach Note: Adjusted guide pacing specifically for '$userLevel' level!",
                            fontSize = 12.sp,
                            color = WarningAmber,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Saved ✅" else "Save & Continue ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 3: WORKSPACE TOUR (403)
// =========================================================
@Composable
private fun Level4Lesson3WorkspaceTourCard(
    isCompleted: Boolean,
    selectedApp: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val tourList = remember(selectedApp) {
        generateEditingWorkspaceTour(selectedApp)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("3", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 3", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Workspace Tour ($selectedApp) 🖥️", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Understanding your $selectedApp editor layout:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        tourList.forEach { item ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(item.first, fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(item.second, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text(item.third, fontSize = 11.5.sp, color = TextMuted, lineHeight = 16.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(item.fourth, fontSize = 10.5.sp, color = WarningAmber, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Tour Complete ✅" else "Got It! Continue ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 4: IMPORT VIDEO (404)
// =========================================================
@Composable
private fun Level4Lesson4ImportCard(
    isCompleted: Boolean,
    hasImported: Boolean,
    onConfirmImport: (Boolean) -> Unit,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("4", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 4", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Import Recorded Clips 📥", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = GlassBg,
                        border = BorderStroke(1.dp, GlassBorder)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("👉 Open your editing app -> Tap 'New Project' -> Select all clips recorded in Phase 5 -> Tap 'Add / Import'.", fontSize = 12.5.sp, color = TextWhite, lineHeight = 17.sp)
                            Text("💡 Tip: Import clips in chronological order (Hook -> Body -> CTA) to save trimming time!", fontSize = 11.5.sp, color = WarningAmber)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Clips imported into editor?", fontSize = 12.5.sp, color = TextWhite, fontWeight = FontWeight.Medium)

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { onConfirmImport(true) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (hasImported) SuccessGreen else GlassBg
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("YES ✅", fontSize = 11.5.sp, color = TextWhite)
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted && hasImported
                    ) {
                        Text(if (isCompleted) "Import Confirmed ✅" else if (hasImported) "Proceed To Cut & Trim ➔" else "Please import clips first", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 5: CUT & TRIM (405)
// =========================================================
@Composable
private fun Level4Lesson5CutTrimCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("5", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 5", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Cut, Trim & Dead Space Removal ✂️", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Remove all silences, breath pauses, and mistakes:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("✂️ SPLIT: Tap clip where you start speaking -> Tap Split/Cut button.", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("🗑️ DELETE: Tap silence/mistake part -> Tap Delete.", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("⚡ JUMP CUT RULE: Keep zero gaps between words. Fast pacing boosts audience retention by 40%!", fontSize = 11.5.sp, color = WarningAmber, modifier = Modifier.padding(10.dp))
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Trimming Complete ✅" else "Mission Complete (Dead Space Removed) ✂️", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 6: TRANSITIONS (406)
// =========================================================
@Composable
private fun Level4Lesson6TransitionsCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("6", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 6", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Smooth Transitions Guide 🔄", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF1B3D23),
                            border = BorderStroke(1.dp, SuccessGreen)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("✅ WHEN TO USE", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("• Between different scenes\n• Whip Pan / Zoom In on topic switch\n• Keep duration short (0.2s - 0.3s)", fontSize = 11.sp, color = TextWhite, lineHeight = 15.sp)
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF381519),
                            border = BorderStroke(1.dp, Color(0xFFFF5252))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("❌ WHEN NOT TO USE", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF5252))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("• Never use tacky 3D spin / heart wipes\n• Avoid transitions on talking head cuts\n• Don't overdo transitions on every clip", fontSize = 11.sp, color = TextWhite, lineHeight = 15.sp)
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Transitions Applied ✅" else "Transitions Understood ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 7: TEXT ANIMATION (407)
// =========================================================
@Composable
private fun Level4Lesson7TextAnimationCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("7", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 7", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Text Overlay & Typography Aesthetics 🔤", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Styling text overlay for clean reading experience:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("🔤 FONTS: Use bold sans-serif fonts (e.g., Montserrat Bold, Futura, Helvetica). Avoid cursive/script fonts!", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("🎨 COLOURS: High contrast! White text with black shadow/stroke, or Yellow text (#FFE500) for key words.", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("📍 POSITIONING: Place text in center-chest zone. Keep away from top profile bar & bottom reel buttons safe zones!", fontSize = 11.5.sp, color = WarningAmber, modifier = Modifier.padding(10.dp))
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Text Style Applied ✅" else "Text Styled & Ready ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 8: AUTO CAPTIONS (408)
// =========================================================
@Composable
private fun Level4Lesson8AutoCaptionsCard(
    isCompleted: Boolean,
    selectedApp: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("8", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 8", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Auto Captions & Subtitles Setup 💬", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("80% of users watch reels on mute! Captions are mandatory:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("1. GENERATE: Tap 'Text' -> Tap 'Auto Captions' -> Select language (Hindi / English / Hinglish) -> Tap Generate.", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("2. EDIT SPELLING: Read through captions and correct any AI typos or Hindi word spelling errors.", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("3. DYNAMIC ANIMATION: Choose 'Active Word Highlight' template (Yellow glow on spoken word).", fontSize = 11.5.sp, color = WarningAmber, modifier = Modifier.padding(10.dp))
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Captions Generated ✅" else "Auto Captions Ready 💬", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 9: MUSIC & AUDIO (409)
// =========================================================
@Composable
private fun Level4Lesson9MusicAudioCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("9", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 9", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Music & Audio Mixing Balance 🎵", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Perfect audio ratio rules:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("🗣️ VOICE / ORIGINAL AUDIO: Keep at 100% volume level.", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("🎶 BACKGROUND MUSIC: Reduce volume to 10% - 15%! Music must never drown out your voice.", fontSize = 11.5.sp, color = WarningAmber, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("⚡ TRENDING AUDIO SYNC: Align cut transitions to background music beat drop for maximum dopamine!", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Audio Mixed ✅" else "Audio Balanced & Synced 🎵", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 10: EFFECTS & FILTERS (410)
// =========================================================
@Composable
private fun Level4Lesson10EffectsCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("10", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 10", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Effects & Filter Rules 🎨", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Avoid over-editing! Less is more for high-value reels:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("✨ Use subtle skin smoothing or soft glow. Avoid distortion / face morphing filters.", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("💥 Use sound effects (swoosh, pop, bell) ONLY on key text popups.", fontSize = 11.5.sp, color = WarningAmber, modifier = Modifier.padding(10.dp))
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Effects Verified ✅" else "Effects Approved 🎨", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 11: COLOUR CORRECTION (411)
// =========================================================
@Composable
private fun Level4Lesson11ColourCorrectionCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("11", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 11", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Colour Correction & HD Punch ☀️", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Golden color recipe for crisp mobile HD video:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("☀️ Brightness: +5 to +10 (Make video pop on OLED screens)", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("🌗 Contrast: +8 (Deeper blacks and vivid details)", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("🎨 Saturation: +5 (Warm natural skin tones)", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("🗡️ Sharpen: +15 (Instantly removes video compression blur!)", fontSize = 11.5.sp, color = WarningAmber, modifier = Modifier.padding(10.dp))
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Colour Graded ✅" else "Apply HD Recipe ☀️", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 12: EXPORT SETTINGS (412)
// =========================================================
@Composable
private fun Level4Lesson12ExportSettingsCard(
    isCompleted: Boolean,
    selectedApp: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val exportSpecs = remember(selectedApp) {
        generateEditingExportGuide(selectedApp)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("12", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 12", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Export Settings ($selectedApp) ⚙️", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Optimal export settings for Instagram upload algorithm:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        exportSpecs.forEach { spec ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(spec.first, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VioletGlow)
                                    Text(spec.second, fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Settings Configured ✅" else "Export Settings Configured ⚙️", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 13: AI EDITING REVIEW & TROUBLESHOOTER (413)
// =========================================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Level4Lesson13EditingReviewCard(
    isCompleted: Boolean,
    selectedIssue: String?,
    onSelectIssue: (String) -> Unit,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val issues = listOf(
        "Blur / Low Quality 🌫️",
        "Video Lag / Stutter ⏱️",
        "Audio Drowned Out 🔊",
        "Bad Timing / Fast Cut ✂️",
        "Dull Colours 🎨",
        "Text Overlap Safe Zone 🔤"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("13", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 13", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("AI Editing Review & Troubleshooter 🤖", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Editing complete ho gayi? Kya video me koi issue aa raha hai?", fontSize = 12.5.sp, color = TextWhite)

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        issues.forEach { issue ->
                            val isSelected = selectedIssue == issue
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectIssue(issue) },
                                label = { Text(issue, fontSize = 11.5.sp, color = TextWhite) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = VioletGlow,
                                    containerColor = GlassBg
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = GlassBorder,
                                    selectedBorderColor = VioletGlow,
                                    enabled = true,
                                    selected = isSelected
                                )
                            )
                        }
                    }

                    if (selectedIssue != null) {
                        val solutionText = generateAiEditingProblemFix(selectedIssue)
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF1D1130),
                            border = BorderStroke(1.dp, VioletGlow)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("💡 AI Customized Fix:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = solutionText,
                                    fontSize = 12.sp,
                                    color = TextWhite,
                                    lineHeight = 16.5.sp
                                )
                            }
                        }
                    } else {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF13281E),
                            border = BorderStroke(1.dp, SuccessGreen)
                        ) {
                            Text("✨ No issue? Great job! Press button below to finish editing review.", fontSize = 12.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Review Done ✅" else "Confirm Review & Proceed ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 14: PRACTICE CHALLENGE & EXPORT REEL (414)
// =========================================================
@Composable
private fun Level4Lesson14PracticeExportCard(
    isCompleted: Boolean,
    selectedApp: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.5.dp, if (isCompleted) SuccessGreen else WarningAmber)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else WarningAmber, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("14", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 14 • FINAL MISSION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Export First Edited Reel 🏆", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // BONUS 5-SEC TIPS
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF28133B),
                        border = BorderStroke(1.dp, VioletGlow)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("⚡ BONUS 5-SEC EDITING SECRETS:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                            Text("• Never upload directly from editor without previewing audio on phone speakers.", fontSize = 11.sp, color = TextWhite)
                            Text("• Turn on 'High Quality Uploads' in IG Settings -> Account -> Data Usage.", fontSize = 11.sp, color = TextWhite)
                        }
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = GlassBg,
                        border = BorderStroke(1.dp, GlassBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("🚀 MISSION:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                            Text("Hit 'Export' button in $selectedApp. Wait for video render to finish and save in phone gallery. Do NOT upload to Instagram yet — just complete your render!", fontSize = 12.sp, color = TextWhite, lineHeight = 16.5.sp)
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = if (isCompleted) SuccessGreen else WarningAmber),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isCompleted) "Mission Complete! Badge Unlocked 🏆" else "MISSION COMPLETE (Reel Exported) 🏆",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

// =========================================================
// LEVEL 4 REWARD CELEBRATION MODAL
// =========================================================
@Composable
private fun Level4RewardCelebrationDialog(
    selectedCategory: String,
    currentLang: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .shadow(24.dp, RoundedCornerShape(28.dp), ambientColor = VioletGlow, spotColor = VioletGlow),
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF130A24),
            border = BorderStroke(2.dp, Brush.horizontalGradient(listOf(VioletGlow, MagentaAccent)))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "🎉✨✂️", fontSize = 42.sp)

                Text(
                    text = "LEVEL 4 COMPLETE!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = WarningAmber
                )

                Text(
                    text = "Badge Unlocked: Video Editing Master ✂️",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = GlassBg,
                    border = BorderStroke(1.dp, GlassBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "⚡ REWARD UNLOCKED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)
                        Text(text = "+1500 XP", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = WarningAmber)
                        Text(
                            text = "Tumne apne $selectedCategory content ko high-level professional look de diya hai! Single edit, HD polish, and auto captions set.",
                            fontSize = 12.sp,
                            color = TextWhite,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.5.sp
                        )
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                ) {
                    Text("Continue Journey ➔", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }
        }
    }
}

// =========================================================
// HELPER FUNCTIONS FOR LEVEL 4 EDITING
// =========================================================

private fun generateEditingWorkspaceTour(app: String): List<InstagramQuadruple<String, String, String, String>> {
    return listOf(
        InstagramQuadruple("🎞️", "Timeline Tracks", "Main video layer at top, audio track below, text overlays at very bottom.", "Center of editor screen"),
        InstagramQuadruple("✂️", "Edit & Split Tool", "Use scissors icon to cut video clips into smaller segments.", "Bottom menu toolbar"),
        InstagramQuadruple("💬", "Text & Subtitles", "Add auto captions, animated stickers, and custom title headers.", "Text tab"),
        InstagramQuadruple("🎵", "Audio & Music", "Import background music tracks, sound effects (SFX), and record voiceovers.", "Audio tab"),
        InstagramQuadruple("📤", "Export Render", "Top right arrow button to save final HD video to gallery.", "Top right corner")
    )
}

private fun generateEditingExportGuide(app: String): List<InstagramQuadruple<String, String, String, String>> {
    return listOf(
        InstagramQuadruple("Resolution 📐", "1080P (1080 x 1920)", "", ""),
        InstagramQuadruple("Frame Rate 🎞️", "60 FPS (or 30 FPS for cinematic)", "", ""),
        InstagramQuadruple("Bitrate 📶", "Recommended / High (15-20 Mbps)", "", ""),
        InstagramQuadruple("Smart HDR ☀️", "Turn OFF HDR to prevent dark/overexposed Instagram upload bugs!", "", "")
    )
}

private fun generateAiEditingProblemFix(issue: String): String {
    return when {
        issue.contains("Blur") -> "🔍 Quality Fix: In export settings, select '1080P' with 'High Bitrate'. Make sure 'High Quality Uploads' is turned ON in Instagram App Settings!"
        issue.contains("Lag") -> "⏱️ Lag Fix: Lower timeline preview quality while editing. Exporting will render at full 60 FPS smoothly."
        issue.contains("Audio") -> "🔊 Audio Balance Fix: Lower background music volume to 10% and boost original voice recording track to 120%."
        issue.contains("Timing") -> "✂️ Pacing Fix: Trim video clip right when last word ends. Remove 0.5s pause between sentences."
        issue.contains("Dull") -> "🎨 Color Punch Fix: Boost Sharpen (+15), Contrast (+10), and Saturation (+5) in your editor's Adjust menu."
        else -> "🔤 Safe Zone Fix: Keep text away from top 15% and bottom 20% of screen to avoid getting covered by IG buttons!"
    }
}

// =========================================================
// PHASE 7 — LEVEL 5 UPLOAD & GROWTH SYSTEM JOURNEY CARD
// =========================================================

@Composable
private fun InstagramLevel5JourneyCard(
    completedCount: Int,
    totalCount: Int = 14,
    isAllCompleted: Boolean,
    currentLang: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(24.dp), ambientColor = VioletGlow, spotColor = VioletGlow),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF140D2B),
        border = BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(VioletGlow, MagentaAccent)))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = VioletDeep,
                    border = BorderStroke(1.dp, VioletGlow)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "🚀", fontSize = 14.sp)
                        Text(
                            text = "LEVEL 5 • UPLOAD & GROWTH SYSTEM",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isAllCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg,
                    border = BorderStroke(1.dp, if (isAllCompleted) SuccessGreen else GlassBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isAllCompleted) Icons.Default.EmojiEvents else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isAllCompleted) WarningAmber else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (isAllCompleted) "Badge: Growth Expert 📈" else "Badge Locked 🔒",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAllCompleted) WarningAmber else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Upload & Growth System 📈",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite
            )

            // AI MENTOR STATEMENT
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(14.dp),
                color = GlassBg,
                border = BorderStroke(1.dp, GlassBorder)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Text("🤖", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "🔥 Amazing! Tumhari reel ready hai. Lekin sirf upload kar dena enough nahi hota. Ab main tumhe sikhaunga ki upload kaise karte hain taaki maximum reach mile.",
                        fontSize = 12.5.sp,
                        color = TextWhite,
                        lineHeight = 17.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // METRICS ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(text = "📚 14 Steps", fontSize = 11.5.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    Text(text = "⏱️ 1–2 Hours", fontSize = 11.5.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    Text(text = "⚡ +2000 XP", fontSize = 11.5.sp, color = WarningAmber, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "$completedCount / $totalCount Completed",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isAllCompleted) SuccessGreen else VioletGlow
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { completedCount.toFloat() / totalCount.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = if (isAllCompleted) SuccessGreen else VioletGlow,
                trackColor = GlassBg
            )
        }
    }
}

// =========================================================
// LEVEL 5 INTERACTIVE GROWTH COACH SECTION
// =========================================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InstagramLevel5GrowthCoachSection(
    completedLessons: Set<Int>,
    selectedCategory: String,
    userGoal: String,
    currentLang: String,
    onLessonCompleted: (Int) -> Unit,
    onOpenSmartHelp: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // STEP 1: PRE UPLOAD CHECKLIST (501)
        Level5Lesson1PreUploadChecklistCard(
            isCompleted = completedLessons.contains(501),
            onMissionComplete = { onLessonCompleted(501) }
        )

        // STEP 2: INSTAGRAM UPLOAD GUIDE (502)
        Level5Lesson2UploadGuideCard(
            isCompleted = completedLessons.contains(502),
            onMissionComplete = { onLessonCompleted(502) }
        )

        // STEP 3: UPLOAD SETTINGS (503)
        Level5Lesson3UploadSettingsCard(
            isCompleted = completedLessons.contains(503),
            onMissionComplete = { onLessonCompleted(503) }
        )

        // STEP 4: CAPTION BUILDER (504)
        Level5Lesson4CaptionBuilderCard(
            isCompleted = completedLessons.contains(504),
            selectedCategory = selectedCategory,
            onMissionComplete = { onLessonCompleted(504) }
        )

        // STEP 5: HASHTAG STRATEGY (505)
        Level5Lesson5HashtagStrategyCard(
            isCompleted = completedLessons.contains(505),
            selectedCategory = selectedCategory,
            onMissionComplete = { onLessonCompleted(505) }
        )

        // STEP 6: BEST UPLOAD TIME (506)
        Level5Lesson6BestTimeCard(
            isCompleted = completedLessons.contains(506),
            selectedCategory = selectedCategory,
            onMissionComplete = { onLessonCompleted(506) }
        )

        // STEP 7: INSTAGRAM ALGORITHM DECODED (507)
        Level5Lesson7AlgorithmCard(
            isCompleted = completedLessons.contains(507),
            onMissionComplete = { onLessonCompleted(507) }
        )

        // STEP 8: FIRST 30 MINUTES STRATEGY (508)
        Level5Lesson8First30MinCard(
            isCompleted = completedLessons.contains(508),
            onMissionComplete = { onLessonCompleted(508) }
        )

        // STEP 9: GROWTH BOOSTER ARSENAL (509)
        Level5Lesson9GrowthBoosterCard(
            isCompleted = completedLessons.contains(509),
            onMissionComplete = { onLessonCompleted(509) }
        )

        // STEP 10: INSIGHTS & METRICS (510)
        Level5Lesson10InsightsGuideCard(
            isCompleted = completedLessons.contains(510),
            onMissionComplete = { onLessonCompleted(510) }
        )

        // STEP 11: AI PERFORMANCE REVIEW & ANALYZER (511)
        Level5Lesson11PerformanceReviewCard(
            isCompleted = completedLessons.contains(511),
            selectedCategory = selectedCategory,
            onMissionComplete = { onLessonCompleted(511) }
        )

        // STEP 12: IF REEL FAILS STRATEGY (512)
        Level5Lesson12ReelFailStrategyCard(
            isCompleted = completedLessons.contains(512),
            onMissionComplete = { onLessonCompleted(512) }
        )

        // STEP 13: VIRAL FORMULA & CONSISTENCY (513)
        Level5Lesson13ViralFormulaCard(
            isCompleted = completedLessons.contains(513),
            onMissionComplete = { onLessonCompleted(513) }
        )

        // STEP 14: BONUS DAILY CHECKLIST & MISSION (514)
        Level5Lesson14BonusMissionCard(
            isCompleted = completedLessons.contains(514),
            onMissionComplete = { onLessonCompleted(514) }
        )
    }
}

// =========================================================
// STEP 1: PRE UPLOAD CHECKLIST (501)
// =========================================================
@Composable
private fun Level5Lesson1PreUploadChecklistCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var coverReady by remember { mutableStateOf(false) }
    var captionReady by remember { mutableStateOf(false) }
    var audioReady by remember { mutableStateOf(false) }
    var qualityReady by remember { mutableStateOf(false) }
    var exportReady by remember { mutableStateOf(false) }
    var ctaReady by remember { mutableStateOf(false) }

    val allChecked = coverReady && captionReady && audioReady && qualityReady && exportReady && ctaReady

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("1", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 1", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Pre-Upload Checklist 📋", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Reel upload karne se pehle ye 6 cheezein confirm karo:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        ChecklistItemRow("🖼️ Cover Frame / Thumbnail Ready", coverReady) { coverReady = it }
                        ChecklistItemRow("💬 Caption & Hook Written", captionReady) { captionReady = it }
                        ChecklistItemRow("🎵 Trending Audio / Voiceover Clear", audioReady) { audioReady = it }
                        ChecklistItemRow("✨ HD Video Quality (No Blur / Pixelation)", qualityReady) { qualityReady = it }
                        ChecklistItemRow("📤 Rendered at 1080p 60fps", exportReady) { exportReady = it }
                        ChecklistItemRow("🎯 Clear CTA (Comment / Save / Follow)", ctaReady) { ctaReady = it }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted && allChecked
                    ) {
                        Text(
                            text = if (isCompleted) "Checklist Verified ✅" else if (allChecked) "Mission Complete (Checklist 6/6) ➔" else "Please check all 6 items above",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChecklistItemRow(
    title: String,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!isChecked) },
        shape = RoundedCornerShape(10.dp),
        color = if (isChecked) Color(0xFF193B28) else GlassBg,
        border = BorderStroke(1.dp, if (isChecked) SuccessGreen else GlassBorder)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontSize = 12.sp, color = TextWhite, modifier = Modifier.weight(1f))
            Checkbox(
                checked = isChecked,
                onCheckedChange = onToggle,
                colors = CheckboxDefaults.colors(
                    checkedColor = SuccessGreen,
                    uncheckedColor = TextMuted
                )
            )
        }
    }
}

// =========================================================
// STEP 2: INSTAGRAM UPLOAD GUIDE (502)
// =========================================================
@Composable
private fun Level5Lesson2UploadGuideCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val uploadSteps = listOf(
        InstagramQuadruple("1️⃣ Open IG", "Tap '+' icon at bottom/top -> Select 'Reel'.", Icons.Default.AddCircle, "Step 1"),
        InstagramQuadruple("2️⃣ Select Video", "Choose your edited 1080p video clip from gallery.", Icons.Default.VideoLibrary, "Step 2"),
        InstagramQuadruple("3️⃣ Crop & Cover", "Tap 'Edit Cover' -> Select high-emotion frame or custom thumbnail.", Icons.Default.Crop, "Step 3"),
        InstagramQuadruple("4️⃣ Tag & Location", "Tag relevant creators/brands and add city/event location.", Icons.Default.Place, "Step 4"),
        InstagramQuadruple("5️⃣ Collaborator", "Invite collaborator if doing co-created reel for 2X reach!", Icons.Default.People, "Step 5")
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("2", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 2", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Instagram Upload Walkthrough 📲", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Follow this exact step-by-step upload flow inside Instagram:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        uploadSteps.forEach { item ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(item.third, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(22.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(item.first, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text(item.second, fontSize = 11.5.sp, color = TextMuted)
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Walkthrough Learned ✅" else "Understand Upload Steps ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 3: UPLOAD SETTINGS (503)
// =========================================================
@Composable
private fun Level5Lesson3UploadSettingsCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("3", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 3", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Critical Upload Settings ⚙️", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Always configure these settings before tapping 'Share':", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SettingExplanationRow(
                            title = "⚡ Upload at Highest Quality",
                            toggleState = "MUST BE ON ✅",
                            explanation = "Settings -> Account -> Data Usage -> Toggle 'Upload at highest quality' ON. Prevents IG from compressing video to blurry 480p!"
                        )
                        SettingExplanationRow(
                            title = "📰 Share to Feed",
                            toggleState = "TURN ON ✅",
                            explanation = "Always turn ON! Your existing followers generate the initial 100 views needed to trigger the algorithm recommendation engine."
                        )
                        SettingExplanationRow(
                            title = "🤖 Recommend on Facebook",
                            toggleState = "TURN ON ✅",
                            explanation = "Cross-posts your reel to Facebook Reels automatically, giving free bonus reach to 2.9 billion active users!"
                        )
                        SettingExplanationRow(
                            title = "🔒 Hide Like Count",
                            toggleState = "KEEP OFF ❌",
                            explanation = "Keep Likes visible! Social proof (likes/comments) builds instant credibility with new viewers."
                        )
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Settings Configured ✅" else "Settings Understood ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingExplanationRow(
    title: String,
    toggleState: String,
    explanation: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = GlassBg,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (toggleState.contains("ON")) SuccessGreen.copy(alpha = 0.2f) else WarningAmber.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = toggleState,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (toggleState.contains("ON")) SuccessGreen else WarningAmber,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Text(explanation, fontSize = 11.5.sp, color = TextMuted, lineHeight = 16.sp)
        }
    }
}

// =========================================================
// STEP 4: CAPTION BUILDER (504)
// =========================================================
@Composable
private fun Level5Lesson4CaptionBuilderCard(
    isCompleted: Boolean,
    selectedCategory: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var topicInput by remember { mutableStateOf("") }
    var generatedCaptions by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedCaptionIndex by remember { mutableStateOf(0) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("4", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 4", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("AI Caption Generator (10 Options) 💬", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter your reel topic to generate 10 unique, high-conversion captions:", fontSize = 12.5.sp, color = TextWhite)

                    OutlinedTextField(
                        value = topicInput,
                        onValueChange = { topicInput = it },
                        placeholder = { Text("e.g. 3 AI tools for creators / Gym workout routine", color = TextMuted, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VioletGlow,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    Button(
                        onClick = {
                            val topic = if (topicInput.isBlank()) "Instagram Growth Hacks" else topicInput
                            generatedCaptions = generateCaptionsForTopic(topic, selectedCategory)
                            selectedCaptionIndex = 0
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VioletGlow),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Generate 10 Captions 🪄", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    if (generatedCaptions.isNotEmpty()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = GlassBg,
                            border = BorderStroke(1.dp, VioletGlow)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("Option #${selectedCaptionIndex + 1} of 10", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                                    Row {
                                        IconButton(
                                            onClick = { if (selectedCaptionIndex > 0) selectedCaptionIndex-- },
                                            enabled = selectedCaptionIndex > 0
                                        ) {
                                            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = TextWhite)
                                        }
                                        IconButton(
                                            onClick = { if (selectedCaptionIndex < generatedCaptions.size - 1) selectedCaptionIndex++ },
                                            enabled = selectedCaptionIndex < generatedCaptions.size - 1
                                        ) {
                                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = TextWhite)
                                        }
                                    }
                                }

                                SelectionContainer {
                                    Text(
                                        text = generatedCaptions[selectedCaptionIndex],
                                        fontSize = 12.5.sp,
                                        color = TextWhite,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted && generatedCaptions.isNotEmpty()
                    ) {
                        Text(if (isCompleted) "Caption Ready ✅" else "Select Caption & Continue ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 5: HASHTAG STRATEGY (505)
// =========================================================
@Composable
private fun Level5Lesson5HashtagStrategyCard(
    isCompleted: Boolean,
    selectedCategory: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val hashtagPillars = remember(selectedCategory) {
        generateHashtagsForTopic(selectedCategory)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("5", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 5", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Hashtag Strategy (3x3 Formula) #️⃣", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Personalized 5-Tier Hashtag Set for '$selectedCategory':", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        hashtagPillars.forEach { pillar ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(pillar.first, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                                        Text(pillar.fourth, fontSize = 10.5.sp, color = TextMuted)
                                    }
                                    SelectionContainer {
                                        Text(pillar.second, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    }
                                    Text(pillar.third, fontSize = 11.sp, color = TextMuted)
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Hashtags Saved ✅" else "Save Hashtags & Continue ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 6: BEST UPLOAD TIME (506)
// =========================================================
@Composable
private fun Level5Lesson6BestTimeCard(
    isCompleted: Boolean,
    selectedCategory: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var selectedCountry by remember { mutableStateOf("India 🇮🇳") }

    val countries = listOf("India 🇮🇳", "USA 🇺🇸", "UK 🇬🇧", "Global 🌍")
    val timeSlots = remember(selectedCountry, selectedCategory) {
        generateBestPostingTimes(selectedCountry, selectedCategory)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("6", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 6", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Best Upload Time Recommender ⏰", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Target Audience Region:", fontSize = 12.5.sp, color = TextWhite)

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(countries) { c ->
                            val isSelected = selectedCountry == c
                            Surface(
                                modifier = Modifier.clickable { selectedCountry = c },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) VioletDeep else GlassBg,
                                border = BorderStroke(1.dp, if (isSelected) VioletGlow else GlassBorder)
                            ) {
                                Text(
                                    text = c,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) WarningAmber else TextWhite,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Text("Recommended Posting Window ($selectedCountry):", fontSize = 12.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        timeSlots.forEach { slot ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(slot.first, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text(slot.second, fontSize = 11.sp, color = TextMuted)
                                    }
                                    Surface(shape = RoundedCornerShape(6.dp), color = VioletDeep) {
                                        Text(slot.fourth, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Time Window Selected ✅" else "Lock Posting Schedule ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 7: INSTAGRAM ALGORITHM DECODED (507)
// =========================================================
@Composable
private fun Level5Lesson7AlgorithmCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val algoMetrics = listOf(
        InstagramQuadruple("👁️ Watch Time & Retention", "Weight: 100%", "Number 1 metric! If users watch >80% of your video, IG pushes reel to Explore page.", "CRITICAL"),
        InstagramQuadruple("🔄 Shares (DMs)", "Weight: 85%", "When users send reel to friends via DM, IG ranks it as viral quality content.", "HIGH"),
        InstagramQuadruple("🔖 Saves", "Weight: 70%", "Saves signal educational value. Tutorials and cheat sheets get maximum saves.", "HIGH"),
        InstagramQuadruple("💬 Comments & Replies", "Weight: 50%", "Comment discussions trigger notification loops, pulling users back.", "MEDIUM"),
        InstagramQuadruple("❤️ Likes & Profile Visits", "Weight: 30%", "Secondary indicators. High profile visits convert non-followers to followers.", "NORMAL")
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("7", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 7", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Instagram Algorithm Decoded 🧠", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("How Instagram decides to push your reel to non-followers:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        algoMetrics.forEach { item ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(item.first, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text(item.second, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                                    }
                                    Text(item.third, fontSize = 11.5.sp, color = TextMuted, lineHeight = 16.sp)
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Algorithm Mastered ✅" else "Got Algorithm Rules ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 8: FIRST 30 MINUTES STRATEGY (508)
// =========================================================
@Composable
private fun Level5Lesson8First30MinCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("8", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 8", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Golden First 30 Minutes Protocol ⏱️", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("The first 30 minutes after publishing decide 80% of reel velocity:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("💬 REPLY TO ALL COMMENTS: Ask a follow-up question in replies to start a conversation thread.", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("📲 SHARE TO STORY: Add a sticker hook like 'New Reel! Tap to watch secret tip #2 🤫'.", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("📌 PIN TOP COMMENT: Pin a comment with your CTA ('Comment SECRET to get PDF guide!').", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = Color(0xFF3B1E22)) {
                            Text("⚠️ DO NOT EDIT OR DELETE: Never edit caption or delete reel within 2 hours. It resets algorithm indexing!", fontSize = 11.5.sp, color = Color(0xFFFF6B6B), modifier = Modifier.padding(10.dp))
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Protocol Memorized ✅" else "Understand First 30 Min Protocol ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 9: GROWTH BOOSTER ARSENAL (509)
// =========================================================
@Composable
private fun Level5Lesson9GrowthBoosterCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val boosters = listOf(
        InstagramQuadruple("🤝 Collaborations", "Invite a creator with similar follower count to double reach.", Icons.Default.Group, "2X Reach"),
        InstagramQuadruple("📢 Broadcast Channel", "Notify your core 10% super-fans immediately upon posting.", Icons.Default.Campaign, "Instant Views"),
        InstagramQuadruple("⭐ Profile Highlights", "Add viral reels to a 'Start Here' highlight on your bio.", Icons.Default.Star, "Evergreen"),
        InstagramQuadruple("🎵 Custom Audio Name", "Name your original audio after your handle for brand search.", Icons.Default.MusicNote, "Branding")
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("9", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 9", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Growth Booster Arsenal 🚀", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Use these 4 feature levers to accelerate account authority:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        boosters.forEach { item ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(item.third, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(22.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(item.first, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text(item.second, fontSize = 11.5.sp, color = TextMuted)
                                    }
                                    Surface(shape = RoundedCornerShape(6.dp), color = VioletDeep) {
                                        Text(item.fourth, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Boosters Unlocked ✅" else "Master Growth Boosters ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 10: INSIGHTS & METRICS (510)
// =========================================================
@Composable
private fun Level5Lesson10InsightsGuideCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("10", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 10", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Insights & Analytics Cheat Sheet 📊", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Benchmark targets for viral account health:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        MetricTargetRow("📊 Non-Follower Reach", "Target: >70%", "If >70% of viewers are non-followers, your hook is working!")
                        MetricTargetRow("⏱️ Average Watch Time", "Target: >80% length", "Example: For 15s reel, average watch time should be 12s+.")
                        MetricTargetRow("🔄 Share-to-Like Ratio", "Target: 1 Share per 5 Likes", "High share ratio indicates viral value content.")
                        MetricTargetRow("👥 Follower Conversion Rate", "Target: 1 Follow per 100 Views", "Indicates strong bio and profile optimization.")
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Metrics Target Saved ✅" else "Understand Metrics Benchmarks ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricTargetRow(title: String, target: String, note: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = GlassBg,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(target, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
            }
            Text(note, fontSize = 11.sp, color = TextMuted)
        }
    }
}

// =========================================================
// STEP 11: AI PERFORMANCE REVIEW & ANALYZER (511)
// =========================================================
@Composable
private fun Level5Lesson11PerformanceReviewCard(
    isCompleted: Boolean,
    selectedCategory: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var viewsText by remember { mutableStateOf("") }
    var likesText by remember { mutableStateOf("") }
    var sharesText by remember { mutableStateOf("") }
    var savesText by remember { mutableStateOf("") }
    var followersText by remember { mutableStateOf("") }
    var aiAnalysisResult by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("11", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 11", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("AI Reel Performance Analyzer 🧪", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter your reel stats (or sample numbers) for instant AI diagnostics:", fontSize = 12.5.sp, color = TextWhite)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = viewsText,
                            onValueChange = { viewsText = it },
                            placeholder = { Text("Views (e.g. 1500)", color = TextMuted, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VioletGlow, unfocusedBorderColor = GlassBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                        )
                        OutlinedTextField(
                            value = likesText,
                            onValueChange = { likesText = it },
                            placeholder = { Text("Likes (e.g. 120)", color = TextMuted, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VioletGlow, unfocusedBorderColor = GlassBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = sharesText,
                            onValueChange = { sharesText = it },
                            placeholder = { Text("Shares (e.g. 45)", color = TextMuted, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VioletGlow, unfocusedBorderColor = GlassBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                        )
                        OutlinedTextField(
                            value = savesText,
                            onValueChange = { savesText = it },
                            placeholder = { Text("Saves (e.g. 30)", color = TextMuted, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VioletGlow, unfocusedBorderColor = GlassBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                        )
                    }

                    OutlinedTextField(
                        value = followersText,
                        onValueChange = { followersText = it },
                        placeholder = { Text("Followers Gained (e.g. 12)", color = TextMuted, fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VioletGlow, unfocusedBorderColor = GlassBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )

                    Button(
                        onClick = {
                            val v = viewsText.toIntOrNull() ?: 1000
                            val l = likesText.toIntOrNull() ?: 80
                            val sh = sharesText.toIntOrNull() ?: 25
                            val sa = savesText.toIntOrNull() ?: 15
                            val fol = followersText.toIntOrNull() ?: 8
                            aiAnalysisResult = analyzeReelPerformance(v, l, sh, sa, fol)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VioletGlow),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Analyze Reel Performance 🤖", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    aiAnalysisResult?.let { resultText ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = GlassBg,
                            border = BorderStroke(1.dp, SuccessGreen)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("🤖 AI Diagnostic Report:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                Text(resultText, fontSize = 12.sp, color = TextWhite, lineHeight = 17.sp)
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Analysis Recorded ✅" else "Save Performance Review ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 12: IF REEL FAILS STRATEGY (512)
// =========================================================
@Composable
private fun Level5Lesson12ReelFailStrategyCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("12", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 12", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("If Reel Fails Strategy & Mindset 🛡️", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("What to do if a reel gets stuck at 200–500 views:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = Color(0xFF3B1E22)) {
                            Text("❌ DON'T PANIC OR DELETE: Deleting reels confuses the algorithm. Leave it up!", fontSize = 11.5.sp, color = Color(0xFFFF6B6B), modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("🔍 DIAGNOSE THE HOOK: If drop-off is in first 2 seconds, your hook line was too slow or boring.", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("🔄 RE-HOOK & RE-POST: Take the exact same video, record a faster 2-second hook, and repost 3 days later!", fontSize = 11.5.sp, color = WarningAmber, modifier = Modifier.padding(10.dp))
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Mindset Adopted ✅" else "Adopt Growth Mindset ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 13: VIRAL FORMULA & CONSISTENCY (513)
// =========================================================
@Composable
private fun Level5Lesson13ViralFormulaCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("13", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 13", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Viral Consistency Formula 🔥", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("The 5 rules to reach 10,000+ followers consistently:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("1️⃣ SERIES CONTENT: Create 5-part series (e.g. 'Day 1 of 30 building my startup'). People follow for Part 2!", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("2️⃣ BATCH RECORDING: Record 4 reels in 1 hour on weekends to stay consistent without burnout.", fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = GlassBg) {
                            Text("3️⃣ LOOPABLE ENDINGS: Make the last sentence flow directly back into the first sentence for infinite watch loops!", fontSize = 11.5.sp, color = WarningAmber, modifier = Modifier.padding(10.dp))
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Formula Mastered ✅" else "Master Consistency Formula ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 14: BONUS MISSION & DAILY CHECKLIST (514)
// =========================================================
@Composable
private fun Level5Lesson14BonusMissionCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else VioletGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("14", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("FINAL MISSION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text("Upload Reel & Finish Master Guide 🏆", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF20133A),
                        border = BorderStroke(1.dp, VioletGlow)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("🎯 YOUR FINAL MISSION:", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = WarningAmber)
                            Text("Upload your edited reel to Instagram using the settings & hashtag strategy you just built. Check your views after 24 hours!", fontSize = 12.5.sp, color = TextWhite, lineHeight = 17.5.sp)
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        enabled = !isCompleted
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                            Text(if (isCompleted) "Reel Uploaded & Master Level Complete! 🏆" else "Mission Complete (Reel Published) 🚀", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// LEVEL 5 REWARD CELEBRATION MODAL
// =========================================================
@Composable
private fun Level5RewardCelebrationDialog(
    selectedCategory: String,
    currentLang: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF140D2B),
            border = BorderStroke(2.dp, Brush.horizontalGradient(listOf(VioletGlow, MagentaAccent, WarningAmber)))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(WarningAmber, VioletDeep)))
                        .border(2.dp, WarningAmber, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📈", fontSize = 42.sp)
                }

                Text(
                    text = "LEVEL 5 COMPLETE! 🚀",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite,
                    textAlign = TextAlign.Center
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = VioletDeep,
                    border = BorderStroke(1.dp, VioletGlow)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "🏆 BADGE UNLOCKED:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                        Text(text = "Instagram Growth Expert", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite)
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = GlassBg,
                    border = BorderStroke(1.dp, GlassBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "⚡ REWARD UNLOCKED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MagentaAccent)
                        Text(text = "+2000 XP", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = WarningAmber)
                        Text(
                            text = "Tumne upload settings, caption AI generator, hashtag strategy, and algorithm distribution system completely master kar liya hai!",
                            fontSize = 12.sp,
                            color = TextWhite,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.5.sp
                        )
                    }
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Claim Badge & Master Certificate 🎓", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }
        }
    }
}

// =========================================================
// HELPER FUNCTIONS FOR LEVEL 5
// =========================================================

private fun generateCaptionsForTopic(topic: String, category: String): List<String> {
    return listOf(
        "🔥 Stop scrolling! Here is the exact strategy for $topic that nobody tells you.\n\nSave this reel for later 📌\nDrop a 'READY' in the comments to get the full step-by-step breakdown directly in your DMs! 💬\n\n#$category #InstagramGrowth #ReelTips",
        "💡 3 Mistakes you're making with $topic (and how to fix them today):\n\n1️⃣ Not framing your value in the first 2 seconds.\n2️⃣ Ignoring the 3x3 hashtag strategy.\n3️⃣ Forgetting a clear CTA.\n\nWhich mistake are you fixing first? 👇",
        "🚀 The secret to mastering $topic in 2026...\n\nSave this post before it gets lost in your feed! 🏷️\nFollow for daily high-value $category tips! ✨",
        "🤫 Most people fail at $topic because they skip this ONE step.\n\nRead caption for full guide 📖\nComment 'INFO' below and I'll send you my secret checklist! 🤖",
        "⚡ 10X your results with $topic in under 30 seconds!\n\nTag a friend who needs to see this right now! 👥\nDouble tap if this was helpful ❤️",
        "📌 Save this reel! Your step-by-step roadmap to $topic:\n\nStep 1: Focus on hook retention.\nStep 2: Optimize cover and caption.\nStep 3: Post at peak audience times.\n\nShare with a fellow creator! 📲",
        "👀 If you want better $category results, try this hack for $topic.\n\nIt takes 2 minutes and changes everything.\nFollow for more creator hacks! 🌟",
        "🎯 How I simplified $topic in 3 easy steps.\n\nDrop a '🔥' if you want Part 2 tomorrow!\n#$category #CreatorTips #GrowthHack",
        "💥 The brutal truth about $topic that nobody is talking about...\n\nWhat are your thoughts on this? Let's discuss in comments! 👇",
        "🏆 Master $topic like a pro with this cheat sheet.\n\nSave for reference 📌\nCheck link in bio for full master guide! 🔗"
    )
}

private fun generateHashtagsForTopic(category: String): List<InstagramQuadruple<String, String, String, String>> {
    val cleanCat = category.replace(" ", "")
    return listOf(
        InstagramQuadruple("🌐 Broad Reach (1M+)", "#ReelsIndia #ExplorePage #ViralReels", "High search volume, fast initial traction", "Tier 1"),
        InstagramQuadruple("🎯 Category Niche (100k-500k)", "#${cleanCat}Tips #${cleanCat}Guide #${cleanCat}Creator", "Reaches core targeted audience", "Tier 2"),
        InstagramQuadruple("⚡ Community Specific (10k-100k)", "#${cleanCat}Community #${cleanCat}Hacks #${cleanCat}Growth", "Highest engagement & conversion rate", "Tier 3"),
        InstagramQuadruple("📍 Location & Language", "#IndiaCreators #DelhiCreators #MumbaiCreators", "Boosts local search algorithm indexing", "Tier 4"),
        InstagramQuadruple("🏷️ Brand & Series", "#${cleanCat}Series #Daily${cleanCat}", "Builds personal brand authority", "Tier 5")
    )
}

private fun generateBestPostingTimes(country: String, category: String): List<InstagramQuadruple<String, String, String, String>> {
    return when {
        country.contains("India") -> listOf(
            InstagramQuadruple("🌅 Morning Burst (8:30 AM - 9:30 AM)", "Peak commute time engagement", "", "High Traffic"),
            InstagramQuadruple("🍱 Lunch Peak (1:00 PM - 2:00 PM)", "High reel scrolling during lunch break", "", "Medium Traffic"),
            InstagramQuadruple("🌆 Evening Prime (6:30 PM - 8:30 PM)", "BEST WINDOW for max reach & DM shares", "", "MAX VIRAL"),
            InstagramQuadruple("🌙 Night Scroll (10:00 PM - 11:15 PM)", "High watch retention for long reels", "", "High Retention")
        )
        else -> listOf(
            InstagramQuadruple("🌅 Morning (9:00 AM EST)", "Good for international reach", "", "High Traffic"),
            InstagramQuadruple("🌆 Evening (7:00 PM EST)", "BEST WINDOW for prime time scrolling", "", "MAX VIRAL"),
            InstagramQuadruple("🌙 Night (10:30 PM EST)", "Great for educational/tutorial content", "", "High Retention")
        )
    }
}

private fun analyzeReelPerformance(views: Int, likes: Int, shares: Int, saves: Int, followers: Int): String {
    val likeRatio = if (views > 0) (likes.toFloat() / views.toFloat() * 100) else 0f
    val shareRatio = if (views > 0) (shares.toFloat() / views.toFloat() * 100) else 0f

    return when {
        views >= 5000 -> "🚀 EXCELLENT REEL! Your reel hit viral distribution. High share ratio (${String.format("%.1f", shareRatio)}%) pushed it to non-followers. Keep this exact hook format for your next 3 reels!"
        views >= 1000 -> "📈 STRONG PERFORMANCE! Good initial reach. To break past 5k views, try adding a pinned comment with a question to double comment replies."
        else -> "💡 GOOD START! Views are held back by hook drop-off. For your next reel: trim the first 1.5 seconds closer to the action and add high-contrast text overlay on the screen!"
    }
}

// =========================================================
// PHASE 8 — LEVEL 6 MONETIZATION & BRAND DEALS JOURNEY CARD
// =========================================================

@Composable
private fun InstagramLevel6JourneyCard(
    completedCount: Int,
    totalCount: Int = 15,
    isAllCompleted: Boolean,
    currentLang: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(24.dp), ambientColor = GoldAccent, spotColor = GoldAccent),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF1E1602),
        border = BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(GoldAccent, WarningAmber)))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF332300),
                    border = BorderStroke(1.dp, GoldAccent)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "💼", fontSize = 14.sp)
                        Text(
                            text = "LEVEL 6 • INSTAGRAM MONETIZATION",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isAllCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg,
                    border = BorderStroke(1.dp, if (isAllCompleted) SuccessGreen else GlassBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isAllCompleted) Icons.Default.EmojiEvents else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isAllCompleted) GoldAccent else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (isAllCompleted) "Badge: Creator Business Ready 💼" else "Badge Locked 🔒",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAllCompleted) GoldAccent else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Monetization & Brand Deals 💰",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite
            )

            // AI MENTOR STATEMENT
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(14.dp),
                color = GlassBg,
                border = BorderStroke(1.dp, GlassBorder)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Text("🤖", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "🔥 Congratulations! Ab tum sirf content creator nahi ho... Ab tum earning journey start karne wale ho. Level 6 mein main tumhe Brand Deals, Rates, Cold DMs aur Invoicing sikhaunga!",
                        fontSize = 12.5.sp,
                        color = TextWhite,
                        lineHeight = 17.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // METRICS ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(text = "📚 15 Steps", fontSize = 11.5.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    Text(text = "⏱️ 2–3 Hours", fontSize = 11.5.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    Text(text = "⚡ +2500 XP", fontSize = 11.5.sp, color = GoldAccent, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "$completedCount / $totalCount Completed",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isAllCompleted) SuccessGreen else GoldAccent
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { completedCount.toFloat() / totalCount.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = if (isAllCompleted) SuccessGreen else GoldAccent,
                trackColor = GlassBg
            )
        }
    }
}

// =========================================================
// LEVEL 6 INTERACTIVE MONETIZATION COACH SECTION
// =========================================================
@Composable
private fun InstagramLevel6MonetizationCoachSection(
    completedLessons: Set<Int>,
    selectedCategory: String,
    userGoal: String,
    userFollowers: String,
    currentLang: String,
    onLessonCompleted: (Int) -> Unit,
    onOpenSmartHelp: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // STEP 1: CHOOSE INCOME GOAL (601)
        Level6Lesson1ChooseIncomeGoalCard(
            isCompleted = completedLessons.contains(601),
            onMissionComplete = { onLessonCompleted(601) }
        )

        // STEP 2: BRAND READY PROFILE AUDIT (602)
        Level6Lesson2BrandReadyChecklistCard(
            isCompleted = completedLessons.contains(602),
            onMissionComplete = { onLessonCompleted(602) }
        )

        // STEP 3: MEDIA KIT GENERATOR (603)
        Level6Lesson3MediaKitCard(
            isCompleted = completedLessons.contains(603),
            selectedCategory = selectedCategory,
            userFollowers = userFollowers,
            onMissionComplete = { onLessonCompleted(603) }
        )

        // STEP 4: RATE CARD & PRICING CALCULATOR (604)
        Level6Lesson4RateCardCard(
            isCompleted = completedLessons.contains(604),
            selectedCategory = selectedCategory,
            userFollowers = userFollowers,
            onMissionComplete = { onLessonCompleted(604) }
        )

        // STEP 5: FINDING BRANDS ROADMAP (605)
        Level6Lesson5FindingBrandsCard(
            isCompleted = completedLessons.contains(605),
            onMissionComplete = { onLessonCompleted(605) }
        )

        // STEP 6: COLD DM MASTERY (10 TEMPLATES) (606)
        Level6Lesson6ColdDmMasteryCard(
            isCompleted = completedLessons.contains(606),
            selectedCategory = selectedCategory,
            onMissionComplete = { onLessonCompleted(606) }
        )

        // STEP 7: PROFESSIONAL EMAIL GENERATOR (607)
        Level6Lesson7ProfessionalEmailCard(
            isCompleted = completedLessons.contains(607),
            selectedCategory = selectedCategory,
            onMissionComplete = { onLessonCompleted(607) }
        )

        // STEP 8: NEGOTIATION MASTERCLASS (608)
        Level6Lesson8NegotiationMasteryCard(
            isCompleted = completedLessons.contains(608),
            onMissionComplete = { onLessonCompleted(608) }
        )

        // STEP 9: FAKE BRAND DETECTION & SCAM SHIELD (609)
        Level6Lesson9FakeBrandScamShieldCard(
            isCompleted = completedLessons.contains(609),
            onMissionComplete = { onLessonCompleted(609) }
        )

        // STEP 10: COLLABORATION AGREEMENT & CONTRACTS (610)
        Level6Lesson10AgreementContractCard(
            isCompleted = completedLessons.contains(610),
            onMissionComplete = { onLessonCompleted(610) }
        )

        // STEP 11: INVOICE GENERATOR & TEMPLATE (611)
        Level6Lesson11InvoiceGeneratorCard(
            isCompleted = completedLessons.contains(611),
            onMissionComplete = { onLessonCompleted(611) }
        )

        // STEP 12: PAYMENT METHODS & TAX BASICS (612)
        Level6Lesson12PaymentMethodsCard(
            isCompleted = completedLessons.contains(612),
            onMissionComplete = { onLessonCompleted(612) }
        )

        // STEP 13: AFTER COLLABORATION & RETAINERS (613)
        Level6Lesson13AfterCollabCard(
            isCompleted = completedLessons.contains(613),
            onMissionComplete = { onLessonCompleted(613) }
        )

        // STEP 14: AFFILIATE MARKETING MASTERY (614)
        Level6Lesson14AffiliateMarketingCard(
            isCompleted = completedLessons.contains(614),
            onMissionComplete = { onLessonCompleted(614) }
        )

        // STEP 15: PERSONAL BRANDING, AI INCOME PLANNER & SPECIAL OFFER ANALYZER (615)
        Level6Lesson15PersonalBrandIncomePlannerCard(
            isCompleted = completedLessons.contains(615),
            selectedCategory = selectedCategory,
            userFollowers = userFollowers,
            onMissionComplete = { onLessonCompleted(615) }
        )
    }
}

// =========================================================
// STEP 1: CHOOSE INCOME GOAL (601)
// =========================================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Level6Lesson1ChooseIncomeGoalCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var selectedGoal by remember { mutableStateOf("💼 Brand Collaborations") }

    val goals = listOf(
        "💼 Brand Collaborations",
        "🔗 Affiliate Marketing",
        "📹 UGC Creator",
        "📦 Sell Physical Products",
        "📑 Digital Products",
        "🚀 Multiple Income Sources"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("1", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 1", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Choose Your Primary Income Goal 🎯", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Instagram se earning start karne ke liye apni main stream choose karo:", fontSize = 12.5.sp, color = TextWhite)

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        goals.forEach { goal ->
                            val isSelected = selectedGoal == goal
                            Surface(
                                modifier = Modifier.clickable { selectedGoal = goal },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Color(0xFF3B2B02) else GlassBg,
                                border = BorderStroke(1.dp, if (isSelected) GoldAccent else GlassBorder)
                            ) {
                                Text(
                                    text = goal,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) GoldAccent else TextWhite,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = GlassBg,
                        border = BorderStroke(1.dp, GlassBorder)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Selected Stream Strategy:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                            Text(
                                text = when {
                                    selectedGoal.contains("Brand") -> "Focus on high engagement, clean aesthetics & direct outreach to brand PR teams."
                                    selectedGoal.contains("Affiliate") -> "Create problem-solving reels featuring products & link-in-bio recommendation hubs."
                                    selectedGoal.contains("UGC") -> "Create high-converting video ads for brands to run on their own ad accounts."
                                    selectedGoal.contains("Physical") -> "Use storytelling reels & unboxing content to drive shop traffic."
                                    selectedGoal.contains("Digital") -> "Offer e-books, Notion templates or masterclasses answering audience FAQs."
                                    else -> "Combine Brand Deals + Affiliate Links + Digital Products for 3X revenue stability!"
                                },
                                fontSize = 11.5.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Income Goal Locked ✅" else "Save Income Strategy ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 2: BRAND READY CHECKLIST (602)
// =========================================================
@Composable
private fun Level6Lesson2BrandReadyChecklistCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var c1 by remember { mutableStateOf(false) }
    var c2 by remember { mutableStateOf(false) }
    var c3 by remember { mutableStateOf(false) }
    var c4 by remember { mutableStateOf(false) }
    var c5 by remember { mutableStateOf(false) }
    var c6 by remember { mutableStateOf(false) }

    val checkedCount = listOf(c1, c2, c3, c4, c5, c6).count { it }
    val readinessScore = (checkedCount.toFloat() / 6f * 100).toInt()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("2", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 2", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Brand-Ready Profile Audit 📋", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Audit items to calculate Brand Readiness Score:", fontSize = 12.sp, color = TextWhite)
                        Text("$readinessScore% Ready", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (readinessScore >= 80) SuccessGreen else GoldAccent)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        ChecklistItemRow("💼 Professional Creator Account (Insights Enabled)", c1) { c1 = it }
                        ChecklistItemRow("✉️ Clear Bio + Public Contact Email Button", c2) { c2 = it }
                        ChecklistItemRow("📸 Clean HD Profile Picture / Logo", c3) { c3 = it }
                        ChecklistItemRow("⭐ Highlights for Reviews / About / Portfolio", c4) { c4 = it }
                        ChecklistItemRow("🎬 At least 10 Active Quality Reels in Grid", c5) { c5 = it }
                        ChecklistItemRow("📅 Consistent Posting Schedule (3+ per week)", c6) { c6 = it }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted && checkedCount >= 4
                    ) {
                        Text(if (isCompleted) "Audit Complete (100%) ✅" else if (checkedCount >= 4) "Pass Audit & Continue ➔" else "Check at least 4 items to pass", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 3: MEDIA KIT GENERATOR (603)
// =========================================================
@Composable
private fun Level6Lesson3MediaKitCard(
    isCompleted: Boolean,
    selectedCategory: String,
    userFollowers: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var handleInput by remember { mutableStateOf("@creator_handle") }

    val mediaKitText = remember(handleInput, selectedCategory, userFollowers) {
        """
        📑 MEDIA KIT — $handleInput
        ------------------------------------------
        🎯 Niche: $selectedCategory
        👥 Followers: ${if (userFollowers.isBlank()) "10,000+" else userFollowers}
        📈 Avg Reel Views: 15,000 - 50,000
        ⚡ Engagement Rate: 6.8% (Above Industry Avg)
        📍 Audience Demographics: 72% India, 65% Gen-Z & Millennials
        
        💼 SERVICES OFFERED:
        • Sponsored Instagram Reels (1080p 60fps)
        • Interactive Story Series + Swipe Up/Sticker Link
        • Dedicated Product Review / Unboxing
        • Brand Ambassador Retainer Packages
        
        ✉️ Contact for PR & Rate Card: pr.$selectedCategory.collabs@gmail.com
        ------------------------------------------
        """.trimIndent()
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("3", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 3", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("AI Media Kit Generator 📄", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Media Kit tumhara creator resume hota hai. Enter handle to build instant kit:", fontSize = 12.5.sp, color = TextWhite)

                    OutlinedTextField(
                        value = handleInput,
                        onValueChange = { handleInput = it },
                        placeholder = { Text("@your_username", color = TextMuted, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldAccent,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = GlassBg,
                        border = BorderStroke(1.dp, GoldAccent)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Generated Media Kit Structure:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                            SelectionContainer {
                                Text(mediaKitText, fontSize = 11.5.sp, color = TextWhite, lineHeight = 16.sp)
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Media Kit Generated ✅" else "Save Media Kit ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 4: RATE CARD & PRICING CALCULATOR (604)
// =========================================================
@Composable
private fun Level6Lesson4RateCardCard(
    isCompleted: Boolean,
    selectedCategory: String,
    userFollowers: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var followerTier by remember { mutableStateOf("10k - 25k Followers") }

    val tiers = listOf("1k - 10k Followers", "10k - 25k Followers", "25k - 50k Followers", "100k+ Followers")

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("4", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 4", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Rate Card & Pricing Calculator 💵", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Select your account follower range to calculate recommended pricing:", fontSize = 12.5.sp, color = TextWhite)

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(tiers) { t ->
                            val isSelected = followerTier == t
                            Surface(
                                modifier = Modifier.clickable { followerTier = t },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Color(0xFF332300) else GlassBg,
                                border = BorderStroke(1.dp, if (isSelected) GoldAccent else GlassBorder)
                            ) {
                                Text(
                                    text = t,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) GoldAccent else TextWhite,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = GlassBg,
                        border = BorderStroke(1.dp, GoldAccent)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Recommended Price Card ($followerTier):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldAccent)

                            val (reelRate, storyRate, comboRate) = when {
                                followerTier.contains("1k - 10k") -> Triple("₹1,500 - ₹3,000 ($25-$45)", "₹500 - ₹1,000", "₹2,000 Combo")
                                followerTier.contains("10k - 25k") -> Triple("₹3,500 - ₹7,000 ($50-$90)", "₹1,200 - ₹2,000", "₹5,000 Combo")
                                followerTier.contains("25k - 50k") -> Triple("₹8,000 - ₹15,000 ($100-$180)", "₹2,500 - ₹4,000", "₹11,000 Combo")
                                else -> Triple("₹20,000 - ₹50,000+ ($250-$600+)", "₹5,000 - ₹10,000", "₹30,000+ Combo")
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("🎬 1x Sponsored Reel:", fontSize = 11.5.sp, color = TextWhite)
                                Text(reelRate, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("📲 1x Story + Link Sticker:", fontSize = 11.5.sp, color = TextWhite)
                                Text(storyRate, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("🔥 Bundle (1 Reel + 2 Stories):", fontSize = 11.5.sp, color = TextWhite)
                                Text(comboRate, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Rate Card Set ✅" else "Confirm Pricing Card ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 5: FINDING BRANDS ROADMAP (605)
// =========================================================
@Composable
private fun Level6Lesson5FindingBrandsCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val channels = listOf(
        InstagramQuadruple("1️⃣ Instagram Search & Tags", "Search tags like #brandcollab #prpackage in your niche to find active brands.", Icons.Default.Search, "Step 1"),
        InstagramQuadruple("2️⃣ LinkedIn Outreach", "Search '[Niche] Brand Manager' or 'Influencer PR' on LinkedIn for direct contacts.", Icons.Default.People, "Step 2"),
        InstagramQuadruple("3️⃣ Direct Brand Website", "Look at footer links: 'Affiliates', 'PR Enquiries' or 'Creator Program'.", Icons.Default.Language, "Step 3"),
        InstagramQuadruple("4️⃣ Creator Platforms", "Register on Winkl, OneImpression, Influencer.in, CreatorIQ for auto brand deals.", Icons.Default.Apps, "Step 4"),
        InstagramQuadruple("5️⃣ Cold Emailing PRs", "Send personalized emails directly to marketing agency reps.", Icons.Default.Email, "Step 5")
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("5", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 5", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("5 Proven Channels to Find Brands 🔍", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Brands ko find karne ke liye in 5 channels ka use karo:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        channels.forEach { item ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(item.third, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(22.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(item.first, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text(item.second, fontSize = 11.5.sp, color = TextMuted)
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Channels Learned ✅" else "Understand Brand Finding ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 6: COLD DM MASTERY (10 TEMPLATES) (606)
// =========================================================
@Composable
private fun Level6Lesson6ColdDmMasteryCard(
    isCompleted: Boolean,
    selectedCategory: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var selectedTemplateIndex by remember { mutableStateOf(0) }

    val templates = remember(selectedCategory) {
        listOf(
            "Hey [Brand Name] team! 👋 Really loved your latest [Product Name]. I create high-converting $selectedCategory reels for a highly engaged audience. Would love to send over my Media Kit for a potential reel collab!",
            "Hi [Brand Name]! I'm a $selectedCategory content creator reaching 50k+ viewers weekly. I'm putting together a reel on [Topic] next week and your brand is a perfect fit. Shall I share my rate card?",
            "Greetings [Brand Name] PR Team. We admire your brand's commitment to quality. My channel focuses on premium $selectedCategory content. I'd love to discuss a strategic partnership for your next campaign.",
            "Hey [Local Business Name]! Love what you're doing in town. I have an active local audience interested in $selectedCategory. Let's collaborate on a spotlight reel to boost your local foot traffic!",
            "Hey team! As a daily user of [Product], I made a concept video showing how I integrate it in my $selectedCategory workflow. Would love to share it with your marketing team!",
            "Hi [Brand]! Your product matches my upcoming $selectedCategory series perfectly. Are you open to sending a sample product for an honest review reel & unboxing story series?",
            "Hey [Fitness/Brand]! My community frequently asks about the best products for $selectedCategory. I'd love to feature your brand in an upcoming dedicated tutorial reel.",
            "Hello [Brand]! I'm planning a $selectedCategory campaign for next month. My reels average 25k views with 7% engagement. Can I send my Media Kit for your review?",
            "Hey [Brand Name]! I love your digital tools. I can create a step-by-step tutorial reel demonstrating your software to $selectedCategory enthusiasts. Let's connect!",
            "Hi [Brand Name] Team! Looking for a long-term brand ambassador in $selectedCategory? Let's do a 3-month retainer with monthly reels & story link coverage!"
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("6", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 6", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Cold DM Mastery (10 Templates) 💬", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("10 Unique, High-Conversion DM Pitch Templates:", fontSize = 12.5.sp, color = TextWhite)

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = GlassBg,
                        border = BorderStroke(1.dp, GoldAccent)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Template #${selectedTemplateIndex + 1} of 10", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                                Row {
                                    IconButton(
                                        onClick = { if (selectedTemplateIndex > 0) selectedTemplateIndex-- },
                                        enabled = selectedTemplateIndex > 0
                                    ) {
                                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = TextWhite)
                                    }
                                    IconButton(
                                        onClick = { if (selectedTemplateIndex < templates.size - 1) selectedTemplateIndex++ },
                                        enabled = selectedTemplateIndex < templates.size - 1
                                    ) {
                                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = TextWhite)
                                    }
                                }
                            }

                            SelectionContainer {
                                Text(
                                    text = templates[selectedTemplateIndex],
                                    fontSize = 12.sp,
                                    color = TextWhite,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Templates Saved ✅" else "Copy Template & Continue ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 7: PROFESSIONAL EMAIL GENERATOR (607)
// =========================================================
@Composable
private fun Level6Lesson7ProfessionalEmailCard(
    isCompleted: Boolean,
    selectedCategory: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var brandNameInput by remember { mutableStateOf("Nike / Boat") }

    val emailSubject = "Collaboration Proposal: @your_handle x $brandNameInput ($selectedCategory)"
    val emailBody = remember(brandNameInput, selectedCategory) {
        """
        Subject: $emailSubject

        Dear $brandNameInput Marketing & PR Team,

        I hope this email finds you well.

        My name is [Your Name], and I am the creator behind @your_handle, a growing Instagram page dedicated to high-quality $selectedCategory content.

        Our community currently consists of 25,000+ active followers with an average reel reach of 35,000 views per post. We recently noticed your new campaign for $brandNameInput and believe it resonates perfectly with our audience.

        We would love to collaborate on a high-impact Instagram Reel featuring your product, accompanied by dedicated Story sticker links.

        I have attached our updated Media Kit highlighting audience demographics and past campaign results.

        Could you please let us know if you are open to reviewing a partnership proposal for this quarter?

        Best regards,
        [Your Name]
        Instagram: @your_handle
        Portfolio / Media Kit Link
        """.trimIndent()
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("7", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 7", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Professional Email Pitch Generator ✉️", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter target brand name to generate formal email pitch:", fontSize = 12.5.sp, color = TextWhite)

                    OutlinedTextField(
                        value = brandNameInput,
                        onValueChange = { brandNameInput = it },
                        placeholder = { Text("Brand Name e.g. Samsung", color = TextMuted, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldAccent,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = GlassBg,
                        border = BorderStroke(1.dp, GoldAccent)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            SelectionContainer {
                                Text(emailBody, fontSize = 11.5.sp, color = TextWhite, lineHeight = 16.sp)
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Email Pitch Saved ✅" else "Save Email Pitch ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 8: NEGOTIATION MASTERCLASS (608)
// =========================================================
@Composable
private fun Level6Lesson8NegotiationMasteryCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("8", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 8", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Brand Deal Negotiation Rules 🤝", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Kabhi bhi brand ki pehli offer directly mat accept karo! Follow these 4 rules:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SettingExplanationRow(
                            title = "1️⃣ Ask Budget First Script",
                            toggleState = "RULE 1",
                            explanation = "\"Thanks for reaching out! To suggest the right deliverables package, could you share the allocated budget range for this campaign?\""
                        )
                        SettingExplanationRow(
                            title = "2️⃣ Anchor 25% Higher",
                            toggleState = "RULE 2",
                            explanation = "If your minimum target price is ₹5,000, quote ₹6,500 initially. Brands always counter-offer lower, so anchoring leaves buffer room."
                        )
                        SettingExplanationRow(
                            title = "3️⃣ Usage Rights Surcharge",
                            toggleState = "RULE 3",
                            explanation = "If the brand wants to run your reel as a Facebook/IG Paid Ad, charge 30-50% extra for paid ad digital rights!"
                        )
                        SettingExplanationRow(
                            title = "4️⃣ Limit Free Revisions",
                            toggleState = "RULE 4",
                            explanation = "Always state: \"Includes 1 minor edit round. Additional script or scene reshoots incur a ₹1,000 revision fee.\""
                        )
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Negotiation Rules Mastered ✅" else "Understand Negotiation ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 9: FAKE BRAND DETECTION & SCAM SHIELD (609)
// =========================================================
@Composable
private fun Level6Lesson9FakeBrandScamShieldCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val redFlags = listOf(
        "🚨 Email sender is @gmail.com or @yahoo.com instead of official domain (e.g. brand.com)",
        "🚨 Brand asks YOU to pay shipping fee or registration fee upfront for free product",
        "🚨 Asking for your IG login credentials, OTP, or password to 'verify account'",
        "🚨 Unrealistic high payment ($5000 for 100 followers) via unverified cheque / gift card",
        "🚨 Redirecting conversation to Telegram or suspicious crypto links"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("9", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 9", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Fake Brand Detection & Scam Shield 🛡️", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Scam brand deals se bachne ke liye ye 5 RED FLAGS hamesha check karo:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        redFlags.forEach { flag ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Text(flag, fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp), lineHeight = 16.sp)
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Scam Shield Active ✅" else "Activate Scam Shield ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 10: COLLABORATION AGREEMENT & CONTRACTS (610)
// =========================================================
@Composable
private fun Level6Lesson10AgreementContractCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("10", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 10", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Collaboration Agreement Basics 📜", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Har paid deal ke pehle ye 5 basic clauses written email/contract mein confirm karo:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SettingExplanationRow("📦 Deliverables", "ITEM 1", "Exact number of Reels, Stories, or Carousel posts required.")
                        SettingExplanationRow("📅 Timeline", "ITEM 2", "Draft submission date & final posting date.")
                        SettingExplanationRow("✏️ Revision Cap", "ITEM 3", "Max 1 minor edit round included before posting.")
                        SettingExplanationRow("🌐 Usage Rights", "ITEM 4", "Organic profile posting vs Paid Ad usage rights.")
                        SettingExplanationRow("💵 Payment Terms", "ITEM 5", "50% advance on approval, remaining 50% post-publish (Net 15).")
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Agreement Terms Understood ✅" else "Save Agreement Guide ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 11: INVOICE GENERATOR & TEMPLATE (611)
// =========================================================
@Composable
private fun Level6Lesson11InvoiceGeneratorCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var creatorName by remember { mutableStateOf("Your Name") }
    var brandName by remember { mutableStateOf("Brand Name") }
    var priceInput by remember { mutableStateOf("₹5,000") }

    val invoiceText = remember(creatorName, brandName, priceInput) {
        """
        🧾 INVOICE #INV-2026-001
        ------------------------------------------
        FROM: $creatorName (@your_handle)
        TO: $brandName Marketing Team
        DATE: July 31, 2026
        
        DESCRIPTION OF SERVICES:
        1x Sponsored Instagram Reel (1080p 60fps)
        2x Instagram Story Posts with Link Sticker
        ------------------------------------------
        TOTAL AMOUNT DUE: $priceInput
        ------------------------------------------
        PAYMENT DETAILS:
        • UPI ID: creator@upi
        • Bank: HDFC Bank / ICICI
        • Acc No: XXXXXXXXXX1234 (IFSC: HDFC0001234)
        • Payment Terms: Due upon receipt / Net 15
        ------------------------------------------
        Thank you for collaborating!
        """.trimIndent()
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("11", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 11", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Simple Creator Invoice Builder 🧾", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Payment claim karne ke liye professional invoice generate karo:", fontSize = 12.5.sp, color = TextWhite)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = creatorName,
                            onValueChange = { creatorName = it },
                            placeholder = { Text("Your Name", fontSize = 11.sp, color = TextMuted) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, unfocusedBorderColor = GlassBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                        )
                        OutlinedTextField(
                            value = brandName,
                            onValueChange = { brandName = it },
                            placeholder = { Text("Brand Name", fontSize = 11.sp, color = TextMuted) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, unfocusedBorderColor = GlassBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                        )
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = GlassBg,
                        border = BorderStroke(1.dp, GoldAccent)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            SelectionContainer {
                                Text(invoiceText, fontSize = 11.sp, color = TextWhite, lineHeight = 15.sp)
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Invoice Ready ✅" else "Save Invoice Template ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 12: PAYMENT METHODS & TAX BASICS (612)
// =========================================================
@Composable
private fun Level6Lesson12PaymentMethodsCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("12", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 12", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Payment Gateways & Basic Taxes 💳", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Receiving payments safely in India & globally:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SettingExplanationRow("📱 Domestic (India)", "UPI / NEFT", "Google Pay, PhonePe, Paytm UPI ID, or Direct Bank NEFT / IMPS transfer.")
                        SettingExplanationRow("🌍 International Deals", "PayPal / Wise", "PayPal business account or Wise for seamless USD/EUR transfers.")
                        SettingExplanationRow("🧾 Basic TDS Note", "10% TDS", "Indian brands deduct 10% TDS under Section 194J/194C on payouts >₹30,000. Collect Form 16A!")
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Payment Methods Set ✅" else "Confirm Payment Setup ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 13: AFTER COLLABORATION & RETAINERS (613)
// =========================================================
@Composable
private fun Level6Lesson13AfterCollabCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("13", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 13", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("After-Collab Strategy & Monthly Retainers 🔄", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Reel publish hone ke baad 1-time client ko monthly recurring retainer client mein badlo:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SettingExplanationRow("📊 7-Day Performance Report", "STEP 1", "Send screenshot of Reel Insights (Views, Reach, Clicks) 7 days after upload.")
                        SettingExplanationRow("❤️ Thank You & Feedback Note", "STEP 2", "\"Thank you for working together! Our audience loved the product.\"")
                        SettingExplanationRow("🔁 Retainer Pitch", "STEP 3", "\"Shall we do a 3-month retainer package with 2 Reels/month at a 15% discount?\"")
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Retainer Strategy Learned ✅" else "Understand Retainer Pitch ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 14: AFFILIATE MARKETING MASTERY (614)
// =========================================================
@Composable
private fun Level6Lesson14AffiliateMarketingCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("14", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 14", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Affiliate Marketing Blueprint 🔗", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Passively earn 5%-15% commission on every product recommendation:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SettingExplanationRow("🌐 Top Affiliate Networks", "NETWORKS", "Amazon Associates, EarnKaro, Impact, ShareASale, CUE Links.")
                        SettingExplanationRow("📲 Link in Bio Hub", "LINK HUB", "Use Linktree / Beacons / Shopmy to organize all product links cleanly.")
                        SettingExplanationRow("⚖️ Compliance & Disclosure", "ASCI RULES", "Always add #ad #affiliate tag in caption & story to follow ASCI/FTC legal rules!")
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Affiliate Mastered ✅" else "Save Affiliate Blueprint ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 15: PERSONAL BRANDING, AI INCOME PLANNER & SPECIAL OFFER ANALYZER (615)
// =========================================================
@Composable
private fun Level6Lesson15PersonalBrandIncomePlannerCard(
    isCompleted: Boolean,
    selectedCategory: String,
    userFollowers: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var followerCountInput by remember { mutableStateOf(if (userFollowers.isBlank()) "10000" else userFollowers) }
    var avgViewsInput by remember { mutableStateOf("15000") }
    var postsPerWeek by remember { mutableStateOf("4") }

    // SPECIAL FEATURE: OFFER/DM ANALYZER STATE
    var offerTextToAnalyze by remember { mutableStateOf("") }
    var offerAnalysisResult by remember { mutableStateOf<String?>(null) }

    val followersInt = followerCountInput.toIntOrNull() ?: 10000
    val viewsInt = avgViewsInput.toIntOrNull() ?: 15000

    // Income calculation estimation
    val estimatedLow = (followersInt * 0.2f + viewsInt * 0.1f).toInt().coerceAtLeast(1500)
    val estimatedHigh = (followersInt * 0.5f + viewsInt * 0.25f).toInt().coerceAtLeast(4000)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("15", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 15 & BONUS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("AI Income Planner & Brand Offer Analyzer 🤖", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("💡 BONUS 1: AI Income Potential Estimator", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldAccent)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = followerCountInput,
                            onValueChange = { followerCountInput = it },
                            label = { Text("Followers", fontSize = 11.sp, color = TextMuted) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, unfocusedBorderColor = GlassBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                        )
                        OutlinedTextField(
                            value = avgViewsInput,
                            onValueChange = { avgViewsInput = it },
                            label = { Text("Avg Views", fontSize = 11.sp, color = TextMuted) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, unfocusedBorderColor = GlassBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                        )
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = GlassBg,
                        border = BorderStroke(1.dp, GoldAccent)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Estimated Monthly Income Range ($selectedCategory):", fontSize = 11.5.sp, color = TextMuted)
                            Text("₹${estimatedLow.toFormatedAmount()} – ₹${estimatedHigh.toFormatedAmount()} / month", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = SuccessGreen)
                            Text("Breakdown: 60% Brand Deals + 25% Affiliate Links + 15% Digital Products.", fontSize = 11.sp, color = TextMuted)
                        }
                    }

                    Divider(color = GlassBorder)

                    Text("✨ SPECIAL FEATURE: Brand DM / Offer Letter Analyzer", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                    Text("Paste any Brand DM or Offer Letter here to check legitimacy & counter-pitch script:", fontSize = 11.5.sp, color = TextWhite)

                    OutlinedTextField(
                        value = offerTextToAnalyze,
                        onValueChange = { offerTextToAnalyze = it },
                        placeholder = { Text("Paste DM text e.g. 'Hey, we love your profile! We want to offer you $500 for a post, please register on our site...'", color = TextMuted, fontSize = 11.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, unfocusedBorderColor = GlassBorder, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )

                    Button(
                        onClick = {
                            offerAnalysisResult = analyzeBrandOfferText(offerTextToAnalyze)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Analyze Brand Offer 🔍", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }

                    if (offerAnalysisResult != null) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = GlassBg,
                            border = BorderStroke(1.dp, SuccessGreen)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                SelectionContainer {
                                    Text(offerAnalysisResult!!, fontSize = 11.5.sp, color = TextWhite, lineHeight = 16.sp)
                                }
                            }
                        }
                    }

                    Divider(color = GlassBorder)

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF332300),
                        border = BorderStroke(1.dp, GoldAccent)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("🎯 FINAL MISSION — LEVEL 6", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                            Text("Prepare your Media Kit or send your first Brand Cold DM today!", fontSize = 11.5.sp, color = TextWhite)
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Level 6 Complete! Badge Unlocked 🎉" else "COMPLETE MISSION & UNLOCK BADGE 🏆", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// LEVEL 6 REWARD CELEBRATION MODAL
// =========================================================
@Composable
private fun Level6RewardCelebrationDialog(
    selectedCategory: String,
    currentLang: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF1E1602),
            border = BorderStroke(2.dp, Brush.horizontalGradient(listOf(GoldAccent, WarningAmber)))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "💼", fontSize = 54.sp)

                Text(
                    text = "LEVEL 6 COMPLETE!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldAccent
                )

                Text(
                    text = "Congratulations! You have unlocked the prestigious Glass Badge:",
                    fontSize = 13.sp,
                    color = TextWhite,
                    textAlign = TextAlign.Center
                )

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF332300),
                    border = BorderStroke(1.dp, GoldAccent)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🏆", fontSize = 20.sp)
                        Text("Badge: Creator Business Ready 💼", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                    }
                }

                Text(
                    text = "⚡ Reward: +2500 XP Added to Your Profile",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("CLAIM BADGE & CONTINUE 🚀", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}

// HELPER FUNCTION FOR FORMATTING NUMBERS
private fun Int.toFormatedAmount(): String {
    return String.format("%,d", this)
}

// HELPER FUNCTION FOR ANALYZING BRAND OFFER TEXT
private fun analyzeBrandOfferText(offerText: String): String {
    if (offerText.isBlank()) {
        return "💡 Paste brand email or DM text above to perform AI Scam & Legitimacy check."
    }

    val textLower = offerText.lowercase()
    val isScam = textLower.contains("register fee") || textLower.contains("shipping fee") ||
            textLower.contains("telegram") || textLower.contains("gift card") ||
            textLower.contains("password") || textLower.contains("otp")

    return if (isScam) {
        "🚨 HIGH SCAM WARNING! This offer contains scam red flags (Upfront fee/Telegram/Password request). DO NOT pay or share credentials!"
    } else {
        "✅ LEGITIMATE OFFER LIKELY! Standard pitch detected. Counter-Offer Strategy: Quote 20% higher than your minimum rate and ask for campaign deliverables & budget confirmation!"
    }
}

// =========================================================
// PHASE 9 — LEVEL 7 AI CREATOR COACH JOURNEY CARD
// =========================================================

@Composable
private fun InstagramLevel7JourneyCard(
    completedCount: Int,
    totalCount: Int = 10,
    isAllCompleted: Boolean,
    currentLang: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(24.dp), ambientColor = MagentaAccent, spotColor = MagentaAccent),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF1E072A),
        border = BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(MagentaAccent, VioletGlow, GoldAccent)))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF330C48),
                    border = BorderStroke(1.dp, MagentaAccent)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "👑", fontSize = 14.sp)
                        Text(
                            text = "LEVEL 7 • AI CREATOR COACH (LIFETIME)",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isAllCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg,
                    border = BorderStroke(1.dp, if (isAllCompleted) SuccessGreen else GlassBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isAllCompleted) Icons.Default.EmojiEvents else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isAllCompleted) GoldAccent else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (isAllCompleted) "Badge: Creator Legend 👑" else "Badge Locked 🔒",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAllCompleted) GoldAccent else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "AI Creator Coach (Lifetime Mentor) 🎓",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite
            )

            // AI MENTOR WELCOME STATEMENT
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(14.dp),
                color = GlassBg,
                border = BorderStroke(1.dp, GlassBorder)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Text("🤖", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "🎉 Congratulations! Tumne Instagram Growth Journey complete kar li. Ab se main sirf teacher nahi... Tumhara Lifetime Creator Coach hoon. Daily check-in, content strategy, profile audit, reel review, brand deal negotiation aur growth targets—main har step par tumhare saath hoon!",
                        fontSize = 12.5.sp,
                        color = TextWhite,
                        lineHeight = 17.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // METRICS ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(text = "🛠️ 10 Permanent Tools", fontSize = 11.5.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    Text(text = "♾️ Lifetime Coach", fontSize = 11.5.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    Text(text = "⚡ +5000 XP", fontSize = 11.5.sp, color = GoldAccent, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "$completedCount / $totalCount Activated",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isAllCompleted) SuccessGreen else GoldAccent
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { completedCount.toFloat() / totalCount.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = if (isAllCompleted) SuccessGreen else MagentaAccent,
                trackColor = GlassBg
            )
        }
    }
}

// =========================================================
// LEVEL 7 LIFETIME AI CREATOR COACH DASHBOARD SECTION
// =========================================================
@Composable
private fun InstagramLevel7AiCreatorCoachDashboardSection(
    completedLessons: Set<Int>,
    selectedCategory: String,
    userGoal: String,
    userFollowers: String,
    currentLang: String,
    onLessonCompleted: (Int) -> Unit,
    onOpenSmartHelp: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // STEP 701: HOME DASHBOARD & LIVE CREATOR STATS
        Level7Lesson1HomeDashboardCard(
            isCompleted = completedLessons.contains(701),
            selectedCategory = selectedCategory,
            userFollowers = userFollowers,
            userGoal = userGoal,
            onMissionComplete = { onLessonCompleted(701) }
        )

        // STEP 702: DAILY CHECK-IN & ZERO-GUILT MOTIVATION ENGINE
        Level7Lesson2DailyCheckinCard(
            isCompleted = completedLessons.contains(702),
            onMissionComplete = { onLessonCompleted(702) }
        )

        // STEP 703: UNLIMITED AI CONTENT PLANNER & STRATEGY HUB
        Level7Lesson3ContentPlannerCard(
            isCompleted = completedLessons.contains(703),
            selectedCategory = selectedCategory,
            onMissionComplete = { onLessonCompleted(703) }
        )

        // STEP 704: FULL PROFILE REVIEW & AUDIT AI
        Level7Lesson4ProfileReviewCard(
            isCompleted = completedLessons.contains(704),
            selectedCategory = selectedCategory,
            onMissionComplete = { onLessonCompleted(704) }
        )

        // STEP 705: REEL PERFORMANCE & RETENTION REVIEWER
        Level7Lesson5ReelReviewCard(
            isCompleted = completedLessons.contains(705),
            onMissionComplete = { onLessonCompleted(705) }
        )

        // STEP 706: BRAND DEAL & CONTRACT ANALYZER
        Level7Lesson6BrandDealReviewCard(
            isCompleted = completedLessons.contains(706),
            onMissionComplete = { onLessonCompleted(706) }
        )

        // STEP 707: MONTHLY GROWTH REPORT & TARGET GENERATOR
        Level7Lesson7MonthlyReportCard(
            isCompleted = completedLessons.contains(707),
            selectedCategory = selectedCategory,
            userFollowers = userFollowers,
            onMissionComplete = { onLessonCompleted(707) }
        )

        // STEP 708: DYNAMIC GOAL SYSTEM & TARGET SELECTOR
        Level7Lesson8GoalSystemCard(
            isCompleted = completedLessons.contains(708),
            onMissionComplete = { onLessonCompleted(708) }
        )

        // STEP 709: CREATOR CHALLENGES & GAMIFIED BADGES
        Level7Lesson9CreatorChallengesCard(
            isCompleted = completedLessons.contains(709),
            onMissionComplete = { onLessonCompleted(709) }
        )

        // STEP 710: COMMUNITY HUB, ALGORITHM UPDATES & LIFETIME Q&A
        Level7Lesson10CommunityHubCard(
            isCompleted = completedLessons.contains(710),
            selectedCategory = selectedCategory,
            onMissionComplete = { onLessonCompleted(710) },
            onOpenSmartHelp = onOpenSmartHelp
        )
    }
}

// =========================================================
// STEP 701: HOME DASHBOARD & LIVE CREATOR STATS
// =========================================================
@Composable
private fun Level7Lesson1HomeDashboardCard(
    isCompleted: Boolean,
    selectedCategory: String,
    userFollowers: String,
    userGoal: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var followerCountInput by remember { mutableStateOf(userFollowers.ifBlank { "12,500" }) }
    var weeklyGoalInput by remember { mutableStateOf("Post 4 High-Hook Reels") }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("1", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 701", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Live Creator Dashboard & Goal Tracker 📊", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF26103A),
                        border = BorderStroke(1.dp, MagentaAccent)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("👑 Level: Creator Legend", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                                Text("⚡ 15,000 XP", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("🔥 Active Streak: 7 Days", fontSize = 12.sp, color = TextWhite)
                                Text("🎯 Niche: $selectedCategory", fontSize = 12.sp, color = TextWhite)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = followerCountInput,
                        onValueChange = { followerCountInput = it },
                        label = { Text("Current Followers", color = TextMuted, fontSize = 11.5.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MagentaAccent,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    OutlinedTextField(
                        value = weeklyGoalInput,
                        onValueChange = { weeklyGoalInput = it },
                        label = { Text("Weekly Goal", color = TextMuted, fontSize = 11.5.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MagentaAccent,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Dashboard Active ✅" else "Activate Live Dashboard ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 702: DAILY CHECK-IN & ZERO-GUILT MOTIVATION ENGINE
// =========================================================
@Composable
private fun Level7Lesson2DailyCheckinCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var didUploadToday by remember { mutableStateOf<Boolean?>(null) }
    var selectedReason by remember { mutableStateOf<String?>(null) }

    val reasons = listOf("Busy with Work/Studies", "No Content Idea", "Stuck in Editing", "No Motivation / Burnout", "Other")

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("2", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 702", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Daily Check-in & Zero-Guilt AI Motivation 📅", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Did you upload a Reel or Story today?", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { didUploadToday = true; selectedReason = null },
                            colors = ButtonDefaults.buttonColors(containerColor = if (didUploadToday == true) SuccessGreen else GlassBg),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("YES 🚀", fontWeight = FontWeight.Bold, color = TextWhite)
                        }
                        Button(
                            onClick = { didUploadToday = false },
                            colors = ButtonDefaults.buttonColors(containerColor = if (didUploadToday == false) ErrorRed else GlassBg),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("NO 💬", fontWeight = FontWeight.Bold, color = TextWhite)
                        }
                    }

                    if (didUploadToday == true) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = GlassBg,
                            border = BorderStroke(1.dp, SuccessGreen)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("🎉 AWESOME WORK!", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                Text("Consistency momentum locked! Reel analytics test karo baad mein.", fontSize = 11.5.sp, color = TextWhite)
                            }
                        }
                    } else if (didUploadToday == false) {
                        Text("Select reason (AI Zero-Guilt Coach solution dega):", fontSize = 11.5.sp, color = TextMuted)
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            reasons.forEach { r ->
                                val isSel = selectedReason == r
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedReason = r },
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSel) Color(0xFF330C48) else GlassBg,
                                    border = BorderStroke(1.dp, if (isSel) MagentaAccent else GlassBorder)
                                ) {
                                    Text(r, fontSize = 11.5.sp, color = if (isSel) GoldAccent else TextWhite, modifier = Modifier.padding(10.dp))
                                }
                            }
                        }

                        if (selectedReason != null) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GoldAccent)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("🤖 Warm Coach Message:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                                    Text(
                                        text = when (selectedReason) {
                                            "Busy with Work/Studies" -> "Welcome back! ❤️ No stress at all. 5 minutes ke andar 1 text-overlay reel draft kar lo!"
                                            "No Content Idea" -> "Welcome back! ❤️ Top 3 FAQs in your niche pick karo and answer in 15 seconds."
                                            "Stuck in Editing" -> "Welcome back! ❤️ Over-editing perfectionism chhodo. Raw & honest content performs best!"
                                            "No Motivation / Burnout" -> "Welcome back! ❤️ Rest is part of the process. Rest today, dominate tomorrow!"
                                            else -> "Welcome back! ❤️ Chalo wahi se continue karte hain jahan hum ruk gaye the."
                                        },
                                        fontSize = 11.5.sp,
                                        color = TextWhite
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted && didUploadToday != null
                    ) {
                        Text(if (isCompleted) "Check-in Complete ✅" else "Save Daily Check-in ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 703: UNLIMITED AI CONTENT PLANNER & STRATEGY HUB
// =========================================================
@Composable
private fun Level7Lesson3ContentPlannerCard(
    isCompleted: Boolean,
    selectedCategory: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val contentPlanner = remember(selectedCategory) {
        mapOf(
            "Today's Reel Idea 🎬" to "3 Big Myths about $selectedCategory that everyone believes (Hook: 'Stop doing this in $selectedCategory!')",
            "Tomorrow's Reel Idea 💡" to "Behind-the-scenes workflow / my exact setup for $selectedCategory (Hook: 'How I manage $selectedCategory in 30 mins')",
            "Weekly 7-Day Calendar 📅" to "Mon: Tutorial | Tue: Mistake Fix | Wed: Tool Recommendation | Thu: BTS | Fri: Case Study | Sat: Q&A | Sun: Recap Reel",
            "Monthly Strategy 🚀" to "Focus 60% on educational problem-solving reels + 30% on trending audio relatable reels + 10% on direct CTA product/service reels."
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("3", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 703", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("AI Content Planner & Strategy Engine 💡", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Auto-generated Content Strategy for $selectedCategory:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        contentPlanner.forEach { (title, desc) ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                                    Text(desc, fontSize = 11.5.sp, color = TextWhite)
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Plan Locked ✅" else "Accept Content Plan ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 704: FULL PROFILE REVIEW & AUDIT AI
// =========================================================
@Composable
private fun Level7Lesson4ProfileReviewCard(
    isCompleted: Boolean,
    selectedCategory: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var usernameInput by remember { mutableStateOf("@creator_legend") }
    var isAudited by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("4", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 704", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Full Instagram Profile Review & Audit AI 🔍", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter handle or profile details to run deep AI audit:", fontSize = 12.5.sp, color = TextWhite)

                    OutlinedTextField(
                        value = usernameInput,
                        onValueChange = { usernameInput = it },
                        label = { Text("Instagram Username", color = TextMuted, fontSize = 11.5.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MagentaAccent,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    Button(
                        onClick = { isAudited = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MagentaAccent),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Run Deep AI Audit 🔍", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }

                    if (isAudited) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = GlassBg,
                            border = BorderStroke(1.dp, GoldAccent)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Profile Audit Score: 94/100", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                    Text("Niche: $selectedCategory", fontSize = 11.5.sp, color = GoldAccent)
                                }
                                Text("✔ Bio Hook: Clean value statement detected.", fontSize = 11.5.sp, color = TextWhite)
                                Text("✔ Contact Info: Direct PR email integrated.", fontSize = 11.5.sp, color = TextWhite)
                                Text("💡 Action Item: Standardize reel cover thumbnail fonts for 100% brand consistency!", fontSize = 11.5.sp, color = WarningAmber)
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted && isAudited
                    ) {
                        Text(if (isCompleted) "Audit Complete ✅" else "Save Audit & Continue ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 705: REEL PERFORMANCE & RETENTION REVIEWER
// =========================================================
@Composable
private fun Level7Lesson5ReelReviewCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var hookInput by remember { mutableStateOf("Stop making this mistake in your reels!") }
    var retentionInput by remember { mutableStateOf("42%") }
    var isAnalyzed by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("5", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 705", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Reel Performance & Retention Reviewer 🎬", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Paste Reel Hook text & Retention stats to analyze:", fontSize = 12.5.sp, color = TextWhite)

                    OutlinedTextField(
                        value = hookInput,
                        onValueChange = { hookInput = it },
                        label = { Text("Reel Hook Text", color = TextMuted, fontSize = 11.5.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MagentaAccent,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    OutlinedTextField(
                        value = retentionInput,
                        onValueChange = { retentionInput = it },
                        label = { Text("Avg Watch Time / Retention %", color = TextMuted, fontSize = 11.5.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MagentaAccent,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    Button(
                        onClick = { isAnalyzed = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MagentaAccent),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Analyze Reel Performance 🚀", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }

                    if (isAnalyzed) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = GlassBg,
                            border = BorderStroke(1.dp, GoldAccent)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("AI Optimization Feedback:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                                Text("🔥 Hook Strength: High (Negative Curiosity Triggered)", fontSize = 11.5.sp, color = TextWhite)
                                Text("💡 Retention Fix: 42% is good. To push to 70%+, add visual zoom cuts every 2 seconds & loop seamless audio!", fontSize = 11.5.sp, color = WarningAmber)
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted && isAnalyzed
                    ) {
                        Text(if (isCompleted) "Reel Reviewed ✅" else "Complete Reel Review ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 706: BRAND DEAL & CONTRACT ANALYZER
// =========================================================
@Composable
private fun Level7Lesson6BrandDealReviewCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var offerText by remember { mutableStateOf("Hi! We love your page. We want to offer you $150 for 1 reel + 2 stories. Let us know your thoughts.") }
    var isAnalyzed by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("6", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 706", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Brand Deal & Contract Analyzer 💼", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Paste Brand Email / DM or Offer details for AI decision:", fontSize = 12.5.sp, color = TextWhite)

                    OutlinedTextField(
                        value = offerText,
                        onValueChange = { offerText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MagentaAccent,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    Button(
                        onClick = { isAnalyzed = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MagentaAccent),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Analyze Brand Offer 🤖", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }

                    if (isAnalyzed) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = GlassBg,
                            border = BorderStroke(1.dp, GoldAccent)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("AI Decision: ACCEPT WITH COUNTER 🤝", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                Text("Suggested Counter: Reply quoting $200 and requesting 50% advance before shooting.", fontSize = 11.5.sp, color = TextWhite)
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted && isAnalyzed
                    ) {
                        Text(if (isCompleted) "Analysis Complete ✅" else "Save Analysis ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 707: MONTHLY GROWTH REPORT & TARGET GENERATOR
// =========================================================
@Composable
private fun Level7Lesson7MonthlyReportCard(
    isCompleted: Boolean,
    selectedCategory: String,
    userFollowers: String,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("7", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 707", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Monthly Growth Report & Target Summary 📈", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = GlassBg,
                        border = BorderStroke(1.dp, GoldAccent)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("📊 MONTHLY PERFORMANCE SUMMARY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("📈 Est. Monthly Reach:", fontSize = 11.5.sp, color = TextWhite)
                                Text("180,000+ Accounts", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("🔥 Best Performing Reel:", fontSize = 11.5.sp, color = TextWhite)
                                Text("48,500 Views (Hook: Mistake)", fontSize = 11.5.sp, color = GoldAccent)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("🎯 Next Month Growth Target:", fontSize = 11.5.sp, color = TextWhite)
                                Text("+3,000 Followers", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Monthly Report Generated ✅" else "Confirm Monthly Report ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 708: DYNAMIC GOAL SYSTEM & TARGET SELECTOR
// =========================================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Level7Lesson8GoalSystemCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var selectedTarget by remember { mutableStateOf("🚀 First Brand Deal") }

    val targets = listOf("🎯 10K Followers", "🔥 50K Followers", "👑 100K Followers", "🚀 First Brand Deal", "💰 ₹10K/mo Income", "⭐ Personal Brand Legend")

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("8", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 708", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Dynamic Goal System & Target Selector 🎯", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Select your primary next milestone to adjust AI coaching:", fontSize = 12.5.sp, color = TextWhite)

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        targets.forEach { t ->
                            val isSel = selectedTarget == t
                            Surface(
                                modifier = Modifier.clickable { selectedTarget = t },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) Color(0xFF330C48) else GlassBg,
                                border = BorderStroke(1.dp, if (isSel) MagentaAccent else GlassBorder)
                            ) {
                                Text(t, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = if (isSel) GoldAccent else TextWhite, modifier = Modifier.padding(10.dp))
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Target Locked ✅" else "Lock Target Milestone ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 709: CREATOR CHALLENGES & GAMIFIED BADGES
// =========================================================
@Composable
private fun Level7Lesson9CreatorChallengesCard(
    isCompleted: Boolean,
    onMissionComplete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }

    val challenges = listOf(
        InstagramQuadruple("⚡ 7-Day Sprint Challenge", "Post 1 Reel every day for 7 days without missing.", Icons.Default.Speed, "Active 🔥"),
        InstagramQuadruple("🔥 30-Day Consistency Challenge", "Maintain 80%+ engagement & post 20 reels.", Icons.Default.CalendarToday, "Unlocked 🔓"),
        InstagramQuadruple("🚀 90-Day Viral Scaling", "Cross 100k+ total views in 90 days.", Icons.Default.TrendingUp, "In Progress"),
        InstagramQuadruple("👑 365-Day Creator Legend", "Build a sustainable full-time creator business.", Icons.Default.WorkspacePremium, "Lifetime Goal")
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("9", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 709", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Creator Challenges & Gamified Badges 🏆", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Participate in Creator Challenges to earn Glass Badges:", fontSize = 12.5.sp, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        challenges.forEach { item ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(item.third, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(22.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(item.first, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text(item.second, fontSize = 11.5.sp, color = TextMuted)
                                    }
                                    Text(item.fourth, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Challenges Joined ✅" else "Accept Creator Challenges ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// STEP 710: COMMUNITY HUB, ALGORITHM UPDATES & LIFETIME Q&A
// =========================================================
@Composable
private fun Level7Lesson10CommunityHubCard(
    isCompleted: Boolean,
    selectedCategory: String,
    onMissionComplete: () -> Unit,
    onOpenSmartHelp: (String, String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(!isCompleted) }
    var promptInput by remember { mutableStateOf("") }

    val updates = listOf(
        "📢 Instagram Algorithm Update: Original audio & watch time completion % given 2X distribution priority!",
        "⚡ New Feature: Trial Reels allow testing content with non-followers without cluttering grid!",
        "💡 Pro Tip: DM keyword automation boosts comment engagement by up to 300%!"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (isCompleted) Color(0xFF0D1C15) else CardBg,
        border = BorderStroke(1.dp, if (isCompleted) SuccessGreen.copy(alpha = 0.6f) else GlassBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) SuccessGreen.copy(alpha = 0.2f) else GlassBg)
                            .border(1.dp, if (isCompleted) SuccessGreen else MagentaAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text("10", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("STEP 710", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("Community Hub & Lifetime AI Q&A 💬", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = TextMuted
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Latest Algorithm Updates & Creator News:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = GoldAccent)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        updates.forEach { up ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                color = GlassBg,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Text(up, fontSize = 11.5.sp, color = TextWhite, modifier = Modifier.padding(10.dp))
                            }
                        }
                    }

                    Text("Ask Anything to your Lifetime AI Creator Coach:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                    OutlinedTextField(
                        value = promptInput,
                        onValueChange = { promptInput = it },
                        placeholder = { Text("Ask about Instagram, Editing, Brand Deals, Mindset...", color = TextMuted, fontSize = 11.5.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MagentaAccent,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    Button(
                        onClick = {
                            val question = promptInput.ifBlank { "How can I scale my $selectedCategory reels to 100k views?" }
                            onOpenSmartHelp("Lifetime Coach Q&A", question)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MagentaAccent),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Ask AI Coach Live 🤖", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }

                    Button(
                        onClick = onMissionComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isCompleted
                    ) {
                        Text(if (isCompleted) "Lifetime Coach Active ✅" else "Activate Lifetime Coach ➔", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

// =========================================================
// LEVEL 7 REWARD CELEBRATION MODAL (FINAL LEVEL COMPLETE)
// =========================================================
@Composable
private fun Level7RewardCelebrationDialog(
    selectedCategory: String,
    currentLang: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF1B072B),
            border = BorderStroke(2.dp, Brush.linearGradient(listOf(GoldAccent, MagentaAccent, VioletGlow)))
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("🎓 🎉 👑", fontSize = 42.sp)

                Text(
                    text = "COURSE COMPLETED!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldAccent
                )

                Text(
                    text = "Instagram Growth Guide Completed — Lifetime AI Creator Coach Activated!",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    textAlign = TextAlign.Center
                )

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFF3B0C54),
                    border = BorderStroke(1.dp, GoldAccent)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("🏆 UNLOCKED PRESTIGIOUS BADGE", fontSize = 11.sp, color = TextMuted)
                        Text("Badge: Creator Legend 👑", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                        Text("⚡ Reward: +5000 XP Added to Your Profile", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                    }
                }

                Text(
                    text = "Tumne saare levels successfully complete kar liye hain. AI Creator Coach ab permanently tumhare saath active rahega!",
                    fontSize = 12.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("ENTER LIFETIME COACH DASHBOARD 🚀", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}






