package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.draw.shadow
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.ElectricPurple
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.creatoracademy.AchievementBadge
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.creatoracademy.CreatorBusinessEngine
import com.example.creatoracademy.CreatorGoal
import com.example.creatoracademy.CreatorSetupData
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite

/**
 * MASTER PHASE 18 — Creator Business Hub Dialog
 * Professional Creator OS Dashboard: Goals Tracking, Achievement Badges System,
 * Real AI Business Insights, and Coming Soon Business OS Tools Previews.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreatorBusinessHubDialog(
    setupData: CreatorSetupData,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val platformPath = setupData.targetPlatform.uppercase()
    val xp = CreatorAcademyPrefs.getXpPoints(context, platformPath)
    val streak = CreatorAcademyPrefs.getStreakDays(context, platformPath)
    val completedTasks = CreatorAcademyPrefs.getCompletedTasks(context, platformPath)
    val completedCount = completedTasks.size

    // Determine Level based on XP
    val levelNumber = (xp / 100) + 1
    val levelTitle = when {
        levelNumber <= 1 -> "Emerging Creator"
        levelNumber <= 3 -> "Growth Creator"
        levelNumber <= 5 -> "Pro Creator"
        else -> "Creator Mogul"
    }

    var activeTab by remember { mutableStateOf("DASHBOARD") } // "DASHBOARD" or "TOOLS"

    var goalsList by remember { mutableStateOf(CreatorBusinessEngine.getGoals(context)) }
    var badgesList by remember {
        mutableStateOf(CreatorBusinessEngine.getBadges(context, streak, completedCount, xp))
    }
    val insights = remember(streak, completedCount, goalsList) {
        CreatorBusinessEngine.getBusinessInsights(context, streak, completedCount, goalsList)
    }

    // Custom Goal Dialog State
    var showAddGoalDialog by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.80f))
                .clickable(onClick = onDismiss)
                .navigationBarsPadding()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color(0xFF0F1A14),
                border = BorderStroke(
                    1.5.dp,
                    Brush.linearGradient(
                        listOf(EmeraldGlow, ElectricPurple.copy(alpha = 0.6f), EmeraldPrimary)
                    )
                ),
                modifier = Modifier
                    .widthIn(max = 520.dp)
                    .fillMaxWidth(0.94f)
                    .fillMaxHeight(0.74f)
                    .shadow(24.dp, RoundedCornerShape(28.dp), spotColor = EmeraldGlow)
                    .clickable(enabled = false) {}
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x2210B981))
                                    .border(BorderStroke(1.dp, EmeraldPrimary), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BusinessCenter,
                                    contentDescription = "Business Hub",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Creator Business Hub™",
                                    fontSize = 16.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                                Text(
                                    text = "Level $levelNumber • $levelTitle ($xp XP)",
                                    fontSize = 10.5.sp,
                                    color = EmeraldPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x22FFFFFF))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    onDismiss()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tab Selector: Dashboard vs OS Tools
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x1AFFFFFF))
                            .padding(3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (activeTab == "DASHBOARD") EmeraldPrimary else Color.Transparent)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    activeTab = "DASHBOARD"
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Analytics,
                                    contentDescription = "Dashboard",
                                    tint = if (activeTab == "DASHBOARD") AmoledBlack else TextWhite,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Progress OS",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeTab == "DASHBOARD") AmoledBlack else TextWhite
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (activeTab == "TOOLS") EmeraldPrimary else Color.Transparent)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    activeTab = "TOOLS"
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Tools",
                                    tint = if (activeTab == "TOOLS") AmoledBlack else TextWhite,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Business Tools (5)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeTab == "TOOLS") AmoledBlack else TextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tab Content Body
                    Box(modifier = Modifier.weight(1f)) {
                        if (activeTab == "DASHBOARD") {
                            DashboardProgressView(
                                levelNumber = levelNumber,
                                levelTitle = levelTitle,
                                xp = xp,
                                streak = streak,
                                completedCount = completedCount,
                                goals = goalsList,
                                badges = badgesList,
                                insights = insights,
                                onAddGoalClick = { showAddGoalDialog = true },
                                onUpdateGoal = { id, newVal ->
                                    CreatorBusinessEngine.updateGoalProgress(context, id, newVal)
                                    goalsList = CreatorBusinessEngine.getGoals(context)
                                },
                                onDeleteGoal = { id ->
                                    CreatorBusinessEngine.deleteGoal(context, id)
                                    goalsList = CreatorBusinessEngine.getGoals(context)
                                    Toast.makeText(context, "Goal Removed", Toast.LENGTH_SHORT).show()
                                },
                                onToggleBadge = { badgeId ->
                                    CreatorBusinessEngine.toggleManualBadge(context, badgeId)
                                    badgesList = CreatorBusinessEngine.getBadges(context, streak, completedCount, xp)
                                }
                            )
                        } else {
                            BusinessOsToolsView()
                        }
                    }
                }
            }
        }
    }

    // Add Goal Dialog
    if (showAddGoalDialog) {
        AddCustomGoalDialog(
            onDismiss = { showAddGoalDialog = false },
            onAddGoal = { newGoal ->
                CreatorBusinessEngine.addGoal(context, newGoal)
                goalsList = CreatorBusinessEngine.getGoals(context)
                showAddGoalDialog = false
                Toast.makeText(context, "🎯 New Goal Added!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

// ==================================================
// DASHBOARD & PROGRESS OS SUBVIEW
// ==================================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DashboardProgressView(
    levelNumber: Int,
    levelTitle: String,
    xp: Int,
    streak: Int,
    completedCount: Int,
    goals: List<CreatorGoal>,
    badges: List<AchievementBadge>,
    insights: List<com.example.creatoracademy.BusinessInsight>,
    onAddGoalClick: () -> Unit,
    onUpdateGoal: (String, Int) -> Unit,
    onDeleteGoal: (String) -> Unit,
    onToggleBadge: (String) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val xpInLevel = xp % 100
    val xpProgress = xpInLevel / 100f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Progress Dashboard Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x18FFFFFF))
                .border(BorderStroke(1.2.dp, EmeraldPrimary.copy(alpha = 0.7f)), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Level",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "PROGRESS DASHBOARD",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = EmeraldPrimary,
                            letterSpacing = 1.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(EmeraldPrimary)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Level $levelNumber",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = AmoledBlack
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // XP Progress Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$levelTitle",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "$xp / ${(levelNumber) * 100} XP",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { xpProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = EmeraldPrimary,
                    trackColor = Color(0x22FFFFFF)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Grid of 4 Key OS Metrics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricBox(
                        label = "STREAK",
                        value = "$streak Days 🔥",
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        label = "LESSONS",
                        value = "$completedCount Done 📚",
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        label = "ACTIVE GOALS",
                        value = "${goals.count { !it.isCompleted }} Goals 🎯",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 2. Real AI Business Insights Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x12FFFFFF))
                .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "AI Insights",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "REAL AI BUSINESS INSIGHTS",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        letterSpacing = 0.8.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                insights.forEach { insight ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "💡 ",
                            fontSize = 11.sp
                        )
                        Column {
                            Text(
                                text = insight.title,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = insight.message,
                                fontSize = 10.5.sp,
                                color = TextWhite.copy(alpha = 0.7f),
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // 3. Creator Goals Tracker Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x12FFFFFF))
                .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = "Goals",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CREATOR GOALS TRACKER",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(EmeraldPrimary)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onAddGoalClick()
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Goal",
                                tint = AmoledBlack,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "New Goal",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Black,
                                color = AmoledBlack
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (goals.isEmpty()) {
                    Text(
                        text = "No active goals created yet. Click 'New Goal' to set your first milestone!",
                        fontSize = 11.sp,
                        color = TextWhite.copy(alpha = 0.5f)
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        goals.forEach { goal ->
                            GoalProgressCard(
                                goal = goal,
                                onUpdateGoal = { newVal -> onUpdateGoal(goal.id, newVal) },
                                onDeleteGoal = { onDeleteGoal(goal.id) }
                            )
                        }
                    }
                }
            }
        }

        // 4. Achievement System (Badges) Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x12FFFFFF))
                .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Badges",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ACHIEVEMENT SYSTEM BADGES",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        letterSpacing = 0.8.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    badges.forEach { badge ->
                        BadgeItemCard(
                            badge = badge,
                            onClick = {
                                onToggleBadge(badge.id)
                                Toast.makeText(
                                    context,
                                    if (badge.isUnlocked) "Badge Details" else "Badge Progress: ${badge.progressText}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x1AFFFFFF))
            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(12.dp))
            .padding(vertical = 8.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite
            )
        }
    }
}

@Composable
private fun GoalProgressCard(
    goal: CreatorGoal,
    onUpdateGoal: (Int) -> Unit,
    onDeleteGoal: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val progressRatio = if (goal.targetValue > 0) (goal.currentValue.toFloat() / goal.targetValue).coerceIn(0f, 1f) else 0f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0x18FFFFFF))
            .border(BorderStroke(1.dp, if (goal.isCompleted) EmeraldPrimary else Color(0x22FFFFFF)), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (goal.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Done",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        text = goal.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${goal.currentValue} / ${goal.targetValue} ${goal.unit}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (goal.isCompleted) EmeraldPrimary else TextWhite.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Goal",
                        tint = TextWhite.copy(alpha = 0.4f),
                        modifier = Modifier
                            .size(14.dp)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onDeleteGoal()
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progressRatio },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(CircleShape),
                color = EmeraldPrimary,
                trackColor = Color(0x22FFFFFF)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Progress Increment Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Update Progress: ",
                    fontSize = 10.sp,
                    color = TextWhite.copy(alpha = 0.5f)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x22FFFFFF))
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onUpdateGoal(goal.currentValue + 1)
                        }
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("+1", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }

                Spacer(modifier = Modifier.width(4.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x22FFFFFF))
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onUpdateGoal(goal.currentValue + 10)
                        }
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("+10", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }

                Spacer(modifier = Modifier.width(4.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(EmeraldPrimary)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onUpdateGoal(goal.targetValue)
                        }
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("Complete", fontSize = 10.sp, fontWeight = FontWeight.Black, color = AmoledBlack)
                }
            }
        }
    }
}

@Composable
private fun BadgeItemCard(
    badge: AchievementBadge,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "badgeScale"
    )

    Box(
        modifier = Modifier
            .width(135.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(14.dp))
            .background(if (badge.isUnlocked) Color(0x2210B981) else Color(0x12FFFFFF))
            .border(
                BorderStroke(1.dp, if (badge.isUnlocked) EmeraldPrimary else Color(0x22FFFFFF)),
                RoundedCornerShape(14.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (badge.isUnlocked) EmeraldPrimary else Color(0x22FFFFFF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (badge.iconType) {
                        "REEL" -> Icons.Default.Star
                        "STREAK_7", "STREAK_30" -> Icons.Default.EmojiEvents
                        "LESSONS_100" -> Icons.Default.Description
                        "AFFILIATE" -> Icons.Default.Percent
                        else -> Icons.Default.Work
                    },
                    contentDescription = badge.title,
                    tint = if (badge.isUnlocked) AmoledBlack else TextWhite.copy(alpha = 0.5f),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = badge.title,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = badge.progressText,
                fontSize = 9.sp,
                color = if (badge.isUnlocked) EmeraldPrimary else TextWhite.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// ==================================================
// BUSINESS OS TOOLS SUBVIEW (5 LOCKED / COMING SOON MODULES)
// ==================================================
@Composable
private fun BusinessOsToolsView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Intro Glass Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x18FFFFFF))
                .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "OS",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PROFESSIONAL CREATOR BUSINESS OS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = EmeraldPrimary,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Upcoming automation tools designed to help creators turn views into a sustainable business.",
                    fontSize = 11.sp,
                    color = TextWhite.copy(alpha = 0.7f),
                    lineHeight = 15.sp
                )
            }
        }

        // 1. Media Kit Builder
        ComingSoonToolCard(
            moduleName = "Media Kit Builder",
            icon = Icons.Default.Description,
            description = "Build a sleek 1-page media kit summarizing your reach, demographics, and brand packages.",
            previewFeatures = listOf(
                "✓ Professional Media Kit Layout",
                "✓ Brand Introduction & Creator Bio",
                "✓ Creator Audience Summary",
                "✓ Deliverables & Rate Cards",
                "✓ Contact Information",
                "✓ Future PDF Export"
            )
        )

        // 2. Creator Portfolio
        ComingSoonToolCard(
            moduleName = "Creator Portfolio",
            icon = Icons.Default.Work,
            description = "Showcase your top performing viral reels, UGC samples, and brand campaign results.",
            previewFeatures = listOf(
                "✓ Interactive Portfolio Builder",
                "✓ Showcase Best Work & UGC Samples",
                "✓ Campaign Case Studies",
                "✓ Future Public Portfolio Link (creator.os/you)"
            )
        )

        // 3. Pricing Calculator
        ComingSoonToolCard(
            moduleName = "Pricing Calculator",
            icon = Icons.Default.Percent,
            description = "AI framework explaining how creators calculate fair rates based on reach, usage rights, and effort.",
            previewFeatures = listOf(
                "✓ Educational Pricing Formula Guide",
                "✓ Factor in Usage Rights & Exclusivity",
                "✓ Deliverables Complexity Multipliers",
                "✓ Principles Only — Never Fake Market Prices"
            )
        )

        // 4. Invoice Generator
        ComingSoonToolCard(
            moduleName = "Invoice Generator",
            icon = Icons.Default.Receipt,
            description = "Generate clean, professional invoices for brand sponsorships and agency contracts.",
            previewFeatures = listOf(
                "✓ Professional Creator Invoices",
                "✓ Payment Net-30 Terms & Bank Details",
                "✓ Tax ID & Client Metadata",
                "✓ Future PDF Export & Tracker"
            )
        )

        // 5. Contract Assistant
        ComingSoonToolCard(
            moduleName = "Contract Assistant",
            icon = Icons.Default.Gavel,
            description = "Learn essential contract terms, spot red flags, and master deal negotiation basics.",
            previewFeatures = listOf(
                "✓ Usage Rights & Exclusivity Explained",
                "✓ Red Flags to Avoid in Brand Agreements",
                "✓ Negotiation Principles & Revisions Clause",
                "✓ Educational Guide (No Legal Advice)"
            )
        )
    }
}

@Composable
private fun ComingSoonToolCard(
    moduleName: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    previewFeatures: List<String>
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var isNotified by remember { mutableStateOf(CreatorBusinessEngine.isNotified(context, moduleName)) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0x12FFFFFF))
            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = moduleName,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = moduleName,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x22FFFFFF))
                        .border(BorderStroke(1.dp, Color(0x33FFFFFF)), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = TextWhite.copy(alpha = 0.7f),
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "COMING SOON",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite.copy(alpha = 0.7f),
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                fontSize = 11.sp,
                color = TextWhite.copy(alpha = 0.7f),
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Feature Checklist Preview
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                previewFeatures.forEach { feature ->
                    Text(
                        text = feature,
                        fontSize = 10.5.sp,
                        color = TextWhite.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Early Access Banner & Notify Button Row
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x1810B981))
                    .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f)), RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🚀 Early Access Pricing",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                        Text(
                            text = "Receive exclusive launch discount.",
                            fontSize = 9.5.sp,
                            color = TextWhite.copy(alpha = 0.6f)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isNotified) Color(0x33FFFFFF) else EmeraldPrimary)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (!isNotified) {
                                    CreatorBusinessEngine.registerNotifyInterest(context, moduleName)
                                    isNotified = true
                                    Toast.makeText(context, "🔔 You'll be notified first when $moduleName launches!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isNotified) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                                contentDescription = "Notify",
                                tint = if (isNotified) EmeraldPrimary else AmoledBlack,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isNotified) "Notified ✓" else "Notify Me",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isNotified) TextWhite else AmoledBlack
                            )
                        }
                    }
                }
            }
        }
    }
}

// Add Custom Goal Dialog
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddCustomGoalDialog(
    onDismiss: () -> Unit,
    onAddGoal: (CreatorGoal) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    var titleInput by remember { mutableStateOf("") }
    var targetInput by remember { mutableStateOf("100") }
    var unitInput by remember { mutableStateOf("Followers") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF0F151F),
            border = BorderStroke(1.2.dp, EmeraldPrimary),
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Create Creator Goal 🎯",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    label = { Text("Goal Title", color = TextWhite.copy(alpha = 0.6f)) },
                    placeholder = { Text("e.g. Reach 5,000 YouTube Subscribers", color = TextWhite.copy(alpha = 0.4f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = targetInput,
                        onValueChange = { targetInput = it.filter { char -> char.isDigit() } },
                        label = { Text("Target", color = TextWhite.copy(alpha = 0.6f)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = Color(0x33FFFFFF),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = unitInput,
                        onValueChange = { unitInput = it },
                        label = { Text("Unit", color = TextWhite.copy(alpha = 0.6f)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = Color(0x33FFFFFF),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Presets Quick Selector
                Text(
                    text = "Goal Presets:",
                    fontSize = 11.sp,
                    color = TextWhite.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CreatorBusinessEngine.PRESET_GOALS.forEach { preset ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x1AFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(8.dp))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    titleInput = preset.title
                                    targetInput = preset.targetValue.toString()
                                    unitInput = preset.unit
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "✦ ${preset.title}",
                                fontSize = 10.sp,
                                color = TextWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x22FFFFFF))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Cancel", fontSize = 12.sp, color = TextWhite)
                    }

                    val canSave = titleInput.isNotBlank() && targetInput.isNotBlank()
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (canSave) EmeraldPrimary else Color(0x33FFFFFF))
                            .clickable(enabled = canSave) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                val targetVal = targetInput.toIntOrNull() ?: 100
                                onAddGoal(
                                    CreatorGoal(
                                        title = titleInput.trim(),
                                        targetValue = targetVal,
                                        unit = unitInput.trim().ifEmpty { "Units" }
                                    )
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Save Goal",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (canSave) AmoledBlack else TextWhite.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}
