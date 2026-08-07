package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import com.example.ui.components.AiAudiencePersonaDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.components.ViriAction
import com.example.ui.components.ViriMascotWidget
import com.example.ui.components.ViriPrefs
import com.example.creatoracademy.AnalysedReel
import com.example.creatoracademy.CreatorGrowthEngine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.delay

// Colors specified in DS-24: Apple Human Interface, Cyan #22D3EE primary, Soft Glass, NO PURPLE
private val CoachDark = Color(0xFF090B10)
private val CoachSurface = Color(0xFF11141C)
private val CoachCard = Color(0xFF181C27)
private val CyanAccent = Color(0xFF22D3EE)
private val CyanGlow = Color(0x3322D3EE)
private val GlassBorder = Color(0x3322D3EE)
private val EmeraldGreen = Color(0xFF10B981)
private val AmberYellow = Color(0xFFF59E0B)
private val RoseRed = Color(0xFFF43F5E)
private val TextWhite = Color(0xFFF8FAFC)
private val TextSecondary = Color(0xFF94A3B8)

data class PriorityItem(
    val id: Int,
    val iconEmoji: String,
    val title: String,
    val impactTag: String
)

data class RemoveItem(
    val timeRange: String,
    val reason: String,
    val detail: String
)

data class AddItem(
    val label: String,
    val timestamp: String,
    val expectedGain: String,
    val gainColor: Color
)

data class ReelMemoryPoint(
    val reelName: String,
    val hookScore: Int
)

@Composable
fun AiViralCoachOverlay(
    config: ProjectSetupConfig?,
    onDismiss: () -> Unit,
    onContinueToEditor: (ProjectSetupConfig) -> Unit,
    onOpenSimulator: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val videoUri = config?.selectedMedia?.firstOrNull()?.uri

    // SECTION 1: 30-SECOND REVIEW TYPEWRITER STATE
    val fullSpeechText = "Hook accha hai, lekin product thoda late dikh raha hai. Agar first 2 seconds me product dikha doge to retention 18% improve ho sakta hai!"
    var displayedSpeechText by remember { mutableStateOf("") }
    var isSpeaking by remember { mutableStateOf(true) }
    var currentAnalysedReel by remember { mutableStateOf<AnalysedReel?>(null) }
    var showAudiencePersonaDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        displayedSpeechText = ""
        fullSpeechText.forEach { char ->
            displayedSpeechText += char
            delay(35)
        }
        isSpeaking = false
    }

    LaunchedEffect(config) {
        val titleName = config?.selectedMedia?.firstOrNull()?.uri?.lastPathSegment ?: "Viral Reel"
        val reelObj = AnalysedReel(
            id = "reel_${System.currentTimeMillis()}",
            title = "Reel • $titleName",
            date = SimpleDateFormat("MMM dd, yyyy", Locale.US).format(Date()),
            finalAiScore = 91,
            uploadConfidence = 88,
            hookScore = 92,
            retentionScore = 85,
            lightingScore = 90,
            voiceScore = 84,
            thumbnailScore = 89,
            ctaScore = 82,
            energyScore = 86,
            productVisibilityScore = 88,
            aiSummary = "Strong scroll-stopping hook with high potential viral retention.",
            weaknesses = listOf("Show product 1s earlier", "Insert bold CTA at 0:12"),
            strengths = listOf("Top 5% scroll-stopping hook", "Bright 3-point lighting setup")
        )
        currentAnalysedReel = reelObj
        if (config != null) {
            CreatorGrowthEngine.addAnalysedReel(
                context = context,
                newReel = reelObj
            )
        }
    }

    // SECTION 2: TOP 3 PRIORITIES (ONLY 3)
    val top3Priorities = remember {
        listOf(
            PriorityItem(1, "🔥", "Show product in first 1 sec", "High Impact (+18% Reach)"),
            PriorityItem(2, "🎤", "Speak louder & clear vocal pitch", "Medium Impact (+8% Retention)"),
            PriorityItem(3, "✨", "Insert bold CTA before ending", "High Impact (+12% Leads)")
        )
    }

    // SECTION 3: WHAT TO REMOVE
    val removeItems = remember {
        listOf(
            RemoveItem("0.0–0.8 sec", "Remove silent intro delay", "Silent pause causes 14% immediate user skip."),
            RemoveItem("6.4–7.0 sec", "Cut long awkward pause", "Pacing drops during transition."),
            RemoveItem("10.1–11.0 sec", "Product hidden behind text", "Caption box obscures price tag.")
        )
    }

    // SECTION 4: WHAT TO ADD
    val addItems = remember {
        listOf(
            AddItem("Zoom effect at 5.2s", "@ 5.2s", "+6% retention", EmeraldGreen),
            AddItem("Emoji overlay at 2.1s", "@ 2.1s", "+4% engagement", CyanAccent),
            AddItem("Price tag badge", "@ 1.8s", "+8% CTR", EmeraldGreen),
            AddItem("Logo badge top-right", "@ 0.5s", "+3% recall", CyanAccent),
            AddItem("CTA button at end", "@ 12.5s", "+7% conversion", EmeraldGreen),
            AddItem("Music beat boost", "@ 8.0s", "+5% energy", CyanAccent)
        )
    }

    // SECTION 5: ONE TAP PRACTICE
    var challengeCompleted by remember { mutableStateOf(false) }
    var currentExp by remember { mutableIntStateOf(ViriPrefs.getExp(context)) }

    // SECTION 6: CREATOR LEVEL
    val creatorLevelName = remember(currentExp) {
        when {
            currentExp >= 1000 -> "Elite Creator"
            currentExp >= 600 -> "Professional Creator"
            currentExp >= 350 -> "Advanced Creator"
            currentExp >= 200 -> "Growing Creator"
            else -> "Beginner Creator"
        }
    }

    // SECTION 7: AI MEMORY (Previous Reels Progress)
    val reelHistory = remember {
        listOf(
            ReelMemoryPoint("Reel #1", 62),
            ReelMemoryPoint("Reel #2", 68),
            ReelMemoryPoint("Reel #3", 74),
            ReelMemoryPoint("Reel #4", 79),
            ReelMemoryPoint("Current", 88)
        )
    }

    // SECTION 9: VIRI COACH MOOD
    val currentReelHookScore = 88
    val (viriMoodEmoji, viriMoodTitle, viriQuote, viriAction) = remember(currentReelHookScore) {
        when {
            currentReelHookScore >= 80 -> Quadruple("🥳", "Celebration Mode", "Tu improve kar raha hai! Zabardast progress! 🔥", ViriAction.CELEBRATING)
            currentReelHookScore >= 60 -> Quadruple("🙂", "Encouragement Mode", "Bas ek aur reel! Bohot accha ja raha hai.", ViriAction.HAPPY)
            else -> Quadruple("😔", "Motivation Mode", "Next wali aur better hogi! Main saath hu.", ViriAction.THINKING)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(CoachDark),
            color = CoachDark
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Header Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = CyanGlow,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Outlined.Psychology,
                                        contentDescription = null,
                                        tint = CyanAccent,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = "🧠 AI Viral Coach",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "Your personal content mentor",
                                    fontSize = 11.5.sp,
                                    color = CyanAccent,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(CoachSurface)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Scrollable Sections (10 Sections)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // ==================================================
                        // SECTION 1: 30-SECOND REVIEW (VIRI SPEAKING)
                        // ==================================================
                        AppleCoachCard(title = "SECTION 1 — 30-SECOND VIRI REVIEW") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Animated Viri Robot Mascot
                                Box(
                                    modifier = Modifier.size(85.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ViriMascotWidget(
                                        size = 85.dp,
                                        action = viriAction
                                    )
                                }

                                // Typewriter Hinglish Speech Bubble
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
                                    color = CoachCard,
                                    border = BorderStroke(1.dp, CyanGlow)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Viri Coach Says $viriMoodEmoji",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = CyanAccent
                                            )

                                            IconButton(
                                                onClick = {
                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    displayedSpeechText = ""
                                                    isSpeaking = true
                                                },
                                                modifier = Modifier.size(22.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Replay,
                                                    contentDescription = "Replay",
                                                    tint = TextSecondary,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }

                                        Text(
                                            text = "\"$displayedSpeechText\"",
                                            fontSize = 12.sp,
                                            color = TextWhite,
                                            fontWeight = FontWeight.Medium,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 2: TOP 3 PRIORITIES (STRICTLY 3)
                        // ==================================================
                        AppleCoachCard(title = "SECTION 2 — TOP 3 PRIORITIES") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Only the highest impact changes to double your reach:",
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )

                                top3Priorities.forEach { priority ->
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        color = CoachCard,
                                        border = BorderStroke(1.dp, GlassBorder)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Text(
                                                text = priority.iconEmoji,
                                                fontSize = 20.sp
                                            )
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = priority.title,
                                                    fontSize = 12.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextWhite
                                                )
                                                Text(
                                                    text = priority.impactTag,
                                                    fontSize = 10.sp,
                                                    color = CyanAccent,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = CyanAccent,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 3: WHAT TO REMOVE
                        // ==================================================
                        AppleCoachCard(title = "SECTION 3 — WHAT TO REMOVE") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                removeItems.forEach { item ->
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        color = CoachCard,
                                        border = BorderStroke(1.dp, RoseRed.copy(alpha = 0.3f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = RoseRed.copy(alpha = 0.2f)
                                            ) {
                                                Text(
                                                    text = item.timeRange,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = RoseRed,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                                )
                                            }

                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = item.reason,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextWhite
                                                )
                                                Text(
                                                    text = item.detail,
                                                    fontSize = 10.sp,
                                                    color = TextSecondary
                                                )
                                            }

                                            Icon(
                                                imageVector = Icons.Default.DeleteOutline,
                                                contentDescription = "Remove",
                                                tint = RoseRed,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 4: WHAT TO ADD
                        // ==================================================
                        AppleCoachCard(title = "SECTION 4 — WHAT TO ADD") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                addItems.forEach { item ->
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        color = CoachCard,
                                        border = BorderStroke(0.5.dp, GlassBorder)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text(
                                                    text = item.timestamp,
                                                    fontSize = 10.sp,
                                                    color = TextSecondary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = item.label,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = TextWhite
                                                )
                                            }

                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = item.gainColor.copy(alpha = 0.2f)
                                            ) {
                                                Text(
                                                    text = item.expectedGain,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = item.gainColor,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 5: ONE TAP PRACTICE
                        // ==================================================
                        AppleCoachCard(title = "SECTION 5 — ONE TAP PRACTICE CHALLENGE") {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    color = CoachCard,
                                    border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.5f))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "🎯 Today's Viral Challenge",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = CyanAccent
                                            )
                                            Text(
                                                text = "+150 XP Reward",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = AmberYellow
                                            )
                                        }

                                        Text(
                                            text = "\"Create a hook under 2 seconds and reveal product in first second.\"",
                                            fontSize = 12.5.sp,
                                            color = TextWhite,
                                            fontWeight = FontWeight.Medium
                                        )

                                        Button(
                                            onClick = {
                                                if (!challengeCompleted) {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    challengeCompleted = true
                                                    currentExp = ViriPrefs.addExp(context, 150).levelNum * 200 + currentExp
                                                    Toast.makeText(context, "🎉 Challenge Completed! +150 XP Earned!", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(40.dp),
                                            shape = RoundedCornerShape(20.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (challengeCompleted) EmeraldGreen else CyanAccent,
                                                contentColor = Color.Black
                                            )
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (challengeCompleted) Icons.Default.Check else Icons.Default.EmojiEvents,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Text(
                                                    text = if (challengeCompleted) "Challenge Completed (+150 XP)" else "Complete Practice Challenge",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 6: CREATOR LEVEL
                        // ==================================================
                        AppleCoachCard(title = "SECTION 6 — CREATOR LEVEL") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = creatorLevelName,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = CyanAccent
                                        )
                                        Text(
                                            text = "Level increases when reels improve",
                                            fontSize = 10.sp,
                                            color = TextSecondary
                                        )
                                    }

                                    Surface(
                                        shape = CircleShape,
                                        color = AmberYellow.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, AmberYellow.copy(alpha = 0.5f))
                                    ) {
                                        Text(
                                            text = "⚡ $currentExp XP",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AmberYellow,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                // Level Progress Bar
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    val progressVal = (currentExp % 200) / 200f
                                    LinearProgressIndicator(
                                        progress = { progressVal.coerceIn(0.1f, 1f) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                        color = CyanAccent,
                                        trackColor = CoachSurface
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Beginner", fontSize = 9.sp, color = TextSecondary)
                                        Text("Growing", fontSize = 9.sp, color = TextSecondary)
                                        Text("Advanced", fontSize = 9.sp, color = TextSecondary)
                                        Text("Pro", fontSize = 9.sp, color = TextSecondary)
                                        Text("Elite", fontSize = 9.sp, color = CyanAccent, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 7: AI MEMORY (PROGRESS GRAPH)
                        // ==================================================
                        AppleCoachCard(title = "SECTION 7 — AI REEL MEMORY") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Reel Score Progression:", fontSize = 11.5.sp, color = TextSecondary)
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = EmeraldGreen.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = "Improvement: +14 pts",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldGreen,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                // Graph Canvas
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(CoachCard)
                                ) {
                                    Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                                        val w = size.width
                                        val h = size.height

                                        val points = listOf(
                                            Offset(0f, h * 0.7f),
                                            Offset(w * 0.25f, h * 0.6f),
                                            Offset(w * 0.5f, h * 0.45f),
                                            Offset(w * 0.75f, h * 0.35f),
                                            Offset(w, h * 0.15f)
                                        )

                                        val path = Path().apply {
                                            moveTo(points[0].x, points[0].y)
                                            for (i in 1 until points.size) {
                                                lineTo(points[i].x, points[i].y)
                                            }
                                        }

                                        drawPath(
                                            path = path,
                                            color = CyanAccent,
                                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                                        )

                                        points.forEach { p ->
                                            drawCircle(color = CyanAccent, radius = 4.dp.toPx(), center = p)
                                            drawCircle(color = Color.Black, radius = 2.dp.toPx(), center = p)
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 8: NEXT REEL PLAN
                        // ==================================================
                        AppleCoachCard(title = "SECTION 8 — NEXT REEL PLAN") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("AI generated content strategy for tomorrow:", fontSize = 11.5.sp, color = TextSecondary)

                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = CoachCard,
                                    border = BorderStroke(1.dp, GlassBorder)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                            Text("📅 Tomorrow's Topic:", fontSize = 11.sp, color = TextSecondary)
                                            Text("Skincare Under ₹299", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                                        }
                                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                            Text("⏰ Best Upload Time:", fontSize = 11.sp, color = TextSecondary)
                                            Text("7:30 PM Today", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        }
                                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                            Text("🎯 Target Audience:", fontSize = 11.sp, color = TextSecondary)
                                            Text("Women 18–30 (Gen-Z)", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("💡 Hook Suggestion:", fontSize = 10.5.sp, color = TextSecondary)
                                        Text(
                                            text = "\"Ye product sach me worth hai ya sirf hype? Let's test live!\"",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 9: VIRI COACH MODE (SUPPORTIVE QUOTE)
                        // ==================================================
                        AppleCoachCard(title = "SECTION 9 — VIRI COACH MODE") {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = CoachCard,
                                border = BorderStroke(1.dp, CyanGlow)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(text = viriMoodEmoji, fontSize = 26.sp)
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = viriMoodTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                                        Text(text = viriQuote, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextWhite)
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // DS-28: AI AUDIENCE PERSONA ENGINE CARD
                        // ==================================================
                        AppleCoachCard(title = "DS-28 — AI AUDIENCE PERSONA ENGINE") {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showAudiencePersonaDialog = true },
                                shape = RoundedCornerShape(16.dp),
                                color = CoachCard,
                                border = BorderStroke(1.dp, CyanGlow)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(text = "👥", fontSize = 28.sp)
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "AI Audience Persona",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                        Text(
                                            text = "Predict WHO is most likely to watch, engage, and buy from this reel.",
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(CyanAccent)
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "View",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CoachDark
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Bottom Action Bar
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = CoachSurface,
                        border = BorderStroke(1.dp, GlassBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (onOpenSimulator != null) {
                                Button(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onOpenSimulator()
                                    },
                                    modifier = Modifier
                                        .weight(1.1f)
                                        .height(48.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CyanGlow,
                                        contentColor = CyanAccent
                                    ),
                                    border = BorderStroke(1.dp, CyanAccent)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text("🚀 Simulator", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val coachReport = """
                                        🧠 AI VIRAL COACH ACTION PLAN 🧠
                                        Creator Level: $creatorLevelName
                                        Top 3 Priorities:
                                        1. Show product in first 1 sec
                                        2. Speak louder & clear pitch
                                        3. Insert bold CTA before ending
                                        Next Reel Topic: Skincare Under ₹299 @ 7:30 PM
                                    """.trimIndent()
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Coach Report", coachReport))
                                    Toast.makeText(context, "Coach Plan Copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(0.9f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CoachCard,
                                    contentColor = CyanAccent
                                ),
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                                    Text("Copy", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    config?.let { onContinueToEditor(it) }
                                },
                                modifier = Modifier
                                    .weight(1.1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyanAccent,
                                    contentColor = Color.Black
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("Editor", fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold)
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAudiencePersonaDialog) {
        val currentReel = currentAnalysedReel ?: AnalysedReel(
            id = "reel_default",
            title = "Current Reel Analysis",
            date = "Today",
            finalAiScore = 91,
            uploadConfidence = 88,
            hookScore = 92,
            retentionScore = 85,
            lightingScore = 90,
            voiceScore = 84,
            thumbnailScore = 89,
            ctaScore = 82,
            energyScore = 86,
            productVisibilityScore = 88,
            aiSummary = "High potential viral product reel.",
            weaknesses = listOf("Show product 1s earlier"),
            strengths = listOf("Top scroll-stopping hook")
        )
        AiAudiencePersonaDialog(
            reel = currentReel,
            onDismiss = { showAudiencePersonaDialog = false }
        )
    }
}

@Composable
private fun AppleCoachCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = CoachSurface,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CyanAccent,
                letterSpacing = 0.5.sp
            )
            content()
        }
    }
}

private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)
