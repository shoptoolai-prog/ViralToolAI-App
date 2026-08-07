package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.ui.components.ViralToolAiLogo

private val DarkBg = Color(0xFF0B0B0B)
private val PrimaryPurple = Color(0xFF20D9E8)
private val AccentGreen = Color(0xFF20D9E8)
private val CardSurface = Color(0xFF141414)
private val CardBorder = Color(0xFF1B1B1B)
private val TextWhite = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFB7B7B7)

data class AiLabProject(
    val id: String,
    val name: String,
    val lastEdited: String,
    val aiScore: Int,
    val platform: String,
    val platformColor: Color,
    val isDraft: Boolean = true,
    val category: String = "Review",
    val thumbnailGradient: List<Color>
)

data class ProcessingTask(
    val title: String,
    val status: String,
    val progress: Float,
    val timeRemaining: String,
    val icon: ImageVector
)

data class AiThumbnailPreset(
    val title: String,
    val style: String,
    val ctrBoost: String,
    val accentColor: Color
)

data class AiReportItem(
    val title: String,
    val date: String,
    val score: String,
    val summary: String,
    val category: String
)

/**
 * AI LABS SCREEN — REDESIGNED CREATOR HUB
 * Features:
 * • Draft Projects
 * • Recent Projects
 * • AI Processing Queue
 * • AI Generated Thumbnails
 * • Saved AI Reports
 * • Continue Editing
 * • Premium Empty State option
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiLabScreen(
    onNavigateToHistory: () -> Unit = {},
    onNavigateToAnalysis: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()

    var showEmptyStateDemo by remember { mutableStateOf(false) }

    // Video Picker Launcher
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            Toast.makeText(context, "Opening video for AI Project Creation...", Toast.LENGTH_SHORT).show()
        }
    }

    // Default projects dataset
    val draftProjects = remember {
        listOf(
            AiLabProject("1", "Meesho Kurti Unboxing Reel", "12m ago", 96, "Meesho", Color(0xFFE91E63), isDraft = true, thumbnailGradient = listOf(Color(0xFF881337), Color(0xFF4C0519))),
            AiLabProject("2", "Amazon Earbuds Sound Test", "45m ago", 92, "Amazon", Color(0xFFFF9900), isDraft = true, thumbnailGradient = listOf(Color(0xFF78350F), Color(0xFF451A03))),
            AiLabProject("3", "Flipkart Saree Drape Review", "3h ago", 89, "Flipkart", Color(0xFF2874F0), isDraft = true, thumbnailGradient = listOf(Color(0xFF1E3A8A), Color(0xFF172554)))
        )
    }

    val recentProjects = remember {
        listOf(
            AiLabProject("4", "Glow Serum Skincare Routine", "Yesterday", 94, "Instagram", Color(0xFFE1306C), isDraft = false, thumbnailGradient = listOf(Color(0xFF831843), Color(0xFF500724))),
            AiLabProject("5", "Kitchen Mixer Speed Ramp Demo", "2 days ago", 88, "YouTube", Color(0xFFFF0000), isDraft = false, thumbnailGradient = listOf(Color(0xFF7F1D1D), Color(0xFF450A0A))),
            AiLabProject("6", "Denim Jacket Try-On Haul", "4 days ago", 91, "Meesho", Color(0xFFE91E63), isDraft = false, thumbnailGradient = listOf(Color(0xFF701A75), Color(0xFF4A044E)))
        )
    }

    val processingQueue = remember {
        listOf(
            ProcessingTask("Smartwatch Review - Auto Captioning", "Processing Audio...", 0.72f, "18s left", Icons.Outlined.Subtitles),
            ProcessingTask("Lipstick Swatch - Background Removal", "Isolating Object...", 0.35f, "42s left", Icons.Outlined.ContentCut),
            ProcessingTask("Air Fryer Demo - Price Sticker Placement", "Queued in Line", 0.0f, "Queued", Icons.Outlined.Sell)
        )
    }

    val thumbnailPresets = remember {
        listOf(
            AiThumbnailPreset("4K Zoom + Glow Tag", "High CTR Fashion", "+38% CTR", PrimaryPurple),
            AiThumbnailPreset("Split Before / After", "Skincare & Beauty", "+45% CTR", AccentGreen),
            AiThumbnailPreset("Price Tag + Flash Border", "Affiliate Deals", "+52% CTR", Color(0xFFF59E0B)),
            AiThumbnailPreset("Cinematic Portrait Blur", "Lifestyle Vlogs", "+29% CTR", Color(0xFF3B82F6))
        )
    }

    val savedReports = remember {
        listOf(
            AiReportItem("Meesho Kurti Viral Hook Audit", "Today", "96/100", "Hook retention increased by 3.2x with subtitle popups.", "Hook Retention"),
            AiReportItem("Audio Clarity & Noise Reduction Report", "Yesterday", "91/100", "Background AC noise reduced by 98.4% seamlessly.", "Audio Processing"),
            AiReportItem("Amazon Affiliate Conversion Score", "3 days ago", "88/100", "Price sticker placement increased click intention.", "Affiliate Reach")
        )
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
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
                .padding(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ViralToolAiLogo(size = 28.dp)

                        Text(
                            text = "AI Labs",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    Text(
                        text = "Creator Intelligence & Video Pipeline",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                // Demo State Toggle Button
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = CardSurface,
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.clickable {
                        showEmptyStateDemo = !showEmptyStateDemo
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (showEmptyStateDemo) Color(0xFFEF4444) else AccentGreen)
                        )
                        Text(
                            text = if (showEmptyStateDemo) "Show Demo Data" else "Test Empty State",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite
                        )
                    }
                }
            }

            if (showEmptyStateDemo) {
                // ==================================================
                // PREMIUM EMPTY STATE FOR DRAFT PROJECTS
                // ==================================================
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .border(1.dp, CardBorder, RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    color = CardSurface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(PrimaryPurple.copy(alpha = 0.15f))
                                .border(1.dp, PrimaryPurple.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FolderOpen,
                                contentDescription = "Empty Drafts",
                                tint = PrimaryPurple,
                                modifier = Modifier.size(38.dp)
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "No Draft Projects Yet",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "Import your product video or photos to launch the AI Creator Studio and generate high-converting review reels.",
                                fontSize = 13.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }

                        Button(
                            onClick = {
                                videoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = TextWhite)
                                Text("Create New Project", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            }
                        }
                    }
                }
            } else {
                // ==================================================
                // 1. CONTINUE EDITING (HERO DRAFT CARD)
                // ==================================================
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Continue Editing", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                    val topDraft = draftProjects.first()
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(26.dp))
                            .border(1.dp, PrimaryPurple.copy(alpha = 0.5f), RoundedCornerShape(26.dp))
                            .clickable {
                                videoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                )
                            },
                        shape = RoundedCornerShape(26.dp),
                        color = CardSurface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Thumbnail Visual
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Brush.linearGradient(topDraft.thumbnailGradient)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PlayCircle, contentDescription = null, tint = TextWhite.copy(alpha = 0.85f), modifier = Modifier.size(32.dp))
                                Surface(
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(6.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    color = topDraft.platformColor
                                ) {
                                    Text(topDraft.platform, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextWhite, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                }
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = topDraft.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(shape = RoundedCornerShape(8.dp), color = AccentGreen.copy(alpha = 0.15f)) {
                                        Text("${topDraft.aiScore} AI Score", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGreen, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                    Text(topDraft.lastEdited, fontSize = 11.sp, color = TextSecondary)
                                }

                                Button(
                                    onClick = {
                                        videoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(36.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Open in Studio", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                }
                            }
                        }
                    }
                }

                // ==================================================
                // 2. DRAFT PROJECTS
                // ==================================================
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Draft Projects", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text("${draftProjects.size} Drafts", fontSize = 12.sp, color = PrimaryPurple, fontWeight = FontWeight.SemiBold)
                    }

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        items(draftProjects) { project ->
                            AiProjectCardItem(
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

                // ==================================================
                // 3. RECENT PROJECTS
                // ==================================================
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Recent Projects", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        items(recentProjects) { project ->
                            AiProjectCardItem(
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

                // ==================================================
                // 4. AI PROCESSING QUEUE
                // ==================================================
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("AI Processing Queue", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Surface(shape = RoundedCornerShape(10.dp), color = PrimaryPurple.copy(alpha = 0.15f)) {
                            Text("Active Engine", fontSize = 11.sp, color = PrimaryPurple, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        processingQueue.forEach { task ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
                                shape = RoundedCornerShape(20.dp),
                                color = CardSurface
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(PrimaryPurple.copy(alpha = 0.2f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(task.icon, contentDescription = null, tint = PrimaryPurple, modifier = Modifier.size(18.dp))
                                            }
                                            Column {
                                                Text(task.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                                Text(task.status, fontSize = 11.sp, color = TextSecondary)
                                            }
                                        }
                                        Text(task.timeRemaining, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGreen)
                                    }

                                    LinearProgressIndicator(
                                        progress = { task.progress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(CircleShape),
                                        color = PrimaryPurple,
                                        trackColor = CardBorder
                                    )
                                }
                            }
                        }
                    }
                }

                // ==================================================
                // 5. AI GENERATED THUMBNAILS
                // ==================================================
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("AI Generated Thumbnails", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(thumbnailPresets) { preset ->
                            Surface(
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(115.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .border(1.dp, CardBorder, RoundedCornerShape(20.dp))
                                    .clickable {
                                        Toast.makeText(context, "Applying ${preset.title} preset...", Toast.LENGTH_SHORT).show()
                                    },
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
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Icon(Icons.Default.Image, contentDescription = null, tint = preset.accentColor, modifier = Modifier.size(20.dp))
                                        Surface(shape = RoundedCornerShape(6.dp), color = preset.accentColor.copy(alpha = 0.2f)) {
                                            Text(preset.ctrBoost, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = preset.accentColor, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                        }
                                    }

                                    Column {
                                        Text(preset.title, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextWhite, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        Text(preset.style, fontSize = 10.5.sp, color = TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    }
                                }
                            }
                        }
                    }
                }

                // ==================================================
                // 6. SAVED AI REPORTS
                // ==================================================
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Saved AI Reports", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        savedReports.forEach { report ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .border(1.dp, CardBorder, RoundedCornerShape(20.dp))
                                    .clickable {
                                        onNavigateToHistory()
                                    },
                                shape = RoundedCornerShape(20.dp),
                                color = CardSurface
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(report.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite, modifier = Modifier.weight(1f))
                                        Surface(shape = RoundedCornerShape(8.dp), color = AccentGreen.copy(alpha = 0.15f)) {
                                            Text(report.score, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGreen, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }

                                    Text(report.summary, fontSize = 12.sp, color = TextSecondary, lineHeight = 16.sp)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(report.category, fontSize = 10.5.sp, color = PrimaryPurple, fontWeight = FontWeight.SemiBold)
                                        Text(report.date, fontSize = 10.5.sp, color = TextSecondary)
                                    }
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
private fun AiProjectCardItem(
    project: AiLabProject,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(180.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, CardBorder, RoundedCornerShape(22.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
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
                    shape = RoundedCornerShape(6.dp),
                    color = project.platformColor.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = project.platform,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = project.platformColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Text(project.lastEdited, fontSize = 10.sp, color = TextSecondary)
            }

            Text(
                text = project.name,
                fontSize = 13.sp,
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
                Text("${project.aiScore} AI Score", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGreen)
            }
        }
    }
}
