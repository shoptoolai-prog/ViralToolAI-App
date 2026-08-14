package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.*

// ============================================================================
// MASTER PHASE E-5 — PROFESSIONAL EXPORT & RENDER STUDIO SHEET
// ============================================================================

private val DarkBackground = Color(0xFF090A0F)
private val CardBackground = Color(0xFF13151F)
private val CardBorder = Color(0xFF202434)
private val MintAccent = Color(0xFF38E8A5)
private val MutedText = Color(0xFF8E95AD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportStudioSheet(
    clips: List<TimelineClip>,
    texts: List<TextOverlay>,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val exportEngine = remember { ExportEngine.getInstance(context) }
    val queueState by exportEngine.exportQueueState.collectAsState()
    val activeJob by exportEngine.activeJobState.collectAsState()

    var selectedFormat by remember { mutableStateOf(ExportFormat.MP4_VIDEO) }
    var selectedResolution by remember { mutableStateOf(ExportResolution.RES_1080P) }
    var selectedFrameRate by remember { mutableStateOf(ExportFrameRate.FPS_30) }
    var selectedVideoCodec by remember { mutableStateOf(VideoCodec.H264_AVC) }
    var selectedAudioCodec by remember { mutableStateOf(AudioCodec.AAC_STEREO_48K) }
    var selectedBitratePreset by remember { mutableStateOf(ExportBitratePreset.HIGH) }
    var customBitrateMbps by remember { mutableStateOf(16.0f) }
    var selectedDestination by remember { mutableStateOf(ExportDestination.GALLERY_MOVIES) }

    var videoTitle by remember { mutableStateOf("Studio_Edit_${System.currentTimeMillis() % 10000}") }
    var authorName by remember { mutableStateOf("Pro Editor") }
    var keepMetadata by remember { mutableStateOf(true) }

    val totalDurationMs = remember(clips) {
        clips.maxOfOrNull { clip: TimelineClip -> clip.startTimelineMs + clip.durationOnTimelineMs } ?: 3000L
    }

    val estimatedSizeMB = remember(totalDurationMs, selectedResolution, selectedFrameRate, selectedBitratePreset, customBitrateMbps) {
        exportEngine.hardwareCodecManager.calculateEstimatedFileSizeMB(
            totalDurationMs, selectedResolution, selectedFrameRate, selectedBitratePreset, customBitrateMbps
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // --------------------------------------------------------------------
        // 1. TOP HEADER TOOLBAR
        // --------------------------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "HARDWARE EXPORT STUDIO",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MintAccent,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${clips.size} Clips • Total ${formatMs(totalDurationMs)}",
                    fontSize = 10.sp,
                    color = MutedText
                )
            }

            Button(
                onClick = {
                    val meta = ExportMetadata(title = videoTitle, author = authorName, keepMetadata = keepMetadata)
                    exportEngine.submitExportJob(
                        title = videoTitle,
                        clips = clips,
                        texts = texts,
                        resolution = selectedResolution,
                        frameRate = selectedFrameRate,
                        format = selectedFormat,
                        videoCodec = selectedVideoCodec,
                        audioCodec = selectedAudioCodec,
                        bitratePreset = selectedBitratePreset,
                        customBitrateMbps = customBitrateMbps,
                        metadata = meta,
                        destination = selectedDestination
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = MintAccent),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Movie, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Text("Export Video", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // --------------------------------------------------------------------
        // 2. ACTIVE EXPORT PROGRESS CARD (If rendering)
        // --------------------------------------------------------------------
        activeJob?.let { job ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF12261F)),
                border = BorderStroke(1.dp, MintAccent),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("ENCODING IN PROGRESS", color = MintAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                            Text(job.title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (job.status == ExportStatus.RENDERING) {
                                IconButton(onClick = { exportEngine.pauseJob(job.id) }) {
                                    Icon(Icons.Default.Pause, contentDescription = "Pause", tint = Color.White)
                                }
                            } else if (job.status == ExportStatus.PAUSED) {
                                IconButton(onClick = { exportEngine.resumeJob(job.id, clips, texts) }) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = "Resume", tint = MintAccent)
                                }
                            }
                            IconButton(onClick = { exportEngine.cancelJob(job.id) }) {
                                Icon(Icons.Default.Stop, contentDescription = "Cancel", tint = Color(0xFFFF4D4D))
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { job.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = MintAccent,
                        trackColor = CardBorder
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${(job.progress * 100).toInt()}% (${job.currentFrame}/${job.totalFrames} frames)", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("${String.format("%.1f", job.currentFps)} FPS (${String.format("%.1f", job.encodingSpeedX)}x)", color = MintAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("ETA ${job.estimatedRemainingSec}s", color = MutedText, fontSize = 11.sp)
                    }
                }
            }
        }

        // --------------------------------------------------------------------
        // 3. EXPORT CONFIGURATION SETTINGS
        // --------------------------------------------------------------------
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // FORMAT SELECTION
            item {
                SectionTitle("EXPORT FORMAT")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(ExportFormat.values()) { fmt ->
                        val isSel = selectedFormat == fmt
                        Card(
                            colors = CardDefaults.cardColors(containerColor = if (isSel) Color(0xFF1E3A2F) else CardBackground),
                            border = BorderStroke(1.dp, if (isSel) MintAccent else CardBorder),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.clickable { selectedFormat = fmt }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(fmt.displayName, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(".${fmt.extension.uppercase()}", color = if (isSel) MintAccent else MutedText, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }

            // RESOLUTION SELECTION
            item {
                SectionTitle("VIDEO RESOLUTION")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(ExportResolution.values()) { res ->
                        val isSel = selectedResolution == res
                        Card(
                            colors = CardDefaults.cardColors(containerColor = if (isSel) Color(0xFF1E3A2F) else CardBackground),
                            border = BorderStroke(1.dp, if (isSel) MintAccent else CardBorder),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.clickable { selectedResolution = res }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(res.displayName, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("${res.width}x${res.height}", color = if (isSel) MintAccent else MutedText, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }

            // FRAME RATE SELECTION
            item {
                SectionTitle("FRAME RATE (FPS)")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(ExportFrameRate.values()) { fps ->
                        val isSel = selectedFrameRate == fps
                        Card(
                            colors = CardDefaults.cardColors(containerColor = if (isSel) Color(0xFF1E3A2F) else CardBackground),
                            border = BorderStroke(1.dp, if (isSel) MintAccent else CardBorder),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.clickable { selectedFrameRate = fps }
                        ) {
                            Text(fps.displayName, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp))
                        }
                    }
                }
            }

            // CODEC SELECTION
            item {
                SectionTitle("VIDEO ENCODER CODEC")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(exportEngine.hardwareCodecManager.getSupportedVideoCodecs()) { codec ->
                        val isSel = selectedVideoCodec == codec
                        Card(
                            colors = CardDefaults.cardColors(containerColor = if (isSel) Color(0xFF1E3A2F) else CardBackground),
                            border = BorderStroke(1.dp, if (isSel) MintAccent else CardBorder),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.clickable { selectedVideoCodec = codec }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(codec.displayName, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("HW Accelerated", color = MintAccent, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }

            // BITRATE & ESTIMATED FILE SIZE CARD
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, CardBorder),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("QUALITY / BITRATE", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("Est. ~${String.format("%.1f", estimatedSizeMB)} MB", color = MintAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(Modifier.height(8.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(ExportBitratePreset.values()) { preset ->
                                val isSel = selectedBitratePreset == preset
                                FilterChip(
                                    selected = isSel,
                                    onClick = { selectedBitratePreset = preset },
                                    label = { Text(preset.displayName, fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MintAccent, selectedLabelColor = Color.Black)
                                )
                            }
                        }
                    }
                }
            }

            // DESTINATION SELECTION
            item {
                SectionTitle("OUTPUT DESTINATION")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(ExportDestination.values()) { dest ->
                        val isSel = selectedDestination == dest
                        Card(
                            colors = CardDefaults.cardColors(containerColor = if (isSel) Color(0xFF1E3A2F) else CardBackground),
                            border = BorderStroke(1.dp, if (isSel) MintAccent else CardBorder),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.clickable { selectedDestination = dest }
                        ) {
                            Text(dest.displayName, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp))
                        }
                    }
                }
            }

            // EXPORT QUEUE HISTORY LIST
            if (queueState.isNotEmpty()) {
                item {
                    SectionTitle("RECENT EXPORT JOBS QUEUE")
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        queueState.forEach { job ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = CardBackground),
                                border = BorderStroke(1.dp, CardBorder),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(job.title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text("${job.resolution.displayName} • ${job.format.extension.uppercase()}", color = MutedText, fontSize = 9.sp)
                                    }
                                    Text(
                                        text = job.status.name,
                                        color = if (job.status == ExportStatus.COMPLETED) MintAccent else Color.Yellow,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
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
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

private fun formatMs(ms: Long): String {
    val seconds = (ms / 1000) % 60
    val minutes = (ms / (1000 * 60)) % 60
    return String.format("%02d:%02d", minutes, seconds)
}
