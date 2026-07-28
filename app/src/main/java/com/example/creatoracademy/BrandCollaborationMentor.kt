package com.example.creatoracademy

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.ui.components.SmartWelcomeBackDialog
import com.example.ui.components.RestartCourseConfirmDialog
import com.example.ui.components.LearningProgressIndicatorCard
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.layout.imePadding
import com.example.ui.theme.responsiveImeAndNavPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.draw.shadow
import com.example.ui.components.PremiumIPhoneButton
import com.example.ui.components.CompactHelperChip
import com.example.ui.components.MentorToolTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.BuildConfig
import com.example.ui.screens.OfficialLogo
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.TextWhite

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

private val EmeraldPrimary = Color(0xFFFACC15) // Gold Yellow for Brand Collab AI
private val EmeraldGlow = Color(0x33FACC15) // Gold Glow Glass Effect
private val brandCollabTheme = MentorToolTheme.BrandCollab

/**
 * MASTER PHASE V2 — Brand Collaboration AI
 * Friendly Mentor Learning System with Multi-Language Support
 * Features:
 * 1. MANDATORY Language Selection Screen (Hindi 🇮🇳, English 🇺🇸, HinEnglish 🌐)
 * 2. Persistent language state & 1-tap language switcher badge
 * 3. Friendly conversational creator mentor tone (Zero textbook jargon!)
 * 4. 10 Step-by-Step Bounded Lessons with real brand examples (Boat, Meesho, Amazon, Snitch, Minimalist, Mamaearth, Nykaa, Mokobara)
 * 5. Interactive "Did you understand?" prompts with Yes 👍, Explain Again 🔄, Practice Task 📝 buttons
 * 6. Daily Missions & Practical Task Checklists for every lesson
 * 7. Session Memory & Progress Bar tracking
 */

// User profile data model
data class BrandCollabUserProfile(
    val experience: String = "Complete Beginner",
    val followers: String = "1k - 10k",
    val platform: String = "Instagram",
    val niche: String = "Lifestyle / Tech"
)

// Lesson Item Data Model for Multi-Language Support
data class BrandLessonItem(
    val stepNumber: Int,
    val titleEnglish: String,
    val titleHindi: String,
    val titleHinglish: String,
    val contentEnglish: String,
    val contentHindi: String,
    val contentHinglish: String,
    val practicalTaskEnglish: String,
    val practicalTaskHindi: String,
    val practicalTaskHinglish: String,
    val realBrandLogo: String = "instagram"
)

// Introduction Card data model
data class IntroCardData(
    val id: Int,
    val title: String,
    val subtitle: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val highlightTag: String,
    val bulletPoints: List<String>
)

// Real Creator App Platform Data Model
data class CreatorPlatformApp(
    val id: String,
    val name: String,
    val logoName: String,
    val category: String,
    val shortDesc: String,
    val playStorePackage: String,
    val websiteUrl: String,
    val badgeText: String
)

// Chat Message Data Model
data class BrandMentorMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val isFromUser: Boolean,
    val text: String,
    val timestamp: String = "Just now",
    val isLessonStep: Boolean = false,
    val stepNumber: Int = 0,
    val stepTitle: String = "",
    val practicalTaskText: String? = null,
    val showConfirmationPrompt: Boolean = false,
    val isSimplerExplanation: Boolean = false,
    val platformApps: List<CreatorPlatformApp>? = null,
    val pitchTemplate: String? = null
)

object BrandCollabStaticData {

    // 6 Introduction Cards
    val introCards = listOf(
        IntroCardData(
            id = 1,
            title = "Welcome to Brand Collaboration AI",
            subtitle = "Become a professional creator.",
            icon = Icons.Default.Campaign,
            highlightTag = "AI MENTOR MODE",
            bulletPoints = listOf("Personal AI guide for brand deals", "Zero knowledge required", "Step-by-step interactive lessons")
        ),
        IntroCardData(
            id = 2,
            title = "Learn Brand Deals",
            subtitle = "Understand how creators earn.",
            icon = Icons.Default.MonetizationOn,
            highlightTag = "MONETIZATION",
            bulletPoints = listOf("Gifted collabs vs paid deals", "Real market pricing formulas", "How brands assign budgets")
        ),
        IntroCardData(
            id = 3,
            title = "Find Collaboration Apps",
            subtitle = "Discover trusted platforms.",
            icon = Icons.Default.Storefront,
            highlightTag = "REAL PLATFORMS",
            bulletPoints = listOf("Direct Play Store app links", "Official creator marketplaces", "Instant campaign applications")
        ),
        IntroCardData(
            id = 4,
            title = "Pitch Like a Professional",
            subtitle = "Learn how to message brands.",
            icon = Icons.Default.Email,
            highlightTag = "PITCH GENERATOR",
            bulletPoints = listOf("High-converting Instagram DMs", "Professional email proposals", "1-tap template copy")
        ),
        IntroCardData(
            id = 5,
            title = "Instagram Growth",
            subtitle = "Prepare your profile before pitching.",
            icon = Icons.Default.TrendingUp,
            highlightTag = "PROFILE AUDIT",
            bulletPoints = listOf("Optimizing your bio for brand deals", "Calculating true engagement rate", "Setting up contact channels")
        ),
        IntroCardData(
            id = 6,
            title = "Let's Start",
            subtitle = "Begin learning.",
            icon = Icons.Default.AutoAwesome,
            highlightTag = "READY",
            bulletPoints = listOf("Your journey starts from step 1", "Interactive confirmations after each lesson", "Personalized mentor guidance")
        )
    )

    // Real Creator Platform Marketplaces
    val realPlatforms = listOf(
        CreatorPlatformApp(
            id = "plixxo",
            name = "Plixxo / Winkl",
            logoName = "instagram",
            category = "Influencer Marketplace",
            shortDesc = "India's largest influencer marketing platform. Apply directly for paid campaigns with top beauty, tech, and lifestyle brands.",
            playStorePackage = "com.popxo.plixxo",
            websiteUrl = "https://www.plixxo.com",
            badgeText = "VERIFIED • TOP RECOMMENDED"
        ),
        CreatorPlatformApp(
            id = "one_impression",
            name = "OneImpression",
            logoName = "google",
            category = "Global Brand Deals",
            shortDesc = "Connect with over 500+ global and Indian brands for YouTube integration and Instagram Reel campaigns.",
            playStorePackage = "com.oneimpression.app",
            websiteUrl = "https://oneimpression.ai",
            badgeText = "HIGH PAYING DEALS"
        ),
        CreatorPlatformApp(
            id = "meesho_creator",
            name = "Meesho Creator Program",
            logoName = "meesho",
            category = "E-Commerce & Affiliate",
            shortDesc = "Get free product samples, review clothing/gadgets, and earn high affiliate commissions on every sale.",
            playStorePackage = "com.meesho.supply",
            websiteUrl = "https://www.meesho.com",
            badgeText = "FREE PRODUCTS & COMMISSIONS"
        ),
        CreatorPlatformApp(
            id = "amazon_influencer",
            name = "Amazon Influencer Program",
            logoName = "amazon",
            category = "Product Storefront",
            shortDesc = "Build your personal Amazon storefront, showcase top recommended gear, and earn passive monthly income.",
            playStorePackage = "com.amazon.mShop.android.shopping",
            websiteUrl = "https://affiliate-program.amazon.in/influencer",
            badgeText = "PASSIVE EARNINGS"
        ),
        CreatorPlatformApp(
            id = "good_creator",
            name = "Good Creator Co / Taggle",
            logoName = "flipkart",
            category = "Lifestyle & Fashion",
            shortDesc = "Exclusive creator network backed by MyGlamm and Good Glamm Group. Get gifted beauty boxes and paid sponsorships.",
            playStorePackage = "com.goodcreator.app",
            websiteUrl = "https://www.goodcreator.co",
            badgeText = "GIFTED BOXES & PAID"
        ),
        CreatorPlatformApp(
            id = "opraahfx",
            name = "OpraahFx & Influencer.in",
            logoName = "youtube",
            category = "Gaming & Tech Campaigns",
            shortDesc = "Specialized agency for gaming, tech, and entertainment creators. Direct sponsorships with ASUS, Boat, and Samsung.",
            playStorePackage = "com.influencer.in",
            websiteUrl = "https://www.influencer.in",
            badgeText = "TECH & GAMING DEALS"
        )
    )

    // 10 Comprehensive Step-by-Step Guided Lessons
    val guidedLessonsV2 = listOf(
        BrandLessonItem(
            stepNumber = 1,
            titleEnglish = "Step 1: What is Brand Collaboration?",
            titleHindi = "स्टेप 1: ब्रांड कोलैबोरेट क्या होता है?",
            titleHinglish = "Step 1: Brand Collaboration Kya Hota Hai?",
            contentEnglish = "Awesome 😄 Today we'll learn from zero how brand deals work! A brand collaboration is when top companies like Boat, Mamaearth, or Minimalist send you free products or pay you money to showcase their item in your Reel or video.",
            contentHindi = "नमस्ते दोस्त! 😄 आज हम एकदम ज़ीरो से सीखेंगे कि ब्रांड डील्स कैसे मिलती हैं! जब Boat, Mamaearth या Minimalist जैसी कंपनियां आपको मुफ्त प्रोडक्ट भेजती हैं या रील्स में अपना प्रोडक्ट दिखाने के लिए पैसे देती हैं, तो उसे ब्रांड कोलैबोरेशन कहते हैं।",
            contentHinglish = "Awesome 😄 Aaj hum zero se seekhenge ki brand deals kaise milti hain! Jab Boat, Mamaearth, ya Minimalist jaisi companies aapko free product bhejti hain ya Reel banane ke paise deti hain, toh use Brand Collaboration kehte hain.",
            practicalTaskEnglish = "🎯 Mission 1: Find 2 creators in your niche and check who sponsors their Reels today.",
            practicalTaskHindi = "🎯 मिशन 1: अपनी कैटेगरी के 2 क्रिएटर्स की रील्स देखें और चेक करें उन्हें कौन स्पॉन्सर कर रहा है।",
            practicalTaskHinglish = "🎯 Mission 1: Apne niche ke 2 creators ki Reels dekho aur check karo unhe kaun sponsor kar raha hai.",
            realBrandLogo = "instagram"
        ),
        BrandLessonItem(
            stepNumber = 2,
            titleEnglish = "Step 2: Prepare Instagram Profile",
            titleHindi = "स्टेप 2: अपना इंस्टाग्राम प्रोफाइल रेडी करें",
            titleHinglish = "Step 2: Instagram Profile Ready Karo",
            contentEnglish = "Before messaging any brand, your profile must look business-ready! Brands look for 3 things: 1) Clear niche in bio (e.g. 'Tech Reviews' or 'Budget Fashion'), 2) Business email address like 'contact.yourname@gmail.com', and 3) High-quality pinned Reels.",
            contentHindi = "किसी ब्रांड को मैसेज करने से पहले अपना प्रोफाइल रेडी करें! ब्रांड्स 3 चीजें देखते हैं: 1) बायो में आपकी कैटेगरी (जैसे 'Tech Reviews' या 'Fashion Hacks'), 2) बिज़नेस ईमेल जैसे 'contact.yourname@gmail.com', और 3) आपकी बेस्ट 3 रील्स पिन्ड हों।",
            contentHinglish = "Brand ko message karne se pehle profile business-ready banao! Brands 3 cheezein dekhte hain: 1) Bio me clear niche, 2) Business email (contact.yourname@gmail.com), aur 3) Top 3 Reels pinned rahein.",
            practicalTaskEnglish = "🎯 Mission 2: Add a business email and clear niche line to your Instagram bio today.",
            practicalTaskHindi = "🎯 मिशन 2: अपने इंस्टाग्राम बायो में बिज़नेस ईमेल और साफ कैटेगरी लाइन जोड़ें।",
            practicalTaskHinglish = "🎯 Mission 2: Apne Instagram bio me business email aur clear niche line add karo.",
            realBrandLogo = "instagram"
        ),
        BrandLessonItem(
            stepNumber = 3,
            titleEnglish = "Step 3: Finding Micro-Creator Brands",
            titleHindi = "स्टेप 3: माइक्रो-क्रिएटर्स को रखने वाले ब्रांड्स खोजें",
            titleHinglish = "Step 3: Micro-Creators Ko Hire Karne Wale Brands",
            contentEnglish = "Don't waste time pitching massive luxury brands! Look at creators who have 2k to 20k followers in your niche. Check who sponsors them! Brands like Mokobara, Snitch, Sugar Cosmetics, and Boat actively hire micro-creators every day.",
            contentHindi = "शुरुआत में बहुत बड़े ब्रांड्स के पीछे मत भागिए! अपने कैटेगरी के 2k से 20k फॉलोअर्स वाले क्रिएटर्स को देखें और चेक करें कि उन्हें कौन स्पॉन्सर कर रहा है। Mokobara, Snitch, Sugar, और Boat जैसे ब्रांड्स माइक्रो-क्रिएटर्स को रोज़ काम देते हैं।",
            contentHinglish = "Shuruat me bade luxury brands ke peeche mat bhago! 2k-20k followers wale creators ko dekho. Mokobara, Snitch, Sugar, aur Boat jaisey brands micro-creators ko daily hire karte hain.",
            practicalTaskEnglish = "🎯 Mission 3: Save 3 brands sponsoring micro-creators in your niche today.",
            practicalTaskHindi = "🎯 मिशन 3: अपने नीश के 3 ऐसे ब्रांड्स सेव करें जो छोटे क्रिएटर्स को स्पॉन्सर करते हैं।",
            practicalTaskHinglish = "🎯 Mission 3: Apne niche ke 3 micro-creator friendly brands ko save karo.",
            realBrandLogo = "meesho"
        ),
        BrandLessonItem(
            stepNumber = 4,
            titleEnglish = "Step 4: Collaboration Apps for Creators",
            titleHindi = "स्टेप 4: कोलैबोरेशन के लिए बेस्ट ऐप्स",
            titleHinglish = "Step 4: Collaboration Ke Liye Best Apps",
            contentEnglish = "You don't have to cold-pitch alone! Automated creator marketplaces like Meesho Creator Program, Amazon Influencer, Plixxo, and OneImpression connect you directly with active brand campaigns where you can apply with 1 click.",
            contentHindi = "आपको अकेले मैसेज भेजने की ज़रूरत नहीं है! Meesho Creator, Amazon Influencer, Plixxo, और OneImpression जैसे ऐप्स क्रिएटर्स को डायरेक्ट ब्रांड कैम्पेन से जोड़ते हैं, जहाँ आप 1-क्लिक में अप्लाई कर सकते हैं।",
            contentHinglish = "Aapko akele cold message bhejne ki zaroorat nahi hai! Meesho Creator Program, Amazon Influencer, Plixxo, aur OneImpression jaise apps par direct campaigns milte hain.",
            practicalTaskEnglish = "🎯 Mission 4: Explore or sign up on 1 creator platform (e.g. Meesho Creator or Amazon Influencer).",
            practicalTaskHindi = "🎯 मिशन 4: आज ही किसी 1 क्रिएटर ऐप (जैसे Meesho या Amazon Influencer) पर एक्सप्लोर करें।",
            practicalTaskHinglish = "🎯 Mission 4: Aaj kisi 1 creator platform (Meesho/Amazon) par sign up karo.",
            realBrandLogo = "amazon"
        ),
        BrandLessonItem(
            stepNumber = 5,
            titleEnglish = "Step 5: Pitching Brands via DM & Email",
            titleHindi = "स्टेप 5: ब्रांड्स को सही मैसेज कैसे भेजें",
            titleHinglish = "Step 5: Brand Ko Message Kaise Kare (Formula)",
            contentEnglish = "Keep your pitch under 3 sentences! 1) Genuine compliment: 'I love your new wireless earbuds!', 2) Value pitch: 'I create tech review Reels reaching 15k+ daily viewers', 3) Action offer: 'Can I send a custom Reel pitch and my Media Kit?'",
            contentHindi = "ब्रांड को मैसेज हमेशा 3 लाइनों में रखें! 1) तारीफ: 'मुझे आपके ईयरबड्स बहुत पसंद आए!', 2) वैल्यू: 'मैं टेक रील्स बनाता हूँ जिसकी डेली रीच 15k+ है', 3) ऑफर: 'क्या मैं आपको इस प्रोडक्ट का रील्स आईडिया और मीडिया किट भेज सकता हूँ?'",
            contentHinglish = "Message hamesha short 3 lines me rakho! 1) Compliment: 'I love your new earbuds!', 2) Value: 'Meri Reels daily 15k+ reach karti hain', 3) Offer: 'Kya main aapko ek Reel idea aur Media Kit bheju?'",
            practicalTaskEnglish = "🎯 Mission 5: Draft your 3-sentence pitch using our built-in Pitch Coach!",
            practicalTaskHindi = "🎯 मिशन 5: हमारे ऐप के Pitch Coach फीचर से अपना 3-लाइन का पिच तैयार करें।",
            practicalTaskHinglish = "🎯 Mission 5: Hamare Pitch Coach se apna 3-line pitch ready karo.",
            realBrandLogo = "google"
        ),
        BrandLessonItem(
            stepNumber = 6,
            titleEnglish = "Step 6: Handling Replies & Follow-Ups",
            titleHindi = "स्टेप 6: ब्रांड के जवाब और फॉलो-अप को हैंडल करें",
            titleHinglish = "Step 6: Reply Kaise Handle Kare aur Follow-up",
            contentEnglish = "If a brand manager doesn't reply in 3 days, don't worry! Send a friendly follow-up: 'Hey! Just following up on my previous message. We'd love to feature your brand this week!' 70% of deals get closed on the 1st follow-up.",
            contentHindi = "अगर ब्रांड मैनेजर 3 दिन तक जवाब न दे, तो परेशान न हों! एक फ्रेंडली फॉलो-अप भेजें: 'हे! पिछले मैसेज की याद दिलाने के लिए मैसेज किया। हम इस हफ़्ते आपकी ब्रांड फीचर करना चाहते हैं!' 70% डील्स फॉलो-अप में फाइनल होती हैं।",
            contentHinglish = "Agar brand manager 3 din tak reply na kare, toh pareshaan mat ho! Aise polite follow-up bhejo: 'Hey! Following up on my previous message.' 70% deals follow-up se hi finalize hoti hain.",
            practicalTaskEnglish = "🎯 Mission 6: Set a reminder to follow up 3 days after sending any brand DM.",
            practicalTaskHindi = "🎯 मिशन 6: किसी ब्रांड को डीएम भेजने के 3 दिन बाद फॉलो-अप का रिमाइंडर सेट करें।",
            practicalTaskHinglish = "🎯 Mission 6: Brand DM bhejne ke 3 din baad follow-up ka reminder rakho.",
            realBrandLogo = "instagram"
        ),
        BrandLessonItem(
            stepNumber = 7,
            titleEnglish = "Step 7: How to Negotiate Payment",
            titleHindi = "स्टेप 7: सही पेमेंट और रेट्स कैसे तय करें",
            titleHinglish = "Step 7: Price Negotiation & Rate Card",
            contentEnglish = "Never work for free if a brand demands custom scripts or ad rights! For 1k–10k followers, charge ₹2,500 – ₹8,000 per Reel. If they want to run your video as a paid ad, add 50% extra usage fee!",
            contentHindi = "अगर ब्रांड आपसे कस्टम स्क्रिप्ट या एड राइट्स मांगता है, तो कभी फ्री में काम न करें! 1k–10k फॉलोअर्स के लिए प्रति रील ₹2,500 से ₹8,000 चार्ज करें। अगर वे आपकी वीडियो को एड की तरह चलाना चाहें, तो 50% एक्स्ट्रा फीस लें!",
            contentHinglish = "Agar brand custom script ya ad rights maange, toh kabhi free me kaam mat karo! 1k-10k followers ke liye ₹2,500 - ₹8,000 per Reel charge karo. Ad usage rights ke liye +50% extra charge karo!",
            practicalTaskEnglish = "🎯 Mission 7: Calculate your minimum Reel & Story collaboration price.",
            practicalTaskHindi = "🎯 मिशन 7: अपनी रील और स्टोरी कोलैबोरेशन की मिनिमम प्राइस तय करें।",
            practicalTaskHinglish = "🎯 Mission 7: Apni minimum Reel & Story pricing calculate karo.",
            realBrandLogo = "flipkart"
        ),
        BrandLessonItem(
            stepNumber = 8,
            titleEnglish = "Step 8: Turning Free Gifts into Paid Deals",
            titleHindi = "स्टेप 8: फ्री गिफ्ट्स को पैसें वाली डील में बदलें",
            titleHinglish = "Step 8: Gifted Product Ko Paid Deal Me Badlo",
            contentEnglish = "When a brand says 'We can only send free products', reply politely: 'Thank you! I'd love to review the product in an unboxing Story for free. But for a dedicated high-converting Reel, my fee is ₹3,000.' This converts 40% of free offers into cash deals!",
            contentHindi = "जब ब्रांड कहे 'हम सिर्फ फ्री प्रोडक्ट दे सकते हैं', तो प्यार से कहें: 'थैंक यू! मैं फ्री प्रोडक्ट की अनबॉक्सिंग स्टोरी शेयर कर दूँगा। लेकिन डेडिकेटेड रील के लिए मेरी फीस ₹3,000 है।' इससे 40% फ्री डील्स पेड कैश में बदल जाती हैं!",
            contentHinglish = "Jab brand kahe 'Hum sirf free product bhejenge', toh politely bolo: 'Thank you! Main Story me unboxing free kar dunga. Par dedicated Reel ke liye meri fee ₹3,000 hai.' Isse 40% free offers cash deals me convert hoti hain!",
            practicalTaskEnglish = "🎯 Mission 8: Use this upgrade script when any brand offers free gifts.",
            practicalTaskHindi = "🎯 मिशन 8: जब भी कोई ब्रांड फ्री गिफ्ट ऑफर करे, इस स्क्रिप्ट का उपयोग करें।",
            practicalTaskHinglish = "🎯 Mission 8: Free product offers aane par is upgrade script ko use karo.",
            realBrandLogo = "meesho"
        ),
        BrandLessonItem(
            stepNumber = 9,
            titleEnglish = "Step 9: Avoiding Sponsorship Scams",
            titleHindi = "स्टेप 9: स्पॉन्सरशिप फ्रॉड और स्कैम से बचें",
            titleHinglish = "Step 9: Sponsorship Scams Se Kaise Bachein",
            contentEnglish = "Beware of emails from random Gmail addresses asking you to click unknown links to 'verify contract'. Real brand managers send emails from official company domains like `@boat.com` or `@meesho.com`. Never share your Instagram password!",
            contentHindi = "अनजान Gmail या लिंक भेजने वाले फेक स्पॉन्सरशिप मेल से सावधान रहें! असली ब्रांड मैनेजर्स हमेशा कंपनी की ऑफिशियल डोमेन (जैसे `@boat.com` या `@meesho.com`) से मेल भेजते हैं। कभी भी पासवर्ड न शेयर करें!",
            contentHinglish = "Random Gmail ID se aane wale fake sponsorship emails se bacho! Real brand managers hamesha company ke official domain (@boat.com ya @meesho.com) se email bhejte hain. Kabhi password ya link mat click karo!",
            practicalTaskEnglish = "🎯 Mission 9: Always verify email domains before opening links or contracts.",
            practicalTaskHindi = "🎯 मिशन 9: कोई भी लिंक या कॉन्ट्रैक्ट खोलने से पहले हमेशा ईमेल डोमेन वेरीफाई करें।",
            practicalTaskHinglish = "🎯 Mission 9: Hamesha email domain verify karke hi contract open karo.",
            realBrandLogo = "amazon"
        ),
        BrandLessonItem(
            stepNumber = 10,
            titleEnglish = "Step 10: Closing Deals & Performance Reports",
            titleHindi = "स्टेप 10: डील फाइनल करें और रिपोर्ट भेजें",
            titleHinglish = "Step 10: Deal Finalize Karo aur Report Bhejo",
            contentEnglish = "Always ask for 50% advance payment before posting your Reel! 7 days after publishing, send a screenshot of your Reel impressions & engagement to the brand manager. Showing great results turns one-time deals into monthly paid retainers!",
            contentHindi = "रील पोस्ट करने से पहले हमेशा 50% एडवांस पेमेंट लें! रील पोस्ट करने के 7 दिन बाद व्यूज और इंगेजमेंट का स्क्रीनशॉट ब्रांड मैनेजर को भेजें। यह प्रोफेशनल आदत आपको हर महीने पक्का काम दिलाएगी!",
            contentHinglish = "Reel publish karne se pehle hamesha 50% advance payment lo! Post karne ke 7 din baad Views aur Engagement ki report brand manager ko send karo. Isse aapko har month recurring retainer milne lagega!",
            practicalTaskEnglish = "🎯 Mission 10: Save a screenshot template of your Reel Insights ready for brand reports.",
            practicalTaskHindi = "🎯 मिशन 10: अपनी रील्स एनालिटिक्स का एक स्क्रीनशॉट टेम्पलेट तैयार रखें।",
            practicalTaskHinglish = "🎯 Mission 10: Apne Reel Insights ka screenshot template ready rakho.",
            realBrandLogo = "google"
        )
    )
}

/**
 * Main Dialog Entry Point
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandCollaborationAiDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    // Animation & View States
    var isEntranceVisible by remember { mutableStateOf(false) }
    var currentIntroCardIndex by remember { mutableIntStateOf(0) }
    var isIntroCompleted by remember { mutableStateOf(false) }

    // Language Selection State
    var selectedLanguage by remember {
        mutableStateOf(CreatorAcademyPrefs.getBrandCollabLanguage(context).ifBlank { "HinEnglish" })
    }
    var isLanguageSelected by remember {
        mutableStateOf(CreatorAcademyPrefs.getBrandCollabLanguage(context).isNotBlank())
    }
    var showLanguageSwitcherModal by remember { mutableStateOf(false) }

    // Session Memory & Resume State
    val savedStepIndex = remember { CreatorAcademyPrefs.getBrandCollabStepIndex(context) }
    var showWelcomeBackDialog by remember { mutableStateOf(isLanguageSelected && savedStepIndex > 0) }
    var showRestartConfirmDialog by remember { mutableStateOf(false) }

    // User Profile
    var userProfile by remember { mutableStateOf(BrandCollabUserProfile()) }
    var isProfileSet by remember { mutableStateOf(false) }

    // Step-by-Step Guided Mentor Chat State
    var currentStepIndex by remember { mutableIntStateOf(0) }
    val chatMessages = remember { mutableStateListOf<BrandMentorMessage>() }
    var isThinking by remember { mutableStateOf(false) }
    var thinkingMessage by remember { mutableStateOf("") }
    var customUserInput by remember { mutableStateOf("") }

    // Pitch Generator Mode Drawer
    var activeTab by remember { mutableStateOf("MENTOR_CHAT") } // MENTOR_CHAT, APPS_MARKET, PITCH_BUILDER, LANG_SETTINGS
    var pitchChannel by remember { mutableStateOf("INSTAGRAM_DM") }
    var targetBrandName by remember { mutableStateOf("Boat Audio") }

    val listState = rememberLazyListState()

    // Trigger Entrance Animation on Launch
    LaunchedEffect(Unit) {
        isEntranceVisible = true
    }

    // Function to load step lesson
    fun loadStepLesson(
        stepIdx: Int,
        isSimpler: Boolean = false,
        giveExample: Boolean = false,
        styleFormat: String? = null
    ) {
        scope.launch {
            isThinking = true
            thinkingMessage = when {
                giveExample -> "🧠 Generating real-world creator example (Email, DM, Rates)..."
                isSimpler -> "🧠 Crafting a fresh, easy-to-understand explanation..."
                else -> "🧠 Deep Thinking: Analyzing your progress, profile & lesson context..."
            }
            delay(700)

            val lessonItem = BrandCollabStaticData.guidedLessonsV2.getOrElse(stepIdx) {
                BrandCollabStaticData.guidedLessonsV2.first()
            }

            val title = when (selectedLanguage) {
                "Hindi" -> lessonItem.titleHindi
                "English" -> lessonItem.titleEnglish
                else -> lessonItem.titleHinglish
            }

            val baseContent = when (selectedLanguage) {
                "Hindi" -> lessonItem.contentHindi
                "English" -> lessonItem.contentEnglish
                else -> lessonItem.contentHinglish
            }

            val practicalTask = when (selectedLanguage) {
                "Hindi" -> lessonItem.practicalTaskHindi
                "English" -> lessonItem.practicalTaskEnglish
                else -> lessonItem.practicalTaskHinglish
            }

            // Fresh dynamic response generator or Gemini cloud call
            val finalExplanation = fetchDynamicStepExplanation(
                stepIndex = stepIdx + 1,
                stepTitle = title,
                baseContent = baseContent,
                userProfile = userProfile,
                selectedLanguage = selectedLanguage,
                isSimpler = isSimpler,
                giveExample = giveExample,
                styleFormat = styleFormat,
                recentHistory = chatMessages.toList()
            )

            isThinking = false

            // Attach Platform Apps to Step 4
            val platformAppsList = if (stepIdx == 3) BrandCollabStaticData.realPlatforms else null

            val message = BrandMentorMessage(
                isFromUser = false,
                text = finalExplanation,
                isLessonStep = true,
                stepNumber = stepIdx + 1,
                stepTitle = title,
                practicalTaskText = practicalTask,
                showConfirmationPrompt = true,
                isSimplerExplanation = isSimpler,
                platformApps = platformAppsList
            )

            chatMessages.add(message)
            delay(100)
            if (chatMessages.isNotEmpty()) {
                listState.animateScrollToItem(chatMessages.size - 1)
            }
        }
    }

    // Function to handle custom freeform AI mentor chat queries
    fun sendCustomUserQuery(queryText: String) {
        if (queryText.isBlank()) return
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

        val userMsg = BrandMentorMessage(isFromUser = true, text = queryText)
        chatMessages.add(userMsg)
        customUserInput = ""

        scope.launch {
            if (chatMessages.isNotEmpty()) {
                listState.animateScrollToItem(chatMessages.size - 1)
            }
            isThinking = true
            thinkingMessage = "🧠 Deep Thinking: Analyzing your question, progress & lesson context..."
            delay(800)

            val response = generateGeminiMentorResponse(
                query = queryText,
                profile = userProfile,
                selectedLanguage = selectedLanguage,
                recentHistory = chatMessages.toList()
            )
            isThinking = false

            val aiMsg = BrandMentorMessage(
                isFromUser = false,
                text = response,
                showConfirmationPrompt = false
            )
            chatMessages.add(aiMsg)
            delay(100)
            if (chatMessages.isNotEmpty()) {
                listState.animateScrollToItem(chatMessages.size - 1)
            }
        }
    }

    val imeBottomPadding = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
    LaunchedEffect(chatMessages.size, isThinking, customUserInput, imeBottomPadding) {
        if (chatMessages.isNotEmpty()) {
            delay(60)
            listState.animateScrollToItem(chatMessages.size - 1)
        }
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
                .background(AmoledBlack)
                .statusBarsPadding()
                .responsiveImeAndNavPadding()
        ) {
            AnimatedVisibility(
                visible = isEntranceVisible,
                enter = slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow)
                ) + fadeIn(animationSpec = tween(400)),
                exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            ) {
                Surface(
                    color = Color(0xFF0F1A14),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // ==================================================
                        // DYNAMIC ISLAND HEADER WITH PROGRESS & LANG SELECTOR
                        // ==================================================
                        DynamicIslandHeader(
                            stepNumber = currentStepIndex + 1,
                            totalSteps = BrandCollabStaticData.guidedLessonsV2.size,
                            currentLanguage = selectedLanguage,
                            onOpenLanguageSelector = {
                                showLanguageSwitcherModal = true
                            },
                            onCloseClick = onDismiss
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (!isLanguageSelected) {
                            // ==================================================
                            // 1. MANDATORY LANGUAGE SELECTION SCREEN
                            // ==================================================
                            LanguageSelectionScreen(
                                currentLanguage = selectedLanguage,
                                onLanguageSelected = { lang ->
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedLanguage = lang
                                    isLanguageSelected = true
                                    CreatorAcademyPrefs.setBrandCollabLanguage(context, lang)
                                }
                            )
                        } else if (!isIntroCompleted) {
                            // ==================================================
                            // 2. INTRODUCTION & SWIPEABLE ROADMAP CARDS
                            // ==================================================
                            IntroCardsView(
                                onStartMentorship = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isIntroCompleted = true
                                },
                                onSkipIntro = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isIntroCompleted = true
                                }
                            )
                        } else if (!isProfileSet) {
                            // ==================================================
                            // 3. ZERO KNOWLEDGE PROFILE SETUP (TAILORS MENTOR)
                            // ==================================================
                            ZeroKnowledgeProfileSetup(
                                profile = userProfile,
                                onProfileChanged = { userProfile = it },
                                onStartMentorship = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isProfileSet = true
                                    if (!showWelcomeBackDialog) {
                                        loadStepLesson(0)
                                    }
                                }
                            )
                        } else {
                            // ==================================================
                            // 4. MAIN MENTOR DASHBOARD & CHAT INTERFACE
                            // ==================================================
                            // Tab Selector
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0x15FFFFFF))
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TabPill("AI Mentor 💬", activeTab == "MENTOR_CHAT") {
                                    activeTab = "MENTOR_CHAT"
                                }
                                TabPill("Real Apps 📱", activeTab == "APPS_MARKET") {
                                    activeTab = "APPS_MARKET"
                                }
                                TabPill("Pitch Coach ✉️", activeTab == "PITCH_BUILDER") {
                                    activeTab = "PITCH_BUILDER"
                                }
                                TabPill("Lang 🌐", activeTab == "LANG_SETTINGS") {
                                    activeTab = "LANG_SETTINGS"
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            if (showWelcomeBackDialog) {
                                SmartWelcomeBackDialog(
                                    courseTitle = "Brand Collaboration AI",
                                    currentStep = savedStepIndex + 1,
                                    totalSteps = BrandCollabStaticData.guidedLessonsV2.size,
                                    onContinue = {
                                        showWelcomeBackDialog = false
                                        currentStepIndex = savedStepIndex
                                        loadStepLesson(savedStepIndex)
                                    },
                                    onRestart = { showRestartConfirmDialog = true },
                                    onDismiss = onDismiss
                                )
                            } else {
                                when (activeTab) {
                                    "MENTOR_CHAT" -> {
                                        LaunchedEffect(chatMessages.size, isThinking) {
                                            if (chatMessages.isNotEmpty()) {
                                                listState.animateScrollToItem(chatMessages.size - 1)
                                            }
                                        }

                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                                .navigationBarsPadding()
                                                .imePadding()
                                        ) {
                                            // Fixed Current Step Header
                                            Surface(
                                                shape = RoundedCornerShape(16.dp),
                                                color = Color(0xFF121B16),
                                                border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.45f)),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 4.dp, vertical = 4.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column {
                                                        Text(
                                                            text = "Brand Collaboration AI",
                                                            fontSize = 13.5.sp,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            color = TextWhite,
                                                            letterSpacing = 0.3.sp
                                                        )
                                                        Spacer(modifier = Modifier.height(2.dp))
                                                        Text(
                                                            text = "Current Step: ${currentStepIndex + 1} / ${BrandCollabStaticData.guidedLessonsV2.size}",
                                                            fontSize = 11.5.sp,
                                                            fontWeight = FontWeight.SemiBold,
                                                            color = EmeraldPrimary
                                                        )
                                                    }

                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(12.dp))
                                                            .background(Color(0x2210B981))
                                                            .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f)), RoundedCornerShape(12.dp))
                                                            .padding(horizontal = 10.dp, vertical = 5.dp)
                                                    ) {
                                                        val percent = ((currentStepIndex + 1) * 100 / BrandCollabStaticData.guidedLessonsV2.size)
                                                        Text(
                                                            text = "$percent% Done",
                                                            fontSize = 10.5.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = EmeraldPrimary
                                                        )
                                                    }
                                                }
                                            }

                                            // Chat Messages List
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .fillMaxWidth()
                                            ) {
                                                LazyColumn(
                                                    state = listState,
                                                    modifier = Modifier.fillMaxSize(),
                                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                                ) {
                                                    items(chatMessages, key = { it.id }) { msg ->
                                                        ChatMessageItem(
                                                            message = msg,
                                                            selectedLanguage = selectedLanguage,
                                                            totalSteps = BrandCollabStaticData.guidedLessonsV2.size,
                                                            onConfirmedNext = {
                                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                                CreatorAcademyPrefs.addXpPoints(context, 50, "BRAND_DEALS")
                                                                val nextIdx = currentStepIndex + 1
                                                                CreatorAcademyPrefs.setBrandCollabStepIndex(context, nextIdx)

                                                                if (nextIdx < BrandCollabStaticData.guidedLessonsV2.size) {
                                                                    currentStepIndex = nextIdx
                                                                    val celebrationToast = when (selectedLanguage) {
                                                                        "Hindi" -> "🎉 बहुत बढ़िया! +50 XP मिले। अगला लेसन..."
                                                                        "English" -> "🎉 Great job! +50 XP earned. Next lesson loading..."
                                                                        else -> "🎉 Awesome! +50 XP earned. Next lesson shuru..."
                                                                    }
                                                                    Toast.makeText(context, celebrationToast, Toast.LENGTH_SHORT).show()
                                                                    loadStepLesson(currentStepIndex, isSimpler = false)
                                                                } else {
                                                                    Toast.makeText(context, "🏆 Congratulations! You completed all 10 Brand Collaboration Lessons!", Toast.LENGTH_LONG).show()
                                                                }
                                                            },
                                                            onExplainAgain = {
                                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                                loadStepLesson(currentStepIndex, isSimpler = true)
                                                            },
                                                            onGiveExample = {
                                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                                loadStepLesson(currentStepIndex, giveExample = true)
                                                            },
                                                            onPreviousStep = {
                                                                if (currentStepIndex > 0) {
                                                                    currentStepIndex--
                                                                    loadStepLesson(currentStepIndex)
                                                                }
                                                            },
                                                            onNextStep = {
                                                                if (currentStepIndex + 1 < BrandCollabStaticData.guidedLessonsV2.size) {
                                                                    currentStepIndex++
                                                                    loadStepLesson(currentStepIndex)
                                                                }
                                                            },
                                                            onAskQuestion = {
                                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                                customUserInput = "Can you give me another example for ${msg.stepTitle}?"
                                                            },
                                                            onOpenPlayStore = { pkg ->
                                                                try {
                                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$pkg"))
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                                    context.startActivity(intent)
                                                                } catch (_: Exception) {
                                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$pkg"))
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                                    context.startActivity(intent)
                                                                }
                                                            },
                                                            onOpenWebsite = { url ->
                                                                try {
                                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                                    context.startActivity(intent)
                                                                } catch (_: Exception) {
                                                                    Toast.makeText(context, "Opening $url", Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                        )
                                                    }

                                                    if (isThinking) {
                                                        item {
                                                            AiThinkingIndicator(message = thinkingMessage)
                                                        }
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // ChatGPT Style Floating Glass Input Area
                                            Surface(
                                                shape = RoundedCornerShape(26.dp),
                                                color = Color(0x2818221D),
                                                border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.45f)),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 4.dp, vertical = 6.dp)
                                                    .shadow(
                                                        elevation = 10.dp,
                                                        shape = RoundedCornerShape(26.dp),
                                                        spotColor = EmeraldPrimary.copy(alpha = 0.35f)
                                                    )
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 14.dp, vertical = 6.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    androidx.compose.foundation.text.BasicTextField(
                                                        value = customUserInput,
                                                        onValueChange = { customUserInput = it },
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .padding(vertical = 8.dp),
                                                        textStyle = androidx.compose.ui.text.TextStyle(
                                                            color = TextWhite,
                                                            fontSize = 13.sp,
                                                            fontFamily = androidx.compose.ui.text.font.FontFamily.Default
                                                        ),
                                                        cursorBrush = androidx.compose.ui.graphics.SolidColor(EmeraldPrimary),
                                                        maxLines = 3,
                                                        decorationBox = { innerTextField ->
                                                            if (customUserInput.isBlank()) {
                                                                val placeholderText = when (selectedLanguage) {
                                                                    "Hindi" -> "ब्रांड डील्स के बारे में कुछ भी पूछें..."
                                                                    "English" -> "Ask AI Mentor anything about brand deals..."
                                                                    else -> "Brand deals ke baare me kuch bhi poochho..."
                                                                }
                                                                Text(
                                                                    text = placeholderText,
                                                                    fontSize = 13.sp,
                                                                    color = TextWhite.copy(alpha = 0.45f)
                                                                )
                                                            }
                                                            innerTextField()
                                                        }
                                                    )

                                                    Spacer(modifier = Modifier.width(8.dp))

                                                    val isEnabled = customUserInput.isNotBlank() && !isThinking
                                                    val sendScale by animateFloatAsState(
                                                        targetValue = if (isEnabled) 1.05f else 0.95f,
                                                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                                                        label = "sendScale"
                                                    )

                                                    Box(
                                                        modifier = Modifier
                                                            .size(38.dp)
                                                            .scale(sendScale)
                                                            .clip(CircleShape)
                                                            .background(if (isEnabled) EmeraldPrimary else Color(0x33FFFFFF))
                                                            .clickable(enabled = isEnabled) {
                                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                                sendCustomUserQuery(customUserInput)
                                                            },
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Send,
                                                            contentDescription = "Send",
                                                            tint = if (isEnabled) AmoledBlack else TextWhite.copy(alpha = 0.3f),
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    "APPS_MARKET" -> {
                                        // Real Creator Platforms List
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                                .verticalScroll(rememberScrollState())
                                        ) {
                                            Text(
                                                text = "📱 Top Recommended Creator Platforms",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextWhite
                                            )
                                            Text(
                                                text = "Verified marketplaces where brands post active collaboration campaigns",
                                                fontSize = 11.sp,
                                                color = TextWhite.copy(alpha = 0.65f)
                                            )

                                            Spacer(modifier = Modifier.height(12.dp))

                                            BrandCollabStaticData.realPlatforms.forEach { app ->
                                                RealPlatformCardItem(
                                                    app = app,
                                                    onOpenPlayStore = { pkg ->
                                                        try {
                                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$pkg"))
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                            context.startActivity(intent)
                                                        } catch (_: Exception) {
                                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$pkg"))
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                            context.startActivity(intent)
                                                        }
                                                    },
                                                    onOpenWebsite = { url ->
                                                        try {
                                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                            context.startActivity(intent)
                                                        } catch (_: Exception) {
                                                            Toast.makeText(context, "Opening $url", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                            }
                                        }
                                    }

                                    "PITCH_BUILDER" -> {
                                        // Pitch Generator Component
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                                .verticalScroll(rememberScrollState())
                                        ) {
                                            AiPitchCoachGeneratorView(
                                                userProfile = userProfile,
                                                selectedChannel = pitchChannel,
                                                targetBrand = targetBrandName,
                                                onChannelChanged = { pitchChannel = it },
                                                onTargetBrandChanged = { targetBrandName = it }
                                            )
                                        }
                                    }

                                    "LANG_SETTINGS" -> {
                                        // Language Switcher Screen inside tabs
                                        LanguageSelectionScreen(
                                            currentLanguage = selectedLanguage,
                                            onLanguageSelected = { lang ->
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                selectedLanguage = lang
                                                CreatorAcademyPrefs.setBrandCollabLanguage(context, lang)
                                                activeTab = "MENTOR_CHAT"
                                                Toast.makeText(context, "Switched to $lang!", Toast.LENGTH_SHORT).show()
                                            }
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

    // Language Switcher Overlay Modal (When clicking header badge)
    if (showLanguageSwitcherModal) {
        Dialog(
            onDismissRequest = { showLanguageSwitcherModal = false },
            properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .clickable { showLanguageSwitcherModal = false }
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF0F121C),
                    border = BorderStroke(1.5.dp, EmeraldPrimary),
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth(0.94f)
                        .clickable(enabled = false) {}
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🌐 Switch Learning Language",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x22FFFFFF))
                                    .clickable { showLanguageSwitcherModal = false },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite, modifier = Modifier.size(16.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        LanguageSelectionScreen(
                            currentLanguage = selectedLanguage,
                            onLanguageSelected = { lang ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                selectedLanguage = lang
                                CreatorAcademyPrefs.setBrandCollabLanguage(context, lang)
                                showLanguageSwitcherModal = false
                                // Refresh current lesson in new language
                                if (chatMessages.isNotEmpty()) {
                                    loadStepLesson(currentStepIndex, isSimpler = false)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showRestartConfirmDialog) {
        RestartCourseConfirmDialog(
            courseTitle = "Brand Collaboration AI",
            onConfirmRestart = {
                CreatorAcademyPrefs.resetCourseProgress(context, "brand_collab")
                isLanguageSelected = false
                selectedLanguage = "HinEnglish"
                currentStepIndex = 0
                chatMessages.clear()
                showWelcomeBackDialog = false
                showRestartConfirmDialog = false
            },
            onDismiss = { showRestartConfirmDialog = false }
        )
    }
}

/**
 * 1. Redesigned Language Selection Screen Component (Glass sheet, Apple rounded buttons, equal spacing)
 */
@Composable
private fun LanguageSelectionScreen(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    var selected by remember { mutableStateOf(if (currentLanguage.isNotBlank()) currentLanguage else "HinEnglish") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 440.dp)
                .fillMaxWidth(0.95f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color(0x1A102018),
                border = BorderStroke(
                    1.5.dp,
                    Brush.verticalGradient(
                        listOf(EmeraldPrimary.copy(alpha = 0.6f), Color(0x22FFFFFF))
                    )
                ),
                shadowElevation = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(EmeraldPrimary.copy(alpha = 0.35f), Color.Transparent)
                                )
                            )
                            .border(BorderStroke(1.5.dp, EmeraldPrimary), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🌐", fontSize = 30.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Choose Your Language",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextWhite,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Select the language you are most comfortable learning in.",
                        fontSize = 12.5.sp,
                        color = TextWhite.copy(alpha = 0.75f),
                        textAlign = TextAlign.Center,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    val languageOptions = listOf(
                        Triple("Hindi", "Hindi (हिन्दी)", "🇮🇳"),
                        Triple("English", "English", "🇺🇸"),
                        Triple("HinEnglish", "Hinglish (Mix)", "🌐")
                    )

                    languageOptions.forEach { (code, displayName, flag) ->
                        val isSelected = selected == code

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .clickable { selected = code },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) Color(0xFF162A20) else Color(0x18FFFFFF),
                            border = BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) EmeraldPrimary else Color(0x25FFFFFF)
                            ),
                            shadowElevation = if (isSelected) 6.dp else 0.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 18.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(text = flag, fontSize = 22.sp)
                                    Text(
                                        text = displayName,
                                        fontSize = 15.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) EmeraldPrimary else TextWhite
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) EmeraldPrimary else Color.Transparent)
                                        .border(
                                            BorderStroke(
                                                1.5.dp,
                                                if (isSelected) EmeraldPrimary else Color(0x44FFFFFF)
                                            ),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = AmoledBlack,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(26.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(EmeraldPrimary, Color(0xFF00E676))
                                )
                            )
                            .clickable { onLanguageSelected(selected) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Continue in ${if (selected == "HinEnglish") "Hinglish" else selected} ➔",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmoledBlack
                        )
                    }
                }
            }
        }
    }
}

/**
 * Session Memory Resume Card
 */
@Composable
private fun SessionResumeCard(
    savedStepIndex: Int,
    totalSteps: Int,
    lessonTitle: String,
    onContinue: () -> Unit,
    onRestart: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF141E28),
        border = BorderStroke(1.5.dp, EmeraldPrimary)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "👋", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Welcome back, Creator!",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "You completed up to Lesson ${savedStepIndex + 1} of $totalSteps",
                        fontSize = 11.sp,
                        color = TextWhite.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x2210B981))
                    .padding(10.dp)
            ) {
                Text(
                    text = "📍 Continue: Lesson ${savedStepIndex + 1} • $lessonTitle",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EmeraldPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(19.dp))
                        .background(Color(0x22FFFFFF))
                        .clickable { onRestart() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Start Over 🔄",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1.5f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(19.dp))
                        .background(EmeraldPrimary)
                        .clickable { onContinue() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Continue Lesson ${savedStepIndex + 1} ➔",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmoledBlack
                    )
                }
            }
        }
    }
}

/**
 * Dynamic Island Style Header Bar with Progress Bar
 */
@Composable
private fun DynamicIslandHeader(
    stepNumber: Int,
    totalSteps: Int,
    currentLanguage: String,
    onOpenLanguageSelector: () -> Unit,
    onCloseClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF161B29))
                    .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)), RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Brand Collab AI",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x2210B981))
                        .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f)), RoundedCornerShape(12.dp))
                        .clickable { onOpenLanguageSelector() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val flag = when (currentLanguage) {
                            "Hindi" -> "🇮🇳"
                            "English" -> "🇺🇸"
                            else -> "🌐"
                        }
                        Text(text = "$flag $currentLanguage ▾", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    }
                }

                Text(
                    text = "Lesson $stepNumber/$totalSteps",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite.copy(alpha = 0.7f)
                )

                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                        .clickable { onCloseClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextWhite,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape)
                .background(Color(0x22FFFFFF))
        ) {
            val progressRatio = (stepNumber.toFloat() / totalSteps.toFloat()).coerceIn(0.05f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progressRatio)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(EmeraldPrimary, Color(0xFF00E676))
                        )
                    )
            )
        }
    }
}

/**
 * 2 & 3 & 4. Redesigned Intro & Swipeable Roadmap Cards View
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun IntroCardsView(
    onStartMentorship: () -> Unit,
    onSkipIntro: () -> Unit
) {
    val cards = BrandCollabStaticData.introCards
    val pagerState = rememberPagerState(pageCount = { cards.size })
    var hasSwipedRoadmap by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage > 0) {
            hasSwipedRoadmap = true
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "borderGlow")
    val borderGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!hasSwipedRoadmap) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x2210B981))
                        .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "⬅ Swipe to explore ➡",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }

            Text(
                text = "Skip Intro ➔",
                fontSize = 12.sp,
                color = TextWhite.copy(alpha = 0.7f),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onSkipIntro() }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp),
            contentPadding = PaddingValues(horizontal = 10.dp),
            pageSpacing = 12.dp
        ) { pageIndex ->
            val card = cards[pageIndex]

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = EmeraldGlow),
                shape = RoundedCornerShape(24.dp),
                color = Color(0x1A14241C),
                border = BorderStroke(
                    1.2.dp,
                    Brush.linearGradient(
                        listOf(
                            EmeraldPrimary.copy(alpha = borderGlowAlpha),
                            Color(0x33FFFFFF)
                        )
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape)
                                .background(Color(0x2210B981))
                                .border(BorderStroke(1.5.dp, EmeraldPrimary), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = card.icon,
                                contentDescription = card.title,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x2210B981))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = card.highlightTag,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = card.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextWhite,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = card.subtitle,
                            fontSize = 12.5.sp,
                            color = TextWhite.copy(alpha = 0.75f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            card.bulletPoints.forEach { pt ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Check",
                                        tint = EmeraldPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = pt,
                                        fontSize = 12.sp,
                                        color = TextWhite.copy(alpha = 0.9f),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    Text(
                        text = "Card ${pageIndex + 1} of ${cards.size}",
                        fontSize = 11.sp,
                        color = TextWhite.copy(alpha = 0.5f),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            cards.indices.forEach { idx ->
                val isSelected = idx == pagerState.currentPage
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 22.dp else 8.dp, 8.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) EmeraldPrimary else Color(0x33FFFFFF))
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        var isPressed by remember { mutableStateOf(false) }
        val buttonScale by animateFloatAsState(
            targetValue = if (isPressed) 0.96f else 1f,
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
            label = "buttonScale"
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .height(52.dp)
                .scale(buttonScale)
                .clickable {
                    isPressed = true
                    onStartMentorship()
                },
            shape = RoundedCornerShape(26.dp),
            color = EmeraldPrimary,
            border = BorderStroke(1.5.dp, Color(0xFF00FF87)),
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(EmeraldPrimary, Color(0xFF00E676))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Start AI Mentorship 🚀",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AmoledBlack
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Start",
                        tint = AmoledBlack,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/**
 * Zero Knowledge Profile Setup
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ZeroKnowledgeProfileSetup(
    profile: BrandCollabUserProfile,
    onProfileChanged: (BrandCollabUserProfile) -> Unit,
    onStartMentorship: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Text(
            text = "🎯 Personalize Your Brand Mentor",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
        Text(
            text = "We teach from absolute zero. Select your current status:",
            fontSize = 12.sp,
            color = TextWhite.copy(alpha = 0.65f)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(text = "Brand Collaboration Experience:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Complete Beginner", "1–3 Gifted Collabs", "4–10 Paid Deals", "10+ Pro").forEach { opt ->
                ChipPill(
                    label = opt,
                    isSelected = profile.experience == opt,
                    onClick = { onProfileChanged(profile.copy(experience = opt)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Current Follower / Subscriber Count:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("< 1k", "1k - 10k", "10k - 50k", "50k+").forEach { opt ->
                ChipPill(
                    label = opt,
                    isSelected = profile.followers == opt,
                    onClick = { onProfileChanged(profile.copy(followers = opt)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Primary Platform:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Instagram", "YouTube", "Both").forEach { opt ->
                ChipPill(
                    label = opt,
                    isSelected = profile.platform == opt,
                    onClick = { onProfileChanged(profile.copy(platform = opt)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Content Niche:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Lifestyle / Tech", "Fashion & Beauty", "Gaming & Entertainment", "Fitness & Food").forEach { opt ->
                ChipPill(
                    label = opt,
                    isSelected = profile.niche == opt,
                    onClick = { onProfileChanged(profile.copy(niche = opt)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(EmeraldPrimary)
                .clickable { onStartMentorship() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Launch Step-by-Step AI Mentorship 🚀",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AmoledBlack
            )
        }
    }
}

/**
 * Chat Message Item with Interactive Confirmation Prompts & Daily Missions
 */
@Composable
private fun ChatMessageItem(
    message: BrandMentorMessage,
    selectedLanguage: String,
    totalSteps: Int = 10,
    onConfirmedNext: () -> Unit = {},
    onExplainAgain: () -> Unit = {},
    onGiveExample: () -> Unit = {},
    onPreviousStep: () -> Unit = {},
    onNextStep: () -> Unit = {},
    onAskQuestion: () -> Unit = {},
    onOpenPlayStore: (String) -> Unit = {},
    onOpenWebsite: (String) -> Unit = {}
) {
    val isUser = message.isFromUser

    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(350)) + slideInVertically(animationSpec = tween(350), initialOffsetY = { it / 3 })
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
        ) {
            if (!isUser) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0x2E10B981))
                        .border(BorderStroke(1.dp, EmeraldPrimary), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "AI Mentor",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column(
                modifier = Modifier.widthIn(max = 420.dp)
            ) {
                val bubbleShape = RoundedCornerShape(
                    topStart = if (isUser) 22.dp else 4.dp,
                    topEnd = if (isUser) 4.dp else 22.dp,
                    bottomStart = 22.dp,
                    bottomEnd = 22.dp
                )

                Surface(
                    shape = bubbleShape,
                    color = if (isUser) Color(0x3B10B981) else Color(0x2E14241C),
                    border = BorderStroke(1.dp, if (isUser) EmeraldPrimary.copy(alpha = 0.6f) else EmeraldPrimary.copy(alpha = 0.35f)),
                    modifier = Modifier.shadow(
                        elevation = 8.dp,
                        shape = bubbleShape,
                        spotColor = if (isUser) EmeraldPrimary.copy(alpha = 0.3f) else Color(0x2210B981)
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        if (message.isLessonStep) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = "Lesson",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = message.stepTitle,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        Text(
                            text = message.text,
                            fontSize = 12.5.sp,
                            color = TextWhite,
                            lineHeight = 18.5.sp
                        )

                        // Daily Mission / Practical Task Chip
                        message.practicalTaskText?.let { task ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x2210B981))
                                    .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f)), RoundedCornerShape(12.dp))
                                    .padding(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Task", tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "DAILY PRACTICAL MISSION",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldPrimary
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = task,
                                            fontSize = 11.5.sp,
                                            color = TextWhite,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Embedded Platform Apps List
                        message.platformApps?.let { apps ->
                            Spacer(modifier = Modifier.height(10.dp))
                            apps.take(3).forEach { app ->
                                RealPlatformCardItem(
                                    app = app,
                                    onOpenPlayStore = onOpenPlayStore,
                                    onOpenWebsite = onOpenWebsite
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                    }
                }

                // Interactive Confirmation Prompt Box ("Did you understand?")
                if (!isUser && message.showConfirmationPrompt) {
                    Spacer(modifier = Modifier.height(10.dp))

                    // Progress Updated ✓ Chip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0x2210B981),
                        border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Progress Updated",
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Progress Updated ✓",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary,
                                letterSpacing = 0.3.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x1C10B981))
                            .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)), RoundedCornerShape(16.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            val promptTitle = when (selectedLanguage) {
                                "Hindi" -> "✅ क्या यह स्टेप समझ आ गया?"
                                "English" -> "✅ Did you understand this step?"
                                else -> "✅ Yeh step samajh aa gaya?"
                            }

                            Text(
                                text = promptTitle,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // Yes, Continue Button
                                val yesLabel = when (selectedLanguage) {
                                    "Hindi" -> "हाँ 👍"
                                    "English" -> "Yes, Continue ✅"
                                    else -> "Yes, Continue ✅"
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1.3f)
                                        .height(36.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(EmeraldPrimary)
                                        .clickable { onConfirmedNext() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = yesLabel,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AmoledBlack
                                    )
                                }

                                // Explain Again Button
                                val explainLabel = when (selectedLanguage) {
                                    "Hindi" -> "फिर से समझाओ ❓"
                                    "English" -> "Explain Again ❓"
                                    else -> "Explain Again ❓"
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .height(36.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(Color(0x22FFFFFF))
                                        .clickable { onExplainAgain() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = explainLabel,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextWhite
                                    )
                                }

                                // Give Example Button
                                val exampleLabel = when (selectedLanguage) {
                                    "Hindi" -> "उदाहरण 📝"
                                    "English" -> "Give Example 📝"
                                    else -> "Give Example 📝"
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .height(36.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(Color(0x22FFFFFF))
                                        .clickable { onGiveExample() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = exampleLabel,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextWhite
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Glass Apple-Style Navigation Buttons: ← Previous  |  Continue →
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val canGoPrev = message.stepNumber > 1
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0x1AFFFFFF),
                            border = BorderStroke(1.dp, if (canGoPrev) Color(0x33FFFFFF) else Color(0x11FFFFFF)),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable(enabled = canGoPrev) { onPreviousStep() }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Previous",
                                    tint = if (canGoPrev) TextWhite else TextWhite.copy(alpha = 0.3f),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "← Previous",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (canGoPrev) TextWhite else TextWhite.copy(alpha = 0.3f)
                                )
                            }
                        }

                        val isLast = message.stepNumber >= totalSteps
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0x2210B981),
                            border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { onNextStep() }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isLast) "Finish 🏆" else "Continue →",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = EmeraldPrimary
                                )
                                if (!isLast) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "Continue",
                                        tint = EmeraldPrimary,
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
}

/**
 * AI Thinking Indicator Component
 */
@Composable
private fun AiThinkingIndicator(message: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "thinking")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0x2810B981),
        border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.45f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(6.dp, RoundedCornerShape(20.dp), spotColor = EmeraldPrimary.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0x3310B981)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "Thinking",
                    tint = EmeraldPrimary,
                    modifier = Modifier
                        .size(20.dp)
                        .scale(0.9f + alphaAnim * 0.18f)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = message,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Analyzing campaign strategies & preparing best answer...",
                    fontSize = 10.5.sp,
                    color = TextWhite.copy(alpha = 0.65f)
                )
            }
        }
    }
}

/**
 * Real Platform App Item Card
 */
@Composable
private fun RealPlatformCardItem(
    app: CreatorPlatformApp,
    onOpenPlayStore: (String) -> Unit,
    onOpenWebsite: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF141824),
        border = BorderStroke(1.dp, Color(0x22FFFFFF)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OfficialLogo(name = app.logoName, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = app.name, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(text = app.category, fontSize = 10.sp, color = EmeraldPrimary)
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x2210B981))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = app.badgeText, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = app.shortDesc, fontSize = 11.5.sp, color = TextWhite.copy(alpha = 0.8f), lineHeight = 16.sp)

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(EmeraldPrimary)
                        .clickable { onOpenPlayStore(app.playStorePackage) },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Storefront, contentDescription = "Play Store", tint = AmoledBlack, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Play Store 📱", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x22FFFFFF))
                        .clickable { onOpenWebsite(app.websiteUrl) },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.OpenInNew, contentDescription = "Website", tint = TextWhite, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Apply Online 🌐", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

/**
 * Pitch Coach Generator Composable View
 */
@Composable
private fun AiPitchCoachGeneratorView(
    userProfile: BrandCollabUserProfile,
    selectedChannel: String,
    targetBrand: String,
    onChannelChanged: (String) -> Unit,
    onTargetBrandChanged: (String) -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var generatedPitch by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "✉️ Personal Brand Pitch Generator", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Text(text = "Generates high-converting proposals tailored to your profile", fontSize = 11.5.sp, color = TextWhite.copy(alpha = 0.65f))

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Target Brand Name:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = targetBrand,
            onValueChange = { onTargetBrandChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = Color(0x33FFFFFF),
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            ),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Pitch Channel:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ChipPill("Instagram DM 📱", selectedChannel == "INSTAGRAM_DM") { onChannelChanged("INSTAGRAM_DM") }
            ChipPill("Email Pitch ✉️", selectedChannel == "EMAIL_PITCH") { onChannelChanged("EMAIL_PITCH") }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clip(RoundedCornerShape(21.dp))
                .background(EmeraldPrimary)
                .clickable {
                    scope.launch {
                        isGenerating = true
                        delay(900)
                        generatedPitch = if (selectedChannel == "INSTAGRAM_DM") {
                            "Hey ${targetBrand.ifBlank { "Brand" }} Team! 👋\nLoved your recent product drop! I create high-converting ${userProfile.niche} Reel content on ${userProfile.platform} with ${userProfile.followers} active followers.\nWould love to review your latest gear in a dedicated Reel! Can I share my media kit with you guys?"
                        } else {
                            "Subject: Brand Partnership Proposal — ${userProfile.niche} Creator x ${targetBrand.ifBlank { "Brand" }}\n\nDear ${targetBrand.ifBlank { "Brand" }} Marketing Team,\n\nI hope this email finds you well!\n\nMy name is Creator, and I run a growing ${userProfile.niche} channel on ${userProfile.platform} (${userProfile.followers} followers, 8.4% engagement rate).\n\nWe would love to showcase ${targetBrand.ifBlank { "Brand" }} to our audience via a high-impact Reel integration.\n\nAttached is our Media Kit. Looking forward to your thoughts!\n\nBest regards,\nCreator Team"
                        }
                        isGenerating = false
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isGenerating) "Generating Pitch..." else "Generate Custom Pitch ✨",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AmoledBlack
            )
        }

        if (generatedPitch.isNotBlank()) {
            Spacer(modifier = Modifier.height(14.dp))
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF141824),
                border = BorderStroke(1.dp, EmeraldPrimary)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("📋 Generated Pitch", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x2210B981))
                                .clickable {
                                    clipboardManager.setText(AnnotatedString(generatedPitch))
                                    Toast.makeText(context, "Copied Pitch to Clipboard!", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("1-Tap Copy 📋", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = generatedPitch, fontSize = 12.sp, color = TextWhite, lineHeight = 17.sp)
                }
            }
        }
    }
}

/**
 * Tab Selector Pill Composable
 */
@Composable
private fun TabPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) EmeraldPrimary else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) AmoledBlack else TextWhite.copy(alpha = 0.75f)
        )
    }
}

/**
 * Generic Chip Pill Option
 */
@Composable
private fun ChipPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) EmeraldPrimary else Color(0x22FFFFFF))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) AmoledBlack else TextWhite.copy(alpha = 0.75f)
        )
    }
}

/**
 * Fetch dynamic explanation using Gemini REST API or intelligent multi-lingual mentor generator
 */
private suspend fun fetchDynamicStepExplanation(
    stepIndex: Int,
    stepTitle: String,
    baseContent: String,
    userProfile: BrandCollabUserProfile,
    selectedLanguage: String,
    isSimpler: Boolean = false,
    giveExample: Boolean = false,
    styleFormat: String? = null,
    recentHistory: List<BrandMentorMessage> = emptyList()
): String = withContext(Dispatchers.IO) {
    val apiKey = try {
        val key = BuildConfig.GEMINI_API_KEY
        if (!key.isNullOrBlank() && key != "BUILDCONFIG_MISSING" && key != "null") key else System.getenv("GEMINI_API_KEY") ?: ""
    } catch (_: Exception) { "" }

    val randomNonce = System.currentTimeMillis() % 10000
    val availableStyles = listOf(
        "Story format with a real creator journey",
        "Beginner explanation with step-by-step points",
        "Real-life practical example (Email pitch, DM, Media Kit, or Contract)",
        "Flowchart style step-by-step breakdown",
        "Checklist format with clear actionable items",
        "Friendly mentor coach style with practical creator pro-tips"
    )
    val chosenStyle = styleFormat ?: availableStyles.random()

    if (apiKey.isNotBlank()) {
        try {
            val historySnippet = recentHistory.takeLast(4).joinToString("\n") { m ->
                if (m.isFromUser) "User: ${m.text}" else "Mentor: ${m.text.take(120)}"
            }

            val langDirective = when (selectedLanguage) {
                "Hindi" -> "Speak in warm, natural Devanagari Hindi."
                "English" -> "Speak in clear, encouraging conversational English."
                else -> "Speak in natural Hinglish (Hindi + English mix)."
            }

            val prompt = """
You are Brand Collaboration AI, an experienced senior creator coach mentoring a beginner one-on-one.
Tone: Friendly, patient, professional, motivating, practical, and human (NEVER robotic, dry, or textbook jargon).
Language: $langDirective

CRITICAL MANDATORY RULES:
1. NEVER REPEAT WORDING. Every reply MUST be newly generated with fresh phrasing. (Variation seed: $randomNonce)
2. EXPLANATION STYLE: Use this format/style: $chosenStyle.
3. REASONING & CONTEXT: Consider user profile (${userProfile.followers} followers in ${userProfile.niche} on ${userProfile.platform}) and lesson step ($stepIndex: $stepTitle).
4. SMART EXAMPLES: Include real Indian/global brands (e.g. Boat, Meesho, Snitch, Mamaearth, Minimalist, Nykaa, Amazon) and practical formats (Email pitch, DM, Media Kit, Rates, Contracts).
${if (giveExample) "5. EXPLICIT DEMAND: The user clicked 'Give Example'. Provide a clear, copyable real-life example (e.g., exact email draft, DM script, pricing card, or contract clause)." else ""}

Recent Chat History:
$historySnippet

Explain '$stepTitle' (Base Concept: $baseContent) in a fresh, engaging way.
""".trimIndent()

            val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
            val url = URL(endpoint)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.connectTimeout = 6000
            conn.readTimeout = 6000

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        })
                    })
                })
            }

            conn.outputStream.use { os ->
                os.write(jsonBody.toString().toByteArray(Charsets.UTF_8))
            }

            if (conn.responseCode == 200) {
                val responseStr = conn.inputStream.bufferedReader().use { it.readText() }
                val root = JSONObject(responseStr)
                val candidates = root.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val first = candidates.getJSONObject(0)
                    val content = first.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text")
                        if (text.isNotBlank()) {
                            return@withContext text.trim()
                        }
                    }
                }
            }
        } catch (_: Exception) {}
    }

    // Dynamic Multi-Style Local Mentor Engine (Fallback)
    val exampleSuffix = when (stepIndex) {
        1 -> "\n\n📝 Real DM Example:\n'Hey Boat Audio Team! 👋 I create tech gadget Reels on Instagram with 5k daily reach. Loved your new Airdopes 141. Would love to feature them in a dedicated Reel! Shall I share my Media Kit?'"
        2 -> "\n\n📝 Profile Bio Example:\n'🎥 Tech & Lifestyle Reviews\n📩 Brand Collabs: contact.rahul@gmail.com\n📍 Mumbai, India'"
        3 -> "\n\n📝 Micro-Brand Target List:\n• Boat Audio (@boatarat.audio)\n• Snitch Clothing (@snitch.co.in)\n• Minimalist Skincare (@beminimalist.co)"
        4 -> "\n\n📝 Platform Application Tip:\nOn Meesho Creator Program or Amazon Influencer, always link your highest-performing Reel in the application notes to get approved within 24 hours!"
        5 -> "\n\n✉️ Real Email Pitch Template:\nSubject: Reel Collaboration — ${userProfile.niche} Creator x Brand\nDear Marketing Team,\nI run a growing channel with ${userProfile.followers} followers on ${userProfile.platform}. We'd love to review your flagship product..."
        6 -> "\n\n📝 Follow-Up Script:\n'Hey! Following up on my previous message regarding the Reel collab. We're finalizing our content calendar for this month and would love to include you guys!'"
        7 -> "\n\n💰 Rate Card Example:\n• 1x Instagram Reel: ₹3,500\n• 2x Instagram Stories: ₹1,500\n• Paid Ad Usage Rights (30 Days): +50% (₹1,750)"
        8 -> "\n\n📝 Gift Upgrade Script:\n'Thank you! I'd love to share an unboxing Story for free. However, for a dedicated high-converting Reel, my standard fee is ₹3,000.'"
        9 -> "\n\n🛡️ Safety Contract Rule:\n1. Never click unknown verification links.\n2. Ensure 50% advance before posting.\n3. Verify sender email domain (@company.com)."
        else -> "\n\n📝 Post-Campaign Report Example:\n'Campaign Results: 42,000 Impressions, 3,800 Engagements, 412 Link Clicks. Looking forward to our next monthly retainer!'"
    }

    if (giveExample) {
        when (selectedLanguage) {
            "Hindi" -> "📝 व्यावहारिक उदाहरण (Practical Example):\n$exampleSuffix"
            "English" -> "📝 Practical Real-World Example:\n$exampleSuffix"
            else -> "📝 Practical Real-World Example:\n$exampleSuffix"
        }
    } else if (isSimpler) {
        when (selectedLanguage) {
            "Hindi" -> "💡 सरल शब्द (Simple Analogy):\nसोचिए जैसे आप किसी करीबी दोस्त को अपनी पसंदीदा बोट इयरफोन या कपड़े रिकमेंड कर रहे हैं! ब्रांड कोलैबोरेशन में बेचना नहीं है, बस ईमानदारी से दिखाना है।"
            "English" -> "💡 Simple Analogy:\nThink of brand collaboration like recommending your favorite gadget or outfit to a close friend. You aren't selling hard; you're naturally sharing value!"
            else -> "💡 Simple Analogy:\nBrand collab socho jaise apne best friend ko favorite gadget recommend karna. Sell nahi karna, bas natural style me show karna hai!"
        }
    } else {
        "$baseContent\n\n📌 Senior Coach Advice for ${userProfile.niche} (${userProfile.followers} on ${userProfile.platform}):\n• Focus on high engagement & clean presentation.\n• Consistency builds long-term brand trust!" + (if (kotlin.random.Random.nextBoolean()) exampleSuffix else "")
    }
}

/**
 * Generate Gemini response for freeform user queries with Language awareness
 */
private suspend fun generateGeminiMentorResponse(
    query: String,
    profile: BrandCollabUserProfile,
    selectedLanguage: String,
    recentHistory: List<BrandMentorMessage> = emptyList()
): String = withContext(Dispatchers.IO) {
    val apiKey = try {
        val key = BuildConfig.GEMINI_API_KEY
        if (!key.isNullOrBlank() && key != "BUILDCONFIG_MISSING" && key != "null") key else System.getenv("GEMINI_API_KEY") ?: ""
    } catch (_: Exception) { "" }

    val randomNonce = System.currentTimeMillis() % 10000

    if (apiKey.isNotBlank()) {
        try {
            val historySnippet = recentHistory.takeLast(4).joinToString("\n") { m ->
                if (m.isFromUser) "User: ${m.text}" else "Mentor: ${m.text.take(120)}"
            }

            val langDirective = when (selectedLanguage) {
                "Hindi" -> "Answer in warm, friendly Devanagari Hindi."
                "English" -> "Answer in clear, motivating conversational English."
                else -> "Answer in natural Hinglish (Hindi + English mix)."
            }

            val prompt = """
You are Brand Collaboration AI, an experienced senior creator coach.
Tone: Friendly, patient, professional, motivating, practical, and human (NEVER robotic or textbook).
Language: $langDirective

CRITICAL MANDATORY RULES:
1. NEVER REPEAT PREVIOUS ANSWERS WORD-FOR-WORD. Generate a newly phrased response. (Seed: $randomNonce)
2. THINK & ANALYZE: Consider creator profile (${profile.followers} followers in ${profile.niche} on ${profile.platform}) and context.
3. SMART EXAMPLES: Whenever possible, include real practical examples (e.g. Email pitch, DM script, Media kit, Pricing, Contract, Payment terms).

Recent Chat History:
$historySnippet

User Question:
$query
""".trimIndent()

            val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
            val url = URL(endpoint)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.connectTimeout = 6000
            conn.readTimeout = 6000

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        })
                    })
                })
            }

            conn.outputStream.use { os ->
                os.write(jsonBody.toString().toByteArray(Charsets.UTF_8))
            }

            if (conn.responseCode == 200) {
                val responseStr = conn.inputStream.bufferedReader().use { it.readText() }
                val root = JSONObject(responseStr)
                val candidates = root.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val first = candidates.getJSONObject(0)
                    val content = first.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text")
                        if (text.isNotBlank()) {
                            return@withContext text.trim()
                        }
                    }
                }
            }
        } catch (_: Exception) {}
    }

    // Fallback Answers
    when {
        query.contains("charge", ignoreCase = true) || query.contains("price", ignoreCase = true) || query.contains("rate", ignoreCase = true) ->
            "💰 Rate Guidance for ${profile.followers} Followers (${profile.niche}):\n• Standard Reel: ₹2,500 - ₹8,000\n• Dedicated Story Series: ₹1,000 - ₹3,000\n• Reel + 30-day Paid Ad Usage Rights: Add 50% extra usage fee!\n\n📝 Example Rate Card:\n'1x Reel (₹4,000) + 2x Stories (₹1,500) = Package Deal ₹5,000'"

        query.contains("contract", ignoreCase = true) || query.contains("scam", ignoreCase = true) ->
            "🛡️ Senior Coach Contract Rules:\n1. Re-shoots limited to maximum 2 minor edits.\n2. Require 50% advance payment before posting.\n3. Never click unknown verification links or share passwords.\n\n📝 Sample Contract Clause:\n'Payment: 50% upon script approval, 50% within 7 days of live post.'"

        else ->
            "✨ Senior Creator Pro Tip:\nAlways send a post-campaign performance summary to brand managers 7 days after publishing! Showing impressions and engagement turns one-off deals into recurring monthly retainers.\n\n📝 Sample Summary Note:\n'Hey Boat Team! Our Reel reached 38k viewers with 8.2% engagement! Excited for our next collab.'"
    }
}
