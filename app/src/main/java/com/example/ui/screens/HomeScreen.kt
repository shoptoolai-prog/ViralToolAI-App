package com.example.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.core.MediaImportHelper
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*

// ==================================================
// VIRALTOOLAI COLOR PALETTE — APPLE CREATOR THEME
// ==================================================
private val DarkBg = Color(0xFF080808)
private val PrimaryPurple = Color(0xFF7C3AED)
private val AccentGreen = Color(0xFF22C55E)
private val CardSurface = Color(0xFF141414)
private val CardBorder = Color(0xFF242424)
private val TextWhite = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFB3B3B3)

/**
 * Press scale feedback modifier for smooth 220ms touch interaction
 */
@Composable
private fun Modifier.pressScale(onClick: (() -> Unit)? = null): Modifier {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
        label = "press_scale"
    )
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(160)
            isPressed = false
        }
    }
    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .then(
            if (onClick != null) {
                Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    isPressed = true
                    onClick()
                }
            } else {
                Modifier
            }
        )
}

/**
 * VIRALTOOLAI — MASTER HOME REDESIGN (PHASE H2)
 * iPhone Productivity Style Creator Hub
 */
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
    val responsiveMetrics = LocalResponsiveMetrics.current
    val scope = rememberCoroutineScope()

    var isImportingMedia by remember { mutableStateOf(false) }
    var showImportErrorDialog by remember { mutableStateOf(false) }

    // Pending import config & category for AI Creator Wizard flow
    var pendingImportConfig by remember { mutableStateOf<ProjectSetupConfig?>(null) }
    var selectedCategoryForWizard by remember { mutableStateOf("Fashion") }
    var showAiWizard by remember { mutableStateOf(false) }
    var showAiAnalysis by remember { mutableStateOf(false) }
    var showAiOptimization by remember { mutableStateOf(false) }
    var analysisConfig by remember { mutableStateOf<ProjectSetupConfig?>(null) }

    // Sample list of projects (set empty to test empty state if desired)
    var projectList by remember {
        mutableStateOf(
            listOf(
                ProjectItemData("Meesho Kurti Review", "Meesho", "94 AI Score", "2h ago", Color(0xFFE91E63)),
                ProjectItemData("Wireless Earbuds Reel", "Instagram", "91 AI Score", "Yesterday", Color(0xFF9C27B0)),
                ProjectItemData("Glow Serum Shorts", "YouTube", "88 AI Score", "3d ago", Color(0xFFFF0000))
            )
        )
    }

    // Video Picker Launcher
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            isImportingMedia = true
            scope.launch {
                try {
                    val importedItems = MediaImportHelper.importVideoUris(context, uris)
                    isImportingMedia = false
                    if (importedItems.isNotEmpty()) {
                        val config = MediaImportHelper.createDefaultProjectConfig(importedItems)
                        pendingImportConfig = config
                        showAiWizard = true
                    } else {
                        Toast.makeText(context, "Unable to open selected media.", Toast.LENGTH_LONG).show()
                        showImportErrorDialog = true
                    }
                } catch (e: Throwable) {
                    isImportingMedia = false
                    Toast.makeText(context, "Unable to open selected media.", Toast.LENGTH_LONG).show()
                    showImportErrorDialog = true
                }
            }
        }
    }

    // Photo Picker Launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            isImportingMedia = true
            scope.launch {
                try {
                    val item = MediaImportHelper.importVideoUri(context, it)
                    isImportingMedia = false
                    if (item != null) {
                        val config = MediaImportHelper.createDefaultProjectConfig(listOf(item))
                        pendingImportConfig = config
                        showAiWizard = true
                    } else {
                        Toast.makeText(context, "Unable to open selected photo.", Toast.LENGTH_LONG).show()
                        showImportErrorDialog = true
                    }
                } catch (e: Throwable) {
                    isImportingMedia = false
                    Toast.makeText(context, "Unable to open selected photo.", Toast.LENGTH_LONG).show()
                    showImportErrorDialog = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = responsiveMetrics.cardMaxWidth)
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ==================================================
            // TOP HEADER (Clean Brand Title)
            // ==================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(PrimaryPurple.copy(alpha = 0.2f))
                            .border(1.dp, PrimaryPurple.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "ViralToolAI Logo",
                            tint = TextWhite,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = androidx.compose.ui.text.buildAnnotatedString {
                            append("ViralTool")
                            withStyle(
                                style = androidx.compose.ui.text.SpanStyle(
                                    color = PrimaryPurple,
                                    fontWeight = FontWeight.Black
                                )
                            ) {
                                append("AI")
                            }
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        letterSpacing = (-0.5).sp
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "AI Creator Workspace",
                    fontSize = 11.5.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
            }

            // ==================================================
            // 1. HERO CARD (Reduced height ~20%, cleaner layout)
            // ==================================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        BorderStroke(
                            1.dp,
                            Brush.verticalGradient(
                                colors = listOf(
                                    PrimaryPurple.copy(alpha = 0.5f),
                                    CardBorder
                                )
                            )
                        ),
                        RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp),
                color = CardSurface
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF1A102E),
                                    CardSurface
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Title & Subtitle block
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(AccentGreen)
                                )
                                Text(
                                    text = "AI Review Studio",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentGreen,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Text(
                                text = "Create Your Next Viral Review",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )

                            Text(
                                text = "Upload your review video and let AI optimize it before publishing.",
                                fontSize = 12.5.sp,
                                color = TextSecondary,
                                lineHeight = 17.sp
                            )
                        }

                        // Buttons (48dp height, 16dp rounded corners)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Primary Button: Import Video
                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                    videoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .pressScale(),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudUpload,
                                        contentDescription = "Import Video",
                                        tint = TextWhite,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Import Video",
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }
                            }

                            // Secondary Button: Import Images
                            OutlinedButton(
                                onClick = {
                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .pressScale(),
                                border = BorderStroke(1.dp, CardBorder),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF1A1A1A)),
                                contentPadding = PaddingValues(horizontal = 12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AddPhotoAlternate,
                                        contentDescription = "Import Images",
                                        tint = TextWhite,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Import Images",
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextWhite
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ==================================================
            // 2. QUICK START (Horizontal Scroll Cards)
            // ==================================================
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Quick Start",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                val quickStartCards = listOf(
                    QuickStartCardItem("Product Review", "🛍️", "Product"),
                    QuickStartCardItem("Unboxing", "📦", "Unboxing"),
                    QuickStartCardItem("Beauty Review", "💄", "Beauty"),
                    QuickStartCardItem("Fashion Review", "👗", "Fashion"),
                    QuickStartCardItem("Kitchen Product", "🍳", "Kitchen"),
                    QuickStartCardItem("Tech Review", "📱", "Tech")
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(quickStartCards) { item ->
                        Surface(
                            modifier = Modifier
                                .width(135.dp)
                                .height(92.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
                                .pressScale {
                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                    selectedCategoryForWizard = item.categoryKey
                                    videoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                    )
                                },
                            shape = RoundedCornerShape(18.dp),
                            color = CardSurface
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(item.emoji, fontSize = 24.sp)
                                Text(
                                    text = item.title,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // ==================================================
            // 3. CONTINUE PROJECTS (Latest 3 Projects or Empty State)
            // ==================================================
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SectionHeading(title = "Continue Projects", actionText = "See All", onActionClick = onNavigateToProjects)

                if (projectList.isEmpty()) {
                    // Empty State Card
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp))
                            .border(1.dp, CardBorder, RoundedCornerShape(22.dp)),
                        shape = RoundedCornerShape(22.dp),
                        color = CardSurface
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryPurple.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Outlined.FolderOpen, contentDescription = null, tint = PrimaryPurple, modifier = Modifier.size(28.dp))
                            }

                            Text(
                                text = "No Projects Yet",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )

                            Text(
                                text = "Import a review video to start optimizing with AI.",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Center
                            )

                            Button(
                                onClick = {
                                    videoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(40.dp)
                            ) {
                                Text("Start New Project", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            }
                        }
                    }
                } else {
                    // Show 3 projects
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp)
                    ) {
                        items(projectList.take(3)) { project ->
                            ProjectCardItem(
                                project = project,
                                onClick = {
                                    videoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // ==================================================
            // 4. AI CREATOR TOOLS (2 Columns Grid, 56dp Icon, Short Title)
            // ==================================================
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "AI Creator Tools",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                val aiToolsList = listOf(
                    AiCreatorToolData("AI Thumbnail", Icons.Outlined.Image, Color(0xFF3B82F6)),
                    AiCreatorToolData("AI Caption", Icons.Outlined.Subtitles, AccentGreen),
                    AiCreatorToolData("AI Viral Score", Icons.Outlined.Analytics, Color(0xFFF59E0B)),
                    AiCreatorToolData("Remove Noise", Icons.Outlined.GraphicEq, Color(0xFFEC4899)),
                    AiCreatorToolData("Price Sticker", Icons.Outlined.Sell, Color(0xFF10B981)),
                    AiCreatorToolData("Logo Placement", Icons.Outlined.BrandingWatermark, Color(0xFF6366F1)),
                    AiCreatorToolData("Background Remove", Icons.Outlined.ContentCut, Color(0xFF8B5CF6)),
                    AiCreatorToolData("Product Detector", Icons.Outlined.CenterFocusWeak, Color(0xFF06B6D4))
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    aiToolsList.chunked(2).forEach { rowTools ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowTools.forEach { tool ->
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(96.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .border(1.dp, CardBorder, RoundedCornerShape(20.dp))
                                        .pressScale {
                                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                            videoPickerLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                            )
                                        },
                                    shape = RoundedCornerShape(20.dp),
                                    color = CardSurface
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        // 56dp Icon Box
                                        Box(
                                            modifier = Modifier
                                                .size(56.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(tool.color.copy(alpha = 0.15f))
                                                .border(1.dp, tool.color.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = tool.icon,
                                                contentDescription = tool.title,
                                                tint = tool.color,
                                                modifier = Modifier.size(26.dp)
                                            )
                                        }

                                        // Short title (No description)
                                        Text(
                                            text = tool.title,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ==================================================
            // 5. TODAY'S INSIGHTS (Premium Card with minimal metric chart)
            // ==================================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .border(1.dp, CardBorder, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                color = CardSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Insights, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(20.dp))
                            Text("Today's Insights", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = AccentGreen.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "+28% Reach",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    // 4 Stat Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InsightStatItem("Today's Exports", "4", Modifier.weight(1f))
                        InsightStatItem("Avg AI Score", "92", Modifier.weight(1f))
                        InsightStatItem("Completed", "3", Modifier.weight(1f))
                        InsightStatItem("Est. Reach", "14.5k", Modifier.weight(1f))
                    }

                    // Minimal Sparkline Chart Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .background(Color(0xFF0D0D0D), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val points = listOf(0.3f, 0.45f, 0.35f, 0.7f, 0.6f, 0.85f, 0.95f)
                            val path = Path()
                            val widthStep = size.width / (points.size - 1)

                            points.forEachIndexed { i, factor ->
                                val x = i * widthStep
                                val y = size.height - (factor * size.height)
                                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                            }

                            drawPath(
                                path = path,
                                color = AccentGreen,
                                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                    }
                }
            }

            // ==================================================
            // 6. BEAUTY / FASHION SHORTCUT ("Made for Creators")
            // ==================================================
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Made for Creators",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "Category Shortcuts",
                        fontSize = 11.5.sp,
                        color = PrimaryPurple,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                val creatorCategories = listOf(
                    CategoryShortcutItem("Beauty", "💄", Color(0xFFE91E63)),
                    CategoryShortcutItem("Fashion", "👗", Color(0xFF9C27B0)),
                    CategoryShortcutItem("Skincare", "✨", Color(0xFF3B82F6)),
                    CategoryShortcutItem("Jewellery", "💎", Color(0xFFF59E0B)),
                    CategoryShortcutItem("Home Decor", "🏺", Color(0xFF10B981))
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(creatorCategories) { cat ->
                        Surface(
                            modifier = Modifier
                                .width(120.dp)
                                .height(100.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
                                .pressScale {
                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                    selectedCategoryForWizard = cat.title
                                    videoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                    )
                                },
                            shape = RoundedCornerShape(18.dp),
                            color = CardSurface
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(cat.color.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(cat.emoji, fontSize = 18.sp)
                                }

                                Text(
                                    text = cat.title,
                                    fontSize = 13.sp,
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
        // AI CREATOR WIZARD OVERLAY
        // ==================================================
        if (showAiWizard) {
            AiCreatorWizardOverlay(
                config = pendingImportConfig,
                onDismiss = { showAiWizard = false },
                onContinueToEditor = { finalConfig ->
                    showAiWizard = false
                    analysisConfig = finalConfig
                    showAiAnalysis = true
                }
            )
        }

        // ==================================================
        // AI ANALYSIS ENGINE OVERLAY (PHASE H4)
        // ==================================================
        if (showAiAnalysis) {
            AiAnalysisOverlay(
                config = analysisConfig ?: pendingImportConfig,
                onDismiss = { showAiAnalysis = false },
                onContinueToOptimization = { finalConfig ->
                    showAiAnalysis = false
                    analysisConfig = finalConfig
                    showAiOptimization = true
                }
            )
        }

        // ==================================================
        // AI OPTIMIZATION PLANNER OVERLAY (PHASE H5)
        // ==================================================
        if (showAiOptimization) {
            AiOptimizationOverlay(
                config = analysisConfig ?: pendingImportConfig,
                onDismiss = { showAiOptimization = false },
                onApplyOptimizations = { finalConfig ->
                    showAiOptimization = false
                    onVideoImportedToEditor(finalConfig)
                }
            )
        }

        // Loading Overlay during media import
        if (isImportingMedia) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    CircularProgressIndicator(color = PrimaryPurple, strokeWidth = 3.dp)
                    Text(
                        text = "Importing Selected Media...",
                        color = TextWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Error Dialog
        if (showImportErrorDialog) {
            AlertDialog(
                onDismissRequest = { showImportErrorDialog = false },
                title = {
                    Text(
                        text = "Unable to import selected media.",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                text = {
                    Text(
                        text = "The selected file could not be opened. Please try again with another video.",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showImportErrorDialog = false
                            videoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text("Try Again", color = TextWhite, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showImportErrorDialog = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                },
                containerColor = CardSurface,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

// ==================================================
// HELPER DATA CLASSES & COMPOSABLES
// ==================================================

private data class QuickStartCardItem(
    val title: String,
    val emoji: String,
    val categoryKey: String
)

private data class AiCreatorToolData(
    val title: String,
    val icon: ImageVector,
    val color: Color
)

private data class CategoryShortcutItem(
    val title: String,
    val emoji: String,
    val color: Color
)

private data class ProjectItemData(
    val name: String,
    val platform: String,
    val score: String,
    val time: String,
    val color: Color
)

@Composable
private fun SectionHeading(
    title: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                fontSize = 13.sp,
                color = PrimaryPurple,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onActionClick() }
            )
        }
    }
}

@Composable
private fun ProjectCardItem(
    project: ProjectItemData,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(175.dp)
            .height(125.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, CardBorder, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = CardSurface
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
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = project.color.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = project.platform,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = project.color,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Text(project.time, fontSize = 10.5.sp, color = TextSecondary)
            }

            Text(
                text = project.name,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(13.dp))
                Text(project.score, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = AccentGreen)
            }
        }
    }
}

@Composable
private fun InsightStatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(value, fontSize = 17.sp, fontWeight = FontWeight.Black, color = TextWhite)
        Text(label, fontSize = 10.5.sp, color = TextSecondary, fontWeight = FontWeight.Normal)
    }
}

// ==================================================
// AI CREATOR WIZARD OVERLAY (PHASE H3)
// 6-step Apple-Style Bottom Sheet Optimization Flow
// ==================================================
@Composable
private fun AiCreatorWizardOverlay(
    config: ProjectSetupConfig?,
    onDismiss: () -> Unit,
    onContinueToEditor: (ProjectSetupConfig) -> Unit
) {
    // Auto-saved progress state across dismissals
    var step by rememberSaveable { mutableIntStateOf(1) }

    // Form states
    var selectedPlatform by rememberSaveable { mutableStateOf("") }
    var productCategory by rememberSaveable { mutableStateOf("") }
    var productName by rememberSaveable { mutableStateOf("") }
    var brandName by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var discount by rememberSaveable { mutableStateOf("") }
    var couponCode by rememberSaveable { mutableStateOf("") }
    var affiliateLink by rememberSaveable { mutableStateOf("") }
    var language by rememberSaveable { mutableStateOf("Hindi") }
    var targetAudience by rememberSaveable { mutableStateOf("Both") }

    // Category search query
    var categorySearchQuery by remember { mutableStateOf("") }

    // AI Settings (all ON by default)
    var improveAudio by rememberSaveable { mutableStateOf(true) }
    var removeNoise by rememberSaveable { mutableStateOf(true) }
    var generateThumbnail by rememberSaveable { mutableStateOf(true) }
    var generateCaptions by rememberSaveable { mutableStateOf(true) }
    var detectProduct by rememberSaveable { mutableStateOf(true) }
    var createPriceSticker by rememberSaveable { mutableStateOf(true) }
    var detectBrandLogo by rememberSaveable { mutableStateOf(true) }
    var improveHook by rememberSaveable { mutableStateOf(true) }
    var optimizeInstagram by rememberSaveable { mutableStateOf(true) }

    val haptic = LocalHapticFeedback.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismiss() },
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.95f)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .border(
                        BorderStroke(1.dp, CardBorder),
                        RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* prevent dismiss click inside sheet */ },
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color(0xFF121212)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top Drag Handle & Navigation Bar
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .width(38.dp)
                                .height(5.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.25f))
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Header Bar with Back Button, Step Progress Dots (1..6), Close X
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (step > 1) {
                                IconButton(
                                    onClick = {
                                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                        step--
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = TextWhite,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(36.dp))
                            }

                            // Step Progress Indicator 1..6
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                (1..6).forEach { i ->
                                    val isCurrent = step == i
                                    val isPassed = step > i

                                    Box(
                                        modifier = Modifier
                                            .size(if (isCurrent) 26.dp else 22.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when {
                                                    isCurrent -> PrimaryPurple
                                                    isPassed -> PrimaryPurple.copy(alpha = 0.45f)
                                                    else -> Color(0xFF262626)
                                                }
                                            )
                                            .border(
                                                1.dp,
                                                if (isCurrent) AccentGreen else Color.Transparent,
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (isPassed) "✓" else "$i",
                                            fontSize = if (isCurrent) 12.sp else 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isCurrent || isPassed) TextWhite else TextSecondary
                                        )
                                    }
                                }
                            }

                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Content Container with AnimatedContent Slide (220ms)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        AnimatedContent(
                            targetState = step,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    slideInHorizontally(animationSpec = tween(220)) { width -> width } + fadeIn(tween(220)) togetherWith
                                            slideOutHorizontally(animationSpec = tween(220)) { width -> -width } + fadeOut(tween(220))
                                } else {
                                    slideInHorizontally(animationSpec = tween(220)) { width -> -width } + fadeIn(tween(220)) togetherWith
                                            slideOutHorizontally(animationSpec = tween(220)) { width -> width } + fadeOut(tween(220))
                                }
                            },
                            label = "wizard_step_slide"
                        ) { currentStep ->
                            when (currentStep) {
                                1 -> Step1Welcome()
                                2 -> Step2Platform(
                                    selectedPlatform = selectedPlatform,
                                    onSelectPlatform = { selectedPlatform = it }
                                )
                                3 -> Step3Category(
                                    selectedCategory = productCategory,
                                    onSelectCategory = { productCategory = it },
                                    searchQuery = categorySearchQuery,
                                    onSearchQueryChange = { categorySearchQuery = it }
                                )
                                4 -> Step4Details(
                                    productName = productName, onProductNameChange = { productName = it },
                                    brandName = brandName, onBrandNameChange = { brandName = it },
                                    price = price, onPriceChange = { price = it },
                                    discount = discount, onDiscountChange = { discount = it },
                                    couponCode = couponCode, onCouponCodeChange = { couponCode = it },
                                    affiliateLink = affiliateLink, onAffiliateLinkChange = { affiliateLink = it },
                                    language = language, onLanguageChange = { language = it },
                                    targetAudience = targetAudience, onTargetAudienceChange = { targetAudience = it }
                                )
                                5 -> Step5AiSettings(
                                    improveAudio = improveAudio, onToggleImproveAudio = { improveAudio = it },
                                    removeNoise = removeNoise, onToggleRemoveNoise = { removeNoise = it },
                                    generateThumbnail = generateThumbnail, onToggleGenerateThumbnail = { generateThumbnail = it },
                                    generateCaptions = generateCaptions, onToggleGenerateCaptions = { generateCaptions = it },
                                    detectProduct = detectProduct, onToggleDetectProduct = { detectProduct = it },
                                    createPriceSticker = createPriceSticker, onToggleCreatePriceSticker = { createPriceSticker = it },
                                    detectBrandLogo = detectBrandLogo, onToggleDetectBrandLogo = { detectBrandLogo = it },
                                    improveHook = improveHook, onToggleImproveHook = { improveHook = it },
                                    optimizeInstagram = optimizeInstagram, onToggleOptimizeInstagram = { optimizeInstagram = it }
                                )
                                6 -> Step6ReadySummary(
                                    platform = selectedPlatform,
                                    category = productCategory,
                                    productName = productName,
                                    language = language,
                                    activeAiCount = listOf(
                                        improveAudio, removeNoise, generateThumbnail, generateCaptions,
                                        detectProduct, createPriceSticker, detectBrandLogo, improveHook, optimizeInstagram
                                    ).count { it }
                                )
                            }
                        }
                    }

                    // Bottom Action Button (48dp height)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        val isNextEnabled = when (step) {
                            1 -> true
                            2 -> selectedPlatform.isNotBlank()
                            3 -> productCategory.isNotBlank()
                            4 -> productName.isNotBlank()
                            5 -> true
                            6 -> true
                            else -> true
                        }

                        Button(
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                if (step < 6) {
                                    step++
                                } else {
                                    val finalConfig = config ?: MediaImportHelper.createDefaultProjectConfig(emptyList())
                                    onContinueToEditor(finalConfig)
                                }
                            },
                            enabled = isNextEnabled,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .pressScale(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (step == 6) AccentGreen else PrimaryPurple,
                                disabledContainerColor = Color(0xFF262626)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = if (step == 6) "Start AI Analysis" else "Continue",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!isNextEnabled) TextSecondary else if (step == 6) Color.Black else TextWhite
                            )
                        }
                    }
                }
            }
        }
    }
}

// STEP 1: WELCOME
@Composable
private fun Step1Welcome() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(120.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                PrimaryPurple.copy(alpha = 0.35f),
                                Color.Transparent
                            )
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(PrimaryPurple)
                    .shadow(16.dp, CircleShape, spotColor = PrimaryPurple),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(38.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Let's Build a High Performing Review",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "AI will optimize your content before editing.",
            fontSize = 13.5.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

// STEP 2: PLATFORM
@Composable
private fun Step2Platform(
    selectedPlatform: String,
    onSelectPlatform: (String) -> Unit
) {
    val platforms = listOf(
        "Instagram" to "📸",
        "YouTube Shorts" to "🔴",
        "Facebook" to "🔵",
        "Meesho" to "🛍️",
        "Amazon" to "📦",
        "Flipkart" to "🛒",
        "Myntra" to "👗",
        "Ajio" to "✨",
        "Nykaa" to "💄",
        "Shopify" to "🟢",
        "Other" to "🌐"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Where will you publish?",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                text = "Select one target platform to tailor aspect ratio & AI algorithms.",
                fontSize = 12.5.sp,
                color = TextSecondary
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            platforms.chunked(2).forEach { rowPlatforms ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowPlatforms.forEach { (name, emoji) ->
                        val isSelected = selectedPlatform == name
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) PrimaryPurple else CardBorder,
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable { onSelectPlatform(name) },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) PrimaryPurple.copy(alpha = 0.25f) else CardSurface
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(text = emoji, fontSize = 22.sp)
                                Text(
                                    text = name,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) TextWhite else TextSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                    if (rowPlatforms.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// STEP 3: PRODUCT CATEGORY
@Composable
private fun Step3Category(
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    val allCategories = listOf(
        "Beauty" to "💄",
        "Fashion" to "👗",
        "Skincare" to "✨",
        "Saree" to "🥻",
        "Jewellery" to "💍",
        "Kitchen" to "🍳",
        "Electronics" to "📱",
        "Home Decor" to "🏠",
        "Footwear" to "👠",
        "Other" to "📦"
    )

    val filtered = if (searchQuery.isBlank()) {
        allCategories
    } else {
        allCategories.filter { it.first.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "What are you reviewing?",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                text = "Select a product category for targeted prompts & filters.",
                fontSize = 12.5.sp,
                color = TextSecondary
            )
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Search category...", fontSize = 13.sp, color = TextSecondary) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
            trailingIcon = if (searchQuery.isNotEmpty()) {
                { IconButton(onClick = { onSearchQueryChange("") }) { Icon(Icons.Default.Clear, contentDescription = null, tint = TextSecondary) } }
            } else null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryPurple,
                unfocusedBorderColor = CardBorder,
                focusedContainerColor = CardSurface,
                unfocusedContainerColor = CardSurface
            )
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            filtered.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowItems.forEach { (name, emoji) ->
                        val isSelected = selectedCategory == name
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) PrimaryPurple else CardBorder,
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable { onSelectCategory(name) },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) PrimaryPurple.copy(alpha = 0.25f) else CardSurface
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(text = emoji, fontSize = 20.sp)
                                Text(
                                    text = name,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) TextWhite else TextSecondary
                                )
                            }
                        }
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// STEP 4: DETAILS
@Composable
private fun Step4Details(
    productName: String, onProductNameChange: (String) -> Unit,
    brandName: String, onBrandNameChange: (String) -> Unit,
    price: String, onPriceChange: (String) -> Unit,
    discount: String, onDiscountChange: (String) -> Unit,
    couponCode: String, onCouponCodeChange: (String) -> Unit,
    affiliateLink: String, onAffiliateLinkChange: (String) -> Unit,
    language: String, onLanguageChange: (String) -> Unit,
    targetAudience: String, onTargetAudienceChange: (String) -> Unit
) {
    val languages = listOf("Hindi", "English", "Mixed")
    val audiences = listOf("Male", "Female", "Both")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Product Details",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                text = "Enter product information for automated AI captions & price tags.",
                fontSize = 12.5.sp,
                color = TextSecondary
            )
        }

        OutlinedTextField(
            value = productName,
            onValueChange = onProductNameChange,
            label = { Text("Product Name *", fontSize = 12.sp) },
            placeholder = { Text("e.g. Silk Kurti Set", fontSize = 12.sp, color = TextSecondary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = productName.isBlank(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryPurple,
                unfocusedBorderColor = CardBorder,
                focusedContainerColor = CardSurface,
                unfocusedContainerColor = CardSurface
            )
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = brandName,
                onValueChange = onBrandNameChange,
                label = { Text("Brand", fontSize = 12.sp) },
                placeholder = { Text("e.g. Meesho", fontSize = 12.sp, color = TextSecondary) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = CardBorder,
                    focusedContainerColor = CardSurface,
                    unfocusedContainerColor = CardSurface
                )
            )

            OutlinedTextField(
                value = price,
                onValueChange = onPriceChange,
                label = { Text("Price", fontSize = 12.sp) },
                placeholder = { Text("e.g. ₹999", fontSize = 12.sp, color = TextSecondary) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = CardBorder,
                    focusedContainerColor = CardSurface,
                    unfocusedContainerColor = CardSurface
                )
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = discount,
                onValueChange = onDiscountChange,
                label = { Text("Discount", fontSize = 12.sp) },
                placeholder = { Text("e.g. 20% OFF", fontSize = 12.sp, color = TextSecondary) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = CardBorder,
                    focusedContainerColor = CardSurface,
                    unfocusedContainerColor = CardSurface
                )
            )

            OutlinedTextField(
                value = couponCode,
                onValueChange = onCouponCodeChange,
                label = { Text("Coupon (Optional)", fontSize = 12.sp) },
                placeholder = { Text("e.g. SAVE20", fontSize = 12.sp, color = TextSecondary) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = CardBorder,
                    focusedContainerColor = CardSurface,
                    unfocusedContainerColor = CardSurface
                )
            )
        }

        OutlinedTextField(
            value = affiliateLink,
            onValueChange = onAffiliateLinkChange,
            label = { Text("Affiliate Link (Optional)", fontSize = 12.sp) },
            placeholder = { Text("e.g. https://...", fontSize = 12.sp, color = TextSecondary) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryPurple,
                unfocusedBorderColor = CardBorder,
                focusedContainerColor = CardSurface,
                unfocusedContainerColor = CardSurface
            )
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Language", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                languages.forEach { lang ->
                    val isSel = language == lang
                    FilterChip(
                        selected = isSel,
                        onClick = { onLanguageChange(lang) },
                        label = { Text(lang, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryPurple,
                            selectedLabelColor = TextWhite
                        )
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Target Audience", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                audiences.forEach { aud ->
                    val isSel = targetAudience == aud
                    FilterChip(
                        selected = isSel,
                        onClick = { onTargetAudienceChange(aud) },
                        label = { Text(aud, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryPurple,
                            selectedLabelColor = TextWhite
                        )
                    )
                }
            }
        }
    }
}

// STEP 5: AI SETTINGS
@Composable
private fun Step5AiSettings(
    improveAudio: Boolean, onToggleImproveAudio: (Boolean) -> Unit,
    removeNoise: Boolean, onToggleRemoveNoise: (Boolean) -> Unit,
    generateThumbnail: Boolean, onToggleGenerateThumbnail: (Boolean) -> Unit,
    generateCaptions: Boolean, onToggleGenerateCaptions: (Boolean) -> Unit,
    detectProduct: Boolean, onToggleDetectProduct: (Boolean) -> Unit,
    createPriceSticker: Boolean, onToggleCreatePriceSticker: (Boolean) -> Unit,
    detectBrandLogo: Boolean, onToggleDetectBrandLogo: (Boolean) -> Unit,
    improveHook: Boolean, onToggleImproveHook: (Boolean) -> Unit,
    optimizeInstagram: Boolean, onToggleOptimizeInstagram: (Boolean) -> Unit
) {
    val toggles = listOf(
        "Improve Audio" to ("Enhance clarity & voice levels" to (improveAudio to onToggleImproveAudio)),
        "Remove Noise" to ("AI background noise suppression" to (removeNoise to onToggleRemoveNoise)),
        "Generate Thumbnail" to ("Extract high-CTR thumbnail frame" to (generateThumbnail to onToggleGenerateThumbnail)),
        "Generate Captions" to ("Auto-generate synchronized subtitles" to (generateCaptions to onToggleGenerateCaptions)),
        "Detect Product" to ("Auto-detect product bounding box" to (detectProduct to onToggleDetectProduct)),
        "Create Price Sticker" to ("Overlay custom animated price tag" to (createPriceSticker to onToggleCreatePriceSticker)),
        "Detect Brand Logo" to ("Identify & align brand watermarks" to (detectBrandLogo to onToggleDetectBrandLogo)),
        "Improve Hook" to ("0–3s hook optimization & text callouts" to (improveHook to onToggleImproveHook)),
        "Optimize For Instagram" to ("Apply algorithm viral retention filter" to (optimizeInstagram to onToggleOptimizeInstagram))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "AI Settings",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                text = "Toggle automatic AI enhancements to apply during analysis.",
                fontSize = 12.5.sp,
                color = TextSecondary
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            toggles.forEach { (title, pair) ->
                val (subtitle, statePair) = pair
                val (checked, onCheckedChange) = statePair

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, CardBorder, RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    color = CardSurface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            Text(subtitle, fontSize = 11.sp, color = TextSecondary)
                        }

                        Switch(
                            checked = checked,
                            onCheckedChange = onCheckedChange,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = TextWhite,
                                checkedTrackColor = PrimaryPurple,
                                uncheckedThumbColor = TextSecondary,
                                uncheckedTrackColor = Color(0xFF262626)
                            )
                        )
                    }
                }
            }
        }
    }
}

// STEP 6: READY SUMMARY
@Composable
private fun Step6ReadySummary(
    platform: String,
    category: String,
    productName: String,
    language: String,
    activeAiCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(AccentGreen.copy(alpha = 0.2f))
                .border(1.dp, AccentGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(36.dp))
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Ready for AI Analysis",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                text = "Review your setup before starting AI enhancement.",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            color = CardSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryRow("Publish Platform", platform.ifBlank { "Not specified" })
                HorizontalDivider(color = CardBorder, thickness = 0.5.dp)
                SummaryRow("Product Category", category.ifBlank { "Not specified" })
                HorizontalDivider(color = CardBorder, thickness = 0.5.dp)
                SummaryRow("Product Name", productName.ifBlank { "Untitled Product" })
                HorizontalDivider(color = CardBorder, thickness = 0.5.dp)
                SummaryRow("Language", language)
                HorizontalDivider(color = CardBorder, thickness = 0.5.dp)
                SummaryRow("AI Features Enabled", "$activeAiCount / 9 Active", valueColor = AccentGreen)
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    valueColor: Color = TextWhite
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 13.sp, color = TextSecondary)
        Text(value, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}
