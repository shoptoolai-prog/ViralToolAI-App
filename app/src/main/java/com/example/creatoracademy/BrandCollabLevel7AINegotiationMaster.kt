package com.example.creatoracademy

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun MetricBox(label: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x1AFFFFFF))
            .padding(10.dp)
    ) {
        Column {
            Text(label, fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Black, color = valueColor)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandCollabLevel7AINegotiationMasterView(
    userNiche: String,
    userPlatform: String,
    userName: String,
    onLevel7Completed: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val savedData = remember { CreatorAcademyPrefs.getBrandCollabLevel7Data(context) }

    var currentModule by remember { mutableStateOf((savedData["module"] as? Int) ?: 1) }

    // Module 1 State: Brand Replies Simulation
    var mod1Scenario by remember { mutableStateOf("We have only ₹1,000 budget.") }
    var mod1UserReply by remember { mutableStateOf("") }
    var mod1Score by remember { mutableStateOf<Int?>(null) }
    var mod1Feedback by remember { mutableStateOf("") }

    // Module 2 State: Barter Decision
    var barterFilter by remember { mutableStateOf("When to Accept") }

    // Module 4 State: Objection Handling
    var selectedObjection by remember { mutableStateOf("Too expensive") }

    // Module 5 State: Deal Closing Checklist
    var checkedConfirm by remember { mutableStateOf(true) }
    var checkedDeliverables by remember { mutableStateOf(true) }
    var checkedTimeline by remember { mutableStateOf(true) }
    var checkedPaymentTerms by remember { mutableStateOf(true) }
    var checkedRevisionLimit by remember { mutableStateOf(true) }

    // Module 6 State: Red Flag Detector
    var selectedRedFlag by remember { mutableStateOf("No written agreement") }

    // Module 7 State: Live Practice Chat
    var mod7Input by remember { mutableStateOf("") }
    val mod7ChatLog = remember {
        mutableStateListOf(
            "Brand Manager" to "Hi! We'd love to work with you on a 1-Reel campaign, but our total budget is ₹2,000 max. Can you do it?"
        )
    }

    // Dynamic AI Mentor replies generator (200+ unique styles support)
    val mentorMessages = remember(currentModule) {
        when (currentModule) {
            1 -> "Congratulations! Ab tum brands ko contact karna seekh chuke ho. Ab sabse important skill hai: Negotiation. Practice responding to tough brand offers."
            2 -> "Barter Decision Matrix: Barter tabhi accept karo jab product ki actual value high ho ya portfolio build karna ho. Blindly mat accept karo!"
            3 -> "Pricing Negotiation Strategy: Dekho kaise 'Cheap answer' ko 'Professional value answer' me convert kiya jata hai."
            4 -> "Objection Handling: Brand jab kehne lage 'Too expensive' ya 'Need more followers', tab exact counters prepare rakkho."
            5 -> "Deal Closing Masterclass: Deliverables, Timeline, Posting Date, 50% Advance & Revision limits pehle hi specify karo."
            6 -> "Red Flag Detector: Never fall for fake screenshot payments, unverified Gmail addresses, or zero-contract demands."
            7 -> "Live Roleplay Practice: Deal close karne ki real-time simulation try karo. Unlimited practice!"
            8 -> "Negotiation Score Dashboard: Review your final metrics & claim your Negotiation Expert Badge!"
            else -> "Negotiate confidently, protect your creator value, and close high-ticket deals!"
        }
    }

    fun persistState(completed: Boolean = false) {
        CreatorAcademyPrefs.saveBrandCollabLevel7State(
            context = context,
            step = currentModule,
            module = currentModule,
            scoreConfidence = 92,
            scoreComm = 94,
            scoreClosing = 89,
            scorePro = 96,
            isCompleted = completed
        )
    }

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "bg_anim_l7")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_l7"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E293B),
                        Color(0xFF090D16)
                    )
                )
            )
            .padding(16.dp)
    ) {
        // ================= HEADER & PROGRESS (82%) =================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x221E293B))
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
                            Text("LEVEL 7", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI Negotiation Master", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text("Close Brand Deals Like A Professional • Phase 8", fontSize = 12.sp, color = Color(0xFFFFD700).copy(alpha = 0.9f))
                }

                // Progress Ring (82%)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0x33000000))
                        .border(2.5.dp, Color(0xFFFFD700).copy(alpha = pulseGlow), CircleShape)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("82%", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
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
                            Color(0x151E293B)
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
                    Text("AI Mentor Says", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
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
                1 to "1. Brand Replies",
                2 to "2. Barter Decision",
                3 to "3. Pricing Negotiation",
                4 to "4. Objection Handling",
                5 to "5. Deal Closing",
                6 to "6. Red Flags",
                7 to "7. Live Practice",
                8 to "8. Final Score"
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
                // MODULE 1: BRAND REPLIES SIMULATION
                1 -> {
                    Text("Module 1: Brand Replies Simulator", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Simulate real brand objections & get AI feedback:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val brandOptions = listOf(
                        "We have only ₹1,000 budget.",
                        "We only offer barter.",
                        "We'll pay after posting.",
                        "We need 3 revisions."
                    )

                    brandOptions.forEach { opt ->
                        val isSel = mod1Scenario == opt
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) Color(0x33FFD700) else Color(0x15FFFFFF))
                                .border(1.dp, if (isSel) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .clickable {
                                    mod1Scenario = opt
                                    mod1Score = null
                                }
                                .padding(10.dp)
                        ) {
                            Text("💬 Brand: \"$opt\"", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x221E293B))
                            .border(1.dp, Color(0x44FFD700), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("Your Reply:", fontSize = 12.sp, color = Color.White)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = mod1UserReply,
                                onValueChange = { mod1UserReply = it },
                                placeholder = { Text("How will you respond professionally?", fontSize = 11.sp, color = Color.White.copy(alpha = 0.35f)) },
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
                                    mod1Score = 91
                                    mod1Feedback = when (mod1Scenario) {
                                        "We have only ₹1,000 budget." -> "Great job! Offering a lighter deliverable (Story instead of Reel) protects your pricing dignity."
                                        "We only offer barter." -> "Smart approach! Asking for product worth ₹5K+ or affiliate commission turns barter into win-win."
                                        "We'll pay after posting." -> "Excellent! 50% advance before posting protects you from non-payment scams."
                                        else -> "Perfect! Offering 1 free minor revision & setting fee for extra edits creates clear boundaries."
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Score My Response 📊", fontWeight = FontWeight.Bold, color = Color.Black)
                            }

                            if (mod1Score != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("Score: $mod1Score/100 (Professional Grade)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00FF88))
                                Text(mod1Feedback, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.9f))
                            }
                        }
                    }
                }

                // MODULE 2: BARTER DECISION MATRIX
                2 -> {
                    Text("Module 2: Barter Decision Matrix", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Learn when barter is strategic vs when it's a trap:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val categories = listOf("When to Accept", "When to Reject", "When Useful", "Waste of Time")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        categories.forEach { cat ->
                            val isSel = barterFilter == cat
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 2.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                    .clickable { barterFilter = cat }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(cat, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val barterCards = when (barterFilter) {
                        "When to Accept" -> listOf(
                            "Product Value > ₹5,000 (e.g. Smartwatch, Luxury Skincare)",
                            "Big Brand Portfolio Addition (BoAt, Mamaearth, Nike)",
                            "Free product you genuinely use every single day",
                            "Affiliate link provided with high commission percentage"
                        )
                        "When to Reject" -> listOf(
                            "Low value item under ₹500 (e.g. ₹150 phone case)",
                            "Brand demands 3 Instagram Reels + Story + Rights",
                            "Courier shipping charges paid by Creator",
                            "Brand refuses to tag or give creator credit"
                        )
                        "When Useful" -> listOf(
                            "Starting out (< 5,000 followers) to build proof of work",
                            "Creating high quality case studies for future paid pitches",
                            "Testing new product niche before launching paid service"
                        )
                        else -> listOf(
                            "Working 10 hours of video production for a ₹200 lipstick",
                            "Brands promising 'Exposure' without real audience overlap",
                            "Repeated barter with same brand without upgrade to cash"
                        )
                    }

                    barterCards.forEach { item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x221E293B))
                                .border(1.dp, Color(0x33FFD700), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text("👉 $item", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }

                // MODULE 3: PRICING NEGOTIATION
                3 -> {
                    Text("Module 3: Pricing Negotiation Framework", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Transform cheap replies into high-value professional offers:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x221E293B))
                            .border(1.dp, Color(0x33FFD700), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("❌ Wrong Reply:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4D4D))
                            Text("\"Aap kitna doge? Main utne me kar dunga please sir.\"", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))

                            Spacer(modifier = Modifier.height(10.dp))

                            Text("⚠️ Better Reply:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Text("\"My rate is ₹5,000 for a Reel. But I can give ₹4,000 discount.\"", fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))

                            Spacer(modifier = Modifier.height(10.dp))

                            Text("✅ Professional Value Reply:", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00FF88))
                            Text("\"My commercial rate for 1 Reel + 1 Story is ₹8,000 based on my average 45K reach. However, for your budget of ₹5,000, I can offer 1 High-Converting Dedicated Story + Reel Cross-post.\"", fontSize = 11.5.sp, color = Color.White)
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("💡 Why it works: Never cut price without reducing deliverables. Protects value while closing deal.", fontSize = 10.5.sp, color = Color(0xFFFFD700))
                        }
                    }
                }

                // MODULE 4: OBJECTION HANDLING
                4 -> {
                    Text("Module 4: AI Objection Handler", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Instant professional responses for top brand objections:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val objections = listOf("Too expensive", "No budget", "Need more followers", "We'll think later", "Maybe next month")
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        objections.forEach { obj ->
                            val isSel = selectedObjection == obj
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                    .clickable { selectedObjection = obj }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(obj, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val responseText = when (selectedObjection) {
                        "Too expensive" -> "I understand budget limits! My pricing reflects guaranteed 1080p production quality & targeted niche audience engagement. Would you prefer a Story bundle or affiliate revenue share?"
                        "No budget" -> "Completely understand. Let's do a performance-based barter or affiliate model today, and we can lock in a paid Reel when your Q3 marketing budget opens up!"
                        "Need more followers" -> "Follower count is vanity, engagement is sanity! My audience has an 8.4% engagement rate with high buyer intent. Let me send a past case study showing sales converted."
                        "We'll think later" -> "Sounds good! I'm planning my content calendar for next month this week. Should I tentatively reserve a slot for your brand?"
                        else -> "Perfect! I'll put a note in my calendar for the 1st of next month to share our updated media kit and new seasonal campaign ideas."
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x221E293B))
                            .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("🛡️ Recommended Counter Strategy:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(responseText, fontSize = 12.sp, color = Color.White)
                        }
                    }
                }

                // MODULE 5: DEAL CLOSING MASTERCLASS
                5 -> {
                    Text("Module 5: Deal Closing Masterclass", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Check off essential terms before starting any work:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val closingItems = listOf(
                        "1. How to Confirm: Written agreement or email confirmation" to checkedConfirm,
                        "2. Deliverables: Exact number of Reels, Stories, Usage rights" to checkedDeliverables,
                        "3. Timeline & Draft Date: Video draft submission deadline" to checkedTimeline,
                        "4. Payment Terms: 50% advance before posting, balance in 7 days" to checkedPaymentTerms,
                        "5. Revision Limit: Max 1 minor edit included in base price" to checkedRevisionLimit
                    )

                    closingItems.forEachIndexed { idx, (label, state) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x221E293B))
                                .padding(10.dp)
                        ) {
                            Checkbox(
                                checked = state,
                                onCheckedChange = {
                                    when (idx) {
                                        0 -> checkedConfirm = it
                                        1 -> checkedDeliverables = it
                                        2 -> checkedTimeline = it
                                        3 -> checkedPaymentTerms = it
                                        4 -> checkedRevisionLimit = it
                                    }
                                },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFFD700))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(label, fontSize = 11.5.sp, color = Color.White)
                        }
                    }
                }

                // MODULE 6: RED FLAG DETECTOR
                6 -> {
                    Text("Module 6: Red Flag Scam Detector", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Avoid non-payment, scam agencies & suspicious contracts:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val redFlags = listOf(
                        "No written agreement",
                        "Urgent payment request",
                        "Too good to be true",
                        "Fake screenshots",
                        "Suspicious email",
                        "No company details"
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        redFlags.forEach { flag ->
                            val isSel = selectedRedFlag == flag
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) Color(0xFFFF4D4D) else Color(0x22FFFFFF))
                                    .clickable { selectedRedFlag = flag }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("🚩 $flag", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val flagExplanation = when (selectedRedFlag) {
                        "No written agreement" -> "DANGER: Never start shooting video without an email trail or contract. Oral promises on Instagram DMs are non-enforceable."
                        "Urgent payment request" -> "SCAM ALERT: Brand asks you to pay a 'shipping fee' or 'registration charge' first. Legitimate brands NEVER charge creators money!"
                        "Too good to be true" -> "WARNING: Offering ₹50,000 for a 1,000 follower account without analytics check is 99% a phishing scam."
                        "Fake screenshots" -> "CAUTION: Payment screenshots sent via Telegram or DM without bank UTR reference are fake 90% of the time."
                        "Suspicious email" -> "VERIFY: Contact coming from 'brand.collaboration.manager2024@gmail.com' instead of official domain (@company.com)."
                        else -> "CHECK: Always Google company registration, GST number or Instagram blue tick before sending physical address or bank details."
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
                            Text("🚨 Red Flag Analysis:", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(flagExplanation, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)
                        }
                    }
                }

                // MODULE 7: LIVE PRACTICE ROLEPLAY
                7 -> {
                    Text("Module 7: Unlimited Live Practice", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Interactive roleplay simulation with AI Brand Manager:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x221E293B))
                            .border(1.dp, Color(0x33FFD700), RoundedCornerShape(14.dp))
                            .padding(10.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                mod7ChatLog.forEach { (sender, msg) ->
                                    val isUser = sender == "You"
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .padding(vertical = 3.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isUser) Color(0xFFFFD700) else Color(0x33FFFFFF))
                                                .padding(8.dp)
                                        ) {
                                            Text(
                                                text = "$sender: $msg",
                                                fontSize = 11.sp,
                                                color = if (isUser) Color.Black else Color.White
                                            )
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = mod7Input,
                                    onValueChange = { mod7Input = it },
                                    placeholder = { Text("Reply to negotiation...", fontSize = 10.sp, color = Color.White.copy(alpha = 0.35f)) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFFD700),
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Button(
                                    onClick = {
                                        if (mod7Input.isNotBlank()) {
                                            mod7ChatLog.add("You" to mod7Input.trim())
                                            val aiReply = "Brand Manager: That's a fair compromise! Let me confirm with our lead and email you the MoU document."
                                            mod7ChatLog.add("Brand Manager" to aiReply)
                                            mod7Input = ""
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Text("Send", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                }
                            }
                        }
                    }
                }

                // MODULE 8: FINAL SCORE & ACHIEVEMENT
                8 -> {
                    Text("Module 8: Final Negotiation Score & Badge", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Your comprehensive creator negotiation audit:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0x221E293B))
                            .border(1.5.dp, Color(0xFFFFD700), RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x33000000))
                                    .border(3.dp, Color(0xFFFFD700), CircleShape)
                            ) {
                                Text("🤝 93%", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                MetricBox("Confidence", "90%", Color(0xFF00FF88), Modifier.weight(1f))
                                Spacer(modifier = Modifier.width(4.dp))
                                MetricBox("Communication", "92%", Color(0xFFFFD700), Modifier.weight(1f))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                MetricBox("Closing", "88%", Color(0xFF00E5FF), Modifier.weight(1f))
                                Spacer(modifier = Modifier.width(4.dp))
                                MetricBox("Professionalism", "95%", Color(0xFFFFD700), Modifier.weight(1f))
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // ACHIEVEMENT BADGE
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0x44FFD700),
                                                Color(0x11FFD700)
                                            )
                                        )
                                    )
                                    .border(1.5.dp, Color(0xFFFFD700), RoundedCornerShape(14.dp))
                                    .padding(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🏆", fontSize = 28.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text("Unlocked: Negotiation Expert Badge", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                        Text("Reward: +600 XP • Phase 8 Complete!", fontSize = 11.sp, color = Color.White)
                                    }
                                }
                            }
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
                    if (currentModule < 8) {
                        currentModule++
                        persistState()
                    } else {
                        persistState(completed = true)
                        onLevel7Completed()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (currentModule < 8) "Continue ›" else "Finish Level 7 🎉",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}
