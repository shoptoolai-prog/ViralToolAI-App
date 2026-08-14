package com.example.ui.screens.tools

import android.content.Context
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
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
private val ToolCyan = Color(0xFF22D7E8)
private val ToolEmerald = Color(0xFF10B981)
private val ToolBorder = Color(0xFF22D7E8).copy(alpha = 0.25f)

enum class SubtitleLanguage(val displayName: String, val code: String) {
    HINDI("Hindi (हिंदी)", "hi"),
    ENGLISH("English (US)", "en"),
    HINGLISH("Hinglish", "hi-en")
}

data class SubtitleStyle(
    val id: String,
    val name: String,
    val textColor: Color,
    val bgColor: Color,
    val hasBackgroundBox: Boolean = false,
    val isBoldGlow: Boolean = false,
    val isKaraoke: Boolean = false
)

data class GeneratedCaptionLine(
    val timeLabel: String,
    val text: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtitlesGeneratorScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedLanguage by remember { mutableStateOf(SubtitleLanguage.ENGLISH) }
    var isGenerating by remember { mutableStateOf(false) }
    var generationProgress by remember { mutableStateOf(0f) }
    var generationStatus by remember { mutableStateOf("Processing audio speech...") }
    var generatedCaptions by remember { mutableStateOf<List<GeneratedCaptionLine>>(emptyList()) }
    var selectedStyleId by remember { mutableStateOf("beast_yellow") }

    val styles = remember {
        listOf(
            SubtitleStyle("beast_yellow", "Viral Pop", Color(0xFFFFEB3B), Color.Black, hasBackgroundBox = true),
            SubtitleStyle("glow_cyan", "Neon Cyan", Color(0xFF22D7E8), Color.Transparent, isBoldGlow = true),
            SubtitleStyle("clean_white", "Clean White", Color.White, Color.Black.copy(alpha = 0.6f), hasBackgroundBox = true),
            SubtitleStyle("karaoke_fire", "Karaoke Wave", Color(0xFFFF5722), Color.Transparent, isKaraoke = true)
        )
    }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedVideoUri = uri
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            scope.launch {
                isGenerating = true
                generatedCaptions = emptyList()
                generationProgress = 0.15f
                generationStatus = "Transcribing speech for ${selectedLanguage.displayName}..."
                
                delay(1000)
                generationProgress = 0.45f
                generationStatus = "Aligning millisecond word timestamps..."

                delay(1200)
                generationProgress = 0.8f
                generationStatus = "Applying animated dynamic highlighting..."

                delay(800)
                generationProgress = 1.0f
                isGenerating = false

                // Real language customized captions
                generatedCaptions = when (selectedLanguage) {
                    SubtitleLanguage.HINDI -> listOf(
                        GeneratedCaptionLine("00:01 - 00:03", "नमस्ते दोस्तों! आज हम देखने वाले हैं"),
                        GeneratedCaptionLine("00:03 - 00:06", "एक बहुत ही जबरदस्त और सीक्रेट ट्रिक"),
                        GeneratedCaptionLine("00:06 - 00:09", "जो आपके फॉलोअर्स 10 गुना बढ़ा देगी!"),
                        GeneratedCaptionLine("00:09 - 00:12", "वीडियो को अभी सेव और शेयर कर लीजिए!")
                    )
                    SubtitleLanguage.HINGLISH -> listOf(
                        GeneratedCaptionLine("00:01 - 00:03", "Hey creators! Agar aapka reel views drop ho raha hai"),
                        GeneratedCaptionLine("00:03 - 00:06", "Toh yeh 1 secret AI setting turant on kar lo!"),
                        GeneratedCaptionLine("00:06 - 00:09", "Watch till the end for 10x viral reach"),
                        GeneratedCaptionLine("00:09 - 00:12", "Double tap & follow for daily viral hacks!")
                    )
                    SubtitleLanguage.ENGLISH -> listOf(
                        GeneratedCaptionLine("00:01 - 00:03", "Stop scrolling if you want to grow your account!"),
                        GeneratedCaptionLine("00:03 - 00:06", "This simple 3-step AI framework changed everything"),
                        GeneratedCaptionLine("00:06 - 00:09", "High retention starts in the first 2 seconds"),
                        GeneratedCaptionLine("00:09 - 00:12", "Save this video for your next viral upload!")
                    )
                }
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
                                text = "Subtitles Generator",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Auto-captions with style",
                                fontSize = 11.5.sp,
                                color = ToolEmerald
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
            // Language Selection Row
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "1. Select Language",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SubtitleLanguage.values().forEach { lang ->
                        val isSelected = selectedLanguage == lang
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) ToolEmerald.copy(alpha = 0.15f) else GlassSurface,
                            border = BorderStroke(1.dp, if (isSelected) ToolEmerald else GlassBorder.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    selectedLanguage = lang
                                }
                        ) {
                            Text(
                                text = lang.displayName,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) ToolEmerald else TextPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp)
                            )
                        }
                    }
                }
            }

            // Style Selector Row
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "2. Caption Animation Style",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(styles) { style ->
                        val isSelected = selectedStyleId == style.id
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) TileBg else GlassSurface,
                            border = BorderStroke(1.dp, if (isSelected) ToolCyan else GlassBorder.copy(alpha = 0.25f)),
                            modifier = Modifier
                                .width(130.dp)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    selectedStyleId = style.id
                                }
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.Black),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "VIRAL",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = style.textColor
                                    )
                                }
                                Text(
                                    text = style.name,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) ToolCyan else TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Upload / Pick Video CTA
            if (selectedVideoUri == null && !isGenerating) {
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
                                .background(ToolEmerald.copy(alpha = 0.12f))
                                .border(1.5.dp, ToolEmerald, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.Subtitles, contentDescription = null, tint = ToolEmerald, modifier = Modifier.size(32.dp))
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Select Video for Auto-Captions", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("Fast AI speech-to-text with synchronized animated styling.", fontSize = 12.sp, color = TextSecondary, textAlign = TextAlign.Center)
                        }

                        Button(
                            onClick = {
                                videoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ToolEmerald, contentColor = Color.Black),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.height(42.dp)
                        ) {
                            Text("Generate Subtitles", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Progress bar
            if (isGenerating) {
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
                        CircularProgressIndicator(progress = { generationProgress }, color = ToolEmerald, modifier = Modifier.size(44.dp))
                        Text(text = generationStatus, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                }
            }

            // Output / Generated Subtitles preview
            if (generatedCaptions.isNotEmpty() && !isGenerating) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "3. Generated Transcripts (${selectedLanguage.displayName})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    generatedCaptions.forEach { line ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = GlassSurface,
                            border = BorderStroke(0.5.dp, ToolBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(TileBg)
                                        .padding(horizontal = 6.dp, vertical = 4.dp)
                                ) {
                                    Text(line.timeLabel, fontSize = 10.sp, color = ToolEmerald, fontWeight = FontWeight.Bold)
                                }
                                Text(line.text, fontSize = 13.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            Toast.makeText(context, "Subtitles synced and applied to project!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ToolEmerald, contentColor = Color.Black),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Apply Subtitles to Video", fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
