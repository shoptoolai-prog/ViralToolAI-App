package com.example.creatorassistant.ui

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import android.util.Log
import java.io.File
import com.example.creatorassistant.domain.*
import com.example.creatorassistant.engine.EngineTestResult
import com.example.creatorassistant.viewmodel.AiCreatorAssistantViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiCreatorAssistantScreen(
    onNavigateToHome: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val viewModel: AiCreatorAssistantViewModel = remember {
        AiCreatorAssistantViewModel(
            context = context.applicationContext
        )
    }

    val currentStage = viewModel.currentStage
    val analysis = viewModel.analysisResult
    val understanding = viewModel.understandingResult
    val plan = viewModel.processingPlan
    val processingResult = viewModel.processingResult
    val selectedTargetRatio = viewModel.selectedTargetRatio
    val selectedActions = viewModel.selectedActions
    val isCustomizing = viewModel.isCustomizing
    val saveSuccessMessage = viewModel.saveSuccessMessage
    val errorMessage = viewModel.errorMessage

    var showQuickSetupSheet by remember { mutableStateOf(false) }
    var showRetryDialog by remember { mutableStateOf(false) }
    var showCancelProcessingDialog by remember { mutableStateOf(false) }
    var comparisonMode by remember { mutableStateOf(0) } // 0: Swipe, 1: Side-by-side, 2: Toggle
    var showToggleAfter by remember { mutableStateOf(true) }

    BackHandler(enabled = viewModel.pipelineStatus == PipelineStatus.PROCESSING) {
        showCancelProcessingDialog = true
    }

    if (showCancelProcessingDialog) {
        AlertDialog(
            onDismissRequest = { showCancelProcessingDialog = false },
            title = { Text("Video is still processing", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("AI is currently rendering your video. Please keep ViralToolAI open until processing finishes.", color = TextSecondary, fontSize = 13.sp) },
            confirmButton = {
                TextButton(onClick = { showCancelProcessingDialog = false }) {
                    Text("Keep Processing", color = CyanAccent, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCancelProcessingDialog = false
                        viewModel.cancelProcessing()
                    }
                ) {
                    Text("Cancel", color = CrimsonRed)
                }
            },
            containerColor = CardSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Media Picker
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            viewModel.onVideoSelected(uri)
            showQuickSetupSheet = true
        } else {
            Toast.makeText(context, "No video selected", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(saveSuccessMessage) {
        saveSuccessMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearSaveMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeBg)
            .statusBarsPadding()
    ) {
        // Soft Cyan Radial Background Light
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(CyanAccent.copy(alpha = 0.12f), Color.Transparent),
                    center = Offset(size.width * 0.5f, size.height * 0.15f),
                    radius = size.width * 0.85f
                )
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // ==================================================
            // TOP BAR
            // ==================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onNavigateToHome()
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CardSurface)
                        .border(1.dp, GlassBorder, CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(CyanAccent)
                    )
                    Text(
                        text = "AI CREATOR ASSISTANT",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanAccent,
                        letterSpacing = 1.sp
                    )
                }

                if (currentStage == ProcessingStage.COMPLETED) {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.resetToUpload()
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CardSurface)
                            .border(1.dp, GlassBorder, CircleShape)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "New", tint = TextPrimary)
                    }
                } else {
                    Spacer(modifier = Modifier.size(40.dp))
                }
            }

            // ==================================================
            // MAIN BODY CONTENT
            // ==================================================
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (currentStage) {
                    ProcessingStage.IDLE -> {
                        UploadStateView(
                            onUploadClick = {
                                mediaPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                )
                            }
                        )
                    }

                    ProcessingStage.VIDEO_SELECTED,
                    ProcessingStage.ANALYZING_VIDEO -> {
                        if (analysis != null) {
                            InspectedVideoPreviewView(
                                analysis = analysis,
                                understanding = understanding,
                                plan = plan,
                                onConfigureClick = { showQuickSetupSheet = true },
                                onReuploadClick = {
                                    mediaPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                    )
                                }
                            )
                        } else {
                            LoadingSpinnerView(label = "Inspecting video format...")
                        }
                    }

                    ProcessingStage.UPLOAD_VALIDATE_VIDEO,
                    ProcessingStage.ANALYZE_METADATA_AUDIO,
                    ProcessingStage.DETECT_PRIMARY_SUBJECT,
                    ProcessingStage.TRACK_SUBJECT_MOVEMENT,
                    ProcessingStage.ANALYZE_COMPOSITION_MARGINS,
                    ProcessingStage.ANALYZE_REQUESTED_RATIO,
                    ProcessingStage.BUILD_SMART_REFRAME_PATH,
                    ProcessingStage.APPLY_AUTO_REFRAME_CROP,
                    ProcessingStage.APPLY_VIDEO_STABILIZATION,
                    ProcessingStage.CLEAN_BACKGROUND_NOISE,
                    ProcessingStage.ENHANCE_VOICE_DIALOGUE,
                    ProcessingStage.BALANCE_AUDIO_LEVELS,
                    ProcessingStage.RENDER_TARGET_RATIO,
                    ProcessingStage.VERIFY_FACE_SUBJECT_VISIBILITY,
                    ProcessingStage.VERIFY_AUDIO_VIDEO_SYNC,
                    ProcessingStage.VERIFY_OUTPUT_PLAYBACK,
                    ProcessingStage.GENERATE_THUMBNAILS,
                    ProcessingStage.FINALIZE_OUTPUT,
                    ProcessingStage.READY_FOR_PREVIEW,
                    ProcessingStage.ANALYZING_VIDEO,
                    ProcessingStage.DETECTING_SUBJECTS,
                    ProcessingStage.UNDERSTANDING_COMPOSITION,
                    ProcessingStage.PROCESSING_VIDEO,
                    ProcessingStage.OPTIMIZING_AUDIO,
                    ProcessingStage.APPLYING_REQUESTED_RATIO,
                    ProcessingStage.QUALITY_CHECK,
                    ProcessingStage.RETRYING -> {
                        ProcessingProgressView(
                            currentStage = currentStage,
                            progressPercent = viewModel.progressPercent,
                            sourceRatioLabel = analysis?.orientationLabel ?: "9:16",
                            targetRatioLabel = viewModel.selectedTargetRatio.label,
                            sourceAspectRatio = analysis?.originalAspectRatio ?: 0f,
                            videoUri = analysis?.videoUri,
                            onCancelClick = { viewModel.cancelProcessing() }
                        )
                    }

                    ProcessingStage.COMPLETED -> {
                        if (processingResult != null && analysis != null) {
                            ResultComparisonView(
                                analysis = analysis,
                                result = processingResult,
                                comparisonMode = comparisonMode,
                                onModeChange = { comparisonMode = it },
                                showToggleAfter = showToggleAfter,
                                onToggleAfterChange = { showToggleAfter = it },
                                onRetryClick = { showRetryDialog = true },
                                onDownloadOriginal = { viewModel.downloadOriginal() },
                                onDownloadAiVersion = { viewModel.downloadAiVersion() },
                                isSaving = viewModel.isSavingVideo
                            )
                        }
                    }

                    ProcessingStage.FAILED -> {
                        ErrorStateView(
                            message = errorMessage ?: "Unable to complete processing for the requested ratio.",
                            onRetry = { viewModel.startAiProcessing() },
                            onChangeRatio = { showQuickSetupSheet = true },
                            onCancel = { viewModel.resetToUpload() }
                        )
                    }

                    else -> {}
                }
            }
        }

        // ==================================================
        // AI QUICK SETUP BOTTOM SHEET
        // ==================================================
        if (showQuickSetupSheet && analysis != null) {
            ModalBottomSheet(
                onDismissRequest = { showQuickSetupSheet = false },
                containerColor = Color(0xFF141822),
                scrimColor = Color.Black.copy(alpha = 0.75f),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                QuickSetupSheetContent(
                    analysis = analysis,
                    understanding = understanding,
                    plan = plan,
                    selectedTargetRatio = selectedTargetRatio,
                    selectedActions = selectedActions,
                    isCustomizing = isCustomizing,
                    onSelectRatio = { viewModel.selectTargetRatio(it) },
                    onToggleAction = { viewModel.toggleAction(it) },
                    onAutoSelect = { viewModel.applyAutoRecommended() },
                    onToggleCustomize = { viewModel.setCustomizingMode(it) },
                    onStartProcessing = {
                        showQuickSetupSheet = false
                        viewModel.startAiProcessing()
                    }
                )
            }
        }

        // ==================================================
        // RETRY APPROACH DIALOG
        // ==================================================
        if (showRetryDialog) {
            AlertDialog(
                onDismissRequest = { showRetryDialog = false },
                containerColor = Color(0xFF1B202E),
                title = {
                    Text("Retry AI Processing", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Choose an AI adjustment approach for re-processing:", color = TextSecondary, fontSize = 13.sp)

                        val approaches = listOf(
                            RetryApproach.DEFAULT,
                            RetryApproach.FOCUS_FACE,
                            RetryApproach.PRESERVE_BACKGROUND,
                            RetryApproach.AGGRESSIVE_AUDIO,
                            RetryApproach.NATURAL_ENHANCE
                        )

                        approaches.forEach { appr ->
                            val isSelected = viewModel.selectedRetryApproach == appr
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) CyanAccent.copy(alpha = 0.15f) else Color(0xFF121620),
                                border = BorderStroke(1.dp, if (isSelected) CyanAccent else GlassBorder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.retryWithApproach(appr)
                                        showRetryDialog = false
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = null,
                                        colors = RadioButtonDefaults.colors(selectedColor = CyanAccent)
                                    )
                                    Column {
                                        Text(appr.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(appr.description, color = TextSecondary, fontSize = 11.5.sp)
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showRetryDialog = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            )
        }
    }
}

// ============================================================================
// UPLOAD STATE VIEW (Phase 2)
// ============================================================================
@Composable
private fun UploadStateView(
    onUploadClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Glowing Icon Header
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(colors = listOf(CyanAccent.copy(alpha = 0.25f), Color.Transparent)))
                .border(1.5.dp, CyanAccent.copy(alpha = 0.8f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = CyanAccent,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "AI Creator Assistant",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Turn any video into a ready-to-post reel.",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = CyanAccent,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "AI automatically adapts, enhances and prepares your video for social platforms.",
            fontSize = 13.5.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Upload Button CTA
        Button(
            onClick = onUploadClick,
            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(52.dp)
                .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = CyanAccent)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(20.dp))
                Text("Upload Video", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("• Gallery", fontSize = 12.sp, color = TextSecondary)
            Text("• File Picker", fontSize = 12.sp, color = TextSecondary)
            Text("• Video Capture", fontSize = 12.sp, color = TextSecondary)
        }
    }
}

// ============================================================================
// INSPECTED VIDEO PREVIEW VIEW (Preserves Original Aspect Ratio Dynamic Container)
// ============================================================================
@Composable
private fun InspectedVideoPreviewView(
    analysis: VideoAnalysisResult,
    understanding: VideoUnderstandingResult?,
    plan: ProcessingPlan?,
    onConfigureClick: () -> Unit,
    onReuploadClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Video Ready for AI", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(
                "Preview & AI Configuration",
                fontSize = 12.5.sp,
                color = CyanAccent
            )
        }

        // Aspect Ratio Preserving Preview
        val aspect = analysis.originalAspectRatio.coerceIn(0.45f, 2.2f)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 260.dp)
                .aspectRatio(aspect)
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, CyanAccent.copy(alpha = 0.6f), RoundedCornerShape(20.dp)),
            color = Color.Black
        ) {
            SimpleExoPlayerView(videoUri = analysis.videoUri)
        }

        // Basic Video Info Summary Card
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = CardSurface,
            border = BorderStroke(1.dp, GlassBorder),
            modifier = Modifier.fillMaxWidth()
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
                    Text(
                        "VIDEO INFORMATION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanAccent,
                        letterSpacing = 1.sp
                    )
                    Text(
                        "Original Format",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                val und = understanding
                val bullets = listOf(
                    "Resolution" to "${analysis.resolutionLabel} (${analysis.orientationLabel})",
                    "Duration" to "${analysis.durationMs / 1000}s",
                    "Subject" to if (und != null) "${und.subjectType}" else "Subject detected",
                    "Audio" to if (und != null) (if (und.hasSpeech) "Speech detected" else "Ambient audio") else "Audio track present",
                    "Motion" to if (und != null) "Camera ${und.cameraShakeLevel.lowercase().replace('_', ' ')}" else "Stable",
                    "Exposure" to if (und != null) "${(und.brightnessScore * 100).toInt()}% brightness" else "Balanced"
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    bullets.chunked(2).forEach { rowPair ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            rowPair.forEach { (label, value) ->
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(label, fontSize = 10.5.sp, color = TextSecondary)
                                    Text(value, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                }
                            }
                            if (rowPair.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onReuploadClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                border = BorderStroke(1.dp, GlassBorder),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.weight(1f).height(48.dp)
            ) {
                Text("Change Video", fontSize = 13.sp)
            }

            Button(
                onClick = onConfigureClick,
                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.weight(1.3f).height(48.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Text("Prepare with AI", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

// ============================================================================
// QUICK SETUP SHEET CONTENT
// ============================================================================
@Composable
private fun QuickSetupSheetContent(
    analysis: VideoAnalysisResult,
    understanding: VideoUnderstandingResult?,
    plan: ProcessingPlan?,
    selectedTargetRatio: TargetRatio,
    selectedActions: Set<AiActionType>,
    isCustomizing: Boolean,
    onSelectRatio: (TargetRatio) -> Unit,
    onToggleAction: (AiActionType) -> Unit,
    onAutoSelect: () -> Unit,
    onToggleCustomize: (Boolean) -> Unit,
    onStartProcessing: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "What should AI fix?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Select aspect ratio & AI transformations",
                    fontSize = 12.5.sp,
                    color = TextSecondary
                )
            }

            // AI Auto Fix Everything Button
            Button(
                onClick = {
                    onToggleCustomize(false)
                    onAutoSelect()
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent.copy(alpha = 0.2f), contentColor = CyanAccent),
                border = BorderStroke(1.dp, CyanAccent),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                    Text("AI Auto Fix", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Target Ratio Selector
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("TARGET ASPECT RATIO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanAccent, letterSpacing = 1.sp)

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                val ratios = listOf(
                    TargetRatio.REELS_9_16,
                    TargetRatio.YOUTUBE_16_9,
                    TargetRatio.SQUARE_1_1,
                    TargetRatio.FEED_4_5,
                    TargetRatio.PORTRAIT_3_4,
                    TargetRatio.LANDSCAPE_4_3,
                    TargetRatio.ORIGINAL
                )
                items(ratios) { ratio ->
                    val isSel = selectedTargetRatio == ratio
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSel) CyanAccent.copy(alpha = 0.18f) else CardSurface,
                        border = BorderStroke(1.5.dp, if (isSel) CyanAccent else GlassBorder),
                        modifier = Modifier.clickable { onSelectRatio(ratio) }
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(ratio.label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isSel) CyanAccent else TextPrimary)
                            Text(ratio.description.take(16), fontSize = 10.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }

        // YOUR AI PLAN PREVIEW CARD
        if (plan != null) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = CardSurface,
                border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "YOUR AI PLAN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent,
                            letterSpacing = 1.sp
                        )
                        Text(
                            "Est. Time: ~${plan.estimatedProcessingTimeSec} sec",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                    }

                    val recommendedDecisions = plan.allDecisions.filter { selectedActions.contains(it.operation) }
                    if (recommendedDecisions.isEmpty()) {
                        Text(
                            "No AI operations selected. Original video will be passed through cleanly.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    } else {
                        recommendedDecisions.forEachIndexed { idx, dec ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    "${idx + 1}.",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanAccent
                                )
                                Column {
                                    Text(
                                        dec.operation.title,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        dec.reason,
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // All AI Actions Toggle List
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("AI ENHANCEMENT ACTIONS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanAccent, letterSpacing = 1.sp)
                Text("${selectedActions.size} / ${AiActionType.values().size} Selected", fontSize = 11.sp, color = TextSecondary)
            }

            AiActionType.values().forEach { action ->
                val isChecked = selectedActions.contains(action)
                val decision = plan?.allDecisions?.find { it.operation == action }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CardSurface,
                    border = BorderStroke(1.dp, if (isChecked) CyanAccent.copy(alpha = 0.4f) else GlassBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleAction(action) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(action.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                if (decision?.recommended == true) {
                                    Text("• Recommended", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                                }
                            }
                            Text(
                                decision?.reason ?: action.description,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { onToggleAction(action) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = CyanAccent,
                                checkmarkColor = Color.Black
                            )
                        )
                    }
                }
            }
        }

        Button(
            onClick = onStartProcessing,
            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                Text("Generate with AI", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ============================================================================
// ============================================================================
// PROCESSING PROGRESS VIEW (CapCut-Style Compact View with White/Cyan Glow Progress Border)
// ============================================================================
private fun parseAspectRatio(label: String): Float {
    return when {
        label.contains("9:16") || label.contains("9x16") || label.contains("Shorts") || label.contains("Reels") || label.contains("TikTok") -> 9f / 16f
        label.contains("16:9") || label.contains("16x9") || label.contains("YouTube") || label.contains("Landscape") -> 16f / 9f
        label.contains("1:1") || label.contains("1x1") || label.contains("Square") -> 1f / 1f
        label.contains("4:5") || label.contains("4x5") || label.contains("Feed") -> 4f / 5f
        label.contains("3:4") || label.contains("3x4") -> 3f / 4f
        label.contains("4:3") || label.contains("4x3") -> 4f / 3f
        else -> 9f / 16f
    }
}

@Composable
private fun ThumbnailWithProgressBorder(
    videoUri: Uri?,
    progressPercent: Int,
    sourceRatioLabel: String,
    targetRatioLabel: String,
    sourceAspectRatio: Float,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var thumbnailBitmap by remember(videoUri) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(videoUri) {
        if (videoUri == null) return@LaunchedEffect
        withContext(Dispatchers.IO) {
            try {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(context, videoUri)
                val durStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                val durUs = ((durStr?.toLongOrNull() ?: 5000L) * 1000L).coerceAtLeast(1000000L)

                val candidateTimes = listOf(
                    1000000L,
                    (durUs * 0.25).toLong(),
                    (durUs * 0.50).toLong(),
                    (durUs * 0.75).toLong(),
                    0L
                )

                var chosenBitmap: android.graphics.Bitmap? = null
                for (timeUs in candidateTimes) {
                    val frame = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                    if (frame != null) {
                        if (chosenBitmap == null) chosenBitmap = frame
                        val w = frame.width
                        val h = frame.height
                        val stepX = (w / 10).coerceAtLeast(1)
                        val stepY = (h / 10).coerceAtLeast(1)
                        var totalLum = 0L
                        var samples = 0
                        var bright = 0
                        for (x in 0 until w step stepX) {
                            for (y in 0 until h step stepY) {
                                val p = frame.getPixel(x, y)
                                val r = (p shr 16) and 0xFF
                                val g = (p shr 8) and 0xFF
                                val b = p and 0xFF
                                val lum = (299 * r + 587 * g + 114 * b) / 1000
                                totalLum += lum
                                if (lum > 20) bright++
                                samples++
                            }
                        }
                        if (samples > 0 && (totalLum / samples >= 12 || (bright.toFloat() / samples) >= 0.04f)) {
                            chosenBitmap = frame
                            break
                        }
                    }
                }
                retriever.release()
                if (chosenBitmap != null) {
                    thumbnailBitmap = chosenBitmap.asImageBitmap()
                }
            } catch (e: Exception) {
                // Fallback handled in UI
            }
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = (progressPercent / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 200, easing = LinearOutSlowInEasing),
        label = "BorderProgressAnimation"
    )

    val frameRatio = remember(sourceAspectRatio, sourceRatioLabel) {
        if (sourceAspectRatio > 0.1f) sourceAspectRatio else parseAspectRatio(sourceRatioLabel)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 320.dp, min = 180.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(frameRatio)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black)
                .drawWithContent {
                    drawContent() // Draw thumbnail image

                    val cornerRadiusPx = 20.dp.toPx()
                    val strokeWidthPx = 3.dp.toPx()
                    val w = size.width
                    val h = size.height
                    val r = cornerRadiusPx

                    // Path starting at TOP-LEFT corner (r, 0f) and traveling clockwise around perimeter back to (r, 0f)
                    val fullPath = Path().apply {
                        moveTo(r, 0f)
                        lineTo(w - r, 0f)
                        arcTo(Rect(w - 2 * r, 0f, w, 2 * r), -90f, 90f, false) // Top-right corner
                        lineTo(w, h - r)
                        arcTo(Rect(w - 2 * r, h - 2 * r, w, h), 0f, 90f, false) // Bottom-right corner
                        lineTo(r, h)
                        arcTo(Rect(0f, h - 2 * r, 2 * r, h), 90f, 90f, false) // Bottom-left corner
                        lineTo(0f, r)
                        arcTo(Rect(0f, 0f, 2 * r, 2 * r), 180f, 90f, false) // Top-left corner
                    }

                    // Subtle background track
                    drawPath(
                        path = fullPath,
                        color = Color.White.copy(alpha = 0.18f),
                        style = Stroke(width = 1.5.dp.toPx())
                    )

                    // Glowing white and cyan progress stroke around perimeter
                    if (animatedProgress > 0f) {
                        val pathMeasure = PathMeasure()
                        pathMeasure.setPath(fullPath, false)
                        val totalLength = pathMeasure.length
                        val drawLength = totalLength * animatedProgress

                        val progressPath = Path()
                        pathMeasure.getSegment(0f, drawLength, progressPath, true)

                        // Cyan glow aura
                        drawPath(
                            path = progressPath,
                            color = CyanAccent.copy(alpha = 0.6f),
                            style = Stroke(width = strokeWidthPx * 1.8f, cap = StrokeCap.Round)
                        )
                        // Core bright white line
                        drawPath(
                            path = progressPath,
                            color = Color.White,
                            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                        )
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            val currentBitmap = thumbnailBitmap
            if (currentBitmap != null) {
                Image(
                    bitmap = currentBitmap,
                    contentDescription = "Uploaded video preview frame",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (videoUri != null) {
                SimpleExoPlayerView(videoUri = videoUri)
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF121822)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Movie,
                        contentDescription = null,
                        tint = TextSecondary.copy(alpha = 0.4f),
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            // Percentage Overlay INSIDE the preview frame
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.65f), Color.Transparent),
                            radius = 350f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$progressPercent%",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CyanAccent
                )
            }

            // Pill Label INSIDE the frame at bottom
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.85f),
                border = BorderStroke(1.dp, GlassBorder),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(CyanAccent))
                    Text(
                        text = "SOURCE: $sourceRatioLabel • TARGET: $targetRatioLabel",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun ProcessingProgressView(
    currentStage: ProcessingStage,
    progressPercent: Int,
    sourceRatioLabel: String,
    targetRatioLabel: String,
    sourceAspectRatio: Float,
    videoUri: Uri?,
    onCancelClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "AI CREATOR ASSISTANT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanAccent,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Processing with AI",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            }

            // Thumbnail frame with dynamic ratio & glowing border progress
            ThumbnailWithProgressBorder(
                videoUri = videoUri,
                progressPercent = progressPercent,
                sourceRatioLabel = sourceRatioLabel,
                targetRatioLabel = targetRatioLabel,
                sourceAspectRatio = sourceAspectRatio
            )

            // Single Status Message below frame
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (progressPercent >= 100) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = CyanAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Processing Complete!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    } else {
                        val infiniteTransition = rememberInfiniteTransition(label = "Pulse")
                        val pulseAlpha by infiniteTransition.animateFloat(
                            initialValue = 0.4f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(600, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "Alpha"
                        )
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(CyanAccent.copy(alpha = pulseAlpha))
                        )
                        Text(
                            text = currentStage.activeDescription,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Text(
                    text = "AI is preparing your final video. Please keep ViralToolAI open.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Closing the app may interrupt processing.",
                    fontSize = 10.5.sp,
                    color = TextSecondary.copy(alpha = 0.65f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Non-intrusive Cancel Button
            TextButton(
                onClick = onCancelClick,
                shape = CircleShape
            ) {
                Text(
                    text = "Cancel Processing",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

// ============================================================================
// RESULT COMPARISON VIEW (Clean Video-Focused Result Screen)
// ============================================================================
@Composable
private fun ResultComparisonView(
    analysis: VideoAnalysisResult,
    result: AiProcessingResult,
    comparisonMode: Int,
    onModeChange: (Int) -> Unit,
    showToggleAfter: Boolean,
    onToggleAfterChange: (Boolean) -> Unit,
    onRetryClick: () -> Unit,
    onDownloadOriginal: () -> Unit,
    onDownloadAiVersion: () -> Unit,
    isSaving: Boolean
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Minimal Top Title
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text(
                text = "AI Result",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,
                letterSpacing = (-0.5).sp
            )
        }

        // Comparison Mode Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .background(CardSurface)
                .padding(3.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val modes = listOf("Swipe", "Side-by-Side", "Toggle")
            modes.forEachIndexed { idx, title ->
                val isSel = comparisonMode == idx
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSel) Color.Black else TextSecondary,
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .background(if (isSel) CyanAccent else Color.Transparent)
                        .clickable { onModeChange(idx) }
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Interactive Video Preview Frame
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.Black)
                .border(1.dp, GlassBorder, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            when (comparisonMode) {
                0 -> { // Swipe Slider
                    var dragOffsetRatio by remember { mutableStateOf(0.5f) }
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Under layer: Original (BEFORE)
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            SimpleExoPlayerView(videoUri = analysis.videoUri)
                            Text(
                                "BEFORE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        // Top layer: AI Version (AFTER)
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(dragOffsetRatio)
                                .clip(RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            AfterExoPlayerView(
                                generatedUri = result.processedVideoUri,
                                showControls = false,
                                onRetry = onRetryClick
                            )
                            Text(
                                "AFTER",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(8.dp)
                                    .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        // Draggable Divider Bar
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(3.dp)
                                .graphicsLayer { translationX = dragOffsetRatio * 320.dp.toPx() }
                                .background(CyanAccent)
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        dragOffsetRatio = (dragOffsetRatio + dragAmount.x / size.width).coerceIn(0.1f, 0.9f)
                                    }
                                }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .align(Alignment.Center)
                                    .clip(CircleShape)
                                    .background(CyanAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Code, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
                1 -> { // Side-by-Side
                    Row(
                        modifier = Modifier.fillMaxSize().padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF0D0D11)),
                            contentAlignment = Alignment.Center
                        ) {
                            SimpleExoPlayerView(videoUri = analysis.videoUri)
                            Text(
                                "BEFORE",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(6.dp)
                                    .background(Color.Black.copy(alpha = 0.75f), CircleShape)
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF0D0D11)),
                            contentAlignment = Alignment.Center
                        ) {
                            AfterExoPlayerView(
                                generatedUri = result.processedVideoUri,
                                showControls = false,
                                onRetry = onRetryClick
                            )
                            Text(
                                "AFTER",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(6.dp)
                                    .background(Color.Black.copy(alpha = 0.75f), CircleShape)
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
                2 -> { // Toggle Button (Default AFTER)
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (showToggleAfter) {
                            AfterExoPlayerView(
                                generatedUri = result.processedVideoUri,
                                showControls = true,
                                onRetry = onRetryClick
                            )
                        } else {
                            SimpleExoPlayerView(
                                videoUri = analysis.videoUri,
                                showControls = false
                            )
                        }
                        
                        Surface(
                            onClick = { onToggleAfterChange(!showToggleAfter) },
                            shape = CircleShape,
                            color = if (showToggleAfter) CyanAccent else Color(0xFF22222E),
                            border = BorderStroke(1.dp, GlassBorder),
                            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = if (showToggleAfter) Color.Black else TextPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    if (showToggleAfter) "Showing AFTER" else "Showing BEFORE",
                                    fontSize = 12.sp,
                                    color = if (showToggleAfter) Color.Black else TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Action Buttons: Download Original, Download AI Version, Share
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = onDownloadOriginal,
                    enabled = !isSaving,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    border = BorderStroke(1.dp, GlassBorder),
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("Download Original", fontSize = 12.sp)
                }

                Button(
                    onClick = onDownloadAiVersion,
                    enabled = !isSaving,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                    shape = CircleShape,
                    modifier = Modifier.weight(1.2f).height(48.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Text("Download AI Version", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            OutlinedButton(
                onClick = {
                    val shareUri = result.processedVideoUri ?: analysis.videoUri
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "video/*"
                        putExtra(Intent.EXTRA_STREAM, shareUri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share AI Processed Video"))
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanAccent),
                border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.5f)),
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().height(44.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Text("Share AI Video", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ============================================================================
// COMPONENT HELPERS
// ============================================================================
@Composable
private fun ScoreCard(title: String, score: Int, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = CardSurface,
        border = BorderStroke(1.dp, GlassBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 10.sp, color = TextSecondary)
            Text("$score", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
        }
    }
}

@Composable
private fun SimpleExoPlayerView(videoUri: Uri, showControls: Boolean = false) {
    val context = LocalContext.current
    var playbackError by remember(videoUri) { mutableStateOf<String?>(null) }

    val exoPlayer = remember(videoUri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    Log.e("VideoPlayer", "BEFORE playback error for $videoUri: ${error.message}", error)
                    playbackError = error.localizedMessage ?: "Playback error"
                }
            })
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    if (playbackError != null) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFF0D0D14)).padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Unable to play video: $playbackError",
                color = TextSecondary,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    } else {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = showControls
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                }
            },
            update = { view ->
                view.player = exoPlayer
                view.useController = showControls
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun AfterExoPlayerView(
    generatedUri: Uri?,
    showControls: Boolean = false,
    onRetry: () -> Unit = {}
) {
    val context = LocalContext.current
    var validationError by remember(generatedUri) { mutableStateOf<String?>(null) }
    var playbackError by remember(generatedUri) { mutableStateOf<String?>(null) }

    // Validate actual generated output file and decodability
    val validUri = remember(generatedUri) {
        if (generatedUri == null) {
            validationError = "AI video is still being prepared..."
            null
        } else {
            try {
                val path = generatedUri.path
                val file = if (path != null) File(path) else null
                val exists = file?.exists() == true
                val size = if (exists) file!!.length() else -1L

                Log.d("VideoPlayer", "AI_OUTPUT_URI = $generatedUri, exists = $exists, size = $size")

                if (file != null && (!exists || size <= 0L)) {
                    validationError = "AI video is still being prepared..."
                    null
                } else {
                    // Check if file is decodable using MediaMetadataRetriever
                    var decodable = true
                    try {
                        val retriever = MediaMetadataRetriever()
                        if (file != null && file.exists()) {
                            retriever.setDataSource(file.absolutePath)
                        } else {
                            retriever.setDataSource(context, generatedUri)
                        }
                        val hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO)
                        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
                        retriever.release()
                        if (hasVideo != "yes" || duration <= 0L) {
                            decodable = false
                        }
                    } catch (e: Exception) {
                        Log.w("VideoPlayer", "Metadata check notice: ${e.message}")
                    }

                    if (!decodable) {
                        validationError = "Unable to play generated video"
                        null
                    } else {
                        validationError = null
                        generatedUri
                    }
                }
            } catch (e: Exception) {
                validationError = "Unable to play generated video"
                null
            }
        }
    }

    if (validationError != null || validUri == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0D0D14))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (validationError == "Unable to play generated video") {
                    Icon(
                        Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = CrimsonRed,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Unable to play generated video",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(26.dp),
                        color = CyanAccent,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        validationError ?: "AI video is still being prepared...",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Retry", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    if (playbackError != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0D0D14))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = CrimsonRed,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Unable to play generated video",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    playbackError ?: "Playback error",
                    color = TextSecondary,
                    fontSize = 10.5.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Retry", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    val exoPlayer = remember(validUri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(validUri))
            prepare()
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    Log.e("VideoPlayer", "Playback error for $validUri: ${error.message}", error)
                    playbackError = error.localizedMessage ?: "Unable to play generated video"
                }
            })
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = showControls
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            }
        },
        update = { view ->
            view.player = exoPlayer
            view.useController = showControls
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun LoadingSpinnerView(label: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = CyanAccent, strokeWidth = 3.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(label, color = TextSecondary, fontSize = 14.sp)
    }
}

@Composable
private fun ErrorStateView(
    message: String,
    onRetry: () -> Unit,
    onChangeRatio: (() -> Unit)? = null,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = CrimsonRed, modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Video couldn't be prepared correctly.", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Please try processing again.", fontSize = 13.sp, color = TextSecondary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = onCancel) { Text("Start Over") }
            if (onChangeRatio != null) {
                OutlinedButton(onClick = onChangeRatio) { Text("Change Ratio") }
            }
            Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black)) { Text("Retry") }
        }
    }
}

private fun Double.format(digits: Int): String = "%.${digits}f".format(this)
