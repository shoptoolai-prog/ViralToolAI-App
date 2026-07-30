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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.window.DialogProperties
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ProfileScreen(
    onSwitchExperience: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val founderInstagramUrl = "https://www.instagram.com/asittttttttttttttttt?igsh=bjJlN3M2N3hzMWI1"
    val officialInstagramUrl = "https://www.instagram.com/viraltoolai?igsh=MXJjN2Q5ODJhd3RobQ=="
    val supportEmail = "asityadavteambusiness@gmail.com"

    val openUrl = { url: String ->
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
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
        Toast.makeText(context, "Copied email: $supportEmail", Toast.LENGTH_SHORT).show()
    }

    val openMailClient = { subject: String ->
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        try {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$supportEmail")
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }
            context.startActivity(emailIntent)
        } catch (e: Exception) {
            copyEmailToClipboard()
        }
    }

    var showFounderDialog by remember { mutableStateOf(false) }
    var showBugReportDialog by remember { mutableStateOf(false) }
    var activeLegalSheet by remember { mutableStateOf<String?>(null) }

    // Entrance Stagger Animations
    var isLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isLoaded = true
    }

    val headerAlpha by animateFloatAsState(
        targetValue = if (isLoaded) 1f else 0f,
        animationSpec = tween(400, delayMillis = 50, easing = FastOutSlowInEasing),
        label = "headerAlpha"
    )
    val headerOffsetY by animateDpAsState(
        targetValue = if (isLoaded) 0.dp else 16.dp,
        animationSpec = tween(400, delayMillis = 50, easing = FastOutSlowInEasing),
        label = "headerOffsetY"
    )

    val accountCardAlpha by animateFloatAsState(
        targetValue = if (isLoaded) 1f else 0f,
        animationSpec = tween(400, delayMillis = 130, easing = FastOutSlowInEasing),
        label = "accountCardAlpha"
    )
    val accountCardOffsetY by animateDpAsState(
        targetValue = if (isLoaded) 0.dp else 16.dp,
        animationSpec = tween(400, delayMillis = 130, easing = FastOutSlowInEasing),
        label = "accountCardOffsetY"
    )

    val followCardAlpha by animateFloatAsState(
        targetValue = if (isLoaded) 1f else 0f,
        animationSpec = tween(400, delayMillis = 200, easing = FastOutSlowInEasing),
        label = "followCardAlpha"
    )
    val followCardOffsetY by animateDpAsState(
        targetValue = if (isLoaded) 0.dp else 16.dp,
        animationSpec = tween(400, delayMillis = 200, easing = FastOutSlowInEasing),
        label = "followCardOffsetY"
    )

    val supportCardAlpha by animateFloatAsState(
        targetValue = if (isLoaded) 1f else 0f,
        animationSpec = tween(400, delayMillis = 270, easing = FastOutSlowInEasing),
        label = "supportCardAlpha"
    )
    val supportCardOffsetY by animateDpAsState(
        targetValue = if (isLoaded) 0.dp else 16.dp,
        animationSpec = tween(400, delayMillis = 270, easing = FastOutSlowInEasing),
        label = "supportCardOffsetY"
    )

    val legalCardAlpha by animateFloatAsState(
        targetValue = if (isLoaded) 1f else 0f,
        animationSpec = tween(400, delayMillis = 340, easing = FastOutSlowInEasing),
        label = "legalCardAlpha"
    )
    val legalCardOffsetY by animateDpAsState(
        targetValue = if (isLoaded) 0.dp else 16.dp,
        animationSpec = tween(400, delayMillis = 340, easing = FastOutSlowInEasing),
        label = "legalCardOffsetY"
    )

    val aboutCardAlpha by animateFloatAsState(
        targetValue = if (isLoaded) 1f else 0f,
        animationSpec = tween(400, delayMillis = 410, easing = FastOutSlowInEasing),
        label = "aboutCardAlpha"
    )
    val aboutCardOffsetY by animateDpAsState(
        targetValue = if (isLoaded) 0.dp else 16.dp,
        animationSpec = tween(400, delayMillis = 410, easing = FastOutSlowInEasing),
        label = "aboutCardOffsetY"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        // Multi-layer Ambient Blur Background Gradients & Floating Light Particles
        BackgroundFloatingParticles()

        // Top Ambient Glow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            EmeraldPrimary.copy(alpha = 0.16f),
                            ElectricPurple.copy(alpha = 0.09f),
                            Color.Transparent
                        ),
                        center = Offset(400f, -50f)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ================= 1. HERO SECTION: ABOUT VIRALTOOLAI & PREMIUM MEMBERSHIP =================
            PremiumHeroHeader(
                alpha = headerAlpha,
                offsetY = headerOffsetY
            )

            Spacer(modifier = Modifier.height(22.dp))

            // ================= 2. ACCOUNT SECTION (FIXED: Tap opens About Founder) =================
            PremiumGlassSectionCard(
                title = "ACCOUNT",
                subtitle = "User Profile & Founder Information",
                headerIcon = Icons.Default.Person,
                headerIconTint = EmeraldPrimary,
                alpha = accountCardAlpha,
                offsetY = accountCardOffsetY
            ) {
                PremiumPressableRow(
                    title = "About Founder",
                    subtitle = "Asit Yadav • Founder, AI Creator & Influencer",
                    icon = Icons.Default.AccountCircle,
                    iconTint = EmeraldPrimary,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showFounderDialog = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= 3. FOLLOW US SECTION (TWO GLASS CARDS) =================
            PremiumGlassSectionCard(
                title = "FOLLOW US",
                subtitle = "Connect with Founder & Official Handles",
                headerIcon = Icons.Default.Share,
                headerIconTint = Color(0xFFE1306C),
                alpha = followCardAlpha,
                offsetY = followCardOffsetY
            ) {
                // Founder Instagram
                FollowInstagramCardRow(
                    handleName = "Asit Yadav",
                    subtitle = "@asittttttttttttttttt • Founder & AI Creator",
                    onClick = { openUrl(founderInstagramUrl) }
                )

                SoftGlassDivider()

                // ViralToolAI Official
                FollowInstagramCardRow(
                    handleName = "ViralToolAI Official",
                    subtitle = "@viraltoolai • Official AI Creator Hub",
                    onClick = { openUrl(officialInstagramUrl) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= 4. SUPPORT SECTION =================
            PremiumGlassSectionCard(
                title = "SUPPORT",
                subtitle = "We're here to help",
                headerIcon = Icons.Default.SupportAgent,
                headerIconTint = EmeraldPrimary,
                alpha = supportCardAlpha,
                offsetY = supportCardOffsetY
            ) {
                // Contact Support Row
                PremiumPressableRow(
                    title = "Contact Support",
                    subtitle = supportEmail,
                    icon = Icons.Default.Email,
                    iconTint = EmeraldPrimary,
                    trailingContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x1A10B981))
                                    .border(BorderStroke(0.8.dp, EmeraldPrimary.copy(alpha = 0.4f)), RoundedCornerShape(10.dp))
                                    .clickable { copyEmailToClipboard() }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy Email",
                                        tint = EmeraldGlow,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "Copy",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldGlow
                                    )
                                }
                            }
                        }
                    },
                    onClick = { openMailClient("ViralToolAI Support Request") }
                )

                SoftGlassDivider()

                // Report Bug Row
                PremiumPressableRow(
                    title = "Report a Bug",
                    subtitle = supportEmail,
                    icon = Icons.Default.BugReport,
                    iconTint = CrimsonLight,
                    trailingContent = {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x1AF43F5E))
                                .border(BorderStroke(0.8.dp, CrimsonLight.copy(alpha = 0.4f)), RoundedCornerShape(10.dp))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    showBugReportDialog = true
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Report",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CrimsonLight
                            )
                        }
                    },
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showBugReportDialog = true
                    }
                )

                SoftGlassDivider()

                // Switch Workspace Experience
                PremiumPressableRow(
                    title = "Switch Workspace Experience",
                    subtitle = "Shopping Intelligence vs Creator Academy",
                    icon = Icons.Default.SwapHoriz,
                    iconTint = EmeraldGlow,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSwitchExperience()
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= 5. LEGAL SECTION =================
            PremiumGlassSectionCard(
                title = "LEGAL",
                subtitle = "Privacy & Security",
                headerIcon = Icons.Default.Gavel,
                headerIconTint = CrimsonLight,
                alpha = legalCardAlpha,
                offsetY = legalCardOffsetY
            ) {
                PremiumPressableRow(
                    title = "Privacy Policy",
                    subtitle = "Zero Data Selling & Local Security",
                    icon = Icons.Default.Security,
                    iconTint = EmeraldPrimary,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        activeLegalSheet = "privacy"
                    }
                )

                SoftGlassDivider()

                PremiumPressableRow(
                    title = "Terms of Service",
                    subtitle = "Usage Terms & Community Rules",
                    icon = Icons.Default.Description,
                    iconTint = TextWhite,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        activeLegalSheet = "terms"
                    }
                )

                SoftGlassDivider()

                PremiumPressableRow(
                    title = "Licenses",
                    subtitle = "Open Source Software & Libraries",
                    icon = Icons.Default.Code,
                    iconTint = ElectricPurple,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        activeLegalSheet = "licenses"
                    }
                )

                SoftGlassDivider()

                PremiumPressableRow(
                    title = "Disclaimer",
                    subtitle = "AI Content Generation & Price Data Policy",
                    icon = Icons.Default.AutoAwesome,
                    iconTint = Color(0xFFFFB703),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        activeLegalSheet = "disclaimer"
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= 6. ABOUT VIRALTOOLAI SECTION =================
            PremiumGlassSectionCard(
                title = "ABOUT VIRALTOOLAI",
                subtitle = "Application Information",
                headerIcon = Icons.Default.Info,
                headerIconTint = ElectricPurple,
                alpha = aboutCardAlpha,
                offsetY = aboutCardOffsetY
            ) {
                // Detailed About Text Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x12FFFFFF))
                        .border(BorderStroke(0.8.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "ViralToolAI is an AI-powered creator platform designed to help creators learn, create, shop smarter and grow faster.",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextWhite,
                            lineHeight = 18.sp
                        )
                        Text(
                            text = "From shopping intelligence to creator education and video editing, every feature is built to simplify the creator journey using AI.",
                            fontSize = 12.sp,
                            color = TextWhite.copy(alpha = 0.70f),
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                PremiumPressableRow(
                    title = "Version",
                    subtitle = "v1.0.0 Stable • Production Build",
                    icon = Icons.Default.SystemUpdateAlt,
                    iconTint = EmeraldGlow
                )

                SoftGlassDivider()

                PremiumPressableRow(
                    title = "Founder & Creator",
                    subtitle = "Built with ❤️ by Asit Yadav",
                    icon = Icons.Default.Code,
                    iconTint = ElectricPurple,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showFounderDialog = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ================= FOOTER =================
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Made with ❤️ by Asit Yadav",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "ViralToolAI v1.0.0 • Nothing OS Glass Edition",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextWhite.copy(alpha = 0.40f),
                    letterSpacing = 0.6.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // ================= DIALOGS & SHEETS =================

        // About Founder Dialog
        if (showFounderDialog) {
            AboutFounderDialog(
                onDismiss = { showFounderDialog = false },
                onFollowInstagram = { openUrl(founderInstagramUrl) }
            )
        }

        // Bug Report Dialog
        if (showBugReportDialog) {
            ReportBugDialog(
                onDismiss = { showBugReportDialog = false },
                onSend = { title, desc ->
                    openMailClient("ViralToolAI Bug Report: $title\n\n$desc")
                }
            )
        }

        // Legal Sheet Dialog
        activeLegalSheet?.let { sheetType ->
            LegalDialogSheet(
                type = sheetType,
                supportEmail = supportEmail,
                onDismiss = { activeLegalSheet = null }
            )
        }
    }
}

// ================= HERO HEADER COMPONENT =================

@Composable
private fun PremiumHeroHeader(
    alpha: Float,
    offsetY: Dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "heroAnims")

    // Aurora Rotation Angle
    val auroraRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "auroraRotation"
    )

    // Floating animation
    val floatingY by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatingY"
    )

    // Shine sweep offset
    val sweepOffset by infiniteTransition.animateFloat(
        initialValue = -200f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweepOffset"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.alpha = alpha
                translationY = offsetY.toPx()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Section Title
        Text(
            text = "ABOUT VIRALTOOLAI",
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            color = EmeraldGlow,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Glass Membership Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = floatingY
                }
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x2E1B2B21),
                            Color(0x18101A14),
                            Color(0x2A152219)
                        )
                    )
                )
                .border(
                    BorderStroke(
                        1.8.dp,
                        Brush.sweepGradient(
                            colors = listOf(
                                EmeraldGlow,
                                ElectricPurple,
                                Color(0xFF2ECC71),
                                EmeraldPrimary,
                                EmeraldGlow
                            )
                        )
                    ),
                    RoundedCornerShape(28.dp)
                )
                .padding(22.dp)
        ) {
            // Glass Light Shine Effect
            Canvas(modifier = Modifier.matchParentSize()) {
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.12f),
                            Color.Transparent
                        ),
                        start = Offset(sweepOffset, 0f),
                        end = Offset(sweepOffset + 120f, size.height)
                    )
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                // Top Badge Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "✨ Premium Membership",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextWhite,
                            letterSpacing = 0.5.sp
                        )
                    }

                    // Badge: Selected User
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF10B981), Color(0xFF059669))
                                )
                            )
                            .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = TextWhite,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Selected User",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite,
                                letterSpacing = 0.4.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Headline
                Text(
                    text = "FREE ACCESS GRANTED",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = EmeraldGlow,
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Body text
                Text(
                    text = "You are one of the selected users enjoying complete Premium access at no cost. Enjoy every AI feature without restrictions.",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextWhite.copy(alpha = 0.85f),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// ================= FOLLOW INSTAGRAM ROW =================

@Composable
private fun FollowInstagramCardRow(
    handleName: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "igRowScale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .clip(RoundedCornerShape(18.dp))
            .background(if (isPressed) Color(0x22FFFFFF) else Color(0x0AFFFFFF))
            .border(
                BorderStroke(
                    0.8.dp,
                    Brush.linearGradient(
                        listOf(
                            Color(0xFFE1306C).copy(alpha = 0.4f),
                            Color(0xFFF77737).copy(alpha = 0.3f)
                        )
                    )
                ),
                RoundedCornerShape(18.dp)
            )
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            // Instagram Icon Badge
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF833AB4),
                                Color(0xFFFD1D1D),
                                Color(0xFFFCB045)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = handleName,
                    tint = TextWhite,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column {
                Text(
                    text = handleName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhite,
                    letterSpacing = 0.2.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextWhite.copy(alpha = 0.65f)
                )
            }
        }

        // Follow Pill Button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF833AB4),
                            Color(0xFFE1306C)
                        )
                    )
                )
                .border(BorderStroke(0.8.dp, Color.White.copy(alpha = 0.5f)), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Follow",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }
    }
}

// ================= ABOUT FOUNDER DIALOG =================

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
                                Color(0xFF2ECC71)
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
                // Header Title
                Text(
                    text = "MEET THE FOUNDER",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = EmeraldGlow,
                    letterSpacing = 1.8.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Avatar Icon Box
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
                        .border(
                            BorderStroke(2.dp, EmeraldGlow),
                            CircleShape
                        ),
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

                // Name & Role
                Text(
                    text = "Asit Yadav",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x1A10B981))
                        .border(BorderStroke(0.8.dp, EmeraldPrimary.copy(alpha = 0.4f)), RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Founder • AI Creator • Influencer",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGlow
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x14FFFFFF))
                        .border(BorderStroke(0.8.dp, Color(0x22FFFFFF)), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "With over 10 years of experience in content creation, AI tools, creator education, brand collaborations and social media growth, Asit built ViralToolAI to help beginners become professional creators through one simple app.",
                            fontSize = 12.sp,
                            color = TextWhite.copy(alpha = 0.90f),
                            lineHeight = 17.sp
                        )

                        Text(
                            text = "Worked with multiple brands and creator campaigns.",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextWhite.copy(alpha = 0.75f)
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = EmeraldGlow,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Mission: Help creators learn, create and earn using AI.",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGlow
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Instagram Button
                Button(
                    onClick = {
                        onFollowInstagram()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF833AB4),
                                        Color(0xFFE1306C),
                                        Color(0xFFFD1D1D)
                                    )
                                )
                            )
                            .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
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
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(onClick = onDismiss) {
                    Text("Close", color = TextGray, fontSize = 13.sp)
                }
            }
        }
    }
}

// ================= REUSABLE GLASS CARD & ROW COMPONENTS =================

@Composable
fun PremiumGlassSectionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    headerIcon: ImageVector,
    headerIconTint: Color = EmeraldPrimary,
    alpha: Float = 1f,
    offsetY: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                this.alpha = alpha
                translationY = offsetY.toPx()
            }
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x241A2820),
                        Color(0x14101813),
                        Color(0x1F1A241C)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0x35FFFFFF),
                            Color(0x10FFFFFF),
                            headerIconTint.copy(alpha = 0.25f),
                            Color(0x20FFFFFF)
                        )
                    )
                ),
                RoundedCornerShape(28.dp)
            )
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Section Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp, end = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(headerIconTint.copy(alpha = 0.15f))
                        .border(
                            BorderStroke(0.8.dp, headerIconTint.copy(alpha = 0.35f)),
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = headerIcon,
                        contentDescription = title,
                        tint = headerIconTint,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Column {
                    Text(
                        text = title.uppercase(),
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextWhite,
                        letterSpacing = 1.2.sp
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Text(
                            text = subtitle,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextWhite.copy(alpha = 0.5f),
                            letterSpacing = 0.3.sp
                        )
                    }
                }
            }

            // Divider Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                Color(0x1AFFFFFF),
                                headerIconTint.copy(alpha = 0.2f),
                                Color(0x1AFFFFFF),
                                Color.Transparent
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.height(10.dp))

            content()
        }
    }
}

@Composable
fun PremiumPressableRow(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    iconTint: Color = EmeraldPrimary,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.97f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "rowScale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .clip(RoundedCornerShape(16.dp))
            .background(if (isPressed && onClick != null) Color(0x1AFFFFFF) else Color.Transparent)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            // Glass Icon Badge
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                iconTint.copy(alpha = 0.18f),
                                Color(0x1AFFFFFF)
                            )
                        )
                    )
                    .border(
                        BorderStroke(
                            0.8.dp,
                            Brush.linearGradient(
                                listOf(
                                    iconTint.copy(alpha = 0.4f),
                                    Color(0x22FFFFFF)
                                )
                            )
                        ),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = title,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    letterSpacing = 0.2.sp
                )
                if (!subtitle.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextWhite.copy(alpha = 0.55f),
                        letterSpacing = 0.1.sp
                    )
                }
            }
        }

        if (trailingContent != null) {
            trailingContent()
        } else if (onClick != null) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextWhite.copy(alpha = 0.35f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun SoftGlassDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .height(1.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color.Transparent,
                        Color(0x0EFFFFFF),
                        Color(0x18FFFFFF),
                        Color(0x0EFFFFFF),
                        Color.Transparent
                    )
                )
            )
    )
}

@Composable
private fun BackgroundFloatingParticles() {
    val infiniteTransition = rememberInfiniteTransition(label = "bgParticles")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val numParticles = 18
        for (i in 0 until numParticles) {
            val cx = (sin((i * 1.8f + pulse * 1.2f).toDouble()) * 0.48f + 0.5f) * size.width
            val cy = (cos((i * 2.1f + pulse * 1.1f).toDouble()) * 0.48f + 0.5f) * size.height
            val radius = (1.8f + (i % 4) * 1.2f).dp.toPx()
            val particleColor = if (i % 2 == 0) EmeraldGlow else ElectricPurple
            drawCircle(
                color = particleColor.copy(
                    alpha = (0.10f + 0.12f * sin((i + pulse).toDouble())).toFloat().coerceIn(0.02f, 0.25f)
                ),
                radius = radius,
                center = Offset(cx.toFloat(), cy.toFloat())
            )
        }
    }
}

// ================= BUG REPORT DIALOG =================

@Composable
fun ReportBugDialog(
    onDismiss: () -> Unit,
    onSend: (title: String, description: String) -> Unit
) {
    var titleText by remember { mutableStateOf("") }
    var descText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false),
        modifier = Modifier.imePadding().navigationBarsPadding(),
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
                Text(
                    "Send Email",
                    color = if (titleText.isNotBlank() && descText.isNotBlank()) EmeraldPrimary else TextGray,
                    fontWeight = FontWeight.Bold
                )
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
                Text("Describe the bug or issue encountered. This will send an email directly to developer support.", fontSize = 12.sp, color = TextGray)

                OutlinedTextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    label = { Text("Bug Summary", fontSize = 12.sp, color = TextGray) },
                    placeholder = { Text("e.g. Issue during video export", fontSize = 12.sp, color = TextGray.copy(alpha = 0.5f)) },
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
                    label = { Text("Steps / Details", fontSize = 12.sp, color = TextGray) },
                    placeholder = { Text("Describe what happened...", fontSize = 12.sp, color = TextGray.copy(alpha = 0.5f)) },
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
        containerColor = Color(0xFF121A15),
        shape = RoundedCornerShape(28.dp)
    )
}

// ================= LEGAL DIALOG SHEET =================

@Composable
fun LegalDialogSheet(
    type: String,
    supportEmail: String,
    onDismiss: () -> Unit
) {
    val title = when (type) {
        "privacy" -> "Privacy Policy"
        "terms" -> "Terms of Service"
        "licenses" -> "Licenses & Open Source"
        else -> "Disclaimer"
    }

    val bodyContent = when (type) {
        "privacy" -> """
            ViralToolAI Privacy Policy
            
            1. Zero Data Selling
            ViralToolAI does not sell, rent, or commercialize your personal data to any third party.
            
            2. Local Processing & Storage
            All your creator settings, session data, and preferences are stored locally on your device.
            
            3. AI & Camera Permissions
            Camera and storage permissions are requested strictly for analyzing product images or video editing when initiated by you.
            
            4. Support Queries
            Support inquiries sent to $supportEmail are used strictly to resolve technical issues and improve app experience.
        """.trimIndent()

        "terms" -> """
            ViralToolAI Terms of Service
            
            1. Acceptance of Terms
            By downloading or using ViralToolAI, you agree to comply with these terms.
            
            2. Creator & Educational Purpose
            ViralToolAI provides AI-assisted creator analysis, video editing guidance, and shopping price analytics for informational & creative purposes.
            
            3. User Responsibility
            Users remain responsible for content generated, posted, or published to social media platforms.
            
            4. Community Standards
            Inappropriate, illegal, or abusive usage of AI models is strictly prohibited.
        """.trimIndent()

        "licenses" -> """
            ViralToolAI Open Source Licenses
            
            This application uses open source libraries and components:
            
            • Jetpack Compose (Apache 2.0)
            • AndroidX Libraries (Apache 2.0)
            • Kotlin Coroutines & Serialization (Apache 2.0)
            • Material Design 3 Components (Apache 2.0)
            • Coil Image Loading (Apache 2.0)
            
            Full license texts available upon request.
        """.trimIndent()

        else -> """
            ViralToolAI Disclaimer & Notice
            
            1. AI Content Generation
            AI recommendations, creator score estimates, and hook suggestions are generated via machine learning algorithms and should be used as guidance.
            
            2. Shopping & Price Intelligence
            Prices, discounts, and cashback values are aggregated estimates subject to seller availability and real-time merchant changes.
            
            3. Social Media Affiliation
            ViralToolAI is an independent creation tool and is not officially affiliated with Instagram, Meta, YouTube, or TikTok.
        """.trimIndent()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false),
        modifier = Modifier.imePadding().navigationBarsPadding(),
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Got It", color = EmeraldPrimary, fontWeight = FontWeight.Bold)
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Gavel, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextWhite)
            }
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 320.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = bodyContent,
                    fontSize = 12.5.sp,
                    color = TextWhite.copy(alpha = 0.85f),
                    lineHeight = 18.sp
                )
            }
        },
        containerColor = Color(0xFF121A15),
        shape = RoundedCornerShape(28.dp)
    )
}
