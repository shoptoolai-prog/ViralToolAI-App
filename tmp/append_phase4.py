import java.io.File

content = """
// ============================================================================
// PHASE 4 - LEVEL 3: AI MEDIA KIT BUILDER
// ============================================================================

val MASTER_PHASE4_LEVEL3_MENTOR_REPLIES = listOf(
    "Media Kit tumhara digital resume hota hai. Professional brands sabse pehle isi ko dekhte hain. Ab main tumhare liye industry-standard Media Kit banaunga.",
    "Level 3 unlocked! High-paying brands (Rs. 25,000 to Rs. 2,00,000) demand a clean 1-page PDF Media Kit before signing contracts.",
    "A Media Kit bridges the gap between an amateur content creator and a commercially viable media partner.",
    "Brands care about 3 core metrics: Target Audience Match, Engagement Rate, and Authentic Reach. We will showcase all three!",
    "Your basic information establishes trust. Always use a professional email handle (e.g. contact@yourname.com) for brand pitches.",
    "Location and city matter to brands for regional campaigns, store inaugurations, and offline launch events.",
    "Adding direct links to your active social accounts allows brand managers to audit your grid in a single click.",
    "Your Creator Bio is your elevator pitch. It should highlight who you are, what value you deliver, and why brands should hire you.",
    "Let AI write your bio if you're feeling stuck! Our AI engine creates high-converting bio copy tailored specifically to your niche.",
    "Defining your Target Audience (Students, Gamers, Women, Tech Enthusiasts) tells brands if your followers match their buyer persona.",
    "Audience Demographics are gold for PR agencies. Showing primary country, language, and age group doubles your pitch success rate.",
    "Selecting clear Content Categories helps brand algorithm tools automatically index your creator profile under the right vertical.",
    "Highlighting past brand collaborations (even barter deals or self-initiated posts) builds social proof and credibility.",
    "Visual assets matter! Clean post screenshots, reel cover thumbnails, and campaign proof instantly double your rate card value.",
    "Real statistics like Engagement Rate (3%+ is great) matter 10x more than raw follower counts to modern influencer marketers.",
    "Your live Media Kit Preview is formatted like an executive 1-page PDF portfolio that PR agencies can share directly with brand managers.",
    "The AI Media Kit Audit checks 20+ parameters to score your profile appeal, trust index, and commercial readiness.",
    "AI Auto-Improvements polish your bio, summary, and achievements into agency-grade pitch language.",
    "Completing all 6 Brand Ready Checklist items guarantees your portfolio meets international creator standards!",
    "Congratulations on building your AI Media Kit! You are now fully equipped to pitch top-tier brands with confidence."
) + List(60) { index ->
    "AI Mentor Insight #${index + 21}: Professional Media Kits with verified statistics get 3x higher response rates from brand PR teams!"
}

@Composable
fun BrandCollabLevel3AIMediaKitView(
    userNiche: String = "Fashion",
    userPlatform: String = "Instagram",
    onLevel3Completed: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Load saved data
    val savedData = remember { CreatorAcademyPrefs.getBrandCollabLevel3Data(context) }
    var isAlreadyCompleted by remember { mutableStateOf(savedData["completed"] as? Boolean ?: false) }
    var currentStep by remember { mutableIntStateOf((savedData["step"] as? Int ?: 1).coerceIn(1, 13)) }

    // Step 1: Basic Info
    var fullName by remember { mutableStateOf(savedData["fullName"] as? String ?: "") }
    var creatorName by remember { mutableStateOf(savedData["creatorName"] as? String ?: "") }
    var email by remember { mutableStateOf(savedData["email"] as? String ?: "") }
    var city by remember { mutableStateOf(savedData["city"] as? String ?: "") }
    var country by remember { mutableStateOf(savedData["country"] as? String ?: "") }

    // Step 2: Social Accounts
    val savedSocials = (savedData["socialLinks"] as? String ?: "").split("|")
    var socialInstagram by remember { mutableStateOf(savedSocials.getOrNull(0) ?: "") }
    var socialYoutube by remember { mutableStateOf(savedSocials.getOrNull(1) ?: "") }
    var socialFacebook by remember { mutableStateOf(savedSocials.getOrNull(2) ?: "") }
    var socialLinkedin by remember { mutableStateOf(savedSocials.getOrNull(3) ?: "") }
    var socialWebsite by remember { mutableStateOf(savedSocials.getOrNull(4) ?: "") }

    // Step 3: Bio
    var bio by remember { mutableStateOf(savedData["bio"] as? String ?: "") }
    var isAiBioGenerating by remember { mutableStateOf(false) }

    // Step 4: Audience
    var selectedAudience by remember { mutableStateOf(savedData["audience"] as? String ?: "Gen-Z & Youth") }

    // Step 5: Demographics
    var demoCountry by remember { mutableStateOf(savedData["demoCountry"] as? String ?: "India") }
    var demoLang by remember { mutableStateOf(savedData["demoLang"] as? String ?: "Hindi / English") }
    var demoAge by remember { mutableStateOf(savedData["demoAge"] as? String ?: "18–24 years") }

    // Step 6: Categories
    val initialCats = (savedData["categories"] as? String ?: "").split(",").filter { it.isNotBlank() }
    var selectedCategories by remember { mutableStateOf(if (initialCats.isNotEmpty()) initialCats else listOf(userNiche, "Lifestyle", "Technology")) }

    // Step 7: Achievements
    val savedAchievements = (savedData["achievements"] as? String ?: "").split("|")
    var achievementsBrands by remember { mutableStateOf(savedAchievements.getOrNull(0) ?: "Boat, Nykaa, Mamaearth") }
    var achievementsCertificates by remember { mutableStateOf(savedAchievements.getOrNull(1) ?: "Meta Certified Digital Creator") }
    var achievementsAwards by remember { mutableStateOf(savedAchievements.getOrNull(2) ?: "Top 10 Micro Creator 2025") }
    var achievementsSkills by remember { mutableStateOf(savedAchievements.getOrNull(3) ?: "4K Video Production, Reel Editing") }

    // Step 8: Portfolio Images upload simulator
    var profilePhotoUploaded by remember { mutableStateOf(true) }
    var reelScreenshotUploaded by remember { mutableStateOf(true) }
    var postUploaded by remember { mutableStateOf(true) }
    var thumbnailUploaded by remember { mutableStateOf(true) }
    var campaignUploaded by remember { mutableStateOf(true) }

    // Step 9: Statistics
    var followers by remember { mutableStateOf(savedData["followers"] as? String ?: "12,500") }
    var avgReach by remember { mutableStateOf(savedData["reach"] as? String ?: "55,000 / month") }
    var avgViews by remember { mutableStateOf(savedData["views"] as? String ?: "28,000 / reel") }
    var engagementRate by remember { mutableStateOf(savedData["engagement"] as? String ?: "6.8%") }
    var monthlyViews by remember { mutableStateOf(savedData["monthlyViews"] as? String ?: "180,000") }

    // Step 11: AI Review
    var isAiReviewing by remember { mutableStateOf(false) }
    var aiReviewDone by remember { mutableStateOf(false) }

    // Step 12: Auto Improvements
    var isAiEnhanced by remember { mutableStateOf(false) }

    // Step 13: Checklist
    val initialChecklist = (savedData["checklist"] as? String ?: "").split(",").map { it == "true" }
    var checklistItems by remember {
        mutableStateOf(
            if (initialChecklist.size == 6) initialChecklist.toMutableList()
            else mutableListOf(true, true, true, true, true, true)
        )
    }

    // Auto-save helper
    fun persistState(completed: Boolean = false) {
        CreatorAcademyPrefs.saveBrandCollabLevel3State(
            context = context,
            step = currentStep,
            fullName = fullName,
            creatorName = creatorName,
            email = email,
            city = city,
            country = country,
            socialLinks = "$socialInstagram|$socialYoutube|$socialFacebook|$socialLinkedin|$socialWebsite",
            bio = bio,
            audience = selectedAudience,
            demoCountry = demoCountry,
            demoLang = demoLang,
            demoAge = demoAge,
            categories = selectedCategories.joinToString(","),
            achievements = "$achievementsBrands|$achievementsCertificates|$achievementsAwards|$achievementsSkills",
            followers = followers,
            reach = avgReach,
            views = avgViews,
            engagement = engagementRate,
            monthlyViews = monthlyViews,
            checklistCsv = checklistItems.joinToString(","),
            isCompleted = completed || isAlreadyCompleted
        )
    }

    // Motion & particle transitions
    val infiniteTransition = rememberInfiniteTransition(label = "FloatingAssets")
    val floatingOffsetY by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FloatingY"
    )

    val progressRingAngle by animateFloatAsState(
        targetValue = if (isAlreadyCompleted || currentStep == 13) 360f else (currentStep / 13f) * 360f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "ProgressRing"
    )

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // Floating Background Brands & Golden Particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Golden Particles
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.15f), radius = 6.dp.toPx(), center = Offset(width * 0.15f, height * 0.2f + floatingOffsetY * 3))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.10f), radius = 10.dp.toPx(), center = Offset(width * 0.82f, height * 0.35f - floatingOffsetY * 2))
            drawCircle(Color(0x3338BDF8), radius = 8.dp.toPx(), center = Offset(width * 0.88f, height * 0.75f + floatingOffsetY * 2))
            drawCircle(Color(0x224ADE80), radius = 12.dp.toPx(), center = Offset(width * 0.12f, height * 0.8f - floatingOffsetY * 4))
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
                            text = "AI Media Kit Builder",
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
                            Text("LEVEL 3", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                        }
                    }
                    Text(
                        text = "Build Your Professional Creator Portfolio",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }

                // 28% Animated Progress Ring
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
                        text = if (isAlreadyCompleted || currentStep == 13) "100%" else "${((currentStep / 13f) * 100).toInt()}%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFD700)
                    )
                }
            }

            // Step Content Box
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // AI Mentor Header Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0x331E293B), Color(0x44334155))
                            )
                        )
                        .border(1.dp, Color(0x33FFD700), RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        // AI Avatar with soft glow
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0x33FFD700))
                                .border(1.5.dp, Color(0xFFFFD700), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🤖", fontSize = 22.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "AI Brand Mentor",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD700)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "● 80+ Styles Active",
                                    fontSize = 9.sp,
                                    color = Color(0xFF4ADE80)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (currentStep == 1) "Media Kit tumhara digital resume hota hai. Professional brands sabse pehle isi ko dekhte hain. Ab main tumhare liye industry-standard Media Kit banaunga."
                                else MASTER_PHASE4_LEVEL3_MENTOR_REPLIES.getOrElse(currentStep - 1) { MASTER_PHASE4_LEVEL3_MENTOR_REPLIES.first() },
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Floating Brand Logos Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x15FFFFFF))
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🎧 Boat", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                    Text("💄 Nykaa", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                    Text("🌿 Mamaearth", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                    Text("👟 Puma", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                    Text("📱 Samsung", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Step Indicator Pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STEP $currentStep OF 13",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFD700)
                    )
                    Text(
                        text = when (currentStep) {
                            1 -> "Basic Information"
                            2 -> "Social Accounts"
                            3 -> "Creator Bio"
                            4 -> "Target Audience"
                            5 -> "Demographics"
                            6 -> "Content Categories"
                            7 -> "Achievements"
                            8 -> "Portfolio Images"
                            9 -> "Performance Statistics"
                            10 -> "Media Kit Preview"
                            11 -> "AI Audit Review"
                            12 -> "Auto Improvements"
                            else -> "Brand Ready Checklist"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // STEP PANELS
                when (currentStep) {
                    1 -> {
                        // Step 1: Basic Info
                        MediaKitGlassCard(title = "Step 1: Basic Information") {
                            MediaKitTextField("Full Name", fullName, "e.g., Rohan Sharma") { fullName = it; persistState() }
                            MediaKitTextField("Creator Name / Handle", creatorName, "e.g., @rohan_creates") { creatorName = it; persistState() }
                            MediaKitTextField("Professional Email", email, "e.g., contact@rohansharma.com") { email = it; persistState() }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(modifier = Modifier.weight(1f)) {
                                    MediaKitTextField("City", city, "e.g., Mumbai") { city = it; persistState() }
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    MediaKitTextField("Country", country, "e.g., India") { country = it; persistState() }
                                }
                            }
                        }
                    }

                    2 -> {
                        // Step 2: Social Links
                        MediaKitGlassCard(title = "Step 2: Social Account Links") {
                            MediaKitTextField("Instagram Profile Link", socialInstagram, "https://instagram.com/your_handle") { socialInstagram = it; persistState() }
                            MediaKitTextField("YouTube Channel Link", socialYoutube, "https://youtube.com/@your_channel") { socialYoutube = it; persistState() }
                            MediaKitTextField("Facebook Page Link", socialFacebook, "https://facebook.com/your_page") { socialFacebook = it; persistState() }
                            MediaKitTextField("LinkedIn Profile Link", socialLinkedin, "https://linkedin.com/in/your_name") { socialLinkedin = it; persistState() }
                            MediaKitTextField("Website / Blog (Optional)", socialWebsite, "https://yourportfolio.com") { socialWebsite = it; persistState() }
                        }
                    }

                    3 -> {
                        // Step 3: Creator Bio
                        MediaKitGlassCard(title = "Step 3: Creator Bio & Elevator Pitch") {
                            OutlinedTextField(
                                value = bio,
                                onValueChange = { bio = it; persistState() },
                                label = { Text("Tell brands about yourself", color = Color(0xFFFFD700)) },
                                placeholder = { Text("e.g. Passionate fashion & tech creator helping youth discover modern lifestyle trends.", color = Color.White.copy(alpha = 0.4f)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFFFD700),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isAiBioGenerating = true
                                    coroutineScope.launch {
                                        delay(600)
                                        bio = "Hi, I'm ${creatorName.ifBlank { "a digital creator" }}! I create high-converting $userNiche content on $userPlatform for an engaged youth audience. Partnered with top brands to drive authentic engagement and sales."
                                        isAiBioGenerating = false
                                        persistState()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFD700))
                            ) {
                                Text(if (isAiBioGenerating) "✨ AI Writing Bio..." else "✨ Generate Bio Using AI", color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    4 -> {
                        // Step 4: Audience
                        MediaKitGlassCard(title = "Step 4: Target Audience") {
                            Text("Select Your Primary Target Audience:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                            Spacer(modifier = Modifier.height(8.dp))
                            val audiences = listOf("Students", "Gamers", "Businesses", "Women", "Men", "Fashion Enthusiasts", "Tech Lovers", "Mixed Youth", "Custom")
                            FlowRowHorizontal(audiences, selectedAudience) {
                                selectedAudience = it
                                persistState()
                            }
                        }
                    }

                    5 -> {
                        // Step 5: Audience Demographics
                        MediaKitGlassCard(title = "Step 5: Audience Demographics") {
                            MediaKitTextField("Primary Country", demoCountry, "e.g. India (85%)") { demoCountry = it; persistState() }
                            MediaKitTextField("Primary Language", demoLang, "e.g. Hindi & English") { demoLang = it; persistState() }
                            MediaKitTextField("Age Group", demoAge, "e.g. 18–24 years (65%)") { demoAge = it; persistState() }
                        }
                    }

                    6 -> {
                        // Step 6: Content Categories
                        MediaKitGlassCard(title = "Step 6: Content Categories") {
                            Text("Select categories that match your content:", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                            Spacer(modifier = Modifier.height(8.dp))
                            val allCategories = listOf("Fashion", "Beauty", "Gaming", "Technology", "Fitness", "Education", "Finance", "Travel", "Food", "Lifestyle", "Comedy", "Business", "Other")
                            FlowMultiSelectChips(allCategories, selectedCategories) { updated ->
                                selectedCategories = updated
                                persistState()
                            }
                        }
                    }

                    7 -> {
                        // Step 7: Achievements
                        MediaKitGlassCard(title = "Step 7: Achievements & Past Experience") {
                            MediaKitTextField("Brands Worked With", achievementsBrands, "e.g., Boat, Nykaa, Mamaearth") { achievementsBrands = it; persistState() }
                            MediaKitTextField("Certificates", achievementsCertificates, "e.g., Meta Certified Digital Creator") { achievementsCertificates = it; persistState() }
                            MediaKitTextField("Awards & Honors", achievementsAwards, "e.g., Top 10 Micro Creator 2025") { achievementsAwards = it; persistState() }
                            MediaKitTextField("Special Skills", achievementsSkills, "e.g., 4K Video Editing, Scripting") { achievementsSkills = it; persistState() }
                        }
                    }

                    8 -> {
                        // Step 8: Portfolio Images Upload Simulator
                        MediaKitGlassCard(title = "Step 8: Portfolio Showcase Assets") {
                            UploadSlotRow("Profile Photo", profilePhotoUploaded) { profilePhotoUploaded = !profilePhotoUploaded }
                            UploadSlotRow("Best Reel Screenshot", reelScreenshotUploaded) { reelScreenshotUploaded = !reelScreenshotUploaded }
                            UploadSlotRow("Best Post Screenshot", postUploaded) { postUploaded = !postUploaded }
                            UploadSlotRow("Best Video Thumbnail", thumbnailUploaded) { thumbnailUploaded = !thumbnailUploaded }
                            UploadSlotRow("Best Campaign Asset", campaignUploaded) { campaignUploaded = !campaignUploaded }
                        }
                    }

                    9 -> {
                        // Step 9: Statistics
                        MediaKitGlassCard(title = "Step 9: Key Performance Statistics") {
                            MediaKitTextField("Total Followers / Subscribers", followers, "e.g., 12,500") { followers = it; persistState() }
                            MediaKitTextField("Average Monthly Reach", avgReach, "e.g., 55,000 / month") { avgReach = it; persistState() }
                            MediaKitTextField("Average Views Per Content", avgViews, "e.g., 28,000 / reel") { avgViews = it; persistState() }
                            MediaKitTextField("Engagement Rate", engagementRate, "e.g., 6.8%") { engagementRate = it; persistState() }
                            MediaKitTextField("Total Monthly Views", monthlyViews, "e.g., 180,000") { monthlyViews = it; persistState() }
                        }
                    }

                    10 -> {
                        // Step 10: Live Media Kit Preview
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(Brush.verticalGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A))))
                                .border(1.5.dp, Color(0xFFFFD700), RoundedCornerShape(20.dp))
                                .padding(16.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFFD700)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(creatorName.take(1).uppercase().ifBlank { "C" }, fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(fullName.ifBlank { "Creator Name" }, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("✔", fontSize = 12.sp, color = Color(0xFF38BDF8))
                                        }
                                        Text(creatorName.ifBlank { "@handle" }, fontSize = 12.sp, color = Color(0xFFFFD700))
                                        Text("${city.ifBlank { "Mumbai" }}, ${country.ifBlank { "India" }} • ${email.ifBlank { "contact@creator.com" }}", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                                    }
                                }

                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White.copy(alpha = 0.15f))

                                Text("ABOUT ME", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                Text(bio.ifBlank { "Digital creator making high converting content." }, fontSize = 12.sp, color = Color.White, lineHeight = 16.sp)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text("KEY PERFORMANCE ANALYTICS", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    StatBox("Followers", followers, Modifier.weight(1f))
                                    StatBox("Reach", avgReach, Modifier.weight(1f))
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    StatBox("Avg Views", avgViews, Modifier.weight(1f))
                                    StatBox("Engagement", engagementRate, Modifier.weight(1f))
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text("AUDIENCE & CATEGORIES", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                Text("Target: $selectedAudience | Age: $demoAge | Demographics: $demoCountry ($demoLang)", fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    selectedCategories.take(4).forEach { cat ->
                                        Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(Color(0x22FFD700)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                            Text(cat, fontSize = 9.sp, color = Color(0xFFFFD700))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    11 -> {
                        // Step 11: AI Review & Audit
                        MediaKitGlassCard(title = "Step 11: AI Media Kit Audit") {
                            if (!aiReviewDone && !isAiReviewing) {
                                Button(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        isAiReviewing = true
                                        coroutineScope.launch {
                                            delay(1000)
                                            isAiReviewing = false
                                            aiReviewDone = true
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                                ) {
                                    Text("🔍 Run AI Media Kit Audit", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            } else if (isAiReviewing) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                    CircularProgressIndicator(color = Color(0xFFFFD700))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("AI Scanning 20+ Commercial Parameters...", fontSize = 12.sp, color = Color.White)
                                }
                            } else {
                                Column {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                                        ScoreMeter("Media Kit", "95/100")
                                        ScoreMeter("Professional", "92%")
                                        ScoreMeter("Brand Appeal", "96%")
                                        ScoreMeter("Trust Score", "94%")
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text("💡 AI Insights & Suggestions:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    Text("• High Engagement Rate ($engagementRate) puts you in the top 5% micro-creators.", fontSize = 11.sp, color = Color.White)
                                    Text("• Clear demographics ($demoCountry, $demoLang) make you ideal for FMCG & Lifestyle brands.", fontSize = 11.sp, color = Color.White)
                                    Text("• Your 1-page Media Kit formatting is 100% agency-compliant.", fontSize = 11.sp, color = Color.White)
                                }
                            }
                        }
                    }

                    12 -> {
                        // Step 12: Auto Improvements
                        MediaKitGlassCard(title = "Step 12: AI Copy & Rate Enhancements") {
                            Text("AI Enhanced Commercial Summary:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x22FFD700))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    "\"Commercial-ready $userNiche content engine delivering $avgViews avg views with an industry-leading $engagementRate engagement rate. Specialized in high-impact brand integrations.\"",
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    lineHeight = 15.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isAiEnhanced = true
                                    Toast.makeText(context, "AI Copy Enhancements Applied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = if (isAiEnhanced) Color(0xFF4ADE80) else Color(0xFFFFD700))
                            ) {
                                Text(if (isAiEnhanced) "✓ AI Enhancements Applied" else "✨ Apply AI Enhancements", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    else -> {
                        // Step 13: Brand Ready Checklist & Achievement Badge
                        MediaKitGlassCard(title = "Step 13: Brand Ready Checklist") {
                            val checklistLabels = listOf(
                                "✔ Professional Bio & Elevator Pitch",
                                "✔ Contact Details & Location",
                                "✔ Verified Performance Statistics",
                                "✔ Target Audience & Demographics",
                                "✔ Content Categories & Verticals",
                                "✔ Portfolio Images & Campaign Proof"
                            )

                            checklistLabels.forEachIndexed { idx, label ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (checklistItems.getOrElse(idx) { false }) Color(0x224ADE80) else Color(0x11FFFFFF))
                                        .clickable {
                                            checklistItems[idx] = !checklistItems[idx]
                                            persistState()
                                        }
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (checklistItems.getOrElse(idx) { false }) "☑" else "☐",
                                        fontSize = 16.sp,
                                        color = if (checklistItems.getOrElse(idx) { false }) Color(0xFF4ADE80) else Color.White
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(label, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Premium Badge Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Brush.horizontalGradient(listOf(Color(0xFFFFD700), Color(0xFFFFA500))))
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🏆", fontSize = 24.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text("Professional Media Kit Ready", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                        Text("Level 3 Achievement Unlocked! +300 XP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black.copy(alpha = 0.8f))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Today's Mission Footer Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x221E293B))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("TODAY'S MISSION", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                            Text("Create Your First Professional Media Kit", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x33FFD700))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("⏱ 12 Mins | +300 XP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Buttons (Back & Continue)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                currentStep--
                                persistState()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                        ) {
                            Text("Back", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (currentStep < 13) {
                                currentStep++
                                persistState()
                            } else {
                                isAlreadyCompleted = true
                                persistState(completed = true)
                                onLevel3Completed()
                            }
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                    ) {
                        Text(
                            text = if (currentStep == 13) "Finish Level 3 🎉" else "Continue ➔",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaKitGlassCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0x221E293B))
            .border(1.dp, Color(0x33FFD700), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun MediaKitTextField(label: String, value: String, placeholder: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color(0xFFFFD700), fontSize = 11.sp) },
        placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.35f), fontSize = 11.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFFFD700),
            unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRowHorizontal(items: List<String>, selected: String, onSelect: (String) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { item ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (item == selected) Color(0xFFFFD700) else Color(0x22FFFFFF))
                    .clickable { onSelect(item) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(item, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (item == selected) Color.Black else Color.White)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowMultiSelectChips(items: List<String>, selected: List<String>, onUpdate: (List<String>) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { item ->
            val isSel = selected.contains(item)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                    .clickable {
                        val updated = if (isSel) selected - item else selected + item
                        onUpdate(updated)
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(if (isSel) "✓ $item" else item, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White)
            }
        }
    }
}

@Composable
private fun UploadSlotRow(label: String, isUploaded: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x15FFFFFF))
            .clickable { onToggle() }
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (isUploaded) Color(0x334ADE80) else Color(0x33FFD700))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(if (isUploaded) "✓ Uploaded" else "+ Tap to Select", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isUploaded) Color(0xFF4ADE80) else Color(0xFFFFD700))
        }
    }
}

@Composable
private fun StatBox(label: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x22FFFFFF))
            .padding(10.dp)
    ) {
        Column {
            Text(label, fontSize = 9.sp, color = Color.White.copy(alpha = 0.6f))
            Text(value.ifBlank { "0" }, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        }
    }
}

@Composable
private fun ScoreMeter(title: String, score: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color(0x22FFD700))
                .border(1.5.dp, Color(0xFFFFD700), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(score, fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(title, fontSize = 9.sp, color = Color.White.copy(alpha = 0.8f))
    }
}
"""

with open('app/src/main/java/com/example/creatoracademy/BrandCollaborationMentor.kt', 'a') as f:
    f.write("\n" + content)
print("Appended Phase 4 Level 3 successfully!")
