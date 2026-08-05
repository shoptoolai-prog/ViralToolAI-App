package com.example.ui.screens

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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

@Composable
fun AiAnalysisOverlay(
    config: ProjectSetupConfig?,
    onDismiss: () -> Unit,
    onContinueToOptimization: (ProjectSetupConfig) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val tasks = remember {
        listOf(
            "Reading Video Metadata",
            "Detecting Product",
            "Detecting Human Face",
            "Detecting Background",
            "Detecting Voice",
            "Detecting Music",
            "Detecting Silence",
            "Detecting Camera Shake",
            "Detecting Lighting",
            "Detecting Product Visibility",
            "Detecting Hook",
            "Detecting CTA",
            "Generating Thumbnail",
            "Calculating Viral Score"
        )
    }

    var currentTaskIndex by remember { mutableIntStateOf(0) }
    var isAnalyzing by remember { mutableStateOf(true) }
    var selectedTitleIndex by remember { mutableIntStateOf(0) }
    var isThumbnailUsed by remember { mutableStateOf(false) }

    // Live Analysis Loop
    LaunchedEffect(Unit) {
        for (i in tasks.indices) {
            currentTaskIndex = i
            delay(260L)
        }
        delay(350L)
        isAnalyzing = false
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
                ) { if (!isAnalyzing) onDismiss() },
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
                if (isAnalyzing) {
                    AiProcessingView(
                        tasks = tasks,
                        currentIndex = currentTaskIndex,
                        onDismiss = onDismiss
                    )
                } else {
                    AiReportView(
                        config = config,
                        selectedTitleIndex = selectedTitleIndex,
                        onSelectTitle = { selectedTitleIndex = it },
                        isThumbnailUsed = isThumbnailUsed,
                        onToggleThumbnail = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isThumbnailUsed = !isThumbnailUsed
                        },
                        onDismiss = onDismiss,
                        onContinue = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            val finalConfig = config ?: MediaImportHelper.createDefaultProjectConfig(emptyList())
                            onContinueToOptimization(finalConfig)
                        }
                    )
                }
            }
        }
    }
}

// ==================================================
// STEP 1 & 2: PREMIUM AI PROCESSING VIEW
// ==================================================
@Composable
private fun AiProcessingView(
    tasks: List<String>,
    currentIndex: Int,
    onDismiss: () -> Unit
) {
    val listState = rememberLazyListState()

    // Auto-scroll list as tasks complete
    LaunchedEffect(currentIndex) {
        if (currentIndex > 2) {
            listState.animateScrollToItem(currentIndex)
        }
    }

    // Infinite transition for AI Orb pulse & glow
    val infiniteTransition = rememberInfiniteTransition(label = "ai_orb")
    val orbScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb_scale"
    )
    val orbRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orb_rotation"
    )

    val progressAnimated by animateFloatAsState(
        targetValue = ((currentIndex + 1).toFloat() / tasks.size.toFloat()).coerceIn(0f, 1f),
        animationSpec = tween(220),
        label = "progress"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Drag Bar & Top Title
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = BrightPurple,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "AI Content Engine",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
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

        Spacer(modifier = Modifier.height(28.dp))

        // Center Animated AI Orb
        Box(
            modifier = Modifier.size(140.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background Aura Glow
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .scale(orbScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                PrimaryPurple.copy(alpha = 0.45f),
                                Color(0xFF00E5FF).copy(alpha = 0.25f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Rotating Outer Ring
            Canvas(
                modifier = Modifier
                    .size(120.dp)
                    .rotate(orbRotation)
            ) {
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            PrimaryPurple,
                            Color(0xFF00E5FF),
                            EmeraldGreen,
                            PrimaryPurple
                        )
                    ),
                    startAngle = 0f,
                    sweepAngle = 280f,
                    useCenter = false,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // Core Glass Orb
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .scale(orbScale)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF9333EA),
                                Color(0xFF3B82F6),
                                Color(0xFF10B981)
                            )
                        )
                    )
                    .border(1.5.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Analyzing your content...",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Extracting video metrics, viral indicators & AI suggestions",
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Progress Bar
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live Scanning",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                Text(
                    text = "${(progressAnimated * 100).toInt()}%",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrightPurple
                )
            }

            LinearProgressIndicator(
                progress = { progressAnimated },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = PrimaryPurple,
                trackColor = LuxuryCard
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Task CheckList Box
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .border(BorderStroke(1.dp, LuxuryBorder), RoundedCornerShape(20.dp)),
            color = LuxuryCard
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(tasks) { index, task ->
                    val isDone = index < currentIndex
                    val isCurrent = index == currentIndex

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isCurrent) PrimaryPurple.copy(alpha = 0.15f)
                                else Color.Transparent
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isDone) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else if (isCurrent) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = BrightPurple,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.15f))
                                )
                            }
                        }

                        Text(
                            text = task,
                            fontSize = 14.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            color = when {
                                isDone -> TextWhite
                                isCurrent -> BrightPurple
                                else -> TextSecondary.copy(alpha = 0.5f)
                            }
                        )
                    }
                }
            }
        }
    }
}

// ==================================================
// STEP 3: COMPREHENSIVE AI REPORT VIEW
// ==================================================
@Composable
private fun AiReportView(
    config: ProjectSetupConfig?,
    selectedTitleIndex: Int,
    onSelectTitle: (Int) -> Unit,
    isThumbnailUsed: Boolean,
    onToggleThumbnail: () -> Unit,
    onDismiss: () -> Unit,
    onContinue: () -> Unit
) {
    val scrollState = rememberScrollState()

    val sampleTitles = remember {
        listOf(
            "This ₹399 Saree Looks Premium 😍",
            "Worth Buying? Honest Review",
            "Best Budget Saree Under ₹500",
            "Meesho Viral Find You Need to Try! 🔥",
            "Unboxing & Try-On: Is it Worth It?"
        )
    }

    val hashtags = remember {
        listOf("#MeeshoFinds", "#FashionReview", "#BudgetShopping", "#SareeHaul", "#ViralReels", "#TrendingOutfits", "#ShoppingHaul")
    }

    val suggestions = remember {
        listOf(
            "Remove background noise",
            "Increase product zoom",
            "Add animated price tag",
            "Add brand logo",
            "Improve hook",
            "Generate captions",
            "Apply beauty color correction"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Drag Bar & Header Bar
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
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "AI Analysis Complete",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                    Text(
                        text = config?.selectedMedia?.firstOrNull()?.title ?: "Imported Video Item",
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

        // Main Report Scrollable Content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. AI VIRAL SCORE GAUGE
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
                        text = "AI VIRAL SCORE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Circular Ring Gauge
                    Box(
                        modifier = Modifier.size(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 12.dp.toPx()
                            val diameter = size.minDimension - strokeWidth
                            val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
                            val arcSize = Size(diameter, diameter)

                            // Track Ring
                            drawArc(
                                color = Color(0xFF26262B),
                                startAngle = 135f,
                                sweepAngle = 270f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )

                            // Active Score Arc (92%)
                            drawArc(
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        PrimaryPurple,
                                        Color(0xFF00E5FF),
                                        EmeraldGreen
                                    )
                                ),
                                startAngle = 135f,
                                sweepAngle = 270f * 0.92f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "92",
                                fontSize = 44.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextWhite
                            )
                            Text(
                                text = "out of 100",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Status Pill
                    Surface(
                        shape = CircleShape,
                        color = EmeraldGreen.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldGreen)
                            )
                            Text(
                                text = "Excellent Viral Potential",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreen
                            )
                        }
                    }
                }
            }

            // 2. BREAKDOWN METRICS
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
                        text = "Score Breakdown",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    val metrics = listOf(
                        "Hook" to 92,
                        "Audio" to 88,
                        "Lighting" to 95,
                        "Camera Stability" to 90,
                        "Product Visibility" to 97,
                        "Voice Clarity" to 94,
                        "Subtitle Readability" to 91,
                        "Thumbnail Potential" to 96
                    )

                    metrics.forEach { (label, valScore) ->
                        MetricRow(label = label, score = valScore)
                    }
                }
            }

            // 3. AI SUGGESTIONS
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Lightbulb,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "AI Optimization Suggestions",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    suggestions.forEach { suggestion ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldGreen.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = suggestion,
                                fontSize = 13.5.sp,
                                color = TextWhite
                            )
                        }
                    }
                }
            }

            // 4. RECOMMENDED THUMBNAIL
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
                    Text(
                        text = "Recommended Thumbnail",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(170.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(LuxuryCard),
                        contentAlignment = Alignment.Center
                    ) {
                        val firstMedia = config?.selectedMedia?.firstOrNull()
                        if (firstMedia != null && firstMedia.uri != Uri.EMPTY) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(firstMedia.uri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Thumbnail Preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF4C1D95),
                                                Color(0xFF1E1B4B)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = TextWhite.copy(alpha = 0.5f),
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }

                        // Badge Tag Overlay
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black.copy(alpha = 0.75f)
                        ) {
                            Text(
                                text = "FRAME 00:02.14 • HIGH ENGAGEMENT HOOK",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrightPurple,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onToggleThumbnail,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isThumbnailUsed) EmeraldGreen else Color(0xFF26262B)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (isThumbnailUsed) Icons.Default.Check else Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = TextWhite,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isThumbnailUsed) "Thumbnail Selected" else "Use Thumbnail",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                    }
                }
            }

            // 5. GENERATED TITLES
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
                    Text(
                        text = "AI Optimized Viral Titles",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    sampleTitles.forEachIndexed { idx, titleText ->
                        val isSelected = selectedTitleIndex == idx
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) PrimaryPurple else Color.Transparent,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { onSelectTitle(idx) },
                            color = if (isSelected) PrimaryPurple.copy(alpha = 0.15f) else LuxuryCard
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { onSelectTitle(idx) },
                                    colors = RadioButtonDefaults.colors(selectedColor = BrightPurple)
                                )
                                Text(
                                    text = titleText,
                                    fontSize = 13.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = TextWhite,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // 6. HASHTAGS
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
                    Text(
                        text = "Recommended Hashtags",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    // Wrap Flow
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        val rows = hashtags.chunked(3)
                        rows.forEach { rowTags ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                rowTags.forEach { tag ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = PrimaryPurple.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, PrimaryPurple.copy(alpha = 0.4f))
                                    ) {
                                        Text(
                                            text = tag,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = BrightPurple,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
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
                onClick = onContinue,
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
                        text = "Continue to AI Optimization",
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

@Composable
private fun MetricRow(label: String, score: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                color = TextWhite,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$score",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (score >= 90) BrightEmerald else BrightPurple
            )
        }

        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = if (score >= 90) EmeraldGreen else PrimaryPurple,
            trackColor = LuxuryCard
        )
    }
}
