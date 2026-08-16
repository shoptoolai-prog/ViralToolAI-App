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
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.core.MediaImportHelper
import com.example.ui.components.CompactCreatorMissionsWidget
import com.example.ui.components.CreatorMissionsDialog
import com.example.ui.components.ViralToolAiLogo
import com.example.ui.components.ViriMascotWidget
import com.example.ui.components.ViriPrefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ============================================================================
// VIRALTOOLAI PREMIUM DESIGN TOKENS
// ============================================================================
private val HomeBg = Color(0xFF0A0C10)
private val CardSurface = Color(0xFF121620)
private val CardSurfaceElevated = Color(0xFF181D2A)
private val CardSurfaceGlass = Color(0xCC151A26)
private val GlassBorder = Color(0xFF22D7E8).copy(alpha = 0.35f)
private val GlassBorderSubtle = Color(0xFFFFFFFF).copy(alpha = 0.08f)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFF94A3B8)
private val TextTertiary = Color(0xFF64748B)
private val CyanAccent = Color(0xFF22D7E8)
private val CyanGlow = Color(0xFF00F0FF)
private val ElectricBlue = Color(0xFF3B82F6)
private val EmeraldAccent = Color(0xFF10B981)
private val PurpleAccent = Color(0xFFA855F7)
private val AmberAccent = Color(0xFFF59E0B)

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
    onNavigateToShoppingAssistant: () -> Unit = {},
    onNavigateToRemoveBackground: () -> Unit = {},
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

    // Screen entrance animation state
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    val contentAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "content_alpha"
    )
    val contentOffsetY by animateFloatAsState(
        targetValue = if (isVisible) 0f else 24f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "content_offset_y"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeBg),
        contentAlignment = Alignment.TopCenter
    ) {
        // Ambient Cyber Glow in background
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(CyanAccent.copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(size.width * 0.5f, size.height * 0.15f),
                    radius = size.width * 0.9f
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(ElectricBlue.copy(alpha = 0.05f), Color.Transparent),
                    center = Offset(size.width * 0.8f, size.height * 0.45f),
                    radius = size.width * 0.7f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .graphicsLayer {
                    alpha = contentAlpha
                    translationY = contentOffsetY
                }
        ) {
            // Scrollable Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 18.dp)
                    .padding(top = 12.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                // ==================================================
                // 1. TOP BRANDING / HEADER WITH ANIMATED "AI READY"
                // ==================================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ViralToolAiLogo(size = 34.dp)
                        Column {
                            Text(
                                text = "ViralToolAI",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                letterSpacing = (-0.4).sp
                            )
                            Text(
                                text = "Creator Studio Pro",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextTertiary,
                                letterSpacing = 0.4.sp
                            )
                        }
                    }

                    // Small Animated AI Status Indicator: "AI READY"
                    AiReadyStatusBadge()
                }

                // ==================================================
                // 2. TOP HERO (Parallax, Glowing Particles & Soft Gradients)
                // ==================================================
                val heroParallax = (scrollState.value * 0.18f).coerceAtMost(40f)
                HeroGlassmorphicCard(
                    modifier = Modifier.graphicsLayer {
                        translationY = -heroParallax
                    }
                )

                // ==================================================
                // 3. TWO MAIN TOOLS — SIDE BY SIDE (Equal Dimension Square Cards)
                // ==================================================
                val cardsParallax = (scrollState.value * 0.08f).coerceAtMost(20f)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            translationY = -cardsParallax
                        },
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // CARD 1: AI Reel Analysis
                    SquareFlagshipToolCard(
                        title = "AI Reel Analysis",
                        subtitle = "Analyze your Reel",
                        imageRes = R.drawable.ai_reel_analysis,
                        imageContentDescription = "AI Reel Analysis Visual",
                        accentColor = CyanAccent,
                        testTag = "tile_ai_reels",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isAnalysisMode = true
                            Log.d("VIRI_DEBUG", "LOG: Gallery opened for Reel Analysis")
                            mediaPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                            )
                        }
                    )

                    // CARD 2: AI Creator Assistant
                    SquareFlagshipToolCard(
                        title = "AI Creator Assistant",
                        subtitle = "Create your video",
                        imageRes = R.drawable.ai_creator_assistant,
                        imageContentDescription = "AI Creator Assistant Visual",
                        accentColor = ElectricBlue,
                        testTag = "tile_ai_creator_assistant",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNavigateToAiCreatorAssistant()
                        }
                    )
                }

                // ==================================================
                // 4. CREATOR TOOLS (Separate Independent Rectangular Cards)
                // ==================================================
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Clean glowing heading with subtle animated underline
                    CreatorToolsSectionHeader(
                        title = "Creator Tools"
                    )

                    // 1. Thumbnail Picker
                    IndependentCreatorToolCard(
                        title = "Thumbnail Picker",
                        subtitle = "Find the best frame",
                        imageRes = R.drawable.thumbnail_picker,
                        accentColor = CyanAccent,
                        testTag = "tile_thumbnail_picker",
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNavigateToThumbnailPicker()
                        }
                    )

                    // 2. Subtitles Generator
                    IndependentCreatorToolCard(
                        title = "Subtitles Generator",
                        subtitle = "Auto-generate subtitles",
                        imageRes = R.drawable.subtitles_generator,
                        accentColor = EmeraldAccent,
                        testTag = "tile_subtitles_generator",
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNavigateToSubtitlesGenerator()
                        }
                    )

                    // 3. Voice Cleaner
                    IndependentCreatorToolCard(
                        title = "Voice Cleaner",
                        subtitle = "Clean noise & enhance voice",
                        imageRes = R.drawable.voice_cleaner,
                        accentColor = PurpleAccent,
                        testTag = "tile_voice_cleaner",
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNavigateToVoiceCleaner()
                        }
                    )

                    // 4. Smart Video Text
                    IndependentCreatorToolCard(
                        title = "Smart Video Text",
                        subtitle = "Add stylish text automatically",
                        imageRes = R.drawable.smart_video_text,
                        accentColor = AmberAccent,
                        testTag = "tile_smart_video_text",
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNavigateToSmartVideoText()
                        }
                    )

                    // 5. Remove Background
                    IndependentCreatorToolCard(
                        title = "Remove Background",
                        subtitle = "Remove image background",
                        imageRes = R.drawable.remove_background,
                        accentColor = Color(0xFF00E5FF),
                        testTag = "tile_remove_background",
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onNavigateToRemoveBackground()
                        }
                    )
                }

                // ==================================================
                // 5. AI SHOPPING ASSISTANT (Premium Flagship Card)
                // ==================================================
                AiShoppingAssistantCard(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onNavigateToShoppingAssistant()
                    }
                )
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
                size = 96.dp,
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
        // DIALOGS & OVERLAYS (UNCHANGED ENGINE INTEGRATION)
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
        // AI REEL ANALYSIS FLOW (UNCHANGED)
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
// ANIMATED "AI READY" STATUS BADGE
// ============================================================================
@Composable
private fun AiReadyStatusBadge(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "ai_ready_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "beacon_pulse"
    )

    Surface(
        shape = RoundedCornerShape(50),
        color = CardSurfaceElevated,
        border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.4f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(CyanAccent.copy(alpha = pulseAlpha))
                    .border(0.5.dp, Color.White.copy(alpha = 0.8f), CircleShape)
            )
            Text(
                text = "AI READY",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CyanAccent,
                letterSpacing = 0.8.sp
            )
        }
    }
}

// ============================================================================
// MAIN HERO GLASSMORPHIC CARD (Liquid Glass & Real Photorealistic Creator Image)
// ============================================================================
@Composable
private fun HeroGlassmorphicCard(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_glass_ambient")
    // Subtle breathing glow on border
    val borderGlow by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hero_border_glow"
    )
    // Smooth liquid glass light reflection sweep across surface
    val sweepProgress by infiniteTransition.animateFloat(
        initialValue = -0.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(4500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "hero_sweep"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        color = CardSurfaceGlass,
        border = BorderStroke(
            1.dp,
            Brush.linearGradient(
                colors = listOf(
                    CyanAccent.copy(alpha = borderGlow * 0.8f),
                    Color.White.copy(alpha = 0.25f),
                    ElectricBlue.copy(alpha = borderGlow * 0.5f),
                    Color.White.copy(alpha = 0.08f)
                ),
                start = Offset(0f, 0f),
                end = Offset(400f, 200f)
            )
        ),
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF101726),
                            Color(0xFF090E17).copy(alpha = 0.96f),
                            Color(0xFF0D1420)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(500f, 300f)
                    )
                )
        ) {
            // Subtle Liquid Glass Light Reflection Ray
            Canvas(
                modifier = Modifier.matchParentSize()
            ) {
                val w = size.width
                val h = size.height
                val sweepX = w * sweepProgress
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.045f),
                            CyanAccent.copy(alpha = 0.07f),
                            Color.White.copy(alpha = 0.045f),
                            Color.Transparent
                        ),
                        start = Offset(sweepX - 80.dp.toPx(), 0f),
                        end = Offset(sweepX + 80.dp.toPx(), h)
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Left text column: Clean & Compact
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "Ready to make your next viral Reel?",
                        fontSize = 17.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary,
                        letterSpacing = (-0.3).sp,
                        lineHeight = 22.sp
                    )

                    Text(
                        text = "Create, enhance and transform your videos with AI.",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextSecondary,
                        lineHeight = 15.5.sp
                    )
                }

                // Right photo container: Real Photorealistic Creator Image in Liquid Glass Frame
                Box(
                    modifier = Modifier
                        .size(width = 96.dp, height = 82.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF131C2D))
                        .border(
                            1.dp,
                            Brush.linearGradient(
                                listOf(
                                    CyanAccent.copy(alpha = 0.6f),
                                    ElectricBlue.copy(alpha = 0.3f),
                                    Color.White.copy(alpha = 0.15f)
                                )
                            ),
                            RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.hero_creator_banner),
                        contentDescription = "Content Creator Recording Reel",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Subtle Glass Sheen on top of image
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.08f),
                                        Color.Transparent,
                                        Color(0xFF090E17).copy(alpha = 0.35f)
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}

// ============================================================================
// SECTION HEADER WITH GLOW & ANIMATED UNDERLINE
// ============================================================================
@Composable
private fun CreatorToolsSectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "header_underline")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "underline_shimmer"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,
                letterSpacing = 0.5.sp
            )

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = CyanAccent.copy(alpha = 0.12f),
                border = BorderStroke(0.8.dp, CyanAccent.copy(alpha = 0.35f))
            ) {
                Text(
                    text = "4 UTILITIES",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanAccent,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    letterSpacing = 0.4.sp
                )
            }
        }

        // Shimmering Animated Underline
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
        ) {
            val width = size.width
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        CyanAccent.copy(alpha = 0.7f),
                        ElectricBlue.copy(alpha = 0.8f),
                        Color.Transparent
                    ),
                    startX = 0f,
                    endX = width * 0.35f
                ),
                start = Offset(0f, 0f),
                end = Offset(width * 0.4f, 0f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            val dotX = width * 0.4f * shimmerOffset
            drawCircle(
                color = Color.White.copy(alpha = 0.8f),
                radius = 1.5.dp.toPx(),
                center = Offset(dotX, 0f)
            )
        }
    }
}

// ============================================================================
// SQUARE FLAGSHIP TOOL CARD (Equal Dimensions, Side-by-Side)
// ============================================================================
@Composable
private fun SquareFlagshipToolCard(
    title: String,
    subtitle: String,
    imageRes: Int,
    imageContentDescription: String,
    accentColor: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "square_tool_scale"
    )

    Surface(
        modifier = modifier
            .aspectRatio(0.85f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(22.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(22.dp),
        color = CardSurfaceElevated,
        border = BorderStroke(
            1.2.dp,
            if (isPressed) accentColor else accentColor.copy(alpha = 0.45f)
        ),
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF19202F),
                            Color(0xFF10141D)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top: Cinematic Artwork Preview Frame
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF090D14))
                        .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = imageContentDescription,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Subtle soft cinematic vignette overlay at edges
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color(0xFF090D14).copy(alpha = 0.35f)
                                    )
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom: Title & Subtitle + Launch Arrow
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = subtitle,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// INDEPENDENT RECTANGULAR CREATOR TOOL CARD
// ============================================================================
@Composable
private fun IndependentCreatorToolCard(
    title: String,
    subtitle: String,
    imageRes: Int,
    accentColor: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "card_press"
    )
    val imgScale by animateFloatAsState(
        targetValue = if (isPressed) 1.05f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "img_press"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(18.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF131722),
        border = BorderStroke(
            1.dp,
            if (isPressed) accentColor else Color(0x22FFFFFF)
        ),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LEFT SIDE: Real Photorealistic Human Image (approx 36% of card width)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.36f)
                    .clip(RoundedCornerShape(topStart = 18.dp, bottomStart = 18.dp))
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = imgScale
                            scaleY = imgScale
                        }
                )

                // Subtle inner right edge vignette to blend with dark card
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color(0x22131722),
                                    Color(0xFF131722)
                                )
                            )
                        )
                )
            }

            // RIGHT SIDE: Tool name, short description, arrow action button
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.64f)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = subtitle,
                        fontSize = 12.5.sp,
                        color = Color.White.copy(alpha = 0.65f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Small Action Arrow in Circle
                Surface(
                    modifier = Modifier.size(34.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, if (isPressed) accentColor else Color(0x22FFFFFF))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Open $title",
                            tint = if (isPressed) accentColor else Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// AI SHOPPING ASSISTANT PREMIUM CARD (Liquid Glass & Real Photorealistic Asset)
// ============================================================================
@Composable
private fun AiShoppingAssistantCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardInteractionSource = remember { MutableInteractionSource() }
    val isCardPressed by cardInteractionSource.collectIsPressedAsState()
    val cardScale by animateFloatAsState(
        targetValue = if (isCardPressed) 0.985f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "shopping_card_scale"
    )

    val buttonInteractionSource = remember { MutableInteractionSource() }
    val isButtonPressed by buttonInteractionSource.collectIsPressedAsState()
    val buttonScale by animateFloatAsState(
        targetValue = if (isButtonPressed) 0.965f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "shopping_btn_scale"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
            .clip(RoundedCornerShape(22.dp))
            .clickable(interactionSource = cardInteractionSource, indication = null) { onClick() }
            .testTag("card_ai_shopping_assistant"),
        shape = RoundedCornerShape(22.dp),
        color = CardSurface,
        border = BorderStroke(
            1.dp,
            if (isCardPressed) CyanAccent.copy(alpha = 0.8f) else GlassBorderSubtle
        ),
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Real Photorealistic AI Shopping Scene Image Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(165.dp)
                    .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ai_shopping_assistant),
                    contentDescription = "Real Person Shopping Online with Smartphone",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Cinematic Soft Depth & Contrast Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xFF090D14).copy(alpha = 0.35f),
                                    Color(0xFF090D14).copy(alpha = 0.88f)
                                )
                            )
                        )
                )

                // Subtle Glass Floating Badge Top-End
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xCC090D14),
                    border = BorderStroke(0.8.dp, CyanAccent.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = CyanAccent,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "AI Shopping",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    }
                }
            }

            // Text content & Premium Glass Action Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "AI Shopping Assistant",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Find. Compare. Buy Smarter.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = CyanAccent.copy(alpha = 0.9f)
                        )
                    }
                }

                Text(
                    text = "Scan a product, reel or screenshot and let AI find the best options.",
                    fontSize = 12.5.sp,
                    color = TextSecondary,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Premium Glass Interaction Button (iPhone-style depth & subtle cyan glow)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .graphicsLayer {
                            scaleX = buttonScale
                            scaleY = buttonScale
                        }
                        .clip(RoundedCornerShape(13.dp))
                        .clickable(
                            interactionSource = buttonInteractionSource,
                            indication = null
                        ) { onClick() }
                        .testTag("btn_open_shopping_assistant"),
                    shape = RoundedCornerShape(13.dp),
                    color = if (isButtonPressed) CyanAccent.copy(alpha = 0.28f) else Color(0xFF10192A),
                    border = BorderStroke(
                        1.dp,
                        Brush.horizontalGradient(
                            listOf(
                                CyanAccent.copy(alpha = if (isButtonPressed) 0.9f else 0.65f),
                                ElectricBlue.copy(alpha = if (isButtonPressed) 0.7f else 0.35f),
                                Color.White.copy(alpha = 0.18f)
                            )
                        )
                    ),
                    shadowElevation = if (isButtonPressed) 2.dp else 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        CyanAccent.copy(alpha = 0.18f),
                                        ElectricBlue.copy(alpha = 0.12f),
                                        Color(0xFF0F1828)
                                    )
                                )
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = CyanAccent
                            )
                            Text(
                                text = "Open Shopping Assistant",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }

                        // Premium Action Arrow Indicator
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Open",
                            tint = CyanAccent,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}

