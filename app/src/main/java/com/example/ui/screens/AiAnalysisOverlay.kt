package com.example.ui.screens

import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.CenterFocusWeak
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.example.ui.components.ViriAction
import com.example.ui.components.ViriMascotWidget
import kotlinx.coroutines.delay

private val ScanBackground = Color(0xFF07090E)
private val CardSurface = Color(0xFF11141D)
private val TileSurface = Color(0xFF191D2A)
private val GlassBorder = Color(0x6622D7E8)
private val CyanGlow = Color(0xFF22D7E8)
private val BrightPurple = Color(0xFFA78BFA)
private val EmeraldGreen = Color(0xFF10B981)
private val AmberYellow = Color(0xFFF59E0B)
private val RoseRed = Color(0xFFF43F5E)
private val TextWhite = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFF94A3B8)

data class DetectionBox(
    val label: String,
    val leftRatio: Float,
    val topRatio: Float,
    val rightRatio: Float,
    val bottomRatio: Float,
    val confidence: Int,
    val isWarning: Boolean = false
)

data class XRayStage(
    val id: Int,
    val name: String,
    val zoomScale: Float,
    val pivotX: Float,
    val pivotY: Float,
    val box: DetectionBox,
    val smartLabel: String,
    val confidence: Int,
    val backgroundStatus: String,
    val viriAction: ViriAction,
    val speech: String,
    val frameMillis: Long
)

data class RankedThumbnail(
    val rank: String,
    val timeMillis: Long,
    val scoreLabel: String,
    val color: Color
)

@OptIn(UnstableApi::class)
@Composable
fun AiAnalysisOverlay(
    config: ProjectSetupConfig?,
    onDismiss: () -> Unit,
    onContinueToOptimization: (ProjectSetupConfig) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current
    val videoUri = config?.selectedMedia?.firstOrNull()?.uri

    var progressPercent by remember { mutableIntStateOf(0) }
    var isScanComplete by remember { mutableStateOf(false) }

    // ExoPlayer for playing uploaded reel silently inside 9:16 phone mockup
    val exoPlayer = remember(context, videoUri) {
        ExoPlayer.Builder(context).build().apply {
            videoUri?.let { uri ->
                setMediaItem(MediaItem.fromUri(uri))
                prepare()
                volume = 0f // Silent playback
                playWhenReady = true
                repeatMode = Player.REPEAT_MODE_ONE
            }
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Sequence of 10 X-Ray Stages
    val stages = remember {
        listOf(
            XRayStage(
                id = 0,
                name = "Full Frame Scan",
                zoomScale = 1.0f,
                pivotX = 0.5f,
                pivotY = 0.5f,
                box = DetectionBox("Background Contour", 0.08f, 0.08f, 0.92f, 0.92f, 94),
                smartLabel = "Lighting Good • Studio Setup",
                confidence = 94,
                backgroundStatus = "Clean Studio Lighting",
                viriAction = ViriAction.POINTING,
                speech = "Starting AI Vision X-Ray...",
                frameMillis = 200L
            ),
            XRayStage(
                id = 1,
                name = "Creator Face",
                zoomScale = 1.75f,
                pivotX = 0.5f,
                pivotY = 0.25f,
                box = DetectionBox("Creator Face", 0.22f, 0.14f, 0.78f, 0.42f, 98),
                smartLabel = "Face Detected 98%",
                confidence = 98,
                backgroundStatus = "Framing Center-Aligned",
                viriAction = ViriAction.CURIOUS,
                speech = "Scanning face expression & focus!",
                frameMillis = 1000L
            ),
            XRayStage(
                id = 2,
                name = "Eye Contact",
                zoomScale = 2.6f,
                pivotX = 0.5f,
                pivotY = 0.22f,
                box = DetectionBox("Eyes Focus Zone", 0.32f, 0.18f, 0.68f, 0.30f, 96),
                smartLabel = "Eye Contact 96%",
                confidence = 96,
                backgroundStatus = "Viewer Engagement Peak",
                viriAction = ViriAction.THINKING,
                speech = "Eye contact triggers +42% watch time!",
                frameMillis = 2000L
            ),
            XRayStage(
                id = 3,
                name = "Voice & Mouth",
                zoomScale = 2.2f,
                pivotX = 0.5f,
                pivotY = 0.34f,
                box = DetectionBox("Mouth / Audio Sync", 0.38f, 0.30f, 0.62f, 0.40f, 95),
                smartLabel = "Voice Clean • Noise Low",
                confidence = 95,
                backgroundStatus = "Acoustics Balanced",
                viriAction = ViriAction.HAPPY,
                speech = "Clear speech detected!",
                frameMillis = 3000L
            ),
            XRayStage(
                id = 4,
                name = "Product Zoom",
                zoomScale = 2.0f,
                pivotX = 0.5f,
                pivotY = 0.58f,
                box = DetectionBox("Product Placement", 0.18f, 0.45f, 0.82f, 0.72f, 89),
                smartLabel = "Product Visible 89%",
                confidence = 89,
                backgroundStatus = "No Object Clutter",
                viriAction = ViriAction.POINTING,
                speech = "Spotting the product focus!",
                frameMillis = 4000L
            ),
            XRayStage(
                id = 5,
                name = "Product Logo",
                zoomScale = 2.8f,
                pivotX = 0.58f,
                pivotY = 0.52f,
                box = DetectionBox("Brand Logo", 0.42f, 0.46f, 0.72f, 0.58f, 93),
                smartLabel = "Logo Visible 93%",
                confidence = 93,
                backgroundStatus = "High Contrast Edge",
                viriAction = ViriAction.CURIOUS,
                speech = "Brand recognition verified!",
                frameMillis = 5000L
            ),
            XRayStage(
                id = 6,
                name = "Price & Offer",
                zoomScale = 2.4f,
                pivotX = 0.70f,
                pivotY = 0.75f,
                box = DetectionBox("Price Overlay", 0.48f, 0.68f, 0.92f, 0.82f, 82, isWarning = true),
                smartLabel = "Price Hidden",
                confidence = 82,
                backgroundStatus = "Opportunity Area",
                viriAction = ViriAction.THINKING,
                speech = "Price tag is missing in frame!",
                frameMillis = 6000L
            ),
            XRayStage(
                id = 7,
                name = "Captions",
                zoomScale = 1.8f,
                pivotX = 0.5f,
                pivotY = 0.85f,
                box = DetectionBox("Subtitle Captions", 0.12f, 0.76f, 0.88f, 0.90f, 100),
                smartLabel = "Hook Strong • Subtitles On",
                confidence = 100,
                backgroundStatus = "High Text Readability",
                viriAction = ViriAction.HAPPY,
                speech = "Captions boost completion rates!",
                frameMillis = 7000L
            ),
            XRayStage(
                id = 8,
                name = "CTA Check",
                zoomScale = 2.1f,
                pivotX = 0.5f,
                pivotY = 0.88f,
                box = DetectionBox("Call To Action", 0.15f, 0.82f, 0.85f, 0.94f, 78, isWarning = true),
                smartLabel = "CTA Missing",
                confidence = 78,
                backgroundStatus = "Weak Ending Signal",
                viriAction = ViriAction.DANCING,
                speech = "Needs a clearer Call To Action!",
                frameMillis = 8500L
            ),
            XRayStage(
                id = 9,
                name = "Full Frame Return",
                zoomScale = 1.0f,
                pivotX = 0.5f,
                pivotY = 0.5f,
                box = DetectionBox("Final Frame X-Ray", 0.05f, 0.05f, 0.95f, 0.95f, 99),
                smartLabel = "X-Ray Vision 100%",
                confidence = 99,
                backgroundStatus = "Scan Verification Done",
                viriAction = ViriAction.CELEBRATING,
                speech = "X-Ray Analysis Complete!",
                frameMillis = 9500L
            )
        )
    }

    var activeStageIndex by remember { mutableIntStateOf(0) }
    val currentStage = stages[activeStageIndex.coerceIn(0, stages.size - 1)]

    // Smooth animated camera zoom scale & pivot point
    val animatedZoomScale by animateFloatAsState(
        targetValue = if (isScanComplete) 1.0f else currentStage.zoomScale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "zoom_scale"
    )
    val animatedPivotX by animateFloatAsState(
        targetValue = if (isScanComplete) 0.5f else currentStage.pivotX,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "pivot_x"
    )
    val animatedPivotY by animateFloatAsState(
        targetValue = if (isScanComplete) 0.5f else currentStage.pivotY,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "pivot_y"
    )

    // Glowing Pulse Animation for Detection Boxes
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_trans")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Moving Scan Line y-progress
    val scanYProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanY"
    )

    // Eye Tracking Particle movement progress
    val particleProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_progress"
    )

    // 10 Extracted Thumbnails with Ranks (Extracted from uploaded reel)
    val rankedThumbnails = remember {
        listOf(
            RankedThumbnail("#1", 3500L, "Best CTR", EmeraldGreen),
            RankedThumbnail("#2", 2000L, "Excellent", CyanGlow),
            RankedThumbnail("#3", 4000L, "Good", BrightPurple),
            RankedThumbnail("#4", 1000L, "Safe", CyanGlow),
            RankedThumbnail("#5", 500L, "Weak", AmberYellow),
            RankedThumbnail("#6", 5000L, "Standard", TextSecondary),
            RankedThumbnail("#7", 6000L, "Standard", TextSecondary),
            RankedThumbnail("#8", 7000L, "Standard", TextSecondary),
            RankedThumbnail("#9", 8000L, "Standard", TextSecondary),
            RankedThumbnail("#10", 9000L, "Standard", TextSecondary)
        )
    }

    // Automated 0..100% Progress Loop
    LaunchedEffect(Unit) {
        Log.d("VIRI_DEBUG", "DS-19: AI Vision X-Ray Engine initialized")
        for (p in 1..100) {
            progressPercent = p
            delay(50) // ~5000ms total scan time

            val targetIdx = (p / 10).coerceIn(0, 9)
            if (targetIdx != activeStageIndex) {
                activeStageIndex = targetIdx
            }
        }

        isScanComplete = true
        delay(700) // Brief finish display moment

        Log.d("VIRI_DEBUG", "DS-19: Scan 100% complete -> Auto opening Final Report")
        config?.let { onContinueToOptimization(it) }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(ScanBackground),
            color = ScanBackground
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(12.dp)
            ) {
                // Background Ambient X-Ray Grid Canvas
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(CyanGlow.copy(alpha = 0.15f), Color.Transparent),
                            center = Offset(size.width * 0.5f, size.height * 0.35f),
                            radius = size.width * 0.8f
                        )
                    )
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 4.dp),
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
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = if (isScanComplete) "X-RAY VERIFIED" else "AI VISION X-RAY ENGINE",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    letterSpacing = 0.8.sp
                                )
                                Text(
                                    text = currentStage.name,
                                    fontSize = 11.sp,
                                    color = CyanGlow,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = CardSurface,
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = EmeraldGreen,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "${currentStage.confidence}%",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldGreen
                                    )
                                }
                            }

                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(CardSurface)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = TextWhite,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // Main Center Stage: Phone Mockup (48% width) + Live Zoom + Apple Vision Boxes + Smart Labels + Eye Tracking
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        // Viri Robot Mascot positioned at Top Edge / Peeking
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-8).dp, y = 4.dp)
                                .zIndex(5f)
                        ) {
                            ViriMascotWidget(
                                action = if (isScanComplete) ViriAction.CELEBRATING else currentStage.viriAction,
                                size = 76.dp
                            )
                        }

                        // Floating Smart Label (Left Side)
                        AnimatedContent(
                            targetState = currentStage.smartLabel,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.85f) togetherWith
                                        fadeOut(animationSpec = tween(200))
                            },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .offset(x = 4.dp, y = (-30).dp)
                                .zIndex(6f),
                            label = "smart_label"
                        ) { labelText ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (currentStage.box.isWarning) RoseRed.copy(alpha = 0.22f) else EmeraldGreen.copy(alpha = 0.22f),
                                border = BorderStroke(
                                    1.dp,
                                    if (currentStage.box.isWarning) RoseRed else EmeraldGreen
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (currentStage.box.isWarning) Icons.Default.Warning else Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (currentStage.box.isWarning) RoseRed else EmeraldGreen,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = labelText,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }
                            }
                        }

                        // Floating Frame Comparison (Best Frame vs Worst Frame Side-by-Side)
                        if (activeStageIndex in 5..8 && !isScanComplete) {
                            Surface(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .offset(x = 4.dp, y = 30.dp)
                                    .width(140.dp)
                                    .zIndex(6f),
                                shape = RoundedCornerShape(14.dp),
                                color = CardSurface.copy(alpha = 0.92f),
                                border = BorderStroke(1.dp, GlassBorder)
                            ) {
                                Column(
                                    modifier = Modifier.padding(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("AI Frame Match", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyanGlow)
                                        Text("Best vs Worst", fontSize = 8.sp, color = TextSecondary)
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        // Worst Frame
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(45.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .border(1.dp, RoseRed, RoundedCornerShape(6.dp))
                                            ) {
                                                AsyncImage(
                                                    model = ImageRequest.Builder(context)
                                                        .data(videoUri)
                                                        .videoFrameMillis(500L)
                                                        .crossfade(true)
                                                        .build(),
                                                    contentDescription = "Worst Frame",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }
                                            Text("Worst Frame", fontSize = 7.5.sp, color = RoseRed, fontWeight = FontWeight.Bold)
                                        }

                                        // Best Frame
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(45.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .border(1.dp, EmeraldGreen, RoundedCornerShape(6.dp))
                                            ) {
                                                AsyncImage(
                                                    model = ImageRequest.Builder(context)
                                                        .data(videoUri)
                                                        .videoFrameMillis(3500L)
                                                        .crossfade(true)
                                                        .build(),
                                                    contentDescription = "Best Frame",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }
                                            Text("Best Frame #1", fontSize = 7.5.sp, color = EmeraldGreen, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        // 9:16 Phone Mockup Container (48% width, centered)
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(0.48f)
                                .aspectRatio(9f / 16f)
                                .graphicsLayer {
                                    shadowElevation = 24f
                                    shape = RoundedCornerShape(26.dp)
                                    clip = true
                                    rotationY = if (isScanComplete) -12f else 0f
                                    cameraDistance = 16f * density.density
                                }
                                .border(
                                    BorderStroke(
                                        1.5.dp,
                                        if (isScanComplete) EmeraldGreen else GlassBorder
                                    ),
                                    RoundedCornerShape(26.dp)
                                )
                                .testTag("phone_mockup_container"),
                            shape = RoundedCornerShape(26.dp),
                            color = Color.Black
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                // Silent Reel Video Player with Dynamic Zooming
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer {
                                            scaleX = animatedZoomScale
                                            scaleY = animatedZoomScale
                                            transformOrigin = androidx.compose.ui.graphics.TransformOrigin(
                                                pivotFractionX = animatedPivotX,
                                                pivotFractionY = animatedPivotY
                                            )
                                        }
                                ) {
                                    if (videoUri != null) {
                                        AndroidView(
                                            factory = { ctx ->
                                                PlayerView(ctx).apply {
                                                    player = exoPlayer
                                                    useController = false
                                                    layoutParams = FrameLayout.LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                                        ViewGroup.LayoutParams.MATCH_PARENT
                                                    )
                                                }
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }

                                // Apple Vision Style Bounding Boxes & Eye Tracking Canvas
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    if (!isScanComplete) {
                                        val box = currentStage.box
                                        val leftPx = size.width * box.leftRatio
                                        val topPx = size.height * box.topRatio
                                        val rightPx = size.width * box.rightRatio
                                        val bottomPx = size.height * box.bottomRatio
                                        val boxW = rightPx - leftPx
                                        val boxH = bottomPx - topPx

                                        val boxColor = if (box.isWarning) RoseRed else CyanGlow

                                        // Glowing Bounding Box Rect
                                        drawRoundRect(
                                            color = boxColor.copy(alpha = 0.25f * pulseAlpha),
                                            topLeft = Offset(leftPx, topPx),
                                            size = Size(boxW, boxH),
                                            cornerRadius = CornerRadius(12.dp.toPx())
                                        )

                                        // Apple Vision Pro Corner Brackets
                                        val bracketLen = 14.dp.toPx()
                                        val strokeW = 2.5.dp.toPx()

                                        // Top-Left Corner
                                        drawLine(boxColor, Offset(leftPx, topPx), Offset(leftPx + bracketLen, topPx), strokeWidth = strokeW, cap = StrokeCap.Round)
                                        drawLine(boxColor, Offset(leftPx, topPx), Offset(leftPx, topPx + bracketLen), strokeWidth = strokeW, cap = StrokeCap.Round)

                                        // Top-Right Corner
                                        drawLine(boxColor, Offset(rightPx, topPx), Offset(rightPx - bracketLen, topPx), strokeWidth = strokeW, cap = StrokeCap.Round)
                                        drawLine(boxColor, Offset(rightPx, topPx), Offset(rightPx, topPx + bracketLen), strokeWidth = strokeW, cap = StrokeCap.Round)

                                        // Bottom-Left Corner
                                        drawLine(boxColor, Offset(leftPx, bottomPx), Offset(leftPx + bracketLen, bottomPx), strokeWidth = strokeW, cap = StrokeCap.Round)
                                        drawLine(boxColor, Offset(leftPx, bottomPx), Offset(leftPx, bottomPx - bracketLen), strokeWidth = strokeW, cap = StrokeCap.Round)

                                        // Bottom-Right Corner
                                        drawLine(boxColor, Offset(rightPx, bottomPx), Offset(rightPx - bracketLen, bottomPx), strokeWidth = strokeW, cap = StrokeCap.Round)
                                        drawLine(boxColor, Offset(rightPx, bottomPx), Offset(rightPx, bottomPx - bracketLen), strokeWidth = strokeW, cap = StrokeCap.Round)

                                        // Viewer Eye Path Animated Trail (Face -> Product -> Caption -> CTA)
                                        val path = Path().apply {
                                            moveTo(size.width * 0.5f, size.height * 0.25f)
                                            lineTo(size.width * 0.5f, size.height * 0.55f)
                                            lineTo(size.width * 0.5f, size.height * 0.82f)
                                        }

                                        drawPath(
                                            path = path,
                                            color = CyanGlow.copy(alpha = 0.5f),
                                            style = Stroke(
                                                width = 1.5.dp.toPx(),
                                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                            )
                                        )

                                        // Moving particle dot on path
                                        val dotY = size.height * (0.25f + 0.57f * particleProgress)
                                        drawCircle(
                                            color = Color.White,
                                            radius = 4.dp.toPx(),
                                            center = Offset(size.width * 0.5f, dotY)
                                        )
                                        drawCircle(
                                            color = CyanGlow.copy(alpha = 0.6f),
                                            radius = 8.dp.toPx(),
                                            center = Offset(size.width * 0.5f, dotY)
                                        )
                                    }

                                    // Crisp Thin Scan Line
                                    if (!isScanComplete) {
                                        val lineY = size.height * scanYProgress
                                        drawLine(
                                            color = Color.White,
                                            start = Offset(0f, lineY),
                                            end = Offset(size.width, lineY),
                                            strokeWidth = 2.dp.toPx()
                                        )
                                        drawRect(
                                            brush = Brush.verticalGradient(
                                                colors = listOf(Color.Transparent, CyanGlow.copy(alpha = 0.3f), Color.Transparent),
                                                startY = lineY - 10.dp.toPx(),
                                                endY = lineY + 10.dp.toPx()
                                            ),
                                            topLeft = Offset(0f, lineY - 10.dp.toPx()),
                                            size = Size(size.width, 20.dp.toPx())
                                        )
                                    }
                                }

                                // Top Camera Notch Pill
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .padding(top = 6.dp)
                                        .width(32.dp)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.Black.copy(alpha = 0.8f))
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // 10 Extracted Real Thumbnails Bar (Extracted directly from uploaded video)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "10 Extracted Reel Thumbnails (Ranked)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Auto AI Selected",
                                fontSize = 10.sp,
                                color = CyanGlow
                            )
                        }

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            itemsIndexed(rankedThumbnails) { _, thumb ->
                                Surface(
                                    modifier = Modifier.width(68.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    color = CardSurface,
                                    border = BorderStroke(1.dp, thumb.color.copy(alpha = 0.6f))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(3.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(50.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color.Black)
                                        ) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(context)
                                                    .data(videoUri)
                                                    .videoFrameMillis(thumb.timeMillis)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = "Thumb ${thumb.rank}",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            Surface(
                                                modifier = Modifier.align(Alignment.TopStart),
                                                shape = RoundedCornerShape(bottomEnd = 6.dp),
                                                color = thumb.color
                                            ) {
                                                Text(
                                                    text = thumb.rank,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = thumb.scoreLabel,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Small Premium Progress Bar at Bottom (No buttons!)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        color = CardSurface,
                        border = BorderStroke(1.dp, Color(0x22FFFFFF))
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = CyanGlow,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = if (isScanComplete) "X-Ray Scan Complete! Opening Report..." else "AI Vision: ${currentStage.name} (${currentStage.backgroundStatus})",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextWhite,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Text(
                                    text = "$progressPercent%",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanGlow
                                )
                            }

                            LinearProgressIndicator(
                                progress = { progressPercent / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = CyanGlow,
                                trackColor = TileSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
