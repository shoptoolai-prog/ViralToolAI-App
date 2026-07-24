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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite

/**
 * MASTER PHASE 15B.1 — Conversation-First AI Creator Profile View
 * No screenshot analysis, no OCR dependencies.
 * Shows Personal Creator Profile Attributes, AI Mentor Status, AI Tools, and Future-Ready Link Analysis.
 */
@Composable
fun CreatorProfileAiScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val setupData = remember { CreatorAcademyPrefs.getSetupData(context) }
    var activeToolDialog by remember { mutableStateOf<String?>(null) }
    var activeLinkDialog by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
                .padding(bottom = 60.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextWhite
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0x2210B981)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "AI Creator Profile",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = "Personal Creator Profile",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "AI Mentor Guidance • No Screenshots Needed",
                            fontSize = 11.sp,
                            color = EmeraldPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Personal Creator Profile Summary Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF0F382A), Color(0xFF131320))
                        )
                    )
                    .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)), RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Profile",
                                    tint = AmoledBlack,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "@creator_profile",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                                Text(
                                    text = "${setupData.targetPlatform} Creator • ${setupData.niche}",
                                    fontSize = 11.5.sp,
                                    color = EmeraldPrimary
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x2210B981))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = setupData.skillLevel,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Profile Grid Attributes
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ProfileStatItem(label = "Followers", value = setupData.currentFollowers, modifier = Modifier.weight(1f))
                        ProfileStatItem(label = "Primary Goal", value = setupData.primaryGoal, modifier = Modifier.weight(1f))
                        ProfileStatItem(label = "Pace", value = setupData.postingFrequency, modifier = Modifier.weight(1f))
                        ProfileStatItem(label = "Language", value = setupData.preferredLanguage, modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI Creator Toolkit
            AiCreatorToolsSection(
                setupData = setupData,
                onOpenTool = { toolName ->
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    activeToolDialog = toolName
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Future Ready Link Analysis
            FutureReadySection(
                onOpenLinkAnalysis = { type ->
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    activeLinkDialog = type
                }
            )
        }

        // Tool Dialogs
        when (activeToolDialog) {
            "Caption Generator" -> CaptionGeneratorDialog(setupData = setupData, onDismiss = { activeToolDialog = null })
            "Hashtag Generator" -> HashtagGeneratorDialog(setupData = setupData, onDismiss = { activeToolDialog = null })
            "Hook Generator" -> HookGeneratorDialog(setupData = setupData, onDismiss = { activeToolDialog = null })
            "Content Planner" -> ContentPlannerDialog(setupData = setupData, onDismiss = { activeToolDialog = null })
            "Posting Checklist" -> PostingChecklistDialog(onDismiss = { activeToolDialog = null })
            "Brand Pitch Guide" -> BrandPitchGuideDialog(setupData = setupData, onDismiss = { activeToolDialog = null })
        }

        if (activeLinkDialog != null) {
            LinkAnalysisDialog(type = activeLinkDialog!!, onDismiss = { activeLinkDialog = null })
        }
    }
}

@Composable
private fun ProfileStatItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = TextWhite.copy(alpha = 0.5f)
        )
    }
}
