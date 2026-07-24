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
import com.example.data.ContentKitStorageManager
import com.example.data.ShoppingResult
import com.example.engine.AiCreatorStudioEngine
import com.example.engine.CreatorCommerceEngine
import com.example.engine.VoiceoverTone
import com.example.ui.theme.*

/**
 * SHOPTOOLAI Master Phase 10B — AI Creator Studio Section
 * Premium Apple-inspired Creator Studio featuring Reel Script Builder, Voiceover Studio (7 Tones),
 * Caption Engine (5 Platforms), Hashtag Engine (6 Categories), Hook Generator, CTA Lab,
 * Content Score Animated Rings, and Local Save Kit.
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Phase10BCreatorStudioSection(
    resultData: ShoppingResult,
    onShowToast: (String) -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(true) }

    val studioKit = remember(resultData) { AiCreatorStudioEngine.generateStudioKit(resultData) }
    val commerceKit = remember(resultData) { CreatorCommerceEngine.generateContentKit(resultData) }

    var selectedVoiceoverTone by remember { mutableStateOf(VoiceoverTone.FRIENDLY) }
    var selectedCaptionPlatform by remember { mutableStateOf("Instagram") }
    var selectedHashtagCategory by remember { mutableStateOf("Trending") }
    var isKitSaved by remember(commerceKit.productUrl) { mutableStateOf(ContentKitStorageManager.isKitSaved(commerceKit.productUrl)) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0x22FF1744),
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

            // HEADER BAR: AI CREATOR STUDIO
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
                            imageVector = Icons.Default.MovieFilter,
                            contentDescription = "Studio AI",
                            tint = TextWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "AI CREATOR STUDIO",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = CrimsonLight,
                                letterSpacing = 1.2.sp
                            )
                            Box(
                                modifier = Modifier
                                    .background(Brush.horizontalGradient(listOf(CrimsonRed, Color(0xFF8B0000))), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "PRO STUDIO",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                            }
                        }
                        Text(
                            text = "Reel Script, Voiceover, Captions & Viral Score",
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

                    // 1. CONTENT SCORE METERS (Quality, Hook, Audience, Retention)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Analytics, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                                Text("CONTENT POTENTIAL SCORE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .background(Color(0x22FFFFFF), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("AI Estimate", fontSize = 8.sp, color = TextGray)
                            }
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Quality Meter
                                    ScoreCircleMeter("Quality", studioKit.contentScore.overallQuality, Color(0xFF2ECC71))
                                    // Hook Meter
                                    ScoreCircleMeter("Hook", studioKit.contentScore.hookStrength, CrimsonRed)
                                    // Audience Meter
                                    ScoreCircleMeter("Audience", studioKit.contentScore.audiencePotential, Color(0xFF3498DB))
                                    // Retention Meter
                                    ScoreCircleMeter("Retention", studioKit.contentScore.retentionEstimate, Color(0xFFFFB74D))
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0x12FFFFFF), RoundedCornerShape(10.dp))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "Verdict: ${studioKit.contentScore.recommendationLabel}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CrimsonLight,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }

                    // 2. REEL SCRIPT BUILDER
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                                Text("REEL SCRIPT BUILDER", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                            }

                            Button(
                                onClick = {
                                    val fullScript = studioKit.scriptSections.joinToString("\n\n") { "${it.title} (${it.timeline})\nVisual: ${it.visualGuidance}\nAudio: \"${it.spokenLine}\"" }
                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                    val clip = android.content.ClipData.newPlainText("Full Reel Script", fullScript)
                                    clipboard.setPrimaryClip(clip)
                                    onShowToast("✔ Full Reel Script Copied!")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x22FFFFFF)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Copy Full Script", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            }
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
                                studioKit.scriptSections.forEachIndexed { index, sec ->
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(sec.title, fontSize = 11.sp, fontWeight = FontWeight.Black, color = CrimsonLight)
                                            Box(
                                                modifier = Modifier
                                                    .background(Color(0x22FFFFFF), RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                                            ) {
                                                Text(sec.timeline, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                            }
                                        }

                                        Text("📹 ${sec.visualGuidance}", fontSize = 10.sp, color = TextGray)
                                        Text("🎙️ \"${sec.spokenLine}\"", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                                    }

                                    if (index < studioKit.scriptSections.size - 1) {
                                        HorizontalDivider(color = Color(0x0CFFFFFF), thickness = 1.dp)
                                    }
                                }
                            }
                        }
                    }

                    // 3. VOICEOVER STUDIO (7 TONES)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                                Text("VOICEOVER STUDIO", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                            }

                            Button(
                                onClick = {
                                    val text = studioKit.voiceoverDrafts[selectedVoiceoverTone] ?: ""
                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                    val clip = android.content.ClipData.newPlainText("Voiceover Script", text)
                                    clipboard.setPrimaryClip(clip)
                                    onShowToast("✔ ${selectedVoiceoverTone.displayName} Voiceover Copied!")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x22FFFFFF)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Copy Audio Script", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            }
                        }

                        // Tone Chips
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            contentPadding = PaddingValues(end = 8.dp)
                        ) {
                            items(studioKit.voiceoverTones, key = { it.name }) { tone ->
                                val isSelected = selectedVoiceoverTone == tone
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) CrimsonRed else Color(0x12FFFFFF))
                                        .clickable { selectedVoiceoverTone = tone }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = "${tone.displayName} (${tone.badge})",
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal,
                                        color = TextWhite
                                    )
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
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "TONE: ${selectedVoiceoverTone.displayName.uppercase()}",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CrimsonLight
                                )
                                Text(
                                    text = studioKit.voiceoverDrafts[selectedVoiceoverTone] ?: "",
                                    fontSize = 11.sp,
                                    color = TextWhite,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    // 4. HOOK GENERATOR CARDS
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                            Text("HOOK GENERATOR", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                        }

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(studioKit.hooks, key = { it.headline }) { hook ->
                                Box(
                                    modifier = Modifier
                                        .width(200.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0x12FFFFFF))
                                        .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                                        .clickable {
                                            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                            val clip = android.content.ClipData.newPlainText("Hook", hook.headline)
                                            clipboard.setPrimaryClip(clip)
                                            onShowToast("✔ Hook Copied!")
                                        }
                                        .padding(12.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(hook.category, fontSize = 9.sp, fontWeight = FontWeight.Black, color = CrimsonLight)
                                            Text(hook.deliveryStyle, fontSize = 8.sp, color = TextGray)
                                        }
                                        Text(
                                            text = "\"${hook.headline}\"",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 5. CTA LAB CARDS
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                            Text("CTA LAB", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                        }

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(studioKit.ctas, key = { it.actionText }) { cta ->
                                Box(
                                    modifier = Modifier
                                        .width(210.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0x12FFFFFF))
                                        .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                                        .clickable {
                                            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                            val clip = android.content.ClipData.newPlainText("CTA", cta.actionText)
                                            clipboard.setPrimaryClip(clip)
                                            onShowToast("✔ CTA Copied!")
                                        }
                                        .padding(12.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(cta.platform, fontSize = 9.sp, fontWeight = FontWeight.Black, color = CrimsonLight)
                                        Text("\"${cta.actionText}\"", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text("Goal: ${cta.goal}", fontSize = 9.sp, color = TextGray)
                                    }
                                }
                            }
                        }
                    }

                    // 6. CAPTION ENGINE (5 PLATFORMS)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Subtitles, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                                Text("CAPTION ENGINE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                            }

                            Button(
                                onClick = {
                                    val cap = studioKit.captions[selectedCaptionPlatform] ?: ""
                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                    val clip = android.content.ClipData.newPlainText("Caption", cap)
                                    clipboard.setPrimaryClip(clip)
                                    onShowToast("✔ $selectedCaptionPlatform Caption Copied!")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x22FFFFFF)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Copy Caption", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            }
                        }

                        // Caption Platform Switcher
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Instagram", "Facebook", "Threads", "YouTube", "Pinterest").forEach { platform ->
                                val isSelected = selectedCaptionPlatform == platform
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) CrimsonRed else Color(0x12FFFFFF))
                                        .clickable { selectedCaptionPlatform = platform }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(platform, fontSize = 9.sp, fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal, color = TextWhite)
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
                                text = studioKit.captions[selectedCaptionPlatform] ?: "",
                                fontSize = 11.sp,
                                color = TextWhite,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // 7. HASHTAG ENGINE (6 CATEGORIES)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Tag, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                                Text("HASHTAG ENGINE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                            }

                            Button(
                                onClick = {
                                    val tags = studioKit.hashtags[selectedHashtagCategory]?.joinToString(" ") ?: ""
                                    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                    val clip = android.content.ClipData.newPlainText("Hashtags", tags)
                                    clipboard.setPrimaryClip(clip)
                                    onShowToast("✔ $selectedHashtagCategory Hashtags Copied!")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x22FFFFFF)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Copy Tags", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            }
                        }

                        // Hashtag Category Switcher
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Trending", "Niche", "Brand", "Shopping", "Review", "Local").forEach { cat ->
                                val isSelected = selectedHashtagCategory == cat
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) CrimsonRed else Color(0x12FFFFFF))
                                        .clickable { selectedHashtagCategory = cat }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(cat, fontSize = 9.sp, fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal, color = TextWhite)
                                }
                            }
                        }

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            studioKit.hashtags[selectedHashtagCategory]?.forEach { tag ->
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

                    // 8. POSTING TIPS
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                            Text("POSTING TIPS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite, letterSpacing = 1.sp)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x0CFFFFFF))
                                .border(BorderStroke(1.dp, Color(0x18FFFFFF)), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                studioKit.postingTips.forEach { tip ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Text(tip.topic, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                            Text(tip.advice, fontSize = 10.sp, color = TextGray)
                                        }
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0x22FFFFFF), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(tip.bestTime, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = CrimsonLight)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 9. SAVE CONTENT KIT BUTTON
                    Button(
                        onClick = {
                            val saved = ContentKitStorageManager.toggleSaveKit(commerceKit)
                            isKitSaved = saved
                            if (saved) {
                                onShowToast("✔ Full Studio Kit Saved Locally!")
                            } else {
                                onShowToast("Removed Studio Kit from Saved")
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
                                text = if (isKitSaved) "FULL STUDIO KIT SAVED" else "SAVE FULL CREATOR STUDIO KIT LOCALLY",
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

@Composable
private fun ScoreCircleMeter(label: String, score: Int, color: Color) {
    val animatedScore by animateIntAsState(
        targetValue = score,
        animationSpec = tween(1000, easing = EaseOutCubic),
        label = "ScoreAnim"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color(0x1A000000))
                .border(BorderStroke(2.dp, color), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("$animatedScore%", fontSize = 13.sp, fontWeight = FontWeight.Black, color = TextWhite)
        }
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextGray)
    }
}
