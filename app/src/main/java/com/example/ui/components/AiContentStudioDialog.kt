package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import com.example.ui.theme.responsiveImeAndNavPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.draw.shadow
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.ElectricPurple
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.creatoracademy.AiContentStudioEngine
import com.example.creatoracademy.ContentStudioResult
import com.example.creatoracademy.CreatorSetupData
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite

/**
 * MASTER PHASE 17 — AI Content Studio Dialog
 * Premium Apple-style Glass Screen/Dialog for planning viral video content before uploading.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiContentStudioDialog(
    setupData: CreatorSetupData,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val clipboard = LocalClipboardManager.current

    val userMemory = remember { AiContentStudioEngine.getUserMemory(context) }

    var activeTab by remember { mutableStateOf("STUDIO") } // "STUDIO" or "SAVED"

    // Form inputs
    var selectedPlatform by remember { mutableStateOf(userMemory.favoritePlatform) }
    var selectedContentType by remember { mutableStateOf("Product Review") }
    var topicInput by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }

    var generatedPlan by remember { mutableStateOf<ContentStudioResult?>(null) }
    var savedPlans by remember { mutableStateOf(AiContentStudioEngine.getSavedContentPlans(context)) }

    val platformOptions = listOf("Instagram", "YouTube", "Both")
    val contentTypeOptions = listOf(
        "Product Review", "UGC", "Tutorial", "Educational",
        "Storytelling", "Comedy", "Lifestyle", "Fashion",
        "Beauty", "Gaming", "Tech", "Travel", "Food", "Business", "Other"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AmoledBlack)
                .statusBarsPadding()
                .responsiveImeAndNavPadding()
        ) {
            Surface(
                color = Color(0xFF0F1A14),
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp)
                ) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x2210B981))
                                    .border(BorderStroke(1.dp, EmeraldPrimary), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = "Studio",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "AI Content Studio™",
                                    fontSize = 16.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                                Text(
                                    text = "Viral Content Intelligence Engine",
                                    fontSize = 10.5.sp,
                                    color = EmeraldPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x22FFFFFF))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    onDismiss()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tab Selector: New Plan vs Saved Ideas
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x1AFFFFFF))
                            .padding(3.dp)
                    ) {
                        // Studio Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (activeTab == "STUDIO") EmeraldPrimary else Color.Transparent)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    activeTab = "STUDIO"
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "New Plan",
                                    tint = if (activeTab == "STUDIO") AmoledBlack else TextWhite,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Content Planner",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeTab == "STUDIO") AmoledBlack else TextWhite
                                )
                            }
                        }

                        // Saved Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (activeTab == "SAVED") EmeraldPrimary else Color.Transparent)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    savedPlans = AiContentStudioEngine.getSavedContentPlans(context)
                                    activeTab = "SAVED"
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = "Saved",
                                    tint = if (activeTab == "SAVED") AmoledBlack else TextWhite,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Saved Ideas (${savedPlans.size})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeTab == "SAVED") AmoledBlack else TextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Main Tab Body
                    Box(modifier = Modifier.weight(1f)) {
                        if (activeTab == "STUDIO") {
                            if (generatedPlan == null) {
                                // Form Input Mode
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    // Step 1: Platform Selection
                                    Text(
                                        text = "STEP 1: TARGET PLATFORM",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldPrimary,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        platformOptions.forEach { platform ->
                                            val isSelected = selectedPlatform.equals(platform, ignoreCase = true)
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(38.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(if (isSelected) EmeraldPrimary else Color(0x18FFFFFF))
                                                    .border(
                                                        BorderStroke(1.dp, if (isSelected) EmeraldPrimary else Color(0x22FFFFFF)),
                                                        RoundedCornerShape(12.dp)
                                                    )
                                                    .clickable {
                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                        selectedPlatform = platform
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = platform,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) AmoledBlack else TextWhite
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Step 2: Content Type Selection
                                    Text(
                                        text = "STEP 2: CONTENT TYPE",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldPrimary,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    FlowRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        contentTypeOptions.forEach { type ->
                                            val isSelected = selectedContentType == type
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(if (isSelected) EmeraldPrimary else Color(0x18FFFFFF))
                                                    .border(
                                                        BorderStroke(1.dp, if (isSelected) EmeraldPrimary else Color(0x22FFFFFF)),
                                                        RoundedCornerShape(10.dp)
                                                    )
                                                    .clickable {
                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                        selectedContentType = type
                                                    }
                                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                            ) {
                                                Text(
                                                    text = type,
                                                    fontSize = 11.5.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = if (isSelected) AmoledBlack else TextWhite
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Step 3: Topic Input
                                    Text(
                                        text = "STEP 3: CONTENT TOPIC / PRODUCT",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldPrimary,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))

                                    OutlinedTextField(
                                        value = topicInput,
                                        onValueChange = { topicInput = it },
                                        placeholder = {
                                            Text("e.g. Best budget wireless earbuds for gym", fontSize = 12.5.sp, color = TextWhite.copy(alpha = 0.4f))
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = EmeraldPrimary,
                                            unfocusedBorderColor = Color(0x33FFFFFF),
                                            focusedContainerColor = Color(0x12FFFFFF),
                                            unfocusedContainerColor = Color(0x12FFFFFF),
                                            focusedTextColor = TextWhite,
                                            unfocusedTextColor = TextWhite
                                        ),
                                        singleLine = true
                                    )

                                    // Quick topic suggestions from AI memory
                                    if (userMemory.frequentlyUsedTopics.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Recent Topics Memory:",
                                            fontSize = 10.sp,
                                            color = TextWhite.copy(alpha = 0.5f)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        FlowRow(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            userMemory.frequentlyUsedTopics.take(4).forEach { recent ->
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(Color(0x1AFFFFFF))
                                                        .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(8.dp))
                                                        .clickable {
                                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                            topicInput = recent
                                                        }
                                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        text = "✦ $recent",
                                                        fontSize = 10.5.sp,
                                                        color = TextWhite.copy(alpha = 0.8f)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(24.dp))

                                    // Generate Button
                                    val canGenerate = topicInput.isNotBlank() && !isGenerating
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .clip(RoundedCornerShape(24.dp))
                                            .background(if (canGenerate) EmeraldPrimary else Color(0x33FFFFFF))
                                            .clickable(enabled = canGenerate) {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                isGenerating = true
                                                val plan = AiContentStudioEngine.generateContentPlan(
                                                    platform = selectedPlatform,
                                                    contentType = selectedContentType,
                                                    topic = topicInput,
                                                    userNiche = setupData.niche,
                                                    lang = com.example.core.LanguageEngine.currentLanguageState.value
                                                )
                                                generatedPlan = plan
                                                isGenerating = false
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            if (isGenerating) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(18.dp),
                                                    color = AmoledBlack,
                                                    strokeWidth = 2.dp
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "Analyzing Virality...",
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = AmoledBlack
                                                )
                                            } else {
                                                Icon(
                                                    imageVector = Icons.Default.AutoAwesome,
                                                    contentDescription = "Generate",
                                                    tint = if (canGenerate) AmoledBlack else TextWhite.copy(alpha = 0.5f),
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "Generate Content Plan ✨",
                                                    fontSize = 13.5.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = if (canGenerate) AmoledBlack else TextWhite.copy(alpha = 0.5f)
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                // Generated Plan Output View
                                GeneratedContentPlanView(
                                    plan = generatedPlan!!,
                                    onSave = {
                                        AiContentStudioEngine.saveContentPlan(context, generatedPlan!!)
                                        savedPlans = AiContentStudioEngine.getSavedContentPlans(context)
                                        Toast.makeText(context, "💾 Content Plan Saved to Memory!", Toast.LENGTH_SHORT).show()
                                    },
                                    onReset = {
                                        generatedPlan = null
                                    }
                                )
                            }
                        } else {
                            // Saved Ideas Tab
                            SavedContentIdeasView(
                                savedPlans = savedPlans,
                                onLoadPlan = { plan ->
                                    generatedPlan = plan
                                    activeTab = "STUDIO"
                                },
                                onDeletePlan = { planId ->
                                    AiContentStudioEngine.deleteContentPlan(context, planId)
                                    savedPlans = AiContentStudioEngine.getSavedContentPlans(context)
                                    Toast.makeText(context, "🗑️ Saved Idea Removed", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GeneratedContentPlanView(
    plan: ContentStudioResult,
    onSave: () -> Unit,
    onReset: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Card: Overall Score Meter
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0x18FFFFFF))
                .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)), RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = "Score",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CONTENT QUALITY SCORE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = EmeraldPrimary,
                            letterSpacing = 1.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(EmeraldPrimary)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${plan.qualityScore.overallScore} / 100",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = AmoledBlack
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Breakdown list of scores
                val scoreItems = listOf(
                    plan.qualityScore.hookScore,
                    plan.qualityScore.retentionScore,
                    plan.qualityScore.ctaScore,
                    plan.qualityScore.seoScore,
                    plan.qualityScore.thumbnailScore,
                    plan.qualityScore.engagementScore
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    scoreItems.forEach { score ->
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = score.title,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "${score.score}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            LinearProgressIndicator(
                                progress = { score.score / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(CircleShape),
                                color = EmeraldPrimary,
                                trackColor = Color(0x22FFFFFF)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = score.explanation,
                                fontSize = 10.sp,
                                color = TextWhite.copy(alpha = 0.6f),
                                lineHeight = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Improvement Suggestions Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x12FFFFFF))
                .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Improvements",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "IMPROVEMENT SUGGESTIONS",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        letterSpacing = 0.8.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                plan.improvementSuggestions.forEach { suggestion ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "💡 ",
                            fontSize = 11.sp
                        )
                        Column {
                            Text(
                                text = "${suggestion.category}: ${suggestion.title}",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = suggestion.detail,
                                fontSize = 10.5.sp,
                                color = TextWhite.copy(alpha = 0.7f),
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Section: Viral Hook
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x12FFFFFF))
                .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔥 VIRAL HOOK (0-3s)",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        letterSpacing = 0.8.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0x22FFFFFF))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                clipboard.setText(AnnotatedString(plan.viralHook))
                                Toast.makeText(context, "Copied Hook!", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = TextWhite,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Copy", fontSize = 10.sp, color = TextWhite)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "\"${plan.viralHook}\"",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    lineHeight = 18.sp
                )

                if (plan.hookOptions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Alternative Hooks:",
                        fontSize = 10.sp,
                        color = TextWhite.copy(alpha = 0.5f)
                    )
                    plan.hookOptions.forEach { alt ->
                        Text(
                            text = "• $alt",
                            fontSize = 11.sp,
                            color = TextWhite.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Section: Video Structure
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x12FFFFFF))
                .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = "🎬 VIDEO STRUCTURE FLOW",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "1. Opening Line:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
                Text(
                    text = plan.videoStructure.openingLine,
                    fontSize = 11.5.sp,
                    color = TextWhite,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "2. Middle Flow:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
                Text(
                    text = plan.videoStructure.middleFlow,
                    fontSize = 11.5.sp,
                    color = TextWhite,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "3. Ending CTA:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
                Text(
                    text = plan.videoStructure.endingCta,
                    fontSize = 11.5.sp,
                    color = TextWhite,
                    lineHeight = 16.sp
                )
            }
        }

        // Section: Caption, SEO & Hashtags
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x12FFFFFF))
                .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📝 CAPTION & 5 HASHTAGS",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        letterSpacing = 0.8.sp
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0x22FFFFFF))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                val fullText = "${plan.caption}\n\n${plan.hashtags.joinToString(" ")}"
                                clipboard.setText(AnnotatedString(fullText))
                                Toast.makeText(context, "Copied Caption & Hashtags!", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = TextWhite,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Copy", fontSize = 10.sp, color = TextWhite)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = plan.caption,
                    fontSize = 11.5.sp,
                    color = TextWhite.copy(alpha = 0.9f),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = plan.hashtags.joinToString(" "),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
            }
        }

        // Section: Thumbnail Text, Upload Time & Length
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x12FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(14.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Time",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("BEST UPLOAD TIME", fontSize = 9.sp, color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(plan.bestUploadTime, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x12FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(14.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Length",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SUGGESTED LENGTH", fontSize = 9.sp, color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(plan.suggestedVideoLength, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }
        }

        // Disclaimer Notice
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Notice",
                tint = TextWhite.copy(alpha = 0.5f),
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = plan.disclaimer,
                fontSize = 10.sp,
                color = TextWhite.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        }

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0x22FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x33FFFFFF)), RoundedCornerShape(22.dp))
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSave()
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Save Idea",
                        tint = TextWhite,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Idea 💾", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(EmeraldPrimary)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onReset()
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "New",
                        tint = AmoledBlack,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("New Idea 🔄", fontSize = 12.sp, fontWeight = FontWeight.Black, color = AmoledBlack)
                }
            }
        }
    }
}

@Composable
private fun SavedContentIdeasView(
    savedPlans: List<ContentStudioResult>,
    onLoadPlan: (ContentStudioResult) -> Unit,
    onDeletePlan: (String) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    if (savedPlans.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "💾", fontSize = 36.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No Saved Ideas Yet",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = "Generated content plans you save will appear here.",
                    fontSize = 12.sp,
                    color = TextWhite.copy(alpha = 0.6f)
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            savedPlans.forEach { plan ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x14FFFFFF))
                        .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onLoadPlan(plan)
                        }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(EmeraldPrimary)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${plan.qualityScore.overallScore} SCORE",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = AmoledBlack
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${plan.platform} • ${plan.contentType}",
                                    fontSize = 10.sp,
                                    color = TextWhite.copy(alpha = 0.6f)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = plan.topic,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = plan.viralHook,
                                fontSize = 11.sp,
                                color = TextWhite.copy(alpha = 0.7f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x22FFFFFF))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    onDeletePlan(plan.id)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
