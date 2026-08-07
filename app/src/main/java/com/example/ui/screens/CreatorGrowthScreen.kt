package com.example.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.creatoracademy.AnalysedReel
import com.example.ui.components.AiAudiencePersonaDialog
import com.example.ui.components.CompactCreatorMissionsWidget
import com.example.ui.components.CreatorMissionsDialog
import com.example.creatoracademy.CreatorBadgeItem
import com.example.creatoracademy.CreatorGrowthEngine
import com.example.creatoracademy.CreatorGrowthLevel
import com.example.creatoracademy.CreatorGrowthStats
import com.example.creatoracademy.SmartChallenge
import com.example.ui.components.ViriMascotWidget
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CyanAccent

private val GlassBg = Color(0x1AFFFFFF)
private val GlassBorder = Color(0x3322D3EE)
private val CardBg = Color(0xFF121622)
private val CyanGlow = Color(0x2222D3EE)
private val TextWhite = Color(0xFFFFFFFF)
private val TextGray = Color(0xFF94A3B8)
private val GreenSuccess = Color(0xFF10B981)
private val WarningOrange = Color(0xFFF59E0B)

/**
 * DS-26 — CREATOR GROWTH MEMORY ENGINE
 * Permanent AI Memory tab for creators. Remembers every reel, tracks growth trends,
 * recurring mistakes, strengths, badges, challenges, Viri mentor history, and yearly milestones.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreatorGrowthScreen(
    onOpenReelInEditor: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    if (hasError) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF141824),
            border = BorderStroke(1.dp, Color(0xFFEF4444))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = "Something went wrong.",
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (errorMessage.isNotBlank()) errorMessage else "Creator Growth memory encountered an unexpected issue.",
                    color = TextGray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = {
                        hasError = false
                        errorMessage = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent)
                ) {
                    Text("Retry", color = AmoledBlack, fontWeight = FontWeight.Bold)
                }
            }
        }
    } else {
        // State from CreatorGrowthEngine with safe fallbacks
        var reelsHistory by remember {
            mutableStateOf(
                try {
                    CreatorGrowthEngine.getAnalysedReels(context)
                } catch (e: Exception) {
                    emptyList()
                }
            )
        }
        var levelInfo by remember {
            mutableStateOf(
                try {
                    CreatorGrowthEngine.getCreatorLevel(context)
                } catch (e: Exception) {
                    CreatorGrowthLevel("Beginner", 0, 300, 0f, "🌱")
                }
            )
        }
        var stats by remember {
            mutableStateOf(
                try {
                    CreatorGrowthEngine.getGrowthStats(context)
                } catch (e: Exception) {
                    CreatorGrowthStats(0, 0, 0, 0, 0)
                }
            )
        }
        var badges by remember {
            mutableStateOf(
                try {
                    CreatorGrowthEngine.getCreatorBadges(context)
                } catch (e: Exception) {
                    emptyList()
                }
            )
        }
        var challenges by remember {
            mutableStateOf(
                try {
                    CreatorGrowthEngine.getSmartChallenges(context)
                } catch (e: Exception) {
                    emptyList()
                }
            )
        }

        val topMistakes = remember {
            try {
                CreatorGrowthEngine.getTopRecurringMistakes(context)
            } catch (e: Exception) {
                emptyList()
            }
        }
        val improvements = remember {
            try {
                CreatorGrowthEngine.getBiggestImprovements(context)
            } catch (e: Exception) {
                emptyList()
            }
        }
        val monthlyReport = remember {
            try {
                CreatorGrowthEngine.getMonthlyReport(context)
            } catch (e: Exception) {
                com.example.creatoracademy.MonthlyReportData("This Month", 0, 0, 0, 0, 0, 0)
            }
        }
        val viriConversations = remember {
            try {
                CreatorGrowthEngine.getCoachMemoryConversations(context)
            } catch (e: Exception) {
                emptyList()
            }
        }
        val yearlyTimeline = remember {
            try {
                CreatorGrowthEngine.getYearlyTimeline(context)
            } catch (e: Exception) {
                emptyList()
            }
        }

        // Dialog state
        var selectedReelForReport by remember { mutableStateOf<AnalysedReel?>(null) }
        var showExportPdfDialog by remember { mutableStateOf(false) }
        var showMissionsDialog by remember { mutableStateOf(false) }

        // Graph time filter: 7 Days, 30 Days, 90 Days, All Time
        var selectedGraphFilter by remember { mutableStateOf("30 Days") }
        var selectedGraphMetric by remember { mutableStateOf("Hook") }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ==================================================
            // TITLE & HEADER BAR
            // ==================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "📈 Creator Growth",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = (-0.5).sp
                        )
                    }
                    Text(
                        text = "Your AI remembers every reel.",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = CyanAccent
                    )
                }

                // Cloud Sync Status Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0x1E22D3EE),
                    border = BorderStroke(1.dp, Color(0x4422D3EE))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(GreenSuccess)
                        )
                        Text(
                            text = "☁️ Synced",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }
            }

            // ==================================================
            // DS-27: AI CREATOR MISSIONS COMPACT WIDGET
            // ==================================================
            CompactCreatorMissionsWidget(
                onClick = { showMissionsDialog = true }
            )

            // ==================================================
            // SECTION 1: CREATOR DASHBOARD TOP CARD
            // ==================================================
            CreatorDashboardTopCard(
                levelInfo = levelInfo,
                stats = stats
            )

            // ==================================================
            // SECTION 2: 30 REEL HISTORY
            // ==================================================
            Reels30HistorySection(
                reels = reelsHistory,
                onReelClick = { reel ->
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    selectedReelForReport = reel
                }
            )

            // ==================================================
            // SECTION 3: IMPROVEMENT GRAPH (Apple Health Style)
            // ==================================================
            ImprovementGraphSection(
                selectedFilter = selectedGraphFilter,
                onFilterSelect = { selectedGraphFilter = it },
                selectedMetric = selectedGraphMetric,
                onMetricSelect = { selectedGraphMetric = it },
                reels = reelsHistory
            )

            // ==================================================
            // SECTION 4: AI MEMORY (RECURRING MISTAKES)
            // ==================================================
            AiMemoryMistakesSection(mistakes = topMistakes)

            // ==================================================
            // SECTION 5: BIGGEST IMPROVEMENTS
            // ==================================================
            BiggestImprovementsSection(improvements = improvements)

            // ==================================================
            // SECTION 6: MONTHLY REPORT
            // ==================================================
            MonthlyReportSection(report = monthlyReport)

            // ==================================================
            // SECTION 7: CREATOR BADGES
            // ==================================================
            CreatorBadgesSection(badges = badges)

            // ==================================================
            // SECTION 8: AI COACH MEMORY (VIRI)
            // ==================================================
            AiCoachMemorySection(conversations = viriConversations)

            // ==================================================
            // SECTION 9: SMART CHALLENGES
            // ==================================================
            SmartChallengesSection(
                challenges = challenges,
                onCompleteChallenge = { challengeId ->
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    CreatorGrowthEngine.completeChallenge(context, challengeId)
                    challenges = CreatorGrowthEngine.getSmartChallenges(context)
                    levelInfo = CreatorGrowthEngine.getCreatorLevel(context)
                    stats = CreatorGrowthEngine.getGrowthStats(context)
                    Toast.makeText(context, "Challenge Completed! +XP Rewarded 🎉", Toast.LENGTH_SHORT).show()
                }
            )

            // ==================================================
            // SECTION 10: YEARLY TIMELINE
            // ==================================================
            YearlyTimelineSection(timeline = yearlyTimeline)

            // ==================================================
            // SECTION 11: EXPORT REPORT BUTTON
            // ==================================================
            Surface(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    showExportPdfDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = CyanAccent),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF101C28),
                border = BorderStroke(1.2.dp, CyanAccent)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0x2222D3EE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = "Export PDF",
                                tint = CyanAccent,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Export Creator Portfolio PDF",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Generate brand-ready pitch deck report",
                                fontSize = 11.5.sp,
                                color = TextGray
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = CyanAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // ==================================================
            // SECTION 12: SYNC BANNER
            // ==================================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF0F141C),
                border = BorderStroke(1.dp, Color(0x22FFFFFF))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudDone,
                        contentDescription = "Sync",
                        tint = CyanAccent,
                        modifier = Modifier.size(22.dp)
                    )
                    Column {
                        Text(
                            text = "Automatic Account Sync Active",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "All 30 reels, scores, badges & timeline are safely preserved across devices.",
                            fontSize = 11.sp,
                            color = TextGray
                        )
                    }
                }
            }
        }

        // Modal: Creator Missions Dialog
        if (showMissionsDialog) {
            CreatorMissionsDialog(
                onDismiss = { showMissionsDialog = false }
            )
        }

        // Modal: Selected Reel Report Detail
        if (selectedReelForReport != null) {
            ReelReportDetailDialog(
                reel = selectedReelForReport!!,
                onDismiss = { selectedReelForReport = null }
            )
        }

        // Modal: Export Portfolio Dialog
        if (showExportPdfDialog) {
            ExportPortfolioDialog(
                levelInfo = levelInfo,
                stats = stats,
                monthlyReport = monthlyReport,
                topMistakes = topMistakes,
                improvements = improvements,
                onDismiss = { showExportPdfDialog = false }
            )
        }
    }
}

// ==================================================
// SUB-COMPONENTS
// ==================================================

@Composable
private fun CreatorDashboardTopCard(
    levelInfo: CreatorGrowthLevel,
    stats: CreatorGrowthStats
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(28.dp), spotColor = CyanAccent),
        shape = RoundedCornerShape(28.dp),
        color = CardBg,
        border = BorderStroke(1.2.dp, GlassBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0x2822D3EE), Color.Transparent),
                        center = Offset(300f, 100f),
                        radius = 600f
                    )
                )
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Header: Level & Ring
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CREATOR LEVEL",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black,
                            color = CyanAccent,
                            letterSpacing = 1.2.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "${levelInfo.badgeEmoji} ${levelInfo.levelName}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                        }
                        Text(
                            text = "${levelInfo.currentXp} XP / ${levelInfo.nextLevelXp} XP",
                            fontSize = 11.5.sp,
                            color = TextGray
                        )
                    }

                    // XP Progress Ring Canvas
                    Box(
                        modifier = Modifier.size(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 7.dp.toPx()
                            // Track background
                            drawCircle(
                                color = Color(0x33FFFFFF),
                                radius = (size.minDimension - strokeWidth) / 2,
                                style = Stroke(strokeWidth)
                            )
                            // Progress Arc
                            val sweepAngle = levelInfo.progressPercent * 360f
                            drawArc(
                                color = CyanAccent,
                                startAngle = -90f,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                style = Stroke(strokeWidth)
                            )
                        }
                        Text(
                            text = "${(levelInfo.progressPercent * 100).toInt()}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }

                Divider(color = Color(0x1AFFFFFF), thickness = 1.dp)

                // Stats Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatPill("Current Streak", "${stats.currentStreakDays} Days 🔥", CyanAccent)
                    StatPill("Total Analysed", "${stats.totalReelsAnalysed} Reels 🎬", TextWhite)
                    StatPill("Avg AI Score", "${stats.averageAiScore}% ⚡", GreenSuccess)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatPill("Upload Streak", "${stats.currentUploadStreak} Days 🚀", CyanAccent)
                    StatPill("Monthly Growth", "+${stats.monthlyGrowthPercent}% 📈", GreenSuccess)
                    StatPill("AI Memory", "30 Reels 🧠", TextWhite)
                }
            }
        }
    }
}

@Composable
private fun StatPill(label: String, value: String, valueColor: Color) {
    Column(
        modifier = Modifier.widthIn(min = 90.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = TextGray
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

@Composable
private fun Reels30HistorySection(
    reels: List<AnalysedReel>,
    onReelClick: (AnalysedReel) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Default.History, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(18.dp))
                Text(
                    text = "30 Reel History",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }
            Text(
                text = "${reels.size} Stored",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = CyanAccent
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(reels) { reel ->
                Surface(
                    onClick = { onReelClick(reel) },
                    modifier = Modifier.width(170.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = CardBg,
                    border = BorderStroke(1.dp, Color(0x3322D3EE))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Card Header Badge & Platform
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = reel.category,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (reel.finalAiScore >= 88) Color(0x3310B981)
                                        else Color(0x3322D3EE)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${reel.finalAiScore}/100",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (reel.finalAiScore >= 88) GreenSuccess else CyanAccent
                                )
                            }
                        }

                        // Thumbnail Box Mock
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(85.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF1E293B), Color(0xFF0F172A))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = "Play",
                                tint = CyanAccent,
                                modifier = Modifier.size(28.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(6.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xCC000000))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = reel.date,
                                    fontSize = 8.5.sp,
                                    color = TextWhite,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Text(
                            text = reel.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Mini Score breakdown
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Hook ${reel.hookScore}", fontSize = 9.5.sp, color = TextGray)
                            Text("Ret ${reel.retentionScore}", fontSize = 9.5.sp, color = TextGray)
                            Text("CTA ${reel.ctaScore}", fontSize = 9.5.sp, color = TextGray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ImprovementGraphSection(
    selectedFilter: String,
    onFilterSelect: (String) -> Unit,
    selectedMetric: String,
    onMetricSelect: (String) -> Unit,
    reels: List<AnalysedReel>
) {
    val filters = listOf("7 Days", "30 Days", "90 Days", "All Time")
    val metrics = listOf("Hook", "Retention", "Lighting", "Voice", "Thumbnail", "CTA", "Confidence", "Energy", "Product Visibility")

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = CardBg,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.ShowChart, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
                    Text(
                        text = "Improvement Graph",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }

                // Apple Style Time Filter Switcher
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    filters.forEach { filter ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (selectedFilter == filter) CyanAccent else Color(0x1AFFFFFF))
                                .clickable { onFilterSelect(filter) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = filter,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedFilter == filter) AmoledBlack else TextWhite
                            )
                        }
                    }
                }
            }

            // Metric Switcher Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(metrics) { metric ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                BorderStroke(
                                    1.dp,
                                    if (selectedMetric == metric) CyanAccent else Color(0x22FFFFFF)
                                ),
                                RoundedCornerShape(14.dp)
                            )
                            .background(if (selectedMetric == metric) Color(0x3322D3EE) else Color.Transparent)
                            .clickable { onMetricSelect(metric) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = metric,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selectedMetric == metric) CyanAccent else TextGray
                        )
                    }
                }
            }

            // Apple Health Style Animated Chart Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0D121B))
                    .padding(12.dp)
            ) {
                val dataPoints = remember(selectedMetric, reels) {
                    reels.take(8).map { reel ->
                        when (selectedMetric) {
                            "Hook" -> reel.hookScore
                            "Retention" -> reel.retentionScore
                            "Lighting" -> reel.lightingScore
                            "Voice" -> reel.voiceScore
                            "Thumbnail" -> reel.thumbnailScore
                            "CTA" -> reel.ctaScore
                            "Confidence" -> reel.uploadConfidence
                            "Energy" -> reel.energyScore
                            else -> reel.productVisibilityScore
                        }
                    }.reversed()
                }

                Canvas(modifier = Modifier.fillMaxSize()) {
                    if (dataPoints.size >= 2) {
                        val maxVal = 100f
                        val minVal = 50f
                        val widthPx = size.width
                        val heightPx = size.height
                        val stepX = widthPx / (dataPoints.size - 1)

                        val path = Path()
                        dataPoints.forEachIndexed { idx, valScore ->
                            val normY = 1f - ((valScore - minVal) / (maxVal - minVal)).coerceIn(0f, 1f)
                            val x = idx * stepX
                            val y = normY * heightPx
                            if (idx == 0) path.moveTo(x, y) else path.lineTo(x, y)
                        }

                        // Draw Gradient Stroke
                        drawPath(
                            path = path,
                            brush = Brush.horizontalGradient(listOf(CyanAccent, GreenSuccess)),
                            style = Stroke(width = 3.dp.toPx())
                        )

                        // Draw Data Points
                        dataPoints.forEachIndexed { idx, valScore ->
                            val normY = 1f - ((valScore - minVal) / (maxVal - minVal)).coerceIn(0f, 1f)
                            val x = idx * stepX
                            val y = normY * heightPx
                            drawCircle(
                                color = CyanAccent,
                                radius = 4.dp.toPx(),
                                center = Offset(x, y)
                            )
                        }
                    }
                }

                Text(
                    text = "Trending Up: +${if (selectedMetric == "Hook") 14 else 12}% ($selectedMetric)",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenSuccess,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
private fun AiMemoryMistakesSection(
    mistakes: List<com.example.creatoracademy.RecurringMistake>
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(Icons.Default.Psychology, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
            Text(
                text = "AI Memory • Top 3 Recurring Mistakes",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            mistakes.forEach { item ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = CardBg,
                    border = BorderStroke(1.dp, Color(0x28FF5252))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0x22FF5252)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${item.occurrences}x",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFF5252)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.advice,
                                fontSize = 11.sp,
                                color = TextGray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BiggestImprovementsSection(
    improvements: List<com.example.creatoracademy.BiggestImprovement>
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = GreenSuccess, modifier = Modifier.size(20.dp))
            Text(
                text = "Biggest Improvements",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(improvements) { item ->
                Surface(
                    modifier = Modifier.width(200.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = CardBg,
                    border = BorderStroke(1.dp, Color(0x3310B981))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.title,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x3310B981))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "+${item.gainPercentage}%",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = GreenSuccess
                                )
                            }
                        }
                        Text(
                            text = item.note,
                            fontSize = 10.5.sp,
                            color = TextGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthlyReportSection(
    report: com.example.creatoracademy.MonthlyReportData
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = CardBg,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MONTHLY REPORT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = CyanAccent,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${report.monthName} • ${report.reelsAnalysedCount} Reels Analysed",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x2222D3EE))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Auto Generated",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanAccent
                    )
                }
            }

            Divider(color = Color(0x1AFFFFFF))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ReportBarRow("Hook Performance", report.hookGain)
                ReportBarRow("Retention Rate", report.retentionGain)
                ReportBarRow("CTA Conversion", report.ctaGain)
                ReportBarRow("Voice Clarity", report.voiceGain)
                ReportBarRow("Thumbnail CTR", report.thumbnailGain)
            }
        }
    }
}

@Composable
private fun ReportBarRow(label: String, gainPercent: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 11.5.sp, color = TextGray)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0x1AFFFFFF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(gainPercent / 25f)
                        .clip(RoundedCornerShape(3.dp))
                        .background(CyanAccent)
                )
            }
            Text(
                text = "+$gainPercent%",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GreenSuccess
            )
        }
    }
}

@Composable
private fun CreatorBadgesSection(
    badges: List<CreatorBadgeItem>
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
            Text(
                text = "Creator Badges",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(badges) { badge ->
                Surface(
                    modifier = Modifier.width(140.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = CardBg,
                    border = BorderStroke(
                        1.dp,
                        if (badge.isUnlocked) CyanAccent else Color(0x22FFFFFF)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = badge.emoji,
                            fontSize = 32.sp
                        )
                        Text(
                            text = badge.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = badge.progressText,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (badge.isUnlocked) GreenSuccess else TextGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AiCoachMemorySection(
    conversations: List<com.example.creatoracademy.AiCoachMemoryConversation>
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ViriMascotWidget(size = 22.dp)
            Text(
                text = "AI Coach Memory (Viri)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            conversations.forEach { conv ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = CardBg,
                    border = BorderStroke(1.dp, GlassBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ViriMascotWidget(size = 28.dp)
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = conv.contextTag,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanAccent
                                )
                                Text(
                                    text = conv.date,
                                    fontSize = 9.5.sp,
                                    color = TextGray
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "\"${conv.viriQuote}\"",
                                fontSize = 12.sp,
                                color = TextWhite,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SmartChallengesSection(
    challenges: List<SmartChallenge>,
    onCompleteChallenge: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(Icons.Default.TaskAlt, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
            Text(
                text = "Smart Challenges",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            challenges.forEach { challenge ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = CardBg,
                    border = BorderStroke(
                        1.dp,
                        if (challenge.isCompleted) Color(0x4410B981) else GlassBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = challenge.type,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CyanAccent
                                )
                                Text(
                                    text = "• +${challenge.rewardXp} XP",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenSuccess
                                )
                            }
                            Text(
                                text = challenge.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Progress: ${challenge.progressText}",
                                fontSize = 10.5.sp,
                                color = TextGray
                            )
                        }

                        if (!challenge.isCompleted) {
                            Button(
                                onClick = { onCompleteChallenge(challenge.id) },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyanAccent,
                                    contentColor = AmoledBlack
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Complete", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GreenSuccess, modifier = Modifier.size(18.dp))
                                Text("Done", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GreenSuccess)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun YearlyTimelineSection(
    timeline: List<com.example.creatoracademy.YearlyMilestone>
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(Icons.Default.Timeline, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
            Text(
                text = "Yearly Timeline",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            timeline.forEach { milestone ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x2222D3EE))
                            .border(BorderStroke(1.dp, CyanAccent), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(milestone.emoji, fontSize = 16.sp)
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        color = CardBg,
                        border = BorderStroke(1.dp, GlassBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = milestone.title,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = milestone.date,
                                    fontSize = 9.5.sp,
                                    color = TextGray
                                )
                            }
                            Text(
                                text = milestone.description,
                                fontSize = 10.5.sp,
                                color = TextGray
                            )
                        }
                    }
                }
            }
        }
    }
}

// Dialog: Reel Report Detail
@Composable
private fun ReelReportDetailDialog(
    reel: AnalysedReel,
    onDismiss: () -> Unit
) {
    var showPersonaDialog by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            color = CardBg,
            border = BorderStroke(1.2.dp, CyanAccent)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(reel.platform, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                        Text(reel.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text("Analysed on ${reel.date}", fontSize = 11.sp, color = TextGray)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite)
                    }
                }

                Divider(color = Color(0x1AFFFFFF))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailMetricBox("AI Score", "${reel.finalAiScore}/100", CyanAccent)
                    DetailMetricBox("Upload Conf.", "${reel.uploadConfidence}%", GreenSuccess)
                    DetailMetricBox("Hook", "${reel.hookScore}/100", CyanAccent)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailMetricBox("Retention", "${reel.retentionScore}/100", TextWhite)
                    DetailMetricBox("Thumbnail", "${reel.thumbnailScore}/100", TextWhite)
                    DetailMetricBox("CTA Score", "${reel.ctaScore}/100", TextWhite)
                }

                Text("AI Summary", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                Text(reel.aiSummary, fontSize = 11.5.sp, color = TextWhite)

                Text("Key Weaknesses", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF5252))
                reel.weaknesses.forEach { w ->
                    Text("• $w", fontSize = 11.sp, color = TextGray)
                }

                Text("Key Strengths", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenSuccess)
                reel.strengths.forEach { s ->
                    Text("• $s", fontSize = 11.sp, color = TextGray)
                }

                // AI Audience Persona Engine Trigger (DS-28)
                Button(
                    onClick = { showPersonaDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B), contentColor = CyanAccent),
                    border = BorderStroke(1.dp, CyanAccent)
                ) {
                    Text("👥 View AI Audience Persona", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = AmoledBlack)
                ) {
                    Text("Close Report", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showPersonaDialog) {
        AiAudiencePersonaDialog(
            reel = reel,
            onDismiss = { showPersonaDialog = false }
        )
    }
}

@Composable
private fun DetailMetricBox(label: String, valStr: String, color: Color) {
    Column(
        modifier = Modifier
            .width(90.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x1AFFFFFF))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, fontSize = 9.5.sp, color = TextGray)
        Text(valStr, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

// Dialog: Export Portfolio Report
@Composable
private fun ExportPortfolioDialog(
    levelInfo: CreatorGrowthLevel,
    stats: CreatorGrowthStats,
    monthlyReport: com.example.creatoracademy.MonthlyReportData,
    topMistakes: List<com.example.creatoracademy.RecurringMistake>,
    improvements: List<com.example.creatoracademy.BiggestImprovement>,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val portfolioSummaryText = remember {
        """
        🚀 VIRALTOOLAI CREATOR PORTFOLIO REPORT
        Level: ${levelInfo.levelName} (${levelInfo.currentXp} XP)
        Total Reels Analysed: ${stats.totalReelsAnalysed}
        Average AI Score: ${stats.averageAiScore}%
        Current Streak: ${stats.currentStreakDays} Days
        Monthly Growth: +${monthlyReport.hookGain}% Hook / +${monthlyReport.thumbnailGain}% CTR
        
        Top Strengths:
        ${improvements.joinToString("\n") { "${it.title}: ${it.note}" }}
        
        Generated by ViralToolAI Creator OS
        """.trimIndent()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            color = CardBg,
            border = BorderStroke(1.2.dp, CyanAccent)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = CyanAccent)
                        Text("Creator Portfolio Deck", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF0F172A),
                    border = BorderStroke(1.dp, GlassBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Portfolio Preview", fontSize = 11.sp, color = CyanAccent, fontWeight = FontWeight.Bold)
                        Text(portfolioSummaryText, fontSize = 11.sp, color = TextWhite)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(portfolioSummaryText))
                            Toast.makeText(context, "Copied Portfolio Report!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x3322D3EE), contentColor = CyanAccent),
                        border = BorderStroke(1.dp, CyanAccent)
                    ) {
                        Text("Copy Deck", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            Toast.makeText(context, "PDF Report Exported Successfully!", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = AmoledBlack)
                    ) {
                        Text("Export PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
