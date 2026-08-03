package com.example.creatoracademy

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================================
// PHASE 9: LEVEL 8 - AI CONTRACT & LEGAL GUIDE
// ============================================================================

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandCollabLevel8AIContractLegalGuideView(
    userNiche: String,
    userPlatform: String,
    userName: String,
    onLevel8Completed: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val savedData = remember { CreatorAcademyPrefs.getBrandCollabLevel8Data(context) }

    val initialModule = (savedData["module"] as? Number)?.toInt() ?: 1
    var currentModule by remember { mutableStateOf(initialModule) }

    // Module 2 State: Contract Sections
    var selectedSection by remember { mutableStateOf("Scope of Work") }

    // Module 3 State: Payment Clauses
    var selectedPaymentClause by remember { mutableStateOf("50/50 Payment") }

    // Module 4 State: Usage Rights
    var selectedUsageRight by remember { mutableStateOf("Paid Ads") }

    // Module 5 State: Red Flag Detector
    var selectedRedFlag by remember { mutableStateOf("Unlimited revisions") }

    // Module 6 State: Fake Contract Simulator
    val spottedErrors = remember { mutableStateListOf<String>() }

    // Module 7 State: Contract Checklist
    var checkBrandName by remember { mutableStateOf(true) }
    var checkDeliverables by remember { mutableStateOf(true) }
    var checkTimeline by remember { mutableStateOf(true) }
    var checkPayment by remember { mutableStateOf(true) }
    var checkRights by remember { mutableStateOf(true) }
    var checkRevisions by remember { mutableStateOf(true) }
    var checkContact by remember { mutableStateOf(true) }
    var checkSignature by remember { mutableStateOf(true) }

    // Module 8 State: AI Contract Explainer
    var userClauseText by remember { mutableStateOf("Creator grants Brand perpetual, exclusive, worldwide rights to edit, modify and run paid advertisements on all social channels without additional compensation.") }
    var selectedLanguage by remember { mutableStateOf((savedData["explainLang"] as? String) ?: "Hinglish") }
    var clauseExplanation by remember { mutableStateOf<String?>(null) }

    // Module 9 State: Scenario Practice
    var userScenarioReply by remember { mutableStateOf("") }
    var scenarioFeedback by remember { mutableStateOf<String?>(null) }

    fun persistState(completed: Boolean = false) {
        CreatorAcademyPrefs.saveBrandCollabLevel8State(
            context = context,
            step = currentModule,
            module = currentModule,
            explainLang = selectedLanguage,
            isCompleted = completed
        )
    }

    // Dynamic AI Mentor message
    val mentorMessages = remember(currentModule) {
        when (currentModule) {
            1 -> "Professional creators sirf deal close nahi karte... Woh contract padhkar apne rights bhi protect karte hain. Aaj main tumhe real-world creator contracts samjhaunga."
            2 -> "Contract Sections: Har contract me Scope, Deliverables, Timeline, Revisions & Payment Terms clear honi chahiye."
            3 -> "Payment Clauses: 50% advance before posting is the safest industry standard for Indian creators!"
            4 -> "Usage Rights: Organic vs Paid Ads usage me farak samjho. Paid Ads or Whitelisting ke extra charges hote hain."
            5 -> "Red Flag Detector: Never accept 'Lifetime usage rights' or 'Unlimited revisions' without extra compensation."
            6 -> "Fake Contract Simulator: Is sample contract me 3 dangerous traps hain. Kya tum inhe dhund sakte ho?"
            7 -> "Contract Checklist: Video shoot shuru karne se pehle ye 8 items tick hona ZARURI hain."
            8 -> "AI Contract Explainer: Kisi bhi complex legal clause ko Hindi, English ya Hinglish me instant simple language me samjho."
            9 -> "Scenario Practice: Brand agar unlimited rights mange bina payment ke, toh professional counter-clause kaise bhejen?"
            10 -> "Creator Safety Rules: Lock in these 5 golden safety rules & claim your Contract Smart Creator Badge!"
            else -> "Always protect your creative rights with a written contract!"
        }
    }

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "bg_anim_l8")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_l8"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0B132B),
                        Color(0xFF1C2541),
                        Color(0xFF090D16)
                    )
                )
            )
            .padding(16.dp)
    ) {
        // ================= HEADER & PROGRESS (88%) =================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x221C2541))
                .border(1.dp, Color(0x33FFD700), RoundedCornerShape(20.dp))
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFD700))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("LEVEL 8", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI Contract & Legal Guide", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text("Protect Yourself Before Every Brand Deal • Phase 9", fontSize = 11.5.sp, color = Color(0xFFFFD700).copy(alpha = 0.9f))
                }

                // Progress Ring (88%)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0x33000000))
                        .border(2.5.dp, Color(0xFFFFD700).copy(alpha = pulseGlow), CircleShape)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("88%", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                        Text("Progress", fontSize = 7.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ================= AI MENTOR BANNER =================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0x33FFD700),
                            Color(0x151C2541)
                        )
                    )
                )
                .border(1.dp, Color(0x44FFD700), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFD700))
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🤖", fontSize = 22.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text("AI Legal Mentor", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text(
                        text = mentorMessages,
                        fontSize = 11.5.sp,
                        color = Color.White.copy(alpha = 0.95f),
                        lineHeight = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ================= MODULE TABS =================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val modules = listOf(
                1 to "1. Contract Basics",
                2 to "2. Key Sections",
                3 to "3. Payment Clauses",
                4 to "4. Usage Rights",
                5 to "5. Red Flags",
                6 to "6. Fake Contract",
                7 to "7. Checklist",
                8 to "8. AI Explainer",
                9 to "9. Scenario",
                10 to "10. Safety Rules"
            )

            modules.forEach { (mNum, mName) ->
                val isSel = currentModule == mNum
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                        .clickable {
                            currentModule = mNum
                            persistState()
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = mName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) Color.Black else Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ================= MODULE CONTENT AREA =================
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            when (currentModule) {
                // MODULE 1: WHAT IS A CREATOR CONTRACT?
                1 -> {
                    Text("Module 1: What Is A Creator Contract?", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Simple explanation without legal jargon:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x221C2541))
                            .border(1.dp, Color(0x44FFD700), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text("📜 Creator Contract Kya Hota Hai?", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Creator Contract ek written legal document (ya confirmation email) hota hai jisme Creator aur Brand ke beech saari baatein clearly defined hoti hain.",
                                fontSize = 11.5.sp,
                                color = Color.White,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text("❌ WhatsApp Verbal Promise vs ✅ Written Contract:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x33FF4D4D))
                                    .padding(10.dp)
                            ) {
                                Text("❌ WhatsApp Chat: Brand: 'Bhai Reel daal do, paise agle hafte de denge.' Result: Payment delay or ghosting.", fontSize = 11.sp, color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x3300FF88))
                                    .padding(10.dp)
                            ) {
                                Text("✅ Written Contract: 50% advance before posting + 50% balance in 7 days. Legally binding protection.", fontSize = 11.sp, color = Color.White)
                            }
                        }
                    }
                }

                // MODULE 2: CONTRACT SECTIONS (GLASS CARDS)
                2 -> {
                    Text("Module 2: Essential Contract Sections", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Tap each section to view key details:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val sections = listOf(
                        "Scope of Work", "Deliverables", "Timeline", "Revisions",
                        "Payment Terms", "Usage Rights", "Termination", "Confidentiality"
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        sections.forEach { sec ->
                            val isSel = selectedSection == sec
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                    .clickable { selectedSection = sec }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(sec, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val sectionDetail = when (selectedSection) {
                        "Scope of Work" -> "Defines the exact campaign goal, target audience, brand messaging guidelines & product focus."
                        "Deliverables" -> "Specifies exact numbers: e.g. 1 Instagram Reel (60s) + 2 Instagram Stories with swipe-up link."
                        "Timeline" -> "Clear dates for: Product Delivery, First Draft Submission, Brand Review, and Final Live Posting Date."
                        "Revisions" -> "Limits edits: Includes 1 free minor edit (text/music). Additional reshoots incur +30% fee."
                        "Payment Terms" -> "50% Advance upon contract signing + 50% remaining balance within 7 days post publication."
                        "Usage Rights" -> "Organic social media usage for 30 days. Paid ads / Whitelisting required extra commercial license."
                        "Termination" -> "Either party can cancel with 48 hours notice. Creator keeps advance if work already commenced."
                        else -> "Protects unreleased products, campaign launching dates & confidential brand strategy data."
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x221C2541))
                            .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("📄 $selectedSection Clause:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(sectionDetail, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                        }
                    }
                }

                // MODULE 3: PAYMENT CLAUSES
                3 -> {
                    Text("Module 3: Payment Clauses & Safest Options", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Understand different payment structures:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val paymentOptions = listOf("Advance Payment", "50/50 Payment", "100% After Delivery", "Escrow", "Milestones")

                    paymentOptions.forEach { opt ->
                        val isSel = selectedPaymentClause == opt
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) Color(0x33FFD700) else Color(0x15FFFFFF))
                                .border(1.dp, if (isSel) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .clickable { selectedPaymentClause = opt }
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(if (opt == "50/50 Payment") "⭐ $opt (Recommended)" else "💳 $opt", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val payDetail = when (selectedPaymentClause) {
                        "Advance Payment" -> "100% payment upfront before shooting. Ideal for celebrity creators or high production cost campaigns."
                        "50/50 Payment" -> "SAFEST CHOICE: 50% advance before video creation + 50% balance before or immediately after posting."
                        "100% After Delivery" -> "RISKY: Creator shoots & posts video first, then waits 30-90 days for payment. High ghosting risk!"
                        "Escrow" -> "Third-party holds funds safely until deliverables are approved. Great for working with new international brands."
                        else -> "Payment split into stages: 30% Script, 40% Video Draft, 30% Live Posting."
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x221C2541))
                            .border(1.dp, Color(0xFF00FF88), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("💡 Safety Analysis:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00FF88))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(payDetail, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                        }
                    }
                }

                // MODULE 4: USAGE RIGHTS
                4 -> {
                    Text("Module 4: Commercial Usage Rights", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Learn how brands use your video content:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val rights = listOf("Organic Usage", "Paid Ads", "Whitelisting", "UGC Rights", "Exclusivity", "Buyout")

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        rights.forEach { r ->
                            val isSel = selectedUsageRight == r
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                    .clickable { selectedUsageRight = r }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(r, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val rightsText = when (selectedUsageRight) {
                        "Organic Usage" -> "Standard: Video stays on Creator's social profile naturally without ad boost. Included in base fee."
                        "Paid Ads" -> "Brand runs your video as Meta/Google ad for 30-90 days. Charge +50% to +100% extra usage fee!"
                        "Whitelisting" -> "Brand gets access to run ads directly through your Instagram page (@yourhandle). Charge premium rate."
                        "UGC Rights" -> "Brand posts your video on THEIR official website/social accounts without posting on your profile."
                        "Exclusivity" -> "You cannot promote competing brands (e.g. Samsung vs iPhone) for 30-90 days. Charge +40% extra."
                        else -> "FULL BUYOUT: Brand owns video forever for TV, Billboards & Ads. Charge 3x - 5x base creator rate!"
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x221C2541))
                            .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("🏷️ $selectedUsageRight Breakdown:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(rightsText, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                        }
                    }
                }

                // MODULE 5: RED FLAG DETECTOR
                5 -> {
                    Text("Module 5: Dangerous Legal Red Flags", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Identify toxic contract clauses before signing:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val redFlags = listOf(
                        "Unlimited revisions",
                        "No payment timeline",
                        "Lifetime usage rights",
                        "No written agreement",
                        "No contact details",
                        "Urgent pressure tactics"
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        redFlags.forEach { rf ->
                            val isSel = selectedRedFlag == rf
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) Color(0xFFFF4D4D) else Color(0x22FFFFFF))
                                    .clickable { selectedRedFlag = rf }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("🚨 $rf", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val redFlagReason = when (selectedRedFlag) {
                        "Unlimited revisions" -> "WHY RISKY: Brand can force you to re-shoot video 10 times for free! Always set limit to 1 minor revision."
                        "No payment timeline" -> "WHY RISKY: Without 'Net 15 days' or '7 days post publication', brand can legally delay payment for 1 year."
                        "Lifetime usage rights" -> "WHY RISKY: Brand uses your face in paid ads for 10 years without paying you single extra rupee."
                        "No written agreement" -> "WHY RISKY: Zero legal proof if brand refuses to pay or steals raw video files."
                        "No contact details" -> "WHY RISKY: Brand uses fake agency email or WhatsApp number without registered office address."
                        else -> "WHY RISKY: 'Sign in 30 mins or offer expires!' is classic high-pressure scamming tactic."
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x33FF4D4D))
                            .border(1.dp, Color(0xFFFF4D4D), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("🚩 Red Flag Risk Explanation:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(redFlagReason, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                        }
                    }
                }

                // MODULE 6: FAKE CONTRACT SIMULATOR
                6 -> {
                    Text("Module 6: Fake Contract Trap Simulator", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Tap clauses in this sample contract to spot dangerous traps:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF0F172A))
                            .border(1.5.dp, Color(0x66FFD700), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("📜 SAMPLE BRAND AGREEMENT Snippet", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(8.dp))

                            // Trap 1
                            val isT1Spotted = spottedErrors.contains("TRAP_1")
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isT1Spotted) Color(0x44FF4D4D) else Color(0x22FFFFFF))
                                    .border(1.dp, if (isT1Spotted) Color(0xFFFF4D4D) else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable {
                                        if (!isT1Spotted) spottedErrors.add("TRAP_1")
                                    }
                                    .padding(8.dp)
                            ) {
                                Text("Clause 3.1: Creator grants Brand exclusive perpetual lifetime global rights to run paid ads on TV & Meta.", fontSize = 11.sp, color = Color.White)
                            }

                            // Trap 2
                            val isT2Spotted = spottedErrors.contains("TRAP_2")
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isT2Spotted) Color(0x44FF4D4D) else Color(0x22FFFFFF))
                                    .border(1.dp, if (isT2Spotted) Color(0xFFFF4D4D) else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable {
                                        if (!isT2Spotted) spottedErrors.add("TRAP_2")
                                    }
                                    .padding(8.dp)
                            ) {
                                Text("Clause 5.2: Payment of ₹2,000 shall be disbursed 120 days post campaign performance audit.", fontSize = 11.sp, color = Color.White)
                            }

                            // Trap 3
                            val isT3Spotted = spottedErrors.contains("TRAP_3")
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isT3Spotted) Color(0x44FF4D4D) else Color(0x22FFFFFF))
                                    .border(1.dp, if (isT3Spotted) Color(0xFFFF4D4D) else Color.Transparent, RoundedCornerShape(8.dp))
                                    .clickable {
                                        if (!isT3Spotted) spottedErrors.add("TRAP_3")
                                    }
                                    .padding(8.dp)
                            ) {
                                Text("Clause 7.4: Creator must perform unlimited reshoots until Brand internal committee gives 100% satisfaction.", fontSize = 11.sp, color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Traps Spotted: ${spottedErrors.size}/3", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))

                            if (spottedErrors.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("✅ Great eye! You caught: ${if (isT1Spotted) "Lifetime Buyout " else ""}${if (isT2Spotted) "120-Day Payment Delay " else ""}${if (isT3Spotted) "Unlimited Revisions" else ""}", fontSize = 11.sp, color = Color(0xFF00FF88))
                            }
                        }
                    }
                }

                // MODULE 7: CONTRACT CHECKLIST
                7 -> {
                    Text("Module 7: Must-Have Contract Checklist", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Ensure all 8 essential parameters are present before signing:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val items = listOf(
                        "1. Registered Brand Name & GST Details" to checkBrandName,
                        "2. Exact Deliverables (Reels, Stories, Formats)" to checkDeliverables,
                        "3. Submission & Live Posting Timeline" to checkTimeline,
                        "4. Payment Terms (50% Advance + Balance Date)" to checkPayment,
                        "5. Commercial Usage Rights Duration (30-90 days)" to checkRights,
                        "6. Revision Limit (1 free minor edit)" to checkRevisions,
                        "7. Official Contact Email / Designation" to checkContact,
                        "8. Authorized Signatures / Digital Consent" to checkSignature
                    )

                    items.forEachIndexed { idx, (label, state) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x221C2541))
                                .padding(8.dp)
                        ) {
                            Checkbox(
                                checked = state,
                                onCheckedChange = {
                                    when (idx) {
                                        0 -> checkBrandName = it
                                        1 -> checkDeliverables = it
                                        2 -> checkTimeline = it
                                        3 -> checkPayment = it
                                        4 -> checkRights = it
                                        5 -> checkRevisions = it
                                        6 -> checkContact = it
                                        7 -> checkSignature = it
                                    }
                                },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFFD700))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(label, fontSize = 11.5.sp, color = Color.White)
                        }
                    }
                }

                // MODULE 8: AI CONTRACT EXPLAINER
                8 -> {
                    Text("Module 8: AI Contract Clause Explainer", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Paste any complex contract clause to simplify it:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Hinglish", "Hindi", "English").forEach { lang ->
                            val isSel = selectedLanguage == lang
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                    .clickable {
                                        selectedLanguage = lang
                                        persistState()
                                    }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(lang, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = userClauseText,
                        onValueChange = { userClauseText = it },
                        placeholder = { Text("Paste legal clause here...", fontSize = 11.sp, color = Color.White.copy(alpha = 0.35f)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFD700),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            clauseExplanation = when (selectedLanguage) {
                                "Hindi" -> "सरल अर्थ: ब्रांड आपका वीडियो हमेशा के लिए मुफ्त में विज्ञापनों में इस्तेमाल करना चाहता है। यह नुकसानदेह है, इसके अतिरिक्त पैसे मांगें।"
                                "English" -> "Simple Meaning: The brand is claiming perpetual ad usage rights without extra pay. Request a 90-day cap or extra licensing fee."
                                else -> "Simple Meaning: Brand aapka video hamesha ke liye paid ads me chalana chahta hai bina extra paise diye. Isko 90-days cap karke extra usage fee charge karo!"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Explain Clause In Simple $selectedLanguage 🪄", fontWeight = FontWeight.Bold, color = Color.Black)
                    }

                    if (clauseExplanation != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x3300FF88))
                                .padding(10.dp)
                        ) {
                            Text(clauseExplanation!!, fontSize = 11.5.sp, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "⚠️ Legal Disclaimer: AI provides general educational guidance and cannot offer legal certainty. Consult a qualified attorney for high-stakes agreements.",
                        fontSize = 9.5.sp,
                        color = Color.White.copy(alpha = 0.5f),
                        lineHeight = 12.sp
                    )
                }

                // MODULE 9: SCENARIO PRACTICE
                9 -> {
                    Text("Module 9: Real Scenario Practice", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Scenario: Brand demands unlimited global usage rights without extra compensation.", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x221C2541))
                            .border(1.dp, Color(0x44FFD700), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("How will you respond professionally in contract negotiations?", fontSize = 11.5.sp, color = Color.White)
                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = userScenarioReply,
                                onValueChange = { userScenarioReply = it },
                                placeholder = { Text("Write your counter clause response...", fontSize = 11.sp, color = Color.White.copy(alpha = 0.35f)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFFFD700),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    scenarioFeedback = "✅ Professional Counter Clause: 'Base rate includes 30-day organic social media posting. For 1-year global paid ad usage rights, an additional commercial license fee of +50% applies.'"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Evaluate My Reply 🎯", fontWeight = FontWeight.Bold, color = Color.Black)
                            }

                            if (scenarioFeedback != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(scenarioFeedback!!, fontSize = 11.5.sp, color = Color(0xFF00FF88))
                            }
                        }
                    }
                }

                // MODULE 10: CREATOR SAFETY RULES & FINAL BADGE
                10 -> {
                    Text("Module 10: Creator Safety Golden Rules", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("5 rules every smart creator must live by:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val rules = listOf(
                        "1. Never start video shoot without signed agreement or 50% advance.",
                        "2. Keep written proof of all brand approvals on email.",
                        "3. Save official invoices & track payment deadlines.",
                        "4. Don't share sensitive bank OTPs or passwords unnecessarily.",
                        "5. Always verify brand GST number & official domain identity."
                    )

                    rules.forEach { r ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x221C2541))
                                .border(1.dp, Color(0x33FFD700), RoundedCornerShape(12.dp))
                                .padding(10.dp)
                        ) {
                            Text("🛡️ $r", fontSize = 11.5.sp, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // ACHIEVEMENT BADGE
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0x44FFD700),
                                        Color(0x11FFD700)
                                    )
                                )
                            )
                            .border(1.5.dp, Color(0xFFFFD700), RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🏆 ACHIEVEMENT UNLOCKED", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Contract Smart Creator", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Reward: +700 XP • Phase 9 Complete!", fontSize = 12.sp, color = Color(0xFF00FF88))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ================= BOTTOM BUTTON BAR =================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    if (currentModule > 1) {
                        currentModule--
                        persistState()
                    } else {
                        onBack()
                    }
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("‹ Back", color = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    if (currentModule < 10) {
                        currentModule++
                        persistState()
                    } else {
                        persistState(completed = true)
                        onLevel8Completed()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (currentModule < 10) "Continue ›" else "Finish Level 8 🎉",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}
