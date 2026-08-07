package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.creatoracademy.AiAudiencePersonaEngine
import com.example.creatoracademy.AiAudiencePersonaReport
import com.example.creatoracademy.AnalysedReel
import com.example.creatoracademy.AudienceCardData
import com.example.creatoracademy.AudienceEmotionData
import com.example.creatoracademy.BuyerIntentReason
import com.example.creatoracademy.ContentMatchData
import com.example.creatoracademy.PlatformMatchData
import com.example.creatoracademy.PlatformRating
import com.example.creatoracademy.RadarCategoryData
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.cos
import kotlin.math.sin

// ==================================================
// FULL DIALOG: DS-28 AI AUDIENCE PERSONA ENGINE
// ==================================================
@Composable
fun AiAudiencePersonaDialog(
    reel: AnalysedReel,
    onDismiss: () -> Unit
) {
    val report = remember(reel) {
        AiAudiencePersonaEngine.generatePersonaReport(reel)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AmoledBlack)
                .statusBarsPadding()
                .testTag("ai_audience_persona_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // HEADER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "👥 AI Audience Persona",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                            // Confidence Pill
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (report.isLowConfidence) Color(0x33EF4444) else Color(0x3322D3EE),
                                border = BorderStroke(1.dp, if (report.isLowConfidence) Color(0xFFEF4444) else CyanAccent)
                            ) {
                                Text(
                                    text = "${report.confidencePercent}% Confidence",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (report.isLowConfidence) Color(0xFFEF4444) else CyanAccent,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Know exactly who your reel is made for • ${report.reelTitle}",
                            fontSize = 11.5.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // SCROLLABLE BODY
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // SECTION 12 (CONFIDENCE NOTICE IF <70%)
                    if (report.isLowConfidence) {
                        LowConfidenceNoticeBanner()
                    }

                    // SECTION 11 — VIRI AI RECOMMENDATION
                    ViriRecommendationCard(recommendation = report.viriRecommendation)

                    // SECTION 1 — PRIMARY AUDIENCE
                    PrimaryAudienceSection(primary = report.primaryAudience)

                    // SECTION 2 — AUDIENCE CARDS
                    AudienceCardsSection(cards = report.audienceCards)

                    // SECTION 3 — INTEREST RADAR
                    InterestRadarSection(radarData = report.interestRadar)

                    // SECTION 4 — PLATFORM MATCH
                    PlatformMatchSection(platforms = report.platformMatches)

                    // SECTION 5 — BUYER INTENT
                    BuyerIntentSection(buyerIntent = report.buyerIntent)

                    // SECTION 6 — WATCH BEHAVIOUR
                    WatchBehaviourSection(watch = report.watchBehaviour)

                    // SECTION 7 — IDEAL POST TIME
                    IdealPostTimeSection(postTime = report.idealPostTime)

                    // SECTION 8 — CONTENT MATCH
                    ContentMatchSection(matches = report.contentMatches)

                    // SECTION 9 — AUDIENCE EMOTION
                    AudienceEmotionSection(emotions = report.audienceEmotions)

                    // SECTION 10 — IMPROVE TARGETING (3 HIGH IMPACT SUGGESTIONS)
                    ImproveTargetingSection(improvements = report.top3Improvements)

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = AmoledBlack)
                    ) {
                        Text(
                            text = "Done Exploring Audience Persona",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

// ==================================================
// SECTION 12 — LOW CONFIDENCE NOTICE
// ==================================================
@Composable
private fun LowConfidenceNoticeBanner() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color(0x26EF4444),
        border = BorderStroke(1.dp, Color(0xFFEF4444))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = Color(0xFFEF4444),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Audience prediction may be inaccurate because the reel does not contain enough visual or audio information.",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFCA5A5)
            )
        }
    }
}

// ==================================================
// SECTION 11 — VIRI AI RECOMMENDATION
// ==================================================
@Composable
private fun ViriRecommendationCard(recommendation: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFF131926),
        border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ViriMascotWidget(size = 38.dp)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Viri AI Audience Strategist",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanAccent
                )
                Text(
                    text = recommendation,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }
        }
    }
}

// ==================================================
// SECTION 1 — PRIMARY AUDIENCE
// ==================================================
@Composable
private fun PrimaryAudienceSection(primary: com.example.creatoracademy.PrimaryAudienceData) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "1. Primary Audience",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF161B26),
            border = BorderStroke(1.dp, Color(0xFF262E40))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = primary.primaryGroup,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanAccent
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x3322D3EE))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Age ${primary.ageRange}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    }
                }

                Divider(color = Color(0x1AFFFFFF))

                // Grid of audience attributes
                val items = listOf(
                    "Gender" to primary.genderDistribution,
                    "Language" to primary.language,
                    "Region" to primary.region,
                    "City Tier" to primary.cityTier,
                    "Shopping" to primary.shoppingBehaviour,
                    "Experience" to primary.experienceLevel
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowItems.forEach { (label, value) ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFF1E2638))
                                        .padding(10.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Text(
                                            text = label.uppercase(),
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextSecondary
                                        )
                                        Text(
                                            text = value,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
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
}

// ==================================================
// SECTION 2 — AUDIENCE CARDS
// ==================================================
@Composable
private fun AudienceCardsSection(cards: List<AudienceCardData>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "2. Audience Personas",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            cards.forEach { card ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF161B26),
                    border = BorderStroke(1.dp, Color(0xFF262E40))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = card.emoji, fontSize = 32.sp)

                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = card.title,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )

                                Text(
                                    text = "${card.likelihoodPercent}% Match",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (card.likelihoodPercent >= 75) EmeraldGlow else CyanAccent
                                )
                            }

                            if (card.reason.isNotBlank()) {
                                Text(
                                    text = card.reason,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            } else {
                                Text(
                                    text = "Interests: ${card.interests.joinToString(" • ")}",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Buying Power: ${card.buyingPower}",
                                        fontSize = 10.5.sp,
                                        color = CyanAccent,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Activity: ${card.activityLevel}",
                                        fontSize = 10.5.sp,
                                        color = TextSecondary
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

// ==================================================
// SECTION 3 — INTEREST RADAR CHART
// ==================================================
@Composable
private fun InterestRadarSection(radarData: List<RadarCategoryData>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "3. Audience Interest Radar",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF161B26),
            border = BorderStroke(1.dp, Color(0xFF262E40))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "AI Spider Web Interest Spectrum",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                // Canvas Radar Web
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val radius = (size.width / 2f) * 0.78f
                        val numCategories = radarData.size
                        val angleStep = (2 * Math.PI / numCategories).toFloat()

                        // Concentric Polygon Grid Lines (0.25, 0.5, 0.75, 1.0)
                        listOf(0.25f, 0.50f, 0.75f, 1.0f).forEach { scale ->
                            val gridPath = Path()
                            for (i in 0 until numCategories) {
                                val angle = i * angleStep - (Math.PI / 2).toFloat()
                                val x = center.x + radius * scale * cos(angle)
                                val y = center.y + radius * scale * sin(angle)
                                if (i == 0) gridPath.moveTo(x, y) else gridPath.lineTo(x, y)
                            }
                            gridPath.close()
                            drawPath(
                                path = gridPath,
                                color = Color(0xFF2A344A),
                                style = Stroke(width = 1.dp.toPx())
                            )
                        }

                        // Spokes from center
                        for (i in 0 until numCategories) {
                            val angle = i * angleStep - (Math.PI / 2).toFloat()
                            val endX = center.x + radius * cos(angle)
                            val endY = center.y + radius * sin(angle)
                            drawLine(
                                color = Color(0xFF2A344A),
                                start = center,
                                end = Offset(endX, endY),
                                strokeWidth = 1.dp.toPx()
                            )
                        }

                        // Active Radar Polygon
                        val dataPath = Path()
                        radarData.forEachIndexed { i, item ->
                            val angle = i * angleStep - (Math.PI / 2).toFloat()
                            val r = radius * item.value.coerceIn(0.1f, 1.0f)
                            val x = center.x + r * cos(angle)
                            val y = center.y + r * sin(angle)
                            if (i == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
                        }
                        dataPath.close()

                        // Fill
                        drawPath(
                            path = dataPath,
                            color = Color(0x6622D3EE)
                        )
                        // Stroke
                        drawPath(
                            path = dataPath,
                            color = CyanAccent,
                            style = Stroke(width = 2.5.dp.toPx())
                        )

                        // Data Points
                        radarData.forEachIndexed { i, item ->
                            val angle = i * angleStep - (Math.PI / 2).toFloat()
                            val r = radius * item.value.coerceIn(0.1f, 1.0f)
                            val x = center.x + r * cos(angle)
                            val y = center.y + r * sin(angle)
                            drawCircle(
                                color = if (item.value > 0.8f) EmeraldGlow else CyanAccent,
                                radius = 4.dp.toPx(),
                                center = Offset(x, y)
                            )
                        }
                    }
                }

                // Top Categories Tags Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    radarData.filter { it.value >= 0.75f }.take(4).forEach { item ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x3322D3EE))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "🔥 ${item.category} (${(item.value * 100).toInt()}%)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================================================
// SECTION 4 — PLATFORM MATCH
// ==================================================
@Composable
private fun PlatformMatchSection(platforms: List<PlatformMatchData>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "4. Platform Match",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            platforms.forEach { p ->
                val ratingColor = when (p.rating) {
                    PlatformRating.EXCELLENT -> EmeraldGlow
                    PlatformRating.GOOD -> CyanAccent
                    PlatformRating.AVERAGE -> Color(0xFFFFB703)
                    PlatformRating.WEAK -> Color(0xFFEF4444)
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF161B26),
                    border = BorderStroke(1.dp, Color(0xFF262E40))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = p.logoEmoji, fontSize = 24.sp)

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = p.platformName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(ratingColor.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = p.ratingText,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ratingColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = p.reason,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================================================
// SECTION 5 — BUYER INTENT
// ==================================================
@Composable
private fun BuyerIntentSection(buyerIntent: com.example.creatoracademy.BuyerIntentData) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "5. Buyer Intent Prediction",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF161B26),
            border = BorderStroke(1.dp, Color(0xFF262E40))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Overall Buyer Intent", fontSize = 12.sp, color = TextSecondary)
                        Text(
                            text = buyerIntent.overallLevel,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = EmeraldGlow
                        )
                    }

                    Text(
                        text = "${buyerIntent.score}/100 Score",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanAccent
                    )
                }

                Divider(color = Color(0x1AFFFFFF))

                buyerIntent.reasons.forEach { r ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = r.factor, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = "${r.score}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                        }

                        LinearProgressIndicator(
                            progress = { r.score / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (r.score >= 80) EmeraldGlow else CyanAccent,
                            trackColor = Color(0xFF1E293B)
                        )
                    }
                }
            }
        }
    }
}

// ==================================================
// SECTION 6 — WATCH BEHAVIOUR CIRCULAR METERS
// ==================================================
@Composable
private fun WatchBehaviourSection(watch: com.example.creatoracademy.WatchBehaviourData) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "6. Audience Watch Behaviour",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF161B26),
            border = BorderStroke(1.dp, Color(0xFF262E40))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Top Highlight: Avg Watch Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Predicted Avg Watch Time", fontSize = 12.sp, color = TextSecondary)
                        Text(
                            text = "${String.format("%.1f", watch.avgWatchTimeSec)}s / ${watch.totalLengthSec.toInt()}s Reel",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x3322D3EE))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${watch.watchTimePercent}% Retention",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    }
                }

                Divider(color = Color(0x1AFFFFFF))

                // Circular meters grid
                val meters = listOf(
                    "Replay" to watch.replayChancePercent,
                    "Share" to watch.shareChancePercent,
                    "Save" to watch.saveChancePercent,
                    "Comment" to watch.commentChancePercent,
                    "Profile Visit" to watch.profileVisitChancePercent,
                    "Follow" to watch.followChancePercent
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    meters.chunked(3).forEach { rowMeters ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            rowMeters.forEach { (label, value) ->
                                CircularMeterItem(label = label, value = value)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CircularMeterItem(label: String, value: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier.size(52.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 5.dp.toPx()
                drawCircle(
                    color = Color(0xFF1E293B),
                    style = Stroke(width = strokeWidth)
                )
                drawArc(
                    color = if (value >= 70) EmeraldGlow else CyanAccent,
                    startAngle = -90f,
                    sweepAngle = (value / 100f) * 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
            Text(
                text = "$value%",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Text(
            text = label,
            fontSize = 10.5.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

// ==================================================
// SECTION 7 — IDEAL POST TIME
// ==================================================
@Composable
private fun IdealPostTimeSection(postTime: com.example.creatoracademy.IdealPostTimeData) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "7. Ideal Post Time & Activity",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF161B26),
            border = BorderStroke(1.dp, Color(0xFF262E40))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Best Posting Windows", fontSize = 12.sp, color = TextSecondary)
                        Text(
                            text = postTime.bestTimeWindow,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGlow
                        )
                        Text(
                            text = "Best Days: ${postTime.bestDay}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF1E293B))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Avoid", fontSize = 9.5.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                            Text(text = postTime.worstTimeWindow, fontSize = 10.5.sp, color = TextSecondary)
                        }
                    }
                }

                Divider(color = Color(0x1AFFFFFF))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Active Audience: ${postTime.expectedActiveAudience}", fontSize = 11.5.sp, color = CyanAccent, fontWeight = FontWeight.SemiBold)
                    Text(text = "Competition: ${postTime.competitionLevel}", fontSize = 11.5.sp, color = TextSecondary)
                }
            }
        }
    }
}

// ==================================================
// SECTION 8 — CONTENT MATCH
// ==================================================
@Composable
private fun ContentMatchSection(matches: List<ContentMatchData>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "8. Reel Content Match",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF161B26),
            border = BorderStroke(1.dp, Color(0xFF262E40))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                matches.forEach { m ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = m.category, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Text(text = "${m.matchPercent}% Match", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                        }

                        LinearProgressIndicator(
                            progress = { m.matchPercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = CyanAccent,
                            trackColor = Color(0xFF1E293B)
                        )
                    }
                }
            }
        }
    }
}

// ==================================================
// SECTION 9 — AUDIENCE EMOTION
// ==================================================
@Composable
private fun AudienceEmotionSection(emotions: List<AudienceEmotionData>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "9. Audience Emotional Reaction",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(emotions) { e ->
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFF161B26),
                    border = BorderStroke(1.dp, Color(0xFF262E40))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${e.emoji} ${e.emotion}", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = "${e.scorePercent}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                        }

                        Text(
                            text = e.description,
                            fontSize = 10.5.sp,
                            color = TextSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

// ==================================================
// SECTION 10 — IMPROVE TARGETING (3 ONLY)
// ==================================================
@Composable
private fun ImproveTargetingSection(improvements: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "10. Top 3 Targeting Improvements",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            improvements.take(3).forEachIndexed { index, tip ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFF161B26),
                    border = BorderStroke(1.dp, Color(0xFF262E40))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(CyanAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmoledBlack
                            )
                        }

                        Text(
                            text = tip,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
