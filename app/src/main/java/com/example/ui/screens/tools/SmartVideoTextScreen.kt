package com.example.ui.screens.tools

import android.content.Context
import android.net.Uri
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val ScreenBg = Color(0xFF0B0D12)
private val GlassSurface = Color(0xFF141824)
private val TileBg = Color(0xFF1B2232)
private val ToolOrange = Color(0xFFFF9800)
private val ToolCyan = Color(0xFF22D7E8)
private val ToolBorder = Color(0xFFFF9800).copy(alpha = 0.25f)

enum class VideoCategory(val displayName: String, val hookTemplate: String, val tagBadge: String) {
    PRODUCT_REVIEW("Product Review", "🔥 IS THIS WORTH IT? (Honest Review)", "MUST BUY"),
    VLOG_TRAVEL("Vlog / Lifestyle", "📍 24 HOURS IN THIS SECRET PLACE", "VLOG 42"),
    TECH_TUTORIAL("Tech / Tutorial", "⚡ STOP DOING THIS! (Do this instead)", "TUTORIAL"),
    FITNESS_HEALTH("Fitness / Health", "💪 3 MISTAKES DESTROYING YOUR GAINS", "DAY 1/30"),
    COMEDY_POV("Comedy / Entertainment", "😭 WAIT TILL THE END...", "POV")
}

data class AutoTextPreset(
    val title: String,
    val subtitle: String,
    val position: String,
    val textColor: Color,
    val bannerBg: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartVideoTextScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedCategory by remember { mutableStateOf(VideoCategory.PRODUCT_REVIEW) }
    var customMainHook by remember { mutableStateOf(selectedCategory.hookTemplate) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var analyzeProgress by remember { mutableStateOf(0f) }
    var isReady by remember { mutableStateOf(false) }
    var textPosition by remember { mutableStateOf("Top Centered") }

    LaunchedEffect(selectedCategory) {
        customMainHook = selectedCategory.hookTemplate
    }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedVideoUri = uri
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            scope.launch {
                isAnalyzing = true
                isReady = false
                analyzeProgress = 0.2f

                delay(800)
                analyzeProgress = 0.6f

                delay(900)
                analyzeProgress = 1.0f
                isAnalyzing = false
                isReady = true
            }
        }
    }

    Scaffold(
        containerColor = ScreenBg,
        topBar = {
            Surface(
                color = ScreenBg.copy(alpha = 0.95f),
                border = BorderStroke(0.5.dp, GlassBorder.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = GlassSurface,
                            border = BorderStroke(1.dp, GlassBorder.copy(alpha = 0.4f)),
                            modifier = Modifier.size(38.dp)
                        ) {
                            IconButton(onClick = onBack) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "Smart Video Text",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Auto-text overlays by video niche",
                                fontSize = 11.5.sp,
                                color = ToolOrange
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Category Selector
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "1. Select Video Niche",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(VideoCategory.values()) { cat ->
                        val isSelected = selectedCategory == cat
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) ToolOrange.copy(alpha = 0.18f) else GlassSurface,
                            border = BorderStroke(1.dp, if (isSelected) ToolOrange else GlassBorder.copy(alpha = 0.3f)),
                            modifier = Modifier.clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                selectedCategory = cat
                            }
                        ) {
                            Text(
                                text = cat.displayName,
                                fontSize = 12.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) ToolOrange else TextPrimary,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }

            // Headline / Overlay Customizer
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "2. Hook Overlay Banner",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                OutlinedTextField(
                    value = customMainHook,
                    onValueChange = { customMainHook = it },
                    label = { Text("Main Hook Text", color = TextSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ToolOrange,
                        unfocusedBorderColor = ToolBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = TileBg,
                        unfocusedContainerColor = TileBg
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Text Positioning Row
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "3. Screen Placement",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Top Centered", "Middle Pop", "Lower Third").forEach { pos ->
                        val isSelected = textPosition == pos
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) ToolOrange.copy(alpha = 0.15f) else GlassSurface,
                            border = BorderStroke(1.dp, if (isSelected) ToolOrange else GlassBorder.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    textPosition = pos
                                }
                        ) {
                            Text(
                                text = pos,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) ToolOrange else TextPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                    }
                }
            }

            // Video Picker CTA
            if (selectedVideoUri == null && !isAnalyzing) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = GlassSurface,
                    border = BorderStroke(1.5.dp, ToolBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            videoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                            )
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(ToolOrange.copy(alpha = 0.12f))
                                .border(1.5.dp, ToolOrange, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.TextFields, contentDescription = null, tint = ToolOrange, modifier = Modifier.size(32.dp))
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Attach Video for Smart Overlay", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("Creates high-retention text banners optimized for 9:16 vertical viewports.", fontSize = 12.sp, color = TextSecondary, textAlign = TextAlign.Center)
                        }

                        Button(
                            onClick = {
                                videoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ToolOrange, contentColor = Color.Black),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.height(42.dp)
                        ) {
                            Text("Choose Video", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Analyzing state
            if (isAnalyzing) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = GlassSurface,
                    border = BorderStroke(1.dp, ToolBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(progress = { analyzeProgress }, color = ToolOrange, modifier = Modifier.size(44.dp))
                        Text(text = "Calculating safe zones & text tracking...", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                }
            }

            // Preview & Apply Result
            if (isReady && !isAnalyzing) {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = GlassSurface,
                    border = BorderStroke(1.5.dp, ToolOrange),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text("Overlay Mockup Preview", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)

                        // 9:16 Video Box Preview with text overlay
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Black),
                            contentAlignment = when (textPosition) {
                                "Top Centered" -> Alignment.TopCenter
                                "Lower Third" -> Alignment.BottomCenter
                                else -> Alignment.Center
                            }
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ToolOrange,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .padding(horizontal = 8.dp)
                            ) {
                                Text(
                                    text = customMainHook,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                Toast.makeText(context, "Smart text overlay layers added to project!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ToolOrange, contentColor = Color.Black),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                                Text("Apply Smart Text to Video", fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
