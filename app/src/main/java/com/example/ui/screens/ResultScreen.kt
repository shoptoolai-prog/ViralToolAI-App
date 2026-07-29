package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PriceCompareItem
import com.example.ui.components.materialSharedBounds
import com.example.data.SimilarProduct
import com.example.data.ShoppingResult
import com.example.data.InstagramProduct
import com.example.data.CouponOffer
import com.example.data.ProductSpecification
import com.example.data.generateResultData
import com.example.engine.PriceTrackerEngine
import com.example.ui.components.ApplePriceGraphComponent
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import coil.compose.SubcomposeAsyncImage

// Beautiful custom Shimmer modifier
fun Modifier.shimmer(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer_effect")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslation"
    )
    
    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.05f),
        Color.White.copy(alpha = 0.18f),
        Color.White.copy(alpha = 0.05f)
    )
    
    background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim - 300f, translateAnim - 300f),
            end = Offset(translateAnim, translateAnim)
        )
    )
}

// Apple-style tactile press animation modifier
fun Modifier.applePressEffect(interactionSource: MutableInteractionSource? = null): Modifier = composed {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val isPressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "applePressScale"
    )
    this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ResultScreen(
    analyzedLink: String,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    var isProcessing by remember { mutableStateOf(true) }
    var pipelineStepIndex by remember { mutableStateOf(0) }
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var reloadTrigger by remember { mutableStateOf(0) }
    
    val resultDataState = produceState<ShoppingResult>(
        initialValue = generateResultData(analyzedLink),
        key1 = analyzedLink,
        key2 = reloadTrigger
    ) {
        val cached = com.example.cache.LocalShoppingCache.getCachedProductResult(analyzedLink)
        if (cached != null) {
            value = cached
        } else {
            val asyncResult = withContext(Dispatchers.IO) {
                com.example.data.integration.RealDataIntegrationLayer.getProductData(analyzedLink)
            }
            value = asyncResult
        }
    }
    val resultData = resultDataState.value

    val pipelineSteps = remember {
        listOf(
            "Reading Product URL",
            "Identifying Store",
            "Detecting Product",
            "Loading Product Image",
            "Verifying Price",
            "Comparing Stores",
            "Checking Discounts",
            "Generating Smart Report"
        )
    }
    
    // Single Seamless AI Processing Experience (2.2s live 9-step pipeline)
    LaunchedEffect(analyzedLink, reloadTrigger) {
        isProcessing = true
        pipelineStepIndex = 0
        for (i in 0 until pipelineSteps.size) {
            delay(240)
            pipelineStepIndex = i + 1
        }
        delay(150)
        isProcessing = false
    }

    // Automatically dismiss toast after 2s
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(2000)
            toastMessage = null
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        if (isProcessing) {
            AiProcessingScreen(
                analyzedLink = analyzedLink,
                currentStepIndex = pipelineStepIndex,
                pipelineSteps = pipelineSteps,
                detectedStore = resultData.detectedStore
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                // Premium Header Capsule
                val headerShimmerTransition = rememberInfiniteTransition(label = "headerShimmer")
                val headerShimmerPos by headerShimmerTransition.animateFloat(
                    initialValue = -300f,
                    targetValue = 800f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(3500, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "headerShimmerPos"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .materialSharedBounds("shopping_analyzer_main_card")
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(24.dp),
                            spotColor = EmeraldPrimary.copy(alpha = 0.35f),
                            ambientColor = Color.Black
                        )
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF131A16), Color(0xFF101C15), Color(0xFF131A16))
                            )
                        )
                        .border(
                            BorderStroke(
                                1.2.dp,
                                Brush.linearGradient(
                                    colors = listOf(
                                        EmeraldPrimary.copy(alpha = 0.8f),
                                        EmeraldGlow.copy(alpha = 0.4f),
                                        Color(0xFF00E5FF).copy(alpha = 0.6f)
                                    ),
                                    start = Offset(headerShimmerPos, 0f),
                                    end = Offset(headerShimmerPos + 300f, 100f)
                                )
                            ),
                            RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0x1AFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x33FFFFFF)), CircleShape)
                                .testTag("result_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldGlow.copy(alpha = 0.2f))
                                    .border(BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.6f)), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "AI Report",
                                    tint = EmeraldGlow,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "Smart Shopping Report",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite,
                                letterSpacing = 0.3.sp
                            )
                        }

                        val refreshInteractionSource = remember { MutableInteractionSource() }
                        val isRefreshPressed by refreshInteractionSource.collectIsPressedAsState()
                        val refreshScale by animateFloatAsState(
                            targetValue = if (isRefreshPressed) 0.90f else 1f,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium),
                            label = "refreshScale"
                        )

                        IconButton(
                            onClick = {
                                reloadTrigger++
                                toastMessage = "✔ Refreshing product extraction..."
                            },
                            interactionSource = refreshInteractionSource,
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = refreshScale
                                    scaleY = refreshScale
                                }
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary.copy(alpha = 0.2f))
                                .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)), CircleShape)
                                .shadow(elevation = 6.dp, shape = CircleShape, spotColor = EmeraldGlow)
                                .testTag("result_refresh_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                tint = EmeraldGlow,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                ReportContent(
                    resultData = resultData, 
                    onShowToast = { toastMessage = it },
                    onRefresh = {
                        reloadTrigger++
                        toastMessage = "✔ Refreshing product extraction..."
                    }
                )
                
                Spacer(modifier = Modifier.height(50.dp))
            }
        }

        // Custom Toast Notification Overlay
        AnimatedVisibility(
            visible = toastMessage != null,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 50 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { 50 }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            toastMessage?.let { msg ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xEE1E293B))
                        .border(BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.5f)), RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = msg,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }
        }
    }
}

// =========================================================
// UNIVERSAL SHOPPING ANALYZER: SINGLE AI PROCESSING SCREEN (V4)
// =========================================================
@Composable
fun AiProcessingScreen(
    analyzedLink: String,
    currentStepIndex: Int,
    pipelineSteps: List<String>,
    detectedStore: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "processingRing")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationAngle"
    )

    val counterRotationAngle by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "counterRotationAngle"
    )

    val auroraOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "auroraOffset"
    )

    val ringGlowPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ringGlowPulse"
    )

    val storeName = remember(analyzedLink, detectedStore) {
        if (detectedStore.isNotBlank() && detectedStore != "Unknown") detectedStore else detectMerchant(analyzedLink).name
    }

    val progressPercent = remember(currentStepIndex, pipelineSteps.size) {
        ((currentStepIndex.toFloat() / pipelineSteps.size) * 100).toInt().coerceIn(0, 100)
    }

    val animatedPercent by animateIntAsState(
        targetValue = progressPercent,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "animatedPercent"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF09120C),
                        Color(0xFF0B1B13),
                        Color(0xFF0D141E),
                        Color(0xFF0A0F0D)
                    ),
                    start = Offset(auroraOffset % 800f, 0f),
                    end = Offset((auroraOffset % 800f) + 600f, 1000f)
                )
            )
            .statusBarsPadding()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Top Engine Pill + Glass Percentage Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(EmeraldGlow.copy(alpha = 0.15f))
                        .border(BorderStroke(1.2.dp, EmeraldGlow.copy(alpha = 0.5f)), RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(EmeraldGlow, CircleShape)
                        )
                        Text(
                            text = "PREMIUM AI SCANNER ACTIVE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = EmeraldGlow,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Glass Percentage Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x2200E5FF))
                        .border(BorderStroke(1.2.dp, Color(0xFF00E5FF)), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "$animatedPercent%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF00E5FF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Neon Circular AI Scanner with Particles and Glass Container
            Box(
                modifier = Modifier.size(140.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer Rotating Sweep Ring
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { rotationZ = rotationAngle }
                ) {
                    val strokeWidth = 4.dp.toPx()
                    drawCircle(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                EmeraldGlow,
                                Color(0xFF00E5FF),
                                Color(0xFFA855F7),
                                EmeraldGlow
                            )
                        ),
                        radius = (size.width / 2f) - strokeWidth,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
                    )
                }

                // Inner Counter-Rotating Ring
                Canvas(
                    modifier = Modifier
                        .size(118.dp)
                        .graphicsLayer { rotationZ = counterRotationAngle }
                ) {
                    val strokeWidth = 2.5.dp.toPx()
                    drawCircle(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFF00E5FF),
                                EmeraldPrimary,
                                Color(0xFF00E5FF)
                            )
                        ),
                        radius = (size.width / 2f) - strokeWidth,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
                    )
                }

                // Central Store Logo in Glowing Glass Circle
                Box(
                    modifier = Modifier
                        .size(102.dp)
                        .shadow(
                            elevation = 20.dp * ringGlowPulse,
                            shape = CircleShape,
                            spotColor = EmeraldGlow,
                            ambientColor = Color.Black
                        )
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF131A16), Color(0xFF0D1812))
                            )
                        )
                        .border(BorderStroke(1.5.dp, EmeraldGlow.copy(alpha = 0.7f)), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        OfficialLogo(
                            name = storeName,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = storeName,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Animated Progress Line
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(Color(0x1AFFFFFF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedPercent / 100f)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(EmeraldPrimary, EmeraldGlow, Color(0xFF00E5FF))
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Live Pipeline Steps Card with Glass Chips & Glow
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pipelineSteps.forEachIndexed { index, stepName ->
                    val isDone = index < currentStepIndex
                    val isCurrent = index == currentStepIndex

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                brush = if (isCurrent) {
                                    Brush.horizontalGradient(
                                        listOf(EmeraldGlow.copy(alpha = 0.20f), Color(0x10FFFFFF))
                                    )
                                } else if (isDone) {
                                    androidx.compose.ui.graphics.SolidColor(Color(0x0EFFFFFF))
                                } else {
                                    androidx.compose.ui.graphics.SolidColor(Color(0x05FFFFFF))
                                },
                                shape = RoundedCornerShape(14.dp)
                            )
                            .border(
                                BorderStroke(
                                    if (isCurrent) 1.2.dp else 1.dp,
                                    if (isCurrent) EmeraldGlow else if (isDone) Color(0x222ECC71) else Color(0x10FFFFFF)
                                ),
                                RoundedCornerShape(14.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 9.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    isDone -> {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(Color(0x222ECC71)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Done",
                                                tint = Color(0xFF2ECC71),
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }
                                    isCurrent -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            color = EmeraldGlow,
                                            strokeWidth = 2.5.dp
                                        )
                                    }
                                    else -> {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .background(TextGray.copy(alpha = 0.3f), CircleShape)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = stepName,
                                fontSize = 12.5.sp,
                                fontWeight = if (isCurrent) FontWeight.Black else if (isDone) FontWeight.Bold else FontWeight.Normal,
                                color = when {
                                    isDone -> TextWhite
                                    isCurrent -> EmeraldGlow
                                    else -> TextGray.copy(alpha = 0.45f)
                                },
                                letterSpacing = 0.2.sp
                            )

                            if (isCurrent) {
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(EmeraldGlow.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "IN PROGRESS",
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Black,
                                        color = EmeraldGlow
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

// =========================================================
// UNIVERSAL SHOPPING ANALYZER: TOP DELIVERY & STOCK CARD (V4)
// =========================================================
@Composable
fun DeliveryInformationCard(resultData: ShoppingResult) {
    val storeName = resultData.detectedStore.ifBlank { "Store" }
    val deliveryText = when {
        resultData.deliveryInfoText.isNotBlank() && !resultData.deliveryInfoText.contains("unavailable", ignoreCase = true) -> resultData.deliveryInfoText
        resultData.priceComparison.firstOrNull()?.deliveryEstimate?.isNotBlank() == true -> resultData.priceComparison.first().deliveryEstimate
        else -> "Delivery details calculated directly from $storeName checkout page"
    }

    val stockText = when {
        resultData.availability.isNotBlank() -> resultData.availability
        resultData.inStock -> "In Stock — Ships directly"
        else -> "Stock status verified on $storeName"
    }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("delivery_information_card"),
        borderColor = EmeraldGlow.copy(alpha = 0.35f),
        backgroundColor = Color(0x0C00FF88)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(EmeraldGlow.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = "Delivery",
                            tint = EmeraldGlow,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "DELIVERY & AVAILABILITY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = EmeraldGlow,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Verified from $storeName",
                            fontSize = 10.sp,
                            color = TextGray
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF2ECC71).copy(alpha = 0.15f))
                        .border(BorderStroke(1.dp, Color(0xFF2ECC71).copy(alpha = 0.4f)), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "VERIFIED",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF2ECC71)
                    )
                }
            }

            HorizontalDivider(color = Color(0x12FFFFFF), thickness = 1.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Estimate",
                        tint = TextWhite,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = deliveryText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFF2ECC71), CircleShape)
                    )
                    Text(
                        text = stockText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2ECC71)
                    )
                }
            }
        }
    }
}

@Composable
fun ShimmerReportPlaceholder() {
    PremiumLoadingSequence()
}

@Composable
fun PremiumLoadingSequence() {
    val steps = listOf(
        "Preparing AI...",
        "Reading Input...",
        "Understanding Context...",
        "Generating Insights...",
        "Building Premium Report...",
        "Completed"
    )

    var currentStep by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        for (i in steps.indices) {
            kotlinx.coroutines.delay(180)
            currentStep = i
        }
    }

    val transition = rememberInfiniteTransition(label = "loadingGlow")
    val pulseGlow by transition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF141420), Color(0xFF090912))
                )
            )
            .border(
                BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(EmeraldPrimary, ElectricPurple))),
                RoundedCornerShape(24.dp)
            )
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Neural Pulse Scanner graphic
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .shadow((18 * pulseGlow).dp, CircleShape, spotColor = EmeraldPrimary)
                    .background(EmeraldPrimary.copy(alpha = 0.2f), CircleShape)
                    .border(BorderStroke(2.dp, EmeraldGlow), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI Scanning",
                    tint = EmeraldGlow,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "ANALYZING PRODUCT INTELLIGENCE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = CrimsonLight,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            val progressPercent = (((currentStep + 1) * 100) / steps.size)
            Text(
                text = "$progressPercent% Completed",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { (currentStep + 1).toFloat() / steps.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = CrimsonRed,
                trackColor = Color(0x22FFFFFF)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                steps.forEachIndexed { index, stepTitle ->
                    val isCompleted = index <= currentStep
                    val isCurrent = index == currentStep

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isCurrent) Color(0x18FF2E44) else Color(0x06FFFFFF))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Done",
                                tint = Color(0xFF2ECC71),
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                color = TextGray,
                                strokeWidth = 2.dp
                            )
                        }

                        Text(
                            text = stepTitle,
                            fontSize = 11.sp,
                            fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Medium,
                            color = if (isCompleted) TextWhite else TextGray
                        )
                    }
                }
            }
        }
    }
}

// ==================================================
// RESIDUAL REPORT CONTENT AND HERO LAYOUT
// ==================================================

// ==================================================
// AI RECOMMENDATION POPUP
// ==================================================
@Composable
fun AiRecommendationPopup(
    savingsAmount: Double,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xEE003816), Color(0xEE0A241B))
                )
            )
            .border(
                BorderStroke(1.2.dp, Color(0xFF2ECC71)),
                RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFF2ECC71), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = AmoledBlack,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = "✔ AI found the best deal",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                    Text(
                        text = "Save ₹${String.format("%,.0f", savingsAmount)} • Swipe cards for more sellers →",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00FFCC)
                    )
                }
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss",
                    tint = TextGray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

data class SellerBadge(val label: String, val color: Color, val textColor: Color = AmoledBlack)

fun getSellerBadges(item: PriceCompareItem, allItems: List<PriceCompareItem>, index: Int): List<SellerBadge> {
    val badges = mutableListOf<SellerBadge>()
    val minPrice = allItems.minOfOrNull { it.price } ?: item.price
    val maxDiscount = allItems.maxOfOrNull { it.discountPercent.toDouble() }?.toInt() ?: item.discountPercent
    
    if (item.isBest || item.price <= minPrice) {
        badges.add(SellerBadge("🏆 BEST DEAL", Color(0xFF2ECC71), AmoledBlack))
    }
    if (item.isOfficialStore || item.store.lowercase().contains("official")) {
        badges.add(SellerBadge("🔥 OFFICIAL STORE", Color(0xFFFF5722), Color.White))
    }
    if (item.isVerified || item.rating >= 4.6) {
        badges.add(SellerBadge("⭐ MOST TRUSTED", Color(0xFFFFD700), AmoledBlack))
    }
    if (index == 0 || item.deliverySpeed.contains("1") || item.deliverySpeed.contains("Next") || item.deliverySpeed.contains("Same") || item.deliveryEstimate.contains("Today") || item.deliveryEstimate.contains("Tomorrow")) {
        badges.add(SellerBadge("⚡ FAST DELIVERY", Color(0xFF00FFCC), AmoledBlack))
    }
    if (item.discountPercent >= maxDiscount && item.discountPercent > 0) {
        badges.add(SellerBadge("💸 BIGGEST DISCOUNT", Color(0xFFFF4081), Color.White))
    }
    if (item.isVerified && badges.size < 2) {
        badges.add(SellerBadge("🛡 GENUINE SELLER", Color(0xFF3897F0), Color.White))
    }
    
    if (badges.isEmpty()) {
        badges.add(SellerBadge("🛡 VERIFIED SELLER", Color(0xFF3897F0), Color.White))
    }
    return badges
}

// ==================================================
// SWIPEABLE SELLER CARDS DECK
// ==================================================
// LIVE COMPARISON WAITING CARD (PHASE 5B)
// ==================================================
@Composable
fun LiveComparisonWaitingCard() {
    // Phase 13C: Developer waiting card removed.
}

// ==================================================
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SellerSwipeCardsDeck(
    priceComparison: List<PriceCompareItem>,
    currentPrice: Double,
    fallbackProductName: String = "",
    fallbackProductImage: String = "",
    detectedStore: String = "Verified Store",
    onBuyNow: (PriceCompareItem) -> Unit,
    onOpenStore: (PriceCompareItem) -> Unit
) {
    // Phase 13C: Single verified merchant -> Show Verified Store and Best Available Price only
    if (priceComparison.size == 1) {
        val item = priceComparison[0]
        val storeName = item.store.ifBlank { detectedStore }
        val priceVal = if (item.price > 0) item.price else currentPrice

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0x1A2ECC71), Color(0x082ECC71), Color(0x06000000))
                    )
                )
                .border(
                    BorderStroke(1.2.dp, Color(0x302ECC71)),
                    RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OfficialLogo(
                        name = storeName,
                        modifier = Modifier
                            .size(40.dp)
                            .border(BorderStroke(1.dp, Color(0x33FFFFFF)), CircleShape)
                    )
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = storeName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified Store",
                                tint = Color(0xFF3897F0),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "VERIFIED STORE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF2ECC71),
                            letterSpacing = 0.8.sp
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "BEST AVAILABLE PRICE",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = TextGray,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "₹${String.format("%,.0f", priceVal)}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF2ECC71)
                    )
                }
            }
        }
        return
    }

    if (priceComparison.isEmpty()) {
        return
    }

    val minPrice = priceComparison.minOfOrNull { it.price } ?: 0.0
    val maxPrice = priceComparison.maxOfOrNull { it.price } ?: currentPrice
    val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = { priceComparison.size })

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CompareArrows,
                    contentDescription = "Compare Sellers",
                    tint = Color(0xFF2ECC71),
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "COMPARE LIVE SELLERS (${priceComparison.size} STORES)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    letterSpacing = 1.2.sp
                )
            }

            Text(
                text = "Swipe to compare →",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2ECC71)
            )
        }

        androidx.compose.foundation.pager.HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 4.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val item = priceComparison[page]
            val isBest = item.isBest || item.price == minPrice
            val badges = remember(item, priceComparison) { getSellerBadges(item, priceComparison, page) }

            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            val cardScale = 1f - (kotlin.math.abs(pageOffset) * 0.05f).coerceIn(0f, 0.15f)
            val cardAlpha = 1f - (kotlin.math.abs(pageOffset) * 0.25f).coerceIn(0f, 0.5f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = cardScale
                        scaleY = cardScale
                        alpha = cardAlpha
                    }
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        if (isBest) {
                            Brush.verticalGradient(
                                colors = listOf(Color(0x1A2ECC71), Color(0x062ECC71), Color(0x0A000000))
                            )
                        } else {
                            Brush.verticalGradient(
                                colors = listOf(Color(0x12FFFFFF), Color(0x04FFFFFF), Color(0x0A000000))
                            )
                        }
                    )
                    .border(
                        BorderStroke(
                            if (isBest) 1.5.dp else 1.dp,
                            if (isBest) {
                                Brush.linearGradient(listOf(Color(0xFF2ECC71), Color(0xFF00FFCC)))
                            } else {
                                Brush.linearGradient(listOf(Color(0x22FFFFFF), Color(0x12FFFFFF)))
                            }
                        ),
                        RoundedCornerShape(22.dp)
                    )
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        for (badge in badges) {
                            Box(
                                modifier = Modifier
                                    .background(badge.color, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = badge.label,
                                    color = badge.textColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OfficialLogo(
                            name = item.store,
                            modifier = Modifier
                                .size(44.dp)
                                .border(BorderStroke(1.2.dp, Color(0x33FFFFFF)), CircleShape)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = item.store,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (item.isVerified || item.isOfficialStore) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified Store",
                                        tint = Color(0xFF3897F0),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Text(
                                        text = "${item.rating}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }
                                Text("•", fontSize = 11.sp, color = TextGray)
                                Text(
                                    text = if (item.isOfficialStore) "Official Brand Partner" else "Verified Seller",
                                    fontSize = 11.sp,
                                    color = TextGray
                                )
                            }
                        }
                    }

                    // Exact Product Identity & Image Lock (Phase 4F)
                    val displayImg = item.productImage.ifEmpty { fallbackProductImage }
                    val displayTitle = item.productName.ifEmpty { fallbackProductName }
                    
                    if (displayTitle.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0x0AFFFFFF), RoundedCornerShape(12.dp))
                                .border(BorderStroke(1.dp, Color(0x15FFFFFF)), RoundedCornerShape(12.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (displayImg.isNotEmpty()) {
                                SubcomposeAsyncImage(
                                    model = displayImg,
                                    contentDescription = displayTitle,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White),
                                    contentScale = ContentScale.Fit,
                                    loading = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0x11FFFFFF)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(16.dp),
                                                color = CrimsonLight,
                                                strokeWidth = 2.dp
                                            )
                                        }
                                    },
                                    error = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0x15FFFFFF)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ShoppingBag,
                                                contentDescription = "Package",
                                                tint = TextGray,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0x15FFFFFF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingBag,
                                        contentDescription = "Package",
                                        tint = TextGray,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = displayTitle,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "AI Match",
                                        tint = Color(0xFF2ECC71),
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Text(
                                        text = "${item.matchPercent}% ${item.confidenceLabel}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF2ECC71)
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0x0EFFFFFF), thickness = 1.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "PRICE OFFER",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextGray,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "₹${String.format("%,.0f", item.price)}",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isBest) Color(0xFF2ECC71) else TextWhite,
                                    letterSpacing = (-0.5).sp
                                )
                                if (item.originalPrice > item.price) {
                                    Text(
                                        text = "₹${String.format("%,.0f", item.originalPrice)}",
                                        fontSize = 14.sp,
                                        color = TextGray,
                                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                    )
                                }
                            }
                        }

                        val savings = (item.originalPrice - item.price).coerceAtLeast(maxPrice - item.price)
                        if (savings > 0 || item.discountPercent > 0) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0x222ECC71), RoundedCornerShape(8.dp))
                                    .border(BorderStroke(1.dp, Color(0xFF2ECC71)), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (item.discountPercent > 0) "${item.discountPercent}% OFF" else "Save ₹${String.format("%,.0f", savings)}",
                                    color = Color(0xFF2ECC71),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color(0x0AFFFFFF), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ElectricBolt,
                                    contentDescription = "Delivery",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "${item.deliveryEstimate} (${item.deliverySpeed})",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .background(Color(0x0AFFFFFF), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = "Warranty",
                                    tint = Color(0xFF00FFCC),
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = item.stock,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onOpenStore(item) },
                            modifier = Modifier.weight(0.42f),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.2.dp, Color(0x33FFFFFF)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Storefront,
                                    contentDescription = "Open Store",
                                    modifier = Modifier.size(14.dp)
                                )
                                Text("Store", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = { onBuyNow(item) },
                            modifier = Modifier
                                .weight(0.58f)
                                .height(46.dp)
                                .shadow(8.dp, RoundedCornerShape(14.dp), ambientColor = Color(0xFF2ECC71), spotColor = Color(0xFF2ECC71)),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFF2ECC71), Color(0xFF00B0FF))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingCart,
                                        contentDescription = "Buy Now",
                                        tint = AmoledBlack,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "BUY NOW",
                                        color = AmoledBlack,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(priceComparison.size) { i ->
                val isSelected = pagerState.currentPage == i
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .height(6.dp)
                        .width(if (isSelected) 20.dp else 6.dp)
                        .background(
                            if (isSelected) Color(0xFF2ECC71) else Color(0x33FFFFFF),
                            CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun SellerCard(
    compareItem: PriceCompareItem,
    bestPrice: Double,
    currentPrice: Double,
    onBuyNow: () -> Unit,
    onOpenStore: () -> Unit
) {
    val isBest = compareItem.isBest
    
    // Animated breathing glow for Best Deal
    val infiniteTransition = rememberInfiniteTransition(label = "BestDealGlow")
    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowIntensity"
    )
    
    val borderBrush = if (isBest) {
        Brush.sweepGradient(
            colors = listOf(
                Color(0xFF2ECC71).copy(alpha = glowIntensity),
                Color(0xFF00FFCC).copy(alpha = glowIntensity),
                Color(0xFF2ECC71).copy(alpha = glowIntensity)
            )
        )
    } else {
        Brush.linearGradient(colors = listOf(Color(0x12FFFFFF), Color(0x12FFFFFF)))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isBest) {
                    Brush.verticalGradient(
                        colors = listOf(Color(0x142ECC71), Color(0x042ECC71))
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(Color(0x0DFFFFFF), Color(0x04FFFFFF))
                    )
                }
            )
            .border(
                BorderStroke(if (isBest) 1.5.dp else 1.dp, borderBrush),
                RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Badges row for Best Deal
            if (isBest) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Best Deal Badge
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF2ECC71), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "⭐ Best Deal",
                            color = AmoledBlack,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    
                    // Most Recommended Badge
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF00FFCC).copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                            .border(BorderStroke(1.dp, Color(0xFF00FFCC)), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Most Recommended",
                            color = Color(0xFF00FFCC),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // Upper Row: Store Info and Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Store logo
                OfficialLogo(
                    name = compareItem.store,
                    modifier = Modifier
                        .size(40.dp)
                        .border(BorderStroke(1.dp, Color(0x22FFFFFF)), CircleShape)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = compareItem.store,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                        if (compareItem.isVerified) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = Color(0xFF3897F0),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Store Rating
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(10.dp)
                            )
                            Text(
                                text = compareItem.rating.toString(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextGray
                            )
                        }
                        Text("•", fontSize = 10.sp, color = TextGray)
                        // Return Policy
                        Text(
                            text = compareItem.returnPolicy,
                            fontSize = 10.sp,
                            color = TextGray
                        )
                        if (compareItem.isOfficialStore) {
                            Text("•", fontSize = 10.sp, color = TextGray)
                            // Official Store Tag
                            Text(
                                text = "Official Store",
                                fontSize = 10.sp,
                                color = Color(0xFF3897F0),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${String.format("%,.0f", compareItem.price)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isBest) Color(0xFF2ECC71) else TextWhite
                    )
                    
                    if (!isBest) {
                        // Show Visually the Difference (Price Savings comparison)
                        val priceDiff = compareItem.price - bestPrice
                        if (priceDiff > 0) {
                            val diffPercent = ((priceDiff / compareItem.price) * 100).toInt()
                            Text(
                                text = "Save ₹${String.format("%,.0f", priceDiff)} ($diffPercent%)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2ECC71)
                            )
                        }
                    } else {
                        // Best price saving compared to highest
                        val highestPrice = currentPrice.coerceAtLeast(compareItem.price)
                        val maxDiff = highestPrice - compareItem.price
                        if (maxDiff > 0) {
                            val maxDiffPercent = ((maxDiff / highestPrice) * 100).toInt()
                            Text(
                                text = "Save ₹${String.format("%,.0f", maxDiff)} ($maxDiffPercent%) max!",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2ECC71)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = Color(0x0AFFFFFF), thickness = 1.dp)

            // Middle Row: Delivery speed, Stock, Estimate
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stock
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                if (compareItem.stock.contains("Stock") || compareItem.stock.contains("In")) Color(0xFF2ECC71) else Color(0xFFFF9900),
                                CircleShape
                            )
                    )
                    Text(
                        text = compareItem.stock,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextGray
                    )
                }

                // Delivery Estimate
                Text(
                    text = "${compareItem.deliveryEstimate} (${compareItem.deliverySpeed})",
                    fontSize = 11.sp,
                    color = TextGray
                )
            }

            // Lower Row: Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Open Store Secondary Action
                OutlinedButton(
                    onClick = onOpenStore,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextWhite
                    ),
                    border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = "Open Store",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Open Store",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Buy Now Primary Action
                Button(
                    onClick = onBuyNow,
                    modifier = Modifier.weight(if (isBest) 1.25f else 1f), // larger button if best deal
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBest) Color(0xFF2ECC71) else Color(0x1FFFFFFF)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = if (isBest) 10.dp else 8.dp) // larger padding if best deal
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Buy Now",
                        tint = if (isBest) AmoledBlack else TextWhite,
                        modifier = Modifier.size(if (isBest) 16.dp else 14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Buy Now",
                        color = if (isBest) AmoledBlack else TextWhite,
                        fontSize = if (isBest) 13.sp else 11.sp, // larger font if best deal
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
fun InstagramReportContent(resultData: ShoppingResult, onShowToast: (String) -> Unit) {
    // 2. PRODUCT CONFIDENCE FILTER (below 80% do not display)
    val products = (resultData.instagramProducts ?: emptyList()).filter { it.aiConfidence >= 80 }
    
    if (products.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0x22F56040), Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Empty Catalog",
                    tint = Color(0xFFF56040).copy(alpha = 0.5f),
                    modifier = Modifier.size(54.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "No shopping products detected in this Reel.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Our AI scanned this Reel but didn't find any high-confidence wearable products. Try another link!",
                fontSize = 12.sp,
                color = TextGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
                lineHeight = 18.sp
            )
        }
        return
    }

    var selectedProduct by remember { mutableStateOf<InstagramProduct?>(products.firstOrNull()) }
    var wishlistedIds by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. SMART INSTAGRAM SHOPPING SUMMARY CARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0x1F833AB4), Color(0x0A0F0F0F))
                    )
                )
                .border(
                    BorderStroke(1.dp, Brush.linearGradient(colors = listOf(Color(0xFFE1306C).copy(alpha = 0.3f), Color.Transparent))),
                    RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Scan",
                        tint = Color(0xFF00FFCC),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "INSTAGRAM AI SHOPPING REPORT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF00FFCC),
                        letterSpacing = 1.2.sp
                    )
                }

                Text(
                    text = resultData.aiRecommendation,
                    fontSize = 13.sp,
                    color = TextWhite,
                    lineHeight = 18.sp
                )

                HorizontalDivider(color = Color(0x12FFFFFF), thickness = 1.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("DETECTED ITEMS", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("${products.size} Products", fontSize = 14.sp, fontWeight = FontWeight.Black, color = TextWhite)
                    }

                    val avgConfidence = (products.map { it.aiConfidence }.average()).toInt()
                    Column {
                        Text("AVG MATCH CONFIDENCE", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("$avgConfidence%", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF2ECC71))
                    }

                    val totalSavings = products.sumOf { p ->
                        val prices = p.priceComparison.map { it.price }
                        if (prices.isNotEmpty()) {
                            (prices.maxOrNull() ?: p.estimatedPrice) - (prices.minOrNull() ?: p.estimatedPrice)
                        } else {
                            0.0
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("TOTAL EST SAVINGS", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("₹${String.format("%,.0f", totalSavings)}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF00FFCC))
                    }
                }
            }
        }

        // 2. HORIZONTAL MULTI-PRODUCT DETECTION LIST
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "SCANNED PRODUCTS (${products.size})",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray,
                letterSpacing = 1.2.sp
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    for (product in products) {
                        val isSelected = selectedProduct?.id == product.id
                        val isWishlisted = wishlistedIds.contains(product.id)
                        
                        val bestDealItem = product.priceComparison.find { it.isBest } ?: product.priceComparison.minByOrNull { it.price }
                        val lowestPrice = bestDealItem?.price ?: product.estimatedPrice
                        val lowestStore = bestDealItem?.store ?: "Sellers"

                        val borderBrush = if (isSelected) {
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFFFDC80))
                            )
                        } else {
                            Brush.linearGradient(colors = listOf(Color(0x1FFFFFFF), Color(0x1FFFFFFF)))
                        }

                        Box(
                            modifier = Modifier
                                .width(260.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(
                                    if (isSelected) Color(0x1F1F1F1F) else Color(0x0F141414)
                                )
                                .border(
                                    BorderStroke(if (isSelected) 1.8.dp else 1.dp, borderBrush),
                                    RoundedCornerShape(18.dp)
                                )
                                .clickable { selectedProduct = product }
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x12FFFFFF)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        val iconVector = when (product.imageUrl) {
                                            "shoes" -> Icons.Default.IceSkating
                                            "clothes" -> Icons.Default.Checkroom
                                            "watch" -> Icons.Default.Watch
                                            "perfume" -> Icons.Default.LocalMall
                                            "bag" -> Icons.Default.LocalMall
                                            else -> Icons.Default.LocalMall
                                        }

                                        if (!product.productImageWebUrl.isNullOrEmpty()) {
                                            SubcomposeAsyncImage(
                                                model = product.productImageWebUrl,
                                                loading = {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .background(Color(0x0DFFFFFF))
                                                    )
                                                },
                                                error = {
                                                    Icon(
                                                        imageVector = iconVector,
                                                        contentDescription = "Vector",
                                                        tint = Color(0xFFE1306C),
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                },
                                                contentDescription = product.name,
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                            )
                                        } else {
                                            Icon(
                                                imageVector = iconVector,
                                                contentDescription = "Vector",
                                                tint = Color(0xFFE1306C),
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0x12FFFFFF), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = product.category.uppercase(),
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFFDC80)
                                            )
                                        }

                                        val isPossibleMatch = product.aiConfidence in 80..94
                                        val confText = if (isPossibleMatch) "${product.aiConfidence}% Possible Match" else "${product.aiConfidence}% AI MATCH"
                                        val confColor = if (isPossibleMatch) Color(0xFFFFCC00) else Color(0xFF2ECC71)
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(5.dp)
                                                    .background(confColor, CircleShape)
                                            )
                                            Text(
                                                text = confText.uppercase(),
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Black,
                                                color = confColor
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = {
                                            wishlistedIds = if (isWishlisted) {
                                                wishlistedIds - product.id
                                            } else {
                                                wishlistedIds + product.id
                                            }
                                            onShowToast(if (isWishlisted) "✔ Removed from Wishlist" else "✔ Saved to your Wishlist!")
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "Wishlist",
                                            tint = if (isWishlisted) Color(0xFFFD1D1D) else TextGray,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = product.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Bottom,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("EST. PRICE", fontSize = 7.sp, color = TextGray, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = "₹${String.format("%,.0f", product.estimatedPrice)}",
                                            fontSize = 11.sp,
                                            color = TextGray,
                                            style = androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("BEST DEAL", fontSize = 7.sp, color = Color(0xFF2ECC71), fontWeight = FontWeight.Bold)
                                        Text(
                                            text = "₹${String.format("%,.0f", lowestPrice)}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFF2ECC71)
                                        )
                                        Text(
                                            text = "on $lowestStore",
                                            fontSize = 8.sp,
                                            color = TextGray
                                        )
                                    }
                                }

                                val context = LocalContext.current
                                Button(
                                    onClick = {
                                        val buyUrl = bestDealItem?.url ?: "https://google.com"
                                        try {
                                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(buyUrl))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            onShowToast("✖ Could not open product link")
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF2ECC71)
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingCart,
                                        contentDescription = "Buy",
                                        tint = AmoledBlack,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Buy Now",
                                        color = AmoledBlack,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. INTERACTIVE SELECTED PRODUCT DETAILS
        selectedProduct?.let { product ->
            HorizontalDivider(color = Color(0x12FFFFFF), thickness = 1.dp)

            Text(
                text = "DETAILS FOR: ${product.name.uppercase()}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00FFCC),
                letterSpacing = 1.2.sp
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0x1F833AB4), Color(0x06FFFFFF))
                        )
                    )
                    .border(BorderStroke(1.2.dp, Color(0x33833AB4)), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "AI Co-Pilot",
                        tint = Color(0xFFE1306C),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "AI SELECTION INSIGHT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFE1306C),
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        val bestDeal = product.priceComparison.find { it.isBest } ?: product.priceComparison.minByOrNull { it.price }
                        val savings = if (bestDeal != null) product.estimatedPrice - bestDeal.price else 0.0
                        val insightText = "AI is highly confident (${product.aiConfidence}%) this is a match. The lowest price is ₹${String.format("%,.0f", bestDeal?.price ?: product.estimatedPrice)} available at ${bestDeal?.store ?: "retailers"}, saving you ₹${String.format("%,.0f", savings)}."
                        
                        Text(
                            text = insightText,
                            fontSize = 13.sp,
                            color = TextWhite,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            val context = LocalContext.current
            SellerSwipeCardsDeck(
                priceComparison = product.priceComparison,
                currentPrice = product.estimatedPrice,
                onBuyNow = { compareItem ->
                    try {
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(compareItem.url))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        onShowToast("✖ Could not open product link")
                    }
                },
                onOpenStore = { compareItem ->
                    val storeUrl = when (compareItem.store.lowercase()) {
                        "amazon" -> "https://amazon.in"
                        "flipkart" -> "https://flipkart.com"
                        "ajio" -> "https://ajio.com"
                        "myntra" -> "https://myntra.com"
                        else -> "https://google.com/search?q=${compareItem.store}"
                    }
                    try {
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(storeUrl))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        onShowToast("✖ Could not open store link")
                    }
                }
            )

            if (product.similarProducts.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "ALTERNATIVE RECOMMENDATIONS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextGray,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        for (similar in product.similarProducts) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0x06FFFFFF))
                                    .border(BorderStroke(1.dp, Color(0x12FFFFFF)), RoundedCornerShape(16.dp))
                                    .padding(14.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OfficialLogo(
                                        name = similar.store,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), CircleShape)
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = similar.name,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "Available at ${similar.store}",
                                            fontSize = 11.sp,
                                            color = TextGray
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "₹${String.format("%,.0f", similar.price)}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Black,
                                            color = TextWhite
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Rating",
                                                tint = Color(0xFFFFD700),
                                                modifier = Modifier.size(10.dp)
                                            )
                                            Text(
                                                text = similar.rating.toString(),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextWhite
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
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF141414))
                .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudQueue,
                    contentDescription = "Offline Preview",
                    tint = TextGray,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = "Offline Preview Mode",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "This is a preview build. Live Instagram catalog tags will automatically load after backend integration.",
                        fontSize = 11.sp,
                        color = TextGray,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumWarningCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFF9D80B).copy(alpha = 0.1f))
            .border(BorderStroke(1.2.dp, Color(0xFFF9D80B).copy(alpha = 0.4f)), RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFF9D80B).copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Unsure Alert",
                    tint = Color(0xFFF9D80B),
                    modifier = Modifier.size(18.dp)
                )
            }
            
            Column {
                Text(
                    text = "CLOUD VERIFICATION REQUIRED",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFF9D80B),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Cloud verification required for accurate shopping results.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun ReportContent(resultData: ShoppingResult, onShowToast: (String) -> Unit, onRefresh: () -> Unit) {
    if (!resultData.isReliable || resultData.status == "Product details unavailable" || resultData.productName == "Product details unavailable") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFFE74C3C).copy(alpha = 0.25f), Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SearchOff,
                    contentDescription = "Unverified Product",
                    tint = Color(0xFFE74C3C),
                    modifier = Modifier.size(44.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            val isAmazon = resultData.detectedStore.equals("Amazon", ignoreCase = true) ||
                    resultData.url.lowercase().contains("amazon") ||
                    resultData.url.lowercase().contains("amzn")

            val isUnsupported = resultData.productName.contains("Unsupported", ignoreCase = true) ||
                    resultData.status.contains("Unsupported", ignoreCase = true) ||
                    resultData.aiRecommendation.contains("Unsupported", ignoreCase = true)

            val titleText = "❌ Product could not be verified."
            val subtitleText = "Product details could not be verified by platform parsers or Google Search."
            
            Text(
                text = titleText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Text(
                text = subtitleText,
                fontSize = 12.sp,
                color = TextGray,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Possible reasons card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x0AFFFFFF))
                    .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Possible reasons:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        letterSpacing = 0.5.sp
                    )
                    
                    val reasons = listOf(
                        "• Unsupported website",
                        "• Broken link",
                        "• Product removed",
                        "• Temporary network issue"
                    )
                    
                    reasons.forEach { reason ->
                        Text(
                            text = reason,
                            fontSize = 12.sp,
                            color = TextWhite.copy(alpha = 0.85f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onRefresh,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldPrimary),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Retry",
                            modifier = Modifier.size(16.dp)
                        )
                        Text("Retry", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                Button(
                    onClick = onRefresh,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = "Paste Another Link",
                            tint = AmoledBlack,
                            modifier = Modifier.size(16.dp)
                        )
                        Text("Paste Another Link", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                    }
                }
            }
        }
        return
    }

    if (resultData.instagramProducts != null) {
        InstagramReportContent(resultData = resultData, onShowToast = onShowToast)
    } else {
        val accentColor = Color(resultData.accentColor)
        
        val savedAmount = (resultData.originalPrice ?: resultData.currentPrice) - resultData.bestPrice
        val discountPercent = resultData.discountPercent ?: if (resultData.currentPrice > 0) (((resultData.originalPrice ?: resultData.currentPrice) - resultData.currentPrice) / (resultData.originalPrice ?: resultData.currentPrice) * 100).toInt() else 0

        val animatedSavings by androidx.compose.animation.core.animateIntAsState(
            targetValue = savedAmount.toInt(),
            animationSpec = androidx.compose.animation.core.tween(1500, easing = androidx.compose.animation.core.EaseOutCubic),
            label = "SavingsAnimation"
        )

        val animatedPercent by androidx.compose.animation.core.animateIntAsState(
            targetValue = discountPercent,
            animationSpec = androidx.compose.animation.core.tween(1500, easing = androidx.compose.animation.core.EaseOutCubic),
            label = "PercentAnimation"
        )
    
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
        // PREMIUM UNIFIED TOP HERO CARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0x12FFFFFF), Color(0x06FFFFFF))
                    )
                )
                .border(BorderStroke(1.2.dp, Color(0x1FFFFFFF)), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Top section: Image left, brand & name right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Custom Glowing Product Image Box with Horizontal Swipe Product Gallery (Phase 3G)
                    Box(
                        modifier = Modifier
                            .size(105.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(accentColor.copy(alpha = 0.25f), Color(0x0AFFFFFF))
                                )
                            )
                            .border(BorderStroke(1.2.dp, accentColor.copy(alpha = 0.3f)), RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        val iconVector = when (resultData.imageUrl) {
                            "headphones" -> Icons.Default.Headphones
                            "phone" -> Icons.Default.Smartphone
                            "shoes" -> Icons.Default.IceSkating
                            "clothes" -> Icons.Default.Checkroom
                            else -> Icons.Default.LocalMall
                        }

                        val imagesList = resultData.productImages
                        if (imagesList.isNotEmpty()) {
                            val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = { imagesList.size })
                            
                            Box(modifier = Modifier.fillMaxSize()) {
                                androidx.compose.foundation.pager.HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier.fillMaxSize()
                                ) { page ->
                                    SubcomposeAsyncImage(
                                        model = imagesList[page],
                                        loading = {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .shimmer()
                                            )
                                        },
                                        error = {
                                            Icon(
                                                imageVector = iconVector,
                                                contentDescription = "Product Vector",
                                                tint = accentColor,
                                                modifier = Modifier.size(40.dp)
                                            )
                                        },
                                        contentDescription = resultData.productName,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                    )
                                }
                                
                                if (imagesList.size > 1) {
                                    Row(
                                        Modifier
                                            .height(16.dp)
                                            .fillMaxWidth()
                                            .align(Alignment.BottomCenter)
                                            .background(Color.Black.copy(alpha = 0.4f)),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        repeat(imagesList.size) { iteration ->
                                            val color = if (pagerState.currentPage == iteration) accentColor else Color.White.copy(alpha = 0.5f)
                                            Box(
                                                modifier = Modifier
                                                    .padding(2.dp)
                                                    .clip(CircleShape)
                                                    .background(color)
                                                    .size(5.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        } else if (!resultData.productImageWebUrl.isNullOrEmpty()) {
                            SubcomposeAsyncImage(
                                model = resultData.productImageWebUrl,
                                loading = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .shimmer()
                                    )
                                },
                                error = {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.padding(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Inventory2,
                                            contentDescription = "No Preview",
                                            tint = accentColor,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "No Product Preview",
                                            fontSize = 7.sp,
                                            color = TextGray,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                },
                                contentDescription = resultData.productName,
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(18.dp)),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = "No Preview",
                                    tint = accentColor,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "No Product Preview",
                                    fontSize = 7.sp,
                                    color = TextGray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        
                        // Overlaid Brand Logo Badge at bottom-right corner
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(4.dp)
                                .size(24.dp)
                                .background(Color(0xFF0F0F0F), CircleShape)
                                .border(BorderStroke(0.8.dp, Color(0x33FFFFFF)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            OfficialLogo(
                                name = resultData.detectedStore,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = resultData.brand.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = accentColor,
                                letterSpacing = 1.5.sp
                            )
                            
                            // Glowing AI Approved Badge / Confidence Threshold Badge
                            if (resultData.detectionConfidence < 80) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFFF9800).copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                        .border(BorderStroke(1.dp, Color(0xFFFF9800).copy(alpha = 0.4f)), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Preview may not be fully accurate",
                                        fontSize = 7.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFFF9800)
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFF2ECC71).copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                        .border(BorderStroke(1.dp, Color(0xFF2ECC71).copy(alpha = 0.4f)), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "AI VERIFIED PRODUCT",
                                        fontSize = 7.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF2ECC71)
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        val cleanTitle = remember(resultData.productName, resultData.detectedStore) {
                            val title = resultData.productName.trim()
                            if (title.isNotEmpty() && !title.startsWith("http://", ignoreCase = true) && !title.startsWith("https://", ignoreCase = true) && !title.contains("://") && !title.contains("www.") && title != "Unknown Product" && title != "HTTPS") {
                                title
                            } else if (resultData.detectedStore.isNotBlank() && resultData.detectedStore != "Unknown" && !resultData.detectedStore.startsWith("http", ignoreCase = true)) {
                                "${resultData.detectedStore} Item"
                            } else {
                                "Product details unavailable"
                            }
                        }
                        
                        Text(
                            text = cleanTitle,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 20.sp
                        )
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = resultData.rating.toString(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                            Text(
                                text = "•",
                                fontSize = 11.sp,
                                color = TextGray
                            )
                            Text(
                                text = "${resultData.reviewsCount} reviews",
                                fontSize = 11.sp,
                                color = TextGray
                            )
                        }
                    }
                }
                
                HorizontalDivider(color = Color(0x12FFFFFF), thickness = 1.dp)
                
                // 5. PRICE PANEL: Current Price, MRP, Discount, Savings, Availability (Phase 13C Strict Price Logic)
                val showMrpCard = resultData.originalPrice != null &&
                        resultData.originalPrice!! > resultData.currentPrice &&
                        (resultData.discountPercent ?: 0) > 0
                val validSavingsVal = if (showMrpCard) (resultData.originalPrice!! - resultData.currentPrice) else 0.0

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Card 1: Current Price & Availability
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x06FFFFFF))
                            .border(BorderStroke(1.dp, Color(0x10FFFFFF)), RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "CURRENT PRICE",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = TextGray,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (resultData.currentPrice > 0.0) "₹${String.format("%,.0f", resultData.currentPrice)}" else "Price unavailable",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Box(modifier = Modifier.size(5.dp).background(Color(0xFF4CAF50), CircleShape))
                                Text(
                                    text = resultData.availability,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50)
                                )
                            }
                        }
                    }

                    // Card 2: ORIGINAL MRP & Discount (Shown ONLY if Verified and Greater than Current Price)
                    if (showMrpCard) {
                        val validMrp = resultData.originalPrice!!
                        val validDiscount = resultData.discountPercent!!
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x06FFFFFF))
                                .border(BorderStroke(1.dp, Color(0x10FFFFFF)), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "ORIGINAL MRP",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextGray,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "₹${String.format("%,.0f", validMrp)}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextGray,
                                    style = androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "$validDiscount% OFF",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = accentColor
                                    )
                                }
                            }
                        }
                    }
                }

                // Card 3: Animated Savings Banner (Shown only when verified MRP > Current Price and Savings > 0)
                if (validSavingsVal > 0) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x102ECC71))
                            .border(BorderStroke(1.dp, Color(0x302ECC71)), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0x202ECC71), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TrendingDown,
                                    contentDescription = "Savings Trend",
                                    tint = Color(0xFF2ECC71),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "YOU SAVE",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF2ECC71),
                                        letterSpacing = 1.sp
                                    )
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFF2ECC71), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "BEST DEAL",
                                            fontSize = 7.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.Black
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "You Save ₹$animatedSavings ($animatedPercent%)",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF2ECC71)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // SECTION: TOP DELIVERY & STOCK INFORMATION CARD (V4)
                DeliveryInformationCard(resultData = resultData)

                Spacer(modifier = Modifier.height(16.dp))

                // SECTION: PRICE TIMELINE CHART (MOVED NEAR TOP - V4)
                if (resultData.hasHistoricalPriceData && resultData.priceHistoryTimeline.isNotEmpty()) {
                    PriceTimelineCard(resultData = resultData)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // SECTION: SELLER COMPARISON CAROUSEL (ALWAYS EXPANDED - MASTER PHASE 4F & STABILIZATION V1)
                val context = LocalContext.current
                SellerSwipeCardsDeck(
                    priceComparison = resultData.priceComparison,
                    currentPrice = resultData.currentPrice,
                    fallbackProductName = resultData.productName,
                    fallbackProductImage = resultData.imageUrl.ifEmpty { resultData.productImages.firstOrNull() ?: "" },
                    onBuyNow = { compareItem ->
                        try {
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(compareItem.url))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            onShowToast("✖ Could not open product link")
                        }
                    },
                    onOpenStore = { compareItem ->
                        try {
                            val storeUrl = when (compareItem.store.lowercase()) {
                                "amazon" -> "https://amazon.in"
                                "flipkart" -> "https://flipkart.com"
                                "ajio" -> "https://ajio.com"
                                "myntra" -> "https://myntra.com"
                                "meesho" -> "https://meesho.com"
                                "croma" -> "https://croma.com"
                                "vijay sales" -> "https://vijaysales.com"
                                "reliance digital" -> "https://reliancedigital.in"
                                "tata cliq" -> "https://tatacliq.com"
                                "snapdeal" -> "https://snapdeal.com"
                                else -> {
                                    try {
                                        val uri = android.net.Uri.parse(compareItem.url)
                                        val scheme = uri.scheme ?: "https"
                                        val host = uri.host
                                        if (host != null) "$scheme://$host" else "https://google.com/search?q=${compareItem.store}+shopping"
                                    } catch (e: Exception) {
                                        "https://google.com/search?q=${compareItem.store}+shopping"
                                    }
                                }
                            }
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(storeUrl))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            onShowToast("✖ Could not open store link")
                        }
                    }
                )

                HorizontalDivider(color = Color(0x0AFFFFFF), thickness = 1.dp)

                // Share and Copy Best Deal Action Buttons
                val bestSellerItem = resultData.priceComparison.find { it.isBest } ?: resultData.priceComparison.minByOrNull { it.price }
                val bestStoreName = bestSellerItem?.store ?: resultData.detectedStore
                val bestProductUrl = bestSellerItem?.url ?: resultData.url

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Share Deal Button
                    Button(
                        onClick = {
                            val shareText = "🔥 Check out this amazing deal found by ViralToolAI!\n\n" +
                                    "📦 Product: ${resultData.productName}\n" +
                                    "💰 Lowest Price: ₹${String.format("%,.0f", resultData.bestPrice)}\n" +
                                    "🏪 Best Store: $bestStoreName\n" +
                                    "🔗 Direct Link: $bestProductUrl\n\n" +
                                    "Shared via ViralToolAI — From Products to Popularity 🚀"
                            
                            val sendIntent = android.content.Intent().apply {
                                action = android.content.Intent.ACTION_SEND
                                putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            val shareIntent = android.content.Intent.createChooser(sendIntent, "Share Deal via")
                            context.startActivity(shareIntent)
                            onShowToast("✔ Deal ready to share!")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .applePressEffect(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0x12FFFFFF),
                            contentColor = TextWhite
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share Deal", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // Copy Best Deal Link Button
                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            val clip = android.content.ClipData.newPlainText("Best Deal Link", bestProductUrl)
                            clipboard.setPrimaryClip(clip)
                            onShowToast("✔ Best Deal Link Copied!")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .applePressEffect(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0x12FFFFFF),
                            contentColor = TextWhite
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Link",
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copy Link", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // 7. PREMIUM DYNAMIC MERCHANT CTA BUTTON (Phase 4D)
                val btnColor = when (resultData.detectedStore.lowercase()) {
                    "flipkart" -> Color(0xFF2874F0)
                    "amazon" -> Color(0xFFFF9900)
                    "myntra", "nykaa" -> Color(0xFFFC2779)
                    "ajio" -> Color(0xFF1E293B)
                    "meesho" -> Color(0xFF9C27B0)
                    "croma" -> Color(0xFF00A3A6)
                    "apple", "nike" -> Color(0xFF111111)
                    else -> CrimsonRed
                }
                
                val btnText = "Open on ${resultData.detectedStore}"

                val ctaPulseTransition = rememberInfiniteTransition(label = "ctaPulse")
                val ctaGlowPulse by ctaPulseTransition.animateFloat(
                    initialValue = 0.85f,
                    targetValue = 1.15f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1400, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "ctaGlowPulse"
                )

                Button(
                    onClick = {
                        try {
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(resultData.url))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            onShowToast("✖ Could not open product link")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .applePressEffect()
                        .shadow(
                            elevation = 16.dp * ctaGlowPulse,
                            shape = RoundedCornerShape(28.dp),
                            clip = false,
                            ambientColor = btnColor,
                            spotColor = btnColor
                        )
                        .testTag("view_original_product_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = btnColor,
                        contentColor = TextWhite
                    ),
                    shape = RoundedCornerShape(28.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OfficialLogo(
                            name = resultData.detectedStore,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = btnText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Open",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
        
        // SECTION: PHASE 9C QUICK ACTIONS BAR
        Phase9CQuickActionsBar(
            resultData = resultData,
            onShowToast = onShowToast
        )

        // SECTION: PHASE 9C PRODUCT QUALITY ESTIMATE METER
        ProductQualityMeterSection(resultData = resultData)

        // SECTION: PHASE 9C PRODUCT KNOWLEDGE ENGINE (CENTRALIZED INTELLIGENCE)
        ProductIntelligenceSection(resultData = resultData)

        // SECTION: PHASE 9C SMART BUYING GUIDE (LABELED AI RECOMMENDATION)
        SmartBuyingGuideSection(resultData = resultData)

        // SECTION: PHASE 9D AI REVIEW INTELLIGENCE ENGINE & ADVISOR
        Phase9DReviewIntelligenceSection(resultData = resultData, onShowToast = onShowToast)

        // SECTION: SMART ACTION BUTTONS (Phase 7B / 7C)
        var showShareSheet by remember { mutableStateOf(false) }

        SmartActionButtonsRow(
            resultData = resultData,
            onShowToast = onShowToast,
            onOpenShareSheet = { showShareSheet = true }
        )

        // SECTION: SMART BUY RECOMMENDATION (Phase 7C / 7D)
        SmartBuyRecommendationCard(resultData = resultData)

        // SECTION: SMART DEALS & DISCOUNTS CARD (Phase 7D)
        SmartDealsCardSection(resultData = resultData)

        // SECTION: COUPON ENGINE (Phase 7D)
        CouponEngineSection(resultData = resultData, onShowToast = onShowToast)

        // SECTION: PRICE DROP WATCH (Phase 7D)
        PriceDropWatchCard(resultData = resultData)

        // SECTION: PRICE TIMELINE (Phase 7C)
        PriceTimelineCard(resultData = resultData)

        // SECTION: MERCHANT TRUST SCORE (Phase 7D)
        MerchantTrustScoreCard(resultData = resultData)

        // SECTION: SAVE MONEY TIPS (Phase 7D)
        SaveMoneyTipsSection(resultData = resultData)

        // SECTION: AI DEAL SCORE & PRICE CONFIDENCE INDICATOR (Phase 7B)
        DealScoreAndConfidenceSection(
            dealScore = resultData.dealScore,
            dealScoreLabel = resultData.dealScoreLabel,
            confidencePercent = resultData.detectionConfidence,
            confidenceLabel = resultData.priceConfidenceLabel
        )

        // SECTION: SMART PRODUCT COMPARISON (Phase 7C)
        SmartProductComparisonSection(resultData = resultData)

        // SECTION: AI BUYING INSIGHTS (Phase 7B)
        AiBuyingInsightsSection(resultData = resultData)

        // SECTION: PROS & CONS CARD DECK (Phase 7B)
        ProsConsDeckSection(
            pros = resultData.pros,
            cons = resultData.cons
        )

        // SECTION: BETTER ALTERNATIVES ARCHITECTURE (Phase 7B)
        BetterAlternativesSection(
            alternatives = resultData.verifiedAlternatives
        )

        // SECTION: AI TRUST SCORE
        TrustScoreSection(
            trustScorePercent = resultData.trustScorePercent,
            trustScoreLevel = resultData.trustScoreLevel
        )

        // SECTION: SMART COUPONS & BANK OFFERS
        if (resultData.coupons.isNotEmpty()) {
            SmartCouponsSection(
                coupons = resultData.coupons,
                onShowToast = onShowToast
            )
        }

        // SECTION: PREMIUM EXPANDABLE PRODUCT DETAILS (Phase 7C)
        ExpandableProductDetailsSection(resultData = resultData)

        // SECTION: PHASE 9C EXPANDABLE PREMIUM DETAILS CARDS
        Phase9CExpandableDetailsSection(resultData = resultData)

        // PREMIUM SHARE SHEET DIALOG (Phase 7C)
        if (showShareSheet) {
            PremiumShareSheetDialog(
                resultData = resultData,
                onDismiss = { showShareSheet = false },
                onShowToast = onShowToast
            )
        }

        // SECTION: AI DETECTION QUALITY METRICS
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "AI SCAN & DETECTION METRICS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray,
                letterSpacing = 1.2.sp
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x06FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x1FFFFFFF)), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("DETECTION CONFIDENCE", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${resultData.detectionConfidence}%", fontSize = 13.sp, fontWeight = FontWeight.Black, color = if (resultData.detectionConfidence >= 95) Color(0xFF2ECC71) else Color(0xFFFFCC00))
                        }
                        
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("TARGET WEBSITE", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(resultData.detectedStore, fontSize = 13.sp, fontWeight = FontWeight.Black, color = TextWhite)
                        }
                        
                        Column(horizontalAlignment = Alignment.End) {
                            Text("PRODUCT CATEGORY", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(resultData.category, fontSize = 13.sp, fontWeight = FontWeight.Black, color = TextWhite)
                        }
                    }
                    
                    HorizontalDivider(color = Color(0x0AFFFFFF), thickness = 1.dp)
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("ESTIMATED MATCH QUALITY", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(resultData.estimatedMatch, fontSize = 13.sp, fontWeight = FontWeight.Black, color = if (resultData.detectionConfidence >= 95) Color(0xFF2ECC71) else Color(0xFFFFCC00))
                        }
                        
                        Column(horizontalAlignment = Alignment.End) {
                            Text("SYSTEM STATUS", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(resultData.status, fontSize = 13.sp, fontWeight = FontWeight.Black, color = if (resultData.isPreviewResult) Color(0xFFFFCC00) else Color(0xFF2ECC71))
                        }
                    }
                }
            }
        }

        if (resultData.isCloudVerificationRequired) {
            PremiumWarningCard()
        }
        
        // SECTION 2: AI Recommendation Insight
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(CrimsonRed.copy(alpha = 0.15f), Color(0x08FFFFFF))
                    )
                )
                .border(BorderStroke(1.2.dp, CrimsonRed.copy(alpha = 0.3f)), RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = "AI Co-Pilot",
                    tint = CrimsonLight,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "AI SHOPPING RECOMMENDATION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = CrimsonLight,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = resultData.aiRecommendation,
                        fontSize = 13.sp,
                        color = TextWhite,
                        lineHeight = 18.sp
                    )
                }
            }
        }
        
        // SECTION 4: Price History Engine
        LaunchedEffect(resultData.url, resultData.currentPrice) {
            PriceTrackerEngine.recordPriceSnapshot(resultData.url, resultData.currentPrice)
        }

        ApplePriceGraphComponent(
            url = resultData.url,
            productName = resultData.productName,
            currentPrice = resultData.currentPrice,
            rawPriceTrend = resultData.priceTrend,
            accentColor = accentColor,
            onShowToast = onShowToast,
            imageUrl = resultData.imageUrl.ifBlank { resultData.productImageWebUrl ?: "" },
            detectedStore = resultData.detectedStore,
            originalPrice = resultData.originalPrice,
            discountPercent = resultData.discountPercent,
            rating = resultData.rating,
            reviewsCount = resultData.reviewsCount,
            availability = resultData.availability,
            bestPrice = resultData.bestPrice
        )
        
        // SECTION 5: Similar Products List
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "SIMILAR PRODUCTS FOR CONSIDERATION",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                resultData.similarProducts.forEach { similar ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x06FFFFFF))
                            .border(BorderStroke(1.dp, Color(0x12FFFFFF)), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OfficialLogo(
                                name = similar.store,
                                modifier = Modifier
                                    .size(36.dp)
                                    .border(BorderStroke(1.dp, Color(0x22FFFFFF)), CircleShape)
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = similar.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Available at ${similar.store}",
                                    fontSize = 11.sp,
                                    color = TextGray
                                )
                            }
                            
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹${String.format("%,.0f", similar.price)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Text(
                                        text = similar.rating.toString(),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // SECTION 6: Offline Preview Mode Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF141414))
                .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudQueue,
                    contentDescription = "Offline Preview",
                    tint = TextGray,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = "Offline Preview Mode",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "This is a preview build. Live shopping data will automatically appear after backend integration.",
                        fontSize = 11.sp,
                        color = TextGray,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
}

// ==================================================
// PHASE 4E EXTENSION COMPOSABLES
// ==================================================

@Composable
fun TrustScoreSection(
    trustScorePercent: Int,
    trustScoreLevel: String
) {
    val levelColor = when (trustScoreLevel) {
        "Excellent" -> Color(0xFF2ECC71)
        "Good" -> Color(0xFF00FFCC)
        "Average" -> Color(0xFFFFD700)
        else -> Color(0xFFFF9800)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x0AFFFFFF))
            .border(BorderStroke(1.dp, Color(0x1EFFFFFF)), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(levelColor.copy(alpha = 0.15f), CircleShape)
                        .border(BorderStroke(1.5.dp, levelColor), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$trustScorePercent%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = levelColor
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "AI TRUST SCORE:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = TextGray,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .background(levelColor.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = trustScoreLevel.uppercase(),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = levelColor
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Verified Store • Genuine Warranty • Direct Returns",
                        fontSize = 11.sp,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SmartCouponsSection(
    coupons: List<CouponOffer>,
    onShowToast: (String) -> Unit
) {
    if (coupons.isEmpty()) return

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = "Coupons",
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "SMART COUPONS & BANK OFFERS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFFFD700),
                letterSpacing = 1.2.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            for (coupon in coupons) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x0CFFFFFF))
                        .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0x22FFD700), RoundedCornerShape(6.dp))
                                        .border(BorderStroke(1.dp, Color(0xFFFFD700)), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = coupon.code,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFFFD700)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .background(Color(0x222ECC71), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = coupon.discountAmountText,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2ECC71)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = coupon.description,
                                fontSize = 12.sp,
                                color = TextWhite,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedButton(
                            onClick = {
                                val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                val clip = android.content.ClipData.newPlainText("Coupon Code", coupon.code)
                                clipboard.setPrimaryClip(clip)
                                onShowToast("✔ Coupon '${coupon.code}' Copied!")
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Copy Code", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductSpecsSection(
    resultData: ShoppingResult
) {
    if (resultData.specifications.isEmpty() && resultData.highlights.isEmpty()) return

    var isExpanded by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x0CFFFFFF))
                .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                .clickable { isExpanded = !isExpanded }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FactCheck,
                    contentDescription = "Specs",
                    tint = CrimsonLight,
                    modifier = Modifier.size(20.dp)
                )
                Column {
                    Text(
                        text = "PRODUCT SPECIFICATIONS & HIGHLIGHTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = CrimsonLight,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Verified Model #${resultData.modelNumber ?: "N/A"}",
                        fontSize = 12.sp,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Toggle",
                tint = TextWhite,
                modifier = Modifier.size(20.dp)
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column {
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0x06FFFFFF))
                        .border(BorderStroke(1.dp, Color(0x12FFFFFF)), RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (resultData.highlights.isNotEmpty()) {
                            Text(
                                text = "Key Highlights",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                            for (highlight in resultData.highlights) {
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("•", color = CrimsonRed, fontWeight = FontWeight.Bold)
                                    Text(highlight, fontSize = 12.sp, color = TextWhite.copy(alpha = 0.85f))
                                }
                            }
                            HorizontalDivider(color = Color(0x10FFFFFF), thickness = 1.dp)
                        }

                        Text(
                            text = "Specifications Table",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )

                        for (spec in resultData.specifications) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(spec.title, fontSize = 11.sp, color = TextGray, modifier = Modifier.weight(0.4f))
                                Text(spec.value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite, modifier = Modifier.weight(0.6f))
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================================================
// PHASE 7B REAL SHOPPING INTELLIGENCE COMPOSABLES
// ==================================================

@Composable
fun DealScoreAndConfidenceSection(
    dealScore: Int,
    dealScoreLabel: String,
    confidencePercent: Int,
    confidenceLabel: String
) {
    val scoreColor = when {
        dealScore >= 80 -> Color(0xFF2ECC71)
        dealScore >= 50 -> Color(0xFFFFB300)
        else -> CrimsonRed
    }

    var animatedProgress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(dealScore) {
        animate(
            initialValue = 0f,
            targetValue = (dealScore.coerceIn(0, 100)) / 100f,
            animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
        ) { value, _ ->
            animatedProgress = value
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x0AFFFFFF))
            .border(BorderStroke(1.dp, Color(0x1EFFFFFF)), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
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
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Deal Score",
                        tint = scoreColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "AI DEAL SCORE & CONFIDENCE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = scoreColor,
                        letterSpacing = 1.2.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .background(scoreColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .border(BorderStroke(1.dp, scoreColor.copy(alpha = 0.5f)), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = dealScoreLabel.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = scoreColor,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier.size(70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0x1AFFFFFF),
                        strokeWidth = 6.dp
                    )
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = scoreColor,
                        strokeWidth = 6.dp
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$dealScore",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                        Text(
                            text = "/100",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextGray
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "PRICE CONFIDENCE INDICATOR",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextGray,
                        letterSpacing = 0.8.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    if (confidencePercent >= 90) Color(0xFF2ECC71)
                                    else if (confidencePercent >= 70) Color(0xFFFFB300)
                                    else CrimsonRed,
                                    CircleShape
                                )
                        )
                        Text(
                            text = confidenceLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    LinearProgressIndicator(
                        progress = { (confidencePercent.coerceIn(0, 100)) / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (confidencePercent >= 90) Color(0xFF2ECC71) else Color(0xFFFFB300),
                        trackColor = Color(0x1AFFFFFF)
                    )
                }
            }
        }
    }
}

@Composable
fun AiBuyingInsightsSection(resultData: ShoppingResult) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = "AI Insights",
                tint = CrimsonLight,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "AI BUYING INSIGHTS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = CrimsonLight,
                letterSpacing = 1.2.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x06FFFFFF))
                .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("🎯", fontSize = 12.sp)
                        Text(
                            text = "Who Should Buy This?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF2ECC71)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = resultData.whoShouldBuy,
                        fontSize = 12.sp,
                        color = TextWhite.copy(alpha = 0.9f),
                        lineHeight = 17.sp
                    )
                }

                HorizontalDivider(color = Color(0x0AFFFFFF), thickness = 1.dp)

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("⚠️", fontSize = 12.sp)
                        Text(
                            text = "Who Should Avoid It?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFFB300)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = resultData.whoShouldAvoid,
                        fontSize = 12.sp,
                        color = TextWhite.copy(alpha = 0.9f),
                        lineHeight = 17.sp
                    )
                }

                HorizontalDivider(color = Color(0x0AFFFFFF), thickness = 1.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "BEST USE CASES",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = TextGray,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        resultData.bestUseCases.forEach { useCase ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(vertical = 1.dp)
                            ) {
                                Text("•", fontSize = 10.sp, color = CrimsonLight, fontWeight = FontWeight.Bold)
                                Text(
                                    text = useCase,
                                    fontSize = 11.sp,
                                    color = TextWhite,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "VALUE & QUALITY",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = TextGray,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = resultData.valueForMoney,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2ECC71)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = resultData.expectedQuality,
                            fontSize = 10.sp,
                            color = TextGray,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProsConsDeckSection(
    pros: List<String>,
    cons: List<String>
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
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
                    imageVector = Icons.Default.ThumbsUpDown,
                    contentDescription = "Pros & Cons",
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "PROS & CONS CARD DECK",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    letterSpacing = 1.2.sp
                )
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x1AFFFFFF))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (selectedTab == 0) Color(0xFF2ECC71) else Color.Transparent)
                        .clickable { selectedTab = 0 }
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Pros (${pros.size})",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = if (selectedTab == 0) Color.Black else TextWhite
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (selectedTab == 1) CrimsonRed else Color.Transparent)
                        .clickable { selectedTab = 1 }
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cons (${cons.size})",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                }
            }
        }

        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
            },
            label = "ProsConsAnimation"
        ) { tabIndex ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        if (tabIndex == 0) Color(0x0C2ECC71) else Color(0x0CFF2E44)
                    )
                    .border(
                        BorderStroke(
                            1.dp,
                            if (tabIndex == 0) Color(0x302ECC71) else Color(0x30FF2E44)
                        ),
                        RoundedCornerShape(18.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val currentList = if (tabIndex == 0) pros else cons
                    val bulletIcon = if (tabIndex == 0) Icons.Default.CheckCircle else Icons.Default.Warning
                    val bulletTint = if (tabIndex == 0) Color(0xFF2ECC71) else CrimsonRed

                    if (currentList.isEmpty()) {
                        Text(
                            text = if (tabIndex == 0) "No specific pros flagged." else "No major cons detected.",
                            fontSize = 12.sp,
                            color = TextGray
                        )
                    } else {
                        currentList.forEach { item ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = bulletIcon,
                                    contentDescription = null,
                                    tint = bulletTint,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = item,
                                    fontSize = 12.sp,
                                    color = TextWhite,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BetterAlternativesSection(
    alternatives: List<SimilarProduct>
) {
    if (alternatives.isEmpty()) return

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CompareArrows,
                contentDescription = "Alternatives",
                tint = TextWhite,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "BETTER ALTERNATIVES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                letterSpacing = 1.2.sp
            )
        }

        if (alternatives.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x06FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x12FFFFFF)), RoundedCornerShape(16.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = TextGray,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "No verified alternatives available.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextGray
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                alternatives.forEach { alternative ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x06FFFFFF))
                            .border(BorderStroke(1.dp, Color(0x12FFFFFF)), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OfficialLogo(
                                name = alternative.store,
                                modifier = Modifier
                                    .size(36.dp)
                                    .border(BorderStroke(1.dp, Color(0x22FFFFFF)), CircleShape)
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = alternative.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Available on ${alternative.store}",
                                    fontSize = 11.sp,
                                    color = TextGray
                                )
                            }
                            
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹${String.format("%,.0f", alternative.price)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Text(
                                        text = alternative.rating.toString(),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
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
fun SmartActionButtonsRow(
    resultData: ShoppingResult,
    onShowToast: (String) -> Unit,
    onOpenShareSheet: () -> Unit
) {
    val context = LocalContext.current
    val isWishlisted = com.example.data.WishlistStorageManager.isWishlisted(resultData.url)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // OPEN LINK
        Button(
            onClick = {
                try {
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(resultData.url))
                    context.startActivity(intent)
                } catch (e: Exception) {
                    onShowToast("✖ Could not open product URL")
                }
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed, contentColor = TextWhite),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            Icon(Icons.Default.OpenInNew, contentDescription = "Open", modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Open", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        // SAVE TO WISHLIST
        Button(
            onClick = {
                val added = com.example.data.WishlistStorageManager.toggleWishlist(
                    productName = resultData.productName,
                    merchant = resultData.detectedStore,
                    price = resultData.bestPrice,
                    url = resultData.url,
                    thumbnailUrl = resultData.imageUrl.ifEmpty { resultData.productImages.firstOrNull() },
                    dealScore = resultData.dealScore,
                    category = resultData.category
                )
                if (added) {
                    onShowToast("❤️ Saved to Wishlist!")
                } else {
                    onShowToast("Removed from Wishlist")
                }
            },
            modifier = Modifier.weight(1.1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isWishlisted) CrimsonLight.copy(alpha = 0.25f) else Color(0x1AFFFFFF),
                contentColor = if (isWishlisted) CrimsonLight else TextWhite
            ),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            Icon(
                imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Wishlist",
                modifier = Modifier.size(14.dp),
                tint = if (isWishlisted) CrimsonLight else TextWhite
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (isWishlisted) "Saved" else "Wishlist", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        // SHARE SHEET
        Button(
            onClick = { onOpenShareSheet() },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0x1AFFFFFF), contentColor = TextWhite),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Share", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        // SAVE REPORT TO HISTORY
        Button(
            onClick = {
                val masterReport = com.example.reports.AiReportEngine.buildShoppingMasterReport(resultData)
                com.example.reports.ReportStorageManager.saveReport(masterReport.id)
                onShowToast("✔ Saved to Shopping History!")
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0x1AFFFFFF), contentColor = TextWhite),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            Icon(Icons.Default.BookmarkBorder, contentDescription = "Save", modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Save", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// =========================================================
// PHASE 7C: SMART BUY RECOMMENDATION CARD
// =========================================================
private data class Phase7CTuple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun SmartBuyRecommendationCard(resultData: ShoppingResult) {
    val (badgeText, badgeColor, icon, bgGradient) = when (resultData.buyRecommendationState) {
        "BUY_NOW" -> Phase7CTuple(
            "BUY NOW — BEST PRICE",
            Color(0xFF2ECC71),
            Icons.Default.ShoppingBag,
            Brush.horizontalGradient(listOf(Color(0x152ECC71), Color(0x052ECC71)))
        )
        "WAIT" -> Phase7CTuple(
            "WAIT FOR BETTER PRICE",
            Color(0xFFFFB300),
            Icons.Default.Timer,
            Brush.horizontalGradient(listOf(Color(0x15FFB300), Color(0x05FFB300)))
        )
        else -> Phase7CTuple(
            "WATCH THIS PRODUCT",
            Color(0xFF9B51E0),
            Icons.Default.NotificationsActive,
            Brush.horizontalGradient(listOf(Color(0x159B51E0), Color(0x059B51E0)))
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(bgGradient)
            .border(BorderStroke(1.dp, badgeColor.copy(alpha = 0.4f)), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "AI Recommendation",
                        tint = badgeColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "AI RECOMMENDATION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = badgeColor,
                        letterSpacing = 1.2.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .background(badgeColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .border(BorderStroke(1.dp, badgeColor.copy(alpha = 0.5f)), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = badgeColor, modifier = Modifier.size(12.dp))
                        Text(
                            text = badgeText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = badgeColor,
                            letterSpacing = 0.8.sp
                        )
                    }
                }
            }

            Text(
                text = resultData.buyRecommendationReason,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextWhite,
                lineHeight = 18.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x0CFFFFFF))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("CURRENT BEST PRICE", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                    Text("₹${String.format("%,.0f", resultData.bestPrice)}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = TextWhite)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("ESTIMATED SAVINGS", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                    Text("₹${String.format("%,.0f", resultData.savingsAmount)}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF2ECC71))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("DEAL SCORE", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                    Text("${resultData.dealScore}/100", fontSize = 14.sp, fontWeight = FontWeight.Black, color = badgeColor)
                }
            }
        }
    }
}

// =========================================================
// PHASE 7C: PRICE TIMELINE & HISTORY CARD
// =========================================================
@Composable
fun PriceTimelineCard(resultData: ShoppingResult) {
    if (!resultData.hasHistoricalPriceData || resultData.priceHistoryTimeline.isEmpty()) return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x08FFFFFF))
            .border(BorderStroke(1.dp, Color(0x1EFFFFFF)), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                        imageVector = Icons.Default.Timeline,
                        contentDescription = "Price Timeline",
                        tint = CrimsonLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "PRICE TIMELINE & HISTORY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = CrimsonLight,
                        letterSpacing = 1.2.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .background(Color(0x1AFFFFFF), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = resultData.priceTrendDirection.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }

            if (!resultData.hasHistoricalPriceData || resultData.priceHistoryTimeline.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x0AFFFFFF))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Historical pricing isn't available yet.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "Price tracking initiated for this SKU. Historical trend graph will automatically populate on future syncs.",
                            fontSize = 10.sp,
                            color = TextGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("TODAY", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                        Text("₹${String.format("%,.0f", resultData.bestPrice)}", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFF2ECC71))
                    }
                    Column {
                        Text("LOWEST SEEN", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                        Text("₹${String.format("%,.0f", resultData.priceLowest)}", fontSize = 13.sp, fontWeight = FontWeight.Black, color = TextWhite)
                    }
                    Column {
                        Text("HIGHEST SEEN", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                        Text("₹${String.format("%,.0f", resultData.priceHighest)}", fontSize = 13.sp, fontWeight = FontWeight.Black, color = CrimsonRed)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    resultData.priceHistoryTimeline.forEachIndexed { index, point ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "₹${String.format("%,.0f", point.price)}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (index == resultData.priceHistoryTimeline.lastIndex) Color(0xFF2ECC71) else TextWhite
                            )
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(
                                        if (index == resultData.priceHistoryTimeline.lastIndex) Color(0xFF2ECC71) else CrimsonLight,
                                        CircleShape
                                    )
                                    .border(BorderStroke(2.dp, Color.Black), CircleShape)
                            )
                            Text(
                                text = point.dateLabel,
                                fontSize = 8.sp,
                                color = TextGray
                            )
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// PHASE 7C: SMART PRODUCT COMPARISON SECTION
// =========================================================
@Composable
fun SmartProductComparisonSection(resultData: ShoppingResult) {
    if (resultData.priceComparison.isEmpty()) return

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Compare,
                contentDescription = "Smart Comparison",
                tint = TextWhite,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "SMART PRODUCT COMPARISON",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                letterSpacing = 1.2.sp
            )
        }

        if (resultData.verifiedAlternatives.isEmpty() && resultData.similarProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x06FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x12FFFFFF)), RoundedCornerShape(16.dp))
                    .padding(18.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = TextGray, modifier = Modifier.size(18.dp))
                        Text(
                            text = "No verified comparison available.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                    Text(
                        text = "We only compare against verified official merchant listings. No fake or unverified alternatives generated.",
                        fontSize = 10.sp,
                        color = TextGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            val context = LocalContext.current
            val bestChoice = resultData.priceComparison.minByOrNull { it.price }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // 1. Current Detected Product
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x0AFFFFFF))
                        .border(BorderStroke(1.dp, Color(0x1EFFFFFF)), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0x22FFFFFF), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("CURRENT PRODUCT", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = resultData.productName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(text = "Detected on ${resultData.detectedStore}", fontSize = 10.sp, color = TextGray)
                        }
                        Text(
                            text = "₹${String.format("%,.0f", resultData.currentPrice)}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                    }
                }

                // 2. Verified Alternative Option
                val alternative = resultData.verifiedAlternatives.firstOrNull()
                if (alternative != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x0AFFFFFF))
                            .border(BorderStroke(1.dp, Color(0x1EFFFFFF)), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0x159B51E0), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("VERIFIED ALTERNATIVE", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9B51E0))
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = alternative.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(text = "Available on ${alternative.store}", fontSize = 10.sp, color = TextGray)
                            }
                            Text(
                                text = "₹${String.format("%,.0f", alternative.price)}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                        }
                    }
                }

                // 3. Best Choice (AI Winner)
                if (bestChoice != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0x202ECC71), Color(0x052ECC71))))
                            .border(BorderStroke(1.5.dp, Color(0xFF2ECC71)), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFF2ECC71), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("🏆 BEST CHOICE (AI WINNER)", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = bestChoice.store,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                                Text(text = "${bestChoice.deliverySpeed} • ${bestChoice.returnPolicy}", fontSize = 10.sp, color = Color(0xFF2ECC71))
                            }
                            Button(
                                onClick = {
                                    try {
                                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(bestChoice.url))
                                        context.startActivity(intent)
                                    } catch (e: Exception) {}
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71), contentColor = Color.Black),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("₹${String.format("%,.0f", bestChoice.price)}", fontSize = 12.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// PHASE 7C: EXPANDABLE PRODUCT DETAILS SECTION
// =========================================================
@Composable
fun ExpandableProductDetailsSection(resultData: ShoppingResult) {
    var expandedIndex by remember { mutableIntStateOf(-1) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Product Details",
                tint = TextWhite,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "PREMIUM PRODUCT DETAILS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                letterSpacing = 1.2.sp
            )
        }

        val detailSections = listOf(
            Triple("Specifications", Icons.Default.List, "Technical specs and attributes"),
            Triple("Highlights", Icons.Default.AutoAwesome, "Top features and key benefits"),
            Triple("Merchant Information", Icons.Default.Storefront, resultData.merchantInfoText),
            Triple("Delivery Info (Future Ready)", Icons.Default.LocalShipping, resultData.deliveryInfoText),
            Triple("Warranty (Future Ready)", Icons.Default.VerifiedUser, resultData.warrantyInfoText)
        )

        detailSections.forEachIndexed { index, (title, icon, summary) ->
            val isExpanded = expandedIndex == index

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x08FFFFFF))
                    .border(BorderStroke(1.dp, if (isExpanded) CrimsonLight.copy(alpha = 0.5f) else Color(0x12FFFFFF)), RoundedCornerShape(16.dp))
                    .clickable { expandedIndex = if (isExpanded) -1 else index }
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(imageVector = icon, contentDescription = title, tint = if (isExpanded) CrimsonLight else TextGray, modifier = Modifier.size(18.dp))
                            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        }

                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle",
                            tint = TextGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            HorizontalDivider(color = Color(0x0AFFFFFF), thickness = 1.dp)
                            Spacer(modifier = Modifier.height(2.dp))

                            when (index) {
                                0 -> {
                                    if (resultData.specifications.isEmpty()) {
                                        Text("Standard manufacturer specifications apply.", fontSize = 11.sp, color = TextGray)
                                    } else {
                                        resultData.specifications.forEach { spec ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(spec.title, fontSize = 11.sp, color = TextGray)
                                                Text(spec.value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                            }
                                        }
                                    }
                                }
                                1 -> {
                                    if (resultData.highlights.isEmpty()) {
                                        Text("Official brand product verified with direct store link.", fontSize = 11.sp, color = TextGray)
                                    } else {
                                        resultData.highlights.forEach { highlight ->
                                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                Text("•", fontSize = 12.sp, color = CrimsonLight, fontWeight = FontWeight.Bold)
                                                Text(highlight, fontSize = 11.sp, color = TextWhite, lineHeight = 16.sp)
                                            }
                                        }
                                    }
                                }
                                else -> {
                                    Text(summary, fontSize = 12.sp, color = TextWhite.copy(alpha = 0.9f), lineHeight = 17.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// PHASE 7C: PREMIUM SHARE SHEET DIALOG
// =========================================================
@Composable
fun PremiumShareSheetDialog(
    resultData: ShoppingResult,
    onDismiss: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF121212),
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, tint = CrimsonLight)
                Text("Share Deal Intelligence", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextWhite)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x12FFFFFF))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OfficialLogo(
                            name = resultData.detectedStore,
                            modifier = Modifier.size(36.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(resultData.productName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("Best Price: ₹${String.format("%,.0f", resultData.bestPrice)} on ${resultData.detectedStore}", fontSize = 10.sp, color = Color(0xFF2ECC71), fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .background(CrimsonRed.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("${resultData.dealScore}/100", fontSize = 10.sp, fontWeight = FontWeight.Black, color = CrimsonLight)
                        }
                    }
                }

                Text(
                    text = "Summary: ${resultData.aiRecommendation}",
                    fontSize = 11.sp,
                    color = TextGray,
                    lineHeight = 16.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            val clip = android.content.ClipData.newPlainText("Product Deal Link", resultData.url)
                            clipboard.setPrimaryClip(clip)
                            onShowToast("✔ Product link copied!")
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x22FFFFFF), contentColor = TextWhite),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy Link", fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            val shareText = "🔥 ViralToolAI Deal Alert:\n\n" +
                                    "📦 ${resultData.productName}\n" +
                                    "💰 Price: ₹${String.format("%,.0f", resultData.bestPrice)} on ${resultData.detectedStore}\n" +
                                    "🏷️ Deal Score: ${resultData.dealScore}/100\n" +
                                    "🔗 Link: ${resultData.url}\n\n" +
                                    "Shared via ViralToolAI 🚀"
                            val sendIntent = android.content.Intent().apply {
                                action = android.content.Intent.ACTION_SEND
                                putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(android.content.Intent.createChooser(sendIntent, "Share Deal Report"))
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed, contentColor = TextWhite),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Share App", fontSize = 11.sp)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = TextGray, fontWeight = FontWeight.Bold)
            }
        }
    )
}

// =========================================================
// PHASE 7D: SMART DEALS & DISCOUNTS CARD
// =========================================================
@Composable
fun SmartDealsCardSection(resultData: ShoppingResult) {
    if (!resultData.hasSmartDeals || resultData.smartDealOffer.isBlank()) return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(listOf(Color(0x15FFD700), Color(0x05FFD700))))
            .border(BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.4f)), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = "Smart Deals",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "SMART DEALS & OFFERS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFD700),
                        letterSpacing = 1.2.sp
                    )
                }

                if (resultData.hasSmartDeals && resultData.smartDealDiscountPercent > 0) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFD700).copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            .border(BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f)), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${resultData.smartDealDiscountPercent}% OFF",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFFD700)
                        )
                    }
                }
            }

            if (!resultData.hasSmartDeals) {
                Text(
                    text = "No verified deals available.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextGray
                )
            } else {
                Text(
                    text = resultData.smartDealOffer,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    lineHeight = 18.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (resultData.smartDealLimitedTime) {
                        Box(
                            modifier = Modifier
                                .background(CrimsonRed.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Timer, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(12.dp))
                                Text("LIMITED TIME OFFER", fontSize = 9.sp, fontWeight = FontWeight.Black, color = CrimsonLight)
                            }
                        }
                    }

                    if (resultData.smartDealVerifiedMerchant) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF2ECC71).copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF2ECC71), modifier = Modifier.size(12.dp))
                                Text("VERIFIED MERCHANT", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF2ECC71))
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// PHASE 7D: COUPON ENGINE (Future Ready Architecture)
// =========================================================
@Composable
fun CouponEngineSection(
    resultData: ShoppingResult,
    onShowToast: (String) -> Unit
) {
    if (resultData.coupons.isEmpty()) return

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x08FFFFFF))
            .border(BorderStroke(1.dp, Color(0x1EFFFFFF)), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                        imageVector = Icons.Default.ConfirmationNumber,
                        contentDescription = "Coupons",
                        tint = CrimsonLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "COUPON ENGINE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = CrimsonLight,
                        letterSpacing = 1.2.sp
                    )
                }

                Text(
                    text = "FUTURE READY",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    color = TextGray
                )
            }

            if (resultData.coupons.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x08FFFFFF))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No verified coupons available.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextGray
                    )
                }
            } else {
                resultData.coupons.forEach { coupon ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x12FFFFFF))
                            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(coupon.code, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFD700))
                                Text(coupon.description, fontSize = 11.sp, color = TextWhite)
                                Text(coupon.discountAmountText + " • " + coupon.terms, fontSize = 9.sp, color = TextGray)
                            }

                            Button(
                                onClick = {
                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                    val clip = android.content.ClipData.newPlainText("Coupon Code", coupon.code)
                                    clipboard.setPrimaryClip(clip)
                                    onShowToast("✔ Coupon code '${coupon.code}' copied!")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700), contentColor = Color.Black),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("COPY", fontSize = 10.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// PHASE 7D: PRICE DROP WATCH CARD
// =========================================================
@Composable
fun PriceDropWatchCard(resultData: ShoppingResult) {
    val (statusText, statusColor, statusIcon) = when (resultData.priceDropWatchStatus) {
        "Price Dropped" -> Triple("PRICE DROPPED", Color(0xFF2ECC71), Icons.Default.TrendingDown)
        "Price Increased" -> Triple("PRICE INCREASED", CrimsonRed, Icons.Default.TrendingUp)
        "Price Stable" -> Triple("PRICE STABLE", Color(0xFF00FFCC), Icons.Default.TrendingFlat)
        else -> Triple("PRICE UNKNOWN", TextGray, Icons.Default.HelpOutline)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x08FFFFFF))
            .border(BorderStroke(1.dp, Color(0x1EFFFFFF)), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Price Drop Watch",
                        tint = statusColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "PRICE DROP WATCH",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = statusColor,
                        letterSpacing = 1.2.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .background(statusColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(imageVector = statusIcon, contentDescription = null, tint = statusColor, modifier = Modifier.size(12.dp))
                        Text(text = statusText, fontSize = 9.sp, fontWeight = FontWeight.Black, color = statusColor)
                    }
                }
            }

            if (!resultData.hasHistoricalPriceData) {
                Text(
                    text = "Price history unavailable.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextGray
                )
            } else {
                Text(
                    text = "Monitored lowest price: ₹${String.format("%,.0f", resultData.priceLowest)} across official stores.",
                    fontSize = 12.sp,
                    color = TextWhite,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

// =========================================================
// PHASE 7D: MERCHANT TRUST SCORE CARD
// =========================================================
@Composable
fun MerchantTrustScoreCard(resultData: ShoppingResult) {
    if (!resultData.merchantTrustInfoAvailable) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x08FFFFFF))
                .border(BorderStroke(1.dp, Color(0x1EFFFFFF)), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("MERCHANT TRUST SCORE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextGray, letterSpacing = 1.2.sp)
                Text("Trust information unavailable.", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray)
            }
        }
        return
    }

    val scoreColor = when (resultData.merchantTrustLabel) {
        "Excellent" -> Color(0xFF2ECC71)
        "Good" -> Color(0xFF00FFCC)
        "Average" -> Color(0xFFFFD700)
        else -> TextGray
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x0AFFFFFF))
            .border(BorderStroke(1.dp, Color(0x1EFFFFFF)), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(scoreColor.copy(alpha = 0.15f), CircleShape)
                        .border(BorderStroke(2.dp, scoreColor), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${resultData.merchantTrustScore}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = scoreColor
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "MERCHANT TRUST SCORE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = TextGray,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .background(scoreColor.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = resultData.merchantTrustLabel.uppercase(),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = scoreColor
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = resultData.merchantInfoText,
                        fontSize = 11.sp,
                        color = TextWhite,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// =========================================================
// PHASE 7D: SAVE MONEY TIPS (GENERAL SHOPPING ADVICE)
// =========================================================
@Composable
fun SaveMoneyTipsSection(resultData: ShoppingResult) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x08FFFFFF))
            .border(BorderStroke(1.dp, Color(0x1EFFFFFF)), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = "Tips",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "GENERAL SHOPPING ADVICE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFFD700),
                    letterSpacing = 1.2.sp
                )
            }

            resultData.generalShoppingAdvice.forEach { advice ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("•", fontSize = 13.sp, color = CrimsonLight, fontWeight = FontWeight.Black)
                    Text(
                        text = advice,
                        fontSize = 12.sp,
                        color = TextWhite,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

// =========================================================
// PHASE 9C: PRODUCT QUALITY ESTIMATE METER
// =========================================================
@Composable
fun ProductQualityMeterSection(resultData: ShoppingResult) {
    val qualityEstimate = remember(resultData) { com.example.engine.ProductQualityEngine.estimateQuality(resultData) }
    
    val animatedScore by animateIntAsState(
        targetValue = qualityEstimate.scorePercent,
        animationSpec = tween(1200, easing = EaseOutCubic),
        label = "QualityScoreAnim"
    )

    val (badgeColor, bgGradient) = when (qualityEstimate.level) {
        com.example.engine.QualityLevel.EXCELLENT -> Color(0xFF2ECC71) to listOf(Color(0x222ECC71), Color(0x082ECC71))
        com.example.engine.QualityLevel.GOOD -> Color(0xFF3498DB) to listOf(Color(0x223498DB), Color(0x083498DB))
        com.example.engine.QualityLevel.AVERAGE -> Color(0xFFFF9800) to listOf(Color(0x22FF9800), Color(0x08FF9800))
        com.example.engine.QualityLevel.UNKNOWN -> Color(0xFF9E9E9E) to listOf(Color(0x229E9E9E), Color(0x089E9E9E))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.verticalGradient(bgGradient))
            .border(BorderStroke(1.2.dp, badgeColor.copy(alpha = 0.35f)), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HighQuality,
                        contentDescription = "Quality",
                        tint = badgeColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "AI PRODUCT QUALITY ESTIMATE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = badgeColor,
                        letterSpacing = 1.2.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .background(badgeColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .border(BorderStroke(1.dp, badgeColor.copy(alpha = 0.5f)), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = qualityEstimate.level.displayName.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = badgeColor,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Animated Progress Bar Meter
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Verified Quality Score",
                        fontSize = 12.sp,
                        color = TextGray,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$animatedScore / 100",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(CircleShape)
                        .background(Color(0x1AFFFFFF))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animatedScore / 100f)
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(badgeColor.copy(alpha = 0.7f), badgeColor)
                                )
                            )
                    )
                }
            }

            Text(
                text = qualityEstimate.summary,
                fontSize = 12.sp,
                color = TextWhite,
                lineHeight = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// =========================================================
// PHASE 9C: PRODUCT KNOWLEDGE ENGINE (CENTRALIZED INTELLIGENCE)
// =========================================================
@Composable
fun ProductIntelligenceSection(resultData: ShoppingResult) {
    val intelData = remember(resultData) { com.example.engine.ProductIntelligenceEngine.analyzeProduct(resultData) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0x0BFFFFFF))
            .border(BorderStroke(1.dp, Color(0x1FFFFFFF)), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Intelligence",
                        tint = CrimsonLight,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "PRODUCT KNOWLEDGE ENGINE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = CrimsonLight,
                        letterSpacing = 1.2.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .background(CrimsonRed.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${intelData.verifiedFactsCount} VERIFIED FACTS",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                }
            }

            Text(
                text = intelData.intelligenceSummary,
                fontSize = 11.sp,
                color = TextGray
            )

            HorizontalDivider(color = Color(0x10FFFFFF), thickness = 1.dp)

            val attributes = listOf(
                "Category" to intelData.category,
                "Brand" to intelData.brand,
                "Material" to intelData.material,
                "Colour" to intelData.colour,
                "Variant" to intelData.variant,
                "Size" to intelData.size,
                "Target Audience" to intelData.targetAudience
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                attributes.forEach { (label, value) ->
                    val isAvailable = value != "Not Available"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            color = TextGray,
                            fontWeight = FontWeight.Medium
                        )
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (isAvailable) {
                                Box(modifier = Modifier.size(6.dp).background(Color(0xFF2ECC71), CircleShape))
                            }
                            Text(
                                text = value,
                                fontSize = 12.sp,
                                fontWeight = if (isAvailable) FontWeight.Bold else FontWeight.Normal,
                                color = if (isAvailable) TextWhite else Color(0x66FFFFFF)
                            )
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// PHASE 9C: SMART BUYING GUIDE (LABELED AI RECOMMENDATION)
// =========================================================
@Composable
fun SmartBuyingGuideSection(resultData: ShoppingResult) {
    val guideData = remember(resultData) { com.example.engine.SmartBuyingGuideEngine.generateGuide(resultData) }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = "Buying Guide",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "SMART BUYING GUIDE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    letterSpacing = 1.2.sp
                )
            }

            Box(
                modifier = Modifier
                    .background(Brush.horizontalGradient(listOf(CrimsonRed, Color(0xFF8B0000))), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "AI RECOMMENDATION",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    letterSpacing = 1.sp
                )
            }
        }

        // Card 1: Best For
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x0CFFFFFF))
                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Stars, contentDescription = null, tint = Color(0xFF2ECC71), modifier = Modifier.size(16.dp))
                    Text("BEST FOR", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFF2ECC71), letterSpacing = 1.sp)
                }
                Text(guideData.bestFor, fontSize = 13.sp, color = TextWhite, fontWeight = FontWeight.SemiBold, lineHeight = 18.sp)
            }
        }

        // Card 2: Things To Check Before Buying
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x0CFFFFFF))
                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.CheckCircleOutline, contentDescription = null, tint = Color(0xFFFF9800), modifier = Modifier.size(16.dp))
                    Text("THINGS TO CHECK BEFORE BUYING", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF9800), letterSpacing = 1.sp)
                }
                guideData.checkBeforeBuying.forEach { item ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                        Text("•", fontSize = 13.sp, color = CrimsonLight, fontWeight = FontWeight.Black)
                        Text(item, fontSize = 12.sp, color = TextWhite, lineHeight = 17.sp)
                    }
                }
            }
        }

        // Card 3 & 4: Who Should Buy / Who Should Avoid Grid
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            // Who Should Buy
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x222ECC71).copy(alpha = 0.1f))
                    .border(BorderStroke(1.dp, Color(0xFF2ECC71).copy(alpha = 0.3f)), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.ThumbUp, contentDescription = null, tint = Color(0xFF2ECC71), modifier = Modifier.size(14.dp))
                        Text("WHO SHOULD BUY", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFF2ECC71), letterSpacing = 0.8.sp)
                    }
                    Text(guideData.whoShouldBuy, fontSize = 11.sp, color = TextWhite, lineHeight = 16.sp)
                }
            }

            // Who Should Avoid
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x22E74C3C).copy(alpha = 0.1f))
                    .border(BorderStroke(1.dp, Color(0xFFE74C3C).copy(alpha = 0.3f)), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.ThumbDown, contentDescription = null, tint = Color(0xFFE74C3C), modifier = Modifier.size(14.dp))
                        Text("WHO SHOULD AVOID", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFE74C3C), letterSpacing = 0.8.sp)
                    }
                    Text(guideData.whoShouldAvoid, fontSize = 11.sp, color = TextWhite, lineHeight = 16.sp)
                }
            }
        }
    }
}

// =========================================================
// PHASE 9C: QUICK ACTIONS BAR
// =========================================================
@Composable
fun Phase9CQuickActionsBar(
    resultData: ShoppingResult,
    onShowToast: (String) -> Unit
) {
    val context = LocalContext.current
    var isSaved by remember(resultData.url) { mutableStateOf(com.example.data.WishlistStorageManager.isWishlisted(resultData.url)) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x0FFFFFFF))
            .border(BorderStroke(1.dp, Color(0x1EFFFFFF)), RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "QUICK ACTIONS",
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                color = TextGray,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Action 1: Open Product
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            try {
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(resultData.url))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                onShowToast("✖ Could not open link")
                            }
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color(0x1AFFFFFF), CircleShape)
                            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Launch, contentDescription = "Open", tint = TextWhite, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Open", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }

                // Action 2: Copy Link
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            val clip = android.content.ClipData.newPlainText("Product Link", resultData.url)
                            clipboard.setPrimaryClip(clip)
                            onShowToast("✔ Product Link Copied!")
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color(0x1AFFFFFF), CircleShape)
                            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = TextWhite, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Copy", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }

                // Action 3: Share
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val shareText = "🛒 ${resultData.productName}\n💰 Best Price: ₹${String.format("%,.0f", resultData.bestPrice)}\n🔗 Link: ${resultData.url}"
                            val intent = android.content.Intent().apply {
                                action = android.content.Intent.ACTION_SEND
                                putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(android.content.Intent.createChooser(intent, "Share Product"))
                            onShowToast("✔ Ready to share")
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color(0x1AFFFFFF), CircleShape)
                            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = TextWhite, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Share", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }

                // Action 4: Save (Wishlist)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val nowSaved = com.example.data.WishlistStorageManager.toggleWishlist(
                                productName = resultData.productName,
                                merchant = resultData.detectedStore,
                                price = resultData.bestPrice,
                                url = resultData.url,
                                thumbnailUrl = resultData.imageUrl.ifEmpty { resultData.productImages.firstOrNull() },
                                dealScore = resultData.dealScore,
                                category = resultData.category
                            )
                            isSaved = nowSaved
                            if (nowSaved) {
                                onShowToast("♥ Added to Saved Wishlist!")
                            } else {
                                onShowToast("Removed from Wishlist")
                            }
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(if (isSaved) CrimsonRed.copy(alpha = 0.3f) else Color(0x1AFFFFFF), CircleShape)
                            .border(BorderStroke(1.dp, if (isSaved) CrimsonRed else Color(0x22FFFFFF)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Save",
                            tint = if (isSaved) CrimsonLight else TextWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(if (isSaved) "Saved" else "Save", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSaved) CrimsonLight else TextWhite)
                }

                // Action 5: Compare (Future Ready)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onShowToast("⚡ Side-by-side comparison active below")
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color(0x1AFFFFFF), CircleShape)
                            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CompareArrows, contentDescription = "Compare", tint = TextWhite, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Compare", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }
        }
    }
}

// =========================================================
// PHASE 9C: EXPANDABLE PREMIUM CARDS WITH APPLE-STYLE ANIMATION
// =========================================================
@Composable
fun Phase9CExpandableDetailsSection(resultData: ShoppingResult) {
    var expandedCardIndex by remember { mutableIntStateOf(0) }

    val cardsList = listOf(
        "Overview",
        "Specifications",
        "Buying Advice",
        "Merchant",
        "AI Summary"
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.MenuBook, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(20.dp))
            Text(
                text = "PREMIUM PRODUCT DETAILS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = CrimsonLight,
                letterSpacing = 1.2.sp
            )
        }

        cardsList.forEachIndexed { index, title ->
            val isExpanded = expandedCardIndex == index
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x0AFFFFFF))
                    .border(BorderStroke(1.dp, if (isExpanded) CrimsonRed.copy(alpha = 0.5f) else Color(0x12FFFFFF)), RoundedCornerShape(18.dp))
                    .animateContentSize(animationSpec = tween(350, easing = EaseOutCubic))
                    .clickable {
                        expandedCardIndex = if (isExpanded) -1 else index
                    }
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            val icon = when (index) {
                                0 -> Icons.Default.Info
                                1 -> Icons.Default.ListAlt
                                2 -> Icons.Default.Lightbulb
                                3 -> Icons.Default.Storefront
                                else -> Icons.Default.AutoAwesome
                            }
                            Icon(icon, contentDescription = null, tint = if (isExpanded) CrimsonLight else TextGray, modifier = Modifier.size(18.dp))
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isExpanded) TextWhite else Color(0xCCFFFFFF)
                            )
                        }

                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = TextGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                        exit = fadeOut(animationSpec = tween(200)) + shrinkVertically(animationSpec = tween(200))
                    ) {
                        Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            HorizontalDivider(color = Color(0x10FFFFFF), thickness = 1.dp)

                            when (index) {
                                0 -> { // Overview
                                    Text("Product Name: ${resultData.productName}", fontSize = 12.sp, color = TextWhite)
                                    Text("Brand: ${resultData.brand}", fontSize = 12.sp, color = TextWhite)
                                    Text("Category: ${resultData.category}", fontSize = 12.sp, color = TextWhite)
                                    Text("Match Confidence: ${resultData.detectionConfidence}% (${resultData.priceConfidenceLabel})", fontSize = 12.sp, color = Color(0xFF2ECC71))
                                }
                                1 -> { // Specifications
                                    val intel = com.example.engine.ProductIntelligenceEngine.analyzeProduct(resultData)
                                    Text("Material: ${intel.material}", fontSize = 12.sp, color = TextWhite)
                                    Text("Colour: ${intel.colour}", fontSize = 12.sp, color = TextWhite)
                                    Text("Variant: ${intel.variant}", fontSize = 12.sp, color = TextWhite)
                                    Text("Size: ${intel.size}", fontSize = 12.sp, color = TextWhite)
                                    if (resultData.specifications.isNotEmpty()) {
                                        resultData.specifications.take(5).forEach { spec ->
                                            Text("${spec.title}: ${spec.value}", fontSize = 11.sp, color = TextGray)
                                        }
                                    }
                                }
                                2 -> { // Buying Advice
                                    val guide = com.example.engine.SmartBuyingGuideEngine.generateGuide(resultData)
                                    Box(
                                        modifier = Modifier
                                            .background(CrimsonRed.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("AI RECOMMENDATION", fontSize = 8.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                    }
                                    Text("Best For: ${guide.bestFor}", fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.SemiBold)
                                    Text("Who Should Buy: ${guide.whoShouldBuy}", fontSize = 11.sp, color = TextGray)
                                    Text("Who Should Avoid: ${guide.whoShouldAvoid}", fontSize = 11.sp, color = TextGray)
                                }
                                3 -> { // Merchant
                                    Text("Primary Store: ${resultData.detectedStore}", fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                                    Text("Seller Reputation: ${resultData.merchantInfoText}", fontSize = 11.sp, color = TextGray)
                                    Text("Delivery Option: ${resultData.deliveryInfoText}", fontSize = 11.sp, color = TextGray)
                                    Text("Warranty Policy: ${resultData.warrantyInfoText}", fontSize = 11.sp, color = TextGray)
                                }
                                else -> { // AI Summary
                                    Text(
                                        text = resultData.aiRecommendation.ifBlank { "Verified product deal analyzed across official e-commerce platforms. Instant discount active on top merchant seller." },
                                        fontSize = 12.sp,
                                        color = TextWhite,
                                        lineHeight = 18.sp
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

// =========================================================
// PHASE 9D: AI REVIEW INTELLIGENCE ENGINE & ADVISOR
// =========================================================
@Composable
fun Phase9DReviewIntelligenceSection(
    resultData: ShoppingResult,
    onShowToast: (String) -> Unit
) {
    val reviewReport = remember(resultData) { com.example.engine.ReviewIntelligenceEngine.analyzeReviews(resultData) }
    if (!reviewReport.hasVerifiedReviews || reviewReport.totalVerifiedReviewsCount == 0) return

    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Section Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.RateReview,
                    contentDescription = "Review Intelligence",
                    tint = CrimsonLight,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "AI REVIEW INTELLIGENCE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = CrimsonLight,
                    letterSpacing = 1.2.sp
                )
            }

            Box(
                modifier = Modifier
                    .background(Color(0x1AFFFFFF), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = if (reviewReport.hasVerifiedReviews) "${reviewReport.totalVerifiedReviewsCount} VERIFIED REVIEWS" else "NO REVIEWS DETECTED",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )
            }
        }

        // 1. TRUST INDICATOR & BUYING VERDICT ROW
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular Trust Indicator
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Color(0x0CFFFFFF))
                    .border(BorderStroke(1.dp, Color(0x22FFFFFF)), CircleShape)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                val animatedTrust by animateIntAsState(
                    targetValue = reviewReport.trustScorePercent,
                    animationSpec = tween(1000, easing = EaseOutCubic),
                    label = "TrustAnim"
                )

                val color = when (reviewReport.trustLevel) {
                    com.example.engine.TrustConfidenceLevel.HIGH -> Color(0xFF2ECC71)
                    com.example.engine.TrustConfidenceLevel.MEDIUM -> Color(0xFF3498DB)
                    com.example.engine.TrustConfidenceLevel.LOW -> Color(0xFFFF9800)
                    com.example.engine.TrustConfidenceLevel.UNKNOWN -> Color(0xFF9E9E9E)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$animatedTrust%", fontSize = 20.sp, fontWeight = FontWeight.Black, color = color)
                    Text(
                        text = reviewReport.trustLevel.displayName,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Buying Verdict Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x0EFFFFFF))
                    .border(BorderStroke(1.2.dp, CrimsonRed.copy(alpha = 0.4f)), RoundedCornerShape(20.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .background(Brush.horizontalGradient(listOf(CrimsonRed, Color(0xFF8B0000))), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("AI RECOMMENDATION", fontSize = 8.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                    }

                    Text(
                        text = reviewReport.verdict.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )

                    Text(
                        text = reviewReport.verdict.description,
                        fontSize = 11.sp,
                        color = TextGray,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // 2. REVIEW SENTIMENT ANIMATED CHART
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x0AFFFFFF))
                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "SENTIMENT ANALYSIS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = TextGray,
                    letterSpacing = 1.sp
                )

                if (!reviewReport.hasVerifiedReviews) {
                    Text(
                        text = "Not Enough Verified Reviews.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9E9E9E)
                    )
                } else {
                    val posAnim by animateIntAsState(reviewReport.positivePercent, tween(1000), label = "PosAnim")
                    val neuAnim by animateIntAsState(reviewReport.neutralPercent, tween(1000), label = "NeuAnim")
                    val negAnim by animateIntAsState(reviewReport.negativePercent, tween(1000), label = "NegAnim")

                    // Stacked Sentiment Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(CircleShape)
                            .background(Color(0x1AFFFFFF))
                    ) {
                        if (posAnim > 0) {
                            Box(
                                modifier = Modifier
                                    .weight(posAnim.toFloat())
                                    .fillMaxHeight()
                                    .background(Color(0xFF2ECC71))
                            )
                        }
                        if (neuAnim > 0) {
                            Box(
                                modifier = Modifier
                                    .weight(neuAnim.toFloat())
                                    .fillMaxHeight()
                                    .background(Color(0xFFFFB74D))
                            )
                        }
                        if (negAnim > 0) {
                            Box(
                                modifier = Modifier
                                    .weight(negAnim.toFloat())
                                    .fillMaxHeight()
                                    .background(Color(0xFFE74C3C))
                            )
                        }
                    }

                    // Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.size(8.dp).background(Color(0xFF2ECC71), CircleShape))
                            Text("Positive $posAnim%", fontSize = 11.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.size(8.dp).background(Color(0xFFFFB74D), CircleShape))
                            Text("Neutral $neuAnim%", fontSize = 11.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.size(8.dp).background(Color(0xFFE74C3C), CircleShape))
                            Text("Negative $negAnim%", fontSize = 11.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 3. AI REVIEW SUMMARY & HIGHLIGHTS
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x0AFFFFFF))
                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "AI REVIEW SUMMARY",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = TextGray,
                    letterSpacing = 1.sp
                )

                Text(
                    text = reviewReport.overallSummary,
                    fontSize = 12.sp,
                    color = TextWhite,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.Medium
                )

                HorizontalDivider(color = Color(0x10FFFFFF), thickness = 1.dp)

                // Loved Features
                if (reviewReport.mostLovedFeatures.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.ThumbUp, contentDescription = null, tint = Color(0xFF2ECC71), modifier = Modifier.size(14.dp))
                            Text("MOST LOVED FEATURES", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFF2ECC71))
                        }
                        reviewReport.mostLovedFeatures.forEach { loved ->
                            Text("✔ $loved", fontSize = 11.sp, color = TextWhite)
                        }
                    }
                }

                // Common Complaints
                if (reviewReport.mostCommonComplaints.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.ThumbDown, contentDescription = null, tint = Color(0xFFE74C3C), modifier = Modifier.size(14.dp))
                            Text("MOST COMMON COMPLAINTS", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFE74C3C))
                        }
                        reviewReport.mostCommonComplaints.forEach { complaint ->
                            Text("✖ $complaint", fontSize = 11.sp, color = TextWhite)
                        }
                    }
                }
            }
        }

        // 4. PROS & CONS SWIPE CARDS
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            // Pros Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x152ECC71))
                    .border(BorderStroke(1.dp, Color(0xFF2ECC71).copy(alpha = 0.3f)), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("PROS", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFF2ECC71), letterSpacing = 1.sp)
                    if (reviewReport.pros.isEmpty()) {
                        Text("No specific pros listed", fontSize = 11.sp, color = TextGray)
                    } else {
                        reviewReport.pros.forEach { pro ->
                            Text("+ $pro", fontSize = 11.sp, color = TextWhite, lineHeight = 15.sp)
                        }
                    }
                }
            }

            // Cons Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x15E74C3C))
                    .border(BorderStroke(1.dp, Color(0xFFE74C3C).copy(alpha = 0.3f)), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("CONS", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFE74C3C), letterSpacing = 1.sp)
                    if (reviewReport.cons.isEmpty()) {
                        Text("No major cons noted", fontSize = 11.sp, color = TextGray)
                    } else {
                        reviewReport.cons.forEach { con ->
                            Text("- $con", fontSize = 11.sp, color = TextWhite, lineHeight = 15.sp)
                        }
                    }
                }
            }
        }

        // 5. PHASE 9D QUICK ACTIONS (Apple-Style Buttons)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x0FFFFFFF))
                .border(BorderStroke(1.dp, Color(0x1EFFFFFF)), RoundedCornerShape(20.dp))
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "REPORT ACTIONS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = TextGray,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Open Product
                    Button(
                        onClick = {
                            try {
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(resultData.url))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                onShowToast("✖ Could not open link")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    ) {
                        Text("Open", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }

                    // Share Report
                    Button(
                        onClick = {
                            val shareText = "🛒 ${resultData.productName}\n⭐ Rating: ${resultData.rating}/5.0\n💡 Verdict: ${reviewReport.verdict.title}\n🔗 ${resultData.url}"
                            val intent = android.content.Intent().apply {
                                action = android.content.Intent.ACTION_SEND
                                putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(android.content.Intent.createChooser(intent, "Share AI Report"))
                            onShowToast("✔ Report ready to share")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x22FFFFFF)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
                    ) {
                        Text("Share", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }

                    // Copy AI Summary
                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            val clip = android.content.ClipData.newPlainText("AI Review Summary", reviewReport.overallSummary)
                            clipboard.setPrimaryClip(clip)
                            onShowToast("✔ AI Summary Copied!")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x22FFFFFF)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    ) {
                        Text("Copy AI", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}
