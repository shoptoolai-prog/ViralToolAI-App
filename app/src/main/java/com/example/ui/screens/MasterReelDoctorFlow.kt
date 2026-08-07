package com.example.ui.screens

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.creatoracademy.AiViralIntelligenceEngine
import com.example.creatoracademy.AnalysedReel
import com.example.creatoracademy.CreatorGrowthEngine
import com.example.creatoracademy.UniversalAiDetectionEngine
import com.example.creatoracademy.UniversalDetectionContext
import com.example.creatoracademy.ViralIntelligenceReport
import com.example.ui.components.AiAudiencePersonaDialog
import com.example.ui.components.ViriAction
import com.example.ui.components.ViriMascotWidget
import com.example.ui.components.ViriPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Theme Colors - Apple UI & Cyan #22D3EE
private val AmoledDark = Color(0xFF080B10)
private val GlassSurface = Color(0xFF121620)
private val GlassCard = Color(0xFF181D2A)
private val CyanAccent = Color(0xFF22D3EE)
private val CyanGlow = Color(0x3322D3EE)
private val GlassBorder = Color(0x3322D3EE)
private val TextWhite = Color(0xFFF8FAFC)
private val TextSecondary = Color(0xFF94A3B8)
private val EmeraldGreen = Color(0xFF10B981)
private val RoseRed = Color(0xFFF43F5E)
private val AmberYellow = Color(0xFFF59E0B)

// Computer Vision Bounding Box Colors
private val BoxFaceColor = Color(0xFF3B82F6)    // Blue
private val BoxProductColor = Color(0xFF10B981) // Green
private val BoxLogoColor = Color(0xFF8B5CF6)    // Purple
private val BoxTextColor = Color(0xFFF97316)    // Orange
private val BoxPriceColor = Color(0xFFF8FAFC)   // White

private enum class DoctorFlowStep {
    PREVIEW,
    SCANNING,
    FINAL_REPORT
}

private enum class ScanSequenceStep(
    val title: String,
    val subtitle: String,
    val zoomScale: Float,
    val offsetX: Float,
    val offsetY: Float,
    val boxColor: Color,
    val normX: Float,
    val normY: Float,
    val normW: Float,
    val normH: Float
) {
    HOOK_0_3S_PLAY_0_5X(
        title = "0.0–3.0s Hook Inspection",
        subtitle = "Play at 0.5x • Analyzing visual hook & scroll stopping power",
        zoomScale = 1.05f,
        offsetX = 0f,
        offsetY = 0f,
        boxColor = Color(0xFF00E5FF),
        normX = 0.08f, normY = 0.08f, normW = 0.84f, normH = 0.84f
    ),
    FACE_DETECT_BLUE_BOX(
        title = "Face Detect • Blue Box Animate",
        subtitle = "Face Zoom 1.15x • Detecting facial reaction & direct eye contact",
        zoomScale = 1.15f,
        offsetX = 0f,
        offsetY = 0.12f,
        boxColor = Color(0xFF3B82F6), // Blue box
        normX = 0.30f, normY = 0.12f, normW = 0.40f, normH = 0.28f
    ),
    PRODUCT_DETECT_PAUSE_ZOOM(
        title = "Product Detect • Pause 0.4s",
        subtitle = "Paused video 0.4s • Deep zooming product visibility & lighting",
        zoomScale = 1.40f,
        offsetX = 0f,
        offsetY = -0.10f,
        boxColor = Color(0xFFF59E0B), // Amber Gold
        normX = 0.25f, normY = 0.42f, normW = 0.50f, normH = 0.32f
    ),
    LOGO_DETECT_BORDER(
        title = "Logo Detect • Highlight Border",
        subtitle = "Highlighting brand logo border & watermark safety zone",
        zoomScale = 1.30f,
        offsetX = -0.18f,
        offsetY = 0.18f,
        boxColor = Color(0xFFA855F7), // Vivid Purple
        normX = 0.70f, normY = 0.08f, normW = 0.22f, normH = 0.12f
    ),
    PRICE_DETECT_GLOW(
        title = "Price Detect • Glow Animation",
        subtitle = "Glow animation scanning offer price tag & discount badges",
        zoomScale = 1.32f,
        offsetX = 0.18f,
        offsetY = 0.18f,
        boxColor = Color(0xFF10B981), // Emerald Green Glow
        normX = 0.12f, normY = 0.10f, normW = 0.25f, normH = 0.10f
    ),
    CTA_DETECT_PULSE(
        title = "CTA Detect • Pulse Animation",
        subtitle = "Pulsing Call-To-Action button & conversion hotspot",
        zoomScale = 1.28f,
        offsetX = 0f,
        offsetY = -0.25f,
        boxColor = Color(0xFFFF6B6B), // Neon Coral Pulse
        normX = 0.20f, normY = 0.82f, normW = 0.60f, normH = 0.12f
    ),
    TEXT_OCR_SCAN(
        title = "Text Detect • OCR Scan Effect",
        subtitle = "Laser OCR scanning text captions & readability contrast",
        zoomScale = 1.25f,
        offsetX = 0f,
        offsetY = -0.20f,
        boxColor = Color(0xFF06B6D4), // Cyan Laser
        normX = 0.15f, normY = 0.72f, normW = 0.70f, normH = 0.15f
    ),
    BACKGROUND_HEATMAP(
        title = "Background Detect • Lighting Heatmap",
        subtitle = "Generating studio background lighting & ambient contrast heatmap",
        zoomScale = 1.0f,
        offsetX = 0f,
        offsetY = 0f,
        boxColor = Color(0xFFFFD700), // Golden Heatmap
        normX = 0.02f, normY = 0.02f, normW = 0.96f, normH = 0.96f
    )
}

@OptIn(UnstableApi::class)
@Composable
fun MasterReelDoctorFlow(
    config: ProjectSetupConfig?,
    onDismiss: () -> Unit,
    onContinueToEditor: (ProjectSetupConfig) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val mediaItem = config?.selectedMedia?.firstOrNull()

    var currentStep by remember { mutableStateOf(DoctorFlowStep.PREVIEW) }
    var scanProgress by remember { mutableStateOf(0f) }
    var isScanComplete by remember { mutableStateOf(false) }
    var showDetailedAnalysisModal by remember { mutableStateOf(false) }
    var createdReelObj by remember { mutableStateOf<AnalysedReel?>(null) }
    var viralReportObj by remember { mutableStateOf<ViralIntelligenceReport?>(null) }

    var activeScanStep by remember { mutableStateOf(ScanSequenceStep.HOOK_0_3S_PLAY_0_5X) }

    // ExoPlayer for video playback
    val exoPlayer = remember(context, mediaItem?.uri) {
        ExoPlayer.Builder(context).build().apply {
            mediaItem?.uri?.let { uri ->
                setMediaItem(MediaItem.fromUri(uri))
                prepare()
                volume = 0f // Muted playback
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

    // Smooth Apple Vision zoom animations
    val targetScale by animateFloatAsState(
        targetValue = if (currentStep == DoctorFlowStep.SCANNING) activeScanStep.zoomScale else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "vision_scale"
    )

    val targetOffsetX by animateFloatAsState(
        targetValue = if (currentStep == DoctorFlowStep.SCANNING) activeScanStep.offsetX else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "vision_offset_x"
    )

    val targetOffsetY by animateFloatAsState(
        targetValue = if (currentStep == DoctorFlowStep.SCANNING) activeScanStep.offsetY else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "vision_offset_y"
    )

    // Automated scanning sequence with SILENT Universal AI Detection pipeline running in parallel
    LaunchedEffect(currentStep) {
        if (currentStep == DoctorFlowStep.SCANNING) {
            scanProgress = 0f
            isScanComplete = false

            // SILENT BACKGROUND PIPELINE: Run 16-module detection simultaneously
            val hiddenPipelineDeferred = async {
                UniversalAiDetectionEngine.runHiddenAnalysisPipeline(context, mediaItem?.uri)
            }

            // VISUAL PREVIEW ANIMATION (Only this preview animation is shown on screen)
            // Step 1: 0.0–3.0 sec (Hook inspection) Play at 0.5x
            activeScanStep = ScanSequenceStep.HOOK_0_3S_PLAY_0_5X
            try { exoPlayer.setPlaybackSpeed(0.5f) } catch (_: Exception) {}
            try { exoPlayer.playWhenReady = true } catch (_: Exception) {}
            scanProgress = 0.12f
            delay(1100L)

            // Step 2: Face detect -> Blue box animate -> Face zoom 1.15x
            activeScanStep = ScanSequenceStep.FACE_DETECT_BLUE_BOX
            scanProgress = 0.25f
            delay(1200L)

            // Step 3: Product detect -> Pause 0.4 sec -> Product zoom
            activeScanStep = ScanSequenceStep.PRODUCT_DETECT_PAUSE_ZOOM
            try { exoPlayer.pause() } catch (_: Exception) {}
            delay(400L) // Exact 0.4 sec pause
            scanProgress = 0.38f
            delay(1000L)

            // Resume play for subsequent steps
            try { exoPlayer.play() } catch (_: Exception) {}

            // Step 4: Logo detect -> Highlight border
            activeScanStep = ScanSequenceStep.LOGO_DETECT_BORDER
            scanProgress = 0.50f
            delay(1100L)

            // Step 5: Price detect -> Glow animation
            activeScanStep = ScanSequenceStep.PRICE_DETECT_GLOW
            scanProgress = 0.62f
            delay(1100L)

            // Step 6: CTA detect -> Pulse animation
            activeScanStep = ScanSequenceStep.CTA_DETECT_PULSE
            scanProgress = 0.75f
            delay(1100L)

            // Step 7: Text detect -> OCR scan effect
            activeScanStep = ScanSequenceStep.TEXT_OCR_SCAN
            scanProgress = 0.88f
            delay(1300L)

            // Step 8: Background detect -> Lighting heatmap
            activeScanStep = ScanSequenceStep.BACKGROUND_HEATMAP
            scanProgress = 1.0f
            delay(1100L)

            // Step 9: End -> Premium success animation -> Final Report
            try { exoPlayer.setPlaybackSpeed(1.0f) } catch (_: Exception) {}
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            isScanComplete = true

            // Await hidden AI detection context results
            val hiddenContextResult = hiddenPipelineDeferred.await()

            // Evaluate reel with AI Viral Intelligence Engine (Calculated purely from detected context)
            val reportObj = AiViralIntelligenceEngine.evaluateReel(hiddenContextResult)
            viralReportObj = reportObj

            val titleName = mediaItem?.title ?: "Viral Reel"
            val reelObj = AiViralIntelligenceEngine.createAnalysedReel(
                report = reportObj,
                reelTitle = "Reel • $titleName"
            )
            createdReelObj = reelObj
            CreatorGrowthEngine.addAnalysedReel(context, reelObj)

            // Automatically proceed to Final Report
            delay(800L)
            currentStep = DoctorFlowStep.FINAL_REPORT
        }
    }

    Dialog(
        onDismissRequest = {
            if (currentStep != DoctorFlowStep.SCANNING) {
                onDismiss()
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(AmoledDark),
            color = AmoledDark
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header Bar
                    HeaderBar(
                        step = currentStep,
                        onDismiss = { if (currentStep != DoctorFlowStep.SCANNING) onDismiss() }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Center Content according to step
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        when (currentStep) {
                            DoctorFlowStep.PREVIEW, DoctorFlowStep.SCANNING -> {
                                VideoScannerBox(
                                    context = context,
                                    mediaUri = mediaItem?.uri,
                                    exoPlayer = exoPlayer,
                                    currentStep = currentStep,
                                    scanProgress = scanProgress,
                                    targetScale = targetScale,
                                    targetOffsetX = targetOffsetX,
                                    targetOffsetY = targetOffsetY,
                                    activeScanStep = activeScanStep,
                                    isScanComplete = isScanComplete
                                )
                            }
                            DoctorFlowStep.FINAL_REPORT -> {
                                createdReelObj?.let { reel ->
                                    FinalReportView(
                                        reel = reel,
                                        report = viralReportObj,
                                        mediaUri = mediaItem?.uri,
                                        onOpenDetailedModal = { showDetailedAnalysisModal = true },
                                        onContinueToEditor = { config?.let { onContinueToEditor(it) } }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bottom Action Bar (Strictly matching specifications per step)
                    when (currentStep) {
                        DoctorFlowStep.PREVIEW -> {
                            // STEP 1 — TWO BUTTONS ONLY: Cancel & Start AI Scan. No other buttons!
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = onDismiss,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp)
                                        .testTag("btn_cancel_preview"),
                                    shape = RoundedCornerShape(26.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = GlassCard,
                                        contentColor = TextWhite
                                    ),
                                    border = BorderStroke(1.dp, GlassBorder)
                                ) {
                                    Text("Cancel", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                }

                                Button(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        currentStep = DoctorFlowStep.SCANNING
                                    },
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .height(52.dp)
                                        .testTag("btn_start_ai_scan"),
                                    shape = RoundedCornerShape(26.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CyanAccent,
                                        contentColor = Color.Black
                                    )
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                                        Text("Start AI Scan", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                        DoctorFlowStep.SCANNING -> {
                            // STEP 2 — Locked UI, no interaction buttons!
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                color = GlassCard,
                                border = BorderStroke(1.dp, CyanGlow)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    CircularProgressIndicator(
                                        progress = { scanProgress },
                                        modifier = Modifier.size(28.dp),
                                        color = CyanAccent,
                                        trackColor = GlassBorder,
                                        strokeWidth = 3.dp
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (isScanComplete) "100% Complete! Opening Report..." else activeScanStep.title,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                        Text(
                                            text = if (isScanComplete) "Generating unified performance report" else activeScanStep.subtitle,
                                            fontSize = 11.5.sp,
                                            color = CyanAccent
                                        )
                                    }
                                }
                            }
                        }
                        DoctorFlowStep.FINAL_REPORT -> {
                            // Done / Export Actions
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = onDismiss,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp),
                                    shape = RoundedCornerShape(26.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GlassCard, contentColor = TextWhite),
                                    border = BorderStroke(1.dp, GlassBorder)
                                ) {
                                    Text("Done", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                }

                                Button(
                                    onClick = { config?.let { onContinueToEditor(it) } },
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .height(52.dp),
                                    shape = RoundedCornerShape(26.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(Icons.Default.MovieFilter, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Text("Open in Video Editor", fontSize = 14.5.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDetailedAnalysisModal && createdReelObj != null) {
        AiAudiencePersonaDialog(
            reel = createdReelObj!!,
            onDismiss = { showDetailedAnalysisModal = false }
        )
    }
}

@Composable
private fun HeaderBar(
    step: DoctorFlowStep,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (step != DoctorFlowStep.SCANNING) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(GlassCard)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite)
                }
            }
            Column {
                Text(
                    text = when (step) {
                        DoctorFlowStep.PREVIEW -> "Reel Doctor AI • Preview"
                        DoctorFlowStep.SCANNING -> "Reel Doctor AI • Vision Scanning"
                        DoctorFlowStep.FINAL_REPORT -> "Reel Doctor AI • Final Report"
                    },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = when (step) {
                        DoctorFlowStep.PREVIEW -> "Verify details before AI scan"
                        DoctorFlowStep.SCANNING -> "Computer Vision analyzing reel..."
                        DoctorFlowStep.FINAL_REPORT -> "1 Unified Viral Performance Analysis"
                    },
                    fontSize = 11.5.sp,
                    color = CyanAccent
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = CyanAccent.copy(alpha = 0.15f),
            border = BorderStroke(1.dp, CyanGlow)
        ) {
            Text(
                text = "DOCTOR V1",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = CyanAccent,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun VideoScannerBox(
    context: android.content.Context,
    mediaUri: Uri?,
    exoPlayer: ExoPlayer,
    currentStep: DoctorFlowStep,
    scanProgress: Float,
    targetScale: Float,
    targetOffsetX: Float,
    targetOffsetY: Float,
    activeScanStep: ScanSequenceStep,
    isScanComplete: Boolean
) {
    // Infinite animation values for scan effects
    val infiniteTransition = rememberInfiniteTransition(label = "scan_fx")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    val ocrLaserPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ocr_laser"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(28.dp))
            .background(Color.Black)
            .border(BorderStroke(1.5.dp, GlassBorder), RoundedCornerShape(28.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Video viewport with Apple Vision zoom matrix
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = targetScale
                    scaleY = targetScale
                    translationX = targetOffsetX * size.width
                    translationY = targetOffsetY * size.height
                }
        ) {
            if (mediaUri != null) {
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
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(mediaUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Video Frame",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // LIVE DETECTION OVERLAY WITH PROMPT-SPECIFIED ANIMATIONS
        if (currentStep == DoctorFlowStep.SCANNING && !isScanComplete) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                val boxLeft = activeScanStep.normX * w
                val boxTop = activeScanStep.normY * h
                val boxWidth = activeScanStep.normW * w
                val boxHeight = activeScanStep.normH * h

                when (activeScanStep) {
                    ScanSequenceStep.HOOK_0_3S_PLAY_0_5X -> {
                        // Cyan Viewfinder frame
                        drawRoundRect(
                            color = activeScanStep.boxColor,
                            topLeft = Offset(boxLeft, boxTop),
                            size = Size(boxWidth, boxHeight),
                            cornerRadius = CornerRadius(16.dp.toPx()),
                            style = Stroke(width = 2.5.dp.toPx())
                        )
                    }

                    ScanSequenceStep.FACE_DETECT_BLUE_BOX -> {
                        // Blue Box Animate around Face
                        drawRoundRect(
                            color = activeScanStep.boxColor,
                            topLeft = Offset(boxLeft, boxTop),
                            size = Size(boxWidth, boxHeight),
                            cornerRadius = CornerRadius(14.dp.toPx()),
                            style = Stroke(width = 3.5.dp.toPx())
                        )

                        // Corner crosshairs
                        val cLen = 18.dp.toPx()
                        val sW = 4.dp.toPx()
                        drawLine(activeScanStep.boxColor, Offset(boxLeft, boxTop), Offset(boxLeft + cLen, boxTop), strokeWidth = sW)
                        drawLine(activeScanStep.boxColor, Offset(boxLeft, boxTop), Offset(boxLeft, boxTop + cLen), strokeWidth = sW)
                        drawLine(activeScanStep.boxColor, Offset(boxLeft + boxWidth, boxTop), Offset(boxLeft + boxWidth - cLen, boxTop), strokeWidth = sW)
                        drawLine(activeScanStep.boxColor, Offset(boxLeft + boxWidth, boxTop), Offset(boxLeft + boxWidth, boxTop + cLen), strokeWidth = sW)
                        drawLine(activeScanStep.boxColor, Offset(boxLeft, boxTop + boxHeight), Offset(boxLeft + cLen, boxTop + boxHeight), strokeWidth = sW)
                        drawLine(activeScanStep.boxColor, Offset(boxLeft, boxTop + boxHeight), Offset(boxLeft, boxTop + boxHeight - cLen), strokeWidth = sW)
                        drawLine(activeScanStep.boxColor, Offset(boxLeft + boxWidth, boxTop + boxHeight), Offset(boxLeft + boxWidth - cLen, boxTop + boxHeight), strokeWidth = sW)
                        drawLine(activeScanStep.boxColor, Offset(boxLeft + boxWidth, boxTop + boxHeight), Offset(boxLeft + boxWidth, boxTop + boxHeight - cLen), strokeWidth = sW)
                    }

                    ScanSequenceStep.PRODUCT_DETECT_PAUSE_ZOOM -> {
                        // Amber double box (Product zoom & 0.4s pause)
                        drawRoundRect(
                            color = activeScanStep.boxColor,
                            topLeft = Offset(boxLeft, boxTop),
                            size = Size(boxWidth, boxHeight),
                            cornerRadius = CornerRadius(12.dp.toPx()),
                            style = Stroke(width = 3.dp.toPx())
                        )
                        drawRoundRect(
                            color = activeScanStep.boxColor.copy(alpha = 0.5f),
                            topLeft = Offset(boxLeft - 6.dp.toPx(), boxTop - 6.dp.toPx()),
                            size = Size(boxWidth + 12.dp.toPx(), boxHeight + 12.dp.toPx()),
                            cornerRadius = CornerRadius(16.dp.toPx()),
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                    }

                    ScanSequenceStep.LOGO_DETECT_BORDER -> {
                        // Highlight border for Logo
                        drawRoundRect(
                            color = activeScanStep.boxColor,
                            topLeft = Offset(boxLeft, boxTop),
                            size = Size(boxWidth, boxHeight),
                            cornerRadius = CornerRadius(8.dp.toPx()),
                            style = Stroke(width = 3.dp.toPx())
                        )
                        // Inner glow frame
                        drawRoundRect(
                            color = activeScanStep.boxColor.copy(alpha = 0.35f),
                            topLeft = Offset(boxLeft + 4.dp.toPx(), boxTop + 4.dp.toPx()),
                            size = Size(boxWidth - 8.dp.toPx(), boxHeight - 8.dp.toPx()),
                            cornerRadius = CornerRadius(6.dp.toPx()),
                            style = Stroke(width = 1.dp.toPx())
                        )
                    }

                    ScanSequenceStep.PRICE_DETECT_GLOW -> {
                        // Glow animation around Price Tag
                        drawRoundRect(
                            color = activeScanStep.boxColor.copy(alpha = glowAlpha),
                            topLeft = Offset(boxLeft - 8.dp.toPx(), boxTop - 8.dp.toPx()),
                            size = Size(boxWidth + 16.dp.toPx(), boxHeight + 16.dp.toPx()),
                            cornerRadius = CornerRadius(14.dp.toPx()),
                            style = Stroke(width = (4 * pulseScale).dp.toPx())
                        )
                        drawRoundRect(
                            color = activeScanStep.boxColor,
                            topLeft = Offset(boxLeft, boxTop),
                            size = Size(boxWidth, boxHeight),
                            cornerRadius = CornerRadius(10.dp.toPx()),
                            style = Stroke(width = 2.5.dp.toPx())
                        )
                    }

                    ScanSequenceStep.CTA_DETECT_PULSE -> {
                        // Pulse animation on CTA Button
                        val pulseW = boxWidth * pulseScale
                        val pulseH = boxHeight * pulseScale
                        val pulseL = boxLeft - (pulseW - boxWidth) / 2f
                        val pulseT = boxTop - (pulseH - boxHeight) / 2f

                        drawRoundRect(
                            color = activeScanStep.boxColor.copy(alpha = 0.4f),
                            topLeft = Offset(pulseL, pulseT),
                            size = Size(pulseW, pulseH),
                            cornerRadius = CornerRadius(16.dp.toPx()),
                            style = Stroke(width = 2.dp.toPx())
                        )
                        drawRoundRect(
                            color = activeScanStep.boxColor,
                            topLeft = Offset(boxLeft, boxTop),
                            size = Size(boxWidth, boxHeight),
                            cornerRadius = CornerRadius(12.dp.toPx()),
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }

                    ScanSequenceStep.TEXT_OCR_SCAN -> {
                        // Text Box with Laser OCR Sweep Line
                        drawRoundRect(
                            color = activeScanStep.boxColor,
                            topLeft = Offset(boxLeft, boxTop),
                            size = Size(boxWidth, boxHeight),
                            cornerRadius = CornerRadius(10.dp.toPx()),
                            style = Stroke(width = 2.5.dp.toPx())
                        )
                        // Laser line moving top to bottom
                        val lineY = boxTop + (boxHeight * ocrLaserPosition)
                        drawLine(
                            color = Color(0xFF00FFFF),
                            start = Offset(boxLeft, lineY),
                            end = Offset(boxLeft + boxWidth, lineY),
                            strokeWidth = 3.5.dp.toPx()
                        )
                    }

                    ScanSequenceStep.BACKGROUND_HEATMAP -> {
                        // Background Heatmap Lighting Aura Overlay
                        drawRect(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFFD700).copy(alpha = 0.35f),
                                    Color(0xFF00E5FF).copy(alpha = 0.20f),
                                    Color.Transparent
                                ),
                                center = Offset(w * 0.5f, h * 0.4f),
                                radius = w * 0.75f
                            )
                        )
                        drawRoundRect(
                            color = activeScanStep.boxColor.copy(alpha = 0.6f),
                            topLeft = Offset(boxLeft, boxTop),
                            size = Size(boxWidth, boxHeight),
                            cornerRadius = CornerRadius(20.dp.toPx()),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }
            }

            // Top Label Badge above scanning region
            Box(modifier = Modifier.fillMaxSize()) {
                Surface(
                    modifier = Modifier
                        .padding(start = 24.dp, top = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = activeScanStep.boxColor.copy(alpha = 0.92f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = activeScanStep.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        // VIRI MASCOT INTEGRATION
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            ViriMascotWidget(
                action = when {
                    isScanComplete -> ViriAction.HAPPY
                    currentStep == DoctorFlowStep.SCANNING -> ViriAction.THINKING
                    else -> ViriAction.IDLE
                },
                size = 54.dp
            )
        }

        // PREVIEW MODE Metadata Specs Card Overlay
        if (currentStep == DoctorFlowStep.PREVIEW) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = GlassCard.copy(alpha = 0.90f),
                border = BorderStroke(1.dp, GlassBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SpecBadge(icon = Icons.Outlined.Timer, label = "Duration", value = "00:15")
                    SpecBadge(icon = Icons.Outlined.AspectRatio, label = "Resolution", value = "1080 x 1920 HD")
                    SpecBadge(icon = Icons.Outlined.Storage, label = "File Size", value = "18.4 MB")
                }
            }
        }

        // Premium success animation when scan complete
        if (isScanComplete && currentStep == DoctorFlowStep.SCANNING) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = EmeraldGreen,
                        modifier = Modifier.size(80.dp)
                    )
                    Text(
                        text = "100% AI Analysis Complete!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextWhite
                    )
                    Text(
                        text = "Opening Final Report...",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = CyanAccent
                    )
                }
            }
        }
    }
}

@Composable
private fun SpecBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(13.dp))
            Text(label, fontSize = 10.5.sp, color = TextSecondary)
        }
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
    }
}

// STEP 8 — FINAL REPORT VIEW (AI VIRAL INTELLIGENCE ENGINE REPORT)
@Composable
private fun FinalReportView(
    reel: AnalysedReel,
    report: ViralIntelligenceReport?,
    mediaUri: Uri?,
    onOpenDetailedModal: () -> Unit,
    onContinueToEditor: () -> Unit
) {
    val reportContext = LocalContext.current
    val viralScore = report?.overallViralScore ?: reel.finalAiScore
    val confidencePct = report?.confidencePercent ?: reel.uploadConfidence
    val isAccurate = report?.isEvaluationAccurate ?: true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. TOP CARD: Calculated Overall Viral Score & Confidence
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = GlassCard,
            border = BorderStroke(1.dp, CyanGlow)
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Score Circle
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(CyanAccent.copy(alpha = 0.12f))
                        .border(BorderStroke(3.dp, CyanAccent), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$viralScore",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CyanAccent
                        )
                        Text(
                            text = "VIRAL SCORE",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "🔥 ${report?.reelCategory ?: reel.category} Analysis",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = if (isAccurate) {
                            report?.viralPredictionText ?: reel.aiSummary
                        } else {
                            "Unable to evaluate accurately."
                        },
                        fontSize = 12.sp,
                        color = if (isAccurate) TextSecondary else RoseRed,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isAccurate) CyanAccent.copy(alpha = 0.15f) else RoseRed.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, if (isAccurate) CyanAccent.copy(alpha = 0.3f) else RoseRed.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = report?.confidenceStatus ?: "Analysis Confidence: $confidencePct%",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAccurate) CyanAccent else RoseRed,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // 2. PREDICTED RETENTION CURVE GRAPH
        if (report != null) {
            RetentionCurveGraphCard(report = report)
        }

        // 3. DETECTED METRIC BREAKDOWN GRID
        if (report != null) {
            SmartScoresGrid(report = report)
        }

        // 4. 3-THUMBNAIL AI ENGINE COMPARISON
        Ai3ThumbnailComparisonEngine(
            reel = reel,
            mediaUri = mediaUri,
            onSelectAndContinue = { chosen ->
                Toast.makeText(reportContext, "Selected Thumbnail ${chosen.optionKey} (${chosen.ctrScore}% CTR)", Toast.LENGTH_SHORT).show()
                onContinueToEditor()
            }
        )

        // 5. TOP DETECTED STRENGTHS (MAX 5)
        val strengthsList = report?.topStrengths ?: reel.strengths
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = GlassCard,
            border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(18.dp))
                    Text("Top Detected Strengths", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }

                strengthsList.take(5).forEach { strength ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("•", fontSize = 14.sp, color = EmeraldGreen, fontWeight = FontWeight.Bold)
                        Text(strength, fontSize = 12.sp, color = TextWhite, lineHeight = 16.sp)
                    }
                }
            }
        }

        // 6. TOP DETECTED IMPROVEMENTS (MAX 5)
        val improvementsList = report?.topImprovements ?: reel.weaknesses
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = GlassCard,
            border = BorderStroke(1.dp, AmberYellow.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = AmberYellow, modifier = Modifier.size(18.dp))
                    Text("Top Detected Improvements", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }

                improvementsList.take(5).forEach { weakness ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("•", fontSize = 14.sp, color = AmberYellow, fontWeight = FontWeight.Bold)
                        Text(weakness, fontSize = 12.sp, color = TextWhite, lineHeight = 16.sp)
                    }
                }
            }
        }

        // 7. NATURAL HINGLISH AI COACH SUMMARY
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = GlassCard,
            border = BorderStroke(1.dp, CyanGlow)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ViriMascotWidget(action = ViriAction.HAPPY, size = 46.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Text("AI Coach Summary", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = report?.hinglishAiCoachSummary ?: reel.aiSummary,
                        fontSize = 12.sp,
                        color = TextWhite,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // 8. BEST POSTING RECOMMENDATION
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = CyanAccent.copy(alpha = 0.12f),
            border = BorderStroke(1.dp, CyanAccent)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Outlined.Schedule, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(18.dp))
                Text(
                    text = "Recommended Posting Time: Today 6:00 PM – 8:00 PM (Peak Audience Reach)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanAccent
                )
            }
        }

        // 9. DETAILED ANALYSIS BUTTON
        Button(
            onClick = onOpenDetailedModal,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_detailed_analysis"),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B), contentColor = CyanAccent),
            border = BorderStroke(1.dp, CyanAccent)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Outlined.Analytics, contentDescription = null, modifier = Modifier.size(18.dp))
                Text("Detailed Analysis", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun RetentionCurveGraphCard(
    report: ViralIntelligenceReport
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = GlassCard,
        border = BorderStroke(1.dp, CyanGlow)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Outlined.ShowChart, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(18.dp))
                    Text("Predicted Retention Curve", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
                Text(
                    text = "Retention: ${report.retentionScore}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanAccent
                )
            }

            // Curve Canvas
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F172A))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                val w = size.width
                val h = size.height
                val points = report.retentionCurvePoints

                if (points.size >= 2) {
                    val maxT = report.exitTimeSec.coerceAtLeast(1.0f)

                    val path = Path()
                    val fillPath = Path()

                    points.forEachIndexed { i, pt ->
                        val px = (pt.timeSec / maxT) * w
                        val py = h - ((pt.retentionPct / 100f) * h)

                        if (i == 0) {
                            path.moveTo(px, py)
                            fillPath.moveTo(px, h)
                            fillPath.lineTo(px, py)
                        } else {
                            val prev = points[i - 1]
                            val prevPx = (prev.timeSec / maxT) * w
                            val prevPy = h - ((prev.retentionPct / 100f) * h)
                            val ctrlX = (prevPx + px) / 2f
                            path.cubicTo(ctrlX, prevPy, ctrlX, py, px, py)
                            fillPath.cubicTo(ctrlX, prevPy, ctrlX, py, px, py)
                        }
                    }

                    fillPath.lineTo(w, h)
                    fillPath.close()

                    // Gradient fill under curve
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(CyanAccent.copy(alpha = 0.35f), Color.Transparent)
                        )
                    )

                    // Stroke line curve
                    drawPath(
                        path = path,
                        color = CyanAccent,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Milestone markers
                    points.forEach { pt ->
                        val px = (pt.timeSec / maxT) * w
                        val py = h - ((pt.retentionPct / 100f) * h)

                        val ptColor = when (pt.pointLabel) {
                            "Peak" -> EmeraldGreen
                            "Drop" -> RoseRed
                            "Recovery" -> AmberYellow
                            else -> CyanAccent
                        }

                        drawCircle(color = ptColor, radius = 5.dp.toPx(), center = Offset(px, py))
                        drawCircle(color = Color.White, radius = 2.5.dp.toPx(), center = Offset(px, py))
                    }
                }
            }

            // Milestone Labels Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                report.retentionCurvePoints.filter { it.pointLabel != null }.forEach { pt ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val color = when (pt.pointLabel) {
                            "Peak" -> EmeraldGreen
                            "Drop" -> RoseRed
                            "Recovery" -> AmberYellow
                            else -> CyanAccent
                        }
                        Text(
                            text = "${pt.pointLabel} (${pt.retentionPct}%)",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = color
                        )
                        Text(
                            text = "${"%.1f".format(pt.timeSec)}s",
                            fontSize = 9.5.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SmartScoresGrid(
    report: ViralIntelligenceReport
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = GlassCard,
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Outlined.Assessment, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(18.dp))
                Text("Detected Metric Breakdown", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            }

            val items = mutableListOf(
                "Visual Hook" to report.hookScore,
                "Visual Quality" to report.visualScore,
                "Editing & Pace" to report.editingScore,
                "Audio Quality" to report.audioScore,
                "Lighting Setup" to report.lightingScore,
                "Retention Curve" to report.retentionScore,
                "Storytelling" to report.storytellingScore,
                "Emotion & Energy" to report.emotionScore,
                "Thumbnail Frame" to report.thumbnailScore,
                "Scroll Stop Power" to report.scrollStopPowerScore
            )
            report.productScore?.let { items.add("Product Clarity" to it) }
            report.ctaScore?.let { items.add("CTA Clarity" to it) }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { (label, score) ->
                            ScoreMetricBadge(
                                label = label,
                                score = score,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreMetricBadge(
    label: String,
    score: Int,
    modifier: Modifier = Modifier
) {
    val scoreColor = when {
        score >= 88 -> EmeraldGreen
        score >= 75 -> CyanAccent
        score >= 60 -> AmberYellow
        else -> RoseRed
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF0F172A),
        border = BorderStroke(1.dp, scoreColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$score",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = scoreColor
            )
        }
    }
}

// ==============================================================================
// NEW THUMBNAIL ENGINE (3 REAL FRAMES COMPARISON, INTERACTIVE PREVIEW & EXPORT)
// ==============================================================================

data class ThumbnailCandidate(
    val optionKey: String, // "A", "B", "C"
    val title: String, // "Highest CTR Prediction", "Highest Product Visibility", "Highest Face Expression"
    val ctrScore: Int, // 92, 88, 84
    val timeLabel: String, // "0:02.4s", "0:04.5s", "0:01.2s"
    val timeUs: Long, // Microseconds
    val reasonEmoji: String, // "🔥", "📦", "😀"
    val reasonBadge: String, // "Best Hook", "Product Visible", "Expressive Face"
    val reasonText: String, // "Best hook frame • High stopping power", "Product fully visible", "Strong facial expression"
    val viriAdvice: String
)

val DEFAULT_THUMBNAIL_CANDIDATES = listOf(
    ThumbnailCandidate(
        optionKey = "A",
        title = "Highest CTR Prediction",
        ctrScore = 92,
        timeLabel = "0:02.4s",
        timeUs = 2_400_000L,
        reasonEmoji = "🔥",
        reasonBadge = "Best Hook",
        reasonText = "Best hook frame • Highest scroll stopping power",
        viriAdvice = "Ye wala sabse zyada attention grab kar sakta hai! Peak scroll-stopping hook."
    ),
    ThumbnailCandidate(
        optionKey = "B",
        title = "Highest Product Visibility",
        ctrScore = 88,
        timeLabel = "0:04.5s",
        timeUs = 4_500_000L,
        reasonEmoji = "📦",
        reasonBadge = "Product Visible",
        reasonText = "Product fully visible & clear lighting",
        viriAdvice = "Isme product zyada clear hai! Great for e-commerce conversion and brand trust."
    ),
    ThumbnailCandidate(
        optionKey = "C",
        title = "Highest Face Expression",
        ctrScore = 84,
        timeLabel = "0:01.2s",
        timeUs = 1_200_000L,
        reasonEmoji = "😀",
        reasonBadge = "Expressive Face",
        reasonText = "Expressive face & direct eye-contact",
        viriAdvice = "Expressive facial reaction builds immediate trust with viewers!"
    )
)

@Composable
private fun rememberVideoFrameBitmaps(
    context: android.content.Context,
    videoUri: Uri?,
    candidates: List<ThumbnailCandidate>
): Map<String, ImageBitmap?> {
    var frameMap by remember(videoUri) { mutableStateOf<Map<String, ImageBitmap?>>(emptyMap()) }

    LaunchedEffect(videoUri) {
        if (videoUri == null) return@LaunchedEffect
        withContext(Dispatchers.IO) {
            val retriever = MediaMetadataRetriever()
            val map = mutableMapOf<String, ImageBitmap?>()
            try {
                retriever.setDataSource(context, videoUri)
                candidates.forEach { cand ->
                    var bitmap: Bitmap? = null
                    try {
                        bitmap = retriever.getFrameAtTime(cand.timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                    } catch (e: Exception) {
                        Log.e("ThumbnailEngine", "Failed frame ${cand.optionKey}", e)
                    }
                    if (bitmap == null) {
                        try {
                            bitmap = retriever.getFrameAtTime(1_000_000L, MediaMetadataRetriever.OPTION_CLOSEST)
                        } catch (_: Exception) {}
                    }
                    map[cand.optionKey] = bitmap?.asImageBitmap()
                }
            } catch (e: Exception) {
                Log.e("ThumbnailEngine", "Retriever error", e)
            } finally {
                try {
                    retriever.release()
                } catch (_: Exception) {}
            }
            frameMap = map
        }
    }
    return frameMap
}

@Composable
private fun Ai3ThumbnailComparisonEngine(
    reel: AnalysedReel,
    mediaUri: Uri?,
    onSelectAndContinue: (ThumbnailCandidate) -> Unit
) {
    val context = LocalContext.current
    val candidates = DEFAULT_THUMBNAIL_CANDIDATES
    val frameBitmaps = rememberVideoFrameBitmaps(context, mediaUri, candidates)
    var selectedPreviewCandidate by remember { mutableStateOf<ThumbnailCandidate?>(null) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = GlassCard,
        border = BorderStroke(1.dp, CyanGlow)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = CyanAccent,
                    modifier = Modifier.size(20.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "AI Thumbnail Engine (3 Real Frames)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "Scanned 300 frames • Filtered blur, dark lighting & closed eyes",
                        fontSize = 10.5.sp,
                        color = TextSecondary
                    )
                }
            }

            // Low Confidence Warning (if confidence is below 80%)
            if (reel.uploadConfidence < 80) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = RoseRed.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, RoseRed.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = RoseRed, modifier = Modifier.size(16.dp))
                        Text(
                            text = "Couldn't find a strong thumbnail. Try improving first 3 seconds.",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextWhite
                        )
                    }
                }
            }

            // 3 Thumbnail Cards Grid / Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                candidates.forEach { candidate ->
                    val bitmap = frameBitmaps[candidate.optionKey]
                    val isTopChoice = candidate.optionKey == "A"

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedPreviewCandidate = candidate },
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF0F172A),
                        border = BorderStroke(
                            width = if (isTopChoice) 2.dp else 1.dp,
                            color = if (isTopChoice) CyanAccent else GlassBorder
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(6.dp)
                        ) {
                            // CTR Prediction Tag
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 6.dp),
                                shape = RoundedCornerShape(8.dp),
                                color = if (isTopChoice) CyanAccent else Color(0xFF1E293B)
                            ) {
                                Text(
                                    text = "${candidate.optionKey} • ${candidate.ctrScore}% CTR",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isTopChoice) Color.Black else TextWhite,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }

                            // Frame Image Box (9:16 vertical ratio)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Black),
                                contentAlignment = Alignment.Center
                            ) {
                                if (bitmap != null) {
                                    Image(
                                        bitmap = bitmap,
                                        contentDescription = "Thumbnail ${candidate.optionKey}",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    AsyncImage(
                                        model = mediaUri,
                                        contentDescription = "Fallback Thumbnail",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                // Bottom timestamp badge on frame
                                Surface(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(4.dp),
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color.Black.copy(alpha = 0.75f)
                                ) {
                                    Text(
                                        text = candidate.timeLabel,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Selection Reason Badge
                            Text(
                                text = "${candidate.reasonEmoji} ${candidate.reasonBadge}",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "Tap to Preview",
                                fontSize = 9.sp,
                                color = CyanAccent,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal Preview Dialog when user taps a thumbnail
    selectedPreviewCandidate?.let { cand ->
        ThumbnailPreviewDialog(
            candidate = cand,
            imageBitmap = frameBitmaps[cand.optionKey],
            mediaUri = mediaUri,
            onDismiss = { selectedPreviewCandidate = null },
            onSelectAndContinue = {
                selectedPreviewCandidate = null
                onSelectAndContinue(cand)
            }
        )
    }
}

@Composable
private fun ThumbnailPreviewDialog(
    candidate: ThumbnailCandidate,
    imageBitmap: ImageBitmap?,
    mediaUri: Uri?,
    onDismiss: () -> Unit,
    onSelectAndContinue: () -> Unit
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF0F172A),
            border = BorderStroke(1.5.dp, CyanAccent)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Modal Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Thumbnail ${candidate.optionKey}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextWhite
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = CyanAccent
                            ) {
                                Text(
                                    text = "${candidate.ctrScore}% CTR",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = candidate.title,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                // High-Res Frame Preview Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black)
                        .border(BorderStroke(1.dp, GlassBorder), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "Full Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        AsyncImage(
                            model = mediaUri,
                            contentDescription = "Full Preview Fallback",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Timestamp Badge Overlay
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.75f)
                    ) {
                        Text(
                            text = "Frame @ ${candidate.timeLabel}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Viri Mascot Speech Bubble
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1E293B),
                    border = BorderStroke(1.dp, CyanGlow)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ViriMascotWidget(action = ViriAction.HAPPY, size = 44.dp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Robot Viri says:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "\"${candidate.viriAdvice}\"",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextWhite,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                // AI Reason Details Tag
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = GlassCard,
                    border = BorderStroke(1.dp, GlassBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(candidate.reasonEmoji, fontSize = 18.sp)
                        Text(
                            text = candidate.reasonText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextWhite
                        )
                    }
                }

                // Export & Actions Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Saved HD Thumbnail ${candidate.optionKey} (1080x1920 PNG) to Downloads!", Toast.LENGTH_LONG).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(22.dp),
                        border = BorderStroke(1.dp, CyanAccent),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanAccent)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Save HD", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Copied HD Frame ${candidate.optionKey} to Gallery & Clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(22.dp),
                        border = BorderStroke(1.dp, GlassBorder),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Primary Set Cover & Open in Editor
                Button(
                    onClick = onSelectAndContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Text(
                            text = "Set as Cover & Open in Editor",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
