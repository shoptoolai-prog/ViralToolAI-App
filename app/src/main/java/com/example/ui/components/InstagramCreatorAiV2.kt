package com.example.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.ui.screens.OfficialLogo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// =========================================================
// COLOR PALETTE & THEME CONSTANTS (BLACK + VIOLET)
// =========================================================
private val DarkCanvasBg = Color(0xFF08030C)
private val CardSurfaceBg = Color(0xFF13091E)
private val CardBorderColor = Color(0x33A855F7)
private val VioletPrimary = Color(0xFF8B5CF6)
private val VioletGlow = Color(0xFFA855F7)
private val VioletDeep = Color(0xFF6D28D9)
private val MagentaAccent = Color(0xFFD946EF)
private val TextWhite = Color(0xFFFFFFFF)
private val TextMuted = Color(0xFFB3A1C9)
private val SuccessGreen = Color(0xFF10B981)
private val ErrorRed = Color(0xFFEF4444)

// =========================================================
// LESSON MODEL
// =========================================================
data class InstagramLessonData(
    val id: Int,
    val icon: ImageVector,
    val badge: String,
    val titleEn: String,
    val titleHi: String,
    val subtitleEn: String,
    val subtitleHi: String,
    val conceptEn: String,
    val conceptHi: String,
    val exampleEn: String,
    val exampleHi: String,
    val checklistEn: List<String>,
    val checklistHi: List<String>,
    val quizQuestionEn: String,
    val quizQuestionHi: String,
    val quizOptionsEn: List<String>,
    val quizOptionsHi: List<String>,
    val quizCorrectIndex: Int,
    val quizExplanationEn: String,
    val quizExplanationHi: String,
    val summaryEn: String,
    val summaryHi: String
)

// =========================================================
// OFFICIAL LESSONS DATABASE
// =========================================================
val INSTAGRAM_LESSONS = listOf(
    InstagramLessonData(
        id = 1,
        icon = Icons.Default.AccountCircle,
        badge = "FOUNDATION",
        titleEn = "Profile & Bio Masterclass",
        titleHi = "प्रोफाइल और बायो मास्टरक्लास",
        subtitleEn = "Turn your Instagram profile into a high-converting creator billboard",
        subtitleHi = "अपने इंस्टाग्राम प्रोफाइल को हाई-कन्वर्टिंग क्रिएटर बिलबोर्ड बनाएं",
        conceptEn = "Your profile is your digital storefront. Within 2 seconds, a visitor decides whether to follow you or leave. A high-converting profile requires 5 elements: a searchable HD handle, clear high-contrast profile picture, a 3-line value bio, social proof, and a single targeted call-to-action link.",
        conceptHi = "आपकी प्रोफाइल आपकी डिजिटल दुकान है। 2 सेकंड के भीतर, एक विजिटर यह तय करता है कि आपको फॉलो करना है या छोड़ना है। एक सफल प्रोफाइल के लिए 5 चीजें ज़रूरी हैं: सर्च योग्य हैंडल, एचडी प्रोफाइल फोटो, 3-लाइन वैल्यू बायो, सोशल प्रूफ और 1 डायरेक्ट लिंक।",
        exampleEn = "Line 1: 🚀 AI Tech Creator | Line 2: 💡 Daily AI Hacks & Reel Growth | Line 3: ⬇️ Free Creator Growth Kit below",
        exampleHi = "लाइन 1: 🚀 AI टेक क्रिएटर | लाइन 2: 💡 डेली AI हैक्स और रील ग्रोथ | लाइन 3: ⬇️ फ्री क्रिएटर टूलकिट के लिए नीचे क्लिक करें",
        checklistEn = listOf(
            "Clean & searchable handle without extra numbers or symbols",
            "High contrast face DP with bright solid background",
            "3-line value proposition bio stating who you serve",
            "One active call-to-action link pointing to your target destination"
        ),
        checklistHi = listOf(
            "बिना किसी उलझन वाला सर्च-फ्रेंडली यूजरनेम",
            "साफ चेहरा और ब्राइट बैकग्राउंड वाली HD डीपी",
            "स्पष्ट 3-लाइन वैल्यू बायो जो बताती है आप क्या वैल्यू देते हैं",
            "1 चालू कॉल-टू-एक्शन लिंक"
        ),
        quizQuestionEn = "What is the primary function of the first line of your Instagram Bio?",
        quizQuestionHi = "इंस्टाग्राम बायो की पहली लाइन का मुख्य उद्देश्य क्या है?",
        quizOptionsEn = listOf(
            "Clearly state who you are & your exact content niche",
            "Write random aesthetic symbols and emojis",
            "Put your phone number for public viewing",
            "Copy paste random song lyrics"
        ),
        quizOptionsHi = listOf(
            "आप कौन हैं और आपकी नीच क्या है, यह साफ़ बताना",
            "सिर्फ स्टाइलिश चिन्ह लगाना",
            "अपना फोन नंबर लिखना",
            "कोई भी यादृच्छिक गाना लिखना"
        ),
        quizCorrectIndex = 0,
        quizExplanationEn = "The first line acts as your headline. Visitors need to immediately know what value they will receive by tapping Follow!",
        quizExplanationHi = "पहली लाइन आपकी हेडलाइन है। विजिटर्स को तुरंत पता चलना चाहिए कि आपको फॉलो करने से उन्हें क्या फायदा होगा!",
        summaryEn = "Focus on clarity over cleverness. A crisp DP, clear bio, and active link convert 3x more profile visits into followers.",
        summaryHi = "बायो को हमेशा साफ़ और सीधा रखें। एक बेहतरीन डीपी और वैल्यू बायो आपकी रीच और फॉलोअर्स 3 गुना बढ़ाता है।"
    ),
    InstagramLessonData(
        id = 2,
        icon = Icons.Default.Dashboard,
        badge = "CREATOR DASHBOARD",
        titleEn = "Professional Account & Creator Insights",
        titleHi = "प्रोफेशनल अकाउंट और क्रिएटर इनसाइट्स",
        subtitleEn = "Unlock analytics, monetization tools, and royalty-free creator audio",
        subtitleHi = "एनालिटिक्स, मोनेटाइजेशन टूल और क्रिएटर ऑडियो अनलॉक करें",
        conceptEn = "Personal accounts hide crucial growth data. Switching to a Professional Creator Account unlocks the Professional Dashboard. This gives you exact metrics on Reached Accounts, Engaged Accounts, Follower Active Hours, and Top Performing Reels.",
        conceptHi = "पर्सनल अकाउंट में रीच का डाटा नहीं दिखता। प्रोफेशनल क्रिएटर अकाउंट में स्विच करने पर आपको 'प्रोफेशनल डैशबोर्ड' मिलता है। इससे आपको रीच, एक्टिव फॉलोअर्स टाइम और वायरल रील्स का सही आँकड़ा मिलता है।",
        exampleEn = "Go to Settings ⚙️ -> Account -> Switch to Professional Account -> Select 'Digital Creator' -> Complete Setup!",
        exampleHi = "सेटिंग्स ⚙️ -> अकाउंट -> स्विच टू प्रोफेशनल अकाउंट -> 'डिजिटल क्रिएटर' चुनें -> ओके करें!",
        checklistEn = listOf(
            "Switch account category to Digital Creator or Video Creator",
            "Enable Professional Dashboard access in profile settings",
            "Inspect Follower Active Hours chart to find peak posting windows",
            "Review Reached Accounts breakdown for non-follower discovery"
        ),
        checklistHi = listOf(
            "अकाउंट टाइप डिजिटल क्रिएटर में बदलें",
            "प्रोफेशनल डैशबोर्ड ऑन करें",
            "फॉलोअर्स एक्टिव रहने का समय देखें",
            "रीच ग्राफ और इनसाइट्स चेक करें"
        ),
        quizQuestionEn = "Why is switching to a Professional Creator Account essential for growth?",
        quizQuestionHi = "ग्रोथ के लिए प्रोफेशनल क्रिएटर अकाउंट पर स्विच करना क्यों ज़रूरी है?",
        quizOptionsEn = listOf(
            "It unlocks Analytics, Insights, and Creator Monetization tools",
            "It automatically makes all your uploaded videos private",
            "It deletes old posts automatically after 30 days",
            "It blocks non-followers from viewing your content"
        ),
        quizOptionsHi = listOf(
            "यह एनालिटिक्स, इनसाइट्स और क्रिएटर टूल्स अनलॉक करता है",
            "यह वीडियो को प्राइवेट कर देता है",
            "यह पुराने पोस्ट डिलीट कर देता है",
            "यह लोगों को ब्लॉक करता है"
        ),
        quizCorrectIndex = 0,
        quizExplanationEn = "Insights allow you to measure what content works best and double down on high-performing video formats!",
        quizExplanationHi = "इनसाइट्स की मदद से आपको पता चलता है कि कौन सी वीडियो वायरल हो रही है ताकि आप वैसी ही और वीडियो बना सकें!",
        summaryEn = "Always monitor your Professional Dashboard to publish content when your audience is most active.",
        summaryHi = "हमेशा प्रोफेशनल डैशबोर्ड चेक करें और फॉलोअर्स के एक्टिव टाइम पर ही पोस्ट करें।"
    ),
    InstagramLessonData(
        id = 3,
        icon = Icons.Default.HighQuality,
        badge = "QUALITY SETTINGS",
        titleEn = "1080p Crystal Clear HD Uploads",
        titleHi = "1080p क्रिस्टल क्लियर HD रील्स",
        subtitleEn = "Stop Instagram compression from destroying your video crispness",
        subtitleHi = "इंस्टाग्राम कंप्रेशन को अपनी वीडियो की क्वालिटी खराब करने से रोकें",
        conceptEn = "By default, Instagram compresses high-resolution videos to save server bandwidth. If your video appears blurry, the algorithm stops pushing it to the Explore page. Toggling 'Upload at Highest Quality' forces Instagram to render your content in full crisp 1080p resolution.",
        conceptHi = "डिफ़ॉल्ट रूप से इंस्टाग्राम डेटा बचाने के लिए वीडियो की क्वालिटी घटा देता है। धुंधली वीडियो को इंस्टाग्राम का एल्गोरिदम आगे नहीं बढ़ाता। 'अपलोड एट हाईएस्ट क्वालिटी' सेटिंग ऑन करने पर आपकी रील फुल HD में रेंडर होती है।",
        exampleEn = "Settings & Privacy ⚙️ -> Media Quality -> Turn ON 'Upload at Highest Quality'.",
        exampleHi = "सेटिंग्स एंड प्राइवेसी ⚙️ -> मीडिया क्वालिटी -> 'अपलोड एट हाईएस्ट क्वालिटी' बटन चालू करें।",
        checklistEn = listOf(
            "Enable 'Upload at Highest Quality' in Instagram Settings",
            "Record & export videos in 1080p at 60 FPS bitrate",
            "Ensure bright, even facial lighting while filming",
            "Avoid applying heavy Instagram filters that introduce noise"
        ),
        checklistHi = listOf(
            "इंस्टाग्राम सेटिंग्स में 'हाईएस्ट क्वालिटी' ऑन करें",
            "1080p 60FPS पर वीडियो एक्सपोर्ट करें",
            "शूटिंग के समय अच्छी लाइट का इस्तेमाल करें",
            "ज्यादा हैवी फिल्टर से बचें जो वीडियो धुंधली करे"
        ),
        quizQuestionEn = "Which setting prevents Instagram from compressing and blurring your uploaded Reels?",
        quizQuestionHi = "कौन सी सेटिंग इंस्टाग्राम को आपकी अपलोड की गई रील्स को धुंधला करने से रोकती है?",
        quizOptionsEn = listOf(
            "Upload at Highest Quality",
            "Data Saver Mode",
            "Auto Archive Posts",
            "Mute Audio Clips"
        ),
        quizOptionsHi = listOf(
            "अपलोड एट हाईएस्ट क्वालिटी (Upload at Highest Quality)",
            "डेटा सेवर मोड",
            "ऑटो आर्काइव",
            "म्यूट ऑडियो"
        ),
        quizCorrectIndex = 0,
        quizExplanationEn = "High visual quality retains viewers for longer watch time, driving significantly higher algorithmic reach!",
        quizExplanationHi = "साफ और HD वीडियो देखने में लोग ज्यादा रुकते हैं, जिससे वॉच टाइम और रीच बढ़ती है!",
        summaryEn = "HD clarity is non-negotiable. Crisp 1080p resolution significantly boosts initial watch retention.",
        summaryHi = "हमेशा HD 1080p में ही वीडियो पोस्ट करें। इससे आपकी रील का फर्स्ट इम्प्रेशन बहुत बढ़िया पड़ता है।"
    ),
    InstagramLessonData(
        id = 4,
        icon = Icons.Default.AutoAwesome,
        badge = "VIRAL HOOKS",
        titleEn = "The 3-Second Viral Hook System",
        titleHi = "3-सेकंड वायरल हुक मास्टरक्लास",
        subtitleEn = "Master visual and verbal hooks that stop the thumb instantly",
        subtitleHi = "वीडियो की शुरुआत में ही दर्शक को रोकने का अनोखा फॉर्मूला",
        conceptEn = "Average attention spans on Instagram Reels are under 3 seconds. If your Reel doesn't create immediate curiosity or visual tension in the first 3 seconds, viewers swipe away. Use a combination of bold text overlay, energetic voiceover, and visual movement.",
        conceptHi = "इंस्टाग्राम पर लोगों का ध्यान सिर्फ 3 सेकंड का होता है। अगर शुरुआत में ही कोई मजबूत हुक या बड़ा सवाल नहीं है, तो यूजर तुरंत स्वाइप कर देगा। पहली 3 सेकंड में स्क्रीन पर बड़ा टेक्स्ट और दमदार वॉइस दें।",
        exampleEn = "Verbal Hook: 'Stop making this 1 mistake on Reels if you want 100k views!' + On-screen text: 'WRONG REEL MISTAKE 🚨'",
        exampleHi = "वॉइस हुक: 'अगर 1 लाख व्यूज चाहिए तो रील में यह 1 गलती करना बंद करो!' + स्क्रीन टेक्स्ट: 'बड़ी रील गलती 🚨'",
        checklistEn = listOf(
            "Place bold contrast on-screen text in frame 1",
            "Ask a compelling curiosity gap question",
            "Use visual movement or gesture immediately",
            "Keep the opening hook line under 6 words"
        ),
        checklistHi = listOf(
            "पहले फ्रेम में ही बड़ा और साफ टेक्स्ट रखें",
            "उत्सुकता बढ़ाने वाला सवाल पूछें",
            "शुरुआत में ही कुछ मूवमेंट या इशारा करें",
            "शुरुआती लाइन 6 शब्दों से छोटी रखें"
        ),
        quizQuestionEn = "What happens if your Reel lacks a strong hook in the first 3 seconds?",
        quizQuestionHi = "अगर आपकी रील में शुरुआती 3 सेकंड में हुक नहीं है तो क्या होगा?",
        quizOptionsEn = listOf(
            "Viewers swipe away, dropping watch time and algorithm distribution",
            "Instagram gives you free views automatically",
            "The video plays 5x slower",
            "It automatically gets pinned to top"
        ),
        quizOptionsHi = listOf(
            "व्यूअर्स स्वाइप कर देंगे, जिससे वॉच टाइम और रीच गिर जाएगी",
            "इंस्टाग्राम फ्री व्यूज देगा",
            "वीडियो अपने आप रुक जाएगी",
            "वीडियो पिन हो जाएगी"
        ),
        quizCorrectIndex = 0,
        quizExplanationEn = "Watch retention in the first 3 seconds is the #1 metric Instagram uses to decide whether to push a Reel to 10k+ people!",
        quizExplanationHi = "शुरुआती 3 सेकंड का वॉच टाइम ही इंस्टाग्राम का सबसे बड़ा सिग्नल है जो आपकी रील को वायरल करता है!",
        summaryEn = "Every viral Reel wins or loses in the first 3 seconds. Hook the viewer visually and verbally!",
        summaryHi = "आपकी रील वायरल होगी या नहीं, यह शुरुआती 3 सेकंड में ही तय हो जाता है।"
    ),
    InstagramLessonData(
        id = 5,
        icon = Icons.Default.VolumeUp,
        badge = "AUDIO & SOUND",
        titleEn = "Trending Audio & Voice Balancing",
        titleHi = "ट्रेंडिंग ऑडियो और वॉइस मिक्सिंग",
        subtitleEn = "Leverage viral audio arrows with balanced crisp voiceovers",
        subtitleHi = "वायरल ट्रेंडिंग ऑडियो एरो और साफ़ वॉइस का परफेक्ट बैलेंस",
        conceptEn = "Reels using audio with a small ↗️ arrow get algorithm priority because Instagram boosts trending audio clips. However, when doing a voiceover, background music must be turned down to 10-15% while keeping your spoken voice at 100% so viewers can hear every word clearly.",
        conceptHi = "जिस ऑडियो के साथ छोटा ↗️ तीर (Trending Arrow) होता है, इंस्टाग्राम उसे ज़्यादा प्रमोट करता है। वॉइसओवर रील बनाते समय बैकग्राउंड म्यूजिक की आवाज़ 10-15% रखें और अपनी आवाज़ 100% पर रखें।",
        exampleEn = "In Audio Controls: Original Audio (Voiceover) = 100%, Added Trending Audio = 12%.",
        exampleHi = "ऑडियो कंट्रोल्स में: ओरिजिनल वॉइस = 100%, ट्रेंडिंग बैकग्राउंड म्यूजिक = 12%.",
        checklistEn = listOf(
            "Look for the small ↗️ trending audio arrow icon",
            "Record spoken voiceover in a quiet, echoes-free room",
            "Set Voiceover volume to 100% and Background Music to 10-15%",
            "Preview audio mix with headphones before publishing"
        ),
        checklistHi = listOf(
            "ऑडियो नाम के पास छोटा ↗️ ट्रेंडिंग तीर देखें",
            "शांत जगह पर क्लियर आवाज रिकॉर्ड करें",
            "वॉइसओवर 100% और बैकग्राउंड म्यूजिक 10-15% रखें",
            "पोस्ट करने से पहले हेडफोन लगाकर आवाज़ चेक करें"
        ),
        quizQuestionEn = "What is the recommended volume balance for background trending audio during a voiceover Reel?",
        quizQuestionHi = "वॉइसओवर वाली रील में बैकग्राउंड ट्रेंडिंग म्यूजिक की आवाज़ कितनी होनी चाहिए?",
        quizOptionsEn = listOf(
            "10% to 15%",
            "100%",
            "75%",
            "0% (Completely muted)"
        ),
        quizOptionsHi = listOf(
            "10% से 15%",
            "100%",
            "75%",
            "0% (बिलकुल बंद)"
        ),
        quizCorrectIndex = 0,
        quizExplanationEn = "10-15% volume allows the trending audio algorithm tag to trigger while keeping your voiceover 100% clear!",
        quizExplanationHi = "10-15% म्यूजिक से ट्रेंडिंग ऑडियो का टैग भी लग जाता है और आपकी आवाज़ भी बिलकुल साफ सुनाई देती है!",
        summaryEn = "Combine trending audio tags with crystal clear voiceovers for maximum discoverability.",
        summaryHi = "ट्रेंडिंग ऑडियो टैग का इस्तेमाल करें और अपनी आवाज को हमेशा साफ रखें।"
    ),
    InstagramLessonData(
        id = 6,
        icon = Icons.Default.School,
        badge = "SCRIPT MASTERCLASS",
        titleEn = "AI Scripting & High-Retention Reel Flow",
        titleHi = "AI स्क्रिप्ट और रील स्ट्रक्चर",
        subtitleEn = "Build 30-second viral scripts with high watch completion rates",
        subtitleHi = "30-सेकंड की हाई-रिटेंशन वायरल रील स्क्रिप्ट बनाना सीखें",
        conceptEn = "High retention requires a proven 3-part script structure: 1) Visual/Verbal Hook (0-3s), 2) Value Delivery / Story (3-25s), and 3) Targeted Call to Action (25-30s). Keep sentence structure short and change on-screen visuals every 2-3 seconds using jump cuts.",
        conceptHi = "वीडियो को पूरा दिखाने के लिए 3-स्टेप स्क्रिप्ट का उपयोग करें: 1) हुक (0-3 सेकंड), 2) मुख्य जानकारी (3-25 सेकंड), और 3) कॉल टू एक्शन (25-30 सेकंड)। हर 2-3 सेकंड में टेक्स्ट या कट बदलें।",
        exampleEn = "Part 1: 'Here is how to get 10k views in 7 days.' | Part 2: 'Step 1: Fix Bio, Step 2: Enable HD Uploads.' | Part 3: 'Comment GUIDE for the full PDF!'",
        exampleHi = "पार्ट 1: '7 दिनों में 10k व्यूज पाने का सीक्रेट।' | पार्ट 2: 'स्टेप 1: बायो सही करो, स्टेप 2: HD ऑन करो।' | पार्ट 3: 'फ्री गाइड के लिए नीचे COMMENT करें!'",
        checklistEn = listOf(
            "Follow the Hook + Core Value + Call to Action script framework",
            "Use fast jump cuts or visual text changes every 2-3 seconds",
            "Add high-contrast auto-caption subtitles on screen",
            "End with a clear Call To Action encouraging comments or saves"
        ),
        checklistHi = listOf(
            "हुक + मुख्य जानकारी + CTA ढांचा फॉलो करें",
            "हर 2-3 सेकंड में कट या टेक्स्ट बदलें",
            "स्क्रीन पर कैप्शन सबटाइटल जोड़ें",
            "अंत में स्पष्ट कमेंट या फॉलो का CTA दें"
        ),
        quizQuestionEn = "What are the 3 mandatory components of a high-retention viral Reel script?",
        quizQuestionHi = "वायरल रील स्क्रिप्ट के 3 सबसे महत्वपूर्ण भाग कौन से हैं?",
        quizOptionsEn = listOf(
            "Hook (0-3s) + Core Value (3-25s) + Call to Action (25-30s)",
            "Intro + Credits + Music",
            "Silent video + Logos + Background Song",
            "Random clips without voice"
        ),
        quizOptionsHi = listOf(
            "हुक (0-3s) + मुख्य जानकारी (3-25s) + कॉल टू एक्शन (25-30s)",
            "इंट्रो + क्रेडिट्स + म्यूजिक",
            "बिना आवाज के रैंडम वीडियो",
            "सिर्फ गानों के क्लिप्स"
        ),
        quizCorrectIndex = 0,
        quizExplanationEn = "Structuring your Reel with Hook, Value, and CTA ensures viewers watch to the end and take action!",
        quizExplanationHi = "इस 3-स्टेप स्ट्रक्चर से लोग वीडियो पूरा देखते हैं और कमेंट या फॉलो भी करते हैं!",
        summaryEn = "Always write your script before filming. High retention = high algorithm distribution!",
        summaryHi = "शूटिंग से पहले स्क्रिप्ट तैयार करें। पूरा वीडियो देखे जाने पर इंस्टाग्राम उसे लाखों लोगों तक पहुँचाता है।"
    ),
    InstagramLessonData(
        id = 7,
        icon = Icons.Default.Star,
        badge = "SEO & HASHTAGS",
        titleEn = "SEO Captions & 3-Tier Hashtag Strategy",
        titleHi = "SEO कैप्शन और 3-टियर हैशटैग स्ट्रेटजी",
        subtitleEn = "Rank on Instagram Search and Explore pages effortlessly",
        subtitleHi = "इंस्टाग्राम सर्च और एक्सप्लोर पेज पर टॉप रैंक करें",
        conceptEn = "Instagram is now a keyword-based search engine. Include your primary niche keywords in the first sentence of your caption. Combine this with the 3-Tier Hashtag Mix: 3 Niche Specific tags (10k-50k posts), 3 Medium Competition tags (100k-500k posts), and 2 High Reach tags (1M+ posts).",
        conceptHi = "इंस्टाग्राम अब सर्च इंजन की तरह काम करता है। अपने टॉपिक का मुख्य कीवर्ड कैप्शन की पहली लाइन में लिखें। इसके साथ 3-टियर हैशटैग मिक्स यूज़ करें: 3 नीच हैशटैग, 3 मीडियम हैशटैग और 2 बड़े हैशटैग।",
        exampleEn = "Caption Line 1: 'Here is the best Instagram Reel growth strategy for creators in 2026.' | Tags: #TechCreator #ReelTips #InstagramGrowth #ViralReels",
        exampleHi = "कैप्शन लाइन 1: '2026 में इंस्टाग्राम रील्स वायरल करने का सबसे आसान तरीका।' | टैग्स: #TechCreator #ReelTips #InstagramGrowth",
        checklistEn = listOf(
            "Include primary topic keyword in sentence 1 of caption",
            "Use 3 Niche + 3 Medium + 2 Broad hashtag mix (8-10 total tags)",
            "Add precise location tag for local regional reach boost",
            "Include ALT text description in Advanced Settings"
        ),
        checklistHi = listOf(
            "कैप्शन की पहली लाइन में मुख्य कीवर्ड डालें",
            "3 नीच + 3 मीडियम + 2 बड़े हैशटैग का मिक्स बनाएं",
            "लोकेशन टैग लगाएं",
            "एडवांस्ड सेटिंग्स में Alt Text जोड़ें"
        ),
        quizQuestionEn = "How does the 3-Tier Hashtag strategy maximize Reel reach?",
        quizQuestionHi = "3-टियर हैशटैग मिक्स आपकी रील की रीच कैसे बढ़ाता है?",
        quizOptionsEn = listOf(
            "Combines Niche Specific, Medium Competition, and High Reach tags for step-by-step ranking",
            "Spams 100 identical tags",
            "Uses celebrity names",
            "Deletes all tags after posting"
        ),
        quizOptionsHi = listOf(
            "यह नीच, मीडियम और बड़े हैशटैग्स को मिलाकर स्टेप-बाय-स्टेप रैंकिंग देता है",
            "एक ही टैग 100 बार लगाता है",
            "फेमस स्टार्स का नाम लिखता है",
            "सारे टैग डिलीट कर देता है"
        ),
        quizCorrectIndex = 0,
        quizExplanationEn = "Niche tags get you early ranking, medium tags expand reach, and high reach tags trigger Explore page exposure!",
        quizExplanationHi = "छोटे हैशटैग्स से शुरुआत में रैंक मिलती है और फिर बड़े हैशटैग्स से वीडियो एक्सप्लोर पेज पर वायरल होती है!",
        summaryEn = "Write captions for humans first and search engine keywords second.",
        summaryHi = "कैप्शन ऐसा लिखें जो पढ़ने में आसान हो और जिसमें मुख्य कीवर्ड्स भी शामिल हों।"
    ),
    InstagramLessonData(
        id = 8,
        icon = Icons.Default.EmojiEvents,
        badge = "ALGORITHM ENGINE",
        titleEn = "Pre-Upload Checklist & Engagement Boost",
        titleHi = "प्री-अपलोड चेकलिस्ट और 24h ग्रोथ बूस्ट",
        subtitleEn = "Execute the 9-point checklist and trigger the 1-hour engagement algorithm",
        subtitleHi = "9-पॉइंट चेकलिस्ट और 1-घंटे का पावरफुल कमेंट रिप्लाई सीक्रेट",
        conceptEn = "Before clicking Share, run through a 9-point pre-publish checklist: HD cover thumbnail, 1080p toggle, SEO caption, 3-tier tags, audio balance, and location tag. Once published, reply to all comments within the first 60 minutes and pin top engaging comments to trigger algorithm distribution.",
        conceptHi = "अपलोड बटन दबाने से पहले 9-पॉइंट चेकलिस्ट चेक करें: HD थंबनेल, 1080p ऑन, SEO कैप्शन, हैशटैग्स और लोकेशन। पोस्ट करने के पहले 1 घंटे के भीतर आए सभी कमेंट्स का रिप्लाई करें और बेस्ट कमेंट पिन करें।",
        exampleEn = "Publish -> Share to Story with 'New Reel' sticker -> Reply to first 10 comments in 15 mins -> Pin best comment.",
        exampleHi = "पोस्ट करें -> स्टोरी पर शेयर करें -> पहले 15 मिनट में आए कमेंट्स का रिप्लाई करें -> बेस्ट कमेंट को PIN करें।",
        checklistEn = listOf(
            "Selected a clear high-CTR cover frame or custom thumbnail",
            "Verified 'Upload at Highest Quality' is turned ON in settings",
            "SEO caption & 3-tier hashtags attached",
            "Replied to early comments within 1 hour of publishing"
        ),
        checklistHi = listOf(
            "साफ और आकर्षक कवर थंबनेल चुना",
            "'हाईएस्ट क्वालिटी' ऑन होना कन्फर्म किया",
            "SEO कैप्शन और हैशटैग्स जोड़े",
            "1 घंटे के अंदर कमेंट्स का रिप्लाई किया"
        ),
        quizQuestionEn = "Why is replying to comments within the first 60 minutes after posting crucial?",
        quizQuestionHi = "पोस्ट करने के पहले 60 मिनट के भीतर कमेंट्स का रिप्लाई करना क्यों बहुत ज़रूरी है?",
        quizOptionsEn = listOf(
            "It signals strong early engagement velocity to the Instagram algorithm to boost reach",
            "It gives you a free verified blue badge",
            "It hides the Reel from non-followers",
            "It lowers video resolution"
        ),
        quizOptionsHi = listOf(
            "यह इंस्टाग्राम एल्गोरिदम को शुरुआती सगाई का सिग्नल देता है जिससे रीच तेजी से बढ़ती है",
            "यह फ्री ब्लू टिक देता है",
            "यह वीडियो छुपा देता है",
            "यह क्वालिटी कम करता है"
        ),
        quizCorrectIndex = 0,
        quizExplanationEn = "Early engagement velocity in the first 60 minutes is the key indicator Instagram uses to push Reels to new non-follower audiences!",
        quizExplanationHi = "पहले 60 मिनट की सगाई (Engagement) से इंस्टाग्राम को सिग्नल मिलता है कि कंटेंट लोगों को पसंद आ रहा है!",
        summaryEn = "Never post and ghost. Execute the 1-hour engagement strategy to maximize viral velocity.",
        summaryHi = "पोस्ट करके कभी गायब न हों। पहले 1 घंटे में एक्टिव रहकर रील की वायरल स्पीड बढ़ाएं!"
    )
)

// =========================================================
// MAIN ENTRY POINT DIALOG
// =========================================================
@Composable
fun InstagramCreatorAiV2Dialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Preferences & State
    var isIntroCompleted by remember {
        mutableStateOf(CreatorAcademyPrefs.isInstagramIntroCompleted(context))
    }
    var currentLang by remember {
        mutableStateOf(CreatorAcademyPrefs.getInstagramLanguage(context).ifBlank { "EN" })
    }
    var completedLessons by remember {
        mutableStateOf(CreatorAcademyPrefs.getInstagramCompletedSteps(context).toSet())
    }
    var activeLessonId by remember { mutableStateOf<Int?>(null) }
    var showResetConfirm by remember { mutableStateOf(false) }

    // Helper: Save Lesson Completion
    fun markLessonCompleted(lessonId: Int) {
        val updated = completedLessons + lessonId
        completedLessons = updated
        CreatorAcademyPrefs.saveInstagramCompletedSteps(context, updated)
        CreatorAcademyPrefs.saveInstagramCurrentStep(context, (lessonId).coerceAtMost(INSTAGRAM_LESSONS.size))
    }

    // Helper: Reset Course
    fun handleResetCourse() {
        CreatorAcademyPrefs.resetCourseProgress(context, "instagram")
        completedLessons = emptySet()
        isIntroCompleted = false
        activeLessonId = null
        showResetConfirm = false
        Toast.makeText(context, if (currentLang == "HI") "कोर्स रीसेट हो गया है!" else "Course progress reset!", Toast.LENGTH_SHORT).show()
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
                .background(DarkCanvasBg)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            when {
                !isIntroCompleted -> {
                    // FIRST TIME INTRO SCREEN
                    InstagramFirstTimeIntroScreen(
                        currentLang = currentLang,
                        onLanguageChange = { lang ->
                            currentLang = lang
                            CreatorAcademyPrefs.saveInstagramLanguage(context, lang)
                        },
                        onStartCourse = {
                            isIntroCompleted = true
                            CreatorAcademyPrefs.setInstagramIntroCompleted(context, true)
                        },
                        onClose = onDismiss
                    )
                }
                else -> {
                    // MAIN COURSE DASHBOARD
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkCanvasBg)
                    ) {
                        // TOP TOOLBAR
                        InstagramTopBar(
                            currentLang = currentLang,
                            onLanguageToggle = {
                                val nextLang = if (currentLang == "EN") "HI" else "EN"
                                currentLang = nextLang
                                CreatorAcademyPrefs.saveInstagramLanguage(context, nextLang)
                            },
                            onClose = onDismiss
                        )

                        // DASHBOARD CONTENT
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(bottom = 24.dp)
                        ) {
                            // HERO BANNER & PROGRESS
                            item {
                                CourseProgressHeaderCard(
                                    completedCount = completedLessons.size,
                                    totalCount = INSTAGRAM_LESSONS.size,
                                    currentLang = currentLang,
                                    onResetCourse = { showResetConfirm = true },
                                    onReplayIntro = { isIntroCompleted = false }
                                )
                            }

                            // LESSONS SECTION HEADER
                            item {
                                SectionTitleHeader(
                                    title = if (currentLang == "HI") "रील्स ग्रोथ रोडमैप (8 पाठ)" else "Reels Growth Roadmap (8 Lessons)",
                                    subtitle = if (currentLang == "HI") "स्टेप-बाय-स्टेप सीखें और क्विज़ पास करें" else "Learn step-by-step and clear the quick quiz"
                                )
                            }

                            // LESSON CARDS
                            itemsIndexed(INSTAGRAM_LESSONS) { idx, lesson ->
                                val isCompleted = completedLessons.contains(lesson.id)
                                LessonListItemCard(
                                    lesson = lesson,
                                    index = idx,
                                    isCompleted = isCompleted,
                                    currentLang = currentLang,
                                    onClick = { activeLessonId = lesson.id }
                                )
                            }

                            // COURSE COMPLETION CERTIFICATE
                            if (completedLessons.size >= INSTAGRAM_LESSONS.size) {
                                item {
                                    CourseCompletionCertificateCard(
                                        currentLang = currentLang,
                                        onResetCourse = { showResetConfirm = true }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // FULLSCREEN LESSON POPUP DIALOG
            activeLessonId?.let { lessonId ->
                val lessonObj = INSTAGRAM_LESSONS.find { it.id == lessonId }
                if (lessonObj != null) {
                    FullscreenLessonPopup(
                        lesson = lessonObj,
                        totalLessons = INSTAGRAM_LESSONS.size,
                        isCompleted = completedLessons.contains(lessonId),
                        currentLang = currentLang,
                        onLanguageToggle = {
                            val nextLang = if (currentLang == "EN") "HI" else "EN"
                            currentLang = nextLang
                            CreatorAcademyPrefs.saveInstagramLanguage(context, nextLang)
                        },
                        onLessonCompleted = {
                            markLessonCompleted(lessonId)
                        },
                        onNextLesson = {
                            if (lessonId < INSTAGRAM_LESSONS.size) {
                                activeLessonId = lessonId + 1
                            } else {
                                activeLessonId = null
                            }
                        },
                        onPrevLesson = {
                            if (lessonId > 1) {
                                activeLessonId = lessonId - 1
                            }
                        },
                        onDismiss = { activeLessonId = null }
                    )
                }
            }

            // RESET CONFIRMATION DIALOG
            if (showResetConfirm) {
                ResetConfirmModal(
                    currentLang = currentLang,
                    onConfirm = { handleResetCourse() },
                    onDismiss = { showResetConfirm = false }
                )
            }
        }
    }
}

// =========================================================
// TOP TOOLBAR COMPONENT
// =========================================================
@Composable
private fun InstagramTopBar(
    currentLang: String,
    onLanguageToggle: () -> Unit,
    onClose: () -> Unit
) {
    Surface(
        color = CardSurfaceBg,
        border = BorderStroke(1.dp, CardBorderColor)
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
                        .background(Brush.radialGradient(listOf(VioletGlow, VioletDeep)))
                        .border(BorderStroke(1.dp, MagentaAccent), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    OfficialLogo(name = "instagram", modifier = Modifier.size(20.dp))
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Instagram Creator Guide",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(VioletPrimary.copy(alpha = 0.3f))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "ViralToolAi",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = VioletGlow
                            )
                        }
                    }
                    Text(
                        text = if (currentLang == "HI") "100% ओरिजिनल मास्टरक्लास" else "100% Original Creator Masterclass",
                        fontSize = 10.5.sp,
                        color = TextMuted
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Dynamic Language Toggle Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.horizontalGradient(listOf(VioletDeep, VioletGlow)))
                        .border(BorderStroke(1.dp, MagentaAccent), RoundedCornerShape(12.dp))
                        .clickable { onLanguageToggle() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = TextWhite,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (currentLang == "EN") "🇬🇧 EN" else "🇮🇳 हिन्दी",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
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

// =========================================================
// FIRST TIME INTRO SCREEN
// =========================================================
@Composable
private fun InstagramFirstTimeIntroScreen(
    currentLang: String,
    onLanguageChange: (String) -> Unit,
    onStartCourse: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(
                    BorderStroke(
                        1.5.dp,
                        Brush.horizontalGradient(listOf(VioletPrimary, VioletGlow, MagentaAccent))
                    ),
                    RoundedCornerShape(24.dp)
                )
                .shadow(20.dp, RoundedCornerShape(24.dp), spotColor = VioletGlow),
            color = CardSurfaceBg
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Action Row (Close + Language)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Language Switch Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x33A855F7))
                            .border(BorderStroke(1.dp, VioletGlow), RoundedCornerShape(12.dp))
                            .clickable { onLanguageChange(if (currentLang == "EN") "HI" else "EN") }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = VioletGlow, modifier = Modifier.size(14.dp))
                            Text(
                                text = if (currentLang == "EN") "English" else "हिन्दी",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color(0x22FFFFFF))
                            .clickable { onClose() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hero Visual Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(BorderStroke(1.dp, VioletGlow.copy(alpha = 0.5f)), RoundedCornerShape(18.dp))
                ) {
                    ToolHeroBanner(
                        toolType = ToolHeroType.INSTAGRAM_CREATOR,
                        height = 130.dp,
                        badgeText = null,
                        subtitleText = null
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = if (currentLang == "HI") "इंस्टाग्राम क्रिएटर गाइड" else "Instagram Creator Guide",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (currentLang == "HI") "जीरो से हीरो रील्स ग्रोथ मास्टरक्लास" else "Zero to Hero Creator Growth Masterclass",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletGlow,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Feature Highlights
                val features = if (currentLang == "HI") listOf(
                    "📸 प्रोफाइल & बायो ऑप्टिमाइजेशन",
                    "⚡ 1080p HD रेंडरिंग & 3-सेकंड वायरल हुक्स",
                    "🎵 ↗️ ट्रेंडिंग ऑडियो मिक्सिंग तकनीक",
                    "🎯 SEO कैप्शन, 3-टियर हैशटैग्स & 1h एल्गो बूस्ट"
                ) else listOf(
                    "📸 Profile & Bio Optimization Framework",
                    "⚡ 1080p HD Rendering & 3-Sec Viral Hooks",
                    "🎵 ↗️ Trending Audio Mixing Secret",
                    "🎯 SEO Captions, 3-Tier Tags & 1h Algo Boost"
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    features.forEach { ft ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x1FA855F7))
                                .border(BorderStroke(0.8.dp, CardBorderColor), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 9.dp)
                        ) {
                            Text(
                                text = ft,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Start Course CTA Button
                Button(
                    onClick = onStartCourse,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(10.dp, RoundedCornerShape(14.dp), spotColor = VioletGlow),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(VioletDeep, VioletGlow, MagentaAccent)
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = TextWhite, modifier = Modifier.size(20.dp))
                            Text(
                                text = if (currentLang == "HI") "कोर्स शुरू करें (8 पाठ)" else "Start Instagram Journey (8 Lessons)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// COURSE PROGRESS HEADER CARD
// =========================================================
@Composable
private fun CourseProgressHeaderCard(
    completedCount: Int,
    totalCount: Int,
    currentLang: String,
    onResetCourse: () -> Unit,
    onReplayIntro: () -> Unit
) {
    val progressPercent = if (totalCount > 0) (completedCount * 100) / totalCount else 0
    val animatedProgress by animateFloatAsState(
        targetValue = progressPercent / 100f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "progressAnim"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(BorderStroke(1.2.dp, Brush.horizontalGradient(listOf(VioletPrimary, MagentaAccent))), RoundedCornerShape(20.dp)),
        color = CardSurfaceBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (currentLang == "HI") "आपकी प्रगति" else "Course Progress",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$completedCount / $totalCount ${if (currentLang == "HI") "पाठ पूर्ण" else "Lessons Completed"}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                }

                // Progress Badge Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (progressPercent == 100) SuccessGreen.copy(alpha = 0.2f)
                            else VioletPrimary.copy(alpha = 0.2f)
                        )
                        .border(
                            BorderStroke(
                                1.dp,
                                if (progressPercent == 100) SuccessGreen else VioletGlow
                            ),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = when {
                            progressPercent == 100 -> if (currentLang == "HI") "पूर्ण 🎉" else "Completed 🎉"
                            progressPercent > 0 -> "$progressPercent% ${if (currentLang == "HI") "पूर्ण" else "Done"}"
                            else -> if (currentLang == "HI") "शुरू नहीं हुआ" else "Not Started"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = if (progressPercent == 100) SuccessGreen else VioletGlow
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress)
                        .clip(CircleShape)
                        .background(Brush.horizontalGradient(listOf(VioletPrimary, VioletGlow, MagentaAccent)))
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons Row (Replay Intro & Reset)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clickable { onReplayIntro() }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.School, contentDescription = null, tint = VioletGlow, modifier = Modifier.size(14.dp))
                    Text(
                        text = if (currentLang == "HI") "इंट्रो देखें" else "Replay Intro",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = VioletGlow
                    )
                }

                if (completedCount > 0) {
                    Row(
                        modifier = Modifier
                            .clickable { onResetCourse() }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                        Text(
                            text = if (currentLang == "HI") "रीसेट करें" else "Reset Progress",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

// =========================================================
// SECTION TITLE HEADER
// =========================================================
@Composable
private fun SectionTitleHeader(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite
        )
        Text(
            text = subtitle,
            fontSize = 11.5.sp,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}

// =========================================================
// LESSON LIST ITEM CARD
// =========================================================
@Composable
private fun LessonListItemCard(
    lesson: InstagramLessonData,
    index: Int,
    isCompleted: Boolean,
    currentLang: String,
    onClick: () -> Unit
) {
    val title = if (currentLang == "HI") lesson.titleHi else lesson.titleEn
    val subtitle = if (currentLang == "HI") lesson.subtitleHi else lesson.subtitleEn

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                BorderStroke(
                    1.dp,
                    if (isCompleted) SuccessGreen.copy(alpha = 0.5f) else CardBorderColor
                ),
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        color = CardSurfaceBg
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon Badge Box
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = if (isCompleted) SolidColor(SuccessGreen.copy(alpha = 0.2f))
                        else Brush.radialGradient(listOf(VioletDeep, CardSurfaceBg))
                    )
                    .border(
                        BorderStroke(
                            1.dp,
                            if (isCompleted) SuccessGreen else VioletPrimary
                        ),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Done", tint = SuccessGreen, modifier = Modifier.size(24.dp))
                } else {
                    Icon(lesson.icon, contentDescription = null, tint = VioletGlow, modifier = Modifier.size(22.dp))
                }
            }

            // Title & Subtitle Info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "LESSON 0${lesson.id}",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Black,
                        color = VioletGlow,
                        letterSpacing = 0.8.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0x22FFFFFF))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = lesson.badge,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Arrow button
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCompleted) SuccessGreen.copy(alpha = 0.15f)
                        else Color(0x1FFFFFFF)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Open",
                    tint = if (isCompleted) SuccessGreen else TextWhite,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// =========================================================
// FULLSCREEN LESSON POPUP DIALOG
// =========================================================
@Composable
private fun FullscreenLessonPopup(
    lesson: InstagramLessonData,
    totalLessons: Int,
    isCompleted: Boolean,
    currentLang: String,
    onLanguageToggle: () -> Unit,
    onLessonCompleted: () -> Unit,
    onNextLesson: () -> Unit,
    onPrevLesson: () -> Unit,
    onDismiss: () -> Unit
) {
    val title = if (currentLang == "HI") lesson.titleHi else lesson.titleEn
    val concept = if (currentLang == "HI") lesson.conceptHi else lesson.conceptEn
    val example = if (currentLang == "HI") lesson.exampleHi else lesson.exampleEn
    val checklist = if (currentLang == "HI") lesson.checklistHi else lesson.checklistEn
    val quizQuestion = if (currentLang == "HI") lesson.quizQuestionHi else lesson.quizQuestionEn
    val quizOptions = if (currentLang == "HI") lesson.quizOptionsHi else lesson.quizOptionsEn
    val quizExplanation = if (currentLang == "HI") lesson.quizExplanationHi else lesson.quizExplanationEn
    val summary = if (currentLang == "HI") lesson.summaryHi else lesson.summaryEn

    // Quiz State
    var selectedQuizOption by remember(lesson.id) { mutableStateOf<Int?>(null) }
    var quizSubmitted by remember(lesson.id) { mutableStateOf(false) }
    var isQuizCorrect by remember(lesson.id) { mutableStateOf(false) }

    // Interactive Checklist Checks State
    val checkedState = remember(lesson.id) {
        mutableStateListOf(*Array(checklist.size) { false })
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
                .background(DarkCanvasBg)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // POPUP HEADER BAR
                Surface(
                    color = CardSurfaceBg,
                    border = BorderStroke(1.dp, CardBorderColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "LESSON ${lesson.id} OF $totalLessons",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = VioletGlow,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Language Switcher
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x33A855F7))
                                    .border(BorderStroke(1.dp, VioletGlow), RoundedCornerShape(12.dp))
                                    .clickable { onLanguageToggle() }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = if (currentLang == "EN") "🇬🇧 EN" else "🇮🇳 हिन्दी",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }

                            // Close Button
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x22FFFFFF))
                                    .clickable { onDismiss() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                // LESSON BODY SCROLLABLE CONTENT
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // CONCEPT EXPLANATION CARD
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .border(BorderStroke(1.dp, CardBorderColor), RoundedCornerShape(18.dp)),
                            color = CardSurfaceBg
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(lesson.icon, contentDescription = null, tint = VioletGlow, modifier = Modifier.size(20.dp))
                                    Text(
                                        text = if (currentLang == "HI") "मुख्य अवधारणा (Key Concept)" else "Key Concept & Strategy",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        color = VioletGlow
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = concept,
                                    fontSize = 13.5.sp,
                                    color = TextWhite,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }

                    // REAL EXAMPLE CARD
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .border(BorderStroke(1.dp, Color(0x3310B981)), RoundedCornerShape(18.dp)),
                            color = Color(0xFF091712)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(18.dp))
                                    Text(
                                        text = if (currentLang == "HI") "वास्तविक उदाहरण (Real-World Example)" else "Real-World Example",
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Black,
                                        color = SuccessGreen
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = example,
                                    fontSize = 13.sp,
                                    color = TextWhite.copy(alpha = 0.95f),
                                    lineHeight = 19.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // PRACTICAL CHECKLIST CARD
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .border(BorderStroke(1.dp, CardBorderColor), RoundedCornerShape(18.dp)),
                            color = CardSurfaceBg
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (currentLang == "HI") "📋 प्रैक्टिकल चेकलिस्ट" else "📋 Practical Checklist",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                checklist.forEachIndexed { idx, chk ->
                                    val isChecked = checkedState.getOrElse(idx) { false }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                if (idx < checkedState.size) {
                                                    checkedState[idx] = !isChecked
                                                }
                                            }
                                            .padding(vertical = 5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Checkbox(
                                            checked = isChecked,
                                            onCheckedChange = { checkedState[idx] = it },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = VioletGlow,
                                                checkmarkColor = Color.White,
                                                uncheckedColor = TextMuted
                                            )
                                        )
                                        Text(
                                            text = chk,
                                            fontSize = 12.5.sp,
                                            color = if (isChecked) SuccessGreen else TextWhite,
                                            fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // INTERACTIVE QUIZ CARD
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        when {
                                            quizSubmitted && isQuizCorrect -> SuccessGreen
                                            quizSubmitted && !isQuizCorrect -> ErrorRed
                                            else -> MagentaAccent.copy(alpha = 0.5f)
                                        }
                                    ),
                                    RoundedCornerShape(18.dp)
                                ),
                            color = CardSurfaceBg
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.HelpOutline, contentDescription = null, tint = MagentaAccent, modifier = Modifier.size(20.dp))
                                    Text(
                                        text = if (currentLang == "HI") "⚡ त्वरित क्विज़ (Quick Quiz)" else "⚡ Quick Knowledge Check",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        color = MagentaAccent
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = quizQuestion,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Quiz Options
                                quizOptions.forEachIndexed { optIdx, optText ->
                                    val isSelected = selectedQuizOption == optIdx
                                    val isCorrectOpt = optIdx == lesson.quizCorrectIndex

                                    val optionBg = when {
                                        quizSubmitted && isCorrectOpt -> SuccessGreen.copy(alpha = 0.25f)
                                        quizSubmitted && isSelected && !isCorrectOpt -> ErrorRed.copy(alpha = 0.25f)
                                        isSelected -> VioletPrimary.copy(alpha = 0.25f)
                                        else -> Color(0x15FFFFFF)
                                    }

                                    val optionBorder = when {
                                        quizSubmitted && isCorrectOpt -> SuccessGreen
                                        quizSubmitted && isSelected && !isCorrectOpt -> ErrorRed
                                        isSelected -> VioletGlow
                                        else -> CardBorderColor
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(optionBg)
                                            .border(BorderStroke(1.dp, optionBorder), RoundedCornerShape(12.dp))
                                            .clickable(!quizSubmitted) {
                                                selectedQuizOption = optIdx
                                                quizSubmitted = true
                                                isQuizCorrect = (optIdx == lesson.quizCorrectIndex)
                                                if (optIdx == lesson.quizCorrectIndex) {
                                                    onLessonCompleted()
                                                }
                                            }
                                            .padding(horizontal = 12.dp, vertical = 10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${('A' + optIdx)}.  $optText",
                                                fontSize = 12.5.sp,
                                                fontWeight = if (isSelected || (quizSubmitted && isCorrectOpt)) FontWeight.Bold else FontWeight.Medium,
                                                color = TextWhite,
                                                modifier = Modifier.weight(1f)
                                            )

                                            if (quizSubmitted && isCorrectOpt) {
                                                Icon(Icons.Default.Check, contentDescription = "Correct", tint = SuccessGreen, modifier = Modifier.size(16.dp))
                                            } else if (quizSubmitted && isSelected && !isCorrectOpt) {
                                                Icon(Icons.Default.Close, contentDescription = "Wrong", tint = ErrorRed, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                // Quiz Feedback Message
                                if (quizSubmitted) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp)),
                                        color = if (isQuizCorrect) SuccessGreen.copy(alpha = 0.15f) else ErrorRed.copy(alpha = 0.15f)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(
                                                text = if (isQuizCorrect) {
                                                    if (currentLang == "HI") "✅ बिल्कुल सही उत्तर! पाठ पूर्ण हुआ!" else "✅ Correct Answer! Lesson Completed!"
                                                } else {
                                                    if (currentLang == "HI") "❌ गलत उत्तर। दोबारा प्रयास करें।" else "❌ Incorrect. Try again!"
                                                },
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Black,
                                                color = if (isQuizCorrect) SuccessGreen else ErrorRed
                                            )
                                            Text(
                                                text = quizExplanation,
                                                fontSize = 11.5.sp,
                                                color = TextWhite.copy(alpha = 0.9f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // LESSON SUMMARY CARD
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .border(BorderStroke(1.dp, CardBorderColor), RoundedCornerShape(18.dp)),
                            color = CardSurfaceBg
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (currentLang == "HI") "📌 मुख्य निष्कर्ष (Takeaway)" else "📌 Main Takeaway",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = summary,
                                    fontSize = 12.5.sp,
                                    color = TextMuted,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                // BOTTOM ACTION BAR (PREV / NEXT / SAVE)
                Surface(
                    color = CardSurfaceBg,
                    border = BorderStroke(1.dp, CardBorderColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Previous Button
                        if (lesson.id > 1) {
                            OutlinedButton(
                                onClick = onPrevLesson,
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, CardBorderColor)
                            ) {
                                Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                                Text(if (currentLang == "HI") "पिछला" else "Previous", fontSize = 12.sp, color = TextWhite)
                            }
                        } else {
                            Spacer(modifier = Modifier.width(1.dp))
                        }

                        // Complete & Next Button
                        Button(
                            onClick = {
                                onLessonCompleted()
                                if (lesson.id < totalLessons) {
                                    onNextLesson()
                                } else {
                                    onDismiss()
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = when {
                                        lesson.id < totalLessons -> if (currentLang == "HI") "अगला पाठ 🚀" else "Next Lesson 🚀"
                                        else -> if (currentLang == "HI") "कोर्स समाप्त करें 🎉" else "Finish Course 🎉"
                                    },
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// COURSE COMPLETION CERTIFICATE CARD
// =========================================================
@Composable
private fun CourseCompletionCertificateCard(
    currentLang: String,
    onResetCourse: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(VioletGlow, MagentaAccent, SuccessGreen))), RoundedCornerShape(20.dp)),
        color = CardSurfaceBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(SuccessGreen.copy(alpha = 0.2f))
                    .border(BorderStroke(1.dp, SuccessGreen), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(30.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (currentLang == "HI") "🎉 बधाई हो! आपने कोर्स पूरा कर लिया!" else "🎉 Congratulations! Course Mastered!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (currentLang == "HI")
                    "आपने इंस्टाग्राम रील्स ग्रोथ, 1080p HD अपलोड, 3s वायरल हुक्स और SEO की सभी रणनीतियाँ मास्टर कर ली हैं।"
                else
                    "You have successfully mastered Profile Optimization, 1080p HD Uploads, 3s Viral Hooks, Trending Audio, and SEO Captions.",
                fontSize = 12.5.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onResetCourse,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, CardBorderColor)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (currentLang == "HI") "पुनः अभ्यास करने के लिए रीसेट करें" else "Restart Course for Practice",
                    fontSize = 12.sp,
                    color = TextWhite
                )
            }
        }
    }
}

// =========================================================
// RESET CONFIRMATION MODAL
// =========================================================
@Composable
private fun ResetConfirmModal(
    currentLang: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (currentLang == "HI") "कोर्स प्रगति रीसेट करें?" else "Reset Course Progress?",
                fontWeight = FontWeight.Black,
                color = TextWhite
            )
        },
        text = {
            Text(
                text = if (currentLang == "HI") "क्या आप वाकई अपनी सभी पाठ प्रगतियों को हटाना चाहते हैं?" else "Are you sure you want to reset all completed lesson steps?",
                color = TextMuted
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(if (currentLang == "HI") "हाँ, रीसेट करें" else "Yes, Reset", color = ErrorRed, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (currentLang == "HI") "रद्द करें" else "Cancel", color = TextWhite)
            }
        },
        containerColor = CardSurfaceBg,
        shape = RoundedCornerShape(20.dp)
    )
}
