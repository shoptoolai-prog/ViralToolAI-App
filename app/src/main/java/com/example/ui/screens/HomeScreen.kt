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
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
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
// DS-9 APPLE HUMAN INTERFACE & MEESHO CREATOR PREMIUM DASHBOARD
// ============================================================================
private val HomeBg = Color(0xFF0F1115) // Dark graphite background with depth
private val CardSurface = Color(0xFF171A21) // Elevated dark surface
private val TileSurface = Color(0xFF1F2430) // Second level tile surface
private val GlassBorder = Color(0xFF22D7E8).copy(alpha = 0.6f) // 1.5dp cyan border
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFA0AAB8)
private val CyanAccent = Color(0xFF22D7E8)

data class RecentProjectItem(
    val id: String,
    val title: String,
    val duration: String,
    val timeAgo: String,
    val isReel: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProjects: () -> Unit = {},
    onNavigateToAiLab: () -> Unit = {},
    onNavigateToAcademy: (() -> Unit)? = null,
    onNavigateToMediaPicker: () -> Unit = {},
    onVideoImportedToEditor: (ProjectSetupConfig) -> Unit = {},
    initialSharedUrl: String? = null
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var isImportingMedia by remember { mutableStateOf(false) }
    var showImportErrorDialog by remember { mutableStateOf(false) }

    // Dialog & overlay states
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showCreatorPlannerDialog by remember { mutableStateOf(false) }
    var showHookLibraryDialog by remember { mutableStateOf(false) }
    var showScriptBuilderDialog by remember { mutableStateOf(false) }
    var showShoppingInsightsDialog by remember { mutableStateOf(false) }
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

    // Dynamic greeting based on current hour
    val currentHour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val greetingText = when (currentHour) {
        in 5..11 -> "Good Morning, Creator 👋"
        in 12..16 -> "Good Afternoon, Creator 👋"
        else -> "Good Evening, Creator 👋"
    }

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

    // Sample Recent Projects (Meesho style horizontal list)
    val recentProjects = remember {
        listOf(
            RecentProjectItem("1", "Viral Hook Reel #14", "0:45", "2 hrs ago"),
            RecentProjectItem("2", "Product Review Shorts", "0:30", "Yesterday"),
            RecentProjectItem("3", "Unboxing Affiliate", "1:12", "3 days ago"),
            RecentProjectItem("4", "Outfit Aesthetic", "0:25", "5 days ago")
        )
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

                    // Large Heading (DS-12 Prompt Exact Text)
                    Text(
                        text = "Ready to make today's viral reel?",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        lineHeight = 34.sp,
                        letterSpacing = (-0.5).sp
                    )

                    // Small Subtitle (DS-12 Prompt Exact Text)
                    Text(
                        text = "Your AI creator assistant is online.",
                        fontSize = 14.5.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // LIVE STATUS BAR (DS-12 Live Status Strip)
                    LiveStatusBar()
                }

                // ==================================================
                // DS-27: AI CREATOR MISSIONS COMPACT HOME WIDGET
                // ==================================================
                CompactCreatorMissionsWidget(
                    onClick = {
                        showCreatorMissionsDialog = true
                    }
                )

                // ==================================================
                // PRIMARY TOOLS (2 Premium Glass Cards)
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

                    // RIGHT TILE: AI VIDEO EDITOR
                    EditVideoAppleCard(
                        modifier = Modifier.weight(1f),
                        testTag = "tile_edit_video",
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isAnalysisMode = false
                            Log.d("VIRI_DEBUG", "LOG: Gallery opened")
                            mediaPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                            )
                        }
                    )
                }

                // ==================================================
                // RECENT PROJECTS (Horizontal Premium Glass Cards)
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
                            text = "Recent Projects",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = "View All",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CyanAccent,
                            modifier = Modifier.clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onNavigateToProjects()
                            }
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(end = 8.dp)
                    ) {
                        items(recentProjects) { project ->
                            RecentProjectCard(
                                project = project,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onNavigateToProjects()
                                }
                            )
                        }
                    }
                }

                // ==================================================
                // QUICK TOOLS (2 Column Premium Grid with Distinct Colors)
                // ==================================================
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Quick Creator Tools",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            QuickToolTile(
                                title = "Daily Planner",
                                subtitle = "Tasks & Schedule",
                                icon = Icons.Outlined.CalendarToday,
                                iconColor = Color(0xFFFF9800), // Amber
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    showCreatorPlannerDialog = true
                                }
                            )

                            QuickToolTile(
                                title = "100+ Viral Hooks",
                                subtitle = "Scroll-stopping ideas",
                                icon = Icons.Outlined.AutoAwesome,
                                iconColor = Color(0xFFA855F7), // Purple
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    showHookLibraryDialog = true
                                }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            QuickToolTile(
                                title = "Caption AI",
                                subtitle = "Instant viral captions",
                                icon = Icons.Outlined.Subtitles,
                                iconColor = Color(0xFF10B981), // Emerald
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    showScriptBuilderDialog = true
                                }
                            )

                            QuickToolTile(
                                title = "Thumbnail AI",
                                subtitle = "High CTR covers",
                                icon = Icons.Outlined.Image,
                                iconColor = Color(0xFFEC4899), // Pink
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    showScriptBuilderDialog = true
                                }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            QuickToolTile(
                                title = "Trend Finder",
                                subtitle = "Trending audio & topics",
                                icon = Icons.Outlined.TrendingUp,
                                iconColor = Color(0xFF3B82F6), // Blue
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onNavigateToAiLab()
                                }
                            )

                            QuickToolTile(
                                title = "Product Detector",
                                subtitle = "Affiliate tag links",
                                icon = Icons.Outlined.ShoppingBag,
                                iconColor = Color(0xFFF59E0B), // Gold
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    showShoppingInsightsDialog = true
                                }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            QuickToolTile(
                                title = "Posting Time",
                                subtitle = "Peak engagement slot",
                                icon = Icons.Outlined.Schedule,
                                iconColor = Color(0xFF06B6D4), // Cyan
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onNavigateToAiLab()
                                }
                            )

                            QuickToolTile(
                                title = "Creator Notes",
                                subtitle = "Scripts & ideas",
                                icon = Icons.Outlined.EditNote,
                                iconColor = Color(0xFF8B5CF6), // Violet
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onNavigateToProjects()
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

        if (showCreatorPlannerDialog) {
            HomeToolModal(
                title = "Daily Planner",
                icon = Icons.Outlined.CalendarToday,
                onDismiss = { showCreatorPlannerDialog = false }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Schedule today's reel posting and content goals.",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Button(
                        onClick = { showCreatorPlannerDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text("Open Full Planner", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (showHookLibraryDialog) {
            HomeToolModal(
                title = "100+ Viral Hooks",
                icon = Icons.Outlined.AutoAwesome,
                onDismiss = { showHookLibraryDialog = false }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val hooks = listOf(
                        "Stop doing this if you want 10k followers...",
                        "Nobody is talking about this secret hack...",
                        "I tried this for 7 days and here is what happened...",
                        "Save this before it gets deleted!"
                    )
                    hooks.forEach { hook ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TileSurface,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = "• \"$hook\"",
                                fontSize = 13.sp,
                                color = TextPrimary,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }

        if (showScriptBuilderDialog) {
            HomeToolModal(
                title = "AI Captions & Script",
                icon = Icons.Outlined.Subtitles,
                onDismiss = { showScriptBuilderDialog = false }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Auto-generate engaging captions and spoken scripts.",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Button(
                        onClick = {
                            showScriptBuilderDialog = false
                            onNavigateToAiLab()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text("Generate AI Captions", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (showShoppingInsightsDialog) {
            HomeToolModal(
                title = "Product Detector",
                icon = Icons.Outlined.ShoppingBag,
                onDismiss = { showShoppingInsightsDialog = false }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Auto-detect products in your reel for Meesho/Amazon affiliate links.",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Button(
                        onClick = { showShoppingInsightsDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text("Detect Products Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (showSettingsDialog) {
            HomeToolModal(
                title = "Settings",
                icon = Icons.Default.Settings,
                onDismiss = { showSettingsDialog = false }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("ViralToolAI Version 2.5 (Pro)", fontSize = 14.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text("Mascot Level: Lvl ${ViriPrefs.getLevel(context).levelNum}", fontSize = 13.sp, color = CyanAccent)
                    Button(
                        onClick = { showSettingsDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = TileSurface, contentColor = TextPrimary),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text("Close Settings")
                    }
                }
            }
        }

        if (showImportErrorDialog) {
            HomeToolModal(
                title = "Media Import Error",
                icon = Icons.Default.ErrorOutline,
                onDismiss = { showImportErrorDialog = false }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Unable to process the selected video file. Supported video formats: MP4, MOV, MKV, WEBM, AVI, MPEG.",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Button(
                        onClick = { showImportErrorDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text("OK", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // ==================================================
        // AI REEL ANALYSIS — MASTER FLOW V1 REEL DOCTOR AI
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
// HELPER COMPONENTS FOR MEESHO / APPLE WIDGET DASHBOARD
// ============================================================================

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
                            // Moving beam
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
private fun EditVideoAppleCard(
    modifier: Modifier = Modifier,
    testTag: String = "tile_edit_video",
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.96f else 1f, label = "press_scale")

    val infiniteTransition = rememberInfiniteTransition(label = "timeline_play")
    val playheadX by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "playhead"
    )

    // Card Floating Animation (Slightly phase offset from left card)
    val cardFloatY by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card_float2"
    )

    // Live Waveform Pulse Animation
    val waveHeightMult by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_anim"
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
                // Top: Animated Timeline & Waveform Graphic
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF101216))
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val canvasW = size.width
                        val canvasH = size.height
                        // Track blocks
                        drawRoundRect(
                            color = Color(0xFF2A313F),
                            topLeft = Offset(0f, 0f),
                            size = Size(canvasW * 0.45f, canvasH * 0.45f),
                            cornerRadius = CornerRadius(4.dp.toPx())
                        )
                        drawRoundRect(
                            color = Color(0xFF2A313F),
                            topLeft = Offset(canvasW * 0.5f, 0f),
                            size = Size(canvasW * 0.45f, canvasH * 0.45f),
                            cornerRadius = CornerRadius(4.dp.toPx())
                        )
                        // Audio Waveform Bars
                        val barCount = 12
                        val barW = (canvasW - (barCount - 1) * 2.dp.toPx()) / barCount
                        for (i in 0 until barCount) {
                            val hFactor = if (i % 2 == 0) waveHeightMult else (1.4f - waveHeightMult)
                            val barH = (canvasH * 0.35f * hFactor).coerceIn(2.dp.toPx(), canvasH * 0.45f)
                            drawRoundRect(
                                color = CyanAccent.copy(alpha = 0.6f),
                                topLeft = Offset(i * (barW + 2.dp.toPx()), canvasH * 0.55f + (canvasH * 0.45f - barH) / 2),
                                size = Size(barW, barH),
                                cornerRadius = CornerRadius(1.dp.toPx())
                            )
                        }
                        // Moving Playhead
                        drawLine(
                            color = CyanAccent,
                            start = Offset(canvasW * playheadX, 0f),
                            end = Offset(canvasW * playheadX, canvasH),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                }

                // Middle: Text Info
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "AI Video Editor",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Trim • Caption • Auto Edit",
                        fontSize = 10.5.sp,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }

                // Bottom: CTA Button
                Surface(
                    onClick = onClick,
                    shape = RoundedCornerShape(20.dp),
                    color = TileSurface,
                    border = BorderStroke(1.dp, CyanAccent),
                    modifier = Modifier.fillMaxWidth().height(36.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Outlined.AutoFixHigh, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Open Editor", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentProjectCard(
    project: RecentProjectItem,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f, label = "proj_press")

    val infiniteTransition = rememberInfiniteTransition(label = "proj_float")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "proj_y"
    )

    Surface(
        modifier = Modifier
            .width(160.dp)
            .height(115.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationY = floatOffset
            }
            .clip(RoundedCornerShape(22.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() },
        shape = RoundedCornerShape(22.dp),
        color = CardSurface,
        border = BorderStroke(1.dp, GlassBorder.copy(alpha = 0.4f)),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(TileSurface)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "REEL",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanAccent
                    )
                }

                Text(
                    text = project.duration,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = project.title,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = "Continue Editing",
                        tint = CyanAccent,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Continue Editing",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CyanAccent
                    )
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
            "🎯 Hook Database Loaded",
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

@Composable
private fun QuickToolTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f, label = "press_scale")

    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_y"
    )

    Surface(
        modifier = modifier
            .height(110.dp)
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
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
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
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun HomeToolModal(
    title: String,
    icon: ImageVector,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(24.dp)),
            color = CardSurface,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(TileSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icon, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(18.dp))
                        }
                        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary, modifier = Modifier.size(20.dp))
                    }
                }

                content()
            }
        }
    }
}
