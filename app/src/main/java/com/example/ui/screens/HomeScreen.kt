package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay

data class HomeRecentProject(
    val id: String,
    val name: String,
    val duration: String,
    val type: String
)

data class AiHomeTool(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val accentColor: Color
)

/**
 * VIRALTOOLAI — HOME SCREEN REDESIGN (MASTER PHASE 1)
 * Workflow-focused creator home screen with CapCut-style density and modern M3 glass styling.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProjects: () -> Unit = {},
    onNavigateToAiLab: () -> Unit = {},
    onNavigateToAcademy: (() -> Unit)? = null,
    onNavigateToMediaPicker: () -> Unit = {},
    initialSharedUrl: String? = null
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()
    val responsiveMetrics = LocalResponsiveMetrics.current

    // Media Import Launchers
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            Toast.makeText(context, "Video imported successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            Toast.makeText(context, "Photo imported successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    // Modal dialog states for AI Tools
    var activeToolDialog by remember { mutableStateOf<String?>(null) } // "CAPTION", "REEL_ANALYZER", "REMOVE_BG", "RESIZE", "THUMBNAIL", "PROMPT_STUDIO"

    // Recent Projects State
    var recentProjects by remember {
        mutableStateOf(
            listOf(
                HomeRecentProject("rp1", "Viral Reel Draft #1", "0:30", "Video"),
                HomeRecentProject("rp2", "Product Review Edit", "1:15", "Video"),
                HomeRecentProject("rp3", "Travel Vlog Cut", "0:45", "Video")
            )
        )
    }

    // Six AI Creator Tools
    val homeAiTools = remember {
        listOf(
            AiHomeTool("tool_caption", "AI Caption Generator", Icons.Default.ClosedCaption, Color(0xFF00E5FF)),
            AiHomeTool("tool_reel_analyzer", "AI Reel Analyzer", Icons.Default.Analytics, ElectricPurple),
            AiHomeTool("tool_remove_bg", "Remove Background", Icons.Default.ContentCut, EmeraldGlow),
            AiHomeTool("tool_resize", "Video Resize", Icons.Default.AspectRatio, Color(0xFFFFB703)),
            AiHomeTool("tool_thumbnail", "AI Thumbnail", Icons.Default.PhotoFilter, Color(0xFFFF007F)),
            AiHomeTool("tool_prompt_studio", "Prompt Studio", Icons.Default.AutoAwesome, VioletGlow)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = responsiveMetrics.cardMaxWidth)
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = responsiveMetrics.horizontalPadding)
                .padding(top = 8.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ==================================================
            // 1. CLEAN PREMIUM HEADER
            // Search, Notification, and Settings icons completely removed.
            // Contains ONLY ViralToolAi Logo, Brand Text, and Soft Violet Glow.
            // ==================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = VioletPrimary.copy(alpha = 0.5f),
                        ambientColor = Color.Black
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1E0E3E),
                                Color(0xFF120828),
                                Color(0xFF0A0518)
                            )
                        )
                    )
                    .border(
                        BorderStroke(
                            1.dp,
                            Brush.linearGradient(
                                colors = listOf(
                                    VioletPrimary.copy(alpha = 0.8f),
                                    VioletGlow.copy(alpha = 0.6f),
                                    ElectricPurple.copy(alpha = 0.3f)
                                )
                            )
                        ),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Official Logo with Soft Violet Aura Glow
                        Box(contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                VioletPrimary.copy(alpha = 0.6f),
                                                ElectricPurple.copy(alpha = 0.2f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )

                            androidx.compose.foundation.Image(
                                painter = painterResource(id = com.example.R.drawable.ic_viraltool_icon),
                                contentDescription = "ViralToolAi Logo",
                                modifier = Modifier
                                    .size(32.dp)
                                    .shadow(6.dp, CircleShape, spotColor = VioletPrimary)
                            )
                        }

                        // Premium Typography
                        Text(
                            text = androidx.compose.ui.text.buildAnnotatedString {
                                append("ViralTool")
                                withStyle(
                                    style = androidx.compose.ui.text.SpanStyle(
                                        color = VioletGlow,
                                        shadow = androidx.compose.ui.graphics.Shadow(
                                            color = VioletPrimary,
                                            blurRadius = 10f
                                        )
                                    )
                                ) {
                                    append("Ai")
                                }
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = (-0.3).sp
                        )
                    }
                }
            }

            // ==================================================
            // 2. HERO BANNER
            // ==================================================
            HomeBannerCarousel(
                modifier = Modifier.fillMaxWidth()
            )

            // ==================================================
            // 3. PRIMARY ACTIONS (IMPORT VIDEO & IMPORT PHOTO)
            // Large adaptive cards with large icons & soft shadows
            // ==================================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Import Video Card
                PrimaryActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Import Video",
                    subtitle = "Gallery or Camera",
                    icon = Icons.Default.VideoCall,
                    gradient = listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9)),
                    accentColor = VioletGlow,
                    onClick = {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onNavigateToMediaPicker()
                    }
                )

                // Import Photo Card
                PrimaryActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Import Photo",
                    subtitle = "Image or Thumbnail",
                    icon = Icons.Default.AddPhotoAlternate,
                    gradient = listOf(Color(0xFF06B6D4), Color(0xFF0891B2)),
                    accentColor = Color(0xFF00E5FF),
                    onClick = {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onNavigateToMediaPicker()
                    }
                )
            }

            // ==================================================
            // 4. RECENT PROJECTS (HORIZONTAL SCROLLING)
            // ==================================================
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📁 Recent Projects",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    if (recentProjects.isNotEmpty()) {
                        Text(
                            text = "View All",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGlow,
                            modifier = Modifier.clickable { onNavigateToProjects() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (recentProjects.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF121624))
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No recent projects.",
                            fontSize = 13.sp,
                            color = TextGray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(recentProjects, key = { it.id }) { project ->
                            RecentProjectCard(
                                project = project,
                                onContinue = {
                                    Toast.makeText(context, "Resuming ${project.name}", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }

            // ==================================================
            // 5. AI CREATOR TOOLS (3 COLUMNS × 2 ROWS GRID)
            // Display ONLY the six required AI tools.
            // ==================================================
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "✨ AI Creator Tools",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    homeAiTools.take(3).forEach { tool ->
                        Box(modifier = Modifier.weight(1f)) {
                            AiGridHomeToolCard(tool = tool) {
                                activeToolDialog = tool.id
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    homeAiTools.drop(3).take(3).forEach { tool ->
                        Box(modifier = Modifier.weight(1f)) {
                            AiGridHomeToolCard(tool = tool) {
                                activeToolDialog = tool.id
                            }
                        }
                    }
                }
            }
        }

        // TOOL MODAL DIALOGS
        activeToolDialog?.let { dialogId ->
            when (dialogId) {
                "tool_prompt_studio" -> {
                    ViralToolAiStudioDialog(
                        onDismiss = { activeToolDialog = null }
                    )
                }
                "tool_reel_analyzer" -> {
                    Dialog(onDismissRequest = { activeToolDialog = null }) {
                        AiReelAnalyzerCard()
                    }
                }
                else -> {
                    // Universal Tool Dialog for Captions, Remove BG, Resize, Thumbnail
                    UniversalToolPopup(
                        toolId = dialogId,
                        onDismiss = { activeToolDialog = null }
                    )
                }
            }
        }
    }
}

/**
 * Large Primary Action Card (Import Video / Photo)
 */
@Composable
private fun PrimaryActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradient: List<Color>,
    accentColor: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1.0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "primaryActionScale"
    )

    Surface(
        modifier = modifier
            .height(115.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(12.dp, shape = RoundedCornerShape(22.dp), spotColor = accentColor)
            .clip(RoundedCornerShape(22.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = accentColor)
            ) {
                onClick()
            },
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFF121624),
        border = BorderStroke(1.dp, Brush.linearGradient(gradient.map { it.copy(alpha = 0.5f) }))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            gradient[0].copy(alpha = 0.25f),
                            Color(0xFF0D101C)
                        )
                    )
                )
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(gradient)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = 10.5.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

/**
 * Recent Project Horizontal Card
 */
@Composable
private fun RecentProjectCard(
    project: HomeRecentProject,
    onContinue: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(170.dp)
            .height(110.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF121624),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ElectricPurple.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = "Project",
                        tint = ElectricPurple,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = project.duration,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGlow
                )
            }

            Text(
                text = project.name,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Continue Editing",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * AI Creator Tool Card (3x2 Grid)
 * Large icon, small title, no description, no border, adaptive spacing.
 */
@Composable
private fun AiGridHomeToolCard(
    tool: AiHomeTool,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1.0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "aiToolGridScale"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(8.dp, shape = RoundedCornerShape(18.dp), spotColor = tool.accentColor)
            .clip(RoundedCornerShape(18.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = tool.accentColor)
            ) {
                onClick()
            },
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF141824)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                tool.accentColor.copy(alpha = 0.35f),
                                tool.accentColor.copy(alpha = 0.15f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = tool.icon,
                    contentDescription = tool.title,
                    tint = tool.accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = tool.title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 13.sp
            )
        }
    }
}
