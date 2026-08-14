package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.Color as AndroidColor
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ============================================================================
// MASTER PHASE E-3 — PROFESSIONAL GPU TRANSITIONS STUDIO SHEET
// ============================================================================

private val DarkBackground = Color(0xFF090A0F)
private val CardBackground = Color(0xFF13151F)
private val CardBorder = Color(0xFF202434)
private val MintAccent = Color(0xFF38E8A5)
private val MutedText = Color(0xFF8E95AD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GPUTransitionsStudioSheet(
    clipA: TimelineClip?,
    clipB: TimelineClip?,
    onApplyTransition: (TransitionType, Long) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val transitionEngine = remember { TransitionEngine.getInstance(context) }
    val scope = rememberCoroutineScope()

    var selectedType by remember { mutableStateOf(TransitionType.FADE_CROSS) }
    var transitionDurationMs by remember { mutableStateOf(800L) }
    var previewProgress by remember { mutableStateOf(0f) }
    var isPreviewPlaying by remember { mutableStateOf(true) }

    // Dummy sample frames for live transition preview
    val sampleFrameA = remember {
        Bitmap.createBitmap(640, 360, Bitmap.Config.ARGB_8888).apply {
            val canvas = android.graphics.Canvas(this)
            val paint = android.graphics.Paint()
            paint.color = AndroidColor.parseColor("#1B2A4A")
            canvas.drawRect(0f, 0f, 640f, 360f, paint)
            paint.color = AndroidColor.WHITE
            paint.textSize = 32f
            paint.isAntiAlias = true
            canvas.drawText("CLIP A (OUTGOING)", 160f, 190f, paint)
        }
    }

    val sampleFrameB = remember {
        Bitmap.createBitmap(640, 360, Bitmap.Config.ARGB_8888).apply {
            val canvas = android.graphics.Canvas(this)
            val paint = android.graphics.Paint()
            paint.color = AndroidColor.parseColor("#4A1B2A")
            canvas.drawRect(0f, 0f, 640f, 360f, paint)
            paint.color = AndroidColor.parseColor("#38E8A5")
            paint.textSize = 32f
            paint.isAntiAlias = true
            canvas.drawText("CLIP B (INCOMING)", 160f, 190f, paint)
        }
    }

    var renderedPreviewFrame by remember { mutableStateOf<Bitmap?>(sampleFrameA) }

    // Loop transition preview progress (0.0 to 1.0)
    LaunchedEffect(selectedType, isPreviewPlaying) {
        if (!isPreviewPlaying) return@LaunchedEffect
        while (true) {
            for (step in 0..50) {
                previewProgress = step / 50f
                withContext(Dispatchers.Default) {
                    renderedPreviewFrame = transitionEngine.renderer.renderTransitionFrame(
                        frameA = sampleFrameA,
                        frameB = sampleFrameB,
                        progress = previewProgress,
                        type = selectedType
                    )
                }
                delay(20)
            }
            delay(400) // Pause at end before repeat
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
                    text = "TRANSITIONS STUDIO",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MintAccent,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${clipA?.name ?: "Clip A"} ➔ ${clipB?.name ?: "Clip B"}",
                    fontSize = 10.sp,
                    color = MutedText
                )
            }

            Button(
                onClick = {
                    onApplyTransition(selectedType, transitionDurationMs)
                    onClose()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MintAccent),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Apply Junction", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        // --------------------------------------------------------------------
        // 2. LIVE PREVIEW VIEWPORT
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
            renderedPreviewFrame?.let { bmp ->
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = "GPU Transition Live Preview",
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Progress Bar Overlay
            LinearProgressIndicator(
                progress = { previewProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(4.dp),
                color = MintAccent,
                trackColor = Color.Transparent
            )
        }

        Spacer(Modifier.height(12.dp))

        // --------------------------------------------------------------------
        // 3. TRANSITION CONTROLS (Duration Slider)
        // --------------------------------------------------------------------
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Transition Duration", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("${transitionDurationMs} ms", color = MintAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = transitionDurationMs.toFloat(),
                onValueChange = { transitionDurationMs = it.toLong() },
                valueRange = 200f..2500f,
                colors = SliderDefaults.colors(thumbColor = MintAccent, activeTrackColor = MintAccent)
            )
        }

        Spacer(Modifier.height(8.dp))

        // --------------------------------------------------------------------
        // 4. TRANSITION SELECTION GRID
        // --------------------------------------------------------------------
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            items(transitionEngine.PRESET_TRANSITIONS) { item ->
                val isSel = selectedType == item.type
                Card(
                    colors = CardDefaults.cardColors(containerColor = if (isSel) Color(0xFF1E3A2F) else CardBackground),
                    border = BorderStroke(1.dp, if (isSel) MintAccent else CardBorder),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedType = item.type }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = item.type.name.replace("_", " "),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Realtime GPU",
                            color = if (isSel) MintAccent else MutedText,
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }
    }
}
