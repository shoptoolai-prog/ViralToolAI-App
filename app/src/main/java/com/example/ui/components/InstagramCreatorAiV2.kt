package com.example.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import com.example.ui.theme.responsiveImeAndNavPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.draw.shadow
import com.example.ui.theme.ElectricPurple
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.ui.screens.OfficialLogo
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * PHASE — INSTAGRAM CREATOR AI V2 (ZERO TO HERO PERSONAL AI MENTOR)
 *
 * An interactive, personal AI mentor that guides users step-by-step from zero to a professional
 * Instagram creator.
 *
 * Core Features:
 * 1. Language Selection (हिन्दी, English, Hinglish)
 * 2. Creator Type Selection (Influencer, Tech, Food, Vlogger, etc.)
 * 3. Step Lock System (Next step unlocks ONLY when current is confirmed)
 * 4. Friendly Conversational AI Personality (No robotic lectures)
 * 5. Smart "Explain Again" Engine (Fresh explanations with real analogies, never repeated)
 * 6. Interactive Script Generator (15s, 30s, 45s, 60s, 90s duration with fresh hooks)
 * 7. Editing Detection & Video Editing Course (VN, CapCut, Premiere)
 * 8. Trending Audio Guide (Voice clarity & audio volume mixing)
 * 9. AI Caption & 3-Tier Hashtag Generator
 * 10. Pre-Upload 9-Point Checklist
 * 11. Post-Upload Analytics & Reply Strategy
 * 12. Complete Session Memory & State Persistence
 */

enum class MentorLanguage(val code: String, val label: String, val nativeName: String) {
    HINDI("HI", "हिन्दी", "Hindi"),
    ENGLISH("EN", "English", "English"),
    HINGLISH("HINGLISH", "Hinglish", "Hinglish")
}

data class MentorStep(
    val id: Int,
    val title: String,
    val shortDesc: String,
    val mentorPromptHi: String,
    val mentorPromptEn: String,
    val mentorPromptHinglish: String,
    val smartQuestionHi: String,
    val smartQuestionEn: String,
    val smartQuestionHinglish: String
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "AI_MENTOR" or "CREATOR"
    val text: String,
    val stepIndex: Int = 0,
    val timestamp: String = "Just now",
    val isFreshExplanation: Boolean = false,
    val scriptData: ScriptResult? = null,
    val captionData: CaptionResult? = null
)

data class ScriptResult(
    val topic: String,
    val duration: String,
    val hook: String,
    val flow: String,
    val cta: String
)

data class CaptionResult(
    val caption: String,
    val hashtags: List<String>
)

val CREATOR_TYPES = listOf(
    "Influencer",
    "Vlogger",
    "Tech Creator",
    "Gaming Creator",
    "Education Creator",
    "Comedy Creator",
    "Lifestyle Creator",
    "Fashion Creator",
    "Business Creator",
    "Fitness Creator",
    "Food Creator",
    "Other"
)

val INSTAGRAM_ROADMAP_STEPS = listOf(
    MentorStep(
        id = 1,
        title = "1. Instagram Download & Setup",
        shortDesc = "App installation & initial account creation",
        mentorPromptHi = "👋 Namaste Creator! Sabse pehle Instagram Play Store/App Store se download karke ek fresh account banao. Kya app install ho gaya?",
        mentorPromptEn = "👋 Welcome Creator! First, download Instagram from the App Store/Play Store and create a fresh account. Is the app installed?",
        mentorPromptHinglish = "👋 Hey Creator! Sabse pehle Instagram download karke fresh account create kar lo. Kya app install ho gaya?",
        smartQuestionHi = "App install ho gaya?",
        smartQuestionEn = "Is the app installed?",
        smartQuestionHinglish = "App install ho gaya?"
    ),
    MentorStep(
        id = 2,
        title = "2. Switch to Professional Account",
        shortDesc = "Unlock creator dashboard & insights",
        mentorPromptHi = "Mast! Ab Settings ⚙️ me jao → Account → Switch to Professional/Creator Account par tap karo. Isse aapko insights aur brand tools milenge. Ban gaya Creator Account?",
        mentorPromptEn = "Great! Now go to Settings ⚙️ → Account → Switch to Professional/Creator Account. This unlocks analytics & monetization tools. Switched to Creator Account?",
        mentorPromptHinglish = "Awesome! Ab Settings ⚙️ -> Account -> Switch to Professional/Creator Account kar lo. Isse real Insights aur Monetization tools unlock ho jaayenge. Switching ho gayi?",
        smartQuestionHi = "Professional account ON hua?",
        smartQuestionEn = "Switched to Professional Account?",
        smartQuestionHinglish = "Professional account ON ho gaya?"
    ),
    MentorStep(
        id = 3,
        title = "3. Profile & Bio Optimization",
        shortDesc = "Username, Bio line, Category & DP",
        mentorPromptHi = "Aapka Bio aapka digital visiting card hai! 1st Line: Aap kaun hain. 2nd Line: Aap kya sikhaate/dikhate hain. 3rd Line: Call To Action + Link. DP clear aur high resolution rakho.",
        mentorPromptEn = "Your Bio is your billboard! Line 1: Who you are. Line 2: What value you offer. Line 3: Call to Action + Link. Keep a sharp, clear DP.",
        mentorPromptHinglish = "Bio aapka main visiting card hai! Line 1: Main kaun hu. Line 2: Viewer ko kya milega. Line 3: Call to Action + Link. Clear profile picture lagao.",
        smartQuestionHi = "Bio & Profile Pic set ho gaye?",
        smartQuestionEn = "Is Bio & Profile Picture updated?",
        smartQuestionHinglish = "Bio & DP set kar liya?"
    ),
    MentorStep(
        id = 4,
        title = "4. High Quality Upload Settings",
        shortDesc = "Crystal clear HD Reels config",
        mentorPromptHi = "Bohot zaroori setting! Settings ⚙️ → Media Quality → Turn ON 'Upload at Highest Quality'. Isse aapke videos blurry nahi honge.",
        mentorPromptEn = "Crucial setting! Settings ⚙️ → Account / Media Quality → Enable 'Upload at Highest Quality'. This prevents pixelated Reels.",
        mentorPromptHinglish = "Super important step! Settings ⚙️ -> Data Usage & Media Quality -> 'Upload at Highest Quality' ON kar do. Reel quality solid rahegi!",
        smartQuestionHi = "Highest quality upload ON kar diya?",
        smartQuestionEn = "Enabled Highest Quality Upload?",
        smartQuestionHinglish = "Highest Quality Upload ON ho gaya?"
    ),
    MentorStep(
        id = 5,
        title = "5. Creator Dashboard & Analytics",
        shortDesc = "Insights, reach & audience demographics",
        mentorPromptHi = "Professional Dashboard open karo. Yahan 'Accounts Reached', 'Engagement', aur 'Total Followers' ki country/age breakdown dikhti hai.",
        mentorPromptEn = "Open your Professional Dashboard. Here you can track Accounts Reached, Engagement Rate, and Follower Demographics.",
        mentorPromptHinglish = "Professional Dashboard check karo. Yahan Reached Accounts, Active Followers aur Top Cities ka breakdown milta hai.",
        smartQuestionHi = "Dashboard me insights dikh rahe hain?",
        smartQuestionEn = "Can you see Insights in Dashboard?",
        smartQuestionHinglish = "Dashboard me insights dikh rahe hain?"
    ),
    MentorStep(
        id = 6,
        title = "6. Content Formats Masterclass",
        shortDesc = "Reels, Stories, Highlights & Trial Reels",
        mentorPromptHi = "Reels = Growth & Reach. Stories = Trust & Connection. Highlights = Permanent Portfolio. Trial Reels = Non-follower test playground!",
        mentorPromptEn = "Reels bring new audience. Stories build trust. Highlights serve as permanent showcase. Trial Reels test content with non-followers!",
        mentorPromptHinglish = "Reels se new audience aati hai. Stories se trust banta hai. Highlights portfolio hain. Trial Reels se non-followers test hote hain!",
        smartQuestionHi = "Sabhi formats samajh aa gaye?",
        smartQuestionEn = "Understood all content formats?",
        smartQuestionHinglish = "Content formats clear hain?"
    ),
    MentorStep(
        id = 7,
        title = "7. Reels Algorithm & Viral Hook Formulas",
        shortDesc = "First 3-seconds watch time engine",
        mentorPromptHi = "Instagram pehle 3 seconds me watch time dekhta hai. Video start karo strong Visual Hook ya Question se (e.g. 'Rukno! Ye secret galti mat karo...').",
        mentorPromptEn = "Instagram measures 3-second retention. Start every video with a punchy Visual or Verbal Hook (e.g., 'Stop! Don't make this mistake...').",
        mentorPromptHinglish = "Reel me pehle 3 seconds sabse main hain! Pehle 3 second me strong Visual or Text Hook do jisse viewer ruk jaye.",
        smartQuestionHi = "3-second Hook formula clear hai?",
        smartQuestionEn = "Got the 3-second Hook formula?",
        smartQuestionHinglish = "Hook formula samajh aa gaya?"
    ),
    MentorStep(
        id = 8,
        title = "8. Trending Audio & Voice Balancing",
        shortDesc = "Background music vs clear voiceover",
        mentorPromptHi = "Trending Audio ka arrow ↗️ dekho. Volume Mixing: Voiceover 100%, Trending Audio 10-15% background me. Voice crystal clear honi chahiye!",
        mentorPromptEn = "Look for the trending arrow ↗️. Audio Balance: Voiceover at 100%, Background Trending Music at 10-15%. Keep voice crisp!",
        mentorPromptHinglish = "Trending Audio sign ↗️ search karo. Audio Mix: Apni Voice 100% aur Trending Sound 10-15% background me rakho.",
        smartQuestionHi = "Audio volume balance samajh aaya?",
        smartQuestionEn = "Clear on audio volume mixing?",
        smartQuestionHinglish = "Audio mixing clear hai?"
    ),
    MentorStep(
        id = 9,
        title = "9. AI Script Generator",
        shortDesc = "Generate high-converting Reel scripts",
        mentorPromptHi = "Ab chalo pehli viral Reel ka script banate hain! Niche Script Generator button par tap karo ya Topic aur Duration batao.",
        mentorPromptEn = "Let's write your first viral Reel script! Use the Script Generator tool below or tell me your topic and length.",
        mentorPromptHinglish = "Chalo ab ek mast Reel script banate hain! Niche Script Generator button tap karo ya topic aur duration batao.",
        smartQuestionHi = "Script ready karein?",
        smartQuestionEn = "Ready to generate script?",
        smartQuestionHinglish = "Script generate karein?"
    ),
    MentorStep(
        id = 10,
        title = "10. Video Editing Skills Check",
        shortDesc = "Editing app workflow & tools",
        mentorPromptHi = "Kya aapko video editing (VN, CapCut, Premiere) aati hai?",
        mentorPromptEn = "Do you know video editing using tools like VN, CapCut, or Premiere?",
        mentorPromptHinglish = "Kya aapko video editing (VN, CapCut, Premiere) aati hai?",
        smartQuestionHi = "Editing aati hai?",
        smartQuestionEn = "Do you know video editing?",
        smartQuestionHinglish = "Editing aati hai?"
    ),
    MentorStep(
        id = 11,
        title = "11. AI Caption & 3-Tier Hashtags",
        shortDesc = "SEO Keywords + Low/Med/High Hashtag Mix",
        mentorPromptHi = "SEO Captions search results me rank karate hain. Hashtags Mix: 3 Niche Specific + 3 Medium Competition + 2 High Reach.",
        mentorPromptEn = "SEO Captions help rank in search. Use 3-Tier Hashtags: 3 Niche Specific + 3 Medium + 2 Broad tags.",
        mentorPromptHinglish = "SEO Captions Search engine me rank karate hain. Hashtag Mix: 3 Niche + 3 Medium + 2 High Reach tags.",
        smartQuestionHi = "Caption aur Hashtags chahiye?",
        smartQuestionEn = "Need Caption & Hashtag generator?",
        smartQuestionHinglish = "Caption & Hashtag ready karein?"
    ),
    MentorStep(
        id = 12,
        title = "12. Pre-Upload 9-Point Checklist",
        shortDesc = "Final verification before tapping Share",
        mentorPromptHi = "Upload se pehle: ✔️ HD Cover, ✔️ Hook Caption, ✔️ Audio Mix, ✔️ Hashtags, ✔️ Location, ✔️ Alt Text Check kar lo!",
        mentorPromptEn = "Before publishing: ✔️ Clear Cover, ✔️ Hook Caption, ✔️ Audio Balance, ✔️ Hashtags, ✔️ Location, ✔️ Alt Text!",
        mentorPromptHinglish = "Posting se pehle: ✔️ Cover Photo, ✔️ Captions, ✔️ Audio Mix, ✔️ Hashtags, ✔️ Location check kar lo!",
        smartQuestionHi = "Checklist poori ho gayi?",
        smartQuestionEn = "Checklist verified?",
        smartQuestionHinglish = "Checklist done?"
    ),
    MentorStep(
        id = 13,
        title = "13. Post-Upload Growth & Analytics",
        shortDesc = "Reply strategy, pinned comments & 24h analysis",
        mentorPromptHi = "Reel post hone ke pehle 1 ghante me aane wale har comment par reply karo aur pin karo. 24h baad Insights check karke watch time analyze karo!",
        mentorPromptEn = "In the first hour after posting, reply to all comments immediately and pin top comments. Analyze 24h watch time in Insights!",
        mentorPromptHinglish = "Post karte hi pehle 1 hour me sabhi comments ka reply aur pin karo. 24h baad Insights analyze karke next Reel planner banao!",
        smartQuestionHi = "Aap Zero to Hero Creator ready ho! 🚀",
        smartQuestionEn = "You are now a Zero to Hero Creator! 🚀",
        smartQuestionHinglish = "Congratulations! Aap Zero to Hero Creator ban gaye! 🚀"
    )
)

@Composable
fun InstagramCreatorAiV2Dialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current

    // Session Persistence
    var savedLangCode by remember {
        mutableStateOf(CreatorAcademyPrefs.getBrandCollabLanguage(context).ifBlank { "" })
    }
    var currentLang by remember {
        mutableStateOf(
            when (savedLangCode) {
                "HI" -> MentorLanguage.HINDI
                "EN" -> MentorLanguage.ENGLISH
                else -> MentorLanguage.HINGLISH
            }
        )
    }

    var selectedCreatorType by remember { mutableStateOf<String?>(null) }
    var currentStepIndex by remember { mutableIntStateOf(0) }
    var completedSteps by remember { mutableStateOf(setOf<Int>()) }

    var showLanguagePicker by remember { mutableStateOf(savedLangCode.isBlank()) }
    var showCreatorTypePicker by remember { mutableStateOf(!showLanguagePicker && selectedCreatorType == null) }

    var showWelcomeBack by remember { mutableStateOf(!showLanguagePicker && selectedCreatorType != null && (currentStepIndex > 0 || completedSteps.isNotEmpty())) }
    var showRestartConfirm by remember { mutableStateOf(false) }

    // Chat Feed State
    val chatMessages = remember { mutableStateListOf<ChatMessage>() }
    var userTextInput by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }
    var reExplainCount by remember { mutableIntStateOf(0) }

    // Interactive Dialog Overlays
    var showScriptGeneratorSheet by remember { mutableStateOf(false) }
    var showEditingCourseSheet by remember { mutableStateOf(false) }
    var showCaptionGeneratorSheet by remember { mutableStateOf(false) }
    var showChecklistSheet by remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()

    // Helper: Push AI Mentor Message
    fun addMentorMessage(
        text: String,
        isFresh: Boolean = false,
        script: ScriptResult? = null,
        caption: CaptionResult? = null
    ) {
        chatMessages.add(
            ChatMessage(
                sender = "AI_MENTOR",
                text = text,
                stepIndex = currentStepIndex,
                isFreshExplanation = isFresh,
                scriptData = script,
                captionData = caption
            )
        )
        coroutineScope.launch {
            delay(100)
            if (chatMessages.isNotEmpty()) {
                lazyListState.animateScrollToItem(chatMessages.size - 1)
            }
        }
    }

    // Initialize Mentor Welcome
    fun initMentorForStep(stepIdx: Int, isFreshExplanation: Boolean = false) {
        val step = INSTAGRAM_ROADMAP_STEPS.getOrNull(stepIdx) ?: return
        val mentorMsg = if (isFreshExplanation) {
            com.example.creatoracademy.AiTeachingVariationsEngine.getMultiStyleExplanation(
                stepTitle = step.title,
                coreConcept = step.mentorPromptEn,
                lang = currentLang.name,
                variationCount = reExplainCount
            )
        } else {
            when (currentLang) {
                MentorLanguage.HINDI -> step.mentorPromptHi
                MentorLanguage.ENGLISH -> step.mentorPromptEn
                MentorLanguage.HINGLISH -> step.mentorPromptHinglish
            }
        }
        addMentorMessage(text = mentorMsg, isFresh = isFreshExplanation)
    }

    // Handle User Action
    fun handleUserConfirmation(userText: String, isDone: Boolean = true) {
        chatMessages.add(
            ChatMessage(
                sender = "CREATOR",
                text = userText,
                stepIndex = currentStepIndex
            )
        )

        if (isDone) {
            completedSteps = completedSteps + currentStepIndex
            if (currentStepIndex < INSTAGRAM_ROADMAP_STEPS.size - 1) {
                currentStepIndex += 1
                coroutineScope.launch {
                    isThinking = true
                    delay(500)
                    isThinking = false
                    val stepObj = INSTAGRAM_ROADMAP_STEPS[currentStepIndex]
                    val introText = when (currentLang) {
                        MentorLanguage.HINDI -> "Bahut badiya! 🎉 Chalo STEP ${stepObj.id} par chalte hain: ${stepObj.title}"
                        MentorLanguage.ENGLISH -> "Awesome! 🎉 Moving to STEP ${stepObj.id}: ${stepObj.title}"
                        MentorLanguage.HINGLISH -> "Chalo mast! 😄 Ab aagaye STEP ${stepObj.id}: ${stepObj.title}"
                    }
                    addMentorMessage(introText)
                    delay(300)
                    initMentorForStep(currentStepIndex)
                }
            } else {
                addMentorMessage(
                    when (currentLang) {
                        MentorLanguage.HINDI -> "🎉 Badhai ho! Aapne poora Instagram Zero to Hero course complete kar liya hai!"
                        MentorLanguage.ENGLISH -> "🎉 Congratulations! You have completed the full Instagram Zero to Hero course!"
                        MentorLanguage.HINGLISH -> "🎉 Badhai ho! Aapne saare steps complete karke Instagram Zero to Hero path master kar liya!"
                    }
                )
            }
        }
    }

    // Handle "Explain Again" request
    fun handleExplainAgain() {
        reExplainCount += 1
        chatMessages.add(
            ChatMessage(
                sender = "CREATOR",
                text = when (currentLang) {
                    MentorLanguage.HINDI -> "❓ Mujhe samajh nahi aaya. Dubara samjhao."
                    MentorLanguage.ENGLISH -> "❓ I didn't understand. Please explain again with another style."
                    MentorLanguage.HINGLISH -> "❓ Samajh nahi aaya, please ek aur tareeke se batao."
                },
                stepIndex = currentStepIndex
            )
        )
        coroutineScope.launch {
            isThinking = true
            delay(500)
            isThinking = false
            initMentorForStep(currentStepIndex, isFreshExplanation = true)
        }
    }

    val imeBottomPadding = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
    LaunchedEffect(chatMessages.size, isThinking, userTextInput, imeBottomPadding) {
        if (chatMessages.isNotEmpty()) {
            delay(60)
            lazyListState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AmoledBlack)
                .statusBarsPadding()
                .responsiveImeAndNavPadding()
        ) {
            Surface(
                color = Color(0xFF0F1A14),
                modifier = Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // TOP NAVBAR
                    TopNavBar(
                        selectedLanguage = currentLang,
                        selectedCreatorType = selectedCreatorType,
                        currentStepIndex = currentStepIndex,
                        totalSteps = INSTAGRAM_ROADMAP_STEPS.size,
                        onChangeLanguage = { showLanguagePicker = true },
                        onChangeCreatorType = { showCreatorTypePicker = true },
                        onClose = onDismiss
                    )

                    // STEP PROGRESS HORIZONTAL BAR
                    if (!showLanguagePicker && !showCreatorTypePicker) {
                        StepProgressBar(
                            steps = INSTAGRAM_ROADMAP_STEPS,
                            currentStepIndex = currentStepIndex,
                            completedSteps = completedSteps,
                            onStepClick = { stepIdx ->
                                if (stepIdx <= currentStepIndex || completedSteps.contains(stepIdx)) {
                                    currentStepIndex = stepIdx
                                    initMentorForStep(stepIdx)
                                } else {
                                    Toast.makeText(context, "🔒 Complete current step first!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }

                    // MAIN CONTENT AREA
                    Box(modifier = Modifier.weight(1f)) {
                        when {
                            showWelcomeBack -> {
                                SmartWelcomeBackDialog(
                                    courseTitle = "Instagram Creator Course",
                                    currentStep = currentStepIndex + 1,
                                    totalSteps = INSTAGRAM_ROADMAP_STEPS.size,
                                    onContinue = { showWelcomeBack = false },
                                    onRestart = { showRestartConfirm = true },
                                    onDismiss = onDismiss
                                )
                            }
                            showLanguagePicker -> {
                                LanguageSelectionOverlay(
                                    selectedLang = currentLang,
                                    onLanguageSelected = { lang ->
                                        currentLang = lang
                                        savedLangCode = lang.code
                                        CreatorAcademyPrefs.setBrandCollabLanguage(context, lang.code)
                                        showLanguagePicker = false
                                        if (selectedCreatorType == null) {
                                            showCreatorTypePicker = true
                                        } else if (chatMessages.isEmpty()) {
                                            initMentorForStep(0)
                                        }
                                    }
                                )
                            }
                            showCreatorTypePicker -> {
                                CreatorTypeSelectionOverlay(
                                    selectedType = selectedCreatorType,
                                    language = currentLang,
                                    onTypeSelected = { type ->
                                        selectedCreatorType = type
                                        showCreatorTypePicker = false
                                        if (chatMessages.isEmpty()) {
                                            val welcomeText = when (currentLang) {
                                                MentorLanguage.HINDI -> "Wah! $type ke roop me aapka Instagram safar shuru hota hai! 🚀"
                                                MentorLanguage.ENGLISH -> "Awesome! Starting your Instagram journey as a $type! 🚀"
                                                MentorLanguage.HINGLISH -> "Mast choice! $type ke liye personalized Instagram roadmap ready hai! 🚀"
                                            }
                                            addMentorMessage(welcomeText)
                                            initMentorForStep(0)
                                        }
                                    }
                                )
                            }
                            else -> {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    // LEARNING PROGRESS CARD
                                    LearningProgressIndicatorCard(
                                        currentStep = currentStepIndex + 1,
                                        totalSteps = INSTAGRAM_ROADMAP_STEPS.size,
                                        stepTitle = INSTAGRAM_ROADMAP_STEPS.getOrNull(currentStepIndex)?.title ?: "Lesson ${currentStepIndex + 1}",
                                        onResetClick = { showRestartConfirm = true },
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                                    )

                                    // CHAT FEED
                                    LazyColumn(
                                        state = lazyListState,
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        item { Spacer(modifier = Modifier.height(10.dp)) }

                                        itemsIndexed(chatMessages) { idx, msg ->
                                            ChatMessageBubble(
                                                message = msg,
                                                onCopyText = { txt ->
                                                    clipboardManager.setText(AnnotatedString(txt))
                                                    Toast.makeText(context, "Copied to Clipboard! 📋", Toast.LENGTH_SHORT).show()
                                                }
                                            )
                                        }

                                        if (isThinking) {
                                            item {
                                                AiThinkingIndicator()
                                            }
                                        }

                                        if (completedSteps.size >= INSTAGRAM_ROADMAP_STEPS.size) {
                                            item {
                                                CourseCompletionCard(
                                                    courseTitle = "Instagram Zero to Hero Course",
                                                    skillsLearned = listOf(
                                                        "Viral Reel Hooks & Scripting",
                                                        "Hashtag & SEO Strategy",
                                                        "High-CTR Thumbnail Design",
                                                        "Audience Engagement & Monetization"
                                                    ),
                                                    onContinue = onDismiss,
                                                    onResetCourse = { showRestartConfirm = true },
                                                    theme = MentorToolTheme.InstagramCreator
                                                )
                                            }
                                        }

                                        item { Spacer(modifier = Modifier.height(16.dp)) }
                                    }

                                    // QUICK ACTIONS BAR & INPUT
                                    MentorActionBar(
                                        currentLang = currentLang,
                                        currentStepIndex = currentStepIndex,
                                        userTextInput = userTextInput,
                                        onUserTextInputChange = { userTextInput = it },
                                        onConfirmDone = {
                                            val label = when (currentLang) {
                                                MentorLanguage.HINDI -> "✅ Ho gaya! Agla step batao."
                                                MentorLanguage.ENGLISH -> "✅ Done! Let's move to the next step."
                                                MentorLanguage.HINGLISH -> "✅ Ho gaya! Next step unlock karo."
                                            }
                                            handleUserConfirmation(label, isDone = true)
                                        },
                                        onExplainAgain = { handleExplainAgain() },
                                        onOpenScriptTool = { showScriptGeneratorSheet = true },
                                        onOpenCaptionTool = { showCaptionGeneratorSheet = true },
                                        onOpenChecklistTool = { showChecklistSheet = true },
                                        onOpenEditingCourse = { showEditingCourseSheet = true },
                                        onSendMessage = { txt ->
                                            if (txt.isNotBlank()) {
                                                val lower = txt.lowercase()
                                                val isExplainReq = lower.contains("nahi samjha") || lower.contains("dubara") || lower.contains("explain") || lower.contains("confused")
                                                val isDoneReq = lower.contains("done") || lower.contains("ho gaya") || lower.contains("samajh aa gaya") || lower.contains("ok")

                                                userTextInput = ""
                                                if (isExplainReq) {
                                                    handleExplainAgain()
                                                } else if (isDoneReq) {
                                                    handleUserConfirmation(txt, isDone = true)
                                                } else {
                                                    chatMessages.add(ChatMessage(sender = "CREATOR", text = txt, stepIndex = currentStepIndex))
                                                    coroutineScope.launch {
                                                        isThinking = true
                                                        delay(500)
                                                        isThinking = false
                                                        val reply = generateDynamicAiReply(txt, currentLang, currentStepIndex)
                                                        addMentorMessage(reply)
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // SCRIPT GENERATOR SHEET OVERLAY
                if (showScriptGeneratorSheet) {
                    ScriptGeneratorDialog(
                        creatorType = selectedCreatorType ?: "Creator",
                        language = currentLang,
                        onDismiss = { showScriptGeneratorSheet = false },
                        onScriptGenerated = { script ->
                            showScriptGeneratorSheet = false
                            val scriptText = "🎬 AI SCRIPT GENERATED (${script.duration})\n\nTopic: ${script.topic}\n\n🪝 HOOK:\n\"${script.hook}\"\n\n⚡ BODY/FLOW:\n${script.flow}\n\n🚀 CALL TO ACTION:\n\"${script.cta}\""
                            addMentorMessage(text = scriptText, script = script)
                        }
                    )
                }

                // EDITING COURSE OVERLAY SHEET
                if (showEditingCourseSheet) {
                    EditingCourseDialog(
                        language = currentLang,
                        onDismiss = { showEditingCourseSheet = false }
                    )
                }

                // CAPTION GENERATOR SHEET OVERLAY
                if (showCaptionGeneratorSheet) {
                    CaptionGeneratorDialog(
                        creatorType = selectedCreatorType ?: "Creator",
                        language = currentLang,
                        onDismiss = { showCaptionGeneratorSheet = false },
                        onCaptionGenerated = { result ->
                            showCaptionGeneratorSheet = false
                            val capText = "✍️ AI CAPTION & HASHTAGS\n\n${result.caption}\n\n🏷️ HASHTAGS:\n${result.hashtags.joinToString(" ")}"
                            addMentorMessage(text = capText, caption = result)
                        }
                    )
                }

                // PRE-POST CHECKLIST SHEET OVERLAY
                if (showChecklistSheet) {
                    PrePostChecklistDialog(
                        language = currentLang,
                        onDismiss = { showChecklistSheet = false }
                    )
                }

                if (showRestartConfirm) {
                    RestartCourseConfirmDialog(
                        courseTitle = "Instagram Creator Course",
                        onConfirmRestart = {
                            CreatorAcademyPrefs.resetCourseProgress(context, "instagram")
                            savedLangCode = ""
                            currentLang = MentorLanguage.HINGLISH
                            selectedCreatorType = null
                            currentStepIndex = 0
                            completedSteps = emptySet()
                            chatMessages.clear()
                            showLanguagePicker = true
                            showCreatorTypePicker = false
                            showWelcomeBack = false
                            showRestartConfirm = false
                        },
                        onDismiss = { showRestartConfirm = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun TopNavBar(
    selectedLanguage: MentorLanguage,
    selectedCreatorType: String?,
    currentStepIndex: Int,
    totalSteps: Int,
    onChangeLanguage: () -> Unit,
    onChangeCreatorType: () -> Unit,
    onClose: () -> Unit
) {
    Surface(
        color = Color(0xFF101913),
        border = BorderStroke(0.8.dp, Color(0x2210B981))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(EmeraldPrimary.copy(alpha = 0.2f))
                        .border(BorderStroke(1.dp, EmeraldGlow), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    OfficialLogo(name = "instagram", modifier = Modifier.size(20.dp))
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Instagram Mentor AI",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(EmeraldPrimary)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "V2 PRO",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Black,
                                color = AmoledBlack
                            )
                        }
                    }
                    Text(
                        text = "Zero to Hero Personal Mentor",
                        fontSize = 10.5.sp,
                        color = EmeraldGlow,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Language Switch Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x22FFFFFF))
                        .border(BorderStroke(0.8.dp, Color(0x33FFFFFF)), RoundedCornerShape(12.dp))
                        .clickable { onChangeLanguage() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Lang",
                            tint = EmeraldGlow,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = selectedLanguage.code,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }

                // Creator Type Switch Pill
                selectedCreatorType?.let { type ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x22FFFFFF))
                            .border(BorderStroke(0.8.dp, Color(0x33FFFFFF)), RoundedCornerShape(12.dp))
                            .clickable { onChangeCreatorType() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = type,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            maxLines = 1
                        )
                    }
                }

                // Close Button
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                        .clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StepProgressBar(
    steps: List<MentorStep>,
    currentStepIndex: Int,
    completedSteps: Set<Int>,
    onStepClick: (Int) -> Unit
) {
    Surface(
        color = Color(0xFF0B140E),
        border = BorderStroke(0.5.dp, Color(0x2210B981))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ROADMAP PROGRESS",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGlow,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Step ${currentStepIndex + 1} of ${steps.size}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
            ) {
                itemsIndexed(steps) { idx, step ->
                    val isCurrent = idx == currentStepIndex
                    val isDone = completedSteps.contains(idx)

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                when {
                                    isCurrent -> Brush.horizontalGradient(listOf(EmeraldPrimary, EmeraldGlow))
                                    isDone -> SolidColor(Color(0x3310B981))
                                    else -> SolidColor(Color(0x18FFFFFF))
                                }
                            )
                            .border(
                                BorderStroke(
                                    1.dp,
                                    when {
                                        isCurrent -> EmeraldGlow
                                        isDone -> EmeraldPrimary.copy(alpha = 0.5f)
                                        else -> Color(0x22FFFFFF)
                                    }
                                ),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { onStepClick(idx) }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isDone) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Done",
                                    tint = if (isCurrent) AmoledBlack else EmeraldGlow,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            } else if (!isCurrent && idx > currentStepIndex) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    tint = TextWhite.copy(alpha = 0.4f),
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = "S${step.id}",
                                fontSize = 10.sp,
                                fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold,
                                color = if (isCurrent) AmoledBlack else TextWhite.copy(alpha = if (isDone) 0.9f else 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LanguageSelectionOverlay(
    selectedLang: MentorLanguage,
    onLanguageSelected: (MentorLanguage) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF121B15),
            border = BorderStroke(1.2.dp, Brush.linearGradient(listOf(EmeraldPrimary, EmeraldGlow))),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "👋 Welcome Creator!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Aap kis language me seekhna chahenge?",
                    fontSize = 13.5.sp,
                    color = EmeraldGlow,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                MentorLanguage.values().forEach { lang ->
                    val isSelected = selectedLang == lang
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) Brush.horizontalGradient(listOf(EmeraldPrimary, EmeraldGlow))
                                else SolidColor(Color(0x18FFFFFF))
                            )
                            .border(
                                BorderStroke(
                                    1.dp,
                                    if (isSelected) EmeraldGlow else Color(0x22FFFFFF)
                                ),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onLanguageSelected(lang) }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "○  ${lang.label} (${lang.nativeName})",
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                color = if (isSelected) AmoledBlack else TextWhite
                            )

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = AmoledBlack,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun CreatorTypeSelectionOverlay(
    selectedType: String?,
    language: MentorLanguage,
    onTypeSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF121B15),
            border = BorderStroke(1.2.dp, Brush.linearGradient(listOf(EmeraldPrimary, EmeraldGlow))),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = when (language) {
                        MentorLanguage.HINDI -> "Aap kya banna chahte ho?"
                        MentorLanguage.ENGLISH -> "What type of Creator do you want to be?"
                        MentorLanguage.HINGLISH -> "Aap kya banna chahte ho?"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Personalized Roadmap for your Creator Niche",
                    fontSize = 11.5.sp,
                    color = EmeraldGlow,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(CREATOR_TYPES) { type ->
                        val isSelected = selectedType == type
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isSelected) Brush.horizontalGradient(listOf(EmeraldPrimary, EmeraldGlow))
                                    else SolidColor(Color(0x18FFFFFF))
                                )
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        if (isSelected) EmeraldGlow else Color(0x22FFFFFF)
                                    ),
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { onTypeSelected(type) }
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🎯 $type",
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
                                    color = if (isSelected) AmoledBlack else TextWhite
                                )

                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Select",
                                    tint = if (isSelected) AmoledBlack else Color.White.copy(alpha = 0.5f),
                                    modifier = Modifier.size(16.dp)
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
private fun ChatMessageBubble(
    message: ChatMessage,
    onCopyText: (String) -> Unit
) {
    val isMentor = message.sender == "AI_MENTOR"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMentor) Arrangement.Start else Arrangement.End
    ) {
        if (isMentor) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(EmeraldPrimary.copy(alpha = 0.2f))
                    .border(BorderStroke(1.dp, EmeraldGlow), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                OfficialLogo(name = "instagram", modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (isMentor) 4.dp else 18.dp,
                bottomEnd = if (isMentor) 18.dp else 4.dp
            ),
            color = if (isMentor) Color(0xFF131D16) else EmeraldPrimary,
            border = if (isMentor) BorderStroke(1.dp, Color(0x3310B981)) else null,
            modifier = Modifier.widthIn(max = 290.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                if (isMentor && message.isFreshExplanation) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0x3310B981))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "💡 FRESH EXPLANATION",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Black,
                            color = EmeraldGlow
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                Text(
                    text = message.text,
                    fontSize = 13.5.sp,
                    color = if (isMentor) TextWhite else AmoledBlack,
                    lineHeight = 19.sp,
                    fontWeight = if (isMentor) FontWeight.Normal else FontWeight.Bold
                )

                if (isMentor) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onCopyText(message.text) }
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = EmeraldGlow,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Copy",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldGlow
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
private fun MentorActionBar(
    currentLang: MentorLanguage,
    currentStepIndex: Int,
    userTextInput: String,
    onUserTextInputChange: (String) -> Unit,
    onConfirmDone: () -> Unit,
    onExplainAgain: () -> Unit,
    onOpenScriptTool: () -> Unit,
    onOpenCaptionTool: () -> Unit,
    onOpenChecklistTool: () -> Unit,
    onOpenEditingCourse: () -> Unit,
    onSendMessage: (String) -> Unit
) {
    Surface(
        color = Color(0xFF0D1610),
        border = BorderStroke(0.8.dp, Color(0x3310B981))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // QUICK ACTION CHIPS ROW
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    ActionPill(
                        label = when (currentLang) {
                            MentorLanguage.HINDI -> "✅ Ho gaya (Next Step)"
                            MentorLanguage.ENGLISH -> "✅ Done (Next Step)"
                            MentorLanguage.HINGLISH -> "✅ Ho Gaya (Next Step)"
                        },
                        isPrimary = true,
                        onClick = onConfirmDone
                    )
                }

                item {
                    ActionPill(
                        label = when (currentLang) {
                            MentorLanguage.HINDI -> "❓ Samajh nahi aaya (Dubara batao)"
                            MentorLanguage.ENGLISH -> "❓ Explain Again (New Example)"
                            MentorLanguage.HINGLISH -> "❓ Samajh Nahi Aaya (Fresh Example)"
                        },
                        isPrimary = false,
                        onClick = onExplainAgain
                    )
                }

                if (currentStepIndex == 8 || currentStepIndex == 6) {
                    item {
                        ActionPill(
                            label = "🎬 AI Script Generator",
                            isPrimary = false,
                            onClick = onOpenScriptTool
                        )
                    }
                }

                if (currentStepIndex == 9) {
                    item {
                        ActionPill(
                            label = "🎥 Video Editing Course",
                            isPrimary = false,
                            onClick = onOpenEditingCourse
                        )
                    }
                }

                if (currentStepIndex == 10) {
                    item {
                        ActionPill(
                            label = "✍️ Caption & Hashtags",
                            isPrimary = false,
                            onClick = onOpenCaptionTool
                        )
                    }
                }

                if (currentStepIndex == 11) {
                    item {
                        ActionPill(
                            label = "📋 Pre-Post Checklist",
                            isPrimary = false,
                            onClick = onOpenChecklistTool
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // TEXT INPUT BAR
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = userTextInput,
                    onValueChange = onUserTextInputChange,
                    placeholder = {
                        Text(
                            text = when (currentLang) {
                                MentorLanguage.HINDI -> "Apna question ya 'Ho gaya' likhein..."
                                MentorLanguage.ENGLISH -> "Ask your mentor or type 'Done'..."
                                MentorLanguage.HINGLISH -> "Apna doubt likhein ya 'Ho gaya' bolein..."
                            },
                            fontSize = 12.sp,
                            color = TextWhite.copy(alpha = 0.4f)
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldGlow,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedContainerColor = Color(0x18FFFFFF),
                        unfocusedContainerColor = Color(0x12FFFFFF),
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Brush.horizontalGradient(listOf(EmeraldPrimary, EmeraldGlow)))
                        .clickable { onSendMessage(userTextInput) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = AmoledBlack,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionPill(
    label: String,
    isPrimary: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isPrimary) Brush.horizontalGradient(listOf(EmeraldPrimary, EmeraldGlow))
                else SolidColor(Color(0x2210B981))
            )
            .border(
                BorderStroke(
                    1.dp,
                    if (isPrimary) EmeraldGlow else EmeraldPrimary.copy(alpha = 0.5f)
                ),
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (isPrimary) AmoledBlack else TextWhite
        )
    }
}

@Composable
private fun AiThinkingIndicator() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = EmeraldGlow,
                modifier = Modifier.size(14.dp)
            )
        }
        Text(
            text = "AI Mentor is typing...",
            fontSize = 12.sp,
            color = EmeraldGlow,
            fontWeight = FontWeight.Medium
        )
    }
}

// =========================================================
// INTERACTIVE DIALOG OVERLAYS
// =========================================================

@Composable
private fun ScriptGeneratorDialog(
    creatorType: String,
    language: MentorLanguage,
    onDismiss: () -> Unit,
    onScriptGenerated: (ScriptResult) -> Unit
) {
    var topic by remember { mutableStateOf("") }
    var selectedDuration by remember { mutableStateOf("30s") }
    var isGenerating by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF121B15),
            border = BorderStroke(1.2.dp, Brush.linearGradient(listOf(EmeraldPrimary, EmeraldGlow))),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎬 AI Reel Script Generator",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Personalized for $creatorType",
                    fontSize = 11.sp,
                    color = EmeraldGlow,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    placeholder = { Text("Video topic (e.g., Top 3 AI Hacks, Daily Vlog)...", fontSize = 12.sp, color = TextWhite.copy(0.4f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldGlow,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Select Duration:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("15s", "30s", "45s", "60s", "90s").forEach { dur ->
                        val isSel = selectedDuration == dur
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSel) Brush.horizontalGradient(listOf(EmeraldPrimary, EmeraldGlow))
                                    else SolidColor(Color(0x18FFFFFF))
                                )
                                .clickable { selectedDuration = dur },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dur,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) AmoledBlack else TextWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .clip(RoundedCornerShape(23.dp))
                        .background(
                            if (topic.isNotBlank()) Brush.horizontalGradient(listOf(EmeraldPrimary, EmeraldGlow))
                            else SolidColor(Color.Gray)
                        )
                        .clickable(enabled = topic.isNotBlank() && !isGenerating) {
                            isGenerating = true
                            val script = generateReelScript(topic, selectedDuration, creatorType, language)
                            onScriptGenerated(script)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isGenerating) "GENERATING..." else "GENERATE SCRIPT ✨",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = AmoledBlack,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun EditingCourseDialog(
    language: MentorLanguage,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF121B15),
            border = BorderStroke(1.2.dp, Brush.linearGradient(listOf(EmeraldPrimary, EmeraldGlow))),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎥 Video Editing Crash Course",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(12.dp))

                val lessons = listOf(
                    "1. VN Code / CapCut Basics (Trim, Split & Cut)",
                    "2. Auto-Captions & Animated Text Presets",
                    "3. Sound Effects (Whoosh, Pop, Click)",
                    "4. Color Grading & Lighting Enhancers",
                    "5. Exporting in 1080p 60FPS Bitrate Settings"
                )

                lessons.forEach { lesson ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x18FFFFFF))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = lesson,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(21.dp))
                        .background(Brush.horizontalGradient(listOf(EmeraldPrimary, EmeraldGlow)))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "GOT IT • CONTINUE LESSON",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = AmoledBlack
                    )
                }
            }
        }
    }
}

@Composable
private fun CaptionGeneratorDialog(
    creatorType: String,
    language: MentorLanguage,
    onDismiss: () -> Unit,
    onCaptionGenerated: (CaptionResult) -> Unit
) {
    var topic by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF121B15),
            border = BorderStroke(1.2.dp, Brush.linearGradient(listOf(EmeraldPrimary, EmeraldGlow))),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "✍️ AI Caption & Hashtag Generator",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    placeholder = { Text("Enter Reel topic for Caption & Hashtags...", fontSize = 12.sp, color = TextWhite.copy(0.4f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldGlow,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            if (topic.isNotBlank()) Brush.horizontalGradient(listOf(EmeraldPrimary, EmeraldGlow))
                            else SolidColor(Color.Gray)
                        )
                        .clickable(enabled = topic.isNotBlank()) {
                            val cap = generateCaptionAndHashtags(topic, creatorType, language)
                            onCaptionGenerated(cap)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "GENERATE CAPTION & HASHTAGS ✨",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = AmoledBlack
                    )
                }
            }
        }
    }
}

@Composable
private fun PrePostChecklistDialog(
    language: MentorLanguage,
    onDismiss: () -> Unit
) {
    val items = remember {
        mutableStateListOf(
            "Clear HD Cover Photo / Thumbnail selected" to false,
            "Highest Quality Upload toggled ON in Settings" to false,
            "Hook sentence in First Line of Caption" to false,
            "3-Tier Hashtag Mix added" to false,
            "Location tagged for local reach" to false,
            "Trending Audio Volume mixed (10-15%)" to false,
            "Alt Text & Product Tags added" to false
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF121B15),
            border = BorderStroke(1.2.dp, Brush.linearGradient(listOf(EmeraldPrimary, EmeraldGlow))),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📋 Pre-Post Checklist",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(12.dp))

                items.forEachIndexed { idx, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { items[idx] = item.first to !item.second }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = item.second,
                            onCheckedChange = { chk -> items[idx] = item.first to chk },
                            colors = CheckboxDefaults.colors(
                                checkedColor = EmeraldGlow,
                                checkmarkColor = AmoledBlack
                            )
                        )
                        Text(
                            text = item.first,
                            fontSize = 12.sp,
                            color = if (item.second) EmeraldGlow else TextWhite,
                            fontWeight = if (item.second) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(21.dp))
                        .background(Brush.horizontalGradient(listOf(EmeraldPrimary, EmeraldGlow)))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "READY TO POST 🚀",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = AmoledBlack
                    )
                }
            }
        }
    }
}

// =========================================================
// GENERATION HELPERS (DYNAMIC & FRESH EXPLANATION GENERATORS)
// =========================================================

private fun getFreshExplanationHi(stepIdx: Int): String {
    return when (stepIdx) {
        0 -> "💡 Aao ise aur aasan sabdo me samjhte hain: Instagram bilkul aapki dukan ki tarah hai. App download karna matlab dukan ki chabi lena! Pehle app install karke basic profile kholo."
        1 -> "💡 Simpler Example: Normal Instagram account personal diary jaisa hota hai. Switch to Creator/Professional matlab dukan ka board lagana, jisse aapko kitne grahak (viewers) aaye wo count dikhe!"
        2 -> "💡 Profile Bio Example: Maan lo aap Restaurant ke bahar ho. Board pe likha hai 'Best Butter Chicken & Fast Delivery'. Viewer aapka bio dekh ke 2 second me samajhna chahiye ki aap kya dikhate ho."
        3 -> "💡 Quality Settings Example: Dhundli (blurry) video koi nahi dekhta. Instagram ka algorithm blurry video ki reach rok deta hai. Highest Quality toggle karne se video crystal clear HD render hoti hai."
        4 -> "💡 Insights Example: Dashboard ek Report Card ki tarah hai. Isse pata chalta hai ki kis time aapke viewers online hain aur kis video pe sabse zyada log ruke."
        5 -> "💡 Content Formats Analogy: Reel = Advertisement poster (naye logon ko lane ke liye). Story = Dost se baatcheet (jo pehle se followers hain unko active rakhne ke liye)."
        6 -> "💡 Hook Example: Agar aapki Reel 3 second me kisi ko pasand nahi aayi toh wo swipe kar dega. Pehle 3 second me screen pe bada text likho ya koi energetic sawaal puchho!"
        7 -> "💡 Audio Balance Example: Apni aawaz ko 100% rakho aur Background music ko 10%. Jaise FM Radio pe RJ ki aawaz clear aati hai aur music peeche dheere bajta hai!"
        8 -> "💡 Scripting Formula: Video me 3 parts hote hain: 1. Hook (0-3s), 2. Main Value / Story (3-25s), 3. Call to Action ('Follow for Part 2') (25-30s)."
        9 -> "💡 Video Editing Rule: Video me har 2-3 second me text ya jump cut hona chahiye jisse viewer bore na ho."
        10 -> "💡 Hashtag Mix Formula: 3 Small tags (10k-50k posts), 3 Medium tags (100k-500k posts), 2 Big tags (1M+ posts)."
        11 -> "💡 Pre-Post Check: Posting se pehle hamesha ek baar flight mode off-on karke internet speed verify karo taaki HD upload fail na ho."
        else -> "💡 Post Strategy: Video post karte hi story par share karo aur 'New Reel' sticker lagao!"
    }
}

private fun getFreshExplanationEn(stepIdx: Int): String {
    return when (stepIdx) {
        0 -> "💡 Let's break it down simply: Downloading Instagram is like opening the front door to your new creator digital studio. Start by downloading the official app!"
        1 -> "💡 Analogy: A Personal Account is a private diary. A Creator Account puts up a store sign and hands you a free analytics dashboard to track viewers!"
        2 -> "💡 Bio Rule: Think of your Bio as a 3-second elevator pitch. Mention who you serve, what value you deliver, and where to click next."
        3 -> "💡 HD Setting Rule: Instagram compresses blurry videos. Toggling 'Highest Quality Upload' ensures your camera's native crispness is preserved."
        4 -> "💡 Analytics Analogy: Insights are your report card! They reveal peak active hours and exactly which age groups watch your content."
        5 -> "💡 Formats Strategy: Reels capture new audience reach. Stories build personal trust. Highlights serve as your permanent portfolio showcase."
        6 -> "💡 Hook Strategy: If the first 3 seconds don't grab attention, viewers swipe away. Use visual movement or a intriguing bold text overlay!"
        7 -> "💡 Audio Mix Rule: Keep your spoken Voiceover at 100% and Background Trending Audio at 10-15% for optimal clarity."
        else -> "💡 Engagement Rule: Reply to all comments within 1 hour of publishing to trigger Instagram's early engagement algorithm boost!"
    }
}

private fun getFreshExplanationHinglish(stepIdx: Int): String {
    return when (stepIdx) {
        0 -> "💡 Aao ek simple example se samjhte hain: Instagram download karna matlab aapne apni creator shop ka shutter khola. Sabse pehle official app install kar lo!"
        1 -> "💡 Professional Account Analogy: Personal account ek private room hai. Switch to Creator matlab dukan ka board lagana jisse aapko viewer counts mil sakein!"
        2 -> "💡 Bio Formula: Bio aapka billboard hai. 2 second me viewer ko pata chalna chahiye ki aap kya value dete hain aur kahan click karna hai."
        3 -> "💡 HD Upload Rule: Blurry video ki reach ruk jaati hai. Settings me 'Upload at Highest Quality' ON karne se video HD me render hoti hai."
        4 -> "💡 Dashboard Example: Professional Dashboard aapka speedometer hai. Isse dikhta hai ki aapki reach fast ho rahi hai ya slow!"
        5 -> "💡 Content Formats: Reels = Naye Followers ke liye. Stories = Purane Followers ke sath bonding ke liye!"
        6 -> "💡 Hook Rule: First 3 seconds me visual motion ya strong question do taaki viewer ka thumb ruk jaye."
        7 -> "💡 Audio Mixing: Voiceover 100% aur Trending Background Sound 10-15% pe balance karo."
        else -> "💡 Post Growth Trick: Reel upload hote hi pehle 1 hour me aane wale sabhi comments ka reply karke top comment pin kar do!"
    }
}

private fun generateDynamicAiReply(input: String, lang: MentorLanguage, stepIdx: Int): String {
    return when (lang) {
        MentorLanguage.HINDI -> "Aapka sawaal bohot accha hai! Step ${stepIdx + 1} ke baare me: '$input' par dhyaan do aur agla step unlock karne ke liye 'Ho gaya' button tap karo! 😄"
        MentorLanguage.ENGLISH -> "Great question! Regarding Step ${stepIdx + 1}: focus on '$input' and tap 'Done' when you're ready for the next step! 😄"
        MentorLanguage.HINGLISH -> "Mast question hai! Step ${stepIdx + 1} me '$input' par focus karo. Complete hone par 'Ho gaya' button tap karke next step par chalein! 😄"
    }
}

private fun generateReelScript(topic: String, duration: String, creatorType: String, language: MentorLanguage): ScriptResult {
    return ScriptResult(
        topic = topic,
        duration = duration,
        hook = "Wait! Don't scroll if you want to master $topic in 2026...",
        flow = "1. Here is the secret mistake 90% of $creatorType creators make...\n2. Fix this by using this 1 simple trick...\n3. Watch your reach double instantly!",
        cta = "Comment 'GUIDE' below & I'll send you the full breakdown in DM! Follow for daily $creatorType tips! 🚀"
    )
}

private fun generateCaptionAndHashtags(topic: String, creatorType: String, language: MentorLanguage): CaptionResult {
    val nTag = creatorType.replace(" ", "").lowercase()
    return CaptionResult(
        caption = "Stop making this $topic mistake! 🚨 Here is the exact step-by-step framework to scale your content strategy in 2026.\n\nSave this Reel for later & share with a friend who needs this! 📌",
        hashtags = listOf("#$nTag", "#$nTag Tips", "#InstagramGrowth", "#ViralReels", "#CreatorEconomy", "#ReelsStrategy", "#TrendingReels")
    )
}
