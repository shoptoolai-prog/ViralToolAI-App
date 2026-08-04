package com.example.creatoracademy

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.sin
import com.example.ui.components.SmartWelcomeBackDialog
import com.example.ui.components.RestartCourseConfirmDialog
import com.example.ui.components.LearningProgressIndicatorCard
import com.example.ui.components.OfficialLogo
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.material3.IconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.foundation.layout.offset
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
import androidx.compose.foundation.layout.wrapContentHeight
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

private val EmeraldPrimary = Color(0xFFE2E8F0) // Premium Soft Silver White
private val EmeraldGlow = Color(0x33E2E8F0) // Soft Silver Glass Glow
private val GreyButtonGradient = Brush.horizontalGradient(listOf(Color(0xFFF1F5F9), Color(0xFFCBD5E1), Color(0xFF94A3B8)))
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
 * MASTER PHASE 1 — BRAND COLLABORATION HUB
 * Complete Redesign with Premium Grey & Gold Theme, Animated Background Loop,
 * Onboarding Flow, Swipe Cards, AI Mentor Avatar, Language & Creator Type Selection,
 * Preview Roadmap, Feature Showcase, and Lifetime Coach Dashboard.
 */

val MASTER_PHASE1_WELCOME_STYLES = listOf(
    "🔥 Welcome aboard! Ready to land your first ₹50,000 paid brand deal?",
    "👋 Hey champ! I'm your Brand Collab Mentor. Let's convert your views into sponsorships!",
    "🚀 High energy today! Welcome to the Brand Collaboration Hub — let's build your media kit!",
    "💎 Welcome! Today is the day we elevate your profile to catch top brand managers' eyes.",
    "🎯 Greetings Creator! Brands are spending millions on influencers. Let's claim your share!",
    "✨ Welcome back! Ready to pitch your dream brands like Boat, Myntra, and Amazon?",
    "🌟 Namaste! Main hoon tumhara AI Brand Collab Coach. Sahi pitch likhna seekhein?",
    "💥 Welcome! Let me help transform your follower count into recurring monthly brand retainers.",
    "🏆 Hello superstar! Today we optimize your Bio & DM strategy for maximum replies.",
    "⚡ Welcome! Zero to sponsored creator roadmap starts right here. Let me guide you!",
    "👑 Hey creator boss! Let's craft a killer pitch deck that brands cannot refuse.",
    "🎁 Welcome! Turn gifted PR packages into high-paying commercial contracts today.",
    "💡 Hello! Did you know 90% of creators pitch wrong? Let's fix your pitch template now!",
    "💼 Welcome! Time to calculate your exact Reel & Story rate card based on real engagement.",
    "📈 Welcome aboard! Let me audit your Brand Ready Score and fix every bottleneck.",
    "🛡️ Welcome! Protect yourself from scam deal emails while landing legit ₹20k brand deals.",
    "🔮 Hey there! Your next brand deal is just one perfect pitch away. Ready to draft it?",
    "🤝 Welcome! Brands are searching for niche creators right now. Let's get you noticed!",
    "🎬 Welcome! From 1,000 views to paid sponsorships — let's unlock your earning potential.",
    "🛍️ Hello! Ready to collaborate with fashion, tech, and lifestyle brands like a pro?",
    "📊 Welcome! Let's build a data-driven Media Kit that impresses brand campaign managers.",
    "🚀 Welcome future top creator! Let's turn your passion into a profitable business.",
    "💯 Hey! No more guessing rates. Let's negotiate like a seasoned influencer manager.",
    "⭐ Welcome! Let's craft customized DMs for Instagram, YouTube, and Email outreach.",
    "🌟 Welcome! Learn how to close long-term monthly brand deals instead of one-time posts.",
    "📦 Welcome! Want to get free products AND get paid commercial fees? Let's begin!",
    "🎨 Welcome creator! Your unique style deserves top-tier brand partnerships.",
    "💬 Namaste! Aaj hum seekhenge brands ko DM karke instant response kaise paayein.",
    "🚀 Welcome! Let me personally guide your brand collab strategy step by step.",
    "🔥 Ready to level up? Let's make brands come directly to your DMs!",
    "💎 Welcome! Your brand deal journey begins with a solid profile foundation. Let's build!",
    "✨ Hello! Let's analyze your niche and target the highest-paying brand categories.",
    "👑 Welcome creator! Master the art of brand negotiation and contract protection.",
    "🎯 Welcome! Let's create an invoice and rate card that command respect and top pay.",
    "💼 Greetings! Ready to turn your content creation into a full-time brand business?",
    "📈 Welcome! High engagement + Smart outreach = Unlimited paid sponsorships!",
    "🌟 Hello! Let me help turn your social media into a brand attraction magnet.",
    "🛡️ Welcome! Learn the exact red flags to avoid fake brand sponsorship scams.",
    "🏆 Welcome superstar! Let's elevate your brand collaboration game to the top 1%!",
    "⚡ Welcome! Let me write a DM pitch that gets read and replied to within 24 hours!",
    "🎉 Welcome! Your personal AI Brand Mentor is active and ready. What shall we tackle first?"
)

@Composable
fun BrandCollabAnimatedBackgroundLoop() {
    val infiniteTransition = rememberInfiniteTransition(label = "bgLoop")
    val animProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "loopProgress"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF121216),
                        Color(0xFF1B1B22),
                        Color(0xFF121216)
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Draw glowing wave analytics graph line
            val graphPath = Path().apply {
                moveTo(0f, h * 0.72f)
                val curveHeight = h * 0.04f
                val offset = animProgress * 2 * Math.PI.toFloat()
                for (x in 0..w.toInt() step 15) {
                    val y = h * 0.72f + sin(x * 0.012f + offset) * curveHeight
                    lineTo(x.toFloat(), y)
                }
            }
            drawPath(
                path = graphPath,
                color = Color(0xFFFFD700).copy(alpha = 0.35f),
                style = Stroke(width = 3.5f)
            )

            // 2. Draw golden particles rising slowly
            val particleCount = 20
            for (i in 0 until particleCount) {
                val pX = (w * (i.toFloat() / particleCount) + (animProgress * 120f)) % w
                val pY = (h - ((animProgress * h + i * 45f) % h))
                val alpha = ((1f - (pY / h)) * 0.5f).coerceIn(0f, 0.75f)
                drawCircle(
                    color = Color(0xFFFFD700).copy(alpha = alpha),
                    radius = 2.5f + (i % 3),
                    center = Offset(pX, pY)
                )
            }
        }

        // 3. Floating Brand Logos & Floating Badges Overlays
        val floatingLogos = listOf(
            "📸" to Offset(0.10f, 0.12f),
            "▶️" to Offset(0.85f, 0.18f),
            "📦" to Offset(0.06f, 0.48f),
            "🛍️" to Offset(0.90f, 0.52f),
            "👗" to Offset(0.18f, 0.78f),
            "🎨" to Offset(0.82f, 0.82f),
            "🖌️" to Offset(0.48f, 0.06f)
        )

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val maxWidthPx = maxWidth.value
            val maxHeightPx = maxHeight.value

            floatingLogos.forEachIndexed { idx, (emoji, relPos) ->
                val floatOffsetY = (sin(animProgress * 2 * Math.PI.toFloat() + idx) * 10f)
                Box(
                    modifier = Modifier
                        .offset(
                            x = (maxWidthPx * relPos.x).dp,
                            y = (maxHeightPx * relPos.y + floatOffsetY).dp
                        )
                        .clip(CircleShape)
                        .background(Color(0x221E1E28))
                        .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.3f), CircleShape)
                        .padding(5.dp)
                ) {
                    Text(text = emoji, fontSize = 13.sp)
                }
            }

            // Floating Verified Badge & Handshake
            Box(
                modifier = Modifier
                    .offset(
                        x = (maxWidthPx * 0.45f).dp,
                        y = (maxHeightPx * 0.30f + sin(animProgress * 2 * Math.PI.toFloat()) * 12f).dp
                    )
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(Color(0x331E1E28))
                    .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f), CircleShape)
                    .padding(7.dp)
            ) {
                Text(text = "🤝", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun BrandCollabGlassHeader(
    onClose: () -> Unit,
    onReset: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "goldGlow")
    val lineAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lineGlow"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, shape = RoundedCornerShape(22.dp), spotColor = Color(0xFFFFD700)),
        shape = RoundedCornerShape(22.dp),
        color = Color(0x331E1E28),
        border = BorderStroke(
            1.5.dp,
            Brush.horizontalGradient(
                listOf(
                    Color(0xFFFFD700).copy(alpha = lineAlpha),
                    Color(0xFFFF8C00).copy(alpha = 0.6f),
                    Color(0xFFFFD700).copy(alpha = lineAlpha)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
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
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFFFF8C00))))
                            .shadow(8.dp, CircleShape, spotColor = Color(0xFFFFD700)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🤝", fontSize = 20.sp)
                    }

                    Column {
                        Text(
                            text = "Brand Collaboration Hub",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Build • Connect • Earn",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (onReset != null) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x22FFFFFF))
                                .clickable { onReset() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
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
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Animated Gold Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.5.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                Color(0xFFFFD700).copy(alpha = lineAlpha),
                                Color(0xFFFF8C00),
                                Color(0xFFFFD700).copy(alpha = lineAlpha),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}

@Composable
fun BrandCollabAiMentorAvatarCard(
    welcomeText: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "avatarBreathing")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(breathScale)
            .shadow(12.dp, shape = RoundedCornerShape(20.dp), spotColor = Color(0xFFFFD700)),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF1E1E26),
        border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(Color(0xFFFFD700), Color(0xFF282834))))
                    .border(2.dp, Color(0xFFFFD700), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🤖", fontSize = 28.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AI Brand Collab Mentor",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "✔️", fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = welcomeText,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.95f),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BrandCollabSwipeCardsSection() {
    val cards = listOf(
        SwipeCardData(
            emoji = "🤝",
            title = "What Is Brand Collaboration?",
            desc = "Professional brands creators ke saath products aur services promote karne ke liye collaborate karti hain.",
            badge = "CONCEPT"
        ),
        SwipeCardData(
            emoji = "💰",
            title = "How Creators Earn",
            desc = "Story Promotion, Reels, Posts, YouTube Videos, Affiliate, & Long-Term Deals.",
            badge = "MONETIZATION"
        ),
        SwipeCardData(
            emoji = "🚀",
            title = "Meet Your AI Mentor",
            desc = "Main sirf information nahi dunga. Main personally guide karunga step-by-step.",
            badge = "PERSONAL COACH"
        ),
        SwipeCardData(
            emoji = "🏆",
            title = "Everything You'll Learn",
            desc = "Profile Optimization, Brand Ready Score, Media Kit, Finding Brands, DMs, Negotiation, Contracts & Payments.",
            badge = "FULL ROADMAP"
        )
    )

    val pagerState = rememberPagerState(pageCount = { cards.size })

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
        ) { page ->
            val item = cards[page]
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp)
                    .shadow(10.dp, RoundedCornerShape(20.dp), spotColor = Color(0xFFFFD700)),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF242430),
                border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = item.emoji, fontSize = 30.sp)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFD700).copy(alpha = 0.15f))
                                .border(0.8.dp, Color(0xFFFFD700).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = item.badge,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = item.title,
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.desc,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Pager Indicator Dots
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(cards.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (isSelected) 20.dp else 7.dp, 7.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color(0xFFFFD700) else Color.White.copy(alpha = 0.3f))
                )
            }
        }
    }
}

data class SwipeCardData(
    val emoji: String,
    val title: String,
    val desc: String,
    val badge: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandCollabLanguagePickerSection(
    selectedLanguage: String,
    onSelectLanguage: (String) -> Unit
) {
    val languages = listOf("Hinglish", "Hindi", "English")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Select Language / भाषा चुनें",
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            languages.forEach { lang ->
                val isSelected = selectedLanguage == lang
                val flag = when (lang) {
                    "Hindi" -> "🇮🇳 Hindi"
                    "English" -> "🇺🇸 English"
                    else -> "🌐 Hinglish"
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelectLanguage(lang) },
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) Color(0xFFFFD700).copy(alpha = 0.2f) else Color(0x22FFFFFF),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) Color(0xFFFFD700) else Color.White.copy(alpha = 0.15f)
                    )
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = flag,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                            color = if (isSelected) Color(0xFFFFD700) else Color.White
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandCollabPersonalizationPickerSection(
    selectedCreatorType: String,
    onSelectCreatorType: (String) -> Unit
) {
    val types = listOf(
        "Student" to "🎓",
        "Content Creator" to "🎬",
        "Influencer" to "🌟",
        "YouTuber" to "📹",
        "Business Owner" to "💼",
        "Freelancer" to "💻",
        "Beginner" to "🚀",
        "Other" to "⭐"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "What best describes you?",
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700)
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            types.forEach { (label, emoji) ->
                val isSelected = selectedCreatorType == label
                Surface(
                    modifier = Modifier.clickable { onSelectCreatorType(label) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) Color(0xFFFFD700).copy(alpha = 0.22f) else Color(0x1AFFFFFF),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) Color(0xFFFFD700) else Color.White.copy(alpha = 0.15f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = emoji, fontSize = 14.sp)
                        Text(
                            text = label,
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color(0xFFFFD700) else Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BrandCollabPreviewRoadmapSection() {
    val timelineSteps = listOf(
        "Create Profile",
        "Become Brand Ready",
        "Get First Reply",
        "Close First Deal",
        "Become Pro Creator"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Preview Roadmap",
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            timelineSteps.forEachIndexed { idx, stepName ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF242430),
                    border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFD700)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${idx + 1}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                        Text(
                            text = stepName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                if (idx < timelineSteps.size - 1) {
                    Text(text = "➔", fontSize = 12.sp, color = Color(0xFFFFD700))
                }
            }
        }
    }
}

@Composable
fun BrandCollabFeatureShowcaseGrid() {
    val features = listOf(
        "AI Mentor" to "🤖",
        "AI Brand Score" to "🎯",
        "AI Media Kit" to "📄",
        "AI DM Generator" to "✉️",
        "AI Negotiation" to "🤝",
        "AI Contract Guide" to "📝",
        "AI Scam Detector" to "🛡️",
        "AI Payment Guide" to "💳"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Feature Showcase",
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            features.forEach { (title, emoji) ->
                Surface(
                    modifier = Modifier.width(110.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0x331E1E28),
                    border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = emoji, fontSize = 22.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = title,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
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

    var isEntranceVisible by remember { mutableStateOf(false) }
    var isIntroCompleted by remember { mutableStateOf(false) }

    var isOnboardingDone by remember {
        mutableStateOf(CreatorAcademyPrefs.isBrandCollabOnboardingDone(context))
    }
    var selectedLanguage by remember {
        mutableStateOf(CreatorAcademyPrefs.getBrandCollabLanguage(context).ifBlank { "Hinglish" })
    }
    var selectedCreatorType by remember {
        mutableStateOf(CreatorAcademyPrefs.getBrandCollabCreatorType(context).ifBlank { "Content Creator" })
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

    // Pitch Generator & Profile Analysis Mode Drawer
    val isProfileCompleted = remember { CreatorAcademyPrefs.isBrandCollabProfileCompleted(context) }
    val isLevel2Completed = remember { CreatorAcademyPrefs.isBrandCollabLevel2Completed(context) }
    val isLevel3Completed = remember { CreatorAcademyPrefs.isBrandCollabLevel3Completed(context) }
    val isLevel4Completed = remember { CreatorAcademyPrefs.isBrandCollabLevel4Completed(context) }
    val isLevel5Completed = remember { CreatorAcademyPrefs.isBrandCollabLevel5Completed(context) }
    val isLevel6Completed = remember { CreatorAcademyPrefs.isBrandCollabLevel6Completed(context) }
    val isLevel7Completed = remember { CreatorAcademyPrefs.isBrandCollabLevel7Completed(context) }
    val isLevel8Completed = remember { CreatorAcademyPrefs.isBrandCollabLevel8Completed(context) }
    val isLevel9Completed = remember { CreatorAcademyPrefs.isBrandCollabLevel9Completed(context) }
    val isLevel10Completed = remember { CreatorAcademyPrefs.isBrandCollabLevel10Completed(context) }
    val isLevel11Completed = remember { CreatorAcademyPrefs.isBrandCollabLevel11Completed(context) }
    val isLevel12Completed = remember { CreatorAcademyPrefs.isBrandCollabLevel12Completed(context) }
    val isLevel13Completed = remember { CreatorAcademyPrefs.isBrandCollabLevel13Completed(context) }
    val isPhase15Completed = remember { CreatorAcademyPrefs.isBrandCollabPhase15Completed(context) }
    var activeTab by remember {
        mutableStateOf(
            if (!isProfileCompleted) "LEVEL1_PROFILE"
            else if (!isLevel2Completed) "LEVEL2_PROFILE"
            else if (!isLevel3Completed) "LEVEL3_PROFILE"
            else if (!isLevel4Completed) "LEVEL4_RATECARD"
            else if (!isLevel5Completed) "LEVEL5_BRANDFINDER"
            else if (!isLevel6Completed) "LEVEL6_OUTREACH"
            else if (!isLevel7Completed) "LEVEL7_NEGOTIATION"
            else if (!isLevel8Completed) "LEVEL8_CONTRACT"
            else if (!isLevel9Completed) "LEVEL9_FINANCE"
            else if (!isLevel10Completed) "LEVEL10_CRM"
            else if (!isLevel11Completed) "LEVEL11_PLANNER"
            else if (!isLevel12Completed) "LEVEL12_PORTFOLIO"
            else if (!isLevel13Completed) "LEVEL13_DASHBOARD"
            else "PHASE15_SUCCESS_HUB"
        )
    } // LEVEL1_PROFILE, LEVEL2_PROFILE, LEVEL3_PROFILE, LEVEL4_RATECARD, LEVEL5_BRANDFINDER, LEVEL6_OUTREACH, LEVEL7_NEGOTIATION, LEVEL8_CONTRACT, LEVEL9_FINANCE, LEVEL10_CRM, MENTOR_CHAT, APPS_MARKET, PITCH_BUILDER, LANG_SETTINGS
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
                    color = Color(0xFF0F1118),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Background Loop
                        BrandCollabAnimatedBackgroundLoop()

                        if (!isOnboardingDone) {
                            // ==================================================
                            // MASTER PHASE 1 — NEW ONBOARDING FLOW
                            // ==================================================
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                BrandCollabGlassHeader(
                                    onClose = onDismiss
                                )

                                val welcomeGreeting = remember {
                                    MASTER_PHASE1_WELCOME_STYLES.random()
                                }

                                BrandCollabAiMentorAvatarCard(welcomeText = welcomeGreeting)

                                BrandCollabSwipeCardsSection()

                                BrandCollabLanguagePickerSection(
                                    selectedLanguage = selectedLanguage,
                                    onSelectLanguage = { lang ->
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        selectedLanguage = lang
                                    }
                                )

                                BrandCollabPersonalizationPickerSection(
                                    selectedCreatorType = selectedCreatorType,
                                    onSelectCreatorType = { type ->
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        selectedCreatorType = type
                                    }
                                )

                                BrandCollabPreviewRoadmapSection()

                                BrandCollabFeatureShowcaseGrid()

                                Spacer(modifier = Modifier.height(4.dp))

                                // Single Premium Glass Gold Button
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .shadow(12.dp, shape = RoundedCornerShape(18.dp), spotColor = Color(0xFFFFD700))
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            CreatorAcademyPrefs.setBrandCollabLanguage(context, selectedLanguage)
                                            CreatorAcademyPrefs.setBrandCollabCreatorType(context, selectedCreatorType)
                                            CreatorAcademyPrefs.setBrandCollabOnboardingDone(context, true)
                                            isOnboardingDone = true
                                            if (chatMessages.isEmpty()) {
                                                loadStepLesson(0)
                                            }
                                        },
                                    shape = RoundedCornerShape(18.dp),
                                    color = Color(0xFFFFD700),
                                    border = BorderStroke(1.5.dp, Color(0xFFFFF099))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 15.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = "Start Your Journey",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color.Black
                                            )
                                            Text(text = "🚀", fontSize = 18.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        } else {
                            // ==================================================
                            // MASTER PHASE 1 — MAIN WORKSPACE
                            // ==================================================
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                BrandCollabGlassHeader(
                                    onClose = onDismiss,
                                    onReset = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        CreatorAcademyPrefs.setBrandCollabOnboardingDone(context, false)
                                        isOnboardingDone = false
                                    }
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Tab Selector
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState())
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0x22FFFFFF))
                                        .padding(4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    TabPill("Level 1 📊", activeTab == "LEVEL1_PROFILE") {
                                        activeTab = "LEVEL1_PROFILE"
                                    }
                                    TabPill("Level 2 🎯", activeTab == "LEVEL2_PROFILE") {
                                        activeTab = "LEVEL2_PROFILE"
                                    }
                                    TabPill("Level 3 📁", activeTab == "LEVEL3_PROFILE") {
                                        activeTab = "LEVEL3_PROFILE"
                                    }
                                    TabPill("Level 4 💰", activeTab == "LEVEL4_RATECARD") {
                                        activeTab = "LEVEL4_RATECARD"
                                    }
                                    TabPill("Level 5 🔍", activeTab == "LEVEL5_BRANDFINDER") {
                                        activeTab = "LEVEL5_BRANDFINDER"
                                    }
                                    TabPill("Level 6 ✉️", activeTab == "LEVEL6_OUTREACH") {
                                        activeTab = "LEVEL6_OUTREACH"
                                    }
                                    TabPill("Level 7 🤝", activeTab == "LEVEL7_NEGOTIATION") {
                                        activeTab = "LEVEL7_NEGOTIATION"
                                    }
                                    TabPill("Level 8 📜", activeTab == "LEVEL8_CONTRACT") {
                                        activeTab = "LEVEL8_CONTRACT"
                                    }
                                    TabPill("Level 9 💰", activeTab == "LEVEL9_FINANCE") {
                                        activeTab = "LEVEL9_FINANCE"
                                    }
                                    TabPill("Level 10 📊", activeTab == "LEVEL10_CRM") {
                                        activeTab = "LEVEL10_CRM"
                                    }
                                    TabPill("Level 11 📋", activeTab == "LEVEL11_PLANNER") {
                                        activeTab = "LEVEL11_PLANNER"
                                    }
                                    TabPill("Level 12 💼", activeTab == "LEVEL12_PORTFOLIO") {
                                        activeTab = "LEVEL12_PORTFOLIO"
                                    }
                                    TabPill("Level 13 👑", activeTab == "LEVEL13_DASHBOARD") {
                                        activeTab = "LEVEL13_DASHBOARD"
                                    }
                                    TabPill("Success Hub 🏆", activeTab == "PHASE15_SUCCESS_HUB") {
                                        activeTab = "PHASE15_SUCCESS_HUB"
                                    }
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

                                if (chatMessages.isEmpty() && !showWelcomeBackDialog) {
                                    LaunchedEffect(Unit) {
                                        loadStepLesson(savedStepIndex.coerceAtLeast(0))
                                    }
                                }

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
                                    "LEVEL1_PROFILE" -> {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            BrandCollabLevel1ProfileAnalysisView(
                                                onProfileAnalysisCompleted = { updatedData ->
                                                    userProfile = userProfile.copy(
                                                        platform = updatedData["platform"] ?: "Instagram",
                                                        followers = updatedData["followers"] ?: "2K–10K",
                                                        niche = updatedData["niche"] ?: "Fashion",
                                                        experience = updatedData["level"] ?: "Beginner"
                                                    )
                                                    activeTab = "LEVEL2_PROFILE"
                                                    Toast.makeText(context, "Level 1 Completed! Moving to Level 2 🚀", Toast.LENGTH_SHORT).show()
                                                },
                                                onBack = {
                                                    activeTab = "MENTOR_CHAT"
                                                }
                                            )
                                        }
                                    }

                                    "LEVEL2_PROFILE" -> {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            BrandCollabLevel2BecomeBrandReadyView(
                                                userNiche = userProfile.niche,
                                                userPlatform = userProfile.platform,
                                                onLevel2Completed = {
                                                    activeTab = "LEVEL3_PROFILE"
                                                    Toast.makeText(context, "Level 2 Completed! Starting Level 3 AI Media Kit Builder 🚀", Toast.LENGTH_SHORT).show()
                                                },
                                                onBack = {
                                                    activeTab = "LEVEL1_PROFILE"
                                                }
                                            )
                                        }
                                    }

                                    "LEVEL3_PROFILE" -> {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            BrandCollabLevel3AIMediaKitView(
                                                userNiche = userProfile.niche,
                                                userPlatform = userProfile.platform,
                                                onLevel3Completed = {
                                                    activeTab = "LEVEL4_RATECARD"
                                                    Toast.makeText(context, "Level 3 Completed! Starting Level 4 AI Rate Card Builder 🚀", Toast.LENGTH_SHORT).show()
                                                },
                                                onBack = {
                                                    activeTab = "LEVEL2_PROFILE"
                                                }
                                            )
                                        }
                                    }

                                    "LEVEL4_RATECARD" -> {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            BrandCollabLevel4AIRateCardView(
                                                userNiche = userProfile.niche,
                                                userPlatform = userProfile.platform,
                                                onLevel4Completed = {
                                                    activeTab = "LEVEL5_BRANDFINDER"
                                                    Toast.makeText(context, "Level 4 Completed! Moving to Level 5 AI Brand Finder 🚀", Toast.LENGTH_SHORT).show()
                                                },
                                                onBack = {
                                                    activeTab = "LEVEL3_PROFILE"
                                                }
                                            )
                                        }
                                    }

                                    "LEVEL5_BRANDFINDER" -> {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            BrandCollabLevel5AIBrandFinderView(
                                                userNiche = userProfile.niche,
                                                userPlatform = userProfile.platform,
                                                onLevel5Completed = {
                                                    activeTab = "LEVEL6_OUTREACH"
                                                    Toast.makeText(context, "Level 5 Completed! Moving to Level 6 AI Outreach Master 🚀", Toast.LENGTH_SHORT).show()
                                                },
                                                onBack = {
                                                    activeTab = "LEVEL4_RATECARD"
                                                }
                                            )
                                        }
                                    }

                                    "LEVEL6_OUTREACH" -> {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            BrandCollabLevel6AIOutreachMasterView(
                                                userNiche = userProfile.niche,
                                                userPlatform = userProfile.platform,
                                                userName = "Creator",
                                                onLevel6Completed = {
                                                    activeTab = "LEVEL7_NEGOTIATION"
                                                    Toast.makeText(context, "Level 6 Completed! Moving to Level 7 AI Negotiation Master 🤝", Toast.LENGTH_SHORT).show()
                                                },
                                                onBack = {
                                                    activeTab = "LEVEL5_BRANDFINDER"
                                                }
                                            )
                                        }
                                    }

                                    "LEVEL7_NEGOTIATION" -> {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            BrandCollabLevel7AINegotiationMasterView(
                                                userNiche = userProfile.niche,
                                                userPlatform = userProfile.platform,
                                                userName = "Creator",
                                                onLevel7Completed = {
                                                    activeTab = "LEVEL8_CONTRACT"
                                                    Toast.makeText(context, "Level 7 Completed! Moving to Level 8 AI Contract & Legal Guide 📜", Toast.LENGTH_SHORT).show()
                                                },
                                                onBack = {
                                                    activeTab = "LEVEL6_OUTREACH"
                                                }
                                            )
                                        }
                                    }

                                    "LEVEL8_CONTRACT" -> {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            BrandCollabLevel8AIContractLegalGuideView(
                                                userNiche = userProfile.niche,
                                                userPlatform = userProfile.platform,
                                                userName = "Creator",
                                                onLevel8Completed = {
                                                    activeTab = "LEVEL9_FINANCE"
                                                    Toast.makeText(context, "Level 8 Completed! Moving to Level 9 AI Payment & Finance Hub 💰", Toast.LENGTH_SHORT).show()
                                                },
                                                onBack = {
                                                    activeTab = "LEVEL7_NEGOTIATION"
                                                }
                                            )
                                        }
                                    }

                                    "LEVEL9_FINANCE" -> {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            BrandCollabLevel9AIPaymentFinanceHubView(
                                                userNiche = userProfile.niche,
                                                userPlatform = userProfile.platform,
                                                userName = "Creator",
                                                onLevel9Completed = {
                                                    activeTab = "LEVEL10_CRM"
                                                    Toast.makeText(context, "Level 9 Completed! Finance Ready Creator Status Unlocked 💰🎉", Toast.LENGTH_SHORT).show()
                                                },
                                                onBack = {
                                                    activeTab = "LEVEL8_CONTRACT"
                                                }
                                            )
                                        }
                                    }

                                    "PHASE15_SUCCESS_HUB" -> {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            BrandCollabPhase15CreatorSuccessHubView(
                                                userNiche = userProfile.niche,
                                                userPlatform = userProfile.platform,
                                                userName = "Creator",
                                                onRestartFullCourse = {
                                                    isOnboardingDone = false
                                                    activeTab = "LEVEL1_PROFILE"
                                                },
                                                onBack = {
                                                    activeTab = "LEVEL13_DASHBOARD"
                                                }
                                            )
                                        }
                                    }

                                    "LEVEL13_DASHBOARD" -> {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            BrandCollabLevel13BusinessDashboardView(
                                                userNiche = userProfile.niche,
                                                userPlatform = userProfile.platform,
                                                userName = "Creator",
                                                onLevel13Completed = {
                                                    activeTab = "PHASE15_SUCCESS_HUB"
                                                    Toast.makeText(context, "Level 13 Completed! Creator Business Manager Status Unlocked 🏆🎉", Toast.LENGTH_SHORT).show()
                                                },
                                                onBack = {
                                                    activeTab = "LEVEL12_PORTFOLIO"
                                                }
                                            )
                                        }
                                    }

                                    "LEVEL12_PORTFOLIO" -> {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            BrandCollabLevel12AIPortfolioBuilderView(
                                                userNiche = userProfile.niche,
                                                userPlatform = userProfile.platform,
                                                userName = "Creator",
                                                onLevel12Completed = {
                                                    activeTab = "LEVEL13_DASHBOARD"
                                                    Toast.makeText(context, "Level 12 Completed! Creator Portfolio Master Status Unlocked 🏆🎉", Toast.LENGTH_SHORT).show()
                                                },
                                                onBack = {
                                                    activeTab = "LEVEL11_PLANNER"
                                                }
                                            )
                                        }
                                    }

                                    "LEVEL11_PLANNER" -> {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            BrandCollabLevel11AICampaignPlannerView(
                                                userNiche = userProfile.niche,
                                                userPlatform = userProfile.platform,
                                                userName = "Creator",
                                                onLevel11Completed = {
                                                    activeTab = "LEVEL12_PORTFOLIO"
                                                    Toast.makeText(context, "Level 11 Completed! Campaign Manager Status Unlocked 🏆🎉", Toast.LENGTH_SHORT).show()
                                                },
                                                onBack = {
                                                    activeTab = "LEVEL10_CRM"
                                                }
                                            )
                                        }
                                    }

                                    "LEVEL10_CRM" -> {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            BrandCollabLevel10CreatorCRMView(
                                                userNiche = userProfile.niche,
                                                userPlatform = userProfile.platform,
                                                userName = "Creator",
                                                onLevel10Completed = {
                                                    activeTab = "LEVEL11_PLANNER"
                                                    Toast.makeText(context, "Level 10 Completed! Creator CRM Master Status Unlocked 🏆🎉", Toast.LENGTH_SHORT).show()
                                                },
                                                onBack = {
                                                    activeTab = "LEVEL9_FINANCE"
                                                }
                                            )
                                        }
                                    }

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
                                                            .background(EmeraldPrimary.copy(alpha = 0.15f))
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
                                                                    currentStepIndex = BrandCollabStaticData.guidedLessonsV2.size
                                                                    Toast.makeText(context, "🏆 Course Completed! Mastered Brand Deals!", Toast.LENGTH_LONG).show()
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
}


val MASTER_PHASE2_MENTOR_REPLIES = listOf(
    "Awesome! Ab main tumhare creator profile ko analyse karunga. Uske baad tumhare liye ek personalized Brand Collaboration Roadmap banaunga.",
    "Super excited! Let's analyze your creator stats now. Next, I'll generate a custom ₹50k brand deal roadmap for you!",
    "Great work! Ab tumhari profile reach aur niche audit karte hain. Iske baad aagaya tumhara step-by-step pitch plan!",
    "Fantastic! Pehle main tumhare followers aur engagement rate ko evaluate karunga, fir personalized brand list prepare hogi.",
    "Let's go! Ab tumhare creator level ka complete AI analysis hoga. Taiyar ho apne pehle paid brand campaign ke liye?",
    "Brilliant choice! Main tumhare main platform aur niche ka deep breakdown karke, tumhara Brand Readiness Index calculate karunga.",
    "Awesome progress! Profile analysis complete hote hi tumhein milaga tumhara Daily Mission + Customized Collab Roadmap.",
    "Perfect! Ab hum tumhare exact growth bottlenecks target karenge taaki top brand managers direct DM mein reply karein.",
    "Welcoming your creator entry! Let's inspect your current stage and unlock your first +100 XP Brand Badge!",
    "Top class! Ab main analyse karunga ki tumhara content kitna brand-friendly hai aur kitni pricing quote karni chahiye.",
    "Namaste Creator! Let's scan your Instagram & YouTube potential to land sponsorships from brands like Boat & Nykaa.",
    "Woohoo! Step 1 profile analysis starts now. Answer 6 quick questions to receive your tailored brand pitch roadmap.",
    "Shabash! Pehle tumhare channel/page ka readiness level calculate karte hain. Tabhi toh ₹20k-₹50k rates demand karoge!",
    "Ready to level up? Let's check your platform focus and follower tier to optimize your media kit pitch strategy.",
    "Welcome to Level 1! Main tumhare niche potential, audience trust aur pricing power ko scientifically measure karunga.",
    "Awesome energy! Zero knowledge se brand pro banne ka raasta profile audit se shuru hota hai. Chaliye shuru karein!",
    "Hey Rockstar! Give me 60 seconds to analyze your profile metrics, and I'll map out your exact brand deal steps.",
    "Outstanding! Profile analysis done hone ke baad tumhein Creator Readiness Score (0-100) aur Daily Mission milega.",
    "Let's turn those views into monthly brand retainers! Let's analyze your platform, niche, and main challenges now.",
    "Aapka personal AI Brand Mentor ready hai! Chaliye 6 steps me aapke creator profile ki puri SWOT analysis karte hain.",
    "Big moves ahead! I'm going to scan your creator niche and generate an 8-stage personalized brand execution plan.",
    "Ready for sponsorship success? Let's analyze where you stand today so we can target high-budget brand deals.",
    "Ekdam sahi direction! Profile analyze karne se pata chalega ki aapko pehle Bio fix karna hai ya Direct Outreach.",
    "Brilliant! Let's evaluate your primary platform, goal, and biggest obstacle to build a bulletproof pitch deck.",
    "Zero to Sponsored Creator journey! First, let's establish your Creator Profile Baseline with this 6-step AI audit.",
    "Hello Champ! Aaj hum tumhara Trust Score, Brand Potential aur Negotiation Tier calculate karne wale hain.",
    "Full power! 6 simple questions, and boom — your customized Brand Collaboration Roadmap will be ready!",
    "Zabardast! Ab tumhare account ki brand valuation aur pricing strategy prepare hogi. Let me analyze your inputs.",
    "Pro level thinking! Analyzing your main platform and follower count now to match you with top verified marketplaces.",
    "Awesome creator mindset! Let's execute this Level 1 profile scan to unlock your Creator Analysis Completed Badge!",
    "Hello Superstar! Profile audit shuru karte hain taaki aapko pata chale ki Boat, Mamaearth aur Snitch se deals kaise lein.",
    "Great determination! Answering these 6 questions unlocks your 8-stage step-by-step brand collaboration roadmap.",
    "AI Mentor active! Let's audit your niche marketability, estimated deal pricing, and audience engagement strength.",
    "Welcome to Phase 2 Level 1! Let's build your verified creator profile so brand campaign managers take you seriously.",
    "Target unlocked! 6 steps in, and I'll reveal your Creator Readiness Score with glowing personalized analytics.",
    "Kamaal ka decision! Let me evaluate your primary platform and biggest problem to craft tailored DM scripts.",
    "Ready to pitch like a pro? Let me first map your current follower count and creator level into an actionable plan.",
    "High value creator incoming! Let me analyze your niche and goals to give you today's +100 XP Daily Mission.",
    "Welcome! Pehle profile analyze karenge, fir personalized media kit templates aur brand application links denge.",
    "Game-changer moment! Let's run a instant AI scan on your main channel type and target brand deal milestones.",
    "Sahi pakde hain! Profile analysis gives us the exact baseline needed to negotiate ₹10,000+ per Instagram Reel.",
    "Greetings! Let's kick off your creator profiling to diagnose why brands haven't replied to your previous pitches.",
    "Let's fix every bottleneck! 6 quick steps to identify your growth potential, trust score, and negotiation strategy.",
    "Powerful start! Your inputs will train my AI model to suggest exact email pitch subject lines for your niche.",
    "Phenomenal! Let me inspect your main platform and follower tier so we can calculate your customized Rate Card.",
    "Welcome Creator! Profile analysis is 5% of your total course, but it sets up 100% of your sponsorship success.",
    "Exciting times! Let's build your creator profile card and unlock your personalized 8-step roadmap timeline.",
    "Ek number choice! 6 steps complete karo, aur dekho tumhara AI Readiness Score 0 se 100 tak kaise jump karta hai.",
    "Master Creator Mode ON! Analyzing your channel niche and level to assign your very first 8-minute Daily Mission.",
    "Aagaye aap! Chaliye aapke creator account ko brand-ready banane ki pehli seedi profile analysis se shuru karte hain.",
    "High performance AI loading! Answering these 6 choices helps me customize your entire Brand Collaboration Hub.",
    "Let's get those paid deals! First, let me scan your platform preference and primary goal in this quick AI questionnaire.",
    "Awesome! Profile audit se pata chalega ki aap Fashion, Tech ya Lifestyle me kis brand manager ko pitch kar sakte hain.",
    "Super clean execution! Let's analyze your follower tier and biggest problem to give you direct solutions.",
    "Welcome onboard! I'm ready to evaluate your creator metrics and craft your step-by-step outreach blueprint.",
    "Bada socho! Profile analysis is your first official milestone in landing commercial brand sponsorship contracts.",
    "Solid start! Let's analyze your primary platform, follower base, and main goals to unlock your +100 XP Reward.",
    "Ready to claim your spot in the creator economy? Let me analyze your creator profile and build your roadmap now!",
    "Ultimate Creator Blueprint! 6 simple clicks to analyze your readiness and generate your personalized timeline.",
    "Chaliye shuru karte hain! Profile analysis completed hote hi tumhara personal Brand Collaboration Hub active ho jayega."
)

/**
 * Premium 5% Animated Progress Ring Component
 */
@Composable
fun BrandCollabProgressRing(
    progressPercent: Float = 0.05f,
    displayText: String = "5%",
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ring_glow")
    val rotationAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Restart),
        label = "rotation"
    )

    val pulseAnim by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(1400, easing = LinearEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    Box(
        modifier = modifier.size(68.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 5.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2
            val center = Offset(size.width / 2, size.height / 2)

            drawCircle(
                color = Color(0x33FFD700),
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidth)
            )

            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFFFFD700),
                        Color(0xFFFFF099),
                        Color(0xFFE2E8F0),
                        Color(0xFFFFD700)
                    )
                ),
                startAngle = -90f + rotationAnim,
                sweepAngle = 360f * progressPercent,
                useCenter = false,
                style = Stroke(width = strokeWidth + 1.dp.toPx())
            )
        }

        Surface(
            shape = CircleShape,
            color = Color(0xFF0D131A),
            border = BorderStroke(1.dp, Color(0x66FFD700)),
            modifier = Modifier
                .size(52.dp)
                .scale(pulseAnim)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = displayText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFD700)
                    )
                    Text(
                        text = "LEVEL 1",
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

/**
 * MASTER PHASE 2 — LEVEL 1 CREATOR PROFILE ANALYSIS VIEW
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandCollabLevel1ProfileAnalysisView(
    onProfileAnalysisCompleted: (Map<String, String>) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val savedProfileData = remember { CreatorAcademyPrefs.getBrandCollabProfileData(context) }
    var isAlreadyCompleted by remember { mutableStateOf(savedProfileData["completed"] as? Boolean ?: false) }

    var currentStep by remember { mutableIntStateOf(1) }
    var selectedPlatform by remember { mutableStateOf(savedProfileData["platform"] as? String ?: "Instagram") }
    var selectedFollowers by remember { mutableStateOf(savedProfileData["followers"] as? String ?: "2K–10K") }
    var customFollowersText by remember { mutableStateOf("") }
    var selectedNiche by remember { mutableStateOf(savedProfileData["niche"] as? String ?: "Fashion") }
    var selectedLevel by remember { mutableStateOf(savedProfileData["level"] as? String ?: "Beginner") }
    var selectedGoal by remember { mutableStateOf(savedProfileData["goal"] as? String ?: "Get My First Brand Deal") }
    var selectedProblem by remember { mutableStateOf(savedProfileData["problem"] as? String ?: "No Brand Replies") }

    var isAnalyzing by remember { mutableStateOf(false) }
    var scanMessageIndex by remember { mutableIntStateOf(0) }
    var calculatedScore by remember { mutableIntStateOf(78) }

    val mentorMessage = remember { MASTER_PHASE2_MENTOR_REPLIES.random() }

    val scanMessages = listOf(
        "⚡ Scanning profile parameters across platforms...",
        "📊 Calculating audience engagement potential...",
        "💡 Matching brand categories for ${selectedNiche}...",
        "🏆 Evaluating Creator Readiness Index (0–100)...",
        "🎯 Generating personalized 8-step Brand Collab Roadmap..."
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0x22121824),
                border = BorderStroke(
                    1.5.dp,
                    Brush.horizontalGradient(
                        listOf(Color(0xFFFFD700).copy(alpha = 0.6f), Color(0x33FFFFFF), Color(0xFFFFD700).copy(alpha = 0.4f))
                    )
                ),
                shadowElevation = 10.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Brand Collaboration Hub",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "👑", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Let's Build Your Creator Profile",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFFD700)
                            )
                        }

                        BrandCollabProgressRing(
                            progressPercent = if (isAlreadyCompleted) 1.0f else (currentStep.toFloat() / 6f) * 0.05f + 0.05f,
                            displayText = if (isAlreadyCompleted) "100%" else "5%"
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, Color(0xFFFFD700), Color.Transparent)
                                )
                            )
                    )
                }
            }

            val infiniteTransition = rememberInfiniteTransition(label = "avatar_breath")
            val breathScale by infiniteTransition.animateFloat(
                initialValue = 0.97f,
                targetValue = 1.03f,
                animationSpec = infiniteRepeatable(tween(1600, easing = LinearEasing), RepeatMode.Reverse),
                label = "breath"
            )

            Surface(
                shape = RoundedCornerShape(22.dp),
                color = Color(0x1A1E2638),
                border = BorderStroke(1.dp, Color(0x33FFD700)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .scale(breathScale)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFFFFD700).copy(alpha = 0.35f), Color.Transparent)
                                )
                            )
                            .border(BorderStroke(1.5.dp, Color(0xFFFFD700)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🤖", fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AI Mentor Says",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD700)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "✨", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (isAlreadyCompleted)
                                "Awesome! Tumhara Creator Profile complete ho gaya hai. Yahan tumhari detailed readiness analytics aur personalized roadmap hai."
                            else
                                mentorMessage,
                            fontSize = 12.5.sp,
                            color = TextWhite,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            if (isAnalyzing) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF101726),
                    border = BorderStroke(1.5.dp, Color(0xFFFFD700)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color(0x22FFD700))
                                .border(BorderStroke(2.dp, Color(0xFFFFD700)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🔍", fontSize = 42.sp)
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Analyzing Creator Profile...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextWhite
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = scanMessages.getOrElse(scanMessageIndex) { scanMessages.first() },
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFFFD700),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else if (isAlreadyCompleted) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = Color(0x22182234),
                        border = BorderStroke(1.5.dp, Color(0xFFFFD700)),
                        shadowElevation = 8.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "CREATOR READINESS SCORE",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFFD700),
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier.size(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val strokeWidth = 10.dp.toPx()
                                    val radius = (size.minDimension - strokeWidth) / 2
                                    val center = Offset(size.width / 2, size.height / 2)

                                    drawCircle(
                                        color = Color(0x33FFFFFF),
                                        radius = radius,
                                        center = center,
                                        style = Stroke(width = strokeWidth)
                                    )

                                    drawArc(
                                        brush = Brush.sweepGradient(
                                            colors = listOf(Color(0xFFFFD700), Color(0xFFFFF099), Color(0xFFFFD700))
                                        ),
                                        startAngle = -90f,
                                        sweepAngle = 360f * (calculatedScore / 100f),
                                        useCenter = false,
                                        style = Stroke(width = strokeWidth + 1.dp.toPx())
                                    )
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$calculatedScore",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = "/ 100",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFD700)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0x33FFD700),
                                border = BorderStroke(1.dp, Color(0xFFFFD700))
                            ) {
                                Text(
                                    text = "🔥 Brand Ready Tier",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD700),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricGlassCard(
                            title = "Brand Potential",
                            value = "85% 🔥",
                            subtitle = "High Demand",
                            modifier = Modifier.weight(1f)
                        )
                        MetricGlassCard(
                            title = "Growth Potential",
                            value = "92% 🚀",
                            subtitle = "Viral Reach",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricGlassCard(
                            title = "Negotiation Level",
                            value = selectedLevel,
                            subtitle = "Rate Power",
                            modifier = Modifier.weight(1f)
                        )
                        MetricGlassCard(
                            title = "Trust Score",
                            value = "88% 🛡️",
                            subtitle = "Verified",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = Color(0x1A141A29),
                        border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "🗺️ Your Personalized Roadmap",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "8 Steps",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD700)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            val roadmapMilestones = listOf(
                                Pair("Optimize Profile", "⚙️ Fix Bio, Links & Highlights"),
                                Pair("Become Brand Ready", "🚀 Create High-Quality Content"),
                                Pair("Create Media Kit", "📄 Professional Rate Card & Stats"),
                                Pair("Find Brands", "🔎 Direct Marketplaces & Agencies"),
                                Pair("Professional Outreach", "✉️ DM & Email Pitch Scripts"),
                                Pair("Negotiate", "🤝 50% Advance & Usage Rights"),
                                Pair("Close First Deal", "💰 Finalize Contract & Post Reel"),
                                Pair("Become Premium Creator", "👑 Monthly Paid Retainers")
                            )

                            roadmapMilestones.forEachIndexed { index, (title, sub) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(if (index == 0) Color(0xFFFFD700) else Color(0x33FFFFFF))
                                                .border(
                                                    BorderStroke(1.5.dp, if (index == 0) Color.White else Color(0x44FFFFFF)),
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (index == 0) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Active",
                                                    tint = Color.Black,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                            } else {
                                                Text(text = "${index + 1}", fontSize = 10.sp, color = TextWhite)
                                            }
                                        }

                                        if (index < roadmapMilestones.size - 1) {
                                            Box(
                                                modifier = Modifier
                                                    .width(2.dp)
                                                    .height(26.dp)
                                                    .background(if (index == 0) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = title,
                                            fontSize = 13.sp,
                                            fontWeight = if (index == 0) FontWeight.ExtraBold else FontWeight.SemiBold,
                                            color = if (index == 0) Color(0xFFFFD700) else TextWhite
                                        )
                                        Text(
                                            text = sub,
                                            fontSize = 10.5.sp,
                                            color = TextWhite.copy(alpha = 0.65f)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0x22FFD700),
                        border = BorderStroke(1.2.dp, Color(0xFFFFD700)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "🎯 TODAY'S MISSION",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFFD700)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Complete Creator Analysis",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "Estimated Time: 8 Minutes",
                                    fontSize = 11.sp,
                                    color = TextWhite.copy(alpha = 0.7f)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFFD700)
                            ) {
                                Text(
                                    text = "+100 XP ✓",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0x1A10281A),
                        border = BorderStroke(1.5.dp, Color(0xFF4ADE80)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🏆", fontSize = 32.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Creator Analysis Completed",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF4ADE80)
                                )
                                Text(
                                    text = "Badge unlocked & saved to profile memory!",
                                    fontSize = 11.5.sp,
                                    color = TextWhite.copy(alpha = 0.75f)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0x22FFFFFF))
                                .border(BorderStroke(1.dp, Color(0x44FFFFFF)), RoundedCornerShape(24.dp))
                                .clickable {
                                    isAlreadyCompleted = false
                                    currentStep = 1
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Restart Audit 🔄",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1.3f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xFFFFD700))
                                .clickable {
                                    onProfileAnalysisCompleted(
                                        mapOf(
                                            "platform" to selectedPlatform,
                                            "followers" to if (selectedFollowers == "Custom") customFollowersText.ifBlank { "Custom" } else selectedFollowers,
                                            "niche" to selectedNiche,
                                            "level" to selectedLevel,
                                            "goal" to selectedGoal,
                                            "problem" to selectedProblem
                                        )
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Continue Learning ➔",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(26.dp),
                    color = Color(0x1E121824),
                    border = BorderStroke(1.5.dp, Color(0x33FFD700)),
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "STEP $currentStep OF 6",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFFD700),
                                letterSpacing = 1.sp
                            )

                            Text(
                                text = "${((currentStep / 6f) * 100).toInt()}% Done",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite.copy(alpha = 0.6f)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        when (currentStep) {
                            1 -> {
                                Text(
                                    text = "Choose Your Main Platform",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(14.dp))

                                val platforms = listOf(
                                    Triple("Instagram", "Instagram", "📸"),
                                    Triple("YouTube", "YouTube", "🎬"),
                                    Triple("Facebook", "Facebook", "📘"),
                                    Triple("LinkedIn", "LinkedIn", "💼"),
                                    Triple("Multiple Platforms", "Multiple Platforms", "🌐")
                                )

                                platforms.forEach { (code, name, icon) ->
                                    SelectableGlassCard(
                                        title = name,
                                        icon = icon,
                                        isSelected = selectedPlatform == code,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            selectedPlatform = code
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            2 -> {
                                Text(
                                    text = "Select Your Follower Count",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(14.dp))

                                val followerRanges = listOf("0–500", "500–2K", "2K–10K", "10K–50K", "50K+", "Custom")

                                followerRanges.forEach { range ->
                                    SelectableGlassCard(
                                        title = range,
                                        icon = "👥",
                                        isSelected = selectedFollowers == range,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            selectedFollowers = range
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                if (selectedFollowers == "Custom") {
                                    OutlinedTextField(
                                        value = customFollowersText,
                                        onValueChange = { customFollowersText = it },
                                        placeholder = { Text("Enter exact follower count (e.g., 7.5K)", color = TextWhite.copy(alpha = 0.4f), fontSize = 12.sp) },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFFFFD700),
                                            unfocusedBorderColor = Color(0x33FFFFFF),
                                            focusedTextColor = TextWhite,
                                            unfocusedTextColor = TextWhite
                                        ),
                                        shape = RoundedCornerShape(14.dp),
                                        singleLine = true
                                    )
                                }
                            }

                            3 -> {
                                Text(
                                    text = "Select Your Content Niche",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(14.dp))

                                val niches = listOf(
                                    "Fashion", "Beauty", "Gaming", "Technology", "Education",
                                    "Finance", "Fitness", "Food", "Travel", "Lifestyle",
                                    "Comedy", "Business", "Photography", "Other"
                                )

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    niches.forEach { niche ->
                                        val isSel = selectedNiche == niche
                                        Surface(
                                            modifier = Modifier.clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                selectedNiche = niche
                                            },
                                            shape = RoundedCornerShape(16.dp),
                                            color = if (isSel) Color(0x33FFD700) else Color(0x18FFFFFF),
                                            border = BorderStroke(
                                                if (isSel) 1.5.dp else 1.dp,
                                                if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF)
                                            )
                                        ) {
                                            Text(
                                                text = niche,
                                                fontSize = 12.5.sp,
                                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSel) Color(0xFFFFD700) else TextWhite,
                                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            4 -> {
                                Text(
                                    text = "Select Your Creator Level",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(14.dp))

                                val levels = listOf(
                                    Pair("Beginner", "Zero brand experience, just starting out"),
                                    Pair("Intermediate", "Done 1-3 gifted collabs or small deals"),
                                    Pair("Advanced", "Regularly posting sponsored content"),
                                    Pair("Professional", "Full-time creator with rate cards")
                                )

                                levels.forEach { (lvl, desc) ->
                                    SelectableGlassCard(
                                        title = lvl,
                                        subtitle = desc,
                                        icon = "🌟",
                                        isSelected = selectedLevel == lvl,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            selectedLevel = lvl
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            5 -> {
                                Text(
                                    text = "What Is Your Primary Goal?",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(14.dp))

                                val goals = listOf(
                                    "Get My First Brand Deal",
                                    "Earn Monthly Income",
                                    "Become Full-Time Creator",
                                    "Premium Brand Collaborations",
                                    "Grow Personal Brand"
                                )

                                goals.forEach { goal ->
                                    SelectableGlassCard(
                                        title = goal,
                                        icon = "🎯",
                                        isSelected = selectedGoal == goal,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            selectedGoal = goal
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            6 -> {
                                Text(
                                    text = "What Is Your Biggest Problem?",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(14.dp))

                                val problems = listOf(
                                    "No Brand Replies",
                                    "Low Followers",
                                    "Low Engagement",
                                    "No Media Kit",
                                    "Don't Know Pricing",
                                    "Negotiation Fear",
                                    "Don't Know Where To Find Brands",
                                    "Other"
                                )

                                problems.forEach { prob ->
                                    SelectableGlassCard(
                                        title = prob,
                                        icon = "⚠️",
                                        isSelected = selectedProblem == prob,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            selectedProblem = prob
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (currentStep > 1) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(Color(0x22FFFFFF))
                                        .border(BorderStroke(1.dp, Color(0x33FFFFFF)), RoundedCornerShape(24.dp))
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            currentStep -= 1
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "← Back",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1.5f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Color(0xFFFFD700))
                                    .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color(0xFFFFD700))
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        if (currentStep < 6) {
                                            currentStep += 1
                                        } else {
                                            scope.launch {
                                                isAnalyzing = true
                                                for (i in 0 until scanMessages.size) {
                                                    scanMessageIndex = i
                                                    delay(600)
                                                }
                                                CreatorAcademyPrefs.saveBrandCollabProfile(
                                                    context = context,
                                                    platform = selectedPlatform,
                                                    followers = if (selectedFollowers == "Custom") customFollowersText.ifBlank { "Custom" } else selectedFollowers,
                                                    niche = selectedNiche,
                                                    level = selectedLevel,
                                                    goal = selectedGoal,
                                                    problem = selectedProblem
                                                )
                                                isAnalyzing = false
                                                isAlreadyCompleted = true
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (currentStep == 6) "Analyze Profile 🚀" else "Continue ➔",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Helper Selectable Glass Card Component
 */
@Composable
private fun SelectableGlassCard(
    title: String,
    subtitle: String? = null,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = if (isSelected) Color(0x33FFD700) else Color(0x18FFFFFF),
        border = BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            if (isSelected) Color(0xFFFFD700) else Color(0x22FFFFFF)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = icon, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 13.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color(0xFFFFD700) else TextWhite
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Text(
                            text = subtitle,
                            fontSize = 10.5.sp,
                            color = TextWhite.copy(alpha = 0.65f)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color(0xFFFFD700) else Color.Transparent)
                    .border(
                        BorderStroke(1.5.dp, if (isSelected) Color(0xFFFFD700) else Color(0x44FFFFFF)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.Black,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

/**
 * Helper Metric Glass Card Component
 */
@Composable
private fun MetricGlassCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color(0x1AFFFFFF),
        border = BorderStroke(1.dp, Color(0x22FFFFFF)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite.copy(alpha = 0.65f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFFD700)
            )
            Text(
                text = subtitle,
                fontSize = 9.5.sp,
                color = TextWhite.copy(alpha = 0.5f)
            )
        }
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
                            .background(GreyButtonGradient)
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
                    .background(EmeraldPrimary.copy(alpha = 0.15f))
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
                        .background(GreyButtonGradient)
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
                        .background(EmeraldPrimary.copy(alpha = 0.15f))
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
                    .background(GreyButtonGradient)
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
                        .background(EmeraldPrimary.copy(alpha = 0.15f))
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
                                .background(EmeraldPrimary.copy(alpha = 0.15f))
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
                                .background(EmeraldPrimary.copy(alpha = 0.15f))
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
            color = Color.Transparent,
            border = BorderStroke(1.5.dp, Color(0x88E2E8F0)),
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GreyButtonGradient),
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
                .background(GreyButtonGradient)
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
                        .background(EmeraldPrimary.copy(alpha = 0.18f))
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
                    color = if (isUser) EmeraldPrimary.copy(alpha = 0.22f) else Color(0x2E14241C),
                    border = BorderStroke(1.dp, if (isUser) EmeraldPrimary.copy(alpha = 0.6f) else EmeraldPrimary.copy(alpha = 0.35f)),
                    modifier = Modifier.shadow(
                        elevation = 8.dp,
                        shape = bubbleShape,
                        spotColor = if (isUser) EmeraldPrimary.copy(alpha = 0.3f) else EmeraldPrimary.copy(alpha = 0.15f)
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
                                    .background(EmeraldPrimary.copy(alpha = 0.15f))
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
                        color = EmeraldPrimary.copy(alpha = 0.15f),
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
                            .background(EmeraldPrimary.copy(alpha = 0.12f))
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
                                        .background(GreyButtonGradient)
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
                            color = EmeraldPrimary.copy(alpha = 0.15f),
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
        color = EmeraldPrimary.copy(alpha = 0.16f),
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
                    .background(EmeraldPrimary.copy(alpha = 0.20f)),
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
                        .background(EmeraldPrimary.copy(alpha = 0.15f))
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
                        .background(GreyButtonGradient)
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
                .background(GreyButtonGradient)
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
                                .background(EmeraldPrimary.copy(alpha = 0.15f))
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
            .background(if (isSelected) GreyButtonGradient else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)))
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
            .background(if (isSelected) GreyButtonGradient else Brush.linearGradient(listOf(Color(0x22FFFFFF), Color(0x22FFFFFF))))
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
    val historySnippet = recentHistory.takeLast(4).joinToString("\n") { m ->
        if (m.isFromUser) "User: ${m.text}" else "Mentor: ${m.text.take(100)}"
    }
    val contextStr = "Creator Profile: ${profile.followers} followers in ${profile.niche} on ${profile.platform}.\nHistory:\n$historySnippet"
    
    ViralAiMentorEngine.generateIntegratedMentorResponse(
        domain = MentorToolDomain.BRAND_COLLABORATION_AI,
        userQuery = query,
        userContext = contextStr,
        language = selectedLanguage
    )
}

val MASTER_PHASE3_LEVEL2_MENTOR_REPLIES = listOf(
    "Brands sirf followers nahi dekhte. Woh pehle tumhara profile dekhte hain. Aaj hum tumhari profile ko Brand Ready banayenge.",
    "Welcome to Level 2! High-paying brands deal dene se pehle tumhara DP, Bio aur Grid check karte hain. Let's optimize everything today!",
    "Followers reach laate hain, par Brand Ready Profile deals convert karta hai! Ready to transform your creator identity?",
    "Awesome momentum! Aaj hum tumhare account ko ek professional brand magnet mein transform karenge.",
    "Brand managers get 100+ DMs daily. Stand out karne ke liye aapki profile authority crystal clear honi chahiye!",
    "Level 2 unlocked! Profile optimization is the secret weapon of micro-creators landing ₹50,000+ brand retainers.",
    "Let's fix every profile flaw! Zero to sponsored creator journey ka sabse critical stage hai profile authority.",
    "High-converting Bio + Professional DP = 80% higher brand response rate! Let's build yours step by step.",
    "Brands want creators who look reliable & aesthetic. Aaj tumhare username se lekar highlights tak sab polish karenge.",
    "Super excited! Step 1 to 10 execute karke tumhara Creator Trust Score 90%+ target karenge.",
    "Namaste Creator! Clean bio and structured highlights make brands feel safe investing ₹20k-₹100k in your reels.",
    "Ready for Level 2? Answer 10 quick profile optimization steps to claim your Brand Ready Profile Badge (+200 XP)!",
    "Profile audit complete karne wale creators ko brand managers direct DM mein barter & paid campaigns offer karte hain.",
    "Shabash! Apne niche ki top 1% profile aesthetic create karne ke liye hum Step 1 Profile Review se shuru karte hain.",
    "Zero confusion, 100% action! AI-powered username ideas and 10 premium bios are waiting for you in Level 2.",
    "Let's elevate your social media presence! Premium brands like Nykaa & Boat look for visual symmetry and contact clarity.",
    "Awesome determination! In 15 minutes, your profile will look like an agency-signed creator page.",
    "Great moves! DP position, bio hooks, and highlights strategy will make brand PR managers say YES instantly.",
    "Welcome Rockstar! A brand-ready profile converts cold pitches into warm sponsorship contracts.",
    "Let's optimize! Aaj tumhare account ki visual quality, consistency, and trust factors ko score karenge.",
    "Sahi raste par ho! Level 2 complete hote hi tumhare pass 10 high-authority bio options air 5 custom usernames honge.",
    "Brand Collaboration Masterclass! Let me scan your profile link or screenshot to calculate your Brand Trust Index.",
    "Phenomenal energy! Let's polish your username, DP, and story highlights for maximum sponsorship attraction.",
    "Top class execution ahead! Follow these 10 steps to unlock your Level 2 Brand Ready Badge and 200 XP!",
    "Brands love organized creators! Let me generate your personalized 10 Premium Bios tailored to your niche.",
    "Profile optimization is like your digital store front. Let's make it look like a 5-star brand showroom!",
    "Let's convert those views into cash flow! A clear business bio & structured highlights double your sponsorship replies.",
    "Professionalism matters! Today we optimize your DP lighting, face position, and call-to-action link.",
    "High value creator mindset! Answering Level 2 steps will generate your exact Brand Attraction Score.",
    "Awesome! Let's review your main profile link or screenshot and craft an irresistible creator identity.",
    "AI Brand Mentor active! Analyzing your feed symmetry, username simplicity, and contact setup.",
    "Ek number choice! Level 2 is designed to give you 5x faster brand approvals from verified marketing agencies.",
    "Let's turn your social page into a monetization engine! STEP 1 Profile Review starts right now.",
    "Zabardast! 10 steps complete karte hi tumhara account official brand pitch ready ho jayega.",
    "Welcome Superstar! Let's eliminate all amateur mistakes from your bio, grid, and highlight covers.",
    "Pro level creator strategy! Highlighting past reviews and portfolio builds instant trust with marketing leads.",
    "Target unlocked! Complete Level 2 today to claim your +200 XP Reward and Brand Ready Profile Badge.",
    "Solid start! Your niche-tailored bios and custom username suggestions will give you an unfair advantage.",
    "Hello Champ! Let's audit your lighting, DP background, and content consistency in 10 fast steps.",
    "Game-changer moment! Micro-influencers with optimized bios get 3x more paid deals than accounts with 50k passive followers.",
    "Let's optimize your brand appeal! Clean highlights like 'About Me' and 'Collabs' show brands you mean business.",
    "Aagaye Level 2 mein! Chaliye aapke creator profile ki A to Z branding complete karte hain.",
    "Ready to command ₹10,000+ per reel? A brand-ready profile gives you the confidence to demand high rate cards.",
    "High authority AI loading! Follow these 10 actionable steps to refine your creator brand image.",
    "Bada socho! Your creator profile is your live resume. Let's make it look world-class today.",
    "Welcome! Let me give you 10 copy-paste premium bios tailored strictly to your creator niche.",
    "Super clean execution! We will optimize DP, Bio, Username, Highlights, and Feed Aesthetics in one flow.",
    "Master Creator Mode ACTIVE! Let's run Step 1 Profile Review and elevate your social credibility.",
    "Shandar! Profile optimization complete hote hi aap pitch decks confidently marketing agencies ko bhej sakte ho.",
    "Let's win those brand sponsorships! 10 quick steps to turn your profile into a magnet for PR packages.",
    "Hello Rockstar! A great bio tells brands WHO you are, WHAT you create, and HOW to contact you in 3 seconds.",
    "Welcome to Level 2! Let me generate your custom 5 username suggestions and 10 high-converting bios.",
    "Ultimate Creator Blueprint! 15 minutes of profile tuning = 10x higher response rate from brand managers.",
    "Exciting times! Level 2 gives you the exact blueprint to transform your account into a verified brand partner.",
    "Let's level up! Your Brand Trust Score, Professional Score, and Visual Score will be generated in Step 8.",
    "Banish amateur profiles forever! Follow this 10-step guided optimization to look like a full-time pro.",
    "Awesome choice! Let me analyze your feed style, consistency, and color theme harmony today.",
    "Ready to collect brand deals? Let's fix your call-to-action links and business email placement.",
    "Welcome Champ! Level 2 is your ticket to getting featured in top brand marketing campaign lists.",
    "Full power! Step 1 to Step 10 guide you with visual DP rules, bio hooks, and story highlight strategy.",
    "Zabardast progress! Complete all 6 checklist items in Step 9 to earn your +200 XP Achievement Badge.",
    "Hello Creator! Clean profile aesthetics build immediate authority when brand managers view your page.",
    "Great determination! Let's optimize your username simplicity and profile picture framing now.",
    "Let's build a brand-ready powerhouse! 10 steps to unlock your official Level 2 Completion Badge.",
    "Pura Josh! Profile optimization is 15% of your total course, but it decides whether brands say YES or ignore.",
    "Let me guide you through DP lighting, background contrast, and expression rules for maximum impact.",
    "Phenomenal! Let's generate 10 premium bios according to your niche so you can copy-paste in 1 second.",
    "Welcome Creator! Time to level up from amateur poster to brand-ready commercial creator.",
    "Action time! Let me scan your profile details and give you instant rating scores in 10 smooth steps.",
    "Awesome energy! Let me show you how to structure 'About Me', 'Work', 'Results', and 'Brands' highlights.",
    "Let's execute! Your Brand Ready Profile badge (+200 XP) is waiting for you at Step 10."
)

/**
 * MASTER PHASE 3 — LEVEL 2 BECOME BRAND READY VIEW
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandCollabLevel2BecomeBrandReadyView(
    userNiche: String = "Fashion",
    userPlatform: String = "Instagram",
    onLevel2Completed: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val savedLevel2Data = remember { CreatorAcademyPrefs.getBrandCollabLevel2Data(context) }
    var isAlreadyCompleted by remember { mutableStateOf(savedLevel2Data["completed"] as? Boolean ?: false) }

    var currentStep by remember { mutableIntStateOf((savedLevel2Data["step"] as? Int ?: 1).coerceIn(1, 10)) }
    var profileLinkInput by remember { mutableStateOf(savedLevel2Data["profile_link"] as? String ?: "") }
    var selectedPlatform by remember { mutableStateOf(userPlatform.ifBlank { "Instagram" }) }
    var isScreenshotUploaded by remember { mutableStateOf(false) }

    var selectedUsername by remember { mutableStateOf(savedLevel2Data["username"] as? String ?: "") }
    var selectedBio by remember { mutableStateOf(savedLevel2Data["bio"] as? String ?: "") }

    val savedChecklistCsv = (savedLevel2Data["checklist"] as? String ?: "")
    val checklistSet = remember {
        mutableStateListOf<String>().apply {
            if (savedChecklistCsv.isNotBlank()) {
                addAll(savedChecklistCsv.split(","))
            }
        }
    }

    var isAnalyzing by remember { mutableStateOf(false) }
    var scanMessageIndex by remember { mutableIntStateOf(0) }

    val mentorMessage = remember { MASTER_PHASE3_LEVEL2_MENTOR_REPLIES.random() }

    val defaultNicheBios = remember(userNiche) {
        listOf(
            "✨ $userNiche Creator & Trendsetter\n📍 India | 📩 Collabs: dm/email\n👇 Best reels & outfits below",
            "🚀 Helping you upgrade your $userNiche game\n💼 Brand Deals & UGC Content\n📧 Contact: business@creator.com",
            "🌟 Daily $userNiche Inspo & Tips\n🎥 100K+ Reach | Commercial Partner\n🔗 Tap link for rate card",
            "🔥 Top $userNiche Picks & Reviews\n🤝 Open for Barter & Paid Campaigns\n👇 Watch my latest haul",
            "💡 Modern $userNiche Insights & Style\n🏆 Trusted by 20+ Premium Brands\n📩 Inquiries in Bio link",
            "🎯 Authentic $userNiche Storytelling\n✨ Quality over quantity\n📩 Collabs: DM for Rate Card",
            "👑 Premium $userNiche Content Creator\n🌍 Mumbai / Delhi\n👇 Tap below for my media kit",
            "⚡ $userNiche | Lifestyle | Aesthetics\n📸 Creating high-converting UGC\n📩 Direct Message for PR",
            "🌿 Sustainable & Modern $userNiche\n💬 DMs open for brand managers\n🔗 Portfolio link below",
            "⭐ $userNiche Expert & Product Reviewer\n🚀 5M+ Impressions\n📧 Email for paid integrations"
        )
    }

    val defaultNicheUsernames = remember(userNiche) {
        val cleanNiche = userNiche.lowercase().replace(" ", "")
        listOf(
            "@stylewith_${cleanNiche}",
            "@official_${cleanNiche}_hub",
            "@${cleanNiche}.creates",
            "@glam_${cleanNiche}_pro",
            "@the_${cleanNiche}_creator"
        )
    }

    val allChecklistItems = listOf(
        "Better Bio Applied",
        "Better DP Uploaded",
        "Better Username Chosen",
        "Better Feed Aesthetic Set",
        "Better Story Highlights Structured",
        "Better Contact Details Added"
    )

    fun persistState(newStep: Int = currentStep, completed: Boolean = isAlreadyCompleted) {
        CreatorAcademyPrefs.saveBrandCollabLevel2State(
            context = context,
            step = newStep,
            profileLink = profileLinkInput,
            selectedUsername = selectedUsername,
            selectedBio = selectedBio,
            checklistCsv = checklistSet.joinToString(","),
            isCompleted = completed
        )
    }

    val scanMessages = listOf(
        "⚡ Scanning profile structure & aesthetics...",
        "🖼️ Evaluating DP resolution & face position...",
        "📝 Auditing bio hook & contact clarity...",
        "⭕ Checking story highlights setup for $selectedPlatform...",
        "📊 Calculating Brand Trust Score (0–100)..."
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Card - Level 2 15% Progress Ring
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0x22121824),
                border = BorderStroke(
                    1.5.dp,
                    Brush.horizontalGradient(
                        listOf(Color(0xFFFFD700).copy(alpha = 0.6f), Color(0x33FFFFFF), Color(0xFFFFD700).copy(alpha = 0.4f))
                    )
                ),
                shadowElevation = 10.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Brand Collaboration Hub",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "👑", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Optimize Your Creator Profile (Level 2)",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFFD700)
                            )
                        }

                        BrandCollabProgressRing(
                            progressPercent = if (isAlreadyCompleted) 1.0f else 0.15f,
                            displayText = if (isAlreadyCompleted) "100%" else "15%"
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, Color(0xFFFFD700), Color.Transparent)
                                )
                            )
                    )
                }
            }

            // AI Mentor Card
            val infiniteTransition = rememberInfiniteTransition(label = "avatar_breath_l2")
            val breathScale by infiniteTransition.animateFloat(
                initialValue = 0.97f,
                targetValue = 1.03f,
                animationSpec = infiniteRepeatable(tween(1600, easing = LinearEasing), RepeatMode.Reverse),
                label = "breath_l2"
            )

            Surface(
                shape = RoundedCornerShape(22.dp),
                color = Color(0x1A1E2638),
                border = BorderStroke(1.dp, Color(0x33FFD700)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .scale(breathScale)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFFFFD700).copy(alpha = 0.35f), Color.Transparent)
                                )
                            )
                            .border(BorderStroke(1.5.dp, Color(0xFFFFD700)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🤖", fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AI Mentor Says",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD700)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "✨", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (isAlreadyCompleted)
                                "Brands sirf followers nahi dekhte. Woh pehle tumhara profile dekhte hain. Aapki profile ab 100% Brand Ready hai!"
                            else
                                mentorMessage,
                            fontSize = 12.5.sp,
                            color = TextWhite,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            if (isAnalyzing) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF101726),
                    border = BorderStroke(1.5.dp, Color(0xFFFFD700)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color(0x22FFD700))
                                .border(BorderStroke(2.dp, Color(0xFFFFD700)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "⚡", fontSize = 42.sp)
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "AI Profile Analysis in Progress...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextWhite
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = scanMessages.getOrElse(scanMessageIndex) { scanMessages.first() },
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFFFD700),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // Steps Container Card
                Surface(
                    shape = RoundedCornerShape(26.dp),
                    color = Color(0x1E121824),
                    border = BorderStroke(1.5.dp, Color(0x33FFD700)),
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "STEP $currentStep OF 10",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFFD700),
                                letterSpacing = 1.sp
                            )

                            Text(
                                text = "${currentStep * 10}% Complete",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite.copy(alpha = 0.6f)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        when (currentStep) {
                            // STEP 1: Profile Review
                            1 -> {
                                Text(
                                    text = "STEP 1: Profile Review",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Select your primary platform & paste your profile link or upload a screenshot to start AI analysis.",
                                    fontSize = 12.sp,
                                    color = TextWhite.copy(alpha = 0.7f)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                val platformList = listOf(
                                    Pair("Instagram", "📸"),
                                    Pair("YouTube", "🎬"),
                                    Pair("Facebook", "📘"),
                                    Pair("LinkedIn", "💼")
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    platformList.forEach { (plat, icon) ->
                                        val isSel = selectedPlatform == plat
                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    selectedPlatform = plat
                                                },
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (isSel) Color(0x33FFD700) else Color(0x18FFFFFF),
                                            border = BorderStroke(
                                                if (isSel) 1.5.dp else 1.dp,
                                                if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF)
                                            )
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            ) {
                                                Text(text = icon, fontSize = 16.sp)
                                                Text(
                                                    text = plat,
                                                    fontSize = 10.sp,
                                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSel) Color(0xFFFFD700) else TextWhite
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                OutlinedTextField(
                                    value = profileLinkInput,
                                    onValueChange = {
                                        profileLinkInput = it
                                        persistState()
                                    },
                                    label = { Text("Paste Your $selectedPlatform Profile Link", color = Color(0xFFFFD700)) },
                                    placeholder = { Text("e.g., https://instagram.com/your_handle", color = TextWhite.copy(alpha = 0.35f), fontSize = 12.sp) },
                                    leadingIcon = { Icon(Icons.Default.OpenInNew, contentDescription = "Link", tint = Color(0xFFFFD700)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFFD700),
                                        unfocusedBorderColor = Color(0x33FFFFFF),
                                        focusedTextColor = TextWhite,
                                        unfocusedTextColor = TextWhite
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "OR UPLOAD PROFILE SCREENSHOT",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite.copy(alpha = 0.6f),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            isScreenshotUploaded = true
                                            Toast
                                                .makeText(context, "Screenshot Uploaded & Verified ✓", Toast.LENGTH_SHORT)
                                                .show()
                                        },
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isScreenshotUploaded) Color(0x224ADE80) else Color(0x18FFFFFF),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isScreenshotUploaded) Color(0xFF4ADE80) else Color(0x33FFFFFF)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isScreenshotUploaded) Icons.Default.Check else Icons.Default.OpenInNew,
                                            contentDescription = "Upload",
                                            tint = if (isScreenshotUploaded) Color(0xFF4ADE80) else Color(0xFFFFD700)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isScreenshotUploaded) "📸 Screenshot Uploaded: profile_preview.png ✓" else "📸 Tap to Upload Profile Screenshot",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isScreenshotUploaded) Color(0xFF4ADE80) else TextWhite
                                        )
                                    }
                                }
                            }

                            // STEP 2: AI Profile Analysis
                            2 -> {
                                Text(
                                    text = "STEP 2: AI Profile Analysis",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "AI evaluation of your Display Picture, Username, Bio, Highlights, and Content Quality.",
                                    fontSize = 12.sp,
                                    color = TextWhite.copy(alpha = 0.7f)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Score Circle
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier.size(110.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Canvas(modifier = Modifier.fillMaxSize()) {
                                            val strokeWidth = 9.dp.toPx()
                                            val radius = (size.minDimension - strokeWidth) / 2
                                            val center = Offset(size.width / 2, size.height / 2)

                                            drawCircle(
                                                color = Color(0x33FFFFFF),
                                                radius = radius,
                                                center = center,
                                                style = Stroke(width = strokeWidth)
                                            )

                                            drawArc(
                                                brush = Brush.sweepGradient(
                                                    colors = listOf(Color(0xFFFFD700), Color(0xFFFFF099), Color(0xFFFFD700))
                                                ),
                                                startAngle = -90f,
                                                sweepAngle = 360f * 0.88f,
                                                useCenter = false,
                                                style = Stroke(width = strokeWidth)
                                            )
                                        }

                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(text = "88", fontSize = 30.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                            Text(text = "/ 100", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                val scanParams = listOf(
                                    Triple("🖼️ Display Picture Quality", "90%", Color(0xFF4ADE80)),
                                    Triple("🆔 Username Professionalism", "85%", Color(0xFFFFD700)),
                                    Triple("📝 Bio Hook & Contact Clarity", "82%", Color(0xFFFFD700)),
                                    Triple("⭕ Highlights Strategy", "88%", Color(0xFF4ADE80)),
                                    Triple("🎥 Content Visuals & Feed Symmetry", "92%", Color(0xFF4ADE80)),
                                    Triple("💼 Professionalism Index", "86%", Color(0xFFFFD700)),
                                    Triple("🛡️ Trust Factor Score", "89%", Color(0xFF4ADE80))
                                )

                                scanParams.forEach { (label, valStr, color) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = label, fontSize = 12.sp, color = TextWhite)
                                        Text(text = valStr, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = color)
                                    }
                                }
                            }

                            // STEP 3: Username Review
                            3 -> {
                                Text(
                                    text = "STEP 3: Username Review",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "AI Checks: Easy to remember, Professional, and Brand Friendly. Select or copy a suggestion:",
                                    fontSize = 12.sp,
                                    color = TextWhite.copy(alpha = 0.7f)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0x22FFD700),
                                    border = BorderStroke(1.dp, Color(0xFFFFD700)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "✅ AI Rules for Brand-Friendly Handles:",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFFD700)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "• Avoid excessive underscores (e.g., _x_creator_99_)\n• Keep length under 15 characters\n• Include your niche or 'official/creates' keyword",
                                            fontSize = 11.sp,
                                            color = TextWhite.copy(alpha = 0.8f)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "SUGGESTED USERNAMES FOR $userNiche:",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFFD700)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                defaultNicheUsernames.forEach { usernameIdea ->
                                    val isSel = selectedUsername == usernameIdea
                                    SelectableGlassCard(
                                        title = usernameIdea,
                                        subtitle = if (isSel) "Selected as your target handle ✓" else "Tap to choose handle",
                                        icon = "🆔",
                                        isSelected = isSel,
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            selectedUsername = usernameIdea
                                            persistState()
                                            Toast.makeText(context, "Selected: $usernameIdea", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            // STEP 4: Bio Optimizer
                            4 -> {
                                Text(
                                    text = "STEP 4: Bio Optimizer",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "10 Premium Bios generated for $userNiche. Tap any bio to select & save to your profile:",
                                    fontSize = 12.sp,
                                    color = TextWhite.copy(alpha = 0.7f)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                defaultNicheBios.forEachIndexed { idx, bioText ->
                                    val isSel = selectedBio == bioText
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = if (isSel) Color(0x33FFD700) else Color(0x18FFFFFF),
                                        border = BorderStroke(
                                            if (isSel) 1.5.dp else 1.dp,
                                            if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                selectedBio = bioText
                                                persistState()
                                                Toast
                                                    .makeText(context, "Saved Bio #${idx + 1} ✓", Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "OPTION #${idx + 1}",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color(0xFFFFD700)
                                                )
                                                Text(
                                                    text = if (isSel) "SELECTED ✓" else "Tap to Select",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSel) Color(0xFF4ADE80) else TextWhite.copy(alpha = 0.5f)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = bioText,
                                                fontSize = 12.sp,
                                                color = TextWhite,
                                                lineHeight = 16.sp
                                            )
                                        }
                                    }
                                }
                            }

                            // STEP 5: Profile Picture Guide
                            5 -> {
                                Text(
                                    text = "STEP 5: Profile Picture Guide",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Master the 6 Golden Rules of a high-converting Brand Friendly DP:",
                                    fontSize = 12.sp,
                                    color = TextWhite.copy(alpha = 0.7f)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                val dpRules = listOf(
                                    Triple("🎯 Face Position", "Eye-level framing taking ~60% of avatar circle area.", "01"),
                                    Triple("💡 Lighting", "Bright, soft natural light or ring light. Avoid dark shadows.", "02"),
                                    Triple("🎨 Background", "Clean solid backdrop or aesthetic blurred high-contrast color.", "03"),
                                    Triple("😊 Expression", "Warm, confident, approachable smile that invites trust.", "04"),
                                    Triple("👔 Dress Code", "Neat, niche-appropriate attire reflecting your content style.", "05"),
                                    Triple("✨ Brand Ring", "Subtle ring or high-contrast border for high visibility in DMs.", "06")
                                )

                                dpRules.forEach { (title, desc, num) ->
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = Color(0x18FFFFFF),
                                        border = BorderStroke(1.dp, Color(0x22FFFFFF)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0x33FFD700)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = num,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color(0xFFFFD700)
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = title,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextWhite
                                                )
                                                Text(
                                                    text = desc,
                                                    fontSize = 11.sp,
                                                    color = TextWhite.copy(alpha = 0.7f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // STEP 6: Highlight Strategy
                            6 -> {
                                Text(
                                    text = "STEP 6: Highlight Strategy",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Custom Story Highlight structure tailored to $userNiche to prove credibility instantly:",
                                    fontSize = 12.sp,
                                    color = TextWhite.copy(alpha = 0.7f)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                val highlightList = listOf(
                                    Pair("👤 About Me", "Your creator journey, niche focus & brand mission"),
                                    Pair("💼 Work / Portfolio", "Best reels, UGC samples & top performing posts"),
                                    Pair("📈 Results", "Impressions proof, reach metrics & audience demographics"),
                                    Pair("⭐ Reviews", "Brand feedback, client messages & subscriber love"),
                                    Pair("🤝 Brands", "Tagged brand collaborations & PR packages unboxing"),
                                    Pair("📩 Contact", "Business email, WhatsApp link & direct rate card")
                                )

                                highlightList.forEach { (name, desc) ->
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = Color(0x18FFFFFF),
                                        border = BorderStroke(1.dp, Color(0x22FFFFFF)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Surface(
                                                shape = CircleShape,
                                                color = Color(0x33FFD700),
                                                border = BorderStroke(1.dp, Color(0xFFFFD700))
                                            ) {
                                                Box(
                                                    modifier = Modifier.size(38.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = "⭕", fontSize = 18.sp)
                                                }
                                            }

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = name,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFFFD700)
                                                )
                                                Text(
                                                    text = desc,
                                                    fontSize = 11.sp,
                                                    color = TextWhite.copy(alpha = 0.75f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // STEP 7: Content Quality Review
                            7 -> {
                                Text(
                                    text = "STEP 7: Content Quality Review",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "AI Content & Feed Quality Audit parameters for $selectedPlatform:",
                                    fontSize = 12.sp,
                                    color = TextWhite.copy(alpha = 0.7f)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                val qualityFactors = listOf(
                                    Triple("🎞️ Feed Aesthetics & Grid Symmetry", "Strong Cover Cards", Color(0xFF4ADE80)),
                                    Triple("📅 Posting Consistency Rate", "3-5 Posts / Week", Color(0xFFFFD700)),
                                    Triple("🎨 Color Theme Harmony", "Unified 2-Color Palette", Color(0xFF4ADE80)),
                                    Triple("📹 Content Format & Style", "High-Hook Reels & Carousels", Color(0xFF4ADE80)),
                                    Triple("💡 Visual Quality & Audio Clarity", "1080p Resolution", Color(0xFF4ADE80))
                                )

                                qualityFactors.forEach { (factor, detail, color) ->
                                    MetricGlassCard(
                                        title = factor,
                                        value = detail,
                                        subtitle = "AI Grade: Excellent",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0x22FFD700),
                                    border = BorderStroke(1.dp, Color(0xFFFFD700)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "💡 Actionable Suggestion:",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFFD700)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Use consistent cover fonts and 3-second visual hooks. Brands evaluate the first 3 posts on your profile before reading pitch emails!",
                                            fontSize = 11.sp,
                                            color = TextWhite.copy(alpha = 0.85f)
                                        )
                                    }
                                }
                            }

                            // STEP 8: Brand Trust Score
                            8 -> {
                                Text(
                                    text = "STEP 8: Brand Trust Score",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Your profile authority breakdown generated across 4 essential brand dimensions:",
                                    fontSize = 12.sp,
                                    color = TextWhite.copy(alpha = 0.7f)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    MetricGlassCard(
                                        title = "Trust Score",
                                        value = "92% 🛡️",
                                        subtitle = "High Credibility",
                                        modifier = Modifier.weight(1f)
                                    )
                                    MetricGlassCard(
                                        title = "Professional Score",
                                        value = "88% 💼",
                                        subtitle = "Agency Ready",
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    MetricGlassCard(
                                        title = "Visual Score",
                                        value = "94% 🎨",
                                        subtitle = "Aesthetic Grid",
                                        modifier = Modifier.weight(1f)
                                    )
                                    MetricGlassCard(
                                        title = "Brand Attraction",
                                        value = "90% 🧲",
                                        subtitle = "Inbound Magnet",
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            // STEP 9: Quick Improvements
                            9 -> {
                                Text(
                                    text = "STEP 9: Quick Improvements",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Tap each item as you apply it to your profile:",
                                    fontSize = 12.sp,
                                    color = TextWhite.copy(alpha = 0.7f)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                allChecklistItems.forEach { item ->
                                    val isChecked = checklistSet.contains(item)
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                if (isChecked) {
                                                    checklistSet.remove(item)
                                                } else {
                                                    checklistSet.add(item)
                                                }
                                                persistState()
                                            },
                                        shape = RoundedCornerShape(16.dp),
                                        color = if (isChecked) Color(0x224ADE80) else Color(0x18FFFFFF),
                                        border = BorderStroke(
                                            1.dp,
                                            if (isChecked) Color(0xFF4ADE80) else Color(0x22FFFFFF)
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(14.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clip(CircleShape)
                                                    .background(if (isChecked) Color(0xFF4ADE80) else Color(0x22FFFFFF)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (isChecked) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = "Done",
                                                        tint = Color.Black,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Text(
                                                text = item,
                                                fontSize = 13.sp,
                                                fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isChecked) Color(0xFF4ADE80) else TextWhite
                                            )
                                        }
                                    }
                                }
                            }

                            // STEP 10: Today's Mission & ACHIEVEMENT
                            10 -> {
                                Text(
                                    text = "STEP 10: Today's Mission",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Complete Profile Optimization & claim your official Level 2 Achievement Badge!",
                                    fontSize = 12.sp,
                                    color = TextWhite.copy(alpha = 0.7f)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Mission Card
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = Color(0x22FFD700),
                                    border = BorderStroke(1.2.dp, Color(0xFFFFD700)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "🎯 TODAY'S MISSION",
                                                fontSize = 10.5.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color(0xFFFFD700)
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "Complete Profile Optimization",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextWhite
                                            )
                                            Text(
                                                text = "Estimated Time: 15 Minutes",
                                                fontSize = 11.sp,
                                                color = TextWhite.copy(alpha = 0.7f)
                                            )
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFFFFD700)
                                        ) {
                                            Text(
                                                text = "+200 XP ✓",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color.Black,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Achievement Badge
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = Color(0x1A10281A),
                                    border = BorderStroke(1.5.dp, Color(0xFF4ADE80)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "🏆", fontSize = 36.sp)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = "Brand Ready Profile",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color(0xFF4ADE80)
                                            )
                                            Text(
                                                text = "Level 2 Achievement Unlocked & Saved (+200 XP)",
                                                fontSize = 11.5.sp,
                                                color = TextWhite.copy(alpha = 0.8f)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Glass Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (currentStep > 1) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(Color(0x22FFFFFF))
                                        .border(BorderStroke(1.dp, Color(0x44FFFFFF)), RoundedCornerShape(24.dp))
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            currentStep--
                                            persistState()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Back",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(Color(0x22FFFFFF))
                                        .border(BorderStroke(1.dp, Color(0x44FFFFFF)), RoundedCornerShape(24.dp))
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            onBack()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Back",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1.3f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Color(0xFFFFD700))
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        if (currentStep < 10) {
                                            if (currentStep == 1) {
                                                // Trigger scanning simulation
                                                scope.launch {
                                                    isAnalyzing = true
                                                    for (i in 0..4) {
                                                        scanMessageIndex = i
                                                        delay(350)
                                                    }
                                                    isAnalyzing = false
                                                    currentStep = 2
                                                    persistState()
                                                }
                                            } else {
                                                currentStep++
                                                persistState()
                                            }
                                        } else {
                                            isAlreadyCompleted = true
                                            persistState(completed = true)
                                            onLevel2Completed()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (currentStep == 10) "Finish Level 2 ➔" else "Continue ➔",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



// ============================================================================
// PHASE 4 - LEVEL 3: AI MEDIA KIT BUILDER
// ============================================================================

val MASTER_PHASE4_LEVEL3_MENTOR_REPLIES = listOf(
    "Media Kit tumhara digital resume hota hai. Professional brands sabse pehle isi ko dekhte hain. Ab main tumhare liye industry-standard Media Kit banaunga.",
    "Level 3 unlocked! High-paying brands (Rs. 25,000 to Rs. 2,00,000) demand a clean 1-page PDF Media Kit before signing contracts.",
    "A Media Kit bridges the gap between an amateur content creator and a commercially viable media partner.",
    "Brands care about 3 core metrics: Target Audience Match, Engagement Rate, and Authentic Reach. We will showcase all three!",
    "Your basic information establishes trust. Always use a professional email handle (e.g. contact@yourname.com) for brand pitches.",
    "Location and city matter to brands for regional campaigns, store inaugurations, and offline launch events.",
    "Adding direct links to your active social accounts allows brand managers to audit your grid in a single click.",
    "Your Creator Bio is your elevator pitch. It should highlight who you are, what value you deliver, and why brands should hire you.",
    "Let AI write your bio if you're feeling stuck! Our AI engine creates high-converting bio copy tailored specifically to your niche.",
    "Defining your Target Audience (Students, Gamers, Women, Tech Enthusiasts) tells brands if your followers match their buyer persona.",
    "Audience Demographics are gold for PR agencies. Showing primary country, language, and age group doubles your pitch success rate.",
    "Selecting clear Content Categories helps brand algorithm tools automatically index your creator profile under the right vertical.",
    "Highlighting past brand collaborations (even barter deals or self-initiated posts) builds social proof and credibility.",
    "Visual assets matter! Clean post screenshots, reel cover thumbnails, and campaign proof instantly double your rate card value.",
    "Real statistics like Engagement Rate (3%+ is great) matter 10x more than raw follower counts to modern influencer marketers.",
    "Your live Media Kit Preview is formatted like an executive 1-page PDF portfolio that PR agencies can share directly with brand managers.",
    "The AI Media Kit Audit checks 20+ parameters to score your profile appeal, trust index, and commercial readiness.",
    "AI Auto-Improvements polish your bio, summary, and achievements into agency-grade pitch language.",
    "Completing all 6 Brand Ready Checklist items guarantees your portfolio meets international creator standards!",
    "Congratulations on building your AI Media Kit! You are now fully equipped to pitch top-tier brands with confidence."
) + List(60) { index ->
    "AI Mentor Insight #" + (index + 21) + ": Professional Media Kits with verified statistics get 3x higher response rates from brand PR teams!"
}

@Composable
fun BrandCollabLevel3AIMediaKitView(
    userNiche: String = "Fashion",
    userPlatform: String = "Instagram",
    onLevel3Completed: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Load saved data
    val savedData = remember { CreatorAcademyPrefs.getBrandCollabLevel3Data(context) }
    var isAlreadyCompleted by remember { mutableStateOf(savedData["completed"] as? Boolean ?: false) }
    var currentStep by remember { mutableIntStateOf((savedData["step"] as? Int ?: 1).coerceIn(1, 13)) }

    // Step 1: Basic Info
    var fullName by remember { mutableStateOf(savedData["fullName"] as? String ?: "") }
    var creatorName by remember { mutableStateOf(savedData["creatorName"] as? String ?: "") }
    var email by remember { mutableStateOf(savedData["email"] as? String ?: "") }
    var city by remember { mutableStateOf(savedData["city"] as? String ?: "") }
    var country by remember { mutableStateOf(savedData["country"] as? String ?: "") }

    // Step 2: Social Accounts
    val savedSocials = (savedData["socialLinks"] as? String ?: "").split("|")
    var socialInstagram by remember { mutableStateOf(savedSocials.getOrNull(0) ?: "") }
    var socialYoutube by remember { mutableStateOf(savedSocials.getOrNull(1) ?: "") }
    var socialFacebook by remember { mutableStateOf(savedSocials.getOrNull(2) ?: "") }
    var socialLinkedin by remember { mutableStateOf(savedSocials.getOrNull(3) ?: "") }
    var socialWebsite by remember { mutableStateOf(savedSocials.getOrNull(4) ?: "") }

    // Step 3: Bio
    var bio by remember { mutableStateOf(savedData["bio"] as? String ?: "") }
    var isAiBioGenerating by remember { mutableStateOf(false) }

    // Step 4: Audience
    var selectedAudience by remember { mutableStateOf(savedData["audience"] as? String ?: "Gen-Z & Youth") }

    // Step 5: Demographics
    var demoCountry by remember { mutableStateOf(savedData["demoCountry"] as? String ?: "India") }
    var demoLang by remember { mutableStateOf(savedData["demoLang"] as? String ?: "Hindi / English") }
    var demoAge by remember { mutableStateOf(savedData["demoAge"] as? String ?: "18–24 years") }

    // Step 6: Categories
    val initialCats = (savedData["categories"] as? String ?: "").split(",").filter { it.isNotBlank() }
    var selectedCategories by remember { mutableStateOf(if (initialCats.isNotEmpty()) initialCats else listOf(userNiche, "Lifestyle", "Technology")) }

    // Step 7: Achievements
    val savedAchievements = (savedData["achievements"] as? String ?: "").split("|")
    var achievementsBrands by remember { mutableStateOf(savedAchievements.getOrNull(0) ?: "Boat, Nykaa, Mamaearth") }
    var achievementsCertificates by remember { mutableStateOf(savedAchievements.getOrNull(1) ?: "Meta Certified Digital Creator") }
    var achievementsAwards by remember { mutableStateOf(savedAchievements.getOrNull(2) ?: "Top 10 Micro Creator 2025") }
    var achievementsSkills by remember { mutableStateOf(savedAchievements.getOrNull(3) ?: "4K Video Production, Reel Editing") }

    // Step 8: Portfolio Images upload simulator
    var profilePhotoUploaded by remember { mutableStateOf(true) }
    var reelScreenshotUploaded by remember { mutableStateOf(true) }
    var postUploaded by remember { mutableStateOf(true) }
    var thumbnailUploaded by remember { mutableStateOf(true) }
    var campaignUploaded by remember { mutableStateOf(true) }

    // Step 9: Statistics
    var followers by remember { mutableStateOf(savedData["followers"] as? String ?: "12,500") }
    var avgReach by remember { mutableStateOf(savedData["reach"] as? String ?: "55,000 / month") }
    var avgViews by remember { mutableStateOf(savedData["views"] as? String ?: "28,000 / reel") }
    var engagementRate by remember { mutableStateOf(savedData["engagement"] as? String ?: "6.8%") }
    var monthlyViews by remember { mutableStateOf(savedData["monthlyViews"] as? String ?: "180,000") }

    // Step 11: AI Review
    var isAiReviewing by remember { mutableStateOf(false) }
    var aiReviewDone by remember { mutableStateOf(false) }

    // Step 12: Auto Improvements
    var isAiEnhanced by remember { mutableStateOf(false) }

    // Step 13: Checklist
    val initialChecklist = (savedData["checklist"] as? String ?: "").split(",").map { it == "true" }
    var checklistItems by remember {
        mutableStateOf(
            if (initialChecklist.size == 6) initialChecklist.toMutableList()
            else mutableListOf(true, true, true, true, true, true)
        )
    }

    // Auto-save helper
    fun persistState(completed: Boolean = false) {
        CreatorAcademyPrefs.saveBrandCollabLevel3State(
            context = context,
            step = currentStep,
            fullName = fullName,
            creatorName = creatorName,
            email = email,
            city = city,
            country = country,
            socialLinks = "$socialInstagram|$socialYoutube|$socialFacebook|$socialLinkedin|$socialWebsite",
            bio = bio,
            audience = selectedAudience,
            demoCountry = demoCountry,
            demoLang = demoLang,
            demoAge = demoAge,
            categories = selectedCategories.joinToString(","),
            achievements = "$achievementsBrands|$achievementsCertificates|$achievementsAwards|$achievementsSkills",
            followers = followers,
            reach = avgReach,
            views = avgViews,
            engagement = engagementRate,
            monthlyViews = monthlyViews,
            checklistCsv = checklistItems.joinToString(","),
            isCompleted = completed || isAlreadyCompleted
        )
    }

    // Motion & particle transitions
    val infiniteTransition = rememberInfiniteTransition(label = "FloatingAssets")
    val floatingOffsetY by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FloatingY"
    )

    val progressRingAngle by animateFloatAsState(
        targetValue = if (isAlreadyCompleted || currentStep == 13) 360f else (currentStep / 13f) * 360f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "ProgressRing"
    )

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // Floating Background Brands & Golden Particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Golden Particles
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.15f), radius = 6.dp.toPx(), center = Offset(width * 0.15f, height * 0.2f + floatingOffsetY * 3))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.10f), radius = 10.dp.toPx(), center = Offset(width * 0.82f, height * 0.35f - floatingOffsetY * 2))
            drawCircle(Color(0x3338BDF8), radius = 8.dp.toPx(), center = Offset(width * 0.88f, height * 0.75f + floatingOffsetY * 2))
            drawCircle(Color(0x224ADE80), radius = 12.dp.toPx(), center = Offset(width * 0.12f, height * 0.8f - floatingOffsetY * 4))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header with Progress Ring
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xCC1E293B))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (currentStep > 1) {
                            currentStep--
                            persistState()
                        } else {
                            onBack()
                        }
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                ) {
                    Text("←", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "AI Media Kit Builder",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0x33FFD700))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("LEVEL 3", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                        }
                    }
                    Text(
                        text = "Build Your Professional Creator Portfolio",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }

                // 28% Animated Progress Ring
                Box(
                    modifier = Modifier.size(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = Color.White.copy(alpha = 0.15f),
                            style = Stroke(width = 3.dp.toPx())
                        )
                        drawArc(
                            color = Color(0xFFFFD700),
                            startAngle = -90f,
                            sweepAngle = progressRingAngle,
                            useCenter = false,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = if (isAlreadyCompleted || currentStep == 13) "100%" else "${((currentStep / 13f) * 100).toInt()}%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFD700)
                    )
                }
            }

            // Step Content Box
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // AI Mentor Header Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0x331E293B), Color(0x44334155))
                            )
                        )
                        .border(1.dp, Color(0x33FFD700), RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        // AI Avatar with soft glow
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0x33FFD700))
                                .border(1.5.dp, Color(0xFFFFD700), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🤖", fontSize = 22.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "AI Brand Mentor",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD700)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "● 80+ Styles Active",
                                    fontSize = 9.sp,
                                    color = Color(0xFF4ADE80)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (currentStep == 1) "Media Kit tumhara digital resume hota hai. Professional brands sabse pehle isi ko dekhte hain. Ab main tumhare liye industry-standard Media Kit banaunga."
                                else MASTER_PHASE4_LEVEL3_MENTOR_REPLIES.getOrElse(currentStep - 1) { MASTER_PHASE4_LEVEL3_MENTOR_REPLIES.first() },
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Floating Brand Logos Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x15FFFFFF))
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🎧 Boat", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                    Text("💄 Nykaa", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                    Text("🌿 Mamaearth", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                    Text("👟 Puma", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                    Text("📱 Samsung", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Step Indicator Pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STEP $currentStep OF 13",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFD700)
                    )
                    Text(
                        text = when (currentStep) {
                            1 -> "Basic Information"
                            2 -> "Social Accounts"
                            3 -> "Creator Bio"
                            4 -> "Target Audience"
                            5 -> "Demographics"
                            6 -> "Content Categories"
                            7 -> "Achievements"
                            8 -> "Portfolio Images"
                            9 -> "Performance Statistics"
                            10 -> "Media Kit Preview"
                            11 -> "AI Audit Review"
                            12 -> "Auto Improvements"
                            else -> "Brand Ready Checklist"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // STEP PANELS
                when (currentStep) {
                    1 -> {
                        // Step 1: Basic Info
                        MediaKitGlassCard(title = "Step 1: Basic Information") {
                            MediaKitTextField("Full Name", fullName, "e.g., Rohan Sharma") { fullName = it; persistState() }
                            MediaKitTextField("Creator Name / Handle", creatorName, "e.g., @rohan_creates") { creatorName = it; persistState() }
                            MediaKitTextField("Professional Email", email, "e.g., contact@rohansharma.com") { email = it; persistState() }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(modifier = Modifier.weight(1f)) {
                                    MediaKitTextField("City", city, "e.g., Mumbai") { city = it; persistState() }
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    MediaKitTextField("Country", country, "e.g., India") { country = it; persistState() }
                                }
                            }
                        }
                    }

                    2 -> {
                        // Step 2: Social Links
                        MediaKitGlassCard(title = "Step 2: Social Account Links") {
                            MediaKitTextField("Instagram Profile Link", socialInstagram, "https://instagram.com/your_handle") { socialInstagram = it; persistState() }
                            MediaKitTextField("YouTube Channel Link", socialYoutube, "https://youtube.com/@your_channel") { socialYoutube = it; persistState() }
                            MediaKitTextField("Facebook Page Link", socialFacebook, "https://facebook.com/your_page") { socialFacebook = it; persistState() }
                            MediaKitTextField("LinkedIn Profile Link", socialLinkedin, "https://linkedin.com/in/your_name") { socialLinkedin = it; persistState() }
                            MediaKitTextField("Website / Blog (Optional)", socialWebsite, "https://yourportfolio.com") { socialWebsite = it; persistState() }
                        }
                    }

                    3 -> {
                        // Step 3: Creator Bio
                        MediaKitGlassCard(title = "Step 3: Creator Bio & Elevator Pitch") {
                            OutlinedTextField(
                                value = bio,
                                onValueChange = { bio = it; persistState() },
                                label = { Text("Tell brands about yourself", color = Color(0xFFFFD700)) },
                                placeholder = { Text("e.g. Passionate fashion & tech creator helping youth discover modern lifestyle trends.", color = Color.White.copy(alpha = 0.4f)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFFFD700),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isAiBioGenerating = true
                                    coroutineScope.launch {
                                        delay(600)
                                        bio = "Hi, I'm ${creatorName.ifBlank { "a digital creator" }}! I create high-converting $userNiche content on $userPlatform for an engaged youth audience. Partnered with top brands to drive authentic engagement and sales."
                                        isAiBioGenerating = false
                                        persistState()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFD700))
                            ) {
                                Text(if (isAiBioGenerating) "✨ AI Writing Bio..." else "✨ Generate Bio Using AI", color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    4 -> {
                        // Step 4: Audience
                        MediaKitGlassCard(title = "Step 4: Target Audience") {
                            Text("Select Your Primary Target Audience:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                            Spacer(modifier = Modifier.height(8.dp))
                            val audiences = listOf("Students", "Gamers", "Businesses", "Women", "Men", "Fashion Enthusiasts", "Tech Lovers", "Mixed Youth", "Custom")
                            FlowRowHorizontal(audiences, selectedAudience) {
                                selectedAudience = it
                                persistState()
                            }
                        }
                    }

                    5 -> {
                        // Step 5: Audience Demographics
                        MediaKitGlassCard(title = "Step 5: Audience Demographics") {
                            MediaKitTextField("Primary Country", demoCountry, "e.g. India (85%)") { demoCountry = it; persistState() }
                            MediaKitTextField("Primary Language", demoLang, "e.g. Hindi & English") { demoLang = it; persistState() }
                            MediaKitTextField("Age Group", demoAge, "e.g. 18–24 years (65%)") { demoAge = it; persistState() }
                        }
                    }

                    6 -> {
                        // Step 6: Content Categories
                        MediaKitGlassCard(title = "Step 6: Content Categories") {
                            Text("Select categories that match your content:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                            Spacer(modifier = Modifier.height(8.dp))
                            val allCategories = listOf("Fashion", "Beauty", "Gaming", "Technology", "Fitness", "Education", "Finance", "Travel", "Food", "Lifestyle", "Comedy", "Business", "Other")
                            FlowMultiSelectChips(allCategories, selectedCategories) { updated ->
                                selectedCategories = updated
                                persistState()
                            }
                        }
                    }

                    7 -> {
                        // Step 7: Achievements
                        MediaKitGlassCard(title = "Step 7: Achievements & Past Experience") {
                            MediaKitTextField("Brands Worked With", achievementsBrands, "e.g., Boat, Nykaa, Mamaearth") { achievementsBrands = it; persistState() }
                            MediaKitTextField("Certificates", achievementsCertificates, "e.g., Meta Certified Digital Creator") { achievementsCertificates = it; persistState() }
                            MediaKitTextField("Awards & Honors", achievementsAwards, "e.g., Top 10 Micro Creator 2025") { achievementsAwards = it; persistState() }
                            MediaKitTextField("Special Skills", achievementsSkills, "e.g., 4K Video Editing, Scripting") { achievementsSkills = it; persistState() }
                        }
                    }

                    8 -> {
                        // Step 8: Portfolio Images Upload Simulator
                        MediaKitGlassCard(title = "Step 8: Portfolio Showcase Assets") {
                            UploadSlotRow("Profile Photo", profilePhotoUploaded) { profilePhotoUploaded = !profilePhotoUploaded }
                            UploadSlotRow("Best Reel Screenshot", reelScreenshotUploaded) { reelScreenshotUploaded = !reelScreenshotUploaded }
                            UploadSlotRow("Best Post Screenshot", postUploaded) { postUploaded = !postUploaded }
                            UploadSlotRow("Best Video Thumbnail", thumbnailUploaded) { thumbnailUploaded = !thumbnailUploaded }
                            UploadSlotRow("Best Campaign Asset", campaignUploaded) { campaignUploaded = !campaignUploaded }
                        }
                    }

                    9 -> {
                        // Step 9: Statistics
                        MediaKitGlassCard(title = "Step 9: Key Performance Statistics") {
                            MediaKitTextField("Total Followers / Subscribers", followers, "e.g., 12,500") { followers = it; persistState() }
                            MediaKitTextField("Average Monthly Reach", avgReach, "e.g., 55,000 / month") { avgReach = it; persistState() }
                            MediaKitTextField("Average Views Per Content", avgViews, "e.g., 28,000 / reel") { avgViews = it; persistState() }
                            MediaKitTextField("Engagement Rate", engagementRate, "e.g., 6.8%") { engagementRate = it; persistState() }
                            MediaKitTextField("Total Monthly Views", monthlyViews, "e.g., 180,000") { monthlyViews = it; persistState() }
                        }
                    }

                    10 -> {
                        // Step 10: Live Media Kit Preview
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(Brush.verticalGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A))))
                                .border(1.5.dp, Color(0xFFFFD700), RoundedCornerShape(20.dp))
                                .padding(16.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFFD700)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(creatorName.take(1).uppercase().ifBlank { "C" }, fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(fullName.ifBlank { "Creator Name" }, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("✔", fontSize = 12.sp, color = Color(0xFF38BDF8))
                                        }
                                        Text(creatorName.ifBlank { "@handle" }, fontSize = 12.sp, color = Color(0xFFFFD700))
                                        Text("${city.ifBlank { "Mumbai" }}, ${country.ifBlank { "India" }} • ${email.ifBlank { "contact@creator.com" }}", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                                    }
                                }

                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White.copy(alpha = 0.15f))

                                Text("ABOUT ME", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                Text(bio.ifBlank { "Digital creator making high converting content." }, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text("KEY PERFORMANCE ANALYTICS", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    StatBox("Followers", followers, Modifier.weight(1f))
                                    StatBox("Reach", avgReach, Modifier.weight(1f))
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    StatBox("Avg Views", avgViews, Modifier.weight(1f))
                                    StatBox("Engagement", engagementRate, Modifier.weight(1f))
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text("AUDIENCE & CATEGORIES", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                Text("Target: $selectedAudience | Age: $demoAge | Demographics: $demoCountry ($demoLang)", fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    selectedCategories.take(4).forEach { cat ->
                                        Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(Color(0x22FFD700)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                            Text(cat, fontSize = 9.sp, color = Color(0xFFFFD700))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    11 -> {
                        // Step 11: AI Review & Audit
                        MediaKitGlassCard(title = "Step 11: AI Media Kit Audit") {
                            if (!aiReviewDone && !isAiReviewing) {
                                Button(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        isAiReviewing = true
                                        coroutineScope.launch {
                                            delay(1000)
                                            isAiReviewing = false
                                            aiReviewDone = true
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                                ) {
                                    Text("🔍 Run AI Media Kit Audit", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            } else if (isAiReviewing) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                    CircularProgressIndicator(color = Color(0xFFFFD700))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("AI Scanning 20+ Commercial Parameters...", fontSize = 12.sp, color = Color.White)
                                }
                            } else {
                                Column {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                                        ScoreMeter("Media Kit", "95/100")
                                        ScoreMeter("Professional", "92%")
                                        ScoreMeter("Brand Appeal", "96%")
                                        ScoreMeter("Trust Score", "94%")
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text("💡 AI Insights & Suggestions:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    Text("• High Engagement Rate ($engagementRate) puts you in the top 5% micro-creators.", fontSize = 11.sp, color = Color.White)
                                    Text("• Clear demographics ($demoCountry, $demoLang) make you ideal for FMCG & Lifestyle brands.", fontSize = 11.sp, color = Color.White)
                                    Text("• Your 1-page Media Kit formatting is 100% agency-compliant.", fontSize = 11.sp, color = Color.White)
                                }
                            }
                        }
                    }

                    12 -> {
                        // Step 12: Auto Improvements
                        MediaKitGlassCard(title = "Step 12: AI Copy & Rate Enhancements") {
                            Text("AI Enhanced Commercial Summary:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x22FFD700))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    "Commercial-ready $userNiche content engine delivering $avgViews avg views with an industry-leading $engagementRate engagement rate. Specialized in high-impact brand integrations.",
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    lineHeight = 15.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isAiEnhanced = true
                                    Toast.makeText(context, "AI Copy Enhancements Applied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = if (isAiEnhanced) Color(0xFF4ADE80) else Color(0xFFFFD700))
                            ) {
                                Text(if (isAiEnhanced) "✓ AI Enhancements Applied" else "✨ Apply AI Enhancements", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    else -> {
                        // Step 13: Brand Ready Checklist & Achievement Badge
                        MediaKitGlassCard(title = "Step 13: Brand Ready Checklist") {
                            val checklistLabels = listOf(
                                "✔ Professional Bio & Elevator Pitch",
                                "✔ Contact Details & Location",
                                "✔ Verified Performance Statistics",
                                "✔ Target Audience & Demographics",
                                "✔ Content Categories & Verticals",
                                "✔ Portfolio Images & Campaign Proof"
                            )

                            checklistLabels.forEachIndexed { idx, label ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (checklistItems.getOrElse(idx) { false }) Color(0x224ADE80) else Color(0x11FFFFFF))
                                        .clickable {
                                            checklistItems[idx] = !checklistItems[idx]
                                            persistState()
                                        }
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (checklistItems.getOrElse(idx) { false }) "☑" else "☐",
                                        fontSize = 16.sp,
                                        color = if (checklistItems.getOrElse(idx) { false }) Color(0xFF4ADE80) else Color.White
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(label, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Premium Badge Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Brush.horizontalGradient(listOf(Color(0xFFFFD700), Color(0xFFFFA500))))
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🏆", fontSize = 24.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text("Professional Media Kit Ready", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                        Text("Level 3 Achievement Unlocked! +300 XP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black.copy(alpha = 0.8f))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Today's Mission Footer Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x221E293B))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("TODAY'S MISSION", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Text("Create Your First Professional Media Kit", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x33FFD700))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("⏱ 12 Mins | +300 XP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Buttons (Back & Continue)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                currentStep--
                                persistState()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                        ) {
                            Text("Back", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (currentStep < 13) {
                                currentStep++
                                persistState()
                            } else {
                                isAlreadyCompleted = true
                                persistState(completed = true)
                                onLevel3Completed()
                            }
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                    ) {
                        Text(
                            text = if (currentStep == 13) "Finish Level 3 🎉" else "Continue ➔",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaKitGlassCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x221E293B))
            .border(1.dp, Color(0x33FFD700), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun MediaKitTextField(label: String, value: String, placeholder: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color(0xFFFFD700), fontSize = 11.sp) },
        placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.35f), fontSize = 11.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFFFD700),
            unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRowHorizontal(items: List<String>, selected: String, onSelect: (String) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { item ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (item == selected) Color(0xFFFFD700) else Color(0x22FFFFFF))
                    .clickable { onSelect(item) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(item, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (item == selected) Color.Black else Color.White)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowMultiSelectChips(items: List<String>, selected: List<String>, onUpdate: (List<String>) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { item ->
            val isSel = selected.contains(item)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                    .clickable {
                        val updated = if (isSel) selected - item else selected + item
                        onUpdate(updated)
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(if (isSel) "✓ $item" else item, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
            }
        }
    }
}

@Composable
private fun UploadSlotRow(label: String, isUploaded: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x15FFFFFF))
            .clickable { onToggle() }
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (isUploaded) Color(0x334ADE80) else Color(0x33FFD700))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(if (isUploaded) "✓ Uploaded" else "+ Tap to Select", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isUploaded) Color(0xFF4ADE80) else Color(0xFFFFD700))
        }
    }
}

@Composable
private fun StatBox(label: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x22FFFFFF))
            .padding(10.dp)
    ) {
        Column {
            Text(label, fontSize = 9.sp, color = Color.White.copy(alpha = 0.6f))
            Text(value.ifBlank { "0" }, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        }
    }
}

@Composable
private fun ScoreMeter(title: String, score: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color(0x22FFD700))
                .border(1.5.dp, Color(0xFFFFD700), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(score, fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(title, fontSize = 9.sp, color = Color.White.copy(alpha = 0.8f))
    }
}
