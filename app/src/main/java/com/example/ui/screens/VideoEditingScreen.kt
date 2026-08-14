@file:OptIn(
    androidx.compose.foundation.ExperimentalFoundationApi::class,
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.media3.common.util.UnstableApi::class
)

package com.example.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.core.engines.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

// ============================================================================
// APPLE / IPHONE STYLE THEME & COLOR PALETTE
// ============================================================================
private val AppleDarkBg = Color(0xFF08090C) // Deep Apple Space Black
private val GlassSurface = Color(0xFF131722).copy(alpha = 0.85f)
private val GlassCardBg = Color(0xFF1B202D)
private val GlassBorder = Color(0xFF2E364A).copy(alpha = 0.6f)
private val AccentMint = Color(0xFF10B981) // Apple Pro Mint #10B981
private val AccentMintGlow = Color(0xFF34D399)
private val AccentGradient = Brush.horizontalGradient(listOf(Color(0xFF10B981), Color(0xFF059669)))
private val TextWhite = Color(0xFFF8FAFC)
private val TextMuted = Color(0xFF94A3B8)

// ============================================================================
// ASSISTANT WORKFLOW STEPS
// ============================================================================
enum class AiAssistantStep {
    UPLOAD_SELECT,
    INSPECTED_PREPARE_SHEET,
    PROCESSING_PIPELINE,
    RESULT_BEFORE_AFTER
}

// ============================================================================
// MAIN COMPOSABLE: AI CREATOR ASSISTANT
// ============================================================================
@OptIn(ExperimentalMaterial3Api::class, UnstableApi::class)
@Composable
fun VideoEditingScreen(
    projectConfig: ProjectSetupConfig? = null,
    onNavigateToHome: () -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    // Pipeline Engine Instance
    val pipeline = remember { DefaultAiCreatorAssistantPipeline() }

    // State Variables
    var currentStep by remember { mutableStateOf(AiAssistantStep.UPLOAD_SELECT) }
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var analysisResult by remember { mutableStateOf<VideoAnalysisResult?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }

    // User Operations Selection
    var selectedTargetRatio by remember { mutableStateOf(TargetAspectRatio.RATIO_9_16) }
    var selectedCaptionLanguage by remember { mutableStateOf("English") }
    var selectedPlatformPreset by remember { mutableStateOf(PlatformPreset.INSTAGRAM_REEL) }
    var retryMode by remember { mutableStateOf("Balanced") } // Balanced, More aggressive, Preserve original

    val selectedOperations = remember {
        mutableStateListOf(
            AiOperationType.CHANGE_RATIO,
            AiOperationType.SMART_AUTO_FRAME,
            AiOperationType.CLEAN_AUDIO,
            AiOperationType.ENHANCE_VOICE,
            AiOperationType.IMPROVE_QUALITY,
            AiOperationType.SMART_CAPTIONS
        )
    }

    // Processing Pipeline Progress
    var processingStageName by remember { mutableStateOf("Preparing your video...") }
    var processingProgress by remember { mutableFloatStateOf(0f) }
    var processedResult by remember { mutableStateOf<ProcessedVideoResult?>(null) }

    // Media Picker Launcher
    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedVideoUri = uri
            isAnalyzing = true
            scope.launch {
                analysisResult = pipeline.inspectVideo(context, uri)
                if (analysisResult != null) {
                    selectedTargetRatio = if (analysisResult!!.aspectRatioLabel == "16:9") TargetAspectRatio.RATIO_9_16 else TargetAspectRatio.fromLabel(analysisResult!!.aspectRatioLabel)
                }
                isAnalyzing = false
                currentStep = AiAssistantStep.INSPECTED_PREPARE_SHEET
            }
        }
    }

    // Handle initial projectConfig if passed from home
    LaunchedEffect(projectConfig) {
        val uri = projectConfig?.thumbnailUri ?: projectConfig?.selectedMedia?.firstOrNull()?.uri
        if (uri != null && analysisResult == null) {
            selectedVideoUri = uri
            isAnalyzing = true
            analysisResult = pipeline.inspectVideo(context, uri)
            selectedTargetRatio = if (analysisResult?.aspectRatioLabel == "16:9") TargetAspectRatio.RATIO_9_16 else TargetAspectRatio.fromLabel(analysisResult?.aspectRatioLabel ?: "9:16")
            isAnalyzing = false
            currentStep = AiAssistantStep.INSPECTED_PREPARE_SHEET
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppleDarkBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("ai_creator_assistant_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ================================================================
            // TOP HEADER BAR
            // ================================================================
            TopHeaderBar(
                onBack = onNavigateToHome,
                currentStep = currentStep
            )

            // ================================================================
            // MAIN WORKFLOW CONTENT
            // ================================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) + slideInVertically { it / 8 } togetherWith
                                fadeOut(animationSpec = tween(200)) + slideOutVertically { -it / 8 }
                    },
                    label = "step_transition"
                ) { step ->
                    when (step) {
                        AiAssistantStep.UPLOAD_SELECT -> {
                            UploadAndSelectView(
                                isAnalyzing = isAnalyzing,
                                onSelectVideo = {
                                    pickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
                                },
                                onPickSampleVideo = { sampleName ->
                                    val uri = Uri.parse("android.resource://${context.packageName}/raw/sample")
                                    selectedVideoUri = uri
                                    isAnalyzing = true
                                    scope.launch {
                                        analysisResult = pipeline.inspectVideo(context, uri)
                                        isAnalyzing = false
                                        currentStep = AiAssistantStep.INSPECTED_PREPARE_SHEET
                                    }
                                }
                            )
                        }

                        AiAssistantStep.INSPECTED_PREPARE_SHEET -> {
                            analysisResult?.let { analysis ->
                                InspectedAndPrepareSheetView(
                                    analysis = analysis,
                                    selectedOperations = selectedOperations,
                                    selectedTargetRatio = selectedTargetRatio,
                                    onTargetRatioSelected = { selectedTargetRatio = it },
                                    selectedCaptionLanguage = selectedCaptionLanguage,
                                    onCaptionLanguageSelected = { selectedCaptionLanguage = it },
                                    selectedPlatformPreset = selectedPlatformPreset,
                                    onPlatformPresetSelected = { preset ->
                                        selectedPlatformPreset = preset
                                        selectedTargetRatio = preset.targetRatio
                                    },
                                    onToggleOperation = { op ->
                                        if (selectedOperations.contains(op)) {
                                            selectedOperations.remove(op)
                                        } else {
                                            selectedOperations.add(op)
                                        }
                                    },
                                    onStartProcessing = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        currentStep = AiAssistantStep.PROCESSING_PIPELINE
                                        scope.launch {
                                            val result = pipeline.exportProcessedVideo(
                                                context = context,
                                                analysis = analysis,
                                                selectedOperations = selectedOperations,
                                                targetRatio = selectedTargetRatio,
                                                captionLanguage = selectedCaptionLanguage,
                                                onProgressUpdate = { stage, prog ->
                                                    processingStageName = stage
                                                    processingProgress = prog
                                                }
                                            )
                                            processedResult = result
                                            currentStep = AiAssistantStep.RESULT_BEFORE_AFTER
                                        }
                                    }
                                )
                            }
                        }

                        AiAssistantStep.PROCESSING_PIPELINE -> {
                            ProcessingPipelineView(
                                stageName = processingStageName,
                                progress = processingProgress,
                                selectedOps = selectedOperations
                            )
                        }

                        AiAssistantStep.RESULT_BEFORE_AFTER -> {
                            processedResult?.let { result ->
                                FinalResultComparisonView(
                                    result = result,
                                    analysis = analysisResult,
                                    retryMode = retryMode,
                                    onRetryModeChanged = { retryMode = it },
                                    onRetryAi = {
                                        currentStep = AiAssistantStep.PROCESSING_PIPELINE
                                        scope.launch {
                                            val newResult = pipeline.exportProcessedVideo(
                                                context = context,
                                                analysis = analysisResult!!,
                                                selectedOperations = selectedOperations,
                                                targetRatio = selectedTargetRatio,
                                                captionLanguage = selectedCaptionLanguage,
                                                onProgressUpdate = { stage, prog ->
                                                    processingStageName = stage
                                                    processingProgress = prog
                                                }
                                            )
                                            processedResult = newResult
                                            currentStep = AiAssistantStep.RESULT_BEFORE_AFTER
                                        }
                                    },
                                    onDownloadVideo = { targetRatio ->
                                        Toast.makeText(context, "Saved AI Video (${targetRatio.label}) to Movies/AICreatorAssistant", Toast.LENGTH_LONG).show()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================================================
// 1. TOP HEADER BAR
// ============================================================================
@Composable
private fun TopHeaderBar(
    onBack: () -> Unit,
    currentStep: AiAssistantStep
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(GlassCardBg)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextWhite,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "AI Creator Assistant",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = "AI-powered video preparation",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }
        }

        // Status Badge
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = GlassCardBg,
            border = BorderStroke(1.dp, GlassBorder)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(AccentMint)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AI Ready",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AccentMint
                )
            }
        }
    }
}

// ============================================================================
// 2. UPLOAD & SELECT VIEW
// ============================================================================
@Composable
private fun UploadAndSelectView(
    isAnalyzing: Boolean,
    onSelectVideo: () -> Unit,
    onPickSampleVideo: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isAnalyzing) {
            CircularProgressIndicator(
                color = AccentMint,
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Inspecting video structure...",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = TextWhite
            )
            Text(
                text = "Detecting resolution, audio signals, and subject motion",
                fontSize = 12.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        } else {
            // Main Upload Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(GlassCardBg)
                    .border(BorderStroke(1.5.dp, GlassBorder), RoundedCornerShape(32.dp))
                    .clickable { onSelectVideo() }
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(AccentMint.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.CloudUpload,
                            contentDescription = "Upload",
                            tint = AccentMint,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Upload Video",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Supports MP4 • MOV • WEBM",
                        fontSize = 13.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onSelectVideo,
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentMint),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Create with AI",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Sub-caption
            Text(
                text = "Make any video ready for any platform automatically",
                fontSize = 13.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ============================================================================
// 3. INSPECTED & AI PREPARE SHEET VIEW
// ============================================================================
@Composable
private fun InspectedAndPrepareSheetView(
    analysis: VideoAnalysisResult,
    selectedOperations: List<AiOperationType>,
    selectedTargetRatio: TargetAspectRatio,
    onTargetRatioSelected: (TargetAspectRatio) -> Unit,
    selectedCaptionLanguage: String,
    onCaptionLanguageSelected: (String) -> Unit,
    selectedPlatformPreset: PlatformPreset,
    onPlatformPresetSelected: (PlatformPreset) -> Unit,
    onToggleOperation: (AiOperationType) -> Unit,
    onStartProcessing: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // VIDEO DETECTED MINIMAL SUMMARY (NO HEAVY BORDERED CARDS EVERYWHERE)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(GlassCardBg)
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VIDEO DETECTED",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentMint,
                    letterSpacing = 1.sp
                )
                Text(
                    text = analysis.resolutionLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Minimal Pill Tags
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PillTag(text = analysis.aspectRatioLabel)
                PillTag(text = "${analysis.fps} FPS")
                PillTag(text = analysis.durationFormatted)
                PillTag(text = "${analysis.bitrateKbps} Kbps")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Trait text line
            Text(
                text = "Traits: Subject detected • ${if (analysis.hasAudio) "Audio Present" else "Muted"} • ${if (analysis.hasBackgroundNoise) "Noise present" else "Clean sound"}",
                fontSize = 12.sp,
                color = TextMuted
            )
        }

        // POPUP / SECTION: WHAT SHOULD AI PREPARE?
        Text(
            text = "What should AI prepare?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )

        // Target Ratio Selector Row
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Target Ratio", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(TargetAspectRatio.entries) { ratio ->
                    val isSelected = selectedTargetRatio == ratio
                    Surface(
                        onClick = { onTargetRatioSelected(ratio) },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) AccentMint else GlassCardBg,
                        border = BorderStroke(1.dp, if (isSelected) AccentMint else GlassBorder)
                    ) {
                        Text(
                            text = ratio.label,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.Black else TextWhite,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        // Platform Preset Selector
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Platform Preset", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(PlatformPreset.entries) { preset ->
                    val isSelected = selectedPlatformPreset == preset
                    Surface(
                        onClick = { onPlatformPresetSelected(preset) },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) AccentMint.copy(alpha = 0.2f) else GlassCardBg,
                        border = BorderStroke(1.dp, if (isSelected) AccentMint else GlassBorder)
                    ) {
                        Text(
                            text = preset.label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) AccentMint else TextWhite,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Subtitle Language Selector
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Smart Captions Language", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("English", "Hindi", "Hinglish").forEach { lang ->
                    val isSelected = selectedCaptionLanguage == lang
                    Surface(
                        onClick = { onCaptionLanguageSelected(lang) },
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) AccentMint else GlassCardBg,
                        border = BorderStroke(1.dp, if (isSelected) AccentMint else GlassBorder)
                    ) {
                        Text(
                            text = lang,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.Black else TextWhite,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // AI Operations List with AI Recommended badges
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(text = "AI Operations", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)

            AiOperationType.entries.forEach { op ->
                val isChecked = selectedOperations.contains(op)
                val isRecommended = analysis.recommendedOperations.contains(op) || op == AiOperationType.FULL_AI

                Surface(
                    onClick = { onToggleOperation(op) },
                    shape = RoundedCornerShape(20.dp),
                    color = GlassCardBg,
                    border = BorderStroke(1.dp, if (isChecked) AccentMint.copy(alpha = 0.6f) else GlassBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { onToggleOperation(op) },
                                colors = CheckboxDefaults.colors(checkedColor = AccentMint, checkmarkColor = Color.Black)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = op.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextWhite
                                )
                                Text(
                                    text = op.category,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        if (isRecommended) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = AccentMint.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = AccentMint, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "AI Recommended",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentMint
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Start Processing Floating CTA
        Button(
            onClick = onStartProcessing,
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentMint),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Prepare Video with AI",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

// Helper Pill Tag Component
@Composable
private fun PillTag(text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF242A38)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextWhite,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

// ============================================================================
// 4. PROCESSING PIPELINE VIEW
// ============================================================================
@Composable
private fun ProcessingPipelineView(
    stageName: String,
    progress: Float,
    selectedOps: List<AiOperationType>
) {
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "proc_prog")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Glowing Progress Indicator
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxSize(),
                color = AccentMint,
                strokeWidth = 6.dp,
                trackColor = GlassCardBg
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = "AI Processing",
                    fontSize = 10.sp,
                    color = AccentMint
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Preparing your video...",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stageName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = AccentMint
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Live Stage Progress Checklist
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(GlassCardBg)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StageCheckItem(title = "Analyzing video", isDone = animatedProgress >= 0.15f)
            StageCheckItem(title = "Understanding scenes", isDone = animatedProgress >= 0.30f)
            StageCheckItem(title = "Finding subject & smart framing", isDone = animatedProgress >= 0.50f)
            StageCheckItem(title = "Cleaning audio & speech clarity", isDone = animatedProgress >= 0.70f)
            StageCheckItem(title = "Enhancing video & captions", isDone = animatedProgress >= 0.88f)
            StageCheckItem(title = "Final verification & export", isDone = animatedProgress >= 0.98f)
        }
    }
}

@Composable
private fun StageCheckItem(title: String, isDone: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            color = if (isDone) TextWhite else TextMuted
        )
        if (isDone) {
            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = AccentMint, modifier = Modifier.size(18.dp))
        } else {
            Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
        }
    }
}

// ============================================================================
// 5. FINAL RESULT COMPARISON VIEW (ASPECT-RATIO-AWARE PREVIEW & SLIDER)
// ============================================================================
@Composable
private fun FinalResultComparisonView(
    result: ProcessedVideoResult,
    analysis: VideoAnalysisResult?,
    retryMode: String,
    onRetryModeChanged: (String) -> Unit,
    onRetryAi: () -> Unit,
    onDownloadVideo: (TargetAspectRatio) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    // Interactive Before/After Split Slider State (0.0f = full Before, 1.0f = full After)
    var sliderPosition by remember { mutableFloatStateOf(0.5f) }
    var selectedPreviewRatio by remember { mutableStateOf(result.targetRatio) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Ready Title Banner
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Your video is ready",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = result.verificationReport.statusSummary,
                    fontSize = 12.sp,
                    color = AccentMint
                )
            }
        }

        // MAIN ASPECT-RATIO-AWARE PREVIEW CONTAINER
        // Dynamically calculates height based on selectedPreviewRatio to strictly preserve real video aspect ratio!
        val aspectVal = selectedPreviewRatio.floatValue
        val containerHeight = (320.dp / aspectVal).coerceIn(220.dp, 440.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(containerHeight)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.Black)
                .border(BorderStroke(1.5.dp, GlassBorder), RoundedCornerShape(24.dp))
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        val newPos = (sliderPosition + dragAmount / size.width).coerceIn(0.05f, 0.95f)
                        if ((newPos * 100).toInt() % 10 == 0) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        }
                        sliderPosition = newPos
                    }
                }
        ) {
            // Right Side: AFTER (AI Enhanced) Video Frame / Bitmap
            if (result.previewBitmap != null) {
                Image(
                    bitmap = result.previewBitmap.asImageBitmap(),
                    contentDescription = "After AI",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Left Side: BEFORE (Original) Overlay Clip
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(sliderPosition)
                    .clip(RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp))
                    .background(Color(0xFF181C26))
            ) {
                if (analysis?.firstFrameBitmap != null) {
                    Image(
                        bitmap = analysis.firstFrameBitmap.asImageBitmap(),
                        contentDescription = "Before Original",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("ORIGINAL 16:9", fontSize = 12.sp, color = TextMuted)
                    }
                }
            }

            // Interactive Vertical Divider Bar
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(sliderPosition)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .width(3.dp)
                        .fillMaxHeight()
                        .background(AccentMint)
                ) {
                    // Touch Knob Indicator
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(AccentMint),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Code, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // Floating BEFORE / AFTER Labels
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            ) {
                Text("BEFORE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextWhite, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = AccentMint.copy(alpha = 0.9f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            ) {
                Text("AFTER (${selectedPreviewRatio.label})", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
        }

        // SMALL FRAME COMPARISON THUMBNAILS (Preserving Real Ratio)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Frame Comparison", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(TargetAspectRatio.entries) { ratio ->
                    val isSelected = selectedPreviewRatio == ratio
                    val thumbW = 70.dp
                    val thumbH = (70.dp / ratio.floatValue).coerceIn(50.dp, 100.dp)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { selectedPreviewRatio = ratio }
                    ) {
                        Box(
                            modifier = Modifier
                                .width(thumbW)
                                .height(thumbH)
                                .clip(RoundedCornerShape(12.dp))
                                .background(GlassCardBg)
                                .border(BorderStroke(1.5.dp, if (isSelected) AccentMint else GlassBorder), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ratio.label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) AccentMint else TextWhite
                            )
                        }
                    }
                }
            }
        }

        // QUALITY VERIFICATION REPORT SUMMARY
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(GlassCardBg)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("QUALITY VERIFICATION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentMint, letterSpacing = 1.sp)
            Text(result.verificationReport.statusSummary, fontSize = 13.sp, color = TextWhite)

            if (result.verificationReport.operationsApplied.isNotEmpty()) {
                Text("Applied Enhancements:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
                result.verificationReport.operationsApplied.forEach { opName ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Check, contentDescription = null, tint = AccentMint, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(opName, fontSize = 12.sp, color = TextWhite)
                    }
                }
            }
        }

        // RETRY AI SECTION
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(GlassCardBg)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Retry AI Mode", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Balanced", "More aggressive", "Preserve original").forEach { mode ->
                    val isSelected = retryMode == mode
                    Surface(
                        onClick = { onRetryModeChanged(mode) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) AccentMint else Color(0xFF242A38)
                    ) {
                        Text(
                            text = mode,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.Black else TextWhite,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = onRetryAi,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, AccentMint),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = null, tint = AccentMint, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Retry AI Processing", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AccentMint)
            }
        }

        // DOWNLOAD BUTTONS
        Button(
            onClick = { onDownloadVideo(selectedPreviewRatio) },
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentMint),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Icon(Icons.Filled.Download, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Download AI Version (${selectedPreviewRatio.label})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
