package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import com.example.ui.theme.VioletGlow
import com.example.ui.theme.VioletPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AiPreEditSuggestion(
    val id: String,
    val title: String,
    val description: String,
    val actionLabel: String,
    val category: String,
    val isApplied: Boolean = false
)

data class AspectRatioOption(
    val id: String,
    val ratioLabel: String,
    val platformName: String,
    val isRecommended: Boolean = false,
    val widthRatio: Float,
    val heightRatio: Float
)

data class ProjectSetupConfig(
    val projectName: String,
    val selectedMedia: List<MediaPickerItem>,
    val aspectRatio: String,
    val resolution: String,
    val fps: String,
    val autoCaptionsEnabled: Boolean,
    val aiAudioCleanEnabled: Boolean,
    val smartReframerEnabled: Boolean,
    val autoCutFillersEnabled: Boolean,
    val initialAudioTracks: List<AudioTrackItem> = emptyList(),
    val initialCaptions: List<TextTrackItem> = emptyList(),
    val initialStickers: List<StickerTrackItem> = emptyList(),
    val initialEffectTracks: List<EffectTrackItem> = emptyList(),
    val aiSuggestions: List<AiPreEditSuggestion> = emptyList(),
    val brandLogoApplied: Boolean = false,
    val silenceSections: List<Pair<Double, Double>> = emptyList(),
    val isShakingDetected: Boolean = false,
    val lowBrightnessDetected: Boolean = false,
    val beautyCategoryDetected: Boolean = false,
    val fashionCategoryDetected: Boolean = false,
    val thumbnailUri: Uri? = null,
    val preEditSummaryReady: Boolean = false
)

private val AspectRatios = listOf(
    AspectRatioOption("9_16", "9:16", "Reel / TikTok / Shorts", isRecommended = true, 9f, 16f),
    AspectRatioOption("16_9", "16:9", "YouTube / Landscape", isRecommended = false, 16f, 9f),
    AspectRatioOption("1_1", "1:1", "Instagram Square Post", isRecommended = false, 1f, 1f),
    AspectRatioOption("4_5", "4:5", "Instagram Portrait Feed", isRecommended = false, 4f, 5f)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectSetupScreen(
    initialSelectedMedia: List<MediaPickerItem>,
    onBackToPicker: () -> Unit,
    onStartEditing: (ProjectSetupConfig) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var selectedMediaList by remember { mutableStateOf(initialSelectedMedia) }

    // Auto-generated initial project name based on date
    val defaultProjectName = remember {
        val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        "Viral Reel Project - ${dateFormat.format(Date())}"
    }

    var projectName by remember { mutableStateOf(defaultProjectName) }
    var selectedAspectRatio by remember { mutableStateOf("9:16") }
    var selectedResolution by remember { mutableStateOf("1080p") } // "1080p", "4K", "720p"
    var selectedFps by remember { mutableStateOf("30 FPS") } // "30 FPS", "60 FPS", "24 FPS"

    // Auto save draft to SharedPreferences whenever config changes
    LaunchedEffect(projectName, selectedMediaList, selectedAspectRatio, selectedResolution, selectedFps) {
        val prefs = context.getSharedPreferences("viraltool_drafts", android.content.Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("draft_project_name", projectName)
            putString("draft_aspect_ratio", selectedAspectRatio)
            putString("draft_resolution", selectedResolution)
            putString("draft_fps", selectedFps)
            putString("draft_media_uris", selectedMediaList.mapNotNull { it.uri?.toString() }.joinToString(","))
            putString("draft_media_titles", selectedMediaList.map { it.title }.joinToString(","))
            putLong("draft_timestamp", System.currentTimeMillis())
            apply()
        }
    }

    // AI Enhancements Toggles
    var autoCaptionsEnabled by remember { mutableStateOf(true) }
    var aiAudioCleanEnabled by remember { mutableStateOf(true) }
    var smartReframerEnabled by remember { mutableStateOf(true) }
    var autoCutFillersEnabled by remember { mutableStateOf(false) }

    // Loading State Overlay
    var isInitializingProject by remember { mutableStateOf(false) }
    var initializationProgress by remember { mutableFloatStateOf(0f) }
    var initializationStatusText by remember { mutableStateOf("Creating AI Timeline...") }

    val randomProjectNames = listOf(
        "Viral Fashion Reel #1",
        "Travel B-Roll Edit",
        "4K Cinematic Short",
        "Product Review Reel",
        "Daily Vlog Cut",
        "Streetwear Haul 60fps"
    )

    fun generateRandomName() {
        projectName = randomProjectNames.random()
        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
    }

    val totalDurationSeconds = remember(selectedMediaList) {
        selectedMediaList.sumOf { it.durationSeconds }
    }

    val totalDurationFormatted = remember(totalDurationSeconds) {
        val mins = totalDurationSeconds / 60
        val secs = totalDurationSeconds % 60
        if (mins > 0) "${mins}m ${secs}s" else "${secs}s"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // ==================================================
            // 1. TOP BAR
            // ==================================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF0C0E17),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                onBackToPicker()
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E1F2E))
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = TextWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Project Setup",
                                color = TextWhite,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${selectedMediaList.size} clips selected • $totalDurationFormatted total",
                                color = EmeraldGlow,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Quick AI Preset Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(VioletPrimary.copy(alpha = 0.4f), ElectricPurple.copy(alpha = 0.2f))
                                )
                            )
                            .border(BorderStroke(1.dp, VioletPrimary.copy(alpha = 0.6f)), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = VioletGlow,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "AI Preset Ready",
                                color = TextWhite,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // ==================================================
            // SCROLLABLE SETUP CONTENT
            // ==================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // --------------------------------------------------
                // SECTION 1: SELECTED MEDIA STRIP
                // --------------------------------------------------
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🎬 Selected Clips (${selectedMediaList.size})",
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "+ Add More Clips",
                            color = VioletGlow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onBackToPicker() }
                        )
                    }

                    if (selectedMediaList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF141624))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No clips selected. Please go back and select media.", color = TextGray, fontSize = 12.sp)
                        }
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            itemsIndexed(selectedMediaList) { index, item ->
                                SetupMediaCard(
                                    item = item,
                                    index = index + 1,
                                    onRemove = {
                                        if (selectedMediaList.size <= 1) {
                                            Toast.makeText(context, "At least 1 clip is required for project", Toast.LENGTH_SHORT).show()
                                        } else {
                                            selectedMediaList = selectedMediaList.filterIndexed { i, _ -> i != index }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                // --------------------------------------------------
                // SECTION: REAL VIDEO SPECS DISPLAY CARD
                // --------------------------------------------------
                val primaryMedia = selectedMediaList.firstOrNull()
                if (primaryMedia != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF111322)),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, VioletPrimary.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = EmeraldGlow,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Selected Video Specs",
                                    color = TextWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            HorizontalDivider(color = Color(0xFF21243A))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Video Title:", color = TextGray, fontSize = 10.sp)
                                    Text(primaryMedia.title, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Duration:", color = TextGray, fontSize = 10.sp)
                                    Text(primaryMedia.durationFormatted, color = EmeraldGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Resolution:", color = TextGray, fontSize = 10.sp)
                                    Text("${primaryMedia.width} x ${primaryMedia.height} (${primaryMedia.resolutionLabel})", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Frame Rate:", color = TextGray, fontSize = 10.sp)
                                    Text(primaryMedia.frameRateLabel, color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Aspect Ratio:", color = TextGray, fontSize = 10.sp)
                                    Text(selectedAspectRatio, color = VioletGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("File Size:", color = TextGray, fontSize = 10.sp)
                                    Text(primaryMedia.fileSizeFormatted, color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }

                // --------------------------------------------------
                // SECTION 2: PROJECT NAME FIELD
                // --------------------------------------------------
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "✏️ Project Name",
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = projectName,
                        onValueChange = { projectName = it },
                        placeholder = { Text("Enter project name...", color = TextGray, fontSize = 13.sp) },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = VioletGlow,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        trailingIcon = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(end = 6.dp)
                            ) {
                                IconButton(onClick = { generateRandomName() }) {
                                    Icon(
                                        imageVector = Icons.Default.Casino,
                                        contentDescription = "Random Name",
                                        tint = EmeraldGlow,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                if (projectName.isNotEmpty()) {
                                    IconButton(onClick = { projectName = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear",
                                            tint = TextGray,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF141624),
                            unfocusedContainerColor = Color(0xFF141624),
                            focusedBorderColor = VioletPrimary,
                            unfocusedBorderColor = Color(0xFF282A3E),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // --------------------------------------------------
                // SECTION 3: ASPECT RATIO SELECTOR
                // --------------------------------------------------
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📐 Select Aspect Ratio",
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AspectRatios.forEach { option ->
                            val isSelected = selectedAspectRatio == option.ratioLabel

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .then(
                                        if (isSelected) Modifier.background(Brush.verticalGradient(listOf(Color(0xFF2A1C4E), Color(0xFF16122E))))
                                        else Modifier.background(Color(0xFF131524))
                                    )
                                    .border(
                                        border = if (isSelected) BorderStroke(2.dp, VioletGlow)
                                        else BorderStroke(1.dp, Color(0xFF23253A)),
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .clickable {
                                        selectedAspectRatio = option.ratioLabel
                                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                    }
                                    .padding(vertical = 12.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    // Visual Frame Preview
                                    Box(
                                        modifier = Modifier
                                            .width(
                                                when (option.ratioLabel) {
                                                    "9:16" -> 16.dp
                                                    "16:9" -> 32.dp
                                                    "1:1" -> 22.dp
                                                    else -> 20.dp
                                                }
                                            )
                                            .height(
                                                when (option.ratioLabel) {
                                                    "9:16" -> 28.dp
                                                    "16:9" -> 18.dp
                                                    "1:1" -> 22.dp
                                                    else -> 25.dp
                                                }
                                            )
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(if (isSelected) VioletGlow else Color(0xFF33364D))
                                            .border(BorderStroke(1.dp, TextWhite.copy(alpha = 0.5f)), RoundedCornerShape(3.dp))
                                    )

                                    Text(
                                        text = option.ratioLabel,
                                        color = if (isSelected) TextWhite else TextGray,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = option.platformName.split(" ").firstOrNull() ?: "",
                                        color = if (isSelected) VioletGlow else TextGray.copy(alpha = 0.8f),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    if (option.isRecommended) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(EmeraldGlow)
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = "BEST",
                                                color = AmoledBlack,
                                                fontSize = 7.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // --------------------------------------------------
                // SECTION 4: EXPORT QUALITY & FPS
                // --------------------------------------------------
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "⚙️ Quality & Framerate",
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Resolution Selection
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Resolution", color = TextGray, fontSize = 11.sp)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF131524))
                                    .padding(3.dp)
                            ) {
                                listOf("1080p", "4K", "720p").forEach { res ->
                                    val isSelected = selectedResolution == res
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) VioletPrimary else Color.Transparent)
                                            .clickable { selectedResolution = res }
                                            .padding(vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = res,
                                            color = if (isSelected) TextWhite else TextGray,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }

                        // FPS Selection
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Frame Rate", color = TextGray, fontSize = 11.sp)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF131524))
                                    .padding(3.dp)
                            ) {
                                listOf("30 FPS", "60 FPS").forEach { fps ->
                                    val isSelected = selectedFps == fps
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) VioletPrimary else Color.Transparent)
                                            .clickable { selectedFps = fps }
                                            .padding(vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = fps,
                                            color = if (isSelected) TextWhite else TextGray,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // --------------------------------------------------
                // SECTION 5: AI ENHANCEMENTS TOGGLES
                // --------------------------------------------------
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✨ AI Editor Enhancements",
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Powered by Gemini AI",
                            color = VioletGlow,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF111322))
                            .border(BorderStroke(1.dp, Color(0xFF22243A)), RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Toggle 1: Auto Subtitles
                        AiToggleRow(
                            title = "Auto Subtitles & Animated Captions",
                            subtitle = "Generate synced viral captions in 15+ languages",
                            icon = Icons.Default.ClosedCaption,
                            iconColor = Color(0xFF00E5FF),
                            checked = autoCaptionsEnabled,
                            onCheckedChange = { autoCaptionsEnabled = it }
                        )

                        HorizontalDivider(color = Color(0xFF1E2034))

                        // Toggle 2: AI Audio Clean
                        AiToggleRow(
                            title = "AI Vocal Clean & Noise Removal",
                            subtitle = "Isolate speech & cancel background noise automatically",
                            icon = Icons.Default.GraphicEq,
                            iconColor = EmeraldGlow,
                            checked = aiAudioCleanEnabled,
                            onCheckedChange = { aiAudioCleanEnabled = it }
                        )

                        HorizontalDivider(color = Color(0xFF1E2034))

                        // Toggle 3: Smart Auto Reframer
                        AiToggleRow(
                            title = "Smart Auto-Reframer (Subject Focus)",
                            subtitle = "Keeps main speaker centered in vertical 9:16 frame",
                            icon = Icons.Default.Crop,
                            iconColor = Color(0xFFFFB703),
                            checked = smartReframerEnabled,
                            onCheckedChange = { smartReframerEnabled = it }
                        )

                        HorizontalDivider(color = Color(0xFF1E2034))

                        // Toggle 4: AI Silence Cut
                        AiToggleRow(
                            title = "AI Filler Word & Pause Cut",
                            subtitle = "Detect and remove long silences and 'um/uh' sounds",
                            icon = Icons.Default.ContentCut,
                            iconColor = ElectricPurple,
                            checked = autoCutFillersEnabled,
                            onCheckedChange = { autoCutFillersEnabled = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            // ==================================================
            // BOTTOM CTA BAR
            // ==================================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF0E101D),
                shadowElevation = 16.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Button(
                        onClick = {
                            if (projectName.isBlank()) {
                                Toast.makeText(context, "Please enter a project name", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)

                            // Start processing overlay
                            isInitializingProject = true
                            scope.launch {
                                initializationProgress = 0.2f
                                initializationStatusText = "Analyzing ${selectedMediaList.size} clip(s)..."
                                delay(300)
                                initializationProgress = 0.5f
                                initializationStatusText = "Configuring $selectedAspectRatio timeline..."
                                delay(300)
                                initializationProgress = 0.85f
                                initializationStatusText = "Loading AI models & captions..."
                                delay(350)
                                initializationProgress = 1.0f

                                val finalConfig = ProjectSetupConfig(
                                    projectName = projectName,
                                    selectedMedia = selectedMediaList,
                                    aspectRatio = selectedAspectRatio,
                                    resolution = selectedResolution,
                                    fps = selectedFps,
                                    autoCaptionsEnabled = autoCaptionsEnabled,
                                    aiAudioCleanEnabled = aiAudioCleanEnabled,
                                    smartReframerEnabled = smartReframerEnabled,
                                    autoCutFillersEnabled = autoCutFillersEnabled
                                )
                                onStartEditing(finalConfig)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .shadow(12.dp, RoundedCornerShape(14.dp), spotColor = VioletPrimary)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MovieFilter,
                                contentDescription = null,
                                tint = TextWhite,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Create Project & Start Editing ➔",
                                color = TextWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }

        // Processing / Initialization Loading Overlay
        if (isInitializingProject) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161828)),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, VioletPrimary),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = VioletGlow,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(44.dp)
                        )

                        Text(
                            text = initializationStatusText,
                            color = TextWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        LinearProgressIndicator(
                            progress = { initializationProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape),
                            color = VioletPrimary,
                            trackColor = Color(0xFF282A3E)
                        )

                        Text(
                            text = "Setting up $selectedResolution • $selectedFps • $selectedAspectRatio",
                            color = TextGray,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SetupMediaCard(
    item: MediaPickerItem,
    index: Int,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A1C2E))
            .border(BorderStroke(1.dp, Color(0xFF2B2E48)), RoundedCornerShape(12.dp))
    ) {
        if (item.uri != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.uri)
                    .crossfade(true)
                    .build(),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircleFilled,
                    contentDescription = null,
                    tint = VioletGlow,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Index Badge
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(4.dp)
                .size(20.dp)
                .clip(CircleShape)
                .background(VioletPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$index",
                color = TextWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Remove Button
        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(2.dp)
                .size(22.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.7f))
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                tint = TextWhite,
                modifier = Modifier.size(12.dp)
            )
        }

        // Duration Label
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.75f))
                .padding(vertical = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.durationFormatted,
                color = TextWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AiToggleRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) },
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
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    color = TextWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = TextGray,
                    fontSize = 10.sp
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TextWhite,
                checkedTrackColor = VioletPrimary,
                uncheckedThumbColor = TextGray,
                uncheckedTrackColor = Color(0xFF22243A)
            )
        )
    }
}
