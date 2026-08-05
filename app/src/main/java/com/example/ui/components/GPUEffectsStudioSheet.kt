package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color as AndroidColor
import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
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
// MASTER PHASE E-2 — PROFESSIONAL GPU EFFECTS STUDIO SHEET (GPUEffectsStudioSheet.kt)
// ============================================================================

private val DarkBackground = Color(0xFF090A0F)
private val CardBackground = Color(0xFF13151F)
private val CardBorder = Color(0xFF202434)
private val MintAccent = Color(0xFF38E8A5)
private val MutedText = Color(0xFF8E95AD)
private val DangerRed = Color(0xFFFF4D4D)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GPUEffectsStudioSheet(
    sampleBitmap: Bitmap? = null,
    onApplyToTimeline: (List<EffectLayer>) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val effectsEngine = remember { EffectsEngine.getInstance(context) }
    val stackState by effectsEngine.stackState.collectAsState()
    val scope = rememberCoroutineScope()

    val baseBitmap = remember(sampleBitmap) {
        sampleBitmap ?: Bitmap.createBitmap(640, 360, Bitmap.Config.ARGB_8888).apply {
            val canvas = android.graphics.Canvas(this)
            val paint = android.graphics.Paint()
            paint.color = AndroidColor.parseColor("#151922")
            canvas.drawRect(0f, 0f, 640f, 360f, paint)
            paint.color = AndroidColor.parseColor("#38E8A5")
            paint.textSize = 28f
            paint.isAntiAlias = true
            canvas.drawText("GPU EFFECTS PREVIEW", 180f, 190f, paint)
        }
    }

    var selectedCategory by remember { mutableStateOf(EffectCategory.ALL) }
    var processedPreviewFrame by remember { mutableStateOf<Bitmap?>(baseBitmap) }
    var isHoldingCompare by remember { mutableStateOf(false) }

    // Live effect preview ticker
    LaunchedEffect(stackState, isHoldingCompare) {
        withContext(Dispatchers.Default) {
            if (isHoldingCompare) {
                processedPreviewFrame = baseBitmap
            } else {
                processedPreviewFrame = effectsEngine.previewRenderer.renderPreviewFrame(
                    sourceBitmap = baseBitmap,
                    effectStack = stackState.effects,
                    timeMs = System.currentTimeMillis()
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
                    text = "EFFECTS STUDIO",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MintAccent,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${stackState.effects.size} Active FX Layers • Realtime Preview",
                    fontSize = 10.sp,
                    color = MutedText
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = { effectsEngine.undo() }) {
                    Icon(Icons.Default.Undo, contentDescription = "Undo", tint = Color.White)
                }
                IconButton(onClick = { effectsEngine.redo() }) {
                    Icon(Icons.Default.Redo, contentDescription = "Redo", tint = Color.White)
                }
                Button(
                    onClick = {
                        onApplyToTimeline(stackState.effects)
                        onClose()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintAccent),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Apply FX", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // --------------------------------------------------------------------
        // 2. LIVE VIEWPORT & BEFORE/AFTER HOLD COMPARE
        // --------------------------------------------------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(CardBackground)
                .border(1.dp, CardBorder, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            processedPreviewFrame?.let { bmp ->
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = "GPU Effect Live Frame",
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
        }

        Spacer(Modifier.height(10.dp))

        // --------------------------------------------------------------------
        // 3. CATEGORY TABS
        // --------------------------------------------------------------------
        ScrollableTabRow(
            selectedTabIndex = EffectCategory.values().indexOf(selectedCategory).coerceAtLeast(0),
            containerColor = Color.Transparent,
            contentColor = MintAccent,
            edgePadding = 16.dp,
            divider = {}
        ) {
            EffectCategory.values().forEach { cat ->
                val isSel = selectedCategory == cat
                Tab(
                    selected = isSel,
                    onClick = { selectedCategory = cat },
                    text = {
                        Text(
                            text = cat.displayName,
                            color = if (isSel) MintAccent else MutedText,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // --------------------------------------------------------------------
        // 4. MAIN EFFECTS GRID & STACK INSPECTOR
        // --------------------------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            val filteredList = if (selectedCategory == EffectCategory.ALL) {
                effectsEngine.PRESET_EFFECTS
            } else {
                effectsEngine.PRESET_EFFECTS.filter { it.category == selectedCategory }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
            ) {
                items(filteredList) { effect ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, CardBorder),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { effectsEngine.addEffect(effect.copy()) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = effect.name,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "${effect.category.displayName} • GPU",
                                color = MintAccent,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Active Effect Stack Inspector
            if (stackState.effects.isNotEmpty()) {
                Text("Active Stack Layers (${stackState.effects.size})", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(Modifier.height(4.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.4f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    itemsIndexed(stackState.effects) { idx, fx ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                            border = BorderStroke(1.dp, CardBorder),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Checkbox(
                                            checked = fx.enabled,
                                            onCheckedChange = { effectsEngine.toggleEffectEnabled(fx.id) },
                                            colors = CheckboxDefaults.colors(checkedColor = MintAccent)
                                        )
                                        Text(fx.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }

                                    Row {
                                        IconButton(onClick = { effectsEngine.duplicateEffect(fx.id) }) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = MutedText, modifier = Modifier.size(14.dp))
                                        }
                                        IconButton(onClick = { effectsEngine.removeEffect(fx.id) }) {
                                            Icon(Icons.Default.Delete, contentDescription = null, tint = DangerRed, modifier = Modifier.size(14.dp))
                                        }
                                    }
                                }

                                // Intensity Slider
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Intensity", color = MutedText, fontSize = 10.sp)
                                    Text(String.format("%.2f", fx.intensity), color = MintAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Slider(
                                    value = fx.intensity,
                                    onValueChange = { effectsEngine.updateEffect(fx.copy(intensity = it)) },
                                    valueRange = 0f..1f,
                                    colors = SliderDefaults.colors(thumbColor = MintAccent, activeTrackColor = MintAccent),
                                    modifier = Modifier.height(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
