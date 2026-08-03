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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Path
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
private fun PortfolioGlassCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0x221E293B))
            .border(1.dp, Color(0x33FFD700), RoundedCornerShape(18.dp))
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
private fun PortfolioTextField(
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

data class CampaignEntry(
    val id: String,
    val campaignName: String,
    val brandName: String,
    val contentType: String,
    val performance: String,
    val completionDate: String,
    val notes: String
)

data class TestimonialEntry(
    val brandName: String,
    val clientName: String,
    val review: String,
    val rating: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandCollabLevel12AIPortfolioBuilderView(
    userNiche: String,
    userPlatform: String,
    userName: String = "Creator",
    onLevel12Completed: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current

    val savedData = remember { CreatorAcademyPrefs.getBrandCollabLevel12Data(context) }
    var currentModule by remember { mutableIntStateOf((savedData["module"] as? Int) ?: 1) }
    var currentStep by remember { mutableIntStateOf((savedData["step"] as? Int) ?: 1) }
    var isAlreadyCompleted by remember { mutableStateOf((savedData["completed"] as? Boolean) ?: false) }

    // Module 1: Creator Profile State
    var creatorName by remember { mutableStateOf((savedData["portfolioName"] as? String).takeIf { !it.isNullOrBlank() } ?: userName) }
    var creatorBio by remember { mutableStateOf((savedData["portfolioBio"] as? String).takeIf { !it.isNullOrBlank() } ?: "Tech & Lifestyle Creator crafting high-converting brand stories.") }
    var creatorPlatform by remember { mutableStateOf(userPlatform.ifBlank { "Instagram & YouTube" }) }
    var creatorNiche by remember { mutableStateOf(userNiche.ifBlank { "Tech & Lifestyle" }) }
    var followerCount by remember { mutableStateOf("125,000+ Engaged Followers") }
    var contactEmail by remember { mutableStateOf("collabs@creatorstudio.com") }

    // Module 3: Case Study Builder State
    var csChallenge by remember { mutableStateOf("Boat Audio wanted to break through noisy wireless earbud market during Diwali sale season.") }
    var csStrategy by remember { mutableStateOf("Created 3-part unboxing story arc + cinematic reel focusing on active noise cancellation in crowded Indian streets.") }
    var csExecution by remember { mutableStateOf("Shot in 4K 60fps with real street noise test. Integrated custom 15% discount code 'BOATXCREATOR'.") }
    var csResults by remember { mutableStateOf("1.4 Million Views, 85,000 Engagement, ₹4.8 Lakh Sales Generated within 7 days.") }
    var csLessons by remember { mutableStateOf("Real-world noise test resonated 3x higher than studio feature breakdown.") }

    // Module 4: Campaign Showcase State
    val campaignList = remember {
        mutableStateListOf(
            CampaignEntry("1", "Nirvana Ion Launch", "Boat Audio", "Instagram Reel & Story", "1.4M Views • 8.4% Engagement", "Aug 2026", "High CTR sales campaign"),
            CampaignEntry("2", "Festive Apparel Edit", "Snitch Clothing", "Carousel & Shorts Cut", "850K Views • 12K Link Clicks", "Jul 2026", "Top converting fashion reel"),
            CampaignEntry("3", "Clear Skin Routine", "Minimalist Skincare", "YouTube Integration", "420K Views • 6.2% CTR", "May 2026", "Long term skincare review")
        )
    }

    // Module 5: Testimonials State
    val testimonialsList = remember {
        mutableStateListOf(
            TestimonialEntry("Boat Audio", "Rohan Sharma (Brand Mgr)", "\"One of the most professional creators we worked with this year. Delivered 24 hours ahead of deadline!\"", "5/5 Stars ⭐"),
            TestimonialEntry("Snitch", "Priya Verma (Marketing Lead)", "\"Content quality was top tier. Overachieved sales targets by 140%.\"", "5/5 Stars ⭐")
        )
    }

    // Module 7: Skills State
    val skillsMap = remember {
        mutableStateListOf(
            "Video Editing" to true,
            "Storytelling" to true,
            "Photography" to true,
            "UGC Creation" to true,
            "Product Review" to true,
            "Voice Over" to false,
            "Script Writing" to true,
            "Short Form Content" to true,
            "Long Form Content" to false,
            "Content Strategy" to true
        )
    }

    // Module 10: Mission Checklist State
    val missionChecklist = remember {
        mutableStateListOf(
            true, // Creator Profile Info Verified
            true, // Portfolio Sections Organized
            true, // Detailed Case Study Built
            false, // Campaign Showcase Timeline Populated
            false // Testimonials & Skills Cloud Configured
        )
    }

    // Show Full Portfolio Preview Modal / View Toggle
    var showLivePortfolioPreview by remember { mutableStateOf(false) }

    // Persist helper
    fun persistState(completed: Boolean = isAlreadyCompleted) {
        CreatorAcademyPrefs.saveBrandCollabLevel12State(
            context = context,
            step = currentStep,
            module = currentModule,
            portfolioName = creatorName,
            portfolioBio = creatorBio,
            isCompleted = completed
        )
    }

    // Progress percentage fixed at 99% for Level 12 AI Portfolio Builder
    val progressPercent = if (isAlreadyCompleted || currentModule == 10) 100 else 99
    val progressRingAngle by animateFloatAsState(
        targetValue = (progressPercent / 100f) * 360f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "level12ProgressRing"
    )

    // Background animation
    val infiniteTransition = rememberInfiniteTransition(label = "portfolioBg")
    val floatingOffsetY by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatingOffsetPortfolio"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF0F172A))))
    ) {
        // Floating Golden Particles & Portfolio Page Graphics
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(Color(0xFFFFD700).copy(alpha = 0.20f), radius = 12.dp.toPx(), center = Offset(w * 0.15f, h * 0.15f + floatingOffsetY * 2f))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.15f), radius = 16.dp.toPx(), center = Offset(w * 0.85f, h * 0.28f - floatingOffsetY * 2.2f))
            drawCircle(Color(0x3338BDF8), radius = 14.dp.toPx(), center = Offset(w * 0.22f, h * 0.75f + floatingOffsetY * 2.8f))
            drawCircle(Color(0x224ADE80), radius = 18.dp.toPx(), center = Offset(w * 0.82f, h * 0.88f - floatingOffsetY * 2.5f))

            // Portfolio Card Line Graphic
            val cardPath = Path().apply {
                moveTo(w * 0.1f, h * 0.42f)
                lineTo(w * 0.9f, h * 0.42f)
                lineTo(w * 0.9f, h * 0.46f)
                lineTo(w * 0.1f, h * 0.46f)
                close()
            }
            drawPath(path = cardPath, color = Color(0x11FFD700))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // HEADER: AI Portfolio Builder
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
                        if (showLivePortfolioPreview) {
                            showLivePortfolioPreview = false
                        } else if (currentModule > 1) {
                            currentModule--
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
                            text = "AI Portfolio Builder",
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
                            Text("LEVEL 12", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                        }
                    }
                    Text(
                        text = "Build A Portfolio That Wins Brand Deals",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }

                // 99% Premium Animated Progress Ring
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

            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                if (!showLivePortfolioPreview) {
                    // Floating Brand Logos Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(" Boat", " Snitch", " Minimalist", " Myntra", " Amazon", " Nykaa", " Samsung", " OnePlus").forEach { logo ->
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

                    // AI MENTOR CARD
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
                                    Text("• Portfolio Architect", fontSize = 9.sp, color = Color(0xFF4ADE80))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                val speech = when (currentModule) {
                                    1 -> "Professional creators sirf content nahi dikhate... Woh apni achievements ko portfolio ki form mein present karte hain. Aaj hum tumhara premium creator portfolio banayenge."
                                    2 -> "Portfolio structure: About Me, My Story, Niche, Services, Audience Demographics, Achievements, Campaigns & Case Studies."
                                    3 -> "Case Study Structure: Challenge → Strategy → Execution → Results → Lessons Learned. Yeh format brand managers ko instant convince karta hai!"
                                    4 -> "Campaign Showcase: Direct proof of your past successful collaborations with view counts and engagement numbers."
                                    5 -> "Testimonials add 10x trust. Brand Manager quotes and review screenshots establish high credibility."
                                    6 -> "Achievements Timeline: Display your journey milestones from your first deal to highest payment unlocked!"
                                    7 -> "Select your core skills cloud: Video Editing, Storytelling, UGC, Script Writing & Content Strategy."
                                    8 -> "Portfolio Score Gauge: AI calculates your portfolio readiness score across 5 crucial parameters!"
                                    9 -> "AI Suggestions: Personalized recommendations to fix weak areas and highlight high-converting metrics."
                                    10 -> "Today's Mission: Complete Your Professional Portfolio (Est. 15 Mins). Unlock your Creator Portfolio Badge!"
                                    else -> "Shaandar! Tumhara AI Portfolio Masterpiece ready hai. Share this live link with brands to close high-ticket deals!"
                                }
                                Text(
                                    text = speech,
                                    fontSize = 11.5.sp,
                                    color = Color.White,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Module Navigation Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(bottom = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val modules = listOf(
                            1 to "1. Profile",
                            2 to "2. Sections",
                            3 to "3. Case Study",
                            4 to "4. Showcase",
                            5 to "5. Reviews",
                            6 to "6. Timeline",
                            7 to "7. Skills",
                            8 to "8. Score",
                            9 to "9. AI Tips",
                            10 to "10. Mission"
                        )
                        modules.forEach { (modNum, modLabel) ->
                            val isSelected = currentModule == modNum
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                    .clickable {
                                        currentModule = modNum
                                        persistState()
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = modLabel,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.Black else Color.White
                                )
                            }
                        }
                    }

                    // MODULE CONTENT CASES
                    when (currentModule) {
                        // MODULE 1: CREATOR PROFILE
                        1 -> {
                            PortfolioGlassCard(title = "Module 1: Creator Profile Details") {
                                Text("Auto-populated from your account profile (editable):", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(8.dp))

                                PortfolioTextField("Creator Name", creatorName, "e.g. Omkar Yadav") { creatorName = it; persistState() }
                                PortfolioTextField("Creator Bio", creatorBio, "Short professional bio...") { creatorBio = it; persistState() }
                                PortfolioTextField("Primary Platform", creatorPlatform, "e.g. Instagram & YouTube") { creatorPlatform = it; persistState() }
                                PortfolioTextField("Niche / Category", creatorNiche, "e.g. Tech & Lifestyle") { creatorNiche = it; persistState() }
                                PortfolioTextField("Followers / Reach", followerCount, "e.g. 125,000+ Followers") { followerCount = it; persistState() }
                                PortfolioTextField("Contact Email for Collabs", contactEmail, "e.g. collabs@domain.com") { contactEmail = it; persistState() }

                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        persistState()
                                        Toast.makeText(context, "Profile saved automatically!", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("💾 Save Profile Data", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }

                        // MODULE 2: PORTFOLIO SECTIONS
                        2 -> {
                            PortfolioGlassCard(title = "Module 2: Portfolio Core Structure") {
                                Text("Your portfolio includes these 9 high-converting sections:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                val sections = listOf(
                                    "👤 About Me" to "Who you are & creator mission statement",
                                    "📖 My Story" to "Journey from 0 to 100k+ followers",
                                    "🎯 My Niche" to "Core audience demographic & content pillar",
                                    "🛠 Services" to "Reels, Unboxing, Integration & UGC licensing",
                                    "📊 Audience" to "Age distribution, gender split & top cities",
                                    "🏆 Achievements" to "Brand milestones & revenue records",
                                    "📁 Campaigns" to "Visual archive of top performing content",
                                    "📈 Results" to "Impressions, engagement rates & ROI data",
                                    "📬 Contact" to "Direct email & booking media kit link"
                                )

                                sections.forEach { (secTitle, secDesc) ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x22FFFFFF))
                                            .padding(10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(secTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                                Text(secDesc, fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.7f))
                                            }
                                            Text("✅ Active", fontSize = 10.sp, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        // MODULE 3: CASE STUDY BUILDER
                        3 -> {
                            PortfolioGlassCard(title = "Module 3: AI Case Study Builder") {
                                Text("Format your top collaboration into a professional case study:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(8.dp))

                                PortfolioTextField("1. Challenge", csChallenge, "What problem did the brand have?") { csChallenge = it }
                                PortfolioTextField("2. Strategy", csStrategy, "What was your creative approach?") { csStrategy = it }
                                PortfolioTextField("3. Execution", csExecution, "How was content shot & edited?") { csExecution = it }
                                PortfolioTextField("4. Results", csResults, "Views, CTR, sales generated...") { csResults = it }
                                PortfolioTextField("5. Lessons Learned", csLessons, "Key takeaway for future deals...") { csLessons = it }
                            }
                        }

                        // MODULE 4: CAMPAIGN SHOWCASE
                        4 -> {
                            PortfolioGlassCard(title = "Module 4: Campaign Showcase Timeline") {
                                Text("Your archived brand collaborations timeline:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                campaignList.forEach { item ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0x22FFFFFF))
                                            .border(1.dp, Color(0x33FFD700), RoundedCornerShape(12.dp))
                                            .padding(10.dp)
                                    ) {
                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(item.campaignName, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                Text(item.completionDate, fontSize = 10.sp, color = Color(0xFFFFD700))
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text("Brand: ${item.brandName} • Type: ${item.contentType}", fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.7f))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("📈 Performance: ${item.performance}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4ADE80))
                                        }
                                    }
                                }
                            }
                        }

                        // MODULE 5: TESTIMONIALS
                        5 -> {
                            PortfolioGlassCard(title = "Module 5: Brand Manager Testimonials") {
                                Text("Client reviews & social proof:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                testimonialsList.forEach { item ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0x22FFFFFF))
                                            .padding(10.dp)
                                    ) {
                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(item.brandName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                                Text(item.rating, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            }
                                            Text("Contact: ${item.clientName}", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(item.review, fontSize = 11.sp, color = Color.White, lineHeight = 15.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // MODULE 6: ACHIEVEMENTS TIMELINE
                        6 -> {
                            PortfolioGlassCard(title = "Module 6: Creator Achievements Timeline") {
                                Text("Animated milestone journey from 0 to pro:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                val milestones = listOf(
                                    "🎉 First Brand Deal Unlocked" to "Signed first paid collaboration with local tech brand (₹5,000)",
                                    "🔥 Top Viral Campaign" to "Nirvana Ion Launch Reel crossed 1.4M organic views",
                                    "💰 Highest Single Payment" to "Earned ₹45,000 for 30-day UGC licensing deal",
                                    "📈 Most Viewed Campaign" to "Accumulated over 3.5 Million total impressions in Q2 2026",
                                    "🤝 Long-Term Retainer Partnership" to "Signed 3-month continuous content retainer with Snitch",
                                    "🏆 Best Creator Milestone" to "Unlocked 'Campaign Manager' & 'Portfolio Master' Status"
                                )

                                milestones.forEachIndexed { idx, (mTitle, mDesc) ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFFFD700)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("★", fontSize = 12.sp, color = Color.Black)
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(mTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            Text(mDesc, fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.7f))
                                        }
                                    }
                                }
                            }
                        }

                        // MODULE 7: CREATOR SKILLS
                        7 -> {
                            PortfolioGlassCard(title = "Module 7: Creator Core Skills Cloud") {
                                Text("Select skill tags to feature on your public portfolio:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    skillsMap.forEachIndexed { idx, (skillName, isSel) ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(20.dp))
                                                .background(if (isSel) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                                .border(1.dp, if (isSel) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                                                .clickable {
                                                    skillsMap[idx] = skillName to !isSel
                                                }
                                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = if (isSel) "✓ $skillName" else "+ $skillName",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSel) Color.Black else Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // MODULE 8: PORTFOLIO SCORE
                        8 -> {
                            PortfolioGlassCard(title = "Module 8: AI Portfolio Readiness Score") {
                                Text("Comprehensive score evaluated across 5 key parameters:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0x33FFD700))
                                        .padding(14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("OVERALL PORTFOLIO SCORE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("96 / 100", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                        Text("GRADE A+ • READY TO PITCH TOP BRANDS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4ADE80))
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                val scoreBreakdown = listOf(
                                    "Professionalism & Bio Structure" to "98%",
                                    "Visual Quality & Case Studies" to "95%",
                                    "Track Record & Campaign Experience" to "92%",
                                    "Brand Communication & Testimonials" to "96%",
                                    "Trust Level & Social Proof" to "97%"
                                )

                                scoreBreakdown.forEach { (param, pct) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(param, fontSize = 11.sp, color = Color.White)
                                        Text(pct, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    }
                                }
                            }
                        }

                        // MODULE 9: AI SUGGESTIONS
                        9 -> {
                            PortfolioGlassCard(title = "Module 9: AI Optimization Suggestions") {
                                Text("Personalized recommendations to maximize portfolio deal conversions:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                val suggestions = listOf(
                                    "💡 Add Direct WhatsApp / Email Booking Link" to "Allow brand managers to initiate a conversation with 1-click.",
                                    "💡 Highlight High CTR in First 3 Seconds" to "Mention 8.4% engagement rate prominently in the header hero section.",
                                    "💡 Include Video Reel Embedded Previews" to "Attach high resolution 4K thumbnail previews for all campaigns.",
                                    "💡 Add Downloadable Media Kit PDF" to "Provide a downloadable PDF rate card alongside live web link."
                                )

                                suggestions.forEach { (title, detail) ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x22FFFFFF))
                                            .padding(10.dp)
                                    ) {
                                        Column {
                                            Text(title, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(detail, fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.8f))
                                        }
                                    }
                                }
                            }
                        }

                        // MODULE 10: TODAY'S MISSION & ACHIEVEMENT
                        10 -> {
                            PortfolioGlassCard(title = "Module 10: Today's Mission & XP Reward") {
                                Text("Complete Your Professional Portfolio (Estimated Time: 15 Minutes)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                Spacer(modifier = Modifier.height(8.dp))

                                val missionTasks = listOf(
                                    "Creator Profile info verified & bio updated",
                                    "Portfolio sections organized (About, Services, Audience)",
                                    "Detailed case study built (Challenge → Results)",
                                    "Campaign showcase timeline populated",
                                    "Testimonials & core skills cloud configured"
                                )

                                missionTasks.forEachIndexed { idx, task ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (missionChecklist[idx]) Color(0x224ADE80) else Color(0x11FFFFFF))
                                            .clickable { missionChecklist[idx] = !missionChecklist[idx] }
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = missionChecklist[idx],
                                            onCheckedChange = { checked -> missionChecklist[idx] = checked },
                                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4ADE80), uncheckedColor = Color.White)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(task, fontSize = 11.sp, color = if (missionChecklist[idx]) Color(0xFF4ADE80) else Color.White)
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // PREMIUM GLASS BADGE & XP REWARD
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                    .background(Brush.linearGradient(listOf(Color(0x44FFD700), Color(0x22FFD700))))
                                    .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(16.dp))
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("🏆 ACHIEVEMENT UNLOCKED", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Creator Portfolio Ready", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("+950 XP Reward • Level 12 AI Portfolio Builder Completed", fontSize = 11.sp, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        showLivePortfolioPreview = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFD700)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                ) {
                                    Text("👁 Preview Live Portfolio", color = Color(0xFFFFD700), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }

                                Button(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        isAlreadyCompleted = true
                                        persistState(completed = true)
                                        onLevel12Completed()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                ) {
                                    Text("FINISH LEVEL 12 🎉", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            } else {
                // FINAL PORTFOLIO PREVIEW MODE (APPLE STYLE GLASS LAYOUT)
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Brush.verticalGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A))))
                            .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFFD700)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("👑", fontSize = 26.sp)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(creatorName, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                        Text("$creatorNiche Creator • $creatorPlatform", fontSize = 11.sp, color = Color(0xFFFFD700))
                                        Text(followerCount, fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.7f))
                                    }
                                }

                                Button(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString("https://creatorstudio.portfolio/$creatorName"))
                                        Toast.makeText(context, "Portfolio Link Copied to Clipboard! 📋", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("🔗 Share", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Text("Bio", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Text(creatorBio, fontSize = 11.5.sp, color = Color.White, lineHeight = 16.sp)

                            Spacer(modifier = Modifier.height(12.dp))

                            // Case Study Highlight
                            Text("Featured Case Study", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x22FFFFFF))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text("Challenge: $csChallenge", fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("Results: $csResults", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4ADE80))
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Skills Cloud
                            Text("Core Skills", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(4.dp))
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                skillsMap.filter { it.second }.forEach { (sName, _) ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0x33FFD700))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(sName, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { showLivePortfolioPreview = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Close Preview & Return to Builder", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (!showLivePortfolioPreview) {
                // BOTTOM NAVIGATION BUTTONS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (currentModule > 1) {
                                currentModule--
                                persistState()
                            } else {
                                onBack()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("Back", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (currentModule < 10) {
                                currentModule++
                                persistState()
                            } else {
                                isAlreadyCompleted = true
                                persistState(completed = true)
                                onLevel12Completed()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                    ) {
                        Text(
                            text = if (currentModule < 10) "Continue →" else "Finish Level 12 🎉",
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
}
