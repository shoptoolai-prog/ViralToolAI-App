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
private fun CrmGlassCard(
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
private fun CrmTextField(
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
private fun CrmRatingRow(
    label: String,
    currentVal: Int,
    onSelect: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 11.sp, color = Color.White)
            Text("$currentVal / 5 Stars", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            (1..5).forEach { star ->
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (star <= currentVal) Color(0xFFFFD700) else Color(0x22FFFFFF))
                        .clickable { onSelect(star) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("★", fontSize = 14.sp, color = if (star <= currentVal) Color.Black else Color.White)
                }
            }
        }
    }
}

data class BrandCrmEntry(
    val id: String,
    val name: String,
    val category: String,
    val website: String,
    val instagram: String,
    val email: String,
    val contactPerson: String,
    val country: String,
    val status: String, // Interested, Contacted, Waiting Reply, Negotiation, Approved, Completed, Rejected, Archived
    val campaignName: String,
    val dealAmount: String,
    val paymentStatus: String, // Pending, Advance Paid, Completed, Overdue
    val completionDate: String,
    val commRating: Int,
    val paySpeedRating: Int,
    val profRating: Int,
    val futureRating: Int,
    val trustRating: Int,
    val notes: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandCollabLevel10CreatorCRMView(
    userNiche: String,
    userPlatform: String,
    userName: String = "Creator",
    onLevel10Completed: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current

    val savedData = remember { CreatorAcademyPrefs.getBrandCollabLevel10Data(context) }
    var currentModule by remember { mutableIntStateOf((savedData["module"] as? Int) ?: 1) }
    var currentStep by remember { mutableIntStateOf((savedData["step"] as? Int) ?: 1) }
    var isAlreadyCompleted by remember { mutableStateOf((savedData["completed"] as? Boolean) ?: false) }

    // Brand List State
    val initialBrands = remember {
        mutableStateListOf(
            BrandCrmEntry(
                id = "1",
                name = "Boat Audio",
                category = "Electronics / Audio",
                website = "www.boataudio.com",
                instagram = "@boat.life",
                email = "collabs@boataudio.com",
                contactPerson = "Rohan Sharma (Influencer Mgr)",
                country = "India",
                status = "Negotiation",
                campaignName = "Nirvana Ion Wireless Launch",
                dealAmount = "₹25,000",
                paymentStatus = "Advance Paid",
                completionDate = "20 Aug 2026",
                commRating = 5,
                paySpeedRating = 4,
                profRating = 5,
                futureRating = 5,
                trustRating = 5,
                notes = "Requires 1 Reel + 2 Stories. Prefers high-energy audio drops."
            ),
            BrandCrmEntry(
                id = "2",
                name = "Snitch Apparel",
                category = "Fashion / D2C",
                website = "www.snitch.co.in",
                instagram = "@snitch.fashion",
                email = "influencer@snitch.co.in",
                contactPerson = "Ananya Roy",
                country = "India",
                status = "Approved",
                campaignName = "Monsoon Fits Lookbook",
                dealAmount = "₹18,000",
                paymentStatus = "Pending Invoice",
                completionDate = "15 Aug 2026",
                commRating = 4,
                paySpeedRating = 4,
                profRating = 4,
                futureRating = 5,
                trustRating = 4,
                notes = "Fast approvals on video cuts. Good retainer potential."
            ),
            BrandCrmEntry(
                id = "3",
                name = "Minimalist Skincare",
                category = "Beauty / Grooming",
                website = "www.beminimalist.co",
                instagram = "@beminimalist.co",
                email = "partnerships@beminimalist.co",
                contactPerson = "Karan Verma",
                country = "India",
                status = "Waiting Reply",
                campaignName = "SPF 50 Sunscreen Campaign",
                dealAmount = "₹15,000",
                paymentStatus = "Pending",
                completionDate = "28 Aug 2026",
                commRating = 3,
                paySpeedRating = 3,
                profRating = 4,
                futureRating = 4,
                trustRating = 4,
                notes = "Sent proposal email 3 days ago. Needs follow-up today."
            ),
            BrandCrmEntry(
                id = "4",
                name = "Myntra",
                category = "E-Commerce",
                website = "www.myntra.com",
                instagram = "@myntra",
                email = "creatorhub@myntra.com",
                contactPerson = "Priya Mehta",
                country = "India",
                status = "Completed",
                campaignName = "EORS Festive Sale UGC",
                dealAmount = "₹35,000",
                paymentStatus = "Completed",
                completionDate = "01 Jul 2026",
                commRating = 5,
                paySpeedRating = 5,
                profRating = 5,
                futureRating = 5,
                trustRating = 5,
                notes = "High budget brand! Paid on 7th working day."
            )
        )
    }

    // Selected Active Brand
    var selectedBrandIndex by remember { mutableIntStateOf(0) }
    val activeBrand = initialBrands.getOrNull(selectedBrandIndex) ?: initialBrands.first()

    // Form editing state for Module 2
    var editBrandName by remember(selectedBrandIndex) { mutableStateOf(activeBrand.name) }
    var editCategory by remember(selectedBrandIndex) { mutableStateOf(activeBrand.category) }
    var editWebsite by remember(selectedBrandIndex) { mutableStateOf(activeBrand.website) }
    var editInstagram by remember(selectedBrandIndex) { mutableStateOf(activeBrand.instagram) }
    var editEmail by remember(selectedBrandIndex) { mutableStateOf(activeBrand.email) }
    var editContactPerson by remember(selectedBrandIndex) { mutableStateOf(activeBrand.contactPerson) }
    var editCountry by remember(selectedBrandIndex) { mutableStateOf(activeBrand.country) }
    var editNotes by remember(selectedBrandIndex) { mutableStateOf(activeBrand.notes) }
    var editStatus by remember(selectedBrandIndex) { mutableStateOf(activeBrand.status) }

    // Form editing state for Module 3 (Collaboration History)
    var editCampaignName by remember(selectedBrandIndex) { mutableStateOf(activeBrand.campaignName) }
    var editDealAmount by remember(selectedBrandIndex) { mutableStateOf(activeBrand.dealAmount) }
    var editPaymentStatus by remember(selectedBrandIndex) { mutableStateOf(activeBrand.paymentStatus) }
    var editCompletionDate by remember(selectedBrandIndex) { mutableStateOf(activeBrand.completionDate) }

    // Ratings state for Module 5
    var commRating by remember(selectedBrandIndex) { mutableIntStateOf(activeBrand.commRating) }
    var paySpeedRating by remember(selectedBrandIndex) { mutableIntStateOf(activeBrand.paySpeedRating) }
    var profRating by remember(selectedBrandIndex) { mutableIntStateOf(activeBrand.profRating) }
    var futureRating by remember(selectedBrandIndex) { mutableIntStateOf(activeBrand.futureRating) }
    var trustRating by remember(selectedBrandIndex) { mutableIntStateOf(activeBrand.trustRating) }

    // Filter status for Module 1
    var dashboardFilterStatus by remember { mutableStateOf("ALL") }

    // Mission 9 Checklist items
    val missionChecklist = remember {
        mutableStateListOf(
            true, // Categorize active brand leads
            true, // Update campaign negotiation status
            true, // Schedule follow-ups for pending replies
            false, // Send ROI report for completed campaigns
            false // Assign brand relationship ratings
        )
    }

    // Persist State helper
    fun persistState(completed: Boolean = isAlreadyCompleted) {
        CreatorAcademyPrefs.saveBrandCollabLevel10State(
            context = context,
            step = currentStep,
            module = currentModule,
            brandsData = activeBrand.name,
            selectedBrand = activeBrand.name,
            isCompleted = completed
        )
    }

    // Save edited brand updates back to list
    fun saveActiveBrandChanges() {
        if (selectedBrandIndex in initialBrands.indices) {
            initialBrands[selectedBrandIndex] = activeBrand.copy(
                name = editBrandName,
                category = editCategory,
                website = editWebsite,
                instagram = editInstagram,
                email = editEmail,
                contactPerson = editContactPerson,
                country = editCountry,
                notes = editNotes,
                status = editStatus,
                campaignName = editCampaignName,
                dealAmount = editDealAmount,
                paymentStatus = editPaymentStatus,
                completionDate = editCompletionDate,
                commRating = commRating,
                paySpeedRating = paySpeedRating,
                profRating = profRating,
                futureRating = futureRating,
                trustRating = trustRating
            )
            persistState()
        }
    }

    // Progress angle (fixed 96% for Level 10 CRM)
    val progressPercent = if (isAlreadyCompleted || currentModule == 9) 100 else 96
    val progressRingAngle by animateFloatAsState(
        targetValue = (progressPercent / 100f) * 360f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "level10ProgressRing"
    )

    // Floating background offset
    val infiniteTransition = rememberInfiniteTransition(label = "crmBg")
    val floatingOffsetY by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "floatingOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF0F172A))))
    ) {
        // Floating Golden Particles & Analytics Graph Canvas Background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Golden Particles
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.15f), radius = 8.dp.toPx(), center = Offset(w * 0.15f, h * 0.15f + floatingOffsetY * 2))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.12f), radius = 12.dp.toPx(), center = Offset(w * 0.85f, h * 0.28f - floatingOffsetY * 3))
            drawCircle(Color(0x3338BDF8), radius = 10.dp.toPx(), center = Offset(w * 0.82f, h * 0.75f + floatingOffsetY * 2))
            drawCircle(Color(0x224ADE80), radius = 14.dp.toPx(), center = Offset(w * 0.12f, h * 0.82f - floatingOffsetY * 3))

            // Analytics Graph Line Overlay
            val graphPath = Path().apply {
                moveTo(0f, h * 0.45f)
                cubicTo(w * 0.25f, h * 0.42f, w * 0.4f, h * 0.48f, w * 0.6f, h * 0.40f)
                cubicTo(w * 0.75f, h * 0.35f, w * 0.9f, h * 0.38f, w, h * 0.30f)
            }
            drawPath(
                path = graphPath,
                color = Color(0x11FFD700),
                style = Stroke(width = 3.dp.toPx())
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // HEADER: Creator CRM
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
                            text = "Creator CRM",
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
                            Text("LEVEL 10", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                        }
                    }
                    Text(
                        text = "Manage Every Brand Professionally",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }

                // 96% Premium Animated Progress Ring
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
                    listOf(" Boat", " Snitch", " Minimalist", " Myntra", " Amazon", " Nykaa", " Meesho", " Flipkart").forEach { logo ->
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
                                Text("• CRM Advisor Active", fontSize = 9.sp, color = Color(0xFF4ADE80))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            val speech = when (currentModule) {
                                1 -> "Professional creators sirf brands nahi dhoondte. Woh relationships build karte hain. Main tumhe sikhaunga ki brands ko long-term clients kaise banana hai."
                                2 -> "Brand Profile me contact person aur direct email save karke rakho. Direct brand manager connection = 3x repeat deals!"
                                3 -> "Har deal ki past campaign history aur payment status track karo taaki koi pending invoice baaki na rahe."
                                4 -> "Timing hi sab kuch hai! Correct follow-up schedule se tumhari deal closing rate 60% badh jaati hai."
                                5 -> "Brand rating se tumhe yeh clear pata rehta hai ki kis brand ke saath dubara kaam karna beneficial hai aur kise avoid karna hai."
                                6 -> "Yeh 4 Relationship Pillars follow karo! Deliverables on-time deliver karke post-campaign ROI reports zaroor share karo."
                                7 -> "Campaign Timeline tumhari visual deal pipeline hai! Abhi '${activeBrand.name}' deal '${activeBrand.status}' phase me hai."
                                8 -> "Main tumhare enter kiye data ke basis par specific AI recommendations generate kar raha hoon. Zero fake predictions!"
                                9 -> "Today's Mission: Organize Your Brand List (Est. 8 Mins). Complete karo aur Relationship Builder badge unlock karo!"
                                else -> "Badhaai ho! Tumhara Creator CRM fully organized hai. Ab har brand collaboration professional corporate standard par handle hoga!"
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
                        1 to "1. Dashboard",
                        2 to "2. Brand Profile",
                        3 to "3. History",
                        4 to "4. Reminders",
                        5 to "5. Brand Rating",
                        6 to "6. Relationship Tips",
                        7 to "7. Timeline",
                        8 to "8. AI Suggestions",
                        9 to "9. Today's Mission"
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
                    // MODULE 1: BRAND DASHBOARD
                    1 -> {
                        CrmGlassCard(title = "Module 1: Brand Pipeline Dashboard") {
                            Text("Manage every brand deal across distinct pipeline stages:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(10.dp))

                            val statuses = listOf("ALL", "Interested", "Contacted", "Waiting Reply", "Negotiation", "Approved", "Completed", "Rejected", "Archived")
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                statuses.forEach { status ->
                                    val count = if (status == "ALL") initialBrands.size else initialBrands.count { it.status.equals(status, ignoreCase = true) }
                                    val isSelected = dashboardFilterStatus == status
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(if (isSelected) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                            .clickable { dashboardFilterStatus = status }
                                            .padding(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                        Text(
                                            text = "$status ($count)",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.Black else Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            val filteredList = if (dashboardFilterStatus == "ALL") initialBrands
                            else initialBrands.filter { it.status.equals(dashboardFilterStatus, ignoreCase = true) }

                            if (filteredList.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0x22FFFFFF))
                                        .padding(14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No brands currently in $dashboardFilterStatus stage.", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                                }
                            } else {
                                filteredList.forEachIndexed { idx, brand ->
                                    val actualIdx = initialBrands.indexOf(brand)
                                    val isSelected = selectedBrandIndex == actualIdx
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSelected) Color(0x33FFD700) else Color(0x22FFFFFF))
                                            .border(1.dp, if (isSelected) Color(0xFFFFD700) else Color.Transparent, RoundedCornerShape(12.dp))
                                            .clickable {
                                                selectedBrandIndex = actualIdx
                                                saveActiveBrandChanges()
                                            }
                                            .padding(10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(brand.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                Text("${brand.category} • ${brand.contactPerson}", fontSize = 10.sp, color = Color.White.copy(alpha = 0.65f))
                                            }
                                            Column(horizontalAlignment = Alignment.End) {
                                                Text(brand.dealAmount, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(Color(0x224ADE80))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(brand.status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4ADE80))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // MODULE 2: BRAND PROFILE
                    2 -> {
                        CrmGlassCard(title = "Module 2: Brand Profile & Contact Details") {
                            Text("Selected Active Brand: ${activeBrand.name}", fontSize = 11.sp, color = Color(0xFFFFD700), fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))

                            CrmTextField("Brand Name", editBrandName, "e.g. Boat Audio") { editBrandName = it; saveActiveBrandChanges() }
                            CrmTextField("Category / Niche", editCategory, "e.g. Electronics / D2C") { editCategory = it; saveActiveBrandChanges() }
                            CrmTextField("Website URL", editWebsite, "e.g. www.boataudio.com") { editWebsite = it; saveActiveBrandChanges() }
                            CrmTextField("Instagram Handle", editInstagram, "e.g. @boat.life") { editInstagram = it; saveActiveBrandChanges() }
                            CrmTextField("Official Email", editEmail, "e.g. collabs@boataudio.com") { editEmail = it; saveActiveBrandChanges() }
                            CrmTextField("Contact Person Name & Role", editContactPerson, "e.g. Rohan Sharma (Influencer Mgr)") { editContactPerson = it; saveActiveBrandChanges() }
                            CrmTextField("Country / Region", editCountry, "e.g. India") { editCountry = it; saveActiveBrandChanges() }
                            CrmTextField("Strategic Brand Notes", editNotes, "e.g. Requires 4K Reels, pays within 15 days") { editNotes = it; saveActiveBrandChanges() }

                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    saveActiveBrandChanges()
                                    Toast.makeText(context, "Brand profile auto-saved!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("💾 Save Brand Profile", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }

                    // MODULE 3: COLLABORATION HISTORY
                    3 -> {
                        CrmGlassCard(title = "Module 3: Campaign Collaboration History") {
                            Text("Track financial deals and completion records for ${activeBrand.name}:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(8.dp))

                            CrmTextField("Active Campaign Name", editCampaignName, "e.g. Nirvana Ion Wireless Launch") { editCampaignName = it; saveActiveBrandChanges() }
                            CrmTextField("Agreed Deal Amount", editDealAmount, "e.g. ₹25,000") { editDealAmount = it; saveActiveBrandChanges() }
                            CrmTextField("Payment Status", editPaymentStatus, "e.g. Advance Paid / Pending") { editPaymentStatus = it; saveActiveBrandChanges() }
                            CrmTextField("Target Completion Date", editCompletionDate, "e.g. 20 Aug 2026") { editCompletionDate = it; saveActiveBrandChanges() }

                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x224ADE80))
                                    .padding(10.dp)
                            ) {
                                Text("✅ Past campaigns record stored in local encrypted database.", fontSize = 10.sp, color = Color(0xFF4ADE80))
                            }
                        }
                    }

                    // MODULE 4: FOLLOW-UP REMINDER
                    4 -> {
                        CrmGlassCard(title = "Module 4: Smart Follow-up Reminder Schedule") {
                            Text("AI suggested follow-up timing for ${activeBrand.name}:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(10.dp))

                            val reminders = listOf(
                                "🔔 Follow-up Today" to "Send polite reminder about contract signing & advance invoice.",
                                "📅 Tomorrow" to "Check if sample product shipment tracking code is generated.",
                                "📆 Next Week" to "Share draft video script for brand manager approval.",
                                "🎯 Custom Date" to "Set custom calendar alert for Q3 Retainer Proposal."
                            )

                            reminders.forEach { (timing, desc) ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0x22FFFFFF))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(timing, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(desc, fontSize = 10.5.sp, color = Color.White.copy(alpha = 0.8f))
                                    }
                                }
                            }
                        }
                    }

                    // MODULE 5: BRAND RATING
                    5 -> {
                        CrmGlassCard(title = "Module 5: Brand Evaluation & Relationship Score") {
                            Text("Rate your experience working with ${activeBrand.name}:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(10.dp))

                            CrmRatingRow("Communication Speed", commRating) { commRating = it; saveActiveBrandChanges() }
                            CrmRatingRow("Payment Punctuality", paySpeedRating) { paySpeedRating = it; saveActiveBrandChanges() }
                            CrmRatingRow("Corporate Professionalism", profRating) { profRating = it; saveActiveBrandChanges() }
                            CrmRatingRow("Future Retainer Potential", futureRating) { futureRating = it; saveActiveBrandChanges() }
                            CrmRatingRow("Overall Trust Level", trustRating) { trustRating = it; saveActiveBrandChanges() }

                            val avgRating = (commRating + paySpeedRating + profRating + futureRating + trustRating) / 5.0
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x33FFD700))
                                    .padding(10.dp)
                            ) {
                                Text("⭐ Brand Relationship Score: %.1f / 5.0 (High Priority Partner)".format(avgRating), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            }
                        }
                    }

                    // MODULE 6: RELATIONSHIP TIPS
                    6 -> {
                        CrmGlassCard(title = "Module 6: How to Become a Repeat Creator") {
                            Text("4 Strategic Pillars to build long-term brand retainer clients:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(10.dp))

                            listOf(
                                "1. Deliver Post-Campaign ROI Analytics: Send screenshot of link clicks, reel plays & saves within 7 days of posting.",
                                "2. Always Meet Production Deadlines: Deliver raw video preview 24 hours prior to scheduled posting date.",
                                "3. Proactive Professional Communication: Respond to brand managers during standard business hours (10 AM - 6 PM).",
                                "4. Pitch Quarterly Retainer Bundles: Offer 15% bundle discount if brand commits to 3 months of continuous content."
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

                    // MODULE 7: CAMPAIGN TIMELINE
                    7 -> {
                        CrmGlassCard(title = "Module 7: Visual Campaign Stage Timeline") {
                            Text("Current Progress Timeline for ${activeBrand.name}:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(10.dp))

                            val timelineStages = listOf("Contact", "Reply", "Negotiation", "Agreement", "Content", "Approval", "Posting", "Payment", "Completed")
                            timelineStages.forEachIndexed { idx, stage ->
                                val isPassed = idx <= 2 // Active up to Negotiation
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 3.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(if (isPassed) Color(0xFFFFD700) else Color(0x22FFFFFF)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(if (isPassed) "✓" else "${idx + 1}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isPassed) Color.Black else Color.White)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(stage, fontSize = 11.sp, fontWeight = if (stage.equals(activeBrand.status, ignoreCase = true)) FontWeight.Black else FontWeight.Normal, color = if (stage.equals(activeBrand.status, ignoreCase = true)) Color(0xFFFFD700) else Color.White)
                                }
                            }
                        }
                    }

                    // MODULE 8: AI SUGGESTIONS
                    8 -> {
                        CrmGlassCard(title = "Module 8: Data-Driven AI Recommendations") {
                            Text("Actionable insights generated strictly from your entered data:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(10.dp))

                            listOf(
                                "🔥 High Priority: Boat Audio (Negotiation phase, ₹25k deal). Send final rate card today.",
                                "📌 Follow-up Recommended: Minimalist Skincare (Waiting reply for 3 days). Send polite bump email.",
                                "⭐ Retainer Opportunity: Myntra (Rating 5/5, fast payment). Pitch Q3 festive campaign bundle!"
                            ).forEach { rec ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0x22FFFFFF))
                                        .padding(10.dp)
                                ) {
                                    Text(rec, fontSize = 11.sp, color = Color.White, lineHeight = 15.sp)
                                }
                            }
                        }
                    }

                    // MODULE 9: TODAY'S MISSION & ACHIEVEMENT
                    9 -> {
                        CrmGlassCard(title = "Module 9: Today's Mission & XP Reward") {
                            Text("Organize Your Brand List (Estimated Time: 8 Minutes)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.height(8.dp))

                            val missionTasks = listOf(
                                "Categorize active brand leads into pipeline stages",
                                "Update active campaign negotiation status",
                                "Schedule follow-up reminder for pending replies",
                                "Send post-campaign ROI report to completed brands",
                                "Assign brand evaluation ratings to top 3 partners"
                            )

                            missionTasks.forEachIndexed { idx, task ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                ) {
                                    Checkbox(
                                        checked = missionChecklist.getOrElse(idx) { true },
                                        onCheckedChange = { checked ->
                                            if (idx < missionChecklist.size) {
                                                missionChecklist[idx] = checked
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFFD700))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(task, fontSize = 11.sp, color = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // ACHIEVEMENT BADGE CARD
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A))))
                                    .border(1.5.dp, Color(0xFFFFD700), RoundedCornerShape(14.dp))
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("🏆 ACHIEVEMENT UNLOCKED", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Relationship Builder", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("+500 XP • Creator CRM Master", fontSize = 11.sp, color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // BOTTOM NAVIGATION BUTTONS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("← Back", color = Color.White, fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (currentModule < 9) {
                            currentModule++
                            persistState()
                        } else {
                            isAlreadyCompleted = true
                            persistState(completed = true)
                            onLevel10Completed()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (currentModule < 9) "Continue →" else "Finish Level 10 🎉",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
