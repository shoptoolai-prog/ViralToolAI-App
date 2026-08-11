package com.example.creatoracademy

import com.example.reports.ReportLanguage

data class CardLocalizedTexts(
    val title: String,
    val working: String,
    val fix: String,
    val action: String,
    val timestamp: String = "00:01"
)

object ReportLocalizationHelper {

    fun getOverallScoreLabel(score: Int, lang: ReportLanguage): String = when (lang) {
        ReportLanguage.HINDI -> when {
            score >= 85 -> "उत्कृष्ट रील"
            score >= 72 -> "अच्छी रील"
            score >= 55 -> "औसत रील"
            else -> "सुधार की आवश्यकता"
        }
        ReportLanguage.HINGLISH -> when {
            score >= 85 -> "Strong Viral Reel"
            score >= 72 -> "Good Reel"
            score >= 55 -> "Moderate Reel"
            else -> "Needs Tuning"
        }
        else -> when {
            score >= 85 -> "Strong Reel"
            score >= 72 -> "Good Reel"
            score >= 55 -> "Moderate Reel"
            else -> "Needs Tuning"
        }
    }

    fun getOverallScoreSubtitle(score: Int, lang: ReportLanguage): String = when (lang) {
        ReportLanguage.HINDI -> when {
            score >= 85 -> "मजबूत शुरुआत और बेहतरीन विजुअल क्वालिटी।"
            score >= 72 -> "अच्छा फाउंडेशन, लेकिन ओपनिंग और पेसिंग में सुधार किया जा सकता है।"
            score >= 55 -> "ओपनिंग हुक और कट रिदम पर ध्यान देने की जरूरत है।"
            else -> "हुक टाइमिंग और विजुअल क्लैरिटी को बेहतर बनाएं।"
        }
        ReportLanguage.HINGLISH -> when {
            score >= 85 -> "Strong foundation, high attention retention."
            score >= 72 -> "Good foundation, but opening and pacing can improve."
            score >= 55 -> "Hook attention & edit rhythm need optimization."
            else -> "Improve opening delay & visual clarity for better reach."
        }
        else -> when {
            score >= 85 -> "Strong foundation with solid visual presentation."
            score >= 72 -> "Strong foundation, but opening and pacing can improve."
            score >= 55 -> "Hook timing and cut rhythm need tuning."
            else -> "Focus on opening speed and visual clarity."
        }
    }

    fun getCardTitle(cardIndex: Int, lang: ReportLanguage): String = when (cardIndex) {
        0 -> when (lang) {
            ReportLanguage.HINDI -> "आपका रील स्कोर"
            ReportLanguage.HINGLISH -> "YOUR REEL SCORE"
            else -> "YOUR REEL SCORE"
        }
        1 -> when (lang) {
            ReportLanguage.HINDI -> "सर्वश्रेष्ठ 3 थंबनेल"
            ReportLanguage.HINGLISH -> "BEST 3 THUMBNAILS"
            else -> "BEST 3 THUMBNAILS"
        }
        2 -> when (lang) {
            ReportLanguage.HINDI -> "ओपनिंग हुक और अटेंशन"
            ReportLanguage.HINGLISH -> "OPENING HOOK & ATTENTION"
            else -> "OPENING HOOK & ATTENTION"
        }
        3 -> when (lang) {
            ReportLanguage.HINDI -> "विजुअल क्वालिटी"
            ReportLanguage.HINGLISH -> "VISUAL QUALITY"
            else -> "VISUAL QUALITY"
        }
        4 -> when (lang) {
            ReportLanguage.HINDI -> "ऑडियो और स्पीच"
            ReportLanguage.HINGLISH -> "AUDIO & SPEECH"
            else -> "AUDIO & SPEECH"
        }
        5 -> when (lang) {
            ReportLanguage.HINDI -> "पेसिंग और कट रिदम"
            ReportLanguage.HINGLISH -> "PACING & CUT RHYTHM"
            else -> "PACING & CUT RHYTHM"
        }
        6 -> when (lang) {
            ReportLanguage.HINDI -> "स्टोरी और नैरेटिव फ्लो"
            ReportLanguage.HINGLISH -> "STORY & NARRATIVE FLOW"
            else -> "STORY & NARRATIVE FLOW"
        }
        7 -> when (lang) {
            ReportLanguage.HINDI -> "ऑन-स्क्रीन टेक्स्ट और कैप्शंस"
            ReportLanguage.HINGLISH -> "ON-SCREEN TEXT & CAPTIONS"
            else -> "ON-SCREEN TEXT & CAPTIONS"
        }
        8 -> when (lang) {
            ReportLanguage.HINDI -> "एंगेजमेंट और वायरल पोटेंशियल"
            ReportLanguage.HINGLISH -> "ENGAGEMENT & VIRAL POTENTIAL"
            else -> "ENGAGEMENT & VIRAL POTENTIAL"
        }
        9 -> when (lang) {
            ReportLanguage.HINDI -> "ऑडियंस और कंटेंट फिट"
            ReportLanguage.HINGLISH -> "AUDIENCE & CONTENT FIT"
            else -> "AUDIENCE & CONTENT FIT"
        }
        10 -> when (lang) {
            ReportLanguage.HINDI -> "3 सबसे महत्वपूर्ण बदलाव"
            ReportLanguage.HINGLISH -> "3 CHANGES THAT MATTER MOST"
            else -> "3 CHANGES THAT MATTER MOST"
        }
        else -> "REPORT"
    }

    fun getSectionHeader(key: String, lang: ReportLanguage): String = when (key.uppercase()) {
        "WHATS_WORKING" -> when (lang) {
            ReportLanguage.HINDI -> "क्या अच्छा काम कर रहा है"
            ReportLanguage.HINGLISH -> "WHAT'S WORKING"
            else -> "WHAT'S WORKING"
        }
        "WHAT_TO_FIX" -> when (lang) {
            ReportLanguage.HINDI -> "क्या ठीक करने की जरूरत है"
            ReportLanguage.HINGLISH -> "WHAT TO FIX"
            else -> "WHAT TO FIX"
        }
        "ACTION", "ONE_QUICK_ACTION" -> when (lang) {
            ReportLanguage.HINDI -> "एक त्वरित कार्रवाई"
            ReportLanguage.HINGLISH -> "ONE QUICK ACTION"
            else -> "ONE QUICK ACTION"
        }
        "EVIDENCE" -> when (lang) {
            ReportLanguage.HINDI -> "प्रमाण (एविडेंस)"
            ReportLanguage.HINGLISH -> "EVIDENCE FRAME"
            else -> "EVIDENCE FRAME"
        }
        "CATEGORY_SCORES" -> when (lang) {
            ReportLanguage.HINDI -> "कैटेगरी स्कोर"
            ReportLanguage.HINGLISH -> "CATEGORY SCORES"
            else -> "CATEGORY SCORES"
        }
        "KEY_SIGNALS" -> when (lang) {
            ReportLanguage.HINDI -> "मुख्य संकेत"
            ReportLanguage.HINGLISH -> "KEY SIGNALS"
            else -> "KEY SIGNALS"
        }
        else -> key
    }

    fun getCardDetails(cardIndex: Int, lang: ReportLanguage): CardLocalizedTexts = when (cardIndex) {
        2 -> when (lang) { // HOOK
            ReportLanguage.HINDI -> CardLocalizedTexts(
                title = "ओपनिंग हुक और अटेंशन",
                working = "ओपनिंग फ्रेम में मुख्य विषय स्पष्ट रूप से दिखाई दे रहा है।",
                fix = "शुरुआती विजुअल मूवमेंट को गति पकड़ने में 0.8 सेकंड का समय लगता है।",
                action = "एक्शन से ठीक पहले की 0.8 सेकंड की निष्क्रियता को ट्रिम करें।",
                timestamp = "00:01"
            )
            ReportLanguage.HINGLISH -> CardLocalizedTexts(
                title = "OPENING HOOK & ATTENTION",
                working = "Opening frame mein main subject bilkul clear hai.",
                fix = "Opening visual movement ko accelerate hone mein 0.8s lagta hai.",
                action = "Initial 0.8s delay ko trim karke action ke sath start karein.",
                timestamp = "00:01"
            )
            else -> CardLocalizedTexts(
                title = "OPENING HOOK & ATTENTION",
                working = "Visual subject clearly present in opening frame.",
                fix = "Opening visual movement takes 0.8s to accelerate.",
                action = "Trim initial 0.8s delay to start right at movement.",
                timestamp = "00:01"
            )
        }
        3 -> when (lang) { // VISUAL
            ReportLanguage.HINDI -> CardLocalizedTexts(
                title = "विजुअल क्वालिटी",
                working = "संतुलित की-लाइट और मुख्य विषय का हाई-कॉन्ट्रास्ट प्रेजेंटेशन।",
                fix = "ऊपरी कोने में हल्की शैडो और एक्सपोज़र में मामूली उतार-चढ़ाव।",
                action = "बेहतर क्लैरिटी के लिए मिड-टोन एक्सपोज़र को +10% बढ़ाएं।",
                timestamp = "00:04"
            )
            ReportLanguage.HINGLISH -> CardLocalizedTexts(
                title = "VISUAL QUALITY",
                working = "Balanced key fill luminance with high frame contrast.",
                fix = "Top corner mein light shadow aur exposure variance hai.",
                action = "Optimal contrast ke liye mid-tone exposure +10% boost karein.",
                timestamp = "00:04"
            )
            else -> CardLocalizedTexts(
                title = "VISUAL QUALITY",
                working = "Balanced key fill luminance with high frame contrast.",
                fix = "Minor background shadow near top corner.",
                action = "Boost mid-tone exposure by +10% for optimal contrast.",
                timestamp = "00:04"
            )
        }
        4 -> when (lang) { // AUDIO
            ReportLanguage.HINDI -> CardLocalizedTexts(
                title = "ऑडियो और स्पीच",
                working = "साफ वोकल फ्रीक्वेंसी के साथ वोकल ट्रैक डिटेक्ट हुआ।",
                fix = "आवाज का स्तर बैकग्राउंड म्यूज़िक ट्रैक के बहुत करीब है।",
                action = "म्यूज़िक की तुलना में वोकल ट्रैक को +3dB बूस्ट करें।",
                timestamp = "00:03"
            )
            ReportLanguage.HINGLISH -> CardLocalizedTexts(
                title = "AUDIO & SPEECH",
                working = "Clean vocal frequencies ke sath voice track detected.",
                fix = "Voice level background music ke bohot paas hai.",
                action = "Music ke comparison mein vocal track ko +3dB boost karein.",
                timestamp = "00:03"
            )
            else -> CardLocalizedTexts(
                title = "AUDIO & SPEECH",
                working = "Audible speech track detected with clean vocal frequencies.",
                fix = "Voice level is close to background audio track level.",
                action = "Boost vocal track +3dB relative to music.",
                timestamp = "00:03"
            )
        }
        5 -> when (lang) { // PACING
            ReportLanguage.HINDI -> CardLocalizedTexts(
                title = "पेसिंग और कट रिदम",
                working = "टाइमलाइन पर सीन्स के बदलाव काफी सुसंगत हैं।",
                fix = "मध्य भाग में एक जगह फ्रेम 3.5 सेकंड से अधिक समय तक स्थिर रहता है।",
                action = "फोकस रिफ्रेश करने के लिए 00:05 पर एक पंच-इन ज़ूम कट लगाएं।",
                timestamp = "00:05"
            )
            ReportLanguage.HINGLISH -> CardLocalizedTexts(
                title = "PACING & CUT RHYTHM",
                working = "Timeline par scene changes consistent hain.",
                fix = "Mid-point par static shot 3.5 seconds se zyada hold karta hai.",
                action = "Focus refresh karne ke liye 00:05 par subtle punch-in zoom cut lagayein.",
                timestamp = "00:05"
            )
            else -> CardLocalizedTexts(
                title = "PACING & CUT RHYTHM",
                working = "Scene changes are mostly consistent across the timeline.",
                fix = "Static shot holds for longer than 3.5 seconds at mid-point.",
                action = "Insert a subtle punch-in zoom cut at 00:05 to refresh focus.",
                timestamp = "00:05"
            )
        }
        6 -> when (lang) { // STORY
            ReportLanguage.HINDI -> CardLocalizedTexts(
                title = "स्टोरी और नैरेटिव फ्लो",
                working = "रील में पहले विषय को पेश किया गया है, फिर मुख्य वैल्यू दिखाई गई है।",
                fix = "मुख्य सीख या निष्कर्ष अंतिम हिस्से में देर से आता है।",
                action = "पहले 3 सेकंड में ही मुख्य टॉपिक या हुक बता दें।",
                timestamp = "00:02"
            )
            ReportLanguage.HINGLISH -> CardLocalizedTexts(
                title = "STORY & NARRATIVE FLOW",
                working = "Reel pehle subject introduce karti hai phir value deliver karti hai.",
                fix = "Core takeaway final section mein late aata hai.",
                action = "First 3 seconds mein hi main topic ya hook state karein.",
                timestamp = "00:02"
            )
            else -> CardLocalizedTexts(
                title = "STORY & NARRATIVE FLOW",
                working = "The reel presents the subject first, then demonstrates core value.",
                fix = "Core takeaway occurs late in the final section.",
                action = "State the primary topic or hook in the first 3 seconds.",
                timestamp = "00:02"
            )
        }
        7 -> when (lang) { // TEXT
            ReportLanguage.HINDI -> CardLocalizedTexts(
                title = "ऑन-स्क्रीन टेक्स्ट और कैप्शंस",
                working = "लोअर थर्ड एरिया टेक्स्ट प्लेसमेंट के लिए साफ और स्पष्ट है।",
                fix = "शुरुआती 2 सेकंड में कोई हाई-कॉन्ट्रास्ट एनिमेटेड टेक्स्ट ओवरले नहीं है।",
                action = "डार्क बैकग्राउंड पिल के साथ सेंटर में ऑटो-कैप्शंस जोड़ें।",
                timestamp = "00:02"
            )
            ReportLanguage.HINGLISH -> CardLocalizedTexts(
                title = "ON-SCREEN TEXT & CAPTIONS",
                working = "Lower third area clean hai text placement ke liye.",
                fix = "First 2 seconds mein koi high-contrast text overlay nahi hai.",
                action = "Dark background pill ke sath center mein auto-captions add karein.",
                timestamp = "00:02"
            )
            else -> CardLocalizedTexts(
                title = "ON-SCREEN TEXT & CAPTIONS",
                working = "Lower third area clean and uncluttered for text placement.",
                fix = "No high-contrast animated text overlay in the first 2 seconds.",
                action = "Add auto-captions in middle-center with dark background pill.",
                timestamp = "00:02"
            )
        }
        8 -> when (lang) { // ENGAGEMENT
            ReportLanguage.HINDI -> CardLocalizedTexts(
                title = "एंगेजमेंट और वायरल पोटेंशियल",
                working = "विजुअल क्लैरिटी और हुक अटेंशन का अच्छा संतुलन।",
                fix = "रिटेंशन बनाए रखने के लिए विजुअल पैटर्न-इंटरप्ट की कमी।",
                action = "रिटेंशन बढ़ाने के लिए 00:05 पर एक विजुअल एलिमेंट जोड़ें।",
                timestamp = "00:05"
            )
            ReportLanguage.HINGLISH -> CardLocalizedTexts(
                title = "ENGAGEMENT & VIRAL POTENTIAL",
                working = "Hook attention aur visual subject clarity ka accha mix hai.",
                fix = "Retention hold karne ke liye visual pattern interrupt missing hai.",
                action = "Audience retention boost karne ke liye 00:05 par visual graphic add karein.",
                timestamp = "00:05"
            )
            else -> CardLocalizedTexts(
                title = "ENGAGEMENT & VIRAL POTENTIAL",
                working = "Good balance of visual clarity and hook attention.",
                fix = "Lacks visual pattern-interrupt to hold audience retention.",
                action = "Add a visual pattern-interrupt graphic at 00:05 to boost retention.",
                timestamp = "00:05"
            )
        }
        else -> CardLocalizedTexts(
            title = "ANALYSIS",
            working = "Clear visual structure.",
            fix = "Minor optimizations possible.",
            action = "Apply key recommendations."
        )
    }

    fun get3Changes(lang: ReportLanguage): List<String> = when (lang) {
        ReportLanguage.HINDI -> listOf(
            "01 • ओपनिंग मजबूत करें: एक्शन से पहले की 0.8s की निष्क्रियता को काटें।",
            "02 • फ्रेमिंग सुधारें: मुख्य सीक्वेंस के दौरान सब्जेक्ट को सेंटर में अलाइन करें।",
            "03 • ऑटो-कैप्शंस जोड़ें: लोअर थर्ड में हाई-कॉन्ट्रास्ट टेक्स्ट ओवरले लगाएं।"
        )
        ReportLanguage.HINGLISH -> listOf(
            "01 • Opening strong karein: Action se pehle ka 0.8s dead time trim karein.",
            "02 • Subject focus improve karein: Main sequence mein center framing adjust karein.",
            "03 • Auto-captions add karein: Lower third mein high-contrast text overlay lagayein."
        )
        else -> listOf(
            "01 • Strengthen opening: trim initial dead time before action (at 00:00.8).",
            "02 • Improve subject focus: adjust crop and center framing during main sequence.",
            "03 • Add auto-captions: place high-contrast text overlay in lower third."
        )
    }

    fun getButtonText(key: String, lang: ReportLanguage): String = when (key.uppercase()) {
        "DONE" -> when (lang) {
            ReportLanguage.HINDI -> "पूर्ण"
            ReportLanguage.HINGLISH -> "DONE"
            else -> "DONE"
        }
        "NEXT" -> when (lang) {
            ReportLanguage.HINDI -> "आगे"
            ReportLanguage.HINGLISH -> "Next"
            else -> "Next"
        }
        "BACK" -> when (lang) {
            ReportLanguage.HINDI -> "पीछे"
            ReportLanguage.HINGLISH -> "Back"
            else -> "Back"
        }
        "USE_FRAME" -> when (lang) {
            ReportLanguage.HINDI -> "इसे कवर बनाएं"
            ReportLanguage.HINGLISH -> "Use this frame"
            else -> "Use this frame"
        }
        "SELECTED_COVER" -> when (lang) {
            ReportLanguage.HINDI -> "✓ चयनित कवर"
            ReportLanguage.HINGLISH -> "✓ Selected Cover"
            else -> "✓ Selected Cover"
        }
        "BEST_PICK" -> when (lang) {
            ReportLanguage.HINDI -> "सर्वश्रेष्ठ चुनाव"
            ReportLanguage.HINGLISH -> "BEST PICK"
            else -> "BEST PICK"
        }
        else -> key
    }

    fun getStatusText(status: ContentTypeMatchStatus = ContentTypeMatchStatus.MATCHED, lang: ReportLanguage): String = when (lang) {
        ReportLanguage.HINDI -> when (status) {
            ContentTypeMatchStatus.MATCHED -> "मैच हुआ"
            ContentTypeMatchStatus.PARTIALLY_MATCHED -> "आंशिक मैच"
            ContentTypeMatchStatus.NOT_MATCHED -> "मैच नहीं हुआ"
            ContentTypeMatchStatus.UNCERTAIN -> "अनिश्चित"
        }
        ReportLanguage.HINGLISH -> when (status) {
            ContentTypeMatchStatus.MATCHED -> "MATCHED"
            ContentTypeMatchStatus.PARTIALLY_MATCHED -> "PARTIAL MATCH"
            ContentTypeMatchStatus.NOT_MATCHED -> "MISMATCH"
            ContentTypeMatchStatus.UNCERTAIN -> "UNCERTAIN"
        }
        else -> when (status) {
            ContentTypeMatchStatus.MATCHED -> "MATCHED"
            ContentTypeMatchStatus.PARTIALLY_MATCHED -> "PARTIAL MATCH"
            ContentTypeMatchStatus.NOT_MATCHED -> "MISMATCH"
            ContentTypeMatchStatus.UNCERTAIN -> "UNCERTAIN"
        }
    }
}
