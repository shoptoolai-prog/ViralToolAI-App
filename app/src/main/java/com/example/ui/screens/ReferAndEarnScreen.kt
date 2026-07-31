package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val GoldPrimary = Color(0xFFFFD700)
private val GoldAccent = Color(0xFFFFA500)

private const val LINK_FOUNDER_IG = "https://www.instagram.com/asittttttttttttttttt?igsh=bjJlN3M2N3hzMWI1"
private const val LINK_VIRALTOOLAI_IG = "https://www.instagram.com/viraltoolai?igsh=MXJjN2Q5ODJhd3RobQ=="

/**
 * REFER & REWARDS — PHASE 4 COMPLETE REDESIGN
 * Encourages creators to produce content for ViralToolAi and participate in verified reward campaigns.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferAndEarnScreen() {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    // State for existing submission
    var hasSubmitted by remember { mutableStateOf(CreatorAcademyPrefs.hasRewardSubmission(context)) }
    var submissionDetails by remember { mutableStateOf(CreatorAcademyPrefs.getRewardSubmissionDetails(context)) }
    var submissionStatus by remember { mutableStateOf(CreatorAcademyPrefs.getRewardSubmissionStatus(context)) }

    // Dialog State
    var showFormDialog by remember { mutableStateOf(false) }

    // Entrance Animation
    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
        )
    }

    // Helper to launch Instagram links
    fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .statusBarsPadding()
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isWide = maxWidth > 600.dp

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = if (isWide) 24.dp else 16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
            ) {
                // ==================================================
                // 1. TOP HEADER & QUICK STATS
                // ==================================================
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                alpha = animProgress.value
                                translationY = (1f - animProgress.value) * 20f
                            }
                    ) {
                        // Title Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(
                                            Brush.linearGradient(
                                                listOf(GoldPrimary.copy(alpha = 0.3f), ElectricPurple.copy(alpha = 0.2f))
                                            )
                                        )
                                        .border(BorderStroke(1.2.dp, GoldPrimary.copy(alpha = 0.6f)), RoundedCornerShape(14.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = "Refer & Rewards",
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Refer & Rewards",
                                        fontSize = 21.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TextWhite,
                                        letterSpacing = (-0.3).sp
                                    )
                                    Text(
                                        text = "Creator Reward Campaigns 2026",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextGray
                                    )
                                }
                            }

                            // Manual Verification Label Badge
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = EmeraldGlow.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, EmeraldGlow.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Manual Verification",
                                        tint = EmeraldGlow,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "Verified",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldGlow
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Stats Summary Grid Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            HeaderStatCard(
                                modifier = Modifier.weight(1f),
                                title = "Campaign",
                                value = "Reel & Post",
                                icon = Icons.Default.Campaign,
                                accentColor = ElectricPurple
                            )
                            HeaderStatCard(
                                modifier = Modifier.weight(1f),
                                title = "Reward Pool",
                                value = "Up to ₹1000",
                                icon = Icons.Default.MonetizationOn,
                                accentColor = GoldPrimary
                            )
                            HeaderStatCard(
                                modifier = Modifier.weight(1f),
                                title = "Status",
                                value = if (hasSubmitted) submissionStatus else "No Entry",
                                icon = Icons.Default.FactCheck,
                                accentColor = if (hasSubmitted) EmeraldGlow else TextGray
                            )
                        }
                    }
                }

                // ==================================================
                // 2. HERO BANNER
                // ==================================================
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = GoldPrimary),
                        shape = RoundedCornerShape(22.dp),
                        color = Color(0xFF141724),
                        border = BorderStroke(
                            1.5.dp,
                            Brush.linearGradient(
                                listOf(GoldPrimary, ElectricPurple, EmeraldGlow)
                            )
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Brush.linearGradient(listOf(GoldPrimary, GoldAccent))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Celebration,
                                        contentDescription = "Party",
                                        tint = Color.Black,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "🎉 Create. Share. Win Rewards!",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = "ViralToolAi Creator Contest",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = GoldPrimary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Create a Reel, Story or Post about ViralToolAi. The most creative and unique content can win rewards up to ₹1000.",
                                fontSize = 13.sp,
                                color = TextWhite.copy(alpha = 0.9f),
                                lineHeight = 19.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Manual Verification Note",
                                    tint = EmeraldGlow,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "Winners are selected only after manual verification.",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = EmeraldGlow
                                )
                            }
                        }
                    }
                }

                // ==================================================
                // 3. SUBMISSION STATUS CARD (IF SUBMITTED) OR EMPTY STATE
                // ==================================================
                item {
                    if (hasSubmitted) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(8.dp, RoundedCornerShape(18.dp)),
                            shape = RoundedCornerShape(18.dp),
                            color = Color(0xFF131826),
                            border = BorderStroke(1.2.dp, EmeraldGlow.copy(alpha = 0.5f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp)
                            ) {
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
                                            imageVector = Icons.Default.AssignmentTurnedIn,
                                            contentDescription = "Status",
                                            tint = EmeraldGlow,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "YOUR SUBMISSION STATUS",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = EmeraldGlow,
                                            letterSpacing = 0.5.sp
                                        )
                                    }

                                    // Status Badge
                                    val statusColor = when (submissionStatus) {
                                        "Approved" -> EmeraldGlow
                                        "Winner Announced" -> GoldPrimary
                                        "Rejected" -> Color.Red
                                        else -> Color(0xFFFFB703)
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = statusColor.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, statusColor)
                                    ) {
                                        Text(
                                            text = submissionStatus,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = statusColor,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Submitted by: ${submissionDetails["name"]} (@${submissionDetails["igUsername"]})",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "Content Type: ${submissionDetails["contentTypes"]} • Date: ${submissionDetails["date"]}",
                                    fontSize = 11.5.sp,
                                    color = TextGray
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFF0C101A)
                                ) {
                                    Text(
                                        text = "✅ Your submission has been received. Manual verification usually takes a few days.",
                                        fontSize = 12.sp,
                                        color = EmeraldGlow,
                                        modifier = Modifier.padding(10.dp),
                                        lineHeight = 16.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                TextButton(
                                    onClick = {
                                        CreatorAcademyPrefs.clearRewardSubmission(context)
                                        hasSubmitted = false
                                        Toast.makeText(context, "Submission reset for new entry", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("Submit Another Entry", fontSize = 12.sp, color = TextGray)
                                }
                            }
                        }
                    } else {
                        // EMPTY STATE
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            color = Color(0xFF121622),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(ElectricPurple.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CardGiftcard,
                                        contentDescription = "No submissions",
                                        tint = ElectricPurple,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "No submissions yet.",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Create your first Reel, Story or Post and participate to win exciting rewards.",
                                    fontSize = 12.sp,
                                    color = TextGray,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }

                // ==================================================
                // 4. HOW TO PARTICIPATE (STEPS)
                // ==================================================
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "HOW TO PARTICIPATE",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = GoldPrimary,
                            letterSpacing = 0.8.sp
                        )

                        // STEP 1: Follow Founder
                        ParticipateStepCard(
                            stepNumber = "1",
                            title = "Follow the Founder Instagram Page",
                            description = "Stay connected with official creator updates & reward announcements.",
                            buttonText = "Follow Founder",
                            icon = Icons.Default.PersonAdd,
                            accentColor = Color(0xFFE1306C),
                            onClick = { openUrl(LINK_FOUNDER_IG) }
                        )

                        // STEP 2: Follow Official ViralToolAi
                        ParticipateStepCard(
                            stepNumber = "2",
                            title = "Follow Official ViralToolAi Instagram",
                            description = "Get featured on our official page & stay eligible for contest rewards.",
                            buttonText = "Follow ViralToolAi",
                            icon = Icons.Default.Campaign,
                            accentColor = ElectricPurple,
                            onClick = { openUrl(LINK_VIRALTOOLAI_IG) }
                        )

                        // STEP 3: Create Content Options
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF131824),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(EmeraldGlow.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "3",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = EmeraldGlow
                                        )
                                    }
                                    Text(
                                        text = "Create Any of These Content Types",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                val contentTypesList = listOf(
                                    "Instagram Reel" to "High reach short video showing ViralToolAi features",
                                    "Instagram Story" to "Quick shoutout with active tag or link sticker",
                                    "Instagram Post" to "Carousel or image post explaining creator tips",
                                    "Multiple Posts" to "Higher chance of winning with multiple entries"
                                )

                                contentTypesList.forEach { (title, desc) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Checked",
                                            tint = EmeraldGlow,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = title,
                                                fontSize = 12.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextWhite
                                            )
                                            Text(
                                                text = desc,
                                                fontSize = 11.sp,
                                                color = TextGray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // ==================================================
                // 5. SUBMIT ENTRY BUTTON
                // ==================================================
                item {
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                            showFormDialog = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .shadow(10.dp, RoundedCornerShape(16.dp), spotColor = GoldPrimary),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Submit",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Submit for Verification",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                    }
                }

                // ==================================================
                // 6. REWARD RULES
                // ==================================================
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFF121622),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Gavel,
                                    contentDescription = "Rules",
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "REWARD RULES & GUIDELINES",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = GoldPrimary,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            val rules = listOf(
                                "Rewards are based on creativity, retention, and originality.",
                                "Duplicate or copied content will be automatically rejected.",
                                "Following both Instagram pages is strictly mandatory.",
                                "Manual verification is required before rewards are issued.",
                                "Reward campaigns & pools may change over time."
                            )

                            rules.forEach { rule ->
                                Row(
                                    modifier = Modifier.padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "• ",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldPrimary
                                    )
                                    Text(
                                        text = rule,
                                        fontSize = 12.sp,
                                        color = TextGray,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ==================================================
        // GOOGLE FORM SUBMISSION DIALOG
        // ==================================================
        if (showFormDialog) {
            GoogleFormSubmissionDialog(
                onDismiss = { showFormDialog = false },
                onSubmitted = { email, name, igUsername, igLink, contentTypes, screenshot ->
                    CreatorAcademyPrefs.saveRewardSubmission(
                        context = context,
                        email = email,
                        name = name,
                        igUsername = igUsername,
                        igLink = igLink,
                        contentTypes = contentTypes,
                        screenshotUri = screenshot
                    )
                    hasSubmitted = true
                    submissionDetails = CreatorAcademyPrefs.getRewardSubmissionDetails(context)
                    submissionStatus = "Pending Review"
                    showFormDialog = false
                    Toast.makeText(
                        context,
                        "✅ Submission received! Manual verification usually takes a few days.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }
}

/**
 * Header Stat Card
 */
@Composable
private fun HeaderStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    accentColor: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF141824),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = accentColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = title,
                fontSize = 9.5.sp,
                color = TextGray
            )
        }
    }
}

/**
 * Step Card Component
 */
@Composable
private fun ParticipateStepCard(
    stepNumber: String,
    title: String,
    description: String,
    buttonText: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF131824),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stepNumber,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = accentColor
                    )
                }

                Column {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = description,
                        fontSize = 11.sp,
                        color = TextGray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = buttonText, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

/**
 * In-App Google Form Style Dialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoogleFormSubmissionDialog(
    onDismiss: () -> Unit,
    onSubmitted: (email: String, name: String, igUsername: String, igLink: String, contentTypes: String, screenshot: String) -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Form Field States
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var igUsername by remember { mutableStateOf("") }
    var igLink by remember { mutableStateOf("") }

    // Content Type Checkboxes
    var isReelChecked by remember { mutableStateOf(false) }
    var isStoryChecked by remember { mutableStateOf(false) }
    var isPostChecked by remember { mutableStateOf(false) }

    // Required Follow Checkboxes
    var isFounderFollowed by remember { mutableStateOf(false) }
    var isOfficialFollowed by remember { mutableStateOf(false) }

    // Screenshot state
    var screenshotName by remember { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            screenshotName = "screenshot_${System.currentTimeMillis()}.png"
            Toast.makeText(context, "Screenshot attached successfully", Toast.LENGTH_SHORT).show()
        }
    }

    val isFormValid = email.contains("@") &&
            fullName.trim().isNotEmpty() &&
            igUsername.trim().isNotEmpty() &&
            igLink.trim().isNotEmpty() &&
            (isReelChecked || isStoryChecked || isPostChecked) &&
            isFounderFollowed &&
            isOfficialFollowed

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFF121622),
            border = BorderStroke(1.5.dp, GoldPrimary.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(GoldPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🏆", fontSize = 24.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "ViralToolAi Creator Rewards",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Submit your content for manual verification. Complete all required details carefully.",
                            fontSize = 11.5.sp,
                            color = TextGray,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = EmeraldGlow.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "🔒 No Instagram password or login required.",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGlow,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                // Field 1: Email
                item {
                    Column {
                        Text(text = "Email Address *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("your.email@gmail.com", fontSize = 12.sp, color = TextGray) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Field 2: Full Name
                item {
                    Column {
                        Text(text = "Full Name *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            placeholder = { Text("Enter your full name", fontSize = 12.sp, color = TextGray) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Field 3: Instagram Username
                item {
                    Column {
                        Text(text = "Instagram Username *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = igUsername,
                            onValueChange = { igUsername = it },
                            placeholder = { Text("@your_username", fontSize = 12.sp, color = TextGray) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Field 4: Instagram Profile Link
                item {
                    Column {
                        Text(text = "Instagram Profile / Post Link *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = igLink,
                            onValueChange = { igLink = it },
                            placeholder = { Text("https://instagram.com/p/...", fontSize = 12.sp, color = TextGray) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Field 5: Content Type Checkboxes
                item {
                    Column {
                        Text(text = "Content Type (Select at least one) *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = isReelChecked, onCheckedChange = { isReelChecked = it }, colors = CheckboxDefaults.colors(checkedColor = GoldPrimary))
                            Text(text = "Reel", fontSize = 12.sp, color = TextWhite)

                            Spacer(modifier = Modifier.width(12.dp))

                            Checkbox(checked = isStoryChecked, onCheckedChange = { isStoryChecked = it }, colors = CheckboxDefaults.colors(checkedColor = GoldPrimary))
                            Text(text = "Story", fontSize = 12.sp, color = TextWhite)

                            Spacer(modifier = Modifier.width(12.dp))

                            Checkbox(checked = isPostChecked, onCheckedChange = { isPostChecked = it }, colors = CheckboxDefaults.colors(checkedColor = GoldPrimary))
                            Text(text = "Post", fontSize = 12.sp, color = TextWhite)
                        }
                    }
                }

                // Field 6: Upload Screenshot
                item {
                    Column {
                        Text(text = "Upload Screenshot (PNG, JPG, JPEG)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedButton(
                            onClick = { photoPickerLauncher.launch("image/*") },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.6f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.CloudUpload, contentDescription = "Upload", tint = GoldPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = screenshotName ?: "Choose Image File",
                                    fontSize = 12.sp,
                                    color = if (screenshotName != null) EmeraldGlow else TextWhite
                                )
                            }
                        }
                    }
                }

                // Field 7: Follow Confirmation Required Checkboxes
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0D111A))
                            .padding(10.dp)
                    ) {
                        Text(text = "Follow Confirmation (Required) *", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { isFounderFollowed = !isFounderFollowed }
                        ) {
                            Checkbox(checked = isFounderFollowed, onCheckedChange = { isFounderFollowed = it }, colors = CheckboxDefaults.colors(checkedColor = GoldPrimary))
                            Text(text = "I follow the Founder Instagram page.", fontSize = 11.sp, color = TextWhite)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { isOfficialFollowed = !isOfficialFollowed }
                        ) {
                            Checkbox(checked = isOfficialFollowed, onCheckedChange = { isOfficialFollowed = it }, colors = CheckboxDefaults.colors(checkedColor = GoldPrimary))
                            Text(text = "I follow the Official ViralToolAi Instagram page.", fontSize = 11.sp, color = TextWhite)
                        }
                    }
                }

                // Action Buttons
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel", color = TextWhite, fontSize = 13.sp)
                        }

                        Button(
                            onClick = {
                                if (isFormValid) {
                                    val types = mutableListOf<String>()
                                    if (isReelChecked) types.add("Reel")
                                    if (isStoryChecked) types.add("Story")
                                    if (isPostChecked) types.add("Post")
                                    onSubmitted(
                                        email,
                                        fullName,
                                        igUsername,
                                        igLink,
                                        types.joinToString(", "),
                                        screenshotName ?: "attached"
                                    )
                                }
                            },
                            enabled = isFormValid,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                disabledContainerColor = GoldPrimary.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Submit Entry", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
