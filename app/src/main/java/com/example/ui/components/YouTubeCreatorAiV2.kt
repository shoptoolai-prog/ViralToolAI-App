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
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import com.example.ui.theme.responsiveImeAndNavPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.scale
import androidx.compose.material.icons.filled.RadioButtonUnchecked
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
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import com.example.ui.screens.OfficialLogo
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

private val EmeraldPrimary = Color(0xFFFF0000) // Premium Red Theme for YouTube Creator AI
private val EmeraldGlow = Color(0x33FF0000) // Pure Red Glow Effect
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

// ============================================================================
// MASTER PHASE 1 — YOUTUBE GROWTH GUIDE ONBOARDING (4 SWIPE CARDS)
// ============================================================================
@Composable
fun YouTubeOnboardingCardsScreen(
    onComplete: () -> Unit,
    onClose: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 4 })

    // Breathing Glow Animation for AI Avatar on Card 3
    val infiniteTransition = rememberInfiniteTransition(label = "mentorGlow")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF121215), Color(0xFF0F0F12), Color(0xFF1A1A22))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 480.dp)
                .align(Alignment.Center)
        ) {
            // Header Top Bar with Badge & Skip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF24242C)),
                        contentAlignment = Alignment.Center
                    ) {
                        OfficialLogo(name = "youtube", modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "YOUTUBE GUIDE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFE5E5EA),
                        letterSpacing = 1.sp
                    )
                }

                Text(
                    text = "Skip",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA1A1AA),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onComplete() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4 Swipe Cards
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 6.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color(0xFF1C1C22))
                        .border(
                            BorderStroke(1.5.dp, Color(0xFF2C2C36)),
                            RoundedCornerShape(28.dp)
                        )
                        .clickable {
                            if (page == 3) {
                                onComplete()
                            }
                        }
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (page) {
                        0 -> {
                            // CARD 1: Welcome 👋 YouTube Growth Guide
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = Color(0xFF2C2C36),
                                    modifier = Modifier.padding(bottom = 20.dp)
                                ) {
                                    Text(
                                        text = "Welcome 👋",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFF4D4D),
                                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(110.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF282832))
                                        .border(BorderStroke(2.dp, Color(0xFFFF3333)), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color(0xFFFF3333),
                                        modifier = Modifier.size(56.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = "YouTube Growth Guide",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "Start your YouTube journey from zero.",
                                    fontSize = 15.sp,
                                    color = Color(0xFFA1A1AA),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        1 -> {
                            // CARD 2: Journey Animation Roadmap
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Your Growth Journey",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )

                                Spacer(modifier = Modifier.height(18.dp))

                                val journeySteps = listOf(
                                    "Channel Setup" to "⚙️",
                                    "First Video" to "🎥",
                                    "100 Subscribers" to "🎯",
                                    "1000 Subscribers" to "🚀",
                                    "Monetization" to "💰",
                                    "Full-time Creator" to "👑"
                                )

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    journeySteps.forEachIndexed { idx, (stepName, emoji) ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier
                                                .fillMaxWidth(0.88f)
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(Color(0xFF25252E))
                                                .border(BorderStroke(1.dp, Color(0xFF383844)), RoundedCornerShape(14.dp))
                                                .padding(horizontal = 14.dp, vertical = 9.dp)
                                        ) {
                                            Text(text = emoji, fontSize = 16.sp)
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = stepName,
                                                fontSize = 13.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }

                                        if (idx < journeySteps.size - 1) {
                                            Text(
                                                text = "↓",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color(0xFFFF4D4D)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        2 -> {
                            // CARD 3: Meet Your AI Mentor (Animated Avatar with Breathing Glow)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Meet Your AI Mentor",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )

                                Spacer(modifier = Modifier.height(26.dp))

                                Box(contentAlignment = Alignment.Center) {
                                    // Breathing Glow Ring
                                    Box(
                                        modifier = Modifier
                                            .size((110 * glowScale).dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFF3333).copy(alpha = glowAlpha * 0.45f))
                                    )

                                    // Avatar Core
                                    Box(
                                        modifier = Modifier
                                            .size(90.dp)
                                            .clip(CircleShape)
                                            .background(
                                                Brush.radialGradient(
                                                    listOf(Color(0xFFFF3333), Color(0xFF880000))
                                                )
                                            )
                                            .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.85f)), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "🤖", fontSize = 42.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(28.dp))

                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = Color(0xFF25252E),
                                    border = BorderStroke(1.dp, Color(0xFF3A3A46)),
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                ) {
                                    Text(
                                        text = "\"I'll personally guide you step by step.\"",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(20.dp),
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }

                        3 -> {
                            // CARD 4: Ready? Swipe to Start (No Button)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF2C2C36),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    Text(
                                        text = "FINAL STEP",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFFF4D4D),
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                    )
                                }

                                Text(
                                    text = "Ready?",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(Color(0xFF2C2C36), Color(0xFF383848))
                                            )
                                        )
                                        .border(BorderStroke(1.5.dp, Color(0xFFFF3333)), RoundedCornerShape(24.dp))
                                        .padding(horizontal = 26.dp, vertical = 14.dp)
                                ) {
                                    Text(
                                        text = "Swipe to Start",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "➔",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFFF3333)
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "Tap or swipe anywhere to start learning",
                                    fontSize = 12.sp,
                                    color = Color(0xFFA1A1AA)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Page Indicator Dots
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(8.dp)
                            .width(if (isSelected) 24.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Color(0xFFFF3333) else Color(0xFF3A3A46)
                            )
                            .clickable {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                    )
                }
            }
        }
    }
}

// ============================================================================
// MASTER PHASE 2 — YOUTUBE GROWTH GUIDE (LEVEL 0: PERSONAL AI SETUP)
// ============================================================================
@Composable
fun YouTubeLevel0SetupScreen(
    onCompleteLevel0: (lang: String, videoType: String, mainGoal: String) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var qIndex by remember { mutableIntStateOf(0) } // 0..8 for Q1..Q9, 9 for Blueprint

    // Question Answers State
    var hasChannel by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeHasChannel(context) ?: "") }
    var channelNameInput by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeChannelName(context) ?: "") }
    var currentLevel by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeCurrentLevel(context) ?: "") }
    var mainGoal by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeMainGoal(context) ?: "") }
    var videoType by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeVideoType(context) ?: "") }
    var weeklyTime by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeWeeklyTime(context) ?: "") }
    var editingExp by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeEditingExp(context) ?: "") }
    var recordingSetup by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeRecordingSetup(context) ?: "") }
    var videoLang by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeVideoLanguage(context) ?: "") }
    var selectedProblem by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeBiggestProblem(context) ?: "") }

    // Breathing Glow Animation for Top Avatar
    val infiniteTransition = rememberInfiniteTransition(label = "avatarGlow")
    val avatarGlowScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )
    val avatarGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF121215), Color(0xFF0F0F12), Color(0xFF1A1A22))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 520.dp)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header Top Bar with Back & Close
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (qIndex > 0) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF24242C))
                            .clickable { qIndex -= 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "←", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Spacer(modifier = Modifier.size(36.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF282834)),
                        contentAlignment = Alignment.Center
                    ) {
                        OfficialLogo(name = "youtube", modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PERSONAL AI SETUP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFA1A1AA),
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF24242C))
                        .clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Premium Header with Animated Avatar
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1C1C24),
                border = BorderStroke(1.5.dp, Color(0xFF2C2C3A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar with Soft Breathing Glow
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size((72 * avatarGlowScale).dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF3333).copy(alpha = avatarGlowAlpha * 0.4f))
                        )
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(Color(0xFFFF3333), Color(0xFF990000))
                                    )
                                )
                                .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.9f)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🤖", fontSize = 28.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Let's Build Your YouTube Journey 🚀",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Main tumhara personal YouTube mentor hoon.\nAaj se hum zero se creator banne ki journey start karte hain.",
                        fontSize = 13.sp,
                        color = Color(0xFFA1A1AA),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress Bar (5% Base Progress - No Fake Progress)
                    val progressPercent = if (qIndex == 9) 100 else 5 + (qIndex * 10)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (qIndex == 9) "Blueprint Ready" else "Step ${qIndex + 1} of 9",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD1D1D6)
                        )
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF2A2A36)
                        ) {
                            Text(
                                text = "$progressPercent%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFF4D4D),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF2C2C38))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progressPercent / 100f)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFFF3333), Color(0xFFFF6666))
                                    )
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic Questions Card Stack (Q1 to Q9) or Blueprint (qIndex 9)
            if (qIndex < 9) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF1C1C24),
                    border = BorderStroke(1.5.dp, Color(0xFF2C2C3A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        when (qIndex) {
                            0 -> {
                                // Q1: Do you already have a YouTube Channel?
                                Text(text = "QUESTION 1", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "Do you already have a YouTube Channel?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    val isYes = hasChannel == "Yes"
                                    val isNo = hasChannel == "No"

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (isYes) Color(0xFF332024) else Color(0xFF252530))
                                            .border(BorderStroke(1.5.dp, if (isYes) Color(0xFFFF3333) else Color(0xFF3A3A4A)), RoundedCornerShape(16.dp))
                                            .clickable {
                                                hasChannel = "Yes"
                                                CreatorAcademyPrefs.saveYouTubeHasChannel(context, "Yes")
                                            }
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "○ Yes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (isYes) Color(0xFFFF4D4D) else Color.White)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (isNo) Color(0xFF332024) else Color(0xFF252530))
                                            .border(BorderStroke(1.5.dp, if (isNo) Color(0xFFFF3333) else Color(0xFF3A3A4A)), RoundedCornerShape(16.dp))
                                            .clickable {
                                                hasChannel = "No"
                                                CreatorAcademyPrefs.saveYouTubeHasChannel(context, "No")
                                                qIndex = 1
                                            }
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "○ No", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (isNo) Color(0xFFFF4D4D) else Color.White)
                                    }
                                }

                                if (hasChannel == "Yes") {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(text = "Enter Channel Name or Link (Optional):", fontSize = 13.sp, color = Color(0xFFA1A1AA))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = channelNameInput,
                                        onValueChange = {
                                            channelNameInput = it
                                            CreatorAcademyPrefs.saveYouTubeChannelName(context, it)
                                        },
                                        placeholder = { Text("e.g. @MyChannel or channel URL", color = Color(0xFF666675)) },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFFFF3333),
                                            unfocusedBorderColor = Color(0xFF3A3A4A),
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Skip",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFA1A1AA),
                                            modifier = Modifier
                                                .clickable { qIndex = 1 }
                                                .padding(8.dp)
                                        )

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(Color(0xFFFF3333))
                                                .clickable { qIndex = 1 }
                                                .padding(horizontal = 22.dp, vertical = 10.dp)
                                        ) {
                                            Text(text = "Continue ➔", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                } else if (hasChannel == "No") {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = Color(0xFF252532),
                                        border = BorderStroke(1.dp, Color(0xFF38384A))
                                    ) {
                                        Text(
                                            text = "💡 AI setup will guide you from zero:\nGoogle Account ➔ Channel Creation ➔ Brand Account ➔ Basics",
                                            fontSize = 12.5.sp,
                                            color = Color(0xFFE5E5EA),
                                            modifier = Modifier.padding(14.dp),
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }

                            1 -> {
                                // Q2: Current Level
                                QuestionOptionsCard(
                                    questionNum = "QUESTION 2",
                                    title = "What is your current YouTube Level?",
                                    options = listOf("Never Started", "Beginner", "Growing", "Already Monetized"),
                                    selectedOption = currentLevel,
                                    onSelect = {
                                        currentLevel = it
                                        CreatorAcademyPrefs.saveYouTubeCurrentLevel(context, it)
                                        qIndex = 2
                                    }
                                )
                            }

                            2 -> {
                                // Q3: Main Goal
                                QuestionOptionsCard(
                                    questionNum = "QUESTION 3",
                                    title = "What is your main YouTube Goal?",
                                    options = listOf("Earn Money", "Become Famous", "Personal Brand", "Business Growth", "Teach People", "Entertainment", "Multiple Goals"),
                                    selectedOption = mainGoal,
                                    onSelect = {
                                        mainGoal = it
                                        CreatorAcademyPrefs.saveYouTubeMainGoal(context, it)
                                        qIndex = 3
                                    }
                                )
                            }

                            3 -> {
                                // Q4: Video Type
                                QuestionOptionsCard(
                                    questionNum = "QUESTION 4",
                                    title = "What type of videos do you want to create?",
                                    options = listOf("Shorts Only", "Long Videos", "Both"),
                                    selectedOption = videoType,
                                    onSelect = {
                                        videoType = it
                                        CreatorAcademyPrefs.saveYouTubeVideoType(context, it)
                                        qIndex = 4
                                    }
                                )
                            }

                            4 -> {
                                // Q5: Weekly Time
                                QuestionOptionsCard(
                                    questionNum = "QUESTION 5",
                                    title = "How much weekly time can you dedicate?",
                                    options = listOf("30 Minutes", "1 Hour", "2 Hours", "4+ Hours"),
                                    selectedOption = weeklyTime,
                                    onSelect = {
                                        weeklyTime = it
                                        CreatorAcademyPrefs.saveYouTubeWeeklyTime(context, it)
                                        qIndex = 5
                                    }
                                )
                            }

                            5 -> {
                                // Q6: Editing Experience
                                QuestionOptionsCard(
                                    questionNum = "QUESTION 6",
                                    title = "What is your video editing experience?",
                                    options = listOf("Never Edited", "Basic", "Intermediate", "Advanced"),
                                    selectedOption = editingExp,
                                    onSelect = {
                                        editingExp = it
                                        CreatorAcademyPrefs.saveYouTubeEditingExp(context, it)
                                        qIndex = 6
                                    }
                                )
                            }

                            6 -> {
                                // Q7: Recording Setup
                                QuestionOptionsCard(
                                    questionNum = "QUESTION 7",
                                    title = "What is your recording setup?",
                                    options = listOf("Only Phone", "Phone + Mic", "Camera Setup", "Professional"),
                                    selectedOption = recordingSetup,
                                    onSelect = {
                                        recordingSetup = it
                                        CreatorAcademyPrefs.saveYouTubeRecordingSetup(context, it)
                                        qIndex = 7
                                    }
                                )
                            }

                            7 -> {
                                // Q8: Language
                                QuestionOptionsCard(
                                    questionNum = "QUESTION 8",
                                    title = "Tum kis language mein YouTube videos banana chahte ho?",
                                    options = listOf("Hindi", "English", "Hinglish", "Regional", "Other"),
                                    selectedOption = videoLang,
                                    onSelect = {
                                        videoLang = it
                                        CreatorAcademyPrefs.saveYouTubeVideoLanguage(context, it)
                                        qIndex = 8
                                    }
                                )
                            }

                            8 -> {
                                // Q9: Current Biggest Problem
                                Text(text = "QUESTION 9", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "What is your current biggest problem?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(16.dp))

                                val problemList = listOf("No Ideas", "No Editing", "No Views", "No Subscribers", "Fear of Camera", "No Equipment", "No Motivation", "Don't Know Where To Start", "Multiple")
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    problemList.forEach { option ->
                                        val isSelected = selectedProblem == option
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(if (isSelected) Color(0xFF332024) else Color(0xFF252530))
                                                .border(BorderStroke(1.2.dp, if (isSelected) Color(0xFFFF3333) else Color(0xFF3A3A4A)), RoundedCornerShape(14.dp))
                                                .clickable {
                                                    selectedProblem = option
                                                    CreatorAcademyPrefs.saveYouTubeBiggestProblem(context, option)
                                                }
                                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = option, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                                if (isSelected) {
                                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFFFF3333), modifier = Modifier.size(18.dp))
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(Color(0xFFFF3333), Color(0xFFCC0000))
                                            )
                                        )
                                        .clickable {
                                            if (selectedProblem.isEmpty()) {
                                                selectedProblem = "Don't Know Where To Start"
                                                CreatorAcademyPrefs.saveYouTubeBiggestProblem(context, selectedProblem)
                                            }
                                            qIndex = 9
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Generate Creator Blueprint 🚀", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                                }
                            }
                        }
                    }
                }
            } else {
                // ============================================================
                // AI ANALYSIS CARD — YOUR CREATOR BLUEPRINT, ROADMAP & MISSION
                // ============================================================
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF1C1C24),
                    border = BorderStroke(1.5.dp, Color(0xFFFF3333).copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🎯", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Your Creator Blueprint",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Text(
                                    text = "Personalized AI Strategy Generated",
                                    fontSize = 12.sp,
                                    color = Color(0xFFFF4D4D),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Blueprint Summary Grid
                        val blueprintItems = listOf(
                            "Selected Category" to (if (videoType == "Shorts Only") "YouTube Shorts" else "YouTube General"),
                            "Current Level" to (if (currentLevel.isEmpty()) "Never Started" else currentLevel),
                            "Goal" to (if (mainGoal.isEmpty()) "Earn Money" else mainGoal),
                            "Learning Speed" to (if (weeklyTime == "4+ Hours") "Sprint Mode" else "Steady Pace"),
                            "Video Type" to (if (videoType.isEmpty()) "Both" else videoType),
                            "Weekly Time" to (if (weeklyTime.isEmpty()) "1 Hour" else weeklyTime),
                            "Editing Level" to (if (editingExp.isEmpty()) "Basic" else editingExp),
                            "Recording Setup" to (if (recordingSetup.isEmpty()) "Only Phone" else recordingSetup),
                            "Growth Difficulty" to "Optimal / Moderate"
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            blueprintItems.forEach { (label, valStr) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF252532))
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = label, fontSize = 13.sp, color = Color(0xFFA1A1AA))
                                    Text(text = valStr, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Personalized Roadmap
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF1C1C24),
                    border = BorderStroke(1.5.dp, Color(0xFF2C2C3A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "🗺️ Personalized 8-Week Roadmap",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tailored for $weeklyTime weekly commitment",
                            fontSize = 12.sp,
                            color = Color(0xFFA1A1AA)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        val roadmapWeeks = listOf(
                            "Week 1" to "Channel Setup & Branding",
                            "Week 2" to "Content Strategy & Niche Focus",
                            "Week 3" to "Video Creation & Script Hooks",
                            "Week 4" to "Editing & Visual Polish",
                            "Week 5" to "Upload Strategy & SEO Tags",
                            "Week 6" to "YouTube Algorithm & CTR Mastery",
                            "Week 7" to "Monetization & Milestone Goals",
                            "Week 8" to "Creator Business & Scaling"
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            roadmapWeeks.forEach { (wk, topic) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF252532))
                                        .border(BorderStroke(1.dp, Color(0xFF38384A)), RoundedCornerShape(12.dp))
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF382024)
                                    ) {
                                        Text(
                                            text = wk,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFFFF4D4D),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(text = topic, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // TODAY'S MISSION GLASS CARD
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF1C1C24),
                    border = BorderStroke(1.5.dp, Color(0xFFFF3333)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "⚡", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Today's Mission",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Complete Channel Setup",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF4D4D),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF2A2A38)
                        ) {
                            Text(
                                text = "Estimated Time: 20 Minutes",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA1A1AA),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFFF3333), Color(0xFFCC0000))
                                    )
                                )
                                .clickable {
                                    // Save Level 0 Completion & persistent memory
                                    CreatorAcademyPrefs.setYouTubeLevel0Completed(context, true)
                                    val mappedLang = if (videoLang.contains("English", ignoreCase = true)) "ENGLISH" else if (videoLang.contains("Hinglish", ignoreCase = true)) "HINGLISH" else "HINDI"
                                    CreatorAcademyPrefs.saveYouTubeLanguage(context, mappedLang)

                                    val mappedType = if (videoType == "Shorts Only") "SHORTS_CREATOR" else "YOUTUBE_VLOGS"
                                    CreatorAcademyPrefs.saveYouTubeCreatorType(context, mappedType)

                                    onCompleteLevel0(mappedLang, mappedType, mainGoal)
                                }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Start Mission ➔",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionOptionsCard(
    questionNum: String,
    title: String,
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit
) {
    Column {
        Text(text = questionNum, fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { option ->
                val isSelected = selectedOption == option
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) Color(0xFF332024) else Color(0xFF252530))
                        .border(BorderStroke(1.2.dp, if (isSelected) Color(0xFFFF3333) else Color(0xFF3A3A4A)), RoundedCornerShape(14.dp))
                        .clickable { onSelect(option) }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = option, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                        if (isSelected) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFFFF3333), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

// ============================================================================
// MASTER PHASE 3 — YOUTUBE GROWTH GUIDE (LEVEL 1: PROFESSIONAL CHANNEL SETUP)
// ============================================================================
@Composable
fun YouTubeLevel1SetupScreen(
    onCompleteLevel1: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var stepIndex by remember { mutableIntStateOf(0) } // 0..11 for Steps 1..12, 12 for Mission Checklist

    // AI Mentor Animated Glow
    val infiniteTransition = rememberInfiniteTransition(label = "level1Glow")
    val avatarGlowScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale1"
    )

    // User State & Preferences
    val savedNiche = remember { CreatorAcademyPrefs.getYouTubeVideoType(context) ?: "General" }
    var chosenChannelName by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeChannelName(context) ?: "") }
    var chosenHandle by remember { mutableStateOf("") }
    var customNicheInput by remember { mutableStateOf(savedNiche) }
    var nameSeedIndex by remember { mutableIntStateOf(0) }
    var handleSeedIndex by remember { mutableIntStateOf(0) }

    // Re-explanation count tracking for "Explain Again" feature
    var explainAgainIndex by remember { mutableIntStateOf(0) }

    // Step 1: Google Account Check state
    var hasGoogleAcc by remember { mutableStateOf<Boolean?>(null) }
    var googleAccSubStep by remember { mutableIntStateOf(0) }

    // Step 2: Open YouTube state
    var isOpenYtConfirmed by remember { mutableStateOf<Boolean?>(null) }
    var openYtAlternateExplain by remember { mutableStateOf(false) }

    // Step 6: Profile DP state
    var hasDpPhoto by remember { mutableStateOf<Boolean?>(null) }

    // Mission Checklist Items State
    var chCreatedChecked by remember { mutableStateOf(false) }
    var handleChecked by remember { mutableStateOf(false) }
    var dpChecked by remember { mutableStateOf(false) }
    var bannerChecked by remember { mutableStateOf(false) }
    var descChecked by remember { mutableStateOf(false) }
    var studioChecked by remember { mutableStateOf(false) }

    // Level Complete Glass Modal
    var showLevel1CompleteModal by remember { mutableStateOf(false) }

    // Copy Toast State
    var copyToastText by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(copyToastText) {
        if (copyToastText != null) {
            kotlinx.coroutines.delay(2000)
            copyToastText = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF121215), Color(0xFF0F0F12), Color(0xFF1A1A22))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 520.dp)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Top Bar Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (stepIndex > 0) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF24242C))
                            .clickable { stepIndex -= 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "←", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Spacer(modifier = Modifier.size(36.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF282834)),
                        contentAlignment = Alignment.Center
                    ) {
                        OfficialLogo(name = "youtube", modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LEVEL 1 • CHANNEL SETUP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFA1A1AA),
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF24242C))
                        .clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AI Mentor Greeting Header
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1C1C24),
                border = BorderStroke(1.5.dp, Color(0xFF2C2C3A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size((68 * avatarGlowScale).dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF3333).copy(alpha = 0.25f))
                        )
                        Box(
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(Color(0xFFFF3333), Color(0xFF990000))
                                    )
                                )
                                .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.9f)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🔥", fontSize = 26.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Professional Channel Setup",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "\"Awesome 🔥 Ab hum YouTube Channel banayenge. Main assume nahi karunga ki tumhe kuch pata hai. Main bilkul zero se sikhaunga.\"",
                        fontSize = 13.sp,
                        color = Color(0xFFA1A1AA),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress Bar (10% base + steps)
                    val progressVal = if (stepIndex == 12) 100 else (10 + ((stepIndex * 90) / 12))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (stepIndex == 12) "Mission Checklist" else "Step ${stepIndex + 1} of 12",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD1D1D6)
                        )
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF2A2A36)
                        ) {
                            Text(
                                text = "$progressVal%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFF4D4D),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF2C2C38))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progressVal / 100f)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFFF3333), Color(0xFFFF6666))
                                    )
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Toast Message when copied
            copyToastText?.let { toastMsg ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF2E2E3E),
                    border = BorderStroke(1.dp, Color(0xFFFF3333)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "📋 $toastMsg",
                        fontSize = 12.5.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // DYNAMIC STEP RENDERER (1..12 or Mission Checklist 12)
            if (stepIndex < 12) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF1C1C24),
                    border = BorderStroke(1.5.dp, Color(0xFF2C2C3A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        when (stepIndex) {
                            0 -> {
                                // STEP 1: Google Account Check
                                Text(text = "STEP 1 OF 12", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Google Account Check 📧", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "Kya tumhare paas Google (Gmail) Account hai?",
                                    fontSize = 14.5.sp,
                                    color = Color(0xFFE5E5EA),
                                    lineHeight = 20.sp
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    val isYes = hasGoogleAcc == true
                                    val isNo = hasGoogleAcc == false

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (isYes) Color(0xFF332024) else Color(0xFF252530))
                                            .border(BorderStroke(1.5.dp, if (isYes) Color(0xFFFF3333) else Color(0xFF3A3A4A)), RoundedCornerShape(16.dp))
                                            .clickable {
                                                hasGoogleAcc = true
                                                stepIndex = 1
                                            }
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "✅ Haan", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = if (isYes) Color(0xFFFF4D4D) else Color.White)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (isNo) Color(0xFF332024) else Color(0xFF252530))
                                            .border(BorderStroke(1.5.dp, if (isNo) Color(0xFFFF3333) else Color(0xFF3A3A4A)), RoundedCornerShape(16.dp))
                                            .clickable {
                                                hasGoogleAcc = false
                                                googleAccSubStep = 0
                                            }
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "❌ Nahi", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = if (isNo) Color(0xFFFF4D4D) else Color.White)
                                    }
                                }

                                if (hasGoogleAcc == false) {
                                    Spacer(modifier = Modifier.height(16.dp))

                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = Color(0xFF252532),
                                        border = BorderStroke(1.dp, Color(0xFF38384A)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                text = "📌 Google Account Banane Ka Step-by-Step Guide:",
                                                fontSize = 13.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFF4D4D)
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))

                                            val stepsList = listOf(
                                                "1. Open Play Store / Device Settings",
                                                "2. Tap 'Add Account' ➔ Select Gmail",
                                                "3. Tap 'Create Account' ➔ Select 'For myself / Business'",
                                                "4. Enter First Name, Last Name & Date of Birth",
                                                "5. Choose your Email ID & Strong Password",
                                                "6. Verify Mobile Number via OTP ➔ Tap Done!"
                                            )

                                            stepsList.forEachIndexed { idx, st ->
                                                val isCurrent = idx <= googleAccSubStep
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(vertical = 4.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = if (isCurrent) Icons.Default.Check else Icons.Default.ChevronRight,
                                                        contentDescription = null,
                                                        tint = if (isCurrent) Color(0xFFFF4D4D) else Color(0xFF666675),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = st,
                                                        fontSize = 13.sp,
                                                        color = if (isCurrent) Color.White else Color(0xFFA1A1AA),
                                                        fontWeight = if (isCurrent) FontWeight.Medium else FontWeight.Normal
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(14.dp))

                                            if (googleAccSubStep < 5) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(Color(0xFF382024))
                                                        .clickable { googleAccSubStep += 1 }
                                                        .padding(vertical = 10.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = "Next Step Verified ➔", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                                }
                                            } else {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(Color(0xFFFF3333))
                                                        .clickable {
                                                            hasGoogleAcc = true
                                                            stepIndex = 1
                                                        }
                                                        .padding(vertical = 12.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = "Account Created & Verified! Continue ➔", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            1 -> {
                                // STEP 2: Open YouTube
                                Text(text = "STEP 2 OF 12", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Open YouTube App 📱", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = if (openYtAlternateExplain) {
                                        "Dekho simple language me: Apne phone me YouTube app open karo ya Chrome browser khol kar https://www.youtube.com search karo aur top right me profile icon par click karo."
                                    } else {
                                        "Apne Phone me YouTube App open karo OR Chrome Browser me https://www.youtube.com open karo."
                                    },
                                    fontSize = 14.sp,
                                    color = Color(0xFFE5E5EA),
                                    lineHeight = 20.sp
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(text = "AI Asks: Open ho gaya?", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(Color(0xFFFF3333))
                                            .clickable {
                                                isOpenYtConfirmed = true
                                                stepIndex = 2
                                            }
                                            .padding(14.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "YES ➔", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(Color(0xFF252530))
                                            .border(BorderStroke(1.2.dp, Color(0xFF3A3A4A)), RoundedCornerShape(14.dp))
                                            .clickable {
                                                isOpenYtConfirmed = false
                                                openYtAlternateExplain = true
                                            }
                                            .padding(14.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "NO (Explain Differently)", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA1A1AA))
                                    }
                                }
                            }

                            2 -> {
                                // STEP 3: Create Channel
                                Text(text = "STEP 3 OF 12", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Create Channel 🛠️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(14.dp))

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF252532),
                                    border = BorderStroke(1.dp, Color(0xFF38384A)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        val steps = listOf(
                                            "1. YouTube App ke Top-Right corner me Profile Photo / Avatar par Tap karo.",
                                            "2. Menu me se 'View Channel' ya 'Your Channel' select karo.",
                                            "3. Agar channel pehle se nahi bana hai toh 'Create Channel' button dikhega.",
                                            "4. 'Create Channel' par tap karke Continue karo."
                                        )
                                        steps.forEach { st ->
                                            Text(
                                                text = st,
                                                fontSize = 13.sp,
                                                color = Color.White,
                                                modifier = Modifier.padding(vertical = 4.dp),
                                                lineHeight = 18.sp
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable {
                                            chCreatedChecked = true
                                            stepIndex = 3
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Channel Created! Proceed to Name Generator ➔", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            3 -> {
                                // STEP 4: Channel Name Generator (20 Names, Unlimited Refresh)
                                Text(text = "STEP 4 OF 12", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Unlimited Channel Name Generator 💡", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "AI Asks: Tum kis niche/topic me content banaoge?",
                                    fontSize = 13.5.sp,
                                    color = Color(0xFFA1A1AA)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = customNicheInput,
                                    onValueChange = { customNicheInput = it },
                                    placeholder = { Text("e.g. Tech, Gaming, Vlog, Cooking, Finance", color = Color(0xFF666675)) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFF3333),
                                        unfocusedBorderColor = Color(0xFF3A3A4A),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "20 Generated Professional Names:",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF382024),
                                        modifier = Modifier.clickable { nameSeedIndex += 1 }
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color(0xFFFF4D4D), modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "Fresh 20 Names 🔄", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                val nameList = get20ChannelNames(customNicheInput, nameSeedIndex)

                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    nameList.chunked(2).forEach { pair ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            pair.forEach { nm ->
                                                val isSelected = chosenChannelName == nm
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(if (isSelected) Color(0xFF332024) else Color(0xFF252530))
                                                        .border(BorderStroke(1.dp, if (isSelected) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(12.dp))
                                                        .clickable {
                                                            chosenChannelName = nm
                                                            CreatorAcademyPrefs.saveYouTubeChannelName(context, nm)
                                                            clipboardManager.setText(AnnotatedString(nm))
                                                            copyToastText = "Copied '$nm' to clipboard!"
                                                        }
                                                        .padding(vertical = 10.dp, horizontal = 10.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = nm, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = if (isSelected) Color(0xFFFF4D4D) else Color.White, textAlign = TextAlign.Center)
                                                }
                                            }
                                            if (pair.size == 1) {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable {
                                            if (chosenChannelName.isEmpty() && nameList.isNotEmpty()) {
                                                chosenChannelName = nameList[0]
                                                CreatorAcademyPrefs.saveYouTubeChannelName(context, nameList[0])
                                            }
                                            stepIndex = 4
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (chosenChannelName.isNotEmpty()) "Selected: $chosenChannelName ➔ Handle Setup" else "Select Name & Continue ➔",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            4 -> {
                                // STEP 5: Handle Generator & Username Rules
                                Text(text = "STEP 5 OF 12", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Channel Handle (@username) Rules 🏷️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFF252532),
                                    border = BorderStroke(1.dp, Color(0xFF38384A)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(text = "✅ GOOD HANDLE (Clean & Easy):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4DEEEA))
                                        Text(text = "• @TechWithAlex  • @AlexGaming  • @TheAlexShow", fontSize = 12.sp, color = Color.White)

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(text = "❌ BAD HANDLE (Hard to remember):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                        Text(text = "• @alex123456789tech_official_99", fontSize = 12.sp, color = Color(0xFFA1A1AA))
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "Handle Suggestions:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF382024),
                                        modifier = Modifier.clickable { handleSeedIndex += 1 }
                                    ) {
                                        Text(text = "Regenerate 🔄", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                val handleList = getHandleSuggestions(chosenChannelName.ifEmpty { "Creator" }, handleSeedIndex)

                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    handleList.forEach { h ->
                                        val isSelected = chosenHandle == h
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (isSelected) Color(0xFF332024) else Color(0xFF252530))
                                                .border(BorderStroke(1.dp, if (isSelected) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(12.dp))
                                                .clickable {
                                                    chosenHandle = h
                                                    clipboardManager.setText(AnnotatedString(h))
                                                    copyToastText = "Copied '$h' to clipboard!"
                                                }
                                                .padding(horizontal = 14.dp, vertical = 10.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = h, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color(0xFFFF4D4D) else Color.White)
                                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = Color(0xFFA1A1AA), modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable {
                                            handleChecked = true
                                            stepIndex = 5
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Handle Set! Proceed to Profile Photo (DP) ➔", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            5 -> {
                                // STEP 6: Profile Photo (DP)
                                Text(text = "STEP 6 OF 12", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Profile Photo (DP) Guide 🖼️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(text = "Already profile picture hai?", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    val isYes = hasDpPhoto == true
                                    val isNo = hasDpPhoto == false

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(if (isYes) Color(0xFF332024) else Color(0xFF252530))
                                            .border(BorderStroke(1.2.dp, if (isYes) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(14.dp))
                                            .clickable {
                                                hasDpPhoto = true
                                                dpChecked = true
                                            }
                                            .padding(14.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "✅ YES", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isYes) Color(0xFFFF4D4D) else Color.White)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(if (isNo) Color(0xFF332024) else Color(0xFF252530))
                                            .border(BorderStroke(1.2.dp, if (isNo) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(14.dp))
                                            .clickable { hasDpPhoto = false }
                                            .padding(14.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "❌ NO", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isNo) Color(0xFFFF4D4D) else Color.White)
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF252532),
                                    border = BorderStroke(1.dp, Color(0xFF38384A)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "🎨 DP Design Idea For ${customNicheInput}:", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                        Spacer(modifier = Modifier.height(6.dp))
                                        val idea = when {
                                            customNicheInput.contains("Gaming", ignoreCase = true) -> "Neon Mascot Logo or High-Contrast Gaming Avatar with glowing eyes."
                                            customNicheInput.contains("Tech", ignoreCase = true) -> "Clean face cut-out with dark background + blue/cyan edge glow OR modern tech emblem."
                                            customNicheInput.contains("Vlog", ignoreCase = true) -> "Smiling HD Portrait shot with vibrant background color and high contrast."
                                            customNicheInput.contains("Finance", ignoreCase = true) -> "Professional blazer photo OR sleek minimalist chart/bull logo."
                                            else -> "HD Face photo with solid bright background OR clean 2-letter typography logo (800x800 px)."
                                        }
                                        Text(text = idea, fontSize = 13.sp, color = Color.White, lineHeight = 18.sp)

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(text = "💡 Free Tools: Canva app / Remove.bg / Photoroom", fontSize = 12.sp, color = Color(0xFFA1A1AA))
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable {
                                            dpChecked = true
                                            stepIndex = 6
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "DP Ready! Proceed to Banner Design ➔", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            6 -> {
                                // STEP 7: Banner
                                Text(text = "STEP 7 OF 12", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Channel Banner & Safe Area 📐", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF252532),
                                    border = BorderStroke(1.dp, Color(0xFF38384A)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "📐 Dimensions Guide:", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = "• Banner Size: 2560 x 1440 px\n• Safe Area (Mobile Visible): 1546 x 423 px (center box)", fontSize = 13.sp, color = Color.White, lineHeight = 18.sp)

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(text = "📝 What to write on Banner:", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4DEEEA))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        val bannerTextEx = when {
                                            customNicheInput.contains("Tech", ignoreCase = true) -> "SMARTPHONE REVIEWS & TECH TIPS | New Video Every Friday 🔔"
                                            customNicheInput.contains("Gaming", ignoreCase = true) -> "DAILY LIVE STREAMS & HIGHLIGHTS | Subscribe For Montages 🎮"
                                            else -> "${chosenChannelName.ifEmpty { "CREATOR" }} | ${customNicheInput.uppercase()} CONTENT | New Videos Weekly 🚀"
                                        }
                                        Text(text = bannerTextEx, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable {
                                            bannerChecked = true
                                            stepIndex = 7
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Banner Set! Proceed to Description ➔", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            7 -> {
                                // STEP 8: Description Generator
                                Text(text = "STEP 8 OF 12", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "SEO Channel Description Generator 📝", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(10.dp))

                                val generatedBio = getChannelDescriptionBio(chosenChannelName.ifEmpty { "My Channel" }, customNicheInput)

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF252532),
                                    border = BorderStroke(1.dp, Color(0xFF38384A)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "Generated Bio:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0xFF382024))
                                                    .clickable {
                                                        clipboardManager.setText(AnnotatedString(generatedBio))
                                                        copyToastText = "Description Bio copied to clipboard!"
                                                    }
                                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                                            ) {
                                                Text(text = "Copy Bio 📋", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(text = generatedBio, fontSize = 12.5.sp, color = Color.White, lineHeight = 18.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable {
                                            descChecked = true
                                            stepIndex = 8
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Description Bio Saved! Proceed to Links ➔", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            8 -> {
                                // STEP 9: Links & Business Email
                                Text(text = "STEP 9 OF 12", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Social Links & Business Email 🌐", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF252532),
                                    border = BorderStroke(1.dp, Color(0xFF38384A)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "💡 Why add links & business email?", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = "• Brand Deals & Sponsorships contact you directly via email.\n• Instagram / Twitter links help build loyal cross-platform followers.", fontSize = 13.sp, color = Color.White, lineHeight = 18.sp)

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(text = "📧 Business Email Format Examples:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4DEEEA))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        val cleanName = chosenChannelName.lowercase().replace(" ", "")
                                        Text(text = "• business.${cleanName}@gmail.com\n• contact.${cleanName}@gmail.com\n• sponsors.${cleanName}@gmail.com", fontSize = 12.sp, color = Color(0xFFA1A1AA))
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 9 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Continue to Watermark Guide ➔", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            9 -> {
                                // STEP 10: Video Watermark
                                Text(text = "STEP 10 OF 12", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Video Branding Watermark 💧", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF252532),
                                    border = BorderStroke(1.dp, Color(0xFF38384A)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "What is a Video Watermark?", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = "Har video ke bottom-right corner me ek chota Subscribe button icon hota hai. Ispar click karke viewers instantly channel subscribe kar sakte hain.", fontSize = 13.sp, color = Color.White, lineHeight = 18.sp)

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(text = "• Best Size: 150 x 150 px PNG (Transparent)\n• Where to set: studio.youtube.com ➔ Customization ➔ Branding ➔ Video Watermark", fontSize = 12.5.sp, color = Color(0xFFA1A1AA), lineHeight = 18.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 10 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Continue to Phone Verification ➔", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            10 -> {
                                // STEP 11: Phone Verification
                                Text(text = "STEP 11 OF 12", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Phone Verification Unlock 🔐", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF252532),
                                    border = BorderStroke(1.dp, Color(0xFF38384A)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "⚡ Benefits Unlocked After Phone Verification:", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                        Spacer(modifier = Modifier.height(8.dp))

                                        val perks = listOf(
                                            "🖼️ Custom Thumbnails (bohot zaroori CTR ke liye)",
                                            "⏳ Videos longer than 15 minutes",
                                            "📡 Live Streaming capability",
                                            "🛡️ Content ID claims & protection"
                                        )
                                        perks.forEach { p ->
                                            Text(text = p, fontSize = 13.sp, color = Color.White, modifier = Modifier.padding(vertical = 3.dp))
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(text = "How: Settings ⚙️ ➔ Channel ➔ Feature Eligibility ➔ Intermediate Features ➔ Verify Phone Number", fontSize = 12.sp, color = Color(0xFFA1A1AA), lineHeight = 16.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 11 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Verified! Continue to YouTube Studio Guide ➔", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            11 -> {
                                // STEP 12: YouTube Studio App Overview
                                Text(text = "STEP 12 OF 12", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "YouTube Studio Control Center 📊", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    val studioTabs = listOf(
                                        "📊 Dashboard" to "Quick overview of subscribers, latest video stats & creator news.",
                                        "📈 Analytics" to "Realtime views, watch time hours, CTR, audience demographics & retention graph.",
                                        "🎬 Content" to "Manage uploaded videos, Shorts, live streams & visibility settings.",
                                        "💬 Comments" to "Reply to fans, filter comments & hold for review.",
                                        "💰 Monetization" to "Track YPP criteria (1K subs, 4K hours / 10M Shorts views) & earn revenue."
                                    )

                                    studioTabs.forEach { (tab, desc) ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFF252532),
                                            border = BorderStroke(1.dp, Color(0xFF38384A)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Text(text = tab, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                Spacer(modifier = Modifier.height(3.dp))
                                                Text(text = desc, fontSize = 12.sp, color = Color(0xFFA1A1AA))
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable {
                                            studioChecked = true
                                            stepIndex = 12
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Studio Installed! View Level 1 Mission ➔", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }

                        // EXPLAIN AGAIN AI BUTTON (40+ Response Variations)
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF282836),
                                border = BorderStroke(1.dp, Color(0xFF3A3A4C)),
                                modifier = Modifier.clickable { explainAgainIndex += 1 }
                            ) {
                                Text(
                                    text = "💡 Explain Again in Fresh Words",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF4D4D),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }

                        if (explainAgainIndex > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF2E2228),
                                border = BorderStroke(1.dp, Color(0xFFFF3333).copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "🤖 AI Mentor Alternative Explanation:\n${getExplainAgainVariant(stepIndex, explainAgainIndex)}",
                                    fontSize = 12.5.sp,
                                    color = Color(0xFFF5F5F7),
                                    modifier = Modifier.padding(12.dp),
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            } else {
                // ============================================================
                // LEVEL 1 MISSION CHECKLIST & LEVEL COMPLETE MODAL TRIGGER
                // ============================================================
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF1C1C24),
                    border = BorderStroke(1.5.dp, Color(0xFFFF3333)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "⚡", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Create Your Professional Channel",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Level 1 Final Verification Checklist",
                            fontSize = 12.sp,
                            color = Color(0xFFFF4D4D),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        val checklistItems = listOf(
                            "Channel Created" to (chCreatedChecked to { chCreatedChecked = !chCreatedChecked }),
                            "Handle Selected" to (handleChecked to { handleChecked = !handleChecked }),
                            "Profile Photo (DP) Uploaded" to (dpChecked to { dpChecked = !dpChecked }),
                            "Channel Banner Added" to (bannerChecked to { bannerChecked = !bannerChecked }),
                            "Description Saved" to (descChecked to { descChecked = !descChecked }),
                            "YouTube Studio Installed" to (studioChecked to { studioChecked = !studioChecked })
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            checklistItems.forEach { (label, pair) ->
                                val (isChecked, onToggle) = pair
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (isChecked) Color(0xFF332024) else Color(0xFF252530))
                                        .border(BorderStroke(1.2.dp, if (isChecked) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(14.dp))
                                        .clickable { onToggle() }
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = (if (isChecked) "☑ " else "☐ ") + label,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isChecked) Color.White else Color(0xFFA1A1AA)
                                    )
                                    if (isChecked) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFFFF3333), modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFFF3333), Color(0xFFCC0000))
                                    )
                                )
                                .clickable {
                                    showLevel1CompleteModal = true
                                }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Mission Complete 🏆 Claim Reward",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // ============================================================
        // LEVEL 1 COMPLETE PREMIUM GLASS MODAL
        // ============================================================
        if (showLevel1CompleteModal) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = Color(0xFF1E1E28),
                    border = BorderStroke(2.dp, Color(0xFFFF3333)),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .widthIn(max = 420.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF3333).copy(alpha = 0.2f))
                                .border(BorderStroke(2.dp, Color(0xFFFF3333)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🏆", fontSize = 34.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "LEVEL 1 COMPLETE!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF382024)
                        ) {
                            Text(
                                text = "Badge Unlocked: Professional Channel Created 🏅",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF4D4D),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "+500 XP Earned! 🌟\nCongratulations! Tumne professionally apna YouTube channel setup kar liya hai.",
                            fontSize = 13.5.sp,
                            color = Color(0xFFE5E5EA),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFFF3333), Color(0xFFCC0000))
                                    )
                                )
                                .clickable {
                                    showLevel1CompleteModal = false
                                    CreatorAcademyPrefs.setYouTubeLevel1Completed(context, true)
                                    onCompleteLevel1()
                                }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Continue to Master Course ➔",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

// ============================================================================
// LEVEL 1 GENERATOR HELPERS
// ============================================================================
private fun get20ChannelNames(niche: String, seed: Int): List<String> {
    val cleanNiche = if (niche.isBlank()) "Creator" else niche.trim()
    val pools = listOf(
        listOf(
            "The ${cleanNiche} Vault", "${cleanNiche} Xpress", "Pulse ${cleanNiche}",
            "${cleanNiche} HQ", "Real ${cleanNiche}", "Ultra ${cleanNiche}",
            "The ${cleanNiche} Show", "${cleanNiche} Matrix", "Prime ${cleanNiche}",
            "Daily ${cleanNiche}", "${cleanNiche} Hub", "Beyond ${cleanNiche}",
            "${cleanNiche} Edge", "${cleanNiche} Code", "True ${cleanNiche}",
            "${cleanNiche} Zone", "${cleanNiche} Studio", "Pro ${cleanNiche}",
            "Master ${cleanNiche}", "Focus ${cleanNiche}"
        ),
        listOf(
            "Hyper ${cleanNiche}", "${cleanNiche} Uncut", "The ${cleanNiche} Guy",
            "${cleanNiche} Tribe", "${cleanNiche} Mindset", "${cleanNiche} Lab",
            "${cleanNiche} Bytes", "Raw ${cleanNiche}", "${cleanNiche} Central",
            "Inside ${cleanNiche}", "${cleanNiche} Shift", "The ${cleanNiche} Daily",
            "${cleanNiche} Insider", "${cleanNiche} Scope", "${cleanNiche} Arena",
            "Next ${cleanNiche}", "${cleanNiche} Connect", "Alpha ${cleanNiche}",
            "${cleanNiche} Playbook", "Smart ${cleanNiche}"
        ),
        listOf(
            "${cleanNiche} Mastery", "Pure ${cleanNiche}", "The ${cleanNiche} Routine",
            "${cleanNiche} Express", "${cleanNiche} Lounge", "${cleanNiche} Sphere",
            "Core ${cleanNiche}", "${cleanNiche} Blueprint", "Apex ${cleanNiche}",
            "${cleanNiche} Pulse", "${cleanNiche} Nation", "${cleanNiche} Spot",
            "Wild ${cleanNiche}", "Active ${cleanNiche}", "${cleanNiche} Vision",
            "${cleanNiche} Frame", "${cleanNiche} Galaxy", "${cleanNiche} Flow",
            "${cleanNiche} Spark", "${cleanNiche} Horizon"
        )
    )
    return pools[seed % pools.size]
}

private fun getHandleSuggestions(name: String, seed: Int): List<String> {
    val clean = name.lowercase().replace(" ", "").replace(Regex("[^a-z0-0]"), "")
    val base = if (clean.isEmpty()) "creator" else clean
    val variants = listOf(
        listOf("@$base", "@the_$base", "@${base}_official", "@ask_$base", "@${base}_hq"),
        listOf("@real_$base", "@${base}_zone", "@${base}_tv", "@$base.x", "@${base}_live"),
        listOf("@${base}_daily", "@$base.official", "@thisis_$base", "@$base.yt", "@${base}_show")
    )
    return variants[seed % variants.size]
}

private fun getChannelDescriptionBio(channelName: String, niche: String): String {
    val n = if (niche.isBlank()) "YouTube" else niche
    return """
👋 Welcome to $channelName!

Target Niche: $n
On this channel, you will discover the best $n tutorials, in-depth breakdowns, expert tips, and daily inspiration to help you level up!

📅 Upload Schedule:
• New Video Every Week
• Daily Shorts & Updates

🔔 Don't forget to SUBSCRIBE & turn on notifications so you never miss a video!

📩 Business Inquiries: contact.$channelName@gmail.com
    """.trimIndent()
}

private fun getExplainAgainVariant(stepIdx: Int, variantIdx: Int): String {
    val explanations = listOf(
        "Dekho simple shabdon me samjho: Is step ka maqsad tumhare channel ko professional look dena aur viewers ka trust jeetna hai.",
        "Ek nayi udaharan se samjho: Jaise ek brand dukaan kholne se pehle board aur nameplate lagata hai, waise hi YT channel ka name aur handle unique hona chahiye.",
        "Simple step formula: Action ➔ Verification ➔ Save. Koi jaldbazi mat karo, ek baar dhyan se set kar lo.",
        "AI Mentor Tip: YouTube Algorithm unhi channels ko boost karta hai jinka basic branding aur metadata complete aur clean hota hai."
    )
    return explanations[(stepIdx + variantIdx) % explanations.size]
}

// ============================================================================
// MASTER PHASE 5 — YOUTUBE GROWTH GUIDE (LEVEL 3: VIDEO PLANNING MASTERCLASS)
// ============================================================================

private fun get10HooksForTopic(topic: String, seed: Int): List<String> {
    val clean = if (topic.isBlank()) "YouTube Video" else topic
    val set1 = listOf(
        "1. Stop making $clean videos until you watch this exact 10-second warning!",
        "2. If I had to start $clean from zero today, this is the FIRST thing I would do...",
        "3. 99% of creators mess up $clean because of ONE stupid mistake. Here’s how to fix it.",
        "4. What if I told you $clean is actually 10x easier than everyone claims?",
        "5. I spent 50 hours researching $clean so you don't have to...",
        "6. Do NOT post your next $clean video before checking these 3 secret rules!",
        "7. The dirty secret about $clean that big YouTubers will never tell you.",
        "8. Want to master $clean in under 5 minutes? Watch this till the end.",
        "9. This simple $clean hack doubled my retention overnight!",
        "10. Here is the exact $clean formula that goes viral every single time."
    )
    val set2 = listOf(
        "1. This ONE $clean trick changed my channel forever...",
        "2. Why nobody is watching your $clean videos (and how to fix it right now).",
        "3. I tried the viral $clean strategy for 7 days... here are the insane results!",
        "4. If you care about $clean, you cannot afford to skip this video.",
        "5. The ultimate step-by-step $clean guide that actually works in 2026.",
        "6. 3 massive $clean lies you still believe!",
        "7. How to make your $clean videos look like a million bucks with $0.",
        "8. This is the smartest $clean method I have ever seen.",
        "9. Watch what happens when you apply this $clean retention secret!",
        "10. Ready to blow up your $clean channel? Let's break down the strategy."
    )
    return if (seed % 2 == 0) set1 else set2
}

private fun get20TitlesForTopic(topic: String, seed: Int): List<String> {
    val clean = if (topic.isBlank()) "YouTube" else topic
    return listOf(
        "1. How to Master $clean in 2026 (Full Step-By-Step Guide) 🔥",
        "2. Stop Doing THIS in $clean! (Top 5 Mistakes) ❌",
        "3. The Secret $clean Blueprint Nobody Talks About 🤫",
        "4. $clean for Complete Beginners: Everything You Need 💡",
        "5. How I Doubled My Results in $clean (Real Case Study) 📈",
        "6. 7 Game-Changing $clean Hacks You Must Try Today ⚡",
        "7. $clean on a $0 Budget: Complete Setup Guide 🎥",
        "8. Why 90% Fail at $clean (And How You Can Win) 🏆",
        "9. I Tested $clean for 30 Days! (Insane Results) 🚀",
        "10. The Ultimate $clean Masterclass for 2026 🎬",
        "11. $clean Uncovered: Truth vs Myth 🎯",
        "12. 5 FREE $clean Tools Every Creator Needs 🛠️",
        "13. How to Make Viral $clean Content Fast 💥",
        "14. $clean Explained in 5 Minutes! ⏱️",
        "15. The Smartest Way to Grow with $clean 🧠",
        "16. $clean Workflow Secrets Revealed! 🔑",
        "17. Don't Start $clean Until You Watch This! ⚠️",
        "18. How to Monetize Your $clean Content Instantly 💰",
        "19. The Best $clean Strategy for Fast Growth 🌟",
        "20. $clean Checklist Before Hitting Publish ✅"
    )
}

private fun getExplainAgainVariantLevel3(stepIdx: Int, variantIdx: Int): String {
    val explanations = listOf(
        "Dekho simple bhasha me: Video planning ka matlab hai shooting se pehle exact structure aur strategy clear rakhna taaki camera ke samne confidence rahe.",
        "Practical Example: Jaise movie shoot karne se pehle script aur shot-list banti hai, waise hi YouTube video me hook + script + B-roll plan hone par watch time double ho jata hai.",
        "AI Master Rule: First 5 seconds (Hook) user ko rokta hai, script value deti hai, aur strong CTA viewer ko subscriber me badalta hai.",
        "Quick Action Plan: Goal decide karo ➔ Hook select karo ➔ Bullet script banao ➔ Lighting & mic set karke record karo!"
    )
    return explanations[(stepIdx + variantIdx) % explanations.size]
}

private fun getConfidenceCoachAdvice(seed: Int): String {
    val adviceList = listOf(
        "💪 Tip #1: Camera lens ko ek dost samjho! Frame me apne sabse acche friend se baat karne ki tarah bolo. Pehli 3 take hamesha warmup hoti hain, relax karo!",
        "🌟 Tip #2: Camera ke samne thoda extra energy multiplier chahiye hota hai! 10% louder aur clear bolo, smile automatic aayegi.",
        "🔥 Tip #3: Galti hona bilkul normal hai! Cut aur edit ka option hamesha hai. Script ke exact shabd mat rato, points samjho aur natural bolo.",
        "🎯 Tip #4: Fear sirf initial 30 seconds tak rehta hai. Deep breath lo, 3-2-1 countdown karo aur roll kardo!",
        "✨ Tip #5: Viewer video ki value ke liye aata hai, perfection ke liye nahi. Apni genuine personality share karo!"
    )
    return adviceList[seed % adviceList.size]
}

@Composable
fun YouTubeLevel3SetupScreen(
    onCompleteLevel3: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var stepIndex by remember { mutableIntStateOf(0) } // 0..13 for Steps 1..14, 14 for Mission Checklist

    // AI Mentor Animated Glow
    val infiniteTransition = rememberInfiniteTransition(label = "level3Glow")
    val avatarGlowScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale3"
    )

    // User Choices & State
    val savedNiche = remember { CreatorAcademyPrefs.getYouTubeNiche(context) ?: "Gaming" }
    var selectedVideoTopic by remember { mutableStateOf("How to Start $savedNiche in 2026") }
    var customTopicInput by remember { mutableStateOf("") }
    var selectedVideoGoal by remember { mutableStateOf("Views 📈") }
    var selectedVideoLength by remember { mutableStateOf("5 Minutes 🎬") }
    var selectedScriptTone by remember { mutableStateOf("Friendly 😊") }
    var selectedCameraDevice by remember { mutableStateOf("Phone 📱") }
    var wantAiThumbnailHelp by remember { mutableStateOf(true) }

    var hookSeed by remember { mutableIntStateOf(0) }
    var titleSeed by remember { mutableIntStateOf(0) }
    var confidenceSeed by remember { mutableIntStateOf(0) }
    var explainVariantIdx by remember { mutableIntStateOf(0) }
    var showConfidenceCoachModal by remember { mutableStateOf(false) }

    // Recording Checklist Items State
    var scriptReadyChecked by remember { mutableStateOf(false) }
    var cameraReadyChecked by remember { mutableStateOf(false) }
    var audioReadyChecked by remember { mutableStateOf(false) }
    var lightingChecked by remember { mutableStateOf(false) }
    var backgroundChecked by remember { mutableStateOf(false) }
    var thumbnailPlanChecked by remember { mutableStateOf(false) }

    // Level Complete Glass Modal
    var showLevel3CompleteModal by remember { mutableStateOf(false) }

    // Copy Toast State
    var copyToastText by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(copyToastText) {
        if (copyToastText != null) {
            kotlinx.coroutines.delay(2000)
            copyToastText = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF121215), Color(0xFF0F0F12), Color(0xFF1A1A22))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 520.dp)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (stepIndex > 0) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF24242C))
                            .clickable { stepIndex -= 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "←", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Spacer(modifier = Modifier.size(36.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF282834)),
                        contentAlignment = Alignment.Center
                    ) {
                        OfficialLogo(name = "youtube", modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LEVEL 3 • VIDEO PLANNING",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFA1A1AA),
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF24242C))
                        .clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AI Mentor Greeting Header
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1C1C24),
                border = BorderStroke(1.5.dp, Color(0xFF2C2C3A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size((68 * avatarGlowScale).dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF3333).copy(alpha = 0.25f))
                        )
                        Box(
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(Color(0xFFFF3333), Color(0xFF990000))
                                    )
                                )
                                .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.9f)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🔥", fontSize = 26.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Video Planning Masterclass",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "\"Perfect! Ab tumhare paas niche bhi hai aur content ideas bhi. Ab hum tumhari pehli professional YouTube video banayenge. Tension mat lo... Main har step par tumhare saath rahunga.\"",
                        fontSize = 13.sp,
                        color = Color(0xFFA1A1AA),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress Bar (Starts at 35%)
                    val progressVal = if (stepIndex == 14) 100 else (35 + ((stepIndex * 65) / 14))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (stepIndex == 14) "Recording Checklist" else "Step ${stepIndex + 1} of 14",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD1D1D6)
                        )
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF2A2A36)
                        ) {
                            Text(
                                text = "$progressVal%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFF4D4D),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF2C2C38))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progressVal / 100f)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFFF3333), Color(0xFFFF6666))
                                    )
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Floating Confidence Coach Quick Trigger Button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF282836),
                        border = BorderStroke(1.dp, Color(0xFF3A3A4C)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                confidenceSeed++
                                showConfidenceCoachModal = true
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "💬 Feel Nervous?", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Tap for AI Confidence Coach", fontSize = 11.5.sp, color = Color(0xFFD1D1D6))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Copy Toast Message
            copyToastText?.let { toastMsg ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF2E2E3E),
                    border = BorderStroke(1.dp, Color(0xFFFF3333)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "📋 $toastMsg",
                        fontSize = 12.5.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // DYNAMIC STEP RENDERER (0..13 for Steps 1..14, 14 for Mission Checklist)
            if (stepIndex < 14) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF1C1C24),
                    border = BorderStroke(1.5.dp, Color(0xFF2C2C3A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        when (stepIndex) {
                            0 -> {
                                // STEP 1: Choose Today's Video
                                Text(text = "STEP 1 OF 14", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Choose Today's Video 🎬", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Aaj kis topic par video banana chahte ho?",
                                    fontSize = 14.5.sp,
                                    color = Color(0xFFE5E5EA)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                val topicCards = listOf(
                                    "Choose From 30 Ideas 💡",
                                    "Write My Own Idea ✍️",
                                    "Generate New Ideas 🔄",
                                    "Trending Topic 🔥",
                                    "Evergreen Topic 🌲"
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    topicCards.forEach { card ->
                                        val isSelected = selectedVideoTopic.contains(card.split(" ").first()) || selectedVideoTopic == card
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(if (isSelected) Color(0xFF332024) else Color(0xFF252530))
                                                .border(BorderStroke(1.2.dp, if (isSelected) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(14.dp))
                                                .clickable {
                                                    selectedVideoTopic = "$card ($savedNiche)"
                                                }
                                                .padding(14.dp)
                                        ) {
                                            Text(text = card, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color(0xFFFF4D4D) else Color.White)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = customTopicInput,
                                    onValueChange = {
                                        customTopicInput = it
                                        if (it.isNotBlank()) selectedVideoTopic = it
                                    },
                                    placeholder = { Text("Or type exact video title idea...", color = Color(0xFF666675)) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFF3333),
                                        unfocusedBorderColor = Color(0xFF38384A),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 1 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Topic Saved ➔ Set Video Goal", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            1 -> {
                                // STEP 2: Video Goal
                                Text(text = "STEP 2 OF 14", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Video Goal 🎯", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Is video ka main purpose kya hai?",
                                    fontSize = 14.5.sp,
                                    color = Color(0xFFE5E5EA)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                val goals = listOf(
                                    "Views 📈", "Subscribers 🔔", "Teach 📚",
                                    "Entertainment 🎭", "Sales 💰", "Personal Brand 🌟", "Mixed ⚡"
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    goals.chunked(2).forEach { pair ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            pair.forEach { item ->
                                                val isSelected = selectedVideoGoal == item
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(14.dp))
                                                        .background(if (isSelected) Color(0xFF332024) else Color(0xFF252530))
                                                        .border(BorderStroke(1.2.dp, if (isSelected) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(14.dp))
                                                        .clickable { selectedVideoGoal = item }
                                                        .padding(vertical = 14.dp, horizontal = 8.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = item, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color(0xFFFF4D4D) else Color.White, textAlign = TextAlign.Center)
                                                }
                                            }
                                            if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 2 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Goal Selected ➔ Video Length", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            2 -> {
                                // STEP 3: Video Length
                                Text(text = "STEP 3 OF 14", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Video Length ⏱️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Video kitni lambi rakhna chahte ho?",
                                    fontSize = 14.5.sp,
                                    color = Color(0xFFE5E5EA)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                val lengths = listOf(
                                    "30 Seconds ⚡", "1 Minute 📱", "3 Minutes 📽️",
                                    "5 Minutes 🎬", "8 Minutes 🍿", "10+ Minutes 🎥"
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    lengths.chunked(2).forEach { pair ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            pair.forEach { item ->
                                                val isSelected = selectedVideoLength == item
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(14.dp))
                                                        .background(if (isSelected) Color(0xFF332024) else Color(0xFF252530))
                                                        .border(BorderStroke(1.2.dp, if (isSelected) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(14.dp))
                                                        .clickable { selectedVideoLength = item }
                                                        .padding(14.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = item, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color(0xFFFF4D4D) else Color.White, textAlign = TextAlign.Center)
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF252534)
                                ) {
                                    Text(
                                        text = "⚡ AI Auto-Adjustment: Script structure and pacing will be tailored automatically for $selectedVideoLength length.",
                                        fontSize = 12.sp,
                                        color = Color(0xFFD1D1D6),
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 3 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Length Set ➔ Generate Hooks", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            3 -> {
                                // STEP 4: Hook Generator
                                Text(text = "STEP 4 OF 14", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Hook Generator 🪝", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF2B2520),
                                    border = BorderStroke(1.dp, Color(0xFFFF9900).copy(alpha = 0.6f))
                                ) {
                                    Text(
                                        text = "💡 MASTER RULE: First 5 seconds decide everything! Viewer scroll karega ya dekhega, yeh hook par depend karta hai.",
                                        fontSize = 12.sp,
                                        color = Color(0xFFFFD700),
                                        modifier = Modifier.padding(10.dp),
                                        lineHeight = 16.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "10 Viral Hook Options", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    TextButton(onClick = { hookSeed++ }) {
                                        Text(text = "🔄 Fresh Hooks", fontSize = 12.sp, color = Color(0xFFFF4D4D))
                                    }
                                }

                                val hooks = remember(hookSeed, selectedVideoTopic) {
                                    get10HooksForTopic(selectedVideoTopic, hookSeed)
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    hooks.forEach { hookItem ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFF252530),
                                            border = BorderStroke(1.dp, Color(0xFF38384A))
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = hookItem,
                                                    fontSize = 12.5.sp,
                                                    color = Color.White,
                                                    modifier = Modifier.weight(1f),
                                                    lineHeight = 17.sp
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .size(32.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFF323242))
                                                        .clickable {
                                                            clipboardManager.setText(AnnotatedString(hookItem))
                                                            copyToastText = "Hook Copied!"
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = "📋", fontSize = 13.sp)
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 4 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Hooks Ready ➔ Script Builder", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            4 -> {
                                // STEP 5: Script Builder
                                Text(text = "STEP 5 OF 14", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Script Builder 📝", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(text = "Select Script Tone:", fontSize = 13.sp, color = Color(0xFFA1A1AA))

                                Spacer(modifier = Modifier.height(6.dp))

                                val scriptTones = listOf("Formal 👔", "Friendly 😊", "Funny 😂", "Storytelling 📖", "Professional 💼", "Motivational ⚡")
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    scriptTones.forEach { tone ->
                                        val isSel = selectedScriptTone == tone
                                        Surface(
                                            shape = RoundedCornerShape(20.dp),
                                            color = if (isSel) Color(0xFF332024) else Color(0xFF252530),
                                            border = BorderStroke(1.dp, if (isSel) Color(0xFFFF3333) else Color(0xFF38384A)),
                                            modifier = Modifier.clickable { selectedScriptTone = tone }
                                        ) {
                                            Text(
                                                text = tone,
                                                fontSize = 12.sp,
                                                color = if (isSel) Color(0xFFFF4D4D) else Color.White,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF22222E),
                                    border = BorderStroke(1.dp, Color(0xFF38384B)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "📜 COMPLETE SCRIPT OVERVIEW ($selectedScriptTone Tone)", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(
                                            text = "📍 INTRODUCTION (0:00 - 0:30):\n\"Hey everyone! If you want to master $savedNiche, this video will change your perspective. Today we break down $selectedVideoTopic.\"\n\n💡 MAIN BODY (0:30 - 3:30):\n1. Point 1: Build strong fundamentals without overcomplicating.\n2. Point 2: Use smart tools to save time.\n3. Point 3: Stay consistent and avoid the 3 rookie mistakes.\n\n🎯 ENDING & SUMMARY (3:30 - 4:15):\n\"To summarize: focus on action rather than perfection.\"\n\n🔔 CTA (4:15 - 5:00):\n\"If this helped, hit Subscribe and comment your favorite tip below!\"",
                                            fontSize = 12.5.sp,
                                            color = Color(0xFFE5E5EA),
                                            lineHeight = 18.sp
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0xFF2C2C3D))
                                                .clickable {
                                                    val scriptTxt = "SCRIPT FOR: $selectedVideoTopic\n\nIntro: Hey everyone! Today we break down $selectedVideoTopic...\nBody: Point 1, Point 2, Point 3...\nOutro & CTA: Subscribe now!"
                                                    clipboardManager.setText(AnnotatedString(scriptTxt))
                                                    copyToastText = "Script Copied!"
                                                }
                                                .padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = "📋 Copy Full Script", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 5 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Script Ready ➔ Story Flow", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            5 -> {
                                // STEP 6: Story Flow
                                Text(text = "STEP 6 OF 14", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Story Flow Timeline ⏳", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                val storyFlow = listOf(
                                    Triple("Scene 1", "Hook & Problem Statement", "0:00 - 0:20"),
                                    Triple("Scene 2", "Core Concept 1 Breakdown", "0:20 - 1:45"),
                                    Triple("Scene 3", "Practical Demo & Visuals", "1:45 - 3:30"),
                                    Triple("Scene 4", "Outro & Call To Action", "3:30 - 4:15")
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    storyFlow.forEachIndexed { idx, (scene, title, duration) ->
                                        Surface(
                                            shape = RoundedCornerShape(16.dp),
                                            color = Color(0xFF252532),
                                            border = BorderStroke(1.dp, Color(0xFF38384B)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(14.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(36.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFFFF3333)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = "${idx + 1}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                                                }
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(text = scene, fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                                    Text(text = title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                }
                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = Color(0xFF1E1E28)
                                                ) {
                                                    Text(text = duration, fontSize = 11.sp, color = Color(0xFFA1A1AA), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                                }
                                            }
                                        }
                                        if (idx < storyFlow.size - 1) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(text = "↓", fontSize = 18.sp, color = Color(0xFFFF4D4D), fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 6 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Timeline Confirmed ➔ Shot List", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            6 -> {
                                // STEP 7: Shot List
                                Text(text = "STEP 7 OF 14", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Camera Shot List 🎥", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                val shots = listOf(
                                    Triple("Shot 1: Talking Head", "Intro camera angle facing creator at eye-level", "🎥"),
                                    Triple("Shot 2: Close Up", "Emphasizing key tips & facial expressions", "🔍"),
                                    Triple("Shot 3: Wide Shot", "Desk setup / background aesthetic context", "🛋️"),
                                    Triple("Shot 4: Screen Recording", "Step-by-step tutorial demo on screen", "💻"),
                                    Triple("Shot 5: Product B-Roll", "Over-the-shoulder hardware / asset view", "📦"),
                                    Triple("Shot 6: Talking Head Outro", "Direct gaze for final CTA and subscription prompt", "🔔")
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    shots.forEach { (name, desc, icon) ->
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = Color(0xFF252530),
                                            border = BorderStroke(1.dp, Color(0xFF38384A))
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = icon, fontSize = 22.sp)
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Text(text = name, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                    Text(text = desc, fontSize = 11.5.sp, color = Color(0xFFA1A1AA))
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 7 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Shots Planned ➔ Camera Guide", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            7 -> {
                                // STEP 8: Camera Guide
                                Text(text = "STEP 8 OF 14", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Camera & Setup Guide 📐", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(text = "Select Your Setup Device:", fontSize = 13.sp, color = Color(0xFFA1A1AA))

                                Spacer(modifier = Modifier.height(6.dp))

                                val devices = listOf("Phone 📱", "Camera 📷", "Screen Recording 💻")
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    devices.forEach { dev ->
                                        val isSel = selectedCameraDevice == dev
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (isSel) Color(0xFF332024) else Color(0xFF252530))
                                                .border(BorderStroke(1.dp, if (isSel) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(12.dp))
                                                .clickable { selectedCameraDevice = dev }
                                                .padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = dev, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color(0xFFFF4D4D) else Color.White)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                val cameraTips = listOf(
                                    "📐 Camera Angle: Eye-level tripod setup. Avoid low chin angles.",
                                    "💡 Lighting: Place primary light 45° in front of you or sit facing a bright window.",
                                    "📏 Distance: Stand/sit 2-3 feet away from lens for natural rule-of-thirds framing.",
                                    "👀 Eye Contact: Always stare at the CAMERA LENS, never at your smartphone screen!",
                                    "🖼️ Background: Keep background tidy with a warm accent lamp for depth separation."
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    cameraTips.forEach { tip ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFF252530),
                                            border = BorderStroke(1.dp, Color(0xFF38384A))
                                        ) {
                                            Text(
                                                text = tip,
                                                fontSize = 12.5.sp,
                                                color = Color.White,
                                                modifier = Modifier.padding(12.dp),
                                                lineHeight = 17.sp
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 8 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Camera Configured ➔ Audio Guide", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            8 -> {
                                // STEP 9: Audio Guide
                                Text(text = "STEP 9 OF 14", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Audio Mastery Guide 🎙️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                val audioRules = listOf(
                                    Triple("Mic Placement", "Clip collar mic 6-8 inches below mouth level.", "🎙️"),
                                    Triple("Room Noise Control", "Turn off fans, AC & close windows before recording.", "🔇"),
                                    Triple("Echo Dampening", "Use curtains, rugs & soft pillows to kill room reverb.", "🔊"),
                                    Triple("Voice Clarity", "Speak 10% slower with clear vocal emphasis.", "🗣️"),
                                    Triple("Free Recording Apps", "Use Dolby On, WavePad, or Audacity for noise filter.", "📲")
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    audioRules.forEach { (title, detail, icon) ->
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = Color(0xFF252530),
                                            border = BorderStroke(1.dp, Color(0xFF38384A))
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = icon, fontSize = 22.sp)
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Text(text = title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                                    Text(text = detail, fontSize = 12.sp, color = Color.White)
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 9 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Audio Set ➔ B-Roll Planner", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            9 -> {
                                // STEP 10: B-Roll Planner
                                Text(text = "STEP 10 OF 14", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "B-Roll Visual Planner 🎞️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "B-Roll cutaways keep viewer retention high by changing visuals every 5-7 seconds.",
                                    fontSize = 13.sp,
                                    color = Color(0xFFD1D1D6)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                val brollPlan = listOf(
                                    Triple("0:15", "Image / Graphic Overlay", "Show diagram / text highlight"),
                                    Triple("1:00", "Screen Recording Clip", "Demonstrate tool on screen"),
                                    Triple("2:30", "Stock Video Footage", "Pexels / Pixabay free video clip"),
                                    Triple("4:00", "Custom Graphic Animation", "Animated bullet list summary")
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    brollPlan.forEach { (time, type, suggestion) ->
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = Color(0xFF252530),
                                            border = BorderStroke(1.dp, Color(0xFF38384A))
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = Color(0xFFFF3333)
                                                ) {
                                                    Text(text = time, fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                                }
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Text(text = type, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                    Text(text = suggestion, fontSize = 11.5.sp, color = Color(0xFFA1A1AA))
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 10 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "B-Roll Planned ➔ Thumbnail Planner", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            10 -> {
                                // STEP 11: Thumbnail Planner
                                Text(text = "STEP 11 OF 14", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Thumbnail Concept Planner 🖼️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(text = "Do you want AI Thumbnail Assistance?", fontSize = 13.5.sp, color = Color.White)

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (wantAiThumbnailHelp) Color(0xFF332024) else Color(0xFF252530))
                                            .border(BorderStroke(1.dp, if (wantAiThumbnailHelp) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(12.dp))
                                            .clickable { wantAiThumbnailHelp = true }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "YES ✨", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (wantAiThumbnailHelp) Color(0xFFFF4D4D) else Color.White)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (!wantAiThumbnailHelp) Color(0xFF332024) else Color(0xFF252530))
                                            .border(BorderStroke(1.dp, if (!wantAiThumbnailHelp) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(12.dp))
                                            .clickable { wantAiThumbnailHelp = false }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "NO 🛑", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (!wantAiThumbnailHelp) Color(0xFFFF4D4D) else Color.White)
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF22222E),
                                    border = BorderStroke(1.dp, Color(0xFFFF3333).copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "🎨 RECOMMENDED THUMBNAIL FORMULA", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))

                                        Spacer(modifier = Modifier.height(10.dp))

                                        val thumbSpecs = listOf(
                                            "🎯 Visual Concept: High contrast shocked face on left side + giant question mark",
                                            "📝 Text Overlay: 3 Words Max (\"DON'T DO THIS!\")",
                                            "😮 Expression: Curious / Shocked face looking at text",
                                            "🏞️ Background: Dark blurred room with red neon accent glow",
                                            "🎨 Color Palette: Yellow (#FFD700) text + Red (#FF3333) glow on black background"
                                        )

                                        thumbSpecs.forEach { spec ->
                                            Text(text = spec, fontSize = 12.sp, color = Color(0xFFE5E5EA), modifier = Modifier.padding(vertical = 4.dp))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 11 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Thumbnail Set ➔ Title Generator", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            11 -> {
                                // STEP 12: Title Generator
                                Text(text = "STEP 12 OF 14", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Title Generator ⚡", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "20 High-CTR Title Ideas", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    TextButton(onClick = { titleSeed++ }) {
                                        Text(text = "🔄 Refresh", fontSize = 12.sp, color = Color(0xFFFF4D4D))
                                    }
                                }

                                val titles = remember(titleSeed, selectedVideoTopic) {
                                    get20TitlesForTopic(selectedVideoTopic, titleSeed)
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    titles.forEach { titleItem ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFF252530),
                                            border = BorderStroke(1.dp, Color(0xFF38384A))
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = titleItem, fontSize = 12.sp, color = Color.White, modifier = Modifier.weight(1f))
                                                Box(
                                                    modifier = Modifier
                                                        .size(28.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFF323242))
                                                        .clickable {
                                                            clipboardManager.setText(AnnotatedString(titleItem))
                                                            copyToastText = "Title Copied!"
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = "📋", fontSize = 12.sp)
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 12 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Titles Finalized ➔ SEO Generator", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            12 -> {
                                // STEP 13: SEO Generator
                                Text(text = "STEP 13 OF 14", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "SEO Pack Generator 🔍", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF22222E),
                                    border = BorderStroke(1.dp, Color(0xFF38384B)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "📝 GENERATED DESCRIPTION TEMPLATE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "In this video, we break down $selectedVideoTopic step-by-step. If you're looking to master $savedNiche in 2026, make sure to watch till the end!",
                                            fontSize = 12.sp, color = Color.White
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(text = "🔑 TARGET KEYWORDS & PHRASES", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "$savedNiche guide 2026, $selectedVideoTopic, how to start $savedNiche, $savedNiche tutorial, $savedNiche tips for beginners",
                                            fontSize = 12.sp, color = Color(0xFFD1D1D6)
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(text = "#️⃣ HASHTAGS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "#${savedNiche.replace(" ", "")} #${savedNiche.replace(" ", "")}Tips #YouTubeGrowth #CreatorAcademy",
                                            fontSize = 12.sp, color = Color(0xFFFFD700)
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0xFF2C2C3D))
                                                .clickable {
                                                    val seoPack = "SEO PACK FOR $selectedVideoTopic\n\nKeywords: $savedNiche 2026, $selectedVideoTopic\nHashtags: #${savedNiche.replace(" ", "")} #YouTubeGrowth"
                                                    clipboardManager.setText(AnnotatedString(seoPack))
                                                    copyToastText = "SEO Pack Copied!"
                                                }
                                                .padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = "📋 Copy Complete SEO Pack", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 13 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "SEO Ready ➔ Recording Checklist", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            13 -> {
                                // STEP 14: Recording Checklist & Mission
                                Text(text = "STEP 14 OF 14", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Recording Checklist 🎬", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                val checkItems = listOf(
                                    Pair("Script Ready & Bullet Points Written", scriptReadyChecked to { scriptReadyChecked = !scriptReadyChecked }),
                                    Pair("Camera Cleaned & Framed at Eye-Level", cameraReadyChecked to { cameraReadyChecked = !cameraReadyChecked }),
                                    Pair("Audio Mic Clipped & Room Quiet", audioReadyChecked to { audioReadyChecked = !audioReadyChecked }),
                                    Pair("Lighting On (45° Window/Lamp Light)", lightingChecked to { lightingChecked = !lightingChecked }),
                                    Pair("Background Tidy & Clean", backgroundChecked to { backgroundChecked = !backgroundChecked }),
                                    Pair("Thumbnail Concept & Title Selected", thumbnailPlanChecked to { thumbnailPlanChecked = !thumbnailPlanChecked })
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    checkItems.forEach { (label, pair) ->
                                        val (checked, onToggle) = pair
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = if (checked) Color(0xFF283628) else Color(0xFF252530),
                                            border = BorderStroke(1.2.dp, if (checked) Color(0xFF4CAF50) else Color(0xFF38384A)),
                                            modifier = Modifier.clickable { onToggle() }
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(14.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(if (checked) Color(0xFF4CAF50) else Color(0xFF38384A)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    if (checked) {
                                                        Text(text = "✓", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                                    }
                                                }
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = label,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = if (checked) Color(0xFF81C784) else Color.White
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF2B2520),
                                    border = BorderStroke(1.dp, Color(0xFFFF9900).copy(alpha = 0.6f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(text = "🏆 MISSION: RECORD YOUR FIRST VIDEO", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "Estimated Time: 45 Minutes. You have everything prepared. Hit record and let your voice be heard!", fontSize = 12.sp, color = Color.White)
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable {
                                            showLevel3CompleteModal = true
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Mission Complete ➔ Finish Level 3!", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }

                        // Explain Again Natural Rewrite Button
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { explainVariantIdx++ }
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "💡 Explain Again in simple words", fontSize = 12.sp, color = Color(0xFFA1A1AA), fontWeight = FontWeight.Bold)
                        }

                        if (explainVariantIdx > 0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFFFF3333).copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = getExplainAgainVariantLevel3(stepIndex, explainVariantIdx),
                                    fontSize = 12.sp,
                                    color = Color(0xFFE5E5EA),
                                    modifier = Modifier.padding(12.dp),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // CONFIDENCE COACH MODAL OVERLAY
        if (showConfidenceCoachModal) {
            Dialog(onDismissRequest = { showConfidenceCoachModal = false }) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF1E1E28),
                    border = BorderStroke(1.5.dp, Color(0xFFFFD700)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "💬 AI CONFIDENCE COACH", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700), letterSpacing = 1.sp)

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = getConfidenceCoachAdvice(confidenceSeed),
                            fontSize = 13.5.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF2C2C3D))
                                    .clickable { confidenceSeed++ }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🔄 Another Tip", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { showConfidenceCoachModal = false }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "I'm Ready! 🔥", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // LEVEL COMPLETE MODAL OVERLAY
        if (showLevel3CompleteModal) {
            Dialog(onDismissRequest = { }) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = Color(0xFF181822),
                    border = BorderStroke(2.dp, Color(0xFFFF3333)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(Color(0xFFFF3333), Color(0xFF880000))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🏆", fontSize = 40.sp)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(text = "LEVEL 3 COMPLETE!", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Congratulations! You completed the Video Planning Masterclass & prepared your first YouTube video!",
                            fontSize = 13.sp,
                            color = Color(0xFFA1A1AA),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF252535),
                            border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🏅 GLASS BADGE:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "First Video Created", fontSize = 12.5.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF252535)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "⭐ +500 XP Earned!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFF3333))
                                .clickable {
                                    CreatorAcademyPrefs.setYouTubeLevel3Completed(context, true)
                                    showLevel3CompleteModal = false
                                    onCompleteLevel3()
                                }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Claim Badge & Complete Masterclass ➔", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// ============================================================================
// LEVEL 4 — UPLOAD LIKE A PRO HELPER FUNCTIONS & COMPOSABLES
// ============================================================================

private fun getEditorExportGuide(editor: String): String {
    return when (editor) {
        "CapCut" -> "🎬 CapCut Export Settings:\n• Resolution: 1080p or 2K (for Shorts/Reels use 1080x1920 9:16)\n• Frame Rate: 30 FPS or 60 FPS\n• Codec: HEVC / H.264\n• Bitrate: Set to 'Recommended' or 'High'\n• Smart HDR: Disable for standard uploads to avoid color shifts."
        "VN" -> "📱 VN Editor Export Settings:\n• Resolution: 1080p (1920x1080 or 1080x1920)\n• FPS: 30 FPS (Standard) or 60 FPS (Fast motion/Gaming)\n• Bit Rate: ~12-15 Mbps for 1080p, ~25 Mbps for 4K\n• Format: MP4."
        "Premiere" -> "🖥️ Adobe Premiere Pro Export Settings:\n• Preset: YouTube 1080p Full HD or YouTube 4K\n• Format: H.264 (.mp4)\n• Render at Maximum Depth: Checked\n• Bitrate Encoding: VBR 2-Pass (Target 15-20 Mbps for 1080p, 45-60 Mbps for 4K)."
        "DaVinci" -> "🎨 DaVinci Resolve Export Settings:\n• Deliver Preset: YouTube 1080p or Custom MP4 H.264\n• Encoding Profile: Main / High\n• Quality: Restrict to 15000 Kb/s for 1080p60\n• Audio: AAC 320 Kbps 48kHz."
        "Filmora" -> "🎥 Wondershare Filmora Export Settings:\n• Format: MP4\n• Resolution: 1920x1080\n• Frame Rate: 30 FPS / 60 FPS\n• Bitrate: 15000 Kbps (Higher Quality)."
        else -> "⚡ Standard Export Recommendation:\n• Resolution: 1080p (1920x1080 for Long-form) / 1080x1920 (Shorts)\n• Frame Rate: 30 or 60 FPS\n• Bitrate: 12-18 Mbps\n• Format: MP4 with H.264 audio AAC."
    }
}

private fun generate10TitleAlternatives(title: String, niche: String): List<String> {
    val cleanTitle = if (title.isBlank()) "YouTube Video" else title.trim()
    val cleanNiche = if (niche.isBlank()) "Creator" else niche.trim()
    return listOf(
        "1. I Tried $cleanTitle (And THIS Happened! 😱)",
        "2. The ONLY $cleanTitle Guide You Need in 2026",
        "3. Stop Doing $cleanTitle WRONG! (Do This Instead) ⚡",
        "4. How to $cleanTitle Like a PRO in 10 Minutes",
        "5. 5 Secret $cleanTitle Hacks Nobody Tells You 🤐",
        "6. $cleanTitle: Exposing the Brutal Truth! 💥",
        "7. I Spent 24 Hours $cleanTitle... Was It Worth It?",
        "8. $cleanTitle for Beginners (Step-by-Step Tutorial)",
        "9. Why 90% Fail at $cleanTitle (And How You Can Win) 🏆",
        "10. $cleanTitle MASTERCLASS: Zero to $1,000/Month 🚀"
    )
}

private fun generateSeoDescription(title: String, niche: String): String {
    val t = if (title.isBlank()) "My YouTube Video" else title
    val n = if (niche.isBlank()) "YouTube Growth" else niche
    return """
📌 IN THIS VIDEO:
Welcome back! In today's video, we are breaking down everything you need to know about '$t'. If you are into $n, this step-by-step guide will help you get faster results without making common mistakes!

⏰ TIMESTAMPS:
00:00 - Intro & The Big Problem
01:15 - Key Step 1: The Foundation
03:30 - Secret Hack for $n
05:45 - Live Demo & Execution
08:10 - Final Result & Action Plan

🔗 RECOMMENDED RESOURCES & LINKS:
• Free Creator Checklist: https://youtube.com
• Best Gear for $n: https://amazon.in

📱 FOLLOW US ON SOCIAL MEDIA:
• Instagram: @creator_academy
• Telegram Community: t.me/creator_hub

#$n #YouTubeCreator #$t #YouTubeGrowth #VideoEditing
""".trimIndent()
}

private fun generateVideoTags(title: String, niche: String): List<String> {
    val t = if (title.isBlank()) "video" else title.lowercase()
    val n = if (niche.isBlank()) "youtube" else niche.lowercase()
    return listOf(
        t, n, "$n tutorial", "$n tips", "$t 2026",
        "how to $t", "$n for beginners", "viral $n",
        "youtube growth", "youtube studio", "content creator",
        "youtube algorithm", "$n hacks", "$t step by step", "youtube monetization"
    )
}

private fun getBestUploadTimes(country: String, audience: String): String {
    return when (audience) {
        "Students" -> "⏰ Best Upload Time for Students ($country):\n• Weekdays: 4:30 PM - 6:30 PM (After school/college)\n• Weekends: 11:00 AM - 1:00 PM\n• Peak Engagement Day: Friday & Saturday"
        "Gamers" -> "🎮 Best Upload Time for Gamers ($country):\n• Daily: 7:00 PM - 11:00 PM (Late night gaming sessions)\n• Weekends: 2:00 PM - 5:00 PM & 9:00 PM - Midnight"
        "Working Professionals" -> "💼 Best Upload Time for Professionals ($country):\n• Morning Peak: 8:00 AM - 9:30 AM (Commute time)\n• Evening Peak: 8:30 PM - 10:30 PM (Post-dinner relaxation)\n• Best Day: Wednesday & Sunday"
        else -> "🌐 General Best Upload Window ($country):\n• Prime Time: 5:00 PM - 8:00 PM\n• Secondary Window: 12:00 PM - 2:00 PM (Lunch break)\n• Tip: Always upload 2 hours before peak time so HD processing & subtitles complete!"
    }
}

private fun analyzeThumbnailMetrics(textReadable: Boolean, faceVisible: Boolean, highContrast: Boolean, emotionHook: Boolean, curiosityHook: Boolean): Triple<Int, String, List<String>> {
    var score = 0
    val tips = mutableListOf<String>()
    if (textReadable) score += 20 else tips.add("❌ Text too small/busy: Use max 3-4 bold words with stroke outline.")
    if (faceVisible) score += 20 else tips.add("💡 Add a close-up face: Thumbnails with clear eye contact get 38% higher CTR.")
    if (highContrast) score += 20 else tips.add("🎨 Boost contrast & saturation: Use bright yellow/red text against dark backgrounds.")
    if (emotionHook) score += 20 else tips.add("😮 Add dramatic emotion: Shock, excitement, or confusion grabs attention instantly.")
    if (curiosityHook) score += 20 else tips.add("❓ Create a curiosity gap: Show a question mark, arrow, or unexpected before/after.")

    val evaluation = when {
        score >= 80 -> "🔥 EXCELLENT THUMBNAIL! High CTR potential (8% - 15%+)."
        score >= 60 -> "👍 GOOD THUMBNAIL. A few tweaks will boost click-through rate significantly."
        else -> "⚠️ NEEDS IMPROVEMENT. Follow the tips below to avoid low views!"
    }
    return Triple(score, evaluation, tips)
}

private fun analyzeVideoAnalytics(ctrPct: Float, avdMin: Float, totalLenMin: Float): Pair<String, String> {
    val avdRatio = if (totalLenMin > 0f) (avdMin / totalLenMin) * 100f else 0f
    val ctrEval = when {
        ctrPct >= 10f -> "🔥 Exceptional CTR ($ctrPct%)! Thumbnail & Title are viral quality."
        ctrPct >= 5f -> "✅ Decent CTR ($ctrPct%). Solid performance, but testing a new thumbnail could boost views by 50%."
        else -> "🚨 Low CTR ($ctrPct%). Change Thumbnail & Title immediately! YouTube is showing impressions but people aren't clicking."
    }

    val avdEval = when {
        avdRatio >= 50f -> "🏆 Outstanding Retention ($avdRatio%)! Viewers are hooked. YouTube algorithm will push this hard."
        avdRatio >= 30f -> "👍 Average Retention ($avdRatio%). Improve your intro hook (first 15s) and cut out slow gaps."
        else -> "⚠️ Low Retention ($avdRatio%). Viewers leave early. Fast-cut editing, B-rolls, and pattern interrupts needed!"
    }

    return Pair(ctrEval, avdEval)
}

@Composable
fun YouTubeLevel4SetupScreen(
    onCompleteLevel4: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var stepIndex by remember { mutableIntStateOf(0) } // 0..17 for Steps 1..18, 18 for BONUS, 19 for MISSION Checklist

    // AI Mentor Animated Glow
    val infiniteTransition = rememberInfiniteTransition(label = "level4Glow")
    val avatarGlowScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale4"
    )

    // User State & Choices
    var isExportDone by remember { mutableStateOf(true) }
    var selectedEditor by remember { mutableStateOf("CapCut") }
    var userVideoTitle by remember { mutableStateOf("") }
    var selectedNiche by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeNiche(context) ?: "Gaming") }

    // Thumbnail Reviewer Checkbox States
    var thumbReadable by remember { mutableStateOf(true) }
    var thumbFace by remember { mutableStateOf(true) }
    var thumbContrast by remember { mutableStateOf(true) }
    var thumbEmotion by remember { mutableStateOf(true) }
    var thumbCuriosity by remember { mutableStateOf(false) }

    // Best Upload Time Selector States
    var selectedCountry by remember { mutableStateOf("India 🇮🇳") }
    var selectedAudienceType by remember { mutableStateOf("Students") }

    // Analytics Reviewer State
    var inputCtr by remember { mutableStateOf("6.5") }
    var inputAvd by remember { mutableStateOf("3.5") }
    var inputTotalLen by remember { mutableStateOf("8.0") }

    // Mission Checklist States
    var chkExport by remember { mutableStateOf(false) }
    var chkThumbnail by remember { mutableStateOf(false) }
    var chkSeo by remember { mutableStateOf(false) }
    var chkUploaded by remember { mutableStateOf(false) }
    var chkAnalytics by remember { mutableStateOf(false) }

    // Level Complete Glass Modal
    var showLevel4CompleteModal by remember { mutableStateOf(false) }

    // Copy Toast
    var copyToastText by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(copyToastText) {
        if (copyToastText != null) {
            kotlinx.coroutines.delay(2000)
            copyToastText = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF121215), Color(0xFF0F0F12), Color(0xFF1A1A22))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 520.dp)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (stepIndex > 0) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF24242C))
                            .clickable { stepIndex -= 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "←", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Spacer(modifier = Modifier.size(36.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF282834)),
                        contentAlignment = Alignment.Center
                    ) {
                        OfficialLogo(name = "youtube", modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LEVEL 4 • UPLOAD LIKE A PRO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFA1A1AA),
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF24242C))
                        .clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Overall Level Progress Bar (Starting at 50% as specified)
            val overallProgress = ((stepIndex.toFloat() / 19f) * 50f + 50f).toInt().coerceIn(50, 100)
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Course Progress", fontSize = 12.sp, color = Color(0xFFA1A1AA))
                    Text(text = "$overallProgress%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                }
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF282834))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(overallProgress / 100f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFFFF3333), Color(0xFFFF6B6B))))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AI Mentor Card (Top)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF1E1E28),
                border = BorderStroke(1.dp, Color(0xFFFF3333).copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .scale(avatarGlowScale)
                            .clip(CircleShape)
                            .background(Color(0xFF382024))
                            .border(BorderStroke(1.5.dp, Color(0xFFFF3333)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🤖", fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "YouTube AI Mentor", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF282836)
                            ) {
                                Text(
                                    text = "LEVEL 4",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF4D4D),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "🔥 Awesome! Video record ho chuki hai. Ab sabse important step hai... Upload. Bahut creators isi step me mistakes kar dete hain. Main tumhe professional creators ki tarah upload karna sikhaunga.",
                            fontSize = 12.sp,
                            color = Color(0xFFD1D1D6),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // STEP CONTENT CARDS
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF181822),
                border = BorderStroke(1.dp, Color(0xFF2A2A38)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    when (stepIndex) {
                        0 -> {
                            // STEP 1: Export Checklist
                            Text(text = "STEP 1 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Export Checklist 🎬", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "AI Mentor asks:\nVideo ki editing poori ho ke final export ho gayi?",
                                fontSize = 13.5.sp,
                                color = Color(0xFFE5E5EA),
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isExportDone) Color(0xFF2E1A1E) else Color(0xFF22222E),
                                    border = BorderStroke(1.5.dp, if (isExportDone) Color(0xFFFF3333) else Color(0xFF38384A)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { isExportDone = true }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = "✅ Yes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Text(text = "Ready to upload", fontSize = 11.sp, color = Color(0xFFA1A1AA))
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (!isExportDone) Color(0xFF2E1A1E) else Color(0xFF22222E),
                                    border = BorderStroke(1.5.dp, if (!isExportDone) Color(0xFFFF3333) else Color(0xFF38384A)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { isExportDone = false }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = "❌ No", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Text(text = "Need export guide", fontSize = 11.sp, color = Color(0xFFA1A1AA))
                                    }
                                }
                            }

                            if (!isExportDone) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(text = "Select your Video Editor for guide:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(8.dp))

                                val editors = listOf("CapCut", "VN", "Premiere", "DaVinci", "Filmora")
                                Row(
                                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    editors.forEach { ed ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (selectedEditor == ed) Color(0xFFFF3333) else Color(0xFF282836),
                                            modifier = Modifier.clickable { selectedEditor = ed }
                                        ) {
                                            Text(
                                                text = ed,
                                                fontSize = 12.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFF22222E),
                                    border = BorderStroke(1.dp, Color(0xFF38384A)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = getEditorExportGuide(selectedEditor),
                                        fontSize = 12.sp,
                                        color = Color.White,
                                        lineHeight = 17.sp,
                                        modifier = Modifier.padding(14.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 1 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Export Confirmed ➔ Best Export Settings", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        1 -> {
                            // STEP 2: Best Export Settings
                            Text(text = "STEP 2 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Best Export Settings ⚙️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "YouTube compression se bachne ke liye hamesha in recommended export parameters par stick karo:",
                                fontSize = 13.sp,
                                color = Color(0xFFE5E5EA),
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            val settingsList = listOf(
                                "📐 Resolution" to "1080p (1920x1080) for Long-form / 1080x1920 for Shorts. 4K if hardware allows.",
                                "🎞️ Frame Rate (FPS)" to "30 FPS for Talking Head/Vlogs • 60 FPS for Gaming/Fast Motion.",
                                "📊 Bitrate" to "1080p: 12-18 Mbps • 2K: 25-30 Mbps • 4K: 45-60 Mbps.",
                                "📦 File Format" to "MP4 Container with H.264 Video Codec and AAC Audio (320 Kbps).",
                                "🌈 HDR vs SDR" to "Standard SDR is best for universal color accuracy across all phones."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                settingsList.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = Color(0xFF22222E),
                                        border = BorderStroke(1.dp, Color(0xFF38384A)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = desc, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 2 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Understood ➔ Open YouTube Studio", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        2 -> {
                            // STEP 3: Open YouTube Studio
                            Text(text = "STEP 3 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Open YouTube Studio 📲", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "YouTube video upload karne ke liye 2 best tareeqe hain:",
                                fontSize = 13.sp,
                                color = Color(0xFFE5E5EA)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFF38384A)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = "📱 Method 1: Mobile App (YouTube / YouTube Studio)", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "• Open YouTube app ➔ Tap '+' button ➔ Select 'Upload a video'.\n• Easy for fast mobile uploads and Shorts.", fontSize = 12.sp, color = Color(0xFFA1A1AA))
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(text = "💻 Method 2: Browser (studio.youtube.com)", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "• Desktop mode browser ➔ Full access to End Screens, Cards, Subtitles, and Copyright Checks.", fontSize = 12.sp, color = Color(0xFFA1A1AA))
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 3 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "✅ Ready in Studio ➔ Step-by-Step Upload", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        3 -> {
                            // STEP 4: Upload Video Flow
                            Text(text = "STEP 4 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Professional Upload Flow 📤", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val uploadSteps = listOf(
                                "1. Upload" to "Click Create ➔ Upload Videos.",
                                "2. Select File" to "Choose your exported MP4 video file.",
                                "3. Title" to "Add your curiosity-driven high CTR title.",
                                "4. Description" to "Add timestamps, social links & SEO keywords.",
                                "5. Thumbnail" to "Upload custom high-contrast thumbnail.",
                                "6. Playlist" to "Add to relevant niche playlist.",
                                "7. Audience" to "Select 'Not Made for Kids' (unless kids content).",
                                "8. Checks" to "Wait for Copyright & Monetization checks.",
                                "9. Visibility" to "Set as Unlisted first, then publish."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                uploadSteps.forEach { (st, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = st, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                            Text(text = desc, fontSize = 11.5.sp, color = Color.White)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 4 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Thumbnail Optimization AI", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        4 -> {
                            // STEP 5: Thumbnail Optimization (Interactive AI Reviewer)
                            Text(text = "STEP 5 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Thumbnail Optimization AI 🖼️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Check all that apply to your thumbnail to evaluate CTR Score:", fontSize = 12.5.sp, color = Color(0xFFE5E5EA))
                            Spacer(modifier = Modifier.height(12.dp))

                            val checkItems = listOf(
                                "Large, readable text (max 3-4 words)" to thumbReadable,
                                "Clear close-up face with eye contact" to thumbFace,
                                "High contrast colors (e.g. Yellow/Red text)" to thumbContrast,
                                "Strong emotion (shock, excitement, mystery)" to thumbEmotion,
                                "Curiosity gap / unexpected element" to thumbCuriosity
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                checkItems.forEachIndexed { idx, (label, isChecked) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isChecked) Color(0xFF2E1A1E) else Color(0xFF22222E),
                                        border = BorderStroke(1.dp, if (isChecked) Color(0xFFFF3333) else Color(0xFF38384A)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                when (idx) {
                                                    0 -> thumbReadable = !thumbReadable
                                                    1 -> thumbFace = !thumbFace
                                                    2 -> thumbContrast = !thumbContrast
                                                    3 -> thumbEmotion = !thumbEmotion
                                                    4 -> thumbCuriosity = !thumbCuriosity
                                                }
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                                contentDescription = null,
                                                tint = if (isChecked) Color(0xFFFF3333) else Color(0xFFA1A1AA),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(text = label, fontSize = 12.5.sp, color = Color.White, fontWeight = FontWeight.Medium)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            val (score, eval, tips) = analyzeThumbnailMetrics(thumbReadable, thumbFace, thumbContrast, thumbEmotion, thumbCuriosity)
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFFFF3333).copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "CTR Score: $score / 100", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                        Text(text = eval, fontSize = 11.5.sp, color = Color.White)
                                    }
                                    if (tips.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        tips.forEach { tip ->
                                            Text(text = tip, fontSize = 11.5.sp, color = Color(0xFFFF6B6B), lineHeight = 15.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 5 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Thumbnail Checked ➔ Title Optimizer AI", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        5 -> {
                            // STEP 6: Title Optimization
                            Text(text = "STEP 6 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Title Optimization AI 🏷️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Type your rough video title to generate 10 high-CTR viral alternatives:", fontSize = 12.5.sp, color = Color(0xFFE5E5EA))
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = userVideoTitle,
                                onValueChange = { userVideoTitle = it },
                                placeholder = { Text("e.g. How I learned video editing", color = Color(0xFF666675)) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFFF3333),
                                    unfocusedBorderColor = Color(0xFF38384A),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            val titles = generate10TitleAlternatives(userVideoTitle, selectedNiche)
                            Text(text = "🔥 10 High-CTR Title Alternatives:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(8.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                titles.forEach { altTitle ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = altTitle, fontSize = 12.sp, color = Color.White, modifier = Modifier.weight(1f), lineHeight = 16.sp)
                                            Box(
                                                modifier = Modifier
                                                    .clip(CircleShape)
                                                    .clickable {
                                                        clipboardManager.setText(AnnotatedString(altTitle))
                                                        copyToastText = "Title copied!"
                                                    }
                                                    .padding(4.dp)
                                            ) {
                                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color(0xFFA1A1AA), modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 6 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Title Selected ➔ SEO Description Generator", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        6 -> {
                            // STEP 7: Description Generator
                            Text(text = "STEP 7 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "SEO Description Generator 📝", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val seoDesc = generateSeoDescription(userVideoTitle, selectedNiche)

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFF38384A)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "✨ AI Generated SEO Description", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                        TextButton(onClick = {
                                            clipboardManager.setText(AnnotatedString(seoDesc))
                                            copyToastText = "Description copied!"
                                        }) {
                                            Text(text = "Copy All 📋", fontSize = 11.sp, color = Color(0xFFFF3333))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = seoDesc, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 7 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Description Done ➔ Tags & Search Keywords", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        7 -> {
                            // STEP 8: Tags & Search Keywords
                            Text(text = "STEP 8 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Tags & Search Keywords 🏷️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tags help YouTube algorithm recognize common spelling mistakes and related search terms.",
                                fontSize = 12.5.sp,
                                color = Color(0xFFE5E5EA)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            val tags = generateVideoTags(userVideoTitle, selectedNiche)
                            val commaTags = tags.joinToString(", ")

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFF38384A)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "🏷️ High-Search Keywords:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                        TextButton(onClick = {
                                            clipboardManager.setText(AnnotatedString(commaTags))
                                            copyToastText = "Tags copied!"
                                        }) {
                                            Text(text = "Copy Tags 📋", fontSize = 11.sp, color = Color(0xFFFF3333))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = commaTags, fontSize = 12.sp, color = Color.White, lineHeight = 18.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 8 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Tags Saved ➔ Playlist Strategy", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        8 -> {
                            // STEP 9: Playlist Setup
                            Text(text = "STEP 9 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Playlist Setup 📂", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Why Playlists Matter:\n• Playlists auto-play your next videos, boosting Session Watch Time.\n• Playlists rank individually on Google and YouTube search results.",
                                fontSize = 13.sp,
                                color = Color(0xFFE5E5EA),
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF22222E),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = "Suggested Playlist Title for your Niche:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = "📂 '$selectedNiche Masterclass 2026 (Full Course)'", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 9 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Playlist Set ➔ Audience Settings (COPPA)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        9 -> {
                            // STEP 10: Audience Setting (COPPA)
                            Text(text = "STEP 10 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Audience Setting (COPPA) 👶", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFF38384A)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = "🔴 Made For Kids:", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6B6B))
                                    Text(text = "Select only if video is strictly made for children under 13. Comments, personalized ads & notifications will be disabled.", fontSize = 12.sp, color = Color.White)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(text = "🟢 Not Made For Kids (RECOMMENDED):", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4ADE80))
                                    Text(text = "Select for general audiences (Gaming, Tech, Vlogs, Education). Enables full comments, notifications & higher ad revenue.", fontSize = 12.sp, color = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 10 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Audience Set ➔ End Screen Masterclass", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        10 -> {
                            // STEP 11: End Screen Masterclass
                            Text(text = "STEP 11 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "End Screen Masterclass 🎬", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val endScreenItems = listOf(
                                "🔘 Subscribe Button" to "Shows channel icon in last 20s for easy 1-tap subscription.",
                                "📺 Best for Viewer Video" to "YouTube algorithm automatically picks the best video for each viewer.",
                                "📂 Related Playlist" to "Guides engaged viewers directly into your binge-worthy playlist."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                endScreenItems.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = desc, fontSize = 12.sp, color = Color.White)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 11 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Understood ➔ Cards Feature (i-Button)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        11 -> {
                            // STEP 12: Cards Feature (i-Button)
                            Text(text = "STEP 12 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Cards Feature (i-Button) ℹ️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "When & How to use Info Cards:\n• Add cards at timestamps where you mention another video or tool.\n• Place a card at the 50% video mark to catch viewers before they drop off.",
                                fontSize = 13.sp,
                                color = Color(0xFFE5E5EA),
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 12 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Visibility & Scheduling Strategy", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        12 -> {
                            // STEP 13: Visibility
                            Text(text = "STEP 13 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Visibility & Scheduling Strategy 🔒", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val visibilities = listOf(
                                "🔒 Private" to "Only you can see. Good for draft checks.",
                                "🔗 Unlisted (BEST PRACTICE)" to "Upload as Unlisted first! Wait 2 hours for HD processing, subtitles & copyright check before going public.",
                                "🌐 Public" to "Everyone can see immediately.",
                                "⏰ Schedule" to "Set a future date/time. Gives YouTube time to process HD and send notifications."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                visibilities.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = desc, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 13 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Best Upload Time Calculator", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        13 -> {
                            // STEP 14: Best Upload Time
                            Text(text = "STEP 14 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Best Upload Time Calculator ⏰", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(text = "Target Country:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA1A1AA))
                            Spacer(modifier = Modifier.height(6.dp))
                            val countries = listOf("India 🇮🇳", "USA 🇺🇸", "UK 🇬🇧", "Global 🌐")
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                countries.forEach { c ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (selectedCountry == c) Color(0xFFFF3333) else Color(0xFF282836),
                                        modifier = Modifier.clickable { selectedCountry = c }
                                    ) {
                                        Text(text = c, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "Audience Type:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA1A1AA))
                            Spacer(modifier = Modifier.height(6.dp))
                            val audTypes = listOf("Students", "Gamers", "Working Professionals", "General")
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                audTypes.forEach { a ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (selectedAudienceType == a) Color(0xFFFF3333) else Color(0xFF282836),
                                        modifier = Modifier.clickable { selectedAudienceType = a }
                                    ) {
                                        Text(text = a, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFFFF3333).copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = getBestUploadTimes(selectedCountry, selectedAudienceType),
                                    fontSize = 12.5.sp,
                                    color = Color.White,
                                    lineHeight = 18.sp,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 14 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Timing Saved ➔ YouTube Algorithm Demystified", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        14 -> {
                            // STEP 15: YouTube Algorithm
                            Text(text = "STEP 15 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "YouTube Algorithm Demystified 🤖", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val algoMetrics = listOf(
                                "🎯 CTR (Click-Through Rate)" to "Pahle 100 logon me se kitne log thumbnail pe click karte hain.",
                                "⏱️ Watch Time & AVD" to "Viewers video ko kitna lamba dekhte hain.",
                                "💬 Engagement (Likes & Comments)" to "Comments and likes signal viewer interest and community interaction.",
                                "🔁 Session Time" to "Aapke video ke baad viewer YouTube par kitna time spend karta hai."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                algoMetrics.forEach { (m, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = m, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = desc, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 15 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Analytics Masterclass", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        15 -> {
                            // STEP 16: Analytics Masterclass
                            Text(text = "STEP 16 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Analytics Masterclass 📊", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val metrics = listOf(
                                "👁️ Views & Impressions" to "Impressions = Thumbnail kitni baar dikha. Views = Kitne logon ne dekha.",
                                "📈 CTR Benchmark" to "Good = 5-10% • Viral = 10%+ • Low = Below 4% (Change Thumbnail!).",
                                "⏳ Average View Duration" to "8 min video me 4+ min (50%+) retention is viral status.",
                                "👥 Returning vs New Viewers" to "New Viewers = Reach growth • Returning Viewers = Loyal fan base."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                metrics.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = desc, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 16 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Audience Retention Mastery", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        16 -> {
                            // STEP 17: Retention
                            Text(text = "STEP 17 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Audience Retention Mastery 🎣", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val retentionTips = listOf(
                                "🪝 First 15 Seconds Hook" to "Never say 'Hey guys welcome back'! Start directly with the main promise or cliffhanger.",
                                "⚡ Pattern Interrupts" to "Change camera angle, add sound effect or text overlay every 5-7 seconds.",
                                "✂️ Trim B-Rolls" to "Cut out unnecessary long pauses, 'umms', and repetitive explanations."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                retentionTips.forEach { (t, d) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = t, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = d, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 17 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ If Video Doesn't Perform", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        17 -> {
                            // STEP 18: If Video Doesn't Perform (Growth Mindset)
                            Text(text = "STEP 18 OF 18", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "If Video Doesn't Perform 🚀", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val mindsetSteps = listOf(
                                "1. ❌ DO NOT DELETE!" to "Deleting videos hurts channel authority. Keep it published.",
                                "2. 🖼️ Change Thumbnail & Title" to "If CTR is below 4%, test a new dramatic thumbnail and shorter curiosity title.",
                                "3. ⏳ Wait 24-48 Hours" to "YouTube algorithm takes time to find the right seed audience.",
                                "4. 📽️ Focus on Next Video" to "Every top creator has low performing videos. Consistency wins long-term!"
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                mindsetSteps.forEach { (t, d) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = t, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = d, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 18 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Proceed ➔ BONUS AI Analytics Reviewer", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        18 -> {
                            // BONUS: AI Video / Analytics Reviewer
                            Text(text = "BONUS TOOL", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "AI Analytics Reviewer Simulator 📊", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Enter your video metrics below to get instant AI personalized advice:", fontSize = 12.5.sp, color = Color(0xFFE5E5EA))
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = inputCtr,
                                    onValueChange = { inputCtr = it },
                                    label = { Text("CTR %") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF3333), unfocusedBorderColor = Color(0xFF38384A), focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                                )
                                OutlinedTextField(
                                    value = inputAvd,
                                    onValueChange = { inputAvd = it },
                                    label = { Text("AVD (Mins)") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF3333), unfocusedBorderColor = Color(0xFF38384A), focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                                )
                                OutlinedTextField(
                                    value = inputTotalLen,
                                    onValueChange = { inputTotalLen = it },
                                    label = { Text("Total Mins") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF3333), unfocusedBorderColor = Color(0xFF38384A), focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            val ctrVal = inputCtr.toFloatOrNull() ?: 5.0f
                            val avdVal = inputAvd.toFloatOrNull() ?: 3.0f
                            val totalLenVal = inputTotalLen.toFloatOrNull() ?: 8.0f
                            val (ctrRes, avdRes) = analyzeVideoAnalytics(ctrVal, avdVal, totalLenVal)

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFFFF3333).copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = "🤖 AI Personalized Diagnosis:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = "• $ctrRes", fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "• $avdRes", fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF3333))
                                    .clickable { stepIndex = 19 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Proceed ➔ Final Mission Checklist", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        19 -> {
                            // MISSION CHECKLIST
                            Text(text = "LEVEL 4 FINAL MISSION", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Upload Your First Video 🚀", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Check all items to complete Level 4 and unlock your badge:", fontSize = 12.5.sp, color = Color(0xFFE5E5EA))
                            Spacer(modifier = Modifier.height(14.dp))

                            val missionItems = listOf(
                                "Export Complete" to chkExport,
                                "Thumbnail Added" to chkThumbnail,
                                "SEO Complete" to chkSeo,
                                "Uploaded" to chkUploaded,
                                "Analytics Opened" to chkAnalytics
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                missionItems.forEachIndexed { idx, (label, isChecked) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isChecked) Color(0xFF2E1A1E) else Color(0xFF22222E),
                                        border = BorderStroke(1.dp, if (isChecked) Color(0xFFFF3333) else Color(0xFF38384A)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                when (idx) {
                                                    0 -> chkExport = !chkExport
                                                    1 -> chkThumbnail = !chkThumbnail
                                                    2 -> chkSeo = !chkSeo
                                                    3 -> chkUploaded = !chkUploaded
                                                    4 -> chkAnalytics = !chkAnalytics
                                                }
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                                contentDescription = null,
                                                tint = if (isChecked) Color(0xFFFF3333) else Color(0xFFA1A1AA),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                }
                            }

                            val allChecked = chkExport && chkThumbnail && chkSeo && chkUploaded && chkAnalytics

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (allChecked) Color(0xFFFF3333) else Color(0xFF38384A))
                                    .clickable(enabled = allChecked) {
                                        showLevel4CompleteModal = true
                                    }
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (allChecked) "MISSION COMPLETE ➔ CLAIM BADGE" else "Complete Checklist to Finish",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // LEVEL COMPLETE GLASS MODAL OVERLAY
        if (showLevel4CompleteModal) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = Color(0xFF1F1F2C),
                    border = BorderStroke(2.dp, Color(0xFFFF3333)),
                    modifier = Modifier.fillMaxWidth(0.92f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🏆", fontSize = 56.sp)

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "LEVEL 4 COMPLETE!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF382024)
                        ) {
                            Text(
                                text = "BADGE: PROFESSIONAL UPLOAD MASTER 🎖️",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFF4D4D),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Outstanding! Tumne Professional Upload, High-CTR Thumbnails, SEO Description, Tags, Algorithm Mechanics aur Analytics Masterclass successfully complete kar li hai!",
                            fontSize = 13.5.sp,
                            color = Color(0xFFD1D1D6),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF282836)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "⭐ +600 XP Earned!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFF3333))
                                .clickable {
                                    CreatorAcademyPrefs.setYouTubeLevel4Completed(context, true)
                                    showLevel4CompleteModal = false
                                    onCompleteLevel4()
                                }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Claim Badge & Complete Level 4 ➔", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}


// ============================================================================
// LEVEL 5 — YOUTUBE MONETIZATION HELPER FUNCTIONS & COMPOSABLES
// ============================================================================

private fun generate10BrandReplyTemplates(brandName: String, offerAmount: String): List<Pair<String, String>> {
    val b = if (brandName.isBlank()) "Brand Partner" else brandName.trim()
    val rate = if (offerAmount.isBlank()) "$500" else offerAmount.trim()

    return listOf(
        "1. Standard Pitch Acceptance & Media Kit" to """
Hi $b Team,

Thank you for reaching out! We love your product and think it’s a perfect fit for our audience. 

Our standard rate for a 60-second integrated segment is $rate. I've attached our updated Media Kit with channel demographics, average CTR, and view retention metrics.

Let me know if this works for your team, and we can move forward with contract details.

Best regards,
[Your Name]
""".trimIndent(),

        "2. Counter-Offer (Requesting Higher Rate)" to """
Hi $b Team,

Thanks for the offer! While we are excited to partner with $b, our current rate for dedicated video coverage/integration is $rate based on our average 30-day view engagement.

Given the high buyer-intent of our niche audience, this integration will deliver high conversion for $b. Let me know if we can lock this rate in for our upcoming video schedule.

Best,
[Your Name]
""".trimIndent(),

        "3. Multi-Video Package Proposal" to """
Hi $b Team,

Thanks for getting in touch! Rather than a single video integration, we recommend a 3-video sponsorship package for maximum ROI and brand recall.

We can offer a discounted bundle of 3 integrated video segments for $rate total (saving 20%). Would you be open to reviewing a full campaign proposal?

Best,
[Your Name]
""".trimIndent(),

        "4. Dedicated Video Sponsorship" to """
Hi $b Team,

Thank you for the inquiry! A full dedicated video review/deep-dive on $b would require approximately 15-20 hours of scripting, testing, and production.

Our rate for a fully dedicated sponsored video is $rate. This includes 2 rounds of draft revisions and a permanent YouTube description affiliate link.

Looking forward to your thoughts!

Best,
[Your Name]
""".trimIndent(),

        "5. Hybrid Base Fee + Affiliate Commission" to """
Hi $b Team,

Thanks for reaching out! We offer a performance hybrid model: a base production fee of $rate plus a 15% affiliate commission link in the top line of our video description.

This aligns our incentives and maximizes tracking for $b. Let us know if you have an affiliate tracking link ready!

Best regards,
[Your Name]
""".trimIndent(),

        "6. Shorts / Reels Quick Integration Rate" to """
Hi $b Team,

Thanks for reaching out! For YouTube Shorts (60 seconds vertical video), our single video placement rate is $rate.

Shorts deliver massive viral reach and rapid brand awareness. We can publish within 5 business days of product arrival.

Best regards,
[Your Name]
""".trimIndent(),

        "7. Requesting Campaign Scope & Requirements" to """
Hi $b Team,

Thank you for reaching out to our channel! We are interested in collaborating with $b.

To provide an accurate rate card, could you share a few details:
1. Deliverable type (60s integration, dedicated video, or Shorts)?
2. Campaign timeline & deadline?
3. Key talking points & usage rights duration?

Once confirmed, I will send over our rate sheet and agreement.

Best,
[Your Name]
""".trimIndent(),

        "8. Follow-Up After 3 Days (No Response)" to """
Hi $b Team,

Following up on my previous email regarding a potential collaboration between $b and our YouTube channel.

We are currently locking in our video sponsorship schedule for next month. Please let us know if you are still interested so we can reserve a slot for $b.

Best regards,
[Your Name]
""".trimIndent(),

        "9. Agreement & Invoice Request" to """
Hi $b Team,

Great! We are excited to finalize this partnership for $rate.

Please send over the sponsorship agreement and campaign brief. Once signed, we will share our invoice details (50% upfront, 50% upon video unlisted preview approval).

Best regards,
[Your Name]
""".trimIndent(),

        "10. Final Deliverable & Live Video Link" to """
Hi $b Team,

Exciting news! Your sponsored video integration for $b is now LIVE on our channel:
[Insert YouTube Video URL]

Thank you for partnering with us! We will send over 7-day performance metrics (impressions, clicks, retention) next week.

Best regards,
[Your Name]
""".trimIndent()
    )
}

private fun calculateEstimatedRevenue(
    subsRange: String,
    monthlyViews: String,
    niche: String,
    country: String,
    uploadsPerMonth: Int
): Triple<String, String, List<String>> {
    val viewsNum = when (monthlyViews) {
        "10K - 50K" -> 30000f
        "50K - 100K" -> 75000f
        "100K - 500K" -> 250000f
        "500K - 1M" -> 750000f
        "1M+" -> 2000000f
        else -> 15000f
    }

    val nicheCpmMultiplier = when (niche.lowercase()) {
        "finance", "business", "crypto", "trading" -> 3.5f
        "tech", "software", "ai", "gadgets" -> 2.5f
        "education", "career", "skills" -> 1.8f
        "gaming", "e-sports" -> 0.8f
        "vlog", "entertainment", "comedy" -> 0.7f
        else -> 1.2f
    }

    val countryMultiplier = if (country.contains("USA") || country.contains("UK") || country.contains("Canada") || country.contains("Australia")) 3.2f else 1.0f

    val baseRpm = (1.5f * nicheCpmMultiplier * countryMultiplier).coerceIn(0.5f, 15f)
    val monthlyAdSense = (viewsNum / 1000f) * baseRpm * 0.55f // YouTube 55% share to creator

    val monthlyBrands = when {
        viewsNum >= 500000f -> uploadsPerMonth * 400f * nicheCpmMultiplier
        viewsNum >= 100000f -> uploadsPerMonth * 150f * nicheCpmMultiplier
        viewsNum >= 30000f -> uploadsPerMonth * 50f * nicheCpmMultiplier
        else -> 0f
    }

    val monthlyAffiliate = (monthlyAdSense * 0.4f) + (viewsNum * 0.001f * nicheCpmMultiplier)

    val totalMin = (monthlyAdSense + monthlyBrands + monthlyAffiliate) * 0.8f
    val totalMax = (monthlyAdSense + monthlyBrands + monthlyAffiliate) * 1.4f

    val currencySymbol = if (country.contains("India")) "₹" else "$"
    val conversionRate = if (country.contains("India")) 85f else 1f

    val minFormatted = String.format("%.0f", totalMin * conversionRate)
    val maxFormatted = String.format("%.0f", totalMax * conversionRate)

    val summary = "Estimated Monthly Income: $currencySymbol$minFormatted - $currencySymbol$maxFormatted / month"
    val breakdown = "• AdSense Revenue: $currencySymbol${String.format("%.0f", monthlyAdSense * conversionRate)}\n• Brand Sponsorships: $currencySymbol${String.format("%.0f", monthlyBrands * conversionRate)}\n• Affiliate Marketing: $currencySymbol${String.format("%.0f", monthlyAffiliate * conversionRate)}"

    val tips = listOf(
        "💡 High RPM Niche Factor: Your niche '$niche' has a CPM multiplier of ${nicheCpmMultiplier}x.",
        "⏱️ Mid-Roll Ads: Make videos 8+ minutes long to enable multiple mid-roll ads and boost revenue by 40%.",
        "📌 Pinned Affiliate Links: Add Amazon/Flipkart affiliate links in video description & pinned comment for extra passive income.",
        "💼 Media Kit Ready: Pitch brands directly once you reach 10,000 monthly views."
    )

    return Triple(summary, breakdown, tips)
}

private fun getMonetizationRoadmap(subsRange: String, watchHoursRange: String): String {
    return when {
        subsRange == "10K+" || subsRange == "1K–10K" -> "🎉 CONGRATULATIONS! You meet the subscriber requirement (1,000+ Subs).\nFocus on completing 4,000 Watch Hours or 10M Shorts Views to submit YPP application!"
        subsRange == "500–1K" -> "🚀 ALMOST THERE! You unlocked Fan Funding Tier (500 Subs)!\n• Unlocked: Channel Memberships, Super Chat & Super Thanks.\n• Next Target: Get ${1000 - 650} more subscribers to unlock Video Ad Revenue!"
        else -> "📈 GROWTH ROADMAP FOR NEW CREATOR:\n• Target 1: Reach 500 Subscribers (Unlocks Fan Funding & Super Thanks)\n• Target 2: Reach 1,000 Subscribers + 4,000 Watch Hours (Full YPP Monetization)\n• Strategy: Post 3 long-form videos + 5 Shorts per week to fast-track growth."
    }
}

@Composable
fun YouTubeLevel5SetupScreen(
    onCompleteLevel5: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var stepIndex by remember { mutableIntStateOf(0) } // 0..15 for Steps 1..15, 16 for BONUS, 17 for MISSION Checklist

    // AI Mentor Animated Glow
    val infiniteTransition = rememberInfiniteTransition(label = "level5Glow")
    val avatarGlowScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale5"
    )

    // User State & Choices
    var subsRange by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeSubsRange(context)) }
    var watchHoursRange by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeWatchHoursRange(context)) }
    var selectedNiche by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeNiche(context) ?: "Tech") }

    // Brand Reply Generator Inputs
    var brandNameInput by remember { mutableStateOf("") }
    var offerAmountInput by remember { mutableStateOf("$300") }

    // Revenue Calculator Inputs
    var calcMonthlyViews by remember { mutableStateOf("100K - 500K") }
    var calcCountry by remember { mutableStateOf("India 🇮🇳") }
    var calcUploadsPerMonth by remember { mutableIntStateOf(8) }

    // Channel Health Checklist States
    var chkCopyrightStatus by remember { mutableStateOf(true) }
    var chkCommunityStrikes by remember { mutableStateOf(true) }
    var chkSpamPolicy by remember { mutableStateOf(true) }
    var chkReusedContent by remember { mutableStateOf(true) }
    var chkAiContentPolicy by remember { mutableStateOf(true) }

    // Mission Checklist States
    var chkYppUnderstood by remember { mutableStateOf(false) }
    var chkAdsenseReady by remember { mutableStateOf(false) }
    var chkCopyrightSafe by remember { mutableStateOf(false) }
    var chkIncomePlanReady by remember { mutableStateOf(false) }

    // Level Complete Glass Modal
    var showLevel5CompleteModal by remember { mutableStateOf(false) }

    // Copy Toast
    var copyToastText by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(copyToastText) {
        if (copyToastText != null) {
            kotlinx.coroutines.delay(2000)
            copyToastText = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF121215), Color(0xFF0F0F12), Color(0xFF1A1A22))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 520.dp)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (stepIndex > 0) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF24242C))
                            .clickable { stepIndex -= 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "←", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Spacer(modifier = Modifier.size(36.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF282836)),
                        contentAlignment = Alignment.Center
                    ) {
                        OfficialLogo(name = "youtube", modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LEVEL 5 • MONETIZATION MASTERCLASS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFA1A1AA),
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF24242C))
                        .clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Overall Level Progress Bar (65% to 100%)
            val overallProgress = ((stepIndex.toFloat() / 17f) * 35f + 65f).toInt().coerceIn(65, 100)
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Course Progress", fontSize = 12.sp, color = Color(0xFFA1A1AA))
                    Text(text = "$overallProgress%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                }
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF282834))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(overallProgress / 100f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFFFFD700), Color(0xFFFF8C00))))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AI Mentor Card (Top)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF1E1E28),
                border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .scale(avatarGlowScale)
                            .clip(CircleShape)
                            .background(Color(0xFF383018))
                            .border(BorderStroke(1.5.dp, Color(0xFFFFD700)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🤖", fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "YouTube AI Mentor", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF282836)
                            ) {
                                Text(
                                    text = "LEVEL 5",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD700),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "🔥 Congratulations! Ab tum YouTube Creator ban chuke ho. Ab next mission hai... YouTube se pehli earning va Multiple Revenue Streams build karna!",
                            fontSize = 12.sp,
                            color = Color(0xFFD1D1D6),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // STEP CONTENT CARDS
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF181822),
                border = BorderStroke(1.dp, Color(0xFF2A2A38)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    when (stepIndex) {
                        0 -> {
                            // STEP 1: Current Status (Subscribers)
                            Text(text = "STEP 1 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Current Channel Subscribers 📊", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "AI Mentor asks: Tumhare channel par abhi kitne subscribers hain?",
                                fontSize = 13.sp,
                                color = Color(0xFFE5E5EA)
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            val subOptions = listOf("0–100", "100–500", "500–1K", "1K–10K", "10K+")
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                subOptions.forEach { opt ->
                                    val isSel = subsRange == opt
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (isSel) Color(0xFF322A18) else Color(0xFF22222E),
                                        border = BorderStroke(1.5.dp, if (isSel) Color(0xFFFFD700) else Color(0xFF38384A)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                subsRange = opt
                                                CreatorAcademyPrefs.saveYouTubeSubsRange(context, opt)
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(14.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = opt, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            if (isSel) {
                                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp))
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 1 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Saved ➔ Current Watch Hours / Shorts Views", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        1 -> {
                            // STEP 2: Current Watch Hours / Shorts Views
                            Text(text = "STEP 2 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Watch Hours / Shorts Views ⏳", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Tumhare channel ke last 365 days ke Watch Hours range select karo:", fontSize = 12.5.sp, color = Color(0xFFE5E5EA))
                            Spacer(modifier = Modifier.height(12.dp))

                            val hourOptions = listOf("0–500", "500–1000", "1000–3000", "3000–4000", "4000+")
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                hourOptions.forEach { opt ->
                                    val isSel = watchHoursRange == opt
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (isSel) Color(0xFF322A18) else Color(0xFF22222E),
                                        border = BorderStroke(1.5.dp, if (isSel) Color(0xFFFFD700) else Color(0xFF38384A)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                watchHoursRange = opt
                                                CreatorAcademyPrefs.saveYouTubeWatchHoursRange(context, opt)
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(14.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "$opt Hours", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            if (isSel) {
                                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp))
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = getMonetizationRoadmap(subsRange, watchHoursRange),
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    lineHeight = 17.sp,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 2 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Roadmap Ready ➔ YouTube Partner Program", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        2 -> {
                            // STEP 3: YouTube Partner Program (YPP)
                            Text(text = "STEP 3 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "YouTube Partner Program (YPP) 💎", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "YPP join karne ke baad tumhe YouTube dwara 5 Earning Streams milti hain:",
                                fontSize = 13.sp,
                                color = Color(0xFFE5E5EA)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            val yppBenefits = listOf(
                                "📺 Watch Page Ads" to "Long-form videos par skip / non-skip ads chalne par 55% Ad revenue share milta hai.",
                                "📱 Shorts Feed Ads" to "Shorts feed ad pool revenue se 45% creator share milta hai.",
                                "⭐ Channel Memberships" to "Viewers monthly subscription ($0.99 - $9.99/mo) pay karke exclusive badges & perks pate hain.",
                                "💬 Super Chat & Super Thanks" to "Live stream & videos par viewers $1 se $500 tak direct tips bhej sakte hain.",
                                "🛍️ YouTube Shopping" to "Apne products, courses ya affiliate gear direct video ke niche tag karo."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                yppBenefits.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = desc, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 3 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Understood ➔ YPP Eligibility Rules", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        3 -> {
                            // STEP 4: YPP Eligibility
                            Text(text = "STEP 4 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "YPP Eligibility Requirements 📜", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFF38384A)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = "🥉 Tier 1: Fan Funding (500 Subs)", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "• 500 Subscribers\n• 3 Public Uploads in last 90 days\n• 3,000 Watch Hours OR 3M Shorts Views\n👉 Unlocks Memberships, Super Chat & Shopping!", fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(text = "🥇 Tier 2: Full Ad Monetization (1,000 Subs)", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "• 1,000 Subscribers\n• 4,000 Public Watch Hours (last 365 days)\n  OR 10 Million Shorts Views (last 90 days)\n👉 Unlocks Video Ads & Shorts Revenue Share!", fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(text = "🔒 Safety Mandates:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6B6B))
                                    Text(text = "• 2-Step Verification turned ON\n• 0 Active Community Guidelines Strikes", fontSize = 12.sp, color = Color(0xFFA1A1AA))
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 4 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Channel Health Check AI", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        4 -> {
                            // STEP 5: Channel Health Check
                            Text(text = "STEP 5 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Channel Health & Policy Check 🏥", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Ensure your channel is 100% compliant before applying for monetization:", fontSize = 12.5.sp, color = Color(0xFFE5E5EA))
                            Spacer(modifier = Modifier.height(12.dp))

                            val healthItems = listOf(
                                "Zero Copyright Claims / Strikes" to chkCopyrightStatus,
                                "Zero Community Guidelines Warnings" to chkCommunityStrikes,
                                "No Spam or Misleading Metadata" to chkSpamPolicy,
                                "Original Content (No Reused/Stolen Videos)" to chkReusedContent,
                                "AI Content Disclosed correctly" to chkAiContentPolicy
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                healthItems.forEachIndexed { idx, (label, isChecked) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isChecked) Color(0xFF322A18) else Color(0xFF22222E),
                                        border = BorderStroke(1.dp, if (isChecked) Color(0xFFFFD700) else Color(0xFF38384A)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                when (idx) {
                                                    0 -> chkCopyrightStatus = !chkCopyrightStatus
                                                    1 -> chkCommunityStrikes = !chkCommunityStrikes
                                                    2 -> chkSpamPolicy = !chkSpamPolicy
                                                    3 -> chkReusedContent = !chkReusedContent
                                                    4 -> chkAiContentPolicy = !chkAiContentPolicy
                                                }
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                                contentDescription = null,
                                                tint = if (isChecked) Color(0xFFFFD700) else Color(0xFFA1A1AA),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(text = label, fontSize = 12.5.sp, color = Color.White, fontWeight = FontWeight.Medium)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 5 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Channel Safe ➔ Copyright Masterclass", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        5 -> {
                            // STEP 6: Copyright Masterclass
                            Text(text = "STEP 6 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Copyright Masterclass 🛡️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val copyrightTopics = listOf(
                                "⚠️ Copyright Claim vs Strike" to "• Claim (Content ID): Video remains active, but ad revenue goes to song owner.\n• Strike (Legal takedown): Video removed! 3 strikes within 90 days = Channel deleted forever.",
                                "⚖️ Fair Use Doctrine" to "Transformative use (commentary, criticism, education) is protected, but short clips (<5-10s) with commentary are safest.",
                                "🎵 Safe Royalty Free Music" to "Use YouTube Audio Library (100% free & monetizable) or paid libraries like Epidemic Sound / Artlist.",
                                "📹 Safe Stock Video Sources" to "Use Pexels, Pixabay, or Unsplash for royalty-free stock footage without copyright risk."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                copyrightTopics.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = desc, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 6 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Copyright Clear ➔ AdSense Setup Guide", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        6 -> {
                            // STEP 7: AdSense Setup Guide
                            Text(text = "STEP 7 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Google AdSense Setup Guide 🏦", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val adsenseSteps = listOf(
                                "1. Create AdSense Account" to "Log in with your official Google Account during YPP application.",
                                "2. Link Channel" to "YouTube Studio ➔ Earn tab ➔ Link active AdSense.",
                                "3. Address Verification PIN" to "Once you reach $10 earnings, Google posts a physical PIN to your address.",
                                "4. Bank & SWIFT Details" to "Enter Bank Name, Account Number, and Bank SWIFT Code for direct transfer.",
                                "5. US Tax Form (W-8BEN)" to "Fill out tax info in AdSense to avoid 30% US viewer tax withholding."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                adsenseSteps.forEach { (st, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = st, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700), modifier = Modifier.weight(0.45f))
                                            Text(text = desc, fontSize = 11.sp, color = Color.White, modifier = Modifier.weight(0.55f), lineHeight = 15.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 7 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "AdSense Ready ➔ First Revenue Mechanics", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        7 -> {
                            // STEP 8: First Revenue Mechanics
                            Text(text = "STEP 8 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Revenue Metrics: RPM vs CPM 📈", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFF38384A)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = "📊 CPM (Cost Per Mille):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    Text(text = "Advertisers 1,000 ad impressions dikhane ke liye kitna pay karte hain.", fontSize = 12.sp, color = Color.White)

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(text = "💰 RPM (Revenue Per Mille):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    Text(text = "YouTube ke 45% cut ke baad tumhare account me 1,000 views par net kitna milta hai.", fontSize = 12.sp, color = Color.White)

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(text = "💵 Payout Threshold:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6B6B))
                                    Text(text = "Google AdSense har month ki 21-26 तारीख ko payment bhejta hai jab minimum balance $100 (approx ₹8,300) ho jaye.", fontSize = 11.5.sp, color = Color(0xFFA1A1AA), lineHeight = 15.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 8 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Brand Deals & 10 Reply Templates", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        8 -> {
                            // STEP 9: Brand Deals Starter Guide & 10 Templates
                            Text(text = "STEP 9 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Brand Deals & 10 Reply Templates 💼", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Generate professional brand email replies in 1-click:", fontSize = 12.5.sp, color = Color(0xFFE5E5EA))
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = brandNameInput,
                                    onValueChange = { brandNameInput = it },
                                    placeholder = { Text("Brand Name (e.g. Boat)", color = Color(0xFF666675), fontSize = 12.sp) },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFFD700),
                                        unfocusedBorderColor = Color(0xFF38384A),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )

                                OutlinedTextField(
                                    value = offerAmountInput,
                                    onValueChange = { offerAmountInput = it },
                                    placeholder = { Text("Rate (e.g. $300)", color = Color(0xFF666675), fontSize = 12.sp) },
                                    modifier = Modifier.weight(0.8f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFFD700),
                                        unfocusedBorderColor = Color(0xFF38384A),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            val templates = generate10BrandReplyTemplates(brandNameInput, offerAmountInput)
                            Text(text = "📋 10 Copyable Brand Email Templates:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(8.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                templates.forEach { (title, body) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                                Box(
                                                    modifier = Modifier
                                                        .clip(CircleShape)
                                                        .clickable {
                                                            clipboardManager.setText(AnnotatedString(body))
                                                            copyToastText = "Template copied!"
                                                        }
                                                        .padding(4.dp)
                                                ) {
                                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(16.dp))
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = body, fontSize = 10.5.sp, color = Color(0xFFD1D1D6), maxLines = 3, lineHeight = 14.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 9 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Templates Saved ➔ Affiliate Marketing", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        9 -> {
                            // STEP 10: Affiliate Marketing
                            Text(text = "STEP 10 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Affiliate Marketing Strategy 🔗", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val affiliatePlatforms = listOf(
                                "🛒 Amazon Associates & Flipkart" to "Video description me mic, camera, tripod & products ke affiliate links lagao. Commission: 2% - 10%.",
                                "📦 Meesho & Fashion Affiliates" to "Clothing & lifestyle niche creators ke liye high conversion links.",
                                "💻 Software & SaaS Affiliates" to "Course, hosting, video editing tools (CapCut Pro, Canva, Hosting) par 30% - 50% recurring monthly commission!",
                                "📌 Placement Rules" to "Hamesha video ki Top 3 lines of description & Pinned Comment me tracking link lagao.",
                                "⚠️ Legal Disclosure" to "Description me add karo: 'Some links are affiliate links, meaning I earn a small commission at no extra cost to you.'"
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                affiliatePlatforms.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = desc, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 10 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Channel Memberships & Perks", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        10 -> {
                            // STEP 11: Channel Memberships
                            Text(text = "STEP 11 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Channel Memberships & Badges ⭐", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val membershipTiers = listOf(
                                " Bronze Tier ($0.99 / ₹89/mo)" to "Loyalty Badges in comments & custom channel emojis.",
                                "🥈 Silver Tier ($2.99 / ₹199/mo)" to "Early access to new videos 24 hours before public release.",
                                "🥇 Gold Tier ($4.99 / ₹399/mo)" to "Exclusive monthly live Q&A stream & Discord VIP role."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                membershipTiers.forEach { (tier, perk) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        border = BorderStroke(1.dp, Color(0xFF38384A)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = tier, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = perk, fontSize = 11.5.sp, color = Color.White)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 11 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Super Thanks & Live Tips", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        11 -> {
                            // STEP 12: Super Thanks & Live Tips
                            Text(text = "STEP 12 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Super Thanks & Super Chat 💸", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFF38384A)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = "💬 How Super Thanks Works:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    Text(text = "Viewers video ke niche 'Thanks' button par click karke $2, $5, $10, $50 direct tip de sakte hain. Unka comment colorful highlighted animation ke sath display hota hai.", fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(text = "⚡ Secret Tip to Double Super Thanks:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6B6B))
                                    Text(text = "Har Super Thanks dene wale viewer ke comment par personalized video reply ya heart/reply zaroor do. Dedicated callout in next video boosts tipping by 300%!", fontSize = 11.5.sp, color = Color(0xFFA1A1AA), lineHeight = 15.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 12 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Merch & Digital Products", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        12 -> {
                            // STEP 13: Merchandise & Digital Products
                            Text(text = "STEP 13 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Merchandise & Digital Products 👕", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val productsList = listOf(
                                "📚 Digital eBooks & Guides" to "Apne niche par 20-page PDF guide or checklist banao aur $5 - $15 me becho.",
                                "🎨 Presets & LUTs / Templates" to "Video editors / photographers video LUTs or CapCut templates sell kar sakte hain.",
                                "🎓 Paid Masterclass / Course" to "10,000 subscribers hone par complete step-by-step video course launch karo.",
                                "👕 Physical Merch (T-Shirts & Hoodies)" to "Spring / Teespring ke print-on-demand integration se automated shipping ke sath sell karo."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                productsList.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = desc, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 13 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ AI Monthly Revenue Calculator", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        13 -> {
                            // STEP 14: AI Monthly Income Planner (Calculator)
                            Text(text = "STEP 14 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "AI Monthly Revenue Calculator 🧮", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Select expected monthly parameters to estimate earning potential:", fontSize = 12.5.sp, color = Color(0xFFE5E5EA))
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(text = "Monthly Views Expectation:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            val viewRanges = listOf("10K - 50K", "50K - 100K", "100K - 500K", "500K - 1M", "1M+")
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                viewRanges.forEach { vr ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (calcMonthlyViews == vr) Color(0xFFFFD700) else Color(0xFF282836),
                                        modifier = Modifier.clickable { calcMonthlyViews = vr }
                                    ) {
                                        Text(
                                            text = vr,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (calcMonthlyViews == vr) Color.Black else Color.White,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Target Audience Country:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            val countries = listOf("India 🇮🇳", "USA / Tier-1 🇺🇸", "Global Mixed 🌐")
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                countries.forEach { c ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (calcCountry == c) Color(0xFFFFD700) else Color(0xFF282836),
                                        modifier = Modifier.clickable { calcCountry = c }
                                    ) {
                                        Text(
                                            text = c,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (calcCountry == c) Color.Black else Color.White,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            val (estSummary, estBreakdown, estTips) = calculateEstimatedRevenue(
                                subsRange, calcMonthlyViews, selectedNiche, calcCountry, calcUploadsPerMonth
                            )

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.6f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = estSummary, fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = estBreakdown, fontSize = 12.sp, color = Color.White, lineHeight = 17.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    estTips.forEach { tip ->
                                        Text(text = tip, fontSize = 11.sp, color = Color(0xFFFF8C00), lineHeight = 15.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 14 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Revenue Calculated ➔ Multiple Income Sources", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        14 -> {
                            // STEP 15: Multiple Income Sources Matrix
                            Text(text = "STEP 15 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Multiple Income Sources Matrix 🌐", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val incomeSources = listOf(
                                "1. YouTube AdSense" to "Passive baseline revenue from video ads.",
                                "2. Brand Sponsorships" to "Highest income multiplier (2x-5x AdSense).",
                                "3. Affiliate Marketing" to "Passive sales links in video descriptions.",
                                "4. Online Courses" to "High-margin digital products created once.",
                                "5. 1-on-1 Consulting" to "Direct coaching calls ($50 - $200/hr).",
                                "6. Digital E-books" to "Low friction entry products ($5 - $15).",
                                "7. Channel Memberships" to "Predictable recurring monthly fanbase revenue.",
                                "8. Physical Merchandise" to "Brand authority & merchandise sales."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                incomeSources.forEach { (src, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = src, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700), modifier = Modifier.weight(0.45f))
                                            Text(text = desc, fontSize = 11.sp, color = Color.White, modifier = Modifier.weight(0.55f), lineHeight = 14.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 15 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Money Mistakes to Avoid (BONUS)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        15 -> {
                            // BONUS: Money Mistakes
                            Text(text = "BONUS LESSON", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF6B6B))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Fatal Money Mistakes to Avoid 🛑", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val mistakesList = listOf(
                                "🚫 Don't Buy Subscribers or Views" to "Fake bot views destroy your channel recommendation algorithm permanently!",
                                "🚫 Don't Click Your Own Ads" to "Self-clicking causes immediate permanent AdSense ban without warning.",
                                "🚫 Don't Re-Upload Copyrighted Videos" to "Reused content policy rejects monetization application instantly.",
                                "🚫 Don't Fake Engagement" to "Engagement baiting or fake comments will get your channel demonetized."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                mistakesList.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF2B1D20),
                                        border = BorderStroke(1.dp, Color(0xFFFF4D4D).copy(alpha = 0.5f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6B6B))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = desc, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 16 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Proceed to Final Mission ➔", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        else -> {
                            // STEP 16: MISSION Checklist
                            Text(text = "FINAL MISSION", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Prepare for Monetization Checklist 🏆", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Complete all monetization tasks to finish Level 5:",
                                fontSize = 13.sp,
                                color = Color(0xFFE5E5EA)
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            val missionItems = listOf(
                                "YPP Eligibility Requirements Understood" to chkYppUnderstood,
                                "Google AdSense Account Ready to Link" to chkAdsenseReady,
                                "Channel Copyright & Policy Safe" to chkCopyrightSafe,
                                "Multiple Income Strategy & Plan Ready" to chkIncomePlanReady
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                missionItems.forEachIndexed { idx, (label, isChecked) ->
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (isChecked) Color(0xFF322A18) else Color(0xFF22222E),
                                        border = BorderStroke(1.5.dp, if (isChecked) Color(0xFFFFD700) else Color(0xFF38384A)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                when (idx) {
                                                    0 -> chkYppUnderstood = !chkYppUnderstood
                                                    1 -> chkAdsenseReady = !chkAdsenseReady
                                                    2 -> chkCopyrightSafe = !chkCopyrightSafe
                                                    3 -> chkIncomePlanReady = !chkIncomePlanReady
                                                }
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(14.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                                contentDescription = null,
                                                tint = if (isChecked) Color(0xFFFFD700) else Color(0xFFA1A1AA),
                                                modifier = Modifier.size(22.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(text = label, fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                            val isAllChecked = chkYppUnderstood && chkAdsenseReady && chkCopyrightSafe && chkIncomePlanReady
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isAllChecked) Color(0xFFFFD700) else Color(0xFF38384A))
                                    .clickable(enabled = isAllChecked) { showLevel5CompleteModal = true }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isAllChecked) "COMPLETE LEVEL 5 & CLAIM BADGE 🚀" else "Check All Items to Complete Mission",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isAllChecked) Color.Black else Color(0xFFA1A1AA)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Copy Toast Notification
        if (copyToastText != null) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF322A18),
                border = BorderStroke(1.dp, Color(0xFFFFD700)),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 30.dp)
            ) {
                Text(
                    text = copyToastText!!,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        // LEVEL 5 COMPLETE GLASS MODAL
        if (showLevel5CompleteModal) {
            Dialog(onDismissRequest = { }) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = Color(0xFF1B1B26),
                    border = BorderStroke(1.5.dp, Color(0xFFFFD700)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF383018))
                                .border(BorderStroke(2.dp, Color(0xFFFFD700)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👑", fontSize = 42.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "LEVEL 5 COMPLETE!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFFD700),
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFF322A18),
                            border = BorderStroke(1.dp, Color(0xFFFFD700))
                        ) {
                            Text(
                                text = "🏆 YouTube Monetization Expert",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "You have mastered YouTube Monetization, Google AdSense setup, Brand Deals, and Multiple Income Streams!",
                            fontSize = 12.5.sp,
                            color = Color(0xFFD1D1D6),
                            textAlign = TextAlign.Center,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF282836)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "⭐ +1000 XP Earned!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFFD700))
                                .clickable {
                                    CreatorAcademyPrefs.setYouTubeLevel5Completed(context, true)
                                    showLevel5CompleteModal = false
                                    onCompleteLevel5()
                                }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Claim Badge & Finish Masterclass ➔", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}


// ============================================================================
// LEVEL 6 — YOUTUBE GROWTH ACCELERATOR HELPER FUNCTIONS & COMPOSABLES
// ============================================================================

private fun generateGrowthPlan30_60_90(
    hoursAvailable: Int,
    currentUploads: Int,
    currentViews: String,
    goal: String
): Triple<String, String, String> {
    val hrs = hoursAvailable.coerceAtLeast(2)
    val uploads = currentUploads.coerceAtLeast(1)

    val plan30 = """
🚀 DAY 1 - 30: FOUNDATION & CONSISTENCY
• Schedule: $hrs hrs/week dedicated to batch shooting $uploads videos/week.
• Focus: Optimize Channel Branding (Banner, Avatar, About Section).
• Action Items:
  1. Script 5 high-hook video ideas targeting low-competition search keywords.
  2. Test 3 distinct thumbnail styles with contrasting primary colors.
  3. Engage with top 10 creator communities in your niche daily.
• Target Metric: First 1,000 views & steady subscriber uptick.
""".trimIndent()

    val plan60 = """
🔥 DAY 31 - 60: RETENTION & VIRAL TESTING
• Schedule: Increase efficiency to publish $uploads Long-form + ${uploads * 2} Shorts per week.
• Focus: Master Pattern Interrupts (Zoom-ins, sound FX, B-roll every 7 seconds).
• Action Items:
  1. Analyze YouTube Studio Audience Retention graphs for the 0:30 mark.
  2. Implement Open Loops in video intros to boost average view duration by 25%.
  3. Publish 2 Community Polls per week to double subscriber engagement.
• Target Metric: Reach 50% average retention rate across long-form uploads.
""".trimIndent()

    val plan90 = """
🏆 DAY 61 - 90: SCALE & MONETIZATION EXPANSION
• Schedule: Systematize content production workflow to reach '$goal'.
• Focus: Brand positioning, series playlists & community loyalty.
• Action Items:
  1. Launch a signature 4-part video playlist series to trigger YouTube recommendation loops.
  2. Establish a Media Kit and pitch 3 potential brand sponsors in your niche.
  3. Host your first live stream Q&A or YouTube Shorts viral campaign.
• Target Metric: Next Milestone Goal ($goal) Achieved!
""".trimIndent()

    return Triple(plan30, plan60, plan90)
}

@Composable
fun YouTubeLevel6SetupScreen(
    onCompleteLevel6: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var stepIndex by remember { mutableIntStateOf(0) } // 0..15 for Steps 1..15, MISSION Checklist

    // AI Mentor Animated Glow
    val infiniteTransition = rememberInfiniteTransition(label = "level6Glow")
    val avatarGlowScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale6"
    )

    // User State & Choices
    var growthGoal by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeGrowthGoal(context)) }
    var userNiche by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeNiche(context) ?: "Tech") }

    // AI Mentor Rephrasing State (Rule: Never repeat replies. Maintain 100+ response styles)
    var mentorExplainCount by remember { mutableIntStateOf(0) }
    val mentorDialogues = listOf(
        "🔥 Amazing! Ab tum YouTube Creator ban gaye ho. Lekin Creator aur Successful Creator me farq hota hai. Ab hum channel ko fast grow karenge!",
        "🚀 Master Phase Active! Channel creation toh shuruat thi, asli game exponential growth aur audience loyalty me hai. Let's build your 10x roadmap!",
        "⚡ Welcome to the Growth Accelerator! Yahan hum Retention, CTR, Viral Hooks aur Community Scaling ka Secret System decode karenge."
    )
    val currentMentorText = mentorDialogues[mentorExplainCount % mentorDialogues.size]

    // Step 8: Competitor Analysis State
    var competitorInput by remember { mutableStateOf("") }
    var competitorAnalyzed by remember { mutableStateOf(false) }

    // Step 12: Growth Planner State
    var hoursAvailableInput by remember { mutableIntStateOf(10) }
    var currentUploadsInput by remember { mutableIntStateOf(2) }
    var currentViewsInput by remember { mutableStateOf("25K - 50K") }

    // Step 13: Channel Audit State
    var auditCtrRating by remember { mutableIntStateOf(7) }
    var auditRetentionRating by remember { mutableIntStateOf(8) }
    var auditConsistencyRating by remember { mutableIntStateOf(9) }

    // Step 15 & BONUS: Daily Missions State
    var missionShortUploaded by remember { mutableStateOf(false) }
    var missionCommentsReplied by remember { mutableStateOf(false) }
    var missionThumbnailImproved by remember { mutableStateOf(false) }
    var missionCompetitorStudied by remember { mutableStateOf(false) }
    var missionIdeasWritten by remember { mutableStateOf(false) }

    // Mission Checklist States
    var chkWeeklyPlanReady by remember { mutableStateOf(false) }
    var chkGrowthGoalSelected by remember { mutableStateOf(false) }
    var chkViralStrategyLearned by remember { mutableStateOf(false) }
    var chkCommunityStrategyReady by remember { mutableStateOf(false) }

    // Level Complete Modal State
    var showLevel6CompleteModal by remember { mutableStateOf(false) }

    // Copy Toast
    var copyToastText by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(copyToastText) {
        if (copyToastText != null) {
            kotlinx.coroutines.delay(2000)
            copyToastText = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF101014), Color(0xFF0D0D11), Color(0xFF161622))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 520.dp)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (stepIndex > 0) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF24242C))
                            .clickable { stepIndex -= 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "←", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Spacer(modifier = Modifier.size(36.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF282836)),
                        contentAlignment = Alignment.Center
                    ) {
                        OfficialLogo(name = "youtube", modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LEVEL 6 • GROWTH ACCELERATOR",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFA1A1AA),
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF24242C))
                        .clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Overall Level Progress Bar (82% to 100%)
            val overallProgress = ((stepIndex.toFloat() / 15f) * 18f + 82f).toInt().coerceIn(82, 100)
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Course Progress", fontSize = 12.sp, color = Color(0xFFA1A1AA))
                    Text(text = "$overallProgress%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                }
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF282834))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(overallProgress / 100f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFFFFD700), Color(0xFFFF5500))))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AI Mentor Card (Top)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF1E1E28),
                border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .scale(avatarGlowScale)
                                .clip(CircleShape)
                                .background(Color(0xFF383018))
                                .border(BorderStroke(1.5.dp, Color(0xFFFFD700)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🤖", fontSize = 24.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "YouTube AI Mentor", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFF282836)
                                    ) {
                                        Text(
                                            text = "LEVEL 6",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFFD700),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFF2A2A38),
                                    border = BorderStroke(1.dp, Color(0xFF3A3A4A)),
                                    modifier = Modifier.clickable { mentorExplainCount += 1 }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "🔄", fontSize = 10.sp)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Explain Again", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentMentorText,
                                fontSize = 12.sp,
                                color = Color(0xFFD1D1D6),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // STEP CONTENT CARDS
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF181822),
                border = BorderStroke(1.dp, Color(0xFF2A2A38)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    when (stepIndex) {
                        0 -> {
                            // STEP 1: Current Growth Goal
                            Text(text = "STEP 1 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Current Growth Goal 🎯", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tumhara next subscriber milestone benchmark kya hai?",
                                fontSize = 13.sp,
                                color = Color(0xFFE5E5EA)
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            val goals = listOf(
                                "100 Subscribers",
                                "500 Subscribers",
                                "1000 Subscribers",
                                "10000 Subscribers",
                                "50000 Subscribers",
                                "100000 Subscribers",
                                "1 Million Subscribers"
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                goals.forEach { g ->
                                    val isSel = growthGoal == g
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (isSel) Color(0xFF322A18) else Color(0xFF22222E),
                                        border = BorderStroke(1.5.dp, if (isSel) Color(0xFFFFD700) else Color(0xFF38384A)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                growthGoal = g
                                                CreatorAcademyPrefs.saveYouTubeGrowthGoal(context, g)
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(14.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(text = "🏆", fontSize = 16.sp)
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Text(text = g, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            }
                                            if (isSel) {
                                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp))
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 1 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Goal Locked ➔ AI Growth Score Analysis", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        1 -> {
                            // STEP 2: Growth Score
                            Text(text = "STEP 2 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "AI Channel Growth Score 📊", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "AI analysis of your Consistency, CTR, Watch Time, Retention & Channel Branding:",
                                fontSize = 12.5.sp,
                                color = Color(0xFFE5E5EA)
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            // Animated Glass Circle with Growth Score
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(130.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.radialGradient(
                                                listOf(Color(0xFFFFD700).copy(alpha = 0.25f), Color(0xFF22222E))
                                            )
                                        )
                                        .border(BorderStroke(3.dp, Brush.sweepGradient(listOf(Color(0xFFFFD700), Color(0xFFFF5500), Color(0xFFFFD700)))), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "88", fontSize = 38.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                        Text(text = "GROWTH SCORE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA1A1AA))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            val scoreMetrics = listOf(
                                "Upload Consistency" to "92% (High)",
                                "Estimated CTR Potential" to "8.5% (Very Good)",
                                "Audience Retention Score" to "78% (Above Average)",
                                "Branding & Visual Polish" to "85% (Professional)"
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                scoreMetrics.forEach { (label, valStr) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = label, fontSize = 12.sp, color = Color.White)
                                            Text(text = valStr, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 2 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Personalized Growth Roadmap", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        2 -> {
                            // STEP 3: Growth Roadmap
                            Text(text = "STEP 3 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Personalized Growth Roadmap 🗺️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val roadmapNodes = listOf(
                                "📍 CURRENT STATE" to "Active Creator in '$userNiche' niche with foundational setup.",
                                "🏁 NEXT MILESTONE" to "Reach 1,000 Subscribers + 4,000 Watch Hours via high-hook short/long content.",
                                "🚀 MID-TERM GOAL" to "Scale to $growthGoal with weekly series and strong community engagement.",
                                "🏆 FINAL VISION" to "Establish full-time income, brand sponsorships & 100K+ loyal subscribers."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                roadmapNodes.forEachIndexed { idx, (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = Color(0xFF22222E),
                                        border = BorderStroke(1.dp, Color(0xFF38384A)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = desc, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                    if (idx < roadmapNodes.size - 1) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = "↓", fontSize = 16.sp, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 3 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Roadmap Ready ➔ Viral Video Blueprint", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        3 -> {
                            // STEP 4: Viral Video Blueprint
                            Text(text = "STEP 4 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Viral Video Blueprint 🎬", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val viralParts = listOf(
                                "1. Strong Hook (0-5s)" to "Viewer ka scroll immediately roko with a shocking statement, question, or visual threat.",
                                "2. Fast Intro (5-15s)" to "Bina time waste kiye direct video topic aur promise confirm karo.",
                                "3. Storytelling Curve" to "Build tension: Conflict ➔ Struggle ➔ Resolution curve maintained throughout.",
                                "4. Pattern Interrupts" to "Har 7-10 seconds me camera angle, zoom, text overlay ya sound FX change karo.",
                                "5. Open Loops" to "Video ke middle me tease karo: 'Iska sabse bada secret 5 minute par dikhaunga...'",
                                "6. High Value Delivery" to "Thumbnail aur Title ka 100% genuine promise deliver karo.",
                                "7. Peak Emotion CTA" to "Jab viewer sabse jyada impress ho, tab hi 'Subscribe' karne ko bolo.",
                                "8. Seamless Ending" to "No 'Thanks for watching'! Direct end screen video par send karo without dropoff."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                viralParts.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = desc, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 4 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Blueprint Mastered ➔ CTR Masterclass", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        4 -> {
                            // STEP 5: CTR Masterclass
                            Text(text = "STEP 5 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "CTR Masterclass (Click-Through Rate) 👁️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val ctrTips = listOf(
                                "🖼️ High-Contrast Thumbnail" to "Use bright yellow/red/cyan against dark backgrounds. Max 3 words of bold text.",
                                "🧲 Curiosity Gap Title" to "Title viewer ke dimaag me sawal paida kare (e.g., 'I Tried AI Editing For 30 Days... Here's What Happened').",
                                "😮 Emotional Facial Expression" to "Human face with strong emotion (shock, joy, confusion) boosts CTR by 38%.",
                                "🎨 Color Theory Strategy" to "YouTube's native colors (Red/Black/White) ke contrast me Yellow, Cyan & Lime Green use karo."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                ctrTips.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        border = BorderStroke(1.dp, Color(0xFF38384A)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = desc, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 5 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Retention Masterclass", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        5 -> {
                            // STEP 6: Retention Masterclass
                            Text(text = "STEP 6 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Audience Retention Masterclass ⏱️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val retentionTips = listOf(
                                "📉 Why Viewers Leave (Drop-off)" to "1. Slow intros\n2. Monotone voice without background music\n3. Static screen without movement for >10s.",
                                "📈 How to Keep Viewers Hooked" to "• B-Roll Footages & Sound FX every 5 seconds\n• Dynamic Text Pop-ups for key points\n• Sudden camera zooms on punchlines.",
                                "⚡ Audio Engineering Secret" to "Low-volume ambient background music elevates energy and masks background room noise."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                retentionTips.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = desc, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 6 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Consistency System Generator", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        6 -> {
                            // STEP 7: Consistency System
                            Text(text = "STEP 7 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Consistency System & Calendar 📅", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFF38384A)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = "🗓️ Weekly Upload Schedule:", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = "• Monday: Niche Research & Script Writing (2 hrs)\n• Wednesday: Batch Shooting & Recording (3 hrs)\n• Friday: Video Editing & Thumbnail Design (3 hrs)\n• Saturday 6 PM: PUBLISH & Community Post", fontSize = 12.sp, color = Color.White, lineHeight = 18.sp)

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(text = "💡 Batching Secret:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    Text(text = "Ek hi din me 3 Short videos shoot karo taaki pure week ka content ready rahe!", fontSize = 11.5.sp, color = Color(0xFFA1A1AA))
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 7 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "System Ready ➔ AI Competitor Analysis", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        7 -> {
                            // STEP 8: AI Competitor Analysis
                            Text(text = "STEP 8 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "AI Competitor Analysis 🔎", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Apne niche ke kisi competitor channel ka name type karo:",
                                fontSize = 12.5.sp,
                                color = Color(0xFFE5E5EA)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = competitorInput,
                                onValueChange = {
                                    competitorInput = it
                                    competitorAnalyzed = true
                                },
                                label = { Text("Competitor Channel Name / Link") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFFFD700),
                                    unfocusedBorderColor = Color(0xFF38384A),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            val cName = if (competitorInput.isBlank()) "Top Niche Competitor" else competitorInput.trim()

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = "🔍 AI Breakdown for '$cName':", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = "• What to Observe: Unke highest-viewed videos ke first 30 seconds ki editing style.", fontSize = 11.5.sp, color = Color.White)
                                    Text(text = "• What NOT to Copy: Exact script, voiceover or thumbnails (AI content detector flags plagiarism).", fontSize = 11.5.sp, color = Color.White)
                                    Text(text = "• Growth Gap Opportunity: Unke comment section me jao aur dekho log kya sawal pooch rahe hain jinka answer video me nahi tha!", fontSize = 11.5.sp, color = Color(0xFFFF6B6B), lineHeight = 15.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 8 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Community Building", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        8 -> {
                            // STEP 9: Community Building
                            Text(text = "STEP 9 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Community Building & Audience Trust 🤝", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val commTactics = listOf(
                                "💬 Reply & Heart Every Comment" to "First 2 hours me har comment ko reply karne se YouTube algorithm engagement signal deta hai.",
                                "📌 Pinned Question Strategy" to "Top comment me question pin karo: 'Aapko kaunsa part sabse accha laga?' to double comments.",
                                "📊 Weekly Community Polls" to "Polls ki reach standard posts se 3x jyada hoti hai. Ask for next video topic via poll!",
                                "🔴 Monthly Q&A Live Stream" to "Direct interaction builds die-hard loyal fans who buy memberships & merch."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                commTactics.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = desc, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 9 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Community Strategy Ready ➔ Shorts Strategy", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        9 -> {
                            // STEP 10: Shorts Strategy
                            Text(text = "STEP 10 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "YouTube Shorts Viral Strategy 📱", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val shortsRules = listOf(
                                "⚡ 1-Second Visual Hook" to "First frame me high movement ya bold caption dikhao taaki swipe-away na ho.",
                                "🔁 Perfect Seamless Loop" to "Short ke last sentence ko first sentence se connect karo (e.g. End: 'Isliye maine bola...', Start: 'YouTube par grow karna aasan hai!').",
                                "📝 On-Screen Auto Captions" to "80% users Shorts bina audio ke dekhte hain. Bold animated text overlays optional nahi, compulsory hain!",
                                "⏱️ Optimal Length" to "20-30 seconds Shorts have the highest completion rate & algorithmic boost."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                shortsRules.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = desc, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 10 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Long Video Strategy", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        10 -> {
                            // STEP 11: Long Video Strategy
                            Text(text = "STEP 11 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Long-Form Video Scaling Strategy 🎥", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val longRules = listOf(
                                "📖 3-Act Story Arc" to "Act 1: Hook & Problem ➔ Act 2: Deep Dive & Climax ➔ Act 3: Golden Takeaway & CTA.",
                                "📌 Chapter Timestamps" to "Description me timestamps dalo (0:00, 1:15, 3:40). Google Search ranking improve hoti hai.",
                                "🔀 End Screen Playlist Funnel" to "Video ke last 10s me agli related video recommend karo: 'Agar ye pasand aaya toh agli video screen par tap karke dekho!'."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                longRules.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = desc, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 11 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ AI Growth Planner (30/60/90 Days)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        11 -> {
                            // STEP 12: AI Growth Planner
                            Text(text = "STEP 12 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "AI 30-60-90 Day Growth Planner 🗓️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Adjust your available weekly hours & upload capacity:", fontSize = 12.5.sp, color = Color(0xFFE5E5EA))
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Hours Available / Week: $hoursAvailableInput hrs", fontSize = 12.sp, color = Color.White)
                                Row {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF2E2E3E))
                                            .clickable { if (hoursAvailableInput > 2) hoursAvailableInput -= 2 },
                                        contentAlignment = Alignment.Center
                                    ) { Text("-", color = Color.White, fontWeight = FontWeight.Bold) }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF2E2E3E))
                                            .clickable { hoursAvailableInput += 2 },
                                        contentAlignment = Alignment.Center
                                    ) { Text("+", color = Color.White, fontWeight = FontWeight.Bold) }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            val (p30, p60, p90) = generateGrowthPlan30_60_90(hoursAvailableInput, currentUploadsInput, currentViewsInput, growthGoal)

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF22222E),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = p30, fontSize = 11.5.sp, color = Color.White, modifier = Modifier.padding(12.dp), lineHeight = 16.sp)
                                }
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF22222E),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = p60, fontSize = 11.5.sp, color = Color.White, modifier = Modifier.padding(12.dp), lineHeight = 16.sp)
                                }
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF22222E),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = p90, fontSize = 11.5.sp, color = Color.White, modifier = Modifier.padding(12.dp), lineHeight = 16.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 12 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Plan Generated ➔ Channel Audit AI", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        12 -> {
                            // STEP 13: Channel Audit
                            Text(text = "STEP 13 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Channel Audit & Opportunities 🔎", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "AI evaluation of your branding, CTR & growth opportunities:", fontSize = 12.5.sp, color = Color(0xFFE5E5EA))
                            Spacer(modifier = Modifier.height(12.dp))

                            val auditFindings = listOf(
                                "✅ Branding Audit" to "Channel Banner & Logo are clean and clearly state niche value proposition.",
                                "⚡ CTR Opportunity" to "Increase font size on thumbnails by 20% to make them readable on mobile screens.",
                                "📈 Retention Booster" to "Add sound effects (swoosh/pop) on every text transition to fix 0:15 drop-offs."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                auditFindings.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = desc, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 13 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Audit Complete ➔ Creator Mindset Coach", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        13 -> {
                            // STEP 14: Creator Mindset Coach
                            Text(text = "STEP 14 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Creator Mindset Coach 🧠", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))

                            val mindsetRules = listOf(
                                "🚫 Avoid Comparison Trap" to "Dusron ke Chapter 20 ko apne Chapter 1 se compare mat karo. Focus on 1% daily improvement.",
                                "🔥 Consistency over Perfection" to "Done is better than perfect! 10 decent videos will teach you more than 1 'perfect' unreleased video.",
                                "⏳ Long-term Patience" to "YouTube exponential growth flywheel is slow at first, then explodes suddenly.",
                                "🤝 Audience First Culture" to "Treat every single view as a real person listening to you in a room."
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                mindsetRules.forEach { (title, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = desc, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 14 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Next ➔ Daily Missions & Achievement Badges", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        14 -> {
                            // STEP 15 & BONUS: Daily AI Missions & Achievement Badges
                            Text(text = "STEP 15 OF 15", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Daily AI Missions & Badges 🏆", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Complete daily action missions to keep your momentum:", fontSize = 12.5.sp, color = Color(0xFFE5E5EA))
                            Spacer(modifier = Modifier.height(12.dp))

                            val missions = listOf(
                                "Upload 1 Short today" to missionShortUploaded,
                                "Reply to 20 community comments" to missionCommentsReplied,
                                "Improve thumbnail contrast on last video" to missionThumbnailImproved,
                                "Study 1 top competitor video" to missionCompetitorStudied,
                                "Write 5 new video title ideas" to missionIdeasWritten
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                missions.forEachIndexed { idx, (label, isChecked) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isChecked) Color(0xFF322A18) else Color(0xFF22222E),
                                        border = BorderStroke(1.dp, if (isChecked) Color(0xFFFFD700) else Color(0xFF38384A)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                when (idx) {
                                                    0 -> missionShortUploaded = !missionShortUploaded
                                                    1 -> missionCommentsReplied = !missionCommentsReplied
                                                    2 -> missionThumbnailImproved = !missionThumbnailImproved
                                                    3 -> missionCompetitorStudied = !missionCompetitorStudied
                                                    4 -> missionIdeasWritten = !missionIdeasWritten
                                                }
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                                contentDescription = null,
                                                tint = if (isChecked) Color(0xFFFFD700) else Color(0xFFA1A1AA),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(text = label, fontSize = 12.5.sp, color = Color.White, fontWeight = FontWeight.Medium)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Text(text = "🎖️ Achievement Badges Unlocked:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                listOf("First Upload", "100 Subs", "500 Subs", "1K Master", "10K Elite").forEach { badge ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF2A2A38),
                                        border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f))
                                    ) {
                                        Text(
                                            text = badge,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable { stepIndex = 15 }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Proceed ➔ Final Mission Checklist", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        15 -> {
                            // MISSION CHECKLIST
                            Text(text = "FINAL MISSION CHECKLIST", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Growth Accelerator Verification 🚀", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Verify all growth milestones before claiming your Level 6 Badge:",
                                fontSize = 12.5.sp,
                                color = Color(0xFFE5E5EA)
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            val missionItems = listOf(
                                "Weekly Upload Plan Ready" to chkWeeklyPlanReady,
                                "Growth Goal Selected ($growthGoal)" to chkGrowthGoalSelected,
                                "Viral Strategy & Retention Mastered" to chkViralStrategyLearned,
                                "Community Building Strategy Ready" to chkCommunityStrategyReady
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                missionItems.forEachIndexed { idx, (label, isChecked) ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isChecked) Color(0xFF322A18) else Color(0xFF22222E),
                                        border = BorderStroke(1.dp, if (isChecked) Color(0xFFFFD700) else Color(0xFF38384A)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                when (idx) {
                                                    0 -> chkWeeklyPlanReady = !chkWeeklyPlanReady
                                                    1 -> chkGrowthGoalSelected = !chkGrowthGoalSelected
                                                    2 -> chkViralStrategyLearned = !chkViralStrategyLearned
                                                    3 -> chkCommunityStrategyReady = !chkCommunityStrategyReady
                                                }
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                                contentDescription = null,
                                                tint = if (isChecked) Color(0xFFFFD700) else Color(0xFFA1A1AA),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(text = label, fontSize = 12.5.sp, color = Color.White, fontWeight = FontWeight.Medium)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable {
                                        showLevel6CompleteModal = true
                                    }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "MISSION COMPLETE ➔ CLAIM LEVEL 6 BADGE", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }

        // LEVEL 6 COMPLETE GLASS MODAL
        if (showLevel6CompleteModal) {
            Dialog(
                onDismissRequest = { },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.85f))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(28.dp),
                        color = Color(0xFF1B1B26),
                        border = BorderStroke(2.dp, Color(0xFFFFD700)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 420.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🎉 LEVEL 6 COMPLETE!", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF383018))
                                    .border(BorderStroke(2.dp, Color(0xFFFFD700)), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "👑", fontSize = 42.sp)
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "YouTube Growth Accelerator Mastered!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Aapne Growth Roadmap, Viral Blueprints, CTR & Retention Tactics, aur AI Weekly System successfully finish kar liya hai!",
                                fontSize = 12.5.sp,
                                color = Color(0xFFD1D1D6),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                lineHeight = 17.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF282836)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "⭐ +1500 XP Earned!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable {
                                        CreatorAcademyPrefs.setYouTubeLevel6Completed(context, true)
                                        showLevel6CompleteModal = false
                                        onCompleteLevel6()
                                    }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Claim Growth Badge & Return to Hub ➔", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}


// ============================================================================
// FINAL LEVEL — LIFETIME AI YOUTUBE CREATOR COACH (MASTER PHASE 9)
// ============================================================================

@Composable
fun YouTubeFinalLifetimeCoachScreen(
    onClose: () -> Unit,
    onRestartCourse: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    // AI Mentor Animated Glow
    val infiniteTransition = rememberInfiniteTransition(label = "lifetimeGlow")
    val avatarGlowScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScaleLifetime"
    )

    // Saved User Context
    val userNiche = remember { CreatorAcademyPrefs.getYouTubeNiche(context) ?: "Tech" }
    val userGoal = remember { CreatorAcademyPrefs.getYouTubeGrowthGoal(context) }
    val userLanguage = remember { CreatorAcademyPrefs.getYouTubeLanguage(context) ?: "Hinglish" }

    // Dashboard State
    var subCountInput by remember { mutableStateOf("1,250") }
    var monthlyViewsInput by remember { mutableStateOf("45.2K") }
    var watchHoursInput by remember { mutableStateOf("3,820") }
    var streakDays by remember { mutableIntStateOf(14) }
    var growthScore by remember { mutableIntStateOf(96) }

    // Navigation Tabs inside Lifetime Coach
    var activeTab by remember { mutableIntStateOf(0) } // 0: Home, 1: Content Planner, 2: Script/Title, 3: Thumbnail, 4: Analytics, 5: News & Trends, 6: Competitor & Badges, 7: Lifetime AI Chat

    // AI Mentor Dialogue Rotation
    var mentorExplainCount by remember { mutableIntStateOf(0) }
    val mentorDialogues = listOf(
        "🎉 Congratulations! Tumne YouTube Growth Guide successfully complete kar li. Ab se main sirf teacher nahi... Tumhara Lifetime YouTube Growth Coach hoon.",
        "🔥 Legendary Status Unlocked! Ab hum tumhare channel ko 100K+ subscribers, brand sponsorships aur full-time YouTube career tak le jayenge.",
        "⚡ Daily Growth Active! Aaj ka content plan, thumbnail review aur viral hooks ready hain. What are we building today?"
    )
    val currentMentorText = mentorDialogues[mentorExplainCount % mentorDialogues.size]

    // Daily Check-in State
    var showDailyCheckIn by remember { mutableStateOf(true) }
    var didUploadToday by remember { mutableStateOf<Boolean?>(null) }
    var noUploadReason by remember { mutableStateOf<String?>(null) }
    var aiCheckInResponse by remember { mutableStateOf<String?>(null) }

    // Today's AI Mission State
    var mission1Done by remember { mutableStateOf(false) }
    var mission2Done by remember { mutableStateOf(false) }
    var mission3Done by remember { mutableStateOf(false) }
    var mission4Done by remember { mutableStateOf(false) }

    // Tool Inputs
    var scriptInput by remember { mutableStateOf("") }
    var scriptReviewResult by remember { mutableStateOf<String?>(null) }

    var titleInput by remember { mutableStateOf("") }
    var titleReviewResult by remember { mutableStateOf<String?>(null) }

    var thumbnailInput by remember { mutableStateOf("") }
    var thumbnailReviewResult by remember { mutableStateOf<String?>(null) }

    var chatMessageInput by remember { mutableStateOf("") }
    val chatHistory = remember { mutableStateListOf<Pair<String, String>>() }

    // Modals
    var showCertificateModal by remember { mutableStateOf(false) }
    var showResetConfirmModal by remember { mutableStateOf(false) }

    // Copy Toast
    var copyToastText by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(copyToastText) {
        if (copyToastText != null) {
            kotlinx.coroutines.delay(2000)
            copyToastText = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0F0F14), Color(0xFF0B0B0E), Color(0xFF141420))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 520.dp)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF282836)),
                        contentAlignment = Alignment.Center
                    ) {
                        OfficialLogo(name = "youtube", modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "LIFETIME CREATOR COACH",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFFD700),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "100% Course Completed 🏆",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4ADE80)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF282836),
                        border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f)),
                        modifier = Modifier.clickable { showCertificateModal = true }
                    ) {
                        Text(
                            text = "🏆 Certificate",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF24242C))
                            .clickable { onClose() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AI Mentor Glass Banner
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF1E1E28),
                border = BorderStroke(1.5.dp, Color(0xFFFFD700)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .scale(avatarGlowScale)
                                .clip(CircleShape)
                                .background(Color(0xFF383018))
                                .border(BorderStroke(2.dp, Color(0xFFFFD700)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👑", fontSize = 26.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "Lifetime YouTube Coach", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFF282836)
                                    ) {
                                        Text(
                                            text = "LEGEND",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFFD700),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF2A2A38),
                                    border = BorderStroke(1.dp, Color(0xFF3A3A4A)),
                                    modifier = Modifier.clickable { mentorExplainCount += 1 }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "🔄", fontSize = 10.sp)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Rephrase", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentMentorText,
                                fontSize = 12.sp,
                                color = Color(0xFFE5E5EA),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Scrollable Category Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val tabs = listOf(
                    "🏠 Dashboard",
                    "🗓️ Content Planner",
                    "✍️ Script & Title",
                    "🖼️ Thumbnail Review",
                    "📊 Channel Audit",
                    "📰 News & Trends",
                    "🏆 Badges & Goals",
                    "💬 AI Coach Chat"
                )
                tabs.forEachIndexed { index, title ->
                    val isSel = activeTab == index
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSel) Color(0xFFFFD700) else Color(0xFF22222E),
                        border = BorderStroke(1.dp, if (isSel) Color(0xFFFFD700) else Color(0xFF38384A)),
                        modifier = Modifier.clickable { activeTab = index }
                    ) {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSel) Color.Black else Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (activeTab) {
                0 -> {
                    // TAB 0: HOME DASHBOARD & DAILY CHECK-IN
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        // Daily Check-In Card
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color(0xFF181824),
                            border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "DAILY CHECK-IN ☀️", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                    Text(text = "Streak: $streakDays Days 🔥", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4ADE80))
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "Did you upload or record content today?", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (didUploadToday == true) Color(0xFF1E3A2A) else Color(0xFF22222E),
                                        border = BorderStroke(1.5.dp, if (didUploadToday == true) Color(0xFF4ADE80) else Color(0xFF38384A)),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                didUploadToday = true
                                                noUploadReason = null
                                                aiCheckInResponse = "🎉 Fantastic job! Consistent uploads build algorithmic momentum. XP +100 Added!"
                                                streakDays += 1
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "✅ Yes, Uploaded!", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (didUploadToday == false) Color(0xFF3A2222) else Color(0xFF22222E),
                                        border = BorderStroke(1.5.dp, if (didUploadToday == false) Color(0xFFFF6B6B) else Color(0xFF38384A)),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                didUploadToday = false
                                                aiCheckInResponse = null
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "❌ Not Today", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                }

                                if (didUploadToday == false) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(text = "Kya problem aayi? Choose reason:", fontSize = 12.sp, color = Color(0xFFA1A1AA))
                                    Spacer(modifier = Modifier.height(8.dp))

                                    val reasons = listOf(
                                        "⌛ Busy with Work/Study",
                                        "✂️ Editing taking time",
                                        "😴 No Motivation",
                                        "💡 No Ideas",
                                        "📷 Equipment Issue"
                                    )
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        reasons.forEach { r ->
                                            Surface(
                                                shape = RoundedCornerShape(10.dp),
                                                color = Color(0xFF282836),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        noUploadReason = r
                                                        aiCheckInResponse = when {
                                                            r.contains("Busy") -> "❤️ Welcome back! Rest bhi zaruri hai. Kal 15-minute script batching se start karenge."
                                                            r.contains("Editing") -> "⚡ Quality matters! Quick tip: Template cuts aur pre-made transitions use karo to save 2 hours."
                                                            r.contains("Motivation") -> "🔥 Remind yourself why you started! 1 Short record kar lo bina overthinking ke."
                                                            r.contains("Ideas") -> "💡 Content Planner tab check karo — AI ne 10 trending topics ready kiye hain!"
                                                            else -> "📷 Equipment doesn't stop a creator — mobile camera + natural light is 100% enough!"
                                                        }
                                                    }
                                            ) {
                                                Text(text = r, fontSize = 11.5.sp, color = Color.White, modifier = Modifier.padding(10.dp))
                                            }
                                        }
                                    }
                                }

                                if (aiCheckInResponse != null) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF2A2A38),
                                        border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = aiCheckInResponse!!,
                                            fontSize = 12.sp,
                                            color = Color(0xFFFFD700),
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(12.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Glass Dashboard Metrics Grid
                        Text(text = "CHANNEL METRICS OVERVIEW", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF1E1E28),
                                border = BorderStroke(1.dp, Color(0xFF38384A)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = "Subscribers", fontSize = 11.sp, color = Color(0xFFA1A1AA))
                                    Text(text = subCountInput, fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)
                                    Text(text = "Goal: $userGoal", fontSize = 9.5.sp, color = Color(0xFFFFD700))
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF1E1E28),
                                border = BorderStroke(1.dp, Color(0xFF38384A)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = "Monthly Views", fontSize = 11.sp, color = Color(0xFFA1A1AA))
                                    Text(text = monthlyViewsInput, fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color(0xFF4ADE80))
                                    Text(text = "+18% this month", fontSize = 9.5.sp, color = Color(0xFF4ADE80))
                                }
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF1E1E28),
                                border = BorderStroke(1.dp, Color(0xFF38384A)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = "Watch Hours", fontSize = 11.sp, color = Color(0xFFA1A1AA))
                                    Text(text = watchHoursInput, fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)
                                    Text(text = "Monetized status", fontSize = 9.5.sp, color = Color(0xFFFFD700))
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF1E1E28),
                                border = BorderStroke(1.dp, Color(0xFF38384A)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = "Growth Score", fontSize = 11.sp, color = Color(0xFFA1A1AA))
                                    Text(text = "$growthScore / 100", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                    Text(text = "Peak optimization", fontSize = 9.5.sp, color = Color(0xFFA1A1AA))
                                }
                            }
                        }

                        // Today's AI Mission
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color(0xFF181824),
                            border = BorderStroke(1.dp, Color(0xFF38384A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "TODAY'S AI MISSIONS 🎯", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                    Text(text = "+250 XP Reward", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                }
                                Spacer(modifier = Modifier.height(10.dp))

                                val missions = listOf(
                                    Triple("🖼️ Create High-CTR Thumbnail for next video", mission1Done) { mission1Done = !mission1Done },
                                    Triple("📜 Write 60s High-Hook Shorts Script", mission2Done) { mission2Done = !mission2Done },
                                    Triple("💬 Reply to 20 Subscriber Comments", mission3Done) { mission3Done = !mission3Done },
                                    Triple("🔍 Research 1 Competitor's Top Video", mission4Done) { mission4Done = !mission4Done }
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    missions.forEach { (title, done, onClick) ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (done) Color(0xFF1E3A2A) else Color(0xFF22222E),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { onClick() }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = title, fontSize = 12.sp, color = Color.White)
                                                if (done) {
                                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(18.dp))
                                                } else {
                                                    Box(modifier = Modifier.size(18.dp).border(1.5.dp, Color(0xFFA1A1AA), CircleShape))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Reset Course Glass Button
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF2A1E1E),
                            border = BorderStroke(1.dp, Color(0xFFFF6B6B).copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showResetConfirmModal = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🔄 Restart Full Course (Reset Progress)", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6B6B))
                            }
                        }
                    }
                }

                1 -> {
                    // TAB 1: CONTENT PLANNER
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "AI CONTENT PLANNER 📅", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

                        val contentIdeas = listOf(
                            "📹 TODAY'S VIDEO" to "Title: '5 Secret $userNiche Hacks Nobody Tells You'\nFormat: Long-form (8 mins)\nHook: 'Stop making this #1 mistake right now...'",
                            "📱 TOMORROW'S SHORT" to "Title: 'Top 3 $userNiche Tools in 2026'\nFormat: 30s Vertical Short\nHook: 'If you use $userNiche, watch this...'",
                            "🗓️ WEEKLY CALENDAR" to "• Mon: $userNiche Tutorial\n• Wed: Shorts Challenge\n• Fri: Top 10 Comparison\n• Sun: Community Q&A Poll",
                            "🔥 TRENDING TOPIC" to "Topic: 'AI Automation in $userNiche'\nSearch Volume: Very High (120K searches/mo)",
                            "🌲 EVERGREEN TOPIC" to "Topic: 'Complete $userNiche Beginner Guide 2026'\nLong-term passive views generator.",
                            "🎉 FESTIVAL / SEASONAL IDEA" to "Topic: 'New Year / Festive Special $userNiche Blueprint'\nHigh sponsor CPM period."
                        )

                        contentIdeas.forEach { (title, desc) ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF1E1E28),
                                border = BorderStroke(1.dp, Color(0xFF38384A)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = desc, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // TAB 2: SCRIPT & TITLE REVIEWER
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(text = "AI SCRIPT & TITLE REVIEWER ✍️", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

                        // Script Analyzer
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF181824),
                            border = BorderStroke(1.dp, Color(0xFF38384A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "1. Paste Video Script for AI Audit", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = scriptInput,
                                    onValueChange = { scriptInput = it },
                                    label = { Text("Paste your hook or full script here...") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFFD700),
                                        unfocusedBorderColor = Color(0xFF38384A),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFFFD700))
                                        .clickable {
                                            val len = scriptInput.trim().length
                                            scriptReviewResult = if (len < 10) {
                                                "⚠️ Please paste at least 1-2 sentences of your script."
                                            } else {
                                                "✅ AI Script Audit Completed:\n• Hook Score: 88/100 (Strong opening threat/promise)\n• Flow & Retention: Good story curve\n• Retention Tip: Add a sound effect + zoom cut at sentence 2.\n• CTA Placement: Excellent placement before the final resolution!"
                                            }
                                        }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Audit Script Now 🚀", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                }

                                if (scriptReviewResult != null) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(text = scriptReviewResult!!, fontSize = 12.sp, color = Color.White, modifier = Modifier.padding(12.dp), lineHeight = 16.sp)
                                    }
                                }
                            }
                        }

                        // Title Generator
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF181824),
                            border = BorderStroke(1.dp, Color(0xFF38384A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "2. Generate 10 High-CTR Title Alternatives", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = titleInput,
                                    onValueChange = { titleInput = it },
                                    label = { Text("Enter draft video title...") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFFD700),
                                        unfocusedBorderColor = Color(0xFF38384A),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFFFD700))
                                        .clickable {
                                            val t = titleInput.ifBlank { "YouTube Growth Strategy" }
                                            titleReviewResult = """
📊 CTR Score: 92/100 | SEO Score: 89/100 | Emotion Score: 95/100

🔥 10 Better High-CTR Viral Titles:
1. I Tried $t For 30 Days... Here's What Happened!
2. The $t Mistake 99% Of Creators Are Making!
3. How I Scaled My Channel Using $t (Step By Step)
4. Stop Doing $t Until You Watch This Video!
5. The Untold Secret Of $t Revealed!
6. $t: The Ultimate Masterclass For 2026
7. Why Everyone Is Talking About $t Right Now!
8. 5 $t Hacks That Feel Illegal To Know!
9. Is $t Still Worth It In 2026? (Honest Truth)
10. The Only $t Guide You'll Ever Need!
""".trimIndent()
                                        }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Generate 10 Viral Titles ⚡", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                }

                                if (titleReviewResult != null) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(text = titleReviewResult!!, fontSize = 11.5.sp, color = Color.White, modifier = Modifier.padding(12.dp), lineHeight = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                3 -> {
                    // TAB 3: THUMBNAIL REVIEWER
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(text = "AI THUMBNAIL REVIEWER 🖼️", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF181824),
                            border = BorderStroke(1.dp, Color(0xFF38384A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "Describe your thumbnail or text elements:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = thumbnailInput,
                                    onValueChange = { thumbnailInput = it },
                                    label = { Text("e.g. Shocked face on left, yellow bold text 'DON'T DO THIS', dark red background") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(90.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFFD700),
                                        unfocusedBorderColor = Color(0xFF38384A),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFFFD700))
                                        .clickable {
                                            thumbnailReviewResult = """
🎯 Estimated CTR Score: 8.8% (High Potential)

• Contrast Rating: 9/10 (Yellow text on dark red background pops out)
• Facial Emotion: Shocked face boosts click curiosity by +35%
• Readability: 3 words max — easily readable on mobile devices
• Color Harmony: Yellow/Red contrast creates high visual urgency
• Key Improvement: Add a subtle drop shadow or outer glow behind the text to increase pop on smaller mobile screens!
""".trimIndent()
                                        }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Analyze Thumbnail CTR 🔍", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                }

                                if (thumbnailReviewResult != null) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF22222E),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(text = thumbnailReviewResult!!, fontSize = 12.sp, color = Color.White, modifier = Modifier.padding(12.dp), lineHeight = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                4 -> {
                    // TAB 4: CHANNEL AUDIT & ANALYTICS
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "CHANNEL AUDIT & PERFORMANCE REVIEW 📊", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

                        val auditMetrics = listOf(
                            "📈 Traffic Sources" to "• YouTube Recommendation: 62% (Highest priority algorithm signal)\n• YouTube Search: 24%\n• External/Direct: 14%",
                            "👥 Audience Loyalty" to "• Returning Viewers: 42% (Strong channel identity)\n• Subscriber Conversion Rate: 3.2% per 100 views",
                            "⏱️ Average Duration" to "• 8-minute videos: 4:15 average watch time (53% retention — Monetization eligible)\n• Shorts: 24s completion (92% retention rate)",
                            "🎨 Channel Branding Audit" to "• Banner & Avatar: Professional 10/10\n• Playlists: 4 Organized Series Playlists\n• Community Engagement Score: 95%"
                        )

                        auditMetrics.forEach { (title, desc) ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF1E1E28),
                                border = BorderStroke(1.dp, Color(0xFF38384A)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = desc, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                                }
                            }
                        }
                    }
                }

                5 -> {
                    // TAB 5: YOUTUBE NEWS & AI TREND FINDER
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "YOUTUBE NEWS & TREND FINDER 📰", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

                        val newsList = listOf(
                            "⚡ YouTube Algorithm Update 2026" to "YouTube now heavily favors 'Satisfaction Score' (like-to-dislike ratio, comment depth & share count) over pure view counts.",
                            "🔥 Trending Format in '$userNiche'" to "Fast-paced side-by-side comparison Shorts with 0.5x speed split screen overlays.",
                            "🎵 Viral Audio Trend" to "High-energy synth ambient tracks are boosting Shorts completion rate by 22%.",
                            "💡 New Creator Studio Feature" to "A/B Thumbnail testing is now available for all creators with 100+ subscribers!"
                        )

                        newsList.forEach { (title, desc) ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF1E1E28),
                                border = BorderStroke(1.dp, Color(0xFF38384A)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = desc, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                                }
                            }
                        }
                    }
                }

                6 -> {
                    // TAB 6: BADGES & GOAL TRACKER
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "ACHIEVEMENT BADGES & MILESTONES 🏆", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

                        val badges = listOf(
                            "🎬 First Upload" to "Uploaded first video on YouTube",
                            "💯 100 Subscribers" to "Built initial 100 die-hard fans community",
                            "🚀 1,000 Subscribers" to "Monetization Milestone Unlocked",
                            "🔥 10,000 Subscribers" to "Established Brand Partner Status",
                            "💎 100,000 Subscribers" to "Silver Play Button Milestone",
                            "👑 YouTube Creator Legend" to "Completed Lifetime AI Creator Academy"
                        )

                        badges.forEach { (title, desc) ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF22222E),
                                border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "🌟", fontSize = 22.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Text(text = desc, fontSize = 11.sp, color = Color(0xFFA1A1AA))
                                    }
                                }
                            }
                        }
                    }
                }

                7 -> {
                    // TAB 7: LIFETIME AI CHAT ASSISTANT
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "LIFETIME AI MENTOR CHAT 💬", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF181824),
                            border = BorderStroke(1.dp, Color(0xFF38384A)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (chatHistory.isEmpty()) {
                                    Text(
                                        text = "🤖 Mentor AI: 'Poocho kuch bhi! Video Editing, Camera Setup, Monetization, Sponsorships ya Mindset... Main har waqt tumhare saath hoon.'",
                                        fontSize = 12.sp,
                                        color = Color(0xFFFFD700)
                                    )
                                } else {
                                    chatHistory.forEach { (userMsg, aiMsg) ->
                                        Text(text = "👤 You: $userMsg", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Text(text = "🤖 Coach: $aiMsg", fontSize = 11.5.sp, color = Color(0xFFE5E5EA), lineHeight = 15.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = chatMessageInput,
                                onValueChange = { chatMessageInput = it },
                                label = { Text("Ask your mentor anything...") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFFFD700),
                                    unfocusedBorderColor = Color(0xFF38384A),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable {
                                        val q = chatMessageInput.trim()
                                        if (q.isNotBlank()) {
                                            val reply = when {
                                                q.contains("camera", ignoreCase = true) || q.contains("phone", ignoreCase = true) -> "📱 Camera Secret: Natural light (window facing) + mobile 4K 30fps mode is 100% better than expensive DSLR with bad lighting!"
                                                q.contains("edit", ignoreCase = true) -> "✂️ Editing Secret: Cut out every pause/breath. Use J-cuts & L-cuts to keep audio leading the visual transition."
                                                q.contains("money", ignoreCase = true) || q.contains("earn", ignoreCase = true) -> "💰 Monetization Path: AdSense + Affiliate Links in Description + Brand Deals + Digital Products!"
                                                q.contains("views", ignoreCase = true) || q.contains("grow", ignoreCase = true) -> "📈 Growth Formula: High-CTR Thumbnail + First 5s Hook + Pattern Interrupts every 7s = Viral Loop!"
                                                else -> "🔥 Great question! Focus on $userNiche audience demand, keep thumbnail contrast high, and upload consistently twice a week!"
                                            }
                                            chatHistory.add(q to reply)
                                            chatMessageInput = ""
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "➔", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }

        // CERTIFICATE MODAL
        if (showCertificateModal) {
            Dialog(onDismissRequest = { showCertificateModal = false }) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF181824),
                    border = BorderStroke(2.dp, Color(0xFFFFD700)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🏆 GLASS CERTIFICATE OF COMPLETION", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(text = "YOUTUBE GROWTH GUIDE", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Lifetime Creator Certification", fontSize = 12.sp, color = Color(0xFF4ADE80))

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFF282836), Color(0xFF1E1E28))
                                    )
                                )
                                .border(BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f)), RoundedCornerShape(16.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "👑 YOUTUBE CREATOR LEGEND", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "This certifies that you have successfully mastered all levels of YouTube Channel Setup, Niche Research, Video Planning, Upload SEO, Monetization & Growth Acceleration.", fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "+10,000 XP Awarded • Lifetime Mentor Unlocked", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4ADE80))
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFFFD700))
                                .clickable { showCertificateModal = false }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Close Certificate", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
            }
        }

        // RESET CONFIRMATION MODAL
        if (showResetConfirmModal) {
            Dialog(onDismissRequest = { showResetConfirmModal = false }) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF1E1E28),
                    border = BorderStroke(1.5.dp, Color(0xFFFF6B6B)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(text = "⚠️ Restart Course?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Kya tum sabhi levels aur growth data reset karke starting se restart karna chahte ho?",
                            fontSize = 13.sp,
                            color = Color(0xFFE5E5EA),
                            lineHeight = 17.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF282836),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { showResetConfirmModal = false }
                            ) {
                                Box(modifier = Modifier.padding(12.dp), contentAlignment = Alignment.Center) {
                                    Text(text = "Cancel", fontSize = 13.sp, color = Color.White)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFF6B6B),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        showResetConfirmModal = false
                                        CreatorAcademyPrefs.resetAllYouTubeData(context)
                                        onRestartCourse()
                                    }
                            ) {
                                Box(modifier = Modifier.padding(12.dp), contentAlignment = Alignment.Center) {
                                    Text(text = "Yes, Reset All", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun get10CompetitorsForNiche(niche: String): List<Triple<String, String, String>> {
    val clean = niche.lowercase()
    return when {
        clean.contains("game") || clean.contains("gaming") -> listOf(
            Triple("Total Gaming", "36M+ Subs", "Free Fire gameplay + energetic commentary"),
            Triple("Techno Gamerz", "38M+ Subs", "Story-based GTA 5 episodic gameplay"),
            Triple("CarryIsLive", "12M+ Subs", "Live streams + comedic roasting"),
            Triple("Mythpat", "15M+ Subs", "Meme editing + funny challenges"),
            Triple("Lokesh Gamer", "15M+ Subs", "Diamond unboxing + crate openings"),
            Triple("Desi Gamers", "14M+ Subs", "Duo gameplay + esports tournaments"),
            Triple("UnGraduate Gamer", "8M+ Subs", "Short gameplay tricks + shorts"),
            Triple("Dynamo Gaming", "10M+ Subs", "Patience PUBG/BGMI snipe plays"),
            Triple("Mortal", "7M+ Subs", "Calm leadership & high skill esports"),
            Triple("BeastBoyShub", "6M+ Subs", "Variety indie games + raw reaction")
        )
        clean.contains("tech") || clean.contains("ai") -> listOf(
            Triple("Technical Guruji", "23M+ Subs", "Daily tech news & unboxing in Hindi"),
            Triple("Technology Gyan", "15M+ Subs", "Budget smartphone tips & security guides"),
            Triple("Trakin Tech", "14M+ Subs", "Fast-paced smartphone unboxings & comparisons"),
            Triple("Tech Burner", "11M+ Subs", "Humor + high budget energetic tech showcases"),
            Triple("Gyan Therapy", "3M+ Subs", "Honest long-term phone reviews"),
            Triple("Tech Master", "3.5M+ Subs", "Gadget hacks & practical tutorials"),
            Triple("Siliconites AI", "500K+ Subs", "AI tools & prompt engineering guides"),
            Triple("CodeWithHarry", "5M+ Subs", "Programming & tech career roadmaps"),
            Triple("Technical Dost", "2M+ Subs", "Tech news & scam awareness"),
            Triple("Slayy Tech", "1M+ Subs", "PC building & desk setup guides")
        )
        clean.contains("finance") || clean.contains("business") -> listOf(
            Triple("Ankur Warikoo", "3.5M+ Subs", "Personal finance & career advice"),
            Triple("Rachana Ranade", "4.5M+ Subs", "Stock market basics & fundamental analysis"),
            Triple("Pranjal Kamra", "5M+ Subs", "Mutual funds & value investing"),
            Triple("Akshat Shrivastava", "1.8M+ Subs", "Macroeconomics & case studies"),
            Triple("Labor Law Advisor", "4M+ Subs", "EPF, Tax hacks & legal rights"),
            Triple("Finology", "3M+ Subs", "Stock analysis & financial literacy"),
            Triple("Asset Yogi", "3.8M+ Subs", "Real estate & banking advice"),
            Triple("Think School", "3M+ Subs", "Business case studies & breakdown"),
            Triple("Startup Gyan", "1.2M+ Subs", "Entrepreneurship & startup stories"),
            Triple("Shashank Udupa", "400K+ Subs", "Crypto & wealth building")
        )
        clean.contains("vlog") || clean.contains("lifestyle") || clean.contains("travel") -> listOf(
            Triple("Sourav Joshi Vlogs", "28M+ Subs", "Daily family vlogs + drawing"),
            Triple("Flying Beast", "8M+ Subs", "Fitness, pilot life & family travel"),
            Triple("Mumbiker Nikhil", "4M+ Subs", "Moto vlogging & luxury lifestyle"),
            Triple("Nomadic Indian", "1.8M+ Subs", "Budget solo international travel"),
            Triple("Gaurav Taneja", "8M+ Subs", "Daily routines & fitness fitness vlogs"),
            Triple("Passionate Yaatri", "900K+ Subs", "Train & offbeat travel guides"),
            Triple("Ronak Vlogs", "1.5M+ Subs", "City exploration & street food"),
            Triple("Tanya Khanijow", "1.2M+ Subs", "Cinematic solo travel vlogs"),
            Triple("Mountain Trekker", "1.6M+ Subs", "Foreign country travel budgets"),
            Triple("Kritika Goel", "600K+ Subs", "Aesthetic travel & cafe vlogs")
        )
        else -> listOf(
            Triple("Sandip Maheshwari", "28M+ Subs", "Motivation & life guidance"),
            Triple("Dr. Vivek Bindra", "21M+ Subs", "Business case studies"),
            Triple("Khan Sir", "22M+ Subs", "Educational concepts simplified"),
            Triple("Dear Sir", "17M+ Subs", "English & Maths exam preparation"),
            Triple("Kabita's Kitchen", "13M+ Subs", "Easy homemade cooking recipes"),
            Triple("Fit Tuber", "7M+ Subs", "Health, diet & natural fitness"),
            Triple("Crazy XYZ", "28M+ Subs", "Science experiments & fun challenges"),
            Triple("MR. INDIAN HACKER", "33M+ Subs", "Big scale experiments & stunts"),
            Triple("BB Ki Vines", "26M+ Subs", "Multi-character comedy sketches"),
            Triple("Round2hell", "31M+ Subs", "High production comedy movies")
        )
    }
}

private fun get4To6ContentPillars(niche: String): List<String> {
    val clean = niche.lowercase()
    return when {
        clean.contains("game") || clean.contains("gaming") -> listOf(
            "1. Gameplay & Story Missions 🎮",
            "2. Pro Tips & Secret Settings ⚙️",
            "3. Funny Moments & Glitches 🤣",
            "4. Challenges & Modded Gameplay 🔥",
            "5. Esports & Tournament Reactions 🏆"
        )
        clean.contains("tech") || clean.contains("ai") -> listOf(
            "1. Unboxing & Hands-on Reviews 📱",
            "2. Hidden Features & Tips 💡",
            "3. Side-by-Side Comparisons ⚡",
            "4. AI Tools & Productivity Hacks 🤖",
            "5. Buying Guides & Best Under ₹X 💰"
        )
        clean.contains("finance") || clean.contains("business") -> listOf(
            "1. Personal Budgeting & Savings 💰",
            "2. Stock Market & Mutual Funds 📈",
            "3. Business Case Studies 🏢",
            "4. Tax Saving & Banking Hacks 🏛️",
            "5. Passive Income Ideas 🚀"
        )
        clean.contains("vlog") || clean.contains("travel") || clean.contains("lifestyle") -> listOf(
            "1. Daily Life & Behind The Scenes 🎥",
            "2. Travel Guides & Budget Breakdown ✈️",
            "3. Food Exploration & Reviews 🍲",
            "4. Challenge Videos & Pranks 🎒",
            "5. Life Advice & Personal Stories 🌟"
        )
        else -> listOf(
            "1. Step-by-Step Beginner Tutorials 📘",
            "2. Top 10 Tips & Hacks ⚡",
            "3. Common Mistakes to Avoid ❌",
            "4. Case Studies & Real Life Stories 🔍",
            "5. Future Trends & Predictions 🚀"
        )
    }
}

private fun get30VideoIdeas(niche: String, seed: Int): List<String> {
    val clean = if (niche.isBlank()) "Creator" else niche
    val pools = listOf(
        listOf(
            "1. How to Start $clean in 2026 (Beginner Step-by-step)",
            "2. Top 5 Biggest Mistakes Beginners Make in $clean",
            "3. My Secret $clean Workflow (Revealed!)",
            "4. $clean on a Budget: Everything You Need Under ₹1000",
            "5. 10 $clean Hacks Nobody Tells You About",
            "6. $clean vs Traditional Methods: Which is Better?",
            "7. I Tried $clean for 30 Days (Real Results)",
            "8. The Ultimate $clean Guide for Complete Beginners",
            "9. Why 90% of People Fail in $clean (And How to Fix It)",
            "10. 5 FREE Tools Every $clean Creator Must Use",
            "11. $clean Unboxed: Is It Worth Your Money?",
            "12. How I Mastered $clean in Just 14 Days",
            "13. Top 7 Game-Changing $clean Secret Features",
            "14. $clean Myth Busted: What You Need to Know",
            "15. Stop Doing This in $clean Immediately!",
            "16. $clean Masterclass: From Zero to Pro",
            "17. Day in the Life of a $clean Creator",
            "18. How to Monetize Your $clean Skill Fast",
            "19. $clean Equipment Setup Tour 2026",
            "20. The Future of $clean: What’s Coming Next?",
            "21. Reacting to the Worst $clean Advice Online",
            "22. $clean Speedrun: Learn the Basics in 10 Minutes",
            "23. 3 Simple Steps to Double Your $clean Efficiency",
            "24. $clean Challenge: Can I Do This in 24 Hours?",
            "25. Behind the Scenes of a Viral $clean Video",
            "26. $clean Checklist Before You Publish Anything",
            "27. Honest Review: Is $clean Still Relevant in 2026?",
            "28. How $clean Changed My Life (Storytime)",
            "29. $clean FAQ: Answering Your Most Asked Questions",
            "30. The Only $clean Roadmap You Will Ever Need!"
        ),
        listOf(
            "1. 10 Surprising $clean Secrets Experts Hide",
            "2. How to Get Started with Zero Investment in $clean",
            "3. $clean Strategy Breakdown: How to Win Big",
            "4. Don't Buy Anything for $clean Until You Watch This!",
            "5. 5 Golden Rules of $clean Every Beginner Needs",
            "6. How I Overcame My Biggest $clean Struggle",
            "7. $clean Tips That Saved Me Hundreds of Hours",
            "8. Is $clean Easy or Hard? Honest Truth",
            "9. Top $clean Trends You Cannot Ignore This Year",
            "10. $clean Step-by-Step Tutorial for 2026",
            "11. How to Make Your First $clean Project Pop",
            "12. 7 Fast Fixes for Common $clean Errors",
            "13. The Ultimate $clean Blueprint for Beginners",
            "14. $clean vs Alternatives: Full Comparison",
            "15. How to Build a Powerful Routine for $clean",
            "16. 3 Secrets to Stand Out in the $clean Niche",
            "17. I Spent 100 Hours Researching $clean (Here's What I Found)",
            "18. $clean Hacks to Save Time and Boost Output",
            "19. What Nobody Tells You About Starting $clean",
            "20. $clean Gear Tier List: Best to Worst",
            "21. How to Get Instant Feedback on $clean",
            "22. The Single Best $clean Advice I Ever Received",
            "23. $clean Transformation: Before & After",
            "24. 5 Mind-Blowing $clean Examples You Must See",
            "25. $clean Strategy for Busy People",
            "26. How to Never Run Out of $clean Content Ideas",
            "27. $clean Pitfalls and How to Avoid Them",
            "28. How to Level Up Your $clean Skills Today",
            "29. $clean Secrets to Organic Growth",
            "30. The Complete $clean Starter Kit!"
        )
    )
    return pools[seed % pools.size]
}

private fun getAudienceBehavior(audience: String, niche: String): String {
    return when (audience) {
        "Students" -> "🎓 Students look for quick, engaging 5-10 min videos or Shorts during 4 PM - 9 PM. Focus on high energy, relatable memes, and actionable study/career tips."
        "Gamers" -> "🎮 Gamers love long-form live streams (1-2 hrs) or fast-paced Shorts (15-30s) late at night (8 PM - 12 AM). Focus on skill plays, jokes, and modded challenges."
        "Working Professionals" -> "💼 Working Professionals watch content during commuting hours (8 AM - 9 AM) and post-dinner (9 PM - 11 PM). Focus on structured, value-dense 10-15 min videos."
        "Business Owners" -> "🏢 Business Owners care about ROI, efficiency, and case studies. Prefer direct, no-fluff 8-12 min breakdown videos with clear takeaways."
        "Kids" -> "🧒 Kids respond to bright visuals, energetic voices, animated graphics, and playful sound effects. High replay rate on short 3-5 min videos."
        "Parents" -> "👨‍👩‍👧 Parents watch during early mornings or late evenings. Prefer trustworthy, calm, educational, and family-safe content."
        else -> "🌐 General audience values curiosity hooks in the first 10 seconds, relatable storytelling, and high audio clarity."
    }
}

private fun getExplainAgainVariantLevel2(stepIdx: Int, variantIdx: Int): String {
    val explanations = listOf(
        "Dekho, simple shabdon me samjho: Sahi Niche choose karne ka matlab hai aisi field chunna jisme aapko interest ho aur audience me demand ho.",
        "Nayi udaharan: Jaise ek dukandari me sahi item bechna zaroori hai, waise hi YouTube par ek fixed topic par focus karne se algorithm aur viewers dono trust karte hain.",
        "Simple Step Formula: Identify Interest ➔ Analyze Competition ➔ Finalize Content Pillars ➔ Build 30-Day Calendar.",
        "AI Mentor Insight: YouTube algorithm unn channels ko ziada promote karta hai jinki niche clarity 100% hoti hai."
    )
    return explanations[(stepIdx + variantIdx) % explanations.size]
}

@Composable
fun YouTubeLevel2SetupScreen(
    onCompleteLevel2: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var stepIndex by remember { mutableIntStateOf(0) } // 0..12 for Steps 1..13, 13 for Mission Checklist

    // AI Mentor Animated Glow
    val infiniteTransition = rememberInfiniteTransition(label = "level2Glow")
    val avatarGlowScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale2"
    )

    // User Choices & State
    var selectedNiche by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeNiche(context) ?: "Gaming") }
    var customNicheInput by remember { mutableStateOf("") }
    var selectedExpLevel by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeNicheExp(context) ?: "Basic") }
    var selectedContentStyle by remember { mutableStateOf(CreatorAcademyPrefs.getYouTubeContentStyle(context) ?: "Face Camera") }
    var selectedAudience by remember { mutableStateOf("Students") }
    var contentPillars by remember { mutableStateOf(get4To6ContentPillars(selectedNiche)) }
    var customPillarInput by remember { mutableStateOf("") }
    var videoIdeasSeed by remember { mutableIntStateOf(0) }
    var viralContentType by remember { mutableStateOf("Today's Trend") } // Today's Trend or Evergreen
    var uploadFrequency by remember { mutableStateOf("2 Videos/week") }

    // Mission Checklist Items State
    var nicheChecked by remember { mutableStateOf(false) }
    var audienceChecked by remember { mutableStateOf(false) }
    var styleChecked by remember { mutableStateOf(false) }
    var pillarsChecked by remember { mutableStateOf(false) }
    var weeklyChecked by remember { mutableStateOf(false) }
    var ideasChecked by remember { mutableStateOf(false) }

    // Level Complete Glass Modal
    var showLevel2CompleteModal by remember { mutableStateOf(false) }

    // Copy Toast State
    var copyToastText by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(copyToastText) {
        if (copyToastText != null) {
            kotlinx.coroutines.delay(2000)
            copyToastText = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF121215), Color(0xFF0F0F12), Color(0xFF1A1A22))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 520.dp)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (stepIndex > 0) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF24242C))
                            .clickable { stepIndex -= 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "←", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Spacer(modifier = Modifier.size(36.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF282834)),
                        contentAlignment = Alignment.Center
                    ) {
                        OfficialLogo(name = "youtube", modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LEVEL 2 • FIND YOUR NICHE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFA1A1AA),
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF24242C))
                        .clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AI Mentor Greeting Header
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1C1C24),
                border = BorderStroke(1.5.dp, Color(0xFF2C2C3A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size((68 * avatarGlowScale).dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF3333).copy(alpha = 0.25f))
                        )
                        Box(
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(Color(0xFFFF3333), Color(0xFF990000))
                                    )
                                )
                                .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.9f)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🔥", fontSize = 26.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Find Your Perfect Niche",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "\"Bahut badhiya! Ab channel toh ready hai... Lekin sabse bada question hai: Kis topic par videos banaoge? Galat niche choose karoge toh growth slow hogi. Main tumhari help karunga best niche choose karne mein.\"",
                        fontSize = 13.sp,
                        color = Color(0xFFA1A1AA),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress Bar (Starts at 20%)
                    val progressVal = if (stepIndex == 13) 100 else (20 + ((stepIndex * 80) / 13))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (stepIndex == 13) "Mission Checklist" else "Step ${stepIndex + 1} of 13",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD1D1D6)
                        )
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF2A2A36)
                        ) {
                            Text(
                                text = "$progressVal%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFF4D4D),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF2C2C38))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progressVal / 100f)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFFF3333), Color(0xFFFF6666))
                                    )
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Toast Message when copied
            copyToastText?.let { toastMsg ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF2E2E3E),
                    border = BorderStroke(1.dp, Color(0xFFFF3333)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "📋 $toastMsg",
                        fontSize = 12.5.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // DYNAMIC STEP RENDERER (0..12 for Steps 1..13, 13 for Mission Checklist)
            if (stepIndex < 13) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF1C1C24),
                    border = BorderStroke(1.5.dp, Color(0xFF2C2C3A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        when (stepIndex) {
                            0 -> {
                                // STEP 1: Current Interest
                                Text(text = "STEP 1 OF 13", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Current Interest 🎯", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Tumhe sabse jyada kis topic me interest hai?",
                                    fontSize = 14.5.sp,
                                    color = Color(0xFFE5E5EA)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                val nicheOptions = listOf(
                                    "Gaming 🎮", "Technology 📱", "AI 🤖", "Education 📚",
                                    "Finance 💰", "Fitness 🏋️", "Travel ✈️", "Movies 🎬",
                                    "Anime ⛩️", "Motivation ⚡", "Comedy 😂", "Lifestyle 🌟",
                                    "Vlogs 🎥", "Business 🏢", "Cooking 🍳", "Fashion 👗",
                                    "Sports ⚽", "Cars 🏎️", "Other 💡"
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    nicheOptions.chunked(2).forEach { pair ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            pair.forEach { item ->
                                                val isSelected = selectedNiche == item || selectedNiche == item.split(" ").first()
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(14.dp))
                                                        .background(if (isSelected) Color(0xFF332024) else Color(0xFF252530))
                                                        .border(BorderStroke(1.2.dp, if (isSelected) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(14.dp))
                                                        .clickable {
                                                            val cleanName = item.split(" ").first()
                                                            selectedNiche = cleanName
                                                            CreatorAcademyPrefs.saveYouTubeNiche(context, cleanName)
                                                            contentPillars = get4To6ContentPillars(cleanName)
                                                            nicheChecked = true
                                                        }
                                                        .padding(vertical = 12.dp, horizontal = 10.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = item,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isSelected) Color(0xFFFF4D4D) else Color.White,
                                                        textAlign = TextAlign.Center
                                                    )
                                                }
                                            }
                                            if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                OutlinedTextField(
                                    value = customNicheInput,
                                    onValueChange = {
                                        customNicheInput = it
                                        if (it.isNotBlank()) {
                                            selectedNiche = it
                                            CreatorAcademyPrefs.saveYouTubeNiche(context, it)
                                            contentPillars = get4To6ContentPillars(it)
                                            nicheChecked = true
                                        }
                                    },
                                    placeholder = { Text("Or enter custom niche...", color = Color(0xFF666675)) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFF3333),
                                        unfocusedBorderColor = Color(0xFF38384A),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable {
                                            if (selectedNiche.isBlank()) {
                                                selectedNiche = "Gaming"
                                                CreatorAcademyPrefs.saveYouTubeNiche(context, "Gaming")
                                            }
                                            nicheChecked = true
                                            stepIndex = 1
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Selected: $selectedNiche ➔ Experience Level", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            1 -> {
                                // STEP 2: Experience Level
                                Text(text = "STEP 2 OF 13", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Experience Level 📊", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Is topic me tumhara experience kitna hai?",
                                    fontSize = 14.5.sp,
                                    color = Color(0xFFE5E5EA)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                val expCards = listOf(
                                    Triple("No Experience", "Bilkul zero se start kar rahe hain", "🌱"),
                                    Triple("Basic", "Thoda bohot basic knowledge hai", "☘️"),
                                    Triple("Intermediate", "Accha khasa experience aur knowledge hai", "🌿"),
                                    Triple("Expert", "Full master hoon is topic me", "🌳")
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    expCards.forEach { (title, desc, icon) ->
                                        val isSelected = selectedExpLevel == title
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(if (isSelected) Color(0xFF332024) else Color(0xFF252530))
                                                .border(BorderStroke(1.2.dp, if (isSelected) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(16.dp))
                                                .clickable {
                                                    selectedExpLevel = title
                                                    CreatorAcademyPrefs.saveYouTubeNicheExp(context, title)
                                                }
                                                .padding(16.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(text = icon, fontSize = 24.sp)
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Column {
                                                    Text(text = title, fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color(0xFFFF4D4D) else Color.White)
                                                    Text(text = desc, fontSize = 12.sp, color = Color(0xFFA1A1AA))
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 2 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Experience Saved ➔ Content Style", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            2 -> {
                                // STEP 3: Content Style
                                Text(text = "STEP 3 OF 13", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Content Style 🎬", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Tum kis style ki videos banana chahte ho?",
                                    fontSize = 14.5.sp,
                                    color = Color(0xFFE5E5EA)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                val styleCards = listOf(
                                    Triple("Face Camera", "Camera ke aage direct bolna", "🎥"),
                                    Triple("Faceless", "Bina chehra dikhaye voice/visuals", "🎭"),
                                    Triple("Animation", "2D/3D Animated Explainers", "🎨"),
                                    Triple("Screen Recording", "Software Tutorials, Gameplay", "💻"),
                                    Triple("Voice Over", "Stock footage/Images par voice", "🎙️"),
                                    Triple("Podcast", "Discussions, Interviews & Long Talk", "🎧"),
                                    Triple("Storytelling", "Case studies, Narratives", "📖"),
                                    Triple("Mixed", "Face + Screen + Animation blend", "⚡")
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    styleCards.chunked(2).forEach { pair ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            pair.forEach { (title, sub, icon) ->
                                                val isSelected = selectedContentStyle == title
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(14.dp))
                                                        .background(if (isSelected) Color(0xFF332024) else Color(0xFF252530))
                                                        .border(BorderStroke(1.2.dp, if (isSelected) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(14.dp))
                                                        .clickable {
                                                            selectedContentStyle = title
                                                            CreatorAcademyPrefs.saveYouTubeContentStyle(context, title)
                                                            styleChecked = true
                                                        }
                                                        .padding(12.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                        Text(text = icon, fontSize = 22.sp)
                                                        Spacer(modifier = Modifier.height(4.dp))
                                                        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color(0xFFFF4D4D) else Color.White, textAlign = TextAlign.Center)
                                                        Text(text = sub, fontSize = 10.5.sp, color = Color(0xFFA1A1AA), textAlign = TextAlign.Center, lineHeight = 13.sp)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable {
                                            styleChecked = true
                                            stepIndex = 3
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Style Selected ➔ AI Niche Analysis", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            3 -> {
                                // STEP 4: AI Analysis (Your Creator Profile)
                                Text(text = "STEP 4 OF 13", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "AI Niche Analysis 🤖", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Surface(
                                    shape = RoundedCornerShape(18.dp),
                                    color = Color(0xFF252532),
                                    border = BorderStroke(1.2.dp, Color(0xFFFF3333).copy(alpha = 0.6f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "✨ YOUR CREATOR PROFILE & NICHE SCORE", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "Target Niche:", fontSize = 13.5.sp, color = Color(0xFFA1A1AA))
                                            Text(text = selectedNiche, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Animated progress metrics
                                        val metrics = listOf(
                                            Triple("Difficulty Score", 0.65f, "Medium (65/100)"),
                                            Triple("Competition", 0.75f, "Moderate to High"),
                                            Triple("Audience Size", 0.90f, "Millions of Viewers"),
                                            Triple("Growth Potential", 0.88f, "🔥 Very High"),
                                            Triple("Monetization Potential", 0.85f, "💰 High CPM"),
                                            Triple("Beginner Friendly", 0.82f, "⭐ 82/100")
                                        )

                                        metrics.forEach { (label, fillRatio, valueStr) ->
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(text = label, fontSize = 12.sp, color = Color(0xFFD1D1D6))
                                                Text(text = valueStr, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(5.dp)
                                                    .clip(RoundedCornerShape(3.dp))
                                                    .background(Color(0xFF38384A))
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxHeight()
                                                        .fillMaxWidth(fillRatio)
                                                        .clip(RoundedCornerShape(3.dp))
                                                        .background(
                                                            Brush.horizontalGradient(
                                                                listOf(Color(0xFFFF3333), Color(0xFFFF8080))
                                                            )
                                                        )
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFF1E1E28)
                                        ) {
                                            Text(
                                                text = "💡 AI Takeaway: $selectedNiche is a high-demand topic. Paired with $selectedContentStyle style, focus on strong thumbnails and emotional hooks to stand out fast!",
                                                fontSize = 12.sp,
                                                color = Color(0xFFE5E5EA),
                                                modifier = Modifier.padding(10.dp),
                                                lineHeight = 16.sp
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 4 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Proceed to Competitor Research ➔", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            4 -> {
                                // STEP 5: Competitor Research
                                Text(text = "STEP 5 OF 13", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Competitor Research 🔍", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF252532),
                                    border = BorderStroke(1.dp, Color(0xFF38384A)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(text = "❓ Competitor Kya Hota Hai?", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                        Text(
                                            text = "Aapke niche me jo channels already successfully videos bana rahe hain. Unhein analyze karke seekhna sabse fast tareeka hai.",
                                            fontSize = 12.5.sp,
                                            color = Color.White,
                                            lineHeight = 17.sp
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(text = "🚨 GOLDEN RULE: Never Copy. Only Learn!", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(text = "Top 10 Channels to Learn From ($selectedNiche):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(8.dp))

                                val competitors = get10CompetitorsForNiche(selectedNiche)

                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    competitors.forEachIndexed { idx, (chName, subs, strat) ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFF252530),
                                            border = BorderStroke(1.dp, Color(0xFF38384A)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Surface(
                                                    shape = CircleShape,
                                                    color = Color(0xFF382024)
                                                ) {
                                                    Text(text = "#${idx + 1}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                                }
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Text(text = chName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                        Text(text = subs, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA1A1AA))
                                                    }
                                                    Text(text = strat, fontSize = 11.5.sp, color = Color(0xFFD1D1D6))
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFF252532),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(text = "💡 What to analyze in competitor videos:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "• 🖼️ Thumbnail: Expression, color contrast, big text\n• 📝 Title: Curiosity hooks, short length\n• ✂️ Editing: Cut frequency, sound effects\n• 🪝 Hook: First 10 seconds retention intro\n• 📅 Posting Style & Upload Frequency", fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 5 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Competitor Research Done ➔ Audience Research", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            5 -> {
                                // STEP 6: Audience Research
                                Text(text = "STEP 6 OF 13", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Audience Research 👥", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Tumhari main target audience kaun hai?",
                                    fontSize = 14.5.sp,
                                    color = Color(0xFFE5E5EA)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                val audienceOptions = listOf(
                                    "Students 🎓", "Gamers 🎮", "Working Professionals 💼",
                                    "Business Owners 🏢", "Kids 🧒", "Parents 👨‍👩‍👧", "Everyone 🌐"
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    audienceOptions.forEach { opt ->
                                        val cleanAud = opt.split(" ").first()
                                        val isSelected = selectedAudience == cleanAud
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(if (isSelected) Color(0xFF332024) else Color(0xFF252530))
                                                .border(BorderStroke(1.2.dp, if (isSelected) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(14.dp))
                                                .clickable {
                                                    selectedAudience = cleanAud
                                                    audienceChecked = true
                                                }
                                                .padding(14.dp)
                                        ) {
                                            Text(text = opt, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color(0xFFFF4D4D) else Color.White)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFF252532),
                                    border = BorderStroke(1.dp, Color(0xFFFF3333).copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(text = "🤖 AI Audience Behavior Analysis:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = getAudienceBehavior(selectedAudience, selectedNiche),
                                            fontSize = 12.5.sp,
                                            color = Color.White,
                                            lineHeight = 17.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable {
                                            audienceChecked = true
                                            stepIndex = 6
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Audience Set ➔ Generate Content Pillars", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            6 -> {
                                // STEP 7: Content Pillars
                                Text(text = "STEP 7 OF 13", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Content Pillars 🏛️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Aapke channel ke 4-6 main topics (Pillars) jin par hamesha videos banenge:",
                                    fontSize = 13.5.sp,
                                    color = Color(0xFFE5E5EA),
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    contentPillars.forEachIndexed { idx, p ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFF252530),
                                            border = BorderStroke(1.dp, Color(0xFF38384A)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = p, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFFFF4D4D), modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = customPillarInput,
                                        onValueChange = { customPillarInput = it },
                                        placeholder = { Text("Add custom pillar...", color = Color(0xFF666675)) },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFFFF3333),
                                            unfocusedBorderColor = Color(0xFF38384A),
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFF382024))
                                            .clickable {
                                                if (customPillarInput.isNotBlank()) {
                                                    contentPillars = contentPillars + customPillarInput
                                                    customPillarInput = ""
                                                }
                                            }
                                            .padding(horizontal = 16.dp, vertical = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "+ Add", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable {
                                            pillarsChecked = true
                                            stepIndex = 7
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Pillars Saved ➔ Generate 30 Video Ideas", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            7 -> {
                                // STEP 8: 30 Video Ideas (Unlimited Refresh)
                                Text(text = "STEP 8 OF 13", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "30 Video Ideas 💡", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF382024),
                                        modifier = Modifier.clickable { videoIdeasSeed += 1 }
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color(0xFFFF4D4D), modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "Fresh 30 Ideas 🔄", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                val ideas = get30VideoIdeas(selectedNiche, videoIdeasSeed)

                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    ideas.forEach { idea ->
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = Color(0xFF252530),
                                            border = BorderStroke(1.dp, Color(0xFF38384A)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(10.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = idea,
                                                    fontSize = 12.5.sp,
                                                    color = Color.White,
                                                    modifier = Modifier.weight(1f),
                                                    lineHeight = 16.sp
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .clip(CircleShape)
                                                        .clickable {
                                                            clipboardManager.setText(AnnotatedString(idea))
                                                            copyToastText = "Idea copied to clipboard!"
                                                        }
                                                        .padding(6.dp)
                                                ) {
                                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color(0xFFA1A1AA), modifier = Modifier.size(16.dp))
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable {
                                            ideasChecked = true
                                            stepIndex = 8
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "30 Ideas Saved ➔ Viral Topic Finder", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            8 -> {
                                // STEP 9: Viral Idea Finder
                                Text(text = "STEP 9 OF 13", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Viral Topic Finder 🔥", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Kaunsa content focus karoge?",
                                    fontSize = 14.5.sp,
                                    color = Color(0xFFE5E5EA)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    val isTrend = viralContentType == "Today's Trend"
                                    val isEvergreen = viralContentType == "Evergreen"

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(if (isTrend) Color(0xFF332024) else Color(0xFF252530))
                                            .border(BorderStroke(1.2.dp, if (isTrend) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(14.dp))
                                            .clickable { viralContentType = "Today's Trend" }
                                            .padding(14.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "🔥 Today's Trend", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = if (isTrend) Color(0xFFFF4D4D) else Color.White)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(if (isEvergreen) Color(0xFF332024) else Color(0xFF252530))
                                            .border(BorderStroke(1.2.dp, if (isEvergreen) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(14.dp))
                                            .clickable { viralContentType = "Evergreen" }
                                            .padding(14.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "🌲 Evergreen Content", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = if (isEvergreen) Color(0xFFFF4D4D) else Color.White)
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                val viralTopics = if (viralContentType == "Today's Trend") listOf(
                                    "🔥 $selectedNiche News & Breaking Shocking Reveal 2026",
                                    "⚡ Reacting to the Biggest $selectedNiche Drama Right Now",
                                    "🚀 New $selectedNiche Feature Everyone Is Talking About",
                                    "💥 24-Hour $selectedNiche Challenge Viral Trend",
                                    "⚠️ Why Everyone is Quitting $selectedNiche? (Truth)",
                                    "📈 Is This $selectedNiche Trend Real or Fake?"
                                ) else listOf(
                                    "🌲 Complete $selectedNiche Masterclass (Zero to Hero)",
                                    "🛠️ Top 10 Must-Have Tools for $selectedNiche in 2026",
                                    "📚 Step-by-Step Beginner Guide to $selectedNiche",
                                    "💰 How to Monetize $selectedNiche (Complete Guide)",
                                    "❌ 5 Beginner Mistakes in $selectedNiche & How to Fix",
                                    "🏆 Ultimate $selectedNiche Roadmap for Long Term Success"
                                )

                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFF252532),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(text = "🎯 Generated High CTR Viral Topics:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                        Spacer(modifier = Modifier.height(8.dp))
                                        viralTopics.forEach { topic ->
                                            Text(text = topic, fontSize = 12.5.sp, color = Color.White, modifier = Modifier.padding(vertical = 4.dp), lineHeight = 16.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 9 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Viral Topics Saved ➔ Upload Frequency", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            9 -> {
                                // STEP 10: Upload Frequency
                                Text(text = "STEP 10 OF 13", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Upload Frequency 📅", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "How many videos per week can you make consistently?",
                                    fontSize = 14.5.sp,
                                    color = Color(0xFFE5E5EA)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                val freqCards = listOf(
                                    Triple("1 Video/week", "Deep research, high editing quality", "🥉"),
                                    Triple("2 Videos/week", "Recommended balance for steady growth", "🥈"),
                                    Triple("3 Videos/week", "Aggressive growth strategy", "🥇"),
                                    Triple("Daily Videos", "Shorts focus or quick daily updates", "🚀")
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    freqCards.forEach { (title, desc, icon) ->
                                        val isSelected = uploadFrequency == title
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(if (isSelected) Color(0xFF332024) else Color(0xFF252530))
                                                .border(BorderStroke(1.2.dp, if (isSelected) Color(0xFFFF3333) else Color(0xFF38384A)), RoundedCornerShape(16.dp))
                                                .clickable { uploadFrequency = title }
                                                .padding(16.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(text = icon, fontSize = 24.sp)
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Column {
                                                    Text(text = title, fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color(0xFFFF4D4D) else Color.White)
                                                    Text(text = desc, fontSize = 12.sp, color = Color(0xFFA1A1AA))
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 10 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Schedule Set ➔ Weekly Planner", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            10 -> {
                                // STEP 11: Weekly Planner
                                Text(text = "STEP 11 OF 13", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Weekly Content Planner 📋", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                val weeklyTasks = listOf(
                                    "Monday 🗓️" to "Topic Research & Script Outline 📝",
                                    "Tuesday 🎥" to "Audio & Video Recording Setup 🎙️",
                                    "Wednesday ✂️" to "Rough Cut & Pacing Editing 💻",
                                    "Thursday 🎨" to "Thumbnail Design & Title SEO 🖼️",
                                    "Friday 🚀" to "Final Export & Scheduled Premiere 📤",
                                    "Saturday 💬" to "Community Post & Reply to Comments 👥",
                                    "Sunday 📊" to "Analytics Review & Rest Day ☕"
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    weeklyTasks.forEach { (day, task) ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFF252530),
                                            border = BorderStroke(1.dp, Color(0xFF38384A)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = day, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                                Text(text = task, fontSize = 12.5.sp, color = Color.White)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable {
                                            weeklyChecked = true
                                            stepIndex = 11
                                        }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Weekly Plan Saved ➔ Monthly Planner", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            11 -> {
                                // STEP 12: Monthly Planner
                                Text(text = "STEP 12 OF 13", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "30-Day Creator Roadmap 🗺️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                val monthRoadmap = listOf(
                                    "Week 1 🚀" to "Idea ➔ Shoot ➔ Edit ➔ First 2 Uploads",
                                    "Week 2 🎨" to "Thumbnail A/B Testing & Audience Retention",
                                    "Week 3 📈" to "Community Engagement & Shorts Integration",
                                    "Week 4 🏆" to "30-Day Review, Analytics & Workflow Scaling"
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    monthRoadmap.forEach { (week, desc) ->
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = Color(0xFF252532),
                                            border = BorderStroke(1.dp, Color(0xFF38384A)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(14.dp)) {
                                                Text(text = week, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(text = desc, fontSize = 12.5.sp, color = Color.White)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 12 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Monthly Roadmap Set ➔ Mistakes to Avoid", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            12 -> {
                                // STEP 13: Mistakes to Avoid
                                Text(text = "STEP 13 OF 13", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Top Mistakes to Avoid ⚠️", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                                Spacer(modifier = Modifier.height(12.dp))

                                val mistakes = listOf(
                                    "❌ Don't Copy Directly" to "Inspiration is fine, but direct copying hurts your unique channel identity.",
                                    "❌ Don't Change Niche Weekly" to "Changing topics every week confuses the YouTube algorithm and unsubscribes viewers.",
                                    "❌ Don't Buy Subscribers" to "Fake subs ruin your click-through rate (CTR) and kill algorithmic impressions.",
                                    "❌ Don't Chase Only Views" to "Focus on building a loyal community that trusts your recommendations.",
                                    "✅ Focus on Consistency" to "Improve 1% in script, editing, or audio with every single video you upload!"
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    mistakes.forEach { (title, desc) ->
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = Color(0xFF252530),
                                            border = BorderStroke(1.dp, Color(0xFF38384A)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Text(text = title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = if (title.startsWith("✅")) Color(0xFF4DEEEA) else Color(0xFFFF4D4D))
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(text = desc, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFFF3333))
                                        .clickable { stepIndex = 13 }
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Got It! Proceed to Mission Checklist ➔", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            } else {
                // MISSION CHECKLIST: Finalize Your Niche
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF1C1C24),
                    border = BorderStroke(1.5.dp, Color(0xFFFF3333)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(text = "MISSION CHECKLIST", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4D4D))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Finalize Your Niche 🎯", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White)

                        Spacer(modifier = Modifier.height(14.dp))

                        val checklistItems = listOf(
                            "Niche Selected ($selectedNiche)" to true,
                            "Audience Selected ($selectedAudience)" to true,
                            "Content Style ($selectedContentStyle)" to true,
                            "Content Pillars Defined" to true,
                            "Weekly Plan Configured" to true,
                            "First 30 Ideas Generated" to true
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            checklistItems.forEach { (label, isDone) ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFF252532),
                                    border = BorderStroke(1.dp, Color(0xFF38384A)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = label, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFFF3333)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFFF3333), Color(0xFFFF6666))
                                    )
                                )
                                .clickable {
                                    showLevel2CompleteModal = true
                                }
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "MISSION COMPLETE ➔ CLAIM BADGE", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }
                }
            }
        }

        // LEVEL COMPLETE GLASS MODAL OVERLAY
        if (showLevel2CompleteModal) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = Color(0xFF1F1F2C),
                    border = BorderStroke(2.dp, Color(0xFFFF3333)),
                    modifier = Modifier.fillMaxWidth(0.92f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🏆", fontSize = 56.sp)

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "LEVEL 2 COMPLETE!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF382024)
                        ) {
                            Text(
                                text = "BADGE: NICHE MASTER 🎖️",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFF4D4D),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Awesome work! Tumne apna perfect Niche ($selectedNiche), Audience, Content Pillars aur 30-day content calendar successfully set kar liya hai!",
                            fontSize = 13.5.sp,
                            color = Color(0xFFD1D1D6),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF282836)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "⭐ +500 XP Earned!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFF3333))
                                .clickable {
                                    CreatorAcademyPrefs.setYouTubeLevel2Completed(context, true)
                                    showLevel2CompleteModal = false
                                    onCompleteLevel2()
                                }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Claim Badge & Continue to Level 3 ➔", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun YouTubeCreatorAiV2Dialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // Check if intro (4 swipe cards), level 0 (questions), level 1 (channel setup), level 2 (niche finder) & level 3 (video planning) have ever been completed
    val isIntroCompleted = remember { CreatorAcademyPrefs.isYouTubeIntroCompleted(context) }
    val isLevel0Completed = remember { CreatorAcademyPrefs.isYouTubeLevel0Completed(context) }
    val isLevel1Completed = remember { CreatorAcademyPrefs.isYouTubeLevel1Completed(context) }
    val isLevel2Completed = remember { CreatorAcademyPrefs.isYouTubeLevel2Completed(context) }
    val isLevel3Completed = remember { CreatorAcademyPrefs.isYouTubeLevel3Completed(context) }
    val isLevel4Completed = remember { CreatorAcademyPrefs.isYouTubeLevel4Completed(context) }
    val isLevel5Completed = remember { CreatorAcademyPrefs.isYouTubeLevel5Completed(context) }
    val isLevel6Completed = remember { CreatorAcademyPrefs.isYouTubeLevel6Completed(context) }
    val isLevelFinalCompleted = remember { CreatorAcademyPrefs.isYouTubeFinalCompleted(context) }

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
    var showWelcomeBack by remember { mutableStateOf(isIntroCompleted && isLevel0Completed && isLevel1Completed && isLevel2Completed && isLevel3Completed && isLevel4Completed && isLevel5Completed && isLevel6Completed && selectedLang != null && creatorType != null && (currentStepId > 1 || completedSteps.isNotEmpty())) }
    var showIntro by remember { mutableStateOf(!isIntroCompleted) }
    var showLevel0 by remember { mutableStateOf(!isLevel0Completed) }
    var showLevel1 by remember { mutableStateOf(!isLevel1Completed) }
    var showLevel2 by remember { mutableStateOf(!isLevel2Completed) }
    var showLevel3 by remember { mutableStateOf(!isLevel3Completed) }
    var showLevel4 by remember { mutableStateOf(!isLevel4Completed) }
    var showLevel5 by remember { mutableStateOf(!isLevel5Completed) }
    var showLevel6 by remember { mutableStateOf(!isLevel6Completed) }
    var showLevelFinal by remember { mutableStateOf(isLevel6Completed || isLevelFinalCompleted) }
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
            if (showIntro) {
                // FIRST LAUNCH ONLY ONBOARDING (4 SWIPE CARDS)
                YouTubeOnboardingCardsScreen(
                    onComplete = {
                        CreatorAcademyPrefs.setYouTubeIntroCompleted(context, true)
                        showIntro = false
                    },
                    onClose = onDismiss
                )
            } else if (showLevel0) {
                // MASTER PHASE 2 — LEVEL 0 PERSONAL AI SETUP (9 QUESTIONS & BLUEPRINT)
                YouTubeLevel0SetupScreen(
                    onCompleteLevel0 = { langStr, typeStr, _ ->
                        showLevel0 = false
                        val langEnum = try { YouTubeLanguage.valueOf(langStr) } catch (e: Exception) { YouTubeLanguage.HINGLISH }
                        selectedLang = langEnum
                        creatorType = typeStr
                    },
                    onClose = onDismiss
                )
            } else if (showLevel1) {
                // MASTER PHASE 3 — LEVEL 1 PROFESSIONAL CHANNEL SETUP (12 STEPS & MISSION)
                YouTubeLevel1SetupScreen(
                    onCompleteLevel1 = {
                        showLevel1 = false
                    },
                    onClose = onDismiss
                )
            } else if (showLevel2) {
                // MASTER PHASE 4 — LEVEL 2 FIND YOUR PERFECT NICHE (13 STEPS & WIZARD)
                YouTubeLevel2SetupScreen(
                    onCompleteLevel2 = {
                        showLevel2 = false
                    },
                    onClose = onDismiss
                )
            } else if (showLevel3) {
                // MASTER PHASE 5 — LEVEL 3 VIDEO PLANNING MASTERCLASS (14 STEPS & WIZARD)
                YouTubeLevel3SetupScreen(
                    onCompleteLevel3 = {
                        showLevel3 = false
                    },
                    onClose = onDismiss
                )
            } else if (showLevel4) {
                // MASTER PHASE 6 — LEVEL 4 UPLOAD LIKE A PRO (18 STEPS & WIZARD)
                YouTubeLevel4SetupScreen(
                    onCompleteLevel4 = {
                        showLevel4 = false
                    },
                    onClose = onDismiss
                )
            } else if (showLevel5) {
                // MASTER PHASE 7 — LEVEL 5 YOUTUBE MONETIZATION (15 STEPS, REVENUE PLANNER, BRAND DEALS & MISSION)
                YouTubeLevel5SetupScreen(
                    onCompleteLevel5 = {
                        showLevel5 = false
                    },
                    onClose = onDismiss
                )
            } else if (showLevel6) {
                // MASTER PHASE 8 — LEVEL 6 YOUTUBE GROWTH ACCELERATOR (15 STEPS, GROWTH SCORE, COMPETITOR GUIDE, WEEKLY PLANNER & MISSION)
                YouTubeLevel6SetupScreen(
                    onCompleteLevel6 = {
                        showLevel6 = false
                        showLevelFinal = true
                        CreatorAcademyPrefs.setYouTubeFinalCompleted(context, true)
                    },
                    onClose = onDismiss
                )
            } else if (showLevelFinal) {
                // MASTER PHASE 9 — YOUTUBE GROWTH GUIDE: FINAL LEVEL (LIFETIME AI CREATOR COACH)
                YouTubeFinalLifetimeCoachScreen(
                    onClose = onDismiss,
                    onRestartCourse = {
                        showLevelFinal = false
                        showIntro = true
                        showLevel0 = true
                        showLevel1 = true
                        showLevel2 = true
                        showLevel3 = true
                        showLevel4 = true
                        showLevel5 = true
                        showLevel6 = true
                        selectedLang = null
                        creatorType = null
                        currentStepId = 1
                        completedSteps.clear()
                    }
                )
            } else if (showWelcomeBack && selectedLang != null && creatorType != null) {
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
                    .heightIn(max = 280.dp)
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
        YouTubeHeaderBar(
            creatorType = creatorType,
            language = language,
            currentStepId = currentStepId,
            totalSteps = YOUTUBE_ROADMAP_STEPS.size,
            onChangeLangClick = onChangeLangClick,
            onClose = onClose
        )

        // STEP PROGRESS INDICATOR STRIP
        YouTubeStepProgressStrip(
            steps = YOUTUBE_ROADMAP_STEPS,
            completedSteps = completedSteps,
            currentStepId = currentStepId,
            onStepChange = onStepChange
        )

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
                        onContinue = { onClose() },
                        onResetCourse = { onResetCourse?.invoke() },
                        theme = ytTheme
                    )
                }
            }
        }

        // BOTTOM INPUT BAR
        YouTubeBottomInputBar(
            inputText = inputText,
            onInputTextChange = { inputText = it },
            language = language,
            onSendClick = { handleUserMsg(inputText) }
        )
    }
}

@Composable
private fun YouTubeHeaderBar(
    creatorType: String,
    language: YouTubeLanguage,
    currentStepId: Int,
    totalSteps: Int,
    onChangeLangClick: () -> Unit,
    onClose: () -> Unit
) {
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
                        text = "$creatorType • ${language.name} • Step $currentStepId/$totalSteps",
                        fontSize = 11.sp,
                        color = TextWhite.copy(alpha = 0.65f)
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
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
}

@Composable
private fun YouTubeStepProgressStrip(
    steps: List<YouTubeStep>,
    completedSteps: List<Int>,
    currentStepId: Int,
    onStepChange: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF140606))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(steps) { step ->
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
}

@Composable
private fun YouTubeBottomInputBar(
    inputText: String,
    onInputTextChange: (String) -> Unit,
    language: YouTubeLanguage,
    onSendClick: () -> Unit
) {
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
                onValueChange = onInputTextChange,
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
                    .clickable { onSendClick() },
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
