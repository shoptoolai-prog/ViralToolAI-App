package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.core.LanguageEngine
import com.example.creatoracademy.CreatorAiGeneratorEngine
import com.example.creatoracademy.CreatorSetupData
import com.example.reports.ReportLanguage
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite

/**
 * MASTER PHASE 15B.1 — Context-Aware AI Tools & Future Ready Link Analysis
 * Completely screenshot-free tools inside Creator Academy:
 * 1. Caption Generator
 * 2. Hashtag Generator
 * 3. Hook Generator
 * 4. Content Planner
 * 5. Posting Checklist
 * 6. Brand Pitch Guide
 *
 * Future Ready:
 * - Profile Link Analysis
 * - Channel Link Analysis
 * - API Integrations
 */

@Composable
fun AiCreatorToolsSection(
    setupData: CreatorSetupData,
    onOpenTool: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "AI CREATOR TOOLKIT",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite.copy(alpha = 0.5f),
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        val tools = listOf(
            Triple("Caption Generator", "Craft high-converting viral captions with CTAs", Icons.Default.FormatQuote),
            Triple("Hashtag Generator", "Generate 3-tier niche SEO hashtag stacks", Icons.Default.Tag),
            Triple("Hook Generator", "5 scroll-stopping 3-second viral hooks", Icons.Default.TrendingUp),
            Triple("Content Planner", "7-day customized posting schedule & ideas", Icons.Default.CalendarMonth),
            Triple("Posting Checklist", "Interactive pre-publishing quality checklist", Icons.Default.Checklist),
            Triple("Brand Pitch Guide", "Sponsorship & brand deal outreach templates", Icons.Default.Email)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            tools.forEach { (name, desc, icon) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x0CFFFFFF))
                        .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(16.dp))
                        .clickable { onOpenTool(name) }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0x2210B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = name,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = desc,
                                fontSize = 11.sp,
                                color = TextWhite.copy(alpha = 0.6f)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(EmeraldPrimary.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Use AI Tool",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FutureReadySection(
    onOpenLinkAnalysis: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "FUTURE READY INTEGRATIONS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite.copy(alpha = 0.5f),
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x0CFFFFFF))
                    .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(16.dp))
                    .clickable { onOpenLinkAnalysis("PROFILE_LINK") }
                    .padding(14.dp)
            ) {
                Column {
                    Text(text = "🔗 Profile Link", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "Analyze via instagram.com/handle", fontSize = 10.5.sp, color = TextWhite.copy(alpha = 0.5f))
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x0CFFFFFF))
                    .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(16.dp))
                    .clickable { onOpenLinkAnalysis("CHANNEL_LINK") }
                    .padding(14.dp)
            ) {
                Column {
                    Text(text = "▶️ Channel Link", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "Analyze via youtube.com/@channel", fontSize = 10.5.sp, color = TextWhite.copy(alpha = 0.5f))
                }
            }
        }
    }
}

// ====================================================================
// INTERACTIVE TOOL DIALOGS
// ====================================================================

@Composable
fun CaptionGeneratorDialog(setupData: CreatorSetupData, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val currentLang = LanguageEngine.currentLanguageState.value
    var topicText by remember { mutableStateOf("") }
    var generatedCaption by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF141420),
            border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp).verticalScroll(rememberScrollState())) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "✍️ Caption Generator", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextWhite.copy(alpha = 0.6f), modifier = Modifier.clickable { onDismiss() })
                }
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = topicText,
                    onValueChange = { topicText = it },
                    label = { Text("Content Topic (e.g. ${setupData.niche} tips)", color = TextWhite.copy(alpha = 0.6f)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary, unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedTextColor = TextWhite, unfocusedTextColor = TextWhite
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(EmeraldPrimary)
                        .clickable {
                            generatedCaption = CreatorAiGeneratorEngine.generateCaption(
                                context = context,
                                setupData = setupData,
                                topic = topicText,
                                lang = currentLang
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Generate Caption ✨", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                }

                if (generatedCaption != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x10FFFFFF))
                            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(text = generatedCaption!!, fontSize = 12.sp, color = TextWhite, lineHeight = 17.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(EmeraldPrimary)
                                    .clickable {
                                        clipboardManager.setText(AnnotatedString(generatedCaption!!))
                                        Toast.makeText(context, "Copied to Clipboard!", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = AmoledBlack, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Copy Caption", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HashtagGeneratorDialog(setupData: CreatorSetupData, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val currentLang = LanguageEngine.currentLanguageState.value
    var hashtagResult by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF141420),
            border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "#️⃣ 3-Tier Hashtag Stack", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextWhite.copy(alpha = 0.6f), modifier = Modifier.clickable { onDismiss() })
                }
                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Niche: ${setupData.niche} • Platform: ${setupData.targetPlatform}", fontSize = 12.sp, color = EmeraldPrimary)

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(EmeraldPrimary)
                        .clickable {
                            hashtagResult = CreatorAiGeneratorEngine.generateHashtags(
                                context = context,
                                setupData = setupData,
                                lang = currentLang
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Generate Hashtags 🚀", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                }

                if (hashtagResult != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x10FFFFFF))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(text = hashtagResult!!, fontSize = 11.5.sp, color = TextWhite, lineHeight = 16.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(EmeraldPrimary)
                                    .clickable {
                                        clipboardManager.setText(AnnotatedString(hashtagResult!!))
                                        Toast.makeText(context, "Hashtag stack copied!", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = AmoledBlack, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Copy Stack", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HookGeneratorDialog(setupData: CreatorSetupData, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val currentLang = LanguageEngine.currentLanguageState.value
    var topicText by remember { mutableStateOf("") }
    var hooks by remember { mutableStateOf<List<String>>(emptyList()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF141420),
            border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp).verticalScroll(rememberScrollState())) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🎯 5 Viral 3-Sec Hooks", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextWhite.copy(alpha = 0.6f), modifier = Modifier.clickable { onDismiss() })
                }
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = topicText,
                    onValueChange = { topicText = it },
                    label = { Text("Topic Focus (Optional)", color = TextWhite.copy(alpha = 0.6f)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary, unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedTextColor = TextWhite, unfocusedTextColor = TextWhite
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(EmeraldPrimary)
                        .clickable {
                            hooks = CreatorAiGeneratorEngine.generateViralHooks(
                                context = context,
                                setupData = setupData,
                                topic = topicText,
                                lang = currentLang
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Generate Fresh Viral Hooks 🔥", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                }

                if (hooks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    hooks.forEach { h ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x10FFFFFF))
                                .padding(10.dp)
                        ) {
                            Text(text = h, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextWhite)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContentPlannerDialog(setupData: CreatorSetupData, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val currentLang = LanguageEngine.currentLanguageState.value
    var plan by remember {
        mutableStateOf(
            CreatorAiGeneratorEngine.generateContentPlan(
                context = context,
                setupData = setupData,
                lang = currentLang
            )
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF141420),
            border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp).verticalScroll(rememberScrollState())) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📅 7-Day Content Plan", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextWhite.copy(alpha = 0.6f), modifier = Modifier.clickable { onDismiss() })
                }
                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Target Frequency: ${setupData.postingFrequency} • Time: ${setupData.availableTime}", fontSize = 11.5.sp, color = EmeraldPrimary)

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(EmeraldPrimary)
                        .clickable {
                            plan = CreatorAiGeneratorEngine.generateContentPlan(
                                context = context,
                                setupData = setupData,
                                lang = currentLang
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Regenerate Plan ✨", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                }

                Spacer(modifier = Modifier.height(12.dp))

                plan.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x10FFFFFF))
                            .padding(10.dp)
                    ) {
                        Text(text = item, fontSize = 12.sp, color = TextWhite)
                    }
                }
            }
        }
    }
}

@Composable
fun PostingChecklistDialog(onDismiss: () -> Unit) {
    val items = remember {
        mutableStateListOf(
            "Lighting & Clear Audio Verified" to true,
            "3-Second Curiosity Hook in Top 30% Screen" to false,
            "On-screen Captions for Silent Viewers" to false,
            "Clear Call To Action (Save / Comment / Share)" to false,
            "3-5 Hyper-Targeted Hashtags Added" to false,
            "High-Contrast Cover Frame Selected" to false,
            "Trending Audio Track Arrow verified ↗️" to false
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF141420),
            border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp).verticalScroll(rememberScrollState())) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "✅ Pre-Publishing Checklist", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextWhite.copy(alpha = 0.6f), modifier = Modifier.clickable { onDismiss() })
                }
                Spacer(modifier = Modifier.height(12.dp))

                items.forEachIndexed { idx, pair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { items[idx] = pair.first to !pair.second }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = pair.second,
                            onCheckedChange = { items[idx] = pair.first to it },
                            colors = CheckboxDefaults.colors(checkedColor = EmeraldPrimary, uncheckedColor = Color(0x44FFFFFF))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = pair.first,
                            fontSize = 12.5.sp,
                            color = if (pair.second) EmeraldPrimary else TextWhite
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BrandPitchGuideDialog(setupData: CreatorSetupData, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val currentLang = LanguageEngine.currentLanguageState.value

    val pitchText = remember(setupData, currentLang) {
        CreatorAiGeneratorEngine.generateBrandPitch(
            context = context,
            setupData = setupData,
            lang = currentLang
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF141420),
            border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp).verticalScroll(rememberScrollState())) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📩 Brand Pitch Template", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextWhite.copy(alpha = 0.6f), modifier = Modifier.clickable { onDismiss() })
                }
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x10FFFFFF))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(text = pitchText, fontSize = 11.5.sp, color = TextWhite, lineHeight = 16.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(EmeraldPrimary)
                                .clickable {
                                    clipboardManager.setText(AnnotatedString(pitchText))
                                    Toast.makeText(context, "Pitch template copied!", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = AmoledBlack, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Copy Pitch Email", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LinkAnalysisDialog(type: String, onDismiss: () -> Unit) {
    val context = LocalContext.current
    var urlInput by remember { mutableStateOf("") }

    val title = if (type == "PROFILE_LINK") "🔗 Profile Link Analysis" else "▶️ Channel Link Analysis"
    val placeholder = if (type == "PROFILE_LINK") "https://instagram.com/username" else "https://youtube.com/@channel"

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF141420),
            border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextWhite.copy(alpha = 0.6f), modifier = Modifier.clickable { onDismiss() })
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Future-ready URL analysis. Enter direct profile link below:",
                    fontSize = 11.5.sp,
                    color = TextWhite.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = urlInput,
                    onValueChange = { urlInput = it },
                    placeholder = { Text(placeholder, fontSize = 12.sp, color = TextWhite.copy(alpha = 0.4f)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary, unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedTextColor = TextWhite, unfocusedTextColor = TextWhite
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(EmeraldPrimary)
                        .clickable {
                            if (urlInput.isBlank()) {
                                Toast.makeText(context, "Please enter a valid link!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "⚡ Link Analysis Queue Ready for API Sync!", Toast.LENGTH_SHORT).show()
                                onDismiss()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Analyze Direct Link 🚀", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AmoledBlack)
                }
            }
        }
    }
}
