package com.example.creatoracademy

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Luxury Theme Palette
private val LuxuryDarkBg1 = Color(0xFF0F081C)
private val LuxuryDarkBg2 = Color(0xFF1E1035)
private val LuxuryPurpleAccent = Color(0xFF8E24AA)
private val LuxuryPurpleLight = Color(0xFFBA68C8)
private val LuxuryGold = Color(0xFFFFD700)
private val LuxuryGlassWhite = Color(0x1FFFFFFF)
private val LuxuryGlassBorder = Color(0x35FFFFFF)

data class SuccessGoalItem(
    val id: String,
    val title: String,
    var target: String,
    var isCompleted: Boolean,
    var progressText: String
)

data class SuccessVaultItem(
    val id: String,
    val title: String,
    val category: String,
    val content: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WishlinkCreatorLevel15SuccessHubView(
    userProfile: Map<String, String>,
    onCompleteLevel15: () -> Unit,
    onRestartCourse: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Creator Profile Info
    val creatorName = userProfile["creatorName"] ?: userProfile["name"] ?: "Creator Legend"
    val currentDateStr = remember {
        val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        sdf.format(Date())
    }
    val certificateId = remember { "WLC-2026-${(10000..99999).random()}" }

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "gold_glow")
    val goldPulseAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Graduation ceremony confetti effect toggle
    var showConfettiCelebration by remember { mutableStateOf(true) }
    var showResetDialog by remember { mutableStateOf(false) }

    // Ask AI Mentor Modal
    var showAskAiDialog by remember { mutableStateOf(false) }
    var userAiQuestion by remember { mutableStateOf("") }
    var aiMentorResponse by remember { mutableStateOf("") }

    // Goals State
    val goalsList = remember {
        mutableStateListOf(
            SuccessGoalItem("1", "First ₹100 Revenue", "₹100", true, "100% Achieved"),
            SuccessGoalItem("2", "First ₹500 Milestone", "₹500", true, "100% Achieved"),
            SuccessGoalItem("3", "First ₹1,000 Target", "₹1,000", true, "100% Achieved"),
            SuccessGoalItem("4", "First ₹5,000 Milestone", "₹5,000", true, "100% Achieved"),
            SuccessGoalItem("5", "First ₹10,000 Master Target", "₹10,000", false, "₹6,850 / ₹10,000")
        )
    }

    // Vault Items
    val vaultList = remember {
        mutableStateListOf(
            SuccessVaultItem("v1", "Viral Hook 1", "Saved Hooks", "Stop wearing basic jeans until you try this Wishlink combo! 👖🔥"),
            SuccessVaultItem("v2", "High Conversion Reel Script", "Saved Scripts", "Hook: 3 Zara Dupes under ₹799!\nBody: Show item 1, 2, 3.\nCTA: Comment 'LINK' below!"),
            SuccessVaultItem("v3", "Festive OOTD Caption", "Saved Captions", "Glam up your festive season with these top curated picks ✨ link in bio & stories!"),
            SuccessVaultItem("v4", "Trending Fashion Hashtags", "Saved Hashtags", "#WishlinkCreator #FestiveOutfits #MyntraFinds #OOTDIndia"),
            SuccessVaultItem("v5", "GRWM Story Idea", "Saved Story Ideas", "Step-by-step Get Ready With Me with direct Wishlink affiliate story sticker!"),
            SuccessVaultItem("v6", "Sneaker Product Review", "Saved Product Reviews", "Puma RS-X honesty review: 9/10 durability & super comfortable."),
            SuccessVaultItem("v7", "Quarterly Brand Plan", "Saved Business Plans", "Goal: ₹25,000 monthly affiliate commission & 10 brand collaborations."),
            SuccessVaultItem("v8", "Verified Creator Media Kit", "Saved Portfolio", "100K+ Monthly Reach | High Conversion Rate | Wishlink Certified")
        )
    }

    var vaultSearchQuery by remember { mutableStateOf("") }

    // Completion persist
    LaunchedEffect(Unit) {
        val certObj = JSONObject()
        certObj.put("creatorName", creatorName)
        certObj.put("completionDate", currentDateStr)
        certObj.put("certificateId", certificateId)
        CreatorAcademyPrefs.completeWishlinkLevel15(context, 100, certObj.toString())
        onCompleteLevel15()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(LuxuryDarkBg1, LuxuryDarkBg2, Color(0xFF0D031A))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Floating Trophy, Shopping Bags, Creator Stars Background Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Glow rings
            drawCircle(
                color = LuxuryGold.copy(alpha = 0.08f + goldPulseAnim * 0.05f),
                center = Offset(w * 0.5f, h * 0.2f),
                radius = w * 0.55f
            )
            drawCircle(
                color = LuxuryPurpleAccent.copy(alpha = 0.12f),
                center = Offset(w * 0.8f, h * 0.7f),
                radius = w * 0.4f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(LuxuryGlassWhite)
                        .border(1.dp, LuxuryGlassBorder, CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF4A148C), Color(0xFF8E24AA))
                            )
                        )
                        .border(1.dp, LuxuryGold, RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(text = "🏆 LEVEL 15: WISHLINK LEGEND", color = LuxuryGold, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // COURSE COMPLETED - 100% Progress Ring
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGold.copy(alpha = 0.8f), RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF26103D)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(110.dp)
                    ) {
                        Canvas(modifier = Modifier.size(100.dp)) {
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(LuxuryGold, Color(0xFFFFF59D), LuxuryPurpleAccent, LuxuryGold)
                                ),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "100%", color = LuxuryGold, fontWeight = FontWeight.ExtraBold, fontSize = 26.sp)
                            Text(text = "Graduated", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Course Completed!",
                        color = LuxuryGold,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = "Wishlink Creator Success Hub",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // AI MENTOR GRADUATION SPEECH
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGold, RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF210C36)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(LuxuryGold, LuxuryPurpleAccent))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Psychology, contentDescription = "AI Mentor", tint = Color.White, modifier = Modifier.size(34.dp))
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Lifetime AI Creator Mentor", color = LuxuryGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(imageVector = Icons.Default.Verified, contentDescription = "Verified", tint = LuxuryGold, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "\"🎉 Congratulations $creatorName!\n\nTumne Wishlink Creator Guide successfully complete kar liya. Ab tum sirf learner nahi... Ek Professional Affiliate Creator ho!\n\nAaj se main tumhara Lifetime AI Creator Mentor hoon. Jab bhi script, caption, store audit, ya monetization advice chahiye — main hamesha available hoon.\"",
                            color = Color(0xFFE1D0FF),
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { showAskAiDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = LuxuryPurpleAccent),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Ask AI Mentor Anything", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // GRADUATION CEREMONY & CERTIFICATE
            SectionHeader(title = "GRADUATION CERTIFICATE", icon = Icons.Default.EmojiEvents)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Brush.horizontalGradient(listOf(LuxuryGold, LuxuryPurpleLight, LuxuryGold)), RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B0B2E))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🏆 OFFICIAL CERTIFICATE OF COMPLETION 🏆", color = LuxuryGold, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Wishlink Creator Guide", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, textAlign = TextAlign.Center)
                    Text(text = "Successfully Completed & Certified", color = Color(0xFFB388FF), fontSize = 13.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "This certificate is awarded to", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = creatorName,
                        color = LuxuryGold,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x20FFFFFF))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Completion Date", color = Color.Gray, fontSize = 10.sp)
                            Text(text = currentDateStr, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Certificate ID", color = Color.Gray, fontSize = 10.sp)
                            Text(text = certificateId, color = LuxuryGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Gold Seal Badge
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(LuxuryGold, Color(0xFFFFA000))))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⭐ OFFICIAL WISHLINK CERTIFIED ⭐", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Export Options
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(context, "Certificate Downloaded as PDF!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = LuxuryPurpleAccent),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Download, contentDescription = "PDF", tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Export PDF", color = Color.White, fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Wishlink Certificate", "Wishlink Creator Certified: $creatorName | ID: $certificateId | Date: $currentDateStr")
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Certificate Info Copied to Clipboard!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x30FFFFFF)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Share Info", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // LIFETIME AI MENTOR FEATURES
            SectionHeader(title = "LIFETIME AI MENTOR PERKS", icon = Icons.Default.LockOpen)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val perks = listOf(
                        "Unlimited Questions & Instant AI Support",
                        "Unlimited Reel Caption Review & Optimization",
                        "Unlimited Reel & Story Script Generator",
                        "Unlimited Wishlink Store Audit & Conversion Checks",
                        "Unlimited Creator Portfolio & Media Kit Review",
                        "Unlimited Weekly Content Coaching & Strategy",
                        "Unlimited Niche & Affiliate Business Guidance"
                    )

                    perks.forEach { perk ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = perk, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // WISHLINK SUCCESS HUB QUICK ACCESS
            SectionHeader(title = "WISHLINK SUCCESS HUB", icon = Icons.Default.Store)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val hubTools = listOf(
                    "Store Audit" to Icons.Default.Store,
                    "Product Review" to Icons.Default.Star,
                    "Caption Generator" to Icons.Default.Edit,
                    "Script Generator" to Icons.Default.RocketLaunch,
                    "Business Planner" to Icons.Default.Analytics,
                    "Portfolio Review" to Icons.Default.Verified,
                    "Analytics Review" to Icons.Default.TrendingUp,
                    "Creator Vault" to Icons.Default.Bookmark
                )

                hubTools.forEach { (tool, icon) ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                Toast.makeText(context, "Opening $tool in Success Hub...", Toast.LENGTH_SHORT).show()
                            }
                            .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = icon, contentDescription = tool, tint = LuxuryGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = tool, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // DAILY AI COACH & WEEKLY / MONTHLY AI REVIEWS
            SectionHeader(title = "DAILY & PERIODIC AI REVIEWS", icon = Icons.Default.Lightbulb)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Daily AI Advice", color = LuxuryGold, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "• Today's Mission: Share 1 Reel & 2 Wishlink Stories with interactive sticker polls", color = Color.White, fontSize = 12.sp)
                    Text(text = "• Today's Advice: Always reply to early reel comments within 15 minutes to boost algorithm distribution", color = Color.White, fontSize = 12.sp)
                    Text(text = "• Today's Motivation: Small daily affiliate actions compound into massive long-term monthly income!", color = Color.White, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(text = "Weekly AI Scorecard", color = LuxuryPurpleLight, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Learning: 99", color = Color.White, fontSize = 12.sp)
                        Text(text = "Business: 95", color = Color.White, fontSize = 12.sp)
                        Text(text = "Store: 98", color = Color.White, fontSize = 12.sp)
                        Text(text = "Content: 96", color = Color.White, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(text = "Monthly Focus", color = LuxuryGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = "Biggest Improvement: High CTR Story Sticker Links (+45% clicks)\nNext Goal: Cross ₹10,000 Total Wishlink Earning Threshold", color = Color(0xFFD1C4E9), fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // CREATOR LEVELS TIMELINE
            SectionHeader(title = "CREATOR LEVELS ROADMAP", icon = Icons.Default.Timeline)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val levels = listOf(
                        "Beginner Creator" to true,
                        "Explorer Creator" to true,
                        "Store Builder" to true,
                        "Content Creator" to true,
                        "Business Creator" to true,
                        "Brand Ready Creator" to true,
                        "🏆 Wishlink Creator Legend (UNLOCKED)" to true
                    )

                    levels.forEachIndexed { idx, (lvlName, isDone) ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = lvlName,
                                color = if (idx == levels.lastIndex) LuxuryGold else Color.White,
                                fontWeight = if (idx == levels.lastIndex) FontWeight.ExtraBold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SUCCESS VAULT
            SectionHeader(title = "SUCCESS VAULT", icon = Icons.Default.Bookmark)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = vaultSearchQuery,
                        onValueChange = { vaultSearchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search saved vault scripts, hooks, captions...", color = Color.Gray, fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = LuxuryPurpleAccent,
                            unfocusedBorderColor = LuxuryGlassBorder
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val filteredVault = vaultList.filter {
                        vaultSearchQuery.isEmpty() || it.title.contains(vaultSearchQuery, ignoreCase = true) || it.content.contains(vaultSearchQuery, ignoreCase = true)
                    }

                    filteredVault.forEach { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(12.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color(0x20FFFFFF))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = item.title, color = LuxuryGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(text = item.content, color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // GOAL TRACKER
            SectionHeader(title = "GOAL TRACKER", icon = Icons.Default.EmojiEvents)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    goalsList.forEachIndexed { idx, goal ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x1AFFFFFF))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = goal.isCompleted,
                                onCheckedChange = { chk ->
                                    goalsList[idx] = goal.copy(isCompleted = chk)
                                },
                                colors = CheckboxDefaults.colors(checkedColor = LuxuryGold, uncheckedColor = Color.White)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = goal.title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text(text = goal.progressText, color = if (goal.isCompleted) Color(0xFF81C784) else LuxuryPurpleLight, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // FINAL ACHIEVEMENT BADGE
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, LuxuryGold, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF331652))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🏆 MAX LEVEL ACHIEVEMENT UNLOCKED 🏆", color = LuxuryGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Wishlink Creator Legend", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                    Text(text = "+15,000 Maximum Creator XP Granted", color = Color(0xFFFFF176), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // RESTART COURSE & NAVIGATION BUTTONS
            Button(
                onClick = { showResetDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x30FFFFFF)),
                border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Default.RestartAlt, contentDescription = "Reset", tint = Color.Red, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Restart Learning Course", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Disclaimer: AI Mentor provides strategic affiliate guidance. Earnings depend on audience engagement and effort. Ethical marketing is always encouraged.",
                color = Color.Gray,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))
        }

        // Dialogs
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                containerColor = Color(0xFF1E1035),
                title = { Text(text = "Restart Wishlink Creator Guide?", color = Color.White, fontWeight = FontWeight.Bold) },
                text = { Text(text = "Are you sure you want to restart from Level 1? All progress and certificates will be reset.", color = Color(0xFFD1C4E9)) },
                confirmButton = {
                    Button(
                        onClick = {
                            showResetDialog = false
                            CreatorAcademyPrefs.resetWishlinkLevel1Data(context)
                            onRestartCourse()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Yes, Restart Course", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Cancel", color = Color.White)
                    }
                }
            )
        }

        if (showAskAiDialog) {
            AlertDialog(
                onDismissRequest = { showAskAiDialog = false },
                containerColor = Color(0xFF230F3B),
                title = { Text(text = "Ask Lifetime AI Creator Mentor", color = LuxuryGold, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = userAiQuestion,
                            onValueChange = { userAiQuestion = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g., How do I get more clicks on my story links?", color = Color.Gray, fontSize = 12.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = LuxuryPurpleAccent,
                                unfocusedBorderColor = LuxuryGlassBorder
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        if (aiMentorResponse.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0x30FFFFFF)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = aiMentorResponse,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (userAiQuestion.isNotEmpty()) {
                                aiMentorResponse = "AI Mentor Strategy: To maximize Wishlink clicks on $userAiQuestion, use interactive poll stickers before dropping the direct link sticker! This increases story reach by 3x."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LuxuryPurpleAccent)
                    ) {
                        Text("Get Strategy", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAskAiDialog = false }) {
                        Text("Close", color = Color.White)
                    }
                }
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = LuxuryGold, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }
}
