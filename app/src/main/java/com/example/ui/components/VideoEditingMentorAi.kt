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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import com.example.ui.theme.responsiveImeAndNavPadding
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Camera
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
 * PHASE — VIDEO EDITING ACADEMY V2 (PERSONAL AI MENTOR SYSTEM)
 *
 * Dedicated, isolated Personal AI Mentors for:
 * 1. CapCut Master
 * 2. VN Video Editor
 * 3. Instagram Edits
 */

enum class EditingToolType(val key: String, val title: String, val color: Color, val logoType: String) {
    CAPCUT("capcut", "CapCut Master AI Mentor", Color(0xFF00E5FF), "capcut"),
    VN("vn", "VN Video Editor AI Mentor", Color(0xFF0288D1), "vn"),
    INSTAGRAM_EDITS("instagram_edits", "Instagram Edits AI Mentor", Color(0xFFCFD8DC), "instagram")
}

enum class EditingLanguage { HINDI, ENGLISH, HINGLISH }

data class EditingStep(
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

data class EditingChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val isFromUser: Boolean,
    val text: String,
    val timestamp: String = "Just now",
    val quickReplies: List<String> = emptyList()
)

val VIDEO_TYPES = listOf(
    "Reels / Shorts" to "⚡",
    "Vlog / Daily Life" to "🎥",
    "Gaming Montage" to "🎮",
    "Tech & Product Review" to "💻",
    "Educational & Tips" to "📚",
    "Comedy & Memes" to "😂",
    "Podcast & Talking Head" to "🎙️"
)

// Generates tool-specific roadmap steps
fun getEditingRoadmapSteps(tool: EditingToolType): List<EditingStep> {
    val toolName = when(tool) {
        EditingToolType.CAPCUT -> "CapCut"
        EditingToolType.VN -> "VN Editor"
        EditingToolType.INSTAGRAM_EDITS -> "Instagram Edits"
    }

    return listOf(
        EditingStep(
            id = 1,
            title = "App Install & Account Setup",
            desc = "Download official $toolName and setup free account.",
            initialMessageHindi = "👋 Welcome! Main aapka Personal $toolName Mentor hoon.\n\nBilkul Zero se start karenge! Sabse pehle $toolName app install kijiye.\n\nKey Action:\n1. Play Store se $toolName install karo.\n2. Guest ya Google account se log in karo.\n\nComplete hone par 'Done' ya 'Ho Gaya' par tap karo!",
            initialMessageEnglish = "👋 Welcome! I am your personal $toolName Mentor.\n\nWe start from complete zero! First, install $toolName app.\n\nKey Action:\n1. Download $toolName from Play Store.\n2. Open app & sign in.\n\nTap 'Done' when ready!",
            initialMessageHinglish = "👋 Hello Creator! Main aapka $toolName AI Mentor hoon.\n\nZero Knowledge Mode ON hai! $toolName app install karke open kar lo.\n\n1. Play Store se $toolName install karo.\n2. Account login/setup karo.\n\nKya $toolName open ho gaya?",
            smartQuestionHindi = "$toolName install karke open kar liya?",
            smartQuestionEnglish = "Did you install and open $toolName?",
            smartQuestionHinglish = "$toolName open ho gaya phone me?",
            explainVariantsHindi = listOf(
                "Dekho simple hai! $toolName app store par free available hai. Isse aap professional 4K videos edit kar sakte ho. Pehle step me bas app open karna hai.",
                "Real life example: Jaise kitchen me khana banane se pehle bartan chahiye, waise hi editing ke liye $toolName app hamara main tool hai."
            ),
            explainVariantsEnglish = listOf(
                "It's super simple! $toolName is a free mobile editor that lets you make high quality Reels and Shorts with zero watermarks.",
                "Think of $toolName as your digital editing workstation. Once installed, tap 'New Project' to enter the main studio."
            ),
            explainVariantsHinglish = listOf(
                "Dost ekdum aasan hai: Play Store kholo, search karo '$toolName', Download button dabao aur guest/Google se login kar lo.",
                "Flow chart: Search ➔ Download ➔ Open App ➔ Tap 'New Project'. Iske baad next step unlock hoga!"
            )
        ),
        EditingStep(
            id = 2,
            title = "Interface & Timeline Tour",
            desc = "Master the preview monitor, timeline track & bottom toolbar.",
            initialMessageHindi = "$toolName ka interface samajhte hain!\n\nMain 3 Sections hote hain:\n1. Top Screen: Video Preview Display\n2. Middle Area: Timeline Tracks (Video, Audio, Text Layers)\n3. Bottom Bar: Main Tools (Edit, Audio, Text, Effects, Canvas)\n\nKya teeno sections dikh rahe hain?",
            initialMessageEnglish = "Let's explore the $toolName UI!\n\n3 Main Sections:\n1. Top: Preview Monitor\n2. Center: Timeline (Video, Audio, Text tracks)\n3. Bottom: Toolbar (Cut, Text, Audio, Filters, Speed)\n\nDo you see these 3 areas?",
            initialMessageHinglish = "$toolName ka UI samajhte hain!\n\n3 main parts hain:\n1. Upar: Video Preview screen\n2. Beech me: Timeline jahan video clips hoti hain\n3. Neeche: Tools bar (Edit, Audio, Text, Effects)\n\nKya screen par ye dikh gaya?",
            smartQuestionHindi = "Timeline aur bottom toolbar dikh rahe hain?",
            smartQuestionEnglish = "Can you see the timeline and bottom menu bar?",
            smartQuestionHinglish = "Upar preview aur neeche tools bar dikh gaya?",
            explainVariantsHindi = listOf(
                "Timeline wahi jagah hai jahan aap video clips ko khiska sakte ho, chhota-bada kar sakte ho aur multiple layers add kar sakte ho.",
                "Bottom toolbar me saare tools hote hain. Jaise Scissors (Cut), Text, Audio, aur Speed. Har tool par tap karke sub-menu milta hai."
            ),
            explainVariantsEnglish = listOf(
                "The timeline is your canvas! You pinch to zoom in/out of clips, drag to reorder, and split wherever you want.",
                "The bottom toolbar gives you access to Split, Text, Speed, Filters, and Audio. It automatically changes based on what layer is selected."
            ),
            explainVariantsHinglish = listOf(
                "Dost simple language me:\nBeech waali patti (Timeline) par ungli se drag karke video ke kisi bhi second par ja sakte ho.",
                "Bottom bar par 'Edit' (Scissors ✂️) button dabaoge toh cut and split waale options khul jayenge."
            )
        ),
        EditingStep(
            id = 3,
            title = "Import Video & Canvas Ratio",
            desc = "Import clips and set 9:16 aspect ratio for Reels/Shorts.",
            initialMessageHindi = "Video Import & Ratio setup:\n\n1. 'New Project' (+) button par tap karo.\n2. Apne phone gallery se clips select karke 'Add' karo.\n3. Bottom bar me 'Aspect Ratio' / 'Canvas' par jao aur '9:16' (TikTok/Reels format) select karo!",
            initialMessageEnglish = "Importing & Canvas Aspect Ratio:\n\n1. Tap 'New Project' (+).\n2. Select video clips from gallery and tap 'Add'.\n3. Go to 'Aspect Ratio' / 'Canvas' in bottom toolbar and pick '9:16' for vertical Reels/Shorts!",
            initialMessageHinglish = "Clips import karke 9:16 Ratio lagate hain!\n\n1. 'New Project' (+) daba kar video clips select karo.\n2. Bottom toolbar me 'Canvas' ya 'Ratio' par jao.\n3. '9:16' (Mobile full screen) choose karo!\n\nHo gaya?",
            smartQuestionHindi = "Video import hoke 9:16 ratio set ho gaya?",
            smartQuestionEnglish = "Is your video imported with 9:16 vertical ratio?",
            smartQuestionHinglish = "Clips import karke 9:16 mobile ratio set kar diya?",
            explainVariantsHindi = listOf(
                "Reels aur Shorts ke liye 9:16 ratio zaroori hai. Isse video poore phone screen par bina black bars ke dikhta hai.",
                "Agar video ke aas-paas black space aaye toh timeline clip par click karke do ungliyon (pinch to zoom) se zoom kar lo!"
            ),
            explainVariantsEnglish = listOf(
                "9:16 is the standard vertical full-screen aspect ratio for Instagram Reels, YouTube Shorts, and TikTok.",
                "Pro Tip: If your video has black bars, tap the clip in timeline and use two fingers to zoom in until it fills the screen completely."
            ),
            explainVariantsHinglish = listOf(
                "Bhai 9:16 mobile screen size hota hai. Bottom toolbar me 'Ratio' ➔ '9:16' select karo.",
                "Agar side me black space aa raha hai toh video timeline par tap karke ungliyon se Zoom in kar do."
            )
        ),
        EditingStep(
            id = 4,
            title = "Cut, Trim & Split Dead Pauses",
            desc = "Remove mistakes, silence, and awkward pauses with precision.",
            initialMessageHindi = "Editing ka GOLDEN RULE: Unwanted Pauses Cut Karo!\n\nSteps:\n1. Timeline Playhead ko galti waale point par le jao.\n2. 'Split' (Scissors ✂️) par tap karo.\n3. Galti ke khatam hone par dubara 'Split' karo.\n4. Beech ki bekaar clip select karke 'Delete' (🗑️) dabao!",
            initialMessageEnglish = "GOLDEN RULE: Trim Dead Silence!\n\nSteps:\n1. Move white playhead line to where mistake starts.\n2. Tap 'Split' (Scissors ✂️).\n3. Move line to where mistake ends and tap 'Split' again.\n4. Select middle unwanted piece and tap 'Delete' (🗑️)!",
            initialMessageHinglish = "Kharaab part aur chup rehne waala hissa CUT karo!\n\nSteps:\n1. White line (Playhead) ko bekaar hisse ke start par lao.\n2. 'Split' (✂️) dabao.\n3. Phir end point par jaakar 'Split' dabao.\n4. Beech ki useless clip par tap karke 'Delete' (🗑️) kar do!",
            smartQuestionHindi = "Bekaar pauses aur mistakes cut ho gaye?",
            smartQuestionEnglish = "Did you trim away all mistakes and dead pauses?",
            smartQuestionHinglish = "Mistakes aur dead silence cut kar diya?",
            explainVariantsHindi = listOf(
                "Fast Paced Editing = Viral Reels! Viewer ka har second important hai. Aah, umm, aur chup rehne waale parts sab delete kar do.",
                "Timeline par do ungliyon se zoom-out karke aap frame-by-frame exact split kar sakte ho."
            ),
            explainVariantsEnglish = listOf(
                "Fast jump cuts keep viewer attention span high! Never leave more than 0.5 seconds of silence between sentences.",
                "Pinch out on the timeline to expand it for micro-frame precision cuts."
            ),
            explainVariantsHinglish = listOf(
                "Dost ise Jump Cuts kehte hain. Lagatar bolne waali energetic video viral hoti hai.",
                "Flow: Playhead stop ➔ Split ✂️ ➔ Move ahead ➔ Split ✂️ ➔ Select middle clip ➔ Delete 🗑️."
            )
        ),
        EditingStep(
            id = 5,
            title = "Text, Fonts & Animations",
            desc = "Add stylish text, shadow, stroke & animated entry effects.",
            initialMessageHindi = "Text & Titles Add Karte Hain!\n\n1. Bottom toolbar me 'Text' -> 'Add Text' par jao.\n2. Apna title/heading type karo.\n3. 'Font' me bold fonts (e.g. Montserrat, Anton) choose karo.\n4. 'Style' me Black stroke / Yellow text or Shadow add karo!",
            initialMessageEnglish = "Adding Engaging Text & Fonts!\n\n1. Tap 'Text' -> 'Add Text' on toolbar.\n2. Type your main title.\n3. Pick BOLD fonts (Anton, Montserrat, Proxima).\n4. In 'Style', add black outline border + yellow/white text fill!",
            initialMessageHinglish = "Bada-bada Catchy Text add karte hain!\n\n1. Toolbar me 'Text' (T) ➔ 'Add Text' dabao.\n2. Apna main point likho.\n3. 'Font' me BOLD font select karo.\n4. 'Style' me Yellow color + Black stroke boundary lagao!",
            smartQuestionHindi = "Bold text aur style lag gaya clip par?",
            smartQuestionEnglish = "Did you style your title text with bold fonts and stroke?",
            smartQuestionHinglish = "Text likhkar bold style aur stroke laga diya?",
            explainVariantsHindi = listOf(
                "Rule: Text bilkul mobile screen ke center me aur eye-level par hona chahiye. Font aisi ho jo door se saaf padhi ja sak sake.",
                "'Animation' tab me jaakar Text par 'Pop In' ya 'Typewriter' effect dalo taaki dynamic entry ho."
            ),
            explainVariantsEnglish = listOf(
                "Keep text centered and high-contrast! Bright yellow or white with black stroke guarantees 100% readability.",
                "Use 'Animation' -> 'In' -> 'Typewriter' or 'Pop' for high-energy text appearance."
            ),
            explainVariantsHinglish = listOf(
                "Dost text chota mat likhna! 2-3 bade words screen ke beech me rakho.",
                "Text animation me 'Typewriter' effect lagane se viewers ka dhyaan tiki rehti hai."
            )
        ),
        EditingStep(
            id = 6,
            title = "Transitions & Keyframe Zoom",
            desc = "Smooth clip transitions & keyframe (💎) movement effects.",
            initialMessageHindi = "Keyframe 💎 Magic & Transitions!\n\n1. Do clips ke beech me jo square box [] hota hai par click karke 'Pull In' / 'Spin' transition dalo.\n2. Keyframe (💎) Icon: Clip par click karo, Diamond icon dabao. Aage jaakar video ko zoom karo -> Automatic smooth zoom motion ban jayega!",
            initialMessageEnglish = "Transitions & Keyframe Motion 💎!\n\n1. Tap the square icon between clips to add smooth transitions ('Glitch', 'Pull In', 'Blur').\n2. Keyframe (💎): Tap clip, tap Diamond 💎 icon. Scroll forward 1 second, zoom video with fingers -> Instant dynamic zoom animation!",
            initialMessageHinglish = "Transitions aur Diamond Keyframe 💎 Motion!\n\n1. Clips ke beech waale box par tap karke 'Glitch' ya 'Pull In' transition dalo.\n2. Keyframe (💎): Clip select karo ➔ Diamond 💎 icon dabao ➔ Thoda aage badho aur video zoom kar do ➔ Smooth zoom-in ready!",
            smartQuestionHindi = "Keyframe zoom aur transition samajh aaye?",
            smartQuestionEnglish = "Did you apply transitions and keyframe zoom?",
            smartQuestionHinglish = "Diamond Keyframe 💎 se zoom effect bana liya?",
            explainVariantsHindi = listOf(
                "Keyframe 💎 ka kaam video me movement lana hai! Jab aap kisi image/video par 2 keyframes lagate ho, toh $toolName dono points ke beech smooth motion bana deta hai.",
                "Transition hamesha fast (0.2s - 0.4s) rakho. Zyaada lambi transition video ko slow kar deti hai."
            ),
            explainVariantsEnglish = listOf(
                "Keyframes 💎 create custom camera movement! Point A (Normal size) to Point B (Zoomed in) creates automatic motion.",
                "Keep transition speed short (0.3 seconds) to maintain fast momentum."
            ),
            explainVariantsHinglish = listOf(
                "Bhai Keyframe 💎 sabse powerful feature hai! Isse kisi bhi photo ya video ko float/zoom kara sakte ho.",
                "Trial: Start me Keyframe 💎 dabao ➔ 1 sec aage jao ➔ Video zoom kar do ➔ Play karke dekho!"
            )
        ),
        EditingStep(
            id = 7,
            title = "Audio, Music & Sound Effects",
            desc = "Sync trending background music (10-15% vol) & Whoosh sound effects.",
            initialMessageHindi = "Audio & Sound Effects (Whoosh, Pop)!\n\n1. 'Audio' -> 'Sounds' me trending background music add karo.\n2. Volume: Background Music ki Volume 10-15% rakho taaki aapki awaaz saaf sune!\n3. Text/Transitions ke saath 'Whoosh' ya 'Pop' Sound Effect dalo.",
            initialMessageEnglish = "Audio Sync & Sound FX!\n\n1. Tap 'Audio' -> Add trending background track.\n2. Set Background Music Volume to 10-15% (Voiceover must be 100% loud & crisp).\n3. Add 'Whoosh' or 'Pop' SFX whenever text appears!",
            initialMessageHinglish = "Music aur Sound Effects (Whoosh 🔊, Pop)!\n\n1. 'Audio' ➔ Background Music select karo.\n2. Music volume 10-15% par set karo (Aapki voice main sunni chahiye).\n3. Jab bhi screen pe text aaye, 'Whoosh' ya 'Pop' sound effect dalo!",
            smartQuestionHindi = "Background music ki volume 15% aur SFX set ho gaya?",
            smartQuestionEnglish = "Is your music volume balanced and SFX added?",
            smartQuestionHinglish = "Music volume 15% karke SFX laga diye?",
            explainVariantsHindi = listOf(
                "Audio rule: Video me voice clear nahi hogi toh viewer turant swipe up kar dega! Music humesha halka background me hona chahiye.",
                "Sound effects (Whoosh, Pop, Camera Click) viewers ke dimaag ko alert rakhte hain."
            ),
            explainVariantsEnglish = listOf(
                "Audio balance: Voiceover = 100%, Music = 10-15%. Always lower music level when voice is present.",
                "Sound Effects (Whoosh, Pop, Swoosh) increase visual impact by 3x!"
            ),
            explainVariantsHinglish = listOf(
                "Dost, agar background music loud hoga toh log skip kar denge. Music Volume = 15%, Main Voice Volume = 100% or 150%.",
                "Whoosh sound effect transitions ke sath bohot solid lagta hai."
            )
        ),
        EditingStep(
            id = 8,
            title = "Auto Captions & Subtitles",
            desc = "Generate auto captions & customize glowing animated captions.",
            initialMessageHindi = "Auto Captions (Animated Subtitles)!\n\n1. Toolbar me 'Text' -> 'Auto Captions' par tap karo.\n2. Language 'Hindi/English' choose karke 'Start' dabao.\n3. Automatic captions generate ho jayenge! Unka style & color customize kar lo.",
            initialMessageEnglish = "Auto Captions & Subtitles!\n\n1. Tap 'Text' -> 'Auto Captions' on toolbar.\n2. Select language & tap 'Generate'.\n3. Instant synced captions created! Customize font, background color & active word highlight.",
            initialMessageHinglish = "Auto Captions (Animated Captions)!\n\n1. 'Text' ➔ 'Auto Captions' par tap karo.\n2. Language Hindi/English select karke 'Generate' dabao.\n3. Automatically puri video me subtitles likhe aa jayenge!",
            smartQuestionHindi = "Auto captions generate hoke style ho gaye?",
            smartQuestionEnglish = "Did you generate and style your Auto Captions?",
            smartQuestionHinglish = "Auto captions generate karke test kar liya?",
            explainVariantsHindi = listOf(
                "80% log social media par MUTE video dekhte hain! Captions hone se bina sound ke bhi video samajh aati hai.",
                "Auto captions me kisi galat word par tap karke 'Batch Edit' se spelling thik kar sakte ho."
            ),
            explainVariantsEnglish = listOf(
                "Over 80% of viewers watch Reels on mute. Auto Captions keep engagement high regardless of sound.",
                "Use 'Batch Edit' to fix any minor misheard words in one clean screen."
            ),
            explainVariantsHinglish = listOf(
                "Bhai captions se watch time double ho jata hai! Automatic captions aane ke baad 'Batch Edit' karke spelling check kar lo.",
                "Active word glow effect select karne se captions har word ke saath highlight hote hain."
            )
        ),
        EditingStep(
            id = 9,
            title = "Color Grading, Filters & Adjustments",
            desc = "Enhance contrast, saturation, sharpness & skin tone glow.",
            initialMessageHindi = "Color Grading & Cinematic Look!\n\n1. Timeline par clip select karo -> 'Adjust' par jao.\n2. Saturation: +10, Contrast: +10, Sharpness: +25.\n3. 'Filters' tab me clean aesthetic filter select karo!",
            initialMessageEnglish = "Color Grading & Aesthetic Look!\n\n1. Select clip -> Tap 'Adjust'.\n2. Boost Saturation (+10), Contrast (+10), Sharpness (+20).\n3. Apply a subtle aesthetic filter from 'Filters' tab!",
            initialMessageHinglish = "Video ki Quality & Colors sharp karo!\n\n1. Clip par tap karke 'Adjust' par jao.\n2. Saturation: +10, Contrast: +10, Sharpness: +20.\n3. 'Filters' me jake clean filter lagao!",
            smartQuestionHindi = "Adjustments aur Sharpness +20 apply kar diya?",
            smartQuestionEnglish = "Applied color adjustments and sharpness?",
            smartQuestionHinglish = "Colors sharp aur clear ho gaye?",
            explainVariantsHindi = listOf(
                "Sharpness (+20 to +30) add karne se video mobile camera se bhi DSLR jaisi sharp dikhne lagti hai!",
                "Vibrance aur Skin Tone ko over-saturate mat karo. Natural aur vibrant look best hota hai."
            ),
            explainVariantsEnglish = listOf(
                "Increasing Sharpness (+25) makes mobile footage look crisp like a professional camera!",
                "Keep skin tones natural while boosting environment greens and blues with Saturation."
            ),
            explainVariantsHinglish = listOf(
                "Dost, Sharpness +25 lagate hi video bilkul HD/4K lagne lagti hai! Halka sa contrast aur saturation badha do.",
                "Isse visual quality instantly 2x boost ho jaati hai."
            )
        ),
        EditingStep(
            id = 10,
            title = "Export Settings & HD Publishing",
            desc = "Export in 1080p, 60 FPS, High Bitrate with zero compression.",
            initialMessageHindi = "Export in FULL HD 60 FPS!\n\n1. Top Right me '1080p' button par tap karo.\n2. Resolution: 1080p (4K compression kar deta hai Reels par).\n3. Frame Rate: 60 FPS (Super Smooth).\n4. Codec/Bitrate: High.\n5. 'Export' par click karke save karo!",
            initialMessageEnglish = "Best Export Settings for Reels/Shorts!\n\n1. Tap '1080p' export button top right.\n2. Resolution: 1080p (Recommended over 4K for Reels).\n3. Frame Rate: 60 FPS (Silky smooth).\n4. Bitrate: High.\n5. Tap 'Export'!",
            initialMessageHinglish = "FULL HD 60 FPS Export Settings!\n\n1. Top right me Export Settings (1080p) par click karo.\n2. Resolution: 1080p\n3. Frame Rate: 60 FPS\n4. Bitrate: High\n5. 'Export' button dabao!",
            smartQuestionHindi = "1080p 60 FPS me video export ho gaya?",
            smartQuestionEnglish = "Did you export in 1080p 60 FPS?",
            smartQuestionHinglish = "1080p 60 FPS me gallery me save ho gaya?",
            explainVariantsHindi = listOf(
                "Reels ke liye 1080p best hai! 4K upload karne par Instagram compress karke blur kar deta hai.",
                "Badhai ho 🎉! Aapne $toolName Zero to Advanced Master Course complete kar liya hai! Regular practice karo aur viral videos banao!"
            ),
            explainVariantsEnglish = listOf(
                "Pro Tip: Always use 1080p instead of 4K for Instagram Reels! Instagram compresses 4K heavily, making it look worse than native 1080p.",
                "Congratulations 🎉! You have successfully mastered $toolName with your AI Mentor. Keep editing and creating high-impact videos!"
            ),
            explainVariantsHinglish = listOf(
                "Bhai 1080p + 60 FPS + High Bitrate = Perfect Crisp Reel! Direct 4K upload karoge toh Instagram compression se video phat sakti hai.",
                "Mubarak ho dost 🎉! $toolName Mentor Course complete ho gaya! Main hamesha yahan hoon guidance ke liye."
            )
        )
    )
}

@Composable
fun VideoEditingMentorAiDialog(
    toolType: EditingToolType,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // Isolated memory state for this specific tool
    var selectedLang by remember {
        mutableStateOf(
            CreatorAcademyPrefs.getEditingToolLanguage(context, toolType.key)?.let {
                try { EditingLanguage.valueOf(it) } catch (e: Exception) { null }
            }
        )
    }
    var videoType by remember {
        mutableStateOf(CreatorAcademyPrefs.getEditingToolVideoType(context, toolType.key))
    }
    var currentStepId by remember {
        mutableIntStateOf(CreatorAcademyPrefs.getEditingToolCurrentStep(context, toolType.key))
    }

    val completedSteps = remember {
        mutableStateListOf<Int>().apply {
            addAll(CreatorAcademyPrefs.getEditingToolCompletedSteps(context, toolType.key))
        }
    }

    // Active tool overlays
    var activeToolOverlay by remember { mutableStateOf<String?>(null) }
    var showWelcomeBack by remember { mutableStateOf(selectedLang != null && videoType != null && (currentStepId > 1 || completedSteps.isNotEmpty())) }
    var showIntro by remember { mutableStateOf(!showWelcomeBack) }
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
                val editingIntroCards = remember(toolType) {
                    when (toolType) {
                        EditingToolType.CAPCUT -> listOf(
                            ToolIntroCardData(
                                title = "CapCut Pro Speed & Text Mastery",
                                subtitle = "Master Keyframe Animations & Velocity Cuts",
                                icon = Icons.Default.Movie,
                                highlightTag = "CapCut Masterclass",
                                bulletPoints = listOf(
                                    "Auto-Caption Styling & Text Effects",
                                    "Smooth Slow-Mo & Velocity Curves",
                                    "3D Text Overlays & Keyframes",
                                    "4K 60fps High Quality Export Settings"
                                )
                            ),
                            ToolIntroCardData(
                                title = "Trending CapCut Effects",
                                subtitle = "1-Tap Viral Transitions & Beat Syncing",
                                icon = Icons.Default.AutoAwesome,
                                highlightTag = "Viral CapCut Effects",
                                bulletPoints = listOf(
                                    "Neon Edge Glow & Body Effects",
                                    "Auto-Velocity Beat Matching",
                                    "Green Screen & Cutout Removal",
                                    "Sound FX & Beat Drop Timing"
                                )
                            ),
                            ToolIntroCardData(
                                title = "Pro Mobile Workflow",
                                subtitle = "Edit Reels 10x Faster on Mobile",
                                icon = Icons.Default.CheckCircle,
                                highlightTag = "CapCut Workflow",
                                bulletPoints = listOf(
                                    "Timeline Trimming & Split Hacks",
                                    "Audio Beat Markers & Waveforms",
                                    "Color Grading Filters & Presets",
                                    "Direct Instagram & TikTok Export"
                                )
                            )
                        )
                        EditingToolType.VN -> listOf(
                            ToolIntroCardData(
                                title = "VN Video Editor Masterclass",
                                subtitle = "No Watermark Pro Mobile Video Editing",
                                icon = Icons.Default.Movie,
                                highlightTag = "VN Masterclass",
                                bulletPoints = listOf(
                                    "Speed Curve Customization & Ramping",
                                    "Multi-Track Timeline & PIP Layers",
                                    "Keyframe Animation & Masking",
                                    "Custom LUT Filters & Color Profiles"
                                )
                            ),
                            ToolIntroCardData(
                                title = "VN Code & Template Sharing",
                                subtitle = "Import Viral VN Templates & Sound Kits",
                                icon = Icons.Default.AutoAwesome,
                                highlightTag = "VN Code Engine",
                                bulletPoints = listOf(
                                    "1-Tap VN Code QR Import",
                                    "Beat Alignment & Music Sync",
                                    "Chroma Key & Background Removal",
                                    "Text Animations & Preset Effects"
                                )
                            ),
                            ToolIntroCardData(
                                title = "Cinematic Mobile Color & Cut",
                                subtitle = "Professional Color Grading & Audio Mixing",
                                icon = Icons.Default.CheckCircle,
                                highlightTag = "VN Production",
                                bulletPoints = listOf(
                                    "HSL Color Wheel Adjustments",
                                    "Fade In/Out Audio Curves",
                                    "Aspect Ratio Presets (9:16, 16:9)",
                                    "Lossless 4K 60fps Exporting"
                                )
                            )
                        )
                        EditingToolType.INSTAGRAM_EDITS -> listOf(
                            ToolIntroCardData(
                                title = "Instagram In-App Edits & Reels Studio",
                                subtitle = "Create Viral Reels Directly Inside Instagram",
                                icon = Icons.Default.CameraAlt,
                                highlightTag = "Instagram Reels Studio",
                                bulletPoints = listOf(
                                    "Reel Audio Beat Syncing",
                                    "Text Timing & Sticker Placement",
                                    "Trending Audio & Effects Engine",
                                    "Custom Cover Photo Frame Selection"
                                )
                            ),
                            ToolIntroCardData(
                                title = "In-App Transitions & Filters",
                                subtitle = "Level Up Engagement with Native Instagram Tools",
                                icon = Icons.Default.AutoAwesome,
                                highlightTag = "Native Reels Hacks",
                                bulletPoints = listOf(
                                    "Seamless Transition Cuts",
                                    "Native Voiceover & Auto Captions",
                                    "Interactive Polls & Quiz Overlays",
                                    "Reel Grid Frame Alignment"
                                )
                            ),
                            ToolIntroCardData(
                                title = "High Quality Export Settings",
                                subtitle = "Prevent Blur & Quality Drop on Reels Uploads",
                                icon = Icons.Default.CheckCircle,
                                highlightTag = "Crisp Reel Uploads",
                                bulletPoints = listOf(
                                    "Enable 'Upload at Highest Quality' Setting",
                                    "Color Profile & Brightness Balance",
                                    "9:16 Aspect Ratio Margin Guides",
                                    "Draft Saving & Scheduling Tips"
                                )
                            )
                        )
                    }
                }
                CommonToolIntroContainer(
                    cards = editingIntroCards,
                    onCompleteIntro = { showIntro = false }
                )
            } else if (showWelcomeBack && selectedLang != null && videoType != null) {
                SmartWelcomeBackDialog(
                    courseTitle = "${toolType.title} Course",
                    currentStep = currentStepId,
                    totalSteps = getEditingRoadmapSteps(toolType).size,
                    onContinue = { showWelcomeBack = false },
                    onRestart = { showRestartConfirm = true },
                    onDismiss = onDismiss
                )
            } else if (selectedLang == null) {
                // STEP 1: LANGUAGE SELECTION OVERLAY
                EditingLanguageSelectionOverlay(
                    toolType = toolType,
                    onSelect = { lang ->
                        selectedLang = lang
                        CreatorAcademyPrefs.saveEditingToolLanguage(context, toolType.key, lang.name)
                    },
                    onClose = onDismiss
                )
            } else if (videoType == null) {
                // STEP 2: VIDEO FORMAT / CATEGORY SELECTION OVERLAY
                EditingVideoTypeSelectionOverlay(
                    toolType = toolType,
                    selectedLanguage = selectedLang!!,
                    onSelect = { type ->
                        videoType = type
                        CreatorAcademyPrefs.saveEditingToolVideoType(context, toolType.key, type)
                    },
                    onBack = { selectedLang = null }
                )
            } else {
                // STEP 3: MAIN AI MENTOR CHAT INTERFACE
                EditingMentorChatScreen(
                    toolType = toolType,
                    language = selectedLang!!,
                    videoType = videoType!!,
                    currentStepId = currentStepId,
                    completedSteps = completedSteps,
                    onStepChange = { newStep ->
                        currentStepId = newStep
                        CreatorAcademyPrefs.saveEditingToolCurrentStep(context, toolType.key, newStep)
                    },
                    onStepComplete = { stepId ->
                        if (!completedSteps.contains(stepId)) {
                            completedSteps.add(stepId)
                            CreatorAcademyPrefs.saveEditingToolCompletedSteps(context, toolType.key, completedSteps.toSet())
                        }
                    },
                    onChangeLangClick = { selectedLang = null },
                    onChangeTypeClick = { videoType = null },
                    onOpenTool = { tool -> activeToolOverlay = tool },
                    onResetCourse = { showRestartConfirm = true },
                    onClose = onDismiss
                )
            }

            if (showRestartConfirm) {
                RestartCourseConfirmDialog(
                    courseTitle = "${toolType.title} Course",
                    onConfirmRestart = {
                        CreatorAcademyPrefs.resetCourseProgress(context, toolType.key)
                        selectedLang = null
                        videoType = null
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
                    "option_finder" -> OptionFinderDialog(
                        toolType = toolType,
                        onDismiss = { activeToolOverlay = null }
                    )
                    "export_guide" -> ExportGuideDialog(
                        toolType = toolType,
                        onDismiss = { activeToolOverlay = null }
                    )
                    "official_links" -> OfficialLinksDialog(
                        toolType = toolType,
                        onDismiss = { activeToolOverlay = null }
                    )
                    "script_workflow" -> EditingWorkflowGeneratorDialog(
                        toolType = toolType,
                        language = selectedLang ?: EditingLanguage.HINGLISH,
                        videoType = videoType ?: "Reels / Shorts",
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
private fun EditingLanguageSelectionOverlay(
    toolType: EditingToolType,
    onSelect: (EditingLanguage) -> Unit,
    onClose: () -> Unit
) {
    var selectedTemp by remember { mutableStateOf(EditingLanguage.HINGLISH) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0D1711), AmoledBlack, Color(0xFF08100C))
                )
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
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

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(toolType.color.copy(alpha = 0.2f))
                        .border(BorderStroke(1.5.dp, toolType.color), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    OfficialLogo(name = toolType.logoType, modifier = Modifier.size(38.dp))
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "👋 Welcome to ${toolType.title}!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Aap kis language me seekhna chahenge?\nSelect your learning language:",
                    fontSize = 14.sp,
                    color = TextWhite.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(28.dp))

                val languages = listOf(
                    EditingLanguage.HINDI to ("○ हिन्दी" to "Pure Hindi"),
                    EditingLanguage.ENGLISH to ("○ English" to "Pure English"),
                    EditingLanguage.HINGLISH to ("○ Hinglish" to "Hindi + English (Recommended)")
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    languages.forEach { (lang, labels) ->
                        val isSelected = selectedTemp == lang
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(
                                    if (isSelected) Brush.horizontalGradient(listOf(toolType.color.copy(alpha = 0.85f), EmeraldGlow))
                                    else SolidColor(Color(0x18FFFFFF))
                                )
                                .border(
                                    BorderStroke(
                                        if (isSelected) 1.5.dp else 1.dp,
                                        if (isSelected) toolType.color else Color(0x33FFFFFF)
                                    ),
                                    RoundedCornerShape(18.dp)
                                )
                                .clickable { selectedTemp = lang }
                                .padding(horizontal = 20.dp, vertical = 16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text(
                                        text = labels.first,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = labels.second,
                                        fontSize = 12.sp,
                                        color = if (isSelected) TextWhite.copy(alpha = 0.95f) else TextWhite.copy(alpha = 0.55f)
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = TextWhite,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(EmeraldPrimary, EmeraldGlow)
                            )
                        )
                        .clickable { onSelect(selectedTemp) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CONTINUE ➔",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = AmoledBlack,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

// ============================================================================
// 2. VIDEO TYPE SELECTION OVERLAY
// ============================================================================
@Composable
private fun EditingVideoTypeSelectionOverlay(
    toolType: EditingToolType,
    selectedLanguage: EditingLanguage,
    onSelect: (String) -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0D1711), AmoledBlack, Color(0xFF08100C))
                )
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("←", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(toolType.color.copy(alpha = 0.2f))
                        .border(BorderStroke(1.dp, toolType.color), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = selectedLanguage.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = toolType.color
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "🎬 Video Format / Type?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Aap kis type ki video edit karna seekhna chahte hain?",
                fontSize = 13.5.sp,
                color = TextWhite.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            var selectedType by remember { mutableStateOf("Reels / Shorts") }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(VIDEO_TYPES) { (type, emoji) ->
                    val isSelected = selectedType == type
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) toolType.color.copy(alpha = 0.35f)
                                else Color(0x18FFFFFF)
                            )
                            .border(
                                BorderStroke(
                                    if (isSelected) 1.5.dp else 1.dp,
                                    if (isSelected) toolType.color else Color(0x22FFFFFF)
                                ),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedType = type }
                            .padding(horizontal = 18.dp, vertical = 14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(emoji, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = type,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = toolType.color,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(EmeraldPrimary, EmeraldGlow)
                        )
                    )
                    .clickable { onSelect(selectedType) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "START MENTORING 🚀",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = AmoledBlack,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

// ============================================================================
// 3. MAIN EDITING MENTOR CHAT INTERFACE
// ============================================================================
@Composable
private fun EditingMentorChatScreen(
    toolType: EditingToolType,
    language: EditingLanguage,
    videoType: String,
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
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val steps = remember(toolType) { getEditingRoadmapSteps(toolType) }
    val currentStep = steps.find { it.id == currentStepId } ?: steps.first()

    // Chat messages history
    val messages = remember { mutableStateListOf<EditingChatMessage>() }
    var userInput by remember { mutableStateOf("") }
    var explainAttemptCount by remember { mutableIntStateOf(0) }
    var isThinking by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // Load initial message for step
    LaunchedEffect(currentStepId, language) {
        val initialText = when(language) {
            EditingLanguage.HINDI -> currentStep.initialMessageHindi
            EditingLanguage.ENGLISH -> currentStep.initialMessageEnglish
            EditingLanguage.HINGLISH -> currentStep.initialMessageHinglish
        }

        val smartQ = when(language) {
            EditingLanguage.HINDI -> currentStep.smartQuestionHindi
            EditingLanguage.ENGLISH -> currentStep.smartQuestionEnglish
            EditingLanguage.HINGLISH -> currentStep.smartQuestionHinglish
        }

        messages.clear()
        messages.add(
            EditingChatMessage(
                isFromUser = false,
                text = initialText,
                quickReplies = listOf("✅ Ho Gaya / Done", "❓ Dubara Batao (Explain Again)", "📍 Option Kahan Hai?", "⚡ Workflow Tip")
            )
        )
        messages.add(
            EditingChatMessage(
                isFromUser = false,
                text = "🤔 Quick Check: $smartQ",
                quickReplies = listOf("YES! Complete", "NO, Doubt Hai")
            )
        )
        listState.animateScrollToItem(messages.size - 1)
    }

    // Auto-scroll when messages update, user types, or IME opens
    val imeBottomPadding = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
    LaunchedEffect(messages.size, userInput, imeBottomPadding) {
        if (messages.isNotEmpty()) {
            delay(60)
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        // TOP HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF101B15))
                .border(BorderStroke(1.dp, Color(0x22FFFFFF)))
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
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(toolType.color.copy(alpha = 0.2f))
                            .border(BorderStroke(1.dp, toolType.color), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        OfficialLogo(name = toolType.logoType, modifier = Modifier.size(24.dp))
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = toolType.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(EmeraldGlow)
                                    .size(6.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${language.name} • $videoType",
                                fontSize = 11.sp,
                                color = toolType.color,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x22FFFFFF))
                            .clickable { onChangeLangClick() }
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Lang",
                            tint = TextWhite,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(32.dp)
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

        val heroType = when (toolType) {
            EditingToolType.CAPCUT -> ToolHeroType.CAPCUT_MASTER
            EditingToolType.VN -> ToolHeroType.VN_EDITOR
            EditingToolType.INSTAGRAM_EDITS -> ToolHeroType.INSTAGRAM_EDITS
            else -> ToolHeroType.CAPCUT_MASTER
        }
        val heroBadge = when (toolType) {
            EditingToolType.CAPCUT -> "⚡ CAPCUT MASTER AI"
            EditingToolType.VN -> "🎬 VN EDITOR AI"
            EditingToolType.INSTAGRAM_EDITS -> "📸 INSTAGRAM EDITS AI"
            else -> "⚡ CAPCUT MASTER AI"
        }
        val heroSub = when (toolType) {
            EditingToolType.CAPCUT -> "Keyframe & Velocity Accelerator"
            EditingToolType.VN -> "Cinematic Timeline & Color Grading"
            EditingToolType.INSTAGRAM_EDITS -> "In-App Reels Studio & Transition Hacks"
            else -> "Keyframe & Velocity Accelerator"
        }

        ToolHeroBanner(
            toolType = heroType,
            height = 110.dp,
            badgeText = heroBadge,
            subtitleText = heroSub,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
        )

        // STEP PROGRESS BAR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0B130E))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STEP ${currentStep.id} OF ${steps.size}: ${currentStep.title}",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        color = EmeraldGlow
                    )

                    Text(
                        text = "${completedSteps.size}/${steps.size} Done",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite.copy(alpha = 0.6f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Progress line
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    steps.forEach { step ->
                        val isDone = completedSteps.contains(step.id)
                        val isCurrent = step.id == currentStepId
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isDone -> EmeraldGlow
                                        isCurrent -> toolType.color
                                        else -> Color(0x33FFFFFF)
                                    }
                                )
                                .clickable { onStepChange(step.id) }
                        )
                    }
                }
            }
        }

        // LEARNING PROGRESS INDICATOR CARD
        LearningProgressIndicatorCard(
            currentStep = currentStepId,
            totalSteps = steps.size,
            stepTitle = currentStep.title,
            onResetClick = onResetCourse,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
        )

        // ACTION BAR TOOLS (QUICK ACCESS OVERLAYS)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F1712))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ActionChip(
                    icon = Icons.Default.Search,
                    label = "Option Finder 📍",
                    color = toolType.color,
                    onClick = { onOpenTool("option_finder") }
                )
            }
            item {
                ActionChip(
                    icon = Icons.Default.Settings,
                    label = "Export Guide ⚙️",
                    color = EmeraldGlow,
                    onClick = { onOpenTool("export_guide") }
                )
            }
            item {
                ActionChip(
                    icon = Icons.Default.Movie,
                    label = "Editing Workflow ⚡",
                    color = Color(0xFF38BDF8),
                    onClick = { onOpenTool("script_workflow") }
                )
            }
            item {
                ActionChip(
                    icon = Icons.Default.Download,
                    label = "Official Links 🔗",
                    color = Color(0xFFF59E0B),
                    onClick = { onOpenTool("official_links") }
                )
            }
        }

        // CHAT MESSAGES LIST
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            itemsIndexed(messages) { _, msg ->
                ChatBubble(
                    message = msg,
                    toolType = toolType,
                    onQuickReplyClick = { reply ->
                        messages.add(EditingChatMessage(isFromUser = true, text = reply))
                        coroutineScope.launch {
                            delay(100)
                            listState.animateScrollToItem(messages.size - 1)
                            processUserReply(
                                reply = reply,
                                toolType = toolType,
                                language = language,
                                currentStep = currentStep,
                                explainAttemptCount = explainAttemptCount,
                                onIncrementExplain = { explainAttemptCount++ },
                                onNextStep = {
                                    onStepComplete(currentStep.id)
                                    if (currentStep.id < steps.size) {
                                        onStepChange(currentStep.id + 1)
                                    } else {
                                        messages.add(
                                            EditingChatMessage(
                                                isFromUser = false,
                                                text = "🎉 CONGRATULATIONS! Aapne ${toolType.title} Zero to Advanced Mastery complete kar liya hai! 🚀\n\nKya ab aap Advanced Effects Course seekhna chahenge?",
                                                quickReplies = listOf("🚀 Start Advanced Editing", "🏠 Done")
                                            )
                                        )
                                    }
                                },
                                onAddMessage = { newMsg -> messages.add(newMsg) },
                                onSetThinking = { thinking -> isThinking = thinking }
                            )
                        }
                    }
                )
            }

            if (isThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                    ) {
                        Text(
                            text = "${toolType.title} Mentor is typing...",
                            fontSize = 12.sp,
                            color = toolType.color,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (completedSteps.size >= steps.size) {
                item {
                    CourseCompletionCard(
                        courseTitle = "${toolType.title} Video Editing Course",
                        skillsLearned = listOf(
                            "Timeline Trimming & Keyframing",
                            "Text Overlay & Animated Titles",
                            "Sound Effects & Audio Leveling",
                            "4K Export & Color Grading"
                        ),
                        onContinue = { onClose?.invoke() },
                        onResetCourse = { onResetCourse?.invoke() },
                        theme = when (toolType) {
                            EditingToolType.CAPCUT -> MentorToolTheme.CapCutMaster
                            EditingToolType.VN -> MentorToolTheme.VnEditor
                            EditingToolType.INSTAGRAM_EDITS -> MentorToolTheme.InstaAutoDm
                            else -> MentorToolTheme.CapCutMaster
                        }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }
        }

        // CHAT INPUT BOX
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F1712))
                .imePadding()
                .navigationBarsPadding()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    placeholder = {
                        Text(
                            text = "Ask $toolType Mentor any editing question...",
                            fontSize = 13.sp,
                            color = TextWhite.copy(alpha = 0.4f)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = toolType.color,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedContainerColor = Color(0xFF142018),
                        unfocusedContainerColor = Color(0xFF142018),
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (userInput.isNotBlank()) toolType.color else Color(0x33FFFFFF)
                        )
                        .clickable(enabled = userInput.isNotBlank()) {
                            val txt = userInput
                            userInput = ""
                            messages.add(EditingChatMessage(isFromUser = true, text = txt))
                            coroutineScope.launch {
                                delay(100)
                                listState.animateScrollToItem(messages.size - 1)
                                processUserReply(
                                    reply = txt,
                                    toolType = toolType,
                                    language = language,
                                    currentStep = currentStep,
                                    explainAttemptCount = explainAttemptCount,
                                    onIncrementExplain = { explainAttemptCount++ },
                                    onNextStep = {
                                        onStepComplete(currentStep.id)
                                        if (currentStep.id < steps.size) {
                                            onStepChange(currentStep.id + 1)
                                        }
                                    },
                                    onAddMessage = { newMsg -> messages.add(newMsg) },
                                    onSetThinking = { thinking -> isThinking = thinking }
                                )
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (userInput.isNotBlank()) AmoledBlack else TextWhite.copy(alpha = 0.4f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

private suspend fun processUserReply(
    reply: String,
    toolType: EditingToolType,
    language: EditingLanguage,
    currentStep: EditingStep,
    explainAttemptCount: Int,
    onIncrementExplain: () -> Unit,
    onNextStep: () -> Unit,
    onAddMessage: (EditingChatMessage) -> Unit,
    onSetThinking: (Boolean) -> Unit
) {
    onSetThinking(true)
    delay(500)
    onSetThinking(false)

    val lower = reply.lowercase()

    when {
        lower.contains("ho gaya") || lower.contains("done") || lower.contains("yes") || lower.contains("complete") -> {
            onAddMessage(
                EditingChatMessage(
                    isFromUser = false,
                    text = "Awesome! 🎉 Ye step complete ho gaya. Chalo agle step par chalte hain!",
                    quickReplies = listOf("➔ Go To Next Step")
                )
            )
            onNextStep()
        }

        lower.contains("explain again") || lower.contains("dubara") || lower.contains("nahi samjha") || lower.contains("doubt") -> {
            onIncrementExplain()
            val variants = when(language) {
                EditingLanguage.HINDI -> currentStep.explainVariantsHindi
                EditingLanguage.ENGLISH -> currentStep.explainVariantsEnglish
                EditingLanguage.HINGLISH -> currentStep.explainVariantsHinglish
            }
            val freshExplanation = variants.getOrElse(explainAttemptCount % variants.size) {
                "Koi baat nahi dost! $toolType me ye step bohot simple hai. Ek baar fir se try karo: timeline clip par single tap karo aur bottom menu check karo!"
            }

            onAddMessage(
                EditingChatMessage(
                    isFromUser = false,
                    text = "💡 Fresh Explanation:\n\n$freshExplanation",
                    quickReplies = listOf("✅ Samjh Aa Gaya!", "📍 Option Kahan Milega?")
                )
            )
        }

        lower.contains("option") || lower.contains("kahan") || lower.contains("where") -> {
            onAddMessage(
                EditingChatMessage(
                    isFromUser = false,
                    text = "📍 UI Locator Tip:\n1. $toolType me main screen par video timeline par single-tap karo.\n2. Bottom menu me 'Edit' (Scissors ✂️) button dabayein.\n3. Saare tools highlight ho jayenge!",
                    quickReplies = listOf("✅ Got It!", "❓ Dubara Batao")
                )
            )
        }

        else -> {
            val domain = when (toolType) {
                EditingToolType.CAPCUT -> com.example.creatoracademy.MentorToolDomain.CAPCUT_MASTER
                EditingToolType.VN -> com.example.creatoracademy.MentorToolDomain.VN_EDITING
                EditingToolType.INSTAGRAM_EDITS -> com.example.creatoracademy.MentorToolDomain.INSTAGRAM_EDITS
            }
            val aiResponse = com.example.creatoracademy.ViralAiMentorEngine.generateIntegratedMentorResponse(
                domain = domain,
                userQuery = reply,
                userContext = "${toolType.title} Step: ${currentStep.title}",
                language = language.name
            )
            onAddMessage(
                EditingChatMessage(
                    isFromUser = false,
                    text = aiResponse,
                    quickReplies = listOf("✅ Ho Gaya / Done", "❓ Dubara Batao")
                )
            )
        }
    }
}

@Composable
private fun ChatBubble(
    message: EditingChatMessage,
    toolType: EditingToolType,
    onQuickReplyClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (message.isFromUser) 20.dp else 4.dp,
                        bottomEnd = if (message.isFromUser) 4.dp else 20.dp
                    )
                )
                .background(
                    if (message.isFromUser) toolType.color
                    else Color(0xFF142018)
                )
                .border(
                    BorderStroke(
                        1.dp,
                        if (message.isFromUser) toolType.color
                        else Color(0x33FFFFFF)
                    ),
                    RoundedCornerShape(20.dp)
                )
                .padding(14.dp)
        ) {
            Text(
                text = message.text,
                fontSize = 13.5.sp,
                color = if (message.isFromUser) AmoledBlack else TextWhite,
                fontWeight = if (message.isFromUser) FontWeight.Bold else FontWeight.Normal,
                lineHeight = 19.sp
            )
        }

        if (!message.isFromUser && message.quickReplies.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(message.quickReplies) { reply ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x22FFFFFF))
                            .border(BorderStroke(1.dp, toolType.color.copy(alpha = 0.5f)), RoundedCornerShape(14.dp))
                            .clickable { onQuickReplyClick(reply) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = reply,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = toolType.color
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(alpha = 0.15f))
            .border(BorderStroke(1.dp, color), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }
    }
}

// ============================================================================
// TOOL OVERLAYS (OPTION FINDER, EXPORT GUIDE, OFFICIAL LINKS, WORKFLOW)
// ============================================================================
@Composable
private fun OptionFinderDialog(
    toolType: EditingToolType,
    onDismiss: () -> Unit
) {
    val toolName = toolType.title.replace(" AI Mentor", "")
    var searchQuery by remember { mutableStateOf("") }

    val optionsList = listOf(
        "Keyframe 💎" to "Timeline clip select karo ➔ Top right of timeline screen on Diamond 💎 icon. For smooth zoom/pan animations.",
        "Split / Cut ✂️" to "Bottom toolbar ➔ First icon 'Edit' ➔ 'Split'. Use to cut unwanted pauses.",
        "Auto Captions 💬" to "Bottom toolbar ➔ 'Text' ➔ 'Auto Captions'. Generates auto animated subtitles.",
        "Velocity / Speed Curve 📈" to "Select video clip ➔ 'Speed' ➔ 'Curve' ➔ Pick 'Custom' or 'Flash' preset.",
        "Canvas 9:16 📐" to "Bottom toolbar ➔ 'Ratio' / 'Canvas' ➔ Pick '9:16' full mobile screen size.",
        "Chroma Key / Green Screen 💚" to "Select overlay clip ➔ 'Cutout' / 'Chroma Key' ➔ Pick green color circle.",
        "Background Music 🎵" to "Bottom toolbar ➔ 'Audio' ➔ 'Sounds' ➔ Pick trending audio track.",
        "Background Noise Reduction 🔊" to "Select audio/video clip ➔ Scroll right on toolbar ➔ 'Reduce Noise'."
    )

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
                shape = RoundedCornerShape(26.dp),
                color = Color(0xFF101B15)
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
                            text = "📍 $toolName Option Finder",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color(0x22FFFFFF))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search option (e.g. Keyframe, Cut, Speed)...", fontSize = 12.sp, color = TextWhite.copy(0.4f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = toolType.color,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedContainerColor = Color(0xFF16241C),
                        unfocusedContainerColor = Color(0xFF16241C),
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filtered = optionsList.filter {
                        it.first.contains(searchQuery, ignoreCase = true) ||
                        it.second.contains(searchQuery, ignoreCase = true)
                    }

                    filtered.forEach { (name, loc) ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0x18FFFFFF))
                                .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(14.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = toolType.color)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(loc, fontSize = 12.sp, color = TextWhite.copy(alpha = 0.8f), lineHeight = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
}

@Composable
private fun ExportGuideDialog(
    toolType: EditingToolType,
    onDismiss: () -> Unit
) {
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
                shape = RoundedCornerShape(26.dp),
                color = Color(0xFF101B15)
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
                            text = "⚙️ HD 1080p Export Checklist",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color(0x22FFFFFF))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                val exportSpecs = listOf(
                    "Resolution" to "1080p (DO NOT USE 4K for Reels compression)",
                    "Frame Rate" to "60 FPS (Ultra Smooth Motion)",
                    "Codecs / Bitrate" to "High Bitrate (~15 Mbps)",
                    "Aspect Ratio" to "9:16 Vertical Full Screen",
                    "Smart HDR" to "Turn OFF Smart HDR export to avoid dark playback"
                )

                exportSpecs.forEach { (label, valStr) ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x18FFFFFF))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(label, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = EmeraldGlow)
                            Text(valStr, fontSize = 11.5.sp, color = TextWhite, textAlign = TextAlign.End)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(EmeraldGlow)
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("GOT IT! SAVE SETTINGS", fontSize = 13.sp, fontWeight = FontWeight.Black, color = AmoledBlack)
                }
            }
        }
    }
}
}

@Composable
private fun OfficialLinksDialog(
    toolType: EditingToolType,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val toolName = toolType.title.replace(" AI Mentor", "")

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
                shape = RoundedCornerShape(26.dp),
                color = Color(0xFF101B15)
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
                            text = "🔗 $toolName Official Links",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )

                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color(0x22FFFFFF))
                                .clickable { onDismiss() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val links = listOf(
                        "Play Store App Page" to "Official verified store link for $toolName Android",
                        "Official Help Center" to "Frequently asked questions & feature guides",
                        "Community Templates" to "Trending pre-made transition templates"
                    )

                    links.forEach { (title, sub) ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0x18FFFFFF))
                                .clickable {
                                    Toast.makeText(context, "Opening official link for $title...", Toast.LENGTH_SHORT).show()
                                }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text(sub, fontSize = 11.sp, color = TextWhite.copy(0.6f))
                                }
                                Icon(Icons.Default.OpenInNew, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditingWorkflowGeneratorDialog(
    toolType: EditingToolType,
    language: EditingLanguage,
    videoType: String,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val workflowText = """
        🎬 5-MINUTE $videoType WORKFLOW FOR ${toolType.name}:
        1. 0:00 - 0:03 ➔ High curiosity Hook (Bada Bold Text + Whoosh sound)
        2. 0:03 - 0:15 ➔ Jump cut main talking point (Split dead silence)
        3. 0:15 - 0:30 ➔ B-Roll overlay clip + Keyframe 💎 slow zoom-in
        4. 0:30 - 0:45 ➔ Auto Captions on (Yellow text + black stroke)
        5. 0:45 - 0:60 ➔ Call to Action (Subscribe / Follow) + Smooth transition
    """.trimIndent()

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
                shape = RoundedCornerShape(26.dp),
                color = Color(0xFF101B15)
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
                            text = "⚡ $videoType Workflow",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color(0x22FFFFFF))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF16241C))
                        .padding(14.dp)
                ) {
                    Text(workflowText, fontSize = 12.5.sp, color = TextWhite, lineHeight = 18.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFF38BDF8))
                        .clickable {
                            clipboardManager.setText(AnnotatedString(workflowText))
                            Toast.makeText(context, "Workflow copied to clipboard!", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = AmoledBlack, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("COPY WORKFLOW", fontSize = 12.5.sp, fontWeight = FontWeight.Black, color = AmoledBlack)
                    }
                }
            }
        }
    }
}
}
