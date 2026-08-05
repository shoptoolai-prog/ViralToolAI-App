package com.example.ui.screens

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.core.MediaImportHelper
import kotlinx.coroutines.delay

private val LuxuryDark = Color(0xFF0E0E10)
private val LuxurySurface = Color(0xFF16161A)
private val LuxuryCard = Color(0xFF1E1E24)
private val LuxuryBorder = Color(0x33FFFFFF)
private val PrimaryPurple = Color(0xFF7C3AED)
private val BrightPurple = Color(0xFFA78BFA)
private val EmeraldGreen = Color(0xFF10B981)
private val BrightEmerald = Color(0xFF34D399)
private val TextWhite = Color(0xFFF8FAFC)
private val TextSecondary = Color(0xFF94A3B8)

data class OptimizationItemData(
    val id: String,
    val title: String,
    val impactPct: Int,
    val description: String,
    val requiresApproval: Boolean = false,
    val category: String = "Enhancement"
)

@Composable
fun AiOptimizationOverlay(
    config: ProjectSetupConfig?,
    onDismiss: () -> Unit,
    onApplyOptimizations: (ProjectSetupConfig) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val optimizationItems = remember {
        listOf(
            OptimizationItemData("noise", "Remove Background Noise", 8, "Attenuates ambient audio hums and echo for crystal voice."),
            OptimizationItemData("voice", "Improve Voice Clarity", 5, "Equalizes midrange frequency response for punchy dialogue."),
            OptimizationItemData("thumbnail", "Generate Better Thumbnail", 12, "Extracts peak engagement frame with contrast boost."),
            OptimizationItemData("captions", "Auto Captions", 9, "Gens timed, high-readability animated subtitle overlays."),
            OptimizationItemData("highlight", "Product Highlight", 6, "Auto-zooms and highlights product during review segment."),
            OptimizationItemData("price", "Smart Price Sticker", 4, "Applies dynamic price callout badge on product view."),
            OptimizationItemData("logo", "Brand Logo Placement", 3, "Places subtle watermark in corner.", requiresApproval = true),
            OptimizationItemData("color", "Color Enhancement", 5, "Grading curve for vibrant colors & cinematic contrast."),
            OptimizationItemData("stab", "Stabilization", 4, "Reduces micro camera shakes on handheld clips."),
            OptimizationItemData("hook", "Hook Enhancement Suggestions", 7, "Trims initial 0.8s dead air to increase 3s retention rate.")
        )
    }

    // Enabled state map
    val enabledState = remember {
        mutableStateMapOf<String, Boolean>().apply {
            optimizationItems.forEach { item ->
                this[item.id] = true
            }
        }
    }

    var isApplying by remember { mutableStateOf(false) }
    var applyProgress by remember { mutableFloatStateOf(0f) }
    var currentPreviewMode by remember { mutableIntStateOf(0) } // 0 = Color, 1 = Noise, 2 = Price Sticker

    // Calculate dynamic optimized score
    val baseScore = 76
    val currentOptimizedScore = remember(enabledState.toMap()) {
        val activeImpactSum = optimizationItems.filter { enabledState[it.id] == true }.sumOf { it.impactPct }
        val maxImpactSum = optimizationItems.sumOf { it.impactPct }
        baseScore + ((15f * activeImpactSum / maxImpactSum.toFloat())).toInt()
    }

    // Processing simulation when user clicks "Apply Selected Optimizations"
    LaunchedEffect(isApplying) {
        if (isApplying) {
            for (step in 1..100) {
                applyProgress = step / 100f
                delay(18)
            }
            delay(200)
            val finalConfig = config ?: MediaImportHelper.createDefaultProjectConfig(emptyList())
            onApplyOptimizations(finalConfig)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { if (!isApplying) onDismiss() },
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.96f)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .border(
                        BorderStroke(1.dp, LuxuryBorder),
                        RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { },
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = LuxuryDark
            ) {
                if (isApplying) {
                    ApplyingOptimizationsProgressView(
                        progress = applyProgress,
                        selectedCount = enabledState.values.count { it }
                    )
                } else {
                    OptimizationDashboardContent(
                        config = config,
                        optimizationItems = optimizationItems,
                        enabledState = enabledState,
                        currentScore = baseScore,
                        optimizedScore = currentOptimizedScore,
                        previewMode = currentPreviewMode,
                        onSelectPreviewMode = { currentPreviewMode = it },
                        onToggleItem = { id ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            enabledState[id] = !(enabledState[id] ?: true)
                        },
                        onDismiss = onDismiss,
                        onApply = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isApplying = true
                        }
                    )
                }
            }
        }
    }
}

// ==================================================
// DASHBOARD CONTENT
// ==================================================
@Composable
private fun OptimizationDashboardContent(
    config: ProjectSetupConfig?,
    optimizationItems: List<OptimizationItemData>,
    enabledState: Map<String, Boolean>,
    currentScore: Int,
    optimizedScore: Int,
    previewMode: Int,
    onSelectPreviewMode: (Int) -> Unit,
    onToggleItem: (String) -> Unit,
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Drag Bar & Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Your Video Is Ready For Optimization",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "AI found several improvements that can increase engagement.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Scrollable Body
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. AI IMPROVEMENT SCORE CARD
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(BorderStroke(1.dp, LuxuryBorder), RoundedCornerShape(24.dp)),
                color = LuxurySurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "OPTIMIZATION IMPACT BOOST",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Current Score
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Current",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF26262B),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                            ) {
                                Box(
                                    modifier = Modifier.size(68.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$currentScore",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextWhite.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }

                        // Animated Boost Arrow
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = BrightPurple,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "+${optimizedScore - currentScore} pts",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreen
                            )
                        }

                        // Optimized Score
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Optimized",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EmeraldGreen
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = CircleShape,
                                color = PrimaryPurple.copy(alpha = 0.2f),
                                border = BorderStroke(1.5.dp, BrightPurple)
                            ) {
                                Box(
                                    modifier = Modifier.size(68.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$optimizedScore",
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextWhite
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        shape = CircleShape,
                        color = EmeraldGreen.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "Estimated +${((optimizedScore - currentScore) * 1.5).toInt()}% Higher Viral Reach",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // 2. OPTIMIZATION CHECKLIST
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(BorderStroke(1.dp, LuxuryBorder), RoundedCornerShape(24.dp)),
                color = LuxurySurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "AI Enhancements Checklist",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "${enabledState.values.count { it }}/${optimizationItems.size} Selected",
                            fontSize = 12.sp,
                            color = BrightPurple,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    optimizationItems.forEach { item ->
                        val isChecked = enabledState[item.id] ?: true
                        OptimizationRowItem(
                            item = item,
                            isChecked = isChecked,
                            onToggle = { onToggleItem(item.id) }
                        )
                    }
                }
            }

            // 3. SMART PREVIEW (BEFORE / AFTER CARD)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(BorderStroke(1.dp, LuxuryBorder), RoundedCornerShape(24.dp)),
                color = LuxurySurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Smart Preview (Before / After)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.08f)
                        ) {
                            Text(
                                text = "Applies on approval",
                                fontSize = 10.5.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Mode Selection Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val modes = listOf("Color & Tone", "Noise Filter", "Price Tag")
                        modes.forEachIndexed { idx, label ->
                            val isSel = previewMode == idx
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onSelectPreviewMode(idx) },
                                color = if (isSel) PrimaryPurple else LuxuryCard,
                                border = BorderStroke(1.dp, if (isSel) BrightPurple else Color.Transparent)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = TextWhite,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }

                    // Split Card Before / After Graphic
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(LuxuryCard),
                        contentAlignment = Alignment.Center
                    ) {
                        val firstMedia = config?.selectedMedia?.firstOrNull()

                        Row(modifier = Modifier.fillMaxSize()) {
                            // BEFORE Half
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(Color(0xFF151518))
                            ) {
                                if (firstMedia != null && firstMedia.uri != Uri.EMPTY) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(firstMedia.uri)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Original",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                // Dim overlay for "Before"
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.35f))
                                )
                                Surface(
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(10.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color.Black.copy(alpha = 0.7f)
                                ) {
                                    Text(
                                        text = "ORIGINAL",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            // Center Divider Line
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .fillMaxHeight()
                                    .background(BrightPurple)
                            )

                            // AFTER Half (Optimized)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(Color(0xFF1E1E28))
                            ) {
                                if (firstMedia != null && firstMedia.uri != Uri.EMPTY) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(firstMedia.uri)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Optimized",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Surface(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(10.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    color = PrimaryPurple
                                ) {
                                    Text(
                                        text = "AI OPTIMIZED",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }

                                if (previewMode == 2) {
                                    // Smart Price Tag Overlay Example
                                    Surface(
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .scale(0.9f),
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFFEF4444)
                                    ) {
                                        Text(
                                            text = "SPECIAL ₹399",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = TextWhite,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. ESTIMATED RESULTS METRICS
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(BorderStroke(1.dp, LuxuryBorder), RoundedCornerShape(24.dp)),
                color = LuxurySurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Estimated Performance Results",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ResultMetricCard(
                            title = "Watch Time",
                            value = "28.4s",
                            subtext = "+38% vs Avg",
                            color = BrightPurple,
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetricCard(
                            title = "Est. CTR",
                            value = "8.6%",
                            subtext = "+14% High",
                            color = EmeraldGreen,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ResultMetricCard(
                            title = "Est. Shares",
                            value = "+22%",
                            subtext = "Viral Hook",
                            color = Color(0xFF3B82F6),
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetricCard(
                            title = "Est. Saves",
                            value = "+31%",
                            subtext = "Product Info",
                            color = Color(0xFFF59E0B),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Text(
                        text = "*Informative AI estimates based on platform benchmarks.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Bottom Action Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Button(
                onClick = onApply,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Apply Selected Optimizations",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = TextWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// ==================================================
// ROW ITEM COMPOSABLE
// ==================================================
@Composable
private fun OptimizationRowItem(
    item: OptimizationItemData,
    isChecked: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                if (isChecked) PrimaryPurple.copy(alpha = 0.5f) else Color.Transparent,
                RoundedCornerShape(16.dp)
            )
            .clickable { onToggle() },
        color = if (isChecked) LuxuryCard else Color(0xFF141418)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Impact Badge
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = EmeraldGreen.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "+${item.impactPct}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = EmeraldGreen,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = item.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    if (item.requiresApproval) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFF59E0B).copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Approval Needed",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFBBF24),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    text = item.description,
                    fontSize = 11.5.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Switch(
                checked = isChecked,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TextWhite,
                    checkedTrackColor = PrimaryPurple,
                    uncheckedThumbColor = TextSecondary,
                    uncheckedTrackColor = Color(0xFF26262B)
                )
            )
        }
    }
}

@Composable
private fun ResultMetricCard(
    title: String,
    value: String,
    subtext: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(BorderStroke(1.dp, LuxuryBorder), RoundedCornerShape(16.dp)),
        color = LuxuryCard
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = title, fontSize = 11.sp, color = TextSecondary)
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite)
            Text(text = subtext, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = color)
        }
    }
}

// ==================================================
// APPLYING PROGRESS VIEW
// ==================================================
@Composable
private fun ApplyingOptimizationsProgressView(
    progress: Float,
    selectedCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                color = PrimaryPurple,
                trackColor = LuxuryCard,
                strokeWidth = 8.dp
            )
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = BrightPurple,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Applying $selectedCount AI Optimizations...",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Rendering audio denoiser, color curve and smart stickers",
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${(progress * 100).toInt()}%",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = BrightEmerald
        )
    }
}
