package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color as AndroidColor
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ============================================================================
// MASTER PHASE 10.4 — PROFESSIONAL GPU FILTER ENGINE STUDIO (GPUFilterStudioSheet.kt)
// ============================================================================

private val DarkBackground = Color(0xFF090A0F)
private val CardBackground = Color(0xFF13151F)
private val CardBorder = Color(0xFF202434)
private val MintAccent = Color(0xFF38E8A5)
private val MutedText = Color(0xFF8E95AD)
private val DangerRed = Color(0xFFFF4D4D)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GPUFilterStudioSheet(
    sampleBitmap: Bitmap? = null,
    onApplyToTimeline: (Bitmap?, List<FilterLayer>) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val filterEngine = remember { FilterEngine.getInstance(context) }
    val stackState by filterEngine.stackState.collectAsState()
    val scope = rememberCoroutineScope()

    // Default Sample Bitmap if none provided
    val defaultSourceBitmap = remember(sampleBitmap) {
        sampleBitmap ?: Bitmap.createBitmap(640, 360, Bitmap.Config.ARGB_8888).apply {
            val canvas = android.graphics.Canvas(this)
            val paint = android.graphics.Paint()
            paint.color = AndroidColor.parseColor("#1C2333")
            canvas.drawRect(0f, 0f, 640f, 360f, paint)
            paint.color = AndroidColor.parseColor("#38E8A5")
            paint.textSize = 28f
            paint.isAntiAlias = true
            canvas.drawText("SAMPLE INPUT PREVIEW", 180f, 190f, paint)
        }
    }

    var isHoldingCompare by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) } // 0: Presets, 1: Basic Light, 2: Effects & Grain, 3: HSL Color, 4: Stack Layers
    var selectedExportResolution by remember { mutableStateOf(ExportResolution.RES_1080P) }
    var isExporting by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }

    // Live Rendered Bitmap Preview
    var processedPreviewBitmap by remember { mutableStateOf<Bitmap?>(defaultSourceBitmap) }

    // Trigger fast re-render whenever filter stack changes or user toggles compare
    LaunchedEffect(stackState, isHoldingCompare) {
        withContext(Dispatchers.Default) {
            if (isHoldingCompare) {
                processedPreviewBitmap = defaultSourceBitmap
            } else {
                processedPreviewBitmap = filterEngine.previewRenderer.renderProxyPreview(
                    sourceBitmap = defaultSourceBitmap,
                    stack = stackState.layers
                )
            }
        }
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
                    text = "FILTERS STUDIO",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MintAccent,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${stackState.layers.size} Active Layers • Realtime 60FPS",
                    fontSize = 10.sp,
                    color = MutedText
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                // Undo
                IconButton(onClick = { filterEngine.undo() }) {
                    Icon(Icons.Default.Undo, contentDescription = "Undo", tint = Color.White)
                }
                // Redo
                IconButton(onClick = { filterEngine.redo() }) {
                    Icon(Icons.Default.Redo, contentDescription = "Redo", tint = Color.White)
                }
                // Apply
                Button(
                    onClick = {
                        scope.launch {
                            isExporting = true
                            val exportBitmap = withContext(Dispatchers.Default) {
                                filterEngine.exportRenderer.exportFullResolution(
                                    sourceBitmap = defaultSourceBitmap,
                                    stack = stackState.layers,
                                    targetResolution = selectedExportResolution
                                )
                            }
                            isExporting = false
                            onApplyToTimeline(exportBitmap, stackState.layers)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintAccent),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isExporting) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.Black, strokeWidth = 2.dp)
                    } else {
                        Text("Apply", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // --------------------------------------------------------------------
        // 2. LIVE VIEWPORT & BEFORE/AFTER HOLD TO COMPARE
        // --------------------------------------------------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(CardBackground)
                .border(1.dp, CardBorder, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            processedPreviewBitmap?.let { bmp ->
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = "Filter Preview",
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Hold to Compare Button
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isHoldingCompare) DangerRed else Color.Black.copy(alpha = 0.7f))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isHoldingCompare = true
                                tryAwaitRelease()
                                isHoldingCompare = false
                            }
                        )
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Compare, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Text(
                        text = if (isHoldingCompare) "ORIGINAL" else "Hold Compare",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Export Resolution Pill
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { showExportDialog = true }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = selectedExportResolution.label,
                    color = MintAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // --------------------------------------------------------------------
        // 3. TAB CATEGORY SELECTION
        // --------------------------------------------------------------------
        val tabNames = listOf("Presets", "Light", "Effects", "HSL Color", "Stack (${stackState.layers.size})")
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = MintAccent,
            edgePadding = 16.dp,
            divider = {}
        ) {
            tabNames.forEachIndexed { idx, name ->
                Tab(
                    selected = selectedTab == idx,
                    onClick = { selectedTab = idx },
                    text = {
                        Text(
                            text = name,
                            fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == idx) MintAccent else MutedText,
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // --------------------------------------------------------------------
        // 4. TAB CONTROLS & INSPECTORS
        // --------------------------------------------------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            when (selectedTab) {
                0 -> PresetsTab(filterEngine = filterEngine)
                1 -> BasicLightTab(filterEngine = filterEngine, stackState = stackState)
                2 -> EffectsTab(filterEngine = filterEngine, stackState = stackState)
                3 -> HslColorTab(filterEngine = filterEngine, stackState = stackState)
                4 -> StackLayersInspector(filterEngine = filterEngine, stackState = stackState)
            }
        }
    }

    // Export Resolution Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            containerColor = CardBackground,
            title = { Text("Export Quality Target", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    ExportResolution.values().forEach { res ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedExportResolution = res
                                    showExportDialog = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedExportResolution == res,
                                onClick = {
                                    selectedExportResolution = res
                                    showExportDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = MintAccent)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(res.label, color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Close", color = MintAccent)
                }
            }
        )
    }
}

// ----------------------------------------------------------------------------
// TAB 0: PRESET CATALOG
// ----------------------------------------------------------------------------
@Composable
private fun PresetsTab(filterEngine: FilterEngine) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(filterEngine.PRESET_FILTERS) { preset ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                border = BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { filterEngine.pushStack(listOf(preset.copy())) }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = preset.name,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "GPU Rendered",
                        color = MintAccent,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------------------------------
// TAB 1: BASIC LIGHT & COLOR
// ----------------------------------------------------------------------------
@Composable
private fun BasicLightTab(filterEngine: FilterEngine, stackState: FilterStackState) {
    val activeLayer = stackState.layers.lastOrNull()

    if (activeLayer == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No Active Filter Layer. Select a Preset or Add a Layer.", color = MutedText, fontSize = 12.sp)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SliderControl("Brightness", activeLayer.brightness, -1.0f..1.0f) {
            filterEngine.updateLayer(activeLayer.copy(brightness = it))
        }
        SliderControl("Contrast", activeLayer.contrast, 0.2f..2.0f) {
            filterEngine.updateLayer(activeLayer.copy(contrast = it))
        }
        SliderControl("Exposure", activeLayer.exposure, -2.0f..2.0f) {
            filterEngine.updateLayer(activeLayer.copy(exposure = it))
        }
        SliderControl("Saturation", activeLayer.saturation, 0.0f..2.0f) {
            filterEngine.updateLayer(activeLayer.copy(saturation = it))
        }
        SliderControl("Temperature (Warm/Cool)", activeLayer.temperature, -100f..100f) {
            filterEngine.updateLayer(activeLayer.copy(temperature = it))
        }
        SliderControl("Tint (Green/Magenta)", activeLayer.tint, -100f..100f) {
            filterEngine.updateLayer(activeLayer.copy(tint = it))
        }
    }
}

// ----------------------------------------------------------------------------
// TAB 2: GRAIN, VIGNETTE, GLOW
// ----------------------------------------------------------------------------
@Composable
private fun EffectsTab(filterEngine: FilterEngine, stackState: FilterStackState) {
    val activeLayer = stackState.layers.lastOrNull()

    if (activeLayer == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No Active Filter Layer.", color = MutedText, fontSize = 12.sp)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SliderControl("Vignette Intensity", activeLayer.vignette, 0.0f..1.0f) {
            filterEngine.updateLayer(activeLayer.copy(vignette = it))
        }
        SliderControl("Film Grain", activeLayer.filmGrain, 0.0f..1.0f) {
            filterEngine.updateLayer(activeLayer.copy(filmGrain = it))
        }
        SliderControl("Fade", activeLayer.fade, 0.0f..1.0f) {
            filterEngine.updateLayer(activeLayer.copy(fade = it))
        }
        SliderControl("Glow Effect", activeLayer.glow, 0.0f..1.0f) {
            filterEngine.updateLayer(activeLayer.copy(glow = it))
        }
    }
}

// ----------------------------------------------------------------------------
// TAB 3: HSL COLOR TUNING
// ----------------------------------------------------------------------------
@Composable
private fun HslColorTab(filterEngine: FilterEngine, stackState: FilterStackState) {
    val activeLayer = stackState.layers.lastOrNull() ?: return
    var selectedChannelName by remember { mutableStateOf("Red") }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Red", "Orange", "Yellow", "Green", "Aqua", "Blue", "Purple", "Magenta").forEach { ch ->
                val isSel = selectedChannelName == ch
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (isSel) MintAccent else CardBackground)
                        .clickable { selectedChannelName = ch },
                    contentAlignment = Alignment.Center
                ) {
                    Text(ch.take(1), color = if (isSel) Color.Black else Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("HSL Channel Tuning: $selectedChannelName", color = MintAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        SliderControl("Hue Shift", 0f, -180f..180f) {}
        SliderControl("Saturation", 0f, -100f..100f) {}
        SliderControl("Luminance", 0f, -100f..100f) {}
    }
}

// ----------------------------------------------------------------------------
// TAB 4: MULTI-LAYER STACK INSPECTOR
// ----------------------------------------------------------------------------
@Composable
private fun StackLayersInspector(filterEngine: FilterEngine, stackState: FilterStackState) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Filter Stack (${stackState.layers.size})", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            TextButton(onClick = { filterEngine.resetStack() }) {
                Text("Reset All", color = DangerRed, fontSize = 11.sp)
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(stackState.layers) { index, layer ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, CardBorder),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Checkbox(
                                    checked = layer.enabled,
                                    onCheckedChange = { filterEngine.toggleLayerEnabled(layer.id) },
                                    colors = CheckboxDefaults.colors(checkedColor = MintAccent)
                                )
                                Text(layer.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            Row {
                                IconButton(onClick = { filterEngine.duplicateLayer(layer.id) }) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, tint = MutedText, modifier = Modifier.size(16.dp))
                                }
                                IconButton(onClick = { filterEngine.removeLayer(layer.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = DangerRed, modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        // Layer Intensity Slider
                        SliderControl("Layer Opacity", layer.intensity, 0.0f..1.0f) {
                            filterEngine.updateLayerIntensity(layer.id, it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SliderControl(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = MutedText, fontSize = 11.sp)
            Text(String.format("%.2f", value), color = MintAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = MintAccent,
                activeTrackColor = MintAccent,
                inactiveTrackColor = CardBorder
            ),
            modifier = Modifier.height(28.dp)
        )
    }
}
