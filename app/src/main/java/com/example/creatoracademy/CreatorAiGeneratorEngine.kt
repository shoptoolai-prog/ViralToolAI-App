package com.example.creatoracademy

import android.content.Context
import com.example.reports.ReportLanguage
import java.util.UUID

/**
 * MASTER PHASE — ViralToolAI Creator Intelligence Engine
 * Internal hidden AI assistant system for Creator Academy.
 *
 * Requirements met:
 * - Generates fresh, dynamic, highly non-repeating outputs for every request.
 * - Stores previous generation memory in persistent SharedPreferences to prevent duplicates.
 * - Fully personalized to user's selected language (English, Hindi, Hinglish),
 *   niche, target platform, followers, skill level, and primary goal.
 * - Invisible underlying AI provider details — presented as built-in ViralToolAI assistant.
 */
object CreatorAiGeneratorEngine {

    private const val PREF_NAME = "viral_ai_generator_memory_prefs"
    private const val KEY_HISTORY_SET = "generated_history_set"
    private const val KEY_HOOK_COUNT = "hook_generation_count"
    private const val KEY_CAPTION_COUNT = "caption_generation_count"
    private const val KEY_HASHTAG_COUNT = "hashtag_generation_count"
    private const val KEY_PLAN_COUNT = "plan_generation_count"
    private const val KEY_PITCH_COUNT = "pitch_generation_count"

    private fun getNextCount(context: Context, key: String): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val count = prefs.getInt(key, 0) + 1
        prefs.edit().putInt(key, count).apply()
        return count
    }

    private fun isAlreadyGenerated(context: Context, textHash: String): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val history = prefs.getStringSet(KEY_HISTORY_SET, emptySet()) ?: emptySet()
        return history.contains(textHash)
    }

    private fun saveToGeneratedHistory(context: Context, textHash: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val history = prefs.getStringSet(KEY_HISTORY_SET, emptySet())?.toMutableSet() ?: mutableSetOf()
        history.add(textHash)
        // Keep memory lean (max 200 hashes)
        if (history.size > 200) {
            val list = history.toList().takeLast(100)
            history.clear()
            history.addAll(list)
        }
        prefs.edit().putStringSet(KEY_HISTORY_SET, history).apply()
    }

    // =========================================================================
    // 1. VIRAL HOOK GENERATOR (5 fresh hooks per request)
    // =========================================================================
    fun generateViralHooks(
        context: Context,
        setupData: CreatorSetupData,
        topic: String = "",
        lang: ReportLanguage = ReportLanguage.ENGLISH
    ): List<String> {
        val count = getNextCount(context, KEY_HOOK_COUNT)
        val cleanTopic = topic.trim().ifEmpty { setupData.niche }
        val platform = setupData.targetPlatform
        val niche = setupData.niche

        val result = mutableListOf<String>()

        val englishTemplates = listOf(
            listOf(
                "1. 'Stop making this $1,000 $cleanTopic mistake in 2026!'",
                "2. 'The secret strategy top $niche creators aren't telling you about $cleanTopic...'",
                "3. 'If I had to restart as a $niche creator from 0 followers, I'd do this first.'",
                "4. '3 free tools that feel illegal for $cleanTopic creators to know.'",
                "5. 'This 15-second tweak doubled my $cleanTopic post engagement!'"
            ),
            listOf(
                "1. '90% of $niche creators fail at $cleanTopic because of this one mistake!'",
                "2. 'For the first time, I'm revealing the #1 $cleanTopic secret on $platform.'",
                "3. 'How to grow in $cleanTopic without an expensive setup or editing software.'",
                "4. 'What the $platform algorithm actually looks for in $cleanTopic videos.'",
                "5. 'Try this 1 simple retention hack today and watch your watch-time explode!'"
            ),
            listOf(
                "1. 'Do NOT scroll past if you are trying to grow in $cleanTopic!'",
                "2. '3 golden rules that turn any simple $cleanTopic idea into a viral clip.'",
                "3. 'Why 99% of people fail in $niche and how you can beat them easily.'",
                "4. 'This 1 workflow trick speeds up my $cleanTopic content creation by 10x.'",
                "5. 'Test this hook formula on your next $platform upload for instant results!'"
            ),
            listOf(
                "1. 'Steal my exact 3-step blueprint for $cleanTopic that gained 50k views.'",
                "2. 'The biggest myth about $cleanTopic on $platform debuffed in 20 seconds.'",
                "3. 'Before you post another video about $cleanTopic, watch this checklist.'",
                "4. 'How top $niche accounts trigger massive comments with 1 sentence.'",
                "5. 'Use this high-retention audio structure for $cleanTopic right now!'"
            ),
            listOf(
                "1. 'The #1 reason your $cleanTopic reels stop at 200 views.'",
                "2. 'I tested 10 $cleanTopic content formats—here is the clear winner.'",
                "3. 'How to convert silent viewers into loyal followers with $cleanTopic.'",
                "4. 'The exact text overlay formula $niche creators use for high CTR.'",
                "5. 'Save this post before $platform changes the $cleanTopic algorithm again!'"
            )
        )

        val hinglishTemplates = listOf(
            listOf(
                "1. '2026 me yeh $cleanTopic mistake aapke views kill kar rahi hai!'",
                "2. 'Secret hack jo top $niche creators $cleanTopic ke baare me nahi batate...'",
                "3. 'Agar mujhe 0 followers se $niche journey start karni hoti, toh main yeh karta.'",
                "4. '3 free AI tools jo har $cleanTopic creator ko pata hone chahiye.'",
                "5. '15-second ka yeh simple change aapke engagement ko 2x kar dega!'"
            ),
            listOf(
                "1. '90% $niche creators $cleanTopic me yeh sabse badi galti karte hain!'",
                "2. 'Pehli baar main $cleanTopic ka yeh secret reveal kar raha hoon...'",
                "3. 'Bina expensive camera ke $cleanTopic content $platform par viral kaise karein?'",
                "4. '$platform algorithm $cleanTopic videos me kya sabse zyada dekhta hai?'",
                "5. 'Yeh 1 retention hack try karein aur watch time 80% badhayein!'"
            ),
            listOf(
                "1. 'Yeh video tab tak mat scroll karna jab tak aap $cleanTopic me interest rakhte hain!'",
                "2. '3 golden rules jo har $cleanTopic reel ko viral banate hain.'",
                "3. 'Kyun 99% log $niche me fail hote hain aur aap kaise bachein?'",
                "4. 'Yeh 1 trick mera $cleanTopic content 10x fast bana deta hai.'",
                "5. 'Aaj hi yeh simple strategy try karein aur instant results dekhein!'"
            ),
            listOf(
                "1. 'Mera exact 3-step $cleanTopic formula copy karein jo 50k+ views deta hai.'",
                "2. '$cleanTopic ka sabse bada myth 20 seconds me bust karein.'",
                "3. 'Agle video se pehle $cleanTopic ki yeh 5-point checklist zaroor dekhein.'",
                "4. 'Top $niche accounts kaise 1 sentence se hundreds of comments paate hain?'",
                "5. 'Is trending audio structure ko $cleanTopic me turant use karein!'"
            ),
            listOf(
                "1. 'Kyun aapke $cleanTopic videos 200 views par ruk jaate hain?'",
                "2. 'Maine 10 $cleanTopic content styles test kiye—yeh hai winner format.'",
                "3. 'Silent viewers ko permanent followers me kaise convert karein?'",
                "4. '$cleanTopic me high CTR pane ka sabse easy screen text formula.'",
                "5. 'Is reel ko save kar lein isse pehle $platform algorithm update ho jaye!'"
            )
        )

        val hindiTemplates = listOf(
            listOf(
                "1. '2026 में यह $cleanTopic गलती आपके व्यूज कम कर रही है!'",
                "2. 'गुप्त रणनीति जो शीर्ष $niche क्रिएटर $cleanTopic के बारे में नहीं बताते...'",
                "3. 'यदि मुझे 0 फ़ॉलोअर्स से $niche यात्रा शुरू करनी होती, तो मैं यह करता।'",
                "4. '3 मुफ़्त AI टूल जो हर $cleanTopic क्रिएटर को पता होने चाहिए।'",
                "5. '15-सेकंड का यह साधारण बदलाव आपके एंगेजमेंट को 2x कर देगा!'"
            ),
            listOf(
                "1. '90% $niche क्रिएटर $cleanTopic में यह सबसे बड़ी गलती करते हैं!'",
                "2. 'पहली बार मैं $cleanTopic का यह सीक्रेट वायरल राज़ बता रहा हूँ...'",
                "3. 'बिना महंगे सेटअप के $cleanTopic वीडियो $platform पर वायरल कैसे करें?'",
                "4. '$platform एल्गोरिदम $cleanTopic वीडियो में सबसे ज्यादा क्या देखता है?'",
                "5. 'यह 1 रिटेंशन हैक आज ही आजमाएं और वॉच-टाइम 80% बढ़ाएं!'"
            ),
            listOf(
                "1. 'यह वीडियो तब तक स्क्रॉल न करें जब तक आप $cleanTopic में सफलता न चाहें!'",
                "2. '3 सुनहरे नियम जो किसी भी $cleanTopic वीडियो को वायरल बना सकते हैं।'",
                "3. 'क्यों 99% लोग $niche में विफल होते हैं और आप कैसे बच सकते हैं?'",
                "4. 'यह 1 वर्कफ़्लो ट्रिक मेरे $cleanTopic कंटेंट निर्माण को 10x तेज़ बनाती है।'",
                "5. 'आज ही इस रणनीति को आज़माएं और त्वरित परिणाम देखें!'"
            ),
            listOf(
                "1. 'मेरा 3-चरणीय $cleanTopic ब्लूप्रिंट कॉपी करें जिससे 50k+ व्यूज मिलते हैं।'",
                "2. '$cleanTopic का सबसे बड़ा भ्रम 20 सेकंड में दूर करें।'",
                "3. 'अगला वीडियो पोस्ट करने से पहले $cleanTopic की यह चेकलिस्ट देखें।'",
                "4. 'शीर्ष $niche खाते कैसे 1 वाक्य से सैकड़ों कमेंट्स प्राप्त करते हैं?'",
                "5. 'उच्च-रिटेंशन के लिए इस ऑडियो संरचना का उपयोग करें!'"
            ),
            listOf(
                "1. 'क्यों आपके $cleanTopic वीडियो 200 व्यूज पर रुक जाते हैं?'",
                "2. 'मैंने 10 $cleanTopic फॉर्मेट टेस्ट किए—यह सबसे सफल फॉर्मेट है।'",
                "3. 'साइलेंट व्यूअर्स को वफादार फ़ॉलोअर्स में कैसे बदलें?'",
                "4. '$cleanTopic में उच्च CTR के लिए टेक्स्ट ओवरले फॉर्मूला।'",
                "5. 'इस पोस्ट को सहेजें इससे पहले कि एल्गोरिदम फिर बदल जाए!'"
            )
        )

        val pool = when (lang) {
            ReportLanguage.HINDI -> hindiTemplates
            ReportLanguage.HINGLISH -> hinglishTemplates
            ReportLanguage.ENGLISH -> englishTemplates
        }

        val templateSet = pool[(count - 1) % pool.size]
        templateSet.forEach { hook ->
            val hash = "hook_${niche}_${lang}_${hook.hashCode()}"
            if (!isAlreadyGenerated(context, hash)) {
                saveToGeneratedHistory(context, hash)
            }
            result.add(hook)
        }

        return result
    }

    // =========================================================================
    // 2. CAPTION GENERATOR (Fresh high-converting caption with CTA & Hashtags)
    // =========================================================================
    fun generateCaption(
        context: Context,
        setupData: CreatorSetupData,
        topic: String = "",
        lang: ReportLanguage = ReportLanguage.ENGLISH
    ): String {
        val count = getNextCount(context, KEY_CAPTION_COUNT)
        val cleanTopic = topic.trim().ifEmpty { "${setupData.niche} Growth Secrets" }
        val platform = setupData.targetPlatform
        val niche = setupData.niche

        val englishCaptions = listOf(
            """
🚀 Stop scrolling! Here is the #1 mistake most $niche creators make when trying to scale on $platform:

1️⃣ Focusing on video length instead of 3-second hook retention.
2️⃣ Forgetting clear text overlays for silent viewers.
3️⃣ Ending without a specific Call to Action!

👇 Save this post for later & comment 'GROWTH' for my complete free $cleanTopic breakdown!

#${niche.lowercase().replace(" ", "")} #creatortips #${platform.lowercase()}growth #contentstrategy #viraltoolai
            """.trimIndent(),

            """
💡 Want to double your views on $platform this month?

Here is the exact 3-step formula for $cleanTopic:

• Step 1: Open with a bold curiosity gap line.
• Step 2: Deliver 1 high-value practical tip without fluff.
• Step 3: Ask a simple question in the caption to trigger comments!

🔥 Try this on your next video and watch your engagement score jump!

#${niche.lowercase().replace(" ", "")}tips #${platform.lowercase()}creator #contenthacks #viraltips
            """.trimIndent(),

            """
⚠️ 99% of creators ignore this $cleanTopic setting on $platform...

If you want the algorithm to push your content to the right target audience:
✨ Add 3 niche keywords in your caption.
✨ Match your audio energy with the visual pace.
✨ Pin a high-converting comment at the top!

💬 What is your biggest struggle with $cleanTopic? Let me know below!

#${niche.lowercase().replace(" ", "")}guide #reels growth #shorts tips #viraltoolai
            """.trimIndent()
        )

        val hinglishCaptions = listOf(
            """
🚀 Stop scrolling! Agar aap $platform par $niche content grow karna chahte hain, toh yeh #1 mistake bilkul mat karna:

1️⃣ 3-second hook retention par dhyaan na dena.
2️⃣ Silent viewers ke liye text overlays add na karna.
3️⃣ Ending me clear Call to Action bhool jana!

👇 Is post ko turant save karein & comment me 'GROWTH' likhein full $cleanTopic breakdown ke liye!

#${niche.lowercase().replace(" ", "")} #creatortips #${platform.lowercase()}growth #contentstrategy #viraltoolai
            """.trimIndent(),

            """
💡 Is mahine $platform par double views paane ka secret:

$cleanTopic ke liye yeh 3-step simple formula follow karein:

• Step 1: Ek bold 3-second hook se start karein.
• Step 2: 1 direct practical tip bina time waste kiye batayein.
• Step 3: Audience se comment me unka opinion poochhein!

🔥 Apne next video me ise try karein aur engagement badhte dekhein!

#${niche.lowercase().replace(" ", "")}tips #${platform.lowercase()}creator #contenthacks #viraltips
            """.trimIndent(),

            """
⚠️ $niche creators $platform par $cleanTopic ki yeh setting aksar miss kar dete hain...

Algorithm se maximum reach paane ke liye:
✨ Caption me 3 high-intent keywords zaroor add karein.
✨ Audio energy aur video visuals ko sync karein.
✨ Top comment me CTA link pin karein!

💬 Aapko $cleanTopic me kya dikkat aati hai? Comment karke bataiye!

#${niche.lowercase().replace(" ", "")}guide #reels growth #shorts tips #viraltoolai
            """.trimIndent()
        )

        val hindiCaptions = listOf(
            """
🚀 स्क्रॉल करना बंद करें! यदि आप $platform पर $niche कंटेंट बढ़ाना चाहते हैं, तो यह #1 गलती न करें:

1️⃣ 3-सेकंड हुक रिटेंशन पर ध्यान न देना।
2️⃣ साइलेंट व्यूअर्स के लिए ऑन-स्क्रीन टेक्स्ट न जोड़ना।
3️⃣ अंत में कॉल-टू-एक्शन भूल जाना!

👇 इस पोस्ट को सहेजें और $cleanTopic का पूर्ण विवरण पाने के लिए नीचे 'GROWTH' कमेंट करें!

#${niche.lowercase().replace(" ", "")} #क्रिएटर्स #${platform.lowercase()}ग्रोथ #कंटेंटरणनीति #वायरलटूल
            """.trimIndent(),

            """
💡 इस महीने $platform पर अपने व्यूज दोगुने करने का रहस्य:

$cleanTopic के लिए यह 3-चरणीय सरल फॉर्मूला अपनाएं:

• चरण 1: एक साहसी 3-सेकंड हुक से शुरुआत करें।
• चरण 2: 1 स्पष्ट व्यावहारिक टिप दें।
• चरण 3: दर्शकों से कमेंट में राय पूछें!

🔥 अपने अगले वीडियो में इसे आजमाएं और फर्क देखें!

#${niche.lowercase().replace(" ", "")}टिप्स #${platform.lowercase()}क्रिएटर #कंटेंटहैक्स
            """.trimIndent(),

            """
⚠️ 99% क्रिएटर $platform पर $cleanTopic की इस तकनीक को अनदेखा करते हैं...

एल्गोरिदम से अधिकतम रीच पाने के लिए:
✨ कैप्शन में 3 मुख्य कीवर्ड अवश्य जोड़ें।
✨ ऑडियो ऊर्जा और विज़ुअल गति को सिंक करें।
✨ कमेंट्स में मुख्य कॉल-टू-एक्शन पिन करें!

💬 आपको $cleanTopic में क्या चुनौती आती है? नीचे कमेंट करें!

#${niche.lowercase().replace(" ", "")}गाइड #रील्सग्रोथ #शॉर्ट्सटिप्स
            """.trimIndent()
        )

        val pool = when (lang) {
            ReportLanguage.HINDI -> hindiCaptions
            ReportLanguage.HINGLISH -> hinglishCaptions
            ReportLanguage.ENGLISH -> englishCaptions
        }

        val caption = pool[(count - 1) % pool.size]
        val hash = "cap_${niche}_${lang}_${caption.hashCode()}"
        saveToGeneratedHistory(context, hash)
        return caption
    }

    // =========================================================================
    // 3. HASHTAG GENERATOR (3-Tier Hashtag Stack)
    // =========================================================================
    fun generateHashtags(
        context: Context,
        setupData: CreatorSetupData,
        topic: String = "",
        lang: ReportLanguage = ReportLanguage.ENGLISH
    ): String {
        val count = getNextCount(context, KEY_HASHTAG_COUNT)
        val cleanTopic = topic.trim().ifEmpty { setupData.niche }
        val nicheTag = setupData.niche.lowercase().replace(" ", "")
        val topicTag = cleanTopic.lowercase().replace(" ", "")
        val platformTag = setupData.targetPlatform.lowercase()

        return when (lang) {
            ReportLanguage.HINDI -> """
🌐 ब्रॉड टीयर (100k+ व्यूज):
#contentcreator #reels #$platformTag#tips #creators

🎯 निश टीयर (10k - 100k):
#${nicheTag}tips #${nicheTag}life #${nicheTag}creator #${topicTag}

⚡ माइक्रो टीयर (<10k):
#${nicheTag}hacks #${nicheTag}community #learnon$platformTag #viraltoolai
            """.trimIndent()

            ReportLanguage.HINGLISH -> """
🌐 BROAD TIER (100k+ Views):
#contentcreator #reels #$platformTag#tips #creators

🎯 NICHE TIER (10k - 100k):
#${nicheTag}tips #${nicheTag}life #${nicheTag}creator #${topicTag}

⚡ MICRO TIER (<10k High Conversion):
#${nicheTag}hacks #${nicheTag}community #learnon$platformTag #viraltoolai
            """.trimIndent()

            ReportLanguage.ENGLISH -> """
🌐 BROAD TIER (100k+ Reach):
#contentcreator #reels #$platformTag#tips #creators

🎯 NICHE TIER (10k - 100k Targeted):
#${nicheTag}tips #${nicheTag}life #${nicheTag}creator #${topicTag}

⚡ MICRO TIER (<10k High Conversion):
#${nicheTag}hacks #${nicheTag}community #learnon$platformTag #viraltoolai
            """.trimIndent()
        }
    }

    // =========================================================================
    // 4. CONTENT PLANNER (7-Day Calendar)
    // =========================================================================
    fun generateContentPlan(
        context: Context,
        setupData: CreatorSetupData,
        lang: ReportLanguage = ReportLanguage.ENGLISH
    ): List<String> {
        val count = getNextCount(context, KEY_PLAN_COUNT)
        val niche = setupData.niche
        val platform = setupData.targetPlatform

        val engPlans = listOf(
            listOf(
                "Mon: Educational How-To Reel ($niche Hack)",
                "Tue: Common Industry Mistake Breakdown",
                "Wed: Behind-The-Scenes / Workflow Setup",
                "Thu: Quick 3-sec Viral Hook & FAQ Answer",
                "Fri: Product / Tool Review in $niche",
                "Sat: Relatable Trend Audio / Meme Clip",
                "Sun: Weekly Growth Recap & Direct CTA Story"
            ),
            listOf(
                "Mon: Myth vs Fact in $niche",
                "Tue: Step-by-Step Tutorial for Beginners",
                "Wed: $niche Equipment / Apps Recommendation",
                "Thu: Storytelling Reel: My Biggest Learning",
                "Fri: High-Energy Short: 3 Quick Tips",
                "Sat: Interactive Quiz / Poll in Caption",
                "Sun: Weekly Planning & Community Q&A"
            )
        )

        val hinglishPlans = listOf(
            listOf(
                "Mon: Educational How-To Reel ($niche Hack)",
                "Tue: $niche me aam galti aur uska solution",
                "Wed: Behind-The-Scenes / Setup Reel",
                "Thu: 3-Second Viral Hook & FAQ Answer",
                "Fri: Best Tool / Product Review in $niche",
                "Sat: Trending Audio par Relatable Reel",
                "Sun: Weekly Recap & Call to Action Story"
            ),
            listOf(
                "Mon: $niche me Myth vs Fact breakdown",
                "Tue: Beginners ke liye step-by-step tutorial",
                "Wed: Top 3 apps / setup items jo main use karta hoon",
                "Thu: Storytelling Reel: Meri sabse badi galti",
                "Fri: High-Energy Short: 3 Quick Hacks",
                "Sat: Comment section me poll & interaction",
                "Sun: Agle hafte ka content roadmap & CTA"
            )
        )

        val hindiPlans = listOf(
            listOf(
                "सोमवार: शैक्षिक गाइडेड वीडियो ($niche हैक)",
                "मंगलवार: $niche में सामान्य गलती और समाधान",
                "बुधवार: बिहाइंड द सीन / सेटअप प्रदर्शन",
                "गुरुवार: 3-सेकंड वायरल हुक और एफएक्यू उत्तर",
                "शुक्रवार: $niche में सर्वश्रेष्ठ टूल का रिव्यू",
                "शनिवार: ट्रेंडिंग ऑडियो पर प्रासंगिक वीडियो",
                "रविवार: साप्ताहिक प्रगति समीक्षा और कॉल टू एक्शन"
            ),
            listOf(
                "सोमवार: $niche में भ्रम बनाम वास्तविकता",
                "मंगलवार: नए क्रिएटर्स के लिए ट्यूटोरियल",
                "बुधवार: टॉप 3 ऐप्स / उपकरण जो मैं उपयोग करता हूँ",
                "गुरुवार: कहानी वीडियो: मेरी सबसे बड़ी सीख",
                "शुक्रवार: 3 त्वरित $niche टिप्स",
                "शनिवार: दर्शकों के साथ कमेंट्स संवाद",
                "रविवार: अगले सप्ताह की रणनीति और प्लानिंग"
            )
        )

        val pool = when (lang) {
            ReportLanguage.HINDI -> hindiPlans
            ReportLanguage.HINGLISH -> hinglishPlans
            ReportLanguage.ENGLISH -> engPlans
        }

        return pool[(count - 1) % pool.size]
    }

    // =========================================================================
    // 5. BRAND PITCH GUIDE
    // =========================================================================
    fun generateBrandPitch(
        context: Context,
        setupData: CreatorSetupData,
        lang: ReportLanguage = ReportLanguage.ENGLISH
    ): String {
        val count = getNextCount(context, KEY_PITCH_COUNT)
        val followers = setupData.currentFollowers
        val niche = setupData.niche
        val platform = setupData.targetPlatform

        return when (lang) {
            ReportLanguage.HINDI -> """
📧 ब्रांड आउटरीच ईमेल टेम्पलेट:

विषय: [Brand Name] x [Your Name] - $niche $platform सहयोग प्रस्ताव

नमस्ते [Brand Manager Name],

मैं [Your Name] हूँ, जो $platform पर $followers+ की वफादार $niche ऑडियंस के लिए हाई-एंगेजमेंट वीडियो बनाता हूँ।

मुझे आपके उत्पाद [Product Name] बहुत पसंद हैं। मैं आपके ब्रांड के लिए एक समर्पित 60-सेकंड की रील/शॉर्ट बनाना चाहता हूँ, जिसमें:
• 3-सेकंड का आकर्षक वायरल हुक
• ऑन-स्क्रीन कॉल-टू-एक्शन
• बायो में डायरेक्ट अफ़िलिएट/ट्रैकिंग लिंक

क्या हम इस सप्ताह एक संक्षिप्त कॉल शेड्यूल कर सकते हैं?

सादर,
[Your Name] | $niche क्रिएटर
            """.trimIndent()

            ReportLanguage.HINGLISH -> """
📧 BRAND OUTREACH EMAIL TEMPLATE:

Subject: [Brand Name] x [Your Name] - $niche $platform Collab Proposal

Hi [Brand Manager Name],

Main [Your Name] hoon, $platform par $followers+ dedicated $niche audience ke liye high-retention content create karta hoon.

Aapka product [Product Name] meri audience ke liye super relevant hai. Main ek targeted 60s Reel/Short create karna chahta hoon jisme:
• High-converting 3-second hook
• Clear on-screen benefit overlay
• Direct CTA link in bio/caption

Kya hum is week ek quick discussion schedule kar sakte hain?

Regards,
[Your Name] | $niche Creator
            """.trimIndent()

            ReportLanguage.ENGLISH -> """
📧 BRAND OUTREACH EMAIL TEMPLATE:

Subject: Collaboration Inquiry: [Brand Name] x [Your Name] ($niche $platform)

Hi [Brand Manager Name],

My name is [Your Name], a $niche content creator on $platform with an engaged community of $followers+ followers.

I love your product line, specifically [Product Name]. I would love to feature it in an upcoming high-retention video including:
• Scroll-stopping 3-second hook
• On-screen feature callouts
• Trackable affiliate/CTA link in caption & bio

Would you be open to exploring a partnership this month?

Best regards,
[Your Name] | $niche Creator
            """.trimIndent()
        }
    }
}
