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
fun BrandCollabLevel6AIOutreachMasterView(
    userNiche: String,
    userPlatform: String,
    userName: String,
    onLevel6Completed: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val savedData = remember { CreatorAcademyPrefs.getBrandCollabLevel6Data(context) }

    var currentStep by remember { mutableStateOf((savedData["step"] as? Int) ?: 1) }
    var selectedMethod by remember { mutableStateOf((savedData["method"] as? String)?.ifBlank { "Instagram DM" } ?: "Instagram DM") }
    var selectedBrandType by remember { mutableStateOf((savedData["brandType"] as? String)?.ifBlank { "Growing Brand" } ?: "Growing Brand") }
    var targetBrandNameInput by remember { mutableStateOf((savedData["brandName"] as? String)?.ifBlank { "Boat Audio" } ?: "Boat Audio") }
    val targetBrandName = targetBrandNameInput.ifBlank { "Boat Audio" }
    var websiteInput by remember { mutableStateOf((savedData["website"] as? String) ?: "") }
    var igHandleInput by remember { mutableStateOf((savedData["igHandle"] as? String) ?: "") }
    var selectedPurpose by remember { mutableStateOf((savedData["purpose"] as? String)?.ifBlank { "Paid Collaboration" } ?: "Paid Collaboration") }

    var dmStyleTone by remember { mutableStateOf("Professional") } // Friendly, Professional, Short, Medium, Premium, Luxury Style
    var followupDays by remember { mutableStateOf("3 Day") } // 24 Hour, 3 Day, 7 Day, 14 Day

    var simBrandScenario by remember { mutableStateOf("We don't have budget") }
    var simUserAnswerInput by remember { mutableStateOf("") }
    var simFeedbackScore by remember { mutableStateOf<Int?>(null) }
    var simSuggestedResponse by remember { mutableStateOf("") }

    var negChatInput by remember { mutableStateOf("") }
    val negChatLog = remember {
        mutableStateListOf(
            "Brand Manager" to "Hi! Thanks for reaching out. What are your commercial rates for a Reel integration?"
        )
    }

    // Generated Message States
    var customGeneratedMessage by remember { mutableStateOf((savedData["dmText"] as? String) ?: "") }
    var emailSubject by remember { mutableStateOf("Collaboration Proposal for $targetBrandName - High Engagement Creator") }

    fun buildEmailBody(): String {
        val name = userName.ifBlank { "Creator" }
        return """
Subject: $emailSubject

Dear $targetBrandName Partnerships Team,

I hope this message finds you well.

My name is $name, and I create $userNiche content on $userPlatform with a highly engaged audience interested in quality recommendations.

I've been closely following $targetBrandName's recent campaigns and believe a partnership would resonate strongly with my audience. 

Purpose: $selectedPurpose
Target Platform: $userPlatform
Key Value: Authentic story-driven content with measured engagement.

I have attached my media kit and rate card for your review. Would you be open to exploring a potential collaboration this month?

Best regards,
$name
Creator & Content Strategist
        """.trimIndent()
    }

    fun buildDmBody(): String {
        val name = userName.ifBlank { "Creator" }
        return when (dmStyleTone) {
            "Short" -> "Hey $targetBrandName team! 👋 $name here. Love your products! Would love to feature $targetBrandName in my upcoming $userNiche reel. Open for a $selectedPurpose deal?"
            "Friendly" -> "Hi $targetBrandName team! 😊 Huge fan of your brand! I'm $name, creating $userNiche content on $userPlatform. My audience loves authentic recommendations. Would love to collab on a $selectedPurpose campaign!"
            "Premium" -> "Greetings $targetBrandName Team. I'm $name, a $userNiche creator ($userPlatform). I am curating a high-converting campaign series and would love to partner with $targetBrandName for a $selectedPurpose."
            "Luxury Style" -> "Excellence meets aesthetics ✨ Dear $targetBrandName, I showcase luxury $userNiche concepts. I would love to craft a bespoke $selectedPurpose deliverable for your upcoming launch."
            else -> "Hello $targetBrandName Team, $name here. I create $userNiche content on $userPlatform. I've designed a content proposal for $selectedPurpose that aligns directly with your target audience. Let's connect!"
        }
    }

    fun buildFollowupBody(): String {
        return when (followupDays) {
            "24 Hour" -> "Hi $targetBrandName team, just bumping this up in case it got buried! Excited to hear your thoughts."
            "3 Day" -> "Hey $targetBrandName team! Following up on my proposal regarding $selectedPurpose. Let me know if you'd like me to send over my media kit!"
            "7 Day" -> "Hi team, I know you're super busy! Just checking in one last time on potential partnership opportunities for this month."
            else -> "Hello $targetBrandName team, reaching out with a quick check-in. We'd love to keep $targetBrandName in mind for our upcoming content calendar!"
        }
    }

    LaunchedEffect(targetBrandNameInput, selectedPurpose, dmStyleTone, currentStep) {
        if (customGeneratedMessage.isBlank()) {
            customGeneratedMessage = buildDmBody()
        }
    }

    fun persistState(completed: Boolean = false) {
        CreatorAcademyPrefs.saveBrandCollabLevel6State(
            context = context,
            step = currentStep,
            method = selectedMethod,
            brandType = selectedBrandType,
            brandName = targetBrandNameInput,
            website = websiteInput,
            igHandle = igHandleInput,
            purpose = selectedPurpose,
            emailText = buildEmailBody(),
            dmText = customGeneratedMessage,
            followupText = buildFollowupBody(),
            confidenceScore = 92,
            isCompleted = completed
        )
    }

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "bg_anim_l6")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val progressPercentage = (currentStep / 13f) * 100f

    val mentorMessages = remember(currentStep, selectedMethod, selectedBrandType) {
        when (currentStep) {
            1 -> "Ab sabse important part aata hai: Brand ko contact kaise karna hai! Select your preferred outreach method ($selectedMethod)."
            2 -> "Brand Type ($selectedBrandType) ke according tone shift hoti hai. Startup ko energetic DM, Luxury brand ko professional email bhejna chahiye."
            3 -> "Target Brand details enter karo. Official website aur Instagram username verify karne se contact accuracy improve hoti hai."
            4 -> "Clear Purpose ($selectedPurpose) set karo. Specific goal dene par brand manager ka decision making super fast ho jata hai."
            5 -> "AI-Generated Personalized Outreach Message! Har brand ke liye unique, natural human tone message ready hai."
            6 -> "Professional Email Generator: Complete email structure (Subject line, Value prop, Call-To-Action, Signature)."
            7 -> "Instagram DM Generator: Tone switchers (Friendly, Short, Premium, Luxury) to match brand vibes."
            8 -> "Polite Follow-up Generator: 24h, 3d, 7d, 14d non-desperate follow-up templates."
            9 -> "AI Conversation Simulator: Practice handling common brand objections like 'No Budget' or 'Send Rate Card'."
            10 -> "Interactive Negotiation Practice: Real-time chat simulation with Brand Manager."
            11 -> "Mistake Detector: Real-time scan for tone, length, pushiness and grammar flaws."
            12 -> "Confidence Score: Dynamic performance metrics on your communication mastery."
            13 -> "Today's Mission: Send Your First Professional Outreach! Claim +500 XP and unlocked badge."
            else -> "Master professional outreach and close lucrative brand deals!"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E293B),
                        Color(0xFF0A0F1D)
                    )
                )
            )
            .padding(16.dp)
    ) {
        // ================= HEADER & PROGRESS =================
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
                            Text("LEVEL 6", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI Outreach Master", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text("Reach Brands Like A Professional • Phase 7", fontSize = 12.sp, color = Color(0xFFFFD700).copy(alpha = 0.9f))
                }

                // Progress Ring
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0x33000000))
                        .border(2.dp, Color(0xFFFFD700).copy(alpha = pulseGlow), CircleShape)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${progressPercentage.toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                        Text("70% Target", fontSize = 7.sp, color = Color.White.copy(alpha = 0.7f))
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

        Spacer(modifier = Modifier.height(12.dp))

        // ================= STEP CONTENT AREA =================
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            when (currentStep) {
                // STEP 1: CHOOSE OUTREACH METHOD
                1 -> {
                    Text("Step 1: Choose Outreach Method", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Select your primary outreach channel:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val methods = listOf(
                        Triple("Instagram DM", "Best for D2C, Fashion, Beauty & Quick Responses", "📸"),
                        Triple("Professional Email", "Best for High-Budget National Brands & Agencies", "✉️"),
                        Triple("LinkedIn Message", "Direct line to Marketing Leads & PR Managers", "💼"),
                        Triple("Contact Form", "Official website submission for structured inquiries", "🌐"),
                        Triple("Creator Portal", "Verified brand agency platforms & influencer boards", "🚀")
                    )

                    methods.forEach { (method, desc, icon) ->
                        val isSel = selectedMethod == method
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSel) Color(0x33FFD700) else Color(0x15FFFFFF))
                                .border(1.5.dp, if (isSel) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                                .clickable {
                                    selectedMethod = method
                                    persistState()
                                }
                                .padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(icon, fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(method, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text(desc, fontSize = 11.sp, color = Color.White.copy(alpha = 0.65f))
                                }
                                if (isSel) Text("✓", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            }
                        }
                    }
                }

                // STEP 2: CHOOSE BRAND TYPE
                2 -> {
                    Text("Step 2: Choose Brand Type", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Select the category of the brand you are pitching:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val brandTypes = listOf(
                        "Startup" to "Agile team, friendly tone, fast decision making",
                        "Local Brand" to "Regional focus, community oriented, quick barter/cash deals",
                        "National Brand" to "Established Indian brand, structured PR budget",
                        "Luxury Brand" to "High aesthetic standards, premium voice, top tier pricing",
                        "International Brand" to "Global MNC, strict compliance, higher retainers",
                        "Agency" to "Influencer management agency holding multiple brand budgets"
                    )

                    brandTypes.forEach { (type, desc) ->
                        val isSel = selectedBrandType == type
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSel) Color(0x33FFD700) else Color(0x15FFFFFF))
                                .border(1.dp, if (isSel) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                                .clickable {
                                    selectedBrandType = type
                                    persistState()
                                }
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Text(type, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color(0xFFFFD700) else Color.White)
                                    if (isSel) Text("✓ Selected", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                }
                                Text(desc, fontSize = 11.sp, color = Color.White.copy(alpha = 0.65f))
                            }
                        }
                    }
                }

                // STEP 3: BRAND NAME & DETAILS
                3 -> {
                    Text("Step 3: Enter Target Brand Details", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Provide specific details to personalize the pitch:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Brand Name *", fontSize = 12.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = targetBrandNameInput,
                        onValueChange = {
                            targetBrandNameInput = it
                            persistState()
                        },
                        placeholder = { Text("e.g., Boat Audio / Mamaearth / Snitch", color = Color.White.copy(alpha = 0.35f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFD700),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Official Website (Optional)", fontSize = 12.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = websiteInput,
                        onValueChange = {
                            websiteInput = it
                            persistState()
                        },
                        placeholder = { Text("e.g., https://boataudio.com", color = Color.White.copy(alpha = 0.35f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFD700),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Instagram Username (Optional)", fontSize = 12.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = igHandleInput,
                        onValueChange = {
                            igHandleInput = it
                            persistState()
                        },
                        placeholder = { Text("e.g., @boat.nirvana", color = Color.White.copy(alpha = 0.35f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFD700),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }

                // STEP 4: PURPOSE
                4 -> {
                    Text("Step 4: Collaboration Purpose", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Specify what deal structure you want to pitch:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val purposes = listOf(
                        "Paid Collaboration" to "Fixed fee for Reel/Post deliverable",
                        "Barter" to "Product seeding for unboxing/review",
                        "Affiliate" to "Performance link & sales commission",
                        "UGC" to "Ad creative for brand's social ads",
                        "Long-Term Partnership" to "3-month recurring content package",
                        "Brand Ambassador" to "Exclusive monthly face of brand"
                    )

                    purposes.forEach { (purpose, desc) ->
                        val isSel = selectedPurpose == purpose
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSel) Color(0x33FFD700) else Color(0x15FFFFFF))
                                .border(1.dp, if (isSel) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                                .clickable {
                                    selectedPurpose = purpose
                                    persistState()
                                }
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Text(purpose, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color(0xFFFFD700) else Color.White)
                                    if (isSel) Text("✓ Selected", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                }
                                Text(desc, fontSize = 11.sp, color = Color.White.copy(alpha = 0.65f))
                            }
                        }
                    }
                }

                // STEP 5: AI GENERATED OUTREACH MESSAGE
                5 -> {
                    Text("Step 5: AI Outreach Message", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Personalized outreach draft generated for $targetBrandName:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x221E293B))
                            .border(1.5.dp, Color(0xFFFFD700), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("⚡ AI Personalized Pitch", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                Button(
                                    onClick = {
                                        customGeneratedMessage = buildDmBody()
                                        persistState()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFD700)),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Re-Generate 🔄", fontSize = 10.sp, color = Color(0xFFFFD700))
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = customGeneratedMessage,
                                onValueChange = {
                                    customGeneratedMessage = it
                                    persistState()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFFFD700),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )
                        }
                    }
                }

                // STEP 6: PROFESSIONAL EMAIL GENERATOR
                6 -> {
                    Text("Step 6: Professional Email Generator", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Complete structured email ready to send:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val fullEmail = buildEmailBody()

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x221E293B))
                            .border(1.dp, Color(0x44FFD700), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text("📧 Full Email Draft", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = fullEmail,
                                fontSize = 11.5.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                // STEP 7: INSTAGRAM DM GENERATOR
                7 -> {
                    Text("Step 7: Instagram DM Tone Switcher", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Select tone style to customize your DM:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val tones = listOf("Professional", "Friendly", "Short", "Premium", "Luxury Style")
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tones.forEach { tone ->
                            val isSel = dmStyleTone == tone
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                    .clickable {
                                        dmStyleTone = tone
                                        customGeneratedMessage = buildDmBody()
                                        persistState()
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(tone, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x221E293B))
                            .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("📸 Generated DM ($dmStyleTone Tone)", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(customGeneratedMessage, fontSize = 12.sp, color = Color.White)
                        }
                    }
                }

                // STEP 8: FOLLOW-UP GENERATOR
                8 -> {
                    Text("Step 8: Follow-up Generator", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Non-desperate follow-up templates for unresponsive brands:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val intervals = listOf("24 Hour", "3 Day", "7 Day", "14 Day")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        intervals.forEach { interval ->
                            val isSel = followupDays == interval
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 2.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                    .clickable {
                                        followupDays = interval
                                        persistState()
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(interval, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                            }
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
                            Text("📩 Follow-Up Template ($followupDays Interval)", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(buildFollowupBody(), fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                        }
                    }
                }

                // STEP 9: AI CONVERSATION SIMULATOR
                9 -> {
                    Text("Step 9: AI Objection Simulator", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Practice responding to common brand manager objections:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val scenarios = listOf("We don't have budget", "Send your media kit", "We'll contact later", "What's your pricing?")
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        scenarios.forEach { sc ->
                            val isSel = simBrandScenario == sc
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                    .clickable {
                                        simBrandScenario = sc
                                        simFeedbackScore = null
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(sc, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                            }
                        }
                    }

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
                            Text("💬 Brand Says: \"$simBrandScenario\"", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = simUserAnswerInput,
                                onValueChange = { simUserAnswerInput = it },
                                placeholder = { Text("Enter your response to the brand...", color = Color.White.copy(alpha = 0.35f), fontSize = 11.sp) },
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
                                    simFeedbackScore = 88
                                    simSuggestedResponse = when (simBrandScenario) {
                                        "We don't have budget" -> "Suggest a performance barter or affiliate structure with a performance bonus once sales target is hit!"
                                        "Send your media kit" -> "Attach your PDF rate card with engagement stats, reach graph, and past brand work links immediately."
                                        "What's your pricing?" -> "Share your tiered pricing (Single Reel vs. Package) with clear deliverable timelines and ROI highlights."
                                        else -> "Ask for a follow-up date and offer a custom concept proposal tailored for their next launch."
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Evaluate Answer with AI 🤖", fontWeight = FontWeight.Bold, color = Color.Black)
                            }

                            if (simFeedbackScore != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("Score: $simFeedbackScore/100 (Excellent Tone)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00FF88))
                                Text("AI Tip: $simSuggestedResponse", fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f))
                            }
                        }
                    }
                }

                // STEP 10: NEGOTIATION PRACTICE
                10 -> {
                    Text("Step 10: Interactive Negotiation Practice", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Roleplay with an AI Brand Manager in real-time:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
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
                                negChatLog.forEach { (sender, msg) ->
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
                                    value = negChatInput,
                                    onValueChange = { negChatInput = it },
                                    placeholder = { Text("Type reply to brand...", fontSize = 10.sp, color = Color.White.copy(alpha = 0.35f)) },
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
                                        if (negChatInput.isNotBlank()) {
                                            negChatLog.add("You" to negChatInput.trim())
                                            val reply = "Brand Manager: Sounds interesting! Let's schedule a call or send over the formal invoice agreement."
                                            negChatLog.add("Brand Manager" to reply)
                                            negChatInput = ""
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

                // STEP 11: MISTAKE DETECTOR
                11 -> {
                    Text("Step 11: Outreach Mistake Detector", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Automated quality audit of your current pitch:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val checks = listOf(
                        Triple("Length Check", "Optimal (Under 100 words for DM)", Color(0xFF00FF88)),
                        Triple("Pushiness Score", "Balanced & Respectful Tone", Color(0xFF00FF88)),
                        Triple("Grammar & Clarity", "No spelling errors detected", Color(0xFF00FF88)),
                        Triple("Call-To-Action", "Clear question included", Color(0xFFFFD700))
                    )

                    checks.forEach { (title, status, color) ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x221E293B))
                                .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("✔ $title", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text(status, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
                            }
                        }
                    }
                }

                // STEP 12: CONFIDENCE SCORE
                12 -> {
                    Text("Step 12: Outreach Confidence Score", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Overall communication and brand attraction ratings:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
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
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x33000000))
                                    .border(3.dp, Color(0xFFFFD700), CircleShape)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("92%", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                    Text("CONFIDENCE", fontSize = 8.sp, color = Color.White.copy(alpha = 0.6f))
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                MetricBox("Professional Score", "94/100", Color(0xFF00FF88), Modifier.weight(1f))
                                Spacer(modifier = Modifier.width(6.dp))
                                MetricBox("Communication", "90/100", Color(0xFFFFD700), Modifier.weight(1f))
                                Spacer(modifier = Modifier.width(6.dp))
                                MetricBox("Attraction Score", "92/100", Color(0xFF38BDF8), Modifier.weight(1f))
                            }
                        }
                    }
                }

                // STEP 13: TODAY'S MISSION & ACHIEVEMENT
                13 -> {
                    Text("Step 13: Today's Mission & Reward", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Finalize Level 6 and claim your achievement:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x44FFD700),
                                        Color(0x151E293B)
                                    )
                                )
                            )
                            .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🏆 ACHIEVEMENT UNLOCKED", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Professional Outreach Ready", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            Text("+500 XP Earned • Master Outreach Specialist", fontSize = 12.sp, color = Color(0xFF00FF88))

                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x22000000))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text("🎯 Today's Mission Complete:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    Text("• Generated personalized email & DM for $targetBrandName", fontSize = 11.5.sp, color = Color.White)
                                    Text("• Mastered 4 follow-up strategies & objection handling", fontSize = 11.5.sp, color = Color.White)
                                    Text("• Completed outreach quality audit with 92% confidence", fontSize = 11.5.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ================= BOTTOM NAVIGATION BUTTONS =================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentStep > 1) {
                OutlinedButton(
                    onClick = {
                        currentStep--
                        persistState()
                    },
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(0.45f)
                ) {
                    Text("‹ Back", color = Color.White)
                }
            } else {
                OutlinedButton(
                    onClick = onBack,
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(0.45f)
                ) {
                    Text("‹ Back", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    if (currentStep < 13) {
                        currentStep++
                        persistState()
                    } else {
                        persistState(completed = true)
                        onLevel6Completed()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (currentStep < 13) "Continue ›" else "Finish Level 6 ✨",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}
