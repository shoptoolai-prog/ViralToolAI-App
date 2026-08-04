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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
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
import kotlin.random.Random

// Luxury Purple Palette
private val LuxuryDarkBg1 = Color(0xFF0F081C)
private val LuxuryDarkBg2 = Color(0xFF1E1035)
private val LuxuryPurpleAccent = Color(0xFF8E24AA)
private val LuxuryPurpleLight = Color(0xFFBA68C8)
private val LuxuryGold = Color(0xFFFFD700)
private val LuxuryGlassWhite = Color(0x1FFFFFFF)
private val LuxuryGlassBorder = Color(0x35FFFFFF)

data class CreatorGoalItem(
    val id: String,
    val title: String,
    var target: String,
    var isCompleted: Boolean,
    var progressText: String
)

data class CreatorVaultItem(
    val id: String,
    val title: String,
    val category: String, // Hooks, Scripts, Captions, CTAs, Hashtags, Story Ideas, Product Reviews, Weekly Plans
    val content: String
)

data class CreatorAchievement(
    val title: String,
    val description: String,
    val isUnlocked: Boolean,
    val icon: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WishlinkCreatorLevel14DashboardView(
    userProfile: Map<String, String>,
    onCompleteLevel14: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Base state tracking
    var xpEarned by remember { mutableStateOf(14800) }
    var streakDays by remember { mutableStateOf(14) }
    var missionCompleted by remember { mutableStateOf(false) }

    // Floating gold particle animation
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    // Goals State
    val goalsList = remember {
        mutableStateListOf(
            CreatorGoalItem("1", "First ₹100 Revenue", "₹100", true, "100% Achieved"),
            CreatorGoalItem("2", "First ₹500 Milestone", "₹500", true, "100% Achieved"),
            CreatorGoalItem("3", "First ₹1,000 Target", "₹1,000", true, "100% Achieved"),
            CreatorGoalItem("4", "First ₹5,000 Milestone", "₹5,000", false, "₹3,250 / ₹5,000"),
            CreatorGoalItem("5", "First ₹10,000 Master Target", "₹10,000", false, "₹3,250 / ₹10,000")
        )
    }

    var customGoalTitle by remember { mutableStateOf("") }
    var customGoalTarget by remember { mutableStateOf("") }
    var showAddGoalDialog by remember { mutableStateOf(false) }

    // Vault State
    val vaultItems = remember {
        mutableStateListOf(
            CreatorVaultItem("v1", "Viral Outfit Hook", "Hooks", "Stop buying clothes before watching these 3 mistakes! 🚫👗"),
            CreatorVaultItem("v2", "High Converting Reel Script", "Scripts", "Hook: Wishlink summer haul under ₹999!\nBody: Item 1 description...\nCTA: Comment 'LINK' for instant DM link."),
            CreatorVaultItem("v3", "Festive Wardrobe Caption", "Captions", "Elevate your festive look effortlessly with these top picks! 🛍️ link in bio & story."),
            CreatorVaultItem("v4", "DM Automation CTA", "CTAs", "Comment 'SHOP' below and my AI assistant will inbox you all outfit links directly! 👇"),
            CreatorVaultItem("v5", "Fashion & Lifestyle Hashtags", "Hashtags", "#WishlinkCreator #AffiliateFashion #OOTDIndia #MyntraFinds #Trends2026"),
            CreatorVaultItem("v6", "GRWM Story Idea", "Story Ideas", "Step-by-step Get Ready With Me showing Wishlink direct affiliate sticker link on story!"),
            CreatorVaultItem("v7", "Honest Sneaker Review", "Product Reviews", "Detailed durability & comfort review of Puma Rs-X sneakers with direct buy link."),
            CreatorVaultItem("v8", "7-Day Content Plan", "Weekly Plans", "Mon: Reel Haul | Wed: Story Carousel | Fri: Trend Remix | Sun: Live Q&A")
        )
    }

    var vaultSearchQuery by remember { mutableStateOf("") }
    var selectedVaultCategory by remember { mutableStateOf("All") }
    var showAddVaultDialog by remember { mutableStateOf(false) }
    var newVaultTitle by remember { mutableStateOf("") }
    var newVaultCategory by remember { mutableStateOf("Hooks") }
    var newVaultContent by remember { mutableStateOf("") }

    // Daily AI Coach State
    var dailyFocus by remember { mutableStateOf("High-intent audience engagement via Story Interactive Polls") }
    var dailyLearning by remember { mutableStateOf("Optimizing Wishlink link placement in story highlights & pinned reels") }
    var dailyImprovement by remember { mutableStateOf("Clearer CTA voiceover in the first 3 seconds of Reels") }
    var dailyMotivation by remember { mutableStateOf("Consistency is your superpower! Everyday effort compounds your affiliate income.") }
    var dailyBusinessTip by remember { mutableStateOf("Always group complementary fashion pieces into a single Wishlink collection for higher basket value!") }

    // Quick AI Tools Modal State
    var activeToolName by remember { mutableStateOf<String?>(null) }
    var toolInputText by remember { mutableStateOf("") }
    var toolOutputResult by remember { mutableStateOf("") }

    // Load saved preferences if available
    LaunchedEffect(Unit) {
        val savedGoalsJson = CreatorAcademyPrefs.getWishlinkLevel14GoalsJson(context)
        if (savedGoalsJson.isNotEmpty()) {
            try {
                val array = JSONArray(savedGoalsJson)
                goalsList.clear()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    goalsList.add(
                        CreatorGoalItem(
                            id = obj.optString("id", "$i"),
                            title = obj.getString("title"),
                            target = obj.getString("target"),
                            isCompleted = obj.getBoolean("isCompleted"),
                            progressText = obj.getString("progressText")
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveCurrentData() {
        val array = JSONArray()
        goalsList.forEach { item ->
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("title", item.title)
            obj.put("target", item.target)
            obj.put("isCompleted", item.isCompleted)
            obj.put("progressText", item.progressText)
            array.put(obj)
        }
        CreatorAcademyPrefs.saveWishlinkLevel14Data(context, array.toString(), "")
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
        // Floating Analytics Background Decor Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Soft luxury glow circles
            drawCircle(
                color = LuxuryPurpleAccent.copy(alpha = 0.12f + floatAnim * 0.05f),
                center = Offset(width * 0.2f, height * 0.15f),
                radius = width * 0.45f
            )
            drawCircle(
                color = LuxuryPurpleLight.copy(alpha = 0.1f + floatAnim * 0.04f),
                center = Offset(width * 0.85f, height * 0.55f),
                radius = width * 0.35f
            )
            drawCircle(
                color = LuxuryGold.copy(alpha = 0.06f + floatAnim * 0.03f),
                center = Offset(width * 0.4f, height * 0.85f),
                radius = width * 0.4f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Header Bar
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
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(LuxuryGlassWhite)
                        .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "XP",
                        tint = LuxuryGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$xpEarned XP",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "🔥 $streakDays Days",
                        color = Color(0xFFFFAB40),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Glass Header Card with 98% Base Progress Ring
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite),
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
                        modifier = Modifier.size(100.dp)
                    ) {
                        Canvas(modifier = Modifier.size(90.dp)) {
                            drawArc(
                                color = Color.White.copy(alpha = 0.15f),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                            )
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(LuxuryPurpleLight, LuxuryGold, LuxuryPurpleAccent, LuxuryPurpleLight)
                                ),
                                startAngle = -90f,
                                sweepAngle = 360f * 0.98f,
                                useCenter = false,
                                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "98%",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                            Text(
                                text = "Mastery",
                                color = LuxuryPurpleLight,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Creator Success Dashboard",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Your Complete AI Creator Business Hub",
                        color = LuxuryPurpleLight,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // AI MENTOR CARD (1400+ Conversation styles feel)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryPurpleAccent.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF23103D).copy(alpha = 0.9f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(LuxuryGold, LuxuryPurpleAccent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "AI Mentor",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Wishlink AI Mentor",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = LuxuryGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        val creatorName = userProfile["creatorName"] ?: userProfile["name"] ?: "Creator"
                        Text(
                            text = "\"Welcome back, $creatorName! Ab tumhare paas apna personal AI Creator Dashboard hai. Yahin se tum apni learning, business aur growth manage karoge.\"",
                            color = Color(0xFFE1D0FF),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 1: TODAY'S DASHBOARD
            SectionHeader(title = "SECTION 1: Today's Dashboard", icon = Icons.Default.Speed)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Current Level: Level 14 - Creator Master",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(LuxuryPurpleAccent.copy(alpha = 0.4f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "XP: $xpEarned",
                                color = LuxuryGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        DashboardStatMiniCard(
                            label = "Weekly Goal",
                            value = "5 Wishlink Reels",
                            modifier = Modifier.weight(1f)
                        )
                        DashboardStatMiniCard(
                            label = "Monthly Goal",
                            value = "₹5,000 Target",
                            modifier = Modifier.weight(1f)
                        )
                        DashboardStatMiniCard(
                            label = "Completion",
                            value = "98% Completed",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 2: DAILY AI COACH
            SectionHeader(title = "SECTION 2: Daily AI Coach", icon = Icons.Default.Lightbulb)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Today's AI Custom Advice",
                            color = LuxuryGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        IconButton(
                            onClick = {
                                val focuses = listOf(
                                    "High-converting Wishlink story interactive stickers",
                                    "Top 3 H&M Capsule lookbook reel script",
                                    "Direct DM automation keyword setup",
                                    "Broadening fashion sub-niche into footwear accessories"
                                )
                                val learnings = listOf(
                                    "Optimizing Wishlink link placement in story highlights",
                                    "Hook timing in the first 2 seconds of fashion reels",
                                    "Analyzing CTR (Click-through-rate) in Wishlink dashboard"
                                )
                                dailyFocus = focuses.random()
                                dailyLearning = learnings.random()
                                Toast.makeText(context, "New Daily AI Advice Generated!", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                        }
                    }

                    CoachDetailRow(label = "Today's Focus", text = dailyFocus)
                    CoachDetailRow(label = "Today's Learning", text = dailyLearning)
                    CoachDetailRow(label = "Today's Improvement", text = dailyImprovement)
                    CoachDetailRow(label = "Today's Motivation", text = dailyMotivation)
                    CoachDetailRow(label = "Business Tip", text = dailyBusinessTip)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 3: WEEKLY REVIEW
            SectionHeader(title = "SECTION 3: Weekly Review", icon = Icons.Default.Analytics)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Weekly Performance Index",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ScoreProgressItem(title = "Consistency Score", score = 95)
                    ScoreProgressItem(title = "Content Score", score = 92)
                    ScoreProgressItem(title = "Store Score", score = 90)
                    ScoreProgressItem(title = "Business Score", score = 88)
                    ScoreProgressItem(title = "Learning Score", score = 98)

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Educational Review: Exceptional progress across all modules. Your store layout and link placement are optimized for high affiliate conversion.",
                        color = Color(0xFFD1C4E9),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 4: MONTHLY GROWTH REPORT
            SectionHeader(title = "SECTION 4: Monthly Growth Report", icon = Icons.Default.TrendingUp)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    GrowthReportRow(title = "Strongest Skill", detail = "Reel Hook & Story Link Conversion")
                    GrowthReportRow(title = "Needs Improvement", detail = "DM Automation CTA Voiceovers")
                    GrowthReportRow(title = "Best Performing Habit", detail = "Posting 2 Wishlink Stories Daily")
                    GrowthReportRow(title = "Recommended Focus", detail = "Multi-item Outfit Capsule Collections")
                    GrowthReportRow(title = "Next Month Goal", detail = "Target ₹10,000 Total Affiliate Earnings")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 5: CREATOR GOALS (Editable & Manual Tracking)
            SectionHeader(title = "SECTION 5: Creator Goals", icon = Icons.Default.EmojiEvents)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Milestone Income Tracker",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        IconButton(onClick = { showAddGoalDialog = true }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Goal", tint = LuxuryGold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    goalsList.forEachIndexed { index, goal ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x1AFFFFFF))
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = goal.isCompleted,
                                onCheckedChange = { checked ->
                                    goalsList[index] = goal.copy(isCompleted = checked)
                                    saveCurrentData()
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = LuxuryGold,
                                    uncheckedColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = goal.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = goal.progressText,
                                    color = if (goal.isCompleted) Color(0xFF81C784) else LuxuryPurpleLight,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 6: ACHIEVEMENTS (Animated Badges)
            SectionHeader(title = "SECTION 6: Achievements", icon = Icons.Default.Star)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val achievements = listOf(
                    CreatorAchievement("Wishlink Ready", "Profile fully integrated", true, "🏆"),
                    CreatorAchievement("Dashboard Explorer", "All analytics mastered", true, "🚀"),
                    CreatorAchievement("Link Expert", "Deep links optimized", true, "🔗"),
                    CreatorAchievement("Store Builder", "Wishlink Store live", true, "🏪"),
                    CreatorAchievement("Analytics Explorer", "Audience metrics tracked", true, "📊"),
                    CreatorAchievement("Content Factory", "Weekly content scheduled", true, "🎬"),
                    CreatorAchievement("Business Builder", "Monetization active", true, "💼"),
                    CreatorAchievement("Brand Ready", "Media kit & portfolio ready", true, "⭐")
                )

                achievements.forEach { badge ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                1.dp,
                                if (badge.isUnlocked) LuxuryGold.copy(alpha = 0.6f) else LuxuryGlassBorder,
                                RoundedCornerShape(16.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (badge.isUnlocked) Color(0xFF2C1947) else Color(0x10FFFFFF)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = badge.icon, fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = badge.title,
                                color = if (badge.isUnlocked) LuxuryGold else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = badge.description,
                                color = Color.LightGray,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 7: CREATOR VAULT (Searchable Library)
            SectionHeader(title = "SECTION 7: Creator Vault", icon = Icons.Default.Bookmark)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Saved Content Vault",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        IconButton(onClick = { showAddVaultDialog = true }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add to Vault", tint = LuxuryGold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = vaultSearchQuery,
                        onValueChange = { vaultSearchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search hooks, scripts, captions...", color = Color.Gray, fontSize = 13.sp) },
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

                    Spacer(modifier = Modifier.height(12.dp))

                    val filteredVault = vaultItems.filter {
                        (selectedVaultCategory == "All" || it.category == selectedVaultCategory) &&
                                (vaultSearchQuery.isEmpty() || it.title.contains(vaultSearchQuery, ignoreCase = true) || it.content.contains(vaultSearchQuery, ignoreCase = true))
                    }

                    filteredVault.forEach { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(12.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color(0x20FFFFFF))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.title,
                                        color = LuxuryGold,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    IconButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("Vault Item", item.content)
                                            clipboard.setPrimaryClip(clip)
                                            Toast.makeText(context, "Copied to Clipboard!", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                }
                                Text(
                                    text = item.content,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 8: QUICK AI TOOLS
            SectionHeader(title = "SECTION 8: Quick AI Tools", icon = Icons.Default.AutoAwesome)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val tools = listOf(
                    "Generate Caption" to Icons.Default.Edit,
                    "Generate Script" to Icons.Default.RocketLaunch,
                    "Review Product" to Icons.Default.Star,
                    "Review Store" to Icons.Default.Verified,
                    "Business Advice" to Icons.Default.Lightbulb,
                    "Weekly Planner" to Icons.Default.Schedule,
                    "Portfolio Review" to Icons.Default.Analytics
                )

                tools.forEach { (toolName, icon) ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                activeToolName = toolName
                                toolInputText = ""
                                toolOutputResult = ""
                            }
                            .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = icon, contentDescription = toolName, tint = LuxuryGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = toolName, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 9: LEARNING TIMELINE
            SectionHeader(title = "SECTION 9: Learning Timeline", icon = Icons.Default.Timeline)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Completed Lessons: Level 1 - 13 (100% Passed)", color = Color(0xFF81C784), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Current Lesson: Level 14 - Creator Success Dashboard", color = LuxuryGold, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Remaining Lesson: Level 15 - Final Creator Graduation & Certification", color = LuxuryPurpleLight, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 10: REMINDER SYSTEM
            SectionHeader(title = "SECTION 10: Inactivity Reminder", icon = Icons.Default.Schedule)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFFFB74D).copy(alpha = 0.6f), RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0x25FF9800))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Lightbulb, contentDescription = "Reminder", tint = Color(0xFFFFD54F), modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "\"Sirf 15 minutes aaj continue karte hain. Continuous practice se tumhara conversion score 2x fast improve hoga!\"",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 11: CREATOR HEALTH METER
            SectionHeader(title = "SECTION 11: Creator Health Meter", icon = Icons.Default.Speed)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Overall Creator Health Score", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "94 / 100", color = LuxuryGold, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                    Text(text = "Status: Excellent (Top 2% Creator Category)", color = Color(0xFF81C784), fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(14.dp))
                    ScoreProgressItem(title = "Learning Meter", score = 98)
                    ScoreProgressItem(title = "Consistency Meter", score = 92)
                    ScoreProgressItem(title = "Business Meter", score = 90)
                    ScoreProgressItem(title = "Content Meter", score = 94)
                    ScoreProgressItem(title = "Optimization Meter", score = 92)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 12: AI GROWTH ROADMAP
            SectionHeader(title = "SECTION 12: AI Growth Roadmap", icon = Icons.Default.RocketLaunch)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = LuxuryGlassWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    RoadmapPhaseItem(period = "Next 7 Days", target = "Publish 3 Wishlink story links & test interactive poll stickers")
                    RoadmapPhaseItem(period = "Next 30 Days", target = "Launch 2 fashion reel series with direct DM automation keywords")
                    RoadmapPhaseItem(period = "Next 90 Days", target = "Reach ₹10,000 monthly affiliate commission & pitch to 5 brand partners")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // TODAY'S MISSION CARD & ACHIEVEMENT REWARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxuryGold, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2E1945))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "TODAY'S MISSION", color = LuxuryGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(text = "Review Your Success Dashboard", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Button(
                            onClick = {
                                missionCompleted = true
                                xpEarned += 1000
                                Toast.makeText(context, "Mission Completed! +1000 XP Earned! 🏆", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = if (missionCompleted) Color(0xFF4CAF50) else LuxuryGold),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = if (missionCompleted) "Completed ✓" else "Claim +1000 XP",
                                color = if (missionCompleted) Color.White else Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // BOTTOM NAVIGATION BUTTONS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .border(1.dp, LuxuryGlassBorder, RoundedCornerShape(26.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = LuxuryGlassWhite),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Text(text = "Back", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        saveCurrentData()
                        CreatorAcademyPrefs.completeWishlinkLevel14(context, 100, "", "")
                        onCompleteLevel14()
                    },
                    modifier = Modifier
                        .weight(1.5f)
                        .height(52.dp)
                        .shadow(12.dp, RoundedCornerShape(26.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = LuxuryPurpleAccent),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Continue To Graduation", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // QUICK AI TOOL DIALOG MODAL
        activeToolName?.let { toolName ->
            AlertDialog(
                onDismissRequest = { activeToolName = null },
                title = { Text(text = "AI Quick Tool: $toolName", color = LuxuryGold, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text(text = "Enter topic / details for instant generation:", color = Color.White, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = toolInputText,
                            onValueChange = { toolInputText = it },
                            placeholder = { Text("e.g., Festive Kurti Haul under ₹1499", color = Color.Gray, fontSize = 12.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (toolOutputResult.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "Generated AI Result:", color = LuxuryPurpleLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0x30FFFFFF))
                                    .padding(10.dp)
                            ) {
                                Text(text = toolOutputResult, color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val topic = toolInputText.ifEmpty { "Fashion Haul" }
                            toolOutputResult = when (toolName) {
                                "Generate Caption" -> "✨ Elevate your style with this $topic! High quality & affordable link in bio & story 🛍️ #WishlinkCreator"
                                "Generate Script" -> "🎬 Hook: Don't buy $topic until you watch this!\nBody: Here are 3 details you need to know...\nCTA: Comment 'LINK' below!"
                                "Review Product" -> "⭐ Product Audit ($topic): 9.5/10 Quality rating. Excellent click-through-rate potential on Wishlink!"
                                else -> "💡 Custom AI Recommendation for $topic: Post during 6 PM - 9 PM peak engagement hours with story stickers."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LuxuryPurpleAccent)
                    ) {
                        Text(text = "Generate", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { activeToolName = null }) {
                        Text(text = "Close", color = Color.Gray)
                    }
                },
                containerColor = Color(0xFF1E1035)
            )
        }

        // ADD GOAL DIALOG
        if (showAddGoalDialog) {
            AlertDialog(
                onDismissRequest = { showAddGoalDialog = false },
                title = { Text(text = "Add Custom Creator Goal", color = LuxuryGold, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = customGoalTitle,
                            onValueChange = { customGoalTitle = it },
                            label = { Text("Goal Title", color = Color.White) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = customGoalTarget,
                            onValueChange = { customGoalTarget = it },
                            label = { Text("Target Value (e.g., ₹15,000)", color = Color.White) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (customGoalTitle.isNotEmpty()) {
                                goalsList.add(
                                    CreatorGoalItem(
                                        id = "${System.currentTimeMillis()}",
                                        title = customGoalTitle,
                                        target = customGoalTarget.ifEmpty { "Target" },
                                        isCompleted = false,
                                        progressText = "0% Progress"
                                    )
                                )
                                saveCurrentData()
                                customGoalTitle = ""
                                customGoalTarget = ""
                                showAddGoalDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LuxuryPurpleAccent)
                    ) {
                        Text("Add Goal", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddGoalDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                },
                containerColor = Color(0xFF1E1035)
            )
        }

        // ADD VAULT ITEM DIALOG
        if (showAddVaultDialog) {
            AlertDialog(
                onDismissRequest = { showAddVaultDialog = false },
                title = { Text(text = "Save Custom Item to Vault", color = LuxuryGold, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newVaultTitle,
                            onValueChange = { newVaultTitle = it },
                            label = { Text("Item Title", color = Color.White) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newVaultContent,
                            onValueChange = { newVaultContent = it },
                            label = { Text("Content / Script / Text", color = Color.White) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newVaultTitle.isNotEmpty() && newVaultContent.isNotEmpty()) {
                                vaultItems.add(
                                    CreatorVaultItem(
                                        id = "v_${System.currentTimeMillis()}",
                                        title = newVaultTitle,
                                        category = newVaultCategory,
                                        content = newVaultContent
                                    )
                                )
                                newVaultTitle = ""
                                newVaultContent = ""
                                showAddVaultDialog = false
                                Toast.makeText(context, "Saved to Vault!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LuxuryPurpleAccent)
                    ) {
                        Text("Save", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddVaultDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                },
                containerColor = Color(0xFF1E1035)
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
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun DashboardStatMiniCard(label: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x1FFFFFFF))
            .padding(10.dp)
    ) {
        Column {
            Text(text = label, color = LuxuryPurpleLight, fontSize = 10.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CoachDetailRow(label: String, text: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, color = LuxuryPurpleLight, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        Text(text = text, color = Color.White, fontSize = 12.sp, lineHeight = 16.sp)
    }
}

@Composable
private fun ScoreProgressItem(title: String, score: Int) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, color = Color.White, fontSize = 12.sp)
            Text(text = "$score / 100", color = LuxuryGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0x20FFFFFF))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(score / 100f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(LuxuryPurpleAccent)
            )
        }
    }
}

@Composable
private fun GrowthReportRow(title: String, detail: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, color = LuxuryPurpleLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text(text = detail, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun RoadmapPhaseItem(period: String, target: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(text = period, color = LuxuryGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Text(text = target, color = Color.White, fontSize = 12.sp)
    }
}
