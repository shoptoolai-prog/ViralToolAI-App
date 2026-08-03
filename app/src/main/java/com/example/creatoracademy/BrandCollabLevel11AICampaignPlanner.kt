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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun CampaignGlassCard(
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
private fun CampaignTextField(
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

data class DeliverableItem(
    val name: String,
    var isSelected: Boolean
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandCollabLevel11AICampaignPlannerView(
    userNiche: String,
    userPlatform: String,
    userName: String = "Creator",
    onLevel11Completed: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current

    val savedData = remember { CreatorAcademyPrefs.getBrandCollabLevel11Data(context) }
    var currentModule by remember { mutableIntStateOf((savedData["module"] as? Int) ?: 1) }
    var currentStep by remember { mutableIntStateOf((savedData["step"] as? Int) ?: 1) }
    var isAlreadyCompleted by remember { mutableStateOf((savedData["completed"] as? Boolean) ?: false) }

    // Module 1: Campaign Overview State
    var campaignName by remember { mutableStateOf((savedData["campaignName"] as? String) ?: "Nirvana Ion Launch") }
    var brandName by remember { mutableStateOf((savedData["brandName"] as? String) ?: "Boat Audio") }
    var campaignPlatform by remember { mutableStateOf(userPlatform.ifBlank { "Instagram" }) }
    var campaignType by remember { mutableStateOf("Product Launch Review") }
    var campaignTimeline by remember { mutableStateOf("14 Days (Aug 10 - Aug 24)") }

    // Module 2: Deliverables Checklist State
    val deliverablesList = remember {
        mutableStateListOf(
            DeliverableItem("Instagram Reel (4K 60fps)", true),
            DeliverableItem("Instagram Story (2x with Swipe Link)", true),
            DeliverableItem("Instagram Feed Post / Carousel", false),
            DeliverableItem("YouTube Video Integration (60s)", false),
            DeliverableItem("YouTube Shorts Cut", true),
            DeliverableItem("UGC Video Raw License", false),
            DeliverableItem("Custom Brand Unboxing Story", true)
        )
    }

    // Module 5: Revision Tracker State
    var revision1Note by remember { mutableStateOf("Adjusted background audio level to -18dB. Logo overlay added at 00:03.") }
    var revision2Note by remember { mutableStateOf("Brand approved color grading. Fixed CTA caption tag @boat.life.") }
    var finalApprovalStatus by remember { mutableStateOf("APPROVED BY BRAND MANAGER") }

    // Module 6: Deadline Manager State
    var daysRemaining by remember { mutableIntStateOf(5) }
    val todayTasks = remember {
        mutableStateListOf(
            "Finalize rough cut video editing",
            "Send draft link via WeTransfer to Rohan (Boat Mgr)",
            "Prepare invoice PDF for 50% milestone payment"
        )
    }

    // Module 7: Campaign Checklist State
    val campaignChecklist = remember {
        mutableStateListOf(
            true,  // Deliverables Ready
            true,  // Brand Approved
            true,  // Content Scheduled
            false, // Posted Successfully
            false, // Payment Requested
            false  // Campaign Closed
        )
    }

    // Module 10: Mission Checklist
    val missionChecklist = remember {
        mutableStateListOf(
            true, // Setup Campaign Overview Details
            true, // Configure Required Deliverables
            true, // Generate AI Hooks & Video Outline
            false, // Setup Calendar & Milestone Dates
            false // Complete Campaign Performance Review
        )
    }

    // Persist helper
    fun persistState(completed: Boolean = isAlreadyCompleted) {
        CreatorAcademyPrefs.saveBrandCollabLevel11State(
            context = context,
            step = currentStep,
            module = currentModule,
            campaignName = campaignName,
            brandName = brandName,
            isCompleted = completed
        )
    }

    // Progress percentage fixed at 98% for Level 11 AI Campaign Planner
    val progressPercent = if (isAlreadyCompleted || currentModule == 10) 100 else 98
    val progressRingAngle by animateFloatAsState(
        targetValue = (progressPercent / 100f) * 360f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "level11ProgressRing"
    )

    // Floating background animation
    val infiniteTransition = rememberInfiniteTransition(label = "plannerBg")
    val floatingOffsetY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatingOffsetPlanner"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF0F172A))))
    ) {
        // Floating Golden Particles & Campaign Calendar Line Graphics
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Golden Particles
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.18f), radius = 10.dp.toPx(), center = Offset(w * 0.2f, h * 0.18f + floatingOffsetY * 2.5f))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.14f), radius = 14.dp.toPx(), center = Offset(w * 0.8f, h * 0.32f - floatingOffsetY * 2f))
            drawCircle(Color(0x3338BDF8), radius = 12.dp.toPx(), center = Offset(w * 0.15f, h * 0.72f + floatingOffsetY * 3f))
            drawCircle(Color(0x224ADE80), radius = 16.dp.toPx(), center = Offset(w * 0.88f, h * 0.85f - floatingOffsetY * 2f))

            // Calendar Timeline Wave Graphic Background
            val timelinePath = Path().apply {
                moveTo(0f, h * 0.38f)
                cubicTo(w * 0.25f, h * 0.32f, w * 0.5f, h * 0.44f, w * 0.75f, h * 0.36f)
                cubicTo(w * 0.88f, h * 0.32f, w * 0.95f, h * 0.35f, w, h * 0.30f)
            }
            drawPath(
                path = timelinePath,
                color = Color(0x18FFD700),
                style = Stroke(width = 3.5.dp.toPx())
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // HEADER: AI Campaign Planner
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
                        if (currentModule > 1) {
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
                            text = "AI Campaign Planner",
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
                            Text("LEVEL 11", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                        }
                    }
                    Text(
                        text = "Plan & Deliver Campaigns Like A Pro",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }

                // 98% Premium Animated Progress Ring
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

            // Scrollable Content Body
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
                                Text("• Campaign Director Active", fontSize = 9.sp, color = Color(0xFF4ADE80))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            val speech = when (currentModule) {
                                1 -> "Deal milna sirf shuruaat hai. Professional creators campaign ko time par complete karte hain. Aaj hum complete campaign management seekhenge."
                                2 -> "Brand contract ke deliverables clear checklist me convert karo. Unboxing, Reel, ya Story — 100% precision maintain karo!"
                                3 -> "Planning → Shooting → Editing → Brand Review → Approval → Publishing → Payment. Yeh 8-step calendar campaign pipeline hai!"
                                4 -> "Main tumhare enter kiye campaign parameters ($campaignName - $brandName) ke liye high-converting hooks aur video structure generate kar raha hoon."
                                5 -> "Revision history hamesha clean track karo. Version numbers (v1.1, v1.2) ke saath professional communication maintain karo."
                                6 -> "Deadlines miss karna = Brand trust lose karna. Days remaining countdown and daily micro-tasks track karo!"
                                7 -> "Campaign Checklist: Step-by-step verified execution ensures 100% payment release on time."
                                8 -> "Campaign complete hone par AI Performance Review zaroor run karo taaki next campaign me higher rate negotiate kar sako!"
                                9 -> "Post-campaign thank you email + ROI analytics share karne se 70% brands same creator ko repeat hire karte hain."
                                10 -> "Today's Mission: Complete Your First Campaign Plan (Est. 12 Mins). Build your campaign roadmap now!"
                                else -> "Badhaai ho! Tumhara Campaign Planner fully master ho chuka hai. Single campaign se repeat brand client banao!"
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

                // Module Navigation Horizontal Scroll Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val modules = listOf(
                        1 to "1. Overview",
                        2 to "2. Deliverables",
                        3 to "3. Calendar",
                        4 to "4. AI Ideas",
                        5 to "5. Revisions",
                        6 to "6. Deadlines",
                        7 to "7. Checklist",
                        8 to "8. AI Review",
                        9 to "9. Retention",
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

                // MODULE CASES
                when (currentModule) {
                    // MODULE 1: CAMPAIGN OVERVIEW
                    1 -> {
                        CampaignGlassCard(title = "Module 1: Create Campaign Overview") {
                            Text("Setup primary parameters for your active brand campaign:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(8.dp))

                            CampaignTextField("Campaign Name", campaignName, "e.g. Nirvana Ion Launch") { campaignName = it; persistState() }
                            CampaignTextField("Brand Name", brandName, "e.g. Boat Audio") { brandName = it; persistState() }
                            CampaignTextField("Target Platform", campaignPlatform, "e.g. Instagram / YouTube") { campaignPlatform = it; persistState() }
                            CampaignTextField("Campaign Type", campaignType, "e.g. Product Launch / Review") { campaignType = it; persistState() }
                            CampaignTextField("Campaign Timeline & Duration", campaignTimeline, "e.g. 14 Days (Aug 10 - Aug 24)") { campaignTimeline = it; persistState() }

                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    persistState()
                                    Toast.makeText(context, "Campaign parameters saved!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("💾 Save Campaign Overview", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }

                    // MODULE 2: DELIVERABLES CHECKLIST
                    2 -> {
                        CampaignGlassCard(title = "Module 2: Deliverables Checklist") {
                            Text("Select agreed content formats for '$campaignName' ($brandName):", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(8.dp))

                            deliverablesList.forEachIndexed { idx, item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (item.isSelected) Color(0x22FFD700) else Color(0x11FFFFFF))
                                        .clickable {
                                            deliverablesList[idx] = item.copy(isSelected = !item.isSelected)
                                        }
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = item.isSelected,
                                        onCheckedChange = { checked ->
                                            deliverablesList[idx] = item.copy(isSelected = checked)
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFFD700), uncheckedColor = Color.White)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(item.name, fontSize = 11.5.sp, fontWeight = if (item.isSelected) FontWeight.Bold else FontWeight.Normal, color = if (item.isSelected) Color(0xFFFFD700) else Color.White)
                                }
                            }
                        }
                    }

                    // MODULE 3: CAMPAIGN CALENDAR
                    3 -> {
                        CampaignGlassCard(title = "Module 3: Campaign Milestone Calendar") {
                            Text("Sequential timeline stages for $brandName execution:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(10.dp))

                            val calendarSteps = listOf(
                                "1. Content Planning" to "Script writing & concept hook definition",
                                "2. Shooting" to "High quality 4K video capture & product B-roll",
                                "3. Editing" to "Color grading, captions & audio sync",
                                "4. Brand Review" to "Submit draft link via WeTransfer to Brand Manager",
                                "5. Revisions" to "Incorporate brand feedback within 24 hours",
                                "6. Final Approval" to "Get written sign-off from brand",
                                "7. Publishing" to "Post video at optimal peak engagement hour",
                                "8. Payment" to "Raise final invoice & share campaign analytics"
                            )

                            calendarSteps.forEachIndexed { idx, (stepTitle, desc) ->
                                Row(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(if (idx < 4) Color(0xFFFFD700) else Color(0x22FFFFFF)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("${idx + 1}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (idx < 4) Color.Black else Color.White)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(stepTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Text(desc, fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.7f))
                                    }
                                }
                            }
                        }
                    }

                    // MODULE 4: AI CONTENT PLANNER
                    4 -> {
                        CampaignGlassCard(title = "Module 4: AI Content & Creative Direction") {
                            Text("AI generated creative blueprint tailored for $campaignName:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(10.dp))

                            val creativePlan = listOf(
                                "⚡ High-Converting Hook" to "\"Wait... Did $brandName just solve the biggest problem in $userNiche? 😱\"",
                                "🎬 Video Structure" to "0-3s: Visual Hook → 3-15s: Unboxing/Problem → 15-40s: Key Features in Action → 40-50s: Personal Verdict → 50-60s: CTA",
                                "📱 Story Sequence" to "Story 1: Teaser Poll → Story 2: Feature Highlight + Swipe Link → Story 3: Exclusive Coupon Code",
                                "🎯 CTA Suggestion" to "\"Comment 'BOAT' below for the direct link + exclusive 15% discount code!\"",
                                "🎨 Creative Direction" to "Lighting: Warm golden glow. Background: Clean desk aesthetic. Audio: Upbeat trending bass beat."
                            )

                            creativePlan.forEach { (heading, detail) ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0x22FFFFFF))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(heading, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(detail, fontSize = 11.sp, color = Color.White, lineHeight = 15.sp)
                                    }
                                }
                            }
                        }
                    }

                    // MODULE 5: REVISION TRACKER
                    5 -> {
                        CampaignGlassCard(title = "Module 5: Revision Tracker & Feedback Log") {
                            Text("Maintain version control history professionally:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(8.dp))

                            CampaignTextField("Revision 1 Log", revision1Note, "e.g. Audio level adjusted") { revision1Note = it }
                            CampaignTextField("Revision 2 Log", revision2Note, "e.g. Tagged brand handle") { revision2Note = it }
                            CampaignTextField("Approval Status", finalApprovalStatus, "e.g. APPROVED BY BRAND MANAGER") { finalApprovalStatus = it }

                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x224ADE80))
                                    .padding(10.dp)
                            ) {
                                Text("✅ Pro Tip: Never delete revision logs. Keeps clear proof of agreed brand modifications.", fontSize = 10.5.sp, color = Color(0xFF4ADE80))
                            }
                        }
                    }

                    // MODULE 6: DEADLINE MANAGER
                    6 -> {
                        CampaignGlassCard(title = "Module 6: Campaign Deadline Manager") {
                            Text("Delivery countdown and daily execution status:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x33FFD700))
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("⏳ DAYS REMAINING TO POSTING DEADLINE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("$daysRemaining Days", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                    Text("Target Posting Date: Aug 20, 2026 (6:00 PM IST)", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Today's Actionable Tasks:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            todayTasks.forEachIndexed { idx, task ->
                                Row(
                                    modifier = Modifier.padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("• ", color = Color(0xFFFFD700), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text(task, fontSize = 11.sp, color = Color.White)
                                }
                            }
                        }
                    }

                    // MODULE 7: CAMPAIGN CHECKLIST
                    7 -> {
                        CampaignGlassCard(title = "Module 7: Master Campaign Verification Checklist") {
                            Text("Ensure every compliance check is satisfied before campaign close:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(10.dp))

                            val checklistItems = listOf(
                                "✔ Deliverables Ready (4K video cut exported)",
                                "✔ Brand Approved (Written email confirmation)",
                                "✔ Content Scheduled (Set for peak engagement)",
                                "✔ Posted Successfully (Live link generated)",
                                "✔ Payment Requested (Tax invoice submitted)",
                                "✔ Campaign Closed (Analytics report sent)"
                            )

                            checklistItems.forEachIndexed { idx, label ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (campaignChecklist[idx]) Color(0x224ADE80) else Color(0x11FFFFFF))
                                        .clickable { campaignChecklist[idx] = !campaignChecklist[idx] }
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = campaignChecklist[idx],
                                        onCheckedChange = { checked -> campaignChecklist[idx] = checked },
                                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4ADE80), uncheckedColor = Color.White)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(label, fontSize = 11.sp, fontWeight = if (campaignChecklist[idx]) FontWeight.Bold else FontWeight.Normal, color = if (campaignChecklist[idx]) Color(0xFF4ADE80) else Color.White)
                                }
                            }
                        }
                    }

                    // MODULE 8: AI PERFORMANCE REVIEW
                    8 -> {
                        CampaignGlassCard(title = "Module 8: Post-Campaign AI Performance Review") {
                            Text("AI audit scores for '$campaignName':", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(10.dp))

                            val metrics = listOf(
                                "Professionalism & Communication" to "9.8 / 10 (Fast response time)",
                                "Content Quality & Visual Polish" to "9.5 / 10 (Sharp 4K grading)",
                                "Deadline Delivery Speed" to "10 / 10 (Delivered 24 hrs early)",
                                "Brand Manager Satisfaction" to "9.6 / 10 (Excellent feedback)"
                            )

                            metrics.forEach { (metric, score) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(metric, fontSize = 11.sp, color = Color.White)
                                    Text(score, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x33FFD700))
                                    .padding(10.dp)
                            ) {
                                Text("💡 AI Recommendation: You qualify for a 20% rate increase on your next campaign with $brandName!", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            }
                        }
                    }

                    // MODULE 9: CLIENT RETENTION TIPS
                    9 -> {
                        CampaignGlassCard(title = "Module 9: Brand Client Retention Strategies") {
                            Text("How to turn one-off deals into recurring retainer contracts:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(10.dp))

                            listOf(
                                "1. Send a Warm Thank You Email: Express genuine appreciation to the brand manager within 2 hours of posting.",
                                "2. Share Post-Campaign Analytics PDF: Send total impressions, link clicks, saves, and audience sentiment after 7 days.",
                                "3. Ask for Direct Feedback: Request a 2-sentence testimonial to feature on your creator media kit.",
                                "4. Propose a Quarterly Retainer: Offer 3 months of planned content at a 15% bundled discount."
                            ).forEach { tip ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0x22FFFFFF))
                                        .padding(10.dp)
                                ) {
                                    Text(tip, fontSize = 11.sp, color = Color.White, lineHeight = 15.sp)
                                }
                            }
                        }
                    }

                    // MODULE 10: TODAY'S MISSION & ACHIEVEMENT
                    10 -> {
                        CampaignGlassCard(title = "Module 10: Today's Mission & XP Reward") {
                            Text("Complete Your First Campaign Plan (Estimated Time: 12 Minutes)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(8.dp))

                            val missionTasks = listOf(
                                "Setup campaign overview details (Name, Brand, Platform)",
                                "Configure required deliverables checklist",
                                "Generate AI creative hooks & video structure outline",
                                "Setup calendar milestone dates for draft submission",
                                "Run AI post-campaign performance review"
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
                                    Text("Campaign Manager", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("+850 XP Reward • Level 11 AI Campaign Planner Completed", fontSize = 11.sp, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isAlreadyCompleted = true
                                    persistState(completed = true)
                                    onLevel11Completed()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text("COMPLETE LEVEL 11 & CLAIM REWARD 🎉", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // BOTTOM ACTION BUTTONS (Back / Continue)
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
                                onLevel11Completed()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                    ) {
                        Text(
                            text = if (currentModule < 10) "Continue →" else "Finish Level 11 🎉",
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
