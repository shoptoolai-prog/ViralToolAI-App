package com.example.ui.components

import com.example.creatoracademy.ViralAiMentorEngine
import com.example.creatoracademy.MentorToolDomain
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import com.example.ui.theme.responsiveImeAndNavPadding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.ui.theme.EmeraldGlow
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.creatoracademy.CreatorAcademyPrefs
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

private val EmeraldPrimary = Color(0xFFFF4081) // Premium Pink theme for Meesho Creator AI
private val EmeraldGlow = Color(0x33FF4081) // Pink Glow Effect
private val meeshoTheme = MentorToolTheme.MeeshoCreator

/**
 * MEESHO CREATOR AI V3 — ZERO TO HERO LEARNING SYSTEM
 * Unlocked Free Access Tool
 * Features:
 * 1. UNLOCKED FREE STATUS with "⭐ Most Used Tool" animated badge
 * 2. Premium Welcome Card "🚀 One Day Meesho Creator Setup" (30-60 Mins)
 * 3. Mandatory Language Selection (Hindi 🇮🇳, English 🇺🇸, HinEnglish 🌐)
 * 4. 8 Roadmap Swipe Cards with smooth step navigation
 * 5. 8 Guided Conversational Lessons with easy visual screen guidance
 * 6. Interactive Confirmation Prompts ("Did you complete this step?" / "What do you see on screen?")
 * 7. Direct Official Play Store & Meesho Creator Web Links
 * 8. Wallet & Commission Earnings Explanation
 * 9. Review Video Guide & AI Caption/Hashtag/CTA Generator
 * 10. Interactive Final Checklist & Celebration with Next Learning Paths
 */

// Data models
data class MeeshoRoadmapCard(
    val cardNumber: Int,
    val iconEmoji: String,
    val titleEnglish: String,
    val titleHindi: String,
    val titleHinglish: String,
    val subtitleEnglish: String,
    val subtitleHindi: String,
    val subtitleHinglish: String,
    val bulletPointsEnglish: List<String>,
    val bulletPointsHindi: List<String>,
    val bulletPointsHinglish: List<String>
)

data class MeeshoLessonItem(
    val stepNumber: Int,
    val titleEnglish: String,
    val titleHindi: String,
    val titleHinglish: String,
    val contentEnglish: String,
    val contentHindi: String,
    val contentHinglish: String,
    val visualCueEnglish: String,
    val visualCueHindi: String,
    val visualCueHinglish: String,
    val practicalTaskEnglish: String,
    val practicalTaskHindi: String,
    val practicalTaskHinglish: String,
    val directActionUrl: String? = null,
    val directActionLabel: String? = null
)

data class MeeshoChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val isFromUser: Boolean,
    val text: String,
    val timestamp: String = "Just now",
    val isLessonStep: Boolean = false,
    val stepNumber: Int = 0,
    val stepTitle: String = "",
    val visualCueText: String? = null,
    val practicalTaskText: String? = null,
    val directActionUrl: String? = null,
    val directActionLabel: String? = null,
    val showConfirmationPrompt: Boolean = false,
    val isSimplerExplanation: Boolean = false
)

object MeeshoCreatorStaticData {

    // 8 Roadmap Cards
    val roadmapCards = listOf(
        MeeshoRoadmapCard(
            cardNumber = 1,
            iconEmoji = "📱",
            titleEnglish = "1. Install Meesho App",
            titleHindi = "1. मीशो ऐप डाउनलोड करें",
            titleHinglish = "1. Install Meesho App",
            subtitleEnglish = "Get the official Meesho app from Google Play Store.",
            subtitleHindi = "गूगल प्ले स्टोर से ऑफिशियल मीशो ऐप डाउनलोड करें।",
            subtitleHinglish = "Google Play Store se official Meesho app download karo.",
            bulletPointsEnglish = listOf("100M+ Downloads on Play Store", "Official Android app", "1-Tap direct install link"),
            bulletPointsHindi = listOf("प्ले स्टोर पर 10 करोड़+ डाउनलोड", "ऑफिशियल एंड्रॉइड ऐप", "1-क्लिक डायरेक्ट डाउनलोड लिंक"),
            bulletPointsHinglish = listOf("Play Store par 100M+ downloads", "Official Android app", "1-Tap direct install link")
        ),
        MeeshoRoadmapCard(
            cardNumber = 2,
            iconEmoji = "👤",
            titleEnglish = "2. Create Meesho Account",
            titleHindi = "2. मीशो अकाउंट बनाएं",
            titleHinglish = "2. Create Meesho Account",
            subtitleEnglish = "Sign in using your 10-digit mobile number & OTP.",
            subtitleHindi = "अपने 10-अंकों के मोबाइल नंबर और OTP से साइन इन करें।",
            subtitleHinglish = "Apne 10-digit mobile number aur OTP se sign in karo.",
            bulletPointsEnglish = listOf("Instant 10-second OTP login", "Free account creation", "Safe & verified user setup"),
            bulletPointsHindi = listOf("तुरंत 10-सेकंड में OTP लॉगिन", "बिल्कुल फ्री अकाउंट", "सुरक्षित और वेरीफाइड सेटअप"),
            bulletPointsHinglish = listOf("Instant 10-sec OTP login", "100% free account", "Safe & verified setup")
        ),
        MeeshoRoadmapCard(
            cardNumber = 3,
            iconEmoji = "🎯",
            titleEnglish = "3. Join Meesho Creator",
            titleHindi = "3. मीशो क्रिएटर प्रोग्राम जॉइन करें",
            titleHinglish = "3. Join Meesho Creator Program",
            subtitleEnglish = "Register for official Meesho affiliate & review program.",
            subtitleHindi = "ऑफिशियल मीशो एफ़िलिएट और क्रिएटर प्रोग्राम में रजिस्टर करें।",
            subtitleHinglish = "Official Meesho affiliate & creator program me register karo.",
            bulletPointsEnglish = listOf("Earn up to 15% commission", "Access creator dashboard", "Get product codes & links"),
            bulletPointsHindi = listOf("15% तक कमीशन कमाएं", "क्रिएटर डैशबोर्ड का एक्सेस पाएं", "प्रोडक्ट कोड्स और लिंक्स पाएं"),
            bulletPointsHinglish = listOf("15% tak commission kamao", "Creator dashboard access", "Product codes aur links pao")
        ),
        MeeshoRoadmapCard(
            cardNumber = 4,
            iconEmoji = "✅",
            titleEnglish = "4. Complete Profile",
            titleHindi = "4. प्रोफाइल पूरी करें",
            titleHinglish = "4. Complete Profile",
            subtitleEnglish = "Link your Instagram handle & select your content category.",
            subtitleHindi = "अपना इंस्टाग्राम हैंडल लिंक करें और कंटेंट कैटेगरी चुनें।",
            subtitleHinglish = "Apna Instagram handle link karo aur content category chuno.",
            bulletPointsEnglish = listOf("Link Instagram handle (@yourname)", "Select Fashion/Tech/Home niche", "Submit for creator review"),
            bulletPointsHindi = listOf("इंस्टाग्राम हैंडल (@yourname) जोड़ें", "फैशन/टेक/होम कैटेगरी चुनें", "क्रिएटर रिव्यू के लिए सबमिट करें"),
            bulletPointsHinglish = listOf("Instagram handle link karo", "Fashion/Tech/Home category chuno", "Approval ke liye submit karo")
        ),
        MeeshoRoadmapCard(
            cardNumber = 5,
            iconEmoji = "💰",
            titleEnglish = "5. Understand Earnings",
            titleHindi = "5. वॉलेट और कमाई समझें",
            titleHinglish = "5. Understand Wallet & Earnings",
            subtitleEnglish = "Learn how commissions, returns, and UPI payouts work.",
            subtitleHindi = "जाने कमीशन, रिटर्न पीरियड और UPI बैंक ट्रांसफर कैसे काम करता है।",
            subtitleHinglish = "Jaano commission, return period aur UPI payout kaise milta hai.",
            bulletPointsEnglish = listOf("Up to 15% margin on every order", "Payout after 7-day return window", "Direct bank / UPI wallet transfer"),
            bulletPointsHindi = listOf("हर ऑर्डर पर 15% तक मार्जिन", "7-दिन के रिटर्न के बाद पेआउट", "डायरेक्ट बैंक/UPI में ट्रांसफर"),
            bulletPointsHinglish = listOf("Har order par 15% tak commission", "7-day return window ke baad payout", "Direct bank/UPI wallet transfer")
        ),
        MeeshoRoadmapCard(
            cardNumber = 6,
            iconEmoji = "🎥",
            titleEnglish = "6. Create First Video",
            titleHindi = "6. पहली रिव्यू वीडियो बनाएं",
            titleHinglish = "6. Create First Review Video",
            subtitleEnglish = "Select a budget product and record a catchy 15-sec Reel.",
            subtitleHindi = "कम बजट वाला प्रोडक्ट चुनें और 15-सेकंड की रील बनाएं।",
            subtitleHinglish = "Budget product chuno aur 15-sec ki catchy Reel record karo.",
            bulletPointsEnglish = listOf("Choose trending product under ₹500", "Show close-up quality & unboxing", "Keep natural & friendly tone"),
            bulletPointsHindi = listOf("₹500 के अंदर का प्रोडक्ट चुनें", "अनबॉक्सिंग और क्वालिटी क्लोज-अप दिखाएं", "फ्रेंडली टोन में रिव्यू करें"),
            bulletPointsHinglish = listOf("₹500 ke andar product chuno", "Unboxing & quality show karo", "Friendly tone me speak karo")
        ),
        MeeshoRoadmapCard(
            cardNumber = 7,
            iconEmoji = "📤",
            titleEnglish = "7. Upload with AI Captions",
            titleHindi = "7. AI कैप्शन्स के साथ अपलोड करें",
            titleHinglish = "7. Upload with AI Captions",
            subtitleEnglish = "Generate viral captions, hashtags, and strong CTA.",
            subtitleHindi = "वायरल कैप्शन, हैशटैग्स और मजबूत CTA जनरेट करें।",
            subtitleHinglish = "Viral caption, hashtags aur strong CTA generate karo.",
            bulletPointsEnglish = listOf("High-converting Call To Action", "Viral Meesho hashtags", "Comment automation trigger"),
            bulletPointsHindi = listOf("हाई-कन्वर्टिंग Call To Action", "वायरल मीशो हैशटैग्स", "कमेंट ऑटोमेशन ट्रिगर"),
            bulletPointsHinglish = listOf("High-converting CTA", "Viral Meesho hashtags", "Comment automation trigger")
        ),
        MeeshoRoadmapCard(
            cardNumber = 8,
            iconEmoji = "📈",
            titleEnglish = "8. Grow Your Earnings",
            titleHindi = "8. डेली कमाई बढ़ाएं",
            titleHinglish = "8. Grow Your Daily Earnings",
            subtitleEnglish = "Post daily and scale your earnings to ₹10k–₹50k/month.",
            subtitleHindi = "रोज़ पोस्ट करें और महीने के ₹10k–₹50k तक कमाएं।",
            subtitleHinglish = "Daily post karo aur monthly ₹10k-₹50k earnings scale karo.",
            bulletPointsEnglish = listOf("Daily consistency formula", "Track top-selling items", "Scale to recurring monthly income"),
            bulletPointsHindi = listOf("डेली कंसिस्टेंसी फार्मूला", "टॉप सेलिंग प्रोडक्ट्स ट्रैक करें", "मंथली पैसिव इनकम बनाएं"),
            bulletPointsHinglish = listOf("Daily consistency formula", "Top-selling products track karo", "Monthly passive income banao")
        )
    )

    // 8 Step Guided Lessons
    val guidedLessons = listOf(
        MeeshoLessonItem(
            stepNumber = 1,
            titleEnglish = "Step 1: Install Official Meesho App",
            titleHindi = "स्टेप 1: ऑफिशियल मीशो ऐप इंस्टॉल करें",
            titleHinglish = "Step 1: Official Meesho App Install Karo",
            contentEnglish = "Welcome to Meesho Creator AI! 😄 Today we will complete your entire setup step-by-step from zero! First step: Open Google Play Store on your phone, search 'Meesho', and tap the install button.",
            contentHindi = "मीशो क्रिएटर एआई में आपका स्वागत है! 😄 आज हम एकदम ज़ीरो से आपका पूरा सेटअप स्टेप-बाय-स्टेप पूरा करेंगे! पहला कदम: अपने फ़ोन में Google Play Store खोलें, 'Meesho' लिखें और इंस्टॉल बटन दबाएं।",
            contentHinglish = "Meesho Creator AI me aapka swagat hai! 😄 Aaj hum zero se aapka pura setup step-by-step complete karenge! First step: Apne phone me Google Play Store kholo, 'Meesho' search karo aur Install button dabao.",
            visualCueEnglish = "Look for the official pink Meesho logo app with 100M+ downloads in Play Store.",
            visualCueHindi = "प्ले स्टोर पर 10 करोड़+ डाउनलोड वाले ऑफिशियल गुलाबी मीशो लोगो वाले ऐप को देखें।",
            visualCueHinglish = "Play Store par 100M+ downloads wale official pink Meesho logo app ko dekho.",
            practicalTaskEnglish = "🎯 Mission 1: Download Meesho app from Play Store or tap the button below.",
            practicalTaskHindi = "🎯 मिशन 1: प्ले स्टोर से मीशो ऐप डाउनलोड करें या नीचे बटन पर टैप करें।",
            practicalTaskHinglish = "🎯 Mission 1: Play Store se Meesho app download karo ya niche button tap karo.",
            directActionUrl = "market://details?id=com.meesho.supply",
            directActionLabel = "📱 Open Meesho in Play Store"
        ),
        MeeshoLessonItem(
            stepNumber = 2,
            titleEnglish = "Step 2: Create Meesho Account",
            titleHindi = "स्टेप 2: मीशो अकाउंट बनाएं",
            titleHinglish = "Step 2: Meesho Account Banao",
            contentEnglish = "Great job downloading the app! Now open Meesho. Tap 'Account' at the bottom right corner, then tap 'Sign Up' or 'Login'. Enter your 10-digit mobile number and verify via OTP.",
            contentHindi = "ऐप डाउनलोड करने के लिए बहुत बढ़िया! अब मीशो ऐप खोलें। नीचे दायें कोने में 'Account' आइकॉन पर टैप करें, फिर 'Sign Up' दबाएं। अपना 10-अंकों का मोबाइल नंबर डालें और OTP से वेरीफाई करें।",
            contentHinglish = "App download karne ke liye awesome! Ab Meesho kholo. Bottom right me 'Account' icon par tap karo, phir 'Sign Up' dabao. Apna phone number daal kar OTP se verify karo.",
            visualCueEnglish = "Look for the pink 'Account' icon at the bottom right navigation bar of Meesho home screen.",
            visualCueHindi = "मीशो होम स्क्रीन के नीचे दायें तरफ बने गुलाबी 'Account' आइकॉन को देखें।",
            visualCueHinglish = "Meesho homepage ke bottom right me pink 'Account' icon ko dekho.",
            practicalTaskEnglish = "🎯 Mission 2: Complete phone number OTP verification and set your full name.",
            practicalTaskHindi = "🎯 मिशन 2: फोन नंबर OTP वेरीफाई करें और अपना नाम सेट करें।",
            practicalTaskHinglish = "🎯 Mission 2: Phone number OTP verify karke apna name set karo.",
            directActionUrl = "https://www.meesho.com",
            directActionLabel = "🌐 Open Meesho Web"
        ),
        MeeshoLessonItem(
            stepNumber = 3,
            titleEnglish = "Step 3: Join Meesho Creator Program",
            titleHindi = "स्टेप 3: मीशो क्रिएटर प्रोग्राम जॉइन करें",
            titleHinglish = "Step 3: Meesho Creator Program Join Karo",
            contentEnglish = "Awesome! Now inside the Meesho app, go to the 'Account' tab. Look for 'Meesho Creator' or 'Earn with Meesho' banner. Tap 'Apply Now' to begin your affiliate creator registration.",
            contentHindi = "शाबाश! अब मीशो ऐप में 'Account' टैब में जाएँ। 'Meesho Creator' या 'Earn with Meesho' वाले बैनर को ढूंढें। अपने एफ़िलिएट क्रिएटर रजिस्ट्रेशन के लिए 'Apply Now' दबाएं।",
            contentHinglish = "Shabaash! Ab Meesho app me 'Account' tab me jao. 'Meesho Creator' ya 'Earn with Meesho' banner ko dhoondho aur 'Apply Now' tap karo.",
            visualCueEnglish = "Look for a colorful banner displaying 'Earn up to ₹25,000/month as Meesho Creator' inside the Account menu.",
            visualCueHindi = "अकाउंट मेनू के अंदर 'Earn up to ₹25,000/month as Meesho Creator' लिखा हुआ कलरफुल बैनर देखें।",
            visualCueHinglish = "Account menu ke andar 'Earn up to ₹25,000/month' wala banner dekho.",
            practicalTaskEnglish = "🎯 Mission 3: Tap 'Apply Now' on the Meesho Creator banner.",
            practicalTaskHindi = "🎯 मिशन 3: मीशो क्रिएटर बैनर पर 'Apply Now' दबाएं।",
            practicalTaskHinglish = "🎯 Mission 3: Meesho Creator banner par 'Apply Now' dabao.",
            directActionUrl = "https://www.meesho.com",
            directActionLabel = "🎯 Visit Meesho Creator Page"
        ),
        MeeshoLessonItem(
            stepNumber = 4,
            titleEnglish = "Step 4: Link Profile & Instagram",
            titleHindi = "स्टेप 4: अपनी प्रोफाइल और इंस्टाग्राम लिंक करें",
            titleHinglish = "Step 4: Profile & Instagram Link Karo",
            contentEnglish = "Now connect your social media! Enter your Instagram handle (e.g. `@yourname`), approximate follower count, and primary content category (Fashion, Tech, Beauty, or Home).",
            contentHindi = "अब अपना सोशल मीडिया लिंक करें! अपना इंस्टाग्राम हैंडल (जैसे `@yourname`), फॉलोअर्स की संख्या और कंटेंट कैटेगरी (फैशन, टेक, ब्यूटी, या होम) दर्ज करें।",
            contentHinglish = "Ab apna social media link karo! Apna Instagram handle (`@yourname`), follower count aur content category (Fashion, Tech, Beauty, Home) fill karo.",
            visualCueEnglish = "You will see fields asking for Instagram profile link or handle and follower category.",
            visualCueHindi = "आपको इंस्टाग्राम प्रोफाइल लिंक या हैंडल और फॉलोअर कैटेगरी वाले फॉर्म फ़ील्ड दिखेंगे।",
            visualCueHinglish = "Aapko Instagram handle aur follower category puchne waale form fields dikhenge.",
            practicalTaskEnglish = "🎯 Mission 4: Submit your Instagram handle and content category for creator review.",
            practicalTaskHindi = "🎯 मिशन 4: रिव्यू के लिए अपना इंस्टाग्राम हैंडल और कैटेगरी सबमिट करें।",
            practicalTaskHinglish = "🎯 Mission 4: Review ke liye apna Instagram handle aur category submit karo."
        ),
        MeeshoLessonItem(
            stepNumber = 5,
            titleEnglish = "Step 5: Understand Wallet & Earnings",
            titleHindi = "स्टेप 5: वॉलेट और कमाई सिस्टम समझें",
            titleHinglish = "Step 5: Wallet & Earnings System Samjho",
            contentEnglish = "Here is how you earn money! Whenever someone orders a product using your Meesho link or code, you earn up to 15% commission. Once the 7-day return period ends, your earnings transfer directly to your UPI/Bank account!",
            contentHindi = "जानिए कमाई कैसे होती है! जब भी कोई आपके मीशो लिंक या कोड से सामान खरीदेगा, आपको 15% तक कमीशन मिलेगा। 7-दिन का रिटर्न पीरियड खत्म होते ही पैसे सीधे आपके UPI या बैंक अकाउंट में ट्रांसफर हो जाएंगे!",
            contentHinglish = "Earnings kaise milti hai samjho! Jab koi aapke Meesho link ya code se order karega, aapko 15% tak commission milega. 7-day return period khatam hote hi paise bank/UPI me transfer ho jaate hain!",
            visualCueEnglish = "Go to 'Account' -> 'My Earnings' / 'Bank Details' to add your UPI ID or bank account for payouts.",
            visualCueHindi = "'Account' -> 'My Earnings' / 'Bank Details' में जाकर पेआउट के लिए अपना UPI ID या बैंक अकाउंट जोड़ें।",
            visualCueHinglish = "'Account' -> 'My Earnings' me jaakar payouts ke liye apna UPI ID / Bank Account add karo.",
            practicalTaskEnglish = "🎯 Mission 5: Go to 'My Earnings' in Meesho app and verify your bank/UPI settings.",
            practicalTaskHindi = "🎯 मिशन 5: मीशो ऐप में 'My Earnings' में जाकर अपना बैंक/UPI अकाउंट वेरीफाई करें।",
            practicalTaskHinglish = "🎯 Mission 5: Meesho app me 'My Earnings' me jaakar bank/UPI details link karo."
        ),
        MeeshoLessonItem(
            stepNumber = 6,
            titleEnglish = "Step 6: Create Your First Review Video",
            titleHindi = "स्टेप 6: अपनी पहली रिव्यू वीडियो बनाएं",
            titleHinglish = "Step 6: Apni Pehli Review Video Banao",
            contentEnglish = "Time for action! Pick a popular budget product under ₹500 (e.g. Trendy Kurti, Smartwatch, Earbuds, or Home Organizer). Record a short 15-30 sec Reel showing product quality, unboxing, and close-up detail.",
            contentHindi = "कंटेंट बनाने का समय! ₹500 के अंदर का कोई पॉपुलर प्रोडक्ट चुनें (जैसे ट्रेंडिंग कुर्ती, स्मार्टवॉच, इयरबड्स, या होम आर्गेनाइजर)। 15-30 सेकंड की एक छोटी रील रिकॉर्ड करें जिसमें अनबॉक्सिंग और क्वालिटी क्लोज-अप दिखाएं।",
            contentHinglish = "Content banane ka time! ₹500 ke andar ka popular product chuno. 15-30 sec ki Reel record karo jisme unboxing, fabric/build quality aur close-up shots ho.",
            visualCueEnglish = "Record near natural window light and speak friendly like recommending to a close friend.",
            visualCueHindi = "खिड़की की नेचुरल लाइट में रिकॉर्ड करें और दोस्त को सलाह देने जैसे सादे अंदाज में बोलें।",
            visualCueHinglish = "Natural window light me record karo aur friends ki tarah naturally bol kar review do.",
            practicalTaskEnglish = "🎯 Mission 6: Pick 1 Meesho product code and outline a 15-second review script.",
            practicalTaskHindi = "🎯 मिशन 6: 1 मीशो प्रोडक्ट कोड चुनें और 15-सेकंड का रील स्क्रिप्ट तैयार करें।",
            practicalTaskHinglish = "🎯 Mission 6: 1 Meesho product code select karke 15-sec review script outline karo."
        ),
        MeeshoLessonItem(
            stepNumber = 7,
            titleEnglish = "Step 7: Upload with AI Caption & CTA",
            titleHindi = "स्टेप 7: AI कैप्शन और CTA के साथ अपलोड करें",
            titleHinglish = "Step 7: AI Caption aur CTA ke Saath Upload Karo",
            contentEnglish = "Posting time! Write a strong Call To Action: 'Comment LINK or Product Code for instant Meesho link!' Use our built-in Caption Generator tab below to get viral hashtags and keywords.",
            contentHindi = "पोस्ट करने का समय! मजबूत Call To Action लिखें: 'इंस्टेंट लिंक के लिए कमेंट में LINK या कोड टाइप करें!' वायरल हैशटैग्स पाने के लिए नीचे दिए गए AI Caption Generator टैब का उपयोग करें।",
            contentHinglish = "Posting time! Strong CTA likho: 'Instant link ke liye comment me LINK or Product Code dalo!' Viral hashtags ke liye niche AI Caption Generator tab use karo.",
            visualCueEnglish = "Always pin your top comment containing the Meesho Product Code or DM automation keyword.",
            visualCueHindi = "अपनी रील में प्रोडक्ट कोड या डीएम ऑटोमेशन कीवर्ड वाला टॉप कमेंट हमेशा पिन करें।",
            visualCueHinglish = "Apni Reel me Product Code wala comment hamesha top par pin karke rakho.",
            practicalTaskEnglish = "🎯 Mission 7: Use our AI Caption Generator tab to generate viral caption for your product.",
            practicalTaskHindi = "🎯 मिशन 7: अपने प्रोडक्ट के लिए AI Caption Generator टैब से वायरल कैप्शन जनरेट करें।",
            practicalTaskHinglish = "🎯 Mission 7: Hamare AI Caption Generator tab se product ke liye caption copy karo."
        ),
        MeeshoLessonItem(
            stepNumber = 8,
            titleEnglish = "Step 8: Scale & Grow Daily Earnings",
            titleHindi = "स्टेप 8: डेली कमाई बढ़ाएं और स्केल करें",
            titleHinglish = "Step 8: Daily Earnings Scale aur Grow Karo",
            contentEnglish = "Consistency is the secret to earning ₹10k–₹50k every month! Post 1 product review Reel daily, reply to all comments with product codes, and track top-selling products in your Meesho Creator Dashboard.",
            contentHindi = "महीने के ₹10k–₹50k कमाने का सीक्रेट कंसिस्टेंसी है! रोज 1 प्रोडक्ट रील पोस्ट करें, सभी कमेंट्स का जवाब प्रोडक्ट कोड के साथ दें और मीशो क्रिएटर डैशबोर्ड में सबसे ज्यादा बिकने वाले प्रोडक्ट्स चेक करें।",
            contentHinglish = "Monthly ₹10k-₹50k earn karne ka secret consistency hai! Daily 1 Reel upload karo, har comment ka reply product code se do aur Meesho Creator Dashboard me top-selling products track karo.",
            visualCueEnglish = "Check your Meesho Creator Dashboard every morning for clicks and order conversions.",
            visualCueHindi = "रोज सुबह अपने मीशो क्रिएटर डैशबोर्ड में क्लिक्स और ऑर्डर्स की रिपोर्ट चेक करें।",
            visualCueHinglish = "Daily morning me Meesho Creator Dashboard me clicks aur conversions analyze karo.",
            practicalTaskEnglish = "🎯 Mission 8: Schedule 3 product review Reel ideas for this week!",
            practicalTaskHindi = "🎯 मिशन 8: इस हफ्ते के लिए 3 प्रोडक्ट रील आइडियाज शेड्यूल करें।",
            practicalTaskHinglish = "🎯 Mission 8: Is week ke liye 3 product review Reel ideas schedule karo."
        )
    )
}

/**
 * UNLOCKED MEESHO CREATOR AI CARD FOR HOME SCREEN
 */
@Composable
fun MeeshoCreatorAiCard(
    onComingSoonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "meeshoCardScale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "meeshoGlow")
    val borderPulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "meeshoBorderPulse"
    )
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "meeshoShimmerOffset"
    )

    val bulletFeatures = listOf(
        "Step-by-Step Meesho Creator Setup",
        "High-Commission Product Finder",
        "Viral Reel Scripts & Video Hooks",
        "One-Click AI Caption & Tag Generator",
        "Live Wallet & Earnings Growth Guide"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = EmeraldPrimary.copy(alpha = 0.55f),
                ambientColor = Color(0x10000000)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF24131D),
                        Color(0xFF140B12)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.2.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            EmeraldPrimary.copy(alpha = borderPulseAlpha),
                            Color(0xFF00E676),
                            Color.White.copy(alpha = 0.25f)
                        ),
                        start = androidx.compose.ui.geometry.Offset(shimmerOffset, 0f),
                        end = androidx.compose.ui.geometry.Offset(shimmerOffset + 350f, 250f)
                    )
                ),
                RoundedCornerShape(24.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onComingSoonClick()
                }
            )
            .padding(18.dp)
    ) {
        // Glass Shine Sweep Overlay
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(24.dp))
        ) {
            val sweepX = shimmerOffset
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.02f),
                        Color.White.copy(alpha = 0.1f),
                        Color.White.copy(alpha = 0.02f),
                        Color.Transparent
                    )
                ),
                start = androidx.compose.ui.geometry.Offset(sweepX, 0f),
                end = androidx.compose.ui.geometry.Offset(sweepX + 160f, size.height),
                strokeWidth = 24.dp.toPx()
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            ToolHeroBanner(
                toolType = ToolHeroType.MEESHO_CREATOR,
                height = 100.dp,
                badgeText = "🔥 MEESHO CREATOR AI",
                subtitleText = "Reseller Commission & Growth Mentor"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Top Row Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimary.copy(alpha = 0.18f))
                            .border(
                                BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        com.example.ui.screens.OfficialLogo(name = "meesho", modifier = Modifier.size(20.dp))
                    }

                    Column {
                        Text(
                            text = "Meesho Creator AI",
                            fontSize = 17.5.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = (-0.3).sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "ZERO TO HERO AFFILIATE SYSTEM",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = com.example.ui.theme.EmeraldGlow,
                            letterSpacing = 1.2.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x2210B981))
                        .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "MOST USED",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.ui.theme.EmeraldGlow,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 5 Bullet Features
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                bulletFeatures.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = com.example.ui.theme.EmeraldGlow,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                        Text(
                            text = feature,
                            fontSize = 12.5.sp,
                            color = TextWhite.copy(alpha = 0.85f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Large Premium Pill Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(23.dp),
                        spotColor = EmeraldPrimary
                    )
                    .clip(RoundedCornerShape(23.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(EmeraldPrimary, com.example.ui.theme.EmeraldGlow)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start Learning",
                        tint = AmoledBlack,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "START LEARNING",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Black,
                        color = AmoledBlack,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }
    }
}

/**
 * MEESHO CREATOR AI V3 — FULL DIALOG EXPERIENCE
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeeshoCreatorAiDialog(
    onDismiss: () -> Unit,
    onNavigateToBrandCollab: () -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current

    // Preferences & Saved State
    var selectedLanguage by remember {
        mutableStateOf(CreatorAcademyPrefs.getMeeshoLanguage(context).ifBlank { "HinEnglish" })
    }
    var isLanguageSelected by remember {
        mutableStateOf(CreatorAcademyPrefs.getMeeshoLanguage(context).isNotBlank())
    }
    var isWelcomeCompleted by remember { mutableStateOf(false) }
    var isRoadmapCompleted by remember { mutableStateOf(false) }

    val savedStepIndex = remember { CreatorAcademyPrefs.getMeeshoStepIndex(context) }
    var currentStepIndex by remember { mutableIntStateOf(if (savedStepIndex in 0..7) savedStepIndex else 0) }

    var isEntranceVisible by remember { mutableStateOf(false) }

    // Navigation Tabs inside Mentor Interface
    var activeTab by remember { mutableStateOf("MENTOR_CHAT") } // MENTOR_CHAT, DIRECT_LINKS, CAPTION_GEN, CHECKLIST

    // Roadmap Card Swipe Index
    var currentRoadmapIndex by remember { mutableIntStateOf(0) }

    // Chat Message State
    val chatMessages = remember { mutableStateListOf<MeeshoChatMessage>() }
    var isThinking by remember { mutableStateOf(false) }
    var thinkingText by remember { mutableStateOf("") }
    var customUserInput by remember { mutableStateOf("") }

    // Caption Generator Tab State
    var captionProductName by remember { mutableStateOf("Trendy Floral Print Kurti") }
    var captionProductCode by remember { mutableStateOf("123456") }
    var generatedCaptionTitle by remember { mutableStateOf("") }
    var generatedCaptionText by remember { mutableStateOf("") }
    var generatedHashtags by remember { mutableStateOf("") }
    var generatedCta by remember { mutableStateOf("") }
    var isGeneratingCaption by remember { mutableStateOf(false) }

    // Final Checklist State
    var check1 by remember { mutableStateOf(true) }
    var check2 by remember { mutableStateOf(true) }
    var check3 by remember { mutableStateOf(savedStepIndex >= 2) }
    var check4 by remember { mutableStateOf(savedStepIndex >= 3) }
    var check5 by remember { mutableStateOf(savedStepIndex >= 4) }
    var check6 by remember { mutableStateOf(savedStepIndex >= 5) }
    var check7 by remember { mutableStateOf(savedStepIndex >= 6) }

    var isSessionCompleted by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // Trigger Entrance
    LaunchedEffect(Unit) {
        isEntranceVisible = true
    }

    // Function to load guided step lesson
    fun loadGuidedStep(stepIdx: Int, isSimpler: Boolean = false) {
        scope.launch {
            isThinking = true
            thinkingText = if (isSimpler) "🧠 Simplifying Meesho guide with visual details..." else "🔍 Deep Search: Fetching Meesho Creator guidelines..."
            delay(750)

            val lessonItem = MeeshoCreatorStaticData.guidedLessons.getOrElse(stepIdx) {
                MeeshoCreatorStaticData.guidedLessons.first()
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

            val visualCue = when (selectedLanguage) {
                "Hindi" -> lessonItem.visualCueHindi
                "English" -> lessonItem.visualCueEnglish
                else -> lessonItem.visualCueHinglish
            }

            val taskText = when (selectedLanguage) {
                "Hindi" -> lessonItem.practicalTaskHindi
                "English" -> lessonItem.practicalTaskEnglish
                else -> lessonItem.practicalTaskHinglish
            }

            // Generate fresh AI mentor response
            val finalExplanation = fetchMeeshoStepExplanation(
                stepIndex = stepIdx + 1,
                stepTitle = title,
                baseContent = baseContent,
                selectedLanguage = selectedLanguage,
                isSimpler = isSimpler
            )

            isThinking = false

            val msg = MeeshoChatMessage(
                isFromUser = false,
                text = finalExplanation,
                isLessonStep = true,
                stepNumber = stepIdx + 1,
                stepTitle = title,
                visualCueText = visualCue,
                practicalTaskText = taskText,
                directActionUrl = lessonItem.directActionUrl,
                directActionLabel = lessonItem.directActionLabel,
                showConfirmationPrompt = true,
                isSimplerExplanation = isSimpler
            )

            chatMessages.add(msg)
            delay(100)
            if (chatMessages.isNotEmpty()) {
                listState.animateScrollToItem(chatMessages.size - 1)
            }
        }
    }

    // Function to handle custom query to Meesho AI Mentor
    fun sendCustomUserQuery(query: String) {
        if (query.isBlank()) return
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

        val userMsg = MeeshoChatMessage(isFromUser = true, text = query)
        chatMessages.add(userMsg)
        customUserInput = ""

        scope.launch {
            if (chatMessages.isNotEmpty()) {
                listState.animateScrollToItem(chatMessages.size - 1)
            }
            isThinking = true
            thinkingText = "🧠 Meesho AI Mentor thinking..."
            delay(900)

            val reply = generateMeeshoAiResponse(query, selectedLanguage)
            isThinking = false

            val aiMsg = MeeshoChatMessage(isFromUser = false, text = reply)
            chatMessages.add(aiMsg)
            delay(100)
            if (chatMessages.isNotEmpty()) {
                listState.animateScrollToItem(chatMessages.size - 1)
            }
        }
    }

    // Function to generate AI Captions
    fun generateProductCaption() {
        if (captionProductName.isBlank()) return
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

        scope.launch {
            isGeneratingCaption = true
            delay(800)

            val (title, caption, hashtags, cta) = generateMeeshoCaptionAi(
                productName = captionProductName,
                productCode = captionProductCode,
                language = selectedLanguage
            )

            isGeneratingCaption = false
            generatedCaptionTitle = title
            generatedCaptionText = caption
            generatedHashtags = hashtags
            generatedCta = cta
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
                        // HEADER WITH LOGO, BADGE, PROGRESS & LANG SWITCHER
                        // ==================================================
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
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x2210B981))
                                        .border(BorderStroke(1.2.dp, EmeraldPrimary), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    com.example.ui.screens.OfficialLogo(name = "meesho", modifier = Modifier.size(24.dp))
                                }

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Meesho Creator AI",
                                            fontSize = 15.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0x2210B981))
                                                .border(BorderStroke(0.8.dp, EmeraldPrimary), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 5.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = "FREE",
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Black,
                                                color = EmeraldPrimary
                                            )
                                        }
                                    }

                                    Text(
                                        text = "Lesson ${currentStepIndex + 1} / 8 • Zero to Hero",
                                        fontSize = 10.5.sp,
                                        color = EmeraldPrimary.copy(alpha = 0.9f),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Language Badge Switcher
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0x22FFFFFF))
                                        .border(BorderStroke(1.dp, Color(0x33FFFFFF)), RoundedCornerShape(12.dp))
                                        .clickable {
                                            isLanguageSelected = false
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = when (selectedLanguage) {
                                            "Hindi" -> "🇮🇳 Hindi"
                                            "English" -> "🇺🇸 English"
                                            else -> "🌐 Hinglish"
                                        },
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = TextWhite.copy(alpha = 0.7f),
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clickable { onDismiss() }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        ToolHeroBanner(
                            toolType = ToolHeroType.MEESHO_CREATOR,
                            height = 110.dp,
                            badgeText = "🛍️ MEESHO AI",
                            subtitleText = "Reseller Commission & Growth Mentor"
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if (!isWelcomeCompleted) {
                            // ==================================================
                            // 1. WELCOME SCREEN CARD
                            // ==================================================
                            MeeshoWelcomeScreenView(
                                onStartClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isWelcomeCompleted = true
                                }
                            )
                        } else if (!isLanguageSelected) {
                            // ==================================================
                            // 2. MANDATORY LANGUAGE SELECTION SCREEN
                            // ==================================================
                            MeeshoLanguageSelectionView(
                                currentLanguage = selectedLanguage,
                                onSelectLanguage = { lang ->
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedLanguage = lang
                                    isLanguageSelected = true
                                    CreatorAcademyPrefs.setMeeshoLanguage(context, lang)
                                }
                            )
                        } else if (!isRoadmapCompleted) {
                            // ==================================================
                            // 3. ROADMAP SWIPE CARDS VIEW (8 CARDS)
                            // ==================================================
                            MeeshoRoadmapSwipeView(
                                currentCardIndex = currentRoadmapIndex,
                                selectedLanguage = selectedLanguage,
                                onNextCard = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    if (currentRoadmapIndex < MeeshoCreatorStaticData.roadmapCards.size - 1) {
                                        currentRoadmapIndex++
                                    } else {
                                        isRoadmapCompleted = true
                                        if (chatMessages.isEmpty()) {
                                            loadGuidedStep(currentStepIndex)
                                        }
                                    }
                                },
                                onPrevCard = {
                                    if (currentRoadmapIndex > 0) currentRoadmapIndex--
                                },
                                onSkipRoadmap = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isRoadmapCompleted = true
                                    if (chatMessages.isEmpty()) {
                                        loadGuidedStep(currentStepIndex)
                                    }
                                }
                            )
                        } else if (isSessionCompleted) {
                            // ==================================================
                            // CELEBRATION & NEXT LEARNING SCREEN
                            // ==================================================
                            MeeshoSessionCompletionView(
                                selectedLanguage = selectedLanguage,
                                onNavigateToBrandCollab = {
                                    onDismiss()
                                    onNavigateToBrandCollab()
                                },
                                onRestart = {
                                    isSessionCompleted = false
                                    currentStepIndex = 0
                                    CreatorAcademyPrefs.setMeeshoStepIndex(context, 0)
                                    chatMessages.clear()
                                    loadGuidedStep(0)
                                },
                                onClose = onDismiss
                            )
                        } else {
                            // ==================================================
                            // 4. MAIN MENTOR INTERFACE WITH TABS
                            // ==================================================
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0x15FFFFFF))
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                MeeshoTabPill("AI Mentor 💬", activeTab == "MENTOR_CHAT") { activeTab = "MENTOR_CHAT" }
                                MeeshoTabPill("Links 🔗", activeTab == "DIRECT_LINKS") { activeTab = "DIRECT_LINKS" }
                                MeeshoTabPill("Captions ✍️", activeTab == "CAPTION_GEN") { activeTab = "CAPTION_GEN" }
                                MeeshoTabPill("Checklist ✅", activeTab == "CHECKLIST") { activeTab = "CHECKLIST" }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            when (activeTab) {
                                "MENTOR_CHAT" -> {
                                    // Chat Messages View
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
                                                MeeshoChatMessageItem(
                                                    message = msg,
                                                    selectedLanguage = selectedLanguage,
                                                    onConfirmedNext = {
                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                        CreatorAcademyPrefs.addXpPoints(context, 50, "MEESHO_CREATOR")
                                                        val nextStep = currentStepIndex + 1
                                                        if (nextStep < MeeshoCreatorStaticData.guidedLessons.size) {
                                                            currentStepIndex = nextStep
                                                            CreatorAcademyPrefs.setMeeshoStepIndex(context, nextStep)
                                                            loadGuidedStep(nextStep)
                                                        } else {
                                                            isSessionCompleted = true
                                                        }
                                                    },
                                                    onExplainAgain = {
                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                        loadGuidedStep(currentStepIndex, isSimpler = true)
                                                    },
                                                    onOpenUrl = { url ->
                                                        try {
                                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                            context.startActivity(intent)
                                                        } catch (e: Exception) {
                                                            Toast.makeText(context, "Opening link...", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                )
                                            }

                                            if (isSessionCompleted) {
                                                item {
                                                    CourseCompletionCard(
                                                        courseTitle = "Meesho Creator E-Commerce Course",
                                                        skillsLearned = listOf(
                                                            "Product Selection & Margins",
                                                            "Reselling Reel Strategies",
                                                            "WhatsApp & Instagram Shop Marketing",
                                                            "Zero-Cost Order Fulfillment"
                                                        ),
                                                        onContinue = onDismiss,
                                                        onResetCourse = {
                                                            CreatorAcademyPrefs.setMeeshoStepIndex(context, 0)
                                                            currentStepIndex = 0
                                                            isSessionCompleted = false
                                                            loadGuidedStep(0)
                                                        },
                                                        theme = meeshoTheme
                                                    )
                                                }
                                            }

                                            if (isThinking) {
                                                item {
                                                    MeeshoThinkingBubble(text = thinkingText)
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Freeform Question Input Bar
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            value = customUserInput,
                                            onValueChange = { customUserInput = it },
                                            placeholder = {
                                                Text(
                                                    text = when (selectedLanguage) {
                                                        "Hindi" -> "मीशो क्रिएटर से जुड़ा कोई सवाल पूछें..."
                                                        "English" -> "Ask Meesho AI Mentor any question..."
                                                        else -> "Meesho Creator se juda koi question pucho..."
                                                    },
                                                    fontSize = 11.5.sp,
                                                    color = TextWhite.copy(alpha = 0.4f)
                                                )
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(48.dp),
                                            shape = RoundedCornerShape(24.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = EmeraldPrimary,
                                                unfocusedBorderColor = Color(0x33FFFFFF),
                                                focusedContainerColor = Color(0x1A000000),
                                                unfocusedContainerColor = Color(0x10000000),
                                                focusedTextColor = TextWhite,
                                                unfocusedTextColor = TextWhite
                                            ),
                                            singleLine = true
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(CircleShape)
                                                .background(EmeraldPrimary)
                                                .clickable {
                                                    sendCustomUserQuery(customUserInput)
                                                },
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

                                "DIRECT_LINKS" -> {
                                    // Official Direct Links Tab
                                    MeeshoDirectLinksView(
                                        onOpenUrl = { url ->
                                            try {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Opening link...", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    )
                                }

                                "CAPTION_GEN" -> {
                                    // AI Caption Generator Tab
                                    MeeshoCaptionGeneratorView(
                                        productName = captionProductName,
                                        onProductNameChange = { captionProductName = it },
                                        productCode = captionProductCode,
                                        onProductCodeChange = { captionProductCode = it },
                                        isGenerating = isGeneratingCaption,
                                        title = generatedCaptionTitle,
                                        caption = generatedCaptionText,
                                        hashtags = generatedHashtags,
                                        cta = generatedCta,
                                        selectedLanguage = selectedLanguage,
                                        onGenerate = { generateProductCaption() },
                                        onCopyAll = { text ->
                                            clipboardManager.setText(AnnotatedString(text))
                                            Toast.makeText(context, "Caption copied to clipboard! 📋", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }

                                "CHECKLIST" -> {
                                    // Final Setup Verification Checklist Tab
                                    MeeshoChecklistView(
                                        check1 = check1, onCheck1Change = { check1 = it },
                                        check2 = check2, onCheck2Change = { check2 = it },
                                        check3 = check3, onCheck3Change = { check3 = it },
                                        check4 = check4, onCheck4Change = { check4 = it },
                                        check5 = check5, onCheck5Change = { check5 = it },
                                        check6 = check6, onCheck6Change = { check6 = it },
                                        check7 = check7, onCheck7Change = { check7 = it },
                                        selectedLanguage = selectedLanguage,
                                        onFinishChecklist = {
                                            isSessionCompleted = true
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

/**
 * 1. WELCOME SCREEN VIEW
 */
@Composable
private fun MeeshoWelcomeScreenView(
    onStartClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary.copy(alpha = 0.18f))
                .border(BorderStroke(2.dp, EmeraldPrimary), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            com.example.ui.screens.OfficialLogo(name = "meesho", modifier = Modifier.size(44.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(EmeraldPrimary.copy(alpha = 0.18f))
                .border(BorderStroke(1.dp, EmeraldPrimary), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = "⚡ ZERO TO HERO LEARNING SYSTEM",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Black,
                color = EmeraldPrimary,
                letterSpacing = 0.8.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "🚀 One Day Meesho Creator Setup",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Today we'll help you complete your Meesho Creator journey from zero to your first earning opportunity.",
            fontSize = 12.5.sp,
            color = TextWhite.copy(alpha = 0.85f),
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Info Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x12FFFFFF))
                    .border(BorderStroke(0.8.dp, Color(0x22FFFFFF)), RoundedCornerShape(14.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "⏱️ Time", fontSize = 11.sp, color = TextWhite.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "30–60 Mins", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x12FFFFFF))
                    .border(BorderStroke(0.8.dp, Color(0x22FFFFFF)), RoundedCornerShape(14.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "💰 Cost", fontSize = 11.sp, color = TextWhite.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "100% Free", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Key Bullet Points
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x0CFFFFFF))
                .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(16.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "✓ Step-by-step account & affiliate registration guide",
                "✓ Learn how Meesho commissions & wallet payouts work",
                "✓ Built-in AI Caption, Hashtag & Call To Action generator",
                "✓ Personal AI friend mentor available 24/7 for questions"
            ).forEach { point ->
                Text(
                    text = point,
                    fontSize = 11.5.sp,
                    color = TextWhite.copy(alpha = 0.9f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Start Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(EmeraldPrimary, Color(0xFF00E676))
                    )
                )
                .clickable { onStartClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Start Learning 🚀",
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Black,
                color = AmoledBlack,
                letterSpacing = 0.5.sp
            )
        }
    }
}

/**
 * 2. MANDATORY LANGUAGE SELECTION VIEW
 */
@Composable
private fun MeeshoLanguageSelectionView(
    currentLanguage: String,
    onSelectLanguage: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🌐 Choose Your Preferred Language",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Select how you would like your AI mentor to speak throughout the session.",
            fontSize = 12.sp,
            color = TextWhite.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        listOf(
            Triple("Hindi", "🇮🇳 Hindi", "हिंदी में सीखें (Devanagari Hindi)"),
            Triple("English", "🇺🇸 English", "Learn in clear, plain English"),
            Triple("HinEnglish", "🌐 HinEnglish", "Hindi + English mix (Most popular)")
        ).forEach { (code, label, desc) ->
            val isSelected = currentLanguage == code
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isSelected) EmeraldPrimary.copy(alpha = 0.18f) else Color(0x12FFFFFF))
                    .border(
                        BorderStroke(if (isSelected) 1.5.dp else 0.8.dp, if (isSelected) EmeraldPrimary else Color(0x22FFFFFF)),
                        RoundedCornerShape(18.dp)
                    )
                    .clickable { onSelectLanguage(code) }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = label,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) EmeraldPrimary else TextWhite
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = desc,
                            fontSize = 11.5.sp,
                            color = TextWhite.copy(alpha = 0.7f)
                        )
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

/**
 * 3. ROADMAP SWIPE CARDS VIEW
 */
@Composable
private fun MeeshoRoadmapSwipeView(
    currentCardIndex: Int,
    selectedLanguage: String,
    onNextCard: () -> Unit,
    onPrevCard: () -> Unit,
    onSkipRoadmap: () -> Unit
) {
    val introCards = remember(selectedLanguage) {
        MeeshoCreatorStaticData.roadmapCards.map { card ->
            val title = when (selectedLanguage) {
                "Hindi" -> card.titleHindi
                "English" -> card.titleEnglish
                else -> card.titleHinglish
            }
            val subtitle = when (selectedLanguage) {
                "Hindi" -> card.subtitleHindi
                "English" -> card.subtitleEnglish
                else -> card.subtitleHinglish
            }
            val bullets = when (selectedLanguage) {
                "Hindi" -> card.bulletPointsHindi
                "English" -> card.bulletPointsEnglish
                else -> card.bulletPointsHinglish
            }

            ToolIntroCardData(
                title = title,
                subtitle = subtitle,
                icon = Icons.Default.ShoppingBag,
                highlightTag = "Meesho Creator Roadmap",
                bulletPoints = bullets
            )
        }
    }

    CommonToolIntroContainer(
        cards = introCards,
        onCompleteIntro = onSkipRoadmap
    )
}

/**
 * 4. CHAT MESSAGE ITEM COMPONENT
 */
@Composable
private fun MeeshoChatMessageItem(
    message: MeeshoChatMessage,
    selectedLanguage: String,
    onConfirmedNext: () -> Unit,
    onExplainAgain: () -> Unit,
    onOpenUrl: (String) -> Unit
) {
    val isUser = message.isFromUser

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        if (message.isLessonStep) {
            // Step Header Tag
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(EmeraldPrimary.copy(alpha = 0.18f))
                    .border(BorderStroke(1.dp, EmeraldPrimary), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "LESSON ${message.stepNumber}: ${message.stepTitle}",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Black,
                    color = EmeraldPrimary,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Message Bubble
        Surface(
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (isUser) 18.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 18.dp
            ),
            color = if (isUser) EmeraldPrimary else Color(0x18FFFFFF),
            border = if (!isUser) BorderStroke(0.8.dp, Color(0x22FFFFFF)) else null,
            modifier = Modifier.widthIn(max = 340.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = message.text,
                    fontSize = 12.5.sp,
                    color = if (isUser) AmoledBlack else TextWhite,
                    lineHeight = 17.5.sp
                )

                // Visual Screen Guidance Cue
                if (!message.visualCueText.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x12FFFFFF))
                            .border(BorderStroke(0.8.dp, EmeraldPrimary.copy(alpha = 0.5f)), RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Text(text = "👀", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = message.visualCueText,
                                fontSize = 11.sp,
                                color = EmeraldPrimary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                // Direct Action Link Button
                if (!message.directActionUrl.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(EmeraldPrimary)
                            .clickable { onOpenUrl(message.directActionUrl) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = "Link",
                                tint = AmoledBlack,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = message.directActionLabel ?: "Open Meesho Direct Link",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmoledBlack
                            )
                        }
                    }
                }

                // Practical Task Mission
                if (!message.practicalTaskText.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = message.practicalTaskText,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFFB300)
                    )
                }
            }
        }

        // Interactive Confirmation Prompts
        if (!isUser && message.showConfirmationPrompt) {
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x0CFFFFFF))
                    .border(BorderStroke(0.8.dp, Color(0x1AFFFFFF)), RoundedCornerShape(16.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = when (selectedLanguage) {
                        "Hindi" -> "क्या आपने यह स्टेप पूरा कर लिया?"
                        "English" -> "Did you complete this step on your screen?"
                        else -> "Kya aapne ye step complete kar liya?"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1.2f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(EmeraldPrimary)
                            .clickable { onConfirmedNext() }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✅ Yes, Done! 👍",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmoledBlack
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x22FFFFFF))
                            .border(BorderStroke(0.8.dp, Color(0x33FFFFFF)), RoundedCornerShape(12.dp))
                            .clickable { onExplainAgain() }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🔄 Explain Again",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite
                        )
                    }
                }
            }
        }
    }
}

/**
 * DIRECT LINKS TAB VIEW
 */
@Composable
private fun MeeshoDirectLinksView(
    onOpenUrl: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "🔗 Official Meesho Creator Links",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary
        )

        Text(
            text = "Tap any link below to directly open the official Play Store app or Meesho Creator portal.",
            fontSize = 11.5.sp,
            color = TextWhite.copy(alpha = 0.7f),
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        listOf(
            Triple("📱 Meesho Android App (Play Store)", "market://details?id=com.meesho.supply", "100M+ Downloads • Official App"),
            Triple("🎯 Meesho Official Creator Portal", "https://www.meesho.com", "Apply for Creator Affiliate Program"),
            Triple("📦 Meesho Seller & Supplier Hub", "https://supplier.meesho.com", "Official Supplier & Seller Network"),
            Triple("🔒 Meesho Privacy & Security", "https://www.meesho.com/legal/privacy", "Verified Security Terms")
        ).forEach { (title, url, desc) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x12FFFFFF))
                    .border(BorderStroke(0.8.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                    .clickable { onOpenUrl(url) }
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = desc, fontSize = 10.5.sp, color = TextWhite.copy(alpha = 0.6f))
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(EmeraldPrimary)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(text = "Open ↗", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                    }
                }
            }
        }
    }
}

/**
 * AI CAPTION GENERATOR TAB VIEW
 */
@Composable
private fun MeeshoCaptionGeneratorView(
    productName: String,
    onProductNameChange: (String) -> Unit,
    productCode: String,
    onProductCodeChange: (String) -> Unit,
    isGenerating: Boolean,
    title: String,
    caption: String,
    hashtags: String,
    cta: String,
    selectedLanguage: String,
    onGenerate: () -> Unit,
    onCopyAll: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "✍️ Meesho AI Caption & Hashtag Generator",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary
        )

        Text(
            text = "Generate viral captions, hashtags, and high-converting CTAs tailored for Meesho review Reels.",
            fontSize = 11.5.sp,
            color = TextWhite.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Product Name Input
        Text(text = "Product Name / Category", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        OutlinedTextField(
            value = productName,
            onValueChange = onProductNameChange,
            placeholder = { Text("e.g. Printed Cotton Kurti, Wireless Earbuds", fontSize = 11.sp, color = TextWhite.copy(alpha = 0.4f)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = Color(0x33FFFFFF),
                focusedContainerColor = Color(0x10000000),
                unfocusedContainerColor = Color(0x10000000),
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            ),
            singleLine = true
        )

        // Product Code Input
        Text(text = "Meesho Product Code (Optional)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        OutlinedTextField(
            value = productCode,
            onValueChange = onProductCodeChange,
            placeholder = { Text("e.g. 123456", fontSize = 11.sp, color = TextWhite.copy(alpha = 0.4f)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = Color(0x33FFFFFF),
                focusedContainerColor = Color(0x10000000),
                unfocusedContainerColor = Color(0x10000000),
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Generate Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(EmeraldPrimary)
                .clickable(enabled = !isGenerating) { onGenerate() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isGenerating) "🧠 Generating AI Copy..." else "✨ Generate Viral Caption",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = AmoledBlack
            )
        }

        if (caption.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))

            val fullText = "$title\n\n$caption\n\n$cta\n\n$hashtags"

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x14FFFFFF))
                    .border(BorderStroke(1.dp, EmeraldPrimary), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🎉 Generated Caption", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(EmeraldPrimary)
                                .clickable { onCopyAll(fullText) }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(text = "Copy All 📋", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = caption, fontSize = 11.5.sp, color = TextWhite.copy(alpha = 0.9f), lineHeight = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = cta, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFB300))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = hashtags, fontSize = 10.5.sp, color = EmeraldPrimary)
                }
            }
        }
    }
}

/**
 * CHECKLIST TAB VIEW
 */
@Composable
private fun MeeshoChecklistView(
    check1: Boolean, onCheck1Change: (Boolean) -> Unit,
    check2: Boolean, onCheck2Change: (Boolean) -> Unit,
    check3: Boolean, onCheck3Change: (Boolean) -> Unit,
    check4: Boolean, onCheck4Change: (Boolean) -> Unit,
    check5: Boolean, onCheck5Change: (Boolean) -> Unit,
    check6: Boolean, onCheck6Change: (Boolean) -> Unit,
    check7: Boolean, onCheck7Change: (Boolean) -> Unit,
    selectedLanguage: String,
    onFinishChecklist: () -> Unit
) {
    val items = listOf(
        Pair("✅ Meesho App Installed from Play Store", check1 to onCheck1Change),
        Pair("✅ Meesho Phone Number Account Verified", check2 to onCheck2Change),
        Pair("✅ Creator Program Application Submitted", check3 to onCheck3Change),
        Pair("✅ Instagram Profile & Category Linked", check4 to onCheck4Change),
        Pair("✅ Bank Details / UPI Wallet Linked", check5 to onCheck5Change),
        Pair("✅ First Meesho Product Selected", check6 to onCheck6Change),
        Pair("✅ First Review Video Content Ready", check7 to onCheck7Change)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "📋 Final Meesho Creator Verification Checklist",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary
        )

        Text(
            text = "Verify that all 7 steps are checked before completing your session.",
            fontSize = 11.5.sp,
            color = TextWhite.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        items.forEach { (label, statePair) ->
            val (checked, onCheckedChange) = statePair
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x12FFFFFF))
                    .border(BorderStroke(0.8.dp, if (checked) EmeraldPrimary else Color(0x22FFFFFF)), RoundedCornerShape(14.dp))
                    .clickable { onCheckedChange(!checked) }
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = onCheckedChange,
                        colors = CheckboxDefaults.colors(
                            checkedColor = EmeraldPrimary,
                            uncheckedColor = TextWhite.copy(alpha = 0.4f),
                            checkmarkColor = AmoledBlack
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = label,
                        fontSize = 11.5.sp,
                        fontWeight = if (checked) FontWeight.Bold else FontWeight.Normal,
                        color = if (checked) TextWhite else TextWhite.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(EmeraldPrimary)
                .clickable { onFinishChecklist() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Complete Session & Celebrate 🎉",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Black,
                color = AmoledBlack
            )
        }
    }
}

/**
 * SESSION COMPLETION & CELEBRATION VIEW
 */
@Composable
private fun MeeshoSessionCompletionView(
    selectedLanguage: String,
    onNavigateToBrandCollab: () -> Unit,
    onRestart: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🎉 FREEDOM!", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Congratulations! You are now a Meesho Creator!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "You have completed all 8 lessons and set up your complete Meesho affiliate creator journey.",
            fontSize = 12.sp,
            color = TextWhite.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "🚀 NEXT RECOMMENDED LEARNING PATHS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary,
            letterSpacing = 0.8.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Next Tool 1: Brand Collaboration AI
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x18FFFFFF))
                .border(BorderStroke(1.dp, EmeraldPrimary), RoundedCornerShape(16.dp))
                .clickable { onNavigateToBrandCollab() }
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "💼", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "Brand Collaboration AI", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(text = "Get paid brand sponsorships & pitches", fontSize = 10.5.sp, color = EmeraldPrimary)
                    }
                }
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Open", tint = EmeraldPrimary)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Next Tool 2: Instagram Growth AI
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x12FFFFFF))
                .border(BorderStroke(0.8.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                .clickable { onClose() }
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📈", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "Instagram Growth AI", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(text = "Bio audit, algorithm hacks & timing", fontSize = 10.5.sp, color = TextWhite.copy(alpha = 0.6f))
                    }
                }
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Open", tint = TextWhite.copy(alpha = 0.6f))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "🔄 Restart Course",
                fontSize = 11.5.sp,
                color = TextWhite.copy(alpha = 0.6f),
                modifier = Modifier.clickable { onRestart() }
            )

            Text(
                text = "Close Mentor ✕",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary,
                modifier = Modifier.clickable { onClose() }
            )
        }
    }
}

// Helpers
@Composable
private fun MeeshoTabPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) EmeraldPrimary else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 10.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) AmoledBlack else TextWhite.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun MeeshoThinkingBubble(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "AI",
                tint = EmeraldPrimary,
                modifier = Modifier.size(14.dp)
            )
        }
        Text(
            text = text,
            fontSize = 11.sp,
            color = EmeraldPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

// Dynamic Gemini Backend Call Helper
private suspend fun fetchMeeshoStepExplanation(
    stepIndex: Int,
    stepTitle: String,
    baseContent: String,
    selectedLanguage: String,
    isSimpler: Boolean
): String = withContext(Dispatchers.IO) {
    val apiKey = try {
        val key = BuildConfig.GEMINI_API_KEY
        if (!key.isNullOrBlank() && key != "BUILDCONFIG_MISSING" && key != "null") key else System.getenv("GEMINI_API_KEY") ?: ""
    } catch (_: Exception) { "" }

    if (apiKey.isNotBlank()) {
        try {
            val systemContext = when (selectedLanguage) {
                "Hindi" -> "You are Meesho Creator AI, a close friend creator mentor. Teach in friendly Devanagari Hindi using warm emojis 😄."
                "English" -> "You are Meesho Creator AI, a close friend creator mentor. Teach in friendly clear English using warm emojis 😄."
                else -> "You are Meesho Creator AI, a close friend creator mentor. Teach in friendly natural Hinglish (Hindi + English) using warm emojis 😄."
            }

            val prompt = if (isSimpler) {
                "$systemContext\nExplain '$stepTitle' in extremely simple terms with visual screen button guidance. Keep it friendly under 4 sentences."
            } else {
                "$systemContext\nExplain '$stepTitle' to a beginner creator. Base info: $baseContent. Keep it friendly, clear, and actionable under 4 sentences."
            }

            val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
            val url = URL(endpoint)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.connectTimeout = 5000
            conn.readTimeout = 5000

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        })
                    })
                })
            }

            conn.outputStream.use { os ->
                os.write(jsonBody.toString().toByteArray(Charsets.UTF_8))
            }

            if (conn.responseCode == 200) {
                val resp = conn.inputStream.bufferedReader().use { it.readText() }
                val root = JSONObject(resp)
                val candidates = root.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val text = candidates.getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                    if (text.isNotBlank()) return@withContext text.trim()
                }
            }
        } catch (_: Exception) {}
    }

    // Fallback if API unavailable
    return@withContext baseContent
}

private suspend fun generateMeeshoAiResponse(
    query: String,
    selectedLanguage: String
): String = withContext(Dispatchers.IO) {
    ViralAiMentorEngine.generateIntegratedMentorResponse(
        domain = MentorToolDomain.MEESHO_CREATOR_AI,
        userQuery = query,
        userContext = "Meesho Creator Hub - Affiliate & Reselling Mentor",
        language = selectedLanguage
    )
}

private fun generateMeeshoCaptionAi(
    productName: String,
    productCode: String,
    language: String
): Quadruple<String, String, String, String> {
    val codeStr = if (productCode.isNotBlank()) "Code: #$productCode" else "Code in bio link"

    val title = when (language) {
        "Hindi" -> "🔥 मीशो का यह शानदार $productName बिल्कुल मिस मत करें!"
        "English" -> "🔥 Unbelievable Meesho Find: $productName review!"
        else -> "🔥 Affordable Meesho Find: $productName Review!"
    }

    val caption = when (language) {
        "Hindi" -> "दोस्तों! आज मैं आपके लिए लाया/लाई हूँ मीशो का सबसे बेहतरीन $productName! इसकी क्वालिटी कमाल की है और प्राइस बहुत ही कम है। अनबॉक्सिंग वीडियो देखें और रेटिंग चेक करें।"
        "English" -> "Guys! Check out this budget-friendly $productName from Meesho! The quality exceeded my expectations for the price. Watch the full unboxing & close-up detail."
        else -> "Guys! Meesho ka ye budget-friendly $productName dekho! Quality ekdam solid hai and pricing super affordable. Unboxing and fabric quality check karo!"
    }

    val cta = "👉 Comment 'LINK' or '$codeStr' for instant buying link in DM! 📥"

    val hashtags = "#meesho #meeshofinds #meeshohaul #affiliate #budgetfashion #shoppingreview #viralreels #reelsindia"

    return Quadruple(title, caption, hashtags, cta)
}

data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)
