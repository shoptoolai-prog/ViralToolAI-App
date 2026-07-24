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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ContentKitStorageManager
import com.example.data.ShoppingResult
import com.example.engine.CreatorCommerceEngine
import com.example.engine.CreatorCommerceKit
import com.example.ui.theme.*

/**
 * SHOPTOOLAI Master Phase 10A — Creator Commerce AI Hub Section
 * Interactive AI Creator Buying Advisor and Reel Content Kit Generator.
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Phase10ACreatorCommerceHubSection(
    resultData: ShoppingResult,
    onShowToast: (String) -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(true) }
    var activeCaptionTab by remember { mutableStateOf("Instagram") }
    var activeHashtagTab by remember { mutableStateOf("Trending") }

    val kit = remember(resultData) { CreatorCommerceEngine.generateContentKit(resultData) }
    var isKitSaved by remember(kit.productUrl) { mutableStateOf(ContentKitStorageManager.isKitSaved(kit.productUrl)) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0x18FF1744),
                        Color(0x0AFFFFFF)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.2.dp,
                    Brush.horizontalGradient(listOf(CrimsonRed.copy(alpha = 0.6f), Color(0x33FFFFFF)))
                ),
                RoundedCornerShape(24.dp)
            )
            .animateContentSize(animationSpec = tween(350, easing = EaseOutCubic))
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // HEADER BAR: CREATOR COMMERCE HUB
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
                                Brush.radialGradient(listOf(CrimsonRed, Color(0xFF8B0000))),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = "Creator AI",
                            tint = TextWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "CREATE CONTENT WITH THIS PRODUCT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = CrimsonLight,
                                letterSpacing = 1.1.sp
                            )
                            Box(
                                modifier = Modifier
                                    .background(CrimsonRed.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                                    .border(BorderStroke(1.dp, CrimsonRed.copy(alpha = 0.5f)), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "CREATOR AI",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                            }
                        }
                        Text(
                            text = "AI Reel Ideas, Shot List & Viral Kit",
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

                    // 1. VIRAL SCORE RING CARD
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x0EFFFFFF))
                            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Animated Ring
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x1A000000))
                                    .border(BorderStroke(2.dp, CrimsonRed), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                val scoreAnim by animateIntAsState(
                                    targetValue = kit.viralScore,
                                    animationSpec = tween(1200, easing = EaseOutCubic),
                                    label = "ViralScoreAnim"
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("$scoreAnim%", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                    Text("VIRAL", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = CrimsonLight)
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "${kit.viralLevel.uppercase()} CONTENT POTENTIAL",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF2ECC71)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0x22FFFFFF), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text("AI Estimate", fontSize = 8.sp, color = TextGray)
                                    }
                                }
                                Text(
                                    text = "High engagement search query in ${kit.productCategory} category with price point under ${kit.merchant}.",
                                    fontSize = 11.sp,
                                    color = TextGray,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }

                    // 2. REEL IDEA GENERATOR (HORIZONTAL SWIPE CARDS)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                                Text("REEL IDEAS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                            }
                            Text("${kit.reelIdeas.size} Concepts", fontSize = 10.sp, color = TextGray)
                        }

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(end = 8.dp)
                        ) {
                            items(kit.reelIdeas, key = { it.title }) { idea ->
                                Box(
                                    modifier = Modifier
                                        .width(220.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(Color(0x12FFFFFF))
                                        .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(18.dp))
                                        .clickable {
                                            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                            val clip = android.content.ClipData.newPlainText("Reel Idea", "${idea.title}\n\n${idea.concept}\n\nCTA: ${idea.callToAction}")
                                            clipboard.setPrimaryClip(clip)
                                            onShowToast("✔ Copied Reel Idea: ${idea.category}")
                                        }
                                        .padding(14.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .background(CrimsonRed.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text("AI Content Idea", fontSize = 8.sp, fontWeight = FontWeight.Black, color = CrimsonLight)
                                            }

                                            Text(idea.category, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        }

                                        Text(
                                            text = idea.title,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite,
                                            maxLines = 1
                                        )

                                        Text(
                                            text = idea.concept,
                                            fontSize = 11.sp,
                                            color = TextGray,
                                            lineHeight = 15.sp,
                                            maxLines = 3
                                        )

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0x10FFFFFF), RoundedCornerShape(8.dp))
                                                .padding(6.dp)
                                        ) {
                                            Text(
                                                text = "CTA: ${idea.callToAction}",
                                                fontSize = 9.sp,
                                                color = CrimsonLight,
                                                fontWeight = FontWeight.SemiBold,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 3. SHOT LIST TIMELINE
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Videocam, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                            Text("SHOT LIST TIMELINE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0x0CFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(18.dp))
                                .padding(14.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                kit.shotList.forEachIndexed { index, shot ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Shot Number Badge
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .background(
                                                    if (index == 0) CrimsonRed else Color(0x22FFFFFF),
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("${shot.stepNumber}", fontSize = 12.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                        }

                                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(shot.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                                Text("${shot.durationSeconds}s", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CrimsonLight)
                                            }
                                            Text(shot.description, fontSize = 10.sp, color = TextGray)
                                            Text("Angle: ${shot.cameraAngle}", fontSize = 9.sp, color = Color(0xAAFFFFFF))
                                        }
                                    }

                                    if (index < kit.shotList.size - 1) {
                                        HorizontalDivider(color = Color(0x0CFFFFFF), thickness = 1.dp)
                                    }
                                }
                            }
                        }
                    }

                    // 4. VOICEOVER STRUCTURE
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Mic, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                            Text("VOICEOVER STRUCTURE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0x0CFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(18.dp))
                                .padding(14.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                kit.voiceoverStructure.forEach { step ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0x22FFFFFF), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(step.section.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = step.guidance,
                                            fontSize = 11.sp,
                                            color = TextWhite,
                                            modifier = Modifier.weight(1f),
                                            lineHeight = 15.sp
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(step.duration, fontSize = 9.sp, color = TextGray, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // 5. CAPTION LAB
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Subtitles, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                                Text("CAPTION LAB", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                            }

                            // Copy Caption Button
                            Button(
                                onClick = {
                                    val textToCopy = kit.captionDrafts[activeCaptionTab] ?: ""
                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                    val clip = android.content.ClipData.newPlainText("Caption Draft", textToCopy)
                                    clipboard.setPrimaryClip(clip)
                                    onShowToast("✔ $activeCaptionTab Caption Copied!")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x22FFFFFF)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Copy Caption", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            }
                        }

                        // Caption Tabs
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Instagram", "YouTube", "Facebook", "Threads").forEach { tab ->
                                val isSelected = activeCaptionTab == tab
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) CrimsonRed else Color(0x12FFFFFF))
                                        .clickable { activeCaptionTab = tab }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(tab, fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal, color = TextWhite)
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x0CFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = kit.captionDrafts[activeCaptionTab] ?: "",
                                fontSize = 11.sp,
                                color = TextWhite,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // 6. HASHTAG LAB
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Tag, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                                Text("HASHTAG LAB", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                            }

                            // Copy Hashtags Button
                            Button(
                                onClick = {
                                    val tags = kit.hashtagSet[activeHashtagTab]?.joinToString(" ") ?: ""
                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                    val clip = android.content.ClipData.newPlainText("Hashtags", tags)
                                    clipboard.setPrimaryClip(clip)
                                    onShowToast("✔ $activeHashtagTab Hashtags Copied!")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x22FFFFFF)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Copy Tags", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            }
                        }

                        // Hashtag Category Tabs
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Trending", "Niche", "Product", "Brand", "Regional").forEach { cat ->
                                val isSelected = activeHashtagTab == cat
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) CrimsonRed else Color(0x12FFFFFF))
                                        .clickable { activeHashtagTab = cat }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(cat, fontSize = 9.sp, fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal, color = TextWhite)
                                }
                            }
                        }

                        // Hashtag Chips Flow
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            kit.hashtagSet[activeHashtagTab]?.forEach { tag ->
                                Box(
                                    modifier = Modifier
                                        .background(Color(0x18FFFFFF), RoundedCornerShape(8.dp))
                                        .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(tag, fontSize = 10.sp, color = CrimsonLight, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }

                    // 7. SAVE CONTENT KIT ACTION BUTTON
                    Button(
                        onClick = {
                            val saved = ContentKitStorageManager.toggleSaveKit(kit)
                            isKitSaved = saved
                            if (saved) {
                                onShowToast("✔ Saved Content Kit to Creator Hub!")
                            } else {
                                onShowToast("Removed Content Kit from Saved")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isKitSaved) Color(0xFF2ECC71) else CrimsonRed
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isKitSaved) Icons.Default.CheckCircle else Icons.Default.BookmarkAdd,
                                contentDescription = null,
                                tint = TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (isKitSaved) "CONTENT KIT SAVED TO HUB" else "SAVE ENTIRE CONTENT KIT LOCALLY",
                                fontSize = 11.sp,
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
