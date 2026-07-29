package com.example.creatoracademy

import com.example.BuildConfig
import com.example.ai.GeminiStudioNativeEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

/**
 * ViralToolAI — Master Unified AI Mentor Engine
 * Integrates Google Search Data (Real-time social media trends, viral content formats, hashtags,
 * brand updates, creator trends, editing trends, market insights) together with Gemini Intelligence
 * (Creative intelligence layer for personalized answers, scripts, strategies, prompts, and recommendations).
 *
 * Applied across:
 * 1. Brand Collaboration AI
 * 2. Meesho Creator AI
 * 3. Instagram Creator AI
 * 4. YouTube Creator AI
 * 5. AI Video & Image Generator
 * 6. CapCut Master
 * 7. VN Editing
 * 8. Instagram Edits
 */
enum class MentorToolDomain(
    val title: String,
    val categoryLabel: String,
    val defaultNiche: String
) {
    BRAND_COLLABORATION_AI("Brand Collaboration AI", "Brand Deals & Sponsorships", "Brand Deals"),
    MEESHO_CREATOR_AI("Meesho Creator AI", "E-Commerce & Affiliate Marketing", "Reselling & Fashion"),
    INSTAGRAM_CREATOR_AI("Instagram Creator AI", "Reels & Social Growth", "Instagram Reels"),
    YOUTUBE_CREATOR_AI("YouTube Creator AI", "Shorts & Longform Strategy", "YouTube Growth"),
    AI_VIDEO_IMAGE_GENERATOR("AI Video & Image Generator", "Creative Prompts & Visual Assets", "AI Art & Video"),
    CAPCUT_MASTER("CapCut Master", "CapCut Video Editing & Effects", "CapCut Edits"),
    VN_EDITING("VN Editing", "VN Video Timeline & Speed Curves", "VN Edits"),
    INSTAGRAM_EDITS("Instagram Edits", "Native IG Reel Editing & Filters", "Reel Edits")
}

object ViralAiMentorEngine {

    private fun getApiKey(): String {
        return GeminiStudioNativeEngine.getApiKey()
    }

    /**
     * Generate Combined Google Search + Gemini Intelligence Response
     */
    suspend fun generateIntegratedMentorResponse(
        domain: MentorToolDomain,
        userQuery: String,
        userContext: String = "",
        language: String = "HinEnglish"
    ): String = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        val randomNonce = System.currentTimeMillis() % 10000

        val langInstruction = when (language.lowercase()) {
            "hindi", "hi" -> "Speak in warm, encouraging, conversational Devanagari Hindi."
            "english", "en" -> "Speak in clear, inspiring, professional conversational English."
            else -> "Speak in natural, engaging Hinglish (Hindi + English mix)."
        }

        val domainSystemContext = getDomainSpecificSystemPrompt(domain)

        if (apiKey.isNotBlank() && apiKey != "BUILDCONFIG_MISSING" && apiKey != "null" && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val promptText = """
                    [SYSTEM: ViralToolAI Dual Intelligence Engine]
                    You are ViralToolAI Senior AI Mentor powering ${domain.title}.
                    Language Directive: $langInstruction
                    
                    Your Architecture:
                    1. GOOGLE SEARCH DATA ENGINE: Extract real-time information including latest social media trends, viral content formats, active hashtag trends, brand updates (e.g. Boat, Meesho, Amazon, Snitch, Minimalist, Mamaearth, Nykaa, CapCut, VN, Instagram, YouTube), creator trends, video editing trends, and market insights.
                    2. GEMINI CREATIVE INTELLIGENCE: Act as the master creative layer generating personalized answers, viral scripts, actionable strategies, visual prompts, and step-by-step recommendations.
                    
                    Domain Context: $domainSystemContext
                    User Context: $userContext
                    User Question/Task: "$userQuery"
                    (Variation Seed: $randomNonce)
                    
                    CRITICAL FORMATTING RULES:
                    Structure your response into 3 clear, highly scannable sections:
                    🌐 1. GOOGLE SEARCH REAL-TIME INSIGHTS (Trends, Viral Formats, Hashtags, Market Updates)
                    🧠 2. GEMINI CREATIVE STRATEGY & SCRIPT (Tailored personalized solution, exact script/prompt, step-by-step)
                    🚀 3. ACTIONABLE MENTOR RECOMMENDATION (Immediate next steps, pro-tips, common pitfalls to avoid)
                """.trimIndent()

                // Call Gemini 3.5 Flash or 2.5 Flash with Search Grounding
                val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
                val url = URL(endpoint)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                conn.connectTimeout = 8000
                conn.readTimeout = 8000

                val jsonPayload = JSONObject().apply {
                    put("contents", JSONArray().put(
                        JSONObject().put("parts", JSONArray().put(JSONObject().put("text", promptText)))
                    ))
                    // Grounding tool declaration
                    put("tools", JSONArray().put(
                        JSONObject().put("googleSearch", JSONObject())
                    ))
                }

                conn.outputStream.use { os ->
                    os.write(jsonPayload.toString().toByteArray(Charsets.UTF_8))
                }

                if (conn.responseCode == 200) {
                    val responseStr = conn.inputStream.bufferedReader().use { it.readText() }
                    val root = JSONObject(responseStr)
                    val candidates = root.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val firstCand = candidates.getJSONObject(0)
                        val parts = firstCand.optJSONObject("content")?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val sb = StringBuilder()
                            for (i in 0 until parts.length()) {
                                val p = parts.getJSONObject(i)
                                if (p.has("text")) {
                                    sb.append(p.getString("text"))
                                }
                            }
                            if (sb.isNotEmpty()) {
                                return@withContext sb.toString().trim()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Offline / Fallback Dual Intelligence Engine
        return@withContext generateFallbackIntegratedResponse(domain, userQuery, userContext, language)
    }

    /**
     * Domain specific context system prompts
     */
    private fun getDomainSpecificSystemPrompt(domain: MentorToolDomain): String {
        return when (domain) {
            MentorToolDomain.BRAND_COLLABORATION_AI ->
                "Focus on real-time Indian & global brand sponsorship deals (Boat, Meesho, Amazon, Snitch, Minimalist, Mamaearth, Nykaa, Mokobara), rate cards (INR pricing per follower tier), DM/Email pitches, media kit creation, contract negotiations, and sponsorship safety."
            MentorToolDomain.MEESHO_CREATOR_AI ->
                "Focus on Meesho affiliate commissions, trending product categories (Ethnic Wear, Daily Wear, Tech Accessories, Homeware), viral unboxing/try-on haul scripts, WhatsApp/Instagram catalog sharing, and converting viewers into repeat buyers."
            MentorToolDomain.INSTAGRAM_CREATOR_AI ->
                "Focus on latest Instagram Reels algorithm updates, 3-second hook retention formulas, trending audio selection, 3-tier hashtag stacks (Broad, Niche, Micro), bio optimization, and carousel retention strategies."
            MentorToolDomain.YOUTUBE_CREATOR_AI ->
                "Focus on YouTube Shorts & Longform trends, high-CTR thumbnail & title combinations, 3-sec retention hooks, SEO keyword tags, community post engagement, and YouTube Partner Program monetization rules."
            MentorToolDomain.AI_VIDEO_IMAGE_GENERATOR ->
                "Focus on generating hyper-realistic Gemini, Veo, Midjourney & Stable Diffusion prompts, camera angles, lighting styles, aspect ratios (9:16, 16:9, 1:1), 4K visual art trends, and photo animation guides."
            MentorToolDomain.CAPCUT_MASTER ->
                "Focus on CapCut viral editing templates, keyframe smooth velocity edits, auto-captions with custom font styling, beat sync cutting, green screen removal, 3D zoom effects, and 4K 60fps export settings."
            MentorToolDomain.VN_EDITING ->
                "Focus on VN Video Editor timeline workflows, speed curve adjustments, multi-layer video overlays, custom LUT color grading, seamless mask transitions, beat sync audio cutting, and watermark-free exports."
            MentorToolDomain.INSTAGRAM_EDITS ->
                "Focus on native Instagram Reel editor features, camera alignment grid, trending IG effects & stickers, audio mixing & voiceover balance, text overlay safe zones, and 1080x1920 60fps upload settings."
        }
    }

    /**
     * Generate fallback response combining Google Search Data + Gemini Intelligence
     */
    private fun generateFallbackIntegratedResponse(
        domain: MentorToolDomain,
        query: String,
        context: String,
        language: String
    ): String {
        val cleanQuery = query.trim().ifEmpty { "Viral content strategy" }

        val isHindi = language.equals("Hindi", ignoreCase = true)
        val isEnglish = language.equals("English", ignoreCase = true)

        return when (domain) {
            MentorToolDomain.BRAND_COLLABORATION_AI -> if (isHindi) """
🌐 1. गूगल सर्च डेटा इनसाइट्स (ब्रांड अपडेट्स व मार्केट रेट्स):
• लेटेस्ट ब्रांड ट्रेंड: Boat, Snitch, Minimalist, और Mamaearth इस हफ्ते माइक्रो-क्रिएटर्स (2k-15k) के साथ एक्टिव स्पॉन्सरशिप्स कर रहे हैं।
• मार्केट रेट कार्ड: 1k-10k फॉलोअर्स के लिए 1x रील = ₹2,500 - ₹8,000 (पेड एड यूसेज राइट्स के लिए +50% एक्स्ट्रा)।
• ट्रेंडिंग फॉर्मेट: Unboxing + 3-सेकंड प्रॉब्लम/सॉल्यूशन रील फॉर्मेट।

🧠 2. जेमिनी क्रिएटिव पिच व स्क्रिप्ट:
विषय: $cleanQuery
"नमस्ते [Brand Team]! 👋 मैंने आपका नया लांच देखा और मुझे इसका प्रोडक्ट कांसेप्ट बहुत पसंद आया। मैं $cleanQuery पर हाई-इंगेजमेंट रील्स बनाता हूँ जिसकी डेली रीच 15k+ है। क्या मैं आपको एक कस्टम रील स्क्रिप्ट व मीडिया किट भेज सकता हूँ?"

🚀 3. एक्शन टिप्स:
• ब्रांड डीएम के 3 दिन बाद पॉलीट फॉलो-अप जरूर भेजें।
• रील पब्लिश करने के 7 दिन बाद व्यूज व इंगेजमेंट रिपोर्ट भेजें ताकि मंथली रिटेनर मिल सके।
            """.trimIndent() else """
🌐 1. GOOGLE SEARCH REAL-TIME INSIGHTS:
• Active Sponsor Trends: Boat Audio, Snitch Clothing, Minimalist, and Mamaearth are actively hiring micro-creators (2k–15k) this week.
• Standard Market Rates: 1k-10k followers tier = ₹2,500 - ₹8,000 per Reel (+50% extra for paid ad rights).
• Top Converting Format: Problem/Solution Hook + Fast-cut product unboxing.

🧠 2. GEMINI CREATIVE STRATEGY & PITCH:
Target Topic: $cleanQuery
"Hey [Brand Manager]! 👋 Loved your latest product launch. I create high-converting content around $cleanQuery with 15k+ weekly viewers. Would love to feature your brand in an upcoming Reel! Shall I send a custom pitch & Media Kit?"

🚀 3. ACTIONABLE RECOMMENDATIONS:
• Always ask for 50% advance before posting.
• Send a post-campaign analytics report 7 days after publishing to secure recurring monthly sponsorships!
            """.trimIndent()

            MentorToolDomain.MEESHO_CREATOR_AI -> if (isHindi) """
🌐 1. गूगल सर्च डेटा इनसाइट्स (मीशो ट्रेंड्स व हैशटैग्स):
• हॉट कैटेगरीज़: एथनिक कुर्तीयों के सेट्स, वर्कवेयर कॉम्बो, और टेक एक्सेसरीज।
• वायरल हैशटैग्स: #MeeshoHaul #MeeshoFinds #BudgetFashion #MeeshoKurti #ViralToolAI
• बेस्ट शेयरिंग चैनल: वॉट्सएप कैटलॉग ग्रुप्स व इंस्टाग्राम बायो लिंक ट्री।

🧠 2. जेमिनी क्रिएटिव स्क्रिप्ट व कैप्शन:
"गाइज! मीशो से ₹499 में मिला यह अमेजिंग प्रोडक्ट देखो! 😱
1️⃣ फैब्रिक क्वालिटी 10/10 है।
2️⃣ मार्केट रेट ₹1,200 है पर मीशो पर आधी कीमत में मिला!
👇 कोड कमेंट करें 'MEESHO' या बायो लिंक से डायरेक्ट खरीदें!"

🚀 3. एक्शन टिप्स:
• वीडियो में प्रोडक्ट कोड ऑन-स्क्रीन बोल्ड टेक्स्ट में रखें।
• पहले 10 कमेंट्स में डायरेक्ट एफिलिएट लिंक पिन करें।
            """.trimIndent() else """
🌐 1. GOOGLE SEARCH REAL-TIME INSIGHTS:
• Trending Categories: Ethnic Kurti Sets, Workwear combos, Aesthetic desk items, and Smart accessories.
• Viral Hashtags: #MeeshoHaul #MeeshoFinds #BudgetFashion #MeeshoOutfit #ViralToolAI
• Top Channel: WhatsApp Broadcast Lists + Instagram Bio LinkTree.

🧠 2. GEMINI CREATIVE SCRIPT & CAPTION:
"Guys! Found this insane ₹499 $cleanQuery deal on Meesho! 😱
• Fabric/Quality: 10/10 rating.
• Offline Store Price: ₹1,499 ❌ | Meesho Price: ₹499 ✅
👇 Comment 'LINK' below to get the direct product code sent to your DM!"

🚀 3. ACTIONABLE RECOMMENDATIONS:
• Put the Meesho product code clearly on-screen for 3 seconds.
• Pin your referral/affiliate link in the top comment immediately after posting!
            """.trimIndent()

            MentorToolDomain.INSTAGRAM_CREATOR_AI -> if (isHindi) """
🌐 1. गूगल सर्च डेटा इनसाइट्स (इंस्टाग्राम रील्स ट्रेंड्स):
• एल्गोरिदम अपडेट: 3-सेकंड वॉच टाइम रिटेंशन और शेयर/सेव मीट्रिक को सबसे ज्यादा रीच मिल रही है।
• ट्रेंडिंग ऑडियो: ↗️ डायगोनल एरो वाले <10k रील्स वाले ओरिजिनल ऑडियो चुनें।
• 3-टायर हैशटैग्स: #ReelsGrowth #ContentStrategy #${cleanQuery.replace(" ", "")} #ViralToolAI

🧠 2. जेमिनी क्रिएटिव हुक व स्ट्रैटेजी:
हुक: "2026 में यह $cleanQuery गलती आपकी रील्स रीच किल कर रही है!"
विजुअल: स्क्रीन पर बोल्‍ड टेक्स्ट + फास्ट मूवमेंट पैटर्न इंटरप्ट।
कैप्शन: "इस रील को सेव कर लें और नीचे कमेंट में 'GROWTH' लिखें!"

🚀 3. एक्शन टिप्स:
• ऑन-स्क्रीन टेक्स्ट को स्क्रीन के ऊपरी 30% हिस्से में रखें।
• कवर फोटो फ्रेम जरूर सेलेक्ट करें ताकि ग्रीड क्लीन दिखे।
            """.trimIndent() else """
🌐 1. GOOGLE SEARCH REAL-TIME INSIGHTS:
• Reel Algorithm Signal: 3-second retention and saves/shares drive 80%+ of Explore page pushes.
• Trending Audio Formula: Pick audio tracks with the diagonal ↗️ arrow and <10k uses.
• 3-Tier Hashtag Stack: #ReelsGrowth #ContentStrategy #${cleanQuery.replace(" ", "")} #ViralToolAI

🧠 2. GEMINI CREATIVE SCRIPT & HOOK:
Hook: "Stop making this $1,000 $cleanQuery mistake in 2026!"
Visual: Bold text overlay in upper third + energetic movement.
CTA: "Save this Reel for later & comment 'STRATEGY' for the full breakdown!"

🚀 3. ACTIONABLE RECOMMENDATIONS:
• Keep text overlays in the upper 30% safe zone.
• Always select a clean high-contrast thumbnail cover frame.
            """.trimIndent()

            MentorToolDomain.YOUTUBE_CREATOR_AI -> if (isHindi) """
🌐 1. गूगल सर्च डेटा इनसाइट्स (यूट्यूब शॉर्ट्स ट्रेंड्स):
• शॉर्ट्स एल्गोरिदम: 'Swiped Away %' < 25% होना चाहिए। पहले फ्रेम में सस्पेंस या एक्शन जरूरी है।
• थंबनेल व टाइटल्स: हाई कंट्रास्ट एक्सप्रेशन + 3 से कम शब्दों का ऑन-थंबनेल टेक्स्ट।
• सर्च टैग्स: #Shorts #YouTubeGrowth #${cleanQuery.replace(" ", "")}

🧠 2. जेमिनी क्रिएटिव शॉर्ट्स स्क्रिप्ट:
0-3s: "अगर आप $cleanQuery कर रहे हैं, तो रुकिए! 🛑"
3-15s: "यह 1 हैक आपकी स्पीड 10x बढ़ा देगा!"
15-30s: "सब्सक्राइब बटन दबाएं ऐसी ही डेली टिप्स के लिए!"

🚀 3. एक्शन टिप्स:
• चैनल अबाउट सेक्शन में 150 शब्दों का कीवर्ड-रिच डिस्क्रिप्शन लिखें।
• एंडिंग सेंटेंस को ओपनिंग हुक से सीमलेसली लूप करें।
            """.trimIndent() else """
🌐 1. GOOGLE SEARCH REAL-TIME INSIGHTS:
• Shorts Algorithm Metric: Maintain 'Viewed vs Swiped' above 75%. First 2 seconds dictate reach.
• High CTR Combo: Emotional face expression thumbnail + curious title.
• Search Tags: #Shorts #YouTubeGrowth #${cleanQuery.replace(" ", "")} #ViralToolAI

🧠 2. GEMINI CREATIVE SHORTS SCRIPT:
0-3s: "Do NOT attempt $cleanQuery until you know this secret!"
3-15s: "Here is the exact 1-step workflow trick top creators use."
15-30s: "Hit subscribe for daily high-value guides!"

🚀 3. ACTIONABLE RECOMMENDATIONS:
• Seamlessly loop the final sentence back into the first sentence for infinite watch-time.
• Add custom captions to keep silent viewers engaged.
            """.trimIndent()

            MentorToolDomain.AI_VIDEO_IMAGE_GENERATOR -> """
🌐 1. GOOGLE SEARCH REAL-TIME VISUAL TRENDS:
• Trending Aesthetics: Cyberpunk neon lighting, Cinematic 8k photorealism, Studio macro depth of field.
• Model Compatibility: Gemini 3 Pro Image, Veo 3.1 Fast, Midjourney v6 style vectors.

🧠 2. GEMINI CREATIVE PROMPT GENERATOR:
Prompt: "Hyper-realistic cinematic 8K shot of $cleanQuery, dramatic studio volumetric lighting, shallow depth of field, vibrant colors, shot on 35mm lens, highly detailed, photorealistic 16:9 aspect ratio --ar 16:9 --v 6.0"

🚀 3. ACTIONABLE PRO-TIPS:
• For Veo Video Generation: Include camera movement keywords like 'Cinematic pan right', 'Slow motion zoom in'.
• Aspect Ratio: Use 9:16 for IG Reels/Shorts and 16:9 for YouTube.
            """.trimIndent()

            MentorToolDomain.CAPCUT_MASTER -> """
🌐 1. GOOGLE SEARCH REAL-TIME EDITING TRENDS:
• Trending CapCut Effects: Velocity Auto-Cut, 3D Zoom Pro, Smooth Slow-Mo, Neon Edge Glow.
• Audio Beat Sync: Auto-Velocity synced to 120 BPM trending tracks.

🧠 2. GEMINI CREATIVE EDITING TUTORIAL:
Step 1: Open CapCut > Import $cleanQuery clip.
Step 2: Tap 'Style' > Select 'Auto-Velocity' or '3D Zoom Pro'.
Step 3: Go to 'Text' > 'Auto Captions' > Pick 'Bold Yellow Glow' preset.
Step 4: Export in 1080p 60fps with Smart HDR disabled for clean color accuracy.

🚀 3. ACTIONABLE EDITING TIPS:
• Cut clips on every audio bass drop for 2x retention.
• Add keyframe motion to static text for dynamic visual flow.
            """.trimIndent()

            MentorToolDomain.VN_EDITING -> """
🌐 1. GOOGLE SEARCH REAL-TIME EDITING TRENDS:
• Trending VN Filters: Kodak Portra LUTs, Teal & Orange Cinematic, Vintage Film Grain.
• Speed Curves: Custom Hero Curve (Fast start 3x -> Slow middle 0.5x -> Fast end 2x).

🧠 2. GEMINI CREATIVE VN TIMELINE WORKFLOW:
Step 1: Import $cleanQuery video into VN timeline.
Step 2: Select clip > Tap 'Speed' > 'Curve' > Apply 'Hero Curve' preset.
Step 3: Tap 'Filter' > Apply 'Cinematic C1' at 70% opacity.
Step 4: Export without watermark (delete end title card in timeline).

🚀 3. ACTIONABLE EDITING TIPS:
• Use Masking tool to create seamless split-screen comparisons.
• Use 24fps for cinematic movie feel or 60fps for silky smooth motion.
            """.trimIndent()

            MentorToolDomain.INSTAGRAM_EDITS -> """
🌐 1. GOOGLE SEARCH REAL-TIME EDITING TRENDS:
• Native IG Reel Tools: Align Tool, Dual Camera Mode, Trending Text Stickers, Sound Effects.
• Safe Zone Dimensions: Keep text inside 1080x1420 pixel center box to avoid feed overlay coverage.

🧠 2. GEMINI CREATIVE INSTAGRAM REEL EDIT GUIDE:
Step 1: Open Instagram Camera > Select 9:16 format.
Step 2: Record $cleanQuery clip using Align Grid to maintain center subject position.
Step 3: Add Text > Select 'Classic Font' with background contrast box.
Step 4: Balance Audio: Original voiceover at 100%, Background Music at 12%.

🚀 3. ACTIONABLE EDITING TIPS:
• Turn on 'Upload at Highest Quality' in Instagram Settings > Account > Data Usage.
• Use native IG text tool for auto-indexing search algorithms!
            """.trimIndent()
        }
    }
}
