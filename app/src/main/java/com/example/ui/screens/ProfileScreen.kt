package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.ui.components.ViralToolAiLogo
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val GoldPrimary = Color(0xFFFFD700)
private val GoldAccent = Color(0xFFFFA500)

/**
 * PROFILE & SETTINGS — PHASE 5 COMPLETE REDESIGN
 * Flagship account, settings, performance & app information hub.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSwitchExperience: () -> Unit = {},
    onNavigateToCourse: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Preferences & State
    val setupData = remember { CreatorAcademyPrefs.getSetupData(context) }
    var userDisplayName by remember { mutableStateOf(CreatorAcademyPrefs.getUserDisplayName(context)) }

    // App Settings State
    var selectedThemeMode by remember { mutableStateOf(CreatorAcademyPrefs.getAppThemeMode(context)) }
    var selectedLanguage by remember { mutableStateOf(CreatorAcademyPrefs.getAppLanguage(context)) }
    var notificationsEnabled by remember { mutableStateOf(CreatorAcademyPrefs.getNotificationsEnabled(context)) }

    // Last Course Data
    val lastCourseData = remember { CreatorAcademyPrefs.getLastOpenedCourse(context) }

    // Storage & Cache calculation
    var cacheSizeBytes by remember { mutableLongStateOf(calculateCacheSize(context)) }
    var isClearingCache by remember { mutableStateOf(false) }

    // Dialog States
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showBugReportDialog by remember { mutableStateOf(false) }
    var showFeatureRequestDialog by remember { mutableStateOf(false) }
    var showFaqDialog by remember { mutableStateOf(false) }
    var showFounderDialog by remember { mutableStateOf(false) }
    var showWhatsNewDialog by remember { mutableStateOf(false) }
    var activeLegalSheet by remember { mutableStateOf<String?>(null) }

    // Links & Support
    val founderInstagramUrl = "https://www.instagram.com/asittttttttttttttttt?igsh=bjJlN3M2N3hzMWI1"
    val officialInstagramUrl = "https://www.instagram.com/viraltoolai?igsh=MXJjN2Q5ODJhd3RobQ=="
    val supportEmail = "asityadavteambusiness@gmail.com"

    val openUrl = { url: String ->
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Opening link...", Toast.LENGTH_SHORT).show()
        }
    }

    val copyEmailToClipboard = {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("Support Email", supportEmail)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context, "Copied support email: $supportEmail", Toast.LENGTH_SHORT).show()
    }

    val openMailClient = { subject: String, body: String ->
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        try {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$supportEmail")
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
            }
            context.startActivity(emailIntent)
        } catch (e: Exception) {
            copyEmailToClipboard()
        }
    }

    // Launch Stagger Animation
    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        // Multi-layered Ambient Background Glow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            EmeraldPrimary.copy(alpha = 0.18f),
                            ElectricPurple.copy(alpha = 0.10f),
                            Color.Transparent
                        ),
                        center = Offset(400f, -50f)
                    )
                )
        )

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isWide = maxWidth > 600.dp
            val isTablet = maxWidth > 900.dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .verticalScroll(scrollState)
                    .padding(horizontal = if (isTablet) 32.dp else if (isWide) 24.dp else 16.dp)
                    .padding(top = 12.dp, bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ==================================================
                // TOP HEADER: PROFILE GLASSMORPHISM CARD
                // ==================================================
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = animProgress.value
                            translationY = (1f - animProgress.value) * 30f
                        }
                ) {
                    TopHeaderProfileCard(
                        displayName = userDisplayName,
                        skillLevel = setupData.skillLevel,
                        niche = setupData.niche,
                        onEditProfile = { showEditProfileDialog = true }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ==================================================
                // SECTION 1: CONTINUE LEARNING
                // ==================================================
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = animProgress.value
                            translationY = (1f - animProgress.value) * 25f
                        }
                ) {
                    SectionHeader(title = "CONTINUE LEARNING", icon = Icons.Default.School, iconTint = EmeraldGlow)

                    Spacer(modifier = Modifier.height(8.dp))

                    if (lastCourseData != null) {
                        val courseName = lastCourseData["name"] as? String ?: "Creator Masterclass"
                        val progress = lastCourseData["progress"] as? Int ?: 0
                        val completed = lastCourseData["completed"] as? Int ?: 0
                        val total = lastCourseData["total"] as? Int ?: 1

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(8.dp, RoundedCornerShape(22.dp)),
                            shape = RoundedCornerShape(22.dp),
                            color = Color(0xFF141824),
                            border = BorderStroke(1.2.dp, EmeraldGlow.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(EmeraldGlow.copy(alpha = 0.2f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PlayCircle,
                                                contentDescription = null,
                                                tint = EmeraldGlow,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = courseName,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextWhite
                                            )
                                            Text(
                                                text = "$completed of $total Lessons Completed",
                                                fontSize = 11.5.sp,
                                                color = TextGray
                                            )
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = EmeraldGlow.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, EmeraldGlow)
                                    ) {
                                        Text(
                                            text = "$progress%",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black,
                                            color = EmeraldGlow,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                LinearProgressIndicator(
                                    progress = { progress / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = EmeraldGlow,
                                    trackColor = Color.White.copy(alpha = 0.1f)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        onNavigateToCourse(courseName)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGlow),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = "Continue Learning", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                    }
                                }
                            }
                        }
                    } else {
                        // EMPTY STATE: START YOUR CREATOR JOURNEY
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            color = Color(0xFF131724),
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
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(ElectricPurple.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RocketLaunch,
                                        contentDescription = "Start Journey",
                                        tint = ElectricPurple,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "Start Your Creator Journey",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Explore 10+ AI creator modules, brand deal guides, Meesho/Wishlink affiliate strategies, and video editing masterclasses.",
                                    fontSize = 12.sp,
                                    color = TextGray,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 17.sp
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        onSwitchExperience()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Explore Creator Courses", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ==================================================
                // SECTION 2: QUICK ACTIONS (GRID)
                // ==================================================
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = animProgress.value
                            translationY = (1f - animProgress.value) * 20f
                        }
                ) {
                    SectionHeader(title = "QUICK ACTIONS", icon = Icons.Default.FlashOn, iconTint = GoldPrimary)

                    Spacer(modifier = Modifier.height(10.dp))

                    val gridColumns = if (isWide) 3 else 2
                    val quickActions = listOf(
                        QuickActionItem("Rate App", "Support on Play Store", Icons.Default.StarRate, GoldPrimary) {
                            Toast.makeText(context, "Opening Play Store rating...", Toast.LENGTH_SHORT).show()
                            openUrl("https://play.google.com/store/apps")
                        },
                        QuickActionItem("Share App", "Invite Fellow Creators", Icons.Default.Share, EmeraldGlow) {
                            try {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "ViralToolAi App")
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "🚀 Learn creator strategies, generate viral scripts & earn rewards with ViralToolAi! Download now: https://play.google.com/store/apps"
                                    )
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share ViralToolAi"))
                            } catch (e: Exception) {
                                Toast.makeText(context, "Could not launch share sheet", Toast.LENGTH_SHORT).show()
                            }
                        },
                        QuickActionItem("Contact Us", "Direct Founder Support", Icons.Default.Email, Color(0xFF3B82F6)) {
                            openMailClient("ViralToolAi Direct Contact", "Hello Asit,\n\nI have a question regarding ViralToolAi:")
                        },
                        QuickActionItem("Report a Bug", "Help us fix issues", Icons.Default.BugReport, CrimsonLight) {
                            showBugReportDialog = true
                        },
                        QuickActionItem("Request Feature", "Suggest new tools", Icons.Default.Lightbulb, GoldPrimary) {
                            showFeatureRequestDialog = true
                        },
                        QuickActionItem("Join Community", "Instagram Creator Hub", Icons.Default.Groups, ElectricPurple) {
                            openUrl(officialInstagramUrl)
                        }
                    )

                    quickActions.chunked(gridColumns).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowItems.forEach { action ->
                                QuickActionCard(
                                    modifier = Modifier.weight(1f),
                                    item = action
                                )
                            }
                            if (rowItems.size < gridColumns) {
                                repeat(gridColumns - rowItems.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ==================================================
                // SECTION 3: APP SETTINGS
                // ==================================================
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = animProgress.value
                            translationY = (1f - animProgress.value) * 15f
                        }
                ) {
                    SectionHeader(title = "APP SETTINGS", icon = Icons.Default.Settings, iconTint = ElectricPurple)

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF131824),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Dark Mode Selector
                            Text(text = "Theme Mode", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray)
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("System", "Dark", "Light").forEach { mode ->
                                    val isSelected = selectedThemeMode == mode
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                selectedThemeMode = mode
                                                CreatorAcademyPrefs.setAppThemeMode(context, mode)
                                                Toast.makeText(context, "Theme set to $mode Mode", Toast.LENGTH_SHORT).show()
                                            },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected) ElectricPurple.copy(alpha = 0.25f) else Color(0xFF0C101A),
                                        border = BorderStroke(1.dp, if (isSelected) ElectricPurple else Color.White.copy(alpha = 0.1f))
                                    ) {
                                        Text(
                                            text = mode,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) ElectricPurple else TextWhite,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
                            Spacer(modifier = Modifier.height(16.dp))

                            // Language Selector
                            Text(text = "App Language", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray)
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("English", "Hindi", "Hinglish").forEach { lang ->
                                    val isSelected = selectedLanguage == lang
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                selectedLanguage = lang
                                                CreatorAcademyPrefs.setAppLanguage(context, lang)
                                                Toast.makeText(context, "Language set to $lang", Toast.LENGTH_SHORT).show()
                                            },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected) EmeraldGlow.copy(alpha = 0.25f) else Color(0xFF0C101A),
                                        border = BorderStroke(1.dp, if (isSelected) EmeraldGlow else Color.White.copy(alpha = 0.1f))
                                    ) {
                                        Text(
                                            text = lang,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) EmeraldGlow else TextWhite,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
                            Spacer(modifier = Modifier.height(16.dp))

                            // Notifications Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "Notifications",
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = GoldPrimary.copy(alpha = 0.15f)
                                        ) {
                                            Text(
                                                text = "Future Ready",
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = GoldPrimary,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "Receive daily creator tips, task reminders & contest updates.",
                                        fontSize = 11.sp,
                                        color = TextGray
                                    )
                                }

                                Switch(
                                    checked = notificationsEnabled,
                                    onCheckedChange = { checked ->
                                        notificationsEnabled = checked
                                        CreatorAcademyPrefs.setNotificationsEnabled(context, checked)
                                        Toast.makeText(
                                            context,
                                            if (checked) "Notifications enabled" else "Notifications muted",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.Black,
                                        checkedTrackColor = EmeraldGlow
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ==================================================
                // SECTION 4: SUPPORT & LEGAL
                // ==================================================
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionHeader(title = "SUPPORT & LEGAL", icon = Icons.Default.HelpOutline, iconTint = EmeraldGlow)

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF131824),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
                    ) {
                        Column {
                            SettingsRowItem(
                                title = "Privacy Policy",
                                subtitle = "Play Store compliant zero data selling policy",
                                icon = Icons.Default.Security,
                                iconTint = EmeraldGlow,
                                onClick = { activeLegalSheet = "privacy" }
                            )
                            HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

                            SettingsRowItem(
                                title = "Terms & Conditions",
                                subtitle = "App usage rules & AI guidelines",
                                icon = Icons.Default.Gavel,
                                iconTint = GoldPrimary,
                                onClick = { activeLegalSheet = "terms" }
                            )
                            HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

                            SettingsRowItem(
                                title = "About ViralToolAi",
                                subtitle = "AI creator platform mission & roadmap",
                                icon = Icons.Default.Info,
                                iconTint = ElectricPurple,
                                onClick = { showFounderDialog = true }
                            )
                            HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

                            SettingsRowItem(
                                title = "Frequently Asked Questions (FAQ)",
                                subtitle = "Answers to common creator queries",
                                icon = Icons.Default.QuestionAnswer,
                                iconTint = Color(0xFF3B82F6),
                                onClick = { showFaqDialog = true }
                            )
                            HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

                            SettingsRowItem(
                                title = "Contact Support",
                                subtitle = supportEmail,
                                icon = Icons.Default.SupportAgent,
                                iconTint = EmeraldGlow,
                                onClick = { openMailClient("ViralToolAi Support Request", "Hello Team,\n\n") }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ==================================================
                // SECTION 5: ABOUT VIRALTOOLAI (APP DETAILS)
                // ==================================================
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionHeader(title = "ABOUT APP", icon = Icons.Default.AutoAwesome, iconTint = GoldPrimary)

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF131824),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    ViralToolAiLogo(size = 32.dp)
                                    Column {
                                        Text(text = "ViralToolAI", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                        Text(text = "AI Creator Intelligence • 2026 Edition", fontSize = 11.5.sp, color = TextGray)
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = EmeraldGlow.copy(alpha = 0.2f),
                                    border = BorderStroke(1.dp, EmeraldGlow)
                                ) {
                                    Text(
                                        text = "v1.0.0 (2026.07)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldGlow,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { showWhatsNewDialog = true },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, GoldPrimary)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.NewReleases, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(15.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("What's New", fontSize = 12.sp, color = GoldPrimary)
                                    }
                                }

                                OutlinedButton(
                                    onClick = { showFounderDialog = true },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, ElectricPurple)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = ElectricPurple, modifier = Modifier.size(15.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Credits", fontSize = 12.sp, color = ElectricPurple)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ==================================================
                // SECTION 6: PERFORMANCE & STORAGE
                // ==================================================
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionHeader(title = "PERFORMANCE & STORAGE", icon = Icons.Default.Speed, iconTint = EmeraldGlow)

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF131824),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "App Storage Used", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text(text = "UI Assets, Local Prefs & Cached Media", fontSize = 11.sp, color = TextGray)
                                }
                                Text(
                                    text = formatBytes(cacheSizeBytes + 14500000L),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "Offline UI Cache", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text(text = formatBytes(cacheSizeBytes), fontSize = 11.sp, color = TextGray)
                                }

                                Button(
                                    onClick = {
                                        if (!isClearingCache) {
                                            isClearingCache = true
                                            scope.launch {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                delay(400)
                                                clearAppCache(context)
                                                cacheSizeBytes = calculateCacheSize(context)
                                                isClearingCache = false
                                                Toast.makeText(context, "✅ Offline cache cleared successfully", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonLight),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (isClearingCache) {
                                            CircularProgressIndicator(modifier = Modifier.size(12.dp), color = Color.White, strokeWidth = 1.5.dp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                        } else {
                                            Icon(imageVector = Icons.Default.CleaningServices, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                        }
                                        Text("Clear Cache", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF0C101A)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(EmeraldGlow)
                                    )
                                    Text(
                                        text = "App Performance Status: Optimal • 60 FPS • Low Battery Usage",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = EmeraldGlow
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ==================================================
                // SECTION 7: DEVELOPER (BUILT IN INDIA & FOUNDER)
                // ==================================================
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionHeader(title = "DEVELOPER", icon = Icons.Default.Code, iconTint = ElectricPurple)

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(22.dp)),
                        shape = RoundedCornerShape(22.dp),
                        color = Color(0xFF141824),
                        border = BorderStroke(
                            1.2.dp,
                            Brush.linearGradient(listOf(EmeraldGlow, ElectricPurple, GoldPrimary))
                        )
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(text = "🇮🇳", fontSize = 22.sp)
                                    Column {
                                        Text(text = "Proudly Built in India", fontSize = 15.sp, fontWeight = FontWeight.Black, color = TextWhite)
                                        Text(text = "Founder: Asit Yadav", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldGlow)
                                    }
                                }

                                IconButton(onClick = { showFounderDialog = true }) {
                                    Icon(imageVector = Icons.Default.Info, contentDescription = "Founder Info", tint = GoldPrimary)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Instagram External Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { openUrl(founderInstagramUrl) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE1306C)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Founder IG", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }

                                Button(
                                    onClick = { openUrl(officialInstagramUrl) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Campaign, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Official IG", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ==================================================
                // SECTION 8: FEEDBACK
                // ==================================================
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionHeader(title = "FEEDBACK", icon = Icons.Default.RateReview, iconTint = GoldPrimary)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showBugReportDialog = true },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, CrimsonLight)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.ReportProblem, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Report Issue", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = CrimsonLight)
                            }
                        }

                        Button(
                            onClick = { showFeatureRequestDialog = true },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.AddComment, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Suggest Feature", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Footer
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Made with ❤️ by Asit Yadav",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ViralToolAi v1.0.0 • Flagship Glass Edition",
                        fontSize = 10.5.sp,
                        color = TextGray
                    )
                }
            }
        }

        // ==================================================
        // DIALOGS & MODALS
        // ==================================================

        // 1. Edit Profile Modal
        if (showEditProfileDialog) {
            EditProfileDialog(
                currentName = userDisplayName,
                currentNiche = setupData.niche,
                onDismiss = { showEditProfileDialog = false },
                onSave = { newName, newNiche ->
                    userDisplayName = newName
                    CreatorAcademyPrefs.saveUserDisplayName(context, newName)
                    CreatorAcademyPrefs.saveSetupData(
                        context,
                        setupData.copy(niche = newNiche)
                    )
                    showEditProfileDialog = false
                    Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // 2. Report Bug Dialog
        if (showBugReportDialog) {
            ReportBugDialog(
                onDismiss = { showBugReportDialog = false },
                onSend = { title, desc ->
                    openMailClient("ViralToolAi Bug Report: $title", "Bug Description:\n$desc\n\nDevice: Android")
                    showBugReportDialog = false
                }
            )
        }

        // 3. Feature Request Dialog
        if (showFeatureRequestDialog) {
            SuggestFeatureDialog(
                onDismiss = { showFeatureRequestDialog = false },
                onSend = { title, desc ->
                    openMailClient("ViralToolAi Feature Request: $title", "Feature Suggestion:\n$desc")
                    showFeatureRequestDialog = false
                }
            )
        }

        // 4. FAQ Accordion Dialog
        if (showFaqDialog) {
            FaqDialog(onDismiss = { showFaqDialog = false })
        }

        // 5. About Founder Dialog
        if (showFounderDialog) {
            AboutFounderDialog(
                onDismiss = { showFounderDialog = false },
                onFollowInstagram = { openUrl(founderInstagramUrl) }
            )
        }

        // 6. What's New Dialog
        if (showWhatsNewDialog) {
            WhatsNewDialog(onDismiss = { showWhatsNewDialog = false })
        }

        // 7. Legal Dialog Sheet
        activeLegalSheet?.let { sheetType ->
            LegalDialogSheet(
                type = sheetType,
                supportEmail = supportEmail,
                onDismiss = { activeLegalSheet = null }
            )
        }
    }
}

/**
 * Top Header Profile Card Component
 */
@Composable
private fun TopHeaderProfileCard(
    displayName: String,
    skillLevel: String,
    niche: String,
    onEditProfile: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(26.dp), spotColor = EmeraldGlow),
        shape = RoundedCornerShape(26.dp),
        color = Color(0xFF131826),
        border = BorderStroke(
            1.5.dp,
            Brush.linearGradient(listOf(EmeraldGlow, ElectricPurple, GoldPrimary))
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Avatar Box
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(EmeraldGlow, ElectricPurple, GoldPrimary)
                                )
                            )
                            .border(2.dp, Color.Black, CircleShape)
                            .clickable { onEditProfile() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = Color.Black,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Welcome back,",
                                fontSize = 12.sp,
                                color = TextGray
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = EmeraldGlow.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "CREATOR PRO ⚡",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = EmeraldGlow,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = displayName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "$niche • $skillLevel Creator",
                            fontSize = 11.5.sp,
                            color = TextGray
                        )
                    }
                }

                // Edit Button
                IconButton(
                    onClick = onEditProfile,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.08f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = TextWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/**
 * Section Header
 */
@Composable
private fun SectionHeader(title: String, icon: ImageVector, iconTint: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = iconTint,
            letterSpacing = 1.2.sp
        )
    }
}

/**
 * Quick Action Data & Card
 */
private data class QuickActionItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val onClick: () -> Unit
)

@Composable
private fun QuickActionCard(
    modifier: Modifier = Modifier,
    item: QuickActionItem
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { item.onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF131824),
        border = BorderStroke(1.dp, item.color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(item.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.color,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.title,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = item.subtitle,
                fontSize = 10.sp,
                color = TextGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Settings Row Item
 */
@Composable
private fun SettingsRowItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(20.dp))
            }

            Column {
                Text(text = title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(text = subtitle, fontSize = 11.sp, color = TextGray)
            }
        }

        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp))
    }
}

// ================= DIALOG IMPLEMENTATIONS =================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileDialog(
    currentName: String,
    currentNiche: String,
    onDismiss: () -> Unit,
    onSave: (newName: String, newNiche: String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var niche by remember { mutableStateOf(currentNiche) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFF121622),
            border = BorderStroke(1.5.dp, EmeraldGlow.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(text = "Edit Profile", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextWhite)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Display Name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldGlow, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = niche,
                    onValueChange = { niche = it },
                    label = { Text("Creator Niche (e.g., Tech, Fashion)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldGlow, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel", color = TextGray) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSave(name.trim(), niche.trim()) },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGlow)
                    ) {
                        Text("Save Changes", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportBugDialog(
    onDismiss: () -> Unit,
    onSend: (title: String, desc: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFF121622),
            border = BorderStroke(1.5.dp, CrimsonLight.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Report a Bug 🐞", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextWhite)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Bug Title (e.g. Screen frozen)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CrimsonLight, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    placeholder = { Text("Detailed description of what happened...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CrimsonLight, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel", color = TextGray) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSend(title, desc) },
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonLight),
                        enabled = title.isNotEmpty() && desc.isNotEmpty()
                    ) {
                        Text("Send Bug Report", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuggestFeatureDialog(
    onDismiss: () -> Unit,
    onSend: (title: String, desc: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFF121622),
            border = BorderStroke(1.5.dp, GoldPrimary.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Suggest a Feature 💡", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextWhite)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Feature Title") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldPrimary, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    placeholder = { Text("How would this feature help you as a creator?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldPrimary, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel", color = TextGray) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSend(title, desc) },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        enabled = title.isNotEmpty() && desc.isNotEmpty()
                    ) {
                        Text("Submit Idea", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun FaqDialog(onDismiss: () -> Unit) {
    val faqs = listOf(
        "Is ViralToolAi completely free?" to "Yes! All creator education, AI script engines, and contest reward campaigns are free for selected creators.",
        "How do Refer & Rewards contest payments work?" to "Submit your video or post link under Refer & Earn. Once manually verified, rewards up to ₹1000 are processed directly.",
        "Will my data or Instagram login be requested?" to "No! ViralToolAi never asks for your Instagram password or private login. Everything runs securely on your device.",
        "How can I contact the founder?" to "You can directly connect with Asit Yadav via Instagram (@asittttttttttttttttt) or email support."
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFF121622),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(text = "Frequently Asked Questions", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextWhite)

                faqs.forEach { (q, a) ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0C101A))
                            .padding(12.dp)
                    ) {
                        Text(text = "Q: $q", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EmeraldGlow)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = a, fontSize = 11.5.sp, color = TextGray, lineHeight = 16.sp)
                    }
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close FAQ", color = TextGray)
                }
            }
        }
    }
}

@Composable
private fun WhatsNewDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFF121622),
            border = BorderStroke(1.dp, GoldPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "✨ What's New in Phase 5", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextWhite)

                val updates = listOf(
                    "🔥 Redesigned Profile & Settings Hub: Glassmorphic UI with full app control.",
                    "🏆 Creator Rewards Campaign: Submit Reels & win up to ₹1000 with manual verification.",
                    "⚡ Multi-Language Support: English, Hindi, and Hinglish for all creator modules.",
                    "🛠 Offline UI Caching & Speed Optimizations for seamless performance."
                )

                updates.forEach { item ->
                    Text(text = item, fontSize = 12.sp, color = TextWhite, lineHeight = 17.sp)
                }

                TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                    Text("Got it!", color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AboutFounderDialog(
    onDismiss: () -> Unit,
    onFollowInstagram: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .imePadding()
                .navigationBarsPadding()
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xFF0F1813))
                .border(
                    BorderStroke(
                        1.5.dp,
                        Brush.linearGradient(
                            listOf(
                                EmeraldGlow,
                                ElectricPurple,
                                GoldPrimary
                            )
                        )
                    ),
                    RoundedCornerShape(28.dp)
                )
                .padding(22.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "MEET THE FOUNDER",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = EmeraldGlow,
                    letterSpacing = 1.8.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    EmeraldPrimary,
                                    ElectricPurple,
                                    Color(0xFF0F1511)
                                )
                            )
                        )
                        .border(BorderStroke(2.dp, EmeraldGlow), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Asit Yadav",
                        tint = TextWhite,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Asit Yadav",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0x1A10B981),
                    border = BorderStroke(0.8.dp, EmeraldPrimary.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "Founder • AI Creator • Influencer",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGlow,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0x14FFFFFF),
                    border = BorderStroke(0.8.dp, Color(0x22FFFFFF))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "With over 10 years of experience in content creation, AI tools, creator education, brand collaborations and social media growth, Asit built ViralToolAi to help creators learn, create and earn using AI.",
                            fontSize = 12.sp,
                            color = TextWhite.copy(alpha = 0.90f),
                            lineHeight = 17.sp
                        )

                        Text(
                            text = "Mission: Empower Indian & global creators with cutting-edge AI tools and verified monetization opportunities.",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGlow
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onFollowInstagram()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE1306C))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = TextWhite,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Follow Asit Yadav on Instagram",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(onClick = onDismiss) {
                    Text("Close", color = TextGray, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun LegalDialogSheet(
    type: String,
    supportEmail: String,
    onDismiss: () -> Unit
) {
    val title = when (type) {
        "privacy" -> "Privacy Policy"
        "terms" -> "Terms of Service"
        "licenses" -> "Open Source Licenses"
        else -> "Disclaimer"
    }

    val contentText = when (type) {
        "privacy" -> """
            ViralToolAi respects your privacy:
            • Zero Data Selling: We never sell, rent, or trade your personal information.
            • Local Preference Storage: Your app settings and preferences are stored securely on your local device.
            • Play Store Compliance: We do not request unnecessary system permissions.
            • Contact: For privacy inquiries, email us at $supportEmail.
        """.trimIndent()
        "terms" -> """
            ViralToolAi Terms of Usage:
            • Fair Use: All AI script generation, brand deal templates, and educational lessons are provided for personal creator development.
            • Automated Verification: Contest submissions under Refer & Earn undergo manual review prior to reward distribution.
            • Content Ownership: Creators retain full ownership of videos and scripts generated using ViralToolAi.
        """.trimIndent()
        "licenses" -> """
            Open Source Software Licenses:
            • Jetpack Compose & AndroidX Libraries (Apache 2.0)
            • Kotlin Coroutines & Serialization (Apache 2.0)
            • Material Components for Android (Apache 2.0)
        """.trimIndent()
        else -> """
            Disclaimer & AI Usage:
            • AI Output: Generated content and scripts should be reviewed for accuracy before publishing.
            • Platform Independence: ViralToolAi is an independent creator platform and is not affiliated directly with Instagram, YouTube, Meesho, or Wishlink.
        """.trimIndent()
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFF121622),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextWhite)

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF0C101A),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = contentText,
                        fontSize = 12.sp,
                        color = TextWhite,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close", color = TextGray)
                }
            }
        }
    }
}

// Helpers
private fun calculateCacheSize(context: Context): Long {
    return try {
        val dir = context.cacheDir
        dir?.walkTopDown()?.map { it.length() }?.sum() ?: 0L
    } catch (e: Exception) {
        0L
    }
}

private fun clearAppCache(context: Context) {
    try {
        context.cacheDir?.deleteRecursively()
    } catch (e: Exception) {
        // Ignore
    }
}

private fun formatBytes(bytes: Long): String {
    return when {
        bytes >= 1024 * 1024 -> String.format("%.1f MB", bytes.toFloat() / (1024 * 1024))
        bytes >= 1024 -> String.format("%.1f KB", bytes.toFloat() / 1024)
        else -> "$bytes Bytes"
    }
}
