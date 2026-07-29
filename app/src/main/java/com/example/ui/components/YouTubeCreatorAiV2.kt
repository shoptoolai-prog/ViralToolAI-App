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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import com.example.ui.theme.responsiveImeAndNavPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
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
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Videocam
import com.example.ui.screens.OfficialLogo
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
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.TextWhite

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val EmeraldPrimary = Color(0xFFEF4444) // YouTube Red Theme
private val EmeraldGlow = Color(0x33EF4444) // Red Glow Effect
private val ytTheme = MentorToolTheme.YouTubeCreator

/**
 * PHASE — YOUTUBE CREATOR AI (ZERO TO ADVANCED AI MENTOR)
 *
 * An interactive, personal AI mentor that guides users step-by-step from zero to a professional,
 * monetized YouTube creator.
 */

// Colors for YouTube Theme
private val YouTubeRed = Color(0xFFFF0000)
private val YouTubeRedGlow = Color(0xFFFF3333)
private val YouTubeRedDark = Color(0xFFCC0000)

enum class YouTubeLanguage { HINDI, ENGLISH, HINGLISH }

data class YouTubeStep(
    val id: Int,
    val title: String,
    val desc: String,
    val initialMessageHindi: String,
    val initialMessageEnglish: String,
    val initialMessageHinglish: String,
    val smartQuestionHindi: String,
    val smartQuestionEnglish: String,
    val smartQuestionHinglish: String,
    val explainVariantsHindi: List<String>,
    val explainVariantsEnglish: List<String>,
    val explainVariantsHinglish: List<String>
)

data class YouTubeChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val isFromUser: Boolean,
    val text: String,
    val timestamp: String = "Just now",
    val quickReplies: List<String> = emptyList(),
    val actionType: String? = null
)

// List of Creator Types
val YOUTUBE_CREATOR_TYPES = listOf(
    "Vlogger" to "🎥",
    "Tech" to "💻",
    "Gaming" to "🎮",
    "Education" to "📚",
    "Comedy" to "😂",
    "Finance" to "📈",
    "Fitness" to "💪",
    "Lifestyle" to "✨",
    "Shorts Creator" to "⚡",
    "Long Video Creator" to "🎬",
    "Music" to "🎵",
    "Podcast" to "🎙️",
    "Other" to "🌟"
)

// Complete YouTube Roadmap
val YOUTUBE_ROADMAP_STEPS = listOf(
    YouTubeStep(
        id = 1,
        title = "Google Account & YouTube App",
        desc = "Setup clean Google Account and install latest YouTube & Studio apps.",
        initialMessageHindi = "Chalo start karte hain! Sabse pehle aapka ek dedicated Google (Gmail) Account hona chahiye YouTube ke liye.\n\nKey Step:\n1. YouTube App & YouTube Studio App install kijiye.\n2. Google account log in kijiye.\n\nSikhnge ki kaise setup karna hai! Tayar ho?",
        initialMessageEnglish = "Welcome! Let's build your YouTube channel from scratch. First, you need a dedicated Google Account.\n\nKey Step:\n1. Install YouTube App & YouTube Studio App.\n2. Sign in with your Google account.\n\nReady to get started?",
        initialMessageHinglish = "Chalo dost, YouTube journey start karte hain! Sabse pehle ek clean Google Account chahiye.\n\nKey Step:\n1. Play Store se YouTube & YouTube Studio app install karo.\n2. Google account me sign in karo.\n\nKya ye ho gaya?",
        smartQuestionHindi = "Google account aur YouTube Studio app ready hai?",
        smartQuestionEnglish = "Is your Google Account and YouTube Studio app ready?",
        smartQuestionHinglish = "Google account aur YT Studio install ho gaya?",
        explainVariantsHindi = listOf(
            "Dekho simple hai! YouTube Studio app YouTube creators ka control center hota hai. Yahan aap views, subscribers, revenue aur comments track kar sakte ho. Google Account wahi email hai jisse aap channel manage karoge.",
            "Flow chart dekho:\nGoogle Email ➔ YouTube Login ➔ YouTube Studio App.\n\nIsse aapko complete analytics aur channel security milegi. Koi confusion hai?"
        ),
        explainVariantsEnglish = listOf(
            "It's very simple! YouTube Studio is your command center. It gives you deep analytics, comments management, and monetization settings. Your Google Account secures your channel.",
            "Here is the simple flow:\nCreate Email ➔ Login to YouTube ➔ Download YT Studio App.\n\nThis keeps your personal emails separate from your creator brand."
        ),
        explainVariantsHinglish = listOf(
            "Dost simple language me samjho:\nYouTube Studio app creators ka dashboard hota hai. Wahan views, subscriber count, aur earnings dikhti hai. Ek fresh Gmail id se login karna best rehta hai.",
            "Ek real life example:\nJaise dukaan kholne se pehle chabi aur register chahiye hota hai, waise hi YT channel ke liye Gmail + YT Studio app chahiye."
        )
    ),
    YouTubeStep(
        id = 2,
        title = "Create YouTube Channel",
        desc = "Name your channel, handle & create branding identity.",
        initialMessageHindi = "Ab hum aapka YouTube Channel create karenge!\n\nSteps:\n1. YouTube App open karo -> Profile icon par tap karo.\n2. 'Create a Channel' select karo.\n3. Apna Creator Handle (@yourhandle) choose karo.\n\nYe step complete karne par 'Done' bolo!",
        initialMessageEnglish = "Now let's create your YouTube Channel!\n\nSteps:\n1. Open YouTube App -> Tap Profile icon.\n2. Tap 'Create a Channel'.\n3. Choose your unique Handle (@yourhandle).\n\nReply 'Done' when completed!",
        initialMessageHinglish = "Ab mast sa YouTube Channel banate hain!\n\nSteps:\n1. YouTube app kholo -> Top right DP par tap karo.\n2. 'Create Channel' par click karo.\n3. Ek catchy Handle (@yourname) set karo.\n\nHo gaya toh 'Done' bolo!",
        smartQuestionHindi = "Channel name aur handle set ho gaya?",
        smartQuestionEnglish = "Did you set your channel name and handle?",
        smartQuestionHinglish = "Channel create karke handle choose kar liya?",
        explainVariantsHindi = listOf(
            "Dhyan do: Channel Name aisa rakho jo log aasani se yaad rakh sakein. Handle (@name) YouTube par aapki unique identity hoti hai.",
            "Example: Agar aap Tech Creator ho toh @TechWithAniket ya @AniketTech Best rahega. Easy to search, clean and professional!"
        ),
        explainVariantsEnglish = listOf(
            "Remember: Pick a clear, memorable channel name. Your handle (@handle) is your unique identifier on YouTube for tagging and search.",
            "Example: For a gaming channel, @GamerAlex or @AlexGaming is easy for viewers to find and mention!"
        ),
        explainVariantsHinglish = listOf(
            "Dost, name simple aur short hona chahiye! Unique handle se log aapko mention kar payenge aur search me easily paayenge.",
            "Example: Agar Gaming niche hai toh @RahulPlays ya @RahulGaming catchy lagta hai."
        )
    ),
    YouTubeStep(
        id = 3,
        title = "Profile Logo & Banner",
        desc = "Upload HD profile photo (logo) and banner art.",
        initialMessageHindi = "Aapka Channel visual identity bohot important hai!\n\n1. Profile Picture (Logo): 800x800 px clear photo ya logo.\n2. Channel Banner (Art): 2560x1440 px banner jo bataye aapka channel kis baare me hai.\n\nCanva app se easily bana sakte ho!",
        initialMessageEnglish = "Your visual identity is key to attracting subscribers!\n\n1. Profile Picture: Clear high-res photo or logo (800x800).\n2. Channel Banner: Banner art (2560x1440) showcasing your content niche.\n\nYou can design both for free on Canva!",
        initialMessageHinglish = "Channel ka look badhiya karna hai!\n\n1. Profile Logo: Clear HD photo ya clean logo.\n2. Channel Banner: Badhiya banner art jo aapke topic ko bataye.\n\nCanva app se 2 minute me ban jata hai. Upload ho gaya?",
        smartQuestionHindi = "Logo aur Banner upload ho gaya?",
        smartQuestionEnglish = "Is your logo and banner art uploaded?",
        smartQuestionHinglish = "Logo aur banner lag gaya channel pe?",
        explainVariantsHindi = listOf(
            "Profile photo aapki face value ya brand dikhati hai. Banner me 3 cheezein likho: Niche, Upload Schedule, aur CTA (e.g. 'New Video Every Friday!').",
            "Canva app me 'YouTube Banner' search karo, bane-banaye templates mil jayenge. Bas text badlo aur download karke upload kar do!"
        ),
        explainVariantsEnglish = listOf(
            "Your profile photo builds trust. On the banner, include 3 key elements: Your Topic, Upload Schedule, and a call to Subscribe.",
            "Pro-Tip: Search 'YouTube Banner' in Canva for pre-made aesthetic templates. Just customize and upload!"
        ),
        explainVariantsHinglish = listOf(
            "Dost simple hai: Logo me aapka face clear hona chahiye ya logo. Banner pe likh do 'New Reel/Video Daily!' taaki new viewers subscribe karein.",
            "Canva install karo, 'YouTube Banner' template edit karo aur YT Studio me Customize -> Branding me upload kar do."
        )
    ),
    YouTubeStep(
        id = 4,
        title = "Channel Description & Links",
        desc = "Write SEO-rich About section & add social links.",
        initialMessageHindi = "Ab 'About' section optimize karenge!\n\n1. First 2 lines me batayein ki viewer ko kya fayda milega.\n2. Relevant Keywords use kijiye (SEO).\n3. Business Inquiry email & Instagram links add kijiye.",
        initialMessageEnglish = "Let's optimize your 'About' section for search!\n\n1. First 2 lines should explain what viewers gain.\n2. Use relevant keywords naturally.\n3. Add business contact email & social links.",
        initialMessageHinglish = "Channel ki About Summary likhte hain!\n\n1. Pehli 2 lines me likho ki aap kya sikhate ho/kya banate ho.\n2. Main topics ke keywords dalo.\n3. Business email aur Instagram link add karo.",
        smartQuestionHindi = "About section aur links add ho gaye?",
        smartQuestionEnglish = "Have you filled your About section and links?",
        smartQuestionHinglish = "About section me details likh di?",
        explainVariantsHindi = listOf(
            "YouTube search engine hai! About section me keywords honge toh aapka channel top searches me dikhega.",
            "Example: 'Welcome! Is channel par aapko daily Tech Reviews, Unboxing aur Tips milenge. Subscribe for weekly updates!'"
        ),
        explainVariantsEnglish = listOf(
            "YouTube is the 2nd largest search engine! Keywords in your description help YouTube recommend your channel to target audiences.",
            "Example: 'Welcome! Here you'll find daily tech tutorials, budget smartphone reviews, and tech tips. Subscribe for fresh content!'"
        ),
        explainVariantsHinglish = listOf(
            "Simple formula:\nGreeting + What you make + Why subscribe + Business Contact.\n\nIsse YouTube Algorithm ko pata chalta hai ki aapka channel kis baare me hai.",
            "Dost 2-3 paragraphs me apna channel explain karo aur hashtags bhi add kar sakte ho."
        )
    ),
    YouTubeStep(
        id = 5,
        title = "Channel SEO & Keywords",
        desc = "Setup core Channel Keywords in YouTube Studio.",
        initialMessageHindi = "Bohot IMPORTANT Step: Channel Keywords!\n\n1. studio.youtube.com ko desktop mode par open karo.\n2. Settings -> Channel -> Basic Info par jaao.\n3. Apne niche se related 10-15 main keywords dalo.",
        initialMessageEnglish = "CRITICAL Step: Channel Keywords!\n\n1. Open studio.youtube.com in Desktop Browser Mode.\n2. Go to Settings -> Channel -> Basic Info.\n3. Add 10-15 core niche keywords.",
        initialMessageHinglish = "Boht zaruri step: Channel Keywords!\n\n1. Chrome Browser me studio.youtube.com Desktop Mode me kholo.\n2. Settings ⚙️ -> Channel -> Basic Info par jao.\n3. Apne category ke main tags aur apna channel name dalo.",
        smartQuestionHindi = "Settings me Channel Keywords add kiye?",
        smartQuestionEnglish = "Did you set your Channel Keywords in YT Studio settings?",
        smartQuestionHinglish = "Studio settings me keywords dhyan se daal diye?",
        explainVariantsHindi = listOf(
            "Keywords batate hain ki aapka channel kin topics par hai. Jaise: 'Tech News, Smartphone Review, Unboxing, How to, Hindi Tech'.",
            "Apna channel name bhi alag-alag tarike se keywords me zaroor dalo (e.g. 'Aniket Tech', 'AniketTech', 'Tech Aniket')."
        ),
        explainVariantsEnglish = listOf(
            "Keywords tell YouTube who to show your videos to. E.g.: 'Vlogs, Daily Vlogger, India Travel, Travel Tips, Daily Life Vlogs'.",
            "Always include your channel name variations in the tags list (e.g. 'JohnVlogs', 'John Vlogs', 'John Daily')."
        ),
        explainVariantsHinglish = listOf(
            "Dost isse channel Search Results me aane lagta hai. Chrome browser me Desktop Site mark karke Studio settings kholo.",
            "Basic Info -> Keywords box me apne topic waale words comma (,) dekar daal do aur Save button daba do."
        )
    ),
    YouTubeStep(
        id = 6,
        title = "Account Verification",
        desc = "Verify phone number for Custom Thumbnails, Live Streaming & >15 min videos.",
        initialMessageHindi = "Phone Number Verification zaroori hai! Iske bina Custom Thumbnail nahi laga paoge.\n\n1. Settings -> Channel -> Feature Eligibility.\n2. Intermediate Features -> Verify Phone Number.\n3. OTP daal kar verify karo!",
        initialMessageEnglish = "Verify your Phone Number! Without this, you CANNOT upload custom thumbnails or live stream.\n\n1. Settings -> Channel -> Feature Eligibility.\n2. Intermediate Features -> Verify Phone Number.\n3. Enter phone number & verify with OTP!",
        initialMessageHinglish = "Phone verification karo varna Custom Thumbnail aur Live Streaming locked rahegi!\n\n1. YT Studio Settings ⚙️ -> Channel -> Feature Eligibility.\n2. Intermediate Features me 'Verify Phone Number' dabao.\n3. OTP dalkar instantly unlock karo!",
        smartQuestionHindi = "Phone number verify hoke Intermediate features active ho gaye?",
        smartQuestionEnglish = "Is intermediate feature eligibility enabled via phone OTP?",
        smartQuestionHinglish = "Phone number OTP se verify ho gaya?",
        explainVariantsHindi = listOf(
            "Bina verification ke aap Custom Thumbnail nahi laga sakte. Aur 90% views thumbnail dekh kar aate hain!",
            "Pehle ye unlock kar lo. Direct link: youtube.com/verify par jaakar bhi phone number OTP se verify kar sakte ho."
        ),
        explainVariantsEnglish = listOf(
            "Without phone verification, you can't use custom thumbnails. 90% of video clicks depend on strong thumbnails!",
            "Quick method: Visit youtube.com/verify on your mobile browser, enter your number, and input the SMS code."
        ),
        explainVariantsHinglish = listOf(
            "Bhai, bina custom thumbnail ke views nahi aayenge! Phone verify karte hi 15 minute se lambe videos aur custom thumbnails enable ho jaate hain.",
            "Ek OTP aayega phone pe. Enter karte hi 'Verified' ka green tick aa jayega."
        )
    ),
    YouTubeStep(
        id = 7,
        title = "YouTube Studio Mastery",
        desc = "Learn Analytics, CTR, Audience Retention & Monetization tabs.",
        initialMessageHindi = "YouTube Studio App ko samajhte hain!\n\nMain Sections:\n• Dashboard: Latest video performance\n• Content: All videos, Shorts, Live\n• Analytics: Views, Watch time, Subscribers, Retention\n• Earn: Monetization status & requirements",
        initialMessageEnglish = "Let's master YouTube Studio!\n\nMain Tabs:\n• Dashboard: Latest video stats\n• Content: Videos, Shorts, Playlists\n• Analytics: Views, Watch Time, CTR, Audience Retention\n• Earn: YPP eligibility & progress",
        initialMessageHinglish = "YouTube Studio app ka tour karte hain!\n\nMain Tabs:\n• Dashboard: Overall progress\n• Content: Long videos & Shorts list\n• Analytics: Kitne views, CTR & Watch time mila\n• Earn: Monetization kitna door hai",
        smartQuestionHindi = "Studio app ke sabhi tabs samajh aaye?",
        smartQuestionEnglish = "Do you understand the key sections of YT Studio?",
        smartQuestionHinglish = "YT Studio app explore karke dekh liya?",
        explainVariantsHindi = listOf(
            "2 Metrics yaad rakho:\n1. CTR (Click Through Rate) - Kitne logon ne thumbnail dekh kar click kiya.\n2. Audience Retention - Log aapka video kitni der tak dekh rahe hain.",
            "Agar CTR > 8% hai aur Average View Duration > 50% hai, toh video VIRAL hona pakka hai!"
        ),
        explainVariantsEnglish = listOf(
            "Focus on 2 core metrics:\n1. CTR (Click-Through Rate) - % of impressions that clicked.\n2. Average View Duration (Retention) - How long viewers stayed tuned.",
            "High CTR + High Retention = YouTube algorithm will push your video to millions!"
        ),
        explainVariantsHinglish = listOf(
            "Dost 2 formula yaad rakho:\nThumbnail Acha = High CTR (More Clicks)\nContent Acha = High Watch Time (More Retention)\n\nDono high honge toh video viral!"
        )
    ),
    YouTubeStep(
        id = 8,
        title = "First Video Idea & Scripting",
        desc = "Generate engaging video scripts with Hook, Content Flow & Call to Action.",
        initialMessageHindi = "Pehla video plan karte hain! Ek shaandar script chahiye.\n\nStructure:\n1. Hook (0-10 sec): Viewers ko hold karo\n2. Body: Main valuable information\n3. Call To Action (CTA): Like, Subscribe & Comment\n\nNeeche 'SCRIPT GENERATOR AI' tool use karke script generate karo!",
        initialMessageEnglish = "Let's plan your first video script!\n\nScript Formula:\n1. Hook (0-10 sec): Capture attention immediately\n2. Core Body: Provide value without filler\n3. Call To Action (CTA): Subscribe & comment\n\nUse our built-in 'SCRIPT GENERATOR AI' below!",
        initialMessageHinglish = "Ab pehle video ki script likhte hain!\n\nFormula:\n1. Hook (Pehle 10 second me curiosity jagao)\n2. Main Content (To-the-point baat karo)\n3. CTA (Subscribe karne ko kaho)\n\nNeeche 'Script Generator' button daba kar try karo!",
        smartQuestionHindi = "Script ready kar li ya Script AI use karna chahte ho?",
        smartQuestionEnglish = "Did you generate or write your script?",
        smartQuestionHinglish = "Script tayar hai ya Script AI se generate karni hai?",
        explainVariantsHindi = listOf(
            "Script bina bolne me atakne lagte hain. Script ka matlab puri line rattna nahi, balki key points ko bullet points me likhna hai.",
            "Neeche dekho, humne ek dedicated 'Script Generator AI' tool diya hai jisse aap Shorts ya Long videos ke liye script bana sakte ho!"
        ),
        explainVariantsEnglish = listOf(
            "Scripting prevents rambling. You don't need to memorize word-for-word, just outline key bullet points to guide your flow.",
            "Use our AI Script Generator tool in the action bar below to create instant viral scripts tailored to your niche!"
        ),
        explainVariantsHinglish = listOf(
            "Bhai, camera ke samne confuse na ho isliye bullet points tayar rakho. Pehle 10 second me viewer ko hooked kar do!",
            "Action bar me 'Script AI' button par click karo aur customized script generate kar lo."
        )
    ),
    YouTubeStep(
        id = 9,
        title = "Video Shooting & Audio Basics",
        desc = "Camera setup, natural lighting & crisp mic audio.",
        initialMessageHindi = "Shooting Quality Tips:\n\n1. Lighting: Window light ya Ring Light aage se aane do.\n2. Audio: Audio 70% video quality hoti hai! Collar mic (Boya M1 / Wireless) use karo.\n3. Camera Eye Contact: Lens par dekho, screen par nahi!",
        initialMessageEnglish = "Shooting Mastery:\n\n1. Lighting: Face the light source (window/ring light).\n2. Audio: Audio is 70% of video retention! Use a collar mic.\n3. Eye Contact: Look at the lens, not the phone screen!",
        initialMessageHinglish = "Video shoot karne ke golden rules:\n\n1. Light: Chehre pe roshni aane do (Khidki ya ring light).\n2. Voice: Mute room me shoot karo ya $5-$10 ka mic use karo.\n3. Eye Contact: Hamesha camera LENS me dekho!",
        smartQuestionHindi = "Lighting aur mic setup clear ho gaya?",
        smartQuestionEnglish = "Are your lighting and mic setup clear?",
        smartQuestionHinglish = "Lighting aur mic ki tips samajh aayi?",
        explainVariantsHindi = listOf(
            "Pro-Tip: Mobile camera lens ko saf kapde se pehle pooncho! Dhadhle lens se video dhundhla dikhta hai.",
            "Aapka room me jitna jyada furniture/curtains honge, utna kam echo (goonj) hoga."
        ),
        explainVariantsEnglish = listOf(
            "Pro Tip: Always wipe your phone camera lens clean before shooting! Fingerprints make videos look blurry.",
            "Minimize echo by shooting in a carpeted or curtained room with soft furniture."
        ),
        explainVariantsHinglish = listOf(
            "Dost, 4k camera se zaroori ACHA AUDIO aur CLEAR LIGHTING hai! Sasta mic bhi mobile ke in-built mic se 10x behtar hota hai.",
            "Lens hamesha ek baar saaf kar liya karo record karne se pehle."
        )
    ),
    YouTubeStep(
        id = 10,
        title = "Video Editing Mastery",
        desc = "CapCut, VN & Mobile Video Editing course options.",
        initialMessageHindi = "Editing se video me jaan aati hai!\n\nKey Editing Rules:\n• Cut dead pauses & mistakes\n• Add Auto Captions\n• Sound Effects (Whoosh, Pop) every 5-10 sec\n• Zoom In/Out keyframes\n\nKya aapko editing aati hai?",
        initialMessageEnglish = "Editing makes or breaks retention!\n\nKey Rules:\n• Trim dead silence & awkward pauses\n• Add animated subtitles\n• Add sound effects (pops, whooshes)\n• Use subtle zooms\n\nDo you know video editing?",
        initialMessageHinglish = "Editing se video viral hota hai!\n\nRules:\n• Beech ke pauses aur mistakes cut karo\n• Subtitles/Captions add karo\n• Sound effects (Whoosh, Pop) daalo\n\nKya aapko editing aati hai?",
        smartQuestionHindi = "Editing aati hai ya Video Editing Academy seekhna chahte ho?",
        smartQuestionEnglish = "Do you know editing or need our Video Editing Academy course?",
        smartQuestionHinglish = "Editing aati hai ya Video Editing Course kholna hai?",
        explainVariantsHindi = listOf(
            "Agar aapko CapCut ya VN Editor seekhna hai toh humari 'Video Editing Academy' ka option neeche available hai! Zero se seekh sakte ho.",
            "CapCut aur VN dono free hain aur mobile me best result dete hain."
        ),
        explainVariantsEnglish = listOf(
            "If you want to master CapCut or VN Editor, check out our Video Editing Academy section! Perfect for beginners.",
            "CapCut and VN are completely free and offer auto-captions, keyframes, and professional effects on mobile."
        ),
        explainVariantsHinglish = listOf(
            "Bhai tension mat lo! Agar editing nahi aati toh Action Bar me 'Editing Course' par click karo, CapCut aur VN seekh loge.",
            "Fast jumps, sound effects aur captions lagate hi retention 2x ho jata hai."
        )
    ),
    YouTubeStep(
        id = 11,
        title = "High CTR Thumbnail Design",
        desc = "Create high-converting, clickable thumbnails with bold fonts & contrast.",
        initialMessageHindi = "Thumbnail = 90% Success!\n\nThumbnail Rules:\n1. Maximum 3-4 Words Text in bold fonts.\n2. High contrast colors (Yellow, Red, Neon Green on Dark background).\n3. Expressive Face photo with big emotions.\n4. Clean background.",
        initialMessageEnglish = "Thumbnail = 90% Success!\n\nRules:\n1. Max 3-4 bold words.\n2. High contrast colors (Yellow/Bright Green on Dark bg).\n3. High emotion face reaction.\n4. Clean, uncluttered layout.",
        initialMessageHinglish = "Thumbnail bohot zaroori hai!\n\nRules:\n1. Bas 3-4 BOLD words likho.\n2. Bright colors (Pila/Hara/Laal) dark BG pe.\n3. Apni photo expressive face ke saath.\n4. Bilkul clear layout.",
        smartQuestionHindi = "Thumbnail design ke 4 golden rules samajh aaye?",
        smartQuestionEnglish = "Got the 4 golden rules of thumbnail design?",
        smartQuestionHinglish = "Thumbnail ka formula samajh aa gaya?",
        explainVariantsHindi = listOf(
            "Rule: Apne thumbnail par wahi mat likho jo Title me hai! Title + Thumbnail milkar ek story batayein.",
            "Example:\nTitle: 'How I Earned My First $100'\nThumbnail Text: 'PROFIT SECRETS 😱' + Screenshot."
        ),
        explainVariantsEnglish = listOf(
            "Golden Rule: Don't repeat the title on the thumbnail! Make the thumbnail complement the title.",
            "Example:\nTitle: 'I Tested a $10 Camera vs $10,000 Camera'\nThumbnail Text: 'IMPOSSIBLE DIFFERENCE! 😱'"
        ),
        explainVariantsHinglish = listOf(
            "Dost, mobile screen choti hoti hai! Isliye text chota-chota mat likho, 3 bade words likho jo door se padha jaaye.",
            "Neeche 'Thumbnail Guide' button daba kar poori checklist dekh sakte ho."
        )
    ),
    YouTubeStep(
        id = 12,
        title = "Catchy Title & Description AI",
        desc = "SEO Titles, Keywords, Chapters & Hashtags.",
        initialMessageHindi = "Title aur Description se YouTube SEO banta hai!\n\n• Title: Curiosity + Main Search Keyword (Max 60-70 chars).\n• Description: Pehle 2 lines me summary, links, chapters & hashtags (#Tech #YouTube).\n\nUse our Title & Description AI tools!",
        initialMessageEnglish = "Title & Description boost YouTube Search!\n\n• Title: High curiosity + target keyword (< 70 chars).\n• Description: Summary, timestamp chapters, links & tags.\n\nTry our Title & Description AI tools below!",
        initialMessageHinglish = "SEO Friendly Title aur Description likhte hain!\n\n• Title: Catchy aur searchable hona chahiye.\n• Description: Video ki summary, timestamps aur hashtags (#Tags).\n\nAction Bar me 'Title AI' aur 'Description AI' use karo!",
        smartQuestionHindi = "Title aur Description AI se generate kiye?",
        smartQuestionEnglish = "Did you generate your title and description?",
        smartQuestionHinglish = "Catchy title aur description ready kar diya?",
        explainVariantsHindi = listOf(
            "Title ka example: 'Earn $100/Day with AI (Step-by-Step Hindi Guide)'. Curiosity + Keyword dono hain!",
            "Action bar me 'Title AI' aur 'Description AI' par click karke automatic SEO-optimized options paayein."
        ),
        explainVariantsEnglish = listOf(
            "Example Title: 'Build a YouTube Channel in 10 Mins (2026 Strategy)'. Combines velocity + keyword!",
            "Tap 'Title AI' or 'Description AI' in the toolbar to get instant viral templates."
        ),
        explainVariantsHinglish = listOf(
            "Dost, Title me log search waale words dhalte hain. Description me social media links aur video me kya bataya hai wo likho.",
            "Aap 'Title AI' button dabakar 5 viral title ideas pa sakte ho!"
        )
    ),
    YouTubeStep(
        id = 13,
        title = "Perfect Video Upload Checklist",
        desc = "Step-by-step uploading via Studio app or Desktop web.",
        initialMessageHindi = "Upload Time Checklist!\n\nPublish karne se pehle ye check karo:\n✔ Custom Thumbnail uploaded\n✔ Title & Description set\n✔ Playlist selected\n✔ Audience: 'Not made for kids'\n✔ Tags added\n✔ Visibility: 'Unlisted' pehle rakho, 1-2 ghanter baad 'Public' karo!",
        initialMessageEnglish = "Before You Publish Checklist!\n\nEnsure:\n✔ Custom Thumbnail attached\n✔ SEO Title & Description\n✔ Added to Playlist\n✔ Not made for kids\n✔ Added Tags\n✔ Set to 'Unlisted' first, wait 1-2 hours then 'Public'!",
        initialMessageHinglish = "Upload Checklist!\n\nVideo Publish karne se pehle:\n✔ Custom Thumbnail lagaya\n✔ Title & Description dhyan se bhara\n✔ Not made for kids select kiya\n✔ Pehle 'Unlisted' karke save karo, 1 ghanter baad 'Public' karo!",
        smartQuestionHindi = "Before Publish Checklist complete hai?",
        smartQuestionEnglish = "Did you review the Pre-Publish checklist?",
        smartQuestionHinglish = "Checklist ke saare points tick kar liye?",
        explainVariantsHindi = listOf(
            "Unlisted kyu rakhna hai? Kyuki YouTube HD processing aur copyright checks me 15-30 minute leta hai. Direct public karne se initial quality low rehti hai.",
            "Neeche 'Pre-Publish Checklist' tool par click karke interactive checklist verify kar sakte ho!"
        ),
        explainVariantsEnglish = listOf(
            "Why upload as Unlisted first? Because YouTube needs 15-30 mins to process HD/4K quality and check copyright.",
            "Click 'Checklist' in the toolbar below to run through the pre-publish inspector!"
        ),
        explainVariantsHinglish = listOf(
            "Bhai, direct Public mat dabana! Pehle Unlisted karke save karo. 1 ghante baad jab HD green tick aa jaye tab Public karo.",
            "Isse video ki processing full clean hoti hai."
        )
    ),
    YouTubeStep(
        id = 14,
        title = "YPP & Monetization Roadmap",
        desc = "1,000 Subscribers + 4,000 Watch Hours or 10M Shorts Views.",
        initialMessageHindi = "Paise kab aur kaise milenge? (YPP Partner Program)\n\nRequirements:\n1. 1,000 Subscribers + 4,000 Public Watch Hours (Long Videos)\nOR\n2. 1,000 Subscribers + 10 Million Shorts Views (90 Days)\n\nIske baad Google AdSense link karke direct Bank account me paise aate hain!",
        initialMessageEnglish = "Monetization Roadmap (YouTube Partner Program)\n\nRequirements:\n1. 1,000 Subscribers + 4,000 Watch Hours\nOR\n2. 1,000 Subscribers + 10M Shorts Views\n\nLink Google AdSense to get paid directly into your Bank Account!",
        initialMessageHinglish = "Monetization complete guide!\n\nTarget:\n1. 1,000 Subscribers + 4,000 Watch Hours (Long Videos)\nYA\n2. 1,000 Subscribers + 10 Million Shorts Views\n\nIske baad Google AdSense link hoke seedha Bank me paise aate hain!",
        smartQuestionHindi = "Monetization ke goals aur AdSense setup samajh aaya?",
        smartQuestionEnglish = "Clear on the YPP thresholds and AdSense process?",
        smartQuestionHinglish = "1K Subs aur 4K Hours waala target samajh aaya?",
        explainVariantsHindi = listOf(
            "YouTube par kamayi ke 5 tareeqe hote hain:\n1. AdSense Ads\n2. Sponsorships\n3. Affiliate Links\n4. Super Chats / Memberships\n5. Brand Deals.",
            "Sponsorships se log AdSense se 5x zyaada kamate hain! Niche consistent rakho."
        ),
        explainVariantsEnglish = listOf(
            "5 Revenue Streams on YouTube:\n1. AdSense Ad Revenue\n2. Brand Sponsorships\n3. Affiliate Links\n4. Channel Memberships & Superchats\n5. Merch & Courses.",
            "Sponsorships often pay 3x to 5x more than AdSense alone!"
        ),
        explainVariantsHinglish = listOf(
            "Dost, AdSense ke alawa Brand Deals se sabse zyaada kamayi hoti hai! Bas ek category par tik kar 20-30 videos dalo.",
            "Neeche 'Monetization Guide' par click karke complete process explore kar sakte ho."
        )
    ),
    YouTubeStep(
        id = 15,
        title = "Post-Upload & 48-Hour Growth Analysis",
        desc = "First hour replies, Community posts & 48-hour analytics optimization.",
        initialMessageHindi = "Video upload karne ke baad kya karna hai?\n\n1. First 1 Hour: Har comment ka reply aur ❤️ do!\n2. Community Tab: Polls & post share karo.\n3. 48-Hour Analysis: Check CTR in YT Studio. Agar CTR < 4% hai, toh NAYA THUMBNAIL & TITLE lagao instantly!",
        initialMessageEnglish = "Post-Upload Growth Playbook!\n\n1. First 1 Hour: Reply & heart every comment!\n2. Community Tab: Share polls & teaser image.\n3. 48-Hour Check: If CTR < 4%, CHANGE Thumbnail & Title immediately!",
        initialMessageHinglish = "Upload ke baad viral karne ka tarika!\n\n1. Pehle 1 ghante me aane wale saare comments ka reply karo aur heart ❤️ do.\n2. 48 ghante baad YT Studio me CTR dekho. Agar CTR kam hai toh Thumbnail + Title BADAL DO!",
        smartQuestionHindi = "Post-upload 48-hour strategy clear hai?",
        smartQuestionEnglish = "Understand the 48-hour thumbnail swap strategy?",
        smartQuestionHinglish = "Upload ke baad ki strategy samajh aayi?",
        explainVariantsHindi = listOf(
            "Secret Hack: Agar video ke views ruk gaye hain 24 ghante me, toh Naya Thumbnail bana kar badal do! YouTube algorithm ise naye viewers ko dubara recommend karne lagta hai.",
            "Congratulation! Aapne YouTube Creator Course complete kar liya hai! Ab bas consistency ke saath kaam karo. Main aapka AI Mentor hamesha yahan hoon!"
        ),
        explainVariantsEnglish = listOf(
            "Secret Growth Hack: If views stall after 24 hours, SWAP the thumbnail & title! YouTube re-evaluates and re-tests the video with fresh audiences.",
            "Congratulations! You've mastered the Zero-to-Advanced YouTube roadmap. I am always here as your personal AI Mentor!"
        ),
        explainVariantsHinglish = listOf(
            "Secret Hack: Agar video chal nahi raha toh ghbrao mat! Thumbnail aur Title change karke dekho, algorithm firse promote karega.",
            "Badhai ho dost! Aapne YouTube Mentor Course poora kar liya! Ab regular upload karo. Koi bhi help chahiye ho toh pooch lo!"
        )
    )
)

@Composable
fun YouTubeCreatorAiV2Dialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // Memory State
    var selectedLang by remember {
        mutableStateOf(
            CreatorAcademyPrefs.getYouTubeLanguage(context)?.let {
                try { YouTubeLanguage.valueOf(it) } catch (e: Exception) { null }
            }
        )
    }
    var creatorType by remember {
        mutableStateOf(CreatorAcademyPrefs.getYouTubeCreatorType(context))
    }
    var currentStepId by remember {
        mutableIntStateOf(CreatorAcademyPrefs.getYouTubeCurrentStep(context))
    }

    // Step state tracking
    val completedSteps = remember {
        mutableStateListOf<Int>().apply {
            addAll(CreatorAcademyPrefs.getYouTubeCompletedSteps(context))
        }
    }

    // Interactive Dialog Overlays
    var activeToolOverlay by remember { mutableStateOf<String?>(null) }
    var showWelcomeBack by remember { mutableStateOf(selectedLang != null && creatorType != null && (currentStepId > 1 || completedSteps.isNotEmpty())) }
    var showRestartConfirm by remember { mutableStateOf(false) }

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
            if (showWelcomeBack && selectedLang != null && creatorType != null) {
                SmartWelcomeBackDialog(
                    courseTitle = "YouTube Creator Course",
                    currentStep = currentStepId,
                    totalSteps = YOUTUBE_ROADMAP_STEPS.size,
                    onContinue = { showWelcomeBack = false },
                    onRestart = { showRestartConfirm = true },
                    onDismiss = onDismiss
                )
            } else if (selectedLang == null) {
                // STEP 1: LANGUAGE SELECTION OVERLAY
                YouTubeLanguageSelectionOverlay(
                    onSelect = { lang ->
                        selectedLang = lang
                        CreatorAcademyPrefs.saveYouTubeLanguage(context, lang.name)
                    },
                    onClose = onDismiss
                )
            } else if (creatorType == null) {
                // STEP 2: CREATOR TYPE SELECTION OVERLAY
                YouTubeCreatorTypeSelectionOverlay(
                    selectedLanguage = selectedLang!!,
                    onSelect = { type ->
                        creatorType = type
                        CreatorAcademyPrefs.saveYouTubeCreatorType(context, type)
                    },
                    onBack = { selectedLang = null }
                )
            } else {
                // STEP 3: MAIN MENTOR CHAT INTERFACE
                YouTubeMentorChatScreen(
                    language = selectedLang!!,
                    creatorType = creatorType!!,
                    currentStepId = currentStepId,
                    completedSteps = completedSteps,
                    onStepChange = { newStep ->
                        currentStepId = newStep
                        CreatorAcademyPrefs.saveYouTubeCurrentStep(context, newStep)
                    },
                    onStepComplete = { stepId ->
                        if (!completedSteps.contains(stepId)) {
                            completedSteps.add(stepId)
                            CreatorAcademyPrefs.saveYouTubeCompletedSteps(context, completedSteps.toSet())
                        }
                    },
                    onChangeLangClick = { selectedLang = null },
                    onChangeTypeClick = { creatorType = null },
                    onOpenTool = { tool -> activeToolOverlay = tool },
                    onResetCourse = { showRestartConfirm = true },
                    onClose = onDismiss
                )
            }

            if (showRestartConfirm) {
                RestartCourseConfirmDialog(
                    courseTitle = "YouTube Creator Course",
                    onConfirmRestart = {
                        CreatorAcademyPrefs.resetCourseProgress(context, "youtube")
                        selectedLang = null
                        creatorType = null
                        currentStepId = 1
                        completedSteps.clear()
                        showWelcomeBack = false
                        showRestartConfirm = false
                    },
                    onDismiss = { showRestartConfirm = false }
                )
            }

            // TOOL OVERLAYS
            activeToolOverlay?.let { tool ->
                when (tool) {
                    "script" -> YouTubeScriptGeneratorDialog(
                        language = selectedLang ?: YouTubeLanguage.HINGLISH,
                        creatorType = creatorType ?: "Shorts Creator",
                        onDismiss = { activeToolOverlay = null }
                    )
                    "thumbnail" -> YouTubeThumbnailGuideDialog(
                        onDismiss = { activeToolOverlay = null }
                    )
                    "title" -> YouTubeTitleGeneratorDialog(
                        language = selectedLang ?: YouTubeLanguage.HINGLISH,
                        creatorType = creatorType ?: "Vlogger",
                        onDismiss = { activeToolOverlay = null }
                    )
                    "description" -> YouTubeDescriptionGeneratorDialog(
                        language = selectedLang ?: YouTubeLanguage.HINGLISH,
                        creatorType = creatorType ?: "Tech",
                        onDismiss = { activeToolOverlay = null }
                    )
                    "checklist" -> YouTubePrePublishChecklistDialog(
                        onDismiss = { activeToolOverlay = null }
                    )
                    "monetization" -> YouTubeMonetizationGuideDialog(
                        onDismiss = { activeToolOverlay = null }
                    )
                    "editing" -> VideoEditingAcademyLinkDialog(
                        onDismiss = { activeToolOverlay = null }
                    )
                }
            }
        }
}
}

// ============================================================================
// 1. LANGUAGE SELECTION OVERLAY
// ============================================================================
@Composable
private fun YouTubeLanguageSelectionOverlay(
    onSelect: (YouTubeLanguage) -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1F0505), AmoledBlack, Color(0xFF140303))
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFF180A0A))
                .border(
                    BorderStroke(
                        1.5.dp,
                        Brush.linearGradient(listOf(YouTubeRed, YouTubeRedGlow, EmeraldGlow))
                    ),
                    RoundedCornerShape(32.dp)
                )
                .padding(26.dp)
        ) {
            // Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                        .clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // YouTube Logo Header
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(YouTubeRed.copy(alpha = 0.2f))
                    .border(BorderStroke(1.5.dp, YouTubeRedGlow), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                OfficialLogo(
                    name = "youtube",
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "👋 Welcome Creator!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Aap kis language me seekhna chahenge?\nChoose your learning language:",
                fontSize = 13.5.sp,
                color = TextWhite.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            var selectedTemp by remember { mutableStateOf(YouTubeLanguage.HINGLISH) }

            val languages = listOf(
                YouTubeLanguage.HINDI to ("○ हिन्दी" to "Pure Hindi"),
                YouTubeLanguage.ENGLISH to ("○ English" to "Pure English"),
                YouTubeLanguage.HINGLISH to ("○ Hinglish" to "Hindi + English (Recommended)")
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                languages.forEach { (lang, labels) ->
                    val isSelected = selectedTemp == lang
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) Brush.horizontalGradient(listOf(YouTubeRed, YouTubeRedGlow))
                                else SolidColor(Color(0x18FFFFFF))
                            )
                            .border(
                                BorderStroke(
                                    if (isSelected) 1.5.dp else 1.dp,
                                    if (isSelected) YouTubeRedGlow else Color(0x33FFFFFF)
                                ),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedTemp = lang }
                            .padding(horizontal = 18.dp, vertical = 14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = labels.first,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = labels.second,
                                    fontSize = 11.5.sp,
                                    color = if (isSelected) TextWhite.copy(alpha = 0.9f) else TextWhite.copy(alpha = 0.5f)
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = TextWhite,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Continue Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(Brush.horizontalGradient(listOf(YouTubeRed, YouTubeRedGlow)))
                    .clickable { onSelect(selectedTemp) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CONTINUE ➔",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    letterSpacing = 0.8.sp
                )
            }
        }
    }
}

// ============================================================================
// 2. CREATOR TYPE SELECTION OVERLAY
// ============================================================================
@Composable
private fun YouTubeCreatorTypeSelectionOverlay(
    selectedLanguage: YouTubeLanguage,
    onSelect: (String) -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1F0505), AmoledBlack, Color(0xFF140303))
                )
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 500.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xFF180A0A))
                .border(
                    BorderStroke(1.5.dp, Brush.linearGradient(listOf(YouTubeRed, YouTubeRedGlow))),
                    RoundedCornerShape(28.dp)
                )
                .padding(22.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "← Back",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = YouTubeRedGlow,
                    modifier = Modifier.clickable { onBack() }
                )
                Text(
                    text = "STEP 2 OF 2",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = when (selectedLanguage) {
                    YouTubeLanguage.HINDI -> "Aap kis type ka YouTuber banna chahte hain?"
                    YouTubeLanguage.ENGLISH -> "What type of YouTuber do you want to become?"
                    YouTubeLanguage.HINGLISH -> "Aap kis type ka YouTuber banna chahte ho?"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Personalizing your YouTube growth roadmap...",
                fontSize = 12.sp,
                color = TextWhite.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(18.dp))

            var selectedTypeTemp by remember { mutableStateOf(YOUTUBE_CREATOR_TYPES.first().first) }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                items(YOUTUBE_CREATOR_TYPES) { (type, emoji) ->
                    val isSelected = selectedTypeTemp == type
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) Brush.horizontalGradient(listOf(YouTubeRed, YouTubeRedGlow))
                                else SolidColor(Color(0x18FFFFFF))
                            )
                            .border(
                                BorderStroke(
                                    if (isSelected) 1.5.dp else 1.dp,
                                    if (isSelected) YouTubeRedGlow else Color(0x22FFFFFF)
                                ),
                                RoundedCornerShape(14.dp)
                            )
                            .clickable { selectedTypeTemp = type }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = emoji, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = type,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = TextWhite,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Brush.horizontalGradient(listOf(YouTubeRed, YouTubeRedGlow)))
                    .clickable { onSelect(selectedTypeTemp) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "START YOUTUBE ROADMAP 🚀",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    letterSpacing = 0.8.sp
                )
            }
        }
    }
}

// ============================================================================
// 3. MAIN YOUTUBE MENTOR CHAT INTERFACE
// ============================================================================
@Composable
private fun YouTubeMentorChatScreen(
    language: YouTubeLanguage,
    creatorType: String,
    currentStepId: Int,
    completedSteps: List<Int>,
    onStepChange: (Int) -> Unit,
    onStepComplete: (Int) -> Unit,
    onChangeLangClick: () -> Unit,
    onChangeTypeClick: () -> Unit,
    onOpenTool: (String) -> Unit,
    onResetCourse: (() -> Unit)? = null,
    onClose: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val currentStep = YOUTUBE_ROADMAP_STEPS.find { it.id == currentStepId }
        ?: YOUTUBE_ROADMAP_STEPS.first()

    var explainCount by remember(currentStepId) { mutableIntStateOf(0) }

    // Messages history
    val messages = remember { mutableStateListOf<YouTubeChatMessage>() }

    // User text input
    var inputText by remember { mutableStateOf("") }

    // Load initial greeting or step message
    LaunchedEffect(currentStepId, language, creatorType) {
        messages.clear()

        val stepGreeting = when (language) {
            YouTubeLanguage.HINDI -> currentStep.initialMessageHindi
            YouTubeLanguage.ENGLISH -> currentStep.initialMessageEnglish
            YouTubeLanguage.HINGLISH -> currentStep.initialMessageHinglish
        }

        val smartQ = when (language) {
            YouTubeLanguage.HINDI -> currentStep.smartQuestionHindi
            YouTubeLanguage.ENGLISH -> currentStep.smartQuestionEnglish
            YouTubeLanguage.HINGLISH -> currentStep.smartQuestionHinglish
        }

        messages.add(
            YouTubeChatMessage(
                isFromUser = false,
                text = "🎯 STEP ${currentStep.id}/${YOUTUBE_ROADMAP_STEPS.size}: ${currentStep.title}\n\n$stepGreeting\n\n💬 $smartQ",
                quickReplies = listOf("✅ Ho gaya / Done", "❓ Dubara batao / Explain Again", "🛠️ Tools")
            )
        )
    }

    // Scroll to bottom when new messages arrive, typing occurs, or keyboard opens
    val imeBottomPadding = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
    LaunchedEffect(messages.size, inputText, imeBottomPadding) {
        if (messages.isNotEmpty()) {
            delay(60)
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    fun handleUserMsg(userInputText: String) {
        if (userInputText.isBlank()) return

        messages.add(YouTubeChatMessage(isFromUser = true, text = userInputText))
        inputText = ""

        val lower = userInputText.lowercase()

        coroutineScope.launch {
            delay(500) // Realistic typing delay

            val isDone = lower.contains("done") || lower.contains("ho gaya") || lower.contains("samajh aa gaya") || lower.contains("yes") || lower.contains("completed") || lower.contains("✅")
            val isExplainAgain = lower.contains("dubara") || lower.contains("explain") || lower.contains("nahi samjha") || lower.contains("again") || lower.contains("confused") || lower.contains("❓")

            if (isDone) {
                onStepComplete(currentStep.id)

                if (currentStep.id < YOUTUBE_ROADMAP_STEPS.size) {
                    val nextStepId = currentStep.id + 1
                    onStepChange(nextStepId)

                    val congrats = when (language) {
                        YouTubeLanguage.HINDI -> "Shabash! 🎉 STEP ${currentStep.id} complete ho gaya. Chalo ab next step karte hain!"
                        YouTubeLanguage.ENGLISH -> "Great job! 🎉 STEP ${currentStep.id} completed. Let's move to the next milestone!"
                        YouTubeLanguage.HINGLISH -> "Boht badhiya dost! 🎉 STEP ${currentStep.id} Done! Ab next step pe chalte hain."
                    }

                    messages.add(
                        YouTubeChatMessage(
                            isFromUser = false,
                            text = congrats,
                            quickReplies = listOf("Next Step ➔")
                        )
                    )
                } else {
                    val finalMsg = when (language) {
                        YouTubeLanguage.HINDI -> "🏆 CONGRATULATIONS! Aapne YouTube Creator ka Zero-to-Advanced roadmap poora kar liya! Ab consistency ke saath videos banao aur rock karo!"
                        YouTubeLanguage.ENGLISH -> "🏆 CONGRATULATIONS! You have completed the entire YouTube Creator Roadmap! Stay consistent and build your empire!"
                        YouTubeLanguage.HINGLISH -> "🏆 SHABASH CREATOR! Aapne poora YouTube Roadmap complete kar liya hai! Ab bas consistent raho aur viral raho!"
                    }
                    messages.add(YouTubeChatMessage(isFromUser = false, text = finalMsg))
                }
            } else if (isExplainAgain) {
                explainCount++

                val list = when (language) {
                    YouTubeLanguage.HINDI -> currentStep.explainVariantsHindi
                    YouTubeLanguage.ENGLISH -> currentStep.explainVariantsEnglish
                    YouTubeLanguage.HINGLISH -> currentStep.explainVariantsHinglish
                }

                val reExplainText = list.getOrElse((explainCount - 1) % list.size) { list.last() }

                val prefix = when (language) {
                    YouTubeLanguage.HINDI -> "Koi baat nahi dost! Main naye tarike se samjhaata hoon:\n\n"
                    YouTubeLanguage.ENGLISH -> "No worries at all! Here is a fresh, simpler explanation:\n\n"
                    YouTubeLanguage.HINGLISH -> "Koi tension nahi dost! Naye example se samjho:\n\n"
                }

                messages.add(
                    YouTubeChatMessage(
                        isFromUser = false,
                        text = prefix + reExplainText,
                        quickReplies = listOf("✅ Ho gaya / Done", "❓ Ek aur example do")
                    )
                )
            } else if (lower.contains("script")) {
                onOpenTool("script")
                messages.add(YouTubeChatMessage(isFromUser = false, text = "🎬 Script Generator AI opened! Customize duration and topic."))
            } else if (lower.contains("thumbnail")) {
                onOpenTool("thumbnail")
                messages.add(YouTubeChatMessage(isFromUser = false, text = "🖼️ High CTR Thumbnail Guide opened!"))
            } else if (lower.contains("title")) {
                onOpenTool("title")
                messages.add(YouTubeChatMessage(isFromUser = false, text = "🏷️ Title Generator AI opened!"))
            } else if (lower.contains("description")) {
                onOpenTool("description")
                messages.add(YouTubeChatMessage(isFromUser = false, text = "📝 Description & Tag Generator AI opened!"))
            } else if (lower.contains("checklist")) {
                onOpenTool("checklist")
                messages.add(YouTubeChatMessage(isFromUser = false, text = "✔ Pre-Publish Checklist opened!"))
            } else if (lower.contains("monetization")) {
                onOpenTool("monetization")
                messages.add(YouTubeChatMessage(isFromUser = false, text = "💰 Monetization Roadmap opened!"))
            } else if (lower.contains("editing")) {
                onOpenTool("editing")
                messages.add(YouTubeChatMessage(isFromUser = false, text = "🎬 Video Editing Academy opened!"))
            } else {
                val aiReply = com.example.creatoracademy.ViralAiMentorEngine.generateIntegratedMentorResponse(
                    domain = com.example.creatoracademy.MentorToolDomain.YOUTUBE_CREATOR_AI,
                    userQuery = userInputText,
                    userContext = "YouTube Step: ${currentStep.title}",
                    language = language.name
                )
                messages.add(
                    YouTubeChatMessage(
                        isFromUser = false,
                        text = aiReply,
                        quickReplies = listOf("✅ Done", "❓ Explain Again")
                    )
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0505))
    ) {
        // TOP HEADER BAR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1D0808), Color(0xFF120404))
                    )
                )
                .border(
                    BorderStroke(1.dp, YouTubeRed.copy(alpha = 0.4f)),
                    RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(YouTubeRed.copy(alpha = 0.2f))
                            .border(BorderStroke(1.dp, YouTubeRedGlow), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        OfficialLogo(
                            name = "youtube",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "YouTube Personal AI Mentor",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldGlow)
                            )
                        }
                        Text(
                            text = "$creatorType • ${language.name} • Step $currentStepId/${YOUTUBE_ROADMAP_STEPS.size}",
                            fontSize = 11.sp,
                            color = TextWhite.copy(alpha = 0.65f)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Language Switch Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x22FFFFFF))
                            .clickable { onChangeLangClick() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = TextWhite,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = language.name, fontSize = 10.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Close Button
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0x22FFFFFF))
                            .clickable { onClose() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextWhite, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        // STEP PROGRESS INDICATOR STRIP
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF140606))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(YOUTUBE_ROADMAP_STEPS) { step ->
                val isCompleted = completedSteps.contains(step.id)
                val isCurrent = step.id == currentStepId

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when {
                                isCurrent -> Brush.horizontalGradient(listOf(YouTubeRed, YouTubeRedGlow))
                                isCompleted -> SolidColor(Color(0x3310B981))
                                else -> SolidColor(Color(0x18FFFFFF))
                            }
                        )
                        .border(
                            BorderStroke(
                                1.dp,
                                when {
                                    isCurrent -> YouTubeRedGlow
                                    isCompleted -> EmeraldGlow
                                    else -> Color(0x22FFFFFF)
                                }
                            ),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onStepChange(step.id) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = EmeraldGlow,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = "S${step.id}",
                            fontSize = 11.sp,
                            fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold,
                            color = if (isCurrent || isCompleted) TextWhite else TextWhite.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        // LEARNING PROGRESS INDICATOR CARD
        LearningProgressIndicatorCard(
            currentStep = currentStepId,
            totalSteps = YOUTUBE_ROADMAP_STEPS.size,
            stepTitle = currentStep.title,
            onResetClick = onResetCourse,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
        )

        // ACTION TOOLS BAR
        YouTubeMentorActionBar(
            onOpenTool = onOpenTool
        )

        // CHAT MESSAGES AREA
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                YouTubeChatMessageBubble(
                    msg = msg,
                    onQuickReplyClick = { reply -> handleUserMsg(reply) }
                )
            }

            if (completedSteps.size >= YOUTUBE_ROADMAP_STEPS.size) {
                item {
                    CourseCompletionCard(
                        courseTitle = "YouTube Creator Masterclass",
                        skillsLearned = listOf(
                            "Viral Shorts & Long-Form Ideas",
                            "High-CTR Thumbnail Design",
                            "YouTube SEO & Algorithm Hacking",
                            "Channel Monetization & Sponsorships"
                        ),
                        onContinue = { onClose?.invoke() },
                        onResetCourse = { onResetCourse?.invoke() },
                        theme = ytTheme
                    )
                }
            }
        }

        // BOTTOM INPUT BAR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF140606))
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = when (language) {
                                YouTubeLanguage.HINDI -> "Poochho ya 'Done' bolo..."
                                YouTubeLanguage.ENGLISH -> "Ask or type 'Done'..."
                                YouTubeLanguage.HINGLISH -> "Poochho ya 'Done' bolo..."
                            },
                            fontSize = 13.sp,
                            color = TextWhite.copy(alpha = 0.4f)
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = YouTubeRedGlow,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedContainerColor = Color(0xFF1C0909),
                        unfocusedContainerColor = Color(0xFF1A0808),
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Brush.horizontalGradient(listOf(YouTubeRed, YouTubeRedGlow)))
                        .clickable { handleUserMsg(inputText) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// ============================================================================
// CHAT MESSAGE BUBBLE
// ============================================================================
@Composable
private fun YouTubeChatMessageBubble(
    msg: YouTubeChatMessage,
    onQuickReplyClick: (String) -> Unit
) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        if (!msg.isFromUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(YouTubeRed.copy(alpha = 0.2f))
                    .border(BorderStroke(1.dp, YouTubeRedGlow), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                OfficialLogo(
                    name = "youtube",
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (msg.isFromUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (msg.isFromUser) 18.dp else 4.dp,
                            bottomEnd = if (msg.isFromUser) 4.dp else 18.dp
                        )
                    )
                    .background(
                        if (msg.isFromUser) Brush.horizontalGradient(listOf(YouTubeRed, YouTubeRedDark))
                        else SolidColor(Color(0xFF1E0A0A))
                    )
                    .border(
                        BorderStroke(
                            1.dp,
                            if (msg.isFromUser) YouTubeRedGlow else Color(0x33FFFFFF)
                        ),
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (msg.isFromUser) 18.dp else 4.dp,
                            bottomEnd = if (msg.isFromUser) 4.dp else 18.dp
                        )
                    )
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = msg.text,
                        fontSize = 13.5.sp,
                        color = TextWhite,
                        lineHeight = 19.sp
                    )

                    if (!msg.isFromUser) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = TextWhite.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable {
                                        clipboard.setText(AnnotatedString(msg.text))
                                        Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                                    }
                            )
                        }
                    }
                }
            }

            // Quick reply chips
            if (msg.quickReplies.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    msg.quickReplies.forEach { reply ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (reply.contains("Done") || reply.contains("Ho gaya")) Brush.horizontalGradient(listOf(EmeraldPrimary, EmeraldGlow))
                                    else SolidColor(Color(0x33FFFFFF))
                                )
                                .border(
                                    BorderStroke(1.dp, if (reply.contains("Done") || reply.contains("Ho gaya")) EmeraldGlow else Color(0x44FFFFFF)),
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable { onQuickReplyClick(reply) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = reply,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                    }
                }
            }
        }
    }
}

// ============================================================================
// MENTOR ACTION TOOLS BAR
// ============================================================================
@Composable
private fun YouTubeMentorActionBar(
    onOpenTool: (String) -> Unit
) {
    val tools = listOf(
        Triple("script", "🎬 Script AI", YouTubeRedGlow),
        Triple("thumbnail", "🖼️ Thumbnail Guide", Color(0xFFFF9800)),
        Triple("title", "🏷️ Title AI", Color(0xFF38BDF8)),
        Triple("description", "📝 Description AI", Color(0xFFA855F7)),
        Triple("checklist", "✔ Checklist", EmeraldGlow),
        Triple("monetization", "💰 Monetization", Color(0xFFFFD700)),
        Triple("editing", "✂️ Editing Academy", Color(0xFF00B2FF))
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF170707))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tools) { (id, label, accentColor) ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(accentColor.copy(alpha = 0.15f))
                    .border(BorderStroke(1.dp, accentColor.copy(alpha = 0.6f)), RoundedCornerShape(14.dp))
                    .clickable { onOpenTool(id) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }
        }
    }
}

// ============================================================================
// TOOL 1: YOUTUBE SCRIPT GENERATOR DIALOG
// ============================================================================
@Composable
private fun YouTubeScriptGeneratorDialog(
    language: YouTubeLanguage,
    creatorType: String,
    onDismiss: () -> Unit
) {
    var topic by remember { mutableStateOf("") }
    var selectedVideoType by remember { mutableStateOf("Shorts") } // Shorts or Long Video
    var selectedDuration by remember { mutableStateOf("60s") }

    var generatedScript by remember { mutableStateOf<String?>(null) }
    var isGenerating by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    val durations = if (selectedVideoType == "Shorts") {
        listOf("15s", "30s", "60s")
    } else {
        listOf("3 min", "5 min", "8 min", "10 min", "20 min")
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .clickable(onClick = onDismiss)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth(0.94f)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(28.dp),
                color = Color(0xFF1C0A0A)
            ) {
                Column(
                    modifier = Modifier
                        .padding(22.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🎬", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "YouTube Script Generator AI",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextWhite,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onDismiss() }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Select Video Type: Shorts vs Long Video
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x22FFFFFF))
                        .padding(4.dp)
                ) {
                    listOf("Shorts", "Long Video").forEach { type ->
                        val isSel = selectedVideoType == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSel) Brush.horizontalGradient(listOf(YouTubeRed, YouTubeRedGlow))
                                    else SolidColor(Color.Transparent)
                                )
                                .clickable {
                                    selectedVideoType = type
                                    selectedDuration = if (type == "Shorts") "60s" else "5 min"
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (type == "Shorts") "⚡ Shorts (<60s)" else "🎬 Long Video",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("Video Topic or Title", color = TextWhite.copy(alpha = 0.7f)) },
                    placeholder = { Text("e.g. Top 5 AI Tools for Students", color = TextWhite.copy(alpha = 0.4f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = YouTubeRedGlow,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedContainerColor = Color(0xFF140505),
                        unfocusedContainerColor = Color(0xFF140505),
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Select Duration:",
                    fontSize = 12.sp,
                    color = TextWhite.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(durations) { dur ->
                        val isSel = selectedDuration == dur
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSel) Brush.horizontalGradient(listOf(YouTubeRed, YouTubeRedGlow))
                                    else SolidColor(Color(0x18FFFFFF))
                                )
                                .clickable { selectedDuration = dur }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(text = dur, fontSize = 11.5.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .clip(RoundedCornerShape(23.dp))
                        .background(
                            if (topic.isNotBlank()) Brush.horizontalGradient(listOf(YouTubeRed, YouTubeRedGlow))
                            else SolidColor(Color.Gray)
                        )
                        .clickable(enabled = topic.isNotBlank() && !isGenerating) {
                            isGenerating = true
                            coroutineScope.launch {
                                delay(1200)
                                generatedScript = generateYouTubeScript(topic, selectedVideoType, selectedDuration, creatorType, language)
                                isGenerating = false
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isGenerating) "GENERATING SCRIPT..." else "GENERATE SCRIPT ✨",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                }

                generatedScript?.let { script ->
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF100303))
                            .border(BorderStroke(1.dp, YouTubeRedGlow.copy(alpha = 0.5f)), RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        LazyColumn {
                            item {
                                Text(
                                    text = script,
                                    fontSize = 12.5.sp,
                                    color = TextWhite,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🔄 Generate Another Variation",
                            fontSize = 11.5.sp,
                            color = YouTubeRedGlow,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                isGenerating = true
                                coroutineScope.launch {
                                    delay(1000)
                                    generatedScript = generateYouTubeScript(topic, selectedVideoType, selectedDuration, creatorType, language)
                                    isGenerating = false
                                }
                            }
                        )

                        Text(
                            text = "📋 Copy Script",
                            fontSize = 11.5.sp,
                            color = EmeraldGlow,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                clipboard.setText(AnnotatedString(script))
                                Toast.makeText(context, "Script copied!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}
}

private fun generateYouTubeScript(
    topic: String,
    videoType: String,
    duration: String,
    creatorType: String,
    language: YouTubeLanguage
): String {
    val isShorts = videoType == "Shorts"
    return if (isShorts) {
        """
⚡ YOUTUBE SHORTS SCRIPT ($duration)
Category: $creatorType | Topic: $topic

🔥 HOOK (0-5 sec):
"Ruko! Kya aap bhi $topic me ye galti kar rahe ho?"

💡 MAIN BODY (5-${if (duration == "15s") "12" else "45"} sec):
"Point 1: Pehle ye simple trick follow karo.
Point 2: Doosri baat, kabhi bhi bina research ke start mat karo.
Point 3: Best outcome ke liye ye tool zaroor use karo!"

📢 CALL TO ACTION (${if (duration == "15s") "12-15" else "45-60"} sec):
"Aise hi viral $creatorType tips ke liye SUBSCRIBE dabana mat bhoolna!"
        """.trimIndent()
    } else {
        """
🎬 YOUTUBE LONG VIDEO SCRIPT ($duration)
Category: $creatorType | Topic: $topic

📌 INTRO HOOK (0-30 sec):
"Dosto, aaj ke video me hum baat karenge $topic ke baare me! Is video ko end tak dekhne ke baad aapko kisi aur tutorial ki zaroorat nahi padegi."

💡 CHAPTER 1: THE FOUNDATION (1-3 min):
"Sabse pehle samajhte hain basic structure..."

🚀 CHAPTER 2: STEP-BY-STEP EXECUTION (3-6 min):
"Ab aate hain main secret strategy par..."

⭐ PRO TIPS & COMMON MISTAKES (6-8 min):
"Ye 2 mistakes bohot log karte hain..."

🔔 OUTRO & CTA:
"Agar video se value mili ho toh LIKE aur SUBSCRIBE zaroor kijiye! Next video kis topic par chahiye comment karke batao."
        """.trimIndent()
    }
}

// ============================================================================
// TOOL 2: THUMBNAIL GUIDE DIALOG
// ============================================================================
@Composable
private fun YouTubeThumbnailGuideDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .clickable(onClick = onDismiss)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth(0.94f)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1A0A0A)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🖼️ High CTR Thumbnail Formula",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextWhite,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onDismiss() }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                val tips = listOf(
                    "1. Max 3-4 Words Text: Bold font (e.g. Impact, Montserrat, Anton).",
                    "2. High Contrast Colors: Yellow/Bright Green text on Dark/Black BG.",
                    "3. Emotional Face: High surprise, shock or smile expression.",
                    "4. Rule of Thirds: Left side text, Right side face/main subject.",
                    "5. CTR Target: Aim for >8% Click Through Rate in Studio Analytics."
                )

                tips.forEach { tip ->
                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = YouTubeRedGlow,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = tip,
                            fontSize = 12.5.sp,
                            color = TextWhite,
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(21.dp))
                        .background(Brush.horizontalGradient(listOf(YouTubeRed, YouTubeRedGlow)))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "GOT IT!", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }
        }
    }
}
}

// ============================================================================
// TOOL 3: TITLE GENERATOR DIALOG
// ============================================================================
@Composable
private fun YouTubeTitleGeneratorDialog(
    language: YouTubeLanguage,
    creatorType: String,
    onDismiss: () -> Unit
) {
    var topic by remember { mutableStateOf("") }
    var generatedTitles by remember { mutableStateOf<List<String>>(emptyList()) }

    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .clickable(onClick = onDismiss)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth(0.94f)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1A0A0A)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "🏷️ Viral Title Generator AI",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    placeholder = { Text("Enter topic (e.g. Budget Camera)", color = TextWhite.copy(alpha = 0.4f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = YouTubeRedGlow,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedContainerColor = Color(0xFF100303),
                        unfocusedContainerColor = Color(0xFF100303),
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(21.dp))
                        .background(
                            if (topic.isNotBlank()) Brush.horizontalGradient(listOf(YouTubeRed, YouTubeRedGlow))
                            else SolidColor(Color.Gray)
                        )
                        .clickable(enabled = topic.isNotBlank()) {
                            generatedTitles = listOf(
                                "I Tried $topic for 30 Days (SHOCKING RESULTS! 😱)",
                                "How to Master $topic in 2026 (Step-by-Step Guide)",
                                "Don't Buy $topic Until You Watch This!",
                                "5 Secret $topic Hacks Nobody Tells You",
                                "The Ultimate $topic Strategy ($creatorType Guide)"
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "GENERATE TITLES ✨", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }

                if (generatedTitles.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        generatedTitles.forEach { title ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x22FFFFFF))
                                    .clickable {
                                        clipboard.setText(AnnotatedString(title))
                                        Toast.makeText(context, "Title copied!", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = title,
                                        fontSize = 12.sp,
                                        color = TextWhite,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = null,
                                        tint = YouTubeRedGlow,
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
}
}

// ============================================================================
// TOOL 4: DESCRIPTION & TAG GENERATOR DIALOG
// ============================================================================
@Composable
private fun YouTubeDescriptionGeneratorDialog(
    language: YouTubeLanguage,
    creatorType: String,
    onDismiss: () -> Unit
) {
    var topic by remember { mutableStateOf("") }
    var generatedDesc by remember { mutableStateOf<String?>(null) }

    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .clickable(onClick = onDismiss)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth(0.94f)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1A0A0A)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "📝 Description & Tag Generator",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    placeholder = { Text("Topic name...", color = TextWhite.copy(alpha = 0.4f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = YouTubeRedGlow,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedContainerColor = Color(0xFF100303),
                        unfocusedContainerColor = Color(0xFF100303),
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(21.dp))
                        .background(
                            if (topic.isNotBlank()) Brush.horizontalGradient(listOf(YouTubeRed, YouTubeRedGlow))
                            else SolidColor(Color.Gray)
                        )
                        .clickable(enabled = topic.isNotBlank()) {
                            generatedDesc = """
Welcome to the channel! In this video, we explore $topic step-by-step. Make sure to subscribe for more $creatorType content!

📌 TIMESTAMPS:
00:00 - Introduction
01:15 - Key Strategy 1
03:30 - Key Strategy 2
06:00 - Conclusion & Outro

🔗 CONNECT WITH ME:
Instagram: @yourhandle
Business Inquiry: yourname@email.com

🏷️ SEO TAGS:
$topic, $topic 2026, $topic guide, $creatorType tips, how to $topic, $topic hindi

#$creatorType #YouTubeGrowth #$topic
                            """.trimIndent()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "GENERATE DESCRIPTION 📝", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }

                generatedDesc?.let { desc ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF100303))
                            .padding(10.dp)
                    ) {
                        LazyColumn {
                            item { Text(text = desc, fontSize = 11.5.sp, color = TextWhite) }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "📋 Copy Description",
                        fontSize = 12.sp,
                        color = EmeraldGlow,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            clipboard.setText(AnnotatedString(desc))
                            Toast.makeText(context, "Description copied!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}
}

// ============================================================================
// TOOL 5: PRE-PUBLISH CHECKLIST DIALOG
// ============================================================================
@Composable
private fun YouTubePrePublishChecklistDialog(onDismiss: () -> Unit) {
    val items = remember {
        mutableStateListOf(
            "Custom HD Thumbnail attached" to true,
            "Clickable & SEO Title set (<70 chars)" to true,
            "Description with timestamps & links" to false,
            "Added to Relevant Playlist" to false,
            "Audience: 'Not made for kids' selected" to false,
            "10-15 Target SEO Tags added" to false,
            "End Screen & Cards added" to false,
            "Visibility set to 'Unlisted' first" to false
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .clickable(onClick = onDismiss)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth(0.94f)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1A0A0A)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "✔ Pre-Publish Inspector",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(12.dp))

                items.forEachIndexed { idx, pair ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = pair.second,
                            onCheckedChange = { checked ->
                                items[idx] = pair.first to checked
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = YouTubeRedGlow,
                                uncheckedColor = Color(0x55FFFFFF)
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = pair.first,
                            fontSize = 12.5.sp,
                            color = TextWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(21.dp))
                        .background(Brush.horizontalGradient(listOf(YouTubeRed, YouTubeRedGlow)))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "READY TO PUBLISH 🚀", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }
        }
    }
}
}

// ============================================================================
// TOOL 6: MONETIZATION GUIDE DIALOG
// ============================================================================
@Composable
private fun YouTubeMonetizationGuideDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .clickable(onClick = onDismiss)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth(0.94f)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1A0A0A)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "💰 Monetization Roadmap (YPP)",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(12.dp))

                val steps = listOf(
                    "1. Milestone 1: 500 Subscribers + 3,000 Watch Hours (Unlocks Memberships & Superchats).",
                    "2. Milestone 2: 1,000 Subscribers + 4,000 Watch Hours OR 10M Shorts Views (Unlocks Ad Revenue).",
                    "3. Link Google AdSense Account in YT Studio -> Earn tab.",
                    "4. Address Verification Pin sent to your home.",
                    "5. Monthly Payout direct to your Bank Account on 21st of every month."
                )

                steps.forEach { step ->
                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = step, fontSize = 12.sp, color = TextWhite, lineHeight = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(21.dp))
                        .background(Brush.horizontalGradient(listOf(YouTubeRed, YouTubeRedGlow)))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "UNDERSTOOD!", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }
        }
    }
}
}

// ============================================================================
// LINK TO VIDEO EDITING ACADEMY DIALOG
// ============================================================================
@Composable
private fun VideoEditingAcademyLinkDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .clickable(onClick = onDismiss)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth(0.94f)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1A0A0A)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "✂️ Video Editing Academy",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Master mobile video editing with CapCut, VN Editor & Instagram Edits in our dedicated Video Editing section!",
                    fontSize = 13.sp,
                    color = TextWhite.copy(alpha = 0.8f),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Brush.horizontalGradient(listOf(Color(0xFF00B2FF), Color(0xFF38BDF8))))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "OPEN EDITING COURSES ➔", fontSize = 13.sp, fontWeight = FontWeight.Black, color = TextWhite)
                }
            }
        }
    }
}
}
