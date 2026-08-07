package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.creatoracademy.CreatorMission
import com.example.creatoracademy.CreatorMissionAchievement
import com.example.creatoracademy.CreatorMissionsEngine
import com.example.creatoracademy.MissionCategory
import com.example.creatoracademy.MissionRewardItem
import com.example.creatoracademy.MissionRewardType
import com.example.creatoracademy.ViriReaction
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

// ==================================================
// 1. COMPACT HOME SCREEN MISSION CARD WIDGET
// ==================================================
@Composable
fun CompactCreatorMissionsWidget(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val dailyMissions = remember { CreatorMissionsEngine.getDailyMissions(context) }
    val completedCount = dailyMissions.count { it.isCompleted }
    val totalCount = dailyMissions.size
    val progressFraction = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
    val streakData = remember { CreatorMissionsEngine.getStreakData(context) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val borderGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            }
            .testTag("compact_creator_missions_widget"),
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFF121622),
        border = BorderStroke(1.2.dp, CyanAccent.copy(alpha = borderGlowAlpha))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Column: Details & Streak
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "🎯 Creator Missions",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    // Streak Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x26FF6B00))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = Color(0xFFFF6B00),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${streakData.currentStreak}d",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF9E00)
                            )
                        }
                    }
                }

                Text(
                    text = "$completedCount/$totalCount Completed Today",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = CyanAccent
                )

                Text(
                    text = "Tap to complete daily creator goals & earn XP",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Right side: Apple Fitness Ring & Arrow
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Fitness Progress Ring
                Box(
                    modifier = Modifier.size(46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 5.dp.toPx()
                        // Outer track
                        drawCircle(
                            color = Color(0xFF1E293B),
                            style = Stroke(width = strokeWidth)
                        )
                        // Active Arc
                        drawArc(
                            color = CyanAccent,
                            startAngle = -90f,
                            sweepAngle = progressFraction * 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "${(progressFraction * 100).toInt()}%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E293B)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Open Missions",
                        tint = CyanAccent,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// ==================================================
// 2. FULL CREATOR MISSIONS DIALOG / SCREEN
// ==================================================
@Composable
fun CreatorMissionsDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    var activeTab by remember { mutableStateOf("DAILY") } // "DAILY", "WEEKLY", "MONTHLY", "REWARDS", "ACHIEVEMENTS"

    // Engine Data State
    var dailyMissions by remember { mutableStateOf(CreatorMissionsEngine.getDailyMissions(context)) }
    var weeklyMissions by remember { mutableStateOf(CreatorMissionsEngine.getWeeklyMissions(context)) }
    var monthlyMissions by remember { mutableStateOf(CreatorMissionsEngine.getMonthlyMissions(context)) }
    var levelData by remember { mutableStateOf(CreatorMissionsEngine.getCreatorLevelData(context)) }
    var streakData by remember { mutableStateOf(CreatorMissionsEngine.getStreakData(context)) }
    var achievements by remember { mutableStateOf(CreatorMissionsEngine.getAchievements(context)) }
    var rewardsCatalog by remember { mutableStateOf(CreatorMissionsEngine.getRewardsCatalog(context)) }

    // Viri mascot feedback & confetti state
    var currentViriReaction by remember {
        mutableStateOf<ViriReaction?>(
            ViriReaction(
                speechMessage = "Welcome to Creator Missions! Let's level up together today! 🚀",
                animationAction = "CELEBRATE",
                isCelebration = true
            )
        )
    }
    var showConfetti by remember { mutableStateOf(false) }

    fun refreshAllData() {
        dailyMissions = CreatorMissionsEngine.getDailyMissions(context)
        weeklyMissions = CreatorMissionsEngine.getWeeklyMissions(context)
        monthlyMissions = CreatorMissionsEngine.getMonthlyMissions(context)
        levelData = CreatorMissionsEngine.getCreatorLevelData(context)
        streakData = CreatorMissionsEngine.getStreakData(context)
        achievements = CreatorMissionsEngine.getAchievements(context)
        rewardsCatalog = CreatorMissionsEngine.getRewardsCatalog(context)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AmoledBlack)
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // TOP BAR: Title, Subtitle, Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "🎯 Creator Missions",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                        Text(
                            text = "Complete missions. Become a better creator.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // APPLE FITNESS RINGS & DUOLINGO STREAK HEADER
                FitnessRingsHeaderCard(
                    levelData = levelData,
                    streakData = streakData,
                    dailyCompleted = dailyMissions.count { it.isCompleted },
                    dailyTotal = dailyMissions.size,
                    weeklyCompleted = weeklyMissions.count { it.isCompleted },
                    weeklyTotal = weeklyMissions.size
                )

                Spacer(modifier = Modifier.height(14.dp))

                // TAB SELECTOR PILL ROW
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFF161B26))
                        .padding(4.dp)
                ) {
                    val tabs = listOf(
                        "DAILY" to "⚡ Daily",
                        "WEEKLY" to "📅 Weekly",
                        "MONTHLY" to "🏆 Monthly",
                        "REWARDS" to "🎁 Rewards",
                        "ACHIEVEMENTS" to "🔥 Badges"
                    )

                    tabs.forEach { (key, label) ->
                        val isSelected = activeTab == key
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (isSelected) CyanAccent else Color.Transparent)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    activeTab = key
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) AmoledBlack else TextPrimary,
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // MAIN TAB CONTENT AREA
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    when (activeTab) {
                        "DAILY" -> {
                            MissionsListTab(
                                missions = dailyMissions,
                                categoryTitle = "Today's Daily Missions",
                                categorySubtitle = "5 AI generated daily growth challenges",
                                onComplete = { missionId ->
                                    val result = CreatorMissionsEngine.completeMission(context, missionId)
                                    refreshAllData()
                                    currentViriReaction = result.viriReaction
                                    showConfetti = true
                                    scope.launch {
                                        delay(3000)
                                        showConfetti = false
                                    }
                                },
                                onSkip = { missionId ->
                                    val rx = CreatorMissionsEngine.skipMission(context, missionId)
                                    refreshAllData()
                                    currentViriReaction = rx
                                }
                            )
                        }

                        "WEEKLY" -> {
                            MissionsListTab(
                                missions = weeklyMissions,
                                categoryTitle = "Weekly Growth Goals",
                                categorySubtitle = "Sustained performance metrics for 7-day impact",
                                onComplete = { missionId ->
                                    val result = CreatorMissionsEngine.completeMission(context, missionId)
                                    refreshAllData()
                                    currentViriReaction = result.viriReaction
                                    showConfetti = true
                                    scope.launch {
                                        delay(3000)
                                        showConfetti = false
                                    }
                                },
                                onSkip = { missionId ->
                                    val rx = CreatorMissionsEngine.skipMission(context, missionId)
                                    refreshAllData()
                                    currentViriReaction = rx
                                }
                            )
                        }

                        "MONTHLY" -> {
                            MissionsListTab(
                                missions = monthlyMissions,
                                categoryTitle = "Monthly Mastery Quests",
                                categorySubtitle = "High XP long term creator achievements",
                                onComplete = { missionId ->
                                    val result = CreatorMissionsEngine.completeMission(context, missionId)
                                    refreshAllData()
                                    currentViriReaction = result.viriReaction
                                    showConfetti = true
                                    scope.launch {
                                        delay(3000)
                                        showConfetti = false
                                    }
                                },
                                onSkip = { missionId ->
                                    val rx = CreatorMissionsEngine.skipMission(context, missionId)
                                    refreshAllData()
                                    currentViriReaction = rx
                                }
                            )
                        }

                        "REWARDS" -> {
                            RewardsTabContent(
                                rewards = rewardsCatalog,
                                levelData = levelData
                            )
                        }

                        "ACHIEVEMENTS" -> {
                            AchievementsTabContent(
                                achievements = achievements,
                                onClaim = { achId ->
                                    CreatorMissionsEngine.claimAchievement(context, achId)
                                    refreshAllData()
                                    currentViriReaction = ViriReaction(
                                        speechMessage = "Achievement Unlocked! +XP Claimed! 🔥",
                                        animationAction = "CELEBRATE",
                                        isCelebration = true
                                    )
                                    showConfetti = true
                                    scope.launch {
                                        delay(3000)
                                        showConfetti = false
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // VIRI SPEECH & ANIMATION FOOTER
                if (currentViriReaction != null) {
                    ViriReactionBanner(
                        reaction = currentViriReaction!!
                    )
                }
            }

            // CONFETTI OVERLAY ANIMATION
            if (showConfetti) {
                ConfettiOverlay()
            }
        }
    }
}

// ==================================================
// 3. FITNESS RINGS & DUOLINGO STREAK HEADER
// ==================================================
@Composable
private fun FitnessRingsHeaderCard(
    levelData: com.example.creatoracademy.CreatorLevelData,
    streakData: com.example.creatoracademy.StreakData,
    dailyCompleted: Int,
    dailyTotal: Int,
    weeklyCompleted: Int,
    weeklyTotal: Int
) {
    val dailyProgress = if (dailyTotal > 0) dailyCompleted.toFloat() / dailyTotal else 0f
    val weeklyProgress = if (weeklyTotal > 0) weeklyCompleted.toFloat() / weeklyTotal else 0f
    val streakProgress = (streakData.currentStreak / 30f).coerceIn(0f, 1f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF131826),
        border = BorderStroke(1.dp, Color(0xFF262E40))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Triple Apple Fitness Style Rings Canvas
            Box(
                modifier = Modifier.size(76.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val centerPx = size.width / 2f

                    // Outer Ring (Daily - Orange)
                    drawCircle(
                        color = Color(0x33FF6B00),
                        radius = centerPx - 4.dp.toPx(),
                        style = Stroke(width = 6.dp.toPx())
                    )
                    drawArc(
                        color = Color(0xFFFF6B00),
                        startAngle = -90f,
                        sweepAngle = dailyProgress * 360f,
                        useCenter = false,
                        style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round),
                        size = androidx.compose.ui.geometry.Size((centerPx - 4.dp.toPx()) * 2, (centerPx - 4.dp.toPx()) * 2),
                        topLeft = androidx.compose.ui.geometry.Offset(4.dp.toPx(), 4.dp.toPx())
                    )

                    // Middle Ring (Weekly - Cyan)
                    drawCircle(
                        color = Color(0x3320D9E8),
                        radius = centerPx - 13.dp.toPx(),
                        style = Stroke(width = 6.dp.toPx())
                    )
                    drawArc(
                        color = CyanAccent,
                        startAngle = -90f,
                        sweepAngle = weeklyProgress * 360f,
                        useCenter = false,
                        style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round),
                        size = androidx.compose.ui.geometry.Size((centerPx - 13.dp.toPx()) * 2, (centerPx - 13.dp.toPx()) * 2),
                        topLeft = androidx.compose.ui.geometry.Offset(13.dp.toPx(), 13.dp.toPx())
                    )

                    // Inner Ring (Streak - Purple)
                    drawCircle(
                        color = Color(0x33A855F7),
                        radius = centerPx - 22.dp.toPx(),
                        style = Stroke(width = 6.dp.toPx())
                    )
                    drawArc(
                        color = ElectricPurple,
                        startAngle = -90f,
                        sweepAngle = streakProgress * 360f,
                        useCenter = false,
                        style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round),
                        size = androidx.compose.ui.geometry.Size((centerPx - 22.dp.toPx()) * 2, (centerPx - 22.dp.toPx()) * 2),
                        topLeft = androidx.compose.ui.geometry.Offset(22.dp.toPx(), 22.dp.toPx())
                    )
                }

                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Right Column: Level & XP Bar + Streak Banner
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Level ${levelData.level} • ${levelData.levelTitle}",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${levelData.currentXp} / ${levelData.xpForNextLevel} XP",
                            fontSize = 11.5.sp,
                            color = CyanAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Streak Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0x33FF6B00),
                        border = BorderStroke(1.dp, Color(0xFFFF6B00))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color(0xFFFF6B00),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${streakData.currentStreak} Day Streak",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF9E00)
                            )
                        }
                    }
                }

                // XP Progress Bar
                LinearProgressIndicator(
                    progress = { levelData.progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = CyanAccent,
                    trackColor = Color(0xFF1E293B)
                )
            }
        }
    }
}

// ==================================================
// 4. MISSIONS LIST TAB
// ==================================================
@Composable
private fun MissionsListTab(
    missions: List<CreatorMission>,
    categoryTitle: String,
    categorySubtitle: String,
    onComplete: (String) -> Unit,
    onSkip: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = categoryTitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = categorySubtitle,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            Text(
                text = "${missions.count { it.isCompleted }}/${missions.size} Done",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = CyanAccent
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(missions) { mission ->
                MissionCardItem(
                    mission = mission,
                    onComplete = { onComplete(mission.id) },
                    onSkip = { onSkip(mission.id) }
                )
            }
        }
    }
}

@Composable
private fun MissionCardItem(
    mission: CreatorMission,
    onComplete: () -> Unit,
    onSkip: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = if (mission.isCompleted) Color(0xFF0F1E1B) else Color(0xFF161B26),
        border = BorderStroke(
            1.dp,
            if (mission.isCompleted) EmeraldGlow.copy(alpha = 0.6f) else Color(0xFF262E40)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Mission Emoji Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (mission.isCompleted) Color(0x3310B981) else Color(0xFF1E293B)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mission.iconEmoji,
                    fontSize = 20.sp
                )
            }

            // Info Column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = mission.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    // XP Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x3320D9E8))
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "+${mission.xpReward} XP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    }
                }

                Text(
                    text = mission.description,
                    fontSize = 11.5.sp,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (mission.rewardTitle.isNotBlank()) {
                    Text(
                        text = "🎁 Reward: ${mission.rewardTitle}",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = ElectricPurple
                    )
                }
            }

            // Action Buttons
            if (mission.isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = EmeraldGlow,
                    modifier = Modifier.size(28.dp)
                )
            } else if (mission.isSkipped) {
                Text(
                    text = "Skipped",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onComplete()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = "Done ✔",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmoledBlack
                        )
                    }

                    Text(
                        text = "Skip ⏩",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.clickable { onSkip() }
                    )
                }
            }
        }
    }
}

// ==================================================
// 5. REWARDS TAB CONTENT
// ==================================================
@Composable
private fun RewardsTabContent(
    rewards: List<MissionRewardItem>,
    levelData: com.example.creatoracademy.CreatorLevelData
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "🎁 Creator Reward Store",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Unlock exclusive themes, frames, robot accessories & icons as you level up",
            fontSize = 11.sp,
            color = TextSecondary
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(rewards) { item ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF161B26),
                    border = BorderStroke(
                        1.dp,
                        if (item.isUnlocked) CyanAccent.copy(alpha = 0.6f) else Color(0xFF262E40)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        item.previewGradient.map { Color(it) }
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = item.iconEmoji, fontSize = 22.sp)
                        }

                        Text(
                            text = item.title,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = item.description,
                            fontSize = 10.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (item.isUnlocked) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x3310B981))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "UNLOCKED ✨",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldGlow
                                )
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "Requires Lv ${item.requiredLevel}",
                                    fontSize = 10.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================================================
// 6. ACHIEVEMENTS TAB CONTENT
// ==================================================
@Composable
private fun AchievementsTabContent(
    achievements: List<CreatorMissionAchievement>,
    onClaim: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "🔥 Creator Badges & Trophies",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Milestones achieved during your viral content journey",
            fontSize = 11.sp,
            color = TextSecondary
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(achievements) { ach ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF161B26),
                    border = BorderStroke(
                        1.dp,
                        if (ach.isUnlocked) Color(0xFFFF6B00) else Color(0xFF262E40)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = ach.iconEmoji,
                            fontSize = 28.sp
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ach.title,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = ach.description,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        if (ach.isUnlocked) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x33FF6B00))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "🔥 BADGE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF9E00)
                                )
                            }
                        } else {
                            Button(
                                onClick = { onClaim(ach.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text(
                                    text = "Claim +${ach.rewardXp} XP",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================================================
// 7. VIRI REACTION BANNER
// ==================================================
@Composable
private fun ViriReactionBanner(
    reaction: ViriReaction
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF181F30),
        border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ViriMascotWidget(size = 32.dp)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Viri AI Mascot",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanAccent
                )
                Text(
                    text = reaction.speechMessage,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            }
        }
    }
}

// ==================================================
// 8. CONFETTI OVERLAY ANIMATION
// ==================================================
@Composable
private fun ConfettiOverlay() {
    val particles = remember {
        List(25) {
            ConfettiParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat() * 0.3f,
                color = listOf(CyanAccent, ElectricPurple, EmeraldGlow, Color(0xFFFF6B00), Color(0xFFFFD700)).random(),
                radius = Random.nextFloat() * 6f + 4f
            )
        }
    }

    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1800, easing = FastOutSlowInEasing)
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { p ->
            val curY = size.height * (p.y + animProgress.value * 0.7f)
            val curX = size.width * p.x
            drawCircle(
                color = p.color.copy(alpha = (1f - animProgress.value).coerceIn(0f, 1f)),
                radius = p.radius.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(curX, curY)
            )
        }
    }
}

private data class ConfettiParticle(
    val x: Float,
    val y: Float,
    val color: Color,
    val radius: Float
)
