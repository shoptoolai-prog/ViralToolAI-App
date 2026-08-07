package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import coil.request.videoFrameMillis
import com.example.ui.components.ViriAction
import com.example.ui.components.ViriMascotWidget
import kotlinx.coroutines.delay

// Colors specified in DS-25: Apple Vision Pro Glass UI, Cyan #22D3EE primary, Soft Glass, NO PURPLE
private val SimDark = Color(0xFF090B10)
private val SimSurface = Color(0xFF11141C)
private val SimCard = Color(0xFF181C27)
private val CyanAccent = Color(0xFF22D3EE)
private val CyanGlow = Color(0x3322D3EE)
private val GlassBorder = Color(0x3322D3EE)
private val EmeraldGreen = Color(0xFF10B981)
private val AmberYellow = Color(0xFFF59E0B)
private val RoseRed = Color(0xFFF43F5E)
private val TextWhite = Color(0xFFF8FAFC)
private val TextSecondary = Color(0xFF94A3B8)

data class TimelineMetric(
    val minuteLabel: String,
    val views: Int,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val saves: Int,
    val visits: Int,
    val followRate: String
)

data class ViewerTypeData(
    val id: Int,
    val emoji: String,
    val name: String,
    val description: String,
    val predictedAction: String,
    val actionColor: Color,
    val watchTime: String
)

data class SimPredictedComment(
    val text: String,
    val category: String, // Positive, Neutral, Negative
    val userHandle: String
)

@Composable
fun AiUploadSimulatorOverlay(
    config: ProjectSetupConfig?,
    onDismiss: () -> Unit,
    onContinueToEditor: (ProjectSetupConfig) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val videoUri = config?.selectedMedia?.firstOrNull()?.uri

    // Prediction Confidence Check
    val confidenceScore = 91
    val isConfidenceLimited = confidenceScore < 75

    // SECTION 1: FIRST 30 MINUTES GRADUAL ANIMATION
    var selectedTimelineStep by remember { mutableIntStateOf(3) } // 0: 0m, 1: 5m, 2: 10m, 3: 30m
    var animProgress by remember { mutableFloatStateOf(0f) }

    val timelineMetrics = remember {
        listOf(
            TimelineMetric("0 min", 0, 0, 0, 0, 0, 0, "0.0%"),
            TimelineMetric("5 min", 420, 38, 6, 4, 12, 18, "1.8%"),
            TimelineMetric("10 min", 1850, 164, 24, 18, 56, 84, "2.4%"),
            TimelineMetric("30 min", 8400, 720, 94, 82, 310, 420, "3.8%")
        )
    }

    val currentMetric = timelineMetrics[selectedTimelineStep]

    // Gradual Value Animation counters
    val animatedViews by animateIntAsState(
        targetValue = (currentMetric.views * animProgress).toInt(),
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label = "views"
    )
    val animatedLikes by animateIntAsState(
        targetValue = (currentMetric.likes * animProgress).toInt(),
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label = "likes"
    )
    val animatedComments by animateIntAsState(
        targetValue = (currentMetric.comments * animProgress).toInt(),
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label = "comments"
    )
    val animatedShares by animateIntAsState(
        targetValue = (currentMetric.shares * animProgress).toInt(),
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label = "shares"
    )
    val animatedSaves by animateIntAsState(
        targetValue = (currentMetric.saves * animProgress).toInt(),
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label = "saves"
    )

    LaunchedEffect(selectedTimelineStep) {
        animProgress = 0f
        delay(50)
        animProgress = 1f
    }

    // SECTION 2 & 3: SCROLL SIMULATION & VIEWER TYPES
    val viewerTypes = remember {
        listOf(
            ViewerTypeData(1, "⚡", "Fast Scroller", "Swipes rapidly through feed", "Watch & Like", EmeraldGreen, "12.4s / 15s"),
            ViewerTypeData(2, "👀", "Normal Viewer", "Watches engaging hooks", "Watch & Save", CyanAccent, "15.0s (Full)"),
            ViewerTypeData(3, "💄", "Beauty Shopper", "Looks for product results", "Replay & Share", EmeraldGreen, "Replayed 2x"),
            ViewerTypeData(4, "🏷️", "Deal Hunter", "Checks price & discount CTA", "Comment 'LINK'", CyanAccent, "14.2s"),
            ViewerTypeData(5, "👑", "Returning Follower", "Interacts with creator content", "Like & Comment", EmeraldGreen, "15.0s (Full)"),
            ViewerTypeData(6, "🌐", "New Audience", "Discovers from explore feed", "Follow Creator", EmeraldGreen, "15.0s + Follow")
        )
    }

    var activeViewerIndex by remember { mutableIntStateOf(0) }
    val activeViewer = viewerTypes[activeViewerIndex]

    // Automated cycling of active viewer in simulation phone
    LaunchedEffect(Unit) {
        while (true) {
            delay(3500)
            activeViewerIndex = (activeViewerIndex + 1) % viewerTypes.size
        }
    }

    // SECTION 6: COMMENT PREDICTION
    val commentPredictions = remember {
        listOf(
            SimPredictedComment("Price kya hai bro?", "Neutral", "@rahul_vlogs"),
            SimPredictedComment("Commented 'LINK' please send DM! 😍", "Positive", "@priya_style"),
            SimPredictedComment("COD available hai kya India me?", "Neutral", "@ankit_deals"),
            SimPredictedComment("Need this device right now! Quality looks solid 🔥", "Positive", "@sneha_tech"),
            SimPredictedComment("First 2 sec intro thoda fast laga.", "Negative", "@content_critic")
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(SimDark),
            color = SimDark
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
                                        imageVector = Icons.Outlined.RocketLaunch,
                                        contentDescription = null,
                                        tint = CyanAccent,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = "🚀 Upload Simulator",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "See what could happen before you post.",
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
                                .background(SimSurface)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Confidence Notice Banner (if applicable)
                    if (isConfidenceLimited) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = AmberYellow.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, AmberYellow.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "Prediction confidence is limited based on available sample data.",
                                fontSize = 11.sp,
                                color = AmberYellow,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(10.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Scrollable Content (9 Sections)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // ==================================================
                        // SECTION 1: FIRST 30 MINUTES
                        // ==================================================
                        AppleSimCard(title = "SECTION 1 — FIRST 30 MINUTES PREDICTION") {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                // Animated Timeline Selector (0min -> 5min -> 10min -> 30min)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(SimCard)
                                        .padding(4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    timelineMetrics.forEachIndexed { index, m ->
                                        val isSelected = selectedTimelineStep == index
                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable {
                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    selectedTimelineStep = index
                                                },
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isSelected) CyanAccent else Color.Transparent
                                        ) {
                                            Text(
                                                text = m.minuteLabel,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.Black else TextWhite,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            )
                                        }
                                    }
                                }

                                // Animated Metrics Grid
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    SimMetricBadge("Views", "👁️ $animatedViews", Modifier.weight(1f))
                                    SimMetricBadge("Likes", "❤️ $animatedLikes", Modifier.weight(1f))
                                    SimMetricBadge("Comments", "💬 $animatedComments", Modifier.weight(1f))
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    SimMetricBadge("Shares", "🔁 $animatedShares", Modifier.weight(1f))
                                    SimMetricBadge("Saves", "🔖 $animatedSaves", Modifier.weight(1f))
                                    SimMetricBadge("Follow Rate", "⚡ ${currentMetric.followRate}", Modifier.weight(1f))
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 2: SCROLL SIMULATION (ANIMATED PHONE)
                        // ==================================================
                        AppleSimCard(title = "SECTION 2 — LIVE FEED SCROLL SIMULATION") {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "AI simulates random viewer feed insertion in real time:",
                                    fontSize = 11.5.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                // Animated Simulated Phone Mockup
                                Surface(
                                    modifier = Modifier
                                        .width(210.dp)
                                        .height(310.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    color = Color.Black,
                                    border = BorderStroke(2.dp, CyanAccent)
                                ) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        // Reel Media
                                        AsyncImage(
                                            model = ImageRequest.Builder(context)
                                                .data(videoUri)
                                                .videoFrameMillis(2000L)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "Simulated Reel",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        // Dark Gradient Shadow
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Brush.verticalGradient(
                                                        colors = listOf(
                                                            Color.Black.copy(alpha = 0.4f),
                                                            Color.Transparent,
                                                            Color.Black.copy(alpha = 0.8f)
                                                        )
                                                    )
                                                )
                                        )

                                        // Viewer Persona Overlay Tag
                                        Surface(
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .padding(10.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color.Black.copy(alpha = 0.75f),
                                            border = BorderStroke(0.5.dp, CyanGlow)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(activeViewer.emoji, fontSize = 11.sp)
                                                Text(activeViewer.name, fontSize = 9.5.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                                            }
                                        }

                                        // Action Prediction Bubble
                                        Surface(
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .padding(16.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            color = activeViewer.actionColor.copy(alpha = 0.9f)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text("PREDICTED ACTION", fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                                                Text(activeViewer.predictedAction, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                                Text("Watch Time: ${activeViewer.watchTime}", fontSize = 8.5.sp, color = Color.Black.copy(alpha = 0.8f))
                                            }
                                        }

                                        // Simulated Reel Right Icons Bar
                                        Column(
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .padding(bottom = 12.dp, end = 8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(Icons.Filled.Favorite, contentDescription = null, tint = RoseRed, modifier = Modifier.size(16.dp))
                                            Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                                            Icon(Icons.Outlined.Share, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 3: VIEWER TYPES
                        // ==================================================
                        AppleSimCard(title = "SECTION 3 — AUDIENCE PERSONA REACTIONS") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    itemsIndexed(viewerTypes) { idx, persona ->
                                        val isSelected = activeViewerIndex == idx
                                        Surface(
                                            modifier = Modifier
                                                .width(130.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable {
                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    activeViewerIndex = idx
                                                },
                                            shape = RoundedCornerShape(12.dp),
                                            color = SimCard,
                                            border = BorderStroke(
                                                1.dp,
                                                if (isSelected) CyanAccent else GlassBorder
                                            )
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(8.dp),
                                                verticalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    Text(persona.emoji, fontSize = 14.sp)
                                                    Text(persona.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite, maxLines = 1)
                                                }
                                                Text(persona.description, fontSize = 8.5.sp, color = TextSecondary, maxLines = 1)
                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = persona.actionColor.copy(alpha = 0.2f),
                                                    modifier = Modifier.padding(top = 2.dp)
                                                ) {
                                                    Text(
                                                        text = persona.predictedAction,
                                                        fontSize = 8.5.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = persona.actionColor,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 4: SWIPE MOMENT
                        // ==================================================
                        AppleSimCard(title = "SECTION 4 — SWIPE MOMENT DIAGNOSIS") {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = SimCard,
                                border = BorderStroke(1.dp, AmberYellow.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = AmberYellow.copy(alpha = 0.2f),
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("4.8s", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = AmberYellow)
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Predicted Swipe Departure", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AmberYellow)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Viewer leaves at 4.8 sec because product demo explanation was delayed. Insert product macro shot at 1.0s to eliminate swipe rate.",
                                            fontSize = 11.sp,
                                            color = TextWhite,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 5: REPLAY MOMENT
                        // ==================================================
                        AppleSimCard(title = "SECTION 5 — REPLAY MOMENT PREDICTION") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Timestamps where viewers are predicted to hit replay:", fontSize = 11.5.sp, color = TextSecondary)

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        color = SimCard,
                                        border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.4f))
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Icon(Icons.Default.Repeat, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(14.dp))
                                                Text("5.2s – 6.8s", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                            }
                                            Text("Unboxing Reveal Moment", fontSize = 9.5.sp, color = TextWhite)
                                        }
                                    }

                                    Surface(
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        color = SimCard,
                                        border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.4f))
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Icon(Icons.Default.Repeat, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(14.dp))
                                                Text("12.0s – 14.5s", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                            }
                                            Text("Discount Code & CTA", fontSize = 9.5.sp, color = TextWhite)
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 6: COMMENT PREDICTION
                        // ==================================================
                        AppleSimCard(title = "SECTION 6 — COMMENT PREDICTION") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                commentPredictions.forEach { comment ->
                                    val catColor = when (comment.category) {
                                        "Positive" -> EmeraldGreen
                                        "Negative" -> RoseRed
                                        else -> CyanAccent
                                    }
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp),
                                        color = SimCard
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(comment.userHandle, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                                Text("\"${comment.text}\"", fontSize = 11.5.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                                            }
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = catColor.copy(alpha = 0.2f)
                                            ) {
                                                Text(
                                                    text = comment.category,
                                                    fontSize = 8.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = catColor,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 7: FOLLOW PROBABILITY
                        // ==================================================
                        AppleSimCard(title = "SECTION 7 — FOLLOW PROBABILITY") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Animated Ring Canvas
                                Box(
                                    modifier = Modifier.size(80.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val strokeW = 8.dp.toPx()
                                        drawArc(
                                            color = SimSurface,
                                            startAngle = 0f,
                                            sweepAngle = 360f,
                                            useCenter = false,
                                            style = Stroke(width = strokeW)
                                        )
                                        drawArc(
                                            color = EmeraldGreen,
                                            startAngle = -90f,
                                            sweepAngle = 0.72f * 360f,
                                            useCenter = false,
                                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("72%", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = EmeraldGreen)
                                        Text("Follow", fontSize = 8.sp, color = TextSecondary)
                                    }
                                }

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("🟢 Strong Creator Trust", fontSize = 11.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                                    Text("🟢 Clear Face & Eye Contact", fontSize = 11.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                                    Text("🟡 Moderate End CTA Urgency", fontSize = 11.sp, color = AmberYellow, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 8 & 9: UPLOAD DECISION & VIRI EXPLANATION
                        // ==================================================
                        AppleSimCard(title = "SECTION 8 & 9 — UPLOAD DECISION & VIRI") {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                // Single Recommendation Decision Badge
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    color = EmeraldGreen.copy(alpha = 0.2f),
                                    border = BorderStroke(1.5.dp, EmeraldGreen)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(22.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("🟢 Upload Now Recommended", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = EmeraldGreen)
                                    }
                                }

                                // Viri Speech Bubble (Max 2 short sentences)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    ViriMascotWidget(size = 55.dp, action = ViriAction.HAPPY)
                                    Surface(
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        color = SimCard,
                                        border = BorderStroke(1.dp, CyanGlow)
                                    ) {
                                        Text(
                                            text = "\"Main ise abhi upload karta! Bas thumbnail frame select kar lo aur post kar do.\"",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextWhite,
                                            modifier = Modifier.padding(10.dp)
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
                        color = SimSurface,
                        border = BorderStroke(1.dp, GlassBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val simReport = """
                                        🚀 AI UPLOAD SIMULATOR SUMMARY 🚀
                                        Decision: 🟢 Upload Now
                                        30 Min Predicted Reach: 8,400 Views | 720 Likes | 310 Saves
                                        Follow Probability: 72%
                                    """.trimIndent()
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Sim Report", simReport))
                                    Toast.makeText(context, "Simulation Summary Copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SimCard,
                                    contentColor = CyanAccent
                                ),
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Text("Copy Summary", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    config?.let { onContinueToEditor(it) }
                                },
                                modifier = Modifier
                                    .weight(1.2f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyanAccent,
                                    contentColor = Color.Black
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("Open In Editor", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AppleSimCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = SimSurface,
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

@Composable
private fun SimMetricBadge(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = SimCard,
        border = BorderStroke(0.5.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontSize = 9.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        }
    }
}
