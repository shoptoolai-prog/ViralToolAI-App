package com.example.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Verified
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
 * MASTER PHASE 15E — Insta Auto DM AI Premium Card & Dialog
 * Locked Premium Automation Suite placed below Universal Shopping Analyzer
 * and above Instagram Shopping AI on the Home Screen.
 */

@Composable
fun InstaAutoDmAiCard(
    onComingSoonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "instaDmCardScale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "robotPulse")
    val robotGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "robotGlow"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = EmeraldPrimary.copy(alpha = 0.5f),
                ambientColor = Color(0x10000000)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF131A24),
                        Color(0xFF0D1117)
                    )
                )
            )
            .border(
                BorderStroke(
                    1.2.dp,
                    Brush.linearGradient(
                        listOf(EmeraldPrimary.copy(alpha = 0.5f), Color(0x1AFFFFFF))
                    )
                ),
                RoundedCornerShape(24.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onComingSoonClick()
                }
            )
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Icon + Title + Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Small Robot Icon Container
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0x2210B981))
                            .border(
                                BorderStroke(1.2.dp, EmeraldPrimary.copy(alpha = robotGlowAlpha)),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = "Robot Icon",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Insta Auto DM AI",
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite,
                                letterSpacing = 0.4.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Automated Comment & DM Product Delivery",
                            fontSize = 11.5.sp,
                            color = EmeraldPrimary.copy(alpha = 0.9f),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Badges Column
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(EmeraldPrimary)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "PREMIUM",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Black,
                            color = AmoledBlack,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x2210B981))
                            .border(BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)), RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "🔒 COMING SOON",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Short Description
            Text(
                text = "Automatically send product links and custom replies to people who comment on your Instagram posts and reels.",
                fontSize = 12.sp,
                color = TextWhite.copy(alpha = 0.8f),
                lineHeight = 16.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Trigger Keywords Preview Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("LINK 🔗", "PRICE 💰", "BUY 🛍️", "AMAZON 📦").forEach { tag ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x14FFFFFF))
                            .border(BorderStroke(0.8.dp, Color(0x22FFFFFF)), RoundedCornerShape(8.dp))
                            .padding(vertical = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tag,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite.copy(alpha = 0.9f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom Bar: Lock Status
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
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "Under Development (v1.0 Foundation)",
                        fontSize = 10.5.sp,
                        color = TextWhite.copy(alpha = 0.6f)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "View Specs",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                }
            }
        }
    }
}

/**
 * INSTA AUTO DM AI — PREMIUM POPUP DIALOG
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InstaAutoDmAiDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val prefs = remember { context.getSharedPreferences("premium_early_access", Context.MODE_PRIVATE) }
    var isNotified by remember { mutableStateOf(prefs.getBoolean("insta_auto_dm_ai", false)) }

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
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.82f))
                .clickable(onClick = onDismiss)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color(0xFF10101C),
                border = BorderStroke(
                    1.5.dp,
                    Brush.linearGradient(
                        listOf(EmeraldPrimary, Color(0x33FFFFFF))
                    )
                ),
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .fillMaxWidth()
                    .clickable(enabled = false) {}
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
                                    imageVector = Icons.Default.SmartToy,
                                    contentDescription = "Insta Auto DM AI",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "Insta Auto DM AI",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "Premium Automation Suite",
                                    fontSize = 11.5.sp,
                                    color = EmeraldPrimary,
                                    fontWeight = FontWeight.SemiBold
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
                                    text = "🔒 COMING SOON",
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

                    // WHAT THIS TOOL WILL DO
                    Text(
                        text = "🤖 WHAT THIS TOOL WILL DO",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(
                            "Automatically reply to comments",
                            "Send Instagram DM instantly",
                            "Trigger using custom keywords"
                        ).forEach { item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Check",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = item,
                                    fontSize = 12.sp,
                                    color = TextWhite,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Keyword Examples Box
                    Text(
                        text = "Supported Trigger Keywords:",
                        fontSize = 11.sp,
                        color = TextWhite.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(
                            "Comment LINK", "Comment PRICE", "Comment BUY", "Comment DEAL",
                            "Comment AMAZON", "Comment MEESHO", "Comment PRODUCT",
                            "+ Custom Keywords Supported"
                        ).forEach { kw ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x1FFFFFFF))
                                    .border(BorderStroke(0.8.dp, EmeraldPrimary.copy(alpha = 0.4f)), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = kw,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // AUTOMATION FEATURES
                    Text(
                        text = "⚡ AUTOMATION FEATURES",
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
                        listOf(
                            "Unlimited DM Templates",
                            "Smart Delay",
                            "Anti-Spam Safety",
                            "Custom Welcome Message",
                            "Product Link Delivery",
                            "Coupon Delivery",
                            "Multiple Product Support",
                            "Auto Reply Analytics",
                            "Daily Reply Statistics",
                            "AI Reply Suggestions",
                            "Smart Keyword Detection",
                            "Creator Inbox Management"
                        ).forEach { feat ->
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

                    Spacer(modifier = Modifier.height(18.dp))

                    // WHO SHOULD USE THIS
                    Text(
                        text = "🎯 WHO SHOULD USE THIS",
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
                        listOf(
                            "Meesho Creators",
                            "Amazon Influencers",
                            "Affiliate Marketers",
                            "Instagram Review Pages",
                            "Product Review Creators",
                            "Small Businesses",
                            "Shopping Pages",
                            "UGC Creators"
                        ).forEach { audience ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x0FFFFFF))
                                    .border(BorderStroke(0.8.dp, EmeraldPrimary.copy(alpha = 0.3f)), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "• $audience",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextWhite.copy(alpha = 0.9f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // FUTURE PREMIUM PLANS PREVIEW
                    Text(
                        text = "💎 FUTURE PREMIUM PLANS PREVIEW",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(
                            "Starter" to "7 Days Trial",
                            "Creator" to "15 Days",
                            "Professional" to "30 Days",
                            "Enterprise" to "Unlimited"
                        ).forEach { (planName, trial) ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x12FFFFFF))
                                    .border(BorderStroke(0.8.dp, Color(0x22FFFFFF)), RoundedCornerShape(12.dp))
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = planName,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = trial,
                                        fontSize = 9.5.sp,
                                        color = EmeraldPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // EARLY ACCESS SECTION
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
                                        prefs.edit().putBoolean("insta_auto_dm_ai", isNotified).apply()

                                        if (isNotified) {
                                            Toast.makeText(
                                                context,
                                                "You're on the Early Access waiting list. 🚀",
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
