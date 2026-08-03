package com.example.creatoracademy

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
private fun SuccessGlassCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0x221E293B))
            .border(1.dp, Color(0x44FFD700), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrandCollabPhase15CreatorSuccessHubView(
    userNiche: String,
    userPlatform: String,
    userName: String = "Creator",
    onRestartFullCourse: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current

    val phase15Data = remember { CreatorAcademyPrefs.getBrandCollabPhase15Data(context) }
    var userGoal by remember { mutableStateOf((phase15Data["goal"] as? String) ?: "₹1,00,000 Goal") }
    var currentXp by remember { mutableIntStateOf(5000) } // Maximum Level
    var showResetDialog by remember { mutableStateOf(false) }

    // Today's date
    val currentDateStr = remember {
        val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        sdf.format(Date())
    }

    // Interactive Tab / View State
    var activeSubView by remember { mutableStateOf("HUB") } // HUB, CERTIFICATE, CONSULTANT, VAULT, REVIEWS

    // Consultant Chat State
    val chatMessages = remember {
        mutableStateListOf(
            "AI Consultant" to "🎉 Congratulations $userName! Welcome to your Lifetime Creator Success Hub. Tum pricing, brand outreach, contracts, ya career scaling ke baare mein mujhse kuch bhi pooch sakte ho!"
        )
    }
    var consultantInput by remember { mutableStateOf("") }

    // Vault Search State
    var vaultSearchQuery by remember { mutableStateOf("") }

    // Floating Particles Animation
    val infiniteTransition = rememberInfiniteTransition(label = "phase15Bg")
    val floatAnimY by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "phase15FloatY"
    )

    val rotateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(12000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "goldRingRotate"
    )

    // Progress Ring Angle (100% Course Completed)
    val progressRingAngle by animateFloatAsState(
        targetValue = 360f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "phase15ProgressRing"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF0F172A))))
    ) {
        // Ultra Premium AI Background Canvas (Confetti, Gold Particles, Revenue Line, Verified Badge)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Floating Golden Confetti Particles
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.35f), radius = 16.dp.toPx(), center = Offset(w * 0.15f, h * 0.15f + floatAnimY * 2f))
            drawCircle(Color(0xFFFFD700).copy(alpha = 0.25f), radius = 22.dp.toPx(), center = Offset(w * 0.82f, h * 0.28f - floatAnimY * 2.2f))
            drawCircle(Color(0x444ADE80), radius = 18.dp.toPx(), center = Offset(w * 0.22f, h * 0.68f + floatAnimY * 2.5f))
            drawCircle(Color(0x3338BDF8), radius = 24.dp.toPx(), center = Offset(w * 0.78f, h * 0.82f - floatAnimY * 2.8f))

            // Smooth Gold Growth Curve Line
            val curvePath = Path().apply {
                moveTo(w * 0.05f, h * 0.42f)
                cubicTo(w * 0.35f, h * 0.40f, w * 0.60f, h * 0.35f, w * 0.95f, h * 0.25f)
            }
            drawPath(
                path = curvePath,
                color = Color(0x44FFD700),
                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // HEADER: Course Completed 100% Gold Ring
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
                        if (activeSubView != "HUB") {
                            activeSubView = "HUB"
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
                            text = "Creator Success Hub",
                            fontSize = 16.sp,
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
                            Text("FINAL LEVEL", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                        }
                    }
                    Text(
                        text = "100% Brand Collaboration Hub Graduate",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }

                // 100% Animated Gold Ring
                Box(
                    modifier = Modifier.size(46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = Color.White.copy(alpha = 0.15f),
                            style = Stroke(width = 3.5.dp.toPx())
                        )
                        drawArc(
                            color = Color(0xFFFFD700),
                            startAngle = -90f,
                            sweepAngle = progressRingAngle,
                            useCenter = false,
                            style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("100%", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                        Text("🎓", fontSize = 9.sp)
                    }
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
                // Marquee Floating Brand Logos Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("🏆 Creator Legend", "💼 Boat Audio", "👔 Snitch", "✨ Minimalist", "🛍 Myntra", "📦 Amazon", "💄 Nykaa", "📱 Samsung", "🎧 OnePlus", "🏅 Verified Brand Partner").forEach { item ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0x33FFD700))
                                .border(1.dp, Color(0x66FFD700), RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(item, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                        }
                    }
                }

                // MOTIVATION MODE WELCOME CARD
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(listOf(Color(0x331E293B), Color(0x33334155))))
                        .border(1.dp, Color(0x66FFD700), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFD700))
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👑", fontSize = 24.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Lifetime AI Brand Coach", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("• GRADUATE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4ADE80))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "🎉 Congratulations $userName! Tumne Brand Collaboration Hub successfully complete kar liya. Ab tum beginner creator nahi... Ek Professional Creator ho. Aaj se main tumhara Lifetime Brand Collaboration Coach hoon.\n\nWelcome back! Tumhari creator journey yahin se continue hogi jahan tumne last time stop kiya tha.",
                                fontSize = 11.5.sp,
                                color = Color.White,
                                lineHeight = 16.5.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // TOP ACTION NAVIGATION TABS
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val tabs = listOf(
                        "HUB" to "🏠 Success Hub",
                        "CERTIFICATE" to "🎓 Certificate",
                        "CONSULTANT" to "🤖 AI Consultant",
                        "VAULT" to "🔐 Success Vault",
                        "REVIEWS" to "📊 Reviews & Goals"
                    )
                    tabs.forEach { (tabKey, tabLabel) ->
                        val isSelected = activeSubView == tabKey
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                .clickable {
                                    activeSubView = tabKey
                                }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = tabLabel,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.Black else Color.White
                            )
                        }
                    }
                }

                when (activeSubView) {
                    // 1. MAIN SUCCESS HUB VIEW
                    "HUB" -> {
                        Column {
                            // GRADUATION UNLOCK CARD
                            SuccessGlassCard(title = "✨ Graduation & Lifetime Unlock") {
                                Text("Your unlocked lifetime creator privileges:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                val perks = listOf(
                                    "✔ Unlimited AI Brand Mentor Questions",
                                    "✔ Unlimited Brand Guidance & Rate Cards",
                                    "✔ Unlimited Negotiation Pitch Practice",
                                    "✔ Unlimited Campaign Strategy & Media Kits",
                                    "✔ Unlimited Business Coaching & Scaling"
                                )

                                perks.forEach { perk ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x22FFFFFF))
                                            .padding(8.dp)
                                    ) {
                                        Text(perk, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4ADE80))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // QUICK ACTIONS DASHBOARD GRID
                            SuccessGlassCard(title = "🚀 Creator Success Hub - Quick Actions") {
                                Text("Access every tool & workspace from one central hub:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(10.dp))

                                val actions = listOf(
                                    "🔎 Find Brands" to "Explore active brand databases",
                                    "📄 Generate Media Kit" to "Export PDF/Link media kit",
                                    "📊 Create Rate Card" to "Calculate custom pricing",
                                    "💬 Practice Negotiation" to "AI roleplay brand deals",
                                    "💼 Review Portfolio" to "Update metrics & showcase",
                                    "📅 Campaign Planner" to "Organize deliverables & timeline",
                                    "📈 Business Dashboard" to "Revenue goals & analytics"
                                )

                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    actions.forEach { (actionTitle, actionDesc) ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(0.48f)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0x33FFD700))
                                                .border(1.dp, Color(0x44FFD700), RoundedCornerShape(12.dp))
                                                .clickable {
                                                    Toast.makeText(context, "$actionTitle Opened!", Toast.LENGTH_SHORT).show()
                                                }
                                                .padding(10.dp)
                                        ) {
                                            Column {
                                                Text(actionTitle, fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD700))
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(actionDesc, fontSize = 9.5.sp, color = Color.White.copy(alpha = 0.8f))
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // AI DAILY COACH
                            SuccessGlassCard(title = "☀️ AI Daily Coach (Fresh Daily)") {
                                val adviceList = listOf(
                                    "Today's Advice" to "Always follow up on brand emails within 24 hours to show professionalism.",
                                    "Today's Task" to "Pitch 1 new brand manager using your Level 10 tailored email template.",
                                    "Today's Motivation" to "Brands don't just buy followers; they buy your trust, storytelling & audience connection.",
                                    "Today's Improvement" to "Include clear call-to-actions in your reel captions to boost engagement rate by 2%."
                                )

                                adviceList.forEach { (type, text) ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x22FFFFFF))
                                            .padding(10.dp)
                                    ) {
                                        Column {
                                            Text(type, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text, fontSize = 11.sp, color = Color.White)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // CREATOR LEGEND TIMELINE
                            SuccessGlassCard(title = "🏆 Creator Progression Timeline") {
                                val timeline = listOf(
                                    "Beginner Creator" to "0 XP (Unlocked)",
                                    "Growing Creator" to "1,000 XP (Unlocked)",
                                    "Professional Creator" to "2,500 XP (Unlocked)",
                                    "Elite Creator" to "3,500 XP (Unlocked)",
                                    "👑 Creator Legend" to "5,000 XP (FINAL LEVEL UNLOCKED 🏆)"
                                )

                                timeline.forEachIndexed { idx, (lvl, xp) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(if (idx == 4) Color(0xFFFFD700) else Color(0xFF4ADE80)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("✓", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(lvl, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (idx == 4) Color(0xFFFFD700) else Color.White)
                                            Text(xp, fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 2. CERTIFICATE VIEW
                    "CERTIFICATE" -> {
                        SuccessGlassCard(title = "🎓 Official Graduation Certificate") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A))))
                                    .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(16.dp))
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("🎓 CERTIFICATE OF MASTERY", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700), letterSpacing = 1.sp)
                                    Text("BRAND COLLABORATION HUB", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text("This is to certify that", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(userName, fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("has successfully completed all 15 Levels of the Brand Collaboration Hub with distinction & unlocked Creator Legend status.", fontSize = 11.sp, textAlign = TextAlign.Center, color = Color.White, lineHeight = 16.sp)

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("Completion Date:", fontSize = 9.sp, color = Color.White.copy(alpha = 0.6f))
                                            Text(currentDateStr, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }

                                        // Premium Gold Seal
                                        Box(
                                            modifier = Modifier
                                                .size(52.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFFFD700))
                                                .border(2.dp, Color.White, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("🏅", fontSize = 20.sp)
                                                Text("VERIFIED", fontSize = 7.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                            }
                                        }

                                        Column(horizontalAlignment = Alignment.End) {
                                            Text("Certificate ID:", fontSize = 9.sp, color = Color.White.copy(alpha = 0.6f))
                                            Text("BCH-2026-LEGEND-8892", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString("Brand Collaboration Hub Certificate ID: BCH-2026-LEGEND-8892 - Verified for $userName"))
                                    Toast.makeText(context, "Certificate Verification Link Copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                            ) {
                                Text("📋 Copy Certificate Verification Code", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }

                    // 3. UNLIMITED AI BUSINESS CONSULTANT
                    "CONSULTANT" -> {
                        SuccessGlassCard(title = "🤖 Lifetime AI Business Consultant") {
                            Text("Ask anything regarding pricing, brands, media kits, contracts, or career growth:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(10.dp))

                            // Chat Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(260.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x11FFFFFF))
                                    .padding(10.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState()),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    chatMessages.forEach { (sender, text) ->
                                        val isUser = sender == "You"
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth(0.85f)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(if (isUser) Color(0x33FFD700) else Color(0x22FFFFFF))
                                                    .border(1.dp, if (isUser) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                                    .padding(10.dp)
                                            ) {
                                                Column {
                                                    Text(sender, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(text, fontSize = 11.sp, color = Color.White, lineHeight = 15.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = consultantInput,
                                    onValueChange = { consultantInput = it },
                                    placeholder = { Text("Ask AI Consultant...", fontSize = 11.sp, color = Color.White.copy(alpha = 0.4f)) },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFFFD700),
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        if (consultantInput.isNotBlank()) {
                                            val query = consultantInput
                                            chatMessages.add("You" to query)
                                            consultantInput = ""

                                            // Generate tailored response
                                            val aiReply = when {
                                                query.contains("price", ignoreCase = true) || query.contains("rate", ignoreCase = true) ->
                                                    "Tumhara current engagement rate standard se 2x better hai. Reel pricing ₹12,000 - ₹18,000 standard basis se negotiate karo, aur 30-day usage rights ke liye +30% add karo."
                                                query.contains("brand", ignoreCase = true) || query.contains("pitch", ignoreCase = true) ->
                                                    "D2C Tech aur Fashion brands ke Brand Managers direct Instagram LinkedIn DM aur email par sabse active hain. Pitch mein direct proof & reach metrics include karo."
                                                query.contains("contract", ignoreCase = true) || query.contains("payment", ignoreCase = true) ->
                                                    "Always insist on 50% advance before posting, aur balance deliverable post hone ke 7-14 din ke andar. Standard deliverables written document karo."
                                                else ->
                                                    "As your Lifetime Creator Coach, mera advice hai consistency + portfolio updates. Tumhare niche ($userNiche) mein retainers win karne ke liye brand ROI showcase karo."
                                            }
                                            chatMessages.add("AI Consultant" to aiReply)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                                ) {
                                    Text("Send", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // 4. SEARCHABLE SUCCESS VAULT
                    "VAULT" -> {
                        SuccessGlassCard(title = "🔐 Searchable Creator Success Vault") {
                            Text("Saved campaigns, rate cards, media kits & negotiation scripts:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = vaultSearchQuery,
                                onValueChange = { vaultSearchQuery = it },
                                placeholder = { Text("Search vault assets...", fontSize = 11.sp, color = Color.White.copy(alpha = 0.4f)) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFFFD700),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            val vaultAssets = listOf(
                                "🎬 Best Campaign Script" to "Boat Audio AirDopes Review Reel (8.4% ER)",
                                "✉️ High-Converting Pitch Email" to "Tailored Brand Pitch Template for D2C Brands",
                                "📄 PDF Media Kit v3.2" to "Verified Creator Analytics & Audience Demographics",
                                "💰 Rate Card Structure" to "Reel + Story Bundle: ₹15,000 + Usage Rights",
                                "💬 Win-Win Negotiation Script" to "Handling 'No Budget' objections politely"
                            ).filter { (title, desc) ->
                                vaultSearchQuery.isBlank() || title.contains(vaultSearchQuery, ignoreCase = true) || desc.contains(vaultSearchQuery, ignoreCase = true)
                            }

                            vaultAssets.forEach { (assetTitle, assetDetail) ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0x22FFFFFF))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(assetTitle, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(assetDetail, fontSize = 11.sp, color = Color.White)
                                    }
                                }
                            }
                        }
                    }

                    // 5. REVIEWS & GOAL SYSTEM
                    "REVIEWS" -> {
                        Column {
                            // WEEKLY REVIEW
                            SuccessGlassCard(title = "📅 Weekly Performance Review") {
                                val weeklyMetrics = listOf(
                                    "Learning Progress" to "100% Course Completed",
                                    "Brand Communication" to "Excellent Professional Pitching",
                                    "Negotiation Mastery" to "Advanced Counter-offer Confidence",
                                    "Brand Readiness Score" to "98 / 100 Grade A+",
                                    "Business Growth Velocity" to "+35% Monthly Potential"
                                )
                                weeklyMetrics.forEach { (metric, value) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(metric, fontSize = 11.sp, color = Color.White)
                                        Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // MONTHLY REVIEW
                            SuccessGlassCard(title = "📊 Monthly Executive Diagnostics") {
                                val monthlyDiagnostics = listOf(
                                    "Most Improved Skill" to "Brand Rate Card Negotiation",
                                    "Strongest Area" to "Visual Portfolio & Engagement Rates",
                                    "Weakest Area" to "Automated Email Sequences",
                                    "Next Strategic Focus" to "3-Month Brand Retainer Contracts",
                                    "Recommended Course" to "Creator Enterprise Masterclass"
                                )
                                monthlyDiagnostics.forEach { (item, valStr) ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x22FFFFFF))
                                            .padding(8.dp)
                                    ) {
                                        Column {
                                            Text(item, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                                            Text(valStr, fontSize = 11.sp, color = Color.White)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // GOAL SELECTOR
                            SuccessGlassCard(title = "🎯 Active Earning Target") {
                                Text("Selected Earning Goal:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                Text(userGoal, fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))

                                Spacer(modifier = Modifier.height(8.dp))

                                val goalList = listOf("First Paid Deal", "₹10,000 Goal", "₹50,000 Goal", "₹1,00,000 Goal", "₹5,00,000 Goal")
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    goalList.forEach { g ->
                                        val isG = userGoal == g
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(if (isG) Color(0xFFFFD700) else Color(0x22FFFFFF))
                                                .clickable {
                                                    userGoal = g
                                                    CreatorAcademyPrefs.saveBrandCollabPhase15State(context = context, goal = g)
                                                }
                                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                        ) {
                                            Text(g, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = if (isG) Color.Black else Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // BOTTOM FIXED ACTIONS: Home Dashboard, Ask AI Mentor, Restart Full Course
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onBack() },
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, Color(0xFFFFD700)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("🏠 Home", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { activeSubView = "CONSULTANT" },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                    ) {
                        Text("🤖 AI Coach", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }

                    OutlinedButton(
                        onClick = { showResetDialog = true },
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, Color(0xFFEF4444)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
                    ) {
                        Text("🔄 Reset", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // CONFIRMATION DIALOG FOR RESTARTING COURSE
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text("Restart Full Course?", fontWeight = FontWeight.Bold, color = Color.White) },
                text = { Text("kya aap poore Brand Collaboration Hub course ko reset karna chahte hain? Sabhi levels and preferences restart ho jayenge.", color = Color.White.copy(alpha = 0.8f)) },
                confirmButton = {
                    Button(
                        onClick = {
                            showResetDialog = false
                            CreatorAcademyPrefs.resetBrandCollabFullCourse(context)
                            Toast.makeText(context, "Full Course Reset Successfully! Restarting...", Toast.LENGTH_SHORT).show()
                            onRestartFullCourse()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                    ) {
                        Text("Reset Course", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Cancel", color = Color.White)
                    }
                },
                containerColor = Color(0xFF1E293B)
            )
        }
    }
}
