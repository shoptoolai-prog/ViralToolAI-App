package com.example.creatoracademy

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.example.config.WishlinkAcademyConfig
import com.example.ui.components.MentorToolTheme
import com.example.ui.screens.OfficialLogo
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.TextWhite
import com.example.ui.theme.responsiveImeAndNavPadding
import kotlinx.coroutines.launch

private val WishlinkOrangePrimary = Color(0xFFFF5722)
private val WishlinkOrangeGlow = Color(0x33FF5722)
private val wishlinkTheme = MentorToolTheme.WishlinkCreator

data class WishlinkLessonItem(
    val moduleIndex: Int,
    val titleEnglish: String,
    val titleHindi: String,
    val titleHinglish: String,
    val subtitleEnglish: String,
    val subtitleHindi: String,
    val subtitleHinglish: String,
    val bulletPointsEnglish: List<String>,
    val bulletPointsHindi: List<String>,
    val bulletPointsHinglish: List<String>,
    val practicalTaskEnglish: String,
    val practicalTaskHindi: String,
    val practicalTaskHinglish: String,
    val actionButtonTextEnglish: String = "Open Wishlink App",
    val actionButtonTextHindi: String = "विशलिंक ऐप खोलें",
    val actionButtonTextHinglish: String = "Wishlink App Kholein",
    val actionUrl: String = WishlinkAcademyConfig.CREATOR_DASHBOARD_URL,
    val isPlayStoreAction: Boolean = false
)

object WishlinkStaticData {

    val introCards = listOf(
        IntroCardData(
            id = 1,
            title = "Welcome to Wishlink Creator Academy",
            subtitle = "Zero to Hero guide to earn affiliate commissions.",
            icon = Icons.Default.School,
            highlightTag = "CREATOR ACADEMY",
            bulletPoints = listOf("Master Wishlink from step 1", "Multi-language support", "Short visual lessons")
        ),
        IntroCardData(
            id = 2,
            title = "Earn on Every Recommendation",
            subtitle = "Turn Instagram clicks into income.",
            icon = Icons.Default.MonetizationOn,
            highlightTag = "COMMISSIONS",
            bulletPoints = listOf("Direct brand affiliate links", "Automated tracking", "Weekly payouts to bank")
        ),
        IntroCardData(
            id = 3,
            title = "Easy Instagram Integration",
            subtitle = "Auto DMs & Story swipe links.",
            icon = Icons.Default.Link,
            highlightTag = "INSTAGRAM TOOLS",
            bulletPoints = listOf("Link in bio setup", "Broadcast channel links", "Comment trigger automation")
        ),
        IntroCardData(
            id = 4,
            title = "10 Step-by-Step Modules",
            subtitle = "Learn at your own speed.",
            icon = Icons.Default.TrendingUp,
            highlightTag = "ROADMAP",
            bulletPoints = listOf("Interactive quizzes & missions", "Real creator strategies", "Progress tracking")
        )
    )

    val modulesList = listOf(
        // Module 1: Introduction to Wishlink
        WishlinkLessonItem(
            moduleIndex = 1,
            titleEnglish = "Module 1: Introduction to Wishlink",
            titleHindi = "मॉड्यूल 1: विशलिंक का परिचय",
            titleHinglish = "Module 1: Wishlink Kya Hai?",
            subtitleEnglish = "What is Wishlink, how creators earn, benefits & who should use it.",
            subtitleHindi = "विशलिंक क्या है, क्रिएटर्स कैसे कमाते हैं, इसके फायदे और किसे इस्तेमाल करना चाहिए।",
            subtitleHinglish = "Wishlink kya hai, creators kaise kamate hain, aur iske fayde.",
            bulletPointsEnglish = listOf(
                "• What is Wishlink? An affiliate platform connecting creators with top fashion & lifestyle brands.",
                "• How Creators Earn: Share custom product links; earn a commission on every valid purchase made through your link.",
                "• Top Benefits: Access 100+ top brands (Myntra, Ajio, Nykaa, H&M, Urbanic) in one single dashboard.",
                "• Who Should Use It? Fashion, beauty, lifestyle, tech, and daily recommendation creators on Instagram or YouTube."
            ),
            bulletPointsHindi = listOf(
                "• विशलिंक क्या है? यह क्रिएटर्स को फैशन और लाइफस्टाइल ब्रांड्स से जोड़ने वाला एफिलिएट प्लेटफॉर्म है।",
                "• कमाई कैसे होती है? अपने कस्टम प्रोडक्ट लिंक्स शेयर करें; हर खरीदारी पर निश्चित कमीशन पाएं।",
                "• मुख्य फायदे: एक ही डैशबोर्ड में 100+ बड़े ब्रांड्स (Myntra, Ajio, Nykaa) के लिंक्स बनाएं।",
                "• किसे इस्तेमाल करना चाहिए? इंस्टाग्राम और यूट्यूब पर फैशन, ब्यूटी, लाइफस्टाइल या शॉपिंग कंटेंट बनाने वाले क्रिएटर्स।"
            ),
            bulletPointsHinglish = listOf(
                "• Wishlink Kya Hai? Yeh creators ko top fashion aur lifestyle brands se jodne wala affiliate platform hai.",
                "• Earning Process: Apne custom product links share karo; har successful purchase par commission pao.",
                "• Main Benefits: Ek hi dashboard me Myntra, Ajio, Nykaa jaisse 100+ top brands ke links banao.",
                "• Who Should Join? Instagram aur YouTube par fashion, beauty, lifestyle aur review content banane wale creators."
            ),
            practicalTaskEnglish = "🎯 Mission 1: Visit official Wishlink website & explore featured brand partners.",
            practicalTaskHindi = "🎯 मिशन 1: विशलिंक की आधिकारिक वेबसाइट पर जाएं और पार्टनर ब्रांड्स देखें।",
            practicalTaskHinglish = "🎯 Mission 1: Wishlink ki official website visit karke partner brands check karo.",
            actionButtonTextEnglish = "Explore Wishlink Website",
            actionButtonTextHindi = "विशलिंक वेबसाइट खोलें",
            actionButtonTextHinglish = "Wishlink Website Visit Karo",
            actionUrl = WishlinkAcademyConfig.OFFICIAL_WEBSITE_URL
        ),

        // Module 2: Install Wishlink
        WishlinkLessonItem(
            moduleIndex = 2,
            titleEnglish = "Module 2: Install & Register Wishlink",
            titleHindi = "मॉड्यूल 2: विशलिंक इंस्टॉल और रजिस्ट्रेशन",
            titleHinglish = "Module 2: Wishlink Install Aur Signup",
            subtitleEnglish = "Download app, create account, login & basic setup.",
            subtitleHindi = "ऐप डाउनलोड करें, अकाउंट बनाएं, लॉगिन और बेसिक सेटअप सीखें।",
            subtitleHinglish = "App download karo, account banao aur basic setup complete karo.",
            bulletPointsEnglish = listOf(
                "• Step 1 Download: Install official Wishlink Creator App from Google Play Store.",
                "• Step 2 Create Account: Enter your active Indian mobile number & verify OTP.",
                "• Step 3 Creator Details: Enter full legal name, email address, and select creator domain.",
                "• Step 4 Basic Setup: Allow necessary notification permissions to receive instant sale updates."
            ),
            bulletPointsHindi = listOf(
                "• स्टेप 1 डाउनलोड: गूगल प्ले स्टोर से आधिकारिक Wishlink Creator ऐप इंस्टॉल करें।",
                "• स्टेप 2 अकाउंट बनाएं: अपना चालू मोबाइल नंबर दर्ज करें और ओटीपी सत्यापित करें।",
                "• स्टेप 3 क्रिएटर जानकारी: अपना सही नाम, ईमेल आईडी दर्ज करें और कैटेगरी चुनें।",
                "• स्टेप 4 बेसिक सेटअप: इंसटेंट सेल अपडेट प्राप्त करने के लिए नोटिफिकेशन ऑन रखें।"
            ),
            bulletPointsHinglish = listOf(
                "• Step 1 Download: Play Store se official Wishlink Creator App install karo.",
                "• Step 2 Account Signup: Apna mobile number dalkar OTP verify karo.",
                "• Step 3 Profile Info: Apna real name, email id dalo aur creator niche select karo.",
                "• Step 4 Basic Setup: Instant sales notifications ke liye permission enable karo."
            ),
            practicalTaskEnglish = "🎯 Mission 2: Download official Wishlink Creator app on Play Store.",
            practicalTaskHindi = "🎯 मिशन 2: प्ले स्टोर से विशलिंक क्रिएटर ऐप डाउनलोड करें।",
            practicalTaskHinglish = "🎯 Mission 2: Wishlink Creator App Play Store se install karo.",
            actionButtonTextEnglish = "Download Wishlink App",
            actionButtonTextHindi = "विशलिंक ऐप डाउनलोड करें",
            actionButtonTextHinglish = "Wishlink App Download Karo",
            actionUrl = WishlinkAcademyConfig.PLAY_STORE_URL,
            isPlayStoreAction = true
        ),

        // Module 3: Complete Profile
        WishlinkLessonItem(
            moduleIndex = 3,
            titleEnglish = "Module 3: Complete Your Creator Profile",
            titleHindi = "मॉड्यूल 3: क्रिएटर प्रोफाइल पूरा करें",
            titleHinglish = "Module 3: Creator Profile Complete Karo",
            subtitleEnglish = "Bio, profile photo, creator category & social media links.",
            subtitleHindi = "बायो, प्रोफाइल फोटो, कैटेगरी और सोशल मीडिया लिंक्स सेट करें।",
            subtitleHinglish = "Bio, profile photo, category aur social links add karo.",
            bulletPointsEnglish = listOf(
                "• Profile Photo: Upload a high-resolution, professional creator avatar or portrait.",
                "• Catchy Bio: Add 1 line describing your content (e.g., 'Daily Outfit Inspo & Budget Finds').",
                "• Category Selection: Select precise niche (Fashion, Beauty, Men's Grooming, Tech, Home).",
                "• Social Accounts: Link your primary Instagram handle & YouTube channel URL accurately."
            ),
            bulletPointsHindi = listOf(
                "• प्रोफाइल फोटो: अपनी एक साफ और प्रोफेशनल फोटो अपलोड करें।",
                "• आकर्षक बायो: 1 लाइन में बताएं कि आप किस तरह का कंटेंट बनाते हैं (जैसे 'Daily Fashion & Budget Finds')।",
                "• कैटेगरी चुनें: अपनी सही कैटेगरी चुनें (फैशन, ब्यूटी, मेंस ग्रूमिंग, टेक, होम)।",
                "• सोशल अकाउंट्स: अपना इंस्टाग्राम यूजरनेम और यूट्यूब चैनल लिंक सही से कनेक्ट करें।"
            ),
            bulletPointsHinglish = listOf(
                "• Profile Photo: Clear aur high-quality creator photo upload karo.",
                "• Catchy Bio: 1 line me likho aap kya content banate ho (e.g. 'Daily Budget Fashion Finds').",
                "• Category Select: Apni exact niche choose karo (Fashion, Beauty, Tech, Lifestyle).",
                "• Social Links: Apna Instagram username aur YouTube channel link add karo."
            ),
            practicalTaskEnglish = "🎯 Mission 3: Open Wishlink Creator dashboard & finalize profile details.",
            practicalTaskHindi = "🎯 मिशन 3: विशलिंक डैशबोर्ड खोलें और अपनी प्रोफाइल डिटेल्स पूरी करें।",
            practicalTaskHinglish = "🎯 Mission 3: Wishlink dashboard kholein aur profile complete karo.",
            actionButtonTextEnglish = "Open Creator Dashboard",
            actionButtonTextHindi = "डैशबोर्ड खोलें",
            actionButtonTextHinglish = "Dashboard Kholein",
            actionUrl = WishlinkAcademyConfig.CREATOR_DASHBOARD_URL
        ),

        // Module 4: Connect Instagram
        WishlinkLessonItem(
            moduleIndex = 4,
            titleEnglish = "Module 4: Connect Instagram & Fix Issues",
            titleHindi = "मॉड्यूल 4: इंस्टाग्राम कनेक्ट करें और समस्याएं सुलझाएं",
            titleHinglish = "Module 4: Instagram Connect & Connection Solutions",
            subtitleEnglish = "How to connect Instagram, required permissions & fixing disconnect issues.",
            subtitleHindi = "इंस्टाग्राम कनेक्ट कैसे करें, जरूरी परमिशन और डिस्कनेक्ट की समस्याएं ठीक करें।",
            subtitleHinglish = "Instagram connect karna, permissions aur login issues fix karna.",
            bulletPointsEnglish = listOf(
                "• Step 1 Account Type: Ensure your Instagram is switched to a Professional / Creator Account.",
                "• Step 2 Facebook Page: Link your Instagram to a Facebook Page (Required by Meta API).",
                "• Step 3 Permissions: Grant read/write access for comment auto-DM link delivery.",
                "• Fix Connection Issues: If disconnected, log out, clear app cache, and reconnect Meta account."
            ),
            bulletPointsHindi = listOf(
                "• स्टेप 1 अकाउंट टाइप: पक्का करें कि आपका इंस्टाग्राम क्रिएटर या बिजनेस अकाउंट पर स्विच है।",
                "• स्टेप 2 फेसबुक पेज: अपने इंस्टाग्राम को एक फेसबुक पेज से जोड़ें (मेटा परमिशन के लिए जरूरी)।",
                "• स्टेप 3 परमिशन: कमेंट ऑटो-डीएम सुविधा चालू करने के लिए सभी परमिशन अलाउ करें।",
                "• समस्या समाधान: अगर डिस्कनेक्ट हो जाए, तो लॉगआउट करके ऐप कैशे क्लियर करें और दोबारा रीकनेक्ट करें।"
            ),
            bulletPointsHinglish = listOf(
                "• Step 1 Account Type: Check karo Instagram Professional/Creator account par ho.",
                "• Step 2 Facebook Page: Instagram ko Facebook Page se connect karo (Meta requirement).",
                "• Step 3 Grant Permissions: Auto-DM link delivery ke liye saari permissions allow karo.",
                "• Fix Connection Issues: Disconnect hone par app cache clear karke fir se reconnect karo."
            ),
            practicalTaskEnglish = "🎯 Mission 4: Check if your Instagram profile is switched to Creator mode.",
            practicalTaskHindi = "🎯 मिशन 4: चेक करें कि आपका इंस्टाग्राम अकाउंट क्रिएटर मोड पर है या नहीं।",
            practicalTaskHinglish = "🎯 Mission 4: Check karo aapka Instagram Creator mode me switch hai ya nahi.",
            actionButtonTextEnglish = "Open Official Instagram Help",
            actionButtonTextHindi = "इंस्टाग्राम हेल्प देखें",
            actionButtonTextHinglish = "Instagram Help Page Kholein",
            actionUrl = WishlinkAcademyConfig.INSTAGRAM_OFFICIAL_URL
        ),

        // Module 5: Add Products
        WishlinkLessonItem(
            moduleIndex = 5,
            titleEnglish = "Module 5: Find & Add Products to Collections",
            titleHindi = "मॉड्यूल 5: प्रोडक्ट्स खोजें और कलेक्शन बनाएं",
            titleHinglish = "Module 5: Products Find Karo Aur Collections Banao",
            subtitleEnglish = "Find trending products, create affiliate links & organize collections.",
            subtitleHindi = "ट्रेंडिंग प्रोडक्ट्स खोजें, एफिलिएट लिंक बनाएं और कलेक्शन व्यवस्थित करें।",
            subtitleHinglish = "Trending products dhoondho, links banao aur collections manage karo.",
            bulletPointsEnglish = listOf(
                "• Search Products: Use Wishlink search bar to find exact items from Myntra, Ajio, Nykaa, etc.",
                "• Generate Link: Copy product URL from e-commerce app & paste in Wishlink Link Generator.",
                "• Organize Collections: Group items into collections like 'Summer Dresses', 'College Outfits', or 'Tech Deals'.",
                "• Manage Recommendations: Keep highest-converting & currently in-stock items at the top."
            ),
            bulletPointsHindi = listOf(
                "• प्रोडक्ट खोजें: विशलिंक सर्च बार से Myntra, Ajio, Nykaa के सटीक प्रोडक्ट्स ढूंढें।",
                "• लिंक जनरेट करें: शॉपिंग ऐप से लिंक कॉपी करें और विशलिंक लिंक जनरेटर में पेस्ट करें।",
                "• कलेक्शन बनाएं: प्रोडक्ट्स को 'Summer Outfits', 'College Finds' जैसे ग्रुप्स में सजाएं।",
                "• रिकमेंडेशन व्यवस्थित करें: सबसे ज्यादा बिकने वाले और इन-स्टॉक प्रोडक्ट्स को ऊपर रखें।"
            ),
            bulletPointsHinglish = listOf(
                "• Search Items: Wishlink search me Myntra, Ajio, Nykaa ke trending products search karo.",
                "• Create Links: Shopping app se URL copy karke Wishlink generator me paste karo.",
                "• Collections: Products ko 'College Wear', 'Grooming' jaise themes me categorize karo.",
                "• Recommendations: In-stock aur highest selling items ko top par rakho."
            ),
            practicalTaskEnglish = "🎯 Mission 5: Generate your first Wishlink affiliate product link.",
            practicalTaskHindi = "🎯 मिशन 5: अपना पहला विशलिंक एफिलिएट लिंक जनरेट करें।",
            practicalTaskHinglish = "🎯 Mission 5: Apna pehla Wishlink affiliate link generate karo.",
            actionButtonTextEnglish = "Generate Link on Wishlink",
            actionButtonTextHindi = "विशलिंक पर लिंक बनाएं",
            actionButtonTextHinglish = "Wishlink Par Link Banao",
            actionUrl = WishlinkAcademyConfig.CREATOR_DASHBOARD_URL
        ),

        // Module 6: Share on Instagram
        WishlinkLessonItem(
            moduleIndex = 6,
            titleEnglish = "Module 6: Share Links on Instagram",
            titleHindi = "मॉड्यूल 6: इंस्टाग्राम पर लिंक्स शेयर करने के तरीके",
            titleHinglish = "Module 6: Instagram Par Links Share Karne Ka Sahi Tarika",
            subtitleEnglish = "Stories, Reels, Posts, Broadcast Channels & Highlights best practices.",
            subtitleHindi = "स्टोरीज, रील्स, पोस्ट्स, ब्रॉडकास्ट चैनल और हाइलाइट्स में लिंक प्लेसमेंट।",
            subtitleHinglish = "Stories, Reels, Broadcast Channels aur Highlights me link placement.",
            bulletPointsEnglish = listOf(
                "• Instagram Stories: Add sticker link directly or trigger 'Comment LINK' auto-DM.",
                "• Instagram Reels: Write clear text overlay e.g., 'Comment LINK for outfit details!'.",
                "• Broadcast Channel: Post direct Wishlink product links with discount updates daily.",
                "• Saved Highlights: Create pinned highlights titled 'My Outfits', 'Tech Gear', 'Skincare'."
            ),
            bulletPointsHindi = listOf(
                "• इंस्टाग्राम स्टोरीज: डायरेक्ट लिंक स्टिकर लगाएं या 'कमेंट में LINK लिखें' ऑटो-डीएम ट्रिगर करें।",
                "• इंस्टाग्राम रील्स: वीडियो पर साफ टेक्स्ट लिखें 'प्रोडक्ट लिंक के लिए LINK कमेंट करें!'।",
                "• ब्रॉडकास्ट चैनल: रोज़ नए डिस्काउंट अपडेट्स के साथ डायरेक्ट विशलिंक लिंक्स शेयर करें।",
                "• सेव्ड हाइलाइट्स: 'My Outfits', 'Grooming', 'Tech' नाम से पिन्ड हाइलाइट्स बनाएं।"
            ),
            bulletPointsHinglish = listOf(
                "• Instagram Stories: Direct link sticker lagao ya 'Comment LINK' auto-DM trigger rakho.",
                "• Instagram Reels: On-screen clear text likho 'Comment LINK for outfit link!'.",
                "• Broadcast Channel: Daily discount deals ke sath direct Wishlink links share karo.",
                "• Highlights: 'Outfits', 'Budget Finds' naam se pinned highlights create karo."
            ),
            practicalTaskEnglish = "🎯 Mission 6: Add your Wishlink bio storefront link to your Instagram bio.",
            practicalTaskHindi = "🎯 मिशन 6: अपने इंस्टाग्राम बायो में विशलिंक स्टोरफ्रंट लिंक जोड़ें।",
            practicalTaskHinglish = "🎯 Mission 6: Apne Instagram bio me Wishlink storefront link add karo.",
            actionButtonTextEnglish = "Open Wishlink Storefront",
            actionButtonTextHindi = "अपना स्टोरफ्रंट देखें",
            actionButtonTextHinglish = "Storefront Open Karo",
            actionUrl = WishlinkAcademyConfig.CREATOR_DASHBOARD_URL
        ),

        // Module 7: Increase Clicks
        WishlinkLessonItem(
            moduleIndex = 7,
            titleEnglish = "Module 7: Strategies to Increase Clicks & Sales",
            titleHindi = "मॉड्यूल 7: क्लिक्स और बिक्री बढ़ाने की रणनीतियां",
            titleHinglish = "Module 7: Clicks Aur Sales Badhane Ki Strategies",
            subtitleEnglish = "Product placement, CTA examples, best posting times & trust building.",
            subtitleHindi = "प्रोडक्ट प्लेसमेंट, कॉल-टू-एक्शन, सही पोस्टिंग टाइम और ट्रस्ट बनाने की टिप्स।",
            subtitleHinglish = "Product placement, strong CTAs, best posting time aur trust tips.",
            bulletPointsEnglish = listOf(
                "• Powerful Call-To-Action (CTA): Say 'Comment LINK for 20% OFF code!' instead of 'Link in bio'.",
                "• Natural Product Placement: Show yourself wearing/using the product in real life.",
                "• Best Posting Times: Post fashion/lifestyle Reels between 6:00 PM – 9:30 PM.",
                "• Build Trust: Be honest about sizing, fabric quality, and price-to-value ratio."
            ),
            bulletPointsHindi = listOf(
                "• दमदार कॉल-टू-एक्शन (CTA): 'बायो में लिंक है' की जगह कहें '20% डिस्काउंट के लिए LINK कमेंट करें!'।",
                "• स्वाभाविक प्रोडक्ट प्लेसमेंट: रियल लाइफ में प्रोडक्ट को खुद पहनकर या इस्तेमाल करके दिखाएं।",
                "• सही पोस्टिंग समय: शाम 6:00 बजे से 9:30 बजे के बीच रील्स पोस्ट करें।",
                "• भरोसा बनाएं: प्रोडक्ट के साइज, कपड़े की क्वालिटी और सही कीमत के बारे में ईमानदार रहें।"
            ),
            bulletPointsHinglish = listOf(
                "• Powerful CTA: 'Link in bio' ki jagah bolo '20% Off ke liye LINK comment karein!'.",
                "• Natural Placement: Product ko real life me pehan kar ya try-on karke dikhao.",
                "• Best Posting Time: Shaam 6:00 PM se 9:30 PM ke beech Reels upload karo.",
                "• Trust Building: Size, fabric quality aur original pricing ke bare me honest raho."
            ),
            practicalTaskEnglish = "🎯 Mission 7: Practice a 3-second spoken video hook with 'Comment LINK'.",
            practicalTaskHindi = "🎯 मिशन 7: 'LINK कमेंट करें' के साथ 3-सेकंड का वीडियो हुक बोलने की प्रैक्टिस करें।",
            practicalTaskHinglish = "🎯 Mission 7: 'Comment LINK' bolte huye 3-second reel hook try karo.",
            actionButtonTextEnglish = "View Conversion Tips",
            actionButtonTextHindi = "कन्वर्जन टिप्स देखें",
            actionButtonTextHinglish = "Conversion Tips Dekho",
            actionUrl = WishlinkAcademyConfig.HELP_CENTER_URL
        ),

        // Module 8: Earning
        WishlinkLessonItem(
            moduleIndex = 8,
            titleEnglish = "Module 8: Earnings, Commissions & Withdrawal",
            titleHindi = "मॉड्यूल 8: कमाई, कमीशन और बैंक विदड्रॉल",
            titleHinglish = "Module 8: Earnings, Commissions & Bank Payouts",
            subtitleEnglish = "How commissions work, payout cycle, withdrawal process & rules.",
            subtitleHindi = "कमीशन कैसे काम करता है, पेआउट चक्र, बैंक विदड्रॉल और जरूरी नियम।",
            subtitleHinglish = "Commissions kaise calculate hote hain, payout cycle aur bank withdrawal.",
            bulletPointsEnglish = listOf(
                "• Commission Structure: Earn between 5% – 25% depending on e-commerce brand category.",
                "• Order Validation: Earnings get locked after the customer return/exchange window closes.",
                "• Payment Cycle: Payouts processed weekly or monthly directly to linked bank account / UPI.",
                "• Compliance Rules: Self-clicks or artificial bots violate policies and lead to account suspension."
            ),
            bulletPointsHindi = listOf(
                "• कमीशन दरें: ब्रांड और प्रोडक्ट कैटेगरी के आधार पर 5% से 25% तक कमीशन कमाएं।",
                "• ऑर्डर वैलिडेशन: कस्टमर रिटर्न/एक्सचेंज का समय खत्म होने के बाद कमाई कंफर्म होती है।",
                "• पेमेंट साइकल: साप्ताहिक या मासिक आधार पर सीधे आपके बैंक खाते या यूआईडी में ट्रांसफर।",
                "• नियम व शर्तें: खुद के लिंक पर बार-बार क्लिक करना या नकली बॉट्स इस्तेमाल करना सख्त मना है।"
            ),
            bulletPointsHinglish = listOf(
                "• Commission Rates: Brand aur category ke basis par 5% - 25% commission milta hai.",
                "• Order Lock Period: Customer return/exchange period khatam hone par earning lock hoti hai.",
                "• Payment Cycle: Direct bank account ya UPI me weekly/monthly payout milta hai.",
                "• Important Policy: Apne hi link par self-click ya fake bot traffic mat use karo."
            ),
            practicalTaskEnglish = "🎯 Mission 8: Add your bank account / UPI ID inside Wishlink wallet section.",
            practicalTaskHindi = "🎯 मिशन 8: विशलिंक वॉलेट सेक्शन में अपना बैंक अकाउंट / यूपीआई जोड़ें।",
            practicalTaskHinglish = "🎯 Mission 8: Wishlink wallet me apna bank account / UPI add karo.",
            actionButtonTextEnglish = "Check Commission Policy",
            actionButtonTextHindi = "कमीशन पॉलिसी पढ़ें",
            actionButtonTextHinglish = "Commission Policy Dekho",
            actionUrl = WishlinkAcademyConfig.COMMISSIONS_GUIDE_URL
        ),

        // Module 9: Advanced Tips
        WishlinkLessonItem(
            moduleIndex = 9,
            titleEnglish = "Module 9: Advanced Affiliate Growth Tips",
            titleHindi = "मॉड्यूल 9: एडवांस्ड एफिलिएट ग्रोथ टिप्स",
            titleHinglish = "Module 9: Advanced Affiliate Sales Growth Tips",
            subtitleEnglish = "Product selection strategy, audience targeting & common mistakes.",
            subtitleHindi = "स्मार्ट प्रोडक्ट सिलेक्शन, ऑडियंस टारगेटिंग और गलतियों से बचें।",
            subtitleHinglish = "Smart product selection, audience targeting aur common mistakes.",
            bulletPointsEnglish = listOf(
                "• High Demand Products: Promote under ₹999 trending budget fashion and viral items.",
                "• Seasonality Strategy: Share wedding wear before wedding season & winter wear in November.",
                "• Avoid Out-Of-Stock Links: Check Wishlink dashboard daily to replace sold-out items.",
                "• Audience Targeting: Match recommendations to your followers' age and budget group."
            ),
            bulletPointsHindi = listOf(
                "• अधिक डिमांड वाले प्रोडक्ट्स: ₹999 से कम कीमत वाले ट्रेंडी बजट फैशन और वायरल आइटम्स प्रोमोट करें।",
                "• सीजनल रणनीति: शादियों के सीजन से पहले एथनिक वेयर और नवंबर में विंटर वेयर शेयर करें।",
                "• आउट-ऑफ-स्टॉक से बचें: रोज डैशबोर्ड चेक करें और जो सामान बिक चुका हो उसका लिंक बदलें।",
                "• ऑडियंस टारगेटिंग: अपनी ऑडियंस की उम्र और बजट के हिसाब से ही प्रोडक्ट्स चुनें।"
            ),
            bulletPointsHinglish = listOf(
                "• High Demand Items: ₹999 ke andar aane wale trending budget fashion items promote karo.",
                "• Seasonal Strategy: Festival season se pehle ethnic wear aur winter me jackets share karo.",
                "• Out-of-Stock Check: Sold-out items ke links ko immediately replace karo.",
                "• Target Audience: Apni audience ki age group aur budget ke hisab se recommendations do."
            ),
            practicalTaskEnglish = "🎯 Mission 9: Pick 3 items under ₹999 and create a budget collection.",
            practicalTaskHindi = "🎯 मिशन 9: ₹999 से कम के 3 प्रोडक्ट्स चुनकर बजट कलेक्शन बनाएं।",
            practicalTaskHinglish = "🎯 Mission 9: ₹999 ke under 3 items choose karke budget collection banao.",
            actionButtonTextEnglish = "Open Wishlink Dashboard",
            actionButtonTextHindi = "विशलिंक डैशबोर्ड खोलें",
            actionButtonTextHinglish = "Wishlink Dashboard Kholein",
            actionUrl = WishlinkAcademyConfig.CREATOR_DASHBOARD_URL
        ),

        // Module 10: Creator Success Guide
        WishlinkLessonItem(
            moduleIndex = 10,
            titleEnglish = "Module 10: Daily & Weekly Creator Success Checklist",
            titleHindi = "मॉड्यूल 10: डेली व वीकली क्रिएटर सक्सेस चेकलिस्ट",
            titleHinglish = "Module 10: Daily & Weekly Creator Growth Routine",
            subtitleEnglish = "Daily workflow, weekly checklist, growth strategy & consistency.",
            subtitleHindi = "दैनिक रूटीन, साप्ताहिक चेकलिस्ट, ग्रोथ स्ट्रेटजी और निरंतरता।",
            subtitleHinglish = "Daily workflow, weekly checklist aur long-term consistency rule.",
            bulletPointsEnglish = listOf(
                "• Daily Routine (15 Mins): Check Wishlink earnings, post 1 Reel with Auto-DM link.",
                "• Weekly Checklist (1 Hour): Update bio link collection, review top 5 clicked products.",
                "• Long-Term Consistency: Post at least 4-5 Reels every week for compound growth.",
                "• Creator Mindset: Treat your affiliate recommendations as genuine helpful advice."
            ),
            bulletPointsHindi = listOf(
                "• दैनिक रूटीन (15 मिनट): विशलिंक अर्निंग्स चेक करें, ऑटो-डीएम लिंक के साथ 1 रील शेयर करें।",
                "• साप्ताहिक चेकलिस्ट (1 घंटा): बायो लिंक कलेक्शन अपडेट करें, टॉप 5 क्लिक हुए प्रोडक्ट्स रिव्यू करें।",
                "• लंबे समय तक निरंतरता: लगातार परिणाम पाने के लिए हर हफ्ते कम से कम 4-5 रील्स डालें।",
                "• क्रिएटर माइंडसेट: अपनी सिफारिशों को विज्ञापन के बजाय लोगों की मदद करने वाली सलाह समझें।"
            ),
            bulletPointsHinglish = listOf(
                "• Daily Workflow (15 Mins): Wishlink earnings check karo, 1 Reel Auto-DM link ke sath dalo.",
                "• Weekly Checklist (1 Hour): Bio collection refresh karo, top clicked products dekho.",
                "• Consistency Rule: Fast result ke liye har hafte 4-5 quality Reels upload karo.",
                "• Creator Mindset: Apne recommendations ko genuine help ki tarah share karo."
            ),
            practicalTaskEnglish = "🎯 Final Mission: Congratulations! You are now a Certified Wishlink Creator!",
            practicalTaskHindi = "🎯 अंतिम मिशन: बधाई हो! अब आप एक सर्टिफाइड विशलिंक क्रिएटर बन गए हैं!",
            practicalTaskHinglish = "🎯 Final Mission: Congratulations! Aap Wishlink Creator Academy complete kar chuke hain!",
            actionButtonTextEnglish = "Go To Wishlink Dashboard",
            actionButtonTextHindi = "विशलिंक डैशबोर्ड पर जाएं",
            actionButtonTextHinglish = "Wishlink Dashboard Par Jao",
            actionUrl = WishlinkAcademyConfig.CREATOR_DASHBOARD_URL
        )
    )
}

/**
 * Main Dialog Entry Point for Wishlink Creator Academy
 */
@Composable
fun WishlinkCreatorAiDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    var isEntranceVisible by remember { mutableStateOf(false) }

    // Language Selection State
    var selectedLanguage by remember {
        mutableStateOf(CreatorAcademyPrefs.getWishlinkLanguage(context).ifBlank { "" })
    }
    var isLanguageSelected by remember {
        mutableStateOf(CreatorAcademyPrefs.getWishlinkLanguage(context).isNotBlank())
    }
    var showLanguagePickerModal by remember { mutableStateOf(false) }

    // Step state
    val savedStepIndex = remember { CreatorAcademyPrefs.getWishlinkStepIndex(context) }
    var currentStepIndex by remember { mutableIntStateOf(savedStepIndex.coerceIn(0, 9)) }
    var showWelcomeBackDialog by remember { mutableStateOf(isLanguageSelected && savedStepIndex > 0) }

    // Completed steps
    var completedSteps by remember {
        mutableStateOf(CreatorAcademyPrefs.getWishlinkCompletedSteps(context).toSet())
    }

    LaunchedEffect(Unit) {
        isEntranceVisible = true
    }

    fun saveLanguage(lang: String) {
        selectedLanguage = lang
        isLanguageSelected = true
        CreatorAcademyPrefs.setWishlinkLanguage(context, lang)
        showLanguagePickerModal = false
    }

    fun setStep(index: Int) {
        val nextIdx = index.coerceIn(0, WishlinkStaticData.modulesList.size - 1)
        currentStepIndex = nextIdx
        CreatorAcademyPrefs.setWishlinkStepIndex(context, nextIdx)
        val updated = completedSteps + currentStepIndex
        completedSteps = updated
        CreatorAcademyPrefs.saveWishlinkCompletedSteps(context, updated)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AmoledBlack.copy(alpha = 0.94f))
                .statusBarsPadding()
                .navigationBarsPadding()
                .responsiveImeAndNavPadding(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = isEntranceVisible,
                enter = fadeIn(tween(350)) + scaleIn(tween(350, easing = FastOutSlowInEasing)),
                exit = fadeOut(tween(250)) + scaleOut(tween(250))
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .border(
                            BorderStroke(1.5.dp, WishlinkOrangeGlow),
                            RoundedCornerShape(28.dp)
                        ),
                    color = Color(0xFF100C0A)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Header Bar
                        WishlinkDialogHeader(
                            selectedLanguage = selectedLanguage,
                            isLanguageSelected = isLanguageSelected,
                            onLanguageClick = { showLanguagePickerModal = true },
                            onCloseClick = onDismiss
                        )

                        if (!isLanguageSelected) {
                            // FIRST SCREEN: Language Selection
                            WishlinkLanguageSelectionView(
                                onSelectLanguage = { lang ->
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    saveLanguage(lang)
                                }
                            )
                        } else {
                            // MAIN LEARNING VIEW
                            WishlinkMainLearningView(
                                currentStepIndex = currentStepIndex,
                                selectedLanguage = selectedLanguage,
                                completedSteps = completedSteps,
                                onNextStep = { setStep(currentStepIndex + 1) },
                                onPrevStep = { setStep(currentStepIndex - 1) },
                                onStepClick = { idx -> setStep(idx) },
                                onRestartCourse = {
                                    setStep(0)
                                    CreatorAcademyPrefs.saveWishlinkCompletedSteps(context, emptySet())
                                    completedSteps = emptySet()
                                }
                            )
                        }
                    }
                }
            }

            // Language Switcher Modal Sheet
            if (showLanguagePickerModal) {
                WishlinkLanguagePickerSheet(
                    currentLanguage = selectedLanguage,
                    onSelectLanguage = { lang ->
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        saveLanguage(lang)
                    },
                    onDismiss = { showLanguagePickerModal = false }
                )
            }
        }
    }
}

/**
 * Top Header Component
 */
@Composable
private fun WishlinkDialogHeader(
    selectedLanguage: String,
    isLanguageSelected: Boolean,
    onLanguageClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A120E))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OfficialLogo(name = "wishlink", modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Wishlink Creator Academy",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = "Zero to Hero Affiliate Guide",
                    fontSize = 11.sp,
                    color = WishlinkOrangePrimary
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLanguageSelected) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(WishlinkOrangeGlow)
                        .border(BorderStroke(1.dp, WishlinkOrangePrimary), RoundedCornerShape(20.dp))
                        .clickable { onLanguageClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = WishlinkOrangePrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = when (selectedLanguage) {
                                "Hindi" -> "हिन्दी"
                                "English" -> "English"
                                else -> "Hinglish"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF281C16))
                    .clickable { onCloseClick() },
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
    }
}

/**
 * Language Selection View (First Screen)
 */
@Composable
private fun WishlinkLanguageSelectionView(
    onSelectLanguage: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OfficialLogo(name = "wishlink", modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select Preferred Learning Language",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Choose your language. The entire Wishlink tutorial, guides, CTAs, and warnings will change completely to your selected language.",
            fontSize = 12.5.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Center,
            lineHeight = 17.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Language Option Cards
        WishlinkLangCard(
            title = "हिन्दी",
            subtitle = "पूरा विशलिंक कोर्स शुद्ध और आसान हिंदी में सीखें।",
            flagEmoji = "🇮🇳",
            onClick = { onSelectLanguage("Hindi") }
        )

        Spacer(modifier = Modifier.height(14.dp))

        WishlinkLangCard(
            title = "English",
            subtitle = "Learn the complete Wishlink Academy in clear simple English.",
            flagEmoji = "🌐",
            onClick = { onSelectLanguage("English") }
        )

        Spacer(modifier = Modifier.height(14.dp))

        WishlinkLangCard(
            title = "Hinglish",
            subtitle = "Natural Hinglish (Hindi + English) conversational guide.",
            flagEmoji = "⚡",
            onClick = { onSelectLanguage("Hinglish") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "🔒 You can change this language anytime from settings inside the tool.",
            fontSize = 11.sp,
            color = WishlinkOrangePrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun WishlinkLangCard(
    title: String,
    subtitle: String,
    flagEmoji: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.97f else 1f, label = "langScale")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF221713))
            .border(BorderStroke(1.2.dp, WishlinkOrangeGlow), RoundedCornerShape(18.dp))
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(WishlinkOrangeGlow),
                contentAlignment = Alignment.Center
            ) {
                Text(text = flagEmoji, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    lineHeight = 16.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Select",
                tint = WishlinkOrangePrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Main Learning Container Component
 */
@Composable
private fun WishlinkMainLearningView(
    currentStepIndex: Int,
    selectedLanguage: String,
    completedSteps: Set<Int>,
    onNextStep: () -> Unit,
    onPrevStep: () -> Unit,
    onStepClick: (Int) -> Unit,
    onRestartCourse: () -> Unit
) {
    val context = LocalContext.current
    val currentModule = WishlinkStaticData.modulesList.getOrElse(currentStepIndex) {
        WishlinkStaticData.modulesList.first()
    }

    val totalModules = WishlinkStaticData.modulesList.size
    val progressPercent = ((currentStepIndex + 1) * 100) / totalModules

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(14.dp)
    ) {
        // Progress Tracker Header Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1E1511))
                .border(BorderStroke(1.dp, WishlinkOrangeGlow), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Module ${currentStepIndex + 1} of $totalModules",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = WishlinkOrangePrimary
                    )
                    Text(
                        text = "$progressPercent% Completed",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2C1F18))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth((currentStepIndex + 1).toFloat() / totalModules.toFloat())
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(WishlinkOrangePrimary, Color(0xFFFF8A65))
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Module Quick Index Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    (0 until totalModules).forEach { idx ->
                        val isCompleted = completedSteps.contains(idx)
                        val isCurrent = idx == currentStepIndex
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isCurrent -> WishlinkOrangePrimary
                                        isCompleted -> Color(0xFF4CAF50)
                                        else -> Color(0xFF2C1F18)
                                    }
                                )
                                .clickable { onStepClick(idx) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${idx + 1}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Module Lesson Card
        val title = when (selectedLanguage) {
            "Hindi" -> currentModule.titleHindi
            "English" -> currentModule.titleEnglish
            else -> currentModule.titleHinglish
        }

        val subtitle = when (selectedLanguage) {
            "Hindi" -> currentModule.subtitleHindi
            "English" -> currentModule.subtitleEnglish
            else -> currentModule.subtitleHinglish
        }

        val bulletPoints = when (selectedLanguage) {
            "Hindi" -> currentModule.bulletPointsHindi
            "English" -> currentModule.bulletPointsEnglish
            else -> currentModule.bulletPointsHinglish
        }

        val practicalTask = when (selectedLanguage) {
            "Hindi" -> currentModule.practicalTaskHindi
            "English" -> currentModule.practicalTaskEnglish
            else -> currentModule.practicalTaskHinglish
        }

        val actionBtnText = when (selectedLanguage) {
            "Hindi" -> currentModule.actionButtonTextHindi
            "English" -> currentModule.actionButtonTextEnglish
            else -> currentModule.actionButtonTextHinglish
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .border(BorderStroke(1.2.dp, WishlinkOrangeGlow), RoundedCornerShape(22.dp)),
            color = Color(0xFF1E1511)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(WishlinkOrangeGlow),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${currentModule.moduleIndex}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = WishlinkOrangePrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = subtitle,
                            fontSize = 11.5.sp,
                            color = Color.LightGray,
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Short Visual Bullet Cards (No long text blocks)
                bulletPoints.forEach { point ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF281C16))
                            .border(BorderStroke(0.8.dp, Color(0xFF3B2A22)), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = point,
                            fontSize = 12.5.sp,
                            color = TextWhite,
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Practical Daily Mission Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(WishlinkOrangeGlow)
                        .border(BorderStroke(1.dp, WishlinkOrangePrimary), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Mission",
                                tint = WishlinkOrangePrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = when (selectedLanguage) {
                                    "Hindi" -> "आज का मिशन:"
                                    "English" -> "Daily Practical Mission:"
                                    else -> "Aaj Ka Practical Mission:"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = WishlinkOrangePrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = practicalTask,
                            fontSize = 12.sp,
                            color = TextWhite,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Official Action Button
                Button(
                    onClick = {
                        if (currentModule.isPlayStoreAction) {
                            WishlinkAcademyConfig.openPlayStore(context)
                        } else {
                            WishlinkAcademyConfig.openUrl(context, currentModule.actionUrl)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WishlinkOrangePrimary,
                        contentColor = TextWhite
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (currentModule.isPlayStoreAction) Icons.Default.Download else Icons.Default.OpenInNew,
                            contentDescription = "Action",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = actionBtnText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Controls (Previous / Next)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onPrevStep,
                enabled = currentStepIndex > 0,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF281C16),
                    contentColor = TextWhite,
                    disabledContainerColor = Color(0xFF1B1411),
                    disabledContentColor = Color.Gray
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when (selectedLanguage) {
                            "Hindi" -> "पिछला"
                            "English" -> "Previous"
                            else -> "Peeche"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = onNextStep,
                enabled = currentStepIndex < totalModules - 1,
                modifier = Modifier
                    .weight(1.2f)
                    .height(46.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WishlinkOrangePrimary,
                    contentColor = TextWhite
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = when (selectedLanguage) {
                            "Hindi" -> if (currentStepIndex == totalModules - 1) "पूरा हुआ" else "अगला सीखें"
                            "English" -> if (currentStepIndex == totalModules - 1) "Completed" else "Next Lesson"
                            else -> if (currentStepIndex == totalModules - 1) "Finish" else "Agla Lesson"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Educational Disclaimer Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF17100D))
                .border(BorderStroke(0.8.dp, Color(0xFF2D1E18)), RoundedCornerShape(14.dp))
                .padding(12.dp)
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Disclaimer",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when (selectedLanguage) {
                        "Hindi" -> "⚠️ डिस्क्लेमर: यह टूल केवल शैक्षणिक उद्देश्य के लिए है। हम किसी भी गारंटीकृत कमाई का दावा नहीं करते हैं। आपकी कमाई क्रिएटर के प्रदर्शन, ऑडियंस जुड़ाव और एफिलिएट नीतियों पर निर्भर करती है।"
                        "English" -> "⚠️ Disclaimer: This tool is for educational purposes. Do not claim any guaranteed earnings. Earnings depend on creator performance, audience engagement and affiliate programme policies."
                        else -> "⚠️ Disclaimer: Yeh tool educational purpose ke liye hai. Direct guaranteed earnings ka koi claim nahi hai. Real earnings creator performance, audience engagement aur affiliate policies par depend karti hain."
                    },
                    fontSize = 10.5.sp,
                    color = Color.Gray,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

/**
 * Language Switcher Sheet
 */
@Composable
private fun WishlinkLanguagePickerSheet(
    currentLanguage: String,
    onSelectLanguage: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(BorderStroke(1.2.dp, WishlinkOrangeGlow), RoundedCornerShape(24.dp)),
            color = Color(0xFF1E1511)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Change Learning Language",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(16.dp))

                WishlinkLangCard(
                    title = "हिन्दी",
                    subtitle = "शुद्ध और आसान हिंदी में सीखें।",
                    flagEmoji = "🇮🇳",
                    onClick = { onSelectLanguage("Hindi") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                WishlinkLangCard(
                    title = "English",
                    subtitle = "Learn in simple clear English.",
                    flagEmoji = "🌐",
                    onClick = { onSelectLanguage("English") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                WishlinkLangCard(
                    title = "Hinglish",
                    subtitle = "Natural Hinglish conversation.",
                    flagEmoji = "⚡",
                    onClick = { onSelectLanguage("Hinglish") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF281C16),
                        contentColor = TextWhite
                    )
                ) {
                    Text(text = "Close / Cancel", fontSize = 13.sp)
                }
            }
        }
    }
}
