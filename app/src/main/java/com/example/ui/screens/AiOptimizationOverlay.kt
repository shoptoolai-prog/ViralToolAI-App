package com.example.ui.screens

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.core.MediaImportHelper
import com.example.ui.components.ViriAction
import com.example.ui.components.ViriMascotWidget
import kotlinx.coroutines.delay
import kotlin.random.Random

private val LuxuryDark = Color(0xFF0A0C10)
private val LuxurySurface = Color(0xFF12151E)
private val LuxuryCard = Color(0xFF191D2B)
private val LuxuryBorder = Color(0x3322D7E8)
private val CyanGlow = Color(0xFF22D7E8)
private val BrightPurple = Color(0xFFA78BFA)
private val EmeraldGreen = Color(0xFF10B981)
private val AmberYellow = Color(0xFFF59E0B)
private val RoseRed = Color(0xFFF43F5E)
private val TextWhite = Color(0xFFF8FAFC)
private val TextSecondary = Color(0xFF94A3B8)

data class TimelineMarker(
    val title: String,
    val timeLabel: String,
    val timeRatio: Float,
    val icon: String,
    val color: Color
)

data class SwipeRiskItem(
    val timeRange: String,
    val riskPercent: Int,
    val isCritical: Boolean
)

data class EmotionDataPoint(
    val label: String,
    val scorePercent: Int,
    val color: Color
)

data class ViewerQuestion(
    val question: String,
    val isAnswered: Boolean,
    val note: String
)

data class PredictedComment(
    val user: String,
    val comment: String,
    val sentiment: String
)

@Composable
fun AiOptimizationOverlay(
    config: ProjectSetupConfig?,
    onDismiss: () -> Unit,
    onApplyOptimizations: (ProjectSetupConfig) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var isApplying by remember { mutableStateOf(false) }
    var applyProgress by remember { mutableFloatStateOf(0f) }

    // Hinglish Random Summary Selection
    val hinglishSummaries = remember {
        listOf(
            "Main is reel ko upload nahi karta.",
            "Bas hook improve kar do, viral potential peak pe hai!",
            "Background clean kar lo aur price text add kar do.",
            "Ye reel viral hone ke bilkul close hai! Hook 🔥"
        )
    }
    val selectedSummary = remember { hinglishSummaries.random() }

    // Processing simulation when user clicks "Apply Selected Optimizations"
    LaunchedEffect(isApplying) {
        if (isApplying) {
            for (step in 1..100) {
                applyProgress = step / 100f
                delay(16)
            }
            delay(150)
            val finalConfig = config ?: MediaImportHelper.createDefaultProjectConfig(emptyList())
            onApplyOptimizations(finalConfig)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.88f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { if (!isApplying) onDismiss() },
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.96f)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .border(
                        BorderStroke(1.dp, LuxuryBorder),
                        RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { },
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = LuxuryDark
            ) {
                if (isApplying) {
                    ApplyingOptimizationsProgressView(progress = applyProgress)
                } else {
                    HumanBehaviourReportContent(
                        config = config,
                        selectedSummary = selectedSummary,
                        onDismiss = onDismiss,
                        onApply = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isApplying = true
                        }
                    )
                }
            }
        }
    }
}

// ==================================================
// HUMAN BEHAVIOUR AI REPORT CONTENT (10 SECTIONS)
// ==================================================
@Composable
private fun HumanBehaviourReportContent(
    config: ProjectSetupConfig?,
    selectedSummary: String,
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Drag Bar & Title Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Psychology,
                        contentDescription = null,
                        tint = CyanGlow,
                        modifier = Modifier.size(22.dp)
                    )
                    Column {
                        Text(
                            text = "HUMAN BEHAVIOUR AI ENGINE",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextWhite,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "Viewer Psychology & Behavioral Prediction",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Scrollable 10 Sections
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ==================================================
            // SECTION 1: ATTENTION TIMELINE
            // ==================================================
            SectionCard(
                title = "1. ATTENTION TIMELINE",
                icon = Icons.Default.Timeline,
                accentColor = CyanGlow
            ) {
                val markers = remember {
                    listOf(
                        TimelineMarker("Hook Started", "0.2s", 0.05f, "🔥", CyanGlow),
                        TimelineMarker("Viewer Interested", "1.8s", 0.25f, "👀", EmeraldGreen),
                        TimelineMarker("Best Moment", "4.2s", 0.45f, "❤️", BrightPurple),
                        TimelineMarker("Attention Drop", "7.5s", 0.65f, "😐", AmberYellow),
                        TimelineMarker("Swipe Risk", "11.0s", 0.82f, "⚠️", RoseRed),
                        TimelineMarker("CTA Moment", "13.5s", 0.95f, "🎯", CyanGlow)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "How a real viewer's attention changes frame-by-frame:",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    // Horizontal Line Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(LuxuryCard)
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxWidth().height(2.dp)) {
                            drawLine(
                                color = Color.White.copy(alpha = 0.2f),
                                start = Offset(0f, size.height / 2),
                                end = Offset(size.width, size.height / 2),
                                strokeWidth = 2.dp.toPx()
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            markers.forEach { m ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = m.icon,
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = m.timeLabel,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = m.color
                                    )
                                }
                            }
                        }
                    }

                    // Marker Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(markers) { m ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = LuxuryCard,
                                border = BorderStroke(1.dp, m.color.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(m.icon, fontSize = 11.sp)
                                    Text(
                                        text = "${m.title} (${m.timeLabel})",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ==================================================
            // SECTION 2: SWIPE RISK PROBABILITY GRAPH
            // ==================================================
            SectionCard(
                title = "2. SWIPE RISK PROBABILITY",
                icon = Icons.Default.Warning,
                accentColor = RoseRed
            ) {
                val risks = remember {
                    listOf(
                        SwipeRiskItem("0-3 sec", 8, isCritical = false),
                        SwipeRiskItem("3-6 sec", 18, isCritical = false),
                        SwipeRiskItem("6-10 sec", 42, isCritical = false),
                        SwipeRiskItem("10-14 sec", 71, isCritical = true)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Peak Swipe Risk Detected at 10-14s",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoseRed
                        )
                        Surface(
                            shape = CircleShape,
                            color = RoseRed.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, RoseRed)
                        ) {
                            Text(
                                text = "CRITICAL ⚠️",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoseRed,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        risks.forEach { item ->
                            val barColor = if (item.isCritical) RoseRed else if (item.riskPercent > 30) AmberYellow else EmeraldGreen
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp)),
                                color = LuxuryCard,
                                border = BorderStroke(1.dp, barColor.copy(alpha = 0.5f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = item.timeRange,
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(36.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color.Black),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(item.riskPercent / 100f)
                                                .background(barColor)
                                        )
                                    }
                                    Text(
                                        text = "${item.riskPercent}% Risk",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = barColor
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ==================================================
            // SECTION 3: EMOTION GRAPH
            // ==================================================
            SectionCard(
                title = "3. CREATOR EMOTION ANALYSIS",
                icon = Icons.Default.Face,
                accentColor = BrightPurple
            ) {
                val emotions = remember {
                    listOf(
                        EmotionDataPoint("Confident", 92, EmeraldGreen),
                        EmotionDataPoint("Happy", 85, CyanGlow),
                        EmotionDataPoint("Excited", 78, BrightPurple),
                        EmotionDataPoint("Low Energy", 28, AmberYellow),
                        EmotionDataPoint("Neutral", 15, TextSecondary),
                        EmotionDataPoint("Confused", 4, RoseRed)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Robot explanation bubble
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = BrightPurple.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, BrightPurple)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("🤖", fontSize = 16.sp)
                            Text(
                                text = "Viri Insight: \"Energy dropped after 8 seconds. Smile disappeared briefly at 10.2s.\"",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextWhite
                            )
                        }
                    }

                    emotions.chunked(2).forEach { rowPair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowPair.forEach { emo ->
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    color = LuxuryCard
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(emo.label, fontSize = 11.sp, color = TextWhite)
                                        Text("${emo.scorePercent}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = emo.color)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ==================================================
            // SECTION 4: VOICE ANALYSIS & WAVEFORM
            // ==================================================
            SectionCard(
                title = "4. VOICE & AUDIO WAVEFORM",
                icon = Icons.Default.GraphicEq,
                accentColor = EmeraldGreen
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Waveform Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(LuxuryCard)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val barCount = 36
                            val barWidth = size.width / barCount
                            val heights = floatArrayOf(
                                0.4f, 0.7f, 0.9f, 0.6f, 0.2f, 0.8f, 0.95f, 0.4f,
                                0.1f, 0.05f, 0.85f, 0.9f, 0.7f, 0.5f, 0.3f, 0.88f,
                                0.92f, 0.6f, 0.15f, 0.75f, 0.8f, 0.6f, 0.4f, 0.9f,
                                0.7f, 0.5f, 0.2f, 0.85f, 0.95f, 0.6f, 0.4f, 0.7f,
                                0.5f, 0.3f, 0.6f, 0.4f
                            )

                            for (i in 0 until barCount) {
                                val hRatio = heights.getOrElse(i) { 0.5f }
                                val h = size.height * hRatio
                                val top = (size.height - h) / 2f
                                val isDeadAir = (i in 8..9) // Highlight dead air
                                val color = if (isDeadAir) RoseRed else EmeraldGreen

                                drawRoundRect(
                                    color = color,
                                    topLeft = Offset(i * barWidth + barWidth * 0.2f, top),
                                    size = Size(barWidth * 0.6f, h),
                                    cornerRadius = CornerRadius(2.dp.toPx())
                                )
                            }
                        }
                    }

                    // Voice Parameters Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        VoiceBadge("Energy", "High (88%)", EmeraldGreen)
                        VoiceBadge("Speed", "145 WPM", CyanGlow)
                        VoiceBadge("Dead Silence", "0.8s ⚠️", RoseRed)
                        VoiceBadge("Confidence", "High (92%)", BrightPurple)
                    }
                }
            }

            // ==================================================
            // SECTION 5: PRODUCT VISIBILITY
            // ==================================================
            SectionCard(
                title = "5. PRODUCT VISIBILITY",
                icon = Icons.Default.Visibility,
                accentColor = CyanGlow
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MetricRow("Seconds Visible", "8.4s / 15.0s (56%)", EmeraldGreen)
                    MetricRow("Product Frame Coverage", "38% of Screen", CyanGlow)
                    MetricRow("Logo Visibility", "92% Clear", EmeraldGreen)
                    MetricRow("Price Visibility", "0% (Price Hidden)", RoseRed)
                    MetricRow("Hand Covering Product", "Yes (1.2s at 05.4s)", AmberYellow)
                    MetricRow("Product Outside Safe Area", "No (100% Inside)", EmeraldGreen)
                }
            }

            // ==================================================
            // SECTION 6: HOOK STRENGTH
            // ==================================================
            SectionCard(
                title = "6. HOOK STRENGTH (FIRST 3 SECONDS)",
                icon = Icons.Default.Star,
                accentColor = AmberYellow
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Hook Meter Rank", fontSize = 12.sp, color = TextSecondary)
                        Surface(
                            shape = CircleShape,
                            color = EmeraldGreen.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, EmeraldGreen)
                        ) {
                            Text(
                                text = "ELITE HOOK ⭐",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = EmeraldGreen,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    HookFactorRow("Face Expression", 95, EmeraldGreen)
                    HookFactorRow("Motion & Action", 88, EmeraldGreen)
                    HookFactorRow("Voice Punchiness", 92, EmeraldGreen)
                    HookFactorRow("Subtitle Readability", 98, EmeraldGreen)
                    HookFactorRow("Text Overlay Contrast", 70, AmberYellow)
                }
            }

            // ==================================================
            // SECTION 7: VIEWER QUESTIONS
            // ==================================================
            SectionCard(
                title = "7. VIEWER QUESTIONS PREDICTION",
                icon = Icons.Default.QuestionAnswer,
                accentColor = BrightPurple
            ) {
                val questions = remember {
                    listOf(
                        ViewerQuestion("Price?", isAnswered = false, "Missing in clip"),
                        ViewerQuestion("Link?", isAnswered = false, "Not mentioned verbally"),
                        ViewerQuestion("Quality?", isAnswered = true, "Clear in 4K video"),
                        ViewerQuestion("Color available?", isAnswered = false, "Unclear"),
                        ViewerQuestion("COD?", isAnswered = false, "Unclear"),
                        ViewerQuestion("Size?", isAnswered = true, "Partially shown")
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Predicted questions viewers will ask in comments:", fontSize = 11.5.sp, color = TextSecondary)

                    questions.chunked(2).forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            pair.forEach { q ->
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    color = LuxuryCard,
                                    border = BorderStroke(1.dp, if (q.isAnswered) EmeraldGreen.copy(alpha = 0.4f) else RoseRed.copy(alpha = 0.4f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(q.question, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text(
                                            text = if (q.isAnswered) "✅ Answered" else "❌ Missing",
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (q.isAnswered) EmeraldGreen else RoseRed
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ==================================================
            // SECTION 8: COMMENT PREDICTION
            // ==================================================
            SectionCard(
                title = "8. COMMENT PREDICTION & SENTIMENT",
                icon = Icons.Default.Comment,
                accentColor = CyanGlow
            ) {
                val comments = remember {
                    listOf(
                        PredictedComment("@aanya_v", "Link please 🔗", "Positive"),
                        PredictedComment("@rohit_07", "Price kitna hai?", "Neutral"),
                        PredictedComment("@tech_guy", "COD available hai kya?", "Neutral"),
                        PredictedComment("@style_diva", "Looking so beautiful 😍", "Positive"),
                        PredictedComment("@troll_master", "Fake lag raha hai bro", "Negative")
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Sentiment Ratio Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SentimentRatioBadge("68% Positive", EmeraldGreen)
                        SentimentRatioBadge("22% Neutral", CyanGlow)
                        SentimentRatioBadge("10% Negative", RoseRed)
                    }

                    comments.forEach { c ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = LuxuryCard
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(c.user, fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                                    Text(c.comment, fontSize = 11.5.sp, color = TextWhite)
                                }
                                Surface(
                                    shape = CircleShape,
                                    color = if (c.sentiment == "Positive") EmeraldGreen.copy(alpha = 0.2f) else if (c.sentiment == "Neutral") CyanGlow.copy(alpha = 0.2f) else RoseRed.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = c.sentiment,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (c.sentiment == "Positive") EmeraldGreen else if (c.sentiment == "Neutral") CyanGlow else RoseRed,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ==================================================
            // SECTION 9: SAVE / SHARE CHANCE GAUGES
            // ==================================================
            SectionCard(
                title = "9. SAVE / SHARE CHANCE GAUGES",
                icon = Icons.Default.Share,
                accentColor = BrightPurple
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CircularGauge("Save", 78, BrightPurple)
                    CircularGauge("Share", 84, EmeraldGreen)
                    CircularGauge("Comment", 65, CyanGlow)
                    CircularGauge("Profile", 52, AmberYellow)
                    CircularGauge("Follow", 46, TextSecondary)
                }
            }

            // ==================================================
            // SECTION 10: FINAL AI SUMMARY WITH VIRI ROBOT
            // ==================================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = LuxuryCard,
                border = BorderStroke(1.5.dp, CyanGlow)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ViriMascotWidget(
                            action = ViriAction.SITTING,
                            size = 72.dp
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "10. FINAL AI VERDICT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanGlow,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "\"$selectedSummary\"",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextWhite
                            )
                        }
                    }

                    Button(
                        onClick = onApply,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyanGlow,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Apply AI Fixes & Proceed to Editor", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ==================================================
// HELPER COMPONENTS
// ==================================================
@Composable
private fun SectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = LuxurySurface,
        border = BorderStroke(1.dp, LuxuryBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite, letterSpacing = 0.8.sp)
            }
            content()
        }
    }
}

@Composable
private fun MetricRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 11.5.sp, color = TextSecondary)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun VoiceBadge(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 9.5.sp, color = TextSecondary)
        Text(value, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun HookFactorRow(label: String, score: Int, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 11.sp, color = TextWhite)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(score / 100f)
                        .fillMaxHeight()
                        .background(color)
                )
            }
            Text("$score%", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun SentimentRatioBadge(label: String, color: Color) {
    Surface(
        shape = CircleShape,
        color = color.copy(alpha = 0.2f),
        border = BorderStroke(1.dp, color)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun CircularGauge(label: String, percent: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(46.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    style = Stroke(width = 3.dp.toPx())
                )
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = 360f * (percent / 100f),
                    useCenter = false,
                    style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Text("$percent%", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 9.5.sp, color = TextSecondary)
    }
}

@Composable
private fun ApplyingOptimizationsProgressView(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ViriMascotWidget(action = ViriAction.HAPPY, size = 90.dp)

            Text(
                text = "Applying AI Fixes & Optimizing Reel...",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = CyanGlow,
                trackColor = Color(0xFF202636)
            )

            Text(
                text = "${(progress * 100).toInt()}% Complete",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CyanGlow
            )
        }
    }
}
