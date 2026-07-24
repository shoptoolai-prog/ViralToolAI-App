package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.core.AiSessionMemory
import com.example.reports.ReportLanguage
import com.example.ui.components.GlassCard
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    onSwitchExperience: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val instagramUrl = "https://www.instagram.com/asittttttttttttttttt?igsh=bjJlN3M2N3hzMWI1"

    val openInstagram = {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl)).apply {
                setPackage("com.instagram.android")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))
                context.startActivity(browserIntent)
            } catch (e2: Exception) {
                Toast.makeText(context, "Opening Instagram...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val supportEmail = "asityadavteambusiness@gmail.com"

    val getDiagnosticInfo = {
        """
        
        -------------------------------------------
        App Version: ViralToolAI v1.0.0 Stable
        Android Version: SDK ${android.os.Build.VERSION.SDK_INT} (${android.os.Build.VERSION.RELEASE})
        Device Model: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}
        -------------------------------------------
        """.trimIndent()
    }

    val sendEmail = { subject: String, bodyText: String ->
        try {
            val fullBody = "$bodyText\n${getDiagnosticInfo()}"
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$supportEmail")
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, fullBody)
            }
            context.startActivity(emailIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Contact: $supportEmail", Toast.LENGTH_LONG).show()
        }
    }

    val openEmailSupport = {
        sendEmail("ViralToolAI Support Request", "Hi Asit,\n\nI need help with ViralToolAI:\n")
    }

    val openRateApp = {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        Toast.makeText(context, "Thank you for rating ViralToolAI! ❤️", Toast.LENGTH_LONG).show()
    }

    var showBugReportDialog by remember { mutableStateOf(false) }
    var showFeatureSuggestDialog by remember { mutableStateOf(false) }
    var showHelpFaqDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var activeLegalSheet by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ================= HEADER =================
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = GlassCardBorder,
                backgroundColor = GlassCardBg
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    // Large Circular Avatar
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(96.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(EmeraldPrimary.copy(alpha = 0.5f), Color.Transparent)
                                    ),
                                    CircleShape
                                )
                        )

                        Box(
                            modifier = Modifier
                                .size(84.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            EmeraldPrimary,
                                            ElectricPurple,
                                            Color(0xFF0F0F16)
                                        )
                                    )
                                )
                                .border(
                                    BorderStroke(
                                        2.dp,
                                        Brush.sweepGradient(
                                            listOf(EmeraldGlow, ElectricPurple, EmeraldPrimary, EmeraldGlow)
                                        )
                                    ),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Asit Avatar",
                                tint = TextWhite,
                                modifier = Modifier.size(46.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = (-4).dp, y = (-2).dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF2ECC71), Color(0xFF27AE60))
                                    )
                                )
                                .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = TextWhite,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = "Creator",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Asit",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        letterSpacing = 0.8.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Creator & Developer",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CrimsonLight,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= 1. ABOUT SECTION =================
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = GlassCardBorder,
                backgroundColor = GlassCardBg
            ) {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "About",
                            tint = CrimsonLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "ABOUT",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "ViralToolAI",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = com.example.core.TaglineEngine.getTagline(com.example.core.AppModule.PROFILE),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = CrimsonLight
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x2210B981))
                                .border(BorderStroke(1.dp, EmeraldPrimary), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "v1.0.0",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = EmeraldPrimary
                            )
                        }

                        Text(
                            text = "Production Release • Stable",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextGray
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Created by Asit ❤️",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= 2. SUPPORT & CONTACT SECTION =================
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = GlassCardBorder,
                backgroundColor = GlassCardBg
            ) {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = "Support & Contact",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "SUPPORT & CONTACT",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    var notificationsEnabled by remember { mutableStateOf(true) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                            Column {
                                Text("AI Insights Notifications", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                Text(if (notificationsEnabled) "Receiving deal & viral trend alerts" else "Notifications muted", fontSize = 10.sp, color = TextGray)
                            }
                        }
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                notificationsEnabled = it
                                Toast.makeText(context, if (it) "Notifications Enabled" else "Notifications Muted", Toast.LENGTH_SHORT).show()
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = AmoledBlack,
                                checkedTrackColor = EmeraldPrimary,
                                uncheckedThumbColor = TextGray,
                                uncheckedTrackColor = Color(0x22FFFFFF)
                            )
                        )
                    }

                    HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                    SettingRow(
                        label = "Contact Support",
                        subLabel = "asityadavteambusiness@gmail.com",
                        icon = Icons.Default.Email,
                        onClick = openEmailSupport
                    )

                    HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                    SettingRow(
                        label = "Report a Bug",
                        subLabel = "Send bug report directly to developer",
                        icon = Icons.Default.BugReport,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            showBugReportDialog = true
                        }
                    )

                    HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                    SettingRow(
                        label = "Suggest a Feature",
                        subLabel = "Propose new ideas & improvements",
                        icon = Icons.Default.Lightbulb,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            showFeatureSuggestDialog = true
                        }
                    )

                    HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                    SettingRow(
                        label = "Help & FAQ",
                        subLabel = "Common questions & app usage guide",
                        icon = Icons.Default.HelpOutline,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            showHelpFaqDialog = true
                        }
                    )

                    HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                    SettingRow(
                        label = "About ViralToolAI",
                        subLabel = "Version 1.0 • App Identity & Credits",
                        icon = Icons.Default.Info,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            showAboutDialog = true
                        }
                    )

                    HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                    SettingRow(
                        label = "Switch Workspace Experience",
                        subLabel = "Shopping Intelligence vs Creator Academy",
                        icon = Icons.Default.SwapHoriz,
                        onClick = onSwitchExperience
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= 3. LANGUAGE SECTION =================
            var selectedLanguage by remember { mutableStateOf(AiSessionMemory.currentLanguage) }

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = GlassCardBorder,
                backgroundColor = GlassCardBg
            ) {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "LANGUAGE",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val languages = listOf(
                        ReportLanguage.ENGLISH to "English (Default)",
                        ReportLanguage.HINGLISH to "HinEnglish",
                        ReportLanguage.HINDI to "Hindi (हिंदी)"
                    )

                    languages.forEachIndexed { index, (lang, label) ->
                        val isSelected = selectedLanguage == lang
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0x2210B981) else Color.Transparent)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedLanguage = lang
                                    com.example.core.LanguageEngine.setLanguage(context, lang)
                                    Toast.makeText(context, "Language set to ${lang.displayName}", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = label,
                                fontSize = 13.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) EmeraldPrimary else TextWhite
                            )

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        if (index < languages.size - 1) {
                            HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= 4. LEGAL SECTION =================
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = GlassCardBorder,
                backgroundColor = GlassCardBg
            ) {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = "Legal",
                            tint = CrimsonLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "LEGAL",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    SettingRow(
                        label = "Privacy Policy",
                        subLabel = "Zero Data Selling & Local Security",
                        icon = Icons.Default.Security,
                        onClick = { activeLegalSheet = "privacy" }
                    )

                    HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                    SettingRow(
                        label = "Terms of Service",
                        subLabel = "Usage Terms & Rules",
                        icon = Icons.Default.Description,
                        onClick = { activeLegalSheet = "terms" }
                    )

                    HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                    SettingRow(
                        label = "Licenses",
                        subLabel = "Open Source Software",
                        icon = Icons.Default.Code,
                        onClick = { activeLegalSheet = "licenses" }
                    )

                    HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                    SettingRow(
                        label = "Disclaimer",
                        subLabel = "Price Policy & Content Estimates",
                        icon = Icons.Default.AutoAwesome,
                        onClick = { activeLegalSheet = "disclaimer" }
                    )
                }
            }

            // Dialogs
            if (showBugReportDialog) {
                ReportBugDialog(
                    onDismiss = { showBugReportDialog = false },
                    onSend = { title, desc ->
                        sendEmail("ViralToolAI Bug Report: $title", "Bug Title: $title\n\nDescription:\n$desc")
                    }
                )
            }

            if (showFeatureSuggestDialog) {
                SuggestFeatureDialog(
                    onDismiss = { showFeatureSuggestDialog = false },
                    onSend = { idea ->
                        sendEmail("ViralToolAI Feature Suggestion", "Feature Idea:\n$idea")
                    }
                )
            }

            if (showHelpFaqDialog) {
                HelpFaqDialog(
                    onDismiss = { showHelpFaqDialog = false }
                )
            }

            if (showAboutDialog) {
                AboutAppDialog(
                    onDismiss = { showAboutDialog = false },
                    supportEmail = supportEmail,
                    onOpenInstagram = openInstagram
                )
            }

            // Render active legal sheet
            activeLegalSheet?.let { sheetType ->
                LegalDialogSheet(
                    type = sheetType,
                    supportEmail = supportEmail,
                    onDismiss = { activeLegalSheet = null }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ================= FOOTER =================
            Text(
                text = "ViralToolAI • From Products to Popularity",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = TextWhite.copy(alpha = 0.45f),
                letterSpacing = 1.2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

@Composable
fun SettingRow(
    label: String,
    subLabel: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(icon, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
            Column {
                Text(label, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(subLabel, fontSize = 10.sp, color = TextGray)
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextGray, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun ReportBugDialog(
    onDismiss: () -> Unit,
    onSend: (title: String, description: String) -> Unit
) {
    var titleText by remember { mutableStateOf("") }
    var descText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (titleText.isNotBlank() && descText.isNotBlank()) {
                        onSend(titleText.trim(), descText.trim())
                        onDismiss()
                    }
                },
                enabled = titleText.isNotBlank() && descText.isNotBlank()
            ) {
                Text("Send Email", color = if (titleText.isNotBlank() && descText.isNotBlank()) EmeraldPrimary else TextGray, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextGray)
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.BugReport, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(22.dp))
                Text("Report a Bug", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextWhite)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Help us improve ViralToolAI by describing the issue encountered.", fontSize = 12.sp, color = TextGray)

                OutlinedTextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    label = { Text("Bug Title", fontSize = 12.sp, color = TextGray) },
                    placeholder = { Text("e.g. Price comparison timeout", fontSize = 12.sp, color = TextGray.copy(alpha = 0.5f)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descText,
                    onValueChange = { descText = it },
                    label = { Text("Description", fontSize = 12.sp, color = TextGray) },
                    placeholder = { Text("Describe what happened and steps to reproduce...", fontSize = 12.sp, color = TextGray.copy(alpha = 0.5f)) },
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        containerColor = Color(0xFF141A16),
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun SuggestFeatureDialog(
    onDismiss: () -> Unit,
    onSend: (idea: String) -> Unit
) {
    var ideaText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (ideaText.isNotBlank()) {
                        onSend(ideaText.trim())
                        onDismiss()
                    }
                },
                enabled = ideaText.isNotBlank()
            ) {
                Text("Send Email", color = if (ideaText.isNotBlank()) EmeraldPrimary else TextGray, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextGray)
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(22.dp))
                Text("Suggest a Feature", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextWhite)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Have a great idea for ViralToolAI? We'd love to hear it!", fontSize = 12.sp, color = TextGray)

                OutlinedTextField(
                    value = ideaText,
                    onValueChange = { ideaText = it },
                    label = { Text("Feature Idea", fontSize = 12.sp, color = TextGray) },
                    placeholder = { Text("Describe the tool or workflow you want added...", fontSize = 12.sp, color = TextGray.copy(alpha = 0.5f)) },
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary,
                        unfocusedBorderColor = Color(0x33FFFFFF),
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        containerColor = Color(0xFF141A16),
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun HelpFaqDialog(
    onDismiss: () -> Unit
) {
    val faqs = listOf(
        "What is ViralToolAI?" to "ViralToolAI is a premium AI-powered platform for smart shopping price comparison, deal intelligence, and creator content virality analysis.",
        "How does Price Intelligence work?" to "Paste product URLs or upload screenshots from Amazon, Flipkart, Myntra, Meesho, or Nike. ViralToolAI scans verified live deals, stock, and coupon discounts.",
        "What is the Creator Academy?" to "Creator Academy gives creators AI-driven viral hook generators, media kit builders, trend trackers, and campaign proposal tools to grow their audience and land brand deals.",
        "Is my data private?" to "Yes. Search history and creator media kits stay saved locally on your device in secure storage."
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Got It", color = EmeraldPrimary, fontWeight = FontWeight.Bold)
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.HelpOutline, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(22.dp))
                Text("Help & FAQ", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextWhite)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                faqs.forEach { (q, a) ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x1AFFFFFF))
                            .padding(12.dp)
                    ) {
                        Text(q, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(a, fontSize = 11.5.sp, color = TextGray, lineHeight = 16.sp)
                    }
                }
            }
        },
        containerColor = Color(0xFF141A16),
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun AboutAppDialog(
    onDismiss: () -> Unit,
    supportEmail: String,
    onOpenInstagram: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .clickable(onClick = onDismiss)
                .padding(vertical = 24.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 680.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF162019), Color(0xFF0B120D))
                        )
                    )
                    .border(
                        BorderStroke(
                            1.2.dp,
                            Brush.linearGradient(
                                listOf(
                                    EmeraldPrimary.copy(alpha = 0.8f),
                                    Color(0x33FFFFFF)
                                )
                            )
                        ),
                        RoundedCornerShape(28.dp)
                    )
                    .clickable(enabled = false) {}
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header Bar
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
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "About ViralToolAI",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // App Identity Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x1AFFFFFF))
                            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF1B2820), Color(0xFF0D1610))
                                        )
                                    )
                                    .border(1.2.dp, EmeraldPrimary, RoundedCornerShape(18.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_viraltool_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(44.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text("ViralToolAI", fontSize = 20.sp, fontWeight = FontWeight.Black, color = TextWhite)
                            Text("From Products to Popularity", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("VERSION", fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = TextGray)
                                    Text("1.0 Stable", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("DEVELOPER", fontSize = 9.5.sp, fontWeight = FontWeight.Black, color = TextGray)
                                    Text("Created by Asit", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // CARD 1: Meet the Creator
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x1A10B981))
                            .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f)), RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                                Text("Meet the Creator", fontSize = 15.sp, fontWeight = FontWeight.Black, color = TextWhite)
                            }

                            Text("👋 Hi, I'm Asit Yadav.", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)

                            Text(
                                "I'm a 20-year-old independent developer from India.\n\n" +
                                "ViralToolAI is my dream project.\n\n" +
                                "I built this application entirely on mobile using AI-powered development tools.\n\n" +
                                "Over the past 10 years, I've explored social media, creator growth, content strategy, and digital products. Every lesson I've learned is being transformed into practical tools that help creators and shoppers save time, grow faster, and make better decisions.\n\n" +
                                "This is only the beginning.\n\n" +
                                "Many more premium features, AI tools, and completely new applications are already planned for the future.\n\n" +
                                "My goal is to build products that genuinely help millions of people while creating a trusted technology brand around the name \"Asit\".\n\n" +
                                "Thank you for being part of this journey.",
                                fontSize = 12.sp,
                                color = TextWhite.copy(alpha = 0.9f),
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // CARD 2: Future Vision
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x1AFFFFFF))
                            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                                Text("Future Vision", fontSize = 15.sp, fontWeight = FontWeight.Black, color = TextWhite)
                            }

                            val visionList = listOf(
                                "More AI Creator Tools",
                                "Advanced Shopping Intelligence",
                                "Premium Creator Academy",
                                "Powerful Business Automation",
                                "New Play Store Applications",
                                "Building a trusted global technology brand"
                            )

                            visionList.forEach { item ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                                    Text(item, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextWhite)
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                "\"The best is yet to come.\"",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // CARD 3: Follow Asit
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x1AFFFFFF))
                            .border(BorderStroke(1.dp, Color(0x22FFFFFF)), RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                                Text("Follow Asit", fontSize = 15.sp, fontWeight = FontWeight.Black, color = TextWhite)
                            }

                            Text("Stay updated with new tools, app updates and creator content.", fontSize = 12.sp, color = TextGray)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(EmeraldPrimary, EmeraldGlow)
                                        )
                                    )
                                    .clickable { onOpenInstagram() },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = AmoledBlack, modifier = Modifier.size(18.dp))
                                    Text("Open Instagram", fontSize = 13.sp, fontWeight = FontWeight.Black, color = AmoledBlack)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // CARD 4: Thank You
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0x2210B981), Color(0x1110B981))
                                )
                            )
                            .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)), RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Thank You ❤️", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextWhite)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Every download, every suggestion, and every piece of feedback helps improve ViralToolAI.\n\nThank you for supporting an independent developer.",
                                fontSize = 12.sp,
                                color = TextGray,
                                textAlign = TextAlign.Center,
                                lineHeight = 17.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Close Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color(0x22FFFFFF))
                            .border(BorderStroke(1.dp, Color(0x33FFFFFF)), RoundedCornerShape(22.dp))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Close", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
    }
}

@Composable
fun LegalDialogSheet(
    type: String,
    supportEmail: String = "asityadavteambusiness@gmail.com",
    onDismiss: () -> Unit
) {
    val title = when (type) {
        "privacy" -> "Privacy Policy"
        "terms" -> "Terms of Service"
        "licenses" -> "Open Source Licenses"
        "disclaimer" -> "Disclaimer"
        else -> "Legal Notice"
    }

    val contentText = when (type) {
        "privacy" -> """
            ViralToolAI ("the App") is committed to protecting your privacy.
            
            1. Zero Data Selling: We never sell, rent, or trade your personal data, shopping history, or creator analytics to third parties.
            
            2. Minimal Processing: Screenshots and text queries are processed solely to extract shopping products or creator metrics.
            
            3. Local Security & Storage: All saved history, creator kits, and favorites remain strictly stored in local secure storage on your device.
            
            4. User Control: You can clear your entire search and report history anytime from the History tab.
            
            5. Contact Support: For privacy questions or data deletion requests, contact $supportEmail.
        """.trimIndent()

        "terms" -> """
            Terms of Service for ViralToolAI
            
            1. Acceptable Use: You agree to use ViralToolAI for lawful shopping research and content strategy purposes.
            
            2. Intellectual Property: ViralToolAI and its custom intelligence models are protected by copyright. Merchant logos belong to their respective owners.
            
            3. Price & Deal Disclaimer: Prices, coupons, and stock availability are extracted in real-time from official merchant listings and may change rapidly. Always verify final checkout prices at merchant portals.
            
            4. Account & Local Storage: Your saved items are stored on-device. Back up your important data prior to uninstalling the app.
        """.trimIndent()

        "licenses" -> """
            Open Source Licenses & Attribution
            
            • Jetpack Compose & AndroidX (Apache 2.0)
            • Kotlin Coroutines & Flow (Apache 2.0)
            • Android Room Database (Apache 2.0)
            • Retrofit & OkHttp — Square, Inc. (Apache 2.0)
            • Coil Image Loading — Coil Contributors (Apache 2.0)
            • Material Symbols & Icons — Google LLC (Apache 2.0)
        """.trimIndent()

        "disclaimer" -> """
            Disclaimer & Usage Policy
            
            1. Estimates & Predictions: ViralScore, Creator Health Index, and Virality Predictions are generated for guidance and strategy.
            
            2. Verified Stores: Price comparison only compares against official, verified merchant listings. Unverified or suspicious deals are filtered automatically.
            
            3. Non-Financial Advice: ViralToolAI insights are strategic suggestions and do not constitute financial, investment, or legal guarantees.
        """.trimIndent()

        else -> "ViralToolAI Production Release v1.0"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = EmeraldPrimary, fontWeight = FontWeight.Bold)
            }
        },
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = contentText,
                    fontSize = 12.sp,
                    color = TextGray,
                    lineHeight = 18.sp
                )
            }
        },
        containerColor = Color(0xFF141A16),
        titleContentColor = TextWhite,
        textContentColor = TextGray,
        shape = RoundedCornerShape(20.dp)
    )
}


