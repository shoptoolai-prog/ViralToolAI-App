package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PriceTrendPoint
import com.example.engine.PriceTrackerEngine
import com.example.ui.theme.CrimsonLight
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite

@Composable
fun ApplePriceGraphComponent(
    url: String,
    productName: String,
    currentPrice: Double,
    rawPriceTrend: List<PriceTrendPoint>,
    accentColor: Color = CrimsonLight,
    onShowToast: (String) -> Unit = {}
) {
    var selectedTimeframeDays by remember { mutableIntStateOf(90) }
    var alertTargetPriceText by remember(currentPrice) { mutableStateOf(String.format("%.0f", currentPrice * 0.95)) }
    var activeAlert by remember(url) { mutableStateOf(PriceTrackerEngine.getAlert(url)) }

    val filteredPoints = remember(rawPriceTrend, selectedTimeframeDays) {
        PriceTrackerEngine.getFilteredPriceHistory(rawPriceTrend, selectedTimeframeDays)
    }

    val stats = remember(filteredPoints, currentPrice) {
        PriceTrackerEngine.calculateStats(filteredPoints, currentPrice)
    }

    val trendColor = when (stats.trendDirection) {
        "Trending Down" -> Color(0xFF2ECC71)
        "Trending Up" -> Color(0xFFE74C3C)
        else -> Color(0xFFF1C40F)
    }

    val darkCanvasBg = Color(0xFF0F0F13)

    Column(modifier = Modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PRICE HISTORY ENGINE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray,
                letterSpacing = 1.2.sp
            )

            // 30D / 90D / 180D Timeframe selector
            if (rawPriceTrend.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x14FFFFFF))
                        .padding(2.dp)
                ) {
                    listOf(30 to "30D", 90 to "90D", 180 to "180D").forEach { (days, label) ->
                        val isSelected = selectedTimeframeDays == days
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) accentColor else Color.Transparent)
                                .pointerInput(Unit) {
                                    detectTapGestures { selectedTimeframeDays = days }
                                }
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else TextGray
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Metrics Summary Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            @Composable
            fun PriceMetricCard(
                label: String,
                value: String,
                isHighlight: Boolean = false,
                highlightColor: Color = Color.Unspecified,
                modifier: Modifier = Modifier
            ) {
                Box(
                    modifier = modifier
                        .background(Color(0x0AFFFFFF), RoundedCornerShape(12.dp))
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
                            color = if (isHighlight) highlightColor else TextWhite
                        )
                    }
                }
            }

            PriceMetricCard("LOWEST", "₹${String.format("%,.0f", stats.lowestPrice)}", isHighlight = true, highlightColor = Color(0xFF2ECC71), modifier = Modifier.weight(1f))
            PriceMetricCard("HIGHEST", "₹${String.format("%,.0f", stats.highestPrice)}", modifier = Modifier.weight(1f))
            PriceMetricCard("AVERAGE", "₹${String.format("%,.0f", stats.averagePrice)}", modifier = Modifier.weight(1f))
            PriceMetricCard("TREND", stats.trendDirection, isHighlight = true, highlightColor = trendColor, modifier = Modifier.weight(1.2f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // GRAPH OR NO-DATA STATE
        if (filteredPoints.size >= 2) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x06FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x12FFFFFF)), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(
                                imageVector = if (stats.trendDirection == "Trending Down") Icons.Default.TrendingDown else Icons.Default.TrendingUp,
                                contentDescription = "Trend Icon",
                                tint = trendColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Verified Price History ($selectedTimeframeDays Days)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = trendColor
                            )
                        }
                        Text(
                            text = "${filteredPoints.size} Datapoints",
                            fontSize = 10.sp,
                            color = TextGray
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Apple-style Canvas Bezier Curve
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
                                .height(120.dp)
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
                                val y = height - (yNorm * (height * 0.8f) + height * 0.1f)
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

                                // Gradient fill
                                drawPath(
                                    path = fillPath,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(accentColor.copy(alpha = 0.35f), accentColor.copy(alpha = 0.02f))
                                    )
                                )

                                // Main stroke
                                drawPath(
                                    path = path,
                                    color = accentColor,
                                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                                )

                                // Highlight point on touch
                                if (touchX >= 0f && selectedPoint != null) {
                                    val activeIndex = filteredPoints.indexOf(selectedPoint).coerceIn(0, pointsOffsets.size - 1)
                                    val activeOffset = pointsOffsets[activeIndex]

                                    drawLine(
                                        color = Color.White.copy(alpha = 0.4f),
                                        start = Offset(activeOffset.x, 0f),
                                        end = Offset(activeOffset.x, height),
                                        strokeWidth = 1.dp.toPx()
                                    )

                                    drawCircle(
                                        color = accentColor,
                                        radius = 6.dp.toPx(),
                                        center = activeOffset
                                    )
                                    drawCircle(
                                        color = Color.White,
                                        radius = 3.dp.toPx(),
                                        center = activeOffset
                                    )
                                }
                            }
                        }

                        // Floating tooltip on touch
                        selectedPoint?.let { pt ->
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(darkCanvasBg)
                                    .border(BorderStroke(1.dp, accentColor), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${pt.date}: ₹${String.format("%,.0f", pt.price)}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                        }
                    }

                    // Date range labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = filteredPoints.firstOrNull()?.date ?: "", fontSize = 9.sp, color = TextGray)
                        Text(text = "Drag or tap graph for details", fontSize = 9.sp, color = TextGray)
                        Text(text = filteredPoints.lastOrNull()?.date ?: "", fontSize = 9.sp, color = TextGray)
                    }
                }
            }
        } else {
            // NO FAKE DATA STATE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x06FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x12FFFFFF)), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Price Tracker Active",
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "PRICE TRACKER ACTIVE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Live price snapshot recorded (₹${String.format("%,.0f", currentPrice)}). Graph will render automatically as additional historical price points are logged.",
                            fontSize = 11.sp,
                            color = TextGray,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // PRICE DROP ALERT SECTION
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x0AFFFFFF))
                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Price Alert",
                            tint = Color(0xFFFFB74D),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "PRICE DROP ALERT",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 1.sp
                        )
                    }

                    if (activeAlert != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x224CAF50))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("ALERT ACTIVE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (activeAlert != null) {
                    // Alert Active View
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x0CFFFFFF))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Notify when price drops to or below:",
                                fontSize = 11.sp,
                                color = TextGray
                            )
                            Text(
                                text = "₹${String.format("%,.0f", activeAlert!!.targetPrice)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFFB74D)
                            )
                        }

                        Button(
                            onClick = {
                                PriceTrackerEngine.removePriceAlert(url)
                                activeAlert = null
                                onShowToast("Price alert cancelled")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x22FF5252)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cancel Alert", fontSize = 11.sp, color = Color(0xFFFF5252))
                        }
                    }
                } else {
                    // Set Alert Controls
                    Text(
                        text = "Set a target price to receive a price drop notification when the price drops.",
                        fontSize = 11.sp,
                        color = TextGray,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = alertTargetPriceText,
                            onValueChange = { alertTargetPriceText = it },
                            label = { Text("Target Price (₹)", fontSize = 10.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFFB74D),
                                unfocusedBorderColor = Color(0x22FFFFFF),
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )

                        Button(
                            onClick = {
                                val parsedTarget = alertTargetPriceText.toDoubleOrNull()
                                if (parsedTarget != null && parsedTarget > 0.0) {
                                    val alert = PriceTrackerEngine.setPriceAlert(url, productName, currentPrice, parsedTarget)
                                    activeAlert = alert
                                    onShowToast("Price alert set for ₹${String.format("%,.0f", parsedTarget)}!")
                                } else {
                                    onShowToast("Please enter a valid target price")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB74D)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Set Alert", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
