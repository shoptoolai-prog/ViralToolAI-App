package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.Color as AndroidColor
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ============================================================================
// MASTER PHASE E-4 — PROFESSIONAL KEYFRAME & ANIMATION STUDIO SHEET
// ============================================================================

private val DarkBackground = Color(0xFF090A0F)
private val CardBackground = Color(0xFF13151F)
private val CardBorder = Color(0xFF202434)
private val MintAccent = Color(0xFF38E8A5)
private val MutedText = Color(0xFF8E95AD)
private val KeyframeDiamondColor = Color(0xFF38E8A5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyframeStudioSheet(
    targetClip: TimelineClip?,
    playheadMs: Long,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val keyframeEngine = remember { KeyframeEngine.getInstance(context) }
    val tracksState by keyframeEngine.tracksState.collectAsState()

    val targetId = targetClip?.id ?: "sample_target"
    val clipDurationMs = targetClip?.durationOnTimelineMs ?: 3000L
    val relativeTimeMs = (playheadMs - (targetClip?.startTimelineMs ?: 0L)).coerceIn(0L, clipDurationMs)

    val track = keyframeEngine.getTrack(targetId)
    val currentKfAtPlayhead = track.keyframes.find { Math.abs(it.timeMs - relativeTimeMs) < 30L }

    var activeTab by remember { mutableStateOf(0) } // 0: Keyframe Properties, 1: Interpolation Curve, 2: Presets (In/Out/Loop)

    // Current property states (or values evaluated at playhead)
    val currentTransform = remember(track, relativeTimeMs) {
        keyframeEngine.animationRenderer.calculateInterpolatedTransform(track, relativeTimeMs, clipDurationMs)
    }

    var posX by remember(currentTransform) { mutableStateOf(currentKfAtPlayhead?.positionX ?: currentTransform.translateX) }
    var posY by remember(currentTransform) { mutableStateOf(currentKfAtPlayhead?.positionY ?: currentTransform.translateY) }
    var scale by remember(currentTransform) { mutableStateOf(currentKfAtPlayhead?.scaleX ?: currentTransform.scaleX) }
    var rot by remember(currentTransform) { mutableStateOf(currentKfAtPlayhead?.rotation ?: currentTransform.rotation) }
    var opacity by remember(currentTransform) { mutableStateOf(currentKfAtPlayhead?.opacity ?: currentTransform.opacity) }
    var blur by remember(currentTransform) { mutableStateOf(currentKfAtPlayhead?.blurRadius ?: currentTransform.blurRadius) }

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
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "KEYFRAME & ANIMATION ENGINE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MintAccent,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${targetClip?.mediaItem?.name ?: "Selected Object"} • ${formatMs(relativeTimeMs)}",
                    fontSize = 10.sp,
                    color = MutedText
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = { keyframeEngine.undo() }) {
                    Icon(Icons.Default.Undo, contentDescription = "Undo", tint = Color.White)
                }
                IconButton(onClick = { keyframeEngine.redo() }) {
                    Icon(Icons.Default.Redo, contentDescription = "Redo", tint = Color.White)
                }
            }
        }

        // --------------------------------------------------------------------
        // 2. KEYFRAME TIMELINE DIAMOND BAR (◆)
        // --------------------------------------------------------------------
        Card(
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            border = BorderStroke(1.dp, CardBorder),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                // Keyframe track line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(CardBorder)
                        .align(Alignment.Center)
                )

                // Keyframe Diamonds (◆)
                track.keyframes.forEach { kf ->
                    val ratio = (kf.timeMs.toFloat() / clipDurationMs.toFloat()).coerceIn(0f, 1f)
                    val isSelected = Math.abs(kf.timeMs - relativeTimeMs) < 30L

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(ratio.coerceAtLeast(0.02f))
                            .align(Alignment.CenterStart)
                    ) {
                        Text(
                            text = "◆",
                            color = if (isSelected) MintAccent else Color.Yellow,
                            fontSize = if (isSelected) 22.sp else 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }

                // Playhead indicator
                val playheadRatio = (relativeTimeMs.toFloat() / clipDurationMs.toFloat()).coerceIn(0f, 1f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(playheadRatio.coerceAtLeast(0.01f))
                        .align(Alignment.CenterStart)
                ) {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .fillMaxHeight()
                            .background(Color.White)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // --------------------------------------------------------------------
        // 3. KEYFRAME ACTION BUTTONS (+ Keyframe / - Keyframe / Mirror / Copy)
        // --------------------------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    val kf = KeyframeData(
                        timeMs = relativeTimeMs,
                        positionX = posX,
                        positionY = posY,
                        scaleX = scale,
                        scaleY = scale,
                        rotation = rot,
                        opacity = opacity,
                        blurRadius = blur
                    )
                    keyframeEngine.addOrUpdateKeyframe(targetId, kf)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MintAccent),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Text(if (currentKfAtPlayhead != null) "Update Keyframe ◆" else "Add Keyframe ◆", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }

            if (currentKfAtPlayhead != null) {
                Button(
                    onClick = { keyframeEngine.removeKeyframe(targetId, currentKfAtPlayhead.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4D4D)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Text("Delete ◆", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }

            IconButton(onClick = { keyframeEngine.mirrorKeyframes(targetId) }) {
                Icon(Icons.Default.Flip, contentDescription = "Mirror Track", tint = Color.White)
            }
        }

        Spacer(Modifier.height(8.dp))

        // --------------------------------------------------------------------
        // 4. STUDIO TABS (0: Transform Controls, 1: Curves, 2: Presets)
        // --------------------------------------------------------------------
        TabRow(
            selectedTabIndex = activeTab,
            containerColor = Color.Transparent,
            contentColor = MintAccent,
            divider = {}
        ) {
            Tab(selected = activeTab == 0, onClick = { activeTab = 0 }) {
                Text("Transform", color = if (activeTab == 0) MintAccent else MutedText, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(10.dp))
            }
            Tab(selected = activeTab == 1, onClick = { activeTab = 1 }) {
                Text("Curve Interpolation", color = if (activeTab == 1) MintAccent else MutedText, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(10.dp))
            }
            Tab(selected = activeTab == 2, onClick = { activeTab = 2 }) {
                Text("In / Out Presets", color = if (activeTab == 2) MintAccent else MutedText, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(10.dp))
            }
        }

        Spacer(Modifier.height(8.dp))

        // --------------------------------------------------------------------
        // 5. TAB CONTENT
        // --------------------------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            when (activeTab) {
                0 -> {
                    // TRANSFORM SLIDERS
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        PropertySlider("Position X", posX, -500f..500f) {
                            posX = it
                            if (currentKfAtPlayhead != null) {
                                keyframeEngine.addOrUpdateKeyframe(targetId, currentKfAtPlayhead.copy(positionX = posX))
                            }
                        }

                        PropertySlider("Position Y", posY, -500f..500f) {
                            posY = it
                            if (currentKfAtPlayhead != null) {
                                keyframeEngine.addOrUpdateKeyframe(targetId, currentKfAtPlayhead.copy(positionY = posY))
                            }
                        }

                        PropertySlider("Scale", scale, 0.2f..3.0f) {
                            scale = it
                            if (currentKfAtPlayhead != null) {
                                keyframeEngine.addOrUpdateKeyframe(targetId, currentKfAtPlayhead.copy(scaleX = scale, scaleY = scale))
                            }
                        }

                        PropertySlider("Rotation", rot, -180f..180f) {
                            rot = it
                            if (currentKfAtPlayhead != null) {
                                keyframeEngine.addOrUpdateKeyframe(targetId, currentKfAtPlayhead.copy(rotation = rot))
                            }
                        }

                        PropertySlider("Opacity", opacity, 0.0f..1.0f) {
                            opacity = it
                            if (currentKfAtPlayhead != null) {
                                keyframeEngine.addOrUpdateKeyframe(targetId, currentKfAtPlayhead.copy(opacity = opacity))
                            }
                        }
                    }
                }
                1 -> {
                    // INTERPOLATION CURVES
                    Text("Select Keyframe Interpolation Curve", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(KeyframeInterpolation.values()) { interp ->
                            val isSel = currentKfAtPlayhead?.interpolation == interp
                            Card(
                                colors = CardDefaults.cardColors(containerColor = if (isSel) Color(0xFF1E3A2F) else CardBackground),
                                border = BorderStroke(1.dp, if (isSel) MintAccent else CardBorder),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.clickable {
                                    if (currentKfAtPlayhead != null) {
                                        keyframeEngine.addOrUpdateKeyframe(targetId, currentKfAtPlayhead.copy(interpolation = interp))
                                    }
                                }
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(interp.name.replace("_", " "), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    Text("Curve Easing", color = MintAccent, fontSize = 9.sp)
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // PRESET IN / OUT ANIMATIONS
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Preset IN Animation", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(listOf(PresetAnimationType.NONE, PresetAnimationType.FADE_IN, PresetAnimationType.ZOOM_IN, PresetAnimationType.SLIDE_IN_LEFT, PresetAnimationType.POP_IN, PresetAnimationType.BOUNCE_IN, PresetAnimationType.TYPEWRITER)) { type ->
                                val isSel = track.inAnimation == type
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = if (isSel) Color(0xFF1E3A2F) else CardBackground),
                                    border = BorderStroke(1.dp, if (isSel) MintAccent else CardBorder),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.clickable {
                                        keyframeEngine.setPresetAnimations(targetId, inAnim = type, inDur = track.inDurationMs, outAnim = track.outAnimation, outDur = track.outDurationMs, loopAnim = track.loopAnimation)
                                    }
                                ) {
                                    Text(type.name, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(10.dp))
                                }
                            }
                        }

                        Text("Preset LOOP Animation", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(listOf(PresetAnimationType.NONE, PresetAnimationType.PULSE_ZOOM, PresetAnimationType.FLOAT_BOUNCE, PresetAnimationType.SPIN_CONTINUOUS, PresetAnimationType.SHAKE_JITTER, PresetAnimationType.SWING_PENDULUM)) { type ->
                                val isSel = track.loopAnimation == type
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = if (isSel) Color(0xFF1E3A2F) else CardBackground),
                                    border = BorderStroke(1.dp, if (isSel) MintAccent else CardBorder),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.clickable {
                                        keyframeEngine.setPresetAnimations(targetId, inAnim = track.inAnimation, inDur = track.inDurationMs, outAnim = track.outAnimation, outDur = track.outDurationMs, loopAnim = type)
                                    }
                                ) {
                                    Text(type.name, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(10.dp))
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
private fun PropertySlider(label: String, value: Float, range: ClosedFloatingPointRange<Float>, onValueChange: (Float) -> Unit) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = Color.White, fontSize = 11.sp)
            Text(String.format("%.2f", value), color = MintAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(thumbColor = MintAccent, activeTrackColor = MintAccent),
            modifier = Modifier.height(22.dp)
        )
    }
}

private fun formatMs(ms: Long): String {
    val seconds = (ms / 1000) % 60
    val minutes = (ms / (1000 * 60)) % 60
    val millis = (ms % 1000) / 10
    return String.format("%02d:%02d.%02d", minutes, seconds, millis)
}
