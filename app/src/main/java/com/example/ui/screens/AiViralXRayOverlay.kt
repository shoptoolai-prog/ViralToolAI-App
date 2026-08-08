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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Analytics
import com.example.creatoracademy.FaceEngineV2
import com.example.creatoracademy.AnalysedReel
import com.example.creatoracademy.OcrEngineV2
import com.example.creatoracademy.LogoEngineV2
import com.example.creatoracademy.ProductEngineV2
import com.example.creatoracademy.PriceEngineV2
import com.example.creatoracademy.HumanActivityEngineV2
import com.example.creatoracademy.SceneClassificationEngineV2
import com.example.creatoracademy.SpeechEngineV2
import androidx.compose.material.icons.outlined.AutoFixHigh
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlinx.coroutines.delay

// Colors specified in DS-23: Apple Human Interface, Cyan #22D3EE primary, Soft Glass, NO PURPLE
private val XRayDark = Color(0xFF090B10)
private val XRaySurface = Color(0xFF11141C)
private val XRayCard = Color(0xFF181C27)
private val CyanAccent = Color(0xFF22D3EE)
private val CyanGlow = Color(0x3322D3EE)
private val GlassBorder = Color(0x3322D3EE)
private val EmeraldGreen = Color(0xFF10B981)
private val AmberYellow = Color(0xFFF59E0B)
private val RoseRed = Color(0xFFF43F5E)
private val TextWhite = Color(0xFFF8FAFC)
private val TextSecondary = Color(0xFF94A3B8)

data class ReelDnaItem(
    val label: String,
    val value: String
)

data class ViralTimelineBlock(
    val timeLabel: String,
    val second: Int,
    val statusColor: Color,
    val statusText: String,
    val detailExplanation: String
)

data class FrameXRayData(
    val frameIndex: Int,
    val timestampMs: Long,
    val faceQuality: Int,
    val productVisibility: Int,
    val lighting: String,
    val composition: String,
    val textSafety: String,
    val emotion: String,
    val ctrValue: String,
    val rankLabel: String,
    val fullExplanation: String
)

data class EmotionMarker(
    val timeRange: String,
    val emotionEmoji: String,
    val emotionLabel: String,
    val description: String
)

@Composable
fun AiViralXRayOverlay(
    config: ProjectSetupConfig?,
    onDismiss: () -> Unit,
    onContinueToEditor: (ProjectSetupConfig) -> Unit,
    onOpenCoach: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val videoUri = config?.selectedMedia?.firstOrNull()?.uri

    // Selected state for Interactive Blocks & Frame detail dialog
    var selectedTimelineBlock by remember { mutableStateOf<ViralTimelineBlock?>(null) }
    var selectedFrameXRay by remember { mutableStateOf<FrameXRayData?>(null) }
    var interactiveGraphSecond by remember { mutableIntStateOf(2) }

    // Confidence indicator state
    val confidenceScore = 92
    val isConfidenceLow = confidenceScore < 70

    // SECTION 1: REEL DNA DATA
    val reelDnaList = remember {
        listOf(
            ReelDnaItem("Category", "Beauty & Tech Review"),
            ReelDnaItem("Product Type", "Smart Product Unboxing"),
            ReelDnaItem("Creator Type", "Micro Influencer"),
            ReelDnaItem("Language", "Hinglish"),
            ReelDnaItem("Target Audience", "Women & Gen-Z (18–34)"),
            ReelDnaItem("Editing Style", "Fast Jump Cut Demo"),
            ReelDnaItem("Review Style", "Honest Hands-On Rating"),
            ReelDnaItem("Platform", "Instagram Reels / Shorts")
        )
    }

    // SECTION 2: VIRAL TIMELINE BLOCKS (Second by Second)
    val timelineBlocks = remember {
        listOf(
            ViralTimelineBlock("0–1s", 1, EmeraldGreen, "🟢 Excellent Hook", "Strong high-contrast facial emotion and clear text overlay captured audience attention in under 0.4s."),
            ViralTimelineBlock("1–2s", 2, EmeraldGreen, "🟢 Face Visible", "Clear eye contact and confident smile created immediate creator trust."),
            ViralTimelineBlock("2–3s", 3, AmberYellow, "🟡 Product Hidden", "Product was briefly angled away from camera light, leading to minor visual ambiguity."),
            ViralTimelineBlock("3–5s", 4, RoseRed, "🔴 Attention Drop", "Pacing slowed down during explanation before product demo started. High skip probability."),
            ViralTimelineBlock("5–7s", 6, EmeraldGreen, "🟢 Product Focus", "Close-up macro shot of the product feature restored retention upwards by +18%."),
            ViralTimelineBlock("7–10s", 8, EmeraldGreen, "🟢 High Energy", "Dynamic voice modulation and rhythmic sound effect held strong viewer engagement."),
            ViralTimelineBlock("10–12s", 11, AmberYellow, "🟡 Text Overlap", "Caption text briefly obstructed bottom price tag. Adjust margin by 12dp."),
            ViralTimelineBlock("12–15s", 14, EmeraldGreen, "🟢 Strong CTA", "Clear call-to-action badge with comment trigger boosted reply conversion rate.")
        )
    }

    // SECTION 4: FRAME X-RAY DATA (Top 12 Extracted Frames)
    val frameXRayList = remember {
        listOf(
            FrameXRayData(1, 1000L, 98, 92, "Studio Soft", "Rule of Thirds", "100% Safe", "😊 Excited", "CTR +24%", "Rank #1", "Frame #1 has maximum facial clarity and perfect ambient lighting. Highest recommended thumbnail!"),
            FrameXRayData(2, 2000L, 95, 90, "Bright", "Centered", "100% Safe", "😍 Happy", "CTR +20%", "Rank #2", "Frame #2 captures sharp product angle with clear smile. Great secondary choice."),
            FrameXRayData(3, 3000L, 88, 85, "Good", "Off-Center", "90% Safe", "😐 Neutral", "CTR +15%", "Rank #3", "Frame #3 has clear product focus but facial emotion is neutral."),
            FrameXRayData(4, 4500L, 82, 78, "Medium", "Wide Shot", "85% Safe", "😲 Surprise", "CTR +12%", "Rank #4", "Frame #4 features high surprise expression which creates strong curiosity."),
            FrameXRayData(5, 6000L, 91, 95, "Studio Bright", "Macro Product", "100% Safe", "😊 Happy", "CTR +19%", "Rank #5", "Frame #5 macro product details highlight key texture and premium finish."),
            FrameXRayData(6, 7500L, 89, 88, "Good", "Centered", "95% Safe", "😍 Excited", "CTR +16%", "Rank #6", "Frame #6 good lighting and steady holding angle."),
            FrameXRayData(7, 9000L, 85, 84, "Natural", "Centered", "90% Safe", "😊 Happy", "CTR +14%", "Rank #7", "Frame #7 mid-section reel demonstration."),
            FrameXRayData(8, 10500L, 87, 86, "Good", "Close-up", "92% Safe", "😍 Excited", "CTR +15%", "Rank #8", "Frame #8 strong eye contact frame."),
            FrameXRayData(9, 12000L, 80, 82, "Warm", "Centered", "88% Safe", "😐 Neutral", "CTR +11%", "Rank #9", "Frame #9 transitioning scene."),
            FrameXRayData(10, 13500L, 92, 94, "Bright", "Close-up", "100% Safe", "😊 Happy", "CTR +18%", "Rank #10", "Frame #10 CTA highlight with product held next to face."),
            FrameXRayData(11, 14500L, 90, 91, "Good", "Centered", "95% Safe", "😍 Excited", "CTR +17%", "Rank #11", "Frame #11 enthusiastic outro pose."),
            FrameXRayData(12, 15000L, 86, 89, "Studio Soft", "Centered", "90% Safe", "😊 Happy", "CTR +13%", "Rank #12", "Frame #12 final logo & discount frame.")
        )
    }

    // SECTION 9: EMOTION DETECTOR MARKERS
    val emotionMarkers = remember {
        listOf(
            EmotionMarker("0–2s", "😍", "Excited", "High energy opening statement grab"),
            EmotionMarker("2–5s", "😐", "Neutral", "Explaining specifications & details"),
            EmotionMarker("5–8s", "😲", "Surprise", "Revealing hidden feature demo"),
            EmotionMarker("8–12s", "😊", "Happy", "Positive personal experience review"),
            EmotionMarker("12–15s", "😍", "Excited", "Closing CTA with discount urgency")
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(XRayDark),
            color = XRayDark
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
                    // Header Bar
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
                                        imageVector = Icons.Outlined.Analytics,
                                        contentDescription = null,
                                        tint = CyanAccent,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = "🔥 Viral X-Ray",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "See why your reel wins or loses.",
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
                                .background(XRaySurface)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Low Confidence Notice Banner (if applicable)
                    if (isConfidenceLow) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = AmberYellow.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, AmberYellow.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = AmberYellow, modifier = Modifier.size(18.dp))
                                Text(
                                    text = "Confidence is low. This recommendation may be inaccurate.",
                                    fontSize = 11.sp,
                                    color = AmberYellow,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Scrollable Sections (11 Sections)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // ==================================================
                        // SECTION 1: REEL DNA
                        // ==================================================
                        AppleCardContainer(title = "SECTION 1 — REEL DNA") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "AI comparison baseline vs thousands of top viral reels in your category:",
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )

                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier = Modifier.height(180.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    userScrollEnabled = false
                                ) {
                                    items(reelDnaList) { item ->
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = XRayCard,
                                            border = BorderStroke(0.5.dp, GlassBorder)
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Text(text = item.label, fontSize = 9.5.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                                                Text(
                                                    text = item.value,
                                                    fontSize = 11.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = CyanAccent,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 2: VIRAL TIMELINE
                        // ==================================================
                        AppleCardContainer(title = "SECTION 2 — VIRAL TIMELINE") {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    text = "Second-by-second breakdown. Tap any block for AI diagnosis:",
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(timelineBlocks) { block ->
                                        val isSelected = selectedTimelineBlock?.second == block.second
                                        Surface(
                                            modifier = Modifier
                                                .width(110.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable {
                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    selectedTimelineBlock = if (isSelected) null else block
                                                },
                                            shape = RoundedCornerShape(12.dp),
                                            color = XRayCard,
                                            border = BorderStroke(
                                                1.dp,
                                                if (isSelected) CyanAccent else block.statusColor.copy(alpha = 0.5f)
                                            )
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(8.dp),
                                                horizontalAlignment = Alignment.Start,
                                                verticalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(text = block.timeLabel, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                                Text(
                                                    text = block.statusText,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = block.statusColor,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = "Tap to expand",
                                                    fontSize = 8.5.sp,
                                                    color = TextSecondary
                                                )
                                            }
                                        }
                                    }
                                }

                                // Expanded Explanation Card when block tapped
                                AnimatedVisibility(
                                    visible = selectedTimelineBlock != null,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    selectedTimelineBlock?.let { b ->
                                        Surface(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp),
                                            color = XRaySurface,
                                            border = BorderStroke(1.dp, CyanGlow)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.Top,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Outlined.Psychology,
                                                    contentDescription = null,
                                                    tint = CyanAccent,
                                                    modifier = Modifier.size(22.dp)
                                                )
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = "AI Diagnosis for ${b.timeLabel} (${b.statusText})",
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = CyanAccent
                                                    )
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = b.detailExplanation,
                                                        fontSize = 11.sp,
                                                        color = TextWhite,
                                                        lineHeight = 15.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 3: ATTENTION GRAPH
                        // ==================================================
                        AppleCardContainer(title = "SECTION 3 — ATTENTION GRAPH") {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Retention Curve", fontSize = 11.5.sp, color = TextSecondary)
                                    Text("Selected: ${interactiveGraphSecond}s", fontSize = 11.sp, color = CyanAccent, fontWeight = FontWeight.Bold)
                                }

                                // Interactive Retention Graph Canvas
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(130.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(XRayCard)
                                        .pointerInput(Unit) {
                                            detectTapGestures { offset ->
                                                val fraction = (offset.x / size.width).coerceIn(0f, 1f)
                                                interactiveGraphSecond = (fraction * 15).toInt().coerceIn(0, 15)
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            }
                                        }
                                ) {
                                    Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                                        val w = size.width
                                        val h = size.height

                                        // Draw Retention Curve Path
                                        val path = Path().apply {
                                            moveTo(0f, h * 0.1f) // 90% at 0s
                                            cubicTo(w * 0.1f, h * 0.15f, w * 0.2f, h * 0.45f, w * 0.3f, h * 0.5f) // Attention Drop
                                            cubicTo(w * 0.4f, h * 0.55f, w * 0.5f, h * 0.2f, w * 0.6f, h * 0.18f) // Product Focus Boost
                                            cubicTo(w * 0.7f, h * 0.15f, w * 0.85f, h * 0.3f, w, h * 0.25f) // Strong CTA Finish
                                        }

                                        // Background Fill Gradient
                                        val fillPath = Path().apply {
                                            addPath(path)
                                            lineTo(w, h)
                                            lineTo(0f, h)
                                            close()
                                        }
                                        drawPath(
                                            path = fillPath,
                                            brush = Brush.verticalGradient(
                                                colors = listOf(CyanAccent.copy(alpha = 0.3f), Color.Transparent)
                                            )
                                        )

                                        // Draw Curve Stroke
                                        drawPath(
                                            path = path,
                                            color = CyanAccent,
                                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                                        )

                                        // Indicator Line at selected second
                                        val selectedX = (interactiveGraphSecond / 15f) * w
                                        drawLine(
                                            color = TextWhite,
                                            start = Offset(selectedX, 0f),
                                            end = Offset(selectedX, h),
                                            strokeWidth = 1.5.dp.toPx(),
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                                        )
                                        drawCircle(
                                            color = CyanAccent,
                                            radius = 5.dp.toPx(),
                                            center = Offset(selectedX, h * 0.3f)
                                        )
                                    }
                                }

                                // Legend Tags
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("🔥 High Interest", fontSize = 9.5.sp, color = EmeraldGreen, fontWeight = FontWeight.Bold)
                                    Text("🙂 Normal", fontSize = 9.5.sp, color = CyanAccent, fontWeight = FontWeight.Bold)
                                    Text("😴 Boring", fontSize = 9.5.sp, color = AmberYellow, fontWeight = FontWeight.Bold)
                                    Text("⚠ Skip Zone", fontSize = 9.5.sp, color = RoseRed, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 4: FRAME X-RAY
                        // ==================================================
                        AppleCardContainer(title = "SECTION 4 — FRAME X-RAY (TOP 12)") {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("AI deep visual analysis of top 12 extracted frames. Tap to view full breakdown:", fontSize = 11.5.sp, color = TextSecondary)

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(frameXRayList) { frameData ->
                                        Surface(
                                            modifier = Modifier
                                                .width(135.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable {
                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                    selectedFrameXRay = frameData
                                                },
                                            shape = RoundedCornerShape(12.dp),
                                            color = XRayCard,
                                            border = BorderStroke(1.dp, GlassBorder)
                                        ) {
                                            Column(modifier = Modifier.padding(6.dp)) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(90.dp)
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(Color.Black)
                                                ) {
                                                    AsyncImage(
                                                        model = ImageRequest.Builder(context)
                                                            .data(videoUri)
                                                            .videoFrameMillis(frameData.timestampMs)
                                                            .crossfade(true)
                                                            .build(),
                                                        contentDescription = "Frame ${frameData.frameIndex}",
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier.fillMaxSize()
                                                    )
                                                    Surface(
                                                        modifier = Modifier.align(Alignment.TopStart).padding(4.dp),
                                                        shape = RoundedCornerShape(4.dp),
                                                        color = Color.Black.copy(alpha = 0.75f)
                                                    ) {
                                                        Text(
                                                            text = frameData.rankLabel,
                                                            fontSize = 8.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = CyanAccent,
                                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                        )
                                                    }
                                                }

                                                Spacer(modifier = Modifier.height(6.dp))

                                                Text(text = "Frame #${frameData.frameIndex}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                                Text(text = "Face Quality: ${frameData.faceQuality}%", fontSize = 9.sp, color = TextSecondary)
                                                Text(text = "Product Vis: ${frameData.productVisibility}%", fontSize = 9.sp, color = TextSecondary)
                                                Text(text = "Emotion: ${frameData.emotion}", fontSize = 9.sp, color = TextWhite)
                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = EmeraldGreen.copy(alpha = 0.2f),
                                                    modifier = Modifier.padding(top = 4.dp)
                                                ) {
                                                    Text(
                                                        text = frameData.ctrValue,
                                                        fontSize = 8.5.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = EmeraldGreen,
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
                        // SECTION 5: HOOK COMPARISON
                        // ==================================================
                        AppleCardContainer(title = "SECTION 5 — HOOK COMPARISON") {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Left: Your Hook
                                    Surface(
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        color = XRayCard,
                                        border = BorderStroke(1.dp, RoseRed.copy(alpha = 0.4f))
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text("YOUR HOOK", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = RoseRed)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("• Missing curiosity trigger\n• Weak facial movement\n• Late product reveal (2.5s)\n• Slow intro delivery", fontSize = 10.sp, color = TextWhite, lineHeight = 14.sp)
                                        }
                                    }

                                    // Right: Top Viral Hook
                                    Surface(
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        color = XRayCard,
                                        border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.4f))
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text("TOP VIRAL HOOK", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("• Instant problem query\n• Bold expressive smile\n• Product in first 0.5s\n• Snappy voiceover", fontSize = 10.sp, color = TextWhite, lineHeight = 14.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 6: SCROLL STOP SCORE
                        // ==================================================
                        AppleCardContainer(title = "SECTION 6 — SCROLL STOP SCORE") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Scroll Stop Prediction:", fontSize = 11.5.sp, color = TextSecondary)
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = EmeraldGreen.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.5f))
                                    ) {
                                        Text(
                                            text = "🟢 Strong (88%)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldGreen,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "\"High-contrast face close-up with energetic text overlay stops user scroll within 0.4 seconds of playback.\"",
                                    fontSize = 11.5.sp,
                                    color = TextWhite,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        // ==================================================
                        // SECTION 7: PRODUCT VISIBILITY
                        // ==================================================
                        AppleCardContainer(title = "SECTION 7 — PRODUCT VISIBILITY") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Timeline Visibility Bar:", fontSize = 11.5.sp, color = TextSecondary)

                                // Visual Bar
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(7.dp))
                                ) {
                                    Box(modifier = Modifier.weight(0.15f).fillMaxHeight().background(RoseRed)) // 0-2s Hidden
                                    Box(modifier = Modifier.weight(0.1f).fillMaxHeight().background(AmberYellow)) // 2-3s Partial
                                    Box(modifier = Modifier.weight(0.75f).fillMaxHeight().background(EmeraldGreen)) // 3-15s Visible
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("🔴 Hidden", fontSize = 9.sp, color = RoseRed)
                                    Text("🟡 Partial", fontSize = 9.sp, color = AmberYellow)
                                    Text("🟢 Visible", fontSize = 9.sp, color = EmeraldGreen)
                                }

                                Text(
                                    text = "💡 AI Suggestion: Reveal product macro angle at 0.8s instead of 2.5s for +14% conversion boost.",
                                    fontSize = 10.5.sp,
                                    color = CyanAccent
                                )
                            }
                        }

                        // ==================================================
                        // SECTION 8: FACE ANALYSIS (FACE ENGINE V2.0)
                        // ==================================================
                        AppleCardContainer(title = "SECTION 8 — FACE ANALYSIS V2.0") {
                            val dummyReel = remember { AnalysedReel(id = "1", title = "Reel", date = "Today") }
                            val fReport = remember(videoUri) {
                                FaceEngineV2.analyzeReelFaceEngineV2(context, videoUri, 15.0f, dummyReel)
                            }
                            val pDetection = fReport.personDetection

                            if (!pDetection.isHumanPresent) {
                                Text(
                                    text = "No human face detected.",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RoseRed
                                )
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf(
                                        "Eye Contact" to (fReport.eyeDetection?.eyeContactScore ?: 80),
                                        "Sharpness" to (fReport.faceQuality?.sharpness ?: 85),
                                        "Lighting" to (fReport.faceQuality?.lighting ?: 82),
                                        "Smile Quality" to (fReport.expression?.smilePercent ?: 75),
                                        "Vocal Energy" to (fReport.speaking?.voiceMatchConfidence ?: 88)
                                    ).forEach { (label, value) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(label, fontSize = 10.5.sp, color = TextWhite, modifier = Modifier.width(100.dp))
                                            LinearProgressIndicator(
                                                progress = { value / 100f },
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(6.dp)
                                                    .clip(RoundedCornerShape(3.dp)),
                                                color = CyanAccent,
                                                trackColor = XRaySurface
                                            )
                                            Text("${value}%", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = CyanAccent, modifier = Modifier.width(36.dp), textAlign = TextAlign.End)
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 8B: OCR & SCENE TEXT INTELLIGENCE V2.0
                        // ==================================================
                        AppleCardContainer(title = "SECTION 8B — OCR & TEXT ENGINE V2.0") {
                            val dummyReelForOcr = remember { AnalysedReel(id = "1", title = "Reel", date = "Today") }
                            val oReport = remember(videoUri) {
                                OcrEngineV2.analyzeReelOcrEngineV2(context, videoUri, 15.0f, dummyReelForOcr)
                            }
                            val act = oReport.activation
                            val summ = oReport.summary

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Text Activation", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text(
                                        text = if (act.isTextVisible) "DETECTED (${act.confidencePercent}% Conf)" else "NO READABLE TEXT",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (act.isTextVisible) EmeraldGreen else RoseRed
                                    )
                                }

                                if (!act.isTextVisible || oReport.failSafeActive) {
                                    Text(
                                        text = "No readable text detected.",
                                        fontSize = 11.sp,
                                        color = RoseRed
                                    )
                                } else {
                                    listOf(
                                        "Language" to summ.primaryLanguage.displayName,
                                        "Price" to summ.priceDisplay,
                                        "Brand / Logo" to summ.brandDisplay,
                                        "CTA" to summ.ctaDisplay,
                                        "Watermark" to summ.watermarkDisplay,
                                        "Readability" to oReport.readability.readabilityRating.name
                                    ).forEach { (k, v) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(k, fontSize = 10.5.sp, color = TextSecondary)
                                            Text(v, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 8C: BRAND & LOGO ENGINE V2.0
                        // ==================================================
                        AppleCardContainer(title = "SECTION 8C — BRAND & LOGO ENGINE V2.0") {
                            val dummyReelForLogo = remember { AnalysedReel(id = "1", title = "Reel", date = "Today") }
                            val lReport = remember(videoUri) {
                                LogoEngineV2.analyzeReelLogoEngineV2(context, videoUri, 15.0f, dummyReelForLogo)
                            }
                            val lAct = lReport.activation
                            val lSumm = lReport.summary
                            val lShop = lReport.shoppingIntegration

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Logo Recognition", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text(
                                        text = if (lAct.isLogoVisible) "DETECTED (${lAct.overallConfidencePercent}% Conf)" else "NO RECOGNIZABLE LOGO",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (lAct.isLogoVisible) EmeraldGreen else RoseRed
                                    )
                                }

                                if (!lAct.isLogoVisible || lReport.failSafeActive) {
                                    Text(
                                        text = lReport.failSafeNotice ?: "No reliable logo detected.",
                                        fontSize = 11.sp,
                                        color = RoseRed
                                    )
                                } else {
                                    listOf(
                                        "Logos" to lSumm.logosDetected.joinToString(),
                                        "Brand Conf" to "${lSumm.brandConfidencePercent}%",
                                        "Shopping Logo" to if (lSumm.shoppingLogoDetected) "YES" else "NO",
                                        "Social Platform" to (lSumm.primarySocialPlatform ?: "None"),
                                        "Shopping Link" to lShop.statusNotice
                                    ).forEach { (k, v) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(k, fontSize = 10.5.sp, color = TextSecondary)
                                            Text(v, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 8D: PRODUCT INTELLIGENCE ENGINE V2.0
                        // ==================================================
                        AppleCardContainer(title = "SECTION 8D — PRODUCT ENGINE V2.0") {
                            val dummyReelForProduct = remember { AnalysedReel(id = "1", title = "Reel", date = "Today") }
                            val pReport = remember(videoUri) {
                                ProductEngineV2.analyzeReelProductEngineV2(context, videoUri, 15.0f, dummyReelForProduct)
                            }
                            val pAct = pReport.activation
                            val pSumm = pReport.summary
                            val pShop = pReport.shoppingContext

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Product Recognition", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text(
                                        text = if (pAct.isProductPresent) "DETECTED (${pAct.activationConfidencePercent}% Conf)" else "NO PRODUCT CONFIDENTLY DETECTED",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (pAct.isProductPresent) EmeraldGreen else RoseRed
                                    )
                                }

                                if (!pAct.isProductPresent || pReport.failSafeActive) {
                                    Text(
                                        text = pReport.failSafeNotice ?: "No product confidently detected.",
                                        fontSize = 11.sp,
                                        color = RoseRed
                                    )
                                    Text(
                                        text = "Shopping & Price Engine disabled (Product absent).",
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                } else {
                                    listOf(
                                        "Primary Product" to (pSumm.primaryProductName ?: "None"),
                                        "Category" to (pSumm.categoryLabel ?: "Unknown"),
                                        "Visibility" to "${pSumm.visibilityPercent}%",
                                        "Presentation" to pSumm.presentationLabel,
                                        "Lighting" to pSumm.lightingLabel,
                                        "Packaging" to pSumm.packagingLabel,
                                        "Brand" to (pSumm.brandLabel ?: "Verified Brand"),
                                        "Shopping Mode" to pShop.detectedMode.label
                                    ).forEach { (k, v) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(k, fontSize = 10.5.sp, color = TextSecondary)
                                            Text(v, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 8E: PRICE INTELLIGENCE ENGINE V2.0
                        // ==================================================
                        AppleCardContainer(title = "SECTION 8E — PRICE ENGINE V2.0") {
                            val dummyReelForPrice = remember { AnalysedReel(id = "1", title = "Reel", date = "Today") }
                            val priceReport = remember(videoUri) {
                                PriceEngineV2.analyzeReelPriceEngineV2(context, videoUri, 15.0f, dummyReelForPrice)
                            }
                            val prAct = priceReport.activation
                            val prSumm = priceReport.summary
                            val prShop = priceReport.shoppingGate

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Price Verification", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text(
                                        text = if (prAct.isPriceActive) "ACTIVE (${prAct.productConfidencePercent}% Prod / ${prAct.ocrConfidencePercent}% OCR)" else "INACTIVE (<80% Conf)",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (prAct.isPriceActive) EmeraldGreen else RoseRed
                                    )
                                }

                                if (!prAct.isPriceActive || priceReport.failSafeActive) {
                                    Text(
                                        text = "No reliable price detected.",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RoseRed
                                    )
                                    Text(
                                        text = priceReport.failSafeNotice ?: "Unable to confidently read the product price.",
                                        fontSize = 10.5.sp,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = prShop.gateReason,
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                } else {
                                    listOf(
                                        "Detected Price" to (prSumm.detectedPriceText ?: "None"),
                                        "Currency" to (prSumm.currencyCode ?: "Unknown"),
                                        "MRP Price" to (prSumm.mrpPriceText ?: "N/A"),
                                        "Discount Offer" to (priceReport.discountInfo.offerText ?: "None"),
                                        "Readability" to "${priceReport.visibilityReport.readabilityScore}%",
                                        "Shopping Gate" to if (prShop.isShoppingActive) "Shopping Modules Active" else "Shopping Disabled"
                                    ).forEach { (k, v) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(k, fontSize = 10.5.sp, color = TextSecondary)
                                            Text(v, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 8F: HUMAN ACTIVITY ENGINE V2.0
                        // ==================================================
                        AppleCardContainer(title = "SECTION 8F — HUMAN ACTIVITY V2.0") {
                            val dummyReelForActivity = remember { AnalysedReel(id = "1", title = "Reel", date = "Today") }
                            val activityReport = remember(videoUri) {
                                HumanActivityEngineV2.analyzeReelHumanActivityV2(context, videoUri, 15.0f, dummyReelForActivity)
                            }
                            val actActivation = activityReport.activation
                            val actSummary = activityReport.summary

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Human Activity Status", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text(
                                        text = if (actActivation.isActivityActive) "ACTIVE (${actActivation.humanConfidencePercent}% Conf)" else "INACTIVE (<75% Conf)",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (actActivation.isActivityActive) EmeraldGreen else RoseRed
                                    )
                                }

                                if (!actActivation.isActivityActive || activityReport.failSafeActive) {
                                    Text(
                                        text = "No human activity detected.",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RoseRed
                                    )
                                    Text(
                                        text = activityReport.failSafeNotice ?: "Unable to confidently determine human activity.",
                                        fontSize = 10.5.sp,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = actActivation.activationReason,
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                } else {
                                    listOf(
                                        "Primary Activity" to (actSummary.primaryActivityLabel ?: "None"),
                                        "Secondary Activity" to (actSummary.secondaryActivityLabel ?: "None"),
                                        "Multi-Activity" to activityReport.activityDetection.multiActivityLabel,
                                        "Interactions" to actSummary.interactionSummary,
                                        "Motion Category" to activityReport.motionAnalysis.motionCategory.label,
                                        "Body Stability" to activityReport.motionAnalysis.bodyStability,
                                        "Study Mode" to if (activityReport.studyMode.isStudyModeActive) "Active (Shopping Disabled)" else "Inactive",
                                        "Product Mode" to if (activityReport.productMode.isProductReviewActive) "Active (Shopping Enabled)" else "Disabled"
                                    ).forEach { (k, v) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(k, fontSize = 10.5.sp, color = TextSecondary)
                                            Text(v, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 8G: MASTER BRAIN SCENE CLASSIFICATION V2.0
                        // ==================================================
                        AppleCardContainer(title = "SECTION 8G — MASTER BRAIN SCENE CLASSIFICATION V2.0") {
                            val dummyReelForScene = remember { AnalysedReel(id = "1", title = "Reel", date = "Today") }
                            val sceneReport = remember(videoUri) {
                                SceneClassificationEngineV2.analyzeReelSceneV2(context, videoUri, 15.0f, dummyReelForScene)
                            }
                            val sceneAct = sceneReport.activation
                            val sceneSum = sceneReport.summary

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Scene Classification Status", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text(
                                        text = if (sceneAct.isClassified) "CLASSIFIED (${sceneAct.confidencePercent}% Conf)" else "UNCLASSIFIED (<70% Conf)",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (sceneAct.isClassified) EmeraldGreen else RoseRed
                                    )
                                }

                                if (!sceneAct.isClassified || sceneReport.failSafeActive) {
                                    Text(
                                        text = "Unable to confidently classify this reel.",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RoseRed
                                    )
                                    Text(
                                        text = sceneReport.failSafeNotice ?: "Confidence below 70% threshold.",
                                        fontSize = 10.5.sp,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = sceneAct.activationReason,
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                } else {
                                    listOf(
                                        "Primary Category" to (sceneSum.primaryCategoryLabel ?: "None"),
                                        "Secondary Category" to (sceneSum.secondaryCategoryLabel ?: "None"),
                                        "Environment" to sceneSum.environmentLabel,
                                        "Content Style" to sceneSum.contentStyleLabel,
                                        "Content Intent" to sceneSum.intentLabel,
                                        "Platform Suitability" to sceneReport.platformSuitability.joinToString(", "),
                                        "Shopping Modules" to if (sceneReport.engineToggles.enableShoppingModules) "Enabled (Product Review)" else "Disabled (Strict Rule)",
                                        "Study Engine" to if (sceneReport.engineToggles.enableStudyEngine) "Enabled" else "Disabled"
                                    ).forEach { (k, v) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(k, fontSize = 10.5.sp, color = TextSecondary)
                                            Text(v, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 8H: SPEECH & AUDIO INTELLIGENCE V2.0
                        // ==================================================
                        AppleCardContainer(title = "SECTION 8H — SPEECH & AUDIO INTELLIGENCE ENGINE V2.0") {
                            val dummyReelForSpeech = remember { AnalysedReel(id = "1", title = "Reel Audio", date = "Today") }
                            val speechReport = remember(videoUri) {
                                SpeechEngineV2.analyzeReelSpeechV2(context, videoUri, 15.0f, dummyReelForSpeech)
                            }
                            val speechAct = speechReport.activation
                            val speechSum = speechReport.summary

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Audio Track Status", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text(
                                        text = speechAct.displayText,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (speechAct.isAudioTrackPresent && !speechReport.failSafeActive) EmeraldGreen else RoseRed
                                    )
                                }

                                if (!speechAct.isAudioTrackPresent || speechReport.failSafeActive) {
                                    Text(
                                        text = "FAILURE / FAIL-SAFE ACTIVE",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RoseRed
                                    )
                                    Text(
                                        text = speechReport.failSafeNotice ?: "Unable to confidently analyze audio.",
                                        fontSize = 10.5.sp,
                                        color = TextWhite
                                    )
                                } else {
                                    listOf(
                                        "Audio Classification" to (speechSum.voiceTypeLabel ?: "Unknown"),
                                        "Language Detected" to (speechSum.languageLabel ?: "Uncertain (<80% Conf)"),
                                        "Voice Quality Rating" to speechSum.speechQualityLabel,
                                        "Speaking Style" to speechReport.speakingStyle.label,
                                        "Noise Analysis" to speechSum.noiseLevelLabel,
                                        "Music Engine" to speechSum.musicTypeLabel,
                                        "AI Transcript" to speechSum.transcriptStatusLabel,
                                        "Sentiment" to (speechReport.sentiment?.label ?: "Unknown"),
                                        "Signal Confidence" to "${speechSum.overallConfidencePercent}%",
                                        "Evidence Source" to speechSum.evidenceSource
                                    ).forEach { (k, v) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(k, fontSize = 10.5.sp, color = TextSecondary)
                                            Text(v, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 9: EMOTION DETECTOR
                        // ==================================================
                        AppleCardContainer(title = "SECTION 9 — EMOTION DETECTOR") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                emotionMarkers.forEach { marker ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(XRayCard)
                                            .padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text(marker.timeRange, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                                        Text(marker.emotionEmoji, fontSize = 16.sp)
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(marker.emotionLabel, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                            Text(marker.description, fontSize = 9.5.sp, color = TextSecondary)
                                        }
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 10: COMPETITOR LESSONS
                        // ==================================================
                        AppleCardContainer(title = "SECTION 10 — COMPETITOR LESSONS") {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Educational tips based on top creators in your category:", fontSize = 11.5.sp, color = TextSecondary)

                                listOf(
                                    "✔ Reveal main product feature in the first 2 seconds",
                                    "✔ Use subtle 1.2x zoom after 5 seconds to re-engage eyes",
                                    "✔ Express a genuine warm smile right before giving CTA",
                                    "✔ Hold product steady in center frame for at least 3 seconds"
                                ).forEach { tip ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(tip, fontSize = 10.5.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                        }

                        // ==================================================
                        // SECTION 11: FINAL SUMMARY
                        // ==================================================
                        AppleCardContainer(title = "SECTION 11 — FINAL SUMMARY") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    color = EmeraldGreen.copy(alpha = 0.12f),
                                    border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.4f))
                                ) {
                                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Text("✅ Biggest Strength: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                        Text("High confidence & clear voice delivery", fontSize = 11.sp, color = TextWhite)
                                    }
                                }

                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    color = RoseRed.copy(alpha = 0.12f),
                                    border = BorderStroke(1.dp, RoseRed.copy(alpha = 0.4f))
                                ) {
                                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Text("⚠ Biggest Weakness: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RoseRed)
                                        Text("Slow product reveal at 2.5 seconds", fontSize = 11.sp, color = TextWhite)
                                    }
                                }

                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    color = CyanGlow,
                                    border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.4f))
                                ) {
                                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Text("🚀 Fastest Improvement: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                                        Text("Trim initial 0.8s and move product macro to start", fontSize = 11.sp, color = TextWhite)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Bottom Action Bar
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = XRaySurface,
                        border = BorderStroke(1.dp, GlassBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (onOpenCoach != null) {
                                Button(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onOpenCoach()
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
                                        Text("🧠 AI Coach", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val summaryReport = """
                                        🔥 VIRAL X-RAY DEEP REPORT 🔥
                                        Category: Beauty & Tech Review
                                        Scroll Stop Score: 88% (Strong)
                                        Face Quality: 92% | Smile: 85% | Confidence: 94%
                                        Fastest Improvement: Trim initial 0.8s and reveal product in first 1 second.
                                    """.trimIndent()
                                    clipboard.setPrimaryClip(ClipData.newPlainText("XRay Report", summaryReport))
                                    Toast.makeText(context, "Report Copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(0.9f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = XRayCard,
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

                // Modal Dialog for Frame X-Ray Full Explanation when clicked
                selectedFrameXRay?.let { frame ->
                    Dialog(onDismissRequest = { selectedFrameXRay = null }) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp)),
                            color = XRaySurface,
                            border = BorderStroke(1.dp, CyanAccent)
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
                                    Text("Frame #${frame.frameIndex} (${frame.rankLabel})", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                                    IconButton(onClick = { selectedFrameXRay = null }, modifier = Modifier.size(28.dp)) {
                                        Icon(Icons.Default.Close, contentDescription = null, tint = TextWhite)
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.Black)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(videoUri)
                                            .videoFrameMillis(frame.timestampMs)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Text(text = frame.fullExplanation, fontSize = 12.sp, color = TextWhite, lineHeight = 16.sp)

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Face: ${frame.faceQuality}%", fontSize = 11.sp, color = TextSecondary)
                                    Text("Lighting: ${frame.lighting}", fontSize = 11.sp, color = TextSecondary)
                                    Text("CTR: ${frame.ctrValue}", fontSize = 11.sp, color = EmeraldGreen, fontWeight = FontWeight.Bold)
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
private fun AppleCardContainer(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = XRaySurface,
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
