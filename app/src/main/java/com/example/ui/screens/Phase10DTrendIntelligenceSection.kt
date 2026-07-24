package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ShoppingResult
import com.example.data.TrendHistoryStorageManager
import com.example.engine.AiCreatorStudioEngine
import com.example.engine.TrendIntelligenceEngine
import com.example.engine.TrendMatchLevel
import com.example.ui.theme.*

/**
 * SHOPTOOLAI Master Phase 10D — AI Trend Intelligence Section
 * Features Trend Dashboard, Animated Ring Match Score (4 levels), Content Category AI,
 * Best Posting Time architecture placeholder ("Available after enough account insights."),
 * Content Improvement Swipe Cards, Competitor Analysis ("Coming Soon"), and Local Trend History.
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Phase10DTrendIntelligenceSection(
    resultData: ShoppingResult,
    onShowToast: (String) -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(true) }

    val report = remember(resultData) { TrendIntelligenceEngine.analyzeTrends(resultData) }
    val studioKit = remember(resultData) { AiCreatorStudioEngine.generateStudioKit(resultData) }

    // Save to local Trend History on load
    LaunchedEffect(resultData) {
        TrendHistoryStorageManager.recordTrendReport(
            url = resultData.url,
            name = resultData.productName,
            store = resultData.detectedStore,
            category = report.classifiedCategory,
            trendScore = report.trendMatchScore,
            creatorScore = studioKit.contentScore.overallQuality
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0x221E88E5),
                        Color(0x0DFFFFFF)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.2.dp,
                    Brush.horizontalGradient(listOf(Color(0xFF1E88E5), Color(0x33FFFFFF)))
                ),
                RoundedCornerShape(24.dp)
            )
            .animateContentSize(animationSpec = tween(350, easing = EaseOutCubic))
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // HEADER BAR: AI TREND INTELLIGENCE
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                Brush.radialGradient(listOf(Color(0xFF1E88E5), Color(0xFF0D47A1))),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Trend AI",
                            tint = TextWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "AI TREND INTELLIGENCE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF64B5F6),
                                letterSpacing = 1.2.sp
                            )
                            Box(
                                modifier = Modifier
                                    .background(Brush.horizontalGradient(listOf(Color(0xFF1E88E5), Color(0xFF0D47A1))), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "LIVE TRENDS",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                            }
                        }
                        Text(
                            text = "Match Score, Category AI & Content Improvement",
                            fontSize = 11.sp,
                            color = TextGray
                        )
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = TextWhite,
                    modifier = Modifier.size(22.dp)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {

                    HorizontalDivider(color = Color(0x15FFFFFF), thickness = 1.dp)

                    // 1. TREND MATCH SCORE & DASHBOARD METRICS
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Speed, contentDescription = null, tint = Color(0xFF64B5F6), modifier = Modifier.size(16.dp))
                                Text("TREND MATCH DASHBOARD", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .background(Color(0x22FFFFFF), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("AI Estimate", fontSize = 8.sp, color = TextGray)
                            }
                        }

                        // Main Dashboard Card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0x0CFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(18.dp))
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Circular Match Score Ring
                                TrendScoreRing(score = report.trendMatchScore, level = report.matchLevel)

                                Spacer(modifier = Modifier.width(16.dp))

                                // Dashboard Cards List
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    MetricRow(label = "Current Status", value = report.currentContentStatus, valueColor = TextWhite)
                                    MetricRow(label = "Category AI", value = report.classifiedCategory, valueColor = CrimsonLight)
                                    MetricRow(label = "Audience Potential", value = report.audiencePotential, valueColor = Color(0xFF64B5F6))
                                    MetricRow(label = "Growth Opportunity", value = report.growthOpportunity, valueColor = Color(0xFF2ECC71))
                                }
                            }
                        }
                    }

                    // 2. BEST POSTING TIME ARCHITECTURE
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = Color(0xFF64B5F6), modifier = Modifier.size(16.dp))
                            Text("BEST POSTING TIME ANALYSIS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x0CFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(16.dp))
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFFFFB74D),
                                    modifier = Modifier.size(20.dp)
                                )
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = report.postingTimeAnalysisMessage,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = "Connect Instagram Insights or YouTube Analytics in Phase 10E for real peak engagement data.",
                                        fontSize = 10.sp,
                                        color = TextGray
                                    )
                                }
                            }
                        }
                    }

                    // 3. CONTENT IMPROVEMENT SWIPE CARDS
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = Color(0xFF64B5F6), modifier = Modifier.size(16.dp))
                            Text("CONTENT IMPROVEMENT CARDS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                        }

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(report.improvements, key = { it.title }) { item ->
                                Box(
                                    modifier = Modifier
                                        .width(230.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(Color(0x12FFFFFF))
                                        .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(18.dp))
                                        .padding(14.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = item.title,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color(0xFF64B5F6)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .background(Color(0x22FFFFFF), RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text("Tip", fontSize = 8.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                                            }
                                        }

                                        Text(
                                            text = item.recommendation,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextWhite,
                                            lineHeight = 15.sp
                                        )

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0x18FFFFFF), RoundedCornerShape(8.dp))
                                                .padding(8.dp)
                                        ) {
                                            Text(
                                                text = "💡 ${item.actionTip}",
                                                fontSize = 9.sp,
                                                color = CrimsonLight,
                                                lineHeight = 13.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 4. COMPETITOR ANALYSIS (COMING SOON)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Group, contentDescription = null, tint = Color(0xFF64B5F6), modifier = Modifier.size(16.dp))
                            Text("COMPETITOR ANALYSIS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x0CFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(16.dp))
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "AI Competitor Benchmarking",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = "Niche competitor engagement comparison engine.",
                                        fontSize = 10.sp,
                                        color = TextGray
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .background(Brush.horizontalGradient(listOf(CrimsonRed, Color(0xFF8B0000))), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = report.competitorStatus,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TextWhite
                                    )
                                }
                            }
                        }
                    }

                    // 5. LOCAL TREND HISTORY
                    val trendHistory = remember { TrendHistoryStorageManager.getHistory() }
                    if (trendHistory.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.History, contentDescription = null, tint = Color(0xFF64B5F6), modifier = Modifier.size(16.dp))
                                    Text("SAVED TREND HISTORY", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                                }
                                Text("${trendHistory.size} Entries", fontSize = 9.sp, color = TextGray)
                            }

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(trendHistory, key = { it.id }) { entry ->
                                    Box(
                                        modifier = Modifier
                                            .width(200.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color(0x12FFFFFF))
                                            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                                            .padding(12.dp)
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(entry.contentCategory, fontSize = 9.sp, fontWeight = FontWeight.Black, color = CrimsonLight)
                                                Text(entry.dateSaved, fontSize = 8.sp, color = TextGray)
                                            }
                                            Text(
                                                text = entry.productName,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextWhite,
                                                maxLines = 1
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("Trend Score: ${entry.trendScore}%", fontSize = 9.sp, color = Color(0xFF64B5F6), fontWeight = FontWeight.Bold)
                                                Text("Store: ${entry.store}", fontSize = 8.sp, color = TextGray)
                                            }
                                        }
                                    }
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
private fun TrendScoreRing(score: Int, level: TrendMatchLevel) {
    val animatedScore by animateIntAsState(
        targetValue = score,
        animationSpec = tween(1200, easing = EaseOutCubic),
        label = "TrendRingScore"
    )

    val color = Color(level.badgeColorHex)

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0x1A000000))
                .border(BorderStroke(3.dp, Brush.sweepGradient(listOf(color, Color.White, color))), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$animatedScore%", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextWhite)
                Text("MATCH", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = TextGray)
            }
        }

        Box(
            modifier = Modifier
                .background(color.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                .border(BorderStroke(1.dp, color), RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = level.displayName.uppercase(),
                fontSize = 8.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
    }
}

@Composable
private fun MetricRow(label: String, value: String, valueColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
        Text(label, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextGray)
        Text(value, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = valueColor, maxLines = 1)
    }
}
