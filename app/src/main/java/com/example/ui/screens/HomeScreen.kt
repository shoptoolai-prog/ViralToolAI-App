package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.MediaImportHelper
import com.example.ui.components.CompactCreatorMissionsWidget
import com.example.ui.components.CreatorMissionsDialog
import com.example.ui.components.ViralToolAiLogo
import com.example.ui.components.ViriMascotWidget
import com.example.ui.components.ViriPrefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

// ============================================================================
// VIRALTOOLAI PREMIUM IPHONE-INSPIRED CREATOR DASHBOARD
// ============================================================================
private val HomeBg = Color(0xFF0F1115) // Dark graphite background with depth
private val CardSurface = Color(0xFF171A21) // Elevated dark surface
private val TileSurface = Color(0xFF1F2430) // Second level tile surface
private val GlassBorder = Color(0xFF22D7E8).copy(alpha = 0.6f) // 1.5dp cyan border
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFA0AAB8)
private val CyanAccent = Color(0xFF22D7E8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProjects: () -> Unit = {},
    onNavigateToAiLab: () -> Unit = {},
    onNavigateToAcademy: (() -> Unit)? = null,
    onNavigateToMediaPicker: () -> Unit = {},
    onVideoImportedToEditor: (ProjectSetupConfig) -> Unit = {},
    onNavigateToAiCreatorAssistant: () -> Unit = {},
    onNavigateToThumbnailPicker: () -> Unit = {},
    onNavigateToSubtitlesGenerator: () -> Unit = {},
    onNavigateToVoiceCleaner: () -> Unit = {},
    onNavigateToSmartVideoText: () -> Unit = {},
    initialSharedUrl: String? = null
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var isImportingMedia by remember { mutableStateOf(false) }
    var showImportErrorDialog by remember { mutableStateOf(false) }
    var showCreatorMissionsDialog by remember { mutableStateOf(false) }

    // Pending import config for AI Creator Wizard flow
    var pendingImportConfig by remember { mutableStateOf<ProjectSetupConfig?>(null) }
    var showAiWizard by remember { mutableStateOf(false) }
    var showAiAnalysis by remember { mutableStateOf(false) }
    var showAiOptimization by remember { mutableStateOf(false) }
    var showAiPreEdit by remember { mutableStateOf(false) }
    var showAiViralXRay by remember { mutableStateOf(false) }
    var showAiViralCoach by remember { mutableStateOf(false) }
    var showAiUploadSimulator by remember { mutableStateOf(false) }
    var analysisConfig by remember { mutableStateOf<ProjectSetupConfig?>(null) }

    // Track mode for import: true for Reel Analysis, false for Direct Editor
    var isAnalysisMode by remember { mutableStateOf(true) }

    // Media Picker Launcher (Videos / Images)
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri == null) {
            Log.d("VIRI_DEBUG", "LOG: If Uri == null -> No video selected")
            Toast.makeText(context, "No video selected", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("VIRI_DEBUG", "LOG: Video selected")
            Log.d("VIRI_DEBUG", "LOG: Uri received: $uri")
            
            // Persist Uri permission if supported
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Throwable) {
                Log.w("VIRI_DEBUG", "Persistable permission notice: ${e.localizedMessage}")
            }

            isImportingMedia = true
            scope.launch {
                try {
                    val item = MediaImportHelper.importVideoUri(context, uri)
                    isImportingMedia = false
                    if (item != null) {
                        val config = MediaImportHelper.createDefaultProjectConfig(listOf(item))
                        pendingImportConfig = config
                        Log.d("VIRI_DEBUG", "LOG: Navigation started")
                        if (isAnalysisMode) {
                            showAiWizard = true
                        } else {
                            onVideoImportedToEditor(config)
                        }
                    } else {
                        Log.e("VIRI_DEBUG", "Failed to process video item for Uri: $uri")
                        Toast.makeText(context, "No video selected or error reading file", Toast.LENGTH_LONG).show()
                        showImportErrorDialog = true
                    }
                } catch (e: Throwable) {
                    isImportingMedia = false
                    Log.e("VIRI_DEBUG", "Error during video import", e)
                    Toast.makeText(context, "Failed to load video: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    showImportErrorDialog = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeBg),
        contentAlignment = Alignment.TopCenter
    ) {
        // Soft Cyan Radial Light Overlay
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(CyanAccent.copy(alpha = 0.12f), Color.Transparent),
                    center = Offset(size.width * 0.5f, size.height * 0.18f),
                    radius = size.width * 0.85f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Scrollable Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                // ==================================================
                // TOP HEADER SECTION (Apple Style Creator Dashboard)
                // ==================================================
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Top Left: ViralToolAI Logo
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ViralToolAiLogo(size = 32.dp)
                        Text(
                            text = "ViralToolAI",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            letterSpacing = (-0.3).sp
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Large Heading
                    Text(
                        text = "Ready to make today's viral reel?",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        lineHeight = 34.sp,
                        letterSpacing = (-0.5).sp
                    )

                    // Small Subtitle
                    Text(
                        text = "Your AI creator assistant is online.",
                        fontSize = 14.5.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // LIVE STATUS BAR
                    LiveStatusBar()
                }

                // ==================================================
                // AI CREATOR MISSIONS COMPACT HOME WIDGET
                // ==================================================
                CompactCreatorMissionsWidget(
                    onClick = {
                        showCreatorMissionsDialog = true
                    }
                )

                // ==================================================
                // PRIMARY TOOLS (AI Reel Analysis & AI Creator Assistant)
                // ==================================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // LEFT TILE: AI REEL ANALYSIS
                    AiReelsAppleCard(
                        modifier = Modifier.weight(1f),
                        testTag = "tile_ai_reels",
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isAnalysisMode = true
                            Log.d("VIRI_DEBUG", "LOG: Gallery opened")
                            mediaPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                            )
                        }
                    )

                    // RIGHT TILE: AI CREATOR ASSISTANT
                    AiCreatorAssistantAppleCard(
                        modifier = Modifier.weight(1f),
                        testTag = "tile_ai_creator_assistant",
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNavigateToAiCreatorAssistant()
                        }
                    )
                }

                // ==================================================
                // CREATOR TOOLS SECTION (4 Premium Modern AI Creator Tools)
                // ==================================================
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Creator Tools",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = CyanAccent.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "4 AI Utilities",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // ROW 1: Thumbnail Picker & Subtitles Generator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CreatorToolCard(
                                title = "Thumbnail Picker",
                                subtitle = "Find your best 2 frames",
                                icon = Icons.Outlined.Image,
                                iconColor = Color(0xFF22D7E8), // Cyan
                                tag = "High CTR",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onNavigateToThumbnailPicker()
                                }
                            )

                            CreatorToolCard(
                                title = "Subtitles Generator",
                                subtitle = "Auto-captions with style",
                                icon = Icons.Outlined.Subtitles,
                                iconColor = Color(0xFF10B981), // Emerald
                                tag = "Multi-Lang",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onNavigateToSubtitlesGenerator()
                                }
                            )
                        }

                        // ROW 2: Voice Cleaner & Smart Video Text
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CreatorToolCard(
                                title = "Voice Cleaner",
                                subtitle = "Noise reduction & boost",
                                icon = Icons.Outlined.Mic,
                                iconColor = Color(0xFFA855F7), // Purple
                                tag = "Crisp Audio",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onNavigateToVoiceCleaner()
                                }
                            )

                            CreatorToolCard(
                                title = "Smart Video Text",
                                subtitle = "Auto-text overlays",
                                icon = Icons.Outlined.TextFields,
                                iconColor = Color(0xFFFF9800), // Orange
                                tag = "Hook Banner",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onNavigateToSmartVideoText()
                                }
                            )
                        }
                    }
                }
            }
        }

        // ==================================================
        // DRAGGABLE AI ROBOT MASCOT ("VIRI")
        // ==================================================
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp, end = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            ViriMascotWidget(
                size = 100.dp,
                onTapAction = {
                    ViriPrefs.addExp(context, 10)
                }
            )
        }

        // ==================================================
        // MEDIA IMPORT LOADING OVERLAY
        // ==================================================
        if (isImportingMedia) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black.copy(alpha = 0.85f)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = CyanAccent,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Importing Media...",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Preparing AI analysis engine",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // ==================================================
        // DIALOGS & OVERLAYS
        // ==================================================
        if (showCreatorMissionsDialog) {
            CreatorMissionsDialog(
                onDismiss = { showCreatorMissionsDialog = false }
            )
        }

        if (showImportErrorDialog) {
            AlertDialog(
                onDismissRequest = { showImportErrorDialog = false },
                containerColor = CardSurface,
                shape = RoundedCornerShape(24.dp),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = CyanAccent)
                        Text("Media Import Notice", fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                },
                text = {
                    Text(
                        text = "Unable to process the selected video file. Supported video formats: MP4, MOV, MKV, WEBM.",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { showImportErrorDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black)
                    ) {
                        Text("OK", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // ==================================================
        // AI REEL ANALYSIS FLOW
        // ==================================================
        if (showAiWizard && pendingImportConfig != null) {
            MasterReelDoctorFlow(
                config = pendingImportConfig,
                onDismiss = {
                    showAiWizard = false
                },
                onContinueToEditor = { cfg ->
                    showAiWizard = false
                    onVideoImportedToEditor(cfg)
                }
            )
        }

        if (showAiAnalysis && analysisConfig != null) {
            AiAnalysisOverlay(
                config = analysisConfig,
                onDismiss = {
                    showAiAnalysis = false
                },
                onContinueToOptimization = { cfg ->
                    showAiAnalysis = false
                    analysisConfig = cfg
                    showAiOptimization = true
                }
            )
        }

        if (showAiOptimization && analysisConfig != null) {
            AiOptimizationOverlay(
                config = analysisConfig,
                onDismiss = {
                    showAiOptimization = false
                },
                onApplyOptimizations = { cfg ->
                    showAiOptimization = false
                    analysisConfig = cfg
                    showAiPreEdit = true
                }
            )
        }

        if (showAiPreEdit && analysisConfig != null) {
            AiAutoFixOverlay(
                config = analysisConfig,
                onDismiss = {
                    showAiPreEdit = false
                },
                onCompletePackageExport = { cfg ->
                    showAiPreEdit = false
                    analysisConfig = cfg
                    showAiViralXRay = true
                }
            )
        }

        if (showAiViralXRay && analysisConfig != null) {
            AiViralXRayOverlay(
                config = analysisConfig,
                onDismiss = {
                    showAiViralXRay = false
                },
                onContinueToEditor = { cfg ->
                    showAiViralXRay = false
                    onVideoImportedToEditor(cfg)
                },
                onOpenCoach = {
                    showAiViralXRay = false
                    showAiViralCoach = true
                }
            )
        }

        if (showAiViralCoach && analysisConfig != null) {
            AiViralCoachOverlay(
                config = analysisConfig,
                onDismiss = {
                    showAiViralCoach = false
                },
                onContinueToEditor = { cfg ->
                    showAiViralCoach = false
                    onVideoImportedToEditor(cfg)
                },
                onOpenSimulator = {
                    showAiViralCoach = false
                    showAiUploadSimulator = true
                }
            )
        }

        if (showAiUploadSimulator && analysisConfig != null) {
            AiUploadSimulatorOverlay(
                config = analysisConfig,
                onDismiss = {
                    showAiUploadSimulator = false
                },
                onContinueToEditor = { cfg ->
                    showAiUploadSimulator = false
                    onVideoImportedToEditor(cfg)
                }
            )
        }
    }
}

// ============================================================================
// HELPER COMPONENTS FOR VIRALTOOLAI DASHBOARD
// ============================================================================

@Composable
private fun CreatorToolCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    tag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f, label = "tool_press_scale")

    val infiniteTransition = rememberInfiniteTransition(label = "tool_float")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tool_float_y"
    )

    Surface(
        modifier = modifier
            .height(138.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationY = floatOffset
            }
            .clip(RoundedCornerShape(26.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() },
        shape = RoundedCornerShape(26.dp),
        color = CardSurface,
        border = BorderStroke(1.dp, GlassBorder.copy(alpha = 0.35f)),
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(TileSurface.copy(alpha = 0.7f), CardSurface)
                    )
                )
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(iconColor.copy(alpha = 0.16f))
                            .border(1.dp, iconColor.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = iconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = iconColor.copy(alpha = 0.12f),
                        border = BorderStroke(0.5.dp, iconColor.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = tag,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = iconColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun AiCreatorAssistantAppleCard(
    modifier: Modifier = Modifier,
    testTag: String = "tile_ai_creator_assistant",
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.96f else 1f, label = "press_scale_assistant")

    val infiniteTransition = rememberInfiniteTransition(label = "assistant_pulse")
    val buttonPulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btn_pulse_assistant"
    )

    Surface(
        modifier = modifier
            .height(185.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(30.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(30.dp),
        color = CardSurface,
        border = BorderStroke(1.5.dp, GlassBorder),
        shadowElevation = 10.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1C2232), Color(0xFF121622))
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CyanAccent.copy(alpha = 0.2f))
                            .border(1.dp, CyanAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = CyanAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CyanAccent.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "AUTO PROCESS",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "AI Creator Assistant",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "AI-powered video preparation",
                        fontSize = 11.5.sp,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }

                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .graphicsLayer {
                            scaleX = buttonPulseScale
                            scaleY = buttonPulseScale
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Open Assistant →", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun AiReelsAppleCard(
    modifier: Modifier = Modifier,
    testTag: String = "tile_ai_reels",
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.96f else 1f, label = "press_scale")

    val infiniteTransition = rememberInfiniteTransition(label = "phone_scan")
    val scanLineY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scan"
    )

    // Card Floating Animation
    val cardFloatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card_float"
    )

    // Button Pulse Animation
    val buttonPulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btn_pulse"
    )

    Surface(
        modifier = modifier
            .height(185.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationY = cardFloatY
            }
            .clip(RoundedCornerShape(30.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(30.dp),
        color = CardSurface,
        border = BorderStroke(1.5.dp, GlassBorder),
        shadowElevation = 10.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1F2430), Color(0xFF141720))
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                // Top: 9:16 Phone Mockup with animated light reflection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .height(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF101216))
                            .border(1.dp, CyanAccent.copy(alpha = 0.8f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val canvasW = size.width
                            val canvasH = size.height
                            drawRect(
                                color = CyanAccent.copy(alpha = 0.5f),
                                topLeft = Offset(0f, canvasH * scanLineY),
                                size = Size(canvasW, 2.dp.toPx())
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = CyanAccent,
                            modifier = Modifier.size(12.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CyanAccent.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "PREDICT",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    }
                }

                // Middle: Text Info
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "AI Reel Analysis",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Analyze & predict score",
                        fontSize = 11.5.sp,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }

                // Bottom: Cyan Upload Button CTA with pulse
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .graphicsLayer {
                            scaleX = buttonPulseScale
                            scaleY = buttonPulseScale
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(14.dp))
                        Text("Upload Reel", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun LiveStatusBar(modifier: Modifier = Modifier) {
    val statuses = remember {
        listOf(
            "🟢 AI Online",
            "⚡ Viral Engine Ready",
            "🎯 Creator Tools Active",
            "📊 Trend Sync Complete"
        )
    }
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentIndex = (currentIndex + 1) % statuses.size
        }
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = TileSurface,
        border = BorderStroke(1.dp, GlassBorder.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(CyanAccent)
            )
            AnimatedContent(
                targetState = statuses[currentIndex],
                transitionSpec = {
                    fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
                },
                label = "status_anim"
            ) { status ->
                Text(
                    text = status,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }
        }
    }
}
