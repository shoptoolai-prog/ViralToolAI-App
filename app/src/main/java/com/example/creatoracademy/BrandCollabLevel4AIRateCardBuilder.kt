package com.example.creatoracademy

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun RateCardGlassCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x221E293B))
            .border(1.dp, Color(0x33FFD700), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun RateCardTextField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
        Spacer(modifier = Modifier.height(2.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 11.sp, color = Color.White.copy(alpha = 0.35f)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFD700),
                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = true
        )
    }
}

@Composable
private fun PricingRow(title: String, price: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x22FFFFFF))
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 12.sp, color = Color.White)
        Text(price.ifBlank { "N/A" }, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
    }
}

@Composable
private fun MetricBox(label: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x22FFFFFF))
            .padding(10.dp)
    ) {
        Column {
            Text(label, fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = valueColor)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandCollabLevel4AIRateCardView(
    userNiche: String,
    userPlatform: String,
    onLevel4Completed: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current

    // Saved state loading
    val savedData = remember { CreatorAcademyPrefs.getBrandCollabLevel4Data(context) }
    val level3Data = remember { CreatorAcademyPrefs.getBrandCollabLevel3Data(context) }
    val profileData = remember { CreatorAcademyPrefs.getBrandCollabProfileData(context) }

    var currentStep by remember { mutableIntStateOf((savedData["step"] as? Int) ?: 1) }
    var isAlreadyCompleted by remember { mutableStateOf((savedData["completed"] as? Boolean) ?: false) }

    // Step 1 AI collects metrics (Pre-fill from earlier phases if available)
    var followers by remember {
        mutableStateOf(
            (savedData["followers"] as? String)?.ifBlank { null }
                ?: (level3Data["followers"] as? String)?.ifBlank { null }
                ?: (profileData["followers"] as? String)?.ifBlank { null }
                ?: "10,500"
        )
    }
    var reach by remember {
        mutableStateOf(
            (savedData["reach"] as? String)?.ifBlank { null }
                ?: (level3Data["reach"] as? String)?.ifBlank { null }
                ?: "45,000 / month"
        )
    }
    var reelViews by remember {
        mutableStateOf(
            (savedData["reelViews"] as? String)?.ifBlank { null }
                ?: (level3Data["views"] as? String)?.ifBlank { null }
                ?: "25,000"
        )
    }
    var storyViews by remember {
        mutableStateOf((savedData["storyViews"] as? String)?.ifBlank { null } ?: "3,500")
    }
    var likes by remember {
        mutableStateOf((savedData["likes"] as? String)?.ifBlank { null } ?: "1,200")
    }
    var engagement by remember {
        mutableStateOf(
            (savedData["engagement"] as? String)?.ifBlank { null }
                ?: (level3Data["engagement"] as? String)?.ifBlank { null }
                ?: "6.8%"
        )
    }

    // Step 2 Content Types (Multi-select)
    val availableContentTypes = listOf(
        "Instagram Story", "Instagram Reel", "Feed Post", "Carousel",
        "YouTube Video", "YouTube Short", "UGC Video", "Brand Event",
        "Monthly Package", "Custom"
    )
    val savedContentTypes = (savedData["contentTypes"] as? String)?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
    val selectedContentTypes = remember {
        mutableStateListOf<String>().apply {
            if (savedContentTypes.isNotEmpty()) addAll(savedContentTypes)
            else addAll(listOf("Instagram Reel", "Instagram Story", "UGC Video", "Monthly Package"))
        }
    }
    var customContentTypeInput by remember { mutableStateOf("") }

    // Step 3 Collaboration Types (Multi-select)
    val availableCollabTypes = listOf("Barter", "Paid", "Affiliate", "Long Term", "Ambassador", "Custom")
    val savedCollabTypes = (savedData["collabTypes"] as? String)?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
    val selectedCollabTypes = remember {
        mutableStateListOf<String>().apply {
            if (savedCollabTypes.isNotEmpty()) addAll(savedCollabTypes)
            else addAll(listOf("Paid", "Long Term", "Ambassador"))
        }
    }

    // Step 4 Brand Types (Multi-select)
    val availableBrandTypes = listOf("Startup", "Local Business", "National Brand", "International Brand", "Luxury Brand", "Custom")
    val savedBrandTypes = (savedData["brandTypes"] as? String)?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
    val selectedBrandTypes = remember {
        mutableStateListOf<String>().apply {
            if (savedBrandTypes.isNotEmpty()) addAll(savedBrandTypes)
            else addAll(listOf("National Brand", "Startup", "International Brand"))
        }
    }

    // Step 5 Country Selection
    val countries = listOf("India", "USA", "UK", "UAE", "Other")
    var selectedCountry by remember { mutableStateOf((savedData["country"] as? String) ?: "India") }

    // Calculated Pricing Ranges (Step 6)
    var storyPrice by remember { mutableStateOf((savedData["storyPrice"] as? String) ?: "") }
    var reelPrice by remember { mutableStateOf((savedData["reelPrice"] as? String) ?: "") }
    var feedPrice by remember { mutableStateOf((savedData["feedPrice"] as? String) ?: "") }
    var youtubePrice by remember { mutableStateOf((savedData["youtubePrice"] as? String) ?: "") }
    var ugcPrice by remember { mutableStateOf((savedData["ugcPrice"] as? String) ?: "") }
    var monthlyPackagePrice by remember { mutableStateOf((savedData["monthlyPackagePrice"] as? String) ?: "") }

    // Negotiation Score (Step 7)
    var negotiationConfidence by remember { mutableStateOf((savedData["negotiationConfidence"] as? String) ?: "High") }

    // Rate Card Checklist (Step 11)
    val savedChecklistCsv = (savedData["checklist"] as? String) ?: ""
    val checklistItems = remember {
        mutableStateListOf<Boolean>().apply {
            val list = if (savedChecklistCsv.isNotBlank()) savedChecklistCsv.split(",").map { it.toBoolean() } else emptyList()
            if (list.size == 5) addAll(list)
            else addAll(listOf(true, true, true, true, true))
        }
    }

    // Recalculate Prices automatically whenever inputs change
    fun recalculatePricing() {
        if (selectedCountry == "India" || selectedCountry == "Other") {
            storyPrice = "₹1,500 – ₹3,500"
            reelPrice = "₹5,000 – ₹12,000"
            feedPrice = "₹3,000 – ₹7,000"
            youtubePrice = "₹10,000 – ₹25,000"
            ugcPrice = "₹4,000 – ₹9,000"
            monthlyPackagePrice = "₹25,000 – ₹60,000"
        } else if (selectedCountry == "USA") {
            storyPrice = "$50 – $120"
            reelPrice = "$150 – $400"
            feedPrice = "$100 – $250"
            youtubePrice = "$300 – $800"
            ugcPrice = "$120 – $300"
            monthlyPackagePrice = "$800 – $2,000"
        } else if (selectedCountry == "UK") {
            storyPrice = "£40 – £100"
            reelPrice = "£120 – £320"
            feedPrice = "£80 – £200"
            youtubePrice = "£250 – £650"
            ugcPrice = "£100 – £250"
            monthlyPackagePrice = "£650 – £1,600"
        } else {
            storyPrice = "AED 200 – AED 500"
            reelPrice = "AED 600 – AED 1,500"
            feedPrice = "AED 400 – AED 1,000"
            youtubePrice = "AED 1,200 – AED 3,200"
            ugcPrice = "AED 500 – AED 1,200"
            monthlyPackagePrice = "AED 3,000 – AED 7,500"
        }
    }

    LaunchedEffect(selectedCountry, followers, reelViews, engagement) {
        recalculatePricing()
    }

    // Persist state helper
    fun persistState(completed: Boolean = isAlreadyCompleted) {
        CreatorAcademyPrefs.saveBrandCollabLevel4State(
            context = context,
            step = currentStep,
            followers = followers,
            reach = reach,
            reelViews = reelViews,
            storyViews = storyViews,
            likes = likes,
            engagement = engagement,
            contentTypes = selectedContentTypes.joinToString(","),
            collabTypes = selectedCollabTypes.joinToString(","),
            brandTypes = selectedBrandTypes.joinToString(","),
            country = selectedCountry,
            storyPrice = storyPrice,
            reelPrice = reelPrice,
            feedPrice = feedPrice,
            youtubePrice = youtubePrice,
            ugcPrice = ugcPrice,
            monthlyPackagePrice = monthlyPackagePrice,
            negotiationConfidence = negotiationConfidence,
            checklistCsv = checklistItems.joinToString(","),
            isCompleted = completed
        )
    }

    // Animation progress ring angle (base 40% up to 100%)
    val progressPercent = if (isAlreadyCompleted || currentStep == 11) 100 else (40 + (currentStep - 1) * 6).coerceAtMost(100)
    val progressRingAngle by animateFloatAsState(
        targetValue = (progressPercent / 100f) * 360f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "level4ProgressRing"
    )

    // Floating animation offset for background particles
    val infiniteTransition = rememberInfiniteTransition(label = "rateCardBg")
    val floatingOffsetY by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatingOffset"
    )

    val creatorName = (level3Data["creatorName"] as? String)?.ifBlank { null }
        ?: (level3Data["fullName"] as? String)?.ifBlank { null }
        ?: "Creator"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF0F172A))))
    ) {
        // Floating Background Particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            drawCircle(Color(0xFFFFD700).copy(alpha = 0.15f), radius = 8.dp.toPx(), center = Offset(width * 0.18f, height * 0.18f + floatingOffsetY * 2))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.12f), radius = 12.dp.toPx(), center = Offset(width * 0.82f, height * 0.32f - floatingOffsetY * 3))
            drawCircle(Color(0x3338BDF8), radius = 10.dp.toPx(), center = Offset(width * 0.85f, height * 0.72f + floatingOffsetY * 2))
            drawCircle(Color(0x224ADE80), radius = 14.dp.toPx(), center = Offset(width * 0.12f, height * 0.78f - floatingOffsetY * 3))
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
                            text = "AI Rate Card Builder",
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
                            Text("LEVEL 4", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                        }
                    }
                    Text(
                        text = "Know Your Creator Value",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }

                // Animated Progress Ring
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
                        text = "$progressPercent%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFD700)
                    )
                }
            }

            // Step Content Scrollable Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Floating Brand Logos Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(" Boat", " Nykaa", " Meesho", " Amazon", " Snitch", " Minimalist", " Flipkart", " Myntra").forEach { logo ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0x22FFD700))
                                .border(1.dp, Color(0x44FFD700), RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(logo, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                        }
                    }
                }

                // AI Mentor Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(listOf(Color(0x331E293B), Color(0x33334155))))
                        .border(1.dp, Color(0x44FFD700), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
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

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("AI Mentor", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("• Soft Glow active", fontSize = 9.sp, color = Color(0xFF4ADE80))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            val mentorSpeech = when (currentStep) {
                                1 -> "Bahut saare creators apni value se kam charge karte hain. Main tumhare profile ke hisaab se ek professional Rate Card banaunga."
                                2 -> "Har content deliverable ka market commercial rate alag hota hai! Select karo tum kya-kya create karte ho."
                                3 -> "Paid deals aur Long-Term retainers se tumhein predictable monthly income milti hai."
                                4 -> "National aur International brands ka budget startups se 3x–5x zyada hota hai!"
                                5 -> "Tum jis country / market ke brands target kar rahe ho, uske mutabiq baseline currency adjust hoti hai."
                                6 -> "Main AI pricing model se tumhare followers, views aur engagement calculate karke exact market rates generate kar raha hoon..."
                                7 -> "Negotiation Confidence score tumhe yeh confident decision lene mein help karega ki price par kitna firm rehna hai."
                                8 -> "Yeh 5 smart pricing rules tumhein undercharging aur scam brand deals se protect karenge."
                                9 -> "Yeh tumhara 1-page Professional AI Rate Card hai! Ab direct brand managers ko share karo."
                                10 -> "Long-term retainer packages aur bundle offers se tum brand deals 2x multiplier kar sakte ho!"
                                else -> "Badhaai ho! Tumhara Professional AI Rate Card ready hai. Ab hamesha apni real value par deal finalize karo!"
                            }
                            Text(
                                text = mentorSpeech,
                                fontSize = 11.5.sp,
                                color = Color.White,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Steps Rendering
                when (currentStep) {
                    1 -> {
                        RateCardGlassCard(title = "Step 1: Creator Metrics & Performance Data") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x224ADE80))
                                    .padding(8.dp)
                            ) {
                                Text("✨ Auto-filled from previous phase profile! Refine any field below:", fontSize = 10.sp, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            RateCardTextField("Followers", followers, "e.g. 10,500") { followers = it; persistState() }
                            RateCardTextField("Average Monthly Reach", reach, "e.g. 45,000 / month") { reach = it; persistState() }
                            RateCardTextField("Average Reel Views", reelViews, "e.g. 25,000") { reelViews = it; persistState() }
                            RateCardTextField("Average Story Views", storyViews, "e.g. 3,500") { storyViews = it; persistState() }
                            RateCardTextField("Average Likes", likes, "e.g. 1,200") { likes = it; persistState() }
                            RateCardTextField("Engagement Rate", engagement, "e.g. 6.8%") { engagement = it; persistState() }
                        }
                    }

                    2 -> {
                        RateCardGlassCard(title = "Step 2: Select Offered Content Types") {
                            Text("Check deliverables you create for brand campaigns:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(8.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                availableContentTypes.forEach { item ->
                                    val isSelected = selectedContentTypes.contains(item)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(if (isSelected) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                            .clickable {
                                                if (isSelected) selectedContentTypes.remove(item)
                                                else selectedContentTypes.add(item)
                                                persistState()
                                            }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = item,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.Black else Color.White
                                        )
                                    }
                                }
                            }

                            if (selectedContentTypes.contains("Custom")) {
                                Spacer(modifier = Modifier.height(8.dp))
                                RateCardTextField("Custom Deliverable Name", customContentTypeInput, "e.g. Podcast Sponsor Segment") {
                                    customContentTypeInput = it
                                }
                            }
                        }
                    }

                    3 -> {
                        RateCardGlassCard(title = "Step 3: Select Collaboration Types") {
                            Text("Choose collaboration structures you accept:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(8.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                availableCollabTypes.forEach { item ->
                                    val isSelected = selectedCollabTypes.contains(item)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(if (isSelected) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                            .clickable {
                                                if (isSelected) selectedCollabTypes.remove(item)
                                                else selectedCollabTypes.add(item)
                                                persistState()
                                            }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = item,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.Black else Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                    4 -> {
                        RateCardGlassCard(title = "Step 4: Select Target Brand Types") {
                            Text("What category of brands do you aim to work with?", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(8.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                availableBrandTypes.forEach { item ->
                                    val isSelected = selectedBrandTypes.contains(item)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(if (isSelected) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                            .clickable {
                                                if (isSelected) selectedBrandTypes.remove(item)
                                                else selectedBrandTypes.add(item)
                                                persistState()
                                            }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = item,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.Black else Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                    5 -> {
                        RateCardGlassCard(title = "Step 5: Target Country & Market Region") {
                            Text("Select market currency for pricing calculations:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(8.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                countries.forEach { country ->
                                    val isSelected = selectedCountry == country
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSelected) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                            .clickable {
                                                selectedCountry = country
                                                recalculatePricing()
                                                persistState()
                                            }
                                            .padding(horizontal = 14.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = country,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.Black else Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                    6 -> {
                        RateCardGlassCard(title = "Step 6: AI Estimated Pricing Range") {
                            Text("Commercial rate estimates generated by AI pricing model:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(8.dp))

                            PricingRow("Instagram Story", storyPrice)
                            PricingRow("Instagram Reel", reelPrice)
                            PricingRow("Feed Post", feedPrice)
                            PricingRow("YouTube Video", youtubePrice)
                            PricingRow("UGC Video (Raw Rights)", ugcPrice)
                            PricingRow("Monthly Retainer Package", monthlyPackagePrice)

                            Spacer(modifier = Modifier.height(10.dp))
                            Text("💡 Pro Tip: Never quote below minimum bound during initial brand pitches.", fontSize = 10.sp, color = Color(0xFFFFD700))
                        }
                    }

                    7 -> {
                        RateCardGlassCard(title = "Step 7: Negotiation Confidence Meter") {
                            Text("Select your current negotiation comfort level:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(8.dp))

                            val options = listOf("Beginner", "Moderate", "High", "Firm Pro")
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                options.forEach { opt ->
                                    val isSel = negotiationConfidence == opt
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                            .clickable {
                                                negotiationConfidence = opt
                                                persistState()
                                            }
                                            .padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Text(opt, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x22FFFFFF))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = when (negotiationConfidence) {
                                        "Beginner" -> "Rule: Always ask 50% advance payment before starting video script!"
                                        "Moderate" -> "Rule: Offer 1 minor revision free. Charge +20% for raw source files."
                                        "High" -> "Rule: Quote +30% higher than your target price to leave room for negotiation."
                                        else -> "Rule: Stand firm on commercial rates. Never take barter for high production videos."
                                    },
                                    fontSize = 11.sp,
                                    color = Color(0xFFFFD700)
                                )
                            }
                        }
                    }

                    8 -> {
                        RateCardGlassCard(title = "Step 8: Smart Pricing Rules") {
                            Text("5 Unbreakable Rules of Creator Pricing:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(8.dp))

                            listOf(
                                "1. Never work for pure 'exposure' if brand has marketing budget.",
                                "2. Always cap revision limit to 1 minor edit in base quote.",
                                "3. Require 50% advance deposit before shooting content.",
                                "4. Charge 25% – 50% extra for digital ad usage rights.",
                                "5. Express enthusiasm first, then present commercial rate card."
                            ).forEach { rule ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0x22FFFFFF))
                                        .padding(8.dp)
                                ) {
                                    Text(rule, fontSize = 11.sp, color = Color.White)
                                }
                            }
                        }
                    }

                    9 -> {
                        RateCardGlassCard(title = "Step 9: Professional Rate Card Preview") {
                            Text("1-Page Official Commercial Rate Card Summary:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A))))
                                    .border(1.5.dp, Color(0xFFFFD700), RoundedCornerShape(14.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(creatorName, fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                            Text("$userNiche Creator • $userPlatform", fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0x33FFD700))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(selectedCountry, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text("COMMERCIAL DELIVERABLE RATES:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.5f))
                                    Spacer(modifier = Modifier.height(4.dp))

                                    PricingRow("Instagram Reel", reelPrice)
                                    PricingRow("Instagram Story", storyPrice)
                                    PricingRow("Feed Post / Carousel", feedPrice)
                                    PricingRow("UGC Video (Brand Owned)", ugcPrice)
                                    PricingRow("Monthly Retainer", monthlyPackagePrice)

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Button(
                                        onClick = {
                                            val textToCopy = """
                                                Official Rate Card - $creatorName ($userNiche)
                                                - Instagram Reel: $reelPrice
                                                - Instagram Story: $storyPrice
                                                - Feed Post: $feedPrice
                                                - UGC Content: $ugcPrice
                                                - Monthly Retainer: $monthlyPackagePrice
                                            """.trimIndent()
                                            clipboardManager.setText(AnnotatedString(textToCopy))
                                            Toast.makeText(context, "Rate Card copied to clipboard!", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("📋 Copy Rate Card Text", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }

                    10 -> {
                        RateCardGlassCard(title = "Step 10: AI Rate Improvements") {
                            Text("Unlock 2x Higher Commercial Rates with these upgrades:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(8.dp))

                            listOf(
                                "🚀 Pitch Retainers instead of 1-off Reels: Predictable income for you, higher ROI for brand.",
                                "📊 Send Past Case Studies: Show screenshot of link clicks or product sales from last deal.",
                                "🎁 Bundle Story + Reel + Link in Bio: Increases perceived deal value by 40%."
                            ).forEach { item ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0x22FFFFFF))
                                        .padding(10.dp)
                                ) {
                                    Text(item, fontSize = 11.sp, color = Color.White)
                                }
                            }
                        }
                    }

                    11 -> {
                        RateCardGlassCard(title = "Step 11: Professional Pricing Ready") {
                            Text("Check off essential terms before sending rate card:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(8.dp))

                            val checklistLabels = listOf(
                                "Calculated realistic rates based on current reach",
                                "Set 1 minor revision limit in deliverables",
                                "Included 50% advance deposit term",
                                "Defined Usage Rights duration (30 / 60 days)",
                                "Ready to share Rate Card with Brand Managers"
                            )

                            checklistLabels.forEachIndexed { idx, label ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                ) {
                                    Checkbox(
                                        checked = checklistItems.getOrElse(idx) { true },
                                        onCheckedChange = { checked ->
                                            if (idx < checklistItems.size) {
                                                checklistItems[idx] = checked
                                                persistState()
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFFD700))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(label, fontSize = 11.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ================= BOTTOM BUTTON BAR =================
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        if (currentStep > 1) {
                            currentStep--
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
                        if (currentStep < 11) {
                            currentStep++
                            persistState()
                        } else {
                            persistState(completed = true)
                            onLevel4Completed()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (currentStep < 11) "Continue ›" else "Finish Level 4 ✨",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
