package com.example.ui.screens.tools

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
private val ToolPurple = Color(0xFFA855F7)
private val ToolCyan = Color(0xFF22D7E8)
private val ToolBorder = Color(0xFFA855F7).copy(alpha = 0.25f)

enum class NoiseReductionLevel(val title: String, val reductionDb: String) {
    STUDIO_CLEAN("Studio Clean", "-18 dB"),
    MILD_OUTDOOR("Mild Wind & Ambient", "-12 dB"),
    AGGRESSIVE("Aggressive Hum Removal", "-24 dB")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceCleanerScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedLevel by remember { mutableStateOf(NoiseReductionLevel.STUDIO_CLEAN) }
    var enhancePresence by remember { mutableStateOf(true) }
    var autoVolumeLevel by remember { mutableStateOf(true) }
    var isCleaning by remember { mutableStateOf(false) }
    var cleaningProgress by remember { mutableStateOf(0f) }
    var isCleaned by remember { mutableStateOf(false) }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedVideoUri = uri
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            scope.launch {
                isCleaning = true
                isCleaned = false
                cleaningProgress = 0.1f

                delay(900)
                cleaningProgress = 0.45f

                delay(1100)
                cleaningProgress = 0.8f

                delay(800)
                cleaningProgress = 1.0f
                isCleaning = false
                isCleaned = true
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
                                text = "Voice Cleaner",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Noise reduction & vocal boost",
                                fontSize = 11.5.sp,
                                color = ToolPurple
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
            // Preset Intensity Mode Selection
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "1. AI Noise Suppression Level",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                NoiseReductionLevel.values().forEach { level ->
                    val isSelected = selectedLevel == level
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) ToolPurple.copy(alpha = 0.15f) else GlassSurface,
                        border = BorderStroke(1.dp, if (isSelected) ToolPurple else GlassBorder.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                selectedLevel = level
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(level.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text("Noise attenuation: ${level.reductionDb}", fontSize = 11.sp, color = TextSecondary)
                            }
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedLevel = level },
                                colors = RadioButtonDefaults.colors(selectedColor = ToolPurple, unselectedColor = TextSecondary)
                            )
                        }
                    }
                }
            }

            // Audio Enhancement Toggles
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "2. Vocal Enhancements",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = GlassSurface,
                    border = BorderStroke(0.5.dp, ToolBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Podcast Vocal Clarity", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text("Boosts 2-4kHz frequencies for crispy spoken voice", fontSize = 11.sp, color = TextSecondary)
                            }
                            Switch(
                                checked = enhancePresence,
                                onCheckedChange = { enhancePresence = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = ToolPurple)
                            )
                        }

                        Divider(color = TileBg, thickness = 1.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Auto Volume Normalizer", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text("Evens out whisper & shouting spikes to -14 LUFS", fontSize = 11.sp, color = TextSecondary)
                            }
                            Switch(
                                checked = autoVolumeLevel,
                                onCheckedChange = { autoVolumeLevel = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = ToolPurple)
                            )
                        }
                    }
                }
            }

            // Upload Video Section
            if (selectedVideoUri == null && !isCleaning) {
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
                                .background(ToolPurple.copy(alpha = 0.12f))
                                .border(1.5.dp, ToolPurple, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.Mic, contentDescription = null, tint = ToolPurple, modifier = Modifier.size(32.dp))
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Clean Audio from Video", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("Isolate speech and eliminate background fan, AC, and street noise.", fontSize = 12.sp, color = TextSecondary, textAlign = TextAlign.Center)
                        }

                        Button(
                            onClick = {
                                videoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ToolPurple, contentColor = Color.Black),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.height(42.dp)
                        ) {
                            Text("Select Video to Clean", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Cleaning animation
            if (isCleaning) {
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
                        CircularProgressIndicator(progress = { cleaningProgress }, color = ToolPurple, modifier = Modifier.size(44.dp))
                        Text(text = "Denoising audio waveform...", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                }
            }

            // Results Card
            if (isCleaned && !isCleaning) {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = GlassSurface,
                    border = BorderStroke(1.5.dp, ToolPurple),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ToolPurple, modifier = Modifier.size(20.dp))
                            Text("Audio Successfully Enhanced", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }

                        // Fake Waveform Visualization
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(TileBg)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val canvasW = size.width
                                val canvasH = size.height
                                val bars = 24
                                val barW = (canvasW - (bars - 1) * 3.dp.toPx()) / bars
                                for (i in 0 until bars) {
                                    val heightRatio = (Math.sin(i * 0.45).toFloat() * 0.4f + 0.5f).coerceIn(0.2f, 0.95f)
                                    val barH = canvasH * heightRatio
                                    drawRoundRect(
                                        color = ToolPurple,
                                        topLeft = Offset(i * (barW + 3.dp.toPx()), (canvasH - barH) / 2),
                                        size = Size(barW, barH),
                                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Noise reduced by ${selectedLevel.reductionDb} • Crisp clarity applied", fontSize = 11.5.sp, color = TextSecondary)
                        }

                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                Toast.makeText(context, "Clean audio track applied to project!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ToolPurple, contentColor = Color.Black),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                                Text("Apply Clean Voice to Project", fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
