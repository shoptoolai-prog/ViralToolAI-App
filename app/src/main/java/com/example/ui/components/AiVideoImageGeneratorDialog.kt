package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class MentorLanguageChoice {
    HINDI, ENGLISH, HINGLISH
}

enum class LearningPath {
    VIDEO, IMAGE
}

data class AiChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "AI_MENTOR" or "CREATOR"
    val text: String,
    val generatedPrompt: String? = null,
    val toolsList: List<AiToolRecommendation>? = null,
    val isFreshExplanation: Boolean = false
)

data class AiToolRecommendation(
    val name: String,
    val logo: ImageVector,
    val website: String,
    val freeLimit: String,
    val isPaidOrFree: String,
    val generateButtonLocation: String
)

@Composable
fun AiVideoImageGeneratorDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    // Preferences & Language state
    var selectedLang by remember { mutableStateOf(MentorLanguageChoice.HINGLISH) }
    var showLangSelector by remember { mutableStateOf(true) }

    // Learning Path: VIDEO vs IMAGE
    var selectedPath by remember { mutableStateOf<LearningPath?>(null) }

    // Step state
    var currentStepIndex by remember { mutableIntStateOf(0) }
    var completedSteps by remember { mutableStateOf(setOf<Int>()) }

    // Re-explanation attempt counters per step (to ensure no duplicate answers)
    val reExplainCount = remember { mutableStateMapOf<Int, Int>() }

    // Video path specific state
    var selectedVideoType by remember { mutableStateOf<String?>(null) }
    var wantsPhotoToVideo by remember { mutableStateOf<Boolean?>(null) }

    // Prompt Builder Inputs
    var characterInput by remember { mutableStateOf("Young creator holding a phone") }
    var backgroundInput by remember { mutableStateOf("Futuristic neon studio with soft bokeh") }
    var cameraInput by remember { mutableStateOf("Slow 4k cinematic zoom-in") }
    var lightingInput by remember { mutableStateOf("Cyberpunk teal & purple rim light") }
    var aspectInput by remember { mutableStateOf("9:16 (Vertical Reels/Shorts)") }
    var moodInput by remember { mutableStateOf("Energetic & Inspiring") }

    // Image path specific state (Thumbnail psychology)
    var selectedImageGoal by remember { mutableStateOf("Viral YouTube Thumbnail") }

    // Generated Prompts History
    val promptHistory = remember { mutableStateListOf<String>() }
    var showPromptHistorySheet by remember { mutableStateOf(false) }

    // Chat Feed
    val chatMessages = remember { mutableStateListOf<AiChatMessage>() }
    val lazyListState = rememberLazyListState()
    var isThinking by remember { mutableStateOf(false) }

    // Scroll helper
    fun scrollToBottom() {
        coroutineScope.launch {
            delay(120)
            if (chatMessages.isNotEmpty()) {
                lazyListState.animateScrollToItem(chatMessages.size - 1)
            }
        }
    }

    // Helper to post mentor message
    fun addMentorMsg(
        text: String,
        generatedPrompt: String? = null,
        toolsList: List<AiToolRecommendation>? = null,
        isFresh: Boolean = false
    ) {
        chatMessages.add(
            AiChatMessage(
                sender = "AI_MENTOR",
                text = text,
                generatedPrompt = generatedPrompt,
                toolsList = toolsList,
                isFreshExplanation = isFresh
            )
        )
        if (generatedPrompt != null) {
            promptHistory.add(0, generatedPrompt)
        }
        scrollToBottom()
    }

    // Helper to post user message
    fun addUserMsg(text: String) {
        chatMessages.add(
            AiChatMessage(
                sender = "CREATOR",
                text = text
            )
        )
        scrollToBottom()
    }

    // Copy to clipboard
    val copyText = { textToCopy: String, label: String ->
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText(label, textToCopy)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context, "Copied $label to clipboard!", Toast.LENGTH_SHORT).show()
    }

    // Initialize Video Step Content
    fun initVideoStep(stepIdx: Int, forceFresh: Boolean = false) {
        val attempt = if (forceFresh) (reExplainCount[stepIdx] ?: 0) + 1 else 0
        if (forceFresh) reExplainCount[stepIdx] = attempt

        when (stepIdx) {
            0 -> {
                // Step V1: What is AI Video & Where it is used
                val msg = when (selectedLang) {
                    MentorLanguageChoice.HINDI -> when (attempt) {
                        0 -> "🎬 Swagat hai AI Video Generator Academy me! ✨\n\nAI Video kya hota hai?\nAI video ek aisi technology hai jahan aap sirf ek text prompt likhte hain ya photo upload karte hain aur AI automatically professional 4K video generate kar deta hai!\n\nKahan use hote hain AI Videos?\n• Instagram Reels & Viral Hooks\n• YouTube Shorts & Long Videos\n• Brand Advertisements & Products\n• Faceless Storytelling Channels\n• Business & Educational Demos\n\nKya aapko basic concept samajh aaya?"
                        1 -> "💡 Naya Example se samjho:\nSocho aapko ek lion ka video banana hai desert me. Old method me real shooting, camera, travel lagta tha. AI Video me bas 5 seconds me prompt likho 'Cinematic lion walking in golden desert' aur AI pixel-perfect video bana dega!\n\nYeh Instagram, Ads aur Shorts me viral traffic laane ke liye ab sabse bada secret tool hai.\n\nAb batao, koi doubt hai ya aage chalein?"
                        else -> "🔥 Simple words me:\nAI Video = Text se Direct Movie Clip! 🎥\nAap command do, AI 10 seconds me video output dega. Zero camera setup, zero actors needed!\n\nSamajh aaya?"
                    }
                    MentorLanguageChoice.ENGLISH -> when (attempt) {
                        0 -> "🎬 Welcome to AI Video Generator Academy! ✨\n\nWhat is an AI Video?\nAI video technology transforms your text prompts or photos into high-definition animated video clips automatically!\n\nWhere are AI Videos used?\n• Instagram Reels & Viral Shorts\n• YouTube Content & Documentaries\n• Product Ads & Marketing\n• Faceless Channels\n• Educational Animations\n\nDoes this basic concept make sense?"
                        1 -> "💡 Let's look at a real-world example:\nImagine you need a video of a futuristic car driving through a cyberpunk city. Normally that requires 3D software or million-dollar budgets. With AI Video, you simply type 'Cyberpunk sports car cruising at night' and get instant video in seconds!\n\nIt's the #1 tool for fast viral content creation.\n\nAre you ready to move forward?"
                        else -> "🔥 In plain terms:\nAI Video = Instant Text-to-Video Engine! 🎥\nType what you imagine, get professional video output without recording anything yourself.\n\nClear so far?"
                    }
                    MentorLanguageChoice.HINGLISH -> when (attempt) {
                        0 -> "🎬 Welcome to AI Video Academy, Creator! ✨\n\nAI Video kya hota hai?\nAI Video ek smart engine hai jahan aap bas TEXT PROMPT likhte ho ya PHOTO upload karte ho, aur AI 100% realistic video generate kar deta hai!\n\nAI Videos kahan use hote hain?\n• Instagram Reels & Viral Hooks\n• YouTube Shorts & Faceless Channels\n• Brand Promotions & Product Ads\n• Motivational & Gaming Edits\n• Educational Videos\n\nSamajh aaya? Ready ho next step ke liye?"
                        1 -> "💡 Ek mast naye example se samjho:\nSuppose aapko ek 'Cyberpunk Robot Drinking Coffee' ka video chahiye. Real shooting me thousands rupees lagte. AI Video me bas prompt type karo aur 5 sec me video tayar!\n\nIsse hazaron creators daily lakhon views gain kar rahe hain zero equipment se.\n\nAb clear hua?"
                        else -> "🔥 Ekdam simple Hinglish me:\nAI Video = Aapka Personal Virtual Director! 🎬\nAap idea do, AI video banakar de dega. No camera, no studio required!\n\nSamajh aaya?"
                    }
                }
                addMentorMsg(msg, isFresh = forceFresh)
            }

            1 -> {
                // Step V2: Choose Video Type
                val msg = when (selectedLang) {
                    MentorLanguageChoice.HINDI -> "🎯 Aap kis type ki AI Video banana chahte hain?\nSelect kijiye neche diye gaye options me se. Main uske hisaab se sabse best FREE tools aur step-by-step method batunga!"
                    MentorLanguageChoice.ENGLISH -> "🎯 Which category of AI Video do you want to create?\nSelect an option below, and I will recommend the top FREE tools tailored to your exact goal!"
                    MentorLanguageChoice.HINGLISH -> "🎯 Aap kis type ki video banana chahte hain?\nSelect karo options me se, main aapke goal ke according BEST FREE AI tools aur secret prompts batunga!"
                }
                addMentorMsg(msg)
            }

            2 -> {
                // Step V3: Tool Recommendation & Account Setup
                val categoryName = selectedVideoType ?: "Talking AI Avatar"
                val tools = listOf(
                    AiToolRecommendation(
                        name = "Google Flow & Gemini AI",
                        logo = Icons.Default.AutoAwesome,
                        website = "flow.google.com / gemini.google.com",
                        freeLimit = "100% Free daily credits",
                        isPaidOrFree = "FREE ACCESS",
                        generateButtonLocation = "Top right 'Generate Video' button"
                    ),
                    AiToolRecommendation(
                        name = "Luma Dream Machine",
                        logo = Icons.Default.Videocam,
                        website = "lumalabs.ai/dream-machine",
                        freeLimit = "30 Free video generations / month",
                        isPaidOrFree = "FREE TIER AVAILABLE",
                        generateButtonLocation = "Center Prompt Box -> 'Create'"
                    ),
                    AiToolRecommendation(
                        name = "Runway ML (Gen-2 / Gen-3)",
                        logo = Icons.Default.Movie,
                        website = "runwayml.com",
                        freeLimit = "125 Free Credits on Signup",
                        isPaidOrFree = "FREE SIGNUP",
                        generateButtonLocation = "'Text/Image to Video' -> Generate"
                    )
                )

                val msg = when (selectedLang) {
                    MentorLanguageChoice.HINDI -> "🛠️ Top FREE AI Tools for $categoryName:\n\n1️⃣ Google Flow & Gemini AI: Instant high-quality video generation with simple prompts.\n2️⃣ Luma Dream Machine: Unmatched motion physics for cinematic & anime scenes.\n3️⃣ Runway ML: Perfect for motion control, camera panning, and high-fidelity output.\n\nAccount Setup Guide:\n• Official Website par jayein aur 'Sign up with Google' par click karein.\n• Login ke baad Dashboard me 'Text to Video' tab kholein.\n• Bottom Prompt Box me apna prompt paste karein aur 'Generate' dabayein!\n\nSamajh aaya?"
                    MentorLanguageChoice.ENGLISH -> "🛠️ Top FREE AI Tools for $categoryName:\n\n1️⃣ Google Flow & Gemini AI: High-speed video synthesis directly from natural prompts.\n2️⃣ Luma Dream Machine: Exceptional motion physics and camera movement.\n3️⃣ Runway ML: Industry standard for camera motion & style control.\n\nAccount Creation & Setup:\n• Visit the website and tap 'Sign Up with Google'.\n• Navigate to the 'Text to Video' generator.\n• Paste your prompt in the prompt box and hit 'Generate'!\n\nDoes this make sense?"
                    MentorLanguageChoice.HINGLISH -> "🛠️ Best Free AI Tools for $categoryName:\n\n1️⃣ Google Flow & Gemini AI: Fastest 4K video generation with free daily credits!\n2️⃣ Luma Dream Machine: Smooth cinematic motion & camera angles.\n3️⃣ Runway ML: Best for high-end professional lighting and details.\n\nAccount Setup & Generation Steps:\n• Website par 'Sign In with Google' karke instant free account banao.\n• Dashboard me 'Text to Video' section open karo.\n• Bottom me 'Prompt Box' dikhega — wahan prompt paste karke Generate click karo!\n\nSamajh aaya?"
                }
                addMentorMsg(msg, toolsList = tools, isFresh = forceFresh)
            }

            3 -> {
                // Step V4: Prompt Learning & Prompt Generator
                val generatedPrompt = "PROMPT [4K CINEMATIC $selectedVideoType]:\n\nSubject: $characterInput\nEnvironment: $backgroundInput\nCamera Motion: $cameraInput\nLighting & Color: $lightingInput\nMood & Vibe: $moodInput\nAspect Ratio: $aspectInput\nStyle: Photorealistic 8K render, smooth 60fps motion, ultra-detailed textures, volumetric lighting --ar ${if (aspectInput.contains("9:16")) "9:16" else "16:9"} --v 6.0"

                val msg = when (selectedLang) {
                    MentorLanguageChoice.HINDI -> "✨ Prompt likhna nahi aata? Tension mat lo!\nMain aapka customized viral AI Video Prompt tayar kar diya hai.\n\nAap is prompt ko copy karke Google Flow, Luma ya Runway me paste karke exact video generate kar sakte hain!"
                    MentorLanguageChoice.ENGLISH -> "✨ Struggling with prompts? Don't worry, I've built one for you!\nHere is your custom-crafted high-converting AI Video Prompt.\n\nCopy this prompt and paste it into Google Flow, Luma, or Runway for instant stunning results!"
                    MentorLanguageChoice.HINGLISH -> "✨ Prompt likhna nahi aata? Main bana deta hoon!\nDekho aapke liye exact High-Converting AI Video Prompt generate kar diya hai.\n\nIs prompt ko COPY karke kisi bhi AI Video tool me paste karo aur magic dekho!"
                }
                addMentorMsg(msg, generatedPrompt = generatedPrompt, isFresh = forceFresh)
            }

            4 -> {
                // Step V5: Personal Photo Video
                val photoGuideMsg = when (selectedLang) {
                    MentorLanguageChoice.HINDI -> "📸 Kya aap apni khud ki PHOTO se AI Video banana chahte hain?\n\nIdeal Photo Rules:\n• Clean Lighting & High Resolution\n• Face clearly visible with natural expression\n• Single person facing camera\n\nKaise Upload Karein?\n1. Tool me 'Image-to-Video' option select karein.\n2. Photo upload karein.\n3. Motion Prompt likhein: 'Subtle smiling expression, slow ambient breeze in hair, 4k cinematic lighting'.\n\nBadhai ho! Aapne AI Video Generation Path complete kar liya! 🎉"
                    MentorLanguageChoice.ENGLISH -> "📸 Want to animate your PERSONAL PHOTO into an AI Video?\n\nPhoto Preparation Guidelines:\n• Bright lighting with sharp facial focus\n• Neutral or smiling expression facing camera\n• Uncluttered background\n\nHow to Animate:\n1. Choose 'Image-to-Video' mode in Luma/Runway.\n2. Upload your photo.\n3. Add motion prompt: 'Natural talking movement, subtle head tilt, volumetric studio light'.\n\nCongratulations! You have mastered AI Video Generation! 🎉"
                    MentorLanguageChoice.HINGLISH -> "📸 Kya aap apni PHOTO use karna chahte hain?\n\nPhoto kaisi honi chahiye?\n• High resolution & front-facing photo\n• Clear lighting, face fully visible\n• No blur or dark background\n\nKaise Animate karein?\n1. AI tool me 'Image-to-Video' button dabao.\n2. Photo upload karo.\n3. Motion prompt daalo: 'Natural talking animation, realistic eye movement, cinematic background glow'.\n\n🎉 Congratulations! Aapne AI Video Path successfully master kar liya!"
                }
                addMentorMsg(photoGuideMsg, isFresh = forceFresh)
            }
        }
    }

    // Initialize Image Step Content
    fun initImageStep(stepIdx: Int, forceFresh: Boolean = false) {
        val attempt = if (forceFresh) (reExplainCount[stepIdx + 10] ?: 0) + 1 else 0
        if (forceFresh) reExplainCount[stepIdx + 10] = attempt

        when (stepIdx) {
            0 -> {
                // Step I1: What is AI Image & Where used
                val msg = when (selectedLang) {
                    MentorLanguageChoice.HINDI -> when (attempt) {
                        0 -> "🖼️ Swagat hai AI Image & Thumbnail Masterclass me! ✨\n\nAI Image kya hoti hai?\nAI Image generation ek technology hai jo aapke text description ko ultra-realistic photos, artwork, logos aur thumbnails me badal deti hai!\n\nKahan use hoti hain AI Images?\n• Viral YouTube Thumbnails & Banners\n• Instagram Posts, Stories & Avatars\n• Product Advertisements & Posters\n• Brand Logos & Vector Graphic Art\n• Profile Pictures & Wallpapers\n\nKya basic idea samajh aaya?"
                        1 -> "💡 Naye Example se samjho:\nMan lo aapko Instagram par ek 'Futuristic Golden Tiger in Neon City' ki photo chahiye. Real photographer yeh click nahi kar sakta. AI Image generator me bas 3 words likho aur 2 seconds me high-resolution image tayar!\n\nYeh graphic designers aur creators ka sabse powerful secret hai.\n\nSamajh aaya?"
                        else -> "🔥 Simple words me:\nAI Image = Instant Digital Studio! 🎨\nAap imagination describe karo, AI 4K image draw karke de dega. Zero drawing skills needed!\n\nClear ho gaya?"
                    }
                    MentorLanguageChoice.ENGLISH -> when (attempt) {
                        0 -> "🖼️ Welcome to AI Image & Thumbnail Masterclass! ✨\n\nWhat is an AI Image Generator?\nAI Image synthesis takes your text prompts and turns them into ultra-detailed 4K photos, illustrations, logos, and viral thumbnails instantly!\n\nWhere are AI Images used?\n• High-CTR YouTube Thumbnails\n• Instagram Feed Graphics & Branding\n• E-commerce Product Catalogs\n• Posters, Banners & Brand Logos\n\nDoes this concept make sense?"
                        1 -> "💡 Here is a real-world scenario:\nInstead of paying thousands for stock images, you can type 'Professional creator working in dark neon gaming room' and get 4 unique royalty-free images in seconds!\n\nIs everything clear so far?"
                        else -> "🔥 Plain & Simple:\nAI Image = Text-to-Art Engine! 🎨\nDescribe what you want to see, get professional artwork instantly.\n\nReady for the next step?"
                    }
                    MentorLanguageChoice.HINGLISH -> when (attempt) {
                        0 -> "🖼️ Welcome to AI Image Generator Academy! ✨\n\nAI Image kya hoti hai?\nAI Image Generator ek smart tool hai jahan aap TEXT PROMPT likhte ho aur AI seconds me photorealistic images, logos, posters aur viral thumbnails generate kar deta hai!\n\nAI Images kahan use hoti hain?\n• High-CTR YouTube Thumbnails\n• Instagram Posts & Brand Assets\n• Product Shoots & Advertisements\n• Unique Logos & Profile Photos\n\nSamajh aaya? Ready ho next topic ke liye?"
                        1 -> "💡 Naye example se dekho:\nAapko 'Cyberpunk Supercar in Rain' ki photo chahiye. AI me prompt daalo aur instant 4K photo mil jayegi without camera or editing software!\n\nAb clear hai?"
                        else -> "🔥 Ekdam simple Hinglish:\nAI Image = Instant Designer Engine! 🎨\nAap soch batao, AI drawing karke ready kar dega!\n\nSamajh aaya?"
                    }
                }
                addMentorMsg(msg, isFresh = forceFresh)
            }

            1 -> {
                // Step I2: Viral Thumbnail Psychology
                val msg = when (selectedLang) {
                    MentorLanguageChoice.HINDI -> "🔥 Viral Thumbnail Psychology Masterclass:\n\n1️⃣ High Contrast Colors: Yellow, Neon Green aur Red background use karein.\n2️⃣ Expressive Face: Shocked, Curious, ya Happy face close-up rakhein.\n3️⃣ Big Bold Text: Maximum 3-4 words in high-contrast font.\n4️⃣ CTR Rule: Thumbnail viewer ko 0.5 sec me question puchne par majboor kare!\n\nGood vs Bad Thumbnail:\n❌ Bad: Dull background, 10 lines of small text, dark face.\n✅ Good: Neon glow background, 3 bold words, clear 4k AI face expression!\n\nSamajh aaya?"
                    MentorLanguageChoice.ENGLISH -> "🔥 Viral Thumbnail Psychology Masterclass:\n\n1️⃣ High-Contrast Palette: Neon Green, Gold, and Deep Charcoal for maximum pop.\n2️⃣ Emotional Face Close-up: Shock, intrigue, or excitement triggers instant clicks.\n3️⃣ 3-Word Rule: Never exceed 3-4 readable words on a thumbnail.\n4️⃣ Curiosity Gap: Make viewers ask 'What happens next?'\n\nGood vs Bad Thumbnail:\n❌ Bad: Dark, cluttered text, no focal point.\n✅ Good: Bright subject, high contrast text, high-resolution AI face!\n\nDo you understand these CTR rules?"
                    MentorLanguageChoice.HINGLISH -> "🔥 Viral Thumbnail Psychology Secrets:\n\n1️⃣ High Contrast Colors: Yellow, Neon Green aur Red background viewer ka dhyan turant khinchte hain.\n2️⃣ Expressive AI Face: Shocked ya Curious face expression daalne se CTR 3x badhta hai!\n3️⃣ Max 3 Words: Text hamesha bada aur padhne me easy hona chahiye.\n4️⃣ Curiosity Gap: Thumbnail dekhte hi bande ke dimaag me 'Kaise hua?' wala question aana chahiye!\n\nSamajh aaya?"
                }
                addMentorMsg(msg, isFresh = forceFresh)
            }

            2 -> {
                // Step I3: Image Tools Recommendation
                val tools = listOf(
                    AiToolRecommendation(
                        name = "Midjourney / Leonardo AI",
                        logo = Icons.Default.Palette,
                        website = "leonardo.ai / midjourney.com",
                        freeLimit = "150 Free Tokens Daily",
                        isPaidOrFree = "FREE DAILY CREDITS",
                        generateButtonLocation = "Image Generation -> Prompt Box"
                    ),
                    AiToolRecommendation(
                        name = "Bing Image Creator & Gemini",
                        logo = Icons.Default.Image,
                        website = "bing.com/create / gemini.google.com",
                        freeLimit = "100% Free Unlimited",
                        isPaidOrFree = "100% FREE",
                        generateButtonLocation = "Prompt Input Bar -> Create"
                    )
                )

                val msg = when (selectedLang) {
                    MentorLanguageChoice.HINDI -> "🛠️ Top FREE AI Image & Thumbnail Tools:\n\n1️⃣ Leonardo AI: 150 daily free tokens, perfect for YouTube thumbnails & character design.\n2️⃣ Bing Image Creator: DALL-E 3 powered 100% free high-resolution image generator.\n3️⃣ Gemini AI: Text-to-Image creation with natural conversational prompts.\n\nSetup Guide:\n• Website khol kar Google/Microsoft account se sign in karein.\n• 'Image Generator' tab par jayein aur prompt paste karke 'Create' button dabayein!\n\nSamajh aaya?"
                    MentorLanguageChoice.ENGLISH -> "🛠️ Top FREE AI Image & Thumbnail Tools:\n\n1️⃣ Leonardo AI: 150 daily tokens, unmatched thumbnail customization controls.\n2️⃣ Bing Image Creator: DALL-E 3 engine, completely free with high photorealism.\n3️⃣ Gemini AI: Conversational image generator with instant iteration.\n\nSetup Guide:\n• Sign in with your Google/Microsoft account.\n• Navigate to the Prompt Bar and hit 'Create'!\n\nIs this clear?"
                    MentorLanguageChoice.HINGLISH -> "🛠️ Best FREE AI Image Tools:\n\n1️⃣ Leonardo AI: Daily 150 free credits! Best for 4K thumbnails & posters.\n2️⃣ Bing Image Creator: DALL-E 3 powered 100% Free tool!\n3️⃣ Gemini AI: Direct chat me text prompt se image generate karta hai.\n\nAccount Setup & Use:\n• Sign in karo Google account se.\n• Image Generator tab kholein, prompt paste karke 'Generate' click karein!\n\nSamajh aaya?"
                }
                addMentorMsg(msg, toolsList = tools, isFresh = forceFresh)
            }

            3 -> {
                // Step I4: Image Prompt Builder
                val generatedPrompt = "PROMPT [VIRAL 4K THUMBNAIL / ART]:\n\nSubject: $characterInput\nEnvironment & Background: $backgroundInput\nLighting & Mood: $lightingInput, dramatic volumetric glow, high contrast\nCamera & Shot: $cameraInput, hyper-detailed skin texture, 8k resolution\nAspect Ratio: $aspectInput\nStyle: Cinematic YouTube Thumbnail style, vibrant neon colors, award-winning photography --ar ${if (aspectInput.contains("9:16")) "9:16" else "16:9"}"

                val msg = when (selectedLang) {
                    MentorLanguageChoice.HINDI -> "✨ Certified Viral AI Image Prompt Generated!\n\nYeh prompt aapko Leonardo AI, Bing Image Creator, ya Midjourney me exact professional result dega.\n\nPrompt copy karein aur apne AI Tool me paste karein! 🎉"
                    MentorLanguageChoice.ENGLISH -> "✨ Certified Viral AI Image Prompt Generated!\n\nThis prompt is optimized for Leonardo AI, Bing Image Creator, and Midjourney to deliver peak visual contrast.\n\nCopy the prompt and paste it into your AI Tool! 🎉"
                    MentorLanguageChoice.HINGLISH -> "✨ High-CTR Viral AI Image Prompt Generated!\n\nIs prompt ko Leonardo AI ya Bing Image Creator me daalo aur exact 4K thumbnail / image ready payein.\n\nCopy button se prompt copy karo aur test karo! 🎉"
                }
                addMentorMsg(msg, generatedPrompt = generatedPrompt, isFresh = forceFresh)
            }
        }
    }

    // Confirmation Handler (YES / NO)
    fun handleConfirmation(isYes: Boolean) {
        val path = selectedPath ?: return
        val currentIdx = currentStepIndex

        if (isYes) {
            val userText = when (selectedLang) {
                MentorLanguageChoice.HINDI -> "Haan, samajh aaya! Aage chalo 🚀"
                MentorLanguageChoice.ENGLISH -> "Yes, I got it! Move to next step 🚀"
                MentorLanguageChoice.HINGLISH -> "Haan, samajh aaya! Aage chalo 🚀"
            }
            addUserMsg(userText)

            completedSteps = completedSteps + currentIdx

            val totalStepsInPath = if (path == LearningPath.VIDEO) 5 else 4
            if (currentIdx < totalStepsInPath - 1) {
                currentStepIndex += 1
                coroutineScope.launch {
                    isThinking = true
                    delay(400)
                    isThinking = false
                    if (path == LearningPath.VIDEO) {
                        initVideoStep(currentStepIndex)
                    } else {
                        initImageStep(currentStepIndex)
                    }
                }
            } else {
                val completionMsg = when (selectedLang) {
                    MentorLanguageChoice.HINDI -> "🎉 Shabaash! Aapne poora course complete kar liya hai! +100 XP Earned!"
                    MentorLanguageChoice.ENGLISH -> "🎉 Outstanding! You have completed the entire masterclass! +100 XP Earned!"
                    MentorLanguageChoice.HINGLISH -> "🎉 Badhai ho! Aapne poora AI Masterclass complete kar liya hai! +100 XP Earned!"
                }
                addMentorMsg(completionMsg)
            }
        } else {
            val userText = when (selectedLang) {
                MentorLanguageChoice.HINDI -> "Nahi, samajh nahi aaya. Dubara naye tareeqe se samjhao 🔄"
                MentorLanguageChoice.ENGLISH -> "No, I didn't get it. Please explain again with a new example 🔄"
                MentorLanguageChoice.HINGLISH -> "Nahi, samajh nahi aaya. Dubara naye example se samjhao 🔄"
            }
            addUserMsg(userText)

            coroutineScope.launch {
                isThinking = true
                delay(500)
                isThinking = false
                if (path == LearningPath.VIDEO) {
                    initVideoStep(currentIdx, forceFresh = true)
                } else {
                    initImageStep(currentIdx, forceFresh = true)
                }
            }
        }
    }

    val imeBottomPadding = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
    LaunchedEffect(chatMessages.size, isThinking, imeBottomPadding) {
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

                    // TOP NAVIGATION BAR
                    TopHeaderBar(
                        selectedLang = selectedLang,
                        selectedPath = selectedPath,
                        currentStep = currentStepIndex,
                        totalSteps = if (selectedPath == LearningPath.VIDEO) 5 else 4,
                        onChangeLang = { showLangSelector = true },
                        onChangePath = {
                            selectedPath = null
                            currentStepIndex = 0
                            chatMessages.clear()
                        },
                        onOpenPromptHistory = { showPromptHistorySheet = true },
                        onClose = onDismiss
                    )

                    // DIALOG CONTENT SWITCHER
                    if (showLangSelector) {
                        // STEP 1: LANGUAGE SELECTION POPUP
                        LanguageSelectionStep(
                            currentLang = selectedLang,
                            onSelectLang = { chosen ->
                                selectedLang = chosen
                                showLangSelector = false
                            }
                        )
                    } else if (selectedPath == null) {
                        // STEP 2: PATH SELECTION ("Aaj aap kya seekhna chahte hain?")
                        LearningPathSelectionStep(
                            onSelectPath = { path ->
                                selectedPath = path
                                currentStepIndex = 0
                                chatMessages.clear()
                                if (path == LearningPath.VIDEO) {
                                    initVideoStep(0)
                                } else {
                                    initImageStep(0)
                                }
                            }
                        )
                    } else {
                        // CHAT & INTERACTIVE STEP CONTENT
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            // Step Progress Bar
                            StepProgressIndicator(
                                currentStep = currentStepIndex,
                                totalSteps = if (selectedPath == LearningPath.VIDEO) 5 else 4,
                                completedSteps = completedSteps
                            )

                            // Chat Feed
                            LazyColumn(
                                state = lazyListState,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                items(chatMessages, key = { it.id }) { message ->
                                    ChatMessageItem(
                                        message = message,
                                        onCopyPrompt = { prompt -> copyText(prompt, "AI Prompt") }
                                    )
                                }

                                if (isThinking) {
                                    item {
                                        ThinkingBubble()
                                    }
                                }

                                val totalStepsInPath = if (selectedPath == LearningPath.VIDEO) 5 else 4
                                if (completedSteps.size >= totalStepsInPath) {
                                    item {
                                        CourseCompletionCard(
                                            courseTitle = if (selectedPath == LearningPath.VIDEO) "AI Video Generation Course" else "AI Thumbnail & Image Design",
                                            skillsLearned = listOf(
                                                "Text-to-Video & Image Prompts",
                                                "Cinematic Camera Movement Keywords",
                                                "Thumbnail Visual Psychology & CTR",
                                                "High-Contrast Color & Aspect Ratio Control"
                                            ),
                                            onContinue = onDismiss,
                                            onResetCourse = {
                                                completedSteps = emptySet()
                                                currentStepIndex = 0
                                                chatMessages.clear()
                                                if (selectedPath == LearningPath.VIDEO) initVideoStep(0) else initImageStep(0)
                                            },
                                            theme = MentorToolTheme.InstagramCreator
                                        )
                                    }
                                }
                            }

                            // INTERACTIVE INPUTS / SELECTION CONTROLS FOR CURRENT STEP
                            InteractiveControlPanel(
                                selectedPath = selectedPath!!,
                                currentStepIndex = currentStepIndex,
                                selectedVideoType = selectedVideoType,
                                characterInput = characterInput,
                                backgroundInput = backgroundInput,
                                cameraInput = cameraInput,
                                lightingInput = lightingInput,
                                aspectInput = aspectInput,
                                moodInput = moodInput,
                                onVideoTypeSelected = { type ->
                                    selectedVideoType = type
                                    addUserMsg("Selected Video Type: $type 🎬")
                                    currentStepIndex = 2
                                    initVideoStep(2)
                                },
                                onCharacterChange = { characterInput = it },
                                onBackgroundChange = { backgroundInput = it },
                                onCameraChange = { cameraInput = it },
                                onLightingChange = { lightingInput = it },
                                onAspectChange = { aspectInput = it },
                                onMoodChange = { moodInput = it },
                                onGeneratePromptAgain = {
                                    if (selectedPath == LearningPath.VIDEO) {
                                        initVideoStep(3, forceFresh = true)
                                    } else {
                                        initImageStep(3, forceFresh = true)
                                    }
                                },
                                onConfirmation = { isYes -> handleConfirmation(isYes) }
                            )
                        }
                    }
                }
            }
        }

        // PROMPT HISTORY SHEET
        if (showPromptHistorySheet) {
            PromptHistoryDialog(
                history = promptHistory,
                onCopy = { copyText(it, "Saved Prompt") },
                onDismiss = { showPromptHistorySheet = false }
            )
        }
    }
}

// ================= TOP HEADER BAR =================

@Composable
private fun TopHeaderBar(
    selectedLang: MentorLanguageChoice,
    selectedPath: LearningPath?,
    currentStep: Int,
    totalSteps: Int,
    onChangeLang: () -> Unit,
    onChangePath: () -> Unit,
    onOpenPromptHistory: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(Color(0xFF0D1611))
            .border(BorderStroke(0.8.dp, Color(0x22FFFFFF)))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                        .background(EmeraldPrimary.copy(alpha = 0.2f))
                        .border(BorderStroke(1.dp, EmeraldGlow), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = EmeraldGlow,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = "AI Video & Images Generator",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                    Text(
                        text = if (selectedPath == null) "Choose Learning Path" else "${selectedPath.name} MASTERCLASS • Step ${currentStep + 1}/$totalSteps",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGlow
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Language Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x1A10B981))
                        .border(BorderStroke(0.8.dp, EmeraldPrimary.copy(alpha = 0.5f)), RoundedCornerShape(10.dp))
                        .clickable { onChangeLang() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = when (selectedLang) {
                            MentorLanguageChoice.HINDI -> "🇮🇳 HI"
                            MentorLanguageChoice.ENGLISH -> "🇬🇧 EN"
                            MentorLanguageChoice.HINGLISH -> "🗣️ HING"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGlow
                    )
                }

                if (selectedPath != null) {
                    // Path Switcher Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x1A8B5CF6))
                            .border(BorderStroke(0.8.dp, ElectricPurple.copy(alpha = 0.5f)), RoundedCornerShape(10.dp))
                            .clickable { onChangePath() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Switch Path",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA78BFA)
                        )
                    }

                    // Prompt History Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x22FFFFFF))
                            .clickable { onOpenPromptHistory() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                            tint = TextWhite,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Close Button
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextWhite
                    )
                }
            }
        }
    }
}

// ================= STEP 1: LANGUAGE SELECTION POPUP =================

@Composable
private fun LanguageSelectionStep(
    currentLang: MentorLanguageChoice,
    onSelectLang: (MentorLanguageChoice) -> Unit
) {
    var tempLang by remember { mutableStateOf(currentLang) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xFF0F1B14))
                .border(
                    BorderStroke(
                        1.5.dp,
                        Brush.linearGradient(
                            listOf(EmeraldGlow, ElectricPurple, EmeraldPrimary)
                        )
                    ),
                    RoundedCornerShape(28.dp)
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(EmeraldPrimary.copy(alpha = 0.2f))
                        .border(BorderStroke(1.2.dp, EmeraldGlow), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = null,
                        tint = EmeraldGlow,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Choose your learning language",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Select your preferred language for AI Video & Image Masterclass lessons and prompts.",
                    fontSize = 12.sp,
                    color = TextWhite.copy(alpha = 0.65f),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Options
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LanguageOptionRow(
                        title = "हिन्दी",
                        subtitle = "Pure Hindi lessons & simple examples",
                        isSelected = tempLang == MentorLanguageChoice.HINDI,
                        onClick = { tempLang = MentorLanguageChoice.HINDI }
                    )

                    LanguageOptionRow(
                        title = "English",
                        subtitle = "Global English terms & professional prompts",
                        isSelected = tempLang == MentorLanguageChoice.ENGLISH,
                        onClick = { tempLang = MentorLanguageChoice.ENGLISH }
                    )

                    LanguageOptionRow(
                        title = "Hinglish",
                        subtitle = "Mix of Hindi & English — Most Popular!",
                        isSelected = tempLang == MentorLanguageChoice.HINGLISH,
                        onClick = { tempLang = MentorLanguageChoice.HINGLISH }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onSelectLang(tempLang) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text(
                        text = "Continue",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = AmoledBlack
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageOptionRow(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0x2210B981) else Color(0x0AFFFFFF))
            .border(
                BorderStroke(
                    1.2.dp,
                    if (isSelected) EmeraldGlow else Color(0x22FFFFFF)
                ),
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextWhite.copy(alpha = 0.6f)
                )
            }

            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = EmeraldGlow)
            )
        }
    }
}

// ================= STEP 2: PATH SELECTION =================

@Composable
private fun LearningPathSelectionStep(
    onSelectPath: (LearningPath) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Aaj aap kya seekhna chahte hain?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Choose a specialized AI creation masterclass below to start learning from Zero to Advance.",
            fontSize = 12.5.sp,
            color = TextWhite.copy(alpha = 0.65f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        // CARD 1: AI VIDEO GENERATION
        PathCardItem(
            title = "🎥 AI Video Generation",
            subtitle = "Learn to create AI videos from zero using free AI tools.",
            accentColor = EmeraldGlow,
            features = listOf(
                "Text to 4K Video Synthesis",
                "Talking AI Avatars & Animate Photos",
                "Top Free AI Tools (Google Flow, Luma, Runway)",
                "Conversational Prompt Builder"
            ),
            onClick = { onSelectPath(LearningPath.VIDEO) }
        )

        Spacer(modifier = Modifier.height(18.dp))

        // CARD 2: AI IMAGE GENERATION
        PathCardItem(
            title = "🖼 AI Image Generation",
            subtitle = "Learn professional AI image creation with prompts.",
            accentColor = ElectricPurple,
            features = listOf(
                "Viral YouTube Thumbnail Psychology & CTR",
                "Text to Photorealistic Art & Logos",
                "Top Free Tools (Midjourney, Leonardo AI, Bing)",
                "High-Converting Image Prompt Generator"
            ),
            onClick = { onSelectPath(LearningPath.IMAGE) }
        )
    }
}

@Composable
private fun PathCardItem(
    title: String,
    subtitle: String,
    accentColor: Color,
    features: List<String>,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "pathCardScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF18261E),
                        Color(0xFF0E1712)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.2.dp,
                    Brush.linearGradient(
                        listOf(accentColor, EmeraldPrimary, Color(0x33FFFFFF))
                    )
                ),
                RoundedCornerShape(24.dp)
            )
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.2f))
                        .border(BorderStroke(0.8.dp, accentColor), RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "START PATH",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Black,
                        color = accentColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextWhite.copy(alpha = 0.75f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = feature,
                            fontSize = 11.5.sp,
                            color = TextWhite.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }
    }
}

// ================= STEP PROGRESS INDICATOR =================

@Composable
private fun StepProgressIndicator(
    currentStep: Int,
    totalSteps: Int,
    completedSteps: Set<Int>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF09120C))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until totalSteps) {
            val isCurrent = i == currentStep
            val isCompleted = completedSteps.contains(i)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCompleted -> EmeraldGlow
                            isCurrent -> EmeraldPrimary
                            else -> Color(0x22FFFFFF)
                        }
                    )
            )
        }
    }
}

// ================= CHAT MESSAGE ITEM =================

@Composable
private fun ChatMessageItem(
    message: AiChatMessage,
    onCopyPrompt: (String) -> Unit
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
                    .background(EmeraldPrimary)
                    .border(BorderStroke(1.dp, EmeraldGlow), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = AmoledBlack,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isMentor) 4.dp else 18.dp,
                        bottomEnd = if (isMentor) 18.dp else 4.dp
                    )
                )
                .background(
                    if (isMentor) Brush.linearGradient(
                        listOf(Color(0xFF14241B), Color(0xFF0C1811))
                    ) else Brush.linearGradient(
                        listOf(EmeraldPrimary, Color(0xFF059669))
                    )
                )
                .border(
                    BorderStroke(
                        0.8.dp,
                        if (isMentor) Color(0x3310B981) else Color.White.copy(alpha = 0.4f)
                    ),
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isMentor) 4.dp else 18.dp,
                        bottomEnd = if (isMentor) 18.dp else 4.dp
                    )
                )
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    color = if (isMentor) TextWhite else AmoledBlack,
                    fontWeight = if (isMentor) FontWeight.Normal else FontWeight.Bold,
                    lineHeight = 18.sp
                )

                // Tools List Recommendation Cards
                if (!message.toolsList.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        message.toolsList.forEach { tool ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x1A10B981))
                                    .border(BorderStroke(0.8.dp, EmeraldPrimary.copy(alpha = 0.4f)), RoundedCornerShape(12.dp))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = tool.logo,
                                            contentDescription = null,
                                            tint = EmeraldGlow,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = tool.name,
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldGlow
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "🌐 ${tool.website}",
                                        fontSize = 10.5.sp,
                                        color = TextWhite.copy(alpha = 0.8f)
                                    )
                                    Text(
                                        text = "🎁 Limit: ${tool.freeLimit} (${tool.isPaidOrFree})",
                                        fontSize = 10.sp,
                                        color = TextWhite.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "📍 Location: ${tool.generateButtonLocation}",
                                        fontSize = 10.sp,
                                        color = EmeraldGlow.copy(alpha = 0.9f)
                                    )
                                }
                            }
                        }
                    }
                }

                // Generated Copyable Prompt Box
                if (!message.generatedPrompt.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF09120C))
                            .border(BorderStroke(1.dp, EmeraldGlow), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "✨ READY AI PROMPT",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = EmeraldGlow
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(EmeraldPrimary)
                                        .clickable { onCopyPrompt(message.generatedPrompt) }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Copy Prompt",
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AmoledBlack
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = message.generatedPrompt,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextWhite.copy(alpha = 0.9f),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ================= THINKING BUBBLE =================

@Composable
private fun ThinkingBubble() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(start = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = EmeraldGlow,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "AI Mentor is typing...",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = EmeraldGlow
        )
    }
}

// ================= INTERACTIVE CONTROL PANEL =================

@Composable
private fun InteractiveControlPanel(
    selectedPath: LearningPath,
    currentStepIndex: Int,
    selectedVideoType: String?,
    characterInput: String,
    backgroundInput: String,
    cameraInput: String,
    lightingInput: String,
    aspectInput: String,
    moodInput: String,
    onVideoTypeSelected: (String) -> Unit,
    onCharacterChange: (String) -> Unit,
    onBackgroundChange: (String) -> Unit,
    onCameraChange: (String) -> Unit,
    onLightingChange: (String) -> Unit,
    onAspectChange: (String) -> Unit,
    onMoodChange: (String) -> Unit,
    onGeneratePromptAgain: () -> Unit,
    onConfirmation: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0C1610))
            .border(BorderStroke(0.8.dp, Color(0x22FFFFFF)))
            .padding(14.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            // Step V2 Video Type Selection Chips
            if (selectedPath == LearningPath.VIDEO && currentStepIndex == 1) {
                Text(
                    text = "Aap kis type ki video banana chahte hain?",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGlow
                )

                val types = listOf(
                    "🗣️ Talking AI Avatar",
                    "🎬 Cinematic Video",
                    "🎨 Anime",
                    "📦 Product Ad",
                    "📖 Story Video",
                    "🔥 Motivational",
                    "✈️ Travel",
                    "🎮 Gaming",
                    "👤 Faceless Content",
                    "✨ Other"
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(types) { type ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x1A10B981))
                                .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)), RoundedCornerShape(12.dp))
                                .clickable { onVideoTypeSelected(type) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = type,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                    }
                }
            }

            // Prompt Builder Inputs Box (Step 3 in Video/Image)
            else if (currentStepIndex == 3) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "🎨 Custom Prompt Inputs:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGlow
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = characterInput,
                            onValueChange = onCharacterChange,
                            label = { Text("Subject / Character", fontSize = 9.5.sp) },
                            textStyle = LocalTextStyle.current.copy(fontSize = 11.sp, color = TextWhite),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = backgroundInput,
                            onValueChange = onBackgroundChange,
                            label = { Text("Background / Studio", fontSize = 9.5.sp) },
                            textStyle = LocalTextStyle.current.copy(fontSize = 11.sp, color = TextWhite),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = cameraInput,
                            onValueChange = onCameraChange,
                            label = { Text("Camera Angle", fontSize = 9.5.sp) },
                            textStyle = LocalTextStyle.current.copy(fontSize = 11.sp, color = TextWhite),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = lightingInput,
                            onValueChange = onLightingChange,
                            label = { Text("Lighting & Glow", fontSize = 9.5.sp) },
                            textStyle = LocalTextStyle.current.copy(fontSize = 11.sp, color = TextWhite),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Button(
                        onClick = onGeneratePromptAgain,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Text(
                            text = "✨ Aur Better Prompt Banayein (New Variant)",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Black,
                            color = AmoledBlack
                        )
                    }
                }
            }

            // YES / NO Confirmation Row
            Column {
                Text(
                    text = "Samajh aaya?",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // YES Button
                    Button(
                        onClick = { onConfirmation(true) },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Text(
                            text = "YES, Aage Chalo 🚀",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = AmoledBlack
                        )
                    }

                    // NO Button
                    Button(
                        onClick = { onConfirmation(false) },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x22F43F5E)),
                        border = BorderStroke(1.dp, CrimsonLight)
                    ) {
                        Text(
                            text = "NO, Dubara Samjhao 🔄",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CrimsonLight
                        )
                    }
                }
            }
        }
    }
}

// ================= PROMPT HISTORY SHEET =================

@Composable
private fun PromptHistoryDialog(
    history: List<String>,
    onCopy: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF0F1B14))
                .border(BorderStroke(1.2.dp, EmeraldGlow), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📜 Generated Prompts History",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = TextWhite)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (history.isEmpty()) {
                    Text(
                        text = "No prompts generated yet. Start learning to create custom prompts!",
                        fontSize = 12.sp,
                        color = TextWhite.copy(alpha = 0.5f)
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.heightIn(max = 300.dp)
                    ) {
                        items(history) { item ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x1AFFFFFF))
                                    .border(BorderStroke(0.8.dp, Color(0x22FFFFFF)), RoundedCornerShape(12.dp))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = item,
                                        fontSize = 11.sp,
                                        color = TextWhite
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    TextButton(onClick = { onCopy(item) }) {
                                        Text("Copy Prompt", fontSize = 10.sp, color = EmeraldGlow)
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
