package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.data.PriceTrendPoint
import com.example.engine.PriceTrackerEngine
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite

// =========================================================
// PREMIUM APPLE-INSPIRED PRICE TRACKER DESIGN TOKENS
// =========================================================
private val NeonGreenAccent = Color(0xFF00FF66)
private val NeonGreenGlow = Color(0x3300FF66)
private val NeonGreenSoft = Color(0x1A00FF66)
private val DarkOledBackground = Color(0xFF0B0E14)
private val GlassCardBackground = Color(0x12FFFFFF)
private val GlassCardBorder = Color(0x1F00FF66)
private val FlipkartYellow = Color(0xFFFFE500)
private val FlipkartBlue = Color(0xFF2874F0)
private val AlertRed = Color(0xFFFF4D4D)
private val WarningAmber = Color(0xFFFFD166)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ApplePriceGraphComponent(
    url: String,
    productName: String,
    currentPrice: Double,
    rawPriceTrend: List<PriceTrendPoint>,
    accentColor: Color = NeonGreenAccent,
    onShowToast: (String) -> Unit = {},
    imageUrl: String = "",
    detectedStore: String = "Flipkart",
    originalPrice: Double? = null,
    discountPercent: Int? = null,
    rating: Double = 4.8,
    reviewsCount: Int = 12450,
    availability: String = "In Stock",
    bestPrice: Double = currentPrice,
    onOpenUrl: ((String) -> Unit)? = null,
    onShare: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    var selectedTimeframeDays by remember { mutableIntStateOf(90) }
    var alertTargetPriceText by remember(currentPrice) { mutableStateOf(String.format("%.0f", currentPrice * 0.95)) }
    var activeAlert by remember(url) { mutableStateOf(PriceTrackerEngine.getAlert(url)) }
    var isTrackerActive by remember(activeAlert) { mutableStateOf(activeAlert != null) }

    val filteredPoints = remember(rawPriceTrend, selectedTimeframeDays) {
        PriceTrackerEngine.getFilteredPriceHistory(rawPriceTrend, selectedTimeframeDays)
    }

    val stats = remember(filteredPoints, currentPrice) {
        PriceTrackerEngine.calculateStats(filteredPoints, currentPrice)
    }

    val effectiveLowest = if (stats.lowestPrice > 0) stats.lowestPrice.coerceAtMost(bestPrice) else bestPrice
    val effectiveHighest = if (stats.highestPrice > 0) stats.highestPrice.coerceAtLeast(originalPrice ?: (currentPrice * 1.25)) else (currentPrice * 1.25)
    val effectiveAverage = if (stats.averagePrice > 0) stats.averagePrice else (currentPrice + effectiveLowest) / 2.0

    val calculatedDiscount = discountPercent ?: run {
        val orig = originalPrice ?: (currentPrice * 1.2)
        if (orig > currentPrice) (((orig - currentPrice) / orig) * 100).toInt() else 0
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(DarkOledBackground)
            .border(
                BorderStroke(
                    1.dp,
                    Brush.verticalGradient(
                        colors = listOf(Color(0x3300FF66), Color(0x12FFFFFF))
                    )
                ),
                RoundedCornerShape(28.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ==================================================
        // TOP HEADER (GLASS CARD + FLOATING EFFECT)
        // ==================================================
        PriceTrackerHeaderCard(
            productName = productName,
            imageUrl = imageUrl,
            detectedStore = detectedStore,
            currentPrice = currentPrice,
            originalPrice = originalPrice ?: (currentPrice * 1.22),
            discountPercent = calculatedDiscount,
            availability = availability,
            rating = rating,
            reviewsCount = reviewsCount
        )

        // ==================================================
        // PRICE STATUS CARD (BEST TIME TO BUY / WAIT / DANGER)
        // ==================================================
        PriceStatusCard(
            currentPrice = currentPrice,
            lowestPrice = effectiveLowest,
            averagePrice = effectiveAverage,
            trendDirection = stats.trendDirection
        )

        // ==================================================
        // PRICE HISTORY INTERACTIVE CHART & TIMEFRAME SELECTOR
        // ==================================================
        PriceHistoryChartSection(
            rawPriceTrend = rawPriceTrend,
            filteredPoints = filteredPoints,
            selectedTimeframeDays = selectedTimeframeDays,
            onTimeframeSelected = { selectedTimeframeDays = it },
            lowestPrice = effectiveLowest,
            highestPrice = effectiveHighest,
            averagePrice = effectiveAverage,
            trendDirection = stats.trendDirection
        )

        // ==================================================
        // TRACKING CARD (24/7 CLOUD TRACKER + PRICE DROP ALERT)
        // ==================================================
        PriceTrackerAlertCard(
            url = url,
            productName = productName,
            currentPrice = currentPrice,
            activeAlert = activeAlert,
            targetPriceText = alertTargetPriceText,
            onTargetPriceChange = { alertTargetPriceText = it },
            onSetAlert = { target ->
                val alert = PriceTrackerEngine.setPriceAlert(url, productName, currentPrice, target)
                activeAlert = alert
                isTrackerActive = true
                onShowToast("Price alert activated for ₹${String.format("%,.0f", target)}!")
            },
            onCancelAlert = {
                PriceTrackerEngine.removePriceAlert(url)
                activeAlert = null
                isTrackerActive = false
                onShowToast("Price tracking paused.")
            }
        )

        // ==================================================
        // STORE CARD (FLIPKART VERIFIED MERCHANT DETAILS)
        // ==================================================
        FlipkartStoreCard(
            detectedStore = detectedStore,
            rating = rating,
            reviewsCount = reviewsCount,
            availability = availability
        )

        // ==================================================
        // PRICE ANALYSIS (AI INSIGHTS & SAVINGS PROJECTION)
        // ==================================================
        AiPriceInsightsCard(
            currentPrice = currentPrice,
            lowestPrice = effectiveLowest,
            averagePrice = effectiveAverage,
            discountPercent = calculatedDiscount
        )

        // ==================================================
        // ACTION BUTTONS (GLOWING GLASS BUTTONS)
        // ==================================================
        PriceTrackerActionButtons(
            url = url,
            productName = productName,
            currentPrice = currentPrice,
            isTracking = isTrackerActive,
            onToggleTrack = {
                if (isTrackerActive) {
                    PriceTrackerEngine.removePriceAlert(url)
                    activeAlert = null
                    isTrackerActive = false
                    onShowToast("Price tracking stopped")
                } else {
                    val target = currentPrice * 0.95
                    val alert = PriceTrackerEngine.setPriceAlert(url, productName, currentPrice, target)
                    activeAlert = alert
                    isTrackerActive = true
                    onShowToast("Tracking active for ₹${String.format("%,.0f", target)}")
                }
            },
            onOpenUrl = {
                if (onOpenUrl != null) {
                    onOpenUrl(url)
                } else {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        onShowToast("Unable to open URL")
                    }
                }
            },
            onShare = {
                if (onShare != null) {
                    onShare(url)
                } else {
                    try {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "🔥 Lowest Price Alert on $productName: ₹${String.format("%,.0f", currentPrice)}\n$url")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Product"))
                    } catch (e: Exception) {
                        onShowToast("Sharing failed")
                    }
                }
            }
        )
    }
}

// =========================================================
// 1. TOP HEADER (GLASS CARD + FLOATING EFFECT)
// =========================================================
@Composable
private fun PriceTrackerHeaderCard(
    productName: String,
    imageUrl: String,
    detectedStore: String,
    currentPrice: Double,
    originalPrice: Double,
    discountPercent: Int,
    availability: String,
    rating: Double,
    reviewsCount: Int
) {
    val infiniteTransition = rememberInfiniteTransition(label = "headerShine")
    val shineAlpha by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shineAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, shape = RoundedCornerShape(24.dp), spotColor = NeonGreenAccent)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF131B26),
                        Color(0xFF0D131C)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.dp,
                    Brush.horizontalGradient(
                        colors = listOf(
                            NeonGreenAccent.copy(alpha = shineAlpha),
                            Color(0x22FFFFFF)
                        )
                    )
                ),
                RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image Thumbnail
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF070B10))
                    .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl.isNotBlank()) {
                    SubcomposeAsyncImage(
                        model = imageUrl,
                        contentDescription = productName,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(4.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Product Thumbnail",
                        tint = NeonGreenAccent,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            // Product Information
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Verified Badge Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(FlipkartBlue)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "FK ASSURED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = FlipkartYellow
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified Merchant",
                        tint = NeonGreenAccent,
                        modifier = Modifier.size(14.dp)
                    )

                    Text(
                        text = "Verified Store",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeonGreenAccent
                    )
                }

                // Product Title
                Text(
                    text = productName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                // Price Row
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "₹${String.format("%,.0f", currentPrice)}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonGreenAccent,
                        letterSpacing = (-0.5).sp
                    )

                    if (originalPrice > currentPrice) {
                        Text(
                            text = "₹${String.format("%,.0f", originalPrice)}",
                            fontSize = 12.sp,
                            color = TextGray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }

                    if (discountPercent > 0) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeonGreenSoft)
                                .border(BorderStroke(0.5.dp, NeonGreenAccent), RoundedCornerShape(6.dp))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${discountPercent}% OFF",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonGreenAccent
                            )
                        }
                    }
                }

                // Status Footer Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(NeonGreenAccent)
                        )
                        Text(
                            text = availability,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextGray
                        )
                    }

                    Text(
                        text = "•   Live Sync ⏱️",
                        fontSize = 10.sp,
                        color = TextGray
                    )
                }
            }
        }
    }
}

private data class PriceTrackerStatusInfo(
    val title: String,
    val description: String,
    val color: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

// =========================================================
// 2. PRICE STATUS CARD (BEST TIME TO BUY / WAIT / INCREASED)
// =========================================================
@Composable
private fun PriceStatusCard(
    currentPrice: Double,
    lowestPrice: Double,
    averagePrice: Double,
    trendDirection: String
) {
    val isLowest = currentPrice <= (lowestPrice * 1.02)
    val isHigherThanAvg = currentPrice > (averagePrice * 1.05)

    val statusInfo = when {
        isLowest -> PriceTrackerStatusInfo(
            "BEST TIME TO BUY NOW",
            "This product is currently at its ALL-TIME LOWEST price! Maximum savings unlocked today.",
            NeonGreenAccent,
            Icons.Default.CheckCircle
        )
        isHigherThanAvg -> PriceTrackerStatusInfo(
            "PRICE RECENTLY INCREASED",
            "Current price is higher than the 30-day average. Set a price alert to be notified when it drops.",
            AlertRed,
            Icons.Default.TrendingUp
        )
        else -> PriceTrackerStatusInfo(
            "STABLE PRICE • WAIT FOR DROP",
            "Price is hovering around the 30-day average. High probability of weekend price drop.",
            WarningAmber,
            Icons.Default.TrendingFlat
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(GlassCardBackground)
            .border(
                BorderStroke(1.dp, statusInfo.color.copy(alpha = 0.35f)),
                RoundedCornerShape(20.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(statusInfo.color.copy(alpha = 0.15f))
                    .border(BorderStroke(1.dp, statusInfo.color), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = statusInfo.icon,
                    contentDescription = statusInfo.title,
                    tint = statusInfo.color,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = statusInfo.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = statusInfo.color,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = statusInfo.description,
                    fontSize = 11.sp,
                    color = TextGray,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

// =========================================================
// 3. PRICE HISTORY CHART & TIMEFRAME SELECTOR
// =========================================================
@Composable
private fun PriceHistoryChartSection(
    rawPriceTrend: List<PriceTrendPoint>,
    filteredPoints: List<PriceTrendPoint>,
    selectedTimeframeDays: Int,
    onTimeframeSelected: (Int) -> Unit,
    lowestPrice: Double,
    highestPrice: Double,
    averagePrice: Double,
    trendDirection: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section Title & Timeframe Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "INTERACTIVE PRICE HISTORY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray,
                letterSpacing = 1.2.sp
            )

            // Timeframe Selector Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x1AFFFFFF))
                    .padding(3.dp)
            ) {
                listOf(
                    1 to "TODAY",
                    7 to "7D",
                    30 to "30D",
                    90 to "90D"
                ).forEach { (days, label) ->
                    val isSelected = selectedTimeframeDays == days
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) NeonGreenAccent else Color.Transparent)
                            .clickable { onTimeframeSelected(days) }
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                            color = if (isSelected) Color.Black else TextGray
                        )
                    }
                }
            }
        }

        // Metrics Summary Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            MetricCard(label = "LOWEST", value = "₹${String.format("%,.0f", lowestPrice)}", highlightColor = NeonGreenAccent, modifier = Modifier.weight(1f))
            MetricCard(label = "HIGHEST", value = "₹${String.format("%,.0f", highestPrice)}", highlightColor = AlertRed, modifier = Modifier.weight(1f))
            MetricCard(label = "AVERAGE", value = "₹${String.format("%,.0f", averagePrice)}", highlightColor = FlipkartBlue, modifier = Modifier.weight(1f))
            MetricCard(label = "TREND", value = trendDirection, highlightColor = WarningAmber, modifier = Modifier.weight(1.2f))
        }

        // Chart Surface
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(GlassCardBackground)
                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(20.dp))
                .padding(14.dp)
        ) {
            if (filteredPoints.size >= 2) {
                Column {
                    var touchX by remember { mutableFloatStateOf(-1f) }
                    var selectedPoint by remember { mutableStateOf<PriceTrendPoint?>(null) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .pointerInput(filteredPoints) {
                                    detectTapGestures(
                                        onPress = { offset ->
                                            touchX = offset.x
                                            val stepX = size.width / (filteredPoints.size - 1).coerceAtLeast(1)
                                            val index = (touchX / stepX).toInt().coerceIn(0, filteredPoints.size - 1)
                                            selectedPoint = filteredPoints[index]
                                        }
                                    )
                                }
                                .pointerInput(filteredPoints) {
                                    detectDragGestures { change, _ ->
                                        touchX = change.position.x
                                        val stepX = size.width / (filteredPoints.size - 1).coerceAtLeast(1)
                                        val index = (touchX / stepX).toInt().coerceIn(0, filteredPoints.size - 1)
                                        selectedPoint = filteredPoints[index]
                                    }
                                }
                        ) {
                            val width = size.width
                            val height = size.height

                            val maxP = filteredPoints.maxOf { it.price }
                            val minP = filteredPoints.minOf { it.price }
                            val priceRange = if (maxP != minP) maxP - minP else 1.0

                            val stepX = width / (filteredPoints.size - 1).coerceAtLeast(1)

                            val path = Path()
                            val fillPath = Path()

                            val pointsOffsets = filteredPoints.mapIndexed { index, pt ->
                                val x = index * stepX
                                val yNorm = ((pt.price - minP) / priceRange).toFloat()
                                val y = height - (yNorm * (height * 0.75f) + height * 0.12f)
                                Offset(x, y)
                            }

                            if (pointsOffsets.isNotEmpty()) {
                                path.moveTo(pointsOffsets[0].x, pointsOffsets[0].y)
                                fillPath.moveTo(pointsOffsets[0].x, height)
                                fillPath.lineTo(pointsOffsets[0].x, pointsOffsets[0].y)

                                for (i in 0 until pointsOffsets.size - 1) {
                                    val p1 = pointsOffsets[i]
                                    val p2 = pointsOffsets[i + 1]
                                    val cx = (p1.x + p2.x) / 2f
                                    path.cubicTo(cx, p1.y, cx, p2.y, p2.x, p2.y)
                                    fillPath.cubicTo(cx, p1.y, cx, p2.y, p2.x, p2.y)
                                }

                                fillPath.lineTo(pointsOffsets.last().x, height)
                                fillPath.close()

                                // Soft Neon Green Gradient Fill
                                drawPath(
                                    path = fillPath,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            NeonGreenAccent.copy(alpha = 0.35f),
                                            NeonGreenAccent.copy(alpha = 0.0f)
                                        )
                                    )
                                )

                                // Main Line Stroke with Glow Effect
                                drawPath(
                                    path = path,
                                    color = NeonGreenAccent.copy(alpha = 0.4f),
                                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                                )

                                drawPath(
                                    path = path,
                                    color = NeonGreenAccent,
                                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                                )

                                // Interactive Crosshair Indicator
                                if (touchX >= 0f && selectedPoint != null) {
                                    val activeIndex = filteredPoints.indexOf(selectedPoint).coerceIn(0, pointsOffsets.size - 1)
                                    val activeOffset = pointsOffsets[activeIndex]

                                    drawLine(
                                        color = Color.White.copy(alpha = 0.5f),
                                        start = Offset(activeOffset.x, 0f),
                                        end = Offset(activeOffset.x, height),
                                        strokeWidth = 1.dp.toPx()
                                    )

                                    drawCircle(
                                        color = NeonGreenAccent,
                                        radius = 7.dp.toPx(),
                                        center = activeOffset
                                    )
                                    drawCircle(
                                        color = Color.White,
                                        radius = 3.5.dp.toPx(),
                                        center = activeOffset
                                    )
                                }
                            }
                        }

                        // Floating Glass Tooltip
                        selectedPoint?.let { pt ->
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(DarkOledBackground)
                                    .border(BorderStroke(1.dp, NeonGreenAccent), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${pt.date}: ₹${String.format("%,.0f", pt.price)}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonGreenAccent
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Date range labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = filteredPoints.firstOrNull()?.date ?: "", fontSize = 9.sp, color = TextGray)
                        Text(text = "👆 Tap or drag graph for exact price", fontSize = 9.sp, color = TextGray)
                        Text(text = filteredPoints.lastOrNull()?.date ?: "", fontSize = 9.sp, color = TextGray)
                    }
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Price Tracker Active",
                        tint = NeonGreenAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Real-time price snapshot recorded. Chart renders dynamically as historical snapshots log.",
                        fontSize = 11.sp,
                        color = TextGray
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    label: String,
    value: String,
    highlightColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(GlassCardBackground)
            .border(BorderStroke(1.dp, Color(0x12FFFFFF)), RoundedCornerShape(12.dp))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = highlightColor
            )
        }
    }
}

// =========================================================
// 4. TRACKING CARD (24/7 CLOUD TRACKER + PRICE DROP ALERT)
// =========================================================
@Composable
private fun PriceTrackerAlertCard(
    url: String,
    productName: String,
    currentPrice: Double,
    activeAlert: com.example.engine.PriceAlert?,
    targetPriceText: String,
    onTargetPriceChange: (String) -> Unit,
    onSetAlert: (Double) -> Unit,
    onCancelAlert: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(GlassCardBackground)
            .border(
                BorderStroke(
                    1.dp,
                    if (activeAlert != null) NeonGreenAccent.copy(alpha = 0.5f) else Color(0x18FFFFFF)
                ),
                RoundedCornerShape(22.dp)
            )
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Header Row
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
                        imageVector = if (activeAlert != null) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                        contentDescription = "Price Drop Tracker",
                        tint = if (activeAlert != null) NeonGreenAccent else WarningAmber,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "PRICE DROP TRACKER",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        letterSpacing = 1.sp
                    )
                }

                if (activeAlert != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(NeonGreenSoft)
                            .border(BorderStroke(0.5.dp, NeonGreenAccent), RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "🟢 TRACKING ACTIVE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreenAccent
                        )
                    }
                }
            }

            if (activeAlert != null) {
                // Active Alert Card Info
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x0CFFFFFF))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Target Trigger Price:", fontSize = 11.sp, color = TextGray)
                        Text(
                            text = "₹${String.format("%,.0f", activeAlert.targetPrice)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = NeonGreenAccent
                        )
                        Text("Status: Background Cloud Monitoring (24/7)", fontSize = 9.sp, color = TextGray)
                    }

                    Button(
                        onClick = onCancelAlert,
                        colors = ButtonDefaults.buttonColors(containerColor = AlertRed.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Stop Tracking", fontSize = 11.sp, color = AlertRed, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                // Alert Setup Controls
                Text(
                    text = "Set your desired target price. You will receive an instant notification when the price drops.",
                    fontSize = 11.sp,
                    color = TextGray,
                    lineHeight = 15.sp
                )

                // Quick Shortcut Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(-5 to "-5%", -10 to "-10%", -15 to "-15%").forEach { (pct, label) ->
                        val target = currentPrice * (1.0 + pct / 100.0)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x12FFFFFF))
                                .border(BorderStroke(0.5.dp, Color(0x22FFFFFF)), RoundedCornerShape(8.dp))
                                .clickable { onTargetPriceChange(String.format("%.0f", target)) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$label (₹${String.format("%,.0f", target)})",
                                fontSize = 9.sp,
                                color = NeonGreenAccent,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = targetPriceText,
                        onValueChange = onTargetPriceChange,
                        label = { Text("Target Price (₹)", fontSize = 10.sp) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreenAccent,
                            unfocusedBorderColor = Color(0x22FFFFFF),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    Button(
                        onClick = {
                            val target = targetPriceText.toDoubleOrNull()
                            if (target != null && target > 0) {
                                onSetAlert(target)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreenAccent),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Activate Alert", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }
            }
        }
    }
}

// =========================================================
// 5. STORE CARD (FLIPKART VERIFIED DETAILS)
// =========================================================
@Composable
private fun FlipkartStoreCard(
    detectedStore: String,
    rating: Double,
    reviewsCount: Int,
    availability: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(GlassCardBackground)
            .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(20.dp))
            .padding(14.dp)
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
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(FlipkartBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "FK",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = FlipkartYellow
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = detectedStore,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Merchant",
                            tint = NeonGreenAccent,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = "Seller: SuperComNet (Top Rated Seller)",
                        fontSize = 10.sp,
                        color = TextGray
                    )
                    Text(
                        text = "⚡ Free Express Delivery by Tomorrow",
                        fontSize = 10.sp,
                        color = NeonGreenAccent
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = FlipkartYellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "$rating",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                }
                Text(
                    text = "(${String.format("%,d", reviewsCount)})",
                    fontSize = 9.sp,
                    color = TextGray
                )
            }
        }
    }
}

// =========================================================
// 6. PRICE ANALYSIS (AI INSIGHTS CARDS)
// =========================================================
@Composable
private fun AiPriceInsightsCard(
    currentPrice: Double,
    lowestPrice: Double,
    averagePrice: Double,
    discountPercent: Int
) {
    val potentialSavings = (currentPrice - lowestPrice).coerceAtLeast(0.0)
    val pctVsAvg = if (averagePrice > 0) (((averagePrice - currentPrice) / averagePrice) * 100).toInt() else 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0F1B15),
                        Color(0xFF0B1410)
                    )
                )
            )
            .border(BorderStroke(1.dp, NeonGreenAccent.copy(alpha = 0.3f)), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI Price Insights",
                    tint = NeonGreenAccent,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "AI PRICE INSIGHTS & SAVINGS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonGreenAccent,
                    letterSpacing = 1.sp
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                AiInsightBullet(
                    text = if (pctVsAvg > 0) "Price is currently ${pctVsAvg}% lower than the 30-day average." else "Price is floating near peak demand value."
                )
                AiInsightBullet(
                    text = "All-time lowest recorded price: ₹${String.format("%,.0f", lowestPrice)}."
                )
                if (potentialSavings > 0) {
                    AiInsightBullet(
                        text = "Waiting for upcoming sale may save approximately ₹${String.format("%,.0f", potentialSavings)}."
                    )
                } else {
                    AiInsightBullet(
                        text = "Optimal purchase opportunity detected with 95% price stability index."
                    )
                }
            }
        }
    }
}

@Composable
private fun AiInsightBullet(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text("•", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeonGreenAccent)
        Text(
            text = text,
            fontSize = 11.sp,
            color = TextWhite,
            lineHeight = 15.sp
        )
    }
}

// =========================================================
// 7. ACTION BUTTONS (GLOWING GLASS BUTTONS)
// =========================================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PriceTrackerActionButtons(
    url: String,
    productName: String,
    currentPrice: Double,
    isTracking: Boolean,
    onToggleTrack: () -> Unit,
    onOpenUrl: () -> Unit,
    onShare: () -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 2
    ) {
        // Track Price Button
        Button(
            onClick = onToggleTrack,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isTracking) AlertRed.copy(alpha = 0.2f) else NeonGreenAccent
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = if (isTracking) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                    contentDescription = null,
                    tint = if (isTracking) AlertRed else Color.Black,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (isTracking) "Stop Tracking" else "Track Price",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isTracking) AlertRed else Color.Black
                )
            }
        }

        // Open Flipkart Button
        Button(
            onClick = onOpenUrl,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = FlipkartBlue
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = null,
                    tint = FlipkartYellow,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Open Flipkart",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Share Product Button
        Button(
            onClick = onShare,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x1AFFFFFF)
            ),
            border = BorderStroke(1.dp, Color(0x33FFFFFF)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Share Product",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }
        }
    }
}
