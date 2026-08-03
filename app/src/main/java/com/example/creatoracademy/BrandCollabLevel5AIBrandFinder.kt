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

data class BrandTrackItem(
    val id: String,
    val name: String,
    val status: String, // Saved, Contacted, Waiting Reply, Negotiation, Approved, Rejected, Completed
    val priority: String, // High, Medium, Low
    val website: String = ""
)

private fun getSampleBrandForNiche(niche: String, index: Int): String {
    val list = when (niche.trim().lowercase()) {
        "fashion" -> listOf("Zara", "H&M", "Urbanic", "Snitch", "FabIndia", "Biba")
        "beauty" -> listOf("Mamaearth", "Sugar Cosmetics", "Plum Goodness", "Nykaa", "Kay Beauty", "Foxtale")
        "tech" -> listOf("Boat Audio", "Noise", "Boult Audio", "Fire-Boltt", "Portronics", "Realme")
        "gaming" -> listOf("Asus ROG", "Logitech G", "Razer", "Redgear", "Cosmic Byte", "HP Omen")
        "finance" -> listOf("INDmoney", "Groww", "Zerodha", "Fi Money", "Jupiter", "Smallcase")
        "fitness" -> listOf("MyProtein", "Fast&Up", "Cult.fit", "Boldfit", "Kapiva", "HealthKart")
        "food" -> listOf("Zomato", "Swiggy", "Slurrp Farm", "The Whole Truth", "Epigamia", "Country Delight")
        "travel" -> listOf("MakeMyTrip", "Goibibo", "Booking.com", "Hostelworld", "Wildcraft", "Safari")
        else -> listOf("Boat Audio", "Mamaearth", "Snitch", "Noise", "MyProtein", "INDmoney")
    }
    return list.getOrElse(index % list.size) { "Brand ${index + 1}" }
}

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
fun BrandCollabLevel5AIBrandFinderView(
    userNiche: String,
    userPlatform: String,
    onLevel5Completed: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val savedData = remember { CreatorAcademyPrefs.getBrandCollabLevel5Data(context) }

    var currentStep by remember { mutableStateOf((savedData["step"] as? Int) ?: 1) }
    var selectedPlatform by remember { mutableStateOf((savedData["platform"] as? String)?.ifBlank { userPlatform } ?: userPlatform.ifBlank { "Instagram" }) }
    var selectedNiche by remember { mutableStateOf((savedData["niche"] as? String)?.ifBlank { userNiche } ?: userNiche.ifBlank { "Tech" }) }
    var selectedBrandSize by remember { mutableStateOf((savedData["brandSize"] as? String)?.ifBlank { "Growing Brands" } ?: "Growing Brands") }
    var selectedCollabType by remember { mutableStateOf((savedData["collabType"] as? String)?.ifBlank { "Paid" } ?: "Paid") }

    // Pre-populated saved brands list
    val initialBrands = remember(selectedNiche) {
        listOf(
            BrandTrackItem("1", getSampleBrandForNiche(selectedNiche, 0), "Saved", "High", "https://official.com"),
            BrandTrackItem("2", getSampleBrandForNiche(selectedNiche, 1), "Contacted", "High", "https://official.com"),
            BrandTrackItem("3", getSampleBrandForNiche(selectedNiche, 2), "Waiting Reply", "Medium", "https://official.com"),
            BrandTrackItem("4", getSampleBrandForNiche(selectedNiche, 3), "Negotiation", "High", "https://official.com")
        )
    }

    val savedBrandsList = remember { mutableStateListOf<BrandTrackItem>().apply { addAll(initialBrands) } }
    var newBrandNameInput by remember { mutableStateOf("") }
    var newBrandStatusInput by remember { mutableStateOf("Saved") }

    // Research checklist state
    val checklistItems = remember {
        listOf(
            "✔ Does brand fit your niche?",
            "✔ Do they work with creators?",
            "✔ Are they active on Instagram / YouTube?",
            "✔ Do they have creator campaigns?",
            "✔ Is official contact available?"
        )
    }
    val checklistState = remember { mutableStateListOf(true, true, true, false, true) }

    var isCalculatingEngine by remember { mutableStateOf(false) }

    fun persistState(completed: Boolean = false) {
        val savedBrandsCsv = savedBrandsList.joinToString(";") { "${it.id}|${it.name}|${it.status}|${it.priority}" }
        val checklistCsv = checklistState.joinToString(",") { it.toString() }
        CreatorAcademyPrefs.saveBrandCollabLevel5State(
            context = context,
            step = currentStep,
            platform = selectedPlatform,
            niche = selectedNiche,
            brandSize = selectedBrandSize,
            collabType = selectedCollabType,
            savedBrandsJson = savedBrandsCsv,
            checklistCsv = checklistCsv,
            isCompleted = completed
        )
    }

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "bg_anim_l5")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val progressPercentage = (currentStep / 13f) * 100f

    val mentorMessages = remember(currentStep, selectedNiche, selectedPlatform) {
        when (currentStep) {
            1 -> "Ab tum Brand Ready ho. Next step hai... Sahi brands ko identify karna. Platform choose karo jahan tum active ho!"
            2 -> "Tumhara primary niche ($selectedNiche) hi tumhari super-power hai. Targeted niche choose karne se brand reply chance 3x ho jata hai."
            3 -> "Brand Size matters! Micro creators ke liye Growing Brands aur National Brands best response conversion rate dete hain."
            4 -> "Collaboration Type clarify kar lo ($selectedCollabType). Premium brands structured campaign deals prefer karte hain."
            5 -> "AI Recommendation Engine analyze kar raha hai. Tumhare profile ke mutabiq high compatibility score wale brand types highlight ho gaye hain."
            6 -> "Sahi brands kahan milenge? Website, LinkedIn, IG tagged posts aur creator portals check karo."
            7 -> "Brand Research Checklist verify karo. Bina research ke batch outreach karne se bad impression padta hai."
            8 -> "Official contact identify karo. Direct PR / Creator team ka email target karo. Spam messages se bacho!"
            9 -> "Priority scoring system setup karo. High priority brands ko sabse pehle personalize pitches bhejo."
            10 -> "Daily Outreach Planner set karo: Daily 5 research, 2 contact, 3 save karo. Consistency produces results!"
            11 -> "Brand Tracker Board: Sabhi targeted, contacted aur active negotiation brands ko ek glass board par manage karo."
            12 -> "Common Outreach Mistakes se bacho! Same DM copy-paste karna, niche ignore karna sabse badi galtiyaan hain."
            13 -> "Today's Mission: Find Your First 5 Relevant Brands! Step complete karke +400 XP aur Brand Research Badge unlocked karo!"
            else -> "Sahi brands ko target karke high-paying deals secure karo!"
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
                        Color(0xFF090D16)
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
                            Text("LEVEL 5", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI Brand Finder", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text("Find The Right Brands • Phase 6", fontSize = 12.sp, color = Color(0xFFFFD700).copy(alpha = 0.9f))
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
                        Text("55% Target", fontSize = 7.sp, color = Color.White.copy(alpha = 0.7f))
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
                // STEP 1: CHOOSE YOUR PLATFORM
                1 -> {
                    Text("Step 1: Choose Your Platform", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Select where you post your main content:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    listOf("Instagram", "YouTube", "Multiple Platforms").forEach { platform ->
                        val isSel = selectedPlatform.equals(platform, ignoreCase = true) || (platform == "Multiple Platforms" && selectedPlatform == "Multiple")
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSel) Color(0x33FFD700) else Color(0x15FFFFFF))
                                .border(1.5.dp, if (isSel) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                                .clickable {
                                    selectedPlatform = if (platform == "Multiple Platforms") "Multiple" else platform
                                    persistState()
                                }
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = when(platform) {
                                            "Instagram" -> "📸"
                                            "YouTube" -> "▶️"
                                            else -> "🌐"
                                        },
                                        fontSize = 22.sp
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(platform, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Text(
                                            when(platform) {
                                                "Instagram" -> "Reels, Stories, Feed Posts & Collab Tags"
                                                "YouTube" -> "Longform Reviews, Shorts & Integrations"
                                                else -> "Cross-platform reach across IG + YT"
                                            },
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                                if (isSel) {
                                    Text("✓", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                }
                            }
                        }
                    }
                }

                // STEP 2: CHOOSE YOUR NICHE
                2 -> {
                    Text("Step 2: Choose Your Niche", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Select your primary content domain:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val niches = listOf(
                        "Fashion", "Beauty", "Tech", "Gaming",
                        "Finance", "Education", "Fitness", "Lifestyle",
                        "Food", "Travel", "Business", "Photography", "Other"
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        niches.forEach { nicheItem ->
                            val isSel = selectedNiche.equals(nicheItem, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                    .border(1.dp, if (isSel) Color(0xFFFFD700) else Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                                    .clickable {
                                        selectedNiche = nicheItem
                                        persistState()
                                    }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = nicheItem,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) Color.Black else Color.White
                                )
                            }
                        }
                    }
                }

                // STEP 3: CHOOSE BRAND SIZE
                3 -> {
                    Text("Step 3: Choose Target Brand Size", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Target the brand scale that matches your experience:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val brandSizes = listOf(
                        "Small Brands" to "D2C Startups & Local Brands (Barter / Fast Paid Deals)",
                        "Growing Brands" to "Regional & Scaleups (Highest reply rate for Micro Creators)",
                        "National Brands" to "Established Indian Brands (Higher budgets & PR Teams)",
                        "International Brands" to "Global MNCs (Requires solid portfolio & numbers)",
                        "Luxury Brands" to "High-end Premium Lifestyle & Fashion",
                        "All Sizes" to "Open to all brand categories & sizes"
                    )

                    brandSizes.forEach { (size, desc) ->
                        val isSel = selectedBrandSize == size
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSel) Color(0x33FFD700) else Color(0x15FFFFFF))
                                .border(1.dp, if (isSel) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                                .clickable {
                                    selectedBrandSize = size
                                    persistState()
                                }
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Text(size, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color(0xFFFFD700) else Color.White)
                                    if (isSel) Text("✓ Selected", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                }
                                Text(desc, fontSize = 11.sp, color = Color.White.copy(alpha = 0.65f))
                            }
                        }
                    }
                }

                // STEP 4: COLLABORATION TYPE
                4 -> {
                    Text("Step 4: Collaboration Type", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Select your preferred deal structure:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val collabTypes = listOf(
                        "Paid" to "Fixed payment for deliverables (Reels/Shorts/Posts)",
                        "Affiliate" to "Commission per sale generated via custom link/code",
                        "Barter" to "Free products / services in exchange for content",
                        "Long-Term Partnership" to "3 to 6 months retainer deal for ongoing content",
                        "Brand Ambassador" to "Exclusive brand face & recurring campaigns",
                        "UGC" to "User Generated Content created for brand's official ads"
                    )

                    collabTypes.forEach { (type, desc) ->
                        val isSel = selectedCollabType == type
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSel) Color(0x33FFD700) else Color(0x15FFFFFF))
                                .border(1.dp, if (isSel) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                                .clickable {
                                    selectedCollabType = type
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

                // STEP 5: AI RECOMMENDATION ENGINE
                5 -> {
                    Text("Step 5: AI Recommendation Engine", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Tailored brand target analytics based on your profile:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0x221E293B))
                            .border(1.5.dp, Color(0xFFFFD700), RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("⚡ AI Match Analytics", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0x3300FF88))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("HIGH COMPATIBILITY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00FF88))
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Top Brand Categories", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                                    Text(
                                        when(selectedNiche.lowercase()) {
                                            "tech" -> "Consumer Tech, Audio, D2C Gadgets"
                                            "fashion" -> "E-commerce Wearables, D2C Apparel"
                                            "beauty" -> "Skincare, Cosmetics, Personal Care"
                                            "gaming" -> "Peripherals, Mobile Gaming, Accessories"
                                            "fitness" -> "Nutrition, Activewear, Supplements"
                                            else -> "D2C Startups, Consumer Brands"
                                        },
                                        fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Recommended Deal Structure", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                                    Text("$selectedCollabType + Reel/Short Integration", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                MetricBox("Expected Reply Chance", "78%", Color(0xFF00FF88), Modifier.weight(1f))
                                Spacer(modifier = Modifier.width(8.dp))
                                MetricBox("Compatibility Score", "94/100", Color(0xFFFFD700), Modifier.weight(1f))
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    isCalculatingEngine = true
                                    persistState()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    if (isCalculatingEngine) "Recalculated ✓" else "Re-Calculate AI Recommendations 🔄",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }

                // STEP 6: BRAND DISCOVERY GUIDE
                6 -> {
                    Text("Step 6: Brand Discovery Guide", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("7 proven sources to discover brand contacts step-by-step:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val discoverySources = listOf(
                        Triple("1. Official Website", "Check footer links for 'Press', 'Media Kit', 'Creator Program', or 'Contact Us'.", "🌐"),
                        Triple("2. Creator Program Page", "Search 'Brand Name + Creator Program' or 'Influencer Partnerships' on Google.", "🚀"),
                        Triple("3. LinkedIn Search", "Search for 'Influencer Marketing Manager', 'PR Lead', or 'Brand Partnerships' at target company.", "💼"),
                        Triple("4. Instagram Tagged Posts", "Check who other creators in your niche tag in sponsored posts.", "📸"),
                        Triple("5. Company Careers Page", "Look for active job postings for PR & Influencer managers to find team leads.", "🔍"),
                        Triple("6. Marketing Agencies", "Connect with agencies handling brand PR budgets (e.g. Winkl, Monk Entertainment).", "🏢"),
                        Triple("7. Creator Platforms", "Join verified platforms like Wishlink, Meesho Creator, Winkl & TagMango.", "📱")
                    )

                    discoverySources.forEach { (title, desc, icon) ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x1AFFFFFF))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Text(icon, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.8f))
                                }
                            }
                        }
                    }
                }

                // STEP 7: BRAND RESEARCH CHECKLIST
                7 -> {
                    Text("Step 7: Brand Research Checklist", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Verify these 5 checkpoints before pitching any brand:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    checklistItems.forEachIndexed { idx, item ->
                        val isChecked = checklistState.getOrElse(idx) { false }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isChecked) Color(0x2200FF88) else Color(0x15FFFFFF))
                                .border(1.dp, if (isChecked) Color(0xFF00FF88) else Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .clickable {
                                    checklistState[idx] = !isChecked
                                    persistState()
                                }
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(item, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        checklistState[idx] = checked
                                        persistState()
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF00FF88), checkmarkColor = Color.Black)
                                )
                            }
                        }
                    }
                }

                // STEP 8: OFFICIAL CONTACT GUIDE
                8 -> {
                    Text("Step 8: Official Contact Guide", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("How to identify legitimate & official contact info:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val contactTypes = listOf(
                        "Official Email" to "Look for emails ending with @branddomain.com (e.g., pr@brand.com, creators@brand.com). Avoid generic gmail.com addresses.",
                        "Official Website Form" to "Submit via official partnership forms on domain.com/creators.",
                        "Creator Portal" to "Sign up through verified brand dashboards or PR agency portals.",
                        "Official Marketing Lead" to "Reach out to verified Influencer Managers on LinkedIn with a clean elevator pitch."
                    )

                    contactTypes.forEach { (title, desc) ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x221E293B))
                                .border(1.dp, Color(0x33FFD700), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text("📌 $title", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Warning banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x33FF3366))
                            .border(1.dp, Color(0xFFFF3366), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            "⚠️ Anti-Spam Directive: Never send mass automated DMs or irrelevant emails. Quality personalized research beats spamming every single time!",
                            fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White
                        )
                    }
                }

                // STEP 9: BRAND PRIORITY SCORE
                9 -> {
                    Text("Step 9: Brand Priority Score", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Categorize your target brand list into 3 priority tiers:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val priorityTiers = listOf(
                        Triple("🟢 High Priority", "Perfect niche fit, active creator campaigns, verified decision maker email available, budget aligned.", Color(0xFF00FF88)),
                        Triple("🟡 Medium Priority", "Good niche fit, works with creators periodically, contact via LinkedIn or website form.", Color(0xFFFFD700)),
                        Triple("🔴 Low Priority", "Unclear budget, rarely tags creators, no direct PR contact info found.", Color(0xFFFF5555))
                    )

                    priorityTiers.forEach { (title, desc, color) ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0x15FFFFFF))
                                .border(1.5.dp, color, RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Column {
                                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                            }
                        }
                    }
                }

                // STEP 10: AI OUTREACH PLANNER
                10 -> {
                    Text("Step 10: AI Outreach Planner", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Daily recommended targets for consistent brand deals:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val dailyTargets = listOf(
                        "🔍 Research 5 Brands" to "Find 5 relevant brands in $selectedNiche niche with active campaigns.",
                        "✉️ Contact 2 Brands" to "Send personalized pitches to verified PR contacts.",
                        "🔖 Save 3 Brands" to "Add promising prospects to your Brand Tracker Board.",
                        "📊 Track Responses" to "Log replies, follow up after 4-5 days, and negotiate terms."
                    )

                    dailyTargets.forEach { (title, desc) ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x221E293B))
                                .border(1.dp, Color(0x33FFD700), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.8f))
                            }
                        }
                    }
                }

                // STEP 11: BRAND TRACKER
                11 -> {
                    Text("Step 11: Brand Tracker Board", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Manage all your targeted and contacted brands:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Add brand input row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newBrandNameInput,
                            onValueChange = { newBrandNameInput = it },
                            placeholder = { Text("Brand Name (e.g. Boat)", color = Color.White.copy(alpha = 0.35f), fontSize = 11.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFFD700),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (newBrandNameInput.isNotBlank()) {
                                    savedBrandsList.add(
                                        BrandTrackItem(
                                            id = System.currentTimeMillis().toString(),
                                            name = newBrandNameInput.trim(),
                                            status = newBrandStatusInput,
                                            priority = "High"
                                        )
                                    )
                                    newBrandNameInput = ""
                                    persistState()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(50.dp)
                        ) {
                            Text("+ Add", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Brand Tracker Glass Board
                    savedBrandsList.forEachIndexed { idx, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x221E293B))
                                .border(1.dp, Color(0x33FFD700), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.name, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("Priority: ${item.priority}", fontSize = 10.5.sp, color = Color(0xFFFFD700))
                                }

                                // Status chip toggle
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            when(item.status) {
                                                "Approved", "Completed" -> Color(0xFF00FF88)
                                                "Contacted", "Waiting Reply" -> Color(0xFFFFD700)
                                                "Negotiation" -> Color(0xFF38BDF8)
                                                else -> Color(0x33FFFFFF)
                                            }
                                        )
                                        .clickable {
                                            val statuses = listOf("Saved", "Contacted", "Waiting Reply", "Negotiation", "Approved", "Completed")
                                            val nextStatus = statuses[(statuses.indexOf(item.status) + 1) % statuses.size]
                                            savedBrandsList[idx] = item.copy(status = nextStatus)
                                            persistState()
                                        }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = "${item.status} 🔄",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (item.status in listOf("Approved", "Completed", "Contacted", "Waiting Reply", "Negotiation")) Color.Black else Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                // STEP 12: COMMON MISTAKES
                12 -> {
                    Text("Step 12: Common Mistakes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("5 fatal errors to avoid during brand outreach:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(12.dp))

                    val mistakes = listOf(
                        "❌ Sending Same DM Everywhere" to "Generic mass messages get ignored immediately. Always personalize the first sentence.",
                        "❌ Ignoring Brand Niche" to "Pitching tech gadgets to a beauty cosmetics brand shows lack of research.",
                        "❌ No Portfolio or Rate Card Link" to "Brands need quick proof of your past content quality and rate transparency.",
                        "❌ Unrealistic / Wrong Pricing" to "Overcharging without engagement metrics or undercharging devalues your work.",
                        "❌ Following Up Excessively" to "Spamming 'Please reply' within 24 hours destroys professional trust. Wait 4-5 days."
                    )

                    mistakes.forEach { (title, desc) ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x15FF3366))
                                .border(1.dp, Color(0xFFFF3366).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6688))
                                Text(desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.85f))
                            }
                        }
                    }
                }

                // STEP 13: TODAY'S MISSION & ACHIEVEMENT BADGE
                13 -> {
                    Text("Step 13: Today's Mission & Reward", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                    Text("Complete your Level 5 milestone:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
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
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🏆", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Brand Research Completed", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Text("Today's Mission: Find Your First 5 Relevant Brands", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFFFD700))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("+400 XP REWARD", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("⏱️ Est. Time: 15 Mins", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    persistState(completed = true)
                                    onLevel5Completed()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Complete Level 5 & Claim +400 XP 🎉", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
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
                border = BorderStroke(1.dp, Color(0xFFFFD700)),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFFD700)),
                modifier = Modifier.weight(1f)
            ) {
                Text(if (currentStep > 1) "‹ Back" else "‹ Previous Level", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    if (currentStep < 13) {
                        currentStep++
                        persistState()
                    } else {
                        persistState(completed = true)
                        onLevel5Completed()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (currentStep < 13) "Continue ›" else "Finish Level 5 ✨",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}
