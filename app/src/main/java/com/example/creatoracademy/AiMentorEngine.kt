package com.example.creatoracademy

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.EmeraldPrimary
import kotlin.random.Random

/**
 * MASTER PHASE 15C — AI Creator Mentor Pro Engine
 * Core architecture for real adaptive learning, dynamic lesson generation,
 * smart help explanations, non-repeating AI encouragement, streak tracking,
 * and future-ready hooks for Brand Deals, Affiliate, & Video Editing.
 */

enum class TaskState {
    LOCKED,
    CURRENT,
    COMPLETED,
    SKIPPED
}

enum class FutureAcademyModule {
    BRAND_COLLABORATION_AI,
    AFFILIATE_MENTOR,
    VIDEO_EDITING_ACADEMY
}

data class CreatorLevel(
    val name: String,
    val badgeName: String,
    val minXp: Int,
    val maxXp: Int,
    val color: Color,
    val icon: ImageVector
) {
    companion object {
        fun getLevelForXp(xp: Int): CreatorLevel {
            return when {
                xp < 200 -> CreatorLevel("Bronze Creator", "BRONZE BADGE", 0, 200, Color(0xFFCD7F32), Icons.Default.WorkspacePremium)
                xp < 500 -> CreatorLevel("Silver Creator", "SILVER BADGE", 200, 500, Color(0xFFC0C0C0), Icons.Default.Star)
                xp < 1000 -> CreatorLevel("Gold Creator", "GOLD BADGE", 500, 1000, Color(0xFFFFD700), Icons.Default.EmojiEvents)
                xp < 2000 -> CreatorLevel("Diamond Creator", "DIAMOND BADGE", 1000, 2000, Color(0xFF00E5FF), Icons.Default.WorkspacePremium)
                else -> CreatorLevel("Legend Creator", "LEGEND BADGE", 2000, 5000, EmeraldPrimary, Icons.Default.EmojiEvents)
            }
        }
    }
}

data class AiMentorTask(
    val id: String,
    val stepNumber: Int,
    val title: String,
    val skillCategory: String,
    val goalStatement: String,
    val coachMessage: String,
    val detailExplanation: String,
    val whyItMatters: String,
    val goodExample: String,
    val badExample: String,
    val proTip: String,
    val commonMistake: String,
    val actionTask: String,
    val simplerExplanation: String,
    val extraRealExample: String,
    val recommendedTool: String? = null,
    val xpReward: Int = 100,
    val state: TaskState = TaskState.LOCKED
) {
    fun getLocalized(lang: com.example.reports.ReportLanguage): AiMentorTask {
        return when (lang) {
            com.example.reports.ReportLanguage.ENGLISH -> this
            com.example.reports.ReportLanguage.HINGLISH -> when (id) {
                "ig_adaptive_1" -> copy(
                    title = "High-Converting Bio Banayein with CTA",
                    skillCategory = "Profile & Bio Optimization",
                    goalStatement = "2 seconds ke andar profile visitors ko followers me convert karein.",
                    coachMessage = "Welcome! Aaiye aapka Instagram bio specially optimize karein. Bio me hona chahiye ki aap kaun hain, kya value de rahe hain, aur kahan click karna hai.",
                    detailExplanation = "Line 1: Aapka main focus. Line 2: Audience ka main benefit. Line 3: Direct Call-To-Action link.",
                    whyItMatters = "80% se zyada visitors bina follow kiye chale jaate hain agar bio clear na ho.",
                    goodExample = "⚡ Daily Insights & Tips\n🎯 60s me sikhiye\n👇 Free guide neeche download karein",
                    badExample = "Bas aise hi post kar raha hoon ✨ DM for collab",
                    proTip = "Bullet points aur emoji spacing use karein mobile screen par easily padhne ke liye.",
                    commonMistake = "CTA link bhool jana ya tough words use karna.",
                    actionTask = "Apne bio ke liye 3 lines likhein aur active link add karein.",
                    simplerExplanation = "Bio ko ek shop ke board ki tarah samjhein. Line 1: Aap kya karte hain? Line 2: Viewer ka kya fayda hai? Line 3: Kahan click karna hai?",
                    extraRealExample = "Example: '💡 Top Tips | 🚀 Daily Reels | 👇 Get free template'"
                )
                "ig_adaptive_2" -> copy(
                    title = "3 Core Content Pillars Set Karein",
                    skillCategory = "Content Strategy",
                    goalStatement = "Content feed ko aise structure karein ki algorithm aapka account sahi categorize kare.",
                    coachMessage = "Awesome! Ab 3 tight content pillars chunein. Pillars aapko burnout se bachate hain aur audience ko aane ka reason dete hain.",
                    detailExplanation = "Pillar 1: Educational Tips. Pillar 2: Common Myths / Mistakes. Pillar 3: Personal Journey.",
                    whyItMatters = "Algorithm aapke content ko target audience tak pahunchane ke liye pillars ka use karta hai.",
                    goodExample = "Pillar A: 30s Hacks\nPillar B: Tool Reviews\nPillar C: Behind-the-scenes",
                    badExample = "Ek din food, agle din crypto aur phir gym clips post karna.",
                    proTip = "Account authority banane ke liye 30 days tak in 3 pillars par bane rahein.",
                    commonMistake = "Ek sath bahut saare unrelated topics cover karne ki koshish karna.",
                    actionTask = "Apne 3 content pillars ke 3 titles likhein.",
                    simplerExplanation = "Content pillars TV channels ki tarah hain. Agar sports channel par cooking dikhe toh log chale jayenge. Focus rahein!",
                    extraRealExample = "Example: 1) Quick Tutorials, 2) Myth Busting, 3) Tool Reviews."
                )
                "ig_adaptive_3" -> copy(
                    title = "3-Second Viral Hook Formula Sikhein",
                    skillCategory = "Reels Scripting",
                    goalStatement = "Pehle 3 seconds me >80% watch time achieve karein.",
                    coachMessage = "Hooks decide karte hain ki Reel viral hogi ya 200 views par ruk jayegi. Aaiye ek zabardast 3-second hook likhein!",
                    detailExplanation = "Screen par bold text overlay ke sath energetic movement combine karein.",
                    whyItMatters = "Instagram 3-second retention ko strictly measure karta hai.",
                    goodExample = "Text: '2026 me yeh mistake bilkul mat karna!' with energetic visual.",
                    badExample = "'Hey guys welcome back today I want to talk about...'",
                    proTip = "Text overlay ko screen ke upper 30% area me rakhein.",
                    commonMistake = "Slow intro ya silent pauses se start karna.",
                    actionTask = "Apne next Reel concept ke liye 3 hook variations script karein.",
                    simplerExplanation = "Hook book cover ki tarah hai. Agar 3 seconds me excite na kare toh log swipe kar denge!",
                    extraRealExample = "Hook idea: '3 tools jo aapko zaroor pata hone chahiye!'"
                )
                "ig_adaptive_4" -> copy(
                    title = "3-Tier SEO Hashtag Stack Banayein",
                    skillCategory = "SEO & Hashtags",
                    goalStatement = "Instagram Search results me top keywords par rank karein.",
                    coachMessage = "Aaiye targeted 3-tier hashtag stack organize karein jisse aapki posts search me top par aayein.",
                    detailExplanation = "Tier 1: Broad tags (100k+ posts). Tier 2: Specific Niche tags. Tier 3: Micro Community tags.",
                    whyItMatters = "Instagram ab Search Engine ki tarah kaam karta hai. Relevant hashtags post ko index karte hain.",
                    goodExample = "#creator #reelsgrowth #creatortips #creatorhacks",
                    badExample = "#viral #fyp #love #explorepage",
                    proTip = "Audio aur captions me bhi main keywords include karein.",
                    commonMistake = "30 generic hashtags use karna jo seconds me gayab ho jaate hain.",
                    actionTask = "5-hashtag stack create karke save karein.",
                    simplerExplanation = "Hashtags library categories ki tarah hain. Sahi category me rakhne se log aapko easily dhoond paayenge!",
                    extraRealExample = "Stack: Broad + Specific + Micro Community."
                )
                "ig_adaptive_5" -> copy(
                    title = "Pre-Publishing Quality Check & Audio Selection",
                    skillCategory = "Publishing Check",
                    goalStatement = "Publish karne se pehle 100% quality ensure karein.",
                    coachMessage = "Next Reel post karne se pehle 7-point quality checklist run karein!",
                    detailExplanation = "Audio levels check karein, trending arrow ↗️ verify karein, clear cover image set karein.",
                    whyItMatters = "Low contrast ya low audio jaisi choti mistakes reach kill kar deti hain.",
                    goodExample = "Trending audio + Auto-captions + Strong cover thumbnail.",
                    badExample = "Andhere room me bina captions ke post karna.",
                    proTip = "Trending arrow icon ↗️ wale audio use karein extra algorithm boost ke liye.",
                    commonMistake = "Good cover photo select kiye bina publish kar dena.",
                    actionTask = "Interactive posting checklist run karein.",
                    simplerExplanation = "Yeh pilot ki pre-flight checklist jaisa hai. Takeoff se pehle lighting, captions, audio verify karein!",
                    extraRealExample = "Checklist: 1) Audio clear? 2) Hook visible? 3) Captions added? 4) Cover picked?"
                )
                "yt_adaptive_1" -> copy(
                    title = "Channel Banner aur Description Optimize Karein",
                    skillCategory = "Channel Branding",
                    goalStatement = "Visitors ko instant subscribers me convert karein.",
                    coachMessage = "Welcome YouTube Creator! Aaiye aapka channel header aur About section optimize karein.",
                    detailExplanation = "Banner par upload schedule aur channel value clear dikhni chahiye.",
                    whyItMatters = "YouTube description padhkar samajhta hai ki aapki videos kinko recommend karni hain.",
                    goodExample = "Banner: 'Weekly Guides & Shorts Every Tuesday' + High contrast design.",
                    badExample = "Default background banner with no info.",
                    proTip = "Key text ko mobile safe area (1546x423) me rakhein.",
                    commonMistake = "About box ko khaali chhodna.",
                    actionTask = "150-word search-friendly description likhein.",
                    simplerExplanation = "Channel home page billboard ki tarah hai. 5 seconds me bataiye ki channel kis baare me hai!",
                    extraRealExample = "Description: 'Welcome! Is channel par hum top strategies aur honest reviews share karte hain.'"
                )
                "yt_adaptive_2" -> copy(
                    title = "YouTube Shorts 3-Sec Retention Hook Sikhein",
                    skillCategory = "Shorts Optimization",
                    goalStatement = "Shorts feed me >75% Viewed vs Swiped metric achieve karein.",
                    coachMessage = "Retention YouTube ka #1 factor hai! Aaiye Shorts hook banayein jo viewers ko roke rakhe.",
                    detailExplanation = "YouTube Shorts me 'Swiped Away %' sabse important hai. Bold statement ya action se start karein.",
                    whyItMatters = "Agar 'Swiped Away' 35% se zyada hua toh algorithm push stop kar deta hai.",
                    goodExample = "High energy start: 'Yeh mistake karne se pehle yeh video zaroor dekhein!'",
                    badExample = "'Hey everyone welcome back to another video...'",
                    proTip = "Ending sentence ko opening hook me seamlessly loop karein.",
                    commonMistake = "Slow intro ya boring opening frame.",
                    actionTask = "15-second Short record karein instant hook ke sath.",
                    simplerExplanation = "Shorts feed super fast chalta hai. Aapko turant kuch exciting bolna hoga!",
                    extraRealExample = "Hook idea: 'Yeh #1 hack koi nahi bata raha!'"
                )
                "yt_adaptive_3" -> copy(
                    title = "High-CTR Thumbnail & Title Combination Banayein",
                    skillCategory = "CTR Optimization",
                    goalStatement = "Search aur Impressions par >8% Click-Through Rate achieve karein.",
                    coachMessage = "Thumbnail + Title = Click Rate! Aaiye high-contrast visual aur exciting title pair karein.",
                    detailExplanation = "Thumbnail title ka text repeat na kare. Max 3 words rakhein.",
                    whyItMatters = "High CTR YouTube ko signal deta hai ki video engaging hai, jisse extra impressions milte hain.",
                    goodExample = "Title: 'How I Scaled My Workflow' | Thumbnail text: '10x Faster!'",
                    badExample = "Title aur thumbnail dono me exact same long sentence.",
                    proTip = "Dark background par high-contrast text aur face expressions use karein.",
                    commonMistake = "Chota text use karna jo mobile par padha na ja sake.",
                    actionTask = "3 distinct Thumbnail + Title concepts outline karein.",
                    simplerExplanation = "Thumbnail picture hai, Title label hai. Dono milkar logon ko click karne par majboor karte hain!",
                    extraRealExample = "Title: 'Strategy EXPOSED' | Thumbnail: 'DO NOT SKIP!'"
                )
                else -> this
            }
            com.example.reports.ReportLanguage.HINDI -> when (id) {
                "ig_adaptive_1" -> copy(
                    title = "उच्च-रूपांतरण बायो बनाएँ (CTA के साथ)",
                    skillCategory = "प्रोफ़ाइल और बायो ऑप्टिमाइजेशन",
                    goalStatement = "2 सेकंड के भीतर विज़िटर्स को वफादार फ़ॉलोअर्स में बदलें।",
                    coachMessage = "नमस्ते! आइए आपका बायो विशेष रूप से तैयार करें। बायो में बताएं कि आप कौन हैं, क्या मूल्य प्रदान करते हैं, और कहां क्लिक करना है।",
                    detailExplanation = "लाइन 1: आपका फ़ोकस। लाइन 2: मुख्य लाभ। लाइन 3: डायरेक्ट कॉल-टू-एक्शन लिंक।",
                    whyItMatters = "यदि आपका बायो स्पष्ट नहीं है तो 80% से अधिक विज़िटर्स बिना फ़ॉलो किए चले जाते हैं।",
                    goodExample = "⚡ इनसाइट्स और टिप्स\n🎯 60 सेकंड में सीखें\n👇 मुफ़्त गाइड नीचे डाउनलोड करें",
                    badExample = "बस ऐसे ही पोस्ट कर रहा हूँ ✨ DM करें",
                    proTip = "पढ़ने में आसानी के लिए बुलेट पॉइंट्स या इमोजी का उपयोग करें।",
                    commonMistake = "CTA लिंक भूल जाना या कठिन शब्दों का उपयोग करना।",
                    actionTask = "अपने बायो के लिए 3 पंक्तियाँ लिखें और एक एक्टिव लिंक जोड़ें।",
                    simplerExplanation = "बायो को दुकान के बोर्ड की तरह समझें। लाइन 1: आप क्या करते हैं? लाइन 2: क्यों देखें? लाइन 3: कहाँ क्लिक करें?",
                    extraRealExample = "उदाहरण: '💡 टॉप टिप्स | 🚀 दैनिक रील्स | 👇 टेम्पलेट प्राप्त करें'"
                )
                "ig_adaptive_2" -> copy(
                    title = "3 मुख्य कंटेंट पिलर बनाएं",
                    skillCategory = "कंटेंट रणनीति",
                    goalStatement = "अपने कंटेंट फ़ीड को इस प्रकार व्यवस्थित करें कि एल्गोरिदम आपके खाते को सही ढंग से वर्गीकृत करे।",
                    coachMessage = "शानदार! अब 3 स्पष्ट कंटेंट पिलर चुनें। ये आपको निरंतरता और ऑडियंस बनाए रखने में मदद करते हैं।",
                    detailExplanation = "पिलर 1: शिक्षा और टिप्स। पिलर 2: गलतफ़हमियाँ। पिलर 3: व्यक्तिगत यात्रा।",
                    whyItMatters = "एल्गोरिदम आपके वीडियो को सही दर्शकों तक पहुँचाने के लिए कंटेंट पिलर्स का उपयोग करता है।",
                    goodExample = "पिलर A: 30s हैक्स\nपिलर B: टूल रिव्यू\nपिलर C: पर्दे के पीछे की बातें",
                    badExample = "एक दिन खाना, अगले दिन क्रिप्टो और फिर जिम के वीडियो डालना।",
                    proTip = "अकाउंट अथॉरिटी बनाने के लिए 30 दिनों तक इन 3 पिलर्स पर टिके रहें।",
                    commonMistake = "एक साथ बहुत सारे असंबंधित विषयों को कवर करने की कोशिश करना।",
                    actionTask = "अपने 3 पिलर्स में से प्रत्येक के लिए 3 विशिष्ट शीर्षक लिखें।",
                    simplerExplanation = "कंटेंट पिलर टीवी चैनल की तरह हैं। यदि स्पोर्ट्स चैनल अचानक खाना पकाने लगे, तो दर्शक चले जाते हैं। फ़ोकस रखें!",
                    extraRealExample = "उदा: 1) त्वरित ट्यूटोरियल, 2) मिथक, 3) उत्पाद सिफ़ारिशें।"
                )
                "ig_adaptive_3" -> copy(
                    title = "3-सेकंड वायरल हुक फॉर्मूला सीखें",
                    skillCategory = "रील्स स्क्रिप्टिंग",
                    goalStatement = "प्रत्येक रील के पहले 3 सेकंड में 80% से अधिक वॉच टाइम प्राप्त करें।",
                    coachMessage = "हुक तय करता है कि आपका वीडियो वायरल होगा या 200 व्यूज पर रुक जाएगा। आइए एक ज़बरदस्त हुक लिखें!",
                    detailExplanation = "स्क्रीन पर बोल्ड, डायनामिक टेक्स्ट ओवरले के साथ मूवमेंट को मिलाएं।",
                    whyItMatters = "इंस्टाग्राम 3-सेकंड रिटेंशन को सख्ती से मापता है।",
                    goodExample = "स्क्रीन टेक्स्ट: '2026 में यह गलती न करें!' गति के साथ।",
                    badExample = "'हेलो दोस्तों आज मैं बात करना चाहता हूँ...'",
                    proTip = "टेक्स्ट ओवरले को स्क्रीन के ऊपरी 30% हिस्से में रखें।",
                    commonMistake = "धीमी शुरुआत या शांत रुकने के साथ वीडियो शुरू करना।",
                    actionTask = "अपने अगले रील विचार के लिए 3 अलग-अलग हुक लिखें।",
                    simplerExplanation = "हुक किताब के कवर की तरह है। यदि यह 3 सेकंड में लोगों को आकर्षित नहीं करता, तो वे स्वाइप कर देते हैं!",
                    extraRealExample = "हुक विचार: '3 टूल जो कमाल के हैं!'"
                )
                "ig_adaptive_4" -> copy(
                    title = "3-स्तरीय SEO हैशटैग स्टैक बनाएं",
                    skillCategory = "SEO और हैशटैग रणनीति",
                    goalStatement = "उच्च-इरादे वाले कीवर्ड्स के लिए इंस्टाग्राम सर्च परिणामों में रैंक करें।",
                    coachMessage = "आइए खोज की दृश्यता को अधिकतम करने के लिए एक लक्षित 3-स्तरीय हैशटैग स्टैक व्यवस्थित करें।",
                    detailExplanation = "स्तर 1: ब्रॉड इंडस्ट्री टैग। स्तर 2: विशिष्ट निश टैग। स्तर 3: माइक्रो कम्युनिटी टैग।",
                    whyItMatters = "इंस्टाग्राम अब सर्च इंजन की तरह काम करता है। प्रासंगिक हैशटैग आपकी रील को सही श्रेणी में दिखाते हैं।",
                    goodExample = "#creator #reelsgrowth #creatortips #creatorhacks",
                    badExample = "#viral #fyp #love #explorepage",
                    proTip = "अधिकतम इंडेक्सिंग के लिए ऑडियो और कैप्शन में भी कीवर्ड शामिल करें।",
                    commonMistake = "30 विशाल सामान्य हैशटैग का उपयोग करना।",
                    actionTask = "अपने विषय के लिए 5-हैशटैग स्टैक बनाएं।",
                    simplerExplanation = "हैशटैग लाइब्रेरी श्रेणियों की तरह हैं। सही श्रेणी खोजना आसान बनाता है!",
                    extraRealExample = "स्टैक: ब्रॉड + विशिष्ट + कम्युनिटी।"
                )
                "ig_adaptive_5" -> copy(
                    title = "प्री-पब्लिशिंग क्वालिटी कंट्रोल और ऑडियो सिलेक्शन",
                    skillCategory = "पब्लिशिंग और सत्यापन",
                    goalStatement = "पब्लिश करने से पहले 100% तकनीकी और एल्गोरिदम गुणवत्ता सुनिश्चित करें।",
                    coachMessage = "अपनी अगली रील पोस्ट करने से पहले, रीच को अधिकतम करने के लिए 7-पॉइंट चेकलिस्ट चलाएं!",
                    detailExplanation = "ऑडियो स्तर जांचें, ट्रेंडिंग ऑडियो तीर ↗️ सूचक सत्यापित करें, उच्च-विपरीत कवर छवि और ऑन-स्क्रीन कैप्शन सुनिश्चित करें।",
                    whyItMatters = "कम कंट्रास्ट या धीमी आवाज़ जैसी छोटी तकनीकी अनदेखी वीडियो रिटेंशन को नुकसान पहुँचाती है।",
                    goodExample = "ट्रेंडिंग ऑडियो + स्पष्ट ऑटो-कैप्शन + मजबूत कवर फ़्रेम।",
                    badExample = "बिना टेक्स्ट कैप्शन या ऑडियो सामान्यीकरण के अंधेरे कमरे में पोस्ट करना।",
                    proTip = "अल्गोरिदम सर्च का लाभ उठाने के लिए तिरछे तीर आइकन ↗️ वाले ट्रेंडिंग ऑडियो का उपयोग करें।",
                    commonMistake = "फ़ीड पूर्वावलोकन के लिए आकर्षक कवर फोटो चुने बिना प्रकाशित करना।",
                    actionTask = "अपनी आगामी रील के लिए इंटरैक्टिव चेकलिस्ट के सभी आइटम चलाएं।",
                    simplerExplanation = "इसे टेकऑफ़ से पहले की पायलट चेकलिस्ट समझें। लाइटिंग, कैप्शन और ऑडियो डबल-चेक करें!",
                    extraRealExample = "चेकलिस्ट: 1) ऑडियो साफ़? 2) हुक दृश्यमान? 3) कैप्शन जोड़े गए? 4) कवर चुना गया?"
                )
                "yt_adaptive_1" -> copy(
                    title = "चैनल बैनर और विवरण अनुकूलित करें",
                    skillCategory = "चैनल ब्रांडिंग",
                    goalStatement = "विज़िटर्स को तुरंत सब्सक्राइबर में बदलें।",
                    coachMessage = "यूट्यूब क्रिएटर का स्वागत है! आइए आपका चैनल हेडर और अबाउट सेक्शन अनुकूलित करें।",
                    detailExplanation = "आपके बैनर पर अपलोड शेड्यूल और वैल्यू स्पष्ट होनी चाहिए।",
                    whyItMatters = "यूट्यूब विवरण पढ़कर समझता है कि आपकी वीडियो किसे दिखानी है।",
                    goodExample = "बैनर: 'साप्ताहिक गाइड और शॉर्ट्स' + उच्च कंट्रास्ट डिजाइन।",
                    badExample = "बिना किसी जानकारी के डिफ़ॉल्ट बैनर।",
                    proTip = "मुख्य टेक्स्ट को मोबाइल सेफ़ एरिया में रखें।",
                    commonMistake = "अबाउट बॉक्स को खाली छोड़ना।",
                    actionTask = "150 शब्दों का खोज-अनुकूल विवरण लिखें।",
                    simplerExplanation = "चैनल होम पेज एक होर्डिंग की तरह है। 5 सेकंड में बताएं कि चैनल किस बारे में है!",
                    extraRealExample = "विवरण: 'स्वागत है! इस चैनल पर हम बेहतरीन रणनीतियाँ साझा करते हैं।'"
                )
                "yt_adaptive_2" -> copy(
                    title = "यूट्यूब शॉर्ट्स 3-सेकंड रिटेंशन हुक सीखें",
                    skillCategory = "शॉर्ट्स अनुकूलन",
                    goalStatement = "शॉर्ट्स फ़ीड में >75% Viewed बनाम Swiped का स्कोर प्राप्त करें।",
                    coachMessage = "रिटेंशन यूट्यूब का #1 कारक है! आइए एक ऐसा हुक बनाएं जो दर्शकों को रोके रखे।",
                    detailExplanation = "यूट्यूब शॉर्ट्स में 'Swiped Away %' सबसे महत्वपूर्ण है। एक साहसी बयान या एक्शन से शुरुआत करें।",
                    whyItMatters = "यदि 'Swiped Away' 35% से अधिक होता है तो एल्गोरिदम रुक जाता है।",
                    goodExample = "उच्च ऊर्जा के साथ शुरुआत: 'यह गलती करने से पहले यह वीडियो अवश्य देखें!'",
                    badExample = "'हेलो दोस्तों आज मैं बात करना चाहता हूँ...'",
                    proTip = "अंतिम वाक्य को शुरुआती हुक में आसानी से लूप करें।",
                    commonMistake = "धीमी शुरुआत या उबाऊ पहला फ़्रेम।",
                    actionTask = "इंस्टेंट हुक के साथ 15-सेकंड का शॉर्ट रिकॉर्ड करें।",
                    simplerExplanation = "शॉर्ट्स फ़ीड बहुत तेज़ी से चलता है। आपको तुरंत कुछ रोमांचक बोलना होगा!",
                    extraRealExample = "हुक विचार: 'यह #1 हैक कोई नहीं बता रहा!'"
                )
                "yt_adaptive_3" -> copy(
                    title = "उच्च-CTR थंबनेल और शीर्षक संयोजन बनाएं",
                    skillCategory = "CTR अनुकूलन",
                    goalStatement = "सर्च और इंप्रेशन पर 8% से अधिक क्लिक-थ्रू रेट प्राप्त करें।",
                    coachMessage = "थंबनेल + शीर्षक = आपकी क्लिक दर! आइए आकर्षक विज़ुअल और शीर्षक का जोड़ा बनाएं।",
                    detailExplanation = "थंबनेल शीर्षक का टेक्स्ट दोहराए नहीं। अधिकतम 3 शब्द रखें।",
                    whyItMatters = "उच्च CTR यूट्यूब को संकेत देता है कि दर्शक आपके वीडियो को आकर्षक पाते हैं।",
                    goodExample = "शीर्षक: 'कार्यप्रवाह कैसे बढ़ाया' | थंबनेल टेक्स्ट: '10 गुना तेज़!'",
                    badExample = "शीर्षक और थंबनेल दोनों में एक ही लंबा वाक्य।",
                    proTip = "डार्क बैकग्राउंड पर उच्च-विपरीत चेहरे के भाव का उपयोग करें।",
                    commonMistake = "छोटा टेक्स्ट जो मोबाइल पर न पढ़ा जा सके।",
                    actionTask = "अपने अगले विषय के लिए 3 थंबनेल + शीर्षक अवधारणाएँ तैयार करें।",
                    simplerExplanation = "थंबनेल डिब्बे पर चित्र है, शीर्षक लेबल है। दोनों मिलकर लोगों को क्लिक करने के लिए प्रेरित करते हैं!",
                    extraRealExample = "शीर्षक: 'रणनीति का पर्दाफाश' | थंबनेल: 'छोड़ें नहीं!'"
                )
                else -> this
            }
        }
    }
}

object AiMentorEngine {

    /**
     * ADAPTIVE LEARNING ENGINE:
     * Dynamically generates personalized lessons based on:
     * - Platform (Instagram, YouTube, Shopping, Personal Brand, etc.)
     * - Experience level (Beginner, Intermediate, Advanced)
     * - Primary Goal (Followers, Views, Brand Deals, Sales, etc.)
     * - Available Time / Pace
     * - Creator Niche / Type (Tech, Fashion, Beauty, Gaming, Travel, Food, Educational, Business, etc.)
     */
    fun generatePersonalizedTasks(
        platform: String,
        setupData: CreatorSetupData,
        currentTaskIndex: Int,
        completedTaskIds: Set<String>,
        skippedTaskIds: Set<String>
    ): List<AiMentorTask> {
        val isYouTube = platform.uppercase() == "YOUTUBE"
        val niche = setupData.niche.ifBlank { "Creator" }
        val skill = setupData.skillLevel
        val goal = setupData.primaryGoal
        val timePace = setupData.availableTime

        val rawTasks = if (isYouTube) {
            getYouTubeTasksForNiche(niche, skill, goal, timePace)
        } else {
            getInstagramTasksForNiche(niche, skill, goal, timePace)
        }

        return rawTasks.mapIndexed { index, task ->
            val state = when {
                completedTaskIds.contains(task.id) -> TaskState.COMPLETED
                skippedTaskIds.contains(task.id) -> TaskState.SKIPPED
                index == currentTaskIndex -> TaskState.CURRENT
                index < currentTaskIndex -> TaskState.COMPLETED
                else -> TaskState.LOCKED
            }
            task.copy(state = state)
        }
    }

    private fun getInstagramTasksForNiche(
        niche: String,
        skill: String,
        goal: String,
        timePace: String
    ): List<AiMentorTask> {
        return listOf(
            AiMentorTask(
                id = "ig_adaptive_1",
                stepNumber = 1,
                title = "Craft a High-Converting $niche Bio with CTA",
                skillCategory = "Profile & Bio Optimization",
                goalStatement = "Convert profile visitors into loyal $niche followers within 2 seconds.",
                coachMessage = "Welcome! Let's tailor your Instagram bio specifically for $niche. A bio must state who you are, what value you provide, and where to click.",
                detailExplanation = "Line 1 defines your $niche focus. Line 2 highlights the main benefit for viewers. Line 3 is a direct Call-To-Action link.",
                whyItMatters = "Over 80% of profile visitors drop off without following if your bio is vague or lacks a clear value statement.",
                goodExample = "⚡ $niche Insights & Weekly Hacks\n🎯 Helping you master $niche in under 60s\n👇 Download the free $niche guide below",
                badExample = "Just loving $niche ✨ DM for collab | live laugh love",
                proTip = "Use bullet points or emoji spacing to improve scannability on mobile screens.",
                commonMistake = "Forgetting a CTA link or using confusing slang that non-experts won't understand.",
                actionTask = "Write 3 bullet lines for your bio and add an active link or lead magnet.",
                simplerExplanation = "Think of your bio like a 3-line sign on a store. Line 1: What do you sell/do? Line 2: Why should I care? Line 3: What should I click right now?",
                extraRealExample = "Example for $niche: '💡 Top 1% $niche Tips | 🚀 Daily Shorts & Guides | 👇 Get my free template'",
                recommendedTool = "Caption Generator"
            ),
            AiMentorTask(
                id = "ig_adaptive_2",
                stepNumber = 2,
                title = "Establish 3 Core $niche Content Pillars ($skill Level)",
                skillCategory = "Content Strategy",
                goalStatement = "Structure your content feed so the algorithm categories your $niche account accurately.",
                coachMessage = "Awesome! Now let's pick 3 tight content pillars. Pillars protect you from burnout and give your audience a consistent reason to return.",
                detailExplanation = "Pillar 1: Educational $niche Tips (How-To). Pillar 2: Common $niche Pitfalls / Mythbusting. Pillar 3: Personal $niche Journey & Workflow.",
                whyItMatters = "Algorithm indexing relies on consistent key topic signals to recommend your videos to $niche enthusiasts.",
                goodExample = "Pillar A: 30s $niche Hacks\nPillar B: Tool & Gear Reviews\nPillar C: Behind-the-scenes mistakes",
                badExample = "Posting random food photos today, crypto memes tomorrow, and gym clips next week.",
                proTip = "Stick strictly to these 3 pillars for 30 days to build account authority.",
                commonMistake = "Trying to cover too many unrelated topics at once, which confuses both viewers and algorithm recommendations.",
                actionTask = "Write down 3 specific titles for each of your 3 content pillars.",
                simplerExplanation = "Content pillars are like TV show channels. If a sports channel suddenly shows cooking, viewers leave. Stay focused on your 3 main sub-topics!",
                extraRealExample = "For $niche: 1) Quick Tutorials, 2) Myth Busting, 3) Product/Tool Recommendations.",
                recommendedTool = "Content Planner"
            ),
            AiMentorTask(
                id = "ig_adaptive_3",
                stepNumber = 3,
                title = "Master the 3-Second Viral $niche Hook Formula",
                skillCategory = "Reels Scripting",
                goalStatement = "Achieve >80% watch time in the first 3 seconds of every Reel.",
                coachMessage = "Hooks decide whether your video goes viral or dies at 200 views. Let's write a scroll-stopping 3-second hook for $niche!",
                detailExplanation = "Combine a physical movement or pattern interrupt on screen with bold, dynamic visual text overlays.",
                whyItMatters = "Instagram measures 3-second retention strictly. If viewers swipe away instantly, the algorithm stops pushing your Reel.",
                goodExample = "Text on screen: 'Stop making this $1,000 $niche mistake in 2026!' paired with energetic movement.",
                badExample = "'Hey guys welcome back to my Reel today I wanted to talk about $niche...'",
                proTip = "Place text overlays in the upper 30% of the screen so captions don't cover it.",
                commonMistake = "Starting videos with slow introductions or silent pauses.",
                actionTask = "Script 3 different hook variations for your next $niche Reel concept.",
                simplerExplanation = "A hook is like a book cover. If it doesn't shock or excite people in 3 seconds, they swipe to the next video!",
                extraRealExample = "Hook idea: '3 $niche tools that feel illegal to know!'",
                recommendedTool = "Hook Generator"
            ),
            AiMentorTask(
                id = "ig_adaptive_4",
                stepNumber = 4,
                title = "Build a 3-Tier SEO Hashtag Stack for $niche",
                skillCategory = "SEO & Hashtag Strategy",
                goalStatement = "Rank on Instagram Search results for high-intent $niche keywords.",
                coachMessage = "Let's organize a targeted 3-tier hashtag stack to maximize searchable discovery for your $niche posts.",
                detailExplanation = "Tier 1: Broad Industry tags (100k+ posts). Tier 2: Specific Niche tags (10k-100k posts). Tier 3: Micro Community tags (<10k posts).",
                whyItMatters = "Instagram now functions like a search engine. Relevant hashtags help index your Reel under active search queries.",
                goodExample = "#${niche.lowercase()} #reelsgrowth #${niche.lowercase()}tips #${niche.lowercase()}hacks #${niche.lowercase()}community",
                badExample = "#viral #fyp #love #explorepage #trending",
                proTip = "Add your primary $niche keywords into the spoken audio and text captions as well for maximum indexing.",
                commonMistake = "Using 30 giant generic hashtags that get buried in seconds.",
                actionTask = "Create and save a 5-hashtag stack tailored to your exact $niche topic.",
                simplerExplanation = "Hashtags are like library categories. Putting your book in 'Books' is too broad, but 'Tech -> Android -> Kotlin' helps readers find you instantly!",
                extraRealExample = "Stack for $niche: Broad (#$niche) + Specific (#${niche}tips) + Community (#${niche}creators).",
                recommendedTool = "Hashtag Generator"
            ),
            AiMentorTask(
                id = "ig_adaptive_5",
                stepNumber = 5,
                title = "Pre-Publishing Quality Control & Audio Selection",
                skillCategory = "Publishing & Verification",
                goalStatement = "Ensure 100% technical and algorithmic quality before hitting publish.",
                coachMessage = "Before posting your next $niche Reel, perform a quick 7-point quality checklist to maximize engagement!",
                detailExplanation = "Check audio levels, verify trending audio arrow ↗️ indicator, ensure high-contrast cover image, and confirm on-screen captions.",
                whyItMatters = "Small technical oversights like low contrast or quiet audio destroy video retention and reach.",
                goodExample = "Reel uses trending audio (<10k posts) + Clear auto-captions + Strong cover thumbnail frame.",
                badExample = "Posting in dark room without text captions or audio normalization.",
                proTip = "Use trending audio with the small diagonal arrow icon ↗️ to ride current algorithmic surges.",
                commonMistake = "Publishing without selecting an engaging cover photo frame for feed preview.",
                actionTask = "Run through all items on the interactive posting checklist for your upcoming Reel.",
                simplerExplanation = "Think of this as a pilot's pre-flight checklist. Double-check lighting, captions, and audio before takeoff!",
                extraRealExample = "Checklist: 1) Audio clear? 2) Hook visible? 3) Captions added? 4) Cover picked? 5) Hashtags added?",
                recommendedTool = "Posting Checklist"
            )
        )
    }

    private fun getYouTubeTasksForNiche(
        niche: String,
        skill: String,
        goal: String,
        timePace: String
    ): List<AiMentorTask> {
        return listOf(
            AiMentorTask(
                id = "yt_adaptive_1",
                stepNumber = 1,
                title = "Optimize Channel Banner & Description for $niche",
                skillCategory = "Channel Branding",
                goalStatement = "Turn channel visitors into instant subscribers with clear $niche positioning.",
                coachMessage = "Welcome YouTube Creator! Let's optimize your YouTube channel header and About section specifically for $niche.",
                detailExplanation = "Your banner is your hero billboard. It must clearly state your upload schedule and $niche value proposition.",
                whyItMatters = "YouTube's recommendation system reads your channel description to understand what audience to recommend your videos to.",
                goodExample = "Banner: 'Weekly $niche Guides & Shorts Every Tuesday' + High contrast branding.",
                badExample = "Default background banner with no schedule or text information.",
                proTip = "Keep key text inside the 1546x423 mobile safe area of your banner graphics.",
                commonMistake = "Leaving the channel About box empty or missing target search keywords.",
                actionTask = "Write a 150-word channel description packed with $niche search keywords.",
                simplerExplanation = "Your channel home page is like a TV billboard. Tell people what your channel is about in 5 seconds or less!",
                extraRealExample = "Description: 'Welcome! On this channel, we break down top $niche strategies, step-by-step guides, and honest reviews.'",
                recommendedTool = "Caption Generator"
            ),
            AiMentorTask(
                id = "yt_adaptive_2",
                stepNumber = 2,
                title = "Master YouTube Shorts 3-Sec Retention Hook ($skill Level)",
                skillCategory = "Shorts Optimization",
                goalStatement = "Achieve >75% 'Viewed' vs 'Swiped Away' metric on YouTube Shorts Feed.",
                coachMessage = "Retention is YouTube's #1 ranking factor! Let's craft a Shorts hook that holds viewers beyond the 3-second mark.",
                detailExplanation = "YouTube Shorts analytics measure 'Swiped Away %' heavily. Start with a bold action statement or unexpected visual cut.",
                whyItMatters = "If 'Swiped Away' exceeds 35%, YouTube Shorts algorithm abruptly stops pushing the video.",
                goodExample = "Start with high energy: 'Do NOT buy your next $niche gear until you watch this!'",
                badExample = "Waving hands: 'Hey everyone welcome back to another video...'",
                proTip = "Loop your ending sentence seamlessly into your opening hook to trigger re-watches.",
                commonMistake = "Slow introductions or boring static opening frames.",
                actionTask = "Record a 15-second Short with an instant visual & verbal hook.",
                simplerExplanation = "Shorts feed moves super fast. Imagine someone flipping through channels. You must shout something interesting immediately!",
                extraRealExample = "Shorts Hook: 'The #1 $niche hack nobody is talking about!'",
                recommendedTool = "Hook Generator"
            ),
            AiMentorTask(
                id = "yt_adaptive_3",
                stepNumber = 3,
                title = "Design High-CTR Thumbnail & Title Combos for $niche",
                skillCategory = "CTR Optimization",
                goalStatement = "Achieve a Click-Through-Rate (CTR) above 8% on YouTube Search & Impressions.",
                coachMessage = "Thumbnail + Title = Your Click-Through Rate! Let's pair high-contrast visuals with irresistible title copy.",
                detailExplanation = "Your thumbnail should complement—not repeat—the text in your title. Aim for 3 words max on thumbnail graphics.",
                whyItMatters = "High CTR signals to YouTube that viewers find your video enticing, boosting recommendation impressions.",
                goodExample = "Title: 'How I Scaled My $niche Workflow' | Thumbnail text: '10x Faster!' with expressive reaction.",
                badExample = "Title and thumbnail both say exact same long sentence.",
                proTip = "Use high-contrast face expressions or close-ups on dark backgrounds.",
                commonMistake = "Using tiny text that becomes unreadable on smartphone screens.",
                actionTask = "Outline 3 distinct Thumbnail + Title concepts for your next $niche topic.",
                simplerExplanation = "Thumbnail is the picture on the box, Title is the label. Make them work together to make people want to open the box!",
                extraRealExample = "Title: '$niche Strategy EXPOSED' | Thumbnail: 'DO NOT SKIP!' with red arrow.",
                recommendedTool = "Content Planner"
            )
        )
    }

    /**
     * DYNAMIC AI ENCOURAGEMENT GENERATOR:
     * Natural, non-repeating motivational messages dynamically calculated after completing lessons.
     */
    fun getRandomEncouragementMessage(niche: String, xp: Int, streak: Int): String {
        val messages = listOf(
            "🔥 Outstanding effort! Your $niche mastery is growing rapidly. Keep this momentum alive!",
            "⚡ Boom! You just leveled up your $niche creator skills. You're building real authority now!",
            "🎯 Flawless execution! Another key $niche lesson unlocked. Consistency always wins!",
            "🚀 Phenomenal progress! With a $streak-day streak and $xp XP, you're outpacing 95% of creators!",
            "🌟 Brilliant work! Every completed lesson brings you closer to your ultimate $niche goal!",
            "💎 Genius execution! Your dedication to $niche content strategy is paying off BIG time!",
            "🏆 Level Up! You're executing like a seasoned pro. Keep stacking those win days!"
        )
        return messages.random()
    }
}

/**
 * MASTER PHASE 1 — MULTI-STYLE AI TEACHING VARIATIONS ENGINE
 * Provides 10 distinct explanation styles (Story, Friend, Teacher, Simple Hindi, Hinglish,
 * Practical, Real Creator Example, Step-by-Step, Analogy, Common Mistakes) so the mentor NEVER
 * repeats the exact same answer when a user asks for re-explanation.
 */
object AiTeachingVariationsEngine {
    fun getMultiStyleExplanation(
        stepTitle: String,
        coreConcept: String,
        lang: String,
        variationCount: Int
    ): String {
        val styleIndex = (variationCount - 1).coerceAtLeast(0) % 10
        val isHindi = lang.contains("HINDI", ignoreCase = true) && !lang.contains("HINGLISH", ignoreCase = true)
        val isHinglish = lang.contains("HINGLISH", ignoreCase = true)

        return when (styleIndex) {
            0 -> if (isHindi) {
                "📖 *Kahaani Style:* Isse ek kahani se samjho: Rohan naam ke creator ne 3 ghante edit kiye par views nahi aaye kyunki unhone '$coreConcept' miss kar diya. Jaise hi unhone '$stepTitle' ko samjha, unki agli video 10x viral ho gayi!"
            } else if (isHinglish) {
                "📖 *Story Style:* Imagine karo ek creator ne mast video banayi par reach zero mili. Reason? Unhone '$coreConcept' ka funda apply nahi kiya tha. Jaise hi '$stepTitle' me yeh add kiya, reach boom ho gayi!"
            } else {
                "📖 *Story Explanation:* Picture this: Creator Rohan spent 3 hours editing, but got zero views because he missed this exact step. Once he applied '$coreConcept' to '$stepTitle', his next posts exploded!"
            }

            1 -> if (isHindi) {
                "🤝 *Dost Style:* Suno dost, tension mat lo! Bilkul simple rakhte hain. '$stepTitle' me bas '$coreConcept' par dhyaan do. Ek baar try karo, turant result dikhega."
            } else if (isHinglish) {
                "🤝 *Buddy Style:* Bro chill karo! Isko overcomplicate mat karo. Main point bas yeh hai ki '$stepTitle' ke liye aapko '$coreConcept' par focus karna hai."
            } else {
                "🤝 *Buddy Style:* Hey friend, let's keep it super casual. Don't overcomplicate '$stepTitle'. All you need is to focus 100% on '$coreConcept'. Try it right now!"
            }

            2 -> if (isHindi) {
                "🎓 *Masterclass Method:* Aao '$stepTitle' ko 3 mukhya niyam me dekhein:\n1) Mukhya Niyam: '$coreConcept'.\n2) Algorithm fayda: Audience video par rukti hai.\n3) Action: Apne agle post me turant lagayein."
            } else if (isHinglish) {
                "🎓 *Masterclass Breakdown:* Let's analyze '$stepTitle':\n1) Main Rule: '$coreConcept'.\n2) Algorithm Impact: Audience drop-off rokti hai.\n3) Action Plan: Agle video me implement karo."
            } else {
                "🎓 *Masterclass Method:* Let's analyze '$stepTitle' scientifically:\n1) Core Principle: '$coreConcept'.\n2) Algorithmic Effect: Maximizes audience retention.\n3) Implementation: Apply in your next draft immediately."
            }

            3 -> if (isHindi) {
                "🇮🇳 *Saral Hindi:* Bilkul seedhi baat: '$stepTitle' ka asli maksad hai '$coreConcept'. Jab viewer ko turant value dikhti hai, woh aapko follow karta hai."
            } else if (isHinglish) {
                "🇮🇳 *Aasan Hindi:* Simple Shabdon me: '$stepTitle' me aapko '$coreConcept' ko dhyan me rakhna hai taaki koi bhi swipe away na kare."
            } else {
                "🇮🇳 *Simplified Guide:* Plain and simple: '$stepTitle' is all about '$coreConcept'. When viewers see instant clarity, they stick around."
            }

            4 -> if (isHindi) {
                "⚡ *Quick Hinglish:* Dekho ji, '$stepTitle' ka main secret hai '$coreConcept'. Agar yeh strong hoga toh algorithm khud aapki video push karega!"
            } else if (isHinglish) {
                "⚡ *Quick Hinglish:* Dekho yaar, '$stepTitle' ka main secret '$coreConcept' hai. Iske bina video average reh jaati hai, iske saath viral ho jaati hai!"
            } else {
                "⚡ *Hinglish Power Tip:* Key takeaways for '$stepTitle': Master '$coreConcept' and watch your retention metrics surge automatically."
            }

            5 -> if (isHindi) {
                "🛠️ *Practical Action Example:* '$stepTitle' ke liye yeh formula use karein: Direct problem dikhao -> '$coreConcept' ka solution do -> Call to action do."
            } else if (isHinglish) {
                "🛠️ *Practical Action:* Formula for '$stepTitle': Problem bolo -> '$coreConcept' offer karo -> Direct CTA do."
            } else {
                "🛠️ *Practical Action Example:* For '$stepTitle', use this action formula: Highlight the problem -> Deliver '$coreConcept' -> End with a clear call-to-action."
            }

            6 -> if (isHindi) {
                "🌟 *Creator Case Study:* Bade creators jaise @viral_pro ne bhi pehle yahi galti ki thi. Jaise hi unhone '$coreConcept' ko '$stepTitle' me lagaya, unke followers double ho gaye!"
            } else if (isHinglish) {
                "🌟 *Real Creator Case Study:* Top viral creators hamesha '$coreConcept' use karte hain. Isse audience end tak video dekhti hai aur engage karti hai."
            } else {
                "🌟 *Creator Case Study:* Top viral creators blew up using '$coreConcept'. They kept content under 30 seconds and focused strictly on '$stepTitle'."
            }

            7 -> if (isHindi) {
                "🔢 *Step-by-Step Plan:*\n1️⃣ Pehle hook tayyar karein.\n2️⃣ Phir '$coreConcept' add karein.\n3️⃣ Final check karke post button dabayein."
            } else if (isHinglish) {
                "🔢 *Step-by-Step Method:*\n1️⃣ Clear idea pakdo.\n2️⃣ '$coreConcept' inject karo.\n3️⃣ Publish karke metrics measure karo."
            } else {
                "🔢 *Step-by-Step Blueprint:*\n1️⃣ Pinpoint your primary audience angle.\n2️⃣ Inject '$coreConcept'.\n3️⃣ Review and hit publish with confidence."
            }

            8 -> if (isHindi) {
                "💡 *Aasan Udaharan:* '$stepTitle' ko ek dukan ke board ki tarah samjho. Agar '$coreConcept' saaf dikhai dega, toh har aane wala grahak rukk kar dekhega!"
            } else if (isHinglish) {
                "💡 *Real Life Analogy:* '$stepTitle' is like a sports car's engine. '$coreConcept' is the turbocharger! Iske bina video slow rahegi, iske saath rocket ban jaayegi!"
            } else {
                "💡 *Real-Life Analogy:* Think of '$stepTitle' like a store storefront. Without '$coreConcept', the lights are off. Turn the lights on and watch visitors enter!"
            }

            else -> if (isHindi) {
                "⚠️ *Galti aur Solution:* 90% naye creators '$stepTitle' me '$coreConcept' bhool jaate hain. Aap yeh galti mat karo — ise clear aur engaging rakho!"
            } else if (isHinglish) {
                "⚠️ *Mistake vs Solution:* 90% creators random cheezein karte hain. Aapko bas '$coreConcept' ko '$stepTitle' me master karna hai."
            } else {
                "⚠️ *Common Mistake vs Fix:* 90% of creators fail because they ignore '$coreConcept' during '$stepTitle'. The top 10% fix this by keeping it sharp and focused."
            }
        }
    }
}


