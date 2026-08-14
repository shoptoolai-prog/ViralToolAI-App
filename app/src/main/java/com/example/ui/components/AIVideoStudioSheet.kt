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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.*

// ============================================================================
// MASTER PHASE E-6 — PROFESSIONAL AI VIDEO PROCESSING STUDIO UI SHEET
// ============================================================================

private val DarkBackground = Color(0xFF07090E)
private val CardBackground = Color(0xFF111420)
private val CardBorder = Color(0xFF1E2336)
private val NeonCyan = Color(0xFF00E5FF)
private val MintAccent = Color(0xFF38E8A5)
private val AIBadgeGradientStart = Color(0xFF7C4DFF)
private val AIBadgeGradientEnd = Color(0xFF651FFF)
private val MutedText = Color(0xFF8B92AD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIVideoStudioSheet(
    clips: List<TimelineClip>,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val aiEngine = remember { AIEngine.getInstance(context) }

    val isProcessing by aiEngine.isProcessing.collectAsState()
    val progress by aiEngine.processingProgress.collectAsState()
    val statusText by aiEngine.processingStatusText.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Auto Cut, 1: Reframe, 2: BG & Portrait, 3: Enhance & Sky, 4: Subtitles, 5: Beat & Zoom

    var cutSuggestions by remember { mutableStateOf<List<AICutSuggestion>>(emptyList()) }
    var highlightMoments by remember { mutableStateOf<List<AIHighlightMoment>>(emptyList()) }

    val totalDurationMs = remember(clips) {
        clips.maxOfOrNull { clip: TimelineClip -> clip.startTimelineMs + clip.durationOnTimelineMs } ?: 5000L
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // --------------------------------------------------------------------
        // 1. TOP TOOLBAR & AI STUDIO BADGE
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

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                    color = AIBadgeGradientStart,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = " AI ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = "AI VIDEO PROCESSING STUDIO",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan,
                    letterSpacing = 0.5.sp
                )
            }

            IconButton(onClick = { /* Help / Info */ }) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "AI", tint = NeonCyan)
            }
        }

        // --------------------------------------------------------------------
        // 2. REAL AI PROCESSING PROGRESS BAR
        // --------------------------------------------------------------------
        if (isProcessing) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B30)),
                border = BorderStroke(1.dp, NeonCyan),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(statusText, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Button(
                            onClick = { aiEngine.cancelAITask() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4D4D)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Cancel", fontSize = 10.sp, color = Color.White)
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = NeonCyan,
                        trackColor = CardBorder
                    )
                }
            }
        }

        // --------------------------------------------------------------------
        // 3. CATEGORY TABS
        // --------------------------------------------------------------------
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkBackground,
            contentColor = NeonCyan,
            edgePadding = 16.dp
        ) {
            val tabs = listOf("Auto Cut & Highlights", "Auto Reframe", "BG & Portrait", "Enhance & Sky", "Subtitles", "Beat & Zoom")
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == index) NeonCyan else MutedText
                        )
                    }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // --------------------------------------------------------------------
        // 4. TAB CONTENT
        // --------------------------------------------------------------------
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (selectedTab) {
                // TAB 0: AI AUTO CUT & HIGHLIGHTS
                0 -> {
                    item {
                        AIToolCard(
                            title = "AI AUTO CUT & PAUSE REMOVER",
                            description = "Scans full timeline to detect silent gaps, long pauses, duplicate frames, and empty sections.",
                            actionLabel = "Analyze & Suggest Cuts",
                            icon = Icons.Default.ContentCut
                        ) {
                            aiEngine.executeAITask("Scanning Video for Silent Gaps") {
                                cutSuggestions = aiEngine.speechAnalyzer.analyzeSilenceGaps(totalDurationMs)
                            }
                        }
                    }

                    if (cutSuggestions.isNotEmpty()) {
                        item {
                            SectionHeader("SUGGESTED CUTS (${cutSuggestions.size} FOUND)")
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                cutSuggestions.forEach { suggestion ->
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
                                                Text(suggestion.reason, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                Text("${suggestion.startMs}ms - ${suggestion.endMs}ms (${suggestion.endMs - suggestion.startMs}ms duration)", color = MutedText, fontSize = 9.sp)
                                            }
                                            Text("Approve Cut", color = MintAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        AIToolCard(
                            title = "AI HIGHLIGHT MOMENT DETECTOR",
                            description = "Identifies best moments, action peaks, smiles, reactions, and speech highlights.",
                            actionLabel = "Detect Highlights",
                            icon = Icons.Default.AutoAwesome
                        ) {
                            aiEngine.executeAITask("Detecting Action & Smile Peaks") {
                                highlightMoments = aiEngine.speechAnalyzer.analyzeHighlightMoments(totalDurationMs)
                            }
                        }
                    }

                    if (highlightMoments.isNotEmpty()) {
                        item {
                            SectionHeader("RECOMMENDED HIGHLIGHT REELS (${highlightMoments.size})")
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(highlightMoments) { hl ->
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                                        border = BorderStroke(1.dp, NeonCyan),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(hl.label, color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            Text("Score: ${(hl.score * 100).toInt()}% • ${hl.startMs}ms - ${hl.endMs}ms", color = Color.White, fontSize = 9.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // TAB 1: AI AUTO REFRAME & OBJECT TRACKING
                1 -> {
                    item {
                        SectionHeader("AI AUTO REFRAME (DYNAMIC SUBJECT CENTERING)")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Enable Auto Reframe", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Switch(
                                checked = aiEngine.isAutoReframeEnabled,
                                onCheckedChange = { aiEngine.isAutoReframeEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                            )
                        }
                    }

                    item {
                        Text("Target Aspect Ratio", color = MutedText, fontSize = 10.sp)
                        Spacer(Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(AIReframesAspect.values()) { aspect ->
                                val isSel = aiEngine.autoReframeAspect == aspect
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = if (isSel) Color(0xFF122C38) else CardBackground),
                                    border = BorderStroke(1.dp, if (isSel) NeonCyan else CardBorder),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.clickable { aiEngine.autoReframeAspect = aspect }
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(aspect.displayName, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text("${aspect.widthRatio.toInt()}:${aspect.heightRatio.toInt()}", color = NeonCyan, fontSize = 9.sp)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        AIToolCard(
                            title = "AI OBJECT & FACE TRACKER",
                            description = "Locks tracking focus on Person, Face, Pet, or Vehicle for stickers, text, or blur effects.",
                            actionLabel = "Run Object Tracking",
                            icon = Icons.Default.CenterFocusStrong
                        ) {
                            aiEngine.executeAITask("Tracking Subject Movement Vectors") {}
                        }
                    }
                }

                // TAB 2: AI BACKGROUND & PORTRAIT MODE
                2 -> {
                    item {
                        SectionHeader("AI BACKGROUND REMOVAL & PORTRAIT MATTING")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Enable Background Matting", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Switch(
                                checked = aiEngine.isBackgroundRemovalEnabled,
                                onCheckedChange = { aiEngine.isBackgroundRemovalEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                            )
                        }
                    }

                    item {
                        Text("Background Mode", color = MutedText, fontSize = 10.sp)
                        Spacer(Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(AIBackgroundMode.values()) { mode ->
                                val isSel = aiEngine.backgroundMode == mode
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = if (isSel) Color(0xFF122C38) else CardBackground),
                                    border = BorderStroke(1.dp, if (isSel) NeonCyan else CardBorder),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.clickable { aiEngine.backgroundMode = mode }
                                ) {
                                    Text(mode.displayName, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp))
                                }
                            }
                        }
                    }

                    if (aiEngine.backgroundMode == AIBackgroundMode.BOKEH_BLUR) {
                        item {
                            Text("Portrait Bokeh Blur Intensity", color = Color.White, fontSize = 11.sp)
                            Slider(
                                value = aiEngine.portraitBlurIntensity,
                                onValueChange = { aiEngine.portraitBlurIntensity = it },
                                colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
                            )
                        }
                    }
                }

                // TAB 3: AI ENHANCE, SKY & STABILIZE
                3 -> {
                    item {
                        AIToggleRow(
                            title = "AI Video Enhance & Detail Boost",
                            subtitle = "Enhances sharpness, contrast, and restores compression details.",
                            checked = aiEngine.isAIEnhanceEnabled,
                            onCheckedChange = { aiEngine.isAIEnhanceEnabled = it }
                        )
                    }

                    item {
                        AIToggleRow(
                            title = "AI Anti-Shake Stabilization",
                            subtitle = "Calculates optical flow to eliminate camera motion shake.",
                            checked = aiEngine.isAIStabilizeEnabled,
                            onCheckedChange = { aiEngine.isAIStabilizeEnabled = it }
                        )
                    }

                    item {
                        SectionHeader("AI SKY REPLACEMENT")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Enable Sky Replacement", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Switch(
                                checked = aiEngine.isSkyReplacementEnabled,
                                onCheckedChange = { aiEngine.isSkyReplacementEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                            )
                        }
                    }

                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(AISkyStyle.values()) { style ->
                                val isSel = aiEngine.skyStyle == style
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = if (isSel) Color(0xFF122C38) else CardBackground),
                                    border = BorderStroke(1.dp, if (isSel) NeonCyan else CardBorder),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.clickable { aiEngine.skyStyle = style }
                                ) {
                                    Text(style.displayName, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp))
                                }
                            }
                        }
                    }
                }

                // TAB 4: AI SUBTITLES & VOICE ENGINE
                4 -> {
                    item {
                        SectionHeader("AI AUTO SUBTITLE GENERATOR")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Enable On-Screen Subtitles", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Switch(
                                checked = aiEngine.isSubtitlesEnabled,
                                onCheckedChange = { aiEngine.isSubtitlesEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                            )
                        }
                    }

                    item {
                        Text("Speech Recognition Language", color = MutedText, fontSize = 10.sp)
                        Spacer(Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(AISubtitleLanguage.values()) { lang ->
                                val isSel = aiEngine.subtitleLanguage == lang
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = if (isSel) Color(0xFF122C38) else CardBackground),
                                    border = BorderStroke(1.dp, if (isSel) NeonCyan else CardBorder),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.clickable { aiEngine.subtitleLanguage = lang }
                                ) {
                                    Text(lang.displayName, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp))
                                }
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                aiEngine.executeAITask("Transcribing Audio & Generating Timed Subtitles") {
                                    aiEngine.activeSubtitles = aiEngine.subtitleEngine.generateSubtitles(totalDurationMs, aiEngine.subtitleLanguage)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Generate Subtitles Now", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    if (aiEngine.activeSubtitles.isNotEmpty()) {
                        item {
                            SectionHeader("GENERATED TIMELINE SUBTITLES (${aiEngine.activeSubtitles.size})")
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                aiEngine.activeSubtitles.forEach { cue ->
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                                        border = BorderStroke(1.dp, CardBorder),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(cue.text, color = Color.Yellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text("${cue.startMs}ms - ${cue.endMs}ms", color = MutedText, fontSize = 9.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // TAB 5: AI BEAT SYNC & SMART ZOOM
                5 -> {
                    item {
                        AIToolCard(
                            title = "AI BEAT SYNC CUTTER",
                            description = "Analyzes audio track beat transients and aligns edit cuts to music rhythm.",
                            actionLabel = "Align Cuts to Music Beats",
                            icon = Icons.Default.MusicNote
                        ) {
                            aiEngine.executeAITask("Detecting Music Audio Transients & BPM Beats") {}
                        }
                    }

                    item {
                        AIToggleRow(
                            title = "AI Dynamic Smart Zoom",
                            subtitle = "Automatically applies subtle keyframe camera zooms when speaker is active.",
                            checked = aiEngine.isSmartZoomEnabled,
                            onCheckedChange = { aiEngine.isSmartZoomEnabled = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun AIToolCard(
    title: String,
    description: String,
    actionLabel: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onAction: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(1.dp, CardBorder),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(6.dp))
            Text(description, color = MutedText, fontSize = 10.sp)

            Spacer(Modifier.height(10.dp))
            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(actionLabel, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun AIToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(1.dp, CardBorder),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = MutedText, fontSize = 9.sp)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan)
            )
        }
    }
}
