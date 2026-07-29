package com.example.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.draw.shadow
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.ElectricPurple
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite

/**
 * MASTER PHASE 15D (UPDATED) — Premium Creator Tools Marketplace
 * Contains 3 Locked Premium Mentorship Tools:
 * 1. Brand Collaboration AI
 * 2. Affiliate Master AI
 * 3. Video Editing Academy
 *
 * All tools display COMING SOON status with a common Apple Glass style popup dialog.
 */

data class PremiumToolData(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val badge: String = "COMING SOON",
    val overview: String,
    val features: List<String>,
    val futurePricePreview: String? = null,
    val launchStatus: String = "Under Active Development (v1.0 Foundation)"
)

object PremiumCreatorMarketplaceData {
    val tools = listOf(
        PremiumToolData(
            id = "brand_collab_ai",
            title = "Brand Collaboration AI",
            subtitle = "Sponsorship Deals, Pitching, Media Kit & Contracts",
            icon = Icons.Default.Campaign,
            overview = "An AI mentor dedicated to helping creators pitch top brands, calculate market pricing, design press kits, negotiate usage rights, and avoid deal scams.",
            features = listOf(
                "Personal Brand Strategy",
                "Brand Pitch Generator",
                "Instagram DM Templates",
                "Professional Email Templates",
                "Brand Outreach Roadmap",
                "Brand Matching Guidance",
                "Scam Detection Guide",
                "Negotiation Guide",
                "Collaboration Checklist"
            ),
            futurePricePreview = "Included in Pro Pass"
        )
    )
}

@Composable
fun PremiumCreatorToolsSection(
    onToolSelected: (PremiumToolData) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "⭐",
                    fontSize = 16.sp
                )
                Text(
                    text = "PREMIUM CREATOR TOOLS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    letterSpacing = 1.2.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x2210B981))
                    .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "UNLOCKED",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tool Cards List
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            PremiumCreatorMarketplaceData.tools.forEach { tool ->
                PremiumToolGlassCard(
                    tool = tool,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onToolSelected(tool)
                    }
                )
            }
        }
    }
}

@Composable
fun PremiumToolGlassCard(
    tool: PremiumToolData,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "premiumCardScale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "brandCardShimmer")
    val borderPulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "borderPulseAlpha"
    )
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    val bulletFeatures = listOf(
        "High-Converting Brand Pitch Templates",
        "Instagram DM & Email Outreach Scripts",
        "Sponsorship Pricing & Rate Calculator",
        "Brand Deal Contract & Scam Checker",
        "Custom Media Kit & Portfolio Builder"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .materialSharedBounds("premium_tool_card_${tool.id}")
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = EmeraldPrimary.copy(alpha = 0.55f),
                ambientColor = Color(0x10000000)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF18221D),
                        Color(0xFF0C1410)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.2.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            EmeraldPrimary.copy(alpha = borderPulseAlpha),
                            Color(0xFF00E676),
                            Color.White.copy(alpha = 0.25f)
                        ),
                        start = androidx.compose.ui.geometry.Offset(shimmerOffset, 0f),
                        end = androidx.compose.ui.geometry.Offset(shimmerOffset + 350f, 250f)
                    )
                ),
                RoundedCornerShape(24.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClick = onClick
            )
            .padding(18.dp)
    ) {
        // Glass Shine Sweep Overlay
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(24.dp))
        ) {
            val sweepX = shimmerOffset
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.02f),
                        Color.White.copy(alpha = 0.1f),
                        Color.White.copy(alpha = 0.02f),
                        Color.Transparent
                    )
                ),
                start = androidx.compose.ui.geometry.Offset(sweepX, 0f),
                end = androidx.compose.ui.geometry.Offset(sweepX + 160f, size.height),
                strokeWidth = 24.dp.toPx()
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Row Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimary.copy(alpha = 0.18f))
                            .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        com.example.ui.screens.OfficialLogo(name = "instagram", modifier = Modifier.size(20.dp))
                    }

                    Column {
                        Text(
                            text = tool.title,
                            fontSize = 17.5.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = (-0.3).sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "SPONSORSHIPS, MEDIA KITS & CONTRACTS",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = com.example.ui.theme.EmeraldGlow,
                            letterSpacing = 1.2.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x2210B981))
                        .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "✨ FEATURED",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.ui.theme.EmeraldGlow,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 5 Bullet Features
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                bulletFeatures.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Check,
                                contentDescription = null,
                                tint = com.example.ui.theme.EmeraldGlow,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                        Text(
                            text = feature,
                            fontSize = 12.5.sp,
                            color = TextWhite.copy(alpha = 0.85f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Large Premium Pill Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(23.dp),
                        spotColor = EmeraldPrimary
                    )
                    .clip(RoundedCornerShape(23.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(EmeraldPrimary, com.example.ui.theme.EmeraldGlow)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.PlayArrow,
                        contentDescription = "Open Tool",
                        tint = AmoledBlack,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "OPEN TOOL",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Black,
                        color = AmoledBlack,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }
    }
}

/**
 * COMMON PREMIUM POPUP DIALOG
 * Used by all 3 Premium Creator Mentorship Tools
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommonPremiumToolPopupDialog(
    tool: PremiumToolData,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val prefs = remember { context.getSharedPreferences("premium_early_access", Context.MODE_PRIVATE) }
    var isNotified by remember { mutableStateOf(prefs.getBoolean(tool.id, false)) }

    val infiniteTransition = rememberInfiniteTransition(label = "lockGlow")
    val lockPulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lockPulse"
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
                .navigationBarsPadding()
                .imePadding()
        ) {
            Surface(
                color = Color(0xFF0F1A14),
                modifier = Modifier
                    .fillMaxSize()
                    .materialSharedBounds("premium_tool_card_${tool.id}")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x2210B981))
                                    .border(BorderStroke(1.dp, EmeraldPrimary), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = tool.icon,
                                    contentDescription = tool.title,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = tool.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = tool.subtitle,
                                    fontSize = 11.sp,
                                    color = TextWhite.copy(alpha = 0.6f)
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextWhite.copy(alpha = 0.6f),
                            modifier = Modifier.clickable { onDismiss() }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Lock & Badge Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x1410B981))
                            .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f)), RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .graphicsLayer {
                                        scaleX = lockPulse
                                        scaleY = lockPulse
                                    }
                                    .clip(CircleShape)
                                    .background(Color(0x2210B981)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Lock",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(EmeraldPrimary)
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "🔒 ${tool.badge}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = AmoledBlack,
                                    letterSpacing = 1.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "This Premium Tool is currently under development. Version 1.0 includes the foundation. A future update will unlock this feature.",
                                fontSize = 11.5.sp,
                                color = TextWhite.copy(alpha = 0.85f),
                                textAlign = TextAlign.Center,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Overview Section
                    Text(
                        text = "📌 Overview",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tool.overview,
                        fontSize = 12.sp,
                        color = TextWhite.copy(alpha = 0.9f),
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Features Preview Grid
                    Text(
                        text = "✨ Features Preview",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tool.features.forEach { feat ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x14FFFFFF))
                                    .border(BorderStroke(0.8.dp, Color(0x22FFFFFF)), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Check",
                                        tint = EmeraldPrimary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = feat,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextWhite
                                    )
                                }
                            }
                        }
                    }

                    if (tool.futurePricePreview != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "🏷️ Future Price Preview",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Expected Launch Price: ${tool.futurePricePreview}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Early Access Section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x0CFFFFFF))
                            .border(BorderStroke(1.dp, Color(0x1AFFFFFF)), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "🚀 Early Access users will receive exclusive launch pricing.",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextWhite,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(if (isNotified) Color(0x3310B981) else EmeraldPrimary)
                                    .border(
                                        BorderStroke(1.dp, EmeraldPrimary),
                                        RoundedCornerShape(22.dp)
                                    )
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        isNotified = !isNotified
                                        prefs.edit().putBoolean(tool.id, isNotified).apply()

                                        if (isNotified) {
                                            Toast.makeText(
                                                context,
                                                "You're on the Early Access waiting list! 🚀",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Removed from Early Access waiting list.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = if (isNotified) Icons.Default.CheckCircle else Icons.Default.NotificationsActive,
                                        contentDescription = "Notify Me",
                                        tint = if (isNotified) EmeraldPrimary else AmoledBlack,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isNotified) "✓ Added to Early Access List" else "🔔 Notify Me",
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isNotified) EmeraldPrimary else AmoledBlack
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
