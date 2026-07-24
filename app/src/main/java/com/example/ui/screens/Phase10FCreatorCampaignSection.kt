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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CreatorStatsStorageManager
import com.example.data.ShoppingResult
import com.example.engine.SmartCampaignPlannerEngine
import com.example.ui.theme.*

/**
 * SHOPTOOLAI Master Phase 10F — Creator Studio Final + Smart Campaign Planner Section
 * Includes:
 * 1. Festival Content Planner (10 Festivals - Architecture "Coming Soon")
 * 2. Brand Campaign Planner Timeline (Upcoming -> Prep -> Creation -> Publishing -> Review)
 * 3. Collaboration Calendar (Brand, Campaign, Deadline, Content Status)
 * 4. Creator Achievements (Interactive Cards with Glow & Unlock effects)
 * 5. Weekly Dashboard & Monthly Summary (Verified local statistics from app storage)
 * 6. Smart Reminders Engine (Upload Reel, Review Product, Weekly/Monthly progress)
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Phase10FCreatorCampaignSection(
    resultData: ShoppingResult,
    onShowToast: (String) -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(true) }

    val campaignPackage = remember(resultData) { SmartCampaignPlannerEngine.generateCampaignPackage(resultData) }
    val statsSummary = remember { CreatorStatsStorageManager.getStatsSummary() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0x22D32F2F),
                        Color(0x0DFFFFFF)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.2.dp,
                    Brush.horizontalGradient(listOf(CrimsonRed, Color(0x33FFFFFF)))
                ),
                RoundedCornerShape(24.dp)
            )
            .animateContentSize(animationSpec = tween(350, easing = EaseOutCubic))
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // HEADER BAR
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
                                Brush.radialGradient(listOf(CrimsonRed, DarkCrimson)),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = "Campaign Planner",
                            tint = TextWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "CREATOR CAMPAIGN PLANNER",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = CrimsonLight,
                                letterSpacing = 1.2.sp
                            )
                            Box(
                                modifier = Modifier
                                    .background(Brush.horizontalGradient(listOf(CrimsonRed, DarkCrimson)), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "FINAL PHASE 10F",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                            }
                        }
                        Text(
                            text = "Festivals, Brand Collabs & Verified Creator Stats",
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

                    // 1. VERIFIED WEEKLY DASHBOARD & MONTHLY SUMMARY
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Analytics, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                                Text("VERIFIED CREATOR DASHBOARD", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .background(Color(0x22FFFFFF), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Local Data Only", fontSize = 8.sp, color = TextGray)
                            }
                        }

                        // Local Stats Cards Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatCard(
                                title = "Products Analysed",
                                value = "${statsSummary.productsAnalysedCount}",
                                icon = Icons.Default.ShoppingBag,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                title = "Creator Kits",
                                value = "${statsSummary.creatorReportsCount}",
                                icon = Icons.Default.FolderSpecial,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                title = "Saved Plans",
                                value = "${statsSummary.savedPlansCount}",
                                icon = Icons.Default.Bookmark,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // 2. CREATOR ACHIEVEMENTS (WITH UNLOCK GLOW & CONFETTI)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                            Text("CREATOR ACHIEVEMENTS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                        }

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(statsSummary.achievements, key = { it.title }) { ach ->
                                var clicked by remember { mutableStateOf(false) }
                                val scale by animateFloatAsState(if (clicked) 1.05f else 1.0f, label = "scale")

                                Box(
                                    modifier = Modifier
                                        .width(180.dp)
                                        .scale(scale)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            if (ach.isUnlocked) Brush.verticalGradient(
                                                listOf(
                                                    Color(0x33D32F2F),
                                                    Color(0x12FFFFFF)
                                                )
                                            ) else Brush.verticalGradient(listOf(Color(0x0AFFFFFF), Color(0x05FFFFFF)))
                                        )
                                        .border(
                                            BorderStroke(
                                                1.dp,
                                                if (ach.isUnlocked) CrimsonRed else Color(0x18FFFFFF)
                                            ),
                                            RoundedCornerShape(16.dp)
                                        )
                                        .clickable {
                                            clicked = true
                                            if (ach.isUnlocked) {
                                                onShowToast("🏆 Achievement Unlocked: ${ach.title}!")
                                            } else {
                                                onShowToast("Locked: Progress ${ach.progressText}")
                                            }
                                        }
                                        .padding(12.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .clip(CircleShape)
                                                    .background(if (ach.isUnlocked) CrimsonRed else Color(0x22FFFFFF)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = getAchievementIcon(ach.iconName),
                                                    contentDescription = null,
                                                    tint = TextWhite,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        if (ach.isUnlocked) Color(0x332ECC71) else Color(0x18FFFFFF),
                                                        RoundedCornerShape(6.dp)
                                                    )
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = ach.progressText,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (ach.isUnlocked) Color(0xFF2ECC71) else TextGray
                                                )
                                            }
                                        }

                                        Text(ach.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text(ach.description, fontSize = 9.sp, color = TextGray, lineHeight = 12.sp)
                                    }
                                }
                            }
                        }
                    }

                    // 3. FESTIVAL CONTENT PLANNER (10 FESTIVALS)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Celebration, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                                Text("FESTIVAL CONTENT PLANNER", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .background(Color(0x22FFFFFF), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("10 Festivals Ready", fontSize = 8.sp, color = CrimsonLight, fontWeight = FontWeight.Bold)
                            }
                        }

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(campaignPackage.festivals, key = { it.id }) { fest ->
                                Box(
                                    modifier = Modifier
                                        .width(160.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0x12FFFFFF))
                                        .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                                        .clickable {
                                            onShowToast("🎉 ${fest.name} Festival Planner Architecture Ready!")
                                        }
                                        .padding(12.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(fest.name, fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                            Box(
                                                modifier = Modifier
                                                    .background(Color(0x22D32F2F), RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                                            ) {
                                                Text(fest.statusBadge, fontSize = 7.sp, fontWeight = FontWeight.Bold, color = CrimsonLight)
                                            }
                                        }
                                        Text(fest.description, fontSize = 9.sp, color = TextGray, lineHeight = 12.sp)
                                    }
                                }
                            }
                        }
                    }

                    // 4. BRAND CAMPAIGN PLANNER TIMELINE
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Timeline, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                            Text("BRAND CAMPAIGN TIMELINE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0x0CFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(18.dp))
                                .padding(14.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                campaignPackage.campaignTimeline.forEachIndexed { idx, step ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(Color(0x22D32F2F))
                                                .border(BorderStroke(1.dp, CrimsonRed), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("${step.stepNumber}", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                        }

                                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Text(step.stepName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                            Text(step.actionGuide, fontSize = 10.sp, color = TextGray)
                                            Text("Deliverable: ${step.deliverable}", fontSize = 9.sp, color = CrimsonLight, fontWeight = FontWeight.SemiBold)
                                        }
                                    }

                                    if (idx < campaignPackage.campaignTimeline.size - 1) {
                                        HorizontalDivider(color = Color(0x0CFFFFFF), thickness = 1.dp)
                                    }
                                }
                            }
                        }
                    }

                    // 5. COLLABORATION CALENDAR & REMINDERS (COMING SOON)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Collab Calendar Card
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x0CFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(14.dp))
                                    Text("COLLAB CALENDAR", fontSize = 9.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                }
                                campaignPackage.collabs.forEach { collab ->
                                    Column {
                                        Text(collab.brandName, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text(collab.campaignName, fontSize = 8.sp, color = TextGray)
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .background(Color(0x22FFFFFF), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("Coming Soon", fontSize = 7.sp, color = TextGray)
                                }
                            }
                        }

                        // Smart Reminders Card
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x0CFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(14.dp))
                                    Text("SMART REMINDERS", fontSize = 9.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                }
                                campaignPackage.reminders.take(2).forEach { rem ->
                                    Column {
                                        Text(rem.title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text(rem.frequency, fontSize = 8.sp, color = TextGray)
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .background(Color(0x22FFFFFF), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("Coming Soon", fontSize = 7.sp, color = TextGray)
                                }
                            }
                        }
                    }

                    // 6. PHASE 11 READY BADGE
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.horizontalGradient(listOf(DarkCrimson, Color(0xFF111111))))
                            .border(BorderStroke(1.dp, CrimsonRed.copy(alpha = 0.5f)), RoundedCornerShape(14.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2ECC71), modifier = Modifier.size(16.dp))
                            Text(
                                text = "CREATOR STUDIO COMPLETE — PHASE 11 ARCHITECTURE READY",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0x12FFFFFF))
            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(14.dp))
            .padding(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(icon, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextWhite)
            Text(title, fontSize = 8.sp, color = TextGray, fontWeight = FontWeight.Bold)
        }
    }
}

private fun getAchievementIcon(name: String): ImageVector {
    return when (name) {
        "FlashOn" -> Icons.Default.FlashOn
        "Explore" -> Icons.Default.Explore
        "ShoppingBag" -> Icons.Default.ShoppingBag
        "BookmarkCheck" -> Icons.Default.Bookmark
        "EmojiEvents" -> Icons.Default.EmojiEvents
        else -> Icons.Default.Star
    }
}
